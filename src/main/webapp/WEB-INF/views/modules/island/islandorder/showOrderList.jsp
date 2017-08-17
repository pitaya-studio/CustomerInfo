<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 订单查询列表显示 -->
<tbody id="orderOrGroup_order_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
			<td colspan="18" style="text-align: center;">
				暂无搜索结果
			</td>
		</tr>
	</c:if>
       
	<!-- 查询结果订单列表循环显示 -->
	<c:forEach items="${page.list }" var="orders" varStatus="s">
		<tr class="toptr">
			<!-- 订单数据 -->
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderData.jsp"%>
			<!-- 订单操作 -->
			<%@ include file="/WEB-INF/views/modules/island/islandorder/orderOperation.jsp"%>
		</tr>
		<!-- 支付记录 -->
		<%@ include file="/WEB-INF/views/modules/island/islandorder/orderPayList.jsp"%>
	</c:forEach>
</tbody>
