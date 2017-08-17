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
<div class="mod_nav">订单 > <c:set var="productType" value="${productType}"></c:set><c:if test="${productType==1}">单团</c:if><c:if test="${productType==2}">散拼</c:if><c:if test="${productType==3}">游学</c:if><c:if test="${productType==4}">大客户</c:if><c:if test="${productType==5}">自由行</c:if> > 改价详情</div>
				<!--右侧内容部分开始-->
				<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
								
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
						 <th width="10%">改价差额</th>
						 <th width="10%">申请人</th>
						 <th width="10%">备注</th>
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
						<td>
						<c:choose>
						<c:when test="${not empty airticketReturnReview.remark}">${airticketReturnReview.remark}</c:when>
						<c:otherwise>
							无
						</c:otherwise>
						</c:choose>
						</td>
					</tr>
					</c:forEach>
					</tbody>
				</table>

				<div class="allzj tr f18">
					当前金额：<span id="totalBefore"><font class="f14" flag="bz" value="">						
						<c:if test="${not empty airticketReturnList }">
							${fns:getCurrencyInfo(airticketReturnList[0].currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnList[0].curtotalmoney}" /></span>
						</c:if>
					</span><br/>					
                    <div class="all-money">改后总额：<span id="totalAfter">
                    	<c:if test="${not empty airticketReturnList }">
                    		${fns:getCurrencyInfo(airticketReturnList[0].currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnList[0].changedtotalmoney}" /></span>
                    	</c:if>
                    </span></div>
				</div>

				<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>				
				<!--右侧内容部分结束-->
			</div>
			 <div class="ydBtn"><a class="ydbz_s" href="javascript:history.go(-1);">返回</a></div>
</body>
</html>
