<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				});
			jQuery.extend(jQuery.validator.messages, {
		  		required: "必填信息",
		  		digits:"请输入正确的数字",
		  		email:"请输入正确的邮箱",
		  		number : "请输入正确的数字价格"
   			}); 
			});
		$(document).ready(function() {
			$("#inputForm").validate({
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
		<li class="active"><a href="${ctx}/sys/profile/info">个人信息</a></li>
		<li><a href="${ctx}/sys/profile/modifyPwd">修改密码</a></li>
        <shiro:hasPermission name="sys:user:view"><li><a href="${ctx}/sys/user/">账号管理</a></li></shiro:hasPermission>
        <shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/sys/user/form">账号添加</a></li></shiro:hasPermission>
        <shiro:hasPermission name="transfer:leave:account"><li><a href="${ctx}/sys/user/transferLeaveAccountForm">离职账户转移</a></li></shiro:hasPermission>
	</content>
	<div class="sysdiv">
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/profile/info" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
			<p>
				<label>姓名:</label>
			<span>
				<form:input path="name" htmlEscape="false" maxlength="50" class="required" readonly="true"/>
			</span>
			</p>
		<p>
			<label>邮箱:</label>
			<span>
				<form:input path="email" htmlEscape="false" maxlength="50" class="email"/>
			</span>
		</p>
		<p>
			<label>电话:</label>
			<span>
				<form:input path="phone" htmlEscape="false" maxlength="15" />
			</span>
		</p>
		<p>
			<label>手机:</label>
			<span>
				<form:input path="mobile" htmlEscape="false" maxlength="50" />
			</span>
		</p>
		<p>
			<label>微信:</label>
			<span>
				<form:input path="weixin" htmlEscape="false" maxlength="20" />
			</span>
		</p>
		<p>
			<label>备注:</label>
			<span>
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
			</span>
		</p>
		<p>
			<label>用户类型:</label>
            <span>
			${fns:getDictLabel(user.userType, 'sys_user_type', '无')}
            </span>
		</p>
		<p>
			<label>用户角色:</label>
			<span>
				${user.roleNames}
			</span>
		</p>
		<p>
			<label>最后登录:</label>
			<span>
				IP: ${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate value="${user.loginDate}" type="both" dateStyle="full"/>
			</span>
		</p>
		<p>
			<label>&nbsp;</label>
			<span>
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保&nbsp;&nbsp;&nbsp;存"/>
			</span>
		</p>
	</form:form>
	</div>
</body>
</html>