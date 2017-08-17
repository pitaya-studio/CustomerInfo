<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>部门管理</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
			jQuery.validator.addMethod("isEnglish", function(value, element) {       
		         return this.optional(element) || /^[A-Za-z]+$/.test(value);       
		    }, "只能包含英文字符"); 
			$("#inputForm").validate({
				rules:{
					code:{
						isEnglish:true
					},
					name:{
						required:true
					}
				},
				submitHandler: function(form) {
					var selectIds = [], nodes = selectTree.getCheckedNodes(true);
					for(var i=0; i<nodes.length; i++) {
						selectIds.push(nodes[i].id);
					}
					$("#selectMenuIds").val(selectIds);

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
			
			//默认为不启用公告板
			var obj = $("input[name='announcement']:checked");
			if(!obj || obj.length == 0) {
				$("input[name='announcement'][value='0']").prop("checked",true);
			}

			var setting = {check:{enable:true,nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};

			// 部门查看菜单
			var selectNodes=[
					<c:forEach items="${selectMenuList}" var="menu">{id:${menu.id}, pId:${not empty menu.parent.id?menu.parent.id:0}, name:"${not empty menu.parent.id?menu.name:'权限列表'}"},
		            </c:forEach>];
			// 初始化树结构
			var selectTree = $.fn.zTree.init($("#selectMenuTree"), setting, selectNodes);
			// 默认选择节点
			var selectIds = "${department.selectMenuIds}".split(",");
			for(var i=0; i<selectIds.length; i++) {
				var node = selectTree.getNodeByParam("id", selectIds[i]);
				try{selectTree.checkNode(node, true, false);}catch(e){}
			}
			// 默认不展开全部节点
			selectTree.expandAll(false);
		});

	</script>
</head>
<body>
	<page:applyDecorator name="department_op_head">
        <page:param name="current">departmentForm</page:param>
    </page:applyDecorator><br/>
	<form:form id="inputForm" modelAttribute="department" action="${ctx}/sys/department/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">上级部门:</label>
			<div class="controls">
                <tags:treeselect id="department" name="parent.id" value="${department.parent.id}" labelName="parent.name" labelValue="${department.parent.name}"
					title="部门" url="/sys/department/treeData" extId="${department.id}" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font style="color:#ff0000; padding-right:5px;">*</font>中文名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">英文名称:</label>
			<div class="controls">
				<form:input path="nameEn" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门所在城市:</label>
			<div class="controls">
				<form:input path="city" htmlEscape="false" maxlength="2"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label"><font style="color:#ff0000; padding-right:5px;">*</font>部门编码:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="2" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否启用公告板:</label>
			<div class="controls">
				<form:radiobutton path="announcement" value="1"/>是
				<form:radiobutton path="announcement" value="0"/>否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门人员相互查看授权:</label>
			<div class="controls">
				<div id="selectMenuTree" class="ztree" style="margin-top:3px;"></div>
				<form:hidden path="selectMenuIds"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="5" class="required digits"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">部门描述:</label>
			<div class="controls">
				<form:textarea path="description" htmlEscape="false"/>
			</div>
		</div>
		<div class="form-actions1">
			<shiro:hasPermission name="sys:department:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn gray" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>