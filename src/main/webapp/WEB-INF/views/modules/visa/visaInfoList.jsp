<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>签证信息管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
    <content tag="three_level_menu">
        <shiro:hasPermission name="product:manager:view"><li class="active"><a href="${ctx}/activity/manager/list">已发布产品</a></li></shiro:hasPermission>
        <shiro:hasPermission name="product:manager:add"><li><a href="${ctx}/activity/manager/form">发布新产品</a></li></shiro:hasPermission>
		<li class="active"><a href="${ctx}/visa/visaInfo/">签证信息列表</a></li>
		<shiro:hasPermission name="visa:visaInfo:edit"><li><a href="${ctx}/visa/visaInfo/form">签证信息添加</a></li></shiro:hasPermission>
	</content>
	<form:form id="searchForm" modelAttribute="visaInfo" action="${ctx}/visa/visaInfo/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>名称 ：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="搜索"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>名称</th><th>备注</th><shiro:hasPermission name="visa:visaInfo:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="visaInfo">
			<tr>
				<td><a href="${ctx}/visa/visaInfo/form?id=${visaInfo.id}">${visaInfo.name}</a></td>
				<td>${visaInfo.remarks}</td>
				<shiro:hasPermission name="visa:visaInfo:edit"><td>
    				<a href="${ctx}/visa/visaInfo/form?id=${visaInfo.id}">修改</a>
					<a href="${ctx}/visa/visaInfo/delete?id=${visaInfo.id}" onclick="return confirmx('确认要删除该签证信息吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
