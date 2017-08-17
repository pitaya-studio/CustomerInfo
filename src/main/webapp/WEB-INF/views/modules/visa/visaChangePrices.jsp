<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-改价详情</title>
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
</script>
</head>
<body>
	<div id="sea">
		<div class="mod_nav">订单 > 签证  > 改价详情</div>
		<!--右侧内容部分开始-->
		<div class="ydbz_tit">订单详情</div>
		<ul class="ydbz_info ydbz_infoli25">
			<li>
				<span>销售：</span>${visaOrder.salerName }
			</li>
			<li><span>下单时间：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate }"/></li>
			<li><span>团队类型：</span>
				<c:if test="${productOrder==null }">单办签</c:if>
				<c:if test="${productOrder!=null }">
					<c:if test="${productOrder.orderStatus==1 }">单团</c:if>
					<c:if test="${productOrder.orderStatus==2 }">散拼</c:if>
					<c:if test="${productOrder.orderStatus==3 }">游学</c:if>
					<c:if test="${productOrder.orderStatus==4 }">大客户</c:if>
					<c:if test="${productOrder.orderStatus==5 }">自由行</c:if>
					<c:if test="${productOrder.orderStatus==6 }">签证</c:if>
					<c:if test="${productOrder.orderStatus==7 }">机票</c:if>
				</c:if>
			</li>
			<li><span>收客人：</span>${visaOrder.createBy.name }</li>
			<li><span>订单编号：</span>${visaOrder.orderNo }</li>
			<li><span>订单团号：</span>${visaOrder.groupCode }</li>
			<c:if test="${productOrder!=null }">
			<li><span>参团订单编号：</span>${visaOrder.activityCode }</li>
			<li><span>参团团号：</span>${activityGroup.groupCode }</li>
			</c:if>
			<li><span>订单总额：</span>${totalMoney }</li>
			<li><span>订单状态：</span>
				<c:if test="${visaOrder.payStatus==1 }">未收款</c:if>
				<c:if test="${visaOrder.payStatus==3 }">预定</c:if>
				<c:if test="${visaOrder.payStatus==5 }">已收款</c:if>
				<c:if test="${visaOrder.payStatus==99 }">已取消</c:if>
			</li>
			<li><span>操作人：</span>${visaProduct.createBy.name }</li>
			<li><span>办签人数：</span>${visaOrder.travelNum }人</li>
			<li>
				<span>下单人：</span>${visaOrder.createBy.name }
			</li>
		</ul>
		
		<div class="ydbz_tit orderdetails_titpr">改价详情</div>
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="12%">申请改价日期</th>
                    <th width="12%">币种</th>
					<th width="12%">改前金额</th>
					<th width="12%">改后金额</th>
					<th width="12%">改价差额</th>
					<th width="12%">申请人</th>
					<th width="26%">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${airticketReturnList}" var="airticketReturnReview">
				<tr>
					<td class="tc">${airticketReturnReview.createDate}</td>
					<td class="tc"><span>${airticketReturnReview.currencyname}</span></td>
					<td class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.curtotalmoney}" /></span></td>
					<td class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.changedtotalmoney}" /></span></td>
					<td class="tc">${fns:getCurrencyInfo(airticketReturnReview.currencyid,0,'mark')}<span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${airticketReturnReview.changedprice}" /></span></td>
					<td class="tc">${fns:getUserNameById(airticketReturnReview.createBy)}</td>
					<td class="tc">
						<c:choose>
							<c:when test="${not empty airticketReturnReview.remark}">${airticketReturnReview.remark}</c:when>
							<c:otherwise>无</c:otherwise>
						</c:choose>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
		
	    <%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
	</div>
	<!--右侧内容部分结束-->
	<div class="ydBtn"><a class="ydbz_s" href="javascript:history.go(-1);">返回</a></div>
</body>
</html>
