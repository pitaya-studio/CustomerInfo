<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>this is the secondpage</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<!-- 
 DEVELOP_DEMO
 -->

  </head>
  
  <body>
  <form action="${flowExecutionUrl}&_eventId=submit" method="post">
  
       籍贯: <input type = "text" name = "userJG" /><br/>
       
       政治: <input type ="text" name = "userZH" />
       
   <input type="submit" value="下一步" />
  </form>
  
       <div>
      姓名:<span id="userName">${user.userName}</span>
   </div>
   
   <a href="${flowExecutionUrl}&_eventId=back">返回</a>
  </body>
</html>
