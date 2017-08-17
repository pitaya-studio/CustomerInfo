<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>订单-签证-借款记录</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
	yd_dt_Handler();
});

function cancelConfirm(revId,_this){
	var $this = $(_this);
		$.jBox.confirm("确定取消吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/visa/workflow/borrowmoney/cancelVisaBorrowMOney",
					async:false,
					cache:false,
					data: {
						revId:revId
					},
					success: function(data){
						if (data.success) {
							top.$.jBox.tip('取消成功','success');
							$this.remove();
							$("#searchForm").submit();
						} else {
							top.$.jBox.tip('取消失败','fail');
						}
					}
				});
			}else if (v == 'cancel'){
				
			}
	});
}
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/visa/order/borrowMoneyRecord?orderId=${orderId}" method="post" >
	<div class="mod_nav">订单 > 签证 > 借款记录</div>
	<div class="filter_btn">
		<c:if test="${companyId eq 71 }">
			<a href="${ctx}/visa/workflow/borrowmoney/applyForm?visaOrderId=${orderId}" class="btn btn-primary" target="_blank">申请借款</a>
		</c:if>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="10%">报批日期</th>
				<th width="10%">游客/团队</th>
				<th width="10%">款项</th>
				<th width="10%">币种</th>
				<th width="12%">借款金额</th>
				<th width="12%">申请人</th>
				<th width="19%">状态</th>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${fn:length(borrowMoneyRecordList) <= 0 }">
			<tr class="toptr" >
				<td colspan="15" style="text-align: center;">暂无申请记录</td>
			</tr>
		</c:if>
		<c:forEach items="${borrowMoneyRecordList }" var="list">
		<tr>
			<td class="tc">${list.createDate }</td>
			<td class="tc">${list.travelerName }</td>
			<td class="tc">签证借款</td>
			<td class="tc">${list.currencyName }</td>
			<td class="tr">￥${list.borrowAmount }</td>
			<td>${fns:getUserNameById(list.createBy)}</td>
			<c:choose>
				<c:when test="${list.status eq 0}">
					<td class="invoice_back" title="${list.denyReason}">${fns:getNextReview(list.id) }</td>
				</c:when>
				<c:when test="${list.status eq 2}">
					<td class="invoice_yes">${fns:getNextReview(list.id) }</td>
				</c:when>
				<c:when test="${list.status eq 3 or list.status eq 1}">
					<td class="invoice_no">${fns:getNextReview(list.id) }</td>
				</c:when>
				<c:when test="${list.status eq 4}">
					<td class="invoice_back">${fns:getNextReview(list.id) }</td>
				</c:when>
			</c:choose>
			<td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
					<dd class="">
						<p>
							<span></span>
							<c:choose>
								<c:when test="${companyId eq 71 }">
									<a href="${ctx}/visa/workflow/borrowmoney/visaBorrowMoney4XXZReviewDetail?orderId=${list.orderId}&travelerId=${list.travelerId}&flag=1&revid=${list.id}&flowType=${list.flowType}&nowLevel=">借款详情</a>
								</c:when>
								<c:otherwise>
									<a href="${ctx}/visa/workflow/borrowmoney/visaBorrowMoneyReviewDetail?orderId=${list.orderId}&travelerId=${list.travelerId}&flag=1&revid=${list.id}&flowType=${list.flowType}&nowLevel=">借款详情</a>
								</c:otherwise>
							</c:choose>
							<c:if test="${list.status eq 1}">
								<a href="javascript:void(0)" onClick="javascript:cancelConfirm('${list.id}',this);">取消</a>
							</c:if>
						</p>
					</dd>
				</dl>
			</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
	</form:form>
</body>
</html>