/**
 * @module qc
 * @version 2.0.0
 * @description 项目数组分页器
 * @requires jquery.js,angular.js,basic.js,qc.module.js
 */
qc.filter('qcPaging', function () {
    return function (arr, rowCount,index) {
        return arr.paging(rowCount,index-1);
    };
});