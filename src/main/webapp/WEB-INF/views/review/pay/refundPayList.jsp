<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>退款付款</title>
<meta name="decorator" content="wholesaler" />
<link href="${ctxStatic}/css/Remark_New.css" type="text/css" rel="stylesheet"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/cost/finance_common.js" type="text/javascript"></script>
<script type="text/javascript">
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i=0;i<searchFormInput.length;i++) {
		var inputValue = $(searchFormInput[i]).val();
		if(inputValue != "" && inputValue != null&& inputValue != "-99999") {
			inputRequest = true;
			break;
		}
	}
	for(var i=0;i<searchFormselect.length;i++) {
		var selectValue = $(searchFormselect[i]).children("option:selected").val();
		if(selectValue != "" && selectValue != null && selectValue != 0 && selectValue != "-99999") {
			selectRequest = true;
			break;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
}

// 548,549需求
$(function () {
	$('.remark_div_child').niceScroll({
		cursorcolor: "#ccc",//#CC0071 光标颜色
		cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
		touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
		cursorwidth: "5px", //像素光标的宽度
		cursorborder: "0", //     游标边框css定义
		cursorborderradius: "5px",//以像素为光标边界半径
		autohidemode: false //是否隐藏滚动条
	});
	$(".remark_548").mouseover(function () {
		$(this).children().show();
		$(".remark_div_child").getNiceScroll().resize();
	});
	$(".remark_548").mouseleave( function () {
		$(this).children().hide();
		if ($(this).find("input").is(':checked')) {
			var reviewUuid = $(this).find("input")[0].value;
			$(this).remove();
			$.ajax({
				type:"post",
				url:"${ctx}/review/common/web/cancelShowRemark",
				data:{reviewUuid:reviewUuid}
			});
		}
	})
});

function printRefund(productType, agentId, orderId, reviewId, groupCode, nFlag , payId, option){
	$("input[name='productType']").val(productType);
	$("input[name='agentId']").val(agentId);
	$("input[name='orderId']").val(orderId);
	$("input[name='reviewId']").val(reviewId);
	$("input[name='groupCodePrint']").val(groupCode);
	$("input[name='payId']").val(payId);
	$("input[name='option']").val(option);
	if( 1 == nFlag ){
		$("#refundRevPrintRefundForm")[0].action = "${ctx}/refundReview/refundReviewInfo";
	} else {
		$("#refundRevPrintRefundForm")[0].action = "${ctx}/refundnew/refundReviewInfo";
	}
	$("#refundRevPrintRefundForm").submit();
};

$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	//计调模糊匹配
	//$("[name=jd]").comboboxSingle();
// 	$("#orderS" ).comboboxInquiry();
	$("#agentIdS" ).comboboxInquiry();
	$("#creatorrefund" ).comboboxInquiry();
	$("#salerrefund" ).comboboxInquiry();
	$("#jdsalerS" ).comboboxInquiry();
	$("#fromBank").comboboxInquiry();

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
        
        var _$orderBy = $("#orderBy").val();
        if(_$orderBy==""){
            _$orderBy="updatetime DESC";
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
function deleteOrder(orderId){
        $.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f){
            if (v == 'ok') {
                $.ajax({
                type: "POST",
                url: "${ctx}/orderCommon/manage/deleteOrder",
                data: {
                    orderId:orderId
                },
                success: function(msg){
                    top.$.jBox.tip('删除成功','warning');
                /*  top.$('.jbox-body .jbox-icon').css('top','55px');*/
                    location.reload();
                }
             });
            }else if (v == 'cancel'){
                
            }
    });
}

