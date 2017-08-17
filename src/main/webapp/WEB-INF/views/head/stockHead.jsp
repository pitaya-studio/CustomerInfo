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
         <li id="activityStock"><a href="${ctx}/stock/manager/apartGroup/?agentId=">散拼产品</a></li>
   
    <shiro:hasPermission name="stock:airticket:view">
        <li id="flightStock"><a href="${ctx}/stock/manager/airticket/?agentId=">机票产品</a></li>
    </shiro:hasPermission>   
</content>