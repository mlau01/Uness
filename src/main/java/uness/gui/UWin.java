package uness.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import uness.LANG;
import uness.Uness;
import uness.Uness.OPTION;
import uness.core.Core;
import uness.core.OptionException;
import uness.core.Option;
import unessdb.DBException;
import unessdb.usql.USQL.DB;
import uness.gui.browse.BrowsePanel;
import uness.gui.device.DevicePanel;
import uness.gui.importui.ImportPanel;
import uness.gui.manage.ManagePanel;
import uness.gui.menu.UMenu;
import uness.gui.rma.RMAPanel;
import uness.gui.search.SearchPanel;
import uness.gui.users.UsersPanel;
import uness.interfaces.Prefillable;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

public class UWin {

	private static Logger logger = LogManager.getLogger();
	private final Display display;
	private final Shell shell;
	private Listener disposeListener, tabClickListener;

	private UsersPanel usersPanel;
	private DevicePanel deviceGui;
	private RMAPanel rmaGui;
	private ImportPanel importPanel;
	private ManagePanel managePanel;
	private BrowsePanel browsePanel;
	private SearchPanel searchPanel;

	private TabFolder tabFolder;
	private TabItem[] tabItems;
	public final int DEVICE = 0;
	public final int RMA = 1;
	public final int IMPORT = 2;
	public final int MANAGE = 3;
	public final int BROWSE = 4;
	public final int SEARCH = 5;
	public final int TAB_SIZE = 6;

	private UMenu menu;
	private UResource resource;
	
	private Core corma;

	public UWin() {
		display = new Display();
		resource = new UResource(this);
		tabItems = new TabItem[TAB_SIZE];
		
		createListeners();
		
		shell = new Shell(display, SWT.SHELL_TRIM);
		shell.addListener(SWT.Dispose, disposeListener);
		shell.setText(Uness.APP_NAME.substring(0, 1).toUpperCase() + Uness.APP_NAME.substring(1) + " " + Uness.APP_VERSION);
		shell.setImage(resource.get_image(UResource.IMG_UNESS));
		
		setWait(shell);
		try {
			corma = new Core();
		} catch (final OptionException e)
		{
			e.printStackTrace();
			showError(e.getClass().getName(), e.getMessage());
			logger.fatal(e);
		} finally {
			stopWait(shell);
		}
		
		createWidgets();

		shell.pack();
		shell.open();
		
		Option optMan = corma.getOptionManager();
		DB useDb = DB.valueOf(optMan.getProperty(OPTION.useDb));
		String dbUrl = optMan.getProperty(OPTION.dbUrl);
		String dbName = optMan.getProperty(OPTION.dbName);
		String dbLogin = optMan.getProperty(OPTION.dbLogin);
		String dbPasswd = optMan.getProperty(OPTION.dbPasswd);

		try {
			corma.getAccessManager().connect(useDb, dbUrl, dbName, dbLogin, dbPasswd);
		} catch (DBException e)
		{
			e.printStackTrace();
			showError("Access init Exception", e.getMessage());
			logger.error(e);
		}
		
		usersPanel.fillDatas();
		deviceGui.fillDatas();
		deviceGui.getFirstFocusControl().forceFocus();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public final void showError(final String title, final String message)
	{
		final MessageBox info = new MessageBox(getShell(), SWT.ERROR | SWT.OK);
		
		info.setText(title);
		info.setMessage(message);
		info.open();
	}

	private void createWidgets() {
		// -- MENU --
		menu = new UMenu(shell, this);
		shell.setMenuBar(menu.getMenu());

		shell.setLayout(new GridLayout(1, false));
		
		// -- Users --
		
		usersPanel = new UsersPanel(this);
		Composite gusers = usersPanel.createWidgets(shell);
		gusers.setLayoutData(new GridData(GridData.END, GridData.BEGINNING, false, false));

		// -- Tab --
		tabFolder = new TabFolder(shell, SWT.NONE);
		tabFolder.addListener(SWT.Selection,tabClickListener);
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			tabItems[DEVICE] = new TabItem(tabFolder, SWT.NONE);
			tabItems[DEVICE].setText(LANG.TAB_DEVICE.get());
			deviceGui = new DevicePanel(this);
			tabItems[DEVICE].setControl(deviceGui.createManage(tabFolder, SWT.NONE));
			
			tabItems[RMA] = new TabItem(tabFolder, SWT.NONE);
			tabItems[RMA].setText(LANG.TAB_RMA.get());
			rmaGui = new RMAPanel(this);
			tabItems[RMA].setControl(rmaGui.create(tabFolder, SWT.NONE));
			
			tabItems[SEARCH] = new TabItem(tabFolder, SWT.NONE);
			tabItems[SEARCH].setText(LANG.TAB_SEARCH.get());
			searchPanel = new SearchPanel(this);
			tabItems[SEARCH].setControl(searchPanel.create(tabFolder, SWT.NONE));
			
			tabItems[BROWSE] = new TabItem(tabFolder, SWT.NONE);
			tabItems[BROWSE].setText(LANG.TAB_BROWSE.get());
			browsePanel = new BrowsePanel(this);
			tabItems[BROWSE].setControl(browsePanel.create(tabFolder, SWT.NONE));
			
			tabItems[IMPORT] = new TabItem(tabFolder, SWT.NONE);
			tabItems[IMPORT].setText(LANG.TAB_IMPORT.get());
			importPanel = new ImportPanel(this);
			tabItems[IMPORT].setControl(importPanel.create(tabFolder, SWT.NONE));
			
			
			tabItems[MANAGE] = new TabItem(tabFolder, SWT.NONE);
			tabItems[MANAGE].setText(LANG.TAB_MANAGE.get());
			managePanel = new ManagePanel(this);
			tabItems[MANAGE].setControl(managePanel.create(tabFolder, SWT.NONE));
			

		}
	}
	
