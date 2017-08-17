angular.isEmpty = function (value) {
    if (angular.isUndefined(value) || value === '') {
        return true;
    } else {
        return false;
    }
};
var mtour = angular.module('mtour', ['qc']);

mtour.constant('fixedValue', {
    searchTypes                  : [
        {searchType: '1', searchTypeName: '团号'},
        {searchType: '3', searchTypeName: '产品名称'},
        {searchType: '4', searchTypeName: '渠道名称'},
        {searchType: '5', searchTypeName: '下单人'},
        {searchType: '6', searchTypeName: 'PNR'},
        {searchType: '7', searchTypeName: '航空公司'},
        {searchType: '8', searchTypeName: '航段名称'}
    ],
    tourOperatorChannelCategories: [
        {
            tourOperatorChannelCategoryCode: '1',
            tourOperatorChannelCategoryName: '地接社'
        },
        {
            tourOperatorChannelCategoryCode: '2',
            tourOperatorChannelCategoryName: '渠道商'
        }
    ],
    receiveType                  : [
        {receiveTypeCode: 1, receiveTypeName: '定金'},
        {receiveTypeCode: 2, receiveTypeName: '尾款'},
        {receiveTypeCode: 3, receiveTypeName: '全款'},
        {receiveTypeCode: 4, receiveTypeName: '追散'},
        {receiveTypeCode: 5, receiveTypeName: '其他收入'}
    ],
    orderReceiveType             : [//订单收款的时候没有其他收入项
        {receiveTypeCode: 1, receiveTypeName: '定金'},
        {receiveTypeCode: 2, receiveTypeName: '尾款'},
        {receiveTypeCode: 3, receiveTypeName: '全款'},
        {receiveTypeCode: 4, receiveTypeName: '追散'}
    ],
    paymentMethods               : [
        {
            paymentMethodName: '支票',
            paymentMethodCode: '1'
        },
        {
            paymentMethodName: '现金',
            paymentMethodCode: '3'
        },
        {
            paymentMethodName: '汇款',
            paymentMethodCode: '4'
        }
    ],
    <!--modify by wlj at 2016.06.16-start-->
    orderStatus                  : [
        {orderStatusCode: '4', orderStatusName: '待确认'},
        {orderStatusCode: '0', orderStatusName: '已生成'},
        {orderStatusCode: '2', orderStatusName: '已取消'}
        //,{orderStatusCode: '3', orderStatusName: '草稿'}
    ],
    orderReceiveStatus           : [
        {receiveStatusCode: 101, receiveStatusName: '待收款'},
        {receiveStatusCode: 102, receiveStatusName: '部分定金'},
        {receiveStatusCode: 103, receiveStatusName: '已收定金'},
        {receiveStatusCode: 104, receiveStatusName: '已收全款'}
    ],
    financeReceiveStatus         : [
        {receiveStatusCode: '99', receiveStatusName: '待确认'},
        {receiveStatusCode: '1', receiveStatusName: '已确认'},
        {receiveStatusCode: '2', receiveStatusName: '已驳回'},
        {receiveStatusCode: '0', receiveStatusName: '已撤销'}
    ],
    financePaymentStatus         : [
        {paymentStatusCode: '0', paymentStatusName: '待付款'},
        {paymentStatusCode: '1', paymentStatusName: '已付款'}
    ],
    financePaymentRecordStatus   : [
        {financePaymentRecordStatusCode: '0', financePaymentRecordStatusName: '已付款'},
        {financePaymentRecordStatusCode: '1', financePaymentRecordStatusName: '已撤销'}
    ],
    fundsType                    : [
        {fundsType: '2', fundsTypeName: '退款'},
        {fundsType: '3', fundsTypeName: '追加成本'},
        {fundsType: '4', fundsTypeName: '成本'}
    ],
    receiveFundsType             : [
        {receiveFundsTypeCode: '1', receiveFundsTypeName: '订单收款'},
        {receiveFundsTypeCode: '2', receiveFundsTypeName: '其他收入收款'}
    ],
    channelType                  : [
        {channelTypeCode: '1', channelTypeName: '签约渠道'},
        {channelTypeCode: '2', channelTypeName: '非签约渠道'}
    ],
    printStatus                  : [
        {printStatusCode: '1', printStatusName: '未打印'},
        {printStatusCode: '2', printStatusName: '已打印'}
    ],
    invoiceOriginalTypes         : [
        {invoiceOriginalTypeCode: '0', invoiceOriginalTypeName: 'PNR'},
        {invoiceOriginalTypeCode: '1', invoiceOriginalTypeName: '地接社'}
    ],
    accountCompleteStatus        : [
        {
            accountCompleteStatusCode: 99,
            accountCompleteStatusName: '待确认'
        },
        {
            accountCompleteStatusCode: 0,
            accountCompleteStatusName: '已撤销'
        },
        {
            accountCompleteStatusCode: 1,
            accountCompleteStatusName: '已确认'
        },
        {
            accountCompleteStatusCode: 2,
            accountCompleteStatusName: '已驳回'
        }
    ],
    accountReceivableAgeStatus   : [
        {
            accountReceivableAgeStatusName: '未结清',
            accountReceivableAgeStatusCode: '0'
        },
        {
            accountReceivableAgeStatusName: '已结清',
            accountReceivableAgeStatusCode: '1'
        }
    ],
    BTypeStatus                  : [
        {BTypeStatusCode: '2', BTypeStatusName: '待提交'},
        {BTypeStatusCode: '3', BTypeStatusName: '已提交'},
        {BTypeStatusCode: '1', BTypeStatusName: '已付款'}
    ],
    //90需求
    financePayOrderListPayStatus : [
        {financePayOrderListPayStatusCode: '0', financePayOrderListPayStatusName: '未付全款'},
        {financePayOrderListPayStatusCode: '1', financePayOrderListPayStatusName: '已付全款'}
    ],
    paymentApplyStatus           : [
        {statusCode: '0', statusName: '已撤销'},
        {statusCode: '1', statusName: '已提交'},
        {statusCode: '2', statusName: '已付款'}
    ],
    //<!--modify by wlj at 2016.06.16-start-->
    exportTable                    : [
        {exportType: '1', exportTypeName: '营收统计表'},
        {exportType: '2', exportTypeName: '订金统计表'},
    ],
    loadBath                    : [
      {loadBathType: '1', loadBathName: '支出单'},
      {loadBathType: '2', loadBathName: '收入单'},
      {loadBathType: '3', loadBathName: '结算单'},
   ],
    /*modify for mtour 0580 by wlj at 20170322 -start */
    lineType                    :[
        {lineTypeName:"国家",lineTypeId:"1"},
        {lineTypeName:"洲际",lineTypeId:"2"}
    ]
    /*modify for mtour 0580 by wlj at 20170322 -start */

});

mtour.value('commonValue', {
    sexes            : {},
    travelerTypes    : [],
    visaTypes        : [],
    credentialsTypes : [],
    tourOperatorTypes: [],
    currencies       : []

});

mtour.factory('joinedStringByArray', function () {
    return function (array, key) {
        var keyArr = [];
        angular.forEach(array, function (item) {
            if (key) {
                keyArr.push(item[key]);
            } else {
                keyArr.push(item);
            }
        });
        return keyArr.join(',');
    };
});
mtour.factory('amountsToRMB', ['$filter', function ($filter) {
    return function (amounts) {
        var index = 0;
        if (!amounts) {
            return amounts;
        }
        var countRMB = 0;
        while (amounts[index]) {
            var amount = amounts[index];
            countRMB += amount.amount * (amount.exchangeRate ? amount.exchangeRate : amount.exchangerate);
            index++;
        }

        return countRMB;
    };
}]);

