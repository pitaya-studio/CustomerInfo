financePayList.directive('paymentRecordList', ['$rootScope', 'urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi', 'qcMessage',
    function ($rootScope, urlConfig, fixedValue, commonValue, qcObjectInArray, qcDialog, qcApi, qcMessage) {
        return {
            restrict: 'A',
            scope: {
                orderUuid: '=',
                paymentUuid: '=',
                fundsType: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/paymentRecordList.html',
            link: function (scope, ele, attrs) {
                paymentRecordList();
                scope.showPaymentDetail = function (payment) {
                    qcApi.post('mtourfinance/getRefundInfo', {
                        paymentUuid: payment.paymentUuid,
                        paymentDetailUuid: payment.paymentDetailUuid,
                        fundsType: payment.fundsType
                    }).success(function (result) {
                        qcDialog.open({
                                templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/paymentDetail.html',
                                controller: ['$scope', function (dialogScope) {
                                    dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                    dialogScope.paymentDetail = result.data;
                                    dialogScope.paymentMethod = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', dialogScope.paymentDetail.paymentMethodCode);
                                    dialogScope.currencies = commonValue.currencies;
                                    dialogScope.currency = qcObjectInArray( dialogScope.currencies,'currencyUuid',dialogScope.paymentDetail.currencyUuid);
                                    dialogScope.convertedCurrency = commonValue.defaultCurrency;
                                }
                                ],
                                width: 500,
                                plain: false
                            }
                        );
                    });
                };
                scope.cancelPayment = function (payment) {
                    qcApi.post('mtourfinance/undoRefundPayInfo', {
                        paymentUuid: payment.paymentUuid,
                        paymentDetailUuid: payment.paymentDetailUuid,
                        fundsType: payment.fundsType
                    }).success(function (result) {
                        $rootScope.$broadcast('finance.payment.cancel',payment.paymentUuid,scope.fundsType);
                    });
                }


                scope.currencies = commonValue.currencies;
                scope.userInfo = $rootScope.userInfo;
                function paymentRecordList() {
                    if (!scope.newList) {
                        qcApi.post('mtourfinance/getPayRecord', {
                            paymentUuid: scope.paymentUuid,
                            fundsType: scope.fundsType
                        }).success(function (result) {
                            scope.newList = result.data;
                            angular.forEach(scope.newList, function (paymentRecord) {
                                paymentRecord.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', paymentRecord.currencyUuid);
                                paymentRecord.paymentMethod = qcObjectInArray(fixedValue.paymentMethods, 'paymentMethodCode', paymentRecord.paymentMethod);
                                paymentRecord.paymentRecordStatus = qcObjectInArray(fixedValue.financePaymentRecordStatus, 'financePaymentRecordStatusCode', paymentRecord.financePaymentRecordStatusCode);
                            });
                        });
                    }
                }
            }
        };
    }])
;
