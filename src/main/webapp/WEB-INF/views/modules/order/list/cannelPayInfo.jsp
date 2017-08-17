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
	$("#offline_paybox  [flag='select']").each(function(index, element) {
                        $(element).jQSelect({});
                    });
      var payType= $("#payType").val();
      if(payType==1){//支票
            $("#payorderbgcolor div[class=patorder_a1]").hide();
		    $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a2]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
		    $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
		    $("#offlinebox tr[class=huikuan]").hide();
		    $("#offlinebox tr[class=shoukuan]").hide();
	        $("#offlinebox tr[class=invoice]").show();
	        $("#offlinebox tr[class=draft]").hide();
      }else if(payType==3 || payType==5){//现金
           $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
            $("#offlinebox tr[class=huikuan]").hide();
            $("#offlinebox tr[class=shoukuan]").hide();
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=draft]").hide();
	        $("#cashPayDisplay").hide();
      }else if(payType==4){//汇款
            $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
	        $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
	        $("#payorderbgcolor div[class=patorder_a1]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").show();
	        $("#offlinebox tr[class=draft]").hide();
      }else if(payType==6){//银行转账
    	  	$("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
	        $("#payorderbgcolor div[class=patorder_a4]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").show();
	        $("#offlinebox tr[class=draft]").hide();
      }else if(payType==7){//汇票
    	    $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a6]").hide();
	        $("#payorderbgcolor div[class=patorder_a5]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").hide();
	        $("#offlinebox tr[class=shoukuan]").show();
	        $("#offlinebox tr[class=draft]").show();
      }else if(payType==8){//POS机刷卡
    	    $("#payorderbgcolor div[class=patorder_a1]").hide();
	        $("#payorderbgcolor div[class=patorder_a2]").hide();
	        $("#payorderbgcolor div[class=patorder_a3]").hide();
		    $("#payorderbgcolor div[class=patorder_a4]").hide();
		    $("#payorderbgcolor div[class=patorder_a5]").hide();
	        $("#payorderbgcolor div[class=patorder_a6]").show().css({"color":"#3A7851","backgroundColor":"#FFF"});
	        $("#offlinebox tr[class=invoice]").hide();
	        $("#offlinebox tr[class=huikuan]").show();
	        $("#offlinebox tr[class=shoukuan]").hide();
	         $("#offlinebox tr[class=accountDate]").hide();
	        $("#offlinebox tr[class=draft]").hide();
      }
});
function tabshow(obj,str){
        $(obj).css({"color":"#3A7851","backgroundColor":"#FFF"}).siblings().css({"color":"#000","backgroundColor":""});
		$(obj).parent().siblings().children('#'+str).show().siblings().hide();
}





</script>
<style>

