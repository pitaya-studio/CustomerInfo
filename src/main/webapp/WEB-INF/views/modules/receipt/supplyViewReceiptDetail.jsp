<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>财务-收据管理-已审核收据-明细</title>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<link rel="stylesheet" href="css/jbox.css" />
<link type="text/css" rel="stylesheet"href="${ctxStatic}/css/jquery.validate.min.css" />
<%@include file="/WEB-INF/views/include/dialog.jsp"%>
</head>
<body>
		<!--右侧内容部分开始-->
		<table class="activitylist_bodyer_table" id="contentTable">
			<thead>
				<tr>
					<th width="6%">序号</th>
					<th width="10%">订单号</th>
					<c:if test="${isHQX }">
						<th width="10%">订单团号</th>
					</c:if>
					<c:if test="${!isHQX }">
						<th width="10%">团号</th>
					</c:if>
					<th width="6%">销售</th>
					<th width="10%">下单时间</th>
					<th width="8%">人数</th>
<!-- 					<th width="5%">出/截团日期</th> -->
					<th width="10%">应收金额</th>
					<th width="10%">财务到账</th>
					<th width="10%">已开收据金额</th>
					<th width="10%">本次开收据金额</th>
				</tr>
			</thead>
			<tbody>
				<%
				int n = 1;
				%>
		          <c:forEach items="${limits}" var="receiptInfo" varStatus="s">
		            <tr>
		            <td class="tc"><%=n++%></td>
					<td class="tc">${receiptInfo[0]}</td>
					<td class="tc">${receiptInfo[1]}</td>
					<td class="tc">${receiptInfo[3]}</td>
					<td class="tc">${receiptInfo[4]}</td>
					<td class="tc">${receiptInfo[5]}</td>
 					<%-- 
 					<td style="padding: 0px;" class="tc"><div class="out-date"> 
 							<c:if test="${not empty receiptInfo[6] }">
 								<fmt:formatDate pattern="yyyy-MM-dd" value="${receiptInfo[6]}" /></c:if></div>
 						<div class="close-date"><c:if test="${not empty receiptInfo[7] }">
 								<fmt:formatDate pattern="yyyy-MM-dd" value="${receiptInfo[7]}" /></c:if></div>
					</td> --%>
					<td class="tr">${fns:getMoneyAmountBySerialNum(receiptInfo[8],1)}</td>
					<td class="tr">${fns:getMoneyAmountBySerialNum(receiptInfo[13],1)}</td>
					<td class="tr">人民币<fmt:formatNumber
								type="currency" pattern="#,##0.00" value="${receiptInfo[10] == null || receiptInfo[10] == '' ? 0.00 : receiptInfo[10]}" /></td>
					<td class="tr">人民币<fmt:formatNumber
								type="currency" pattern="#,##0.00" value="${receiptInfo[11] == null || receiptInfo[11] == '' ? 0.00 : receiptInfo[11]}" /></td>
				</tr>
				</c:forEach>
			</tbody>
		</table>

		<div class="sup_detail_top">
			<ul class="team_co clearFix vote-ul">
				<li style="width:100%;"><label>收据抬头：</label>${details[0].invoiceHead }</li>
				<li><label>收据号：</label><c:if test="${details[0].createStatus eq  '1' || details[0].createStatus eq  '2'}">${details[0].invoiceNum}</c:if></li>
				<li><label>开收据类型：</label>${invoiceTypes[details[0].invoiceType]}</li>
				<li><label>开收据方式：</label>${invoiceModes[details[0].invoiceMode] }</li>
				<li><label>开收据项目：</label>${invoiceSubjects[details[0].invoiceSubject] }</li>
				<li style="text-overflow: ellipsis;"><label>开收据客户：</label>${details[0].invoiceCustomer }</li>
				<li><label>开收据金额：</label>￥${invoiceMoney }</li>
				<li><label>开收据日期：</label><fmt:formatDate pattern="yyyy-MM-dd" value="${details[0].updateDate}" /></li>
				<li class="clear"></li>
			</ul>
			<table>
				<tbody>
				    <tr>
				        <td width="100" class="tr vertical-top">开收据原因：</td>
				        <td class="tl">
				            <span class="ellipsis-remarks" >${details[0].remarks }</span>
				        </td>
				    </tr>
				</tbody>
			</table>
		</div>
		<div class=" ydbz_button">
		    <a class="ydbz_x  gray" href="javascript:void(0)" onclick="javascript:window.close();">关闭</a>
		</div>
		<!--右侧内容部分结束-->
</body>
</html>
