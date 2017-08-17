<%@ page contentType="text/html;charset=UTF-8"%>
<form method="post" id="f1" name="nf1">
	<select name="currencyTemplate" id="currencyTemplate" style="display:none;"></select> 
	<input type="hidden" name="currencyIds" id="currencyIds"> 
	<input type="hidden" name="currencyNames" id="currencyNames"> 
	<input type="hidden" name="currencyMarks" id="currencyMarks"> 
	<input type="hidden" name="borrowPrices" id="borrowPrices"> 
	<input type="hidden" name="currencyExchangerates" id="currencyExchangerates"> 
	<input type="hidden" value="${orderDetail.currencyId}" id="orderCurrencyId" name="orderCurrencyId"> 
	<input type="hidden" value="${orderId }" name="orderId" id="orderId">
	<input type="hidden" value="${orderDetail.totalMoney }" id="orderPrice">
	<input type="hidden" value="${productOrder}" id="orderType"/>
	<input type="hidden" value="${isAllowMultiRebateObject}" id="rebateObject"/>
	<%-- C475新增返佣对象 add by wangxu --%>
	<c:if test="${isAllowMultiRebateObject == 1 }">
		<div class="ydbz_tit">
				<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
				<c:choose>
					<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<span class="fl">宣传费对象</span>
					</c:when>
					<c:otherwise>
						<span class="fl">返佣对象</span>
					</c:otherwise>
				</c:choose>   
			
	    </div>
	    <div class="rebateObj">
			<span>
				<label><em class="xing">*</em>对象类型：</label>
				<select class="rebateTarget" id="rebateTarget">
				    <option>请选择</option>
				    <option value="1">渠道</option>
				    <option value="2">供应商</option>
				</select>
			</span>
			<span class="supplyNameSpan">
				<label><em class="xing">*</em>供应商名称：</label>
				<select class="supplyName" id="supplyInfo">
				    <option>请选择</option>
				</select>
			</span>
			<span class="supplyInfoSpan">
				<label><em class="xing">*</em>账户类型：</label>
				<select class="accountType" id="accountType">
				    <option>请选择</option>
				    <option value="1">境内</option>
				    <option value="2">境外</option>
				</select>
				<label><em class="xing">*</em>开户行名称：</label>
				<select class="accountName" id="accountName">
				    <option>请选择</option>
				</select>
				<label><em class="xing">*</em>账户号码：</label>
				<select id="accountNo">
				    <option>请选择</option>
				</select>
			</span>
	    </div>
	</c:if>
    <div class="ydbz_tit">
    	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		<c:choose>
			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				<span class="fl">个人宣传费</span>
			</c:when>
			<c:otherwise>
				<span class="fl">个人返佣</span>
			</c:otherwise>
		</c:choose>   
		
   	</div>
	<table class="activitylist_bodyer_table modifyPrice-table">
		   <thead>
			  <tr>
				 <th width="10%">姓名</th>
		         <th width="5%">币种</th>
		         <th width="13%">款项</th>
				 <th width="13%">应收金额</th>
				 <!-- add by jiangyang -->
				 	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
				<c:choose>
					<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<th width="13%">预计个人宣传费金额</th>
						 <th width="14%">宣传费金额</th>
						 <th width="13%">累计宣传费金额</th>
					</c:when>
					<c:otherwise>
						<th width="13%">预计个人返佣金额</th>
						 <th width="14%">返佣金额</th>
						 <th width="13%">累计返佣金额</th>
					</c:otherwise>
				</c:choose>   
