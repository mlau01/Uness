package uness.gui.users;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import uness.gui.UWin;
import uness.interfaces.Prefillable;
import unessdb.DBException;

public class UsersPanel implements Prefillable {

	private UWin uwin;
	private UsersPanelEvents events;

	private Composite gusers;

	private Label timerLabel;
	private Combo users;

	public UsersPanel(UWin p_uwin) {
		uwin = p_uwin;
		events = new UsersPanelEvents(uwin, this);
	}

	public final Composite createWidgets(Composite parent) {
		gusers = new Composite(parent, SWT.NONE);
		{
			GridLayout layout = new GridLayout(2, false);
			layout.marginHeight = layout.marginWidth = 0;
			gusers.setLayout(layout);
			
			timerLabel = new Label(gusers, SWT.NONE);
			timerLabel.setText("00:00");

			users = new Combo(gusers, SWT.DROP_DOWN | SWT.READ_ONLY);
			users.setToolTipText("TEST");
			users.setLayoutData(new GridData(200, 30));
		}

		events.createEvents();

		return gusers;
	}

	public void update(Object o) {
		double timeLeft = (double) o;
		if (timeLeft <= 0) {
			events.timerEnd();
		}
	}

	public void fillDatas() {
		String[] names;
		try {
			names = uwin.getCorma().getUsersManager().getFullnameList();
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		users.setItems(names);
	}

	public final Composite getGroup() {
		return gusers;
	}

	public final Combo getComboUsers() {
		return users;
	}

	public final Label getLabelTimer() {
		return timerLabel;
	}

	public final Composite getCompositeUser() {
		return gusers;
	}

}
