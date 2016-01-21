package mine.allen.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class test {

	public static void main(String[] args) {
		String driver = "com.mysql.jdbc.Driver";
        String dbName = "longchenhan";
        String passwrod = "123456";
        String userName = "root";
        String url = "jdbc:mysql://localhost:3306/" + dbName;
        String sql = "insert into longchenhan values (?,?,?)";
        String[] arrays = {"long","chen","han","hou","fan","zhu"};
 
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, userName,
                    passwrod);
            PreparedStatement ps = conn.prepareStatement(sql);
            int i=2;
            while(i>0){
            	ps.setString(1, arrays[i*3-3]);
            	ps.setString(2, arrays[i*3-2]);
            	ps.setString(3, arrays[i*3-1]);
            	ps.addBatch();
            	i--;
            }
            System.err.println(ps.executeBatch());
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                System.out.println("id : " + rs.getString(1) + " name : "
//                        + rs.getString(2) + " password : " + rs.getString(3));
//            }
 
            // 关闭记录集
//            if (rs != null) {
//                try {
//                    rs.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
 
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
	
	/*System.err.println("path="+path);
    System.err.println("AuthType="+request.getAuthType());
    System.err.println("ContextPath="+request.getContextPath());
    System.err.println("head="+request.getHeader("USER-AGENT"));
    System.err.println("LocalAddr="+request.getLocalAddr());
    System.err.println("LocalName="+request.getLocalName());
    System.err.println("Method="+request.getMethod());
    System.err.println("PathInfo="+request.getPathInfo());
    System.err.println("PathTranslated="+request.getPathTranslated());
    System.err.println("Protocol="+request.getProtocol());
    System.err.println("LocalName="+request.getRealPath("/"));*/

}
