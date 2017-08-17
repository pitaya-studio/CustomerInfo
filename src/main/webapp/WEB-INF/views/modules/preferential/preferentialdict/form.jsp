<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>字典信息维护</title>
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
							url: "${ctx}/preferentialDict/check?type="+$('#type').val()+"&uuid="+$('#uuid').val()
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
						url="${ctx}/preferentialDict/save";
					} else {
						url="${ctx}/preferentialDict/update";
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
			
			if("${preferentialDictInput.logicOperationUuid}"){
				$("select[name=logicOperationUuid]").val("${preferentialDictInput.logicOperationUuid}");
			}
			var unitUuids = "${preferentialDictInput.unitUuidsString}";
			$("input[name=unitUuids]").each(function(){
				if(unitUuids.indexOf($(this).val())>-1){
					$(this).attr("checked","true");
				}
			});
		});
		
		//逻辑规则添加
		function addLogic(obj){
			if($(obj).find('option:last').attr('selected') != 'selected') {
				return ;
			}
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px"><em class="xing">*</em>逻辑名称：</label><input type="text" id="name" name="name"/></p>'+
	           '<p><label style="width:90px"><em class="xing">*</em>执行类：</label><input type="text" maxlength="99" id="runClass" name="runClass"/></p>'+
			   '<p><label style="width:90px"><em class="xing">*</em>执行方法：</label><input type="text" maxlength="99" id="runMethod" name="runMethod"/></p>'+
	           '</div>';
			$.jBox(html, { title: "添加逻辑规则", submit: function (v, h, f) {
			    if (f.name == '') {
			        $.jBox.tip("请输入逻辑名称。", 'error', { focusId: "name" }); 
			        return false;
			    }
			    if (f.runClass == '') {
			        $.jBox.tip("请输入执行类。", 'error', { focusId: "runClass" }); 
			        return false;
			    }
			    if (f.runMethod == '') {
			        $.jBox.tip("请输入执行方法。", 'error', { focusId: "runMethod" }); 
			        return false;
			    }
			    $.post("${ctx}/preferentialLogicOperation/save", {"name":f.name,"runClass":f.runClass,"runMethod":f.runMethod},
					function(data){
						if(data.result=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).find('option:selected').before('<option value="'+data.uuid+'">'+f.name+'</option>');
							$(obj).find('option:selected').prev().prop("selected",true);
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:300});
		}
		//单位添加
		function addUnit(obj){
			var html = '<div class="jbox_type">'+
			   '<p><label style="width:90px"><em class="xing">*</em>名称：</label><input type="text" id="name" name="name"/></p>'+
			   '<p><label style="width:90px"><em class="xing">*</em>排序：</label><input type="text" maxlength="4" id="sort" name="sort"/></p>'+
			   '<p><label style="width:90px">描述：</label><textarea id="description" maxlength="99" name="description" ></textarea></p>'+
	           '</div>';
			$.jBox(html, { title: "添加单位", submit: function (v, h, f) {
			    if (f.name == '') {
			        $.jBox.tip("请输入单位。", 'error', { focusId: "name" }); 
			        return false;
			    }
			    if (f.sort == '') {
			        $.jBox.tip("请输入单位的排序。", 'error', { focusId: "sort" }); 
			        return false;
			    }
			    var reg = new RegExp("^[0-9]*$");
				if(!reg.test(f.sort)) {
					$.jBox.tip("单位的排序只能输入整数。", 'error', { focusId: "sort" }); 
					return false;
				} 
				if (f.sort == '') {
			        $.jBox.tip("请输入单位的排序。", 'error', { focusId: "sort" }); 
			        return false;
			    }
			    if (f.description.length>=30) {
			        $.jBox.tip("单位的描述不能大于30字数。", 'error', { focusId: "description" }); 
			        return false;
			    }
			    
			    $.post("${ctx}/preferentialUnit/save", 
			    	{
			    	"name":f.name,
			    	"sort":f.sort,
			    	"description":f.description
			    	},
					function(data){
						if(data=="1"){
							top.$.jBox.tip('添加成功','success');
							$(obj).before('<label><input type="checkbox" name="unitUuid"  value="'+data.uuid+'">'+f.name+'</label>');
							
							return true;
						}else{
							top.$.jBox.tip(data.message,'warning');
							return false;
						}
					}
				);
			} ,height:330});
		}
	</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbz_tit pl20">字典信息维护</div>
	<form:form method="post" modelAttribute="preferentialDictInput" action="" class="form-horizontal" id="inputForm" novalidate="">
		<form:hidden path="uuid" />
		<div class="maintain_add">
			<p>
				<label><em class="xing"></em>字典名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="11" class="required" />
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing"></em>数据类型：</label>
				<form:select path="dataType" class="required">
					<form:option value="">请选择</form:option>
					<form:option value="0">数值</form:option>
					<form:option value="1">日期</form:option>
				</form:select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing"></em>因果类型：</label>
				<form:select path="type" class="required">
					<form:option value="">请选择</form:option>
					<form:option value="0">因</form:option>
					<form:option value="1">果</form:option>
				</form:select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing"></em>关系运算符</label>
				<form:select path="relationalOperator" class="required">
					<form:option value="">请选择</form:option>
					<form:option value="0">&gt;</form:option>
					<form:option value="1">&ge;</form:option>
					<form:option value="2">&lt;</form:option>
					<form:option value="3">&le;</form:option>
					<form:option value="4">=</form:option>
					<form:option value="5">!=</form:option>
					<form:option value="6">between</form:option>
				</form:select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing"></em>逻辑运算配置：</label>
				<select name="logicOperationUuid" class="required" onchange="addLogic(this);">
						<option value="" >请选择</option>
						<c:forEach items="${logicList }" var="entry">
							<option value="${entry.uuid }">${entry.name }</option>
						</c:forEach>
						<option value="">+添加</option>
				</select>
			</p>
			<p class="maintain_kong"></p>
			<p>
				<label><em class="xing"></em>单位：</label>
				<c:forEach items="${unitList }" var="entry">
					<input type="checkbox" name="unitUuids" value="${entry.uuid }" />${entry.name }&nbsp;
				</c:forEach>
				<span  class="host-check-add" style="float:right" onclick="addUnit(this)">+ 添 加</span>
			</p>
			
			<p class="maintain_kong"></p>
			<p class="maintain_btn">
				<label>&nbsp;</label> <input type="button" value="关&nbsp;&nbsp;&nbsp;闭" class="btn btn-primary gray" onclick="window.close();" />
				<input type="submit" value="提&nbsp;&nbsp;&nbsp;交" class="btn btn-primary" id="btnSubmit" />
			</p>
		</div>
	</form:form>
	<!--右侧内容部分结束-->
</body>
</html>
