<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10"/>
    <meta http-equiv="content-type" content="text/html;charset=utf-8">
<%--begin--%>

	<meta name="description" content="A responsive coming soon template, un template HTML pour une page en cours de construction">

	<!-- Disable screen scaling-->
	<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1, maximum-scale=1, user-scalable=0">

	<!-- Place favicon.ico and apple-touch-icon(s) in the root directory -->

	<!-- Initializer -->
	<link rel="stylesheet" href="${ctxStatic}/ljht/css/normalize.css">


	<!-- Vendor CSS style -->
	<link rel="stylesheet" href="${ctxStatic}/ljht/css/pageloader.css">
	<link rel="stylesheet" href="${ctxStatic}/ljht/css/foundation.min.css">
	<link rel="stylesheet" href="${ctxStatic}/ljht/js/vendor/jquery.fullPage.css">
	<link rel="stylesheet" href="${ctxStatic}/ljht/js/vegas/vegas.min.css">


	<!-- Main CSS files -->
	<link rel="stylesheet" href="${ctxStatic}/ljht/css/main.css">
	<!-- alt layout -->
	<link rel="stylesheet" href="${ctxStatic}/ljht/css/style-color2.css">
	<!-- responsiveness -->
	<link rel="stylesheet" href="${ctxStatic}/ljht/css/main_responsive.css">
	<%--end--%>


<%--

	<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
	<link href="${ctxStatic}/css/huanqiu-style.css" type="text/css" rel="stylesheet"/>
	<link href="${ctxStatic}/css/login-huiteng.css" type="text/css" rel="stylesheet"/>

--%>





	<c:choose>
		<c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
			<link href="${ctxStatic}/images/huiTeng/huiTengFavicon.ico" rel="shortcut icon"/>
			<title>北京辉腾国际旅行社有限公司</title>
		</c:when>
		<c:otherwise>
			<link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
			<title>QUAUQ（夸克）旅游交易预订系统</title>
		</c:otherwise>
	</c:choose>


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




<body id="menu" class="alt-bg">
<!--[if lt IE 8]>
<p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
<![endif]-->
<!-- BEGIN OF site header Menu -->
<header class="header-top">
	<div class="logo-wrapper">
		<h2 class="logo">
			<!-- <img src="img/logo_huiTeng.png" alt="Logo">  -->
			<!-- <span class="title">北京辉腾国际旅行有限公司</span>  -->
		</h2>
	</div>
	<!-- Begin of Site primary navigation -->
	<nav class="site-nav" id="site-nav">
		<ul  >
			<!--<li class="active" data-menuanchor="home">
                <a href="#home">Home</a>
            </li>
            <li data-menuanchor="about-us">
                <a href="#about-us">About</a>
            </li>-->
			<li>
				<p>建议使用以下浏览器</p>
			</li >
			<li style="width:17px;">
				<a class="browser-ie8" href="${ctxStatic}/file/IE.exe" title="推荐使用IE9"><img src="${ctxStatic}/ljht/img/IE.png"></a>
			</li>
			<li style="width: 15px;margin-top: 1px;">
				<a class="browser-gg" href="http://w.x.baidu.com/alading/anquan_soft_down_ub/14744" title="谷歌"><img src="${ctxStatic}/ljht/img/chrome.png"></a>
			</li>
			<li style="width:17px;">
				<a class="browser-ff" href="http://download.firefox.com.cn/releases/full/35.0/zh-CN/Firefox-full-latest.exe" title="火狐"><img src="${ctxStatic}/ljht/img/firefox.png"></a>
			</li>
			<!-- <li data-menuanchor="write-message" > -->
			<!-- <img src="img/360.png"></img> -->
			</li>
		</ul>
	</nav>
	<!-- End of Site primary navigation -->
</header>
<!-- END OF site header Menu-->


<!-- BEGIN OF sidebar page navigation -->

<!-- END OF sidebar page navigation -->

