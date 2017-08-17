orderList.directive('orderReceiveInputBtn', ['orderReceivePop',
    function (orderReceivePop) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid: '='
            },
            link    : function (scope, ele, attrs) {
                ele.on('click', function () {
                    orderReceivePop(scope.orderUuid)
                });
            }
        };
    }])


orderList.factory('orderReceivePop', ['urlConfig', 'qcApi', 'qcDialog', 'commonValue', 'qcObjectInArray', 'fixedValue', '$rootScope', 'qcMessage', '$q',
    function (urlConfig, qcApi, qcDialog, commonValue, qcObjectInArray, fixedValue, $rootScope, qcMessage, $q) {
        return function (orderUuid) {
            qcDialog.open({
                templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReceive.html',
                controller : ['$scope', function (dialogScope) {
                    dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                    //默认货币设置
                    dialogScope.defaultCurrency = commonValue.defaultCurrency;
                    //订单收款货币和汇率
                    var defer = $q.defer();
                    qcApi.post('order/getExistsCurrency', {orderId: orderUuid, type: '2'}).success(function (result) {
                        dialogScope.currencies = result.data;
                        dialogScope.selectedCurrency = dialogScope.currencies[0];
                        defer.resolve();
                    });
                    defer.promise.then(function () {
                        //查询订单的未收金额和外保总额
                        qcApi.post('order/getOrderPriceDetail', {
                            orderUuid: orderUuid
                        }).success(function (result) {
                        	dialogScope.groupNo = result.data.groupNo;
                            dialogScope.unreceiveAmount = result.data.unreceiveAmount;
                            dialogScope.totalSalePrice = result.data.totalSalePrice;
                            angular.forEach(dialogScope.unreceiveAmount, function (amount) {
                                amount.currencyCode = qcObjectInArray(dialogScope.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
                            });
                            angular.forEach(dialogScope.totalSalePrice, function (amount) {
                                amount.currencyCode = qcObjectInArray(dialogScope.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
                            });
                            dialogScope.reservationCount = result.data.reservationCount;
                        });
                    });

                    //即时结算默认设置
                    dialogScope.speedyClearance = '1';


                    //货币换算
                    dialogScope.convertedExchangeRate = function () {
                        return dialogScope.selectedCurrency != undefined ? dialogScope.selectedCurrency.exchangeRate : '';
                    };
                    dialogScope.convertedAmount = function () {
                        return dialogScope.receiveAmount != undefined ? dialogScope.receiveAmount * dialogScope.convertedExchangeRate() : "";
                    }
                    //订单uuid
                    dialogScope.orderUuid = orderUuid;

                    //收款类别
                    dialogScope.receiveTypes = fixedValue.orderReceiveType;
                    //支付方式
                    dialogScope.paymentMethod = {};
                    dialogScope.paymentMethod.payMethods = fixedValue.paymentMethods;
                    dialogScope.paymentMethod.paymentMethodCode = '1';
                    //dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                    //获取此订单的大编号组
                    qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: orderUuid}).success(function (result) {
                        dialogScope.bigCodes = result.data;
                        dialogScope.tourOperatorChannelCategoryCode = '2';
                        dialogScope.channelType = dialogScope.bigCodes.channelType;
                        dialogScope.channel = dialogScope.bigCodes.channel;
                        dialogScope.channelUuid = dialogScope.bigCodes.channel.channelUuid;
                        //获取付款行信息
                        qcApi.post('common/getBankInfo', {
                            type: dialogScope.tourOperatorChannelCategoryCode,
                            uuid: dialogScope.channelUuid
                        })
                            .success(function (result) {
                                dialogScope.paymentBanks = result.data;
                                dialogScope.selectedPayBank = dialogScope.paymentBanks != undefined ? dialogScope.paymentBanks[0] : null;
                            });
                    });

                    //地接社
                    dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;//地接社类型
                    dialogScope.$on('selectedTourOperatorType.change', function () {
                        dialogScope.tourOperator = null;//地接社选择
                    });
                    dialogScope.$on('selectedPayBank.change', function () {
                        dialogScope.selectedPayAccount = null;
                    });

                    //获取登录用户信息 包括收款行信息
                    dialogScope.userInfo = $rootScope.userInfo;
                    dialogScope.$on('selectedBank.change', function () {
                        dialogScope.selectedAccount = null;
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

                    dialogScope.orderReceiveCommit = function () {
                        if (!valid()) {
                            qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                            dialogScope.$broadcast('qcValid.check');
                            return;
                        }
                        dialogScope.newOrderReceive = {
                            orderUuid         : orderUuid,
                            speedyClearance   : dialogScope.speedyClearance,//'0':否,'1':是
                            receiveType       : dialogScope.selectedReceiveType.receiveTypeCode,
                            receivePeopleCount: dialogScope.receivePeopleCount,
                            receiveAmount     : dialogScope.receiveAmount,
                            exchangeRate      : dialogScope.convertedExchangeRate(),
                            currencyUuid      : dialogScope.selectedCurrency.currencyUuid,
                            paymentMethodCode : dialogScope.paymentMethod.paymentMethodCode,
                            payer             : dialogScope.payer,
                            checkNo           : dialogScope.checkNo,
                            checkIssueDate    : dialogScope.checkIssueDate,
                            paymentBank       : getPaymentBankName(),
                            paymentAccount    : getPaymentAccountNo(),
                            receiveBank       : dialogScope.selectedBank != undefined ? dialogScope.selectedBank.bankName : '',
                            receiveAccount    : dialogScope.selectedAccount != undefined ? dialogScope.selectedAccount.accountNo : '',
                            attachments       : dialogScope.attachments,
                            memo              : dialogScope.memo
                        };
                        if (dialogScope.reservationCount < dialogScope.receivePeopleCount) {
                            qcMessage.tip('收款人数应小于等于订单总人数');
                            return false;
                        }
                        qcApi.post('order/saveOrderpayInfo',
                            dialogScope.newOrderReceive
                        ).success(function (result) {
                                $rootScope.$broadcast('order.orderReceive.save', orderUuid);
                                dialogScope.closeThisDialog();
                            });
                    };
                    function getPaymentBankName() {
                        if (dialogScope.channelType.channelTypeCode == '2') {
                            return dialogScope.selectedPayBankName;
                        } else {
                            return dialogScope.selectedPayBank ? dialogScope.selectedPayBank.bankName : '';
                        }
                    }

                    function getPaymentAccountNo() {
                        if (dialogScope.channelType.channelTypeCode == '2') {
                            return dialogScope.selectedPayAccountNo;
                        } else {
                            return dialogScope.selectedPayAccount ? dialogScope.selectedPayAccount.accountNo : '';
                        }
                    }

                    function valid() {
                        var isValid = true;
                        if (!dialogScope.selectedReceiveType) {
                            isValid = false;
                        }
                        if (!dialogScope.receivePeopleCount) {
                            isValid = false;
                        }
                        if (!dialogScope.selectedCurrency) {
                            isValid = false;
                        }
                        if (!dialogScope.receiveAmount) {
                            isValid = false;
                        }
                        //0038需求
                        if (dialogScope.companyRoleCode != '0') {
                            if (!dialogScope.payer) {
                                isValid = false;
                            }
                        }


                        if (dialogScope.paymentMethod.paymentMethodCode == '1') {
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
        }
    }]);