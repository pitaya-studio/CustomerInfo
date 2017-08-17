<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>其它收入录入</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link
	href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css"
	type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><!--<link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />-->
<script src="${ctxStatic}/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/css/jh-style.css?ver=1" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic }/json/json2.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic }/common/common.js" type="text/javascript" ></script>
<style type="text/css">
/*#supplier .custom-combobox {width:130px;height:36px;display:inline-block;} */
#supply {
	height: 40px;
}

.custom-combobox .custom-combobox-input {
	height: 26px;
}

.custom-combobox .custom-combobox-toggle {
	vertical-align: top;
}
</style>

<script type="text/javascript">
	var regneg =/^(-)[0-9]*(\.[0-9]{0,2})?$/;
	$(function() {
		//搜索条件筛选 
		$("#agentId" ).comboboxInquiry();
		$("#supplier").comboboxInquiry();
	
		$("#second .custom-combobox").on("autocompleteselect",function(){
			getbank(arguments[1].item.option.value);
		});
	
		$("#secondagent .custom-combobox").on("autocompleteselect",function(){
			getbank(arguments[1].item.option.value);
		});

	});

	function submitForm(review){
     $("#review").val(review);
      formSubmit(); 
    } 

	function multiply(arg1,arg2) {
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
	}

	function formSubmit() {
		var agentId =$("#agentId").val();
		var first=$("#first").val();
		var supplier=$("#supplier").val();
		var itemname= $("#itemname").val();
		var currencyId =$("#currencyId").val();
		var quantity = $("#quantity").val();
		var price =  $("#price").val();
		var comment = $("#comment").val();
		var rate = $("#rate").val();
		var bank=$('#bank').val(); 
		var bankname = $("#bankname").val();
		var account=$('#account').val(); 

		var commentLength = comment.length;

		var reg =/^[0-9]*(\.[0-9]{0,2})?$/;
	
		var supplyType = $('input[name="supplyType"]').filter(':checked');
		if(! supplyType.length) {
			alert('请选择客户类别'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}
		supplyType=supplyType.val();


		if(supplyType=="1"){
			if(agentId==""){
			alert('请选择渠道商'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}else {
			supplyName=$("#agentId").find("option:selected").text();
			supplier=agentId;
		}
	}
			
		if(supplyType=="0" ){
			 if(first=="0" ){
			 alert('请选择供应商类型'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			} else if (supplier=="0"||supplier=="") {
			 alert('请选择地接社类型'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			} else {
			supplyName=$("#supplier").find("option:selected").text();
			}
		}

		if(itemname==""){
			alert('项目名称不能为空'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(currencyId=="0"){
			alert('请选择单价币种'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(price==""){
			alert('请输入单价'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		var regu = /^[-]{0,1}[0-9]{1,}$/; 
		if(quantity==""|| !regu.test(quantity)){
			alert('数量请输入数量'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(bank== -1){
			if(bankname==""){
				alert('请输入汇款行名称'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			}
			if(account==""){
				alert('请输入汇款行账户'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			}
		}

		if(bank==0){
			alert('选择银行账号'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(rate==""){
			alert('请输入汇率'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(parseInt(rate)>parseInt(9999)){
			alert('汇率不能大于9999'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}
		
		var priceAfter=$("#priceAfter").val();
		if(price > 9999999999.99 || quantity.length > 8 || priceAfter > 9999999999.99) {
			alert("单价或转换后总价值过大，请修改");
			$("#save").attr('disabled',false);
			$("#saveto").attr('disabled',false);
			return false;
		}
		if(commentLength > 999){
			alert('项目备注不能大于1000 字符'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		} else {
			var priceAfter=multiply(quantity,price);
			var priceAfter=multiply(priceAfter,rate);
			$("#priceAfter").val((Math.round(priceAfter*100)/100).toFixed(2));

			if(!reg.test(price) && !regneg.test(price)){
				alert('项目金额最多有2位小数,不能有字母');
				$("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			}else{
				$("#costForm").submit(); 
			}
		}
	}

	function tosales() {
		$("#sales").show();
		$("#supply").hide();
		$("#supplyid").hide();
		$("#kbRadio").hide();
		$('#bank').empty();
		options="<option value=-1>请先选择渠道商</option>";
		$('#bank').append(options);
	}

	function tosupply(){
		$("#sales").hide();
		$("#supply").show();
		$("#supplyid").show();
		$("#kbRadio").show();
		$('#bank').empty();
		options="<option value=-1>请先选择地接社</option>";
		$('#bank').append(options);
	}

	function selectchange(parentId) {
		$('#supplier').empty();
		$('#second input').val('');
		$('#bank').empty();
		if(parentId){
			var noCache = new Date();
			$.getJSON("${ctx}/cost/manager/supplylist/"+parentId,{"noCache":noCache},function(myJSON){
				var options="";
				if(myJSON.length>0){
					for(var i=0;i<myJSON.length;i++){
						$('#supplier').append("<option value='"+myJSON[i].supplierid+"'>"+myJSON[i].suppliername+"</option>");
					}
					$("#second").show();
				} else if(myJSON.length<=0){
					options="<option value=0>没有记录</option>";
					$('#supplier').append(options);
					$("#second").show();
				}
			});
		}
	}
</script>

</head>

<body>
	<form:form name="frm" id="costForm" modelAttribute="sysdefinedict"
		action="${ctx}/costReview/activity/saveHQX" target="_parent" method="post">

		<input type="hidden" id="activityId" name="activityId" value="${activityId}" />
		<input type="hidden" id="groupId" name="groupId" value="${groupId}" />
		<input type="hidden" id="typeId" name="typeId" value="${typeId}" />
		<input type="hidden" id="budgetType" name="budgetType" value="${budgetType}" />
		<input type="hidden" id="review" name="review" />
		<input  type="hidden" id="deptId" name="deptId" value="${deptId}" />
		<br>

		<div id="moneyId" style=" margin:10px;">
			<table border="0" width="96%">
				<tr style="height:35px;">
					<td align="right">客户类别：</td>
					<td>
						<input type="radio" name="supplyType" id="supplyType" value="0" checked onClick="tosupply()" />地接社 &nbsp;&nbsp;
						<input type="radio" name="supplyType" id="supplyType" value="1" onClick="tosales()" />渠道商
					</td>
				</tr>

					<tr id="sales" style="height:45px;display:none">
						<td align="right">渠道商选择：</td>
						<td><span id="secondagent"> <select name="agentId"
								id="agentId" onchange="selectbank(this.value,1)" style="width:180px">
									<option value="">===请选择===</option>
									<c:choose>
										<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1" >未签</option></c:when>
										<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' }"><option value="-1" >直客</option></c:when>
										<c:otherwise><option value="-1" >非签约渠道</option></c:otherwise>
									</c:choose>
									<c:forEach var="agentinfo" items="${agentinfo }">
										<option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}</option>
									</c:forEach>
							</select> </span></td>
					</tr>

					<tr id="supplyid">
						<td align="right">地接社类型：</td>
						<td><select style="width:180px"
							onchange="selectchange(this.value)" id="first" name="first">
								<option value="" >===请选择===</option>
								<c:forEach items="${supplytypelist}" var="dict">
									<option value="${dict.value}">${dict.label}</option>
								</c:forEach>
							</select>
						</td>
					</tr>

				<tr id="supply">
					<td align="right">地接社选择：</td>
					<td>
						<span id="second">
							<select id="supplier" name="supplier" onchange="selectbank()" style="width:180px">
								<option value="0" selected>请选择</option>
							</select>
						</span>
					</td>
				</tr>

				<tr>
					<td align="right">开户银行：</td>
					<td>
						<select id="bank" name="bank" onchange="inputbank()" style="width:180px">
							<option value="0" selected>请选择</option>
						</select>
					</td>
				</tr>

				<tr id="mybankname" style="display:none">
					<td align="right">汇款行名称：</td>
					<td><input name="bankname" id="bankname" type="text" style="width:180px" /></td>
				</tr>

				<tr id="myaccount" style="display:none">
					<td align="right">汇款行账号：</td>
					<td><input name="account" id="account" type="text" style="width:180px" /></td>
				</tr>

				<tr>
					<td align="right">项目名称：</td>
					<td><input type="text" name="itemname" id="itemname" maxlength="50" style="width:180px" /></td>
				</tr>
				
				<shiro:hasPermission name="${permission}">	
					<tr>
						<td align="right">选择订单：</td>
						<td> 
							<select id="selectorder" name="selectorder" style="width:180px" onchange="selectOrder(this.value, this.options[this.selectedIndex].text)">
								<option value="-1" >请选择</option>
								<c:forEach items="${orders}" var="order">
									<option value="${order.orderId}">${order.orderNo}</option>
								</c:forEach>
							</select>  
						</td>
					</tr>
		 		</shiro:hasPermission>
				
				<c:if test="${companyUuid eq TMYT }">
					<tr id="kbRadio" style="height:35px;">
						<td align="right">是否为kb款：</td>
						<td>
							<input type="radio" name="kb" id="kb" value="1" />&nbsp;&nbsp;是 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="radio" name="kb" id="kb" value="0" checked />&nbsp;&nbsp;否
						</td>
					</tr>
				</c:if>

				<input type="hidden" name="quantity" id="quantity" value="1" onkeyup="changeprice()" style="width:180px" />
							
				<tr>
					<td align="right">转换前单价：</td>
					<td>
						<select style="width:75px; margin-right:5px;" id="currencyId" name="currencyId" onchange="changeprice()">
							<c:forEach items="${curlist}" var="currency">
								<option value="${currency.id}" <c:if test="${currency.currencyName eq '人民币' }">selected</c:if>>${currency.currencyName}</option>
							</c:forEach>
						</select><input type="text" name="price" id="price" onkeyup="changeprice()" style="width:100px" maxlength="11"/>
					</td>
				</tr>

				<tr>
					<td align="right">转换后币种：</td>
					<td>RMB &nbsp; 
						<select style="width:75px; margin-right:5px;display:none" id="currencyAfter" name="currencyAfter" onchange="changeprice()">
							<c:forEach items="${curlist}" var="currency">
								<c:if test="${currency.currencyName=='人民币'}">
									<option value="${currency.id}" selected>${currency.currencyName}</option>
								</c:if>
							</c:forEach>
						</select>&nbsp; 汇率：<input type="text" name="rate" id="rate" maxlength="6" <c:if test="${fn:contains(companyName, '越柬行踪') }">readonly="readonly"</c:if>
							onkeyup="changerate()" style="width:98px" />
					</td>
				</tr>

				<tr>
					<td align="right">转换后总价：</td>
					<td>
						<span id="costfinal"><input type="text" name="priceAfter" id="priceAfter" readonly style="width:180px" />
						</span>
					</td>
				</tr>

				<tr>
					<td align="right">项目备注：</td>
					<td>
						<textarea rows="3" cols="20" id="comment" name="comment" maxlength="1000" style="width:180px"></textarea>
					</td>
				</tr>
			</table>
		</div>

		<div class="release_next_add">
			<input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()" />
			<c:if test="${companyId !=68 || (companyId ==68  && budgetType == 2)}">
				<input type="button" class="btn btn-primary" onClick="this.disabled=true;submitForm('4')" id="save" value="提交" />
			</c:if>
		</div>
	</form:form>

</body>
<script language="javascript">

function SelectTips(flag){
	alert($('#supplier').val());
}

function inputbank(){
	var bank=$('#bank').val(); 
	if(bank== -1){
		$("#mybankname").show();
		$("#myaccount").show();
	}else{
		$("#mybankname").hide();
		$("#myaccount").hide();
	}
}

function selectOrder(value, orderNum) {
	if(value != -1){
		$("#comment").val(orderNum);
	}else {
		$("#comment").val("");
	}	
}

function getbank(supplyid){ 
	$('#bank').empty();
	$('#account').empty();
	var supplyType = $('input[name="supplyType"]').filter(':checked').val();
	if (supplyid=="") return;
	var noCache = Date();
	if (supplyType=="0") supplyType=1;
	else if (supplyType=="1") supplyType=2;
	$.getJSON("${ctx}/cost/manager/banklist/"+supplyType+"/"+supplyid,{"noCache":noCache},function(myJSON){
		var options="";	
		if(myJSON.length>0){
			options+="<option value=0>请选择....</option><option value=-1>录入新银行账号</option>";
			$('#bank').append(options);
			for(var i=0;i<myJSON.length;i++){
				$('#bank').append("<option value='"+myJSON[i].bankid+"'>"+myJSON[i].bankname+", 账号："+myJSON[i].account+"</option>");
			}
			$("#mybankname").hide();
			$("#myaccount").hide();
		} else if(myJSON.length<=0){
			options="<option value=-1>没有银行账号记录</option>";
			$('#bank').append(options);
			$("#mybankname").show();
			$("#myaccount").show();
		}
	});
}

function  trim(str){
	for(var i = 0 ;i<str.length && str.charAt(i)=="  "; i++);
	for(var j =str.length;  j>0 && str.charAt(j-1)=="  ";j--);
	if(i>j)  return  "";
	return  str.substring(i,j);
}

function changeprice(){
	var price=trim($('#price').val());
	var quantity=trim($('#quantity').val());
	var currencyId=$('#currencyId').val();

	if(currencyId=="") {
		alert("请选择转换前单价币种");
		return;
	}

	if(quantity=="") {
		alert("请输入数量");
		return;
	}
	var regu = /^[-]{0,1}[0-9]{1,}$/;
	if(!regu.test(quantity)){
		alert('数量请输入整数');
		return false;
	}
	var reg =/^[0-9]*(\.[0-9]{0,2})?$/;
	if(!reg.test(price) && !regneg.test(price)){
		alert('项目单价最多有2位小数,不能有字母');
		return false;
	}

	var noCache = Date();
	$.getJSON("${ctx}/cost/manager/changecurrency/"+currencyId, {"noCache":noCache},function(myJSON){
		var options="";
		if(myJSON.length>0){
			for(var i=0;i<myJSON.length;i++){
				var rate=myJSON[i].rate;
				$("#rate").val(myJSON[i].rate);
				if (rate!="") {
					var priceAfter=multiply(quantity,price);
					var priceAfter=multiply(priceAfter,rate);
					$("#priceAfter").val((Math.round(priceAfter*100)/100).toFixed(2));
				}
			}
		} else if(myJSON.length<=0){
			return;
		}
	});
}

function changerate() { 
	var price=trim($('#price').val());
	var quantity=trim($('#quantity').val());
	var currencyId=$('#currencyId').val();
	var rate=trim($('#rate').val());

	if(currencyId=="") {
		alert("请选择转换前单价币种");
		return;
	}

	if(quantity=="") {
		alert("请输入数量");
		return;
	}

	if(rate=="") {
		alert("请输入汇率");
		return;
	}

	var reg =/^[0-9]*(\.[0-9]{0,2})?$/;   
	if(!reg.test(price) && !regneg.test(price)){
		alert('项目单价最多有2位小数,不能有字母');
		return false;
	}
	var reg =/^[0-9]*(\.[0-9]{0,4})?$/;
	if(!reg.test(rate)){
		alert('汇率不能出现字母');
		return false;
	}
	var currencyAfter=$("#currencyAfter").find("option:selected").text();
	var priceAfter=multiply(quantity,price);
	var priceAfter=multiply(priceAfter,rate);
	$("#priceAfter").val((Math.round(priceAfter*100)/100).toFixed(2));
	return; 
}

</script>
</html>
