<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="application/javascript">
    $(function(){
        var orderStatus = '<sitemesh:getProperty property="orderStatus" />';
        var mark = '';
        var markTail = '';
        switch (orderStatus)
        {
            case '0' :
                mark = 'all';
                break;
            case '1' :
                mark = 'notSingUp';
                break;
            case '2' :
                mark = 'alreadySingUp';
                break;
            case '3' :
                mark = 'canceled';
                break;
        }
        if(mark != ''){
            $('#' + mark).addClass('active');
        }
    });
</script>
<content tag="three_level_menu">
    <shiro:hasPermission name="hotelOrder:list:allorder">
        <li id="all"><a href="${ctx}/hotelOrder/list/0.htm">全部订单</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="hotelOrder:not:singUp">
        <li id="notSingUp"><a href="${ctx}/hotelOrder/list/1.htm">待确认报名</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="hotelOrder:already:singUp">
        <li id="alreadySingUp"><a href="${ctx}/hotelOrder/list/2.htm">已确认报名</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="hotelOrder:list:cancelorder">
        <li id="canceled"><a href="${ctx}/hotelOrder/list/3.htm">已取消</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="hotelOrder:list:orderStatistics">
    	<li id="statistics"><a href="${ctx}/orderStatistics/manage/orderStatistics/<sitemesh:getProperty property="orderStatus" />.htm?orderOrGroup=${orderOrGroup}">订单统计</a></li>
    </shiro:hasPermission>
</content>