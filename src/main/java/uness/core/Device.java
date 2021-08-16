package uness.core;

import java.text.ParseException;
import java.util.ArrayList;

import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.DBManagerInterface.Access;
import unessdb.FIELD;
import unessdb.TABLE;
import uness.LANG;
import uness.Uness;
import unessdb.UData;
import unessdb.usql.USQLParams;

public class Device {
	
	private final DBManagerInterface acMan;
	private final Core core;
	
	public Device(final Core p_core, final DBManagerInterface p_acMan)
	{
		acMan = p_acMan;
		core = p_core;
	}

	public final ArrayList<UData> searchDevices(final USQLParams params) throws DBException
	{
		return acMan.select(TABLE.DEVICE.getFields(), true, params);
	}
	
	public final ArrayList<UData> searchDevices(final USQLParams params, final FIELD order, final Access sort) throws DBException
	{
		return acMan.select(TABLE.DEVICE.getFields(), true, params, order, sort);
	}
	
	/**
	 * Simply return device data using serial
	 * @param serial
	 * @return UData contain the first device found or null if not found
	 * @throws DBException
	 */
	public final UData getDevice(final String serial) throws DBException
	{
		USQLParams filter = new USQLParams(FIELD.DEVICE_serial, serial);
		ArrayList<UData> datas = acMan.select(TABLE.DEVICE.getFields(), true, filter);
		if(datas.isEmpty()) return null;
		else return datas.get(0);
	}
	public final ArrayList<UData> getDevices(final USQLParams params) throws DBException
	{
		ArrayList<UData> datas = acMan.select(TABLE.DEVICE.getFields(), true, params);

		return datas;
	}
	public final int addDevice(final UData device) throws DBException, ParseException
	{
		//Serial Pattern Test
		
		String serial = String.valueOf(device.getValue(FIELD.DEVICE_serial));
		String modelName = String.valueOf(device.getValue(FIELD.DEVICE_model));
		if( ! core.getModelManager().testSerial(modelName, serial)) throw new DBException(LANG.ERROR_SERIAL_PATTERN.get() +  serial + ", " + modelName);
		
		//Test Location presence, insert Void if not
		String location = String.valueOf(device.getValue(FIELD.DEVICE_location));
		if(location == null || location.equals("null")) device.add(FIELD.DEVICE_location, "Void");

		return acMan.insert(device.getFields(), false, device);
	}
	public final int updateDevice(final UData device, final USQLParams target) throws DBException, ParseException
	{
		//Serial Pattern Test
		String modelName = String.valueOf(device.getValue(FIELD.DEVICE_model));
		String serial = String.valueOf(device.getValue(FIELD.DEVICE_serial));
		
		if( ! core.getModelManager().testSerial(modelName, serial)) throw new DBException(LANG.ERROR_SERIAL_PATTERN.get() +  serial + ", " + modelName);
		
		return acMan.update(device.getFields(), false, target, device);
	}

	/**
	 * Delete a device
	 * @param tableId int Table id of the selected item 
	 */
	public final int remove(final UData deviceToRemove) throws DBException
	{
		int retVal = 0;
		String deviceId = String.valueOf(deviceToRemove.getValue(FIELD.DEVICE_id));
	
		USQLParams targetLog = new USQLParams(FIELD.LOG_device, deviceId); 
		acMan.delete(FIELD.LOG_device, false, targetLog);
		
		USQLParams targetDevice = new USQLParams(FIELD.DEVICE_id, deviceId);
		retVal = acMan.delete(FIELD.DEVICE_id, false, targetDevice);
		//Reset list
		
		return retVal;
		
	}
}
