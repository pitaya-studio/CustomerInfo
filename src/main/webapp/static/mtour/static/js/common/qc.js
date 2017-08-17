(function (window, document, undefined) {
    /**
     * @description 项目名称
     */
    window.projectName = 'M-tour';
})(window, document);
/*华丽的分隔线*/
(function (window, document, undefined) {

    /**
     * @description 让string实例对象能够去除前后空格
     * @function
     * @global
     * @return {String} 去除前后空格的字符串
     */
    if (!String.prototype.trim) {
        String.prototype.trim = function () {
            return this.replace(/^\s+|\s+$/g, '');
        };
    }
    
    if(!Number.prototype.floatAdd){
    	//加    
    	Number.prototype.floatAdd = function floatAdd(arg2){
    		var arg1 = this;
			 var r1,r2,m;    
			 try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}    
			 try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}    
			 m=Math.pow(10,Math.max(r1,r2));    
			 return (arg1*m+arg2*m)/m;    
    	}
    }

    if(!Number.prototype.floatSubtract){
        //减
        Number.prototype.floatSubtract = function(arg2){
            var arg1 = this;
            var r1,r2,m,n;
            try{r1=arg1.toString().split(".")[1].length}catch(e){r1=0}
            try{r2=arg2.toString().split(".")[1].length}catch(e){r2=0}
            m=Math.pow(10,Math.max(r1,r2));
            //动态控制精度长度
            n=(r1>=r2)?r1:r2;
            return ((arg1*m-arg2*m)/m).toFixed(n);
        }
    }

    if(!Number.prototype.floatMul){
        //减
        Number.prototype.floatMul = function(arg2){
            var arg1 = this;
            var m=0,s1=arg1.toString(),s2=arg2.toString();
            try{m+=s1.split(".")[1].length}catch(e){}
            try{m+=s2.split(".")[1].length}catch(e){}
            return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m);
        }
    }

    if(!String.prototype.milliFormat){
        String.prototype.milliFormat = function(){
            var s = this;
            var isFloat = true;
            var minusSign = false;
            if(/^\-/.test(s)){
                minusSign = true;
                s = s.substring(1);
            }
            if(/[^0-9\.]/.test(s)) return "invalid value";
            s=s.replace(/^(\d*)$/,"$1.");
            s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
            s=s.replace(".",",");
            var re=/(\d)(\d{3},)/;
            while(re.test(s)){
                s=s.replace(re,"$1,$2");
            }
            if(isFloat){
                s=s.replace(/,(\d\d)$/,".$1");
            }else{
                s=s.replace(/,(\d\d)$/,"");
            }
            if(minusSign){
                s= '-' + s;
            }
            return s.replace(/^\./,"0.");
        }
    }
    
    /**
     * @description 从数组中返回指定位置的对象或者值
     * @function
     * @global
     * @param elt {Object|String|Number|Array|Boolean|...} 需要查找的值
     * @return {Number} 数组中第一个满足和查找值相等的索引
     */
    if (!Array.prototype.indexOf) {
        Array.prototype.indexOf = function (elt) {
            var len = this.length ? this.length : 0;
            var from = Number(arguments[1]) || 0;
            from = (from < 0) ? Math.ceil(from) : Math.floor(from);
            if (from < 0) {
                from += len;
            }
            for (; from < len; from++) {
                if (from in this && this[from] === elt) {
                    return from;
                }
            }
            return -1;
        };
    }

    /**
     * @description 删除数组中指定的对象或者值
     * @function
     * @global
     * @param  item {Object|String|Number|Array|Boolean|...} 需要删除的对象或者值
     * @return {Boolean} 是否有对应的对象或者值
     */
    if (!Array.prototype.remove) {
        Array.prototype.remove = function (item) {
            var index = this.indexOf(item);
            if (index === -1) {
                return false;
            }
            for (var i = 0, n = 0; i < this.length; i++) {
                if (this[i] !== this[index]) {
                    this[n++] = this[i];
                }
            }
            this.length -= 1;
            return true;
        };
    }

    /**
     *@description 数组的分页方法
     * @function
     * @global
     * @param rowCount {Number:每页的行数}
     * @param index {Number:第几页} 默认第0页
     */
    if (!Array.prototype.paging) {
        Array.prototype.paging = function (rowCount, index) {
            index = (+index);
            if (!index) {
                index = 0;
            }
            return this.slice(rowCount * index, rowCount * (index + 1));
        };
    }

    /**
     * @description 按照指定的过滤方式筛选出数据
     * @function
     * @global
     * @param fun {Function(item,index,array):Boolean} 筛选function,如果返回值是true,则通过筛选
     * @return {Array} 通过筛选的新数组
     */
    if (!Array.prototype.filter) {
        Array.prototype.filter = function (fun) {
            var t = Object(this);
            var len = t.length ? this.length : 0;
            if (typeof fun !== "function") {
                throw new TypeError();
            }
            var res = [];
            var thisp = arguments[1];
            for (var i = 0; i < len; i++) {
                if (i in t) {
                    var val = t[i];
                    if (fun.call(thisp, val, i, t)) {
                        res.push(val);
                    }
                }
            }
            return res;
        };
    }
})(window, document);
/*华丽的分隔线*/
/**
 * @description：qc module,定义项目必要的angular组件
 * @global
 * @requires jquery.js,jquery-ui.js,angular.js,basic.js
 */
var qc = angular.module('qc', []);

/**
 * @description api服务端信息.发布时,根据服务器不同需要重写
 */
qc.config(['$httpProvider', '$provide', function ($httpProvider, $provide) {
    //if (!$httpProvider.defaults.headers.get) {
    //    $httpProvider.defaults.headers.get = {};
    //}
    //$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    //$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
    //$httpProvider.defaults.headers.common['Pragma'] = 'no-cache';
    //$httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
    $provide.constant('urlConfig', {
        mtourApiUrl: window.mtourApiUrl ? window.mtourApiUrl : 'http://localhost:8080/trekiz_wholesaler_tts/a/mtour/',
        mtourStaticUrl: window.mtourStaticUrl ? window.mtourStaticUrl : 'http://localhost:8080/trekiz_wholesaler_tts/static/mtour/static',
        mtourHtmlTemplateUrl: window.mtourHtmlTemplateUrl ? window.mtourHtmlTemplateUrl : 'http://localhost:8080/trekiz_wholesaler_tts/static/mtour/html/',
        mtourLoginUrl: window.mtourLoginUrl ? window.mtourLoginUrl : 'http://localhost:8080/trekiz_wholesaler_tts/a/login',
        mtourUploadFileUrl: window.mtourUploadFileUrl ? window.mtourUploadFileUrl : 'http://localhost:8080//trekiz_wholesaler_tts/a/sys/docinfo/download/',
        mtourLogoutUrl: window.mtourLogoutUrl ? window.mtourLogoutUrl : 'http://localhost:8080/trekiz_wholesaler_tts/a/logout',
        mtourBaseUrl: window.mtourBaseUrl ? window.mtourBaseUrl : 'http://localhost:8080/trekiz_wholesaler_tts/a/'
    });
}]);

/*华丽的分隔线*/
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
                    ngModel.$setViewValue({
                        startDate: scope.datePeriod.Sdate,
                        endDate: scope.datePeriod.Edate
                    });
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

/*华丽的分隔线*/
/**
 * Implementing Drag and Drop functionality in AngularJS is easier than ever.
 * Demo: http://codef0rmer.github.com/angular-dragdrop/
 *
 * @version 1.0.11
 *
 * (c) 2013 Amit Gharat a.k.a codef0rmer <amit.2006.it@gmail.com> - amitgharat.wordpress.com
 */

