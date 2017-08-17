/**
 * @module qc
 * @version 2.0.0
 * @description 项目加载数据的共同的api服务,整个项目都要使用该provider来进行数据交互
 * @requires angular.js,basic.js,qc.module.js
 */

qc.provider('qcApi', function () {
    this.$get = ['$http', 'urlConfig', function ($http, urlConfig) {
        //function get(router, config) {
        //    var apiUrl = apiConfig.url + ':' + apiConfig.port + '/' + router;
        //   return $http.get(apiUrl, config);
        //}
        function mtourPost(router,params) {

            var postData = "requestType=mtour data";
            if(params){
                //if(angular.isObject(params))
            	var param = base64encode(JSON.stringify(params));
                postData += "&param=" + param;
            }
//            postData =encodeURI(postData);
            var url=urlConfig.mtourApiUrl + router+'?date='+new Date();
            return $http.post(url,postData,{'headers':{'Content-Type':'application/x-www-form-urlencoded','x-requested-with':'XMLHttpRequest'}});
        }
        function mtourGet(router,params) {

            var postData={requestType:"mtour data",param:params};
            var url=urlConfig.mtourApiUrl + router+'?date='+new Date();
            return $http.get(url,postData,{'headers':{'Content-Type':'application/x-www-form-urlencoded','x-requested-with':'XMLHttpRequest'}});
        }
        return {
            get: mtourGet,
            post:mtourPost
        };
    }];
});