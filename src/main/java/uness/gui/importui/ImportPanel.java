package uness.gui.importui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uness.LANG;
import uness.Uness;
import uness.Uness.OPTION;
import uness.core.Export.EXPORT;
import uness.core.Import;
import uness.core.Model;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.gui.common.UControl;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.UDataException;
import unessdb.usql.USQLParams;
import unessdb.usql.USQL.FIELD_TYPE;

public class ImportPanel {
	
	private final UWin uwin;
	private Composite cPanel;
	
	private final Button[] buttons;
	public final int BUTTON_HEADER = 0;
	public final int BUTTON_LOAD = 1;
	public final int BUTTON_EXPORT = 2;
	public final int BUTTON_COMPARE = 3;
	public final int BUTTON_WRITE = 4;
	private final int BUTTON_SIZE = 5;
	
	
	private Combo dateFormat;
	private Label importLabel, compareLabel;
	private Table importTable, compareTable;
	private ArrayList<UData> importDatas;
	private String lastFilePath;
	private TableEditor importEditor;
	private final Import manager;
	private final int verboseLevel = 5;
	private final Model modelManager;
	private Listener copyListener, selectionListener;
	private final UResource resMan;
	
	public ImportPanel(final UWin p_uwin) {
		uwin = p_uwin;
		buttons = new Button[BUTTON_SIZE];
		manager = uwin.getCorma().getImportManager();
		modelManager = uwin.getCorma().getModelManager();
		resMan = uwin.getResourceManager();
	}

	public Composite create(TabFolder tabFolder, int compositeStyle) {
		cPanel = new Composite(tabFolder, compositeStyle);
		cPanel.setLayout(new GridLayout(3, false));
		
		createListeners();
		createWidgets();
		
		return cPanel;
	}

	private void createWidgets() {
		Composite buttonContainer = new Composite(cPanel, SWT.NONE);
		{
			FillLayout layout = new FillLayout(SWT.VERTICAL);
			
			buttonContainer.setLayout(layout);
			
			buttons[BUTTON_HEADER] = new Button(buttonContainer, SWT.PUSH);
			buttons[BUTTON_HEADER].addListener(SWT.Selection, e -> new ImportHelpPanel(uwin).open());
			buttons[BUTTON_HEADER].setText("Aide");
			
			new Label(buttonContainer, SWT.HORIZONTAL | SWT.SEPARATOR);
			
			new Label(buttonContainer, SWT.NONE).setText("Format des dates du fichier :");
			dateFormat = new Combo(buttonContainer, SWT.READ_ONLY);
			dateFormat.setItems(new String[] {"dd/MM/yyyy", "dd-MM-yyyy"});
			dateFormat.select(0);
			
			new Label(buttonContainer, SWT.HORIZONTAL | SWT.SEPARATOR);
			
			buttons[BUTTON_LOAD] = new Button(buttonContainer, SWT.PUSH);
			buttons[BUTTON_LOAD].addListener(SWT.Selection, e -> action_load(null));
				
			buttons[BUTTON_LOAD].setText("Charger un fichier CSV");
			
			
			buttons[BUTTON_EXPORT] = new Button(buttonContainer, SWT.PUSH);
			buttons[BUTTON_EXPORT].addListener(SWT.Selection, e -> action_export());
			buttons[BUTTON_EXPORT].setText("Sauvegarder les modifications");
			buttons[BUTTON_EXPORT].setEnabled(false);
			
			new Label(buttonContainer, SWT.HORIZONTAL | SWT.SEPARATOR);
			
			buttons[BUTTON_COMPARE] = new Button(buttonContainer, SWT.PUSH);
			buttons[BUTTON_COMPARE].addListener(SWT.Selection, e-> action_load(lastFilePath));
			buttons[BUTTON_COMPARE].setText("Relancer la comparaison");
			buttons[BUTTON_COMPARE].setEnabled(false);
			
			
			new Label(buttonContainer, SWT.HORIZONTAL | SWT.SEPARATOR);
			
			buttons[BUTTON_WRITE] = new Button(buttonContainer, SWT.PUSH);
			buttons[BUTTON_WRITE].addListener(SWT.Selection, e -> action_write());
			buttons[BUTTON_WRITE].setText("! Injecter dans la base !");
			buttons[BUTTON_WRITE].setEnabled(false);
			
		}
		
		Composite c_importTable = new Composite(cPanel, SWT.NONE);
		c_importTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			c_importTable.setLayout(new GridLayout());
			
			importLabel = new Label(c_importTable, SWT.NONE);
			//importLabel.setText("");
			
			importTable = new Table(c_importTable, SWT.MULTI | SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION | SWT.CHECK);
			importTable.setLinesVisible(true);
			importTable.setHeaderVisible(true);
			importTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			importTable.addListener(SWT.KeyDown, copyListener);
			importTable.addListener(SWT.Selection, selectionListener);
			
			importEditor = new TableEditor(importTable);
			importEditor.horizontalAlignment = SWT.LEFT;
			importEditor.grabHorizontal = true;
			importEditor.minimumWidth = 50;
		}
		
