<%@ page contentType="text/html;charset=UTF-8" %>
<div class="ydbz_tit">产品信息</div>
<div class="orderdetails2">
	<p class="ydbz_mc">${product.acitivityName}</p>
	<ul class="ydbz_info">
		<c:choose>
			<c:when test="${productorder.orderStatus == '10'}">
				<li><span>产品系列：</span>${product.activityLevelName}</li>
				<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
				<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
				<li><span>行程天数：</span>${product.activityDuration}天</li>
				<li id="mddtargetAreaNames" title="${product.targetAreaNames}"><span >目的地：</span>${product.targetAreaNames}</li>
			</c:when>
			<c:otherwise>
				<li><span>旅游类型：</span>${product.travelTypeName}</li>
				<li><span>产品系列：</span>${product.activityLevelName}</li>
				<li><span>产品类型：</span>${product.activityTypeName}</li>
				
				<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
				<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
				<li><span>行程天数：</span>${product.activityDuration}天</li>
				<li id="mddtargetAreaNames" title="${product.targetAreaNames}"><span >目的地：</span>${product.targetAreaNames}</li>
			</c:otherwise>
		</c:choose>	
	</ul>
	<ul class="ydbz_info">
		<c:choose>
			<c:when test="${(productorder.orderStatus == '2' and productorder.priceType ne '2') or productorder.orderStatus == '10' }">
				<li>
					<!-- 报名时选择价格标准，同行价、直客价 -->
					<label>报名价方式：</label>
					<input type="radio" name="priceTypeRadio" id="priceTypeSettlement" value="0" <c:if test="${priceType==0 }">checked="checked"</c:if> disabled="true">同行价
					<input type="radio" name="priceTypeRadio" id="priceTypeSuggest" value="1" <c:if test="${priceType==1 }">checked="checked"</c:if> disabled="true">直客价
				</li>
			</c:when>
			<c:otherwise></c:otherwise>
		</c:choose>
	</ul>
</div>
