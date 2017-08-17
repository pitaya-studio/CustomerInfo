<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

	<div class="accordion" id="menu">
	<c:set var="menuList" value="${fns:getMenuList()}"/>
	<c:forEach items="${menuList}" var="menu" varStatus="idxStatus">
	<c:if test="${menu.parent.id eq (not empty param.parentId?param.parentId:1)&&menu.isShow eq 1}">
		<div class="accordion-group">
		    <div class="accordion-heading">
		    	<a class="accordion-toggle" data-toggle="collapse" data-parent="#menu" href="#collapse${menu.id}" title="${menu.remarks}">
		    	</a>
		    </div>
		    <div id="collapse${menu.id}" class="accordion-body collapse ${firstMenu?'in':''}">
				<div class="accordion-inner">
					<ul class="nav nav-list">
					<c:forEach items="${menuList}" var="menuChild">
					<c:if test="${menuChild.parent.id eq menu.id&&menuChild.isShow eq 1}">
						<li><a href="${fn:indexOf(menuChild.href, '://') eq -1?ctx:''}${not empty menuChild.href?menuChild.href:'/404'}" target="${not empty menuChild.target?menuChild.target:'mainFrame'}" ><i class="icon-${not empty menuChild.icon?menuChild.icon:'circle-arrow-right'}"></i>&nbsp;${menuChild.name}</a></li>
					<c:set var="firstMenu" value="false"/></c:if></c:forEach></ul>
				</div>
		    </div>
		</div>
	</c:if></c:forEach></div>
