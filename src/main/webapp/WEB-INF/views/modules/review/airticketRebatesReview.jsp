<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>机票返佣审核</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
	function jbox_bohui(rid,userLevel){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/order/rebates/review/reviewRebates",
		            data: {
			            rid: rid,
			            result: 0,
			            userLevel: userLevel,
			            denyReason: $("#denyReason").val()		            
		            },
		            success: function(msg){
		            	if("success" == msg){
		            		$("input[name='review']").attr('disabled',"true");
	           				$("input[name='bohui']").attr('disabled',"true");
	           				window.opener.location.href = window.opener.location.href;
		            		window.close();
		            	}else{
		            		jBox.tip("操作失败");
		            	}
		            }
		        });
			}
		},height:250,width:500});
	}
	function reviewPass(rid,userLevel){
		$.ajax({
           type: "POST",
           url: "${ctx}/order/rebates/review/airticketReviewRebates",
           data: {
	           rid: rid,
	           result: 1,
	           userLevel: userLevel,
	           denyReason: ""		            
           },
           success: function(msg){
	           	if("success" == msg){
	           		$("input[name='review']").attr('disabled',"true");
	           		$("input[name='bohui']").attr('disabled',"true");
	           		jBox.tip("操作成功");
	           		window.opener.location.href = window.opener.location.href;
	           		window.close();
	           	}else{
	           		jBox.tip("操作失败");
	           	}
           }
       });
	}
	</script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
	    <page:param name="desc">返佣审批</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
    <div class="mod_nav">审核 > 返佣申请 > 返佣审批</div>
	<div class="ydbz_tit">订单信息</div>
	<input type="hidden" value="${productOrder.id}" id="orderId"/>
	<input type="hidden" value="${productOrder.productTypeId}" id="orderType"/>
    <div class="orderdetails1">
	   	<table border="0" style="margin-left: 25px" width="98%">
			<tbody>
				<tr>
					<td class="mod_details2_d1">下单人：</td>
			        <td class="mod_details2_d2">${productOrder.createBy.name}</td>
					<td class="mod_details2_d1">下单时间：</td>
					<td class="mod_details2_d2"><fmt:formatDate value="${productOrder.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
		            <td class="mod_details2_d1">收客人：</td>
			        <td class="mod_details2_d2">${productOrder.createBy.name}</td> 
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
					<td class="mod_details2_d1">销售：</td>
			        <td class="mod_details2_d2">${productOrder.salerName}</td>
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
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[0].startTime}</td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3">${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}</td>
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
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[1].startTime}</td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3">${airticketReturnDetailInfoMap.flightInfoList[1].arrivalTime}</td>
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
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[0].startTime}</td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3">${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}</td>
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
				<c:forEach items="${airticketReturnDetailInfoMap.flightInfoList }"
					var="flightInfo">
					<div class="title_samil">第${flightInfo.number}段：</div>
					<!-- ${flightInfo.ticket_area_type} -->
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
								<td class="mod_details2_d2">${flightInfo.startTime}</td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2" colspan="3">${flightInfo.arrivalTime}</td>
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
						成人：<font color="#FF0000">￥${airticketReturnDetailInfoMap.settlementAdultPrice}</font>
						
					</p>
					<p>
						儿童：<font color="#FF0000">￥${airticketReturnDetailInfoMap.settlementcChildPrice}</font>
						
					</p>
					<p>
						特殊人群：<font color="#FF0000">￥${airticketReturnDetailInfoMap.settlementSpecialPrice}</font>
						
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
				<li class="ydbz_single"><span class="">税费：</span><font color="#FF0000">￥${airticketReturnDetailInfoMap.taxamt}</font>/人
				</li>
			</ul>
		</div>
	</div>


    <div class="ydbz_tit"><span class="fl">返佣审批</span></div>
	<%-- <div class="ydbz_tit"><span class="fl"><c:if test="${not empty rebates.traveler}">个人</c:if><c:if test="${empty rebates.traveler}">团队</c:if>返佣</span></div> --%>
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
			 <th width="12%">应收金额</th>
			 <th width="12%">预计返佣金额</th>
			 <th width="12%">累计返佣金额</th>
			 <th width="13%">返佣差额</th>
			 <th width="20%">备注</th>
		  </tr>
	  	</thead>
	   	<tbody>
	   	 <c:forEach items="${rebates}" var="rebates" varStatus="i">
		  <tr>
			 <td >
			   <c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if>
			 </td>
             <td>${rebates.currency.currencyName}</td>
			 <td class="tr">${paymentProject}</td>
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.totalMoney,2)}</td>
			 <td class="tr">${rebates.rebatesStr}</td>
			 <td class="tr">${fns:getOldRebatesMoneyAmountBySerialNum(rebates.oldRebates)}</td>
           <%--   <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.newRebates,2)}</td> --%>
             <td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
			 <td>${rebates.remark}</td>
		  </tr>
		  </c:forEach>
	   </tbody>
	</table>
	<div class="allzj tr f18">
      	<%-- 原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      	返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />--%> 
        <%-- <div class="all-money">改后返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.newRebates)}</div> --%>
        <c:set value="${currencyName}" var="currencyNames" />
       <c:set value=" ${rebatesDiff}" var="rebatesDiffs" />
                    返佣差额：<c:forEach items="${fn:split(rebatesDiffs, ',')}" var="rebatesDiff" varStatus="i">
          <font class="f14">
          ${fns:getCurrencyNameOrFlag(fn:split(currencyNames, ',')[i.index],"1")}
          </font>
          <span class="f20">
          <fmt:formatNumber type="currency" pattern="##,###.00" value=" ${rebatesDiff }" />
          </span><br />
      </c:forEach>
	</div>
	<div class="ydbz_tit">
		<span class="fl">审核动态</span>
	</div>
	<ul class="spdtai">
		<li><fmt:formatDate value="${rebates[0].createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> 【${rebates[0].createBy.name}】申请返佣</li>
		<c:forEach items="${reviewLogList}" var="reviewLog">
			<li><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> 【${fns:getUserById(reviewLog.createBy).name}】${reviewLog.result}</li>
		</c:forEach>
	</ul>
	<%-- <c:if test="${rebates.review.status == 0}">
		<div class="ydbz_tit">
			<span class="fl">驳回理由</span>
		</div>
		<ul class="spdtai">
			<li>${rebates.review.denyReason}</li>
		</ul>
	</c:if> --%>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">关闭</a>
		<input type="button" name="bohui" value="驳回" class="btn btn-primary" onclick="jbox_bohui(${rebates[0].review.id},${userLevel});">
		<input type="button" name="review" value="审核通过" class="btn btn-primary" onclick="reviewPass(${rebates[0].review.id},${userLevel});">
	</div>
	<!--右侧内容部分结束-->
</body>
</html>