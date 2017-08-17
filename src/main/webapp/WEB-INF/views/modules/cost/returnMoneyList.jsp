<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>请款单</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css"/>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/dayinbdzy.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
function download(payId,prdType, printTime,rid,type) {
	//if(printTime == '') {
		window.location.href = "${ctx}/cost/manager/fankuandanDownload/" + rid + "/" + prdType + "/" +type;
	//}else {
    	$.ajax({
	        type: "POST",
	       url: "${ctx}/cost/manager/updatePrint4fkd",
	        data: {
	        	payId : payId,
	        	prdType : prdType,
	        	reviewId : rid
	        },
	        success: function(msg){	  
	        	if(msg == 'success'){
	        		window.opener.$("#searchForm").submit();
				//	window.location.href = "${ctx}/cost/manager/fankuandanDownload/" + rid + "/" + prdType;
				//	alert("test");
				//	window.location.reload();	
	       		} else {
	       			top.$.jBox.tip(msg);	
	        	}
	        }
	    });
	// }
}
// 打印
function printTure(payId, prdType,rid) {
	$.ajax({
        type: "POST",
        url: "${ctx}/cost/manager/updatePrint4fkd",
        data: {
        	payId : payId,
        	prdType : prdType,
        	reviewId : rid
        },
        success: function(msg){
        	if(msg == 'success'){
        		window.opener.$("#searchForm").submit();
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
      <td width="25%">TO：财务 </td>
      <td width="50%">&nbsp;</td>
      <td width="11%">编号</td>
      <td width="14%">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="4" class="tc finance_table_16px">请款单</td>
    </tr>
    <tr>
      <td colspan="2">项目：其他</td>
      <td colspan="2">日期: ${data.createDate }</td>
    </tr>
  </table>
  <table class="finanece_table_application finanece_table_application_border">
    <tr>
      <td class="tc finance_table_h100">组团社</td>
      <td colspan="3">${data.orderCompanyName }</td>
      <td><span>金额:${data.rebatesDiff }</span> <span></span></td>
    </tr>
    <tr>
      <td width="18%" class="tc">团编号</td>
      <td width="21%">${data.groupCode }</td>
      <td width="16%">团号</td>
      <td width="20%">${data.activityName }</td>
      <td width="25%">&nbsp;</td>
    </tr>
    <!-- 
    <tr>
      <td  class="tc">&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
     -->
    <tr>
      <td colspan="4"  class="tr">
      <p class="tr">金额总计：${data.rebatesDiff }</p>
      <p></p></td>
      <td>${data.rebatesDiff }</td>
    </tr>
    <tr>
      <td colspan="5"><p>户名：${data.payerName }</p>
        <p>开户行：${data.receive_bank_name }</p>
        <p>账号：${data.receive_account }</p></td>
    </tr>
    <tr>
      <td colspan="3"><p>请款人签字：</p></td>
      <td colspan="2"><p>操作部主管签字：</p>
        <p></p></td>
    </tr>
    <tr>
      <td colspan="3">经理签字：</td>
      <td colspan="2">部门财务签字：</td>
    </tr>
    <tr>
      <td colspan="3">财务主管签字：</td>
      <td colspan="2">出纳签字：</td>
    </tr>
    <tr>
      <td colspan="5"><p>财务备注：</p>
        <p></p></td>
    </tr>
  </table>
</div>
<!--S打印&下载按钮-->
	<div class="dbaniu">
		<input id="dayinDiv" name="dayinDiv" type="button" class="tab ydbz_s" value="打印" onclick="printTure(${data.payId},${data.prdType},${data.rid });">
		<input id="" name="" type="button" class="ydbz_s" value="下载" onclick="download(${data.payId},${data.prdType},'${data.printTime }',${data.rid },${data.type });">
		<input type="button" value="关闭" onclick="javaScript:window.close();" class="ydbz_s">
	</div>
</body>
</html>