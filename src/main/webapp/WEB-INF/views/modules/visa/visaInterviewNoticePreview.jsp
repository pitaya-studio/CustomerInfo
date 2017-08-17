<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-签证-面签通知预览</title>

<link rel="stylesheet" href="${ctxStatic }/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic }/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor2.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/i18n/jquery-ui-i18n.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.disabled.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.date.format.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/jquery.file.filter.js" type="text/javascript"></script>
<script type="text/javascript">
function CloseWebPage() { 

		window.close();   
}  
$(document).keyup(function(e){
	var key =  e.which;
	if(key == 27){

		 CloseWebPage();
	}
});	
</script>
</head>
<body>
	<input type="button" onclick="history.back()" class="btn btn-primary" value="esc退出">	
	<div class="ydbz_tit">面签通知预览</div>
	<div class="seach25 seach100">
		<p>约签人：</p>
		<c:forEach items="${myTravelers }" var="item">
			${item[1] }&nbsp;
		</c:forEach>
	</div>
	<div class="seach25 seach100">
		<p>国家：</p>${interview.country }
	</div>
	<div class="seach25 seach100">
		<p>领区：</p>  <!--  ${fns:getDictLabel(interview.area,'from_area','')} -->
        ${interview.area}
	</div>
	<div class="seach25 seach100">
		<p>预约地点：</p>${interview.address }
	</div>
	<div class="seach25 seach100">
		<p>约签时间：</p><fmt:formatDate value="${interview.interviewTime }" pattern="yyyy-MM-dd HH:mm"/>
	</div>
	<div class="seach25 seach100">
		<p>说明会时间：</p><fmt:formatDate value="${interview.explainationTime }" pattern="yyyy-MM-dd HH:mm"/>
	</div>
	<div class="seach25 seach100">
		<p>联系人：</p>${interview.contactMan }
	</div>
	<div class="seach25 seach100">
		<p>联系方式：</p>${interview.contactWay }
	</div>
<script type="text/javascript">
</script>
</body>
</html>