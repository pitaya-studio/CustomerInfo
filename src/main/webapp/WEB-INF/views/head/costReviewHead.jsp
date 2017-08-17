<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
	<shiro:hasPermission name="cost:activity:review"> 
		<li id="single"><a href="${ctx}/cost/review/list/1/1">单团产品<font color="red">${fns:getReviewTotal(15,1)}</font></a></li>
        <li id="loose"><a href="${ctx}/cost/review/list/2/1">散拼产品<font color="red">${fns:getReviewTotal(15,2)}</font></a></li>
        <li id="study"><a href="${ctx}/cost/review/list/3/1">游学产品<font color="red">${fns:getReviewTotal(15,3)}</font></a></li>
        <li id="free"><a href="${ctx}/cost/review/list/5/1">自由行<font color="red">${fns:getReviewTotal(15,5)}</font></a></li>
        <li id="bigCustomer"><a href="${ctx}/cost/review/list/4/1">大客户<font color="red">${fns:getReviewTotal(15,4)}</font></a></li>
        <li id="cruise"><a href="${ctx}/cost/review/list/10/1">游轮<font color="red">${fns:getReviewTotal(15,10)}</font></a></li>
	</shiro:hasPermission>

	<shiro:hasPermission name="cost:airticket:review">
		<li id="airticket"><a href="${ctx}/cost/review/airTicketList/1">机票产品<font color="red">${fns:getReviewTotal(15,7)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cost:visa:review">
		<li id="visa"><a href="${ctx}/cost/review/visaList/1">签证产品<font color="red">${fns:getReviewTotal(15,6)}</font></a></li>
	</shiro:hasPermission>
	
	<shiro:hasPermission name="cost:island:review">
		<li id="island"><a href="${ctx}/cost/review/island/1">海岛游产品<font color="red">${fns:getReviewTotal(15,12)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cost:hotel:review">
        <li id="hotel"><a href="${ctx}/cost/review/hotel/1">酒店产品<font color="red">${fns:getReviewTotal(18,11)}</font></a></li>
	</shiro:hasPermission>
</content>