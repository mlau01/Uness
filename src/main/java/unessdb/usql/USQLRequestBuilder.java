package unessdb.usql;

import java.util.Iterator;

import unessdb.FIELD;
import unessdb.usql.USQL.SYNTAX;

abstract class USQLRequestBuilder extends USQLRequestPrepare {

	protected final boolean aliasOn;
	protected String request;
	protected String fieldStartSyntax = "";
	protected String fieldEndSyntax = "";

	public USQLRequestBuilder(final FIELD[] p_fieldArray, final boolean p_followLink, final boolean p_aliasOn) {
		super(p_fieldArray, p_followLink);

		aliasOn = p_aliasOn;
		if(syntax == SYNTAX.TSQL)
		{
			fieldStartSyntax = "[";
			fieldEndSyntax = "]";
		}
	}

	// Build the table part of a request
	protected final String buildTablePart() {
		String tablePart = "";
		int linkNbr = linksMap.size();
		int bracketNbr = linkNbr - 1;

		for (int i = 0; i < bracketNbr; i++)
			tablePart += "(";

		if (aliasOn)
			tablePart += mainTable.getAccessName() + " " + mainTable.getAccessAlias();
		else
			tablePart += mainTable.getAccessName();

		Iterator<USQLRelation> en = linksMap.values().iterator();
		while (en.hasNext()) {
			USQLRelation so = en.next();
			tablePart += " LEFT JOIN " + so.getTargetTable().getAccessName() + " " + so.getTargetTableAlias();
			tablePart += " ON " + so.getSourceTableAlias() + "." + fieldStartSyntax + so.getSourceField().getAccessName() + fieldEndSyntax + "=";
			tablePart += so.getTargetTableAlias() + "." + fieldStartSyntax + FIELD.getIdField(so.getTargetTable()).getAccessName()+ fieldEndSyntax;

			if (bracketNbr > 0) {
				tablePart += ")";
				bracketNbr--;
			}
		}

		return tablePart;
	}

	// Build the field part of the request
	protected final String buildSelectionFieldPart() {
		String fieldPart = "";
		Iterator<USQLRelation> en = fieldsMap.values().iterator();
		while (en.hasNext()) {
			if (!fieldPart.isEmpty())
				fieldPart += ", ";

			USQLRelation so = en.next();
			if (aliasOn)
				fieldPart += so.getSourceTableAlias() + "." + fieldStartSyntax + so.getSourceField().getAccessName() + fieldEndSyntax;
			else
				fieldPart += fieldStartSyntax + so.getSourceField().getAccessName() + fieldEndSyntax;

		}

		return fieldPart;
	}

	public final String getRequest() {
		return request;
	}
	
	public String toString()
	{
		String out = super.toString();
		out += "\nTable part: " + buildTablePart();
		out += "\nField part: " + buildSelectionFieldPart() + "\n";
		return out;
	}

	public abstract void build() throws Exception;

}