input,input[type="text"]{height:25px;padding:4px 6px;font-size:12px;}
.trtextaling,.list{font-size:12px;}
</style>
<div class="payforDiv" style="margin-left:0">
	<div id="offline_paybox" class="pay_clearfix" style="clear: both;">
     
		<div id="payorderbgcolor" style=" z-index: 2;height:30px; position:relative;">
			<div  onclick="tabshow(this,'4')" class="patorder_a1" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇款</div>
			<div  onclick="tabshow(this,'1')"class="patorder_a2" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: inherit;">支票</div>
			<div  onclick="tabshow(this,'3')" class="patorder_a3" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">现金支付</div> 
	        <div onclick="tabshow(this,'6')" class="patorder_a4"
				style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">银行转账</div>
			<div onclick="tabshow(this,'7')" class="patorder_a5"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇票</div>
			<div onclick="tabshow(this,'8')" class="patorder_a6"
					style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">POS机刷卡</div>
			</div>
		
		<div id="pcot" class="payORDER"  style="heigth:450;clear:both; padding:10px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">
		    <input type="hidden"  name="payType1">
		<div id="offlinebox" style=""  class="payDiv">
                   <input type="hidden"  name="refundId"  id="refundId"  value="${payInfoDetail.id}"/>
                   <input type="hidden"  name="payType"  id="payType" value="${payInfoDetail.payType}"/>
                   <input type="hidden" name="recordId" id="recordId" value="${payInfoDetail.recordId}"/>
                   
					<table width="100%" cellpadding="5" cellspacing="0" border="0">
						<tbody>
							<tr>
								<td colspan= '2'>
								            <table class="payment-log-view">
								                <thead>
								                    <tr>
								                        <th class="tc" width="130">项目名称</th>
								                        <th class="tc" width="300">金额</th>
								                    </tr>
								                </thead>
								                <tbody>
								                    <tr>
								                        <td class="tc">付款金额</td>
									                        <c:choose>
												               <c:when test="${payInfoDetail.mergePayFlag==0 }">
												                  <td class="tl">${payInfoDetail.refundDispStyle }</td>
												               </c:when>
												               <c:otherwise>
												                  <td class="tl">${payInfoDetail.refundDispStyle}</td>
												               </c:otherwise>
												             </c:choose>
								                    </tr>
								                     <c:forEach items="${payFees }" var="payFee">
												        <tr>
									                        <td class="tc">${payFee.feeName }</td>
									                        <td class="tl"><span class="money"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${payFee.feeCurrencyId }"/><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${payFee.feeAmount}" /></span></td>
									                    </tr>
													</c:forEach>
								                    <tr>
								                        <td class="tc">合计付款金额</td>
								                        <td class="tl">${payInfoDetail.moneyDispStyle}</td>
								                    </tr>
								                </tbody>
								            </table>
								</td>
							</tr>
					        <tr>
	                            <td class="trtextaling" style="padding:0px;">收款单位：</td>
	                            <td class="trtextalingi"><input type="text" name="payerName" id="payerName"  value="${payInfoDetail.payerName }"/>    	</td>
                            </tr>
                            
                            
                             <tr class="draft" >
								<td class="trtextaling">出票人名称：</td>
								<td class="trtextalingi"><input type="text" name="drawerName" value="${payInfoDetail.drawerName }"/></td>
                             </tr>
                             <tr class="draft" >
								<td class="trtextaling">出票人账号：</td>
								<td class="trtextalingi"><input type="text" name="drawerAccount" value="${payInfoDetail.drawerAccount }"/></td>
                             </tr>
                             <tr class="draft" >
								<td class="trtextaling">付款行全称：</td>
								<td class="trtextalingi"><input type="text" name="payBankName" value="${payInfoDetail.payBankName }"/></td>
                             </tr>
							</div>
							
                            <!-- 汇票到期日20150619  -->
							<tr class="draft" >
								<td class="trtextaling">汇票到期日：</td>
								<td class="trtextalingi"><input type="text"
									name="draftAccountedDate" value="${payInfoDetail.draftAccountedDate}" class="required"
									id="draftAccountedDate" onClick="WdatePicker()" readonly />
								</td>
							</tr>
					        <!--  汇款内容 -->
                           
                            <tr class="shoukuan" id="huikuanbank">
	                            <td class="trtextaling" style="padding:0px;">收款银行：</td>
	                            <td class="trtextalingi">
							          <input   type="text" name="toBankName"  id="toBankName"  maxlength="20"  value="${payInfoDetail.tobankName }" onchange="changeToBankName(this)" readonly="readonly"/>
							    </td>
                            </tr>
                            <tr class="shoukuan" id="huikuanaccount">
	                            <td class="trtextaling">收款账户：</td>
	                            <td class="trtextalingi">
										<input id="toBankAccount" type="text" name="toBankAccount"  maxlength="21" value="${payInfoDetail.tobankAccount }" readonly="readonly"/>
									</td>	
							</tr>
							
                            <tr class="huikuan">
	                            <td class="trtextaling" style="padding:0px;">汇款行名称：</td>
								<td class="trtextalingi"><input type="text" name="bankName" maxlength="20" class="required" value="${payInfoDetail.bankName}"/></td>
							 </tr>
                            <tr class="huikuan">
	                            <td class="trtextaling" style="padding:0px;">汇款账户：</td>
								<td class="trtextalingi"><input id="bankAccount" type="text" name="bankAccount" value="${payInfoDetail.bankAccount }"/></td>
							</tr>
							<!--  汇款内容 结束-->
							
							
							<!-- 支票 -->
							   <tr class="invoice">
									<td  class="trtextaling" >支票号：</td>
									<td class="trtextalingi"><input class="check_char_or_num required"  maxlength="10"  type="text"  name="checkNumber"  id="checkNumber"  value="${payInfoDetail.checkNumber }"/></td>
								</tr >
								<tr class="invoice">
									<td  class="trtextaling" >开票日期：</td>
									<td class="trtextalingi"><input type="text" name="invoiceDate" value ="<fmt:formatDate pattern="yyyy-MM-dd " value="${payInfoDetail.invoiceDate }"/>"class="required"   id="invoiceDate"  onClick="WdatePicker()" readonly/></td>
								</tr>
							
							<!-- 支票 结束-->
							
							
							<tr class="trVoucher_4">
								<td class="trtextaling payforFiles-t"  width="">支付凭证：</td>
								<td class="payforFiles"><!--<input type="button"  id ="DocInfoIds" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);"/>-->
								
								<ol class="batch-ol" style="font-size:12px !important;">
								         <c:if test="${fn:length(payInfoDetail.docInfoList)>0}">
								            	<c:forEach items="${payInfoDetail.docInfoList}" var="docInfo" varStatus="status">
								               <li><span>${docInfo.docName }</span></li>
								               </c:forEach>
								         </c:if>
								</ol>
								<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label></td>
							</tr>
							
						</tbody>
					</table>
			</div>
		
			
		</div>
		
</div>
</div>
