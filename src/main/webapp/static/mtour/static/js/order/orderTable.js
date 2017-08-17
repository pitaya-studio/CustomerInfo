mtour.directive('showcostin', ['urlConfig', 'qcDialog', '$timeout', 'groupAmount', 'qcApi', 'qcMessage', 'calculateTotalFee', '$rootScope', 'commonValue',
    function (urlConfig, qcDialog, $timeout, groupAmount, qcApi, qcMessage, calculateTotalFee, $rootScope, commonValue) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid   : '=',
                operatorType: '='
            },
            link    : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                ele.on('click', function () {
                    qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg.html',
                        controller : ['$scope', 'qcApi', function ($scope, qcApi) {

                            if (scope.operatorType == 0) {
                                $scope.orders = [{}];
                                $scope.currentOrder = $scope.orders[0];
                                $scope.currentOrder.editModel = 'edit';//编辑模式
                                $timeout(function () {
                                    $scope.$broadcast('currentOrder.editModel.reset', $scope.currentOrder.editModel);
                                }, 1000);
                            }
                            else {
                                qcApi.post('order/showAirticketOrderDetail', {
                                    orderUuid: scope.orderUuid
                                }).success(function (result) {
                                    $scope.currentOrder = result.data;
                                    $scope.orders = [$scope.currentOrder];
                                    if (scope.operatorType == 1) {
                                        if (scope.companyRoleCode == '0') {
                                            angular.forEach($scope.currentOrder.fee.invoiceOriginalGroups, function (invoiceOriginalGroup) {
                                                angular.forEach(invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                                                    qcApi.post('common/getBankInfo', {
                                                        type: '1',
                                                        uuid: invoiceOriginal.costTourOperatorUuid
                                                    }).success(function (result) {
                                                        invoiceOriginal.costBanks = result.data;
                                                        angular.forEach(invoiceOriginal.costBanks, function (costBank) {
                                                            if (costBank.bankName == invoiceOriginal.costBankName) {
                                                                invoiceOriginal.selectedCostBank = {};
                                                                invoiceOriginal.selectedCostBank.accounts = costBank.accounts;
                                                            }
                                                        });
                                                        if (invoiceOriginal.selectedCostBank) {
                                                            invoiceOriginal.selectedCostBank.bankName = invoiceOriginal.costBankName;
                                                        } else {
                                                            invoiceOriginal.selectedCostBank = {bankName: invoiceOriginal.costBankName};
                                                        }
                                                        invoiceOriginal.selectedCostAccount = {accountNo: invoiceOriginal.costAccountNo};
                                                    });
                                                });

                                            });
                                        }
                                        $scope.currentOrder.editModel = 'edit';//编辑模式
                                        $timeout(function () {
                                            $scope.$broadcast('currentOrder.editModel.reset', $scope.currentOrder.editModel);
                                        }, 500);
                                    } else if (scope.operatorType == 2) {
                                        $scope.currentOrder.editModel = 'detail';//详情模式
                                        $timeout(function () {
                                            $scope.$broadcast('currentOrder.editModel.reset', $scope.currentOrder.editModel);
                                        }, 500);
                                    }
                                    if ($scope.currentOrder.baseInfo.orderStatusCode == '0') {
                                        $scope.currentOrder.saveModel = 'commit';//订单已已提交
                                        $timeout(function () {
                                            $scope.$broadcast('currentOrder.saveModel.reset', $scope.currentOrder.saveModel);
                                        }, 1000);
                                    }
                                });
                            }

                            $scope.$on('fee.change', function () {
                                $scope.$broadcast('fee.changed');
                            });
                            $scope.initOrder = function () {
                                qcDialog.openMessage({msg: '当前页面未保存,确认清空?', type: 'confirm'})
                                    .then(function () {
                                        $scope.currentOrder = {
                                            editModel   : 'edit',
                                            baseInfo    : {
                                                orderer: $rootScope.userInfo.userName
                                            },
                                            reservations: {
                                                contacts: [{}]
                                            },
                                            fee         : {
                                                invoiceOriginalGroups: [
                                                    {
                                                        invoiceOriginals: [
                                                            {
                                                                airlines: [
                                                                    {
                                                                        costs     : [{}],
                                                                        salePrices: [{}]
                                                                    }
                                                                ],
                                                                deadline: {}
                                                            }
                                                        ]
                                                    }
                                                ],
                                                priceChange          : {
                                                    additionalCosts: [{}],
                                                    salePrices     : [{}]
                                                }
                                            },
                                            flights     : [{}],
                                            travelers   : [{}]
                                        };
                                        $scope.$broadcast('fee.changed');
                                    });
                            };
                            $scope.changeToEdit = function () {
                                $scope.currentOrder.editModel = 'edit';//编辑模式
                                $scope.$broadcast('currentOrder.editModel.reset', $scope.currentOrder.editModel);
                            };
                            <!--modify by wlj at 2016.06.16-start-->
                            $scope.saveOrderForConfirm = function () {
                               var tip="订单保存为待确认成功"
                                var saveUrl = '';
                                if ($rootScope.userInfo.companyRoleCode == '0') {
                                    saveUrl = 'order/saveAirticketOrder';
                                }
                                else {
                                    saveUrl = 'order/saveAirticketOrderForCommon';
                                }
                                if (!validOrder($scope.currentOrder)) {
                                    $scope.$broadcast('qcValid.check');
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    return;
                                }
                                if (angular.isUndefined($scope.currentOrder.baseInfo.orderStatusCode)) {
                                    $scope.currentOrder.baseInfo.orderStatusCode = '4';
                                }

                                //金额统计
                                var totalAmount = calculateTotalFee($scope.currentOrder.fee.invoiceOriginalGroups,
                                    $scope.currentOrder.fee.priceChange, $scope.currentOrder.fee.returnAmount, $scope.currentOrder.fee.receivedAmount, $scope.currentOrder.fee.arrivedAmount);
                                $scope.currentOrder.fee.totalSalePrice = totalAmount.totalSalePrice;
                                $scope.currentOrder.fee.orderAmount = totalAmount.orderAmount;
                                $scope.currentOrder.fee.profit = totalAmount.profit;
                                $scope.currentOrder.fee.totalDeposit = totalAmount.totalDeposit;
                                $scope.currentOrder.fee.totalCost = totalAmount.totalCost;
                                $scope.currentOrder.fee.receivableAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.receivedAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.unreceiveAmount = totalAmount.unreceive;
                                $scope.currentOrder.fee.arrivedAmount = totalAmount.arrived;
                                $scope.currentOrder.fee.returnAmount = totalAmount.return;
                                transferChangeType($scope.currentOrder);

                                /*
                                 {orderStatusCode: '4', orderStatusName: '待确定'},
                                 */

                                qcApi.post(saveUrl, $scope.currentOrder)
                                    .success(function (result) {
                                        $scope.currentOrder.baseInfo.orderUuid = result.data.baseInfo.orderUuid;
                                        //此处当是待确定的时候没有传递值，具体的看是后台传值还是此处写死
                                        $scope.currentOrder.baseInfo.orderStatus = result.data.baseInfo.orderStatus;
                                        $scope.currentOrder.baseInfo.orderStatusCode = result.data.baseInfo.orderStatusCode;
                                        qcMessage.tip(tip);
                                        $scope.$emit('order.save');
                                        $scope.closeThisDialog();

                                    }).error(function (data, status, headers, config) {

                                });
                            };
                            $scope.commitOrder = function () {
                                var saveUrl = '';
                                if ($rootScope.userInfo.companyRoleCode == '0') {
                                    saveUrl = 'order/saveAirticketOrder';
                                }
                                else {
                                    saveUrl = 'order/saveAirticketOrderForCommon';
                                }
                                if (!validOrder($scope.currentOrder)) {
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    $scope.$broadcast('qcValid.check');
                                    return;
                                }
                                $scope.currentOrder.baseInfo.orderStatusCode = '0';


                                angular.forEach($scope.currentOrder.fee.invoiceOriginalGroups, function (group) {
                                    angular.forEach(group.invoiceOriginals, function (invoiceOriginal) {
                                        if (invoiceOriginal.PNR) {
                                            invoiceOriginal.PNR = invoiceOriginal.PNR.replace(/[\r\n]/g, ' ');
                                        }
                                    });
                                });

                                //金额统计
                                var totalAmount = calculateTotalFee($scope.currentOrder.fee.invoiceOriginalGroups,
                                    $scope.currentOrder.fee.priceChange, $scope.currentOrder.fee.returnAmount, $scope.currentOrder.fee.receivedAmount, $scope.currentOrder.fee.arrivedAmount);
                                $scope.currentOrder.fee.totalSalePrice = totalAmount.totalSalePrice;
                                $scope.currentOrder.fee.orderAmount = totalAmount.orderAmount;
                                $scope.currentOrder.fee.profit = totalAmount.profit;
                                $scope.currentOrder.fee.totalDeposit = totalAmount.totalDeposit;
                                $scope.currentOrder.fee.totalCost = totalAmount.totalCost;
                                $scope.currentOrder.fee.receivableAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.receivedAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.unreceiveAmount = totalAmount.unreceive;
                                $scope.currentOrder.fee.arrivedAmount = totalAmount.arrived;
                                $scope.currentOrder.fee.returnAmount = totalAmount.return;
                                if ($scope.currentOrder.fee.priceChange.additionalCosts.length > 0) {
                                    angular.forEach($scope.currentOrder.fee.priceChange.additionalCosts, function (additionalCost) {
                                        if (!additionalCost.amount) {
                                            $scope.currentOrder.fee.priceChange.additionalCosts.remove(additionalCost);
                                        }
                                    })
                                }
                                if ($scope.currentOrder.fee.priceChange.salePrices.length > 0) {
                                    angular.forEach($scope.currentOrder.fee.priceChange.salePrices, function (salePrice) {
                                        if (!salePrice.amount) {
                                            $scope.currentOrder.fee.priceChange.salePrices.remove(salePrice);
                                        }
                                    })
                                }
                                transferChangeType($scope.currentOrder);
                                qcApi.post(saveUrl, $scope.currentOrder)
                                    .success(function (result) {
                                        $scope.currentOrder.baseInfo.orderUuid = result.data.baseInfo.orderUuid;
                                        $scope.currentOrder.baseInfo.orderStatus = result.data.baseInfo.orderStatus;
                                        $scope.currentOrder.baseInfo.orderStatusCode = result.data.baseInfo.orderStatusCode;
                                        qcMessage.tip('提交成功');
                                        $scope.$emit('order.commit');
                                        $scope.closeThisDialog();
                                    }).error(function (data, status, headers, config) {
                                });
                            };
                            $scope.saveOrder = function () {
                                var saveUrl = '';
                                if ($rootScope.userInfo.companyRoleCode == '0') {
                                    saveUrl = 'order/saveAirticketOrder';
                                }
                                else {
                                    saveUrl = 'order/saveAirticketOrderForCommon';
                                }
                                if (!validOrder($scope.currentOrder)) {
                                    $scope.$broadcast('qcValid.check');
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    return;
                                }
                                if (angular.isUndefined($scope.currentOrder.baseInfo.orderStatusCode)) {
                                    $scope.currentOrder.baseInfo.orderStatusCode = '3';
                                }

                                //金额统计
                                var totalAmount = calculateTotalFee($scope.currentOrder.fee.invoiceOriginalGroups,
                                    $scope.currentOrder.fee.priceChange, $scope.currentOrder.fee.returnAmount, $scope.currentOrder.fee.receivedAmount, $scope.currentOrder.fee.arrivedAmount);
                                $scope.currentOrder.fee.totalSalePrice = totalAmount.totalSalePrice;
                                $scope.currentOrder.fee.orderAmount = totalAmount.orderAmount;
                                $scope.currentOrder.fee.profit = totalAmount.profit;
                                $scope.currentOrder.fee.totalDeposit = totalAmount.totalDeposit;
                                $scope.currentOrder.fee.totalCost = totalAmount.totalCost;
                                $scope.currentOrder.fee.receivableAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.receivedAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.unreceiveAmount = totalAmount.unreceive;
                                $scope.currentOrder.fee.arrivedAmount = totalAmount.arrived;
                                $scope.currentOrder.fee.returnAmount = totalAmount.return;
                                transferChangeType($scope.currentOrder);
                                qcApi.post(saveUrl, $scope.currentOrder)
                                    .success(function (result) {
                                        $scope.currentOrder.baseInfo.orderUuid = result.data.baseInfo.orderUuid;
                                        $scope.currentOrder.baseInfo.orderStatus = result.data.baseInfo.orderStatus;
                                        $scope.currentOrder.baseInfo.orderStatusCode = result.data.baseInfo.orderStatusCode;
                                        qcMessage.tip('保存成功');
                                        $scope.$emit('order.save');
                                        $scope.closeThisDialog();

                                    }).error(function (data, status, headers, config) {

                                    });
                            };
                            $scope.commitOrder = function () {
                                var saveUrl = '';
                                if ($rootScope.userInfo.companyRoleCode == '0') {
                                    saveUrl = 'order/saveAirticketOrder';
                                }
                                else {
                                    saveUrl = 'order/saveAirticketOrderForCommon';
                                }
                                if (!validOrder($scope.currentOrder)) {
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    $scope.$broadcast('qcValid.check');
                                    return;
                                }
                                $scope.currentOrder.baseInfo.orderStatusCode = '0';


                                angular.forEach($scope.currentOrder.fee.invoiceOriginalGroups, function (group) {
                                    angular.forEach(group.invoiceOriginals, function (invoiceOriginal) {
                                        if (invoiceOriginal.PNR) {
                                            invoiceOriginal.PNR = invoiceOriginal.PNR.replace(/[\r\n]/g, ' ');
                                        }
                                    });
                                });

                                //金额统计
                                var totalAmount = calculateTotalFee($scope.currentOrder.fee.invoiceOriginalGroups,
                                    $scope.currentOrder.fee.priceChange, $scope.currentOrder.fee.returnAmount, $scope.currentOrder.fee.receivedAmount, $scope.currentOrder.fee.arrivedAmount);
                                $scope.currentOrder.fee.totalSalePrice = totalAmount.totalSalePrice;
                                $scope.currentOrder.fee.orderAmount = totalAmount.orderAmount;
                                $scope.currentOrder.fee.profit = totalAmount.profit;
                                $scope.currentOrder.fee.totalDeposit = totalAmount.totalDeposit;
                                $scope.currentOrder.fee.totalCost = totalAmount.totalCost;
                                $scope.currentOrder.fee.receivableAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.receivedAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.unreceiveAmount = totalAmount.unreceive;
                                $scope.currentOrder.fee.arrivedAmount = totalAmount.arrived;
                                $scope.currentOrder.fee.returnAmount = totalAmount.return;
                                if ($scope.currentOrder.fee.priceChange.additionalCosts.length > 0) {
                                    angular.forEach($scope.currentOrder.fee.priceChange.additionalCosts, function (additionalCost) {
                                        if (!additionalCost.amount) {
                                            $scope.currentOrder.fee.priceChange.additionalCosts.remove(additionalCost);
                                        }
                                    })
                                }
                                if ($scope.currentOrder.fee.priceChange.salePrices.length > 0) {
                                    angular.forEach($scope.currentOrder.fee.priceChange.salePrices, function (salePrice) {
                                        if (!salePrice.amount) {
                                            $scope.currentOrder.fee.priceChange.salePrices.remove(salePrice);
                                        }
                                    })
                                }
                                transferChangeType($scope.currentOrder);
                                qcApi.post(saveUrl, $scope.currentOrder)
                                    .success(function (result) {
                                        $scope.currentOrder.baseInfo.orderUuid = result.data.baseInfo.orderUuid;
                                        $scope.currentOrder.baseInfo.orderStatus = result.data.baseInfo.orderStatus;
                                        $scope.currentOrder.baseInfo.orderStatusCode = result.data.baseInfo.orderStatusCode;
                                        qcMessage.tip('提交成功');
                                        $scope.$emit('order.commit');
                                        $scope.closeThisDialog();
                                    }).error(function (data, status, headers, config) {
                                    });
                            };
                            $scope.commitAndReceiveOrder = function () {
                                var saveUrl = '';
                                if ($rootScope.userInfo.companyRoleCode == '0') {
                                    saveUrl = 'order/saveAirticketOrder';
                                }
                                else {
                                    saveUrl = 'order/saveAirticketOrderForCommon';
                                }
                                if (!validOrder($scope.currentOrder)) {
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    $scope.$broadcast('qcValid.check');
                                    return;
                                }

                                angular.forEach($scope.currentOrder.fee.invoiceOriginalGroups, function (group) {
                                    angular.forEach(group.invoiceOriginals, function (invoiceOriginal) {
                                        if (invoiceOriginal.PNR) {
                                            invoiceOriginal.PNR = invoiceOriginal.PNR.replace(/[\r\n]/g, ' ');
                                        }
                                    });
                                });

                                $scope.currentOrder.baseInfo.orderStatusCode = '0';

                                //金额统计
                                var totalAmount = calculateTotalFee($scope.currentOrder.fee.invoiceOriginalGroups,
                                    $scope.currentOrder.fee.priceChange, $scope.currentOrder.fee.returnAmount, $scope.currentOrder.fee.receivedAmount, $scope.currentOrder.fee.arrivedAmount);
                                $scope.currentOrder.fee.totalSalePrice = totalAmount.totalSalePrice;
                                $scope.currentOrder.fee.orderAmount = totalAmount.orderAmount;
                                $scope.currentOrder.fee.profit = totalAmount.profit;
                                $scope.currentOrder.fee.totalDeposit = totalAmount.totalDeposit;
                                $scope.currentOrder.fee.totalCost = totalAmount.totalCost;
                                $scope.currentOrder.fee.receivableAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.receivedAmount = totalAmount.receivable;
                                $scope.currentOrder.fee.unreceiveAmount = totalAmount.unreceive;
                                $scope.currentOrder.fee.arrivedAmount = totalAmount.arrived;
                                $scope.currentOrder.fee.returnAmount = totalAmount.return;
                                transferChangeType($scope.currentOrder);
                                qcApi.post(saveUrl, $scope.currentOrder)
                                    .success(function (result) {
                                        $scope.currentOrder.baseInfo.orderUuid = result.data.baseInfo.orderUuid;
                                        $scope.currentOrder.baseInfo.orderStatus = result.data.baseInfo.orderStatus;
                                        $scope.currentOrder.baseInfo.orderStatusCode = result.data.baseInfo.orderStatusCode;
                                        qcMessage.tip('提交成功');
                                        $scope.$emit('order.commitAndReceive', $scope.currentOrder.baseInfo.orderUuid);
                                        $scope.closeThisDialog();
                                    }).error(function (data, status, headers, config) {
                                    });
                            };

                            $scope.canClear = function () {
                                if (!$scope.currentOrder) {
                                    return false;
                                }
                                if (!$scope.currentOrder.baseInfo) {
                                    return true;
                                }
                                if (angular.isUndefined($scope.currentOrder.baseInfo.orderStatusCode)) {
                                    return true;
                                }
                                return false;
                            }
                            $scope.toggleAll = function () {
                                $scope.foldAll = !$scope.foldAll;
                                $scope.foldUploader = $scope.foldAll;
                                $scope.$broadcast('toggleAll', $scope.foldAll);
                            }

                            function transferChangeType(order) {
                                angular.forEach(order.fee.priceChange.salePrices, function (salePrice) {
                                    if (salePrice.changeType == '-') {
                                        salePrice.changeType = '1';
                                    } else {
                                        salePrice.changeType = '0';
                                    }
                                });
                            }

                            function validOrder(order) {
                                if (angular.isEmpty(order.baseInfo.productName)) {
                                    return false;
                                }
                                if (angular.isEmpty(order.baseInfo.departureDate)) {
                                    return false;
                                }
                                if (angular.isEmpty(order.baseInfo.reservationCount)) {
                                    return false;
                                }
                                if (angular.isEmpty(order.baseInfo.lineCountryUuid)) {
                                    return false;
                                }
                                if (angular.isEmpty(order.baseInfo.itinerary)) {
                                    return false;
                                }
                                if (angular.isEmpty(order.reservations.channelTypeCode)) {
                                    return false;
                                } else if (order.reservations.channelTypeCode != '1' && angular.isEmpty(order.reservations.channelCode)) {
                                    return false;
                                }
                                if (angular.isEmpty(order.reservations.channelName)) {
                                    return false;
                                }
                                var isValidContact = true;
                                angular.forEach(order.reservations.contacts, function (contact) {
                                    if (angular.isEmpty(contact.name) || angular.isEmpty(contact.phone)) {
                                        isValidContact = false;
                                    }
                                });
                                if (!isValidContact) {
                                    return false;
                                }
                                var isValidFee = true;
                                angular.forEach(order.fee.invoiceOriginalGroups, function (invoiceOriginalGroup) {
                                    angular.forEach(invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                                        if (invoiceOriginal.invoiceOriginalTypeCode == '0' && angular.isEmpty(invoiceOriginal.PNR)) {
                                            isValidFee = false;
                                        }
                                        if (angular.isEmpty(invoiceOriginal.costTourOperatorTypeCode)) {
                                            isValidFee = false;
                                        }
                                        if (angular.isEmpty(invoiceOriginal.tourOperatorUuid)) {
                                            isValidFee = false;
                                        }
                                        if (angular.isEmpty(invoiceOriginal.airlineCompanyUuid)) {
                                            isValidFee = false;
                                        }
                                        //if (angular.isEmpty(invoiceOriginal.costBankName)) {
                                        //    isValidFee = false;
                                        //}
                                        //if (angular.isEmpty(invoiceOriginal.costAccountNo)) {
                                        //    isValidFee = false;
                                        //}
                                        angular.forEach(invoiceOriginal.airlines, function (airline) {
                                            if (angular.isEmpty(airline.airlineName)) {
                                                isValidFee = false;
                                            }
                                            angular.forEach(airline.costs, function (cost) {
                                                if (angular.isEmpty(cost.exchangeRate)) {
                                                    isValidFee = false;
                                                }
                                                if (angular.isEmpty(cost.peopleCount)) {
                                                    isValidFee = false;
                                                }
                                                if (angular.isEmpty(cost.costUnitPrice)) {
                                                    isValidFee = false;
                                                }
                                                if (angular.isEmpty(cost.totalDeposit)) {
                                                    isValidFee = false;
                                                }
                                            });
                                            angular.forEach(airline.salePrices, function (salePrice) {
                                                if (angular.isEmpty(salePrice.exchangeRate)) {
                                                    isValidFee = false;
                                                }
                                                if (angular.isEmpty(salePrice.peopleCount)) {
                                                    isValidFee = false;
                                                }
                                                if (angular.isEmpty(salePrice.costUnitPrice)) {
                                                    isValidFee = false;
                                                }
                                                if (angular.isEmpty(salePrice.totalDeposit)) {
                                                    isValidFee = false;
                                                }
                                            });
                                        });
                                    });
                                });
                                if (order.fee.priceChange.showType == 'additional') {
                                    angular.forEach(order.fee.priceChange.additionalCosts, function (additionalCost) {
                                        if (angular.isEmpty(additionalCost.amount)) {
                                            isValidFee = false;
                                        }
                                    })
                                }
                                if (order.fee.priceChange.showType == 'salePrice') {
                                    angular.forEach(order.fee.priceChange.salePrices, function (salePrice) {
                                        if (angular.isEmpty(salePrice.amount)) {
                                            isValidFee = false;
                                        }
                                    })
                                }

                                if (!isValidFee) {
                                    return false;
                                }
                                return true;
                            }
                        }],
                        className  : 'qc-dialog-theme-page',
                        width      : 1170,
                        plain      : false
                    });
                });
            }
        };
    }]);

