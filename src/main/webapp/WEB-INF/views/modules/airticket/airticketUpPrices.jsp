<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-机票-改价列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

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
/**
 * 取消申请
 */
function cancelApply(j){
	var tarval = $(j).attr("tarval");
	var refval = $(j).attr("refval");
	jBox.confirm("是否确认取消该申请？", "来自系统的提示", function(v, h, f){
		 if (v == true){
		$.ajax({
			url:tarval,
			type:"post",
			success:function(){
				 jBox.tip("取消成功!", 'success');
				 window.location.href = refval;
			}
		});
		return true;
		 }else{
			 
		 }
	}, { id:'hahaha', showScrolling: false, buttons: { '是': true, '否': false } });
	

}

</script>
</head>
<body>
<div id="sea">
<div class="mod_nav">显示订单 > 机票 > 改价记录 </div>
				<!--右侧内容部分开始-->
				<div class="ydbz_tit orderdetails_titpr">改价记录
				<c:choose>
				<c:when test="${flag }">
<!-- 					<span class="tdorange fbold" style=" float:right;"> 该订单为待审核状态，不能申请改价！</span>   -->
				</c:when>
				<c:otherwise>
<!-- 				<a class="ydbz_x" href="${ctx}/airTicketUpProces/upAirPrices?orderId=${orderId}&productType=${productType}&flowType=${flowType}">申请改价</a> -->
				</c:otherwise>
				</c:choose>
				
				
				</div>
				<table id="contentTable" class="activitylist_bodyer_table">
					<thead>
						<tr>
						 <th width="10%">报批日期</th>
                         <th width="10%">游客/团队</th>
                         <th width="8%">款项</th>
						 <th width="8%">币种</th>
						 <th width="10%">改前金额</th>
						 <th width="10%">改后金额</th>
						 <th width="10%">改价差额</th>
						 <th width="8%">申请人</th>
						 <th width="10%">审批动态</th>
						 <th width="10%">备注</th>
						 <th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${airticketReturnList}" var="airticketReturnReview">
					<tr>
						<td class="tc">${airticketReturnReview.createDate}</td>
						<td>${fns:getTravelerNameById(airticketReturnReview.travelerid)}</td><!-- ${airticketReturnReview.travelerId} -->
						<td  class="tc">${airticketReturnReview.changedfund}</td>
						<td class="tc"><span>${airticketReturnReview.currencyname}</span></td>
						<td  class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.curtotalmoney}" /></span></td>
						<td  class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.changedtotalmoney}" /></span></td>
						<td  class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.changedprice}" /></span></td>
						<td  class="tc">${fns:getUserNameById(airticketReturnReview.createBy)}</td>
						<td class="invoice_yes">
						
						<c:choose>
						<c:when test="${airticketReturnReview.status == 4 }">
						<span class="tdred">
						</c:when>
						<c:otherwise>
						<span>
						</c:otherwise>
						</c:choose>
						<!-- ${fns:getDictLabel(airticketReturnReview.status, 'review_result_type', airticketReturnReview.status)} -->
						${fns:getNextReview(airticketReturnReview.id) }
						</span></td>
						
						<td>
						<c:choose>
						<c:when test="${not empty airticketReturnReview.remark}">${airticketReturnReview.remark}</c:when>
						<c:otherwise>
							无
						</c:otherwise>
						</c:choose>
						</td>
						<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"/></dt>
								<dd class="">
									<p>
										<span></span> 
<!-- 										<a href="${ctx}/order/manage/airticketOrderDetail?orderId=${airticketReturnReview.orderId}" onclick="">订单详情</a> -->
										<a href="${ctx}/airTicketUpProces/changePrice?id=${airticketReturnReview.id}&orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}">改价详情</a>
										<c:if test="${airticketReturnReview.status == 1 }">
										<a href="javascript:void();" refval="${ctx}/airTicketUpProces/list?orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}&active=${airticketReturnReview.active}&revid=${airticketReturnReview.id}" tarval="${ctx}/airTicketUpProces/cancelTravlerReturnRequest?orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}&active=${airticketReturnReview.active}&revid=${airticketReturnReview.id}" onclick="javascript:cancelApply(this);">取消申请</a>
										</c:if>
										<c:if test="${airticketReturnReview.status == 0 }">
										<!-- <a href="${ctx}/airTicketUpProces/applyForUpAirPrices?orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}&active=${airticketReturnReview.active}&sign=flag&revid=${airticketReturnReview.id}" onclick="">重新申请</a> -->
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
			 <div class="ydBtn">
<!-- 			 <a href="${ctx}/airticketOrderList/manage/airticketOrderList/1" class="ydbz_s">返回</a> -->
			 <a href="javascript:window.close();" class="ydbz_s">关闭</a>
			 </div>
</body>
</html>
