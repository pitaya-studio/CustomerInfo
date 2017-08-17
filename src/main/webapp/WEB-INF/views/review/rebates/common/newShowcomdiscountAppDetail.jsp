<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>机票返佣详情</title>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<!--驳回，审批通过的相关方法-->
	<script type="text/javascript">
	function jbox_bohui(reviewId){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v == "1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/order/newcomdiscountApprove/singleComdiscountApprove",
		            data: {
		            	reviewId: reviewId,
			            result: 0,
			            remarks: $("#denyReason").val()		            
		            },
		            success: function(msg){
		            	if("success" == msg.flag){
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
	function reviewPass(reviewId){
		$.ajax({
           type: "POST",
           url: "${ctx}/order/newcomdiscountApprove/singleComdiscountApprove",
           data: {
        	   reviewId: reviewId,
	           result: 1,
	           remarks: ""		            
           },
           success: function(msg){
	           	if("success" == msg.flag){
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
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
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
				<c:forEach items="${airticketReturnDetailInfoMap.flightInfoList }"
					var="flightInfo">
					<div class="title_samil">第${flightInfo.number}段：</div>
					<table width="90%" border="0">
						<tbody>
							<tr>
								<td class="mod_details2_d1">出发机场：</td>
								<td class="mod_details2_d2">${fns:getAirportName(flightInfo.leaveAirport)}</td>
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${fns:getAirportName(flightInfo.destinationAirpost)}</td>
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
								<td class="mod_details2_d1">到达时刻：</td>
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
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
			 <th width="12%">应收金额</th>
			 <!-- add by jiangyang -->
			 <th width="12%">预计返佣金额</th>
			 <th width="13%">返佣差额</th>
			 <th width="12%">累计返佣金额</th>
		  </tr>
	  	</thead>
	   	<tbody>
	   	 <c:forEach items="${rebates}" var="rebates" varStatus="i">
		  <tr>
			 <td >${rebates.travelerName}</td>
             <td>${rebates.currencyName}</td>
			 <td class="tr">${rebates.costname}</td>
			 <td class="tr">${rebates.travelerPrice}</td>
			 <!-- add start by jiangyang -->
             <td class="tr">
               	暂不测试
             	<%-- <c:if test="${empty rebates.traveler }"><c:if test="${empty groupRebate }">——</c:if>${groupRebate }</c:if>             	
             	<c:if test="${not empty rebates.traveler }">
             		<c:forEach var="travelInfo" items="${travelInfoList }" >
             			<c:if test="${travelInfo.id == rebates.travelerId }"><c:if test="${empty travelInfo.travelerRebate }">——</c:if>${travelInfo.travelerRebate }</c:if>
             		</c:forEach>
             	</c:if> --%>
             </td>
             <!-- add end   by jiangyang -->
             <td class="tr">${rebates.currency.currencyFlag}${rebates.rebatesDiff}</td>
			 <td class="tr">${rebates.totalAmount}</td>
		  </tr>
		  </c:forEach>
	   </tbody>
	</table>
	<div class="ydbz_tit"><span class="fl">备注</span></div>
      <dl class="gai-price-tex"><dd>${bremarks}</dd></dl> 
	<div class="allzj tr f18">
      	<%-- 原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />--%>
                    返佣差额：${CumulativeAmount}
        <%-- <div class="all-money">改后返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.newRebates)}</div> --%>
	</div>
	<div class="ydbz_tit">
		<span class="fl">审核动态</span>
	</div>
	<ul class="spdtai">
		<c:if test="${not empty reviewLogList}">
			<c:forEach items="${reviewLogList}" var="log" varStatus="s">
			<li>
			   <fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;&nbsp;
			         【${fns:getUserNameById(log.createBy)}】&nbsp;&nbsp; ${log.operationDescription} &nbsp;&nbsp;
			 </li>
			</c:forEach>
		</c:if>
		<c:if test="${empty reviewLogList}">
			<li>暂无审核动态</li>
		</c:if>
	</ul>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.opener=null;window.close();">关闭</a>
	</div>
	
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">关闭</a>
		<input type="button" name="bohui" value="驳回" class="btn btn-primary" onclick="jbox_bohui(${rebates[0].review.id});">
		<input type="button" name="review" value="审核通过" class="btn btn-primary" onclick="reviewPass(${rebates[0].review.id});">
	</div>
	<!--右侧内容部分结束-->
</body>
</html>