mtour.directive('orderRegBaseInfo', ['$rootScope', 'urlConfig', 'commonValue', function ($rootScope, urlConfig, commonValue) {

    return {
        restrict   : 'A',
        scope      : {
            baseInfo: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-baseInfo.html',
        link       : function (scope, ele, attrs) {

            scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
            //scope.companyRoleCode = '0';
            if (!scope.baseInfo) {
                scope.baseInfo = {};
            }
            scope.baseInfo.tickets=[];
            function initBaseInfo() {
                if (!scope.baseInfo.departureCity && scope.baseInfo.departureCityUuid) {
                    scope.baseInfo.departureCity = {
                        cityUuid: scope.baseInfo.departureCityUuid,
                        cityName: scope.baseInfo.departureCityName
                    };
                }
                if (!scope.baseInfo.arrivalCity && scope.baseInfo.arrivalCityUuid) {
                    scope.baseInfo.arrivalCity = {
                        cityUuid: scope.baseInfo.arrivalCityUuid,
                        cityName: scope.baseInfo.arrivalCityName
                    };
                }
                if (!scope.baseInfo.lineCountry && scope.baseInfo.lineCountryUuid) {
                    scope.baseInfo.lineCountry = {
                        countryUuid: scope.baseInfo.lineCountryUuid,
                        countryName: scope.baseInfo.lineCountryName
                    };
                }
                if (!scope.baseInfo.ticket && scope.baseInfo.ticketId) {
                    scope.baseInfo.ticket = {
                        userId  : scope.baseInfo.ticketId,
                        userName: scope.baseInfo.ticketName
                    };
                    scope.baseInfo.tickets.push(scope.baseInfo.ticket);
                }
                if (!scope.baseInfo.operator && scope.baseInfo.operatorId) {
                    scope.baseInfo.operator = {
                        userId  : scope.baseInfo.operatorId,
                        userName: scope.baseInfo.operatorName
                    };
                }
                //scope.editModel = 'detail';

                scope.baseInfo.orderer = $rootScope.userInfo.userName;
            }

            scope.$on('baseInfo.departureCity.change', function () {
                scope.baseInfo.departureCityUuid = scope.baseInfo.departureCity.cityUuid;
                scope.baseInfo.departureCityName = scope.baseInfo.departureCity.cityName;
            });
            scope.$on('baseInfo.arrivalCity.change', function () {
                scope.baseInfo.arrivalCityUuid = scope.baseInfo.arrivalCity.cityUuid;
                scope.baseInfo.arrivalCityName = scope.baseInfo.arrivalCity.cityName;
            });
            scope.$on('baseInfo.lineCountry.change', function () {
                scope.baseInfo.lineCountryUuid = scope.baseInfo.lineCountry.countryUuid;
                scope.baseInfo.lineCountryName = scope.baseInfo.lineCountry.countryName;
            });
            scope.$on('baseInfo.ticket.change', function () {
                scope.baseInfo.ticketId = scope.baseInfo.ticket.userId;
                scope.baseInfo.ticketName = scope.baseInfo.ticket.userName;

                scope.baseInfo.ticket = {
                    userId  : scope.baseInfo.ticketId,
                    userName: scope.baseInfo.ticketName
                };
                scope.baseInfo.tickets.push(scope.baseInfo.ticket);
            });
            scope.$on('baseInfo.operator.change', function () {
                scope.baseInfo.operatorId = scope.baseInfo.operator.userId;
                scope.baseInfo.operatorName = scope.baseInfo.operator.userName;
            });
            scope.$on('baseInfo.tickets.change', function () {
                scope.baseInfo.operatorId = scope.baseInfo.operator.userId;
                scope.baseInfo.operatorName = scope.baseInfo.operator.userName;
            });


            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
                initBaseInfo()
            });

            scope.$on('currentOrder.saveModel.reset', function ($e, saveModel) {
                scope.saveModel = saveModel;
            });
        }
    };
}]);


