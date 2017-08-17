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
            case '7' :
                mark = 'op';
                break;
            case '8' :
                mark = 'cw';
                break;
            case '9' :
                mark = 'cw_cx';
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
			case '20':
				mark = 'cover';
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
    <shiro:hasPermission name="${orderTypeStr}Order:list:allorder">
        <li id="all">
        	<a href="javascript:void(0)" onclick="getOrderList(0, ${orderStatus}, '${orderOrGroup}');">全部订单</a>
        </li>
    </shiro:hasPermission>
	
    <li id="paid" class="ernav"><a href="javascript:void(0);">已收款订单<i></i></a>
        <dl>
            <shiro:hasPermission name="${orderTypeStr}Order:list:paid">
                <dt id="paidAll">
                	<a href="javascript:void(0)" onclick="getOrderList(5, ${orderStatus}, '${orderOrGroup}');">已收全款</a>
                	<span>丨</span>
                </dt>
            </shiro:hasPermission>
            <shiro:hasPermission name="${orderTypeStr}Order:list:frontMoneyPay">
                <dt id="paidHead">
                	<a href="javascript:void(0)" onclick="getOrderList(4, ${orderStatus}, '${orderOrGroup}');">已收订金</a>
                </dt>
            </shiro:hasPermission>
        </dl>
    </li>

    <li id="notPaid" class="ernav"><a href="javascript:void(0);">未收款订单<i></i></a>
        <dl>
            <shiro:hasPermission name="${orderTypeStr}Order:list:ordernopay">
                <dt id="notPaidAll">
                	<a href="javascript:void(0)" onclick="getOrderList(1, ${orderStatus}, '${orderOrGroup}');">未收全款</a>
                	<span>丨</span>
                </dt>
            </shiro:hasPermission>
            <shiro:hasPermission name="${orderTypeStr}Order:list:frontMoneyoccupyNoPay">
                <dt id="notPaidHead">
                	<a href="javascript:void(0)" onclick="getOrderList(2, ${orderStatus}, '${orderOrGroup}');">未收订金</a>
                </dt>
            </shiro:hasPermission>
        </dl>
    </li>

    <shiro:hasPermission name="${orderTypeStr}Order:list:occupyNoPay">
        <li id="occupied"><a href="javascript:void(0)" onclick="getOrderList(3, ${orderStatus}, '${orderOrGroup}');">已占位</a></li>
    </shiro:hasPermission>
	<!-- C322大洋屏蔽 -->
	<%--<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">--%>
    <%--<c:if test="${office.isShowCancelOrder == 0}">--%>
    <shiro:hasPermission name="${orderTypeStr}Order:list:cancelorder">
        <li id="canceled"><a href="javascript:void(0)" onclick="getOrderList(99, ${orderStatus}, '${orderOrGroup}');">已取消</a></li>
    </shiro:hasPermission>
    <%--</c:if>--%>
	<%--</c:if>	    --%>
	<%--<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">   --%>
    <%--<c:if test="${office.isShowDeleteOrder == 0}">--%>
    <shiro:hasPermission name="${orderTypeStr}Order:list:deleteByFlag">
        <li id="deleted"><a href="javascript:void(0)" onclick="getOrderList(111, ${orderStatus}, '${orderOrGroup}');">已删除</a></li>
    </shiro:hasPermission>
    <%--</c:if>--%>
    <shiro:hasPermission name="${orderTypeStr}Order:list:op">
        <li id="op"><a href="javascript:void(0)" onclick="getOrderList(7, ${orderStatus}, '${orderOrGroup}');">待确认订单</a></li>
    </shiro:hasPermission>
    
    <c:if test="${fns:getUser().company.orderPayMode eq 1}">
    	<shiro:hasPermission name="${orderTypeStr}Order:list:cw">
	        <li id="cw"><a href="javascript:void(0)" onclick="getOrderList(8, ${orderStatus}, '${orderOrGroup}');">待财务确认</a></li>
	    </shiro:hasPermission>
	    
	    <shiro:hasPermission name="${orderTypeStr}Order:list:cw_cx">
	        <li id="cw_cx"><a href="javascript:void(0)" onclick="getOrderList(9, ${orderStatus}, '${orderOrGroup}');">已撤销占位</a></li>
	    </shiro:hasPermission>
    </c:if>
    <!-- 隐藏预报名 
    <c:choose>
		<c:when test="${youjiaCompanyUuid==companyUuid}">
			
		</c:when>
		<c:otherwise>
			 <shiro:hasPermission name="${orderTypeStr}Order:list:applyOrder">
		        <li id="preOrder"><a href="${ctx}/applyOrderCommon/manage/showApplyOrderList/1000/<sitemesh:getProperty property="orderStatus" />.htm">预报名订单</a></li>
		    </shiro:hasPermission>
		</c:otherwise>
	</c:choose>
	-->
    
    <shiro:hasPermission name="${orderTypeStr}Order:list:orderStatistics">
    	<li id="statistics"><a href="${ctx}/orderStatistics/manage/orderStatistics/<sitemesh:getProperty property="orderStatus" />.htm?orderOrGroup=${orderOrGroup}">订单统计</a></li>
    </shiro:hasPermission>
    
    <shiro:hasPermission name="looseOrder:list:cover">
    	<li id="cover"><a href="${ctx}/groupCover/coverList">补位订单</a></li>
    </shiro:hasPermission>
</content>