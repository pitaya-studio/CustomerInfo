<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>基础信息维护-酒店住客类型管理-添加住客类型</title>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<%@include file="/WEB-INF/views/include/treetable.jsp"%>
	rel="stylesheet" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ctxStatic}/js/default.validator.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--基础信息维护模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
<script type="text/javascript">
	$(document).ready(function()
	{
		$("#inputForm").validate(
		{
			rules : {
				name : {
					required : true,
					remote : {
						type : "POST",
						url : "${ctx}/hotelGuestType/check?uuid="+ $('#uuid').val()
					}
				},
				sysGuestType : {
					required : true,
					remote: {
						type : "POST",
						url : "${ctx}/hotelGuestType/validateTravelerType?uuid="+ $('#uuid').val()
					}
				},
				sort : {
					required : true,
					digits : true
				}
			},
			submitHandler : function(form) {
				var url = "";
				if ($("#uuid").val() == '') {
					url = "${ctx}/hotelGuestType/save";
				} else {
					url = "${ctx}/hotelGuestType/update";
				}
				
				$("#btnSubmit").attr("disabled", "disabled");
				$.post(url, $("#inputForm").serialize(),
				function(data) {
					if (data.message == "1") {
						$("#searchForm",window.opener.document).submit();
						$.jBox.tip("保存成功!");
						setTimeout(function() {window.close();}, 500);
					} else if (data.message == "2") {
						$("#searchForm", window.opener.document).submit();
						$.jBox.tip("修改成功!");
						setTimeout(function() {	window.close();}, 500);
					} else if (data.message == "3") {
						$.jBox.tip(data.error, 'warning');
						$("#btnSubmit").attr("disabled",false);
					} else {
						$.jBox.tip('系统异常，请重新操作!', 'warning');
						$("#btnSubmit").attr( "disabled", false);
					}
				});
			},
			messages : {
				name : {remote : "名称已存在"},
				sysGuestType:{remote:"系统已保存此住客类型!"}
			}
		});

		if ($("#uuid").val() == '') {
			$("#sort").val("50");
		}
	});
</script>

</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">住客类型信息</div>
	<form:form method="post" modelAttribute="hotelGuestType" action=""
		class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<div class="maintain_add">
			<style>
.maintain_add p {
	width: 30%;
}

.maintain_add p label {
	width: 150px;
}
</style>
			<p>
				<label><span class="xing">*</span>住客类型名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="20" cssStyle="width:150px;"
					class="required" />
			</p>
			<p>
				<label for="status"><input id="status" type="checkbox"
					name="status"
					<c:if test="${hotelGuestType.status == 1 }">checked="checked"</c:if>
					value="1" />启用</label>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><span class="xing">*</span>对应系统的住客类型： </label>
				<form:select path="sysGuestType" class="required">
					<option value="">请选择</option>
					<c:forEach items="${typeList }" var="tl">
						<option value="${tl.uuid }"
							<c:if test="${hotelGuestType.sysGuestType ==tl.uuid }">selected="selected"</c:if>>${tl.name }</option>
					</c:forEach>
				</form:select>
			</p>
			<p class="maintain_kong"></p>
			<!-- 2015.08.10 去掉使用范围 1.6版本
			<p>
            	<label><span class="xing">*</span>适用范围：</label>
	            <form:select path="useRange" class="required">
		            <form:option value="">请选择</form:option>
		            <form:option value="1">酒店房型</form:option>
		            <form:option value="2">酒店餐型</form:option>
	            </form:select>
			</p>
			 -->
			<p>
				<label>排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="4"
					class="required" />
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
				<label>描述：</label>
				<form:textarea path="description" maxlength="99"
					class="madintain_text" />
			</p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button"
					value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray"
					onclick="window.close();" /> <input type="submit"
					value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
