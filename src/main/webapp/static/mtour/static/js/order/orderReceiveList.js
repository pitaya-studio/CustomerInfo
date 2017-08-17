orderList.directive('orderReceiveList', ['$rootScope', 'urlConfig', 'commonValue', 'qcObjectInArray', 'qcApi', 'qcDialog', 'fixedValue', '$q', 'checkRole',
    function ($rootScope, urlConfig, commonValue, qcObjectInArray, qcApi, qcDialog, fixedValue, $q, checkRole) {
        return {
            restrict   : 'A',
            scope      : {
                orderUuid           : '=',
                settlementLockStatus: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReceiveList.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;//登录用户是否为美途
                scope.lockStatus = scope.settlementLockStatus;//结算单锁定状态
                orderReceiveList();
                scope.currencies = commonValue.currencies;
                scope.userInfo = $rootScope.userInfo;
                scope.toggleSubTable = function () {
                    scope.$emit('subTable.toggle.request', 'orderReceiveList' + scope.orderUuid);
                };

                function orderReceiveList() {
                    qcApi.post('order/getOrderReceiptList', {
                        orderUuid: scope.orderUuid
                    }).success(function (result) {
                        scope.newList = result.data;
                        angular.forEach(scope.newList, function (orderReceive) {
                            orderReceive.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', orderReceive.currencyUuid);
                            orderReceive.convertedCurrency = commonValue.defaultCurrency;
                            //orderReceive.exchangeRate = orderReceive.currency.exchangeRate;
                            orderReceive.receiveMethod = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', orderReceive.receiveMethodCode.toString());
                            orderReceive.convertedCurrency = commonValue.defaultCurrency;
                            orderReceive.selectedReceiveType = qcObjectInArray(fixedValue.receiveType, 'receiveTypeCode', orderReceive.receiveType);
                            orderReceive.convertedCurrency = commonValue.defaultCurrency;
                            orderReceive.accountCompleteStatus = qcObjectInArray(fixedValue.accountCompleteStatus, 'accountCompleteStatusCode', orderReceive.completeCheck);
                        });
                    });
                }
                
                /**
                 * 重新提交
                 *   orderUuid  : orderReceive.orderUuid,
                        receiveUuid: orderReceive.receiveUuid
                 */
                 scope.submitAgain=function (orderReceive){
                	var lastType={};
                	var lastReceiveCount;
                	qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReceiveAgain.html',
                        controller : ['$scope', function (dialogScope) {
                            dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                            //默认货币设置
                            dialogScope.defaultCurrency = commonValue.defaultCurrency;
                            //订单收款货币和汇率
                            var defer = $q.defer();
                            qcApi.post('order/getExistsCurrency', {orderId: orderReceive.orderUuid, type: '2'}).success(function (result) {
                                dialogScope.currencies = result.data;
                                dialogScope.selectedCurrency = dialogScope.currencies[0];
                                defer.resolve();
                            });
                            defer.promise.then(function () {
                                //查询订单的未收金额和外报总额
                                qcApi.post('order/getOrderPriceDetail', {
                                    orderUuid: orderReceive.orderUuid
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
                            //货币换算
                            dialogScope.convertedExchangeRate = function () {
                                return dialogScope.selectedCurrency != undefined ? dialogScope.selectedCurrency.exchangeRate : '';
                            };
                            dialogScope.convertedAmount = function () {
                                return dialogScope.receiveAmount != undefined ? dialogScope.receiveAmount * dialogScope.convertedExchangeRate() : "";
                            }
                            //订单uuid
                            dialogScope.orderUuid = orderReceive.orderUuid;
                            //dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                            //获取此订单的大编号组
                            qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: orderReceive.orderUuid}).success(function (result) {
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

                            qcApi.post('order/getOrderReceiptDetail', {
                                orderUuid  : orderReceive.orderUuid,
                                receiveUuid: orderReceive.receiveUuid
                            }).success(function (result) {
                                           // dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                            dialogScope.OrderReceiveDetail = result.data;
                                            //即时结算设置
			                                dialogScope.speedyClearance =dialogScope.OrderReceiveDetail.speedyClearance;
			                                //收款类别
			                                dialogScope.receiveTypes = fixedValue.orderReceiveType;
			                                //设置上次交易的类型值为默认值
			                                dialogScope.selectedReceiveType=qcObjectInArray(fixedValue.receiveType, 'receiveTypeCode', dialogScope.OrderReceiveDetail.receiveTypeCode);
                                            //现金  paymentMethodCode"3"  paymentMethodName  现金
                                            dialogScope.paymentMethodType = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', dialogScope.OrderReceiveDetail.paymentMethodCode.toString());
                                            /**currencyName"人民币"
                                             * currencyCode "¥"
                                             */
                                            dialogScope.selectedCurrency = qcObjectInArray(commonValue.currencies, 'currencyUuid', dialogScope.OrderReceiveDetail.currency);
                                            var selectedCurrency={};
                                            dialogScope.convertedCurrency = commonValue.defaultCurrency;
                                            //设置收款人数为上次的值
                                            dialogScope.receivePeopleCount=dialogScope.OrderReceiveDetail.receivePeopleCount;
                                            dialogScope.receiveAmount= dialogScope.OrderReceiveDetail.receiveAmount;
                                            //货币换算
                                            dialogScope.convertedExchangeRate = function () {
                                                return dialogScope.selectedCurrency ? dialogScope.selectedCurrency.exchangeRate : '';
                                            };
                                            dialogScope.convertedAmount = function () {
                                                return dialogScope.receiveAmount != undefined ? dialogScope.receiveAmount * dialogScope.selectedCurrency.exchangeRate : "";
                                            }
                                            //付款的形式
                                            //支付方式
                                            dialogScope.paymentMethod = {};
                                            dialogScope.paymentMethod.payMethods = fixedValue.paymentMethods;
                                            dialogScope.paymentMethod.paymentMethodCode=dialogScope.OrderReceiveDetail.paymentMethodCode;
                                            //付款单位
                                            dialogScope.payer=dialogScope.OrderReceiveDetail.payer;
                                            dialogScope.memo=dialogScope.OrderReceiveDetail.memo;
                                            //付款行名称
                                            dialogScope.selectedPayBankName=dialogScope.OrderReceiveDetail.paymentBank;
                                            //凭证信息
                                            dialogScope.attachments=dialogScope.OrderReceiveDetail.attachments;
                                            //支票形式
                                            if(dialogScope.OrderReceiveDetail.paymentMethodCode=='1'){
                                            	dialogScope.checkNo=dialogScope.OrderReceiveDetail.checkNo;
                                            	dialogScope.checkIssueDate=dialogScope.OrderReceiveDetail.checkIssueDate;
                                            }
                                          //汇款形式
                                            if(dialogScope.OrderReceiveDetail.paymentMethodCode=='4'){
                                            	  //为三个下拉框付默认选中值为上次的值
//                                                dialogScope.selectedPayBank=dialogScope.OrderReceiveDetail.paymentBank;
                                                dialogScope.selectedPayBank={bankName:dialogScope.OrderReceiveDetail.paymentBank};
                                                dialogScope.selectedPayAccount={accountNo:dialogScope.OrderReceiveDetail.paymentAccount};
                                                dialogScope.selectedBank={bankName:dialogScope.OrderReceiveDetail.receiveBank};
                                                dialogScope.selectedAccount={accountNo:dialogScope.OrderReceiveDetail.receiveAccount};
                                            }
                            		}
                                )
                            
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


                            dialogScope.orderReceiveCommit = function () {
                                if (!valid()) {
                                    qcDialog.openMessage({msg: '输入项有误,请重新确认'});
                                    dialogScope.$broadcast('qcValid.check');
                                    return;
                                }
                                dialogScope.newOrderReceive = {
                                    orderUuid         : orderReceive.orderUuid,
                                    receiveUuid		  : orderReceive.receiveUuid,
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
                                    memo              : dialogScope.memo,
                                    Resubmit		  : true
                                };
                                if (dialogScope.reservationCount < dialogScope.receivePeopleCount) {
                                    qcMessage.tip('收款人数应小于等于订单总人数');
                                    return false;
                                }
                                qcApi.post('order/saveOrderpayInfo',
                                    dialogScope.newOrderReceive
                                ).success(function (result) {
                                        $rootScope.$broadcast('order.orderReceive.save', orderReceive.orderUuid);
                                        dialogScope.closeThisDialog();
                                        if((result.Resubmit!=null || result.Resubmit!="") && result.Resubmit==false){
                                 	       alert("该记录已重新提交，提交失败!");	
                                 	    }
                                    }).error(function (){
                                    	qcMessage.tip('该单据已经提交，请勿重复操作');
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

                scope.showOrderReceiveDetail = function (orderReceive) {
                    qcApi.post('order/getOrderReceiptDetail', {
                        orderUuid  : orderReceive.orderUuid,
                        receiveUuid: orderReceive.receiveUuid
                    }).success(function (result) {
                        qcDialog.open({
                                templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderReceiveDetail.html',
                                controller : ['$scope', function (dialogScope) {
                                    dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                    dialogScope.OrderReceiveDetail = result.data;

                                    //dialogScope.OrderReceiveDetail.receiveTypeCode
                                    dialogScope.receiveType = qcObjectInArray(fixedValue.receiveType, 'receiveTypeCode', dialogScope.OrderReceiveDetail.receiveTypeCode);
                                    //dialogScope.OrderReceiveDetail.paymentMethodCode
                                    dialogScope.paymentMethodType = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', dialogScope.OrderReceiveDetail.paymentMethodCode.toString());
                                    dialogScope.selectedCurrency = qcObjectInArray(commonValue.currencies, 'currencyUuid', dialogScope.OrderReceiveDetail.currency);
                                    dialogScope.convertedCurrency = commonValue.defaultCurrency;

                                    //货币换算
                                    //dialogScope.convertedExchangeRate = function () {
                                    //    return dialogScope.selectedCurrency ? dialogScope.selectedCurrency.exchangeRate : '';
                                    //};
                                    dialogScope.convertedAmount = function () {
                                        return dialogScope.OrderReceiveDetail.receiveAmount != undefined ? dialogScope.OrderReceiveDetail.receiveAmount * dialogScope.OrderReceiveDetail.exchangeRate : "";
                                    }
                                }
                                ],
                                width      : 500,
                                plain      : false
                            }
                        );
                    });
                };
                scope.isRole = function (url) {
                    return checkRole(url);
                };

                scope.cancelOrderReceive = function (orderReceive) {
                    qcDialog.openMessage({
                        msg : '是否撤销',
                        type: 'confirm'
                    }).then(function () {
                        qcApi.post('order/orderReceiptCancel', {
                            orderUuid  : orderReceive.orderUuid,
                            receiveUuid: orderReceive.receiveUuid
                        }).success(function (result) {
                            $rootScope.$broadcast('order.orderReceive.cancel', orderReceive.orderUuid);
                        });
                    });
                };
            }
        }
            ;
    }])
;