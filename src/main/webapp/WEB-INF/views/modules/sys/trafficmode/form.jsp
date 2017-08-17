<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基础信息维护-交通信息-添加交通方式</title>
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
	
	<!--基础信息维护模块的脚本-->
	<script type="text/javascript" src="${ctxStatic}/js/tmp.basicInfo.js"></script>
	<script type="text/javascript">
	$(document).ready(function() {
		$("#inputForm").validate({
			rules:{
				label:{
					required:true,
					remote: {
						type: "POST",
						url: "${ctx}/sys/CompanyDict/check?type="+$('#type').val()+"&id="+$('#id').val()+"&checked=label"
							}
					},
				sort:{
					required:true,
					digits:true
				}
			},
			messages:{
				label:{remote:"名称已存在"},
			}
		});
		
		if(!"${sysdefinedict.id}") {
			var typeValue = "";
			if("${param.type}"=="travel_type")
					typeValue = "旅游类型" ;
				else if("${param.type}"=="product_level")
					typeValue = "产品系列" ;
				else if("${param.type}"=="product_type")
					typeValue = "产品类型" ;
				else if("${param.type}"=="traffic_mode")
					typeValue = "交通方式" ;
			
				$("#description").val(typeValue);
		}
		
		if($("#id").val()=='') {
			$("#sort").val("50");
		}
	});
	</script>
</head>
<body>
     <!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">添加交通方式</div>
	<form:form method="post" modelAttribute="sysdefinedict" action="${ctx}/sys/CompanyDict/saveCompanyDict" class="form-horizontal" id="inputForm" >
		<input type="hidden" name="type" id="type" value="${param.type}"/>
        <input type="hidden" name="id" id="id" value="${sysdefinedict.id}"/>
		<div class="maintain_add">
			<p>
				<label><em class="xing">*</em>名称：</label>
				<form:input path="label" htmlEscape="false" maxlength="11" />
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label>排序：</label>
				<form:input path="sort" type="text" maxlength="4" />
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
				<label>描述：</label>
				<form:textarea class="madintain_text"  maxlength="99"  path="description" />
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
