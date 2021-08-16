package unessdb;

import unessdb.usql.USQLManager;
import unessdb.usql.USQLParams;

import java.util.ArrayList;

import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQL.DB;

public class Test {
	
	public static void main(String args[]) throws DBException
	{
		DBManagerInterface dbman = new USQLManager();
		dbman.connect(DB.access, "D:\\Dev\\Projects\\Wessdb\\wess-0.2.1.accdb", "", "", "");
		
		FIELD[] devFields = new FIELD[] {FIELD.DEVICE_model, FIELD.DEVICE_location, FIELD.DEVICE_serial};
		USQLParams filter = new USQLParams(FIELD.DEVICE_location, "T1-Spare");
		ArrayList<UData> datas = dbman.select(devFields, true, filter, FIELD.DEVICE_location, Access.ASC);
		
		for(UData data : datas) {
			System.out.println(data);
		}
		
	}

}
