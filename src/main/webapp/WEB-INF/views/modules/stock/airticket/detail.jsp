<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>库存详情</title>
    <meta name="decorator" content="wholesaler"/>
    <style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/flexbox-0.9.6/FlexBox/css/jquery.flexbox.css" />
    <script src="${ctxStatic}/jquery-other/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactorStock.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/flexbox-0.9.6/FlexBox/js/jquery.flexbox.js"></script>
	<script type="text/javascript">
    function showAll(){
        $(".grouprow").each(function(){
        	$(this).show();
        });
    }
    function getChickList(){
        var dateArr = new Array();
        var date;
        var index=0;
        dateArr[0] = new Date();
         <%--<c:forEach items="${travelActivity.activityGroups}" var="group" varStatus="status">--%>
            <%--<c:if test="${group.leftdays < 0 }">--%>
                <%--date = '<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>'.split("-");--%>
                <%--dateArr[index] = new Date(date[0],date[1]-1,date[2]);--%>
                <%--index++;--%>
            <%--</c:if>--%>
        <%--</c:forEach>--%>
        return dateArr;
    }
	  $().ready(function(){
		  
		  if("${airticketId}"){
			  $(".team_a_click").hide();
			  $(".team_a_click").next("img").hide();	
			  var cloneTbody = $("#contentTable").clone();
			  $("#contentTableclone").append(cloneTbody);
			  $("#contentTableclone").attr("width","100%");
			  $("#contentTableclone").find("tr [class!='1']").remove();
			  $("#contentTableclone").find("thead").remove();
			  $("#contentTableclone").find("#contentTable").remove();
			  $("#contentTableclone").find("tbody").remove();
		  }
		  
          var datepicker = $(".groupDate").datepickerRefactor({dateFormat: "yy-mm-dd",target:"#dateList",numberOfMonths: 3,isChickArr:getChickList(),defaultDate:'${travelActivity.groupOpenDate }'},"#groupOpenDate","#groupCloseDate");
        	var thisClass = "";
        	$(".team_a_click").click(function(){
        		thisClass = $(this).attr("class").substring(12,19);
        		if($(this).parent().hasClass('td-extend')){
                    $(this).parent().attr("class",thisClass + " tr");
                    $(this).next().show();
                }else{
                    var objs=$(this).parents("tr").find("[class^='td-extend']");
                    if(objs.length>0){
                        objs.removeClass('td-extend');
                        objs.find(".team_a_click").next().show();
                    }
                    $(this).parent().attr("class","td-extend tr "+thisClass);
                    $(this).next().hide();
                }
        	});
        })
		function expand(current,obj,child,child1,child2){
			$(child).hide();
           	$(child1).hide();
           	$(child2).hide();
			if("${airticketId}"){
					$(current).show();        			
        		}else{
        			$(current).toggle();
        		}
           	if("${airticketId}"){
				$("#contentTableclone").append($(current));  
				$(".cDetail-ul").find(".cDetail-ul-active").attr("class","");
				$(obj).parent().attr("class","cDetail-ul-active");
			}
		}
        //ajax
        function soldNopayPosition(current,obj,child,child1,child2,productGroupId){
        	$(child).hide();
           	$(child1).hide();
           	$(child2).hide();
           	var trs = $(current).find("tbody").find("tr");     
        	if(trs.length!=0){
        		if(productGroupId){
					$(current).show();        			
        		}else{
        			$(current).toggle();
        		}
        	}      		
        	else{
        		var noCache = Date();
        		$.getJSON("${ctx}/stock/manager/airticket/soldNopayPosition/"+productGroupId,{"noCache":noCache},
        			function(result){
        				if(result.length==0){
        					$(current).find("tbody").empty();
        					var tr = $("<tr></tr>");
        					$(tr).append("<td class='tc' colspan=\"10\">没有记录</td>");
        					if(productGroupId){
								$("#contentTableclone").append($(current));  
        					}
        					$(current).find("tbody").append(tr);
        					$(current).show();
        				}else{
        					var numFlag = "";
    						var numCount = 0;
    						var tr;
    						$(current).find("tbody").empty();
        					$(result).each(function(index,obj){
        						if(numFlag == "")
    								numFlag = obj.orderCompany;
    							if(obj.orderCompany != numFlag){
    								var str = $(current).find("tbody").find("tr")[index-numCount];
    								var td1 = $(str).find("td")[0];
    								var td2 = $(str).find("td")[1];
    								var td3 = $(str).find("td:eq(2)");
    								$(td1).attr("rowspan",numCount);
    								$(td2).attr("rowspan",numCount);
    								$(td3).attr("rowspan",numCount);
    								numFlag = obj.orderCompany;
    								numCount = 0;
    							}
    							tr = $("<tr></tr>");
    		             		if(numCount==0){
    		             			$(tr).append("<td class='tc'>"+obj.agentName+"</td>")
										.append("<td class='tc'>"+obj.payReservePosition+"</td>")
										.append("<td class='tc'>"+obj.leftpayReservePosition+"</td>");
    		             		}
    		             		var ps = obj.orderState==null?"":obj.orderState;
        						//$(tr).append("<td class='tc'>"+obj.createUserName+"</td>")
        						$(tr).append("<td class='tc'> <span class='order-picker onshow'>" + obj.createUserName + "</span>" + "<span class='order-saler'>" + obj.salerName + "</span>")
        						.append("<td class='tc'>"+obj.orderNo+"</td>")
        						.append("<td class='tr'>"+obj.totalMoney+"</td>")
        						.append("<td class='tr'>"+obj.payMoney+"</td>")
        						.append("<td class='tc'>"+obj.personNum+"</td>")
        						.append("<td class='tc'>"+obj.payStatus+"</td>")
        						.append("<td class='tl'>" + obj.orderRemark + "</td>");
        						numCount++;	
        						$(current).find("tbody").append(tr);
        						if(index==result.length-1){
    								var str = $(current).find("tbody").find("tr")[index+1-numCount];
    								var td1 = $(str).find("td")[0];
    								var td2 = $(str).find("td")[1];
    								var td3 = $(str).find("td:eq(2)");
    								$(td1).attr("rowspan",numCount);
    								$(td2).attr("rowspan",numCount);
    								$(td3).attr("rowspan",numCount);
    							}
        					});
        					if(productGroupId){
								$("#contentTableclone").append($(current));  
        					}
        					$(current).show();
        				}
        			}
        		);
        	}
        	if(productGroupId){
				$(".cDetail-ul").find(".cDetail-ul-active").attr("class","");
				$(obj).parent().attr("class","cDetail-ul-active");
			}
        }
       function tolist(){
          window.location.href = "${ctx}/stock/manager/airticket/?agentId=";
      }
        
        //ajax
		function orderPersonNum(current,obj,child,child1,child2,productGroupId,status){
        	$(child).hide();
           	$(child1).hide();
           	$(child2).hide();
           	if(productGroupId){
                $(current).show();                  
            }else{
                $(current).toggle();
            }
            var trs = $(current).find("tbody").find("tr"); 
            /*if(trs.length!=0){
                if(productGroupId){
                    $(current).show();                  
                }else{
                    $(current).toggle();
                }
            }          
            else*/ 
            if(trs.length == 0){
        		$.ajax({
			        type: "POST",
			        url: "${ctx}/stock/manager/airticket/airticketDetail",
			        data: {
			        	productGroupId:productGroupId,
			        	status:status
			        },
			         success: function(msg){
			         	if(msg==""){
			         		$(current).find("tbody").empty();
        					var tr = $("<tr></tr>");
        					var colspanNum = status=="nopay"?5:6;
        					$(tr).append("<td class='tc' colspan=\""+colspanNum+"\">没有记录</td>");
        					$(current).find("tbody").append(tr);
        					$(current).show();
        				}else{
				        	var orderPersonNum = 0;
				        	var agentName = null;
				        	var tbody = null;
				        	var tbody1 = null;
				        	var tbody2 = null;
				        	var count = 0;
				        	$(current).find("tbody").empty();
				        $(msg).each(function(index1,obj1){
							tbody1 = "<td class='tc' rowspan="+ obj1.length+">"+obj1[0].agentName+"</td>";
					        $(obj1).each(function(index2,obj2){
						        	if(status==""){
										//tbody2 = "<td class='tc'>"+obj2.createUserName+"</td><td class='tc'>"+obj2.orderNo+"</td><td class='tr'>"+obj2.totalMoney+"元</td><td class='tc'>"+obj2.personNum+"</td>";
						        		 tbody2 = "<td class='tc'> <span class='order-picker onshow'>" + obj2.createUserName + "</span>" 
                                         + "<span class='order-saler'>" + obj2.salerName + "</span>"
                                         +"</td><td class='tc'>"+obj2.orderNo+"</td><td class='tr'>"+obj2.totalMoney+"元</td><td class='tc'>"+obj2.personNum+"</td>";
						        	}else{
						        		var payStatus = null;						        		
						        		switch (obj2.order_state)
						        		{
						        		   
						        			case 1:
						        				payStatus = "未收全款";
						        			break;
						        			case 2:
						        				payStatus = "未收订金";
						        			break;
						        			case 3:
						        				payStatus = "已占位";
						        			break;
						        			case 4:
						        				payStatus = "已收订金 ";
						        			break;
						        			case 5:
						        				payStatus = "已收全款";
						        			break;
						        			case 99:
						        				payStatus = '<span style="color:#eb0205">已取消</span>';
						        			break;
						        			case 111:
						        				payStatus = '<span style="color:#eb0205">已删除</span>';
						        			break;
						        			default:
						        				payStatus = '';
						        			break;
						        		}
						        		var orderPersonNumColor = " ";
						        		if(payStatus==99|| payStatus==111)
						        			orderPersonNumColor = "style='color:#eb0205'";
						        		//tbody2 = "<td class='tc'>"+obj2.createUserName+"</td><td class='tc' "+orderPersonNumColor+">"+obj2.personNum+"</td><td class='tc'>"+payStatus+  "</td>";
						        		 tbody2 = "<td class='tc'> <span class='order-picker onshow'>" + obj2.createUserName + "</span>" 
                                         + "<span class='order-saler'>" + obj2.salerName + "</span>"
                                         +"</td><td class='tc' "+orderPersonNumColor+">"+obj2.personNum+"</td><td class='tc'>"+payStatus+  "</td>";
					        		}
								if(count == 0){
									$(current).find("tbody")
									.append($("<tr></tr>")
									.append(tbody1)
									.append("<td class=\"orderCompanyNum\" rowspan="+obj1.length+">"+orderPersonNum+"</td>")
									.append(tbody2));
								}else{
									$(current).find("tbody")
									.append($("<tr></tr>")
									.append(tbody2));
									}
								if(obj2.orderState!=99 && obj2.orderState!=111)
									orderPersonNum+=obj2.personNum;
								count++;
				        	});
				        	$(current).find(".orderCompanyNum").text(orderPersonNum);
				        	$(current).find(".orderCompanyNum").attr("class","tc");
				        	orderPersonNum = 0;
				        	count = 0;
						});
					}
				}
			});
        		if(productGroupId){
					$("#contentTableclone").append($(current));  
   					}
			$(current).show();
        	}
        	if(productGroupId){
				$(".cDetail-ul").find(".cDetail-ul-active").attr("class","");
				$(obj).parent().attr("class","cDetail-ul-active");
			}
        }		
	   // 删除确认对话框
        function confirmxDel(mess, href,proId){
            top.$.jBox.confirm(mess,'系统提示',function(v){
                if(v=='ok'){
                    loading('正在提交，请稍等...');
                    window.location="${ctx}/stock/manager/airticket/delete?id=${param.id}&agentId=${param.agentId }&groupreserveId="+proId;
                }
            },{buttonsFocus:1});
            top.$('.jbox-body .jbox-icon').css('top','55px');
            return false;
        }
        $(function () {
            //table中团号、产品名称切换
 			switchSalerAndPicker();
            switchNumAndPro();

        });
        function switchSalerAndPicker() {
        	//点击团号
        	$(document).on("click", ".order-saler-title", function () {
        	   $(this).addClass("on").siblings().removeClass('on');
        	   var $table = $(this).parents('table:first');
        	   $table.find('.order-saler').addClass('onshow');
        	   $table.find('.order-picker').removeClass('onshow');
        	});
        	//点击产品
        	$(document).on("click", ".order-picker-title", function () {
        	   $(this).addClass("on").siblings().removeClass('on');
        	   var $table = $(this).parents('table:first');
        	   $table.find('.order-saler').removeClass('onshow');
        	   $table.find('.order-picker').addClass('onshow');
        	});
           }
        
