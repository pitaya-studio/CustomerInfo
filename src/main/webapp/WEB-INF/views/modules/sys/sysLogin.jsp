<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- ---根据不同的域名判断登录页面--djw--- -->
<c:choose>
	<c:when test="${fn:contains(pageContext.request.requestURL,'t1.quauqsystem.com')}">
		<!-- t1的登录页面 -->
		<%@include file = "/WEB-INF/views/modules/quauq/quauqLogin.jsp" %>
	</c:when>
	<c:when test="${fn:contains(pageContext.request.requestURL,'quauq.biz')}">
		<!-- t1的登录页面 -->
		<%@include file = "/WEB-INF/views/modules/quauq/quauqLogin.jsp" %>
	</c:when>
	<c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
		<!-- 辉腾国际的登录页面 -->
		<%@include file = "/WEB-INF/views/modules/quauq/huiTengLogin.jsp" %>
	</c:when>
	<c:when test="${fn:contains(pageContext.request.requestURL,'travel.jsjbt')}">
		<!-- 金陵国旅的登录页面 -->
		<%@include file = "/WEB-INF/views/modules/quauq/jinLingLogin.jsp" %>
	</c:when>
	<c:otherwise>
		<!-- t2的登录页面 -->
		<%@include file = "/WEB-INF/views/modules/sys/t2Login.jsp" %>
	</c:otherwise>
</c:choose>