<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>系统游客类型信息</title>
	<meta name="decorator" content="wholesaler"/>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script> --%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				rules:{
					name:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/sysTravelerType/checkName?uuid="+$('#uuid').val()
								}
					sort:{
						required:true,
						digits:true
					},
					shortName:{
						required:true,
						alnum:[],
						remote: {
							type: "POST",
							url: "${ctx}/sysTravelerType/checkShortName?uuid="+$('#uuid').val()
								}
					} 
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/sysTravelerType/save";
					} else {
						url="${ctx}/sysTravelerType/update";
					}
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},900);
						}else if(data=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},900);
						}else{
							$.jBox.tip('操作异常！','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
				},
				messages:{
					name:{remote:"名称已存在"},
				}
			});
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
			jQuery.validator.addMethod("alnum", function(value, element){
				return this.optional(element) ||/^[a-zA-Z0-9]+$/.test(value);
				}, "只能输入字母和数字");
		});
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">系统游客类型信息</div>
	<form:form method="post" modelAttribute="sysTravelerTypeInput" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<%-- <tags:message content="${message}"/> --%>
		<div class="maintain_add">
			<%-- <p>
				<label><em class="xing"></em>uuid：</label>
				<form:input path="uuid" htmlEscape="false" maxlength="11" class="required" />
			</p> --%>
			<p>
				<label><em class="xing">*</em>排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label style="width: 110px;"><em class="xing">*</em>游客类型名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="25" class="required" />
			</p>
			<p>
				<label style="width: 110px;"><em class="xing">*</em>游客类型简写：</label>
				<form:input path="shortName" htmlEscape="false" maxlength="25" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>人员类型：</label>
				<select id="personType" name="personType">
					<option value="">请选择</option>
					<option value="0" <c:if test="${sysTravelerTypeInput.personType=='0' }">selected="selected" </c:if> >成人</option>
					<option value="1" <c:if test="${sysTravelerTypeInput.personType=='1' }">selected="selected" </c:if> >婴儿</option>
					<option value="2" <c:if test="${sysTravelerTypeInput.personType=='2' }">selected="selected" </c:if> >儿童</option>
				</select>
			</p>
			<%-- <p>
				<label><em class="xing"></em>创建人：</label>
				<form:input path="createBy" htmlEscape="false" maxlength="11" class="required" />
			</p> --%>
			<%-- <p>
				<label><em class="xing"></em>创建时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${sysTravelerType.createDate}" pattern="yyyy-MM-dd" />" name="createDate" class="inputTxt dateinput" id="createDate" />
			</p> --%>
			<%-- <p>
				<label><em class="xing"></em>修改人：</label>
				<form:input path="updateBy" htmlEscape="false" maxlength="11" class="required" />
			</p> --%>
			<%-- <p>
				<label><em class="xing"></em>修改时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${sysTravelerType.updateDate}" pattern="yyyy-MM-dd" />" name="updateDate" class="inputTxt dateinput" id="updateDate" />
			</p> --%>
			<%-- <p>
				<label><em class="xing"></em>删除状态：</label>
				<form:input path="delFlag" htmlEscape="false" maxlength="11" class="required" />
			</p> --%>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="submit" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
