<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>退签证押金记录</title>
<meta name="decorator" content="wholesaler" />
    
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
});
</script>

</head>
<body>
		<div class="mod_nav">订单 > 签证 > 退签证押金记录</div>
		<div class="ydbz_tit orderdetails_titpr">
			退签证押金记录
			<a class="ydbz_x" href="${ctx}/order/manager/visaDeposit/refundForm?proId=${param.proId}">申请退押金</a>
		</div>
		
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="10%">游客</th>
					<th width="10%">币种</th>
					<th width="10%">押金金额</th>
					<th width="10%">已达账押金</th>
					<th width="10%">申请金额</th>
					<th width="20%">原因备注</th>
					<th width="10%">审核状态</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${reviewDetailList}" var="review" varStatus="s">
					<c:set var="applied" value="${appliedList.get(s.index)}"></c:set>
					<tr>
						<td>${review.travelerName}</td>
						<td>
							<c:if test="${not empty review.depositPriceCurrency}">
								${fns:getCurrencyInfo(review.depositPriceCurrency, 0, 'name') }
							</c:if>
						</td>
						<td class="tr tdred">${review.depositPrice}</td>
						<td>${review.payPrice}</td>
						<td>${review.refundPrice}</td>
						<td>${review.remark}</td>
						<c:choose>
							<c:when test="${applied.status eq 0}">
								<td class="invoice_back" title="${applied.denyReason}">${fns:getNextReview(review.id) }</td>
							</c:when>
							<c:when test="${applied.status eq 2}">
								<td class="invoice_yes">${fns:getNextReview(review.id) }</td>
							</c:when>
							<c:when test="${applied.status eq 3 or applied.status eq 1}">
								<td class="invoice_no">${fns:getNextReview(review.id) }</td>
							</c:when>
							<c:when test="${applied.status eq 4}">
								<td class="invoice_back">${fns:getNextReview(review.id) }</td>
							</c:when>
						</c:choose>
						<td class="p0">
						<dl class="handle">
							<dt>
								<img src="${ctxStatic}/images/handle_cz.png" title="操作">
							</dt>
							<dd class="">
								<p>
								<span></span>
									<a href="${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId=${param.proId}&mainOrderCode=&details=1" target="_blank">订单详情 </a>
										<a href="${ctx}/deposite/depositeRefundReviewDetail?revid=${review.id}&orderid=${param.proId}" >审核详情 </a>
									<c:choose>
										<c:when test="${applied.status eq 1}">
<%-- 											<a href="${ctx}/order/manager/visaDeposit/refundForm?proId=${param.proId}&reviewId=${applied.id}">重新申请</a> --%>
											<a href="${ctx}/order/manager/visaDeposit/removeRefundReview?reviewId=${applied.id}">取消申请</a>
										</c:when>
<%-- 										<c:when test="${applied.status eq 0}"> --%>
<%-- 											<a href="${ctx}/order/manager/visaDeposit/refundForm?proId=${param.proId}&reviewId=${applied.id}">重新申请</a> --%>
<%-- 										</c:when> --%>
									</c:choose>
								</p>
							</dd>
						</dl>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
</body>
</html>