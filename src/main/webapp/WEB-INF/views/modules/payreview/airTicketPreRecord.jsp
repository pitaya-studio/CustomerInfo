<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>机票付款审核</title>
<meta name="decorator" content="wholesaler"/>
<link rel="stylesheet" href="css/jbox.css" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript">

//付款审核-拒绝某条成本
function denyCost(dom,id,reviewLevel){
	var items_val = id +"," + reviewLevel + ",7";
	$.ajax({
		type: "POST",
		url: "${ctx}/pay/review/batchDeny",
		cache:false,
		dataType:"json",
		async:false,
		data:{items:items_val,comment:""},
		success:function (data){ 
			if(data.flag){
				$.jBox.tip('驳回成功', 'success');
				window.opener.$("#searchForm").submit(); //刷新父窗口
				window.location.reload();
			}else{
				$.jBox.tip(data.msg, 'error');
				return false;
			}
		},
		error: function (){
			$.jBox.tip("驳回失败", 'error');
			return false;
		}
	})
}

//付款审核-通过某条成本
function passCost(dom,id,reviewLevel){
	var items_val = id +"," + reviewLevel + "," + ${reviewCompanyId} + ",7";
	$.ajax({
		type: "POST",
		url: "${ctx}/pay/review/batchPass",
		cache:false,
		dataType:"json",
		async:false,
		data:{items:items_val,comment:""},
		success:function (data){ 
			if(data.flag){
				$.jBox.tip('审核通过', 'success');
				window.opener.$("#searchForm").submit(); //刷新父窗口
				window.location.reload();
			}else{
				$.jBox.tip(data.msg, 'error');
				return false;
			}
		},
		error:function (){
			$.jBox.tip("审核失败", 'error');
			return false;
		}
	})
}

function selectchange(parentId){
	if(null!= parentId && ""!=parentId){
		var noCache = Date();
		$.getJSON("${ctx}/pay/manager/supplylist/"+parentId,{"noCache":noCache},function(myJSON){
			var options="";
			if(myJSON.length>0){
				options+="<option value=''>==请选择类型==</option>";
				for(var i=0;i<myJSON.length;i++){
					options+="<option value="+myJSON[i].supplierid+">"+myJSON[i].suppliername+"</option>"; 
				}
				$("#supplier").html(options);
				$("#second").show();
			}else if(myJSON.length<=0){
				
			}
		});
	}
}

function saveCheckBox(id){
	var tmp = '';
	$("input[name='"+id+"']").each(function(){ 
		if ($(this).attr("checked") != null && $(this).attr("checked")=="checked"){
			var val = $(this).attr('value') + ',' + ${reviewCompanyId} + ',7'
			if(tmp == ''){
				tmp = val;
			}else{
				tmp += "&&" + val;
			}
		}
	});
	if(tmp == ''){
		$.jBox.tip("请选择需要审核的项目", 'error');
		return false;
	}
	$.ajax({
		type: "POST",
		url: "${ctx}/pay/review/batchPass",//saveCostList
		cache:false,
		dataType:"json",
		async:false,
		data:{items:tmp,comment:''},
		success:function(data){
			if(data.flag){
				$.jBox.tip('审核通过', 'success');
				window.opener.$("#searchForm").submit(); //刷新父窗口
				window.location.reload();
			}else{
				$.jBox.tip(data.msg, 'error');
			}
		},
		error : function(e){
			$.jBox.tip("审核失败", 'error');
			return false;
		}
	});
}
</script>
</head>
<body>                 
      <page:applyDecorator name="pay_review_head">
      <page:param name="current">airticket</page:param>
      </page:applyDecorator>
   
   
