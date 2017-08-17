<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js" type="text/javascript"></script>

<script src="${ctxStatic}/modules/order/payedConfirm.js" type="text/javascript"></script>

  
<script type="text/javascript">

$(document).ready(function(){
	
	//驳回类型标识，0-订单收款驳回操作，1-签证押金收款驳回操作
	var sign = $("#sign").val();
	if(sign == "0"){
		$("#orderDiv").show();
		$("#visaDiv").hide();
	}
	
	if(sign == "1"){
		$("#orderDiv").hide();
		$("#visaDiv").show();
	}
	
});
</script>
<style>
.innerDiv{margin-top: 40px;font-size:13px;margin-left: 65px;}
#innerTable{margin-top: 25px;margin-left: 25px;}
.trtextaling{font-size:13px;text-align: center;margin-left: 105px;}
.trtextaling input{width:25px;}
</style>

<div>

	<input type="hidden"  name="orderPayId"  id="orderPayId"  value="${orderpay.id}"/>
    <input type="hidden"  name="payType"  id="payType" value="${orderpay.payType }"/>
    <input type="hidden" name="sign" id="sign" value="${sign}" />
    
    <div id="orderDiv">
	  	<div class="innerDiv">请您确认对此笔款项支付的驳回操作是否影响占位？</div>    
		<table id="innerTable" width="90%" cellpadding="5" cellspacing="0" border="0">
			<tbody>
		        <tr>
		            <td class="trtextaling">
		            	<input type="radio" name="rejectRadio" value="1" />退回占位
		            </td>
		            <td class="trtextaling">
		            	<input type="radio" name="rejectRadio" value="0" />保持占位 	
		            </td>
	             </tr>
			</tbody>
		</table>
	</div>

	<div id="visaDiv">
		<div class="innerDiv">请您确认是否对此笔款项支付记录做成的驳回操作？<br/>驳回后销售将需要重新支付！</div> 
	</div>

	<div id="reject" class="textarea pr wpr20" style="margin-top:20px;margin-left:40px">
		<label>备注：</label>
		<textarea style="width:298px;" id="reason" name="reason"  class="textarea_long" rows="3" cols="30" onkeyup="this.value=this.value.replaceForChar()" onafterpaste="this.value=this.value.replaceForChar()"> </textarea>
	</div>
</div>
