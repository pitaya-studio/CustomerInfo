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
	<c:if test="${menuId eq 80}">
    	<li id="2"><a href="${ctx}/airTicket/list/2">已上架产品</a></li>
    	<li id="3"><a href="${ctx}/airTicket/list/3" >已下架产品</a></li>
	</c:if>
	<c:if test="${menuId eq 70}">
		<shiro:hasPermission name="visaProduct:list:onLine">
	    	<li id="2"><a href="${ctx}/visa/visaProducts/list/2">已上架产品</a></li>
	    </shiro:hasPermission>
	    <shiro:hasPermission name="visaProduct:list:offLine">
	    	<li id="3"><a href="${ctx}/visa/visaProducts/list/3" >已下架产品</a></li>
	    </shiro:hasPermission>
	</c:if>
</content>