<input type="hidden" name="airTicketId" id="airTicketId" value="${airticket.id}"/>
<input type="hidden" name="budgetType" id="budgetType" value="1"/>
		<!--右侧内容部分开始-->
			<div class="mod_nav">财务 > 财务审核 > 付款审核 > 机票付款审核</div>
			
			<div class="produceDiv">
				 <div class="orderdetails_tit"><span>1</span>机票产品信息</div>
     
      <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">

        <p class="ydbz_mc">
         产品编号: ${activityAirTicket.productCode}, ${activityAirTicket.departureCityLabel()}&mdash;<c:forEach items="${areas}" var="arrivedcity">
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
  </div>



				
      <div class="mod_information">
          <div class="mod_information_d">
            <div class="ydbz_tit">订单列表</div>
          </div>
        </div>
        <table class="activitylist_bodyer_table mod_information_dzhan" id="contentTable">
          <thead>
            <tr>
                        <th width="8%">预定渠道</th>
                        <th width="8%">订单号</th>
                        <th width="7%">销售</th>
                        <th width="13%">预订/剩余时间</th>
                        <th width="9%">出/截团日期</th>
                        <th width="7%">机票类型</th>
                        <th width="4%">人数</th>
                        <th width="8%">订单状态</th>
                        <th width="7%">订单总额</th>
                        <th width="8%">已付金额<br/>到账金额</th>
                        <th width="8%">备注</th>
                        <th width="5%">操作</th>                       
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${airTicketOrderList}" var="airTicketOrder">
            <tr >
              <td>${airTicketOrder.agentName}</td>
              <td>${airTicketOrder.orderNo}</td>
              <td class="tc">${airTicketOrder.orderUserName}</td>
              <td class="p0">
                <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${airTicketOrder.createDate }"/></div>
                <div class="close-date">${airTicketOrder.leftDays }</div>
              </td>
              <td style="padding: 0px;">
                <div class="out-date">${airTicketOrder.groupOpenDate}</div>
                <div class="close-date">${airTicketOrder.groupCloseDate}</div>
              </td>
              <td>
              <c:if test="${airTicketOrder.airType=='3'}">单程</c:if>
              <c:if test="${airTicketOrder.airType=='1'}">多段</c:if>
              <c:if test="${airTicketOrder.airType=='2'}">往返</c:if>
              </td>
              <td class="tr">${airTicketOrder.personNum}</td>
              <td>${fns:getDictLabel(airTicketOrder.order_state, "order_pay_status", "")}</td>
              <td class="tr"><span class="tdorange fbold">${airTicketOrder.totalMoney} </span></td>
              <td class="p0 tr">  
                <div class="yfje_dd"> 
                  <span class="fbold">${airTicketOrder.payedMoney}</span>
                </div>
                <div class="dzje_dd">
                  <span class="fbold">${airTicketOrder.accountedMoney}</span>
                </div>
              </td>
              <td>${airTicketOrder.comments}</td>
              <td class="tc"><a target="_blank" href="${ctx}/order/manage/airticketOrderDetail?orderId=${airTicketOrder.orderId}" onclick="">详情</a></td>
            </tr>
            </c:forEach>
          </tbody>
        </table>  		
	

      <!-- 
      <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            预算成本                          
                    </div>
                </div>
                <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="10%">境内/外项目</th>
                            <th width="15%">项目名称</th>
                            <th width="10%">数量</th>

                            <th width="13%">批发商/地接社</th>

                            <th width="10%">币种</th>
                            <th width="10%">金额</th>
                            <th width="12%">备注</th>
                            <th width="8%">状态</th>
                            <th width="8%">操作</th>
                            <th width="8%">选择</th>
                        </tr>
                    </thead>
                    <tbody>            
                        <c:forEach items="${budgetInList}" var="budgetIn" varStatus="status">
                        <tr class="budgetInCost">
                        <c:if test="${status.count==1}">
                         <td  rowspan="${fn:length(budgetInList)}">境内付款明细</td>
                         </c:if>
                        <td  name="tdName">${budgetIn.name}</td>
                        <td  class="tr" name="tdAccount">${budgetIn.quantity}</td>
                        <td  name="tdSupply">
                        ${budgetIn.supplyName}
                         </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetIn.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tr" name="tdPrice">
                         <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetIn.currencyId}">
                             ${currency.currencyMark}${budgetIn.price}
                          </c:if>
                        </c:forEach></td>
                        <td  name="tdComment">${budgetIn.comment}</td>
                        <td  class="tc">
                        <c:if test="${budgetIn.reviewType==0}">
                         <c:forEach items="${review_cost}" var="review_cost">
                          <c:if test="${review_cost.value==budgetIn.review}">
                             ${review_cost.label}
                          </c:if>
                        </c:forEach>
                        </c:if>
                         <c:if test="${budgetIn.reviewType!=0}">
                         ${budgetIn.reviewStatus}
                         </c:if>
                        </td>
                        <td class="tc">
                         <c:if test="${budgetIn.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetIn.nowLevel}">
                                 <a href="javascript:void(0)" onclick="passCost(this,'${budgetIn.id}','${budgetIn.nowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${budgetIn.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>     
                        </td>
                        <td>                         
                          <c:if test="${budgetIn.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetIn.nowLevel}">  
                          <input type="checkbox" value="${budgetIn.id}"  name="budgetId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>       
                       </tr>
                        </c:forEach>
                    </tbody>
                    <tfoot>
                    <c:if test="${! empty budgetInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="9">&nbsp;<span id="budgetInShow" name="budgetInShow"></span></td>
                    </tr>
                  </c:if>
                 <c:forEach items="${budgetOutList}" var="budgetOut" varStatus="status">
                        <tr class="budgetOutCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(budgetOutList)}">境外付款明细</td>
                         </c:if>
                        <td name="tdName">${budgetOut.name}</td>
                        <td class="tr" name="tdAccount">${budgetOut.quantity}</td>
                        <td name="tdSupply">
                       ${budgetOut.supplyName}
                         </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==budgetOut.currencyId}">
                             ${currency.currencyMark}${budgetOut.price}
                          </c:if>
                        </c:forEach></td>
                        <td  name="tdComment">${budgetOut.comment}</td>
                         <td  class="tc" width="8%">
                      <c:if test="${budgetOut.reviewType==0}">
                         <c:forEach items="${review_cost}" var="review_cost">
                          <c:if test="${review_cost.value==budgetOut.review}">
                             ${review_cost.label}
                          </c:if>
                        </c:forEach>
                        </c:if>
                         <c:if test="${budgetOut.reviewType!=0}">
                         ${budgetOut.reviewStatus}
                         </c:if>
                        </td>
                        <td class="tc">
                             <c:if test="${budgetOut.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetOut.nowLevel}">
                                 <a href="javascript:void(0)" onclick="passCost(this,'${budgetOut.id}','${budgetOut.nowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${budgetOut.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>                          
                        </td>
                        <td>
                         <c:if test="${budgetOut.review ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==budgetOut.nowLevel}">  
                          <input type="checkbox" value="${budgetOut.id}"  name="budgetId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>   
                        </tr>
                        </c:forEach>                       
                  
                    <c:if test="${! empty budgetOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan="9">&nbsp;<span id="budgetOutShow" name="budgetOutShow"></td>
                    </tr></c:if>
                    </tbody>
                </table>



               <div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
          <ul class="cost-ul" data-total="cost">
          <ul class="cost-ul" data-total="income">
                      <li>预计总收入：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney') }"/>
