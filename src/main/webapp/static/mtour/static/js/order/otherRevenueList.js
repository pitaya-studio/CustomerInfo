orderList.directive('otherRevenueList', ['$rootScope', 'urlConfig', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi', 'fixedValue', 'checkRole',
    function ($rootScope, urlConfig, commonValue, qcObjectInArray, qcDialog, qcApi, fixedValue, checkRole) {
        return {
            restrict   : 'A',
            scope      : {
                orderUuid: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/otherRevenueList.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;//登录用户是否为美途
                scope.toggleSubTable = function () {
                    scope.$emit('subTable.toggle.request', 'otherRevenueList' + scope.orderUuid);
                };

                scope.isRole = function (url) {
                    return checkRole(url);
                };

                
                otherRevenueList();
                function otherRevenueList() {
                    //查询列表数据
                    if (!scope.newList) {
                        qcApi.post('mtourfinance/getOtherCostRecords', {
                            orderUuid: scope.orderUuid
                        }).success(function (result) {
                            scope.newList = result.data;
                            angular.forEach(scope.newList, function (otherRevenue) {
                                otherRevenue.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', otherRevenue.currencyUuid);
                                otherRevenue.convertedCurrency = commonValue.defaultCurrency;
                            });
                        });
                    }
                }

                scope.showOtherRevenueDetail = function (otherRevenue) {
                    qcApi.post('order/getOtherReceiptDetail', {
                        otherRevenueUuid: otherRevenue.otherRevenueUuid
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
                };
                scope.deleteOtherRevenue = function (otherRevenue) {
                    qcDialog.openMessage({
                        msg : '是否删除',
                        type: 'confirm'
                    }).then(function () {
                        qcApi.post('order/deleteCostRecord', {
                            otherRevenueUuid: otherRevenue.otherRevenueUuid
                        }).success(function (result) {
                            $rootScope.$broadcast('order.otherRevenue.delete', otherRevenue.orderUuid);
                        });
                    });

                };
                scope.submitOtherRevenue = function (otherRevenue) {
                    qcApi.post('mtourfinance/submitCostRecord', {
                        otherRevenueUuid: otherRevenue.otherRevenueUuid
                    }).success(function (result) {
                        $rootScope.$broadcast('order.otherRevenue.commit', otherRevenue.orderUuid);
                    });
                };
                scope.cancelOtherRevenue = function (otherRevenue) {
                    qcDialog.openMessage({
                        msg : '是否撤销',
                        type: 'confirm'
                    }).then(function () {
                        qcApi.post('mtourfinance/cancelCostRecord', {
                            otherRevenueUuid: otherRevenue.otherRevenueUuid
                        }).success(function (result) {
                            $rootScope.$broadcast('order.otherRevenue.cancel', otherRevenue.orderUuid);
                        });
                    });
                }

                scope.currencies = commonValue.currencies;
                scope.userInfo = $rootScope.userInfo;
                //其他收入录入成功后展开录入列
            }
        }
    }]);