package uness.gui.device;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import unessdb.DBException;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.UDataException;
import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQLParams;
import uness.Uness;
import uness.Uness.OPTION;
import uness.core.Option;
import uness.gui.GuiException;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.gui.common.RemovePanel;
import uness.gui.common.UControl;
import uness.gui.common.UTable;
import uness.interfaces.Prefillable;

public class DevicePanel implements Prefillable {

	private final UWin uwin;
	
	public final int TI_DEV_RMA = 0;
	public final int TI_DEV_NEW = 1;
	public final int TI_DEV_EDIT = 2;
	public final int TI_DEV_DEL = 3;
	public final int TI_LOG_NEW = 4;
	public final int TI_LOG_EDIT = 5;
	public final int TI_LOG_DEL = 6;
	public final int TI_LOG_PRINT = 7;
	private final int TI_SIZE = 8;
	private ToolItem[] ti;
	
	public final int TABLE_DEVICE = 0;
	public final int TABLE_LOG = 1;
	private final int TABLE_SIZE = 2;
	private UTable[] table;

	public final int LBL_STATUS = 0;
	private final int LBL_SIZE = 1;
	private final Label[] lbl;
	
	public final int CTL_FLT_LOCATION = 0;
	public final int CTL_FLT_MODEL = 1;
	public final int CTL_SRC_LOCATION = 2;
	public final int CTL_SRC_SERIAL = 3;
	public final int CTL_SRC_NOTE = 4;
	public final int CTL_TGT_LOCATION = 5;
	public final int CTL_TGT_SERIAL = 6;
	public final int CTL_TGT_NOTE = 7;
	private final int CTL_SIZE = 8;
	private final UControl[] ctl;
	
	public final int BTN_SEARCH = 0;
	public final int BTN_SWAP = 1;
	public final int BTN_TGT_CLEAN = 2;
	public final int BTN_SRC_CLEAN = 3;
	private final int BTN_SIZE = 4;
	private final Button[] btn;
	
	private Menu rightClickMenu;
	private MenuItem loadSrc, loadTgt;
	private Text searchText;
	private Listener searchKeyEnterPressedListener;
	
	private Group swap, search;
	private final Option opt;
	private final UResource resMan;

	public DevicePanel(final UWin p_uwin) {
		uwin = p_uwin;
		resMan = uwin.getResourceManager();
		opt = uwin.getCorma().getOptionManager();
		
		ti = new ToolItem[TI_SIZE];
		table = new UTable[TABLE_SIZE];
		ctl = new UControl[CTL_SIZE];
		lbl = new Label[LBL_SIZE];
		btn = new Button[BTN_SIZE];
		
		initListeners();
	}

	public final Composite createManage(Composite tabFolder, int compositeStyle) {
		
		Composite cManage = new Composite(tabFolder, compositeStyle);
		cManage.setLayout(new GridLayout(2, true));

		search = new Group(cManage, SWT.NONE);
		
		search.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
		{
			search.setLayout(new GridLayout(2, false));
			search.setText("Recherche");
		
			searchText = new Text(search, SWT.SEARCH | SWT.ICON_SEARCH);
			searchText.addListener(SWT.KeyUp, searchKeyEnterPressedListener);
			searchText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			btn[BTN_SEARCH] = new Button(search, SWT.PUSH);
			btn[BTN_SEARCH].addListener(SWT.Selection, e -> action_device_search());
			btn[BTN_SEARCH].setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END, GridData.CENTER, false, false));
			btn[BTN_SEARCH].setImage(resMan.get_image(UResource.IMG_SEARCH));
	
