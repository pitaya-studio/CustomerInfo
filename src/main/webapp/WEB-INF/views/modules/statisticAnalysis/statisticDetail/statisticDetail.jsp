<%--
  Created by IntelliJ IDEA.
  User: ymx
  Date: 2016/12/23
  Time: 14:04
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
    <script type="text/javascript" src="${ctxStatic}/js/statisticDetail/dataManageAnalysis.js"></script>

    <link type="text/css" rel="stylesheet" href="${ctxStatic }/css/statisticDetail/dataManageAnalysis.css" />
    <link rel="stylesheet" href="${ctxStatic }/css/font-awesome-4.6.3/css/font-awesome.min.css">

</head>

<body>
<page:applyDecorator name="agent_op_head">
</page:applyDecorator>

<!--右侧内容部分开始-->
    <content tag="three_level_menu">
        <li class="active"><a href="javascript:void(0);" onclick="switchTab(1);">渠道分析</a></li>
        <li><a href="javascript:void(0);" onclick="switchTab(2);">销售分析</a></li>
        <li><a href="javascript:void(0);" onclick="switchTab(3);">产品分析</a></li>
    </content>

    <div class="tab_change">
        <span >今日</span>
        <span>本周</span>
        <span>本月</span>
        <span>本年</span>
        <span>全部</span>
        <span class="date-text">
                    自定义：
                    <input type="text" class="dateinput" id="startDate" name="startDate"  onFocus="var endDate=$dp.$('endDate');WdatePicker({onpicked:function(){endDate.focus();},maxDate:'#F{$dp.$D(\'endDate\',{d:-1});}'});showButton();" readonly/>
					至
					<input type="text" class="dateinput" id="endDate" name="endDate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'startDate\',{d:0});}'});endDate.blur();showButton();" readonly/>

                    <input type="button" onclick="customized()" id="sureButton" style="display: none" value="确定">
                </span>
    </div>
    <!--右侧内容部分开始-->
    <div class="activitylist_bodyer_right_team_co_bgcolor">
        <input type="hidden" value="${ctx}" id="ctx">
        <input type="hidden" value="${pageTab}" id="pageTab">
        <input type="hidden" value="${orderType}" id="orderTypeF">
        <input type="hidden" value="${analysisType}" id="analysisTypeF">
        <input type="hidden" value="${searchDate}" id="searchDateF">
        <input type="hidden" value="${startDate}" id="startDateF">
        <input type="hidden" value="${endDate}" id="endDateF">
        <form id="searchForm" action="" method="post">
            <input id="activityIds" type="hidden" name="activityIds" value=""/>
            <div class="activitylist_bodyer_right_team_co">
                <%--bug17521 去掉搜索框前字段 改变筛选按钮位置 调整筛选项 --%>
                <div class="activitylist_bodyer_right_team_co2 pr">
                    <%--<div class="activitylist_team_co3_text">搜索：</div>--%>
                    <input class="txtPro inputTxt searchInput" flag="istips" type="text" />
                    <span class="ipt-tips">输入团号、订单号</span> </div>
                <a class="zksx">筛选</a>
                <div class="form_submit">
                    <input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="searchAllSearch()" value="搜索">
                    <input class="btn ydbz_x" type="button" onclick="resetAllSearch()" value="清空所有条件">
                </div>
                <div class="ydxbd">
                    <span></span>
                    <div class="activitylist_bodyer_right_team_co3">
                        <label class="activitylist_team_co3_text">产品类型：</label>
                        <div class="selectStyle">
                            <select id="orderTypeId">
                                <option value="0">全部</option>
                            </select>
                        </div>
                    </div>
                    <div class="activitylist_bodyer_right_team_co1">
                        <label class="activitylist_team_co3_text">订单数：</label>
                        <input value="" id="orderNumStart" oninput="inputint(this)" class="inputTxt" type="text">
                        - <input value="" id="orderNumEnd" oninput="inputint(this)" class="inputTxt" type="text"> 单
                    </div>
                    <div class="activitylist_bodyer_right_team_co1">
                        <label class="activitylist_team_co3_text">收客人数：</label>
                        <input value="" id="orderPersonNumStart" oninput="inputint(this)" class="inputTxt" type="text">
                        -
                        <input value="" id="orderPersonNumEnd" oninput="inputint(this)" class="inputTxt" type="text"> 人
                    </div>
                    <div class="activitylist_bodyer_right_team_co1">
                        <label class="activitylist_team_co3_text">订单金额：</label>
                        <input value="" id="orderMoneyStart" oninput="inputNum(this)" class="inputTxt" type="text">
                        - <input value="" id="orderMoneyEnd" oninput="inputNum(this)" class="inputTxt" type="text"> 元
                    </div>
                    <div class="kong"></div>
                </div>
                <div class="kong"></div>
            </div>
        </form>
    </div>
    <!--查询结果筛选条件排序开始-->
    <div class="activitylist_bodyer_right_team_co_paixu">
        <div class="activitylist_paixu">
            <div class="activitylist_paixu_left">
                <ul>
                    <li>合计：</li>
                    <li>订单总数：<span class="red_count_style"></span></li>
                    <li>收客总人数：<span class="red_count_style"></span></li>
                    <li>订单总金额：<span class="red_count_style"></span></li>
                </ul>
            </div>
            <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>8</strong>&nbsp;条</div>
            <div class="kong"></div>
        </div>
    </div>
    <!--查询结果筛选条件排序结束-->
    <div>
        <table class="table_third">
            <thead>
            <tr>
                <th style="min-width: 110px">序号</th>
                <th style="min-width: 510px" class="title_of_type">渠道名称</th>
                <th style="min-width: 190px"><span class="sort_thead">订单数 <i class="fa fa-caret-up"></i><i class="fa fa-caret-down sel_sort"></i></span></th>
                <th style="min-width: 160px"><span class="sort_thead">收客人数 <i class="fa fa-caret-up"></i><i class="fa fa-caret-down"></i></span></th>
                <th class="text_right_side" style="min-width: 100px"><span class="sort_thead">订单金额 <i class="fa fa-caret-up"></i><i class="fa fa-caret-down"></i></span></th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td colspan='5'>暂无查询数据</td>
            </tr>
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