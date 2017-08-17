<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title><c:if test="${param.option eq 'detail' or empty param.option}">交易明细</c:if><c:if test="${param.option eq 'account'}">账龄查询</c:if></title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i=0;i<searchFormInput.length;i++) {
		var inputValue = $(searchFormInput[i]).val();
		if(inputValue != "" && inputValue != null) {
			inputRequest = true;
			break;
		}
	}
	for(var i=0;i<searchFormselect.length;i++) {
		var selectValue = $(searchFormselect[i]).children("option:selected").val();
		if(selectValue != "" && selectValue != null && selectValue != 0) {
			selectRequest = true;
			break;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
}

$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	
	// renderSelects(selectQuery());
	//签证国家
	$("#sysCountryIdForFinace").comboboxInquiry();
	//计调模糊匹配
	$("[name=jd]").comboboxSingle();
	//销售模糊匹配
	$("[name=saler]").comboboxSingle();
	//产品销售和下单人切换
    switchSalerAndPicker();
    //下单恩模糊匹配
    $("[name=orderPersonId]").comboboxSingle();
    
    //渠道改为可输入的select
    $("[name=agentId]").comboboxSingle();
	
    $(document).delegate(".downloadzfpz","click",function(){
        window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
    });
        
	$("#contentTable").delegate("ul.caption > li","click",function(){
		var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
		$(this).addClass("on").siblings().removeClass('on');
		$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
	});
	
	    
	    $("#contentTable").delegate(".tuanhao","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.chanpin_cen').removeClass('onshow');
	        $('.tuanhao_cen').addClass('onshow');
	    });
	    
	    	
	    $("#contentTable").delegate(".chanpin","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.tuanhao_cen').removeClass('onshow');
	         $('.chanpin_cen').addClass('onshow');
	        
	    });

	    $("#contentTable").delegate(".nopay","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.total_cen').removeClass('onshow');
	        $('.nopay_cen').addClass('onshow');
	    });
	    
	    	
	    $("#contentTable").delegate(".total","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.nopay_cen').removeClass('onshow');
	         $('.total_cen').addClass('onshow');
	        
	    });
        var _$orderBy = $.trim($("#orderBy").val() || "createDate DESC");
		$("#orderBy").val(_$orderBy);
        var orderBy = _$orderBy.split(" ");
        $(".activitylist_paixu_left li").each(function(){
            if ($(this).hasClass("li"+orderBy[0])){
                orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                $(this).attr("class","activitylist_paixu_moren");
            }
        });

      //“团队类型”当类型选择为“签证”时，弹出一个条件，“签证国家”
        $('#orderS').bind('change', function(){
        	  var val = $(this).val(); 
        	  if(val == 6)
        		  $('#sysCountry').show();
        	  else
        		  $('#sysCountry').hide();
        });
      if($('#orderS').val() == 6)
    	  $('#sysCountry').show();
      else
    	  $('#sysCountry').hide();


