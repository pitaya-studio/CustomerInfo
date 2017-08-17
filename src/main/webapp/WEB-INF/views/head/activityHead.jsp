<%--
  Created by IntelliJ IDEA.
  User: ZhengZiyu
  Date: 2014/9/23
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">

<%--    <shiro:hasPermission name="product:manager:online"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>--%>
<%--    <shiro:hasPermission name="product:manager:tmp"><li id="tmp" ><a href="${ctx}/activity/manager/list/1/${activityKind}" >草稿中产品</a></li></shiro:hasPermission>--%>
<%--    <shiro:hasPermission name="product:manager:offline"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>--%>
	
	<c:choose>
		<c:when test="${activityKind eq '1'}">
			<shiro:hasPermission name="singleProduct:list:onLine"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>
			<shiro:hasPermission name="singleProduct:list:offLine"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>
		</c:when>
		<c:when test="${activityKind eq '2'}">
<%--			<li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li>--%>
<%--			<li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li>--%>
			<shiro:hasPermission name="looseProduct:list:onLine"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>
			<shiro:hasPermission name="looseProduct:list:offLine"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>
		</c:when>
		<c:when test="${activityKind eq '3'}">
			<shiro:hasPermission name="studyProduct:list:onLine"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>
			<shiro:hasPermission name="studyProduct:list:offLine"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>
		</c:when>
		<c:when test="${activityKind eq '4'}">
			<shiro:hasPermission name="bigCustomerProduct:list:onLine"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>
			<shiro:hasPermission name="bigCustomerProduct:list:offLine"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>
		</c:when>
		<c:when test="${activityKind eq '5'}">
			<shiro:hasPermission name="freeProduct:list:onLine"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>
			<shiro:hasPermission name="freeProduct:list:offLine"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>
		</c:when>
		<c:when test="${activityKind eq '10'}">
			<shiro:hasPermission name="cruiseProduct:list:onLine"><li id="online" ><a href="${ctx}/activity/manager/list/2/${activityKind}">已上架产品</a></li></shiro:hasPermission>
			<shiro:hasPermission name="cruiseProduct:list:offLine"><li id="offline" ><a href="${ctx}/activity/manager/list/3/${activityKind}" >已下架产品</a></li></shiro:hasPermission>
		</c:when>
	</c:choose>

</content>