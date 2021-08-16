package uness.gui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import uness.gui.UWin;

public class RemovePanel {

	private final UWin uwin;
	private final Shell parent;
	private Shell shell;
	
	private Text text;
	private Button ok;
	private Button cancel;
	
	private boolean retVal = false;

	public RemovePanel(final UWin p_uwin) {
		uwin = p_uwin;
		parent = uwin.getShell();
	}

	public final boolean open() {
		shell = new Shell(parent, SWT.TITLE | SWT.CLOSE | SWT.APPLICATION_MODAL);
		shell.setText("Confirmation");
		shell.setLayout(new GridLayout(2, true));

		createWidgets();
		createEvents();

		shell.pack();
		shell.setLocation(UWin.getPrefferedPopupPoint(parent, shell));
		shell.open();
		
		Display dp = uwin.getDisplay();
		while( ! shell.isDisposed())
		{
			if( ! dp.readAndDispatch())
			{
				dp.sleep();
			}
		}
	
		return retVal;
	}
	
	private void createWidgets()
	{
		Label lbl = new Label(shell, SWT.NONE);
		lbl.setText("Taper DEL");
		lbl.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

		text = new Text(shell, SWT.SHADOW_IN);
		text.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 2, 1));

		ok = new Button(shell, SWT.PUSH);
		ok.setText("OK");
		ok.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		ok.setEnabled(false);

		cancel = new Button(shell, SWT.PUSH);
		cancel.setText("Cancel");
		cancel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
	}
	
	private void createEvents()
	{
		//Text modification
		text.addListener(SWT.Modify, e -> {
			if (text.getText().toLowerCase().equals("del"))
				ok.setEnabled(true);
			else
				ok.setEnabled(false);
		});

		//Ok pressed
		ok.addListener(SWT.Selection, e -> {
			actionValid();
		});
		
		//Key enter pressed
		text.addListener(SWT.KeyUp, e -> {
			if (e.keyCode == 13 || e.keyCode == 16777296)
				actionValid();
		});
	
		//Cancel pressed
		cancel.addListener(SWT.Selection, e -> {
			retVal = false;
			shell.dispose();
		});
	}
	
	private void actionValid()
	{
		if (text.getText().toLowerCase().equals("del")) {
			retVal = true;
			shell.dispose();
		}
	}

}
