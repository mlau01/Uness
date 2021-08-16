package uness.gui.device;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import uness.LANG;
import uness.Uness;
import uness.core.Device;
import uness.core.Model;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.gui.common.AbstractPopupPanel;
import uness.gui.common.ComboUtils;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.usql.USQLParams;

public class DeviceEditorPanel extends AbstractPopupPanel{
	
	private final UData selectedDevice;
	private UData device;
	
	private final Control ctls[];
	private static int LOCATION = 0;
	private static int MODEL = 1;
	private static int SERIAL = 2;
	private static int OWNER = 3;
	private static int IDCLIENT = 4;
	private static int COMMENT = 5;
	private static int PURCHASE = 6;
	private static int WARRANTY = 7;
	private static int NETWORKNAME = 8;
	private static int CTLS_SIZE = 9;
	
	private Button NCButton;
	private Listener serialFocusOutListener;
	private final Model modelManager;
	private final Device deviceManager;
	
	public DeviceEditorPanel(UWin p_uwin, UData p_selectedDevice)
	{
		super(p_uwin);
		initListeners();
		ctls = new Control[CTLS_SIZE];
		selectedDevice = p_selectedDevice;
		modelManager = p_uwin.getCorma().getModelManager();
		deviceManager = p_uwin.getCorma().getDeviceManager();
	}
	
	private void initListeners(){
		serialFocusOutListener = e -> {
			String serial = ((Text)ctls[SERIAL]).getText();
			String model = null;
			UData existingSerial = null;
			try {
				model = modelManager.getMatchModel(serial);
				existingSerial = deviceManager.getDevice(serial);
			} catch (DBException e1) {
				uwin.showError(e1.getClass().getName(), e1.getMessage());
				e1.printStackTrace();
				return;
			}
			
			if(existingSerial != null) uwin.showError(this.getClass().getName(), LANG.IMPORT_ERROR_DUPLICATE_SERIAL.get());
			if(model != null) ((Combo)ctls[MODEL]).setText(model);
		};
		
		
	}

