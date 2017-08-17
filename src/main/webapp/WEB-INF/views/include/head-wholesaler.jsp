<%@ page contentType="text/html;charset=UTF-8" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="Cache-Control" content="no-store" /><meta http-equiv="Pragma" content="no-cache" /><meta http-equiv="Expires" content="0" />
<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value:'default'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/css/jh-style.css?ver=1" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/css/jh-style.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet" />
<%--added for UG_V2 by tlw start--%>
<link href="${ctxStatic}/css/UIrebuild_V1.css" type="text/css" rel="stylesheet"/>
<%--added for UG_V2 by tlw end--%>
<script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js" type="text/javascript"></script>
<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/trekiz.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/vendor.service_mode1.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic }/json/json2.js" type="text/javascript" ></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript" ></script>
<script src="${ctxStatic }/common/common.js" type="text/javascript" ></script>
<script src="${ctxStatic }/common/systemAsyncLoadData.js" type="text/javascript" ></script>
<shiro:hasPermission name="travel:service:branch">
	<script src="${ctxStatic }/common/branchOfTravelService.js" type="text/javascript" ></script>
</shiro:hasPermission>
<script src="${ctxStatic }/common/systemNotice.js" type="text/javascript" ></script>
<script src="${ctxStatic }/js/socket/sockjs-0.3.min.js" type="text/javascript" ></script>
<script src="${ctxStatic }/js/socket/stomp.js" type="text/javascript" ></script>
<%--added for UG_V2 by tlw start--%>
<script src="${ctxStatic }/js/uiRebuild.js" type="text/javascript" ></script>
<%--added for UG_V2 by tlw end--%>



