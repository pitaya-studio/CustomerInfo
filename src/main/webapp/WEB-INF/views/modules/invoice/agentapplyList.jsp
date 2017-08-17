<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>渠道商发票审核记录查询</title>
<meta name="decorator" content="wholesaler"/>
<%@include file="/WEB-INF/views/include/dialog.jsp" %>
<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
		});

		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/invoice/limit/agentinvoicelist?verifyStatus="+$("#verifyStatus").val());
			$("#searchForm").submit();
	    }
	    
	    function viewdetail(invoiceid,orderid){
	    	window.open("${ctx}/invoice/limit/agentviewdetail/"+invoiceid+"/"+orderid);
	    	
	    }
	</script>
</head>
<body>
<page:applyDecorator name="agent_invoice_op_head" >
    <page:param name="current">agentInvoiceList</page:param>
</page:applyDecorator>
	<form:form id="searchForm" action="${ctx}/invoice/limit/agentinvoicelist" method="post" class="form-search">
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2">
				<label>出团日期：</label><input id="groupOpenDateBegin" name="groupOpenDateBegin"  class="inputTxt dateinput"value="${groupOpenDateBegin }" readonly onClick="WdatePicker()"/> 至 
				<input id="groupOpenDateEnd" name="groupOpenDateEnd" value="${groupOpenDateEnd }"  class="inputTxt dateinput" readonly onClick="WdatePicker()"/>&nbsp;
			</div>
				<label>发票状态：</label><select id="verifyStatus" name="verifyStatus">
					<option value="" <c:if test="${empty verifyStatus}">selected="selected"</c:if>>全部</option>
					<option value="0" <c:if test="${verifyStatus==0}">selected="selected"</c:if>>未审核</option>
					<option value="1" <c:if test="${verifyStatus==1}">selected="selected"</c:if>>审核通过</option>
					<option value="2" <c:if test="${verifyStatus==2}">selected="selected"</c:if>>被驳回</option>
				</select>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onClick=""/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	 <table id="contentTable" class="activitylist_bodyer_table">
            <thead>
			<tr>
				<th>发票号</th>
				<th>订单号</th>
				<th>团号</th>
				<th>出团日期</th>
				<th>使用状态</th>
				<th>申请日期</th>
				<th>开票方式</th>
				<th>开票类型</th>
				<th>发票状态</th>
				<th>开票客户</th>
				<th>开票项目</th>
				<th>开票金额</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
<%--		${invoiceTypes[orderinvoice['invoiceType']]}--%>
			<c:forEach items="${page.list}" var="orderinvoice" varStatus="s">	
				<tr>
					<td>${orderinvoice['invoiceNum']}</td>
					<td>${orderinvoice['orderNum']}</td>
					<td>${orderinvoice['groupCode']}</td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${orderinvoice['groupOpenDate']}"/></td>
					<td></td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${orderinvoice['createDate']}"/></td>
					<td>${invoiceModes[fn:trim(orderinvoice['invoiceMode'])]}</td>
					<td>${invoiceTypes[fn:trim(orderinvoice['invoiceType'])]}</td>
					<td>
					<c:if test="${orderinvoice['verifyStatus']==0}">
						未审核
					</c:if>
					<c:if test="${orderinvoice['verifyStatus']==1}">
						审核通过
					</c:if>
					<c:if test="${orderinvoice['verifyStatus']==2}">
						被驳回
					</c:if>
					
					</td>
					<td>${orderinvoice['invoiceCustomer']}</td>
					<td>${invoiceSubjects[fn:trim(orderinvoice['invoiceSubject'])]}</td>
					<td class="tr">¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${orderinvoice['invoiceAmount']}" /></td>
					<td>
						<c:if test="${orderinvoice['verifyStatus']==1}">
							<a href="javascript:void(0)" onClick="viewdetail(${orderinvoice['id']},${orderinvoice['orderId']})">明细</a>
						</c:if>						
					</td>
										
				</tr>
			</c:forEach>			
		</tbody>
	</table>
	<div class="pagination">${page}</div>
   <div class="page">
   	
   </div> 	 
</body>
</html>