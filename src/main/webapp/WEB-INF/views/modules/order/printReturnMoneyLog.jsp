<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<link href="${ctxStatic}/css/dayinbdzy.css" rel="stylesheet" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js"
 type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>

		<title>签证费还款单</title>
</head>
<body>
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab" value="打印" onclick="printTure();">
		<a href="${ctx}/visaOrderPayLog/manage/returnMoney?travleId=${travleId}&serialNum=${serialNum}">下载</a>
		<div id="printDiv">
			<table class="dayinzy">
				<thead>
					<tr>
						<th class="fr f4 paddr" colspan="8">首次打印日期：${printTime}</th>
					</tr>
					<tr>
						<th class="f1" colspan="8">签证费还款单</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期：<fmt:formatDate value="${list.fillDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " /></th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="f2">还款单位</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">签证部</div>
						</td>
						<td class="f2">经办人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320">${list.returnPerson}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">还款说明</td>
						<td class="f3" colspan="7">
							<div class="dayinzy_w698"> </div>
						</td>
					</tr>
					<tr>
						<td class="f2">还款金额</td>
						<td class="f3" colspan="4">
							<div class="dayinzy_w373">人民币 ${fns:changeAmount(list.borrowAmount)}</div>
						</td>
						<td class="f2">￥</td>
						<td class="f3" colspan="2">
							<div class="dayinzy_w193">${list.borrowAmount}</div>
						</td>
					</tr>
					<tr>
						<td class="f2">还款人</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w300">${list.returnPerson}</div>
						</td>
						<td class="f2">总经理</td>
						<td class="f3" colspan="3">
							<div class="dayinzy_w320"></div>
						</td>
					</tr>
					<tr>
						<td class="f2" width="70">财务主管</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130"></div>
						</td>
						<td class="f2" width="40">出纳</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130"></div>
						</td>
						<td class="f2" width="70">财务</td>
						<td class="f3" width="110">
							<div class="dayinzy_w130"></div>
						</td>
						<td class="f2" width="40">审批</td>
						<td class="f3">
							<div class="dayinzy_w152"></div>
						</td>
					</tr>
					<tr>
						<td class="fr f3 noborder paddr" colspan="8">确认付款日期：</td>
					</tr>
				</tbody>
			</table>
		</div>
		<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="js/common.js"></script>
		<script type="text/javascript">
			function printTure() {
				printPage(document.getElementById("printDiv"));
			}
		</script>
	</body>

</html>