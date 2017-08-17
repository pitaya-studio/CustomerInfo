var orderList = angular.module('orderList', ['mtour', 'qc.uploader']);

orderList.controller('OrderListController', [
    '$scope', '$timeout', 'fixedValue', 'commonValue', 'joinedStringByArray', 'qcApi', 'amountsToRMB', 'qcObjectInArray', '$rootScope', 'orderReceivePop',
    function ($scope, $timeout, fixedValue, commonValue, joinedStringByArray, qcApi, amountsToRMB, qcObjectInArray, $rootScope, orderReceivePop) {
        var _companyRoleCode="";
        $scope.$on('userinfo.loaded', function () {
            _companyRoleCode = $rootScope.userInfo.companyRoleCode;
            if ($scope.companyRoleCode == '0') {
            }
        });
        $scope.companyRoleCode =_companyRoleCode;
        $scope.orderStatusList = fixedValue.orderStatus;
     /*   console.log("_companyRoleCode======1==="+_companyRoleCode);
        if(_companyRoleCode=='1'){
            $scope.orderStatusList.shift();
        }*/
        $scope.orderStatusList.unshift({
            orderStatusCode: 'ALL', orderStatusName: '全部'
        });
        $scope.selectedOrderStatus = $scope.orderStatusList[0];
        $scope.manyToDown={};


        $scope.searchParam = {searchType: '1', searchKey: ''};
        $scope.filterParam = {
            selectedChannels          : [],
            selectedOrderers          : [],
            selectedReceiveOrderStatus: [],
            selectedCreateDates       : []
        };
        $scope.pageInfo = {
            totalRowCount: 0,
            currentIndex : 1,
            rowCount     : 20
        };
        $scope.results = [];
        $scope.payTab='4';//订单列表
        $scope.sortKeys = [
            {
                name: '创建时间',
                code: 'orderDateTime'
            },
            {
                name: '更新时间',
                code: 'modifiedDateTime'
            },
            {
                name: '出团日期',
                code: 'departureDate'
            }
        ];
        $scope.sortInfo = {
            sortKey: 'orderDateTime',
            dec    : true
        };

        $scope.isShowFilters = function () {
            return $scope.filterParam.selectedChannels.length
                || $scope.filterParam.selectedOrderers.length
                || $scope.filterParam.selectedReceiveOrderStatus.length
                || $scope.filterParam.selectedCreateDates.length;
        }

        //保存订单后
        $rootScope.$on('order.save', function () {
            $scope.requestOrder();
        });

        //子表格
        $scope.spreadSubTableId;
        $scope.initOrderSpreadSubTableIs = function (order) {
            order.qcSubTableIds = [
                'pnrList' + order.orderUuid,//展开pnr
                'refund' + order.orderUuid,//退款
                'refundList' + order.orderUuid,//退款记录
                'loan' + order.orderUuid,//退款
                'loanList' + order.orderUuid,//退款记录
                'orderReceiveList' + order.orderUuid,//订单收款记录
                'otherRevenueList' + order.orderUuid,//其他收入记录
                'orderCostList' + order.orderUuid,//成本记录
                'additionalCost' + order.orderUuid,//追加成本
                'additionalCostList' + order.orderUuid//追加成本记录
            ];
        };
        //$scope.spreadSubTables = {};
        $scope.$on('subTable.toggle.request', function ($e, qcSubTableId) {
            if ($scope.spreadSubTableId == qcSubTableId) {
                $scope.$broadcast('subTable.fold');
            }
            else {
                $scope.$broadcast('subTable.spread.ready', qcSubTableId);
            }
        });
        $scope.$on('subTable.spread.complete', function ($e, qcSubTableId) {
            $scope.spreadSubTableId = qcSubTableId;
        });
        $scope.$on('subTable.fold.complete', function ($e, qcSubTableId) {
            if ($scope.spreadSubTableId == qcSubTableId) {
                $scope.spreadSubTableId = undefined;
            }
        });
        $scope.$on('subTable.foldAll.request', function () {
            $scope.$broadcast('subTable.fold');
        })

        //下单日期选择
        $scope.$on('selectedCreateDate.change', function () {
            $scope.filterParam.selectedCreateDates = [
                {
                    startDate: $scope.selectedCreateDate.startDate,
                    endDate  : $scope.selectedCreateDate.endDate
                }];
            $scope.$broadcast('selectedCreateDate.clear');
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //主过滤条件变化
        $scope.$on('mainFilter.change', function ($e, orderStatus) {
            $scope.filterParam.selectedChannels.length = 0;
            $scope.filterParam.selectedOrderers.length = 0;
            $scope.filterParam.selectedReceiveOrderStatus.length = 0;
            $scope.filterParam.selectedCreateDates.length = 0;
            $scope.$broadcast('mainSearch.init');
            $scope.searchParam = {searchType: '1', searchKey: ''};
            $scope.pageInfo.totalRowCount = 0;
            $scope.pageInfo.currentIndex = 1
            $scope.requestOrder();
        });

        //点击主搜索框的查询按钮后
        $scope.$on('mainSearch.search', function ($e, searchParam) {
            $scope.searchParam = searchParam;
            $scope.filterParam.selectedChannels.length = 0;
            $scope.filterParam.selectedOrderers.length = 0;
            $scope.filterParam.selectedReceiveOrderStatus.length = 0;
            $scope.filterParam.selectedCreateDates.length = 0;
            $scope.pageInfo.totalRowCount = 0;
            $scope.pageInfo.currentIndex = 1
            $scope.requestOrder();
        });

        //分页信息变化
        $scope.$on('pagination.change', function ($e, flag) {
            if (flag == 'backFirstPage') {
                $scope.filterOrder('filter');
            }
            else {
                $scope.filterOrder();
            }
        });

        //排序规则变化后
        $scope.$on('sort.change', function () {
            $scope.filterOrder();
        });


        //过滤轨迹中删除 一个条件时
        $scope.$on('searchFilter.remove', function () {
            if (!$scope.isShowFilters()) {
                $scope.filterOrder();
            }
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //滤轨迹中清除所有条件时
        $scope.$on('searchFilter.clearAll', function () {
            $scope.filterOrder();
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的渠道变化
        $scope.$on('selectedChannels.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //选中的下单人变化
        $scope.$on('selectedOrderers.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的收款状态变化
        $scope.$on('selectedReceiveOrderStatus.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });


        //取消订单后
        $scope.$on('order.cancel', function ($e, orderUuid) {
            $scope.requestOrder();
        });
        //生成订单后
        $rootScope.$on('order.commit', function () {
            $scope.requestOrder();
        });
        //生成订单并收款后
        $rootScope.$on('order.commitAndReceive', function ($e, orderUuid) {
            orderReceivePop(orderUuid);
            $scope.requestOrder();
        });
        //订单追散后
        $rootScope.$on('order.addInvoiceOriginalGroupButton', function ($e, orderUuid) {
            $scope.requestOrder('pnrList' + orderUuid);
        });
        //成本录入成功后展开录入列表
        $scope.$on('order.orderCost.save', function ($e, orderUuid) {
            $scope.requestOrder('orderCostList' + orderUuid);
        });
        //订单收款成功后展开录入列表
        $scope.$on('order.orderReceive.save', function ($e, orderUuid) {
            $scope.requestOrder('orderReceiveList' + orderUuid);
        });
        //订单收款成功后展开录入列表
        $scope.$on('order.orderReceive.cancel', function ($e, orderUuid) {
            $scope.requestOrder('orderReceiveList' + orderUuid);
        });

        //其他收入录入成功/修改后展开录入列表
        $scope.$on('order.otherRevenue.save', function ($e, orderUuid) {
            $scope.requestOrder('otherRevenueList' + orderUuid);
        });

        //其他收入取消后刷新订单列表和其他收列表
        $scope.$on('order.otherRevenue.cancel', function ($e, orderUuid) {
            $scope.requestOrder('otherRevenueList' + orderUuid);
        });


        //其他收入删除后刷新订单列表和其他收列表
        $scope.$on('order.otherRevenue.delete', function ($e, orderUuid) {
            $scope.requestOrder('otherRevenueList' + orderUuid);
        });

        //其他收入提交后刷新订单列表和其他收列表
        $scope.$on('order.otherRevenue.commit', function ($e, orderUuid) {
            $scope.requestOrder('otherRevenueList' + orderUuid);
        });

        function joinDate(dates) {
            var dateStrings = [];
            angular.forEach(dates, function (date) {
                var string = '';
                if (date.startDate) {
                    string += date.startDate;
                }
                string += '~';
                if (date.endDate) {
                    string += date.endDate;
                }
                dateStrings.push(string);
            });
            return dateStrings.join(',');
        }

        $scope.requestOrder = function (spreadSubTableId) {
            $timeout(function () {
                //是否是美途用户
                //qcApi.post('order/showAirticketOrderDetail', {
                //    //orderUuid: scope.orderUuid
                //}).success(function (result) {
                //});
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        channelUuid      : joinedStringByArray($scope.filterParam.selectedChannels, 'channelUuid'),
                        ordererId        : joinedStringByArray($scope.filterParam.selectedOrderers, 'userId'),
                        orderStatusCode  : $scope.selectedOrderStatus.orderStatusCode == "ALL" ? "" : $scope.selectedOrderStatus.orderStatusCode,
                        receiveStatusCode: joinedStringByArray($scope.filterParam.selectedReceiveOrderStatus, 'receiveStatusCode'),
                        orderDateTime    : joinDate($scope.filterParam.selectedCreateDates)
                    },
                    sortInfo   : $scope.sortInfo,
                    pageParam  : $scope.pageInfo
                };
                $rootScope.$broadcast('qcTableContainer.reset');
                qcApi.post('order/getOrderList', param).success(function (result) {
                    //$scope.results.length=0;
                    $scope.results = result.data.results;
                    angular.forEach($scope.results, function (order) {

                        angular.forEach(order.deposit, function (amount) {
                            amount.currencyCode = '¥';
                        });
                        angular.forEach(order.fullPayment, function (amount) {
                            amount.currencyCode = '¥';
                        });
                        angular.forEach(order.balancePayment, function (amount) {
                            amount.currencyCode = '¥';
                        });
                        angular.forEach(order.receivedAmount, function (amount) {
                            amount.currencyCode = '¥';
                        });
                        angular.forEach(order.arrivedAmount, function (amount) {
                            amount.currencyCode = '¥';
                        });
                        order.depositRMB = amountsToRMB(order.deposit);
                        order.fullPaymentRMB = amountsToRMB(order.fullPayment);
                        order.balancePaymentRMB = amountsToRMB(order.balancePayment);
                        order.receivedAmountRMB = amountsToRMB(order.receivedAmount);
                        order.arrivedAmountRMB = amountsToRMB(order.arrivedAmount);
                        order.checkFlag = false;

                    });
                    $scope.pageInfo.totalRowCount = result.data.page.totalRowCount;
                    $scope.pageInfo.currentIndex = result.data.page.currentIndex;
                    $scope.pageInfo.rowCount = result.data.page.rowCount;
                    if (spreadSubTableId) {
                        $timeout(function () {
                            $scope.$broadcast('subTable.spread.ready', spreadSubTableId);
                        }, 500);

                    }
                });
                //qcApi.post('common/countAllInfo', param).success(function (result) {
                //    $scope.searchCount = result.data;
                //});
            });
        };
        Array.prototype.remove = function (item) {
            var index = this.indexOf(item);
            if (index === -1) {
                return false;
            }
            for (var i = 0, n = 0; i < this.length; i++) {
                if (this[i] !== this[index]) {
                    this[n++] = this[i];
                }
            }
            this.length -= 1;
            return true;
        };
        $scope.aloneBtn=function(c,v){
            var _flag= c.currentTarget.checked;
            var  order=v;
            if (_flag == true) {
                order.checkFlag = true;
                //将order的id类似值存入备用
                //$scope.manyToDown.push(order.orderUuid);
                $scope.manyToDown[order.orderUuid]="1";
            } else {
                $scope.manyToDown[order.orderUuid]="0";
                //$scope.manyToDown.remove($scope.manyToDown.indexOf(order.orderUuid));
                order.checkFlag = false;
            }
        }
        $scope.allBtn= function (c,v) {//全选
            var _flag= c.currentTarget.checked;
            angular.forEach($scope.results, function (order) {
                if (_flag == true) {                   
                	order.checkFlag = true;
                    //将order的id类似值存入备用
                    //$scope.manyToDown.push(order.orderUuid);
                    $scope.manyToDown[order.orderUuid]="1";
                } else {
                    $scope.manyToDown[order.orderUuid]="0";
                    //$scope.manyToDown.remove($scope.manyToDown.indexOf(order.orderUuid));
                	order.checkFlag = false;
                }
            })
        };
        $scope.filterOrder = function (flag) {
            $timeout(function () {
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        channelUuid      : joinedStringByArray($scope.filterParam.selectedChannels, 'channelUuid'),
                        ordererId        : joinedStringByArray($scope.filterParam.selectedOrderers, 'userId'),
                        orderStatusCode  : $scope.selectedOrderStatus.orderStatusCode == "ALL" ? "" : $scope.selectedOrderStatus.orderStatusCode,
                        receiveStatusCode: joinedStringByArray($scope.filterParam.selectedReceiveOrderStatus, 'receiveStatusCode'),
                        orderDateTime    : joinDate($scope.filterParam.selectedCreateDates)
                    },
                    sortInfo   : $scope.sortInfo,
                    pageParam  : $scope.pageInfo
                };
                if (flag == 'filter') {
                    param.pageParam.currentIndex = 1;
                }
                $rootScope.$broadcast('qcTableContainer.reset');
                qcApi.post('order/getOrderList', param).success(function (result) {
                    //$scope.results.length=0;
                    //console.log(result);
                    $scope.results = result.data.results;
                    angular.forEach($scope.results, function (order) {

                        angular.forEach(order.deposit, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = currency.currencyCode;
                            amount.exchangeRate = currency.exchangeRate;
                        });
                        angular.forEach(order.fullPayment, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = currency.currencyCode;
                            amount.exchangeRate = currency.exchangeRate;
                        });
                        angular.forEach(order.balancePayment, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = currency.currencyCode;
                            amount.exchangeRate = currency.exchangeRate;
                        });
                        angular.forEach(order.receivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = currency.currencyCode;
                            amount.exchangeRate = currency.exchangeRate;
                        });
                        angular.forEach(order.arrivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = currency.currencyCode;
                            amount.exchangeRate = currency.exchangeRate;
                        });
                        order.depositRMB = amountsToRMB(order.deposit);
                        order.fullPaymentRMB = amountsToRMB(order.fullPayment);
                        order.balancePaymentRMB = amountsToRMB(order.balancePayment);
                        order.receivedAmountRMB = amountsToRMB(order.receivedAmount);
                        order.arrivedAmountRMB = amountsToRMB(order.arrivedAmount);

                    });
                    $scope.pageInfo.totalRowCount = result.data.page.totalRowCount;
                    $scope.pageInfo.currentIndex = result.data.page.currentIndex;
                });
            });
        }
        $scope.requestOrder();
    }]);


