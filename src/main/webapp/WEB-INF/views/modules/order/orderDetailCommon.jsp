<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单详情</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/modules/order/modify/orderJS.jsp"%>
<script src="${ctxStatic}/modules/order/orderDetail.js?v=1209" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/single/otherCostFee.js?v=1209" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/orderList.js?v=1209" type="text/javascript"></script>
</head>
<body onload="initSetting();">
<%@ include file="/WEB-INF/views/modules/order/modify/orderPara.jsp"%>
<%@ include file="/WEB-INF/views/modules/order/modify/download.jsp"%>
<%@ include file="/WEB-INF/views/modules/order/modify/travelerTemplate.jsp"%>
<div class="ydbzbox fs">
	<%@ include file="/WEB-INF/views/modules/order/modify/orderInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/modify/activityInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/modify/orderBaseInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/modify/orderTravelerAndOperate.jsp"%>
</div>
</body>
</html>