mtour.factory('groupAmount', ['qcObjectInArray', 'commonValue', function (qcObjectInArray, commonValue) {
    return function (amounts, amountKey) {
        //var groupAmounts = [];
        //angular.forEach(amounts, function (amount) {
        //    var groupAmount = qcObjectInArray(groupAmounts, 'currencyUuid', amount.currencyUuid);
        //    if (groupAmount) {
        //        groupAmount.amount = groupAmount.amount.floatAdd(amount.amount);
        //    } else {
        //        groupAmounts.push(amount);
        //    }
        //});

        var index = 0;
        if (!amounts) {
            return amounts;
        }
        if (!amountKey) {
            amountKey = 'amount';
        }
        var groupedAmounts = [];

        while (amounts[index]) {
            var amount = amounts[index];
            var groupedAmount = qcObjectInArray(groupedAmounts, 'currencyUuid', amount.currencyUuid);
            //if (groupedAmount) {
            //    groupedAmount[amountKey] = (+groupedAmount[amountKey]).floatMul(+groupedAmount.exchangeRate) + (+amount[amountKey]).floatMul(+amount.exchangeRate);
            //    groupedAmount.currencyUuid = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).currencyUuid;
            //    groupedAmount.exchangeRate = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).exchangeRate;
            //    groupedAmount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', amount.currencyUuid).currencyCode;
            //} else {
            groupedAmount = {
                amount      : amount[amountKey],
                currencyCode: amount.currencyCode,
                currencyUuid: amount.currencyUuid,
                exchangeRate: amount.exchangeRate ? amount.exchangeRate : amount.exchangerate
            };
            if (!groupedAmount.currencyCode) {
                groupedAmount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', groupedAmount.currencyUuid).currencyCode;
                groupedAmount[amountKey] = (+amount[amountKey] );
            }
            groupedAmounts.push(groupedAmount);
            //}
            //groupedAmount.currencyUuid = 108;
            //groupedAmount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', 108).currencyCode;
            //groupedAmount.exchangeRate = qcObjectInArray(commonValue.currencies, 'currencyUuid', 108).exchangeRate;

            index++;
        }

        return groupedAmounts;
    };
}]);


mtour.factory('checkRole', ['$rootScope', 'qcObjectInArray', function ($rootScope, qcObjectInArray) {
    return function (roleUrl, roles) {
        if (!roles) {
            roles = $rootScope.pageRoles;
        }
        if (qcObjectInArray(roles, 'roleCode', roleUrl)) {
            return true;
        }
        return false;
    }
}]);

mtour.directive('userInfo', ['$rootScope', 'urlConfig', function ($rootScope, urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            showCreateOrder: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'user-info.html',
        link       : function (scope, ele, attrs) {
            scope.logoutUrl = urlConfig.mtourLogoutUrl;
            scope.userInfo = $rootScope.userInfo;
            scope.canCreateOrder = function () {
                if ($rootScope.pageRoles) {
                    var length = $rootScope.pageRoles.length;
                    for (var i = 0; i < length; i++) {
                        if ($rootScope.pageRoles[i].roleCode == 'mtourOrder:createorder') {
                            return true;
                        }
                    }
                }
                if ($rootScope.createOrderRole) {
                    return true;
                }
                return false;
            }
        }
    };
}]);
mtour.directive('orderStatisticsInfo', ['$rootScope', 'urlConfig', function ($rootScope, urlConfig) {
    return {
        restrict   : 'A',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order-statistics-info.html',
        link       : function (scope, ele, attrs) {

        }
    };
}]);

mtour.directive('mainLeft', ['$window', '$timeout', 'urlConfig', 'qcApi', '$rootScope', function ($window, $timeout, urlConfig, qcApi, $rootScope) {
    return {
        restrict   : 'A',
        scope      : {
            currentMenuUrl: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'main-left.html',
        link       : function (scope, ele, attrs) {
            function setHeight() {
                var $menus = ele.find('ul:first');
                var top = $menus.offset().top;
                $menus.css({'height': $($window).height() - (top)});
            }

            scope.baseUrl = urlConfig.mtourBaseUrl;
            scope.menuFold = false;
            scope.menuSwitch = function () {
                scope.menuFold = !scope.menuFold;

                angular.forEach(scope.menus, function (menu) {
                    if (scope.menuFold) {
                        menu.spread = false;
                    }
                });
                var $rightPart = angular.element('#rightPart');
                if (scope.menuFold) {
                    $rightPart.css('margin-left', '50px');
                } else {
                    $rightPart.css('margin-left', '150px');
                }
            };
            scope.menuToggle = function (menu) {
                if (!menu.spread) {
                    angular.forEach(scope.menus, function (menu) {
                        menu.spread = false;
                    });
                }
                menu.spread = !menu.spread;
            };

            $($window).resize(function () {
                setHeight();
            });
            qcApi.post('common/getMenuByUser').success(function (result) {
                scope.menus = result.data;
                angular.forEach(scope.menus, function (menu) {
                    angular.forEach(menu.subMenus, function (subMenu) {
                        if (subMenu.subMenuUrl == '/mtour/common/createorder') {
                            $rootScope.createOrderRole = subMenu.roles;
                        }
                        if (subMenu.subMenuUrl == scope.currentMenuUrl) {
                            subMenu.active = true;
                            menu.active = true;
                            $rootScope.pageRoles = subMenu.roles;
                        }
                    });
                });
            });
            setHeight();
        }
    };
}]);

mtour.directive('mainFilter', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            items   : '=',
            itemText: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'main-filter.html',
        link       : function (scope, ele, attrs, ngModel) {
            scope.companyRoleCode=scope.$root.userInfo.companyRoleCode;
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            scope.select = function (item) {
                scope.selectedItem = item;
                ngModel.$setViewValue(item);
                scope.$emit('mainFilter.change', scope.selectedItem);
            }
        }
    };
}]);

//90需求-付款分款项和订单列表
mtour.directive('paymentListType', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            items   : '=',
            itemText: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + '/finance/payment-listType.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            scope.select = function (item) {
                scope.selectedItem = item;
                ngModel.$setViewValue(item);
                scope.$emit('payOrdermainFilter.change', scope.selectedItem);
            }
        }
    };
}]);


mtour.directive('mainSearch', ['urlConfig', 'fixedValue', 'qcObjectInArray', function (urlConfig, fixedValue, qcObjectInArray) {
    return {
        restrict   : 'A',
        //require: '^ngModel',
        scope      : {
            items            : '=',
            itemText         : '@',
            defaultSearchType: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'main-search.html',
        link       : function (scope, ele, attrs, ngModel) {

            scope.searchTypes = fixedValue.searchTypes;

            if (attrs.page == 'financeAging') {
                scope.searchTypes = [
                    {searchType: '1', searchTypeName: '团号'},
                    {searchType: '4', searchTypeName: '渠道名称'}
                ];
            } else if (attrs.page == 'financePay') {
                scope.searchTypes = [
                    {searchType: '1', searchTypeName: '团号'},
                    {searchType: '3', searchTypeName: '产品名称'}
                ];
            } else if (attrs.page == 'financeReceive') {
                scope.searchTypes = [
                    {searchType: '1', searchTypeName: '团号'}
                    //{searchType: '3', searchTypeName: '产品名称'}
                ];
            }

            scope.selectedSearchType = qcObjectInArray(scope.searchTypes, 'searchType', scope.defaultSearchType);
            //scope.searchKeyEnter= function (e) {
            //    var keyCode = window.event?e.keyCode:e.which;
            //    if(keyCode==13){
            //
            //    }
            //}
            scope.search = function () {
                scope.$emit('mainSearch.search', {
                    searchType: scope.selectedSearchType ? scope.selectedSearchType.searchType : "",
                    searchKey : scope.searchKey
                });
            };
            scope.$on('mainSearch.init', function () {
                scope.selectedSearchType = qcObjectInArray(scope.searchTypes, 'searchType', scope.defaultSearchType);
                scope.searchKey = '';
            });

            scope.$on('selectedSearchType.change', function () {
                scope.searchKey = '';
            });
        }
    };
}]);


mtour.directive('mainCount', ['urlConfig', 'fixedValue', 'qcObjectInArray', function (urlConfig, fixedValue, qcObjectInArray) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            currentCode: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'main-count.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                if (!angular.isArray(modelValue)) {
                    return;
                }
                scope.allCountInfo = qcObjectInArray(modelValue, 'listCategoryCode', '0');
                scope.currentCountInfo = qcObjectInArray(modelValue, 'listCategoryCode', scope.currentCode);
                scope.otherCountInfos = modelValue.filter(function (item) {
                    if (item.listCategoryCode != 0 && item.listCategoryCode != scope.currentCode) {
                        return true;
                    }
                });
            });
        }
    };
}]);


mtour.directive('searchFilterList', ['urlConfig', function (urlConfig) {
    return {
        restrict  : 'A',
        controller: ['$scope', function ($scope) {
            this.clearFilter = function () {
                $scope.$broadcast('searchFilter.clearAll');
                $scope.$emit('searchFilter.clearAll');
            };
            this.clearPayOrderListFilter = function () {
                $scope.$broadcast('payOrderlistSearchFilter.clearAll');
                $scope.$emit('payOrderlistSearchFilter.clearAll');
            };
            //收款的款项列表
            this.clearReceiveFilter = function () {
                $scope.$broadcast('searchFilter.clearAll');
                $scope.$emit('searchFilter.clearAll');
            };
            //收款的订单列表
            this.clearReceiveOrderFilter = function () {
                $scope.$broadcast('payOrderlistSearchFilter.clearAll');
                $scope.$emit('payOrderlistSearchFilter.clearAll');
            };
        }]
    };
}]);

