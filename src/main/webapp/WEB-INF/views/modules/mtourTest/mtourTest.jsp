<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>报价器</title>
	<meta name="decorator" content="wholesaler"/>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
	<style type="text/css">
		.activitylist_bodyer_right_team_co1{
			min-width:270px;
		}
        .activitylist_team_co3_text_80.title-md{
            width: 100px;
        }
        .preferential-td{
            font-size: 16px;;
        }
    </style>
    <style type="text/css">
            .activitylist_bodyer_right_team_co1{
                min-width:270px;
            }
            .activitylist_team_co3_text_80.title-md {
                width: 100px;
            }

            .preferential-td {
                font-size: 16px;;
            }
            .preferential-empty{
                margin: 100px auto;
                line-height: 40px;
                font-size: 24px;
                text-align: center;
            }
    </style>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor3.js"></script>
	<script src="${ctxStatic}/jquery.zclip/jquery.zclip.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript">
	$(function(){
		$("#quote1").on("click", function () {
		 	var param = {};
		 	param.tourOperatorTypeUuid="test";
		 	
		 	$.post("${ctx}/mtour/common/getSupplierList4Type",{"requestType":"test","param":JSON.stringify(param)},function(data){
		 		var obj = JSON.parse(data);
		 		alert(obj.data[0].tourOperatorTypeName);
		 	});
		 });
	});
		
	</script>
</head>
<body>
	<input id="quote1" type="button" value="测试" class="btn btn-primary jbox-width100" />
	<a href="${ctx}/mtourfinance/downloadIncomeSheet" target="_blank">下载测试</a>
</body>
</html>