</script>
<style type="text/css">
body{ font-size:12px;}
.activity_team_top1 .team_top table {
    border-width: 0;
    border-style: solid;
}
</style>
</head>
<body>
       <page:applyDecorator name="stock_op_head">
      <page:param name="current">flightStock</page:param>
      </page:applyDecorator>
            <div class="ydbzbox fs">
<%--            <div class="mod_nav departure_title">库存详情</div>--%>
       <div class="orderdetails_tit"><span>1</span>机票产品信息</div>





 <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden">

        <p class="ydbz_mc">产品编号: ${activityAirTicket.productCode}, ${activityAirTicket.departureCityLabel() } &mdash;<c:forEach items="${arrivedcitylist}" var="arrivedcity">         
                    <c:if test="${arrivedcity.id eq activityAirTicket.arrivedCity}">
                     ${arrivedcity.name}
                    </c:if>
                   </c:forEach>
            (<c:if test="${ activityAirTicket.airType ==1}">多段</c:if>
            <c:if test="${ activityAirTicket.airType ==2}">往返</c:if>
            <c:if test="${ activityAirTicket.airType ==3}">单程</c:if>)
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
                            <c:forEach items="${airportlist}" var="airport">
                             <c:if test="${airport.id eq flightInfos.leaveAirport}">
                             ${airport.airportName}
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
                             <c:if test="${airportlist.id eq activityAirTicket.flightInfos[1].destinationAirpost}">
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



			<div class="orderdetails_tit">
				<span>2</span>机票信息
			</div>

    <table id="contentTable" class="table table-stock table-bordered table-condensed psf_table_son table-mod2-group activitylist_bodyer_table">
        <thead style="background:#403738">
            <tr>
                <th rowspan="2">产品编号</th>
                <th rowspan="2">预收</th>
                <th colspan="2">已分配人数</th>
                <th colspan="2">已售出人数</th>
                <th rowspan="2">余位</th>
                <th rowspan="2">操作</th>
            </tr>
            <tr>
                <th>占位人数</th>
                <th>切位人数</th>
                <th>售出占位</th>
                <th>售出切位</th>
            </tr>
        </thead>
                    
        <tbody>
            <tr>
                <td>${activityAirTicket.productCode}</td>
                <td>${activityAirTicket.reservationsNum}</td>

                <td>${activityAirTicket.nopayReservePosition}</td>
                <td>${activityAirTicket.payReservePosition}</td>
                <td>${activityAirTicket.soldNopayPosition}</td>
                <td>${activityAirTicket.soldPayPosition}</td>
                <td>${activityAirTicket.freePosition}</td>
                <td><a href="${ctx}/stock/manager/airticket/reserve?id=${activityAirTicket.id}"><c:if test="${showReserve!=0 }">申请切位</c:if></a></td>
            </tr>
            <tr id="reservePosition1" style="display:none" class="1 activity_team_top1">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;" >
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>切位总数</th>
                                    <th>预订人</th>
                                    <th>备注</th>
