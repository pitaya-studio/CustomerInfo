<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>借款申请</title>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript" src="${ctxStatic}/review/borrowing/singlegroup/singleGroupLoanApplication.js"></script>
<script type="text/javascript">
$(function(){
	//input获得失去焦点提示信息显示隐藏
	
 	inputTips();
 	employee("employee");	
	g_context_url = "${ctx}";
	loadCurrency();
	$("select[name='currencyId']").each(function(){
		chageCurr($(this));
	});
	$(".gai-price-btn").on("click",function(){
		var html = '<li><i><input type="hidden" name="travelerId" value="0"><input type="text" name="lendName" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund" name="currencyId" onchange="chageCurr(this);">{0}<select><input type=hidden name="currencyName" value=""><input type=hidden name="currencyExchangerate" value=""><input type=hidden name="currencyMark" value=""></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="lendPrice" class="gai-price-ipt1" flag="istips" onkeyup="lendInput(this)" onblur="lendInputs(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		<%--UG_V2 按钮调整 20170315 by tlw--%>
//		html += '<i><a class="ydbz_s gray clear-btn" onclick="delgroup(this);">删除</a></i>';
		html += '<i><input class="btn ydbz_x clear-btn" type="button" onclick="delgroup(this);" value="删除"></i>';
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
<%@ include file="/WEB-INF/views/review/borrowing/singlegroup/singlegrouporderBaseInfo.jsp"%>
<form method="post" id="f1" name="nf1">

	<select name="currencyTemplate" id="currencyTemplate" style="display:none;">
	</select> 
	<input type="hidden" name="orderId" value="${orderId }">
	<input id="token" name="token" type="hidden" value="${token}" />
	<input type="hidden" name="orderType" value="${productType }" id ="myOrderType">
	<input type="hidden" name="currencyIds" id="currencyIds"> 
	<input type="hidden" name="currencyNames" id="currencyNames"> 
	<input type="hidden" name="currencyMarks" id="currencyMarks"> 
	<input type="hidden" name="borrowPrices" id="borrowPrices"> 
	<input type="hidden" name="currencyExchangerates" id="currencyExchangerates"> 
	<input type="hidden" value="${orderDetail.currencyId}" id="orderCurrencyId" name="orderCurrencyId"> 
	<input type="hidden" value="${orderDetail.totalMoney }" id="orderPrice">
	<!--<input type="hidden" value="${reviewId }" id="reviewId"
		data-type="${reviewObj.currencyId }">  -->
	<input type="hidden" value="${reviewId }" name="reviewId" id="reviewId" data-type="${reviewObj.currencyId }">
	<div class="ydbz_tit">
		<span class="fl">游客借款</span>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table modifyPrice-table">

		<thead>
			<tr>
				<th width="8%">姓名</th>
				<th width="12%">币种</th>
				<th width="11%">游客结算价</th>
				<th width="13%">借款金额</th>
				<th width="20%">备注</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(travelerList) == 0 }">
				<tr>
					<td colspan="5" style="text-align: center;">无游客信息</td>
				</tr>
			</c:if>
			<c:forEach items="${travelerList }" var="v">
				<tr group="travler1">
					<td class="tc">
						${v.name } 
						<input type="hidden" name="travelerName" value="${v.name}"> 
						<input type="hidden" name="travelerId" value="${v.id}"> 
						<input type="hidden" name="payPrice" value="${v.payPrice}">
						<input type="hidden" name="lendName"  />
					</td>
					<td class="tc">
						<select class="selectrefund"   flag="ylendPrice" name="currencyId" onchange="chageCurr(this);">
	
						</select> 
						<input type="hidden" name="currencyName" value=""> 
						<input type="hidden" name="currencyMark" value="">
						<input type="hidden" name="currencyExchangerate" value="">
					</td>
					<td class="tr">${v.payPrice }</td>
					<td class="tc">
						<dl class="huanjia">
							<dt>
								<input name="lendPrice" type="text"  class="borrowPrice"  flag=ylendPrice  onkeyup="lendInput(this)" maxLength="9"
									onblur="lendInputs(this)" />
							</dt>
							<dd>
								<%--UG_V2 按钮调整 20170315 by tlw--%>
								<%--<div class="ydbz_x" flag="appAll">应用全部</div>--%>
								<%--<div class="ydbz_x gray" flag="reset">清空</div>--%>
								<input class="btn btn-primary ydbz_x" type="button" flag="appAll" value="应用全部">
								<input class="btn ydbz_x" type="button" flag="reset" value="清空">
							</dd>
						</dl>
					</td>
					<td class="tc"><input name="remark"  flag=yremark  type="text" value="" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
		<%--UG_V2 按钮调整 20170315 by tlw--%>
		<%--<div class="ydbz_x fl re-storeall"  onclick="allreset();">全部重置</div>--%>
		<input class="btn btn-primary ydbz_x fl re-storeall" type="button" onclick="allreset();" value="全部重置">
	</div>

	<div class="ydbz_tit">
		<span class="fl">团队借款</span>
	</div>
	<div>
		<ol class="gai-price-ol">
			<li><i>
					<input type="hidden" name="travelerId" value="0"> 
					<input type="text" name="lendName" class="gai-price-ipt1" flag="istips" />
					<span class="ipt-tips ipt-tips2">其他款项</span>
				</i> 
				<i> <select class="selectrefund" name="currencyId" onchange="chageCurr(this);">
						<%-- <c:forEach items="${currencyList}" var="lcu">
							<option currencyName ="${lcu.currencyName }" currencyMark ="${lcu.currencyMark }"  value="${lcu.id }">${lcu.currencyName }</option>
						</c:forEach> --%>
					</select> 
					<input type="hidden" name="currencyName" value=""> 
					<input type="hidden" name="currencyMark" value="">
					<input type="hidden" name="currencyExchangerate" value="">
				</i> 
				<i><input type="text" name="lendPrice" class="gai-price-ipt1 borrowPrice" flag="istips" onkeyup="lendInput(this)" onblur="lendInputs(this)" maxLength="9" />
					<span class="ipt-tips ipt-tips2">费用</span>
				</i> 
				<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" />
					<span class="ipt-tips ipt-tips2">备注</span>
				</i> 
				<i>
					<%--UG_V2 按钮调整 20170315 by tlw--%>
					<%--<a class="ydbz_s gai-price-btn">+增加</a>--%>
					<input class="btn btn-primary ydbz_x gai-price-btn" type="button" onclick="allreset();" value="增加">
				</i>
			</li>
		</ol>
	</div>
	
	 <div class="ydbz_tit"><span class="fl">备注</span></div>
     <dl class="gai-price-tex">
     	<dd>
     		<textarea class="" rows="" cols="" id="borrowRemark" name="borrowRemark"></textarea>
     	</dd>
     </dl>
     
     <!-- 还款日期 -->
	<div class="activitylist_bodyer_right_team_co1">
		</br>
		<label><c:if test="${office.isMustRefundDate eq 1 }"><span class="xing">*</span></c:if>还款日期：</label>
		<input type="text" name="refundDate" id="refundDate" value='<fmt:formatDate value="${refundDate}" pattern="yyyy-MM-dd" />' onclick="WdatePicker({})" class="dateinput <c:if test='${office.isMustRefundDate eq 1 }'>requiry</c:if>"/>
	</div>

	<div class="allzj tr f18">
		<!-- <div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">
			借款金额：<span style="font-size:12px;">人民币</span>
			       <span class="red">800</span>
			       <span style="color:green;">+</span>
			       <span style="font-size:12px;">美元</span>
			       <span class="red">800</span>
		</div> -->
		<div class="all-money">
			借款总金额：<span></span>
		</div>

	</div>
	<div class="dbaniu" style="width:260px;">
		<%--UG_V2 按钮调整 20170315 by tlw--%>
		<%--<a class="ydbz_s gray" id="bt_cancel" onclick="closeWindows();">关闭</a>--%>
		<input type="button" id="bt_cancel" onclick="closeWindows();" value="关闭" class="btn">
		<input type="button"  onclick="bind();" value="申请借款" id="bt_submit" class="btn btn-primary">
	</div>
	<!--右侧内容部分结束-->
</form>
	<!--右侧内容部分结束-->
</body>
</html>