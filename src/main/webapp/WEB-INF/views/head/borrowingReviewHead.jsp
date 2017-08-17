<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">

	<shiro:hasPermission name="single:borrowing:review">         
        <li id="single"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=1">单团产品<font color="red">${fns:getReviewNum(19,1)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="loose:borrowing:review"> 
         <li id="loose"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=2">散拼产品<font color="red">${fns:getReviewNum(19,2)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="study:borrowing:review"> 
        <li id="study"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=3">游学产品<font color="red">${fns:getReviewNum(19,3)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="free:borrowing:review"> 
        <li id="free"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=5">自由行<font color="red">${fns:getReviewNum(19,5)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="bigCustomer:borrowing:review"> 
        <li id="bigCustomer"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=4">大客户<font color="red">${fns:getReviewNum(19,4)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="visa:borrowing:review">
        <li id="visa"><a href="${ctx}/visa/workflow/borrowmoney/visaBorrowMoney4XXZReviewList?flowType=20">签证产品<font color="red">${fns:getReviewNum(20,6)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="airticket:borrowing:review">
        <li id="airticket"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=7">机票产品<font color="red">${fns:getReviewNum(19,7)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="cruise:borrowing:review">
        <li id="cruise"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=10">游轮产品<font color="red">${fns:getReviewNum(19,10)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="island:borrowing:review">
        <li id="island"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=12">海岛游产品<font color="red">${fns:getReviewNum(19,12)}</font></a></li>
	</shiro:hasPermission>
	<shiro:hasPermission name="hotel:borrowing:review">
        <li id="hotel"><a href="${ctx}/orderReview/manage/getBorrowingList?orderType=11">酒店产品<font color="red">${fns:getReviewNum(19,11)}</font></a></li>
	</shiro:hasPermission>
</content>