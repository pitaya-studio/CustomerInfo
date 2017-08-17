var financeAgeList = angular.module('financeAgeList', ['mtour', 'finance']);

financeAgeList.controller('financeAgeController', [
    '$scope', '$timeout', 'fixedValue', 'commonValue', 'joinedStringByArray', 'joinChannelAndTourOperator',
    'qcApi', 'amountsToRMB', 'qcObjectInArray', 'joinedStringByArray',
    'joinAmount', 'joinDate', 'qcDialog', 'urlConfig', '$rootScope', 'checkRole', 'amountsToRMB',
    function ($scope, $timeout, fixedValue, commonValue, joinedStringByArray, joinChannelAndTourOperator,
              qcApi, amountsToRMB, qcObjectInArray, joinedStringByArray, joinAmount, joinDate,
              qcDialog, urlConfig, $rootScope, checkRole, amountsToRMB) {

        $scope.isRole = function (url) {
            return checkRole(url);
        };
        //换成签约渠道 和非签约渠道，而这个改换为未结清和已结清
        /*channelType                  : [
            {channelTypeCode: '1', channelTypeName: '签约渠道'},
            {channelTypeCode: '2', channelTypeName: '非签约渠道'}
        ],*/
        $scope.channelType=fixedValue.channelType;
        $scope.channelTypeSelf = $scope.channelType[0];




        //进来的小标签切换的标签值
        $scope.accountReceivableAgeStatusList = fixedValue.accountReceivableAgeStatus;
      /*  console.log($scope.accountReceivableAgeStatusList);
        console.log(fixedValue);*/
        $scope.accountReceivableAgeStatusList.unshift({
            accountReceivableAgeStatusCode: 'ALL', accountReceivableAgeStatusName: '全部'
        });
        $scope.selectedAccountReceivableAgeStatus = $scope.accountReceivableAgeStatusList[0];
        $scope.searchParam = {searchType: '1', searchKey: ''};
        $scope.filterParam = {
            accountReceivableAgeStatusList:[],
            selectedChannels: [],
            selectedReceiveAges: [],
            selectedSales   : []
        };
        $scope.pageInfo = {
            totalRowCount: 0,
            currentIndex : 1,
            rowCount     : 20
        };
        $scope.results = [];
        <!--modify by wlj at 2016.06.16-start-->
        $scope.payTab='3';
        $scope.sortKeys = [
            {
                name: '应收金额',
                code: 'receivableAmountRMB'
            },
            {
                name: '已收金额',
                code: 'receivedAmountRMB'
            },
            {
                name: '到账金额',
                code: 'arrivedAmountRMB'
            },
            {
                name: '未收金额',
                code: 'unreceiveAmountRMB'
            }
        ];
        $scope.sortInfo = {
            sortKey: 'receivableAmountRMB',
            dec    : true
        };

        $scope.isShowFilters = function () {
            return $scope.filterParam.selectedChannels.length
                || $scope.filterParam.selectedSales.length
                || $scope.filterParam.selectedReceiveAges.length;

        }


        //子表格
        $scope.spreadSubTableId;
        $scope.initOrderSpreadSubTableIs = function (age) {
            age.qcSubTableIds = [
                'channelOrder' + age.channelUuid
            ];
        };
        //$scope.spreadSubTables = {};
        $scope.toggleSubTable = function (qcSubTableId) {
            if ($scope.spreadSubTableId == qcSubTableId) {
                $scope.$broadcast('subTable.fold');
            }
            else {
                $scope.$broadcast('subTable.spread.ready', qcSubTableId);
            }
        };
        $scope.$on('subTable.spread.complete', function ($e, qcSubTableId) {
            $scope.spreadSubTableId = qcSubTableId;
        });
        $scope.$on('subTable.fold.complete', function ($e, qcSubTableId) {
            if ($scope.spreadSubTableId == qcSubTableId) {
                $scope.spreadSubTableId = undefined;
            }
        });
        $scope.$on('subTable.foldAll.request', function () {
            $scope.$broadcast('subTable.fold');
        })


        //主过滤条件变化
        $scope.$on('mainFilter.change', function ($e) {

            $scope.filterParam.selectedChannels.length = 0;
            $scope.filterParam.selectedReceiveAges.length = 0;
            $scope.filterParam.selectedSales.length = 0;

            $scope.$broadcast('mainSearch.init');
            $scope.searchParam = {searchType: '1', searchKey: ''};
            $scope.pageInfo.totalRowCount = 0;
            $scope.pageInfo.currentIndex = 1
            $scope.requestAge();
            //$scope.requestCount();
        });

        //点击主搜索框的查询按钮后
        $scope.$on('mainSearch.search', function ($e, searchParam) {
            $scope.searchParam = searchParam;
            $scope.filterParam.selectedChannels.length = 0;
            $scope.filterParam.selectedReceiveAges.length = 0;
            $scope.filterParam.selectedSales.length = 0;
            //
            $scope.pageInfo.totalRowCount = 0;
            $scope.pageInfo.currentIndex = 1
            $scope.requestAge();
            //$scope.requestCount();
        });


        //分页信息变化
        $scope.$on('pagination.change', function ($e, flag) {
            if (flag == 'backFirstPage') {
                $scope.requestAge('filter');
            } else {
                $scope.requestAge();
            }
        });
        //导出方式变化时
        $scope.$on('selectedExportTypes.change', function ($e, flag) {
        	//营收统计表
            if(flag == '1'){
            	window.open("../../../../static/mtour/html/finance/financeAgeDeposit.html");
            }
           //定金统计表
            else if(flag == '2'){
            	
            }
        });
        //排序规则变化后
        $scope.$on('sort.change', function () {
            $scope.requestAge();
        });
        //排序规则变化后
        $scope.$on('sort.change', function () {
            $scope.requestAge();
        });

        //过滤轨迹中删除 一个条件时
        $scope.$on('searchFilter.remove', function () {
            if (!$scope.isShowFilters()) {
                $scope.requestAge();
            }
        });

        //滤轨迹中清除所有条件时
        $scope.$on('searchFilter.clearAll', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
            $scope.requestAge();
        });

        //选中的渠道变化时
        $scope.$on('selectedChannels.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });
        //选中的账单完成状态变化时
        //选中的收款类别变化时
        $scope.$on('selectedReceiveAges.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        //选中的跟进销售变化时
        $scope.$on('selectedSales.change', function () {
            $rootScope.$broadcast('qcTableContainer.reset');
        });

        $scope.requestAge = function (flag) {
            $timeout(function () {
                var param = {
                    searchParam: $scope.searchParam,
                    filterParam: {
                        //accountReceivableAgeStatusCode: $scope.selectedAccountReceivableAgeStatus.accountReceivableAgeStatusCode == "ALL" ? "" : $scope.selectedAccountReceivableAgeStatus.accountReceivableAgeStatusCode,
                       // accountReceivableAgeStatusCode: $scope.selectedReceiveAges.accountReceivableAgeStatusCode,
                        channelTypeCode:$scope.channelTypeSelf.channelTypeCode,//签约与非签约
                        channels                      : joinedStringByArray($scope.filterParam.selectedChannels, 'channelUuid'),//具体的渠道
                        accountReceivableAgeStatusCode                : joinedStringByArray($scope.filterParam.selectedReceiveAges, 'accountReceivableAgeStatusCode'),//账款类型
                        //accountReceivableAgeStatusList: joinedStringByArray($scope.filterParam.selectedReceiveAges, 'accountReceivableAgeStatusCode'),
                        sales                         : joinedStringByArray($scope.filterParam.selectedSales, 'userId')//销售人员
                    },
                    sortInfo   : $scope.sortInfo,
                    pageParam  : $scope.pageInfo
                };
                if (flag == 'filter') {
                    param.pageParam.currentIndex = 1;
                }

                qcApi.post('mtourfinance/getAccountAgeList', param).success(function (result) {
                    // 0293 美途国际隐藏已收金额，在此查询公司编码不知是否妥当 ？  add by shijun.liu 2016.05.09
                    $scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                    $scope.results.length = 0;
                    $scope.results = result.data.results;
                    angular.forEach($scope.results, function (age) {
                        angular.forEach(age.receivableAmount, function (amount) {
                            //amount.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            //amount.currencyCode = amount.currency.currencyCode;
                            //amount.exchangeRate = amount.currency.exchangeRate;
                            amount.currencyCode = '¥';
                            amount.exchangeRate = 1;
                        });
                        age.receivableAmountRMB = amountsToRMB(age.receivableAmount);
                        angular.forEach(age.receivedAmount, function (amount) {
                            //amount.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            //amount.currencyCode = amount.currency.currencyCode;
                            //amount.exchangeRate = amount.currency.exchangeRate;
                            amount.currencyCode = '¥';
                            amount.exchangeRate = 1;
                        });
                        age.receivedAmountRMB = amountsToRMB(age.receivedAmount);
                        angular.forEach(age.unreceiveAmount, function (amount) {
                            //amount.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            //amount.currencyCode = amount.currency.currencyCode;
                            //amount.exchangeRate = amount.currency.exchangeRate;
                            amount.currencyCode = '¥';
                            amount.exchangeRate = 1;
                        });
                        age.unreceiveAmountRMB = amountsToRMB(age.unreceiveAmount);
                        angular.forEach(age.arrivedAmount, function (amount) {
                            //amount.currency = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid);
                            //amount.currencyCode = amount.currency.currencyCode;
                            //amount.exchangeRate = amount.currency.exchangeRate;
                            amount.currencyCode = '¥';
                            amount.exchangeRate = 1;
                        });
                        age.arrivedAmountRMB = amountsToRMB(age.arrivedAmount);
                    });
                    $rootScope.$broadcast('qcTableContainer.reset');

                    $scope.pageInfo.totalRowCount = result.data.page.totalRowCount;
                    $scope.pageInfo.currentIndex = result.data.page.currentIndex;
                    $scope.pageInfo.rowCount = +(result.data.page.rowCount);

                });
            });
        };
        //$scope.requestCount = function () {
        //    var param = {
        //        searchParam: $scope.searchParam
        //    };
        //    $timeout(function () {
        //        qcApi.post('common/countAllInfo', param).success(function (result) {
        //            $scope.searchCount = result.data;
        //        });
        //    });
        //};
        $scope.requestAge();
        //$scope.requestCount();

    }]);