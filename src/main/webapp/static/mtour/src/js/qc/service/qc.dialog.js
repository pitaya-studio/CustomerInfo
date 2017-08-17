/**
 * @module qc
 * @version 2.0.0
 * @description 项目弹出框service
 * @requires jquery.js,jquery-ui.js,angular.js,angular-dragdrop.js,basic.js,qc.module.js
 */

qc.provider('qcDialog', function () {
    var $el = angular.element;
    var isDef = angular.isDefined;
    var style = (document.body || document.documentElement).style;
    var animationEndSupport = isDef(style.animation) || isDef(style.WebkitAnimation) || isDef(style.MozAnimation) || isDef(style.MsAnimation) || isDef(style.OAnimation);
    var animationEndEvent = 'animationend webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend';
    var focusableElementSelector = 'a[href], area[href], input:not([disabled]), select:not([disabled]), textarea:not([disabled]), button:not([disabled]), iframe, object, embed, *[tabindex], *[contenteditable]';
    var disabledAnimationClass = 'qc-dialog-disabled-animation';
    var forceElementsReload = {html: false, body: false};
    var scopes = {};
    var openIdStack = [];
    var keydownIsBound = false;
    var defaults = this.defaults = {
        className: 'qc-dialog-theme-default',
        disableAnimation: false,
        draggable: true,
        plain: false,
        showClose: true,
        closeByDocument: false,
        closeByEscape: false,
        closeByNavigation: false,
        appendTo: false,
        preCloseCallback: false,
        overlay: true,
        cache: true,
        trapFocus: false,//打开弹出窗后,自动焦点(优先顺序[添加autofocus属性的元素>表单元素])
        preserveFocus: true,//弹出窗关闭后,是否将焦点回落到原来的焦点元素
        ariaAuto: true,
        ariaRole: null,
        ariaLabelledById: null,
        ariaLabelledBySelector: null,
        ariaDescribedById: null,
        ariaDescribedBySelector: null
    };

    this.setForceHtmlReload = function (_useIt) {
        forceElementsReload.html = _useIt || false;
    };

    this.setForceBodyReload = function (_useIt) {
        forceElementsReload.body = _useIt || false;
    };

    this.setDefaults = function (newDefaults) {
        angular.extend(defaults, newDefaults);
    };

    var globalID = 0, dialogsCount = 0, closeByDocumentHandler, defers = {};

    this.$get = ['$document', '$templateCache', '$compile', '$q', '$http', '$rootScope', '$timeout', '$window', '$controller', '$injector',
        function ($document, $templateCache, $compile, $q, $http, $rootScope, $timeout, $window, $controller, $injector) {
            var $elements = [];

            angular.forEach(
                ['html', 'body'],
                function (elementName) {
                    $elements[elementName] = $document.find(elementName);
                    if (forceElementsReload[elementName]) {
                        var eventName = privateMethods.getRouterLocationEventName();
                        $rootScope.$on(eventName, function () {
                            $elements[elementName] = $document.find(elementName);
                        });
                    }
                }
            );

            var privateMethods = {
                onDocumentKeydown: function (event) {
                    if (event.keyCode === 27) {
                        publicMethods.close('$escape');
                    }
                },

                activate: function ($dialog) {
                    var options = $dialog.data('$qcDialogOptions');

                    if (options.trapFocus) {
                        $dialog.on('keydown', privateMethods.onTrapFocusKeydown);

                        // Catch rogue changes (eg. after unfocusing everything by clicking a non-focusable element)
                        $elements.body.on('keydown', privateMethods.onTrapFocusKeydown);
                    }
                },

                deactivate: function ($dialog) {
                    $dialog.off('keydown', privateMethods.onTrapFocusKeydown);
                    $elements.body.off('keydown', privateMethods.onTrapFocusKeydown);
                },

                deactivateAll: function () {
                    angular.forEach(function (el) {
                        var $dialog = angular.element(el);
                        privateMethods.deactivate($dialog);
                    });
                },

                setBodyPadding: function (width) {
                    var originalBodyPadding = parseInt(($elements.body.css('padding-right') || 0), 10);
                    $elements.body.css('padding-right', (originalBodyPadding + width) + 'px');
                    $elements.body.data('ng-dialog-original-padding', originalBodyPadding);
                },

                resetBodyPadding: function () {
                    var originalBodyPadding = $elements.body.data('ng-dialog-original-padding');
                    if (originalBodyPadding) {
                        $elements.body.css('padding-right', originalBodyPadding + 'px');
                    } else {
                        $elements.body.css('padding-right', '');
                    }
                },

                performCloseDialog: function ($dialog, value) {
                    //如果在弹窗中使用了 niceScroll,会出现关闭前滚动条闪现,
                    //为了解决这个问题,下面的是临时解决方案,如果由更好的方案,请更新
                    //@todo
                    var $scroll = $dialog.find('.nicescroll-rails');
                    $scroll.remove();

                    var options = $dialog.data('$qcDialogOptions');
                    var id = $dialog.attr('id');
                    var scope = scopes[id];

                    if (!scope) {
                        // Already closed
                        return;
                    }

                    if (typeof $window.Hammer !== 'undefined') {
                        var hammerTime = scope.hammerTime;
                        hammerTime.off('tap', closeByDocumentHandler);
                        if (hammerTime.destroy) {
                            hammerTime.destroy();
                        }
                        //hammerTime.destroy && hammerTime.destroy();
                        delete scope.hammerTime;
                    } else {
                        $dialog.unbind('click');
                    }

                    if (dialogsCount === 1) {
                        $elements.body.unbind('keydown', privateMethods.onDocumentKeydown);
                    }

                    if (!$dialog.hasClass('qc-dialog-closing')) {
                        dialogsCount -= 1;
                    }

                    var previousFocus = $dialog.data('$qcDialogPreviousFocus');
                    if (previousFocus) {
                        previousFocus.focus();
                    }

                    $rootScope.$broadcast('qc-dialog.closing', $dialog, value);
                    dialogsCount = dialogsCount < 0 ? 0 : dialogsCount;
                    if (animationEndSupport && !options.disableAnimation) {
                        scope.$destroy();
                        $dialog.unbind(animationEndEvent).bind(animationEndEvent, function () {
                            privateMethods.closeDialogElement($dialog, value);
                        }).addClass('qc-dialog-closing');
                    } else {
                        scope.$destroy();
                        privateMethods.closeDialogElement($dialog, value);
                    }
                    if (defers[id]) {
                        defers[id].resolve({
                            id: id,
                            value: value,
                            $dialog: $dialog,
                            remainiqcDialogs: dialogsCount
                        });
                        delete defers[id];
                    }
                    if (scopes[id]) {
                        delete scopes[id];
                    }
                    openIdStack.splice(openIdStack.indexOf(id), 1);
                    if (!openIdStack.length) {
                        $elements.body.unbind('keydown', privateMethods.onDocumentKeydown);
                        keydownIsBound = false;
                    }
                },

                closeDialogElement: function ($dialog, value) {
                    $dialog.remove();
                    if (dialogsCount === 0) {
                        $elements.html.removeClass('qc-dialog-open');
                        $elements.body.removeClass('qc-dialog-open');
                        privateMethods.resetBodyPadding();
                    }
                    $rootScope.$broadcast('qc-dialog.closed', $dialog, value);
                },

                closeDialog: function ($dialog, value) {
                    var preCloseCallback = $dialog.data('$qcDialogPreCloseCallback');

                    if (preCloseCallback && angular.isFunction(preCloseCallback)) {

                        var preCloseCallbackResult = preCloseCallback.call($dialog, value);

                        if (angular.isObject(preCloseCallbackResult)) {
                            if (preCloseCallbackResult.closePromise) {
                                preCloseCallbackResult.closePromise.then(function () {
                                    privateMethods.performCloseDialog($dialog, value);
                                });
                            } else {
                                preCloseCallbackResult.then(function () {
                                    privateMethods.performCloseDialog($dialog, value);
                                }, function () {
                                    return;
                                });
                            }
                        } else if (preCloseCallbackResult !== false) {
                            privateMethods.performCloseDialog($dialog, value);
                        }
                    } else {
                        privateMethods.performCloseDialog($dialog, value);
                    }
                },

                onTrapFocusKeydown: function (ev) {
                    var el = angular.element(ev.currentTarget);
                    var $dialog;

                    if (el.hasClass('qc-dialog')) {
                        $dialog = el;
                    } else {
                        $dialog = privateMethods.getActiveDialog();

                        if ($dialog === null) {
                            return;
                        }
                    }

                    var isTab = (ev.keyCode === 9);
                    var backward = (ev.shiftKey === true);

                    if (isTab) {
                        privateMethods.handleTab($dialog, ev, backward);
                    }
                },

                handleTab: function ($dialog, ev, backward) {
                    var focusableElements = privateMethods.getFocusableElements($dialog);

                    if (focusableElements.length === 0) {
                        if (document.activeElement) {
                            document.activeElement.blur();
                        }
                        return;
                    }

                    var currentFocus = document.activeElement;
                    var focusIndex = Array.prototype.indexOf.call(focusableElements, currentFocus);

                    var isFocusIndexUnknown = (focusIndex === -1);
                    var isFirstElementFocused = (focusIndex === 0);
                    var isLastElementFocused = (focusIndex === focusableElements.length - 1);

                    var cancelEvent = false;

                    if (backward) {
                        if (isFocusIndexUnknown || isFirstElementFocused) {
                            focusableElements[focusableElements.length - 1].focus();
                            cancelEvent = true;
                        }
                    } else {
                        if (isFocusIndexUnknown || isLastElementFocused) {
                            focusableElements[0].focus();
                            cancelEvent = true;
                        }
                    }

                    if (cancelEvent) {
                        ev.preventDefault();
                        ev.stopPropagation();
                    }
                },

                autoFocus: function ($dialog) {
                    var dialogEl = $dialog[0];

                    // Browser's (Chrome 40, Forefix 37, IE 11) don't appear to honor autofocus on the dialog, but we should
                    var autoFocusEl = dialogEl.querySelector('*[autofocus]');
                    if (autoFocusEl !== null) {
                        autoFocusEl.focus();

                        if (document.activeElement === autoFocusEl) {
                            return;
                        }

                        // Autofocus element might was display: none, so let's continue
                    }

                    var focusableElements = privateMethods.getFocusableElements($dialog);

                    if (focusableElements.length > 0) {
                        focusableElements[0].focus();
                        return;
                    }

                    // We need to focus something for the screen readers to notice the dialog
                    var contentElements = privateMethods.filterVisibleElements(dialogEl.querySelectorAll('h1,h2,h3,h4,h5,h6,p,span'));

                    if (contentElements.length > 0) {
                        var contentElement = contentElements[0];
                        $el(contentElement).attr('tabindex', '-1').css('outline', '0');
                        contentElement.focus();
                    }
                },

                getFocusableElements: function ($dialog) {
                    var dialogEl = $dialog[0];

                    var rawElements = $dialog.find(focusableElementSelector);

                    // Ignore untabbable elements, ie. those with tabindex = -1
                    var tabbableElements = privateMethods.filterTabbableElements(rawElements);

                    return privateMethods.filterVisibleElements(tabbableElements);
                },

                filterTabbableElements: function (els) {
                    var tabbableFocusableElements = [];

                    for (var i = 0; i < els.length; i++) {
                        var el = els[i];

                        if ($el(el).attr('tabindex') !== '-1') {
                            tabbableFocusableElements.push(el);
                        }
                    }

                    return tabbableFocusableElements;
                },

                filterVisibleElements: function (els) {
                    var visibleFocusableElements = [];

                    for (var i = 0; i < els.length; i++) {
                        var el = els[i];

                        if (el.offsetWidth > 0 || el.offsetHeight > 0) {
                            visibleFocusableElements.push(el);
                        }
                    }

                    return visibleFocusableElements;
                },

                getActiveDialog: function () {
                    var dialogs = document.querySelectorAll('.qc-dialog');

                    if (dialogs.length === 0) {
                        return null;
                    }

                    // TODO: This might be incorrect if there are a mix of open dialogs with different 'appendTo' values
                    return $el(dialogs[dialogs.length - 1]);
                },

                applyAriaAttributes: function ($dialog, options) {
                    if (options.ariaAuto) {
                        if (!options.ariaRole) {
                            var detectedRole = (privateMethods.getFocusableElements($dialog).length > 0) ?
                                'dialog' :
                                'alertdialog';

                            options.ariaRole = detectedRole;
                        }

                        if (!options.ariaLabelledBySelector) {
                            options.ariaLabelledBySelector = 'h1,h2,h3,h4,h5,h6';
                        }

                        if (!options.ariaDescribedBySelector) {
                            options.ariaDescribedBySelector = 'article,section,p';
                        }
                    }

                    if (options.ariaRole) {
                        $dialog.attr('role', options.ariaRole);
                    }

                    privateMethods.applyAriaAttribute(
                        $dialog, 'aria-labelledby', options.ariaLabelledById, options.ariaLabelledBySelector);

                    privateMethods.applyAriaAttribute(
                        $dialog, 'aria-describedby', options.ariaDescribedById, options.ariaDescribedBySelector);
                },

                applyAriaAttribute: function ($dialog, attr, id, selector) {
                    if (id) {
                        $dialog.attr(attr, id);
                    }

                    if (selector) {
                        var dialogId = $dialog.attr('id');

                        var firstMatch = $dialog[0].querySelector(selector);

                        if (!firstMatch) {
                            return;
                        }

                        var generatedId = dialogId + '-' + attr;

                        $el(firstMatch).attr('id', generatedId);

                        $dialog.attr(attr, generatedId);

                        return generatedId;
                    }
                },

                detectUIRouter: function () {
                    //Detect if ui-router module is installed if not return false
                    try {
                        angular.module("ui.router");
                        return true;
                    } catch (err) {
                        return false;
                    }
                },

                getRouterLocationEventName: function () {
                    if (privateMethods.detectUIRouter()) {
                        return '$stateChangeSuccess';
                    }
                    return '$locationChangeSuccess';
                },
                getMessageTemplate: function (msg, type) {
                    var template = '' +
                        '<div class="dialog-body-md-del">' +
                        '   <span><em class="fa fa-exclamation-triangle"></em><i></i>' + msg + '</span>' +
                        '</div>' +
                        '<div class="qc-dialog-footer">' +
                        '   <div class="text-center">';
                    if (type === 'confirm') {
                        template += '<button class="butn butn-default " ng-click="confirm()">确认</button>';
                    }

                    template += '<button class="butn butn-primary " ng-click="closeThisDialog(this)">关闭</button>' +
                    '   </div>' +
                    '</div>' +
                    '<div class="qc-dialog-close-base" ng-click="closeThisDialog(this)"></div>';
                    return template;
                }
            };

            var publicMethods = {

                /*
                 * @param {Object} options:
                 * - template {String} - id of ng-template, url for partial, plain string (if enabled)
                 * - plain {Boolean} - enable plain string templates, default false
                 * - scope {Object}
                 * - controller {String}
                 * - controllerAs {String}
                 * - className {String} - dialog theme class
                 * - disableAnimation {Boolean} - set to true to disable animation
                 * - showClose {Boolean} - show close button, default true
                 * - closeByEscape {Boolean} - default true
                 * - closeByDocument {Boolean} - default true
                 * - preCloseCallback {String|Function} - user supplied function name/function called before closing dialog (if set)
                 *
                 * @return {Object} dialog
                 */
                open: function (opts) {
                    var options = angular.copy(defaults);
                    var localID = ++globalID;
                    var dialogID = 'qcDialog' + localID;
                    openIdStack.push(dialogID);

                    opts = opts || {};
                    angular.extend(options, opts);

                    var defer;
                    defers[dialogID] = defer = $q.defer();

                    var scope;
                    scopes[dialogID] = scope = angular.isObject(options.scope) ? options.scope.$new() : $rootScope.$new();
                    if (options.title) {
                        scope.title = options.title;
                    }
                    var $dialog, $dialogParent;

                    var resolve = angular.extend({}, options.resolve);

                    angular.forEach(resolve, function (value, key) {
                        resolve[key] = angular.isString(value) ? $injector.get(value) : $injector.invoke(value, null, null, key);
                    });

                    $q.all({
                        template: loadTemplate(options.template || options.templateUrl),
                        locals: $q.all(resolve)
                    }).then(function (setup) {
                        var template = setup.template,
                            locals = setup.locals;
                        if (options.title) {
                            template = '<div class="qc-dialog-header"> <div class="qc-dialog-title" ng-bind="title"></div> </div>' + template;
                        }
                        if (options.showClose) {
                            template += '<div class="qc-dialog-close"></div>';
                        }
                        $dialog = $el('<div id="qcDialog' + localID + '" class="qc-dialog"></div>');
                        $dialog.html((options.overlay ?
                        '<div class="qc-dialog-overlay"></div><div class="qc-dialog-content" role="document">' + template + '</div>' :
                        '<div class="qc-dialog-content" role="document">' + template + '</div>'));
                        //设置宽度
                        if (options.width) {
                            angular.element('.qc-dialog-content', $dialog).css({'width': options.width});
                        }
                        if (options.draggable) {//添加拖拽功能
                            angular.element('.qc-dialog-content', $dialog).attr('data-drag', true);
                            angular.element('.qc-dialog-content', $dialog).attr('qc-draggable', '');
                            angular.element('.qc-dialog-content', $dialog).attr('data-qc-options', '{cancel:".qc-dialog-header~"}');//只能在header拖拽
                        }
                        $dialog.data('$qcDialogOptions', options);

                        scope.qcDialogId = dialogID;

                        if (options.data && angular.isString(options.data)) {
                            var firstLetter = options.data.replace(/^\s*/, '')[0];
                            scope.qcDialogData = (firstLetter === '{' || firstLetter === '[') ? angular.fromJson(options.data) : options.data;
                            scope.qcDialogData.qcDialogId = dialogID;
                        } else if (options.data && angular.isObject(options.data)) {
                            scope.qcDialogData = options.data;
                            scope.qcDialogData.qcDialogId = dialogID;
                        }

                        if (options.controller && (angular.isString(options.controller) || angular.isArray(options.controller) || angular.isFunction(options.controller))) {

                            var label;

                            if (options.controllerAs && angular.isString(options.controllerAs)) {
                                label = options.controllerAs;
                            }

                            var controllerInstance = $controller(options.controller, angular.extend(
                                    locals,
                                    {
                                        $scope: scope,
                                        $element: $dialog
                                    }),
                                null,
                                label
                            );
                            $dialog.data('$qcDialogControllerController', controllerInstance);
                        }

                        if (options.className) {
                            $dialog.addClass(options.className);
                        }

                        if (options.disableAnimation) {
                            $dialog.addClass(disabledAnimationClass);
                        }

                        if (options.appendTo && angular.isString(options.appendTo)) {
                            $dialogParent = angular.element(document.querySelector(options.appendTo));
                        } else {
                            $dialogParent = $elements.body;
                        }

                        privateMethods.applyAriaAttributes($dialog, options);

                        if (options.preCloseCallback) {
                            var preCloseCallback;

                            if (angular.isFunction(options.preCloseCallback)) {
                                preCloseCallback = options.preCloseCallback;
                            } else if (angular.isString(options.preCloseCallback)) {
                                if (scope) {
                                    if (angular.isFunction(scope[options.preCloseCallback])) {
                                        preCloseCallback = scope[options.preCloseCallback];
                                    } else if (scope.$parent && angular.isFunction(scope.$parent[options.preCloseCallback])) {
                                        preCloseCallback = scope.$parent[options.preCloseCallback];
                                    } else if ($rootScope && angular.isFunction($rootScope[options.preCloseCallback])) {
                                        preCloseCallback = $rootScope[options.preCloseCallback];
                                    }
                                }
                            }

                            if (preCloseCallback) {
                                $dialog.data('$qcDialogPreCloseCallback', preCloseCallback);
                            }
                        }

                        scope.closeThisDialog = function (value) {
                            privateMethods.closeDialog($dialog, value);
                        };

                        $timeout(function () {
                            var $activeDialogs = document.querySelectorAll('.qc-dialog');
                            privateMethods.deactivateAll($activeDialogs);

                            $compile($dialog)(scope);
                            var widthDiffs = $window.innerWidth - $elements.body.prop('clientWidth');
                            $elements.html.addClass('qc-dialog-open');
                            $elements.body.addClass('qc-dialog-open');
                            var scrollBarWidth = widthDiffs - ($window.innerWidth - $elements.body.prop('clientWidth'));
                            if (scrollBarWidth > 0) {
                                privateMethods.setBodyPadding(scrollBarWidth);
                            }
                            $dialogParent.append($dialog);

                            privateMethods.activate($dialog);

                            if (options.trapFocus) {
                                privateMethods.autoFocus($dialog);
                            }

                            if (options.name) {
                                $rootScope.$broadcast('qc-dialog.opened', {dialog: $dialog, name: options.name});
                            } else {
                                $rootScope.$broadcast('qc-dialog.opened', $dialog);
                            }
                        });

                        if (!keydownIsBound) {
                            $elements.body.bind('keydown', privateMethods.onDocumentKeydown);
                            keydownIsBound = true;
                        }

                        if (options.closeByNavigation) {
                            var eventName = privateMethods.getRouterLocationEventName();
                            $rootScope.$on(eventName, function () {
                                privateMethods.closeDialog($dialog);
                            });
                        }

                        if (options.preserveFocus) {
                            $dialog.data('$qcDialogPreviousFocus', document.activeElement);
                        }

                        closeByDocumentHandler = function (event) {
                            var isOverlay = options.closeByDocument ? $el(event.target).hasClass('qc-dialog-overlay') : false;
                            var isCloseBtn = $el(event.target).hasClass('qc-dialog-close');

                            if (isOverlay || isCloseBtn) {
                                publicMethods.close($dialog.attr('id'), isCloseBtn ? '$closeButton' : '$document');
                            }
                        };

                        if (typeof $window.Hammer !== 'undefined') {
                            var hammerTime = scope.hammerTime = $window.Hammer($dialog[0]);
                            hammerTime.on('tap', closeByDocumentHandler);
                        } else {
                            $dialog.bind('click', closeByDocumentHandler);
                        }

                        dialogsCount += 1;

                        return publicMethods;
                    });

                    return {
                        id: dialogID,
                        closePromise: defer.promise,
                        close: function (value) {
                            privateMethods.closeDialog($dialog, value);
                        }
                    };

                    function loadTemplateUrl(tmpl, config) {
                        $rootScope.$broadcast('qcDialog.templateLoading', tmpl);
                        return $http.get(tmpl, (config || {})).then(function (res) {
                            $rootScope.$broadcast('qcDialog.templateLoaded', tmpl);
                            return res.data || '';
                        });
                    }

                    function loadTemplate(tmpl) {
                        if (!tmpl) {
                            return 'Empty template';
                        }

                        if (angular.isString(tmpl) && options.plain) {
                            return tmpl;
                        }

                        if (typeof options.cache === 'boolean' && !options.cache) {
                            return loadTemplateUrl(tmpl, {cache: false});
                        }

                        return loadTemplateUrl(tmpl, {cache: $templateCache});
                    }
                },

                /*
                 * @param {Object} options:
                 * - template {String} - id of ng-template, url for partial, plain string (if enabled)
                 * - plain {Boolean} - enable plain string templates, default false
                 * - name {String}
                 * - scope {Object}
                 * - controller {String}
                 * - controllerAs {String}
                 * - className {String} - dialog theme class
                 * - showClose {Boolean} - show close button, default true
                 * - closeByEscape {Boolean} - default false
                 * - closeByDocument {Boolean} - default false
                 * - preCloseCallback {String|Function} - user supplied function name/function called before closing dialog (if set); not called on confirm
                 *
                 * @return {Object} dialog
                 */
                openConfirm: function (opts) {
                    var defer = $q.defer();

                    var options = {
                        closeByEscape: false,
                        closeByDocument: false
                    };
                    angular.extend(options, opts);

                    options.scope = angular.isObject(options.scope) ? options.scope.$new() : $rootScope.$new();
                    options.scope.confirm = function (value) {
                        defer.resolve(value);
                        var $dialog = $el(document.getElementById(openResult.id));
                        privateMethods.performCloseDialog($dialog, value);
                    };

                    var openResult = publicMethods.open(options);
                    openResult.closePromise.then(function (data) {
                        if (data) {
                            return defer.reject(data.value);
                        }
                        return defer.reject();
                    });

                    return defer.promise;
                },

                openMessage: function (opts) {
                    var defer = $q.defer();


                    var options = {
                        closeByEscape: false,
                        closeByDocument: false,
                        plain: true,
                        showClose: false,
                        template: privateMethods.getMessageTemplate(opts.msg, opts.type)
                    };
                    angular.extend(options, opts);

                    options.scope = angular.isObject(options.scope) ? options.scope.$new() : $rootScope.$new();
                    options.scope.confirm = function (value) {
                        defer.resolve(value);
                        var $dialog = $el(document.getElementById(openResult.id));
                        privateMethods.performCloseDialog($dialog, value);
                    };

                    var openResult = publicMethods.open(options);
                    openResult.closePromise.then(function (data) {
                        if (data) {
                            return defer.reject(data.value);
                        }
                        return defer.reject();
                    });

                    return defer.promise;
                },
                openCover: function (opts) {
                    var options = {
                        className: 'qc-dialog-theme-cover',
                        draggable: false,
                        closeByEscape: false,
                        closeByDocument: false
                    };
                    angular.extend(options, opts);

                    var openResult = publicMethods.open(options);
                    return openResult;
                },

                isOpen: function (id) {
                    var $dialog = $el(document.getElementById(id));
                    return $dialog.length > 0;
                },

                /*
                 * @param {String} id
                 * @return {Object} dialog
                 */
                close: function (id, value) {
                    var $dialog = $el(document.getElementById(id));

                    if ($dialog.length) {
                        privateMethods.closeDialog($dialog, value);
                    } else {
                        if (id === '$escape') {
                            var topDialogId = openIdStack[openIdStack.length - 1];
                            $dialog = $el(document.getElementById(topDialogId));
                            if ($dialog.data('$qcDialogOptions').closeByEscape) {
                                privateMethods.closeDialog($dialog, value);
                            }
                        } else {
                            publicMethods.closeAll(value);
                        }
                    }

                    return publicMethods;
                },

                closeAll: function (value) {
                    var $all = document.querySelectorAll('.qc-dialog');

                    // Reverse order to ensure focus restoration works as expected
                    for (var i = $all.length - 1; i >= 0; i--) {
                        var dialog = $all[i];
                        privateMethods.closeDialog($el(dialog), value);
                    }
                },

                getOpenDialogs: function () {
                    return openIdStack;
                },

                getDefaults: function () {
                    return defaults;
                }
            };

            return publicMethods;
        }];
});