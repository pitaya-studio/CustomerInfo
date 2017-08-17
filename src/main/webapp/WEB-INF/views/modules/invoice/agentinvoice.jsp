<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>渠道商申请发票</title>
<meta name="decorator" content="wholesaler"/>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<%--
<script src="${ctxStatic}/jquery-other/jquery-ui-1.8.10.custom.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>--%>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	    
//验证信息
$(document).ready(function(){
	$("#addForm").validate({
		errorPlacement: function(error, element) { 
    if ( element.is(":radio") ) 
        error.appendTo ( element.parent() ); 
    else if ( element.is(":checkbox") ) 
        error.appendTo ( element.parent() ); 
    else if ( element.is("input") ) 
        error.appendTo ( element.parent() ); 
    else 
        error.insertAfter(element ); 
	}
	});
	
	$("#btnSubmit").click(function(){
		var value = $("input[name='invoiceAmount']").val();
		var Head = $("input[name='invoiceHead']").val();
		var Customer = $("input[name='invoiceCustomer']").val();
		if(value=="0"||value==""||value==undefined){
			top.$.jBox.tip('开票金额不可为0','error');
			return false;
        }else{
           
          $("#addForm").submit();  

        }
	});
	
	$("#btnCancel").click(function(){
	     if(${fns:getUser().userType eq '1' }) {
		     window.location.href='${ctx}/invoice/limit/agentlist';
	     }else{
		     window.location.href='${ctx}/orderList/manage/showOrderList/0';
	     }
	  	 
	
	});
	jQuery.extend(jQuery.validator.messages, {
  		required: "必填信息",
  		number:	"输入信息有误",
  		digits: "输入信息有误",
  		email: "email格式错误"
		});
	
	});
	    
	    function makinvoice(limitid,groupid,orderid){
	    	window.location.href = "${ctx}/invoice/limit/agentinvoice/"+limitid+"/"+groupid+"/"+orderid;
	    	
	    }
	    function limitInvoiceChg(obj){
	    	var a=parseInt($(obj).val());
	    	if(isNaN(a)||a==""||a==undefined){
	    		a=0;
	    	}
	    	var b=$(obj).parent().prev().children().val();
	    	if(isNaN(b)||b==""||b==undefined){
	    		b=0;
	    	}
	    	
	    	
	    	if(a>b){
	    		top.$('.jbox-body .jbox-icon').css('top','55px');
			    top.$.jBox.info("已超过可用发票限额", "警告");
	    		$(obj).val(b);
	    	}
	    		var total = 0;
		    	$("input[name='curPrice']").each(function(){
		    		
		    	    var value = $(this).val();
		    	    if(isNaN(value)||value==""||value==undefined){
		    	    	value=0;
		            }
		    	    total = parseInt(total)+parseInt(value);
		    	});
		    	$("input[name='totalLimitPrice']").val(total);
		    	$("input[name='invoiceAmount']").val(total);
	    	
	    }
	  
	</script>
</head>
<body>
    <page:applyDecorator name="show_head">
        <page:param name="desc">渠道商填写发票申请</page:param>
    </page:applyDecorator>
	<div class="sup_detail_top">
