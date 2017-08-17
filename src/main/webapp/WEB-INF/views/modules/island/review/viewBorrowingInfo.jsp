<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>借款详情</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<!-- 静态资源 -->

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">


function closeCurWindow(){
	this.close();
}

</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<input type="hidden" value="${orderId }" id="orderId">
		<div class="mod_nav">订单&nbsp;&nbsp;>海岛游&nbsp;&nbsp;
                 > 借款记录&nbsp;&nbsp; > 借款详情</div>
		<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostAndNumInfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostInfo.jsp"%>
		<input type="hidden" name="orderUuid" id="orderUuid" value="${orderUuid }"/>

				
				<div class="ydbz_tit pl20"><span class="fl">借款记录</span></div>
				<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new">
				   <thead>
					  <tr>
						 <th width="7%">申请日期</th>
						 <th width="7%">姓名</th>
						 <th width="7%">舱位等级</th>
						 <th width="7%">游客类型</th>
						 <th width="10%">游客结算价</th>
						 <th width="10%">累计借款金额</th>
						 <th width="10%">本次借款金额</th>
						 <th width="7%">申请人</th>
						 <th width="12%">备注</th>
						 <th width="7%">审批状态</th>
					  </tr>
				   </thead>
				   <tbody>
				   <c:forEach items="${tralist}" var="borrowing">
					  <tr group="travler1">
						 <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${applyDate}"/></td>
                         <td class="tc">${borrowing.travelerName} </td>
                         <td class="tc">${fns:getDictLabel(traveler.spaceLevel,"spaceGrade_Type" , "无")}</td>
                         <td class="tc">
                         <trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${traveler.personType}"/>
                         </td>
                         <td class="tr">${fns:getIslandMoneyAmountBySerialNum(traveler.payPriceSerialNum,2)}</td>
						 <td class="tr">${fns:getBorrowPayMoneyTravelByOrderType(orderUuid,traveler.uuid,12)}</td>

						 <td class="tr">${borrowing.currencyMark}${borrowing.lendPrice}</td>
 						 <td class="tc">${fns:getUserNameById(borrowing.createBy)}</td>
 						 <td>${review.createReason}</td>
 						 <td class="tc tdgreen" title="">审核通过</td>
					  </tr>
					  </c:forEach>
				   </tbody>
				</table>
				<div class="dbaniu" >
					<a class="ydbz_s" onclick="window.close();">关闭</a>
				</div>
												
	
</body>
</html>