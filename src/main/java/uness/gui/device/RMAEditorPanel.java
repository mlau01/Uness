package uness.gui.device;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import uness.LANG;
import uness.Uness;
import uness.core.Report;
import uness.core.Rma;
import uness.core.Rma.RMA_STATUS;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.UDataException;
import uness.gui.UWin;
import uness.gui.common.AbstractPopupPanel;
import uness.gui.common.UTable;
import uness.gui.rma.RMAReport;
import uness.interfaces.Prefillable;

public class RMAEditorPanel implements Prefillable {

	private UWin uwin;
	private Shell parent;

	private Shell dialog;
	private RMA_STATUS mode;

	private UData deviceSelected;
	private UData rma;
	private final DevicePanel devicePanel;
	private final Rma rmaManager;
	
	Control[] ctls;
	public static final int RMA = 0;
	public static final int MODEL = 1;
	public static final int SERIAL = 2;
	public static final int LOCATION = 3;
	public static final int WARRANTY = 4;
	public static final int DATESENT = 5;
	public static final int SENDER = 6;
	public static final int VOUCHER = 7;
	public static final int CATCHER = 8;
	public static final int DATECATCHED = 9;
	public static final int COMMENT = 10;
	public static final int CTL_SIZE = 11;
	

	private Button ok;
	private Button cancel;

