<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta  charset='utf-8' />
<title>支出凭单</title>

<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<style type="text/css">
	.dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
	.ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
    .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
    .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
</style>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
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
	var ids ='${ids}'; 
	$.ajax({
		type:"GET",
        url:ctx+"/cost/manager/batchUpdatePrinted",
		dataType:"json",
		async:false,			//注意此处不能改成 true，否则FireFox有问题(此Ajax不执行)。
        data:{ids:ids},
        success:function(item){
			console.log(item);
			if(item.flag == 'success'){
				for(var i = 0 ; i < ids.length ; i ++){
					updatePrinted(ids.id,ids.orderType,item.msg);
				}
			}
        }
	});
   	printPage(document.getElementById("printDiv"));
   	window.opener.$("#searchForm").submit();  //刷新父页面
}
	
function updatePrinted(id, orderType, nowDate){
	var doc = window.opener.document;
   	var idval = 'print_'+id+'_'+orderType;
   	var $td = $(doc.getElementById(idval));
   	var $value = $td.text().trim();
   	if($value.indexOf('已打印') == -1){
   		var v = "<p class='uiPrint'>已打印<span style='display: none;'>首次打印时间<br>"+nowDate+"</span></p>";
   		$td.html(v);
   	}
}
</script>
</head>

<body>
	<div id="printDiv">
	<c:forEach items="${list }" var="list">
		<table class="dayinzy" >
			<thead></thead>
			<thead>
				<tr>
					<th class="fr f4 paddr" colspan="8">首次打印日期：
						<fmt:formatDate value="${list.firstPrintTime }" type="both" dateStyle="default" pattern="yyyy/MM/dd HH:mm"/>
					</th>
				</tr>
				<tr>
					<th class="f1" colspan="8">支 出 凭 单</th>
				</tr>
				<tr>
					<th class="fl paddl f3 bot_border" colspan="4">填写日期：
						<c:if test="${empty list.createDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if>
						<c:if test="${not empty list.createDate }"><fmt:formatDate value="${list.createDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " /></c:if>
					</th>
					<th class="fr paddr f3 bot_border" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
				</tr>
			</thead>
			</table>
		<table  class="dayinzy  dayInIe">
			<tbody>
				<tr>
					<td class="fc f2" width="110">团号</td>
					<td class="fc f3" colspan="2" style="white-space: normal;word-break: break-all">
						&nbsp;${list.groupCode}
					</td>
					<td class="fc f2" width="90">款项</td>
					<td class="fc f3" width="110" colspan="2">&nbsp;${list.money}</td>
					<td class="fc f2" width="60">经办人</td>
					<td class="fc f3" width="90">&nbsp;${list.person}</td>
				</tr>
				<tr>
					<td class="fc f2">摘要</td>
					<td class="fl f3 paddl" colspan="7">
						<div class="danhangnormal">
							<c:choose>
								<c:when test="${list.group }">
									<c:if test="${not empty list.remark }">项目备注：${list.remark}&nbsp;&nbsp;</c:if>
									<c:if test="${not empty list.pay.remarks }">付款备注：${list.pay.remarks }</c:if>
								</c:when>
								<c:otherwise>
									&nbsp;${list.pay.remarks }
								</c:otherwise>
							</c:choose>
						</div>
					</td>
				</tr>
				<tr>
					<td class="fc f2">收款单位</td>
					<td class="fc f3" colspan="7">
						<div class="danhangnormal">
						<c:if test="${list.group }">
							<c:choose>
							   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && list.supplyName=='非签约渠道'}"> 
								       直客
							   </c:when>
							   <c:otherwise>
								   &nbsp;${list.supplyName}
							   </c:otherwise>
							</c:choose> 
						</c:if>
						&nbsp;&nbsp;${list.bankName}&nbsp;&nbsp;${list.accountName}&nbsp;&nbsp;${list.bankAccount}
						</div>
					</td>
				</tr>
				 <c:forEach items="${list.curlist}" var="currency">
					<c:if test="${list.currencyId == currency.id }">
						&nbsp;<c:set var="currencyName" value="${currency.currencyName}"></c:set>
						<c:set var="currencyExchangerate" value="${list.rate}"></c:set>
						<c:set var="currentAmount" value="${list.currencyMoney}"></c:set>
						<c:if test="${not list.group }">
							&nbsp;<c:set var="amount" value="${currencyMoney  }"></c:set>
						</c:if>
					</c:if>
			    </c:forEach>
				<tr>
					<td class="fc f2">人民币</td>
					<td class="fl f3" style="text-align:right;">
						<c:if test="${fn:startsWith(currencyName, '人民')}">
							&nbsp;<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${list.amount}" />
						</c:if>
					</td>
					<td class="fc f2">美元</td>
					<td class="fc f3">汇率
						<c:if test="${fn:startsWith(currencyName, '美元')}">&nbsp;${currencyExchangerate}</c:if>
					</td>
					<td class="fl f3" style="text-align:right;">
						<c:choose>
							<c:when test="${list.group }">
								<c:if test="${fn:startsWith(currencyName, '美元')}">
									&nbsp;<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${currentAmount}" />
				        		</c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${fn:startsWith(currencyName, '美元')}">
									&nbsp;<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${list.originMoney}" />
						        </c:if>
							</c:otherwise>
						</c:choose>
					</td>
					<td class="fc f2">加币</td>
					<td class="fc f3">汇率<c:if test="${fn:startsWith(currencyName, '加')}">&nbsp;${currencyExchangerate}</c:if></td>
					<td class="fl f3" style="text-align:right;">
						<c:choose>
							<c:when test="${list.group }">
								&nbsp;<c:if test="${fn:startsWith(currencyName, '加')}"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${currentAmount}" /></c:if>
							</c:when>
							<c:otherwise>
								<c:if test="${fn:startsWith(currencyName, '加')}">
									&nbsp;<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${list.originMoney}" />
								</c:if>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="fc f2">合计人民币</td>
					<td class="fl f3" colspan="4">
						<c:choose>
							<c:when test="${list.group }">
								<c:if test="${list.amount < 0 }">红字${fns:changeAmount(fn:replace(list.amount,"-",""))}</c:if>
								<c:if test="${list.amount > 0 }">&nbsp;${fns:changeAmount(list.amount)}</c:if>
							</c:when>
							<c:otherwise>
								&nbsp;${list.totalRMBDX}
							</c:otherwise>
						</c:choose>
					</td>
					<td class="fc f3">¥</td>
					<td class="fl f3" colspan="2" style="text-align:right;">
						<c:choose>
							<c:when test="${list.group }">
								&nbsp;<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${list.amount}" />
							</c:when>
							<c:otherwise>
								&nbsp;${list.totalRMB}
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<!-- 越柬行踪付款方式为支票或者现金时，显示实际领款人 需求号0419，update by shijun.liu 2016.04.25 -->
					<td class="fc f2">
						<c:choose>
							<c:when test="${list.group }">
								<c:if test="${not empty list.payee}">实际</c:if>
							</c:when>
						</c:choose>
						领款人</td>
					<td class="fc f3" colspan="2">
						<c:if test="${list.group }">
							<c:choose>
								<c:when test="${not empty list.payee}">&nbsp;${list.payee}</c:when>
								<c:otherwise>&nbsp;${list.person }</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${not list.group }">
							&nbsp;${list.person }
						</c:if>
					</td>
					<td class="fc f2">主管审批</td>
					<td class="fc f3" colspan="4">&nbsp;${list.deptmanager }</td>
				</tr>
				<tr>
					<c:choose>
						<c:when test="${list.group }">
							<td class="fc f2">总经理</td>
							<td class="fc f3">&nbsp;${list.manager}</td>
							<td class="fc f2">财务主管</td>
							<td class="fc f3">&nbsp;${list.finance }</td>
							<td class="fc f2">出纳</td>
							<td class="fc f3">&nbsp;${list.cashier }</td>
							<td class="fc f2">审核</td>
							<td class="fc f3">&nbsp;${list.reviewer}</td>
						</c:when>
						<c:otherwise>
							<td class="fc f2">总经理</td>
							<td class="fc f3">
								${list.manager}
							<td class="fc f2">财务主管</td>
							<td class="fc f2">&nbsp;</td>
							<td class="fc f2">出纳</td>
							<td class="fc f3">
								&nbsp;
							</td>
							<td class="fc f2">审核</td>
							<td class="fc f3">&nbsp;${list.reviewer}</td>
						</c:otherwise>
					</c:choose>
				</tr>
				</tbody>
			</table>
				<table  class="dayinzy  TopHeight">
				<tr>
					<td class="fr f3 noborder paddr" colspan="8">确认付款日期：
					<c:if test="${list.group }">
						<c:choose>
							<c:when test="${list.isHQX or list.isLMT}">
									&nbsp;　　&nbsp;年&nbsp;　&nbsp;月&nbsp;　&nbsp;日
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${list.payStatus eq '0'}">
										&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日
									</c:when>
									<c:otherwise>
										<c:if test="${empty list.conDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if>
										<c:if test="${not empty list.conDate }"><fmt:formatDate value="${list.conDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " /></c:if>&nbsp;　&nbsp;
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${not list.group  }">
							&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日
					</c:if>
					</td>
				</tr>
		</table>
		<div style="page-break-after: left;"></div> 
	</c:forEach>	
	</div>
	<!--S打印&下载按钮-->
	<div class="dbaniu">
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure('${ctx}');">
	</div>
</body>
</html>