package unessdb.usql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import unessdb.FIELD;

final class USQLRequestMax extends USQLRequestBuilder {
	
	public USQLRequestMax(final FIELD[] p_fieldArray, final boolean p_followLink)
	{
		super(p_fieldArray, p_followLink, true);
	}
	
	//Build MAX request
	public void build() {
		request = "SELECT MAX(";
		request += buildSelectionFieldPart();
		request += ") FROM ";
		request += buildTablePart();
	}
	
	public final int execute(final Connection con) throws SQLException
	{
		
		PreparedStatement ps = con.prepareStatement(request);
		
		int max = -1;
		try {
			ResultSet res = ps.executeQuery();

			if (res != null && res.next()) {
				max = res.getInt(1);
			}
		} catch (SQLException e) {
			throw e;
		}
		
		return max;
	}


}
