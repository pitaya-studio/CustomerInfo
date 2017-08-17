<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>订单统计信息</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
	function exportProduct(){
		window.location.href="${ctx}/statistics/product/controller/psc/exportAllProduct";
	}
	function exportProductCount(){
		var beginDate = document.getElementById("orderTimeBegin").value;
		var endDate = document.getElementById("orderTimeEnd").value;
		var url = "${ctx}/statistics/product/controller/psc/exportProductNumPerOffice?"+"beginDate="+beginDate+"&endDate="+endDate;
		window.location.href=url;
	}
</script>
</head>
<body>
<div class="activitylist_bodyer_right_team_co_bgcolor">
	<form:form id="searchForm" action="${ctx}/statistics/order/controller/odsc/exportAllOrderDetail" method="post" >
         <div class="activitylist_bodyer_right_team_co2" style="width:23%">
			<label class="activitylist_team_co3_text">下单时间：</label>
			<input id="orderTimeBegin" name="beginDate" class="inputTxt dateinput" value="${beginDate }" 
						readonly onClick="WdatePicker()"/> 至 
			<input id="orderTimeEnd" name="endDate" value="${endDate }" readonly onClick="WdatePicker()" 
						class="inputTxt dateinput"/>
		</div>
		<div class="form_submit">
             <input class="btn btn-primary ydbz_x" type="submit" value="导出Excel"/>
             <input class="btn btn-primary ydbz_x" type="button" value="导出产品信息" onclick="exportProduct();"/>
			 <input class="btn btn-primary ydbz_x" type="button" value="导出产品数量信息" onclick="exportProductCount()"/>
        </div>
    </form:form>
    
</div>
</body>
</html>