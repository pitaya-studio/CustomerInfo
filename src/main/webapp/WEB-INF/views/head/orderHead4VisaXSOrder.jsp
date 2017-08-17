<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="application/javascript">
    $(function(){
        var showType = '<sitemesh:getProperty property="showType" />';
        var mark = '';

		// 如果有已取消订单tab页面的显示许可权限，且选择此tab页，则显示此“已取消”
		// 如果没有任何特殊tab页面显示许可权限，或者选择此tab页，则显示此“全部订单”
        switch (showType)
        {
            case '0' :
                mark = 'all';
                break;
            case '99' :
                mark = 'canceled';
                break;
        }
        if(mark != ''){
            $('#' + mark).addClass('active');
        }
    });
</script>
<content tag="three_level_menu">

    <li id="all">
    	<a href="javascript:void(0)" onclick="getVisaOrderList(0, '${showList}');">全部订单</a>
    </li>
    
    <shiro:hasPermission name="visaOrderXS:list:cancelorder">
        <li id="canceled"><a href="javascript:void(0)" onclick="getVisaOrderList(99, '${showList}');">已取消</a></li>
    </shiro:hasPermission>
    
</content>