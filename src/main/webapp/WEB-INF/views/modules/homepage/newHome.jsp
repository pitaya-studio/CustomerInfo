<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
    <meta charset='utf-8'/>
    <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
    <title>QUAUQ（夸克）旅游交易预订系统</title>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/json/json2.js"></script>
    <![endif]-->
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <!--525需求新增css-->
    <link rel="stylesheet" href="${ctxStatic}/css/newHomePage.css"/>
    <%--<link href="/favicon.ico" rel="shortcut icon" />--%>
    <!--525 end -->
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.css"/>
    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
</head>
<body>
<img  class="waiting-img" id="waitingImg" src="${ctxStatic}/images/cool-load.gif">
<%@ include file="T1Head.jsp" %><!--header end-->
<div class="sea">
    <%--项目根路径--%>
    <input id="ctx" type="hidden"  value="${ctx}">
    <input id="t1img" type="hidden"  value="${t1img}">
    <!--header start-->
    <form  id="homeSearchingForm"   method="post">
        <!--main start-->
        <div class="main">
            <div class="mainHomePage">
                <!--顶部搜索开始-->
                <div class="main_head_div">
                    <span id="company_logo" class="hedear-logo" onclick="reload()"></span>
                    <span class="float_right font_0 ">
                        <input id="keywords" name="keywords"  type="text"
                               placeholder="产品名称 / 供应商 / 团号 / 目的地" value="" />
                        <span class="main_head_search"  onclick="getInToT1Index(this,4);">搜 索</span>
                    </span>
                </div> <!--顶部搜索结束-->
                <!--出境游和国内游开始-->
                <div class="search-container bottom_line">
                    <span id="out"  onclick="getLogo(this);"></span>
                    <span id="home"  onclick="getLogo(this);"></span>
                </div><!--出境游和国内游结束-->
                <!--logo列表和区域展示-->
                <div  style="min-height:100vh;min-height:1000px\0;"  class="homepage_content">

                </div>
            </div>
        </div>
        <!--main end-->
    </form>
    <!--footer start-->
    <%@ include file="t1footer.jsp" %> <!--footer end-->
</div>
<script src="${ctxStatic}/js/placeholder.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/T1HomePage/t1HomeReady.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/t1ForHuiTeng.js"></script>
<script type="text/javascript">

</script>
</body>
</html>
