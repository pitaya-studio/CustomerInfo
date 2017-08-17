<%@ page contentType="text/html;charset=UTF-8"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="com.trekiz.admin.common.config.Global"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="sitemesh"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" pageEncoding="utf-8"
	import="org.apache.shiro.subject.Subject,org.apache.shiro.session.Session,org.apache.shiro.SecurityUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>修改其它收入</title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta http-equiv="X-UA-Compatible" content="IE=7,IE=9,IE=10" />
<link
	href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css"
	type="text/css" rel="stylesheet" />
<!--[if lte IE 6]><link href="${ctxStatic}/bootstrap/bsie/css/bootstrap-ie6.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/bootstrap/bsie/js/bootstrap-ie.min.js" type="text/javascript"></script><![endif]-->
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"
	rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"
	rel="stylesheet" type="text/css" />
<link
	href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"
	rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/css/jh-style.css?ver=1" type="text/css"
	rel="stylesheet" />
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"
	type="text/javascript"></script>
<link
	href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"
	type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"
	type="text/javascript"></script>
<script
	src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js"
	type="text/javascript"></script>
<script src="${ctxStatic }/json/json2.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css"
	rel="stylesheet" />
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
  
    function cancelForm() { 
      $("#review").val("4"); 
      formSubmit(); 
    }
    
     function submitForms(review,payReview,companyId){ 
     
       //alert(companyId);
      if(companyId==68){
      $("#review").val("2"); 
      $("#payReview").val("4"); 
     } else if(review==0||review==4||review==5) {
      $("#review").val("1");
     } else if(payReview==0||payReview==4||payReview==5) {      
      $("#payReview").val("1");
     }   
      formSubmit(); 
    } 

     function submitForm(){ 
     
     $("#review").val("1");      
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

         function payForm(){ 
    // $("#review").val("1");  
     if( $("#budgetType").val()==1){
       $("#payReview").val("1");
     }     
      formSubmit(); 
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

      
          if(bankname==""){
                 alert('请输入汇款行名称'); return false;
               }
               if(account==""){
                 alert('请输入汇款账户'); return false;
              }
         

            if(rate==""){
                 alert('请输入汇率'); return false;
            }
          
            if(parseInt(rate)>parseInt(9999)){
                 alert('汇率不能大于9999'); return false;
            }
            if(commentLength > 999){
                 alert('项目备注不能大于1000 字符'); return false;
            }       
            else {
                if(!reg.test(price) && !regneg.test(price)){
                     alert('项目金额最多有2位小数,不能有字母');
                    return false;
                }else{
                    var priceAfter=multiply(quantity,price);  
                    var priceAfter=multiply(priceAfter,rate);  
                    $("#priceAfter").val(priceAfter);
                  
                    $("#costForm").submit(); 
                    /*
                    $.ajax({
                        type: "POST",
                        url: "${ctx}/cost/manager/save",
                        cache:false,
                        dataType:"json",
                        async:false,
                        data:{ 
                            typeId:${typeId},overseas: overseas, supplyType :supplyType, agentId : agentId, supplyId :supplier,name : itemname, currencyId :currencyId, price : price,quantity : quantity, comment : comment, activityId : ${groupId},supplyName:supplyName, budgetType :${budgetType}  },
                        success: function(array){  

                        },
                        error : function(e){
                           alert('请求失败。');
                            //top.$.jBox.tip('请求失败。'+e,'error' ,{ focusId: 'price' });
                            return false;
                        }
                     });
                      */

                }
            }


                  
        }



function selectchange(parentId)
{   
  $('#supplier').empty(); 
  if(null!= parentId && ""!=parentId){  
  var noCache = Date();

  $.getJSON("${ctx}/cost/manager/supplylist/"+parentId,{"noCache":noCache},function(myJSON){ 
  var options=""; 
  if(myJSON.length>0){ 
  options+="<option value=0>请选择类型</option>"; 
  for(var i=0;i<myJSON.length;i++){ 
  // options+="<option value='"+myJSON[i].supplierid+"'>"+myJSON[i].suppliername+"</option>"; 
   $('#supplier').append("<option value='"+myJSON[i].supplierid+"'>"+myJSON[i].suppliername+"</option>");   
   }   
  //$('#supplier').append(options); 
  $("#second").show(); 
  }  
  else if(myJSON.length<=0){  
    options="<option value=0>没有记录</option>"; 
    $('#supplier').append(options);     
    $("#second").show();      
   } 
  });
  }  
}

function tosales()
 {  
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

function inputbank()
{  
  var bank=$('#bank').val(); 
  if(bank== -1){
     $("#mybankname").show();
     $("#myaccount").show();
  }else{
     $("#mybankname").hide();
     $("#myaccount").hide();
  }
}

function getbank(supplyid)
{ 
  $('#bank').empty(); 
  $('#account').empty(); 
   var supplyType = $('input[name="supplyType"]').filter(':checked').val();
  // var supplyType=$('#supplyType').val(); 
  //var supplyid=$('#supplier').val();
  //supplyid=68;
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
  }  
  else if(myJSON.length<=0){  
    options="<option value=-1>没有银行账号记录</option>"; 
    $('#bank').append(options);  
    $("#mybankname").show();
    $("#myaccount").show();  
     
   } 
  });
 
}
  </script>


