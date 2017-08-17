<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-机票-退款-申请退款</title>

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${ctxStatic}/modules/order/airticket/airticketOrderRefund.js"></script>
	<script src="${ctxStatic}/modules/order/orderList.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {
		g_context_url = "${ctx}";
		currencyObj = {};
		loadCurrency();
		bind();
		//input获得失去焦点提示信息显示隐藏
		inputTips();
		//退款
		refunds();
		changeRefund();
		totalRefund();
		inputTips();
	});
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<!--币种模板开始-->
	<select name="currencyTemplate" id="currencyTemplate"
		style="display:none;">
	</select>
	<input type="hidden" value="${orderDetail.currencyId}"
		id="orderCurrencyId">
	<input type="hidden" value="${orderId }" id="orderId">
	<input type="hidden" value="${orderDetail.totalMoney }" id="orderPrice">
	<input type="hidden" value="${reviewId }" id="reviewId" data-type="${reviewObj.currencyId }">
	<!--币种模板结束-->
	<div class="mod_nav">订单 > 机票 > 申请退款</div>
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
                    </tr><tr>
                    	        
                </tr>
            </tbody>
        </table>
    </div>
	<div class="ydbzbox fs" style="overflow:hidden;">
		<div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden;">
		   <p class="ydbz_mc">
		   ${fns:getDictLabel(orderDetail.departureCity, 'from_area', '')}
							—
		   <c:forEach items="${arrivedareas }" var="v">
				<c:if test="${v.id == orderDetail.arrivedCity }">${v.name }</c:if>
		   </c:forEach>
		 
		   ：
		   
			<c:choose>
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
		<c:if test="${(reviewId != null && reviewObj.travelerId != 0) || reviewId == null}">
		
		<div class="ydbz_tit">游客退款</div>
		<table id="contentTable" class="table activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="10%">游客</th>
					<th width="20%">退款款项</th>
					<th width="10%">币种</th>
					<th width="20%">应收金额</th>
					<th width="20%">金额</th>
					<th width="20%">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${reviewId != null && reviewObj.travelerId != 0 }">
					<tr>
						<td width="10%">${reviewObj.travelerName} 
						<input  type="hidden" name="travelerId" value="${reviewObj.travelerId }" />
						<input type="hidden" name="travelerName" value="${reviewObj.travelerName }" /> 
						<input type="hidden" name="payPrice" value="${reviewObj.payPrice }" /></td>
						<td width="90%" colspan="5" class="p0" style="border:none">
							<table class="refundTable" style="border:none" width="100%">
								<tr>
									<td width="22%" class="refundtd"><input type="text"
										class="inputTxt" name="refundName"
										value="${reviewObj.refundName }" />
									<div class="pr">
											
										</div>
									</td>
									<td class="tc" width="11%"><select style="width:90%;"
										class="selectrefund" name="currency">
									</select></td>
									<td class="tr" width="22%"><span class="tdgreen">${reviewObj.payPrice }
									</span></td>
									<td width="22%"><input type="text" name="refund"
										value="${reviewObj.refundPrice }" onkeyup="refundInput(this)"
										onafterpaste="refundInput(this))" onblur="refundInputs(this)">
									</td>
									<td width="22%"><input type="text" name="remark"
										value="${reviewObj.remark }" />
									</td>
								</tr>
							</table></td>
					</tr>
				</c:if>

				<c:if test="${reviewId == null}">
					<c:if test="${fn:length(orderDetail.travelInfoList) == 0 }">
						<tr>
							<td colspan="6" style="text-align: center;">无游客信息</td>
						</tr>
					</c:if>
					<c:forEach items="${orderDetail.travelInfoList }" var="v">
						<tr>
							<td width="10%">${v.travelName } <input type="hidden"
								name="travelerId" value="${v.id }"> <input type="hidden"
								name="travelerName" value="${v.travelName }"> <input
								type="hidden" name="payPrice" value="${v.payPrice }"></td>
							<td width="90%" colspan="5" class="p0" style="border:none">
								<table class="refundTable" style="border:none" width="100%">
									<tr>
										<td width="22%" class="refundtd"><input type="text"
											class="inputTxt" name="refundName" />
										<div class="pr">
												<i title="添加款项" class="gaijia-add"></i>
											</div>
										</td>
										<td class="tc" width="11%"><select style="width:90%;"
											class="selectrefund" name="currency">
										</select></td>
										<td class="tr" width="22%"><span class="tdgreen">${v.payPrice }
										</span></td>
										<td width="22%"><input type="text" name="refund"
											onkeyup="refundInput(this)" onafterpaste="refundInput(this))" onblur="refundInputs(this)">
										</td>
										<td width="22%"><input type="text" name="remark" />
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
		</c:if>
		
		<c:if test="${reviewId == null || (reviewId != null && reviewObj.travelerId == 0)}">
		   <div class="ydbz_tit">团队退款</div>
			<div>
				<ol class="gai-price-ol">
					<c:if test="${reviewId != null && reviewObj.travelerId == 0}">
						<li><i><input type="text" name="refundName"
								value="${reviewObj.refundName }" class="gai-price-ipt1"
								flag="istips" /><span class="ipt-tips ipt-tips2">款项</span>
						</i> <i><select class="selectrefund" name="currency" >
							</select> </i> <i><input type="text" name="refund"
								value="${reviewObj.refundPrice }" onblur="refundInputs(this)" onkeyup="refundInput(this)"
								onafterpaste="refundInput(this))" class="gai-price-ipt1"
								flag="istips" /><span class="ipt-tips ipt-tips2">费用</span>
						</i> <i><input type="text" name="remark" class="gai-price-ipt2"
								flag="istips" value="${reviewObj.remark }" /><span
								class="ipt-tips ipt-tips2">备注</span>
						</i></li>
					</c:if>
					<c:if test="${reviewId == null}">
	
						<li><i><input type="text" name="refundName"
								class="gai-price-ipt1" flag="istips" /><span
								class="ipt-tips ipt-tips2">款项</span>
						</i> <i><select class="selectrefund" name="currency">
							</select> </i> <i><input type="text" name="refund"
								onkeyup="refundInput(this)" onblur="refundInputs(this)" onafterpaste="refundInput(this))"
								class="gai-price-ipt1" flag="istips" /><span
								class="ipt-tips ipt-tips2">费用</span>
						</i> <i><input type="text" name="remark" class="gai-price-ipt2"
								flag="istips" /><span class="ipt-tips ipt-tips2">备注</span>
						</i> <i><a class="ydbz_s refund-price-btn">+增加</a>
						</i></li>
					</c:if>
				</ol>
			</div>
		</c:if>
		<div class="allzj tr f18">
			<div class="all-money">
				退款总金额：<span></span>
			</div>
		</div>
		<div class="ydBtn ydBtn2">
			<a class="ydbz_s gray" id="bt_cancel">取消</a><a class="ydbz_s"
				id="bt_submit">提交</a>
		</div>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
