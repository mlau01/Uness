package uness.gui.users;

import org.eclipse.swt.SWT;

import uness.Uness;
import uness.gui.UResource;
import uness.gui.UWin;

public class UsersPanelEvents {

	private UWin uwin;
	private UsersPanel panel;

	private Runnable timer;
	private int timeLeft = Uness.userSessionLenght;
	private boolean timerRunning;

	public UsersPanelEvents(UWin p_uwin, UsersPanel p_panel) {
		uwin = p_uwin;
		panel = p_panel;
		timerCreate();
	}

	public void createEvents() {
		panel.getComboUsers().addListener(SWT.Selection, e -> {
			final int index = panel.getComboUsers().getSelectionIndex();
			final String fullname = panel.getComboUsers().getItem(index);
			
			//new UserLoginPanel(uwin, fullname).open();
			
			uwin.getCorma().getUsersManager().login(fullname);
			//panel.getCompositeUser().setBackground(uwin.getResourceManager().get_color(UResource.COLOR_RED));
			timerStart();
		});
	}

	private void timerCreate() {
		timer = new Runnable() {
			public void run() {
				if (panel.getLabelTimer().isDisposed())
					return;

				if (timeLeft <= 0)
					timerEnd();
				else {
					timeLeft--;
					timerUpdateLabel();
				}

				uwin.getDisplay().timerExec(1000, this);
			}
		};
	}

	private void timerUpdateLabel() {
		String value;
		int minute;
		int second = timeLeft;
		if (second > 59) {
			minute = second / 60;
			second = second % 60;
		} else
			minute = 0;

		String sMinute = String.valueOf(minute);
		if (minute < 10)
			sMinute = "0" + sMinute;

		String sSecond = String.valueOf(second);
		if (second < 10)
			sSecond = "0" + sSecond;

		value = sMinute + ":" + sSecond;

		panel.getLabelTimer().setText(value);
		panel.getLabelTimer().pack();
	}

	public void timerStart() {
		if (timerRunning) {
			timerStop();
		}
		timerRunning = true;
		timeLeft = Uness.userSessionLenght;
		uwin.getDisplay().timerExec(1000, timer);
	}

	public void timerStop() {
		uwin.getDisplay().timerExec(-1, timer);
		timerRunning = false;
	}

	public void timerEnd() {
		panel.getComboUsers().select(0);
		uwin.getCorma().getUsersManager().logout();
		panel.getCompositeUser().setBackground(null);

		timerStop();
	}

}
