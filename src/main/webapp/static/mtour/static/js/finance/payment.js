financePayList.directive('financePaymentInput', ['urlConfig', 'qcApi', 'qcDialog', 'commonValue', 'qcObjectInArray', 'fixedValue', '$rootScope', 'qcMessage',
    function (urlConfig, qcApi, qcDialog, commonValue, qcObjectInArray, fixedValue, $rootScope, qcMessage) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid      : '=',
                paymentUuid    : '=',
                fundsType      : '=',
                channelTypeCode: '=',
                paidAmount     : '='
            },
            link    : function (scope, ele, attrs) {
                ele.on('click', function () {
                        qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/payment.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;//判断登录用户
                                dialogScope.paidAmount = scope.paidAmount;
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
                                //支付方式
                                dialogScope.paymentMethod = {};
                                dialogScope.paymentMethod.payMethods = fixedValue.paymentMethods;
                                dialogScope.paymentMethod.paymentMethodCode = '1';
                                //是否为签约渠道
                                dialogScope.channelType = {};
                                dialogScope.channelType.channelTypeCode = scope.channelTypeCode;
                                //获取付款信息
                                qcApi.post('mtourfinance/getPayedMoneyInfoForPay', {
                                    orderUuid  : scope.orderUuid,
                                    paymentUuid: scope.paymentUuid,
                                    fundsType  : scope.fundsType
                                }).success(function (result) {
                                    dialogScope.paymentInfo = result.data;
                                    if (dialogScope.companyRoleCode == '0') {
                                        dialogScope.receiveCompany = dialogScope.paymentInfo.receiveCompany;
                                    }
                                    dialogScope.payableAmount = dialogScope.paymentInfo.payableAmount;//应付金额
                                    dialogScope.payingAmount = dialogScope.paymentInfo.payingAmount;//未付金额
                                    angular.forEach(dialogScope.payableAmount, function (amount) {
                                        amount.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                                    });
                                    angular.forEach(dialogScope.payingAmount, function (amount) {
                                        amount.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                                    });


                                    dialogScope.currentPayingAmount = dialogScope.payingAmount[0];
                                    dialogScope.paymentAmount = dialogScope.currentPayingAmount.amount;
                                    dialogScope.$on('currentPayingAmount.change', function () {
                                        dialogScope.paymentAmount = dialogScope.currentPayingAmount.amount;
                                    });

                                    //if ((dialogScope.paidAmount == undefined) || (dialogScope.paidAmount.length == 0)) {
                                    //    dialogScope.currentPayingAmount.exchangeRate = dialogScope.currentPayingAmount.currency.exchangeRate;
                                    //}

                                    //货币换算
                                    dialogScope.convertedExchangeRate = function () {
                                        return dialogScope.currentPayingAmount.exchangeRate;
                                    };

                                    dialogScope.convertedAmount = function () {
                                        return dialogScope.paymentAmount != undefined ? dialogScope.paymentAmount * dialogScope.currentPayingAmount.exchangeRate : "";
                                    }
                                    if (dialogScope.channelType.channelTypeCode) {
                                        dialogScope.tourOperatorChannelCategoryCode = '2';
                                    } else {
                                        dialogScope.tourOperatorChannelCategoryCode = '1';
                                    }
                                    qcApi.post('common/getBankInfo', {
                                        type: dialogScope.tourOperatorChannelCategoryCode,
                                        uuid: dialogScope.paymentInfo.receiveCompanyUuid
                                    }).success(function (result) {
                                        dialogScope.receiveBanks = result.data;
                                        if (dialogScope.companyRoleCode == '0') {
                                            angular.forEach(dialogScope.receiveBanks, function (receiveBank) {
                                                if (receiveBank.bankName == dialogScope.paymentInfo.receiveBank) {
                                                    dialogScope.selectedReceiveBank = {};
                                                    dialogScope.selectedReceiveBank.accounts = receiveBank.accounts;
                                                }
                                            });
                                            if (dialogScope.selectedReceiveBank) {
                                                dialogScope.selectedReceiveBank.bankName = dialogScope.paymentInfo.receiveBank;
                                            } else {
                                                dialogScope.selectedReceiveBank = {bankName: dialogScope.paymentInfo.receiveBank};
                                            }
                                            dialogScope.selectedReceiveAccount = {accountNo: dialogScope.paymentInfo.receiveAccount};
                                        }
                                    })
                                });
                                //付款行信息--美途--由登录用户带出
                                dialogScope.userInfo = $rootScope.userInfo;
                                //默认货币设置
                                dialogScope.defaultCurrency = commonValue.defaultCurrency;


                                dialogScope.$on('selectedPayBank.change', function () {
                                    dialogScope.selectedPayAccount = null;
                                });
                                dialogScope.currencies = commonValue.currencies;
                                dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;

                                //此段逻辑有问题,使用下面的代码替换
//                                qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: scope.orderUuid}).success(function (result) {
//                                    dialogScope.bigCodes = result.data;
//                                    if(dialogScope.channelType.channelTypeCode){
//                                    	dialogScope.tourOperatorChannelCategoryCode = '2';
//                                    	dialogScope.uuid = dialogScope.bigCodes.channel.channelUuid;
//                                    }else{
//                                    	dialogScope.tourOperatorChannelCategoryCode = '1';
//                                    	dialogScope.uuid = dialogScope.bigCodes.invoiceOriginals[0].tourOperatorUuid;
//                                    }
//                                    //获取收款行信息
//                                    qcApi.post('common/getBankInfo', {
//                                        type: dialogScope.tourOperatorChannelCategoryCode,
//                                        uuid: dialogScope.uuid
//                                    })
//                                        .success(function (result) {
//                                            dialogScope.receiveBanks = result.data;
//                                            if(dialogScope.companyRoleCode=='0'){
//                                            	dialogScope.selectedReceiveBank = {bankName: dialogScope.paymentInfo.receiveBank};
//                                                dialogScope.selectedReceiveAccount = {accountNo: dialogScope.paymentInfo.receiveAccount};
//                                            }
//                                        });
//                                });

                                dialogScope.$on('selectedReceiveBank.change', function () {
                                    dialogScope.selectedReceiveAccount = null;
                                });
                                dialogScope.payConfirm = function () {
                                    if (!valid()) {
                                        qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                        dialogScope.$broadcast('qcValid.check');
                                        return;
                                    }
                                    dialogScope.newPayment = {
                                        paymentMethodCode: dialogScope.paymentMethod.paymentMethodCode,
                                        paymentAmount    : dialogScope.paymentAmount,
                                        orderUuid        : scope.orderUuid,
                                        paymentUuid      : scope.paymentUuid,
                                        fundsType        : scope.fundsType,
                                        currencyUuid     : dialogScope.currentPayingAmount.currency.currencyUuid,
                                        exchangeRate     : dialogScope.currentPayingAmount.exchangeRate,
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
                                    qcApi.post('mtourfinance/saveRefundInfo', dialogScope.newPayment
                                    ).success(function (result) {
                                            $rootScope.$broadcast('finance.payment.save', scope.paymentUuid, scope.fundsType);
                                            dialogScope.closeThisDialog();
                                        });
                                }
                                function valid() {
                                    var isValid = true;
                                    if (angular.isEmpty(dialogScope.paymentAmount)) {
                                        isValid = false;
                                    }
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
                            }
                            ],
                            className: 'qc-dialog-theme-page',
                            width      : 500,
                            plain      : false
                        });

                    }
                );
            }
        };
    }])