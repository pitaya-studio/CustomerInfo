<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审核-退票审批</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
	$(function() {
		//AA码
		AAHover();
		if ("${reply}" != null && "${reply}" != '') {
			alert("${reply}");
		}
	});
	//驳回
	function jbox_bohui1(){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$("#result").val(0);
				$("#denyReason").val(f.reason);
				$("#searchForm").submit();
			}
		},height:250,width:500});
	};
	//审核通过
	function review(){
		$("#result").val(1);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<!--<div class="mod_nav">审核 > 退票申请 > 退票审批</div>-->
	<div class="ydbz_tit">订单详情</div>
	<ul class="ydbz_info">
		<li><span>下单人：</span>${airticketReturnDetailInfoMap.createName}</li>
		<li><span>下单时间：</span>${airticketReturnDetailInfoMap.create_date}</li>
		<li><span>操作人：</span>${airticketReturnDetailInfoMap.createByName}</li>
		<li><span>团队类型：</span>${fns:getDictLabel(airticketReturnDetailInfoMap.product_type_id,
			'order_type', '')}</li>
		<!-- group_type改为了order_type -->
		<li><span>订单编号：</span>${airticketReturnDetailInfoMap.order_no}</li>
		<li><span>订单团号：</span>${airticketReturnDetailInfoMap.group_code}</li>
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
	<div class="ydbz_tit">
		<span class="fl">游客退票</span><span class="fr wpr20">报批日期：<fmt:formatDate
				value="${reviewTraveler.createtime}" pattern="yyyy-MM-dd HH:mm:ss" />
		</span>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th>游客</th>
				<th>下单时间</th>
				<!-- <th>币种</th> -->
				<th>应付金额</th>
				<th>原因备注</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>${reviewTraveler.tname}</td>
				<td class="tc"><fmt:formatDate
						value="${reviewTraveler.orderdate}" pattern="yyyy-MM-dd HH:mm:ss" />
				</td>
				<!-- <td>美</td> -->
				<td class="tr"><span class="tdred">${reviewTraveler.payprice}</span>
				</td>
				<td>${reviewTraveler.createReason}</td>
			</tr>
		</tbody>
	</table>
	<div class="allzj tr f18">
		<div class="all-money">
			退票总金额：<font class="f14"><font color="#FF0000">${reviewTraveler.payprice}</font><!-- 人民币</font><span class="f20">3,450</span> <span class="tdgreen">+</span>  <font class="f14">美 -->
			</font>
			<!-- <span class="f20">650</span> -->
		</div>
	</div>
	<c:if test="${flag == 0}">
		<div class="dbaniu">
		<form id="searchForm" action="${ctx}/airticketreturn/reviewAirticketReturn" method="post">
			<!-- 添加提交请求所需数据 -->
			<input type = "hidden" id = "revid" name="revid" value = "${reviewTraveler.revid}"/>
			<input type = "hidden" id = "nowlevel" name="nowlevel" value = "${reviewTraveler.curlevel}"/>
			<input type = "hidden" id = "result" name="result"/>
			<input type = "hidden" id = "denyReason" name="denyReason"/>
			<input type = "hidden" id = "orderId" name="orderId" value = "${reviewTraveler.orderid}"/>
			<input type = "hidden" id = "travelerId" name="travelerId" value = "${reviewTraveler.tid}"/>
			<input type = "hidden" id = "flag" name="flag" value = "${flag}"/>
			<a class="ydbz_s gray" href = "${ctx}/airticketreturn/airticketReturnReviewList">返回</a> <a class="ydbz_s"
				onclick="jbox_bohui1();">驳回</a> <input type="button" value="审核通过" onclick="review()"
				class="btn btn-primary">
		</form>
		</div>
	</c:if>
	<c:if test="${flag != 0}">
		<div class="dbaniu">
			<a class="ydbz_s gray" href = "${ctx}/airticketreturn/airticketReturnReviewList">返回</a>
		</div>
	</c:if>
	<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
	<!--右侧内容部分结束-->
</body>
</html>
