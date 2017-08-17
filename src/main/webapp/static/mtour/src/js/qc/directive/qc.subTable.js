/**
 * @module qc
 * @version 2.0.0
 * @description 项目的子表展开和隐藏
 * @requires angular.js,basic.js,qc.module.js
 */


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

qc.directive('qcSubTable', ['$timeout',function ($timeout) {
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
