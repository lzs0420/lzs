<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();StringBuffer uri=request.getRequestURL();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!-- 测试getXXX()和isXXX() -->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'testbean.jsp' starting page</title>
    
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
  	<jsp:useBean id="ch" class="own.test.bean.User"></jsp:useBean>
  	${path}<%=uri%><br>
  	<%=path%><br>
  	${basePath}<br>
  	<%=basePath%><br>
    This is my JSP page. <br>
    ${ch.love }<br>
    <c:if test="${ch.student}">love</c:if>
    <c:if test="${not ch.student}">hate</c:if>
  </body>
</html>
