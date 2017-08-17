<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
    <shiro:hasPermission name="sys:department:view"><li id="departmentList"><a href="${ctx}/sys/department/list">部门列表</a></li></shiro:hasPermission>
	<!--<shiro:hasPermission name="sys:department:edit"><li id="departmentForm"><a href="javascript:void(0)" onclick="addDepartment();">部门添加</a></li></shiro:hasPermission> -->
	<shiro:hasPermission name="sys:department:edit"><li id="departmentForm"><a href="${ctx}/sys/department/form" onclick="addDepartment();">部门添加</a></li></shiro:hasPermission>
	<shiro:hasPermission name="sys:department:view"><li id="departmentJob"><a href="${ctx}/sys/department/jobManagement">职务管理</a></li></shiro:hasPermission>
</content>