<!-- 				 <th width="20%">备注</th> -->
<!-- 		         <th width="13%" style="display:none;">改后返佣金额</th> -->
			  </tr>
		  	</thead>
  			<tbody>
	  			<c:if test="${fn:length(orderDetail.travelInfoList) == 0 }">
					<tr>
						<td colspan="7" style="text-align: center;">无游客信息</td>
					</tr>
				</c:if>
  				<c:forEach items="${orderDetail.travelInfoList}" var="v" varStatus="s">
  					<tr group="travler${s.count}">
						<td>${v.travelName } 
							<input type="hidden" name="travelerName" value="${v.travelName}"> 
							<input type="hidden" name="travelerId" value="${v.id}"> 
							<input type="hidden" name="payPrice" value="${v.payPrice}">
						</td>
						<td>
							<select  class="selectrefund" name="gaijiaCurency" nowvalue="2" onchange="totalSelect();">
							</select>
						</td>
							<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
								<td class="tc"><input name="costname" value="机票宣传费" type="text"/></td>
							</c:when>
							<c:otherwise>
								<td class="tc"><input name="costname" value="机票返佣" type="text"/></td>
							</c:otherwise>
						</c:choose>   
						
						<td class="tr">${v.traPayPrice}</td>
						<!-- add by jiangyang -->
						<td class="tr"><c:if test="${empty v.travelerRebate }">——</c:if>${v.travelerRebate }</td>
							<td class="tc">
							<dl class="huanjia">
								<dt><input name="plusys" type="text" onkeyup="refundInput(this)" onafterpaste="refundInput(this))" /></dt>
								<dd>
									<div class="ydbz_x" flag="appAll">应用全部</div>
									<div class="ydbz_x gray" flag="reset">还原</div>
								</dd>
							</dl>
						</td>
						<td class="tr">
							<c:forEach items="${rebatesMap}" var="entry">    
								<c:if test="${entry.key eq v.id}">
									${fns:getOldRebatesMoneyAmountBySerialNum(entry.value.newRebates)}
								</c:if>
							</c:forEach>  
							<input type="hidden" name="oldRebates" value="${travelerRebates.oldRebates}">
						</td>
						<td class="tr" flag="afterys" style="display:none;">0</td>
					</tr>
  				</c:forEach>
			</tbody>
	</table>
	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
		<div class="ydbz_x fl re-storeall">全部重置</div>
<!-- 	    <div class="fr f14 all-money" style="display:none;">游客返佣差额：<span id="totalTravelerPlus"></span></div> -->
	</div>
	<div class="ydbz_tit">
			<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<span class="fl">团队宣传费</span>
				    	<label class="ydLable2 ydColor1" style="width: 100px; line-height:33px;">预计团队宣传费：</label>
				</c:when>
				<c:otherwise>
					<span class="fl">团队返佣</span>
						<!-- add start by jiangyang -->
				    	<label class="ydLable2 ydColor1" style="width: 100px; line-height:33px;">预计团队返佣：</label>
				</c:otherwise>
			</c:choose>   
    	<span class="disabledshowspan"><c:if test="${empty orderDetail.groupRebate }">——</c:if>${orderDetail.groupRebate }</span>
        <!-- add end   by jiangyang -->
	</div>
	<div>
		<ol class="gai-price-ol">
		   	<li>
				<i>
				<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<input type="text" name="costname" value="机票宣传费" class="gai-price-ipt1" flag="istips" />
				</c:when>
				<c:otherwise>
					<input type="text" name="costname" value="机票返佣" class="gai-price-ipt1" flag="istips" />
				</c:otherwise>
			</c:choose>   
				<span class="ipt-tips ipt-tips2">其他款项</span></i>
				<i>
					<select name="teamCurrency" class="selectrefund" onchange="totalSelect();">
<!-- 						<c:forEach items="${currencyList}" var="currency"> -->
<!-- 							<option value="${currency.id}">${currency.currencyName}</option> -->
<!-- 						</c:forEach> -->
					</select>
				</i>
				<i><input type="text" name="plusys" class="gai-price-ipt1" flag="istips" onkeyup="refundInput(this)" onafterpast="refundInput(this)" value="" /><span class="ipt-tips ipt-tips2">费用</span></i>
<!-- 				<i><span class="ipt-tips ipt-tips2">备注</span></i> -->
				<i><a class="ydbz_s gai-price-btn">+增加</a></i>
		    </li>
		</ol>
	</div>
	
	<div class="ydbz_tit"><span class="fl">备注</span></div>
    <dl class="gai-price-tex"><dd><textarea class="" rows="" cols="" flag="remarks" id="remarks" name="remarks"></textarea></dd></dl> 
	
	<div class="allzj tr f18">
	
		<div class="all-money">
				<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							宣传费差额：<span></span>
				</c:when>
				<c:otherwise>
						返佣差额：<span></span>
				</c:otherwise>
			</c:choose>   
		
		</div>
		<input type="hidden" flag = "leijiprice">
		<input type="hidden" id = "rebatesArray" value="">
	</div>
	<div class="dbaniu" style="width:156px;">
		<a class="ydbz_s" href="javascript:rebates.cancleRebates();">取消</a>
			<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<input type="button" name="apply" value="申请宣传费" class="btn btn-primary">
				</c:when>
				<c:otherwise>
							<input type="button" name="apply" value="申请返佣" class="btn btn-primary">
				</c:otherwise>
			</c:choose>   
		
	</div>
	<!--右侧内容部分结束-->
</form>