mtour.directive('searchFilter', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            itemText : '@',
            labelText: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'search-filter.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.items = ngModel.$modelValue;
            });
            scope.$on('searchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
            scope.$on('payOrderlistSearchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
            scope.remove = function (item) {
                ngModel.$modelValue.remove(item);
                scope.$emit('searchFilter.remove');
                scope.$emit('payOrderlistSearchFilter.remove');
            };
        }
    };
}]);
mtour.directive('searchFilterDatePeriod', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            labelText: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'search-filter-date-period.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.items = ngModel.$modelValue;
            });
            scope.$on('searchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
            scope.$on('payOrderlistSearchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
            scope.remove = function (item) {
                ngModel.$modelValue.remove(item);
                scope.$emit('searchFilter.remove');
                scope.$emit('payOrderlistSearchFilter.remove');
            };
        }
    };
}]);

mtour.directive('searchFilterAmountPeriod', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            labelText: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'search-filter-amount-period.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.items = ngModel.$modelValue;
            });
            scope.$on('searchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
            scope.remove = function (item) {
                ngModel.$modelValue.remove(item);
                scope.$emit('searchFilter.remove');
            }
        }
    };
}]);
mtour.directive('searchFilterAmountPeriodPrice', ['urlConfig', function (urlConfig) {
	return {
		restrict   : 'A',
		require    : '^ngModel',
		scope      : {
			labelText: '@'
		},
		replace    : true,
		templateUrl: urlConfig.mtourHtmlTemplateUrl + 'search-filter-amount-period-price.html',
		link       : function (scope, ele, attrs, ngModel) {
			ngModel.$formatters.push(function (modelValue) {
				scope.items = ngModel.$modelValue;
			});
			scope.$on('searchFilter.clearAll', function () {
				ngModel.$modelValue.length = 0;
			});
			scope.remove = function (item) {
				ngModel.$modelValue.remove(item);
				scope.$emit('searchFilter.remove');
			}
		}
	};
}]);


mtour.directive('searchFilterText', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            labelText: '@'
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'search-filter-text.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.items = ngModel.$modelValue;
            });
            scope.$on('searchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
            //modify by wlj at 2016.06.02 -start
            scope.$on('payOrderlistSearchFilter.clearAll', function () {
                ngModel.$modelValue.length = 0;
            });
          //modify by wlj at 2016.06.02 -end
            scope.remove = function (item) {
                ngModel.$modelValue.remove(item);
                scope.$emit('searchFilter.remove');
            }
        }
    };
}]);

mtour.directive('searchFilterClear', [function (urlConfig) {
    return {
        restrict: 'A',
        require : '^searchFilterList',
        scope   : {
            pageFlag: '='
        },
        link    : function (scope, ele, attrs, searchFilterList) {
            ele.on('click', function () {
                scope.$apply(function () {
                    if (scope.pageFlag == '1' || scope.pageFlag == '3' || scope.pageFlag == '0') {//付款和收款的款项列表
                        searchFilterList.clearFilter();
                    }
                    if (scope.pageFlag == '2' || scope.pageFlag == '4') {//付款和收款的订单列表
                        searchFilterList.clearPayOrderListFilter();
                    }
                    if (scope.pageFlag == '5') {//付款和收款的订单列表
                        searchFilterList.clearPayOrderListFilter();
                    }
                    //if (scope.pageFlag == 'receivelist') {
                    //    searchFilterList.clearReceiveFilter();
                    //}
                    //if (scope.pageFlag == 'receiveorderlist') {
                    //    searchFilterList.clearReceiveOrderFilter();
                    //}
                });
            });
        }
    };
}]);


mtour.directive('exchangeRateIcon', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            exchangeRate: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'exchangeRateIcon.html',
        link       : function (scope, ele, attrs) {

        }
    };
}]);

mtour.directive('tourOperatorDropdown', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        require    : '^ngModel',
        restrict   : 'A',
        scope      : {
            tourOperatorTypeCode: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/tourOperator-dropdown.html',
        link       : function (scope, ele, attrs, ngModel) {
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedTourOperator = modelValue;
            });
            scope.$on('selectedTourOperator.search', function ($e, filterText) {
                qcApi.post('common/getSupplierList', {
                    tourOperatorTypeCode: scope.tourOperatorTypeCode,
                    tourOperatorName    : filterText,
                    count               : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.tourOperators = result.data;
                });
            });
            scope.$on('selectedTourOperator.change', function () {
                ngModel.$setViewValue(scope.selectedTourOperator);
            });
        }
    };
}]);


mtour.directive('paginationSort', ['$rootScope','urlConfig', function ($rootScope,urlConfig) {
    return {
        restrict   : 'A',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'pagination-sort.html',
        link       : function (scope, ele, attrs) {
            scope.canFoldAll = true;
            //console.log(scope);
            if (attrs.canFoldAll == 'false') {
                scope.canFoldAll = false;
            }
            scope.companyRoleCode=$rootScope.userInfo.companyRoleCode;
            //scope.companyRoleCode=scope.$parent.$root.userInfo.companyRoleCode;
            scope.theSame=true;
            scope.sortInfo.dec=false;
            scope.setSort = function (key,thesa) {
                if (scope.sortInfo.sortKey != key.code) {
                    scope.sortInfo.sortKey = key.code;
                    scope.upAsc=false;
                    scope.downDesc=true;
                    scope.sortInfo.dec = true;
                    scope.theSame=false;
                } else {
                   if(!thesa){
                       // scope.theSame=!theSame;;
                       scope.upAsc=true;
                       scope.downDesc=false;
                       scope.sortInfo.dec = !scope.sortInfo.dec;
                   }else{
                       scope.upAsc=false;
                       scope.downDesc=true;
                       scope.sortInfo.dec = !scope.sortInfo.dec;
                    }
                    scope.theSame=!thesa;;



                  /*  scope.upAsc=false;
                    scope.downDesc=true;
                    scope.sortInfo.dec = !scope.sortInfo.dec;*/
                }
                scope.$emit('sort.change');
            };
            scope.foldAll = function () {
                scope.$emit('subTable.foldAll.request');
            };
            scope.pre = function () {
                if (scope.pageInfo.currentIndex > 1) {
                    scope.pageInfo.currentIndex = +(scope.pageInfo.currentIndex) - 1;
                    scope.$emit('pagination.change');
                }
            };
            scope.next = function () {
                if (scope.pageInfo.currentIndex < scope.calculatePageCount()) {
                    scope.pageInfo.currentIndex = +(scope.pageInfo.currentIndex) + 1;
                    scope.$emit('pagination.change');
                }
            };
            scope.calculatePageCount = function () {
                if (!scope.pageInfo || !scope.pageInfo.totalRowCount) {
                    return 0;
                }
                return Math.ceil(scope.pageInfo.totalRowCount / scope.pageInfo.rowCount);
            };
        }
    };
}]);

