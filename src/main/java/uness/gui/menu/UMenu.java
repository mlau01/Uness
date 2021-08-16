package uness.gui.menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import uness.gui.UWin;

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Menu;

public class UMenu {

	private Menu menu = null;

	public UMenu(Shell shell, UWin mw) {
		menu = new Menu(shell, SWT.BAR);

		MenuItem itemFile = new MenuItem(menu, SWT.CASCADE);
		itemFile.setText("Fichier");
		File file = new File(shell, mw);
		itemFile.setMenu(file.getMenu());

		MenuItem itemEdit = new MenuItem(menu, SWT.CASCADE);
		itemEdit.setText("Edition");
		Edit edit = new Edit(shell, mw);
		itemEdit.setMenu(edit.getMenu());

		/*
		 * MenuItem itemView = new MenuItem(menu, SWT.CASCADE);
		 * itemView.setText(Uness.TEXT.getString(Uness.TEXT_KEYS.menu_view.
		 * toString())); View view = new View(shell, mw);
		 * itemView.setMenu(view.getMenu());
		 */

		MenuItem itemHelp = new MenuItem(menu, SWT.CASCADE);
		itemHelp.setText("Aide");
		Help help = new Help(shell, mw);
		itemHelp.setMenu(help.getMenu());

	}

	public Menu getMenu() {
		return menu;
	}

}
