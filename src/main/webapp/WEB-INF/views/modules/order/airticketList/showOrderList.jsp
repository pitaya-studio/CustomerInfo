<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 订单查询列表显示 -->
<tbody id="orderOrGroup_order_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
			<td colspan="16" style="text-align: center;">
				 暂无搜索结果
			</td>
		</tr>
	</c:if>
       
	<!-- 查询结果订单列表循环显示 -->
	<c:forEach items="${page.list }" var="order" varStatus="s">
		<tr class="toptr" <c:if test="${isNeedNoticeOrder == 1 && (isOpManager or companyUuid eq '58a27feeab3944378b266aff05b627d2' or (companyUuid ne '58a27feeab3944378b266aff05b627d2' && queryType eq 2))}">onclick="changeSeenFlagByMouse('${order.id}', this);"</c:if>>
			<input type="hidden" name="seenFlag" value="${order.seenFlag}" id="order_${order.id}"/>
			<input id="airticketOrderId" type="hidden" value="${order.id }"/>
			<!-- 数据列表 -->
			<%@ include file="/WEB-INF/views/modules/order/airticketList/orderData.jsp"%>
			<!-- 操作按钮 -->
			<%@ include file="/WEB-INF/views/modules/order/airticketList/orderOperation.jsp"%>
		</tr>
		<!-- 支付记录 -->
		<%@ include file="/WEB-INF/views/modules/order/airticketList/orderPayList.jsp"%>
		<!-- 航班信息 -->
		<%@ include file="/WEB-INF/views/modules/order/airticketList/flightInfo.jsp"%>
	</c:forEach>
</tbody>
<tr class="checkalltd">
	<td colspan='19' class="t1">
		<label> <input type="checkbox" name="airticket_orderAllChk" onclick="airticket_orderAllChecked(this)" /> 全选 </label> 	
		<label> <input type="checkbox" name="airticket_orderAllChkNo" onclick="airticket_orderAllNoCheck()" /> 反选 </label>
		<c:if test="${queryType == 1 }">   <!-- 销售机票订单 -->
			<shiro:hasPermission name="airticketOrderSale:download:confirmation">
				<input type="button" class="btn btn-primary" value="批量下载确认单" onclick="airticket_orderBatchDownload('airticketOrderId')">
			</shiro:hasPermission>
			<shiro:lacksPermission name="airticketOrderSale:download:confirmation">
				<input type="button" class="btn btn-primary gray" value="批量下载确认单" disabled="disabled">
			</shiro:lacksPermission>
		</c:if>	
		<c:if test="${queryType == 2 }">   <!-- 计调机票订单 -->
			<shiro:hasPermission name="airticketOrderOprt:download:confirmation">
				<input type="button" class="btn btn-primary" value="批量下载确认单" onclick="airticket_orderBatchDownload('airticketOrderId')">
			</shiro:hasPermission>
			<shiro:lacksPermission name="airticketOrderOprt:download:confirmation">
				<input type="button" class="btn btn-primary gray" value="批量下载确认单" disabled="disabled">
			</shiro:lacksPermission>
		</c:if>					
	</td>
</tr>
