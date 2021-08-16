package uness.gui.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import uness.gui.UWin;

public class Help {

	private Menu help = null;

	public Help(Shell shell, UWin mw) {
		help = new Menu(shell, SWT.DROP_DOWN);

		MenuItem console = new MenuItem(help, SWT.PUSH);
		console.setText("Aide");

	}

	public Menu getMenu() {
		return help;
	}

}
