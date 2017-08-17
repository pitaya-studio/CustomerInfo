<%--
  Created by changying huo IDEA.
  User: quauq
  Date: 2016/12/28
  Time: 13:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <title>数据管理</title>
    <link rel="stylesheet" href="${ctxStatic }/css/statisticsPage.css"/>
</head>
<input id="getCtxStatic" type="hidden" value="${ctxStatic}"/>
<input id='ctx' type="hidden" value="${ctx}">
<div>
    <ul class="Group_tab">
        <li value="dd" class="active" id="order_use">订单总览</li>
        <li value="xd" id="inquiry_use">询单总览</li>
    </ul>
</div>
<!--页面右侧主体部分-->
<div class="main-right-bottom">
    <div class="Condition_div Condition_div_use">
        <ul class="Condition_tab">
            <li value="2" class="active two"><em class="DA_img "></em>收客人数</li>
            <li value="3" class="one"><em class="DA_img "></em>订单金额</li>
            <li value="1" class="three"><em class="DA_img "></em>订单数</li>
        </ul>
    </div>
    <div class="time_div">
        <ul class="time_tab">
            <li class="front" data-searchDate="5">全部</li>
            <li class="front" data-searchDate="1">今日</li>
            <li class="front" data-searchDate="-1">昨日</li>
            <li class="active  front" data-searchDate="3">本月</li>
            <li class="front" data-searchDate="-3">上月</li>
            <li class="front" data-searchDate="4">本年</li>
            <li class="front" data-searchDate="-4">去年</li>
            <li class="back"><span class="custom" data-searchDate="custom">自定义：</span>
                <input type="text" class="inputTxt dateinput" id="orderTimeBegin" name="orderTimeBegin"
                       onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'orderTimeEnd\')||\'%y-%M-%d\'}'})"
                       readonly="">
                <span>&nbsp至 </span>
                <input type="text" class="inputTxt dateinput" id="orderTimeEnd" name="orderTimeEnd"
                       onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderTimeBegin\')}',maxDate:'%y-%M-%d'})" readonly="">
                <input type="button" class="make_sure" name="orderTimeSure" value="确定"/>
            </li>
        </ul>
    </div>
    <div class="orderTrend_div">
        <div class="line_left">
            <div class="line_left_header">
                <span class="header_use"></span>
                <span class="timeUse">本月</span>新增<span class="analysisType">订单数</span>
                <em class="center DA_img">
                    <div class="hover_box">
                        <div class="border_triangle border_triangle_left"></div>
                        <div class="color_blue colorRefresh" >数据更新时间：</div>
                        <div id="refreshTime">2016-12-12 16:16:16</div>
                    </div>
                </em>
            </div>
            <div class="line_left_body">
                <div class="loading_img"></div>
                <div style="display: none">
                    <div class="count_big color_use" id="count"></div>
                    <div class="incease_rate">
                        增长率：<span class="inceaseUse">30%</span> <span class="DA_img up"></span>
                    </div>
                </div>
            </div>
        </div>
        <div class="line_right">
            <div class="right_header">
                <div class="left">订单数（单）</div>
                <div class="right">
                    <span  id="year">年</span>
                    <span class="active" id="month">月</span>
                </div>
            </div>
            <div class="right_body" id="lineCharts">

            </div>
        </div>
    </div>

    <div class="dataPresentation_footer">
        <div class="pieChart ">
            <div class="pieChart_header">
                <div><span class="header_taggle">渠道占比</span>
                    <div class="pull-right">
                        <shiro:hasPermission name="agent:statistic:detail">
                            <a target="_blank"><span>详情&nbsp</span><i class="fa fa-angle-double-right"></i></a>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
            <div class="pieChart_body" id="pieChart"></div>
            <%--<div class="pieChart_footer">
                <div>
                    <span class="taggle_use_left">其他渠道总</span><span class="taggle_use">收客人数</span>：<span class="chart_footer"><span
                        id="pie_other"></span><span class="unit">单</span></span>
                </div>
            </div>--%>
        </div>
        <div class="barChart ">
            <div class="barChart_header">
                <div><span class="header_taggle">销售统计</span>
                    <div class="pull-right">
                        <shiro:hasPermission name="agent:statistic:detail">
                            <a target="_blank"><span>详情&nbsp</span><i class="fa fa-angle-double-right"></i></a>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
            <div class="barChart_body" id="barChart"></div>
            <div class="barChart_footer">
                <div>
                    其他销售总<span class="taggle_use">收客人数</span>：<span class="chart_footer"><span
                        id="bar_other"></span><span class="unit">单</span></span>
                </div>
            </div>
        </div>
        <div class="orderList ">
            <div class="orderList_header">
                <div><span class="header_taggle">产品统计</span>
                    <div class="pull-right">
                        <shiro:hasPermission name="agent:statistic:detail">
                            <a target="_blank"><span>详情&nbsp</span><i class="fa fa-angle-double-right"></i></a>
                        </shiro:hasPermission>
                    </div>
                </div>
            </div>
            <div class="orderList_body"></div>
            <div class="orderList_footer">
                <div>
                    其它产品总<span class="taggle_use">收客人数</span>：<span class="chart_footer"><span
                        id="orderList_other"></span><span class="unit"></span></span>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic }/js/echarts.min.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/statisticsPage.js"></script>
