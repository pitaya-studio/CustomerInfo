<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!--费用结算开始-->
<div class="ydbz_tit"><span class="ydExpand" data-target="#costSettlement"></span>费用结算</div>
<div id="costSettlement">
	<div class="mod_information_dzhan">
		<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
			<div class="mod_information_d2">
				<label>订单总额：</label><span class="totalCost" id="costMoneySpan">${costMoneyStr }</span>
			</div>
			<div class="mod_information_d2 ">
				<label>结算总额：</label><span class="red accounts" id="totalMoneySpan">${totalMoneyStr }</span>
			</div>
		</div>
	</div>
	<!-- <p class="ydbz_qdmc"></p>-->
	<p class="price_sale_houser_line"></p>
	<div class="mod_information_dzhan">
		<div class="mod_information_dzhan_d error_add1" id="oneStepContent" style="overflow: hidden;">
			<div class="mod_information_d2">
				<label>应收金额：</label><span class="accounts">${totalMoneyStr }</span>
			</div>
			<div class="mod_information_d2 ">
				<label>已收金额：</label><span class="payedMoney" id="payedMoneySpan">${payedMoneyStr }</span>
			</div>
			<div class="mod_information_d2 ">
				<label>未收金额：</label><span class="green unReceipted">${noPayMoneyStr }</span>
			</div>
		</div>
	</div>
</div>
<!--费用结算结束-->