<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler" />
<title>服务费统计</title>
<script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
var sysCtx;
$(function () {

	launch();

	sysCtx = $("#sysCtx").val();
	$("#officeId").comboboxInquiry();
});

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/quauqServiceFeeStatistics").submit();
	return false;
}

function search(type) {
	if (type == 1) {
		$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/quauqServiceFeeStatistics").submit();
	} else {
		window.open(sysCtx + "/quauqAgent/manage/downloadAllQuauqServiceFeeStatistics" + "?" + $('#searchForm').serialize());
		$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/quauqServiceFeeStatistics").submit();
	}
}
</script>
<style type="text/css">
.fwfwjq {color:#eb0301; padding-right:5px;}
.fwfyjq{color:#009535;padding-right:5px;}
/* .activitylist_paixu,.activitylist_bodyer_right_team_co_paixu{height:auto;} */
	.ydxbd{
		overflow:hidden;
	}
</style>
</head>
<body>
<%--added for UG_V2 添加tab at 20170223 by tlw start.--%>
<content tag="three_level_menu">
	<li class="active">
		<a href="javascript:void(0)">服务费统计</a>
	</li>
</content>
<%--added for UG_V2 添加tab at 20170223 by tlw end.--%>
<div class="activitylist_bodyer_right_team_co_bgcolor">
	<form:form id="searchForm" method="post" action="${ctx}/quauqAgent/manage/quauqServiceFeeStatistics" modelAttribute="fee">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />

	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2">
			<label>供应商：</label>
			<select name="officeId" id="officeId">
				<option value="" selected="">全部</option>
				<c:forEach var="office" items="${officeList }">
					<option value="${office.id}" <c:if test="${fee.officeId == office.id}">selected="selected"</c:if>>${office.name}</option>
				</c:forEach>
			</select>
		</div>
		<div class="form_submit">
			<input class="btn btn-primary ydbz_x" type="button" onclick="search(1)" value="搜索">
			<input class="btn ydbz_x" value="导出Excel" type="button" onclick="search(2)">
		</div>
	</div>
	</form:form>
	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul class="cwxt-qbdd">
					<li><span class="summation">合计：</span></li>
					<li>订单数：<span>${total.orderCount}</span></li>
					<li>人数：<span>${total.personCount}</span></li>
					<li>服务费总计：<span>${total.serviceFeeTotalCount}</span></li>
					<li>QUAUQ服务费：<span><c:if test="${not empty total.quauqChargeTotal}">${total.quauqChargeTotal}</c:if>
									<c:if test="${empty total.quauqChargeTotal}"><c:if test="${not empty total.orderCount}">¥0.00</c:if></c:if></span></li>
					<li>渠道服务费：<span><c:if test="${not empty total.agentChargeTotal}">${total.agentChargeTotal}</c:if>
									<c:if test="${empty total.agentChargeTotal}"><c:if test="${not empty total.orderCount}">¥0.00</c:if></c:if></span></li>
					<li>抽成服务费：<span><c:if test="${not empty total.cutChargeTotal}">${total.cutChargeTotal}</c:if>
									<c:if test="${empty total.cutChargeTotal}"><c:if test="${not empty total.orderCount}">¥0.00</c:if></c:if></span></li>
					<li>已结清总额：<span>${total.serviceFeeSettled}</span></li>
					<li>未结清总额：<span>${total.serviceFeeUnsettled}</span></li>
				</ul>
			</div>
			<%-- <div class="activitylist_paixu_right">
				查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
			</div> --%>
			<div class="kong"></div>
		</div>
	</div>
	<table class="activitylist_bodyer_table mainTable" id="contentTable_quauq">
		<thead>
			<tr>
				<th width="3%">序号</th>
				<th width="8%">供应商名称</th>
				<th width="4%">订单数</th>
				<th width="4%">人数</th>
				<th width="8%">服务费总额</th>
				<th width="8%">QUAUQ服务费总额</th>
				<th width="8%">渠道服务费总额</th>
				<th width="8%">抽成服务费总额</th>
				<th width="8%">已结清总额</th>
				<th width="8%">未结清总额</th>
				<th width="5%">操作</th>
			</tr>
		</thead>
		<tbody class="orderOrGroup_group_tbody">
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
					<%--<td colspan="10" style="text-align: center;">暂无搜索结果</td>--%>
					<td colspan="11" style="text-align: center;">暂无搜索结果</td><%--bug17610 colspan改为11--%>
				</tr>
			</c:if>
       
			<c:forEach items="${page.list}" var="serviceFee" varStatus="s">
				<tr>
					<td class="tc">${s.index + 1}</td>
					<td class="tc">${serviceFee.officeName}</td>
					<td class="tc">${serviceFee.orderCount}</td>
					<td class="tc">${serviceFee.personCount}</td>
					<td class="tc"><span class="fwfyjq">${serviceFee.serviceFeeTotalCount}</span></td>
					<td class="tc"><span class="fwfyjq"><c:if test="${not empty serviceFee.quauqTotalMoney}">${serviceFee.quauqTotalMoney}</c:if>
														<c:if test="${empty serviceFee.quauqTotalMoney}">¥0.00</c:if></span></td>
					<td class="tc"><span class="fwfyjq"><c:if test="${not empty serviceFee.agentTotalMoney}">${serviceFee.agentTotalMoney}</c:if>
														<c:if test="${empty serviceFee.agentTotalMoney}">¥0.00</c:if></span></td>
					<td class="tc"><span class="fwfyjq"><c:if test="${not empty serviceFee.cutTotalMoney}">${serviceFee.cutTotalMoney}</c:if>
														<c:if test="${empty serviceFee.cutTotalMoney}">¥0.00</c:if></span></td>
					<td class="tc"><span class="fwfyjq">${serviceFee.serviceFeeSettled}</span></td>
					<td class="tc"><span class="fwfwjq">${serviceFee.serviceFeeUnsettled}</span></td>
					<td class="tc">
						<shiro:hasPermission name="transaction:details:statistics">
							<a href="${ctx}/quauqAgent/manage/tradeDetail?officeId=${serviceFee.officeId}" target="_blank">查看明细</a>
						</shiro:hasPermission>
					</td>
				</tr> 
			</c:forEach>
		</tbody>
	</table>
</div>
	<div class="pagination clearFix">
		${page}
		<div style="clear:both;"></div>
	</div>
</body>
</html>

