    <%@ page contentType="text/html;charset=UTF-8" %>
        <%@ include file="/WEB-INF/views/include/taglib.jsp" %>
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8">
        <title>付款列表</title>
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
        src="${ctxStatic}/mtour/static/js/finance/financePayList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/paymentRecordList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/paymentMergeList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/payment.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/order/orderList.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/order/orderReg.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/batchPayRecordDetail.js"></script>
        <script type="text/javascript"
        src="${ctxStatic}/mtour/static/js/finance/batchPaymentFundsTypeDetail.js"></script>

        <style type="text/css"></style>
        </head>
        <body ng-app="financePayList">
        <div class="header">
        <!--S面包屑-->
        <div class="breadcrumbclip">
        <i class="fa fa-map-marker"></i>
        <span><a href="javascript:void(0);">财务 &gt;</a></span> <span><a
        href="javascript:void(0);">付款列表
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
        <div main-Left current-menu-url="/mtour/mtourfinance/financePay"></div>
        <div id="rightPart" class="main-right ">
        <div class="content main_frame_father">
        <div class="main_frame " ng-controller="financePayController">
        <div class="main-filter-search-container">
        <%--<div main-filter item-text="paymentStatusName" items="paymentStatusList"--%>
        <%--ng-model="selectedPaymentStatus" ng-show="companyRoleCode!='0'"></div>--%>
        <div payment-list-type ng-model="selectedPaymentListType"
        items="paymentListTypes" item-text="name"></div>

        <div class="main-search-container">
        <ul>
        <li>
        <div main-search ng-model="searchParam" default-search-type="'1'" page="financePay"></div>
        </li>
        <li>
      <!--   <div ng-if="searchCount" main-count ng-model="searchCount"
        current-Code="'2'"></div> -->
        </li>
        </ul>
        </div>

        </div>
        <!--付款-款项列表-->
        <div class="search-container" ng-show="selectedPaymentListType.code=='1'" >
        <div class="search-condition">
        <span class="fa fa-filter search-condition-icon"></span>
        <ul>
        <li>
        <div search-Condition-funds-Type class="search-condition-item"
        ng-model="filterParam.selectedFundsTypes"></div>

        <%--90需求二级搜索添加付款状态条件--%>
        <div search-Condition-paystatus class="search-condition-item"
        ng-model="filterParam.selectedPaymentStatus"></div>
        <%--90需求二级搜索添加付款状态条件--%>

        <div search-Condition-Tour-Operator-Or-Channel-Payment
        class="search-condition-item"
        ng-model="filterParam.selectedTourOperatorOrChannel">
        </div>
        <div search-condition-channel
        ng-if="filterParam.selectedTourOperatorOrChannel.tourOperatorChannelCategoryCode=='2'"
        class="search-condition-item"
        ng-model="filterParam.selectedChannels"></div>

        <div search-condition-tour-operator
        ng-if="filterParam.selectedTourOperatorOrChannel.tourOperatorChannelCategoryCode=='1'"
        class="search-condition-item"
        ng-model="filterParam.selectedTourOperators"></div>

		<div search-condition-Pnr ng-show="companyRoleCode==0" class="search-condition-item" label-text="PNR"
		        ng-model="selectedPayPnr"></div>
        <div search-condition-Applicant class="search-condition-item" ng-model="filterParam.selectedApplicants"></div>

        <div qc-Date-Period
        class="search-condition-item"
        ng-model="selectedApprovalDates">报批日期<span>▼</span></div>

        <div qc-Date-Period
        class="search-condition-item"
        ng-model="selectedDepartureDates">出团日期<span>▼</span></div>

        <div search-Condition-Amount-period class="search-condition-item" label-text="应付金额"  ng-model="selectedPayableAmount"></div>

        <div search-Condition-Amount-period-price class="search-condition-item" label-text="成本单价"  ng-model="selectedPayAmount"></div>


        </li>
        </ul>
        </div>
        <div ng-if="isShowFilters()" search-filter-list class="search-filter-list">
        <ul>

        <li search-filter ng-if="filterParam.selectedFundsTypes.length"
        label-text="款项类型"
        item-text="fundsTypeName" multiple="multiple"
        ng-model="filterParam.selectedFundsTypes"></li>
        <%--90需求Start--%>
        <li search-filter ng-if="filterParam.selectedPaymentStatus.length"
        label-text="付款确认"
        item-text="paymentStatusName" multiple="multiple"
        ng-model="filterParam.selectedPaymentStatus"></li>
        <%--90需求End--%>
        <li search-filter ng-if="filterParam.selectedChannels.length" label-text="渠道"
        item-text="channelName" multiple="multiple"
        ng-model="filterParam.selectedChannels"></li>
        <li search-filter ng-if="filterParam.selectedTourOperators.length" label-text="地接社"
        item-text="tourOperatorName" multiple="multiple"
        ng-model="filterParam.selectedTourOperators"></li>
        <li search-filter-text ng-if="filterParam.selectedPayPnrs.length" label-text="PNR" multiple="multiple"
        ng-model="filterParam.selectedPayPnrs"></li>
        <li search-filter ng-if="filterParam.selectedApplicants.length" label-text="申请人"
        item-text="userName" multiple="multiple"
        ng-model="filterParam.selectedApplicants"></li>

        <li search-filter-date-period ng-show="filterParam.selectedApprovalDates.length" label-text="报批日期"
        multiple="multiple"
        ng-model="filterParam.selectedApprovalDates"></li>
        <li search-filter-date-period ng-show="filterParam.selectedDepartureDates.length" label-text="出团日期"
        multiple="multiple"
        ng-model="filterParam.selectedDepartureDates"></li>

        <li search-filter-amount-period ng-if="filterParam.selectedPayableAmounts.length" label-text="应付金额"
        multiple="multiple"
        ng-model="filterParam.selectedPayableAmounts"></li>
        <li search-filter-amount-period-price  ng-if="filterParam.selectedPayAmounts.length" label-text="成本单价"
        multiple="multiple"
        ng-model="filterParam.selectedPayAmounts"></li>

        </ul>
        <button class="butn butn-warning f-l" ng-click="requestPay('','filter')">确定筛选</button>
        <i search-filter-clear page-flag="1">[ 清空 ]</i>
        </div>
        </div >

        <%--90需求--付款-订单列表--%>
        <div class="search-container" ng-show="selectedPaymentListType.code=='2'" >
        <div class="search-condition">
        <span class="fa fa-filter search-condition-icon"></span>
        <ul>
        <li>
        <div qc-Date-Period
        class="search-condition-item"
        ng-model="selectedOrderDateTime">下单日期<span>▼</span></div>
        <div qc-Date-Period
        class="search-condition-item"
        ng-model="payOrderSelectedDepartureDates">出团日期<span>▼</span></div>
		<%--PNR modified by wlj at 2016.06.02-start--%>
		<div search-condition-Pnr ng-show="companyRoleCode==0" class="search-condition-item" label-text="PNR"
		        ng-model="selectedPayPnrOrder"></div>
        <%--PNR modified by wlj at 2016.06.02-end--%>
        <%--下单人--%>
        <div search-condition-orderer class="search-condition-item"
        ng-model="payOrderListfilterParam.selectedOrderers"></div>

        <%--订单付款状态--%>
        <div search-condition-financepay-orderlist-paystatus class="search-condition-item"
        ng-model="payOrderListfilterParam.selectedfinancePayOrderListPayStatus"></div>

        </li>
        </ul>
        </div>
        <div ng-if="payOrderIsShowFilters()" search-filter-list class="search-filter-list" >
        <ul>

        <li search-filter-date-period ng-show="payOrderListfilterParam.selectedOrderDateTime.length" label-text="下单日期"
        multiple="multiple"
        ng-model="payOrderListfilterParam.selectedOrderDateTime"></li>

        <li search-filter-date-period ng-show="payOrderListfilterParam.payOrderSelectedDepartureDate.length"
        label-text="出团日期"
        multiple="multiple"
        ng-model="payOrderListfilterParam.payOrderSelectedDepartureDate"></li>
        <%--PNR modified by wlj at 2016.06.02-start--%>
         <li search-filter-text ng-if="payOrderListfilterParam.selectedPayPnrsOrder.length" label-text="PNR" multiple="multiple"
        ng-model="payOrderListfilterParam.selectedPayPnrsOrder"></li>
		<%--PNR modified by wlj at 2016.06.02-end--%>
        <li search-filter ng-show="payOrderListfilterParam.selectedOrderers.length"
        label-text="下单人"
        item-text="userName" multiple="multiple"
        ng-model="payOrderListfilterParam.selectedOrderers"></li>

        <li search-filter ng-show="payOrderListfilterParam.selectedfinancePayOrderListPayStatus.length"
        label-text="订单付款状态"
        multiple="multiple" item-text="financePayOrderListPayStatusName"
        ng-model="payOrderListfilterParam.selectedfinancePayOrderListPayStatus"></li>

        </ul>
        <button class="butn butn-warning f-l" ng-click="requestPayOrderList('','filter')">确定筛选</button>
        <i search-filter-clear page-flag="2">[ 清空 ]</i>
        </div>
        </div>

        <!--E高级搜索--><!--S列表排序-->
        <div pagination-sort ng-show="selectedPaymentListType.code=='1'"></div>
        <div pay-order-pagination-sort ng-show="selectedPaymentListType.code=='2'"></div>
        <!--E列表排序--><!--S列表-->

        <%--款项列表table--%>
        <div class="qc-table-container qc-scroll" bottom-gap="5"
        ng-show="selectedPaymentListType.code=='1'">
        <table id="contentTable" class=" table-list-lg" qc-sub-table-container
        qc-table-fixed-header>
        <thead>
        <tr>
        <th class="table-th-default-width">序号</th>
        <th class="table-th-default-width">报批日期</th>
        <th class="table-th-default-width">PNR</th>
        <th class="table-th-md-width"><p>团号</p>
        <p ng-if="companyRoleCode!=0">产品名称</p>
        </th>
        <th class="table-th-default-width">出团日期</th>
        <th class="table-th-default-width">款项名称</th>
        <th class="table-th-default-width">款项类型</th>
        <th class="table-th-default-width">地接社渠道商</th>
        <th class="table-th-default-width">申请人</th>
        <th class="table-th-default-width text-right"><p>应付金额</p><p>已付金额</p></th>
        <th class="table-th-default-width">付款确认</th>
        <th class="table-th-default-width">付款操作</th>
        <th style="width: 180px">操作</th>
        <th class="empty"></th>
        </tr>
        </thead>
        <tbody ng-class="{'odd':$odd,'even':$even,'active':pay.qcSubTableIds.indexOf(spreadSubTableId)>=0}"
        ng-init="initOrderSpreadSubTableIs(pay)"
        ng-repeat-start="pay in results">
        <tr ng-cloak="">
        <td>{{(pageInfo.currentIndex-1)*pageInfo.rowCount+$index+1}}</td>
        <td>{{pay.approvalDate}}</td>
        <td>
        <span class="table-th-default-width ellipsis"
        title="{{pay.invoiceOriginalTypeCode=='0'?pay.PNR:pay.tourOperatorName}}">{{pay.invoiceOriginalTypeCode=='0'?pay.PNR:pay.tourOperatorName}}</span>
        </td>
        <td><p class="table-th-md-width ellipsis" title="{{pay.groupNo}}">{{pay.groupNo}}</p>
        <p class="table-th-md-width" ng-if="companyRoleCode!='0'"
        ellipsis title="{{pay.productName}}">{{pay.productName}}</p></td>
        <td>{{pay.departureDate|dateString}}</td>
        <td><p class="table-th-default-width ellipsis" title="{{pay.fundsName}}">{{pay.fundsName}}</p></td>
        <!-- modify by wlj at 2016.06.02 start -->
        <td>{{pay.payFundsType.fundsTypeName}}<p  ng-if="pay.payFundsType.fundsTypeName=='成本'" ng-repeat="amount in pay.payAmount">/{{amount.amount|qcCurrency:amount.currencyCode}}</p></td>
        <!-- modify by wlj at 2016.06.02 end -->
        <td><p class="table-th-default-width ellipsis"
        title="{{pay.tourOperatorOrChannelName}}">{{pay.tourOperatorOrChannelName}}</p></td>
        <td><p class="table-th-default-width ellipsis" title="{{pay.applicant}}">{{pay.applicant}}</p></td>
        <td class="text-right">
        <p ng-repeat="amount in pay.payableAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        <p ng-repeat="amount in pay.paidAmount">{{amount.amount|qcCurrency:amount.currencyCode}}</p>
        </td>
        <td>{{pay.payStatus.paymentStatusName}}</td>
        <td>
        <span class="base-operator">
        <button finance-payment-input=""
        payment-uuid="pay.paymentUuid" ng-disabled="!isRole('mtourFinance:pay:payMoney')"
        order-uuid="pay.orderUuid" funds-Type="pay.fundsType"
        channel-type-code="pay.channelType.channelTypeCode" paid-amount="pay.paidAmount">付款</button>
        <button ng-disabled="!isRole('mtourFinance:pay:showPayList')"
        ng-class="{'active':spreadSubTableId!='paymentRecordList'+pay.fundsType+'-'+pay.paymentUuid}"
        ng-click="toggleSubTable('paymentRecordList'+pay.fundsType+'-'+pay.paymentUuid)">付款记录</button>
        </span>
        </td>
        <td>
        <span class="base-operator">
        <button ng-disabled="!isRole('mtourFinance:pay:showPayDetail')"
        ng-click="showPayFundsDetail(pay.fundsType,pay.paymentUuid,pay.orderUuid)">款项明细</button>
        <button ng-disabled="!isRole('mtourFinance:pay:expenditure')"
        print-Expenditure-Button
        payment-Uuid="pay.paymentUuid"
        funds-Type="pay.fundsType">支出单</button>
        <button ng-disabled="!isRole('mtourFinance:pay:statements')"
        print-settlement-Button order-Uuid="pay.orderUuid" >结算单</button>
        </span>
        </td>
        <td></td>
        </tr>
        </tbody>

        <%--付款记录--%>
        <tbody class="qc-sub-table-container" ng-repeat-end style="display: none">
        <tr>
        <td colspan="13">
        <div qc-sub-table qc-sub-table-id="{{'paymentRecordList'+pay.fundsType+'-'+pay.paymentUuid}}">
        <div payment-record-list="" payment-uuid="pay.paymentUuid" funds-Type="pay.fundsType"
        order-uuid="pay.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>
        </table>
        <div class="page" ng-if="pageInfo.totalRowCount">
        <div pagination page-info="pageInfo"></div>
        </div>
        </div>

        <%--订单列表table--%>
        <div class="qc-table-container qc-scroll" bottom-gap="5"
        ng-show="selectedPaymentListType.code=='2'">
        <table id="contentTable" class=" table-list-lg" qc-sub-table-container
        qc-table-fixed-header>
        <thead>
        <tr>
        <th class="table-th-default-width">序号</th>
        <th class="table-th-md-width">团号</th>
        <th class="table-th-default-width">下单时间</th>
        <th class="table-th-default-width">出团日期</th>
        <th class="table-th-default-width">下单人</th>
        <th class="table-th-lg-width text-right">应付总额</th>
        <th class="table-th-lg-width text-right">已付总额</th>
        <th class="table-th-default-width">订单付款状态</th>
        <th style="width: 180px">操作</th>
        <th class="empty"></th>
        </tr>
        </thead>
        <tbody ng-class="{'odd':$odd,'even':$even,'active':order.qcSubTableIds.indexOf(payorderSpreadSubTableId)>=0}"
        ng-init="initPayOrderSpreadSubTableIs(order)"
        ng-repeat-start="order in orderListResults">
        <tr ng-cloak="">
        <td>{{(payorder_pageInfo.currentIndex-1)*payorder_pageInfo.rowCount+$index+1}}</td>
        <td>
        <span class="table-th-md-width ellipsis ng-binding"
        title="{{order.groupNo}}">{{order.groupNo}}</span>
        </td>
        <td>{{order.orderDateTime}}</td>
        <td>{{order.departureDate|dateString}}</td>
        <td><p class="table-th-default-width ellipsis" title="{{order.orderer}}">{{order.orderer}}</p></td>
        <td class="text-right">
        <em ng-repeat-start=" amount in order.payableAmount"
        ng-bind="amount.amount|qcCurrency:amount.currencyCode"></em>
        <em ng-repeat-end="" ng-show="!$last">+</em>
        </td>
        <td class="text-right">
        <em ng-repeat-start=" amount in order.paidAmount"
        ng-bind="amount.amount|qcCurrency:amount.currencyCode"></em>
        <em ng-repeat-end="" ng-show="!$last">+</em>
        </td>
        <td>{{order.financePayOrderListPayStatusName}}</td>
        <td>
        <span class="base-operator">
        <button
        ng-class="{'active':payorderSpreadSubTableId!='paymentMergeList'+order.orderUuid}"
        ng-click="orderListToggleSubTable('paymentMergeList'+order.orderUuid)"
        ng-show="payorderSpreadSubTableId!=('paymentMergeList'+order.orderUuid)">展开</button>
        <button
        ng-class="{'active':payorderSpreadSubTableId!='paymentMergeList'+order.orderUuid}"
        ng-click="orderListToggleSubTable('paymentMergeList'+order.orderUuid)"
        ng-show="payorderSpreadSubTableId==('paymentMergeList'+order.orderUuid)">收起</button>
        <button ng-disabled="!isRole('mtourFinance:pay:statements')"
        print-settlement-Button order-Uuid="order.orderUuid" >结算单</button>
        </span>
        </td>
        <td></td>
        </tr>
        </tbody>

        <%--90需求-订单列表的二级列表--%>
        <tbody class="qc-sub-table-container" ng-repeat-end style="display: none">
        <tr>
        <td colspan="13">
        <div qc-sub-table qc-sub-table-id="{{'paymentMergeList'+order.orderUuid}}">
        <div payment-merge-list order-uuid="order.orderUuid"></div>
        </div>
        </td>
        </tr>
        </tbody>
        </table>
        <div class="page" ng-if="payorder_pageInfo.totalRowCount">
        <div pay-order-pagination page-info="payorder_pageInfo" ></div>
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