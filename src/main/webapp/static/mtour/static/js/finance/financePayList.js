var financePayList = angular.module('financePayList', ['mtour', 'finance', 'qc.uploader']);

financePayList.controller('financePayController', [
    '$scope', '$timeout', 'fixedValue', 'commonValue', 'joinedStringByArray', 'joinChannelAndTourOperator',
    'qcApi', 'amountsToRMB', 'qcObjectInArray', 'joinedStringByArray', 'joinAmount','joinAmountWithUnit', 'joinDate', 'qcDialog', 'urlConfig', '$rootScope', 'checkRole',
    function ($scope, $timeout, fixedValue, commonValue, joinedStringByArray, joinChannelAndTourOperator,
              qcApi, amountsToRMB, qcObjectInArray, joinedStringByArray, joinAmount, joinAmountWithUnit,joinDate,
              qcDialog, urlConfig, $rootScope, checkRole) {
        $scope.channelType=fixedValue.channelType;
        $scope.channelTypeSelf = $scope.channelType[0];
        //90需求
        $scope.paymentListTypes = [{
            code: '1', name: '款项列表'
        }];
        $scope.$on('userinfo.loaded',function(){
            $scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
        });

        //90需求
        qcApi.post('common/getMenuByUser').success(function (result) {
            $scope.menus = result.data;
            angular.forEach($scope.menus, function (menu) {
                angular.forEach(menu.subMenus, function (subMenu) {
                    angular.forEach(subMenu.roles, function (role) {
                        if (role.roleCode.indexOf('mtourFinance:pay:orderList') != -1) {
                            $scope.paymentListTypes.push({
                                code: '2', name: '订单列表'
                            });
                            return;
                        }
                    });
                });
            });

        });
        $scope.selectedPaymentListType = $scope.paymentListTypes[0];
        $scope.paymentStatusList = fixedValue.financePaymentStatus;

        $scope.filterParam = {
            selectedFundsTypes           : [],
            selectedTourOperatorOrChannel: {},
            selectedPaymentStatus        : [],
            selectedChannels             : [],
            selectedTourOperators        : [],
            selectedApplicants           : [],
            selectedApprovalDates        : [],
            selectedDepartureDates       : [],
            selectedPayableAmounts       : [],
            selectedPayAmounts      	 : [],
            selectedPayPnrs				 : []
        };
        $scope.isShowFilters = function () {
            return $scope.filterParam.selectedFundsTypes.length
                || $scope.filterParam.selectedPaymentStatus.length
                || $scope.filterParam.selectedChannels.length
                || $scope.filterParam.selectedTourOperators.length
                || $scope.filterParam.selectedApplicants.length
                || $scope.filterParam.selectedPayPnrs.length
                || $scope.filterParam.selectedApprovalDates.length
                || $scope.filterParam.selectedDepartureDates.length
                || $scope.filterParam.selectedPayAmounts.length
                || $scope.filterParam.selectedPayableAmounts.length;

        };

        $scope.isRole = function (url) {
            return checkRole(url);
        };


        $scope.searchParam = {searchType: '1', searchKey: ''};


        $scope.pageInfo = {
            totalRowCount: 0,
            currentIndex : 1,
            rowCount     : 20
        };
        $scope.payorder_pageInfo = {
            totalRowCount: 0,
            currentIndex : 1,
            rowCount     : 20
        };

        $scope.results = [];


        $scope.sortKeys = [
            {
                name: '报批日期',
                code: 'approvalDate'
            },
            {
                name: '更新时间',
                code: 'modifiedDateTime'
            },
            {
                name: '出团日期',
                code: 'departureDate'
            }
        ];
        $scope.sortInfo = {
            sortKey: 'approvalDate',
            dec    : true
        };


        //子表格
        $scope.spreadSubTableId;
        $scope.initOrderSpreadSubTableIs = function (pay) {
            pay.qcSubTableIds = [
                'paymentRecordList' + pay.fundsType + '-' + pay.paymentUuid
            ];
        };
        //$scope.spreadSubTables = {};
        $scope.toggleSubTable = function (qcSubTableId) {
            if ($scope.spreadSubTableId == qcSubTableId) {
                $scope.$broadcast('subTable.fold');
            }
            else {
                $scope.$broadcast('subTable.spread.ready', qcSubTableId);
            }
        };
        $scope.$on('subTable.spread.complete', function ($e, qcSubTableId) {
            $scope.spreadSubTableId = qcSubTableId;
        });
        $scope.$on('subTable.fold.complete', function ($e, qcSubTableId) {
            if ($scope.spreadSubTableId == qcSubTableId) {
                $scope.spreadSubTableId = undefined;
            }
        });
        $scope.$on('subTable.foldAll.request', function () {
            $scope.$broadcast('subTable.fold');
        })

        
        //点击主搜索框的查询按钮后
        $scope.$on('mainSearch.search', function ($e, searchParam) {
            $scope.searchParam = searchParam;
            $scope.filterParam.selectedFundsTypes.length = 0;
            $scope.filterParam.selectedPaymentStatus.length = 0;
            if ($scope.selectedPaymentListType.code == '2') {
                $scope.payOrderListfilterParam.selectedOrderDateTime.length = 0;
                $scope.payOrderListfilterParam.payOrderSelectedDepartureDate.length = 0;
                $scope.payOrderListfilterParam.selectedOrderers.length = 0;
                $scope.payOrderListfilterParam.selectedfinancePayOrderListPayStatus.length = 0;
                $scope.payorder_pageInfo.totalRowCount = 0;
                $scope.payorder_pageInfo.currentIndex = 1
                $scope.requestPayOrderList();
            }
            else {
                $scope.filterParam.selectedChannels.length = 0;
                $scope.filterParam.selectedTourOperators.length = 0;
                $scope.filterParam.selectedApplicants.length = 0;
                $scope.filterParam.selectedPayPnrs.length = 0;
                $scope.filterParam.selectedApprovalDates.length = 0;
                $scope.filterParam.selectedDepartureDates.length = 0;
                $scope.filterParam.selectedPayableAmounts.length = 0;
                $scope.filterParam.selectedPayAmounts.length = 0;
                $scope.pageInfo.totalRowCount = 0;
                $scope.pageInfo.currentIndex = 1
                $scope.requestPay();
            }


            //$scope.requestCount();
        });


        //分页信息变化
        $scope.$on('pagination.change', function ($e, flag) {

            if (flag == 'backFirstPage') {
                $scope.requestPay('', 'filter');
            }
            else {
                $scope.requestPay();
            }
        });
        //排序规则变化后
        $scope.$on('sort.change', function () {
            $scope.requestPay();
        });

        //过滤轨迹中删除 一个条件时
        $scope.$on('searchFilter.remove', function () {
            if (!$scope.isShowFilters()) {
                $scope.requestPay();
            }
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //滤轨迹中清除所有条件时
        $scope.$on('searchFilter.clearAll', function () {
            $scope.requestPay();
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //款项类型变化时
        $scope.$on('selectedFundsTypes.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的渠道变化时
        $scope.$on('selectedChannels.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的地接社变化时
        $scope.$on('selectedTourOperators.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });
      //PRN选择
        $scope.$on('selectedPayPnr.confirm', function () {
            $scope.filterParam.selectedPayPnrs.push($scope.selectedPayPnr);
            $scope.$broadcast('selectedPayPnr.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //报批日期选择
        $scope.$on('selectedApprovalDates.change', function () {
            //$scope.filterParam.selectedApprovalDates.push(
            //    {
            //        startDate: $scope.selectedApprovalDate.startDate,
            //        endDate: $scope.selectedApprovalDate.endDate
            //    }
            //);
            //只能选择一个日期范围
            $scope.filterParam.selectedApprovalDates = [
                {
                    startDate: $scope.selectedApprovalDates.startDate,
                    endDate  : $scope.selectedApprovalDates.endDate
                }];
            $scope.$broadcast('selectedApprovalDate.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestPay();
        });


        //已收金额选择
        $scope.$on('selectedPayableAmount.confirm', function () {

           if ($scope.filterParam.selectedPayableAmounts.length) {
                $scope.filterParam.selectedPayableAmounts[0] = {
                    minAmount: $scope.selectedPayableAmount.minAmount,
                    maxAmount: $scope.selectedPayableAmount.maxAmount
                };
            } else {
                $scope.filterParam.selectedPayableAmounts.push(
                    {
                        minAmount: $scope.selectedPayableAmount.minAmount,
                        maxAmount: $scope.selectedPayableAmount.maxAmount
                    }
                );
            }
            $scope.$broadcast('selectedPayableAmount.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestPay();
        });

        //成本单价金额、单位选择"selectedPayAmount"
        $scope.$on('selectedPayAmount.confirm', function () {
        	console.log("-------$scope.filterParam.selectedPayAmounts.length"+$scope.filterParam.selectedPayAmounts.length);
            var unit=[];
            var id= $scope.selectedPayAmount.id;
            if(!id){
                id="";
            }
            for(var i=0;i<id.length;i++){
                if(!id[i].isNumber()){
                    unit.push(id[i]);
                }else{
                    break;
                }
            }
            unit=unit.join("");
            id=id.slice(i);
                $scope.filterParam.selectedPayAmounts.push(
                    {
                    	id: 	   id,
                        minAmount: $scope.selectedPayAmount.minAmount,
                        maxAmount: $scope.selectedPayAmount.maxAmount,
                        unit        :unit
                    }
                );
            $scope.$broadcast('selectedPayAmount.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestPay();
        });

        //选中的申请人变化时
        $scope.$on('selectedApplicants.change', function () {
            //$scope.requestPay();
            $rootScope.$broadcast('qcTableContainer.reset');
        });
        //付款成功后展开录入列表
        $scope.$on('finance.payment.save', function ($e, paymentUuid, fundsType) {
            $scope.requestPay('paymentRecordList' + fundsType + '-' + paymentUuid);
        });
        //付款撤销后刷新主列表和子列表
        $scope.$on('finance.payment.cancel', function ($e, paymentUuid, fundsType) {
            $scope.requestPay('paymentRecordList' + fundsType + '-' + paymentUuid);
        });
        $scope.requestPay = function (spreadTableId, flag) {
            $timeout(function () {
                //90需求-美途
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        paymentStatusCode    : ($scope.filterParam.selectedPaymentStatus.length == 2) ? '' : joinedStringByArray($scope.filterParam.selectedPaymentStatus, 'paymentStatusCode'),
                        fundsType            : joinedStringByArray($scope.filterParam.selectedFundsTypes, 'fundsType'),
                        tourOperatorOrChannel: joinChannelAndTourOperator($scope.filterParam.selectedChannels, $scope.filterParam.selectedTourOperators),
                        applicantId          : joinedStringByArray($scope.filterParam.selectedApplicants, 'userId'),
                        pnrValue	         : joinedStringByArray($scope.filterParam.selectedPayPnrs),
                        approvalDate         : joinDate($scope.filterParam.selectedApprovalDates),

                        departureDate: joinDate($scope.filterParam.selectedDepartureDates),
                        payableAmount: joinAmount($scope.filterParam.selectedPayableAmounts),
                        payAmount: 	   joinAmountWithUnit($scope.filterParam.selectedPayAmounts)
                    },
                    sortInfo   : $scope.sortInfo,
                    pageParam  : $scope.pageInfo
                };
                if (flag == 'filter') {
                    param.pageParam.currentIndex = 1;
                    $scope.pageInfo.currentIndex = 1;
                }

                qcApi.post('mtourfinance/getPayList', param).success(function (result) {
                    $scope.results.length = 0;
                    $scope.results = result.data.results;
                    angular.forEach($scope.results, function (receive) {
                        angular.forEach(receive.payableAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = currency.currencyCode;
                                amount.exchangeRate = currency.exchangeRate;
                            }

                        });
                        angular.forEach(receive.paidAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = currency.currencyCode;
                                amount.exchangeRate = currency.exchangeRate;
                            }
                        });
                        angular.forEach(receive.payAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = currency.currencyCode;
                                amount.exchangeRate = currency.exchangeRate;
                            }
                        });
                        receive.payFundsType = qcObjectInArray(fixedValue.fundsType, 'fundsType', receive.fundsType);
                        if (!receive.payFundsType) {
                            receive.payFundsType = {
                                fundsType    : receive.fundsType,
                                fundsTypeName: "借款"
                            }
                        }
                        receive.payStatus = qcObjectInArray(fixedValue.financePaymentStatus, 'paymentStatusCode', receive.paymentStatus);
                    });
                    $rootScope.$broadcast('qcTableContainer.reset');
                    $scope.pageInfo.totalRowCount = result.data.page.totalRowCount;
                    $scope.pageInfo.currentIndex = result.data.page.currentIndex;
                    $scope.pageInfo.rowCount = +(result.data.page.rowCount);
                    if (spreadTableId) {
                        $timeout(function () {
                            $scope.$broadcast('subTable.spread.ready', spreadTableId);
                        }, 500);
                    }
                });

            });
        };
        //$scope.requestCount = function () {
        //    var param = {
        //        searchParam: $scope.searchParam
        //    };
        //    $timeout(function () {
        //        qcApi.post('common/countAllInfo', param).success(function (result) {
        //            $scope.searchCount = result.data;
        //        });
        //    });
        //};

        //$scope.requestCount();
        $scope.requestPay();

        $scope.showPayFundsDetail = function (fundsType, paymentUuid, orderUuid) {
            if (fundsType == "1") {//借款
                qcApi.post('mtourfinance/getLoanRefundInfo', {
                    paymentUuid: paymentUuid
                }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/loanDetail.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.payFundsDetail = result.data;
                                angular.forEach(dialogScope.payFundsDetail.totalAmounts, function (totalAmount) {
                                    var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', +totalAmount.currencyUuid);
                                    totalAmount.currencyCode = currency.currencyCode;
                                });
                                dialogScope.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', dialogScope.payFundsDetail.currencyUuid);
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    );
                });
            }
            if (fundsType == "2") {//退款
                qcApi.post('mtourfinance/getReturnRefundInfo', {
                    paymentUuid: paymentUuid
                }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/refundDetail.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.payFundsDetail = result.data;
                                angular.forEach(dialogScope.payFundsDetail.totalAmounts, function (totalAmount) {
                                    var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', +totalAmount.currencyUuid);
                                    totalAmount.currencyCode = currency.currencyCode;
                                });
                                dialogScope.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', dialogScope.payFundsDetail.currencyUuid);
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    );
                });
            }
            if (fundsType == "3") {//追加成本
                qcApi.post('mtourfinance/getAddCostRefundInfo', {
                    paymentUuid: paymentUuid
                }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/additionalCostDetail.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.payFundsDetail = result.data;
                                angular.forEach(dialogScope.payFundsDetail.totalAmounts, function (totalAmount) {
                                    var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', +totalAmount.currencyUuid);
                                    totalAmount.currencyCode = currency.currencyCode;
                                });
                                dialogScope.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', dialogScope.payFundsDetail.currencyUuid);
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    );
                });
            }
            if (fundsType == "4") {//成本
                qcApi.post('mtourfinance/getPaymentCost', {
                    paymentUuid: paymentUuid
                }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderCostDetail.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.OrderCostDetail = result.data;


                                dialogScope.convertedCurrency = commonValue.defaultCurrency;

                                qcApi.post('order/getExistsCurrency', {
                                    orderId: orderUuid,
                                    type   : '2'
                                }).success(function (result) {
                                    dialogScope.currencies = result.data;
                                    dialogScope.selectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', dialogScope.OrderCostDetail.currencyUuid);
                                    angular.forEach(dialogScope.OrderCostDetail.totalAmounts, function (totalAmount) {
                                        var currency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', +totalAmount.currencyUuid);
                                        totalAmount.currencyCode = currency.currencyCode;
                                    });
                                });

                                dialogScope.OrderCostDetail.paymentBank = dialogScope.OrderCostDetail.remitBank;
                                dialogScope.OrderCostDetail.paymentAccount = dialogScope.OrderCostDetail.remitAccount;
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    );
                });
            }
        }

        //90需求--付款订单列表
        $scope.orderListResults = [];
        $scope.financePayOrderListPayStatus = fixedValue.financePayOrderListPayStatus;
        $scope.selectedfinancePayOrderListPayStatus = $scope.financePayOrderListPayStatus[0];
        $scope.payOrderListfilterParam = {
            selectedfinancePayOrderListPayStatus: [],
            selectedOrderDateTime               : [],
            payOrderSelectedDepartureDate       : [],
            selectedOrderers                    : [],
            selectedPayPnrsOrder				: []
        };
        $scope.payOrderIsShowFilters = function () {
            return $scope.payOrderListfilterParam.selectedfinancePayOrderListPayStatus.length
                || $scope.payOrderListfilterParam.selectedOrderDateTime.length
                || $scope.payOrderListfilterParam.payOrderSelectedDepartureDate.length
                || $scope.payOrderListfilterParam.selectedPayPnrsOrder.length
                || $scope.payOrderListfilterParam.selectedOrderers.length;
        };

        //主过滤条件变化
        $scope.$on('payOrdermainFilter.change', function ($e) {
            if ($scope.selectedPaymentListType.code == '1') {
                $scope.filterParam.selectedFundsTypes.length = 0;
                $scope.filterParam.selectedPaymentStatus.length = 0;
                $scope.filterParam.selectedChannels.length = 0;
                $scope.filterParam.selectedTourOperators.length = 0;
                $scope.filterParam.selectedApplicants.length = 0;
                $scope.filterParam.selectedPayPnrs.length = 0;
                $scope.filterParam.selectedApprovalDates.length = 0;
                $scope.filterParam.selectedDepartureDates.length = 0;
                $scope.filterParam.selectedPayableAmounts.length = 0;
                $scope.filterParam.selectedPayAmounts.length = 0;

                $scope.$broadcast('mainSearch.init');
                $scope.searchParam = {searchType: '1', searchKey: ''};
                $scope.pageInfo.totalRowCount = 0;
                $scope.pageInfo.currentIndex = 1;
                $scope.requestPay('','filter');
            }
            else {
                $scope.payOrderListfilterParam.selectedOrderDateTime.length = 0;
                $scope.payOrderListfilterParam.payOrderSelectedDepartureDate.length = 0;
                $scope.payOrderListfilterParam.selectedOrderers.length = 0;
                $scope.payOrderListfilterParam.selectedPayPnrsOrder.length = 0;
                $scope.payOrderListfilterParam.selectedfinancePayOrderListPayStatus.length = 0;


                $scope.$broadcast('mainSearch.init');
                $scope.searchParam = {searchType: '1', searchKey: ''};
                $scope.payorder_pageInfo.totalRowCount = 0;
                $scope.payorder_pageInfo.currentIndex = 1;
                $scope.requestPayOrderList('','filter');
            }
        });
        //下单日期选择
        $scope.$on('selectedOrderDateTime.change', function () {
            //只能选择一个日期范围
            $scope.payOrderListfilterParam.selectedOrderDateTime = [
                {
                    startDate: $scope.selectedOrderDateTime.startDate,
                    endDate  : $scope.selectedOrderDateTime.endDate
                }];
            $scope.$broadcast('selectedOrderDateTime.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestPay();
        });

        //出团日期选择
        $scope.$on('selectedDepartureDates.change', function () {
            //$scope.filterParam.selectedDepartureDates.push(
            //    {
            //        startDate: $scope.selectedDepartureDate.startDate,
            //        endDate: $scope.selectedDepartureDate.endDate
            //    }
            //);
            //只能选择一个日期范围
            //modify by wlj at 2016.08.22  for bug 15490 -start
            // $scope.payOrderListfilterParam.selectedDepartureDates = [
            //modify by wlj at 2016.08.22  for bug 15490 -end
            $scope.filterParam.selectedDepartureDates = [
                {
                    startDate: $scope.selectedDepartureDates.startDate,
                    endDate  : $scope.selectedDepartureDates.endDate
                }];
            $scope.$broadcast('selectedDepartureDates.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestPay();
        });

      //PRN选择
        $scope.$on('selectedPayPnrOrder.confirm', function () {
            $scope.payOrderListfilterParam.selectedPayPnrsOrder.push($scope.selectedPayPnrOrder);
            console.log("$scope.payOrderListfilterParam.selectedPayPnrsOrder的长度-=============="+$scope.payOrderListfilterParam.selectedPayPnrsOrder.length);
            $scope.$broadcast('selectedPayPnrOrder.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        $scope.$on('payOrderSelectedDepartureDates.change', function () {
            //只能选择一个日期范围
            $scope.payOrderListfilterParam.payOrderSelectedDepartureDate = [
                {
                    startDate: $scope.payOrderSelectedDepartureDates.startDate,
                    endDate  : $scope.payOrderSelectedDepartureDates.endDate
                }];
            $scope.$broadcast('payOrderSelectedDepartureDates.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //分页信息变化
        $scope.$on('payOrderPagination.change', function ($e, flag) {
            if (flag == 'backFirstPage') {
                $scope.requestPayOrderList('', 'filter');
            }
            else {
                $scope.requestPayOrderList();
            }
        });
        //排序规则变化后
        $scope.$on('payOrderSort.change', function () {
            //$scope.requestPayOrderList('', 'filter');
            $scope.requestPayOrderList();
        });

        //过滤轨迹中删除 一个条件时
        $scope.$on('payOrderlistSearchFilter.remove', function () {
            if (!$scope.payOrderIsShowFilters()) {
                $scope.requestPayOrderList('', 'filter');
            }
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的下单人变化时
        $scope.$on('selectedOrderers.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中付款状态变化时
        //选中的下单人变化时
        $scope.$on('selectedfinancePayOrderListPayStatus.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //滤轨迹中清除所有条件时
        $scope.$on('payOrderlistSearchFilter.clearAll', function () {
            $scope.requestPayOrderList();
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        $scope.payOrderSortKeys = [
            {
                name: '下单日期',
                code: 'orderDateTime'
            },
            {
                name: '出团日期',
                code: 'departureDate'
            }
        ];
        $scope.payOrderSortInfo = {
            sortKey: 'orderDateTime',
            dec    : true
        };

        //合并后的子表格
        $scope.payorderSpreadSubTableId;
        $scope.initPayOrderSpreadSubTableIs = function (order) {
            order.qcSubTableIds = [
                'paymentMergeList' + order.orderUuid
            ];
        };
        //$scope.spreadSubTables = {};
        $scope.orderListToggleSubTable = function (qcSubTableId) {
            if ($scope.payorderSpreadSubTableId == qcSubTableId) {
                $scope.$broadcast('subTable.fold');
            }
            else {
                $scope.$broadcast('subTable.spread.ready', qcSubTableId);
            }
        };
        $scope.$on('subTable.spread.complete', function ($e, qcSubTableId) {
            $scope.payorderSpreadSubTableId = qcSubTableId;
        });
        $scope.$on('subTable.fold.complete', function ($e, qcSubTableId) {
            if ($scope.payorderSpreadSubTableId == qcSubTableId) {
                $scope.payorderSpreadSubTableId = undefined;
            }
        });
        $scope.$on('subTable.foldAll.request', function () {
            $scope.$broadcast('subTable.fold');
        });


        //批量付款成功后展开付款记录列表
        $scope.$on('finance.batchPayment.save', function ($e, orderUuid) {
            $scope.requestPayOrderList('paymentMergeList' + orderUuid);
        });


        $scope.requestPayOrderList = function (spreadTableId, flag) {
            $timeout(function () {
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        orderDateTime                 : joinDate($scope.payOrderListfilterParam.selectedOrderDateTime),
                        departureDate                 : joinDate($scope.payOrderListfilterParam.payOrderSelectedDepartureDate),
                        ordererId                     : joinedStringByArray($scope.payOrderListfilterParam.selectedOrderers, 'userId'),
                        pnrValue                      : joinedStringByArray($scope.payOrderListfilterParam.selectedPayPnrsOrder),
                        payment_orderPaymentStatusCode: joinedStringByArray($scope.payOrderListfilterParam.selectedfinancePayOrderListPayStatus, 'financePayOrderListPayStatusCode')
                    },
                    sortInfo   : $scope.payOrderSortInfo,
                    pageParam  : $scope.payorder_pageInfo
                };
                if (flag == 'filter') {
                    param.pageParam.currentIndex = 1;
                    $scope.payorder_pageInfo.currentIndex = 1;
                }
                qcApi.post('mtourfinance/getRefundOrderPage', param).success(function (result) {
                    //$scope.orderListResults.length = 0;
                    $scope.payorderSpreadSubTableId = '';
                    $scope.orderListResults = result.data.results;

                    if ($scope.orderListResults) {
                        angular.forEach($scope.orderListResults, function (result) {
                            if (result.payableAmount.length > 0) {
                                angular.forEach(result.payableAmount, function (amount) {
                                    if (amount.currencyUuid) {
                                        amount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
                                    }
                                });
                            }
                            if (result.paidAmount.length > 0) {
                                angular.forEach(result.paidAmount, function (amount) {
                                    if (amount.currencyUuid) {
                                        amount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
                                    }
                                });
                            }

                        });
                    }


                    $rootScope.$broadcast('qcTableContainer.reset');
                    $scope.payorder_pageInfo = result.data.page;
                    $scope.payorder_pageInfo.totalRowCount = result.data.page.totalRowCount;
                    $scope.payorder_pageInfo.currentIndex = result.data.page.currentIndex;
                    $scope.payorder_pageInfo.rowCount = +(result.data.page.rowCount);
                    spreadTableId = spreadTableId ? spreadTableId : $scope.payorderSpreadSubTableId;
                    if (spreadTableId) {
                        $timeout(function () {
                            $scope.$broadcast('subTable.spread.ready', spreadTableId);
                        }, 500);
                    }
                });
            });
        };

    }]);