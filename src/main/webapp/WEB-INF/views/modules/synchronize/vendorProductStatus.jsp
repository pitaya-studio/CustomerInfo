<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>同步异常信息管理</title>
	<meta name="decorator" content="wholesaler"/>
	<script type="text/javascript">
		$(function(){
			$('#check').attr('class', 'active');
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
	    	return false;
	    }
		var resync = function(id){
			$.ajax({
			    type:"POST",
			    url:"${ctx}/vedorSynchronizeStatus/resync",
			    data:{
			        activityId : id
			    },
			    success:function(msg) {
			    	$.jBox.info(msg, '系统提示');
			    }
			});
		} 
	</script>
</head>
<body>
	<%@ include file="header.jsp" %>
	<form id="searchForm" action="${ctx}/vedorSynchronizeStatus" method="get" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<label>批发商：</label>
		<select name="company" >
			<c:forEach items="${companyList}" var="company">
				<c:choose>
					<c:when test="${companyId == company.id}">
						<option selected="selected" value="${company.id}">${company.name}</option>
					</c:when>
					<c:otherwise>
						<option value="${company.id}">${company.name}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<select name="isSuccess" >
			<c:choose>
				<c:when test="${isSuccess == 0}">
					<option selected="selected" value="0">未同步</option>
				</c:when>
				<c:otherwise>
					<option value="0">未同步</option>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isSuccess == 1}">
					<option selected="selected" value="1">新增成功</option>
				</c:when>
				<c:otherwise>
					<option value="1">新增成功</option>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isSuccess == -1}">
					<option selected="selected" value="-1">新增失败</option>
				</c:when>
				<c:otherwise>
					<option value="-1">新增失败</option>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isSuccess == 2}">
					<option selected="selected" value="2">修改成功</option>
				</c:when>
				<c:otherwise>
					<option value="2">修改成功</option>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isSuccess == -2}">
					<option selected="selected" value="-2">修改失败</option>
				</c:when>
				<c:otherwise>
					<option value="-2">修改失败</option>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isSuccess == 3}">
					<option selected="selected" value="3">删除成功</option>
				</c:when>
				<c:otherwise>
					<option value="3">删除成功</option>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${isSuccess == -3}">
					<option selected="selected" value="-3">删除失败</option>
				</c:when>
				<c:otherwise>
					<option value="-3">删除失败</option>
				</c:otherwise>
			</c:choose>
		</select>
		&nbsp;<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
	</form>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>产品id</th><th>产品名称</th><th>批发商ID</th><th>批发商名</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="activity">
			<tr>
				<td>
					${activity.id}
				</td>
				<td>
					${activity.acitivityName}
				</td>
				<td>
					${activity.proCompany}
				</td>
				<td>
					${activity.proCompanyName}
				</td>
				<td>
					<input id="resync" class="btn btn-primary" onclick="resync(${activity.id})" type="button" value="重新同步"/>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>