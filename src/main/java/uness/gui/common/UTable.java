package uness.gui.common;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import uness.Uness.OPTION;
import uness.core.Option;
import uness.gui.UWin;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;

public class UTable {

	private Composite parent;
	private Table table;
	private final UWin uwin;
	private Listener menuItemClickListener, columnClickListener, columnMoveListener, menuDetectListener, tableItemClickListener, copyListener;
	private final int verboseLevel = 2;
	private final OPTION showOption, sortOption, sensOption;
	private final FIELD[] showableColumn;
	private int columnStyle;
	private Menu headerMenu;
	private Hashtable<TableItem, UData> tableData;
	private final Option optionManager;

	public UTable(final UWin p_uwin, FIELD[] p_showableColumn, OPTION p_showOption, OPTION p_sortOption, OPTION p_sensOption) {
		uwin = p_uwin;
		showOption = p_showOption;
		showableColumn = p_showableColumn;
		tableData = new Hashtable<TableItem, UData>();
		optionManager = uwin.getCorma().getOptionManager();
		sortOption = p_sortOption;
		sensOption = p_sensOption;
	}
	
	protected void action_clickColumn(){
		
	}
	protected void action_clickTableItem(UData tableItem){
		
	}
	protected Menu menuContent(){
		return null;
	}
	
	public final Table create(final Composite p_parent, final int style){
		parent = p_parent;
		ArrayList<FIELD> columns = FIELD.toFieldArray(uwin.getCorma().getOptionManager().getProperty(showOption));

		table = new Table(parent, style);
		table.setHeaderVisible(true);
		makeListeners();
		headerMenu = new Menu(table);
		for(FIELD f : showableColumn)
		{
			MenuItem mi = new MenuItem(headerMenu, SWT.CHECK);
			mi.setText(f.getDisplayName());
			if(f.isIn(columns)) mi.setSelection(true);
			mi.addListener(SWT.Selection, menuItemClickListener);
		}
		
		table.addListener(SWT.MenuDetect, menuDetectListener);
		table.addListener(SWT.Selection, tableItemClickListener);
		table.addListener(SWT.KeyDown, copyListener);
		
		return table;
	}
	
	public void draw(Collection<UData> datas)
	{
		ArrayList<FIELD> columns = FIELD.toFieldArray(uwin.getCorma().getOptionManager().getProperty(showOption));
		for(TableColumn col : table.getColumns())
			col.removeListener(SWT.Move, columnMoveListener);
		
		for(TableColumn col : table.getColumns())
			col.dispose();
		
		table.removeAll();
		tableData.clear();
		
		for(FIELD f : columns)
		{
			if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> draw(...): Drawing column: " + f.getDisplayName());
			TableColumn col = new TableColumn(table, columnStyle);
			col.setText(f.getDisplayName());
		}
		
		for(UData ud : datas)
		{
			TableItem ti = new TableItem(table, SWT.NONE);
			tableData.put(ti, ud);
			int index = 0;
			for(FIELD f : columns)
			{
				ti.setText(index, String.valueOf(ud.getValue(f)));
				index++;
			}
		}
		
		for(TableColumn col : table.getColumns())
		{
			col.pack();
			col.addListener(SWT.Selection, columnClickListener);
			col.addListener(SWT.Move, columnMoveListener);
			col.setMoveable(true);
		}
	}
	
	public void setColumnStyle(int style)
	{
		columnStyle = style;
	}
	
	public final ArrayList<UData> generateDataFromTable()
	{
		ArrayList<UData> newDatas = new ArrayList<UData>();
		
		TableColumn[] cols = table.getColumns();
		int[] columnOrder = table.getColumnOrder();
		TableItem[] items = table.getItems();
		
		for(TableItem item : items)
		{
			UData line = new UData();
			for(int index : columnOrder)
			{
				FIELD f = FIELD.getFieldByDisplayName(showableColumn, cols[index].getText());
				String value = item.getText(index);
				line.add(f, value);
			}
			newDatas.add(line);
		}
		
		
		return newDatas;
	}
	
