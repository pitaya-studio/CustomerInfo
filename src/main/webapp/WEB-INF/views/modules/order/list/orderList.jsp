<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<c:set var="companyUuid" value="${fns:getCompanyUuid()}"/>
<!-- 针对C147andC109 奢华和优加：优惠，和申请办签信息的展示 控制开关 -->
<c:if test="${companyUuid == '75895555346a4db9a96ba9237eae96a5' or companyUuid == '7a81c5d777a811e5bc1e000c29cf2586'}">
	<c:set var="isForYouJia" value="true"/>
</c:if>
<c:if test="${!(companyUuid == '75895555346a4db9a96ba9237eae96a5' or companyUuid == '7a81c5d777a811e5bc1e000c29cf2586')}">
	<c:set var="isForYouJia" value="false"/>
</c:if>
<!-- 针对C109 奢华和优加：是否是散拼产品 控制开关 -->
<c:if test="${orderStatus == '2'}">
	<c:set var="isLoose" value="true"/>
</c:if>
<c:if test="${orderStatus != '2'}">
	<c:set var="isLoose" value="false"/>
</c:if>

<html>
<head>
<%@ include file="/WEB-INF/views/modules/order/list/orderHeadInfo.jsp"%>
    <style>
        .team_top table tr td {

            border-top: 1px solid #dddddd;

        }

    </style>
</head>

<body>
<!-- 顶部菜单列表 -->
<%@ include file="/WEB-INF/views/modules/order/list/topMenu.jsp"%>
	
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
	<!-- 搜索查询 -->
	<%@ include file="/WEB-INF/views/modules/order/list/orderForm.jsp"%>
	<!-- 部门分区 -->
	<%@ include file="/WEB-INF/views/common/departmentDiv.jsp"%>
	<c:choose>
		<c:when test="${showType != 1000}">
			<!-- 订单列表、团期列表 -->
			<%@ include file="/WEB-INF/views/modules/order/list/swith.jsp"%>
		</c:when>
	</c:choose>
	<!-- 订单排序 -->
	<%@ include file="/WEB-INF/views/modules/order/list/orderByDiv.jsp"%>
	<!-- 需求223 看板 -->
	<shiro:hasPermission name="cruiseshipStockList:stock:orderboard">
		<c:if test="${orderStatus eq '10' }">
			<%@ include file="/WEB-INF/views/modules/cruiseship/cruiseshipboard/cruiseshipboard.jsp"%>
		</c:if>
	</shiro:hasPermission>
	<!-- 订单列表：showType为1000时，是预报名订单 -->
	<c:choose>
		<c:when test="${showType != 1000}">
			<table id="contentTable" class="table mainTable activitylist_bodyer_table">
				<c:choose>
					<c:when test="${orderOrGroup == 'order'}">
						<!-- 订单标题 -->
						<%@ include file="/WEB-INF/views/modules/order/list/orderThead.jsp"%>
						<!-- 订单列表 -->
						<%@ include file="/WEB-INF/views/modules/order/list/showOrderList.jsp"%>
					</c:when>
					<c:otherwise>
						<!-- 团期列表 -->
						<%@ include file="/WEB-INF/views/modules/order/list/showGroupList.jsp"%>
					</c:otherwise>
				</c:choose>
			</table>
		</c:when>
		<c:otherwise>
			<c:if test="${orderStatus == 2}">
				<%@ include file="/WEB-INF/views/modules/order/list/applyOrderList.jsp"%>
			</c:if>
		</c:otherwise>
	</c:choose>
</div>
<!-- 0444 -->
<div id="relationInvoice" class="display-none">
	<div class="select_account_pop" style="padding:20px">
		<div >
			<table class="table activitylist_bodyer_table">
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
<div class="pagination clearFix">
   ${page}
</div>
</body>
</html>
