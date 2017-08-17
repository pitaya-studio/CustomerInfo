<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title><c:if test="${param.audited eq '1'}">开具发票</c:if><c:if test="${empty param.audited}">发票审核</c:if></title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
	    
	    $(document).ready(function() {
        
	    	$("#searchForm").validate({});
	    	
		    jQuery.extend(jQuery.validator.messages, {
	            required: "必填信息",
	            digits:"请输入正确的数字",
	            number : "请输入正确的数字价格"
	        });
        
	    });
	    function makinvoice(limitid,groupid,orderid){
	    	window.location.href = "${ctx}/invoice/limit/supplyinvoice/"+limitid+"/"+groupid+"/"+orderid;
	    	
	    }
	    function limitInvoiceChg(){
	    	var total = 0;
	    	$("input[name='curPrice']").each(function(){
	    	    var value = $(this).val();
	    	    total = total+value;
	    	});
	    	$("input[name='totalLimitPrice']").val(total);
	    	$("input[name='invoiceAmount']").val(total);
	    }
	    function denyinvoice(){
	    	$("#searchForm").attr("action","${ctx}/invoice/limit/denyinvoice");
			$("#searchForm").submit();
	    }
	    
	    function createInvoiceSubmit() {
	    	var inoviceNum = $("[name='invoiceNum']").val();
	    	var validate = $("#searchForm").valid();
	    	if(validate) {
		    	$.ajax({
		    		type:"POST",
		    		url:"${ctx}/invoice/limit/invoiceNum",
		    		data:{
		    			inoviceNum : inoviceNum
		    		},
		    		success:function(msg) {
		    			if(msg=="true") {
		    				createInvoiceByInvoiceNum();
		    			}else{
		    				var submit = function (v, h, f) {
						    if (v == 'ok') {
						        createInvoiceByInvoiceNum();
						    }
						    if (v == 'no') {
						    }
						    if (v == 'cancel') {
						    	$("[name='invoiceNum']").select();
						    }
						    	return true;
							};
						
							$.jBox.confirm("该发票号已经存在，是否再次使用？", "提示", submit ,{persistent: true});
		    			}
		    		}
		    	});
		    }
	    }
	    
	    function createInvoiceByInvoiceNum() {
	    	$("#createInvoiceHid").val("true");
	    	$("#searchForm").submit();
	    }
	    
	</script>
</head>
<body>
    <page:applyDecorator name="show_head">
        <page:param name="desc"><c:if test="${param.audited eq '1'}">开具发票</c:if><c:if test="${empty param.audited}">发票审核</c:if></page:param>
    </page:applyDecorator>
	<div class="sup_detail_top">
<ul class="team_co clearFix vote-ul">
	
			<li><label>团号：</label><input value="${limit['groupCode'] }" type="hidden"/>${limit['groupCode'] }</li>
			<li><label>出团日期：</label><input value="<fmt:formatDate pattern="yyyy-MM-dd" value="${limit['groupOpenDate'] }"/>" type="hidden"/><fmt:formatDate pattern="yyyy-MM-dd" value="${limit['groupOpenDate'] }"/></li>
			<li><label>订单号：</label><input value="${limit['orderNum'] }" type="hidden"/>${limit['orderNum'] }</li>
			<li><label>预订日期：</label><input value="<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${limit['orderTime'] }"/>" type="hidden"/><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${limit['orderTime'] }"/></li>
			<li class="clear"></li>
			<li><label>人数：</label><input value="${limit['orderPersonNum'] }" type="hidden"/>${limit['orderPersonNum'] }</li>
			<li><label>应收团款：</label><input value="${limit['totalPrices'] }" type="hidden"/>${limit['totalPrices'] }</li>
			<li><label>已收团款：</label><input value="${limit['totalPrices'] }" type="hidden"/>${limit['totalPrices'] }</li>
			<li><label>已开发票：</label><input value="${limit['usePrice'] }" type="hidden"/>${limit['usePrice'] }</li></ul>
	</div>
	<tags:message content="${message}"/>
	<form:form id="searchForm" action="${ctx}/invoice/limit/verifyinvoice" method="post" class="breadcrumb form-search">
	<input name="invoiceid" value="${invoice.id }" type="hidden"/>
	<input id="createInvoiceHid" name="createInvoiceHid" value="" type="hidden"/>
		<table id="contentTable"  class="activitylist_bodyer_table" style="border-top:1px solid #dddddd">
      <thead>
			<tr>