//90需求-付款的订单列表的排序
mtour.directive('payOrderPaginationSort', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'payorder-pagination-sort.html',
        link       : function (scope, ele, attrs) {
            scope.canFoldAll = true;
            if (attrs.canFoldAll == 'false') {
                scope.canFoldAll = false;
            }
            /**
             * modify by wlj at 2016.08.22 -start
             * @param key
             */

            scope.theSame=true;
            scope.payOrderSortInfo.dec=false;
            scope.setSort1 = function (key,thesa) {
                /*if (scope.payOrderSortInfo.sortKey != key.code) {
                    scope.payOrderSortInfo.sortKey = key.code;
                    scope.payOrderSortInfo.dec = true;
                } else {
                    scope.payOrderSortInfo.dec = !scope.payOrderSortInfo.dec;
                }*/
                if (scope.payOrderSortInfo.sortKey != key.code) {
                    scope.payOrderSortInfo.sortKey = key.code;
                    scope.upAsc=false;
                    scope.downDesc=true;
                    scope.payOrderSortInfo.dec = true;
                    scope.theSame=false;
                } else {
                    if(!thesa){
                        // scope.theSame=!theSame;;
                        scope.upAsc=true;
                        scope.downDesc=false;
                        scope.payOrderSortInfo.dec = !scope.payOrderSortInfo.dec;
                    }else{
                        scope.upAsc=false;
                        scope.downDesc=true;
                        scope.payOrderSortInfo.dec = !scope.payOrderSortInfo.dec;
                    }
                    scope.theSame=!thesa;;
                }
                scope.$emit('payOrderSort.change');
            };
            scope.foldAll1 = function () {
                scope.$emit('subTable.foldAll.request');
            };
            scope.pre1 = function () {
                if (scope.payorder_pageInfo.currentIndex > 1) {
                    scope.payorder_pageInfo.currentIndex = +(scope.payorder_pageInfo.currentIndex) - 1;
                    scope.$emit('payOrderPagination.change');
                }
            };
            scope.next1 = function () {
                if (scope.payorder_pageInfo.currentIndex < scope.calculatePageCount()) {
                    scope.payorder_pageInfo.currentIndex = +(scope.payorder_pageInfo.currentIndex) + 1;
                    scope.$emit('payOrderPagination.change');
                }
            };
            scope.calculatePageCount1 = function () {
                if (!scope.payorder_pageInfo || !scope.payorder_pageInfo.totalRowCount) {
                    return 0;
                }
                return Math.ceil(scope.payorder_pageInfo.totalRowCount / scope.payorder_pageInfo.rowCount);
            };
        }
    };
}]);

mtour.directive('pagination', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            pageInfo: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'pagination.html',
        link       : function (scope, ele, attrs) {
            scope.centerCount = 5;
            scope.goto = function (index) {
                scope.pageInfo.currentIndex = index;
                scope.$emit('pagination.change');
            };
            scope.confirmCurrentIndex = function () {
                if (scope.currentIndex > scope.calculatePageCount()) {
                    scope.currentIndex = scope.calculatePageCount();
                }
                if (scope.currentIndex < 1) {
                    scope.currentIndex = 1;
                }
                scope.pageInfo.currentIndex = scope.currentIndex;
                scope.$emit('pagination.change');
            };
            scope.pre = function () {
                if (scope.pageInfo.currentIndex > 1) {
                    scope.pageInfo.currentIndex -= 1;
                    scope.$emit('pagination.change');
                }
            };
            scope.next = function () {
                if (scope.pageInfo.currentIndex < scope.calculatePageCount()) {
                    scope.pageInfo.currentIndex = +(scope.pageInfo.currentIndex) + 1;
                    scope.$emit('pagination.change');
                }
            };
            scope.calculatePageCount = function () {
                return Math.ceil(scope.pageInfo.totalRowCount / scope.pageInfo.rowCount);
            };
            scope.getCenterList = function () {
                var startIndex = 2;
                var centerList = [];
                if (scope.pageInfo.currentIndex > ((scope.centerCount + 3) / 2)) {
                    startIndex = scope.pageInfo.currentIndex - ((scope.centerCount - 1) / 2);
                }
                startIndex = Math.min(startIndex, scope.calculatePageCount() - scope.centerCount);
                if (startIndex < 2) {
                    startIndex = 2;
                }
                var endIndex = startIndex + Math.min(scope.centerCount, scope.calculatePageCount() - startIndex);
                if (endIndex > 1) {
                    var i = startIndex;
                    while (i < endIndex) {
                        centerList.push(i);
                        i++;
                    }
                }
                return centerList;
            };
            scope.isShowPreEllipsis = function () {
                return (scope.getCenterList()[0] > 2);
            };
            scope.isShowNextEllipsis = function () {
                var centerList = scope.getCenterList();
                var length = centerList.length;
                return centerList[length - 1] < (scope.calculatePageCount() - 1);
            };

            scope.$watch('pageInfo.rowCount', function (oldValue, newValue) {
                if (oldValue && oldValue != newValue) {
                    scope.$emit('pagination.change', 'backFirstPage');
                }
            });

            scope.$watch('pageInfo.currentIndex', function () {
                scope.currentIndex = scope.pageInfo.currentIndex;
            });
        }
    };
}]);
mtour.directive('payOrderPagination', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            pageInfo: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'pagination.html',
        link       : function (scope, ele, attrs) {
            scope.centerCount = 5;
            scope.goto = function (index) {
                scope.pageInfo.currentIndex = index;
                scope.$emit('payOrderPagination.change');
            };
            scope.confirmCurrentIndex = function () {
                if (scope.currentIndex > scope.calculatePageCount()) {
                    scope.currentIndex = scope.calculatePageCount();
                }
                if (scope.currentIndex < 1) {
                    scope.currentIndex = 1;
                }
                scope.pageInfo.currentIndex = scope.currentIndex;
                scope.$emit('payOrderPagination.change');
            };
            scope.pre = function () {
                if (scope.pageInfo.currentIndex > 1) {
                    scope.pageInfo.currentIndex -= 1;
                    scope.$emit('payOrderPagination.change');
                }
            };
            scope.next = function () {
                if (scope.pageInfo.currentIndex < scope.calculatePageCount()) {
                    scope.pageInfo.currentIndex = +(scope.pageInfo.currentIndex) + 1;
                    scope.$emit('payOrderPagination.change');
                }
            };
            scope.calculatePageCount = function () {
                return Math.ceil(scope.pageInfo.totalRowCount / scope.pageInfo.rowCount);
            };
            scope.getCenterList = function () {
                var startIndex = 2;
                var centerList = [];
                if (scope.pageInfo.currentIndex > ((scope.centerCount + 3) / 2)) {
                    startIndex = scope.pageInfo.currentIndex - ((scope.centerCount - 1) / 2);
                }
                startIndex = Math.min(startIndex, scope.calculatePageCount() - scope.centerCount);
                if (startIndex < 2) {
                    startIndex = 2;
                }
                var endIndex = startIndex + Math.min(scope.centerCount, scope.calculatePageCount() - startIndex);
                if (endIndex > 1) {
                    var i = startIndex;
                    while (i < endIndex) {
                        centerList.push(i);
                        i++;
                    }
                }
                return centerList;
            };
            scope.isShowPreEllipsis = function () {
                return (scope.getCenterList()[0] > 2);
            };
            scope.isShowNextEllipsis = function () {
                var centerList = scope.getCenterList();
                var length = centerList.length;
                return centerList[length - 1] < (scope.calculatePageCount() - 1);
            };

            scope.$watch('pageInfo.rowCount', function (oldValue, newValue) {
                if (oldValue && oldValue != newValue) {
                    scope.$emit('payOrderPagination.change', 'backFirstPage');
                }
            });

            scope.$watch('pageInfo.currentIndex', function () {
                scope.currentIndex = scope.pageInfo.currentIndex;
            });
        }
    };
}]);

mtour.directive('dialogUpload', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        scope      : {
            attachment: '='
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'dialog-upload.html',
        link       : function (scope, ele, attrs) {
            scope.editModel = attrs.editModel ? attrs.editModel : 'edit';
            scope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
            if (!angular.isArray(scope.newAttachments)) {
                scope.newAttachments = [];
                scope.editModel = 'edit';
            } else {
                scope.editModel = 'detail';
            }
            scope.qcUploaderOptions = {
                makeThumb: true,
                swf      : urlConfig.mtourStaticUrl + 'components/flash/Uploader.swf',
                server   : urlConfig.mtourApiUrl + 'common/upload?requestType=mtour data',
                //accept: {
                //    title: 'Images',
                //    extensions: 'gif,jpg,jpeg,bmp,png',
                //    mimeTypes: 'image/*'
                //},
                accept   : null,
                auto     : true,
                resize   : false
            };
            scope.remove = function (file) {
                qcApi.post('common/delFile', {attachmentUuid: file.attachmentUuid}).success(function () {
                    scope.attachment.remove(file);
                });
            }
            scope.$on('uploader.completed', function ($e, uploadedFile) {
                var file = {
                    attachmentUuid: uploadedFile.attachmentUuid,
                    attachmentUrl : uploadedFile.attachmentUrl,
                    fileName      : uploadedFile.fileName,
                    status        : uploadedFile.status
                };
                if (!angular.isArray(scope.attachment)) {
                    scope.attachment = [];
                }
                scope.attachment.push(file);
                //scope.newAttachments.remove(uploadedFile);
                scope.$broadcast('uploader.removeFile', uploadedFile.id);
            });
            scope.$on('upload.editModel.reset', function ($e, editModel) {
                scope.editModel = editModel;
            });
            angular.module('file')

        }
    };
}]);

