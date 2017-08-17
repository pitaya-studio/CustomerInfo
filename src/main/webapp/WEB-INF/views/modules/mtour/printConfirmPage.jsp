<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>打印确认单</title>
    <link rel="stylesheet" type="text/css"
          href="${ctxStatic}/mtour/static/css/common/qc.print.css">

    <script type="text/javascript"
            src="${ctxStatic}/mtour/static/js/common/jquery.js"></script>
    <script type="text/javascript"
            src="${ctxStatic}/mtour/static/js/common/angular.js"></script>
   <script type="text/javascript">
        var mtourApiUrl = '${ctx}/mtour/';
        var mtourStaticUrl = '${ctxStatic}/mtour/static/';
        var mtourHtmlTemplateUrl = '${ctxStatic}/mtour/html/';
        var mtourLoginUrl = '${ctx}/login';
        var mtourUploadFileUrl = '${ctx}/mtour/common/download/';
        var mtourLogoutUrl = '${ctx}/logout';
        var mtourBaseUrl = '${ctx}';
        </script>
    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <script type="text/javascript"
                src="${ctxStatic}/mtour/static/js/common/quauq.base64.js"></script>  
    <script type="text/javascript"
            src="${ctxStatic}/mtour/static/js/common/qc.js"></script>
    <script type="text/javascript"
            src="${ctxStatic}/mtour/static/js/print/print.js"></script>
    <script type="text/javascript"
            src="${ctxStatic}/mtour/static/js/print/confirm.js"></script>
</head>

	<body ng-app="settlement" ng-controller="SettlementController">
    <div class="print-btn">
    	<button type="button" class="button-sm" id="btnPrint">打印</button>
        <a  class="button-sm" ng-href="{{downloadUrl}}" >下载</a>
    </div>
		<div id="printDiv">
    <table class="page-print title ">
        <thead>
        <tr>
            <th width="135"></th>
            <th width="250"></th>
            <th width="136"></th>
            <th width="250"></th>
        </tr>
        <tr>
            <th class="fr f4 paddr" colspan="4"></th>
        </tr>
        <tr>
            <th class="fr text_title" colspan="4">{{data.companyName}}</th>
        </tr>
        <tr>
            <th class="fr address" colspan="4">{{data.companyName_EN}}</th>
        </tr>
        <tr>
            <th class="fr address" colspan="4">{{data.addressee}} TEL:{{data.tel}} FAX:{{data.tax}}</th>
        </tr>
        <tr>
            <th class="fr address" colspan="4">{{data.addressee_EN}}
            </th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="fc f2" width="135">收件人:</td>
            <td class="fl danhangnormal paddl f3">{{data.channelName}}</td>
            <td width="136" class="fc f2">发件人：</td>
            <td colspan="4" class="fl danhangnormal f3">{{data.orderer}}</td>
        </tr>
        <tr>
            <td class="fc f2">电话:</td>
            <td class="fl f3 paddl">{{data.channelTel}}</td>
            <td width="136" class="fc f2">电话：</td>
            <td class="fl f3">{{data.ordererTel}}</td>
        </tr>
        <tr>
            <td class="fc f2">页数:</td>
            <td class="fl paddl  f3 ">&nbsp;</td>
            <td width="136" class="fc f2">日期：</td>
            <td class="fl f3">{{data.orderDate}}</td>
        </tr>
        <tr>
            <td colspan="4" class="fc f2">收款通知书</td>
        </tr>
        <tr>
            <td colspan="4" class="fl paddl f2">你好！承蒙大力支持，团队具体费用通知如下：</td>
        </tr>
        <tr>
            <td class="fc f2">团号：</td>
            <td class="fl f3 paddl">{{data.groupNo}}</td>
            <td class="fc f2">路线：</td>
            <td class="fc f3">{{data.routerInfo}}</td>
        </tr>
        <tr>
            <td class="fc f2">名称</td>
            <td colspan="3" class="fc f3">明细</td>
        </tr>
        <tr>
            <td rowspan="2" class="fl text-vertical-middle duohangnormal paddl f2">
            {{ data.productName}}
            </td>
            <td colspan="3" class="fl paddl f3" ng-if="data.flights.length">
                烦请参考价格及航班见下：
                <p ng-repeat="flight in  data.flights">
                    {{flight.flightNo}} {{flight.departureAirport}} {{flight.arrivalAirport}} {{flight.departureTime}} {{flight.arrivalTime}}
                    <br/>
                    {{flight.memo}}
                </p>
            </td>
        </tr>
        <tr>
            <td colspan="3" class="fl paddl f3">
                备注:
                <p class="memo">{{data.memo}}</p>
            </td>
        </tr>
        <tr>
            <td class="fc f2">总计：</td>
            <td colspan="2" class="fl danhangnormal f3">{{data.total}}</td>
            <td class="fc f3">{{data.totalResult}}</td>
        </tr>
        <tr>
            <td colspan="4" class="fl paddl f2">请将团款汇入以下账号或支票支付：</td>
        </tr>
        <tr>
            <td colspan="2" class="fl paddl  danhangnormal f3">
                <p>开户名：{{data.overseasAccount.accountName}}</p>

                <p>Routing：{{data.overseasAccount.routing}}</p>

                <p>账号：{{data.overseasAccount.account}}</p>

                <p>Swift Number：{{data.overseasAccount.swiftNumber}}</p>

                <p>开户行：{{data.overseasAccount.accountBank}}</p>

                <p>Bank address：{{data.overseasAccount.accountBankAddress}}</p>

                <p>Phone number：{{data.overseasAccount.accountPhone}}</p>
            </td>
            <td colspan="2" class="fl paddl danhangnormal f3">
                <p>公司名称：{{data.account.accountName}}</p>

                <p>开户行：{{data.account.accountBank}}</p>

                <p>账号：{{data.account.account}}</p>
            </td>
        </tr>
        <tr>
            <td colspan="2" class="fc f2">销售方签章</td>
            <td colspan="2" class="fc f2">客户方签章</td>
        </tr>
        <tr>
            <td colspan="2" class="fl paddl danhangnormal  f3">
                <p>公司名称：{{data.companyName}}</p>

                <p>签字：</p>

                <p>盖章：</p>
            </td>
            <td colspan="2" class="fl paddl danhangnormal  f3">
                <p>公司名称：{{data.channelName}}</p>

                <p>签字：</p>

                <p>盖章：</p>
            </td>
        </tr>
        </tbody>
    </table>
</div>
	</body>

</html>