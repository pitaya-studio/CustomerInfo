orderList.directive('pnrList', ['urlConfig', 'qcApi','qcDialog',function (urlConfig,qcApi,qcDialog) {
    return {
        restrict: 'A',
        scope: {
            orderUuid:'=',
            settlementLockStatus: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/pnr-list.html',
        link: function (scope, ele, attrs) {
            scope.lockStatus = scope.settlementLockStatus;//结算单锁定状态
            qcApi.post('order/showAirticketOrderPNRList',{ orderUuid:scope.orderUuid}).success(function (result) {
                scope.invoiceOriginalGroups=result.data;
            });
            scope.toggleSubTable = function () {
                scope.$emit('subTable.toggle.request','pnrList'+scope.orderUuid);
            };
            scope.initInvoiceOriginalGroup = function (invoiceOriginalGroup) {
                invoiceOriginalGroup.oldDrawerCount = invoiceOriginalGroup.drawerCount;
                invoiceOriginalGroup.oldReserveCount = invoiceOriginalGroup.reserveCount;
            };
            scope.cancelInvoiceOriginalGroup = function (invoiceOriginalGroup) {
                invoiceOriginalGroup.drawerCount = invoiceOriginalGroup.oldDrawerCount;
                invoiceOriginalGroup.reserveCount = invoiceOriginalGroup.oldReserveCount;
                invoiceOriginalGroup.edit=false;
            };
            scope.editInvoiceOriginalGroup = function (invoiceOriginalGroup) {
                invoiceOriginalGroup.edit=true;
            };
            scope.commitInvoiceOriginalGroup = function (invoiceOriginalGroup,invoiceOriginalScope) {
                if(!valid(invoiceOriginalGroup)){
                    qcDialog.openMessage({msg:'输入项有误,请重新确认'});
                    invoiceOriginalScope.$broadcast('qcValid.check');
                    return;
                }
                qcApi.post('order/modifyPNRGroupAddRecord',invoiceOriginalGroup).success(function (result) {
                    invoiceOriginalGroup.oldDrawerCount = invoiceOriginalGroup.drawerCount;
                    invoiceOriginalGroup.oldReserveCount = invoiceOriginalGroup.reserveCount;
                    invoiceOriginalGroup.edit=false;
                });
            };
            scope.showHistory= function (invoiceOriginalGroup) {
                alert('@todo');
            };
            function valid(invoiceOriginalGroup){
                if(angular.isEmpty( invoiceOriginalGroup.drawerCount) ){
                    return false;
                }
                if(angular.isEmpty( invoiceOriginalGroup.reserveCount)){
                    return false;
                }
                return true;
            }
        }
    };
}]);