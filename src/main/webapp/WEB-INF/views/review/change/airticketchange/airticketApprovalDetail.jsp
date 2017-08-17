<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta>
<c:choose>
	<c:when test="${not empty isReview and isReview}">
		<title>机票改签-审批</title>
	</c:when>
	<c:otherwise>
		<title>机票改签-详情</title>
	</c:otherwise>
</c:choose>	

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />

<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">

function closeCurWindow(){
	window.opener.$("#searchForm").submit();
	this.close();
}
$(function(){

	$("#submit").click(function() {
		
		
		var html = '<div class="add_allactivity"><label>请填写您的备注!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" id="reason" cols="" rows="" maxlength="200"></textarea>';
		html += '</div>';
		$.jBox(html, { title: "备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$.ajax({    
		 				type: "POST",                 
						url:"${ctx}/airticketChange/airChange/planeReview",                 
						data:{travelerId:$("#trvalerId").val(),reason:$("#reason").val(),
						reviewId:$("#reviewId").val(),orderId:$("#orderId").val(),operate:1},                
						error: function(request) {                     
						},      
						dataType:"json",           
						success: function(msg) { 
// 								alert(msg.result);		
								window.opener.$("#searchForm").submit();
								window.close();	
						}             
				});
			}
		},height:250,width:500});
	});
	
	$("#back").click(function() {
	
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" name="reason" id="reason" cols="" rows="" maxlength="200"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({    
	 				type: "POST",                 
					url:"${ctx}/airticketChange/airChange/planeReview",                 
					data:{travelerId:$("#trvalerId").val(),reason:$("#reason").val(),
					reviewId:$("#reviewId").val(),orderId:$("#orderId").val(),operate:0},                
					error: function(request) {                     
					},      
					dataType:"json",           
					success: function(msg) { 
// 							alert(msg.result);		
							window.opener.$("#searchForm").submit();
							window.close();	
					}             
			});
		}
	},height:250,width:500});
	});
		

});

	
</script>

</head>

