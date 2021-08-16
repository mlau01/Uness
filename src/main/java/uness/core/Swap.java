package uness.core;

import java.util.AbstractMap.SimpleEntry;
import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.UDataException;
import unessdb.usql.USQLParams;

public class Swap {

	private UData source;
	private UData target;
	private final DBManagerInterface acMan;

	public Swap(final DBManagerInterface p_acMan) {
		acMan = p_acMan;
	}

	public final int swap(final UData userSelected, final UData newSrc, final UData newTgt)
			throws DBException, UDataException {
		int success = 0;

		// Update source
		final UData sendSrcData = new UData();
		sendSrcData.put(FIELD.DEVICE_location, newSrc.getValue(FIELD.DEVICE_location));

		
		USQLParams targetid = new USQLParams(newSrc.getIdField(), newSrc.getIdValue());
		success += acMan.update(new FIELD[] { FIELD.DEVICE_location }, false, targetid, sendSrcData);
	

		// Update target
		if (target != null) {	
				final UData sendTgtData = new UData();
				sendTgtData.put(FIELD.DEVICE_location, newTgt.getValue(FIELD.DEVICE_location));	
				
				targetid = new USQLParams(newTgt.getIdField(), newTgt.getIdValue());
				success += acMan.update(new FIELD[] { FIELD.DEVICE_location }, false, targetid, sendTgtData);
		}

		// Set logs to update
		String noteSrc = "";
		noteSrc += "Vient de " + (String) source.getValue(FIELD.DEVICE_location);
		noteSrc += ", va en " + (String) newSrc.getValue(FIELD.DEVICE_location);
		if (target != null) {
			noteSrc += " et remplace " + (String) newTgt.getValue(FIELD.DEVICE_model);
			noteSrc += " S/N: " + (String) newTgt.getValue(FIELD.DEVICE_serial);
		}

		Log.send(acMan, noteSrc, newSrc, userSelected);
	

		// Upload target log
		if (target != null) {
			String noteTgt = "";
			noteTgt += "Vient de " + (String) target.getValue(FIELD.DEVICE_location);
			noteTgt += ", va en " + (String) source.getValue(FIELD.DEVICE_location);
			noteTgt += " et remplace " + (String) source.getValue(FIELD.DEVICE_model);
			noteTgt += " S/N: " + (String) source.getValue(FIELD.DEVICE_serial);
			
			Log.send(acMan, noteTgt, newTgt, userSelected);
		}

		return success;
	}

	// ---- Setters ----
	public void setSource(final UData p_source) {
		source = p_source;
	}

	public void setTarget(final UData p_target) {
		target = p_target;
	}

	// ---- GETTERS ----
	public final UData getSource() {
		return source;
	}

	public final UData getTarget() {
		return target;
	}

}