mtour.directive('orderRegReservation', ['urlConfig', 'fixedValue', 'qcObjectInArray', function (urlConfig, fixedValue, qcObjectInArray) {
    return {
        restrict   : 'A',
        scope      : {
            reservations: '='
        },
        controller : ['$scope', function ($scope) {
            this.addContact = function () {
                $scope.reservations.contacts.push({});
            };
            this.removeContact = function (contact) {
                $scope.reservations.contacts.remove(contact);
            }
        }],
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-reservation.html',
        link       : function (scope, ele, attrs) {
            scope.channelTypes = fixedValue.channelType;
            if (!scope.reservations) {
                scope.reservations = {
                    contacts: [{}]
                };
            }
            function initReservation() {

                if (!scope.reservations.channelType && scope.reservations.channelTypeCode) {
                    scope.reservations.channelType = qcObjectInArray(scope.channelTypes, 'channelTypeCode', scope.reservations.channelTypeCode);
                    scope.reservations.channelTypeName = scope.reservations.channelType.channelTypeName;
                }
                if (!scope.reservations.channel && scope.reservations.channelUuid) {
                    scope.reservations.channel = {
                        channelUuid: scope.reservations.channelUuid,
                        channelName: scope.reservations.channelName
                    };
                }
            }

            scope.$on('reservations.channelType.change', function () {
                scope.reservations.channelTypeCode = scope.reservations.channelType.channelTypeCode;
                scope.reservations.channelTypeName = scope.reservations.channelType.channelTypeName;
                scope.reservations.channel = undefined;
                scope.reservations.channelUuid = undefined;
                scope.reservations.channelName = undefined;
                scope.reservations.channelCode = undefined;
                scope.reservations.contacts = [{}];
            });
            scope.$on('reservations.channel.change', function () {
                scope.reservations.channelUuid = scope.reservations.channel.channelUuid;
                scope.reservations.channelName = scope.reservations.channel.channelName;
                scope.reservations.contacts = [{}];
                scope.reservations.contacts[0].name = scope.reservations.channel.contactName;
                scope.reservations.contacts[0].phone = scope.reservations.channel.contactPhone;
            });
            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
                initReservation();
            });
            scope.$on('currentOrder.saveModel.reset', function ($e, saveModel) {
                scope.saveModel = saveModel;
            });
        }
    };
}]);

mtour.directive('contactInfo', ['urlConfig', '$rootScope', function (urlConfig, $rootScope) {
    return {
        require    : '^orderRegReservation',
        restrict   : 'A',
        scope      : {
            contact  : '=',
            canDelete: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/contact-info.html',
        link       : function (scope, ele, attrs, orderRegReservationCtrl) {
            scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
            //scope.companyRoleCode = '0';
            function initContactInfo() {
                scope.editModel = 'edit';
            }

            scope.toggle = function () {
                scope.fold = !scope.fold;
            }
            scope.add = function () {
                orderRegReservationCtrl.addContact();
            };
            scope.remove = function (contact) {
                orderRegReservationCtrl.removeContact(contact);
            }
            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
            });
            scope.$on('currentOrder.saveModel.reset', function ($e, saveModel) {
                scope.saveModel = saveModel;
            });
            initContactInfo();
        }
    };
}]);


mtour.directive('orderRegFee', ['urlConfig', '$rootScope', function (urlConfig, $rootScope) {
    return {
        restrict   : 'A',
        scope      : {
            fee      : '=',
            orderUuid: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-fee.html',
        link       : function (scope, ele, attrs) {
            scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
            //scope.companyRoleCode = '0';
            scope.orderUuid = scope.orderUuid;
            function initFee() {
                if (!scope.fee) {
                    scope.fee = {};
                }
                if (!scope.fee.invoiceOriginalGroups) {
                    scope.fee.invoiceOriginalGroups = [{}];
                }
            }

            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
            });
            scope.$on('currentOrder.saveModel.reset', function ($e, saveModel) {
                scope.saveModel = saveModel;
            });
            scope.$on('toggleAll', function ($e, fold) {
                scope.fold = fold;
            });
            initFee();
        }
    };
}]);

