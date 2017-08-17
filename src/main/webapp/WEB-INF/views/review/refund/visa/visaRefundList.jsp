<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证退款申请列表</title>
<meta name="decorator" content="wholesaler" />
    
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
});
</script>

</head>
<body>
		<div class="mod_nav">订单 > 签证 > 签证退款申请列表</div>
		<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
		<div class="ydbz_tit orderdetails_titpr">
			签证退款申请列表
			<a class="ydbz_x" href="${ctx}/newRefund/visa/refundForm?proId=${param.proId}&type=1">申请退款</a>
		</div>
		
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="10%">游客/团队</th>
					<th width="10%">款项</th>
					<th width="10%">报批日期</th>
					<th width="10%">币种</th>
					<th width="10%">应收金额</th>
					<th width="10%">退款金额</th>
					<th width="20%">备注</th>
					<th width="10%">状态</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${reviewDetailList}" var="review" varStatus="s">
<%-- 					<c:set var="applied" value="${appliedList.get(s.index)}"></c:set> --%>
					<tr>
						<td>${review.travelerName}</td>
						<td>${review.refundName}</td>
						<td class="tc">
							<c:forEach items="${appliedList}" var="applied" >
								<c:if test="${applied.id  eq  review.id}">
									<c:if test="${not empty applied.updateDate}">
										<fmt:formatDate pattern="yyyy-MM-dd" value="${applied.updateDate}"/>
									</c:if>
								</c:if>
							</c:forEach>
						</td>
						<td>${review.currencyName}</td>
						<td class="tr">
							<span class="tdgreen">${review.payPrice}</span>
						</td>
						<td class="tr">
							<span class="tdred">${review.refundPrice}</span>
						</td>
						<td>${review.remark}</td>
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${applied.status eq 0}"> --%>
<%-- 								<td class="invoice_back" title="${applied.denyReason}">${fns:getNextReview(applied.id) }</td> --%>
<%-- 							</c:when> --%>
<%-- 							<c:when test="${applied.status eq 2}"> --%>
<%-- 								<td class="invoice_yes">${fns:getNextReview(applied.id) }</td> --%>
<%-- 							</c:when> --%>
<%-- 							<c:when test="${applied.status eq 3 or applied.status eq 1}"> --%>
<%-- 								<td class="invoice_no">${fns:getNextReview(applied.id) }</td> --%>
<%-- 							</c:when> --%>
<%-- 							<c:when test="${applied.status eq 4}"> --%>
<%-- 								<td class="invoice_back">${fns:getNextReview(applied.id) }</td> --%>
<%-- 							</c:when> --%>
<%-- 						</c:choose> --%>
			<!--审批状态--已驳回0, 处理中1,已通过2,已取消3 -->
			<c:forEach items="${appliedList}" var="applied" >
				<c:if test="${applied.id  eq  review.id}">
					<c:choose>
						<c:when test="${applied.status eq 0}">
							<td class="invoice_back" title="${applied.denyReason}">已驳回</td>
						</c:when>
						<c:when test="${applied.status eq 1}">
							<td class="invoice_yes">${fns:getChineseReviewStatusByUuid(applied.id)}</td>
						</c:when>
						<c:when test="${applied.status eq 2}">
							<td class="invoice_no">已通过</td>
						</c:when>
						<c:when test="${applied.status eq 3}">
							<td class="invoice_back">已取消</td>
						</c:when>
					</c:choose>
				</c:if>
			</c:forEach>

						<td class="p0">
						<dl class="handle">
							<dt>
								<img src="${ctxStatic}/images/handle_cz.png" title="操作">
							</dt>
							<dd class="">
								<p>
								<span></span>
									<a href="${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId=${param.proId}&mainOrderCode=&details=1">订单详情</a>
									<a href="${ctx}/newRefund/visa/refundForm?proId=${param.proId}&type=${review.id}">审批详情</a>
									<c:forEach items="${appliedList}" var="applied" >
										<c:if test="${applied.id  eq  review.id}">
											<c:choose>
												<c:when test="${applied.status eq 1}">
													<c:if test="${applied.createBy eq loginUser }">
														<a href="${ctx}/newRefund/visa/removeRefundReview?reviewId=${applied.id}&visaOrderId=${param.proId}">取消申请</a>
													</c:if>
												</c:when>
											</c:choose>
										</c:if>
									</c:forEach>
								</p>
							</dd>
						</dl>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		</div>
</body>
</html>