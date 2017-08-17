<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>模板维护</title>
	<meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/js/default.validator.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#inputForm").validate({
				rules:{
					
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/preferentialTemplates/save";
					} else {
						url="${ctx}/preferentialTemplates/update";
					}
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data.message=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="2"){
							$("#searchForm",window.opener.document).submit();
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else if(data.message=="3"){
							$.jBox.tip(data.error,"warning");
							$("#btnSubmit").attr("disabled", false);
						}else{
							$.jBox.tip("操作异常!","warning");
							$("#btnSubmit").attr("disabled", false);
						}
					});
				}
			});
			
		});
			
		function dictChange(selectType) {
			var dictUuid = $("#"+selectType+"DictUuid").val();
			
			$.post("${ctx}/preferentialTemplates/getUnitsByDictUuid", {"uuid":dictUuid}, 
				function(data){
					if(data.units) {
						$("#"+selectType+"UnitUuid").empty();
						$("#"+selectType+"UnitUuid").append("<option value=''>请选择</option>");
						$.each(data.units,function(i,n){
			   				$("#"+selectType+"UnitUuid").append($("<option/>").text(n.unitName).attr("value",n.unitUuid));
			   			});
					}
				}
			);
		}
		
		function unitChange(selectType) {
			var dictUuid = $("#"+selectType+"DictUuid").val();
			var unitUuid = $("#"+selectType+"UnitUuid").val();
			
			$.post("${ctx}/preferentialTemplates/getRelDesc", {"dictUuid":dictUuid, "unitUuid":unitUuid}, 
				function(data){
					if(data.desc) {
						$("#"+selectType+"Desc").text(data.desc);
					} else {
						$("#"+selectType+"Desc").text('');
					}
				}
			);
		}
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">模板维护</div>
	<form:form method="post" modelAttribute="preferentialTemplates" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<div class="maintain_add">
			<p>
				<label><em class="xing">*</em>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="30" class="required" />
			</p>
			<p class="maintain_kong"></p>
			<p style="width: 600px;">
				<label><em class="xing"></em>选择字典：</label>&nbsp;&nbsp;&nbsp;
				因：<select id="causeDictUuid" name="causeDictUuid" onchange="dictChange('cause');" class="required">
						<option value="">请选择</option>
						<c:forEach items="${causeDictList }" var="entity">
							<option value="${entity.dictUuid}" <c:if test="${entity.dictUuid == causeDictDefault}">selected="selected"</c:if>>${entity.dictName}</option>
						</c:forEach>
					</select>&nbsp;&nbsp;&nbsp;
					<select id="causeUnitUuid" name="causeUnitUuid" onchange="unitChange('cause');" class="required">
						<option value="">请选择</option>
						<c:forEach items="${causeUnits }" var="entity">
							<option value="${entity.unitUuid}" <c:if test="${entity.unitUuid == causeUnitDefault}">selected="selected"</c:if>>${entity.unitName}</option>
						</c:forEach>
					</select>
					<br/>
					<label></label>&nbsp;&nbsp;&nbsp;
				果：<select id="effectDictUuid" name="effectDictUuid" onchange="dictChange('effect');" class="required">
						<option value="">请选择</option>
						<c:forEach items="${effectDictList }" var="entity">
							<option value="${entity.dictUuid}" <c:if test="${entity.dictUuid == effectDictDefault}">selected="selected"</c:if>>${entity.dictName}</option>
						</c:forEach>
					</select>&nbsp;&nbsp;&nbsp;
					<select id="effectUnitUuid" name="effectUnitUuid" onchange="unitChange('effect');" class="required">
						<option value="">请选择</option>
						<c:forEach items="${effectUnits }" var="entity">
							<option value="${entity.unitUuid}" <c:if test="${entity.unitUuid == effectUnitDefault}">selected="selected"</c:if>>${entity.unitName}</option>
						</c:forEach>
					</select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label></label>
				<span id="causeDesc">${causeDesc }</span>
				<span id="effectDesc">${effectDesc }</span>
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
