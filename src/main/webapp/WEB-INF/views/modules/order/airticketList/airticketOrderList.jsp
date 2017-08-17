<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<%@ include file="/WEB-INF/views/modules/order/airticketList/orderHeadInfo.jsp"%>
</head>

<body>
<!-- 顶部菜单列表 -->
<%@ include file="/WEB-INF/views/modules/order/airticketList/topMenu.jsp"%>
	
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
	<!-- 搜索查询 -->
	<%@ include file="/WEB-INF/views/modules/order/airticketList/orderForm.jsp"%>
	<!-- 部门分区 -->
	<%@ include file="/WEB-INF/views/common/departmentDiv.jsp"%>
	<!-- 订单列表、团期列表 -->
	<%@ include file="/WEB-INF/views/modules/order/airticketList/swith.jsp"%>
	<!-- 订单排序 -->
	<%@ include file="/WEB-INF/views/modules/order/airticketList/orderByDiv.jsp"%>
	<!-- 订单列表 -->
	<table id="contentTable" class="table mainTable activitylist_bodyer_table">
		<c:choose>
			<c:when test="${orderOrGroup == 'order'}">
				<!-- 订单标题 -->
				<%@ include file="/WEB-INF/views/modules/order/airticketList/orderThead.jsp"%>
				<!-- 订单列表 -->
				<%@ include file="/WEB-INF/views/modules/order/airticketList/showOrderList.jsp"%>
			</c:when>
			<c:otherwise>
				<!-- 团期列表 -->
				<%@ include file="/WEB-INF/views/modules/order/airticketList/showGroupList.jsp"%>
			</c:otherwise>
		</c:choose>
	</table>
</div>
<!-- 0444 -->
<div id="relationInvoice" class="display-none">
	<div class="select_account_pop" style="padding:20px">
		<div >
			<table class="activitylist_bodyer_table">
				<thead>
					<tr>
						<th>选择</th>
						<th>发票号</th>  
						<th>团号</th>
						<th>申请人</th>
						<th>开票状态</th>
						<th>开票金额</th>
					</tr>
				</thead>
				<tbody id="relationInvoiceTable">
					<tr>
						<td name='logId'  class='tc' value=''><input id="" type="checkbox" value=""></td>
						<td name='operation' class='tc'><span></span></td>
						<td name='operation' class='tc'><span></span></td>
						<td name='operationTime' class='tc'><span></span></td>
						<td name='mainContext' class='tl'><span></span></td>
						<td name='mainContext' class='tl'><span></span></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<!-- 0444 -->
<div class="pagination clearFix">
   ${page}
</div>
</body>
</html>
