<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>客人报名收款单</title>

<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.10.2.js"></script> 
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script> 
<script type="text/javascript">
Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}

// 打印
function printTure(ctx) {

	var $payids = document.getElementsByName("payid");
	var $ordertypes = document.getElementsByName("ordertype");
	
	var payids = new Array();
	var ordertypes = new Array();
	
	for (var i = 0; i < $payids.length; i++) {
		payids.push($payids[i].value);
		ordertypes.push($ordertypes[i].value);
	}

 	$.ajax({
    	type:"GET",
        url:ctx+"/orderCommon/manage/updateBatchPrint",
        dataType:"json",
        async:false,
        data:{
        	payids:payids.join(','),
        	ordertypes:ordertypes.join(',')
        },
        success:function(data){
        	if(data.flag == 'success'){
            	updatePrinted(data.msg);
        	}
        }
   });
   printPage(document.getElementById("printDiv"));
   window.opener.$("#searchForm").submit();  //刷新父页面 
   window.close();
}
	
function updatePrinted(msg){
	var doc = window.opener.document;
	
	for (var i = 0; i < msg.length; i++) {
		var payId = msg[i].payId;
		var orderType = msg[i].orderType;
		var date = msg[i].date;
		
		if (date != null && date != '') {
			var idval = 'print_' + payId + '_' + orderType;
			var $td = $(doc.getElementById(idval));
			var $value = $td.text().trim();
			if ($value.indexOf('已打印') == -1) {
				var v = "<p class='uiPrint'>已打印<span style='display: none;'>首次打印时间<br>" + date + "</span></p>";
				$td.html(v);
			}
		}
	}
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
	<div id="printDiv">
		<c:forEach items="${list }" var="print">
			<table class="dayinzy">
				<thead></thead>
				<thead>
					<tr>
                   		<th class="fc f2" width="110"></th>
                    	<th class="fc f3" width="280"></th>
                    	<th class="fc f2" width="110"></th>
                    	<th class="fc f3" width="70"></th>
                    	<th class="fc f2" width="60"></th>
                    	<th class="fc f3" width="70"></th>
                	</tr>
					<tr>
						<th class="fr f4 paddr" colspan="6">首次打印日期：
							<fmt:formatDate value="${print.firstPrintTime }" type="both" dateStyle="default" pattern="yyyy/MM/dd HH:mm"/>
						</th>
					</tr>
					<tr>
						<th class="f1" colspan="6">客人报名收款单</th>
					</tr>
					<tr>
						<th class="fl paddl f3 bot_border" colspan="3">填写日期：
							<c:if test="${empty print.pay.createDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if>
							<c:if test="${not empty print.pay.createDate }">
								<fmt:formatDate value="${print.pay.createDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " />
							</c:if>
						</th>
						<th class="fr paddr f3 bot_border" colspan="3">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="fc f2" width="110">团号</td>
						<td class="fc f3" style="white-space: normal;word-break: break-word">
							${print.groupCode}
						</td>
						<td class="fc f2" width="110">出发/签证日期</td>
						<td class="fc f3" width="70">
							<fmt:formatDate value="${print.groupOpenDate}" type="both" dateStyle="long" pattern="MM 月  dd 日 " />
						</td>
						<td class="fc f2" width="60">经办人</td>
						<td class="fc f3" width="70">
							${print.pay.name}
						</td>
					</tr>
					<tr>
						<td class="fc f2">线路/产品</td>
						<td class="fl f3 paddl" colspan="5">
							${print.groupName}
						</td>
					</tr>
					<tr>
						<td class="fc f2">来款单位信息</td>
						<td class="fc f3" colspan="3">
							<div class="danhangnormal">${print.pay.payerName }&nbsp;&nbsp;</div>
						</td>
						<td class="fc f2">款项</td>
						<td class="fc f3">
							<c:if test="${print.orderType < 6 || print.orderType == 10 }">团款</c:if>
							<c:if test="${print.orderType == 7 }">机票款</c:if>
							<c:if test="${print.orderType > 10 }">团款</c:if>
							${print.item }
						</td>
					</tr>
					<tr>
						<td class="fc f2">客人名单</td>
						<td class="fl f3 paddl" colspan="5">
							<div class="danhangnormal">${print.traveler}</div>
						</td>
					</tr>
					<tr>
						<td class="fc f2">备注</td>
						<td class="fl f3 paddl" colspan="5">
							${print.pay.remarks}
						</td>
					</tr>
					<c:if test = "${isHQX eq 'true'}">
						<tr>
		                    <td class="fc f2">开票状态</td>
		                    <td class="fl f3 paddl" >${print.invoiceStatus}</td>
		                    <td class="fc f2">开票金额</td>
		                    <td class="fl f3" colspan="3" style="text-align:right;">${print.invoiceMoney}</td>
		                </tr>
	                </c:if>
					<tr>
						<td class="fc f2">收款金额</td>
						<td class="fl f3" style="text-align:right;">
							${print.payedMoney}
						</td>
						<td class="fc f2">大写</td>
						<td class="fl f3" colspan="3">
							${print.capitalMoney}
						</td>
					</tr>
					<tr>
						<td class="fc f2">收款账号</td>
						<td class="fl f3" style="overflow:hidden;white-space:normal !important; table-layout:fixed;max-height:120px;" >
							${print.pay.toBankNname}${print.pay.toBankAccount}${print.pay.toAlipayName }${print.pay.toAlipayAccount }
						</td>
						<td class="fc f2">交款人</td>
						<td class="fc f3">
							${print.pay.name}
						</td>
						<td class="fc f2">收款人</td>
						<td class="fc f3">
							<c:if test="${print.pay.isAsAccount == 1}">${print.pay.createName}</c:if>
						</td>
					</tr>
					<tr>
						<td class="fl f3 noborder paddl" colspan="3">银行到账日期：
						<c:choose>
							<c:when test="${fn:contains(companyName,'环球行')}">
								<c:if test="${empty print.pay.accountDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if>
								<c:if test="${not empty print.pay.accountDate }">
									<c:choose>
										<c:when test="${print.pay.isAsAccount eq '1'}">
											<fmt:formatDate value="${print.pay.accountDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " />
										</c:when>
										<c:otherwise>
											&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日
										</c:otherwise>
									</c:choose>
								</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${empty print.pay.accountDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if>
								<c:if test="${not empty print.pay.accountDate }">
									<fmt:formatDate value="${print.pay.accountDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " />
								</c:if>
							</c:otherwise>
						</c:choose>
						</td>
						<!-- 20151011 环球行、拉美途客户确认到账时间为空，其它客户当财务撤销确认后，确认收款日期消失 -->
						<td class="fr f3 noborder paddr" colspan="3">确认收款日期：
						<c:choose>
							<c:when test="${isHQX or isLMT }">
								&nbsp;　　&nbsp;年&nbsp;　&nbsp;月&nbsp;　&nbsp;日
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${print.pay.isAsAccount == 0 ||empty print.pay.isAsAccount || print.pay.isAsAccount == 2}">
										&nbsp;　　&nbsp;年&nbsp;　&nbsp;月&nbsp;　&nbsp;日
									</c:when>
									<c:otherwise>
										<c:if test="${empty print.pay.conDate }">
											&nbsp;　　&nbsp;年&nbsp;　&nbsp;月&nbsp;　&nbsp;日
										</c:if>
			 							<c:if test="${not empty print.pay.conDate }">
			 								<fmt:formatDate value="${print.pay.conDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " />
			 							</c:if>	
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
						</td>
					</tr>
				</tbody>
			</table>
			<input type="hidden" name="payid" value="${print.payid }" />
			<input type="hidden" name="ordertype" value="${print.orderType }" />
			<div style="page-break-after: always;"></div>
		</c:forEach>
	</div>
	
	<!--S打印&下载按钮-->
	<div class="dbaniu">
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure('${ctx}');">
		<!-- <input type="button" class="tab ydbz_s" value="返回" onclick="window.opener.location.reload();window.close();" /> -->
	</div>
	<!--S打印&下载按钮-->
</body>
</html>
