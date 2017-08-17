<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>${title }</title>
        <link rel="stylesheet" type="text/css"
              href="${ctxStatic}/mtour/static/css/common/quauq.css">
        <link rel="stylesheet" type="text/css"
              href="${ctxStatic}/mtour/static/css/common/jquery-ui.css">
        <link rel="stylesheet" type="text/css"
              href="${ctxStatic}/mtour/static/css/common/font-awesome.css">
        <link rel="stylesheet" type="text/css"
              href="${ctxStatic}/mtour/static/css/component/qc.uploader.css">
        <style>
            html, body {
                overflow: hidden;
            }
        </style>
        
        <script type="text/javascript">
            var mtourApiUrl = '${ctx}/mtour/';
            var mtourStaticUrl = '${ctxStatic}/mtour/static/';
            var mtourHtmlTemplateUrl = '${ctxStatic}/mtour/html/';
            var mtourLoginUrl = '${ctx}/login';
            var mtourUploadFileUrl = '${ctx}/mtour/common/download/';
            var mtourLogoutUrl = '${ctx}/logout';
            var mtourBaseUrl = '${ctx}';
        </script>
        
       
        <!--[if lte IE 8]>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/respond.src.js"></script>
        <![endif]-->
        <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/jquery.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/jquery-ui.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/jquery.nicescroll.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/angular.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/component/webuploader.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/quauq.base64.js"></script>       
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/qc.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/component/qc.uploader.js"></script>

        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/mtour.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderList.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderReg.js"></script>
    
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderReceive.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/otherRevenue.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/additionalCost.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/refund.js"></script>

        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/loan.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderCost.js"></script>

        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderReceiveList.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/otherRevenueList.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderCostList.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/pnrList.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/addInvoiceOriginalGroup.js"></script>
        <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/order/orderCancel.js"></script>
        <style type="text/css"></style>
    </head>
    <body ng-app="orderList">
        <div class="header">
            <!--S面包屑-->
            <div class="breadcrumbclip">
                <i class="fa fa-map-marker"></i>
                <span><a href="javascript:void(0);">首页</a></span> 
            </a></span>
            </div>
            <!--E面包屑-->

            <!--S登录信息-->
            <div>
            	<ul user-info></ul>
            </div>
            <!--E登录信息-->
        </div>
        <div class="main">
            <div main-Left current-menu-url="/mtour/common/mtourIndex"></div>
            <div id="rightPart" class="main-right  ">
                
				<div class="content main_frame_father">
					<img src="${ctxStatic}/mtour/static/img/common/background/${images}">
				</div>
            </div>
        </div>
        </div>
    </body>
</html>