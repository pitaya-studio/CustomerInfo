<%@ page contentType="text/html;charset=UTF-8" %>
<div class="tr">
	<a href="${ctx}/orderCommon/manage/getOrderModifyRecord/${productorder.id}">修改记录</a>
	<shiro:hasPermission name="${orderTypeStr}Order:download:travelerInfo">
		<a href="javascript:void(0)"  onClick="downloadData('${productorder.id}', 'traveler' )">下载游客资料</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${orderTypeStr}Order:download:visa">
		<a href="${ctx}/orderCommon/manage/interviewNotice/${productorder.id}">下载面签通知</a>
	</shiro:hasPermission>
	<shiro:hasPermission name="${orderTypeStr}Order:download:group">
		<c:if test="${not empty orders.open_date_file}">
			<a href="javascript:void(0)"  onClick="downloadData('${orders.open_date_file}', 'group')">下载出团通知</a>
		</c:if>
	</shiro:hasPermission>
	<shiro:hasPermission name="${orderTypeStr}Order:download:confirmation">
		<c:if test="${not empty orders.confirmationFileId}">
			<a href="javascript:void(0)"  onClick="downloadData('${orders.confirmationFileId}', 'confirmation')">下载确认单</a>
		</c:if>
	</shiro:hasPermission>
</div>

<!-- 下载 -->
<form id="exportForm" action="${ctx}/orderCommon/manage/downloadData" method="post">
	<input type="hidden" id="orderId" name="orderId">
	<input type="hidden" name="orderType" value="${productorder.orderStatus}">
	<input type="hidden" id="downloadType" name="downloadType">
	<!-- 导出 excel 首行显示 订单编号 20150108 -->
	<input type="hidden" name="orderNum" value="${productorder.orderNum}">
</form>
<!-- 导出 -->
<form id="exportTravelesForm" action="${ctx}/activity/manager/exportExcel" method="post">
	<input type="hidden" id="groupId" name="groupId">
	<input type="hidden" name="orderType" value="${productorder.orderStatus}">
	<input type="hidden" id="groupCode" name="groupCode">
</form>