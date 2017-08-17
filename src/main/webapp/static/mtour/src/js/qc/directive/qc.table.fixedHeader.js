/**
 * @module qc
 * @version 2.0.0
 * @description 表格固定列头
 * @requires jquery.js,angular.js,basic.js,qc.module.js
 */
qc.directive('qcTableContainer', ['$window','$timeout', function ($window,$timeout) {
    return {
        restrict: 'C',
        link: function (scope, ele, attrs) {
            var bottomGap = (+attrs.bottomGap);
            if (isNaN(bottomGap)) {
                bottomGap = 0;
            }

            function setHeight() {
                var top = ele.offset().top;
                ele.css({'height': $($window).height() - (top  + bottomGap)});
            }
            $timeout(function () {
                setHeight();
            },1000);
            $($window).resize(function () {
                setHeight();
            });
            scope.$on('qcTableContainer.reset', function () {
                $timeout(function () {
                    setHeight();
                },1000);
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