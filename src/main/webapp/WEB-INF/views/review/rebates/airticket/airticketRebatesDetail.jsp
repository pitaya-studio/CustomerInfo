<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<title>宣传费-详情</title>
				</c:when>
				<c:otherwise>
							<title>返佣-详情</title>
				</c:otherwise>
			</c:choose>   
    
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
    	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<page:param name="desc">宣传费审批</page:param>
				</c:when>
				<c:otherwise>
							<page:param name="desc">返佣审批</page:param>
				</c:otherwise>
			</c:choose>   
	    
	</page:applyDecorator>
	<!--右侧内容部分开始-->
		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<div class="mod_nav">审批 > 宣传费申请 > 宣传费审批</div>
				</c:when>
				<c:otherwise>
						<div class="mod_nav">审批 > 返佣申请 > 返佣审批</div>
				</c:otherwise>
			</c:choose>   
    
	<div class="ydbz_tit">订单信息</div>
	<input type="hidden" value="${productOrder.id}" id="orderId"/>
	<input type="hidden" value="${productOrder.productTypeId}" id="orderType"/>
    <div class="orderdetails1">
	   	<table border="0" style="margin-left: 25px" width="98%">
			<tbody>
				<tr>
					<td class="mod_details2_d1">下单人：</td>
			        <td class="mod_details2_d2">${productOrder.createBy.name}</td>
			        <td class="mod_details2_d1">销售：</td>
			        <td class="mod_details2_d2">${productOrder.salerName}</td> 
					<td class="mod_details2_d1">下单时间：</td>
					<td class="mod_details2_d2"><fmt:formatDate value="${productOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
			        <td class="mod_details2_d1">操作人：</td>
			        <td class="mod_details2_d2">${product.createBy.name}</td>              
				</tr>
				<tr> 
				    <td class="mod_details2_d1">团队类型：</td>
			        <td class="mod_details2_d2">${orderStatusStr}</td>	
		            <td class="mod_details2_d1">订单编号：</td>
					<td class="mod_details2_d2">${productOrder.orderNo}</td>
		            <td class="mod_details2_d1">订单团号：</td>
					<td class="mod_details2_d2">${productOrder.groupCode}</td>
		           <%--  <td class="mod_details2_d1">订单总额：</td>
					<td class="mod_details2_d2">${fns:getMoneyAmountBySerialNum(productOrder.totalMoney,2)}</td> 
		            <td class="mod_details2_d1">订单状态：</td>
					<td class="mod_details2_d2">${payModeStr}</td>	 --%>
		        </tr>
			</tbody>
		</table>
    </div>
<div class="ydbz_tit">产品信息</div>

<div class="mod_information_dzhan" style="overflow:hidden">
		<div class="mod_information_dzhan_d mod_details2_d">
			<c:if test="${airticketReturnDetailInfoMap.activityAirType == 2 }">
				<span style="color:#009535; font-size:16px; font-weight:bold;">往返（内陆+国际）</span>
				<div class="mod_information_d7"></div>
				<!--往返-->
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.departureCity,
								'from_area', '')}</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">${fns:findAreaNameById(airticketReturnDetailInfoMap.arrivedCity)}
							</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.reservationsNum}人</td>
						</tr>
					</tbody>
				</table>
				<div class="title_samil">去程：</div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(airticketReturnDetailInfoMap.flightInfoList[0].leaveAirport)}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(airticketReturnDetailInfoMap.flightInfoList[0].destinationAirpost)}
							</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
<!-- 							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[0].startTime}</td> -->
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
							
							<td class="mod_details2_d1">到达时刻：</td>
<!-- 							<td class="mod_details2_d2" colspan="3">${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}</td> -->
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[0].airlines,
								'traffic_name', '')}</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[0].spaceGrade,
								'spaceGrade_Type', '')}</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[0].airspace,
								'airspace_Type', '')}</td>
						</tr>
					</tbody>
				</table>
				<div class="title_samil">返程：</div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(airticketReturnDetailInfoMap.flightInfoList[1].leaveAirport)}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(airticketReturnDetailInfoMap.flightInfoList[1].destinationAirpost)}</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
<!-- 							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[1].startTime}</td> -->
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[1].startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
<!-- 							<td class="mod_details2_d2" colspan="3">${airticketReturnDetailInfoMap.flightInfoList[1].arrivalTime}</td> -->
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[1].arrivalTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[1].airlines,
								'traffic_name', '')}</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[1].spaceGrade,
								'spaceGrade_Type', '')}</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[1].airspace,
								'airspace_Type', '')}</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			<!--单程-->
			<c:if test="${airticketReturnDetailInfoMap.activityAirType == 3}">
				<span style="color:#009535; font-size:16px; font-weight:bold;">单程</span>
				<div class="mod_information_d7"></div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.departureCity,
								'from_area', '')}</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">${fns:findAreaNameById(airticketReturnDetailInfoMap.arrivedCity)}</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.reservationsNum}人</td>
						</tr>
					</tbody>
				</table>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(airticketReturnDetailInfoMap.flightInfoList[0].leaveAirport)}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${fns:getAirportName(airticketReturnDetailInfoMap.flightInfoList[0].destinationAirpost)}</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
