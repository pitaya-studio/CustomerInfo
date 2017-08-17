<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>切位收款</title>
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
	}else{
		$('.zksx').text('筛选');
	}
}

$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	
	//renderSelects(selectQuery());
	//计调模糊匹配
	$("[name=jd]").comboboxSingle();
	//销售模糊匹配
	$("[name=saler]").comboboxSingle();
	$("[name=creator]").comboboxSingle();
	//切位渠道改为可输入的select
    $("#modifyAgentInfo").comboboxInquiry();
	//$("[name=toBankNname]").comboboxInquiry();

        
	    $("#contentTable").delegate("ul.caption > li","click",function(){
	        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
	        $(this).addClass("on").siblings().removeClass('on');
	        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
	    });
	     $("#contentTable").delegate(".saler","click",function(){
	        $(this).addClass("on").siblings().removeClass('on');
	        $('.creator_cen').removeClass('onshow');
	        $('.saler_cen').addClass('onshow');
	    });
	    
	    	
	    $("#contentTable").delegate(".creator","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.saler_cen').removeClass('onshow');
	         $('.creator_cen').addClass('onshow');
	        
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
        
        var _$orderBy = $("#orderBy").val();
        var currentCompanyUuid = "${companyUuid}";
        console.log(currentCompanyUuid);
        if(!_$orderBy && 'ed88f3507ba0422b859e6d7e62161b00' == currentCompanyUuid){//诚品旅游 默认出团日期降序
        	_$orderBy = 'groupOpenDate DESC';
	       $("#orderBy").val(_$orderBy);
        }else if(!_$orderBy){
        	_$orderBy = "updateDate DESC";
        	$("#orderBy").val(_$orderBy);
        }
        var orderBy = _$orderBy.split(" ");
        $(".activitylist_paixu_left li").each(function(){
            if ($(this).hasClass("li"+orderBy[0])){
                orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                $(this).attr("class","activitylist_paixu_moren");
            }
        });

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

function saveAsAcount(id,_this){
       var $this = $(_this);
        $.jBox.confirm("确定已经到账吗？", "系统提示", function(v, h, f){
            if (v == 'ok') {
                $.ajax({
                type: "POST",
                url: "${ctx}/activityReserveOrder/manage/saveAsAcount",
                data: {
                    id:id
                },
                success: function(msg){
                    top.$.jBox.tip('确定成功','success');                   
                    $this.remove();
                    $("#searchForm").submit();
                }
             });
            }else if (v == 'cancel'){
                
            }
    });
}

function changeGroup(orderId){
    window.open("${ctx}/orderCommon/manage/changGroup/"+orderId,"_blank");
}

function orderDetail(reserveType,orderNum,orderId,productId){
	if(reserveType == 0) {
		window.open("${ctx}/stock/manager/apartGroup/reserveOrderInfo?orderNum=" + orderNum+"&orderId="+orderId+"&productId="+productId,"_blank");
	}else{
		window.open("${ctx}/stock/manager/airticket/airTicketReserveOrderInfo?orderNum=" + orderNum + "&orderId=" + orderId+"&id="+productId,"_blank");
	}
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}

$(function(){
    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }
    
    $("#groupOpenDate").datepicker({
        dateFormat:"yy-mm-dd",
       dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
       closeText:"关闭", 
       prevText:"前一月", 
       nextText:"后一月",
       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
       });
    
    $("#groupCloseDate").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
    
});

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
}


function downLoad(ctx){
    var args = $('#searchForm').serialize();//查询条件参数
    window.open(ctx + "/orderCommon/manage/exportReserveOrderList?" + args);
}
</script>

