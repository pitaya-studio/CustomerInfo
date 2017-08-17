<%@ page contentType="text/html;charset=UTF-8"%>


<form method="post" id="f1" name="nf1">

	<select name="currencyTemplate" id="currencyTemplate" style="display:none;">
	</select> 
	
	<input type="hidden" name="currencyIds" id="currencyIds"> 
	<input type="hidden" name="currencyNames" id="currencyNames"> 
	<input type="hidden" name="currencyMarks" id="currencyMarks"> 
	<input type="hidden" name="borrowPrices" id="borrowPrices"> 
	<input type="hidden" name="currencyExchangerates" id="currencyExchangerates"> 
	<input type="hidden" value="${orderDetail.currencyId}" id="orderCurrencyId" name="orderCurrencyId"> 
	<input type="hidden" value="${orderId }" name="orderId" id="orderId">
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
			<c:if test="${fn:length(orderDetail.travelInfoList) == 0 }">
				<tr>
					<td colspan="5" style="text-align: center;">无游客信息</td>
				</tr>
			</c:if>
			<c:forEach items="${orderDetail.travelInfoList }" var="v">
				<tr group="travler1" >
					<td class="tc">
						${v.travelName } 
						<input type="hidden" name="travelerName" value="${v.travelName}"> 
						<input type="hidden" name="travelerId" value="${v.id}"> 
						<input type="hidden" name="payPrice" value="${v.payPrice}">
						<input type="hidden" name="lendName"  />
					</td>
					<td class="tc">
						<select class="selectrefund" name="currencyId"  flag="yselectrefund" onchange="chageCurr(this);">
	
						</select> 
						<input type="hidden" name="currencyName" value=""> 
						<input type="hidden" name="currencyMark" value="">
						<input type="hidden" name="currencyExchangerate" value="">
					</td>
					<td class="tr">${v.traPayPrice }</td>
					<td class="tc">
						<dl class="huanjia">
							<dt>
									<input name="lendPrice"  class="borrowPrice" flag="ylendPrice" type="text" onkeyup="lendInput(this)"
										onblur="lendInputs(this)"  />
							</dt>
							<dd>
								<div class="ydbz_x" flag="appAll">应用全部</div>
								<div class="ydbz_x gray" flag="reset">清空</div>
							</dd>
						</dl>
					</td>
					<td class="tc">
							<input name="remark"  flag="yremark" type="text" value=""  />
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
		<div class="ydbz_x fl re-storeall" onclick="allreset();">全部重置</div>
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
				<i><input type="text" name="lendPrice" class="gai-price-ipt1 borrowPrice" flag="istips" onkeyup="lendInput(this)" onblur="lendInputs(this)" />
					<span class="ipt-tips ipt-tips2">费用</span>
				</i> 
				<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" />
					<span class="ipt-tips ipt-tips2">备注</span>
				</i> 
				<i>
					<a class="ydbz_s gai-price-btn">+增加</a>
				</i>
			</li>
		</ol>
	</div>
	
	 <div class="ydbz_tit"><span class="fl">备注</span></div>
     <dl class="gai-price-tex"><dd><textarea class="" rows="" cols="" id="borrowRemark" name="borrowRemark"></textarea></dd></dl> 
	<!-- 还款日期 -->
     <div class="activitylist_bodyer_right_team_co2">
     	<br/>
		<label><c:if test="${office.isMustRefundDate eq 1 }"><span class="xing">*</span></c:if>还款日期：</label>
		<input type="text" name="refundDate" id="refundDate" value='<fmt:formatDate value="${refundDate}" pattern="yyyy-MM-dd" />' onclick="WdatePicker({})" class="dateinput <c:if test='${office.isMustRefundDate eq 1 }'>requiry</c:if>"/>
	</div>
	<div class="allzj tr f18">
	
		<div class="all-money">
			借款总金额：<span></span>
		</div>

	</div>
	<div class="dbaniu" style="width:260px;">
		<a class="ydbz_s gray" id="bt_cancel" onclick="closeWindows();">关闭</a>
		<input type="button"  value="申请借款" id="bt_submit" onclick="bind();" class="btn btn-primary">
	</div>
	
	<!--右侧内容部分结束-->
</form>

