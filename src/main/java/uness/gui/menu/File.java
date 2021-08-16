package uness.gui.menu;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import uness.gui.UWin;

public class File {

	private Menu file;

	public File(Shell shell, UWin mw) {
		file = new Menu(shell, SWT.DROP_DOWN);

		//Separator
		MenuItem sep1 = new MenuItem(file, SWT.SEPARATOR);

		//Quit
		MenuItem quit = new MenuItem(file, SWT.PUSH);
		quit.setText("Quitter");
		quit.addListener(SWT.Selection, e -> {
			shell.dispose();
		});

	}

	public Menu getMenu() {
		return file;
	}

}
