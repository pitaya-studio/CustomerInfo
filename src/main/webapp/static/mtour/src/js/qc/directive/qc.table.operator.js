/**
 * @module qc
 * @version 2.0.0
 * @description 表格中的操作项容器
 * @requires jquery.js,angular.js,basic.js,qc.module.js
 */

qc.directive('qcTableOperator', ['$timeout', function ($timeout) {
    return {
        restrict  : 'C',
        scope     : {
            qcTableOperatorOption: '='
        },
        template  : '<div ng-transclude ng-if="active"></div>',
        replace   : false,
        transclude: true,
        link      : function (scope, ele, attrs) {
            var defaultOptions = {
                activeDelay  : 300,
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