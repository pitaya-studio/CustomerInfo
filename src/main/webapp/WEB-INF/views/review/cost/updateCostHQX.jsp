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
<title>成本修改</title>
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
	.custom-combobox .custom-combobox-input{height:26px;width: initial; }
	.custom-combobox .custom-combobox-toggle{vertical-align: top;}
</style>

<script type="text/javascript">
	$(function() {
		
		if(${conf eq 1 and budgetType eq 1 and review eq 2}) {
			$("#overseas").attr("disabled",true);
			$("#agentId").attr("disabled",true);
			$("#first").attr("disabled",true);
			$("#supplier").attr("disabled",true);
			$("#bankname").attr("readonly","readonly");
			$("#account").attr("readonly","readonly");
			$("#itemname").attr("readonly","readonly");
			$("#quantity").attr("readonly","readonly");
			$("#currencyId").attr("disabled",true);
			$("#price").attr("readonly","readonly");
			$("#rate").attr("readonly","readonly");
		}else{
			//搜索条件筛选 
			$("#agentId" ).comboboxInquiry();
			if(${supplierType}==0){
				// selectchange('2');
			} else {
				// selectchange(${supplierType});
			} 
		
			$("#supplier").comboboxInquiry();
		
			$("#second .custom-combobox").on("autocompleteselect",function(){
				getbank(arguments[1].item.option.value);
			});
		
			$("#secondagent .custom-combobox").on("autocompleteselect",function(){
				getbank(arguments[1].item.option.value);
			});
		}
	});

	var regneg =/^(-)[0-9]*(\.[0-9]{0,2})?$/;
	function cancelForm() {
		$("#review").val("4");
		formSubmit();
	}

	function submitForms(review,payReview,companyId){
		if($("#costAutoPass").val()==1){
			$("#review").val("1");
			$("#payReview").val("4");
		} else if(review==0||review==4||review==5) {
			$("#review").val("1");
			var budgetType = $("#budgetType").val();
			if(${companyUuid eq DHJQ} && budgetType == "1") {
				if(($(".uploadPath").size() > 0 && $(".uploadPath").children().size() > 0) || ($(".batch-ol").size() > 0 && $(".batch-ol").children().size() > 0)) {

				}else{
					alert("请上传附件后重新提交");
					$("#saveto").attr('disabled',false);
					$("#save").attr('disabled',false);
					return;
				}
			}

		} else if(payReview==0||payReview==4||payReview==5) {
			$("#payReview").val("1");
		}
		formSubmit();
	} 

	function payForms(review,payReview,companyId){
		if(payReview==0||payReview==4||payReview==5) {
			$("#payReview").val("1");
		}
		formSubmit();
	} 

	function submitForm(){
		$("#review").val("1");
		// bug17730 处理 gaoyang 2017-04-10
		/* if(($(".uploadPath").size() > 0 && $(".uploadPath").children().size() > 0) || ($(".batch-ol").size() > 0 && $(".batch-ol").children().size() > 0)) {

		}else{
			alert("请上传附件后重新提交");
			$("#saveto").attr('disabled',false);
			$("#save").attr('disabled',false);
			return;
		} */
		formSubmit();
	} 

	function redoForm(){
		$("#review").val("2");
		$("#payReview").val("1");
		formSubmit();
	} 

	function saveForm(){ 
		$("#review").val("4");
		formSubmit();
	} 

	function saveForms(){
		formSubmit();
	} 

	function payForm(){ 
		// $("#review").val("1");  
		if( $("#budgetType").val()==1){
			$("#payReview").val("1");
		}
		formSubmit();
	} 

	function multiply(arg1,arg2) {
		var m=0,s1=arg1.toString(),s2=arg2.toString();
		try{m+=s1.split(".")[1].length}catch(e){}
		try{m+=s2.split(".")[1].length}catch(e){}
		return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)
	}

	function formSubmit() {
		$("#overseas").attr("disabled",false);
		$("#agentId").attr("disabled",false);
		$("#first").attr("disabled",false);
		$("#supplier").attr("disabled",false);
		$("#currencyId").attr("disabled",false);
		
		var overseas =$("#overseas").val();
		var agentId =$("#agentId").val();
		var first=$("#first").val();
		var supplier=$("#supplier").val();
		var itemname;
		if("${fns:getUser().company.uuid eq 'dfafad3ebab448bea81ca13b2eb0673e'  && typeId!=6 && typeId!=7}"=="true"){
			itemname= $("#itemnameselect").val();
			if(itemname==""){
				$("#spanCheck").text("必填项");
			}
		}else{
			itemname= $("#itemname").val();
		}
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
	
		if(itemname==""){
			alert('项目名称不能为空'); return false;
		}

		if(currencyId=="0"){
			alert('请选择单价币种'); return false;
		}

		if(price==""){
			alert('请输入单价'); return false;
		}

		if(quantity==""){
			alert('请输入数量'); return false;
		}

		if(rate==""){
			alert('请输入汇率'); return false;
		}
		
		if(parseInt(rate)>parseInt(9999)){
			alert('汇率不能大于9999'); return false;
		}
		if(commentLength > 999){
			alert('项目备注不能大于1000 字符'); return false;
		} else {
			if(!reg.test(price) && !regneg.test(price)){
				alert('项目金额最多有2位小数,不能有字母');
				return false;
			}else{
				var priceAfter=multiply(quantity,price);
				var priceAfter=multiply(priceAfter,rate);
				$("#priceAfter").val(priceAfter);
				
				$("#costForm").submit();
			}
		}
	}

	function selectchange(parentId){
		$('#supplier').empty();
		$('#second input').val();
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
	
	function downloads(id){
		window.open("${ctx}/sys/docinfo/download/"+id);
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
	<form:form name="frm" id="costForm" modelAttribute="sysdefinedict" action="${ctx}/costReview/activity/updateHQX" target="_parent" method="post">
		<input type="hidden" id="activityId" name="activityId" value="${activityId}" />
		<input type="hidden" id="groupId" name="groupId" value="${groupId}" />
		<input type="hidden" id="typeId" name="typeId" value="${typeId}" />
		<input type="hidden" id="costAutoPass" name="costAutoPass" value="${costAutoPass}" />
		<input type="hidden"  id="payReview" name="payReview" value="${payReview}" />
		<input type="hidden"  id="budgetType" name="budgetType" value="${budgetType}" />
		<input type="hidden"  id="review" name="review" value="${review}"/>
		<input type="hidden"  id="costId" name="costId"  value="${costRecord.id}" />
		<input type="hidden" id="deptId" name="deptId" value="${deptId}" />
		<input type="hidden" id="supplyType" name="supplyType" value="${supplyType}" />
		<br>
		<div id="moneyId" style=" margin:10px;" >
			<table border="0" width="96%">
				<tr>
					<td align="right">项目选择：</td>
					<td>
						<select name="overseas" id="overseas" style="width:180px">
							<option value=""selected="selected">请选择</option>  
							<option value="0" <c:if test="${costRecord.overseas==0}">selected</c:if>>境内明细</option>
							<option value="1" <c:if test="${costRecord.overseas==1}">selected</c:if>>境外明细</option>
						</select>
					</td>
				</tr>
		
				<tr id="sales" <c:if test="${supplyType==0}">style="display:none"</c:if>>
					<td align="right">渠道商选择：</td>
					<td>
						<span id="secondagent">
							<select name="agentId" id="agentId" style="width:180px">
								<option value="" >===请选择===</option>
								<option value="-1" <c:if test="${costRecord.supplyId == -1 }">selected="selected"</c:if>>非签约渠道</option>
								<c:forEach var="agentinfo" items="${agentinfo }"> 
									<option value="${agentinfo.id }" <c:if test="${costRecord.supplyId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
								</c:forEach>
							</select>
						</span>
					</td>
				</tr>

				<tr id="supplyid" <c:if test="${supplyType==1}">style="display:none"</c:if>>
					<td align="right">地接社类型：</td>
					<td> 
						<select style="width:180px" id="first" name="first">
							<c:forEach items="${supplytypelist}" var="dict">
								<option <c:if test="${supplierType==dict.value}">selected="selected"</c:if> value="${dict.value}">${dict.label}</option>
							</c:forEach>
						</select>
					</td>
				</tr>

				<tr id="supply" <c:if test="${supplyType==1}">style="display:none"</c:if>>
					<td align="right">地接社选择：</td>
					<td>  
						<span id="second">
							<select id="supplier" name="supplier"  style="width:180px" > 
								<option selected="selected" value="${costRecord.supplyId}">${costRecord.supplyName}</option>
								<c:forEach items="${supplylist}" var="dict">
									<option  value="${dict.supplierId}">${dict.supplierName}</option>
								</c:forEach>
							</select>
						</span>
					</td>
				</tr> 

				<tr>
					<td align="right">汇款行名称：</td>
					<td>  
						<input name="bankname" id="bankname" type="text" value="${costRecord.bankName}" style="width:180px" />
					</td>
				</tr>

				<tr>
					<td align="right">汇款行账号：</td>
					<td>
						<input name="account" id="account" type="text" value="${costRecord.bankAccount}" style="width:180px" />
					</td>
				</tr>

				<tr>
					<c:choose>
								<c:when test="${fns:getUser().company.uuid eq 'dfafad3ebab448bea81ca13b2eb0673e' && typeId!=6 && typeId!=7}">
									<td align="right"><span style="color:red;">*</span>项目名称：</td>
									<td> 
										<select name="itemname" id="itemnameselect" onchange="checkSelect()" style="width:190px">
											<option value="">请选择</option>
											<option value="地接费" <c:if test="${costRecord.name=='地接费'}">selected</c:if> >地接费</option>
											<option value="机票"  <c:if test="${costRecord.name=='机票'}">selected</c:if>>机票</option>
											<option value="签证" <c:if test="${costRecord.name=='签证'}">selected</c:if>>签证</option>
											<option value="领队补助" <c:if test="${costRecord.name=='领队补助'}">selected</c:if>>领队补助</option>
											<option value="保险" <c:if test="${costRecord.name=='保险'}">selected</c:if>>保险</option>
											<option value="其他" <c:if test="${costRecord.name=='其他'}">selected</c:if>>其他</option>
										</select>
									</td>	
									<td>
										<span style="color:red" id="spanCheck"></span>	
									</td>
								</c:when>
								<c:otherwise>
									<td align="right">项目名称：</td>
									<td>
										<input type="text" name="itemname"  id="itemname"  maxlength="50" value="${costRecord.name}" style="width:180px" />
									</td>
								</c:otherwise>
							</c:choose>
				</tr>

				<tr>
					<td align="right">数量：</td>
					<td>
						<input type="text" name="quantity"  id="quantity"  value="${costRecord.quantity}"  onkeyup="changeprice()" style="width:180px"/>
					</td>
				</tr>

				<tr>
					<td align="right">转换前单价：</td>
					<td>
						<select style="width:75px; margin-right:5px;" id="currencyId" name="currencyId"  onchange="changeprice()">
							<c:forEach items="${curlist}" var="currency">
								<option value="${currency.id}"  <c:if test="${costRecord.currencyId==currency.id}">selected</c:if>>${currency.currencyName}</option>
							</c:forEach>
						</select>
						<input type="text" name="price" id="price" onkeyup="changeprice()" value="${costRecord.price}" style="width:100px"/>
					</td>
				</tr>

				<tr>
					<td align="right">转换后币种：</td>
					<td>  RMB &nbsp;
						<select style="width:75px; margin-right:5px;;display:none" id="currencyAfter"  name="currencyAfter"  onchange="changeprice()">
							<c:forEach items="${curlist}" var="currency">
								<option value="${currency.id}" <c:if test="${costRecord.currencyAfter==currency.id}">selected</c:if>>${currency.currencyName}</option>
							</c:forEach>
						</select>&nbsp; 汇率：<input type="text" name="rate" id="rate" value="${costRecord.rate}" onkeyup="changerate()" style="width:55px" <c:if test="${companyUuid eq YJXZ}">readonly="readonly"</c:if>/>
					</td>
				</tr>

				<tr>
					<td align="right">转换后总价：</td>
					<td>  
						<span id="costfinal"><input type="text" name="priceAfter" id="priceAfter" value="${costRecord.priceAfter}"  readonly style="width:180px"/></span>
					</td>
				</tr>
				
				<c:if test="${budgetType eq 1}">
					<tr>
						<td align="right">上传附件：</td>
						<td class="payforFiles">
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a type="text" name="DocInfoIds" onclick="uploadFiles('${ctx}',null,this);">选择文件</a>
							<ol class="batch-ol">
								<c:forEach items="${docList}" var="docInfo" varStatus="s1">
									<li>
										<span>${docInfo.docName}</span><!--<a style="margin-left:10px;" href="javascript:void(0)" onClick="downloads(${docInfo.id})">下载</a> -->
										<input type="hidden" name="DocInfoIds" value="${docInfo.id}"/>
										<c:if test="${docInfo.createBy.id eq userId }">
											<a class="batchDel" href="javascript:void(0)" onclick="deleteFileInfo(null,'DocInfoIds',this)">×</a>
										</c:if>
									</li>
								</c:forEach>
							</ol>
						</td>
					</tr>
				</c:if>

				<tr>
					<td align="right">项目备注：</td>
					<td> 
						<textarea rows="3" cols="20" id="comment" name="comment"  maxlength="1000" style="width:180px">${costRecord.comment}</textarea>
					</td>
				</tr>
				<tr><td colspan="2" >&nbsp;</td></tr>
				<tr>
					<td colspan="2" align="center">
						<input class="btn btn-primary gray" type="button" value="取消" onClick="window.parent.window.jBox.close()"/>
<!-- 						<c:if test="${costRecord.review==1 }"> -->
<!-- 							<input type="button" class="btn btn-primary" onClick="cancelForm()"  value="撤销审批" /> -->
<!-- 						</c:if> -->
						<c:if test="${ budgetType==0 && ( costRecord.review==4  || costRecord.review==5) }">
							<input type="button" class="btn btn-primary" onClick="saveForm()" value="保存" />
						</c:if>
						<c:if test="${ budgetType==0 && costRecord.review==0 }">
							<input type="button" class="btn btn-primary" onClick="submitForm()" value="提交审批" />
						</c:if> 
						<c:if test="${ budgetType==1 && ( costRecord.review==4 || costRecord.review==5||(costRecord.review==2&&(costRecord.payReview==0 ||   costRecord.payReview==4 ||costRecord.payReview==5 ))) }">
							<input type="button" class="btn btn-primary" onClick="saveForms()" value="保存" />
						</c:if> 
						<c:if test="${ budgetType==1 && costRecord.review!=1  && costRecord.review!=2 }">
							<input type="button" class="btn btn-primary" onClick="submitForms( ${costRecord.review}, ${costRecord.payReview},${companyId})"  value="提交审批"/>
						</c:if>
<!-- 						<c:if test="${ budgetType==1 && costRecord.review==2 && (costRecord.payReview==0 || costRecord.payReview==4 || costRecord.payReview==5 ) }"> -->
<!-- 							<input type="button" class="btn btn-primary" onClick="payForms( ${costRecord.review}, ${costRecord.payReview},${companyId})"  value="付款申请"/> -->
<!-- 						</c:if> -->
					</td>
				</tr>
			</table>
		</div>
	</form:form>
</body>
<script language="javascript">

function SelectTips(flag){
	alert($('#supplier').val());
} 

function getbank(supplyid){  
	var supplyType=$('#supplyType').val();
	if(supplyid==""){
		return false;
	}
	var noCache = Date();
	if(supplyType == "0") {
		supplyType=1;
	}else if(supplyType == "1"){
		supplyType=2;
	}

	$.getJSON("${ctx}/cost/manager/banklist/"+supplyType+"/"+supplyid,{"noCache":noCache},function(myJSON){ 
		var options="";
		if(myJSON.length>0){
			options+="<option value=0>请选择....</option><option value=-1>录入新银行账号</option>";
			for(var i=0;i<myJSON.length;i++){ 
				if(i==0) {
					$("#bankname").val(myJSON[i].bankname);
					$("#account").val(myJSON[i].account);
				} 
			}
		}else if(myJSON.length<=0){
		} 
	});
}

function trim(str){
	for(var i = 0 ;i<str.length && str.charAt(i)=="  "; i++);
	for(var j =str.length; j>0 && str.charAt(j-1)=="  ";j--);
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
				var rate=myJSON[i].rate;
				$("#rate").val(myJSON[i].rate);
	
				if (rate!="") {
					var priceAfter=multiply(quantity,price);
					var priceAfter=multiply(priceAfter,rate);
	
					$("#priceAfter").val(priceAfter);
				}
			}
		} else if(myJSON.length<=0){
			return; 
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

	var currencyAfter=$("#currencyAfter").find("option:selected").text();
	var priceAfter=multiply(quantity,price);
	var priceAfter=multiply(priceAfter,rate);
	$("#priceAfter").val(priceAfter);
	return;
}

</script>
</html>