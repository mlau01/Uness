package uness.core.print;

import unessdb.FIELD;
import unessdb.UData;

public class LogBean {

	private String LOG_date;
	private String LOG_comment;
	private String LOG_author;
	
	public LogBean(String p_LOG_date, String p_LOG_type, String p_LOG_note, String p_LOG_user)
	{

		LOG_date = p_LOG_date;
		LOG_comment = p_LOG_note;
		LOG_author = p_LOG_user;
		
	}
	
	public LogBean(final UData p_uData)
	{
		LOG_date = String.valueOf(p_uData.getValue(FIELD.LOG_datetime));
		LOG_comment = String.valueOf(p_uData.getValue(FIELD.LOG_comment));
		LOG_author = String.valueOf(p_uData.getValue(FIELD.LOG_author));
	}

	public final LogBean getMe()
	{
		return this;
	}
	
	public final String getLOG_date()
	{
		//Date Format (delete all after YYYY-MM-dd)
		if (LOG_date.length() > 10) {
			LOG_date = LOG_date.substring(0, 10);
		}
	
		return LOG_date;
	}
	
	public final String getLOG_comment()
	{
		return LOG_comment;
	}
	public final String getLOG_author()
	{
		return LOG_author;
	}

}
