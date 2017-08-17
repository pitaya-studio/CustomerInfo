<%@ page contentType="text/html;charset=UTF-8"%> 
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>请款单</title>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css"/>
<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script> 
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript">
	function printTure(payId, prdType,reviewId) {
		$.ajax({
	        type: "POST",
	        url: "${ctx}/cost/manager/updatePrint4fkd",
	        data: {
	        	payId : payId,
	        	prdType : prdType,
	        	reviewId : reviewId
	        },
	        success: function(msg){
	        	if(msg == 'success'){
					printPage(document.getElementById("printDiv"));
					window.location.reload();
	        	} else {
	        		top.$.jBox.tip(msg);
	        	}
	        }
	     });
	}
</script>
<style type="text/css">
	.dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
	.ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
    .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
    .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
</style>
</head>
<body>
	<div class="finanece_table_application_out" id="printDiv">
		<table class="finanece_table_application">
			<tr>
				<td width="25%">TO：财务</td>
				<td width="49%">&nbsp;</td>
				<td width="9%">财务编号：</td>
				<td width="17%">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="4" class="tc finance_table_16px">请款单</td>
			</tr>
			<tr>
				<td colspan="2">项目：退款 </td>
				<td class="tr">日期：</td>
				<td>${createDate }</td>
			</tr>
		</table>
		<table
			class="finanece_table_application finanece_table_application_border">
			<tr>
				<td width="18%" class="tc">团编号</td>
				<td width="15%">${orderNum }</td>
				<td width="17%">团号及线路</td>
				<td width="18%">${productName }</td>
				<td width="7%">组团社</td>
				<td width="25%">${agentName }</td>
			</tr>
			<tr>
				<td class="tc">退款简要明细</td>
				<td colspan="5">${remarks }</td>
			</tr>
			<tr>
				<td class="tc">退款总额</td>
				<td colspan="5">${refundPrice }</td>
			</tr>
			<tr>
				<td colspan="6"><p>户名：${payerName }</p>
					<p>开户行：${bankName }</p>
					<p>账号：${bankAccount }</p></td>
			</tr>
			<tr>
				<td colspan="3"><p>请款人签字：</p>
					<p>提示：提示和同行的确认 盖章+签字</p></td>
				<td colspan="3"><p>操作签字：</p>
					<p>提示：请录龙之旅</p></td>
			</tr>
			<tr>
				<td colspan="3">操作部主管签字：</td>
				<td colspan="3">经理签字：</td>
			</tr>
			<tr>
				<td colspan="3">部门财务签字：</td>
				<td colspan="3">财务主管签字：</td>
			</tr>
			<tr>
				<td colspan="3">出纳签字：</td>
				<td colspan="3"></td>
			</tr>
			<tr>
				<td colspan="6"><p>备注：</p>
					<p></p></td>
			</tr>
		</table>
		<table class="finanece_table_application">
			<tr>
				<td class="tdred">请保证以上项目全部填写完整后 财务方可汇款！</td>
			</tr>
		</table>
	</div>
	<!--S打印&下载按钮-->
	<div class="dbaniu">
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onClick="printTure('${payId }','${orderType }','${reviewId }');">
		<input id="" name="" type="button" class="ydbz_s" value="下载" onClick="window.location.href='${ctx}/refundReview/downLoadRefundTable?reviewId=${reviewId }'">
		<input type="button" value="关闭" onclick="javaScript:window.close();" class="ydbz_s">
	</div>
</body>

</html>