function payPal(id,_this){
	   var $this = $(_this);
		$.jBox.confirm("确认付款成功了吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderCommon/manage/payPal",
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
/*
 * 付款确认页面
 */
function paymentConfirm(id) {
	location.href = "${ctx}/cost/manager/paymentConfirm/" + id;
}

function changeGroup(orderId){
    window.open("${ctx}/orderCommon/manage/changGroup/"+orderId,"_blank");
}

function orderDetail(orderId){
    window.open("${ctx}/orderCommon/manage/orderDetail/"+orderId,"_blank");
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
  //展开收起搜索条件
	//操作浮框
	operateHandler();	 
	//输入金额提示
	inputTips();
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

// 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22
function exportExcel() {
    $('#searchForm').attr('action', '${ctx}/costNew/payManager/getRefundPayListExcel/201')
    $('#searchForm').submit();
    $('#searchForm').attr('action', '${ctx}/costNew/payManager/payList/201')
}

Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  

function expand(child,obj,id,type,orderType,agentId,orderId,revId,groupCode,reviewFlag) {
	$.ajax({
		url:"${ctx}/orderCommon/manage/refundPayInfo/",
 		type:"POST",
        data:{id:id,type:type,orderType:orderType},
        success:function(data){
			var htmlstr=""
            var num = data.length;
            if(num>0){
				var str1='';
                for(var i =0;i<num;i++){
					var str = data[i].payvoucher.split("|")
                    var index = str.length;
                    if(index>0){
						for(var a=0;a<index;a++){
							str1+=str[a]+"<br/>"
						}
                    }
					if (data[i].label == null){ <%-- 拉美图的因公支付宝结算方式暂未保存到字典表中。后期如做调整可删除 --%>
						data[i].label = "因公支付宝";
					}
                    htmlstr+="<tr><td class='tc'>"+data[i].label+"</td><td class='tc'>"+data[i].amount+"</td><td class='tc'>"+(new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss")+"</td><td class='tc'>"+data[i].a+
                    "</td><td class='tc'>"+str1+"</td><td class='tc'>"+data[i].opstatus+"</td><td class='tc'>";

					if(data[i].opstatus=='已支付'){
						if(orderType=='11' || orderType=='12') {  //酒店和海岛游
							htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>"+
							"&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\" >撤销</a></td></tr>"
						}else{
							htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>"+
							"&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\" >撤销</a>"+
							"&nbsp;&nbsp;<a onClick=\"printRefund('"+orderType+"','"+agentId+"','"+orderId+"','"+revId+"','"+groupCode+"','"+reviewFlag+"','"+data[i].id+"', 'pay')\">打印</a></td></tr>"
						}
						
                   	}else{  //已撤销后不显示打印凭单
						htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;</td></tr>"                     
                   }                   
                   str1='';
				}
			}
            $("#rpi_"+id).html(htmlstr);
		}
	});
	if ($(child).is(":hidden")) {
		$(obj).html("收起");
		$(obj).parents("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	} else {
		if (!$(child).is(":hidden")) {
			$(obj).parents("tr").removeClass("tr-hover");
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("支付记录");
		}
	}
}

function viewRefundInfo(ctx,str){	  
	$.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=view",{
		title: "付款记录",width:830,height: 500,buttons:{'关闭': 0},
		persistent:true,
	   	loaded: function(h){},
	   	submit:function(v,h,f){}
	})
}

function cancelOper(ctx,str,orderType,obj){
	$.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&payType=2&orderType="+orderType,{
		title: "付款记录",
		width:830,
	   	height: 500,
	   	buttons:{'撤销': 1,'关闭':0},
	   	persistent:true,
	   	loaded: function(h){},
	   	submit:function(v,h,f){
			if(v==1){
				var refundId =  $(h.find("iframe")[0].contentWindow.refundId).val();
	   		    var recordId =  $(h.find("iframe")[0].contentWindow.recordId).val();
	   		    $.ajax({
					type:"GET",
	   		        url:ctx+"/refund/manager/undoRefundPayInfo",
	   		        dataType:"json",
	   		        data:{refundId:refundId,recordId:recordId},
	   		        success:function(data){
						if(data.flag=='ok'){
							$('#searchForm').submit();
// 	   		                $('#child1_').hide();
// 	   		                $('#child1_' + data.recordId).show();
// 	   		                $("#click_fun").click();
// 	   		                $("#click_fun").click();
						}
					}
				})
			}
		}
	}).find("#jbox-content").css("overflow", "hidden");
}
	
function cancelDetailOper(ctx,refundId,recordId,obj){
	$.ajax({
		type:"GET",
        url:ctx+"/refund/manager/undoRefundPayInfo",
        dataType:"json",
        data:{refundId:refundId,recordId:recordId},
        success:function(data){
			if(data.flag=='ok'){
				$(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
			}
		}
	});
}
	
/**
 * 查询条件重置
 * 
 */
var resetSearchParams = function(){
//     $('#groupCode').val('');
//     $('select[name=prdType]').val('');
//     $('select[name=agentId]').val('');
//     $('select[name=saler]').val('');
//     $('select[name=payStatus]').val('');
//     $('select[name=currencyid]').val('');
//     $('#startMoney').val('');
//     $('#endMoney').val('');
//     $('select[name=jdsaler]').val('');
	var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
	var selectArray = $('#searchForm').find("select");
	for(var i=0;i<inputArray.length;i++){
		if($(inputArray[i]).val()){
			$(inputArray[i]).val('不限');
		}
 		if($(inputArray[i]).parent().prev().prev().text()=="渠道选择："){
 			$(inputArray[i]).val('全部');
 		}
		if($(inputArray[i]).parent().prev().prev().text()=="来款银行："){
			$(inputArray[i]).val('请选择');
		}
	}
	
	$("#startMoney").val("");
	$("#endMoney").val("");
	$("#cashierConfirmDateBegin").val("");
	$("#cashierConfirmDateEnd").val("");
	for(var i=0;i<selectArray.length;i++){
		var selectOption = $(selectArray[i]).children("option");
		$(selectOption[0]).attr("selected","selected");
	}
	$("input[name='createTimeMin']").val("");
	$("input[name='createTimeMax']").val("");
}

//批量付款的处理
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
})

//批量打印查询
function batchPrint(){
	var boxs = $("input[name = 'checkBox']:checked");
	if(boxs.length == 0){
		top.$.jBox.tip("请选择数据！");
		return;
	}
	var datas = [];
	for(var i = 0 ; i < boxs.length ; i ++){
		var box = $(boxs[i]);
		var data = {};
		data.productType = box.parent().find("input[name = 'productType']").val();
		data.agentId = box.parent().find("input[name = 'agentId']").val();
		data.orderId = box.parent().find("input[name='orderId']").val();
		data.reviewId = box.parent().find("input[name='reviewId']").val();
		data.payId = box.parent().find("input[name='payId']").val();
		data.option = box.parent().find("input[name='option']").val();
		data.groupCodePrint = box.parent().find("input[name='groupCodePrint']").val();
		data.nFlag = box.parent().find("input[name='nFlag']").val();
		datas.push(data);
	}
	$("#params").val(JSON.stringify(datas));
	$("#printForm").submit();
	//window.open('${ctx}/refundnew/batchRefundReviewInfo?params='+JSON.stringify(datas));
}
</script>

<style type="text/css">
a {
	display: inline-block;
}

label {
	cursor: inherit;
}
</style>

