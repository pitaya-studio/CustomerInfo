<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="flx" uri="/WEB-INF/tlds/StringFormatLx.tld" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	<!--顶部搜索开始-->
	<div class="top-search">
	    <div class="top-container">
			<div class="search-input"><i class="fa fa-search" aria-hidden="true"></i><input type="text" placeholder="搜索..."  onkeyup="searhStore(this);"  onkeydown="searhStore(this);"></div>
			<i class="fa fa-times-circle" aria-hidden="true"  onclick="hidePanel();"></i>
		</div>
		<ul  class="letter-nav"  id="letter">
			<li  onclick="posToSel(this)">A</li>
			<li  onclick="posToSel(this)">B</li>
			<li  onclick="posToSel(this)">C</li>
			<li  onclick="posToSel(this)">D</li>
			<li  onclick="posToSel(this)">E</li>
			<li  onclick="posToSel(this)">F</li>
			<li  onclick="posToSel(this)">G</li>
			<li  onclick="posToSel(this)">H</li>
			<li  onclick="posToSel(this)">I</li>
			<li  onclick="posToSel(this)">J</li>
			<li  onclick="posToSel(this)">K</li>
			<li  onclick="posToSel(this)">L</li>
			<li  onclick="posToSel(this)">M</li>
			<li  onclick="posToSel(this)">N</li>
			<li  onclick="posToSel(this)">O</li>
			<li  onclick="posToSel(this)">P</li>
			<li  onclick="posToSel(this)">Q</li>
			<li  onclick="posToSel(this)">R</li>
			<li  onclick="posToSel(this)">S</li>
			<li  onclick="posToSel(this)">T</li>
			<li  onclick="posToSel(this)">U</li>
			<li  onclick="posToSel(this)">V</li>
			<li  onclick="posToSel(this)">W</li>
			<li  onclick="posToSel(this)">X</li>
			<li  onclick="posToSel(this)">Y</li>
			<li  onclick="posToSel(this)">Z</li>
		</ul>
	</div><!--顶部搜索结束-->
	<!--去除父元素的透明度-->
	<div class="content-list simulate-bottom"  id="contentList"></div>    
    <!--列表开始-->
	<div class="content-list simulate-top"  id="contentList0">
	<shiro:hasPermission name="travel:service:outlinestatus">
		<c:set var="onlinestatus" value="true"></c:set>
	</shiro:hasPermission>
	<shiro:hasPermission name="travel:service:saleroomMsg">
		<c:set var="saleroomMsg" value="true"></c:set>
	</shiro:hasPermission>
	<c:forEach items="${addresslist}" var="addresslest">
	<c:if test="${addresslest.key ne '思锐创途' && addresslest.key ne '思锐创途销售专用' && addresslest.key ne '测试专用'}">
		<dl  class="removeBottom">
			<dt  class="ellipis">${addresslest.key }</dt>
			<c:forEach items="${fns:orderByAgentName(addresslest.key,addresslest.value) }" var="address">
				<c:if test="${!(fn:length(addresslest.value) != 1 && address.agentName == addresslest.key)}">
				 <c:if test="${address.loginStatus == 0 && saleroomMsg ne null}">
				  	<dd  mailType="1"  class="disabled" ></c:if>
				  <c:if test="${address.loginStatus == 1 && saleroomMsg ne null}">    
				  	<dd  mailType="1" >
				  </c:if>
				  <c:if test="${saleroomMsg eq null && address.loginStatus == 0}"><dd  mailType="0"  class="disabled" ></c:if>
				   <c:if test="${saleroomMsg eq null && address.loginStatus == 1}"><dd  mailType="0" ></c:if>
				<c:if test="${onlinestatus ne null }"><i class="fa fa-circle" aria-hidden="true"></i></c:if> 
				<c:choose>
					<c:when test="${fn:length(addresslest.value) == 1 && address.agentName == addresslest.key}"><span  class="ellipis">${address.agentName }</span></c:when>
					<c:when test="${fn:length(addresslest.value) != 1 && address.agentName != addresslest.key}"><span   class="ellipis">${flx:getChildString(address.agentName,addresslest.key)}</span></c:when>
					<c:otherwise><span  class="ellipis">${address.agentName }</span></c:otherwise>
				</c:choose>
				<!--门市信息浮窗开始-->
				<div  class="mail-suspen" id="${address.agentId }">
				</div>
				</c:if>
			</dd>
			</c:forEach>
		</dl>
		</c:if>
		</c:forEach>
    </div><!--列表结束--> 
