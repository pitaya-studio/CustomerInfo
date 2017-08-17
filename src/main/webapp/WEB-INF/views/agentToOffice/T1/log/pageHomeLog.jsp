<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>搜索日志</title>
	<meta name="decorator" content="wholesaler"/>
	<%--<link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet">--%>
    <%--<link href="${ctxStatic}/css/page-T2.css" type="text/css" rel="stylesheet">--%>
	<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/common.css"/>
	<script src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"type="text/javascript"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript">
		function page(n,s){
					$("#pageNo").val(n);
					$("#pageSize").val(s);
					$("#searchForm").attr("action","${ctx}/search/log/getLogList");
					$("#searchForm").submit();
				}
	</script>


  </head>
  	<form id="searchForm" method="post" action="${ctx}/search/log/getLogList">
  		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize"  type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/> 
  	</form>
  		<table id="contentTable" class="table activitylist_bodyer_table mainTable">
  			<thead>
  				<tr>
  				<th width="2%">序号</th>
  				<th width="6%">搜索框</th>
  				<th width="6%">出发城市</th>
  				<th width="6%">目的地</th>
  				<th width="6%">抵达城市</th>
  				<th width="6%">供应商</th>
  				<th width="6%">出团日期</th>
  				<th width="6%">行程天数</th>
  				<th width="6%">价格区间</th>
  				<th width="6%">余位</th>
  				<th width="6%">国内游/境外游</th>
  				<th width="6%">产品类型</th>
  				<th width="6%">搜索时间</th>
  				<th width="6%">搜索人</th>
  				<th width="4%">团期数</th>
  				</tr>
  			</thead>
  			<tbody>
	  			<c:forEach items="${page.list }" varStatus="status" var="s">
	  				<tr>
	  					<td>${status.count }</td>
	  					<td>${s.input }</td>
	  					<td>${s.fromCity }</td>
	  					<td>${s.target }</td>
	  					<td>${s.arrival }</td>
	  					<td>${s.supply }</td>
	  					<td>${s.groupOpenDate }</td>
	  					<td>${s.days }</td>
	  					<td>${s.prices }</td>
	  					<td>${s.seats }</td>
	  					<td>
	  						<c:choose>
	  							<c:when test="${s.bugetType == 100000 }">境外游</c:when>
	  							<c:otherwise>
	  								国内游
	  							</c:otherwise>
	  						</c:choose>
	  					</td>
	  					<td>散拼</td>
	  					<td><fmt:formatDate value="${s.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
	  					<td>${fns:getUserById(s.createBy).getName()}</td>
	  					<td>${s.count }</td>
	  				</tr>
	  			</c:forEach>
  			</tbody>
  		</table>
  		<div class="page">
				<div class="pagination">
						<div class="endPage">${page }</div>
				</div>
		</div>	
    	
  </body>
</html>
