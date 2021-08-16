package unessdb;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import unessdb.usql.USQL.DB;
import unessdb.usql.USQLParam;
import unessdb.usql.USQLParams;

public interface DBManagerInterface {
	
	public enum Access {
		NEWLOG,
		NEWDEVICE,
		EDITLOG,
		EDITDEVICE,
		IMPORTDEVICE,
		DESC,
		ASC
	}
	

	
	// ---- Database ----
	/**
	 * Connect to the specified database
	 * @param useDb
	 * @param dbUrl
	 * @param dbName
	 * @param dbLogin
	 * @param dbPasswd
	 * @throws DBException
	 */
	abstract public void connect(final DB useDb, final String dbUrl, final String dbName, final String dbLogin, final String dbPasswd) throws DBException;
	
	abstract public boolean isConnected() throws DBException;
	
	// ---- Selection ----
	
	// ---- General selection ----
	/**
	 * 
	 * @param fields
	 * @param followLink
	 * @return
	 * @throws DBException
	 */
	abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink) throws DBException;
	
	/**
	 * Close the database connection
	 * @throws DBException
	 */
	abstract public void disconnect() throws DBException;
	
	/**
	 * 
	 * @param fields
	 * @param followLink
	 * @param filter
	 * @return
	 * @throws DBException
	 */
	//abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final SimpleEntry<FIELD, Object> filter) throws DBException;
	
	/**
	 * 
	 * @param fields
	 * @param followLink
	 * @param filter
	 * @param orderIndicator
	 * @param sort
	 * @return
	 * @throws DBException
	 */
	//abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final SimpleEntry<FIELD, Object> filter, final FIELD orderIndicator, final Access sort) throws DBException;
	
	/**
	 * 
	 * @param fields to select
	 * @param followLink
	 * @param params
	 * @return
	 * @throws DBException
	 */
	abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final USQLParams params) throws DBException;
	abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final USQLParams params, final FIELD orderIndicator, final Access sort) throws DBException;
	
	/**
	 * 
	 * @param fields to select
	 * @param followLink
	 * @param searchFields datas to search
	 * @param requiredFields only in fields
	 * @return
	 * @throws DBException
	 */
	//abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final UData searchFields, final UData requiredFields) throws DBException;
	
	/**
	 * 
	 * @param fields
	 * @param followLink
	 * @param searchFields
	 * @param requiredFields
	 * @param orderIndicator
	 * @param sort
	 * @return
	 * @throws DBException
	 */
	//abstract public ArrayList<UData> select(final FIELD[] fields, final boolean followLink, final UData searchFields, final UData requiredFields, final FIELD orderIndicator, final Access sort) throws DBException;
	
	// ---- Insertion ----
	/**
	 * 
	 * @param fields
	 * @param followLink
	 * @param datas
	 * @return The number of row being inserted
	 * @throws DBException
	 */
	abstract public int insert(final FIELD[] fields, final boolean followLink, final UData datas) throws DBException;
	
	// ---- Update ----
	/**
	 * 
	 * @param fields
	 * @param followLink
	 * @param filter
	 * @param datas
	 * @return
	 * @throws DBException
	 */
	abstract public int update(final FIELD[] fields, final boolean followLink, final USQLParams filter, final UData datas) throws DBException;
	
	// ---- Delete ---	
	/**
	 * Delete specified id
	 * @param fieldIndicator
	 * @param followLink
	 * @param filter
	 * @return 1 if a row was deleted, 0 otherwise
	 * @throws DBException
	 */
	abstract public int delete(final FIELD fieldIndicator, final boolean followLink, final USQLParams filter) throws DBException;
	
	// ---- Utils ----
	/**
	 * 
	 * @param field
	 * @param value
	 * @return
	 * @throws DBException
	 */
	//abstract public boolean exists(final FIELD field, final String value) throws DBException;
	
	/**
	 * 
	 * @param field
	 * @param followLink
	 * @return
	 * @throws DBException
	 */
	abstract public int max(final FIELD field, final boolean followLink) throws DBException;

 }
