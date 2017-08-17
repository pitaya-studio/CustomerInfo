/**
 * @description：qc.webuploader module,定义项目文件上传组件
 * @global
 * @requires jquery.js,webuploader.js,angular.js,basic.js,qc module
 */

angular.module('qc.uploader', ['qc']).
    provider('qcUploader', function () {
        this.options = {
            thumb: {
                width: 110,
                height: 110,

                // 图片质量，只有type为`image/jpeg`的时候才有效。
                quality: 100,

                // 是否允许放大，如果想要生成小图的时候不失真，此选项应该设置为false.
                allowMagnify: true,

                // 是否允许裁剪。
                crop: false,

                // 为空的话则保留原有图片格式。否则强制转换成指定的类型。
                type: 'image/jpeg'
            },
            makeThumb: false,
            swf: '/src/components/webuploader/Uploader.swf',
            server: 'http://192.168.130.4:3000/files/uploading',
            //accept: {
            //    title: 'Images',
            //    extensions: 'gif,jpg,jpeg,bmp,png',
            //    mimeTypes: 'image/*'
            //},
            auto: true,
            resize: false
        };
        this.$get = ['$timeout', function ($timeout) {
            var uploaderOptions = this.options;
            var create = function (options) {
                var uploader = WebUploader.create(angular.extend(uploaderOptions, options));
                return uploader;
            };
            return {
                create: create
            };
        }];
    }).
    //格式化文件大小(目前只支持kb和mb)
    filter('fileSize', ['$filter', function ($filter) {
        return function (value, unit) {
            var num;
            switch (unit.toUpperCase()) {
                case 'KB':
                    num = parseFloat(value / 1024);
                    break;
                case 'MB':
                    num = parseFloat(value / 1024 / 1024);
                    break;
                default :
                    num = 0;
            }
            return $filter('number')(num, 2) + ' ' + unit;
        };
    }]).
    directive('qcUploaderPicker', ['qcUploader', 'qcFlashVersion', '$window', '$timeout', function (qcUploader, qcFlashVersion, $window, $timeout) {
        return {
            require: '^ngModel',
            restrict: 'AE',
            scope: {
                qcUploaderOptions: '='
            },
            link: function (scope, ele, attrs, ngModel) {
                if (!qcFlashVersion.installed) {
                    scope.$emit('qcUpload.flash.uninstall');
                    ele.html('<a class="flash-link"  href="https://get.adobe.com/cn/flashplayer" target="_blank">没安装flash</a>');
                    //ele.on('click', function () {
                    //    $window.open('https://get.adobe.com/cn/flashplayer');
                    //});
                    return;
                } else if (qcFlashVersion.version < '12') {
                    scope.$emit('qcUpload.flash.outdated');
                    ele.html('<a class="flash-link" href="https://get.adobe.com/cn/flashplayer" target="_blank">flash版本太低</a>');
                    //ele.text('flash版本太低!');
                    //ele.on('click', function () {
                    //    $window.open('https://get.adobe.com/cn/flashplayer');
                    //});
                    return;
                }
                var options = angular.extend(scope.qcUploaderOptions ? scope.qcUploaderOptions : {}, {pick: ele});
                var uploader = qcUploader.create(options);
                //初始化ngModel(空数组)
                ngModel.$setViewValue(uploader.getFiles());
                uploader.on('fileQueued', function (file) {
                    ngModel.$setViewValue(uploader.getFiles());
                    file.on('statuschange', function (status, oldStatus) {
                        //scope.$apply(function () {
                            file.status = status;
                        //});
                    });
                    if (uploader.options.makeThumb) {
                        uploader.makeThumb(file, function (error, src) {
                            scope.$apply(function () {
                                file.thumbSrc = src;
                            });
                        }, uploader.options.thumb.width, uploader.options.thumb.height);
                    }
                });

                uploader.on('uploadError', function (file, reason ) {
                    file.errorMsg=reason;
                });

                uploader.on('uploadProgress', function (file, percentage) {
                    var modelFile = ngModel.$modelValue.filter(function (item) {
                        return item.id === file.id;
                    });
                    if (modelFile.length) {
                        scope.$apply(function () {
                            modelFile[0].ProgressPercentage = percentage;
                        });
                    }
                });
                uploader.on('uploadSuccess', function (file, response) {
                    var modelFile = ngModel.$modelValue.filter(function (item) {
                        return item.id === file.id;
                    });
                    if (modelFile.length) {
                        scope.$apply(function () {
                            //modelFile[0].url = scope.$eval(response._raw).url;
                            angular.extend(modelFile[0], scope.$eval(response._raw).data[0]);
                            scope.$emit('uploader.completed',modelFile[0]);
                        });
                    }
                });


                //监控ngModel的文件列表,当ngModel删除了上传的文件时,uploader也需要做相应的删除
                scope.modelFiles = ngModel.$modelValue;
                scope.$on('uploader.removeFile', function ($e,fileId) {
                    var queueFiles = uploader.getFiles();
                    var removeFile;
                    angular.forEach(queueFiles, function (file) {
                        if(file.id===fileId){
                            removeFile= file;
                        }
                    });

                    uploader.removeFile(removeFile,true);
                    ngModel.$setViewValue(queueFiles);
                });
                //scope.$watchCollection('modelFiles', function (files) {
                //    var filterFn = function (item) {
                //        return item.id === file.id;
                //    };
                //    var removedFiles = [];
                //
                //    var queueFiles = uploader.getFiles();
                //    var queueIndex = queueFiles.length;
                //    if (queueIndex > files.length) {
                //        while (queueIndex) {
                //            var file = queueFiles[queueIndex - 1];
                //            var modeFile = files.filter(filterFn);
                //            if (modeFile.length < 1) {
                //                removedFiles.push(file);
                //            }
                //            queueIndex--;
                //        }
                //    }
                //    $timeout(function () {
                //        var removeIndex = removedFiles.length;
                //        while (removeIndex) {
                //            uploader.removeFile(removedFiles[removeIndex - 1]);
                //            removeIndex--;
                //        }
                //    });
                //});

                //如果指定了上传按钮,则给该按钮注册上传事件,只有当auto为false时才有实际意义
                if (options.submitSelector) {
                    $(options.submitSelector).on('click', function () {
                        uploader.upload();
                    });
                }
            }
        };
    }]);