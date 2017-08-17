    <%@ page contentType="text/html;charset=UTF-8" %>
        <%@ include file="/WEB-INF/views/include/taglib.jsp" %>
        <!DOCTYPE html>
        <html>

        <head>
        <meta charset="UTF-8">
        <title>收款列表</title>
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/mtour/static/css/common/quauq.css">
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/mtour/static/css/common/jquery-ui.css">
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/mtour/static/css/common/font-awesome.css">
        <link rel="stylesheet" type="text/css" href="${ctxStatic}/mtour/static/css/component/qc.uploader.css">
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
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/jquery.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/jquery-ui.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/jquery.nicescroll.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/angular.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/component/webuploader.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/quauq.base64.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/qc.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/component/qc.uploader.js"></script>

        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/mtour.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/finance/finance.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/finance/financeReceiveList.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/order/orderList.js"></script>
        <script type="text/javascript" src="${ctxStatic}/mtour/static/js/order/orderReg.js"></script>
        <style type="text/css"></style>
        </head>

        <body ng-app="financeReceiveList">
        <div class="header">
        <!--S面包屑-->
        <div class="breadcrumbclip">
        <i class="fa fa-map-marker"></i>
        <span><a href="javascript:void(0);">财务 &gt;</a></span> <span><a
        href="javascript:void(0);">收款列表
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
        <div main-Left current-menu-url="/mtour/mtourfinance/financeReceive"></div>
        <div id="rightPart" class="main-right ">
        <div class="content main_frame_father">
        <div class="main_frame " ng-controller="financeReceiveController">
        <div class="main-filter-search-container">
        <%--<div main-filter item-text="receiveStatusName" items="receiveStatusList" ng-model="selectedReceiveStatus"></div>--%>
        <div main-filter item-text="name" items="listTypes" ng-model="selectedListType"></div>
        <div class="main-search-container">
        <ul>
        <li>
        <div main-search ng-model="searchParam" default-search-type="'1'" page="financeReceive"></div>
        </li>
        <li>
        <div ng-if="searchCount" main-count ng-model="searchCount" current-Code="'3'"></div>
        </li>
        </ul>
        </div>

        </div>
        <!--S高级搜索-->
        <div class="search-container" ng-show="selectedListType.code=='1'">
        <div class="search-condition">
        <span class="fa fa-filter search-condition-icon"></span>
        <ul>
        <li>
        <div search-Condition-Receive-Type class="search-condition-item"
        ng-model="filterParam.selectedReceiveTypes"></div>

        <%--122收款确认--%>
        <div search-condition-receive-status class="search-condition-item"
        ng-model="filterParam.selectedReceiveStatus"></div>

        <div search-Condition-Tour-Operator-Or-Channel-receive
        class="search-condition-item" ng-model="filterParam.selectedTourOperatorOrChannel">
        </div>
        <div search-condition-channel
        ng-if="filterParam.selectedTourOperatorOrChannel.tourOperatorChannelCategoryCode=='2'"
        class="search-condition-item" ng-model="filterParam.selectedChannels"></div>

        <div search-condition-tour-operator
        ng-if="filterParam.selectedTourOperatorOrChannel.tourOperatorChannelCategoryCode=='1'"
        class="search-condition-item" ng-model="filterParam.selectedTourOperators"></div>

        <div qc-Date-Period class="search-condition-item" ng-model="selectedDepartureDate">出团日期<span>▼</span></div>

        <div search-Condition-Amount-period class="search-condition-item" label-text="已收金额"
        ng-model="selectedReceivedAmount"></div>

        <div qc-Date-Period class="search-condition-item" ng-model="selectedReceiveDate">收款日期<span>▼</span></div>

        <div qc-Date-Period class="search-condition-item" ng-model="selectedArrivalBankDate">到账日期<span>▼</span></div>

        <div search-Condition-text class="search-condition-item" label-text="付款单位"
        ng-model="selectedReceiveCompany"></div>

        <div search-condition-Receiver class="search-condition-item" ng-model="filterParam.selectedReceivers"></div>
        </li>
        </ul>
        </div>
        <div ng-if="isShowFilters()" search-filter-list class="search-filter-list">
        <ul>

        <li search-filter ng-if="filterParam.selectedReceiveTypes.length" label-text="收款类别" item-text="receiveTypeName"
        multiple="multiple" ng-model="filterParam.selectedReceiveTypes"></li>

        <li search-filter ng-if="filterParam.selectedReceiveStatus.length" label-text="收款确认"
        item-text="receiveStatusName"
        multiple="multiple" ng-model="filterParam.selectedReceiveStatus"></li>

        <li search-filter ng-if="filterParam.selectedChannels.length" label-text="渠道" item-text="channelName"
        multiple="multiple" ng-model="filterParam.selectedChannels"></li>
        <li search-filter ng-if="filterParam.selectedTourOperators.length" label-text="地接社" item-text="tourOperatorName"
        multiple="multiple" ng-model="filterParam.selectedTourOperators"></li>

        <li search-filter-date-period ng-if="filterParam.selectedDepartureDates.length" label-text="出团日期"
        multiple="multiple" ng-model="filterParam.selectedDepartureDates"></li>

        <li search-filter-amount-period ng-if="filterParam.selectedReceivedAmounts.length" label-text="已收金额"
        multiple="multiple" ng-model="filterParam.selectedReceivedAmounts"></li>

        <li search-filter-date-period ng-if="filterParam.selectedReceiveDates.length" label-text="收款日期"
        multiple="multiple" ng-model="filterParam.selectedReceiveDates"></li>
        <li search-filter-date-period ng-if="filterParam.selectedArrivalBankDates.length" label-text="到账日期"
        multiple="multiple" ng-model="filterParam.selectedArrivalBankDates"></li>

        <li search-filter-text ng-if="filterParam.selectedReceiveCompanys.length" label-text="收款单位" multiple="multiple"
        ng-model="filterParam.selectedReceiveCompanys"></li>

        <li search-filter ng-if="filterParam.selectedReceivers.length" label-text="收款人" item-text="userName"
        multiple="multiple" ng-model="filterParam.selectedReceivers"></li>

        </ul>
        <button class="butn butn-warning f-l" ng-click="requestReceive()">确定筛选</button>
        <i search-filter-clear page-flag="3">[ 清空 ]</i>
        </div>
        </div>
        <div class="search-container" ng-show="selectedListType.code=='2'">
        <div class="search-condition">
        <span class="fa fa-filter search-condition-icon"></span>
        <ul>
        <li>

        <div qc-Date-Period class="search-condition-item" ng-model="selectedOrderDateTime">下单日期<span>▼</span></div>

        <div qc-Date-Period class="search-condition-item"
        ng-model="receiveOrder_selectedDepartureDate">出团日期<span>▼</span></div>

        <%--下单人--%>
        <div search-condition-orderer class="search-condition-item"
        ng-model="receiveOrderListfilterParam.selectedOrderers"></div>

        <%--订单收款状态--%>
        <div search-condition-receiveorder-receivestatus class="search-condition-item"
        ng-model="receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus"></div>

        </li>
        </ul>
        </div>
        <div ng-if="receiveOrderisShowFilters()" search-filter-list class="search-filter-list">
        <ul>

        <li search-filter-date-period ng-if="receiveOrderListfilterParam.selectedOrderDateTime.length"
        label-text="下单日期"
        multiple="multiple" ng-model="receiveOrderListfilterParam.selectedOrderDateTime"></li>

        <li search-filter-date-period ng-if="receiveOrderListfilterParam.receiveOrder_selectedDepartureDate.length"
        label-text="出团日期"
        multiple="multiple" ng-model="receiveOrderListfilterParam.receiveOrder_selectedDepartureDate"></li>

        <li search-filter ng-show="receiveOrderListfilterParam.selectedOrderers.length"
        label-text="下单人"
        item-text="userName" multiple="multiple"
        ng-model="receiveOrderListfilterParam.selectedOrderers"></li>

        <li search-filter ng-show="receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus.length"
        label-text="订单收款状态" multiple="multiple" item-text="receiveStatusName"
        ng-model="receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus"></li>

        </ul>
        <button class="butn butn-warning f-l" ng-click="requestReceiveOrderList()">确定筛选</button>
        <i search-filter-clear page-flag="4">[ 清空 ]</i>
        </div>
        </div>
        <!--E高级搜索-->
        <!--S列表排序-->
        <div pagination-sort can-Fold-All="false" ng-show="selectedListType.code=='1'"></div>
        <div pay-order-pagination-sort can-Fold-All="false" ng-show="selectedListType.code=='2'"></div>
        <!--E列表排序-->
        <!--S列表-->
        <div class="qc-table-container qc-scroll" bottom-gap="5" ng-show="selectedListType.code=='1'">
        <table id="contentTable" class=" table-list-lg text-left" qc-sub-table-container qc-table-fixed-header>
        <thead>
        <tr>
        <th class="table-th-default-width">序号</th>
        <th class="table-th-default-width">收款日期
        <br>银行到账日期</th>
        <th class="table-th-default-width">
        <span>款项名称</span></th>
        <th class="table-th-md-width text-left"><span>团号</span>
        <%--<br /><span>产品名称</span>--%>
        </th>
        <th class="table-th-default-width ">出团日期</th>
        <th class="table-th-default-width ">收款类别</th>
        <th class="table-th-default-width "><span>地接社</span><br><span>渠道商</span></th>
        <th class="table-th-default-width">付款单位</th>
        <th class="table-th-default-width">收款人</th>
        <th class="table-th-default-width text-right"><span>订单总额</span>
        <br><span>累计到账总额</span></th>
        <th class="table-th-default-width text-right"><span>收款金额</span>
        <br><span>到账金额</span></th>
        <th class="table-th-default-width">收款确认</th>
        <th class="table-th-default-width">收款审核</th>
        <th style="width: 180px">操作</th>
        <th class="empty"></th>
        </tr>
        </thead>
        <tbody ng-class="{'odd':$odd,'even':$even}" ng-repeat="receive in results">
        <tr ng-cloak="">
        <td>{{(pageInfo.currentIndex-1)*pageInfo.rowCount+$index+1}}</td>
        <td><p>{{receive.receiveDate|dateString}}</p>
        <p>{{receive.arrivalBankDate|dateString}}</p></td>
        <td>
        <span class="table-th-default-width ellipsis" title="{{receive.fundsName}}">{{receive.fundsName}}</span>
        </td>
        <td><p class="table-th-md-width ellipsis" title="{{receive.groupNo}}">{{receive.groupNo}}</p>
        <%--<p class="table-th-md-width ellipsis" title="{{receive.productName}}">{{receive.productName}}</p>--%>
        </td>
        <td>{{receive.departureDate|dateString}}</td>
        <td><p class=" ellipsis" title="{{receive.receiveTypeName}}">{{receive.receiveTypeName}}</p></td>
        <td><p class="table-th-default-width ellipsis" title="{{receive.tourOperatorOrChannelName}}"
        >{{receive.tourOperatorOrChannelName}}</p></td>
        <td>
        <p class="table-th-default-width ellipsis" title="{{receive.paymentCompany}}">{{receive.paymentCompany}}</p>
        </td>
        <td><p class="table-th-default-width ellipsis" title="{{receive.receiver}}">{{receive.receiver}}</p></td>
        <td class="text-right">
        <p class="table-th-default-width ellipsis" ng-repeat="amount in receive.orderAmount"
        title="{{amount.amount|qcCurrency:'¥'}}">{{amount.amount|qcCurrency:'¥'}}</p>
        <!--<p class="table-th-default-width ellipsis" >---</p>-->
        <p ng-repeat="amount in receive.totalArrivedAmount"
        title="{{amount.amount|qcCurrency:'¥'}}">{{amount.amount|qcCurrency:'¥'}}</p>
        </td>
        <td class="text-right">
        <p class="table-th-default-width ellipsis" ng-repeat="amount in
        receive.receivedAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        <!--<p class="table-th-default-width ellipsis" >---</p>-->
        <p class="table-th-default-width ellipsis" ng-repeat="amount in
        receive.arrivedAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        </td>
        <td>{{receive.receiveStatusName}}</td>
        <td>
        <span class="base-operator">
        <button ng-click="receiveConfirmPop(receive)"
        ng-disabled="receive.receiveStatusCode!=99 || !isRole('mtourFinance:receive:confirm')">确认收款</button>
        <button ng-click="reject(receive)"
        ng-disabled="receive.receiveStatusCode!=99 || !isRole('mtourFinance:receive:reject')">驳回</button>
        </span>
        </td>
        <td>
        <span class="base-operator">
        <button ng-disabled="!isRole('mtourFinance:receive:showOrder')"
        order-reg-button="" order-uuid="receive.orderUuid"
        operator-type="2" >查看订单</button>
        <button ng-disabled="!isRole('mtourFinance:receive:detail')"
        ng-click="showReceiveDetail(receive)">详情</button>
        <button ng-disabled="!isRole('mtourFinance:receive:income')" print-income-button
        receive-uuid="receive.receiveUuid" funds-Type="receive.receiveTypeCode">收入单</button>
        <button ng-click="cancel(receive)"
        ng-disabled="receive.receiveStatusCode!=1">取消确认</button>
        </span>
        </td>
        <td>
        </td>
        </tr>
        </tbody>

        </table>
        <div class="page" ng-if="pageInfo.totalRowCount">
        <div pagination page-info="pageInfo"></div>
        </div>

        </div>
        <div class="qc-table-container qc-scroll" bottom-gap="5" ng-show="selectedListType.code=='2'">
        <table id="contentTable" class=" table-list-lg text-left" qc-sub-table-container qc-table-fixed-header>
        <thead>
        <tr>
        <th class="table-th-default-width">序号</th>
        <th class="table-th-default-width">团号</th>
        <th class="table-th-default-width">下单时间</th>
        <th class="table-th-default-width ">出团日期</th>
        <th class="table-th-default-width ">下单人</th>
        <th class="table-th-default-width text-right">订单总额</th>
        <th class="table-th-default-width text-right">累计到账总额</th>
        <th class="table-th-default-width">订单收款状态</th>
        <th style="width: 180px">操作</th>
        </tr>
        </thead>
        <tbody
        ng-class="{'odd':$odd,'even':$even,'active':order.qcSubTableIds.indexOf(receiveOrderSpreadSubTableId)>=0}"
        ng-init="initReceiveOrderSpreadSubTableIs(order)"
        ng-repeat-start="order in receiveOrderList.results">
        <tr ng-cloak="">
        <td>{{(payorder_pageInfo.currentIndex-1)*payorder_pageInfo.rowCount+$index+1}}</td>
        <td>
        <span class="table-th-default-width ellipsis ng-binding"
        title="{{order.groupNo}}">{{order.groupNo}}</span>
        </td>
        <td>{{order.orderDateTime}}</td>
        <td>{{order.departureDate|dateString}}</td>
        <td><p class="table-th-default-width ellipsis" title="{{order.orderer}}">{{order.orderer}}</p></td>
        <td class="text-right">{{order.orderAmount}}</td>
        <td class="text-right">{{order.totalArriveAmount}}</td>
        <td>{{order.orderReceiveStatusCodeStatusName}}</td>
        <td>
        <span class="base-operator">
        <button
        ng-class="{'active':receiveOrderSpreadSubTableId!='receiveList'+order.orderUuid}"
        ng-click="orderListToggleSubTable('receiveList'+order.orderUuid)"
        ng-show="receiveOrderSpreadSubTableId!=('receiveList'+order.orderUuid)">展开</button>
        <button
        ng-class="{'active':receiveOrderSpreadSubTableId!='receiveList'+order.orderUuid}"
        ng-click="orderListToggleSubTable('receiveList'+order.orderUuid)"
        ng-show="receiveOrderSpreadSubTableId==('receiveList'+order.orderUuid)">收起</button>
        <button ng-disabled="!isRole('mtourFinance:receive:showOrder')"
        order-reg-button="" order-uuid="order.orderUuid"
        operator-type="2" >查看订单</button>
        </span>
        </td>
        </tr>
        </tbody>
        <%--122需求-订单列表的二级列表--%>
        <tbody class="qc-sub-table-container" ng-repeat-end style="display: none">
        <tr>
        <td colspan="9">
        <div qc-sub-table qc-sub-table-id="{{'receiveList'+order.orderUuid}}">
        <div receive-list order-uuid="order.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>
        </table>
        <div class="page" ng-if="payorder_pageInfo.totalRowCount">
        <div receive-order-pagination page-info="payorder_pageInfo"></div>
        </div>

        </div>
        </div>

        <!--E列表-->
        <!--S分页-->

        <!--E分页-->
        </div>

        </div>
        </div>
        </div>
        </body>

        </html>