orderList.directive('additionalCostList', ['urlConfig', 'commonValue', 'qcObjectInArray', 'qcApi', 'groupAmount', '$q',
    function (urlConfig, commonValue, qcObjectInArray, qcApi, groupAmount, $q) {
        return {
            restrict   : 'A',
            scope      : {
                orderUuid           : '=',
                settlementLockStatus: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/additionalCostList.html',
            link       : function (scope, ele, attrs) {
                scope.lockStatus = scope.settlementLockStatus;//结算单锁定状态
                scope.fundsType = '3';//追加成本

                var defer1 = $q.defer();
                qcApi.post('order/getExistsCurrency', {orderId: scope.orderUuid, type: '2'}).success(function (result) {
                    scope.currencies = result.data;
                    defer1.resolve();
                });
                defer1.promise.then(function () {
                    getOldList();
                });
                scope.toggleSubTable = function () {

                    scope.$emit('subTable.toggle.request', 'additionalCostList' + scope.orderUuid);
                };
                scope.cancel = function (funds) {
                    qcApi.post('order/cancelAirticketOrderMoneyAmount', funds)
                        .success(function (data) {
                            getOldList();
                        });
                }
                function getOldList() {
                    qcApi.post('order/getAirticketOrderMoneyAmountList', {
                        orderUuid: scope.orderUuid,
                        fundsType: scope.fundsType
                    }).success(function (result) {
                        scope.oldList = result.data;
                        angular.forEach(scope.oldList, function (funds) {
                            funds.currencyCode = qcObjectInArray(scope.currencies, 'currencyUuid', funds.currencyUuid).currencyCode;
                        });
                        var totalList = [];
                        angular.forEach(scope.oldList, function (funds) {
                            if (funds.stateCode != '0') {
                                totalList.push(funds);
                            }
                        });
                        //scope.oldTotalAmount = groupAmount(totalList);
                        var totalAmountArr = groupAmount(totalList);
                        //scope.oldTotalAmount =

                        var amountObj = {};
                        if (totalAmountArr.length > 0) {
                            for (var p = 0; p < totalAmountArr.length; p++) {
                                //payTotalAmount = payTotalAmount + (payTotalAmount ? "+" : "") + paymentArr[p].currencyCode + paymentArr[p].amount.milliFormat();
                                if (!amountObj[totalAmountArr[p].currencyCode]) {
                                    amountObj[totalAmountArr[p].currencyCode] = totalAmountArr[p].amount;
                                }
                                else {
                                    amountObj[totalAmountArr[p].currencyCode] = (+amountObj[totalAmountArr[p].currencyCode]) + (+totalAmountArr[p].amount);
                                }
                            }
                        }
                        var totalAmountStr = '';
                        for (var item in amountObj) {
                            totalAmountStr += (item + amountObj[item].toString().milliFormat()) + '+';
                        }
                        totalAmountStr = totalAmountStr.substr(0, totalAmountStr.length - 1);
                        scope.oldTotalAmount = totalAmountStr;
                    });
                }

            }
        };
    }]);

orderList.directive('additionalCost', ['$rootScope', 'urlConfig', 'commonValue', 'qcObjectInArray', 'qcApi', 'groupAmount', 'qcDialog', '$q',
    function ($rootScope, urlConfig, commonValue, qcObjectInArray, qcApi, groupAmount, qcDialog, $q) {
        return {
            restrict   : 'A',
            scope      : {
                orderAdditionalCost: '=',
                orderUuid          : '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/additionalCost.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                //默认货币设置
                scope.defaultCurrency = commonValue.defaultCurrency;
                scope.fundsType = '3';//追加成本
                var defer1 = $q.defer();
                var defer2 = $q.defer();
                qcApi.post('order/getExistsCurrency', {orderId: scope.orderUuid, type: '2'}).success(function (result) {
                    scope.currencies = result.data;
                    scope.defaultCurrency = scope.currencies[0];
                    defer1.resolve();
                    defer2.resolve();
                });
                scope.userInfo = $rootScope.userInfo;

                scope.toggleSubTable = function () {
                    scope.orderAdditionalCost.newList = [{}];
                    scope.$emit('subTable.toggle.request', 'additionalCost' + scope.orderUuid);
                };
                scope.commit = function (funds, fundsScope) {
                    if (!valid([funds])) {
                        qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                        fundsScope.$broadcast('qcValid.check');
                        return;
                    }
                    funds.orderUuid = scope.orderUuid;

                    qcApi.post('order/saveAirticketOrderMoneyAmount', [funds])
                        .success(function (data) {
                            getOldList();
                            scope.orderAdditionalCost.newList.remove(funds);
                            if (scope.orderAdditionalCost.newList.length == 0) {
                                scope.orderAdditionalCost.newList = [{}];
                            }
                        });
                };
                scope.clear = function (funds) {
                    scope.initFunds(funds);
                    funds.memo = '';
                    funds.fundsName = '';
                    funds.amount = '';
                };
                scope.clearAll = function () {
                    scope.orderAdditionalCost.newList = [{}];
                    scope.initFunds(scope.orderAdditionalCost.newList[0]);
                }
                scope.commitAll = function () {
                    if (!valid(scope.orderAdditionalCost.newList)) {
                        qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                        scope.$broadcast('qcValid.check');
                        return;
                    }
                    angular.forEach(scope.orderAdditionalCost.newList, function (funds) {
                        funds.orderUuid = scope.orderUuid;
                    });
                    qcApi.post('order/saveAirticketOrderMoneyAmount', scope.orderAdditionalCost.newList)
                        .success(function (data) {
                            getOldList();
                            scope.orderAdditionalCost.newList = [{}];
                        });
                };
                scope.cancel = function (funds) {
                    qcApi.post('order/cancelAirticketOrderMoneyAmount', funds)
                        .success(function (data) {
                            getOldList();
                        });
                }
                //scope.initFunds = function (funds) {
                //    funds.currency = scope.defaultCurrency;
                //    funds.currencyUuid = scope.defaultCurrency.currencyUuid;
                //    funds.exchangeRate=scope.defaultCurrency.exchangeRate;
                //    funds.fundsType='3';
                //
                //}
                defer2.promise.then(function (funds) {
                    scope.initFunds = function (funds) {
                        funds.currency = scope.defaultCurrency;
                        funds.currencyUuid = scope.defaultCurrency.currencyUuid;
                        funds.exchangeRate = scope.defaultCurrency.exchangeRate;
                        funds.fundsType = '3';

                    }
                });
                function valid(list) {
                    var isValid = true;
                    angular.forEach(list, function (funds) {
                        if (!funds.fundsName) {
                            isValid = false;
                        }
                        if (!funds.currencyUuid) {
                            isValid = false;
                        }
                        if (!funds.exchangeRate) {
                            isValid = false;
                        }
                        if (!funds.amount) {
                            isValid = false;
                        }
                    })
                    return isValid;
                }

                scope.$on('funds.currency.change', function ($e) {
                    $e.targetScope.$parent.funds.currencyUuid = $e.targetScope.$parent.funds.currency.currencyUuid;
                    $e.targetScope.$parent.funds.exchangeRate = $e.targetScope.$parent.funds.currency.exchangeRate;
                });
                defer1.promise.then(function () {
                    if (!scope.orderAdditionalCost) {
                        scope.orderAdditionalCost = {
                            newList: [
                                {}
                            ]
                        };
                    }
                    getOldList();
                });

                function getOldList() {
                    qcApi.post('order/getAirticketOrderMoneyAmountList', {
                        orderUuid: scope.orderUuid,
                        fundsType: scope.fundsType
                    }).success(function (result) {
                        scope.oldList = result.data;
                        angular.forEach(scope.oldList, function (funds) {
                            funds.currencyCode = qcObjectInArray(scope.currencies, 'currencyUuid', funds.currencyUuid).currencyCode;
                        });
                        var totalList = [];
                        angular.forEach(scope.oldList, function (funds) {
                            if (funds.stateCode != '0') {
                                totalList.push(funds);
                            }
                        });
                        //scope.oldTotalAmount = groupAmount(totalList);
                        var totalAmountArr = groupAmount(totalList);
                        //scope.oldTotalAmount =

                        var amountObj = {};
                        if (totalAmountArr.length > 0) {
                            for (var p = 0; p < totalAmountArr.length; p++) {
                                //payTotalAmount = payTotalAmount + (payTotalAmount ? "+" : "") + paymentArr[p].currencyCode + paymentArr[p].amount.milliFormat();
                                if (!amountObj[totalAmountArr[p].currencyCode]) {
                                    amountObj[totalAmountArr[p].currencyCode] = totalAmountArr[p].amount;
                                }
                                else {
                                    amountObj[totalAmountArr[p].currencyCode] = (+amountObj[totalAmountArr[p].currencyCode]) + (+totalAmountArr[p].amount);
                                }
                            }
                        }
                        var totalAmountStr = '';
                        for (var item in amountObj) {
                            totalAmountStr += (item + amountObj[item].toString().milliFormat()) + '+';
                        }
                        totalAmountStr = totalAmountStr.substr(0, totalAmountStr.length - 1);
                        scope.oldTotalAmount = totalAmountStr;
                    });
                }

            }
        };
    }]);
