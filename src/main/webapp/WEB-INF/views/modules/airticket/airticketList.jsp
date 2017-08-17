<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>产品-机票产品及发布</title>
    <style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/forTTS/airticket/airticketList.js"></script>

<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.spinner.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script type="text/javascript">
var root = "${ctx}";
var productStatus = "${airTicket.productStatus}";
$(function(){
	
	g_context_url = "${ctx}";
	
	//搜索条件筛选
	launch();
	//产品名称文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	
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
	  
		    
			
});

$(document).ready(function() {
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
});

 var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#airlines').val('');
    $('#targetAreaId').val('');
    $('#minprice').val('');
    $('#maxprice').val('');
    $('#querystartTime').val('');
    $('#quertEndTime').val('');
    $('#airType').val('');
    $('#productNum').val('');
    $('#orderShowType').val('${showType}');
    $('#sousuo').show();
}
function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();

    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
        }else{
            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
        }
    }else{
        sortBy = sortBy+" DESC";
    }
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#searchForm").submit();
}


//c452,c453  大洋87
function openGroupLibPage(){
	//debugger;
	//groupcodelibtype = 0 为机票
    $.jBox("iframe:"+g_context_url+"/activity/groupcodelibrary/toGroupcodeLibraryBox?groupcodelibtype=7",{  //groupcodelibtype = 7 为机票
        title:"团号库",buttons:{'关闭':1},height:680,width:680,persistent:true
    }).find("#jbox-content").css("overflow","hidden"); 
}


</script>
</head>
<body>


