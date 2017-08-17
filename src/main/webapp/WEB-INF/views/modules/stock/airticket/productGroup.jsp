<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>产品团期列表</title>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <link href="${ctxStatic}/css/bootstrap.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css"/>
    <link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
    <style>
        .p00 .handle a{
            width: 50px;
            margin-left: auto;
            margin-right: auto
        }
    </style>
    <script type="text/javascript">

        $(function(){
        	
        	 var _$orderBy = $("#orderBy").val();
	         var orderBy = _$orderBy.split(" ");
	         $(".activitylist_paixu_left li").each(function(){
	             if ($(this).hasClass("li"+orderBy[0])){
	                 orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
	                 $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
	                 $(this).attr("class","activitylist_paixu_moren");
	             }
	         });
        	
            //初始时已选择团期置灰
            setGroupState();
        });

        function stockDetail(airticketId){
            window.parent.open("${ctx}/stock/manager/airticket/detail/"+airticketId+"?showReserve=0");
        }

        //排序
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
            $("#groupform").submit();
 	    };

        function CurentTime(){
            return new Date();
        }
        //点击查询时显示列表
        $(document).on('click','[name="search"]',function(){
            if($('.activitylist_bodyer_right_team_co_paixu').css('display')=="none"){
                $('.activitylist_bodyer_right_team_co_paixu').show();
                $('form').show();
            }
        })

        function setGroupState(){
        	
            //若列表中数据已被添加,则勾选并置灰
            var selectedProductArr = '${selectedProducts}';
            if(!selectedProductArr){
                return ;
            }
            var selectedProducts = selectedProductArr.split(',');
            $('[name="groupRow"]').each(function(){
                for(var i= 0,len=selectedProducts.length;i<len;i++){
                    var $tds = $(this).children('td');
                    if(selectedProducts[i]==$tds.eq(8).find("input[name=id]").val()){
                        $tds.eq(0).children('input').attr('checked','true').attr('disabled','disabled');
                    }
                }
            });
        }
        $(document).on('change','[name="checkAll"]',function(){
            if($(this).attr('checked')){
                $(this).parents('#contentTable').find('input[type="checkbox"]:enabled').attr('checked',true);
            }else{
                $(this).parents('#contentTable').find('input[type="checkbox"]:enabled').attr('checked',false);
            }
        });

        $(document).on('click','[name="showDetail"]', function () {
            var $tr= $(this).parents('tr:first');
            var $toggleTr= $tr.next();
            $toggleTr.toggle();
            if($toggleTr.is(':visible')){
                $(this).text( $(this).text().replace('展开','收起'));
            }else{
                $(this).text( $(this).text().replace('收起','展开'));
            }
        });
        
        function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_op,payMode_cw,payMode_data,payMode_guarantee,payMode_express) {
            if($(child).is(":hidden")){
                var selectdata = $("#agentIdSel").length ? $("#agentIdSel option:selected") : $("input[name='agentId']");
                var agentId = selectdata.val();
                if(agentId!=null&&agentId!="") {
                	
                }else{
                    $(obj).html("收起");
                    $(obj).parents('tr').addClass('tr-hover');
                    $(child).show();
                    $(obj).parents("td").addClass("td-extend");
                }
            }else{
                if(!$(child).is(":hidden")){
                    $(obj).parents('tr').removeClass('tr-hover');
                    $(child).hide();
                    $(obj).parents("td").removeClass("td-extend");
                    $(obj).html("展开");
                }
            }
        }
        
        function formReset() {
        	$(':input','#groupform')
        		.not(':button, :submit, :reset, :hidden')
        		.val('')
        		.removeAttr('checked')
        		.removeAttr('selected');
        }
    </script>
