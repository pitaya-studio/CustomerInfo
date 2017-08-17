<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<c:if test="${!empty convertFlag && convertFlag == 1}">
	<c:if test="${!empty moneyFlag && moneyFlag != 0}">
		<tr>
			<td colspan="2" class="exchange-radio">
				<em class="xing">*</em>合并支付：
				<label><input type="radio" name="mergePayFlag" value="1" <c:if test="${mergePayFlag == 1}">checked</c:if> <c:if test="${convertUseFlag == 1}">disabled</c:if> checked="checked"> 是</label>
				<label><input type="radio" name="mergePayFlag" value="0" <c:if test="${mergePayFlag == 0}">checked</c:if> <c:if test="${convertUseFlag == 1}">disabled</c:if>>否</label>
			</td>
		</tr>
		<c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
			<c:forEach items="${curlist}" var="cur">
				<c:if test="${cur.id==currencyId}">
					<tr>
						<td class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
						<td class="exchange">
							<input type="text" maxlength="12" size="10" value="${paramCurrencyPrice[status.index]}" name="dqzfprice" onblur="exchange(this)" class="required"/> 
							<label <c:if test="${mergePayFlag == 0}">style="display: none;"</c:if>>
								汇率选择：<input class="exchange-rate" type="text" maxlength="10" size="10" value="${convertList[status.index]}" name="convertLowest" onblur="exchange(this)" <c:if test="${convertUseFlag == 1}">readonly</c:if> />
							</label> 
							<label <c:if test="${mergePayFlag == 0}">style="display: none;"</c:if>>
								换算后的金额： <span class="exchange-zje">${subSum[status.index]}</span>
							</label> 
							<input type="hidden" value="${cur.id}" name="currencyIdPrice" /></td>
					</tr>
				</c:if>
			</c:forEach>
		</c:forEach>
		<tr class="exchange-total" <c:if test="${mergePayFlag == 0}">style="display: none;"</c:if>>
			<td class="trtextaling">金额合并：</td>
			<td class="exchange"><input type="text" class="exchange-total" name="mergeCurrencyPrice" value="${totalSum}" readonly /></td>
		</tr>
	</c:if>
</c:if>
<c:if test="${!empty convertFlag && convertFlag == 0}">
	<c:if test="${!empty moneyFlag && moneyFlag != 0}">
		<c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
			<c:forEach items="${curlist}" var="cur">
				<c:if test="${cur.id==currencyId}">
					<tr>
						<td class="trtextaling" width="26%"><span style="color:#f00;">*</span>${cur.currencyName}金额：${cur.currencyMark}</td>
						<td>
							<c:choose>
								<c:when test="${moneyFlag==1}">
									<input type="text" maxlength="12" value="<fmt:formatNumber type='currency' pattern='###0.00' value='${paramCurrencyPrice[status.index]}' />" name="dqzfprice"  class="required"/>
								</c:when>
								<c:when test="${moneyFlag==2}">
									<input type="text" maxlength="12" value="<fmt:formatNumber type='currency' pattern='###0.00' value='${paramCurrencyPrice[status.index]}' />" name="dqzfprice" readonly="readonly" />
								</c:when>
							</c:choose>
						</td>
						<input type="hidden" value="${cur.id}" name="currencyIdPrice" />
					</tr>
				</c:if>
			</c:forEach>
		</c:forEach>
	</c:if>
</c:if>