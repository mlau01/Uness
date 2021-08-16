package unessdb.usql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.AbstractMap.SimpleEntry;

import unessdb.DBException;
import unessdb.DBManagerInterface;
import unessdb.FIELD;
import unessdb.UData;
import unessdb.usql.USQL.DB;

public class USQLManager implements DBManagerInterface{
	
	private Connection con;
	private int verboseLevel = 0;

	public void connect(final DB useDb, final String dbUrl, final String dbName, final String dbLogin, final String dbPasswd) throws DBException
	{

		con = USQLConnection.connect(useDb, dbUrl, dbName, dbLogin, dbPasswd);
	}
	
	public final boolean isConnected() throws DBException
	{
		if(con == null) return false;
		
		try {
			if(con.isClosed()) return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
		
		return true;
	}
	
	// ---- SELECT ----
	public final ArrayList<UData> select(final FIELD[] fields, final boolean followLink) throws DBException
	{
		return select(fields, followLink, new USQLParams());
	}

	public final ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final USQLParams params) throws DBException {
		final USQLRequestSelect select = new USQLRequestSelect(fields, true);
		select.setFilter(params);
		select.build();
		
		if(!isConnected())
			return new ArrayList<UData>();
		
		if(verboseLevel > 0) System.out.println(select);
		try {
			return select.execute(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	
	public final ArrayList<UData> select(final FIELD[] fields, final boolean followLink, USQLParams params, final FIELD orderIndicator, final Access sort) throws DBException {
		if(!isConnected())
			return new ArrayList<UData>();
		
		final USQLRequestSelect select = new USQLRequestSelect(fields, true);
		
		if(params == null) params = new USQLParams();
		select.setFilter(params);
		
		if(orderIndicator != null)
			select.setOrder(orderIndicator);
		
		if(sort != null)
			select.setSort(sort);
		
		select.build();
		
		if(verboseLevel > 0) System.out.println(select);
		
		try {
			return select.execute(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	
	// ---- INSERT ----
	
	public final int insert(final FIELD[] fields, final boolean followLink, final UData datas) throws DBException{
		final USQLRequestInsert insert = new USQLRequestInsert(fields, followLink);
		insert.setDatas(datas);
		try {
			insert.build();
			return insert.execute(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	
	// ---- UPDATE ----
	public final int update(final FIELD[] fields, final boolean followLink, final USQLParams filter, final UData datas) throws DBException{
		USQLRequestUpdate req;
		try {
			req = new USQLRequestUpdate(fields, false);
			req.setWriteDatas(datas);
			req.setFilter(filter);
			req.build();
			return req.execute(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	
	}
	
	// ---- DELETE ----
	public final int delete(final FIELD fieldIndicator, final boolean followLink, final USQLParams filter) throws DBException
	{
		final USQLRequestDelete req1 = new USQLRequestDelete(new FIELD[] { fieldIndicator }, false);
		req1.setFilter(filter);
		int retVal = 0;
		try {
			req1.build();
			retVal = req1.execute(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
		
		return retVal;
		
	}
	
	// ---- EXISTS ----
	/*
	public final boolean exists(final FIELD field, final String value) throws DBException{
		try {
			return USQLUtils.exists(con, field, value);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	*/
	// ---- MAX ----
	public final int max(final FIELD field, final boolean followLink) throws DBException {
		final USQLRequestMax req = new USQLRequestMax(new FIELD[] { field }, followLink);
		req.build();
		try {
			return req.execute(con);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}
	
	// ---- DISCONNECT ----
	public void disconnect() throws DBException
	{
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException(e.getMessage());
		}
	}

}
