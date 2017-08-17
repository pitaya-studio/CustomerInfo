<%@ page contentType="text/html;charset=UTF-8"%> 
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta  charset='utf-8' />
	<link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
	<title>退款单</title>
<!-- 	<meta name="decorator" content="wholesaler" /> -->
	<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
	<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script> 
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript">
	   function printdiv(printpage)
		{
			var headstr = "<html><head><title></title></head><body>";
			var footstr = "</body>";
			var newstr = document.all.item(printpage).innerHTML;
			var oldstr = document.body.innerHTML;
			document.body.innerHTML = headstr+newstr+footstr;
			window.print(); 
			document.body.innerHTML = oldstr;
			return false;
		};
		function downLoadPrintRefund(productType, agentId, orderId, reviewId, groupCode){
		
			$.ajax({
		        type: "POST",
		        url: "${ctx}/refundmentReview/updatePrint",
		        data: {
		        	reviewId : reviewId
		        },
		        success: function(msg){
		        	window.opener.$("#searchForm").submit();
					$("input[name='productType']").val(productType);
					$("input[name='agentId']").val(agentId);
					$("input[name='orderId']").val(orderId);
					$("input[name='reviewId']").val(reviewId);
					$("input[name='groupCodePrint']").val(groupCode);
					$("#refundRevPrintRefundDownLoadForm").submit();
		        }
		     });
		};
		function printTure(reviewId) {
			$.ajax({
		        type: "POST",
		        url: "${ctx}/refundmentReview/updatePrint",
		        data: {
		        	reviewId : reviewId
		        },
		        success: function(msg){
		        	window.opener.$("#searchForm").submit();
// 			            location.reload();
				printPage(document.getElementById("printDiv"));
		        }
		     });
		};
	</script>
	<style type="text/css">
		.dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
		.ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
        .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
        .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
	</style>
</head>
<body>
		<div id="printDiv">
			<table class="dayinzy">
				<thead>
					<tr>
						<th class="fr f4 paddr" colspan="8">首次打印日期：${refundInfo.printTime}</th>
					</tr>
					<tr>
						<th class="f1" colspan="8">退 款 单</th>
					</tr>
					<tr>
						<th class="fl paddl f3" colspan="4">填写日期：${refundInfo.applyDate }</th>
						<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				</table>
			<table  class="dayinzy  dayInIe">
				<tbody>
					<tr>
						<td class="f2 fr" width="100">团号：</td>
						<td class="f3" width="100" colspan="2"><div style="float:left;width:128px;">&nbsp;${refundInfo.groupCodePrint}</div> <div style="width:40px;border-left:1px solid #000;height:30px;float:left;">人数</div></td>
<!-- 						<td class="f2 fr" width="80">人数：</td> -->
						<td class="f3">&nbsp;${refundInfo.personNum}</td>
						<td class="f2 fr" width="100">出发日期：</td>
						<td class="f3" width="80">&nbsp;${refundInfo.startDate}</td>
						<td class="f2 fr" width="80">返回日期：</td>
						<td class="f3" width="70">&nbsp;${refundInfo.endDate}</td>
					</tr>
					<tr>
						<td class="f2 fr">产品线路：</td>
						<td class="f3 fl" colspan="7">&nbsp;${refundInfo.productName}</td>
					</tr>
					<tr>
						<td class="f2 fr">应收金额：</td>
						<td class="f3 fr">&nbsp;${refundInfo.totalMoney }</td>
						<td class="f2 fr">已收金额：</td>
						<td class="f3 fr">&nbsp;${refundInfo.payedMoney }</td>
						<td class="f2 fr">退款金额：</td>
						<td class="f3 fr" colspan="3">&nbsp;${refundInfo.refundPrice}</td>
					</tr>
					<tr>
						<td class="f2 fr">退款说明：</td>
						<td class="f3 fl" colspan="3">&nbsp;${refundInfo.remarks }</td>
						<td class="fr f2">退款金额(大写)：</td>
						<td class="f3 fl" colspan="3">&nbsp;${refundInfo.upcase}</td>
					</tr>
					<tr>
						<td class="f2 fr">客户：</td>
						<td class="f3 fl" colspan="3">&nbsp;${refundInfo.keHu }</td>
						<td class="fr f2">电话：</td>
						<td class="f3 fl" colspan="3">&nbsp;${refundInfo.tel }</td>
					</tr>
					<tr>
						<td class="f2 fr">收款人全称：</td>
						<td class="f3 fl" colspan="3">&nbsp;${refundInfo.shouKuanRen}</td>
						<td class="fr f2">开户行/账号：</td>
						<td class="f3 fl" colspan="3">&nbsp;${refundInfo.bankInfo}</td>
					</tr>
					<tr>
						<td class="f2 fr">经办人：</td>
						<td class="f3 fl">&nbsp;${refundInfo.applyPerson }</td>
						<td class="f2 fr">财务：</td>
						<td class="f3 fl">&nbsp;${refundInfo.cwzg}</td>
						<td class="f2 fr">审批：</td>
						<td class="f3 fl">&nbsp;${refundInfo.bmjl}</td>
						<td class="f2 fr">总经理：</td>
						<td class="f3 fl">&nbsp;${refundInfo.sp}</td>
					</tr>
				</tbody>
			</table>
			<table  class="dayinzy  TopHeight">
					<tr>
						<td class="fr f3 noborder paddr" colspan="8">确认付款日期：${refundInfo.spDate }</td>
					</tr>
			</table>
		</div> 
		<!--S打印&下载按钮-->
		<div class="dbaniu">
			<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure(${refundInfo.reviewId});">
			<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downLoadPrintRefund('${refundInfo.productType}', '${refundInfo.agentId}', '${refundInfo.orderId}', '${refundInfo.reviewId}', '${refundInfo.groupCodePrint}');">
		</div>
	<form id="refundRevPrintRefundDownLoadForm" action="${ctx}/refundmentReview/downloadList" method = "post">
		<input type="hidden" value="" name="productType"/>
		<input type="hidden" value="" name="agentId"/>
		<input type="hidden" value="" name="orderId"/>
		<input type="hidden" value="" name="reviewId"/>
		<input type="hidden" value="" name="groupCodePrint"/>
		<input type="hidden" value="order" name="option">
	</form>
</body>

</html>