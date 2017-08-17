/**
 * @module qc.module
 * @version 2.0.0
 * @author yongsheng.kuang
 * @description
 * @requires angular.js
 */

/**
 * @description：qc module,定义项目必要的angular组件
 * @global
 * @requires jquery.js,jquery-ui.js,angular.js,basic.js
 */
var qc = angular.module('qc',[]);

/**
 * @description api服务端信息.发布时,根据服务器不同需要重写
 */
qc.config(['$httpProvider','$provide', function ($httpProvider,$provide) {
    //if (!$httpProvider.defaults.headers.get) {
    //    $httpProvider.defaults.headers.get = {};
    //}
    //$httpProvider.defaults.headers.get['Cache-Control'] = 'no-cache';
    //$httpProvider.defaults.headers.get['Pragma'] = 'no-cache';
    //$httpProvider.defaults.headers.common['Pragma'] = 'no-cache';
    //$httpProvider.defaults.headers.common['Cache-Control'] = 'no-cache';
    $provide.constant('urlConfig',{
        mtourApiUrl:window.mtourApiUrl?window.mtourApiUrl:'http://localhost:8080/trekiz_wholesaler_tts/a/mtour/',
        mtourStaticUrl:window.mtourStaticUrl?window.mtourStaticUrl: 'http://localhost:8080/trekiz_wholesaler_tts/static/mtour/static',
        mtourHtmlTemplateUrl:window.mtourHtmlTemplateUrl?window.mtourHtmlTemplateUrl: 'http://localhost:8080/trekiz_wholesaler_tts/static/mtour/html/',
        mtourLoginUrl:window.mtourLoginUrl?window.mtourLoginUrl: 'http://localhost:8080/trekiz_wholesaler_tts/a/login',
        mtourUploadFileUrl:window.mtourUploadFileUrl?window.mtourUploadFileUrl: 'http://localhost:8080//trekiz_wholesaler_tts/a/sys/docinfo/download/',
        mtourLogoutUrl:window.mtourLogoutUrl?window.mtourLogoutUrl: 'http://localhost:8080/trekiz_wholesaler_tts/a/logout',
        mtourBaseUrl:window.mtourBaseUrl?window.mtourBaseUrl: 'http://localhost:8080/trekiz_wholesaler_tts/a/'
    });
}]);
