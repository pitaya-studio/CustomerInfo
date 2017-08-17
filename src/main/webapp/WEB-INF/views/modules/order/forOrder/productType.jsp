<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 产品系列列表 -->
	<c:if test="${not empty productType}">
		<div class="supplierLine" style="margin-bottom:10px;">
		<a onclick="productType('');" href="javascript:void(0)" name="productTypeHref">全部</a>
		<c:forEach var="type" items="${fn:split(productType, ',')}">
			<a onclick="productType('${type}');" href="javascript:void(0)" name="productTypeHref">${type}</a>
		</c:forEach>
		</div>
	</c:if>

