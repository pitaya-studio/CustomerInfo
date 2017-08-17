<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>支付记录</title>

<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css"
	rel="stylesheet" />
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<style type="text/css">
	li {
		margin-left: 6px;
	}
	ul{
		margin:0px;
	}
	.table_new_down{
		table-layout: fixed;
		width:450px;
	}
	.table_new_down td{
		word-break: break-all;
		margin:0px;
		padding:4px 0;

	}
	.tr{
		text-align: right;
	}
	.width_110{
		width:110px;
	}
	.vertical_align_t{
		vertical-align: top;
	}
</style>

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>	
<script type="text/javascript">
function downloads(docid){
	window.open("${ctx}/sys/docinfo/download/"+docid);
}
</script>
</head>
<body>
	<input type="hidden"  name="moneyType"  id="moneyType"  value="${moneyType}"/>
	<input type="hidden"  name="recordId"  id="recordId"  value="${recordId}"/>
	<input type="hidden"  name="payId"  id="payId"  value="${payId}"/>
	<div id="jboxDiv" style="display:block;overflow:hidden;">
		<div class="jbox-getPay jbox-getPay-txt">
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
                        <td class="tl">${refundDispStyle}</td>
                    </tr>
                    <c:forEach items="${payFees }" var="payFee">
                    <tr>
                        <td class="tc">${payFee.feeName }</td>
                        <td class="tl"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${payFee.feeCurrencyId }"/><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${payFee.feeAmount }" /></span></td>
                    </tr>
                    </c:forEach>
                    <tr>
                        <td class="tc">合计付款金额</td>
                        <!-- Bug:12566 如果合计付款金额为空则显示付款金额  update by shijun.liu-->
                        <td class="tl"><c:if test="${empty moneyDispStyle and empty payFees }">${refundDispStyle}</c:if>
                        			  <c:if test="${not empty moneyDispStyle }">${moneyDispStyle}</c:if></td>
                    </tr>
                </tbody>
            </table>
			<table class="table_new_down">
			<tbody>
			<tr>
				<td class="tr width_110 vertical_align_t">付款类型：</td><td >${payTypeName}</td>
			</tr>
			<tr>
				<td class="tr width_110 vertical_align_t">收款单位：</td><td >${payerName}</td>
			</tr>
			<c:if test="${payType == 1}"><%-- 支票 --%>
				
				<tr>
					<td class="tr width_110 vertical_align_t">支票号：</td><td>${checkNumber}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">开票日期：</td><td><fmt:formatDate
							value="${invoiceDate}" pattern="yyyy-MM-dd" /></td>
				</tr>
			</c:if>
			<c:if test="${payType == 4}"><%-- 汇款 --%>
				<tr>
					<td class="tr width_110 vertical_align_t">来款行名称：</td><td>${bankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">来款账户：</td><td>${bankAccount}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款行名称：</td><td>${tobankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款账户：</td><td>${tobankAccount}</td>
				</tr>
			</c:if>
			<c:if test="${payType == 6 }"><!-- 银行转帐 -->
				<tr>
					<td class="tr width_110 vertical_align_t">收款行名称：</td><td>${tobankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款账户：</td><td>${tobankAccount}</td>
				</tr>
			    <tr>
					<td class="tr width_110 vertical_align_t">汇款行名称：</td><td>${bankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">汇款账户：</td><td>${bankAccount}</td>
				</tr>
			</c:if>
			<c:if test="${payType == 7 }"><!-- 汇票 -->
			    <tr>
					<td class="tr width_110 vertical_align_t">收款行名称：</td><td>${tobankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款账户：</td><td>${tobankAccount}</td>
				</tr>
			    <tr>
					<td class="tr width_110 vertical_align_t">出票人名称：</td><td>${drawerName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">出票人账号：</td><td>${drawerAccount}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">付款行全称：</td><td>${payBankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">汇票到期日：</td><td>${draftAccountedDate}</td>
				</tr>
			</c:if>
			<c:if test="${payType == 8 }"><!-- POS机刷卡 -->
			    <tr>
					<td class="tr width_110 vertical_align_t">收款行名称：</td><td>${tobankName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款账户：</td><td>${tobankAccount}</td>
				</tr>
			</c:if>
			<c:if test="${payType == 9 }"><!-- 支付宝 -->
				<tr>
					<td class="tr width_110 vertical_align_t">来款支付宝名称：</td><td>${fromAlipayName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">来款支付宝账户：</td><td>${fromAlipayAccount}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款支付宝名称：</td><td>${toAlipayName}</td>
				</tr>
				<tr>
					<td class="tr width_110 vertical_align_t">收款支付宝账户：</td><td>${toAlipayAccount}</td>
				</tr>
			</c:if>
			<tr>
				<td class="tr width_110 vertical_align_t">支付凭证：</td>
				<td class="batch-ol">
					<ul>
					<c:forEach items="${docInfoList}" var="docInfo">
						<li><span>${docInfo.docName}</span><a class="batchDl" style='margin-left:10px;' href='javascript:void(0)' onClick='downloads(${docInfo.id})'>下载</a></li>
					</c:forEach>
					</ul>
				</td>
			</tr>
			<tr>
				<td class="tr width_110 vertical_align_t">备注信息：</td><td>${remarks}</td>
			</tr>
			</tbody>
			</table>
		</div>
	</div>
</body>
</html>