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
import java.util.LinkedList;

import pojo.People;


public class DbPeople implements LocalDBHandler{
	
	private String TABLE_NAME = "people";
	private Connection conn = null;

	public DbPeople() {
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
	public boolean dropTable() {
		boolean status = true;
		// Drop table if previous information exist
		String sqlTDrop = "drop table IF EXISTS " + TABLE_NAME;
        
        if(conn==null)
        	connect();
        
        Statement stmt = null;
        
        try {
			if(conn!=null) {
				stmt = conn.createStatement();
			    stmt.execute(sqlTDrop);
			}
		} catch (SQLException e) {
			status = false;
			System.out.println("1"+e.getMessage());
		}finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out.println("2"+e.getMessage());
			}
		}
        System.out.println(" drop : " + status );
        return status;
	}
	
	public boolean createNewTable() {
		// Drop table if previous information exist
		if(!dropTable())
			return false;
        // SQL statement for creating a new table
        String sqlTCreate = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("
                + "	Linkedin_Profile_URL text PRIMARY KEY,"
                + "	First_Name text,"
                + "	Last_Name text,"
                + "	Email_ID text,"
                + "	Address text,"
                + "	Designation text,"
                + "	Service_Range text,"
                + "	Company text,"
                + "	Location text,"
                + "	Degree_Name text,"
                + "	FOS text,"
                + "	Institute text,"
                + "	Dates text"
                + ");";
        
        if(conn==null)
        	connect();
        
        Statement stmt = null;
        
        try {
			if(conn!=null) {
				stmt = conn.createStatement();
			    stmt.execute(sqlTCreate);
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
        
        return true;
    }
	
	public int countRecords() {
		int count = 0;
		String sql = "SELECT count(*) FROM " + TABLE_NAME;
		if(conn == null)
        	connect();
		Statement stmt = null;
		ResultSet rset = null;
		if(conn != null) {
			try {
				stmt = conn.createStatement();
				rset = stmt.executeQuery(sql);
				rset.next();
				count = rset.getInt(1);
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}finally {
				try {
					stmt.close();
					rset.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("total : " + count);
		return count;
	}
	
	public People selectAtIndex(int num) {
		People people = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " LIMIT 1 OFFSET " + num;
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
					System.out.println(rs.getString("Linkedin_Profile_URL"));
				    people = new People(
		                       rs.getString("Linkedin_Profile_URL"),
		                       rs.getString("First_Name"), 
		                       rs.getString("Last_Name"),
		                       rs.getString("Email_ID"), 
		                       rs.getString("Address"),
		                       rs.getString("Designation"), 
		                       rs.getString("Service_Range"),
		                       rs.getString("Company"),
		                       rs.getString("Location"),
		                       rs.getString("Degree_Name"),
		                       rs.getString("FOS"),
		                       rs.getString("Institute"),
		                       rs.getString("Dates")
					);
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
		return people;
	}
	public LinkedList<People> selectRows(int num){
		LinkedList<People> list  = new LinkedList<>();
		
        String sql = "SELECT * FROM " + TABLE_NAME + " LIMIT " + num;
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
					People people = new People(
				                       rs.getString("Linkedin_Profile_URL"),
				                       rs.getString("First_Name"), 
				                       rs.getString("Last_Name"),
				                       rs.getString("Email_ID"), 
				                       rs.getString("Address"),
				                       rs.getString("Designation"), 
				                       rs.getString("Service_Range"),
				                       rs.getString("Company"),
				                       rs.getString("Location"),
				                       rs.getString("Degree_Name"),
				                       rs.getString("FOS"),
				                       rs.getString("Institute"),
				                       rs.getString("Dates")
							);
					list.add(people);
					
				    System.out.println( 
				                       rs.getString("Linkedin_Profile_URL") + "\t" +
				                       rs.getString("First_Name") +  "\t" + 
				                       rs.getString("Last_Name") 
				                       );
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
        return list;
	}

	public boolean insert(Object obj) {
		People people = (People)obj;
		String sql = "INSERT INTO " 
					+ TABLE_NAME 
					+ " (Linkedin_Profile_URL, First_Name, Last_Name, Email_ID, Address,"
					+ " Designation, Service_Range, Company, Location,"
					+ " Degree_Name, FOS, Institute, Dates) "
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, people.getLink());
				pstmt.setString(2, people.getFirstName());
				pstmt.setString(3, people.getLastName());
				pstmt.setString(4, people.getEmail());
				pstmt.setString(5, people.getAddress());
				pstmt.setString(6, people.getCurrentJobTitle());
				pstmt.setString(7, people.getServiceRange());
				pstmt.setString(8, people.getCurrentCompany());
				pstmt.setString(9, people.getCompanyLocation());
				pstmt.setString(10, people.getDegreeName());
				pstmt.setString(11, people.getFos());
				pstmt.setString(12, people.getInstitute());
				pstmt.setString(13, people.getDates());
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("3"+e.getMessage());
				e.printStackTrace();
				return false;
			}finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("4"+e.getMessage());
				}
			}
        }
        return true;
	}
	
	public boolean update(Object obj, String salesLink) {
		People people = (People) obj;
		String sql = "UPDATE " 
					+ TABLE_NAME 
					+ " SET Linkedin_Profile_URL = ?, First_Name = ?, Last_Name = ?, Email_ID = ?, Address = ?,"
					+ " Designation = ?, Service_Range = ?, Company = ?, Location = ?,"
					+ " Degree_Name = ?, FOS = ?, Institute = ?, Dates = ? "
					+ " WHERE Linkedin_Profile_URL = ?";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, people.getLink());
				pstmt.setString(2, people.getFirstName());
				pstmt.setString(3, people.getLastName());
				pstmt.setString(4, people.getEmail());
				pstmt.setString(5, people.getAddress());
				pstmt.setString(6, people.getCurrentJobTitle());
				pstmt.setString(7, people.getServiceRange());
				pstmt.setString(8, people.getCurrentCompany());
				pstmt.setString(9, people.getCompanyLocation());
				pstmt.setString(10, people.getDegreeName());
				pstmt.setString(11, people.getFos());
				pstmt.setString(12, people.getInstitute());
				pstmt.setString(13, people.getDates());
				pstmt.setString(14, salesLink);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.out.println("3"+e.getMessage());
				return false;
			}finally {
				try {
					pstmt.close();
				} catch (SQLException e) {
					System.out.println("4"+e.getMessage());
				}
			}
        }
        return true;
	}



	
}
