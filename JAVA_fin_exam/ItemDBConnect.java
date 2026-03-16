package JAVA_fin_exam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector; 

public class ItemDBConnect{
	
	public static Connection con;
	//모듈화로 GUI클래스에서 호출할 수 있게 public 으로 변경
	public static void getDataFromTable(Connection con, String tableName, ArrayList<Item> books) {
		// TODO Auto-generated method stub
		String selectSQL = "select * from " + tableName + ";";
		try {
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			ResultSet rs = pstmt.executeQuery();  // rs --> 검색 결과 .....레코드로 온다!!!
			while(rs.next()) {
				Item item = new Item();// 매번 개체를 만들고 추가
				item.setMenu(rs.getString(1));
				item.setPrice(rs.getInt(2));
				item.setBarcode(rs.getString(3));
				books.add(item);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void insertIntoTable(Connection con, String tableName, Item item) {
		// TODO Auto-generated method stub
		String insertSQL = "insert into " + tableName + " (menu, price, barcode, stock) " +
				"values (?,?,?,?); ";  // ??? 실행 쿼리문을 찾아서 sql문 생성...
		try {
			PreparedStatement pstmt = con.prepareStatement(insertSQL);
			pstmt.setString(1, item.getMenu());  
			pstmt.setInt(2, item.getPrice()); 
			pstmt.setString(3, item.getBarcode()); 
			pstmt.setInt(4, item.getStock());
			pstmt.execute();
			System.out.println("Insert Into Table OK!!!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Insert Into Table Error!!!");
			e.printStackTrace();
		}
		
	}

	public static void dropTable(Connection con, String tableName) {
		// TODO Auto-generated method stub
		String dropTableSQL = "drop table if exists " + tableName + ";";
		try {
			PreparedStatement pstmt = con.prepareStatement(dropTableSQL);
			pstmt.execute();
			System.out.println("Drop Table OK!!!");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Drop Table Error!!!");
			e.printStackTrace();
		}
		
	}

	public static void createTable(Connection con, String tableName) {
		// TODO Auto-generated method stub
		String createTableSQL = "create table if not exists " + tableName + " (" +
				"menu VARCHAR(50) NOT NULL," +
				"price INT NOT NULL, " +
				"barcode VARCHAR(50), " +
				"stock INT NOT NULL, " +
				"UNIQUE INDEX (barcode), " +   
				"PRIMARY KEY(barcode) ) default charset=utf8;";
		
		try {
			PreparedStatement pstmt =con.prepareStatement(createTableSQL);
			System.out.println("Create Table " + tableName + " SQL OK!!!!");
			pstmt.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Create Table " + tableName + " SQL Error!!!!");
			e.printStackTrace();
		}
		
	}

	public static Connection makeConnection() {
		// TODO Auto-generated method stub
		String hostName = "127.0.0.1";  //만일 본인 PC내에 --> localhost : 127.0.0.1
		String databaseName = "javaDB";  // MySQL로 생성한 javaDB 호출  
		String utf8Connection = "?useUnicode=true&characterEncoding=utf8"; // 한글지원
		String url = "jdbc:mysql://"+hostName+":3306/"+databaseName + utf8Connection + "?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Seoul";
		String userName = "root";  // 변경, 만일 본인 PC내에 ... 변경
		String password = "Whgudwns02!";  // 변경, 만일 본인 PC내에 ... 변경		

		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			System.out.println("JDBC Driver Loading OK!!!");
			con = DriverManager.getConnection(url, userName, password);  // url, id, pw
			System.out.println("MYSQL Server Connection OK!!!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("JDBC Driver Loading Error!!!");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("MYSQL Server Connection Error!!!");
			e.printStackTrace();
		}
		
		return con;
	}

	public static void deleteFromTable(Connection con, String tableName, String menu) {
		// TODO Auto-generated method stub
		String deleteSQL = "DELETE FROM " + tableName + " WHERE menu = ?"; 
		try {
			PreparedStatement pstmt = con.prepareStatement(deleteSQL);
			pstmt.setString(1, menu);
			pstmt.execute();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}

	public static void updateTable(Connection con, String tableName, Item itme) {
		// TODO Auto-generated method stub
		String updateSQL = "UPDATE " + tableName + " SET menu = ?, price = ?, stock = ? WHERE barcode = ?";
	
		 try {
		        PreparedStatement pstmt = con.prepareStatement(updateSQL);
		        pstmt.setString(1, itme.getMenu());
		        pstmt.setInt(2, itme.getPrice());
		        pstmt.setInt(3, itme.getStock());
		        pstmt.setString(4, itme.getBarcode());
		       
		        pstmt.executeUpdate();
		        System.out.println("Update Table OK!!!");
		    } catch (SQLException e) {
		        System.out.println("Update Table Error!!!");
		        e.printStackTrace();
		    }
	}
	public static Vector<Vector<String>> searchFromTable(Connection con, String tableName, String keyword) {
	    Vector<Vector<String>> result = new Vector<>();
	    String searchSQL = "SELECT * FROM " + tableName + " WHERE menu LIKE ?";
	    try {
	        PreparedStatement pstmt = con.prepareStatement(searchSQL);
	        pstmt.setString(1, "%" + keyword + "%"); // 메뉴 검색
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            Vector<String> row = new Vector<>();
	            row.add(rs.getString("menu"));
	            row.add("" + rs.getInt("price"));
	            row.add(rs.getString("barcode"));
	            result.add(row);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return result;
	}
}
