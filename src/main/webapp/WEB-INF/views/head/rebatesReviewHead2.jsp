<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">

	<shiro:hasPermission name="single:rebates:review">         
        <li id="single"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=1&flag=1">单团产品<font color="red">${fns:getReviewNum(9,1)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="loose:rebates:review"> 
         <li id="loose"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=2&flag=1">散拼产品<font color="red">${fns:getReviewNum(9,2)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="study:rebates:review"> 
        <li id="study"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=3&flag=1">游学产品<font color="red">${fns:getReviewNum(9,3)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="free:rebates:review"> 
        <li id="free"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=5&flag=1">自由行<font color="red">${fns:getReviewNum(9,5)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="bigCustomer:rebates:review"> 
        <li id="bigCustomer"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=4&flag=1">大客户<font color="red">${fns:getReviewNum(9,4)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="visa:rebates:review">
        <li id="visa"><a href="${ctx}/review/visaRebates/financeList?flag=1">签证产品<font color="red">${fns:getReviewNum(9,6)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="airticket:rebates:review">
        <li id="airticket"><a href="${ctx}/airticketRebates/queryAirticketRebatesList?orderType=7&flag=1">机票产品<font color="red">${fns:getReviewNum(9,7)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cruise:rebates:review">
        <li id="cruise"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=10&flag=1">游轮产品<font color="red">${fns:getReviewNum(9,10)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="island:rebates:review">
        <li id="island"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=12&flag=1">海岛游产品<font color="red">${fns:getReviewNum(9,12)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="hotel:rebates:review">
        <li id="hotel"><a href="${ctx}/order/rebates/review/showRebatesReviewList?orderType=11&flag=1">酒店产品<font color="red">${fns:getReviewNum(9,11)}</font></a></li>
	</shiro:hasPermission>
</content>