</head>

<body>
	<form:form name="frm" id="costForm" modelAttribute="sysdefinedict"
		action="${ctx}/cost/island/updateIsland" target="_parent" method="post">

		<input type="hidden" id="activityIslandUuid" name="activityIslandUuid" value="${activityIslandUuid}" />
		<input type="hidden" id="activityUuid" name="activityUuid" value="${activityUuid}" />
		<input type="hidden" id="typeId" name="typeId" value="${typeId}" />
		<input type="hidden" id="payReview" name="payReview"
			value="${payReview}" />
		<input type="hidden" id="budgetType" name="budgetType"
			value="${budgetType}" />
		<input type="hidden" id="review" name="review" value="${review}" />
		<input type="hidden" id="costId" name="costId"
			value="${costRecordIsland.id}" />
		<input  type="hidden" id="deptId" name="deptId" value="${deptId}" />
		<br>
			<div id="moneyId" style=" margin:10px;">
				<table border="0" width="96%">
					<!-- <tr>
						<td width="200px" align="right">境内外项目选择：</td>
						<td><select name="overseas" id="overseas" style="width:180px">
								<option value="" selected="selected">请选择</option>
								<option value="0"
									<c:if test="${costRecord.overseas==0}">selected</c:if>>境内明细</option>
								<option value="1"
									<c:if test="${costRecord.overseas==1}">selected</c:if>>境外明细</option>
						</select></td>
					</tr> -->
					<tr style="height:35px;">
						<td align="right">客户类别：</td>
						<td><input type="radio" name="supplyType" id="supplyType"
							value="0" <c:if test="${costRecordIsland.supplyType == 0 }">checked</c:if> onClick="tosupply()" />地接社 &nbsp;&nbsp; <input
							type="radio" name="supplyType" id="supplyType" value="1"
							<c:if test="${costRecordIsland.supplyType == 1 }">checked</c:if> onClick="tosales()" />渠道商</td>
					</tr>
					<tr id="sales" <c:if test="${costRecordIsland.supplyType == 0 }">style="height:45px;display:none"</c:if>>
						<td align="right">渠道商选择：</td>
						<td><span id="secondagent"> <select name="agentId"
								id="agentId" onchange="selectbank(this.value,1)"
								style="width:180px">
									<option value="">===请选择===</option>
									<option value="-1" <c:if test="${costRecordIsland.supplyId == -1 }">selected="selected"</c:if>>非签约渠道
									<c:forEach var="agentinfo" items="${agentinfo }">
										<option value="${agentinfo.id }"
											<c:if test="${costRecordIsland.supplyId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}
										</option>
									</c:forEach>
							</select> </span></td>
					</tr>

					<tr id="supplyid" <c:if test="${costRecordIsland.supplyType == 1 }">style="display:none"</c:if>>
						<td align="right">地接社类型：</td>
						<td><select style="width:180px"
							onchange="selectchange(this.value)" id="first" name="first">
								<c:forEach items="${supplytypelist}" var="dict">
									<option value="${dict.value}" <c:if test="${costRecordIsland.supplierType == dict.value }">selected="selected"</c:if> >${dict.label}</option>
								</c:forEach>
						</select></td>
					</tr>

					<tr id="supply" <c:if test="${costRecordIsland.supplyType == 1 }">style="display:none"</c:if>>
						<td align="right">地接社选择：</td>
						<td><span id="second"><select style="width:180px"
							onchange="selectchange(this.value)" id="supplier" name="supplier">
								<c:forEach items="${supplierTypeList}" var="supplierType">
									<option value="${supplierType.supplierId}" <c:if test="${costRecordIsland.supplyId == dict.supplierId }">selected="selected"</c:if> >${supplierType.supplierName}</option>
								</c:forEach>
						</select>
						</span></td>
					</tr>
					