	@Override
	protected void widgets() {
		GridData wdata = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		wdata.widthHint = 200;
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_location.getDisplayName());
		ctls[LOCATION] = new Combo(dialog, SWT.NONE);
		ctls[LOCATION].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_serial.getDisplayName());
		Composite c_serial = new Composite(dialog, SWT.NONE);
		{
			GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			c_serial.setLayout(layout);
			
			ctls[SERIAL] = new Text(c_serial, SWT.BORDER);
			ctls[SERIAL].setLayoutData(new GridData(120, 15));
			ctls[SERIAL].addListener(SWT.FocusOut, serialFocusOutListener);
			
			NCButton = new Button(c_serial, SWT.PUSH);
			NCButton.setText(LANG.DEVICE_EDITOR_NCBUTTON_TEXT.get());
			NCButton.setToolTipText(LANG.DEVICE_EDITOR_NCBUTTON_TOOLTIP.get());
		}
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_model.getDisplayName());
		ctls[MODEL] = new Combo(dialog, SWT.NONE);
		ctls[MODEL].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_owner.getDisplayName());
		ctls[OWNER] = new Combo(dialog, SWT.NONE);
		ctls[OWNER].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_idclient.getDisplayName());
		ctls[IDCLIENT] = new Text(dialog, SWT.BORDER);
		ctls[IDCLIENT].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_comment.getDisplayName());
		ctls[COMMENT] = new Text(dialog, SWT.BORDER);
		ctls[COMMENT].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_purchasedate.getDisplayName());
		
		ctls[PURCHASE] = new CDateTime(dialog, CDT.BORDER | CDT.COMPACT | CDT.DROP_DOWN | CDT.DATE_SHORT);
		ctls[PURCHASE].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_warrantyexpire.getDisplayName());
		ctls[WARRANTY] = new CDateTime(dialog, CDT.BORDER | CDT.COMPACT | CDT.DROP_DOWN | CDT.DATE_SHORT);
		ctls[WARRANTY].setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.DEVICE_networkName.getDisplayName());
		ctls[NETWORKNAME] = new Text(dialog, SWT.BORDER);
		ctls[NETWORKNAME].setLayoutData(wdata);
		
		
	}

	@Override
	protected void loadDatas() {
		String[] locations = null;
		String[] models = null;
		String[] owners = null;
		
		try {
			locations = uwin.getCorma().getControlManager().getStringArray(FIELD.LOCATION_location);
			models = uwin.getCorma().getControlManager().getStringArray(FIELD.MODEL_model);
			owners = uwin.getCorma().getControlManager().getStringArray(FIELD.ENTERPRISE_enterprise);
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
		
		((Combo)ctls[LOCATION]).setItems(locations);
		((Combo)ctls[MODEL]).setItems(models);
		((Combo)ctls[OWNER]).setItems(owners);
		
		if(selectedDevice != null)
		{
			ComboUtils.selectComboItem((Combo)ctls[LOCATION], String.valueOf(selectedDevice.getValue(FIELD.DEVICE_location)));
			ComboUtils.selectComboItem((Combo)ctls[MODEL], String.valueOf(selectedDevice.getValue(FIELD.DEVICE_model)));
			ComboUtils.selectComboItem((Combo)ctls[OWNER], String.valueOf(selectedDevice.getValue(FIELD.DEVICE_owner)));
			
			((Text)ctls[SERIAL]).setText(String.valueOf(selectedDevice.getValue(FIELD.DEVICE_serial)));
			((Text)ctls[IDCLIENT]).setText(String.valueOf(selectedDevice.getValue(FIELD.DEVICE_idclient)));
			((Text)ctls[COMMENT]).setText(String.valueOf(selectedDevice.getValue(FIELD.DEVICE_comment)));
			
			String purchaseDateText = String.valueOf(selectedDevice.getValue(FIELD.DEVICE_purchasedate));
			String expireWarrantyText = String.valueOf(selectedDevice.getValue(FIELD.DEVICE_warrantyexpire));
			
			//if( ! purchaseDateText.isEmpty() && ! purchaseDateText.equals("null")) DateUtils.setDateTimeValue((DateTime)ctls[PURCHASE], java.sql.Date.valueOf(purchaseDateText));
			//if( ! expireWarrantyText.isEmpty() && ! expireWarrantyText.equals("null")) DateUtils.setDateTimeValue((DateTime)ctls[WARRANTY], java.sql.Date.valueOf(expireWarrantyText));
			if( ! purchaseDateText.isEmpty() && ! purchaseDateText.equals("null")) ((CDateTime)ctls[PURCHASE]).setSelection(java.sql.Date.valueOf(purchaseDateText));
			if( ! expireWarrantyText.isEmpty() && ! expireWarrantyText.equals("null")) ((CDateTime)ctls[WARRANTY]).setSelection(java.sql.Date.valueOf(expireWarrantyText));
		}
		
		for(Control c : ctls)
		{
			c.addListener(SWT.Modify, e -> {
				c.setBackground(null);
				c.setToolTipText(null);
			});
		}
		
		NCButton.addListener(SWT.Selection, e -> {
			int rand = ThreadLocalRandom.current().nextInt(1000000, 9999999);
			((Text)ctls[SERIAL]).setText("NC" + rand);
		});
		
	}

	

	@Override
	protected String setTitle() {
		return "Machine";
	}

	@Override
	protected int actionValid() {
		Color red = uwin.getResourceManager().get_color(UResource.COLOR_RED);
		if(ComboUtils.valueNotFound(ctls[LOCATION])) {
			ctls[LOCATION].setBackground(red);
			return 1;
		}
		if(ComboUtils.valueNotFound(ctls[MODEL])) {
			ctls[MODEL].setBackground(red);
			return 1;
		}
		if(ComboUtils.valueNotFound(ctls[OWNER])) {
			ctls[OWNER].setBackground(red);
			return 1;
		}
		if(((Text)ctls[SERIAL]).getText().isEmpty()) {
			ctls[SERIAL].setBackground(red);
			return 2;
		}

		
		String serial = ((Text)ctls[SERIAL]).getText();
		String model = ((Combo)ctls[MODEL]).getText();
		try {
			if( ! modelManager.testSerial(model, serial)) {
				ctls[SERIAL].setBackground(red);
				ctls[SERIAL].setToolTipText(LANG.ERROR_SERIAL_PATTERN + serial + ", " + model);
				return 3;
			}
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
		
		device = new UData();	
		device.add(FIELD.DEVICE_location, ((Combo)ctls[LOCATION]).getText());
		device.add(FIELD.DEVICE_model, ((Combo)ctls[MODEL]).getText());
		device.add(FIELD.DEVICE_serial, ((Text)ctls[SERIAL]).getText());
		device.add(FIELD.DEVICE_owner, ((Combo)ctls[OWNER]).getText());
		
		
		java.sql.Date purchaseSQLDate = null;
		java.util.Date purchaseDate = ((CDateTime)ctls[PURCHASE]).getSelection();
		if(purchaseDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(purchaseDate);
			purchaseSQLDate = new java.sql.Date(cal.getTimeInMillis());
		}
		device.add(FIELD.DEVICE_purchasedate, purchaseSQLDate);
		
		java.sql.Date warrantySQLDate = null;
		java.util.Date warrantyDate = ((CDateTime)ctls[WARRANTY]).getSelection();
		if(warrantyDate != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(warrantyDate);
			warrantySQLDate = new java.sql.Date(cal.getTimeInMillis());
		}
		device.add(FIELD.DEVICE_warrantyexpire, warrantySQLDate);
		
		device.add(FIELD.DEVICE_idclient, ((Text)ctls[IDCLIENT]).getText());
		device.add(FIELD.DEVICE_comment, ((Text)ctls[COMMENT]).getText());
		
		
		device.add(FIELD.DEVICE_networkName, ((Text)ctls[NETWORKNAME]).getText());
		
		if(selectedDevice != null) device.add(FIELD.DEVICE_id, Integer.valueOf(String.valueOf(selectedDevice.getValue(FIELD.DEVICE_id))));
		

		
		return 0;
	}
	
	public final UData getDevice()
	{
		return device;
	}

}