</li>                   
                    </ul>
                      <li>预计总成本：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(budgetCost,'cost')}"/>
</li>               
                     </ul>
                  <ul class="cost-ul" data-total="profit">
                      <li>预计总毛利：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'totalMoney')-fns:getSum(budgetCost,'cost') }"/>
</li>                      
                     </ul> 
                    <ul style="margin: 0px;float: right;">
                    <input class="btn-primary" type="button" value="审核通过"  onclick="saveCheckBox('budgetId')" />                        
                    </ul>     
        </div>   -->

            <div class="mod_information">
                    <div class="mod_information_d">
                        <div class="ydbz_tit">
                            实际成本                           
                        </div>
                    </div>
                </div>
                <table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
                    <thead>
                        <tr>
                            <th width="10%">境内/外项目</th>
                            <th width="15%">项目名称</th>
                            <th width="10%">数量</th>
                            <th width="13%">批发商/地接社</th>
                            <th width="10%">币种</th>
                            <th width="10%">金额</th>
                            <th width="12%">备注</th>
                            <th width="8%">付款审核状态</th>
                            <th width="8%">操作</th>
                            <th width="8%">选择</th>
                        </tr>
                    </thead>
            
                        <c:forEach items="${actualInList}" var="actualIn" varStatus="status">
                        <tr  class="actualInCost">
                        <c:if test="${status.count==1}">
                         <td rowspan="${fn:length(actualInList)}">境内付款明细</td>
                         </c:if>
                        <td name="tdName">${actualIn.name}</td>
                        <td class="tr" name="tdAccount">${actualIn.quantity}</td>
                        <td name="tdSupply">
                          ${actualIn.supplyName}
                         </td>
                        <td name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualIn.currencyId}">
                             ${currency.currencyMark}${actualIn.price}
                          </c:if>
                        </c:forEach></td>
                        <td  name="tdComment">${actualIn.comment}</td>
                         <td  class="tc">
                         <c:if test="${actualIn.reviewType==0}">
                           ${fns:getNextPayReview(actualIn.id)}   
                        </c:if>
                         <c:if test="${actualIn.reviewType!=0}">
                         <font color="green">${actualIn.reviewStatus}</font>
                         </c:if>
                        </td>
                        <td class="tc">                        
                         <c:if test="${actualIn.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualIn.payNowLevel}">
                               <a href="javascript:void(0)" onclick="passCost(this,'${actualIn.id}','${actualIn.payNowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${actualIn.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>  
                        </td>
                        <td>
                         <c:if test="${actualIn.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualIn.payNowLevel}">  
                          <input type="checkbox" value="${actualIn.id},${actualIn.payNowLevel}"  name="actualId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>            
                       </tr>
                        </c:forEach>                   
                    <c:if test="${! empty actualInList}">
                    <tr>
                        <td>境内小计</td>
                        <td colspan="9">&nbsp;<span id="actualInShow" name="actualInShow"></td>
                    </tr></c:if>
                    <c:forEach items="${actualOutList}" var="actualOut" varStatus="status">
                        <tr class="actualOutCost">
                        <c:if test="${status.count==1}">
                         <td  rowspan="${fn:length(actualOutList)}">境外付款明细</td>
                         </c:if>
                        <td  name="tdName">${actualOut.name}</td>
                        <td  class="tr" name="tdAccount">${actualOut.quantity}</td>
                        <td  name="tdSupply">
                       ${actualOut.supplyName}
                         </td>
                        <td  name="tdCurrencyName">
                        <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyName}
                          </c:if>
                        </c:forEach>
                        </td>
                        <td  class="tr" name="tdPrice"> <c:forEach items="${curlist}" var="currency">
                          <c:if test="${currency.id==actualOut.currencyId}">
                             ${currency.currencyMark}${actualOut.price}
                          </c:if>
                        </c:forEach></td>
                      <td  name="tdComment">${actualOut.comment}</td>
                        <td width="8%" class="tc">
                         <c:if test="${actualOut.reviewType==0}">
                          ${fns:getNextPayReview(actualOut.id)}   
                        </c:if>
                         <c:if test="${actualOut.reviewType!=0}">
                         <font color="green">${actualOut.reviewStatus}</font>
                         </c:if>
                        </td>
                      <td class="tc">                         
                          <c:if test="${actualOut.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualOut.payNowLevel}">
                               <a href="javascript:void(0)" onclick="passCost(this,'${actualOut.id}','${actualOut.payNowLevel}')">通过</a>&nbsp;<a href="javascript:void(0)" onclick="denyCost(this,'${actualOut.id}','0')">拒绝</a>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>
                          <td>
                         <c:if test="${actualOut.payReview ==1 }">
                           <c:forEach items="${levelList}" var="levelList">
                           <c:if test="${levelList==actualOut.payNowLevel}">  
                          <input type="checkbox" value="${actualOut.id},${actualOut.payNowLevel}"  name="actualId"/>
                             </c:if>
                          </c:forEach>
                         </c:if>
                        </td>                               
                        </tr>
                        </c:forEach>                        
                    </tbody>
                    <tfoot>
                    <c:if test="${! empty actualOutList}">
                    <tr>
                        <td>境外小计</td>
                        <td colspan="9">&nbsp;<span id="actualOutShow" name="actualOutShow"></td>
                    </tr></c:if>
                    </tfoot>
                </table>    
       

                <div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
          <ul class="cost-ul" data-total="cost">
          <ul class="cost-ul" data-total="income">
                      <li>实际总收入：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney') }"/>
