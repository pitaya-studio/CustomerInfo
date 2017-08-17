<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
	 <ul class="nav-tabs">
          <li class="active"><a target="_self">添加<c:if test="${lable=='guonei'}">国内</c:if><c:if test="${lable=='guoji'}">国际</c:if>顶级区域</a></li>
     </ul>
</content>

