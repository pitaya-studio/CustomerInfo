<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>机票产品切位</title>
<meta name="decorator" content="wholesaler"/>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
	<script type="text/javascript">

	function closeCurWindow(){
		this.close();
	}

	   function downloads(docid,activitySerNum,groupCode,acitivityName,iszip){
            if(iszip){
		                var zipname = activitySerNum+'-'+groupCode;
		                window.open("${ctx}/sys/docinfo/zipdownload/"+docid+"/"+zipname);
            }else{
            		    window.open("${ctx}/sys/docinfo/download/"+docid);
             		}
       	   }
	</script>
</head>
<body>
        <page:applyDecorator name="show_head">
		    <page:param name="desc">机票切位详情</page:param>
		</page:applyDecorator>
       
        <div class="ydbzbox fs">
	
	<input type="hidden" id="id" name="id" value="${airticketId }"/>
    <div class="orderdetails_tit"><span>1</span>机票产品信息</div>
       
      <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">

        <p class="ydbz_mc">
		   ${fns:getDictLabel(activityAirTicket.departureCity, 'from_area', '')}
                        	—
    	   <c:forEach items="${arrivedareas}" var="arrivedCitys">
		      		<c:if test="${arrivedCitys.id == activityAirTicket.arrivedCity}">
		     		 		${arrivedCitys.name}
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
                                    <td class="mod_details2_d1">特殊人群备注：</td>
                                    <td class="mod_details2_d2"><p style="white-space:nowrap; text-overflow:ellipsis; overflow:hidden;width:200px" 
                                    title="${activityAirTicket.specialremark}">${activityAirTicket.specialremark}</p>
                                    </td>
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
                                    <td class="mod_details2_d1">特殊人群备注：</td>
                                    <td class="mod_details2_d2"><p style="white-space:nowrap; text-overflow:ellipsis; overflow:hidden;width:200px" 
                                    title="${activityAirTicket.specialremark}">${activityAirTicket.specialremark}</p>
                                    </td>
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
                                    <td class="mod_details2_d1">特殊人群备注：</td>
                                    <td class="mod_details2_d2"><p style="white-space:nowrap; text-overflow:ellipsis; overflow:hidden;width:200px" 
                                    title="${activityAirTicket.specialremark}">${activityAirTicket.specialremark}</p>
                                    </td>
                                 </tr>
                    </table>
  </c:if>
  </div>
            
           
       <div class="orderdetails_tit">
			<span>2</span>
			切位订单信息
			</div>
            
        <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel" style="margin-top:10px; ">
        <thead>
            <tr>
            	    <th width="7%" rowspan="2">出发日期</th>
					<th width="7%" rowspan="2">订金(元)</th>
					<th width="7%" rowspan="2">切位人数</th>
					<th width="7%" rowspan="2">余位</th>
					<th width="7%" rowspan="2">预订人</th>
					<th width="15%" colspan="3" class="t-th2">同行价</th>
					<th width="5%" rowspan="2">预收</th>					
					<th width="11%" rowspan="2">收款时间</th>
					<th width="6%" rowspan="2">收款方式</th>
					<th width="6%" rowspan="2">收款凭证</th>
					<th width="5%" rowspan="2">备注</th>
                    
				</tr>
				<tr>
					<th width="5%">成人</th>
					<th width="5%">儿童</th>
					<th width="5%">特殊人群</th>
			</tr>
            </tr>
        </thead>
        <tbody><fmt:formatNumber type="currency" pattern="#,##0.00" value="" />
        <c:forEach items="${list}" var="reserve" varStatus="status">
            <tr>
            	 <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${activityAirTicket.startingDate}"/></td>              
                <td class="tc"><c:if test="${not empty reserve.orderMoney}">${fns:getCurrencyNameOrFlag(reserve.moneyType,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${reserve.orderMoney}" /></td>
                <td class="tc">${reserve.payReservePosition }</td>
                <td class="tc">${activityAirTicket.freePosition}</td>
                <td class="tc">${reserve.reservation }</td>
                <td class="tc"><c:if test="${not empty activityAirTicket.settlementAdultPrice}">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityAirTicket.settlementAdultPrice}" /></td>
                <td class="tc"><c:if test="${not empty activityAirTicket.settlementcChildPrice}">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityAirTicket.settlementcChildPrice}" /></td>
                <td class="tc"><c:if test="${not empty activityAirTicket.settlementSpecialPrice}">${fns:getCurrencyNameOrFlag(activityAirTicket.currency_id,0)}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityAirTicket.settlementSpecialPrice}" /></td>
                <td class="tc">${activityAirTicket.reservationsNum}</td>
              
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${reserve.createDate}"/></td>
                <td class="tc">${reserve.payTypeLabel}</td>
                <td class="tc">
                    <c:if test="${payVoucherIds!='' }">
                <a href="javascript:void(0)" onClick="downloads('${payVoucherIds }','${activityAirTicket.id }','','',true)">下载</a>
      
                 </c:if>
  
                </td>
                <td class="tc">${reserve.remark }</td>
                  
            </tr> 
        </c:forEach>

        </tbody>
    </table>
      
	</div>
		<!--右侧内容部分结束-->
	<div class="ydbz_sxb" id="secondDiv" style="display: block;">
		<div class="ydBtn ydBtn2">
				<a class="ydbz_s gray" onclick="closeCurWindow()">关闭</a>
		</div>
	</div>
	
</body>
</html>