<style type="text/css">
a{
    display: inline-block;
}
label{ cursor:inherit;}
</style>
</head>
<body>
<c:choose><c:when test="${param.option eq 'detail' or empty param.option}"><c:set var ="current" value="dealList" /></c:when><c:when test="${param.option eq 'account'}"><c:set var ="current" value="agingList" /></c:when><c:when test="${param.option eq 'pay'}"><c:set var ="current" value="payList" /></c:when></c:choose>
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
 <form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/finance/manage/showFinanceList/${showType}/${orderS}.htm?option=reserve" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="showType" name="showType" type="hidden" value="${showType}" />
     	 <div class="activitylist_bodyer_right_team_co">
           <div class="activitylist_bodyer_right_team_co2">
	            <input id="groupCode" name="groupCode" class="inputTxt searchInput inputTxtlong" value="${groupCode }"placeholder="请输入团号"/>
           </div>
           <div class="zksx">筛选</div>
           <div class="form_submit">
               <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
               <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
               <input class="btn ydbz_x" type="button" onclick="downLoad('${ctx}')" value="导出Excel" />

           </div>
           <div class="ydxbd">
               <span></span>
               <div class="activitylist_bodyer_right_team_co1" >
                <label class="activitylist_team_co3_text">团队类型：</label>
                <div class="selectStyle">
                    <select name="orderS" id="orderS">
                        <c:forEach var="order" items="${orderTypes }">
                            <option value="${order.value }" <c:if test="${orderS==order.value}">selected="selected"</c:if>>${order.label }</option>
                        </c:forEach>
                    </select>
                </div>
	        </div>
               <div class="activitylist_bodyer_right_team_co1" >
	            <div class="activitylist_team_co3_text" >订单编号：</div>
	            <input id="orderNum" name="orderNum" class="inputTxt inputTxtlong" value="${orderNum }"/> 
            </div>
               <div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co4_text">银行到账日期：</label><input id="accountDateBegin" name="accountDateBegin" class="inputTxt dateinput" value="${accountDateBegin }" 
											readonly onClick="WdatePicker()"/> 至 
				<input id="accountDateEnd" name="accountDateEnd" value="${accountDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
			<!-- 增加是否到账的查询条件  -->
               <div class="activitylist_bodyer_right_team_co1" >
               <label class="activitylist_team_co3_text">是否到账：</label>
                   <div class="selectStyle">
                       <select name="isAccounted">
                           <option value="">全部</option>
                           <option value="Y" <c:if test="${isAccounted eq 'Y'}">selected="selected"</c:if>>已到账</option>
                           <option value="N" <c:if test="${isAccounted eq 'N'}">selected="selected"</c:if>>未到账</option>
                       </select>
                   </div>
            </div>
               <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">渠道选择：</label>
                <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
                <select id="modifyAgentInfo" name="agentId">
                    <option value="">全部</option>
                    <c:choose>
                        <c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1">未签</option></c:when>
                        <c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }"><option value="-1">直客</option></c:when>
                        <c:otherwise><option value="-1">非签约渠道</option></c:otherwise>
                    </c:choose>
                    <c:forEach var="agentinfo" items="${agentinfoList }">
                        <c:choose>
                            <c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agentinfo.agentName=='非签约渠道' }">
                            <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>直客</option>
                            </c:when>
                            <c:otherwise>
                            <option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
               <div class="activitylist_bodyer_right_team_co1" >
	            <label class="activitylist_team_co3_text">销售：</label>
	            <select name="saler" style="width:44%;">
                    <option value="">全部</option>
                    <c:forEach var="salers" items="${agentSalers }">
                    <option value="${salers.key }" <c:if test="${saler==salers.key}">selected="selected"</c:if>>${salers.value }</option>
                    </c:forEach>
	            </select>
	        </div>
               <div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款日期：</label>
				<input id="createDateBegin" name="createDateBegin" class="inputTxt dateinput" value="${createDateBegin }" 
											readonly onClick="WdatePicker()"/> 至 
				<input id="createDateEnd" name="createDateEnd" value="${createDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
	        <!-- 增加计调查询条件 -->
               <div class="activitylist_bodyer_right_team_co1" >
	             <label class="activitylist_team_co3_text">计调：</label>
	             <select name="jd">
	                 <option value="">全部</option>
	                 <c:forEach var="operator" items="${agentJd }">
	                    <option value="${operator.id }" <c:if test="${jds==operator.id}">selected="selected"</c:if>>${operator.name }</option>
	                 </c:forEach>
	             </select>
            </div>
               <div class="activitylist_bodyer_right_team_co1" >
	            <div class="activitylist_team_co3_text" >来款单位：</div>
	            <input id="payerName" name="payerName" class="inputTxt inputTxtlong" value="${payerName }"/> 
            </div>
               <div class="activitylist_bodyer_right_team_co1" >
	            <label class="activitylist_team_co3_text">下单人：</label>
                <select name="creator">
                    <option value="">全部</option>
                    <c:forEach var="creators" items="${creatorList }">
                        <option value="${creators.id }" <c:if test="${creator==creators.id}">selected="selected"</c:if>>${creators.name }</option>
                    </c:forEach>
                </select>
	        </div>
            <!-- 付款金额查询条件 -->
               <div class="activitylist_bodyer_right_team_co1">
               <label class="activitylist_team_co3_text">收款金额：</label>
               <input id="payAmountStrat" class="inputTxt" name="payAmountStrat"  value="${payAmountStrat}"  onkeyup="validNum(this)" onafterpaste="validNum(this)" /> 至
               <input id="payAmountEnd" class="inputTxt"   name="payAmountEnd"    value="${payAmountEnd}"    onkeyup="validNum(this)" onafterpaste="validNum(this)" />
            </div>
	        <!--支付方式查询条件，sys_dict字典表有支付方式的数据，但是各系统平台维护的有点乱，所以在此直接写死了  -->
               <div class="activitylist_bodyer_right_team_co1" >
                   <label class="activitylist_team_co3_text">收款方式：</label>
                   <div class="selectStyle">
                       <select name="payType">
                           <option value="">全部</option>
                           <option value="1" <c:if test="${payType eq '1'}">selected="selected"</c:if>>支票</option>
                           <option value="3" <c:if test="${payType eq '3' or payType eq '5'}">selected="selected"</c:if>>现金</option>
                           <option value="4" <c:if test="${payType eq '4'}">selected="selected"</c:if>>汇款</option>
                           <option value="6" <c:if test="${payType eq '6'}">selected="selected"</c:if>>转账</option>
                           <option value="7" <c:if test="${payType eq '7'}">selected="selected"</c:if>>汇票</option>
                           <option value="8" <c:if test="${payType eq '8'}">selected="selected"</c:if>>POS</option>
                           <c:if test="${isLMT }">
                             <option value="9" <c:if test="${payType eq '9'}">selected="selected"</c:if>>因公支付宝</option>
                           </c:if>
                       </select>
                   </div>
                </div>
            <!-- 添加出收款确认日期查询条件 需求0405  -->
                <div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款确认日期：</label>
				<input class="inputTxt dateinput" name="receiptConfirmDateBegin"
					value='${receiptConfirmDateBegin }' onfocus="WdatePicker()" readonly />
				<span style="font-size:12px; font-family:'宋体';">至</span> 
				<input class="inputTxt dateinput"
					name="receiptConfirmDateEnd" value='${receiptConfirmDateEnd }' onClick="WdatePicker()" readonly />
			</div>
            <!-- 添加出入团日期查询条件 -->
                <c:if test="${companyUuid eq '58a27feeab3944378b266aff05b627d2' }">
                    <div class="activitylist_bodyer_right_team_co1">
                        <label class="activitylist_team_co3_text">出团日期：</label>
                        <input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateBegin"
                            value='${groupOpenDateBegin }' onfocus="WdatePicker()" readonly />
                        <span style="font-size:12px; font-family:'宋体';">至</span>
                        <input id="groupCloseDate" class="inputTxt dateinput"
                            name="groupOpenDateEnd" value='${groupOpenDateEnd }' onClick="WdatePicker()" readonly />
                    </div>
                </c:if>
            </div>
          </div>
    </form:form>
    
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
            <ul>
            <li class="activitylist_paixu_left_biankuang licreateDate"><a onClick="sortby('createDate',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang liupdateDate"><a onClick="sortby('updateDate',this)">更新时间</a></li>
            <li class="activitylist_paixu_left_biankuang ligroupOpenDate"><a onClick="sortby('groupOpenDate',this)">出团日期</a></li>
            </ul>
      </div>
            <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        </div>
      </div>