//上传的文件名称过长的时候，以省略号展示(默认20个)
mtour.filter('nameCut', function () {
    return function (value,wordwise,max,tail) {
        if(!value) return '';
        max=parseInt(max,10);
        if(!max) return value;
        if(value.length<max) return value;
        value=value.substr(0,max);
        if(wordwise){
            var lastspace=value.lastIndexOf(" ");
            if(lastspace!=-1){
                value=value.substr(0,lastspace);
            }
        }
        return value+(tail||'...');
    }
});

mtour.directive('printExpenditureButton', ['urlConfig', '$window', function (urlConfig, $window) {
    return {
        restrict: 'A',
        scope   : {
            paymentUuid: '=',
            fundsType  : '='
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/printOutPayPage?paymentUuid=' + scope.paymentUuid + '&fundsType=' + scope.fundsType + '');
            });
        }
    };
}]);

mtour.directive('printIncomeButton', ['urlConfig', '$window', function (urlConfig, $window) {
    return {
        restrict: 'A',
        scope   : {
            receiveUuid: '=',
            fundsType  : '=',
            whichone: '='
        },
        link    : function (scope, ele, attrs) {
            console.log(scope.whichone);

            ele.on('click', function () {
                $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/printIncomeInfoPage?receiveUuid=' + scope.receiveUuid + '&fundsType=' + scope.fundsType + '');
            });
        }
    };
}]);
mtour.directive('showFinanceIncome', ['urlConfig', '$window', function (urlConfig, $window) {
    return {
        restrict: 'A',
        scope   : {
            receiveUuid: '=',
            fundsType  : '=',
            whichone: '='
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/showFinanceIncome?incomeType=' + scope.whichone.exportType + '');
            });
        }
    };
}]);

