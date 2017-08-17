<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-机票-退票</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
});
</script>
</head>
<body>
<div id="sea">
				<!--右侧内容部分开始-->
				<div class="ydbz_tit orderdetails_titpr">退票记录</div>
				<table id="contentTable" class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="10%">游客</th>
							<th width="15%">下单时间</th>
							<th width="15%">报批日期</th>
							<th width="10%">应收金额</th>
							<th width="30%">退票原因</th>
							<th width="10%">状态</th>
							<th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${airticketReturnList}" var="airticketReturnReview">
					<tr>
						<td>${fns:getTravelerNameById(airticketReturnReview.travelerId)}</td><!-- ${airticketReturnReview.travelerId} -->
						<td class="tc"><fmt:formatDate value="${airticketReturnReview.orderCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td class="tc"><fmt:formatDate value="${airticketReturnReview.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td class="tr"><span class="tdred">${airticketReturnReview.payPrice}</span></td>
						<td>${airticketReturnReview.createReason}</td>
						<td class="invoice_yes">${fns:getNextReview(airticketReturnReview.id) }</td>
						<!--${fns:getDictLabel(airticketReturnReview.status, 'review_result_type', airticketReturnReview.status)}-->
						<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"/></dt>
								<dd class="">
									<p>
										<span></span> 
										<a href="${ctx}/order/manage/airticketOrderDetail?orderId=${airticketReturnReview.orderId}" onclick="" target="_blank">订单详情</a>
										<c:if test="${airticketReturnReview.status == 1 }">
										<a href="${ctx}/airticketreturn/cancelTravlerReturnRequest?orderId=${airticketReturnReview.orderId}&revid=${airticketReturnReview.id}&orderType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}" onclick="">取消申请</a>
										</c:if>
									</p>
								</dd>
							</dl> 
						</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
				<!--右侧内容部分结束-->
			</div>
</body>
</html>
