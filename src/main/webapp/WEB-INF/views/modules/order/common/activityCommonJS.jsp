<%@ page contentType="text/html;charset=UTF-8" %>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/single/activityListForOrder.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript">
	function selectQuery(){
		$("#activityCreate" ).comboboxInquiry();
	}
	$(function(){
    	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
 		$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
 		$("#activityCreate" ).comboboxInquiry();
 		//renderSelects(selectQuery());
	});
</script>
<style type='text/css'>
	.ui-front { z-index: 2100; }
	.ico-remarks-td {
	height: 16px;
	width: 31px;
	background-image: url("${ctxStatic}/images/ico-remarks-td.png");
	background-repeat: no-repeat;
	position: absolute;
	top: -1px;
	left: 0px;
	cursor: pointer;
}
</style>