	private void makeListeners()
	{
		menuItemClickListener = e -> {
			if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> ColumnMenuItem Triggered");
			MenuItem emi = (MenuItem)e.widget;
			int checkedItemCount = 0;
			for(MenuItem mi : headerMenu.getItems()){
				if(mi.getSelection()) checkedItemCount++;
			}
			if(checkedItemCount == 0){
				emi.setSelection(true);
				return;
			}
			
			FIELD f = FIELD.getFieldByDisplayName(showableColumn, emi.getText());
			Option o = uwin.getCorma().getOptionManager();
			o.columnToShowSwitch(showOption, f);
			
			Collection<UData> newDrawData = new ArrayList<UData>(tableData.values());
			draw(newDrawData);
			
		};	

		columnClickListener = e -> {
			if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> columnClickListener Triggered");
			TableColumn ecol = (TableColumn)e.widget;
			FIELD cf = FIELD.getFieldByDisplayName(showableColumn,ecol.getText());
			if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> columnClickListener : Setting sort field to:" + cf.toString());
			optionManager.setProperty(sortOption, cf.toString());
			if(optionManager.getProperty(sensOption).equals("ASC")) {
				if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> columnClickListener : Setting sens to: DESC");
				optionManager.setProperty(sensOption, "DESC");
			}
			else if(optionManager.getProperty(sensOption).equals("DESC")) {
				if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> columnClickListener : Setting sens to: ASC");
				optionManager.setProperty(sensOption, "ASC");
			} else {
				System.out.println(this.getClass().getName() + " -> columnClickListener : Wrong sens value: " + optionManager.getProperty(sensOption));
			}

			action_clickColumn();
		};
		
		columnMoveListener = e -> {
			if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> columnMoveListener Triggered");
			int[] newOrder = table.getColumnOrder();
			TableColumn[] tcols = table.getColumns();
			String newOrderString = "";
			for(int i : newOrder){
				if( ! newOrderString.isEmpty()) newOrderString +=",";
				FIELD field = FIELD.getFieldByDisplayName(showableColumn, tcols[i].getText());
				newOrderString += field;
			}
			
			uwin.getCorma().getOptionManager().changeColumnOrder(showOption, newOrderString);
		
		};
		
		menuDetectListener = e ->
	    {			
	        Point pt = Display.getCurrent().map(null, table, new Point(e.x, e.y));
	        Rectangle clientArea = table.getClientArea();
	        boolean headerClicked = (clientArea.y <= pt.y && pt.y < (clientArea.y + table.getHeaderHeight()));

	        if (headerClicked)
	        {
	            int xOffset = 0;
	            for (int colIdx : table.getColumnOrder())
	            {
	                int colWidth = table.getColumn(colIdx).getWidth();

	                if (xOffset <= pt.x && pt.x < (xOffset + colWidth))
	                {
	                    table.setMenu(headerMenu);
	                    break;
	                }
	                xOffset += colWidth;
	            }
	        }
	        else
	        {
	        	table.setMenu(menuContent());
	        }
	        
	        
	    };
	    
	    tableItemClickListener = e -> {
			TableItem[] selection = table.getSelection();
			if(selection.length < 1) return;
			TableItem selectedItem = selection[0];
			UData selectedDevice = tableData.get(selectedItem);
			action_clickTableItem(selectedDevice);
	    };
	    
		copyListener = e -> {
			if(e.keyCode == 99 && e.stateMask == 262144) 
			{
				String copy = "";
				for(TableItem ti : table.getSelection()){
					if( ! copy.isEmpty()) copy += "\n";
					for(int i = 0; i < table.getColumnCount(); i++)
					{
						if(i>0) copy += "\t";
						copy += ti.getText(i);
					}
				}
				StringSelection stringSelection = new StringSelection(copy);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}

		};
		
	}
	
	public final Table getTable()
	{
		return table;
	}
	
	/**
	 * 
	 * @return The current selection or null if nothing selected
	 */
	public final UData getSelection()
	{
		TableItem[] selection = table.getSelection();
		if(selection.length < 1) return null;
		TableItem selectedItem = selection[0];
		
		return tableData.get(selectedItem);
	}
	
	public final Collection<UData> getDatas()
	{
		return tableData.values();
	}
	
	/**
	 * Replace the current selection
	 * @param modifiedItem new data
	 */
	public void replace(UData modifiedItem)
	{
		TableItem[] selection = table.getSelection();
		TableItem selectedItem = selection[0];
		
		tableData.replace(selectedItem, modifiedItem);
		int index = 0;
		for(TableColumn col : table.getColumns())
		{
			FIELD field = FIELD.getFieldByDisplayName(showableColumn, col.getText());
			String value = String.valueOf(modifiedItem.getValue(field));
			if(value.equals("null")) value = "";
			selectedItem.setText(index, value);
			index++;
		}
	}
	
	/**
	 * Replace the target in the internal array and the displayed data
	 * @param target
	 * @param newData
	 */
	public void replace(UData target, UData newData)
	{
		TableItem targetItem = null;

		for(TableItem item : tableData.keySet())
		{
			if(tableData.get(item) == target){
				targetItem = item;
				break;
			}
		}
		if(targetItem == null) return;
		
		tableData.replace(targetItem, newData);
		int index = 0;
		for(TableColumn col : table.getColumns()) {
			FIELD f = FIELD.getFieldByDisplayName(TABLE.DEVICE, col.getText());
			targetItem.setText(index, String.valueOf(newData.getValue(f)));
			index++;
		}
		
		
	}
	public void removeSelection()
	{
		TableItem[] selection = table.getSelection();
		TableItem selectedItem = selection[0];
		if(selectedItem == null) return;
		
		tableData.remove(selectedItem);
		selectedItem.dispose();
	}
	
}