mtour.directive('orderRegFeeInvoiceOriginalGroup', ['urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcApi', '$q', '$rootScope', 'qcDialog',
    function (urlConfig, fixedValue, commonValue, qcObjectInArray, qcApi, $q, $rootScope, qcDialog) {
        return {
            restrict   : 'A',
            scope      : {
                invoiceOriginalGroup: '=',
                editModel           : '=',
                saveModel           : '=',
                orderUuid           : '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-fee-invoiceOriginalGroup-0024.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                //scope.companyRoleCode = '0';
                var oldInvoiceOriginalGroup = [];
                angular.forEach(scope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                    oldInvoiceOriginalGroup.push({
                        tourOperatorUuid: invoiceOriginal.costTourOperatorUuid,
                        uuid            : invoiceOriginal.uuid
                    });
                })
                scope.imgUrl = urlConfig.mtourStaticUrl + 'img/';
                scope.invoiceOriginalTypes = fixedValue.invoiceOriginalTypes;
                scope.defaultInvoiceOriginal = qcObjectInArray(scope.invoiceOriginalTypes, 'invoiceOriginalTypeCode', '0');
                scope.currencies = commonValue.currencies;
                scope.selectedCurrency = scope.currencies[0];
                scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
                if (scope.orderUuid) {
                    var defer1 = $q.defer();
                    var defer2 = $q.defer();
                    var defer3 = $q.defer();
                    qcApi.post('order/getExistsCurrency?date=' + new Date(), {
                        orderId: scope.orderUuid,
                        type   : '2'
                    }).success(function (result) {
                        scope.currencies = result.data;
                        scope.selectedCurrency = scope.currencies[0];
                        scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
                        defer1.resolve();
                        defer2.resolve();
                        defer3.resolve();
                    });
                }
                else {
                    scope.currencies = commonValue.currencies;
                    scope.selectedCurrency = scope.currencies[0];
                    scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
                }

                scope.tourOperatorTypes = commonValue.tourOperatorTypes;

                function initInvoiceOriginalGroup() {
                    if (!scope.invoiceOriginalGroup.invoiceOriginals) {
                        scope.invoiceOriginalGroup.invoiceOriginals = [
                            {
                                airlines: [{
                                    costs     : [{}],
                                    salePrices: [{}]
                                }]
                            }
                        ];
                    }
                    else {
                        angular.forEach(scope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                            angular.forEach(invoiceOriginal.airlines, function (airline) {
                                if (airline.costs.length == 0) {
                                    airline.costs = [{orderCostStateCode: 2}];
                                }
                            })
                        })
                    }
                }

                /**
                 * 通过指定属性和属性值 获取数组中的该对象
                 * @param arr 对象数组
                 * @param propName 属性名
                 * @param propValue 属性值
                 * @returns {*} 指定属性的对象
                 */
                function getObjectByProp(arr, propName, propValue) {
                    if (!arr || arr.length == 0) {
                        return null;
                    }
                    for (var index in arr) {
                        if (arr[index][propName] == propValue) {
                            return arr[index];
                        }
                    }
                }

                scope.initCost = function (cost) {
                    if (!cost.currency) {
                        if (!cost.currencyUuid) {
                            cost.currency = scope.defaultCurrency;
                            cost.currencyUuid = scope.defaultCurrency.currencyUuid;
                            cost.currencyCode = scope.defaultCurrency.currencyCode;
                            cost.exchangeRate = scope.defaultCurrency.exchangeRate;
                        } else {
                            cost.currency = qcObjectInArray(scope.currencies, 'currencyUuid', cost.currencyUuid);
                            if (cost.currency && cost.exchangeRate) {
                                cost.currency.exchangeRate = cost.exchangeRate;
                                cost.currencyCode = cost.currency.currencyCode;
                            }
                        }
                    } else {
                        cost.currencyUuid = cost.currency.currencyUuid;
                        cost.currencyCode = cost.currency.currencyCode;
                        cost.exchangeRate = cost.exchangeRate;
                    }
                };

                //scope.initCost = function (cost) {
                //    //if(cost.currency){
                //    cost.currency = scope.defaultCurrency;
                //    cost.currencyUuid = scope.defaultCurrency.currencyUuid;
                //    cost.currencyCode = scope.defaultCurrency.currencyCode;
                //    cost.exchangeRate = scope.defaultCurrency.exchangeRate;
                //    //}
                //}

                scope.initSalePrice = function (salePrice) {
                    if (!salePrice.currency) {
                        if (!salePrice.currencyUuid) {
                            salePrice.currency = scope.defaultCurrency;
                            salePrice.currencyUuid = scope.defaultCurrency.currencyUuid;
                            salePrice.currencyCode = scope.defaultCurrency.currencyCode;
                            salePrice.exchangeRate = scope.defaultCurrency.exchangeRate;
                        } else {
                            salePrice.currency = qcObjectInArray(scope.currencies, 'currencyUuid', salePrice.currencyUuid);
                            if (salePrice.currency && salePrice.exchangeRate) {
                                salePrice.currency.exchangeRate = salePrice.exchangeRate;
                                salePrice.currencyCode = salePrice.currency.currencyCode;
                            }
                        }
                    } else {
                        salePrice.currencyUuid = salePrice.currency.currencyUuid;
                        salePrice.currencyCode = salePrice.currency.currencyCode;
                        salePrice.exchangeRate = salePrice.currency.exchangeRate;
                    }
                };
                scope.initInvoiceOriginal = function (invoiceOriginal, invoiceOriginalScope) {
                    invoiceOriginalScope.$on('toggleAll', function ($e, fold) {
                        invoiceOriginalScope.fold = fold;
                    });
                    if (!invoiceOriginal.invoiceOriginalType) {
                        if (angular.isUndefined(invoiceOriginal.invoiceOriginalTypeCode)) {
                            invoiceOriginal.invoiceOriginalType = scope.defaultInvoiceOriginal;
                            invoiceOriginal.invoiceOriginalTypeCode = '0';//PNR
                            invoiceOriginal.invoiceOriginalTypeName = 'PNR';//PNR
                        } else {
                            invoiceOriginal.invoiceOriginalType = qcObjectInArray(scope.invoiceOriginalTypes, 'invoiceOriginalTypeCode', invoiceOriginal.invoiceOriginalTypeCode);
                            invoiceOriginal.invoiceOriginalTypeName = invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeName;
                            if (!invoiceOriginal.costTourOperatorType) {
                                invoiceOriginal.costTourOperatorType = {
                                    tourOperatorTypeName: invoiceOriginal.costTourOperatorTypeName,
                                    tourOperatorTypeCode: invoiceOriginal.costTourOperatorTypeCode
                                };
                                invoiceOriginal.tourOperatorType = invoiceOriginal.costTourOperatorType;
                            }
                            if (!invoiceOriginal.tourOperator) {
                                invoiceOriginal.tourOperator = {
                                    tourOperatorUuid: invoiceOriginal.invoiceOriginalTypeCode != '0' ? invoiceOriginal.tourOperatorUuid : invoiceOriginal.costTourOperatorUuid,
                                    tourOperatorName: invoiceOriginal.costTourOperatorName
                                };
                                invoiceOriginal.tourOperatorUuid = invoiceOriginal.costTourOperatorUuid;
                                invoiceOriginal.tourOperatorName = invoiceOriginal.costTourOperatorName;
                            }
                            if (!invoiceOriginal.selectedCostBank && invoiceOriginal.costBankName) {
                                invoiceOriginal.selectedCostBank = {bankName: invoiceOriginal.costBankName};
                            }
                            if (!invoiceOriginal.selectedCostAccount && invoiceOriginal.costAccountNo) {
                                invoiceOriginal.selectedCostAccount = {accountNo: invoiceOriginal.costAccountNo};
                            }

                            if (!invoiceOriginal.airlineCompany && invoiceOriginal.airlineCompanyUuid) {
                                invoiceOriginal.airlineCompany = {
                                    airlineCompanyUuid: invoiceOriginal.airlineCompanyUuid,
                                    airlineCompanyName: invoiceOriginal.airlineCompanyName
                                };
                            }
                        }

                    } else {
                        invoiceOriginal.invoiceOriginalTypeCode = invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeCode;
                        invoiceOriginal.invoiceOriginalTypeName = invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeName;
                    }
                };
                if (defer2) {
                    defer2.promise.then(function () {
                        angular.forEach(scope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                            angular.forEach(invoiceOriginal.airlines, function (airline) {
                                angular.forEach(airline.costs, function (cost) {
                                    scope.initCost(cost);
                                });
                            });
                        });
                    });
                }

                if (defer3) {
                    defer3.promise.then(function () {
                        angular.forEach(scope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                            angular.forEach(invoiceOriginal.airlines, function (airline) {
                                angular.forEach(airline.salePrices, function (salePrice) {
                                    scope.initCost(salePrice);
                                });
                            });
                        });
                    });
                }


                scope.addInvoiceOriginal = function () {
                    scope.invoiceOriginalGroup.invoiceOriginals.push({
                        airlines: [{
                            costs     : [{}],
                            salePrices: [{}]
                        }]
                    })
                };
                scope.deleteAirline = function (invoiceOriginal, airline) {
                    var hasPayedCost = false;
                    var committed = false;
                    angular.forEach(airline.costs, function (cost) {
                        if (cost.orderCostStateCode == '1') {
                            hasPayedCost = true;
                            return false;
                        }
                        if (cost.orderCostStateCode == '3') {
                            committed = true;
                            return false;
                        }
                    })
                    if (hasPayedCost) {
                        qcDialog.openMessage({msg: '该航段存在已付款成本，无法删除'});
                        return;
                    }
                    if (committed) {
                        qcDialog.openMessage({msg: '是否撤回已提交成本并删除该航段', type: 'confirmCancel'})
                            .then(function () {
                                angular.forEach(airline.costs, function (cost) {
                                    if (cost.orderCostStateCode == '3') {
                                        qcApi.post('mtourfinance/cancelCostRecord', {
                                            orderUuid: scope.orderUuid,
                                            costUuid : cost.costUuid
                                        }).success(function (result) {
                                        });
                                    }
                                });
                                invoiceOriginal.airlines.remove(airline);

                            }, function () {
//                                angular.forEach(airline.costs, function (cost) {
//                                    if (cost.orderCostStateCode == '3') {
//                                        qcApi.post('mtourfinance/cancelCostRecord', {
//                                            orderUuid: scope.orderUuid,
//                                            costUuid : cost.costUuid
//                                        }).success(function (result) {
//                                            cost.orderCostStateCode = 2;
//                                        });
//                                    }
//                                });//点击取消只撤回不删除
////                                alert(1);
                                scope.closeThisDialog();
                            });
                    } else {
                        invoiceOriginal.airlines.remove(airline);
                    }
                }

                scope.cancelCost = function (cost) {
                    qcDialog.openMessage({
                        msg : '是否撤回成本',
                        type: 'confirm'
                    }).then(function () {
                        qcApi.post('mtourfinance/cancelCostRecord', {
                            orderUuid: scope.orderUuid,
                            costUuid : cost.costUuid
                        }).success(function (result) {
                            cost.orderCostStateCode = 2;
                        });
                    });
                };


                scope.removeCost = function (cost, costs) {
                    costs.remove(cost);
                    scope.$emit('fee.change');
                };
                scope.removeSalePrice = function (salePrice, salePrices) {
                    salePrices.remove(salePrice);
                    scope.$emit('fee.change');
                };

                scope.$on('invoiceOriginal.invoiceOriginalType.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.invoiceOriginalTypeCode = $e.targetScope.$parent.invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeCode;
                    $e.targetScope.$parent.invoiceOriginal.invoiceOriginalTypeName = $e.targetScope.$parent.invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeName;
                });
                scope.$on('invoiceOriginal.costTourOperatorType.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorTypeCode = $e.targetScope.$parent.invoiceOriginal.costTourOperatorType.tourOperatorTypeCode;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorTypeName = $e.targetScope.$parent.invoiceOriginal.costTourOperatorType.tourOperatorTypeName;
                    $e.targetScope.$parent.invoiceOriginal.tourOperator = null;
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorUuid = undefined;
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorName = undefined;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorUuid = undefined;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorName = undefined;
                    $e.targetScope.$parent.invoiceOriginal.costBanks = [];
                    $e.targetScope.$parent.invoiceOriginal.selectedCostBank = null;
                    $e.targetScope.$parent.invoiceOriginal.selectedCostAccount = null;
                });
                //美途0024订单修改需求
                scope.$on('invoiceOriginal.tourOperator.change', function ($e) {
                    if (scope.companyRoleCode == '0') {
                        //美途0134需求注销
//                        if ($e.targetScope.$parent.invoiceOriginal.tourOperatorUuid != $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid
//                            && !getObjectByProp(oldInvoiceOriginalGroup, 'tourOperatorUuid', $e.targetScope.$parent.$parent.invoiceOriginal.tourOperator.tourOperatorUuid)
//                            ) {
//                            scope.showBankYN = true;
//                        }
//                        else {
//                            scope.showBankYN = false;
//                        }
                        $e.targetScope.$parent.invoiceOriginal.costBankName = null;
                        $e.targetScope.$parent.invoiceOriginal.costAccountNo = null;
                    }

                    $e.targetScope.$parent.invoiceOriginal.tourOperatorUuid = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid;
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorName = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorName;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorUuid = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorName = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorName;
                    qcApi.post('common/getBankInfo', {
                        type: '1',
                        uuid: $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid
                    })
                        .success(function (result) {
                            $e.targetScope.$parent.invoiceOriginal.costBanks = result.data;
                            $e.targetScope.$parent.invoiceOriginal.selectedCostBank = null;
                            $e.targetScope.$parent.invoiceOriginal.selectedCostAccount = null;
                        });
                });


                scope.$on('invoiceOriginal.selectedCostBank.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.costBankName = $e.targetScope.$parent.invoiceOriginal.selectedCostBank.bankName;
                });
                scope.$on('invoiceOriginal.selectedCostAccount.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.costAccountNo = $e.targetScope.$parent.invoiceOriginal.selectedCostAccount.accountNo;
                });
                scope.$on('invoiceOriginal.airlineCompany.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.airlineCompanyUuid = $e.targetScope.$parent.invoiceOriginal.airlineCompany.airlineCompanyUuid;
                    $e.targetScope.$parent.invoiceOriginal.airlineCompanyName = $e.targetScope.$parent.invoiceOriginal.airlineCompany.airlineCompanyName;
                });
                scope.$on('cost.currency.change', function ($e) {
                    $e.targetScope.$parent.cost.currencyUuid = $e.targetScope.$parent.cost.currency.currencyUuid;
                    $e.targetScope.$parent.cost.currencyCode = $e.targetScope.$parent.cost.currency.currencyCode;
                    $e.targetScope.$parent.cost.exchangeRate = $e.targetScope.$parent.cost.currency.exchangeRate;
                });
                scope.$on('salePrice.currency.change', function ($e) {
                    $e.targetScope.$parent.salePrice.currencyUuid = $e.targetScope.$parent.salePrice.currency.currencyUuid;
                    $e.targetScope.$parent.salePrice.currencyCode = $e.targetScope.$parent.salePrice.currency.currencyCode;
                    $e.targetScope.$parent.salePrice.exchangeRate = $e.targetScope.$parent.salePrice.currency.exchangeRate;
                });

                scope.$watch('editModel', function () {
                    if (scope.editModel == 'edit') {
                        initInvoiceOriginalGroup();
                    }
                });
                //scope.$on('currentOrder.editModel.reset', function ($e,editModel) {
                //    if(editModel=='edit'){
                //        initInvoiceOriginalGroup();
                //    }
                //});
                if (defer1) {
                    defer1.promise.then(function () {
                        initInvoiceOriginalGroup();
                    });
                }

            }
        };
    }]);

