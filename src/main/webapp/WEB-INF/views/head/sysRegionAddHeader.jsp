<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
     <ul class="nav-tabs">
     <li class="active">
                        <c:choose>
							    <c:when test="${kind =='update'}">
							       <a target="_self">修改<c:if test="${lable=='guonei'}">国内</c:if><c:if test="${lable=='guoji'}">国际</c:if>地理区域</a>
							    </c:when>    
						        <c:otherwise>
						         <a target="_self">新增<c:if test="${lable=='guonei'}">国内</c:if><c:if test="${lable=='guoji'}">国际</c:if>地理区域</a>
						        </c:otherwise>
						   </c:choose>
     </li>
     </ul>
</content>