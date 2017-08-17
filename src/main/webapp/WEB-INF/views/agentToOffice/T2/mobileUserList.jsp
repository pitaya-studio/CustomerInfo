<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="decorator" content="wholesaler" />
  <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
  <title>微信账号列表</title>

  <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
  <script type="text/javascript" src="${ctxStatic }/js/jquery-2.1.1.min.js"></script>
  <script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>

  <script type="text/javascript" src="${ctxStatic}/js/jquery.nicescroll.min.js"></script>
  <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js" ></script>
  <script type="text/javascript" src="${ctxStatic}/js/page12.js"></script>
  <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
  <script type="text/javascript" src="${ctxStatic}/js/weChatMatchWindow.js"></script>
  <script type="text/javascript" src="${ctxStatic}/js/weChatAccount.js"></script>
  <script type="text/javascript" src="${ctxStatic }/js/page12.js"></script>

  <link type="text/css" rel="stylesheet" href="${ctxStatic }/css/table.css" />
  <link rel="stylesheet" href="${ctxStatic}/css/weixinAccountMatch.css">
  <link type="text/css" rel="stylesheet" href="${ctxStatic }/css/wechatAccount.css" />
  <link rel="stylesheet" href="${ctxStatic }/css/font-awesome-4.6.3/css/font-awesome.min.css">
  <link rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/jbox.css"/>
</head>

<body>
<page:applyDecorator name="agent_op_head">
</page:applyDecorator>

<!--右侧内容部分开始-->
<content tag="three_level_menu">
  <li class="active"><a href="javascript:void(0);" onclick="switchConnectTab(0);">待关联</a></li>
  <li><a href="javascript:void(0);" onclick="switchConnectTab(1);">已关联</a></li>
  <li><a href="javascript:void(0);" onclick="switchConnectTab(2);">已删除</a></li>
</content>


<!--右侧内容部分开始-->
<form id="searchForm" class="form-search" method="post">
  <input id="pageNo" name="pageNo" type="hidden" value="1">
  <input id="ctx" name="pageNo" type="hidden" value="${ctx}">
  <input id="pageSize" name="pageSize" type="hidden" value="10">


  <div class="activitylist_bodyer_right_team_co">
    <div class="activitylist_bodyer_right_team_co2 pr">
      <input type="text" class="searchInput inputTxtlong inputTxt" name="agentName" placeholder="输入渠道名称" id="agentname">
    </div>
    <div class="zksx">筛选</div>
    <div class="form_submit">
      <input type="button" value=" 搜索 " onclick="searchWechat();" id="seachbutton" class="btn btn-primary ydbz_x">
      <input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件">
    </div>
    <div class="ydxbd">
      <span></span>

      <div class="activitylist_bodyer_right_team_co3">
        <label class="activitylist_team_co3_text">姓名：</label>
        <input type="text" class="inputTxt" id="name">
      </div>

      <div class="activitylist_bodyer_right_team_co3">
        <label class="activitylist_team_co3_text">手机号：</label>
        <input type="text" id="telephone" <%--oninput="onlyInputNum(this);"--%> value="">
      </div>
      <div class="activitylist_bodyer_right_team_co3">
        <label class="activitylist_team_co3_text">微信号：</label>
        <input type="text" class="inputTxt" id="wechatcode">
      </div>
      <div class="activitylist_bodyer_right_team_co1 phone_Num">
        <label class="activitylist_team_co3_text">座机号：</label>
        <input type="text" id="areaCode"> -
        <input type="text" id="phone">
      </div>

    <div class="kong"></div>
  </div>
  </div>
</form>
<div>
  <table class="activitylist_bodyer_table mainTable" id="wechatList">
    <thead>
    <tr>
      <th width="8%">序号</th>
      <th width="33%">旅社名称</th>
      <th class="width_use" width="11%">姓名</th>
      <th class="width_use" width="11%">手机</th>
      <th class="width_use" width="11%">微信号</th>
      <th class="width_use" width="11%">座机</th>
      <th width="25%" class="change_th">所匹配到的登录名</th>
      <th class="we_operate">操作</th>
    </tr>
    </thead>
    <tbody>
    <%--<tr>--%>
      <%--<td>1</td>--%>
      <%--<td class="text_left"><div class="unbind_hint">解除关联--%>
        <%--<div class="unbind_reason">--%>
        <%--<em></em>--%>
          <%--<div class="unbind_reason_title">解除关联原因</div>--%>
        <%--<ul class="unbind_reason_content">--%>
          <%--<li><label>旅社名称：</label><span class="detail_unbind"><span class="before_change">好想你枣业</span> 改为 <span class="after_change">北京奢华之旅国际旅行社有限公司</span></span></li>--%>
          <%--<li><label>微信号：</label><span class="detail_unbind"><span class="before_change">vfssdfdsdfszadsz</span> 改为 <span class="after_change">fgdsrtfdfedsfcdzfdxvfd</span></span></li>--%>
        <%--</ul>--%>
      <%--</div></div> 山西永信天下旅行社</td>--%>
      <%--<td>李大嘴</td>--%>
      <%--<td>15255556666</td>--%>
      <%--<td>fdshkj44fgsufres</td>--%>
      <%--<td>010-22222222</td>--%>
      <%--<td>12345678977777</td>--%>
      <%--<td>--%>
        <%--<em class="fa fa-cog"></em>--%>
        <%--<div class="handle">--%>
          <%--<span></span>--%>
          <%--<ul>--%>
            <%--<li onclick="linkToChannel(138)">关联</li>--%>
            <%--<li>生成新账号</li>--%>
            <%--<li onclick="delete_weChat(99)">删除</li>--%>
          <%--</ul>--%>
        <%--</div>--%>
      <%--</td>--%>
    <%--</tr>--%>
    </tbody>
  </table>
</div>
<!--右侧内容部分结束-->
<%--<div id="customerLevel" class="display-none">--%>
  <%--<div class="content-container" style="max-height: 391px">--%>
  <%--</div>--%>
<%--</div>--%>
<div class="page">
  <div  class="pagination">
  </div>
</div>
<script type="text/javascript" src="${ctxStatic}/js/weChatMatchWindow.js"></script>
<script>
  //UG_V2 added for 展开/收起筛选
  $(function() {
    //搜索条件筛选
    launch();
  });
</script>
</body>
</html>