mtour.directive('orderRegFeePriceChange', ['urlConfig', 'commonValue', 'qcObjectInArray', '$rootScope', function (urlConfig, commonValue, qcObjectInArray, $rootScope) {
    return {
        restrict   : 'A',
        scope      : {
            priceChange: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-fee-priceChange.html',
        link       : function (scope, ele, attrs) {
            scope.currencies = commonValue.currencies;
            scope.changeTypes = ['+', '-'];
            scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
            //scope.companyRoleCode = '0';
            if (!scope.priceChange) {
                scope.priceChange = {
                    additionalCosts: [],
                    salePrices     : []
                };
                scope.editModel = 'edit';
            }
            scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
            scope.initAdditionalCost = function (additionalCost) {
                if (!additionalCost.currency) {
                    if (angular.isUndefined(additionalCost.currencyUuid)) {
                        additionalCost.currency = scope.defaultCurrency;
                        additionalCost.currencyUuid = scope.defaultCurrency.currencyUuid;
                        additionalCost.currencyCode = scope.defaultCurrency.currencyCode;
                        additionalCost.exchangeRate = scope.defaultCurrency.exchangeRate;
                    } else {
                        additionalCost.currency = qcObjectInArray(scope.currencies, 'currencyUuid', additionalCost.currencyUuid);
                        additionalCost.currencyCode = additionalCost.currency.currencyCode;
                        //additionalCost.exchangeRate = additionalCost.currency.exchangeRate;

                    }
                }
                ;
            }
            scope.initSalePrice = function (salePrice) {
                if (!salePrice.currency) {
                    if (angular.isUndefined(salePrice.currencyUuid)) {
                        salePrice.currency = scope.defaultCurrency;
                        salePrice.currencyUuid = scope.defaultCurrency.currencyUuid;
                        salePrice.currencyCode = scope.defaultCurrency.currencyCode;
                        salePrice.exchangeRate = scope.defaultCurrency.exchangeRate;
                    } else {
                        salePrice.currency = qcObjectInArray(scope.currencies, 'currencyUuid', salePrice.currencyUuid);
                        salePrice.currencyCode = salePrice.currency.currencyCode;
                        //salePrice.exchangeRate =  salePrice.currency.exchangeRate;
                    }
                }
                if (salePrice.changeType != '-') {
                    salePrice.changeType = '+';
                }
            };

            scope.showAdditionalCost = function () {
                if (!scope.priceChange.additionalCosts) {
                    scope.priceChange.additionalCosts = [];
                }
                if (scope.priceChange.additionalCosts.length == 0) {
                    scope.priceChange.additionalCosts.push({});
                }
                scope.priceChange.showType = 'additional';
                scope.showType = 'additional';
            };
            scope.showSalePrice = function () {
                if (!scope.priceChange.salePrices) {
                    scope.priceChange.salePrices = [];
                }
                if (scope.priceChange.salePrices.length == 0) {
                    scope.priceChange.salePrices.push({});
                }
                scope.priceChange.showType = 'salePrice';
                scope.showType = 'salePrice';
            };
            scope.removeAdditionalCost = function (additionalCost) {
                scope.priceChange.additionalCosts.remove(additionalCost)
                scope.$emit('fee.change');
            }
            scope.removeSalePrice = function (salePrice) {
                scope.priceChange.salePrices.remove(salePrice)
                scope.$emit('fee.change');
            }
            scope.$on('additionalCost.currency.change', function ($e) {
                $e.targetScope.$parent.additionalCost.currencyUuid = $e.targetScope.$parent.additionalCost.currency.currencyUuid;
                $e.targetScope.$parent.additionalCost.currencyCode = $e.targetScope.$parent.additionalCost.currency.currencyCode;
                $e.targetScope.$parent.additionalCost.exchangeRate = $e.targetScope.$parent.additionalCost.currency.exchangeRate;
            });
            scope.$on('salePrice.currency.change', function ($e) {
                $e.targetScope.$parent.salePrice.currencyUuid = $e.targetScope.$parent.salePrice.currency.currencyUuid;
                $e.targetScope.$parent.salePrice.currencyCode = $e.targetScope.$parent.salePrice.currency.currencyCode;
                $e.targetScope.$parent.salePrice.exchangeRate = $e.targetScope.$parent.salePrice.currency.exchangeRate;
            });
            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
                if (!scope.showType && scope.priceChange) {
                    if (scope.priceChange.salePrices && scope.priceChange.salePrices.length) {
                        scope.showType = 'salePrice';
                        return;
                    }
                    if (scope.priceChange.additionalCosts && scope.priceChange.additionalCosts.length) {
                        scope.showType = 'additional';
                        return;
                    }
                }
            });
            scope.$on('currentOrder.saveModel.reset', function ($e, saveModel) {
                scope.saveModel = saveModel;
            });
        }

    };
}]);


mtour.directive('orderRegFeeTotal', ['urlConfig', 'groupAmount', 'calculateTotalFee', '$timeout', 'commonValue', 'amountsToRMB', 'qcObjectInArray',
    function (urlConfig, groupAmount, calculateTotalFee, $timeout, commonValue, amountsToRMB, qcObjectInArray) {
        return {
            restrict   : 'A',
            scope      : {
                fee      : '=',
                saveModel: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-fee-total.html',
            link       : function (scope, ele, attrs) {

                function setAmountExchangeRate(amounts) {
                    //angular.forEach(amounts, function (amount) {
                    //    var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                    //    amount.exchangeRate = currency ? currency.exchangeRate : undefined;
                    //});
                    return amounts;
                }

                scope.setTotalAmount = function () {
                    var totalAmount = calculateTotalFee(scope.fee.invoiceOriginalGroups, scope.fee.priceChange, scope.fee.returnAmount, scope.fee.receivedAmount, scope.fee.arrivedAmount);
                    scope.totalSalePrice = amountsToRMB(setAmountExchangeRate(totalAmount.totalSalePrice));
                    scope.orderAmount = amountsToRMB(setAmountExchangeRate(totalAmount.orderAmount));
                    scope.totalCost = amountsToRMB(setAmountExchangeRate(totalAmount.totalCost));
                    scope.return = amountsToRMB(setAmountExchangeRate(totalAmount.return));
                    scope.profit = amountsToRMB(setAmountExchangeRate(totalAmount.profit));
                    scope.receivable = amountsToRMB(setAmountExchangeRate(totalAmount.receivable));
                    scope.received = amountsToRMB(setAmountExchangeRate(totalAmount.received));
                    scope.unreceive = amountsToRMB(setAmountExchangeRate(totalAmount.unreceive));
                    scope.arrived = amountsToRMB(setAmountExchangeRate(totalAmount.arrived));
                };
                scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                    scope.editModel = editModel;
                    scope.setTotalAmount()
                });
                scope.$on('currentOrder.saveModel.reset', function ($e, saveModel) {
                    scope.saveModel = saveModel;
                    scope.setTotalAmount()
                });

                scope.$on('fee.changed', function ($e, saveModel) {
                    $timeout(function () {
                        scope.setTotalAmount();
                    });
                });
                scope.setTotalAmount();
            }
        };
    }]);

