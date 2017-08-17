<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 支付记录 -->
<tr name="subtr" style="display: none;" class="activity_team_top1">
	<td colspan="${showType==89?15:14}" class="team_top" style="background-color:#dde7ef;">
		<table  class="table activitylist_bodyer_table" style="margin:0 auto;" id="table_orderPay">
			<thead>
				<tr>
					<th class="tc">收款方式</th>
					<th class="tr">金额</th>
					<c:if test="${not empty orders.differenceFlag and orders.differenceFlag eq '1'}">
						<th class="tr">门店结算价差额返还</th>
					</c:if>
					<th class="tc">日期</th>
					<th class="tc">收款类型</th>
					<th class="tc">是否已确认达账</th>
					<th class="tc">收款凭证</th>
					<c:if test="${orders.payStatus==4 || orders.payStatus==5}">
						<th colspan=2 class="tc">操作</th>
					</c:if>
				</tr>
			</thead> 
		</table>
	</td>
</tr>
