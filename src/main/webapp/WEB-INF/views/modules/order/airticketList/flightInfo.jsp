<%@ page contentType="text/html;charset=UTF-8"%>
<tr id="child${order.id}" style="display:none;" class="activity_team_top1">
	<td colspan="19" class="team_top" style="background-color:#d1e5f5;"><!-- 解决bug#14184colspan由17变成了19 -->
		<table id="teamTable" class="table activitylist_bodyer_table_fly" style="margin:0 auto;">
			<thead>
				<tr>
					<th class="tc" width="5%">行程段</th>
					<th class="tc" width="14%">航空公司</th>
					<th class="tc" width="8%">航班号</th>
					<th class="tc" width="14%">出发城市机场</th>
					<th class="tc" width="11%">起飞时间</th>
					<th class="tc" width="14%">到达城市机场</th>
					<th  class="tc" width="11%">到达时间</th>
					<th class="tc" width="7%">舱位</th>
					<th class="tr" width="10%">价格/人<br>税费</th>
				</tr>
			</thead>
			<tbody>
				<c:set value="true" var="flag" />
				<c:set value="true" var="flag2" />
				<c:forEach items="${order.airticketOrderFlights }" var="airticketOrderFlight">

					<tr class="tr-hovers">
						<td class="tc">${airticketOrderFlight.orderNumber }</td>
						<td class="tc">${fns:getAirlineNameByAirlineCode(airticketOrderFlight.airlines)}</td>
						<td class="tc">${airticketOrderFlight.flight_number}</td>
						<td class="tc">${airticketOrderFlight.startAirportName }</td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airticketOrderFlight.startTime}" /></td>
						<td class="tc">${airticketOrderFlight.endAirportName}</td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airticketOrderFlight.arrivalTime}" /></td>
						<td class="tc" style="border-right:1px solid #DDD">${fns:getDictLabel(airticketOrderFlight.spaceGrade,"spaceGrade_Type" , "无")}</td>
						<c:choose>
							<c:when test="${order.airType==1}">
								<c:if test="${order.isSection == 1 }">
								<td class="tr">
									<div class="yfje_dd">
										${airticketOrderFlight.formatedSubAdultPrice}
									</div>
									<div class="dzje_dd">
											${airticketOrderFlight.currencyMark}${airticketOrderFlight.taxamt}
									</div>
								</td>
								</c:if>
								<c:if test="${order.isSection != 1 && flag2 == true}">
									<td class="tr" rowspan="${order.airticketOrderFlights.size()}" style="text-align:center; vertical-align:middle;">
										<div class="yfje_dd">
											${order.formatedAdultPrice }
										</div>
										<div class="dzje_dd">
											${airticketOrderFlight.currencyMark}${airticketOrderFlight.taxamt}
										</div>
									</td>
								<c:set value="false" var="flag2" />
								</c:if>
							</c:when>
							<c:when test="${order.airType==3}">
								<td class="tr">
									<div class="yfje_dd">
											${order.formatedAdultPrice }
									</div>
									<div class="dzje_dd">
											${airticketOrderFlight.currencyMark}${airticketOrderFlight.taxamt}
									</div>
								</td>
							</c:when>
							<c:when test="${order.airType==2 && flag == true}">
								<td class="tr" rowspan="2" style="text-align:center; vertical-align:middle;">
									<div class="yfje_dd">
										${order.formatedAdultPrice }
									</div>
									<div class="dzje_dd">
											${airticketOrderFlight.currencyMark}${airticketOrderFlight.taxamt}
									</div>
								</td>
								<c:set value="false" var="flag" />
							</c:when>
						</c:choose>	
					</tr>
				 </c:forEach>
			</tbody>
		</table>
	</td>
</tr>