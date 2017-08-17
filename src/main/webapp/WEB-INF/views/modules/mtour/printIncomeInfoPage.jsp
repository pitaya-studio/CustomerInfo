<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>收入单</title>
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
            src="${ctxStatic}/mtour/static/js/print/income.js"></script>
</head>

	<body ng-app="income" ng-controller="IncomeController">
    <div class="print-btn">
    	<button type="button" class="button-sm" id="btnPrint">打印</button>
           <a  class="button-sm" ng-href="{{downloadUrl}}" >下载</a>
    </div>
			<div id="printDiv">
			<table class="page-print">
				<thead>
                	<tr>
                    <th width="135"></th>
                    <th width="80"></th>
                    <th width="80"></th>
                    <th width="81"></th>
                    <th width="136"></th>
                    <th width="80"></th>
                    <th width="80"></th>
                    <th width="86"></th>
                    </tr>
					<tr>
						<th class="fr f4 paddr" colspan="8"></th>
					</tr>
					<tr>
						<th class="f1" colspan="8">收入单</th>
					</tr>
					<tr>
						<th class="fr paddr f3" colspan="4">{{data.applicantDate}}</th>
						<th class="fr paddr f3">编号：</th>
						<th colspan="3" class="fl paddr f3">{{data.printNo}}</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td class="fc f2" width="135">团号:</td>
						<td class="fl paddl f3" colspan="3" ng-bind="data.groupNo"></td>
						<td width="136" class="fc f2">PNR号:</td>
						<td colspan="3" class="fl f3" ng-bind="invoiceOriginalTypeCode=='0'?data.PNR:data.tourOperatorName"></td>
					</tr>
					<tr>
						<td class="fc f2">兹由:</td>
						<td colspan="3" class="fl f3 paddl">{{data.fromInfo}}</td>
						<td width="136" class="fc f2">交来:</td>
						<td colspan="3" class="fl f3 paddl">{{data.toInfo}}</td>
					</tr>
					<tr>
					    <td class="fc f2">计人民币（大写）:</td>
					    <td colspan="3" class="fl paddl danhangnormal f3 " ng-bind="data.totalRMB_CN"></td>
					   	<td width="136" class="fc f2">(小写):</td>
					    <td colspan="3" class="fl f3 paddl" ng-bind="data.totalRMB"></td>
				    </tr>
					<tr>
					    <td class="fc f2">外币（大写）:</td>
					    <td colspan="3" class="fl  danhangnormal  paddl f3">{{data.totalOther_CN}}</td>
					    <td width="136" class="fc f2 ">(小写):</td>
					    <td colspan="3" class="fl f3 paddl">{{data.totalOther}}</td>
				    </tr>
					<tr>
						<td class="fc f2">开具发票：</td>
						<td class="fc f3" colspan="3">有&nbsp; &nbsp;&nbsp; &nbsp;&nbsp; 无</td>
						<td class="fc f2">制单人：</td>
						<td colspan="3" class="fl f3 danhangnormal paddl">{{data.incomeAloner}}</td>
					</tr>
					<tr>
						<td class="fc f2">开具日期：</td>
						<td colspan="3" class="fc f3">&nbsp;</td>
						<td class="fc f2">审核人：</td>
						<td colspan="3" class="fl f3 danhangnormal paddl">{{data.approver}}</td>
					</tr>
					<tr>
						<td class="fr f3 noborder paddr" colspan="8">&nbsp;</td>
					</tr>
				</tbody>
			</table>
		</div>

	</body>

</html>