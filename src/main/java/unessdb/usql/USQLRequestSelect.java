package unessdb.usql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import unessdb.FIELD;
import unessdb.UData;
import unessdb.DBManagerInterface.Access;

final class USQLRequestSelect extends USQLRequestFilter {

	protected Access sort;
	protected String order;
	protected String limit;
	private short verboseLevel = 0;

	public USQLRequestSelect(final FIELD[] p_fieldArray, final boolean p_followLink) {
		super(p_fieldArray, p_followLink, true);
	}

	public void build() {
		request = "SELECT ";
		request += buildSelectionFieldPart();
		request += " FROM ";
		request += buildTablePart();

		if (filter != null) {
			request += filter;
		}
		if (order != null) {
			request += order;
		}
		if (sort != null)
			request += " " + sort.toString();

		if (limit != null)
			request += limit;
	}

	public final ArrayList<UData> execute(final Connection con) throws SQLException {
		if (params == null) return null;
		if (verboseLevel >= 1) System.out.println(this.getClass().getName() + " > Preparing statement: " + request);
		
		PreparedStatement ps = con.prepareStatement(request);
		
		if (verboseLevel >= 1) System.out.println(this.getClass().getName() + " > Statement Data: (" + params + ")");
		int index = 1;
		for (USQLParam p  : params.getList()) {
			String value = String.valueOf(p.getValue());
			switch (p.getField().getAccessType()) {
				case TEXT:
					ps.setString(index, value);
					break;
				case DIGIT:
					try {
						ps.setInt(index, Integer.valueOf(value));
					} catch (NumberFormatException e) {
						ps.setString(index, value);
					}
				break;
				case DATE:
					ps.setDate(index, java.sql.Date.valueOf(value));
				break;
			}
			
			index++;
		}
		
		ResultSet res = ps.executeQuery();
		ArrayList<UData> uRes = new ArrayList<UData>();
		while (res.next()) {
			UData ur = new UData();
			index = 1;
			for(FIELD field : fieldArray)
			{
				if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> Getting field: " + field);
				Object value = null;
				switch(field.getAccessType())
				{
					case TEXT :
						value = res.getString(index);
					break;
					
					case DIGIT :
						if(field.isLinked()) value = res.getString(index);
						else value = res.getInt(index);
					break;
					case DATE :
						value = res.getDate(index);
					break;
				}
				
				if(value == null) value = "";
				ur.put(field, value);
				index++;
			}

			if(verboseLevel >= 2) System.out.println(this.getClass().getName() + " -> Retrieving data: " + ur);
			uRes.add(ur);
		}


		return uRes;
	}
	
	public void setSort(final Access p_sort) {
		sort = p_sort;
	}
	
	/**
	 * Set the field whose the server ordered the results
	 * @param field Field
	 */
	public void setOrder(FIELD field) {
		String alias;
		if(field.isLinked()) field = field.getLink();

		if (tablesMap.containsKey(field.getTable()))
			alias = tablesMap.get(field.getTable());
		else
			alias = field.getTable().getAccessName();

		order = " ORDER BY " + alias + "." + fieldStartSyntax + field.getAccessName() + fieldEndSyntax;
	}
	
	/**
	 * Set the result LIMIT
	 * @param offset
	 * @param limitNbr
	 */
	public void setLimit(final int offset, final int limitNbr) {
		String tmp = "";
		tmp += " LIMIT " + limitNbr;
		if(offset > 0)
			tmp += " OFFSET " + offset;
		
		limit = tmp;
	}	
	
	public String toString()
	{
		String out = super.toString();
		out += "\nFinal request: " + request + "\n";
		
		return out;
	}
}
