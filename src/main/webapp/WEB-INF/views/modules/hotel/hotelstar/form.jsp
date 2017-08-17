<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>酒店星级信息</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

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
							url: "${ctx}/hotelStar/check?uuid="+$('#uuid').val()
								}
						},
					sort:{
						required:true,
						digits:true
					},
					dictUuid:{
						required:true
					} 
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/hotelStar/save";
					} else {
						url="${ctx}/hotelStar/update";
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
							$.jBox.tip(data.error,'warning');
							$("#btnSubmit").attr("disabled", false);
						}else{
							$.jBox.tip('系统异常，请重新操作!','warning');
							$("#btnSubmit").attr("disabled", false);
						}
					});
					
				},
				messages:{
					label:{remote:"名称已存在"},
				}
			});
			
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
		});
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">酒店星级信息</div>
	<form:form method="post" modelAttribute="hotelStar" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<div class="maintain_add">
			<p>
				<label><em class="xing">*</em>星级名称：</label>
				<form:input path="label" htmlEscape="false" maxlength="11" />
			</p>
			<p class="maintain_kong"></p>
            <p>
              <label><em class="xing">*</em>星级类型：</label>
              <form:select path="dictUuid" itemValue="key" itemLabel="value">
                  <form:option value="">请选择</form:option>
                  <c:forEach items="${hotelStarDicts}" var="hotelStarDict">
                  		<form:option value="${hotelStarDict.uuid }">${hotelStarDict.label}</form:option>
                  </c:forEach>
              </form:select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label>排序：</label>
				<form:input path="sort" type="text" maxlength="4" />
			</p>
			<p class="maintain_kong"></p>
			<p class="maintain_pfull">
				<label>描述：</label>
				<form:textarea class="madintain_text"  maxlength = "99" path="description" />
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
