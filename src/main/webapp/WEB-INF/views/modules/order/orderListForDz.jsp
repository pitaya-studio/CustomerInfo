<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单收款</title>
<meta name="decorator" content="wholesaler"/>  
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payedConfirm.js" type="text/javascript"></script>
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
	//收款银行模糊匹配
	$("[name=toBankNname]").comboboxSingle();
	//销售模糊匹配
	$("[name=saler]").comboboxSingle();
	$("[name=creator]").comboboxSingle();
	$(document).delegate(".downloadzfpz","click",function(){
        window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
    });
	
	//渠道改为可输入的select
    $("#modifyAgentInfo").comboboxInquiry();
	
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
	        $('.account_cen').removeClass('onshow');
	        $('.nopay_cen').addClass('onshow');
	    });
	    
	    	
	    $("#contentTable").delegate(".account","click",function(){
	         $(this).addClass("on").siblings().removeClass('on');
	         $('.nopay_cen').removeClass('onshow');
	         $('.account_cen').addClass('onshow');
	        
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
	    
        var _$orderBy = $("#orderBy").val();
        var currentCompanyUuid = "${companyUuid}";
        console.log(currentCompanyUuid);
        if(!_$orderBy && 'ed88f3507ba0422b859e6d7e62161b00' == currentCompanyUuid){//诚品旅游 默认出团日期降序
        	_$orderBy = 'groupOpenDate DESC';
	        $("#orderBy").val(_$orderBy);
        }else if(!_$orderBy){
	        $("#orderBy").val("updateDate DESC");
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

function cancelConfirm(orderId,payId,payType,ordertype,_this){
	 var $this = $(_this);
		$.jBox.confirm("确定撤销吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderPayMore/cancelConfirmOper",
			        async:false,
			        cache:false,
			        data: {
			        	orderId:orderId,
			        	payId:payId,
			        	payType:payType,
			        	ordertype:ordertype
			        },
			        success: function(data){
			        	top.$.jBox.tip('确定成功','success');                   
	                    $this.remove();
	                    $("#searchForm").submit();
			        }
		     	}); 
			}else if (v == 'cancel'){
				
			}
	});
}
//海岛游确认收款撤销20150617
function newProductCancelConfirm(orderId,payId,payType,ordertype,_this){
	 var $this = $(_this);
		$.jBox.confirm("确定撤销吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderPayMore/newProductCancelConfirmOper",
			        async:false,
			        cache:false,
			        data: {
			        	orderUuid:orderId,
			        	payUuid:payId,
			        	payType:payType,
			        	ordertype:ordertype
			        },
			        success: function(data){
			        	top.$.jBox.tip('确定成功','success');                   
	                    $this.remove();
	                    $("#searchForm").submit();
			        }
		     	}); 
			}else if (v == 'cancel'){
				
			}
	});
}

function saveAsAcount(orderId,moneyType,orderType,serialNum,_this){
	   var $this = $(_this);
		$.jBox.confirm("确定已经到账吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderCommon/manage/saveAsAcount",
			        async:false,
			        cache:false,
			        data: {
			        	orderId:orderId,
			        	moneyType:moneyType,
			        	orderType:orderType,
			        	serialNum:serialNum
			        },
			        success: function(data){
			        	top.$.jBox.tip('确定成功','success');                   
	                    $this.remove();
	                    $("#searchForm").submit();
			        }
		     	}); 
			}else if (v == 'cancel'){
				
			}
	});
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
	//首次打印提醒
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	})
	
});

