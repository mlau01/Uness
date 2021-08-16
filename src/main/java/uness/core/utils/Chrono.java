package uness.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Chrono {
	Calendar m_start = new GregorianCalendar();
	Calendar m_stop = new GregorianCalendar();
	
	public Chrono() {
		
	}
	
	public void start() {
		m_start.setTime(new Date());
	}
	
	public void stop() {
		m_stop.setTime(new Date());
	}
	
	public long getMilliSec() {
		return (m_stop.getTimeInMillis() - m_start.getTimeInMillis());
	}
}

