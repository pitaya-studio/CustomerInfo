<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<div id="manageOrder_new">
	<div class="ydbz_tit">游客信息</div>
	<div class="warningtravelerNum">暂无游客信息</div>
	<!--填充游客列表信息-->
	<div id="traveler">
		<%@ include file="/WEB-INF/views/modules/order/modify/travelerList.jsp"%>
	</div>
	<!--添加游客按钮开始-->
	<div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
	<!--添加游客按钮结束-->
	<!-- 20150812订单预定返佣金额 start -->
	<div class="tourist" style="margin-top: -10px;margin-bottom: 10px;">
		<c:if test="${not empty amount }">
	        <div class="traveler-rebatesDiv">
	        	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	             <c:choose>
		         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<label class="ydLable2 ydColor1" style="width: 100px;">预计团队宣传费：</label>
					</c:when>
		            <c:otherwise>
		                 <label class="ydLable2 ydColor1" style="width: 100px;">预计团队返佣：</label>
		             </c:otherwise>
	         </c:choose>   
	            <span class="disabledshowspan">${currencyMark} </span>
	            <span class="disabledshowspan">${amount }</span>
	        </div>
		</c:if>
    </div>
	<!-- 支付记录 -->
	<c:if test="${not empty orderPayList}">
		<div class="ydbz_tit">收款信息</div>
		<c:forEach items="${orderPayList}" var="orderPay">
			<p class="orderdetails6">
				<span style="width: 16.6%">收款方式：${orderPay.payTypeName }</span>
				<span style="width: 16.6%">收款类型：${fns:getDictLabel(orderPay.payPriceType, "payprice_Type", "")}</span>
				<span style="width: 16.6%">收款金额：${orderPay.moneySerialNum }</span>
				<span style="width: 16.6%">收款时间：<fmt:formatDate value="${orderPay.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></span>
				<span style="width: 16.6%">收款凭证：
					<c:if test="${empty orderPay.payVoucher}">
						<td>暂无收款凭证</td>
					</c:if>
					<c:if test="${not empty orderPay.payVoucher}">
						<td><a class="showpayVoucher" lang="${orderPay.id}">收款凭证</a></td>
					</c:if>
				</span>
				<span style="width: 16.6%">是否到账：
							  <c:choose>
							    <c:when test="${empty orderPay.isAsAccount}">
									未到账
							    </c:when>
							    <c:when test="${orderPay.isAsAccount == 0}">
									未到账
							    </c:when> 
							    <c:when test="${orderPay.isAsAccount == 99}">
									未到账
							    </c:when>  
							    <c:when test="${orderPay.isAsAccount == 2}">
									已驳回
							    </c:when>  
							   <c:otherwise>  
							   		已到账
							   </c:otherwise>
							  </c:choose>
						</span>
			</p>
		</c:forEach>
		<c:if test="${productorder.payStatus == 5 && not empty costChangeStr}">
			<p class="orderdetails6">
				<span>其他费用：${costChangeStr}</span>
			</p>
		</c:if>
	</c:if>
	
	<div style="text-align:right; font-size:12px; ">
		<c:if test="${priceType ne 2}">
			<b style="font-size:18px">订单总<c:if test="${priceType eq 2}">QUAUQ价</c:if><c:if test="${priceType eq 1}">直客价</c:if><c:if test="${priceType ne 1 and priceType ne 2 }">同行价</c:if>：</b><span id="travelerSumPrice" class="tdred f20">${travelerSumPrice}</span>
			<b style="font-size:18px">订单总结算价：</b><span id="travelerSumClearPrice" class="tdred f20">${travelerSumClearPrice}</span>
		</c:if>
		<c:if test="${priceType eq 2}">
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
					<input type="hidden" id="oldDifferenceMoney" value="${differenceMoney}">
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
	
	<div class="ydbz_sxb" id="firstDiv" style="margin-top:20px; padding-top:10px; padding-right:10px;">
	    <div class="ydBtn ydBtn2">
	        <a class="ydbz_x" id="oneToSecondStepDiv">下一步</a>
	    </div>
	</div>
	<div class="ydbz_sxb" id="secondDiv" style='display:none;margin-top:20px; padding-top:10px; padding-right:10px;'>
	    <div class="ydBtn ydBtn2">
	        <div class="ydbz_s" id="modSecondToOneStepDiv" >上一步</div>
			<c:if test="${productorder.payStatus == 1 || productorder.payStatus == 2}">
				<div class="ydbz_x" id="secondToThirdStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存并收款</div>
			</c:if>
			<c:if test="${productorder.payStatus != 1 && productorder.payStatus != 2}">
				<div class="ydbz_x" id="secondToThirdStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">保存</div>
			</c:if>
	    </div>
	</div>
	<div class="ydbz_sxb" id="closeOperation" style="display: none; margin-top:20px; padding-top:10px; padding-right:10px;">
		<div class="ydBtn ydBtn2">
			<a class="ydbz_s" onClick="window.close();">关闭</a>
		</div>
	</div>
 </div>