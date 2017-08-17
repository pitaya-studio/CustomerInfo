<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<title>订单-单办机票订单操作</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<style type="text/css">
	.mod_information_dzhan{
       	width:100%;
    }
</style>
<script type="text/javascript">
function closeCurWindow(){
    this.close();
}
/*
 *公用下载方法 
 */
function downloads(docIds,fileName){
	if(docIds == null || docIds == ""){
		top.$.jBox.tip("没有" + fileName);
		return;
	}
	var doc = docIds.replace(/[ ]/g,"");
		window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + doc + "/"+fileName)));
		return;
	}
	
</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<div class="ydbzbox fs">
		<div class="tr">
		<c:choose>
		<c:when test="${orderDetailInfoMap.type== 1}">
			 <a href="${ctx}/order/manage/airticketOrderTravelExport?orderId=${orderDetailInfoMap.orderId}" class="dyzx-add">下载全部游客资料</a>
			 <a href="${ctx}/order/manage/airticketOrderNameList?orderId=${orderDetailInfoMap.orderId}" class="dyzx-add">下载出票名单</a>
		</c:when>
		<c:otherwise>
			<a href="${ctx}/order/manage/airticketOrderTravelExport?orderId=${orderDetailInfoMap.orderId}" class="dyzx-add">下载全部游客资料</a>
			<a href="${ctx}/order/manage/airticketOrderNameList?orderId=${orderDetailInfoMap.orderId}" class="dyzx-add">下载出票名单</a>
			<a href="#" class="dyzx-add" onclick="javascript:downloads('${activityGroup.openDateFile }','出团通知书');">下载出团通知书</a> 
			<a href="#" class="dyzx-add" onclick="javascript:downloads('${productOrder.confirmationFileId}','确认单');" >下载确认单</a> 
		</c:otherwise>
		</c:choose>
			
		</div>
		<div class="orderdetails">
			<div class="orderdetails_tit">
				<span>1</span>订单信息
				<input id="orderId" type="hidden" value="${orderDetailInfoMap.orderId }">
			</div>
			<div class="orderdetails1">
				<table border="0" width="90%" style="margin-left:0;">
					<tbody>
						<tr>
							<td class="mod_details2_d1">下单人：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
							<td class="mod_details2_d1">下单时间：</td>
							<td class="mod_details2_d2"><fmt:formatDate value="${orderDetailInfoMap.orderCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
							<td class="mod_details2_d1">销售：</td>
							<td class="mod_details2_d2">${orderDetailInfoMap.salerName}</td>
							<td class="mod_details2_d1">参团订单编号：</td>
							<td class="mod_details2_d2">${porder.orderNum }</td>
							<td class="mod_details2_d1">参团订单团号：</td>
							<td class="mod_details2_d2">${groupNum }</td>
						</tr>
						
						<c:if test="${orderDetailInfoMap.type == 2 }">
							<tr>
								<td class="mod_details2_d1">下单人：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.userName }</td>
