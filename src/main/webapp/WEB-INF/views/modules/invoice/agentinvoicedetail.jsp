<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>渠道商申请发票详情</title>
<meta name="decorator" content="wholesaler"/>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
	    
	    function closeself(){
	    	//window.location.href = "${ctx}/invoice/limit/agentinvoicelist?verifyStatus=1";
	    	window.close();	    	
	    }
	</script>
</head>
<body>
    <page:applyDecorator name="show_head">
        <page:param name="desc">渠道商申请发票申请</page:param>
    </page:applyDecorator>
	<div class="sup_detail_top">
<ul class="team_co clearFix">
<li>
					
			<label>发票号：</label><input type="text" value="${orderinvoice.invoiceNum}" readonly="readonly"/></li>
			<li><label>开票日期：</label><input type="text" value="<fmt:formatDate pattern="yyyy-MM-dd" value="${orderinvoice.createDate}"/>" readonly="readonly"/></li>
			<li><label>开票类型：</label><input type="text" value="${invoiceTypes[orderinvoice.invoiceType] }" readonly="readonly"/></li>
			<li><label>开票方式：</label><input type="text" value="${invoiceModes[orderinvoice.invoiceMode] }" readonly="readonly"/></li>
			<li class="clear"></li>
			<li><label>开票客户：</label><input type="text" value="${orderinvoice.invoiceCustomer }" readonly="readonly"/></li>
			<li><label>开票项目：</label><input type="text" value="${invoiceSubjects[orderinvoice.invoiceSubject] }" readonly="readonly"/></li>
			<li><label>开票金额：</label><input type="text" value="${orderinvoice.invoiceAmount }" readonly="readonly"/></li></ul>
			
	</div>


	<div style="margin-top:8px;">
		 <table id="contentTable"  class="activitylist_bodyer_table" style="border-top:1px solid #dddddd">
      <thead >
			<tr>
				<th>团号</th>
				<th>出团日期</th>
				<th>订单号</th>
				<th>预订日期</th>
				<th>人数</th>
				<th>应收团款</th>
				<th>已收团款</th>
				<th>已开发票</th>
				<th>本次开票</th>
			</tr>
		</thead>
		<tbody>
				<tr>
					<td>${limit['groupCode']}</td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${limit['groupOpenDate']}"/></td>
					<td>${limit['orderNum']}</td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${limit['orderTime']}"/></td>
					<td class="tr">${limit['orderPersonNum']}</td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalPrices']}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalPrices']}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['usePrice']}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['invoiceAmount']}" /></td>									
				</tr>			
		</tbody>
		</table>

	
	
</div>	 

<div style="margin-top:8px;">
<table id="contentTable" class="table table-striped table-bordered table-condensed table_backfff">
		<thead>
			<tr>
				<th>付款日期</th>
				<th>付款方式</th>
				<th>付团款金额</th>
				<th>财务达账金额</th>
				<th>本次开票金额</th>
			</tr>
		</thead>
		<tbody>
			<c:set var="len" value="${fn:length(detail)}"></c:set>
			<c:set var="totalPrice" value="0"></c:set>
			<c:set var="invoicePrice" value="0"></c:set>
			<c:forEach items="${details}" var="detail" varStatus="s">
				<c:set var="totalPrice" value="${totalPrice+detail[6]}"></c:set>
				<c:set var="totalPrice2" value="${totalPrice2+detail[2]}"></c:set>
				<c:set var="invoicePrice" value="${invoicePrice+detail[4]}"></c:set>		
				<tr>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${detail[0]}"/></td>
					<td>${detail[1]}</td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${detail[6]}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${detail[2]}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${detail[4]}" /></td>			
				</tr>
			</c:forEach>
			<tr>
				<td colspan="2">小计</td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${totalPrice}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${totalPrice2}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${invoicePrice}" /></td>
			</tr>
		
		

				
		</tbody>
	</table>
	</div>
	
			
		<div class="release_next_add" style="margin-top:50px;">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="关闭" onClick="closeself()"/>
		</div>
</body>
</html>