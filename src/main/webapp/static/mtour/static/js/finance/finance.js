var finance = angular.module('finance', ['mtour', 'qc.uploader']);
String.prototype.Trim = function() {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}
/*** 检查是否为数 ***/
String.prototype.isNumber = function() {
    var s = this.Trim();
    return (s.search(/^[+-]?[0-9.]*$/) >= 0);
}
finance.directive('searchConditionReceiveType', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-receiveType.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.receiveTypes = fixedValue.receiveType;
            scope.selectedReceiveTypes = ngModel.$modelValue;
        }
    };
}]);

finance.directive('searchConditionReceiveAge', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-receiveAge.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.receiveAges = fixedValue.accountReceivableAgeStatus;
            scope.selectedReceiveAges = ngModel.$modelValue;
        }
    };
}]);


finance.directive('searchConditionFundsType', ['urlConfig', 'qcApi', 'fixedValue', '$rootScope', function (urlConfig, qcApi, fixedValue, $rootScope) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-fundsType.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            var fundsType = angular.copy(fixedValue.fundsType);
            scope.fundsTypes = fundsType;
            scope.$on('userinfo.loaded', function () {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                if (scope.companyRoleCode == '0') {
                    scope.fundsTypes.splice(0, 1);
                }
            });

            scope.selectedFundsTypes = ngModel.$modelValue;
        }
    };
}]);
<!--modify by wlj at 2016.06.16-start-->
finance.directive('searchConditionCostAge', ['urlConfig', 'qcApi', 'fixedValue', '$rootScope', function (urlConfig, qcApi, fixedValue, $rootScope) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-cost-age.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            var fundsType = angular.copy(fixedValue.exportTable);
            scope.fundsTypes = fundsType;
            scope.$on('userinfo.loaded', function () {
                scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
                if (scope.companyRoleCode == '0') {
                    scope.fundsTypes.splice(0, 1);
                }
            });

            scope.selectedExportTypes = ngModel.$modelValue;
        }
    };
}]);

finance.directive('searchConditionTourOperatorOrChannelReceive', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-tour-operator-or-channel-receive.html',
        link       : function (scope, ele, attrs, ngModel) {
            scope.labelText = attrs.labelText;
            scope.tourOperatorChannelCategorys = fixedValue.tourOperatorChannelCategories;
            scope.selectedTourOperatorOrChannel = ngModel.$modelValue;

            scope.$on('selectedTourOperatorOrChannel.change', function () {
                ngModel.$setViewValue(scope.selectedTourOperatorOrChannel);
            });
        }
    };
}]);


finance.directive('searchConditionTourOperatorOrChannelPayment', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-tour-operator-or-channel-payment.html',
        link       : function (scope, ele, attrs, ngModel) {
            scope.labelText = attrs.labelText;
            scope.tourOperatorChannelCategorys = fixedValue.tourOperatorChannelCategories;
            scope.selectedTourOperatorOrChannel = ngModel.$modelValue;

            scope.$on('selectedTourOperatorOrChannel.change', function () {
                ngModel.$setViewValue(scope.selectedTourOperatorOrChannel);
            });
        }
    };
}]);

finance.directive('searchConditionChannel', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-channel.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }


            scope.selectedChannels = ngModel.$modelValue;
            scope.$on('selectedChannels.search', function ($e, filterText) {
                var channelCode= scope.channelTypeSelf.channelTypeCode;
                console.log("channelCode=="+channelCode);
                qcApi.post('common/getAgentinfoByTypeCode', {
                    channelTypeCode: channelCode,//@todo 修改为空
                    channelName    : filterText,//模糊搜索
                    count          : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.channels = result.data;
                });
            });
        }
    };
}]);

finance.directive('searchConditionTourOperator', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-tour-operator.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.selectedTourOperators = ngModel.$modelValue;
            scope.$on('selectedTourOperators.search', function ($e, filterText) {
                qcApi.post('common/getSupplierList', {
                    tourOperatorTypeCode: '',
                    tourOperatorName    : filterText,//模糊搜索
                    count               : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.tourTourOperators = result.data;
                });
            });
        }
    };
}]);
finance.directive('searchConditionPnr', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
	console.log("124");
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            labelText: '@'
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-pnr.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue('');
            }
            scope.pnr = ngModel.$modelValue;
            scope.confirm = function () {
                if (scope.pnr) {
                    ngModel.$setViewValue(scope.pnr);
                    scope.$emit(attrs.ngModel + '.confirm');
                }
            }
            scope.$on(attrs.ngModel + '.clear', function () {
                scope.pnr = '';
            });
        }
    };
}]);
finance.directive('searchConditionPnrOrder', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
        labelText: '@'
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-pnr-order.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue('');
            }
            scope.pnrOrder = ngModel.$modelValue;
            scope.confirm = function () {
                if (scope.pnrOrder) {
                    ngModel.$setViewValue(scope.pnrOrder);
                    scope.$emit(attrs.ngModel + '.confirm');
                }
            }
            scope.$on(attrs.ngModel + '.clear', function () {
                scope.pnrOrder = '';
            });
        }
    };
}]);

