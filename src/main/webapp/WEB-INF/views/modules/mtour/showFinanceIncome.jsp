<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>财务收入账单</title>
    <link rel="stylesheet" type="text/css"
          href="${ctxStatic}/mtour/static/css/common/qc.print.css">
	<link rel="stylesheet" type="text/css"
		  href="${ctxStatic}/mtour/static/css/common/quauq.css">
    <script type="text/javascript"
            src="${ctxStatic}/mtour/static/js/common/jquery.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
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
	<script type="text/javascript"
			src="${ctxStatic}/mtour/static/js/print/financeIncome.js"></script>
	<script type="text/javascript"
			src="${ctxStatic}/mtour/static/js/finance/finance.js"></script>
	<style  rel="stylesheet" type="text/css">
		.date_Input{
			width: 90px;
			height: 24px;
			border-radius: 3px;
			border: 1px solid #d9d9d9;
			padding: 0 5px;
			color: #666;
			display: inline-block;
		}
		.su-btn{
			display: inline-block;
			height: 26px;
			padding: 0 10px;
			cursor: pointer;
			border: 1px solid #999;
			/* color: white; */
			border-radius: 3px;
		}
		.su-btn:hover{
			background-color: #f2f2f2;
		}
	</style>
</head>

	<body ng-app="financeIncome" ng-controller="financeIncomeController">
    <div class="print-btn">
		 <div  style="display:inline-block;">
            <input id="groupOpenDate"  class=" date_Input date  inputTxt dateinput homeInput" name="groupOpenDateBegin" value="{{startTime}}" onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" > —
            <input id="groupCloseDate" class=" date_Input  date inputTxt dateinput homeInput" name="groupOpenDateEnd" value="{{endTime}}" onclick="WdatePicker()" >
            <input type="button" class="su-btn"  show-Finance-Income-Button ng-show="incomeType==1"  ng-click="requestAgeIncome(1)"  value="确定">
            <input type="button" class="su-btn"  show-Finance-Income-Button ng-show="incomeType==2"  ng-click="requestAgeIncome(2)"  value="确定">
        </div>
		<%--<p>美图国旅</p><p>2016年3月</p><p>营业收入统计表(当月已结账)</p>--%>
    	<button type="button" class="button-sm" id="btnPrint">打印</button>
           <a  class="button-sm" ng-href="{{downloadUrl}}" >下载</a>
    </div>
			<div id="printDiv">
			<table class="page-print"  style="width:auto;">
				<caption  style="font-size:22px;font-weight: bold;">美途国旅{{startTime}}至{{endTime}}{{incomeType==1?"营业收入统计表":"机票定金统计表"}}</caption>
				<%--<caption  align="top" style="font-size:22px;font-weight: bold;" ng-show="incomeType=='1'">美途国旅2016年3月营业收入统计表（当月已结账）</caption>--%>

				<caption  style="text-align:right;padding-right:100px;">单位:元</caption>
				<thead>
                	<tr>
                    <td class="fc f2" width="135" >销售</td>
                    <td class="fc f2" width="80">团号</td>
                    <td class="fc f2" width="80" ng-show="incomeType==1">营业收入</td>
                    <td class="fc f2" width="80" ng-show="incomeType==2">机票全款</td>
                    <td class="fc f2" width="81" ng-show="incomeType==1">营业成本</td>
					<td class="fc f2" width="80" ng-show="incomeType==2">定金</td>
                    <td class="fc f2" width="136" ng-show="incomeType==1">毛利</td>
					<td class="fc f2" width="80" ng-show="incomeType==2">未收定金</td>
                    <td class="fc f2" width="80" ng-show="incomeType==1">应收账款</td>
					<td class="fc f2" width="80" ng-show="incomeType==2">未收全款</td>
                    </tr>

				</thead>
				<tbody  ng-repeat="item in items">
				<tr ng-repeat="order in item.orders track by $index">
					<td ng-if="$index ==0"  rowspan="{{item.orders.length}}"  style="vertical-align: middle;padding: 0 20px;">
						<span ng-bind="order.salerName"></span>
					</td>
					<td ng-show="incomeType==1">
						<span ng-bind="order.groupCode"></span>
					</td>
							<td ng-show="incomeType==2">
								<span ng-bind="order.orderNum"></span>
							</td>
					<td>
						￥<span ng-bind="order.totalMoney"></span>
					</td>
					<td ng-show="incomeType==1">
						￥<span ng-bind="order.cost"></span>
					</td>
							<td ng-show="incomeType==2">
								￥<span ng-bind="order.frontMoney"></span>
							</td>
					<td ng-show="incomeType==1">
						￥<span ng-bind="order.profit"></span>
					</td>
							<td ng-show="incomeType==2">
								￥<span ng-bind="order.notAccountFrontMoney"></span>
							</td>
					<td ng-show="incomeType==1">
						￥<span ng-bind="order.receivable"></span>
					</td>
							<td ng-show="incomeType==2">
								￥<span ng-bind="order.notAccountedTotalMoney"></span>
							</td>
				</tr>
				</tr>
				</tbody>
			</table>
		</div>

	</body>

</html>