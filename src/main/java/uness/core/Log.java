package uness.core;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import uness.Uness;
import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.UDataException;
import unessdb.usql.USQLParams;
import unessdb.DBManagerInterface.Access;
import uness.core.print.LogBean;

public class Log {

	private static Logger logger = LogManager.getLogger();
	private final DBManagerInterface acMan;

	public Log(final DBManagerInterface p_acMan)
	{
		acMan = p_acMan;
	}

	public final ArrayList<UData> getLogs(UData selectedDevice, FIELD sort, Access sens) throws DBException {
		int intDeviceId = (Integer)selectedDevice.getValue(FIELD.DEVICE_id);
		String deviceId = String.valueOf(intDeviceId);

		USQLParams filter = new USQLParams(FIELD.LOG_device, deviceId);
		
		return acMan.select(TABLE.LOG.getFields(), true, filter, sort, sens);
	}
	
	/**
	 * 
	 * @param log
	 * @return The number of row that was inserted
	 * @throws DBException
	 */
	public final int newLog(final UData log) throws DBException
	{
		return acMan.insert(log.getFields(), false, log);
	}
	
	public final int updateLog(final UData data, final USQLParams id) throws DBException {
		return acMan.update(data.getFields(), false, id, data);
	}

	public final int remove(final UData logToRemove) throws DBException {
		USQLParams target = new USQLParams(FIELD.LOG_id, logToRemove.getIdValue());
		return acMan.delete(FIELD.LOG_id, false, target);
	}

	public void printLog(UData selectedDevice){
		// Init dataset
		HashMap<String, Object> device = new HashMap<String, Object>();
		// Prepare log array
		ArrayList<UData> logsData = null;
		try {
			logsData = getLogs(selectedDevice, FIELD.LOG_datetime, Access.DESC);
		} catch (DBException e1) {
			logger.error(e1);
			e1.printStackTrace();
		}
		ArrayList<LogBean> logs = new ArrayList<LogBean>();
		for (UData log : logsData) {
			logs.add(new LogBean(log));
		}
		// Prepare device info
		device.put(FIELD.DEVICE_model.toString(), (String) selectedDevice.getValue(FIELD.DEVICE_model));
		device.put(FIELD.DEVICE_location.toString(), (String) selectedDevice.getValue(FIELD.DEVICE_location));
		device.put(FIELD.DEVICE_serial.toString(), (String) selectedDevice.getValue(FIELD.DEVICE_serial));
		device.put(FIELD.DEVICE_comment.toString(), (String) selectedDevice.getValue(FIELD.DEVICE_comment));

		JRDataSource beans = new JRBeanCollectionDataSource(logs);
		InputStream templateStream = this.getClass().getResourceAsStream(Uness.logTemplate);
		
		try {
			Report.print(templateStream, device, beans);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public static final int send(final DBManagerInterface acMan, final String comment, final UData deviceAssocied, final UData userAssocied) 
			throws DBException, UDataException {
				
				FIELD[] minFields = new FIELD[] {FIELD.LOG_device, FIELD.LOG_datetime, FIELD.LOG_comment, FIELD.LOG_author};
				
				UData data = new UData();
				data.put(FIELD.LOG_device, String.valueOf(deviceAssocied.getValue(FIELD.DEVICE_id)));
				data.put(FIELD.LOG_datetime, new SimpleDateFormat(Uness.DATE_FORMAT).format(System.currentTimeMillis()));
				data.put(FIELD.LOG_comment, comment);
				data.put(FIELD.LOG_author, String.valueOf(userAssocied.getValue(FIELD.USER_fullname)));
				
				return acMan.insert(minFields, false, data);
			}

}