</head>
<body>
	<form novalidate="novalidate" id="groupform" name="groupform" action="${ctx}/stock/manager/airticket/getActivityAirTicketList" method="post">
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input type="hidden" name="selectedProducts" value="${selectedProducts }" />
		<input type="hidden" name="source" value="${source }" />
		<input type="hidden" name="agentId" value="${agentId }" />
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>出发城市：</label> 
				<input name="departureCity" class="inputTxt" value="${activityAirTicket.departureCity }" style="width:130px;" type="text" />
			</div>
			<div class="activitylist_bodyer_right_team_co2 pr wpr20">
				<label>到达城市：</label>
				<input name="arrivedCity" class="inputTxt" value="${activityAirTicket.arrivedCity }" style="width:130px;" type="text" />
			</div>
			<div class="form_submit">
				<input value="搜索" class="btn btn-primary" type="submit" name="search">&nbsp;
				<input class="btn btn-primary" value="重置查询" onclick="formReset();" type="button">
			</div>
			<div class="ydxbd" style="display: block;background-color: #ffffff">
				<div class="activitylist_bodyer_right_team_co2">
					<div class="activitylist_team_co3_text">团号：</div>
					<input value="${activityAirTicket.groupCode }" name="groupCode" class="inputTxt" type="text" />
				</div>
			<div class="activitylist_bodyer_right_team_co2">
	            <label>余位数：</label>
	            <input  maxlength="7" class="inputTxt" name="freePositionStart" value="${freePositionStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
	            <span style="font-size:12px;font-family:'宋体';"> 至</span>
	            <input maxlength="7" class="inputTxt" name="freePositionEnd" value="${freePositionEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
       	 </div>	
				<%-- <div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">切位渠道：</div>
					<select name="agentId">
						<option value="">不限</option>
						<c:forEach items="${agentinfoList}" var="agentInfo">
                            <option value="${agentInfo.id}" ${agentInfo.id==agentId?"selected":''}>${agentInfo.agentName}</option>
                        </c:forEach>
					</select>
				</div>  --%>
				<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">机票类型：</div>
					<select name="airType">
						<option value="">不限</option>
						<option value="3" ${activityAirTicket.airType=='3'?"selected":''}>单程</option>
                        <option value="1" ${activityAirTicket.airType=='1'?"selected":''}>多段</option>
                        <option value="2" ${activityAirTicket.airType=='2'?"selected":''}>往返</option>
					</select>
				</div>
				<div class="kong" style="margin:5px 0px"></div>
				<div class="activitylist_bodyer_right_team_co2">
					<label>价格范围：</label>
					<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="${settlementAdultPriceStart }" name="settlementAdultPriceStart" class="inputTxt" maxlength="7" id="settlementAdultPriceStart"> 
					<span> 至 </span>
					<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" value="${settlementAdultPriceEnd }" name="settlementAdultPriceEnd" class="inputTxt" maxlength="7" id="settlementAdultPriceEnd">
				</div>
				<div class="activitylist_bodyer_right_team_co3">
					<div class="activitylist_team_co3_text">航空公司：</div>
					<select name="airlines">
						<option value="">不限</option>
						<c:forEach items="${traffic_namelist}" var="trafficName">
                            <option value="${trafficName.value}" ${trafficName.value==activityAirTicket.airlines?"selected":''}>${trafficName.label} ${trafficName.description}</option>
                        </c:forEach>
					</select>
				</div>
				<div class="activitylist_bodyer_right_team_co2">
					<label>时间区间：</label> 
					<input readonly="" onfocus="WdatePicker({minDate:CurentTime(),onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" style="margin-left: -3px;" value="${groupOpenDate }" name="groupOpenDate" class="inputTxt dateinput" id="groupOpenDate"> 
					<span> 至 </span> 
					<input readonly="" onclick="WdatePicker({minDate:CurentTime()})" value="${groupCloseDate }" name="groupCloseDate" class="inputTxt dateinput" id="groupCloseDate">
				</div>
			</div>
			<div class="kong"></div>
		</div>
		<div class="activitylist_bodyer_right_team_co_paixu" >
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li style="width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
						<li class="activitylist_paixu_left_biankuang licreateDate"><a onclick="sortby('createDate',this)">创建时间</a></li>
						<li class="activitylist_paixu_left_biankuang liupdateDate"><a onclick="sortby('updateDate',this)">更新时间</a></li>
						<li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onclick="sortby('settlementAdultPrice',this)">同行价格</a></li>
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp; <strong>${pageCount}</strong> &nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>
	</form>
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="10%">
					<input name="checkAll" value="" type="checkbox" style="width: auto;">序号
				</th>
				<th width="10%">团号</th>
				<th width="10%">机票类型</th>
				<th width="10%">产品编号</th>
				<th width="10%">应收价格</th>
				<th width="10%">机位余位</th>
				<th width="10%">切位数</th>
				<th width="10%">已分配数</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list }" var="airticket" varStatus="status">
				<tr id="parent1" name="groupRow">
					<td class="tc"><input type="checkbox">${status.count }
					</td>
					<td class="tc">${airticket.groupCode }</td>
					<td class="tc">
						<c:choose>
                    		<c:when test="${airticket.airType == 3}">单程</c:when>
				            <c:when test="${airticket.airType == 2}">往返</c:when>
				            <c:otherwise>多段</c:otherwise>
                		</c:choose>
					</td>
					<td>${airticket.productCode}</td>
					<td>
						<c:forEach items="${curlist}" var="curlist">
		                    <c:if test="${curlist.id==airticket.currency_id}">
		                        ${curlist.currencyMark}
		                    </c:if>
		                </c:forEach>
	                	${airticket.settlementAdultPrice}
	                </td>
					<td>${airticket.freePosition}</td>
					<td>${airticket.payReservePosition}</td>
					<td>${airticket.nopayReservePosition + airticket.payReservePosition}</td>
					<td class="p00">
						<dl class="handle">
							<a name="showDetail" href="javascript:void(0)" onclick="expand('#child${s.count}',this,1203)">展开</a>
							<a name="stockDetail" onclick="stockDetail(${airticket.id })">库存详情</a>
						</dl>
						<input type="hidden" value="${airticket.id }" name="id" />
		                <input type="hidden" value='<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${airticket.currency_id }" />${airticket.settlementAdultPrice }' name="settlementAdultPrice" />
		                <input type="hidden" value='<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${airticket.currency_id }" />${airticket.settlementcChildPrice }' name="settlementcChildPrice" />
		                <input type="hidden" value='<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${airticket.currency_id }" />${airticket.settlementSpecialPrice }' name="settlementSpecialPrice" />
		                <input type="hidden" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.startingDate}"/>' name="startingDate"/>
		                <input type="hidden" value="${airticket.reservationsNum}" name="reservationsNum" />
		                <input type="hidden" value="${airticket.soldPayPosition }" name="soldPayPosition">
					</td>
				</tr>
				
				<tr style="display:none" id="child${status.count}" class="activity_team_top1">
					<td colspan="9">
						<table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable">
							<thead>
								<tr>
									<th width="5%">序号</th>
									<th width="10%">航空公司</th>
									<th width="7%">舱位</th>
									<th width="11%">出发机场</th>
									<th width="11%">到达机场</th>
									<th width="12%">起飞时间</th>
									<th width="12%">到达时间</th>
									<th width="9%">成人同行价</th>
									<th width="9%">儿童同行价</th>
									<th width="10%">特殊人群同行价</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${airticket.flightInfos}" var="group" varStatus="s2">
									<tr>
										<td class="tc">${group.number}</td>
										<td>${group.airlinesLabel()}</td>
										<td>${group.airspaceLabel()}</td>
										<td>
											<c:forEach items="${airportlist}" var="airportlist">
				                            	<c:if test="${airportlist.id eq group.leaveAirport}">
				                            		${airportlist.airportName}
				                            	</c:if>
				                            </c:forEach>
										</td>
										<td>
											<c:forEach items="${airportlist}" var="airportlist">
					                            <c:if test="${airportlist.id eq group.destinationAirpost}">
					                           		${airportlist.airportName}
					                            </c:if>
				                            </c:forEach>
										</td>
										<td><fmt:formatDate value="${group.startTime}" pattern="yyyy-MM-dd  HH:mm"/></td>
										<td><fmt:formatDate value="${group.arrivalTime}" pattern="yyyy-MM-dd  HH:mm"/></td>
										<td class="tc">
											<c:if test="${airticket.settlementAdultPrice=='0.00'}">
							                  0
							                </c:if> 
							                <c:if test="${airticket.settlementAdultPrice!='0.00'}">
								                <c:forEach items="${curlist}" var="curlist">
								                    <c:if test="${curlist.id==airticket.currency_id}">
								                        ${curlist.currencyMark}
								                    </c:if>
								                </c:forEach>
							                	<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.settlementAdultPrice}" />
							                </c:if>
										</td>
										<td class="tc">
											<c:if test="${airticket.settlementcChildPrice=='0.00'}">
							                  0
							                </c:if> 
							                <c:if test="${airticket.settlementcChildPrice!='0.00'}">
								                <c:forEach items="${curlist}" var="curlist">
							                    <c:if test="${curlist.id==airticket.currency_id}">
							                        ${curlist.currencyMark}
							                    </c:if>
								                </c:forEach>
							                	<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.settlementcChildPrice}" />
							                </c:if> 
										</td>
										<td class="tc">
											<c:if test="${airticket.settlementSpecialPrice=='0.00'}">
							                  0
							                </c:if> 
							                <c:if test="${airticket.settlementSpecialPrice!='0.00'}">
								                <c:forEach items="${curlist}" var="curlist">
								                    <c:if test="${curlist.id==airticket.currency_id}">
								                        ${curlist.currencyMark}
								                    </c:if>
								                </c:forEach>
							                <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${airticket.settlementSpecialPrice}" />
							                </c:if> 
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>