<%--
  Created by IntelliJ IDEA.
  User: wanglijun
  Date: 2016/11/23
  Time: 9:44
  To change this template use File | Settings | File Templates.

--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>--%>
<link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
<input id="ctx" type="hidden" value="${ctx}"/>
<input id="ctxStatic" type="hidden" value="${ctxStatic}"/>
<!--顶部搜索框开始-->
<div class="main_head_div">
    <span id="company_logo" class="" onclick="goHomePage('${ctx}')"></span>
    <span class="float_right font_0 ">
            <input id="keyword" data_from="jumpParam"  name="keyword" value="" type="text" placeholder="">
            <span class="main_head_search" onclick="rakeList();">搜 索</span>
        </span>
</div><!--顶部搜索框结束-->
<!--顶部tab页和悬浮窗口开始-->
<div id="topTab_jumpParam" style="display:none" class="suspend-tab">
    <!--top-tab开始-->
    <div class="top-tab">
        <i class="fa fa-th-list" aria-hidden="true"></i>
        <span id="reginType"></span>
        <span id="regionSelect">选择<i class="fa fa-angle-down" aria-hidden="true"></i></span>
    </div>
    <!--top-tab结束-->
    <!--top-suspend开始-->
    <div class="top-suspend">
        <img class="waiting-img  sus" id="waitingImgSus" src="${ctxStatic}/images/cool-load.gif">
    </div><!--top-suspend结束-->
</div><!--顶部tab页和悬浮窗口结束-->
<script type="text/javascript" src="${ctxStatic}/js/t1/supplierAndSearch.js"></script>