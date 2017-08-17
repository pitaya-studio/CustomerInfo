<%@ page contentType="text/html;charset=UTF-8" %>
<div class="ydbz yd-step1" id="stepbar">&nbsp;</div>
<div class="ydbz_tit">订单基本信息</div>
<p class="ydbz_mc">${product.acitivityName}</p>
<!-- 产品基本信息 -->
<ul class="ydbz_info">
	<li><span>团号：</span><em class="fArial" title="${productGroup.groupCode}">${productGroup.groupCode}</em></li>
	<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
	<li title="${product.targetAreaNames}"><span>目的地：</span>${product.targetAreaNames}</li>
	<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
	<li><span>行程天数：</span>${product.activityDuration}天</li>
	<li><span>预收人数：</span>${productGroup.planPosition}</li>
	<li><span>余位人数：</span>${productGroup.freePosition}</li>
	<li><span>销售：</span>${fns:getUserById(productorder.salerId).name}</li>
	<li><span>下单人：</span>${fns:getUser().name}</li>
</ul>