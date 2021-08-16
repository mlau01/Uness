package uness.gui.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import uness.gui.UWin;

public class View {

	private Menu view = null;

	public View(Shell shell, UWin mw) {
		view = new Menu(shell, SWT.DROP_DOWN);
	}

	public Menu getMenu() {
		return view;
	}

}
