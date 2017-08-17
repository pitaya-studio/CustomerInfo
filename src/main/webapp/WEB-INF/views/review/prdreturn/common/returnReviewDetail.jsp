<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title><c:if test="${processType eq '3' }">退票</c:if><c:if test="${processType eq '8' }">退团</c:if>-<c:if test="${flag == 0}">审批</c:if><c:if test="${flag != 0}">详情</c:if></title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />


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
	function jbox_bohui1(revid,orderid,tid,processType){
		$("#failBtn").attr('disabled',true);
		$("#succBtn").attr('disabled',true);
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$.ajax({
					type : "post",
					url : "${ctx}/prdreturn/returnreview",
					data : {
						"revid" : revid, 
						"result" : '0', 
						"denyReason" : f.reason, 
						"orderId" : orderid, 
						"travelerId" : tid,
						"processType": processType
					},
					success : function(data){
						if(data.flag == 'success'){
							window.opener.$("#searchForm").submit();
							window.close();
						} else {
							$.jBox.tip(data.msg);
							$("#failBtn").attr('disabled',false);
							$("#succBtn").attr('disabled',false);
						}
					}
				})
			}else {
				$("#failBtn").attr('disabled',false);
				$("#succBtn").attr('disabled',false);
			}
		},closed:function(){
			$("#succBtn").attr('disabled',false);
			$("#failBtn").attr('disabled',false);
		},height:250,width:500});
	};
	//审核通过
	function review(revid,orderid,tid,processType){
		$("#failBtn").attr('disabled',true);
		$("#succBtn").attr('disabled',true);
		var html = '<div class="add_allactivity"><label>请填写您的备注!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$.ajax({
					type : "post",
					url : "${ctx}/prdreturn/returnreview",
					data : {
						"revid" : revid,
						"result" : '1', 
						"denyReason" : f.reason, 
						"orderId" : orderid, 
						"travelerId" : tid,
						"processType": processType
					},
					success : function(data){
						if(data.flag == 'success'){
							window.opener.$("#searchForm").submit();
							window.close();
						} else {
							$.jBox.tip("审核失败");
							$("#failBtn").attr('disabled',false);
							$("#succBtn").attr('disabled',false);
						}
					}
				})
			}else {
				$("#failBtn").attr('disabled',false);
				$("#succBtn").attr('disabled',false);
			}
		},closed:function(){
			$("#succBtn").attr('disabled',false);
			$("#failBtn").attr('disabled',false);
		},height:250,width:500});
	
	}
</script>
<style type="text/css">
.allzj {
	border-top : none;
}