<!-- 								重复显示   by sy 20150925 -->
<!-- 								<td class="mod_details2_d1">参团订单编号：</td> -->
<!-- 								<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td> -->
<!-- 								<td class="mod_details2_d1">参团团号：</td> -->
<!-- 								<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td> -->
							</tr>
						</c:if>
					</tbody>
				</table>
				<div class="mod_information_d7" style="width:auto;margin-left:25px"></div>
	            <table border="0" width="90%" style="margin-left:0;">
	                <tbody>
	                <tr>
	                    <td class="mod_details2_d1" style="padding-top:8px;">发票号：</td>
	                    <td class="mod_details2_d2" >
	                    	<c:forEach var="invoice" items="${invoices }" varStatus="varStatus">
								<c:if test="${varStatus.count > 1 }"></br></c:if>
								<span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;" title="${invoice.invoiceNum }">
									<a target="_blank" href="${ctx}/orderInvoice/manage/viewInvoiceInfo/${invoice.uuid}/-2/7">${invoice.invoiceNum }</a>
								</span>
							</c:forEach>
	                    </td>
	                    <td class="mod_details2_d1" style="padding-top:8px;">收据号：</td>
	                    <td class="mod_details2_d2">
	                    	<c:forEach var="receipt" items="${receipts }" varStatus="varStatus">
	                    		<c:if test="${varStatus.count > 1 }"></br></c:if>
								<span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;" title="${receipt.invoiceNum }">
									<a target="_blank" href="${ctx}/receipt/limit/supplyviewrecorddetail/${receipt.uuid}/-2/7">${receipt.invoiceNum }</a>
								</span>
							</c:forEach>
	                    </td>
	                    <td class="mod_details2_d1"></td>
	                    <td class="mod_details2_d2"></td>
	                </tr>
	                </tbody>
	            </table>
			</div>
			
			
			
			
			<div class="mod_information_dzhan" style="overflow:hidden;">
				<div class="mod_information_dzhan_d mod_details2_d">
						<c:choose>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && orderDetailInfoMap.airType == 2 }">
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
										<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
									
								</c:forEach> 
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && orderDetailInfoMap.airType == 3 }">
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
										<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
								</c:forEach>
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && orderDetailInfoMap.airType == 1 }">
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
									 		<c:when test="${flightInfo.ticketAreaType == 4 }">国内</c:when>
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
										<td class="mod_details2_d2">${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
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
							<p>成人：<font color="#FF0000">${orderDetailInfoMap.adultPrice }</font></p>
							<p>儿童：<font color="#FF0000">${orderDetailInfoMap.childPrice }</font></p>
							<p>特殊人群：<font color="#FF0000">${orderDetailInfoMap.specialPrice }</font></p>
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
			
			<div class="orderdetails_tit">
				<span>2</span>机票
			</div>
			<div flag="messageDiv" class="ydbz2_lxr">
				<form class="contactTable">
					<p>
						<label style="vertical-align:top;width:300px;text-align:center;">航班备注：<c:if test="${ not empty orderDetailInfoMap.remark }">${orderDetailInfoMap.remark }</c:if><c:if test="${empty orderDetailInfoMap.remark }">无</c:if></label>
					</p>
				</form>
			</div>
			<div class="orderdetails_tit">
				<span>3</span>预订人信息
			</div>
           	<shiro:hasPermission name="airticketOrder4Sale:agentinfo:visibility">
           		<input type="hidden" value="1" id="agentinfo_visibility">
				<c:set var="agentinfo_visibility" value="1"></c:set>
           	</shiro:hasPermission>
           	<shiro:hasPermission name="airticketOrder4Oper:agentinfo:visibility">
            	<input type="hidden" value="1" id="agentinfo_visibility">
				<c:set var="agentinfo_visibility" value="1"></c:set>
           	</shiro:hasPermission>
			<div class="orderdetails4">
				
				<c:if test="${airticketOrder.agentinfoId ne -1 }">
					<p class="ydbz_qdmc">预订渠道： ${orderDetailInfoMap.agentName  } </p> <br/><br/> 
				</c:if>
				<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
				<c:if test="${airticketOrder.agentinfoId eq -1 }">
					<c:choose>
					   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
					       <p class="ydbz_qdmc">直客名称： ${orderDetailInfoMap.nagentName }</p>
					   </c:when>
					   <c:otherwise>
					       <p class="ydbz_qdmc">非签约渠道名称： ${orderDetailInfoMap.nagentName }</p>
					   </c:otherwise>
					</c:choose> 
				</c:if>
				<c:if test="${fns:getUser().id eq orderDetailInfoMap.orderCreateBy or fns:getUser().id eq orderDetailInfoMap.saler or (not empty agentinfo_visibility and agentinfo_visibility eq 1) }">
				<ul class="ydbz_qd" >
					<c:forEach items="${orderContacts }" var = "orderContact">
					   <li><label>渠道联系人：</label>${orderContact.contactsName }</li>
	                   <li class="ydbz_qd_lilong"><label>渠道联系人电话：</label>${orderContact.contactsTel }<div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,'','2')">展开全部</div></li>
	                   <li flag="messageDiv" style="display:none" class="ydbz_qd_close">
	                       <ul>
	                       		<li><label>固定电话：</label>${orderContact.contactsTixedTel}</li>
		                        <li><label>渠道地址：</label>${orderContact.contactsAddress }</li>
		                        <li><label>传真：</label>${orderContact.contactsFax }</li>
		                        <li><label>QQ：</label>${orderContact.contactsQQ }</li>
		                        <li><label>Email：</label>${orderContact.contactsEmail }</li>
		                        <li><label>渠道邮编：</label>${orderContact.contactsZipCode }</li>
		                        <li><label>其他：</label>${orderContact.remark }</li>
	                       </ul>
                   	   </li>
					</c:forEach>					
                </ul>
                </c:if>
			</div>

			<!-- 特殊需求 -->
			<div id="manageOrder_m">
				<div id="contact">
					<div class="orderdetails_tit"> <span>4</span>特殊需求</div>
					<div class="ydbz2_lxr" flag="messageDiv">
						<form class="contactTable">
							<table width="80%">
								<tbody><tr>
									<td width="90px" style="vertical-align:top;text-align:right">特殊需求：</td>
									<%--<textarea style="display: none;" class="disabledClass" id="specialDemand"--%>
                                              <%--name="specialDemand" maxlength="100">value</textarea>--%>
									<td width="80%" class="disabledshowspan" style="word-break:break-all">${orderDetailInfoMap.comments}</td>
								</tr>
								</tbody>
							</table>
						</form>
					</div>
				</div>
			</div>

			<div class="orderdetails_tit orderdetails_titpr" style="padding-left: 0;">
				<span>5</span>游客信息
			</div>
			<c:if test="${empty orderDetailInfoMap.travelInfoList }">
				<div class="warningtravelerNum">暂无游客信息</div>
			</c:if>
			<div class="orderdetails_tit orderdetails_titpr">
			</div>
			<%int n=1;%>
			<c:if test="${not empty orderDetailInfoMap.travelInfoList }">
				<c:forEach items="${orderDetailInfoMap.travelInfoList }" var="travelInfo">
					<div class="tourist">
						<div class="tourist-t">
							<span class="add_seachcheck" onclick="boxCloseOnAdd(this,'1')"></span><label class="ydLable">游客<%=n++%>：</label>
							<div class="tourist-t-off">
								<label>签证状态：${travelInfo.visaStauts }</label>
								
								<c:if test="${orderDetailInfoMap.type == 1 }">
									<span class="fr">结算价：<span class="gray14"></span>
									<span class="ydFont2">${travelInfo.payPrice}</span>
								</c:if>
								
								</span>
							</div>
							<div class="tourist-t-on">
								<label>
									<c:choose>
										<c:when test="${travelInfo.personType == 1 }">成人</c:when>
										<c:when test="${travelInfo.personType == 2 }">儿童</c:when>
										<c:when test="${travelInfo.personType != 1 && travelInfo.personType != 2 }">特殊人群</c:when>
									</c:choose>
									<c:if test="${travelInfo.delFlag == 6 }">
										<div class="ycq yj" style="margin-top:1px;">
									 		已改签
										</div>
									</c:if>
										<c:if test="${travelInfo.isAirticketFlag == 0 }">
										<div class="ycq yj" style="margin-top:1px;">
									 		已退票
										</div>
									</c:if>
								</label>
								<div class="tourist-t-r">是否联运：
									<label>
										<c:choose>
											<c:when test="${travelInfo.intermodalType == 0 }">不需要</c:when>
											<c:when test="${travelInfo.intermodalType == 1 }">需要</c:when>
										</c:choose>
									</label>
									<c:if test="${travelInfo.intermodalType == 1 }">
										<label>${travelInfo.groupPart }：<span>${travelInfo.price }</span>
									</c:if>
									<%-- <c:if test="${orderDetailInfoMap.type == 2 }"> 
										</label><label>团号：dt690725</label> <label>单团</label> <label>美国纽约一地6天五晚</label> <label>操作人：王海</label>
									</c:if> --%>
								</div>
							</div>
						</div>
						<div class="tourist-con" flag="messageDiv">
							<!--游客信息左侧开始-->
							<div class="tourist-left">
								<div class="ydbz_tit ydbz_tit_child">
									<em onclick="boxCloseOnAdd(this)" class="ydExpand closeOrExpand"></em>基本信息
								</div>
								<ul class="tourist-info1 clearfix" flag="messageDiv">
									<li><label class="ydLable">姓名：</label>${travelInfo.travelName }</li>
									<li><label class="ydLable">英文／拼音：</label><span class="fArial">${travelInfo.travelEName }</span>
									</li>
									<li><label class="ydLable">性别：</label><c:choose><c:when test="${travelInfo.sex == 1}">男</c:when><c:when test="${travelInfo.sex == 2}">女</c:when> </c:choose></li>
									<li><label class="ydLable">国籍：</label>${fns:getCountryName((travelInfo.nationality== null || travelInfo.nationality==0) ? null : travelInfo.nationality)} </li>
									<li><label class="ydLable">出生日期：</label><span class="fArial">${travelInfo.birthDay }</span>
									</li>
									<li><label class="ydLable">联系电话：</label><span class="fArial">${travelInfo.telephone }</span>
									</li>
									<li><label class="ydLable">护照号：</label><span class="fArial">${travelInfo.passportCode }</span>
									</li>
									<li><label class="ydLable">护照有效期：</label><span class="fArial">${travelInfo.passportValidity }</span>
									</li>
									<li><label class="ydLable">身份证号：</label><span class="fArial">${travelInfo.idCard }</span>
									</li>
								</ul>
								<div class="ydbz_tit ydbz_tit_child">&#12288;&#12288;备注：</div>
								<ul>
									<span class="ydbz_2uploadfile_span"></span>
									<c:choose>
									<c:when test="${not empty travelInfo.remark }">${travelInfo.remark }</c:when>
									<c:otherwise>
									无
									</c:otherwise>
									</c:choose>
								</ul>
								<div class="ydbz_tit ydbz_tit_child">补充资料：</div>
								<ul class="ydbz_2uploadfile_ul">
									<c:if test="${not empty travelInfo.travelAttachInfoList }">
										<c:forEach items="${travelInfo.travelAttachInfoList }" var="attachInfo">
											<li>${attachInfo.fileType }：${attachInfo.fileName}</li>
										</c:forEach>
									</c:if>
								</ul>
							</div>
							<!--游客信息左侧结束-->
							<!--游客信息右侧开始-->
							<c:if test="${orderDetailInfoMap.type == 1 }">
								<div class="tourist-right">
									<div class="clearfix">
										<ul class="tourist-info2">
										<c:choose>
										<c:when test="${travelInfo.personType == 1 }"><li><label class="ydLable2">成人价：</label><span class="ydFont4">${orderDetailInfoMap.adultPrice }</span></li></c:when>
										<c:when test="${travelInfo.personType == 2 }"><li><label class="ydLable2">儿童价：</label><span class="ydFont4">${orderDetailInfoMap.childPrice }</span></li></c:when>
										<c:when test="${travelInfo.personType != 1 && travelInfo.personType != 2 }"><li><label class="ydLable2">特殊人群价：</label><span class="ydFont4">${orderDetailInfoMap.specialPrice }</span></li></c:when>
										</c:choose>
										</ul>
									</div>
									<div class="yd-line"></div>
			
								<!-- 	<div class="clearfix">
										<div class="payfor-otherDiv">
											<div class="payfor-other cost">
												<ul class="tourist-info2">
													<li><label class="ydLable2">其他费用：</label>￥<span class="ydFont4">10</span>
													</li>
													<li><label class="ydLable2">其他费用：</label>￥<span class="ydFont4">20</span>
													</li>
													<li><label class="ydLable2">其他费用：</label>￥<span class="ydFont4">30</span>
													</li>
												</ul>
											</div>
										</div>
									</div> -->
									<!-- add start by jiangyang C189 -->
									<div class="clearfix">
                                        <div class="traveler-rebatesDiv">
                                        	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
								       		<c:choose>
								      			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
													<label class="ydLable2 ydColor1">预计个人宣传费：</label><span> </span>
												</c:when>
								         		<c:otherwise>
								         			<label class="ydLable2 ydColor1">预计个人返佣：</label><span> </span>
								          		</c:otherwise>
										 </c:choose>   
                                            
                                            <span class="disabledshowspan"><c:if test="${empty travelInfo.travelerRebate }">——</c:if>${travelInfo.travelerRebate }</span>
                                        </div>
                                    </div>
                                    <div class="yd-line"></div>
									<!-- add end   by jiangyang C189 -->
									<div class="yd-total clearfix">
										<div class="fr">
											<label class="ydLable2">结算价：</label><span class="gray14"></span><span class="ydFont2">${travelInfo.traPayPrice }</span>
										</div>
									</div>
								</div>
							</c:if>
							<!--游客信息右侧结束-->
						</div>
					</div>
				</c:forEach>
			</c:if>
			<!-- add start by jiangyang C189 -->
			<div class="tourist " style="margin-top: 10px;margin-bottom: 10px;">
            	<div class="traveler-rebatesDiv">
                	<label class="ydLable2 ydColor1" style="width: 100px;">
                	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       		<c:choose>
		      			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<label class="ydLable2 ydColor1" style="width: 100px;">预计团队宣传费：</label><span> </span>
						</c:when>
		         		<c:otherwise>
		         			<label class="ydLable2 ydColor1">预计团队返佣：</label><span> </span>
		          		</c:otherwise>
				 </c:choose>   
                	
                	</label>
                	<span class="disabledshowspan"><c:if test="${empty orderDetailInfoMap.groupRebate }">——</c:if>${orderDetailInfoMap.groupRebate }</span>
            	</div>
            </div>
			<!-- add end   by jiangyang C189 -->
			<c:if test="${orderDetailInfoMap.type == 1 }">
				<div class="orderdetails_tit">
					<span>6</span>收款信息
				</div>
				
				<c:forEach items="${orderDetailInfoMap.orderPayInfoList }" var="payInfo">
					<p class="orderdetails6">
						<span style="width: 16.6%">收款方式：${payInfo.payTypeName }</span> 
						<span style="width: 16.6%">收款类型：${fns:getDictLabel(payInfo.payPriceType,"payprice_Type" , "")}</span> 
						<span style="width: 16.6%">收款金额：${payInfo.payPrice }</span> 
						<span style="width: 16.6%">收款时间： <fmt:formatDate value="${payInfo.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span> 
						<span style="width: 16.6%">收款凭证：
						<c:forEach items="${payInfo.docList }" var="docInfo">
							 <a lang="4453" href="${ctx}/sys/docinfo/download/${docInfo.id}" class="showpayVoucher">${docInfo.docName }</a> 
						</c:forEach>
						</span>
						<span style="width: 16.6%">是否到账：
							  <c:choose>
							    <c:when test="${empty payInfo.isAsAccount}">
									未到账
							    </c:when>
							    <c:when test="${payInfo.isAsAccount == 0}">
									未到账
							    </c:when>  
							     <c:when test="${payInfo.isAsAccount == 99}">
									未到账
							    </c:when>  
							    <c:when test="${payInfo.isAsAccount == 2}">
									已驳回
							    </c:when>  
							   <c:otherwise>  
							   		已到账
							   </c:otherwise>
							  </c:choose>
						</span>
					</p>
				</c:forEach>	
				
				<div class="payment_information">
					<p class="orderdetails6">
						<span>成人：${orderDetailInfoMap.adultPrice }/成人 x ${orderDetailInfoMap.adultNum }人</span> 
						<span>儿童：${orderDetailInfoMap.childPrice }/儿童 x ${orderDetailInfoMap.childNum }人</span> 
						<span>特殊人群：${orderDetailInfoMap.specialPrice }/特殊人群 x ${orderDetailInfoMap.specialNum }人</span> 
						<!-- <span>内陆机票：￥33/人x1</span>  -->
						<span>其他费用：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${orderDetailInfoMap.otherFee }" /></span>
					</p>
					<c:if test="${orderDetailInfoMap.type == 1 }">
						<div class="ordermoney ordermoney2">
							应收总计：<em class="gray14"></em><span>${orderDetailInfoMap.totalMoney }</span><br />
							达账金额：<em class="gray14"></em><span>${orderDetailInfoMap.accountedMoney }</span>
						</div>
					</c:if>
				</div>
			</c:if>
		</div>
		<%@ include file="/WEB-INF/views/modules/order/airticketOrderReviewInfo.jsp"%>
	</div>
	<!--右侧内容部分结束-->
	<div class="ydbz_sxb" id="secondDiv" style="display: block;">
		<div class="ydBtn ydBtn2">
				<a class="ydbz_s gray" onclick="closeCurWindow()">关闭</a>
		</div>
	</div>
</body>
</html>