<ul class="team_co clearFix vote-ul">
			<c:forEach items="${invoicelist}" var="invoice" varStatus="s">
				<c:if test="${invoice.pay.isAsAccount eq 1}">
					<c:set var="accountedMoneySumLabel" value="${accountedMoneySumLabel + invoice.pay.payPrice }"></c:set>
				</c:if>
			</c:forEach>
			<li><label>团号：</label>${limit['groupCode'] }</li>
			<li><label>出团日期：</label><fmt:formatDate pattern="yyyy-MM-dd" value="${limit['groupOpenDate'] }"/></li>
			<li><label>订单号：</label>${limit['orderNum'] }</li>
			<li><label>预订日期：</label><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${limit['orderTime'] }"/></li>
			<li class="clear"></li>
			<li><label>人数：</label>${limit['orderPersonNum'] } </li>
			<li><label>应收团款：</label>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalPrices'] }" />  </li>
			<li><label>财务到账：</label>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${accountedMoneySumLabel }" />  </li>
			<li><label>已开发票：</label>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['usePrice'] }" />  </li>
			</ul>
	</div>
	
	<form:form id="addForm" modelAttribute="invoice" action="${ctx}/invoice/limit/saveinvoice" method="post" class="form-search">
	<input name="groupCode" value="${limit['groupCode'] }" type="hidden"/>
	<input name="orderNum" value="${limit['orderNum'] }" type="hidden"/>
	<input name="orderId" value="${limit['orderId'] }" type="hidden"/>
	<input name="invoiceId" value="${invoice.id}" type="hidden"/>
	<input name="agentid" value="${param.agentid}" type="hidden"/>	
		 <table id="contentTable"  class="activitylist_bodyer_table" style="border-top:1px solid #dddddd">
      <thead>
			<tr>
				<th>付款日期</th>
				<th>付款方式</th>
				<th>付款类型</th>
				<th>付款金额</th>
				<th>达账金额</th>
				<th>已开票金额</th>
				<th>可开票金额</th>
				<th>本次开票金额</th>
			</tr>
		</thead>
		<tbody>
			<c:set var="totalPrice" value="0"></c:set>
			<c:set var="userPrice" value="0"></c:set>
			<c:set var="limitPrice" value="0"></c:set>
			<c:forEach items="${invoicelist}" var="invoice" varStatus="s">
				<c:set var="totalPrice" value="${totalPrice + invoice.pay.payPrice}"></c:set>
				<c:choose>
					<c:when test="${invoice.pay.isAsAccount eq 1}">
						<c:set var="accountedMoney" value="${invoice.pay.payPrice}"></c:set>
						<c:set var="userPrice" value="${invoice.limit.totalPrice - invoice.limit.limitPrice}"></c:set>
						<c:set var="residuePrice" value="${invoice.limit.limitPrice }"></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="accountedMoney" value="0"></c:set>
						<c:set var="userPrice" value="0"></c:set>
						<c:set var="residuePrice" value="0"></c:set>
					</c:otherwise>
				</c:choose>
				<c:set var="userPriceSum" value="${userPriceSum + userPrice}"></c:set>
				<c:set var="residuePriceSum" value="${residuePriceSum + residuePrice }"></c:set>
				<c:set var="accountedMoneySum" value="${accountedMoneySum + accountedMoney }"></c:set>
				<tr>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${invoice.pay.createDate}"/></td>
					<td>${invoice.pay.payTypeName}</td>
					<td>${invoice.payType}</td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${invoice.pay.payPrice}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${accountedMoney }" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${userPrice }" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${residuePrice }" /><input type="hidden" value="${residuePrice }"/></td>
					<td class="tc"><input name="limitids" value="${invoice.limit.id }" type="hidden"/>
						<input name="curPrice" value="${residuePrice }" onBlur="limitInvoiceChg(this)" class="inputTxt rmb" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/[^\d\+\-]/g,'')"/></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3">小计</td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${totalPrice}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${accountedMoneySum}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${userPriceSum}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${residuePriceSum}" /></td>
				<td class="tc"><input name="totalLimitPrice" class="inputTxt rmb" value="${residuePriceSum }" readonly/></td>
			</tr>				
		</tbody>
		</table>
		 <ul class="team_co clearFix team_co_agent">
      <li>
		
			<label><span class="xing">*</span>开票方式：</label>
			<form:select id="invoiceMode" path="invoiceMode" itemValue="key" itemLabel="value" cssClass="required">
				<form:option value="">不限</form:option>
				<form:options items="${invoiceModes}" />
			</form:select>
			</li>
			<li><label><span class="xing">*</span>开票类型：</label>
			<form:select id="invoiceType" path="invoiceType" itemValue="key" itemLabel="value" cssClass="required">
				<form:option value="">不限</form:option>
				<form:options items="${invoiceTypes}" />
			</form:select></li>

			<li><label>开票金额：</label><input name="invoiceAmount" type="text" readonly value="${residuePriceSum }"/></li>
			<li class="clear"></li>
			<li><label><span class="xing">*</span>发票抬头：</label><input type="text" name="invoiceHead" value="" class="required"/></li>
			<li><label><span class="xing">*</span>开票客户：</label><input type="text" name="invoiceCustomer" value="" class="required"/></li>
			
			<li><label><span class="xing">*</span>开票项目：</label>
			<form:select id="invoiceSubject" path="invoiceSubject" itemValue="key" itemLabel="value" cssClass="required">
				<form:option value="">不限</form:option>
				<form:options items="${invoiceSubjects}" />
			</form:select></li>
			
		</ul>
		<div class="release_next_add" style="margin-top:50px;">
				<input id="btnCancel" class="btn btn-primary" type="button" value="返回" />
				<input id="btnSubmit" class="btn btn-primary" type="button" value="申请" />
			</div>
	</form:form> 
</body>
</html>