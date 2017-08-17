<%@ page contentType="text/html;charset=UTF-8"%>  
	<div class="ydbz_tit">订单详情 </div>
	<div class="orderdetails1">
        <table border="0" width="96%" style="margin-left:0;">
            <tbody>
                <tr>
                    <td class="mod_details2_d1">下单人：</td>
                    <td class="mod_details2_d2">${orderDetail.userName }</td>
                    <td class="mod_details2_d1">销售：</td>
                    <td class="mod_details2_d2">${orderDetail.salerName }</td>
                    <td class="mod_details2_d1">下单时间：</td>
                    <td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.orderCreateDate }" pattern="yyyy-MM-dd HH:mm:ss" /></td> 
                    <td class="mod_details2_d1">操作人：</td>
                    <td class="mod_details2_d2">${orderDetail.activityCreateName }</td>		        
                </tr>
                <tr> 
                	 <td class="mod_details2_d1">团队类型：</td>
                    <td class="mod_details2_d2">单办机票</td>
                    <td class="mod_details2_d1">订单编号：</td>
                    <td class="mod_details2_d2">${orderDetail.orderNo }</td>
                    <td class="mod_details2_d1">订单团号：</td>
                    <td class="mod_details2_d2">${orderDetail.orderGroupCode }</td>
                    <c:if test="${orderDetail.airType ne '3' }">
	                    <td class="mod_details2_d1">转机点：</td>
	                    <td class="mod_details2_d2">${orderDetail.flightInfoList[1].startAirportName }</td>
                    </c:if>
                    </tr><tr>
                </tr>
            </tbody>
        </table>
    </div>
		<div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden;">
		   <p class="ydbz_mc">
<!-- 			    <c:forEach items="${fromAreas}" var="v"> -->
<!-- 				    <c:if test="${v.value == orderDetail.departureCity }">${v.label }</c:if> -->
<!-- 			    </c:forEach> -->
			    ${fns:getDictLabel(orderDetail.departureCity,'from_area', '')}
			    —
			    ${fns:findAreaNameById(orderDetail.arrivedCity)}
<!-- 			    <c:forEach items="${arrivedareas }" var="v"> -->
<!-- 					<c:if test="${v.id == orderDetail.arrivedCity }">${v.name }</c:if> -->
<!-- 				</c:forEach> -->
		       ：<c:choose>
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
					  <c:when test="${info.ticketAreaType==4}">（国内）</c:when>
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
													  <c:when test="${fi.ticketAreaType==4}">国内</c:when>
												   </c:choose>
						</div>
						<table width="90%" border="0">
							<tbody>
								<tr>
									<td class="mod_details2_d1">出发机场：</td>
									<td class="mod_details2_d2">${fi.startAirportName }</td>
									<td class="mod_details2_d1">到达机场：</td>
									<td class="mod_details2_d2">${fi.endAirportName }</td>
									 <c:forEach
											items="${spaceGradelist }" var="v" varStatus="vs">
											<c:if test="${fi.spaceGrade == v.value }">${v.label}</c:if>
										</c:forEach></td>
									<td class="mod_details2_d1">舱位：</td>
									<td class="mod_details2_d2">
									    <c:forEach
											items="${airspacelist }" var="v">
											<c:if test="${v.value == fi.airspace }">${v.label}</c:if>
										</c:forEach></td>
								</tr>
								<tr>
									<td class="mod_details2_d1">出发时刻：</td>
									<td class="mod_details2_d2"><fmt:formatDate value="${fi.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
									<td class="mod_details2_d1">到达时刻：</td>
									<td class="mod_details2_d2"><fmt:formatDate value="${fi.arrivalTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								</tr>
							</tbody>
						</table>
					</c:forEach>
				</c:when>
				<c:when test="${orderDetail.airType == 2 }">
					<!--往返-->
					<div class="title_samil">去程：</div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${orderDetail.flightInfoList[0].startAirportName
									}</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${orderDetail.flightInfoList[0].endAirportName
									}</td>
								 <c:forEach
										items="${spaceGradelist }" var="v" varStatus="vs">
										<c:if test="${fi.spaceGrade == v.value }">${v.label}</c:if>
									</c:forEach></td>
								<td class="mod_details2_d1">舱位：</td>
								<td class="mod_details2_d2">
								    <c:forEach
										items="${airspacelist }" var="v">
										<c:if test="${v.value == fi.airspace }">${v.label}</c:if>
									</c:forEach></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate
										value="${orderDetail.flightInfoList[0].startTime }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate
										value="${orderDetail.flightInfoList[0].arrivalTime }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
							</tr>
						</tbody>
					</table>
					<div class="title_samil">返程：</div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${orderDetail.flightInfoList[1].startAirportName}</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${orderDetail.flightInfoList[1].endAirportName}</td>
								<td class="mod_details2_d1">舱位：</td>
								<td class="mod_details2_d2"><c:forEach
										items="${airspacelist }" var="v">
										<c:if
											test="${v.value == orderDetail.flightInfoList[1].airspace }">${ v.label}</c:if>
								</c:forEach></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate
										value="${orderDetail.flightInfoList[1].startTime }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate
										value="${orderDetail.flightInfoList[1].arrivalTime }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
							</tr>
						</tbody>
					</table>
				</c:when>
				<c:when test="${orderDetail.airType == 3 }">
					<!--单程-->
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${orderDetail.flightInfoList[0].startAirportName
									}</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${orderDetail.flightInfoList[0].endAirportName
									}</td>
								<td class="mod_details2_d1">舱位：</td>
								<td class="mod_details2_d2"><c:forEach
										items="${airspacelist }" var="v">
										<c:if
											test="${v.value == orderDetail.flightInfoList[0].airspace }">${v.label}</c:if>
								</c:forEach></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate
										value="${orderDetail.flightInfoList[0].startTime }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate
										value="${orderDetail.flightInfoList[0].arrivalTime }"
										pattern="yyyy-MM-dd HH:mm:ss" />
								</td>
							</tr>
						</tbody>
					</table>
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