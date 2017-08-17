<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
     	<li id="records"><a href="${ctx}/receipt/limit/supplyreceiptlist/9">收据记录   </a></li>
        <li id="review"><a href="${ctx}/receipt/limit/supplyreceiptlist/0">待审核收据<font color="red">${fns:getToBeReviewedReceiptNum()}</font></a></li>
        <li id="reviewed"><a href="${ctx}/receipt/limit/supplyreceiptlist/1">已审核收据</a></li>
        <li id="a_rece_c"><a href="${ctx}/receipt/limit/supplyreceiptlist/2">开收据<font color="red">${fns:getReviewedReceiptNum()}</font></a></li>
</content>