<!-- 签证公告展示 -->
<%@ include file="/WEB-INF/views/modules/visa/visaPublicBulletinMarquee.jsp"%>
	<page:applyDecorator name="activity_visa_head">
	    <page:param name="current">${airTicket.productStatus}</page:param>
	</page:applyDecorator>
    <!--右侧内容部分开始-->
    <div class="activitylist_bodyer_right_team_co_bgcolor">
        <form:form id="searchForm" modelAttribute="airTicket" action="${ctx}/airTicket/list/${airTicket.productStatus}" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="airTicketIds" name="airTicketIds" value="${airTicketIds}" type="hidden">
        <input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
        <div class="activitylist_bodyer_right_team_co">

            <div class="activitylist_bodyer_right_team_co2 pr ">
	         	<input class="txtPro inputTxt searchInput" id="wholeSalerKey"
                       placeholder="输入团号，支持模糊匹配" name="wholeSalerKey" value="${airTicket.groupCode }"/>
	         	<%--<span id="sousuo" class="ipt-tips" style="display: block;">输入团号，支持模糊匹配</span>--%>
         	</div>
            <a class="zksx">筛选</a>
            <div class="form_submit">
                <input class="btn btn-primary ydbz_x" value="搜索" type="submit">
                <input class="btn ydbz_x " type="button" onclick="resetSearchParams()" value="清空所有条件"/>
                
                <!-- 大洋87  对应需求号  c451,c453   -->
                <c:if test="${fns:getUser().company.isNeedGroupCode eq 1}">
                   <input class="btn ydbz_x " type="button" onclick="openGroupLibPage()" value="团号库"/>
                </c:if>
            </div>
            <%--bug17548--%>
            <p class="main-right-topbutt"><a class="primary" href="${ctx}/airTicket/form.htm">发布新产品</a></p>
            <div class="ydxbd">
                <span></span>
                <div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">航空公司：</div>
                    <div class="selectStyle">
                    <select id="airlines" name="airlines">
                        <option value="" >不限</option>
                        <c:forEach var="airline" items="${airlines_list}">
									<option value="${airline.airlineCode}"  <c:if test="${fn:trim(airlines) eq fn:trim(airline.airlineCode)}">selected</c:if>>${airline.airlineName}</option>
						</c:forEach>
                        
                    </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
               	 	<div class="activitylist_team_co3_text">到达城市：</div>
                	<input id="queryarrivedCity" name="arrivedCity" class="inputTxt" value="${arrivedCity}" type="text" />
            	</div>
                <div class="activitylist_bodyer_right_team_co1">
                	<div class="activitylist_team_co3_text">出发城市：</div>
                	<input id="querydepartureCity" name="departureCity" class="inputTxt" value="${departureCity}" type="text" />
            	</div>
				<div class="activitylist_bodyer_right_team_co1">
                    <div class="activitylist_team_co3_text">机票类型：</div>
                    <div class="selectStyle">
                    <form:select path="airType" itemValue="key" itemLabel="value">
                        <form:option value="">不限</form:option>
                        <form:options items="${airTypes}" />
                    </form:select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">价格区间：</label>
                    <input id="minprice" class="rmb inputTxt" name="minprice" value="${minprice}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" type="text" />
                    <span> 至 </span>
                    <input id="maxprice" class="rmb inputTxt" name="maxprice" value="${maxprice}" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" type="text" />
                </div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">产品编号：</div>
				    <input name="productCode"  id="productNum" type="text"  value="${airTicket.productCode}" />
				</div>
                <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text">时间区间：</label><input onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('quertEndTime').value==''){$dp.$('quertEndTime').value=vvv;}}})" value="${param.querystartTime}" name="querystartTime" class="inputTxt dateinput" id="querystartTime" readonly >
                    <span> 至 </span>
                    <input onclick="WdatePicker()" value="${param.quertEndTime}" name="quertEndTime" class="inputTxt dateinput" id="quertEndTime" readonly >
                </div>
            </div>
        </div>


    </form:form>

  
        <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
             <ul>
            <li class="activitylist_paixu_left_biankuang liid"><a onClick="sortby('id',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <li class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a onClick="sortby('settlementAdultPrice',this)">同行价格</a></li>
            </ul>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

    
    <div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
			<%--<a <c:if test="${empty departmentId}">class="select"</c:if> href="javascript:void(0)" onclick="getDepartment('');">全部产品</a>--%>
			<c:forEach var="department" items="${showAreaList}" varStatus="status">
				<c:choose>
					<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
						<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</div>
    <table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
     
            <tr>
            <th width="4%">序号</th>
            <th width="4%" class="table_borderLeftN">全选<input type="checkbox" onclick="checkall(1)" id="allChk1" name="allChk"></th>
            <th width="9%">产品编号</th>
            <c:set var="companyId" value="${fns:getUser().company.id }"></c:set>
            <c:set var="companyUUID" value="${fns:getUser().company.uuid }"></c:set>
            <c:if test="${companyId eq 68 or companyId eq 71 or companyUUID eq '7a81b21a77a811e5bc1e000c29cf2586'}">
            	<th width="9%">产品团号</th>
            </c:if>
            <th  width="5%">计调</th>
            <th width="7%">机票类型</th>
            <th width="6%">出发城市</th>
            <th width="6%">到达城市</th>
            <th width="10%">成人价/税费</th>
            <th width="8%">机位余位</th>
            <!-- 0258需求,发票税:针对懿洋假期-tgy-s -->
            <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
            <th width="8%">发票税</th>
            </c:if>
            <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->
            <th width="9%">订金时限<br />取消时限</th>
            <th width="10%">下载资料</th>
            <th width="9%">出票日期</th>
			<th width="4%">操作</th>
           </tr>
       </thead>
       
        <tbody>
     		 <c:if test="${fn:length(page.list) <= 0 }">
				                 <tr class="toptr" >
				                 	<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
				                 </tr>
			</c:if>
     	   <c:forEach items="${page.list}" var="airticket" varStatus="s">
        
            <tr id="parent${s.count}">
            	<td>${s.count}</td>
                <td class="table_borderLeftN"><input type="checkbox" name="airticketId" value="${airticket.id}" <c:if test="${fn:contains(airTicketIds,fn:trim(airticket.id))}">checked="checked"</c:if> onclick="idcheckchg(this)" value="1152" ></td>
                <td>${airticket.productCode}</td>
	            <c:if test="${companyId eq 68 or companyId eq 71 or companyUUID eq '7a81b21a77a811e5bc1e000c29cf2586'}">
		            <td>${airticket.groupCode}</td>
	            </c:if>
                 <td>
                		${airticket.createBy.name }
                </td>
                <td>  <c:choose>
                    <c:when test="${airticket.airType == 3}">
                        	单程
                    </c:when>
                    <c:when test="${airticket.airType == 2}">
                        	往返
                    </c:when>
                    <c:otherwise>
                        	多段
                    </c:otherwise>
                </c:choose></td>
                
                 <td>${fns:getDictLabel(airticket.departureCity, 'from_area', '')}</td>
                <td>
                    <c:forEach items="${arrivedareas}" var="area">
                        <c:if test="${area.id eq airticket.arrivedCity}">
                            ${area.name}
                        </c:if>
                    </c:forEach>
                </td>
            
                <td class="tr">
                <c:forEach items="${curlist}" var="curlist">
                    <c:if test="${curlist.id==airticket.currency_id}">
                        ${curlist.currencyMark}
                    </c:if>
                </c:forEach>
               
                <span class="tdred fbold">${airticket.settlementAdultPrice}</span><br /><span class="tdorange fbold">
                <c:forEach items="${curlist}" var="curlist">
                    <c:if test="${curlist.id==airticket.currency_id}">
                        ${curlist.currencyMark}
                    </c:if>
                </c:forEach>
                ${airticket.taxamt}</span>
                </td>  
                <td class="tr">${airticket.freePosition}</td>
                <!-- 0258需求,发票税:针对懿洋假期-tgy-s -->
                <c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' }"> 
                <!-- 处理发票税不为空的情况 -->
                <c:if test="${!empty airticket.invoiceTax}">
                <td class="tr">${airticket.invoiceTax }&nbsp;%</td>
                </c:if>
                <!-- 处理发票税为空的情况 -->
                <c:if test="${empty airticket.invoiceTax}">
                <td class="tr">0.00&nbsp;%</td>
                </c:if>
                </c:if>
                 <!-- 0258需求,发票税:针对懿洋假期-tgy-e -->
                <td class="p0">
                    <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.depositTime}"/></div>
                    <div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.cancelTimeLimit}"/></div>
                </td>
                <td>
                <c:set var="docids" value="0"/>
                <c:forEach items="${airticket.airTicketFiles}" var="afile">
                	<c:set var="docids" value='${docids},${afile.docId}'/>
                </c:forEach>
                <a href="javascript:void(0)" onclick="downloads('${docids}','机票产品资料','true')">下载资料模板</a></td>
                <!--  
                <td class="tc"><a id="close${s.count}"  onclick="expand('#child${s.count}',this,${airticket.id})" class="team_a_click" href="javascript:void(0)" onmouseenter="if($(this).html()=='展开'){$(this).html('展开航段')}" onmouseleave="if($(this).html()=='展开航段'){$(this).html('展开')}">展开</a> <a onclick="productModify(${airticket.id})" href="javascript:void(0)" >修改</a>
                    <a onclick="return confirmxDel('要删除该产品吗？', 1152)" href="javascript:void(0)">删除</a>
                    <a onclick="javascript:window.open('${ctx}/airTicket/actityAirTickettail/${airticket.id}')" href="javascript:void(0)">详情</a>
                </td>
                -->
                <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${airticket.outTicketTime}"/></br>
                <a onclick="expand('#child${s.count}',this,${airticket.id})" href="javascript:void(0)" id="close${s.count}" onmouseenter="if($(this).html()=='展开'){$(this).html('展开航段')}" onmouseleave="if($(this).html()=='展开航段'){$(this).html('展开')}">展开航程</a>
                </td>
                  <td class="p0 ">
                                <dl class="handle">
                                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                                    <dd>
                                    <p>
                                        <span></span>
                                        <a onclick="productModify(${airticket.id})" href="javascript:void(0)">修改</a>
                                        <a onclick="return confirmxDel('要删除该产品吗？', ${airticket.id})" href="javascript:void(0)">删除</a>
                                        <a onclick="javascript:window.open('${ctx}/airTicket/actityAirTickettail/${airticket.id}')" href="javascript:void(0)">详情</a>
                                    	<c:if test="${airTicket.productStatus eq 2}">
				                        	<a href="${ctx}/airTicket/batchOffAirtickets/2/${airticket.id}" onClick="return confirmx('需要将该产品下架吗', this.href)">下架</a>
				                    	</c:if>
				                        <c:if test="${airTicket.productStatus eq 3}">
				                            <a href="${ctx}/airTicket/batchOffAirtickets/3/${airticket.id}" onClick="return confirmx('需要将该产品上架吗', this.href)">上架</a>
				                        </c:if>
                                    </p>
                                    </dd>
                                </dl>
                            </td>       
            </tr>


            <tr class="activity_team_top1" id="child${s.count}" style="display:none">
                <td class="team_top" colspan="15">
                    <table style="margin:0 auto;" class="table activitylist_bodyer_table" id="teamTable">
                        <thead>
                        <tr>
                            <th width="5%">序号</th>
                            <th width="6%">航空公司</th>
                            <th width="5%">舱位</th>
                            <th width="10%">出发机场</th>
                            <th width="10%">到达机场</th>
                            <th width="10%">起飞时间</th>
                            <th width="10%">到达时间</th>
                            <th width="10%">成人同行价</th>
                            <th width="10%">儿童同行价</th>
                            <th width="10%">特殊人群同行价</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${airticket.flightInfos}" var="flightInfo" varStatus="s2">
                        <tr>
                            <td >${s2.count}</td>
                            <td >
                               ${fns:getAirlineNameByAirlineCode(flightInfo.airlines)}
                            </td>
                            <td >
                                <c:choose>
                                    <c:when test="${flightInfo.spaceGrade=='-1' || flightInfo.spaceGrade=='' }">不限</c:when>
                                    <c:otherwise>
                                        <c:forEach items="${spaceGradelist}" var="spaceGrade">
                                            <c:if test="${spaceGrade.value eq flightInfo.spaceGrade}">${spaceGrade.label}</c:if>
                                        </c:forEach>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td ><c:forEach items="${airports}" var="from_area">
                                      	         <c:if test="${from_area.id==flightInfo.leaveAirport}">
                                                 ${from_area.airportName} 
                                                 </c:if>
                                                 </c:forEach></td>
                            <td ><c:forEach items="${airports}" var="area">
                                      	         <c:if test="${area.id==flightInfo.destinationAirpost}">
                                                 ${area.airportName} 
                                                 </c:if>
                                                 </c:forEach></td>
                            <td ><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfo.startTime}"/>  </td>
                            <td ><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${flightInfo.arrivalTime}"/>  </td>
                            <c:choose>
                                <c:when test="${airticket.airType == 2}">
                                    <td >-</td>
                                    <td >-</td>
                                    <td >-</td>
                                </c:when>
                                <c:when test="${airticket.airType == 3 }">
                                  <td >
                                        ${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="tdred fbold">${airticket.settlementAdultPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="tdred fbold">${airticket.settlementcChildPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(airticket.currency_id, 0, "mark")}<span class="tdred fbold">${airticket.settlementSpecialPrice}</span>
                                    </td>
                                </c:when>
                                <c:otherwise>
                                    <td >
                                        ${fns:getCurrencyInfo(flightInfo.currency_id, 0, "mark")}<span class="tdred fbold">${flightInfo.settlementAdultPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(flightInfo.currency_id, 0, "mark")}<span class="tdred fbold">${flightInfo.settlementcChildPrice}</span>
                                    </td>
                                    <td >
                                        ${fns:getCurrencyInfo(flightInfo.currency_id, 0, "mark")}<span class="tdred fbold">${flightInfo.settlementSpecialPrice}</span>
                                    </td>
                                </c:otherwise>
                            </c:choose>
                        </tr>
                         </c:forEach>
                        </tbody>
                    </table>

                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
    
    <div class="page">
        <div class="pagination">
            <dl>
                <dt><input id="allChk2" name="allChk" onclick="checkall(2)" type="checkbox">全选</dt>
                <dd>
                	<c:if test="${airTicket.productStatus eq 2}">
	                    <%--<a onclick="confirmBatchIsNull('需要将选择的产品下架吗','off')">批量下架</a>--%>
                        <input type="button" class="btn ydbz_x" onclick="confirmBatchIsNull('需要将选择的产品下架吗','off')" value="批量下架">
                	</c:if>
                	<c:if test="${airTicket.productStatus eq 3}">
                		<%--<a onclick="confirmBatchIsNull('需要将选择的产品上架吗','on')">批量上架</a>--%>
                        <input type="button" class="btn ydbz_x" onclick="confirmBatchIsNull('需要将选择的产品上架吗','on')" value="批量上架">
                	</c:if>
                    <%--<a onclick="confirmBatchIsNull('删除所有选择的团期吗','del')">批量删除</a>--%>
                    <input type="button" class="btn ydbz_x" onclick="confirmBatchIsNull('删除所有选择的产品吗','del')" value="批量删除">
                </dd>
            </dl>
            <div class="endPage">${page}
            </div>
            <div style="clear:both;"></div>
        </div>
    </div>



    <!--右侧内容部分结束-->
</body>
</html>