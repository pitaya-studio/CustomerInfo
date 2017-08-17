<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%    
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>

<html>
<head>
<title>签证订单收款</title>
<meta name="decorator" content="wholesaler"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payedConfirm.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
var startOutBegin ='${startOutBegin}';
var startOutEnd = '${startOutEnd}';
//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
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
	contextPath = "${ctx}";
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	switchSalerAndPicker();
	//renderSelects(selectQuery());
	//计调模糊匹配
	$("[name=jd]").comboboxSingle();
	//销售模糊匹配
	$("[name=saler]").comboboxSingle();
	//下单人模糊匹配
	$("[name=createByName]").comboboxSingle();
	
	switchYingshouAndWeishou();
	
	//渠道改为可输入的select
 	$("#modifyAgentInfo").comboboxInquiry();
 	//0405 收款银行改为可输入的select add by 20160427
 	$("#toBankNname").comboboxInquiry();
	//渠道模糊匹配
	$("[name=agentName]").comboboxSingle();
	
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
            _$orderBy="updateDate DESC";
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
            
            
          //首次打印提醒
            $(".uiPrint").hover(function(){
            	$(this).find("span").show();
            },function(){
            	$(this).find("span").hide();
            })
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

function orderDetail(orderId,orderType){
	var url = "";
		url = "${ctx}/visa/order/goUpdateVisaOrder?visaOrderId=" + orderId +"&mainOrderCode=&details=1";
	window.open(url,"_blank");
}

function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    var timstamp = (new Date()).valueOf();
    $('#searchForm').attr("action",contextPath + "/visaOrderPayLog/manage/showVisaOrderTravelPayList/199/1.htm?option=visaOrder&timestamp="+timstamp);
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
	
	var timestamp = (new Date()).valueOf();
	var act  = $('#searchForm').attr("action");
	$('#searchForm').attr("action",act+"&timestamp="+timestamp);
	
	$('#searchForm').submit();
}
/**
 * 		String orderno = request.getParameter("orderno");
	String money = request.getParameter("money");
	String serialNum = request.getParameter("serialNum");
 */

