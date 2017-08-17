<%--
  Created by IntelliJ IDEA.
  User: ymx
  Date: 2016/10/25
  Time: 9:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="decorator" content="wholesaler" />
    <title>全部团控操作记录</title>

    <link href="${ctxStatic }/css/page-T2.css" type="text/css" rel="stylesheet">
    <link type="text/css" rel="stylesheet" href="${ctxStatic }/css/operationRecord.css" />

    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic }/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>

    <script src="${ctxStatic}/js/wholeOperationRecord.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" ></script>
    <script type="text/javascript" src="${ctxStatic }/js/page12.js"></script>

    <%--<script type="text/javascript" src="${ctxStatic}/static/js/jquery.placeholder.min.js"></script>--%>
    <%--<script>--%>
    <%--$(function () {--%>
    <%--//input内添加placeholder属性--%>
    <%--$('input').placeholder();--%>
    <%--})--%>
    <%--</script>--%>


    <%--<script type="text/javascript">--%>
    <%--function page(n, s) {--%>
    <%--$("#pageNo").val(n);--%>
    <%--$("#pageSize").val(s);--%>
    <%--$("#searchForm").submit();--%>
    <%--return false;--%>
    <%--}--%>
    <%--</script>--%>
    <script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
</head>


<page:applyDecorator name="agent_op_head">

</page:applyDecorator>

<!--右侧内容部分开始-->
<content tag="three_level_menu" id="threeLevelMenu" class="bottom">
    <div class="bottom">
        <span class="bottom-first">团期操作记录</span>
    </div>
</content>


<div class="activitylist_bodyer_right_team_co">
    <div class="activitylist_bodyer_right_team_co2 wpr20 pr">
        <div class="activitylist_team_co3_text">搜索：</div>
        <input placeholder="输入产品名称，团号" class="inputTxt inputTxtlong" id="searchPanelInput">
        <input style="display: none" id="sysCtx" value="${ctxStatic}">
        <%--<span class="text_hint">输入产品名称，团号</span>--%>
    </div>
    <div class="form_submit">
        <input type="button" value="搜索" onclick="$('#selectTime,#wholeTimeBegin,#wholeTimeEnd').attr('value','');searchWholeProduct()" class="btn btn-primary submitButton">
        <input type="button" value="重置搜索" class="btn btn-primary " onclick="clearAllInput();">
    </div>
</div>
<div class="title_bottom">
    <label>查询日期： </label>
    <div class="select_date">
        <input type="text" readonly="readonly" value="全部" id="dateInput" unselectable="on" onkeyup="" onblur="">
        <ul class="date_option"  id="dateOption">
            <li>全部</li>
            <li>按单日</li>
            <li>自定义</li>
        </ul>
    </div>
    <span class="sec_select">
            <input type="text" class="inputTxt dateinput" onblur="searchWholeProduct($(this));" id="selectTime" name="" value='${selectTime}' onFocus="WdatePicker();selectTime.blur();" readonly/>
        </span>
    <span class="third_select">
        <input type="text" class="inputTxt dateinput" id="wholeTimeBegin" name="wholeTimeBegin" value='${wholeTimeBegin}' onFocus="var wholeTimeEnd=$dp.$('wholeTimeEnd');WdatePicker({onpicked:function(){wholeTimeEnd.focus();},maxDate:'#F{$dp.$D(\'wholeTimeEnd\',{d:-1});}'})" readonly/>
        <span>至</span>
        <input type="text" class="inputTxt dateinput" onblur="searchWholeProduct($(this));" id="wholeTimeEnd" name="wholeTimeEnd" value='${wholeTimeEnd}' onFocus="WdatePicker({minDate:'#F{$dp.$D(\'wholeTimeBegin\',{d:0});}'});wholeTimeEnd.blur();" readonly/>
        </span>
</div>
<table class="table activitylist_bodyer_table" id="contentTable">
    <thead>
    <tr>
        <th style="width:13%;min-width: 140px">操作时间</th>
        <th style="width:9%;min-width: 90px">操作人</th>
        <th style="width:30%;min-width: 350px">产品名称</th>
        <th style="width:15%;min-width: 160px">团号</th>
        <th style="width:7%;min-width: 70px">操作项</th>
        <th style="width:7%;min-width: 70px">数量</th>
        <th style="width:19%;min-width: 220px">备注</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>


<%--<div class="pagination clearFix">--%>
<%--${page}--%>
<%--<div style="clear:both;"></div>--%>
<%--</div>--%>

<div class="page">
    <div  class="pagination">
    </div>
</div>

