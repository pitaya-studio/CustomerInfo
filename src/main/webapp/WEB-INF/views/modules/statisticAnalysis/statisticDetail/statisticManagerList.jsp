<%--
  Created by IntelliJ IDEA.
  User: ymx
  Date: 2017/3/7
  Time: 12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="decorator" content="wholesaler" />
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>数据管理列表详情</title>

    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic }/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>

    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" ></script>
    <script type="text/javascript" src="${ctxStatic}/js/page12.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/statisticDetail/statisticManageList.js"></script>

    <link type="text/css" rel="stylesheet" href="${ctxStatic }/css/statisticDetail/dataManageAnalysis.css" />
    <link rel="stylesheet" href="${ctxStatic }/css/font-awesome-4.6.3/css/font-awesome.min.css">

</head>

<body>
<page:applyDecorator name="agent_op_head">
</page:applyDecorator>

<!--右侧内容部分开始-->
<content tag="three_level_menu">
    <div class="bottom">
        <span class="bottom-first">渠道详情</span>
    </div>
</content>
<!--右侧内容部分开始-->
<div class="activitylist_bodyer_right_team_co_bgcolor">
    <input type="hidden" value="${ctx}" id="ctx">
    <input type="hidden" value="${pageTab}" id="pageTab">
    <input type="hidden" value="${searchDate}" id="searchDateF">
    <input type="hidden" value="${startDate}" id="startDateF">
    <input type="hidden" value="${endDate}" id="endDateF">
    <input type="hidden" value="${analysisType}" id="analysisType">

    <input id="activityIds" type="hidden" name="activityIds" value=""/>
    <div class="activitylist_bodyer_right_team_co" style="padding-top: 0">
        <div class="activitylist_bodyer_right_team_co2 pr wpr20">
            <input id="search_context" class="txtPro" flag="istips" type="text" />
            <span class="ipt-tips" style="left: 5px;">请输入渠道名称</span>
        </div>
	    <div class="form_submit">
	    	<input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="searchInput()" value="搜索">
	        <input class="btn btn-primary ydbz_x" type="button" onclick="exportExcel()" value="导出Excel">
	        <%-- 导出excel 使用表单提交  --%>
	        <form id="excelform" style="display: none;">
	        	<input name="param" id="excelparam" value="">
	        </form>
	    </div>
    </div>
    <div class="tab_change margin_bottom">
        <span search-data="5">全部</span>
        <span search-data="1">今日</span>
        <span search-data="-1">昨日</span>
        <span search-data="3">本月</span>
        <span search-data="-3">上月</span>
        <span search-data="4">本年</span>
        <span search-data="-4">去年</span>
        <span class="date-text">
            自定义：
            <input type="text" class="dateinput" id="startDate" name="startDate"  onFocus="var endDate=$dp.$('endDate');WdatePicker({onpicked:function(){endDate.focus();},maxDate:'%y-%M-%d'});showButton();" readonly/>
            至
            <input type="text" class="dateinput" id="endDate" name="endDate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\',{d:0});}',maxDate:'%y-%M-%d'});endDate.blur();showButton();" readonly/>

            <input type="button" onclick="customized()" id="sureButton" style="display: none" value="确定">
        </span>
    </div>
</div>
<div>
    <table class="table_third">
        <thead>
        <tr>
            <th style="min-width: 80px;width:10%;">排名</th>
            <th style="min-width:250px;width:34%;" class="title_of_type">渠道名称</th>
            <th style="min-width:110px;width:14%;" class="text_right"><span class="sort_thead">订单数量（单）<i class="fa fa-caret-up" order-by="2" ></i><i class="fa fa-caret-down" order-by="1"></i></span></th>
            <th style="min-width:110px;width:14%;" class="text_right"><span class="sort_thead">收客人数（人）<i class="fa fa-caret-up" order-by="6"></i><i class="fa fa-caret-down" order-by="5"></i></span></th>
            <th style="min-width:110px;width:14%;" class="text_right"><span class="sort_thead">订单金额（元）<i class="fa fa-caret-up" order-by="4"></i><i class="fa fa-caret-down" order-by="3"></i></span></th>
            <th style="min-width:110px;width:14%;" class="text_right"><span class="sort_thead">询单数量（单）<i class="fa fa-caret-up" order-by="8"></i><i class="fa fa-caret-down" order-by="7"></i></span></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td colspan='6'>暂无查询数据</td>
        </tr>
        <%--<tr>--%>
            <%--<td><span class="sort_logo first_logo"></span>1</td>--%>
            <%--<td>拉美图</td>--%>
            <%--<td>300</td>--%>
            <%--<td>90</td>--%>
            <%--<td>38269.05</td>--%>
            <%--<td>90</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td><span class="sort_logo second_logo"></span>2</td>--%>
            <%--<td>拉美图</td>--%>
            <%--<td>300</td>--%>
            <%--<td>90</td>--%>
            <%--<td>38269.05</td>--%>
            <%--<td>90</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td><span class="sort_logo third_logo"></span>3</td>--%>
            <%--<td>拉美图</td>--%>
            <%--<td>300</td>--%>
            <%--<td>90</td>--%>
            <%--<td>38269.05</td>--%>
            <%--<td>90</td>--%>
        <%--</tr>--%>
        <%--<tr>--%>
            <%--<td>4</td>--%>
            <%--<td>拉美图</td>--%>
            <%--<td>300</td>--%>
            <%--<td>90</td>--%>
            <%--<td>38269.05</td>--%>
            <%--<td>90</td>--%>
        <%--</tr>--%>
        </tbody>
    </table>
</div>
<!--右侧内容部分结束-->


<div class="page">
    <div  class="pagination">
    </div>
</div>
</body>
</html>