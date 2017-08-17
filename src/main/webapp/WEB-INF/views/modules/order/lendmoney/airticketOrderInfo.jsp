<%@ page contentType="text/html;charset=UTF-8"%>  
	<div class="ydbz_tit">订单详情 </div>
	<div class="orderdetails1">
        <table border="0" width="96%" style="margin-left:0;">
            <tbody>
                <tr>
                    <td class="mod_details2_d1">下单人：</td>
                    <td class="mod_details2_d2">${orderDetail.userName }</td>
                    <td class="mod_details2_d1">下单时间：</td>
                    <td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.orderCreateDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td> 
                    <td class="mod_details2_d1">操作人：</td>
                    <td class="mod_details2_d2">${orderDetail.activityCreateName }</td>		        
                </tr>
                <tr> 
                    <td class="mod_details2_d1">订单编号：</td>
                    <td class="mod_details2_d2">${orderDetail.orderNo }</td>
                    <td class="mod_details2_d1">订单团号：</td>
                    <td class="mod_details2_d2">${orderDetail.orderGroupCode }</td>
                    </tr>
                <tr>
                    <td class="mod_details2_d1">销售：</td>
			        <c:set value="${orderDetail.salerId}" var="saler"></c:set>
			        <td class="mod_details2_d2">${fns:getUserNameById(saler)}</td>	        
                </tr>
            </tbody>
        </table>
    </div>
	<div class="ydbzbox fs" style="overflow:hidden;">
		<div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden;">
		   <p class="ydbz_mc">
			  ${fns:getDictLabel(orderDetail.departureCity, 'from_area', '')}
			  —
			    <c:forEach items="${arrivedareas}" var="area">
                    <c:if test="${area.id eq orderDetail.arrivedCity}">
                        ${area.name}
                    </c:if>
                </c:forEach>：<c:choose>
				<c:when test="${orderDetail.airType == 1 }">多段</c:when>
				<c:when test="${orderDetail.airType == 2 }">往返</c:when>
				<c:when test="${orderDetail.airType == 3 }">单程</c:when>
			</c:choose>
			<c:forEach items="${orderDetail.flightInfoList }" var="info" varStatus="s">
				<c:if test="${s.index==0&&orderDetail.airType!=1}">
				  <c:choose>
				      <c:when test="${info.ticketAreaType==1}">（内陆）</c:when>
					  <c:when test="${info.ticketAreaType==2}">（国际）</c:when>
					  <c:when test="${info.ticketAreaType==3}">（内陆+国际）</c:when>
				   </c:choose>
				</c:if>
			</c:forEach>
			</p>
			<c:choose>
				<c:when test="${orderDetail.airType == 1 }">
					<!--多段-->

					<c:forEach items="${orderDetail.flightInfoList }" var="fi">
						<div class="title_samil">
							第${fi.orderNumber }段：<c:choose>
												      <c:when test="${fi.ticketAreaType==1}">内陆</c:when>
													  <c:when test="${fi.ticketAreaType==2}">国际</c:when>
													  <c:when test="${fi.ticketAreaType==3}">内陆+国际</c:when>
												   </c:choose>
						</div>
				<c:forEach items="${orderDetail.flightInfoList}" var="flightInfo">
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${flightInfo.endAirportName }</td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">航班号：</td>
                                <td class="mod_details2_d2">${flightInfo.flightNumber }</td>
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
					</c:forEach>
				</c:when>
				<c:when test="${orderDetail.airType == 2 }">
					<!--往返-->
					<div class="title_samil">去程：</div>
					<c:forEach items="${orderDetail.flightInfoList}" var="flightInfo">
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${flightInfo.endAirportName }</td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">航班号：</td>
                                <td class="mod_details2_d2">${flightInfo.flightNumber }</td>
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
					<div class="title_samil">返程：</div>
					<c:forEach items="${orderDetail.flightInfoList}" var="flightInfo">
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${flightInfo.endAirportName }</td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">航班号：</td>
                                <td class="mod_details2_d2">${flightInfo.flightNumber }</td>
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
				<c:when test="${orderDetail.airType == 3 }">
					<!--单程-->
					<c:forEach items="${orderDetail.flightInfoList}" var="flightInfo">
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${flightInfo.endAirportName }</td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/>
								</td>
								<td class="mod_details2_d1">航班号：</td>
                                <td class="mod_details2_d2">${flightInfo.flightNumber }</td>
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
			<div class="seach25 seach100">
                <p class="fbold f14">预收人数：</p>
                <div>${orderDetail.reservationsNum }</div>
            </div>
			<div class="mod_information_d7"></div>
			<ul class="ydbz_dj specialPrice">
				<li style="display: none;"><input type="text" class="required"
					value="0" onafterpaste="this.value=this.value.replace(/\D/g,'')"
					onkeyup="this.value=this.value.replace(/\D/g,'')"
					id="orderPersonelNum"></li>
				<li><span class="ydtips">单价</span>
					<p>
						成人：<font color="#FF0000">
						${orderDetail.adultPrice }
						</font>
					</p>
					<p>
						儿童：<font color="#FF0000">
						${orderDetail.childPrice }
						</font>
					</p>
					<p>
						特殊人群：<font color="#FF0000">
						${orderDetail.specialPrice }
						</font>
					</p></li>
				<li><span class="ydtips"> 出行人数</span>
					<p>
						成人：<span>${orderDetail.adultNum }</span> 人
					</p>
					<p>
						儿童：<span>${orderDetail.childNum }</span> 人
					</p>
					<p>
						特殊人群：<span>${orderDetail.specialNum }</span> 人
					</p></li>
				<li class="ydbz_single">
				   <span class="">税费：</span>${orderDetail.taxamt }/人
				</li>
			</ul>
		</div>