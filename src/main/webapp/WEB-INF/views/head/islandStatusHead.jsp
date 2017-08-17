<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<script type="application/javascript">
    $(function(){
        var showType = '<sitemesh:getProperty property="showType" />';
        var orderStatus = '<sitemesh:getProperty property="orderStatus" />';
        var current = '<sitemesh:getProperty property="current" />';
        if(current == 'dealList')
        	showType = '89';
        else if(current == 'agingList')
        	showType = '99';
        else if(current == 'payList')
        	showType = '111';
        else if(current == 'visaList')
        	showType = '1000';
        else if(current == 'visaOrder')
        	showType = '1001';
        var mark = '';
        var markTail = '';
        switch (showType)
        {
            case '195' :
                mark = 'paid';
                markTail = 'paidAll';
                break;
            case '196' :
                mark = 'paid';
                markTail = 'paidHead';
                break;
            case '197' :
                mark = 'paid';
                markTail = 'paidVisa';
                break;
            case '198' :
                mark = 'paid';
                markTail = 'paidVisaOrder1';
                break;
            case '199' :
                mark = 'paid';
                markTail = 'paidVisaOrder';
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
<shiro:hasPermission name="order:list:paidForCheck">
<ul class="nav-tabs hasNav">
        <li id="paid" class="ernav active current"><a href="javascript:void(0);">海岛游</a>
        	<dl >
        		<dt id="paidAll"><a href="${ctx}/island/getIslandList/195/1.htm?option=order">全部</a><span>丨</span></dt>
        		<dt id="paidHead"><a href="${ctx}/island/getIslandList/196/1.htm?option=reserve">已上架</a><span>丨</span></dt>
        		<dt id="paidVisa"><a href="${ctx}/island/getIslandList/197/1.htm?option=visa">已下架</a><span>丨</span></dt>
        		<dt id="paidVisaOrder1"><a href="${ctx}/island/getIslandList/198/1.htm?option=visaOrder1">草稿箱</a><span>丨</span></dt>
        		<dt id="paidVisaOrder"><a href="${ctx}/island/getIslandList/199/1.htm?option=visaOrder">已删除</a></dt>
        	</dl>
        </li>
        </ul>
   </shiro:hasPermission>       
</content>