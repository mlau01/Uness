package unessdb.usql;

import unessdb.FIELD;
import unessdb.TABLE;

final class USQLRelation {

	enum TYPE {
		TABLE, FIELD, LINK
	}

	private FIELD srcField;
	private FIELD tgtField;
	private String srcTableAlias;
	private String tgtTableAlias;
	private TABLE tgtTable;
	private TYPE type;

	public USQLRelation(FIELD p_srcField, String p_srcTableAlias) {
		srcField = p_srcField;
		srcTableAlias = p_srcTableAlias;
		type = TYPE.FIELD;

	}

	public USQLRelation(FIELD p_srcField, String p_srcTableAlias, FIELD p_tgtField, String p_tgtTableAlias) {
		type = TYPE.LINK;
		srcField = p_srcField;
		srcTableAlias = p_srcTableAlias;
		tgtField = p_tgtField;
		tgtTableAlias = p_tgtTableAlias;
		tgtTable = tgtField.getTable();
	}

	public FIELD getSourceField() {
		return srcField;
	}

	public String getSourceTableAlias() {
		return srcTableAlias;
	}

	public String getTargetTableAlias() {
		return tgtTableAlias;
	}

	public TABLE getTargetTable() {
		return tgtTable;
	}

	public TYPE getType() {
		return type;
	}

	public FIELD getTargetField() {
		return tgtField;
	}
	
	public String toString()
	{
		String out = srcField.getTable() + "." + srcField + " => " + tgtTable + "." + tgtField;
		
		return out;
	}
}
