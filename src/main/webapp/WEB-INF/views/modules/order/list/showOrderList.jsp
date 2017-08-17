<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 订单查询列表显示 -->
<tbody id="orderOrGroup_order_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
                <td colspan="${showType==89?18:17}" style="text-align: center;">
				 暂无搜索结果
                </td>
		</tr>
       </c:if>
       
       <!-- 查询结果订单列表循环显示 -->
       <c:forEach items="${page.list }" var="orders" varStatus="s">
		<tr <c:if test="${isNeedNoticeOrder == 1}">onclick="changeSeenFlagByMouse('${orders.id}', this);"</c:if>>
			<input type="hidden" name="seenFlag" value="${orders.seenFlag}" id="order_${orders.id}"/>
			<%@ include file="/WEB-INF/views/modules/order/list/orderData.jsp"%>
			<%@ include file="/WEB-INF/views/modules/order/list/orderOperation.jsp"%>
		</tr>
		<!-- 支付记录 -->
		<%@ include file="/WEB-INF/views/modules/order/list/orderPayList.jsp"%>
	</c:forEach>
	<tr class="checkalltd">
		<td colspan='19' class="t1">
			<label> <input type="checkbox" name="product_orderAllChk" onclick="product_orderAllChecked(this)" /> 全选 </label> 	
			<label> <input type="checkbox" name="product_orderAllChkNo" onclick="product_orderAllNoCheck()" /> 反选 </label>
			<shiro:hasPermission name="${orderTypeStr}Order:download:confirmation">			
				<input type="button" class="btn btn-primary" value="批量下载确认单" onclick="product_orderBatchDownload('productOrderId')">
			</shiro:hasPermission>
			<shiro:lacksPermission name="${orderTypeStr}Order:download:confirmation">
				<input type="button" class="btn btn-primary gray" value="批量下载确认单" disabled="disabled">
			</shiro:lacksPermission>	
		</td>
	</tr>
</tbody>

