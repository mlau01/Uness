package unessdb.usql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import unessdb.FIELD;

final class USQLRequestDelete extends USQLRequestFilter {

	public USQLRequestDelete(final FIELD[] p_fieldArray, final boolean p_followLink) {
		super(p_fieldArray, p_followLink, true);
	}

	public void build() throws SQLException {

		request = "DELETE FROM ";
		request += buildTablePart();

		if (filter != null)
			request += filter;
		else
			throw new SQLException("A Delete action must have a filter");

	}

	/**
	 * Execute the DELETE request
	 * @param con
	 * @return 1 if a row was deleted, 0 otherwise
	 * @throws SQLException
	 */
	public final int execute(final Connection con) throws SQLException {
		System.out.println(this.getClass().getName() + ": execute(...): " + request);

		int retVal = 0;

		PreparedStatement ps = con.prepareStatement(request);
		if (params != null) {
			System.out.println(this.getClass().getName() + "Statement Data: (" + params + ")");
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
		
			
		retVal = ps.executeUpdate();

		return retVal;
	}

}
