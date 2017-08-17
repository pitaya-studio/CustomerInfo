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
    <link rel="stylesheet" href="${ctxStatic }/css/dataAdministration.css"/>
</head>
<input id="getCtxStatic" type="hidden" value="${ctxStatic}"/>
<input id='ctx' type="hidden" value="${ctx}">
<div>
    <ul class="Group_tab"></ul>
</div>
<!--页面右侧主体部分-->
<div class="main-right-bottom">

    <div class="Condition_div">
        <ul class="Condition_tab">
            <li value="3" class="active one"><em class="DA_img "></em>订单金额</li>
            <li value="2" class="two"><em class="DA_img "></em>收客人数</li>
            <li value="1" class="three"><em class="DA_img "></em>订单数</li>
        </ul>
    </div>
    <div class="time_div">
        <ul class="time_tab">
            <li class="active front">今日</li>
            <li class="front">本周</li>
            <li class="front">本月</li>
            <li class="front">本年</li>
            <li class="front">全部</li>
            <li class="back"><span class="custom">自定义：</span>
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
        <div class="orderTrend_div_header">
            <div class="font_18 orderTrend_new_header">新增<span>订单</span>趋势
                <div class="pull-right">
                    <em class="left DA_img " title="刷新"></em>
                    <em class="center DA_img">
                        <div class="hover_box">
                            <div class="border_triangle"></div>
                            <div class="color_blue">数据更新时间：</div>
                            <div>2016-12-12 16:16:16</div>
                        </div>
                    </em>

                </div>
            </div>
            <div class="orderTrend_blank"></div>
        </div>
        <div id="containerBox">
            <div class="containerTop">
                <div id="increaseShow">
                </div>
                <div class="orderTrend_right">
                    <div class="time">
                        <div class="active type1">时</div>
                        <div class="type2"></div>
                    </div>
                    <div class="compare">
                        对比：
                        <div id="compare1">
                            <div class="compare_square"></div>
                            <p class="compare1">前一日</p>
                        </div>
                        <div id="compare2">
                            <div class="compare_square"></div>
                            <p class="compare2">上周同期</p>
                        </div>
                    </div>
                    <div class="line">
                        <div class="line_right">
                            <div class="line2"></div>
                            <p class="compare_line2">今日订单</p>
                        </div>

                        <div class="line_center">
                            <div class="line3"></div>
                            <p class="compare_line3">上周同期订单</p>
                        </div>
                        <div class="line_left">
                            <div class="line1"></div>
                            <p class="compare_line1">前一日订单</p>
                        </div>

                    </div>
                </div>
            </div>
            <div id="container" class="container_new"></div>
            <img  id="lineErrorImg" alt=""/>
        </div>
    </div>

    <div class="dataPresentation_footer">
        <div class="pieChart ">
            <div class="pieChart_header">
                <div><span class="header_taggle">本周</span>渠道<span class="taggle_use">收客人数</span>
                    <div class="pull-right">
                        <em class="left DA_img" title="刷新"></em>
                        <em class="center DA_img">
                            <div class="hover_box">
                                <div class="border_triangle"></div>
                                <div class="color_blue">数据更新时间：</div>
                                <div>2016-12-12 16:16:16</div>
                            </div>
                        </em>
                        <%--<shiro:hasPermission name="agent:statistic:detail">--%>
                        <em class="right DA_img href_use" data-id="1" title="更多"></em>
                        <%--</shiro:hasPermission>--%>
                    </div>
                </div>
            </div>
            <div class="pieChart_body" id="pieChart"></div>
            <div class="pieChart_footer">
                <div>
                    <span class="taggle_use_left">其他渠道总</span><span class="taggle_use"></span><span class="taggle_use_right">（包含非签约渠道）：</span><span class="chart_footer"><span
                        id="pie_other"></span><span class="unit">单</span></span>
                </div>
            </div>
        </div>
        <div class="barChart ">
            <div class="barChart_header">
                <div><span class="header_taggle">本周</span>销售<span class="taggle_use">收客人数</span>
                    <div class="pull-right">
                        <em class="left DA_img" title="刷新"></em>
                        <em class="center DA_img">
                            <div class="hover_box">
                                <div class="border_triangle"></div>
                                <div class="color_blue">数据更新时间：</div>
                                <div>2016-12-12 16:16:16</div>
                            </div>
                        </em>
                        <%--<shiro:hasPermission name="agent:statistic:detail">--%>
                        <em class="right DA_img href_use" data-id="2" title="更多"></em>
                        <%--</shiro:hasPermission>--%>
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
                <div><span class="header_taggle">本周</span>产品<span class="taggle_use">收客人数</span>
                    <div class="pull-right">
                        <%--<shiro:hasPermission name="agent:statistic:detail">--%>
                        <em class="right DA_img href_use" data-id="3" title="更多"></em>
                        <%--</shiro:hasPermission>--%>
                    </div>
                </div>
            </div>
            <div class="orderList_body">

            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctxStatic }/js/echarts.min.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/dataAdministration.js"></script>
