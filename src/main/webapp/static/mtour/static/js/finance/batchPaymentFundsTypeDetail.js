financePayList.directive('batchPaymentFundstypeDetail', ['$rootScope', 'urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi', 'qcMessage', '$window',
    function ($rootScope, urlConfig, fixedValue, commonValue, qcObjectInArray, qcDialog, qcApi, qcMessage, $window) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid                      : '=',
                fundsType                      : '=',
                paymentObjectUuid              : '=',
                touroperatorChannelCategoryCode: '='
            },
            link    : function (scope, ele, attrs) {
                ele.on('click', function () {
                    qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/batchPaymentFundsTypeDetail.html',
                        controller : ['$scope', function (dialogScope) {
                            qcApi.post('mtourfinance/getOrderPaymentInfo', {
                                orderUuid                      : scope.orderUuid,
                                paymentObjectUuid              : scope.paymentObjectUuid,
                                tourOperatorChannelCategoryCode: scope.touroperatorChannelCategoryCode
                            }).success(function (result) {
                                dialogScope.fundsTypeDetailList = result.data;
                                //angular.forEach(dialogScope.fundsTypeDetailList.results, function (detail) {
                                //    if (detail.fundsType == '3') {
                                //        angular.forEach(detail.totalAddAmount, function (amount) {
                                //            amount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
                                //        });
                                //    }
                                //    if (detail.fundsType == '2') {
                                //        angular.forEach(detail.totalRefundAmount, function (amount) {
                                //            amount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
                                //        });
                                //    }
                                //    detail.paymentStatusName = qcObjectInArray(fixedValue.financePaymentStatus, 'paymentStatusCode', detail.paymentStatusCode).paymentStatusName;
                                //});
                            });
                            //全选
                            dialogScope.chkAll = function ($event) {
                                var checkbox = $event.target;
                                if (checkbox.checked) {
                                    angular.forEach(dialogScope.fundsTypeDetailList.results, function (result) {
                                        result.batchChecked = true;
                                    });
                                }
                                else {
                                    angular.forEach(dialogScope.fundsTypeDetailList.results, function (result) {
                                        result.batchChecked = false;
                                    });
                                }
                            };


                            dialogScope.penditureBtnEnabled = function () {
                                var i = 0;
                                if (dialogScope.fundsTypeDetailList) {
                                    angular.forEach(dialogScope.fundsTypeDetailList.results, function (pay) {
                                        if (pay.batchChecked) {
                                            i++;
                                        }
                                    });
                                    if (i == 0) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }
                                }
                                else {
                                    return false;
                                }
                            };
                            dialogScope.generateOutPayPage = function () {
                                var checkedPay = [];
                                var tmpObj = {};
                                var fundsTypePayList = [];
                                var m = 0;
                                angular.forEach(dialogScope.fundsTypeDetailList.results, function (pay) {
                                        if (pay.batchChecked) {
                                            checkedPay.push(pay.currencyId);
                                            fundsTypePayList.push({
                                                    paymentUuid: pay.paymentUuid, fundsType: pay.fundsType
                                                }
                                            );
                                        }
                                    }
                                );
                                for (var i = 0; i < checkedPay.length; i++) {
                                    if (!tmpObj.hasOwnProperty(checkedPay[i])) {
                                        tmpObj[checkedPay[i]] = 1;
                                    }
                                    else {
                                        tmpObj[checkedPay[i]] = parseInt(tmpObj[checkedPay[i]]) + 1;
                                    }
                                }
                                for (var amount in tmpObj) {
                                    m++;
                                }
                                if (m > 1) {
                                    qcDialog.openMessage({msg: '所选款项币种不一致无法生成支出单,请重新选择'});
                                    return;
                                }

                                dialogScope.mergeParam = {
                                    orderUuid        : scope.orderUuid,
                                    paymentObjectUuid: scope.paymentObjectUuid,
                                    fundsTypePayList : fundsTypePayList
                                };
                                var mergeParam = JSON.stringify(dialogScope.mergeParam);
                                $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/mergePrintOutPayPage?mergeParam=' + mergeParam + '');
                            };
                        }],
                        width      : 1100,
                        heigth     : 300,
                        plain      : false
                    });
                });
                scope.currencies = commonValue.currencies;
                scope.userInfo = $rootScope.userInfo;
            }
        };
    }]);
