    <%@ page contentType="text/html;charset=UTF-8" %>
        <%@ include file="/WEB-INF/views/include/taglib.jsp" %>

        <!doctype html>
        <html>
        <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>机票结算通知单</title>
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
        src="${ctxStatic}/mtour/static/js/print/settlement.js"></script>
        </head>

        <body ng-app="settlement" ng-controller="SettlementController">
        <div class="print-btn">
        <button type="button" class="button-sm" id="btnPrint">打印</button>
        <a class="button-sm" ng-href="{{downloadUrl}}" >下载</a>
        <%--如果是美途用户登录，且配置了可锁定结算单权限 锁定按钮才会显示--%>
        <button type="button" class="button-sm" ng-click="lockSettlementPage()"
        ng-if="data.lockStatus!=1&&companyRoleCode=='0'&&lockBtnShow==1">锁定</button>
        <button type="button" class="button-sm" ng-click="unlockSettlementPage()"
        ng-if="data.lockStatus==1&&companyRoleCode=='0'&&unlockBtnShow==1">解锁</button>
        </div>
        <div id="printDiv">
        <table class="page-print">
        <thead>
        <tr>
        <th width="135"></th>
        <th width="70"></th>
        <th width="70"></th>
        <th width="70"></th>
        <th width="136"></th>
        <th width="70"></th>
        <th width="70"></th>
        <th width="70"></th>
        <th width="80"></th>
        </tr>
        <tr>
        <th class="fr f4 paddr" colspan="9"></th>
        </tr>
        <tr>
        <th class="f1" colspan="9">机票结算通知单</th>
        </tr>
        </thead>
        <tbody>
        <tr>
        <td class="fc f2" width="135">团号:</td>
        <td class="fl paddl f3" colspan="3">{{data.groupNo}}</td>
        <td width="136" rowspan="3" class="fc f2">备注：</td>
        <td colspan="4" rowspan="3" class="fl f3"></td>
        </tr>
        <tr>
        <td class="fc f2">人数:</td>
        <td colspan="3" class="fl f3 paddl">{{data.peopleCount}}</td>
        </tr>
        <tr>
        <td class="fc f2">出票日期:</td>
        <td colspan="3" class="fl paddl f3 ">{{data.invoiceDate}}</td>
        </tr>
        <tr>
        <td class="fc f2">行程:</td>
        <td colspan="3" class="fl paddl f3">{{data.itinerary}}</td>
        <td class="fc f2">行程时间段</td>
        <td colspan="4" class="fl f3">{{data.travelPeriod}}</td>
        </tr>
        <tr>
        <td class="fc f2">航空公司：</td>
        <td class="fc f3" colspan="3">{{data.airlineCompany}}</td>
        <td class="fc f2">供应商名称</td>
        <td colspan="4" class="fc f3">{{data.supplierName}}</td>
        </tr>
        <tr>
        <td class="fc f2">机票操作员</td>
        <td colspan="3" class="fc danhangnormal f3">{{data.ticketName}}</td>
        <td class="fc f2">销售人员</td>
        <td colspan="4" class="fc f3">{{data.orderer}}</td>
        </tr>
        <tr>
        <td colspan="9" class="table-head-bg fc f2" bgcolor="#F5F5F5">收入项</td>
        </tr>
        <tr>
        <td class="fc f2">收款日期</td>
        <td class="fc f2">客户名称</td>
        <td class="fc f2">人数</td>
        <td class="fc danhangnormal f2">定金</td>
        <td class="fc f2">尾款（全款）</td>
        <td class="fc danhangnormal f2">总计</td>
        <td class="fc f2">币种</td>
        <td class="fc danhangnormal f2">汇率</td>
        <td class="fc danhangnormal f2">折合（RMB）</td>
        </tr>
        <tr ng-repeat="income in data.incomes">
        <td class="fc f2">{{income.receiveDate}}</td>
        <td class="fc f3 danhangnormal">{{income.customer}}</td>
        <td class="fc f3">{{income.peopleCount}}</td>
        <td class="fc danhangnormal f3">{{income.deposit|qcCurrency:income.currencyMark}}</td>
        <td class="fc f3">{{income.balancePayment|qcCurrency:income.currencyMark}}</td>
        <td class="fc danhangnormal f3">{{income.totalAmount}}</td>
        <td class="fc f3">{{income.currencyMark}}</td>
        <td class="fc danhangnormal f3">{{income.exchangeRate}}</td>
        <td class="fc danhangnormal f3">{{income.rmb}}</td>
        </tr>

        <tr>
        <td class="fc f2">收入合计</td>
        <td class="fc f3" colspan="5">{{data.inComeSum}}</td>
        <td class="fc f3" colspan="3">{{data.inComeSumRMB}}</td>

        </tr>
        <tr>
        <td colspan="9" class="table-head-bg fc f2" bgcolor="#F5F5F5">成本项</td>
        </tr>
        <tr>
        <td class="fc f2">项目</td>
        <td class="fc f3">大编号</td>
        <td class="fc f3">航空公司</td>
        <td class="fc f3">人数</td>
        <td class="fc f3">单价</td>
        <td class="fc f3">总计</td>
        <td class="fc f3">币种</td>
        <td class="fc f3">汇率</td>
        <td class="fc f3"><span class="fc f2">折合（RMB）</span></td>
        </tr>
        <tr ng-repeat="cost in data.costs">
        <td class="fc danhangnormal f2">{{cost.fundsName}}</td>
        <td class="fc danhangnormal f3">{{cost.invoiceOriginalTypeCode=='0'?cost.PNR:cost.tourOperatorName}}</td>
        <td class="fc danhangnormal f3">{{cost.airlineCompany}}</td>
        <td class="fc danhangnormal f3">{{cost.peopleCount}}</td>
        <td class="fc danhangnormal f3">{{cost.price|qcCurrency:cost.currencyMark}}</td>
        <td class="fc danhangnormal f3">{{cost.totalAmount}}</td>
        <td class="fc danhangnormal f3">{{cost.currencyMark}}</td>
        <td class="fc danhangnormal f3">{{cost.exchangeRate}}</td>
        <td class="fc danhangnormal f3">{{cost.rmb}}</td>
        </tr>
        <tr>
        <td class="fc f2">追加成本</td>
        <td class="fc danhangnormal f3" colspan="5">{{data.additionalCostSum}}</td>
        <td class="fc danhangnormal f3" colspan="3">{{data.additionalCostSumRMB}}</td>
        </tr>
        <tr>
        <td class="fc f2">成本合计</td>
        <td class="fc danhangnormal f3" colspan="5">{{data.costSum}}</td>
        <td class="fc danhangnormal f3" colspan="3">{{data.costSumRMB}}</td>
        </tr>
        <tr ng-if="data.refundSum || data.refundSumRMB">
        <td class="fc f2">退款</td>
        <td class="fc danhangnormal f3" colspan="5">{{data.refundSum}}</td>
        <td class="fc danhangnormal f3" colspan="3">{{data.refundSumRMB}}</td>
        </tr>

        <tr>
        <td class="fc f2">毛利</td>
        <td colspan="3" class="fc danhangnormal f3">{{data.grossProfit}}</td>
        <td class="fc f2">毛利率%</td>
        <td colspan="4" class="fc danhangnormal f3">{{data.grossProfitRate}}</td>
        </tr>
        <tr>
        <td class="fr f2 noborder paddr">&nbsp;</td>
        <td colspan="3" class="fr f3 noborder paddr">&nbsp;</td>
        <td class="fr f2 noborder paddr">&nbsp;</td>
        <td colspan="4" class="fr f3 noborder paddr">&nbsp;</td>
        </tr>
        <tr>
        <td class="fr f2 noborder paddr">制表：</td>
        <td colspan="3" class="noborder" style="text-align: left" title="{{data.lister}}">{{data.lister}}</td>
        <td class="fr f2 noborder paddr">日期：</td>
        <td colspan="4" class="fl f3 noborder paddr">{{data.listerDate}}</td>
        </tr>
        </tbody>
        </table>
        </div>

        </body>

        </html>