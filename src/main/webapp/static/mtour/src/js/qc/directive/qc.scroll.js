/**
 * @module qc
 * @version 2.0.0
 * @description 项目的滚动条
 * @requires jquery,jquery-nicescroll.js,angular.js,basic.js,qc.module.js
 */

/**
 * @description directive tab容器
 */
qc.directive('qcScroll',['$timeout',function($timeout){
    return {
        restrict: 'C',
        scope:{
          qcScrollOptions:'='
        },
        link:function(scope,ele,attrs){
            //滚动条默认属性
            //为了让样式(.nicescroll-cursors)能够起作用需要将部分属性设置为空
            var defaultOptions={
                zindex: "auto",
                cursoropacitymin: 0,
                cursoropacitymax: 1,
                cursorcolor: "",
                cursorwidth: "8px",
                cursorborder: "",
                cursorborderradius: "",
                scrollspeed: 60,
                mousescrollstep: 8 * 3,
                touchbehavior: false,
                hwacceleration: true,
                usetransition: true,
                boxzoom: false,
                dblclickzoom: true,
                gesturezoom: true,
                grabcursorenabled: true,
                autohidemode: 'leave',
                background: "",
                iframeautoresize: true,
                cursorminheight: 32,
                preservenativescrolling: true,
                railoffset: false,
                railhoffset: false,
                bouncescroll: true,
                spacebarenabled: true,
                railpadding: {
                    top: 0,
                    right: 0,
                    left: 0,
                    bottom: 0
                },
                disableoutline: true,
                horizrailenabled: true,
                railalign: "right",
                railvalign: "bottom",
                enabletranslate3d: true,
                enablemousewheel: true,
                enablekeyboard: true,
                smoothscroll: true,
                sensitiverail: true,
                enablemouselockapi: true,
                //      cursormaxheight:false,
                cursorfixedheight: false,
                directionlockdeadzone: 6,
                hidecursordelay:0,
                nativeparentscrolling: true,
                enablescrollonselection: true,
                overflowx: true,
                overflowy: true,
                cursordragspeed: 0.3,
                rtlmode: "auto",
                cursordragontouch: false,
                oneaxismousemode: "auto",
                //scriptpath: getScriptPath(),
                preventmultitouchscrolling: true,
                createDelay:400
            };
            var options = angular.extend(defaultOptions,scope.qcScrollOptions);
            $timeout(function () {
                ele.niceScroll(options);
            },options.createDelay);//
            scope.$on('qcTableContainer.reset', function () {
                $timeout(function () {
                    ele.getNiceScroll().resize();
                },500);
            });
        }
    };
}]);