(function (window, angular, $, undefined) {
    qc.service('ngDragDropService', ['$timeout', '$parse', function ($timeout, $parse) {
        this.draggableScope = null;
        this.droppableScope = null;

        this.callEventCallback = function (scope, callbackName, event, ui) {
            if (!callbackName) {
                return;
            }
            var objExtract = extract(callbackName),
                callback = objExtract.callback,
                constructor = objExtract.constructor,
                args = [event, ui].concat(objExtract.args);

            // call either $scoped method i.e. $scope.dropCallback or constructor's method i.e. this.dropCallback.
            // Removing scope.$apply call that was performance intensive (especially onDrag) and does not require it
            // always. So call it within the callback if needed.
            return (scope[callback] || scope[constructor][callback]).apply(scope, args);

            function extract(callbackName) {
                var atStartBracket = callbackName.indexOf('(') !== -1 ? callbackName.indexOf('(') : callbackName.length,
                    atEndBracket = callbackName.lastIndexOf(')') !== -1 ? callbackName.lastIndexOf(')') : callbackName.length,
                    args = callbackName.substring(atStartBracket + 1, atEndBracket), // matching function arguments inside brackets
                    constructor = callbackName.match(/^[^.]+.\s*/)[0].slice(0, -1); // matching a string upto a dot to check ctrl as syntax
                constructor = scope[constructor] && typeof scope[constructor].constructor === 'function' ? constructor : null;

                return {
                    callback: callbackName.substring(constructor && constructor.length + 1 || 0, atStartBracket),
                    args: $.map(args && args.split(',') || [], function (item) {
                        return [$parse(item)(scope)];
                    }),
                    constructor: constructor
                };
            }
        };

        this.invokeDrop = function ($draggable, $droppable, event, ui) {
            var dragModel = '',
                dropModel = '',
                dragSettings = {},
                dropSettings = {},
                qc_pos = null,
                dragItem = {},
                dropItem = {},
                dragModelValue,
                dropModelValue,
                $droppableDraggable = null,
                droppableScope = this.droppableScope,
                draggableScope = this.draggableScope;

            dragModel = $draggable.ngattr('ng-model');
            dropModel = $droppable.ngattr('ng-model');
            dragModelValue = draggableScope.$eval(dragModel);
            dropModelValue = droppableScope.$eval(dropModel);

            $droppableDraggable = $droppable.find('[qc-draggable]:last,[data-qc-draggable]:last');
            dropSettings = droppableScope.$eval($droppable.attr('qc-droppable') || $droppable.attr('data-qc-droppable')) || [];
            dragSettings = draggableScope.$eval($draggable.attr('qc-draggable') || $draggable.attr('data-qc-draggable')) || [];

            // Helps pick up the right item
            dragSettings.index = this.fixIndex(draggableScope, dragSettings, dragModelValue);
            dropSettings.index = this.fixIndex(droppableScope, dropSettings, dropModelValue);

            qc_pos = angular.isArray(dragModelValue) ? dragSettings.index : null;
            dragItem = angular.isArray(dragModelValue) ? dragModelValue[qc_pos] : dragModelValue;

            if (dragSettings.deepCopy) {
                dragItem = angular.copy(dragItem);
            }

            if (angular.isArray(dropModelValue) && dropSettings && dropSettings.index !== undefined) {
                dropItem = dropModelValue[dropSettings.index];
            } else if (!angular.isArray(dropModelValue)) {
                dropItem = dropModelValue;
            } else {
                dropItem = {};
            }

            if (dropSettings.deepCopy) {
                dropItem = angular.copy(dropItem);
            }

            if (dragSettings.animate === true) {
                this.move($draggable, $droppableDraggable.length > 0 ? $droppableDraggable : $droppable, null, 'fast', dropSettings, null);
                this.move($droppableDraggable.length > 0 && !dropSettings.multiple ? $droppableDraggable : [], $draggable.parent('[qc-droppable],[data-qc-droppable]'), qc.startXY, 'fast', dropSettings, angular.bind(this, function () {
                    $timeout(angular.bind(this, function () {
                        // Do not move this into move() to avoid flickering issue
                        $draggable.css({'position': 'relative', 'left': '', 'top': ''});
                        // Angular v1.2 uses ng-hide to hide an element not display property
                        // so we've to manually remove display:none set in this.move()
                        $droppableDraggable.css({
                            'position': 'relative',
                            'left': '',
                            'top': '',
                            'display': $droppableDraggable.css('display') === 'none' ? '' : $droppableDraggable.css('display')
                        });

                        this.mutateDraggable(draggableScope, dropSettings, dragSettings, dragModel, dropModel, dropItem, $draggable);
                        this.mutateDroppable(droppableScope, dropSettings, dragSettings, dropModel, dragItem, qc_pos);
                        this.callEventCallback(droppableScope, dropSettings.onDrop, event, ui);
                    }));
                }));
            } else {
                $timeout(angular.bind(this, function () {
                    this.mutateDraggable(draggableScope, dropSettings, dragSettings, dragModel, dropModel, dropItem, $draggable);
                    this.mutateDroppable(droppableScope, dropSettings, dragSettings, dropModel, dragItem, qc_pos);
                    this.callEventCallback(droppableScope, dropSettings.onDrop, event, ui);
                }));
            }
        };

        this.move = function ($fromEl, $toEl, toPos, duration, dropSettings, callback) {
            if ($fromEl.length === 0) {
                if (callback) {
                    window.setTimeout(function () {
                        callback();
                    }, 300);
                }
                return false;
            }

            var zIndex = $fromEl.css('z-index'),
                fromPos = $fromEl[dropSettings.containment || 'offset'](),
                displayProperty = $toEl.css('display'), // sometimes `display` is other than `block`
                hadNgHideCls = $toEl.hasClass('ng-hide');

            if (toPos === null && $toEl.length > 0) {
                if (($toEl.attr('qc-draggable') || $toEl.attr('data-qc-draggable')) !== undefined && $toEl.ngattr('ng-model') !== undefined && $toEl.is(':visible') && dropSettings && dropSettings.multiple) {
                    toPos = $toEl[dropSettings.containment || 'offset']();
                    if (dropSettings.stack === false) {
                        toPos.left += $toEl.outerWidth(true);
                    } else {
                        toPos.top += $toEl.outerHeight(true);
                    }
                } else {
                    // Angular v1.2 uses ng-hide to hide an element 
                    // so we've to remove it in order to grab its position
                    if (hadNgHideCls) {
                        $toEl.removeClass('ng-hide');
                    }
                    toPos = $toEl.css({
                        'visibility': 'hidden',
                        'display': 'block'
                    })[dropSettings.containment || 'offset']();
                    $toEl.css({'visibility': '', 'display': displayProperty});
                }
            }

            $fromEl.css({'position': 'absolute', 'z-index': 9999})
                .css(fromPos)
                .animate(toPos, duration, function () {
                    // Angular v1.2 uses ng-hide to hide an element
                    // and as we remove it above, we've to put it back to
                    // hide the element (while swapping) if it was hidden already
                    // because we remove the display:none in this.invokeDrop()
                    if (hadNgHideCls) {
                        $toEl.addClass('ng-hide');
                    }
                    $fromEl.css('z-index', zIndex);
                    if (callback) {
                        callback();
                    }
                });
        };

        this.mutateDroppable = function (scope, dropSettings, dragSettings, dropModel, dragItem, qc_pos) {
            var dropModelValue = scope.$eval(dropModel);

            scope.dndDragItem = dragItem;

            if (angular.isArray(dropModelValue)) {
                if (dropSettings && dropSettings.index >= 0) {
                    dropModelValue[dropSettings.index] = dragItem;
                } else {
                    dropModelValue.push(dragItem);
                }
                if (dragSettings && dragSettings.placeholder === true) {
                    dropModelValue[dropModelValue.length - 1]['qc_pos'] = qc_pos;
                }
            } else {
                $parse(dropModel + ' = dndDragItem')(scope);
                if (dragSettings && dragSettings.placeholder === true) {
                    dropModelValue['qc_pos'] = qc_pos;
                }
            }
        };

        this.mutateDraggable = function (scope, dropSettings, dragSettings, dragModel, dropModel, dropItem, $draggable) {
            var isEmpty = angular.equals(dropItem, {}) || !dropItem,
                dragModelValue = scope.$eval(dragModel);

            scope.dndDropItem = dropItem;

            if (dragSettings && dragSettings.placeholder) {
                if (dragSettings.placeholder !== 'keep') {
                    if (angular.isArray(dragModelValue) && dragSettings.index !== undefined) {
                        dragModelValue[dragSettings.index] = dropItem;
                    } else {
                        $parse(dragModel + ' = dndDropItem')(scope);
                    }
                }
            } else {
                if (angular.isArray(dragModelValue)) {
                    if (isEmpty) {
                        if (dragSettings && ( dragSettings.placeholder !== true && dragSettings.placeholder !== 'keep' )) {
                            dragModelValue.splice(dragSettings.index, 1);
                        }
                    } else {
                        dragModelValue[dragSettings.index] = dropItem;
                    }
                } else {
                    // Fix: LIST(object) to LIST(array) - model does not get updated using just scope[dragModel] = {...}
                    // P.S.: Could not figure out why it happened
                    $parse(dragModel + ' = dndDropItem')(scope);
                    if (scope.$parent) {
                        $parse(dragModel + ' = dndDropItem')(scope.$parent);
                    }
                }
            }

            $draggable.css({'z-index': '', 'left': '', 'top': ''});
        };

        this.fixIndex = function (scope, settings, modelValue) {
            if (settings.applyFilter && angular.isArray(modelValue) && modelValue.length > 0) {
                var dragModelValueFiltered = scope[settings.applyFilter](),
                    lookup = dragModelValueFiltered[settings.index],
                    actualIndex;

                modelValue.forEach(function (item, i) {
                    if (angular.equals(item, lookup)) {
                        actualIndex = i;
                    }
                });

                return actualIndex;
            }

            return settings.index;
        };
    }]).directive('qcDraggable', ['ngDragDropService', function (ngDragDropService) {
        return {
            require: '?qcDroppable',
            restrict: 'A',
            link: function (scope, element, attrs) {
                var dragSettings, qcOptions, zIndex;
                var updateDraggable = function (newValue, oldValue) {
                    if (newValue) {
                        dragSettings = scope.$eval(element.attr('qc-draggable') || element.attr('data-qc-draggable')) || {};
                        qcOptions = scope.$eval(attrs.qcOptions) || {};
                        element
                            .draggable({disabled: false})
                            .draggable(qcOptions)
                            .draggable({
                                start: function (event, ui) {
                                    ngDragDropService.draggableScope = scope;
                                    zIndex = angular.element(qcOptions.helper ? ui.helper : this).css('z-index');
                                    angular.element(qcOptions.helper ? ui.helper : this).css('z-index', 9999);
                                    qc.startXY = angular.element(this)[dragSettings.containment || 'offset']();
                                    ngDragDropService.callEventCallback(scope, dragSettings.onStart, event, ui);
                                },
                                stop: function (event, ui) {
                                    angular.element(qcOptions.helper ? ui.helper : this).css('z-index', zIndex);
                                    ngDragDropService.callEventCallback(scope, dragSettings.onStop, event, ui);
                                },
                                drag: function (event, ui) {
                                    ngDragDropService.callEventCallback(scope, dragSettings.onDrag, event, ui);
                                }
                            });
                    } else {
                        element.draggable({disabled: true});
                    }
                };
                scope.$watch(function () {
                    return scope.$eval(attrs.drag);
                }, updateDraggable);
                updateDraggable();

                element.on('$destroy', function () {
                    element.draggable({disabled: true}).draggable('destroy');
                });
            }
        };
    }]).directive('qcDroppable', ['ngDragDropService', '$q', function (ngDragDropService, $q) {
        return {
            restrict: 'A',
            priority: 1,
            link: function (scope, element, attrs) {
                var dropSettings;
                var updateDroppable = function (newValue, oldValue) {
                    if (newValue) {
                        dropSettings = scope.$eval(angular.element(element).attr('qc-droppable') || angular.element(element).attr('data-qc-droppable')) || {};
                        element
                            .droppable({disabled: false})
                            .droppable(scope.$eval(attrs.qcOptions) || {})
                            .droppable({
                                over: function (event, ui) {
                                    ngDragDropService.callEventCallback(scope, dropSettings.onOver, event, ui);
                                },
                                out: function (event, ui) {
                                    ngDragDropService.callEventCallback(scope, dropSettings.onOut, event, ui);
                                },
                                drop: function (event, ui) {
                                    var beforeDropPromise = null;

                                    if (dropSettings.beforeDrop) {
                                        beforeDropPromise = ngDragDropService.callEventCallback(scope, dropSettings.beforeDrop, event, ui);
                                    } else {
                                        beforeDropPromise = (function () {
                                            var deferred = $q.defer();
                                            deferred.resolve();
                                            return deferred.promise;
                                        })();
                                    }

                                    beforeDropPromise.then(angular.bind(this, function () {
                                        if (angular.element(ui.draggable).ngattr('ng-model') && attrs.ngModel) {
                                            ngDragDropService.droppableScope = scope;
                                            ngDragDropService.invokeDrop(angular.element(ui.draggable), angular.element(this), event, ui);
                                        } else {
                                            ngDragDropService.callEventCallback(scope, dropSettings.onDrop, event, ui);
                                        }
                                    }), function () {
                                        ui.draggable.css({left: '', top: ''});
                                    });
                                }
                            });
                    } else {
                        element.droppable({disabled: true});
                    }
                };

                scope.$watch(function () {
                    return scope.$eval(attrs.drop);
                }, updateDroppable);
                updateDroppable();

                element.on('$destroy', function () {
                    element.droppable({disabled: true}).droppable('destroy');
                });
            }
        };
    }]);

    angular.element.prototype.ngattr = function (name, value) {
        var element = angular.element(this).get(0);

        return element.getAttribute(name) || element.getAttribute('data-' + name);
    };
})(window, window.angular, window.jQuery);

/*华丽的分隔线*/
qc.directive('qcDropdown', function () {
    return {
        require: '^ngModel',
        restrict: 'AE',
        scope: {
            filterExpression: '@',//当没有过滤表达式的时候,不过滤,也不显示过滤文本框
            items: '=',//绑定的下拉列表
            multiple: '@',
            listWidth: '=',
            listMaxHeight: '='
        },
        replace: true,
        template: function (ele, attrs) {
            var htmlArray = [];
            htmlArray.push('<div class="qc-dropdown">');
            var dropdownTextTemplate = '<div class="qc-dropdown-text-container" ng-click="showList()">';
            var dropText = '请选择';
            if (attrs.dropdownText) {
                dropText = attrs.dropdownText;
            }
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="' + dropText + '">' + dropText + '</span>';
            dropdownTextTemplate += '<span class="qc-dropdown-anchor">▼</span>';
            dropdownTextTemplate += '</div>';

            htmlArray.push(dropdownTextTemplate);
            //htmlArray.push('<input class="qc-dropdown-filter" ng-model="filterText" ng-hide="!active || !filterExpression">');

            htmlArray.push('<div class="qc-dropdown-container" ng-if="active"   ng-style="{\'width\':listWidth}">');


            if (attrs.listMaxHeight) {
                htmlArray.push('<ul class="qc-dropdown-list qc-scroll" ng-style="{\'max-height\':listMaxHeight}">');
            } else {
                htmlArray.push('<ul class="qc-dropdown-list" ng-style="{\'max-height\':listMaxHeight}">');
            }

            htmlArray.push('<li class="qc-dropdown-list-search" ng-hide="!filterExpression"><div  class="qc-dropdown-filter-container"><input class="qc-dropdown-filter" ng-model="filterText" ></div><em></em></li>');

            var filterString = '';
            if (attrs.filterExpression) {
                filterString = ' | qcPropsFilter:\'' + attrs.filterExpression + '\':filterText';
            }
            var activeItemExpression = '(qcDropdownItem==selectedItem)';
            if (attrs.multiple === 'multiple') {
                activeItemExpression = '(selectedItem.indexOf(qcDropdownItem)>-1)';
            }


            htmlArray.push('<li class="qc-dropdown-item" ng-class="{active:' + activeItemExpression + '}" ' +
            'ng-click="select(qcDropdownItem)" ng-repeat="qcDropdownItem in items' + filterString + '">' + attrs.itemText +
            '</li>');
            htmlArray.push('</ul>');
            htmlArray.push('</div>');
            htmlArray.push('</div>');
            return htmlArray.join('');
        },
        link: function (scope, ele, attrs, ngModel) {
            //ngModel的modelValue变动后通知下拉框
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            ngModel.$parsers.unshift(function (viewValue) {
                if (scope.multiple !== 'multiple') {
                    scope.active = false;
                    scope.selectedItem = viewValue;
                    scope.clearFilter();
                    return viewValue;
                } else {
                    if (ngModel.$modelValue.indexOf(viewValue) === -1) {
                        ngModel.$modelValue.push(viewValue);
                    } else {
                        ngModel.$modelValue.remove(viewValue);
                    }
                    return ngModel.$modelValue;
                }

            });
            scope.showList = function () {
                scope.active = true;
                //active 变更后视图需要刷新,所以不要立即让过滤文本获取焦点,等待视图更新完成后再进行焦点设置
                setTimeout(function () {
                    ele.find('.qc-dropdown-filter').focus();
                }, 100);
            };
            scope.select = function (qcDropdownItem) {
                ngModel.$setViewValue(qcDropdownItem);
                scope.$emit(attrs.ngModel + '.change');
            };
            scope.clearFilter = function () {
                scope.filterText = '';
            };
            angular.element(document).on('click', function (e) {
                var $this = $(e.target);
                if (!ele.has($this).length) {
                    scope.$apply(function () {
                        scope.active = false;
                        scope.clearFilter();
                    });
                }
            });
        }
    };
});

qc.directive('qcDropdownExport', function () {
    return {
        require: '^ngModel',
        restrict: 'AE',
        scope: {
            filterExpression: '@',//当没有过滤表达式的时候,不过滤,也不显示过滤文本框
            items: '=',//绑定的下拉列表
            multiple: '@',
            listWidth: '=',
            listMaxHeight: '='
        },
        replace: true,
        template: function (ele, attrs) {
            var htmlArray = [];
            htmlArray.push('<div class="qc-dropdown">');
            var dropdownTextTemplate = '<div class="qc-dropdown-text-container" ng-click="showList()">';
            var dropText = '请选择';
            if (attrs.dropdownText) {
                dropText = attrs.dropdownText;
            }
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="' + dropText + '">' + dropText + '</span>';
            dropdownTextTemplate += '<span class="qc-dropdown-anchor">▼</span>';
            dropdownTextTemplate += '</div>';

            htmlArray.push(dropdownTextTemplate);
            //htmlArray.push('<input class="qc-dropdown-filter" ng-model="filterText" ng-hide="!active || !filterExpression">');

            htmlArray.push('<div style="position:absolute;width:78px;left:-7px;border-radius:4px;" class="qc-dropdown-container" ng-if="active">');


            if (attrs.listMaxHeight) {
                htmlArray.push('<ul style="border-radius: 4px;background-color: white;" class="qc-dropdown-list qc-scroll" ng-style="{\'max-height\':listMaxHeight}">');
            } else {
                htmlArray.push('<ul style="border-radius: 4px;background-color: white;" class="qc-dropdown-list" ng-style="{\'max-height\':listMaxHeight}">');
            }

            htmlArray.push('<li  class="qc-dropdown-list-search" ng-hide="!filterExpression"><div  class="qc-dropdown-filter-container"><input class="qc-dropdown-filter" ng-model="filterText" ></div><em></em></li>');

            var filterString = '';
            if (attrs.filterExpression) {
                filterString = ' | qcPropsFilter:\'' + attrs.filterExpression + '\':filterText';
            }
            var activeItemExpression = '(qcDropdownItem==selectedItem)';
            if (attrs.multiple === 'multiple') {
                activeItemExpression = '(selectedItem.indexOf(qcDropdownItem)>-1)';
            }


            htmlArray.push('<li style="border: none;margin: 0;padding:3px 8px;"  class="qc-dropdown-item" ng-class="{active:' + activeItemExpression + '}" ' +
            'ng-click="select1(qcDropdownItem)" whichone="qcDropdownItem"  href="javascript:void(0)"  show-Finance-Income   ng-repeat="qcDropdownItem in items' + filterString + '">' + attrs.itemText +
            '</li>');
            htmlArray.push('</ul>');
            htmlArray.push('</div>');
            htmlArray.push('</div>');
            return htmlArray.join('');
        },
        link: function (scope, ele, attrs, ngModel) {
            //ngModel的modelValue变动后通知下拉框
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            ngModel.$parsers.unshift(function (viewValue) {
                if (scope.multiple !== 'multiple') {
                    scope.active = false;
                    scope.selectedItem = viewValue;
                    scope.clearFilter();
                    return viewValue;
                } else {
                    if (ngModel.$modelValue.indexOf(viewValue) === -1) {
                        ngModel.$modelValue.push(viewValue);
                    } else {
                        ngModel.$modelValue.remove(viewValue);
                    }
                    return ngModel.$modelValue;
                }

            });
            scope.showList = function () {
                scope.active = true;
                //active 变更后视图需要刷新,所以不要立即让过滤文本获取焦点,等待视图更新完成后再进行焦点设置
                setTimeout(function () {
                    ele.find('.qc-dropdown-filter').focus();
                }, 100);
            };
            scope.select = function (qcDropdownItem) {
                ngModel.$setViewValue(qcDropdownItem);
                scope.$emit(attrs.ngModel + '.change',qcDropdownItem.exportType);
            };
            scope.clearFilter = function () {
                scope.filterText = '';
            };
            angular.element(document).on('click', function (e) {
                var $this = $(e.target);
                if (!ele.has($this).length) {
                    scope.$apply(function () {
                        scope.active = false;
                        scope.clearFilter();
                    });
                }
            });
        }
    };
});




