package uness.core;

import java.text.ParseException;
import java.util.ArrayList;

import uness.Uness;
import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.usql.USQL.DB;
import unessdb.usql.USQLParams;

public class Test {
	
	private final Core core;
	private final Device devMan;
	private final Log logMan;
	private ArrayList<UData> devices;
	private ArrayList<UData> logs;
	
	public Test() throws OptionException, DBException
	{
		core = new Core();
		devMan = core.getDeviceManager();
		logMan = core.getLogMan();
		
		DBManagerInterface dbMan = core.getAccessManager();
		dbMan.connect(DB.access, "D:\\Dev\\Projects\\Wessdb\\wess-0.1.7.accdb", "", "", "");
		System.out.println("Connected: " + dbMan.isConnected());
	}
	
	public Core getCore()
	{
		return core;
	}
	
	public static void main(String[] args) throws OptionException, DBException
	{

		Test test = new Test();
		
		//test.createDevice();
		test.selectDevice();
		//test.updateDevice();
		test.selectDevice();
		test.createLog();
		test.selectLog();
		//test.printLog();
		//test.deleteDevice();

	}
	
	public void createDevice() throws DBException, ParseException
	{
		UData newDevice = new UData();
		newDevice.add(FIELD.DEVICE_location, "T1-Spare");
		newDevice.add(FIELD.DEVICE_model, "IER 400");
		newDevice.add(FIELD.DEVICE_serial, "serialdetest");
		newDevice.add(FIELD.DEVICE_owner, "ACA");
		System.out.println("Inserting new device...");
		devMan.addDevice(newDevice);
	}
	
	public void selectDevice() throws DBException{
		System.out.println("Selecting device...");
		devices = devMan.searchDevices(new USQLParams(FIELD.DEVICE_serial, "serialdetest"));
		System.out.println(devices.get(0));
	}
	public void updateDevice() throws DBException, ParseException
	{
		System.out.println("Updating device...");
		UData newDatas = new UData();
		newDatas.add(FIELD.DEVICE_location, "T1-BQ-A01");
		int res = devMan.updateDevice(newDatas, new USQLParams(FIELD.DEVICE_serial, "serialdetest"));
		System.out.println("Removed device: " + res);
	}
	
	public void createLog() throws DBException {
		UData newLog = new UData();
		newLog.add(FIELD.LOG_device, devices.get(0).getValue(FIELD.DEVICE_id));
		newLog.add(FIELD.LOG_author, "Me");
		newLog.add(FIELD.LOG_comment, "Hello");
		newLog.add(FIELD.LOG_datetime, Uness.currentTime);
		
		logMan.newLog(newLog);
	}
	
	public void selectLog() throws DBException {
		//logs = logMan.getLogs(devices.get(0));
		//System.out.println(logs.get(0));
	}
	
	public void deleteDevice() throws DBException
	{
		System.out.println("Removing device...");
		int res = devMan.remove(devices.get(0));
		System.out.println("Removed device: " + res);
	}

}
