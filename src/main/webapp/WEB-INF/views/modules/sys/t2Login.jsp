<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html >
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=8,IE=9,IE=10" />
	<link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
	<%--<title>QUAUQ（夸克）旅游交易预订系统</title>--%>
	<title>QUAUQ（夸克）旅游业务管理系统</title>
	<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctxStatic}/css/t2LoginRebuild.css" type="text/css" rel="stylesheet" />
<style>
.login_ie7{width:548px;padding:0 10px 0 10px;height:38px;line-height:38px;font-size:12px;position:fixed;top:0;left:20%;background-color:#ffedbb; -moz-border-radius: 0 0 3px 3px;-webkit-border-radius: 0 0 3px 3px;border-radius:0 0 3px 3px;border:1px solid #f2d083;border-top:none; }
.login_ie7 .ie7_judge{color:#72612b;}
.login_ie7 .ie7_judge_red{color:#cc3300;}
.login_ie7 .ie7_judge_red a{color:#cc3300;}
.login_ie7 .ie7_judge_del{width:12px;height:12px;margin-left:5px;cursor:pointer;}
</style>
	<script src="${ctxStatic}/jquery/jquery-1.9.1.min.js" type="text/javascript" ></script>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/t2LoginRebuild.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/t1Common.js" type="text/javascript"></script>

	<script type="text/javascript">
		$(document).ready(function() {			
				//记住我
			$("label[for='rememberMe']").click(function(){
				var $this = $(this);
				var $i = $this.find("i");
				if($i.hasClass("checked")){
					$i.removeClass("checked");
				}else{
					$i.addClass("checked");
				}
			});
			
			$("#loginForm").validate({
				rules: {
					validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
				},
				messages: {
					username: {required: "请填写用户名."},password: {required: "请填写密码."},
					validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
				},
				errorLabelContainer: "#messageBox",
				errorPlacement: function(error, element) {
					error.appendTo($("#loginError").parent());
				} 
			});
			//处理多次点击重复提交问题（由于使用disabled会导致表单不能提交，所以暂时使用切换标签来实现）
			$("#login-btn1").click(function(){
				//debugger;
				var username = $("#username").val();
				var password = $("#password").val();
				var loginError = $("#loginError").val();
				if(username != "" && password != ""){
					if('${isValidateCodeLogin}' != false){
						if(loginError == null){
							$("#login-btn1").css("display","none");
							$("#login-btn2").css("display","block").css("background-color","#cccccc;");
						}
					}
				}
			});
			
		});
		// 如果在框架中，则跳转刷新上级页面
		if(self.frameElement && self.frameElement.tagName=="IFRAME"){
			parent.location.reload();
		}
		<c:if test="${fn:contains(pageContext.request.requestURL,'lmels.com')}">
		function guestvisit(){
			$("#username").val('lmelsguest');
			$("#password").val('aaaaaa');
			$("#loginForm").submit();
		}
		</c:if>
		
		$(function(){
			var width=$(document).width();
			var cwidth=$('.login_ie7').width();
			var leftpx=Math.floor((width-cwidth)/2);	
			$('.login_ie7').css('left',leftpx+'px');
		});
		function hide_login_ie7(){
			$('.login_ie7').hide();
		}
		
	</script>
</head>
<body>
	<%--背景图片--%>
	<div class="bg"></div>
	<%--遮罩--%>
	<div class="bg-cover"></div>
	<%--头部开始--%>
	<header>
		<%--logo图片--%>
		<div class="logo"></div>
		<span class="title">旅游业务管理系统</span>
		<span>用户登录</span>

	</header>
	<%--头部结束--%>
	<!--[if lt IE 8]>
	<div class="login_ie7">
	<span class="ie7_judge">
		你正在使用一个不安全的浏览器，它可能会对您的计算机安全造成威胁，
	</span>
	<span class="ie7_judge_red">
		请更换推荐浏览器或<a href="http://browsehappy.com/" target="_blank">升级</a>
	</span>
	<span class="ie7_judge_del" onclick="hide_login_ie7()"><img src="${ctxStatic}/images/login_ie_icon.png" /></span>
</div>  
<![endif]-->
	<input type="hidden" value="${ctx}" id="ctx_T2Login">
	<div id="loginError"></div>
	<c:set var="prefixLongin" value=""></c:set>
	<c:choose>
		<c:when test="${fn:contains(pageContext.request.requestURL,'ycbooking')}">
			<c:set var="prefixLongin" value="remote-vacation-"></c:set>
		</c:when>
		<c:when test="${fn:contains(pageContext.request.requestURL,'hqqz.quauqsystem.com.cn')}">
			<c:set var="prefixLogo" value="zsgl-"></c:set>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	<div class="${prefixLongin}loginDiv">
		<%--左侧slogan开始--%>
		<div class="slogan"></div>
		<%--左侧slogan结束--%>
		<!--登录内容开始-->
		<div class="login-w clearfix">
	    	<div class="${prefixLongin}login">
	            <form id="loginForm" class="loginForm" action="${ctx}/login" method="post" autocomplete="off">
	            <!--用户名、密码开始-->
	            <div class="loginCon">
	            	<dl class="login-user">
	                	<dt>用户名</dt>
	                    <dd>
	                    	  <input type="text" id="username" name="username" autocomplete="off" class="required" value="${username}" placeholder="请输入用户名" />
	                    </dd>
	                </dl>
	                <dl class="login-pwd">
	                	<dt>密码</dt>
	                    <dd>
							<input type="password" id="password" name="password" autocomplete="off" class="required" placeholder="请输入密码" />
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
				<div id="messageBox2" class="alert alert-error <%=domain_name_error==null?"hide":""%>"><button data-dismiss="alert" class="close">×</button>
					<label id="Domain_name_error" class="error"><%=domain_name_error%></label>
				</div>
				<div class="cl-both"></div>
				<c:if test="${isValidateCodeLogin}">
					<div class="validateCode clearfix">
						<label class="input-label mid" for="validateCode">验证码：</label>
						<tags:validateCode name="validateCode" inputCssStyle="margin-bottom:0;width:80px"/>
					</div>
				</c:if>
				
				<!--验证码结束-->
				<p class="rememberme"><input type="checkbox" name="rememberMe" id="rememberMe" /><label for="rememberMe"><i></i>记住我 (公共场所慎用)</label></p>
	            <div class="clearfix"><input id="login-btn1" class="login-btn" type="submit" value="登&nbsp;录" /></div>
	           	<!--  处理多次点击重复提交问题（由于使用disabled会导致表单不能提交，所以暂时使用切换标签来实现） -->
	            <div class="clearfix"><input style="display:none" id="login-btn2" class="login-btn" type="submit" value="正在登录..." disabled="disabled"/></div>
	            <c:if test="${fn:contains(pageContext.request.requestURL,'lmels.com')}">
					<a href="javascript:void(0);" onclick="javascript:guestvisit();">以游客身份登录</a>
		        </c:if>
	            </form>
	            <!--建议开始-->
	            <p class="brower">建议使用以下浏览器：<a class="browser-ff" href="http://download.firefox.com.cn/releases/full/35.0/zh-CN/Firefox-full-latest.exe" title="火狐">火狐</a><a class="browser-ie8" href="${ctxStatic}/file/IE.exe" title="推荐使用IE9">IE</a><a class="browser-gg" href="http://w.x.baidu.com/alading/anquan_soft_down_ub/14744" title="谷歌">谷歌</a></p>
	            <!--建议结束-->
	        </div>
	    </div>
		</div>
	    <!--登录内容结束-->
	    <!--页脚开始-->
	<div class="loginFooter">
	    	<div class="login-w">
	    		<%--<c:choose>--%>
					<%--<c:when test="${fn:contains(pageContext.request.requestURL,'uc-tours')}">--%>
						<%--<img class="login-logo" src="${ctxStatic}/images/youchuang-travel.png" alt="trekiz" width="94" height="32" />--%>
					<%--</c:when>--%>
					<%--<c:otherwise>--%>
						<%--<img class="login-logo" src="${ctxStatic}/images/${prefixLogo}logo-94-32.png" alt="trekiz" width="94" height="32" />--%>
					<%--</c:otherwise>--%>
				<%--</c:choose>--%>
	            <%--<span class="login-splite">|</span>--%>
	            <span class="loginFooter-txt">Copyright © ${fns:getConfig('copyrightYear')} , quauqsystem.com.cn&nbsp;&nbsp; | &nbsp;&nbsp;Powered  by  QUAUQ  Technology &nbsp;&nbsp;|&nbsp;&nbsp; 京ICP备13023310号-9&nbsp;&nbsp;|&nbsp;&nbsp;客服电话：010-85718666</span>
	        </div>
	    </div>
	    <!--页脚结束-->
	<script src="${ctxStatic}/js/withpw_plaholder.js"></script><%--ie9兼容placeholder--%>
</body>
</html>