	public RMAEditorPanel(final UWin p_uwin, DevicePanel p_devicePanel, final UData p_deviceSelected) {
		uwin = p_uwin;
		parent = uwin.getShell();
		deviceSelected = p_deviceSelected;
		devicePanel = p_devicePanel;
		rmaManager = uwin.getCorma().getRmaManager();

		ctls = new Control[CTL_SIZE];
		
		try {
			rma = uwin.getCorma().getRmaManager().getFirstUnclosedRma(deviceSelected);
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		if (rma == null) mode = RMA_STATUS.NEW;
		else mode = RMA_STATUS.RETURN;
	}

	public Shell open() {
		dialog = new Shell(parent, SWT.TITLE | SWT.CLOSE | SWT.APPLICATION_MODAL);

		createWidgets();
		fillDatas();

		dialog.pack();

		dialog.setLocation(UWin.getPrefferedPopupPoint(parent, dialog));
		dialog.open();

		return dialog;
	}

	public void fillDatas() {
		
		String[] userList;
		Calendar cal = GregorianCalendar.getInstance();
		int selectedUserIndex = uwin.getUsersPanel().getComboUsers().getSelectionIndex();
		
		try {
			userList = uwin.getCorma().getUsersManager().getFullnameList();
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
	
		
		((Text)ctls[MODEL]).setText(String.valueOf(deviceSelected.getValue(FIELD.DEVICE_model)));
		((Text)ctls[SERIAL]).setText(String.valueOf(deviceSelected.getValue(FIELD.DEVICE_serial)));
		((Text)ctls[LOCATION]).setText(String.valueOf(deviceSelected.getValue(FIELD.DEVICE_location)));
		((Text)ctls[WARRANTY]).setText(String.valueOf(deviceSelected.getValue(FIELD.DEVICE_warrantyexpire)));
		
		if(mode == RMA_STATUS.NEW)
		{
			dialog.setText(LANG.RMA_TITLE_NEW.get());
			ctls[DATECATCHED].setEnabled(false);
			ctls[CATCHER].setEnabled(false);
			((Combo)ctls[SENDER]).setItems(userList);
			((Combo)ctls[SENDER]).select(selectedUserIndex);
			((CDateTime)ctls[DATESENT]).setSelection(cal.getTime());
		}
		
		if(mode == RMA_STATUS.RETURN){
			dialog.setText(LANG.RMA_TITLE_RETURN.get());
			((Text)ctls[RMA]).setText((String.valueOf(rma.getValue(FIELD.RMA_rmanumber))));
			ctls[RMA].setEnabled(false);
			((Combo)ctls[SENDER]).setText(String.valueOf(rma.getValue(FIELD.RMA_sender)));
			ctls[SENDER].setEnabled(false);
			((CDateTime)ctls[DATESENT]).setSelection(java.sql.Date.valueOf(String.valueOf(rma.getValue(FIELD.RMA_datesent))));
			ctls[DATESENT].setEnabled(false);
			((Text)ctls[VOUCHER]).setText(String.valueOf(rma.getValue(FIELD.RMA_travelvoucher)));
			ctls[VOUCHER].setEnabled(false);
			((Text)ctls[COMMENT]).setText(String.valueOf(rma.getValue(FIELD.RMA_comment)));
			ctls[COMMENT].setEnabled(false);
			((Combo)ctls[CATCHER]).setItems(userList);
			((Combo)ctls[CATCHER]).select(selectedUserIndex);
			((CDateTime)ctls[DATECATCHED]).setSelection(cal.getTime());
		}
	}

	private void createWidgets() {
		
		dialog.setLayout(new GridLayout(2, true));

		{
			// Folder------
			Group topLeft = new Group(dialog, SWT.NONE);
			topLeft.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
			topLeft.setText("Dossier");
			topLeft.setLayout(new GridLayout(2, true));
			{
				new Label(topLeft, SWT.NONE).setText(FIELD.RMA_rmanumber.getDisplayName());
				
				ctls[RMA] = new Text(topLeft, SWT.BORDER);
			}

			// Device------
			Group topRight = new Group(dialog, SWT.NONE);
			topRight.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
			topRight.setText("Matériel");
			topRight.setLayout(new GridLayout(2, false));
			{
				new Label(topRight, SWT.NONE).setText(FIELD.DEVICE_model.getDisplayName());
				ctls[MODEL] = new Text(topRight, SWT.BORDER | SWT.READ_ONLY);
				
				new Label(topRight, SWT.NONE).setText(FIELD.DEVICE_serial.getDisplayName());
				ctls[SERIAL] = new Text(topRight, SWT.BORDER | SWT.READ_ONLY);
				
				new Label(topRight, SWT.NONE).setText(FIELD.DEVICE_location.getDisplayName());
				ctls[LOCATION] = new Text(topRight, SWT.BORDER | SWT.READ_ONLY);
				
				new Label(topRight, SWT.NONE).setText(FIELD.DEVICE_warrantyexpire.getDisplayName());
				ctls[WARRANTY] = new Text(topRight, SWT.BORDER | SWT.READ_ONLY);
			}

			// ------Sending--------
			Group botLeft = new Group(dialog, SWT.NONE);
			botLeft.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
			botLeft.setText("Envoi");

			botLeft.setLayout(new GridLayout(2, false));
			{
				new Label(botLeft, SWT.NONE).setText(FIELD.RMA_sender.getDisplayName());
				ctls[SENDER] = new Combo(botLeft, SWT.BORDER);
				
				new Label(botLeft, SWT.NONE).setText(FIELD.RMA_datesent.getDisplayName());
				ctls[DATESENT] = new CDateTime(botLeft, CDT.BORDER | CDT.COMPACT | CDT.DROP_DOWN | CDT.DATE_SHORT);
				
				new Label(botLeft, SWT.NONE).setText(FIELD.RMA_travelvoucher.getDisplayName());
				ctls[VOUCHER] = new Text(botLeft, SWT.BORDER);

			}
			// ------Return-------
			Group botRight = new Group(dialog, SWT.NONE);
			botRight.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true));
			botRight.setText("Retour");
			
			botRight.setLayout(new GridLayout(2, false));
			{

				new Label(botRight, SWT.NONE).setText(FIELD.RMA_catcher.getDisplayName());
				ctls[CATCHER] = new Combo(botRight, SWT.BORDER);
				
				new Label(botRight, SWT.NONE).setText(FIELD.RMA_datecatched.getDisplayName());
				ctls[DATECATCHED] = new CDateTime(botRight, CDT.BORDER | CDT.COMPACT | CDT.DROP_DOWN | CDT.DATE_SHORT);

			}

			// -----Note-----
			Group bottom = new Group(dialog, SWT.NONE);
			
			bottom.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, true, 2, 1));
			bottom.setLayout(new GridLayout());
			{
				new Label(bottom, SWT.NONE).setText(FIELD.RMA_comment.getDisplayName());
				ctls[COMMENT] = new Text(bottom, SWT.BORDER);
			}
			
