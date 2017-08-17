    <%@ page contentType="text/html;charset=UTF-8" %>
        <%@ include file="/WEB-INF/views/include/taglib.jsp" %>
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8">
        <title>账龄列表</title>
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
        src="${ctxStatic}/mtour/static/js/finance/finance.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/financeAgeList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/channelOrderDetailList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/order/orderList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/order/orderReg.js"></script>
        <%--<script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/order/orderTable.js"></script>
--%>
        <style type="text/css"></style>
        </head>
        <body ng-app="financeAgeList">
        <div class="header">
        <!--S面包屑-->
        <div class="breadcrumbclip">
        <i class="fa fa-map-marker"></i>
        <span><a href="javascript:void(0);">财务 &gt;</a></span> <span><a
        href="javascript:void(0);">账龄列表
        </a></span>
        </div>
        <!--E面包屑-->

        <!--S登录信息-->
        <div >
        <ul user-info></ul>
        </div>
        <!--E登录信息-->
        </div>
        <div class="main">
        <div main-Left current-menu-url="/mtour/mtourfinance/financeAging"></div>
        <div id="rightPart" class="main-right ">
        <div class="content main_frame_father">
        <div class="main_frame " ng-controller=financeAgeController>
        <div class="main-filter-search-container">
        <%--<div main-filter item-text="accountReceivableAgeStatusName" items="accountReceivableAgeStatusList"
        ng-model="selectedAccountReceivableAgeStatus"></div>--%>
        <div main-filter item-text="channelTypeName" items="channelType"
        ng-model="channelTypeSelf"></div><!--此处的channelTypeSelf双向绑定-->

        <div class="main-search-container">
        <ul>
        <li>
        <div main-search ng-model="searchParam" default-search-type="'1'" page="financeAging"></div>
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

        <div search-Condition-Receive-Age
             class="search-condition-item"
             ng-model="filterParam.selectedReceiveAges"></div>

        <%--<div search-Condition-Receive-Type class="search-condition-item"
             ng-model="filterParam.selectedReceiveTypes"></div>--%>


        <div search-condition-channel
        class="search-condition-item"
        ng-model="filterParam.selectedChannels"></div>

        <div search-condition-Sale class="search-condition-item"
        ng-model="filterParam.selectedSales"></div>

        </li>
        </ul>
        </div>
        <div ng-if="isShowFilters()" search-filter-list class="search-filter-list">
        <ul>


        <li search-filter ng-if="filterParam.selectedReceiveAges.length" label-text="账款是否结清" item-text="accountReceivableAgeStatusName"
            multiple="multiple" ng-model="filterParam.selectedReceiveAges"></li>
        <li search-filter ng-if="filterParam.selectedChannels.length" label-text="渠道"
        item-text="channelName" multiple="multiple"
        ng-model="filterParam.selectedChannels"></li>
        <li search-filter ng-if="filterParam.selectedSales.length" label-text="跟进销售"
        item-text="userName" multiple="multiple"
        ng-model="filterParam.selectedSales"></li>

        </ul>
        <button class="butn butn-warning f-l" ng-click="requestAge()">确定筛选</button>
        <i search-filter-clear page-flag="5">[ 清空 ]</i>
        </div>
        </div>
        <!--E高级搜索--><!--S列表排序-->
        <div pagination-sort class="search-condition"></div>
        <!--E列表排序--><!--S列表-->
        <div class="qc-table-container qc-scroll" bottom-gap="5">
        <table id="contentTable" class=" table-list-lg" qc-sub-table-container
        qc-table-fixed-header>
        <thead>
        <tr>
        <th class="table-th-default-width">序号</th>
        <th class="table-th-default-width">渠道名称</th>
        <th class="table-th-default-width text-right">游客数</th>
        <th class="table-th-default-width">跟进销售</th>
        <th class="table-th-default-width text-right">应收金额</th>
        <th class="table-th-default-width text-right" ng-if="companyRoleCode!='0'">已收金额</th>
        <th class="table-th-default-width text-right">到账金额</th>
        <th class="table-th-default-width text-right">未收金额</th>
        <th style="width: 180px">操作</th>
        <th class="empty"></th>
        </tr>
        </thead>
        <tbody ng-class="{'odd':$odd,'even':$even,'active':pay.qcSubTableIds.indexOf(spreadSubTableId)>=0}"
        ng-init="initOrderSpreadSubTableIs(age)"
        ng-repeat-start="age in results">
        <tr ng-cloak="">
        <td>{{(pageInfo.currentIndex-1)*pageInfo.rowCount+$index+1}}</td>
        <td><p class="table-th-default-width ellipsis" title="{{age.channelName}}">{{age.channelName}}</p></td>
        <td class="text-right">{{age.totalTravelerCount|number}}</td>
        <td><p class="table-th-default-width ellipsis" title="{{age.salesName}}">{{age.salesName}}</p></td>
        <td class="text-right">
        <p ng-repeat="amount in age.receivableAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        </td>
        <td class="text-right" ng-if="companyRoleCode!='0'">
        <p ng-repeat="amount in age.receivedAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        </td>
        <td class="text-right">
        <p ng-repeat="amount in age.arrivedAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        </td>
        <td class="text-right">
        <p ng-repeat="amount in age.unreceiveAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        </td>
        <td>
        <a href="javascript:void(0);"
        ng-class="{'active':spreadSubTableId!='paymentRecordList'+pay.paymentUuid}"
        ng-click="toggleSubTable('channelOrder'+age.channelUuid)">订单明细</a>
        </td>
        <td class="empty"></td>
        </tr>
        </tbody>

        <%--订单明细--%>
        <tbody class="qc-sub-table-container" ng-repeat-end style="display: none">
        <tr>
        <td colspan="9">
        <div qc-sub-table qc-sub-table-id="{{'channelOrder'+age.channelUuid}}">
        <div channel-order-detail-list="" channel-uuid="age.channelUuid"></div>
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