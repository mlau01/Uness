package uness.gui.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;

import uness.Uness;
import uness.Uness.OPTION;
import uness.core.Manage;
import uness.core.Model;
import uness.core.Option;
import uness.core.Users;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.gui.common.AbstractPopupPanel;
import uness.gui.common.UControl;
import uness.gui.common.UTable;
import uness.interfaces.Prefillable;
import unessdb.DBException;
import unessdb.DBManagerInterface.Access;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;

public class ManagePanel implements Prefillable {
	
	private final UWin uWin;
	
	private Composite cPanel;
	private Users usersManager;
	private Manage manager;
	
	private final UTable[] table;
	public static final int TBL_LOCATION = 0;
	public static final int TBL_GROUP = 1;
	public static final int TBL_MODEL = 2;
	public static final int TBL_ENTERPRISE = 3;
	public static final int TBL_USER = 4;
	public static final int TBL_SIZE = 5;

	private Button[] btns;
	public static final int BTN_UNLOCK = 0;
	public static final int BTN_EXPORT = 1;
	public static final int BTN_LOCATION_ADD = 2;
	public static final int BTN_LOCATION_EDIT = 3;
	public static final int BTN_LOCATION_DEL = 4;
	public static final int BTN_GROUP_ADD = 5;
	public static final int BTN_GROUP_EDIT = 6;
	public static final int BTN_GROUP_DEL = 7;
	public static final int BTN_MODEL_ADD = 8;
	public static final int BTN_MODEL_EDIT = 9;
	public static final int BTN_MODEL_DEL = 10;
	public static final int BTN_ENTERPRISE_ADD = 11;
	public static final int BTN_ENTERPRISE_EDIT = 12;
	public static final int BTN_ENTERPRISE_DEL = 13;
	public static final int BTN_USER_ADD = 14;
	public static final int BTN_USER_EDIT = 15;
	public static final int BTN_USER_DEL = 16;
	public static final int BTN_PATTERN_TEST = 17;
	private int BTN_SIZE = 18;
	
	private UControl[] unpts;
	public static final int NPT_LOCATION_NAME = 0;
	public static final int NPT_LOCATION_GROUP = 1;
	public static final int NPT_LOCATION_COMMENT = 2;
	public static final int NPT_GROUP_NAME = 4;
	public static final int NPT_GROUP_PARENT = 5;
	public static final int NPT_GROUP_COMMENT = 6;
	public static final int NPT_MODEL_NAME = 7;
	public static final int NPT_MODEL_CONSTRUCTOR = 8;
	public static final int NPT_MODEL_SPATTERN = 9;
	public static final int NPT_MODEL_COMMENT = 10;
	public static final int NPT_ENTERPRISE_NAME = 11;
	public static final int NPT_USER_FULLNAME = 12;
	private int NPT_SIZE = 13;
	
	private Control[] rnpts;
	public static final int RNPT_PATTERN_SERIAL = 0;
	public static final int RNPT_PATTERN_PATTERN = 1;
	public static final int RNPT_PATTERN_RESULT = 2;
	private int RNPT_SIZE = 3;
	
	private final Option optionManager;
	private final Model modelManager;
	
	private final int CONTAINER_STYLE = SWT.NONE;

	private boolean btns_isLocked = true;
	private final UResource resMan;
	
	public ManagePanel(final UWin p_uWin)
	{
		uWin = p_uWin;
		resMan = uWin.getResourceManager();
		manager = uWin.getCorma().getManageManager();
		usersManager = uWin.getCorma().getUsersManager();
		
		btns = new Button[BTN_SIZE];
		unpts = new UControl[NPT_SIZE];
		table = new UTable[TBL_SIZE];
		rnpts = new Control[RNPT_SIZE];
		optionManager = uWin.getCorma().getOptionManager();
		modelManager = uWin.getCorma().getModelManager();
	}
	