mtour.directive('showExp', ['urlConfig', '$window','qcDialog','qcApi','$rootScope','qcMessage', function (urlConfig, $window,qcDialog,qcApi,$rootScope,qcMessage) {
    return {
        restrict: 'A',
        scope   : {
            receiveUuid: '=',
            fundsType  : '=',
            whichone: '='
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                //支出单
                var manyToDownObj = scope.$parent.$parent.$parent.$parent.$parent.manyToDown;
                var manyToDownArray = [];
                for (var i in manyToDownObj) {
                    if (manyToDownObj[i] == "1") {
                        manyToDownArray.push(i);
                    }
                }
                if (manyToDownArray.length == 0) {
                    qcMessage.tip('请选中订单之后再进行操作');
                }else{
                if (scope.whichone.loadBathType == '1') {
                    qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/mergeManyExpenditurePage.html',
                        controller: ['$scope', function (dialogScope) {
                            /* var manyToDownObj = scope.$parent.$parent.$parent.$parent.$parent.manyToDown;
                             var manyToDownArray = [];
                             for (var i in manyToDownObj) {
                             if (manyToDownObj[i] == "1") {
                             manyToDownArray.push(i);
                             }
                             }*/
                            qcApi.post('order/batchQueryPayOrder', {
                                //orderUuid: scope.orderUuid,
                                orderUuid: manyToDownArray
                            }).success(function (result) {
                                dialogScope.result = result.data;
                                dialogScope.result.expenditureList = result.data.expenditureList;
                                //dialogScope.payObjList=dialogScope.result.
                                dialogScope.selectedUuid = [];
                                Array.prototype.remove = function (dx) {
                                    var index = this.indexOf(dx);
                                    if (index > -1) {
                                        this.splice(index, 1);
                                    }
                                }
                                //默认全选
                                angular.forEach(dialogScope.result.expenditureList, function (result) {
                                    //多选框的选中
                                    result.chkAllObj = true;
                                    result.checkAll = true;

                                    var filterArr = [];
                                    //下面每个单独选项的选中
                                    angular.forEach(result.paymentObject, function (res) {
                                        //result.batchChecked = true;
                                        res.checkedDetail = true;
                                        res.display = "block";
                                        //dialogScope.selectedUuid.push(res.fundsPNR);
                                    })
                                    angular.forEach(result.payObj, function (res) {
                                            res.batchChecked = true;
                                            filterArr.push(res.paymentObjectUuid);
                                        }
                                    )
                                    result.displays = filterArr;
                                });


                            });


                            //全选
                            dialogScope.chkAll = function ($event, eachMSG) {
                                var checkbox = $event.target;
                                if (checkbox.checked) {
                                    angular.forEach(eachMSG, function (result) {
                                        result.batchChecked = true;
                                        result.checkedDetail = true;
                                        //dialogScope.selectedUuid.push(result.fundsPNR);
                                    });
                                } else {
                                    angular.forEach(eachMSG, function (result) {
                                        result.checkedDetail = false;
                                        //dialogScope.selectedUuid.remove(result.fundsPNR);
                                    });
                                }
                            };
                            dialogScope.chkAllPayObj = function ($event, eachMSG, expenditure, nowMsg) {
                                var checkbox = $event.target;
                                var groupNo = expenditure.groupNo;
                                var filterArr = [];
                                var newArray = [].concat(dialogScope.result.expenditureList);
                                if ($event.target.checked == true) {
                                    //过滤
                                    //将id=uuid的信息展现出来
                                    // parentMsg.chkAllObj=true;
                                    angular.forEach(newArray, function (result) {
                                        if (result.groupNo == groupNo) {
                                            angular.forEach(result.paymentObject, function (res) {
                                                //设置为选中,并显示
                                                res.checkedDetail = true;
                                            })
                                        }
                                    })
                                    angular.forEach(eachMSG, function (rs) {
                                        rs.batchChecked = true;
                                        filterArr.push(rs.paymentObjectUuid);
                                    });
                                } else {
                                    //将id=uuid的信息隐藏
                                    //parentMsg.chkAllObj=false;
                                    angular.forEach(newArray, function (result) {
                                        if (result.groupNo == groupNo) {
                                            angular.forEach(result.paymentObject, function (res) {
                                                //设置为不选中,并隐藏
                                                res.checkedDetail = false;
                                            })
                                        }
                                    })
                                    angular.forEach(eachMSG, function (rs) {
                                        rs.batchChecked = false;
                                        filterArr = [];
                                    });
                                }
                                expenditure.displays = filterArr;
                            };


                            dialogScope.totalUuid = function () {
                                var total = "";
                                dialogScope.selectedUuid = [];
                                angular.forEach(dialogScope.result.expenditureList, function (results) {
                                    angular.forEach(results.paymentObject, function (res) {
                                        if (res.checkedDetail == true) {
                                            dialogScope.selectedUuid.push(res.fundsPNR);
                                        }
                                    })
                                });
                                //dialogScope.selectedUuid;
                                var total = dialogScope.selectedUuid.join(",");
                                return total;

                            };
                            function changeTotalUuid(newValue, oldValue, scope) {
                                dialogScope.selectedUuid = newValue;
                            }

                            //dialogScope.$watch(dialogScope.totalUuid,changeTotalUuid);
                            //或者用下一种写法
                            dialogScope.getChecked = function () {
                                var flag=false;
                                dialogScope.selectedUuid = [];
                                dialogScope.returnDownObj = [];
                                angular.forEach(dialogScope.result.expenditureList, function (results) {
                                    var obj = {};
                                    obj.orderUuid = results.orderUuid;
                                    obj.groupNo=results.groupNo;
                                    var paymentList = [];
                                    angular.forEach(results.paymentObject, function (res) {
                                        if (res.checkedDetail == true) {
                                            flag=true;
                                            var _tempObj = {};
                                            //dialogScope.selectedUuid.push(res.fundsPNR);
                                            _tempObj["currencyId"] = res.currencyId;
                                            _tempObj["paymentUuid"] = res.paymentUuid;
                                            _tempObj["fundsType"] = res.fundsType;
                                            _tempObj["paymentObjectUuid"] = res.paymentObjectUuid;
                                            _tempObj["paymentObjectName"] = res.paymentObjectName;
                                            paymentList.push(_tempObj);
                                        }
                                    })
                                    obj["paymentList"] = paymentList;
                                    dialogScope.returnDownObj.push(obj);
                                    dialogScope.flag=flag;
                                });
                            }
                            dialogScope.getCheckedAndDownLoad=function(){
                                dialogScope.getChecked();
                                if(!dialogScope.flag){
                                    qcMessage.tip('请选择需要下载的数据');
                                }else{
                                    $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/batchDownloadPaySheet?params=' + JSON.stringify(dialogScope.returnDownObj) + '');
                                }
                                //ele.on('click', function () {
                                  /*  qcApi.post('mtourfinance/batchDownloadPaySheet', {
                                        params: dialogScope.returnDownObj
                                    }).success(function (result) {

                                        console.log("result===0"+result);
                                    console.log("下载成功");

                                    //});
                                })*/


                            }

                            dialogScope.chkAlone = function ($event, eachMSG, parentMsg) {
                                if ($event.target.checked == true) {
                                    eachMSG.checkedDetail = true;
                                    //dialogScope.selectedUuid.push(eachMSG.fundsPNR);
                                } else {
                                    eachMSG.checkedDetail = false;
                                    parentMsg.checkAll = false;
                                    //dialogScope.selectedUuid.remove(eachMSG.fundsPNR);
                                }
                            };
                            //选中某个付款对象进行的操作
                            dialogScope.chkPayObj = function ($event, eachObjMSG, parentMsg) {
                                //选中的支付对象
                                var uuid = eachObjMSG.paymentObjectUuid;
                                //对象的团号
                                var groupNo = parentMsg.groupNo;
                                var newArray = [].concat(dialogScope.result.expenditureList);
                                if ($event.target.checked == true) {
                                    //过滤
                                    //将id=uuid的信息展现出来
                                    // parentMsg.chkAllObj=true;
                                    angular.forEach(newArray, function (result) {
                                        if (result.groupNo == groupNo) {
                                            angular.forEach(result.paymentObject, function (res) {
                                                if (res.paymentObjectUuid == uuid) {
                                                    //设置为选中,并显示
                                                    res.checkedDetail = true;
                                                    result.displays.push(uuid);
                                                }
                                            })
                                        }
                                    })
                                } else {
                                    //将id=uuid的信息隐藏
                                    parentMsg.chkAllObj = false;
                                    angular.forEach(newArray, function (result) {
                                        if (result.groupNo == groupNo) {
                                            angular.forEach(result.paymentObject, function (res) {
                                                if (res.paymentObjectUuid == uuid) {
                                                    //设置为不选中,并隐藏
                                                    res.checkedDetail = false;
                                                    if(result.displays.indexOf(uuid)>-1){
                                                        result.displays.splice(result.displays.indexOf(uuid), 1);
                                                    }
                                                } else {
                                                    //留下
                                                }
                                            })
                                        }
                                    })
                                }
                            };


                            dialogScope.applicant = $rootScope.userInfo.userName;
                            dialogScope.expenditrueListIsShow = 0;
                        }
                        ],
                        className: 'qc-dialog-theme-page',
                        width: 820,
                        height: 350,
                        plain: false
                    });
                } else if (scope.whichone.loadBathType == '2') {//收入单
                    var manyToDownObj = scope.$parent.$parent.$parent.$parent.$parent.manyToDown;
                    var manyToDownArray = [];
                    for (var i in manyToDownObj) {
                        if (manyToDownObj[i] == "1") {
                            manyToDownArray.push(i);
                        }
                    }
                    if (manyToDownArray.length == 0) {
                        qcMessage.warning('请选中订单之后再进行操作!');
                    } else {
                        qcDialog.open({
                            templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/mergeIncomePage.html',
                            controller: ['$scope', function (dialogScope) {

                                qcApi.post('order/batchQueryReceiveOrder', {
                                    orderUuid: manyToDownArray
                                }).success(function (result) {
                                    dialogScope.result = result.data;
                                    //dialogScope.selectedPaymentObject = dialogScope.reslut.paymentObject[0];
                                    dialogScope.result.expenditureList = result.data.expenditureList;
                                    //默认全选
                                    angular.forEach(dialogScope.result.expenditureList, function (result) {
                                        //多选框的选中
                                        result.chkAllObj = true;
                                        result.checkAll = true;

                                        var filterArr = [];
                                        //下面每个单独选项的选中
                                        angular.forEach(result.paymentObject, function (res) {
                                            //result.batchChecked = true;
                                            res.checkedDetail = true;
                                            res.display = "block";
                                            //dialogScope.selectedUuid.push(res.fundsPNR);
                                        })
                                        angular.forEach(result.payObj, function (res) {
                                                res.batchChecked = true;
                                                filterArr.push(res.paymentObjectUuid);
                                            }
                                        )
                                        result.displays = filterArr;
                                    });

                                });

                                //dialogScope.payObjList=dialogScope.result.
                                dialogScope.selectedUuid = [];
                                Array.prototype.remove = function (dx) {
                                    var index = this.indexOf(dx);
                                    if (index > -1) {
                                        this.splice(index, 1);
                                    }
                                }
                                //全选
                                dialogScope.chkAll = function ($event, eachMSG) {
                                    var checkbox = $event.target;
                                    if (checkbox.checked) {
                                        angular.forEach(eachMSG, function (result) {
                                            result.batchChecked = true;
                                            result.checkedDetail = true;
                                            //dialogScope.selectedUuid.push(result.fundsPNR);
                                        });
                                    } else {
                                        angular.forEach(eachMSG, function (result) {
                                            result.checkedDetail = false;
                                            //dialogScope.selectedUuid.remove(result.fundsPNR);
                                        });
                                    }
                                };

                                dialogScope.chkAllPayObj = function ($event, eachMSG, expenditure, nowMsg) {
                                    var checkbox = $event.target;
                                    var groupNo = expenditure.groupNo;
                                    var filterArr = [];
                                    var newArray = [].concat(dialogScope.result.expenditureList);
                                    if ($event.target.checked == true) {
                                        //过滤
                                        //将id=uuid的信息展现出来
                                        // parentMsg.chkAllObj=true;
                                        angular.forEach(newArray, function (result) {
                                            if (result.groupNo == groupNo) {
                                                angular.forEach(result.paymentObject, function (res) {
                                                    //设置为选中,并显示
                                                    res.checkedDetail = true;
                                                })
                                            }
                                        })
                                        angular.forEach(eachMSG, function (rs) {
                                            rs.batchChecked = true;
                                            filterArr.push(rs.paymentObjectUuid);
                                        });
                                    } else {
                                        //将id=uuid的信息隐藏
                                        //parentMsg.chkAllObj=false;
                                        angular.forEach(newArray, function (result) {
                                            if (result.groupNo == groupNo) {
                                                angular.forEach(result.paymentObject, function (res) {
                                                    //设置为不选中,并隐藏
                                                    res.checkedDetail = false;
                                                })
                                            }
                                        })
                                        angular.forEach(eachMSG, function (rs) {
                                            rs.batchChecked = false;
                                            filterArr = [];
                                        });
                                    }
                                    expenditure.displays = filterArr;
                                };

                                dialogScope.totalUuid = function () {
                                    var total = "";
                                    dialogScope.selectedUuid = [];
                                    angular.forEach(dialogScope.result.expenditureList, function (results) {
                                        angular.forEach(results.paymentObject, function (res) {
                                            if (res.checkedDetail == true) {
                                                dialogScope.selectedUuid.push(res.fundsPNR);
                                            }
                                        })
                                    });
                                    //dialogScope.selectedUuid;
                                    var total = dialogScope.selectedUuid.join(",");
                                    return total;

                                };
                                function changeTotalUuid(newValue, oldValue, scope) {
                                    dialogScope.selectedUuid = newValue;
                                }

                                //dialogScope.$watch(dialogScope.totalUuid,changeTotalUuid);
                                //或者用下一种写法
                                dialogScope.getChecked = function () {
                                    var flag=false;
                                    dialogScope.selectedUuid = [];
                                    dialogScope.returnDownObj = {};
                                    var outSideObj = {};
                                    outSideObj.income = [];
                                    var outSideArray = [];
                                    angular.forEach(dialogScope.result.expenditureList, function (results) {
                                        var insideObj = {};
                                        var paymentObj = [];
                                        insideObj.orderId = results.id;
                                        angular.forEach(results.paymentObject, function (res) {
                                            if (res.checkedDetail == true) {
                                                flag=true;
                                                var _tempObj = {};
                                                //dialogScope.selectedUuid.push(res.fundsPNR);
                                                _tempObj["payObjectUuid"] = res.payObjectUuid;
                                                _tempObj["payPriceType"] = res.payPriceType;
                                                _tempObj["paymentObjectName"] = res.paymentObjectName;
                                                paymentObj.push(_tempObj);
                                            }
                                        })
                                        insideObj["paymentObj"] = paymentObj;
                                        outSideArray.push(insideObj);
                                        dialogScope.flag=flag;
                                    });
                                    /*outSideObj.income = outSideArray;
                                    dialogScope.returnDownObj.paymentObj = outSideObj;*/
                                    outSideObj.income = outSideArray;
                                    dialogScope.returnDownObj = outSideObj;
                                }
                                dialogScope.getCheckedAndDownLoad=function(){
                                    dialogScope.getChecked();
                                    if(!dialogScope.flag){
                                        qcMessage.tip('请选择需要下载的数据');
                                    }else{
                                        $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/batchDownloadIncomeSheet?params=' + JSON.stringify(dialogScope.returnDownObj) + '');
                                    }
                                }

                                dialogScope.chkAlone = function ($event, eachMSG, parentMsg) {
                                    if ($event.target.checked == true) {
                                        eachMSG.checkedDetail = true;
                                        //dialogScope.selectedUuid.push(eachMSG.fundsPNR);
                                    } else {
                                        eachMSG.checkedDetail = false;
                                        parentMsg.checkAll = false;
                                        //dialogScope.selectedUuid.remove(eachMSG.fundsPNR);
                                    }
                                };
                                //选中某个付款对象进行的操作
                                dialogScope.chkPayObj = function ($event, eachObjMSG, parentMsg) {
                                    //选中的支付对象
                                    var uuid = eachObjMSG.paymentObjectUuid;
                                    //对象的团号
                                    var groupNo = parentMsg.groupNo;
                                    var newArray = [].concat(dialogScope.result.expenditureList);
                                    if ($event.target.checked == true) {
                                        //过滤
                                        //将id=uuid的信息展现出来
                                        // parentMsg.chkAllObj=true;
                                        angular.forEach(newArray, function (result) {
                                            if (result.groupNo == groupNo) {
                                                angular.forEach(result.paymentObject, function (res) {
                                                    if (res.paymentObjectUuid == uuid) {
                                                        //设置为选中,并显示
                                                        res.checkedDetail = true;
                                                        result.displays.push(uuid);
                                                    }
                                                })
                                            }
                                        })
                                    } else {
                                        //将id=uuid的信息隐藏
                                        parentMsg.chkAllObj = false;
                                        angular.forEach(newArray, function (result) {
                                            if (result.groupNo == groupNo) {
                                                angular.forEach(result.paymentObject, function (res) {
                                                    if (res.paymentObjectUuid == uuid) {
                                                        //设置为不选中,并隐藏
                                                        res.checkedDetail = false;
                                                        if(result.displays.indexOf(uuid)>-1){
                                                            result.displays.splice(result.displays.indexOf(uuid), 1);
                                                        }
                                                        //result.displays.splice(result.displays.indexOf(uuid), 1);
                                                    } else {
                                                        //留下
                                                    }
                                                })
                                            }
                                        })
                                    }
                                };


                                dialogScope.applicant = $rootScope.userInfo.userName;
                                dialogScope.expenditrueListIsShow = 0;
                            }
                            ],
                            className: 'qc-dialog-theme-page',
                            width: 820,
                            height: 350,
                            plain: false
                        });
                    }

                } else if (scope.whichone.loadBathType == '3') {//结算单
                    //$window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/showFinanceIncome');
                    var manyToDownObj = scope.$parent.$parent.$parent.$parent.$parent.manyToDown;
                    var manyToDownArray = [];
                    for (var i in manyToDownObj) {
                        if (manyToDownObj[i] == "1") {
                            manyToDownArray.push(i);
                        }
                    }
                    ele.on('click', function () {
                        console.log();
                        $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/batchDownLoadSettlemnt?orderUuids=' + manyToDownArray + '');
                    });
                }
            }
            });
        }
    };
}]);

