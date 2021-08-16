package unessdb.usql;

import unessdb.FIELD;
import unessdb.UData;

abstract class USQLRequestFilter extends USQLRequestBuilder {
	
	private final int verboseLevel = 0;
	protected String filter;
	protected USQLParams params;
	
	public USQLRequestFilter(final FIELD[] p_fieldArray, final boolean p_followLink, final boolean p_aliasOn)
	{
		super(p_fieldArray, p_followLink, p_aliasOn);
	}

	public void setFilter(final USQLParams p_params)
	{
		params = p_params;
		String buildNotStrict = "";
		String buildStrict = "";
		String build = "";
		
		for(USQLParam p : params.getList())
		{
			FIELD f = p.getField();
			String alias;
			String fieldAccessName;
			String predicate;
			
			if(fieldsMap.containsKey(f))
			{
				USQLRelation uo = fieldsMap.get(f);
				alias = uo.getSourceTableAlias();
				fieldAccessName = uo.getSourceField().getAccessName();
			} else
			{
				if(tablesMap.containsKey(f.getTable()))
					alias = tablesMap.get(f.getTable());
				else
					alias = f.getTable().getAccessName();
			
				fieldAccessName = f.getAccessName();
			}
			
			predicate = alias + "." + fieldStartSyntax + fieldAccessName + fieldEndSyntax + " " + p.getOperator().get() + " ?";
			
			if(p.isStrict()) {
				if( ! buildStrict.isEmpty()) buildStrict += " AND ";
				buildStrict += predicate;
			} else {
				if( ! buildNotStrict.isEmpty()) buildNotStrict += " OR ";
				buildNotStrict += predicate;
			}
		}
		
		if( ! buildNotStrict.isEmpty()) {
			if(build.isEmpty()) build += " WHERE ";
			buildNotStrict = "(" + buildNotStrict + ")";
			build += buildNotStrict;
		}
		if( ! buildStrict.isEmpty())
		{
			if(build.isEmpty()) build += " WHERE ";
			buildStrict = "(" + buildStrict + ")";
			if( ! buildNotStrict.isEmpty()) build += " AND ";
			build += buildStrict;
		}
		if(verboseLevel >= 1) System.out.println(this.getClass().getName() + " -> setFilter(...) -> builded where: " + build);
		filter = build;
		request += build;
	}
	
	public String toString()
	{
		String out = super.toString();
		out += "\nFilter: " + filter + "\n";
		
		return out;
	}
}
