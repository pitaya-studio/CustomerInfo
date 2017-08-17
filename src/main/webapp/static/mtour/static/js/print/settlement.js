var settlement = angular.module('settlement', ['qc']);
settlement.controller('SettlementController', ['$scope', 'qcApi', 'qcObjectInArray', 'urlConfig', '$rootScope', function ($scope, qcApi, qcObjectInArray, urlConfig, $rootScope) {
    qcApi.post('common/getCurrentUserInfo').success(function (result) {
        $scope.companyRoleCode = result.data.companyRoleCode;
    });
    qcApi.post('common/getMenuByUser').success(function (result) {
        $scope.menus = result.data;
        angular.forEach($scope.menus, function (menu) {
            angular.forEach(menu.subMenus, function (subMenu) {
                angular.forEach(subMenu.roles, function (role) {
                    if (role.roleCode.indexOf('mtourFinance:pay:lockSettlement') != -1) {
                        $scope.lockBtnShow = 1;
                        return;
                    }
                });
            });
        })
        angular.forEach($scope.menus, function (menu) {
            angular.forEach(menu.subMenus, function (subMenu) {
                angular.forEach(subMenu.roles, function (role) {
                    if (role.roleCode.indexOf('mtourFinance:pay:unlockSettlement') != -1) {
                        $scope.unlockBtnShow = 1;
                        return;
                    }
                });
            });
        });
    });
    var args = GetUrlParams();
    $scope.downloadUrl = urlConfig.mtourBaseUrl + '/mtour/mtourfinance/downLoadSettlemnt?orderUuid=' + args.orderUuid;
    qcApi.post('mtourfinance/getSettlement', args).success(function (result) {
        $scope.data = result.data;
        console.log(result.data);
        //data.totalIncomeRmb=0;
        //angular.forEach(data.incomes, function (income) {
        //    income.currency = qcObjectInArray(fixedValue.currencies,'currencyUuid',income.currencyUuid);
        //    data.totalIncomeRmb =  data.totalIncomeRmb+(+income.rmb);
        //});
        //angular.forEach(data.costs, function (cost) {
        //    cost.currency = qcObjectInArray(fixedValue.currencies,'currencyUuid',cost.currencyUuid);
        //});
        //data.groupedIncome = groupAmount(data.incomes,'totalAmount');
        //data.groupedAdditionalCost =groupAmount(data.additionalCosts);
        //
        //var allCost=data.costs.concat(data.additionalCosts);
        //
        //data.allCostRmb = 0;
        //angular.forEach(allCost, function (cost) {
        //    data.allCostRmb =  data.allCostRmb +(+cost.rmb)
        //});
        //
        //data.groupedAllCost=groupAmount(allCost);
        //data.groupedRefund = groupAmount(data.refunds);
    });
    $scope.lockSettlementPage = function () {
        qcApi.post('mtourfinance/lockSettlement', {orderUuid: $scope.data.orderUuid}).success(function (result) {
            $scope.data.lockStatus = 1;
        });
    }
    $scope.unlockSettlementPage = function () {
        qcApi.post('mtourfinance/unlockSettlement', {orderUuid: $scope.data.orderUuid}).success(function (result) {
            $scope.data.lockStatus = 0;
        });
    }
}]);