<!-- 							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[0].startTime}</td> -->
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
<!-- 							<td class="mod_details2_d2" colspan="3">${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}</td> -->
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[0].airlines,
								'traffic_name', '')}</td>
							<td class="mod_details2_d1">舱位等级：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[0].spaceGrade,
								'spaceGrade_Type', '')}</td>
							<td class="mod_details2_d1">舱位：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.flightInfoList[0].airspace,
								'airspace_Type', '')}</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			<c:if test="${airticketReturnDetailInfoMap.activityAirType == 1 }">
				<!--多段-->
				<span style="color:#009535; font-size:16px; font-weight:bold;">多段</span>
				<div class="mod_information_d7"></div>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发城市：</td>
							<td class="mod_details2_d2">${fns:getDictLabel(airticketReturnDetailInfoMap.departureCity,
								'from_area', '')}</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">${fns:findAreaNameById(airticketReturnDetailInfoMap.arrivedCity)}
							</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.reservationsNum}人</td>
						</tr>
					</tbody>
				</table>
				<c:forEach items="${airticketReturnDetailInfoMap.flightInfoList }" var="flightInfo">
					<div class="title_samil">第${flightInfo.number}段：
						<c:choose>
					 		<c:when test="${flightInfo.ticket_area_type == 1 }">内陆</c:when>
					 		<c:when test="${flightInfo.ticket_area_type == 2 }">国际</c:when>
					 		<c:when test="${flightInfo.ticket_area_type == 3 }">内陆+国际</c:when>
					 		<c:when test="${flightInfo.ticket_area_type == 4 }">国内</c:when>
					 	</c:choose>
					</div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${fns:getAirportName(flightInfo.leaveAirport)}</td>
								<!-- ${flightInfo.leaveAirport} -->
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${fns:getAirportName(flightInfo.destinationAirpost)}</td>
								<!-- ${flightInfo.destinationAirpost} -->
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
<!-- 								<td class="mod_details2_d2">${flightInfo.startTime}</td> -->
								<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
								<td class="mod_details2_d1">到达时刻：</td>
<!-- 								<td class="mod_details2_d2" colspan="3">${flightInfo.arrivalTime}</td> -->
								<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">航空公司：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airlines,
									'traffic_name', flightInfo.airlines)}</td>
								<td class="mod_details2_d1">舱位等级：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,
									'spaceGrade_Type', '')}</td>
								<td class="mod_details2_d1">舱位：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,
									'airspace_Type', '')}</td>
							</tr>
						</tbody>
					</table>
				</c:forEach>
			</c:if>
			<div class="mod_information_d7"></div>
			<ul class="ydbz_dj specialPrice">
				<li style="display: none;"><input type="text" class="required"
					value="0" onafterpaste="this.value=this.value.replace(/\D/g,'')"
					onkeyup="this.value=this.value.replace(/\D/g,'')"
					id="orderPersonelNum"></li>
				<li><span class="ydtips">单价</span>
					<p>
						成人：<font color="#FF0000">${fns:getCurrencyNameOrFlag(airticketReturnDetailInfoMap.currency_id,"0")}${airticketReturnDetailInfoMap.settlementAdultPrice}</font>
						
					</p>
					<p>
						儿童：<font color="#FF0000">${fns:getCurrencyNameOrFlag(airticketReturnDetailInfoMap.currency_id,"0")}${airticketReturnDetailInfoMap.settlementcChildPrice}</font>
						
					</p>
					<p>
						特殊人群：<font color="#FF0000">${fns:getCurrencyNameOrFlag(airticketReturnDetailInfoMap.currency_id,"0")}${airticketReturnDetailInfoMap.settlementSpecialPrice}</font>
						
					</p></li>
				<li><span class="ydtips"> 出行人数</span>
					<p>
						成人：<span>${airticketReturnDetailInfoMap.adult_num}</span> 人
					</p>
					<p>
						儿童：<span>${airticketReturnDetailInfoMap.child_num}</span> 人
					</p>
					<p>
						特殊人群：<span>${airticketReturnDetailInfoMap.special_num}</span> 人
					</p></li>
				<li class="ydbz_single"><span class="">税费：</span>${fns:getCurrencyNameOrFlag(airticketReturnDetailInfoMap.currency_id,"0")}${airticketReturnDetailInfoMap.taxamt}/人
				</li>
			</ul>
		</div>
	</div>
	<c:if test="${isAllowMultiRebateObject == 1 }">
			<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					<div class="ydbz_tit"><span class="fl">宣传费对象</span></div>
				</c:when>
				<c:otherwise>
					<div class="ydbz_tit"><span class="fl">返佣对象</span></div>
				</c:otherwise>
			</c:choose>   
	    
	    <c:choose>
		    <c:when test="${not empty accountInfo}">
			    <div>
			        <span><label>对象类型：</label><span>供应商</span></span>
			        <span class="rabateInfo"><label>供应商名称：</label><span>${accountInfo.name }</span></span>
			        <span class="rabateInfo"><label>账户类型：</label><span>${accountInfo.platType }</span></span>
			        <span class="rabateInfo"><label>开户行名称：</label><span>${accountInfo.bankName }</span></span>
			        <span class="rabateInfo"><label>账户号码：</label><span>${accountInfo.bankAccountCode }</span></span>
		        </div>
		    </c:when>
		    <c:otherwise>
		    	 <span><label>对象类型：</label><span>渠道</span></span>
		    	 <span class="rabateInfo"><label>渠道名称：</label><span>${rebateObject}</span></span>
		    </c:otherwise>
	    </c:choose>
    </c:if>
    <div class="ydbz_tit">
    	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					<span class="fl">宣传费审批</span>
				</c:when>
				<c:otherwise>
					<span class="fl">返佣审批</span>
				</c:otherwise>
			</c:choose>   
    	
    </div>
	<%-- <div class="ydbz_tit"><span class="fl"><c:if test="${not empty rebates.traveler}">个人</c:if><c:if test="${empty rebates.traveler}">团队</c:if>返佣</span></div> --%>
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
			 <th width="12%">应收金额</th>
			 <!-- add by jiangyang -->
			 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					 <th width="12%">预计宣传费金额</th>
					 <th width="13%">宣传费金额</th>
					 <th width="12%">累计宣传费金额</th>
				</c:when>
				<c:otherwise>
					 <th width="12%">预计返佣金额</th>
					 <th width="13%">返佣金额</th>
					 <th width="12%">累计返佣金额</th>
				</c:otherwise>
			</c:choose>   
			
