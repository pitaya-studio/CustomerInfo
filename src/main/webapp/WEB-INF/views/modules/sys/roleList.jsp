]<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
			$(".selectinput" ).comboboxInquiry();
			$(".custom-combobox-input").css("width","160px");
			
			// 表格排序
			var orderBy = $("#orderBy").val().split(" ");
			$("#contentTable th.sort").each(function(){
				if ($(this).hasClass(orderBy[0])){
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
					$(this).html($(this).html()+" <i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				}
			});
			$("#contentTable th.sort").click(function(){
				var order = $(this).attr("class").split(" ");
				var sort = $("#orderBy").val().split(" ");
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort"){order = order[i+1]; break;}
				}
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
					$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
				}else{
					$("#orderBy").val(order+" ASC");
				}
				page();
			});
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/sys/role/");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
    <content tag="three_level_menu">
		<li class="active"><a href="${ctx}/sys/role/">角色列表</a></li>
		<shiro:hasPermission name="sys:role:edit"><li><a href="${ctx}/sys/role/form">角色添加</a></li></shiro:hasPermission>
	</content>
	<form:form id="searchForm" modelAttribute="role" action="${ctx}/sys/role/" method="post" class="form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
	<div class="activitylist_bodyer_right_team_co">
        <c:if test="${fns:getUser().id==1}">
			<div class="activitylist_bodyer_right_team_co2 wpr20">
	            <label class="activitylist_team_co2_text">归属公司：</label>
				<form:select path="companyId" class="selectinput">
					<form:option value="" label="请选择"/>
						<c:forEach items="${fns:getOfficeList(false,'','')}" var="company" varStatus="idxStatus">
							<c:if test="${company.id!=1}">
								<form:option value="${company.id}" label="${company.name}"/>
							</c:if>
						</c:forEach>
				</form:select>
			</div>

		</c:if>
		<div class="activitylist_bodyer_right_team_co2">
			<label class="activitylist_team_co2_text">角色名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/>

		</div>
		<div class="form_submit">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="搜索" onClick="return page();"/>
		</div>
	</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered">
		<tr><th>角色名称</th><th>所属批发商</th><shiro:hasPermission name="sys:role:edit"><th>操作</th></shiro:hasPermission></tr>
		<c:forEach items="${page.list}" var="role">
			<tr>
				<td width="30%"><a href="form?id=${role.id}">${role.name}</a></td>
				<td width="50%">
					<c:if test="${empty role.companyId }">系统默认</c:if>
					<c:if test="${not empty role.companyId and role.companyId ne '0'}">${offMap[role.id] }</c:if>
					<c:if test="${role.companyId eq '0'}">暂不使用</c:if>
				</td>
				<shiro:hasPermission name="sys:role:edit">
					<td width="20%">
						<a href="${ctx}/sys/role/form?id=${role.id}">修改</a>
						<a href="${ctx}/sys/role/delete?id=${role.id}" onclick="return confirmx('确认要删除该角色吗？', this.href)">删除</a>
					</td>
				</shiro:hasPermission>	
			</tr>
		</c:forEach>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>