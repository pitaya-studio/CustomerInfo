<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<title>订单-签证-借款记录</title>
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
					url: "${ctx}/visa/xxz/borrowmoney/cancelXXZBorrowAmount",
					async:false,
					cache:false,
					data: {
						reviewId:revId
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
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/visa/xxz/borrowmoney/borrowMoneyRecord4XXZactivity?orderId=${orderId}" method="post" >
	<div class="mod_nav">订单 > 签证 > 借款记录</div>
	<div class="filter_btn">
		<c:if test="${companyId eq 68 }">
		    <!-- 申请借款    原始按钮  屏蔽  -->
		    <!-- 
			<a href="${ctx}/visa/workflow/borrowmoney/applyForm?visaOrderId=${orderId}" class="btn btn-primary" target="_blank">申请借款</a>
			 -->
			<!-- 申请借款    新  借款  链接  active申请借款 
			<a href="${ctx}/visa/xxz/borrowmoney/toVisaXXZBorrowMoneyAppPage?visaOrderId=${orderId}" target="_blank">申请借款</a>
			-->
		</c:if>
		
		<a target="_blank" class="btn btn-primary" style="float: right;padding-right: 20px;margin-bottom: 10px;" href="${ctx}/visa/order/borrowMoneyRecord?orderId=${orderId}">原借款记录</a>
		
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
			<td invoice_back>${fns:getUserNameById(list.createBy)}</td>
			<!-- 获取审批状态函数 -->
			
			<!--审批状态--已驳回0, 处理中1,已通过2,已取消3 -->
			<c:choose>
				<c:when test="${list.status eq 0}">
					<td class="invoice_back" title="${list.denyReason}">已驳回</td>
				</c:when>
				<c:when test="${list.status eq 1}">
					<td class="invoice_yes" >${fns:getChineseReviewStatusByUuid(list.id)}</td>
				</c:when>
				<c:when test="${list.status eq 2}">
					<td class="invoice_no">已通过</td>
				</c:when>
				<c:when test="${list.status eq 3}">
					<td class="invoice_back">已取消</td>
				</c:when>
			</c:choose>
		
			<td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
					<dd class="">
						<p>
							<span></span>
							<a href="${ctx}/visa/hqx/borrowmoney/visaBorrowMoney4HQXReviewDetail?orderId=${list.orderId}&travelerId=${list.travellerId}&reviewId=${list.id}">详情</a>
							<c:if test="${list.status eq 1}">
							    <!-- 
								<a href="javascript:void(0)" onClick="javascript:cancelConfirm('${list.id}',this);">取消</a>
								 -->
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