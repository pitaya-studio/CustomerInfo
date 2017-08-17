<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<link href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css"
	type="text/css" rel="stylesheet" />
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/huanqiu-style.css" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/modules/activity/activity.js"
	type="text/javascript"></script>

<script src="${ctxStatic}/modules/order/payedConfirm.js"
	type="text/javascript"></script>


<script type="text/javascript">
//20150619增加银行转款、汇票、POS机刷卡支付方式
$(document).ready(function(){
	$("#offline_paybox  [flag='select']").each(function(index, element) {
                        $(element).jQSelect({});
                    });
      var payType= $("#payType").val();
      
      if(payType==1){//支票
            $("#payorderbgcolor div[class=patorder_a1]").hide();
		    $("#payorderbgcolor div[class=patorder_a2]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		    $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
		    $("#payorderbgcolor div[class=patorder_a7]").hide();
		    $("#offlinebox tr[class=huikuan]").hide();
		    $("#offlinebox tr[class=shoukuan]").hide();
	        $("#offlinebox tr[class=invoice]").show();
	        $("#offlinebox tr[class=draft]").hide();
	        $("#offlinebox tr[class=alipay]").hide();
      }else if(payType==3 || payType==5){//现金
           $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
		    $("#payorderbgcolor div[class=patorder_a7]").hide();
            $("#offlinebox tr[class=huikuan]").hide();
            $("#offlinebox tr[class=shoukuan]").hide();
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=draft]").hide();
	        $("#offlinebox tr[class=alipay]").hide();
	        $("#cashPayDisplay").hide();
      }else if(payType==4){//汇款
            $("#payorderbgcolor div[class=patorder_a1]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
            $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
	        $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
		    $("#payorderbgcolor div[class=patorder_a7]").hide();
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").show();
	        $("#offlinebox tr[class=draft]").hide();
	        $("#offlinebox tr[class=alipay]").hide();
      }else if(payType==6){//银行转账
    	  	$("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
	        $("#payorderbgcolor div[class=patorder_a4]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
		    $("#payorderbgcolor div[class=patorder_a7]").hide();
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").show();
	        $("#offlinebox tr[class=draft]").hide();
	        $("#offlinebox tr[class=alipay]").hide();
      }else if(payType==7){//汇票
    	    $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
		    $("#payorderbgcolor div[class=patorder_a7]").hide();
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").show();
	        $("#offlinebox tr[class=draft]").show();
	        $("#offlinebox tr[class=alipay]").hide();
      }else if(payType==8){//POS机刷卡
    	    $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
	        $("#payorderbgcolor div[class=patorder_a6]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#payorderbgcolor div[class=patorder_a7]").hide();
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").hide();
	        $("#offlinebox tr[class=draft]").hide();
	        $("#offlinebox tr[class=alipay]").hide();
      }else if(payType == 9) {//因公支付宝
      		$("#payorderbgcolor div[class=patorder_a1]").hide();
	 		$("#payorderbgcolor div[class=patorder_a2]").hide();
	    	$("#payorderbgcolor div[class=patorder_a3]").hide();
			$("#payorderbgcolor div[class=patorder_a4]").hide();
			$("#payorderbgcolor div[class=patorder_a5]").hide();
	    	$("#payorderbgcolor div[class=patorder_a6]").hide();
      		$("#payorderbgcolor div[class=patorder_a7]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
      		$("#offlinebox tr[class=invoice]").hide();
      		$("#offlinebox tr[class=huikuan]").hide();
	    	$("#offlinebox tr[class=shoukuan]").hide();
	    	$("#offlinebox tr[class=draft]").hide();
      		$("#offlinebox tr[class=alipay]").show();
      }
     initValue($("#HsupplierId").val(), $("#HtoBankNname").val(),$("#HtoBankAccount").val());
      
});
function tabshow(obj,str){
        $(obj).css({"color":"#3A7851","backgroundColor":"#FFF"}).siblings().css({"color":"#000","backgroundColor":""});
		$(obj).parent().siblings().children('#'+str).show().siblings().hide();
}

function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }

function isnull(){
     var accountDate = $("#accountDate").val();
     if(accountDate==""){
        alert("到账日期不能为空");
     }
}

function changeBank(supplierId, bankName, eleId) {
		bankName = encodeURI(encodeURI(bankName));
		if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPayMore/getBankAccount/" + supplierId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
						}
					}
					$("#" + eleId).html('');
					$("#" + eleId).append(options);
				},
				error : function(e) {
					alert(e.responseText);
				}
			});
		}
	}
	//开户行
	function changeOpenBank(supplierId, bankName) {
		bankName = encodeURI(encodeURI(bankName));
		if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPay/getOpenBankById/" + supplierId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
						}
					}
					$("#toBankAccount").html('');
					$("#toBankAccount").append(options);

				},
				error : function() {
					alert('返回数据失败');
				}
			});
		} else {
			$("#toBankAccount").html('');
		}
	}

    function initAgentBankAccountInfo(agentId,bankName,bankAccount){
        bankName = encodeURI(encodeURI(bankName));
        if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPay/initAgentBankAccountInfo/" + agentId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
						   if(data[i][5]==toBankAccount){
						      options += '<option value="'+data[i][5]+'" selected="selected">'+data[i][5] + '</option>';
						   }else{
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
									}
						}
					}
					$("#bankAccount").html('');
					$("#bankAccount").append(options);

				},
				error : function() {
					alert('返回数据失败');
				}
			});
		}
    }
    function initValue(supplierId, bankName,toBankAccount){
        bankName = encodeURI(encodeURI(bankName));
		if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPay/getOpenBankById/" + supplierId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
						   if(data[i][5]==toBankAccount){
						      options += '<option value="'+data[i][5]+'" selected="selected">'+data[i][5] + '</option>';
						   }else{
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
									}
						}
					}
					$("#toBankAccount").html('');
					$("#toBankAccount").append(options);

				},
				error : function() {
					alert('返回数据失败');
				}
			});
		}
    }
    
    $(function(){
    	if("${orderType}"==2 && "${returnDifference.uuid}" != ""){
    		var payPrices = $("input[name = 'payPrice']");
    		var total = "";
    		for(var i = 0 ; i<payPrices.length ; i ++){
    			var payPrice = $(payPrices[i]).val();
    			var mark = "";
    			var amount = "";
    			if(payPrice.indexOf("-")>-1){
    				mark = payPrice.charAt(1);
    				amount = "-"+payPrice.substring(2).replace(",","");
    			}else{
    				mark = payPrice.charAt(0);
    				amount = payPrice.substring(1).replace(",","");
    			}
    			var value = 0.00;
    			if(mark == "${differenceMark}"){
    				value = Number(amount)+Number(${returnDifference.returnPrice});
    			}else{
    				value = amount
    			}
    			if(total == ""){
    				total = mark + value.toFixed(2);
    			}else{
    				total = total+"+"+mark+value.toFixed(2);;
    			}
    		}
    		$("#total").text(total);
    	}
    })