mtour.directive('orderRegFlight', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            flights: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-flight.html',
        link       : function (scope, ele, attrs) {
            function initFlights() {
                if (!angular.isArray(scope.flights)) {
                    scope.flights = [{}];
                }
            }

            scope.initFlight = function (flight) {
                flight.departureAirport = {
                    airportUuid: flight.departureAirportUuid,
                    airportName: flight.departureAirportName,
                    airportCode: flight.departureAirportCode
                };
                flight.arrivalAirport = {
                    airportUuid: flight.arrivalAirportUuid,
                    airportName: flight.arrivalAirportName,
                    airportCode: flight.arrivalAirportCode
                };
            }

            scope.formatDate = function (day, hour, minute) {
                if (!day) {
                    return '';
                }
                if (!hour) {
                    hour = '00';
                }
                if (!minute) {
                    minute = '00';
                }
                return day + ' ' + hour + ':' + minute;
            };
            scope.$on('flight.departureAirport.change', function ($e) {
                $e.targetScope.$parent.flight.departureAirportUuid = $e.targetScope.$parent.flight.departureAirport.airportUuid;
                $e.targetScope.$parent.flight.departureAirportName = $e.targetScope.$parent.flight.departureAirport.airportName;
            });
            scope.$on('flight.arrivalAirport.change', function ($e) {
                $e.targetScope.$parent.flight.arrivalAirportUuid = $e.targetScope.$parent.flight.arrivalAirport.airportUuid;
                $e.targetScope.$parent.flight.arrivalAirportName = $e.targetScope.$parent.flight.arrivalAirport.airportName;
            });
            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
            });
            scope.$on('toggleAll', function ($e, fold) {
                scope.fold = fold;
            });
            initFlights();
        }
    };
}]);


mtour.directive('orderRegTraveler', ['urlConfig', 'commonValue', 'qcObjectInArray', function (urlConfig, commonValue, qcObjectInArray) {
    return {
        restrict   : 'A',
        scope      : {
            travelers: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-traveler.html',
        link       : function (scope, ele, attrs) {
            function initTravelers() {
                if (!angular.isArray(scope.travelers)) {
                    scope.travelers = [{}];
                }
            }

            scope.currencies = commonValue.currencies;
            scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
            scope.sexes = commonValue.sexes;
            scope.visaTypes = commonValue.visaTypes;
            scope.credentialsTypes = commonValue.credentialsTypes;

            scope.formatVisa = function (visa) {
                if (!visa) {
                    return;
                }
                var formatString = '';
                if (visa.visaCountryName) {
                    formatString += visa.visaCountryName;
                }
                if (visa.visaTypeName) {
                    if (formatString) {
                        formatString += '/' + visa.visaTypeName;
                    } else {
                        formatString += visa.visaTypeName;
                    }
                }
                return formatString;
            };
            scope.formatCredential = function (credential) {
                if (!credential) {
                    return;
                }
                var formatString = '';
                if (credential.credentialsTypeName) {
                    formatString += credential.credentialsTypeName;
                }
                if (credential.credentialsNo) {
                    if (formatString) {
                        formatString += '/' + credential.credentialsNo;
                    } else {
                        formatString += credential.credentialsNo;
                    }
                }
                if (credential.credentialsExpire) {
                    if (formatString) {
                        formatString += '/' + credential.credentialsExpire;
                    } else {
                        formatString += credential.credentialsExpire;
                    }
                }
                return formatString;
            }
            scope.initTraveler = function (traveler) {
                if (!traveler.visas) {
                    traveler.visas = [{}];
                }
                if (!traveler.credentials) {
                    traveler.credentials = [{}];
                }
                if (!traveler.sex && traveler.sexCode) {
                    traveler.sex = qcObjectInArray(scope.sexes, 'sexCode', traveler.sexCode);
                    if (traveler.sex) {
                        traveler.sexName = traveler.sex.sexName;
                    }
                }
                if (!traveler.currency) {
                    if (traveler.currencyUuid) {
                        traveler.currency = qcObjectInArray(scope.currencies, 'currencyUuid', traveler.currencyUuid);
                        traveler.currencyCode = traveler.currency.currencyCode;
                    }
                    else {
                        traveler.currency = scope.defaultCurrency;
                        traveler.currencyUuid = scope.defaultCurrency.currencyUuid;
                        traveler.currencyName = scope.defaultCurrency.currencyName;
                        traveler.currencyCode = scope.defaultCurrency.currencyCode;
                    }
                }
            };
            scope.initVisa = function (visa) {
                if (!visa.country && !angular.isUndefined(visa.visaCountryUuid)) {
                    visa.country = {
                        countryUuid: visa.visaCountryUuid,
                        countryName: visa.visaCountryName,
                        countryId  : visa.visaCountryId
                    };
                }
                if (!visa.visaType) {
                    visa.visaType = qcObjectInArray(scope.visaTypes, 'visaTypeCode', visa.visaTypeCode);
                    if (visa.visaType) {
                        visa.visaTypeName = visa.visaType.visaTypeName;
                    }
                }
            }
            scope.initCredential = function (credential) {
                if (!credential.credentialsType && !angular.isUndefined(credential.credentialsTypeCode)) {
                    //credential.credentialsType = {
                    //    credentialsTypeCode: credential.credentialsTypeCode,
                    //    credentialsTypeName: credential.credentialsTypeName
                    //};
                    credential.credentialsType = qcObjectInArray(scope.credentialsTypes, 'credentialsTypeCode', credential.credentialsTypeCode);
                    if (credential.credentialsType) {
                        credential.credentialsTypeName = credential.credentialsType.credentialsTypeName;
                    }
                }
            }
            scope.$on('traveler.sex.change', function ($e) {
                $e.targetScope.$parent.traveler.sexCode = $e.targetScope.$parent.traveler.sex.sexCode;
                $e.targetScope.$parent.traveler.sexName = $e.targetScope.$parent.traveler.sex.sexName;
            });
            scope.$on('traveler.currency.change', function ($e) {
                $e.targetScope.$parent.traveler.currencyUuid = $e.targetScope.$parent.traveler.currency.currencyUuid;
                $e.targetScope.$parent.traveler.currencyCode = $e.targetScope.$parent.traveler.currency.currencyCode;
            });
            scope.$on('visa.country.change', function ($e) {
                $e.targetScope.$parent.visa.visaCountryUuid = $e.targetScope.$parent.visa.country.countryUuid;
                $e.targetScope.$parent.visa.visaCountryName = $e.targetScope.$parent.visa.country.countryName;
                $e.targetScope.$parent.visa.visaCountryId = $e.targetScope.$parent.visa.country.countryId;
            });
            scope.$on('visa.visaType.change', function ($e) {
                $e.targetScope.$parent.visa.visaTypeCode = $e.targetScope.$parent.visa.visaType.visaTypeCode;
                $e.targetScope.$parent.visa.visaTypeName = $e.targetScope.$parent.visa.visaType.visaTypeName;
            });
            scope.$on('credential.credentialsType.change', function ($e) {
                $e.targetScope.$parent.credential.credentialsTypeCode = $e.targetScope.$parent.credential.credentialsType.credentialsTypeCode;
                $e.targetScope.$parent.credential.credentialsTypeName = $e.targetScope.$parent.credential.credentialsType.credentialsTypeName;
            });
            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
            });
            scope.$on('toggleAll', function ($e, fold) {
                scope.fold = fold;
            });
            initTravelers();
        }
    };
}]);


mtour.directive('orderRegUpload', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        scope      : {
            attachment: '=',
            editModel : '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-upload.html',
        link       : function (scope, ele, attrs) {
            scope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
            if (!angular.isArray(scope.newAttachments)) {
                scope.newAttachments = [];
            }
            if (!scope.editModel) {
                scope.editModel = 'detail';
            }
            scope.qcUploaderOptions = {
                makeThumb: true,
                swf      : urlConfig.mtourStaticUrl + 'components/flash/Uploader.swf',
                server   : urlConfig.mtourApiUrl + 'common/upload?requestType=mtour data',
                //accept: {
                //    title: 'Images',
                //    extensions: 'gif,jpg,jpeg,bmp,png',
                //    mimeTypes: 'image/*'
                //},
                accept   : null,
                auto     : true,
                resize   : false
            };
            scope.remove = function (file) {
                qcApi.post('common/delFile', {attachmentUuid: file.attachmentUuid}).success(function () {
                    scope.attachment.remove(file);
                });
            }
            scope.$on('uploader.completed', function ($e, uploadedFile) {
                var file = {
                    attachmentUuid: uploadedFile.attachmentUuid,
                    attachmentUrl : uploadedFile.attachmentUrl,
                    fileName      : uploadedFile.fileName,
                    status        : uploadedFile.status
                };
                if (!angular.isArray(scope.attachment)) {
                    scope.attachment = [];
                }
                scope.attachment.push(file);
                //scope.newAttachments.remove(uploadedFile);
                scope.$broadcast('uploader.removeFile', uploadedFile.id);
            });
        }
    };
}]);


mtour.directive('orderRegMemo', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            memo: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-memo.html',
        link       : function (scope, ele, attrs) {
            scope.editModel = 'edit';
            scope.$on('currentOrder.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
            });
            scope.$on('toggleAll', function ($e, fold) {
                scope.fold = fold;
            });
        }
    };
}]);


mtour.directive('city', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {},
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/city.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedCity = modelValue;
            });
            scope.$on('selectedCity.search', function ($e, filterText) {
                qcApi.post('common/getCity', {
                    cityKey: filterText,//模糊搜索
                    count  : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.cities = result.data;
                });
            });
            scope.$on('selectedCity.change', function () {
                ngModel.$setViewValue(scope.selectedCity);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);


mtour.directive('lineCountry', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {},
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/lineCountry.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedCountry = modelValue;
            });
            scope.$on('selectedCountry.search', function ($e, filterText) {
                qcApi.post('common/getCountryVaguely', {
                    countryKey: filterText,//模糊搜索
                    count     : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.countries = result.data;
                });
            });
            scope.$on('selectedCountry.change', function () {
                ngModel.$setViewValue(scope.selectedCountry);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);

mtour.directive('visaCountry', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {},
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/visaCountry.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedCountry = modelValue;
            });
            scope.$on('selectedCountry.search', function ($e, filterText) {
                qcApi.post('common/getCountryVaguely', {
                    countryKey: filterText,//模糊搜索
                    count     : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.countries = result.data;
                });
            });
            scope.$on('selectedCountry.change', function () {
                ngModel.$setViewValue(scope.selectedCountry);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);

mtour.directive('user', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {
            userType: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/user.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedUser = modelValue;
            });
            scope.$on('selectedUser.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: scope.userType,
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.users = result.data;
                });
            });
            scope.$on('selectedUser.change', function () {
                ngModel.$setViewValue(scope.selectedUser);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);
mtour.directive('manyuser', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {
            userType: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/many-user.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedUser = modelValue;
            });
            scope.$on('selectedUser.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: scope.userType,
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.users = result.data;
                });
            });
            /*  scope.select = function (qcDropdownItem) {
             ngModel.$setViewValue(qcDropdownItem);
             scope.$emit(attrs.ngModel+'.change');
             };*/
            scope.$on('selectedUser.change', function () {
                ngModel.$setViewValue(scope.selectedUser);
                //"baseInfo.tickets"
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);

mtour.directive('channel', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {
            channelTypeCode: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/channel.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedChannel = modelValue;
            });
            scope.$on('selectedChannel.search', function ($e, filterText) {
                qcApi.post('common/getAgentinfoByTypeCode', {
                    channelTypeCode: scope.channelTypeCode,
                    channelName    : filterText,//模糊搜索
                    count          : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.channels = result.data;
                });
            });
            scope.$on('selectedChannel.change', function () {
                ngModel.$setViewValue(scope.selectedChannel);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);

mtour.directive('tourOperator', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            tourOperatorTypeCode: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/tourOperator.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedTourOperator = modelValue;
            });
            scope.$on('selectedTourOperator.search', function ($e, filterText) {
                qcApi.post('common/getSupplierList', {
                    tourOperatorTypeCode: scope.tourOperatorTypeCode,
                    tourOperatorName    : filterText,//模糊搜索
                    count               : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.tourTourOperators = result.data;
                });
            });
            scope.$on('selectedTourOperator.change', function () {
                ngModel.$setViewValue(scope.selectedTourOperator);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);


mtour.directive('airlineCompany', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {},
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/airlineCompany.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedAirlineCompany = modelValue;
            });
            scope.$on('selectedAirlineCompany.search', function ($e, filterText) {
                qcApi.post('common/getAirlineVaguely', {
                    airlineCompanyKey: filterText,//模糊搜索
                    count            : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.airlineCompanys = result.data;
                });
            });
            scope.$on('selectedAirlineCompany.change', function () {
                ngModel.$setViewValue(scope.selectedAirlineCompany);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);


mtour.directive('airport', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        scope      : {},
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/airport.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedAirport = modelValue;
            });
            scope.$on('selectedAirport.search', function ($e, filterText) {
                qcApi.post('common/getAirportVaguely', {
                    airportKey: filterText,//模糊搜索
                    count     : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.airports = result.data;
                });
            });
            scope.$on('selectedAirport.change', function () {
                ngModel.$setViewValue(scope.selectedAirport);
                scope.$emit(attrs.ngModel + '.change');
            });
        }
    };
}]);

