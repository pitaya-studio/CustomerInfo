orderList.directive('orderCancel', ['urlConfig', 'qcApi', 'qcDialog', '$q', '$rootScope',
    function (urlConfig, qcApi, qcDialog, $q, $rootScope) {
        return {
            restrict: 'A',
            scope: {
                orderUuid: '='
            },
            link: function (scope, ele, attrs) {
                ele.on('click', function () {
                    var defer = $q.defer();
                    qcApi.post('order/cancelOrderValidate', {orderUuid: scope.orderUuid}).success(
                        function (result) {
                            if (result.data.cancelCode != 4) {
                                qcDialog.openMessage({
                                    msg: result.data.msg,
                                    type: 'confirm'
                                }).then(function () {
                                    defer.resolve(result.data);
                                });
                            }
                            else{
                                qcDialog.openMessage({
                                    msg: result.data.msg,
                                    type: 'detail'
                                }).then(function () {
                                });
                            }
                        }
                    );
                    defer.promise.then(function (data) {
                        qcApi.post('order/cancelOrder', {orderUuid: scope.orderUuid}).then(
                            function (result) {
                                //scope 的$emit 在订单列表中不能监控到,所以用了rootScope的$broadcast
                                $rootScope.$broadcast('order.cancel', scope.orderUuid);
                            }
                        );

                    });
                });
            }
        };
    }]);