	public Composite create(TabFolder tabFolder, int compositeStyle) {
		cPanel = new Composite(tabFolder, compositeStyle); 
		cPanel.setLayout(new GridLayout(3, true));
		
		createWidgets();
	
		
		for(int i = 0; i < btns.length; i++)
		{
			if(i != BTN_UNLOCK && i != BTN_EXPORT) btns[i].setEnabled( ! btns_isLocked);
		}

		return cPanel;
	}

	private void createWidgets() {
		
		Composite top = new Composite(cPanel, SWT.NONE);
		top.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 3, 1));
		{
			top.setLayout(new RowLayout(SWT.HORIZONTAL));
			
			btns[BTN_UNLOCK] = new Button(top, SWT.NONE);
			btns[BTN_UNLOCK].addListener(SWT.Selection, e -> action_unlock());
			btns[BTN_UNLOCK].setText("Déverouiller");
			
			
			btns[BTN_EXPORT] = new Button(top, SWT.PUSH);
			btns[BTN_EXPORT].addListener(SWT.Selection, e -> action_export_unescan());
			btns[BTN_EXPORT].setText("Exporter la config Unescan");
		}
		
		
		Group location = new Group(cPanel, SWT.NONE);
		location.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 2));
		{
			location.setText("Localisations");
			location.setLayout(new GridLayout(3, false));
			
			btns[BTN_LOCATION_ADD] = new Button(location, SWT.NONE);
			btns[BTN_LOCATION_ADD].addListener(SWT.Selection, e -> action_location_add());
			btns[BTN_LOCATION_ADD].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			btns[BTN_LOCATION_ADD].setText("Ajouter");
			
			btns[BTN_LOCATION_EDIT] = new Button(location, SWT.NONE);
			btns[BTN_LOCATION_EDIT].addListener(SWT.Selection, e -> action_location_edit());
			btns[BTN_LOCATION_EDIT].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			btns[BTN_LOCATION_EDIT].setText("Modifier");
			
			btns[BTN_LOCATION_DEL] = new Button(location, SWT.NONE);
			btns[BTN_LOCATION_DEL].setText("Supprimer");
			btns[BTN_LOCATION_DEL].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			
			new Label(location, SWT.NONE).setText(FIELD.LOCATION_location.getDisplayName());
			new Label(location, SWT.NONE).setText(FIELD.LOCATION_group.getDisplayName());
			new Label(location, SWT.NONE).setText(FIELD.LOCATION_comment.getDisplayName());
			
			unpts[NPT_LOCATION_NAME] = new UControl(uWin, FIELD.LOCATION_location, location, SWT.BORDER);
			unpts[NPT_LOCATION_GROUP] = new UControl(uWin, FIELD.LOCATION_group, location, SWT.BORDER);
			unpts[NPT_LOCATION_COMMENT] = new UControl(uWin, FIELD.LOCATION_comment, location, SWT.BORDER);
			
			table[TBL_LOCATION] = new UTable(uWin, TABLE.LOCATION.getFields(), OPTION.table_manage_location_show, OPTION.table_manage_location_sort, OPTION.table_manage_location_sens){
				
				protected void action_clickColumn(){
					action_location_draw();
				}
				@Override
				protected void action_clickTableItem(UData tableItem) {
					action_location_select(tableItem);
				}
			};
			Table rawTable = table[TBL_LOCATION].create(location, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
			rawTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));
			
		
		}
		
		Group group = new Group(cPanel, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			group.setText("Groupes");
			group.setLayout(new GridLayout(3, false));
			
			btns[BTN_GROUP_ADD] = new Button(group, SWT.NONE);
			btns[BTN_GROUP_ADD].addListener(SWT.Selection, e -> action_group_add());
			btns[BTN_GROUP_ADD].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			btns[BTN_GROUP_ADD].setText("Ajouter");
			
			btns[BTN_GROUP_EDIT] = new Button(group, SWT.NONE);
			btns[BTN_GROUP_EDIT].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			btns[BTN_GROUP_EDIT].setText("Modifier");
			
			btns[BTN_GROUP_DEL] = new Button(group, SWT.NONE);
			btns[BTN_GROUP_DEL].setText("Supprimer");
			btns[BTN_GROUP_DEL].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			
			new Label(group, SWT.NONE).setText(FIELD.GROUP_group.getDisplayName());
			new Label(group, SWT.NONE).setText(FIELD.GROUP_parentgroup.getDisplayName());
			new Label(group, SWT.NONE).setText(FIELD.GROUP_comment.getDisplayName());
			
			unpts[NPT_GROUP_NAME] = new UControl(uWin, FIELD.GROUP_group, group, SWT.BORDER);
			unpts[NPT_GROUP_PARENT] = new UControl(uWin, FIELD.GROUP_parentgroup, group, SWT.BORDER);
			unpts[NPT_GROUP_COMMENT] = new UControl(uWin, FIELD.GROUP_comment, group, SWT.BORDER);
			
			table[TBL_GROUP] = new UTable(uWin, TABLE.GROUP.getFields(), OPTION.table_manage_group_show,OPTION.table_manage_group_sort, OPTION.table_manage_group_sens){
				@Override
				protected void action_clickTableItem(UData tableItem) {
					action_group_select(tableItem);
				}
			};
			Table rawTable = table[TBL_GROUP].create(group, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
			rawTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));
			
		
		}
		
		Group model = new Group(cPanel, SWT.NONE);
		model.setText("Modèles");
		model.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			model.setLayout(new GridLayout());
			
			Composite buttons = new Composite(model, CONTAINER_STYLE);
			{
				buttons.setLayout(new GridLayout(3, false));
				
				btns[BTN_MODEL_ADD] = new Button(buttons, SWT.NONE);
				btns[BTN_MODEL_ADD].addListener(SWT.Selection, e -> action_model_add());
				btns[BTN_MODEL_ADD].setText("Ajouter");
				
				btns[BTN_MODEL_EDIT] = new Button(buttons, SWT.NONE);
				btns[BTN_MODEL_EDIT].addListener(SWT.Selection, e -> action_model_edit());
				btns[BTN_MODEL_EDIT].setText("Modifier");
				
				btns[BTN_MODEL_DEL] = new Button(buttons, SWT.NONE);
				//TODO DEL LISTENER
				btns[BTN_MODEL_DEL].setText("Supprimer");
			}
			
			Composite inputs = new Composite(model, CONTAINER_STYLE);
			{
				inputs.setLayout(new GridLayout(4, false));
				
				new Label(inputs, SWT.NONE).setText(FIELD.MODEL_model.getDisplayName());
				new Label(inputs, SWT.NONE).setText(FIELD.MODEL_constructor.getDisplayName());
				
				new Label(inputs, SWT.NONE).setText(FIELD.MODEL_serial_pattern.getDisplayName());
				new Label(inputs, SWT.NONE).setText(FIELD.MODEL_comment.getDisplayName());
				
				unpts[NPT_MODEL_NAME] = new UControl(uWin, FIELD.MODEL_model, inputs, SWT.BORDER);
				unpts[NPT_MODEL_CONSTRUCTOR] = new UControl(uWin, FIELD.MODEL_constructor, inputs, SWT.BORDER);
				unpts[NPT_MODEL_SPATTERN] = new UControl(uWin, FIELD.MODEL_serial_pattern, inputs, SWT.BORDER);
				unpts[NPT_MODEL_SPATTERN].getWidget().setToolTipText("Le pattern sert à identifier le modèle dans l'application de scan\n"
						+ ". symbolise n'importe quel caractère\n"
						+ "^ symbolise le début de la ligne\n"
						+ "* suivant un symbole veut dire qu'il peut être répéter à l'infinit");
				unpts[NPT_MODEL_COMMENT] = new UControl(uWin, FIELD.MODEL_comment, inputs, SWT.BORDER);
			}
			
			table[TBL_MODEL] = new UTable(uWin, TABLE.MODEL.getFields(), OPTION.table_manage_model_show, OPTION.table_manage_model_sort, OPTION.table_manage_model_sens) {
				
				@Override
				protected void action_clickTableItem(UData tableItem) {
					action_model_select(tableItem);
				}
				
				@Override
				protected void action_clickColumn() {
					action_model_draw();
				}
			};
			table[TBL_MODEL].create(model, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
			table[TBL_MODEL].getTable().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			
			Group tpattern = new Group(model, CONTAINER_STYLE);
			tpattern.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			
			tpattern.setText("Pattern testeur");
			{
				tpattern.setLayout(new GridLayout(4,false));
				
				rnpts[RNPT_PATTERN_SERIAL] = new Text(tpattern, SWT.BORDER);
				((Text)rnpts[RNPT_PATTERN_SERIAL]).setMessage("Serial");
				rnpts[RNPT_PATTERN_SERIAL].setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
				
				rnpts[RNPT_PATTERN_PATTERN] = new Text(tpattern, SWT.BORDER);
				((Text)rnpts[RNPT_PATTERN_PATTERN]).setMessage("Pattern");
				rnpts[RNPT_PATTERN_PATTERN].setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			
				btns[BTN_PATTERN_TEST] = new Button(tpattern, SWT.PUSH);
				btns[BTN_PATTERN_TEST].addListener(SWT.Selection, e -> action_pattern_test());
				btns[BTN_PATTERN_TEST].setText("TEST");
				
				rnpts[RNPT_PATTERN_RESULT] = new Text(tpattern, SWT.BORDER | SWT.READ_ONLY);
				rnpts[RNPT_PATTERN_RESULT].setLayoutData(new GridData(30, 15));
			}
		}
		
		Group enterprise = new Group(cPanel, SWT.NONE);
		enterprise.setText("Entreprise");
		enterprise.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			enterprise.setLayout(new GridLayout());
			
			Composite buttons = new Composite(enterprise, CONTAINER_STYLE);
			{
				buttons.setLayout(new GridLayout(3, false));
	
				btns[BTN_ENTERPRISE_ADD] = new Button(buttons, SWT.NONE);
				btns[BTN_ENTERPRISE_ADD].addListener(SWT.Selection, e -> action_enterprise_add());
				btns[BTN_ENTERPRISE_ADD].setText("Ajouter");
				
				btns[BTN_ENTERPRISE_EDIT] = new Button(buttons, SWT.NONE);
				btns[BTN_ENTERPRISE_EDIT].addListener(SWT.Selection, e -> action_enterprise_edit());
				btns[BTN_ENTERPRISE_EDIT].setText("Modifier");
				
				btns[BTN_ENTERPRISE_DEL] = new Button(buttons, SWT.NONE);
				btns[BTN_ENTERPRISE_DEL].addListener(SWT.Selection, e -> action_enterprise_delete());
				btns[BTN_ENTERPRISE_DEL].setText("Supprimer");
			}
			
			Composite inputs = new Composite(enterprise, CONTAINER_STYLE);
			{
				inputs.setLayout(new GridLayout(3, false));
				
				new Label(inputs, SWT.NONE).setText(FIELD.ENTERPRISE_enterprise.getDisplayName());
				
				unpts[NPT_ENTERPRISE_NAME] = new UControl(uWin, FIELD.ENTERPRISE_enterprise, inputs, SWT.BORDER);
				unpts[NPT_ENTERPRISE_NAME].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 3, 1));
			}
			
			table[TBL_ENTERPRISE] = new UTable(uWin, TABLE.ENTERPRISE.getFields(), OPTION.table_manage_enterprise_show, OPTION.table_manage_enterprise_sort, OPTION.table_manage_enterprise_sens){
				@Override
				protected void action_clickTableItem(UData tableItem) {
					action_enterprise_select(tableItem);
				}
			};
			Table rawTable = table[TBL_ENTERPRISE].create(enterprise, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
			rawTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));
			
		}
		
		Group user = new Group(cPanel, SWT.NONE);
		user.setText("Utilisateurs");
		user.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			user.setLayout(new GridLayout(3, false));
			
			btns[BTN_USER_ADD] = new Button(user, SWT.NONE);
			btns[BTN_USER_ADD].addListener(SWT.Selection, e -> action_user_add());
			btns[BTN_USER_ADD].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			btns[BTN_USER_ADD].setText("Ajouter");
			
			btns[BTN_USER_EDIT] = new Button(user, SWT.NONE);
			btns[BTN_USER_EDIT].addListener(SWT.Selection, e -> action_user_edit());
			btns[BTN_USER_EDIT].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			btns[BTN_USER_EDIT].setText("Modifier");
			
			btns[BTN_USER_DEL] = new Button(user, SWT.NONE);
			btns[BTN_USER_DEL].addListener(SWT.Selection, e -> action_user_delete());
			btns[BTN_USER_DEL].setText("Supprimer");
			btns[BTN_USER_DEL].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			
			new Label(user, SWT.NONE).setText(FIELD.USER_fullname.getDisplayName());
			/*
			new Label(user, SWT.NONE).setText(FIELD.USER_level.getDisplayName());
			new Label(user, SWT.NONE).setText(FIELD.USER_w_welcomepage.getDisplayName());
			*/
			unpts[NPT_USER_FULLNAME] = new UControl(uWin, FIELD.USER_fullname, user, SWT.BORDER);
			unpts[NPT_USER_FULLNAME].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 3, 1));
			
			/*
			user_controls[1] = new UControl(uWin, FIELD.USER_level, user, SWT.BORDER);
			user_controls[2] = new UControl(uWin, FIELD.USER_w_welcomepage, user, SWT.BORDER);
			*/
			
			table[TBL_USER] = new UTable(uWin, TABLE.USER.getFields(), OPTION.table_manage_user_show, OPTION.table_manage_user_sort, OPTION.table_manage_user_sens){
				@Override
				protected void action_clickTableItem(UData tableItem) {
					action_user_select(tableItem);
				}
			};
			Table rawTable = table[TBL_USER].create(user, SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
			rawTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));
			
			
		}
	}

	@Override
	public void fillDatas() {
		unpts[NPT_LOCATION_GROUP].clear();
		unpts[NPT_LOCATION_GROUP].fillDatas();
		unpts[NPT_GROUP_PARENT].clear();
		unpts[NPT_GROUP_PARENT].fillDatas();
		unpts[NPT_MODEL_CONSTRUCTOR].clear();
		unpts[NPT_MODEL_CONSTRUCTOR].fillDatas();
		
		action_location_draw();
		action_model_draw();
		
		try {
			table[TBL_GROUP].draw(manager.getGroups());
			
			table[TBL_ENTERPRISE].draw(manager.getEnterprise());
			table[TBL_USER].draw(uWin.getCorma().getUsersManager().getUserList());
			
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		
		

	}
	
	//######################### ACTIONS ###########################
	private void action_model_draw(){
		FIELD modelSort = FIELD.valueOf(optionManager.getProperty(Uness.OPTION.table_manage_model_sort));
		Access modelSens = Access.valueOf(optionManager.getProperty(Uness.OPTION.table_manage_model_sens));
		
		try {
			table[TBL_MODEL].draw(modelManager.getModels(modelSort, modelSens));
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}	
	}
	private void action_location_draw()
	{
		FIELD locationSort = FIELD.valueOf(optionManager.getProperty(Uness.OPTION.table_manage_location_sort));
		Access locationSens = Access.valueOf(optionManager.getProperty(Uness.OPTION.table_manage_location_sens));
		
		try {
			table[TBL_LOCATION].draw(manager.getLocations(null, locationSort, locationSens));
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
	}
	
	private void action_pattern_test()
	{
		String test = ((Text)rnpts[ManagePanel.RNPT_PATTERN_SERIAL]).getText();
		String pattern = ((Text)rnpts[ManagePanel.RNPT_PATTERN_PATTERN]).getText();
		Text result = (Text)rnpts[ManagePanel.RNPT_PATTERN_RESULT];
		if(test.matches(pattern)) {
			result.setText("OK");
			result.setBackground(resMan.get_color(UResource.COLOR_GREEN));
		} else
		{
			result.setText("NOK");
			result.setBackground(resMan.get_color(UResource.COLOR_RED));
		}
	}
	
	//### ENTERPRISE ###
	
	private void action_enterprise_select(UData datas)
	{
		unpts[NPT_ENTERPRISE_NAME].setText(String.valueOf(datas.getValue(FIELD.ENTERPRISE_enterprise)));
	}
	private void action_enterprise_add()
	{
		String enterpriseName = unpts[NPT_ENTERPRISE_NAME].getText();
		if(enterpriseName.isEmpty()) return;
		
		try {
			manager.addEnterprise(enterpriseName);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		unpts[ManagePanel.NPT_ENTERPRISE_NAME].clear();
		fillDatas();
	}
	private void action_enterprise_edit()
	{
		String enterpriseName = unpts[ManagePanel.NPT_ENTERPRISE_NAME].getText();
		if(enterpriseName.isEmpty()) return;

		UData datas = table[TBL_ENTERPRISE].getSelection();
		if(datas == null) return;
		
		
		try {
			manager.editEnterprise(String.valueOf(datas.getValue(FIELD.ENTERPRISE_id)), enterpriseName);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		unpts[ManagePanel.NPT_ENTERPRISE_NAME].clear();
		fillDatas();
	}
	private void action_enterprise_delete()
	{
		//TODO
	}
	
	//### USER ###
	
	private void action_user_select(UData datas)
	{
		unpts[ManagePanel.NPT_USER_FULLNAME].setText(String.valueOf(datas.getValue(FIELD.USER_fullname)));
	}
	
	private void action_user_add()
	{
		String fullname = unpts[ManagePanel.NPT_USER_FULLNAME].getText();
		if(fullname.isEmpty()) return;
		
		try {
			usersManager.addUser(fullname);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		unpts[ManagePanel.NPT_USER_FULLNAME].clear();
		fillDatas();
	}
	
	private void action_user_edit()
	{
		String userFullname = unpts[ManagePanel.NPT_USER_FULLNAME].getText();
		if(userFullname.isEmpty()) return;
		
		UData datas = table[TBL_USER].getSelection();
		if(datas == null) return;
		
		try {
			usersManager.editUser(String.valueOf(datas.getValue(FIELD.USER_id)), userFullname);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		unpts[ManagePanel.NPT_USER_FULLNAME].clear();
		fillDatas();
	}
	
	private void action_user_delete()
	{
		UData datas = table[TBL_USER].getSelection();
		if(datas == null) return;
		
		try {
			usersManager.removeUser(String.valueOf(datas.getValue(FIELD.USER_id)));
		} catch (DBException e)
		{
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		unpts[ManagePanel.NPT_USER_FULLNAME].clear();
		fillDatas();
	}
	
	//### UNLOCK ###
	
	private void action_unlock()
	{
		
		if(btns_isLocked) {
			int ask = new AbstractPopupPanel(uWin) {
				private Text password;
				@Override
				protected void widgets() {
					password = new Text(this.dialog, SWT.BORDER | SWT.PASSWORD);
				}
				@Override
				protected int actionValid() {
					if(password.getText().equals(Uness.MANAGE_PASSWORD))
						return 0;
					else {
						uwin.showError("Password", "Password incorrecte");
						return 1;
					}
				}
				@Override
				protected String setTitle() {
					return "Password requis";
				}
				@Override
				protected void loadDatas() {
				}
				
			}.open();

			if(ask == 0) {
				btns_isLocked = false;
				btns[ManagePanel.BTN_UNLOCK].setText("Vérouiller");
			}
		}
		else { 
			btns_isLocked = true;
			btns[ManagePanel.BTN_UNLOCK].setText("Dévérouiller");
		}
		
		for(int i = 0; i < btns.length; i++)
		{
			if(i != ManagePanel.BTN_UNLOCK) btns[i].setEnabled(! btns_isLocked);
		}

	}
	
	//### EXPORT UNESCAN CONFIG ###
	
	private void action_export_unescan()
	{
		FileDialog savefile = new FileDialog(uWin.getShell(), SWT.SAVE);
		savefile.setOverwrite(true);
		savefile.setFileName("config.xml");
		savefile.setFilterExtensions(new String[] { ".xml" });
		String filepath = savefile.open();
		if (filepath != null)
		{
			try {
				ArrayList<UData> enterpriseData = uWin.getCorma().getManageManager().getEnterprise();
				ArrayList<UData> models = uWin.getCorma().getModelManager().getModels(null, null);
				ArrayList<UData> locations = uWin.getCorma().getManageManager().getLocations(null, null, null);
				ArrayList<UData> groups = uWin.getCorma().getManageManager().getGroups();
				uWin.getCorma().getExportManager().exportUnescan(filepath, models, enterpriseData, groups, locations);
			} catch (DBException | IOException ex) {
				ex.printStackTrace();
				uWin.showError(ex.getClass().getName(), ex.getMessage());
			}
		}
	}
	
	//### LOCATIONS ###
	
	private void action_location_select(UData datas)
	{	
		unpts[ManagePanel.NPT_LOCATION_NAME].setText(String.valueOf(datas.getValue(FIELD.LOCATION_location)));
		unpts[ManagePanel.NPT_LOCATION_GROUP].setText(String.valueOf(datas.getValue(FIELD.LOCATION_group)));
		unpts[ManagePanel.NPT_LOCATION_COMMENT].setText(String.valueOf(datas.getValue(FIELD.LOCATION_comment)));
	}
	
	private void action_location_add()
	{	
		String location = unpts[ManagePanel.NPT_LOCATION_NAME].getText();
		String group = unpts[ManagePanel.NPT_LOCATION_GROUP].getText();
		String comment = unpts[ManagePanel.NPT_LOCATION_COMMENT].getText();
		if(location.isEmpty() || group.isEmpty()) return;
		
		try {
			manager.addLocation(location, group, comment);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		unpts[ManagePanel.NPT_LOCATION_NAME].clear();
		unpts[ManagePanel.NPT_LOCATION_GROUP].clear();
		unpts[ManagePanel.NPT_LOCATION_COMMENT].clear();
		fillDatas();
	}
	
	private void action_location_edit()
	{
		String location = unpts[ManagePanel.NPT_LOCATION_NAME].getText();
		String group = unpts[ManagePanel.NPT_LOCATION_GROUP].getText();
		String comment = unpts[ManagePanel.NPT_LOCATION_COMMENT].getText();
		if(location.isEmpty() || group.isEmpty()) return;
		
		UData datas = table[TBL_LOCATION].getSelection();
		if(datas == null) return;
		
		String id = String.valueOf(datas.getValue(FIELD.LOCATION_id));
		
		try {
			manager.editLocation(id, location, group, comment);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		unpts[ManagePanel.NPT_LOCATION_NAME].clear();
		unpts[ManagePanel.NPT_LOCATION_GROUP].clear();
		unpts[ManagePanel.NPT_LOCATION_COMMENT].clear();
		fillDatas();
		
	}
	
	private void action_location_delete()
	{
		//TODO
	}
	
	//### GROUPS ###
	
	private void action_group_select(UData datas)
	{	
		unpts[ManagePanel.NPT_GROUP_NAME].setText(String.valueOf(datas.getValue(FIELD.GROUP_group)));
		unpts[ManagePanel.NPT_GROUP_PARENT].setText(String.valueOf(datas.getValue(FIELD.GROUP_parentgroup)));
		unpts[ManagePanel.NPT_GROUP_COMMENT].setText(String.valueOf(datas.getValue(FIELD.GROUP_comment)));
	}
	
	private void action_group_add()
	{
		String group = unpts[ManagePanel.NPT_GROUP_NAME].getText();
		String parentGroup = unpts[ManagePanel.NPT_GROUP_PARENT].getText();
		String comment = unpts[ManagePanel.NPT_GROUP_COMMENT].getText();
		if(group.isEmpty() || parentGroup.isEmpty()) return;
		
		try {
			manager.addGroup(group, parentGroup, comment);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		unpts[ManagePanel.NPT_GROUP_NAME].clear();
		unpts[ManagePanel.NPT_GROUP_PARENT].clear();
		unpts[ManagePanel.NPT_GROUP_COMMENT].clear();
		fillDatas();
	}
	
	private void action_group_edit()
	{
		String group = unpts[ManagePanel.NPT_GROUP_NAME].getText();
		String parentGroup = unpts[ManagePanel.NPT_GROUP_PARENT].getText();
		String comment = unpts[ManagePanel.NPT_GROUP_COMMENT].getText();
		if(group.isEmpty() || parentGroup.isEmpty()) return;
		
		UData datas = table[TBL_GROUP].getSelection();
		if(datas == null) return;
		//TODO
	}
	
	private void action_group_delete()
	{
		//TODO
	}
	
	//### MODELS ###
	
	private void action_model_select(UData datas)
	{	
		unpts[ManagePanel.NPT_MODEL_NAME].setText(String.valueOf(datas.getValue(FIELD.MODEL_model)));
		unpts[ManagePanel.NPT_MODEL_CONSTRUCTOR].setText(String.valueOf(datas.getValue(FIELD.MODEL_constructor)));
		unpts[ManagePanel.NPT_MODEL_SPATTERN].setText(String.valueOf(datas.getValue(FIELD.MODEL_serial_pattern)));
		unpts[ManagePanel.NPT_MODEL_COMMENT].setText(String.valueOf(datas.getValue(FIELD.MODEL_comment)));
	}
	private void action_model_add()
	{
		String model = unpts[ManagePanel.NPT_MODEL_NAME].getText();
		String maker = unpts[ManagePanel.NPT_MODEL_CONSTRUCTOR].getText();
		String serial_pattern = unpts[ManagePanel.NPT_MODEL_SPATTERN].getText();
		String comment = unpts[ManagePanel.NPT_MODEL_COMMENT].getText();
		if(model.isEmpty() || maker.isEmpty()) return;
		
		if( ! serial_pattern.isEmpty()){
			try {
			Pattern.compile(serial_pattern);
			} catch (PatternSyntaxException e)
			{
				uWin.showError(e.getClass().getName(), e.getMessage());
				return;
			}
		}
		
		try {
			modelManager.addModel(model, maker, serial_pattern, comment);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		fillDatas();
		
		unpts[ManagePanel.NPT_MODEL_NAME].clear();
		unpts[ManagePanel.NPT_MODEL_CONSTRUCTOR].clear();
		unpts[ManagePanel.NPT_MODEL_SPATTERN].clear();
		unpts[ManagePanel.NPT_MODEL_COMMENT].clear();
	}
	
	private void action_model_edit()
	{
		String model = unpts[ManagePanel.NPT_MODEL_NAME].getText();
		String maker = unpts[ManagePanel.NPT_MODEL_CONSTRUCTOR].getText();
		String serial_pattern = unpts[ManagePanel.NPT_MODEL_SPATTERN].getText();
		String comment = unpts[ManagePanel.NPT_MODEL_COMMENT].getText();
		if(model.isEmpty() || maker.isEmpty()) return;
		
		if( ! serial_pattern.isEmpty()){
			try {
			Pattern.compile(serial_pattern);
			} catch (PatternSyntaxException e)
			{
				uWin.showError(e.getClass().getName(), e.getMessage());
				return;
			}
		}
		
		UData datas = table[TBL_MODEL].getSelection();
		
		String id = String.valueOf(datas.getValue(FIELD.MODEL_id));

		try {
			modelManager.editModel(id, model, maker, serial_pattern, comment);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		unpts[ManagePanel.NPT_MODEL_NAME].clear();
		unpts[ManagePanel.NPT_MODEL_CONSTRUCTOR].clear();
		unpts[ManagePanel.NPT_MODEL_SPATTERN].clear();
		unpts[ManagePanel.NPT_MODEL_COMMENT].clear();
		fillDatas();
		
	}
	
	private void action_model_delete()
	{
		//TODO
	}
}