<table id="contentTable" class="table mainTable table-striped table-bordered table-condensed activitylist_bodyer_table"  style="margin-left:0px;">
   	<thead style="background:#403738;">
        <tr>
            <th width="3%">序号</th>
             <th width="6%">收款日期<br>银行到账日期</th>
             <th width="7%">收款确认日期</th>
			<th width="8%">切位订单号</th>
			<c:choose>
				<c:when test="${isQHJQ }">
					<th width="4%">团号</th>
					<th width="5%">产品名称</th>
				</c:when>
				<c:otherwise>
					<th width="8%">
						<span class="tuanhao on">团号</span>/
						<span class="chanpin">产品名称</span>
					</th>
				</c:otherwise>
			</c:choose>
			<c:if test="${companyUuid eq '58a27feeab3944378b266aff05b627d2' }">
				<th width="6%">出团日期</th>
			</c:if>
            <th width="5%">团队类型</th>
			<th width="4%">计调</th>
			<th width="7%">
				<c:choose>
					<c:when test="${isQHJQ }">
						<span>销售</span>
					</c:when>
					<c:otherwise>
						<span class="saler on">销售</span>/
						<span class="creator">下单人</span>
					</c:otherwise>
				</c:choose>
	        </th>
			<th width="8%">渠道名称</th>
			<th width="6%">来款单位</th>
			<th width="6%">收款银行</th>
			<th width="5%">订单状态</th>
			<th width="6%">订单金额</th>
			<th width="8%">已收金额<br>到账金额</th>
			<th width="5%">收款确认</th>
			<th width="4%">操作</th>
        </tr>
        </thead>
        <tbody>
        
        <c:if test="${fn:length(page.list) <= 0 }">
                 <tr class="toptr" >
                 <td colspan="16" style="text-align: center;">
                                      暂无搜索结果
                 </td>
                 </tr>
        </c:if>
        
        
        <c:forEach items="${page.list }" var="orders" varStatus="s">
                <tr class="toptr">
                    <td>${s.count }</td>
                     <td>
                        <div class="out-date"> 
						<c:if test="${not empty orders.createDate }"><fmt:formatDate value="${orders.createDate}" pattern="yyyy-MM-dd"/></c:if> 
						</div> 
 						<div class="close-date"> 
						<c:if test="${not empty orders.updateDate }"><c:if test="${orders.confirm eq '1'}"><fmt:formatDate value="${orders.updateDate}" pattern="yyyy-MM-dd"/></c:if></c:if></div> 
                    </td>
					<!-- 0405 -->
					<td>
						<c:choose>
							<c:when test="${orders.confirm eq 1 }">
								<fmt:formatDate value="${orders.updateDate }" pattern="yyyy-MM-dd"/>
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
					</td>
                    <td>${orders.orderNum }</td>
					<!-- 需求0307 起航假期  王洋 -->
					<c:choose>
						<c:when test="${isQHJQ }">
							<td><div title="${orders.groupCode}">${orders.groupCode}</div></td>
							<td>
								<div title="${orders.acitivityName}">
	               		 			<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.productId}')">
			               			<c:choose> 
								     	<c:when test="${fn:length(orders.acitivityName) > 15}"> 
								     	 	${fn:substring(orders.acitivityName, 0, 15)}...
								     	</c:when> 
								     	<c:otherwise> 
								      		${orders.acitivityName}
								     	</c:otherwise>
									</c:choose>
	               		 			</a>
               					</div>
							</td>
						</c:when>
						<c:otherwise>
							<td>
                    			<div class="tuanhao_cen onshow" title="${orders.groupCode}"> 
                    				${orders.groupCode}</div>
								<div class="chanpin_cen qtip" title="${orders.acitivityName}">
	               		 			<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.productId}')">
			               			<c:choose> 
								     	<c:when test="${fn:length(orders.acitivityName) > 15}"> 
								     	 	${fn:substring(orders.acitivityName, 0, 15)}...
								     	</c:when> 
								     	<c:otherwise> 
								      		${orders.acitivityName}
								     	</c:otherwise>
									</c:choose>
	               		 			</a>
               					</div>
               				</td>
						</c:otherwise>
					</c:choose>

               		<!-- 加入出团日期 -->
               		<c:if test="${companyUuid eq '58a27feeab3944378b266aff05b627d2' }">
               			<td>
               	 			<div>
               					<fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/>
               				</div>  
               			</td>
               		</c:if>
                    <td> ${fns:getDictLabel(orders.orderType, "order_type", "")}</td>
               		<c:choose>
               			<c:when test="${not empty orders.operator}">
               				<td>${fns:getUserById(orders.operator).name}</td>
               			</c:when>
               			<c:otherwise>
               				<td> </td>
               			</c:otherwise>
               		</c:choose>
                    <td>
                    	<!--  需求0307 起航假期 王洋 -->
                    	<c:choose>
                    		<c:when test="${isQHJQ }">
                    			<div title="${fns:getUserById(orders.saleId).name}">${fns:getUserById(orders.saleId).name }</div>
                    		</c:when>
                    		<c:otherwise>
                    			<div class="saler_cen onshow" title="${fns:getUserById(orders.saleId).name}">${fns:getUserById(orders.saleId).name }</div>
                        		<div class="creator_cen qtip" title="${fns:getUserById(orders.createBy).name }">${fns:getUserById(orders.createBy).name}</div>
                    		</c:otherwise>
                    	</c:choose>
                    </td>
                    <td>
                    	<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && orders.agentName=='非签约渠道'}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						       ${orders.agentName }
						   </c:otherwise>
						</c:choose> 
                    </td>
                    <td style="text-align: center;"></td>
                    <td style="text-align: center;"></td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty orders.orderStatus}">
                                <c:choose>
                                    <c:when test="${orders.orderStatus eq 0 }">未收订金</c:when>
						            <c:otherwise>已收订金</c:otherwise>
						        </c:choose>
					        </c:when>
					        <c:otherwise></c:otherwise>
                        </c:choose>
                    </td>
                    <td class="tr">
                      <c:if test="${not empty orders.orderMoney}">¥</c:if><span class="fbold"><span class="fbold"><fmt:formatNumber value='${orders.orderMoney}' type="currency" pattern="#,##0.00" /></span>
                    </td>
                    <td class="p0 tr pr">
                        <c:choose>
		            	  <c:when test="${orders.payType == 1}"><p><span class="icon-mode-of-payment-check"></span></p></c:when>
		            	  <c:when test="${orders.payType == 3 or orders.payType==5}"><p><span class="icon-mode-of-payment-cash"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 4 }"><p><span class="icon-mode-of-payment-remit"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 6}"><p><span class="icon-mode-of-payment-transfer"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 7}"><p><span class="icon-mode-of-payment-bill"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 8 or orders.payType==2}"><span class="icon-mode-of-payment-pos"> </span></p></c:when>
		            	  <c:when test="${orders.payType == 9}"><p><span class="icon-mode-of-payment-alipay"> </span></p></c:when>
		            	</c:choose>
                        <div class="yfje_dd">
                     		<c:if test="${not empty orders.orderMoney}">¥</c:if><span class="fbold"><fmt:formatNumber value='${orders.orderMoney}' type="currency" pattern="#,##0.00" /></span>
                       </div>
                    	<div class="dzje_dd">
	                      	<c:if test="${orders.confirm == 0 }"></c:if>
	                      	<c:if test="${orders.confirm == 1 }">¥<span class="fbold"><fmt:formatNumber value='${orders.payMoney}' type="currency" pattern="#,##0.00" /></span></c:if>	
	                    </div>
                    </td>
					<td>
                        <c:choose>
                            <c:when test="${not empty orders.confirm}">
                                <c:choose>
                                    <c:when test="${orders.confirm eq 0 }">
                                        <a href="javascript:void(0)" onClick="javascript:saveAsAcount(${orders.orderId});">收款确认 </a>
                                    </c:when>
                                    <c:otherwise>
                                        已确认
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise></c:otherwise>
                        </c:choose>
					</td>
                    <td>
                        <div class="orderList_line">
                            <a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.reserveType },'${orders.orderNum}',${orders.orderId },${orders.productId });">查看详情 </a>
                        </div>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
<div class="pagination clearFix">${page}</div>
 <style type="text/css">
.orderList_line{ height:100%; width:50px;float:left; }
 </style>  
</body>
</html>