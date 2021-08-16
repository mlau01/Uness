package unessdb.usql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import unessdb.FIELD;
import unessdb.UData;

class USQLUtils {
	
	/**
	 * Test is the value exist for a field
	 * @param f FIELD
	 * @param value String value
	 * @return Boolean
	 */
	/*
	public static final boolean exists(final Connection con, final FIELD f, final String value) throws SQLException {
		
		final USQLRequestCount count = new USQLRequestCount(new FIELD[] { f }, false);
		
		boolean bool = false;
		
		count.setFilter(f, value);
		count.build();
		double nbr = count.execute(con);

		if (nbr == 1)
			bool = true;


		return bool;
	}
	*/
	/**
	 * Search the reference field value and return the first entry id value
	 * @param refField reference FIELD
	 * @param value Value of the reference field
	 * @return String id
	 */
	
	public static final String getIdValue(final Connection con, final FIELD refField, final String refValue) throws SQLException {
		String aid = "";
		FIELD idField = FIELD.getIdField(refField.getTable());
		
		USQLRequestSelect select = new USQLRequestSelect(new FIELD[] {idField}, true);

		select.setFilter(new USQLParams(refField, refValue));
		select.build();
		ArrayList<UData> datas = select.execute(con);

		if( ! datas.isEmpty()) aid = String.valueOf(datas.get(0).getValue(idField));
	
		
		return aid;

	}

}
