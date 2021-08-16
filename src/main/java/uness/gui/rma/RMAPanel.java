package uness.gui.rma;


import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import uness.Uness.OPTION;
import uness.core.Option;
import uness.core.Rma;
import uness.core.Rma.RMA_STATUS;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.gui.common.UTable;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.UData;
import unessdb.DBManagerInterface.Access;
import unessdb.usql.USQLParams;

public class RMAPanel {

	private UWin uwin;
	private Composite composite;
	
	private UTable table;
	private ToolItem print;
	private ToolItem del;
	private Group search;
	private Text searchText;
	private Button buttonSearch;
	private Option optionManager;
	private Listener keyEnterPressed;
	private Rma rmaManager;
	private final UResource resMan;
	
	public RMAPanel(UWin p_uwin) {
		uwin = p_uwin;
		optionManager = uwin.getCorma().getOptionManager();
		rmaManager = uwin.getCorma().getRmaManager();
		resMan = uwin.getResourceManager();
		initListeners();
	}

	public final Composite create(TabFolder tabFolder, int style) {
		composite = new Composite(tabFolder, style);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		
		search = new Group(composite, SWT.NONE);
		search.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		search.setText("Recherche");
		{
			search.setLayout(new GridLayout(2, true));
			searchText = new Text(search, SWT.BORDER);
			searchText.addListener(SWT.KeyUp, keyEnterPressed);
			searchText.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			buttonSearch = new Button(search, SWT.PUSH);
			buttonSearch.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END, GridData.CENTER, false, false));
			buttonSearch.setImage(resMan.get_image(UResource.IMG_SEARCH));
			buttonSearch.addListener(SWT.Selection, e -> action_search());
		}
		
		// ToolBar
		ToolBar toolbar = new ToolBar(composite, SWT.HORIZONTAL);
		toolbar.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		{
			print = new ToolItem(toolbar, SWT.PUSH);
			print.setText("Rapport");
			print.setImage(resMan.get_image(UResource.IMG_PRINT));
			print.addListener(SWT.Selection, e -> action_report());
			//del = new UToolItem(uwin, toolbar).createItem("Nuclear_explosion_32x32.png", "&Supprimer", true);
		}

		// Liste
		table = new UTable(uwin, TABLE.RMA.getFields(), OPTION.table_rma_show, OPTION.table_devices_sort, OPTION.table_devices_sens){
			@Override
			protected void action_clickColumn() {
				action_search();
			}
		};
		table.create(composite, SWT.MULTI | SWT.FULL_SELECTION | SWT.VIRTUAL | SWT.BORDER);
		table.getTable().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		return composite;
	}

	public Text getSearchInput()
	{
		return searchText;
	}
	
	private void initListeners()
	{
		keyEnterPressed = e -> {
			if(e.keyCode == 13 || e.keyCode == SWT.KEYPAD_CR){
				action_search();
			}
		};
	}
	
	private void action_search()
	{
		//Prepare search
			FIELD[] searchFields = new FIELD[] {FIELD.RMA_device, FIELD.RMA_rmanumber, FIELD.RMA_travelvoucher};
			String keyword = searchText.getText();

			USQLParams params = new USQLParams();
			
			for(FIELD field : searchFields)
				params.add(field, "%" + keyword + "%", false);
			
			FIELD sort = FIELD.valueOf(optionManager.getProperty(OPTION.table_rma_sort));
			Access sens = Access.valueOf(optionManager.getProperty(OPTION.table_rma_sens));

			//Send search
			ArrayList<UData> datas = null;
			try {
				datas = rmaManager.get(params, sort, sens);
			} catch (DBException e) {
				uwin.showError(e.getClass().getName(), e.getMessage());
				e.printStackTrace();
				return;
			}

			table.draw(datas);

	}
	
	/*
	private void action_delete()
	{
	
		final UData selectedItem = null;
		if(new RemovePanel(uwin).open()){
			try {
				uwin.getCorma().getRmaHandlerManager().remove(selectedItem);
			} catch (Exception ex)
			{
				uwin.showError("Rma Delete Action Error", ex.getMessage());
				return;
			}
		
		}
		
	}
	*/
	private void action_report()
	{
		//TODO
		final UData selectedItem = table.getSelection();
		if(selectedItem == null) return;
		
		RMA_STATUS status = RMA_STATUS.valueOf(String.valueOf(selectedItem.getValue(FIELD.RMA_status)));
		new RMAReport(uwin, status, rmaManager.getPrintObject(selectedItem)).open();
	}

}