mtour.directive('printConfirmButton', ['urlConfig', '$window', function (urlConfig, $window) {
    return {
        restrict: 'A',
        scope   : {
            orderUuid: '='
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/printConfirmPage?orderUuid=' + scope.orderUuid + '');
            });
        }
    };
}]);


mtour.directive('printSettlementButton', ['urlConfig', '$window', function (urlConfig, $window) {
    return {
        restrict: 'A',
        scope   : {
            orderUuid: '='
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/printSettlementPage?orderUuid=' + scope.orderUuid);
            });
        }
    };
}]);

mtour.directive('expenditureButton', ['urlConfig', '$window', 'qcDialog', 'qcApi', 'qcObjectInArray', 'commonValue', '$rootScope', function (urlConfig, $window, qcDialog, qcApi, qcObjectInArray, commonValue, $rootScope) {
    return {
        restrict: 'A',
        scope   : {
            orderUuid: '='
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                    qcDialog.open({
                        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/mergeExpenditurePage.html',
                        controller : ['$scope', function (dialogScope) {
                            qcApi.post('order/queryPayOrder', {
                                orderUuid: scope.orderUuid
                            }).success(function (result) {
                                dialogScope.reslut = result.data;
                                dialogScope.reslut.paymentObject.unshift({
                                    'paymentObjectCode': 'ALL',
                                    'paymentObjectName': '请选择'
                                });
                                dialogScope.selectedPaymentObject = dialogScope.reslut.paymentObject[0];
                            });
                            dialogScope.applicant = $rootScope.userInfo.userName;
                            dialogScope.expenditrueListIsShow = 0;
                            dialogScope.$on('selectedPaymentObject.change', function () {
                                if (dialogScope.selectedPaymentObject.paymentObjectCode != 'ALL') {
                                    //根据付款对象查询提交后的付款数据
                                    qcApi.post('mtourfinance/getOrderPaymentInfo', {
                                        orderUuid                      : scope.orderUuid,
                                        paymentObjectUuid              : dialogScope.selectedPaymentObject.paymentObjectUuid,
                                        tourOperatorChannelCategoryCode: dialogScope.selectedPaymentObject.tourOperatorChannelCategoryCode
                                    }).success(function (result) {
                                        dialogScope.payList = result.data;
                                        //angular.forEach(dialogScope.payList, function (pay) {
                                        //    pay.payableAmount.currencyCode = qcObjectInArray(commonValue.currencies, 'currencyUuid', pay.payableAmount.currencyUuid).currencyCode;
                                        //});
                                    });
                                    dialogScope.expenditrueListIsShow = 1;
                                }
                                else {
                                    dialogScope.expenditrueListIsShow = 0;
                                    $('#chkAll').removeAttr('checked');
                                }
                            });
                            //全选
                            dialogScope.chkAll = function ($event) {
                                var checkbox = $event.target;
                                if (checkbox.checked) {
                                    angular.forEach(dialogScope.payList.results, function (result) {
                                        result.batchChecked = true;
                                    });
                                }
                                else {
                                    angular.forEach(dialogScope.payList.results, function (result) {
                                        result.batchChecked = false;
                                    });
                                }
                            };

                            dialogScope.payableTotalAmount = function () {
                                var checkedPay = [];
                                var tmpObj = {};
                                var strAmount = '';
                                if (dialogScope.payList) {
                                    angular.forEach(dialogScope.payList.results, function (pay) {
                                        if (pay.batchChecked) {
                                            checkedPay.push({
                                                currencyCode: qcObjectInArray(commonValue.currencies, 'currencyUuid', pay.currencyId).currencyCode,
                                                amount      : pay.strPayableAmount_amount
                                            });
                                        }
                                    });
                                    if (checkedPay.length > 0) {
                                        for (var i = 0; i < checkedPay.length; i++) {
                                            if (!tmpObj.hasOwnProperty(checkedPay[i].currencyCode)) {
                                                tmpObj[checkedPay[i].currencyCode] = (+checkedPay[i].amount).toFixed(2);
                                            }
                                            else {
                                                tmpObj[checkedPay[i].currencyCode] = (+tmpObj[checkedPay[i].currencyCode]).floatAdd(+checkedPay[i].amount).toFixed(2);
                                            }
                                        }
                                        for (var amout in tmpObj) {
                                            strAmount += amout + tmpObj[amout] + '+';
                                        }
                                        strAmount = strAmount.substr(0, strAmount.length - 1);

                                    }
                                    else {
                                        strAmount = '';
                                    }
                                }
                                return strAmount;
                            };

                            dialogScope.penditureBtnEnabled = function () {
                                var i = 0;
                                if (dialogScope.payList) {


                                    angular.forEach(dialogScope.payList.results, function (pay) {
                                        if (pay.batchChecked) {
                                            i++;
                                        }
                                    });
                                    if (i == 0) {
                                        return false;
                                    }
                                    else {
                                        return true;
                                    }
                                }
                                else {
                                    return false;
                                }
                            };

                            dialogScope.createExpenditruePage = function () {
                                var checkedPay = [];
                                var tmpObj = {};
                                var fundsTypePayList = [];
                                var m = 0;
                                angular.forEach(dialogScope.payList.results, function (pay) {
                                        if (pay.batchChecked) {
                                            checkedPay.push({
                                                currencyCode: qcObjectInArray(commonValue.currencies, 'currencyUuid', pay.currencyId).currencyCode,
                                                amount      : pay.strPayableAmount_amount
                                            });
                                            fundsTypePayList.push({
                                                    paymentUuid: pay.paymentUuid, fundsType: pay.fundsType
                                                }
                                            );
                                        }
                                    }
                                );
                                if (checkedPay.length > 0) {
                                    for (var i = 0; i < checkedPay.length; i++) {
                                        if (!tmpObj.hasOwnProperty(checkedPay[i].currencyCode)) {
                                            tmpObj[checkedPay[i].currencyCode] = checkedPay[i].amount;
                                        }
                                        else {
                                            tmpObj[checkedPay[i].currencyCode] = parseInt(tmpObj[checkedPay[i].currencyCode]) + parseInt(checkedPay[i].amount);
                                        }
                                    }
                                    for (var amount in tmpObj) {
                                        m++;
                                    }
                                    if (m > 1) {
                                        qcDialog.openMessage({msg: '所选款项币种不一致无法生成支出单,请重新选择'});
                                        return;
                                    }
                                    dialogScope.mergeParam = {
                                        orderUuid        : scope.orderUuid,
                                        paymentObjectUuid: dialogScope.selectedPaymentObject.paymentObjectUuid,
                                        fundsTypePayList : fundsTypePayList
                                    }
                                    var mergeParam = JSON.stringify(dialogScope.mergeParam);
                                    $window.open(urlConfig.mtourBaseUrl + '/mtour/mtourfinance/mergePrintOutPayPage?mergeParam=' + mergeParam + '');
                                }
                            }
                        }
                        ],
                        width      : 820,
                        height     : 350,
                        plain      : false
                    });
                }
            )
            ;
        }
    };
}]);

