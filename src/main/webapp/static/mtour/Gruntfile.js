/**
 * Created by ding on 2015/8/21.
 */
'use strict';
module.exports = function (grunt) {

    //统计grunt执行的时间统计
    require('time-grunt')(grunt);
    //加载所有在package.json文件中引用的所有grunt task
    require('load-grunt-tasks')(grunt);
    var config = {
        tmp: 'tmp',
        src: {
            component: 'src/components',
            js: 'src/js',
            css: 'src/css',
            img: 'src/img',
            html: 'src/html'
        },
        static: {
            js: 'static/js',
            flash: 'static/flash',
            css: 'static/css',
            img: 'static/img',
            html: 'html'
        }

    }
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        config: config,
        less:{
            qc:{
                src: '<%= config.src.css %>/less/quauq.less',
                dest: '<%= config.src.css %>/qc/quauq.css'
            },
            qc_print:{
                src: '<%= config.src.css %>/less/qc.print.less',
                dest: '<%= config.src.css %>/qc/qc.print.css'
            }
        },

        //css自动排序
        csscomb:{
            options:{
                config:'qc.csscomb.json'
            },
            qc:{
                expand: true,
                cwd: '<%= config.src.css %>/qc/',
                src: ['*.css'],
                dest: '<%= config.src.css %>/qc/'
            }
        },
        connect: {
            options: {

                hostname: 'localhost'
            },
            sample: {
                options: {
                    port: 7499,
                    livereload: 35729,
                    open: {
                        target: 'http://localhost:7499/src/html/sample/'
                    }
                }
            },
            doc: {
                options: {
                    port: 7498,
                    livereload: 35730,
                    hostname: '0.0.0.0',
                    open: {
                        target: 'http://localhost:7498/doc/'
                    }
                }
            }
        },
        watch: {
            //less:{
            //    tasks: 'less:qc',
            //    files: [
            //        '<%= config.src.css %>/less/**/*.*'
            //    ]
            //},
            sample: {
                options: {
                    livereload: '<%=connect.sample.options.livereload%>'  //监听前面声明的端口
                },
                files: [
                    'src/**/*.*'
                ]
            },
            doc: {
                options: {
                    livereload: '<%=connect.doc.options.livereload%>'  //监听前面声明的端口
                },
                files: [
                    'doc/**/*.*'
                ]
            }
        },
        jshint: {
            options: {
                //node:true,
                browser: true,//将bom对象作为已经定义的全局对象,设置为false的时候,使用document或者window等bom对象的时候,会提示没有定义的错误
                jquery: true,
                globals: {
                    angular: true,
                    WebUploader: true,
                    qc: true
                },
                strict: false,
                bitwise: true,//不允许使用位运算符
                loopfunc: false,//不允许在循环中定义function
                maxerr: 10,//最多提示10个错误,后面的提示"错误太多"
                multistr: false,//不允许多行字符串
                shadow: true,
                asi: false,//行尾必须加分号
                curly: true,//if,while 等结构化语句必须用{}来明确代码块
                debug: false,//不要出现debugger;这样的语句
                //eqeqeq: true,//使用恒等比较(===或者!==)
                eqnull: true,//允许使用"== null"作比较 ,== null 通常用来判断一个变量是undefined或者是null（当时用==，null和undefined都会转化为false）
                newcap: true,//要求每一个构造函数名都要大写字母开头。构造器是一种使用new运算符来创建对象的一种函数，new操作符会创建新的对象，并建立这个对象自己的this，一个构造函数如果不用new运算符来运行，那么他的this会指向全局对象而导致一些问题的发生
                noarg: true,//禁止arguments.caller和arguments.callee的使用
                noempty: true,//会禁止出现空的代码块
                //regexp:true,//不允许使用.和[^...]的正则
                //nomen:true,//禁用下划线的变量名
                onevar: true,//函数只被var的形式声明一遍
                //plusplus:true,//禁用自增运算和自减运算++和--可能会带来一些代码的阅读上的困惑。
                sub: true,//会允许各种形式的下标来访问对象
                undef: true,//要求所有的非全局变量，在使用前都被声明
                //unused:true,//
                white: true,//严格的空白规范检查代码
                evil: true,//允许使用eval ,eval提供了访问Javascript编译器的途径.这有时很有用，但是同时也对你的代码形成了注入攻击的危险
                forin: true,//for in 循环里面必须出现hasOwnProperty
                boss: false//不允许在if，for，while里面编写赋值语句
            },
            common_qc: {
                src: ['<%= config.src.js %>/qc/**/*.js']
            },
            component_uploader: {
                src: ['<%= config.src.js %>/component/**/*.js']
            }
        },
        autoprefixer: {
            options: {
                //browsers: ['last 2 versions', '> 2%','ie 8', 'ie 9','Firefox ESR','Opera 12.1'],
                //map: true
                //browsers: ['last 2 versions', 'ie 8', 'ie 9'],
                //safe: true
            },
            qc: {
                src: '<%= config.src.css %>/qc/quauq.css'
            }
        },
        concat: {
            options: {
                separator: '\n/*华丽的分隔线*/\n',
                //banner:'/*公共的js*/',
                stripBanners: true
            },
            js_common_qc: {
                src: [
                    '<%= config.src.js %>/qc/qc.basic*.js',
                    '<%= config.src.js %>/qc/qc.module.js',
                    '<%= config.src.js %>/qc/**/qc*.js'
                ],
                dest: '<%= config.tmp %>/qc/js/common/qc.js'
            }
        },
        copy: {
            //合并后的js文件从tmp复制到static中
            js_common_qc: {
                flatten: true,
                expand: true,
                cwd: '<%= config.tmp %>/qc/js/common/',
                src: '**',
                dest: '<%= config.static.js %>/common/'
            },
            css_common_qc: {
                flatten: true,
                expand: true,
                cwd: '<%= config.src.css %>/qc/',
                src: ['quauq.css','qc.print.css'],
                dest: '<%= config.static.css %>/common/'
            },

            //第三方常用组件js源文件
            js_common_component_src: {
                expand: true,
                flatten: true,
                cwd: '<%= config.src.component %>/',
                src: [
                    'jquery/dist/jquery.js',
                    'jquery-ui/jquery-ui.js',
                    'jquery-nicescroll/jquery.nicescroll.js',
                    'angular/angular.js',
                    'angular-ui-sortable/sortable.js',
                    'angular-resource/angular-resource.js',
                    'angular-cookies/angular-cookies.js',
                    'bootstrap/dist/js/bootstrap.js',
                    'respond/dest/respond.src.js',
                ],
                dest: '<%= config.static.js %>/common/'
            },
            //第三方常用组件js min文件
            js_common_component_min: {
                expand: true,
                flatten: true,
                cwd: '<%= config.src.component %>/',
                src: [
                    'jquery/dist/jquery.min.js',
                    'jquery-ui/jquery-ui.min.js',
                    'jquery-nicescroll/jquery.nicescroll.min.js',
                    'angular/angular.min.js',
                    'angular-ui-sortable/sortable.min.js',
                    'angular-resource/angular-resource.min.js',
                    'angular-cookies/angular-cookies.min.js',
                    'bootstrap/dist/js/bootstrap.min.js',
                    'respond/dest/respond.min.js'
                ],
                dest: '<%= config.static.js %>/common/'
            },

            js_component_uploader_src: {
                expand: true,
                flatten: true,
                src: [
                    '<%= config.src.component %>/webuploader/webuploader.js',
                    '<%= config.src.js %>/component/qc.uploader.js',
                ],
                dest: '<%= config.static.js %>/component/'
            },
            js_component_uploader_min: {
                expand: true,
                flatten: true,
                src: [
                    '<%= config.src.component %>/webuploader/webuploader.min.js',
                ],
                dest: '<%= config.static.js %>/component/'
            },

            swf_component_uploader: {
                expand: true,
                flatten: true,
                src: [
                    '<%= config.src.component %>/webuploader/Uploader.swf',
                ],
                dest: '<%= config.static.flash %>/'
            },

            //第三方组件css 源文件
            css_common_component_src: {
                expand: true,
                flatten: true,
                cwd: '<%= config.src.component %>/',
                src: [
                    'bootstrap/dist/css/bootstrap.css',
                    'jquery-ui/themes/base/jquery-ui.css',
                    'font-awesome/css/font-awesome.css'
                ],
                dest: '<%= config.static.css %>/common/'
            },
            //第三方组件css min文件
            css_common_component_min: {
                expand: true,
                flatten: true,
                cwd: '<%= config.src.component %>/',
                src: [
                    'bootstrap/dist/css/bootstrap.min.css',
                    'jquery-ui/themes/base/jquery-ui.min.css',
                    'font-awesome/css/font-awesome.min.css'
                ],
                dest: '<%= config.static.css %>/common/'
            },

            css_component_uploader_src: {
                expand: true,
                flatten: true,
                src: [
                    '<%= config.src.css %>/component/qc.uploader.css'
                ],
                dest: '<%= config.static.css %>/component/'
            },

            //font-awesome 字体文件
            font_common_component_awesome:{
                expand: true,
                flatten: true,
                cwd: '<%= config.src.component %>/font-awesome/fonts/',
                src:'*',
                dest: '<%= config.static.css %>/fonts/'
            },


            img_common: {
                expand: true,
                flatten: false,
                cwd: '<%= config.src.img %>/',
                src: [
                    '**/*.*'
                ],
                dest: '<%= config.static.img %>/'
            },

            img_common_component_src: {
                expand: true,
                flatten: true,
                cwd: '<%= config.src.component %>/',
                src: [
                    'jquery-ui/themes/base/images/**/*.*'
                ],
                dest: '<%= config.static.css %>/common/images'
            }

        },
        uglify: {
            options: {
                report: 'min',
                sourceMap: false//不生成map文件
            },
            //压缩项目js
            common_qc: {
                options: {
                    mangle: true, //混淆变量名
                    preserveComments: false,//删除注释
                    footer: '\n/*! <%= pkg.name %> 最后修改于： <%= grunt.template.today("yyyy-mm-dd") %> */',//添加footer
                    expand: true
                },
                files: {
                    '<%= config.static.js %>/common/qc.min.js': '<%= config.static.js %>/common/qc.js'
                }
            },
            component_uploader: {
                options: {
                    mangle: true, //混淆变量名
                    preserveComments: false,//删除注释
                    footer: '\n/*! <%= pkg.name %> 最后修改于： <%= grunt.template.today("yyyy-mm-dd") %> */',//添加footer
                    expand: true
                },
                files: {
                    '<%= config.static.js %>/component/qc.uploader.min.js': '<%= config.static.js %>/component/qc.uploader.js'
                }
            }
        },
        cssmin: {
            options: {
                shorthandCompacting: false,
                roundingPrecision: -1
            },
            common_qc: {
                files: {
                    '<%= config.static.css %>/common/quauq.min.css': '<%= config.static.css %>/common/quauq.css',
                    '<%= config.static.css %>/common/qc.print.min.css': '<%= config.static.css %>/common/qc.print.css'
                }
            },
            component_uploader: {
                files: {
                    '<%= config.static.css %>/component/qc.uploader.min.css': '<%= config.static.css %>/component/qc.uploader.css'
                }
            }
        },
        clean: {
            tmp_qc: {
                src: '<%= config.tmp %>/qc/'
            },
            dist_html: {
                src: ['<%= config.html %>/**/*.html'],
                //*:匹配任意字符,不匹配"/"
                //?:匹配任意一个字符,不匹配"/"
                //**匹配任意数量的任意字符,包括"/"
                //{a,b}:匹配或者b
                //!:非
                filter: function (filepath) {//过滤
                    return (!grunt.file.isDir(filepath));
                }
            }
        }
    });
    // 默认被执行的任务列表。
    //grunt.registerTask('default', ['copy']);

    grunt.registerTask('build', [
        'less:qc',
        'less:qc_print',
        'autoprefixer:qc',
        'jshint:common_qc',
        'jshint:component_uploader',

        'concat:js_common_qc',
        //'concat:css_common_qc',

        'copy:js_common_qc',
        'copy:css_common_qc',

        'copy:js_common_component_src',
        'copy:js_common_component_min',

        'copy:js_component_uploader_src',
        'copy:js_component_uploader_min',
        'copy:swf_component_uploader',

        'copy:css_common_component_src',
        'copy:css_common_component_min',

        'copy:css_component_uploader_src',

        'copy:font_common_component_awesome',

        'copy:img_common',

        'copy:img_common_component_src',

        'uglify:common_qc',

        'uglify:component_uploader',

        'cssmin:common_qc',

        'cssmin:component_uploader',

        'clean:tmp_qc'
    ]);
    grunt.registerTask('serve:sample', [
        'connect:sample',

        'watch:sample',
        'watch:less'
    ]);
    grunt.registerTask('serve:doc', [
        'connect:doc',
        'watch:doc'
    ]);
    //组合任务
    grunt.registerTask('serve', 'start the doc!!!', function (target) {
        if (grunt.option('allow-remote')) {
            grunt.config.set('connect.options.hostname', '0.0.0.0');
        }
        if (target === 'dist') {
            return grunt.task.run(['build', 'connect:dist:keepalive']);
            //keepalive:false,//grunt 运行完成后,关闭web server
        }
        grunt.task.run(['', '', '']);
    });
}