<!-- 					
					<tr id="supplyid">
						<td align="right">批发商/地接社：</td>
						<td><input name="supplier" id="supplier" type="text"
							value="${costRecord.supplyName}" readonly style="width:180px" />
						</td>
					</tr>-->
					<tr style="height:50px;">
						<td align="right">开户银行：</td>
						<td><select id="bank" name="bank" onchange="inputbank()"
							style="width:180px">
								<option value="0" selected>请选择</option>
								<option value="-1" selected>录入新银行账号</option>
								<c:forEach items="${bankList }" var="bank">
									<option value="${bank.id}" <c:if test="${bank != null and bank.id == oneBank.id }">selected="selected"</c:if> >${bank.bankName}, 账号：${bank.bankAccountCode}
								</c:forEach>
						</select></td>
					</tr>

					<tr id="mybankname" <c:if test="${bankList == null }">style="display:none"</c:if>>
						<td align="right">汇款行名称：</td>
						<td><input name="bankname" id="bankname" type="text"
							value="${costRecordIsland.bankName}" style="width:180px" /></td>
					</tr>

					<tr id="myaccount" <c:if test="${bankList == null }">style="display:none"</c:if>>
						<td align="right">汇款行账号：</td>
						<td><input name="account" id="account" type="text"
							value="${costRecordIsland.bankAccount}" style="width:180px" /></td>
					</tr> 

					<tr>
						<td align="right">款项名称：</td>
						<td><input type="text" name="itemname" id="itemname"
							maxlength="50" value="${costRecordIsland.name}" style="width:180px" />
						</td>
					</tr>

					<input type="hidden" name="quantity" id="quantity"
							value="1" onkeyup="changeprice()"
							style="width:180px" />

					<tr>
						<td align="right">转换前单价：</td>
						<td><select style="width:75px; margin-right:5px;"
							id="currencyId" name="currencyId" onchange="changeprice()">
								<c:forEach items="${curlist}" var="currency">
									<c:if test="${costRecordIsland.currencyId==currency.id}">
										<option value="${currency.id}">${currency.currencyName}</option>
									</c:if>
								</c:forEach>
						</select><input type="text" name="price" id="price" oninput="changeprice()"
							value="${costRecordIsland.price}" style="width:100px" /></td>
					</tr>


					<tr>
						<td align="right">转换后币种：</td>
						<td>RMB &nbsp; <select
							style="width:75px; margin-right:5px;;display:none"
							id="currencyAfter" name="currencyAfter" onchange="changeprice()">
								<c:forEach items="${curlist}" var="currency">
									<option value="${currency.id}"
										<c:if test="${costRecordIsland.currencyAfter==currency.id}">selected</c:if>>${currency.currencyName}</option>
								</c:forEach>
						</select>&nbsp; 汇率：<input type="text" name="rate" id="rate"
							value="${costRecordIsland.rate}" onkeyup="changerate()"
							style="width:55px" /></td>
					</tr>

					<tr>
						<td align="right">转换后总价：</td>
						<td><span id="costfinal"><input type="text"
								name="priceAfter" id="priceAfter"
								value="${costRecordIsland.priceAfter}" readonly style="width:180px" />
						</span></td>
					</tr>

					<tr>
						<td align="right">项目备注：</td>
						<td><textarea rows="3" cols="20" id="comment" name="comment"
								maxlength="1000" style="width:180px">${costRecordIsland.comment}</textarea>
						</td>
					</tr>
				</table>
			</div>
			<div class="release_next_add">
				<input class="btn btn-primary gray" type="button" value="取消"
					onClick="window.parent.window.jBox.close()" />
				<c:if test="${costRecordIsland.review==1 }">
					<input type="button" class="btn btn-primary" onClick="cancelForm()"
						value="撤销审核" />
				</c:if>
				<c:if
					test="${ budgetType==2 && ( costRecordIsland.review==4  ||  costRecordIsland.review==0||  costRecordIsland.review==5) }">
					<input type="button" class="btn btn-primary" onClick="saveForm()"
						value="提交" />
					<!-- <input type="button" class="btn btn-primary" onClick="submitForm()"
						value="提交审核" /> -->
				</c:if>
				<c:if
					test="${ budgetType==1 &&  ( costRecordIsland.payReview==0 || costRecordIsland.review==4  ||  costRecord.review==0 ||  costRecordIsland.review==5  ||  costRecordIsland.review==5 || ( costRecordIsland.review==2 &&  (costRecordIsland.payReview==4 ||costRecordIsland.payReview==5) )) }">

					<input type="button" class="btn btn-primary"
						onClick="submitForms( ${costRecordIsland.review}, ${costRecordIsland.payReview},${companyId})"
						<c:if test="${ companyId==68}">value="保存"</c:if>
						<c:if test="${ companyId!=68}">value="提交审核"</c:if> />
				</c:if>



			</div>
			</div>
	</form:form>

