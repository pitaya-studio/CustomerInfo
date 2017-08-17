<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="decorator" content="wholesaler"/>
    <title>订单列表</title>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/channelPricing.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/channelPricingNew.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/placeholder.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/page12.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" ></script>
    <script type="text/javascript" src="${ctxStatic}/js/orderInform.js" ></script>
    <script type="text/javascript" src="${ctxStatic}/agentToOffice/t2/bookOrder.js" ></script>
</head>
<body>
<!-- 搜索查询 -->
<input id="ctx" type="hidden" value="${ctx}"/>
<div id="searchForm" class="activitylist_bodyer_right_team_co">
    <div class="activitylist_bodyer_right_team_co2 ">
       <input id="groupCode" placeholder="输入产品名称、团号" name="groupCode" class="txtPro inputTxt searchInput vaTop"
                                 value="${groupCode }"/>
    </div>
    <div class="zksx">筛选</div>
    <div class="form_submit">
        <input class="btn btn-primary ydbz_x" type="button" onclick="queryMarginInCondition()" value="搜索"/>
        <input
                class="btn ydbz_x" type="button"
                onclick="resetForm()" value="清空所有条件"/>
    </div>
    <div class="ydxbd">
        <span></span>
        <div class="activitylist_bodyer_right_team_co1">
            <label class="activitylist_team_co3_text">渠道名称：</label>
            <select name="channelName" id="channelName">
            </select>
        </div>
        <div class="activitylist_bodyer_right_team_co1">
            <label class="activitylist_team_co3_text">登录账号：</label>
            <select id="loginNumber" name="loginNumber">
            </select>
        </div>
        <div class="activitylist_bodyer_right_team_co1">
            <label class="activitylist_team_co3_text">下单状态：</label>
            <div class="selectStyle">
            <select name="orderStatus" id="orderStatus">
            </select>
            </div>
        </div>
        <div class="activitylist_bodyer_right_team_co1">
            <label class="activitylist_team_co3_text">下单时间：</label>
            <input type="text" class="inputTxt dateinput" id="orderTimeBegin" name="orderTimeBegin" value='${orderTimeBegin}' onFocus="var orderTimeEnd=$dp.$('orderTimeEnd');WdatePicker({onpicked:function(){orderTimeEnd.focus();},maxDate:'#F{$dp.$D(\'orderTimeEnd\',{d:-1});}'})" readonly/>
            <span style="font-size:12px; font-family:'宋体';">至</span>
            <input type="text" class="inputTxt dateinput" id="orderTimeEnd" name="orderTimeEnd" value='${orderTimeEnd}' onFocus="WdatePicker({minDate:'#F{$dp.$D(\'orderTimeBegin\',{d:0});}'})" readonly/>

        </div>
    </div>
</div>
<%--下面表格--%>
<div class="table-lists Mrn MinW">
    <table style="width:100%;" id="ProTab">
        <thead>
        <tr class="table-lists-header">
            <th style="min-width:60px;" class="tc">序号</th>
            <th style="min-width:150px;">提交编号</th>
            <th style="min-width:200px;">下单信息</th>
            <th style="min-width:50px;">人数</th>
            <th style="min-width:150px;">渠道名称</th>
            <th style="width:200px;">登录账号</th>
            <th style="min-width:250px;">价格信息</th>
            <th style="width:110px;" class="tc">操作</th>
        </tr>
        </thead>
        <tbody id="tbody">
        <tr id="noResult" style="display: none">
            <td  colspan="11" style="text-align: center;">
                暂无搜索结果
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div class="page">
    <div  class="pagination">
    </div>
</div>
</body>
</html>

