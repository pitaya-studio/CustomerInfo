orderList.directive('otherRevenueInputBtn', ['urlConfig', 'qcApi', 'qcDialog', 'commonValue', 'qcObjectInArray', 'fixedValue', '$rootScope', 'qcMessage', '$q', '$timeout',
    function (urlConfig, qcApi, qcDialog, commonValue, qcObjectInArray, fixedValue, $rootScope, qcMessage, $q, $timeout) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid           : '=',
                operatorType        : '=',
                otherRevenue        : '=',
                settlementLockStatus: '='
            },
            link    : function (scope, ele, attrs) {
                ele.on('click', function () {
                    qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/otherRevenue.html',
                        controller : ['$scope', function (dialogScope) {
                            dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                            //订单uuid
                            dialogScope.orderUuid = scope.orderUuid;
                            dialogScope.settlementLockStatus = scope.settlementLockStatus;
                            //收款类别
                            dialogScope.receiveTypes = fixedValue.receiveType;
                            //支付方式
                            dialogScope.paymentMethod = {};
                            dialogScope.paymentMethod.payMethods = fixedValue.paymentMethods;
                            dialogScope.paymentMethod.paymentMethodCode = '1';
                            //即时结算默认设置
                            dialogScope.speedyClearance = '1';
                            //dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;

                            var defer1 = $q.defer();
                            qcApi.post('order/getExistsCurrency', {
                                orderId: dialogScope.orderUuid,
                                type   : '1'
                            }).success(function (result) {
                                dialogScope.currencies = result.data;
                                dialogScope.selectedCurrency = dialogScope.currencies[0];
                                defer1.resolve();
                            });

                            // 这里截取有效的150个字符  这里其实可以用maxlength来做，但是呢 那个就不支持提示了
                            var limitation=150;
                            dialogScope.$watch('memo',function(newVal,oldVal){
                                if (newVal && newVal != oldVal) {
                                    if (newVal.length >= limitation) {
                                         qcMessage.tip('请保持输入字符数在150之内');
                                        dialogScope.memo = newVal.substr(0, limitation);
                                    }
                                }
                            });


                            //获取此订单的大编号组
                            var defer2 = $q.defer();
                            qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: scope.orderUuid}).success(function (result) {
                                dialogScope.bigCodes = result.data;
                                //dialogScope.selectedChannelType = dialogScope.bigCodes.channelType;
                                //dialogScope.selectedChannel = dialogScope.bigCodes.channel;
                                dialogScope.tourOperatorChannelCategoryCode = '2';
                                dialogScope.channelUuid = dialogScope.bigCodes.channel.channelUuid;
                                dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;
                                dialogScope.tourOperatorType = {};
                                dialogScope.tourOperator = {};
                                dialogScope.channelTypes = fixedValue.channelType;
                                dialogScope.channelType = dialogScope.bigCodes.channelType;
                                dialogScope.channel = dialogScope.bigCodes.channel;
                                //获取付款行信息
                                defer2.resolve();
                                qcApi.post('common/getBankInfo', {
                                    type: dialogScope.tourOperatorChannelCategoryCode,
                                    uuid: dialogScope.channelUuid
                                })
                                    .success(function (result) {
                                        dialogScope.channelPaymentBanks = result.data;
                                        dialogScope.paymentBanks = dialogScope.channelPaymentBanks;
                                    });
                            });

                            if (scope.operatorType != '0') {
                                $q.all([defer1.promise, defer2.promise]).then(function () {
                                    qcApi.post('order/getOtherReceiptDetail', {
                                        otherRevenueUuid: scope.otherRevenue.otherRevenueUuid
                                    }).success(function (result) {
                                        dialogScope.OtherRevenueDetail = result.data;
                                        dialogScope.OtherRevenueDetail = result.data;
                                        dialogScope.speedyClearance = dialogScope.OtherRevenueDetail.speedyClearance;

                                        dialogScope.fundsName = dialogScope.OtherRevenueDetail.fundsName;
                                        dialogScope.amount = dialogScope.OtherRevenueDetail.amount;

                                        dialogScope.selectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', dialogScope.OtherRevenueDetail.currencyUuid);
                                        dialogScope.convertedSelectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', dialogScope.OtherRevenueDetail.convertedCurrencyUuid);
                                        dialogScope.paymentMethod.paymentMethodCode = dialogScope.OtherRevenueDetail.paymentMethodCode;
                                        dialogScope.memo = dialogScope.OtherRevenueDetail.memo;
                                        dialogScope.payer = dialogScope.OtherRevenueDetail.payer;
                                        dialogScope.checkNo = dialogScope.OtherRevenueDetail.checkNo;
                                        dialogScope.checkIssueDate = dialogScope.OtherRevenueDetail.checkIssueDate;

                                        dialogScope.attachments = dialogScope.OtherRevenueDetail.attachments;
                                        dialogScope.tourOperatorChannelCategoryCode = dialogScope.OtherRevenueDetail.tourOperatorChannelCategoryCode;
                                        $timeout(function () {
                                            if (dialogScope.tourOperatorChannelCategoryCode == 1) {
                                                dialogScope.tourOperatorType.tourOperatorTypeName = qcObjectInArray(dialogScope.tourOperatorTypes, 'tourOperatorTypeCode', dialogScope.OtherRevenueDetail.tourOperatorOrChannelTypeCode).tourOperatorTypeName;
                                                dialogScope.tourOperatorType.tourOperatorTypeCode = qcObjectInArray(dialogScope.tourOperatorTypes, 'tourOperatorTypeCode', dialogScope.OtherRevenueDetail.tourOperatorOrChannelTypeCode).tourOperatorTypeCode;

                                                dialogScope.tourOperator.tourOperatorUuid = dialogScope.OtherRevenueDetail.tourOperatorOrChannelUuid;
                                                dialogScope.tourOperator.tourOperatorName = dialogScope.OtherRevenueDetail.tourOperatorOrChannelName;
                                                qcApi.post('common/getBankInfo', {
                                                    type: dialogScope.tourOperatorChannelCategoryCode,
                                                    uuid: dialogScope.tourOperator.tourOperatorUuid
                                                }).success(function (result) {
                                                    dialogScope.tourOperatorPaymentBanks = result.data;
                                                    dialogScope.paymentBanks = dialogScope.tourOperatorPaymentBanks;
                                                });
                                            }
                                            else {
                                                dialogScope.channelType = qcObjectInArray(dialogScope.channelTypes, 'channelTypeCode', dialogScope.OtherRevenueDetail.tourOperatorOrChannelTypeCode);
                                                dialogScope.channel.channelUuid = dialogScope.OtherRevenueDetail.tourOperatorOrChannelUuid;
                                                dialogScope.channel.channelName = dialogScope.OtherRevenueDetail.tourOperatorOrChannelName;
                                            }
                                            if (dialogScope.isOtherPay()) {
                                                dialogScope.selectedPayBankName = dialogScope.OtherRevenueDetail.paymentBank;
                                                dialogScope.selectedPayAccountNo = dialogScope.OtherRevenueDetail.paymentAccount;
                                            } else {
                                                dialogScope.selectedPayBank = {bankName: dialogScope.OtherRevenueDetail.paymentBank};
                                                dialogScope.selectedPayAccount = {accountNo: dialogScope.OtherRevenueDetail.paymentAccount};
                                            }
                                            dialogScope.selectedBank = {bankName: dialogScope.OtherRevenueDetail.receiveBank};
                                            dialogScope.selectedAccount = {accountNo: dialogScope.OtherRevenueDetail.receiveAccount};
                                        });

                                    });
                                })
                            }
                            //默认货币设置
                            dialogScope.defaultCurrency = commonValue.defaultCurrency;

                            //货币换算
                            dialogScope.convertedExchangeRate = function () {
                                return dialogScope.selectedCurrency ? dialogScope.selectedCurrency.exchangeRate : undefined;
                            };
                            dialogScope.convertedAmount = function () {
                                return dialogScope.amount != undefined ? dialogScope.amount * dialogScope.convertedExchangeRate() : "";
                            }

                            //地接社
                            dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;//地接社类型
                            dialogScope.$on('tourOperatorType.change', function () {
                                dialogScope.tourOperator = null;//地接社选择
                            });

                            dialogScope.$on('selectedPayBank.change', function () {
                                dialogScope.selectedPayAccount = null;
                            });

                            dialogScope.$on('selectedTourOperator.change', function () {
                                qcApi.post('common/getBankInfo', {
                                    type: dialogScope.tourOperatorChannelCategoryCode,
                                    uuid: dialogScope.tourOperator.tourOperatorUuid
                                })
                                    .success(function (result) {
                                        dialogScope.tourOperatorPaymentBanks = result.data;
                                        dialogScope.paymentBanks = dialogScope.tourOperatorPaymentBanks;
                                    });
                                dialogScope.selectedPayBank = null;
                                dialogScope.selectedPayAccount = null;
                            });

                            //获取登录用户信息 包括收款行信息
                            dialogScope.userInfo = $rootScope.userInfo;
                            dialogScope.$on('selectedBank.change', function () {
                                dialogScope.selectedAccount = null;
                            });
                            dialogScope.$watch('tourOperatorChannelCategoryCode', function () {
                                dialogScope.selectedPayBank = null;
                                dialogScope.selectedPayAccount = null;
                                if (dialogScope.tourOperatorChannelCategoryCode == '2') {
                                    dialogScope.paymentBanks = dialogScope.channelPaymentBanks;
                                } else {
                                    dialogScope.paymentBanks = dialogScope.tourOperatorPaymentBanks;
                                }
                            });
                            function getPayBankName() {
                                if (dialogScope.isOtherPay()) {
                                    return dialogScope.selectedPayBankName;
                                } else {
                                    return dialogScope.selectedPayBank != undefined ? dialogScope.selectedPayBank.bankName : '';
                                }
                            }

                            function getPaymentAccountNo() {
                                if (dialogScope.isOtherPay()) {
                                    return dialogScope.selectedPayAccountNo;
                                } else {
                                    return dialogScope.selectedPayAccount != undefined ? dialogScope.selectedPayAccount.accountNo : '';
                                }
                            }

                            dialogScope.isOtherPay = function () {
                                if (dialogScope.tourOperatorChannelCategoryCode == '2' && dialogScope.channelType.channelTypeCode == '2') {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                            dialogScope.save = function (type) {
                                if (!valid()) {
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    dialogScope.$broadcast('qcValid.check');
                                    return;
                                }
                                dialogScope.newOtherRevenue = {
                                    saveType                       : type,//'0':保存,'1':提交
                                    orderUuid                      : scope.orderUuid,
                                    otherRevenueUuid               : scope.otherRevenue ? scope.otherRevenue.otherRevenueUuid : undefined,
                                    speedyClearance                : dialogScope.speedyClearance,
                                    fundsName                      : dialogScope.fundsName,
                                    currencyUuid                   : dialogScope.selectedCurrency.currencyUuid,
                                    amount                         : dialogScope.amount,
                                    convertedCurrencyUuid          : dialogScope.defaultCurrency.currencyUuid,
                                    exchangeRate                   : dialogScope.convertedExchangeRate(),
                                    convertedAmount                : dialogScope.convertedAmount(),
                                    paymentMethodCode              : dialogScope.paymentMethod.paymentMethodCode,
                                    tourOperatorChannelCategoryCode: dialogScope.tourOperatorChannelCategoryCode,
                                    tourOperatorOrChannelTypeCode  : dialogScope.tourOperatorChannelCategoryCode == '1' ? dialogScope.tourOperatorType.tourOperatorTypeCode : dialogScope.channelType.channelTypeCode,
                                    tourOperatorOrChannelName      : dialogScope.tourOperatorChannelCategoryCode == '1' ? dialogScope.tourOperator.tourOperatorName : dialogScope.channel.channelName,
                                    tourOperatorOrChannelUuid      : dialogScope.tourOperatorChannelCategoryCode == '1' ? dialogScope.tourOperator.tourOperatorUuid : dialogScope.channel.channelUuid,
                                    payer                          : dialogScope.payer,
                                    checkNo                        : dialogScope.checkNo,
                                    checkIssueDate                 : dialogScope.checkIssueDate,
                                    paymentBank                    : getPayBankName(),
                                    paymentAccount                 : getPaymentAccountNo(),
                                    receiveBank                    : dialogScope.selectedBank != undefined ? dialogScope.selectedBank.bankName : '',
                                    receiveAccount                 : dialogScope.selectedAccount != undefined ? dialogScope.selectedAccount.accountNo : '',
                                    attachments                    : dialogScope.attachments,
                                    memo                           : dialogScope.memo
                                };
                                qcApi.post('mtourfinance/saveOrUpdateOtherCostRecord',
                                    dialogScope.newOtherRevenue
                                ).success(function (result) {
                                        $rootScope.$broadcast('order.otherRevenue.save', scope.orderUuid);
                                        qcMessage.tip('其他收入录入成功');
                                        dialogScope.closeThisDialog();
                                    });
                            };
                            function valid() {
                                var isValid = true;
                                if (!dialogScope.fundsName) {
                                    isValid = false;
                                }
                                if (!dialogScope.amount) {
                                    isValid = false;
                                }
                                if (dialogScope.companyRoleCode != '0') {
                                    if (!dialogScope.payer) {
                                        isValid = false;
                                    }
                                }
                                if (dialogScope.tourOperatorChannelCategoryCode == '1') {
                                    {
                                        if (!dialogScope.tourOperatorType) {
                                            isValid = false;
                                        }
                                        if (!dialogScope.tourOperator || !dialogScope.tourOperator.tourOperatorUuid) {
                                            isValid = false;
                                        }
                                    }

                                }
                                //0038需求
//                                if (!dialogScope.payer) {
//                                    isValid = false;
//                                }
                                if (dialogScope.paymentMethod.paymentMethodCode == 1) {
                                    if (!dialogScope.checkNo) {
                                        isValid = false;
                                    }
                                    if (!dialogScope.checkIssueDate) {
                                        isValid = false;
                                    }
                                }
                                return isValid;
                            }
                        }
                        ],
                        width      : 500,
                        plain      : false
                    });
                });
            }
        }
    }])
;