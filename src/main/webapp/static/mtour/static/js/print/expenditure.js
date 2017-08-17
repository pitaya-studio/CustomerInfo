var expenditure = angular.module('expenditure', ['qc']);
expenditure.controller('ExpenditureController', ['$scope', 'qcApi', 'urlConfig', '$rootScope', function ($scope, qcApi, urlConfig) {
    var args = GetUrlParams();
    qcApi.post('common/getCurrentUserInfo').success(function (result) {
        $scope.userInfo = result.data;
        $scope.companyRoleCode = $scope.userInfo.companyRoleCode;
    });
//    $scope.companyRoleCode='1';
    $scope.downloadUrl = urlConfig.mtourBaseUrl + '/mtour/mtourfinance/downloadMtourPaySheet?paymentUuid=' + args.paymentUuid + '&fundsType=' + args.fundsType;
    qcApi.post('mtourfinance/getMtourPaySheet', args).success(function (result) {
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
    //$scope.data = {
    //    paymentUuid: '付款Uuid',
    //    applicantDate: '日期',
    //    groupNo: '团号',
    //    invoiceOriginalTypeCode: 'PNR或者地接社的的代码',//大编号(票源类型)
    //    PNR: 'PNR编号',//大编号为PNR的时候才有意义
    //    tourOperatorName: '地接社Name',//大编号为地接社的时候才有意义
    //    tourOperatorOrChannelName: '支付对象名称',
    //    purpose: '用途',
    //    totalRMB: '计人民币',
    //    totalRMB_CN: '计人民币(大写)',
    //    applicant: '申请人'
    //}
}]);
