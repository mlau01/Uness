package unessdb.usql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import unessdb.FIELD;
import unessdb.UData;

final class USQLRequestInsert extends USQLRequestBuilder {
	
	private final boolean verbose = true;
	private UData datas;
	
	public USQLRequestInsert(final FIELD[] p_fieldArray, final boolean p_followLink)
	{
		super(p_fieldArray, p_followLink, false);
	}
	
	public void build() throws SQLException {
		request = "INSERT INTO ";
		request += buildTablePart();
		request += "(" + buildSelectionFieldPart() + ")";
		request += " VALUES (";
		
		if(datas != null)
		{
			String values = "";
			for(int i = 0; i < datas.getSize(); i++) {
				if( ! values.isEmpty())
					values += ",";
				values += "?";
			}
			request += values + ")";
		}
		else
			throw new SQLException("Missing insert datas values");

	}
	
	/**
	 * Set the data for INSERT request
	 * @param p_datas UData object
	 */
	public void setDatas(final UData p_datas) {
		datas = p_datas;
	}
	
	public final UData getDatas()
	{
		return datas;
	}
	
	public final int execute(final Connection con) throws SQLException
	{
		if(verbose) System.out.println("Preparing statement: " + request);
		if(verbose) System.out.println("Statement datas: " + datas);
		
		PreparedStatement ps = con.prepareStatement(request);
		
		int index = 1;
		for(FIELD field : datas.getFields())
		{
			String value = String.valueOf(datas.getValue(field));
			
			//If the field is linked, search the id value pointed 
			if(field.isLinked())
			{
				String newValue = USQLUtils.getIdValue(con, field.getLink(), value);
				if(verbose) {
					System.out.println(this.getClass().getName() + " ->\nlink: " + field + " value: " + value + "\nbecome: " + field + " value: " + newValue);
				}
				if(newValue.isEmpty()) throw new SQLException("Unknown value: " + value);
				value = newValue;
				
			}
			
			
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

		return ps.executeUpdate();
	}

}