orderList.directive('searchConditionImportBatch', ['urlConfig', 'qcApi', 'fixedValue', '$rootScope', function (urlConfig, qcApi, fixedValue, $rootScope) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/search-condition-import-batch.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            var fundsType = angular.copy(fixedValue.loadBath);
            scope.fundsTypes = fundsType;
            scope.$on('userinfo.loaded', function () {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                if (scope.companyRoleCode == '0') {
                    scope.fundsTypes.splice(0, 1);
                }
            });

            scope.selectedExportTypes = ngModel.$modelValue;
        }
    };
}]);


orderList.directive('searchConditionChannel', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/search-condition-channel.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.selectedChannels = ngModel.$modelValue;
            scope.$on('selectedChannels.search', function ($e, filterText) {
                qcApi.post('common/getAgentinfoByTypeCode', {
                    channelTypeCode: '1',//@todo 修改为空
                    channelName    : filterText,//模糊搜索
                    count          : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.channels = result.data;
                });
            });
        }
    };
}]);


orderList.directive('searchConditionOrderer', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/search-condition-orderer.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrderers = modelValue;
            //});
            scope.selectedOrderers = ngModel.$modelValue;
            scope.$on('selectedOrderers.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.orderers = result.data;
                });

            });
        }
    };
}]);


orderList.directive('searchConditionOrderStatus', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/search-condition-orderStatus.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.orderStatus = fixedValue.orderStatus;
            scope.selectedOrderStatus = ngModel.$modelValue;
        }
    };
}]);

