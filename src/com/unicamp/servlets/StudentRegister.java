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

@WebServlet("/StudentRegister")
public class StudentRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		PreparedStatement statement = null;
        ResultSet rs = null;
        
        String email = (String) request.getAttribute("email");
        String password = (String) request.getAttribute("password");
        String rePassword = (String) request.getAttribute("rePassword");
        String id = (String) request.getAttribute("id");
        String universityId = (String) request.getAttribute("universityId");
        String firstName = (String) request.getAttribute("firstName");
        String lastName = (String) request.getAttribute("lastName");
        String userType = "student";
        
        try {
        	
        	RequestDispatcher rd = null;
        	
        	if(password.equals(rePassword)) {
        		request.setAttribute(password, "The passwords do not match.");
        		rd = request.getRequestDispatcher("/studentRegister");
        		rd.forward(request, response);
        	} else {
        		boolean emailPresent = false, idPresent = false;
            	
            	conn = Database.getConnection();
    			statement = conn.prepareStatement("select * from students where email = ?");
    			statement.setString(1, email);
    			
    			rs = statement.executeQuery();
    			
    			if(rs.next()) {
    				emailPresent = true;
    				request.setAttribute("email", "Email taken, try another.");
    			}
    			
    			statement = conn.prepareStatement("select * from students where id = ?");
    			statement.setString(1, id);
    			
    			rs = statement.executeQuery();
    			
    			if(rs.next()) {
    				idPresent = true;
    				request.setAttribute("id", "College ID already registered, please contact your university to if you haven't.");
    			}
    			
    			if(idPresent || emailPresent) {
    			rd = request.getRequestDispatcher("/studentLogin.jsp");
    				rd.forward(request, response);
    			}
    			
    			if(!idPresent && !emailPresent) {
    				boolean success = false;
    				
    				statement = conn.prepareStatement("insert into students (email, password, id, userType, firstName, lastName, universityId) values (?,?,?,?,?,?,?)");
    				statement.setString(1, email);
    				statement.setString(2, password);
    				statement.setString(3, id);
    				statement.setString(4, userType);
    				statement.setString(5, firstName);
    				statement.setString(6, lastName);
    				statement.setString(7, universityId);
    				
    				success = statement.execute();
    				
    				if(success) {
    					request.setAttribute("success", "Request forwarded to your universtity. Your account will be active soon. Contact your university if not done within 48 hours.");
    					rd = request.getRequestDispatcher("/studentRegister");
    					rd.forward(request, response);
    				} else {
    					request.setAttribute("fail", "Registration failed. Try again. If issue peesists contact university or tech support.");
    					rd = request.getRequestDispatcher("/studentRegister");
    					rd.forward(request, response);
    				}
    			}
        	}       	
			
        } catch(Exception e) {
        	
        } finally {
        	Database.closeConnection(conn);
        }
	}
}
