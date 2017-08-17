<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>押金转担保申请列表</title>
<meta name="decorator" content="wholesaler" />
    
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
	//产品销售和下单人切换
	switchSalerAndPicker();
});
</script>

</head>
<body>
				<div class="mod_nav">订单 > 签证 > 押金转担保记录</div>
						<div class="ydbz_tit orderdetails_titpr">
			押金转担保记录
			<a class="ydbz_x" href="${ctx}/order/manager/visaNew/warrantFormNew?orderId=${orderId}">押金转担保申请NEW</a>
		</div>
		
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="10%">游客</th>
					<th width="10%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
					<th width="10%">下单时间</th>
					<th width="10%">报批日期</th>
					<th width="10%">币种</th>
					<th width="15%">押金金额</th>
					<th width="15%">原因备注</th>
					<th width="15%">审核状态</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${reviewDetailList}" var="review" varStatus="s">
					<tr>
						<td>${review.travellerName}</td>
						<td class="tc"><span class="order-saler onshow">${review.salerName }</span><span class="order-picker">${review.orderCreatorName }</span></td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${review.createDate}"/></td>
						<td class="tc">
							<fmt:formatDate pattern="yyyy-MM-dd" value="${review.createDate}"/>
						</td>
						<td>${review.extend4}</td>
						<td>${review.extend3}</td>
						<td>${review.remark}</td>
						
						<!--审批状态--已驳回0, 处理中1,已通过2,已取消3 -->
						<c:choose>
							<c:when test="${review.status eq 0}">
								<td class="invoice_back" title="${list.denyReason}">已驳回</td>
							</c:when>
							<c:when test="${review.status eq 1}">
								<td class="invoice_yes">处理中</td>
							</c:when>
							<c:when test="${review.status eq 2}">
								<td class="invoice_no">已通过</td>
							</c:when>
							<c:when test="${review.status eq 3}">
								<td class="invoice_back">已取消</td>
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
									<a href="${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId=${review.orderId}&mainOrderCode=&details=1" target="_blank">订单详情 </a>
									<a href="${ctx}/order/manager/visaNew/getReviewDetailByReviewId?orderId=${review.orderId}&reviewId=${review.id}&travellerId=${review.travellerId}" >审核详情 </a>
									<c:if test="${review.status eq 1}">
										<a href="${ctx}/order/manager/visaNew/removeWarrantReview?orderId=${review.orderId}&reviewId=${review.id}">取消申请</a>
									</c:if>
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