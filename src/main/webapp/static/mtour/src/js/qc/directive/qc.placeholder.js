/**
 * @module qc
 * @version 2.0.0
 * @description 项目的placeholder,
 * @requires jquery.js,angular.js,basic.js,qc.module.js
 */

//扩展doc element 的 placeholder方法
(function($) {
    // @todo Document this.
    $.extend($,{ placeholder: {
        browser_supported: function() {
            return this._supported !== undefined ?
                this._supported :
                ( this._supported = !!('placeholder' in $('<input type="text">')[0]) );
        },
        shim: function(opts) {
            var config = {
                color: '#888',
                cls: 'placeholder',
                selector: 'input[placeholder], textarea[placeholder]',
                align: 'left'
            };
            $.extend(config,opts);
            if(!this.browser_supported()) {
                $(config.selector)._placeholder_shim(config);
            }
        }
    }});

    $.extend($.fn,{
        _placeholder_shim: function(config) {
            function calcPositionCss(target) {
                var op = $(target).offsetParent().offset();
                var ot = $(target).offset();

                return {
                    top: ot.top - op.top,
                    left: ot.left - op.left,
                    width: $(target).outerWidth(true)
                };
            }
            return this.each(function() {
                var $this = $(this);

                if( $this.data('placeholder') ) {
                    var $ol = $this.data('placeholder');
                    $ol.css(calcPositionCss($this));
                    return true;
                }

                var possible_line_height = {};
                if( !$this.is('textarea') && $this.css('line-height') !== 'auto') {
                    possible_line_height = { lineHeight: $this.css('line-height'), whiteSpace: 'nowrap' };
                }

                var ol = $('<label />')
                    .text($this.attr('placeholder'))
                    .addClass(config.cls)
                    .css($.extend({
                        position:'absolute',
                        display: 'inline',
                        'float':'none',
                        overflow:'hidden',
                        textAlign: config.align,
                        color: config.color,
                        cursor: 'text',
                        paddingTop: $this.css('padding-top'),
                        paddingRight: $this.css('padding-right'),
                        paddingBottom: $this.css('padding-bottom'),
                        paddingLeft: $this.css('padding-left'),
                        fontSize: $this.css('font-size'),
                        fontFamily: $this.css('font-family'),
                        fontStyle: $this.css('font-style'),
                        fontWeight: $this.css('font-weight'),
                        textTransform: $this.css('text-transform'),
                        backgroundColor: 'transparent',
                        zIndex: 99
                    }, possible_line_height))
                    .css(calcPositionCss(this))
                    .attr('for', this.id)
                    .data('target',$this)
                    .click(function(){
                        $(this).data('target').focus();
                    })
                    .insertBefore(this);
                $this
                    .data('placeholder',ol)
                    .focus(function(){
                        ol.hide();
                    }).blur(function() {
                        ol[$this.val().length ? 'hide' : 'show']();
                    }).triggerHandler('blur');
                $(window)
                    .resize(function() {
                        var $target = ol.data('target');
                        ol.css(calcPositionCss($target));
                    });
            });
        }
    });
})(jQuery);
//jQuery(document).add(window).bind('ready load', function() {
//    if (jQuery.placeholder) {
//        jQuery.placeholder.shim();
//    }
//});

/* angular-placeholder-shim version 0.3.1
 * License: MIT.
 * Copyright (C) 2013, Uri Shaked.
 */

//封装成angular组件
qc.directive('placeholder', ['$interpolate', '$timeout', function ($interpolate, $timeout) {
    if (jQuery.placeholder.browser_supported()) {
        return {};
    }

    return function (scope, element,attrs) {
        var config = {
            color: '#888',
            cls: 'placeholder'
        };

        var interpolatedPlaceholder = $interpolate(element.attr('placeholder'));
        var placeholderText = null;

        var overlay = null;
        var pendingTimer = null;

        function addPlaceholder() {
            pendingTimer = $timeout(function () {
                element._placeholder_shim(config);
                overlay = element.data('placeholder');
                pendingTimer = null;
            });
        }
        //ie8在响应式页面中需要等待respond.js执行完成后再添加placeholder
        $timeout(function(){
            if (element.is(':visible')) {
                addPlaceholder();
            }
        },100);

        // The following code accounts for value changes from within the code
        // and for dynamic changes in placeholder text
        scope.$watch(function () {
            if (!overlay && element.is(':visible') && !pendingTimer) {
                addPlaceholder();
            }
            if (overlay && (element.get(0) !== document.activeElement)) {
                if (element.val().length) {
                    overlay.hide();
                } else {
                    overlay.show();
                }
            }
            if (overlay) {
                var newText = interpolatedPlaceholder(scope);
                if (newText !== placeholderText) {
                    placeholderText = newText;
                    overlay.text(placeholderText);
                }
            }
        });
        scope.$on('$destroy', function () {
            if (pendingTimer) {
                $timeout.cancel(pendingTimer);
                pendingTimer = null;
            }
        });
    };
}]);