<!-- BEGIN OF site cover -->
<div class="page-cover">
	<!-- Cover Background -->
	<div class="cover-bg pos-abs full-size bg-img" data-image-src="${ctxStatic}/ljht/img/down_huiTeng.png"></div>

	<!-- BEGIN OF Slideshow Background -->
	<div class="cover-bg pos-abs full-size slide-show">
		<i class='img' data-src='${ctxStatic}/ljht/img/up_huiTeng.png'></i>
		<!-- <i class='img' data-src='img/bg-sample1.jpg'></i> -->
		<!-- <i class='img' data-src='img/2.png'></i>
        <i class='img' data-src='img/5.png'></i>
        <i class='img' data-src='img/bg-sample4.jpg'></i>
        <i class='img' data-src='img/bg-sample1.jpg'></i> -->
	</div>




	<div class="cover-bg-mask pos-abs full-size bg-color" data-bgcolor="rgba(0, 0, 0, 0.50)"></div>
	<!-- <div class="cover-bg-mask pos-abs full-size bg-color" data-bgcolor="rgba(45, 46, 51, 0.8)"></div> -->

</div>
<!--END OF site Cover -->



<!-- BEGIN OF site main content content here -->
<main class="page-main page-home fullpg" id="mainpage">


	<div class="section  section-message section-cent" data-section="write-message">
		<!-- Content -->
		<section class="content">
			<!-- left elements -->
			<div class="c-left">
				<div class="anim-slideleft">
					<div class="c-logo home-logo ">
						<!-- <i class="icon lnr lnr-pencil"></i> -->
						<img src="${ctxStatic}/ljht/img/biaoyu_huiTeng.png">

					</div>
				</div>
			</div>

			<!-- right elements -->
			<div class="c-right">
				<div class="anim-slideright">
					<header class="header">
						<!-- <h2><strong>辉腾</strong> 旅游</h2> -->
						<img src="${ctxStatic}/ljht/img/logo_huiTeng.png" style="width:50%; height:50%;">
						<h3 style="font-size:20px;">
							<!-- <span>北京辉腾国际旅行有限公司</span> -->
						</h3>
					</header>
					<!-- begin of forms -->
					<div class="form-wrapper desc">
						<form id="loginForm" class="message form send_message_form" method="post" autocomplete="off" action="${ctx}/login">
							<div class="fields clearfix">
								<div id="input" class="input">
									<label for="username" style="margin-bottom:5px;">用户名 </label>
									<input id="username" name="username" type="text" placeholder="请输入用户名" autocomplete="off" class="required"  value="${username}" style="width:330px;"/>
								</div>
							</div>
							<div class="fields clearfix">
								<div class="input">
									<label for="password" style="margin-bottom:5px;">密码</label>
									<input id="password" type="password" placeholder="请输入密码" name="password"  style="width:330px;" autocomplete="off" class="required"/>
								</div>
							</div>

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
							<c:if test="${isValidateCodeLogin}">
								<div class="validateCode clearfix">
									<label class="input-label mid" for="validateCode">验证码：</label>
									<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;width:92px;"/>
								</div>
							</c:if>

							<div id="remember-forget">
								<input name="rememberMe" id="rememberMe"  type="checkbox" />
								<label for="rememberMe">记住我（公共场所慎用）</label>
							</div>

							<div class="btns">
								<button id="submit-message" class="btn email_b" name="submit_message">登入</button>
							</div>
						</form>
					</div>
					<!-- end of forms -->
				</div>
			</div>
		</section>
	</div>
</main>
<!-- BEGIN OF site footer -->
<footer class="site-footer">
	<div class="note">
		<p>Copyright © 2016 旅游交易预订系统&nbsp;&nbsp;&nbsp;Powered by QUAUQ Technology&nbsp;&nbsp;&nbsp;技术支持：010-85718666</p>
	</div>
</footer>
<!-- END OF site footer -->


<!-- All vendor scripts -->
<script src="${ctxStatic}/ljht/js/vendor/all.js"></script>
<!-- Form script -->
<%--<script src="${ctxStatic}/ljht/js/form_script.js"></script>--%>
<!-- Javascript main files -->
<script src="${ctxStatic}/ljht/js/main.js"></script>
<%--<script src="${ctxStatic}/js/placeholder.js"></script>--%>
<script src="${ctxStatic}/js/withpw_plaholder.js"></script>
</body>
</html>