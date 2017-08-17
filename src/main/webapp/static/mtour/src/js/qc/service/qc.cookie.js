/**
 * @module qc
 * @version 2.0.0
 * @description 项目加载数据的共同的api服务,整个项目都要使用该provider来进行数据交互
 * @requires angular.js,basic.js,qc.module.js
 */

qc.service('qcCookie', ['$document', function ($document) {
    this.get = function (key) {
        if ($document[0].cookie && $document[0].cookie.length>0)
        {
           var c_start=$document[0].cookie.indexOf(key + '=');
            if (c_start!==-1)
            {
                c_start=c_start + key.length+1;
                var c_end=$document[0].cookie.indexOf(';',c_start);
                if (c_end===-1) {
                    c_end=$document[0].cookie.length;
                }
                return angular.fromJson($document[0].cookie.substring(c_start,c_end));
            }
        }
        return '';
    };
    this.set = function (key, value, expiredays) {
        var expires ='';
        if(!angular.isUndefined(expiredays)){
            var d = new Date();
            d.setTime(d.getTime() + (expiredays*24*60*60*1000));
            var expires = 'expires='+d.toUTCString();
        }
        if(expires){
            $document[0].cookie = key + '=' + angular.toJson(value) + '; ' + expires+'; path=/';
        }
        else{
            $document[0].cookie = key + '=' + angular.toJson(value)+'; path=/';
        }
    };
    this.remove= function (key) {
        this.set(key,'',-1);
    };
}]);