/**
 * @module qc
 * @version 2.0.0
 * @description 项目的下拉框指令
 * @requires jquery.js,angular.js,basic.js,qc.module.js
 */

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
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="'+dropText+'">' + dropText + '</span>';
            dropdownTextTemplate += '<span class="qc-dropdown-anchor">▼</span>';
            dropdownTextTemplate += '</div>';

            htmlArray.push(dropdownTextTemplate);
            //htmlArray.push('<input class="qc-dropdown-filter" ng-model="filterText" ng-hide="!active || !filterExpression">');

            htmlArray.push('<div class="qc-dropdown-container" ng-if="active"  ng-style="{\'width\':listWidth}">');



            if (attrs.listMaxHeight) {
                htmlArray.push('<ul class="qc-dropdown-list qc-scroll" ng-style="{\'max-height\':listMaxHeight}">');
            } else {
                htmlArray.push('<ul class="qc-dropdown-list"  ng-style="{\'max-height\':listMaxHeight}">');
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
                scope.$emit(attrs.ngModel+'.change');
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
            dropdownTextTemplate += '<span  class="qc-dropdown-text" title="'+dropText+'">' + dropText + '</span>';
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

            htmlArray.push('<li class="qc-dropdown-item"' +
            'ng-click="select(qcDropdownItem)" ng-repeat="qcDropdownItem in items" ng-class="{\'active\':getItemInNgModel(qcDropdownItem)}">' + attrs.itemText +
            '</li>');
            htmlArray.push('</ul>');
            htmlArray.push('</div>');
            htmlArray.push('</div>');
            return htmlArray.join('');
        },
        link: function (scope, ele, attrs, ngModel) {
//            if(scope.multiple && !ngModel.$modelValue){
//                ngModel.$setViewValue([]);
//            }
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
            var dropdownTextTemplate = '<div class="qc-dropdown-text-container" ng-click="showList()">';
            //var dropText = '请选择';
            if (attrs.dropdownText) {
                dropText = attrs.dropdownText;
            }
            //dropdownTextTemplate += '<span  class="qc-dropdown-text" title="'+dropText+'">' + dropText + '</span>';
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