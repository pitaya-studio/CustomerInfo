<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<title>订单-单办机票订单修改</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/manageorder.js" type="text/javascript"></script>
<style type="text/css">
input[disabled],select[disabled],textarea[disabled],input[readonly],select[readonly],textarea[readonly],
input[disabled]:focus,select[disabled]:focus,textarea[disabled]:focus,input[readonly]:focus, select[readonly]:focus,textarea[readonly]:focus{
    cursor:auto;
    background:transparent;
    border:0px;
    box-shadow:inset 0 0px 0px rgba(0,0,0,0.075)
}
</style>
<script type="text/javascript">
	
	//页面加载完执行的js
	$(function(){
	
		//各块信息展开与收起
		$(".ydClose").click(function(){
			var obj_this = $(this);
			if(obj_this.attr("class").match("ydExpand")) {
				obj_this.removeClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
			} else {
				obj_this.addClass("ydExpand");
				obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
			}
		});
		
		ydbz2interradio();//联运按钮
	});
	
	
	
	//保存航班备注和预订人信息
	function saveRemark(){
		var orderId = $("#orderId").val();
		var airticketId = $("#airticketId").val();
		var flightRemark = $(":input[name='remark']").val();//航班备注
		
		
		var data = {};
		data['orderId']=orderId;
		data['airticketId']=airticketId;
		data['flightRemark']=flightRemark;
		
		$.ajax({
			type:"POST",
			url:"${ctx}/order/manage/airticketOrderFlightRemark",
			data:data,
			success:function(data){
				if(data.result == "success"){
					$.jBox.tip('保存成功', 'success');
				}
			}
		});
	}
	
