<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>

<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
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
      }else if(payType==3 || payType==5){//现金
    	  $("#payorderbgcolor div[class=patorder_a1]").hide();
		  $("#payorderbgcolor div[class=patorder_a2]").hide();
		  $("#payorderbgcolor div[class=patorder_a3]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		  $("#payorderbgcolor div[class=patorder_a4]").hide();
		  $("#payorderbgcolor div[class=patorder_a5]").hide();
		  $("#payorderbgcolor div[class=patorder_a6]").hide();
		  $("#payorderbgcolor div[class=patorder_a7]").hide();
      }else if(payType==4){//汇款
    	  $("#payorderbgcolor div[class=patorder_a1]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
    	  $("#payorderbgcolor div[class=patorder_a2]").hide();
		  $("#payorderbgcolor div[class=patorder_a3]").hide();
		  $("#payorderbgcolor div[class=patorder_a4]").hide();
		  $("#payorderbgcolor div[class=patorder_a5]").hide();
		  $("#payorderbgcolor div[class=patorder_a6]").hide();
		  $("#payorderbgcolor div[class=patorder_a7]").hide();
      }else if(payType==6){
    	  $("#payorderbgcolor div[class=patorder_a1]").hide();
		  $("#payorderbgcolor div[class=patorder_a2]").hide();
		  $("#payorderbgcolor div[class=patorder_a3]").hide();
		  $("#payorderbgcolor div[class=patorder_a4]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		  $("#payorderbgcolor div[class=patorder_a5]").hide();
		  $("#payorderbgcolor div[class=patorder_a6]").hide();
		  $("#payorderbgcolor div[class=patorder_a7]").hide();
      }else if(payType==7){
    	  $("#payorderbgcolor div[class=patorder_a1]").hide();
		  $("#payorderbgcolor div[class=patorder_a2]").hide();
		  $("#payorderbgcolor div[class=patorder_a3]").hide();
		  $("#payorderbgcolor div[class=patorder_a4]").hide();
		  $("#payorderbgcolor div[class=patorder_a5]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		  $("#payorderbgcolor div[class=patorder_a6]").hide();
		  $("#payorderbgcolor div[class=patorder_a7]").hide();
      }else if(payType==8){
    	  $("#payorderbgcolor div[class=patorder_a1]").hide();
		  $("#payorderbgcolor div[class=patorder_a2]").hide();
		  $("#payorderbgcolor div[class=patorder_a3]").hide();
		  $("#payorderbgcolor div[class=patorder_a4]").hide();
		  $("#payorderbgcolor div[class=patorder_a5]").hide();
		  $("#payorderbgcolor div[class=patorder_a6]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		  $("#payorderbgcolor div[class=patorder_a7]").hide();
      }else if(payType == 9) {// 因公支付宝
      	  $("#payorderbgcolor div[class=patorder_a1]").hide();
      	  $("#payorderbgcolor div[class=patorder_a2]").hide();
      	  $("#payorderbgcolor div[class=patorder_a3]").hide();
      	  $("#payorderbgcolor div[class=patorder_a4]").hide();
      	  $("#payorderbgcolor div[class=patorder_a5]").hide();
      	  $("#payorderbgcolor div[class=patorder_a6]").hide();
      	  $("#payorderbgcolor div[class=patorder_a7]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
      }
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
</script>
<style>
input, input[type="text"] {
	height: 35px;
	line-height: 25px;
	padding: 4px 6px;
	font-size: 12px;
}

.trtextaling, .list {
	font-size: 12px;
}
</style>
		<div id="offline_paybox" class="pay_clearfix" style="clear: both;">
			<div id="payorderbgcolor"
				style="z-index: 2; height: 30px; position: relative;">
				<div onclick="tabshow(this,'4')" class="patorder_a1"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇款</div>
				<div onclick="tabshow(this,'1')" class="patorder_a2"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: inherit;">支票</div>
				<div onclick="tabshow(this,'3')" class="patorder_a3"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">现金支付</div>
				<div onclick="tabshow(this,'6')" class="patorder_a4"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">银行转账</div>
				<div onclick="tabshow(this,'7')" class="patorder_a5"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇票</div>
				<div onclick="tabshow(this,'8')" class="patorder_a6"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">POS机刷卡</div>
				<div onclick="tabshow(this,'9')" class="patorder_a7"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius: 3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">因公支付宝</div>
			</div>
			<div id="pcot" class="payORDER"
				style="heigth: 450; clear: both; padding: 10px; border: 1px #cccccc solid; margin-top: -2px; margin-top: -1px; background-color: #FFF;">
				<input type="hidden" name="payType1">
				<div id="offlinebox" style="" class="payDiv">
					<input type="hidden" name="payGroupId" id="payGroupId" value="${payGroup.id}" /> 
					<input type="hidden" name="payType" id="payType" value="${payGroup.payType }" />
					<table width="100%" cellpadding="5" cellspacing="0" border="0">
						<tbody>
							<tr>
								<td class="trtextaling">收款金额：</td>
								<td class="trtextalingi">
									<input type="text" name="payPrice" id="payPrice" readonly="readonly" 
										value="${fns:getMoneyBySerialNumAndOrderType(payGroup.payPrice,2,payGroup.orderType)}" />
								</td>
							</tr>
							<c:if test="${payGroup.payType == 1}"> <!-- 支票 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">来款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" />
									</td>
								</tr>
								<tr class="invoice">
									<td class="trtextaling">支票号：</td>
									<td class="trtextalingi">
										<input class="check_char_or_num required" maxlength="10" type="text"
										name="checkNumber" id="checkNumber" value="${payGroup.checkNumber }" />
									</td>
								</tr>
								<tr class="invoice">
									<td class="trtextaling">开票日期：</td>
									<td class="trtextalingi">
										<input type="text" name="invoiceDate"
											value="<fmt:formatDate pattern="yyyy-MM-dd " value="${payGroup.invoiceDate }"/>"
											class="required" id="invoiceDate" onclick="WdatePicker()" disabled="disabled" />
									</td>
								</tr>
							</c:if>
							<c:if test="${payGroup.payType == 3 || payGroup.payType == 5}"> <!-- 现金支付 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">来款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" />
									</td>
								</tr>
							</c:if>
							<c:if test="${payGroup.payType == 4}"> <!-- 汇款 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">来款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
												来款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="bankName" maxlength="20" id="bankName"
												class="required" value="${payGroup.bankName}" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
												来款账号：</td>
									<td class="trtextalingi">
										<input id="bankAccount" type="text" name="bankAccount"
												value="${payGroup.bankAccount }" maxlength="21"
												class="required" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;">收款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="toBankName" id="toBankName" maxlength="20"
											value="${payGroup.toBankNname }" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling">收款账号：</td>
									<td class="trtextalingi">
										<input id="toBankAccount" type="text" name="toBankAccount" maxlength="21"
											value="${payGroup.toBankAccount }" readonly="readonly" />
									</td>
								</tr>
							</c:if>
							<c:if test="${payGroup.payType == 6}"> <!-- 银行转账 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">来款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
												来款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="bankName" id="bankName" maxlength="20"
											class="required" value="${payGroup.bankName}" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
													来款账号：</td>
									<td class="trtextalingi">
										<input id="bankAccount" type="text" name="bankAccount" value="${payGroup.bankAccount }" 
												maxlength="21" class="required" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;">收款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="toBankName" id="toBankName" maxlength="20" 
											value="${payGroup.toBankNname }" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling">收款账号：</td>
									<td class="trtextalingi">
										<input id="toBankAccount" type="text" name="toBankAccount" maxlength="21"
											value="${payGroup.toBankAccount }" readonly="readonly" />
									</td>
								</tr>
							</c:if>
							<c:if test="${payGroup.payType == 7}"> <!-- 汇票 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">来款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
												来款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="bankName" id="bankName" maxlength="20"
											class="required" value="${payGroup.bankName}" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
													来款账号：</td>
									<td class="trtextalingi">
										<input id="bankAccount" type="text" name="bankAccount" value="${payGroup.bankAccount }" 
												maxlength="21" class="required" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;">收款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="toBankName" id="toBankName" maxlength="20" 
											value="${payGroup.toBankNname }" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling">收款账号：</td>
									<td class="trtextalingi">
										<input id="toBankAccount" type="text" name="toBankAccount" maxlength="21"
											value="${payGroup.toBankAccount }" readonly="readonly" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling">汇票到期日：</td>
									<td class="trtextalingi">
										<input type="text" name="draftAccountedDate" id="draftAccountedDate"
										value="<fmt:formatDate value="${payGroup.draftAccountedDate}" pattern="yyyy-MM-dd"/>" readonly="readonly" />
									</td>
								</tr>
							</c:if>
							<c:if test="${payGroup.payType == 8}"> <!-- POS机刷卡 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">来款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
												来款行名称：</td>
									<td class="trtextalingi">
										<input type="text" name="bankName" id="bankName" maxlength="20"
											class="required" value="${payGroup.bankName}" />
									</td>
								</tr>
								<tr>
									<td class="trtextaling" style="padding: 0px;"><span style="color: #f00;">*</span>
													来款账号：</td>
									<td class="trtextalingi">
										<input id="bankAccount" type="text" name="bankAccount" value="${payGroup.bankAccount }" 
												maxlength="21" class="required" />
									</td>
								</tr>
							</c:if>
							<c:if test="${payGroup.payType == 9 }">	<!-- 因公支付宝 -->
								<tr>
									<td class="trtextaling" style="padding: 0px;">付款单位：</td>
									<td class="trtextalingi">
										<input type="text" name="payerName" id="payerName" value="${payGroup.payerName }" maxlength="25"/>
									</td>
								</tr>
								<tr>
									<td class="trtextaling">支付宝名称（来款）：</td>
									<td class="trtextalingi">
										<input id="fromAlipayName" type="text" name="fromAlipayName" value="${payGroup.fromAlipayName }" maxlength="50"/>
									</td>
								</tr>
								<tr>
									<td class="trtextaling">支付宝账号（来款）：</td>
									<td class="trtextalingi">
										<input id="fromAlipayAccount" type="text" name="fromAlipayAccount" value="${payGroup.fromAlipayAccount }" maxlength="50"/>
									</td>
								</tr>
								<tr>
									<td class="trtextaling">收款单位：</td>
									<td class="trtextalingi">
										<input id="comeOfficeName" type="text" name="comeOfficeName" value="${payGroup.comeOfficeName }" maxlength="25"/>
									</td>
								</tr>
								<tr>
									<td class="trtextaling">支付宝名称（收款）：</td>
									<td class="trtextalingi">
										<input id="toAlipayName" type="text" name="toAlipayName" value="${payGroup.toAlipayName }" maxlength="50"/>
									</td>
								</tr>
								<tr>
									<td class="trtextaling">支付宝账号（收款）：</td>
									<td class="trtextalingi">
										<input id="toAlipayAccount" type="text" name="toAlipayAccount" value="${payGroup.toAlipayAccount }" maxlength="50"/>
									</td>
								</tr>
							</c:if>
							<tr>
								<td class="trtextaling"><span style="color: #f00;">*</span>银行到账日期:</td>
								<td class="trtextalingi">
									<input type="text" name="accountDate" value="<fmt:formatDate value="${payGroup.accountDate }" pattern="yyyy-MM-dd"/>" class="required"
									id="accountDate" onclick="WdatePicker()" onchange="isnull()" /></td>
							</tr>
							<tr class="trVoucher_4">
								<td class="trtextaling payforFiles-t" width="">
<!-- 								<span id="cashPayDisplay" style="color: #f00;">*</span> -->
								收款凭证：</td>
								<td class="payforFiles">
									<ol class="batch-ol">
										<c:if test="${fn:length(voucherList)>0}">
											<c:forEach items="${voucherList}" var="docInfo" varStatus="status">
												<li>
													<span style="float: left;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;display: inline-block;width: 200px;"
															title="${docInfo.docName }">${docInfo.docName }</span>
													<a style='margin-left: 10px;float: left;' href='javascript:void(0)'
													onclick='downloads(${docInfo.id})'>查看</a>&nbsp;&nbsp;
											</c:forEach>
										</c:if>
									</ol> <label
									style="display: none; font-size: 12px; font-weight: normal; padding-left: 0; 
									margin-left: 10px; padding-bottom: 2px; width: 60px; color: #ea5200">必填信息</label>
								</td>
							</tr>
							<tr>
								<td class="trtextaling" style="">备注信息：</td>
								<td class="trtextalingi" style="vertical-align: top"><textarea
										name="remarks" maxlength="254"
										style="width: 500px; resize: none;" id="remarks">${payGroup.remarks }</textarea>
								</td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
