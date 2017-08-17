qc.directive('qcInputAmount', ['$timeout', function ($timeout) {
    return {
        require : '^ngModel',
        restrict: 'A',
        scope   : {
            qcInputNumber: '='
        },
        link    : function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength    : 9,
                zero         : true,//是否可以为0
                decimalLength: 2
            };
            var options = angular.extend(defaultOptions, scope.qcInputNumber);
            var validator = function (value) {
                var numberReg;
                if (options.decimalLength === 0) {
                    numberReg = new RegExp('^\\d{1,' + options.intLength + '}$');
                } else {
                    numberReg = new RegExp('^\\d{1,' + options.intLength + '}(\\.\\d{0,' + options.decimalLength + '}){0,1}$');
                }
                var valid = numberReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue && viewValue !== 0 && viewValue !== '0') {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                } else {
                    if (options.maxValue) {
                        var newValue = parseFloat(viewValue);
                        if (newValue > options.maxValue) {
                            viewValue = options.maxValue;
                        }
                    }
                    if (!options.zero) {
                        var newValue = parseFloat(viewValue);
                        if (newValue === 0) {
                            viewValue = ngModel.$modelValue;
                        }
                    }
                    if (viewValue) {
                        if (viewValue.length > 1 && viewValue[0] === '0' && viewValue[1] != '.') {
                            viewValue = viewValue.substr(1, viewValue.length - 1);
                        }
                    }
                }
                ngModel.$viewValue = viewValue;
                //ngModel.$commitViewValue();
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);
//可以输入负数
qc.directive('qcInputNegativeAmount', ['$timeout', function ($timeout) {
    return {
        require : '^ngModel',
        restrict: 'A',
        scope   : {
            qcInputNumber: '='
        },
        link    : function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength    : 9,
                zero         : true,//是否可以为0
                decimalLength: 2
            };
            var options = angular.extend(defaultOptions, scope.qcInputNumber);
            var validator = function (value) {
                var numberReg;
                if (options.decimalLength === 0) {
                    numberReg = new RegExp('^-?\\d{1,' + options.intLength + '}$');
                } else {
                    numberReg = new RegExp('^-?\\d{1,' + options.intLength + '}(\\.\\d{0,' + options.decimalLength + '}){0,1}$');
                }
                var valid = numberReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue && viewValue !== 0 && viewValue !== '0') {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                } else {
                    if (options.maxValue) {
                        var newValue = parseFloat(viewValue);
                        if (newValue > options.maxValue) {
                            viewValue = options.maxValue;
                        }
                    }
                    if (!options.zero) {
                        var newValue = parseFloat(viewValue);
                        if (newValue === 0) {
                            viewValue = ngModel.$modelValue;
                        }
                    }
                    if (viewValue) {
                        if (viewValue.length > 1 && viewValue[0] === '0' && viewValue[1] != '.') {
                            viewValue = viewValue.substr(1, viewValue.length - 1);
                        }
                    }
                }
                ngModel.$viewValue = viewValue;
                //ngModel.$commitViewValue();
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);

//汇率输入框9+3
qc.directive('qcInputExchangeRate', ['$timeout', function ($timeout) {
    return {
        require : '^ngModel',
        restrict: 'A',
        scope   : {
            qcInputNumber: '='
        },
        link    : function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength    : 9,
                zero         : true,//是否可以为0
                decimalLength: 3
            };
            var options = angular.extend(defaultOptions, scope.qcInputNumber);
            var validator = function (value) {
                var numberReg;
                if (options.decimalLength === 0) {
                    numberReg = new RegExp('^\\d{1,' + options.intLength + '}$');
                } else {
                    numberReg = new RegExp('^\\d{1,' + options.intLength + '}(\\.\\d{0,' + options.decimalLength + '}){0,1}$');
                }
                var valid = numberReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue && viewValue !== 0 && viewValue !== '0') {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                } else {
                    if (options.maxValue) {
                        var newValue = parseFloat(viewValue);
                        if (newValue > options.maxValue) {
                            viewValue = options.maxValue;
                        }
                    }
                    if (!options.zero) {
                        var newValue = parseFloat(viewValue);
                        if (newValue === 0) {
                            viewValue = ngModel.$modelValue;
                        }
                    }
                    if (viewValue) {
                        if (viewValue.length > 1 && viewValue[0] === '0' && viewValue[1] != '.') {
                            viewValue = viewValue.substr(1, viewValue.length - 1);
                        }
                    }
                }
                ngModel.$viewValue = viewValue;
                //ngModel.$commitViewValue();
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);

qc.directive('qcInputInt', ['$timeout', function ($timeout) {
    return {
        require : '^ngModel',
        restrict: 'A',
        scope   : {
            qcInputInt: '='
        },
        link    : function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength    : 5,
                decimalLength: 0
            };
            var options = angular.extend(defaultOptions, scope.qcInputInt);
            var validator = function (value) {
                var numberReg;
                if (options.decimalLength === 0) {
                    numberReg = new RegExp('^\\d{0,' + options.intLength + '}$');
                } else {
                    numberReg = new RegExp('^\\d{0,' + options.intLength + '}(\\.\\d{0,' + options.decimalLength + '}){0,1}$');
                }
                var valid = numberReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue && viewValue !== 0 && viewValue !== '0') {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                }
                else {
                    if (options.maxValue) {
                        var newValue = parseInt(viewValue);
                        if (newValue > options.maxValue) {
                            viewValue = options.maxValue;
                        }
                    }
                    if (!options.zero) {
                        var newValue = parseInt(viewValue);
                        if (newValue === 0) {
                            viewValue = ngModel.$modelValue;
                        }
                    }
                    if (viewValue) {
                        if (viewValue.length > 1 && viewValue[0] === '0') {
                            viewValue = viewValue.substr(1, viewValue.length - 1);
                        }
                    }
                }

                ngModel.$viewValue = viewValue;
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);

qc.directive('qcInputEnglishName', ['$timeout', function ($timeout) {
    return {
        require : '^ngModel',
        restrict: 'A',
        link    : function (scope, element, attrs, ngModel) {
            var validator = function (value) {
                var nameReg;
                nameReg = new RegExp('^[a-zA-Z\.\\-_\(\)]{0,}$');
                var valid = nameReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue) {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                }
                ngModel.$viewValue = viewValue;
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);


qc.directive('qcInputCode', ['$timeout', function ($timeout) {
    return {
        require : '?ngModel',
        restrict: 'A',
        link    : function (scope, element, attrs, ngModel) {
            var validator = function (value) {
                var codeReg;
                codeReg = new RegExp('^[a-zA-Z0-9]{0,6}$');
                var valid = codeReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue) {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                }
                ngModel.$viewValue = viewValue;
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);

qc.directive('qcInputHour', ['$timeout', function ($timeout) {
    return {
        require : '?ngModel',
        restrict: 'A',
        link    : function (scope, element, attrs, ngModel) {
            var validator = function (value) {
                var codeReg1;
                codeReg1 = new RegExp('^[0-1]{0,1}[0-9]{0,1}$');
                var valid1 = codeReg1.test(value);
                var codeReg2 = new RegExp('^[2]{0,1}[0-3]{0,1}$');
                var valid2 = codeReg2.test(value);
                return valid1 || valid2;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue) {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                }
                ngModel.$viewValue = viewValue;
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);


qc.directive('qcInputMinute', ['$timeout', function ($timeout) {
    return {
        require : '?ngModel',
        restrict: 'A',
        link    : function (scope, element, attrs, ngModel) {
            var validator = function (value) {
                var codeReg;
                codeReg = new RegExp('^([0-5]{0,1}[0-9]{0,1})$');
                var valid = codeReg.test(value);
                return valid;
            };
            ngModel.$parsers.push(function (viewValue) {
                if (!viewValue) {
                    return viewValue;
                }
                var test = validator(viewValue);
                if (!test) {
                    viewValue = ngModel.$modelValue;
                }
                ngModel.$viewValue = viewValue;
                ngModel.$render();
                return viewValue;
            });
        }
    };
}]);

qc.directive('input', ['$timeout', function ($timeout) {
    return {
        require : '?ngModel',
        restrict: 'E',
        link    : function (scope, element, attrs, ngModel) {
            var maxLength = 50;
            if (ngModel) {
                ngModel.$parsers.push(function (viewValue) {
                    if (!viewValue) {
                        return viewValue;
                    }
                    if (viewValue.length > maxLength) {
                        viewValue = viewValue.substr(0, maxLength);
                        ngModel.$viewValue = viewValue;
                        ngModel.$render();
                    }
                    return viewValue;
                });
            }
        }
    };
}]);