<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>字典管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#value").focus();
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
</head>
<body>
<!-- 
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/dict/">字典列表</a></li>
		<li class="active"><a href="${ctx}/sys/dict/form?id=${visabasics.id}">字典${not empty dict.id?'修改':'添加'}查看</a></li>
	</ul><br/>
 -->
	<form:form id="inputForm" modelAttribute="visabasics" action="${ctx}/visa/instruction/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>

		<table>
			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">签证类型:</label>
						<div class="controls">
							<form:input path="visaType" htmlEscape="false" maxlength="50"
								class="required" />
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">是否需要面试:</label>
						<div class="controls">
							<form:select path="isNeedAudition">
							    <form:options items="${yesorno}" itemLabel="label" itemValue="value" />
							</form:select>
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">需机票订单:</label>
						<div class="controls">
							<form:radiobuttons path="isneedFlightOrder" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
			</tr>


			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">签证国家:</label>
						<div class="controls">
							<form:input path="visaCountry" htmlEscape="false" maxlength="50"
								class="required" />
						</div>
					</div>
				</td>
				<td>
					<div class="control-group">
						<label class="control-label">抽查面试:</label>
						<div class="controls">
							<form:radiobuttons path="isNeedSpotAudition" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">酒店订单:</label>
						<div class="controls">
							<form:radiobuttons path="isNeedSpotAudition" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
			</tr>


			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">办理所需时间:</label>
						<div class="controls">
							<form:input path="handleTime" htmlEscape="false" maxlength="50"
								class="required" />
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">是否需要预约:</label>
						<div class="controls">
							<form:radiobuttons path="isNeedBespeak" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">是否需要保险:</label>
						<div class="controls">
							<form:radiobuttons path="isNeedInsurance" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
			</tr>


			<tr>
				<td>
					<div class="control-group">
						<label class="control-label">销签所需时间:</label>
						<div class="controls">
							<form:input path="pinCheckTime" htmlEscape="false" maxlength="50"
								class="required" />
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">是否需要销签:</label>
						<div class="controls">
							<form:radiobuttons path="isNeedPinCheck" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
				<td>
					<div class="control-group">
						<label class="control-label">自备邀请:</label>
						<div class="controls">
							<form:radiobuttons path="selfInvited" htmlEscape="false"
								maxlength="50" class="required" items="${yesorno}"
								itemLabel="label" itemValue="value" />
						</div>
					</div></td>
			</tr>


		</table>

		<div class="control-group">
            <label class="control-label">领区划分:</label>
            <div class="controls">
                <form:textarea path="pinCheckTime" htmlEscape="false" class="required" style="width: 60%;" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">贴示:</label>
            <div class="controls">
                <form:textarea path="stickshow" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">注意事项:</label>
            <div class="controls">
                <form:textarea path="attention" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">特别提示:</label>
            <div class="controls">
                <form:textarea path="specialTips" htmlEscape="false"  class="required" style="width: 60%;"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">其他说明:</label>
            <div class="controls">
                <form:textarea path="otherDescription" htmlEscape="false" class="required" style="width: 60%;"/>
            </div>
        </div>
        
        <div class="form-actions">
            <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
            <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
        </div>
		
	</form:form>
</body>
</html>