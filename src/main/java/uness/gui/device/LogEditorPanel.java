package uness.gui.device;

import java.util.ArrayList;
import java.util.Calendar;
import java.sql.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import unessdb.DBException;
import unessdb.FIELD;
import unessdb.UData;
import uness.gui.UWin;
import uness.gui.common.AbstractPopupPanel;
import uness.gui.common.DateUtils;

public class LogEditorPanel extends AbstractPopupPanel{
	
	private final UData selectedLog;
	private final UData selectedDevice;
	private UData log;
	
	private Combo author;
	private DateTime datetime;
	private Text comment;
	
	public LogEditorPanel(final UWin p_uwin, final UData p_selectedDevice, final UData p_selectedLog)
	{
		super(p_uwin);
		selectedLog = p_selectedLog;
		selectedDevice = p_selectedDevice;
	}
	protected final String setTitle() {
		return "Log";
	}

	@Override
	protected void widgets()
	{
		GridData wdata = new GridData(GridData.FILL, GridData.BEGINNING, true, false);
		wdata.widthHint = 200;
		
		new Label(dialog, SWT.NONE).setText(FIELD.LOG_author.getDisplayName() + ":");
		author = new Combo(dialog, SWT.READ_ONLY);
		author.setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.LOG_datetime.getDisplayName() + ":");
		datetime = new DateTime(dialog, SWT.DATE | SWT.MEDIUM | SWT.DROP_DOWN);
		datetime.setLayoutData(wdata);
		
		new Label(dialog, SWT.NONE).setText(FIELD.LOG_comment.getDisplayName() + ":");
		comment = new Text(dialog, SWT.BORDER);
		comment.setLayoutData(wdata);
	}
	
	protected void loadDatas()
	{
		ArrayList<UData> userList = null;
		try {
			userList = uwin.getCorma().getUsersManager().getUserList();
		} catch (DBException e) {
			uwin.showError(e.getClass().getName(), e.getMessage());
			e.printStackTrace();
			return;
		}
		UData signedUser =  uwin.getCorma().getUsersManager().getSignedUser();
		int index = 0;
		for(UData user : userList)
		{
			String nAuthor = String.valueOf(user.getValue(FIELD.USER_fullname));
			author.add(nAuthor);
			if(user == signedUser) author.select(index);

			index++;
		}
		
		if(selectedLog != null) load_update();
	}
	
	private void load_update()
	{
		String sAuthor = String.valueOf(selectedLog.getValue(FIELD.LOG_author));
		for(int i = 0; i < author.getItemCount(); i++)
			if(author.getItems()[i].equals(sAuthor)) author.select(i);
		author.setEnabled(false);
		
		Date date = (Date)selectedLog.getValue(FIELD.LOG_datetime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		datetime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
		datetime.setMonth(calendar.get(Calendar.MONTH));
		datetime.setYear(calendar.get(Calendar.YEAR));
		datetime.setEnabled(false);
		
		comment.setText(String.valueOf(selectedLog.getValue(FIELD.LOG_comment)));
	}
	
	
	@Override
	/**
	 * return the number of row inserted, -1 if fields are not filled
	 */
	protected int actionValid(){
		UData newlog = new UData();
		String s_author = author.getText();
		Date d_date = DateUtils.extractDate(datetime);

		String s_comment = comment.getText();
		if(s_author.isEmpty() || d_date == null || s_comment.isEmpty()) return 1;
	
		newlog.add(FIELD.LOG_comment, s_comment);
		newlog.add(FIELD.LOG_author, String.valueOf(author.getText()));
		newlog.add(FIELD.LOG_datetime, d_date);
		
		if(selectedLog == null) {
			newlog.add(FIELD.LOG_device, selectedDevice.getValue(FIELD.DEVICE_id));
		}
		
		log = newlog;
		return 0;
	}
	
	public final UData getLog()
	{
		return log;
	}

}
