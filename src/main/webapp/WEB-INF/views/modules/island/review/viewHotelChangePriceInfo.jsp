<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-改价详情</title>
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
</script>
</head>
<body>
<div id="sea">
<div class="mod_nav">订单 > 酒店 > 改价详情</div>
				<!--右侧内容部分开始-->
				
				<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderHotelBaseinfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderCostAndNumInfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderCostInfo.jsp"%>
				<div class="ydbz_tit orderdetails_titpr">改价详情</div>
				<table id="contentTable" class="activitylist_bodyer_table">
					<thead>
						<tr>
						 <th width="10%">报批日期</th>
                         <th width="10%">游客/团队</th>
                         <th width="8%">款项</th>
						 <th width="8%">币种</th>
						 <th width="10%">改前金额</th>
						 <th width="10%">改后金额</th>
						 <!--  
						 <th width="10%">改价差额</th>
						 -->
						 <th width="10%">申请人</th>
						 <th width="10%">备注</th>
						 <th width="7%">审批状态</th>
						</tr>
					</thead>
					<tbody>
					<c:forEach items="${airticketReturnList}" var="airticketReturnReview">
					<tr>
						<td class="tc">${airticketReturnReview.createDate}</td>
						<td>${fns:getSysTravelerNameById(airticketReturnReview.travelerid,11)}</td><!-- ${airticketReturnReview.travelerId} -->
						<td  class="tc">${airticketReturnReview.changedfund}</td>
						<td class="tc"><span>${airticketReturnReview.currencyname}</span></td>
						<td  class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.curtotalmoney}" /></span></td>
						<td  class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.changedtotalmoney}" /></span></td>
						<!-- 
						<td  class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.changedprice}" /></span></td>
						-->
						<td  class="tc">${fns:getUserNameById(airticketReturnReview.createBy)}</td>
						<td>
						<c:choose>
						<c:when test="${not empty airticketReturnReview.remark}">${airticketReturnReview.remark}</c:when>
						<c:otherwise>
							无
						</c:otherwise>
						</c:choose>
						</td>
						<td class="tc tdgreen" title="">审核通过</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>
				
				<!--右侧内容部分结束-->
			</div>
			<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a></div>
</body>
</html>
