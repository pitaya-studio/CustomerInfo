financePayList.directive('batchPayRecordDetail', ['$rootScope', 'urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi', 'qcMessage', '$timeout',
    function ($rootScope, urlConfig, fixedValue, commonValue, qcObjectInArray, qcDialog, qcApi, qcMessage, $timeout) {
        return {
            restrict: 'A',
            scope   : {
                orderUuid        : '=',
                paymentObjectUuid: '=',
                payobjPaymentUuid: '='
            },
            link    : function (scope, ele, attrs) {
                var pop;
                var showTimer;
                var hideTimer;
                ele.on('click', function () {
                    $timeout.cancel(hideTimer);
                    showTimer = $timeout(function () {
                        pop = qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/batchPayRecordDetailList.html',
                            controller : ['$scope', function (dialogScope) {
                                dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                //getBatchPayRecordList(scope.orderUuid, payment.paymentObjectUuid);
                                //dialogScope.detailList = {
                                //    paymentMethodCode   : '1',
                                //    paymentMethodName   : '支票',
                                //    paymentDate         : '2015-12-12',
                                //    recordList          : [{
                                //        paymentUuid      : '1111',
                                //        approvalDate     : '2015-12-12',
                                //        fundsType        : '1',
                                //        applicant        : '申请人',
                                //        payableAmount    : '$100',
                                //        paidAmount       : '$80',
                                //        exchangeRate     : '1',
                                //        paymentAmount    : '$10',
                                //        paymentStatusCode: '0',//0-已付款 1-已撤销
                                //        paymentStatusName: '已付款'//0-已付款 1-已撤销
                                //    }],
                                //    paymentTotalAmount  : '$100.00+C$100.00+￥100.00',
                                //    convertedTotalAmount: '￥112.00',
                                //    receiveCompany      : '收款单位',
                                //    checkNo             : '支票号',//只有付款方式是支票时才有效
                                //    checkIssueDate      : '开票日期', //只有付款方式是支票时才有效
                                //    paymentBank         : '付款行',//只有付款方式是汇款时才有效
                                //    paymentAccount      : '付款账号',//只有付款方式是汇款时才有效
                                //    receiveBank         : '收款行',//只有付款方式是汇款时才有效
                                //    receiveAccount      : '收款账户',//只有付款方式是汇款时才有效
                                //    attachments         : [//付款附件
                                //        {
                                //            attachmentUuid: '付款附件uuid',
                                //            fileName      : '付款附件名称',
                                //            attachmentUrl : '付款附件url'
                                //        }],
                                //    memo                : '备注'
                                //};

                                qcApi.post('mtourfinance/queryPayJDetail', {
                                    orderUuid             : scope.orderUuid,
                                    paymentObj_paymentUuid: scope.payobjPaymentUuid,
                                    paymentObjectUuid     : scope.paymentObjectUuid
                                }).success(function (result) {
                                    dialogScope.detailList = result.data;
                                });
                            }],
                            width      : 850,
                            height     : 300,
                            plain      : false
                        });
                    }, 1000);
                });
//                ele.on('mouseleave', function () {
//                    $timeout.cancel(showTimer);
//                    hideTimer = $timeout(function () {
//                        qcDialog.close(pop.id);
//                    }, 1000);
//                });
            }
        };
    }]);