</li>                   
                    </ul>
                      <li>实际总成本：&nbsp;￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(actualCost,'cost')}"/>
</li>               
                     </ul>
                  <ul class="cost-ul" data-total="profit">
                      <li>实际总毛利：￥<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${fns:getSum(incomeList,'accountedMoney')-fns:getSum(actualCost,'cost') }"/>
</li>                      
                     </ul> 
                  <ul style="margin: 0px;float: right;">
                    <input class="btn-primary" type="button" value="审核通过"  onclick="saveCheckBox('actualId')" />                        
                    </ul>  
               </div>   

     </div>


    
         <div class="mod_information">
          <div class="mod_information_d">
            <div class="ydbz_tit">审核日志</div>
          </div>
        </div>    
        <div style="margin:0 auto; width:98%;">
                    <ul class="spdtai">
                        <c:forEach items="${costLog}" var="log" varStatus="status">
                     <li>   <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${log.createDate}"/>&#12288;【${log.costName}】<c:if test="${log.result==-1}"><span class="invoice_back">审核已撤销</span></c:if><c:if test="${log.result==0}"><span class="invoice_back">审核未通过</span></c:if><c:if test="${log.result==1}"><span class="invoice_yes">审核通过</span></c:if>&#12288;【${log.name}】&#12288; <c:if test="${!empty log.remark}"><font color="red">批注:</font>&nbsp;${log.remark} </c:if></li>
                         </c:forEach>
                     </ul>
                </div>

                </div>


            <div class="release_next_add">
               <input class="btn btn-primary" type="button" value="关 闭"  onclick="javascript:window.opener=null;window.open('','_self');window.close();" /> 
            </div>
     </div> 
            <!-- <div class="release_next_add">
             <c:if test="${reviewLevel > 0}">                         
                <input class="btn btn-primary" type="button" value="驳 回"  onclick="denyAirCost('${activityAirTicket.id}')" />  <input class="btn btn-primary" type="button" value="审核通过"  onclick="passAirCost(this,'${activityAirTicket.id}','0')" /> 
             </c:if>
             <c:if test="${reviewLevel <=0}">
                没有审核权限
             </c:if>
            </div> -->


			  <div id="agent" style="display:none">
         <select name="agentId" id="agentId" style="width:180px">
         <option value="" >===请选择===</option>
                <option value="-1" >非签约渠道</option>
                <c:forEach var="agentinfo" items="${agentinfoList }"> 
                <option value="${agentinfo.id }" <c:if
                test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                </c:forEach>
            </select>          
            <div id="currency">
            <select style="width:75px; margin-right:5px;" id="currencyId" name="currencyId">
                    <c:forEach items="${curlist}" var="currency">
                        <option value="${currency.id}">
                               ${currency.currencyName} 
                        </option>
                    </c:forEach>
                </select><input type="text" name="price" id="price"  style="width:100px"/>
                </div>
                <select style="width:180px" onchange="selectchange(this.value)" id="first" name="first">
                    <c:forEach items="${supplytypelist}" var="dict">
                        <option value="${dict.value}">
                               ${dict.label} 
                        </option>
                    </c:forEach>
                </select>                   
       </div>
       
 
    <!--修改项目模板开始-->
            <div id="addItem" style="display:none;">
                <label>境内外项目选择：</label><select id="detailType0" name="detailType0">
                    <option value="0" selected="selected">境内明细</option>
                    <option value="1">境外明细</option>
                </select><br />
               <!-- <label>批发商类别选择：</label><select id="supplyClassify0" name="supplyClassify0">
                    <option value="1" selected="selected">签约渠道</option>
                    <option value="0">非签约渠道</option>
                </select><br />-->
                <label>批发商：</label> 批发商不能改 <!--<select id="supply0" name="supply0">
                <c:forEach items="${companyList}" var="company">
                <option value="${company.id}">${company.name}</option>
                </c:forEach>
                </select> --> <br />
                <label>项目名称：</label><input type="text" id="name0" name="name0" value="" /><br />
                <label>单价：</label><select style="width:75px; margin-right:20px;" id="currencyType0" name="currencyType0">
                    <c:forEach items="${curlist}" var="currency">
                        <option value="${currency.id}">
                               ${currency.currencyMark} 
                        </option>
                    </c:forEach>
                </select><input type="text" style="width:110px;" id="price0" name="price0" onblur="validNumFinally(this)" onafterpaste="validNum(this))" onkeyup="validNum(this)" /><br />
                <label>数量：</label><input type="text" class="inputTxt" style="width:60px;" id="account0" name="account0" onkeyup="this.value=this.value.replace(/\D/,'')" onafterpast="this.value=this.value.replace(/\D/,'')" /><br />
                <label>项目备注：</label><textarea rows="" cols="" id="comment0" name="comment0"></textarea>
            </div>


