package uness.gui.rma;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import uness.Uness;
import uness.core.Report;
import uness.core.Rma.RMA_STATUS;
import uness.gui.UWin;
import uness.gui.common.AbstractPopupPanel;

public class RMAReport extends AbstractPopupPanel{
	private Button print;
	private Button savePdf;
	private HashMap<String, Object> printObject;
	private RMA_STATUS status;
	
	public RMAReport(UWin p_uwin, RMA_STATUS p_status, HashMap<String, Object> p_printObject) {
		super(p_uwin);
		printObject = p_printObject;
		status = p_status;
	}


	@Override
	protected void widgets() {
		
		Label label1 = new Label(dialog, SWT.CENTER);
		Font font = new Font(dialog.getDisplay(), "Arial", 24, SWT.BOLD);
		label1.setFont(font);
		label1.setText("RMA Enregistré");
		label1.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		font.dispose();

		print = new Button(dialog, SWT.PUSH);
		print.setText("Imprimer");
		print.addListener(SWT.Selection, e -> action_print());
		print.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		
		savePdf = new Button(dialog, SWT.PUSH);
		savePdf.setText("Enregistrer en PDF");
		savePdf.addListener(SWT.Selection, e -> action_saveAsPdf());
		savePdf.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));
		
	}
	private void action_print() {
		uwin.setWait(dialog);
		String template = "";
		if (status == RMA_STATUS.NEW)
			template = Uness.sendRmaTemplate;
		else if (status == RMA_STATUS.RETURN)
			template = Uness.returnRmaTemplate;

		try {
			final InputStream templateStream = this.getClass().getResourceAsStream(template);
			Collection<Map<String, ?>> arrayData = new ArrayList<Map<String,?>>();
			HashMap<String, Object> mapData = new HashMap<String,Object>();
			mapData.put("void", "void");
			arrayData.add(mapData);
			JRMapCollectionDataSource jrData = new JRMapCollectionDataSource(arrayData);
			Report.print(templateStream, printObject, jrData);
		} catch (JRException e1) {
			uwin.showError("Print error", e1.getMessage());
			e1.printStackTrace();
		}

		uwin.stopWait(dialog);
		dialog.dispose();
	}
	
	private void action_saveAsPdf()
	{
		uwin.setWait(dialog);
		String template = "";
		if (status == RMA_STATUS.NEW)
			template = Uness.sendRmaTemplate;
		else if (status == RMA_STATUS.RETURN)
			template = Uness.returnRmaTemplate;

		try {
			final InputStream templateStream = this.getClass().getResourceAsStream(template);

			Collection<Map<String, ?>> arrayData = new ArrayList<Map<String,?>>();
			arrayData.add(new HashMap<String,Object>());
			
			JRMapCollectionDataSource jrData = new JRMapCollectionDataSource(arrayData);
			
			FileDialog savefile = new FileDialog(uwin.getShell(), SWT.SAVE);
			savefile.setFilterExtensions(new String[] { "*.pdf"});
			savefile.setOverwrite(true);
			String filepath = savefile.open();
			if(filepath == null) return;
			Report.saveAsPdf(templateStream, printObject, jrData, filepath);
		} catch (JRException | IOException e1) {
			uwin.showError("Print error", e1.getMessage());
			e1.printStackTrace();
		}

		uwin.stopWait(dialog);
		dialog.dispose();
	}
	
	@Override
	protected String setTitle() {
		return "Rapport";
	}
	
	@Override
	protected int actionValid() {
		return 0;
	}

	@Override
	protected void loadDatas() {
		
	}
}
