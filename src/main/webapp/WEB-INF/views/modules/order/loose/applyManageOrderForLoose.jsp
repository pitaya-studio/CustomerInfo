<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>填写散拼预报名信息</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/modules/order/common/orderLooseCommonJS.jsp"%>
<script src="${ctxStatic}/modules/order/single/applysingleorder.js" type="text/javascript"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/order/common/travelerTemplate.jsp"%>
<div class="ydbzbox fs">
	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfoHide.jsp"%>
	<div class="ydbz yd-step1" id="stepbar">&nbsp;</div>
	<div class="ydbz_tit">订单基本信息</div>
	<p class="ydbz_mc">${product.acitivityName}</p>
	<ul class="ydbz_info">
		<li><span>团号：</span><em class="fArial">${productGroup.groupCode}</em></li>
		<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
		<li title="${product.targetAreaNames}"><span>目的地：</span>${product.targetAreaNames}</li>
		<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
		<li><span>行程天数：</span>${product.activityDuration}天</li>
		<li><span>预收人数：</span>${productGroup.planPosition}</li>
		<li><span>已切位数：</span>${empty groupReserve.payReservePosition?0:groupReserve.payReservePosition}</li>
		<li><span>余位人数：</span>${productGroup.freePosition}</li>
		<li><span>预报名数：</span>${productGroup.orderPersonNum}</li>
		<li><span>销售：</span>${fns:getUserById(productorder.salerId).name}</li>
		<li><span>下单人：</span>${fns:getUser().name}</li>
	</ul>
	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/common/orderTravelerAndOperate.jsp"%>
</div>
</body>
</html>