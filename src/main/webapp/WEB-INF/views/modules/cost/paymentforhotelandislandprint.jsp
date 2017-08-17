<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<jsp:useBean id="now" class="java.util.Date" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>支出 凭单</title>

<link href="${ctxStatic}/css/dayinbdzy.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<style type="text/css">
	.dbaniu { overflow: hidden; margin-top: 50px; margin-right: auto; margin-bottom: 50px; margin-left: auto; text-align: center; }
	.ydbz_s{height:28px; padding:0 15px;text-align:center; display: inline-block;margin:0 3px;cursor:pointer;}
    .ydbz_s{color:#fff;border:medium none;background:#5f7795;box-shadow: none;text-shadow: none;font-size:12px;border-radius:4px;height:28px;line-height:28px;}
    .ydbz_s:hover{ text-decoration:none; background:#28b2e6; color:#fff;}
</style>
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
function printTure(ctx,id,orderType) {
	   //window.opener.$("#searchForm").submit();  刷新父页面 
	$.ajax({
         type:"GET",
         url:ctx+"/cost/manager/updatePrintedForHotelAndIsland",
         dataType:"json",
         data:{"id":id,"orderType":orderType},
         success:function(data){
           //console.log(data);
           if(data.flag == 'success'){
           		updatePrinted(id,orderType,data.msg);
           		printPage(document.getElementById("printDiv"));
           }
         }
      });
   
}
	
function downLoadTrue(ctx,id,orderType){
	//window.location.href=ctx+"/cost/manager/paymentList?id="+id+"&orderType="+orderType;
	
	$.ajax({
        type:"GET",
        url:ctx+"/cost/manager/updatePrintedForHotelAndIsland",
        dataType:"json",
        data:{"id":id,"orderType":orderType},
        async: false,
        success:function(data){
          //console.log(data);
          if(data.flag == 'success'){
        	    if(data.msg){
        	    	updatePrinted(id,orderType,data.msg);
        	    }
          		window.location.href=ctx+"/cost/manager/paymentListForHotelAndIsland?id="+id+"&orderType="+orderType;
          }
        }
     });
	//updatePrinted(id,orderType,new Date().Format("yyyy-MM-dd hh:mm:ss"));
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
		<table class="dayinzy">
			<thead>
				<tr>
					<th class="fr f4 paddr" colspan="8">首次打印日期：
						<fmt:formatDate value="${firstPrintTime }" type="both" dateStyle="default" pattern="yyyy/MM/dd HH:mm"/>
					</th>
				</tr>
				<tr>
					<th class="f1" colspan="8">支 出 凭 单</th>
				</tr>
				<tr>
					<th class="fl paddl f3" colspan="4">填写日期：
						<c:if test="${empty createDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if>
						<c:if test="${not empty createDate }"><fmt:formatDate value="${createDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " /></c:if>
					</th>
					<th class="fr paddr f3" colspan="4">第&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td class="fc f2" width="110">团号</td>
					<td class="fc f3" colspan="2">
						${groupCode}
					</td>
					<td class="fc f2" width="90">款项</td>
					<td class="fc f3" width="110" colspan="2">
						${money}
					</td>
					<td class="fc f2" width="60">经办人</td>
					<td class="fc f3" width="90">
						${person}
					</td>
				</tr>
				<tr>
					<td class="fc f2">摘要</td>
					<td class="fl f3 paddl" colspan="7">
						${pay.remarks}
					</td>
				</tr>
				<tr>
					<td class="fc f2">收款单位</td>
					<td class="fc f3" colspan="7">
						<div class="danhangnormal">
						${supplyName}&nbsp;&nbsp;${bankName}&nbsp;&nbsp;${bankAccount}
						</div>
					</td>
				</tr>
				 <c:forEach items="${curlist}" var="currency">
					<c:if test="${currencyId == currency.id }">
						<c:set var="currencyName" value="${currency.currencyName }"></c:set>
						<c:set var="currencyExchangerate" value="${currency.convertLowest }"></c:set>
						<c:set var="currentAmount" value="${currencyMoney }"></c:set>
						<c:set var="amount" value="${currencyMoney  }"></c:set>
					</c:if>
			    </c:forEach>
				<tr>
					<td class="fc f2">人民币</td>
					<td class="fl f3" style="text-align:right;">
						<c:if test="${fn:startsWith(currencyName, '人民')}"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${amount}" /></c:if>
					</td>
					<td class="fc f2">美元</td>
					<td class="fc f3">
						汇率
						<c:if test="${fn:startsWith(currencyName, '美元')}">${currencyExchangerate}</c:if>
					</td>
					<td class="fl f3" style="text-align:right;">
						<c:if test="${fn:startsWith(currencyName, '美元')}">
				            <fmt:formatNumber  type="currency" pattern="#,##0.00" value="${originMoney}" />
				        </c:if>
					</td>
					<td class="fc f2">加币</td>
					<td class="fc f3">汇率<c:if test="${fn:startsWith(currencyName, '加')}">${currencyExchangerate}</c:if></td>
					<td class="fl f3" style="text-align:right;">
						<c:if test="${fn:startsWith(currencyName, '加')}">
							<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${originMoney}" />
						</c:if>
					</td>
				</tr>
				<tr>
					<td class="fc f2">合计人民币</td>
					<td class="fl f3" colspan="4">${totalRMBDX}</td>
					<td class="fc f3">￥</td>
					<td class="fl f3" colspan="2" style="text-align:right;">${totalRMB}</td>
				</tr>
				<tr>
					<td class="fc f2">领款人</td>
					<td class="fc f3" colspan="2">
						${person }
					</td>
					<td class="fc f2">主管审批</td>
					<td class="fc f3" colspan="4">
						${deptmanager }
					</td>
				</tr>
				<tr>
					<td class="fc f2">总经理</td>
					<td class="fc f3">
						${manager}
					<td class="fc f2">财务主管</td>
					<td class="fc f2">&nbsp;</td>
					<td class="fc f2">出纳</td>
					<td class="fc f3">
						&nbsp;
					</td>
					<td class="fc f2">审核</td>
					<td class="fc f3">${reviewer}</td>
				</tr>
				<tr>
					<td class="fr f3 noborder paddr" colspan="8">确认付款日期：
							&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日
<%-- 							<c:if test="${empty conDate }">&nbsp;　　　　&nbsp;年&nbsp;　　&nbsp;月&nbsp;　　&nbsp;日</c:if> --%>
<%-- 							<c:if test="${not empty conDate }"><fmt:formatDate value="${conDate}" type="both" dateStyle="long" pattern="yyyy 年  MM 月  dd 日 " /></c:if>&nbsp;　&nbsp; --%>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<!--S打印&下载按钮-->
	<div class="dbaniu">
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure('${ctx}','${id}','${orderType}');">
		<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="downLoadTrue('${ctx}','${id}','${orderType}')">
	</div>
</body>
</html>