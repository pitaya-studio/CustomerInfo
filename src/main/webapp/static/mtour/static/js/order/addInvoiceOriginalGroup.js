orderList.directive('addInvoiceOriginalGroupButton', ['urlConfig', 'qcDialog', 'qcApi', 'groupAmount', 'qcDialog', 'calculateTotalFee', '$timeout', '$q', '$rootScope',
    function (urlConfig, qcDialog, qcApi, groupAmount, qcDialog, calculateTotalFee, $timeout, $q, $rootScope) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid: '='
            },
            link    : function (scope, ele, attrs) {
                ele.on('click', function () {
                    qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/addInvoiceOriginalGroup.html',
                        controller : ['$scope', function (dialogScope) {
                            dialogScope.invoiceOriginalGroup = {};

                            dialogScope.orderUuid = scope.orderUuid;
                            dialogScope.commit = function () {
                                var saveUrl = '';
                                if ($rootScope.userInfo.companyRoleCode == '0') {
                                    saveUrl = 'order/addToAirticketOrder';
                                }
                                else {
                                    saveUrl = 'order/addToAirticketOrderForCommon';
                                }
                                if (!validInvoiceOriginalGroup()) {
                                    dialogScope.$broadcast('qcValid.check');
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    return;
                                }

                                var totalAmount = calculateTotalFee([dialogScope.invoiceOriginalGroup]);
                                //.totalSalePrice = totalAmount.totalSalePrice;
                                //.orderAmount = totalAmount.orderAmount;
                                //.profit = totalAmount.profit;
                                //.totalDeposit = totalAmount.totalDeposit;
                                //.totalCost = totalAmount.totalCost;
                                //.receivableAmount = totalAmount.receivable;
                                //.receivedAmount = totalAmount.receivable;
                                //.unreceiveAmount = totalAmount.unreceive;
                                //.arrivedAmount = totalAmount.arrived;
                                //.returnAmount = totalAmount.return;
                                qcApi.post(saveUrl, {
                                    orderUuid       : scope.orderUuid,
                                    addFITCount     : dialogScope.addFITCount,
                                    invoiceOriginals: dialogScope.invoiceOriginalGroup.invoiceOriginals,
                                    totalDeposit    : totalAmount.totalDeposit,
                                    totalSalePrice  : totalAmount.totalSalePrice//追散外报总额
                                }).success(function () {
                                    dialogScope.$emit('order.addInvoiceOriginalGroupButton', scope.orderUuid);
                                    dialogScope.closeThisDialog();
                                });
                            };
                            $timeout(function () {
                                dialogScope.$broadcast('currentOrder.editModel.reset', 'edit');
                            }, 500);

                            function validInvoiceOriginalGroup() {
                                var isValidFee = true;
                                if (!dialogScope.addFITCount) {
                                    isValidFee = false;
                                }
                                angular.forEach(dialogScope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                                    if (invoiceOriginal.invoiceOriginalTypeCode == '0' && !invoiceOriginal.PNR) {
                                        isValidFee = false;
                                    }
                                    if (!invoiceOriginal.costTourOperatorTypeCode) {
                                        isValidFee = false;
                                    }
                                    if (!invoiceOriginal.tourOperatorUuid) {
                                        isValidFee = false;
                                    }
                                    if (!invoiceOriginal.airlineCompanyUuid) {
                                        isValidFee = false;
                                    }
                                    //if (!invoiceOriginal.costBankName) {
                                    //    isValidFee = false;
                                    //}
                                    //if (!invoiceOriginal.costAccountNo) {
                                    //    isValidFee = false;
                                    //}
                                    angular.forEach(invoiceOriginal.airlines, function (airline) {
                                        if (!airline.airlineName) {
                                            isValidFee = false;
                                        }
                                        angular.forEach(airline.costs, function (cost) {
                                            if (!cost.exchangeRate) {
                                                isValidFee = false;
                                            }
                                            if (!cost.peopleCount) {
                                                isValidFee = false;
                                            }
                                            if (!cost.costUnitPrice) {
                                                isValidFee = false;
                                            }
                                            if (!cost.totalDeposit) {
                                                isValidFee = false;
                                            }
                                        });
                                        angular.forEach(airline.salePrices, function (salePrice) {
                                            if (!salePrice.exchangeRate) {
                                                isValidFee = false;
                                            }
                                            if (!salePrice.peopleCount) {
                                                isValidFee = false;
                                            }
                                            if (!salePrice.costUnitPrice) {
                                                isValidFee = false;
                                            }
                                            if (!salePrice.totalDeposit) {
                                                isValidFee = false;
                                            }
                                        });
                                    });
                                });
                                if (!isValidFee) {
                                    return false;
                                }
                                return isValidFee;
                            }
                        }],
                        width      : 800,
                        plain      : false
                    });

                });
            }
        };
    }]);