<!-- 
author:chenry
describe:订单详情页，订单操作中 团签 功能跳转页面,适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2014-11-04
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>申请退款
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){

	inputTips();
	//退款
	refunds();
	totalRefund();
	changeRefund();
	currencyObj = {};
	loadCurrency();
    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    
	<%-- 前端js效果部分 --%>
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  	
  	$("#allChk").click(function(){
		if($(this).attr('checked') == 'checked'){
			$("input[name='activityId']").attr('checked','checked');
		}else{
			$("input[name='activityId']:checked").removeAttr('checked');
		}
	});
});


function loadCurrency(){
   $.ajax({
		  type: "GET",
		  url: "${ctx}/order/refund/currencyJson",
		  dataType: "json",
		  async: false,
		  success:function(msg){
			  if(msg && msg.currencyList){
			      $.each(msg.currencyList, function(i,n){
			         currencyObj[n.id] = n;
			      });
			  }
		  }
		  
		});
}
//产品名称获得焦点显示隐藏
function inputTips(){
	$("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	$("input[flag=istips]").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}


//验证是否是数值
function formatNum(number){
	return Number(number) ? Number(number) : 0;
}

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = thisvalue.replace(/\D/g,"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}else{
		$(dom).val(0);
	}
}
//数字添加千位符
function milliFormat(s,isFloat){
	var minusSign = false;
	if((typeof s) != String){
		s = s.toString();
	}
	if(/^\-/.test(s)){
		minusSign = true;
		s = s.substring(1);
	}
	if(/[^0-9\.]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	if(isFloat){
		s=s.replace(/,(\d\d)$/,".$1");
	}else{
		s=s.replace(/,(\d\d)$/,"");
	}
	if(minusSign){
		s= '-' + s;
	}
	return s.replace(/^\./,"0.");
}

//团队退款
function refunds(){
	$('.refund-price-btn').click(function() {
		var html = '<li><i><input type="text" name="refundAmount" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" class="gai-price-ipt1" name="refund" data-type="rmb" flag="istips" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this)"/><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
	});
	//删除团队退款一项
	$('.gai-price-ol').on("click",".clear-btn",function(){
		$(this).parents('li').remove();
		totalRefund();
	});
	
//	$('.bgMainRight_right').on("click",".ydbz_s",function(){
//		submitForm();
//	});
	$('.bgMainRight_right').on("click",'.gaijia-delete',function(){
		$(this).parent().parent().parent().remove();
		totalRefund();
		})
	$('.bgMainRight_right').on("click",'.gaijia-add',function(){
		//$(this).parent().parent().parent().remove();
		var addMoney=$(this).parent().parent().parent().find(".tdgreen").text();
		var html='<tr>';
            html+='<td class="refundtd"><input type="text" name="refundAmount"><div class="pr"><i class="gaijia-delete" title="删除款项"></i></div></td>';
			html+='<td class="tc"><select style="width:90%;" class="selectrefund">'+$("#currencyTemplate").html()+'</select></td>';
			html+='<td class="tr"><span class="tdgreen">'+addMoney+'</span></td>';
			html+='<td><input type="text" onkeyup="refundInput(this)" onafterpaste="refundInput(this)" onblur="refundInputs(this)" name="refund" data-type="eur"></td>';
			html+='<td><input type="text" name="remark"></td></tr>';
		$(this).parents('.refundTable').append(html);
		//$(this).parents('tbody').find('td[rowspan]')
		totalRefund();
		});		
}
//订单团队退款
    function refundInput(obj){
		 var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
         var txt = ms.split(".");
         obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
	    totalRefund();
   }
   function refundInputs(obj){
	   objs=obj.value;
	   objs=objs.replace(/^(\d*)$/,"$1.");
	   objs=(objs+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	   objs= objs.replace(/^\./,"0.");
	   $(obj).val(objs);
       $(obj).next("span").hide();
	   
	   }

	function checkForm() {
		var selects = $("select[class='selectrefund']");
		var flag = true;
		selects.each(function(index, element) {
			var si = $(selects[index]);
         	var s = si.val();
            var checkinput = si.parents("tr").find("input[type='checkbox']");
			if (checkinput.prop("checked")) {
				var n = si.parent().parent().find("input[name='refund']").val();
				if (n == "" || n == undefined || n <= 0) {
				   top.$.jBox.tip('已选择游客退款金额不能为空', 'error', { focusId: 'name' });
				   flag= false;
				}
				var remarks = si.parent().parent().find("input[name='remark']").val().replace(/\s+/g,"");
//				if(remarks == ""|| remarks == undefined){
//					top.$.jBox.tip('已选择游客退款备注不能为空', 'error', { focusId: 'name' });
//					flag= false;
//				}
				var reAmount=si.parent().parent().find("input[name='refundAmount']").val().replace(/\s+/g,"");
				if (reAmount == ""|| reAmount == undefined) {
					top.$.jBox.tip('已选择游客退款款项不能为空', 'error', { focusId: 'name' });
					flag= false;
				}
			}
			if (checkinput.length == 0) {
				var reAmount = si.parent().parent().find("input[name='refundAmount']").val().replace(/\s+/g,"");
				var n = si.parent().parent().find("input[name='refund']").val();
				var remarks = si.parent().parent().find("input[name='remark']").val().replace(/\s+/g,"");
	   	   
				if (reAmount == ""|| reAmount == undefined) {
					if (n != "" && n != undefined && parseFloat(n) > 0) {
						top.$.jBox.tip('团队退款请正确填写完整信息！', 'error', { focusId: 'name' });
						flag= false;
					}
				} else {
					if (n == ""|| n == undefined || parseFloat(n) == 0) {
						top.$.jBox.tip('团队退款请正确填写完整信息！', 'error', { focusId: 'name' });
						flag= false;
					}
					if (remarks == ""|| remarks == undefined) {
						top.$.jBox.tip('团队退款请正确填写完整信息！', 'error', { focusId: 'name' });
						flag= false;
					}
				}
			}
	   });
	   return flag;
	}
	   
	   function checkValueSingle(a,b){
	   	var a1=a.sort();
	   	var b1=b.sort();
	   	if(a.length+b.length>0){
		   	if(a.length>0){
		   		for(var i=0;i<a.length;i++){
			   		if(a1[i] == a1[i+1]){
			   			top.$.jBox.tip('请检查已选择游客退款填写的信息！', 'error', { focusId: 'name' });
			   			return false;
			   		}
	   			}
		   	}
	   		if(b.length>0){
		   		for(var i=0;i<b.length;i++){
			   		if(b1[i] == b1[i+1]){
			   			top.$.jBox.tip('请检查已选择团队退款填写的信息！', 'error', { focusId: 'name' });
			   			return false;
			   		}
	   			}
		   	}
		   	return true;
	   	}else{
	   		top.$.jBox.tip('请填写退款内容信息再提交！', 'error', { focusId: 'name' });
	   		return false;
	   	}
	   }
	   
	function submitForm(){
	   var b=checkForm();
	   if(b){
	   var travelerId=[];
	   var refundAmount=[];
	   var currency_id=[];
	   var refund=[];
	   var remark=[];
	   var orderId;
	   var selects=$("select[class='selectrefund']");
	   var checkArray1=[];
	   var checkArray2=[];
	   var objArray=[];
	   var orderTotal=$("#orderTotal").val();
	   selects.each(function(index, element) {
	   		var si = $(selects[index]);
         	var s = si.val();
            var checkinput=si.parents("tr").find("input[type='checkbox']");
	   		if(checkinput.prop("checked")){
				         var obj={};
					     var n = si.parent().parent().find("input[name='refund']").val();					     
					     if(n==""|| n ==undefined){n=0;}else{n=n;}
					     var orderTotalTr=si.parent().parent().find("input[name='orderTotalTr']").val();
					     if(orderTotalTr==""|| orderTotalTr ==undefined){orderTotalTr=0;}else{orderTotalTr=orderTotalTr;}
					     refund.push(n);
					     currency_id.push(s);
					     var remarks = si.parent().parent().find("input[name='remark']").val();
					     remark.push(remarks);
					     var ra=si.parent().parent().find("input[name='refundAmount']").val();
					     refundAmount.push(ra);
					     var tid=si.parents("tr").find("input[name='travelerId']").val();
					     travelerId.push(tid);
					     checkArray1.push(s+ra+tid);
					     
					    obj.travelerId = tid;
			        	obj.travelerName = si.parents("tr").find("input[name='travelerName']").val();
			        	obj.refundName = ra;
			        	obj.currencyId = s;
			        	obj.currencyName = currencyObj[obj.currencyId].currencyName;
			        	obj.currencyMark = currencyObj[obj.currencyId].currencyMark;
			        	obj.payPrice =orderTotalTr;
			        	obj.refundPrice = n;
			        	obj.remark = remarks;
			        	obj.status = 1;
			        	objArray.push(obj);					     				      
			}
			if(checkinput.length==0){
						var obj={};
						 var n = si.parent().parent().find("input[name='refund']").val();
						 var ra=si.parent().parent().find("input[name='refundAmount']").val().replace(/\s+/g,"");	
						 var remarks = si.parent().parent().find("input[name='remark']").val().replace(/\s+/g,"");
						 
						 if((n !="" && n !=undefined &&n >0)&& ra !="" &&ra !=undefined &&remarks !="" && remarks !=undefined){
						 refund.push(n);
					     currency_id.push(s);
					     
					     remark.push(remarks);
					     refundAmount.push(ra);
					     checkArray2.push(s+ra);
					     //formBean拼接
					     obj.travelerId = 0;
			        	obj.travelerName = "团队";
			        	obj.refundName = ra;
			        	obj.currencyId = s;
			        	obj.currencyName = currencyObj[obj.currencyId].currencyName;
			        	obj.currencyMark = currencyObj[obj.currencyId].currencyMark;
			        	obj.payPrice =orderTotal;
			        	obj.refundPrice = n;
			        	obj.remark = remarks;
			        	obj.status = 1;
			        	objArray.push(obj);
						 }				     					     
					     
			}			 			
	   	   
	   });
	   var flag=checkValueSingle(checkArray1,checkArray2);
	   var submitNum = 1;
	   if(flag){
		   if (submitNum == 1) {
			   submitNum++;
			   $.ajax({
	                type: "POST",
	                url: "${ctx}/orderCommon/manage/saveGroupRefundInfo",
	                data: {
	                	refundRecordsStr : JSON.stringify(objArray),
	                    travelerId:travelerId.join(','),
	                    refundAmount :refundAmount.join(','),
	                    currencyId :currency_id.join(','),
	                    refund :refund.join(','),
	                    remark :remark.join(','),
	                    productType:"${productType}",flowType:1,orderId:"${orderId}"
	                    
	                },
	                success: function(msg){
		                if(msg.sign == 1){
				            	window.location.href ="${ctx}/orderCommon/manage/viewGroupRefund?productType=${productType}&&orderId=${orderId}&flowType=1";
				            }else{
				            	submitNum--;
				            	top.$.jBox.tip(msg.result, 'error', { focusId: 'name' });
				            }
		                }
	            });
			}
	   }
	   
	   }	   	   	   	   
	}
	   
	   
	function changeRefund(){
		 $(".bgMainRight_right").delegate("select[class='selectrefund']","change",function(){
		   totalRefund();
		 })
		 $(".table_borderLeftN").delegate("input[type='checkbox']","click",function(){
		   totalRefund();
		 })
		  $(".table_borderLeftN").delegate("input[name='allChk']","click",function(){
		      if($(this).prop("checked")){
			     $(".table_borderLeftN").find("input[type='checkbox']").attr("checked",'true'); totalRefund();
			  }else{
			     $(".table_borderLeftN").find("input[type='checkbox']").removeAttr("checked"); totalRefund();
			  }
		 })
	}
	function totalRefund(){
		 var selects=$("select[class='selectrefund']");
		 var totalcost="";
		 $("#currencyTemplate option").each(function(i, e) {
		    var datatype=$(e).text();
		    e=$(e).val();
			var money = 0;
			selects.each(function(index, element) {
				var si = $(selects[index]);
                var s = si.val();
				var checkinput=si.parents("tr").find("input[type='checkbox']");
				if(checkinput.prop("checked") || checkinput.length==0){
				    if(s == e){
					     var n = si.parent().parent().find("input[name='refund']").val();
					     if(n==""|| n ==undefined){n=0;}else{money += parseFloat(n);}
				    }  
				}			
            });
			if(money!=""||money!=0){
					datatype="<font class='tdgreen'> + </font><font class='gray14'>"+datatype+"</font>";
					money="<span class='tdred'>"+milliFormat(money,'1')+"</span>";
			        totalcost+=datatype+money;
				}
		});
		if(totalcost==0){$('.all-money').find('span').html(0)}else{$('.all-money').find('span').html(totalcost);}
		$('.all-money').find('span').find(".tdgreen").first().hide();
	}

function myTest() {
	
	window.open("${ctx}/orderCommon/manage/viewGroupVisa?id="+1);
}
function checkall(obj){
  if($(obj).attr('checked'=='checked')){
  $("input[name='activityId']").attr('checked','checked');
  
  }else{
  $("input[name='activityId']:checked").removeAttr('checked');
  }
}
</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}

.gaijia-add,.gaijia-delete{
	display:block; width:16px; height:16px;
	position:absolute; top:-16px; right:5px;
	cursor:pointer;
	background:url('${ctxStatic}/images/button_add.gif') no-repeat;
}
.gaijia-delete{
	top:-35px;
	background:url('${ctxStatic}/images/chazi_07.gif') -2px -2px no-repeat;
}
.refundtd input[type="text"]{width:70%}
.refundtd .gaijia-delete,.refundtd .gaijia-add{top:-24px;}

</style>

</head>

<body>
<div id="sea">

	<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    
 <!--右侧内容部分开始-->
 	<div class="bgMainRight_right">
         <div class="ydbzbox fs">
            <div class="orderdetails">
               <%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
               <div class="ydbz_tit">符合退款条件游客信息</div>
               <input type="hidden" id="orderTotal" name="orderTotal"  value="${fns:getMoneyAmountBySerialNum(productOrder.totalMoney,2)}"/>
               <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
               <c:forEach items="${currencyList}" var="cur" varStatus="b">
                    <option value="${cur.id }">${cur.currencyName }</option>
                    </c:forEach>
                </select>
                
				<table id="contentTable" class="activitylist_bodyer_table">
                   <thead>
                        <tr>
                        	<th class="table_borderLeftN" width="5%">全选<input name="allChk" id="allChk" type="checkbox"></th>
                            <th width="5%">游客</th>
                            <th width="20%">退款款项</th>
                            <th width="10%">币种</th>
                            <th width="20%">应收金额</th>
                            <th width="20%">金额</th>
                            <th width="20%">备注</th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty travelerList}">
                    <tr class="toptr" >
					                 <td colspan="7" style="text-align: center;">
										 暂无结果
					                 </td>
								</tr>
                    </c:if>
                     <c:forEach items="${travelerList}" var="orders" varStatus="s">
                     
						<tr>
							<td class="table_borderLeftN" width="5%"><input name="activityId" type="checkbox">
							<input name="travelerId" value="${orders.id }" type="hidden" >
							<input name="travelerName" value="${orders.name }" type="hidden" >
							</td>
							<td><span class="travelerName">
							<c:if test="${orders.delFlag == 2 }">
								<div class="ycq yj" style="margin-top:1px;">
							 		退团审核中
								</div>
							</c:if>
							<c:if test="${orders.delFlag == 3 }">
								<div class="ycq yj" style="margin-top:1px;">
							 		已退团
								</div>
							</c:if>
							<c:if test="${orders.delFlag == 4 }">
								<div class="ycq yj" style="margin-top:1px;">
							 		转团审核中
								</div>
							</c:if>
							<c:if test="${orders.delFlag == 5 }">
								<div class="ycq yj" style="margin-top:1px;">
							 		已转团
								</div>
							</c:if>
							${orders.name }</span></td>
							<td width="90%" colspan="5" class="p0" style="border:none">
							<table class="refundTable" style="border:none" width="100%">
							<tr>
                            <td width="22%" class="refundtd"><input type="text" name="refundAmount" maxlength="128" class="inputTxt"/><div class="pr"><i title="添加款项" class="gaijia-add"></i></div></td>
							<td class="tc" width="11%">
							<select style="width:90%;" class="selectrefund" name="currency">
							 <c:forEach items="${currencyList}" var="cur" varStatus="b">
							 <option value="${cur.id }">${cur.currencyName }</option>
							 </c:forEach>
							</select>
							</td>
							<td class="tr"  width="22%"><span class="tdgreen">${orders.payPrice}</span></td>
							<td width="22%"><input type="text" maxlength="12" name="refund" data-type="eur" onkeyup="refundInput(this)" onafterpaste="refundInput(this))" onblur="refundInputs(this)">
							<input type="hidden" name="orderTotalTr"  value="${orders.payPrice}"/>
							</td>
							<td width="22%"><input type="text" maxlength="128" name="remark"/></td>
							</tr>
                              </table>
                              </td>					
						</tr>
						</c:forEach>
                    </tbody>
                </table>
            </div>
            <c:if test="${groupRefundSign == 0 }">
            	<div class="ydbz_tit">团队退款</div>
                <div>                                               
                 
                   <ol class="gai-price-ol">
                    	<li >
                        	<i><input type="text" name="refundAmount" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">款项</span></i>
                        	<i><select style="width:90%;" class="selectrefund" name="currency">
							 <c:forEach items="${currencyList}" var="cur" varStatus="b">
							 <option value="${cur.id }">${cur.currencyName }</option>
							 </c:forEach>
							</select></i>
                            <i><input type="text" name="refund" data-type="rmb" onkeyup="refundInput(this)" onafterpaste="refundInput(this))"  onblur="refundInputs(this)" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">费用</span></i>
                            <i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>
                            <i><a class="ydbz_s refund-price-btn">+增加</a></i>
                        </li>
                    </ol>
				</div>
            </c:if>
           
				<div class="allzj tr f18">
					<div class="all-money">退款总金额：<!--<font class="gray14">人民币</font><span class="f20 tdred" name="refund1"></span>--><span></span></div>
			</div>
			<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">取消</a><a class="ydbz_s" href="javascript:submitForm()">提交</a></div>
            </div>
          </div>  
    </div>
    <!--右侧内容部分结束--> 
    
</div>
</body>
</html>
