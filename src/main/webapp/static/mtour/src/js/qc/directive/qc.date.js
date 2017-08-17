/*global angular */
/*
 jQuery UI Datepicker plugin wrapper

 @note If ≤ IE8 make sure you have a polyfill for Date.toISOString()
 @param [ui-date] {object} Options to pass to $.fn.datepicker() merged onto uiDateConfig
 */


qc.constant('qcDateConfig', {
    changeYear: true,
    changeMonth: true,
    showMonthAfterYear: true,
    //monthNames:  [ "一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月" ],
    //monthNamesShort : [ "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二" ],
    dateFormat: 'yy-mm-dd'
});

qc.directive('qcDate', ['qcDateConfig', 'qcDateConverter', function (qcDateConfig, qcDateConverter) {
    'use strict';
    var options;
    options = {};
    angular.extend(options, qcDateConfig);
    return {
        require: '?ngModel',
        link: function (scope, element, attrs, controller) {
            var getOptions = function () {
                return angular.extend({}, qcDateConfig, scope.$eval(attrs.qcDate));
            };
            var initDateWidget = function () {
                var showing = false;
                var opts = getOptions();

                function setVal() {
                    var keys = ['Hours', 'Minutes', 'Seconds', 'Milliseconds'],
                        isDate = angular.isDate(controller.$modelValue),
                        preserve = {};

                    if (isDate) {
                        angular.forEach(keys, function (key) {
                            preserve[key] = controller.$modelValue['get' + key]();
                        });
                    }
                    controller.$setViewValue(element.datepicker('getDate'));

                    if (isDate) {
                        angular.forEach(keys, function (key) {
                            controller.$viewValue['set' + key](preserve[key]);
                        });
                    }
                }

                // If we have a controller (i.e. ngModelController) then wire it up
                if (controller) {

                    // Set the view value in a $apply block when users selects
                    // (calling directive user's function too if provided)
                    var _onSelect = opts.onSelect || angular.noop;
                    opts.onSelect = function (value, picker) {
                        scope.$apply(function () {
                            showing = true;
                            setVal();
                            _onSelect(value, picker);
                            element.blur();
                        });
                    };

                    var _beforeShow = opts.beforeShow || angular.noop;
                    opts.beforeShow = function (input, picker) {
                        showing = true;
                        _beforeShow(input, picker);
                    };

                    var _onClose = opts.onClose || angular.noop;
                    opts.onClose = function (value, picker) {
                        showing = false;
                        _onClose(value, picker);
                    };
                    element.off('blur.datepicker').on('blur.datepicker', function () {
                        if (!showing) {
                            scope.$apply(function () {
                                element.datepicker('setDate', element.datepicker('getDate'));
                                setVal();
                            });
                        }
                    });

                    // Update the date picker when the model changes
                    controller.$render = function () {
                        var date = controller.$modelValue;
                        if (angular.isDefined(date) && date !== null && !angular.isDate(date)) {
                            if (angular.isString(controller.$modelValue)) {
                                date = qcDateConverter.stringToDate(attrs.qcDateFormat, controller.$modelValue);
                            } else {
                                throw new Error('ng-Model value must be a Date, or a String object with a date formatter - currently it is a ' + typeof date + ' - use ui-date-format to convert it from a string');
                            }
                        }
                        element.datepicker('setDate', date);
                    };
                }
                // Check if the element already has a datepicker.
                if (element.data('datepicker')) {
                    // Updates the datepicker options
                    element.datepicker('option', opts);
                    element.datepicker('refresh');
                } else {
                    // Creates the new datepicker widget
                    element.datepicker(opts);

                    //Cleanup on destroy, prevent memory leaking
                    element.on('$destroy', function () {
                        element.datepicker('destroy');
                    });
                }

                if (controller) {
                    // Force a render to override whatever is in the input text box
                    controller.$render();
                }
            };
            // Watch for changes to the directives options
            scope.$watch(getOptions, initDateWidget, true);
            if (element.is('input')) {
                element.attr('readonly', true);
            }
        }
    };
}
]);
qc.directive('qcDatePeriod', ['qcDateConfig', 'qcDateConverter', '$timeout', 'qcDialog', function (qcDateConfig, qcDateConverter, $timeout, qcDialog) {
    var options;
    options = {};
    angular.extend(options, qcDateConfig);

    function getTemp(element, attrs) {
        var periodText = attrs.periodText;
        if (!periodText) {
            periodText = '起止日期:';
        }
        var temp = '' +
            '<span class="qc-dateperiod">' +
            '   <span ng-click="showCalendar()" ng-transclude=""></span>' +
            '   <div class="qc-dateperiod-container"  ng-show="show">' +
            '       <div qc-date="dateOptions" ng-model="datePeriod.Sdate" class="qc-dateperiod-calendar" qc-date-format="yy-mm-dd"></div>' +
            '       <div qc-date="dateOptions" ng-model="datePeriod.Edate" class="qc-dateperiod-calendar" qc-date-format="yy-mm-dd"></div>' +
            '       <div>' +
            '           <button class="btn btn-default" ng-click="confirm()">确定</button>' +
            '       </div>' +
            '   </div>' +
            '</span>';
        return temp;
    }

    return {
        require: '?ngModel',
        restrict: 'A',
        replace: false,
        transclude: true,
        scope: {},
        template: getTemp,
        link: function (scope, element, attrs, ngModel) {
            scope.showCalendar = function () {
                //element.find(".qc-dateperiod-container").show();
                //element.find(".qc-dateperiod-container").css("display", "inline-block");
                scope.show = true;
                scope.datePeriod = {};
            };
            ngModel.$formatters.push(function (modelValue) {
                scope.datePeriod = modelValue;
            });
            scope.confirm = function () {
                if (!scope.datePeriod.Sdate && !scope.datePeriod.Edate) {
                    return;
                }
                if (scope.datePeriod.Sdate > scope.datePeriod.Edate) {
                    qcDialog.openMessage({msg: '开始日期不能大于结束日期，请重新选择'});
                    return;
                }
                $timeout(function () {
                    if (ngModel.$viewValue == null) {
                        ngModel.$setViewValue({
                            startDate: scope.datePeriod.Sdate,
                            endDate: scope.datePeriod.Edate
                        });
                    }
                    else {
                        qcDialog.openMessage({msg: '只能选择一个时间段，请重新选择'});
                        return;
                    }
                    //element.find(".qc-dateperiod-container").hide();
                    scope.show = false;
                    scope.$emit(attrs.ngModel + '.change');
                });
            };

            scope.$on(attrs.ngModel + '.clear', function () {
                scope.datePeriod = {};
            });
            angular.element(document).on('click', function (e) {
                var $this = $(e.target);
                if (!element.has($this).length && !$this.parents('.ui-datepicker-header').length) {
                    //element.find(".qc-dateperiod-container").hide();
                    scope.show = false;
                }
            });
        }
    };
}
]);
qc.factory('qcDateConverter', ['qcDateFormatConfig', function (qcDateFormatConfig) {

    function dateToString(dateFormat, value) {
        dateFormat = dateFormat || qcDateFormatConfig;
        if (value) {
            if (dateFormat) {
                return jQuery.datepicker.formatDate(dateFormat, value);
            }

            if (value.toISOString) {
                return value.toISOString();
            }
        }
        return null;
    }

    function stringToDate(dateFormat, value) {
        dateFormat = dateFormat || qcDateFormatConfig;
        if (angular.isString(value)) {
            if (dateFormat) {
                return jQuery.datepicker.parseDate(dateFormat, value);
            }

            var isoDate = new Date(value);
            return isNaN(isoDate.getTime()) ? null : isoDate;
        }
        return null;
    }

    return {
        stringToDate: stringToDate,
        dateToString: dateToString
    };

}]);
qc.constant('qcDateFormatConfig', '');
qc.directive('qcDateFormat', ['qcDateConverter', function (qcDateConverter) {
    var directive = {
        require: 'ngModel',
        link: function (scope, element, attrs, modelCtrl) {
            var dateFormat = attrs.qcDateFormat;

            // Use the datepicker with the attribute value as the dateFormat string to convert to and from a string
            modelCtrl.$formatters.unshift(function (value) {
                return qcDateConverter.stringToDate(dateFormat, value);
            });

            modelCtrl.$parsers.push(function (value) {
                return qcDateConverter.dateToString(dateFormat, value);
            });

        }
    };

    return directive;
}]);
