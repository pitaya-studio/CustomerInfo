<%@ page contentType="text/html;charset=UTF-8" %>
<tr name="subtr" style="display: none;" class="activity_team_top1">
	<td name="colspanCount" class="tc">
		<table class="table activitylist_bodyer_table" style="margin:0 auto;" id="table_orderPay">
			<thead>
				<tr>
					<th class="tc">序号</th>
					<c:if test="${not empty orderNo}"><th class="tc" name="orderNo">订单号</th></c:if>
					<c:if test="${not empty agentName}"><th class="tc" name="agentName">渠道</th></c:if>
					<c:if test="${not empty shell }"><th class="tc" name="shell">销售</th></c:if>
					<c:if test="${not empty orderUser }"><th class="tc" name="orderUser">下单人</th></c:if>
					<c:if test="${not empty reserveDate }"><th class="tc" name="reserveDate">预定时间</th></c:if>
					<c:if test="${not empty personNum}"><th class="tc" name="personNum">人数</th></c:if>
					<c:if test="${not empty orderStatus }"><th class="tc" name="orderStatus">订单状态</th></c:if>
					<c:if test="${not empty totalAmount }"><th class="tc" name="totalAmount">订单总额</th></c:if>
					<c:if test="${not empty payedAmount }"><th class="tc" name="payedAmount">已付金额<br/>到账金额</th></c:if>
				</tr>
			</thead>
			<tbody>
	                                   
			</tbody>
		</table>
	</td>
</tr>