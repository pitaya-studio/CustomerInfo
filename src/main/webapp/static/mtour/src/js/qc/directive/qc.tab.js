/**
 * @module qc
 * @version 2.0.0
 * @description 项目的基本tab指令
 * @requires angular.js,basic.js,qc.module.js
 */

/**
 * @description directive tab容器
 */
qc.directive('qcTab',function(){
    return {
        restrict: 'AE',
        controller:function($scope){
            this.showTab= function (tabName,oldTableName) {
                $scope.activeTab=tabName;
                if(angular.isFunction($scope.qcTabChanged)){
                    if(tabName!==oldTableName){
                        $scope.qcTabChanged(tabName,oldTableName);
                    }
                }
            };
        },
        scope:true,
        replace:false,
        link:function(scope,ele,attrs){
            scope.activeTab =attrs.activeTab;
            if(attrs.qcTabChanged){
                scope.qcTabChanged=scope.$eval(attrs.qcTabChanged);
            }
        }
    };
});

/**
 * @description directive tab的头部,依赖 qc-tab. 点击后显示与其qc-tab-name相同的 qc-tab-body
 */
qc.directive('qcTabHeader',function(){
    return {
        require:'^qcTab',
        restrict: 'AE',
        scope:true,
        replace:true,
        transclude:true,
        template:'<div  ng-transclude ng-class="{active:(qcTabName==activeTab)}"></div>',
        link: function (scope,ele,attrs,ctrl) {
            scope.qcTabName=attrs.qcTabName;
            ele.on('click',function(){
                scope.$apply(function(){
                    ctrl.showTab(scope.qcTabName,scope.activeTab);
                });

            });
        }
};
});
/**
 * @description directive tab页,依赖 qc-tab. 当与其对应的qc-tab-header 处于激活状态时显示
 */
qc.directive('qcTabBody',function(){
    return {
        restrict: 'AE',
        require:'^qcTab',
        scope:true,
        transclude:true,
        replace:true,
        template:'<div ng-hide="qcTabName!=activeTab" ng-transclude ></div>',
        link:function(scope,ele,attrs,ctrl){
            scope.qcTabName=attrs.qcTabName;
        }
    };
});