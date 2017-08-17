<!-- 
author:chenry
describe:订单详情页，订单操作中 团签 功能跳转页面,适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2014-11-03
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>申请退团
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
	var groupCodeVal = $("#groupCodeEle").text();
	if(groupCodeVal.length > 20) {
		groupCodeVal = groupCodeVal.substring(0, 20) + "...";
	}
	$("#groupCodeEle").text(groupCodeVal);	
	
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
function submitForm(){
	//扣减金额不能为空
	var flag = true;
	$("input[name='activityId']:checked").each(function(index, obj) {
		var subtractMoney = $($(this).parent().parent()).find("input[name='subtractMoney']").val();
		if (!subtractMoney || subtractMoney == "" || subtractMoney == null) {
			flag = false;
			return false;
		}
	});
	if (!flag) {
		jBox.tip("退团后应收金额不能为空", 'error');
		return;
	}
var str=[];
var travelerId=[];
var travelerName=[];
var subtractMoneyArr = [];	//扣减金额
var flag=true;
 $("input[name='activityId']:checked").each(function(){
   var exitReason =$($(this).parent().parent()).find("input[name='exitReason']").val().replace(/\s+/g,"");
   var travelerIds=$($(this).parent().parent()).find("input[name='travelerId']").val();
   var travelerNames=$($(this).parent().parent()).find("input[name='travelerName']").val();
   if(undefined==exitReason||""==exitReason){
   exitReason=" ";
   }
   str.push(exitReason);
   travelerId.push(travelerIds);
   travelerName.push(travelerNames);
   //扣减金额处理
   var subtractMoney=$($(this).parent().parent()).find("input[name='subtractMoney']").val();
   if(subtractMoney && subtractMoney != "" && subtractMoney != null){
	   var subtractCurrency=$($(this).parent().parent()).find("select[name='subtractCurrency']").find("option:selected").val();
	   var travelId = $($(this).parent().parent()).find("input[name='travelerId']").val();
	   subtractMoneyArr.push(travelId + "#" + subtractCurrency + "#" + subtractMoney);
   }
 });
 if(flag){
	 if(str.length>0){
	/**	 $.ajax({
		            type: "POST",
		            url: "${ctx}/orderCommon/manage/saveExitGroupInfo",
		            data: {
		            exitReason:str.join(','),travelerId:travelerId.join(','),travelerName:travelerName.join(','),
		            productType:"${productType}",flowType:"${flowType}",orderId:"${orderId}"
		            
		            },
		            success: function(msg){
		            if(msg.result == 1){
		            	window.location.href ="${ctx}/orderCommon/manage/viewExitGroup?orderId=${orderId}&productType=${productType}";
		            }else{
		            	top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
		            }
		            
		            }
		        });*/
		        
		$.ajax({
		            type: "POST",
		            url: "${ctx}/orderCommon/manage/checkExitGroupInfo",
		            data: {		            
		            orderType:"${productType}",flowType:"${flowType}",orderId:"${orderId}",
		            travelerId:travelerId.join(','),travelerName:travelerName.join(',')
		            },
		            success: function(msg){
		            if(msg.sign == 0){
		            	saveExitGroupInfo(str,travelerId,travelerName,subtractMoneyArr);
		            }else if(msg.sign == 1){
		            	 top.$.jBox.tip(msg.message+"您不能提交审核申请！", 'error', { focusId: 'name' });
		            }else if(msg.sign == 2){
						top.$.jBox.confirm("警告："+msg.message + "确定要取消以上流程，并发起退团审核流程？",'系统提示',function(v){
						if(v=='ok'){
							saveCommonExitGroupInfo(str,travelerId,travelerName);
							}
						},{buttonsFocus:1});
						top.$('.jbox-body .jbox-icon').css('top','55px');
						return false;
		            }
		            
		            }
		        });        
		          
	 }else{
	 top.$.jBox.tip('请选择需退团的游客！', 'error', { focusId: 'name' });
	 }
 
 
 }
		

}

		
function checkall(obj){
  if($(obj).attr('checked'=='checked')){
  $("input[name='activityId']").attr('checked','checked');
  
  }else{
  $("input[name='activityId']:checked").removeAttr('checked');
  }
}
/** 验证流程互斥情况，只有当无互斥流程及其他流程性操作时，发起退团审核流程，否则返回错误信息*/
function saveExitGroupInfo(str,travelerId,travelerName,subtractMoneyArr){
	$.ajax({
				type: "POST",
				url: "${ctx}/orderCommon/manage/saveExitGroupInfo",
				data: {
					exitReason:str.join(','),
					travelerId:travelerId.join(','),
					travelerName:travelerName.join(','),
					subtractMoneyArr:subtractMoneyArr.join(','),
					productType:"${productType}",
					flowType:"${flowType}",
					orderId:"${orderId}"
				},
				success: function(msg){
				if(msg.result == 2){
				     window.location.href ="${ctx}/orderCommon/manage/viewExitGroup?orderId=${orderId}&productType=${productType}";
				}else{
				     top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
				     }
				            
				   }
			});
}
/** 无验证互斥情况，先取消该订单所有与退团流程互斥或相关的流程，然后发起退团审核流程*/
function saveCommonExitGroupInfo(str,travelerId,travelerName){
	$.ajax({
				type: "POST",
				url: "${ctx}/orderCommon/manage/saveCommonExitGroupInfo",
				data: {
				exitReason:str.join(','),travelerId:travelerId.join(','),travelerName:travelerName.join(','),
				productType:"${productType}",flowType:"${flowType}",orderId:"${orderId}"
				            
				},
				success: function(msg){
					if( msg.result == 2){
					     window.location.href ="${ctx}/orderCommon/manage/viewExitGroup?orderId=${orderId}&productType=${productType}";
					}else{
						top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
					}
					            
				}
			});
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
</style>

</head>

<body>
<div id="sea">

	<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    
 <!--右侧内容部分开始-->
         <div class="ydbzbox fs">
            <div class="orderdetails">

               <%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
               <div class="ydbz_tit">申请退团</div>
               <form id="submitForm" method="post" >
				<table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            <th class="table_borderLeftN" width="5%">全选<input name="allChk" id="allChk" type="checkbox"></th>
                            <th width="10%">游客</th>
                            <th width="15%">下单时间</th>
                            <th width="15%">应收金额</th>
                            <th width="15%">退团后应收</th>
                            <th width="15%">退团原因</th>
                        </tr>
                    </thead>
                    
                    <tbody>
                    <c:forEach items="${travelerList}" var="tra" varStatus="s">
                    
						<tr>
							<td class="table_borderLeftN"><input name="activityId" type="checkbox"></td>
							<td>
							<c:if test="${tra.delFlag == 4 }">
								<div class="ycq yj" style="margin-top:1px;">
							 		转团审核中
								</div>
							</c:if>${tra.name}
							
							</td>
							<td><fmt:formatDate value="${tra.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
							<td class="tr"><span class="tdred">${tra.payPrice}</span></td>
							<td class="tr">
								<select name="subtractCurrency" class="selectrefund" style="width: 80px">
									<c:set value="${tra.currencyId }" var="tempCurrencyId"></c:set>
									<c:forEach items="${fn:split(tempCurrencyId , ',')  }" var="item">
										<option value="${item }"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_name" value="${item }"/></option>
									</c:forEach>
								</select>
								<input type="text" name="subtractMoney" maxlength="10" onkeyup="refundInput(this)" onafterpast="refundInput(this)" value="0" style="width: 100px">
							</td>
							<td><input type="text" name="exitReason" maxlength="100"/>
							<input name="travelerId"value="${tra.id}" type="hidden">
							<input name="travelerName"value="${tra.name}" type="hidden">
							</td>					
						</tr>
						</c:forEach>
                    </tbody>
                    
                </table></form>
            </div>
           <div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a><a class="ydbz_s" href="javascript:submitForm()">提交</a></div>
            </div>
            
    </div>
    <!--右侧内容部分结束--> 
    
</div>
</body>
</html>