$(document).scrollLeft(0);
$("#targetAreaId").val("${travelActivity.targetAreaIds}");
$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
});
$().ready(function(){
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

function changeGroup(orderId){
    window.open("${ctx}/orderCommon/manage/changGroup/"+orderId,"_blank");
}

function orderDetail(orderId,orderType){
	var url = "";
	if(orderType == "7") {
		url = "${ctx}/order/manage/airticketOrderDetail?orderId=" + orderId;
	}else if(orderType == "6") {
		url = "${ctx}/visa/order/goUpdateVisaOrder?visaOrderId=" + orderId +"&mainOrderCode=&details=1";
	}else {
		url = "${ctx}/orderCommon/manage/orderDetail/" + orderId ;
	}
	window.open(url,"_blank");
	//window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}

$(function(){
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
});

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
function query() {
	$('#searchForm').submit();
}
/**
 * 表单重置，不能改成 $('#searchForm')[0].reset() 
 * 输入条件点击查询之后，再点击条件重置按钮，此方法在Google Chrome 下失效 
 * @author shijun.liu
 */
function resetForm(){
	var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
	var selectArray = $('#searchForm').find("select");
	for(var i=0;i<inputArray.length;i++){
		if($(inputArray[i]).val()){
			$(inputArray[i]).val('');
		}
	}
	for(var i=0;i<selectArray.length;i++){
		var selectOption = $(selectArray[i]).children("option");
		$(selectOption[0]).attr("selected","selected");
	}
	 if($('#orderS').val() == 6)
	  	  $('#sysCountry').show();
	    else
	  	  $('#sysCountry').hide();
}

function exportToExcel(ctx){
	//表单所有参数
	var args = $('#searchForm').serialize();
	window.open(ctx + "/orderCommon/manage/exportExcel?1=1&" + args);
}

</script>

<style type="text/css">
a{
    display: inline-block;
}
label{ cursor:inherit;}
.total_sum{min-height:100px;height:auto;}
</style>
</head>
<body>
<%--<page:applyDecorator name="finance_op_head" >--%>
    <%--<page:param name="current"><c:choose><c:when test="${param.option eq 'detail' or empty param.option}">dealList</c:when><c:when test="${param.option eq 'account'}">agingList</c:when><c:when test="${param.option eq 'pay'}">payList</c:when></c:choose></page:param>--%>
<%--</page:applyDecorator>--%>
<c:choose><c:when test="${param.option eq 'detail' or empty param.option}"><c:set var ="current" value="dealList" /></c:when><c:when test="${param.option eq 'account'}"><c:set var ="current" value="agingList" /></c:when><c:when test="${param.option eq 'pay'}"><c:set var ="current" value="payList" /></c:when></c:choose>
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/finance/manage/showFinanceList/${showType}/${orderS}.htm?option=${param.option }" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
     	<div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
				<%--bug 17457 添加class inputTxt by tlw at 20170307--%>
				<input id="groupCode"  name="groupCode" class="searchInput inputTxt inputTxtlong" value="${groupCode }"  placeholder="请输入团号"/>
            </div>
			<div class="zksx">筛选</div>
            <div class="form_submit">
                 <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
                 <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
				<c:if test="${param.option eq 'detail' or empty param.option}"><input class="btn ydbz_x" onclick="exportToExcel('${ctx}')" value="导出Excel"
                   type="button"></c:if>
            </div>
            <div class="ydxbd">
				<span></span>
	        	<div class="activitylist_bodyer_right_team_co1" >
	            	<label class="activitylist_team_co3_text">
	            	<c:choose>
	            		<c:when test="${param.option eq 'detail' or empty param.option}">团队类型：</c:when>
	            		<c:otherwise>类型选择：</c:otherwise>
	            	</c:choose>
	            	</label>
					<div class="selectStyle">
						<select name="orderS" id="orderS">
							<c:forEach var="order" items="${orderTypes }">
								<option value="${order.value }" <c:if test="${orderS==order.value}">selected="selected"</c:if>>${order.label }</option>
							</c:forEach>
						</select>
					</div>
	        	</div>           
         		<div class="activitylist_bodyer_right_team_co1" >
	            	<label class="activitylist_team_co3_text">订单编号：</label>
	            	<input id="orderNum" name="orderNum" class="inputTxt inputTxtlong" value="${orderNum }"/> 
            	</div>
            	<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">下单时间：</label>
					<input id="orderTimeBegin" name="orderTimeBegin" class="inputTxt dateinput" value="${orderTimeBegin }" 
								readonly onClick="WdatePicker()"/> 至 
					<input id="orderTimeEnd" name="orderTimeEnd" value="${orderTimeEnd }" readonly onClick="WdatePicker()" 
								class="inputTxt dateinput"/>
				</div>
				<div class="activitylist_bodyer_right_team_co1" >
                    <label class="activitylist_team_co3_text">下单人：</label>
                    <select name="orderPersonId">
                        <option value="">全部</option>
                        <c:forEach var="order_person" items="${orderPersonList}">
                            <option value="${order_person.id }" <c:if test="${orderPersonId==order_person.id}">selected="selected"</c:if>>${order_person.name }</option>
                        </c:forEach>
                    </select>
                </div>
          		<c:if test="${fns:getUser().userType ==3}">
	                <div class="activitylist_bodyer_right_team_co1">
	                    <label class="activitylist_team_co3_text">渠道选择：</label>
                        <select id="modifyAgentInfoForfinace" name="agentId">
                            <option value="">全部</option>
							<c:choose>
								<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1">未签</option></c:when>
								<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }"><option value="-1">直客</option></c:when>
								<c:otherwise><option value="-1" <c:if test="${agentId == -1}">selected="selected"</c:if>>非签约渠道</option></c:otherwise>
							</c:choose>
                            <c:forEach var="agentinfo" items="${agentinfoList }">
                            	<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
								<c:choose>
								   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.agentName eq '非签约渠道'}">
								       <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>直客</option>
								   </c:when>
								   <c:otherwise>
								       <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
								   </c:otherwise>
								</c:choose> 
                            </c:forEach>
                        </select>
	                </div>
                </c:if>
            	<div class="activitylist_bodyer_right_team_co1" >
                    <label class="activitylist_team_co3_text">销售：</label>
                    <select name="saler">
                        <option value="">全部</option>
                        <c:forEach var="salers" items="${agentSalers }">
                        	<option value="${salers.key }" <c:if test="${saler==salers.key}">selected="selected"</c:if>>${salers.value }</option>
                        </c:forEach>
                    </select>
	            </div> 
	            <!-- 增加部门查询条件 -->
            	<div class="activitylist_bodyer_right_team_co1" >
                    <label class="activitylist_team_co3_text">部门：</label>
					<div class="selectStyle">
						<select name="queryDepartmentId">
							<option value="">全部</option>
							<c:forEach var="departmentlist" items="${departmentList}">
								<option value="${departmentlist.id}" <c:if test="${queryDepartmentId == departmentlist.id}">selected="selected"</c:if>>${departmentlist.name}</option>
							</c:forEach>
						</select>
					</div>
	            </div>   
             	<!-- 增加计调查询条件  -->
	         	<div class="activitylist_bodyer_right_team_co1" >
                    <label class="activitylist_team_co3_text">计调：</label>
                    <select name="jd">
                        <option value="">全部</option>
                        <c:forEach var="jd" items="${agentJd }">
                        	<option value="${jd.id }" <c:if test="${jds==jd.id}">selected="selected"</c:if>>${jd.name }</option>
                        </c:forEach>
                    </select>
	            </div> 
	            <div class="kong"></div>
	            <!-- 增加付款状态和达账状态查询条件 -->
	            <c:if test="${param.option eq 'detail' or empty param.option}">
					<div class="activitylist_bodyer_right_team_co1">
					    <label class="activitylist_team_co3_text">收款状态：</label>
						<div class="selectStyle">
							<select name="payStatus">
								<option value="0" selected="selected">全部</option>
	<%-- 					              <option value="1" <c:if test="${payStatus==1}">selected="selected"</c:if>>已付全款</option> --%>
								<option value="2" <c:if test="${payStatus==2}">selected="selected"</c:if>>未收款</option>
<%-- 					              <option value="3" <c:if test="${payStatus==3}">selected="selected"</c:if>>未付全款</option> --%>
								<option value="4" <c:if test="${payStatus==4}">selected="selected"</c:if>>已收款</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">达账状态：</label>
						<div class="selectStyle">
							<select name="accountStatus"><option value="0" selected="selected">全部</option>
									<%-- 					              <option value="1" <c:if test="${accountStatus==1}">selected="selected"</c:if>>全款达账</option> --%>
								<option value="2" <c:if test="${accountStatus==2}">selected="selected"</c:if>>未达账</option>
									<%-- 					              <option value="3" <c:if test="${accountStatus==3}">selected="selected"</c:if>>部分达账</option> --%>
								<option value="4" <c:if test="${accountStatus==4}">selected="selected"</c:if>>已达账</option>
							</select>
						</div>
					</div>
					<!-- 添加出入团日期查询条件 -->
            		<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">出团日期：</label> 
						<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateBegin"
							value='${groupOpenDateBegin }' onfocus="WdatePicker()" readonly />
						<span>至</span>
						<input id="groupCloseDate" class="inputTxt dateinput"
							name="groupOpenDateEnd" value='${groupOpenDateEnd }' onClick="WdatePicker()" readonly />
					</div>
					<c:if test="${hasPermission }">
                     	<div class="activitylist_bodyer_right_team_co1"><%--bug17473 class替换成activitylist_bodyer_right_team_co1--%>
					 		<label class="activitylist_team_co3_text">是否确认占位：</label>
                            <div class="selectStyle">
                                <select name="isSeizedConfirmed">
                                    <option value="0" selected="selected">全部</option>
                                    <option value="1" <c:if test="${isSeizedConfirmed==1}">selected="selected"</c:if>>未确认</option>
                                    <option value="2" <c:if test="${isSeizedConfirmed==2}">selected="selected"</c:if>>已确认</option>
                                </select>
                            </div>
						</div>
					</c:if>
					<div id="sysCountry" class="activitylist_bodyer_right_team_co1"  style="display:none;">
                            <div class="activitylist_team_co3_text">签证国家：</div><%--bug17473 class替换成activitylist_team_co3_text 去掉内联样式--%>
                            <select  name="sysCountryId" id="sysCountryIdForFinace">
                                <option value=" ">所有</option>
                                <c:forEach items="${countryInfoList}" var="visaCountry">
                                    <option value="${visaCountry[0]}" <c:if test="${sysCountryId eq visaCountry[0]}"> selected="selected" </c:if> >${visaCountry[1]}</option>
                                </c:forEach>
                            </select>
                    </div>
				</c:if> 
            </div>
        </div>
    </form:form>
</div>
<div class="activitylist_bodyer_right_team_co_paixu total_sum">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
            <ul>
            <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <!-- 0308&0336 交易明细  添加出团日期排序  -->
            <c:if test="${param.option eq 'detail' or empty param.option}">
            	<li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
            </c:if>
            </ul>
            <div class="cwxt-qbdd">
				全部订单：<span>${page.count}&nbsp;单</span>
				应收总金额：<span><c:if test="${empty sumTotalMoney }"></c:if>${sumTotalMoney }</span>
				未达账金额：<span><c:if test="${sumPayedMoney ne '0' }">${sumPayedMoney }</c:if></span>
				<c:if test="${param.option eq 'detail' or empty param.option}">
					应收未收总金额:<span><c:if test="${sumNotPayedMoney ne '0' }">${sumNotPayedMoney }</c:if></span>
				</c:if>
				总人数：<span>${sumPerson}</span>
			</div>
      </div>
          <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

<table id="contentTable" class="table table-striped mainTable table-bordered table-condensed activitylist_bodyer_table"  style="margin-left:0px;">
        <thead style="background:#403738;">
        <tr>
            <th width="3%">序号</th>
			<th width="8%">订单号</th>
			<%-- 需求0307 起航假期   王洋  --%>
			<c:choose>
				<c:when test="${isQHJQ }">
					<th width="7%">团号</th>
					<th width="7%">产品名称</th>
				</c:when>
				<c:when test="${isHQX }"><%--环球行显示订单团号，不再显示团号 --%>
					<th width="12%">
						<span class="tuanhao on">订单团号</span>/
						<span class="chanpin">产品名称</span>
					</th>
				</c:when>
				<c:otherwise>
					<th width="12%">
						<span class="tuanhao on">团号</span>/
						<span class="chanpin">产品名称</span>
					</th>
				</c:otherwise>
			</c:choose>
           	<c:if test="${param.option eq 'detail' or empty param.option}">
           		<th width="6%">团队类型</th>
				<c:if test="${orderS eq '6' }"><th width="6%">签证国家</th></c:if>
           	</c:if>
			<th width="6%">计调</th>
			<th width="6%">渠道</th>
			<!-- 需求0307 起航假期   王洋     -->
			<th width="6%">
				<c:choose>
					<c:when test="${isQHJQ }">
						<span>销售</span>
					</c:when>
					<c:otherwise>
						<span class="order-saler-title on">销售</span>/
                		<span class="order-picker-title">下单人</span>
					</c:otherwise>
				</c:choose>
			</th>	
			<th width="9%">预订时间</th>
			<th width="3%">人数</th>
			
			<!-- 需求253 懿洋远航显示游客，隐藏出/截团日期  modify by wangyang 2016.3.14 -->
			<c:choose>
				<c:when test="${companyUUID eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
					<c:choose>
						<c:when test="${param.option eq 'detail' or empty param.option }">
							<th width="8%">游客(已收款)</th>
						</c:when>
						<c:otherwise>
							<th width="8%">出/截团日期</th>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${isQHJQ }">
						<!-- 需求0307 起航假期   王洋   -->
							<th width="6%">出团日期</th>
							<th width="6%">截团日期</th>
						</c:when>
						<c:otherwise>
							<th width="8%">出/截团日期</th>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
			
			<th width="6%">订单状态</th>
			<c:choose>
			    <c:when test="${companyUUID eq '7a45838277a811e5bc1e000c29cf2586' }">
			         <th width="8%"><span class="total on">订单总额</span>/<span class="nopay">未收余款</span></th>
			    </c:when>
			    <c:otherwise>
			      <th width="8%">订单总额</th>
			    </c:otherwise>
			</c:choose>
			
			<th width="9%">已收金额<br>到账金额</th>
			<th width="5%">操作</th>
        </tr>
        </thead>
        <tbody>
        
        <c:if test="${fn:length(page.list) <= 0 }">
                 <tr class="toptr" >
                 <td colspan="15" style="text-align: center;">
                                      暂无搜索结果
                 </td>
                 </tr>
        </c:if>

        <c:forEach items="${page.list }" var="orders" varStatus="s">
                <tr class="toptr">
                    <td>${s.count }</td>
<%--                    <td>${orders.orderCompanyName }</td>--%>
                    <td>
                    	<c:if test="${orders.isSeizedConfirmed eq 1 and (orders.payStatus eq 3 || orders.payStatus eq 4 || orders.payStatus eq 5)}">
                    		<span style="font-size:12px;background-color:#effce1;border:1px solid #c1dba4;border-radius: 3px; color:#7cb837;padding:3px">
                               	 已确认
                            </span>
                    	</c:if>
                    	${orders.orderNum }
                    </td>
					
					<%-- 需求0307 起航假期   王洋     --%>
					<c:choose>
						<c:when test="${isQHJQ }">
							<td>
								<div title="${orders.groupCode}">
									${orders.groupCode}</div>
							</td>
							<td>
								<div title="${orders.acitivityName}">
								<c:choose>
									<c:when test="${orderS eq '6' }">
	               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.activityId}')">
								      		${orders.acitivityName}
	               		 				</a>
	               		 			</c:when>
	               		 			<c:when test="${orderS eq '7' }">
	               		 				<a href="javascript:void(0)">${orders.acitivityName}</a>
	               		 			</c:when>
	               		 			<c:otherwise>
	               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.activityId}')">
								      		${orders.acitivityName}
	               		 				</a>
	               		 			</c:otherwise>
	               		 		</c:choose>
               					</div>
							</td>
						</c:when>
						<c:otherwise>
							<td>
                    			<div class="tuanhao_cen onshow ellipsis" title="${orders.groupCode}">
									${orders.groupCode}</div>
								<div class="chanpin_cen qtip" title="${orders.acitivityName}">
								<c:choose>
									<c:when test="${orderS eq '6' }">
	               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.activityId}')">
								      		${orders.acitivityName}
	               		 				</a>
	               		 			</c:when>
	               		 			<c:when test="${orderS eq '7' }">
	               		 				<a href="javascript:void(0)">${orders.acitivityName}</a>
	               		 			</c:when>
	               		 			<c:otherwise>
	               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.activityId}')">
								      		${orders.acitivityName}
	               		 				</a>
	               		 			</c:otherwise>
	               		 		</c:choose>
               					</div>
               				</td>
						</c:otherwise>
					</c:choose>
					
               		<c:if test="${param.option eq 'detail' or empty param.option}">
               		<td>${fns:getDictLabel(orders.orderType, "order_type", "")}</td>
               		<c:if test="${orderS eq '6' }"><td>${fns:getCountryName(orders.sysCountryId) }</td></c:if>
               		</c:if>
               		<c:choose>
               			<c:when test="${not empty orders.w_operator}">
               				<td>${fns:getUserById(orders.w_operator).name}</td>
               			</c:when>
               			<c:otherwise>
               				<td> </td>
               			</c:otherwise>
               		</c:choose>
               		<td>
               		<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
               			<c:choose>
               				<c:when test="${orders.orderType lt 6 || orders.orderType eq 10}">
               					<c:choose>
               						<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and orders.orderCompanyName eq '非签约渠道' }">未签</c:when>
               						<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and orders.orderCompanyName eq '非签约渠道' }">直客</c:when>
               						<c:otherwise>${orders.orderCompanyName}</c:otherwise>
               					</c:choose>
               				</c:when>
               				<c:otherwise>
               					<c:choose>
               						<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and fns:getAgentName(orders.w_agent) eq '非签约渠道' }">未签</c:when>
               						<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and fns:getAgentName(orders.w_agent) eq '非签约渠道' }">直客</c:when>
               						<c:otherwise>${fns:getAgentName(orders.w_agent)}</c:otherwise>
               					</c:choose>
               				</c:otherwise>
               			</c:choose>
               		</td>
               		<td>
               		<%-- 需求0307 起航假期  王洋    --%>
               			<c:choose>
               				<c:when test="${isQHJQ }">
               					<span>
               						<c:if test="${not empty orders.w_saler}">
               							${fns:getUserById(orders.w_saler).name}
               						</c:if>
               					</span>
               				</c:when>
               				<c:otherwise>
               					<span class="order-saler onshow">
               						<c:if test="${not empty orders.w_saler}">
               							${fns:getUserById(orders.w_saler).name}
               						</c:if>
               					</span>
               					<span class="order-picker">
               						<c:if test="${not empty orders.createUserId}">
               							${fns:getUserById(orders.createUserId).name}
               						</c:if>
               					</span>
               				</c:otherwise>
               			</c:choose>
               		</td>
                    <td class="tc">
                      <fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>                      
                    <td class="tr">${orders.orderPersonNum }</td>
                    
                    <!-- 需求253 懿洋远航显示游客，隐藏出/截团日期  modify by wangyang 2016.3.14 -->
                    <td>
                    	<c:choose>
							<c:when test="${companyUUID eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
							<c:choose>
								<c:when test="${param.option eq 'detail' or empty param.option }">
									<c:set var="travelerName" value="${fns:getPayedTravelerName(orders.orderNum,orders.orderType) }" scope="page"/>
									<div title="${travelerName }">${fns:abbr(travelerName,23)}</div>
								</c:when>
								<c:otherwise>
									<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
									<div class="close-date"><fmt:formatDate value="${orders.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
								</c:otherwise>
							</c:choose>
							</c:when>
							<c:when test="${isQHJQ }">
								<fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/>
							</c:when>
							<c:otherwise>
								<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
								<div class="close-date"><fmt:formatDate value="${orders.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
							</c:otherwise>
						</c:choose>
					</td>
					<%-- 需求0307 起航假期  王洋   --%>
					<c:if test="${isQHJQ }">
						<td>
							<fmt:formatDate value="${orders.groupCloseDate}" pattern="yyyy-MM-dd"/>
						</td>
					</c:if>
                    <td>
                        ${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}
                    </td>
                    <td class="tr">
                        <c:choose>
                            <c:when test="${companyUUID eq '7a45838277a811e5bc1e000c29cf2586' }">
                                <span class="total_cen onshow">
	                                <c:choose>
		                            <c:when test="${fns:getMoneyAmountByUUIDOrderType(orders.totalMoney,orders.orderType,13,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
		                            <c:otherwise><span class="fbold">
		                            	<c:choose>
		                            		<c:when test="${orders.strTotalMoney == null || orders.strTotalMoney=='' }">${fns:getMoneyAmountByUUIDOrderType(orders.totalMoney,orders.orderType,13,2)}</c:when>
		                            		<c:otherwise>${orders.strTotalMoney }</c:otherwise>
		                            	</c:choose>
		                            	</span></c:otherwise>
		                            </c:choose>
	                            </span>
                                <span class="nopay_cen qtip">${fns:getNotPayMoney(orders.ordertype,orders.totalMoney,orders.payedMoney) }</span>
                            </c:when>
                            <c:otherwise>
                               <c:choose>
	                            	<c:when test="${fns:getMoneyAmountByUUIDOrderType(orders.totalMoney,orders.orderType,13,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
	                            	<c:otherwise><span class="fbold">
		                            	<c:choose>
		                            		<c:when test="${orders.strTotalMoney == null || orders.strTotalMoney=='' }">${fns:getMoneyAmountByUUIDOrderType(orders.totalMoney,orders.orderType,13,2)}</c:when>
		                            		<c:otherwise>${orders.strTotalMoney }</c:otherwise>
		                            	</c:choose>
		                            	</span></c:otherwise>
	                          </c:choose>
                            </c:otherwise>
                        </c:choose>
                    	
                    </td>
                    <td class="p0 tr">	
                        <div class="yfje_dd">
                        	<c:choose>
	                            <c:when test="${fns:getMoneyAmountByUUIDOrderType(orders.payedMoney,orders.orderType,5,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
	                            <c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(orders.payedMoney,orders.orderType,5,2)}</span></c:otherwise>
                        	</c:choose>
                       </div>
                    	<div class="dzje_dd">
                    		<c:choose>
	                            <c:when test="${fns:getMoneyAmountByUUIDOrderType(orders.accountedMoney,orders.orderType,4,2) eq '¥ 0.00'}"><span class="fbold"> </span></c:when>
	                            <c:otherwise><span class="fbold">${fns:getMoneyAmountByUUIDOrderType(orders.accountedMoney,orders.orderType,4,2)}</span></c:otherwise>
                        	</c:choose>
	                    </div>
                    </td>
						
                    <td>
                        <div class="orderList_line"> 
                     	<a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id},${orders.orderType});">查看详情 </a>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody> 
    </table>

<div class="pagination clearFix">${page}</div>
 <style type="text/css">
.orderList_dosome{ text-align:left; margin-left:11px;}
.orderList_line{ height:100%; width:50px;float:left; }
 </style>  
</body>
</html>