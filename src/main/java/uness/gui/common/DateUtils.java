package uness.gui.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;

import uness.Uness;

public class DateUtils {
	
	
	public static String toString(final DateTime datetime)
	{
		String value = "";
		
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
		
		return value;
	}
	
	public static Calendar parseDate(String date) throws ParseException
	{
		java.util.Date formatedDate = new SimpleDateFormat(Uness.DATE_FORMAT).parse(date);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(formatedDate);
		
		return calendar;
	}
	
	public static java.sql.Date extractDate(final DateTime datetime)
	{	
		int year = datetime.getYear();
		int month = datetime.getMonth();
		int day = datetime.getDay();
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		
		return new java.sql.Date(cal.getTimeInMillis());
	}
	
	public static DateTime setDateTimeValue(final DateTime datetime, final java.sql.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		datetime.setDay(cal.get(Calendar.DAY_OF_MONTH));
		datetime.setMonth(cal.get(Calendar.MONTH));
		datetime.setYear(cal.get(Calendar.YEAR));
		
		return datetime;
	}
	
	public static DateTime setDateTimeValue(final DateTime datetime, final java.util.Date date)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		datetime.setDay(cal.get(Calendar.DAY_OF_MONTH));
		datetime.setMonth(cal.get(Calendar.MONTH));
		datetime.setYear(cal.get(Calendar.YEAR));
		
		return datetime;
	}
	
	public static final String incrementMonth(String date, String dateFormat, String monthInc) throws ParseException
	{
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		Date formatedDate = format.parse(date);
		Calendar cal = GregorianCalendar.getInstance();
		cal.setTime(formatedDate);
		cal.add(Calendar.MONTH, Integer.valueOf(monthInc));
		String value = format.format(cal.getTime());
		
		return value;
	}
}
