<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">

	<shiro:hasPermission name="single:exitGroup:review">         
        <li id="single"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=1">单团产品<font color="red">${fns:getReviewNum(8,1)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="loose:exitGroup:review"> 
         <li id="loose"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=2">散拼产品<font color="red">${fns:getReviewNum(8,2)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="study:exitGroup:review"> 
        <li id="study"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=3">游学产品<font color="red">${fns:getReviewNum(8,3)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="free:exitGroup:review"> 
        <li id="free"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=5">自由行<font color="red">${fns:getReviewNum(8,5)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="bigCustomer:exitGroup:review"> 
        <li id="bigCustomer"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=4">大客户<font color="red">${fns:getReviewNum(8,4)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cruise:exitGroup:review">
        <li id="cruise"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=10">游轮产品<font color="red">${fns:getReviewNum(8,10)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="island:exitGroup:review">
        <li id="island"><a href="${ctx}/orderReview/manage/getExitGroupReviewList?orderType=12">海岛游产品<font color="red">${fns:getReviewNum(8,12)}</font></a></li>
	</shiro:hasPermission>
</content>