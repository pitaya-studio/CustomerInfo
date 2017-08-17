<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发票详情</title>
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
		<page:param name="desc"><c:if test="${verifyStatus eq '-2'}">发票详情</c:if>
</page:param>
	</page:applyDecorator>
	<form:form id="searchForm" action="${ctx}/invoice/limit/verifyinvoice/${details[0].invoiceNum}" method="post" class="breadcrumb form-search">
	<input id="createInvoiceHid" name="createInvoiceHid" value="" type="hidden"/>
	<div style="margin-top: 8px;">
		<table id="contentTable" class="activitylist_bodyer_table"
			style="border-top: 1px solid #dddddd">
			<thead>
				<tr>
					<th>序号</th>
					<th>订单号</th>
					<th>团号</th>
					<c:if test="${verifyStatus eq '0'}">
					<th>销售</th>
					</c:if>
					<th>下单时间</th>
					<th>人数</th>
					<th>出/截团日期</th>
					<th>应收金额</th>
					<th>财务到账</th>
					<th>已开票金额</th>
					<th>本次开票金额</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${limits}" var="lim" varStatus="s">
					<tr>
						<td>${s.count}</td>
						<td class="tc">${lim[0]}</td>
						<td class="tc">${lim[1]}</td>
						<c:if test="${verifyStatus eq '0'}">
						<td class="tc">${lim[3]}</td>
						</c:if>
						<c:set var="supplyBy" value="${lim[3]}" />
						<td class="tc">${lim[4]}</td>
						<td class="tc">${lim[5]}</td>
						<td class="tc" style="padding: 0px;">
							<c:if test="${lim[12] ne '6' && lim[12] ne '7'}">
							<div class="out-date">
							<c:if test="${not empty lim[6] }">
								<fmt:formatDate pattern="yyyy-MM-dd" value="${lim[6]}" /></c:if>
							</div>
							<div class="close-date">
							<c:if test="${not empty lim[7] }">
								<fmt:formatDate pattern="yyyy-MM-dd" value="${lim[7]}" /></c:if>
							</div>
						</c:if>
						</td>
						<td class="tr"><c:if test="${not empty lim[8]}"></c:if>${fns:getMoneyAmountBySerialNum(lim[8],1)}</td>
						<td class="tr"><c:if test="${not empty lim[13]}"></c:if>${fns:getMoneyAmountBySerialNum(lim[13],1)}</td>
						<td class="tr"><c:if test="${not empty lim[10]}">¥</c:if> <fmt:formatNumber
								type="currency" pattern="#,##0.00" value="${lim[10]}" /></td>
								<td class="tr"><c:if test="${not empty lim[11]}">¥</c:if>${lim[11]}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="sup_detail_top">

		<ul class="team_co clearFix vote-ul">
		<c:if test="${details[0].createStatus==1}">
		<li><label>发票号：</label>
				${details[0].invoiceNum}</li>
		<li><label>开票时间：</label>
				<fmt:formatDate pattern="yyyy-MM-dd" value="${details[0].updateDate}" /></li>
		</c:if>
			<li><label>开票类型：</label>
				${invoiceTypes[details[0].invoiceType]}</li>
			<li><label>开票方式：</label>
				${invoiceModes[details[0].invoiceMode] }</li>
			<li><label>开票项目：</label>
				${invoiceSubjects[details[0].invoiceSubject] }</li>
<!-- 			<li class="clear"></li> -->
			<li><label>发票抬头：</label> ${details[0].invoiceHead}</li>
			<li><label>开票客户：</label> ${details[0].invoiceCustomer }</li>
			<li><label>申请人：</label> ${supplyBy}</li>
			<li><label>开票金额：</label>￥${invoiceMoney} </li>
			<li><label>开票原因：</label> ${details[0].remarks }</li>
			<li class="clear"></li>
			<c:if test="${verifyStatus eq '-1'}">
			<li><label><font style="color:#ff0000; padding-right:5px;">*</font>发票号：</label><input type="text" name="invoiceNum" value="" maxlength="20" class="required"/></li>
			</c:if>
		</ul>
	</div>
	<br />
		<div class="ydBtn">
		<a class="ydbz_x" href="javascript:void(0)"
			onClick="javascript:window.close();">关闭</a>
		</div>
	
	<div style="clear: both"></div>
	</form:form>
</body>
</html>