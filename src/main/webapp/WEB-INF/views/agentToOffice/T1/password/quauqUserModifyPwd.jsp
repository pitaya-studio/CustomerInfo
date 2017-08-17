<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <title>修改密码</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/>
    <%-- <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/> --%>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <link rel="stylesheet" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js"></script>
	<script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>
	<script type="text/javascript">
		var flag = '${flag}';
		var message = '${message}';
		$(document).ready(function() {
			if(flag && flag == "success"){
				top.$.jBox.tip(message,'success',{top:'0'});
				window.parent.window.jBox.close();
				return;
			}
			if(message){
				top.$.jBox.tip(message,'error',{top:'0'});
			}
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
					// 解决火狐弹出框问题
					if ($("#newPassword").val() != $("#confirmNewPassword").val()) {
						return  false;
					}
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
		
		function baseInfo(){
    		location.href="${ctx}/person/info/getAgentInfo";
    	}
    	
    	function doSthOldPwd(obj) {
    		// 开始输入则清空旧密码错误提示
    		$("span[name=targetHere]").empty();
    		$(obj).attr("onkeyup","");
    		$(obj).attr("onafterpaste","");
    	}
    	
	</script>
	<style type="text/css">
		.details_d1{
			vertical-align: top;
		}
		.details_d2{
			vertical-align: top;
			height:60px;
		}
		label.error{
			padding-left:0;
			background:none;
		}
		.details_d1 label{
			display: inline-block;
    		width: 90px;
		}
	</style>
</head>
<body>
<div style="overflow:hidden;">
	<div id="changePwd" >
		<form:form id="inputForm" modelAttribute="user" action="${ctx}/t1/password/manage/modifyPwd" method="post" class="form-horizontal" autocomplete="off">
			<form:hidden path="id"/>
			
			<table>
				<tbody>
					<tr style="height:45px;">
						<td class="details_d1"><label style="margin-top:5px;" for="oldPassword">旧密码：</label></td>
						<td class="details_d2">
							<input id="oldPassword" name="oldPassword" style="height:30px" type="password" value="" maxlength="50" minlength="3" class="required" autocomplete="off" onkeyup="doSthOldPwd(this)" onafterpaste="doSthOldPwd(this)" />
							<div class="">
								<span name="targetHere" style="color:red">${message }</span>
							</div>
						</td>
					</tr>
					<tr style="height:45px;">
						<td class="details_d1"><label style="margin-top:5px;" for="newPassword">新密码：</label></td>
						<td class="details_d2">
							<input id="newPassword" name="newPassword" style="height:30px" type="password" value="" maxlength="50" minlength="3" class="required" autocomplete="off"/>
						</td>
					</tr>
					<tr style="height:45px;">
						<td  style="width:140px;" class="details_d1"><label style="margin-top:5px;" for="confirmNewPassword">确认密码：</label></td>
						<td class="details_d2">
							<input id="confirmNewPassword" name="confirmNewPassword" style="height:30px" type="password" value="" maxlength="50" minlength="3" class="required" equalTo="#newPassword" autocomplete="off"/>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="cancelSave">
				<input id="btnSubmit" type="submit" class="button1 savebutton" value="保&nbsp;&nbsp;&nbsp;存"/>
			</div>
		</form:form>
	</div>
</div>
</body>
</html>