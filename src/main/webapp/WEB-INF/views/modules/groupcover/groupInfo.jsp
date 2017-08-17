<%@ page contentType="text/html;charset=UTF-8" %>
<div class="ydbz_tit">产品信息</div>
<div class="orderdetails2">
    <p class="ydbz_mc">${product.acitivityName }</p>
	<ul class="ydbz_info">
		<li><span>团号：</span>${productGroup.groupCode}</li>
		<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
		<li><span title="${product.targetAreaNames}">目的地：</span>${product.targetAreaNames}</li>
	</ul>
	<ul class="ydbz_info">
		<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
		<li><span>行程天数：</span>${product.activityDuration}天</li>
		<li><span>预收人数：</span>${productGroup.planPosition}</li>
	</ul>
	<ul class="ydbz_info">
		<li><span>余位人数：</span>${productGroup.freePosition}</li>
	</ul>
	<ul class="ydbz_dj specialPrice">
		<li><span class="ydtips">单价</span>
			<p>成人：<font color="red"><c:if test="${not empty productGroup.settlementAdultPrice}">${fns:getCurrencyInfo(productGroup.currencyType,0,'mark')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${productGroup.settlementAdultPrice}" /></font></p>
			<p>儿童：<font color="red"><c:if test="${not empty productGroup.settlementcChildPrice}">${fns:getCurrencyInfo(productGroup.currencyType,1,'mark')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${productGroup.settlementcChildPrice}" /></font></p>
			<p>特殊人群：<font color="red"><c:if test="${not empty productGroup.settlementSpecialPrice}">${fns:getCurrencyInfo(productGroup.currencyType,2,'mark')}</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${productGroup.settlementSpecialPrice}" /></font></p>
		</li>
	</ul>
</div>