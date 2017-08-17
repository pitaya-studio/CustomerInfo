<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

      <ul class="main-nav">

				 <c:forEach items="${fns:getMenuList()}" var="menu" varStatus="idxStatus">
 					<c:if test="${menu.parent.id eq 3&&menu.isShow eq 1}">
				         <li class="menu ${firstMenu?' active':''}">
					        <a class="menu" href="${ctx}${menu.href}" >
					        <div class="nav-sqd">${menu.name}</div>
					        </a>
				          </li>
						<c:if test="${firstMenu}">
							<c:set var="firstMenuId" value="${menu.id}"/>
						</c:if>
						<c:set var="firstMenu" value="false"/>
					</c:if>
				 </c:forEach>
        
      </ul>