<body>
	<!-- tab 
	
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>-->
	
	<!--右侧内容部分开始-->
	<input id="orderId" type="hidden" value="${orderId}">
				<input id=reviewId type="hidden" value="${reviewId}">
				<input type='hidden' id="trvalerId" value="${trvalerId}"/>
				<!--右侧内容部分开始-->
				<div class="mod_nav">审批 > 改签申请 > 改签审批</div>
				<div class="ydbz_tit">订单详情</div>
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
						<c:if test="${orderDetailInfoMap.type == 2 }">
							<tr>
								<td class="mod_details2_d1">销售：</td>
								<c:set value="${orderDetailInfoMap.saler }" var="saler"></c:set>
								<td class="mod_details2_d2">${fns:getUserNameById(saler) }</td> 
								<td class="mod_details2_d1">参团订单编号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.mainOrderId }</td>
								<td class="mod_details2_d1">参团团号：</td>
								<td class="mod_details2_d2">${orderDetailInfoMap.activityGroupCode }</td>
							</tr>
						</c:if>
					</tbody>
				</table>
                </div>
				<div class="ydbz_tit"><span class="fl">产品信息</span></div>
				<div style="overflow:hidden;">
				<c:choose>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) == 2 }">
								<!--往返-->
								<p class="ydbz_mc">航段信息（往返）</p>								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
												${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">
												 <c:forEach items="${arrivedCitys}" var="arrivedCitys">
													<c:if test="${arrivedCitys.id == orderDetailInfoMap.arrivedCity}">
													${arrivedCitys.name}
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
										<td class="mod_details2_d2"> ${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}</td>
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
								<p class="ydbz_mc">航段信息（单程）</p>								
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
											${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">
											 <c:forEach items="${arrivedCitys}" var="arrivedCitys">
					                       		<c:if test="${arrivedCitys.id == orderDetailInfoMap.arrivedCity}">
					                       		${arrivedCitys.name}
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
										<td class="mod_details2_d2">${flightInfo.endAirportName} </td>
										
									 </tr>
									 <tr>
										<td class="mod_details2_d1">出发时刻：</td>
										<td class="mod_details2_d2"><fmt:formatDate value="${flightInfo.startTime}" pattern="yyyy-MM-dd HH:mm"/></td>
										<td class="mod_details2_d1">到达时刻：</td>
										<td class="mod_details2_d2" colspan="3"><fmt:formatDate value="${flightInfo.arrivalTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									 </tr>
									 <tr>
										<td class="mod_details2_d1">航空公司：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
								</c:forEach>
							</c:when>
							<c:when test="${not empty orderDetailInfoMap.flightInfoList && fn:length(orderDetailInfoMap.flightInfoList) >= 3 }">
								<!--多段-->
								<p class="ydbz_mc">航段信息（多段）</p>
								<table width="90%" border="0">
									<tbody>
										<tr>
											<td class="mod_details2_d1">出发城市：</td>
											<td class="mod_details2_d2">
											${fns:getDictLabel(orderDetailInfoMap.departureCity, 'from_area', '')}
											</td>
											<td class="mod_details2_d1">到达城市：</td>
											<td class="mod_details2_d2">
												<c:forEach items="${arrivedCitys}" var="arrivedCitys">
													<c:if test="${arrivedCitys.id == orderDetailInfoMap.arrivedCity}">
														${arrivedCitys.name}
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
									 		<c:when test="${flightInfo.ticketAreaType == 4 }">国内</c:when>
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
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airlines,"traffic_name" , "")}</td>
										<td class="mod_details2_d1">舱位等级：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.spaceGrade,"spaceGrade_Type" , "无")}</td>		
										<td class="mod_details2_d1">舱位：</td>
										<td class="mod_details2_d2">${fns:getDictLabel(flightInfo.airspace,"airspace_Type" , "无")}</td>
									 </tr>
									</tbody></table>
								</c:forEach>
							</c:when>
						</c:choose>
				</div>
				<div class="ydbz_tit">
					<span class="fl">游客改签</span><span class="fr wpr20">报批日期：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orderDetailInfoMap.travlerDetail.create_date}"/> </span>
        		</div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th>游客</th>
                         <th>下单时间</th>
						 <th>币种</th>
						 <th>应收金额</th>
						 <th>原因备注</th>
					  </tr>
				   </thead>
				   <tbody>
					  <tr>
						 <td>${orderDetailInfoMap.travlerDetail.name}</td>
                         <td class="tc">
                         	<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orderDetailInfoMap.travlerDetail.create_date}"/>
                         </td>
						 <td>${orderDetailInfoMap.travlerDetail.bz}</td>
						 <td class="tr"><span class="tdred">${orderDetailInfoMap.travlerDetail.je}</span></td>
						 <td>${orderDetailInfoMap.travlerDetail.createReason}</td>
					  </tr>
				   </tbody>
				</table>
                <div class="allzj tr f18">
                    <div class="all-money">改签总金额：<font class="f14">${orderDetailInfoMap.travlerDetail.bz}</font><span class="f20">${orderDetailInfoMap.travlerDetail.je}</span> <span class="tdgreen"></div>
				</div>
				
				<div class="ydbz_tit">
					<span class="fl">审批动态</span>
				</div>
				<c:set var="rid" value="${reviewId}"></c:set>
				<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
				
				<c:choose>
					<c:when test="${not empty isReview and isReview}">
						<div class="dbaniu">
							<a class="ydbz_s gray" onclick="closeCurWindow()">取消</a>
							<a class="ydbz_s" id="back">驳回</a>
		               		<a class="ydbz_s" id="submit">审批通过</a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="ydBtn ydBtn2">
							<a class="ydbz_s" onClick="window.close();">关闭</a>
						</div>
					</c:otherwise>
				</c:choose>		
				<!--右侧内容部分结束-->
				
            
	<!--右侧内容部分结束-->
</body>


</html>
