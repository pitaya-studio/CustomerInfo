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
			<a class="ydbz_x" href="${ctx}/order/manager/visa/warrantForm?proId=${param.proId}">押金转担保申请</a>
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
					<c:set var="applied" value="${appliedList.get(s.index)}"></c:set>
					<tr>
						<td>${review.travelerName}</td>
						<td class="tc"><span class="order-saler onshow">${saler }</span><span class="order-picker">${createBy }</span></td>
						<td class="tc">${fn:substring(review.createDate,0,10)}</td>
						<td class="tc">
							<c:if test="${not empty applied.updateDate}">
								<fmt:formatDate pattern="yyyy-MM-dd" value="${applied.updateDate}"/>
							</c:if>
						</td>
						<td>${review.priceCurrency}</td>
						<td class="tr tdred">${review.price}</td>
						<td>${review.reasonMark}</td>
						<c:choose>
							<c:when test="${applied.status eq 0}">
								<td class="invoice_back" title="${applied.denyReason}">${fns:getNextReview(applied.id) }</td>
							</c:when>
							<c:when test="${applied.status eq 1}">
								<td class="invoice_no">${fns:getNextReview(applied.id) }</td>
							</c:when>
							<c:when test="${applied.status eq 2 or applied.status eq 3}">
								<td class="invoice_yes">${fns:getNextReview(applied.id) }</td>
							</c:when>
							<c:when test="${applied.status eq 4}">
								<td class="invoice_back">${fns:getNextReview(applied.id) }</td>
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
									<a href="${ctx}/review/deposit/reviewDetail?reviewId=${review.id}" >审核详情 </a>
									<c:if test="${applied.status eq 1}">
<!-- 									<a href="${ctx}/order/manager/visa/warrantForm?proId=${param.proId}&reviewId=${applied.id}">重新申请</a> -->
										<a href="${ctx}/order/manager/visa/removeWarrantReview?reviewId=${applied.id}">取消申请</a>
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