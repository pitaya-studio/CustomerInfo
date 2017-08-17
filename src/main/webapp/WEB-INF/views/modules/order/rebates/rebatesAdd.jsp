<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>返佣申请</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/rebates/rebatesList.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){  
			//input获得失去焦点提示信息显示隐藏  
			inputTips();
			rebates.init();
			var groupCodeVal = $("#groupCodeEle").text();
			if(groupCodeVal.length > 20) {
				groupCodeVal = groupCodeVal.substring(0, 20) + "...";
			}
			$("#groupCodeEle").text(groupCodeVal);
		});
	</script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
	    <page:param name="desc">返佣申请</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
    <!--币种模板开始-->
    <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
        <c:forEach items="${currencyList}" var="currency">
			<option value="${currency.id}">${currency.currencyName}</option>
		</c:forEach>
    </select>
    <!--币种模板结束-->
    <div class="mod_nav">订单 > ${fns:getStringOrderStatus(productOrder.orderStatus)} > 返佣记录 > 返佣申请</div>
    <%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
	<div class="ydbz_tit">
		<span class="fl">个人返佣</span>
   	</div>
	<table class="activitylist_bodyer_table modifyPrice-table">
		   <thead>
			  <tr>
				 <th width="8%">姓名</th>
		         <th width="8%">币种</th>
		         <th width="11%">款项</th>
				 <th width="12%">应收金额</th>
				 <!-- 20150812 预订个人返佣  start -->
				  <th width="12%">预计个人返佣</th>
				 <!-- 20150812 预订个人返佣  end -->
				 <th width="12%">原返佣金额</th>
				 <th width="13%">返佣差额</th>
				 <th width="20%">备注</th>
		         <th width="13%" style="display:none;">改后返佣金额</th>
			  </tr>
		  	</thead>
  			<tbody>
  				<c:forEach items="${travelerRebatesList}" var="travelerRebates" varStatus="s">
  					<tr group="travler${s.count}">
						<td>${travelerRebates.traveler.name}<input type="hidden" name="travelerId" value="${travelerRebates.traveler.id}"></td>
						<td>
							<select name="gaijiaCurency" nowvalue="2">
								<c:forEach items="${currencyList}" var="currency">
									<option value="${currency.id}">${currency.currencyName}</option>
								</c:forEach>
							</select>
						</td>
						<td class="tc"><input name="costname" type="text"/></td>
						<td class="tr">${fns:getMoneyAmountBySerialNum(travelerRebates.traveler.payPriceSerialNum,1)}</td>
						<!-- 20150812 预订个人返佣  start-->
						<td class="tr">${fns:getScheduleByUUID(travelerRebates.traveler.rebatesMoneySerialNum) }</td>
						<!-- 20150812 预订个人返佣  end -->
						<!-- 
						<td class="tr">${fns:getOldRebatesMoneyAmountBySerialNum(travelerRebates.oldRebates)}<input type="hidden" name="oldRebates" value="${travelerRebates.oldRebates}"></td> -->
						<td class="tr">${fns:getMoneyAmountBySerialNum(travelerRebates.oldRebates,2)}<input type="hidden" name="oldRebates" value="${travelerRebates.oldRebates}"></td>
						<td class="tc">
							<dl class="huanjia">
								<dt><input name="plusys" type="text" onkeyup="refundInput(this)" onafterpaste="refundInput(this))" /></dt>
								<dd>
									<div class="ydbz_x" flag="appAll">应用全部</div>
									<div class="ydbz_x gray" flag="reset">还原</div>
								</dd>
							</dl>
						</td>
		            	<td class="tc"><input name="remark" type="text"/></td>
						<td class="tr" flag="afterys" style="display:none;">0</td>
					</tr>
  				</c:forEach>
			</tbody>
	</table>
	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
		<div class="ydbz_x fl re-storeall">全部还原</div>
	    <div class="fr f14 all-money">游客返佣差额：<span id="totalTravelerPlus"></span></div>
	</div>
	<div class="ydbz_tit">
		<span class="fl">团队返佣</span>
		<!-- 20150812 预订团队返佣  start-->
		<c:if test="${not empty amount }">
			<label class="ydLable2 ydColor1" style="width: 100px;">预计团队返佣：</label>
			<span class="disabledshowspan">${currencyMark}</span>
			<span class="disabledshowspan">${amount }</span>
		</c:if>
		<!-- 20150812 预订团队返佣  end-->
	</div>
	<div>
		<ol class="gai-price-ol">
		   	<li>
				<i><input type="text" name="costname" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>
				<i>
					<select name="teamCurrency">
						<c:forEach items="${currencyList}" var="currency">
							<option value="${currency.id}">${currency.currencyName}</option>
						</c:forEach>
					</select>
				</i>
				<i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" onkeyup="refundInput(this)" onafterpast="refundInput(this)" value="" /><span class="ipt-tips ipt-tips2">费用</span></i>
				<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>
				<i><a class="ydbz_s gai-price-btn">+增加</a></i>
		    </li>
		</ol>
	</div>
	<div class="allzj tr f18">
		<c:forEach items="${teamList}" var="team">
			<span name='gaijiaCurencyOld' data='${team[2]}' style="display:none;">${team[1]}</span>
		</c:forEach>
		原返佣金额：<span id="totalBefore"></span><br />
		返佣差额：<span id="totalPlus"></span><br />
		<div class="all-money" style="display:none;">改后返佣金额：<span id="totalAfter"></span></div>
	</div>
	<div class="dbaniu" style="width:150px;">
		<a class="ydbz_s" href="javascript:rebates.cancleRebates();">取消</a>
		<input type="button" name="apply" value="申请返佣" class="btn btn-primary">
	</div>
	<!--右侧内容部分结束-->
</body>
</html>