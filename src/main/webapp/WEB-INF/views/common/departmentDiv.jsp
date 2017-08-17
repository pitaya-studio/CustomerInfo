<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 部门分区 -->
<div class="supplierLine" <c:if test="${empty showAreaList || fn:length(showAreaList) == 1}">style="display: none;"</c:if>>
	<c:forEach var="department" items="${showAreaList}" varStatus="status">
		<c:choose>
			<c:when test="${(department.id == departmentId) || (empty departmentId && status.index == 0)}">
				<a class="select" href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
			</c:when>
			<c:otherwise>
				<a href="javascript:void(0)" onclick="getDepartment('${department.id}');">${department.name}</a>
			</c:otherwise>
		</c:choose>

	</c:forEach>
</div>
