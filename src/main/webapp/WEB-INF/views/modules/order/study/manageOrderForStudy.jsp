<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>填写游学订单信息</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/modules/order/common/orderCommonJS.jsp"%>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/order/common/travelerTemplate.jsp"%>
<div class="ydbzbox fs">
	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfoHide.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/common/activityBaseInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/common/orderTravelerAndOperate.jsp"%>
</div>
</body>
</html>