package uness.core;

import java.util.ArrayList;

import uness.Uness;
import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQLParams;

public class Model {
	
	private final DBManagerInterface dbMan;
	private final short verboseLevel = 2;
	
	public Model(final DBManagerInterface p_dbMan)
	{
		dbMan = p_dbMan;
	}
	
	public void addModel(final String model, final String maker, final String serial_prefix, final String comment) throws DBException
	{		
		UData datas = new UData();
		datas.add(FIELD.MODEL_model, model);
		datas.add(FIELD.MODEL_constructor, maker);
		datas.add(FIELD.MODEL_comment, comment);
		datas.add(FIELD.MODEL_serial_pattern, serial_prefix);
		
		dbMan.insert(datas.getFields(), false, datas);
	}
	public void editModel(final String id, final String model, final String maker, final String serial_pattern, final String comment) throws DBException
	{
		UData datas = new UData();
		datas.add(FIELD.MODEL_model, model);
		datas.add(FIELD.MODEL_constructor, maker);
		datas.add(FIELD.MODEL_serial_pattern, serial_pattern);
		datas.add(FIELD.MODEL_comment, comment);
		
		
		USQLParams params = new USQLParams(FIELD.MODEL_id, id);
		
		dbMan.update(datas.getFields(), false, params, datas);
	}
	public final ArrayList<UData> getModels(FIELD sort, Access sens) throws DBException
	{
		ArrayList<UData> modelData = dbMan.select(TABLE.MODEL.getFields(), false, null, sort, sens);
		
		return modelData;
	}
	
	public final String getMatchModel(String serial) throws DBException
	{
		ArrayList<UData> models = getModels(FIELD.MODEL_id, Access.ASC);
		for(UData model : models)
		{
			String serialPattern = String.valueOf(model.getValue(FIELD.MODEL_serial_pattern));
			if(serialPattern.isEmpty()) continue;
			if(serial.matches(serialPattern)) return String.valueOf(model.getValue(FIELD.MODEL_model));
		}
		
		return null;
	}
	
	public final boolean testSerial(final String model, final String serial) throws DBException
	{
		if(serial.matches(Uness.NCPATTERN)) return true;
		if(model.toLowerCase().startsWith("desko") && ! serial.matches("[0-9]{6} [0-9]{5}")) return false;
		
		ArrayList<UData> models = dbMan.select(TABLE.MODEL.getFields(), true, new USQLParams(FIELD.MODEL_model, model));
		if(models.isEmpty()) return false;
		String serialPattern = String.valueOf(models.get(0).getValue(FIELD.MODEL_serial_pattern));
		if(serialPattern.isEmpty() || serial.matches(serialPattern)) return true;
		
		return false;
	}

}
