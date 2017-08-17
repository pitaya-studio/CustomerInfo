<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>修改密码</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#oldPassword").focus();
			$("#inputForm").validate({
				rules: {
					oldPassword:"required"
				},
				errorPlacement: function(error, element) { 
				    if ( element.is(":radio") ) 
				        error.appendTo ( element.parent() ); 
				    else if ( element.is(":checkbox") ) 
				        error.appendTo ( element.parent() ); 
				    else if ( element.is("input") ) 
				        error.appendTo ( element.parent() ); 
				    else 
				        error.insertAfter(element); 
				},
				messages: {
					confirmNewPassword: {equalTo: "两次输入不同"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
<style type="text/css">
</style>
</head>
<body>
    <content tag="three_level_menu">
		<li><a href="${ctx}/sys/profile/info">个人信息</a></li>
		<li class="active"><a href="${ctx}/sys/profile/modifyPwd">修改密码</a></li>
        <shiro:hasPermission name="sys:user:view"><li><a href="${ctx}/sys/user/">账号管理</a></li></shiro:hasPermission>
        <shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form">账号添加</a></li></shiro:hasPermission>
        <shiro:hasPermission name="transfer:leave:account"><li><a href="${ctx}/sys/user/transferLeaveAccountForm">离职账户转移</a></li></shiro:hasPermission>
	</content>
	<div class="sysdiv">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/profile/modifyPwd" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<p>
			<label><font style="color:#ff0000; padding-right:5px;">*</font>旧密码:</label>
			<span>
				<input id="oldPassword" name="oldPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
			</span>
		</p>
		<p>
			<label><font style="color:#ff0000; padding-right:5px;">*</font>新密码:</label>
			<span>
				<input id="newPassword" name="newPassword" type="password" value="" maxlength="50" minlength="3" class="required"/>
			</span>
		</p>
		<p>
			<label><font style="color:#ff0000; padding-right:5px;">*</font>确认新密码:</label>
			<span>
				<input id="confirmNewPassword" name="confirmNewPassword" type="password" value="" maxlength="50" minlength="3" class="required" equalTo="#newPassword"/>
			</span>
		</p>
		<p>
			<label>&nbsp;</label>
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保&nbsp;&nbsp;&nbsp;存"/>
		</p>
	</form:form>
	</div>
</body>
</html>