$(function(){
    $('.nav-tabs li').hover(function(){
    	$('.nav-tabs li').removeClass('current');
    	$(this).parent().removeClass('nav_current');
         if($(this).hasClass('ernav')){
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



function clearNoNum(event, obj) {
	//响应鼠标事件，允许左右方向键移动
	event = window.event || event;
	if (event.keyCode == 37 | event.keyCode == 39) {
		return false;
	}
	var t = obj.value.charAt(0);
	//先把非数字的都替换掉，除了数字和.
	obj.value = obj.value.replace(/[^\d.]/g, "");
	//必须保证第一个为数字而不是.
	obj.value = obj.value.replace(/^\./g, "");
	//保证只有出现一个.而没有多个.
	obj.value = obj.value.replace(/\.{2,}/g, ".");
	//保证.只出现一次，而不能出现两次以上
	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
	//如果第一位是负号，则允许添加   如果不允许添加负号 可以把这块注释掉
	if (t == '-') {
		obj.value = '-' + obj.value;
	}
}
function downLoad(ctx){
	var args = $('#searchForm').serialize();//查询条件参数
	window.open(ctx + "/orderCommon/manage/downLoadOrderListForDZ?" + args);
}

// 批量打印- wangyang 2016.10.20
$(document).on('change','#contentTable,#contentTable_foot, [type="checkbox"]',function(){
	var $this = $(this);
    var $contentContainer = $("#contentTable,#contentTable_foot");
    var $all =$contentContainer.find('[check-action="All"]');
    var $reverse = $contentContainer.find('[check-action="Reverse"]');
    var $chks=$contentContainer.find('[check-action="Normal"]:enabled');
    
    if($this.is('[check-action="All"]')){
    	if($this.is(':checked')){
        	$chks.attr('checked',true);
        }else{
        	$chks.removeAttr('checked');
        }
    }
    
    if($this.is('[check-action="Reverse"]')){
    	$chks.each(function(){
        	var $chk = $(this);
            if($chk.is(':checked')){
            	$chk.removeAttr('checked');
            }else{
            	$chk.attr('checked',true);
            }
        });
    }
    
    if($chks.length && ($chks.length ==$chks.filter(':checked').length)){
    	$all.attr('checked',true);
    }else{
    	$all.removeAttr('checked');
    }
});

function batchPrint(){
	var checks = $(".box:checked");
	var payids = new Array();
	var ordertypes = new Array();
	
	// 未勾选订单时提示并中断操作
	if (checks.length == 0) {
		top.$.jBox.tip('请选择数据', 'warnning');
		return ;
	}
	
	for (var i = 0; i < checks.length; i++) {
		var check = checks[i];
		if (check.value.indexOf('+') > -1) {
			payids.push(check.value.split('+')[0]);
			ordertypes.push(check.value.split('+')[1]);
		}
	}

	var $div = $("#batchPrintDiv");
	$div.find("#payids").val(payids);
	$div.find("#ordertypes").val(ordertypes);
	
	$form = $div.find("#batchPrintForm");
	$form.submit();
}
</script>
<style type="text/css">
a{display: inline-block;}
label{ cursor:inherit;}
.disableCss{
	pointer-events:none;
	color:#afafaf;
	cursor:default
} 
.account{
	border-top:none !important;
	padding:0px;
	width:auto;
}
div.jbox .jbox-content {
	overflow-y: hidden !important;
}

.activitylist_paixu_left{ 
	width:79%;
	float:left;
}
input[type="checkbox"] {margin: 0;padding-left: 0;padding-right: 0;}
</style>
</head>
<body>
<!-- 顶部参数 -->
<%--<page:applyDecorator name="finance_op_head" >--%>
    <%--<page:param name="showType">${showType}</page:param>--%>
<%--</page:applyDecorator>--%>
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%" id="zhmdiv">
 <form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/finance/manage/showFinanceList/${showType}/${orderS}.htm?option=order" method="post" >
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
        <input id="showType" name="showType" type="hidden" value="${showType}" />
     	 <div class="activitylist_bodyer_right_team_co">
            <div class="activitylist_bodyer_right_team_co2">
	            <input id="groupCode"  name="groupCode" class="inputTxt searchInput inputTxtlong" value="${groupCode }" placeholder="请输入团号"/>
            </div>
            <div class="zksx">筛选</div>
            <div class="form_submit">
                 <input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
                 <input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
                 <input class="btn ydbz_x" type="button" onclick="downLoad('${ctx}')" value="导出excel">
            </div>

            <div class="ydxbd">
                <span></span>
                <div class="activitylist_bodyer_right_team_co1" >
                       <label class="activitylist_team_co3_text">团队类型：</label>
                       <div class="selectStyle">
                           <select name="orderS" id="orderS">
                              <c:forEach var="order" items="${orderTypes }">
                                    <!-- 由于签证订单收款已经抽取为一个单独的功能，因此类型选择去掉签证类型（签证类型的值在数据库中为6） -->
                                  <c:if test="${order.value != 6}">
                                      <option value="${order.value }" <c:if test="${orderS==order.value}">selected="selected"</c:if>>${order.label }</option>
                                  </c:if>
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
          <!-- 增加计调查询条件 -->
	            <div class="activitylist_bodyer_right_team_co1" >
	            <label class="activitylist_team_co3_text">计调：</label>
	            <select name="jd">
	            	<option value="">全部</option>
	            	<c:forEach var="jd" items="${agentJd }">
	            		<option value="${jd.id }" <c:if test="${jds==jd.id}">selected="selected"</c:if>>${jd.name }</option>
	            	</c:forEach>
	            </select>
	      </div>
	            <div class="activitylist_bodyer_right_team_co1" >
                <label class="activitylist_team_co3_text">销售：</label>
                <select name="saler">
                    <option value="">全部</option>
                    <c:forEach var="salers" items="${agentSalers }">
                    <option value="${salers.key }" <c:if test="${saler==salers.key}">selected="selected"</c:if>>${salers.value }</option>
                    </c:forEach>
                </select>
          </div>
                <c:choose>
                    <c:when test="${orderS ne '11' and orderS ne '12' }">
                        <div class="activitylist_bodyer_right_team_co1" >
                            <label class="activitylist_team_co3_text">下单人：</label>
                            <select name="creator">
                                <option value="">全部</option>
                                <c:forEach var="creators" items="${orderPersonList }">
                                <option value="${creators.id }" <c:if test="${creator==creators.id}">selected="selected"</c:if>>${creators.name }</option>
                                </c:forEach>
                            </select>
                         </div>
                    </c:when>
                </c:choose>
                <c:if test="${fns:getUser().userType ==3}">
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
                </c:if>
            <!-- 增加是否到账的查询条件  -->
                <div class="activitylist_bodyer_right_team_co1" >
                    <label class="activitylist_team_co3_text">是否到账：</label>
                    <div class="selectStyle">
                    <select name="isAccounted">
                        <option value="">全部</option>
                        <option value="Y" <c:if test="${isAccounted eq 'Y'}">selected="selected"</c:if>>已到账</option>
                        <option value="N" <c:if test="${isAccounted eq 'N'}">selected="selected"</c:if>>未到账</option>
                        <option value="C" <c:if test="${isAccounted eq 'C'}">selected="selected"</c:if>>已驳回</option>
                    </select>
                    </div>
                </div>
                <div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款日期：</label>
				<input id="createDateBegin" name="createDateBegin" class="inputTxt dateinput" value="${createDateBegin }" 
											readonly onClick="WdatePicker()"/> 至 
				<input id="createDateEnd" name="createDateEnd" value="${createDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
                <div class="activitylist_bodyer_right_team_co1" >
	            <div class="activitylist_team_co3_text" >来款单位：</div>
	            <input id="payerName" name="payerName" class="inputTxt inputTxtlong" value="${payerName }"/> 
            </div>
                <div class="activitylist_bodyer_right_team_co1" >
	            <label class="activitylist_team_co3_text">收款银行：</label>
	            <select name="toBankNname">
	            	<option value="">全部</option>
	            	<c:forEach var="toBank" items="${toBank }">
	            		<option value="${toBank.text }" <c:if test="${toBankNname==toBank.text}">selected="selected"</c:if>>${toBank.text }</option>
	            	</c:forEach>
	            </select>
	       </div> 
            <!-- 付款金额查询条件 -->
                <div class="activitylist_bodyer_right_team_co1">
               <label class="activitylist_team_co3_text">收款金额：</label>
               <input id="payAmountStrat" name="payAmountStrat" class="inputTxt" value="${payAmountStrat}"  onkeyup="clearNoNum(event,this)" onafterpaste="clearNoNum(event,this)" autocomplete="off"/> 至
               <input id="payAmountEnd"   name="payAmountEnd"  class="inputTxt"  value="${payAmountEnd}"    onkeyup="clearNoNum(event,this)" onafterpaste="clearNoNum(event,this)" autocomplete="off"/>
            </div>
                <div class="activitylist_bodyer_right_team_co1" >
                <label class="activitylist_team_co3_text">打印状态：</label>
                <div class="selectStyle">
                   <select name="printFlag">
                       <option value="">全部</option>
                       <option value="1" <c:if test="${printFlag eq '1'}">selected="selected"</c:if>>已打印</option>
                       <option value="0" <c:if test="${printFlag eq '0'}">selected="selected"</c:if>>未打印</option>
                   </select>
                </div>
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
           	    <div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">出团日期：</label>
				<input id="groupOpenDateStart" name="groupOpenDateBegin" class="inputTxt dateinput" value="${groupOpenDateBegin }"
											readonly onClick="WdatePicker()"/> 至 
				<input id="groupOpenDateEnd" name="groupOpenDateEnd" value="${groupOpenDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
			    <div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款确认日期：</label>
				<input id="receiptConfirmationDateBegin" name="receiptConfirmationDateBegin" class="inputTxt dateinput" value="${receiptConfirmationDateBegin }"
											readonly onClick="WdatePicker()"/> 至 
				<input id="receiptConfirmationDateEnd" name="receiptConfirmationDateEnd" value="${receiptConfirmationDateEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
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
          <div class="activitylist_paixu_right"><!--<input class="btn btn-primary " onclick="downLoad('${ctx}')" value="导出Excel" type="button">-->查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        </div>
      </div>


<table id="contentTable" class="table mainTable table-striped table-condensed activitylist_bodyer_table"  style="margin-left:0px;">
	<!--  需求0307 起航假期  王洋   -->
        <thead style="background:#403738;">
        <tr>
            <th width="4%">序号</th>
            <th width="8%">收款日期<br>银行到账日期</th>
			<th width="8%">收款确认日期</th>
			<th width="8%">订单号</th>
			<th width="4%">团队类型</th>
			<c:choose>
				<c:when test="${isQHJQ }">
					<th width="6%">团号</th>
					<th width="7%">产品名称</th>
				</c:when>
				<c:otherwise>
					<th width="8%">
						<span class="tuanhao on">团号</span>/
						<span class="chanpin">产品名称</span>
					</th>
				</c:otherwise>
			</c:choose>
			<c:if test="${'58a27feeab3944378b266aff05b627d2' eq companyUuid }">
				<th width="5%">出团日期</th>
			</c:if>
			<th width="4%">计调</th>
			<c:choose>
			    <c:when test="${orderS eq '11' || orderS eq '12' || isQHJQ }">
			       <th width="4%">销售</th>
			    </c:when>
			    <c:otherwise>
			        <th width="4%">
		                <span class="saler on">销售</span>/
						<span class="creator">下单人</span>
			        </th>
			    </c:otherwise>
			</c:choose>
			<th width="7%">渠道名称</th>
			<th width="6%">来款单位</th>
			<th width="6%">收款银行</th>
			<th width="6%">订单状态</th>
			<c:choose>
			    <c:when test="${companyUUID eq '7a45838277a811e5bc1e000c29cf2586' }">
			        <th width="8%"><p>订单金额</p>
			        <span class="nopay on">未收金额</span>/<span class="account" style="padding:0px;">累计达账金额</span></th>
			    </c:when>
			    <c:otherwise>
			        <th width="6%">订单金额<br>累计达账金额</th>
			    </c:otherwise>
			</c:choose>
			<th width="6%">已收金额<br>到账金额</th>
			<th width="5%">收款确认 </th>
			<th width="4%">打印确认 </th>
			<th width="5%">操作</th>
	    </tr>
	    </thead>
        <tbody>
         <c:if test="${fn:length(page.list) <= 0 }">
			<tr class="toptr" >
				<td colspan="17" style="text-align: center;">暂无搜索结果</td>
			</tr>
        </c:if>
        <c:forEach items="${page.list }" var="orders" varStatus="s">
		        <tr class="toptr">
		            <td class="checkbox_td">
		            	<input type="checkbox" check-action="Normal" class="box" value="${orders.payid }+${orders.ordertype }" />
		            	${s.count }
		            </td>
 					<td>
						<div class="out-date">
							<c:if test="${not empty orders.createDate }"><fmt:formatDate value="${orders.createDate}" pattern="yyyy-MM-dd"/></c:if>
						</div>
 						<div class="close-date"> 
							<c:if test="${not empty orders.accountDate }">
								<c:if test="${orders.account eq '1'}"><fmt:formatDate value="${orders.accountDate}" pattern="yyyy-MM-dd"/></c:if>
							</c:if>
						</div>
                    </td>
                    <td><fmt:formatDate value="${orders.receiptConfirmationDate }" pattern="yyyy-MM-dd"/></td>
                    <td>${orders.orderNum }</td>
                    <td>${fns:getDictLabel(orders.ordertype, "order_type", "")}</td>
		            <c:choose>
		            	<c:when test="${isQHJQ }">
		            		<td>
		            			<div title="${orders.groupCode}">${orders.groupCode}</div>
		            		</td>
		            		<td>
		            			<div title="${orders.acitivityName}">
	               		 		<c:choose>
									<c:when test="${orders.ordertype eq '6' }">
	               		 			<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.activityId}')">
								    	${orders.acitivityName}
	               		 			</a>
	               		 			</c:when>
	               		 			<c:when test="${orders.ordertype eq '12' }">
	               		 				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${orders.activityId}?type=product')">
								    		${orders.acitivityName}
	               		 				</a>
	               		 			</c:when>
	               		 			<c:when test="${orders.ordertype eq '11' }"> <!-- 酒店 -->
										<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${orders.activityId}?type=product')">
							      			${orders.acitivityName}
               		 					</a>
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
										<c:when test="${orders.ordertype eq '6' }">
	               		 					<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/visa/visaProducts/visaProductsDetail/${orders.activityId}')">
								    			${orders.acitivityName}
	               		 				</a>
	               		 				</c:when>
	               		 				<c:when test="${orders.ordertype eq '12' }">
	               		 					<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${orders.activityId}?type=product')">
								    			${orders.acitivityName}
	               		 					</a>
	               		 				</c:when>
	               		 				<c:when test="${orders.ordertype eq '11' }"> <!-- 酒店 -->
											<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${orders.activityId}?type=product')">
							      				${orders.acitivityName}
               		 						</a>
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
               		<c:if test="${'58a27feeab3944378b266aff05b627d2' eq companyUuid }">
						<td><fmt:formatDate value="${orders.groupOpenDate}" type="both" pattern="yyyy-MM-dd" /></td>
					</c:if>
               		<c:choose>
               			<c:when test="${orders.opCreateBy != null && orders.opCreateBy != ''}">
               				<td>${fns:getUserById(orders.opCreateBy).name}</td>
               			</c:when>
               			<c:otherwise>
               				<td></td>
               			</c:otherwise>
               		</c:choose>
               		<c:choose>
               		    <c:when test="${orderS eq '11' || orderS eq '12' }">
               		        <td>${orders.createUserName }</td>
               		    </c:when>
               		    <c:when test="${qhjq }">
               		    	<td>
               		    		<div title="${orders.saler}">${orders.saler}</div>
               		    	</td>
               		    </c:when>
               		    <c:otherwise>
               		        <td>
		                        <div class="saler_cen onshow" title="${orders.saler}">${orders.saler}</div>
		                        <div class="creator_cen qtip" title="${orders.creator}">${orders.creator}</div>
		                    </td>
               		    </c:otherwise>
               		</c:choose>
                    <td>
                    	<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
                    	<c:choose>
                    		<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and orders.orderCompanyName eq '非签约渠道' }">未签</c:when>
                    		<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and orders.orderCompanyName eq '非签约渠道' }">直客</c:when>
                    		<c:otherwise>${orders.orderCompanyName }</c:otherwise>
                    	</c:choose>
                    </td>
                    <td>${orders.payerName }</td>
                    <td>${orders.toBankNname }</td>
		            <td>
		            <c:choose>
                    	<c:when test="${orders.ordertype > 10 }">
                    		<c:if test="${orders.payStatus eq '1' }">待确认报名</c:if>
                    		<c:if test="${orders.payStatus eq '2' }">已确认报名</c:if>
                    	</c:when>
                    	<c:otherwise>${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}</c:otherwise>
                    </c:choose>
		            </td>
		            <c:choose>
		                <c:when test="${companyUUID eq '7a45838277a811e5bc1e000c29cf2586'}">
		                    <td class="tr">
				                <div class="yfje_dd">
									<span class="fbold"><c:if test="${not empty orders.totalMoney}">${fns:getMoneyAmountForDZ(orders.ordertype,orders.totalMoney,2)}</c:if></span>
								</div>
								<div class="dzje_dd">
					              	<span class="nopay_cen onshow">${fns:getNotPayMoney(orders.ordertype,orders.totalMoney,orders.payedMoney) }</span>
					                <span class="account_cen qtip"><c:if test="${not empty orders.accountedmoney}">${fns:getMoneyAmountForDZ(orders.ordertype,orders.accountedmoney,2)}</c:if></span>
					            </div>
				            </td>
		                </c:when>
		                <c:otherwise>
		                    <td class="tr">
				                <div class="yfje_dd">
					            	<span class="fbold"><c:if test="${not empty orders.totalMoney}">${fns:getMoneyAmountForDZ(orders.ordertype,orders.totalMoney,2)}</c:if></span>
								</div>
					            <div class="dzje_dd">
					                <span class="fbold"><c:if test="${not empty orders.accountedmoney}">${fns:getMoneyAmountForDZ(orders.ordertype,orders.accountedmoney,2)}</c:if></span>
					            </div>
				            </td>
		                </c:otherwise>
		            </c:choose>
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
		              		<span class="fbold"><c:if test="${not empty orders.payedMoney}">${fns:getMoneyAmountForDZ(orders.ordertype,orders.payedMoney,2)}</c:if></span>
                        </div>
                        <div class="dzje_dd">
			              	<span class="fbold"><c:if test="${orders.account eq '1'}">${fns:getMoneyAmountForDZ(orders.ordertype,orders.payedMoney,2)}</c:if></span>
                        </div>
		            </td>
		            <td>
		            <c:if test="${not empty orders.payPriceType }">
		            	<c:if test="${orders.payPriceType eq '10'}">
<%-- 		            		<a href="javascript:void(0)" onClick="javascript:saveAsAcount(${orders.id},1,${orders.ordertype},'${orders.serialNum}',this);">全款</a> --%>
		            		<c:choose>
		            			<c:when test="${orders.account eq '2'}">已驳回</c:when>
		            			<c:otherwise>
		            				<c:choose>
		            					<c:when test="${orders.ordertype > 10 }">
		            						<a href="javascript:void(0)" onClick="javascript:payedConfirm('${ctx}','${orders.payid}',${orders.agentinfo_id },${orders.ordertype},this);">收款</a>&nbsp;
		            						<a href="javascript:void(0)" onClick="javascript:payedConfirmReject('${ctx}',${orders.payid},${orders.id},${orders.ordertype},'1',${orders.payStatus },this);">驳回</a>
		            					</c:when>
		            					<c:otherwise>
		            						<a href="javascript:void(0)" onClick="javascript:payedConfirm('${ctx}',${orders.payid},${orders.agentinfo_id },${orders.ordertype},this);">全款</a>&nbsp;
		            						<a href="javascript:void(0)" onClick="javascript:payedConfirmReject('${ctx}',${orders.payid},${orders.id},${orders.ordertype},'0',${orders.payStatus },this);">驳回</a>
		            					</c:otherwise>
		            				</c:choose>
		            			</c:otherwise>
		            		</c:choose>
		            	</c:if>
		            	<c:if test="${orders.payPriceType eq '30'}">
<%-- 		            		<a href="javascript:void(0)" onClick="javascript:saveAsAcount(${orders.id},3,${orders.ordertype},'${orders.serialNum}',this);">订金</a> --%>
							<c:choose>
		            			<c:when test="${orders.account eq '2'}">已驳回</c:when>
		            			<c:otherwise>
		            				<a href="javascript:void(0)" onClick="javascript:payedConfirm('${ctx}',${orders.payid},${orders.agentinfo_id },${orders.ordertype},this);">订金</a>&nbsp;
		            				<a href="javascript:void(0)" onClick="javascript:payedConfirmReject('${ctx}',${orders.payid},${orders.id},${orders.ordertype},'0',${orders.payStatus },this);">驳回</a>
		            			</c:otherwise>
		            		</c:choose>
		            	</c:if>
		            	<c:if test="${orders.payPriceType eq '20'}">
<%-- 		            		<a href="javascript:void(0)" onClick="javascript:saveAsAcount(${orders.id},2,${orders.ordertype},'${orders.serialNum}',this);">尾款</a> --%>
							<c:choose>
		            			<c:when test="${orders.account eq '2'}">已驳回</c:when>
		            			<c:otherwise>
		            				<a href="javascript:void(0)" onClick="javascript:payedConfirm('${ctx}',${orders.payid},${orders.agentinfo_id },${orders.ordertype},this);">尾款</a>&nbsp;
		            				<a href="javascript:void(0)" onClick="javascript:payedConfirmReject('${ctx}',${orders.payid},${orders.id},${orders.ordertype},'0',${orders.payStatus },this);">驳回</a>
		            			</c:otherwise>
		            		</c:choose>
		            	</c:if>
		            	<c:if test="${orders.payPriceType eq '11'}">
		            		<c:choose>
		            			<c:when test="${orders.account eq '2'}">已驳回</c:when>
		            			<c:otherwise>
									<c:choose>
										<c:when test="${orders.ordertype > 10 }">已收<br>
											<shiro:hasPermission name="orderpay:operation:revoke">
												<a href="javascript:void(0)" onclick="javascript:newProductCancelConfirm('${orders.orderUuid}','${orders.payUuid}',${orders.payType},${orders.ordertype},this)">撤销</a>
											</shiro:hasPermission>
										</c:when>
										<c:otherwise>全款已收<br>
											<shiro:hasPermission name="orderpay:operation:revoke">
												<a href="javascript:void(0)" onclick="javascript:cancelConfirm(${orders.id},${orders.payid},1,${orders.ordertype},this)">撤销</a>
											</shiro:hasPermission>
										</c:otherwise>
									</c:choose>
		            			</c:otherwise>
		            		</c:choose>
		            	</c:if>
		            	<c:if test="${orders.payPriceType eq '31'}">
		            		<c:choose>
		            			<c:when test="${orders.account eq '2'}">已驳回</c:when>
		            			<c:otherwise>订金已收<br>
		            				<shiro:hasPermission name="orderpay:operation:revoke">
		            					<a href="javascript:void(0)" onclick="javascript:cancelConfirm(${orders.id},${orders.payid},1,${orders.ordertype},this)">撤销</a>
		            				</shiro:hasPermission>
		            			</c:otherwise>
		            		</c:choose>
		            	</c:if>
		            	<c:if test="${orders.payPriceType eq '21'}">
		            		<c:choose>
		            			<c:when test="${orders.account eq '2'}">已驳回</c:when>
		            			<c:otherwise>尾款已收<br>
		            				<shiro:hasPermission name="orderpay:operation:revoke">
		            					<a href="javascript:void(0)" onclick="javascript:cancelConfirm(${orders.id},${orders.payid},1,${orders.ordertype},this)">撤销</a>
		            				</shiro:hasPermission>
		            			</c:otherwise>
		            		</c:choose>
		            	</c:if>
		            </c:if>
		            </td>
		            <td class="invoice_yes" id="print_${orders.payid}_${orders.ordertype}">
		            	<c:if test="${empty orders.printFlag || orders.printFlag == 0 }">未打印</c:if>
		            	<c:if test="${orders.printFlag == 1 }"><p class="uiPrint">已打印<span style="display: none;">首次打印时间<br>
		            		<fmt:formatDate value="${orders.printTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
						</c:if>
		            </td>
		            <td> 
		                <c:choose>
		                    <c:when test="${orders.ordertype eq '11' }">
		                     <!--酒店查看订单详情-->
		                       <a target="_blank" href="${ctx }/hotelOrder/hotelOrderDetail/${orders.orderUuid}">查看详情 </a>
		                    </c:when>
		                    <c:when test="${orders.ordertype eq '12' }">
		                       <!--海岛游查看订单详情-->
		                       <a target="_blank" href="${ctx }/islandOrder/islandOrderDetail/${orders.orderUuid}">查看详情 </a>
		                    </c:when>
		                    <c:otherwise>
		                       <a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id},${orders.ordertype });">查看详情 </a>
		                    </c:otherwise>
		                </c:choose>
                    	<a target="_blank" href="${ctx}/orderCommon/manage/print/${orders.payid}/${orders.ordertype}">打印</a>
                    	<%--  <a href="javascript:void(0)" onclick="javascript:printForm('${ctx}','${orders.payid}','${orders.ordertype}');">打印</a> --%>
                    </td>
		        </tr>
            </c:forEach>
        </tbody>
    </table>
    <%--<div id="contentTable_foot">--%>
        <%--<label><input check-action="All" type="checkbox"> 全选</label>--%>
        <%--<label><input check-action="Reverse" type="checkbox"> 反选</label>--%>
        <%--<input type="button" class="btn ydbz_x" onclick="batchPrint();" value="批量打印">--%>
    <%--</div>--%>


    <div id="batchPrintDiv">
    	<%-- ${ctx }/orderCommon/manage/batchPrint --%>
    	<form action="${ctx }/orderCommon/manage/batchPrint" id="batchPrintForm" target="_blank" method="post">
    		<input type="hidden" value="" id="payids" name="payids" />
    		<input type="hidden" value="" id="ordertypes" name="ordertypes" />
    	</form>
    </div>



<%--<div class="pagination clearFix">${page}</div>--%>

    <div class="pagination" id="contentTable_foot">
        <dl>
            <dt>
                <input check-action="All" type="checkbox">全选
            </dt>
            <dt>
                <input check-action="Reverse" type="checkbox">反选
            </dt>
            <dd>
                <input type="button" class="btn ydbz_x" onclick="batchPrint();" value="批量打印">
            </dd>
        </dl>
        <div class="pagination clearFix"><ul>
            ${page}
        </ul>
            <div style="clear:both;"></div></div>
        <div style="clear: both;"></div>
    </div>

</div>
</body>
</html>