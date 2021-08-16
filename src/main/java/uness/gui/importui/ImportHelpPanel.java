package uness.gui.importui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import uness.LANG;
import uness.Uness;
import uness.gui.UResource;
import uness.gui.UWin;
import uness.gui.common.AbstractPopupPanel;
import unessdb.FIELD;
import unessdb.TABLE;

public class ImportHelpPanel extends AbstractPopupPanel {
	private Text requiredField;
	private Text headers;
	private final UResource resMan;
	
	public ImportHelpPanel(UWin p_uwin) {
		super(p_uwin);
		resMan = uwin.getResourceManager();
	}

	@Override
	protected void widgets() {
		Composite panel = new Composite(this.dialog, SWT.NONE);
		panel.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 2, 1));
		{
			panel.setLayout(new GridLayout(2, false));
			
			Label l1 = new Label(panel, SWT.NONE);
			l1.setText("Encodage: UTF-8");
			l1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			
			new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			
			Label lc3 = new Label(panel, SWT.BORDER);
			lc3.setBackground(resMan.get_color(UResource.COLOR_RED));
			lc3.setText("                     ");
			new Label(panel, SWT.NONE).setText("Valeur ou pattern invalide");
			
			Label lc2 = new Label(panel, SWT.BORDER);
			lc2.setBackground(resMan.get_color(UResource.COLOR_YELLOW));
			lc2.setText("                     ");
			new Label(panel, SWT.NONE).setText("Résultat de comparaison de champ différent");
			
			Label lc4 = new Label(panel, SWT.BORDER);
			lc4.setBackground(resMan.get_color(UResource.COLOR_GREEN));
			lc4.setText("                     ");
			new Label(panel, SWT.NONE).setText("Résultat de comparaison de champ identique");
			
			new Label(panel, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			
			Label lh1 = new Label(panel, SWT.NONE);
			lh1.setText(LANG.IMPORT_HEADERS_L1.get());
			lh1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			
			requiredField = new Text(panel, SWT.BORDER | SWT.READ_ONLY);
			requiredField.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			
			Label l2 = new Label(panel, SWT.NONE);
			l2.setText(LANG.IMPORT_HEADERS_L2.get());
			l2.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
			
			headers = new Text(panel, SWT.BORDER | SWT.READ_ONLY);
			headers.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		}
		
	}

	@Override
	protected void loadDatas() {
		
		String buildHeaders = "";
		String buildRequired = "";
		
		for(FIELD f : Uness.import_device_required)
		{
			if( ! buildRequired.isEmpty()) buildRequired += ",";
			buildRequired += f.getAccessName();
		}
		for(FIELD f: TABLE.DEVICE.getFields())
		{
			if( ! buildHeaders.isEmpty()) buildHeaders += ",";
			buildHeaders += f.getAccessName();
		}
		final String headersString = buildHeaders;
		final String requiredString = buildRequired;
		
		requiredField.setText(requiredString);
		headers.setText(headersString);
		
	}

	@Override
	protected String setTitle() {
		return "Aide";
	}

	@Override
	protected int actionValid() {
		return 0;
	}

}