	public final Core getCorma()
	{
		return corma;
	}

	public Shell getShell() {
		return shell;
	}

	public Display getDisplay() {
		return display;
	}

	public static Point getShellCenter(Shell shell) {
		// Point shellSize = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point shellSize = shell.getSize();
		int x = shell.getLocation().x + (shellSize.x / 2);
		int y = shell.getLocation().y + (shellSize.y / 2);

		return new Point(x, y);
	}

	public static Point getPrefferedPopupPoint(Shell parent, Shell dialog) {
		if (parent == null)
			return null;
		Point parentCenter = getShellCenter(parent);
		// Point dialogSize = dialog.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point dialogSize = dialog.getSize();
		int x = parentCenter.x - (dialogSize.x / 2);
		int y = parentCenter.y - (dialogSize.y / 2);

		return new Point(x, y);
	}
	
	public final UsersPanel getUsersPanel()
	{
		return usersPanel;
	}

	public void setWait(Shell target) {
		if (target != null && !target.isDisposed())
			target.setCursor(display.getSystemCursor(SWT.CURSOR_WAIT));

		shell.setCursor(display.getSystemCursor(SWT.CURSOR_WAIT));
	}

	public void stopWait(Shell target) {
		if (target != null && !target.isDisposed())
			target.setCursor(null);

		shell.setCursor(null);
	}

	// ----
	
	public static void main(String[] args)
	{
		new UWin();
	}
	
	public final UResource getResourceManager()
	{
		return resource;
	}
	
	private void createListeners() {
		// Dispose Events
		disposeListener = e ->  {
			if (resource != null) resource.dispose();
			
			try {
				corma.getOptionManager().write();
			} catch (OptionException e1) {
				showError(e1.getClass().getName(), e1.getMessage());
				e1.printStackTrace();
				logger.error(e);
			}
			
		};

		// Tab Selection Events
		tabClickListener = e -> {
			TabItem selected = tabFolder.getSelection()[0];
			TabItem[] items = tabItems;
			if (selected == items[DEVICE]) {
				deviceGui.fillDatas();
				deviceGui.getFirstFocusControl().forceFocus();
			}
			else if (selected == items[RMA]) rmaGui.getSearchInput().forceFocus();
			else if(selected == items[SEARCH]){
				searchPanel.fillDatas();
				searchPanel.getFirstFocusControl().forceFocus();
			}
			else if(selected == items[MANAGE]){
				managePanel.fillDatas();
			}
			else if(selected == items[BROWSE]){
				browsePanel.fillDatas();
			}
		};

	}

}
