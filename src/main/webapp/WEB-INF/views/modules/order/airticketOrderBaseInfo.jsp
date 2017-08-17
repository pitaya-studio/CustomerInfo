<%@ page contentType="text/html;charset=UTF-8"%>
<div class="orderdetails">
	<div class="orderdetails_tit">
		<span></span>订单信息 <input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
	</div>
	<div class="orderdetails1">
		<table border="0" width="90%" style="margin-left:0;">
			<tbody>
				<tr>
					<td class="mod_details2_d1">下单人：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
					<td class="mod_details2_d1">下单时间：</td>
					<td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td class="mod_details2_d1">操作人：</td>
					<td class="mod_details2_d2">${fns:getUserNameById(orderDetailInfoMap.createBy)}</td>
				</tr>				
				<tr>
					<td class="mod_details2_d1">订单编号：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.orderNo }</td>
					<td class="mod_details2_d1">订单团号：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.orderGroupCode }</td>
					<td class="mod_details2_d1">团队类型：</td>
					<td class="mod_details2_d2">
						<c:choose>
							<c:when test="${orderDetailInfoMap.type == 1 }">单办</c:when>
							<c:when test="${orderDetailInfoMap.type == 2 }">参团</c:when>
						</c:choose>
					</td>
				</tr>				
				<tr>
					<td class="mod_details2_d1">销售：</td>
					<td class="mod_details2_d2">${orderDetailInfoMap.salerName}</td>
					<td class="mod_details2_d1">参团订单编号：</td>
					<td class="mod_details2_d2">${porder.orderNum }</td>
					<td class="mod_details2_d1">参团订单团号：</td>
					<td class="mod_details2_d2">${groupNum }</td>
				</tr>
				<c:if test="${orderDetailInfoMap.type == 2 }">
					<tr>
						<td class="mod_details2_d1">下单人：</td>
						<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
						<td class="mod_details2_d1">参团订单编号：</td>
						<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td>
						<td class="mod_details2_d1">参团团号：</td>
						<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td>
					</tr>
				</c:if>
				<tr>
					<td class="mod_details2_d1">订单总额：</td>
					<td class="mod_details2_d2">${fns:getMoneyAmountBySerialNum(orderDetailInfoMap.total_money,2)}</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="orderdetails_tit">
		<span></span>产品信息
	</div>
	<div class="mod_information_dzhan" style="overflow:hidden;">
		<div class="mod_information_dzhan_d mod_details2_d">
			<c:choose>
				<c:when test="${not empty orderDetailInfoMap.flightInfoList && orderDetailInfoMap.airType == 2 }">
					<!--往返-->
					<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（往返）</span>
					<div class="mod_information_d7"></div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发城市：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}</td>
								<td class="mod_details2_d1">到达城市：</td>
								<td class="mod_details2_d2">
									<c:forEach items="${arrivedareas}" var="area">
										<c:if test="${area.id eq orderDetailInfoMap.arrivedCity}">${area.name}</c:if>
									</c:forEach>
								</td>
								<td class="mod_details2_d1">预收人数：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
							</tr>
						</tbody>
					</table>
					<c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
						<div class="title_samil">
							<c:choose>
								<c:when test="${flightInfo.orderNumber==1 }">去程：</c:when>
								<c:when test="${flightInfo.orderNumber==2 }">回程：</c:when>
							</c:choose>
						</div>
						<table width="90%" border="0">
							<tbody>
								<tr>
									<td class="mod_details2_d1">出发机场：</td>
									<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
									<td class="mod_details2_d1">到达机场：</td>
									<td class="mod_details2_d2">${flightInfo.endAirportName }
									</td>
								</tr>
								<tr>
									<td class="mod_details2_d1">出发时刻：</td>
									<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm" /></td>
									<td class="mod_details2_d1">到达时刻：</td>
									<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm" /></td>
								</tr>
								<tr>
									<td class="mod_details2_d1">航空公司：</td>
									<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
									<td class="mod_details2_d1">舱位等级：</td>
									<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type", "无")}</td>
									<td class="mod_details2_d1">舱位：</td>
									<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type", "无")}</td>
								</tr>
							</tbody>
						</table>
					</c:forEach>
				</c:when>
				<c:when test="${not empty orderDetailInfoMap.flightInfoList && orderDetailInfoMap.airType == 3 }">
					<!--单程-->
					<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（单程）</span>
					<div class="mod_information_d7"></div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发城市：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}</td>
								<td class="mod_details2_d1">到达城市：</td>
								<td class="mod_details2_d2">
									<c:forEach items="${arrivedareas}" var="area">
										<c:if test="${area.id eq orderDetailInfoMap.arrivedCity}">${area.name}</c:if>
									</c:forEach>
								</td>
								<td class="mod_details2_d1">预收人数：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
							</tr>
						</tbody>
					</table>
					<c:forEach items="${orderDetailInfoMap.flightInfoList}"
						var="flightInfo">
						<table width="90%" border="0">
							<tbody>
								<tr>
									<td class="mod_details2_d1">出发机场：</td>
									<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
									<td class="mod_details2_d1">到达机场：</td>
									<td class="mod_details2_d2">${flightInfo.endAirportName }
									</td>
								</tr>
								<tr>
									<td class="mod_details2_d1">出发时刻：</td>
									<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm" /></td>
									<td class="mod_details2_d1">到达时刻：</td>
									<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm" /></td>
								</tr>
								<tr>
									<td class="mod_details2_d1">航空公司：</td>
									<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
									<td class="mod_details2_d1">舱位等级：</td>
									<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type", "无")}</td>
									<td class="mod_details2_d1">舱位：</td>
									<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type", "无")}</td>
								</tr>
							</tbody>
						</table>
					</c:forEach>
				</c:when>
				<c:when test="${not empty orderDetailInfoMap.flightInfoList && orderDetailInfoMap.airType == 1 }">
					<!--多段-->
					<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（多段）</span>
					<div class="mod_information_d7"></div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发城市：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}</td>
								<td class="mod_details2_d1">到达城市：</td>
								<td class="mod_details2_d2">
									<c:forEach items="${arrivedareas}" var="area">
										<c:if test="${area.id eq orderDetailInfoMap.arrivedCity}">${area.name}</c:if>
									</c:forEach>
								</td>
								<td class="mod_details2_d1">预收人数：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
							</tr>
						</tbody>
					</table>
					<c:forEach items="${orderDetailInfoMap.flightInfoList }" var="flightInfo">
						<div class="title_samil">第${flightInfo.orderNumber }段：
							<c:choose>
								<c:when test="${flightInfo.ticketAreaType == 1 }">内陆</c:when>
								<c:when test="${flightInfo.ticketAreaType == 2 }">国际</c:when>
								<c:when test="${flightInfo.ticketAreaType == 3 }">内陆+国际</c:when>
							</c:choose>
						</div>
						<table width="90%" border="0">
							<tbody>
								<tr>
									<td class="mod_details2_d1">出发机场：</td>
									<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
									<td class="mod_details2_d1">到达机场：</td>
									<td class="mod_details2_d2">${flightInfo.endAirportName }
									</td>
								</tr>
								<tr>
									<td class="mod_details2_d1">出发时刻：</td>
									<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm" /></td>
									<td class="mod_details2_d1">到达时刻：</td>
									<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm" /></td>
								</tr>
								<tr>
									<td class="mod_details2_d1">航空公司：</td>
									<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
									<td class="mod_details2_d1">舱位等级：</td>
									<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>
									<td class="mod_details2_d1">舱位：</td>
									<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
								</tr>
							</tbody>
						</table>
					</c:forEach>
				</c:when>
			</c:choose>
			<div class="mod_information_d7"></div>
			<ul class="ydbz_dj specialPrice">
				<li style="display: none;">
					<input type="text" class="required" value="${orderDetailInfoMap.personNum}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" id="orderPersonelNum">
				</li>
				<li>
					<span class="ydtips">单价</span>
					<input id="aPrice" type="hidden" value="${orderDetailInfoMap.adultPrice }">
					<input id="cPrice" type="hidden" value="${orderDetailInfoMap.childPrice }">
					<input id="sPrice" type="hidden" value="${orderDetailInfoMap.specialPrice }">
					<p>成人：<font color="#FF0000">${orderDetailInfoMap.adultPrice }</font></p>
					<p>儿童：<font color="#FF0000">${orderDetailInfoMap.childPrice }</font></p>
					<p>特殊人群：<font color="#FF0000">${orderDetailInfoMap.specialPrice}</font></p>
				</li>
				<li>
					<span class="ydtips"> 出行人数</span>
					<p>成人：<span>${orderDetailInfoMap.adultNum }</span> 人</p>
					<p>儿童：<span>${orderDetailInfoMap.childNum }</span> 人</p>
					<p>特殊人群：<span>${orderDetailInfoMap.specialNum }</span> 人</p>
				</li>
				<li class="ydbz_single"><span class="">税费：</span>${orderDetailInfoMap.taxamt }/人</li>
			</ul>
		</div>
	</div>
</div>