/**
 * @module qc
 * @version 2.0.0
 * @description 项目消息窗service
 * @requires jquery.js,jquery-ui.js,angular.js,angular-dragdrop.js,basic.js,qc.module.js,qc.dialog.js
 */
qc.provider('qcMessage', function () {
    var messageOptions = {
        infoShowTime:2,//默认5s
        tipShowTime: 5//默认5s
    };
    this.$get = [ '$timeout','$q', function ($timeout,$q) {
        var isDef = angular.isDefined;
        var style = (document.body || document.documentElement).style;
        var animationEndSupport = isDef(style.animation) || isDef(style.WebkitAnimation) || isDef(style.MozAnimation) || isDef(style.MsAnimation) || isDef(style.OAnimation);
        var animationEndEvent = 'animationend webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend';

        var containerTemplate='<div class="qc-message-container"></div>';
        var overlayTemplate='<div class="qc-message-overlay"></div>';
        var contentTemplate = ''+
            '<div class="qc-message-content">'+
            '    <div class="qc-message-header">'+
            '        <div class="qc-message-title"></div>'+
            '    </div>'+
            '    <div class="qc-message-body">'+
            '    </div>'+
            '    <div class="qc-message-footer">'+
            '    </div>'+
            '</div>';
        var warning = function (msg) {
            var defer  =$q.defer();
            var $container = angular.element(containerTemplate);
            angular.element(document.body).append($container);

            $container.addClass('warning');

            var $content = angular.element(contentTemplate);
            angular.element('.qc-message-title',$content).html('!!!Warning');
            angular.element('.qc-message-body',$content).html(msg);

            var $closeButton=angular.element('<button class="qc-message-button-close btn btn-default">关闭</button>');
            angular.element('.qc-message-footer',$content).append($closeButton);
            $closeButton.on('click',function(){
                closeAnimateElement(angular.element('.qc-message-content',$container),$container);
                closeAnimateElement(angular.element('.qc-message-overlay',$container));
                return defer.resolve('close');
            });
            $container.append(overlayTemplate);
            $container.append($content);
            return defer.promise;
        };
        var error = function (msg) {
            var defer  =$q.defer();
            var $container = angular.element(containerTemplate);
            angular.element(document.body).append($container);

            $container.addClass('error');

            var $content = angular.element(contentTemplate);
            angular.element('.qc-message-title',$content).html('*&@Error');
            angular.element('.qc-message-body',$content).html(msg);

            var $closeButton=angular.element('<button class="qc-message-button-close btn btn-default">关闭</button>');
            angular.element('.qc-message-footer',$content).append($closeButton);
            $closeButton.on('click',function(){
                closeAnimateElement(angular.element('.qc-message-content',$container),$container);
                closeAnimateElement(angular.element('.qc-message-overlay',$container));
                return defer.resolve('close');
            });
            $container.append(overlayTemplate);
            $container.append($content);
            return defer.promise;
        };
        var confirm = function (msg) {
            var defer  =$q.defer();
            var $container = angular.element(containerTemplate);
            angular.element(document.body).append($container);
            $container.addClass('confirm');
            var $content = angular.element(contentTemplate);
            angular.element('.qc-message-title',$content).html('???Confirm');
            angular.element('.qc-message-body',$content).html(msg);
            var $confirmButton = angular.element('<button class="qc-message-button-confirm btn btn-primary">确认</button>');
            var $cancelButton=angular.element('<button class="qc-message-button-cancel btn btn-default">取消</button>');
            angular.element('.qc-message-footer',$content).append($confirmButton);
            angular.element('.qc-message-footer',$content).append("<span> </span>");
            angular.element('.qc-message-footer',$content).append($cancelButton);

            $confirmButton.on('click',function(){
                closeAnimateElement(angular.element('.qc-message-content',$container),$container);
                closeAnimateElement(angular.element('.qc-message-overlay',$container));
                defer.resolve('confirm');
            });
            $cancelButton.on('click',function(){
                closeAnimateElement(angular.element('.qc-message-content',$container),$container);
                closeAnimateElement(angular.element('.qc-message-overlay',$container));
                defer.reject('cancel');
            });
            $container.append(overlayTemplate);
            $container.append($content);
            return defer.promise;
        };
        var tip = function (msg) {
            var tipContainer = angular.element(document.querySelector('.qc-message-container.tip'));
            if (!tipContainer.length) {
                tipContainer = angular.element('<div class="qc-message-container tip"></div>');
                angular.element(document.body).append(tipContainer);
            }
            var $tip = angular.element('<div class="qc-message-content">' + msg + '</div>');
            tipContainer.prepend($tip);
            $timeout(function () {
                closeAnimateElement($tip);
            }, messageOptions.tipShowTime * 1000);
        };
        var info = function (msg) {
            var infoContainer = angular.element(document.querySelector('.qc-message-container.info'));
            if (!infoContainer.length) {
                infoContainer = angular.element('<div class="qc-message-container info"></div>');
                angular.element(document.body).append(infoContainer);
            }
            var $info = angular.element('<div class="qc-message-content">' + msg + '</div>');
            infoContainer.prepend($info);
            $timeout(function () {
                closeAnimateElement($info);
            }, messageOptions.infoShowTime * 1000);
        };
        var closeAnimateElement = function ($el,$removeEl) {
            if(!$removeEl){
                $removeEl=$el;
            }
            if (animationEndSupport) {
                $el.addClass('closing');
                $el.on(animationEndEvent, function () {
                    $removeEl.remove();
                });
            } else {
                $removeEl.remove();
            }
        };
        return {
            warning: warning,
            error: error,
            confirm: confirm,
            info:info,
            tip: tip
        };
    }];
});