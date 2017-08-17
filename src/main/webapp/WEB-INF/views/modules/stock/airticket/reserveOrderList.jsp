<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
%>
<html>
<head>
	<title>机票切位订单</title>
	<meta name="decorator" content="wholesaler"/>
	<style type="text/css">

	.p0{ padding:0;}
	.table .tr,.activitylist_bodyer_table .tr{ text-align:right;}
	.dzje_dd span{color:#009535;padding-right:5px;}
	.fbold{ font-weight:bold;}/**字号加粗**/
	</style>
<%-- 	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> --%>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
   <style type="text/css">
   </style>
	<script type="text/javascript">
	
	var activityIds = "";
	function selectQuery(){
	 $("#modifyAgentInfo").comboboxInquiry();
}
	$(function() {
		//added for 筛选项展开收起 by tlw at 20170223
		launch();
		//added for 显示排序 by tlw at 20170223
//	bug17482使用原有排序及显示方法 at20170308
	var _$orderBy = $("#orderBy").val();
            if(_$orderBy==""){
                _$orderBy="id DESC";
            }
            var orderBy = _$orderBy.split(" ");
            $(".activitylist_paixu_left li").each(function(){
                if ($(this).hasClass("li"+orderBy[0])){
                    orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                    $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                    $(this).attr("class","activitylist_paixu_moren");
                }
            });
    //加载操作按键
    $('.handle').hover(function() {
        if(0 != $(this).find('a').length){
            $(this).addClass('handle-on');
            $(this).find('dd').addClass('block');
        }
    },function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  
     	$( ".spinner" ).spinner({
				spin: function( event, ui ) {
				if ( ui.value > 365 ) {
					$( this ).spinner( "value", 1 );
					return false;
				} else if ( ui.value < 0 ) {
					$( this ).spinner( "value", 365 );
					return false;
				}
				}
			});
			$(".qtip").tooltip({
				track: true
			});
			
			$('.team_top').find('.table_activity_scroll').each(function(index, element) {
				var _gg=$(this).find('tr').length;
	            if(_gg>=20){
	            $(this).addClass("group_h_scroll");
	            $(this).prev().wrap("<div class='group_h_scroll_top'></div>");
	            }

		  });

			//如果展开部分有查询条件的话，默认展开，否则收起	
			var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum'][id!='departureCity'][id!='arrivedCity']");
			var searchFormselect = $("#searchForm").find("select");
			var inputRequest = false;
			var selectRequest = false;
			for(var i = 0; i<searchFormInput.length; i++) {
				if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
					inputRequest = true;
				}
			}
			for(var i = 0; i<searchFormselect.length; i++) {
				if($(searchFormselect[i]).children("option:selected").val() != "" && 
						$(searchFormselect[i]).children("option:selected").val() != null) {
					selectRequest = true;
				}
			}
			if(inputRequest||selectRequest) {
				$('.zksx').click();
			}
			
			activityIds = "${activityIds}";
		 	$("#groupform").validate({});
		 	jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息"
   			});
            $('.nav-tabs li').hover(function(){
                $('.nav-tabs li').removeClass('current');
                $(this).parent().removeClass('nav_current');
                 if($(this).hasClass('ernav'))
                 {
                     if(!$(this).hasClass('current')){
                        $(this).addClass('current');
                        $(this).parent().addClass('nav_current');
                     }
                 }
                },function(){
                    $('.nav-tabs li').removeClass('current');
                    $(this).parent().removeClass('nav_current');
                    var _active = $(".totalnav .active").eq(0);
                    if(_active.hasClass('ernav')){
                        _active.addClass('current');
                        $(this).parent().addClass('nav_current');
                    }
                });
            
            //渠道改为可输入的select
           
			selectQuery();
   			//产品名称获得焦点显示隐藏
			$("#wholeSalerKey").focusin(function(){
				var obj_this = $(this);
					obj_this.next("span").hide();
			}).focusout(function(){
				var obj_this = $(this);
				if(obj_this.val()!="") {
				obj_this.next("span").hide();
			}else
				obj_this.next("span").show();
			});
			if($("#wholeSalerKey").val()!="") {
				$("#wholeSalerKey").next("span").hide();
			}
			
			
        
           $('.team_top').find('.table_activity_scroll').each(function(index, element) {
             var _gg=$(this).find('tr').length;
            if(_gg>=20){
            $(this).addClass("group_h_scroll");}
          });
		});

	function page(n,s){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").attr("action","${ctx}/stock/manager/airticket/getReserveOrderList");
		$("#searchForm").submit();
	}
	function confirmBatchIsNull(mess,sta) {
		if(activityIds != ""){
			if(sta=='off'){
				confirmBatchOff(mess);
			}else if(sta=='del'){
				confirmBatchDel(mess);
			}
		}else{
			$.jBox.error('未选择产品','系统提示');
		}
	}
	var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
}
	
	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
			});	
		 });
	   
		
        function sortby(sortBy,obj){
            var temporderBy = $("#orderBy").val();
            if(temporderBy.match(sortBy)){
                sortBy = temporderBy;
                if(sortBy.match(/ASC/g)){
                    sortBy = sortBy.replace(/ASC/g,"");
                }else{
                    sortBy = $.trim(sortBy)+" ASC";
                }
            }

            $("#orderBy").val(sortBy);
            $("#searchForm").submit();
        }
        
    	function viewRefund(aid){
    		window.open("${ctx}/stock/manager/apartGroup/viewAirTicketRefund?orderId="+aid);
    	}

    
	</script>
