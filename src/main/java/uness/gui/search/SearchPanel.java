package uness.gui.search;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import uness.Uness;
import uness.Uness.OPTION;
import uness.gui.UWin;
import uness.gui.common.DateUtils;
import uness.gui.common.UTable;
import uness.interfaces.Prefillable;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQLParams;
import unessdb.usql.USQL.OPERATOR;

public class SearchPanel implements Prefillable{
	
	private final UWin uWin;
	
	private Composite c_panel;
	private final int CONTAINER_STYLE = SWT.NONE;
	
	private UTable uTable;
	
	private final Combo[] operators;
	private final String[] opelist = {
			"",
			OPERATOR.GTE.get(),
			OPERATOR.LTE.get(),
			OPERATOR.EQ.get(),
			OPERATOR.BET.get()
	};
	
	public final String CUSTOM = "Personnalisé";
	public final String WARRANTY6M = "Garantie 6 mois restant";
	public final String WARRANTY1Y = "Garantie 1 ans restant";
	public final String WARRANTYEXPIRED = "Garantie Expirée";
	
	private final Control[] ctls;
	public final int PRESET = 0;
	public final int MODEL = 1;
	public final int SERIAL = 2;
	public final int LOCATION = 3;
	public final int IDCLIENT = 4;
	public final int COMMENT = 5;
	public final int OWNER = 6;
	public final int PURCHASE = 7;
	public final int PURCHASE2 = 8;
	public final int WAEXPIRE = 9;
	public final int WAEXPIRE2 = 10;
	public final int CTL_SIZE = 11;
	
	private final Button[] buttons;
	public final int SEARCH = 0;
	public final int EXPORT = 1;
	public final int BTN_SIZE = 2;
	
	public SearchPanel(final UWin p_uWin)
	{
		uWin = p_uWin;
		ctls = new Control[CTL_SIZE];
		operators = new Combo[CTL_SIZE];

		buttons = new Button[BTN_SIZE];
	}
	
	public Composite create(TabFolder tabFolder, int compositeStyle) {
		c_panel = new Composite(tabFolder, compositeStyle); 
		c_panel.setLayout(new GridLayout(2, false));
		
		{
			Composite c_input = new Composite(c_panel, CONTAINER_STYLE);
			c_input.setLayoutData(new GridData(GridData.BEGINNING, GridData.FILL, false, true));
			{
				c_input.setLayout(new GridLayout(1, false));
				Label[] sep = new Label[6];
				
				new Label(c_input, SWT.NONE).setText("Preset");
				ctls[PRESET] = new Combo(c_input, SWT.READ_ONLY);
				ctls[PRESET].addListener(SWT.Selection, e -> action_preset_select(e));
				ctls[PRESET].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				
				sep[0] = new Label(c_input, SWT.SEPARATOR | SWT.HORIZONTAL);
				
				new Label(c_input, SWT.NONE).setText(FIELD.DEVICE_serial.getDisplayName());
				ctls[SERIAL] = new Text(c_input, SWT.BORDER);
				ctls[SERIAL].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			
				new Label(c_input, SWT.NONE).setText(FIELD.DEVICE_idclient.getDisplayName());
				ctls[IDCLIENT] = new Text(c_input, SWT.BORDER);
				ctls[IDCLIENT].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				
				sep[1] = new Label(c_input, SWT.SEPARATOR | SWT.HORIZONTAL);
				
				new Label(c_input, SWT.NONE).setText(FIELD.DEVICE_location.getDisplayName());
				ctls[LOCATION] = new Combo(c_input, SWT.NONE);
				ctls[LOCATION].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				
				new Label(c_input, SWT.NONE).setText(FIELD.DEVICE_model.getDisplayName());
				ctls[MODEL] = new Combo(c_input, SWT.NONE);
				ctls[MODEL].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				
				new Label(c_input, SWT.NONE).setText(FIELD.DEVICE_owner.getDisplayName());
				ctls[OWNER] = new Combo(c_input, SWT.NONE);
				ctls[OWNER].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				
				sep[2] = new Label(c_input, SWT.SEPARATOR | SWT.HORIZONTAL);
				
				new Label(c_input, SWT.NONE).setText(FIELD.DEVICE_comment.getDisplayName());
				ctls[COMMENT] = new Text(c_input, SWT.BORDER);
				ctls[COMMENT].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
				
				sep[3] = new Label(c_input, SWT.SEPARATOR | SWT.HORIZONTAL);
				
				Composite c1 = new Composite(c_input, CONTAINER_STYLE);
				{
					c1.setLayout(new GridLayout(2, false));
					
					Label c1l1 = new Label(c1, SWT.NONE);
					c1l1.setText(FIELD.DEVICE_purchasedate.getDisplayName());
					c1l1.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false, 2, 1));
					
					operators[PURCHASE] = new Combo(c1, SWT.READ_ONLY);
					operators[PURCHASE].addListener(SWT.Selection, e -> {
						String value = ((Combo)e.widget).getText();
						ctls[PURCHASE].setEnabled( ! value.isEmpty());
						ctls[PURCHASE2].setEnabled( ! value.isEmpty() && value.equals("><"));
					});
					operators[PURCHASE].setLayoutData(new GridData(15, 10));
					ctls[PURCHASE] = new DateTime(c1, SWT.BORDER);
					ctls[PURCHASE].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
					
					ctls[PURCHASE].setEnabled(false);
					new Label(c1, SWT.NONE);
					ctls[PURCHASE2] = new DateTime(c1, SWT.BORDER);
					ctls[PURCHASE2].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
					ctls[PURCHASE2].setEnabled(false);
					
					Label c1l3 = new Label(c1, SWT.NONE);
					c1l3.setText("Garantie expiration");
					c1l3.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, true, false, 2, 1));
					
