var settlement = angular.module('settlement', ['qc']);
settlement.controller('SettlementController', ['$scope', 'qcApi','qcObjectInArray', 'urlConfig',function ($scope, qcApi,qcObjectInArray,urlConfig) {
    var args = GetUrlParams();
    $scope.data={};
    $scope.downloadUrl=urlConfig.mtourBaseUrl+'/mtour/mtourfinance/downloadConfirmSheet?orderUuid='+args.orderUuid;
    qcApi.post('mtourfinance/getConfirmSheetData',args).success(function (result) {
        $scope.data=result.data;
    });

}]);
