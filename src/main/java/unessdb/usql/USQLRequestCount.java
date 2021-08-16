package unessdb.usql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import unessdb.FIELD;

final class USQLRequestCount extends USQLRequestFilter {

	public USQLRequestCount(final FIELD[] p_fieldArray, final boolean p_followLink) {
		super(p_fieldArray, p_followLink, true);
	}

	// Build COUNT request
	public void build() {
		request = "SELECT COUNT(";
		request += buildSelectionFieldPart();
		request += ") FROM ";
		request += buildTablePart();
		if (filter != null) {
			request += filter;
		}
	}

	public final double execute(final Connection con) throws SQLException {
		double count = -1;
		PreparedStatement ps = con.prepareStatement(request);
		
		if (params != null) {
			System.out.println(this.getClass().getName() + ": Statement Data: (" + params + ")");
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
		}
		ResultSet res = ps.executeQuery();
		if (res.next())
			count = res.getDouble(1);

		return count;
	}

}
