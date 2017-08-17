<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-签证-改价列表</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
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
<div class="mod_nav">订单 > 签证> 改价记录</div>
				<!--右侧内容部分开始-->
				<div class="ydbz_tit orderdetails_titpr">改价记录
<%-- 				<c:choose> --%>
<%-- 				<c:when test="${flag }"> --%>
<!-- 					<span class="tdorange fbold" style=" float:right;"> 该订单为待审核状态，不能申请改价！</span>   -->
<%-- 				</c:when> --%>
<%-- 				<c:otherwise> --%>
<%-- 				<a class="ydbz_x" href="${ctx}/visaUpProces/upVisaPrices?orderId=${orderId}&productType=${productType}&flowType=${flowType}">申请改价</a> --%>
<%-- 				</c:otherwise> --%>
<%-- 				</c:choose> --%>
				</div>
				<table id="contentTable" class="activitylist_bodyer_table">
					<thead>
						<tr>
						 <th width="10%">申请改价日期</th>
                         <th width="10%">币种</th>
						 <th width="10%">改前金额</th>
						 <th width="10%">改后金额</th>
						 <th width="10%">改价差额</th>
						 <th width="10%">申请人</th>
						 <th width="10%">审批动态</th>
						 <th width="18%">备注</th>
						 <th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${airticketReturnList}" var="airticketReturnReview">
					<tr>
						<td class="tc">${airticketReturnReview.createDate}</td>
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
<!-- 										<a href="${ctx}/visa/order/goUpdateVisaOrderForSales?visaOrderId=${airticketReturnReview.orderId}&mainOrderCode=${airticketReturnReview.mainOrderCode}&details=1" onclick="">订单详情</a> -->
										<a href="${ctx}/visaUpProces/changePrice/${airticketReturnReview.orderId}/${airticketReturnReview.productType}/${airticketReturnReview.flowType}/${airticketReturnReview.id}?mainOrderCode=${airticketReturnReview.mainOrderCode}&details=1">改价详情</a>
										<c:if test="${airticketReturnReview.status == 1 }">
										<a href="javascript:void();" refval="${ctx}/visaUpProces/list?orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}&active=${airticketReturnReview.active}&revid=${airticketReturnReview.id}" tarval="${ctx}/visaUpProces/cancelTravlerReturnRequest?orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}&active=${airticketReturnReview.active}&revid=${airticketReturnReview.id}" onclick="javascript:cancelApply(this);">取消申请</a>
										</c:if>
										<c:if test="${airticketReturnReview.status == 0 }">
										<!-- <a href="${ctx}/visaUpProces/applyForUpVisaPrices?orderId=${airticketReturnReview.orderId}&productType=${airticketReturnReview.productType}&flowType=${airticketReturnReview.flowType}&travelerId=${airticketReturnReview.travelerId}&active=${airticketReturnReview.active}&sign=flag&revid=${airticketReturnReview.id}" onclick="">重新申请</a> -->
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