</body>
<script language="javascript">

function SelectTips(flag){
  alert($('#supplier').val());
} 



function getbank(supplyid)
{ 
  $('#bank').empty(); 
  $('#account').empty(); 
  var supplyType=$('#supplyType').val();
  //var supplyid=$('#supplier').val();
  //supplyid=68;
  if (supplyid=="") return;
  var noCache = Date();

  $.getJSON("${ctx}/cost/manager/banklist/"+supplyType+"/"+supplyid,{"noCache":noCache},function(myJSON){ 
  var options="";    
  if(myJSON.length>0){ 
  options+="<option value=0>请选择....</option><option value=-1>录入新银行账号</option>";
  $('#bank').append(options); 
  for(var i=0;i<myJSON.length;i++){ 
  // options+="<option value='"+myJSON[i].supplierid+"'>"+myJSON[i].suppliername+"</option>"; 
   $('#bank').append("<option value='"+myJSON[i].bankid+"'>"+myJSON[i].bankname+", 账号："+myJSON[i].account+"</option>");   
   }   
  //$("#second").show(); 
  }  
  else if(myJSON.length<=0){  
    options="<option value=-1>没有银行账号记录,请录入新新银行账号</option>"; 
    $('#bank').append(options);  
    $("#mybankname").show();
    $("#myaccount").show(); 
    $("#bankname").val("");
    $("#account").val(""); 
     
   } 
  });
 
}

function  trim(str){
    for(var i = 0 ;i<str.length && str.charAt(i)=="  "; i++);
    for(var j =str.length;  j>0 && str.charAt(j-1)=="  ";j--);
    if(i>j)  return  "";  
    return  str.substring(i,j);  
}

function multiply(arg1,arg2) 
{ 
 var m=0,s1=arg1.toString(),s2=arg2.toString(); 
 try{m+=s1.split(".")[1].length}catch(e){} 
 try{m+=s2.split(".")[1].length}catch(e){} 
 return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m) 
}

function changeprice()
{ var price=trim($('#price').val());
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

  /*
  var currencyAfter=$("#currencyAfter").find("option:selected").text(); 
  if( trim(currencyAfter) !="人民币") {
   $("#rate").val("");
   $("#priceAfter").val("");
    return;
  } */
  

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
      
        $("#priceAfter").val(priceAfter);
      }
   }     
  }  
  else if(myJSON.length<=0){ 
     return; 
    //options="<option value=0>没有记录</option>"; 
    //$('#bank').append(options);    
   } 
  });
 }

 function changerate()
{ 
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
    if(!reg.test(price) || !regneg.test(price)){
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
