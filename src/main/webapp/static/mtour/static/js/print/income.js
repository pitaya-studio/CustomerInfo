var income = angular.module('income', ['qc']);
income.controller('IncomeController', ['$scope', 'qcApi','qcObjectInArray', 'urlConfig',function ($scope, qcApi,qcObjectInArray,urlConfig) {
    var args = GetUrlParams();

    $scope.downloadUrl=urlConfig.mtourBaseUrl+'/mtour/mtourfinance/downloadIncomeSheet?receiveUuid='+args.receiveUuid+'&fundstype='+args.fundsType;
    qcApi.post('mtourfinance/showIncomeInfo',args).success(function (result) {
        $scope.data=result.data;
    });
}]);
