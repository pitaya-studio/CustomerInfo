/**
 * @module qc
 * @version 2.0.0
 * @description  从一个数组中获取对应属性的对象
 * @requires angular.js,basic.js,qc.module.js
 */

qc.factory('qcObjectInArray', function () {
    return function (array,key,value) {
        if(!angular.isArray(array)){
            return undefined;
        }
        var length = array.length;
        var index=0;
        var returnObject;
        while(index<length){
            if(array[index][key]==value){
                returnObject=array[index];
                break;
            }
            index++;
        }
        return returnObject;
    };
});