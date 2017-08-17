<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
	 <ul class="nav-tabs">
                <ul class="nav-tabs">
                <li class="active">
                <a href="#" >
                <c:if test="${lable=='guonei'}">国内地理区域</c:if>
                <c:if test="${lable=='guoji'}">国际地理区域</c:if>
                </a>
                </li>
                </ul>
     </ul>
</content>