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
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <title>团期操作记录</title>

    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic }/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>

    <script src="${ctxStatic}/js/singleOperationRecord.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" ></script>
    <link type="text/css" rel="stylesheet" href="${ctxStatic }/css/operationRecord.css" />
    <%--<script type="text/javascript" src="${ctxStatic}/static/js/jquery.placeholder.min.js"></script>--%>
    <%--<script>--%>
    <%--$(function () {--%>
    <%--//input内添加placeholder属性--%>
    <%--$('input').placeholder();--%>
    <%--})--%>
    <%--</script>--%>
    <script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic }/js/page12.js"></script>

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
	    <div class="title_bold"></div>
	    <input type="hidden" id="groupId" value="${groupId }">
        <input type="hidden" id="sysCtx" value="${ctxStatic}">
	    <div class="title_detail"><label>团号： </label><span class="groupNum"></span><label>出团日期： </label><span class="group_date"></span></div>
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
	        <input type="text" class="inputTxt dateinput" onblur="searchSingleProduct();" id="selectTime" value='${selectTime}' onFocus="WdatePicker();selectTime.blur();" readonly/>
	    </span>
	    <span class="third_select">
	        <input type="text" class="inputTxt dateinput" id="singleTimeBegin" value='${singleTimeBegin}' onFocus="var singleTimeEnd=$dp.$('singleTimeEnd');WdatePicker({onpicked:function(){singleTimeEnd.focus();},maxDate:'#F{$dp.$D(\'singleTimeEnd\',{d:-1});}'})" readonly/>
	        <span>至</span>
	        <input type="text" class="inputTxt dateinput" onblur="searchSingleProduct();" id="singleTimeEnd" value='${singleTimeEnd}' onFocus="WdatePicker({minDate:'#F{$dp.$D(\'singleTimeBegin\',{d:0});}'});singleTimeEnd.blur();" readonly/>
	        </span>
	</div>
<table class="table activitylist_bodyer_table" id="contentTable">
    <thead>
    <tr>
        <th style="width:17%;min-width: 190px">操作时间</th>
        <th style="width:17%;min-width: 190px">操作人</th>
        <th style="width:17%;min-width: 190px">操作项</th>
        <th style="width:17%;min-width: 190px">数量</th>
        <th style="width:32%;min-width: 360px">备注</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<div class="page">
    <div  class="pagination">
    </div>
</div>