</script>
<style>
input,input[type="text"] {
	height: 35px;
	line-height: 25px;
	padding: 4px 6px;
	font-size: 12px;
}

.trtextaling,.list {
	font-size: 12px;
}
. trtextaling{
   max-width: 110px;
}
.trtextalingi{
	font-size:12px;
	line-height:35px;
}
</style>
<!-- <div class="ydbzbox" style="min-height:380px;"> -->
<!-- 	<div class="payforDiv" style="margin-left:0"> -->
		<div id="offline_paybox" class="pay_clearfix" style="clear: both;">

			<div id="payorderbgcolor"
				style=" z-index: 2;height:30px; position:relative;">
				<div onclick="tabshow(this,'4')" class="patorder_a1"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇款</div>
				<div onclick="tabshow(this,'1')" class="patorder_a2"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: inherit;">支票</div>
				<div onclick="tabshow(this,'3')" class="patorder_a3"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">现金支付</div>
				<div onclick="tabshow(this,'6')" class="patorder_a4"
				style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">银行转账</div>
				<div onclick="tabshow(this,'7')" class="patorder_a5"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇票</div>
				<div onclick="tabshow(this,'8')" class="patorder_a6"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">POS机刷卡</div>
				<div onclick="tabshow(this,'9')" class="patorder_a7"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">因公支付宝</div>
			</div>

			<div id="pcot" class="payORDER"
				style="heigth:450;clear:both; padding:10px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">
				<input type="hidden" name="payType1">
				<div id="offlinebox" style="" class="payDiv">
					<input type="hidden" name="token" id="token" value="${token}" />
					<input type="hidden" name="orderPayId" id="orderPayId" value="${orderpay.id}" />
					<input type="hidden" name="payType" id="payType" value="${orderpay.payType }" /> 
					<input type="hidden" name="supplierId" id="HsupplierId" value="${supplierId}" /> 
					<input type="hidden" name="toBankNname" id="HtoBankNname" value="${orderpay.toBankNname }" /> 
					<input type="hidden" name="toBankAccount" id="HtoBankAccount" value="${orderpay.toBankAccount }" />
					<input type="hidden"  name="orderNum" id="orderNum" value="${orderpay.orderNum }"/>
					
					<c:if test="${orderType > 10 }">
						<input type="hidden" name="payProductOrderUuid"
							id="payProductOrderUuid" value="${orderpay.uuid }" />
					</c:if>
					<table width="100%" cellpadding="5" cellspacing="0" border="0">
						<tbody>
							<tr>
					             <td class="trtextaling">收款金额：</td>
					             <c:choose>
					                 <c:when test="${orderType eq '6' }">
					                     <td class="trtextalingi"><input type="text" readonly="readonly" name="payPrice"  id="payPrice"  value="${orderpay.moneyAmount}"/></td>
					                 </c:when>
					                 <c:when test="${orderType eq '11' || orderType eq '12' }">
					                     <td class="trtextalingi"><input type="text" readonly="readonly" name="payPrice"  id="payPrice"  value="${payPrice}"/></td>
					                 </c:when>
					                 <c:otherwise>
					                     <td class="trtextalingi"><input type="text" readonly="readonly" name="payPrice"  id="payPrice"  value="${fns:getMoneyAmountBySerialNum(orderpay.moneySerialNum,2)}"/></td>
					                 </c:otherwise>
					             </c:choose>
					             
					        </tr>
					       <c:if test="${orderType eq '2' and  not empty returnDifference.uuid }">
					        	<tr>
					        		<td class="trtextaling">门店结算价差额返还：${differenceMark }</td><td class="trtextalingi">${returnDifference.returnPrice }</td>
					        	</tr>
					        	<tr>
					        		<td class="trtextaling">总计：</td><td class="trtextalingi" id="total"></td>
					        	</tr>
					        </c:if> 
							<tr>
								<td class="trtextaling" style="padding:0px;">来款单位：</td>
								<td class="trtextalingi"><input type="text"
									name="payerName" id="payerName" value="${orderpay.payerName }" maxlength="25"/>
								</td>
							</tr>
							<!--  汇款内容 -->

							<tr class="huikuan">
								<td class="trtextaling" style="padding:0px;">来款行名称：</td>
								<td class="trtextalingi">
								<c:choose>
										<c:when test="${fn:length(agentBanks)>1}">
											<select id="bankName" name="bankName" class="required"
												onload="initAgentBankAccountInfo('${agentId}',[this.options[this.options.selectedIndex].value],'${orderpay.bankAccount }')" 
												onchange="changeBank('${agentId}',[this.options[this.options.selectedIndex].value],'bankAccount')">
												<option value="">--请选择--</option>
												<c:forEach items="${agentBanks}" var="bankInfo"
													varStatus="status">
													<option value="${bankInfo.bankName}"
														<c:if test="${bankInfo.bankName eq orderpay.bankName}">selected="selected"</c:if>>${bankInfo.bankName}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<input type="text" id="bankName" name="bankName" maxlength="20"
												class="required" value="${orderpay.bankName}" />
										</c:otherwise>
									</c:choose>
								</td>

							</tr>
							<tr class="huikuan">
								<td class="trtextaling" style="padding:0px;">来款账号：</td>
								<td class="trtextalingi"><c:choose>
										<c:when test="${fn:length(agentBanks)>1}">
											<select id="bankAccount" name="bankAccount" class="required">
												<option value="${orderpay.bankAccount }">${orderpay.bankAccount}</option>
											</select>
										</c:when>
										<c:otherwise>
											<input id="bankAccount" type="text" name="bankAccount" value="${orderpay.bankAccount }" maxlength="21" class="required" />
										</c:otherwise>
									</c:choose>
								</td>

							</tr>
							<!-- 汇票到期日20150619  -->
							<tr class="draft" >
								<td class="trtextaling">汇票到期日：</td>
								<td class="trtextalingi"><input type="text"
									name="draftAccountedDate" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${orderpay.draftAccountedDate}"/>" class="required"
									id="draftAccountedDate" onClick="WdatePicker()" readonly />
								</td>
							</tr>

							<tr class="shoukuan" id="huikuanbank">
								<td class="trtextaling" style="padding:0px;">收款银行：</td>
								<td class="trtextalingi"><c:choose>
										<c:when test="${fn:length(supplierBanks)>1}">
											<select id="toBankNname" name="toBankNname"
												onload="initValue('${supplierId}',[this.options[this.options.selectedIndex].value],'${orderpay.toBankAccount }')"
												onchange="changeOpenBank('${supplierId}',[this.options[this.options.selectedIndex].value])">
												<option value="-1">--请选择--</option>
												<c:forEach items="${supplierBanks}" var="bankInfo" varStatus="status">
													<option value="${supplierBanks[status.index]}"
														<c:if test="${supplierBanks[status.index] eq orderpay.toBankNname}">selected="selected"</c:if>>${supplierBanks[status.index]}</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<input type="text" id="toBankNname" name="toBankNname" maxlength="20" value="${orderpay.toBankNname }"/>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<tr class="shoukuan" id="huikuanaccount">
								<td class="trtextaling">收款账号：</td>
								<td class="trtextalingi"><c:choose>
										<c:when test="${fn:length(supplierBanks)>1}">
											<select id="toBankAccount" name="toBankAccount">
												<option value="-1">--请选择--</option>
											</select>
										</c:when>
										<c:otherwise>
											<input id="toBankAccount" type="text" name="toBankAccount" value="${orderpay.toBankAccount }"
												maxlength="21" />
										</c:otherwise>
									</c:choose>
								</td>
							</tr>
							<!--  汇款内容 结束-->
							<!-- 支票 -->
							<tr class="invoice">
								<td class="trtextaling">支票号：</td>
								<td class="trtextalingi"><input
									class="check_char_or_num required" maxlength="10" type="text"
									name="checkNumber" id="checkNumber"
									value="${orderpay.checkNumber }" />
								</td>
							</tr>
							<tr class="invoice">
								<td class="trtextaling">开票日期：</td>
								<td class="trtextalingi"><input type="text"
									name="invoiceDate"
									value="<fmt:formatDate pattern="yyyy-MM-dd " value="${orderpay.invoiceDate }"/>"
									class="required" id="invoiceDate" onClick="WdatePicker()"
									readonly />
								</td>
							</tr>
							<!-- 支票 结束-->
							
							<!-- 支付宝  224因公支付宝 2016.7.18 wangyang -->
							<tr class="alipay">
								<td class="trtextaling">支付宝名称（来款）：</td>
								<td class="trtextalingi">
									<input id="alipayName" type="text" name="fromAlipayName" value="${ orderpay.fromAlipayName }" maxlength="50"/>
								</td>
							</tr>
							<tr class="alipay">
								<td class="trtextaling">支付宝账号（来款）：</td>
								<td class="trtextalingi">
									<input id="alipayAccount" type="text" name="fromAlipayAccount" value="${orderpay.fromAlipayAccount }" maxlength="50"/>
								</td>
							</tr>
							<tr class="alipay">
								<td class="trtextaling">收款单位：</td>
								<td class="trtextalingi">
									<input id="comeOfficeName" type="text" name="comeOfficeName" value="${ orderpay.comeOfficeName}" maxlength="25"/>
								</td>
							</tr>
							<tr class="alipay">
								<td class="trtextaling">支付宝名称（收款）：</td>
								<td class="trtextalingi">
									<input id="toAlipayName" type="text" name="toAlipayName" value="${ orderpay.toAlipayName }" maxlength="50"/>
								</td>
							</tr>
							<tr class="alipay">
								<td class="trtextaling">支付宝账号（收款）：</td>
								<td class="trtextalingi">
									<input id="toAlipayAccount" type="text" name="toAlipayAccount" value="${ orderpay.toAlipayAccount }" maxlength="50"/>
								</td>
							</tr>
							<!-- 支付宝 结束 -->
							
							<tr class="accountDate">
								<td class="trtextaling"><span style="color:#f00;">*</span>银行到账日期：</td>
								<td class="trtextalingi"><input type="text"
									name="accountDate" value="${accountDate }" class="required"
									id="accountDate" onClick="WdatePicker()" />
								</td>
							</tr>

							<tr class="trVoucher_4">
								<td class="trtextaling payforFiles-t" width="">
<!-- 								<span id="cashPayDisplay" style="color:#f00;">*</span> -->
								收款凭证：</td>
								<td class="payforFiles">
									<!--<input type="button"  id ="DocInfoIds" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);"/>-->

									<ol class="batch-ol">
										<c:if test="${fn:length(docInfoList)>0}">
											<c:forEach items="${docInfoList}" var="docInfo"
												varStatus="status">
												<li><span style="float: left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;display: inline-block;width: 200px;"
															title="${docInfo.docName }">
														${docInfo.docName }</span>
													<a style='margin-left:10px;float: left;' href='javascript:void(0)'  class="batchDl"
													onClick='downloads(${docInfo.id})'>查看</a>&nbsp;&nbsp;<!--  <a class='batchDel' href='javascript:void(0)' onclick='deleteFileInfo(null,null,this)'>删除</a></li>-->
											</c:forEach>
										</c:if>
									</ol> <label
									style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
								</td>
							</tr>
							<tr>
								<td class="trtextaling" style="">备注信息：</td>
								<td class="trtextalingi" style="vertical-align:top">
									<textarea name="remarks" maxlength="254" style="width:500px; resize:none;" id="remarks">${orderpay.remarks }</textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
<!-- 	</div> -->
<!-- </div> -->
