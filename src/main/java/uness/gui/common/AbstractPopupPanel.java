package uness.gui.common;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;

import uness.gui.UWin;
import unessdb.UData;

abstract public class AbstractPopupPanel {

	protected UWin uwin;

	protected Shell dialog;
	private Button ok;
	private Button cancel;
	private int retVal = 1;

	/**
	 * Create a popup dialog
	 * @param p_uwin
	 * @param style SWT.OK | SWT.CANCEL ou SWT.CLOSE
	 */
	public AbstractPopupPanel(final UWin p_uwin)
	{
		uwin = p_uwin;
	}

	public final int open() {

		dialog = new Shell(uwin.getShell(), SWT.TITLE | SWT.CLOSE | SWT.APPLICATION_MODAL);

		createWidgets();
		dialog.setText(setTitle());
		
		loadDatas();

		dialog.setDefaultButton(ok);

		dialog.pack();
		dialog.setLocation(UWin.getPrefferedPopupPoint(uwin.getShell(), dialog));
		dialog.open();
		
		while( ! dialog.isDisposed())
		{
			if( ! uwin.getDisplay().readAndDispatch())
			{
				uwin.getDisplay().sleep();
			}
		}
		
		return retVal;
	}
	
	private void createWidgets() {

		dialog.setLayout(new GridLayout(2, false));
		
		widgets();

		// Buttons
		Composite buttons = new Composite(dialog, SWT.NONE);
		buttons.setLayoutData(new GridData(GridData.END, GridData.CENTER, true, true, 2, 1));
		{
			buttons.setLayout(new GridLayout(2, true));

			ok = new Button(buttons, SWT.PUSH);
			ok.setText("Ok");
			ok.addListener(SWT.Selection, e -> {
				retVal = actionValid();
				if(retVal == 0) dialog.dispose();
			});
			ok.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));

			cancel = new Button(buttons, SWT.PUSH);
			cancel.setText("Annuler");
			cancel.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
			cancel.addListener(SWT.Selection, e -> dialog.dispose());


		}

	}

	abstract protected void widgets();
	abstract protected void loadDatas();
	abstract protected String setTitle();
	
	/**
	 * Action when user press button Ok
	 * @return has to return not null object for closing
	 */
	abstract protected int actionValid();

}