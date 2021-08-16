package uness.gui.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import uness.LANG;
import uness.Uness;
import unessdb.DBException;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.usql.USQL.FIELD_TYPE;
import uness.gui.GuiException;
import uness.gui.UWin;
import uness.interfaces.Prefillable;

/**
 * This class enclose SWT controls of types Text, Combo, DateTime and add some features like testing value, auto fill combos, format date 
 * @author Matt
 *
 */
public class UControl implements Prefillable {

	private UWin uwin;
	private FIELD field;
	private Control control;
	private CONTROL_TYPE type;
	private String[] comboValues;
	private boolean valueModified = false;
	private static final int verboseLevel = 2; 

	public enum CONTROL_TYPE {
		COMBO, DATETIME, TEXT, DIGIT
	}

	/**
	 * Create a new Control, type can be Text, Combo or DateTime related to the corresponding field, 
	 * @param p_uwin
	 * @param p_field
	 * @param parent
	 * @param style
	 */
	public UControl(final UWin p_uwin, final FIELD p_field, final Composite parent, final int style) {
		uwin = p_uwin;
		
		field = p_field;
		FIELD_TYPE accessType = field.getAccessType();
		if (field.isLinked()) {
			control = new Combo(parent, style);
			type = CONTROL_TYPE.COMBO;

		} else if (accessType == FIELD_TYPE.DATE) {
			control = new DateTime(parent, SWT.DATE | SWT.MEDIUM | SWT.DROP_DOWN);
			type = CONTROL_TYPE.DATETIME;
		} else if (accessType == FIELD_TYPE.DIGIT) {
			control = new Spinner(parent, SWT.BORDER);
			((Spinner)control).setMaximum(Spinner.LIMIT);
			type = CONTROL_TYPE.DIGIT;
		} else {
			control = new Text(parent, style);
			type = CONTROL_TYPE.TEXT;
		}
	}
	

	/**
	 * Auto filling of combo list values
	 */
	public void fillDatas() {
		//Automatic filling of combo controls
		if (type == CONTROL_TYPE.COMBO) {
			
			//Clear the combo
			((Combo)control).removeAll();
			
			FIELD link = field.getLink();
			System.out.println(link);
			ArrayList<UData> datas = null;
		
			try {
				datas = uwin.getCorma().getControlManager().loadValues(link);
			} catch (DBException e)
			{
				uwin.showError(e.getClass().getName(), e.getMessage());
				e.printStackTrace();
				return;
			}
			
			for(UData data : datas)
			{
				((Combo) control).add((String)data.getValue(link));
			}
			
			comboValues = ((Combo) control).getItems();
			
			//TODO maybe this special cases has to be handle by the original panel 
			//Pre-selected Combo users values
			if(field == FIELD.RMA_sender || field == FIELD.RMA_catcher || field == FIELD.LOG_author)
			{
				UData selectedUser = uwin.getCorma().getUsersManager().getSignedUser();
				if(selectedUser != null) {
					((Combo) control).setText((String)selectedUser.getValue(FIELD.USER_fullname));
				}
				else {
					if(verboseLevel > 0) System.out.println(this.getClass().getName() + " -> No users pre-selected");
				}
			}
				
		}
	}
	
	public void clear()
	{
		switch (type) {
			case COMBO :
				((Combo)control).removeAll();
			break;
			case TEXT:
				((Text)control).setText("");
			break;
		}
	}

	public final Control getWidget() {
		return control;
	}

	public void setText(final String text) {
		valueModified = true;
		
		switch (type) {
		case COMBO :
			((Combo) control).setText(text);
		break;

		case DATETIME :
			//Parse the date given as string to fill DateTime control 
			try {
				Date formatedDate = new SimpleDateFormat(Uness.DATE_FORMAT).parse(text);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(formatedDate);
				((DateTime) control).setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH));
			} catch (ParseException e) {
				e.printStackTrace();
				((DateTime) control).setDate(9999, 0, 1);
			}
		break;
		case TEXT:
			((Text) control).setText(text);
		break;
		case DIGIT:
			if( ! text.isEmpty())
				((Spinner)control).setSelection(Integer.valueOf(text));
		break;
		}
	}

	public String getText() {
		String value = null;
		
		switch (type) {
		case COMBO :
			value = ((Combo) control).getText();
		break;
		
		case DATETIME :
			//Return DateTime Control value as String
			value = "";
			DateTime datetime = (DateTime) control;
			int year = datetime.getYear();
			int month = datetime.getMonth();
			int day = datetime.getDay();

			value += String.valueOf(year);
			value += "-";
			if (month < 9)
				value += "0";
			value += String.valueOf(datetime.getMonth() + 1);
			value += "-";
			if (day < 10)
				value += "0";
			value += String.valueOf(datetime.getDay());
		break;
		case TEXT:
			value = ((Text) control).getText();
		break;
		case DIGIT :
			value = ((Spinner) control).getText();
		break;
		}

		return value;
	}
	
	public void selectAll()
	{
		switch (type) {
			case TEXT :
				((Text)control).selectAll();
			break;
			case COMBO :
				//((Combo)control).select(0);
			break;
			case DIGIT:
				
			break;
		}
	}
	public void setFocus()
	{
		control.setFocus();
	}

	public void setEnabled(final boolean bool) {
		switch (type) {
		case COMBO :
			((Combo) control).setEnabled(bool);
		break;
		case DATETIME :
			if ((!bool) && (!valueModified))
				((DateTime) control).setDate(9999, 0, 1);
			((DateTime) control).setEnabled(bool);
		break;
		case TEXT:
			((Text) control).setEnabled(bool);
		break;
		case DIGIT:
			((Spinner)control).setEnabled(bool);
		break;
		}
	}
	public void setLayoutData(final GridData gdata)
	{
		control.setLayoutData(gdata);
	}
	
	/**
	 * Test the value of a control
	 * @param customLabelText A custom name for the control
	 * @param exceptFields Array of fields that will not be tested
	 * @return False if something is wrong, True if everything is ok
	 */
	public final void testValue(final FIELD[] exceptFields) throws GuiException {
		if (exceptFields != null) {
			for (FIELD eField : exceptFields) {
				if (eField == field)
					return;
			}
		}

		switch(type){
			case COMBO :
				final Combo combo = (Combo)control;
				if (combo.getText().isEmpty()) throw new GuiException(LANG.ERROR_EMPTY_VALUE + field.getDisplayName());
				if (notMatchComboValue(combo.getText())) throw new GuiException(LANG.ERROR_WRONG_COMBO_VALUE + field.getDisplayName());
				
			break;
			
			case TEXT :
				final Text text = (Text)control;
				if (text.getText().isEmpty()) throw new GuiException(LANG.ERROR_EMPTY_VALUE + field.getDisplayName());
			break;
			case DIGIT:
				final Spinner spinner = (Spinner)control;
				if (spinner.getText().isEmpty()) throw new GuiException(LANG.ERROR_EMPTY_VALUE + field.getDisplayName());
			break;
		}
	}

	private final boolean notMatchComboValue(final String value) {
		for (String s : comboValues) {
			if (s.equals(value))
				return false;
		}
		return true;
	}
	
	public final void testValue() throws GuiException  
	{
		testValue(null);
	}
}
