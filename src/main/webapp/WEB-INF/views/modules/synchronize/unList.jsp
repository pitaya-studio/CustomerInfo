<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>正反向产品ID未存在关联日志</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		$(function(){
			$('#un').attr('class', 'active');
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<!-- <ul class="nav nav-tabs">
		<li><a href="${ctx}/synchronizeExceptionLog/manage/notSolved">待解决异常团期同步</a></li>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeExceptionLog/manage/solved">已解决异常团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeSuccessLog/manage/success">成功团期同步</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li><a href="${ctx}/synchronizeUnPriceLog/manage/unPrice">成人价格不存在异常</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:view"><li class="active"><a href="${ctx}/synchronizeUnLog/manage/un">正反向产品ID未存在关联日志</a></li></shiro:hasPermission>
		<shiro:hasPermission name="synchronize:config"><li><a href="${ctx}/mailConfig/manage/config">邮件发送设置</a></li></shiro:hasPermission>
	</ul> -->
	<%@ include file="header.jsp" %>
	<form:form id="searchForm" modelAttribute="unSynchronizeLog" action="${ctx}/synchronizeUnLog/manage/un" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="state" name="state" type="hidden" value="2"/>
		<label>类型：</label>
		<form:select id="type" path="type" class="input-medium">
			<form:option value="" label=""/>
			<form:option value="1" label="添加"/>
			<form:option value="2" label="修改"/>
			<form:option value="3" label="删除"/>
		</form:select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>类型</th><th>产品ID</th><th>团期ID</th><th>产生时间</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="synchronize">
			<tr>
				<td>
					<c:if test="${synchronize.type==1}">
						添加
					</c:if>
					<c:if test="${synchronize.type==2}">
						修改
					</c:if>
					<c:if test="${synchronize.type==3}">
						删除
					</c:if>
				</td>
				<td>${synchronize.idTrekizwholesaler}</td>
				<td>${synchronize.groupIdTrekizwholesaler}</td>
				<td>
					<fmt:formatDate value="${synchronize.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>