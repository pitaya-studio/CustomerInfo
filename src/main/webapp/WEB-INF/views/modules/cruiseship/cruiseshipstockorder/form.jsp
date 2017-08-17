<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>CruiseshipStockOrder信息</title>
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
					label:{
						required:true,
						remote: {
							type: "POST",
							url: "${ctx}/cruiseshipStockOrder/check?type="+$('#type').val()+"&uuid="+$('#uuid').val()
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
						url="${ctx}/cruiseshipStockOrder/save";
					} else {
						url="${ctx}/cruiseshipStockOrder/update";
					}
					
					$("#btnSubmit").attr("disabled", "disabled");
					$.post(url,$("#inputForm").serialize(),function(data){
						if(data=="1"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("保存成功!");
							setTimeout(function(){window.close();},500);
						}else if(data=="2"){
							$("#searchForm",window.opener.document).submit(); 
							$.jBox.tip("修改成功!");
							setTimeout(function(){window.close();},500);
						}else{
							$.jBox.tip('操作异常！','warning');
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
	<div class="ydbz_tit pl20">CruiseshipStockOrder信息</div>
	<form:form method="post" modelAttribute="cruiseshipStockOrderInput" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<div class="maintain_add">
			<p>
				<label><em class="xing"></em>uuid：</label>
				<form:input path="uuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>产品表ID：</label>
				<form:input path="activityId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>产品团期表类型（1：activitygroup表；）：</label>
				<form:input path="activityType" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>产品名称：</label>
				<form:input path="activityName" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>产品出发地ID：</label>
				<form:input path="departureCityId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>产品出发地名称：</label>
				<form:input path="departureCityName" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>库存UUID：</label>
				<form:input path="cruiseshipStockUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>库存明细UUID：</label>
				<form:input path="cruiseshipStockDetailUuid" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>舱位名称：</label>
				<form:input path="cruiseshipCabinName" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>性别（女：F，男：M）：</label>
				<form:input path="sex" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>女人数：</label>
				<form:input path="fnum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>男人数：</label>
				<form:input path="mnum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>女拼（拼：0；不拼：1；）：</label>
				<form:input path="fpiece" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>男拼（拼：0；不拼：1；）：</label>
				<form:input path="mpiece" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>总人数：</label>
				<form:input path="allNum" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>批发商id：</label>
				<form:input path="wholesalerId" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>创建人：</label>
				<form:input path="createBy" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>创建时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${cruiseshipStockOrder.createDate}" pattern="yyyy-MM-dd" />" name="createDate" class="inputTxt dateinput" id="createDate" />
			</p>
			<p>
				<label><em class="xing"></em>修改人：</label>
				<form:input path="updateBy" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p>
				<label><em class="xing"></em>修改时间：</label>
				<input readonly onclick="WdatePicker()" value="<fmt:formatDate value="${cruiseshipStockOrder.updateDate}" pattern="yyyy-MM-dd" />" name="updateDate" class="inputTxt dateinput" id="updateDate" />
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