orderList.directive('searchConditionOrderReceiveStatus', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/search-condition-orderReceiveStatus.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.orderReceiveStatus = fixedValue.orderReceiveStatus;
            scope.selectedReceiveOrderStatus = ngModel.$modelValue;
        }
    };
}]);

orderList.directive('rowOperator', ['urlConfig', 'qcApi', 'qcDialog', 'commonValue', 'qcObjectInArray', 'fixedValue', 'checkRole', '$rootScope',
    function (urlConfig, qcApi, qcDialog, commonValue, qcObjectInArray, fixedValue, checkRole, $rootScope) {
        return {
            restrict   : 'A',
            scope      : {
                order           : '=',
                spreadSubTableId: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/row-operator.html',
            link       : function (scope, ele, attrs) {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                scope.toggleSubTable = function (qcSubTableId, $e) {
                    scope.$emit('subTable.toggle.request', qcSubTableId);
                };
                scope.isRole = function (url) {
                    return checkRole(url);
                };
            }
        };
    }]);

orderList.factory('joinDate', function () {
    return function (dates) {
        var dateStrings = [];
        angular.forEach(dates, function (date) {
            var string = '';
            if (date.startDate) {
                string += date.startDate;
            }
            string += '~';
            if (date.endDate) {
                string += date.endDate;
            }
            dateStrings.push(string);
        });
        return dateStrings.join(',');
    }
});