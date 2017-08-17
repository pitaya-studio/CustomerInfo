<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="application/javascript">
    $(function(){
        var showType = '<sitemesh:getProperty property="showType" />';
        var mark = '';
        var markTail = '';
        switch (showType)
        {
        	case '' :
            	mark = 'all';
            	break;
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
	<li id="all">
		<a href="javascript:void(0)" onclick="getOrderList(0, '${orderOrGroup}');">全部订单</a>
	</li>
    <li id="paid" class="ernav"><a href="javascript:void(0);">已收款订单<i></i></a>
        <dl>
			<dt id="paidAll">
				<a href="javascript:void(0)" onclick="getOrderList(5, '${orderOrGroup}');">已收全款</a>
				<span>丨</span>
			</dt>
			<dt id="paidHead">
				<a href="javascript:void(0)" onclick="getOrderList(4, '${orderOrGroup}');">已收订金</a>
			</dt>
        </dl>
    </li>
    <li id="notPaid" class="ernav"><a href="javascript:void(0);">未收款订单<i></i></a>
        <dl>
			<dt id="notPaidAll">
				<a href="javascript:void(0)" onclick="getOrderList(1, '${orderOrGroup}');">未收全款</a>
				<span>丨</span>
			</dt>
			<dt id="notPaidHead">
				<a href="javascript:void(0)" onclick="getOrderList(2, '${orderOrGroup}');">未收订金</a>
			</dt>
        </dl>
    </li>
	<li id="occupied"><a href="javascript:void(0)" onclick="getOrderList(3, '${orderOrGroup}');">已占位</a></li>
	<!-- C322大洋屏蔽 -->
	<%--<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">--%>
    <%--<c:if test="${office.isShowDeleteOrder == 0}">--%>
    <li id="canceled"><a href="javascript:void(0)" onclick="getOrderList(99, '${orderOrGroup}');">已取消</a></li>
    <%--</c:if>--%>
    <%--<c:if test="${office.isShowDeleteOrder == 0}">--%>
    <li id="deleted"><a href="javascript:void(0)" onclick="getOrderList(111, '${orderOrGroup}');">已删除</a></li>
	<%--</c:if>--%>
	<li id="operator"><a href="javascript:void(0)" onclick="getOrderList(7, '${orderOrGroup}');">待确认订单</a></li>
	<c:if test="${fns:getUser().company.orderPayMode eq 1}">
		<li id="cw"><a href="javascript:void(0)" onclick="getOrderList(8, '${orderOrGroup}');">待财务确认</a></li>
		<li id="cw_cx"><a href="javascript:void(0)" onclick="getOrderList(9, '${orderOrGroup}');">已撤销占位</a></li>
	</c:if>
</content>