</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_modify">
	</page:applyDecorator>
	
	<!--右侧内容部分开始-->
	<div class="ydbzbox fs">
		<div class="tr">
		</div>
		<div class="orderdetails">
		
			<div class="orderdetails_tit">
				<span>1</span>订单信息
				<input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
				<input id="airticketId" type="hidden" value="${orderDetailInfoMap.airticketId }">
			</div>
			
			<!-- 订单信息部分 start -->
			<div class="orderdetails1">
				<table border="0" width="90%" style="margin-left:0;">
					<tbody>
						<tr>
							<td class="mod_details2_d1">下单人：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
							<td class="mod_details2_d1">销售：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.salerName }</td>
							<td class="mod_details2_d1">下单时间：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate }" pattern="yyyy-MM-dd HH:mm"/></td>
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
							<td class="mod_details2_d1">参团订单编号：</td>
							<td class="mod_details2_d2">${porder.orderNum }</td>
							<td class="mod_details2_d1">参团订单团号：</td>
							<td class="mod_details2_d2">${groupNum }</td>
							
						</tr>
						<c:if test="${orderDetailInfoMap.type == 2 }">
							<tr>
								<td class="mod_details2_d1">参团订单编号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td>
								<td class="mod_details2_d1">参团团号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
			<!-- 订单信息部分 end -->
			
			<!-- 航段信息部分 start -->
			<div class="mod_information_dzhan" style="overflow:hidden;">
				<div class="mod_information_dzhan_d mod_details2_d">
						<c:choose>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 2 }">
								<!--往返-->
								<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（往返）</span>
								<div class="mod_information_d7"></div>
								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
											${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">
											<c:forEach items="${arrivedareas}" var="area">
												<c:if test="${area.id eq orderDetailInfoMap.arrivedCity}">
													${area.name}
												</c:if>
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
									<tbody><tr>
										<td class="mod_details2_d1">出发机场：</td>
										<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
										<td class="mod_details2_d1">到达机场：</td>
										<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
									
								</c:forEach> 
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 1 }">
								<!--单程-->
								<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（单程）</span>
								<div class="mod_information_d7"></div>
								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
											${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">
											<c:forEach items="${arrivedareas}" var="area">
												<c:if test="${area.id eq orderDetailInfoMap.arrivedCity}">
													${area.name}
												</c:if>
											</c:forEach>
											</td>
											<td class="mod_details2_d1">预收人数：</td>
											<td class="mod_details2_d2">${orderDetailInfoMap.reservationsNum }人</td>
										 </tr>
									 </tbody>
								 </table>
								 
								 <c:forEach items="${orderDetailInfoMap.flightInfoList}" var="flightInfo">
									<table width="90%" border="0">
									<tbody><tr>
										<td class="mod_details2_d1">出发机场：</td>
										<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
										<td class="mod_details2_d1">到达机场：</td>
										<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
								</c:forEach>
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) >= 3 }">
								<!--多段-->
								<span style="color:#009535; font-size:16px; font-weight:bold;">航段信息（多段）</span>
								<div class="mod_information_d7"></div>
								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
											${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">
											<c:forEach items="${arrivedareas}" var="area">
												<c:if test="${area.id eq orderDetailInfoMap.arrivedCity}">
													${area.name}
												</c:if>
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
									<tbody><tr>
										<td class="mod_details2_d1">出发机场：</td>
										<td class="mod_details2_d2">${flightInfo.startAirportName }</td>
										<td class="mod_details2_d1">到达机场：</td>
										<td class="mod_details2_d2">${flightInfo.endAirportName } </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime }" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2">${fns:getDictDescription(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
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
							<p>成人：<font color="#FF0000">${orderDetailInfoMap.adultPrice }</font> </p>
							<p>儿童：<font color="#FF0000">${orderDetailInfoMap.childPrice }</font> </p>
							<p>特殊人群：<font color="#FF0000">${orderDetailInfoMap.specialPrice }</font> </p>
						</li>
						 <li><span class="ydtips"> 出行人数</span>
							<p>成人：<span>${orderDetailInfoMap.adultNum }</span> 人</p>
							<p>儿童：<span>${orderDetailInfoMap.childNum }</span> 人</p>
							<p>特殊人群：<span>${orderDetailInfoMap.specialNum }</span> 人</p>
						 </li>
						 <li class="ydbz_single">
						 <span class="">税费：</span>${orderDetailInfoMap.taxamt }/人
						 </li>
					</ul>
				</div>
			</div>
			<!-- 航段信息部分 end -->
			
			<div class="orderdetails_tit">
				<span>2</span>机票
			</div>
			
			<!-- 航班备注部分 start -->
			<div flag="messageDiv" class="ydbz2_lxr">
				<p>
					<label style="vertical-align:top;width:200px;text-align:center;">航班备注：</label>
					<textarea name="remark" value="${orderDetailInfoMap.remark}">${orderDetailInfoMap.remark }</textarea>
				</p>
			</div>
			<!-- 航班备注部分 end -->
			
			<div class="orderdetails_tit">
				<span>3</span>预订人信息
			</div>
			
			<!-- 预订人部分start -->
			<div flag="messageDiv">
				<form id="orderpersonMesdtail">
					<p class="ydbz_qdmc">预订渠道：渠道商北中广</p>
					<ul name="orderpersonMes" id="orderpersonMes" class="ydbz_qd">
						<li>
							<label><span class="xing">*</span>渠道联系人：</label>
							<input type="text" disabled="disabled" maxlength="10" id="orderPersonName" value="${orderDetailInfoMap.agentContact }" class="required valid" onkeyup="this.value=this.value.replaceSpecialChars()" onafterpaste="this.value=this.value.replaceSpecialChars()">
						</li>
						<li class="ydbz_qd_lilong">
							<label><span class="xing">*</span>联系人电话：</label>
							<input type="text" disabled="disabled" maxlength="15" id="orderPersonPhoneNum" value="${orderDetailInfoMap.agentTel }" class="required valid" onkeyup="this.value=this.value.replace(/[^\d\+\-]/g,'')" onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')">
						</li>
						<li>
							<label>渠道地址：</label>
							<input type="text" disabled="disabled" maxlength="" id="" value="${orderDetailInfoMap.agentAddress }" class="required">
						</li>
						<li>
							<label>传真：</label>
							<input type="text" disabled="disabled" maxlength="" id="" value="${orderDetailInfoMap.agentFax }" class="required">
						</li>
						<li>
							<label>QQ：</label>
							<input type="text" disabled="disabled" maxlength="" id="" value="${orderDetailInfoMap.agentQQ }" class="required">
						</li>
						<li>
							<label>Email：</label>
							<input type="text" disabled="disabled" maxlength="" id="" value="${orderDetailInfoMap.agentEmail }" class="required">
						</li>
						<li>
							<label>其它：</label>
							<input type="text" disabled="disabled" maxlength="" id="" value="${orderDetailInfoMap.agentRemarks }" class="required">
						</li>
					</ul>
				</form>
			</div>
			<!-- 预订人部分end -->
			
			<div class="orderdetails_tit orderdetails_titpr">
				<span>4</span>游客信息
			</div>
			
			<!-- 游客部分start -->
			<div id="traveler">
				<c:forEach items="${orderDetailInfoMap.travelInfoList }" var="travelInfo">
				<form class="travelerTable">
						<div class="tourist">
						
							<div class="tourist-t">
								<input id="travelId" type="hidden" name="travelId" value="${travelInfo.id }" class="traveler"> 
								<span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span>
								<label class="ydLable">游客<em class="travelerIndex"></em>:</label>
								<div class="tourist-t-off">
									${travelInfo.travelName }<label>签证状态：${travelInfo.visaStauts }</label>
									<c:if test="${orderDetailInfoMap.type == 1 }">
										<span class="fr">结算价：<span class="gray14"></span><span class="ydFont2">${travelInfo.payPrice}</span></span>
									</c:if>
								</div>
								<div class="tourist-t-on">
								
									<c:choose>
										<c:when test="${travelInfo.personType == 1 }">成人</c:when>
										<c:when test="${travelInfo.personType == 2 }">儿童</c:when>
										<c:when test="${travelInfo.personType == 3 }">特殊人群</c:when>
									</c:choose>
									
									<!--是否需要联运 start-->
									<div class="tourist-t-r">是否联运：
										<label>
											<c:choose>
												<c:when test="${travelInfo.intermodalType == 1 }">需要</c:when>
												<c:otherwise>不需要</c:otherwise>
											</c:choose>
										</label>
										<c:if test="${travelInfo.intermodalType == 1 }">
											${travelInfo.groupPart }：<em>${travelInfo.price }</em>
										</c:if>
										<%-- <c:if test="${orderDetailInfoMap.type == 2 }"> 
											</label><label>团号：dt690725</label> <label>单团</label> <label>美国纽约一地6天五晚</label> <label>操作人：王海</label>
										</c:if> --%>
									</div>
									<!--是否需要联运 end-->
									
								</div>
							</div>
							
							<div class="tourist-con" flag="messageDiv">
							
								<!--游客信息左侧开始-->
								<div class="tourist-left">
									<div class="ydbz_tit ydbz_tit_child">
										<em class="ydExpand closeOrExpand" onclick="boxCloseOnAdd(this)"></em>基本信息
									</div>
									<ul class="tourist-info1 clearfix" flag="messageDiv">
										<li><label class="ydLable">姓名：</label>${travelInfo.travelName }</li>
										<li><label class="ydLable">英文／拼音：</label>${travelInfo.travelEName }</li>
										<li><label class="ydLable">性别：</label><c:choose><c:when test="${travelInfo.sex == 1 }">男</c:when><c:when test="${travelInfo.sex == 1 }">女</c:when></c:choose></li>
										<li><label class="ydLable">国籍：</label>${travelInfo.nationality}</li>
										<li><label class="ydLable">出生日期：</label>${travelInfo.birthDay }</li>
										<li><label class="ydLable">联系电话：</label>${travelInfo.telephone }</li>
										<li><label class="ydLable">护照号：</label>${travelInfo.passportCode }</li>
										<li><label class="ydLable">护照有效期：</label>${travelInfo.passportValidity }</li>
										<li><label class="ydLable">身份证号：</label>${travelInfo.idCard }</li>
										<li><label class="ydLable">护照类型：</label><c:choose><c:when test="${travelInfo.passportType == 1 }">因公护照</c:when><c:when test="${travelInfo.passportType == 2 }">因私护照</c:when></c:choose></li>
									</ul>
									<div class="ydbz_tit ydbz_tit_child">&#12288;&#12288;备注：</div>
									<ul class="textareaulpad">${travelInfo.remark }</ul>
								</div>
								<!--游客信息左侧结束-->
								
								<c:if test="${orderDetailInfoMap.type == 1 }">
									<div class="kong"></div>
		                            	<div class="fr" style="padding-right:10px"><label class="ydLable2">结算价：</label><span class="gray14"></span><span class="ydFont2">${travelInfo.payPrice}</span>
			                        </div>
		                        </c:if>
							</div>
						</div>
				</form>
				</c:forEach>	
			</div>
			<!-- 游客部分 end -->
			
			<!-- 支付部分start -->
			<c:if test="${orderDetailInfoMap.type == 1 }">
			
				<div class="orderdetails_tit">
					<span>5</span>支付信息
				</div>
				
				<c:forEach items="${orderDetailInfoMap.orderPayInfoList }" var="payInfo">
					<p class="orderdetails6">
						<span>付款方式：${payInfo.payTypeName }</span> 
						<span>支付款类型：${fns:getDictLabel(payInfo.payPriceType,"payprice_Type" , "")}</span> 
						<span>付款金额：${payInfo.payPrice }</span> 
						<span>付款时间：  <fmt:formatDate value="${payInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></span> 
						<span>付款凭证： <a lang="4453" class="showpayVoucher">${payInfo.docName }</a> </span>
					</p>
					
					<div class="payment_information">
						<p class="orderdetails6">
							<span>成人：${orderDetailInfoMap.adultPrice }/成人 x ${orderDetailInfoMap.adultNum }人</span> 
							<span>儿童：${orderDetailInfoMap.childPrice }/儿童 x ${orderDetailInfoMap.childNum }人</span> 
							<span>特殊人群：${orderDetailInfoMap.specialPrice }/特殊人群 x ${orderDetailInfoMap.specialNum }人</span> 
							<!-- <span>内陆机票：￥33/人x1</span>  -->
							<span>其他费用：${orderDetailInfoMap.otherFee }</span>
						</p>
						<c:if test="${orderDetailInfoMap.type == 1 }">
							<div class="ordermoney ordermoney2">
								应收总计：<span>${orderDetailInfoMap.totalMoney }</span><br />达账金额：<span>${orderDetailInfoMap.accountedMoney }</span>
							</div>
						</c:if>
					</div>
					
				</c:forEach>
			</c:if>
			<!-- 支付部分end -->
				
			<!-- 按钮部分 start -->
			<div class="ydbz_sxb" id="secondDiv" style="display: block;">
				<div class="ydBtn ydBtn2">
						<a class="ydbz_s gray" onclick="closeCurWindow()">取消</a><input type="button" class="btn btn-primary fl" value="确定" onClick="saveRemark()">
				</div>
			</div>
			<!-- 按钮部分 end -->
				
			
		</div>
	</div>
	<!--右侧内容部分结束-->
	
	
<!-- js -->
<script type="text/javascript">
	function closeCurWindow(){
		this.close();
	}
	$(document).ready(function(e) {
	    var leftmenuid = $("#leftmenuid").val();
	    $(".main-nav").find("li").each(function(index, element) {
	        if($(this).attr("menuid")==leftmenuid){
	            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
	        }
	    });
	});
	$(function(){
	    $('.closeNotice').click(function(){
	        var par = $(this).parent().parent();
	        par.hide();
	        par.prev().removeClass('border-bottom');
	        par.prev().find('.notice-date').show();
	    });
	    $('.showNotice').click(function(){
	        $(this).parent().hide();
	        var par = $(this).parent().parent();
	        par.addClass('border-bottom');
	        par.next().show();
	    });
	});
	$(function(){
		$('.main-nav li').click(function(){
			$(this).addClass('select').siblings().removeClass('select');
		})
	})
	
	String.prototype.formatNumberMoney= function(pattern){
		  var strarr = this?this.toString().split('.'):['0'];   
		  var fmtarr = pattern?pattern.split('.'):[''];   
		  var retstr='';   
		  var str = strarr[0];
		  var fmt = fmtarr[0];
		  var i = str.length-1;     
		  var comma = false;   
		  for(var f=fmt.length-1;f>=0;f--){   
		    switch(fmt.substr(f,1)){   
		      case '#':   
		        if(i>=0 ) retstr = str.substr(i--,1) + retstr;   
		        break;   
		      case '0':   
		        if(i>=0) retstr = str.substr(i--,1) + retstr;   
		        else retstr = '0' + retstr;   
		        break;   
		      case ',':   
		         comma = true;   
		         retstr=','+retstr;   
		        break;   
		     }   
		   }   
		  if(i>=0){   
		    if(comma){   
		      var l = str.length;   
		      for(;i>=0;i--){   
		         retstr = str.substr(i,1) + retstr;   
		        if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;   
		       }   
		     }   
		    else retstr = str.substr(0,i+1) + retstr;   
		   }   
		  
		   retstr = retstr+'.';   
		   
		   str=strarr.length>1?strarr[1]:'';   
		   fmt=fmtarr.length>1?fmtarr[1]:'';   
		   i=0;   
		  for(var f=0;f<fmt.length;f++){   
		    switch(fmt.substr(f,1)){   
		      case '#':   
		        if(i<str.length) retstr+=str.substr(i++,1);   
		        break;   
		      case '0':   
		        if(i<str.length) retstr+= str.substr(i++,1);   
		        else retstr+='0';   
		        break;   
		     }   
		   }   
		  return retstr.replace(/^,+/,'').replace(/\.$/,'');   
	}
	
	String.prototype.replaceSpecialChars=function(regEx){
		if(!regEx){
			regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
		}
		return this.replace(regEx,'');
		
	};
</script>
</body>
</html>