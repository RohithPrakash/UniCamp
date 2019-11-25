package com.unicamp.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.unicamp.database.Database;
import com.unicamp.reports.Report;

@WebServlet("/StudentLogin")
public class StudentLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement statement = null;
        ResultSet rs = null;
        
        String email = (String) request.getAttribute("email");
        String password = (String) request.getAttribute("password");
        String userType = "student";
        String id = null;
        
		try {
			conn = Database.getConnection();
			statement = conn.prepareStatement("select * from users where email = ? and password = ? ");
			statement.setString(1, email);
			statement.setString(2, password);
			
			rs = statement.executeQuery();
			
			if(rs.next()) {
				
				id = rs.getString("id");
				
				if(rs.getBoolean("blocked")) {
					Report.loginReport(conn, email, id, userType, "Login", "Account Blocked");
					
					request.setAttribute("blocked", true);
					RequestDispatcher rd = request.getRequestDispatcher("/studentLogin.jsp");
					rd.forward(request, response);
				} else if(!rs.getBoolean("verified")) {
					Report.loginReport(conn, email, id, userType, "Login", "Account Not Verified");
					
					request.setAttribute("verified", false);
					RequestDispatcher rd = request.getRequestDispatcher("/studentLogin.jsp");
					rd.forward(request, response);
				} else {
					
					Report.loginReport(conn, email, id, userType, "Login", "Successful");
					
					request.setAttribute("verified", true);
					
					request.getSession().setAttribute("name", rs.getString("name"));
					request.getSession().setAttribute("email", rs.getString("email"));
					request.getSession().setAttribute("university", rs.getString("university"));
					request.getSession().setAttribute("id", rs.getString("id"));
					request.getSession().setAttribute("course", rs.getString("course"));
					request.getSession().setAttribute("department", rs.getString("department"));
					
					RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
					rd.forward(request, response);
				}
			} else {
				Report.loginReport(conn, email, id, userType, "Login", "Unsuccessful");
				
				request.setAttribute("error", "Check the login credentials and try again.");
				RequestDispatcher rd = request.getRequestDispatcher("/studentLogin.jsp");
				rd.include(request, response);
			}
			
        } catch(Exception e) {
        	System.out.println(e);
        } finally {
            Database.closeConnection(conn);
        }
	}

}
