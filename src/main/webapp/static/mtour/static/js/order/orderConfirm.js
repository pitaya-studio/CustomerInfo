orderList.directive('orderConfirm', ['urlConfig', 'qcApi', 'qcDialog', 'qcMessage','$q', '$rootScope',
    function (urlConfig, qcApi, qcDialog,qcMessage, $q, $rootScope) {
        return {
            restrict: 'A',
            scope: {
                orderUuid: '='
            },
            link: function (scope, ele, attrs) {
                ele.on('click', function () {
                    var defer = $q.defer();
                    qcApi.post('order/confirmOrder', {orderUuid: scope.orderUuid})
                        .success(function (result) {
                            qcMessage.tip('生成订单成功');
                            $rootScope.$broadcast('order.commit', scope.orderUuid);
                        }).error(function (data, status, headers, config) {
                    });
                });
            }
        };
    }]);