<%--                                     <th>操作</th>--%>
                                 </tr>
                            </thead>
                            <tbody>
                        <!--表单内容-->
	                        <c:if test="${empty airticketReserve}"><tr><td class="tc" colspan="4">没有记录</td></tr></c:if>
                            <c:forEach items="${airticketReserve}" var="groupReserve">
                                <tr>
                                    <td class='tc'>${groupReserve.agentName }</td>
                                    <td class='tc'>${groupReserve.payReservePosition }</td>
                                    <td class='tc'>${groupReserve.reservation }</td>
                                    <td class='tl'>${groupReserve.remark }</td>
<%--                                    <td><a href="${ctx}/stock/manager/revise?id=${param.id }&groupreserveId=${groupReserve.id }&agentId=${groupReserve.agentId }">修改切位</a>&nbsp;&nbsp;&nbsp;
                                    <a href="javascript:void(0)" onClick="return confirmxDel('要删除该切位记录吗？', this.href ,${groupReserve.id })">删除切位</a></td>--%>
                                </tr>

                            </c:forEach>
                            </tbody>
                        </table>
                    </td>
                </tr>
               <tr id="noPayReservePosition1" style="display:none" class="1 table-Second activity_team_top1 activity_team_top1_purple">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;" >
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>占位总数</th>
                                    <th>
                                    <span class="order-picker-title on">下单人</span>/
									<span class="order-saler-title">销售</span>
                                    </th>
                                    <th>占位人数</th>
                                    <th>订单状态</th>
                                 </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr id="soldPosition1" style="display:none" class="1 table-Second activity_team_top1 activity_team_top1_green">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table table-striped table-bordered table-condensed table-Second" style="margin-bottom: 1px;">
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>占位总数</th>
                                    <th>
                                    <span class="order-picker-title on">下单人</span>/
									<span class="order-saler-title">销售</span>
                                    </th>
                                    <th>订单号</th>
                                    <th>应收团款金额</th>
                                    <th>占位人数</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </td>
            	</tr>
            <tr id="soldNopayPosition1" style="display:none" class="1 activity_team_top1  activity_team_top1_orange">
                    <td class="team_top" colspan="10">
                        <table id="teamTable" class="table table-striped table-bordered table-condensed" style="margin-bottom: 1px;">
                            <thead>
                                <tr>
                                    <th>渠道名称</th>
                                    <th>切位总数</th>
                                    <th>切位空余数</th>
                                    <th>
                                    <span class="order-picker-title on">下单人</span>/
									<span class="order-saler-title">销售</span>
                                    </th>
                                    <th>订单号</th>
                                    <th>应收团款金额</th>
                                    <th>已收金额</th>
                                    <th>已售出切位</th>
                                    <th>状态</th>
                                    <th>备注</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </td>
           	 </tr>
        </tbody>
    </table>
    		<div class="cDetail">
				<ul class="cDetail-ul">
					<li class="">
              	 	<a id="close0" class="purple" href="javascript:void(0)" onClick="orderPersonNum('#noPayReservePosition1',this,'#reservePosition1','#soldNopayPosition1','#soldPosition1',${airticketId },'nopay')">已占位明细</a>
		              <em></em>
		              </li>
		              <li class="">
		              <a id="close1" class="blue" href="javascript:void(0)" onClick="expand('#reservePosition1',this,'#soldPosition1','#soldNopayPosition1','#noPayReservePosition1')">已切位明细</a>
		              <em></em>
		              </li>
		              <li class="">
		              <a id="close2" href="javascript:void(0)" class="green" onClick="orderPersonNum('#soldPosition1',this,'#reservePosition1','#soldNopayPosition1','#noPayReservePosition1',${airticketId },'')">售出占位明细</a>
		              <em></em>
		              </li>
		              <li class="">
		              <a id="close3" class="orange" href="javascript:void(0)" onClick="soldNopayPosition('#soldNopayPosition1',this,'#soldPosition1','#reservePosition1','#noPayReservePosition1',${airticketId })">售出切位明细</a>
	       				</li>
	       		</ul>
	       	</div>
		    <table id="contentTableclone">
		        </table>
            <div class="release_next_add">
                <input id="btnSubmit"  class="btn btn-primary" type="button" value="返 回" onClick="tolist()" />
            </div>           
     
   </div>
  </div>
</body>
</html>