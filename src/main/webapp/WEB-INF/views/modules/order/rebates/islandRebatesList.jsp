<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<title>返佣记录</title>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/rebates/islandRebatesList.js"
	type="text/javascript"></script>
<script>
	$(function() {
		operateHandler();
	});
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="mod_nav">订单 > 海岛游 > 返佣记录</div>
	<div class="filter_btn">
		<a class="btn btn-primary"
			href="javascript:rebates.addRebates('${orderId}','${orderType}','${orderUuid }');">申请返佣</a>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table_new">
		<thead>
			<tr>
				<th width="7%">申请日期</th>
				<th width="7%">姓名</th>
				<th width="7%">舱位等级</th>
				<th width="7%">游客类型</th>
				<th width="7%">款项</th>
				<th width="9%">应收款金额</th>
				<th width="9%">累计返佣金额</th>
				<th width="7%">币种</th>
				<th width="9%">本次返佣金额</th>
				<th width="12%">备注</th>
				<th width="6%">审批状态</th>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
			<!-- 无查询结果 -->
			<c:if test="${fn:length(rebatesList) <= 0 }">
				<tr class="toptr">
					<td colspan="12" style="text-align: center;">暂无返佣记录</td>
				</tr>
			</c:if>
			<c:forEach items="${rebatesList}" var="rebates">
				<tr>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd"
							value="${rebates.createDate}" /></td>
					<td class="tc"><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if>
						<c:if test="${empty rebates.traveler}">团队</c:if></td>
					<td class="tc"> ${fns:getDictLabel(rebates.traveler.spaceLevel,"spaceGrade_Type" , "无")}</td>
					<td class="tc"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${rebates.traveler.personType}"/></td>
					<td>${rebates.costname}</td>
					<td class="tr">${fns:getIslandMoneyAmountBySerialNum(rebates.totalMoney,2)}</td>
					<td class="tr">${rebates.oldRebates}</td>
					<td>${rebates.currency.currencyName}</td>

					<td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
					<td>${rebates.remark}</td>
					<td class="tc tdgreen" title="">审核通过</td>

					<td class="tc">
						<a href="javascript:rebates.showDetail('${rebates.id}','${orderUuid }');">查看详情</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="ydBtn">
		<a class="ydbz_s" onclick="window.close();">关闭</a>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>