<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
    <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
    <title>QUAUQ（夸克）旅游交易预订系统</title>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet"/>
    <style>
        .login_ie7 {
            width: 548px;
            padding: 0 10px 0 10px;
            height: 38px;
            line-height: 38px;
            font-size: 12px;
            position: fixed;
            top: 0;
            left: 20%;
            background-color: #ffedbb;
            -moz-border-radius: 0 0 3px 3px;
            -webkit-border-radius: 0 0 3px 3px;
            border-radius: 0 0 3px 3px;
            border: 1px solid #f2d083;
            border-top: none;
        }

        .login_ie7 .ie7_judge {
            color: #72612b;
        }

        .login_ie7 .ie7_judge_red {
            color: #cc3300;
        }

        .login_ie7 .ie7_judge_red a {
            color: #cc3300;
        }

        .login_ie7 .ie7_judge_del {
            width: 12px;
            height: 12px;
            margin-left: 5px;
            cursor: pointer;
        }
        .loginDivNew {
            font-size: 12px;
            margin-top: 0;
            display: table;
            height:86%;
            width: 100%;

        }
        body{
            background: url(${ctxStatic}/images/load.jpg) top center no-repeat;
            /*filter: url(images/load.jpg);*/
            /*-webkit-filter: blur(3px);*/
            /*-moz-filter: blur(3px);*/
            /*-ms-filter: blur(3px);*/
            /*filter: blur(3px);*/
            /*filter: progid:DXImageTransform.Microsoft.Blur(PixelRadius=10, MakeShadow=false);*/
        }
        html, body{ margin:0; height:100%; }
        .login{
            height:80%;
        }
        .brower{
            border: 0;
            margin-bottom: 40px;
        }
        .login-user{
            height:36px;
        }
        .rememberme{
            margin:8px;
        }
    </style>

    <script src="${ctxStatic}/js/jquery-1.9.1.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script type="text/javascript">
        //用于百度统计的js代码
        var _hmt = _hmt || [];
        (function() {
            var hm = document.createElement("script");
            hm.src = "//hm.baidu.com/hm.js?09b70cb643d83d26f997900c24c7ab59";
            var s = document.getElementsByTagName("script")[0];
            s.parentNode.insertBefore(hm, s);
        })();
        $(document).ready(function () {
            //记住我
            $("label[for='rememberMe']").click(function () {
                var $this = $(this);
                var $i = $this.find("i");
                if ($i.hasClass("checked")) {
                    $i.removeClass("checked");
                } else {
                    $i.addClass("checked");
                }
            });

            $("#loginForm").validate({
                rules: {
                    validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
                },
                messages: {
                    username: {required: "请填写用户名."}, password: {required: "请填写密码."},
                    validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
                },
                errorLabelContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    error.appendTo($("#loginError").parent());
                }
            });
        });
        // 如果在框架中，则跳转刷新上级页面
        if (self.frameElement && self.frameElement.tagName == "IFRAME") {
            parent.location.reload();
        }


        $(function () {
            var width = $(document).width();
            var cwidth = $('.login_ie7').width();
            var leftpx = Math.floor((width - cwidth) / 2);
            $('.login_ie7').css('left', leftpx + 'px');
        });
        function hide_login_ie7() {
            $('.login_ie7').hide();
        }

    </script>
</head>
<body>
<!--[if lt IE 8]>
<div class="login_ie7">
	<span class="ie7_judge">
		你正在使用一个不安全的浏览器，它可能会对您的计算机安全造成威胁，
	</span>
	<span class="ie7_judge_red">
		请更换推荐浏览器或<a href="http://browsehappy.com/" target="_blank">升级</a>
	</span>
    <span class="ie7_judge_del" onclick="hide_login_ie7()"><img src="images/login_ie_icon.png"/></span>
</div>
<![endif]-->
<div id="loginError"></div>


<div class="${prefixLongin}loginDivNew">
    <!--登录内容开始-->
    <div class="login-w clearfix">
        <div class="${prefixLongin}login">
            <h1 class="login-new-h1">login</h1>

            <form id="loginForm" class="loginForm" action="${ctx}/login" method="post" autocomplete="off">
                <!--用户名、密码开始-->
                <div class="loginConNew">
                    <dl class="login-user border_bottom">
                        <dt class="user"></dt>
                        <span class="baba">|</span>
                        <dd >
                            <input type="text" id="username" name="username" autocomplete="off" class="required"
                                   value="${username}"/>
                        </dd>
                    </dl>
                    <dl class="login-pwd-new border_bottom">
                        <dt class=" pwd"></dt>
                        <span class="baba">|</span>
                        <dd>
                            <input type="password" id="password" name="password" autocomplete="off" class="required"/>
                        </dd>
                    </dl>
                </div>
                <!--用户名、密码结束-->
                <!--错误提示信息开始
                <div id="messageBox" class="login-error"><i></i>
                    <label class="error" id="loginError" style="display: none;">用户或密码错误, 请重试.</label>
                    <label for="username" class="error">请填写用户名.</label><label for="password" class="error">请填写密码.</label><label for="validateCode" class="error">请填写验证码.</label>
                </div>
                -->
                <!--错误提示信息结束-->
                <!--验证码开始-->
				<%String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);%>
								<%String domain_name_error = (String) request.getAttribute("Domain_name_error"); %>

                <div id="messageBox" class="login-error <%=error==null?"hide":""%>"><i></i>
                    <label id="loginError" class="error"><%=error==null?"":"com.trekiz.admin.modules.sys.security.CaptchaException".equals(error)?"验证码错误, 请重试.":"用户或密码错误, 请重试." %></label>
                </div>
                <div id="messageBox2" class="alert alert-error <%=domain_name_error==null?"hide":""%>">
                    <button data-dismiss="alert" class="close">×</button>
                    <label id="Domain_name_error" class="error"><%=domain_name_error%></label>
                </div>
                <div class="cl-both"></div>
				<div class="cl-both"></div>
				<c:if test="${isValidateCodeLogin}">
					<div class="validateCode clearfix">
						<label class="input-label mid" for="validateCode">验证码：</label>
						<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
					</div>
				</c:if>

                <!--验证码结束-->
                <p class="rememberme"><input type="checkbox" name="rememberMe" id="rememberMe" class="display-block"/><label for="rememberMe" class="rememberNew">记住我
                    (公共场所慎用)</label></p>

                <div class="clearfix"><input class="login-btn" type="submit" value="登&nbsp;&nbsp;&nbsp;&nbsp;录"/></div>

            </form>
            <!--建议开始-->
            <p class="brower">建议使用以下浏览器：<a class="browser-ff"
                                           href="http://download.firefox.com.cn/releases/full/35.0/zh-CN/Firefox-full-latest.exe"
                                           title="火狐">火狐</a><a class="browser-ie8"
                                                               href="${ctxStatic}/file/IE.exe"
                                                               title="推荐使用IE9">IE</a><a class="browser-gg"
                                                                                     href="http://w.x.baidu.com/alading/anquan_soft_down_ub/14744"
                                                                                     title="谷歌">谷歌</a></p>
            <!--建议结束-->
        </div>
    </div>
    <!--登录内容结束-->

</div>
<div class="login-w quauqL ">
   	<img class="login-logoNew" src="${ctxStatic}/images/logo-New.png" alt="trekiz" width="94" height="32"/>
   	<br/>
    <!-- <span class="login-spliteF">|</span> -->
    <span class="loginFooter-txtNew">Copyright &copy; 2016 旅游交易预订系统&#12288;&#12288;客服电话：010-85718666</span>
    <div class="footer">
    </div>
</div>
</body>
</html>