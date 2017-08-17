<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 支付记录 -->
<tr name="subtr" style="display: none;" class="activity_team_top1">
	<td colspan="17" class="team_top" style="background-color:#dde7ef;">
		<table  class="table activitylist_bodyer_table" style="margin:0 auto;" id="table_orderPay">
			<thead>
				<tr>
					<th>收款方式</th>
					<th>金额</th>
					<th>日期</th>
					<th>是否已确认达账</th>
					<th>支付凭证</th>
					<th colspan=2>操作</th>
				</tr>
			</thead> 
			<c:forEach items="${orders.orderPayList }" var="orderPay">
				<tr>
					<td>${orderPay.payTypeName }</td>
					<td>
						<c:choose>
							<c:when test="${not empty orderPay.moneySerialNum}">${orderPay.moneySerialNum }</c:when>
							<c:otherwise>¥ 0.00</c:otherwise>
						</c:choose>
					</td>
					<td>
						<fmt:formatDate value="${orderPay.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>
						<!--空为未达账 0为撤销 1为达账 2为驳回-->
						<c:choose>
							<c:when test="${empty orderPay.isAsAccount}">未达账</c:when>
							<c:when test="${orderPay.isAsAccount == 0}">已撤销</c:when>
							<c:when test="${orderPay.isAsAccount == 1}">是</c:when>
							<c:when test="${orderPay.isAsAccount == 2}">已驳回</c:when>
						</c:choose>
					</td>
					<c:if test="${empty orderPay.payVoucher}">
						<td>暂无支付凭证</td>
					</c:if>
					<c:if test="${not empty orderPay.payVoucher}">
						<td><a class="showpayVoucher" lang="${orderPay.id}">收款凭证</a></td>
					</c:if>
                         
					<td><a href="javascript:void(0)" onClick="changepayVoucher('${orderPay.uuid}','${orders.orderUuid}')">改收款凭证</a></td>
				</tr>
			</c:forEach>
		</table>
	</td>
</tr>
