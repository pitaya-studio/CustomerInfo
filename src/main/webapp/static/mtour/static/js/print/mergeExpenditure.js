var mergeExpenditure = angular.module('mergeExpenditure', ['qc']);
mergeExpenditure.controller('mergeExpenditureController', ['$scope', 'qcApi', 'urlConfig', '$rootScope', function ($scope, qcApi, urlConfig) {
    var args = GetUrlParams();
    qcApi.post('common/getCurrentUserInfo').success(function (result) {
        $scope.userInfo = result.data;
        $scope.companyRoleCode = $scope.userInfo.companyRoleCode;
    });
//    $scope.companyRoleCode='1';
    $scope.downloadUrl = urlConfig.mtourBaseUrl + '/mtour/mtourfinance/downloadMtourMergePaySheet?mergeParam=' + args.mergeParam;
    qcApi.post('mtourfinance/mergePaymentDoc', JSON.parse(args.mergeParam)).success(function (result) {
        $scope.data = result.data;
        //var allPeople = '';
        //angular.forEach($scope.data.paymentPeople, function (person) {
        //    allPeople += person.name + ' ';
        //})
        //$scope.allPaymentPeople =allPeople;
        if ($scope.data.paymentPeople.length > 12) {
            $scope.data.paymentPeople = $scope.data.paymentPeople.substr(0, 12);
        }
    });
}]);
