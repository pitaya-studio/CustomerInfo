<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单修改</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/modules/order/modify/orderJS.jsp"%>
<script src="${ctxStatic}/modules/order/modifyOrder.js?v=1209" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/single/otherCostFee.js?v=1209" type="text/javascript"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/order/modify/orderPara.jsp"%>
<%@ include file="/WEB-INF/views/modules/order/modify/travelerTemplate.jsp"%>
<div class="ydbzbox fs">
	<%@ include file="/WEB-INF/views/modules/order/modify/orderInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/modify/activityInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/modify/orderBaseInfo4Modify.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/modify/orderTravelerAndOperate.jsp"%>
</div>
</body>
</html>