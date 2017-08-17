<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
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
     
      var payType= $("#payType").val();
      if(payType==1){//支票
            $("#payorderbgcolor div[class=patorder_a1]").hide();
		    $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a2]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		    $("#offlinebox tr[class=huikuan]").hide();
	        $("#offlinebox tr[class=invoice]").show();
      }else if(payType==3){//现金
           $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
            $("#offlinebox tr[class=huikuan]").hide();
	        $("#offlinebox tr[class=invoice]").hide();  
      }else if(payType==4){//汇款
            $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
	        $("#payorderbgcolor div[class=patorder_a1]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
      }
});
function tabshow(obj,str){
        $(obj).css({"color":"#3A7851","backgroundColor":"#FFF"}).siblings().css({"color":"#000","backgroundColor":""});
		$(obj).parent().siblings().children('#'+str).show().siblings().hide();
}

function downloads(docid){
		window.open("${ctx}/sys/docinfo/download/"+docid);
    }

//开户行
function changeOpenBank(supplierId,bankName){
    $("#toBankName").attr("value",bankName);
	bankName = encodeURI(encodeURI(bankName));
	if(bankName!=-1){
		$.ajax({
			 type: "POST",
			 url: "${ctx}/orderPay/getOpenBankById/"+supplierId,                       
	        cache: false,
	        async: false,
	        data:"bankName="+bankName,
	        dataType: "json",//返回的数据类型  
	        success: function (data){
		       	var options='';
		       	if(data!=null){
		       		for(var i=0;i<data.length;i++){
		       			options+='<option value="'+data[i][5]+'">'+data[i][5]+'</option>';
		       		}
		       	}
		       	$("#skbankAccount").html('');
		       	$("#skbankAccount").append(options);
				
	        },
	        error: function (){
	      	 	alert('返回数据失败');
	        }
	   });
	}
}

function changeToAccount(obj){
        $("#tobankAccount").attr("value",obj.value);
}
function changeToBankName(obj){
      $("#toBankName").attr("value",obj.value);
}

function changeBankName(obj){
    $("#bankName").attr("value",obj.value);
}
function changeBankAccount(obj){
    $("#bankAccount").attr("value",obj.value);
}
function changeToBank(supplierId,bankName){
	bankName = encodeURI(encodeURI(bankName));
	$("#bankName").attr("value",bankName);
	if(bankName!=-1){
		$.ajax({
			 type: "POST",
			 url: "${ctx}/orderPay/getAgentOpenBankById/"+supplierId,                       
	        cache: false,
	        async: false,
	        data:"bankName="+bankName,
	        dataType: "json",//返回的数据类型  
	        success: function (data){
		       	var options='';
		       	if(data!=null){
		       		for(var i=0;i<data.length;i++){
		       			options+='<option value="'+data[i][5]+'">'+data[i][5]+'</option>';
		       		}
		       	}
		       	$("#lkbankAccount").html('');
		       	$("#lkbankAccount").append(options);
				
	        },
	        error: function (){
	      	 	alert('返回数据失败');
	        }
	   });
	}
}
</script>
<style>

