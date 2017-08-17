<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
	 <ul class="nav-tabs">
          <li class="active"><a target="_self"><c:if test="${kind=='save'}">新增</c:if><c:if test="${kind=='update'}">修改</c:if><c:if test="${lable=='guonei'}">国内</c:if><c:if test="${lable=='guoji'}">国际</c:if>行政区域</a></li>
     </ul>
</content>