function rejectConfirmOper(orderno,travelerid,orderPayId,serialNum,_this){
	 //'${orders.order_no}','${orders.amount}','${orders.oid}','${orders.serialNum}',this)
	 var $this = $(_this);
		$.jBox.confirm("确定驳回吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
					type: "POST",
			        url: "${ctx}/visaOrderPayLog/manage/rejectConfirmOper",
			        async:false,
			        cache:false,
			        data: {
			        	orderno:orderno,
			        	travelerId:travelerid,
			        	orderPayId:orderPayId,
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



function cancelConfirm(orderPaySerialNum,_this){
	

	 var $this = $(_this);
		$.jBox.confirm("确定撤销吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				
				$("table a").each(function(){
					var atext = $(this).html();
					if("收款确认"==atext||"撤销"==atext||"驳回"==atext){
						//alert(atext);
						//$(this).attr('disabled','disabled');
						$(this).addClass("disableCss");
					}
				});
				
				$.ajax({
			        type: "POST",
			        url: "${ctx}/orderPay/cancelVisaOrderOper",
			        async:false,
			        cache:false,
			        data: {
			        	'orderPaySerialNum':orderPaySerialNum,
			        	'payType':"1"
			        },
			        success: function(data){
			        	top.$.jBox.tip('确定成功','success');                   
	                    //$this.remove();
	                    $("#searchForm").submit();
			        }
		     	}); 
			}else if (v == 'cancel'){
				
				
			}
	});
}


//('${ctx}','${orders.order_no}','${orders.travelerid}','${orders.oid}','${orders.serialNum}','1',this);">驳回${orders.travelerid}

//'${orders.orderPaySerialNum}','${orders.vpid}','${orders.vorderid}'
function payedConfirmRejects(ctx,orderPaySerialNum,vorderid,sign,obj){

	//debugger;
	$.jBox("iframe:"+ctx+"/visaOrderPayLog/manage/getOrderPayForRejectOne",{		
		    title: "驳回确认",
			width:500,
	   		height: 330,
	   		buttons:{'取消': 0,'确认':1},
	   		persistent:true,
	   		loaded: function (h) {},
	   		submit: function(v,h,f){
	   			if(v==1){
	   				
	   				//禁止操作  所有收款确认列的链接
	   				$("table a").each(function(){
	   					var atext = $(this).html();
	   					if("收款确认"==atext||"撤销"==atext||"驳回"==atext){
	   						//alert(atext);
	   						//$(this).attr('disabled','disabled');
	   						$(this).addClass("disableCss");
	   					}
	   				});
	   				
	   				$(obj).siblings().remove();
	   				$(obj).remove();
	   				  var payType = $(h.find("iframe")[0].contentWindow.payType).val();
	
				      var orderPayId = $(h.find("iframe")[0].contentWindow.orderPayId).val();
				      
				      //0-保持占位，1-退回占位
				      var rejectRadio = $(h.find("iframe")[0].contentWindow.innerTable).find("[name=rejectRadio]:checked").val();

					//驳回备注
					var reason = $(h.find("iframe")[0].contentWindow.reject).find("[name=reason]").val();
				      
				    //sign为驳回类型标识，0-订单收款驳回操作，1-签证押金收款驳回操作
				      if(sign == "0"){
				    	  if(null == rejectRadio){
					    	  top.$.jBox.tip("请选择驳回占位方式！",'success');  
					    	  return false;
					      }
				      }
				      
			         dataparam={ 
										      payType:payType,
										      orderPayId:orderPayId
							     }
			            
			      $.ajax({ 
			          type:"POST",
			          url: "${ctx}/visaOrderPayLog/manage/rejectConfirmOper",
			          dataType:"json",
			          //data:dataparam,
			          data: {
			        	'orderPaySerialNum':orderPaySerialNum,
			        	'vorderid':vorderid,
						'reason':reason
			        },
			          success:function(data){
			        	  var rejectMark = data.rejectMark;
			        	  if(null == rejectMark){
			        		  return false;
			        	  }
			        	  top.$.jBox.tip(data.rejectMark,'success');  
			        	  
				             // $(obj).parents('table').siblings('#searchForm').submit();
				        	  $("#searchForm").submit();
			             // $(obj).parents('table').siblings('#searchForm').submit();
			             
			          }
			      });
	   			}
	   			
	   		}
	});
	$("#jbox-content").css("overflow-y","hidden");
}

function payedConfirmForVisOrder(ctx,orderPaySerialNum,orderNum,agentid,payType,obj){
	var ht = ($(window).height())*0.7;
	$.jBox("iframe:"+ctx+"/orderPay/getOrderPayInfoForVisaOrder?orderNum="+orderNum+"&agentid="+agentid+"&orderPaySerialNum="+orderPaySerialNum+"&payType="+payType,{		
		    title: "收款确认",
			width:830,
	   		height: ht,
	   		buttons:{'取消': 0,'确认收款':1},
	   		persistent:true,
	   		loaded: function (h) {
	   			$(".jbox-content").css("overflow","hidden");
	   		},
	   		submit: function(v,h,f){
	   			if(v==1){
					$("table a").each(function(){
						var atext = $(this).html();
						if("收款确认"==atext||"撤销"==atext||"驳回"==atext){
							//alert(atext);
							//$(this).attr('disabled','disabled');
							if($(h.find("iframe")[0].contentWindow.accountDate).val().length==0)
							return false;
							$(this).addClass("disableCss");
						}
					});
	   				
	   				   var payerName =  $(h.find("iframe")[0].contentWindow.payerName).val();
				     
				      var payType = $(h.find("iframe")[0].contentWindow.payType).val();
				      var bankName= $(h.find("iframe")[0].contentWindow.bankName).val();
				      var bankAccount =$(h.find("iframe")[0].contentWindow.bankAccount).val();
				      var toBankNname = $(h.find("iframe")[0].contentWindow.toBankNname).val();		

				      var toBankAccount=$(h.find("iframe")[0].contentWindow.toBankAccount).val();
				      var orderPayId = $(h.find("iframe")[0].contentWindow.orderPayId).val();
				      var DocInfoIds = $(h.find("iframe")[0].contentWindow.DocInfoIds).parent().find("div[class=uploadPath] input[name=DocInfoIds]").val();
				      var checkNumber =  $(h.find("iframe")[0].contentWindow.checkNumber).val();
				      var remarks= $(h.find("iframe")[0].contentWindow.remarks).val();
				      var accountDate = $(h.find("iframe")[0].contentWindow.accountDate).val();
				      var orderNum =$(h.find("iframe")[0].contentWindow.orderNum).val();
				      //var orderPaySerialNum = $(h.find("iframe")[0].contentWindow.orderPaySerialNum).val();
				      
				      var fromAlipayName = $(h.find("iframe")[0].contentWindow.fromAlipayName).val();
				      var fromAlipayAccount = $(h.find("iframe")[0].contentWindow.fromAlipayAccount).val();
				      var toAlipayName = $(h.find("iframe")[0].contentWindow.toAlipayName).val();
				      var toAlipayAccount = $(h.find("iframe")[0].contentWindow.toAlipayAccount).val();
				      var comeOfficeName = $(h.find("iframe")[0].contentWindow.comeOfficeName).val();
			         dataparam={ 
			        	  payerName:payerName,
					      bankName:bankName,
					      bankAccount:bankAccount,
					      toBankNname:toBankNname,
					      toBankAccount:toBankAccount,
					      payType:payType,
					      orderPayId:orderPayId,
					      DocInfoIds:DocInfoIds,
					      checkNumber:checkNumber,
					      accountDate:accountDate,
					      remarks:remarks,
					      orderNum:orderNum,
					      orderPaySerialNum:orderPaySerialNum,
					     // invoiceDate:invoiceDate
					     fromAlipayName:fromAlipayName,
					     fromAlipayAccount:fromAlipayAccount,
					     toAlipayName:toAlipayName,
					     toAlipayAccount:toAlipayAccount,
					     comeOfficeName:comeOfficeName
						}
			      if(accountDate == ""){
			          $.jBox.tip("银行到账日期不能为空","WARM");
			          return false;
			      }else{
			           $.ajax({ 
				          type:"POST",
				          url:ctx+"/orderPay/confirmPayInfoForVisaOrder",
				          dataType:"json",
				          data:dataparam,
				          success:function(data){
				        	if('ok'==data.flag){
				                $("#searchForm").submit();
				        	}else if("false" == data.flag){
				        	    $.jBox.tip("收款确认失败！","WARM");
				        	    
				        	}
				          }
				      });
			      }     
			     
	   			}
	   			
	   		}
	});
}

$(document).ready(function() {
    $("#searchForm").validate({
        submitHandler: function(form){
            loading('数据处理中，请稍等...');
            form.submit();
        }
    });
});
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


function expand(child, obj) {
	debugger;
	if("收起客户" == $(obj).html()){
			$(obj).html("展开客户");
			$(obj).parents('tr').next().hide();
	}else{
		$(obj).html("收起客户")
	    $.ajax({ 
	        type:"POST",
	        url: "${ctx}/visaOrderPayLog/manage/getTravellerList",
	        dataType:"json",
	        async:false,
	        data: {
	      	serialNum:child,
			startOutBegin:startOutBegin,
			startOutEnd:startOutEnd
	      },
	        success:function(data){
	        	if($("#child").length == 0){
					$(obj).parents('tr').after(data.travellerinfo);
	        	}
	        }
	    });
		}

	
}

//---------批量打印-----------


function exportVisaorderPayExcel(ctx)
{	
	var data = $('#searchForm').serialize().toString();
    var form=$("<form>");//定义一个form表单
 	form.attr("style","display:none");
 	form.attr("target","_blank");//应0069需求,实现一种打开一种空白窗口交互的等待特效
 	                             //待弹出下载框,空白窗口会自动关闭
 	form.attr("method","post");
 	form.attr("action",ctx + "/visaOrderPayLog/manage/exportVisaorderPayExcel");
 	var input1=$("<input>");
 	input1.attr("type","hidden");
 	input1.attr("name","data");
 	input1.attr("value",data);
	$("body").append(form);//将表单放置在web中
 	form.append(input1);
	form.submit();//表单提交	 
}

</script>
<script type="text/javascript">
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

// 批量打印所需参数对象  wangyang 2016.10.25
function batchPrintObj(orderPaySerialNum, vpid, vorderid, isAsAccount) {
	this.orderPaySerialNum = orderPaySerialNum;
	this.vpid = vpid;
	this.vorderid = vorderid;
	this.isAsAccount = isAsAccount;
}

function batchPrint() {
	
	var objList = new Array();
	var $checks = $(".box:checked");
	
	// 未勾选订单时提示并中断操作
	if ($checks.length == 0) {
		top.$.jBox.tip('请选择数据', 'warnning');
		return ;
	}
	
	for (var i = 0; i < $checks.length; i++) {
		var check = $checks[i];
		var $td = $(check).parent();
		var bpObj = new batchPrintObj($td.find("#orderPaySerialNum").val(), 
			$td.find("#vpid").val(), $td.find("#vorderid").val(), $td.find("#isAsAccount").val());
		objList.push(bpObj);
	}
	
	var $div = $("#batchPrintDiv");
	$div.find("#paylist").val(JSON.stringify(objList));
	$div.find("#batchPrintForm").submit();
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
	.activitylist_paixu_left{ 
		width:79%;
		float:left;
	}
	input[type="checkbox"] {margin: 0;padding-left:0;padding-right:0;}
	
	
</style>
</head>
<body>
<!-- 顶部参数 -->
<%--<page:applyDecorator name="finance_op_head" >--%>
    <%--<page:param name="current"><c:choose><c:when test="${param.option eq 'visaOrder' or empty param.option}">visaOrder</c:when><c:when test="${param.option eq 'account'}">agingList</c:when></c:choose></page:param>--%>
<%--</page:applyDecorator>--%>
<c:choose><c:when test="${param.option eq 'visaOrder' or empty param.option}"><c:set var ="current" value="visaOrder" /></c:when><c:when test="${param.option eq 'account'}"><c:set var ="current" value="agingList" /></c:when></c:choose>
<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
<!-- visaOrderPayLog/manage/showVisaOrderTravelPayList/199/1.htm?option=visaOrder -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/visaOrderPayLog/manage/showVisaOrderTravelPayList/199/1.htm?option=visaOrder" method="post" >
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
	<!--<div class="order_bill"></div>-->
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2">
			<input id="orderNum" name="orderNum" class="inputTxt searchInput inputTxtlong" value="${orderNum }" placeholder="请输入订单号"/>
		</div>
		<div class="zksx">筛选</div>
		<div class="form_submit">
			<input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索"/>
			<input class="btn ydbz_x" type="button" onclick="resetForm();" value="清空所有条件" />
			<input class="btn ydbz_x" onclick="exportVisaorderPayExcel('${ctx}')" value="导出Excel" type="button">
		</div>
		<div class="ydxbd">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">来款单位：</div>
				<input value="${payerName}" class="inputTxt" name="payerName" id="payerName" type="text" />
			</div> 
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">打印状态：</div>
				<div class="selectStyle">
					<select name="printFlag">
						<option value="">全部</option>
						<option value="1" <c:if test="${printFlag eq '1'}">selected="selected"</c:if>>已打印</option>
						<option value="2" <c:if test="${printFlag eq '2'}">selected="selected"</c:if>>未打印</option>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">银行到账日期：</label>
				<input id="bankTimeBegin" name="bankTimeBegin"  value="${bankTimeBegin}"  class="inputTxt dateinput" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				<span> 至 </span>  
				<input id="bankTimeEnd" name="bankTimeEnd" value="${bankTimeEnd}" class="inputTxt dateinput" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">游客：</div>
				<input id="travellerName" name="travellerName"   class="inputTxt inputTxtlong" value="${travellerName}" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">销售：</div>
				<select name="saler">
					<option value="">全部</option>
					<c:forEach var="salers" items="${agentSalers }">
						<option value="${salers.key }" <c:if test="${saler==salers.key}">selected="selected"</c:if>>${salers.value }</option>
					</c:forEach>
				</select>
			</div>   
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款日期：</label>
				<input id="payTimeBegin" name="payTimeBegin" class="inputTxt dateinput" value="${payTimeBegin }" 
											readonly onClick="WdatePicker()"/> 至 
				<input id="payTimeEnd" name="payTimeEnd" value="${payTimeEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>          
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">渠道选择：</label>
				<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
				<select id="modifyAgentInfo" name="agentId">
					<option value="">全部</option>
					<c:choose>
						<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>>未签</option></c:when>
						<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }"><option value="-1" <c:if test="${agentId==-1}">selected="selected"</c:if>>直客</option></c:when>
						<c:otherwise><option value="-1" <c:if test="${agentId eq -1}">selected="selected"</c:if>>非签约渠道</option></c:otherwise>
					</c:choose>
					<c:forEach var="agentinfo" items="${agentinfoList }">
						<option value="${agentinfo.id }" <c:if test="${agentId eq agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">是否到账：</div>
				<div class="selectStyle">
					<select name="isAccounted">
						<option value="">全部</option>
						<option value="Y" <c:if test="${isAccounted eq 'Y'}">selected="selected"</c:if>>已到账</option>
						<option value="N" <c:if test="${isAccounted eq 'N'}">selected="selected"</c:if>>未到账</option>
						<option value="D" <c:if test="${isAccounted eq 'D'}">selected="selected"</c:if>>已驳回</option>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款金额：</label>
				<input id="payAmountStrat" name="payAmountStrat"  class="inputTxt" data-type="float" value="${payAmountStrat}"  />
				至
				<input id="payAmountEnd"   name="payAmountEnd"   class="inputTxt" data-type="float" value="${payAmountEnd}" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款银行：</label>
				<select id="toBankNname" name="toBankNname">
					<option value="">请选择</option>
					<c:forEach var="bankName" items="${receiptBankName }">
						<option value="${bankName }" <c:if test="${bankName eq toBankNname}">selected="selected"</c:if>>${bankName }</option>
					</c:forEach>
				</select>
			</div>
			<!--0405 收款银行改为可输入可下拉选择模糊匹配 END  -->
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">计调：</div>
				<select name="jd">
					<option value="">全部</option>
					<c:forEach var="jd" items="${agentJd }">
						<option value="${jd.key }" <c:if test="${jds==jd.key}">selected="selected"</c:if>>${jd.value }</option>
                    </c:forEach>
				</select>
			</div>
			<!-- 0405 收款确认日期 -->
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">收款确认日期：</label>
				<input id="confirmationDateStar" name="confirmationDateStar"  value="${confirmationDateStar}"  class="inputTxt dateinput" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				<span> 至 </span>  
				<input id="confirmationDateEnd" name="confirmationDateEnd" value="${confirmationDateEnd}" class="inputTxt dateinput" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">下单人：</div>
				<select id="createByName" name="createByName">
					<option value="">不限</option>
					<c:forEach items="${createByList}" var="createByIdandName1">
						<option value="${createByIdandName1.id}" <c:if test="${createByName eq createByIdandName1.id}">selected="selected"</c:if>>${createByIdandName1.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">收款方式：</div>
				<div class="selectStyle">
					<select id="payType" name="payType"  >
						<option value="">全部</option>
						<option value="1"  <c:if test="${payType == 1}"> selected="selected" </c:if>>支票</option>
						<option value="3"  <c:if test="${payType == 3}"> selected="selected" </c:if>>现金</option>
						<option value="4"  <c:if test="${payType == 4}"> selected="selected" </c:if>>汇款</option>
						<option value="6"  <c:if test="${payType == 6}"> selected="selected" </c:if>>转账</option>
						<option value="7"  <c:if test="${payType == 7}"> selected="selected" </c:if>>汇票</option>
						<option value="8"  <c:if test="${payType == 8}"> selected="selected" </c:if>>POS</option>
						<c:if test="${isLMT }">
							<option value="9"  <c:if test="${payType == 9}"> selected="selected" </c:if>>因公支付宝</option>
						</c:if>
					</select>
				</div>
			</div>
			<%-- <div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
					<select name="paymentType">
						<option value="">不限</option>
						<c:forEach var="pType" items="${fns:findAllPaymentType()}">
							<option value="${pType[0] }"
								<c:if test="${paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
						</c:forEach>
					</select>
				</div> --%>
			<%--180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv--%>
			<c:if test="${companyUuid eq '58a27feeab3944378b266aff05b627d2' }">
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">出团日期：</label>
					<input id="startOutBegin" name="startOutBegin" class="inputTxt dateinput" value="${startOutBegin }"
							readonly onClick="WdatePicker()"/> 至
					<input id="startOutEnd" name="startOutEnd" value="${startOutEnd }" readonly onClick="WdatePicker()" class="inputTxt dateinput"/>
				</div>
				 <%--180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv--%>
			</c:if>
		</div>
	</div>
</form:form>
    


	<div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
          <div class="activitylist_paixu_left">
            <ul>
            <li class="activitylist_paixu_left_biankuang lio.createDate"><a onClick="sortby('o.createDate',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang lio.updateDate"><a onClick="sortby('o.updateDate',this)">更新时间</a></li>
            </ul>
      		</div>
          <div class="activitylist_paixu_right">
   	      <%--<input class="btn btn-primary " onclick="exportVisaorderPayExcel('${ctx}')" value="导出Excel" type="button">--%>
                                查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
          <div class="kong"></div>
        </div>
      </div>

<table id="contentTable" class="table table-striped mainTable table-condensed activitylist_bodyer_table">
        <!--  需求0307 起航假期  王洋 ${companyUUID eq '5c05dfc65cd24c239cd1528e03965021' }  -->
		<%-- <c:set var="qhjq" value="${companyUUID eq '5c05dfc65cd24c239cd1528e03965021' }" scope="page"/> --%>
        <thead style="background:#403738;">
        <tr>
			<%--161009 bug16220 调整th百分比--%>
            <th width="4%">序号</th>
            <th width="11%">收款日期</br>银行到账日期</th>
            <th width="5%">收款确认日期</th>
           <th width="5%">订单号</th>
            <c:if test="${fns:getUser().company.id == '68' }">
			    <th width="5%">订单团号</th>
			</c:if>
			<c:if test="${fns:getUser().company.id != '68' }">
			    <th width="5%">团号</th>
			</c:if>
			<th width="5%">产品名称</th>
			<!--  
			<th width="4%">签证类型</th>
			<th width="4%">签证国家</th>
			-->
			
			<%-- <c:if test="${fns:getUser().company.id == '68' }">
				<th width="5%"><span class="tuanhao">团号</span>/<span class="chanpin on">产品名称</span></th>
			</c:if>
			<c:if test="${fns:getUser().company.id != '68' }">
				<th width="5%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
			</c:if> --%>
				
			<th width="5%">
			<%-- <c:choose>
				<c:when test="${qhjq }">
					<span>销售</span>
				</c:when>
				隐藏下单人_djw -- %>
					<%-- <span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span>
				<c:otherwise>
				</c:otherwise>
			</c:choose> --%>
			<span>销售</span>
			</th>
			<th width="5%">计调</th>
			<th width="5%">渠道名称</th>
			<th width="5%">来款单位</th>
			<th width="5%">收款银行</th>
			<!--  
			<th width="6%">下单时间</th>
			<th width="6%">付款时间</th>
			-->
			 <c:if test="${Companyuuid eq '7a45838277a811e5bc1e000c29cf2586'}"> 
			 <th width="10%">订单金额 <br /><span class="order-weishou-title">未收余额</span>/<span class="order-yingshou-title on">累计到账金额</span></th>
			 </c:if>
			  <c:if test="${Companyuuid ne '7a45838277a811e5bc1e000c29cf2586'}"> 
			 <th width="10%">订单金额 <br />累计到账金额</th>
			  </c:if>
			
			<th width="7%">已收金额<br>到账金额</th>
			<th width="5%">收款确认 </th>
			<th width="5%">打印确认</th>
			<th width="5%">操作</th>
	    </tr>
	    </thead>
        <tbody>
        
         <c:if test="${fn:length(page.list) <= 0 }">
		         <tr class="toptr" ><td colspan="16" style="text-align: center;">暂无结果</td></tr>
        </c:if>
        <c:forEach items="${page.list }" var="orders" varStatus="s">
		        <tr class="toptr">
					<td>
						<input type="checkbox" check-action="Normal" class="box" />
						<input type="hidden" id="orderPaySerialNum" value="${orders.orderPaySerialNum}" />
						<input type="hidden" id="vpid" value="${orders.vpid}" />
						<input type="hidden" id="vorderid" value="${orders.vorderid}" />
						<input type="hidden" id="isAsAccount" value="${orders.isAsAccount}" />
						${s.count }</td>
					<td class="p0">
						<div class="out-date">
		           <c:choose>
                            <c:when test="${not empty orders.odate}">  <fmt:formatDate value="${orders.odate}" pattern="yyyy-MM-dd"/></c:when>
                            <c:otherwise></c:otherwise>
                    </c:choose>
		            </div>
                    	<div class="close-date">
                    <c:choose>
                            <c:when test="${not empty orders.accountDate && orders.isAsAccount == 1}">  <fmt:formatDate value="${orders.accountDate}" pattern="yyyy-MM-dd"/></c:when>
                            <c:otherwise></c:otherwise>
                    </c:choose>
                    </div>
                    </td>
                    <!-- 0405 收款确认日期 -->
		            <td><fmt:formatDate value="${orders.receiptConfirmationDate}" pattern="yyyy-MM-dd"/></td>
                    <c:choose>
                            <c:when test="${not empty orders.orderNum}"><td>${orders.orderNum}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    
                    <!-- 
                    <c:choose>
                            <c:when test="${not empty orders.label}"><td>${orders.label}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    <c:choose>
                            <c:when test="${not empty orders.countryName_cn}"><td>${orders.countryName_cn}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    -->
                    <!--  
                    <c:choose>
                            <c:when test="${not empty orders.productName}"><td>${orders.productName}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    -->
               
                        <td>
                         <c:if test="${fns:getUser().company.id == '68' }">
                           <div class="tuanhao_cen onshow">${orders.groupCode}</div>
                         </c:if>
                         <c:if test="${fns:getUser().company.id != '68' }">
                          <div class="tuanhao_cen onshow">${orders.vpGroupCode}</div>
                         </c:if>
                        </td>
              			<td>
							<c:if test="${fns:getUser().company.id == '68' }">
								<div class="" title="${orders.productName}">${orders.productName}</div>
							</c:if>
							<c:if test="${fns:getUser().company.id != '68' }">
								<div class="" title="${orders.productName}">${orders.productName}</div>
							</c:if>
			  			</td>
              		   

                <td>
							<span class="order-saler onshow">
							 	<c:choose>
                            		<c:when test="${not empty orders.sName}">${orders.sName}</c:when>
                            		<c:otherwise></c:otherwise>
                    			</c:choose>
							</span>
							<span class="order-picker">
								<c:choose>
                            		<c:when test="${not empty orders.suuName}">${orders.suuName}</c:when>
                            		<c:otherwise></c:otherwise>
                    			</c:choose>
							</span>
	
				</td> 
                   <c:choose>
                            <c:when test="${not empty orders.jd_Name}"><td>${orders.jd_Name}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    <!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
                     <c:choose>
                            <c:when test="${not empty orders.agentName}">
                            	<td>
                            		<c:choose>
                            			<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and orders.agentName eq '非签约渠道' }">未签</c:when>
                            			<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and orders.agentName eq '非签约渠道' }">直客</c:when>
                            			<c:otherwise>${orders.agentName}</c:otherwise>
                            		</c:choose>
                            	</td>
                            </c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    
                      <!--   
                    <c:choose>
                            <c:when test="${not empty orders.vodate}"><td>  <fmt:formatDate value="${orders.vodate}" pattern="yyyy-MM-dd"/></td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    <c:choose>
                            <c:when test="${not empty orders.odate}"><td>  <fmt:formatDate value="${orders.odate}" pattern="yyyy-MM-dd"/></td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    -->
                    
                    
                    
                    <c:choose>
                            <c:when test="${not empty orders.payerName}"><td>${orders.payerName}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    
                    <c:choose>
                            <c:when test="${not empty orders.toBankNname}"><td>${orders.toBankNname}</td></c:when>
                            <c:otherwise><td> </td></c:otherwise>
                    </c:choose>
                    
                    
                    
                    
                    
              <c:if test="${Companyuuid eq '7a45838277a811e5bc1e000c29cf2586'}"> 
              	<td class="p0 tr">	
                        <div class="yfje_dd">
		              		<span class="fbold"><c:if test="${not empty orders.total_money}">${orders.total_money}</c:if>
		               	</span>
                        </div>
                        <div class="dzje_dd">
                        
                        <span class="order-weishou">
                         <span class="fbold"><c:if test="${not empty orders.no_pay_money}">${orders.no_pay_money}</c:if>
			            </span>
			            </span>
			            <span class="order-yingshou onshow">
			             <span class="fbold"><c:if test="${not empty orders.accounted_money}">${orders.accounted_money}</c:if>
			            </span>
			            </span>
                        </div>
		            </td> 
              
              </c:if>
              
              
                <c:if test="${Companyuuid ne '7a45838277a811e5bc1e000c29cf2586'}"> 
               <td class="p0 tr">	
                        <div class="yfje_dd">
		              		<span class="fbold"><c:if test="${not empty orders.total_money}">${orders.total_money}</c:if>
		               	</span>
                        </div>
                        <div class="dzje_dd">
			             <span class="fbold"><c:if test="${not empty orders.accounted_money && orders.cancleFlag == 1}">${orders.accounted_money}</c:if>
			            </span>
                        </div>
		            </td>
                </c:if>
              
                    
                    <td class="p0 tr pr">	
                    	<c:if test="${orders.payType==3 }">
                    	     <span class="icon-mode-of-payment-cash">