mtour.factory('calculateTotalFee', ['groupAmount', 'commonValue', 'qcObjectInArray', function (groupAmount, commonValue, qcObjectInArray) {
    return function (invoiceOriginalGroups, priceChange, returnAmount, receivedAmount, arrivedAmount) {
        //外保总额
        function getTotalSalePrice() {
            var totalSalePrices = [];
            angular.forEach(invoiceOriginalGroups, function (invoiceOriginalGroup) {
                angular.forEach(invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                    angular.forEach(invoiceOriginal.airlines, function (airline) {
                        angular.forEach(airline.salePrices, function (salePrice) {
                            if (salePrice.peopleCount * salePrice.costUnitPrice) {
                                totalSalePrices.push({
                                    currencyUuid: salePrice.currencyUuid,
                                    amount      : salePrice.peopleCount * salePrice.costUnitPrice,
                                    exchangeRate: salePrice.exchangeRate
                                });
                            }
                        });
                    });
                });
            });
            return totalSalePrices;
        }

        function getTotalDeposit() {
            var totalDeposits = [];
            angular.forEach(invoiceOriginalGroups, function (invoiceOriginalGroup) {
                angular.forEach(invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                    angular.forEach(invoiceOriginal.airlines, function (airline) {
                        angular.forEach(airline.salePrices, function (salePrice) {
                            if (salePrice.totalDeposit) {
                                totalDeposits.push({
                                    currencyUuid: salePrice.currencyUuid,
                                    amount      : salePrice.totalDeposit,
                                    exchangeRate: salePrice.exchangeRate
                                });
                            }
                        });
                    });
                });
            });
            return totalDeposits;
        }

        //成本总额
        function getTotalCost() {
            var totalCosts = [];
            angular.forEach(invoiceOriginalGroups, function (invoiceOriginalGroup) {
                angular.forEach(invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                    angular.forEach(invoiceOriginal.airlines, function (airline) {
                        angular.forEach(airline.costs, function (cost) {
                            if (cost.peopleCount * cost.costUnitPrice) {
                                totalCosts.push({
                                    currencyUuid: cost.currencyUuid,
                                    amount      : cost.peopleCount * cost.costUnitPrice,
                                    exchangeRate: cost.exchangeRate
                                });
                            }
                        });
                    });
                });
            });
            return totalCosts;
        }

        //外报价修改
        function getChangedSalePrice() {
            var changedSalePrices = [];
            if (priceChange && angular.isArray(priceChange.salePrices)) {
                angular.forEach(priceChange.salePrices, function (salePrice) {
                    if (salePrice.amount) {
                        changedSalePrices.push({
                            currencyUuid: salePrice.currencyUuid,
                            amount      : salePrice.amount * (salePrice.changeType === '+' ? 1 : -1),
                            exchangeRate: salePrice.exchangeRate
                        });
                    }
                })
            }
            return changedSalePrices;
        }

        //价格修改追加成本
        function getChangeAdditionalPrice() {
            var changedAdditionalPrices = [];
            if (priceChange && angular.isArray(priceChange.additionalCosts)) {
                angular.forEach(priceChange.additionalCosts, function (additionalCostPrice) {
                    if (additionalCostPrice.amount) {
                        changedAdditionalPrices.push({
                            currencyUuid: additionalCostPrice.currencyUuid,
                            amount      : additionalCostPrice.amount,
                            exchangeRate: additionalCostPrice.exchangeRate
                        });
                    }
                })
            }
            return changedAdditionalPrices;
        }


        //获取退款金额
        function getReturn() {
            if (!returnAmount) {
                return [];
            } else {
                return returnAmount;
            }
        }

        //已收金额
        function getReceivedAmount() {
            if (!receivedAmount) {
                return [];
            } else {
                return receivedAmount;
            }
        }

        //未收金额(应收-已收)
        //未收金额(应收-到账)  update by shijun.liu 0293  2016.05.06
        function getUnreceiveAmount() {
            //var amounts = getReceivedAmount();//已收
            var amounts = getArrivedAmount();//到账
            var minusAmounts = [];
            angular.forEach(amounts, function (amount) {
                var minusAmount = {
                    amount      : -amount.amount,
                    currencyUuid: amount.currencyUuid,
                    //currencyCode: amount.currencyCode
                    exchangeRate: amount.exchangeRate ? amount.exchangeRate : amount.exchangerate
                };
                minusAmounts.push(minusAmount)
            });
            return getTotalSalePrice().concat(getChangedSalePrice()).concat(minusAmounts);
        }

        //到账金额
        function getArrivedAmount() {
            if (!arrivedAmount) {
                return [];
            } else {
                return arrivedAmount;
            }
        }

        //预计利润(订单总额-成本总额-退款总额)
        function getProfit() {
            //订单总额
            var orderTotal = getTotalSalePrice().concat(getChangedSalePrice());
            //成本总额和退款总额
            var amounts = getTotalCost().concat(getChangeAdditionalPrice()).concat(getReturn());
            var minusAmounts = [];
            angular.forEach(amounts, function (amount) {
                var minusAmount = {
                    amount      : -amount.amount,
                    currencyUuid: amount.currencyUuid,
                    currencyCode: amount.currencyCode,
                    exchangeRate: amount.exchangeRate
                };
                minusAmounts.push(minusAmount)
            });
            return orderTotal.concat(minusAmounts);
        }


        //统计订单总额
        function getGroupedOrderAmount() {
            return groupAmount(getTotalSalePrice().concat(getChangedSalePrice()));
        };

        function getGroupedTotalDeposit() {
            return groupAmount(getTotalDeposit());
        }

        //统计外报总额
        function getGroupedTotalSalePrice() {
            return groupAmount(getTotalSalePrice());
        };
        //统计成本总额
        function getGroupTotalCost() {
            return groupAmount(getTotalCost().concat(getChangeAdditionalPrice()));
        };
        //统计退款总额
        function getGroupedReturn() {
            return groupAmount(getReturn());
        };
        //统计利润
        function getGroupedProfit() {
            return groupAmount(getProfit());
        };
        //应收金额=订单总额
        //统计已收金额
        function getGroupedReceivedAmount() {
            return groupAmount(getReceivedAmount());
        }

        //统计未收金额
        function getGroupedUnreceiveAmount() {
            return groupAmount(getUnreceiveAmount());
        }

        //统计到账金额
        function getGroupedArrivedAmount() {
            return groupAmount(getArrivedAmount());
        }

        function setAmountExchangeRate(amounts) {
            //angular.forEach(amounts, function (amount) {
            //    var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
            //    amount.exchangeRate = currency ? currency.exchangeRate : undefined;
            //});
            return amounts;
        }

        return {
            totalSalePrice: setAmountExchangeRate(getGroupedTotalSalePrice()),
            orderAmount   : setAmountExchangeRate(getGroupedOrderAmount()),
            totalDeposit  : setAmountExchangeRate(getGroupedTotalDeposit()),
            totalCost     : setAmountExchangeRate(getGroupTotalCost()),
            return        : setAmountExchangeRate(getGroupedReturn()),
            profit        : setAmountExchangeRate(getGroupedProfit()),
            receivable    : setAmountExchangeRate(getGroupedOrderAmount()),
            received      : setAmountExchangeRate(getGroupedReceivedAmount()),
            unreceive     : setAmountExchangeRate(getGroupedUnreceiveAmount()),
            arrived       : setAmountExchangeRate(getGroupedArrivedAmount())
        };

    }
}]);


//大编号下的航班金额 和 总的价格修改编号后
mtour.directive('feeWatch', function () {
    return {
        restrict: 'A',
        require : '^ngModel',
        link    : function (scope, ele, attrs, ngModel) {
            ngModel.$parsers.push(function (viewValue) {
                scope.$emit('fee.change');
                return viewValue;
            });
        }
    };
});

