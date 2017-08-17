<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
        <li id="single"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=1">单团产品<font color="red">${fns:getReviewNum(10,1)}</font></a></li>
        <li id="loose"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=2">散拼产品<font color="red">${fns:getReviewNum(10,2)}</font></a></li>
        <li id="study"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=3">游学产品<font color="red">${fns:getReviewNum(10,3)}</font></a></li>
        <li id="free"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=5">自由行<font color="red">${fns:getReviewNum(10,5)}</font></a></li>
        <li id="bigCustomer"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=4">大客户<font color="red">${fns:getReviewNum(10,4)}</font></a></li>
        <li id="boat"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=10">游轮<font color="red">${fns:getReviewNum(10,10)}</font></a></li>
		<!-- <li id="island"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=12">海岛游产品<font color="red">${fns:getReviewNum(1,12)}</font></a></li>
        <li id="hotel"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=11">酒店产品<font color="red">${fns:getReviewNum(1,11)}</font></a></li> -->
        <li id="airticket"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=7">机票产品<font color="red">${fns:getReviewNum(10,7)}</font></a></li>
        <li id="visa"><a href="${ctx}/changePricement/changePriceReviewList?headPrd=6">签证产品<font color="red">${fns:getReviewNum(10,6)}</font></a></li>
</content>