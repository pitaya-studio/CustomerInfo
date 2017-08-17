<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 支付记录 -->
<tr name="subtr" style="display: none;" class="activity_team_top1">
	<td colspan="19" class="team_top" style="background-color:#dde7ef;">
		<table  class="table activitylist_bodyer_table_fly" style="margin:0 auto;" id="table_orderPay">
			<thead>
				<tr>
					<th>收款方式</th>
					<th class="tr">金额</th>
					<th>日期</th>
					<th>收款类型</th>
					<th>是否已确认达账</th>
					<th>收款凭证</th>
					<c:if test="${order.order_state==4 || order.order_state==5}">
						<th colspan=2>操作</th>
					</c:if>
				</tr>
			</thead> 
			<c:forEach items="${order.orderPayList }" var="orderPay">
				<tr class="tr-hovers">
					<td>${orderPay.payTypeName }</td>
					<td class="tr">
						<c:choose>
							<c:when test="${not empty orderPay.moneySerialNum}">${orderPay.moneySerialNum }</c:when>
							<c:otherwise>¥ 0.00</c:otherwise>
						</c:choose>
					</td>
					<td>
						<fmt:formatDate value="${orderPay.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td>${fns:getDictLabel(orderPay.payPriceType, "payprice_Type", "")}</td>
					<%-- <td>${fns:getDictLabel(orderPay.isAsAccount, "yes_no", "否")}</td> --%>
					<!--1 达账  0或空 未达账  -->
					<td>
						<c:if test="${orderPay.isAsAccount eq '0'}">
							已撤销
						</c:if>
						<c:if test="${orderPay.isAsAccount eq '1'}">
							已达账
						</c:if>
						<c:if test="${orderPay.isAsAccount eq '2'}">
							<c:if test="${empty orderPay.rejectReason }">已驳回</c:if>
							<c:if test="${not empty orderPay.rejectReason }">
								<div class="pr xuanfudiv">已驳回
									<div class="ycq xuanfu" style="width: 24px;">备注</div><!--111 添加悬浮-->
		                           	<div class="hover-title team_top hide" id="hoverWindow" >${orderPay.rejectReason }</div>
	                          	</div>
                          	</c:if>
						</c:if>
					</td>
					<c:if test="${empty orderPay.payVoucher}">
						<td>暂无收款凭证</td>
					</c:if>
					<c:if test="${not empty orderPay.payVoucher}">
						<td><a class="showpayVoucher" lang="${orderPay.id}">收款凭证</a></td>
					</c:if>
					<c:if test="${order.order_state==4 || order.order_state==5}">
						<td><a href="javascript:void(0)" onClick="changepayVoucher(${orderPay.id},${order.id})">改收款凭证</a></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
	</td>
</tr>