package uness.core;

import java.util.ArrayList;

import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.DBManagerInterface.Access;

public class Control {
	
	private final DBManagerInterface acMan;
	private short verboseLevel = 2;
	
	public Control(final DBManagerInterface p_acMan)
	{
		acMan = p_acMan;
	}
	
	
	public final ArrayList<UData> loadValues(final FIELD field) throws DBException
	{
		return acMan.select(new FIELD[] {field}, false, null, field, Access.ASC);
	}
	
	public final String[] getStringArray(final FIELD field) throws DBException
	{
		ArrayList<UData> datas = acMan.select(new FIELD[] {field}, false, null, field, Access.ASC);
		String[] array = new String[datas.size()];
		
		int index = 0;
		for(UData tupple : datas)
		{
			array[index] = String.valueOf(tupple.getValue(field));
			index++;
		}
		
		return array;
	}

}
