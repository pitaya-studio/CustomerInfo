<%--
  Created by IntelliJ IDEA.
  User: shijun.liu
  Date: 2016/9/2
  Time: 16:19
  Quauq服务费和渠道服务费显示列表
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="mod_information">
    <div class="mod_information_d">
        <div class="ydbz_tit"> 代收服务费</div>
    </div>
</div>
<table class="table table-striped table-bordered table-condensed table-mod2-group table-toggel mod_information_dzhan">
    <thead>
        <tr>
            <th width="7%">项目名称</th>
            <th width="10%">结算方</th>
            <th width="10%">订单号</th>
            <th width="9%">人数</th>
            <th width="9%">销售</th>
            <th width="9%">金额</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="item" items="${serviceCharge}">
            <tr class="budgetInCost">
                <td class="tc">${item.itemName}</td>
                <td class="tc">${item.settleName}</td>
                <td class="tc">${item.orderNum}</td>
                <td class="tc">${item.personNumber}</td>
                <td class="tc">${item.salerName}</td>
                <td class="tc">${item.amount}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<div class="costSum  clearfix" style=" width: 98%; margin: 0px auto; padding:10px 0px 0px 0px;">
    <ul class="cost-ul">
        <li>金额合计：${serviceChargeSum}</li>
    </ul>
</div>
