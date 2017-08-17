<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>角色管理</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treeview.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					var ids = [], nodes = tree.getCheckedNodes(true);
					for(var i=0; i<nodes.length; i++) {
						ids.push(nodes[i].id);
					}
					$("#menuIds").val(ids);
					var ids2 = [], nodes2 = tree2.getCheckedNodes(true);
					for(var i=0; i<nodes2.length; i++) {
						ids2.push(nodes2[i].id);
					}
					$("#officeIds").val(ids2);
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
			var setting = {check:{enable:true,chkboxType : { "Y" : "", "N" : "" },nocheckInherit:true},view:{selectedMulti:false},
					data:{simpleData:{enable:true}},callback:{beforeClick:function(id, node){
						tree.checkNode(node, !node.checked, true, true);
						return false;
					}}};
			
			// 用户-菜单
			var zNodes=[
					<c:forEach items="${menuList}" var="menu">{id:${menu.id}, pId:${not empty menu.parent.id?menu.parent.id:0}, name:"${not empty menu.parent.id?menu.name:'权限列表'}"},
		            </c:forEach>];
			// 初始化树结构
			var tree = $.fn.zTree.init($("#menuTree"), setting, zNodes);
			// 默认选择节点
			var ids = "${role.menuIds}".split(",");
			for(var i=0; i<ids.length; i++) {
				var node = tree.getNodeByParam("id", ids[i]);
				try{tree.checkNode(node, true, false);}catch(e){}
			}
			// 默认展开全部节点
			tree.expandAll(false);
			
			$("#userType").change(function(){
				if($('#userType').val()!='') {
					$('#companyId').parent().parent().show();
					if($('#userType').val()=='1') {
						$('#roleType').parent().parent().hide();
					}else if($('#userType').val()=='3') {
						$('#roleType').parent().parent().show();
					}
				}else{
					$('#companyId').parent().parent().hide();
					$('#roleType').parent().parent().hide();
				}
			});
			
		});
	</script>
</head>
<body>
    <content tag="three_level_menu">
		<li><a href="${ctx}/sys/role/">角色列表</a></li>
		<li class="active"><a href="${ctx}/sys/role/form?id=${role.id}">角色<shiro:hasPermission name="sys:role:edit">${not empty role.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:role:edit">查看</shiro:lacksPermission></a></li>
	</content><br/>
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/sys/role/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">角色名称:</label>
			<div class="controls">
				<input id="oldName" name="oldName" type="hidden" value="${role.name}">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
        <div class="control-group">
            <label class="control-label">用户类型:</label>
            <div class="controls">
               <form:select path="userType" class="required" >
               <form:option value="" label="请选择"/>
               <form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
               </form:select>
            </div>
        </div>
        <div class="control-group" style="<c:if test="${empty role.companyId}">display: none</c:if>">
        	<label class="control-label">所属批发商:</label>
            <div class="controls">
            	<form:select path="companyId" class="required">
            		<form:options items="${officeMapList }" itemLabel="name" itemValue="id" htmlEscape="false"/>
            	</form:select>
            </div>
        </div>
        
        <div class="control-group" style="<c:if test="${empty role.roleType}">display: none</c:if>">
        	<label class="control-label">角色类型:</label>
            <div class="controls">
            	<form:select path="roleType" class="required">
            		<c:forEach items="${roleType }" var="roleType">
            			<form:option value="${roleType.key }">${roleType.value }</form:option>
            		</c:forEach>
            	</form:select>
            </div>
        </div>
        
               <div class="control-group">
			<label class="control-label">所属部门:</label>
			<div class="controls">
				<div class="input-append">
					<input id="departmentId" name="departmentId" type="hidden" value="${role.department.id}" class="required">
					<input id="departmentName" name="departmentName" readonly="readonly" type="text" value="${role.department.name}" style="" class="required valid">
					<a id="departmentButton" href="javascript:" class="btn " style="_padding-top:6px;">&nbsp;选择&nbsp;</a>
				</div>
				<script type="text/javascript">
					$("#departmentButton").click(function() {
						var flag = $("#companyId").parent().parent().is(":visible");
						if(!flag) {
							top.$.jBox.info("请先选择批发商", "警告");
		   	                top.$('.jbox-body .jbox-icon').css('top','55px');
		   	                return false;
						}
						// 是否限制选择，如果限制，设置为disabled
						if ("" == "disabled") {
							return true;
						}
						var url = "/sys/department/treeData?officeId=" + $("#companyId").val();
						// 正常打开	
						top.$.jBox.open("iframe:${ctx}/tag/treeselect?url="+encodeURIComponent(url)+"&module=&checked=&extId=&selectIds="+$("#departmentId").val(), "选择部门", 300, 420,{
							buttons:{"确定":"ok", "关闭":true}, submit:function(v, h, f){
								if (v=="ok"){
									var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
									var ids = [], names = [], nodes = [];
									if ("" == "true"){
										nodes = tree.getCheckedNodes(true);
									}else{
										nodes = tree.getSelectedNodes();
									}
									for(var i=0; i<nodes.length; i++) {//
										ids.push(nodes[i].id);
										names.push(nodes[i].name);//
										break; // 如果为非复选框选择，则返回第一个选择  
									}
									$("#departmentId").val(ids);
									$("#departmentName").val(names);
									$("#departmentName").focus();
									$("#departmentName").blur();
								}//
							}, loaded:function(h){
								$(".jbox-content", top.document).css("overflow-y","hidden");
							},persistent:true
						});
					});
				</script>
			</div>
		</div>
        <div class="control-group">
			<label class="control-label">是否可操作:</label>
			<div class="controls">
				<form:radiobutton path="isOperational" value="1"/>是
				<form:radiobutton path="isOperational" value="0"/>否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">角色授权:</label>
			<div class="controls">
				<div id="menuTree" class="ztree" style="margin-top:3px;"></div>
				<form:hidden path="menuIds"/>
			</div>
		</div>
		<div class="form-actions1">
			<shiro:hasPermission name="sys:role:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onClick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>