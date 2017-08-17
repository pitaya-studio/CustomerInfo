<%--
  Created by IntelliJ IDEA.
  User: ZhengZiyu
  Date: 2014/9/23
  Time: 15:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="application/javascript">
    $(function(){
        var showType = '<sitemesh:getProperty property="showType" />';
        var orderStatus = '<sitemesh:getProperty property="orderStatus" />';
        var mark = '';
        var markTail = '';
        switch (showType)
        {
            case '0' :
                mark = 'all';
                break;
            case '1' :
                mark = 'notPaid';
                markTail = 'notPaidAll';
                break;
            case '2' :
                mark = 'notPaid';
                markTail = 'notPaidHead';
                break;
            case '3' :
                mark = 'occupied';
                break;
            case '4' :
                mark = 'paid';
                markTail = 'paidHead';
                break;
            case '5' :
                mark = 'paid';
                markTail = 'paidAll';
                break;
            case '99' :
                mark = 'canceled';
                break;
            case '111' :
                mark = 'deleted';
                break;
            case '1000' :
                mark = 'preOrder';
                break;
            case 'statistics' :
                mark = 'statistics';
                break;
        }
        if(mark != ''){
            $('#' + mark).addClass('active');
        }
        if(markTail != '' && mark != ''){
            $('#' + markTail).children('a').addClass('active');
            $('#' + mark).addClass('current');
            $('#' + mark).parent('ul').addClass('hasNav');
        }

    });
</script>
<content tag="three_level_menu">
    <shiro:hasPermission name="singleOrder:list:allorder">
        <li id="all"><a href="${ctx}/orderList/manage/showOrderList/0/<sitemesh:getProperty property="orderStatus" />.htm">全部订单</a></li>
    </shiro:hasPermission>

    <li id="paid" class="ernav"><a href="javascript:void(0);">已支付订单<i></i></a>
        <dl>
            <shiro:hasPermission name="singleOrder:list:paid">
                <dt id="paidAll"><a href="${ctx}/orderList/manage/showOrderList/5/<sitemesh:getProperty property="orderStatus" />.htm">已支付全款</a><span>丨</span></dt>
            </shiro:hasPermission>
            <shiro:hasPermission name="singleOrder:list:frontMoneyPay">
                <dt id="paidHead"><a href="${ctx}/orderList/manage/showOrderList/4/<sitemesh:getProperty property="orderStatus" />.htm">已支付订金</a></dt>
            </shiro:hasPermission>
        </dl>
    </li>

    <li id="notPaid" class="ernav"><a href="javascript:void(0);">未支付订单<i></i></a>
        <dl>
            <shiro:hasPermission name="singleOrder:list:ordernopay">
                <dt id="notPaidAll"><a href="${ctx}/orderList/manage/showOrderList/1/<sitemesh:getProperty property="orderStatus" />.htm">未支付全款</a><span>丨</span></dt>
            </shiro:hasPermission>
            <shiro:hasPermission name="singleOrder:list:frontMoneyoccupyNoPay">
                <dt id="notPaidHead"><a href="${ctx}/orderList/manage/showOrderList/2/<sitemesh:getProperty property="orderStatus" />.htm">未支付订金</a></dt>
            </shiro:hasPermission>
        </dl>
    </li>

    <shiro:hasPermission name="singleOrder:list:occupyNoPay">
        <li id="occupied"><a href="${ctx}/orderList/manage/showOrderList/3/<sitemesh:getProperty property="orderStatus" />.htm">已占位</a></li>
    </shiro:hasPermission>

    <shiro:hasPermission name="singleOrder:list:cancelorder">
        <li id="canceled"><a href="${ctx}/orderList/manage/showOrderList/99/<sitemesh:getProperty property="orderStatus" />.htm">已取消</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="singleOrder:list:deleteByFlag">
        <li id="deleted"><a href="${ctx}/orderList/manage/showOrderList/111/<sitemesh:getProperty property="orderStatus" />.htm">已删除</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="singleOrder:list:orderStatistics">
    	<li id="statistics"><a href="${ctx}/orderStatistics/manage/orderStatistics/1.htm">订单统计</a></li>
    </shiro:hasPermission>
</content>