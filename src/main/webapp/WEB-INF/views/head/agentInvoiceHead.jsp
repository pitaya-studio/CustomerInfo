<%--
  Created by IntelliJ IDEA.
  User: ZhengZiyu
  Date: 2014/9/23
  Time: 16:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="base.jsp"%>
<content tag="three_level_menu">
    <shiro:hasPermission name="invoice:agent:view"><li id="agentInvoiceApply"><a href="${ctx}/invoice/limit/agentlist">发票申请</a></li></shiro:hasPermission>
    <shiro:hasPermission name="invoice:agentverify:view"><li id="agentInvoiceList"><a href="${ctx}/invoice/limit/agentinvoicelist?verifyStatus=1">发票审核记录查询</a></li></shiro:hasPermission>
</content>