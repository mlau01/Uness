package uness.gui.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import uness.gui.UWin;
import uness.gui.options.OptionMainPanel;

public class Edit {

	private Shell shell;
	private UWin uwin;

	private Menu edit;
	private MenuItem options;

	public Edit(Shell p_shell, UWin p_uwin) {
		uwin = p_uwin;
		shell = p_shell;
		edit = new Menu(p_shell, SWT.DROP_DOWN);

		options = new MenuItem(edit, SWT.PUSH);
		options.setText("Options");

		createEvents();

	}

	private void createEvents() {
		options.addListener(SWT.Selection, e -> {
			new OptionMainPanel(uwin, uwin.getShell()).open();

		});

	}

	public Menu getMenu() {
		return edit;
	}

}
