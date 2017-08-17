/**
 * Created by ding on 2015/8/25.
 */

var docModule = angular.module('docModule', []);

docModule.constant('docNavInfo', {
    menus: [
        {
            url: '/trekiz_wholesaler_tts/static/mtour/doc/standard/startup.html',
            code: 'startup',
            name: '引导程序'
        },
        {
            url: '/trekiz_wholesaler_tts/static/mtour/doc/standard/standard.html',
            code: 'standard',
            name: '项目标准'
        },
        {
            code: 'qc',
            name: '前端开发文档',
            subMenus: [
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/basic.html',
                    code: 'basic',
                    name: '基础'
                },
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/interact.html',
                    code: 'interact',
                    name: 'UI交互'
                },
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/table.html',
                    code: 'table',
                    name: '表格'
                },
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/common.html',
                    code: 'common',
                    name: '常用组件'
                },
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/valid.html',
                    code: 'valid',
                    name: '验证'
                },
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/server.html',
                    code: 'server',
                    name: '后台数据交互'
                },
                {
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/qc/uploader.html',
                    code: 'uploader',
                    name: '文件上传'
                }
            ]
        },
        {
            code: 'api',
            name: 'API文档',
            subMenus: [
                {
                    code: 'apitest',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/apitest.html',
                    name: 'Api 测试'
                },
                {
                    code: 'type',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/type.html',
                    name: '固定值'
                },
                {
                    code: 'common',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/common.html',
                    name: '基础信息'
                },
                {
                    code: 'order',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/order.html',
                    name: '订单'
                },
                {
                    code: 'operator',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/operator.html',
                    name: '计调销售'
                },
                {
                    code: 'finance',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/finance.html',
                    name: '财务'
                } ,
                {
                    code: 'print',
                    url: '/trekiz_wholesaler_tts/static/mtour/doc/api/print.html',
                    name: '打印'
                }
            ]
        }
    ]
});

docModule.directive('docNav', ['$window', '$rootScope', function ($window, $rootScope) {
    return {
        restrict: 'E',
        scope: {
            nav: '='
        },
        templateUrl: '/trekiz_wholesaler_tts/static/mtour/doc/static/template/docNav.html',
        replace: true,
        link: function (scope, ele, attrs) {
           
        }
    };
}]);


docModule.directive('docFooter', function () {
    return {
        restrict: 'E',
        scope: {
            nav: '='
        },
        templateUrl: '/trekiz_wholesaler_tts/static/mtour/doc/static/template/docFooter.html',
        replace: true
    };
});