finance.directive('searchConditionAmountPeriod', ['$rootScope','urlConfig', 'qcApi', 'qcDialog', function ($rootScope,urlConfig, qcApi, qcDialog) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            labelText: '@'
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-amount-period.html',
        link       : function (scope, ele, attrs, ngModel) {
            scope.companyRoleCode = $rootScope.userInfo.companyRoleCode||scope.$root.userInfo.companyRoleCode;//0024美途国际用户//兼容火狐
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue({});
            }
            scope.selectedAmount = ngModel.$modelValue;

            scope.confirm = function () {
                if (scope.selectedAmount.minAmount && scope.selectedAmount.maxAmount) {
                    if (+scope.selectedAmount.minAmount > +scope.selectedAmount.maxAmount) {
                        qcDialog.openMessage({msg: '金额区间范围有误,请重新填写'});
                        return;
                    }
                }
                ngModel.$setViewValue(scope.selectedAmount);
                scope.$emit(attrs.ngModel + '.confirm');

            }
            scope.$on(attrs.ngModel + '.clear', function () {
                scope.selectedAmount = {
                    minAmount: '',
                    maxAmount: ''
                }
            });
        }
    };
}]);
/**
 * js成本单价  带有单位
 * search-Condition-Amount-period-price
 */
finance.directive('searchConditionAmountPeriodPrice', ['urlConfig', 'qcApi', 'qcDialog','commonValue','qcObjectInArray', 'fixedValue','$rootScope','qcMessage','$q', function (urlConfig, qcApi, qcDialog, commonValue, qcObjectInArray, fixedValue, $rootScope, qcMessage, $q) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
        labelText: '@'
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-amount-period-price.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue({});
            }
            scope.selectedAmountPrice = ngModel.$modelValue;
           /**
            * 取数据
            */
            var orderUuid;
            scope.companyRoleCode = $rootScope.userInfo.companyRoleCode;
            //默认货币设置
            scope.defaultCurrency = "币种";
//            scope.defaultCurrency = commonValue.defaultCurrency;
            //订单收款货币和汇率
            var defer = $q.defer();
            qcApi.post('order/getExistsCurrency', {orderId: "1", type: '2'}).success(function (result) {
            	scope.currencies = result.data;
            	scope.selectedCurrency = scope.currencies[0];
                defer.resolve();
            });
            scope.confirm = function () {
                if (scope.selectedAmountPrice.minAmount && scope.selectedAmountPrice.maxAmount) {
                    if (+scope.selectedAmountPrice.minAmount > +scope.selectedAmountPrice.maxAmount) {
                        qcDialog.openMessage({msg: '金额区间范围有误,请重新填写'});
                        return;
                    }
                }
                ngModel.$setViewValue(scope.selectedAmountPrice);
                scope.$emit(attrs.ngModel + '.confirm');

            }
            scope.$on(attrs.ngModel + '.clear', function () {
                scope.selectedAmountPrice = {
                	id:'',
                    minAmount: '',
                    maxAmount: ''
                }
            });
        }
    };
}]);

finance.directive('searchConditionText', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        scope      : {
            labelText: '@'
        },
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-text.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue('');
            }
            scope.text = ngModel.$modelValue;
            scope.confirm = function () {
                if (scope.text) {
                    ngModel.$setViewValue(scope.text);
                    scope.$emit(attrs.ngModel + '.confirm');
                }
            }
            scope.$on(attrs.ngModel + '.clear', function () {
                scope.text = '';
            });
        }
    };
}]);

finance.directive('searchConditionReceiver', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-receiver.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrdererss = modelValue;
            //});
            scope.selectedReceivers = ngModel.$modelValue;
            scope.$on('selectedReceivers.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.receivers = result.data;
                });

            });
        }
    };
}]);


finance.directive('searchConditionPayer', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-payer.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrdererss = modelValue;
            //});
            scope.selectedReceivers = ngModel.$modelValue;
            scope.$on('selectedReceivers.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    pnrValue: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.receivers = result.data;
                });

            });
        }
    };
}]);
finance.directive('searchConditionApplicant', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-applicant.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrdererss = modelValue;
            //});
            scope.selectedApplicants = ngModel.$modelValue;
            scope.$on('selectedApplicants.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.applicants = result.data;
                });

            });
        }
    };
}]);

finance.directive('searchConditionPnr1', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-pnr.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrdererss = modelValue;
            //});
            scope.selectedpnrs = ngModel.$modelValue;
            scope.$on('selectedpnrs.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.applicants = result.data;
                });

            });
        }
    };
}]);

