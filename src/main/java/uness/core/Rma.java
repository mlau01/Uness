package uness.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.jasperreports.engine.JRException;
import uness.Uness;
import uness.core.Rma.RMA_STATUS;
import uness.gui.UWin;
import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.UDataException;
import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQLParam;
import unessdb.usql.USQLParams;

public class Rma {
	
	public enum RMA_STATUS {
		NEW,RETURN
	}
	
	private static Logger logger = LogManager.getLogger();

	private final DBManagerInterface acMan;
	private final Core core;

	public Rma(final Core p_core, final DBManagerInterface p_acMan) {
		acMan = p_acMan;
		core = p_core;
	}

	public final UData getFirstUnclosedRma(final UData selectedDevice) throws DBException {
		final USQLParams filter = new USQLParams(FIELD.RMA_device, selectedDevice.getValue(FIELD.DEVICE_id));
		
		final ArrayList<UData> selectResult = acMan.select(TABLE.RMA.getFields(), true, filter);
		if(selectResult.size() >= 1)
		{
			for(UData tuple : selectResult)
			{
				if(tuple.getValue(FIELD.RMA_status).equals(RMA_STATUS.NEW.toString()))
					return tuple;
			}
		}
		else if(selectResult.isEmpty())
			return null;

		
		return null;
	}
	
	public final UData create(final UData selectedUser, final UData selectedDevice, final UData datas, final UData existentDatas) throws DBException, UDataException
	{
		RMA_STATUS status;
		UData deviceUpdateDatas = new UData();
		String logComment;
		UData report = null;
		
		if(existentDatas == null){
			status = RMA_STATUS.NEW;
			acMan.insert(datas.getFields(), false, datas);
			deviceUpdateDatas.put(FIELD.DEVICE_location, "Constructor");
			logComment = "Départ chez constructeur";
			report = datas;
		}
		else {
			status = RMA_STATUS.RETURN;
			USQLParams target = new USQLParams(FIELD.RMA_device, selectedDevice.getValue(FIELD.DEVICE_id));
			acMan.update(datas.getFields(), false, target, datas);
			deviceUpdateDatas.put(FIELD.DEVICE_location, existentDatas.getValue(FIELD.RMA_devicelastlocation));
			logComment = "Retour du constructeur";
			report = existentDatas.update(datas);
		}
		
		USQLParams targetDevice = new USQLParams(FIELD.DEVICE_id, selectedDevice.getValue(FIELD.DEVICE_id));
		acMan.update(deviceUpdateDatas.getFields(), false, targetDevice, deviceUpdateDatas); 
		Log.send(acMan, logComment, selectedDevice, selectedUser);
		
		return report;
	}
	
	public final HashMap<String, Object> getPrintObject(UData rma)
	{
		USQLParams deviceParam = new USQLParams(FIELD.DEVICE_id, String.valueOf(rma.getValue(FIELD.RMA_device)));
		UData device = null;
		try {
			device = core.getDeviceManager().getDevices(deviceParam).get(0);
		} catch (DBException e) {
			e.printStackTrace();
			logger.error(e);
			return null;
		}
		HashMap<String, Object> printObject = new HashMap<String, Object>();
		printObject.put(FIELD.RMA_rmanumber.toString(), rma.getValue(FIELD.RMA_rmanumber));
		printObject.put(FIELD.DEVICE_model.toString(), device.getValue(FIELD.DEVICE_model));
		printObject.put(FIELD.DEVICE_serial.toString(), device.getValue(FIELD.DEVICE_serial));
		printObject.put(FIELD.RMA_sender.toString(), rma.getValue(FIELD.RMA_sender));
		printObject.put(FIELD.RMA_datesent.toString(), rma.getValue(FIELD.RMA_datesent));
		printObject.put(FIELD.RMA_travelvoucher.toString(), rma.getValue(FIELD.RMA_travelvoucher));
		printObject.put(FIELD.RMA_comment.toString(), rma.getValue(FIELD.RMA_comment));
		printObject.put(FIELD.RMA_catcher.toString(),  rma.getValue(FIELD.RMA_catcher));
		printObject.put(FIELD.RMA_datecatched.toString(), rma.getValue(FIELD.RMA_datecatched));
		
		
		return printObject;
	}
	
	public final ArrayList<UData> get(USQLParams params, FIELD sort, Access sens) throws DBException
	{
		ArrayList<UData> rmas;
		rmas = acMan.select(TABLE.RMA.getFields(), true, params, sort, sens);


		
		return rmas;
	}
	
	public final int remove(UData rma) throws DBException
	{
		USQLParams target = new USQLParams(FIELD.RMA_id, String.valueOf(rma.getValue(FIELD.RMA_id)));
		return acMan.delete(FIELD.RMA_id, false, target);
	}
	
	public void print(final RMA_STATUS mode) throws JRException
	{
		String template;
		if (mode == RMA_STATUS.NEW)
			template = Uness.sendRmaTemplate;
		else if (mode == RMA_STATUS.RETURN)
			template = Uness.returnRmaTemplate;

		HashMap<String, Object> dataset = new HashMap<String, Object>();
		// TODO
		/*
		dataset.put(FIELD.RMA_rmaNumber.toString(), selectedItem.getValue(FIELD.RMA_rmaNumber));
		dataset.put(FIELD.DEVICE_model.toString(), selectedItem.getValue(FIELD.RMA_device));
		dataset.put(FIELD.DEVICE_serial.toString(), selectedItem.getValue(FIELD.RMA_deviceSerial));
		dataset.put(FIELD.RMA_sentUser.toString(), selectedItem.getValue(FIELD.RMA_sentUser));
		dataset.put(FIELD.RMA_sentDate.toString(), selectedItem.getValue(FIELD.RMA_sentDate));
		dataset.put(FIELD.RMA_bonPorteur.toString(), selectedItem.getValue(FIELD.RMA_bonPorteur));
		dataset.put(FIELD.RMA_issue.toString(), selectedItem.getValue(FIELD.RMA_issue));
		dataset.put(FIELD.RMA_note.toString(), selectedItem.getValue(FIELD.RMA_note));
		
		if(mode == RMA_MODE.RMA_RETURN) {
			dataset.put(FIELD.RMA_retrieveBy.toString(),  selectedItem.getValue(FIELD.RMA_retrieveBy));
			dataset.put(FIELD.RMA_returnDate.toString(), selectedItem.getValue(FIELD.RMA_returnDate));
		}
		

		final InputStream templateStream = this.getClass().getResourceAsStream(template);
		Print.print(templateStream, dataset, null);
		*/
	}
}
