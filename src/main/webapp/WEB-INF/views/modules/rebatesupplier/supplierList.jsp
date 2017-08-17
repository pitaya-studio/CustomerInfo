<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>供应商列表</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">

		.rebate_searchCondition {
			min-width: 300px;
			height: 28px;
			float: left;
			margin-left: 2%;
		}
		#contentTable td{
			height: auto;
		}
	</style>
	<script type="text/javascript">

		/**
		 * 翻页
		 * n 为页数
		 * s 为条数
		 */
		function page(n, s) {
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
			return false;
		}
	</script>
</head>
<body>
    <content tag="three_level_menu">
		<li class="active"><a href="javascript:void(0)">返佣供应商列表</a></li>
		<%--<shiro:hasPermission name="">--%><li><a href="${ctx}/rebatesupplier/manager/firstForm">返佣供应商添加</a></li><%--</shiro:hasPermission>--%>
	</content>
	<tags:message content="${message}"/>
	<%--<form id="searchForm" action="" method="post">
		<div class="activitylist_bodyer_right_team_co">
			<div class="ydxbd" style="display: block;">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="searchLabel" id="searchProductType">供应商名称：</div>
					<input value="${name}" class="inputTxt inputTxtlong" name="name" id="" >
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="searchLabel" id="searchProcessType">跟进计调人员：</div>
					<select name="operatorId">
						<option value="">全部</option>
						<c:forEach var="operator" items="${operators}">
							<option value="${operator.key}" <c:if test="${ (not empty operatorId) and (operator.key eq operatorId) }">selected</c:if> >${operator.value}</option>
						</c:forEach>
					</select>
				</div>
				<div class="form_submit">
					<input id="btn_search" value="搜索" class="btn btn-primary ydbz_x" type="submit">
				</div>
			</div>
			<div class="kong"></div>
		</div>
	</form>--%>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
	<form id="searchForm" action="" method="post">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label >供应商名称：</label><input style="width:180px" class=" inquiry_left_text" value="${name}" name="name" type="text"/>
			</div>
			<div class="activitylist_bodyer_right_team_co2 pr">
				<label class="activitylist_team_co2_text">跟进计调人员：</label>
				<div class="selectStyle">
					<select  name="operatorId">
						<option value="">全部</option>
						<c:forEach var="operator" items="${operators}">
							<option value="${operator.key}" <c:if test="${ (not empty operatorId) and (operator.key eq operatorId) }">selected</c:if> >${operator.value}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="form_submit">
				<input class="btn btn-primary" value="搜索" type="submit">
			</div>
		</div>
	</form>
		<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
<%--	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_right">
				查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
			</div>
			<div class="kong"></div>
		</div>
	</div>--%>
	<table id="contentTable" class="table activitylist_bodyer_table mainTable">
		<thead>
		<tr><th class="tc" width="5%">序号</th><th class="tc" width="20%">供应商名称</th><th class="tc" width="15%">供应商品牌</th><th class="tc" width="10%">负责人</th><th class="tc" width="10%">跟进计调人员</th><th class="tc" width="30%">描述</th><%--<shiro:hasPermission name="sys:office:edit">--%><th class="tc" width="10%">操作</th><%--</shiro:hasPermission>--%></tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="supplier" varStatus="status">
			<tr id="${supplier.id}">
			    <td class="tc">${status.index+1}</td>
				<td class="tl"><a href="${ctx}/rebatesupplier/manager/firstForm?id=${supplier.id}" target="_blank">${supplier.name}</a></td>
				<td class="tc">${supplier.brand}</td>
				<td class="tc">${supplier.personInCharge}</td>
				<td class="tc">${supplier.operatorName}</td>
				<td class="tl">${supplier.description}</td>
				<%--<shiro:hasPermission name="">--%>
				<td class="tc">
					<a href="${ctx}/rebatesupplier/manager/firstForm?id=${supplier.id}">修改</a>
					<a href="${ctx}/rebatesupplier/manager/delete/${supplier.id}" onclick="return confirmx('确定要删除数据？', this.href)">删除</a>
				</td>
				<%--</shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination clearFix">
		${page}
		<div style="clear:both;"></div>
	</div>
	</div>
</body>
</html>