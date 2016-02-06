package own.allen.db;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class myDB {
//	 private static  String url ="jdbc:mysql://localhost:3306/test";
//	 private static  String user ="root";
//	 private static  String password ="123456";
//	 private static  String driver ="com.mysql.jdbc.Driver";

	 private void test1(){
		 	String driver = "com.mysql.jdbc.Driver";
	        String dbName = "mysql";
	        String passwrod = "123456";
	        String userName = "root";
	        String url = "jdbc:mysql://localhost:3306/" + dbName;
	        String sql = "select * from user where id = ?";
	        Object o =1;
	        try {
	            Class.forName(driver);
	            Connection conn = DriverManager.getConnection(url, userName,
	                    passwrod);
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setObject(1, o);
	            System.out.println(ps.toString());
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                System.out.println("id : " + rs.getString(1) + " name : "
	                        + rs.getString(2) + " password : " + rs.getString(3));
	            }
	 
	            // 关闭记录集
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	            // 关闭声明
	            if (ps != null) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	            // 关闭链接对象
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
	 
	 private void test2(){
		 String driver = "org.sqlite.JDBC";
	        String dbName = "d:\\Sqlite\\test.db";
	        String url = "jdbc:sqlite:" + dbName;
	        String sql = "select * from mytable";
	 
	        try {
	    		InputStream inStream = JdbcUtils.class.getResourceAsStream("/jdbc.properties");
	    		Properties prop = new Properties();
	    		prop.load(inStream);
	    		driver = prop.getProperty("jdbc.sqlite.driver");
	    		url = prop.getProperty("jdbc.sqlite.url");
	    		System.out.println("===================读取配置文件成功！======================");  
			} catch (Exception e) {
				throw new RuntimeException("读取配置文件异常",e);
			}
	        
	        try {
	            Class.forName(driver);
	            Connection conn = DriverManager.getConnection(url);
	            PreparedStatement ps = conn.prepareStatement(sql);
	            System.out.println(ps.toString());
	            ResultSet rs = ps.executeQuery();
	            while (rs.next()) {
	                System.out.println("id : " + rs.getString(1) + " value : "
	                        + rs.getString(2) );
	            }
	 
	            // 关闭记录集
	            if (rs != null) {
	                try {
	                    rs.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	            // 关闭声明
	            if (ps != null) {
	                try {
	                    ps.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	            // 关闭链接对象
	            if (conn != null) {
	                try {
	                    conn.close();
	                } catch (SQLException e) {
	                    e.printStackTrace();
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 }
	 private static String username;
	 private static void loanConfig() {
	    	try {
	    		System.setProperty("username", "long");
	    		InputStream inStream = JdbcUtils.class.getResourceAsStream("/jdbc.properties");
	    		Properties prop = new Properties();
	    		prop.load(inStream);
	    		username = prop.getProperty("test");
	    		System.out.println("===================读取配置文件成功！======================");  
			} catch (Exception e) {
				throw new RuntimeException("读取配置文件异常",e);
			}
		}
	static{
		loanConfig();
	}
    public static void main(String[] args) throws ClassNotFoundException {
//    	new myDB().test1();
//        new myDB().test2();
    		Class<?> s =Class.forName("java.util.XMLUtils");
    		Field[] f = s.getDeclaredFields();
    		for (Field field : f) {
    			System.out.println(field.toString());
    		}
    }
 
}