<!--                                 <span>现金支付</span> -->
                             </span>
                    	</c:if>
                    	<c:if test="${orders.payType==1 }">
                    	     <span class="icon-mode-of-payment-check">  </span>
                    	</c:if>
                    	<c:if test="${orders.payType==4 }">
                    	     <span class="icon-mode-of-payment-remit">  </span>
                    	</c:if>
                    	<c:if test="${orders.payType==6 }">
                    	     <span class="icon-mode-of-payment-transfer">  </span>
                    	</c:if>
                    	<c:if test="${orders.payType==7 }">
                    	     <span class="icon-mode-of-payment-bill">  </span>
                    	</c:if>
                    	<c:if test="${orders.payType==8 }">
                    	     <span class="icon-mode-of-payment-pos">  </span>
                    	</c:if>
                    	<c:if test="${orders.payType==9 }">
                    		 <span class="icon-mode-of-payment-alipay">  </span>
                    	</c:if>
                    	
                        <div class="yfje_dd">
		              		<span class="fbold"><c:if test="${not empty orders.payed_money}">${orders.payed_money}</c:if>
		               	</span>
                        </div>
                        <div class="dzje_dd">
			              <span class="fbold"><c:if test="${orders.isAsAccount eq '1'}">${orders.payed_money}</c:if>
			            </span>
                        </div>
		            </td>   
                 	<c:if test="${orders.isAsAccount == 99 }">
                 	    <td class="p0 tr">
                 	       <a href="javascript:void(0)" onClick="javascript:payedConfirmForVisOrder('${ctx}','${orders.orderPaySerialNum}','${orders.orderNum }','${orders.agentId}',${orders.payType },this)">收款确认</a>||
                 	       <a  href="javascript:void(0)" onClick="javascript:payedConfirmRejects('${ctx}','${orders.orderPaySerialNum}','${orders.vorderid}','1',this);">驳回</a>
                 	    </td>
                 	</c:if>
                    <c:if test="${orders.isAsAccount == 2 }"><td class="p0 tr">已驳回</td></c:if>
                    <c:if test="${orders.isAsAccount == 0 }">
	                    <td class="p0 tr">
                           <a href="javascript:void(0)" onClick="javascript:payedConfirmForVisOrder('${ctx}','${orders.orderPaySerialNum}','${orders.orderNum }','${orders.agentId}',${orders.payType },this)">收款确认</a>||
                 	       <a  href="javascript:void(0)" onClick="javascript:payedConfirmRejects('${ctx}','${orders.orderPaySerialNum}','${orders.vorderid}','1',this);">驳回</a>
	                    </td>
                    </c:if>
                    <c:if test="${orders.isAsAccount == 1 }">
	                    <td class="p0 tr">已确认
	                    	<shiro:hasPermission name="visaorder:operation:revoke">
	                        ||<a href="javascript:void(0)" onClick="javascript:cancelConfirm('${orders.orderPaySerialNum}',this);">撤销</a>
	                        </shiro:hasPermission>
	                    </td>
                    </c:if>
                    
                    <td class="invoice_yes">
		            	<c:if test="${empty orders.printFlag || orders.printFlag == 0 }">未打印</c:if>
		            	<c:if test="${orders.printFlag == 1 }"><p class="uiPrint">已打印<span style="display: none;">首次打印时间<br>${orders.printTime}</span></p></c:if>
		            </td>


                  	<td class="p0 td">
	                  	<a href="javascript:void(0)" onClick="javascript:orderDetail('${orders.vorderid}',6);">订单详情</a> <br>
	                  	<a  href="${ctx}/visaOrderPayLog/manage/showPrint?orderPaySerialNum=${orders.orderPaySerialNum}&vpid=${orders.vpid}&vorderid=${orders.vorderid}&isAsAccount=${orders.isAsAccount}" target="_blank" >打印</a> <br>
	                  	<a href="${ctx}/visa/order/downloadTravelerDetails?visaOrderId=${orders.vorderid}&serialNum=${orders.orderPaySerialNum}">游客明细</a>
	                  	<a onclick="expand('${orders.orderPaySerialNum}',this)"  href="javascript:void(0)">展开客户</a>
                  	</td>  

		        </tr>

		        
            </c:forEach>
        </tbody>
    </table>
    <%--<div id="contentTable_foot">--%>
        <%--<label><input check-action="All" type="checkbox"> 全选</label>--%>
        <%--<label><input check-action="Reverse" type="checkbox"> 反选</label>--%>
        <%--<input type="button" class="btn btn-primary" onclick="batchPrint();" value="批量打印">--%>
    <%--</div>--%>
    <div id="batchPrintDiv">
    	<form action="${ctx }/visaOrderPayLog/manage/showBatchPrint" id="batchPrintForm" method="post" target="_blank">
    		<input id="paylist" name="paylist" type="hidden" value="" />
    	</form>
    </div>
</div>
<div class="page" id="contentTable_foot">
	<div class="pagination">
		<dl>
			<dt><input check-action="All" type="checkbox">全选</dt>
			<dt><input check-action="Reverse" type="checkbox">反选</dt>
			<dd>
				<input type="button" class="btn ydbz_x " onclick="batchPrint();" value="批量打印">
			</dd>
		</dl>
	</div>

	<div class="pagination clearFix">
		${page}
	</div>
</div>
 <style type="text/css">
 /*.jbox-title-panel{background: #3a7850 !important;}
div.jbox .jbox-button{background: #3a7850 !important; color:#FFF; font-size:12px;}
.jbox-title{}*/
.orderList_dosome{ text-align:left; margin-left:11px;}
.orderList_line{ height:100%; width:50px;float:left; }
 </style>  
</body>
</html>