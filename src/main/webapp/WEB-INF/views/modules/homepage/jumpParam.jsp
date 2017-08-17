<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
    <meta  http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta  charset='utf-8' />
    <link rel="stylesheet" href="${ctxStatic}/css/common.css"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/newHomePage.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <%--筛选项样式表--%>
    <link rel="stylesheet" href="${ctxStatic}/css/search.css"/>
    <%--产品页样式表--%>
    <link rel="stylesheet" href="${ctxStatic}/css/t1-product.css"/>
    <c:choose>
        <c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
            <link href="${ctxStatic}/images/huiTeng/huiTengFavicon.ico" rel="shortcut icon"/>
            <title>北京辉腾国际旅行社有限公司</title>
        </c:when>
        <c:when test="${fn:contains(pageContext.request.requestURL,'travel.jsjbt')}">
            <link href="${ctxStatic}/images/jinLing/jinLingFavicon.ico" rel="shortcut icon"/>
            <title>江苏金陵商务国际旅行社有限责任公司</title>
        </c:when>
        <c:otherwise>
            <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
            <title>QUAUQ（夸克）旅游交易预订系统</title>
        </c:otherwise>
    </c:choose>

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/normalize.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/viewer.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/main.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/animate.min.css">

    <!--字体图标-->
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/newHomePage.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1ProductList.css"/>
    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1home.js"></script>
    <%--产品页js--%>
    <script type="text/javascript" src="${ctxStatic}/js/t1-product.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/page.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1ProductList.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/jPages.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>

</head>
<body>
<!--header start-->
<%@ include file="T1Head.jsp"%><!--header end-->
<!--页面主体部分开始-->
<div class="wrapper"  id="sea">
    <%--项目根路径--%>
    <input id="ctx" type="hidden"  value="${ctx}">
    <input id="t1img" type="hidden"  value="${t1img}">
    <input id="ctxStatic" type="hidden"  value="${ctxStatic}">
    <input id="pageDisplay" type="hidden"  value="${pageDisplay}">
    <input id="jspId" type="hidden"  value="theSecondPage">
    <!--跳转时返回的首页选中参数开始--><!--此处为测试数据，已接口返回数据为准-->
    <input type="hidden" id="tabText">
    <input type="hidden" id="travelAreaId">
    <input type="hidden" id="travelAreaName">
    <input type="hidden" id="travelInOutId">
    <input type="hidden" id="travelInOutName">
    <!--跳转时返回的首页选中参数结束-->
        <%@ include file="supplierAndSearch.jsp"%><!--悬浮窗 end-->

  <%--  <!--顶部搜索框开始-->
    <div class="main_head_div">
        <span id="company_logo" class="" onclick="reload()"></span>
        <span class="float_right font_0 ">
            <input id="keyword" data_from="jumpParam" name="keyword" value="" type="text" placeholder="">
            <span class="main_head_search" onclick="rakeList();">搜 索</span>
        </span>
    </div><!--顶部搜索框结束-->
    <!--顶部tab页和悬浮窗口开始-->
    <div  id="topTab_jumpParam" style="display:none" class="suspend-tab">
        <!--top-tab开始-->
        <div  class="top-tab">
            <i class="fa fa-th-list" aria-hidden="true"></i>
            <span id="reginType"></span>
            <span id="regionSelect">选择<i class="fa fa-angle-down" aria-hidden="true"></i></span>
        </div><!--top-tab结束-->
        <!--top-suspend开始-->
        <div class="top-suspend">
            <img  class="waiting-img  sus" id="waitingImgSus" src="${ctxStatic}/images/cool-load.gif">
        </div><!--top-suspend结束-->
    </div><!--顶部tab页和悬浮窗口结束-->--%>

    <!--多选项筛选开始-->
      <div id="productList">
        <div class="search-container">
            <img  class="waiting-img" id="waitingImg" src="${ctxStatic}/images/cool-load.gif">
        </div><!--多选项筛选结束-->
        <!--显示更多筛选条件开始-->
        <div  class="more-screen">显示更多筛选条件<i class="fa fa-angle-double-down" aria-hidden="true"></i></div><!--显示更多筛选条件结束-->
          <div id="tab">
              <span <c:if test="${listFlag eq 1}">class="activeList"</c:if> id="productListTab"><i class="fa fa-reorder" aria-hidden="true"></i>产品列表</span>
              <span <c:if test="${listFlag ne 1}">class="activeList"</c:if> id="groupListTab"><i class="fa fa-calendar"></i>团期列表</span>
              <input type="button" onclick="setViewMode()" value="设置默认查看方式">
          </div>
      </div>
        <!--列表排序开始-->
        <div class="rank">
            <span class="float_left ">排序：</span>
            <span id="defaultSort" class="float_left rank_child_left rank_child_left_two" onclick="sortby('defaultSort',this)">默认 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
            <span id="activityDuration" class="float_left rank_child_left rank_child_left_two" onclick="sortby('activityDuration',this)">行程天数 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
            <span id="groupOpenDate" class="float_left rank_child_left" onclick="sortby('groupOpenDate',this)">出团日期 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
            <span id="quauqPrice" class="float_left rank_child_left" onclick="sortby('quauqPrice',this)">结算价 <i class="relative"><em class="t1_2 rank_up"></em><em class="t1_2 rank_down"></em></i></span>
            <span id="page2" class="float_right"><span class="rank_product_right">共 <span class="orange font_14"  id="totalList"></span> 条</span><em class="orange_left t1_2"  onclick="culPage(this,true)"></em>
            <span class="orange"  id="orange"></span><span>/</span><span  id="totalPage"></span><em class="orange_right t1_2" onclick="culPage(this,false)"></em>
       </span>
        </div><!--列表排序结束-->
        <!--列表开始1-->
        <table class="table_width table_width_hover table_width_collapse">
            <thead class="groupOrder J_m_nav groupHomerOrder " id="J_m_nav">
            </thead>
            <tbody id="content">
            </tbody>
        </table><!--列表结束1-->
        </div><!--页面主体部分结束-->
        <!--分页开始-->
        <div class="page">
            <div class="pagination">
                <div id="page" class="endPage">
                </div>
            </div>
        </div><!---分页结束-->
        <div id="productDetail" style="display:none">123</div>



<!--footer start-->
<%@ include file="t1footer.jsp"%><!--footer end-->
<%--<script type="text/javascript" src="${ctxStatic}/js/T1HomePage/t1HomeReady.js"></script>--%>
<script type="text/javascript" src="${ctxStatic}/js/t1ForHuiTeng.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/placeholder.js"></script>
<div id="script"></div>
</body>