<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>     
<%@ page import="javax.naming.*" %>     
<%@ page import="javax.sql.DataSource" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'testJNDI.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    Tomcat连接池测试,获取数据源 <br>     
    <%     
        try {      
            //初始化查找命名空间
            Context ctx = new InitialContext();  
            //参数java:/comp/env为固定路径   
            Context envContext = (Context)ctx.lookup("java:/comp/env"); 
            //参数jdbc/mysqlds为数据源和JNDI绑定的名字
            DataSource ds = (DataSource)envContext.lookup("jdbc/mysqlds"); 
            Connection conn = ds.getConnection();   
            PreparedStatement ps = conn.prepareStatement("select * from userinfo");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                /* System.out.println("id : " + rs.getString(1) + " name : "
                        + rs.getString(2) + " password : " + rs.getString(3)); */
                out.println("</p>id : " + rs.getString(1) + " name : "
                        + rs.getString(2) + " password : " + rs.getString(3)+"</p>");
            }
 
            // 关闭记录集
            if (rs != null) {
                rs.close();
            }
 
            // 关闭声明
            if (ps != null) {
                ps.close();
            }
 
            // 关闭链接对象
            if (conn != null) {
                conn.close();
            }   
            out.println("<span style='color:red;'>JNDI测试成功<span>");     
        } catch (NamingException e) {     
            e.printStackTrace();     
        } catch (SQLException e) {     
            e.printStackTrace();     
        }     
    %>     
  </body>
</html>
