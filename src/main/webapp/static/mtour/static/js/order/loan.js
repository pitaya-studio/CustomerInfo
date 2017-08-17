orderList.directive('loanList', ['urlConfig', 'commonValue', 'qcObjectInArray', 'qcApi', 'groupAmount', '$q',
    function (urlConfig, commonValue, qcObjectInArray, qcApi, groupAmount, $q) {
        return {
            restrict   : 'A',
            scope      : {
                orderUuid           : '=',
                settlementLockStatus: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/loan-list.html',
            link       : function (scope, ele, attrs) {
                scope.lockStatus = scope.settlementLockStatus;//结算单锁定状态
                scope.fundsType = '1';//借款
                var defer1 = $q.defer();
                qcApi.post('order/getExistsCurrency', {orderId: scope.orderUuid, type: '2'}).success(function (result) {
                    scope.currencies = result.data;
                    defer1.resolve();
                });
                scope.toggleSubTable = function () {
                    scope.$emit('subTable.toggle.request', 'loanList' + scope.orderUuid);
                };
                scope.cancel = function (funds) {
                    qcApi.post('order/cancelAirticketOrderMoneyAmount', funds)
                        .success(function (data) {
                            getOldList();
                        });
                }

                defer1.promise.then(function () {
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
                            if (funds.stateCode != 0) {
                                totalList.push(funds);
                            }
                        });
                        scope.oldTotalAmount = groupAmount(totalList);
                    });
                }

            }
        };
    }]);

orderList.directive('loan', ['$rootScope', 'urlConfig', 'commonValue', 'qcObjectInArray', 'qcApi', 'groupAmount', 'qcDialog', '$q',
    function ($rootScope, urlConfig, commonValue, qcObjectInArray, qcApi, groupAmount, qcDialog, $q) {
        return {
            restrict   : 'A',
            scope      : {
                orderLoan: '=',
                orderUuid: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/loan.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                scope.fundsType = '1';//借款
                var defer1 = $q.defer();
                var defer2 = $q.defer();
                qcApi.post('order/getExistsCurrency?date=' + new Date(), {
                    orderId: scope.orderUuid,
                    type   : '2'
                }).success(function (result) {
                    scope.currencies = result.data;
                    scope.defaultCurrency = scope.currencies[0];
                    defer1.resolve();
                    defer2.resolve();
                });
                scope.userInfo = $rootScope.userInfo;

                scope.toggleSubTable = function () {
                    scope.orderLoan.newList = [{}];
                    scope.$emit('subTable.toggle.request', 'loan' + scope.orderUuid);
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
                            scope.orderLoan.newList.remove(funds);
                            if (scope.orderLoan.newList.length == 0) {
                                scope.orderLoan.newList = [{}];
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
                    scope.orderLoan.newList = [{}];
                    scope.initFunds(scope.orderLoan.newList[0]);
                }
                scope.commitAll = function () {
                    if (!valid(scope.orderLoan.newList)) {
                        qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                        scope.$broadcast('qcValid.check');
                        return;
                    }
                    angular.forEach(scope.orderLoan.newList, function (funds) {
                        funds.orderUuid = scope.orderUuid;
                    });
                    qcApi.post('order/saveAirticketOrderMoneyAmount', scope.orderLoan.newList)
                        .success(function (data) {
                            getOldList();
                            scope.orderLoan.newList = [{}];
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
                //    funds.exchangeRate = scope.defaultCurrency.exchangeRate;
                //    funds.fundsType = '1';
                //
                //}
                defer2.promise.then(function (funds) {
                    //initFunds(funds);
                    scope.initFunds = function (funds) {
                        funds.currency = scope.defaultCurrency;
                        funds.currencyUuid = scope.defaultCurrency.currencyUuid;
                        funds.exchangeRate = scope.defaultCurrency.exchangeRate;
                        funds.fundsType = '1';

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
                    if (!scope.orderLoan) {
                        scope.orderLoan = {
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
                            if (funds.stateCode != 0) {
                                totalList.push(funds);
                            }
                        });
                        scope.oldTotalAmount = groupAmount(totalList);
                    });
                }

            }
        };
    }]);