qc.directive('qcDropdownExportBatch', function () {
    return {
        require: '^ngModel',
        restrict: 'AE',
        scope: {
            filterExpression: '@',//当没有过滤表达式的时候,不过滤,也不显示过滤文本框
            items: '=',//绑定的下拉列表
            multiple: '@',
            listWidth: '=',
            listMaxHeight: '='
        },
        replace: true,
        template: function (ele, attrs) {
            var htmlArray = [];
            htmlArray.push('<div class="qc-dropdown" style="border:none;">');
            var dropdownTextTemplate = '<div  style="border-radius:3px;" class="qc-dropdown-text-container" ng-click="showList()">';
            var dropText = '请选择';
            if (attrs.dropdownText) {
                dropText = attrs.dropdownText;
            }
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="' + dropText + '">' + dropText + '</span>';
            dropdownTextTemplate += '<span class="qc-dropdown-anchor">▼</span>';
            dropdownTextTemplate += '</div>';

            htmlArray.push(dropdownTextTemplate);
            //htmlArray.push('<input class="qc-dropdown-filter" ng-model="filterText" ng-hide="!active || !filterExpression">');

            htmlArray.push('<div style="position:absolute;width:78px;left:0;border-radius:4px;" class="qc-dropdown-container" ng-if="active">');


            if (attrs.listMaxHeight) {
                htmlArray.push('<ul style="border-radius: 4px;background-color: white;" class="qc-dropdown-list qc-scroll" ng-style="{\'max-height\':listMaxHeight}">');
            } else {
                htmlArray.push('<ul style="border-radius: 4px;background-color: white;" class="qc-dropdown-list" ng-style="{\'max-height\':listMaxHeight}">');
            }

            htmlArray.push('<li  class="qc-dropdown-list-search" ng-hide="!filterExpression"><div  class="qc-dropdown-filter-container"><input class="qc-dropdown-filter" ng-model="filterText" ></div><em></em></li>');

            var filterString = '';
            if (attrs.filterExpression) {
                filterString = ' | qcPropsFilter:\'' + attrs.filterExpression + '\':filterText';
            }
            var activeItemExpression = '(qcDropdownItem==selectedItem)';
            if (attrs.multiple === 'multiple') {
                activeItemExpression = '(selectedItem.indexOf(qcDropdownItem)>-1)';
            }


            htmlArray.push('<li style="width:60px;text-align:left;border: none;margin: 0;padding:3px 8px;"  class="qc-dropdown-item" ng-class="{active:' + activeItemExpression + '}" ' +
            ' whichone="qcDropdownItem"  href="javascript:void(0)"  show-Exp   ng-repeat="qcDropdownItem in items' + filterString + '">' + attrs.itemText +
            '</li>');
            htmlArray.push('</ul>');
            htmlArray.push('</div>');
            htmlArray.push('</div>');
            return htmlArray.join('');
        },
        link: function (scope, ele, attrs, ngModel) {
            //ngModel的modelValue变动后通知下拉框
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            ngModel.$parsers.unshift(function (viewValue) {
                if (scope.multiple !== 'multiple') {
                    scope.active = false;
                    scope.selectedItem = viewValue;
                    scope.clearFilter();
                    return viewValue;
                } else {
                    if (ngModel.$modelValue.indexOf(viewValue) === -1) {
                        ngModel.$modelValue.push(viewValue);
                    } else {
                        ngModel.$modelValue.remove(viewValue);
                    }
                    return ngModel.$modelValue;
                }

            });
            scope.showList = function () {
                scope.active = true;
                //active 变更后视图需要刷新,所以不要立即让过滤文本获取焦点,等待视图更新完成后再进行焦点设置
                setTimeout(function () {
                    ele.find('.qc-dropdown-filter').focus();
                }, 100);
            };
            scope.select = function (qcDropdownItem) {
                ngModel.$setViewValue(qcDropdownItem);
                scope.$emit(attrs.ngModel + '.change',qcDropdownItem.exportType);
            };
            scope.clearFilter = function () {
                scope.filterText = '';
            };
            angular.element(document).on('click', function (e) {
                var $this = $(e.target);
                if (!ele.has($this).length) {
                    scope.$apply(function () {
                        scope.active = false;
                        scope.clearFilter();
                    });
                }
            });
        }
    };
});







qc.directive('qcDropdownAsyncForMany', function () {
    return {
        require: '^ngModel',
        restrict: 'AE',
        scope: {
            filterExpression: '@',//当没有过滤表达式的时候,不过滤,也不显示过滤文本框
            items: '=',//绑定的下拉列表
            listWidth: '=',
            multiple: '@',
            idName: '@',//判断对象是否相等的属性名称
            modelName: '@',
            listMaxHeight: '='
        },
        replace: true,
        template: function (ele, attrs) {
            var htmlArray = [];
            htmlArray.push('<div class="qc-dropdown">');
            var dropdownTextTemplate = '<div class="qc-dropdown-text-container multi_angle" ng-click="showList()">';
            var dropText = ' ';
            if (attrs.dropdownText) {
                dropText = attrs.dropdownText;
            }
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="'+dropText+'">' + dropText + '</span>';
            dropdownTextTemplate += '<span class="qc-dropdown-anchor">▼</span>';
            dropdownTextTemplate += '</div>';

            htmlArray.push(dropdownTextTemplate);

            htmlArray.push('<div style="position:relative;left:82px;top:-5px;" class="qc-dropdown-container" ng-if="active"   ng-style="{\'width\':listWidth}">');
            //htmlArray.push('<input class="qc-dropdown-filter" ng-keyup="search()">');
            if (attrs.listMaxHeight) {
                htmlArray.push('<ul class="qc-dropdown-list qc-scroll" ng-style="{\'max-height\':listMaxHeight}">');
            } else {
                htmlArray.push('<ul class="qc-dropdown-list" ng-style="{\'max-height\':listMaxHeight}">');
            }
            htmlArray.push('<li class="qc-dropdown-list-search"  ng-hide="!filterExpression"><div  class="qc-dropdown-filter-container"><input class="qc-dropdown-filter"  ng-keyup="search()" ></div><em></em></li>');

            htmlArray.push('<li class="qc-dropdown-item"' +
                'ng-click="select(qcDropdownItem)" ng-repeat="qcDropdownItem in items" ng-class="{\'active\':getItemInNgModel(qcDropdownItem)}">' + attrs.itemText +
                '</li>');
            htmlArray.push('</ul>');
            htmlArray.push('</div>');
            htmlArray.push('</div>');
            return htmlArray.join('');
        },
        link: function (scope, ele, attrs, ngModel) {
            //if(scope.multiple && !ngModel.$modelValue){
            //    ngModel.$setViewValue([]);
            //}
            //ngModel的modelValue变动后通知下拉框
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            ngModel.$parsers.unshift(function (viewValue) {

                if (scope.multiple !== 'multiple') {
                    scope.active = false;
                    scope.selectedItem = viewValue;
                    scope.clearFilter();
                    return viewValue;
                } else {
                    var modelItem = scope.getItemInNgModel(viewValue);
                    if (!modelItem) {
                        ngModel.$modelValue.push(viewValue);
                    } else {
                        ngModel.$modelValue.remove(modelItem);
                    }
                    return ngModel.$modelValue;
                }
            });
            scope.getItemInNgModel = function (item) {
                if(!ngModel.$modelValue){
                    return null;
                }
                if (scope.multiple !== 'multiple') {
                    var isSame = false;
                    if (scope.idName) {
                        isSame = (item[scope.idName] === ngModel.$modelValue[scope.idName]);
                    } else {
                        isSame = (item === ngModel.$modelValue);
                    }
                    return isSame?ngModel.$modelValue:null;
                } else {
                    return ngModel.$modelValue.filter(function (modelItem) {
                        var isSame = false;
                        if (scope.idName) {
                            isSame = (item[scope.idName] === modelItem[scope.idName]);
                        } else {
                            isSame = (item === modelItem);
                        }
                        return isSame;
                    })[0];
                }
            };
            scope.search = function () {
                var $filter = ele.find('input');
                if(scope.multiple && !ngModel.$modelValue){
                    ngModel.$modelValue=[];
                }
                scope.$emit(attrs.ngModel + '.search', $filter.val());
            };
            scope.showList = function () {
                //此处是为了做修改操作的时候，将原先选中的值默认选中
                //modify  by wlj at 2016.06.22 -start
                var _x=scope.$parent.$$prevSibling.items;
                if(_x&&scope.$parent.selectedUser&&scope.$parent.selectedUser.length==0){
                    console.log("已选择的值"+scope.$parent.$$prevSibling.items);
                    for(var i=0;i<_x.length;i++){
                        ngModel.$setViewValue(_x[i]);
                        scope.$emit(attrs.ngModel+'.change');
                    }
                }
                //modify  by wlj at 2016.06.22 -end
                scope.active = true;
                //active 变更后视图需要刷新,所以不要立即让过滤文本获取焦点,等待视图更新完成后再进行焦点设置
                setTimeout(function () {
                    ele.find('.qc-dropdown-filter').focus();
                    scope.search();
                }, 100);
            };
            scope.select = function (qcDropdownItem) {
                if(scope.selectedItem && qcDropdownItem[scope.idName] === scope.selectedItem[scope.idName]){
                    return;
                }
                ngModel.$setViewValue(qcDropdownItem);
                scope.$emit(attrs.ngModel+'.change');
            };
            scope.clearFilter = function () {
                scope.filterText = '';
                scope.item = [];
            };
            angular.element(document).on('click', function (e) {
                var $this = $(e.target);
                if (!ele.has($this).length) {
                    scope.$apply(function () {
                        scope.active = false;
                        scope.clearFilter();
                    });
                }
            });
        }
    };
});

