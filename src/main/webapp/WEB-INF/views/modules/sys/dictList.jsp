<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
		function formReset(){
			$(':input','#searchForm')
			.not(':button, :submit, :reset, :hidden')
			.val('')
			.removeAttr('checked')
			.removeAttr('selected');
		}
		function returnUrl(content,des,type){
			window.location.href = content+"/sys/dict/form?description="+encodeURI(encodeURI(des))+"&type="+type;
		}
	</script>
</head>
<body>
    <content tag="three_level_menu">
		<li class="active"><a href="${ctx}/sys/dict/">字典列表</a></li>
		<shiro:hasPermission name="sys:dict:edit"><li><a href="${ctx}/sys/dict/form">字典添加</a></li></shiro:hasPermission>
	</content>
	<form:form id="searchForm" modelAttribute="dict" action="${ctx}/sys/dict/" method="post" class="form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 wpr20">
				<label class="activitylist_team_co2_text">类型：</label>
				<div class="selectStyle">
					<form:select id="type" path="type" class="input-medium">
						<form:option value="" label=""/>
						<form:options items="${typeList}" htmlEscape="false"/>
					</form:select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co2">
			<label class="activitylist_team_co2_text">描述 ：</label>
				<form:input path="description" htmlEscape="false" maxlength="50" class="input-medium"/>
			</div>
			<div class="form_submit">
				<input id="btnSubmit" class="btn btn-primary ydbz_x" type="submit" value="搜索"/>
				<input class="btn ydbz_x" type="button" value="清空所有条件" onclick="formReset();">
			</div>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>标签</th><th>键值</th><th>类型</th><th>描述</th><th>排序</th><shiro:hasPermission name="sys:dict:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="dict">
			<tr>
				<td><a href="${ctx}/sys/dict/form?id=${dict.id}">${dict.label}</a></td>
				<td>${dict.value}</td>
				<td><a href="javascript:" onclick="$('#type').val('${dict.type}');$('#searchForm').submit();return false;">${dict.type}</a></td>
				<td>${dict.description}</td>
				<td>${dict.sort}</td>
				<shiro:hasPermission name="sys:dict:edit"><td>
    				<a href="${ctx}/sys/dict/form?id=${dict.id}">修改</a>
					<a href="${ctx}/sys/dict/delete?id=${dict.id}" onclick="return confirmx('确认要删除该字典吗？', this.href)">删除</a>
    				<a href="javascript:void(0);" onclick="returnUrl('${ctx}','${dict.description}','${dict.type}');">添加键值</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>