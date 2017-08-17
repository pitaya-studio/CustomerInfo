/**
 * @module qc
 * @version 2.0.0
 * @description 项目的过滤器,通过多个属性来过滤数组
 * @requires jquery.js,angular.js,basic.js,qc.module.js
 */
qc.filter('qcPropsFilter', function () {
    return function (arr, propString, text) {
        if(angular.isUndefined(text)){
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