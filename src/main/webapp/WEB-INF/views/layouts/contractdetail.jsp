<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="flx" uri="/WEB-INF/tlds/StringFormatLx.tld" %>
	<em class="blue-trianle"></em>
				<!--门市信息浮窗开始-->
	<shiro:hasPermission name="travel:service:saleroomMsg">
		 <div  class="mail-header">
			<span  class="mail-title">${flx:meragerStr(agentinfo.agentName,agentinfo.agentBrand)}</span>
			<dl>
				<dt>地址:</dt>
				<dd>${agentAddressFull }</dd>
			</dl>
		</div>
		<shiro:hasPermission name="travel:service:salerommDetail">
		<c:set var="detail" value="true"></c:set>
		</shiro:hasPermission>
		<div  class="suspen-container ${detail ne null? 'info-detail':''}" >
			<c:forEach items="${supplyContactsList}" var="contact">
			 <div  class="suspen-items">
				<span  ${detail ne null? 'class="removeEnter"':''} >${contact.contactName }</span>
				<dl>
					<dt>手机：</dt>
					<dd>${contact.contactMobile }</dd><br>
					<dt>座机：</dt>
					<dd>${contact.contactPhone }</dd>
					<c:if test="${detail ne null }">
					<dt>QQ：</dt>
					<dd>${contact.contactQQ}</dd>
					<dt>邮箱：</dt>
					<dd>${contact.contactEmail}</dd>
					</c:if>
				</dl>	
			</div>
			</c:forEach>
		</div> 
	</shiro:hasPermission>
				