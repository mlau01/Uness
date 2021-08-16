package unessdb.usql;

import java.util.Hashtable;
import java.util.LinkedHashMap;

import unessdb.FIELD;
import unessdb.TABLE;
import unessdb.usql.USQL.ACTION;
import unessdb.usql.USQL.SYNTAX;

abstract class USQLRequestPrepare {
	
	protected final SYNTAX syntax = SYNTAX.TSQL;
	protected FIELD[] fieldArray;
	protected final boolean followLink;
	
	protected LinkedHashMap<FIELD, USQLRelation> fieldsMap;
	protected LinkedHashMap<FIELD, USQLRelation> linksMap;
	protected LinkedHashMap<TABLE, String> tablesMap;
	
	protected ACTION action;
	protected final TABLE mainTable;
	
	private final short verboseLevel = 0;
	
	public USQLRequestPrepare(final FIELD[] p_fieldArray, final boolean p_followLink)
	{
		fieldArray = p_fieldArray;
		followLink = p_followLink;
		
		fieldsMap = new LinkedHashMap<FIELD, USQLRelation>();
		linksMap = new LinkedHashMap<FIELD, USQLRelation>();
		tablesMap = new LinkedHashMap<TABLE, String>();
		
		mainTable = fieldArray[0].getTable();
		
		buildSqlMap();
	}
	
	private void buildSqlMap()
	{
		

		Hashtable<TABLE, Integer> linkTables = new Hashtable<TABLE, Integer>();

		for (FIELD field : fieldArray) {
			TABLE table = field.getTable();
			String tableAlias = table.getAccessAlias();
			FIELD srcField = field;

			// If follow link is true and field is a link
			if (followLink && field.isLinked()) {
				if(verboseLevel > 1) System.out.println("USQLRequestPrepare > Field (" + field + ") is linked, attempt to get the link value...");
				field = field.getLink();

				table = field.getTable();

				// Set the table alias
				if (linkTables.containsKey(table)) {
					int value = linkTables.get(table).intValue();
					value++;
					linkTables.put(table, value);
				} else {
					linkTables.put(table, 0);
				}

				tableAlias = table.getAccessAlias() + linkTables.get(table).intValue();

				linksMap.put(srcField, new USQLRelation(srcField, srcField.getTable().getAccessAlias(), field, tableAlias));
			}

			if (!tablesMap.containsKey(table))
				tablesMap.put(table, tableAlias);
			fieldsMap.put(srcField, new USQLRelation(field, tableAlias));

		}
	}
	

	public String toString()
	{
		String str = "";
		str += "Fields: ";
		for(FIELD f : fieldArray)
			str += (f.toString() + ", ");
		
		str += "\n\n";
		str += "Fields Map: \n";
		for(FIELD f : fieldsMap.keySet())
			str += (f.toString() + ": " + fieldsMap.get(f) + "\n");
		
		str += "\n";
		str += "Tables Map: \n";
		for(TABLE t : tablesMap.keySet())
			str += t.toString() + ": " + tablesMap.get(t) + "\n";
		
		str += "\n";
		str += "Links Map: \n";
		for(FIELD f : linksMap.keySet())
			str += f.toString() + ": " + linksMap.get(f) + "\n";
		
		return str;
	}

	
	public final FIELD[] getFieldArray()
	{
		return fieldArray;
	}

}