</head>
<body>
	<form action="${ctx}/refundnew/batchRefundReviewInfo" id = "printForm" method="post" target="_blank">
		<input name = "params" value ="" type="hidden" id="params">
	</form>
	<%--<page:applyDecorator name="finance_op_head">--%>
		<%--<page:param name="showType">${payType }</page:param>--%>
	<%--</page:applyDecorator>--%>
	<c:set var="showType" value="${payType }" />
	<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
	<div class="activitylist_bodyer_right_team_co_bgcolor"
		style="float:left;width: 100%">
		<form:form id="searchForm" modelAttribute="travelActivity"
			action="${ctx}/costNew/payManager/payList/201"
			method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" />
			<input id="orderBy" name="orderBy" type="hidden"
				value="${page.orderBy}" />
			<!--<div class="order_bill"></div>-->
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2">
					<input id="groupCode" name="groupCode"
						class="txtPro searchInput inputTxt" value="${params.groupCode }" placeholder="请输入团号" />
				</div>
				<div class="zksx">筛选</div>
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" type="button"
						onclick="query()" value="搜索" /> 
					<input
						class="btn ydbz_x" type="button"
						onclick="resetSearchParams()" value="清空所有条件" />
					<!-- 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22 -->	
					<input class="btn ydbz_x" type="button" onclick="exportExcel()" value="导出Excel" />
				</div>
				<div class="ydxbd">
					<span></span>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">团队类型：</div>
						<div class="selectStyle">
							<select name="prdType" id="orderS">
								<c:forEach var="order" items="${dicts}">
									<option value="${order.value }"
										<c:if test="${params.prdType==order.value}">selected="selected"</c:if>>${order.label
										}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">渠道选择：</label> 
						<select name="agentId" style="width:278px;" id="agentIdS">
							<option value="">全部</option>
							<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
							<c:choose>
							   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
							       <option value="-1" <c:if test="${params.agentId==-1}">selected="selected"</c:if>>直客</option>
							   </c:when>
							   <c:otherwise>
							      <option value="-1" <c:if test="${params.agentId==-1}">selected="selected"</c:if>>非签约渠道</option>
							   </c:otherwise>
							</c:choose> 
							<c:forEach var="s" items="${agentinfoList}">
								<c:choose>
							   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && s.id==-1}"> 
							      <option value="${s.id}"
									<c:if test="${params.agentId==s.id}">selected="selected"</c:if>>直客</option>
							   </c:when>
							   <c:otherwise>
							      <option value="${s.id}"
									<c:if test="${params.agentId==s.id}">selected="selected"</c:if>>${s.agentName}</option>
							   </c:otherwise>
							</c:choose> 
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">下单人：</div>
						<select class="sel-w1" style='height : 28px' name="creator" id="creatorrefund">
							<option value="-99999" selected="selected">不限</option>
							<c:forEach items="${salerList }" var="creator">
								<option value="${creator.id }"
									<c:if test="${params.creator==creator.id }">selected="selected"</c:if>>${creator.name	}
								</option>
							</c:forEach>
						</select>
					</div>
					<c:if test="${params.prdType ne 11 and params.prdType ne 12 }">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">销售：</div>
						<select class="sel-w1" style='height : 28px' name="saler"
							id="salerrefund">
							<option value="-99999" selected="selected">不限</option>
							<c:forEach items="${salerList }" var="userinfo">
								<!-- 用户类型  1 代表销售 -->
								<option value="${userinfo.id }"
									<c:if test="${params.saler==userinfo.id }">selected="selected"</c:if>>${userinfo.name }
								</option>
							</c:forEach>
						</select>
					</div>
					</c:if>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">付款状态：</div>
						<div class="selectStyle">
							<select name="payStatus">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${params.payStatus eq '0'}">selected="selected"</c:if>>未付款</option>
								<option value="1"
									<c:if test="${params.payStatus eq '1'}">selected="selected"</c:if>>已付款</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co4">
						<div class="activitylist_team_co3_text">付款金额：</div>
						<div class="selectStyle">
							<select id="currency" name="currencyid">
								<option value="">币种选择</option>
								<c:forEach items="${fns:findCurrencyList()}" var="c">
									<option value="${c.id}"
										<c:if test="${params.currencyid==c.id}">selected="selected"</c:if>>${c.currencyName}</option>
								</c:forEach>
							</select>
						</div>
						<input type="text" value="${params.cAmountStart }" name="cAmountStart"
							id="startMoney" class="inputTxt" onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额" /><%--bug17483 去掉flag="istips"--%>
						<span> ~ </span>
						<input type="text" value="${params.cAmountEnd }" name="cAmountEnd" id="endMoney"
							class="inputTxt" onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额" /><%--bug17483 去掉flag="istips"--%>

					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">计调：</div>
						<select name="jdsaler" id = "jdsalerS">
							<option value="" selected="selected">不限</option>
							<c:forEach items="${jdList }" var="userinfo">
								<!-- 用户类型  3 代表计调 -->
								<option value="${userinfo.id }"
									<c:if test="${params.jdsaler==userinfo.id }">selected="selected"</c:if>>${userinfo.name}
								</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
					   <div class="activitylist_team_co3_text">打印状态：</div>
						<div class="selectStyle">
							<select name="printStatus">
								<option value="">全部</option>
								<option value="0" <c:if test="${params.printStatus eq '0'}">selected="selected"</c:if>>未打印</option>
								<option value="1" <c:if test="${params.printStatus eq '1'}">selected="selected"</c:if>>已打印</option>
						    </select>
						</div>
				    </div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">款项：</div>
						<div class="selectStyle">
							<select name="payType2">
								<option selected="selected" value="0">全部</option>
								<option value="7" <c:if test="${params.payType2 eq '7'}">selected="selected"</c:if>>退签证押金</option>
								<option value="1" <c:if test="${params.payType2 eq '1'}">selected="selected"</c:if>>退款</option>
							</select>
						</div>
					</div>
					<%-- 0409需求，新增以下3个筛选条件，yudong.xu 2016.5.18 --%>
					<div class="activitylist_bodyer_right_team_co1">
						<label  class="activitylist_team_co3_text">出纳确认时间：</label>
						<input id="cashierConfirmDateBegin" class="inputTxt dateinput" name="cashierConfirmDateBegin"
							   value='${params.cashierConfirmDateBegin }' onfocus="WdatePicker()" readonly />
						<span>至</span>
						<input id="cashierConfirmDateEnd" class="inputTxt dateinput" name="cashierConfirmDateEnd"
							   value='${params.cashierConfirmDateEnd }' onClick="WdatePicker()" readonly />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">支付方式：</div>
						<div class="selectStyle">
							<select name="payMode">
								<option selected="selected" value="">请选择</option>
								<option value="1" <c:if test="${params.payMode eq '1'}">selected="selected"</c:if>>支票</option>
								<option value="3" <c:if test="${params.payMode eq '3'}">selected="selected"</c:if>>现金支付</option>
								<option value="4" <c:if test="${params.payMode eq '4'}">selected="selected"</c:if>>汇款</option>
								<option value="6" <c:if test="${params.payMode eq '6'}">selected="selected"</c:if>>银行转账</option>
								<option value="7" <c:if test="${params.payMode eq '7'}">selected="selected"</c:if>>汇票</option>
								<option value="8" <c:if test="${params.payMode eq '8'}">selected="selected"</c:if>>POS机刷卡</option>
								<c:if test="${isLMT }">
									<option value="9" <c:if test="${params.payMode eq '9'}">selected="selected"</c:if>>因公支付宝</option>
								</c:if>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">来款银行：</label>
						<select name="fromBank" id="fromBank">
							<option value="" selected="selected">请选择</option>
							<c:forEach var="fromBank" items="${fromBanks }">
								<option value="${fromBank.bankName}" <c:if test="${params.fromBank==fromBank.bankName}">selected="selected"</c:if>>${fromBank.bankName}</option>
							</c:forEach>
						</select>
					</div>
					<%-- <div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
					<select name="paymentType">
						<option value="">不限</option>
						<c:forEach var="pType" items="${fns:findAllPaymentType()}">
							<option value="${pType[0] }"
								<c:if test="${params.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
						</c:forEach>
					</select>
				</div> --%>
				<div class="activitylist_bodyer_right_team_co1">
                     <label class="activitylist_team_co3_text">申请日期：</label>
                     <input class="inputTxt dateinput" name="createTimeMin" value="${createTimeMin }" onclick="WdatePicker()"
                            readonly="readonly"/>
                     <span> 至 </span>
                     <input class="inputTxt dateinput" name="createTimeMax" value="${createTimeMax }" onclick="WdatePicker()"
                            readonly="readonly"/>
                 </div>
				</div>
			</div>


	</form:form>

	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li class="activitylist_paixu_left_biankuang licreatetime"><a
						onClick="sortby('createtime',this)">创建时间</a>
					</li>
					<li class="activitylist_paixu_left_biankuang liupdatetime"><a
						onClick="sortby('updatetime',this)">更新时间</a>
					</li>
				</ul>
			</div>
			<div class="activitylist_paixu_right">
				查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
			</div>
		</div>
	</div>

	<table id="contentTable" class="table mainTable activitylist_bodyer_table" style="margin-left:0px;">
	    <!--  需求0307 起航假期  王洋   -->
		<thead style="background:#403738;">
			<tr>
				<th colspan="2" width="6%">序号</th>
				<th width="8%">申请日期</th>
				<th width="5%">团队类型</th>
				<c:if test="${isHQX}">
					<th width="7%">订单团号</th>
				</c:if>
				<c:if test="${!isHQX}">
					<th width="7%">团号</th>
				</c:if>
				<th width="7%">团队名称</th>
				<th width="10%">渠道商</th>
				<th width="8%">款项</th>
				<th width="10%">付款金额<br>已付金额</th>
				<th width="7%">计调</th>
				<th width="7%">
					<c:choose>
						<c:when test="${isQHJQ }">
							<div>销售</div>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${params.prdType eq 11 or params.prdType eq 12 }">下单人</c:when>
								<c:otherwise>
									<span class="tuanhao on">销售</span>/
									<span class="chanpin">下单人</span>
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>
				</th>
				<th width="4%">出纳确认</th>
				<th width="7%">出纳确认时间</th>
				<th width="7%">打印确认</th>
				<c:if test="${companyUuid eq '75895555346a4db9a96ba9237eae96a5'}">
					<th width="7%">发票状态</th>
				</c:if>
				<th width="4%">操作</th>
			</tr>
		</thead>
		<tbody>

			<c:if test="${fn:length(page.list) <= 0 }">
				<tr>
					<td colspan="15" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>


			<c:forEach items="${page.list }" var="orders" varStatus="s">
				<tr>
					<td class="tc" >
						<input type="checkbox" name="checkBox" value="${orders.revid }_${orders.prdtype}_${orders.reviewflag}_${orders.payStatus}" check-action="Normal" /> 
					 	<input name = "productType"  value = "${orders.prdtype }" type="hidden">
						<input name = "agentId"  value = "${orders.agentid }" type="hidden">
						<input name = "orderId"  value = "${orders.orderid }" type="hidden">
						<input name = "reviewId"  value = "${orders.revid }" type="hidden">
						<input name = "payId"  value = "" type="hidden">
						<input name = "option"  value = "${orders.prdtype }" type="hidden">
						<input name = "groupCodePrint"  value = "${orders.groupcode }" type="hidden">
						<input name = "nFlag"  value = "${orders.reviewflag}" type="hidden">
					</td>
					<td>${s.count }</td>
					<td><fmt:formatDate value="${orders.createtime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<!-- 报批日期 -->
					<td>${fns:getDictLabel(orders.prdtype,'order_type','')}</td>
					<td>${orders.groupcode }</td>
					<c:if test="${orders.prdtype !=11 && orders.prdtype !=12}">
						<td>${fns:getProductName(orders.chanpid, orders.prdtype)}</td>
					</c:if>
					<c:if test="${orders.prdtype ==11 || orders.prdtype ==12}">
						<td>${orders.chanpname}</td>
					</c:if>
					<!-- 目前放的是产品的id -->
					<td>
						 <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
						<c:choose>
						   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && fns:getAgentName(orders.agentid) eq '非签约渠道'}"> 
						       直客
						   </c:when>
						   <c:otherwise>
						       ${fns:getAgentName(orders.agentid)}
						 </c:otherwise>
						</c:choose>
						
					</td>
					<td>
						<c:if test="${orders.flowtype == 1}">${orders.refundName }</c:if>
						<c:if test="${orders.flowtype == 7}">退签证押金</c:if>
					</td>
					<td class="p0 tr">
						<c:choose>
							<c:when test="${orders.payType == 1}"><p><span class="icon-mode-of-payment-check"></span></p></c:when><%--支票--%>
							<c:when test="${orders.payType == 3 or orders.payType==5}"><p><span class="icon-mode-of-payment-cash"> </span></p></c:when><%--现金--%>
							<c:when test="${orders.payType == 4 }"><p><span class="icon-mode-of-payment-remit"> </span></p></c:when><%--汇款--%>
							<c:when test="${orders.payType == 6}"><p><span class="icon-mode-of-payment-transfer"> </span></p></c:when><%--转账--%>
							<c:when test="${orders.payType == 7}"><p><span class="icon-mode-of-payment-bill"> </span></p></c:when><%--汇票--%>
							<c:when test="${orders.payType == 8 or orders.payType==2}"><span class="icon-mode-of-payment-pos"> </span></p></c:when><%--POS--%>
						</c:choose>
						<div class="yfje_dd">
							<span class="fbold">${fns:getCurrencyNameOrFlag(orders.mcurid,'0')}${orders.mamount}</span>/<span style="color:#000">${orders.rate}</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">
								<c:if test="${orders.reviewflag == 2}">
									<c:if test="${orders.flowtype == 1}">${fns:getRefundPayedMoney(orders.idlong, '8',orders.prdtype)}</c:if>
									<c:if test="${orders.flowtype == 7}">${fns:getRefundPayedMoney(orders.idlong, '11',orders.prdtype)}</c:if>
								</c:if>
								<c:if test="${orders.reviewflag == 1}">
									<c:if test="${orders.flowtype == 1}">${fns:getRefundPayedMoney(orders.revid, '2',orders.prdtype)}</c:if>
									<c:if test="${orders.flowtype == 7}">${fns:getRefundPayedMoney(orders.revid, '5',orders.prdtype)}</c:if>
								</c:if>
							</span>
						</div></td>
					<td>${fns:getUserNameById(orders.jidcreateby)}</td>
					<td>
						<c:choose>
							<c:when test="${isQHJQ }">
								<div title="${orders.salerName }">${orders.salerName }</div>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${params.prdType eq 11 or params.prdType eq 12 }">${fns:getUserNameById(orders.salecreateby )}</c:when>
									<c:otherwise>
										<div class="tuanhao_cen onshow" title="${orders.salerName }">${orders.salerName }</div>
										<div class="chanpin_cen qtip" title="${fns:getUserNameById(orders.salecreateby )}">${fns:getUserNameById(orders.salecreateby )}</div>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</td>
					<td>
					<c:if test="${orders.payStatus ne '1' }">
						<font style="color:red" class="noPay">未付款</font>
					</c:if> <c:if test="${orders.payStatus eq '1' }">
						<font style="color:green" class="noPay">已付款</font>
					</c:if>
					<c:if test="${not empty orders.reviewLogs}">
						<c:set var="review" value="${fns:getReviewNewByUuid(orders.revid)}" />
						<c:if test="${empty review.extend4 or review.extend4 eq '0'}">
						<div class="relative div_548">
							<div class="remark_548">备注
								<div class="remark_div">
									<em class="arrows_548"></em>
									<div class="remark_div_head">
										<div class="remark_head_left">审批备注</div>
										<div class="remark_head_right">
											<input type="checkbox" value="${orders.revid}"/>
											<div>不再显示</div>
										</div>
									</div>
									<div class="remark_div_body">
										<div class="remark_div_child">
											<c:forEach var="reviewLog" items="${orders.reviewLogs}" varStatus="status">
												<c:choose>
													<%-- 驳回状态 --%>
													<c:when test="${reviewLog.operation eq 2}">
														<ul>
															<li class="left_548">
																<div class="color_black">
																	<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																</div>
																<div class="green_548">${reviewLog.operationDescription}</div>
															</li>
															<li class="center_548"><em class="point-time stop"></em></li>
															<li class="right_548">
																<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																<div class="right_body">
																	<div class="remarks">备注：</div>${reviewLog.remark}
																</div>
															</li>
														</ul>
														<ul>
															<li class="left_548">
																<div class="color_black"></div>
																<div class="green_548">审批驳回</div>
															</li>
															<li class="center_548"><em class="point-time end"></em></li>
															<li class="right_548">
																<div class="right_head"></div>
															</li>
														</ul>
													</c:when>
													<c:otherwise>
														<c:choose>
															<c:when test="${(review.status eq 2) and status.last}">
																<ul>
																	<li class="left_548">
																		<div class="color_black">
																			<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																		</div>
																		<div class="green_548">${reviewLog.operationDescription}</div>
																	</li>
																	<li class="center_548"><em class="point-time step_yes"></em></li>
																	<li class="right_548">
																		<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																		( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																		<div class="right_body">
																			<div class="remarks">备注：</div>${reviewLog.remark}
																		</div>
																	</li>
																</ul>
																<ul>
																	<li class="left_548">
																		<div class="color_black"></div>
																		<div class="green_548">审批通过</div>
																	</li>
																	<li class="center_548"><em class="point-time yes"></em></li>
																	<li class="right_548">
																		<div class="right_head"></div>
																	</li>
																</ul>
															</c:when>
															<c:when test="${(review.status eq 1) and status.last}">
																<ul>
																	<li class="left_548">
																		<div class="color_black">
																			<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																		</div>
																		<div class="green_548">${reviewLog.operationDescription}</div>
																	</li>
																	<li class="center_548"><em class="point-time step_yes"></em></li>
																	<li class="right_548">
																		<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																		( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																		<div class="right_body">
																			<div class="remarks">备注：</div>${reviewLog.remark}
																		</div>
																	</li>
																</ul>
																<ul>
																	<li class="left_548">
																		<div class="color_black"></div>
																		<div class="green_548"></div>
																	</li>
																	<li class="center_548"><em class="point-time next"></em></li>
																	<li class="right_548">
																		<div class="right_head"></div>
																	</li>
																</ul>
															</c:when>
															<c:otherwise>
																<ul>
																	<li class="left_548">
																		<div class="color_black">
																			<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																		</div>
																		<div class="green_548">${reviewLog.operationDescription}</div>
																	</li>
																	<li class="center_548"><em class="point-time step_yes"></em></li>
																	<li class="right_548">
																		<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																		( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																		<div class="right_body">
																			<div class="remarks">备注：</div>${reviewLog.remark}
																		</div>
																	</li>
																</ul>
															</c:otherwise>
														</c:choose>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>
						</div>
						</c:if>
					</c:if>

					</td>
					<td><%--出纳确认时间,未确认或取消确认的不显示 yudong.xu--%>
						<c:if test="${orders.payStatus eq '1' }">
							<fmt:formatDate value="${orders.payConfirmDate }" pattern="yyyy-MM-dd"/>
						</c:if>
					</td>
					<td class="invoice_yes">
					<c:if test="${empty orders.printFlag || orders.printFlag == 0 }">未打印</c:if>
					<c:if test="${orders.printFlag == 1 }"><p class="uiPrint">已打印<span style="display: none;">首次打印时间<br><fmt:formatDate value="${orders.printTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p></c:if>
					</td>
					<c:if test="${companyUuid eq '75895555346a4db9a96ba9237eae96a5' }">
						<td class="invoice_yes">
							<c:if test="${orders.invoiceStatus == 0 }">未收</c:if>
							<c:if test="${orders.invoiceStatus == 1 }">已收</c:if>
						</td>
					</c:if>
					<td class="p0">
						<dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png">
							</dt>
							<dd>
								<p>
									<span></span>
										<c:if test="${orders.reviewflag == 2}">
											<a href="${ctx}/costNew/payManager/pay/201?orderType=${orders.prdtype}&orderId=${orders.orderid}&travelerId=${orders.travelerid}&currencyId=${orders.mcurid}&payPrice=${orders.mamount}&reviewId=${orders.revid}&agentId=${orders.agentid}&flowType=${orders.flowtype}" target="_blank">付款 </a>
										</c:if>
										<c:if test="${orders.reviewflag == 1}">
											<a href="${ctx}/cost/payManager/pay/201?orderType=${orders.prdtype}&orderId=${orders.orderid}&travelerId=${orders.travelerid}&currencyId=${orders.mcurid}&payPrice=${orders.mamount}&reviewId=${orders.revid}&agentId=${orders.agentid}&flowType=${orders.flowtype}" target="_blank">付款 </a>
										</c:if>
										<c:if test="${orders.reviewflag == 2}">
											<c:if test = "${orders.flowtype == 1}">
												<a onclick="expand('#child1_${orders.idlong}',this,${orders.idlong},'8','${orders.prdtype}','${orders.agentid}','${orders.orderid}','${orders.revid}','${orders.groupcode}','${orders.reviewflag}')"
													href="javascript:void(0)">支付记录</a>  <!-- '${fns:getCurrencyNameOrFlag(orders.mcurid,'0')}${orders.mamount}' -->
											</c:if>
											<c:if test = "${orders.flowtype == 7}">
												<a onclick="expand('#child1_${orders.idlong}',this,${orders.idlong},'11','${orders.prdtype}','${orders.agentid}','${orders.orderid}','${orders.revid}','${orders.groupcode}','${orders.reviewflag}')"
													href="javascript:void(0)">支付记录</a>
											</c:if>
										</c:if>
										<c:if test="${orders.reviewflag == 1}">
											<c:if test = "${orders.flowtype == 1}">
												<a onclick="expand('#child1_${orders.revid}',this,${orders.revid},'2','${orders.prdtype}','${orders.agentid}','${orders.orderid}','${orders.revid}','${orders.groupcode}','${orders.reviewflag}')"
													href="javascript:void(0)">支付记录</a>
											</c:if>
											<c:if test = "${orders.flowtype == 7}">
												<a onclick="expand('#child1_${orders.revid}',this,${orders.revid},'5','${orders.prdtype}','${orders.agentid}','${orders.orderid}','${orders.revid}','${orders.groupcode}','${orders.reviewflag}')"
													href="javascript:void(0)">支付记录</a>
											</c:if>
										</c:if>
										<c:if test="${orders.reviewflag eq 1 }">
											<c:if test = "${orders.flowtype == 1}">
												<c:if test="${orders.prdtype == 6}">
					                            	<a href="${ctx }/cost/review/visaRead/${orders.chanpid}/0?menuid=2" target="_blank">查看</a>
					                            </c:if>
					                            <c:if test="${orders.prdtype == 7}">
					                            	<a target="_blank" href="${ctx}/cost/review/airTicketRead/${orders.chanpid}/0?menuid=2">查看</a>
					                            </c:if>
			
					                            <c:if test="${orders.prdtype == 11}">
					                            	<a target="_blank" href="${ctx}/cost/review/hotelRead/${orders.chanpid}/${orders.groupid}/11?from=operatorPre&menuid=2">查看</a>
					                            </c:if>
					                            <c:if test="${orders.prdtype == 12}">
					                            	<a target="_blank" href="${ctx}/cost/review/islandRead/${orders.chanpid}/${orders.groupid}/12?from=operatorPre&menuid=2">查看</a>
					                            </c:if>
					                            <c:if test="${orders.prdtype !=6 && orders.prdtype !=7 && orders.prdtype !=11 && orders.prdtype !=12 }">
					                            	<a target="_blank" href="${ctx}/cost/review/read/${orders.chanpid}/${orders.groupid}/0?from=operatorPre&menuid=2">查看</a>
					                            </c:if>
				                            </c:if>
			                            </c:if>
		                            <c:if test="${orders.reviewflag eq 2 }">
											<c:if test="${orders.prdtype < 6 || orders.prdtype == 10}">
												<a href="${ctx }/costReview/activity/activityCostReviewDetail/${orders.chanpid }/${orders.groupid }/2?read=1" target="_blank">查看</a>
											</c:if>
											<c:if test="${orders.prdtype == 6 }">
												<a href="${ctx }/costReview/visa/visaCostReviewDetail/${orders.chanpid }/2?read=1" target="_blank">查看</a>
											</c:if>
											<c:if test="${orders.prdtype == 7 }">
												<a href="${ctx }/costReview/airticket/airticketCostReviewDetail/${orders.chanpid }/2?read=1" target="_blank">查看</a>
											</c:if>
										</c:if>
		                            <c:if test = "${orders.flowtype == 7}">
		                            	<a href="${ctx}/deposite/depositeRefundReviewDetail?orderid=${orders.orderid}&revid=${orders.revid}&prdType=${orders.prdtype}&flag=1&nowlevel=${orders.curlevel}" target="_blank">查看</a>
									</c:if>
									<c:if test="${(orders.prdtype == 7 || orders.prdtype == 6) && orders.flowtype == 1}">
										<c:if test="${not empty orders.chanpid}"><shiro:hasPermission name="refundpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${orders.chanpid}/${orders.prdtype}" target="_blank">预报单</a></shiro:hasPermission></c:if>
	                              		<c:if test="${not empty orders.chanpid}"><shiro:hasPermission name="refundpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${orders.chanpid}/${orders.prdtype}" target="_blank">结算单</a></shiro:hasPermission></c:if>
									</c:if>
									<c:if test="${orders.prdtype != 7 && orders.prdtype != 6&& orders.prdtype != 8&& orders.prdtype != 9}">
										<c:if test="${not empty orders.chanpid}"><shiro:hasPermission name="refundpay:operation:statement"><a href="${ctx }/cost/manager/forcastList/${orders.groupid}/${orders.prdtype}" target="_blank">预报单</a></shiro:hasPermission></c:if>
	                              		<c:if test="${not empty orders.chanpid}"><shiro:hasPermission name="refundpay:operation:finalStatement"><a href="${ctx }/cost/manager/settleList/${orders.groupid}/${orders.prdtype}" target="_blank">结算单</a></shiro:hasPermission></c:if>
									</c:if>
									<c:if test = "${orders.reviewflag == 1}">
										<c:if test="${orders.payStatus eq '1' }">
											<a onclick="confirmOrCannePay('${ctx }','${orders.revid}','2','0',${orders.payStatus })"
												href="javascript:void(0)">撤销付款</a>
										</c:if>
										<c:if test="${orders.payStatus ne '1'}">
											<a onclick="jbox_paymentconfirmrefundold('${ctx }','${orders.revid}','2','1',${orders.payStatus })"
												href="javascript:void(0)">确认付款</a>
										</c:if>
									</c:if>
									<c:if test = "${orders.reviewflag == 2}">
										<c:if test="${orders.payStatus eq '1' }">
											<a onclick="confirmOrCannelPay('${ctx }','${orders.revid}','0','1')"
												href="javascript:void(0)">撤销付款</a>
										</c:if>
										<c:if test="${orders.payStatus ne '1'}">
											<a onclick="jbox_paymentconfirmrefund('${ctx }','${orders.revid}','1','0')"
												href="javascript:void(0)">确认付款</a>
									</c:if>
									</c:if>
									<c:if test="${companyUuid eq '75895555346a4db9a96ba9237eae96a5' }">
										<c:if test="${orders.invoiceStatus eq '1' }">
											<a onclick="confirmOrCannelInvoice('${ctx }','${orders.revid}','1','1')"
												href="javascript:void(0)">撤销发票</a>
										</c:if>
										<c:if test="${orders.invoiceStatus ne '1'}">
											<a onclick="confirmOrCannelInvoice('${ctx }','${orders.revid}','0','0')"
													href="javascript:void(0)">确认发票</a>
										</c:if>
									</c:if>
									<c:if test="${orders.prdtype == 11 || orders.prdtype == 12}">
										<a href="${ctx }/refundReview/refundTable?reviewId=${orders.revid}" target="_blank">打印</a>
									</c:if>
									<c:if test="${orders.prdtype != 11 && orders.prdtype != 12}">
										<shiro:hasPermission name="refund:operation:print">
											<a onClick="printRefund('${orders.prdtype}', '${orders.agentid}', '${orders.orderid}', '${orders.revid}', '${orders.groupcode}', '${orders.reviewflag}', '','order');">打印</a>
										</shiro:hasPermission>
									</c:if>
								</p>
							</dd>
						</dl>
						</td>
				</tr>
					
				<c:if test="${orders.reviewflag == 2}">
				<tr id="child1_${orders.idlong}" class="activity_team_top1"
					style="display:none">
				</c:if>
				<c:if test="${orders.reviewflag == 1}">
				<tr id="child1_${orders.revid}" class="activity_team_top1"
					style="display:none">
				</c:if>
					<td colspan="15" class="team_top">
						<table id="teamTable" class="table activitylist_bodyer_table"
							style="margin:0 auto;">
							<thead>
								<tr>
									<th class="tc" width="10%">付款方式</th>
									<th class="tc" width="10%">金额</th>
									<th class="tc" width="15%">日期</th>
									<th class="tc" width="10%">支付类型</th>
									<th class="tc" width="25%">支付凭证</th>
									<th class="tc" width="10%">状态</th>
									<th class="tc" width="15%">操作</th>
								</tr> 
							</thead>
							<c:if test="${orders.reviewflag == 2}">
								<tbody id='rpi_${orders.idlong}'>
							</c:if>
							<c:if test="${orders.reviewflag == 1}">
								<tbody id='rpi_${orders.revid}'>
							</c:if>
							</tbody>
						</table></td>
				</tr>

			</c:forEach>
		</tbody>
	</table>
	<%--<div id="contentTable_foot">--%>
        <%--<label><input check-action="All" type="checkbox"> 全选</label>--%>
        <%--<label><input check-action="Reverse" type="checkbox"> 反选</label>--%>
        <%--<input type="button" class="btn-primary" value="批量确认付款" onclick="paymentconfirmallrefunds('${ctx }','checkBox','2',1)" />--%>
        <%--<c:if test="${companyUuid eq '75895555346a4db9a96ba9237eae96a5' }">--%>
        	<%--<input type="button" class="btn-primary" value="批量确认发票" onclick="confirmOrCannelInvoiceValues('${ctx }','checkBox','2',1)" />--%>
        <%--</c:if>--%>
        <%--<shiro:hasPermission name="refund:operation:print">--%>
        	<%--&nbsp;<input type="button" class="btn-primary" value="批量打印" onclick="batchPrint()" />--%>
        <%--</shiro:hasPermission>--%>
    <%--</div>--%>
	<%--<div class="pagination clearFix">${page}</div>--%>

		<div class="page" id="contentTable_foot">
			<div class="pagination">
				<dl>
					<dt><input check-action="All" type="checkbox">全选</dt>
					<dt><input check-action="Reverse" type="checkbox">反选</dt>
					<dd>
						<input type="button" class="btn ydbz_x" value="批量确认付款" onclick="paymentconfirmallrefunds('${ctx }','checkBox','2',1)" />
						<c:if test="${companyUuid eq '75895555346a4db9a96ba9237eae96a5' }">
							<input type="button" class="btn ydbz_x" value="批量确认发票" onclick="confirmOrCannelInvoiceValues('${ctx }','checkBox','2',1)" />
						</c:if>
						<shiro:hasPermission name="refund:operation:print">
							&nbsp;<input type="button" class="btn ydbz_x" value="批量打印" onclick="batchPrint()" />
						</shiro:hasPermission>
					</dd>
				</dl>
			</div>

			<div class="pagination clearFix">
				${page}
			</div>
		</div>


	<form id="refundRevPrintRefundForm" action="${ctx}/refundnew/refundReviewInfo" method = "post" target="_blank">
		<input type="hidden" value="" name="productType"/>
		<input type="hidden" value="" name="agentId"/>
		<input type="hidden" value="" name="orderId"/>
		<input type="hidden" value="" name="reviewId"/>
		<input type="hidden" value="" name="groupCodePrint"/>
		<input type="hidden" value="" name="payId" />
		<input type="hidden" value="" name="option"/>
	</form>
</div>
	<style type="text/css">
/*.jbox-title-panel{background: #3a7850 !important;}
div.jbox .jbox-button{background: #3a7850 !important; color:#FFF; font-size:12px;}
.jbox-title{}*/
.orderList_dosome {
	text-align: left;
	margin-left: 11px;
}

.orderList_line {
	height: 100%;
	width: 50px;
	float: left;
}
</style>
</body>
</html>