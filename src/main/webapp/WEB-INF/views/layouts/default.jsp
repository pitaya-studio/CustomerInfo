<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<!DOCTYPE html>
<html style="overflow-x:hidden;overflow-y:auto;">
	<head>
		<title><sitemesh:title/></title>
		<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
		<%@include file="/WEB-INF/views/include/head.jsp" %>
		<sitemesh:head/>
        <%@include file="/WEB-INF/views/include/dialog.jsp" %>
	</head>
	<body>
		<sitemesh:body/>
	</body>
</html>
