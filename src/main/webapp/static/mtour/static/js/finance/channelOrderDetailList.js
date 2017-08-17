financeAgeList.directive('channelOrderDetailList', ['$rootScope', 'urlConfig', 'fixedValue', 'commonValue', 'qcObjectInArray', 'qcDialog', 'qcApi','$rootScope',
    function ($rootScope, urlConfig, fixedValue, commonValue, qcObjectInArray, qcDialog, qcApi,$rootScope) {
        return {
            restrict: 'A',
            scope: {
                channelUuid: '='
            },
            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/channelOrderDetailList.html',
            link: function (scope, ele, attrs) {
                scope.currencies = commonValue.currencies;
                scope.userInfo = $rootScope.userInfo;
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                qcApi.post('mtourfinance/getOrderDetail', {
                    channelUuid: scope.channelUuid
                }).success(function (result) {
                    scope.newList = result.data;
                    angular.forEach(scope.newList, function (orderDetail) {
                        angular.forEach(orderDetail.orderAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = '¥';
                        });
                        angular.forEach(orderDetail.receivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = '¥';
                        });
                        angular.forEach(orderDetail.arrivedAmount, function (amount) {
                            var currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            amount.currencyCode = '¥';
                        });
                        var operators="操作:";
                        angular.forEach(orderDetail.operator,function(operator){
                        	operators += operator+"、";
                        });
                        var len = operators.length-1;
                        scope.operators = operators.substring(0,len);
                    });
                });
                scope.$on('ngRepeatFinished', function ($e) {
                    $rootScope.$broadcast('qcTableContainer.reset');
                })
            }
        };
    }]);
financeAgeList.directive('onFinishRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit('ngRepeatFinished');
                });
            }
        }
    }
});