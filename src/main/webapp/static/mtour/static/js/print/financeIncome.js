var income = angular.module('financeIncome', ['qc']);
income.controller('financeIncomeController', ['$scope', 'qcApi','qcObjectInArray', 'urlConfig',function ($scope, qcApi,qcObjectInArray,urlConfig) {
    var args = GetUrlParams();
    console.log(args.incomeType);
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();
    var initEndDate=year+"-"+month+"-"+day;
    var initStartDate=year+"-"+month+"-1";
    console.log(initStartDate+"~~~~~~");
    args.startDate=initStartDate;
    args.endDate=initEndDate;
    console.log("args.incomeType====="+args.incomeType);
    if(args.incomeType=="1"){
        $scope.downloadUrl=urlConfig.mtourBaseUrl+'/mtour/mtourfinance/downloadOperatingRevenue?incomeType='+args.incomeType+'&startDate='+initStartDate+'&endDate='+initEndDate;
        qcApi.post('mtourfinance/getOperatingRevenue',args).success(function (result) {
            $scope.data=result.data;
            $scope.items=result.data;
            $scope.startTime=initStartDate;
            $scope.endTime=initEndDate;
            $scope.incomeType=args.incomeType;
        });
    }else if(args.incomeType=="2"){
        $scope.downloadUrl=urlConfig.mtourBaseUrl+'/mtour/mtourfinance/downloadFrontMoneyStatExcel?incomeType='+args.incomeType+'&startDate='+initStartDate+'&endDate='+initEndDate;
        qcApi.post('mtourfinance/frontMoneyStat',args).success(function (result) {
            $scope.data=result.data;
            $scope.items=result.data;
            $scope.startTime=initStartDate;
            $scope.endTime=initEndDate;
            $scope.incomeType=args.incomeType;
        });
    }
    $scope.requestAgeIncome = function (flag) {
            var param = {
            };
            param.startDate=$("#groupOpenDate").val();
            param.endDate=$("#groupCloseDate").val();
            if (flag == '1') {
                $scope.downloadUrl=urlConfig.mtourBaseUrl+'/mtour/mtourfinance/downloadOperatingRevenue?incomeType='+flag+'&startDate='+param.startDate+'&endDate='+param.endDate;
                qcApi.post('mtourfinance/getOperatingRevenue', param).success(function (result) {
                    // 0293 美途国际隐藏已收金额，在此查询公司编码不知是否妥当 ？  add by shijun.liu 2016.05.09
                    $scope.data=result.data;
                    $scope.items=result.data;
                    $scope.startTime=param.startDate;
                    $scope.endTime=param.endDate;
                    $scope.incomeType=flag;
                    //$rootScope.$broadcast('qcTableContainer.reset');
                });
            }else if(flag == '2'){
                $scope.downloadUrl=urlConfig.mtourBaseUrl+'/mtour/mtourfinance/downloadFrontMoneyStatExcel?incomeType='+flag+'&startDate='+param.startDate+'&endDate='+param.endDate;
                qcApi.post('mtourfinance/frontMoneyStat', param).success(function (result) {
                    // 0293 美途国际隐藏已收金额，在此查询公司编码不知是否妥当 ？  add by shijun.liu 2016.05.09
                    $scope.data=result.data;
                    $scope.items=result.data;
                    $scope.startTime=param.startDate;
                    $scope.endTime=param.endDate;
                    $scope.incomeType=flag;
                    //$rootScope.$broadcast('qcTableContainer.reset');
                });

            }
    }


}]);
