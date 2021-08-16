package unessdb.usql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import unessdb.FIELD;
import unessdb.UData;

final class USQLRequestUpdate extends USQLRequestFilter{
	
	private UData datas;
	private final boolean verbose = true;
	
	public USQLRequestUpdate(final FIELD[] p_fieldArray, final boolean p_followLink) throws SQLException
	{
		super(p_fieldArray, p_followLink, true);
	}
	
	public void build() throws SQLException{
		request = "UPDATE ";
		request += buildTablePart();
		request += " SET ";
		if(datas != null)
		{
			String values = "";
			for(USQLRelation so : fieldsMap.values())
			{
				if( ! values.isEmpty())
					values += ", ";

				values += so.getSourceTableAlias() + ".[" + so.getSourceField().getAccessName() + "]";
				values += "=?";
			}
			
			request += values;
		}
		else
			throw new SQLException("Data object is null");
		if (filter != null)
			request += filter;
		else
			throw new SQLException("A filter has to be set for UPDATE action");
	}
	
	/**
	 * Set the data for INSERT request
	 * @param p_datas UData object
	 */
	public void setWriteDatas(final UData p_datas) {
		datas = p_datas;
	}
	public final UData getWriteDatas()
	{
		return datas;
	}
	
	public final int execute(final Connection con) throws SQLException
	{
		int retVal = 0;
	
		if(verbose) System.out.println(this.getClass().getName() + "Preparing statement: " + request);
		
		
		PreparedStatement ps = con.prepareStatement(request);
		
		UData datas = UData.concat(this.getWriteDatas(), params);
		if(verbose) System.out.println(this.getClass().getName() + "Statement datas: " + datas);
		
		int index = 1;
		for(FIELD field : datas.getFields())
		{
			String value = String.valueOf(datas.getValue(field));
			
			if(field.isLinked())
			{
				String newValue = USQLUtils.getIdValue(con, field.getLink(), value);
				if(verbose) System.out.println(this.getClass().getName() + " -> link found: " + field + "(" + value + ") become " + field + "(" + newValue + ")");
				if(newValue.isEmpty()) throw new SQLException("Unknown value: " + value);
				value = newValue;
			}
			
			if(verbose) System.out.println(this.getClass().getName() + " -> Parsing field: " + field + " value: " + value);
			switch(field.getAccessType())
			{
			
				case TEXT:
					ps.setString(index, value);
				break;
				case DIGIT:
					ps.setInt(index, Integer.valueOf(value));
				break;
				case DATE:
					if(value.equals("null") || value.isEmpty()) ps.setNull(index, java.sql.Types.DATE);
					else ps.setDate(index, java.sql.Date.valueOf(value));
				break;
			}
			
			index++;
		}
		retVal = ps.executeUpdate();

		
		return retVal;
	}

}
