<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>定价策略查询</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
   <style type="text/css">
   #contentTable td{padding:4px 12px;text-align:left;}
   #contentTable th{padding:12px;text-align:left;}
	</style>
</head>
<body>
    <page:applyDecorator name="agent_op_head">
        <page:param name="current">priceStrategyList</page:param>
    </page:applyDecorator>
	<iframe src="${phpCtx}/pricerule/strategyList/index.html?supplyId=${fns:getUser().company.id}" width="100%" height="640px;" frameborder="0" border="0" style="overflow:hidden"></iframe>
</body>
</html>