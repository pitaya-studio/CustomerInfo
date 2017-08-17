<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>渠道商发票申请</title>
<meta name="decorator" content="wholesaler"/>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {});
		
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/invoice/limit/agentlist");
			$("#searchForm").submit();
	    }
	    
	    function makinvoice(groupid,orderid,invoiceid){
	    	window.location.href = "${ctx}/invoice/limit/agentinvoice/"+groupid+"/"+orderid+"?invoiceid="+invoiceid;
	    	
	    }
	</script>
</head>
<body>
<page:applyDecorator name="agent_invoice_op_head" >
    <page:param name="current">agentInvoiceApply</page:param>
</page:applyDecorator>
	<form:form id="searchForm" action="${ctx}/invoice/limit/agentlist" method="post" class="form-search">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<input id="agnetid" name="agentid" type="hidden" value="<c:if test="${not empty param.agentid}">${param.agentid}</c:if>"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2">
				<label>出团日期：</label><input id="groupOpenDateBegin" name="groupOpenDateBegin"  class="inputTxt dateinput"value="${groupOpenDateBegin }" readonly onClick="WdatePicker()"/> 至 
				<input id="groupOpenDateEnd" name="groupOpenDateEnd" value="${ groupOpenDateEnd}" class="inputTxt dateinput" readonly onClick="WdatePicker()"/>
			</div>
			&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onClick=""/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	 <table id="contentTable" class="activitylist_bodyer_table">
            <thead>
			<tr>
				<th>订单号</th>
				<th>团号</th>
				<th>预订日期</th>
				<th>人数</th>
				<th>出团日期</th>
				<th>应收团款</th>
				<th>已收团款</th>
				<th>财务到账</th>
				<th>已开发票</th>
				<th>待审核金额</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		
			<c:forEach items="${page.list}" var="limit" varStatus="s">
				<c:set var="s1" value="${limit['orderId']}"></c:set>
<%--				<fmt:formatNumber var="s" value="${limit[9]}" pattern="0"></fmt:formatNumber>--%>
				
				<tr>
					<td>${limit['orderNum']}</td>
					<td>${limit['groupCode']}</td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${limit['orderTime']}"/></td>
					<td class="tr"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['orderPersonNum']}" /></td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${limit['groupOpenDate']}"/></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalPrices']}" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalPrices']}" /></td>
					<td class="tr">¥<c:if test="${empty limit['accountedMoney']}">0</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['accountedMoney'] }" /></td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['usePrice']}" /></td>
					<td class="tr">
						<c:choose>
							<c:when test="${not empty apply[s1] and createStatus[s1] ne 1 }">
								¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['invoiceAmount']}" />
							</c:when>
							<c:otherwise>
								¥0
							</c:otherwise>
						
						</c:choose>
					</td>
					
						<c:choose>
							<c:when test="${limit['accountedMoney'] > 0}">
								<c:choose>
									<c:when test="${limit['accountedMoney'] <= limit['usePrice']}">
										<td class="invoice_back">发票已开完</td>
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${apply[s1] == 0}">
												<td class="invoice_no">发票未审核</td>
											</c:when>
											<c:when test="${apply[s1] == 1}">
												<c:if test="${createStatus[s1] == 0}">
													<td class="invoice_yes">审核已通过，待开票</td>
												</c:if>
												<c:if test="${createStatus[s1] != 0 }">
													<td><a href="javascript:void(0)" onClick="makinvoice(${limit['gruopId']},${limit['orderId']},'${limit['invoiceid']}')">申请发票</a></td>
												</c:if>
											</c:when>
											<c:when test="${apply[s1] == 2}">
												<td class="invoice_back">发票审核被驳回，<a href="javascript:void(0)" onClick="makinvoice(${limit['gruopId']},${limit['orderId']},'${limit['invoiceid']}')">再次申请</a></td>
											</c:when>
											<c:otherwise>
												<td><a href="javascript:void(0)" onClick="makinvoice(${limit['gruopId']},${limit['orderId']},'${limit['invoiceid']}')">申请发票</a></td>
											</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>	
							</c:when>
							<c:otherwise>
								<td class="invoice_back">暂无到账金额</td>
							</c:otherwise>
						</c:choose>
										
				</tr>
			</c:forEach>
		
		

				
		</tbody>
	</table>
	<div class="pagination">${page}</div>
   <div class="page">
   	
   </div> 
</body>

</html>