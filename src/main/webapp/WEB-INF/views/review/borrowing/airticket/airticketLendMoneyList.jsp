<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>订单-机票-借款申请</title>

<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<!-- 静态资源 -->
<link rel="stylesheet" href="css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<!-- <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script> -->
<!-- <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script> -->
<!-- <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" /> -->
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctxStatic}/review/borrowing/airticket/airticketLoanApplication.js"></script>
<script type="text/javascript">
$(function(){
	//input获得失去焦点提示信息显示隐藏
 	inputTips();
 	employee("employee");
	g_context_url = "${ctx}";
	loadCurrency();
// 	bind();
	$("select[name='currencyId']").each(function(){
		chageCurr($(this));
	});
	$(".gai-price-btn").on("click",function(){
		var html = '<li><i><input type="hidden" name="travelerId" value="0"><input type="text" name="lendName" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund" name="currencyId" onchange="chageCurr(this);">{0}<select><input type=hidden name="currencyName" value=""><input type=hidden name="currencyExchangerate" value=""><input type=hidden name="currencyMark" value=""></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="lendPrice" class="gai-price-ipt1 borrowPrice" flag="istips" onkeyup="lendInput(this)" onblur="lendInputs(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn" onclick="delgroup(this);">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		chageCurr($(this).parents('.gai-price-ol').find("li:last").find("select[name='currencyId']"));
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips($(this).parents('.gai-price-ol').find("li:last"));
	});
});

</script>
</head>
<body>
	<!--右侧内容部分开始-->
                <!--币种模板开始-->
                
                <!--币种模板结束-->
                <%@ include file="/WEB-INF/views/review/borrowing/airticket/airticketOrderInfo.jsp"%>
				<%@ include file="/WEB-INF/views/review/borrowing/airticket/airticketLoanApplication.jsp"%>
	<!--右侧内容部分结束-->
</body>
</html>