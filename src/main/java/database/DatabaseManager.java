package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
	private Connection connection;
	
	private static final String HOST = "sql7.freemysqlhosting.net";
	private static final int PORT = 3306;
	private static final String DATABASE = "sql7754505";
	private static final String USERNAME = "sql7754505";
	private static final String PASSWORD = "ZDLTeu4VtA";
	
	public void openConnection() throws SQLException {
		//jdbc:mysql://<host>:<port>/<database>
		String url = String.format("jdbc:mysql://%s:%s/%s", HOST, PORT, DATABASE); 
		connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
		connection.setAutoCommit(false);
	}
	
	public void closeConnection() throws SQLException {
		if(connection.isClosed())
			return;
		
		connection.commit();
		connection.close();
	}
	
	public void saveUsageTime(String userID, long usageTime) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(
				"INSERT INTO AppUsage (USER_ID, USED_TIME) values (?, ?)");
		statement.setString(1, userID);
		statement.setLong(2, usageTime);
		
		statement.execute();
		connection.commit();
	}
}
