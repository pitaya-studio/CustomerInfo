<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<content tag="three_level_menu">
		<shiro:hasPermission name="eprice:list:eprices">
	    	  <li <c:if test="${showType==216}">class='active'</c:if>><a href="${ctx}/eprice/manager/project/list4saler">询价记录</a></li>
	     </shiro:hasPermission>
	      <shiro:hasPermission name="eprice:list:recordStatistics">
	      	<li <c:if test="${showType==202}">class='active'</c:if>><a href="${ctx}/eprice/manager/statistics/recordstatistics">询价统计</a></li>
	      </shiro:hasPermission>
</content>

