<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>批发商列表</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#treeTable").treeTable({expandLevel : 5});
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
		<li class="active"><a href="${ctx}/sys/office/">批发商列表</a></li>
		<shiro:hasPermission name="sys:office:add">
			<li><a href="${ctx}/sys/office/form">批发商添加</a></li>
		</shiro:hasPermission>
	</content>
	<tags:message content="${message}"/>
	<table id="treeTable" class="table table-striped table-bordered table-condensed">
		<tr><th width="11%">批发商id</th><th width="21%">批发商名称</th><th width="18%">批发商编码</th><th width="39%">备注</th><shiro:hasPermission name="sys:office:edit"><th width="11%">操作</th></shiro:hasPermission></tr>
		<c:forEach items="${list}" var="office">
			<tr id="${office.id}" pId="${office.parent.id ne requestScope.office.id?office.parent.id:'0'}">
			    <td>${office.id}</td>
				<td><a href="${ctx}/sys/office/form?id=${office.id}">${office.name}</a></td>
				<td>${office.code}</td>
				<td>${office.remarks}</td>
				<td>
					<shiro:hasPermission name="sys:office:edit">
						<a href="${ctx}/sys/office/form?id=${office.id}">修改</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:office:delete">
						<a href="${ctx}/sys/office/delete?id=${office.id}" onclick="return confirmx('要删除该批发商及所有子机构项吗？', this.href)">删除</a>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>