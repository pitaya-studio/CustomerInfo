<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="base.jsp"%><content tag="three_level_menu">

	<li class="active">
		<c:choose>
			<c:when test="${flag==1}">
				<a href="${ctx}/hotelControl/tosavehotelcontrol">新增日期</a>
			</c:when>
			<c:otherwise>
				<a href="${ctx}/hotelControl/tosavehotelcontrol">新增控房</a>
			</c:otherwise>
		</c:choose>
	</li>
</content>