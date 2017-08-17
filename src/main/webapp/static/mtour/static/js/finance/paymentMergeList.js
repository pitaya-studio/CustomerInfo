financePayList.directive('paymentMergeList', ['$rootScope', 'urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi', 'qcMessage',
    function ($rootScope, urlConfig, fixedValue, commonValue, qcObjectInArray, qcDialog, qcApi, qcMessage) {
        return {
            restrict   : 'A',
            scope      : {
                orderUuid: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/paymentMergeList.html',
            link       : function (scope, ele, attrs) {
                paymentMergeList();
                scope.batchPayment = function (payment) {
                    qcApi.post('/mtourfinance/getBatchRefundInfo', {
                        orderUuid                      : payment.orderUuid,
                        paymentObjectUuid              : payment.paymentObjectUuid,
                        tourOperatorChannelCategoryCode: payment.tourOperatorChannelCategoryCode
                    }).
                        success(function (result) {
                            qcDialog.open({
                                templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/batchPayment.html',
                                controller : ['$scope', function (dialogScope) {
                                    dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                                    //默认货币设置
                                    dialogScope.defaultCurrency = commonValue.defaultCurrency;
                                    dialogScope.tourOperatorChannelCategoryCode = payment.tourOperatorChannelCategoryCode;
                                    //支付方式
                                    dialogScope.paymentMethod = {};
                                    dialogScope.paymentMethod.payMethods = fixedValue.paymentMethods;
                                    dialogScope.paymentMethod.paymentMethodCode = '1';
                                    dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                    dialogScope.payList = result.data;
                                    angular.forEach(dialogScope.payList.results, function (pay) {
                                        pay.fundsTypeName = qcObjectInArray(fixedValue.fundsType, 'fundsType', pay.fundsType).fundsTypeName;
                                        pay.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', pay.payableAmount.currencyUuid);
                                        pay.currencyUuid = pay.currency.currencyUuid;
                                        pay.tourOperatorChannelCategoryCode = dialogScope.tourOperatorChannelCategoryCode;
                                        //if (pay.paidAmount == undefined || pay.paidAmount.length == 0) {
                                        //    pay.exchangeRate = pay.currency.exchangeRate;
                                        //}

                                    });
                                    dialogScope.paymentAmount = 0;

                                    //收款行
                                    qcApi.post('common/getBankInfo', {
                                        type: payment.tourOperatorChannelCategoryCode,
                                        uuid: payment.paymentObjectUuid
                                    }).success(function (result) {
                                        dialogScope.receiveBanks = result.data;
                                        //if (dialogScope.companyRoleCode == '0') {
                                        //    angular.forEach(dialogScope.receiveBanks, function (receiveBank) {
                                        //        if (receiveBank.bankName == dialogScope.paymentInfo.receiveBank) {
                                        //            dialogScope.selectedReceiveBank = {};
                                        //            dialogScope.selectedReceiveBank.accounts = receiveBank.accounts;
                                        //        }
                                        //    });
                                        //    if (dialogScope.selectedReceiveBank) {
                                        //        dialogScope.selectedReceiveBank.bankName = dialogScope.paymentInfo.receiveBank;
                                        //    } else {
                                        //        dialogScope.selectedReceiveBank = {bankName: dialogScope.paymentInfo.receiveBank};
                                        //    }
                                        //    dialogScope.selectedReceiveAccount = {accountNo: dialogScope.paymentInfo.receiveAccount};
                                        //}
                                    });
                                    dialogScope.$on('selectedReceiveBank.change', function () {
                                        dialogScope.selectedReceiveAccount = null;
                                    });

                                    //全部--付全部款
                                    dialogScope.payAll = function (needToPayList) {
                                        angular.forEach(needToPayList, function (pay) {
                                            pay.paymentAmount = pay.payableAmount.amount.floatSubtract(pay.paidAmount ? pay.paidAmount.amount : 0);
                                        });
                                        dialogScope.countPayTotalAmount();
                                    };

                                    //确认支付
                                    dialogScope.payBatchConfirm = function () {
                                        if (!valid()) {
                                            qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                            dialogScope.$broadcast('qcValid.check');
                                            return;
                                        }
                                        ;
                                        var i = 0;
                                        angular.forEach(dialogScope.payList.results, function (pay) {
                                            if (pay.paymentAmount && pay.paymentAmount != 0 && pay.paymentAmount != '-') {
                                                i++;
                                            }
                                        });
                                        if (i == 0) {
                                            qcDialog.openMessage({msg: '至少有一笔款不能为空或0'});
                                            return;
                                        }
                                        dialogScope.newPayment = {
                                            orderUuid        : scope.orderUuid,
                                            paymentObjectUuid: dialogScope.payList.paymentObjectUuid,
                                            payments         : dialogScope.payList.results,
                                            paymentMethodCode: dialogScope.paymentMethod.paymentMethodCode,
                                            receiveCompany   : dialogScope.receiveCompany,
                                            checkNo          : dialogScope.checkNo,
                                            checkIssueDate   : dialogScope.checkIssueDate,
                                            paymentBank      : dialogScope.selectedBank != undefined ? dialogScope.selectedBank.bankName : '',
                                            paymentAccount   : dialogScope.selectedAccount != undefined ? dialogScope.selectedAccount.accountNo : '',//只有付款方式是汇款时才有效
                                            receiveBank      : dialogScope.selectedReceiveBank != undefined ? dialogScope.selectedReceiveBank.bankName : dialogScope.receiveBankName || '',//只有付款方式是汇款时才有效
                                            receiveAccount   : dialogScope.selectedReceiveAccount != undefined ? dialogScope.selectedReceiveAccount.accountNo : dialogScope.receiveAccountNo || '',//只有付款方式是汇款时才有效
                                            attachments      : dialogScope.attachments,
                                            memo             : dialogScope.memo
                                        };
                                        qcApi.post('mtourfinance/batchSaveRefundInfo', dialogScope.newPayment
                                        ).success(function (result) {
                                                $rootScope.$broadcast('finance.batchPayment.save', scope.orderUuid);
                                                dialogScope.closeThisDialog();
                                            });
                                    };

                                    function valid() {
                                        var isValid = true;
                                        if (dialogScope.paymentMethod.paymentMethodCode == 1) {
                                            if (!dialogScope.checkNo) {
                                                isValid = false;
                                            }
                                            if (!dialogScope.checkIssueDate) {
                                                isValid = false;
                                            }
                                        }
                                        if (dialogScope.paymentMethod.paymentMethodCode == 4) {
                                            if (!dialogScope.selectedBank) {
                                                isValid = false;
                                            }
                                            if (!dialogScope.selectedAccount) {
                                                isValid = false;
                                            }
                                        }
                                        if (!dialogScope.receiveCompany) {
                                            isValid = false;
                                        }
                                        return isValid;
                                    }

                                    dialogScope.countPayTotalAmount = function () {
                                        var paymentArr = [];
                                        angular.forEach(dialogScope.payList.results, function (pay) {
                                            if (!pay.paymentAmount || pay.paymentAmount == '-') {
                                                return;
                                            }
                                            var payment = {};
                                            payment.currencyCode = pay.currency.currencyCode;
                                            payment.currencyUuid = pay.currency.currencyUuid;
                                            payment.amount = (+pay.paymentAmount).toFixed(2);
                                            payment.exchangeRate = pay.exchangeRate;
                                            paymentArr.push(payment);
                                            //}
                                        });
                                        var payTotalAmount = '';
                                        var payTotalRMBAmount = 0;
                                        var currencyObj = {};
                                        if (paymentArr.length > 0) {
                                            for (var p = 0; p < paymentArr.length; p++) {
                                                //payTotalAmount = payTotalAmount + (payTotalAmount ? "+" : "") + paymentArr[p].currencyCode + paymentArr[p].amount.milliFormat();
                                                payTotalRMBAmount = payTotalRMBAmount.floatAdd((+paymentArr[p].amount).floatMul(+paymentArr[p].exchangeRate));
                                                if (!currencyObj[paymentArr[p].currencyCode]) {
                                                    currencyObj[paymentArr[p].currencyCode] = paymentArr[p].amount;
                                                }
                                                else {
                                                    currencyObj[paymentArr[p].currencyCode] = (+currencyObj[paymentArr[p].currencyCode]) + (+paymentArr[p].amount);
                                                }

                                            }
                                        }

                                        for (var i in currencyObj) {
                                            payTotalAmount += (i + currencyObj[i].toString().milliFormat()) + '+';
                                        }
                                        payTotalAmount = payTotalAmount.substr(0, payTotalAmount.length - 1);
                                        //付款总额
                                        dialogScope.payList.payTotalAmount = payTotalAmount;
                                        //转换后总额
                                        dialogScope.payList.payTotalRMBAmount = payTotalRMBAmount.toFixed(2).milliFormat();
                                    }
                                    ;
                                }
                                ],
                                width      : 900,
                                height     : 500,
                                plain      : false
                            });
                        });
                };

                //批量付款记录列表
                scope.batchPaymentRecordList = function (payment) {
                    //qcApi.post('mtourfinance/getPayRecord', {
                    //    orderUuid        : scope.orderUuid,
                    //    paymentObjectUuid: payment.paymentObjectUuid
                    //}).success(function (result) {
                    //    //angular.forEach(scope.newList, function (paymentRecord) {
                    //    //    paymentRecord.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', paymentRecord.currencyUuid);
                    //    //    paymentRecord.paymentMethod = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', paymentRecord.paymentMethod);
                    //    //    paymentRecord.paymentRecordStatus = qcObjectInArray(fixedValue.financePaymentRecordStatus, 'financePaymentRecordStatusCode', paymentRecord.financePaymentRecordStatusCode);
                    //    //});
                    //    qcDialog.open({
                    //        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/batchPaymentRecordList.html',
                    //        controller : ['$scope', function (dialogScope) {
                    //            dialogScope.recordList = result.data;
                    //        }],
                    //        width      : 700,
                    //        height     : 500,
                    //        plain      : false
                    //    });
                    //});

                    qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/batchPaymentRecordList.html',
                            controller : ['$scope', function (dialogScope) {
                                getBatchPayRecordList(scope.orderUuid, payment.paymentObjectUuid, payment.tourOperatorChannelCategoryCode);

                                //撤销
                                dialogScope.cancelBatchPay = function (record) {
                                    qcApi.post('mtourfinance/batchUndoRefundPayInfo', {
                                        orderUuid             : record.orderUuid,
                                        paymentObj_paymentUuid: record.paymentObj_paymentUuid,
                                        paymentObjectUuid     : record.paymentObjectUuid
                                    }).success(function (result) {
                                        $rootScope.$broadcast('finance.batchPayment.cancel', record.orderUuid, record.paymentObjectUuid, record.tourOperatorChannelCategoryCode);
                                    });
                                };

                                dialogScope.$on('finance.batchPayment.cancel', function ($e, orderUuid, paymentObjectUuid, tourOperatorChannelCategoryCode) {
                                    getBatchPayRecordList(orderUuid, paymentObjectUuid, tourOperatorChannelCategoryCode);
                                    $rootScope.$broadcast('finance.batchPayment.save', orderUuid);
                                });

                                function getBatchPayRecordList(orderUuid, paymentObjectUuid, tourOperatorChannelCategoryCode) {
                                    qcApi.post('mtourfinance/getRefundRecordsInfo', {
                                        orderUuid                      : orderUuid,
                                        paymentObjectUuid              : paymentObjectUuid,
                                        tourOperatorChannelCategoryCode: tourOperatorChannelCategoryCode
                                    }).success(function (result) {
                                        dialogScope.recordList = result.data;
                                        angular.forEach(scope.recordList, function (record) {
                                            record.tourOperatorChannelCategoryCode = tourOperatorChannelCategoryCode;
                                        });
                                    });
                                }
                            }
                            ],
                            width      : 850,
                            height     : 300,
                            plain      : false
                        }
                    )
                    ;
                };

                scope.currencies = commonValue.currencies;
                scope.userInfo = $rootScope.userInfo;
                function paymentMergeList() {
                    if (!scope.newmergePayList) {
                        qcApi.post('mtourfinance/getPayChildrenList', {
                            orderUuid: scope.orderUuid
                        }).success(function (result) {
                            scope.newmergePayList = result.data;

                            angular.forEach(scope.newmergePayList, function (paymentRecord) {
                                paymentRecord.payableAmountArr = [];
                                paymentRecord.paidAmountArr = [];
                                if (paymentRecord.payableAmount && (paymentRecord.payableAmount.indexOf('+') != -1)) {
                                    paymentRecord.payableAmountArr = paymentRecord.payableAmount.split('+');
                                }
                                else {
                                    paymentRecord.payableAmountArr.push(paymentRecord.payableAmount);
                                }
                                if (paymentRecord.paidAmount && (paymentRecord.paidAmount.indexOf('+') != -1)) {
                                    paymentRecord.paidAmountArr = paymentRecord.paidAmount.split('+');
                                }
                                else {
                                    paymentRecord.paidAmountArr.push(paymentRecord.paidAmount);
                                }
                            });
                        });
                    }
                }
            }
        };
    }])
;
