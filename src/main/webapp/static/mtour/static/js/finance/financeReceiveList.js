var financeReceiveList = angular.module('financeReceiveList', ['mtour', 'finance', 'qc.uploader']);

financeReceiveList.controller('financeReceiveController', [
    '$scope', '$timeout', 'fixedValue', 'commonValue', 'joinedStringByArray', 'joinChannelAndTourOperator',
    'qcApi', 'amountsToRMB', 'qcObjectInArray', 'joinedStringByArray', 'joinAmount', 'joinDate', 'qcDialog', 'urlConfig', '$rootScope', 'qcMessage', 'checkRole',
    function ($scope, $timeout, fixedValue, commonValue, joinedStringByArray, joinChannelAndTourOperator,
              qcApi, amountsToRMB, qcObjectInArray, joinedStringByArray, joinAmount, joinDate,
              qcDialog, urlConfig, $rootScope, qcMessage, checkRole) {
        $scope.channelType=fixedValue.channelType;
        $scope.channelTypeSelf = $scope.channelType[0];
        qcApi.post('common/getCurrentUserInfo').success(function (result) {
            $scope.companyRoleCode = result.data.companyRoleCode;
        });

        //122需求-收款添加订单列表
        $scope.listTypes = [{
            code: '1', name: '款项列表'
        }];
        qcApi.post('common/getMenuByUser').success(function (result) {
            $scope.menus = result.data;
            angular.forEach($scope.menus, function (menu) {
                angular.forEach(menu.subMenus, function (subMenu) {
                    angular.forEach(subMenu.roles, function (role) {
                        if (role.roleCode.indexOf('mtourFinance:receive:orderList') != -1) {
                            $scope.listTypes.push({
                                code: '2', name: '订单列表'
                            });
                            return;
                        }
                    });
                });
            });
        });
        $scope.selectedListType = $scope.listTypes[0];


        $scope.isRole = function (url) {
            return checkRole(url);
        };


        $scope.searchParam = {searchType: '1', searchKey: ''};
        $scope.filterParam = {
            selectedTourOperatorOrChannel: {},
            selectedChannels             : [],
            selectedTourOperators        : [],
            selectedDepartureDates       : [],
            selectedReceivedAmounts      : [],
            selectedReceiveDates         : [],
            selectedArrivalBankDates     : [],
            selectedReceiveCompanys      : [],
            selectedReceivers            : [],
            selectedReceiveTypes         : [],
            selectedReceiveStatus        : []
        };
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
                name: '收款时间',
                code: 'receiveDate'
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
            sortKey: 'receiveDate',
            dec    : true
        };

        $scope.isShowFilters = function () {
            return $scope.filterParam.selectedChannels.length
                || $scope.filterParam.selectedTourOperators.length
                || $scope.filterParam.selectedDepartureDates.length
                || $scope.filterParam.selectedReceivedAmounts.length
                || $scope.filterParam.selectedReceiveDates.length
                || $scope.filterParam.selectedArrivalBankDates.length
                || $scope.filterParam.selectedReceiveCompanys.length
                || $scope.filterParam.selectedReceivers.length
                || $scope.filterParam.selectedReceiveTypes.length
                || $scope.filterParam.selectedReceiveStatus.length;
        }


        //主过滤条件变化
        $scope.$on('mainFilter.change', function ($e) {
            if ($scope.selectedListType.code == '1') {
                $scope.filterParam.selectedChannels.length = 0;
                $scope.filterParam.selectedTourOperators.length = 0;
                $scope.filterParam.selectedDepartureDates.length = 0;
                $scope.filterParam.selectedReceivedAmounts.length = 0;
                $scope.filterParam.selectedReceiveDates.length = 0;
                $scope.filterParam.selectedArrivalBankDates.length = 0;
                $scope.filterParam.selectedReceiveCompanys.length = 0;
                $scope.filterParam.selectedReceivers.length = 0;
                $scope.filterParam.selectedReceiveTypes.length = 0;
                $scope.filterParam.selectedReceiveStatus.length = 0;
                $scope.$broadcast('mainSearch.init');
                $scope.searchParam = {searchType: '1', searchKey: ''};
                $scope.pageInfo.totalRowCount = 0;
                $scope.pageInfo.currentIndex = 1
                $scope.requestReceive();
            }
            else {
                $scope.receiveOrderListfilterParam.selectedOrderDateTime.length = 0;
                $scope.receiveOrderListfilterParam.receiveOrder_selectedDepartureDate.length = 0;
                $scope.receiveOrderListfilterParam.selectedOrderers.length = 0;
                $scope.receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus.length = 0;
                $scope.$broadcast('mainSearch.init');
                $scope.searchParam = {searchType: '1', searchKey: ''};
                $scope.payorder_pageInfo.totalRowCount = 0;
                $scope.payorder_pageInfo.currentIndex = 1
                $scope.requestReceiveOrderList();
            }

            //$scope.requestCount();
        });

        //点击主搜索框的查询按钮后
        $scope.$on('mainSearch.search', function ($e, searchParam) {
            $scope.searchParam = searchParam;
            if ($scope.selectedListType.code == '1') {
                $scope.filterParam.selectedChannels.length = 0;
                $scope.filterParam.selectedTourOperators.length = 0;
                $scope.filterParam.selectedDepartureDates.length = 0;
                $scope.filterParam.selectedReceivedAmounts.length = 0;
                $scope.filterParam.selectedReceiveDates.length = 0;
                $scope.filterParam.selectedArrivalBankDates.length = 0;
                $scope.filterParam.selectedReceiveCompanys.length = 0;
                $scope.filterParam.selectedReceivers.length = 0;
                $scope.filterParam.selectedReceiveTypes.length = 0;
                $scope.filterParam.selectedReceiveStatus.length = 0;

                $scope.pageInfo.totalRowCount = 0;
                $scope.pageInfo.currentIndex = 1
                $scope.requestReceive();
            }
            else {
                $scope.receiveOrderListfilterParam.selectedOrderDateTime.length = 0;
                $scope.receiveOrderListfilterParam.receiveOrder_selectedDepartureDate.length = 0;
                $scope.receiveOrderListfilterParam.selectedOrderers.length = 0;
                $scope.receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus.length = 0;

                $scope.payorder_pageInfo.totalRowCount = 0;
                $scope.payorder_pageInfo.currentIndex = 1;
                $scope.requestReceiveOrderList();
            }

            //$scope.requestCount();
        });

        //分页信息变化
        $scope.$on('pagination.change', function () {
            if (flag == 'backFirstPage') {
                $scope.requestReceive('filter');
            }
            else {
                $scope.requestReceive();
            }

        });
        //排序规则变化后
        $scope.$on('sort.change', function () {
            $scope.requestReceive();
        });

        //过滤轨迹中删除 一个条件时
        $scope.$on('searchFilter.remove', function () {
            if (!$scope.isShowFilters()) {
                $scope.requestReceive();
            }
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //滤轨迹中清除所有条件时
        $scope.$on('searchFilter.clearAll', function () {
            $scope.requestReceive();
            $rootScope.$broadcast('qcTableContainer.reset');
        });
        //分页信息变化
        $scope.$on('pagination.change', function () {
            $scope.requestReceive();
        });
        //选中的渠道变化时
        $scope.$on('selectedChannels.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });
        //选中的地接社变化时
        $scope.$on('selectedTourOperators.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的收款确认状态变化时--122需求
        $scope.$on('selectedReceiveStatus.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的下单人变化时

        //选中的收款类别变化时
        $scope.$on('selectedReceiveTypes.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //出团日期选择
        $scope.$on('selectedDepartureDate.change', function () {
            //$scope.filterParam.selectedDepartureDates.push(
            //    {
            //        startDate: $scope.selectedDepartureDate.startDate,
            //        endDate: $scope.selectedDepartureDate.endDate
            //    }
            //);
            //只能选择一个日期范围
            $scope.filterParam.selectedDepartureDates = [
                {
                    startDate: $scope.selectedDepartureDate.startDate,
                    endDate  : $scope.selectedDepartureDate.endDate
                }];
            $scope.$broadcast('selectedDepartureDate.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestReceive();
        });

        //已收金额选择
        $scope.$on('selectedReceivedAmount.confirm', function () {
            if ($scope.filterParam.selectedReceivedAmounts.length) {
                $scope.filterParam.selectedReceivedAmounts[0] = {
                    minAmount: $scope.selectedReceivedAmount.minAmount,
                    maxAmount: $scope.selectedReceivedAmount.maxAmount
                };
            } else {
                $scope.filterParam.selectedReceivedAmounts.push(
                    {
                        minAmount: $scope.selectedReceivedAmount.minAmount,
                        maxAmount: $scope.selectedReceivedAmount.maxAmount
                    }
                );
            }

            $scope.$broadcast('selectedReceivedAmount.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestReceive();
        });

        //收款日期选择
        $scope.$on('selectedReceiveDate.change', function () {
//            $scope.filterParam.selectedReceiveDates.push(
//                {
//                    startDate: $scope.selectedReceiveDate.startDate,
//                    endDate: $scope.selectedReceiveDate.endDate
//                }
//            );
            $scope.filterParam.selectedReceiveDates = [
                {
                    startDate: $scope.selectedReceiveDate.startDate,
                    endDate  : $scope.selectedReceiveDate.endDate
                }
            ];
            $scope.$broadcast('selectedReceiveDate.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestReceive();
        });
        //到账日期选择
        $scope.$on('selectedArrivalBankDate.change', function () {
            $scope.filterParam.selectedArrivalBankDates = [
                {
                    startDate: $scope.selectedArrivalBankDate.startDate,
                    endDate  : $scope.selectedArrivalBankDate.endDate
                }
            ];
            $scope.$broadcast('selectedArrivalBankDate.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestReceive();
        });

        //付款单位选择
        $scope.$on('selectedReceiveCompany.confirm', function () {
            $scope.filterParam.selectedReceiveCompanys.push($scope.selectedReceiveCompany);
            $scope.$broadcast('selectedReceiveCompany.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
            //$scope.requestReceive();
        });
        //选中的收款人变化时
        $scope.$on('selectedReceivers.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        $scope.requestReceive = function (flag) {
            $timeout(function () {
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        receiveTypeCode      : joinedStringByArray($scope.filterParam.selectedReceiveTypes, 'receiveTypeCode'),
                        tourOperatorOrChannel: joinChannelAndTourOperator($scope.filterParam.selectedChannels, $scope.filterParam.selectedTourOperators),
                        receiveStatusCode    : joinedStringByArray($scope.filterParam.selectedReceiveStatus, 'receiveStatusCode'),
                        departureDate        : joinDate($scope.filterParam.selectedDepartureDates),
                        receivedAmount       : joinAmount($scope.filterParam.selectedReceivedAmounts),
                        receiveDate          : joinDate($scope.filterParam.selectedReceiveDates),
                        arrivalBankDate      : joinDate($scope.filterParam.selectedArrivalBankDates),
                        //到账日期
                        payer                : joinedStringByArray($scope.filterParam.selectedReceiveCompanys),
                        receiver             : joinedStringByArray($scope.filterParam.selectedReceivers, 'userId')
                    },
                    sortInfo   : $scope.sortInfo,
                    pageParam  : $scope.pageInfo
                };
                if (flag == 'filter') {
                    param.pageParam.currentIndex = 1;
                    $scope.pageInfo.currentIndex = 1;
                }
                qcApi.post('mtourfinance/showOrderList', param).success(function (result) {
                    $scope.results.length = 0;
                    $scope.results = result.data.results;
                    angular.forEach($scope.results, function (receive) {

                        //开发后台传过来的是code
                        //var receiveType = qcObjectInArray(fixedValue.receiveType, 'receiveTypeCode', +receive.receiveTypeName);
                        //if (receiveType) {
                        //    receive.receiveTypeName = receiveType.receiveTypeName;
                        //}
                        var receiveStatus = qcObjectInArray(fixedValue.financeReceiveStatus, 'receiveStatusCode', receive.receiveStatusCode);
                        if (receiveStatus) {
                            receive.receiveStatusName = receiveStatus.receiveStatusName;
                        }
                        angular.forEach(receive.orderAmount, function (amount) {

                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = '¥';
                            }

                        });
                        angular.forEach(receive.totalArrivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = '¥';
                            }
                        });
                        angular.forEach(receive.receivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = currency.currencyCode;
                            }
                        });
                        angular.forEach(receive.arrivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            if (currency != undefined) {
                                amount.currencyCode = currency.currencyCode;
                            }
                        });
                    });
                    $rootScope.$broadcast('qcTableContainer.reset');
                    $scope.pageInfo.totalRowCount = result.data.page.totalRowCount;
                    $scope.pageInfo.currentIndex = result.data.page.currentIndex;
                    $scope.pageInfo.rowCount = +(result.data.page.rowCount);
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
        $scope.requestReceive();
        //$scope.requestCount();

        //确认收款
        $scope.receiveConfirmPop = function (receive) {
            var _self=this;
            qcApi.post('mtourfinance/getReceivedOrderInfo', {
                orderUuid  : receive.orderUuid,
                receiveUuid: receive.receiveUuid,
                fundsType  : receive.receiveFundsType.receiveFundsTypeCode
            }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/receiveConfirm.html',
                            controller : ['$scope', function (dialogScope) {
                                //qcApi.post('common/getCurrentUserInfo').success(function (result) {
                                //    dialogScope.companyRoleCode = result.data.companyRoleCode;
                                //});
                                dialogScope.defaultCurrency = commonValue.defaultCurrency;
                                dialogScope.companyRoleCode = $scope.companyRoleCode;
                                dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                dialogScope.receiveInfo = result.data;
                                dialogScope.tourOperatorChannelCategoryCode = dialogScope.receiveInfo.tourOperatorChannelCategoryCode;
                                dialogScope.touroperatorUUid = dialogScope.receiveInfo.tourOperatorOrChannelUuid;
                                dialogScope.receiveInfo.receiveFundsTypeCode = receive.receiveFundsType.receiveFundsTypeCode;
                                dialogScope.receiveMethod = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', dialogScope.receiveInfo.receiveMethodCode);
                                dialogScope.receiveCurrency = qcObjectInArray(commonValue.currencies, 'currencyUuid', dialogScope.receiveInfo.currencyUuid);
                                dialogScope.convertedCurrency = commonValue.defaultCurrency;
                                //货币换算
                                dialogScope.convertedExchangeRate = function () {
                                    return dialogScope.receiveInfo.exchangeRate;
                                };
                                dialogScope.convertedAmount = function () {
                                    return dialogScope.receiveInfo.receiveAmount != undefined ? dialogScope.receiveInfo.receiveAmount * dialogScope.convertedExchangeRate() : "";
                                }
                                //获取登录用户信息 包括收款行信息
                                dialogScope.userInfo = $rootScope.userInfo;
                                dialogScope.selectedReceiveBank = (dialogScope.receiveInfo.receiveBank != undefined && dialogScope.receiveInfo.receiveBank != "") ? qcObjectInArray(dialogScope.userInfo.banks, 'bankName', dialogScope.receiveInfo.receiveBank) : '';
                                dialogScope.selectedReceiveAccount = (dialogScope.selectedReceiveBank != "" && dialogScope.selectedReceiveBank != undefined) ? qcObjectInArray(dialogScope.selectedReceiveBank.accounts, 'accountNo', dialogScope.receiveInfo.receiveAccount) : '';
                                dialogScope.$on('selectedReceiveBank.change', function () {
                                    dialogScope.selectedReceiveAccount = null;
                                });
                                qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: dialogScope.receiveInfo.orderUuid}).success(function (result) {
                                    dialogScope.bigCodes = result.data;
                                    //dialogScope.selectedInvoiceOriginal = qcObjectInArray(dialogScope.bigCodes.invoiceOriginals, 'invoiceOriginalTypeCode', dialogScope.OrderCostDetail.invoiceOriginalTypeCode);
                                    dialogScope.channelUuid = dialogScope.bigCodes.channel.channelUuid;
                                    dialogScope.channelTypeCode = dialogScope.bigCodes.channelType.channelTypeCode;
                                    //获取付款行信息
                                    //其他收入收款的渠道或者订单收款（订单收款不分渠道地接社）
                                    if (!dialogScope.tourOperatorChannelCategoryCode || dialogScope.tourOperatorChannelCategoryCode == 2) {
                                        qcApi.post('common/getBankInfo', {
                                            //type: dialogScope.bigCodes.tourOperatorChannelCategoryCode,
                                            type: '2',
                                            uuid: dialogScope.channelUuid
                                        })
                                            .success(function (result) {
                                                dialogScope.paymentBanks = result.data;
                                                if (dialogScope.channelTypeCode != '2') {
                                                    dialogScope.selectedPayBank = qcObjectInArray(dialogScope.paymentBanks, 'bankName', dialogScope.receiveInfo.paymentBank);
                                                    dialogScope.selectedPayAccount = dialogScope.selectedPayBank != undefined ? qcObjectInArray(dialogScope.selectedPayBank.accounts, 'accountNo', dialogScope.receiveInfo.paymentAccount) : '';
                                                } else {
                                                    dialogScope.selectedPayBankName = dialogScope.receiveInfo.paymentBank;
                                                    dialogScope.selectedPayAccountNo = dialogScope.receiveInfo.paymentAccount;
                                                }
                                            }
                                        )
                                        ;
                                    }
                                    else {//其他收入收款的地接社
                                        qcApi.post('common/getBankInfo', {
                                            //type: dialogScope.bigCodes.tourOperatorChannelCategoryCode,
                                            type: '1',
                                            uuid: dialogScope.touroperatorUUid
                                        })
                                            .success(function (result) {
                                                dialogScope.paymentBanks = result.data;
                                                dialogScope.selectedPayBank = qcObjectInArray(dialogScope.paymentBanks, 'bankName', dialogScope.receiveInfo.paymentBank);
                                                dialogScope.selectedPayAccount = dialogScope.selectedPayBank != undefined ? qcObjectInArray(dialogScope.selectedPayBank.accounts, 'accountNo', dialogScope.receiveInfo.paymentAccount) : '';
                                            }
                                        )
                                        ;
                                    }

                                });
                                function getPaymentBankName() {
                                    if (dialogScope.channelTypeCode == '2') {
                                        return dialogScope.selectedPayBankName;
                                    } else {
                                        return dialogScope.selectedPayBank ? dialogScope.selectedPayBank.bankName : '';
                                    }
                                }

                                function getPaymentAccountNo() {
                                    if (dialogScope.channelTypeCode == '2') {
                                        return dialogScope.selectedPayAccountNo;
                                    } else {
                                        return dialogScope.selectedPayAccount ? dialogScope.selectedPayAccount.accountNo : '';
                                    }
                                }

                                dialogScope.receiveConfirm = function (receiveInfo) {
                                    dialogScope.newReceive = {
                                        orderUuid           : dialogScope.receiveInfo.orderUuid,
                                        receiveUuid         : dialogScope.receiveInfo.receiveUuid,
                                        payer               : dialogScope.receiveInfo.payer,
                                        arrivalBankDate     : dialogScope.receiveInfo.arrivalBankDate,
                                        exchange            : dialogScope.receiveInfo.exchangeRate,
                                        totalPrice          : dialogScope.convertedAmount(),
                                        //paymentBank: dialogScope.selectedPayBank.bankName,//收款方式为汇款时有效
                                        //paymentAccount: dialogScope.selectedPayAccount.accountNo,//收款方式为汇款时有效
                                        //receiveBank: dialogScope.selectedReceiveBank.bankName,//收款方式为汇款时有效
                                        //receiveAccount: dialogScope.selectedReceiveAccount.accountNo,
                                        attachments         : dialogScope.receiveInfo.attachments,
                                        receiveFundsTypeCode: receiveInfo.receiveFundsTypeCode
                                    };
                                    if ($scope.companyRoleCode != '0') {
                                        if (!dialogScope.receiveInfo.payer) {
                                            qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                            dialogScope.$broadcast('qcValid.check');
                                            return;
                                        }
                                    }
                                    if (receiveInfo.receiveMethodCode == "1") {
                                        dialogScope.newReceive.checkNo = dialogScope.receiveInfo.checkNo;
                                        dialogScope.newReceive.checkIssueDate = dialogScope.receiveInfo.checkIssueDate;//收款方式为支票时有效
                                    }
                                    if (receiveInfo.receiveMethodCode == "4") {
                                        dialogScope.newReceive.paymentBank = getPaymentBankName();
                                        dialogScope.newReceive.paymentAccount = getPaymentAccountNo();
                                        dialogScope.newReceive.receiveBank = dialogScope.selectedReceiveBank != undefined ? dialogScope.selectedReceiveBank.bankName : '';
                                        dialogScope.newReceive.receiveAccount = dialogScope.selectedReceiveAccount != undefined ? dialogScope.selectedReceiveAccount.accountNo : '';
                                    }
                                    qcApi.post('mtourfinance/payedConfirm', dialogScope.newReceive
                                    ).success(function (result) {
                                            qcMessage.tip('财务确认收款成功');
                                            dialogScope.closeThisDialog();
                                            $scope.requestReceive();
                                       // modify by wlj at 2016.09.20 for bug 15933
                                        angular.forEach(_self.results, function (result) {
                                            if(result.receiveUuid==receive.receiveUuid){
                                                result.receiveStatusCode=1;
                                            }
                                        });
                                        // modify by wlj at 2016.09.20 for bug 15933
                                        });
                                }
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    )
                    ;
                }
            )
            ;
        }
        ;

//显示详情 订单收款/其他收入收款
        $scope.showReceiveDetail = function (receive) {
            if (receive.receiveFundsType.receiveFundsTypeCode == "1") {
                qcApi.post('mtourfinance/getPayedConfirmDetail', {
                    //qcApi.post('order/getOrderReceiptDetail', {
                    orderUuid  : receive.orderUuid,
                    receiveUuid: receive.receiveUuid
                }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReceiveDetail.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                dialogScope.OrderReceiveDetail = result.data;
                                //dialogScope.OrderReceiveDetail.receiveTypeCode

                                dialogScope.OrderReceiveDetail.receiveTypeCode = dialogScope.OrderReceiveDetail.receiveType;
                                dialogScope.OrderReceiveDetail.paymentMethodCode = dialogScope.OrderReceiveDetail.receiveMethod;


                                dialogScope.receiveType = qcObjectInArray(fixedValue.receiveType, 'receiveTypeCode', (+dialogScope.OrderReceiveDetail.receiveType));
                                //dialogScope.OrderReceiveDetail.paymentMethodCode
                                dialogScope.paymentMethodType = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', dialogScope.OrderReceiveDetail.receiveMethod);
                                dialogScope.selectedCurrency = qcObjectInArray(commonValue.currencies, 'currencyUuid', +dialogScope.OrderReceiveDetail.currencyUuid);
                                dialogScope.convertedCurrency = commonValue.defaultCurrency;

                                //货币换算
                                dialogScope.convertedExchangeRate = function () {
                                    return dialogScope.OrderReceiveDetail.exchangeRate;
                                };
                                dialogScope.convertedAmount = function () {
                                    return dialogScope.OrderReceiveDetail.receiveAmount != undefined ? dialogScope.OrderReceiveDetail.receiveAmount * dialogScope.convertedExchangeRate() : "";
                                }


                                //撤销
                                dialogScope.cancel = function (OrderReceiveDetail) {
                                    qcDialog.openMessage({
                                        msg : '是否撤销',
                                        type: 'confirm'
                                    }).then(function () {
                                        qcApi.post('mtourfinance/cancelReceive', {
                                            orderUuid  : receive.orderUuid,
                                            receiveUuid: receive.receiveUuid
                                        }).success(function (result) {
                                            $scope.requestReceive();
                                        });
                                    });
                                };
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    );
                });
            }
            else {
                qcApi.post('mtourfinance/getCostReceiptDetail', {
                    orderUuid  : receive.orderUuid,
                    receiveUuid: receive.receiveUuid
                }).success(function (result) {
                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/otherRevenueDetail.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                dialogScope.OtherRevenueDetail = result.data;
                                dialogScope.currencies = commonValue.currencies;
                                dialogScope.selectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', dialogScope.OtherRevenueDetail.currencyUuid);
                                dialogScope.convertedSelectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', dialogScope.OtherRevenueDetail.convertedCurrencyUuid);
                                dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;
                                dialogScope.selectedTourType = qcObjectInArray(dialogScope.tourOperatorTypes, 'tourOperatorTypeCode', dialogScope.OtherRevenueDetail.tourOperatorOrChannelTypeCode);
                                dialogScope.channelTypes = fixedValue.channelType;
                                dialogScope.selectedChannelType = qcObjectInArray(dialogScope.channelTypes, 'channelTypeCode', dialogScope.OtherRevenueDetail.tourOperatorOrChannelTypeCode);
                                dialogScope.paymentMethod = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', dialogScope.OtherRevenueDetail.paymentMethodCode.toString());
                            }
                            ],
                            width      : 500,
                            plain      : false
                        }
                    );
                });
            }
        };

//驳回
        $scope.reject = function (receive) {
            qcDialog.openMessage({
                msg : '是否确认驳回',
                type: 'confirm'
            }).then(function () {
                qcApi.post('mtourfinance/confirmRejectOper', {
                    orderUuid  : receive.orderUuid,
                    receiveUuid: receive.receiveUuid,
                    fundsType  : receive.receiveFundsType.receiveFundsTypeCode
                }).success(function (result) {
                    $scope.requestReceive();
                });
            });
        };

//撤销
        $scope.cancel = function (receive) {
            qcDialog.openMessage({
                msg : '是否取消确认',
                type: 'confirm'
            }).then(function () {
                qcApi.post('mtourfinance/cancelReceive', {
                    orderUuid  : receive.orderUuid,
                    receiveUuid: receive.receiveUuid,
                    fundsType  : receive.receiveFundsType.receiveFundsTypeCode
                }).success(function (result) {
                    qcMessage.tip('取消成功');
                    $scope.requestReceive();
                });
            });
        };


        //122需求--订单列表
        $scope.receiveOrderListfilterParam = {
            selectedOrderDateTime                       : [],
            receiveOrder_selectedDepartureDate          : [],
            selectedOrderers                            : [],
            selectedfinanceReceiveOrderListReceiveStatus: []
        };
        $scope.receiveOrderList = [];


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

        $scope.receiveOrderisShowFilters = function () {
            return $scope.receiveOrderListfilterParam.selectedOrderDateTime.length
                || $scope.receiveOrderListfilterParam.receiveOrder_selectedDepartureDate.length
                || $scope.receiveOrderListfilterParam.selectedOrderers.length
                || $scope.receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus.length;
        };
        //下单日期选择
        $scope.$on('selectedOrderDateTime.change', function () {
            //只能选择一个日期范围
            $scope.receiveOrderListfilterParam.selectedOrderDateTime = [
                {
                    startDate: $scope.selectedOrderDateTime.startDate,
                    endDate  : $scope.selectedOrderDateTime.endDate
                }];
            $scope.$broadcast('selectedOrderDateTime.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //出团日期选择
        $scope.$on('receiveOrder_selectedDepartureDate.change', function () {
            //只能选择一个日期范围
            $scope.receiveOrderListfilterParam.receiveOrder_selectedDepartureDate = [
                {
                    startDate: $scope.receiveOrder_selectedDepartureDate.startDate,
                    endDate  : $scope.receiveOrder_selectedDepartureDate.endDate
                }];
            $scope.$broadcast('receiveOrder_selectedDepartureDate.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //分页信息变化
        $scope.$on('payOrderPagination.change', function ($e, flag) {
            if (flag == 'backFirstPage') {
                $scope.requestReceiveOrderList('filter');
            }
            else {
                $scope.requestReceiveOrderList();
            }
        });
        //排序规则变化后
        $scope.$on('payOrderSort.change', function () {
            //$scope.requestReceiveOrderList( 'filter');
            $scope.requestReceiveOrderList();
        });


        //过滤轨迹中删除 一个条件时
        $scope.$on('payOrderlistSearchFilter.remove', function () {
            if (!$scope.receiveOrderisShowFilters()) {
                $scope.requestReceiveOrderList('filter');
            }
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的下单人变化时
        $scope.$on('selectedOrderers.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中付款状态变化时
        $scope.$on('selectedfinanceReceiveOrderListReceiveStatus.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //滤轨迹中清除所有条件时
        $scope.$on('payOrderlistSearchFilter.clearAll', function () {
            $scope.requestReceiveOrderList();
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //合并后的子表格
        $scope.receiveOrderSpreadSubTableId;
        $scope.initReceiveOrderSpreadSubTableIs = function (order) {
            order.qcSubTableIds = [
                'receiveList' + order.orderUuid
            ];
        };
        //$scope.spreadSubTables = {};
        $scope.orderListToggleSubTable = function (qcSubTableId) {
            if ($scope.receiveOrderSpreadSubTableId == qcSubTableId) {
                $scope.$broadcast('subTable.fold');
            }
            else {
                $scope.$broadcast('subTable.spread.ready', qcSubTableId);
            }
        };
        $scope.$on('subTable.spread.complete', function ($e, qcSubTableId) {
            $scope.receiveOrderSpreadSubTableId = qcSubTableId;
        });
        $scope.$on('subTable.fold.complete', function ($e, qcSubTableId) {
            if ($scope.receiveOrderSpreadSubTableId == qcSubTableId) {
                $scope.receiveOrderSpreadSubTableId = undefined;
            }
        });
        $scope.$on('subTable.foldAll.request', function () {
            $scope.$broadcast('subTable.fold');
        });
        $scope.receiveOrderList = [];
        $scope.requestReceiveOrderList = function (flag) {
            $timeout(function () {
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        orderDateTime                   : joinDate($scope.receiveOrderListfilterParam.selectedOrderDateTime),
                        departureDate                   : joinDate($scope.receiveOrderListfilterParam.receiveOrder_selectedDepartureDate),
                        ordererId                       : joinedStringByArray($scope.receiveOrderListfilterParam.selectedOrderers, 'userId'),
                        orderReceiveStatusCodeStatusCode: joinedStringByArray($scope.receiveOrderListfilterParam.selectedfinanceReceiveOrderListReceiveStatus, 'receiveStatusCode')
                    },
                    sortInfo   : $scope.payOrderSortInfo,
                    pageParam  : $scope.payorder_pageInfo
                };
                if (flag == 'filter') {
                    param.pageParam.currentIndex = 1;
                    $scope.payorder_pageInfo.currentIndex = 1;
                }
                qcApi.post('mtourfinance/receiveOrderList', param).success(function (result) {
                    $scope.receiveOrderSpreadSubTableId = '';
                    $scope.receiveOrderList.length = 0;
                    $scope.receiveOrderList = result.data;
                    $rootScope.$broadcast('qcTableContainer.reset');
                    $scope.payorder_pageInfo = $scope.receiveOrderList.page;
                    $scope.payorder_pageInfo.totalRowCount = $scope.receiveOrderList.page.totalRowCount;
                    $scope.payorder_pageInfo.currentIndex = $scope.receiveOrderList.page.currentIndex;
                    $scope.payorder_pageInfo.rowCount = +($scope.receiveOrderList.page.rowCount);
                    angular.forEach($scope.receiveOrderList.results, function (receive) {
                        var orderReceiveStatusCodeStatus = qcObjectInArray(fixedValue.orderReceiveStatus, 'receiveStatusCode', receive.orderReceiveStatusCodeStatusCode);
                        if (orderReceiveStatusCodeStatus) {
                            receive.orderReceiveStatusCodeStatusName = orderReceiveStatusCodeStatus.receiveStatusName;
                        }
                    });

                    //$scope.receiveOrderList = {
                    //    results: [//表格的输出结果
                    //        {
                    //            orderUuid                       : 4788,
                    //            groupNo                         : 'JP240445001160226',
                    //            orderDateTime                   : '2016-02-26',
                    //            departureDate                   : '2016-02-26',
                    //            orderer                         : '美途国际管理员（测试专用）美途国际管理员',
                    //            //totalArriveAmount               : [//累计到账总额'
                    //            //    {
                    //            //        currencyUuid: '108',
                    //            //        amount      : '1000'
                    //            //    }
                    //            //],
                    //            //orderAmount                     : [//订单总额
                    //            //    {
                    //            //        currencyUuid: '127',
                    //            //        amount      : '456'
                    //            //    }
                    //            //],
                    //            totalArriveAmount               : '累计到账金额',
                    //            orderAmount                     : '订单总额',
                    //            orderReceiveStatusCodeStatusCode: '1',//0-待收款,1-部分定金，2-已收定金,3-已收全款
                    //            orderReceiveStatusCodeStatusName: '部分定金'
                    //        }
                    //    ],
                    //    page   : {
                    //        totalRowCount: 25,
                    //        currentIndex : 1,
                    //        rowCount     : 20
                    //    }
                    //};


                    //spreadTableId = spreadTableId ? spreadTableId : $scope.payorderSpreadSubTableId;
                    //});
                });
            });
        };
    }
])
;

financeReceiveList.directive('receiveList', ['urlConfig','qcDialog','qcMessage', '$window', 'checkRole', 'qcApi', 'qcObjectInArray', 'commonValue', 'fixedValue', function (urlConfig,qcDialog,qcMessage, $window, checkRole, qcApi, qcObjectInArray, commonValue, fixedValue) {
    return {
        restrict   : 'A',
        scope      : {
            orderUuid: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/receive-list.html',
        link       : function (scope, ele, attrs) {
            qcApi.post('mtourfinance/getReceiveOrderSubList', {orderUuid: scope.orderUuid}).success(function (result) {
                scope.results = result.data;
                angular.forEach(scope.results, function (result) {
                    angular.forEach(result.receivedAmount, function (amount) {
                        var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                        if (currency != undefined) {
                            amount.currencyCode = currency.currencyCode;
                        }

                    });
                    angular.forEach(result.arrivedAmount, function (amount) {
                        var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                        if (currency != undefined) {
                            amount.currencyCode = currency.currencyCode;
                        }
                    });
                    result.receiveStatusName = qcObjectInArray(fixedValue.financeReceiveStatus, 'receiveStatusCode', result.receiveStatusCode).receiveStatusName;
                });
            });
            scope.isRole = function (url) {
                return checkRole(url);
            };

            //scope.results = [{
            //    receiveUuid              : '1933',
            //    receiveFundsTypeCode     : '5',
            //    receiveDate              : '2016-01-19',
            //    arrivalBankDate          : '2016-01-19',
            //    orderUuid                : '4788',
            //    fundsName                : 'dw',
            //    receiveTypeName          : '其他收入',
            //    receiveTypeCode          : '5',
            //    receiveFundsType         : {
            //        receiveFundsTypeCode: '2',
            //        receiveFundsTypeName: '其他收入收款'
            //    },
            //    tourOperatorOrChannelName: '大渠道公司',
            //    paymentCompany           : '付款单位',
            //    receiver                 : '美途国际',
            //    receiveStatusCode        : '1',
            //    receivedAmount           : '$1000',
            //    arrivedAmount            : '￥200'
            //}];
            scope.receiveorder_cancel = function (receive) {
                //modify by wlj at 2016.09.20 for bug 15933 -start
                qcDialog.openMessage({
                    msg : '是否取消确认',
                    type: 'confirm'
                }).then(function () {
                    qcApi.post('mtourfinance/cancelReceive', {
                        orderUuid  : receive.orderUuid,
                        receiveUuid: receive.receiveUuid,
                        fundsType  : receive.receiveFundsType.receiveFundsTypeCode
                    }).success(function (result) {
                        qcMessage.tip('取消成功');
                        // scope.requestReceive();
                         angular.forEach(scope.results, function (result) {
                         if(result.receiveUuid==receive.receiveUuid){
                         result.receiveStatusCode=99;
                         }
                         });
                    });
                });

                // scope.$parent.cancel(receive);
                //modify by wlj at 2016.09.20 for bug 15933 -end
            };

            scope.receiveorder_showReceiveDetail = function (receive) {
                scope.$parent.showReceiveDetail(receive);
            };

            scope.receiveorder_receiveConfirmPop = function (receive) {
                // scope.$parent.receiveConfirmPop(receive);

                scope.$parent.receiveConfirmPop.call(scope,receive);

                //modify by wlj at 2016.09.20 for bug 15933 -start
                /*angular.forEach(scope.results, function (result) {
                    if(result.receiveUuid==receive.receiveUuid){
                        result.receiveStatusCode=1;
                    }
                });*/
                //modify by wlj at 2016.09.20 for bug 15933 -end
            };

            scope.receiveorder_reject = function (receive) {
                scope.$parent.reject(receive);
            };
        }
    };
}]);