<%--				<th>付款日期</th>--%>
<%--				<th>付款方式</th>--%>
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
				<c:set var="sumTotalPrice" value="${sumTotalPrice + invoice.limit.totalPrice}"></c:set>
				<c:set var="sumpayPrice" value="${sumpayPrice + invoice.pay.payPrice}"></c:set>
				<c:set var="sumAccountedMoney" value="${sumAccountedMoney + invoice.accountedMoney}"></c:set>
				<c:set var="sumLimitPrice" value="${sumLimitPrice + (invoice.limit.totalPrice - invoice.limit.limitPrice)}"></c:set>
				<c:set var="sumCanLimitPrice" value="${sumCanLimitPrice + invoice.pay.payPrice - (invoice.limit.totalPrice-invoice.limit.limitPrice) }"></c:set>
				<c:set var="sumLimitCurPrice" value="${sumLimitCurPrice + invoice.limit.curPrice}"></c:set>
<%--					<td><fmt:formatDate pattern="yyyy-MM-dd" value="${invoice.pay.createDate}"/></td>--%>
<%--					<td>${invoice.pay.payTypeName}</td>--%>
<%--					<td>¥<fmt:formatNumber type="currency" pattern="#,###" value="${invoice.limit.totalPrice}" /></td>--%>
<%--					<td>¥<fmt:formatNumber type="currency" pattern="#,###" value="${invoice.pay.payPrice}" /></td>--%>
<%--					<td>¥<fmt:formatNumber type="currency" pattern="#,###" value="${invoice.limit.totalPrice-invoice.limit.limitPrice}" /></td>--%>
<%--					<td>¥<fmt:formatNumber type="currency" pattern="#,###" value="${invoice.pay.payPrice - (invoice.limit.totalPrice-invoice.limit.limitPrice) }" /></td>--%>
				<input name="limitids" value="${invoice.limit.id }" type="hidden"/>
										
			</c:forEach>
				<tr>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${sumTotalPrice}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${sumpayPrice}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${sumLimitPrice}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${sumCanLimitPrice}" /></td>
				<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${sumLimitCurPrice}" /><input name="totalLimitPrice" type="hidden" value="${sumLimitCurPrice}"  class="inputTxt"/></td>
				</tr>
<%--			<tr>--%>
<%--				<td colspan="2">小计</td>--%>
<%--			</tr>				--%>
		</tbody>
		</table>
		<ul class="team_co clearFix vote-ul">
			<li><label>开票方式：</label> ${invoiceModes[invoice.invoiceMode] } </li>
			<li><label>开票类型：</label> ${invoiceTypes[invoice.invoiceType] } </li>
			<li><label>开票金额：</label> ${invoice.invoiceAmount } </li>
			<li class="clear"></li>
			<c:choose>
				<c:when test="${audited eq '1'}">
					<li><label><font style="color:#ff0000; padding-right:5px;">*</font>发票号：</label><input type="text" name="invoiceNum" value="" maxlength="20" class="required"/></li>
				</c:when>
				<c:otherwise>
					<li><label>发票抬头：</label> ${invoice.invoiceHead} 
				</c:otherwise>
			</c:choose>
			<li><label>开票客户：</label> ${invoice.invoiceCustomer} </li>
			<li><label>开票项目：</label> ${invoiceSubjects[invoice.invoiceSubject]} </li>
			
		</ul>
		<div class="ydBtn" style="margin-top:50px;width: 230px;">
			<input type="button" value="返   回" onclick="history.go(-1)" class="btn btn-primary" id="btnCancel">&nbsp;&nbsp;
			<c:choose>
				<c:when test="${audited eq '1'}">
					<input id="createInvoice" class="btn btn-primary" type="button" value="开票" onClick="createInvoiceSubmit()" class="required"/>&nbsp;&nbsp;
				</c:when>
				<c:otherwise>
					<input id="btnDeny" class="btn btn-primary" type="button" value="驳回" onClick="denyinvoice(${invoice.id })"/>
					<input id="btnSubmit" class="btn btn-primary" type="submit" value="审批通过" onClick=""/>&nbsp;&nbsp;
				</c:otherwise>
			</c:choose>
		</div>
	</form:form>
	
	
</body>
</html>