		Composite c_compareTable = new Composite(cPanel, SWT.NONE);
		c_compareTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		{
			c_compareTable.setLayout(new GridLayout());
			
			compareLabel = new Label(c_compareTable, SWT.NONE);
			//compareLabel.setText("Base de donnée");
			
			compareTable = new Table(c_compareTable, SWT.MULTI | SWT.VIRTUAL | SWT.BORDER | SWT.FULL_SELECTION);
			compareTable.setLinesVisible(true);
			compareTable.setHeaderVisible(true);
			compareTable.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
			compareTable.addListener(SWT.Selection, selectionListener);
		}
		
	}
	
	private void action_cellClick(final Point coord) {
		if(importEditor.getEditor() != null) importEditor.getEditor().dispose();
		FIELD[] editableFields = importDatas.get(0).getFields();
		
		int[] cellPosition = getCellPosition(importTable, coord);
		if(cellPosition == null) return;
		
		TableItem item = importTable.getItem(cellPosition[0]);
		int column = cellPosition[1];
		
		FIELD field = editableFields[column];
		
		UControl control = new UControl(uwin, field,importTable, SWT.BORDER);
		control.fillDatas();
		
		
		String value = item.getText(column);
		if(value == null) value = "";
		control.setText(value);
		
		
		Listener listener = e -> {
			e.doit = false;
			if(e.type == SWT.FocusOut) e.doit = false;
			if(e.keyCode == 13 || e.keyCode == 16777296) e.doit = true;
			else if(e.keyCode == 27) e.doit = false;
			else return;
			
			if(e.doit) {
				String newValue = control.getText();
				item.setText(column, newValue);
				if(field.isLinked()) {
					if(testComboValue(field, newValue)) {
						item.setBackground(column,null);
						item.setData(true);
					}
					else {
						item.setBackground(column, resMan.get_color(UResource.COLOR_RED));
						item.setData(false);
					}
				}
				if(field == FIELD.DEVICE_serial)
				{
					
					int modelIndex = 0;
					for(FIELD f : editableFields)
					{
						if(f == FIELD.DEVICE_model) break;
						modelIndex++;
					}
					try {
						if(modelManager.testSerial(item.getText(modelIndex), newValue)){
							item.setBackground(column, null);
							item.setData(true);
						}
						else {
							item.setBackground(column, resMan.get_color(UResource.COLOR_RED));
							item.setData(false);
						}
					} catch (DBException e1) {
						uwin.showError(e1.getClass().getName(), e1.getMessage());
						e1.printStackTrace();
					}
				}
				if(field == FIELD.DEVICE_model)
				{
					int serialIndex = 0;
					for(FIELD f: editableFields)
					{
						if(f == FIELD.DEVICE_serial) break;
						serialIndex++;
					}
					try {
						if(modelManager.testSerial(newValue, item.getText(serialIndex))) {
							item.setData(true);
							item.setBackground(serialIndex, null);
						}
						else {
							item.setBackground(serialIndex, resMan.get_color(UResource.COLOR_RED));
							item.setData(false);
						}
					} catch (DBException e1) {
						uwin.showError(e1.getClass().getName(), e1.getMessage());
						e1.printStackTrace();
					}
				}
			}
			
			control.getWidget().dispose();
			importEditor.getEditor().dispose();
			importTable.deselectAll();
		};
		
		control.getWidget().addListener(SWT.FocusOut, listener);
		control.getWidget().addListener(SWT.KeyUp, listener);
		control.selectAll();
		control.setFocus();

		importEditor.setEditor(control.getWidget(), item, column);
	}

	
	private final int[] getCellPosition(final Table table, final Point point)
	{
		TableColumn[] columns = table.getColumns();
		int columnsCount = columns.length;
		int itemCount = table.getItemCount();
		for(int i = 0; i < itemCount; i++)
		{
			TableItem item = table.getItem(i);
			for(int y = 0; y < columnsCount; y++)
			{
				Rectangle rect = item.getBounds(y);
				if(rect.contains(point)) return new int[] {i, y};
			}
		}
		
		return null;
	}
	
	private void action_load(String filepath)
	{
		if(filepath == null || filepath.isEmpty()) {
			importDatas = null;
			FileDialog fdialog = new FileDialog(uwin.getShell(), SWT.OPEN);
			fdialog.setFilterExtensions(new String[] { "*.csv" });
			filepath = fdialog.open();
			if ((filepath == null) || (filepath.length() <= 0)) {
				return;
			}
		}
		
		lastFilePath = filepath;
	
		if(importDatas == null) {
			try {
				importDatas = manager.importCsv(filepath);
			} catch (IOException | UDataException ex) {
				ex.printStackTrace();
				uwin.showError(this.getClass().getName(), ex.getMessage());
				return;
			}
		}
		else
		{
			importDatas = rebuildDatas(true);
		}
		
		
		//Clear from previous use
		importTable.removeAll();
		compareTable.removeAll();

		for(TableColumn col : importTable.getColumns())
			col.dispose();
		
		for(TableColumn col : compareTable.getColumns())
			col.dispose();
		
		//Set column name
		FIELD[] fields = importDatas.get(0).getFields();
		int required_found = 0;
		for (FIELD field : fields) {
			if(field.isIn(Uness.import_device_required)) required_found++;
			 new TableColumn(importTable, SWT.CENTER).setText(field.getDisplayName());
			 new TableColumn(compareTable, SWT.CENTER).setText(field.getDisplayName());
		}
		
		//Error when mandatory column number not match
		if(required_found != Uness.import_device_required.length)
		{
			uwin.showError(this.getClass().getName(), LANG.ERROR_IMPORT_REQUIRED_FIELD.get());
			return;
		}

		for(UData localDevice : importDatas)
		{
			TableItem localRow = new TableItem(importTable, SWT.NONE);
			TableItem dbRow = new TableItem(compareTable, SWT.NONE);
			localRow.setChecked(true);
			localRow.setData(true);
			dbRow.setChecked(true);
			dbRow.setData(false);
			
			
			boolean isNew = false;
			UData dbDevice = null;
			try {
				dbDevice = uwin.getCorma().getDeviceManager().getDevice((String)localDevice.getValue(FIELD.DEVICE_serial));
			} catch (DBException e) {
				uwin.showError(e.getClass().getName(), e.getMessage());
				e.printStackTrace();
				return;
			}
			if(dbDevice == null) isNew = true;
			
			int index = 0;
			for(FIELD field : fields)
			{
				String dbValue = "";
				String localValue = String.valueOf(localDevice.getValue(field));
	
				if( ! isNew)
					dbValue = String.valueOf(dbDevice.getValue(field));
				
				dbRow.setText(index, dbValue);
		
				if(localValue == null) localValue = "";
				
				//Parse the date if present
				if( ! localValue.isEmpty() && field.getAccessType() == FIELD_TYPE.DATE)
					localValue = parseDate(localValue);
				
				localRow.setText(index, localValue);
				
				//Test if combo value is correct and set background depending of it
				if(field.isLinked()){
					if(testComboValue(field, localValue)) localRow.setBackground(index, null);
					else {
						localRow.setData(false);
						localRow.setBackground(index, resMan.get_color(UResource.COLOR_RED));
					}
				}
				
				//Test if serial is corresponding to model pattern and set background depending of it
				if(field == FIELD.DEVICE_serial)
				{
					try {
						if(modelManager.testSerial(String.valueOf(localDevice.getValue(FIELD.DEVICE_model)), localValue)) localRow.setBackground(index, null);
						else {
							localRow.setBackground(index, resMan.get_color(UResource.COLOR_RED));
							localRow.setData(false);
						}
					} catch (DBException e1) {
						uwin.showError(e1.getClass().getName(), e1.getMessage());
						e1.printStackTrace();
					}
				}
				
				if(localValue != null && ! localValue.isEmpty()) {
					if( ! localValue.equals(dbValue)) {
						dbRow.setBackground(index, resMan.get_color(UResource.COLOR_YELLOW));
						dbRow.setData(true);
					}
					else
					{
						dbRow.setBackground(index, resMan.get_color(UResource.COLOR_GREEN));
					}
				}
				
				index++;
			}
		}
		for(TableColumn col : importTable.getColumns())
			col.pack();
		for(TableColumn col : compareTable.getColumns())
			col.pack();
		
		importLabel.setText("Fichier local: " + filepath);
		importLabel.pack();
		importTable.addListener(SWT.MouseDown, e -> action_cellClick(new Point(e.x, e.y)));
		compareLabel.setText("Base de donnée: " + uwin.getCorma().getOptionManager().getProperty(OPTION.dbUrl));
		compareLabel.pack();
		buttons[BUTTON_WRITE].setEnabled(true);
		buttons[BUTTON_EXPORT].setEnabled(true);
		buttons[BUTTON_COMPARE].setEnabled(true);
	}
	
	private String parseDate(String date)
	{
		SimpleDateFormat parser = new SimpleDateFormat(dateFormat.getText());
		Date parsed = null;
		try {
			parsed = parser.parse(date);
		} catch (ParseException e1) {
			uwin.showError(e1.getClass().getName(), e1.getMessage());
			e1.printStackTrace();
			return null;
		}
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		return formater.format(parsed);
	}
	
	private void action_export()
	{
		FileDialog savefile = new FileDialog(uwin.getShell(), SWT.SAVE);
		savefile.setFilterExtensions(new String[] { "*.csv"});
		savefile.setOverwrite(true);
		String filepath = savefile.open();
		if(filepath == null) return;
		
		try {
			uwin.getCorma().getExportManager().exportCsv(filepath, rebuildDatas(true), EXPORT.CSV_HEADER_ACCESSNAME);
		} catch (IOException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
	}
	
	private final ArrayList<UData> rebuildDatas(boolean withErrors)
	{
		ArrayList<UData> newData = new ArrayList<UData>();
		FIELD[] importFields = importDatas.get(0).getFields();
		

		for(TableItem item : importTable.getItems())
		{
			if(! withErrors && ! (boolean)item.getData()) continue;
			UData tupple = new UData();
			for(int i = 0; i < importTable.getColumnCount(); i++)
			{
				tupple.add(importFields[i], item.getText(i));
			}
			newData.add(tupple);
		}
		
		return newData;
	}
	
	private void action_write()
	{
		//Test all items for entry errors
		for(TableItem item : importTable.getItems())
		{
			if( item.getChecked() && ! (boolean)item.getData())
			{
				uwin.showError(this.getClass().getName(), "Erreur trouvé dans les lignes sélectionnées");
				return;
			}
		}
		

	
		uwin.setWait(null);

		
		ArrayList<UData> newData = new ArrayList<UData>();
		FIELD[] importFields = importDatas.get(0).getFields();
		
		TableItem[] importItems = importTable.getItems();
		TableItem[] compareItems = compareTable.getItems();
		for(int i = 0; i < importItems.length; i++)
		{
			TableItem localItem = importItems[i];
			TableItem dbItem = compareItems[i];
			
			if((boolean)localItem.getData() && (boolean)dbItem.getData() && localItem.getChecked()) {
			UData tupple = new UData();
			for(int y = 0; y < importTable.getColumnCount(); y++)
			{
				tupple.add(importFields[y], localItem.getText(y));
			}
			newData.add(tupple);
			}
		}
		
		//Duplicate detector
		ArrayList<String> serials = new ArrayList<String>();
		for(UData ud : newData)  
		{
			String serial = String.valueOf(ud.getValue(FIELD.DEVICE_serial));
			if(serials.contains(serial)) {
				uwin.showError(this.getClass().getName(), LANG.IMPORT_ERROR_DUPLICATE_SERIAL.get() + serial);
				return;
			}
			else serials.add(serial);
		}
		
		int importDataSize = newData.size();
		//LOOP EACH LINES
		for(int i = 0; i < importDataSize; i++)
		{
			UData importDevice = newData.get(i);
			boolean isNew = false;
			UData dbDevice = null;
			try {
				dbDevice = uwin.getCorma().getDeviceManager().getDevice((String)importDevice.getValue(FIELD.DEVICE_serial));
			} catch (DBException e) {
				uwin.showError(e.getClass().getName(), e.getMessage());
				e.printStackTrace();
				return;
			}
			UData writeData = new UData();
			
			if(dbDevice == null) isNew = true;
			
			//Loop each column
			for(FIELD field : importDevice.getFields())
			{
				String value = String.valueOf(importDevice.getValue(field));
				if(value != null && ! value.isEmpty())
					writeData.add(field, value);
				else{
					if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> action_write() -> FIELD: " + field + " null or empty value");
				}
					
			}
			try {
				if(isNew)
					uwin.getCorma().getDeviceManager().addDevice(writeData);
				else	
					uwin.getCorma().getDeviceManager().updateDevice(writeData, new USQLParams(FIELD.DEVICE_id, dbDevice.getValue(FIELD.DEVICE_id)));
			} catch (DBException | ParseException e) {
				uwin.showError(e.getClass().getName(), e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		
		action_load(lastFilePath);
		uwin.stopWait(null);
	}
	
	private final boolean testComboValue(final FIELD field, final String value)
	{
		ArrayList<UData> fieldValues = null;
		try {
			fieldValues = uwin.getCorma().getControlManager().loadValues(field.getLink());
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
		}
		
		for(UData fd : fieldValues)
		{
			if(fd.getValue(field.getLink()).equals(value))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	private void createListeners()
	{
		copyListener = e -> {
			if(e.keyCode == 99 && e.stateMask == 262144) 
			{
				String copy = "";
				for(TableItem ti : importTable.getSelection()){
					if( ! copy.isEmpty()) copy += "\n";
					for(int i = 0; i < importTable.getColumnCount(); i++)
					{
						if(i>0) copy += ",";
						copy += ti.getText(i);
					}
				}
				StringSelection stringSelection = new StringSelection(copy);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}

		};
		
		selectionListener = e -> {
			Table table = ((Table)e.widget);
			int itemIndex = table.getSelectionIndex();
			if(itemIndex < 0) return;
			TableItem item = table.getItem(itemIndex);
			if(table == importTable)
				compareTable.setSelection(itemIndex);
			else if(table == compareTable)
				importTable.setSelection(itemIndex);
		};
	}

	

}
