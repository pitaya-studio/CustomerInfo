<%--
  Created by IntelliJ IDEA.
  User: ZhengZiyu
  Date: 2014/9/23
  Time: 16:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>

<content tag="three_level_menu">
	<shiro:hasPermission name="supplier:manager:view">
		<li id="supplierList"><a href="${ctx}/supplier/supplierInfoList">地接社列表</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="supplier:manager:add">
		<li id="supplierAdd"><a href="${ctx}/supplier/supplierFirstForm">地接社<shiro:hasPermission name="supplier:manager:add">${not empty supplierId?'修改':'添加'}</shiro:hasPermission></a></li>
	</shiro:hasPermission>
</content>