<!-- 			 <th width="20%">备注</th> -->
		  </tr>
	  	</thead>
	   	<tbody>
	   	 <c:forEach items="${rebates}" var="rebates" varStatus="i">
		  <tr>
			 <td >
			   <c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if>
			 </td>
             <td>${rebates.currency.currencyName}</td>
			 <td class="tr">${rebates.costname}</td>
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.totalMoney,2)}</td>
			 <!-- add start by jiangyang -->
             <td class="tr">
             	<c:if test="${empty rebates.traveler }"><c:if test="${empty groupRebate }">——</c:if>${groupRebate }</c:if>             	
             	<c:if test="${not empty rebates.traveler }">
             		<c:forEach var="travelInfo" items="${travelInfoList }" >
             			<c:if test="${travelInfo.id == rebates.travelerId }"><c:if test="${empty travelInfo.travelerRebate }">——</c:if>${travelInfo.travelerRebate }</c:if>
             		</c:forEach>
             	</c:if>
             </td>
             <!-- add end   by jiangyang -->
             <td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
			 <td class="tr">
			  <c:if test="${not empty rebates.traveler}">${rebates.rebatesdiffString1}</c:if>
			  <c:if test="${empty rebates.traveler}">${teamMonery }</c:if>
			 </td>
           <%--   <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.newRebates,2)}</td> --%>
<!-- 			 <td>${rebates.remark}</td> -->
		  </tr>
		  </c:forEach>
	   </tbody>
	</table>
	<div class="ydbz_tit"><span class="fl">备注</span></div>
    <dl class="gai-price-tex"><dd>${bremarks}</dd></dl> 
	<div class="allzj tr f18">
      	<%-- 原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />--%>
        <%-- 	返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br /> --%> 
       <c:set value="${currencyName}" var="currencyNames" />
       <c:set value=" ${rebatesDiff}" var="rebatesDiffs" />
       				 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					  宣传费差额：
				</c:when>
				<c:otherwise>
					    返佣差额：
				</c:otherwise>
			</c:choose>   
             <c:forEach items="${fn:split(rebatesDiffs, ',')}" var="rebatesDiff" varStatus="i">    
          <font class="f14">
          ${fns:getCurrencyNameOrFlag(fn:split(currencyNames, ',')[i.index],"1")}
          </font>
          <span class="f20">
          <fmt:formatNumber type="currency" pattern="##,###.00" value=" ${rebatesDiff }" />
          </span><br />
      </c:forEach>
        <%-- <div class="all-money">改后返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.newRebates)}</div> --%>
	</div>
	<div class="ydbz_tit">
		<span class="fl">审批动态</span>
	</div>
	<%--审批记录 --%>
	<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>	
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.opener=null;window.close();">关闭</a>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>