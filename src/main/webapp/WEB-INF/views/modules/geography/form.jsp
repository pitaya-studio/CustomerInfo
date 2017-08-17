<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>SysGeography信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon" />
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
	<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					if($("#id").val()=='') {
						$("#inputForm").attr("action","${ctx}/sysGeography/save");
						$("#inputForm").submit();
						$("#btnSubmit").attr("disabled", true);
					} else {
						$("#inputForm").attr("action","${ctx}/sysGeography/update");
						$("#inputForm").submit();
						$("#btnSubmit").attr("disabled", true);
					}
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
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">SysGeography信息</div>
	<form:form method="post" modelAttribute="sysGeography" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="id" />
		<tags:message content=" ${message}"/>
		<div class="maintain_add">
			<p>
				<label><em class="xing"></em>uuid：</label>
				<form:input path="uuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>父级ID：</label>
				<form:input path="parentId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>父级UUID：</label>
				<form:input path="parentUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>级别：</label>
				<form:input path="level" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>中文名称：</label>
				<form:input path="nameCn" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>中文缩写：</label>
				<form:input path="nameShortCn" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>英文名称：</label>
				<form:input path="nameEn" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>英文缩写：</label>
				<form:input path="nameShortEn" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>拼音：</label>
				<form:input path="namePinyin" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>拼音缩写：</label>
				<form:input path="nameShortPinyin" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>交叉栏目：</label>
				<form:input path="crossSection" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>描述：</label>
				<form:input path="description" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>创建人：</label>
				<form:input path="createBy" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>创建时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${sysGeography.createDate}" pattern="yyyy-MM-dd" />" name="createDate" class="inputTxt dateinput" id="createDate" />
			</p>
			<p>
				<label><em class="xing"></em>修改人：</label>
				<form:input path="updateBy" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>修改时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${sysGeography.updateDate}" pattern="yyyy-MM-dd" />" name="updateDate" class="inputTxt dateinput" id="updateDate" />
			</p>
			<p>
				<label><em class="xing"></em>状态：</label>
				<form:input path="status" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>删除状态：</label>
				<form:input path="delFlag" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="submit" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
