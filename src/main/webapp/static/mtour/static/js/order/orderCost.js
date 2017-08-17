orderList.directive('orderCostInputBtn', ['urlConfig', 'qcApi', 'qcDialog', 'commonValue', 'qcObjectInArray',
    'fixedValue', '$rootScope', 'qcMessage', '$timeout', '$q',
    function (urlConfig, qcApi, qcDialog, commonValue, qcObjectInArray, fixedValue, $rootScope, qcMessage, $timeout, $q) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid           : '=',
                orderCost           : '=',
                //orderCost: '=',
                operatorType        : '=',
                settlementLockStatus: '='
            },
            link    : function (scope, ele, attrs) {
                ele.on('click', function () {
                        qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderCost.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                                //dialogScope.companyRoleCode = '0';
                                dialogScope.settlementLockStatus = scope.settlementLockStatus;
                                dialogScope.orderUuid = scope.orderUuid;
                                dialogScope.tourOperatorChannelCategoryCode = '1';
                                qcApi.post('order/getOrderPriceDetail', {
                                    orderUuid: dialogScope.orderUuid
                                }).success(function (result) {
                                    dialogScope.reservationCount = result.data.reservationCount;
                                });

                                var limitation=150;
                                dialogScope.$watch('memo',function(newVal,oldVal){
                                    if (newVal && newVal != oldVal) {
                                        if (newVal.length >= limitation) {
                                             qcMessage.tip('请保持输入字符数在150之内');
                                            dialogScope.memo = newVal.substr(0, limitation); // 这里截取有效的150个字符
                                        }
                                    }
                                });










                                //dialogScope.currencies = commonValue.currencies;
                                //dialogScope.selectedCurrency = dialogScope.defaultCurrency;
                                var defer1 = $q.defer();
                                qcApi.post('order/getExistsCurrency', {
                                    orderId: dialogScope.orderUuid,
                                    type   : '2'
                                }).success(function (result) {
                                    dialogScope.currencies = result.data;
                                    dialogScope.selectedCurrency = dialogScope.currencies[0];
                                    defer1.resolve();
                                });

                                //dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                //获取此订单的大编号组
                                var defer2 = $q.defer();
                                qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: scope.orderUuid}).success(function (result) {
                                    dialogScope.bigCodes = result.data;
                                    dialogScope.channelType = dialogScope.bigCodes.channelType;
                                    dialogScope.channel = dialogScope.bigCodes.channel;
                                    dialogScope.channelUuid = dialogScope.bigCodes.channel.channelUuid;
                                    dialogScope.selectedInvoiceOriginal = dialogScope.bigCodes.invoiceOriginals[0];
                                    initReceiveBank().then(function () {
                                        defer2.resolve();
                                    });
                                });
                                $q.all([defer1.promise, defer2.promise]).then(function () {
                                    if (scope.operatorType != '0') {//修改成本时,绑定当前的成本
                                        getCost().then(function (result) {
                                            dialogScope.fundsName = result.fundsName;
                                            dialogScope.costTotalDeposit = result.costTotalDeposit;
                                            if (result.invoiceOriginalTypeCode == '0') {
                                                dialogScope.selectedInvoiceOriginal = qcObjectInArray(dialogScope.bigCodes.invoiceOriginals, 'PNR', result.PNR);
                                            }
                                            else {
                                                dialogScope.selectedInvoiceOriginal = qcObjectInArray(dialogScope.bigCodes.invoiceOriginals, 'invoiceOriginalUuid', result.invoiceOriginalUuid);
                                            }

                                            qcApi.post('mtourfinance/getAirlineNames', {
                                                orderUuid          : scope.orderUuid,
                                                invoiceOriginalUuid: dialogScope.selectedInvoiceOriginal.invoiceOriginalUuid
                                            }).success(function (airlineNameResult) {
                                                dialogScope.airlineNames = airlineNameResult.data;
                                                dialogScope.selectedAirLineName = qcObjectInArray(dialogScope.airlineNames, 'airlineName', result.airlineName);
                                            });


                                            dialogScope.peopleCount = result.peopleCount;
                                            dialogScope.exchangeRate = result.exchangeRate;
                                            //dialogScope.convertedAmount = result.convertedAmount;
                                            dialogScope.selectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', result.currencyUuid);
                                            //if (result.exchangeRate) {
                                            //    dialogScope.selectedCurrency.exchangeRate = result.exchangeRate;
                                            //}
                                            dialogScope.price = result.price;
                                            dialogScope.tourOperatorChannelCategoryCode = result.tourOperatorChannelCategoryCode;
                                            dialogScope.tourOperatorType = qcObjectInArray(dialogScope.tourOperatorTypes, 'tourOperatorTypeCode', result.tourOperatorOrChannelTypeCode);
                                            //$timeout(function () {
                                            //渠道是固定的
                                            if (result.invoiceOriginalTypeCode == 0 && result.tourOperatorChannelCategoryCode == 1) {//大编号:PNR + 地接社
                                                dialogScope.tourOperator = {
                                                    tourOperatorCode: result.tourOperatorOrChannelUuid,
                                                    tourOperatorName: result.tourOperatorOrChannelName
                                                };
                                            }
                                            $timeout(function () {
                                                if (result.tourOperatorChannelCategoryCode == '2' && result.tourOperatorOrChannelTypeCode == '2') {//类别为渠道+ 非签约渠道(文本框输入银行和账户)
                                                    dialogScope.selectedPayBankName = result.paymentBank;
                                                    dialogScope.selectedPayAccountNo = result.paymentAccount;
                                                } else {//其他情况选择账户
                                                    dialogScope.selectedPayBank = {bankName: result.paymentBank};
                                                    dialogScope.selectedPayAccount = {accountNo: result.paymentAccount};
                                                }
                                            });

                                            dialogScope.memo = result.memo;
                                            //});

                                        });
                                    }
                                    else {
                                        qcApi.post('mtourfinance/getAirlineNames', {
                                            orderUuid          : scope.orderUuid,
                                            invoiceOriginalUuid: dialogScope.selectedInvoiceOriginal.invoiceOriginalUuid
                                        }).success(function (airlineNameResult) {
                                            dialogScope.airlineNames = airlineNameResult.data;
                                            dialogScope.selectedAirLineName = dialogScope.airlineNames[0];
                                        });
                                        dialogScope.exchangeRate = dialogScope.selectedCurrency.exchangeRate;
                                    }


                                });
                                dialogScope.$on('selectedCurrency.change', function () {
                                    dialogScope.exchangeRate = dialogScope.selectedCurrency.exchangeRate;
                                });

                                dialogScope.$on('selectedInvoiceOriginal.change', function () {
                                    initReceiveBank();
                                    qcApi.post('mtourfinance/getAirlineNames', {
                                        orderUuid          : scope.orderUuid,
                                        invoiceOriginalUuid: dialogScope.selectedInvoiceOriginal.invoiceOriginalUuid
                                    }).success(function (result) {
                                        dialogScope.airlineNames = result.data;
                                        dialogScope.selectedAirLineName = dialogScope.airlineNames[0];
                                    });
                                });
                                dialogScope.$watch('tourOperatorChannelCategoryCode', function () {
                                    initReceiveBank();
                                });

                                dialogScope.$on('tourOperatorType.change', function () {
                                    dialogScope.tourOperator = null;
                                    dialogScope.paymentBanks = [];
                                    dialogScope.selectedPayBank = null;
                                    dialogScope.selectedPayAccount = null;
                                });
                                dialogScope.$on('selectedTourOperator.change', function ($e) {
                                    qcApi.post('common/getBankInfo', {
                                        type: dialogScope.tourOperatorChannelCategoryCode,
                                        uuid: $e.targetScope.selectedItem.tourOperatorUuid
                                    })
                                        .success(function (result) {
                                            dialogScope.paymentBanks = result.data;
                                            //dialogScope.selectedPayBank = dialogScope.paymentBanks != undefined ? dialogScope.paymentBanks[0] : null;
                                            //dialogScope.selectedPayAccount = dialogScope.selectedPayBank != undefined ? dialogScope.selectedPayBank.accounts[0] : '';
                                            dialogScope.selectedPayBank = null;
                                            dialogScope.selectedPayAccount = null;
                                        });
                                });

                                //默认货币设置
                                dialogScope.defaultCurrency = commonValue.defaultCurrency;

                                dialogScope.convertedAmount = function () {
                                    return dialogScope.price != undefined ? dialogScope.peopleCount * dialogScope.price * dialogScope.exchangeRate : "";
                                }

                                dialogScope.$on('selectedPayBank.change', function () {
                                    dialogScope.selectedPayAccount = null;
                                });
                                dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;


                                dialogScope.save = function (type) {
                                    if (!valid()) {
                                        qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                        dialogScope.$broadcast('qcValid.check');
                                        return;
                                    }

                                    dialogScope.newOrderCost = {
                                        saveType                       : type,//'0':保存,'1':提交
                                        orderUuid                      : scope.orderUuid,
                                        costUuid                       : scope.orderCost ? scope.orderCost.costUuid : undefined,
                                        fundsName                      : dialogScope.fundsName,
                                        invoiceOriginalTypeCode        : dialogScope.selectedInvoiceOriginal.invoiceOriginalTypeCode,//大编号(票源类型)
                                        invoiceOriginalUuid            : dialogScope.selectedInvoiceOriginal.invoiceOriginalUuid,//大编号Uuid
                                        PNR                            : dialogScope.selectedInvoiceOriginal.PNR,//大编号为PNR的时候才有意义
                                        exchangeRate                   : dialogScope.exchangeRate,
                                        peopleCount                    : dialogScope.peopleCount,
                                        currencyUuid                   : dialogScope.selectedCurrency.currencyUuid,
                                        price                          : dialogScope.price,
                                        convertedCurrencyUuid          : dialogScope.defaultCurrency.currencyUuid,
                                        convertedAmount                : dialogScope.convertedAmount(),
                                        tourOperatorChannelCategoryCode: dialogScope.tourOperatorChannelCategoryCode,
                                        paymentBank                    : dialogScope.selectedPayBank ? dialogScope.selectedPayBank.bankName : '',
                                        paymentAccount                 : dialogScope.selectedPayAccount ? dialogScope.selectedPayAccount.accountNo : '',
                                        memo                           : dialogScope.memo
                                        //airlineUuid                    : dialogScope.selectedAirLineName.airlineUuid,
                                        //airlineName                    : dialogScope.selectedAirLineName.airlineName,
                                        //costTotalDeposit               : dialogScope.costTotalDeposit
                                    };

                                    if (dialogScope.tourOperatorChannelCategoryCode == '1') {//选择地接社
                                        if (dialogScope.selectedInvoiceOriginal.invoiceOriginalTypeCode == 1) {//大编号是地接社时，
                                            dialogScope.newOrderCost.tourOperatorOrChannelTypeCode = dialogScope.selectedInvoiceOriginal.tourOperatorTypeCode;
                                            dialogScope.newOrderCost.tourOperatorOrChannelUuid = dialogScope.selectedInvoiceOriginal.tourOperatorUuid;
                                            dialogScope.newOrderCost.tourOperatorOrChannelName = dialogScope.selectedInvoiceOriginal.tourOperatorName;
                                        }
                                        else {
                                            dialogScope.newOrderCost.tourOperatorOrChannelTypeCode = dialogScope.tourOperatorType ? dialogScope.tourOperatorType.tourOperatorTypeCode : undefined;
                                            dialogScope.newOrderCost.tourOperatorOrChannelUuid = dialogScope.tourOperator ? dialogScope.tourOperator.tourOperatorUuid : undefined;
                                            dialogScope.newOrderCost.tourOperatorOrChannelName = dialogScope.tourOperator ? dialogScope.tourOperator.tourOperatorName : undefined;
                                        }
                                    }
                                    else {
                                        dialogScope.newOrderCost.tourOperatorOrChannelTypeCode = dialogScope.channelType.channelTypeCode;
                                        dialogScope.newOrderCost.tourOperatorOrChannelUuid = dialogScope.channel.channelUuid;
                                        dialogScope.newOrderCost.tourOperatorOrChannelName = dialogScope.channel.channelName;
                                    }
                                    if (dialogScope.reservationCount < dialogScope.peopleCount) {
                                        qcMessage.tip('收款人数应小于等于订单总人数');
                                        return false;
                                    }


                                    //l类别为渠道 + 非签约渠道(银行和账户 是手动输入的)
                                    if (dialogScope.newOrderCost.tourOperatorChannelCategoryCode == '2' && dialogScope.newOrderCost.tourOperatorOrChannelTypeCode == '2') {
                                        dialogScope.newOrderCost.paymentBank = dialogScope.selectedPayBankName;
                                        dialogScope.newOrderCost.paymentAccount = dialogScope.selectedPayAccountNo;
                                    }
                                    //美途和华尔使用2套接口
                                    if (dialogScope.companyRoleCode != '0') {
                                        qcApi.post('mtourfinance/saveOrUpdateCostRecord4Old',
                                            dialogScope.newOrderCost
                                        ).success(function (result) {
                                                $rootScope.$broadcast('order.orderCost.save', scope.orderUuid);
                                                dialogScope.closeThisDialog();
                                            });
                                    }
                                    else {
                                        //对于美途收款行名称与收款账户,地接社类型、地接社名称都不可修改,则取原来的值
                                        dialogScope.newOrderCost.paymentBank = dialogScope.selectedInvoiceOriginal.receiveBank;
                                        dialogScope.newOrderCost.paymentAccount = dialogScope.selectedInvoiceOriginal.receiveAccountNo;
                                        dialogScope.newOrderCost.tourOperatorOrChannelTypeCode = dialogScope.selectedInvoiceOriginal.tourOperatorTypeCode;
                                        dialogScope.newOrderCost.tourOperatorOrChannelName = dialogScope.selectedInvoiceOriginal.tourOperatorName;
                                        dialogScope.newOrderCost.tourOperatorOrChannelUuid = dialogScope.selectedInvoiceOriginal.tourOperatorUuid;

                                        dialogScope.newOrderCost.airlineUuid = dialogScope.selectedAirLineName.airlineUuid;
                                        dialogScope.newOrderCost.airlineName = dialogScope.selectedAirLineName.airlineName;
                                        dialogScope.newOrderCost.costTotalDeposit = dialogScope.costTotalDeposit;
                                        qcApi.post('mtourfinance/saveOrUpdateCostRecord',
                                            dialogScope.newOrderCost
                                        ).success(function (result) {
                                                $rootScope.$broadcast('order.orderCost.save', scope.orderUuid);
                                                dialogScope.closeThisDialog();
                                            });
                                    }

                                };

                                function valid() {
                                    var isValid = true;
                                    if (!dialogScope.fundsName) {
                                        isValid = false;
                                    }
                                    if (!dialogScope.selectedInvoiceOriginal) {
                                        isValid = false;
                                    }
                                    if (!dialogScope.peopleCount) {
                                        isValid = false;
                                    }
                                    if (!dialogScope.price) {
                                        isValid = false;
                                    }
                                    if (dialogScope.companyRoleCode == '0') {
                                        if (!dialogScope.costTotalDeposit && dialogScope.costTotalDeposit != 0) {
                                            isValid = false;
                                        }
                                    }

                                    return isValid;
                                }

                                function initReceiveBank() {
                                    var defer = $q.defer();
                                    dialogScope.tourOperatorType = null;
                                    dialogScope.tourOperator = null;
                                    dialogScope.selectedPayBank = null;
                                    dialogScope.selectedPayBankName = null;
                                    dialogScope.paymentBanks = [];
                                    dialogScope.selectedPayAccount = null;
                                    dialogScope.selectedPayAccountNo = null;
                                    if (dialogScope.tourOperatorChannelCategoryCode == '2') {//获取渠道商银行
                                        qcApi.post('common/getBankInfo', {
                                            type: dialogScope.tourOperatorChannelCategoryCode,
                                            uuid: dialogScope.channelUuid
                                        })
                                            .success(function (result) {
                                                dialogScope.paymentBanks = result.data;
                                                dialogScope.selectedPayBank = dialogScope.paymentBanks != undefined ? dialogScope.paymentBanks[0] : null;
                                                dialogScope.selectedPayAccount = dialogScope.selectedPayBank != undefined ? dialogScope.selectedPayBank.accounts[0] : '';
                                                defer.resolve();
                                            });
                                    }
                                    else {//获取地接社银行
                                        var tourOrChannelUuid;
                                        if (dialogScope.selectedInvoiceOriginal != undefined) {
                                            if (dialogScope.selectedInvoiceOriginal.invoiceOriginalTypeCode == 0) {

                                                tourOrChannelUuid = undefined;
                                            }
                                            else {
                                                tourOrChannelUuid = dialogScope.selectedInvoiceOriginal.tourOperatorUuid;

                                            }
                                            dialogScope.selectedPayBank = null;
                                            dialogScope.selectedPayAccount = null;
                                            if (tourOrChannelUuid) {
                                                qcApi.post('common/getBankInfo', {
                                                    type: dialogScope.tourOperatorChannelCategoryCode,
                                                    uuid: tourOrChannelUuid
                                                })
                                                    .success(function (result) {
                                                        dialogScope.paymentBanks = result.data;
                                                        dialogScope.selectedPayBank = dialogScope.paymentBanks != undefined ? dialogScope.paymentBanks[0] : null;
                                                        dialogScope.selectedPayAccount = dialogScope.selectedPayBank != undefined ? dialogScope.selectedPayBank.accounts[0] : '';
                                                        defer.resolve();
                                                    });
                                            } else {
                                                defer.resolve();
                                            }
                                        } else {
                                            defer.resolve();
                                        }
                                    }
                                    if (dialogScope.selectedInvoiceOriginal != undefined) {
                                        if (dialogScope.selectedInvoiceOriginal.invoiceOriginalTypeCode == 0) {
                                            dialogScope.tourOperatorType = null;
                                            dialogScope.tourOperator = null;
                                            dialogScope.selectedPayBank = null;
                                            dialogScope.selectedPayAccount = null;
                                        }
                                        else {
                                            dialogScope.tourOperatorType = qcObjectInArray(dialogScope.tourOperatorTypes, 'tourOperatorTypeCode', dialogScope.selectedInvoiceOriginal.tourOperatorTypeCode);
                                        }
                                    }
                                    return defer.promise;
                                };
                            }
                            ],
                            width      : 500,
                            plain      : false
                        });
                        function getCost() {
                            var defer = $q.defer();
                            qcApi.post('mtourfinance/getCostDetail', {
                                costUuid: scope.orderCost.costUuid
                            }).success(function (result) {
                                defer.resolve(result.data);
                            });
                            return defer.promise;
                        }
                    }
                );
            }
        }
    }])