//122需求--start
mtour.directive('receiveOrderPagination', ['urlConfig', function (urlConfig) {
    return {
        restrict   : 'A',
        scope      : {
            pageInfo: '='
        },
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'pagination.html',
        link       : function (scope, ele, attrs) {
            scope.centerCount = 5;
            scope.goto = function (index) {
                scope.pageInfo.currentIndex = index;
                scope.$emit('payOrderPagination.change');
            };
            scope.confirmCurrentIndex = function () {
                if (scope.currentIndex > scope.calculatePageCount()) {
                    scope.currentIndex = scope.calculatePageCount();
                }
                if (scope.currentIndex < 1) {
                    scope.currentIndex = 1;
                }
                scope.pageInfo.currentIndex = scope.currentIndex;
                scope.$emit('payOrderPagination.change');
            };
            scope.pre = function () {
                if (scope.pageInfo.currentIndex > 1) {
                    scope.pageInfo.currentIndex -= 1;
                    scope.$emit('payOrderPagination.change');
                }
            };
            scope.next = function () {
                if (scope.pageInfo.currentIndex < scope.calculatePageCount()) {
                    scope.pageInfo.currentIndex = +(scope.pageInfo.currentIndex) + 1;
                    scope.$emit('payOrderPagination.change');
                }
            };
            scope.calculatePageCount = function () {
                return Math.ceil(scope.pageInfo.totalRowCount / scope.pageInfo.rowCount);
            };
            scope.getCenterList = function () {
                var startIndex = 2;
                var centerList = [];
                if (scope.pageInfo.currentIndex > ((scope.centerCount + 3) / 2)) {
                    startIndex = scope.pageInfo.currentIndex - ((scope.centerCount - 1) / 2);
                }
                startIndex = Math.min(startIndex, scope.calculatePageCount() - scope.centerCount);
                if (startIndex < 2) {
                    startIndex = 2;
                }
                var endIndex = startIndex + Math.min(scope.centerCount, scope.calculatePageCount() - startIndex);
                if (endIndex > 1) {
                    var i = startIndex;
                    while (i < endIndex) {
                        centerList.push(i);
                        i++;
                    }
                }
                return centerList;
            };
            scope.isShowPreEllipsis = function () {
                return (scope.getCenterList()[0] > 2);
            };
            scope.isShowNextEllipsis = function () {
                var centerList = scope.getCenterList();
                var length = centerList.length;
                return centerList[length - 1] < (scope.calculatePageCount() - 1);
            };

            scope.$watch('pageInfo.rowCount', function (oldValue, newValue) {
                if (oldValue && oldValue != newValue) {
                    scope.$emit('payOrderPagination.change', 'backFirstPage');
                }
            });

            scope.$watch('pageInfo.currentIndex', function () {
                scope.currentIndex = scope.pageInfo.currentIndex;
            });
        }
    };
}]);
//122需求--end

mtour.directive('showAttachment', ['urlConfig', 'qcDialog', function (urlConfig, qcDialog) {
    return {
        restrict: 'A',
        scope   : {
            attachment: '=showAttachment'
        },
        link    : function (scope, ele, attrs) {
            ele.on('click', function () {
                qcDialog.open({
                    templateUrl    : urlConfig.mtourHtmlTemplateUrl + 'attachment.html',
                    width          : 400,
                    closeByDocument: true,
                    closeByEscape  : true,
                    controller     : ['$scope', function (dialogScope) {
                        dialogScope.attachment = scope.attachment;
                        dialogScope.uploadFileUrl = urlConfig.mtourUploadFileUrl;
                    }]
                });
            });
        }
    };
}]);

//将日期字符串,截取前面的几个(默认10个)
mtour.filter('dateString', function () {
    return function (string, number) {
        if (!angular.isNumber(number)) {
            number = 10;
        }
        return string.substr(0, number);
    }
});
//将查出来的数组不合格的隐藏掉
mtour.filter('expenditureHide', function () {
    return function (input,id) {
        var returnArr=[];
        var ids=id;
        angular.forEach(input,function(rs){
            if(ids.indexOf(rs.paymentObjectUuid)>-1){
                returnArr.push(rs);
            }
        })
        return returnArr;
    }
});

mtour.run(['$rootScope', 'urlConfig', 'qcApi', 'commonValue', 'qcObjectInArray', function ($rootScope, urlConfig, qcApi, commonValue, qcObjectInArray) {
    function toCommonTypes(array, code, name) {
        var commonTypes = [];
        if (angular.isArray(array)) {
            angular.forEach(array, function (item) {
                var commonType = {};
                commonType[code] = item.code;
                commonType[name] = item.name;
                commonTypes.push(commonType);
            })
        }
        return commonTypes;
    };
    qcApi.post('common/getCurrentUserInfo').success(function (result) {
        $rootScope.userInfo = result.data;
        $rootScope.$broadcast('userinfo.loaded');

    });
    qcApi.post('common/getDictListByType', {dictType: 'person_sex'}).success(function (result) {
        commonValue.sexes = toCommonTypes(result.data, 'sexCode', 'sexName');
    });
    qcApi.post('common/getDictListByType', {dictType: 'traveler_type'}).success(function (result) {
        commonValue.travelerTypes = toCommonTypes(result.data, 'travelerTypeCode', 'travelerTypeName');
    });
    qcApi.post('common/getDictListByType', {dictType: 'new_visa_type'}).success(function (result) {
        commonValue.visaTypes = toCommonTypes(result.data, 'visaTypeCode', 'visaTypeName');
    });
    qcApi.post('common/getDictListByType', {dictType: 'papers_Type'}).success(function (result) {
        commonValue.credentialsTypes = toCommonTypes(result.data, 'credentialsTypeCode', 'credentialsTypeName');
    });
    qcApi.post('common/getDictListByType', {dictType: 'travel_agency_type'}).success(function (result) {
        commonValue.tourOperatorTypes = toCommonTypes(result.data, 'tourOperatorTypeCode', 'tourOperatorTypeName');
    });
    qcApi.post('common/getCurrency').success(function (result) {
        commonValue.currencies = result.data;
        angular.forEach(commonValue.currencies, function (currency) {
            currency.exchangeRate = currency.convertLowest;
        });
        commonValue.defaultCurrency = qcObjectInArray(commonValue.currencies, 'currencyCode', '¥');
    });
}]);