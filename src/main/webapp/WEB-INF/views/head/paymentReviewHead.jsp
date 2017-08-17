<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
	

        <li id="single"><a href="${ctx}/payment/review/list/1/1">单团产品<font color="red">${fns:getReviewTotal(18,1)}</font></a></li>
        <li id="loose"><a href="${ctx}/payment/review/list/2/1">散拼产品<font color="red">${fns:getReviewTotal(18,2)}</font></a></li>
        <li id="study"><a href="${ctx}/payment/review/list/3/1">游学产品<font color="red">${fns:getReviewTotal(18,3)}</font></a></li>
        <li id="free"><a href="${ctx}/payment/review/list/5/1">自由行<font color="red">${fns:getReviewTotal(18,5)}</font></a></li>
        <li id="bigCustomer"><a href="${ctx}/payment/review/list/4/1">大客户<font color="red">${fns:getReviewTotal(18,4)}</font></a></li>
        <li id="cruise"><a href="${ctx}/payment/review/list/10/1">游轮<font color="red">${fns:getReviewTotal(18,10)}</font></a></li>
		<!--<li id="island"><a href="${ctx}/payment/review/island/1">海岛游产品</a></li>
        <li id="hotel"><a href="${ctx}/payment/review/hotel/1">酒店产品</a></li>  -->
        <li id="airticket"><a href="${ctx}/payment/review/airTicketList/1">机票产品<font color="red">${fns:getReviewTotal(18,7)}</font></a></li>
        <li id="visa"><a href="${ctx}/payment/review/visaList/1">签证产品<font color="red">${fns:getReviewTotal(18,6)}</font></a></li>


</content>