input,input[type="text"]{height:25px;line-height:25px;padding:4px 6px;font-size:12px;}
</style>

	<div id="offline_paybox" class="pay_clearfix" style="clear: both;">
     <!--  
		<div id="payorderbgcolor" style=" z-index: 2;height:30px; position:relative;">
			<div  onclick="tabshow(this,'4')" class="patorder_a1" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇款</div>
			<div  onclick="tabshow(this,'1')"class="patorder_a2" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: inherit;">支票</div>
			<div  onclick="tabshow(this,'3')" class="patorder_a3" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">现金支付</div> 
	  </div>
		-->
		<div id="pcot" class="payORDER"  style="heigth:450;clear:both; padding:10px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">
		    <input type="hidden"  name="payType1">
		<div id="offlinebox" style=""  class="payDiv">
                   <input type="hidden"  name="orderPayId"  id="orderPayId"  value="${orderpay. id}"/>
                   <input type="hidden"  name="payType"  id="payType" value="${orderpay.payType }"/>
                   <input type="hidden"   name="toBankName"   id="toBankName" value="${orderpay.toBankNname }"/>
                   <input type="hidden"   name="toBankAccount"   id="toBankAccount" value="${orderpay.toBankAccount }"/>
                   <input type="hidden" name="bankName"  id="bankName"  maxlength="21"  value="${orderpay.bankName}"  /> 
                   <input type="hidden" name="bankAccount"   id="bankAccount" maxlength="21"  value="${orderpay.bankAccount}"  /> 
					<table width="100%" cellpadding="5" cellspacing="0" border="0">
						<tbody>
					        <tr>
					             <td class="trtextaling">收款金额：</td>
					             <td class="trtextalingi"><input type=“text”  readonly="readonly" name="payPrice"  id="payPrice"  value="${fns:getMoneyAmountBySerialNum(orderpay.moneySerialNum,2)}"/></td>
					        </tr>
					        <tr>
	                            <td class="trtextaling" style="padding:0px;">付款单位：</td>
	                            <td class="trtextalingi"><input type="text" name="payerName" id="payerName"  value="${orderpay.payerName }"/>    	</td>
                            </tr>
					        <!--  汇款内容 -->
							
                            <tr class="huikuan">
	                            <td class="trtextaling" style="padding:0px;">来款银行名称：</td>
	                            <td class="trtextalingi"><c:choose>
									<c:when test="${fn:length(agentBanks)>0 }">
										<select  id="lkbank" name="bankName" onchange="changeToBank('${agentId}',[this.options[this.options.selectedIndex].value])">
			                            	<option value="-1">--请选择--</option>
				                            <c:forEach items="${agentBanks}" var="bankInfo">
				                            	<option <c:if test="${orderpay.toBankNname eq bankInfo.bankName}">selected="selected"</c:if>  value="${bankInfo.bankName}">${bankInfo.bankName}</option>
				                            </c:forEach>
		                            	</select>
									</c:when>
									<c:otherwise>
										<input type="text" name="bankName" maxlength="21"  value="${orderpay.bankName}" onchange="changeBankName(this)"/> 
									</c:otherwise>                            	
                            	</c:choose> </td>
                            </tr>
                           
                            <tr class="huikuan" id="huikuanbank">
	                            <td class="trtextaling" style="padding:0px;">收款银行：</td>
	                            <td class="trtextalingi">
									<c:choose>
									<c:when test="${fn:length(supplierBankNames)>0}">
													<select id="skbank" name="toBankNname" onchange="changeOpenBank('${supplierId}',[this.options[this.options.selectedIndex].value])">
														<option value="-1">--请选择--</option>
														<c:forEach items="${supplierBankNames}" var="bankInfo" varStatus="status">
															<option <c:if test="${orderpay.toBankNname eq  supplierBankNames[status.index]}">selected="selected"</c:if>  value="${supplierBankNames[status.index]}">${supplierBankNames[status.index]}</option>
														</c:forEach>
													</select>
									</c:when>
									<c:otherwise>
										<input onblur=""  type="text" name="toBankNname"  maxlength="20"  value="${orderpay.toBankNname }" onchange="changeToBankName(this)"/>
									</c:otherwise>
								</c:choose>
								</td>
                            </tr>
                            <tr class="huikuan" id="huikuanaccount">
	                            <td class="trtextaling">收款账户：</td>
	                            <td class="trtextalingi">
	                            <c:choose>
									<c:when test="${fn:length(supplierBanks)>0}">
										<select id="skbankAccount" name="bankAccount"  onchange="changeToAccount(this)">
											<option value="-1">--请选择--</option>
										</select>
									</c:when>
									<c:otherwise>
										<input id="tobankAccount" type="text" name="tobankAccount"  maxlength="21" value="${orderpay.toBankAccount }"/>
									</c:otherwise>
								</c:choose>
									</td>	
							</tr>
							<tr class="invoice">
									<td  class="trtextaling" >收款日期：</td>
									<td class="trtextalingi"><input type="text" name="invoiceDate" value ="<fmt:formatDate pattern="yyyy-MM-dd " value="${orderpay.invoiceDate }"/>"class="required"   id="invoiceDate"  onClick="WdatePicker()" readonly/></td>
							</tr>
							<tr class="trVoucher_4">
								<td class="trtextaling payforFiles-t"  width=""><span style="color:#f00;">*</span>支付凭证：</td>
								<td class="payforFiles"><input type="button"  id ="DocInfoIds" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);"/>
								
								<ol class="batch-ol">
								         <c:if test="${fn:length(docInfoList)>0}">
								            	<c:forEach items="${docInfoList}" var="docInfo" varStatus="status">
								               <li><span>${docInfo.docName }</span><a style='margin-left:10px;' href='javascript:void(0)' onClick='downloads(${docInfo.id})'>下载</a>&nbsp;&nbsp;<a class='batchDel' href='javascript:void(0)' onclick='deleteFileInfo(null,null,this)'>删除</a></li>
								               </c:forEach>
								         </c:if>
								</ol>
								<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label></td>
							</tr>
							<tr>
								<td class="trtextaling" style="">备注信息：</td>
								<td class="trtextalingi" style="vertical-align:top" ><textarea
										name="remarks" maxlength="254"
										style="width:500px; resize:none;" id="remarks">${orderpay.remarks }</textarea>
								</td>
							</tr>
						</tbody>
					</table>
			</div>
		
			
		</div>
		
	</div>
		</div>
		

