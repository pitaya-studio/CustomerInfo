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
<title>申请切位退款
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
	   
	function submitForm(){
	   var orderId;
	   var objArray=[];
	   var flag=false;
				         var obj={};
					     var n = $("input[name='refund']").val();					     
					     if(n==""|| n ==undefined){n=0;}else{n=n;}
					     var remarks = $("input[name='remark']").val();
					     var ra=$("input[name='refundAmount']").val();
					     
					    obj.travelerId = "${refundInfo.agentId}";
			        	obj.travelerName = "${refundInfo.agentName}";
			        	obj.refundName = ra;
			        	obj.currencyId = "${refundInfo.moneyType}";
			        	obj.currencyName = "${refundInfo.currencyName}";
			        	obj.payPrice ="${refundInfo.payMoney}${refundInfo.currencyName}";
			        	obj.refundPrice = n;
			        	obj.remark = remarks;
			        	obj.status = 1;
			        	objArray.push(obj);					     				      			 			
	   	 if(n!=0&&""!=ra&&undefined!=ra){
	   	 flag=true;
	   	 }   
	   	 if(flag){
		   	 $.ajax({
	                type: "POST",
	                url: "${ctx}/stock/manager/apartGroup/saveRefundInfo",
	                data: {
	                	refundRecordsStr : JSON.stringify(objArray),
	                    
	                    productType:9,flowType:1,orderId:"${orderId}"
	                    
	                },
	                success: function(msg){
	                	if(msg.sign == 1){
			            	window.location.href ="${ctx}/stock/manager/apartGroup/viewGroupRefund?orderId=${orderId}";
			            }else{
			            	top.$.jBox.tip(msg.result, 'error', { focusId: 'name' });
			            }
	                }
	            }); 
	   	 }else{
	   	 
	   	 	top.$.jBox.tip('填写错误!请正确填写位切退款信息！', 'error', { focusId: 'name' });
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
		 var totalcost="";
		 var money=0;
		 var n = $("input[name='refund']").val();
					     if(n==""|| n ==undefined){n=0;}else{money += parseFloat(n);}
		if(money!=""||money!=0){
					datatype="<font class='tdgreen'> + </font><font class='gray14'>${refundInfo.currencyName}</font>";
					money="<span class='tdred'>"+milliFormat(money,'1')+"</span>";
			        totalcost+=datatype+money;
				}			     
		if(totalcost==0){$('.all-money').find('span').html(0)}else{$('.all-money').find('span').html(totalcost);}
		$('.all-money').find('span').find(".tdgreen").first().hide();
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
               <div class="mod_nav">
散拼切位
&nbsp;&nbsp;>申请退款</div>
               <div class="ydbz_tit">产品信息</div>
               <div class="orderdetails2">
                  <p class="ydbz_mc">${refundInfo.acitivityName}</p>
                 	<ul class="ydbz_info">
                     <li><span>出发城市：</span>${refundInfo.getFromAreaName()}</li>
                     <li><span>出团日期：</span><fmt:formatDate value="${refundInfo.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
                     <li><span>行程天数：</span>${product.activityDuration}</li>
          <!--           <li><span>离境城市：</span>2014-09-10</li>  -->  
                     <li id="mddtargetAreaNames" title="${refundInfo.areaName}" class="orderdetails2_text"><span >目的地：</span>${refundInfo.areaName}</li>
                    
                     
                  </ul>
               </div>
               <div class="ydbz_tit">切位退款</div>
                
				<table id="contentTable" class="activitylist_bodyer_table">
                   <thead>
                        <tr>                        	
                            <th width="5%">渠道</th>
                            <th width="20%">退款款项</th>
                            <th width="10%">币种</th>
                            <th width="20%">应收金额</th>
                            <th width="20%">金额</th>
                            <th width="20%">备注</th>
                        </tr>
                    </thead>
                    <tbody>
                     
						<tr>
							<td><span class="travelerName">${refundInfo.agentName}</span></td>
							<td width="90%" colspan="5" class="p0" style="border:none">
							<table class="refundTable" style="border:none" width="100%">
							<tr>
                            <td width="22%" class="refundtd"><input type="text" name="refundAmount" class="inputTxt"/></td>
							<td class="tc" width="11%">
							${refundInfo.currencyName}
							</td>
							<td class="tr"  width="22%"><span class="tdgreen">${refundInfo.payMoney}${refundInfo.currencyName}
							
							</span></td>
							<td width="22%"><input type="text" name="refund" data-type="eur" onkeyup="refundInput(this)" onafterpaste="refundInput(this))" onblur="refundInputs(this)"></td>
							<td width="22%"><input type="text" name="remark"/></td>
							</tr>
                              </table>
                              </td>					
						</tr>
                    </tbody>
                </table>
            </div>
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
