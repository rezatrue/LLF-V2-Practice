package db;
/*
 * Sqlite DB for keeping data in local machine
 * http://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LocalDBHandler {
	
	private final String DB_NAME = "llf.db";
	private String TABLE_NAME = "profile";
	private Connection conn = null;

	public LocalDBHandler() {
	}
		
	public void connect() {
	        try {
	            String url = "jdbc:sqlite:sqlite/db/" + DB_NAME;
	            conn = DriverManager.getConnection(url);
	            
	            System.out.println("Connection to SQLite has been established.");
	            
	        } catch (SQLException e) {
	            System.out.println("0"+e.getMessage());
	        }
	    }
	
	public void closeConnection() {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("10"+ex.getMessage());
            }
	}
	
	public void dropTable() {
		
	}
	
	public void createNewTable() {
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("
                + "	Linkedin_Profile_URL text PRIMARY KEY,"
                + "	First_Name text,"
                + "	Last_Name text,"
                + "	Email_ID text,"
                + "	Contact_Number text,"
                + "	Location text,"
                + "	Industry text,"
                + "	Designation text,"
                + "	Company_Name text,"
                + "	Company_Size text"
                + ");";
        
        if(conn==null)
        	connect();
        
        Statement stmt = null;
        
        try {
			if(conn!=null) {
				stmt = conn.createStatement();
			    stmt.execute(sql);
			}
		} catch (SQLException e) {
			System.out.println("1"+e.getMessage());
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("2"+e.getMessage());
			}
		}
    }
	
	public void selectAll(){
        String sql = "SELECT * FROM " + TABLE_NAME;
        if(conn == null)
        	connect();
        
        Statement stmt = null;
        ResultSet rs = null;
        if(conn!=null) {
        	
        	try {
				stmt  = conn.createStatement();
				rs    = stmt.executeQuery(sql);
				// loop through the result set
				while (rs.next()) {
				    System.out.println( 
				                       rs.getString("Linkedin_Profile_URL") + "\t" +
				                       rs.getString("First_Name") +  "\t" + 
				                       rs.getString("Last_Name") + "\t" +
				                       rs.getString("Email_ID") +  "\t" + 
				                       rs.getString("Contact_Number") + "\t" +
				                       rs.getString("Location") +  "\t" + 
				                       rs.getString("Industry") + "\t" +
				                       rs.getString("Designation") +  "\t" + 
				                       rs.getString("Company_Name") + "\t" +
				                       rs.getString("Company_Size"));
				}
			} catch (SQLException e) {
				System.out.println("5"+e.getMessage());
			}finally {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e) {
					System.out.println("6"+e.getMessage());
				}
				
			}
			
        }
	}

	public void insert(String link, String fname, String lname, String company) {
        String sql = "INSERT INTO "+ TABLE_NAME + " (Linkedin_Profile_URL, First_Name, Last_Name, Company_Name) VALUES(?,?,?,?)";
        PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, link);
				pstmt.setString(2, fname);
				pstmt.setString(3, lname);
				pstmt.setString(4, company);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("3"+e.getMessage());
			}finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("4"+e.getMessage());
				}
			}
        }  
    }
	
}
