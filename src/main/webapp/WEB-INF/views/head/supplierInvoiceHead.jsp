<%--
  Created by IntelliJ IDEA.
  User: ZhengZiyu
  Date: 2014/9/23
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
    <shiro:hasPermission name="order:list:tradingrecords">
        <li id="invoiceTrace"><a href="${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=">发票记录</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="invoice:supplier:view">
        <li id="invoiceApply"><a href="${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=0">待审核发票<font color="red">${fns:getToBeReviewedInvoiceNum()}</font></a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="invoice:supplyverify:view">
        <li id="invoiceApplied"><a href="${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=ne0">已审核发票</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="invoice:limit:supplyinvoicelist">
        <li id="invoiceOuted"><a href="${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=1">开票<font color="red">${fns:getReviewedInvoiceNum()}</font></a></li>
    </shiro:hasPermission>
</content>