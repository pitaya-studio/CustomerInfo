<%@ page contentType="text/html;charset=UTF-8" %>
<div id="manageOrder_new" style="display:none;">
<div class="ydbz_tit">
	请填写游客信息
	<input type="hidden" id="newOrderFlag" value="${newOrderFlag }" >
	<c:if test="${isForYouJia and isLoose}">
	<input type="button" name="applyDiscount" class="btn btn-primary fr discount-button-mr" onclick="jbox__apply_for_tourist_fee__pop_fab();" value="申请优惠">
	</c:if>
</div>
	<div class="warningtravelerNum">暂无游客信息</div>
	<!--填充游客列表信息-->
	<div id="traveler">
	</div>
	<!--添加游客按钮开始-->
	<div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
	<!--添加游客按钮结束-->
	<!-- 20150812订单预定返佣金额 start -->
	<div class="tourist" style="margin-top: -10px;margin-bottom: 10px;">
        <div class="traveler-rebatesDiv">
        	<!-- 265需求，针对鼎鸿假期，将所有返佣字段改为宣传费 -->
	         <c:choose>
	         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					<label class="ydLable2 ydColor1" style="width: 100px;">预计团队宣传费：</label>
				</c:when>
	            <c:otherwise>
	                 <label class="ydLable2 ydColor1" style="width: 100px;">预计团队返佣：</label>
	             </c:otherwise>
	         </c:choose>   
            <select id="rebatesCurrency">${currencyOptions}</select>
            <input id="rebatesMoney" type="text" class="required ipt-rebates" name="rebatesMoney" maxlength="9" onafterpaste="checkRebatesValue(this)" onkeyup="checkRebatesValue(this)"/>
        </div>
    </div>
	<!-- 20150812订单预定返佣金额 end -->
	<div style="text-align:right;">
		<c:if test="${agentSourceType ne 2}">
			<b style="font-size:18px">订单总<span id="totalSPGPrice"></span>：</b><span id="travelerSumPrice" class="tdred f20"></span>
			<b style="font-size:18px">订单总结算价：</b><span id="travelerSumClearPrice" class="tdred f20"></span>
		</c:if>
		<c:if test="${agentSourceType eq 2}">
			<c:choose>
	         	<c:when test="${not empty differenceFlag and differenceFlag eq 1}"> 
					</br></br></br><b style="font-size:18px">系统结算价总额：</b><span id="orderTotalPrice" class="tdred f20"></span>
					<em class="what relative" style="display: none;">
						<span class="what_child" style="background:lightyellow;">
							<b>含代收服务费：</b><span id="totalChargePrice" class="tdgray f16"></span>
						</span>
					</em>
					<input type="hidden" id="totalCharge" name="totalCharge">
					
					
					</br><b style="font-size:18px">门店结算价差额返还：</b><span id="orderTotalPrice" class="tdred f20"></span>
					${adultCurrencyMark}<input id="differenceMoney" name="differenceMoney" value="${differenceMoney}" onkeyup="setAllTotalMoney(this);" onblur="setAllTotalMoney(this);" maxlength="14">
					</br><b style="font-size:18px">收款总额：</b><span id="allTotoalMoney" class="tdred f20"></span>
				</c:when>
	            <c:otherwise>
					</br></br></br><b style="font-size:18px">订单总额：</b><span id="orderTotalPrice" class="tdred f20"></span>
					<em class="what relative" style="display: none;">
						<span class="what_child" style="background:lightyellow;">
							<b>含代收服务费：</b><span id="totalChargePrice" class="tdgray f16"></span>
						</span>
					</em>
					<input type="hidden" id="totalCharge" name="totalCharge">
	             </c:otherwise>
			</c:choose>
		</c:if>
	</div>
	<div class="ydbz_sxb" id="secondDiv" style="display:none;font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;" >
	    <div class="ydBtn ydBtn2">
	        <a class="ydbz_s" id="secondToOneStepDiv">上一步</a>
	        <a class="ydbz_x" id="secondToThirdStepDiv">下一步</a>
	    </div>
	</div>
	<div class="ydbz_sxb" id="thirdDiv" style='display:none;font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;'>
	    <div class="ydBtn ydBtn2 extend">
	        <div class="ydbz_s" id="thirdToSecondTStepDiv" >上一步</div>
			<c:if test="${productorder.payStatus == 1 || productorder.payStatus == 2}">
				<div class="ydbz_x" id="thirdToFourthStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存并收款</div>
			</c:if>
			<c:if test="${productorder.payStatus != 1 && productorder.payStatus != 2}">
				<div class="ydbz_x" id="thirdToFourthStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存</div>
			</c:if>
			<div class="ydbz_x" id="saveThenSubmit" style="display:none" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存并提交审批</div>
	    </div>
	</div>
 </div>
<!--S--C109--报名游客中申请优惠-->
<div id="apply-for-tourist-fee-pop" class="display-none">
	<div class="apply-for-tourist-fee-pop">
		<table class="table-visa">
			<thead>
			<tr>
				<th colspan="6" class="tl">申请优惠</th>
			</tr>
			<tr>
				<th class="tc" width="10%">选择</th>
				<th class="tc" width="15%">姓名</th>
				<th class="tc" width="15%">游客类型</th>
				<th class="tc" width="20%">同行价</th>
				<th class="tc" width="20%">同行结算价</th>
				<th class="tc" width="20%">申请优惠金额</th>
			</tr>
			</thead>
			<tbody class="travelerDiscountInfo">
			</tbody>
		</table>
	</div>
</div>
<!--E--C109--报名游客中申请优惠-->

<!--S--C299--报名游客展示价格方案-->
<div class="pricePlan_container" style="display: none">
	<table id="pricePlanTable" name="pricePlanTable"
		   class="table activitylist_bodyer_table border-table-spread" style="margin: 0 auto">
		<thead>
		<tr>
			<th rowspan="2" class="tc" style="width: 50px">序号</th>
			<th rowspan="2" class="tc" style="width: 500px">
				价格方案
			</th>
			<th colspan="3" class="tc t-th2">同行价</th>
			<c:if test="${activityKind == '2'}">
				<th colspan="3" class="tc t-th2">直客价</th>
			</c:if>
			<th rowspan="2" class="tc">备注</th>
		</tr>
		<tr>
			<th class="tc">成人</th>
			<th class="tc">儿童</th>
			<th class="tc" style="border-right: 0px">特殊人群</th>
			<c:if test="${activityKind == '2'}">
			<th class="tc">成人</th>
			<th class="tc">儿童</th>
			<th class="tc" style="border-right: 0px">特殊人群</th>
			</c:if>
		</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
</div>
<!--E--C299--报名游客展示价格方案-->