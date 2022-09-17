package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

public class DbConnection {
	
	private static String HOST = "localhost";
	private static String PASSWORD = "projectpassword";
	private static int PORT = 3306;
	private static String SCHEMA = "projects";
	private static String USER = "projects";
	
	public static java.sql.Connection getConnection(){
		String uri = 
				String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST, PORT, SCHEMA, USER, PASSWORD);
		//System.out.println("Connecting with url=" + uri);
		
		try {
			Connection conn = DriverManager.getConnection(uri);
			System.out.println("Successful connection!");
			return conn;
		} catch (SQLException e) {
			System.out.println("Unuccessful connection!");
			throw new DbException(e);
		}
		
	}

}
