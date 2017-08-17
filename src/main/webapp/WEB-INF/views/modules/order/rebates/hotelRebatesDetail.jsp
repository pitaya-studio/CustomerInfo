<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>返佣详情</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/rebates/hotelRebatesList.js" type="text/javascript"></script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
	    <page:param name="desc">返佣详情</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
    <div class="mod_nav">订单 > 酒店 > 返佣记录 > 返佣详情</div>
	<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderHotelBaseinfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderCostAndNumInfo.jsp"%>
		<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderCostInfo.jsp"%>
	
	<div class="ydbz_tit pl20"><span class="fl">返佣历史</span></div>
				<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom:40px;">
				  <thead>
				    <tr>
				      <th width="8%">申请日期</th>
				      <th width="8%">姓名</th>
				      <th width="8%">游客类型</th>
				      <th width="8%">款项</th>
				      <th width="11%">应收金额</th>
				      <th width="11%">累计返佣金额</th>
				      <th width="7%">币种</th>
				      <th width="12%">本次返佣金额</th>
				      <th width="9%">审批状态</th>
				      <th width="18%">备注</th>
			        </tr>
			      </thead>
				  <tbody>
				    <tr group="travler1">
				      <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd"
							value="${rebates.createDate}" /></td>
				      <td class="tc">${travelerInfo.name}</td>
				      <td class="tc"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${travelerInfo.personType}"/></td>
				      <td>${rebates.costname}</td>
				      <td class="tr">${fns:getHotelMoneyAmountBySerialNum(travelerInfo.payPriceSerialNum,2)}</td>
					<td class="tr">${fns:getTravelRebatesByOrderType(travelerInfo.uuid,11)}</td>
				      <td>${rebates.currency.currencyName}</td>
				      <td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
				      <td class="tc tdgreen">审核通过</td>
				      <td>${rebates.remark}</td>
			        </tr>
			      </tbody>
			  </table>
			  
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">关闭</a>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>