<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
        <li id="single"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=1">单团产品<font color="red">${fns:getReviewNum(1,1)}</font></a></li>
        <li id="loose"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=2">散拼产品<font color="red">${fns:getReviewNum(1,2)}</font></a></li>
        <li id="study"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=3">游学产品<font color="red">${fns:getReviewNum(1,3)}</font></a></li>
        <li id="free"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=5">自由行<font color="red">${fns:getReviewNum(1,5)}</font></a></li>
        <li id="bigCustomer"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=4">大客户<font color="red">${fns:getReviewNum(1,4)}</font></a></li>
        <li id="boat"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=10">游轮<font color="red">${fns:getReviewNum(1,10)}</font></a></li>
		<!-- <li id="island"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=12">海岛游产品<font color="red">${fns:getReviewNum(1,12)}</font></a></li>
        <li id="hotel"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=11">酒店产品<font color="red">${fns:getReviewNum(1,11)}</font></a></li> -->
        <li id="airticket"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=7">机票产品<font color="red">${fns:getReviewNum(1,7)}</font></a></li>
        <li id="visa"><a href="${ctx}/refundmentReview/refundReviewList?headPrd=6">签证产品<font color="red">${fns:getReviewNum(1,6)}</font></a></li>
</content>