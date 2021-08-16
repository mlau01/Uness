package unessdb.usql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import unessdb.DBException;
import unessdb.usql.USQL.DB;

class USQLConnection {
	
	public static final Connection connect(final DB useDb, final String dburl, final String dbbase, final String dbLogin, final String dbpwd) throws DBException {
		final Connection tcon;

		String scon = null;
		if(useDb == DB.mssql || useDb == DB.mysql)
			scon = "jdbc:sqlserver://" + dburl + ":1433;database="+ dbbase + ";user=" + dbLogin + "@" + dbbase + ";password=" + dbpwd + ";encrypt=true;loginTimeout=30;";
		else if(useDb == DB.access) {
			final File db = new File(dburl);
			long size = db.length();
			String passwordEntry = ";password=" + dbpwd;
			String noMem=size>30000000?";memory=false":"";
			
			scon = "jdbc:ucanaccess://" + db.getAbsolutePath() + passwordEntry+noMem;
		}
		try {
			tcon = DriverManager.getConnection(scon);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("Failed to connect : " + e.getMessage());
		}
		//TODO
		//tcon.setAutoCommit(false);
		
		return tcon;
	}

}
