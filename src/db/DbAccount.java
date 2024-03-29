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

import pojo.Account;

public class DbAccount implements LocalDBHandler{
	
	private String TABLE_NAME = "account";
	private Connection conn = null;

	public DbAccount() {

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
		if(!dropTable()) return false;
		
        // SQL statement for creating a new table
        String sqlTCreate = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME + " ("
                + "	Linkedin_Company_URL text PRIMARY KEY,"
                + "	Company_Name text,"
                + "	Headquarters text,"
                + "	Website text,"
                + "	Founded text,"
                + "	Company_Size text,"
                + "	Industry text,"
                + "	Company_Type text"
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
	
	public Account selectAtIndex(int num) {
		Account account = null;
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
				    System.out.println(rs.getString("Linkedin_Company_URL"));
				    account = new Account(
		                       rs.getString("Linkedin_Company_URL"),
		                       rs.getString("Company_Name"), 
		                       rs.getString("Headquarters"),
		                       rs.getString("Website"), 
		                       rs.getString("Founded"),
		                       rs.getString("Company_Size"), 
		                       rs.getString("Industry"),
		                       rs.getString("Company_Type")
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
		return account;
	}
	public LinkedList<Account> selectRows(int num){
		LinkedList<Account> list  = new LinkedList<>();
		
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
					Account acc = new Account(
				                       rs.getString("Linkedin_Company_URL"),
				                       rs.getString("Company_Name"), 
				                       rs.getString("Headquarters"),
				                       rs.getString("Website"), 
				                       rs.getString("Founded"),
				                       rs.getString("Company_Size"), 
				                       rs.getString("Industry"),
				                       rs.getString("Company_Type")
							);
					list.add(acc);
					
				    System.out.println( 
				                       rs.getString("Linkedin_Company_URL") + "\t" +
				                       rs.getString("Company_Name") +  "\t" + 
				                       rs.getString("Headquarters") + "\t" +
				                       rs.getString("Website") +  "\t" + 
				                       rs.getString("Founded") + "\t" +
				                       rs.getString("Company_Size") +  "\t" + 
				                       rs.getString("Industry") + "\t" +
				                       rs.getString("Company_Type") );
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
		
		Account acc = (Account)obj;
		String sql = "INSERT INTO " 
					+ TABLE_NAME 
					+ " (Linkedin_Company_URL, Company_Name, Headquarters, Website, Founded,"
					+ " Company_Size, Industry, Company_Type) "
					+ " VALUES(?,?,?,?,?,?,?,?)";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, acc.getComUrl());
				pstmt.setString(2, acc.getComName());
				pstmt.setString(3, acc.getComHeadquarters());
				pstmt.setString(4, acc.getComWebsite());
				pstmt.setString(5, acc.getComFounded());
				pstmt.setString(6, acc.getComSize());
				pstmt.setString(7, acc.getComIndustry());
				pstmt.setString(8, acc.getComType());
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
	
	public boolean update(Object obj, String comLink) {
		
		// not updating all cell as there are data available
		
		Account acc = (Account)obj;
		String sql = "UPDATE " 
					+ TABLE_NAME 
					+ " SET Linkedin_Company_URL = ?, Website = ?, Founded = ?, Company_Type = ? "
					+ " WHERE Linkedin_Company_URL = ?";
		
		PreparedStatement pstmt = null;
        if(conn == null)
        	connect();
        if(conn != null) {
	        try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, acc.getComUrl());
				pstmt.setString(2, acc.getComWebsite());
				pstmt.setString(3, acc.getComFounded());
				pstmt.setString(4, acc.getComType());
				pstmt.setString(5, comLink);
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
