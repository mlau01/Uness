package uness.gui.options;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import uness.Uness.OPTION;
import uness.core.Option;
import uness.gui.UWin;
import uness.interfaces.Prefillable;

public class OptionMainPanel implements Prefillable {
	
	private final UWin uwin;
	private final Shell parent;
	
	private Shell dialog;

	private Button valid;
	private Button cancel;
	
	private TabFolder tabFolder;
	private TabItem baseTab;
	
	private Option manager;
	
	private Control[] ctls;
	public int CTL_USEDB = 0;
	public int CTL_DBURL = 1;
	public int CTL_DBNAME = 2;
	public int CTL_DBLOGIN = 3;
	public int CTL_DBPASSWD = 4;
	public int CTL_SIZE = 5;


	
	public OptionMainPanel(final UWin p_uwin, final Shell p_parent)
	{
		uwin = p_uwin;
		parent = p_parent;
		manager = uwin.getCorma().getOptionManager();
		ctls = new Control[CTL_SIZE];
	}
	
	public final Shell open()
	{
		dialog = new Shell(parent, SWT.TITLE | SWT.CLOSE | SWT.APPLICATION_MODAL);
		dialog.setText("Options");

		
		createWidgets();
		fillDatas();
		
		dialog.pack();
		
		dialog.setLocation(UWin.getPrefferedPopupPoint(parent, dialog));
		dialog.open();
		
		return dialog;
	}
	
	private void createWidgets()
	{
		GridLayout layout = new GridLayout();
		dialog.setLayout(layout);
		
		tabFolder = new TabFolder(dialog, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
		baseTab = new TabItem(tabFolder, SWT.NONE);
		baseTab.setText("Base de donnée");
		Composite baseComposite = new Composite(tabFolder, SWT.NONE);
		baseTab.setControl(baseComposite);
		{
			baseComposite.setLayout(new FillLayout(SWT.VERTICAL));
			
			Group base = new Group(baseComposite, SWT.NONE);
			base.setLayout(new GridLayout(2, true));
			
			
			Label useDbLabel = new Label(base, SWT.NONE);
			useDbLabel.setText("Type de base de donnée");
			useDbLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			
			ctls[CTL_USEDB] = new Combo(base, SWT.READ_ONLY | SWT.DROP_DOWN);
			((Combo)ctls[CTL_USEDB]).setItems(new String[]{"access", "mssql", "mysql"});
			ctls[CTL_USEDB].setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
	
			Label baseLbl = new Label(base, SWT.NONE);
			baseLbl.setText("URL Base de donnée");
			baseLbl.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
	
			ctls[CTL_DBURL] = new Text(base, SWT.BORDER);
			GridData gdata = new GridData(GridData.FILL, GridData.CENTER, true, false);
			gdata.widthHint = 400;
			ctls[CTL_DBURL].setLayoutData(gdata);
			
			Label nameLbl = new Label(base, SWT.NONE);
			nameLbl.setText("Nom de la base de donnée");
			gdata = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
			gdata.horizontalSpan = 2;
			nameLbl.setLayoutData(gdata);
			
			ctls[CTL_DBNAME] = new Text(base, SWT.BORDER);
			gdata = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
			gdata.widthHint = 150;
			ctls[CTL_DBNAME].setLayoutData(gdata);
			
			Label loginLbl = new Label(base, SWT.NONE);
			loginLbl.setText("Login");
			loginLbl.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
			
			ctls[CTL_DBLOGIN] = new Text(base, SWT.BORDER);
			gdata = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
			gdata.widthHint = 150;
			ctls[CTL_DBLOGIN].setLayoutData(gdata);
	
	
			Label passLbl = new Label(base, SWT.NONE);
			passLbl.setText("Password");
			passLbl.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false, 2, 1));
	
			ctls[CTL_DBPASSWD] = new Text(base, SWT.PASSWORD | SWT.BORDER);
			gdata = new GridData(GridData.BEGINNING, GridData.CENTER, false, false);
			gdata.widthHint = 150;
			ctls[CTL_DBPASSWD].setLayoutData(gdata);
		}
		
		
		Composite bottom = new Composite(dialog, SWT.NONE);
		bottom.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, false));
		bottom.setLayout(new RowLayout());
		{
			valid = new Button(bottom, SWT.PUSH);
			valid.addListener(SWT.Selection, e -> action_valid());
			valid.setText("Appliquer");
			dialog.setDefaultButton(valid);
			
			cancel = new Button(bottom, SWT.PUSH);
			cancel.addListener(SWT.Selection, e -> dialog.dispose());
			cancel.setText("Fermer");
		}
	}
	//--Getters
	
	private void action_valid()
	{
		manager.setProperty(OPTION.useDb, ((Combo)ctls[CTL_USEDB]).getText());
		manager.setProperty(OPTION.dbUrl, ((Text)ctls[CTL_DBURL]).getText());
		manager.setProperty(OPTION.dbName, ((Text)ctls[CTL_DBNAME]).getText());
		manager.setProperty(OPTION.dbLogin, ((Text)ctls[CTL_DBLOGIN]).getText());
		manager.setProperty(OPTION.dbPasswd, ((Text)ctls[CTL_DBPASSWD]).getText());
		
		MessageBox info = new MessageBox(dialog, SWT.ICON_INFORMATION | SWT.OK);
		info.setMessage("L'application à besoin d'être relancé pour appliquer les changements");
		info.open();
		
		uwin.getShell().dispose();
	}

	@Override
	public void fillDatas() {
		Combo useDb = ((Combo)ctls[CTL_USEDB]);
		Text dbUrl = ((Text)ctls[CTL_DBURL]);
		Text dbName = ((Text)ctls[CTL_DBNAME]);
		Text dbLogin = ((Text)ctls[CTL_DBLOGIN]);
		Text dbPasswd = ((Text)ctls[CTL_DBPASSWD]);
		
        for(int i = 0; i < useDb.getItemCount(); i++)
        {
        	if(useDb.getItem(i).equals(manager.getProperty(OPTION.useDb))) useDb.select(i);
        }
        
        dbUrl.setText(manager.getProperty(OPTION.dbUrl));
        dbName.setText(manager.getProperty(OPTION.dbName));
        dbLogin.setText(manager.getProperty(OPTION.dbLogin));
        dbPasswd.setText(manager.getProperty(OPTION.dbPasswd));
		
	}
}
