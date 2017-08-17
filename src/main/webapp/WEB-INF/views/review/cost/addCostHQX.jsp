<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.trekiz.admin.common.config.Global"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page language="java" pageEncoding="utf-8" import="org.apache.shiro.subject.Subject,org.apache.shiro.session.Session,org.apache.shiro.SecurityUtils" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>成本录入</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" /><meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
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
<script src="${ctxStatic }/json/json2.js" type="text/javascript" ></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript" ></script>
<script src="${ctxStatic }/common/common.js" type="text/javascript" ></script>
<script src="${ctxStatic}/modules/cost/cost.js" type="text/javascript"></script>
<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet" />

<style type="text/css">
	/*#supplier .custom-combobox {width:130px;height:36px;display:inline-block;} */
	#supply{ height:40px; }
	.custom-combobox .custom-combobox-input{height:26px; }
	.custom-combobox .custom-combobox-toggle{vertical-align: top;}
</style>

<script type="text/javascript">
	var regneg =/^(-)[0-9]*(\.[0-9]{0,2})?$/;
	$(function() {
		//搜索条件筛选 
		$("#agentId" ).comboboxInquiry();
		if(${companyId=="68"}){
// 			selectchange('2');
		} else {
// 			selectchange('1');
		} 
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
		// bug 12324,12325,12327,12328,12329,12330,12332,12333 update by shijun.liu 2016.02.15
		if('4' == review){
			$("#saveto").attr('disabled',true); 
		}else if('1' == review){
			$("#save").attr('disabled',true);
			var budgetType = $("#budgetType").val();
			if(${companyUuid eq DHJQ} && budgetType == "1") {
				if($(".uploadPath").size() > 0 && $(".uploadPath").children().size() > 0) {

				}else{
					alert("请上传附件后重新提交");
					$("#saveto").attr('disabled',false);
					$("#save").attr('disabled',false);
					return;
				}
			}

		}
		formSubmit();
	} 

	function multiply(arg1,arg2) {
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length;}catch(e){}
		try{m+=s2.split(".")[1].length;}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
	}

	function formSubmit() {
		var overseas =$("#overseas").val();
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

		//var reg = /^[\-\+]?\d+$/;
		var reg =/^[0-9]*(\.[0-9]{0,2})?$/;
	
		if(overseas==""){
			alert('请选择境内外项目'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		var supplyType = $('input[name="supplyType"]').filter(':checked');
		if(!supplyType.length) {
			alert('请选择客户类别'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}
		supplyType=supplyType.val();

		if(supplyType=="1"){
			if(agentId==""){
				alert('请选择渠道商'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			}else {
				supplyName=$("#agentId").find("option:selected").text();
				supplier=agentId;
			};
		}
		if($("#itemnameselect").val()==""){
			$("#spanCheck").text("必填项");
			alert('请选择项目名称'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}else{
			
		}	
		if(supplyType=="0"){
			if(first=="0" || first==""){
				alert('请选择地接社类型'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			} else if (supplier=="0"||supplier=="") {
				alert('请选择供用商'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
			} else {
				supplyName=$("#supplier").find("option:selected").text();
			};
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
			
		if(bank==0){
			alert('选择银行账号'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(rate==""){
			alert('请输入汇率'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(parseInt(rate)>parseInt(9999)){
			alert('汇率不能大于9999'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		}

		if(commentLength > 999){
			alert('项目备注不能大于1000 字符'); $("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false;
		} else {
			var priceAfter=multiply(quantity,price);
			var priceAfter=multiply(priceAfter,rate);
			//$("#priceAfter").val(priceAfter);
			$("#priceAfter").val((Math.round(priceAfter*100)/100).toFixed(2));

			if(!reg.test(price) && !regneg.test(price)){
				alert('项目金额最多有2位小数,不能有字母');
				$("#save").attr('disabled',false); $("#saveto").attr('disabled',false);return false; 
			}else{
				$("#costForm").submit();
			};
		};
	}

	function tosales(){
		$("#sales").show();
		$("#supply").hide();
		$("#supplyid").hide();
		$('#bank').empty();
		options="<option value=-1>请先选择渠道商</option>";
		$('#bank').append(options);
	}

	function tosupply(){
		$("#sales").hide();
		$("#supply").show();
		$("#supplyid").show();
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
					options+="<option value=0>请选择类型</option>"; 
					for(var i=0;i<myJSON.length;i++){
						$('#supplier').append("<option value='"+myJSON[i].supplierid+"'>"+myJSON[i].suppliername+"</option>");
					}
					//$('#supplier').append(options); 
					$("#second").show(); 
				} else if(myJSON.length<=0){  
					options="<option value=0>没有记录</option>"; 
					$('#supplier').append(options);
					$("#second").show();
				}
			});
		}
	}
	
	function checkSelect(){
		if($("#itemnameselect").val()==""){
			$("#spanCheck").text("必填项");
		}else{
			$("#spanCheck").text("");
		}
	}
</script>
</head>

	<body>
		<form:form name="frm" id="costForm" modelAttribute="sysdefinedict" action="${ctx}/costReview/activity/saveHQX" target="_parent" method="post">
			<input type="hidden" id="activityId" name="activityId" value="${activityId}" />
			<input type="hidden" id="groupId" name="groupId" value="${groupId}" />
			<input type="hidden" id="typeId" name="typeId" value="${typeId}" />
			<input type="hidden" id="budgetType" name="budgetType" value="${budgetType}" />
			<input type="hidden"  id="review" name="review" />
			<input type="hidden" id="deptId" name="deptId" value="${deptId}" />
			<br>

			<div id="moneyId" style=" margin:10px;">
				<table border="0" width="96%">
					<tr>
						<td align="right">项目选择：</td>
						<td>
							<select name="overseas" id="overseas" style="width:180px">  
							<option value=""selected="selected">请选择</option>  
							<option value="0">境内明细</option>  
							<option value="1">境外明细</option></select>  
						</td>
					</tr>
					<tr style="height:30px;"><td align="right">客户类别：</td><td>  
						<input type="radio" name="supplyType" id="supplyType"  value="0" checked onClick="tosupply()" />地接社 &nbsp;&nbsp;  
						<input type="radio" name="supplyType" id="supplyType"  value="1"  onClick="tosales()"/>渠道商</td>
					</tr>
					<tr id="sales" style="height:45px;display:none">
						<td align="right">渠道商选择：</td>
						<td>  
							<span id="secondagent">
								<select name="agentId" id="agentId"  onchange="selectbank(this.value,1)" style="width:180px">
									<option value="" >===请选择===</option>
									<c:choose>
										   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
										       <option value="-1" >直客</option>
										   </c:when>
										   <c:otherwise>
										       <option value="-1" >非签约渠道</option>
										   </c:otherwise>
									</c:choose>
									<c:forEach var="agentinfo" items="${agentinfo }">
										 
										<option value="${agentinfo.id }" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
										
									</c:forEach>
								</select>
							</span>	
						</td>
					</tr>

					<tr id="supplyid" >
						<td align="right">地接社类型：</td>
			 			<td> 
							<select style="width:180px" onchange="selectchange(this.value)" id="first" name="first">
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
								<select id="supplier" name="supplier" onchange="selectbank()" style="width:180px" >
									<option value="0" selected>请选择</option>	
								</select>
							</span> 
						</td>
					</tr> 

					<tr>
						<td align="right">开户银行：</td>
						<td>
							<select id="bank" name="bank"  onchange="inputbank()"  style="width:180px" >  
								<option value="0" selected>请选择</option>
							</select> 
						</td>
					</tr>	

					<tr id="mybankname" style="display:none">
						<td align="right">汇款行名称：</td>
						<td>  
							<input name="bankname" id="bankname" type="text" style="width:180px" />
						</td>
					</tr> 

					<tr id="myaccount" style="display:none">
						<td align="right">汇款行账号：</td>
						<td>  
							<input name="account" id="account" type="text"	style="width:180px" />
						</td>
					</tr>

					<tr>
							<c:choose>
								<c:when test="${fns:getUser().company.uuid eq 'dfafad3ebab448bea81ca13b2eb0673e' && typeId!=6 && typeId!=7}">
									<td align="right"><span style="color:red">*</span>项目名称：</td>
									<td> 
										<select name="itemname" id="itemnameselect" onchange="checkSelect()" style="width:190px">
											<option value="">请选择</option>
											<option value="地接费">地接费</option>
											<option value="机票">机票</option>
											<option value="签证">签证</option>
											<option value="领队补助">领队补助</option>
											<option value="保险">保险</option>
											<option value="其他">其他</option>
										</select>
									</td>
									<td>
										<span style="color:red" id="spanCheck"></span>	
									</td>
								</c:when>
								<c:otherwise>
									<td align="right">项目名称：</td>
									<td> 
										<input type="text" name="itemname"  id="itemname" maxlength="50" style="width:180px" /> 
									</td>	
								</c:otherwise>
							</c:choose>
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

					<tr>
						<td align="right">数量：</td> 
						<td>
							<input type="text" name="quantity"  id="quantity"  onkeyup="changeprice()" style="width:180px" maxlength="4"/> 
						</td>
					</tr> 

					<tr>
						<td align="right">转换前单价：</td>
						<td>
							<select style="width:75px; margin-right:5px;" id="currencyId" name="currencyId"  onchange="changeprice()">
								<c:forEach items="${curlist}" var="currency">
									<option value="${currency.id}">${currency.currencyName}</option>
								</c:forEach>
							</select>
							<input type="text" name="price"  id="price" onkeyup="changeprice()" style="width:100px" maxlength="11"/>
						</td>
					</tr>

					<tr>
						<td align="right">转换后币种：</td>
						<td> RMB &nbsp;
							<select style="width:75px; margin-right:5px;display:none" id="currencyAfter" name="currencyAfter"  onchange="changeprice()">
								<c:forEach items="${curlist}" var="currency">
									<c:if test="${currency.currencyName=='人民币'}">
										<option value="${currency.id}" selected>${currency.currencyName}</option>
									</c:if> 
								</c:forEach>
							</select>&nbsp; 汇率：<input type="text" name="rate" id="rate" onkeyup="changerate()"  style="width:98px" maxlength="6"
													  <c:if test="${companyUuid eq YJXZ}">readonly="readonly"</c:if>/>
						</td>
					</tr>

					<tr>
						<td align="right">转换后总价：</td>
						<td>  
							<span id="costfinal"><input type="text" name="priceAfter" id="priceAfter" readonly style="width:180px"/></span>
						</td>
					</tr>
					
					<c:if test="${budgetType eq 1}">
						<tr>
							<td align="right">上传附件：</td>
							<td class="payforFiles">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a type="text" name="DocInfoIds" onclick="uploadFiles('${ctx}',null,this);">选择文件</a>
								<ol class="batch-ol">
								</ol>
							</td>
						</tr>
					</c:if>

					<tr>
						<td align="right">项目备注：</td>
						<td> 
							<textarea rows="3" cols="20" id="comment" name="comment"  maxlength="1000" style="width:180px"></textarea>
						</td>
					</tr> 
					<tr>
						<td colspan="2" >&nbsp;</td>
					</tr>
					<tr>
						<td colspan="2" align="center">
							<input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
								<input  type="button"  class="btn btn-primary" onClick="this.disabled=true;submitForm('4')"  id="save" value="保存" />
							<input  type="button"  class="btn btn-primary" onClick="this.disabled=true;submitForm('1')" id="saveto" value="提交审批" />
						</td>
					</tr>
				</table>
			</div>
		</form:form>

	</body>
<script language="javascript">

function selectOrder(value, orderNum) {
	if(value != -1){
		$("#comment").val(orderNum);
	}else{
		$("#comment").val("");
	}
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
				// options+="<option value='"+myJSON[i].supplierid+"'>"+myJSON[i].suppliername+"</option>"; 
				$('#bank').append("<option value='"+myJSON[i].bankid+"'>"+myJSON[i].bankname+", 账号："+myJSON[i].account+"</option>");   
			}   
			$("#mybankname").hidden();
			$("#myaccount").hidden();
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
	//var currencyAfter=$('#currencyAfter').val();
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
				//alert(myJSON[i].rate);
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
			//options="<option value=0>没有记录</option>";
			//$('#bank').append(options);
		}
	});
}

function changerate(){
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

	//var currencyAfter=$('#currencyAfter').val();
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
