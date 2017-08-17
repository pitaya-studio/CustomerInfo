<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<%--<script type="text/javascript">--%>
	<%--$(function(){--%>
		<%--var activeMark = '<sitemesh:getProperty property="current" />';--%>
		<%--switch (${typeId}) {--%>
			<%--case 1 : activeMark = "single"; break;--%>
			<%--case 2 : activeMark = "loose"; break;--%>
			<%--case 3 : activeMark = "study"; break;--%>
			<%--case 4 : activeMark = "bigCustomer"; break;--%>
			<%--case 5 : activeMark = "free"; break;--%>
			<%--case 6 : activeMark = "visa"; break;--%>
			<%--case 7 : activeMark = "airticket"; break;--%>
			<%--case 10 : activeMark = "cruise"; break;--%>
<%--//			case 11 : activeMark = "hotel"; break;--%>
<%--//			case 12 : activeMark = "island"; break;--%>
		<%--}--%>
		<%--$('#' + activeMark).addClass('active');--%>
	<%--});--%>

	<%--function jump(href){--%>
		<%--var _m = '${_m}';--%>
		<%--var _mc = '${_mc}';--%>
		<%--href = appendParam(href, {_m : _m, _mc : _mc});--%>
		<%--window.location.href = href;--%>
	<%--}--%>
<%--</script>--%>
<content tag="three_level_menu">
	<shiro:hasPermission name="singleCost:manager:view">         
        <li id="single"><a href="${ctx}/cost/manager/list/1/">单团产品</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="looseCost:manager:view"> 
         <li id="loose"><a href="${ctx}/cost/manager/list/2/">散拼产品</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="studyCost:manager:view"> 
        <li id="study"><a href="${ctx}/cost/manager/list/3/">游学产品</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="freeCost:manager:view"> 
        <li id="free"><a href="${ctx}/cost/manager/list/5/">自由行</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="bigCustomerCost:manager:view"> 
        <li id="bigCustomer"><a href="${ctx}/cost/manager/list/4/">大客户</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cruiseCost:manager:view"> 
        <li id="cruise"><a href="${ctx}/cost/manager/list/10/">游轮</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="island:manager:view"> 
  <li id="island"><a href="${ctx}/cost/island/list">海岛游产品</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="hotel:manager:view"> 
  <li id="hotel"><a href="${ctx}/cost/island/hotel">酒店产品</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cost:airticket:view">
        <li id="airticket"><a href="${ctx}/cost/manager/airTicketList">机票产品</a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="visaCost:manager:view">
        <li id="visa"><a href="${ctx}/cost/visa/list">签证产品</a></li>
	</shiro:hasPermission>
</content>