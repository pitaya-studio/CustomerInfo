orderList.directive('orderCostList', ['$rootScope','urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi', 'qcMessage',
    function ($rootScope, urlConfig, fixedValue, commonValue, qcObjectInArray, qcDialog, qcApi, qcMessage) {
        return {
            restrict   : 'A',
            scope      : {
                orderUuid           : '=',
                settlementLockStatus: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderCostList.html',
            link       : function (scope, ele, attrs) {
                //查询列表
                getNewList();
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;//0024美途国际用户
                scope.toggleSubTable = function () {
                    scope.$emit('subTable.toggle.request', 'orderCostList' + scope.orderUuid);
                };
                function getNewList() {
                    qcApi.post('mtourfinance/getCostRecords', {
                        orderUuid: scope.orderUuid
                    }).success(function (result) {
                        scope.newList = result.data;

                        angular.forEach(scope.newList, function (orderCost) {
                            orderCost.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', orderCost.currencyUuid);
                            orderCost.convertedCurrency = commonValue.defaultCurrency;
                            orderCost.invoiceOriginal = {};
                            if (orderCost.invoiceOriginalTypeCode == 0) {
                                orderCost.invoiceOriginal.invoiceOriginalCode = orderCost.PNR;
                                orderCost.invoiceOriginal.invoiceOriginalName = orderCost.PNR;
                            }
                            else {
                                orderCost.invoiceOriginal.invoiceOriginalCode = orderCost.tourOperatorUuid;
                                orderCost.invoiceOriginal.invoiceOriginalName = orderCost.tourOperatorName;
                            }

                        });

                    });
                }
                //添加多选按钮

                scope.tesarry=scope.newList;//初始化数据
                scope.choseArr=[];//定义数组用于存放前端显示
                var str="";//
                var flag='';//是否点击了全选，是为a


                scope.all= function (c,v) {//全选
                    var _flag= c.currentTarget.checked;
                    angular.forEach(scope.newList, function (orderCost) {

                        orderCost.batchChecked=false;//默认未选中
                        if (_flag == true) {
                            if(orderCost.stateCode=='2'){
                                orderCost.batchChecked = true;
                                choseArr = v;
                            }
                        } else {
                            orderCost.batchChecked = false;
                            choseArr = [""];
                        }

                        flag = 'a';
                    })
                };
               /* scope.isSelected = function(id){
                        return scope.selected.indexOf(id)>=0;
                       }*/
                scope.chk= function (z,x) {//单选或者多选
                    if(flag=='a') {//在全选的基础上操作
                        str = scope.choseArr.join(',') + ',';
                    }
                    if (x == true) {//选中
                        str = str + z + ',';
                    } else {
                        str = str.replace(z + ',', '');//取消选中
                    }

                    scope.choseArr=(str.substr(0,str.length-1)).split(',');

                };
                scope.delete= function () {// 操作CURD

                    if(scope.choseArr[0]==""||scope.choseArr.length==0){//没有选择一个的时候提示
                        alert("请至少选中一条数据在操作！")
                        return;
                    };

                    for(var i=0;i<scope.choseArr.length;i++){
                        //alert(scope.choseArr[i]);
                        console.log(scope.choseArr[i]);//遍历选中的id
                    }
                };





                scope.canBatchCommit = function () {
                    var canBatch = false;
                    if (scope.companyRoleCode != '0') {
                        angular.forEach(scope.newList, function (orderCost) {
                            if (orderCost.batchChecked) {
                                canBatch = true;
                            }
                        });
                    }
                    else {
                        if (scope.settlementLockStatus != 1) {//美途用户登录且订单结算单未锁定
                            canBatch = true;
                        }
                    }
                    return canBatch;
                }
                scope.showOrderCostDetail = function (orderCost) {
                    qcApi.post('mtourfinance/getCostDetail', {
                        costUuid: orderCost.costUuid
                    }).success(function (result) {
                        qcDialog.open({
                                templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/orderCostDetail.html',
                                controller : ['$scope', function (dialogScope) {
                                    dialogScope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                                    //dialogScope.companyRoleCode = '0';
                                    qcApi.post('order/getAirticketOrderPNCListByOrderUuid', {orderUuid: scope.orderUuid}).success(function (result) {
                                        dialogScope.bigCodes = result.data;
                                    });
                                    dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                                    dialogScope.OrderCostDetail = result.data;
                                    dialogScope.convertedCurrency = commonValue.defaultCurrency;
                                    dialogScope.tourOperatorTypes = commonValue.tourOperatorTypes;
                                    dialogScope.selectedTourType = qcObjectInArray(dialogScope.tourOperatorTypes, 'tourOperatorTypeCode', dialogScope.OrderCostDetail.tourOperatorOrChannelTypeCode);
                                    dialogScope.channelTypes = fixedValue.channelType;
                                    dialogScope.selectedChannelType = qcObjectInArray(dialogScope.channelTypes, 'channelTypeCode', dialogScope.OrderCostDetail.tourOperatorOrChannelTypeCode);

                                    qcApi.post('order/getExistsCurrency', {
                                        orderId: scope.orderUuid,
                                        type   : '2'
                                    }).success(function (result) {
                                        dialogScope.currencies = result.data;
                                        dialogScope.selectedCurrency = qcObjectInArray(dialogScope.currencies, 'currencyUuid', dialogScope.OrderCostDetail.currencyUuid);
                                    });
                                }
                                ],
                                width      : 500,
                                plain      : false
                            }
                        );
                    });
                };
                scope.deleteOrderCost = function (orderCost) {

                    qcDialog.openMessage({
                        msg : '是否删除成本',
                        type: 'confirm'
                    }).then(function () {
                        qcApi.post('order/deleteCostRecord', {
                            costUuid: orderCost.costUuid
                        }).success(function (result) {
                            getNewList();
                        });
                    });
                };
                scope.SubmitOrderCost = function (orderCost) {
                    qcApi.post('mtourfinance/submitCostRecord', {
                        costUuid: orderCost.costUuid
                    }).success(function (result) {
                        getNewList();
                        //qcMessage.tip('成本提交成功');
                    });
                };
                scope.batchSubmitOrderCost = function () {
                    var batchCost = [];
                    angular.forEach(scope.newList, function (orderCost) {
                        if (orderCost.batchChecked) {
                            batchCost.push({
                                costUuid : orderCost.costUuid,
                                orderUuid: orderCost.orderUuid
                            })
                        }
                    });
                    qcApi.post('mtourfinance/batchSubmitCostRecord', batchCost).success(function (result) {
                        getNewList();
                        //qcMessage.tip('成本批量提交成功');
                    });
                };
                scope.cancelOrderCost = function (orderCost) {

                    qcDialog.openMessage({
                        msg : '是否撤回成本',
                        type: 'confirm'
                    }).then(function () {
                        qcApi.post('mtourfinance/cancelCostRecord', {
                            orderUuid: orderCost.orderUuid,
                            costUuid : orderCost.costUuid
                        }).success(function (result) {
                            getNewList();
                        });
                    });
                }
                scope.currencies = commonValue.currencies;
                scope.invoiceOriginalTypes = fixedValue.invoiceOriginalTypes;
                scope.userInfo = $rootScope.userInfo;

            }
        };
    }]);