<script type="text/javascript">

//运控-成本录入-添加项目--小计
function costSums(obj,objshow){    
  var objMoney = {}; 
     obj.each(function(index, element) { 
    //var currencyName = $(element).find("td[name='tdCurrencyName']").text();
    var thisAccount = $(element).find("td[name='tdAccount']").text();

    var thisPrice = $(element).find("td[name='tdPrice']").text();   
    var border=2;
    //去掉两边空格
    thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
    //找到金额中第一个数字位置
        for(var i=0;i<thisPrice.length;i++){
         if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
           border=i;      
       break;
      }
    }
        var currencyName =thisPrice.substring(0,border);
    //金额去掉第一个字符(币种)
    thisPrice=thisPrice.substring(border);        
    if(typeof objMoney[currencyName] == "undefined"){
      objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
    }else{
      objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
    }
  });
  //输出结果
  var strCurrency = "";
  var sign = " + "; 
  for(var i in objMoney){
    var isNegative = /^\-/.test(objMoney[i]);
    if(isNegative){
      sign = " - ";
    }
    if(strCurrency != '' || (strCurrency == '' && isNegative)){
      strCurrency += sign;
    }
    strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
  }
  if(objshow.length>0) objshow.text("  "+strCurrency);
}

$(function(){
    //实际成本录入-境内小计
    costSums($('tr.budgetInCost'),$('#budgetInShow'));
    //实际成本录入-境外小计  
    costSums($('tr.budgetOutCost'),$("#budgetOutShow"));

    costSums($('tr.actualInCost'),$("#actualInShow"));
    
    costSums($('tr.actualOutCost'),$("#actualOutShow")); 

});
</script>
    
</body>
</html>