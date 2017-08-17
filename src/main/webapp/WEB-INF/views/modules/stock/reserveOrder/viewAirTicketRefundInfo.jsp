<!-- 
author:chenry
describe:订单详情页，订单操作中 团签 功能跳转页面,适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2014-11-03
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>散拼切位退款审核详情
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    
	<%-- 前端js效果部分 --%>
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
	
  		
});


</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
.wpr20 label{ width:60px; text-align:right;}
.fl{float:left;}
.fr{float:right;}
.ydbz_tit{
	background-color:#f3f3f3;
	line-height:33px;color:#333333;
	font-weight:bold;padding-left:32px;margin-top:10px;margin-bottom:10px;
	position:relative; *height:33px;
	font-size:14px; border-radius:1px;
	overflow:hidden;
}
label{ cursor:inherit;}
</style>

</head>

<body>
<div id="sea">
				<!--右侧内容部分开始-->
				<div class="mod_nav">
散拼切位
&nbsp;&nbsp;>退款审核&nbsp;&nbsp;>审核详情</div>
				<div class="ydbz_tit">产品信息</div>
               <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">

        	<p class="ydbz_mc">${activityAirTicket.departureCityLabel() }&mdash;${activityAirTicket.arrivedCityLabel()  }    
       			<c:forEach items="${arrivedcitylist}" var="arrivedcity">
                    <c:if test="${arrivedcity.id eq activityAirTicket.arrivedCity}">
                     ${arrivedcity.name}：
                    </c:if>
                </c:forEach>
            <c:if test="${ activityAirTicket.airType ==1}">多段</c:if>
            <c:if test="${ activityAirTicket.airType ==2}">往返</c:if>
            <c:if test="${ activityAirTicket.airType ==3}">单程</c:if>
               </p>
       
				<c:if test="${ activityAirTicket.airType ==1}">
				       <c:forEach items="${activityAirTicket.flightInfos}" var="flightInfos" varStatus="s">
				            <div class="title_samil">第${flightInfos.number}段：<c:if test="${flightInfo.ticket_area_type eq '3'}">内陆</c:if>
				                            <c:if test="${flightInfo.ticket_area_type eq '2'}">国际</c:if>
				                            <c:if test="${flightInfo.ticket_area_type eq '1'}">内陆+国际</c:if></div>
				            <table border="0" width="90%">
                                <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq flightInfos.leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                      </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq flightInfos.destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                               </td>
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfos.arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${flightInfos.flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${flightInfos.airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${flightInfos.spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${flightInfos.airspaceLabel() }</td>
                                 </tr>
			            </table>
			       </c:forEach>
			    </c:if>


			<c:if test="${ activityAirTicket.airType ==2}">
			       <div class="title_samil">去程：</div>
			              <table border="0" width="90%">
                            <tbody><tr>
                            <td class="mod_details2_d1">出发机场：</td>
                            <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                      </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                            <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                               </td>
                           </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
                                 </tr>
                           </table>
                                 <div class="title_samil">返程：</div>
                                  <table border="0" width="90%">
                                 <tbody><tr>
                                    <td class="mod_details2_d1">出发机场：</td>
                                    <td class="mod_details2_d2">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                              </td>
                             <td class="mod_details2_d1">到达机场：</td>
                             <td class="mod_details2_d2">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                     </td>
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[1].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[1].airspaceLabel() }</td>
                                 </tr>
			                    </table>
			 			 </c:if>


						<c:if test="${ activityAirTicket.airType ==3}">
						       <table border="0" width="90%">
                                <tbody><tr>
                                <td class="mod_details2_d1">出发机场：</td>
                                <td class="mod_details2_d2">
                                <c:forEach items="${airportlist}" var="airportlist">
                                <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].leaveAirport}">
                                ${airportlist.airportName}
                                </c:if>
                                </c:forEach>
                                   </td>
                                    <td class="mod_details2_d1">到达机场：</td>
                                    <td class="mod_details2_d2">
                                   <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[0].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                                 </td> 
                                 </tr>
                                 <tr>
                                    <td class="mod_details2_d1">出发时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].startTime }"/></td>
                                    <td class="mod_details2_d1">到达时刻：</td>
                                    <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${activityAirTicket.flightInfos[0].arrivalTime }"/></td>
                                    <td class="mod_details2_d1">航班号：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].flightNumber }</td>               
                                   </tr>
                                    <tr>
                                    <td class="mod_details2_d1">航空公司：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airlinesLabel() }</td>
                                    <td class="mod_details2_d1">舱位等级：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].spaceGradeLabel() }</td>		
                                    <td class="mod_details2_d1">舱位：</td>
                                    <td class="mod_details2_d2">${activityAirTicket.flightInfos[0].airspaceLabel() }</td>
                                 </tr>
		                    </table>
					  </c:if>
					  <!--分区联运开始-->
                            <div class="mod_information_d2 lianyun">
                                	<div class="lianyun_select">
                                        <label class="fbold f14" style="width:auto">联运类型：</label>分区联运
                                    </div>
                                    <div class="transport_city">
	                                    <c:forEach items="${activityAirTicket.intermodalStrategies}" var="list" varStatus="listIndex">
	                                        <p><c:if test="${activityAirTicket.intermodalType==2}"><label class="transport_city_label">联运城市分区：</label></c:if>${list.groupPart}<label>联运价格：</label><span class="currency">${list.priceCurrency.currencyMark}</span>${list.price}</p>
	                                    </c:forEach> 
                                    </div>
                             </div>          
                             <!--分区联运结束-->  
                             <!--离境口岸开始-->                 
                            <div class="seach25 seach100">
                                <p class="fbold f14">离境口岸：</p>
                                <div>${fns:getDictLabel(activityAirTicket.outArea, "out_area", "")}</div>
                            </div>
                            <!--离境口岸结束-->
                            
							 <!--预收人数开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">预收人数：</p>
                                <div>${activityAirTicket.reservationsNum}</div>
                            </div>
                            <div class="kong"></div>
                            <!--预收人数结束-->
                            <!--出票日期开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">出票日期：</p>
                                <div><fmt:formatDate value="${activityAirTicket.outTicketTime}" pattern="yyyy-MM-dd"/></div>
                            </div>
                            <!--出票日期结束-->
							<div class="kong"></div>
							<!--占位方式开始-->
                            <div class="seach25 seach100">
                                <p class="fbold f14" style="width:auto;">付款方式：</p>
                                <p class="seach_r add-paytype">
                                    <font id="payModeText"><c:if test="${activityAirTicket.payMode_deposit==1}">订金占位（保留${activityAirTicket.remainDays_deposit}天）</c:if>&nbsp;&nbsp;<c:if test="${activityAirTicket.payMode_advance==1}">预占位（保留${activityAirTicket.remainDays_advance}天）</c:if>&nbsp;&nbsp;<c:if test="${activityAirTicket.payMode_full==1}">全款支付</c:if>&nbsp;&nbsp;<c:if test="${activityAirTicket.payMode_op==1}">计调确认占位</c:if>&nbsp;&nbsp;<c:if test="${activityAirTicket.payMode_cw==1}">财务确认占位</c:if></font>
                                </p>
                                <div class="kong"></div>
                            </div>
                            <!--占位方式结束-->
							
                            <div class="seach25 seach100 pro-marks1">
                              <p class="fbold f14" style="width:auto;">备注：</p>
                              <p class="seach_r"><span class="disabledshowspan">${activityAirTicket.remark}</span></p>
                            </div>
					  </div>
				<div class="ydbz_tit">
					<span class="fl">
					散拼切位退款
					</span><span class="fr wpr20">报批日期：${hashMap.reviewInfo.createDate}</span>
        		</div>
				<table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            
                            <th width="10%">渠道 </th>
                            <th width="15%">款项</th>
                            <th width="15%">币种</th>
                            <th width="15%">应付金额</th>
                            <th width="15%">退款金额</th>
                            
                            <th width="15%">退款原因</th>
                        </tr>
                    </thead>
                    
                    <tbody>                    
						<tr>
							<td>
							${hashMap.reviewInfo.travelerName}
							</td>
							<td>${hashMap.reviewInfo.refundName}</td>
							<td>${hashMap.reviewInfo.currencyName}</td>
							<td class="tr"><span class="tdred">${hashMap.reviewInfo.payPrice}
							</span></td>	
							<td>
							${hashMap.reviewInfo.refundPrice}
							</td>
													
							<td>${hashMap.reviewInfo.createReason}
							</td>					
						</tr>
                    </tbody>                    
                </table>
				<div style="margin-top:20px;"></div>
				 <div class="ydbz_tit">
					<span class="fl">审核动态</span>
        		</div>
				<ul class="spdtai">
				<c:if test="${not empty hashMap.reviewLogInfo}">
					<c:forEach items="${hashMap.reviewLogInfo}" var="log" varStatus="s">
					<li><fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;&nbsp;
					 【${fns:getUserNameById(log.createBy)}】&nbsp;&nbsp;${log.result}</li>
					</c:forEach>
				
				</c:if>
				<c:if test="${empty hashMap.reviewLogInfo}">
				<li>暂无审核动态</li>
				</c:if>
				</ul>
				<c:if test="${ hashMap.reviewInfo.status ==0}">
				<div class="ydbz_tit">
					<span class="fl">驳回理由</span>
        		</div>
                <ul class="spdtai">
					<li>${ hashMap.reviewInfo.denyReason}</li>
				</ul>
				</c:if>				
				<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a></div>
				<!--右侧内容部分结束-->
			</div>
                
               
                
</body>
</html>