finance.directive('searchConditionSale', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-sale.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrdererss = modelValue;
            //});
            scope.selectedSales = ngModel.$modelValue;
            scope.$on('selectedSales.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.sales = result.data;
                });

            });
        }
    };
}]);


finance.factory('joinChannelAndTourOperator', function () {
    return function (selectedChannels, selectedTourOperators) {
        var concatArray = [];
        angular.forEach(selectedChannels, function (channel) {
            concatArray.push({
                tourOperatorChannelCategoryCode: '2',
                tourOperatorOrChannelUuid      : channel.channelUuid
            });
        });
        angular.forEach(selectedTourOperators, function (tourOperator) {
            concatArray.push({
                tourOperatorChannelCategoryCode: '1',
                tourOperatorOrChannelUuid      : tourOperator.tourOperatorUuid
            });
        });
        return concatArray;
    }
});

finance.factory('joinAmount', function () {
    return function (amounts) {
        var amountStrings = [];
        angular.forEach(amounts, function (amount) {
            var string = '';
            if (amount.minAmount) {
                string += amount.minAmount;
            }
            string += '~';
            if (amount.maxAmount) {
                string += amount.maxAmount;
            }
            amountStrings.push(string);
        });
        return amountStrings.join(',');
    }
});
/**
 * 带单位的金钱拼接，如果有单位，则单位在第一位
 * 如果没有单位，则只显示数字
 */
finance.factory('joinAmountWithUnit', function () {
	return function (amounts) {
		var amountStrings = [];
		
		angular.forEach(amounts, function (amount) {
			var reStr={"id":"","minAmount":"","maxAmount":""};
			var string = '';
			if(amount.id){
				reStr.id=amount.id;
			}
			if (amount.minAmount) {
				reStr.minAmount=amount.minAmount;
			}
			if (amount.maxAmount) {
				reStr.maxAmount=amount.maxAmount;
			}
			amountStrings.push(reStr);
		});
		return amountStrings;
	}
});
finance.factory('joinDate', function () {
    return function (dates) {
        var dateStrings = [];
        angular.forEach(dates, function (date) {
            var string = '';
            if (date.startDate) {
                string += date.startDate;
            }
            string += '~';
            if (date.endDate) {
                string += date.endDate;
            }
            dateStrings.push(string);
        });
        return dateStrings.join(',');
    }
});

//90需求-美途
finance.directive('searchConditionPaystatus', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-paystatus.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.financePaymentStatus = fixedValue.financePaymentStatus;
            scope.selectedPaymentStatus = ngModel.$modelValue;
        }
    };
}]);

//90需求-二级搜索-下单人-系统所有用户
finance.directive('searchConditionOrderer', ['urlConfig', 'qcApi', function (urlConfig, qcApi) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'order/search-condition-orderer.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            //ngModel.$formatters.push(function (modelValue) {
            //    scope.selectedOrdererss = modelValue;
            //});
            scope.selectedOrderers = ngModel.$modelValue;
            scope.$on('selectedOrderers.search', function ($e, filterText) {
                qcApi.post('common/getUserByRoleType', {
                    roleType: '',
                    userName: filterText,//模糊搜索
                    count   : '10'//-1表示返回全部,必填
                }).success(function (result) {
                    scope.orderers = result.data;
                });

            });
        }
    };
}]);

//90需求-付款-订单列表-订单付款状态
finance.directive('searchConditionFinancepayOrderlistPaystatus', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-Condition-financePayOrderListPayStatus.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.financePayOrderListPayStatus = fixedValue.financePayOrderListPayStatus;
            scope.selectedfinancePayOrderListPayStatus = ngModel.$modelValue;
        }
    };
}]);

//122需求-strart
finance.directive('searchConditionReceiveStatus', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-receivetatus.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.financeReceiveStatus = fixedValue.financeReceiveStatus;
            scope.selectedReceiveStatus = ngModel.$modelValue;
        }
    };
}]);
//122订单收款状态
finance.directive('searchConditionReceiveorderReceivestatus', ['urlConfig', 'qcApi', 'fixedValue', function (urlConfig, qcApi, fixedValue) {
    return {
        restrict   : 'A',
        require    : '^ngModel',
        replace    : true,
        templateUrl: urlConfig.mtourHtmlTemplateUrl + 'finance/search-condition-financeReceive-orderlist-receivestatus.html',
        link       : function (scope, ele, attrs, ngModel) {
            if (!ngModel.$modelValue) {
                ngModel.$setViewValue([]);
            }
            scope.receive_orderReceiveStatus = fixedValue.orderReceiveStatus;
            scope.selectedfinanceReceiveOrderListReceiveStatus = ngModel.$modelValue;
        }
    };
}]);
