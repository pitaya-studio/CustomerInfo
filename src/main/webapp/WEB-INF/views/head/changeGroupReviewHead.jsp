<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">

	<shiro:hasPermission name="single:changeGroup:review">         
        <li id="single"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=1">单团产品<font color="red">${fns:getReviewNum(11,1)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="loose:changeGroup:review"> 
         <li id="loose"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=2">散拼产品<font color="red">${fns:getReviewNum(11,2)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="study:changeGroup:review"> 
        <li id="study"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=3">游学产品<font color="red">${fns:getReviewNum(11,3)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="free:changeGroup:review"> 
        <li id="free"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=5">自由行<font color="red">${fns:getReviewNum(11,5)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="bigCustomer:changeGroup:review"> 
        <li id="bigCustomer"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=4">大客户<font color="red">${fns:getReviewNum(11,4)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cruise:changeGroup:review">
        <li id="cruise"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=10">游轮产品<font color="red">${fns:getReviewNum(11,10)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="island:changeGroup:review">
        <li id="island"><a href="${ctx}/orderReview/manage/getChangeGroupReviewList?orderType=12">海岛游产品<font color="red">${fns:getReviewNum(11,12)}</font></a></li>
	</shiro:hasPermission>
</content>