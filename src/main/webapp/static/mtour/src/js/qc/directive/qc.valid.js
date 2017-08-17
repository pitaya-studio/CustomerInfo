/**
 * @module qc
 * @version 2.0.0
 * @description 项目的验证
 * @requires angular.js,basic.js,qc.module.js
 */

/**
 * @todo 示例代码,待确认
 */
qc.directive('passwordValidate', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ngModelCtrl) {
            ngModelCtrl.$parsers.unshift(function (viewValue) {
                scope.pwdValidLength = (viewValue && viewValue.length >= 8 ? 'valid' : undefined);
                scope.pwdHasLetter = (viewValue && /[A-z]/.test(viewValue)) ? 'valid' : undefined;
                scope.pwdHasNumber = (viewValue && /\d/.test(viewValue)) ? 'valid' : undefined;

                if (scope.pwdValidLength && scope.pwdHasLetter && scope.pwdHasNumber) {
                    ngModelCtrl.$setValidity(attrs.ngModel, true);
                    //elm.$setValidity('pwd', true); //<-- 这样用也是没问题的
                    //这里还可以继续做其他的事情
                    return viewValue;
                } else {
                    ngModelCtrl.$setValidity(attrs.ngModel, false);
                    //elm.$setValidity('pwd', false); //<-- 这样用也是没问题的
                    //这里还可以继续做其他的事情
                    return undefined;
                }

            });
        }
    };
});

/**
 * @todo 示例代码,待确认
 */
qc.directive('validateCode', function () {
    return {
        require: 'ngModel',
        controller: function ($scope) {
        },
        link: function (scope, elm, attrs, ngModelCtrl) {
            var validOpts = scope.$eval(attrs.validateCode);
            if (!angular.isObject(scope.validOptions)) {
                scope.validOptions = {};
            }
            angular.extend(scope.validOptions, {'validateCode': validOpts});
            ngModelCtrl.$parsers.push(function (viewValue) {
                var codeLength;
                if (angular.isObject(validOpts)) {
                    codeLength = validOpts.length;
                } else {
                    codeLength = validOpts;
                }
                if (!codeLength) {
                    codeLength = 4;
                }
                var codeReg = new RegExp('^[a-zA-Z]{' + codeLength + '}$');
                var codeValidate = codeReg.test(viewValue);
                ngModelCtrl.$setValidity('validateCode', codeValidate);
                if (codeValidate) {
                    elm.tooltip('destroy');
                } else {
                    elm.tooltip({'container': 'body', 'html': true, 'title': validOpts.msg, 'trigger': 'focus'});
                    elm.tooltip('show');
                }

                return codeValidate ? viewValue : undefined;
            });
        }
    };
});


/**
 *  @description directive 邮箱验证
 */
qc.directive('qcValidRequired', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ngModelCtrl) {
            var validator = function (value) {
                if (ngModelCtrl.$isEmpty(value)) {
                    ngModelCtrl.$setValidity('qcValidRequired', false);
                    return false;
                } else {
                    ngModelCtrl.$setValidity('qcValidRequired', true);
                    return true;
                }
            };
            ngModelCtrl.$parsers.push(function (viewValue) {
                validator(viewValue);
                return viewValue;
            });
            elm.on('blur click focus', function () {
                validator(ngModelCtrl.$modelValue);
            });

            //上层scope 要求验证
            scope.$on('qcValid.check', function () {
                validator(ngModelCtrl.$modelValue);
            });
            //ngModelCtrl.$formatters.push(function (modeValue) {
            //    validator(modeValue);
            //    return modeValue;
            //});
            //attrs.$observe('qcValidRequired', function () {
            //    validator(ngModelCtrl.$viewValue);
            //});
        }
    };
});

/**
 *  @description directive 邮箱验证
 */
qc.directive('qcValidEmail', function () {
    return {
        require: 'ngModel',
        link: function (scope, elm, attrs, ngModelCtrl) {
            var validator = function (viewValue) {
                var emailReg = /^[a-zA-Z0-9_\\.]+@[a-zA-Z0-9-]+[\\.a-zA-Z]+$/;
                var valid = emailReg.test(viewValue);
                ngModelCtrl.$setValidity('qcValidEmail', (viewValue) ? valid : true);
                //return valid ? viewValue : undefined;
                return valid;
            };
            ngModelCtrl.$parsers.push(function (viewValue) {
                validator(viewValue);
                return viewValue;
            });
            ngModelCtrl.$formatters.push(function (modeValue) {
                validator(modeValue);
                return modeValue;
            });
        }
    };
});

/**
 *  @description directive 数字验证(可以输入小数点)
 */
qc.directive('qcValidNumber', function () {
    return {
        require: 'ngModel',
        scope: {
            qcValidNumber: '='
        },
        link: function (scope, elm, attrs, ngModelCtrl) {
            var defaultOptions = {
                intLength: 20,
                decimalLength: 10
            };
            var options = angular.extend(defaultOptions, scope.qcValidNumber);
            var validator = function (value) {
                var numberReg;
                if (options.decimalLength === 0) {
                    numberReg = new RegExp('^\\d{1,' + options.intLength + '}$');
                } else {
                    numberReg = new RegExp('^\\d{1,' + options.intLength + '}(\\.\\d{1,' + options.decimalLength + '}){0,1}$');
                }
                var valid = numberReg.test(value);
                ngModelCtrl.$setValidity('qcValidNumber', (value) ? valid : true);
                //return valid ? value : undefined;
                return valid;
            };
            ngModelCtrl.$parsers.push(function (viewValue) {
                validator(viewValue);
                return viewValue;
            });
            ngModelCtrl.$formatters.push(function (modeValue) {
                validator(modeValue);
                return modeValue;
            });
        }
    };
});