//订单追散及非美途用户用的航段信息组件
mtour.directive('addOrderRegFeeInvoiceOriginalGroup', ['urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcApi', '$q', '$rootScope',
    function (urlConfig, fixedValue, commonValue, qcObjectInArray, qcApi, $q, $rootScope) {
        return {
            restrict   : 'A',
            scope      : {
                invoiceOriginalGroup: '=',
                editModel           : '=',
                saveModel           : '=',
                orderUuid           : '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReg-fee-invoiceOriginalGroup.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                scope.imgUrl = urlConfig.mtourStaticUrl + 'img/';
                scope.invoiceOriginalTypes = fixedValue.invoiceOriginalTypes;
                scope.defaultInvoiceOriginal = qcObjectInArray(scope.invoiceOriginalTypes, 'invoiceOriginalTypeCode', '0');
                scope.currencies = commonValue.currencies;
                scope.selectedCurrency = scope.currencies[0];
                scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
                if (scope.orderUuid) {
                    var defer1 = $q.defer();
                    var defer2 = $q.defer();
                    var defer3 = $q.defer();
                    qcApi.post('order/getExistsCurrency?date=' + new Date(), {
                        orderId: scope.orderUuid,
                        type   : '2'
                    }).success(function (result) {
                        scope.currencies = result.data;
                        scope.selectedCurrency = scope.currencies[0];
                        scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
                        defer1.resolve();
                        defer2.resolve();
                        defer3.resolve();
                    });
                }
                else {
                    scope.currencies = commonValue.currencies;
                    scope.selectedCurrency = scope.currencies[0];
                    scope.defaultCurrency = qcObjectInArray(scope.currencies, 'currencyCode', '¥');
                }

                scope.tourOperatorTypes = commonValue.tourOperatorTypes;

                function initInvoiceOriginalGroup() {
                    if (!scope.invoiceOriginalGroup.invoiceOriginals) {
                        scope.invoiceOriginalGroup.invoiceOriginals = [
                            {
                                airlines: [{
                                    costs     : [{}],
                                    salePrices: [{}]
                                }]
                            }
                        ];
                    }
                }

                scope.initCost = function (cost) {
                    if (!cost.currency) {
                        if (!cost.currencyUuid) {
                            cost.currency = scope.defaultCurrency;
                            cost.currencyUuid = scope.defaultCurrency.currencyUuid;
                            cost.currencyCode = scope.defaultCurrency.currencyCode;
                            cost.exchangeRate = scope.defaultCurrency.exchangeRate;
                        } else {
                            cost.currency = qcObjectInArray(scope.currencies, 'currencyUuid', cost.currencyUuid);
                            if (cost.currency && cost.exchangeRate) {
                                cost.currency.exchangeRate = cost.exchangeRate;
                                cost.currencyCode = cost.currency.currencyCode;
                            }
                        }
                    } else {
                        cost.currencyUuid = cost.currency.currencyUuid;
                        cost.currencyCode = cost.currency.currencyCode;
                        //cost.exchangeRate = cost.currency.exchangeRate;
                    }
                };

                //scope.initCost = function (cost) {
                //    //if(cost.currency){
                //    cost.currency = scope.defaultCurrency;
                //    cost.currencyUuid = scope.defaultCurrency.currencyUuid;
                //    cost.currencyCode = scope.defaultCurrency.currencyCode;
                //    cost.exchangeRate = scope.defaultCurrency.exchangeRate;
                //    //}
                //}

                scope.initSalePrice = function (salePrice) {
                    if (!salePrice.currency) {
                        if (!salePrice.currencyUuid) {
                            salePrice.currency = scope.defaultCurrency;
                            salePrice.currencyUuid = scope.defaultCurrency.currencyUuid;
                            salePrice.currencyCode = scope.defaultCurrency.currencyCode;
                            salePrice.exchangeRate = scope.defaultCurrency.exchangeRate;
                        } else {
                            salePrice.currency = qcObjectInArray(scope.currencies, 'currencyUuid', salePrice.currencyUuid);
                            if (salePrice.currency && salePrice.exchangeRate) {
                                salePrice.currency.exchangeRate = salePrice.exchangeRate;
                                salePrice.currencyCode = salePrice.currency.currencyCode;
                            }
                        }
                    } else {
                        salePrice.currencyUuid = salePrice.currency.currencyUuid;
                        salePrice.currencyCode = salePrice.currency.currencyCode;
                        salePrice.exchangeRate = salePrice.currency.exchangeRate;
                    }
                };
                scope.initInvoiceOriginal = function (invoiceOriginal, invoiceOriginalScope) {
                    invoiceOriginalScope.$on('toggleAll', function ($e, fold) {
                        invoiceOriginalScope.fold = fold;
                    });
                    if (!invoiceOriginal.invoiceOriginalType) {
                        if (angular.isUndefined(invoiceOriginal.invoiceOriginalTypeCode)) {
                            invoiceOriginal.invoiceOriginalType = scope.defaultInvoiceOriginal;
                            invoiceOriginal.invoiceOriginalTypeCode = '0';//PNR
                            invoiceOriginal.invoiceOriginalTypeName = 'PNR';//PNR
                        } else {
                            invoiceOriginal.invoiceOriginalType = qcObjectInArray(scope.invoiceOriginalTypes, 'invoiceOriginalTypeCode', invoiceOriginal.invoiceOriginalTypeCode);
                            invoiceOriginal.invoiceOriginalTypeName = invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeName;
                            if (!invoiceOriginal.costTourOperatorType) {
                                invoiceOriginal.costTourOperatorType = {
                                    tourOperatorTypeName: invoiceOriginal.costTourOperatorTypeName,
                                    tourOperatorTypeCode: invoiceOriginal.costTourOperatorTypeCode
                                };
                                invoiceOriginal.tourOperatorType = invoiceOriginal.costTourOperatorType;
                            }
                            if (!invoiceOriginal.tourOperator) {
                                invoiceOriginal.tourOperator = {
                                    tourOperatorUuid: invoiceOriginal.invoiceOriginalTypeCode != '0' ? invoiceOriginal.tourOperatorUuid : invoiceOriginal.costTourOperatorUuid,
                                    tourOperatorName: invoiceOriginal.costTourOperatorName
                                };
                                invoiceOriginal.tourOperatorUuid = invoiceOriginal.costTourOperatorUuid;
                                invoiceOriginal.tourOperatorName = invoiceOriginal.costTourOperatorName;
                            }
                            if (!invoiceOriginal.selectedCostBank && invoiceOriginal.costBankName) {
                                invoiceOriginal.selectedCostBank = {bankName: invoiceOriginal.costBankName};
                            }
                            if (!invoiceOriginal.selectedCostAccount && invoiceOriginal.costAccountNo) {
                                invoiceOriginal.selectedCostAccount = {accountNo: invoiceOriginal.costAccountNo};
                            }

                            if (!invoiceOriginal.airlineCompany && invoiceOriginal.airlineCompanyUuid) {
                                invoiceOriginal.airlineCompany = {
                                    airlineCompanyUuid: invoiceOriginal.airlineCompanyUuid,
                                    airlineCompanyName: invoiceOriginal.airlineCompanyName
                                };
                            }
                        }

                    } else {
                        invoiceOriginal.invoiceOriginalTypeCode = invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeCode;
                        invoiceOriginal.invoiceOriginalTypeName = invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeName;
                    }
                };
                if (defer2) {
                    defer2.promise.then(function () {
                        angular.forEach(scope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                            angular.forEach(invoiceOriginal.airlines, function (airline) {
                                angular.forEach(airline.costs, function (cost) {
                                    scope.initCost(cost);
                                });
                            });
                        });
                    });
                }

                if (defer3) {
                    defer3.promise.then(function () {
                        angular.forEach(scope.invoiceOriginalGroup.invoiceOriginals, function (invoiceOriginal) {
                            angular.forEach(invoiceOriginal.airlines, function (airline) {
                                angular.forEach(airline.salePrices, function (salePrice) {
                                    scope.initCost(salePrice);
                                });
                            });
                        });
                    });
                }


                scope.addInvoiceOriginal = function () {
                    scope.invoiceOriginalGroup.invoiceOriginals.push({
                        airlines: [{
                            costs     : [{}],
                            salePrices: [{}]
                        }]
                    })
                };

                scope.removeCost = function (cost, costs) {
                    costs.remove(cost);
                    scope.$emit('fee.change');
                };
                scope.removeSalePrice = function (salePrice, salePrices) {
                    salePrices.remove(salePrice);
                    scope.$emit('fee.change');
                };

                scope.$on('invoiceOriginal.invoiceOriginalType.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.invoiceOriginalTypeCode = $e.targetScope.$parent.invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeCode;
                    $e.targetScope.$parent.invoiceOriginal.invoiceOriginalTypeName = $e.targetScope.$parent.invoiceOriginal.invoiceOriginalType.invoiceOriginalTypeName;
                });
                scope.$on('invoiceOriginal.costTourOperatorType.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorTypeCode = $e.targetScope.$parent.invoiceOriginal.costTourOperatorType.tourOperatorTypeCode;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorTypeName = $e.targetScope.$parent.invoiceOriginal.costTourOperatorType.tourOperatorTypeName;
                    $e.targetScope.$parent.invoiceOriginal.tourOperator = null;
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorUuid = undefined;
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorName = undefined;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorUuid = undefined;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorName = undefined;
                    $e.targetScope.$parent.invoiceOriginal.costBanks = [];
                    $e.targetScope.$parent.invoiceOriginal.selectedCostBank = null;
                    $e.targetScope.$parent.invoiceOriginal.selectedCostAccount = null;
                });

                scope.$on('invoiceOriginal.tourOperator.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorUuid = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid;
                    $e.targetScope.$parent.invoiceOriginal.tourOperatorName = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorName;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorUuid = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid;
                    $e.targetScope.$parent.invoiceOriginal.costTourOperatorName = $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorName;
                    qcApi.post('common/getBankInfo', {
                        type: '1',
                        uuid: $e.targetScope.$parent.invoiceOriginal.tourOperator.tourOperatorUuid
                    })
                        .success(function (result) {
                            $e.targetScope.$parent.invoiceOriginal.costBanks = result.data;
                            $e.targetScope.$parent.invoiceOriginal.selectedCostBank = null;
                            $e.targetScope.$parent.invoiceOriginal.selectedCostAccount = null;
                        });
                });
                scope.$on('invoiceOriginal.selectedCostBank.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.costBankName = $e.targetScope.$parent.invoiceOriginal.selectedCostBank.bankName;
                });
                scope.$on('invoiceOriginal.selectedCostAccount.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.costAccountNo = $e.targetScope.$parent.invoiceOriginal.selectedCostAccount.accountNo;
                });
                scope.$on('invoiceOriginal.airlineCompany.change', function ($e) {
                    $e.targetScope.$parent.invoiceOriginal.airlineCompanyUuid = $e.targetScope.$parent.invoiceOriginal.airlineCompany.airlineCompanyUuid;
                    $e.targetScope.$parent.invoiceOriginal.airlineCompanyName = $e.targetScope.$parent.invoiceOriginal.airlineCompany.airlineCompanyName;
                });
                scope.$on('cost.currency.change', function ($e) {
                    $e.targetScope.$parent.cost.currencyUuid = $e.targetScope.$parent.cost.currency.currencyUuid;
                    $e.targetScope.$parent.cost.currencyCode = $e.targetScope.$parent.cost.currency.currencyCode;
                    $e.targetScope.$parent.cost.exchangeRate = $e.targetScope.$parent.cost.currency.exchangeRate;
                });
                scope.$on('salePrice.currency.change', function ($e) {
                    $e.targetScope.$parent.salePrice.currencyUuid = $e.targetScope.$parent.salePrice.currency.currencyUuid;
                    $e.targetScope.$parent.salePrice.currencyCode = $e.targetScope.$parent.salePrice.currency.currencyCode;
                    $e.targetScope.$parent.salePrice.exchangeRate = $e.targetScope.$parent.salePrice.currency.exchangeRate;
                });

                scope.$watch('editModel', function () {
                    if (scope.editModel == 'edit') {
                        initInvoiceOriginalGroup();
                    }
                });
                //scope.$on('currentOrder.editModel.reset', function ($e,editModel) {
                //    if(editModel=='edit'){
                //        initInvoiceOriginalGroup();
                //    }
                //});
                if (defer1) {
                    defer1.promise.then(function () {
                        initInvoiceOriginalGroup();
                    });
                }

            }
        };
    }]);