<%--
  Created by IntelliJ IDEA.
  User: wanglijun
  Date: 2016/10/17
  Time: 16:00
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <title></title>
    <title>成本付款</title>
    <meta name="decorator" content="wholesaler"/>
    <link rel="stylesheet" href="../css/page-T2.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/mtour/static/js/common/jquery-ui.js"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/page12.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/differenceDetail.js"></script>
</head>
<body>
<input id="ctx" type="hidden" value="${ctx}"/>
    <div id="searchForm" class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2">
            <input id="groupCode" placeholder="输入产品名称、团号、订单号、销售"   name="groupCode" class="txtPro inputTxt searchInput" style="vertical-align: top;" value=""/>
        </div>
        <div class="zksx">筛选</div>
        <div class="form_submit">
            <input class="btn btn-primary ydbz_x "  type="button" onclick="queryMarginInCondition()" value="搜索"/>
            <input class="btn ydbz_x" type="button" onclick="resetForm()" value="清空所有条件" />
        </div>

        <div class="ydxbd">
            <span></span>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">渠道名称：</label>
                <select id="agentifo" name="agentifo">
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">渠道联系人：</label>
                <select id="agentUser" name="agentUser">
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">付款状态：</div>
                <div class="selectStyle">
                    <select id="payType" name="payType">
                        <option value="" >全部</option>
                        <option value="0">未付款</option>
                        <option value="1" >已付款</option>
                    </select>
                </div>
            </div>
        </div>
    </div>

<table id="contentTable" class="table activitylist_bodyer_table mainTable">
    <thead>
    <tr>
        <th width="6%">序号</th>
        <th width="9%">产品名称</th>
        <th width="9%">团号</th>
        <th width="6%">订单号</th>
        <th width="9%">订单人数</th>
        <th width="9%">销售</th>
        <th width="9%">渠道商</th>
        <th width="6%">渠道联系人</th>
        <th width="6%">预计门店结算价差额返还</th>
        <th width="6%">到账门店结算价差额返还</th>
        <th width="6%">付款状态</th>
    </tr>
    </thead>
    <tbody id="tbody">
         <tr id="loading" style="display: none">
             <td colspan="11" style="text-align:center">
                 <img class="loading-gif"/></span>
                 <span>请稍等，正在为您进行实时查询</span>
             </td>
         </tr>
        <tr id="noResult" style="display: none">
            <td  colspan="11" style="text-align: center;">
                暂无搜索结果
            </td>
        </tr>

    </tbody>
</table>
    <div id="contentTable_foot" style="display: none">
        <label><input check-action="All" id="checkAll"  type="checkbox"> 全选</label>
        <label><input check-action="Reverse"  id="reverseCheck" type="checkbox"> 反选</label>
        <input type="button"  class="btn-primary pay paymentConfirm" value="已付款" onclick="paymentConfirm(1)"/>
        <input type="button"  class="btn-primary unpay paymentConfirm" value="未付款" onclick="paymentConfirm(0)" />
    </div>

<div class="page">
    <div  class="pagination">
    </div>
</div>
</body>
</html>