package uness.interfaces;

import java.sql.SQLException;

public interface Deletable {
	
	public int remove() throws SQLException, Exception;

}
