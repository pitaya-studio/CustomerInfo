<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>酒店餐型信息</title>
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
					mealName:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/hotelMeal/check/${hotelMeal.hotelUuid}?uuid="+$('#uuid').val()
								}
						},
					
					sort:{
						required:true,
						digits:true
					}
				},
				submitHandler: function(form){
					var url = "";
					if($("#uuid").val()=='') {
						url="${ctx}/hotelMeal/save";
					} else {
						url="${ctx}/hotelMeal/update";
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
					mealName:{remote:"名称已存在"}
				}
			});
			
			if('${message}' != '') {
				var message = '${message}';
				if(message == "1") {
					$("#searchForm",window.opener.document).submit();
					$.jBox.tip("保存成功!");
					setTimeout(function(){window.close();},500);
				} else if(message == "2") {
					$("#searchForm",window.opener.document).submit();
					$.jBox.tip("修改成功!");
					setTimeout(function(){window.close();},500);
				} else if(message == "3") {
					$.jBox.tip('${error}','warning');
					$("#btnSubmit").attr("disabled", false);
				}
			}
			
			if($("#uuid").val()=='') {
				$("#sort").val("50");
			}
		});
		
		function submitForm(){
			$("#inputForm").submit();
		}
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">酒店餐型信息</div>
	<form:form method="post" modelAttribute="hotelMeal" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<form:hidden path="hotelUuid" />
		<div class="maintain_add"> 
            <p>
              <label><em class="xing">*</em>餐型名称：</label>
			  <form:input path="mealName" htmlEscape="false" maxlength="50"  class="required"/>
			</p>
            <p>
              <label>类型：</label>
              <trekiz:defineDict name="mealType" type="hotel_meal_type" defaultValue="${hotelMeal.mealType }" />
			</p>
            <p class="maintain_kong"></p>
            <p>
              <label>适用人数：</label>
				<form:input path="suitableNum" htmlEscape="false" maxlength="11" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');" />
			</p>
            <p>
              <label>价格：</label>
				<form:input path="price" htmlEscape="false" maxlength="11" class="number" />
			</p>
            <p class="maintain_kong"></p>
            <p>
              <label>排序：</label>
				<form:input path="sort" htmlEscape="false" maxlength="4" onblur="this.value=this.value.replace(/\D/g,'');"  onkeyup="this.value=this.value.replace(/\D/g,'');"   onafterpaste="this.value=this.value.replace(/\D/g,'');"/>
			</p>
            <p class="maintain_kong"></p>
            <p class="maintain_pfull">
              <label>餐型描述：</label>
              <form:textarea class="madintain_text" path="mealDescription"></form:textarea>
			</p>
            <p class="maintain_btn">
              <label>&nbsp;</label>
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input  value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" onclick="submitForm();"/>
			</p>    
          </div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