					operators[WAEXPIRE] = new Combo(c1, SWT.READ_ONLY);
					operators[WAEXPIRE].addListener(SWT.Selection, e -> {
						String value = ((Combo)e.widget).getText();
						ctls[WAEXPIRE].setEnabled( ! value.isEmpty());
						ctls[WAEXPIRE2].setEnabled( ! value.isEmpty() && value.equals("><"));
					});
					operators[WAEXPIRE].setLayoutData(new GridData(15, 10));
					ctls[WAEXPIRE] = new DateTime(c1, SWT.BORDER);
					ctls[WAEXPIRE].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
					ctls[WAEXPIRE].setEnabled(false);
					new Label(c1, SWT.NONE);
					ctls[WAEXPIRE2] = new DateTime(c1, SWT.BORDER);
					ctls[WAEXPIRE2].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
					ctls[WAEXPIRE2].setEnabled(false);
					
				}
				
				for(Control c : ctls) c.addListener(SWT.KeyDown, e -> {
					if(e.keyCode == 13 || e.keyCode == 16777296) action_search();
				});
				for(Combo op : operators) {
					if(op != null) {
						op.addListener(SWT.KeyDown, e -> {
							if(e.keyCode == 13 || e.keyCode == 16777296) action_search();
						});
					}
				}
				
				sep[4] = new Label(c_input, SWT.SEPARATOR | SWT.HORIZONTAL);
				
				
				buttons[SEARCH] = new Button(c_input, SWT.PUSH);
				buttons[SEARCH].setText("Chercher");
				buttons[SEARCH].addListener(SWT.Selection, e -> action_search());
				buttons[SEARCH].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1));
				
				sep[5] = new Label(c_input, SWT.SEPARATOR | SWT.HORIZONTAL);
				
				buttons[EXPORT] = new Button(c_input, SWT.PUSH);
				buttons[EXPORT].setText("Exporter");
				buttons[EXPORT].addListener(SWT.Selection, e -> action_export());
				buttons[EXPORT].setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false, 2, 1));
				
				for(Label l : sep) l.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
			}
			
			
			
			uTable = new UTable(uWin, TABLE.DEVICE.getFields(), OPTION.table_search_show, OPTION.table_search_sort, OPTION.table_search_sens){
				public void action_clickColumn()
				{
					action_search();
				}

				public Menu menuContent()
				{
					return null;
				}
				public void action_clickTableItem(UData item)
				{
					//System.out.println(item);
				}
			};
			
			Table table = uTable.create(c_panel, SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.BORDER);
			table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		}
		
		return c_panel;
	}
	
	@Override
	public void fillDatas() {
		uness.core.Control cm = uWin.getCorma().getControlManager();
		try {
			((Combo)ctls[LOCATION]).setItems(cm.getStringArray(FIELD.LOCATION_location));
			((Combo)ctls[MODEL]).setItems(cm.getStringArray(FIELD.MODEL_model));
			((Combo)ctls[OWNER]).setItems(cm.getStringArray(FIELD.ENTERPRISE_enterprise));
		} catch (DBException e)
		{
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		((Combo)ctls[PRESET]).setItems(new String[] {CUSTOM, WARRANTY6M, WARRANTY1Y, WARRANTYEXPIRED});
		((Combo)ctls[PRESET]).select(0);

		for(Combo c : operators)
		{
			if(c != null) {
				c.setItems(opelist);
				c.select(0);
			}
		}
		
	}
	
	private void action_preset_select(Event e)
	{
		Combo preset = (Combo)ctls[PRESET];
		String selection = preset.getItems()[preset.getSelectionIndex()];
		Combo operatorWarrantyExpire = operators[WAEXPIRE];
		DateTime waExpire = (DateTime)ctls[WAEXPIRE];
		DateTime waExpire2 = (DateTime)ctls[WAEXPIRE2];
		if(selection.equals(CUSTOM)){
			operatorWarrantyExpire.select(0);
			Calendar cal = GregorianCalendar.getInstance();
			DateUtils.setDateTimeValue(waExpire2, cal.getTime());
		}
		else if(selection.equals(WARRANTY6M)){
			operatorWarrantyExpire.select(4);
			Calendar cal = GregorianCalendar.getInstance();
			cal.add(Calendar.MONTH, 6);
			DateUtils.setDateTimeValue(waExpire2, cal.getTime());
		}
		else if(selection.equals(WARRANTY1Y)){
			operatorWarrantyExpire.select(4);
			Calendar cal = GregorianCalendar.getInstance();
			cal.add(Calendar.YEAR, 1);
			DateUtils.setDateTimeValue(waExpire2, cal.getTime());
		}
		else if(selection.equals(WARRANTYEXPIRED)){
			operatorWarrantyExpire.select(2);
			Calendar cal = GregorianCalendar.getInstance();
			DateUtils.setDateTimeValue(waExpire, cal.getTime());
		}
	}
	
	private void action_search()
	{	
		ArrayList<UData> datas;
		
		//Prepare request params
		String[] values = new String[CTL_SIZE];
		for(int i = 0; i < ctls.length; i++)
		{
			if(ctls[i] instanceof Text) values[i] = "%" + ((Text)ctls[i]).getText() + "%";
			if(ctls[i] instanceof Combo) values[i] = "%" + ((Combo)ctls[i]).getText() + "%";
		}
		
		USQLParams params = new USQLParams(); 
		
		if( ! values[SERIAL].matches("%%")) params.add(FIELD.DEVICE_serial, values[SERIAL], true);
		if( ! values[IDCLIENT].equals("%%")) params.add(FIELD.DEVICE_idclient, values[IDCLIENT], true);
		if( ! values[MODEL].equals("%%")) params.add(FIELD.DEVICE_model, values[MODEL], true);
		if( ! values[LOCATION].equals("%%")) params.add(FIELD.DEVICE_location, values[LOCATION], true);
		if( ! values[OWNER].equals("%%")) params.add(FIELD.DEVICE_owner, values[OWNER], true);
		if( ! values[COMMENT].equals("%%")) params.add(FIELD.DEVICE_comment, values[COMMENT], true);
		
		OPERATOR purchaseOperator = OPERATOR.get(operators[PURCHASE].getText());
		if(purchaseOperator != null) {
			Date date = DateUtils.extractDate((DateTime)ctls[PURCHASE]);
			OPERATOR lastOperator = purchaseOperator;
			
			if(purchaseOperator == OPERATOR.BET)
			{
				lastOperator = OPERATOR.GTE;
				Date date2 = DateUtils.extractDate((DateTime)ctls[PURCHASE2]);
				params.add(FIELD.DEVICE_purchasedate, OPERATOR.LTE, date2.toString(), true);
			}
			params.add(FIELD.DEVICE_purchasedate, lastOperator, date.toString(), true);
		}
		
		OPERATOR warrantyOperator = OPERATOR.get(operators[WAEXPIRE].getText());
		if(warrantyOperator != null) {
			Date date = DateUtils.extractDate((DateTime)ctls[WAEXPIRE]);
			OPERATOR lastOperator = warrantyOperator;
			
			if(warrantyOperator == OPERATOR.BET)
			{
				lastOperator = OPERATOR.GTE;
				Date date2 = DateUtils.extractDate((DateTime)ctls[WAEXPIRE2]);
				params.add(FIELD.DEVICE_warrantyexpire, OPERATOR.LTE, date2.toString(), true);
			}
			params.add(FIELD.DEVICE_warrantyexpire, lastOperator, date.toString(), true);
		}
		
		FIELD order = FIELD.valueOf(uWin.getCorma().getOptionManager().getProperty(OPTION.table_search_sort));
		Access sens = Access.valueOf(uWin.getCorma().getOptionManager().getProperty(OPTION.table_search_sens));
		
		try {
			datas = uWin.getCorma().getDeviceManager().searchDevices(params, order, sens);
		} catch (DBException e) {
			uWin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		
		uTable.draw(datas);
		
	}
	
	private void action_export()
	{
		if(uTable.getTable().getItemCount() < 1) return; 
		FileDialog savefile = new FileDialog(uWin.getShell(), SWT.SAVE);
		savefile.setFilterExtensions(new String[] { "*.html", "*.csv"});
		savefile.setOverwrite(true);
		String filepath = savefile.open();
		if(filepath == null) return;
		
		int filterIndex = savefile.getFilterIndex();
		String ext = savefile.getFilterExtensions()[filterIndex];
		
		try {
			ArrayList<UData> newDatas = uTable.generateDataFromTable();
			if(ext.equals("*.html")) uWin.getCorma().getExportManager().exportHtml(filepath, newDatas);
			if(ext.equals("*.csv")) uWin.getCorma().getExportManager().exportCsv(filepath, newDatas, uness.core.Export.EXPORT.CSV_HEADER_DISPLAYNAME);
		} catch (IOException e) {
			e.printStackTrace();
			uWin.showError(e.getClass().getName(), e.getMessage());
		}
		
		
	}
	
	public Control getFirstFocusControl()
	{
		return ctls[SERIAL];
	}
}
