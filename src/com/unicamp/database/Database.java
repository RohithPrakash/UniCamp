package com.unicamp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
	public static Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		
        String url = "jdbc:mysql://localhost:3306/";
        String database = "unicamp";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "root";

        Class.forName(driver).newInstance();
        Connection conn = DriverManager.getConnection(url + database, username, password);

        return conn;
	}
	
	public static void closeConnection(Connection conn) {
        try {
        	conn.close();
        } catch(SQLException e) {
        	System.out.println(e);
        }

    }
}