			Group search_in = new Group(search, SWT.NONE);
			search_in.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1));
			{
				search_in.setLayout(new GridLayout(4, false));
				search_in.setText("Filtres");
			
				new Label(search_in, SWT.NONE).setText("Localisation: ");
				ctl[CTL_FLT_LOCATION] = new UControl(uwin, FIELD.DEVICE_location, search_in, SWT.READ_ONLY);
				ctl[CTL_FLT_LOCATION].getWidget().addListener(SWT.KeyUp, searchKeyEnterPressedListener);
				new Label(search_in, SWT.NONE).setText("Modèle: ");
				ctl[CTL_FLT_MODEL] = new UControl(uwin, FIELD.DEVICE_model, search_in, SWT.READ_ONLY);
				ctl[CTL_FLT_MODEL].getWidget().addListener(SWT.KeyUp, searchKeyEnterPressedListener);
			}

		}

		// Swap panel
		swap = new Group(cManage, SWT.NONE);
		swap.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		{
			swap.setText("Swap localisation");
			swap.setLayout(new GridLayout(2, false));
			
			Group source = new Group(swap, SWT.NONE);
			source.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			{
				source.setLayout(new GridLayout(3, false));
				source.setText("Source");
				
				ctl[CTL_SRC_LOCATION] = new UControl(uwin, FIELD.DEVICE_location, source, SWT.NONE);
				ctl[CTL_SRC_LOCATION].getWidget().setEnabled(false);
				ctl[CTL_SRC_LOCATION].getWidget().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				
				ctl[CTL_SRC_SERIAL] = new UControl(uwin, FIELD.DEVICE_serial, source, SWT.BORDER | SWT.READ_ONLY);
				ctl[CTL_SRC_SERIAL].getWidget().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				
				ctl[CTL_SRC_NOTE] = new UControl(uwin, FIELD.DEVICE_comment, source, SWT.BORDER | SWT.READ_ONLY);
				ctl[CTL_SRC_NOTE].getWidget().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			}
			
			btn[BTN_SRC_CLEAN] = new Button(swap, SWT.PUSH);
			btn[BTN_SRC_CLEAN].addListener(SWT.Selection, e -> actionCleanSwapSource());
			btn[BTN_SRC_CLEAN].setText("Effacer");

			btn[BTN_SWAP] = new Button(swap, SWT.PUSH);
			btn[BTN_SWAP].addListener(SWT.Selection, e -> action_device_swap());
			btn[BTN_SWAP].setImage(resMan.get_image(UResource.IMG_SWAP));
			btn[BTN_SWAP].setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, false, 2, 1));
			
	
			Group target = new Group(swap, SWT.NONE);
			target.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			{
				target.setText("Destination");
				target.setLayout(new GridLayout(3, false));
			
				ctl[CTL_TGT_LOCATION] = new UControl(uwin, FIELD.DEVICE_location, target, SWT.NONE);
				ctl[CTL_TGT_LOCATION].getWidget().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				
				ctl[CTL_TGT_SERIAL] = new UControl(uwin, FIELD.DEVICE_serial, target, SWT.BORDER | SWT.READ_ONLY);
				ctl[CTL_TGT_SERIAL].getWidget().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				
				ctl[CTL_TGT_NOTE] = new UControl(uwin, FIELD.DEVICE_comment, target, SWT.BORDER | SWT.READ_ONLY);
				ctl[CTL_TGT_NOTE].getWidget().setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			}
			
			
			btn[BTN_TGT_CLEAN] = new Button(swap, SWT.PUSH);
			btn[BTN_TGT_CLEAN].addListener(SWT.Selection, e -> actionCleanSwapTarget());
			btn[BTN_TGT_CLEAN].setText("Effacer");
		}
		
		
		
		//Devices table
		Composite device = new Composite(cManage, SWT.NONE);
		device.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2));
		{
			device.setLayout(new GridLayout());
			ToolBar deviceToolbar = new ToolBar(device, SWT.HORIZONTAL);
			deviceToolbar.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			{
				ti[TI_DEV_RMA] = new ToolItem(deviceToolbar, SWT.PUSH);
				ti[TI_DEV_RMA].addListener(SWT.Selection, e -> action_device_openRma());
				ti[TI_DEV_RMA].setText("&RMA");
				ti[TI_DEV_RMA].setImage(resMan.get_image(UResource.IMG_RMA));
				
				new ToolItem(deviceToolbar, SWT.SEPARATOR);
				
				ti[TI_DEV_NEW] = new ToolItem(deviceToolbar, SWT.PUSH);
				ti[TI_DEV_NEW].addListener(SWT.Selection, e -> action_device_new());
				ti[TI_DEV_NEW].setText("&Nouveau");
				ti[TI_DEV_NEW].setImage(resMan.get_image(UResource.IMG_NEWDEV));
				
				ti[TI_DEV_EDIT] = new ToolItem(deviceToolbar, SWT.PUSH);
				ti[TI_DEV_EDIT].addListener(SWT.Selection, e -> action_device_edit());
				ti[TI_DEV_EDIT].setText("&Editer");
				ti[TI_DEV_EDIT].setImage(resMan.get_image(UResource.IMG_EDIT));
				
				ti[TI_DEV_DEL] = new ToolItem(deviceToolbar, SWT.PUSH);
				ti[TI_DEV_DEL].addListener(SWT.Selection, e -> action_device_remove());
				ti[TI_DEV_DEL].setText("&Supprimer");
				ti[TI_DEV_DEL].setImage(resMan.get_image(UResource.IMG_BOMB));
						
			}
			
			Composite table_container = new Composite(device, SWT.BORDER);
			table_container.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2));
			{
				table_container.setLayout(new GridLayout());
				
				table[TABLE_DEVICE] = new UTable(uwin, TABLE.DEVICE.getFields(), OPTION.table_devices_show, OPTION.table_devices_sort, OPTION.table_devices_sens){
					protected void action_clickColumn() {
						action_device_search();
					}
					protected void action_clickTableItem(UData tableItem) {
						action_load_deviceLogs();
					}
					protected Menu menuContent() {
						rightClickMenu = new Menu(this.getTable());
						loadSrc = new MenuItem(rightClickMenu, SWT.NONE);
						loadSrc.setText("Swap(Source)");
						loadSrc.addListener(SWT.Selection, e -> actionSetSwapSource(getSelection()));
						loadTgt = new MenuItem(rightClickMenu, SWT.NONE);
						loadTgt.addListener(SWT.Selection, e -> actionSetSwapTarget(getSelection()));
						loadTgt.setText("Swap(Destination)");
						
						return rightClickMenu;
					}
					
				};
				Table rawTable = table[TABLE_DEVICE].create(table_container, SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.BORDER);
				rawTable.addListener(SWT.KeyUp, e -> {
					if (e.keyCode == SWT.DEL) action_device_remove();
				});
				rawTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
	
			
				
				lbl[LBL_STATUS] = new Label(table_container, SWT.NONE);
				lbl[LBL_STATUS].setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				
			}
		
		}
		
		// Log panel
		Composite device_select = new Composite(cManage, SWT.NONE);
		device_select.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2));
		{
			device_select.setLayout(new GridLayout());
	
			Group log = new Group(device_select, SWT.NONE);
			log.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			{
				log.setText("Logs");
				log.setLayout(new GridLayout());
				
				// ---- LOG BAR ----
				ToolBar logToolbar = new ToolBar(log, SWT.HORIZONTAL);
				logToolbar.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				{
					ti[TI_LOG_NEW] = new ToolItem(logToolbar, SWT.PUSH);
					ti[TI_LOG_NEW].setText("N&ouveau");
					ti[TI_LOG_NEW].setImage(resMan.get_image(UResource.IMG_NOTE));
					ti[TI_LOG_NEW].addListener(SWT.Selection, e -> action_log_new());
					
					ti[TI_LOG_EDIT] = new ToolItem(logToolbar, SWT.PUSH);
					ti[TI_LOG_EDIT].setText("E&diter");
					ti[TI_LOG_EDIT].setImage(resMan.get_image(UResource.IMG_EDIT));
					ti[TI_LOG_EDIT].addListener(SWT.Selection, e -> action_log_edit());
					
					ti[TI_LOG_DEL] = new ToolItem(logToolbar, SWT.PUSH);
					ti[TI_LOG_DEL].setText("S&upprimer");
					ti[TI_LOG_DEL].setImage(resMan.get_image(UResource.IMG_BOMB));
					ti[TI_LOG_DEL].addListener(SWT.Selection, e -> action_log_remove());
					
					new ToolItem(logToolbar, SWT.SEPARATOR);
					
					ti[TI_LOG_PRINT] = new ToolItem(logToolbar, SWT.PUSH);
					ti[TI_LOG_PRINT].addListener(SWT.Selection, e -> action_log_print());
					ti[TI_LOG_PRINT].setText("Im&primer");
					ti[TI_LOG_PRINT].setImage(resMan.get_image(UResource.IMG_PRINT));
					
					
					
				}
	
				// ---- LOGS TABLE ----
				table[TABLE_LOG] = new UTable(uwin, TABLE.LOG.getFields(), OPTION.table_log_show, OPTION.table_log_sort, OPTION.table_log_sens){

					protected void action_clickColumn() {
						action_load_deviceLogs();
					}

					protected void action_clickTableItem(UData tableItem) {
						
					}

					protected Menu menuContent() {
						return null;
					}
					
				};
			
				Table rawTable = table[TABLE_LOG].create(log, SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.BORDER);
				rawTable.addListener(SWT.KeyUp, e -> {
					if(e.keyCode == SWT.DEL) action_log_remove();
				});
				rawTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			}
		}
	
		return cManage;
	}
	
	public void fillDatas() {	
		ctl[CTL_TGT_LOCATION].fillDatas();
		ctl[CTL_FLT_LOCATION].fillDatas();
		((Combo)ctl[CTL_FLT_LOCATION].getWidget()).add("", 0);
		((Combo)ctl[CTL_FLT_LOCATION].getWidget()).select(0);
		ctl[CTL_FLT_MODEL].fillDatas();
		((Combo)ctl[CTL_FLT_MODEL].getWidget()).add("", 0);
		((Combo)ctl[CTL_FLT_MODEL].getWidget()).select(0);
	}
	
	private void initListeners()
	{
		searchKeyEnterPressedListener = e -> {
			if(e.keyCode == 13 || e.keyCode == SWT.KEYPAD_CR){
				action_device_search();
			}
		};
	}
	
	
	public final void action_device_search()
	{	
		//Prepare search
		FIELD[] searchFields = new FIELD[] {FIELD.DEVICE_idclient, FIELD.DEVICE_location, FIELD.DEVICE_model, FIELD.DEVICE_networkName, FIELD.DEVICE_serial};
		String location = ctl[CTL_FLT_LOCATION].getText();
		String model = ctl[CTL_FLT_MODEL].getText();
		String keyword = searchText.getText();

		USQLParams params = new USQLParams();
		
		for(FIELD field : searchFields)
			params.add(field, "%" + keyword + "%", false);
		
		if( ! location.isEmpty()) params.add(FIELD.DEVICE_location, location, true);
		if( ! model.isEmpty()) params.add(FIELD.DEVICE_model, model, true);
		
		FIELD sort = FIELD.valueOf(opt.getProperty(OPTION.table_devices_sort));
		Access sens = Access.valueOf(opt.getProperty(OPTION.table_devices_sens));

		//Send search
		ArrayList<UData> datas = null;
		try {
			datas = uwin.getCorma().getDeviceManager().searchDevices(params, sort, sens);
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}

		table[TABLE_DEVICE].draw(datas);

		//Set result number in status bar left
		lbl[LBL_STATUS].setText(String.valueOf(datas.size()) + " Résultat(s)");
	}
	
	private void action_log_new()
	{
		
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		
		LogEditorPanel logEditor = new LogEditorPanel(uwin, selectedDevice, null);
		logEditor.open();
		UData log = logEditor.getLog();
		
		if(log == null) return;
			
		try {
			uwin.getCorma().getLogMan().newLog(log);
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		action_load_deviceLogs();
		
		
	}
	
	private void action_log_edit()
	{
		UData selectedLog = table[TABLE_LOG].getSelection();
		if(selectedLog == null) return;
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		if(selectedDevice == null) return;
		
		LogEditorPanel logEditor = new LogEditorPanel(uwin, selectedDevice, selectedLog);
		logEditor.open();
		UData newLog = logEditor.getLog();
		
		if(newLog == null) return;
		
		try {
			USQLParams logId = new USQLParams(FIELD.LOG_id, selectedLog.getValue(FIELD.LOG_id));
			uwin.getCorma().getLogMan().updateLog(newLog, logId);
		} catch (DBException e)
		{
			e.printStackTrace();
			uwin.showError(e.getClass().getName(), e.getMessage());
			return;
		}
		
		action_load_deviceLogs();
	}
	
	private void action_log_remove()
	{
		UData selectedLog = table[TABLE_LOG].getSelection();
		if(selectedLog == null) return;
		
		if(new RemovePanel(uwin).open())
		{
			try {
				uwin.getCorma().getLogMan().remove(selectedLog);
			} catch (DBException ex)
			{
				uwin.showError("Remove Log Error", ex.getMessage());
				return;
			}
		}
		
		action_load_deviceLogs();
		
		
	}
	
	private void action_log_print(){
		Collection<UData> datas = table[TABLE_LOG].getDatas();
		if(datas.size() < 1) return;
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		
		
		uwin.setWait(null);
		
		uwin.getCorma().getLogMan().printLog(selectedDevice);
		uwin.stopWait(null);
	}
	
	private void action_device_openRma()
	{
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		if(selectedDevice == null) return;

		new RMAEditorPanel(uwin,this, selectedDevice).open();
	}
	
	private void action_device_new()
	{
		DeviceEditorPanel deviceEditor = new DeviceEditorPanel(uwin, null);
		deviceEditor.open();
		
		UData device = deviceEditor.getDevice();
		if(device == null) return;
		
		try {
			uwin.getCorma().getDeviceManager().addDevice(device);
		} catch (DBException | ParseException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		searchText.setText(String.valueOf(device.getValue(FIELD.DEVICE_serial)));
		action_device_search();
	}
	
	private void action_device_edit()
	{
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		if(selectedDevice == null) return;
		
		DeviceEditorPanel deviceEditor = new DeviceEditorPanel(uwin, selectedDevice);
		deviceEditor.open();
		
		UData modifiedDevice = deviceEditor.getDevice();
		if(modifiedDevice == null) return;
		
		try {
			uwin.getCorma().getDeviceManager().updateDevice(modifiedDevice, new USQLParams(FIELD.DEVICE_id, selectedDevice.getValue(FIELD.DEVICE_id)));
		} catch (DBException | ParseException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
	table[TABLE_DEVICE].replace(modifiedDevice);
		
	}
	
	public void action_load_deviceLogs()
	{	
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		FIELD sort = FIELD.valueOf(opt.getProperty(Uness.OPTION.table_log_sort));
		Access sens = Access.valueOf(opt.getProperty(Uness.OPTION.table_log_sens));
		
		
		//Get logs and show it
		ArrayList<UData> logs = null;
		try {
			logs = uwin.getCorma().getLogMan().getLogs(selectedDevice, sort, sens);
		} catch (DBException e) {
			e.printStackTrace();
			uwin.showError(e.getClass().getName(), e.getMessage());
			return;
		}
		
		table[TABLE_LOG].draw(logs);
	}
	
	private final void action_device_remove() {
		UData selectedDevice = table[TABLE_DEVICE].getSelection();
		if(selectedDevice == null) return;
		if ( ! new RemovePanel(uwin).open()) return;
		
		try {
			uwin.getCorma().getDeviceManager().remove(selectedDevice);
		} catch (DBException e) {
			e.printStackTrace();
			uwin.showError("Error", e.getMessage());
			return;
		}
		
		table[TABLE_DEVICE].removeSelection();
	}
	
	public void action_device_swap()
	{
		final UData userSelected = uwin.getCorma().getUsersManager().getSignedUser();
		final UData source = uwin.getCorma().getSwapManager().getSource();
		final UData target = uwin.getCorma().getSwapManager().getTarget();

		if(source == null) {
			uwin.showError("Swap error", "Source vide");
			return;
		}
		
		//Invalid value
		try {
			ctl[CTL_TGT_LOCATION].testValue();
		} catch (GuiException e)
		{
			uwin.showError(e.getClass().getName(), e.getMessage());
			return;
		}

		//No user selected
		if(userSelected == null)
		{
			Combo combo = uwin.getUsersPanel().getComboUsers();
			combo.setFocus();
			combo.setListVisible(true);
			return;
		}
		//Source and target location are the same
		if(ctl[CTL_SRC_LOCATION].getText().equals(ctl[CTL_TGT_LOCATION].getText()))
		{
			uwin.showError("Swap error","Source et destination identique");
			return;
		}
		//All test is OK
		
		//Prepare the datas
		
		UData newSrc = UData.copy(source);
		UData newTgt;
		
		try {
			//If the target is an object
			if(target != null)
			{
				newSrc.set(FIELD.DEVICE_location, (String)target.getValue(FIELD.DEVICE_location));
				newTgt = UData.copy(target);
				newTgt.set(FIELD.DEVICE_location, (String)source.getValue(FIELD.DEVICE_location));
			}
			//Or if it is a text location
			else
			{
				newSrc.set(FIELD.DEVICE_location, ctl[CTL_TGT_LOCATION].getText());
				newTgt = UData.copy(source);
				newTgt.set(FIELD.DEVICE_location, ctl[CTL_TGT_LOCATION].getText());
			}
			
			uwin.getCorma().getSwapManager().swap(userSelected, newSrc, newTgt);
		}catch (UDataException | DBException e)
		{
			e.printStackTrace();
			uwin.showError(e.getClass().getName(), e.getMessage());
			return;
		}

		//Success actions
		
		//If the target is just a text
		if(target == null) actionCleanSwapSource();
		else actionSetSwapSource(newSrc);	
		
		actionSetSwapTarget(newTgt);
		
		//Display actions
		table[TABLE_DEVICE].replace(source, newSrc);
		table[TABLE_DEVICE].replace(target, newTgt);
	}
	
	public void actionSetSwapSource(UData deviceSelected)
	{
		if(deviceSelected == null) return;
		uwin.getCorma().getSwapManager().setSource(deviceSelected);
		// POST ACTION
		ctl[CTL_SRC_LOCATION].setText((String)deviceSelected.getValue(FIELD.DEVICE_location));
		ctl[CTL_SRC_SERIAL].setText((String)deviceSelected.getValue(FIELD.DEVICE_serial));
		ctl[CTL_SRC_NOTE].setText((String)deviceSelected.getValue(FIELD.DEVICE_comment));
		actionCleanSwapTarget();
	}
	
	public void actionSetSwapTarget(UData deviceSelected)
	{
		if(deviceSelected == null) return;
		uwin.getCorma().getSwapManager().setTarget(deviceSelected);
		// POST ACTION
		ctl[CTL_TGT_LOCATION].setText((String)deviceSelected.getValue(FIELD.DEVICE_location));
		ctl[CTL_TGT_LOCATION].setEnabled(false);
		ctl[CTL_TGT_SERIAL].setText((String)deviceSelected.getValue(FIELD.DEVICE_serial));
		ctl[CTL_TGT_NOTE].setText((String)deviceSelected.getValue(FIELD.DEVICE_comment));
	}
	
	public void actionCleanSwapSource()
	{
		uwin.getCorma().getSwapManager().setSource(null);
		ctl[CTL_SRC_LOCATION].setText("");
		ctl[CTL_SRC_SERIAL].setText("");
		ctl[CTL_SRC_NOTE].setText("");
	}
	public void actionCleanSwapTarget()
	{
		/*
		uwin.getCorma().getSwapManager().setTarget(null);
		panel.getTgtLocation().setText("");
		panel.getTgtLocation().setEnabled(true);
		panel.getTgtSerial().setText("");
		panel.getTgtNote().setText("");
		*/
	}
	
	public final Group getGroupSwap()
	{
		return swap;
	}
	public final Group getGroupSearch()
	{
		return search;
	}
	public final Control getFirstFocusControl()
	{
		return searchText;
	}
	
	public final UTable[] getTable()
	{
		return table;
	}

}
