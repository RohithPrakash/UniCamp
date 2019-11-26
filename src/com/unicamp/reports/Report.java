package com.unicamp.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Report {

	public static void report(Connection conn, String email, String id, String userType, String action, String outcome) throws SQLException {
		
		PreparedStatement report = conn.prepareStatement("insert into report(email, id, userType, action, outcome)"+ "values (?, ?, ?, ?, ?)");
		report.setString(1, email);
		report.setString(2, id);
		report.setString(3, userType);
		report.setString(4, action);
		report.setString(5, outcome);
		report.execute();
	}
	
	public static void report(Connection conn, String email, String id, String userType, String action, String outcome, String verifiedId) throws SQLException {
		
		PreparedStatement report = conn.prepareStatement("insert into report(email, id, userType, action, outcome, verifiedId)"+ "values (?, ?, ?, ?, ?, ?)");
		report.setString(1, email);
		report.setString(2, id);
		report.setString(3, userType);
		report.setString(4, action);
		report.setString(5, outcome);
		report.setString(6, verifiedId);
		report.execute();
	}

}
