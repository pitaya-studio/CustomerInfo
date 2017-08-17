<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-退款详情</title>
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
	<div class="mod_nav">订单 > 机票  > 退款详情</div>
	
	<!--右侧内容部分开始-->
	<div class="orderdetails_tit">
		<span></span>订单信息
		<input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
	</div>
	<div class="orderdetails1">
		<table border="0" width="90%" style="margin-left:0;">
			<tbody>
				<tr>
					<td class="mod_details2_d1">下单人：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
					<td class="mod_details2_d1">销售：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.salerName }</td>
					<td class="mod_details2_d1">下单时间：</td>
					<td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td class="mod_details2_d1">操作人：</td>
					<td class="mod_details2_d2">${fns:getUserNameById(orderDetailInfoMap.createBy)}</td>
				</tr>
				<tr>
					<td class="mod_details2_d1">订单编号：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.orderNo }</td>
					<td class="mod_details2_d1">订单团号：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.orderGroupCode }</td>
					<td class="mod_details2_d1">团队类型：</td>
					<td class="mod_details2_d2">
						<c:choose>
							<c:when test="${orderDetailInfoMap.type == 1 }">单办</c:when>
							<c:when test="${orderDetailInfoMap.type == 2 }">参团</c:when>
						</c:choose>
					</td>
				</tr>
				<c:if test="${orderDetailInfoMap.type == 2 }">
				<tr>
					<td class="mod_details2_d1">参团订单编号：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td>
					<td class="mod_details2_d1">参团团号：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td>
				</tr>
				</c:if>
			</tbody>
		</table>
	</div>
				
	<div class="ydbz_tit orderdetails_titpr">退款详情</div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="10%">游客</th>
				<th width="10%">款项</th>
				<th width="10%">报批日期</th>
				<th width="10%">币种</th>
				<th width="10%">应收金额</th>
				<th width="10%">退款金额</th>
				<th width="20%">备注</th>
			</tr>
		</thead>
		<tbody>
	     <c:forEach items="${viewMap }" var="map">
	         <c:forEach items="${map.value}" varStatus="s1" var="bean">
		      <tr>
			    <c:if test="${s1.first}">
			        <td class="tc" rowspan="${fn:length(map.value)}">${bean.travelerName }</td>
			    </c:if>  
		        <td class="tc">${bean.refundName }</td>
				<td class="tc"><fmt:formatDate value="${bean.applyDate }" pattern="yyyy-MM-dd"/></td>
				<td class="tc">${bean.currencyName }</td>
				<td class="tc"><span class="tdgreen">${bean.payPrice }</span></td>
				<td class="tc">${bean.currencyMark }<span class="tdred"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${bean.refundPrice }" /></span></td>
				<td class="tc">${bean.remark }</td>
				</tr>
		   </c:forEach>
	     </c:forEach>
		</tbody>
	</table>
				
        		   <%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
	<!--右侧内容部分结束-->
	</div>
	<div class="ydBtn"><a class="ydbz_s" href="javascript:history.go(-1);">返回</a></div>
	
</body>
</html>
