<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>部门管理</title>
	<meta name="decorator" content="wholesaler"/>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<style type="text/css">.table td i{margin:0 2px;}</style>
	<script type="text/javascript">
		$(document).ready(function() {
			var msgContent = $("#msgContent").val();
			if(msgContent != "") {
	            top.$.jBox.tip(msgContent,'warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
                $("#msgContent").val('');
			}
			$("#treeTable").treeTable({expandLevel : 3});
		});
    	function updateSort() {
			loading('正在提交，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/sys/department/updateSort");
	    	$("#listForm").submit();
    	}
    	function addParent() {
    		loading('正在添加，请稍等...');
	    	$("#listForm").attr("action", "${ctx}/sys/department/addParent");
	    	$("#listForm").submit();
        }

    	//添加部门
        function addDepartment() {
        	$.ajax({
                type: "POST",
                url: "${ctx}/sys/department/checkParent",
                data: {},
                success: function(msg) {
                    if(msg == 'error') {
                    	top.$.jBox.tip('请先添加顶级部门','warning');
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                        return false;
                    } else {
                    	window.location.href = "${ctx}/sys/department/form";
                    }
                }
            });
        }
	</script>
</head>
<body>
	<page:applyDecorator name="department_op_head" >
		<page:param name="current">departmentList</page:param>
	</page:applyDecorator>
	<input id="msgContent" type="hidden" value="${message}">
	
	<form id="listForm" method="post">
		<c:choose>
			<c:when test="${not empty list}">
				<table id="treeTable" class="table table-striped table-bordered">
					<tr><th>中文名称</th><th>英文名称</th><th>城市缩写</th><th>部门编码</th><th>是否启用公告板</th><th style="text-align:center;">排序</th><shiro:hasPermission name="sys:department:edit"><th>操作</th></shiro:hasPermission></tr>
						<c:set var="isParent"></c:set>
						<c:forEach items="${list}" var="department">
							<c:choose>
								<c:when test="${empty department.parent.id || department.parent.id eq '0'}">
									<c:set var="isParent" value="yes"></c:set>
								</c:when>
								<c:otherwise>
									<c:set var="isParent" value="no"></c:set>
								</c:otherwise>
							</c:choose>
							<tr id="${department.id}" pId="${department.parent.id ne '0' ? department.parent.id:'0'}">
								<td>
									<c:choose>
										<c:when test="${isParent == 'yes'}">
											${department.name}
										</c:when>
										<c:otherwise>
											<a href="${ctx}/sys/department/form?id=${department.id}">${department.name}</a>
										</c:otherwise>
									</c:choose>
								</td>
								<td><a href="${ctx}/sys/department/form?id=${department.id}">${department.nameEn}</a></td>
								<td>${department.city}</td>
								<td>${department.code}</td>
								<td>
									<c:choose>
										<c:when test="${department.announcement==1}">
											是
										</c:when>
										<c:otherwise>
											否
										</c:otherwise>
									</c:choose>
								</td>
								<td style="text-align:center;">
									<shiro:hasPermission name="sys:department:edit">
										<input type="hidden" name="ids" value="${department.id}" <c:if test="${isParent == 'yes'}">disabled="disabled"</c:if>/>
										<input name="sorts" type="text" value="${department.sort}" style="width:50px;margin:0;padding:0;text-align:center;" <c:if test="${empty department.parent.id || department.parent.id eq '0'}">
										disabled="disabled"
									</c:if>>
									</shiro:hasPermission>
									<shiro:lacksPermission name="sys:department:edit">
										${department.sort}
									</shiro:lacksPermission>
								</td>
								<shiro:hasPermission name="sys:department:edit">
									<td>
										<c:if test="${isParent != 'yes'}">
											<a href="${ctx}/sys/department/form?id=${department.id}">修改</a>
											<a href="${ctx}/sys/department/delete?id=${department.id}" onclick="return confirmx('要删除该部门及所有子部门项吗？', this.href)">删除</a>
										</c:if>
										<a href="${ctx}/sys/department/form?parent.id=${department.id}">添加下级部门</a> 
									</td>
								</shiro:hasPermission>
							</tr>
						</c:forEach>
				</table>
				<shiro:hasPermission name="sys:department:edit"><div class="form-actions pagination-left">
					<input id="btnSubmit" class="btn btn-primary" type="button" value="保存排序" onclick="updateSort();"/>
				</div></shiro:hasPermission>
			</c:when>
			<c:otherwise>
				<input id="btnSubmit" class="btn btn-primary" type="button" value="添加顶级部门" onclick="addParent();"/>
			</c:otherwise>
		</c:choose>
	</form>
</body>
</html>
