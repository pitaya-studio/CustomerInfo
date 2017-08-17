<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="com.trekiz.admin.common.beanvalidator.BeanValidators"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>


<%@ include file="/WEB-INF/views/include/taglib.jsp"%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<!-- 为数据库连接偶发性被关闭定义的客户友好提醒 --> 


    <meta name="decorator" content="wholesaler"/>  
</head>
<body>   
	<div class="container-fluid">
		<p><h3><font color="red">${errorMsg }</font></h3></p>
	</div>
</body>
</html>