</style>
</head>
<body>
	<!--右侧内容部分开始-->
	<!--<div class="mod_nav">审核 > 退票申请 > 退票审批</div>-->
	<c:if test="${processType eq '3' }">
	<div class="ydbz_tit">订单详情</div>
	<ul class="ydbz_info">
		<!-- 退票 -->
		<li><span>下单人：</span>${airticketReturnDetailInfoMap.createName}</li>
		<li><span>下单时间：</span>${airticketReturnDetailInfoMap.create_date}</li>
		<li><span>操作人：</span>${airticketReturnDetailInfoMap.createByName}</li>
		<li><span>团队类型：</span>${fns:getDictLabel(airticketReturnDetailInfoMap.product_type_id,'order_type', '')}</li>
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
							<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(airticketReturnDetailInfoMap.flightInfoList[0].airlines)}</td>
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
							<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(airticketReturnDetailInfoMap.flightInfoList[1].airlines)}</td>
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
							<td class="mod_details2_d2"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[1].startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="mod_details2_d1">到达时刻：</td>
							<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${airticketReturnDetailInfoMap.flightInfoList[1].arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						</tr>
						<tr>
							<td class="mod_details2_d1">航空公司：</td>
							<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(airticketReturnDetailInfoMap.flightInfoList[0].airlines)}</td>
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
					<c:choose>
				      <c:when test="${flightInfo.ticket_area_type==1}">内陆</c:when>
					  <c:when test="${flightInfo.ticket_area_type==2}">国际</c:when>
					  <c:when test="${flightInfo.ticket_area_type==3}">内陆+国际</c:when>
					  <c:when test="${flightInfo.ticket_area_type==4}">国内</c:when>
				   </c:choose>
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
								<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td class="mod_details2_d1">到达时刻：</td>
								<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">航空公司：</td>
								<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
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
							成人：<font color="#FF0000">${fns:getCurrencyInfo(airticketReturnDetailInfoMap.currid, 0, 'mark')}${airticketReturnDetailInfoMap.settlementAdultPrice}</font>
							
						</p>
						<p>
							儿童：<font color="#FF0000">${fns:getCurrencyInfo(airticketReturnDetailInfoMap.currid, 0, 'mark')}${airticketReturnDetailInfoMap.settlementcChildPrice}</font>
							
						</p>
						<p>
							特殊人群：<font color="#FF0000">${fns:getCurrencyInfo(airticketReturnDetailInfoMap.currid, 0, 'mark')}${airticketReturnDetailInfoMap.settlementSpecialPrice}</font>
							
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
						</p>
					</li>
					<li class="ydbz_single"><span class="">税费：</span><font color="#FF0000">${fns:getCurrencyInfo(airticketReturnDetailInfoMap.currid, 0, 'mark')}${airticketReturnDetailInfoMap.taxamt}</font>/人
				</li>
			</ul>
		</div>
	</div>
	</c:if>
	<c:if test="${processType eq '8' }">
		<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
	</c:if>
	<div class="ydbz_tit">
		<c:if test="${processType eq '3' }">
			<span class="fl">游客退票</span>
		</c:if>
		<c:if test="${processType eq '8' }">
			<span class="fl">游客退团</span>
		</c:if>
		<span class="fr wpr20">报批日期：<fmt:formatDate value="${reviewInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss" />
		</span>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th>序号</th>
				<th>游客</th>
				<th>下单时间</th>
				<c:if test="${airticketReturnDetailInfoMap.product_type_id eq 7 }">
					<th>应收金额</th>
				</c:if>
				<c:if test="${airticketReturnDetailInfoMap.product_type_id ne 7 }">
					<th>应收金额</th>
					<th>退团后应收</th>
					<th>退款金额</th>
				</c:if>
				<th>退团原因</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>${traveler.name}</td>
				<td class="tc">
					<c:if test="${processType eq '3' }">
						<fmt:formatDate value="${airticketReturnDetailInfoMap.create_date}" pattern="yyyy-MM-dd HH:mm:ss" />
					</c:if>
					<c:if test="${processType eq '8' }">
						<fmt:formatDate value="${productOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss" />
					</c:if>
				</td>
				<!-- forbug15438 退团审批成功之后 应收金额显示不正确 如果为空显示¥0,00-->
				<td class="tr"><span class="tdgreen">
					<c:choose>
						<c:when test="${empty payPrice }">¥0,00</c:when>
						<c:otherwise>${payPrice}</c:otherwise>
					</c:choose>
				</span></td>
				<c:if test="${airticketReturnDetailInfoMap.product_type_id ne 7 }">	
					<td class="tr"><span class="tdgreen">
						<c:choose>
							<c:when test="${empty reviewInfo.afterString }">¥0,00</c:when>
							<c:otherwise>${reviewInfo.afterString}</c:otherwise>
						</c:choose></span></td>
					<td class="tr"><span class="tdgreen">
						<c:choose>
							<c:when test="${empty reviewInfo.refundString }">¥0,00</c:when>
							<c:otherwise>${reviewInfo.refundString}</c:otherwise>
						</c:choose></span></td>
				</c:if>
				<td>${reviewInfo.remark}</td>
			</tr>
		</tbody>
	</table>
	<div class="allzj tr f18">
		<div class="all-money">
			<c:if test="${processType eq '3' }">
				退票总金额：<font class="f14"><font color="#FF0000">
					<c:choose>
						<c:when test="${empty payPrice }">¥0,00</c:when>
						<c:otherwise>${payPrice}</c:otherwise>
					</c:choose></font></font>
			</c:if>
			<c:if test="${processType eq '8' }">
				退团总金额：<font class="f14"><font color="#FF0000">
					<c:choose>
						<c:when test="${empty payPrice }">¥0,00</c:when>
						<c:otherwise>${payPrice}</c:otherwise>
					</c:choose></font></font>
			</c:if>
		</div>
	</div>
	<div class="ydbz_tit"><span class="fl">审批动态</span></div>
	<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>


	<c:if test="${flag == 0}">
		<div class="dbaniu">
		<form id="searchForm" action="${ctx}/prdreturn/returnreview" method="post">
			<!-- 添加提交请求所需数据 -->
			<input type = "hidden" id = "revid" name="revid" value = "${reviewInfo.id}"/>
			<input type = "hidden" id = "result" name="result"/>
			<input type = "hidden" id = "denyReason" name="denyReason"/>
			<input type = "hidden" id = "orderId" name="orderId" value = "${reviewInfo.orderId}"/>
			<input type = "hidden" id = "travelerId" name="travelerId" value = "${reviewInfo.travellerId}"/>
			<input type = "hidden" id = "flag" name="flag" value = "${flag}"/>
			<a class="ydbz_s gray" href = "javascript:window.close();">关闭</a><a class="ydbz_s" id = "failBtn"
				onclick="jbox_bohui1('${reviewInfo.id}','${reviewInfo.orderId}','${reviewInfo.travellerId}','${processType }');">驳回</a> 
				<input type="button" id = "succBtn" value="审批通过" onclick="review('${reviewInfo.id}','${reviewInfo.orderId}','${reviewInfo.travellerId}',
						'${processType }')" class="btn btn-primary"/>
		</form>
		</div>
	</c:if>
	<c:if test="${flag != 0}">
		<div class="dbaniu">
			<a class="ydbz_s gray" href = "javascript:window.close();">关闭</a>
		</div>
	</c:if>
</body>
</html>