</head>
<body>
<%--added for UG_V2 添加tab at 20170223 by tlw start.--%>
<content tag="three_level_menu">
	<li class="active">
		<a href="javascript:void(0)">全部订单</a>
	</li>
</content>
<%--added for UG_V2 添加tab at 20170223 by tlw end.--%>
<div class="activitylist_bodyer_right_team_co_bgcolor">
<!--   <div class="activitylist_bodyer_right_team_co_bgcolor">-->
	<form:form id="searchForm" modelAttribute="airTicket" action="${ctx}/stock/manager/airticket/getReserveOrderList" method="post" >
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
    	<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2">
				<input class="radius4 inputTxt inquiry_left_text" id="departureCity" name="departureCity" placeholder="出发城市" value="${departureCityd }"/>
				—
				<input class="radius4 inputTxt inquiry_left_text" id="arrivedCity" name="arrivedCity" placeholder="到达城市" value="${arrivedCityd }"/>
			</div>
			<div class="zksx filterButton_solo" >筛选</div>
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" type="submit" value="搜索">
				<input class="btn ydbz_x " type="button" onclick="resetSearchParams()" value="清空所有条件"/>
			</div>
			<div class="ydxbd">
				<span></span>
				<div class="activitylist_bodyer_right_team_co3">
			<label class="activitylist_team_co3_text">渠道选择：</label>
				 <select id="modifyAgentInfo" name="agentId" >
                     <option value="">全部</option>
                     <c:forEach var="agentinfo" items="${agentinfoList }">
                     <option value="${agentinfo.id }" <c:if test="${param.agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                     </c:forEach>
                 </select>
			</div>
				<div class="activitylist_bodyer_right_team_co3">
                  <label class="activitylist_team_co3_text">航空公司：</label>
				 <div class="selectStyle">
                   <select id="airlines" name="airlines">
                        <option value="" selected="selected">不限</option>
                        <c:forEach var="dic" items="${traffic_namelist}">
                            <option value="${dic.airlineCode}" <c:if test="${param.airlines==dic.airlineCode}">selected="selected"</c:if>>${dic.airlineName}</option>
                        </c:forEach>
                    </select>
				 </div>
             </div>
				<div class="activitylist_bodyer_right_team_co1 ">
				<label class="activitylist_team_co3_text">价格范围：</label>
				<input id="settlementAdultPriceStart" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
				<span style="font-size:12px;font-family:'宋体';"> 至</span> 
				<input id="settlementAdultPriceEnd" maxlength="7" class="rmb inputTxt" name="settlementAdultPriceEnd" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
         	</div>
         	<%-- <div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
				<select name="paymentType" id="paymentType" >
					<option value="">不限</option>
					<c:forEach items="${fns:findAllPaymentType()}" var="pType">
						<!-- 用户类型  1 代表销售 -->
						<option value="${pType[0]}" <c:if test="${pType[0] eq paymentType}">selected="selected"</c:if>>${pType[1] }</option>
					</c:forEach> 
				</select>
			</div> --%>
				<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">切位日期：</label>
				<input id="startingDate" class="inputTxt dateinput" name="startingDate" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('startingDate').value==''){$dp.$('startingDate').value=vvv;}}})" style="margin-left: -3px;" value='<fmt:formatDate value="${activityAirTicket.startingDate}" pattern="yyyy-MM-dd"/>'  readonly="" />
				<span> 至 </span>
				<input  id="returnDate" name="returnDate" class="inputTxt dateinput" onclick="WdatePicker()" value='<fmt:formatDate value="${activityAirTicket.returnDate}" pattern="yyyy-MM-dd"/>' readonly="">

			</div>
				<div class="activitylist_bodyer_right_team_co1">
         	    <label class="activitylist_team_co3_text">机票类型：</label>
				<div class="selectStyle">
				     <select name="airType">
                        <option value="">不限</option>
                        <option value="3" ${activityAirTicket.airType=='3'?"selected":''}>单程</option>
                        <option value="1" ${activityAirTicket.airType=='1'?"selected":''}>多段</option>
                        <option value="2" ${activityAirTicket.airType=='2'?"selected":''}>往返</option>
                    </select>
				</div>
         	</div>
			</div>
			<div class="kong"></div>
		</div>
	</form:form>
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
					 <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
					 <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">更新时间</a></li>

					</ul>
				</div>
				<div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
				<div class="kong"></div>
			</div>
		</div>
	<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table activitylist_bodyer_table mainTable" >
		<thead >		
              <tr>
                               <th width="4%">序号</th>
									<th width="8%">订单编号</th>
								<th width="8%">渠道</th>
								<th width="9%">切位时间</th>
								<th width="4%">机票类型</th>
								<th width="4%">舱位</th>                         
							    <th width="8%">出发机场</th>
								<th width="8%">到达机场</th>
								<th width="12%">起飞时间</th>
								<th width="12%">到达时间</th>		
								<th width="5%">余位数</th>
								<th width="5%">切位数</th>
								<th width="7%">切位订金</th>
								<th width="7%">已收金额<br />到账金额</th>
								<th width="4%">操作</th>
            </tr>
		</thead>
		<tbody>		
				<c:if test="${fn:length(page.list) <= 0 }">
	                 <tr class="toptr" >
	                 	<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
	                 </tr>
        		</c:if>
			<c:forEach items="${page.list}" var="activity" varStatus="s" >
			 	<tr id="parent${s.count}">
					<td class="tc">${s.count} </td>
					<td class="tc">${activity.orderNum} </td>
					
					<td class="tc">${activity.agentName }</td>
					 <td class="tc"><fmt:formatDate value="${activity.createDate}" pattern="yyyy-MM-dd"/></td>
                   <td class="tc"><c:choose>
	                    <c:when test="${activity.activityAirTicket.airType== 3}">
	                        单程
	                    </c:when>
	                    <c:when test="${activity.activityAirTicket.airType == 2}">
	                        往返
	                    </c:when>
	                    <c:otherwise>
	                        多段
	                    </c:otherwise>
	                    </c:choose>   
                    </td>
              		<td class="tc">${activity.activityAirTicket.flightInfos[0].airspaceLabel()  }</td>
            
              
                     <td class="tc">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activity.activityAirTicket.flightInfos[0].leaveAirport}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                      </td>
					<td class="tc">
                             <c:forEach items="${airportlist}" var="airportlist">
                             <c:if test="${airportlist.id eq activity.activityAirTicket.flightInfos[activity.activityAirTicket.flightInfos.size()-1].destinationAirpost}">
                             ${airportlist.airportName}
                             </c:if>
                             </c:forEach>
                         </td>

                    <td class="tc p0">
                                    ${activity.activityAirTicket.flightInfos[0].startTime } 
                     </td>
                    <td class="tc">${activity.activityAirTicket.flightInfos[activity.activityAirTicket.flightInfos.size()-1].arrivalTime  }</td>
              
                 	
                    <td class="tc">${activity.activityAirTicket.freePosition }</td>
                    <td class="tc">${activity.payReservePosition}</td>
                     <td class="tc">${activity.orderMoney}</td>
                    <td class="p0 tr">	
									<div class="yfje_dd"><span class="fbold">${activity.orderMoney}</span></div>
									<div class="dzje_dd"><span class="fbold"><c:if test="${activity.confirm == 1 }">${activity.orderMoney}</c:if></span></div>
								</td>
                    <td class="p00">
                            <dl class="handle">
                                <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
                                <dd class="">
                                    <p>
                                        <span></span>                                       

 <a href="${ctx}/stock/manager/airticket/airTicketReserveOrderInfo?orderNum=${activity.orderNum}&id=${activity.activityAirTicket.id} " onclick=""   target="_blank">查看详情</a>
     
 <shiro:hasPermission name="stock:manager:edit"><a href="javascript:void(0)" onclick="viewRefund('${activity.aid }');">切位退款</a></shiro:hasPermission>                                       
                                    </p>
                                </dd>
                            </dl>
                        </td>
             </tr>
						
			</c:forEach>
		</tbody>
	</table>
	</form>
	<div class="page">
		<div class="pagination">
			<div class="endPage">${page}</div>
			<div style="clear:both;"></div>
		</div>
	</div>
</div>
</body>
</html>