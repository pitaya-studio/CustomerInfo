<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单-机票-退票-申请退票</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	$(function() {

		$(".table_borderLeftN")
				.delegate(
						"input[name='allChk']",
						"click",
						function() {
							if ($(this).prop("checked")) {
								// $(".table_borderLeftN").find("input[type='checkbox']")[1].attr("checked",'true');
								for ( var i = 0; i < $(".table_borderLeftN")
										.find("input[type='checkbox']").length; i++) {
									$(".table_borderLeftN").find(
											"input[type='checkbox']")[i].checked = true;
								}
							} else {
								$(".table_borderLeftN").find(
										"input[type='checkbox']").removeAttr(
										"checked");
							}
						});
		//gaijia();
		if ("${reply}" != null && "${reply}" != '') {
			alert("${reply}");
		}
	});
	function idcheckchg(obj) {
		if (obj.checked == true) {
			obj.value = "0";//标示选中的记录
		} else {
			obj.value = "1";//标示未选中的记录
		}
	};
	function sendData(obj) {
		if($("#succBtnTp")[0].disabled == true){
			return false;
		}
		$("#succBtnTp").attr('disabled',true);
		var str = "";
		var checkFlag = false;
		var traStr = new Array();
		var n = 0;
		var num = 0;
		$("input[name='activityId']").each(function() {
			if (this.checked == true || this.checked == "checked") {
				//str+=$(this).val()+",";
				str += "1" + ",";
				traStr[n] = $("input[name='travelerId']")[num].value;
				n = n + 1;
				checkFlag = true;
			} else {
				str += "0,";
			}
			num = num + 1;
		});
		if (!checkFlag) {
			$.jBox.tip("必须选择一个游客进行退票!");
			$("#succBtnTp").attr('disabled',false);
			return;
		}
		$("input[name='activityIds']").val(str);
		//校验审核互斥
		var tras = "";
		for(var i = 0; i < traStr.length; i++){
			if(i==0){
				tras += traStr[i];
			} else {
				tras += "," + traStr[i];
			}
		}
		if(!validateReview(tras)){
			return false;
		}
	};
	function validateReview(str) {
		var orderid = $("input[name='orderId']").val();
// 		for(var i = 0; i < str.length; i++){
// 			var travelerId = str[i];
				$.ajax({
		            type: "POST",
		            url: "${ctx}/refundReview/beforeAddReview",
		            data: {
		            orderId : orderid,
		            travelerids : str,
		            reviewFlowId : 3//退票		            
		            },
		            async : false,
		            success: function(msg){
		            	if(msg.result == ''){
		            		$("#airticketReturnDetailForm_Id").submit();
		            		return true;
		            	}else{
		            		top.$.jBox.confirm(msg.result,'系统提示',function(v){
								if(v=='ok'){
										$.ajax({
								            type: "POST",
								            url: "${ctx}/refundReview/cancelOtherReview",
								            data: {
									            orderId : msg.orderId,
									            travelerids : msg.travelerids,
									            reviewFlowId : msg.reviewFlowId//退票		            
								            },
								            async : false,
								            success: function(msg){
								            	if(msg == 'success'){
								            		$("#airticketReturnDetailForm_Id").submit();
								            		return true;
								            	}else{
								            		$("#succBtnTp").attr('disabled',false);
								            		return false;
								            	}
								            }
								        });
									} else {
										$("#succBtnTp").attr('disabled',false);
									}
								},{buttonsFocus:1});
		            	}
		            }
		        });
// 		}
	};
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit">订单详情</div>
	<ul class="ydbz_info">
		<li><span>下单人：</span>${airticketReturnDetailInfoMap.createName}</li>
		<li><span>下单时间：</span><fmt:formatDate value="${airticketReturnDetailInfoMap.create_date}" pattern="yyyy-MM-dd HH:mm:ss"/></li>
		<li><span>操作人：</span>${airticketReturnDetailInfoMap.createByName}</li>
		<li><span>团队类型：</span>${fns:getDictLabel(airticketReturnDetailInfoMap.product_type_id,
			'order_type', '')}</li>
		<!-- group_type改为了order_type -->
		<li><span>订单编号：</span>${airticketReturnDetailInfoMap.order_no}</li>
		<li><span>订单团号：</span>${airticketReturnDetailInfoMap.group_code}</li>
		<li><span>接客人：</span>${airticketReturnDetailInfoMap.createName}</li>
	</ul>
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
							<td class="mod_details2_d2">
							${fns:getDictLabel(airticketReturnDetailInfoMap.departureCity, 'from_area', '')}
							</td>
							<td class="mod_details2_d1">到达城市：</td>
							<td class="mod_details2_d2">${fns:findAreaNameById(airticketReturnDetailInfoMap.arrivedCity)}
							</td>
							<td class="mod_details2_d1">预收人数：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.reservationsNum}人</td>
							<td class="mod_details2_d1">余位：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.freePosition}人</td>
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
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getDictDescription(airticketReturnDetailInfoMap.flightInfoList[0].airlines, 'traffic_name', airticketReturnDetailInfoMap.flightInfoList[0].airlines)}</td>
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
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[1].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[1].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getDictDescription(airticketReturnDetailInfoMap.flightInfoList[1].airlines, 'traffic_name', airticketReturnDetailInfoMap.flightInfoList[1].airlines)}</td>
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
							<td class="mod_details2_d1">余位：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.freePosition}人</td>
						</tr>
					</tbody>
				</table>
				<table width="90%" border="0">
					<tbody>
						<tr>
							<td class="mod_details2_d1">出发机场：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[0].leaveAirport_name}</td>
							<td class="mod_details2_d1">到达机场：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.flightInfoList[0].destinationAirpost_name}</td>
						</tr>
						<tr>
							<td class="mod_details2_d1">出发时刻：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[0].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getDictDescription(airticketReturnDetailInfoMap.flightInfoList[0].airlines, 'traffic_name', airticketReturnDetailInfoMap.flightInfoList[0].airlines)}</td>
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
							<td class="mod_details2_d1">余位：</td>
							<td class="mod_details2_d2">${airticketReturnDetailInfoMap.freePosition}人</td>
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
								<td class="mod_details2_d2">${flightInfo.leaveAirport_name}</td>
								<!-- ${flightInfo.leaveAirport} -->
								<td class="mod_details2_d1">到达机场：</td>
								<td class="mod_details2_d2">${flightInfo.destinationAirpost_name}</td>
								<!-- ${flightInfo.destinationAirpost} -->
							</tr>
							<tr>
								<td class="mod_details2_d1">出发时刻：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">航空公司：</td>
								<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines, 'traffic_name', flightInfo.airlines)}</td>
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
						成人：<font color="#FF0000">¥${airticketReturnDetailInfoMap.settlementAdultPrice}</font>
						
					</p>
					<p>
						儿童：<font color="#FF0000">¥${airticketReturnDetailInfoMap.settlementcChildPrice}</font>
						
					</p>
					<p>
						特殊人群：<font color="#FF0000">¥${airticketReturnDetailInfoMap.settlementSpecialPrice}</font>
						
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
				<li class="ydbz_single"><span class="">税费：</span><font color="#FF0000">¥${airticketReturnDetailInfoMap.taxamt}</font>/人
				</li>
			</ul>
		</div>
	</div>
	<div class="ydbz_tit orderdetails_titpr">游客退票（温馨提示：可选择的游客为可退票的游客。游客不可选的原因：1.游客已退票 2.游客未付款）</div>
	<form id="airticketReturnDetailForm_Id" method="post"
		action="${ctx}/airticketreturn/submitAirticketReturnReq">

		<table id="contentTable" class="table activitylist_bodyer_table">
			<thead>
				<tr>
					<th class="table_borderLeftN" width="10%">全选<input
						name="allChk" type="checkbox">
					</th>
					<!-- onclick="checkall(this)" -->
					<th width="15%">游客</th>
					<th width="15%">下单时间</th>
					<!-- <th width="15%">报批日期</th> -->
					<th width="15%">应付金额</th>
					<th width="30%">退票原因</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${airticketReturnDetailInfoMap.travelInfoList}"
					var="travelInfo">
					<tr>
						<c:if test="${travelInfo.isAirticketFlag == '1'}">
							<td class="table_borderLeftN"><input name="activityId"
								type="checkbox" /><br><br>
							</td>
						</c:if>
						<c:if test="${travelInfo.isAirticketFlag==null || travelInfo.isAirticketFlag=='0'}">
							<td class="table_borderLeftN">
							</td>
						</c:if>
						<td>${travelInfo.name}<input type="hidden" name='travelerId' value="${travelInfo.id}" />
						</td>
						<input type="hidden" name='reviewId'
							value="${travelInfo.reviewId ? travelInfo.reviewId : 0}" />
						<td class="tc"><fmt:formatDate value="${travelInfo.create_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						<td class="tr">
							<!-- ¥ -->
							<span class="tdred">${travelInfo.payPrice}</span>
						</td>
						<td><input type="text" name='returnReason' />
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<input name="orderId" type="hidden"
			value="${airticketReturnDetailInfoMap.id }" /> <input
			name="activityIds" type="hidden" /> <input name="productType"
			type="hidden"
			value="${airticketReturnDetailInfoMap.product_type_id }" />
		<!-- 产品类型id -->
		<input name="flowType" type="hidden" value="3" />
		<!-- 流程类型  3代表退票 -->
		<div class="ydBtn ydBtn2">
			<a class="ydbz_s gray" href="${ctx}/airticketreturn/airticketReturnList?orderId=${airticketReturnDetailInfoMap.id}&flowType=3&orderType=7">取消</a>
				<input id = "succBtnTp" type="button" value="提交" onclick="sendData(this);" class="btn btn-primary">
		</div>
		<!-- href = "sendData(this)" -->
	</form>
</body>
</html>
