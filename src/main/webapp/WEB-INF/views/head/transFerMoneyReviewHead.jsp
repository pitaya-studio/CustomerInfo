<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">

	<shiro:hasPermission name="single:transFerMoney:review">         
        <li id="single"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=1">单团产品<font color="red">${fns:getReviewNum(12,1)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="loose:transFerMoney:review"> 
         <li id="loose"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=2">散拼产品<font color="red">${fns:getReviewNum(12,2)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="study:transFerMoney:review"> 
        <li id="study"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=3">游学产品<font color="red">${fns:getReviewNum(12,3)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="free:transFerMoney:review"> 
        <li id="free"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=5">自由行<font color="red">${fns:getReviewNum(12,5)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="bigCustomer:transFerMoney:review"> 
        <li id="bigCustomer"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=4">大客户<font color="red">${fns:getReviewNum(12,4)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cruise:transFerMoney:review">
        <li id="cruise"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=10">游轮产品<font color="red">${fns:getReviewNum(12,10)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="island:transFerMoney:review">
        <li id="island"><a href="${ctx}/orderReview/manage/getTransFerMoneyReviewList?orderType=12">海岛游产品<font color="red">${fns:getReviewNum(12,12)}</font></a></li>
	</shiro:hasPermission>
</content>