qc.directive('qcDropdownAsync', function () {
    return {
        require: '^ngModel',
        restrict: 'AE',
        scope: {
            filterExpression: '@',//当没有过滤表达式的时候,不过滤,也不显示过滤文本框
            items: '=',//绑定的下拉列表
            listWidth: '=',
            multiple: '@',
            idName: '@',//判断对象是否相等的属性名称
            modelName: '@',
            listMaxHeight: '='
        },
        replace: true,
        template: function (ele, attrs) {
            var htmlArray = [];
            htmlArray.push('<div class="qc-dropdown">');
            var dropdownTextTemplate = '<div class="qc-dropdown-text-container" ng-click="showList()">';
            var dropText = '请选择';
            if (attrs.dropdownText) {
                dropText = attrs.dropdownText;
            }
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="' + dropText + '">' + dropText + '</span>';
            dropdownTextTemplate += '<span class="qc-dropdown-anchor">▼</span>';
            dropdownTextTemplate += '</div>';

            htmlArray.push(dropdownTextTemplate);

            htmlArray.push('<div class="qc-dropdown-container" ng-if="active"   ng-style="{\'width\':listWidth}">');
            //htmlArray.push('<input class="qc-dropdown-filter" ng-keyup="search()">');
            if (attrs.listMaxHeight) {
                htmlArray.push('<ul class="qc-dropdown-list qc-scroll" ng-style="{\'max-height\':listMaxHeight}">');
            } else {
                htmlArray.push('<ul class="qc-dropdown-list" ng-style="{\'max-height\':listMaxHeight}">');
            }
            htmlArray.push('<li class="qc-dropdown-list-search"  ng-hide="!filterExpression"><div  class="qc-dropdown-filter-container"><input class="qc-dropdown-filter"  ng-keyup="search()" ></div><em></em></li>');
            /**
             * 新添加Style  如果选项太长，就不会压到下面的那一行了
             */
            htmlArray.push('<li style="white-space: nowrap" class="qc-dropdown-item"' +
            'ng-click="select(qcDropdownItem)" ng-repeat="qcDropdownItem in items" ng-class="{\'active\':getItemInNgModel(qcDropdownItem)}">' + attrs.itemText +
            '</li>');
            htmlArray.push('</ul>');
            htmlArray.push('</div>');
            htmlArray.push('</div>');
            //console.log(htmlArray);
            return htmlArray.join('');
        },
        link: function (scope, ele, attrs, ngModel) {
            //if(scope.multiple && !ngModel.$modelValue){
            //    ngModel.$setViewValue([]);
            //}
            //ngModel的modelValue变动后通知下拉框
            ngModel.$formatters.push(function (modelValue) {
                scope.selectedItem = modelValue;
            });
            ngModel.$parsers.unshift(function (viewValue) {
            	
                if (scope.multiple !== 'multiple') {
                    scope.active = false;
                    scope.selectedItem = viewValue;
                    scope.clearFilter();
                    return viewValue;
                } else {
                    var modelItem = scope.getItemInNgModel(viewValue);
                    if (!modelItem) {
                        ngModel.$modelValue.push(viewValue);
                    } else {
                        ngModel.$modelValue.remove(modelItem);
                    }
                    return ngModel.$modelValue;
                }
            });
            scope.getItemInNgModel = function (item) {
                if (!ngModel.$modelValue) {
                    return null;
                }
                if (scope.multiple !== 'multiple') {
                    var isSame = false;
                    if (scope.idName) {
                        isSame = (item[scope.idName] === ngModel.$modelValue[scope.idName]);
                    } else {
                        isSame = (item === ngModel.$modelValue);
                    }
                    return isSame ? ngModel.$modelValue : null;
                } else {
                    return ngModel.$modelValue.filter(function (modelItem) {
                        var isSame = false;
                        if (scope.idName) {
                            isSame = (item[scope.idName] === modelItem[scope.idName]);
                        } else {
                            isSame = (item === modelItem);
                        }
                        return isSame;
                    })[0];
                }
            };
            scope.search = function () {
                var $filter = ele.find('input');
                if (scope.multiple && !ngModel.$modelValue) {
                    ngModel.$modelValue = [];
                }
                scope.$emit(attrs.ngModel + '.search', $filter.val().replace(/[']/g,''));
            };
            scope.showList = function () {
               scope.active = true;
                //scope.active = false;
                //active 变更后视图需要刷新,所以不要立即让过滤文本获取焦点,等待视图更新完成后再进行焦点设置
                setTimeout(function () {
                    ele.find('.qc-dropdown-filter').focus();
                    scope.search();
                }, 100);
            };
            scope.select = function (qcDropdownItem) {
                if (scope.selectedItem && qcDropdownItem[scope.idName] === scope.selectedItem[scope.idName]) {
                    return;
                }
                ngModel.$setViewValue(qcDropdownItem);
                scope.$emit(attrs.ngModel + '.change');
            };
            scope.clearFilter = function () {
                scope.filterText = '';
                scope.item = [];
            };
            angular.element(document).on('click', function (e) {
                var $this = $(e.target);
                if (!ele.has($this).length) {
                    scope.$apply(function () {
                        scope.active = false;
                        scope.clearFilter();
                    });
                }
            });
        }
    };
});
/*华丽的分隔线*/
qc.directive('qcInputAmount', ['$timeout', function ($timeout) {
    return {
        require: '^ngModel',
        restrict: 'A',
        scope: {
            qcInputNumber: '='
        },
        link: function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength: 9,
                zero: true,//是否可以为0
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
        require: '^ngModel',
        restrict: 'A',
        scope: {
            qcInputNumber: '='
        },
        link: function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength: 9,
                zero: true,//是否可以为0
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
                if (value == '-') {
                    return true;
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
        require: '^ngModel',
        restrict: 'A',
        scope: {
            qcInputInt: '='
        },
        link: function (scope, element, attrs, ngModel) {
            var defaultOptions = {
                intLength: 5,
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
        require: '^ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, ngModel) {
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
        require: '?ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, ngModel) {
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
        require: '?ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, ngModel) {
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
        require: '?ngModel',
        restrict: 'A',
        link: function (scope, element, attrs, ngModel) {
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
        require: '?ngModel',
        restrict: 'E',
        link: function (scope, element, attrs, ngModel) {
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
/*华丽的分隔线*/
//扩展doc element 的 placeholder方法
(function ($) {
    // @todo Document this.
    $.extend($, {
        placeholder: {
            browser_supported: function () {
                return this._supported !== undefined ?
                    this._supported :
                    ( this._supported = !!('placeholder' in $('<input type="text">')[0]) );
            },
            shim: function (opts) {
                var config = {
                    color: '#888',
                    cls: 'placeholder',
                    selector: 'input[placeholder], textarea[placeholder]',
                    align: 'left'
                };
                $.extend(config, opts);
                if (!this.browser_supported()) {
                    $(config.selector)._placeholder_shim(config);
                }
            }
        }
    });

    $.extend($.fn, {
        _placeholder_shim: function (config) {
            function calcPositionCss(target) {
                var op = $(target).offsetParent().offset();
                var ot = $(target).offset();

                return {
                    top: ot.top - op.top,
                    left: ot.left - op.left,
                    width: $(target).outerWidth(true)
                };
            }

            return this.each(function () {
                var $this = $(this);

                if ($this.data('placeholder')) {
                    var $ol = $this.data('placeholder');
                    $ol.css(calcPositionCss($this));
                    return true;
                }

                var possible_line_height = {};
                if (!$this.is('textarea') && $this.css('line-height') !== 'auto') {
                    possible_line_height = {lineHeight: $this.css('line-height'), whiteSpace: 'nowrap'};
                }

                var ol = $('<label />')
                    .text($this.attr('placeholder'))
                    .addClass(config.cls)
                    .css($.extend({
                        position: 'absolute',
                        display: 'inline',
                        'float': 'none',
                        overflow: 'hidden',
                        textAlign: config.align,
                        color: config.color,
                        cursor: 'text',
                        paddingTop: $this.css('padding-top'),
                        paddingRight: $this.css('padding-right'),
                        paddingBottom: $this.css('padding-bottom'),
                        paddingLeft: $this.css('padding-left'),
                        fontSize: $this.css('font-size'),
                        fontFamily: $this.css('font-family'),
                        fontStyle: $this.css('font-style'),
                        fontWeight: $this.css('font-weight'),
                        textTransform: $this.css('text-transform'),
                        backgroundColor: 'transparent',
                        zIndex: 99
                    }, possible_line_height))
                    .css(calcPositionCss(this))
                    .attr('for', this.id)
                    .data('target', $this)
                    .click(function () {
                        $(this).data('target').focus();
                    })
                    .insertBefore(this);
                $this
                    .data('placeholder', ol)
                    .focus(function () {
                        ol.hide();
                    }).blur(function () {
                        ol[$this.val().length ? 'hide' : 'show']();
                    }).triggerHandler('blur');
                $(window)
                    .resize(function () {
                        var $target = ol.data('target');
                        ol.css(calcPositionCss($target));
                    });
            });
        }
    });
})(jQuery);
//jQuery(document).add(window).bind('ready load', function() {
//    if (jQuery.placeholder) {
//        jQuery.placeholder.shim();
//    }
//});

/* angular-placeholder-shim version 0.3.1
 * License: MIT.
 * Copyright (C) 2013, Uri Shaked.
 */

//封装成angular组件
qc.directive('placeholder', ['$interpolate', '$timeout', function ($interpolate, $timeout) {
    if (jQuery.placeholder.browser_supported()) {
        return {};
    }

    return function (scope, element, attrs) {
        var config = {
            color: '#888',
            cls: 'placeholder'
        };

        var interpolatedPlaceholder = $interpolate(element.attr('placeholder'));
        var placeholderText = null;

        var overlay = null;
        var pendingTimer = null;

        function addPlaceholder() {
            pendingTimer = $timeout(function () {
                element._placeholder_shim(config);
                overlay = element.data('placeholder');
                pendingTimer = null;
            });
        }

        //ie8在响应式页面中需要等待respond.js执行完成后再添加placeholder
        $timeout(function () {
            if (element.is(':visible')) {
                addPlaceholder();
            }
        }, 100);

        // The following code accounts for value changes from within the code
        // and for dynamic changes in placeholder text
        scope.$watch(function () {
            if (!overlay && element.is(':visible') && !pendingTimer) {
                addPlaceholder();
            }
            if (overlay && (element.get(0) !== document.activeElement)) {
                if (element.val().length) {
                    overlay.hide();
                } else {
                    overlay.show();
                }
            }
            if (overlay) {
                var newText = interpolatedPlaceholder(scope);
                if (newText !== placeholderText) {
                    placeholderText = newText;
                    overlay.text(placeholderText);
                }
            }
        });
        scope.$on('$destroy', function () {
            if (pendingTimer) {
                $timeout.cancel(pendingTimer);
                pendingTimer = null;
            }
        });
    };
}]);
/*华丽的分隔线*/
/**
 * @description directive tab容器
 */
qc.directive('qcScroll', ['$timeout', function ($timeout) {
    return {
        restrict: 'C',
        scope: {
            qcScrollOptions: '='
        },
        link: function (scope, ele, attrs) {
            //滚动条默认属性
            //为了让样式(.nicescroll-cursors)能够起作用需要将部分属性设置为空
            var defaultOptions = {
                zindex: "auto",
                cursoropacitymin: 0,
                cursoropacitymax: 1,
                cursorcolor: "",
                cursorwidth: "8px",
                cursorborder: "",
                cursorborderradius: "",
                scrollspeed: 60,
                mousescrollstep: 8 * 3,
                touchbehavior: false,
                hwacceleration: true,
                usetransition: true,
                boxzoom: false,
                dblclickzoom: true,
                gesturezoom: true,
                grabcursorenabled: true,
                autohidemode: 'leave',
                background: "",
                iframeautoresize: true,
                cursorminheight: 32,
                preservenativescrolling: true,
                railoffset: false,
                railhoffset: false,
                bouncescroll: true,
                spacebarenabled: true,
                railpadding: {
                    top: 0,
                    right: 0,
                    left: 0,
                    bottom: 0
                },
                disableoutline: true,
                horizrailenabled: true,
                railalign: "right",
                railvalign: "bottom",
                enabletranslate3d: true,
                enablemousewheel: true,
                enablekeyboard: true,
                smoothscroll: true,
                sensitiverail: true,
                enablemouselockapi: true,
                //      cursormaxheight:false,
                cursorfixedheight: false,
                directionlockdeadzone: 6,
                hidecursordelay: 0,
                nativeparentscrolling: true,
                enablescrollonselection: true,
                overflowx: true,
                overflowy: true,
                cursordragspeed: 0.3,
                rtlmode: "auto",
                cursordragontouch: false,
                oneaxismousemode: "auto",
                //scriptpath: getScriptPath(),
                preventmultitouchscrolling: true,
                createDelay: 400
            };
            var options = angular.extend(defaultOptions, scope.qcScrollOptions);
            $timeout(function () {
                ele.niceScroll(options);
            }, options.createDelay);//
            scope.$on('qcTableContainer.reset', function () {
                $timeout(function () {
                    ele.getNiceScroll().resize();
                }, 500);
            });
        }
    };
}]);
/*华丽的分隔线*/
qc.directive('qcSubTableParent', function () {
    return {
        //require: '^qcSubTableContainer',
        restrict: 'A',
        scope: false,
        link: function (scope, ele, attrs) {
            if (!angular.isArray(scope.qcSubTableIds)) {
                return;
            }
            scope.spreadCount = 0;
            angular.forEach(scope.qcSubTableIds, function (qcSubTableId) {
                scope.$on('subTable.spread.ready.' + qcSubTableId, function () {
                    scope.spreadCount++;

                });
                scope.$on('subTable.fold.ready.' + qcSubTableId, function () {
                    if (scope.spreadCount) {
                        scope.spreadCount--;
                    }
                });
                scope.$on('subTable.spreadAll', function () {
                    scope.spreadCount++;
                });
                scope.$on('subTable.foldAll', function () {
                    if (scope.spreadCount) {
                        scope.spreadCount--;
                    }
                });
            });
        }
    };
});

qc.directive('qcSubTable', ['$timeout', function ($timeout) {
    return {
        restrict: 'A',

        //scope: true,
        template: '<div ng-if="spreaded" ng-transclude=""></div>',
        transclude: true,
        scope: {
            qcSubTableId: '@',
            manualReady: '='//是否由页面代码来提示子表格准备完成
        },
        link: function (scope, ele, attrs) {
            scope.spreaded = false;
            ele.css('display', 'none');
            //scope.qcSubTableId = attrs.qcSubTableId;
            scope.show = function () {
                scope.spreaded = true;
                //if (scope.manualReady) {
                //    scope.$emit('subTable.spreading', scope.qcSubTableId);
                //}
                $timeout(function () {
                    ele.parents('tbody:first').show();
                    ele.slideDown(350);
                    scope.$emit('subTable.spread.complete', scope.qcSubTableId);
                });
            };
            scope.hide = function () {
                ele.slideUp(350, function () {
                    ele.parents('tbody:first').hide();
                    scope.spreaded = false;
                    $timeout(function () {
                        scope.$emit('subTable.fold.complete', scope.qcSubTableId);
                    });
                });
            };
            scope.$on('subTable.spread.ready', function ($e, qcSubTableId) {
                if (scope.qcSubTableId === qcSubTableId) {
                    scope.show();
                } else {
                    scope.hide();
                }
            });
            scope.$on('subTable.fold', function () {
                scope.hide();
            });
        }
    };
}]);

/*华丽的分隔线*/
/**
 * @description directive tab容器
 */
qc.directive('qcTab', function () {
    return {
        restrict: 'AE',
        controller: function ($scope) {
            this.showTab = function (tabName, oldTableName) {
                $scope.activeTab = tabName;
                if (angular.isFunction($scope.qcTabChanged)) {
                    if (tabName !== oldTableName) {
                        $scope.qcTabChanged(tabName, oldTableName);
                    }
                }
            };
        },
        scope: true,
        replace: false,
        link: function (scope, ele, attrs) {
            scope.activeTab = attrs.activeTab;
            if (attrs.qcTabChanged) {
                scope.qcTabChanged = scope.$eval(attrs.qcTabChanged);
            }
        }
    };
});

/**
 * @description directive tab的头部,依赖 qc-tab. 点击后显示与其qc-tab-name相同的 qc-tab-body
 */
qc.directive('qcTabHeader', function () {
    return {
        require: '^qcTab',
        restrict: 'AE',
        scope: true,
        replace: true,
        transclude: true,
        template: '<div  ng-transclude ng-class="{active:(qcTabName==activeTab)}"></div>',
        link: function (scope, ele, attrs, ctrl) {
            scope.qcTabName = attrs.qcTabName;
            ele.on('click', function () {
                scope.$apply(function () {
                    ctrl.showTab(scope.qcTabName, scope.activeTab);
                });

            });
        }
    };
});
/**
 * @description directive tab页,依赖 qc-tab. 当与其对应的qc-tab-header 处于激活状态时显示
 */
qc.directive('qcTabBody', function () {
    return {
        restrict: 'AE',
        require: '^qcTab',
        scope: true,
        transclude: true,
        replace: true,
        template: '<div ng-hide="qcTabName!=activeTab" ng-transclude ></div>',
        link: function (scope, ele, attrs, ctrl) {
            scope.qcTabName = attrs.qcTabName;
        }
    };
});
/*华丽的分隔线*/
qc.directive('qcTableContainer', ['$window', '$timeout', function ($window, $timeout) {
    return {
        restrict: 'C',
        link: function (scope, ele, attrs) {
            var bottomGap = (+attrs.bottomGap);
            if (isNaN(bottomGap)) {
                bottomGap = 0;
            }

            function setHeight() {
                var top = ele.offset().top;
                ele.css({'height': $($window).height() - (top + bottomGap)});
            }

            $timeout(function () {
                setHeight();
            }, 1000);
            $($window).resize(function () {
                setHeight();
            });
            scope.$on('qcTableContainer.reset', function () {
                $timeout(function () {
                    setHeight();
                }, 1000);
            });
        }
    };
}]);
qc.directive('qcTableFixedHeader', ['$timeout', '$compile', function ($timeout, $compile) {
    return {
        restrict: 'A',
        link: function (scope, ele, attrs) {
            var fixedHeaderMinTop = 10;
            var $fixedHeader = ele.clone();
            $fixedHeader.empty();
            $fixedHeader.append(ele.find('thead').clone());
            $fixedHeader.addClass('qc-table-fixed-header');
            $fixedHeader.removeAttr('qc-table-fixed-header');
            ele.after($fixedHeader);
            $compile($fixedHeader)(scope);
            $fixedHeader.hide();
            var $offsetParent = ele.offsetParent();
            var scrollTimer;
            $offsetParent.scroll(function () {
                $timeout.cancel(scrollTimer);
                var $this = $(this);
                var $fixedHeader = $this.find('.qc-table-fixed-header');
                $fixedHeader.hide();
                scrollTimer = $timeout(function () {
                    var top = ele.position().top;
                    var scrollTop = $this.scrollTop();
                    $fixedHeader.css({top: scrollTop});
                    if (-top > fixedHeaderMinTop) {
                        $fixedHeader.fadeIn(100);
                    }
                }, 200);

            });
        }
    };
}]);
/*华丽的分隔线*/
qc.directive('qcTableOperator', ['$timeout', function ($timeout) {
    return {
        restrict: 'C',
        scope: {
            qcTableOperatorOption: '='
        },
        template: '<div ng-transclude ng-if="active"></div>',
        replace: false,
        transclude: true,
        link: function (scope, ele, attrs) {
            var defaultOptions = {
                activeDelay: 300,
                inactiveDelay: 295
            };
            var options = angular.extend(defaultOptions, scope.qcTableOperatorOption);
            if (attrs.height) {
                ele.css('height', attrs.height + 'px');
            }
            if (attrs.width) {
                ele.css('width', attrs.width + 'px');
            }
            var $tbody = ele.parents('tbody:first').addClass('qc-table-operator-tbody');
            var $td = ele.parents('td:first');
            var showTimer;
            var hideTimer;
            $tbody.on('mouseenter', function () {
                $timeout.cancel(hideTimer);
                showTimer = $timeout(function () {
                    //$tbody.addClass('active');
                    var scrollTop = $td.offsetParent().scrollTop();
                    var scrollLeft = $td.offsetParent().scrollLeft();
                    var left = $td.position().left + scrollLeft;
                    var top = $td.position().top + scrollTop;
                    var height = ele.height();
                    var width = ele.width();
                    //操作项不能越出表格父级容器

                    if (top < scrollTop) {
                        top = scrollTop;
                    }
                    var bottom = $td.offsetParent().height() + scrollTop;
                    if (top + height > bottom) {
                        top = bottom - height;
                    }

                    var right = $td.offsetParent().width() + scrollLeft;
                    if (left + width > right) {
                        left = right - width;
                    }
                    ele.css({top: top, left: left});
                    ele.show();
                    scope.active = true;
                }, options.activeDelay);
            });
            $tbody.on('mouseleave', function () {
                $timeout.cancel(showTimer);
                hideTimer = $timeout(function () {
                    //$tbody.removeClass('active');
                    ele.hide();
                    scope.active = false;
                }, options.inactiveDelay);
            });
        }
    };
}]);
/*华丽的分隔线*/
//jquery封装
(function ($, window) {
    var __bind = function (fn, me) {
            return function () {
                return fn.apply(me, arguments);
            };
        },
        __slice = [].slice;
    var ResizableColumns;

    ResizableColumns = (function () {
        ResizableColumns.prototype.defaults = {
            store: window.store,
            rigidSizing: false
        };

        function ResizableColumns($table, options) {
            this.mousedown = __bind(this.mousedown, this);
            var _this = this;

            this.options = $.extend({}, this.defaults, options);
            this.$table = $table;
            this.tableId = this.$table.data('resizable-columns-id');
            this.createHandles();
            this.restoreColumnWidths();
            this.syncHandleWidths();
            $(window).on('resize.rc', (function () {
                return _this.syncHandleWidths();
            }));
        }

        ResizableColumns.prototype.destroy = function () {
            this.$handleContainer.remove();
            this.$table.removeData('resizableColumns');
            return $(window).off('.rc');
        };

        ResizableColumns.prototype.createHandles = function () {
            var _this = this;

            this.$table.before((this.$handleContainer = $("<div class='qc-table-resizecolumn-container' />")));
            this.$table.find('tr th').each(function (i, el) {
                var $handle;

                //if (_this.$table.find('tr th').eq(i + 1).length === 0 || (_this.$table.find('tr th').eq(i).attr('data-noresize') != null) || (_this.$table.find('tr th').eq(i + 1).attr('data-noresize') != null)) {
                if (_this.$table.find('tr th').eq(i).length === 0 || (_this.$table.find('tr th').eq(i).attr('data-noresize') != null) || (_this.$table.find('tr th').eq(i + 1).attr('data-noresize') != null)) {
                    return;
                }
                $handle = $("<div class='qc-table-resizecolumn-handle' />");
                $handle.data('th', $(el));
                return $handle.appendTo(_this.$handleContainer);
            });
            return this.$handleContainer.on('mousedown', '.qc-table-resizecolumn-handle', this.mousedown);
        };

        ResizableColumns.prototype.syncHandleWidths = function () {
            var _this = this;

            this.$handleContainer.width(this.$table.width());
            return this.$handleContainer.find('.qc-table-resizecolumn-handle').each(function (_, el) {
                return $(el).css({
                    //left: $(el).data('th').outerWidth() + ($(el).data('th').offset().left - _this.$handleContainer.offset().left),
                    left: $(el).data('th').outerWidth() + ($(el).data('th').position().left),
                    height: _this.$table.find('thead').height()
                });
            });
        };

        ResizableColumns.prototype.saveColumnWidths = function () {
            var _this = this;

            return this.$table.find('tr th').each(function (_, el) {
                var id;

                if ($(el).attr('data-noresize') == null) {
                    id = _this.tableId + '-' + $(el).data('resizable-column-id');
                    if (_this.options.store != null) {
                        return window.store.set(id, $(el).width());
                    }
                }
            });
        };

        ResizableColumns.prototype.restoreColumnWidths = function () {
            var _this = this;

            return this.$table.find('tr th').each(function (_, el) {
                var id, width;

                id = _this.tableId + '-' + $(el).data('resizable-column-id');
                if ((_this.options.store != null) && (width = window.store.get(id))) {
                    return $(el).width(width);
                }
            });
        };

        ResizableColumns.prototype.mousedown = function (e) {
            var $currentGrip, $leftColumn, $rightColumn, idx, leftColumnStartWidth, rightColumnStartWidth,
                _this = this;

            e.preventDefault();
            this.startPosition = e.pageX;
            $currentGrip = $(e.currentTarget);
            $leftColumn = $currentGrip.data('th');
            leftColumnStartWidth = $leftColumn.width();
            idx = this.$table.find('tr th').index($currentGrip.data('th'));
            $rightColumn = this.$table.find('tr th').eq(idx + 1);
            rightColumnStartWidth = $rightColumn.width();
            $(document).on('mousemove.rc', function (e) {
                var difference, newLeftColumnWidth, newRightColumnWidth;

                difference = e.pageX - _this.startPosition;
                newRightColumnWidth = rightColumnStartWidth - difference;
                newLeftColumnWidth = leftColumnStartWidth + difference;
                if (_this.options.rigidSizing && ((parseInt($rightColumn[0].style.width) < $rightColumn.width()) && (newRightColumnWidth < $rightColumn.width())) || ((parseInt($leftColumn[0].style.width) < $leftColumn.width()) && (newLeftColumnWidth < $leftColumn.width()))) {
                    return;
                }
                var minLeftColumnWidth = parseInt($leftColumn.css('min-width'));
                var minRightColumnWidth = parseInt($rightColumn.css('min-width'));
                //if(minRightColumnWidth>newRightColumnWidth || minLeftColumnWidth>newLeftColumnWidth){
                //    return;
                //}

                $leftColumn.width(newLeftColumnWidth > minLeftColumnWidth ? newLeftColumnWidth : minLeftColumnWidth);
                $rightColumn.width(newRightColumnWidth > minRightColumnWidth ? newRightColumnWidth : minRightColumnWidth);
                return _this.syncHandleWidths();
            });
            return $(document).one('mouseup', function () {
                $(document).off('mousemove.rc');
                return _this.saveColumnWidths();
            });
        };

        return ResizableColumns;

    })();
    return $.fn.extend({
        resizableColumns: function () {
            var args, option;

            option = arguments[0];
            args = (2 <= arguments.length ? __slice.call(arguments, 1) : []);
            return this.each(function () {
                var $table, data;

                $table = $(this);
                data = $table.data('resizableColumns');
                if (!data) {
                    $table.data('resizableColumns', (data = new ResizableColumns($table, option)));
                }
                if (typeof option === 'string') {
                    return data[option].apply(data, args);
                }
            });
        }
    });
})(window.jQuery, window);


qc.directive('qcTableResizecolumn', function () {
    return {
        restrict: 'C',
        link: function (scope, ele, attrs) {
            ele.resizableColumns({});
        }
    };
});
/*华丽的分隔线*/
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
/*华丽的分隔线*/
qc.filter('qcCurrency', ["$filter", function ($filter) {
    return function (amount, currencySymbol) {

        var currency = $filter('currency');

        if (amount < 0) {
            return currency(amount, currencySymbol).replace("(", "").replace(")", "").replace(currencySymbol, currencySymbol + '- ');
        }

        return currency(amount, currencySymbol);
    };

}]);
/*华丽的分隔线*/

/*华丽的分隔线*/
qc.filter('qcPaging', function () {
    return function (arr, rowCount, index) {
        return arr.paging(rowCount, index - 1);
    };
});
/*华丽的分隔线*/
qc.filter('qcPropsFilter', function () {
    return function (arr, propString, text) {
        if (angular.isUndefined(text)) {
            return arr;
        }
        var props = propString.split(',');
        var length = props.length;
        return arr.filter(function (item) {
            var is = false;
            for (var i = 0; i < length; i++) {
                if (angular.isString(item[props[i]])) {
                    if (item[props[i]].indexOf(text) >= 0) {
                        is = true;
                        break;
                    }
                }
            }
            return is;
        });
    };
});
/*华丽的分隔线*/
qc.provider('qcApi', function () {
    this.$get = ['$http', 'urlConfig', function ($http, urlConfig) {
        //function get(router, config) {
        //    var apiUrl = apiConfig.url + ':' + apiConfig.port + '/' + router;
        //   return $http.get(apiUrl, config);
        //}
        function mtourPost(router, params) {

            var postData = "requestType=mtour data";
            if (params) {
                //if(angular.isObject(params))
                var param = base64encode(JSON.stringify(params));
                postData += "&param=" + param;
            }
            //postData = encodeURI(postData);
            var url = urlConfig.mtourApiUrl + router + '?date=' + new Date();
            return $http.post(url, postData, {
                'headers': {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'x-requested-with': 'XMLHttpRequest'
                }
            });
        }

        function mtourGet(router, params) {

            var postData = {requestType: "mtour data", param: params};
            var url = urlConfig.mtourApiUrl + router + '?date=' + new Date();
            return $http.get(url, postData, {
                'headers': {
                    'Content-Type': 'application/x-www-form-urlencoded',
                    'x-requested-with': 'XMLHttpRequest'
                }
            });
        }

        return {
            get: mtourGet,
            post: mtourPost
        };
    }];
});
/*华丽的分隔线*/
qc.service('qcCookie', ['$document', function ($document) {
    this.get = function (key) {
        if ($document[0].cookie && $document[0].cookie.length > 0) {
            var c_start = $document[0].cookie.indexOf(key + '=');
            if (c_start !== -1) {
                c_start = c_start + key.length + 1;
                var c_end = $document[0].cookie.indexOf(';', c_start);
                if (c_end === -1) {
                    c_end = $document[0].cookie.length;
                }
                return angular.fromJson($document[0].cookie.substring(c_start, c_end));
            }
        }
        return '';
    };
    this.set = function (key, value, expiredays) {
        var expires = '';
        if (!angular.isUndefined(expiredays)) {
            var d = new Date();
            d.setTime(d.getTime() + (expiredays * 24 * 60 * 60 * 1000));
            var expires = 'expires=' + d.toUTCString();
        }
        if (expires) {
            $document[0].cookie = key + '=' + angular.toJson(value) + '; ' + expires + '; path=/';
        }
        else {
            $document[0].cookie = key + '=' + angular.toJson(value) + '; path=/';
        }
    };
    this.remove = function (key) {
        this.set(key, '', -1);
    };
}]);
/*华丽的分隔线*/
qc.provider('qcDialog', function () {
    var $el = angular.element;
    var isDef = angular.isDefined;
    //若为chrome内核，判断版本是否超过40
    var chromeVersionIsNew = (function getChromeVersion () {
        var raw = navigator.userAgent.match(/Chrom(e|ium)\/([0-9]+)\./);
        return raw ? parseInt(raw[2], 10)>40 : true;
    })();
    var style = (document.body || document.documentElement).style;
    var animationEndSupport = (isDef(style.animation) || isDef(style.WebkitAnimation) || isDef(style.MozAnimation) || isDef(style.MsAnimation) || isDef(style.OAnimation))&&chromeVersionIsNew;
    var animationEndEvent = 'animationend webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend';
    var focusableElementSelector = 'a[href], area[href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, *[tabindex], *[contenteditable]';
    var disabledAnimationClass = 'qc-dialog-disabled-animation';
    var forceElementsReload = {html: false, body: false};
    var scopes = {};
    var openIdStack = [];
    var keydownIsBound = false;
    var defaults = this.defaults = {
        className: 'qc-dialog-theme-default',
        disableAnimation: false,
        draggable: true,
        plain: false,
        showClose: true,
        closeByDocument: false,
        closeByEscape: false,
        closeByNavigation: false,
        appendTo: false,
        preCloseCallback: false,
        overlay: true,
        cache: true,
        trapFocus: false,//打开弹出窗后,自动焦点(优先顺序[添加autofocus属性的元素>表单元素])
        preserveFocus: true,//弹出窗关闭后,是否将焦点回落到原来的焦点元素
        ariaAuto: true,
        ariaRole: null,
        ariaLabelledById: null,
        ariaLabelledBySelector: null,
        ariaDescribedById: null,
        ariaDescribedBySelector: null
    };

    this.setForceHtmlReload = function (_useIt) {
        forceElementsReload.html = _useIt || false;
    };

    this.setForceBodyReload = function (_useIt) {
        forceElementsReload.body = _useIt || false;
    };

    this.setDefaults = function (newDefaults) {
        angular.extend(defaults, newDefaults);
    };

    var globalID = 0, dialogsCount = 0, closeByDocumentHandler, defers = {};

    this.$get = ['$document', '$templateCache', '$compile', '$q', '$http', '$rootScope', '$timeout', '$window', '$controller', '$injector',
        function ($document, $templateCache, $compile, $q, $http, $rootScope, $timeout, $window, $controller, $injector) {
            var $elements = [];

            angular.forEach(
                ['html', 'body'],
                function (elementName) {
                    $elements[elementName] = $document.find(elementName);
                    if (forceElementsReload[elementName]) {
                        var eventName = privateMethods.getRouterLocationEventName();
                        $rootScope.$on(eventName, function () {
                            $elements[elementName] = $document.find(elementName);
                        });
                    }
                }
            );

            var privateMethods = {
                onDocumentKeydown: function (event) {
                    if (event.keyCode === 27) {
                        publicMethods.close('$escape');
                    }
                },

                activate: function ($dialog) {
                    var options = $dialog.data('$qcDialogOptions');

                    if (options.trapFocus) {
                        $dialog.on('keydown', privateMethods.onTrapFocusKeydown);

                        // Catch rogue changes (eg. after unfocusing everything by clicking a non-focusable element)
                        $elements.body.on('keydown', privateMethods.onTrapFocusKeydown);
                    }
                },

                deactivate: function ($dialog) {
                    $dialog.off('keydown', privateMethods.onTrapFocusKeydown);
                    $elements.body.off('keydown', privateMethods.onTrapFocusKeydown);
                },

                deactivateAll: function () {
                    angular.forEach(function (el) {
                        var $dialog = angular.element(el);
                        privateMethods.deactivate($dialog);
                    });
                },

                setBodyPadding: function (width) {
                    var originalBodyPadding = parseInt(($elements.body.css('padding-right') || 0), 10);
                    $elements.body.css('padding-right', (originalBodyPadding + width) + 'px');
                    $elements.body.data('ng-dialog-original-padding', originalBodyPadding);
                },

                resetBodyPadding: function () {
                    var originalBodyPadding = $elements.body.data('ng-dialog-original-padding');
                    if (originalBodyPadding) {
                        $elements.body.css('padding-right', originalBodyPadding + 'px');
                    } else {
                        $elements.body.css('padding-right', '');
                    }
                },

                performCloseDialog: function ($dialog, value) {
                    //如果在弹窗中使用了 niceScroll,会出现关闭前滚动条闪现,
                    //为了解决这个问题,下面的是临时解决方案,如果由更好的方案,请更新
                    //@todo
                    var $scroll = $dialog.find('.nicescroll-rails');
                    $scroll.remove();

                    var options = $dialog.data('$qcDialogOptions');
                    var id = $dialog.attr('id');
                    var scope = scopes[id];

                    if (!scope) {
                        // Already closed
                        return;
                    }

                    if (typeof $window.Hammer !== 'undefined') {
                        var hammerTime = scope.hammerTime;
                        hammerTime.off('tap', closeByDocumentHandler);
                        if (hammerTime.destroy) {
                            hammerTime.destroy();
                        }
                        //hammerTime.destroy && hammerTime.destroy();
                        delete scope.hammerTime;
                    } else {
                        $dialog.unbind('click');
                    }

                    if (dialogsCount === 1) {
                        $elements.body.unbind('keydown', privateMethods.onDocumentKeydown);
                    }

                    if (!$dialog.hasClass('qc-dialog-closing')) {
                        dialogsCount -= 1;
                    }

                    var previousFocus = $dialog.data('$qcDialogPreviousFocus');
                    if (previousFocus) {
                        previousFocus.focus();
                    }

                    $rootScope.$broadcast('qc-dialog.closing', $dialog, value);
                    dialogsCount = dialogsCount < 0 ? 0 : dialogsCount;
                    if (animationEndSupport && !options.disableAnimation) {
                        scope.$destroy();
                        $dialog.unbind(animationEndEvent).bind(animationEndEvent, function () {
                            privateMethods.closeDialogElement($dialog, value);
                        }).addClass('qc-dialog-closing');
                    } else {
                        scope.$destroy();
                        privateMethods.closeDialogElement($dialog, value);
                    }
                    if (defers[id]) {
                        defers[id].resolve({
                            id: id,
                            value: value,
                            $dialog: $dialog,
                            remainiqcDialogs: dialogsCount
                        });
                        delete defers[id];
                    }
                    if (scopes[id]) {
                        delete scopes[id];
                    }
                    openIdStack.splice(openIdStack.indexOf(id), 1);
                    if (!openIdStack.length) {
                        $elements.body.unbind('keydown', privateMethods.onDocumentKeydown);
                        keydownIsBound = false;
                    }
                },

                closeDialogElement: function ($dialog, value) {
                    $dialog.remove();
                    if (dialogsCount === 0) {
                        $elements.html.removeClass('qc-dialog-open');
                        $elements.body.removeClass('qc-dialog-open');
                        privateMethods.resetBodyPadding();
                    }
                    $rootScope.$broadcast('qc-dialog.closed', $dialog, value);
                },

                closeDialog: function ($dialog, value) {
                    var preCloseCallback = $dialog.data('$qcDialogPreCloseCallback');

                    if (preCloseCallback && angular.isFunction(preCloseCallback)) {

                        var preCloseCallbackResult = preCloseCallback.call($dialog, value);

                        if (angular.isObject(preCloseCallbackResult)) {
                            if (preCloseCallbackResult.closePromise) {
                                preCloseCallbackResult.closePromise.then(function () {
                                    privateMethods.performCloseDialog($dialog, value);
                                });
                            } else {
                                preCloseCallbackResult.then(function () {
                                    privateMethods.performCloseDialog($dialog, value);
                                }, function () {
                                    return;
                                });
                            }
                        } else if (preCloseCallbackResult !== false) {
                            privateMethods.performCloseDialog($dialog, value);
                        }
                    } else {
                        privateMethods.performCloseDialog($dialog, value);
                    }
                },

                onTrapFocusKeydown: function (ev) {
                    var el = angular.element(ev.currentTarget);
                    var $dialog;

                    if (el.hasClass('qc-dialog')) {
                        $dialog = el;
                    } else {
                        $dialog = privateMethods.getActiveDialog();

                        if ($dialog === null) {
                            return;
                        }
                    }

                    var isTab = (ev.keyCode === 9);
                    var backward = (ev.shiftKey === true);

                    if (isTab) {
                        privateMethods.handleTab($dialog, ev, backward);
                    }
                },

                handleTab: function ($dialog, ev, backward) {
                    var focusableElements = privateMethods.getFocusableElements($dialog);

                    if (focusableElements.length === 0) {
                        if (document.activeElement) {
                            document.activeElement.blur();
                        }
                        return;
                    }

                    var currentFocus = document.activeElement;
                    var focusIndex = Array.prototype.indexOf.call(focusableElements, currentFocus);

                    var isFocusIndexUnknown = (focusIndex === -1);
                    var isFirstElementFocused = (focusIndex === 0);
                    var isLastElementFocused = (focusIndex === focusableElements.length - 1);

                    var cancelEvent = false;

                    if (backward) {
                        if (isFocusIndexUnknown || isFirstElementFocused) {
                            focusableElements[focusableElements.length - 1].focus();
                            cancelEvent = true;
                        }
                    } else {
                        if (isFocusIndexUnknown || isLastElementFocused) {
                            focusableElements[0].focus();
                            cancelEvent = true;
                        }
                    }

                    if (cancelEvent) {
                        ev.preventDefault();
                        ev.stopPropagation();
                    }
                },

                autoFocus: function ($dialog) {
                    var dialogEl = $dialog[0];

                    // Browser's (Chrome 40, Forefix 37, IE 11) don't appear to honor autofocus on the dialog, but we should
                    var autoFocusEl = dialogEl.querySelector('*[autofocus]');
                    if (autoFocusEl !== null) {
                        autoFocusEl.focus();

                        if (document.activeElement === autoFocusEl) {
                            return;
                        }

                        // Autofocus element might was display: none, so let's continue
                    }

                    var focusableElements = privateMethods.getFocusableElements($dialog);

                    if (focusableElements.length > 0) {
                        focusableElements[0].focus();
                        return;
                    }

                    // We need to focus something for the screen readers to notice the dialog
                    var contentElements = privateMethods.filterVisibleElements(dialogEl.querySelectorAll('h1,h2,h3,h4,h5,h6,p,span'));

                    if (contentElements.length > 0) {
                        var contentElement = contentElements[0];
                        $el(contentElement).attr('tabindex', '-1').css('outline', '0');
                        contentElement.focus();
                    }
                },

                getFocusableElements: function ($dialog) {
                    var dialogEl = $dialog[0];

                    var rawElements = $dialog.find(focusableElementSelector);

                    // Ignore untabbable elements, ie. those with tabindex = -1
                    var tabbableElements = privateMethods.filterTabbableElements(rawElements);

                    return privateMethods.filterVisibleElements(tabbableElements);
                },

                filterTabbableElements: function (els) {
                    var tabbableFocusableElements = [];

                    for (var i = 0; i < els.length; i++) {
                        var el = els[i];

                        if ($el(el).attr('tabindex') !== '-1') {
                            tabbableFocusableElements.push(el);
                        }
                    }

                    return tabbableFocusableElements;
                },

                filterVisibleElements: function (els) {
                    var visibleFocusableElements = [];

                    for (var i = 0; i < els.length; i++) {
                        var el = els[i];

                        if (el.offsetWidth > 0 || el.offsetHeight > 0) {
                            visibleFocusableElements.push(el);
                        }
                    }

                    return visibleFocusableElements;
                },

                getActiveDialog: function () {
                    var dialogs = document.querySelectorAll('.qc-dialog');

                    if (dialogs.length === 0) {
                        return null;
                    }

                    // TODO: This might be incorrect if there are a mix of open dialogs with different 'appendTo' values
                    return $el(dialogs[dialogs.length - 1]);
                },

                applyAriaAttributes: function ($dialog, options) {
                    if (options.ariaAuto) {
                        if (!options.ariaRole) {
                            var detectedRole = (privateMethods.getFocusableElements($dialog).length > 0) ?
                                'dialog' :
                                'alertdialog';

                            options.ariaRole = detectedRole;
                        }

                        if (!options.ariaLabelledBySelector) {
                            options.ariaLabelledBySelector = 'h1,h2,h3,h4,h5,h6';
                        }

                        if (!options.ariaDescribedBySelector) {
                            options.ariaDescribedBySelector = 'article,section,p';
                        }
                    }

                    if (options.ariaRole) {
                        $dialog.attr('role', options.ariaRole);
                    }

                    privateMethods.applyAriaAttribute(
                        $dialog, 'aria-labelledby', options.ariaLabelledById, options.ariaLabelledBySelector);

                    privateMethods.applyAriaAttribute(
                        $dialog, 'aria-describedby', options.ariaDescribedById, options.ariaDescribedBySelector);
                },

                applyAriaAttribute: function ($dialog, attr, id, selector) {
                    if (id) {
                        $dialog.attr(attr, id);
                    }

                    if (selector) {
                        var dialogId = $dialog.attr('id');

                        var firstMatch = $dialog[0].querySelector(selector);

                        if (!firstMatch) {
                            return;
                        }

                        var generatedId = dialogId + '-' + attr;

                        $el(firstMatch).attr('id', generatedId);

                        $dialog.attr(attr, generatedId);

                        return generatedId;
                    }
                },

                detectUIRouter: function () {
                    //Detect if ui-router module is installed if not return false
                    try {
                        angular.module("ui.router");
                        return true;
                    } catch (err) {
                        return false;
                    }
                },

                getRouterLocationEventName: function () {
                    if (privateMethods.detectUIRouter()) {
                        return '$stateChangeSuccess';
                    }
                    return '$locationChangeSuccess';
                },
                getMessageTemplate: function (msg, type) {
                    var template = '' +
                        '<div class="dialog-body-md-del">' +
                        '   <span><em class="fa fa-exclamation-triangle"></em><i></i>' + msg + '</span>' +
                        '</div>' +
                        '<div class="qc-dialog-footer">' +
                        '   <div class="text-center">';
                    if (type === 'confirm') {
                        template += '<button class="butn butn-default " ng-click="confirm()">确认</button>';
                    }
                    if (type === 'confirmCancel') {
                        template += '<button class="butn butn-default " ng-click="confirm()">确认</button>';
                        template += '<button class="butn butn-primary " ng-click="closeThisDialog(this)">取消</button>';
                    }
                    if (type != 'confirmCancel') {
                        template += '<button class="butn butn-primary " ng-click="closeThisDialog(this)">关闭</button>';
                    }

                    template +=
                        '   </div>' +
                        '</div>' +
                        '<div class="qc-dialog-close-base" ng-click="closeThisDialog(this)"></div>';
                    return template;
                }
            };

            var publicMethods = {

                /*
                 * @param {Object} options:
                 * - template {String} - id of ng-template, url for partial, plain string (if enabled)
                 * - plain {Boolean} - enable plain string templates, default false
                 * - scope {Object}
                 * - controller {String}
                 * - controllerAs {String}
                 * - className {String} - dialog theme class
                 * - disableAnimation {Boolean} - set to true to disable animation
                 * - showClose {Boolean} - show close button, default true
                 * - closeByEscape {Boolean} - default true
                 * - closeByDocument {Boolean} - default true
                 * - preCloseCallback {String|Function} - user supplied function name/function called before closing dialog (if set)
                 *
                 * @return {Object} dialog
                 */
                open: function (opts) {
                    var options = angular.copy(defaults);
                    var localID = ++globalID;
                    var dialogID = 'qcDialog' + localID;
                    openIdStack.push(dialogID);

                    opts = opts || {};
                    angular.extend(options, opts);

                    var defer;
                    defers[dialogID] = defer = $q.defer();

                    var scope;
                    scopes[dialogID] = scope = angular.isObject(options.scope) ? options.scope.$new() : $rootScope.$new();
                    if (options.title) {
                        scope.title = options.title;
                    }
                    var $dialog, $dialogParent;

                    var resolve = angular.extend({}, options.resolve);

                    angular.forEach(resolve, function (value, key) {
                        resolve[key] = angular.isString(value) ? $injector.get(value) : $injector.invoke(value, null, null, key);
                    });

                    $q.all({
                        template: loadTemplate(options.template || options.templateUrl),
                        locals: $q.all(resolve)
                    }).then(function (setup) {
                        var template = setup.template,
                            locals = setup.locals;
                        if (options.title) {
                            template = '<div class="qc-dialog-header"> <div class="qc-dialog-title" ng-bind="title"></div> </div>' + template;
                        }
                        if (options.showClose) {
                            template += '<div class="qc-dialog-close"></div>';
                        }
                        $dialog = $el('<div id="qcDialog' + localID + '" class="qc-dialog"></div>');
                        $dialog.html((options.overlay ?
                        '<div class="qc-dialog-overlay"></div><div class="qc-dialog-content" role="document">' + template + '</div>' :
                        '<div class="qc-dialog-content" role="document">' + template + '</div>'));
                        //设置宽度
                        if (options.width) {
                            angular.element('.qc-dialog-content', $dialog).css({'width': options.width});
                        }
                        if (options.draggable) {//添加拖拽功能
                            angular.element('.qc-dialog-content', $dialog).attr('data-drag', true);
                            angular.element('.qc-dialog-content', $dialog).attr('qc-draggable', '');
                            angular.element('.qc-dialog-content', $dialog).attr('data-qc-options', '{cancel:".qc-dialog-header~"}');//只能在header拖拽
                        }
                        $dialog.data('$qcDialogOptions', options);

                        scope.qcDialogId = dialogID;

                        if (options.data && angular.isString(options.data)) {
                            var firstLetter = options.data.replace(/^\s*/, '')[0];
                            scope.qcDialogData = (firstLetter === '{' || firstLetter === '[') ? angular.fromJson(options.data) : options.data;
                            scope.qcDialogData.qcDialogId = dialogID;
                        } else if (options.data && angular.isObject(options.data)) {
                            scope.qcDialogData = options.data;
                            scope.qcDialogData.qcDialogId = dialogID;
                        }

                        if (options.controller && (angular.isString(options.controller) || angular.isArray(options.controller) || angular.isFunction(options.controller))) {

                            var label;

                            if (options.controllerAs && angular.isString(options.controllerAs)) {
                                label = options.controllerAs;
                            }

                            var controllerInstance = $controller(options.controller, angular.extend(
                                    locals,
                                    {
                                        $scope: scope,
                                        $element: $dialog
                                    }),
                                null,
                                label
                            );
                            $dialog.data('$qcDialogControllerController', controllerInstance);
                        }

                        if (options.className) {
                            $dialog.addClass(options.className);
                        }

                        if (options.disableAnimation) {
                            $dialog.addClass(disabledAnimationClass);
                        }

                        if (options.appendTo && angular.isString(options.appendTo)) {
                            $dialogParent = angular.element(document.querySelector(options.appendTo));
                        } else {
                            $dialogParent = $elements.body;
                        }

                        privateMethods.applyAriaAttributes($dialog, options);

                        if (options.preCloseCallback) {
                            var preCloseCallback;

                            if (angular.isFunction(options.preCloseCallback)) {
                                preCloseCallback = options.preCloseCallback;
                            } else if (angular.isString(options.preCloseCallback)) {
                                if (scope) {
                                    if (angular.isFunction(scope[options.preCloseCallback])) {
                                        preCloseCallback = scope[options.preCloseCallback];
                                    } else if (scope.$parent && angular.isFunction(scope.$parent[options.preCloseCallback])) {
                                        preCloseCallback = scope.$parent[options.preCloseCallback];
                                    } else if ($rootScope && angular.isFunction($rootScope[options.preCloseCallback])) {
                                        preCloseCallback = $rootScope[options.preCloseCallback];
                                    }
                                }
                            }

                            if (preCloseCallback) {
                                $dialog.data('$qcDialogPreCloseCallback', preCloseCallback);
                            }
                        }

                        scope.closeThisDialog = function (value) {
                            privateMethods.closeDialog($dialog, value);
                        };

                        $timeout(function () {
                            var $activeDialogs = document.querySelectorAll('.qc-dialog');
                            privateMethods.deactivateAll($activeDialogs);

                            $compile($dialog)(scope);
                            var widthDiffs = $window.innerWidth - $elements.body.prop('clientWidth');
                            $elements.html.addClass('qc-dialog-open');
                            $elements.body.addClass('qc-dialog-open');
                            var scrollBarWidth = widthDiffs - ($window.innerWidth - $elements.body.prop('clientWidth'));
                            if (scrollBarWidth > 0) {
                                privateMethods.setBodyPadding(scrollBarWidth);
                            }
                            $dialogParent.append($dialog);

                            privateMethods.activate($dialog);

                            if (options.trapFocus) {
                                privateMethods.autoFocus($dialog);
                            }

                            if (options.name) {
                                $rootScope.$broadcast('qc-dialog.opened', {dialog: $dialog, name: options.name});
                            } else {
                                $rootScope.$broadcast('qc-dialog.opened', $dialog);
                            }
                        });

                        if (!keydownIsBound) {
                            $elements.body.bind('keydown', privateMethods.onDocumentKeydown);
                            keydownIsBound = true;
                        }

                        if (options.closeByNavigation) {
                            var eventName = privateMethods.getRouterLocationEventName();
                            $rootScope.$on(eventName, function () {
                                privateMethods.closeDialog($dialog);
                            });
                        }

                        if (options.preserveFocus) {
                            $dialog.data('$qcDialogPreviousFocus', document.activeElement);
                        }

                        closeByDocumentHandler = function (event) {
                            var isOverlay = options.closeByDocument ? $el(event.target).hasClass('qc-dialog-overlay') : false;
                            var isCloseBtn = $el(event.target).hasClass('qc-dialog-close');

                            if (isOverlay || isCloseBtn) {
                                publicMethods.close($dialog.attr('id'), isCloseBtn ? '$closeButton' : '$document');
                            }
                        };

                        if (typeof $window.Hammer !== 'undefined') {
                            var hammerTime = scope.hammerTime = $window.Hammer($dialog[0]);
                            hammerTime.on('tap', closeByDocumentHandler);
                        } else {
                            $dialog.bind('click', closeByDocumentHandler);
                        }

                        dialogsCount += 1;

                        return publicMethods;
                    });

                    return {
                        id: dialogID,
                        closePromise: defer.promise,
                        close: function (value) {
                            privateMethods.closeDialog($dialog, value);
                        }
                    };

                    function loadTemplateUrl(tmpl, config) {
                        $rootScope.$broadcast('qcDialog.templateLoading', tmpl);
                        return $http.get(tmpl, (config || {})).then(function (res) {
                            $rootScope.$broadcast('qcDialog.templateLoaded', tmpl);
                            return res.data || '';
                        });
                    }

                    function loadTemplate(tmpl) {
                        if (!tmpl) {
                            return 'Empty template';
                        }

                        if (angular.isString(tmpl) && options.plain) {
                            return tmpl;
                        }

                        if (typeof options.cache === 'boolean' && !options.cache) {
                            return loadTemplateUrl(tmpl, {cache: false});
                        }

                        return loadTemplateUrl(tmpl, {cache: $templateCache});
                    }
                },

                /*
                 * @param {Object} options:
                 * - template {String} - id of ng-template, url for partial, plain string (if enabled)
                 * - plain {Boolean} - enable plain string templates, default false
                 * - name {String}
                 * - scope {Object}
                 * - controller {String}
                 * - controllerAs {String}
                 * - className {String} - dialog theme class
                 * - showClose {Boolean} - show close button, default true
                 * - closeByEscape {Boolean} - default false
                 * - closeByDocument {Boolean} - default false
                 * - preCloseCallback {String|Function} - user supplied function name/function called before closing dialog (if set); not called on confirm
                 *
                 * @return {Object} dialog
                 */
                openConfirm: function (opts) {
                    var defer = $q.defer();

                    var options = {
                        closeByEscape: false,
                        closeByDocument: false
                    };
                    angular.extend(options, opts);

                    options.scope = angular.isObject(options.scope) ? options.scope.$new() : $rootScope.$new();
                    options.scope.confirm = function (value) {
                        defer.resolve(value);
                        var $dialog = $el(document.getElementById(openResult.id));
                        privateMethods.performCloseDialog($dialog, value);
                    };

                    var openResult = publicMethods.open(options);
                    openResult.closePromise.then(function (data) {
                        if (data) {
                            return defer.reject(data.value);
                        }
                        return defer.reject();
                    });

                    return defer.promise;
                },

                openMessage: function (opts) {
                    var defer = $q.defer();


                    var options = {
                        closeByEscape: false,
                        closeByDocument: false,
                        plain: true,
                        showClose: false,
                        template: privateMethods.getMessageTemplate(opts.msg, opts.type)
                    };
                    angular.extend(options, opts);

                    options.scope = angular.isObject(options.scope) ? options.scope.$new() : $rootScope.$new();
                    options.scope.confirm = function (value) {
                        defer.resolve(value);
                        var $dialog = $el(document.getElementById(openResult.id));
                        privateMethods.performCloseDialog($dialog, value);
                    };

                    var openResult = publicMethods.open(options);
                    openResult.closePromise.then(function (data) {
                        if (data) {
                            return defer.reject(data.value);
                        }
                        return defer.reject();
                    });

                    return defer.promise;
                },
                openCover: function (opts) {
                    var options = {
                        className: 'qc-dialog-theme-cover',
                        draggable: false,
                        closeByEscape: false,
                        closeByDocument: false
                    };
                    angular.extend(options, opts);

                    var openResult = publicMethods.open(options);
                    return openResult;
                },

                isOpen: function (id) {
                    var $dialog = $el(document.getElementById(id));
                    return $dialog.length > 0;
                },

                /*
                 * @param {String} id
                 * @return {Object} dialog
                 */
                close: function (id, value) {
                    var $dialog = $el(document.getElementById(id));

                    if ($dialog.length) {
                        privateMethods.closeDialog($dialog, value);
                    } else {
                        if (id === '$escape') {
                            var topDialogId = openIdStack[openIdStack.length - 1];
                            $dialog = $el(document.getElementById(topDialogId));
                            if ($dialog.data('$qcDialogOptions').closeByEscape) {
                                privateMethods.closeDialog($dialog, value);
                            }
                        } else {
                            publicMethods.closeAll(value);
                        }
                    }

                    return publicMethods;
                },

                closeAll: function (value) {
                    var $all = document.querySelectorAll('.qc-dialog');

                    // Reverse order to ensure focus restoration works as expected
                    for (var i = $all.length - 1; i >= 0; i--) {
                        var dialog = $all[i];
                        privateMethods.closeDialog($el(dialog), value);
                    }
                },

                getOpenDialogs: function () {
                    return openIdStack;
                },

                getDefaults: function () {
                    return defaults;
                }
            };

            return publicMethods;
        }];
});
/*华丽的分隔线*/
qc.service('qcFlashVersion', ['$window', function ($window) {
    var i_flash;
    var v_flash;
    // Netscape
    if ($window.document.all) {
        var swf;
        try {
            swf = new $window.ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        } catch (e) {
        }
        if (swf) {
            i_flash = true;
            var VSwf = swf.GetVariable("$version");
            v_flash = VSwf.split(" ")[1];
            v_flash = v_flash.substring(0, v_flash.indexOf(','));
        }
    }
    else {
        for (var i = 0; i < $window.navigator.plugins.length; i++) {
            if ($window.navigator.plugins[i].name.toLowerCase().indexOf("shockwave flash") >= 0) {
                i_flash = true;
                v_flash = $window.navigator.plugins[i].description.substring($window.navigator.plugins[i].description.toLowerCase().lastIndexOf("flash ") + 6, $window.navigator.plugins[i].description.length);
                v_flash = v_flash.substring(0, v_flash.indexOf('.'));
            }
        }
    }
    this.installed = i_flash;
    this.version = v_flash;
}]);
/*华丽的分隔线*/
qc.config(['$httpProvider', function ($httpProvider) {
    var interceptorQc = ['qcMessage', '$q', 'urlConfig', function (qcMessage, $q, urlConfig) {
        return {
            'request': function (config) {
                return config;
            },
            'response': function (response) {
                if (response.data && response.data.responseType === 'mtour data') {
                    if (response.data.responseCode === 'success') {
                        //response.data = response.data;
                        return response;
                    }
                    else if (response.data.responseCode === 'fail') {
                        qcMessage.warning(response.data.msg.code + ':' + response.data.msg.description);
                        return $q.reject(response);
                    }
                    else if (response.data.responseCode === 'authentication') {
                        qcMessage.warning('您还没有登录,请重新登录!')
                            .then(function () {
                                window.location = urlConfig.mtourLoginUrl;
                            });
                        return $q.reject(response);
                    }
                    else if (response.data.responseCode === 'error') {
                        qcMessage.error('服务器发生异常,请联系管理员');
                        return $q.reject(response);
                    } else {
                        qcMessage.error('未定义的返回代码:' + response.data.responseCode);
                    }
                }
                else {
                    return response;
                }
            },
            'responseError': function (rejection) {
                if (rejection.responseType === 'mtour data') {
                    return $q.reject(rejection);
                } else {
                    qcMessage.error('发生未知异常,请联系管理员');
                    return $q.reject(rejection);
                }
            }
        };
    }];
    $httpProvider.interceptors.push(interceptorQc);
}]);
/*华丽的分隔线*/
/*! 
 * angular-loading-bar v0.8.0
 * https://chieffancypants.github.io/angular-loading-bar
 * Copyright (c) 2015 Wes Cruver
 * License: MIT
 */
/*
 * angular-loading-bar
 *
 * intercepts XHR requests and creates a loading bar.
 * Based on the excellent nprogress work by rstacruz (more info in readme)
 *
 * (c) 2013 Wes Cruver
 * License: MIT
 */
/**
 * @module qc
 * @version 2.0.0
 * @description 项目加载数据时的loading提示
 * @requires angular.js,basic.js,qc.module.js
 */

/**
 * loading bar provider,显示loading bar 或者spinner,可以被直接调用,或者由$http拦截器调用
 */
qc.provider('qcLoadingBar', function () {

    this.autoIncrement = true;
    this.includeSpinner = true;
    this.includeBar = false;
    this.latencyThreshold = 100;
    this.startSize = 0.02;
    this.parentSelector = 'body';
    this.spinnerTemplate = '<div id="qc-loading-bar-spinner"><div class="spinner-icon"></div></div>';
    this.loadingBarTemplate = '<div id="qc-loading-bar"><div class="bar"><div class="peg"></div></div></div>';

    this.$get = ['$injector', '$document', '$timeout', '$rootScope', function ($injector, $document, $timeout, $rootScope) {
        var $animate;
        var $parentSelector = this.parentSelector,
            loadingBarContainer = angular.element(this.loadingBarTemplate),
            loadingBar = loadingBarContainer.find('div').eq(0),
            spinner = angular.element(this.spinnerTemplate);

        var incTimeout,
            completeTimeout,
            started = false,
            status = 0;

        var autoIncrement = this.autoIncrement;
        var includeSpinner = this.includeSpinner;
        var includeBar = this.includeBar;
        var startSize = this.startSize;

        /**
         * Inserts the loading bar element into the dom, and sets it to 2%
         */
        function _start() {
            if (!$animate) {
                $animate = $injector.get('$animate');
            }

            var $parent = $document.find($parentSelector).eq(0);
            $timeout.cancel(completeTimeout);

            // do not continually broadcast the started event:
            if (started) {
                return;
            }

            $rootScope.$broadcast('qcLoadingBar:started');
            started = true;

            if (includeBar) {
                $animate.enter(loadingBarContainer, $parent, angular.element($parent[0].lastChild));
            }

            if (includeSpinner) {
                $animate.enter(spinner, $parent, angular.element($parent[0].lastChild));
            }

            _set(startSize);
        }

        /**
         * Set the loading bar's width to a certain percent.
         *
         * @param n any value between 0 and 1
         */
        function _set(n) {
            if (!started) {
                return;
            }
            var pct = (n * 100) + '%';
            loadingBar.css('width', pct);
            status = n;

            // increment loadingbar to give the illusion that there is always
            // progress but make sure to cancel the previous timeouts so we don't
            // have multiple incs running at the same time.
            if (autoIncrement) {
                $timeout.cancel(incTimeout);
                incTimeout = $timeout(function () {
                    _inc();
                }, 250);
            }
        }

        /**
         * Increments the loading bar by a random amount
         * but slows down as it progresses
         */
        function _inc() {
            if (_status() >= 1) {
                return;
            }

            var rnd = 0;

            // TODO: do this mathmatically instead of through conditions

            var stat = _status();
            if (stat >= 0 && stat < 0.25) {
                // Start out between 3 - 6% increments
                rnd = (Math.random() * (5 - 3 + 1) + 3) / 100;
            } else if (stat >= 0.25 && stat < 0.65) {
                // increment between 0 - 3%
                rnd = (Math.random() * 3) / 100;
            } else if (stat >= 0.65 && stat < 0.9) {
                // increment between 0 - 2%
                rnd = (Math.random() * 2) / 100;
            } else if (stat >= 0.9 && stat < 0.99) {
                // finally, increment it .5 %
                rnd = 0.005;
            } else {
                // after 99%, don't increment:
                rnd = 0;
            }

            var pct = _status() + rnd;
            _set(pct);
        }

        function _status() {
            return status;
        }

        function _completeAnimation() {
            status = 0;
            started = false;
        }

        function _complete() {
            if (!$animate) {
                $animate = $injector.get('$animate');
            }

            $rootScope.$broadcast('qcLoadingBar:completed');
            _set(1);

            $timeout.cancel(completeTimeout);

            // Attempt to aggregate any start/complete calls within 500ms:
            completeTimeout = $timeout(function () {
                var promise = $animate.leave(loadingBarContainer, _completeAnimation);
                if (promise && promise.then) {
                    promise.then(_completeAnimation);
                }
                $animate.leave(spinner);
            }, 500);
        }

        return {
            start: _start,
            set: _set,
            status: _status,
            inc: _inc,
            complete: _complete,
            autoIncrement: this.autoIncrement,
            includeSpinner: this.includeSpinner,
            latencyThreshold: this.latencyThreshold,
            parentSelector: this.parentSelector,
            startSize: this.startSize
        };


    }];     //
});       // wtf javascript. srsly

/**
 * loadingBar拦截器
 *监控angular 的$http服务
 */
qc.config(['$httpProvider', function ($httpProvider) {

    var interceptor = ['$q', '$cacheFactory', '$timeout', '$rootScope', '$log', 'qcLoadingBar', function ($q, $cacheFactory, $timeout, $rootScope, $log, qcLoadingBar) {

        /**
         * The total number of requests made
         */
        var reqsTotal = 0;

        /**
         * The number of requests completed (either successfully or not)
         */
        var reqsCompleted = 0;

        /**
         * The amount of time spent fetching before showing the loading bar
         */
        var latencyThreshold = qcLoadingBar.latencyThreshold;

        /**
         * $timeout handle for latencyThreshold
         */
        var startTimeout;


        /**
         * calls qcLoadingBar.complete() which removes the
         * loading bar from the DOM.
         */
        function setComplete() {
            $timeout.cancel(startTimeout);
            qcLoadingBar.complete();
            reqsCompleted = 0;
            reqsTotal = 0;
        }

        /**
         * Determine if the response has already been cached
         * @param  {Object}  config the config option from the request
         * @return {Boolean} retrns true if cached, otherwise false
         */
        function isCached(config) {
            var cache;
            var defaultCache = $cacheFactory.get('$http');
            var defaults = $httpProvider.defaults;

            // Choose the proper cache source. Borrowed from angular: $http service
            if ((config.cache || defaults.cache) && config.cache !== false &&
                (config.method === 'GET' || config.method === 'JSONP')) {
                cache = angular.isObject(config.cache) ? config.cache
                    : angular.isObject(defaults.cache) ? defaults.cache
                    : defaultCache;
            }

            var cached = cache !== undefined ?
            cache.get(config.url) !== undefined : false;

            if (config.cached !== undefined && cached !== config.cached) {
                return config.cached;
            }
            config.cached = cached;
            return cached;
        }


        return {
            'request': function (config) {
                // Check to make sure this request hasn't already been cached and that
                // the requester didn't explicitly ask us to ignore this request:
                if (!config.ignoreLoadingBar && !isCached(config)) {
                    $rootScope.$broadcast('qcLoadingBar:loading', {url: config.url});
                    if (reqsTotal === 0) {
                        startTimeout = $timeout(function () {
                            qcLoadingBar.start();
                        }, latencyThreshold);
                    }
                    reqsTotal++;
                    qcLoadingBar.set(reqsCompleted / reqsTotal);
                }
                return config;
            },

            'response': function (response) {
                if (!response || !response.config) {
                    $log.error('Broken interceptor detected: Config object not supplied in response:\n https://github.com/chieffancypants/angular-loading-bar/pull/50');
                    return response;
                }

                if (!response.config.ignoreLoadingBar && !isCached(response.config)) {
                    reqsCompleted++;
                    $rootScope.$broadcast('qcLoadingBar:loaded', {url: response.config.url, result: response});
                    if (reqsCompleted >= reqsTotal) {
                        setComplete();
                    } else {
                        qcLoadingBar.set(reqsCompleted / reqsTotal);
                    }
                }
                return response;
            },

            'responseError': function (rejection) {
                if (!rejection || !rejection.config) {
                    $log.error('Broken interceptor detected: Config object not supplied in rejection:\n https://github.com/chieffancypants/angular-loading-bar/pull/50');
                    return $q.reject(rejection);
                }

                if (!rejection.config.ignoreLoadingBar && !isCached(rejection.config)) {
                    reqsCompleted++;
                    $rootScope.$broadcast('qcLoadingBar:loaded', {
                        url: rejection.config.url,
                        result: rejection
                    });
                    if (reqsCompleted >= reqsTotal) {
                        setComplete();
                    } else {
                        qcLoadingBar.set(reqsCompleted / reqsTotal);
                    }
                }
                return $q.reject(rejection);
            }
        };
    }];

    $httpProvider.interceptors.push(interceptor);
}]);

/*华丽的分隔线*/
qc.provider('qcMessage', function () {
    var messageOptions = {
        infoShowTime: 2,//默认5s
        tipShowTime: 5//默认5s
    };
    this.$get = ['$timeout', '$q', function ($timeout, $q) {
        var isDef = angular.isDefined;
        var style = (document.body || document.documentElement).style;
        var animationEndSupport = isDef(style.animation) || isDef(style.WebkitAnimation) || isDef(style.MozAnimation) || isDef(style.MsAnimation) || isDef(style.OAnimation);
        var animationEndEvent = 'animationend webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend';

        var containerTemplate = '<div class="qc-message-container"></div>';
        var overlayTemplate = '<div class="qc-message-overlay"></div>';
        var contentTemplate = '' +
            '<div class="qc-message-content">' +
            '    <div class="qc-message-header">' +
            '        <div class="qc-message-title"></div>' +
            '    </div>' +
            '    <div class="qc-message-body">' +
            '    </div>' +
            '    <div class="qc-message-footer">' +
            '    </div>' +
            '</div>';
        var warning = function (msg) {
            var defer = $q.defer();
            var $container = angular.element(containerTemplate);
            angular.element(document.body).append($container);

            $container.addClass('warning');

            var $content = angular.element(contentTemplate);
            angular.element('.qc-message-title', $content).html('!!!Warning');
            angular.element('.qc-message-body', $content).html(msg);

            var $closeButton = angular.element('<button class="qc-message-button-close btn btn-default">关闭</button>');
            angular.element('.qc-message-footer', $content).append($closeButton);
            $closeButton.on('click', function () {
                closeAnimateElement(angular.element('.qc-message-content', $container), $container);
                closeAnimateElement(angular.element('.qc-message-overlay', $container));
                return defer.resolve('close');
            });
            $container.append(overlayTemplate);
            $container.append($content);
            return defer.promise;
        };
        var error = function (msg) {
            var defer = $q.defer();
            var $container = angular.element(containerTemplate);
            angular.element(document.body).append($container);

            $container.addClass('error');

            var $content = angular.element(contentTemplate);
            angular.element('.qc-message-title', $content).html('*&@Error');
            angular.element('.qc-message-body', $content).html(msg);

            var $closeButton = angular.element('<button class="qc-message-button-close btn btn-default">关闭</button>');
            angular.element('.qc-message-footer', $content).append($closeButton);
            $closeButton.on('click', function () {
                closeAnimateElement(angular.element('.qc-message-content', $container), $container);
                closeAnimateElement(angular.element('.qc-message-overlay', $container));
                return defer.resolve('close');
            });
            $container.append(overlayTemplate);
            $container.append($content);
            return defer.promise;
        };
        var confirm = function (msg) {
            var defer = $q.defer();
            var $container = angular.element(containerTemplate);
            angular.element(document.body).append($container);
            $container.addClass('confirm');
            var $content = angular.element(contentTemplate);
            angular.element('.qc-message-title', $content).html('???Confirm');
            angular.element('.qc-message-body', $content).html(msg);
            var $confirmButton = angular.element('<button class="qc-message-button-confirm btn btn-primary">确认</button>');
            var $cancelButton = angular.element('<button class="qc-message-button-cancel btn btn-default">取消</button>');
            angular.element('.qc-message-footer', $content).append($confirmButton);
            angular.element('.qc-message-footer', $content).append("<span> </span>");
            angular.element('.qc-message-footer', $content).append($cancelButton);

            $confirmButton.on('click', function () {
                closeAnimateElement(angular.element('.qc-message-content', $container), $container);
                closeAnimateElement(angular.element('.qc-message-overlay', $container));
                defer.resolve('confirm');
            });
            $cancelButton.on('click', function () {
                closeAnimateElement(angular.element('.qc-message-content', $container), $container);
                closeAnimateElement(angular.element('.qc-message-overlay', $container));
                defer.reject('cancel');
            });
            $container.append(overlayTemplate);
            $container.append($content);
            return defer.promise;
        };
        var tip = function (msg) {
            var tipContainer = angular.element(document.querySelector('.qc-message-container.tip'));
            if (!tipContainer.length) {
                tipContainer = angular.element('<div class="qc-message-container tip"></div>');
                angular.element(document.body).append(tipContainer);
            }
            var $tip = angular.element('<div class="qc-message-content">' + msg + '</div>');
            tipContainer.prepend($tip);
            $timeout(function () {
                closeAnimateElement($tip);
            }, messageOptions.tipShowTime * 1000);
        };
        var info = function (msg) {
            var infoContainer = angular.element(document.querySelector('.qc-message-container.info'));
            if (!infoContainer.length) {
                infoContainer = angular.element('<div class="qc-message-container info"></div>');
                angular.element(document.body).append(infoContainer);
            }
            var $info = angular.element('<div class="qc-message-content">' + msg + '</div>');
            infoContainer.prepend($info);
            $timeout(function () {
                closeAnimateElement($info);
            }, messageOptions.infoShowTime * 1000);
        };
        var closeAnimateElement = function ($el, $removeEl) {
            if (!$removeEl) {
                $removeEl = $el;
            }
            if (animationEndSupport) {
                $el.addClass('closing');
                $el.on(animationEndEvent, function () {
                    $removeEl.remove();
                });
            } else {
                $removeEl.remove();
            }
        };
        return {
            warning: warning,
            error: error,
            confirm: confirm,
            info: info,
            tip: tip
        };
    }];
});
/*华丽的分隔线*/
qc.factory('qcObjectInArray', function () {
    return function (array, key, value) {
        if (!angular.isArray(array)) {
            return undefined;
        }
        var length = array.length;
        var index = 0;
        var returnObject;
        while (index < length) {
            if (array[index][key] == value) {
                returnObject = array[index];
                break;
            }
            index++;
        }
        return returnObject;
    };
});