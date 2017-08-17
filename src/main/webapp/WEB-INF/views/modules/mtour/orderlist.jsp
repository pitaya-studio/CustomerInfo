    <%@ page contentType="text/html;charset=UTF-8" %>
        <%@ include file="/WEB-INF/views/include/taglib.jsp" %>
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8">
        <title>订单列表</title>
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
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/order/orderConfirm.js"></script>
        <style type="text/css"></style>
        </head>
        <body ng-app="orderList">
        <div class="header">
        <!--S面包屑-->
        <div class="breadcrumbclip">
        <i class="fa fa-map-marker"></i>
        <span><a href="javascript:void(0);">订单 &gt;</a></span> <span><a
        href="javascript:void(0);">订单列表
        </a></span>
        </div>
        <!--E面包屑-->

        <!--S登录信息-->
        <div>
        <ul user-info show-Create-Order="true"></ul>
        </div>
        <!--E登录信息-->
        </div>
        <div class="main">
        <div main-Left current-menu-url="/mtour/order/list"></div>
        <div id="rightPart" class="main-right ">
        <div class="content main_frame_father">
        <div class="main_frame " ng-controller="OrderListController">
        <div class="main-filter-search-container">
        <div main-filter item-text="orderStatusName" items="orderStatusList"
        ng-model="selectedOrderStatus"></div>

        <div class="main-search-container">
        <ul>
        <li>
        <div main-search ng-model="searchParam" default-search-type="'1'"></div>
        </li>
        <li>
        <div ng-if="searchCount" main-count ng-model="searchCount"
        current-Code="'1'"></div>
        </li>
        </ul>
        </div>

        </div>
        <!--S高级搜索-->
        <div class="search-container">
        <div class="search-condition">
        <span class="fa fa-filter search-condition-icon"></span>
        <ul>
        <li>
        <div search-condition-channel class="search-condition-item"
        ng-model="filterParam.selectedChannels"></div>
        <div search-condition-orderer class="search-condition-item"
        ng-model="filterParam.selectedOrderers"></div>
        <!--<div search-condition-order-status--><!--class="search-condition-item"-->
        <!--ng-model="filterParam.selectedOrderStatus">--><!--</div>-->
        <div search-condition-order-receive-status class="search-condition-item"
        ng-model="filterParam.selectedReceiveOrderStatus"></div>
        <div qc-Date-Period class="search-condition-item" ng-model="selectedCreateDate">下单日期<span>▼</span>
        </div>
        </li>
        </ul>
        </div>
        <div ng-if="isShowFilters()" search-filter-list class="search-filter-list">
        <ul>
        <li search-filter ng-if="filterParam.selectedChannels.length" label-text="渠道"
        item-text="channelName" multiple="multiple"
        ng-model="filterParam.selectedChannels"></li>
        <li search-filter ng-if="filterParam.selectedOrderers.length" label-text="下单人"
        item-text="userName" multiple="multiple"
        ng-model="filterParam.selectedOrderers"></li>

        <li search-filter ng-if="filterParam.selectedReceiveOrderStatus.length"
        label-text="收款状态"
        item-text="receiveStatusName" multiple="multiple"
        ng-model="filterParam.selectedReceiveOrderStatus"></li>
        <li search-filter-date-period ng-if="filterParam.selectedCreateDates.length" label-text="下单日期"
        ng-model="filterParam.selectedCreateDates"></li>
        </ul>
        <button class="butn butn-warning f-l" ng-click="filterOrder()">确定筛选</button>
        <i search-filter-clear page-flag="0">[ 清空 ]</i>
        </div>
        </div>
        <!--E高级搜索--><!--S列表排序-->
        <div pagination-sort></div>
        <!--E列表排序--><!--S列表-->
        <div class="qc-table-container qc-scroll" bottom-gap="5">
        <table id="contentTable" class=" table-list-lg" qc-sub-table-container
        qc-table-fixed-header>
        <thead>
        <tr>
        <th class="table-th-default-width"><input ng-show="companyRoleCode==0" ng-click="allBtn($event,results)"  style="position:relative;top:2px;margin-right:5px;" type="checkbox"/>序号</th>
        <th class="table-th-sm-width">下单日期</th>
        <th class="table-th-sm-width">团号</th>
        <th class="table-th-sm-width">产品名称</th>
        <th class="table-th-default-width">渠道名称</th>
        <th class="table-th-default-width text-right">定金</th>
        <th class="table-th-default-width text-right">全款</th>
        <th class="table-th-default-width text-right">尾款</th>
        <th class="table-th-default-width text-right">已收金额<br>到账金额
        </th>
        <th class="table-th-default-width">下单人</th>
        <th class="table-th-default-width">订单状态</th>
        <th class="table-th-default-width">收款状态</th>
        <th style="width: 150px">操作</th>
        <th class="empty"></th>
        </tr>
        </thead>
        <tbody ng-class="{'odd':$odd,'even':$even,'active':order.qcSubTableIds.indexOf(spreadSubTableId)>=0}"
        ng-repeat-start="order in results"
        ng-init="initOrderSpreadSubTableIs(order)">
        <tr ng-cloak="">
        <td><input  ng-model="order.checkFlag" ng-show="companyRoleCode==0"  ng-click="aloneBtn($event,order)" ng-checked="{{order.checkFlag}}"  style="position:relative;top:2px;margin-right:5px;" type="checkbox"/>{{(pageInfo.currentIndex-1)*pageInfo.rowCount+$index+1}}</td>
        <td>
        <span class="table-th-sm-width ellipsis" ng-bind="order.orderDateTime" title="{{order.orderDateTime}}"></span>
        </td>
        <td>
        <span class="table-th-sm-width ellipsis" ng-bind="order.groupNo" title="{{order.groupNo}}"></span>

        </td>
        <td>
        <span class="table-th-sm-width ellipsis" ng-bind="order.productName" title="{{order.productName}}"></span>
        </td>
        <td><span class="table-th-default-width ellipsis" ng-bind="order.channelName"
        title="{{order.channelName}}"></span></td>
        <td class="text-right">
        <p ng-repeat="amount in order.deposit">
        {{amount.amount|qcCurrency:amount.currencyCode}}
        </p>
        </td>
        <td class="text-right">
        <p ng-repeat="amount in order.fullPayment">
        {{amount.amount|qcCurrency:amount.currencyCode}}
        </p>
        </td>
        <td class="text-right">
        <p ng-repeat="amount in order.balancePayment">
        {{amount.amount|qcCurrency:amount.currencyCode}}
        </p>
        </td>
        <td class="text-right">
        <p ng-repeat="amount in order.receivedAmount">
        {{amount.amount|qcCurrency:amount.currencyCode}}
        </p>
        <p ng-repeat="amount in order.arrivedAmount">
        {{amount.amount|qcCurrency:amount.currencyCode}}
        </p>
        </td>
        <td><span class="table-th-default-width ellipsis" ng-bind="order.orderer" title="{{order.orderer}}"></span></td>
        <td ng-bind="order.orderStatus">{{}}</td>
        <td ng-bind="order.receiveStatus"></td>
        <td>
        <div class="qc-table-operator" qc-table-operator-option="defaultOptions">
        <div row-operator order="order"
        spread-Sub-Table-id="spreadSubTableId"></div>
        </div>

        </td>
        <td> </td>
        </tr>
        </tbody>
        <!--展开PNR-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'pnrList'+order.orderUuid}}">
        <div pnr-list="" order-uuid="order.orderUuid" settlement-lock-status="order.lockStatus"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--借款-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'loan'+order.orderUuid}}">
        <div loan="" order-loan="order.orderLoan" order-uuid="order.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--借款记录-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'loanList'+order.orderUuid}}">
        <div loan-list="" order-uuid="order.orderUuid" settlement-lock-status="order.lockStatus"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--退款-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'refund'+order.orderUuid}}">
        <div refund="" order-refund="order.orderRefund" order-uuid="order.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--退款记录-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'refundList'+order.orderUuid}}">
        <div refund-list="" order-uuid="order.orderUuid" settlement-lock-status="order.lockStatus"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--订单收款记录-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'orderReceiveList'+order.orderUuid}}">
        <div order-Receive-List="" order-uuid="order.orderUuid" settlement-lock-status="order.lockStatus"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--成本记录-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="14">
        <div qc-sub-table qc-sub-table-id="{{'orderCostList'+order.orderUuid}}">
        <div order-Cost-List="" order-uuid="order.orderUuid" settlement-lock-status="order.lockStatus"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--追加成本-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'additionalCost'+order.orderUuid}}">
        <div additional-cost=""
        order-additional-Cost="order.orderAdditionalCost"
        order-uuid="order.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--追加成本记录-->
        <tbody class="qc-sub-table-container" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table
        qc-sub-table-id="{{'additionalCostList'+order.orderUuid}}">
        <div additional-cost-list="" order-uuid="order.orderUuid" settlement-lock-status="order.lockStatus"></div>
        </div>
        </td>
        </tr>
        </tbody>

        <!--其他收入记录-->
        <tbody class="qc-sub-table-container" ng-repeat-end="" style="display: none">
        <tr>
        <td colspan="12">
        <div qc-sub-table qc-sub-table-id="{{'otherRevenueList'+order.orderUuid}}">
        <div other-Revenue-List="" order-uuid="order.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>
        </table>
        <div class="page" ng-if="pageInfo.totalRowCount">
        <div pagination page-info="pageInfo"></div>
        </div>

        </div>

        </div>

        <!--E列表--><!--S分页-->

        <!--E分页-->
        </div>

        </div>
        </div>
        </div>
        </body>
        </html>