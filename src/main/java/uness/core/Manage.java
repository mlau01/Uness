package uness.core;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.DBManagerInterface.Access;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.usql.USQLParams;

public class Manage {
	
	private final DBManagerInterface dbMan;
	private ArrayList<UData> locationData;
	private ArrayList<UData> groupData;
	private ArrayList<UData> modelData;
	private final short verboseLevel = 2;
	
	public Manage(final DBManagerInterface p_dbMan)
	{
		dbMan = p_dbMan;
	}
	

	public void addLocation(final String location, final String group, final String comment) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.LOCATION_location, location);
		datas.add(FIELD.LOCATION_group, group);
		datas.add(FIELD.LOCATION_comment, comment);
		dbMan.insert(datas.getFields(), false, datas);
	}
	
	public void editLocation(final String id, final String location, final String group, final String comment) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.LOCATION_location, location);
		datas.add(FIELD.LOCATION_group, group);
		datas.add(FIELD.LOCATION_comment, comment);
		
		USQLParams params = new USQLParams(FIELD.LOCATION_id, id);
		
		dbMan.update(datas.getFields(), false, params, datas);
	}
	public final ArrayList<UData> getLocations(USQLParams params, FIELD sort, Access sens) throws DBException
	{
		locationData = dbMan.select(TABLE.LOCATION.getFields(), true, params, sort, sens);
		
		return locationData;
	}
	
	
	public void addGroup(final String group, String parentGroup, final String comment) throws DBException
	{
		if(parentGroup.isEmpty())
			parentGroup = "0";
		
		UData datas = new UData();
		datas.add(FIELD.GROUP_group, group);
		datas.add(FIELD.GROUP_parentgroup, parentGroup);
		datas.add(FIELD.GROUP_comment, comment);
		
		dbMan.insert(datas.getFields(), false, datas);
	}
	
	public final ArrayList<UData> getGroups() throws DBException
	{
		groupData = dbMan.select(TABLE.GROUP.getFields(), false);
		
		return groupData;
	}
	
	public void addEnterprise(final String enterpriseName) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.ENTERPRISE_enterprise, enterpriseName);
		
		dbMan.insert(datas.getFields(), false, datas);
	}
	
	public void editEnterprise(final String id, final String enterpriseName) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.ENTERPRISE_enterprise, enterpriseName);
		
		USQLParams params = new USQLParams(FIELD.ENTERPRISE_id, id);
		
		dbMan.update(datas.getFields(), false, params, datas);
	}
	
	public final ArrayList<UData> getEnterprise() throws DBException
	{
		ArrayList<UData> enterpriseData = dbMan.select(TABLE.ENTERPRISE.getFields(), false);
		return enterpriseData;
	}

}