			for(Control control : ctls){
				control.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			}

			// Buttons widgets
			Composite buttons = new Composite(dialog, SWT.NONE);
			buttons.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true, 2, 1));
			{
				buttons.setLayout(new GridLayout(2, true));

				ok = new Button(buttons, SWT.PUSH);
				ok.setText(LANG.OK.get());
				ok.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				ok.addListener(SWT.Selection, e -> actionValidRma());
				dialog.setDefaultButton(ok);

				cancel = new Button(buttons, SWT.PUSH);
				cancel.setText("Annuler");
				cancel.addListener(SWT.Selection, e -> dialog.dispose());
				cancel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			}
		}
	}
	
	private void actionValidRma()
	{
		String deviceLocation = null;
		UData datas = new UData();
		UData user = null;
		UData report = null;
		
		if(mode == RMA_STATUS.NEW)
		{
			deviceLocation = "Constructor";
			String rmaNumber = ((Text)ctls[RMA]).getText();
			String sender = ((Combo)ctls[SENDER]).getText();
			CDateTime datesend = ((CDateTime)ctls[DATESENT]);
			String voucher = ((Text)ctls[VOUCHER]).getText();
			
			if(rmaNumber.isEmpty() || sender.isEmpty() || voucher.isEmpty() || datesend.getText().equals("<choisissez une date>")) return;
			
			
			datas.add(FIELD.RMA_device, String.valueOf(deviceSelected.getValue(FIELD.DEVICE_id)));
			datas.add(FIELD.RMA_devicelastlocation, String.valueOf(deviceSelected.getValue(FIELD.DEVICE_location)));
			
			datas.add(FIELD.RMA_rmanumber, rmaNumber);
		
			user = uwin.getCorma().getUsersManager().getUserByFullname(sender); 
			datas.add(FIELD.RMA_sender, sender);
			
			java.util.Date sendDate = datesend.getSelection();
			Calendar cal = Calendar.getInstance();
			cal.setTime(sendDate);
			java.sql.Date sent = new java.sql.Date(cal.getTimeInMillis());
			datas.add(FIELD.RMA_datesent, sent);
			
			datas.add(FIELD.RMA_travelvoucher, voucher);
			datas.add(FIELD.RMA_comment, ((Text)ctls[COMMENT]).getText());
		}
			
		else if(mode == RMA_STATUS.RETURN)
		{
			deviceLocation = String.valueOf(rma.getValue(FIELD.RMA_devicelastlocation));
			String catcher = ((Combo)ctls[CATCHER]).getText();
			CDateTime dateCatch = ((CDateTime)ctls[DATECATCHED]);
			
			if(catcher.isEmpty() || dateCatch.getText().equals("<choisissez une date>")) return;
			
			user = uwin.getCorma().getUsersManager().getUserByFullname(catcher); 
			datas.add(FIELD.RMA_catcher, catcher);
			
			java.util.Date catchDate = dateCatch.getSelection();
			Calendar cal = Calendar.getInstance();
			cal.setTime(catchDate);
			java.sql.Date catched = new java.sql.Date(cal.getTimeInMillis());
			datas.add(FIELD.RMA_datecatched, catched);
		}
		
		datas.put(FIELD.RMA_status, mode.toString());
		
		try {
			report = rmaManager.create(user, deviceSelected, datas, rma);
		} catch (DBException | UDataException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
	
		new RMAReport(uwin, mode, rmaManager.getPrintObject(report)).open();
		
		UTable deviceTable = devicePanel.getTable()[devicePanel.TABLE_DEVICE];
		try {
			deviceSelected.set(FIELD.DEVICE_location, deviceLocation);
		} catch (UDataException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
		deviceTable.replace(deviceSelected, deviceSelected);
		
		
		dialog.dispose();
		
	}
}
