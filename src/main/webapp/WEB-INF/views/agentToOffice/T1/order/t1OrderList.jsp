<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page import="com.trekiz.admin.common.config.Context" language="java" %>
<html>
<head>
    <!--[if !IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=8,Chrome=1"/>
    <![endif]-->
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <![endif]-->

    <c:choose>
        <c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
            <link href="${ctxStatic}/images/huiTeng/huiTengFavicon.ico" rel="shortcut icon"/>
        </c:when>
        <c:when test="${fn:contains(pageContext.request.requestURL,'travel.jsjbt')}">
            <link href="${ctxStatic}/images/jinLing/jinLingFavicon.ico" rel="shortcut icon"/>
        </c:when>
        <c:otherwise>
            <link href="http://t215.quauqsystem.com.cn/favicon.ico" rel="shortcut icon"/>
        </c:otherwise>
    </c:choose>

    <link href="css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <%@ include file="orderHeadInfo.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/placeholder.js"></script>

    <script type="text/javascript">
        function page(n, s) {
            /* searchConditions(); */
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
        }

        $(function () {
            $.ajax({
                type: "POST",
                url: "${ctx}/t1/orderList/manage/countHasSeen",
                data: {},
                success: function (data) {
                    if (data.hasSeenCount && data.hasSeenCount > 0) {
                        $(".hint").css('display', 'block');
                        $(".hint").html(data.hasSeenCount);
                    } else {
                        $(".hint").css('display', 'none');
                    }
                }
            });
        });

        function searchByPayStatus(obj, status) {
            $("#threeSerch").val("");
            resertFilter();
            var setTab = $(obj).attr("name");
            $("#setTab").val(setTab);
            $("#payStatus").val(status);
            $("#searchForm").submit();
        }

        $(window).load(function () {
            $("#setTab01").removeClass("group_nav_child_active");
            var setTab = "#" + $("#setTab").val();
            //alert(setTab);
            $(setTab).attr("class", "group_nav_child group_nav_child_active");
        });

        //是否符合金额规则
        function isMoney(obj) {
            var rr = $(obj).val();
            var rule = /^[^0-9|\.]$/;
            if (rr.length > 0) {
                var newStr = '';
                //过滤掉非字（不过滤小数点）
                for (var i = 0; i < rr.length; i++) {
                    var c = rr.substr(i, 1);
                    if (!rule.test(c)) {
                        newStr += c;
                    }
                }
                if (newStr != '') {
                    //只能有一个小数点，并去掉多余的0
                    var szfds = newStr.split('.');
                    var zs = '';
                    var xs = '';
                    if (szfds.length > 1) {
                        zs = szfds[0];
                        xs = szfds[1];
                        for (var i = 1; i < zs.length; i++) {
                            var zs_char = zs.substr(0, 1);
                            if (zs_char == '0') {
                                zs = zs.substring(1, zs.length);
                            }
                        }
                        //保留两位小数
                        if (xs.length > 2) {
                            xs = xs.substring(0, 2);
                        }
                        newStr = zs + '.' + xs;
                    } else {
                        zs = szfds[0];
                        for (var i = 1; i < zs.length; i++) {
                            var zs_char = zs.substr(0, 1);
                            if (zs_char == '0') {
                                newStr = zs.substring(1, zs.length);
                            }
                        }
                    }

                    //'.'之前没有数字会自动补0
                    if (newStr.indexOf('.') == 0) {
                        newStr = '0' + newStr;
                    }
                }

                $(obj).val(newStr);//asdf090980123
            }
        }
        //批发商认证
        function targetToOfficeList() {
            window.location.href = "${ctx}/wholesalers/certification/getOfficeList"
        }

    </script>
</head>
<body>
<%@ include file="../../../modules/homepage/T1Head.jsp" %>
<div class="sea">
    <!--main start-->
    <div class="main">
        <div class="mainHomePage">
            <div class="contentHomePage">
                <div class="bread_new"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('${ctx}');">首页</a>
                    &gt; 订单管理
                </div>
                <div id="group">
                    <div class="group_nav">
                        <em class="t1_2 nav_p_left" onclick="nav_p_left()"></em>
                        <div class="inline "><p class="nav_p">
                            <span id="setTab01" name="setTab01" class="group_nav_child group_nav_child_active"
                                  onclick="searchByPayStatus('0')"
                                  <c:if test="${inlineStatus eq 'rigth'}">style="margin:0 0 0 -370px" </c:if>>全部订单<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab02" name="setTab02" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'1')">未支付全款<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab03" name="setTab03" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'2')">未支付订金<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab04" name="setTab04" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'5')">已支付全款<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab05" name="setTab05" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'4')">已支付订金<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab06" name="setTab06" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'3')">已占位<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab07" name="setTab07" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'7')">待计调确认<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab08" name="setTab08" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'8')">待财务确认<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab09" name="setTab09" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'9')">已撤销占位<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab10" name="setTab10" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'99')">已取消<em
                                    class="group_nav_child_right"></em></span>
                            <span id="setTab11" name="setTab11" class="group_nav_child"
                                  onclick="searchByPayStatus(this,'111')">已删除</span>
                        </p></div>
                        <em class="t1_2 nav_p_right" onclick="nav_p_right()"></em>

                        <span class="group_nav_child_float_right ">
                            <input class="search_input" type="text" placeholder="订单编号 / 产品名称 / 团号" id="threeSerch"
                                   value="${orderNumOrProductNameOrGroupCode}"/>
                            <span class="advanced_search_parrent" onclick="formSubmit();"><em
                                    class="t1_2 advanced_search"></em></span>
                            <span class="advanced_search_parrent" onclick="high_ranking()">高级 <em
                                    class="t1_2 group_nav_child_em"></em></span>
                        </span>
                    </div>
                    <div class="line"></div>
                    <form:form id="searchForm" modelAttribute="travelActivity"
                               action="${ctx}/t1/orderList/manage/showOrderList/${orderStatus}.htm" method="post">
                        <div class="groupSearch" id="groupSearch">
                            <input id="ctx" type="hidden" value="${ctx}"/>
                            <input id="orderStatus" type="hidden" value="${orderStatus}"/>
                            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
                            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
                            <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
                            <input id="showType" name="showType" type="hidden" value="${showType}"/>
                            <input id="orderOrGroup" name="orderOrGroup" type="hidden" value="order"/>
                            <input id="payStatus" name="payStatus" type="hidden" value="${payStatus }"/>
                            <input id="supplierId" name="supplierId" type="hidden" value="${supplierId }"/>
                            <input id="orderType" name="orderType" type="hidden" value="${orderType}"/>
                            <input id="setTab" name="setTab" type="hidden" value="${setTab }">
                            <input id="inlineStatus" name="inlineStatus" type="hidden" value="${inlineStatus}">
                            <input id="orderNumOrProductNameOrGroupCode" name="orderNumOrProductNameOrGroupCode"
                                   type="hidden" value="${orderNumOrProductNameOrGroupCode}">
                            <div>
                            <span class="groupSearchSpan">
                                <span class="groupSearchSpan_head">出团时间：</span>
                                <input id="groupOpenDate" class="inputTxtTwo dateinputTwo"
                                       name="groupOpenDateBegin" value="${groupOpenDateBegin}"
                                       onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                                       readonly="">&nbsp;—&nbsp;
                                <input id="groupCloseDate" class="inputTxtTwo dateinputTwo"
                                       name="groupOpenDateEnd" value="${groupOpenDateEnd}" onclick="WdatePicker()"
                                       readonly="">
                            </span>
                                <span class="groupSearchSpan">
                                <span class="groupSearchSpan_head">下单时间：</span>
                                <input id="groupOpenDateTwo" class="inputTxtTwo dateinputTwo"
                                       name="orderTimeBegin" value="${orderTimeBegin}"
                                       onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDateTwo').value==''){$dp.$('groupCloseDateTwo').value=vvv;}}})"
                                       readonly="">&nbsp;—&nbsp;
                                <input id="groupCloseDateTwo" class="inputTxtTwo dateinputTwo"
                                       name="orderTimeEnd" value="${orderTimeEnd}" onclick="WdatePicker()" readonly="">
                            </span>
                                <div class="groupSearchSpan" id="supStorm" data_from="t1OrderList">
                                    <span class="groupSearchSpan_head">供应商：</span>
                                    <div class="relative inline ">
                                        <p id="searchByOffice"
                                           class="provider_input font_12 write_space write_space_use_one">
                                            <!--  <span class="groupHomeSearch_right_child">天津<em class="t1_2"></em></span> -->
                                        </p>
                                        <a class="provider_a" onclick="switchTip(this);" href="javascript:void(0)"><em
                                                class="t1_2 provider_em"></em></a>
                                        <div class="main_container main_container_order">
                                            <div class="main_content main_content_order">
                                                <div class="input_container input_container_order">
                                                    <input type="text" class="se_input se_input_order">
                                                    <em class="input_icon input_icon_order"
                                                        onclick="fuzzySearch(this);"></em>
                                                </div>
                                                <ul class="city_list">
                                                    <c:forEach var="office" items="${officeList}">
                                                    <li class="city_item" title="${office.name}"><em class="item_icon"
                                                                                                     onclick="switchStatus(this);"></em>
                                                        <c:set var="officeName" value=""></c:set>
                                                        <c:choose>
                                                            <c:when test="${fn:length(office.name) > 10}">
                                                                <c:set var="officeName"
                                                                       value="${fn:substring(office.name, 0, 10)}..."></c:set>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:set var="officeName" value="${office.name}"></c:set>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <span class="item_text" id="${office.id}"
                                                              title="${office.name}">${officeName}</span>
                                                    </li>
                                                    </c:forEach>
                                            </div>
                                            <span class="main_footer main_footer_order">
                                             <span class="butn_sure" onclick="getEle2(this,1);">确定</span>
                                         </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div>
                        	<span class="groupSearchSpan">
                                <span class="groupSearchSpan_head">金额：</span>
                                <input id="moneyStrMin" name="moneyStrMin" onkeyup="isMoney(this)"
                                       onblur="isMoney(this)" onpaste="isMoney(this)" class="inputSmall"
                                       value="${moneyStrMin}">&nbsp;—&nbsp;
                                <input id="moneyStrMax" name="moneyStrMax" onkeyup="isMoney(this)"
                                       onblur="isMoney(this)" onpaste="isMoney(this)" class="inputSmall"
                                       value="${moneyStrMax}">
                            </span>

                                <c:if test="${setTab eq 'setTab01'}">
                                    <div class="groupSearchSpan" id="orderState">
                                        <span class="groupSearchSpan_head">订单状态：</span>
                                        <div class="relative inline font_0">
                                            <p id="searchByOrderState"
                                               class="provider_input font_12 write_space write_space_use_one">
                                            </p>
                                            <a class="provider_a" href="javascript:void(0)"
                                               onclick="switchTip(this);"><em class="t1_2 provider_em"></em></a>
                                            <div class="main_container main_container_order">
                                                <div class="main_content main_content_order">
                                                    <div class="input_container input_container_order">
                                                        <input type="text" class="se_input se_input_order">
                                                        <em class="input_icon input_icon_order"
                                                            onclick="fuzzySearch(this);"></em>
                                                    </div>
                                                    <ul class="city_list">
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="1"
                                                                lang="orderType">未支付全款</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="2"
                                                                lang="orderType">未支付订金</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="5"
                                                                lang="orderType">已支付全款</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="4"
                                                                lang="orderType">已支付订金</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="3" lang="orderType">已占位</span>
                                                        </li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="7"
                                                                lang="orderType">待计调确认</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="8"
                                                                lang="orderType">待财务确认</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="9"
                                                                lang="orderType">已撤销占位</span></li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="99" lang="orderType">已取消</span>
                                                        </li>
                                                        <li class="city_item"><em class="item_icon"
                                                                                  onclick="switchStatus(this);"></em><span
                                                                class="item_text" value="111"
                                                                lang="orderType">已删除</span></li>
                                                    </ul>
                                                </div>
                                                <span class="main_footer main_footer_order">
	                                             <span class="butn_sure" onclick="getEle2(this,2);">确定</span>
	                                         </span>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>

                            <p class="search">
                                <span class="search_child" onclick="formSubmit();">搜 索</span>
                                <span class="reset" onclick="resertFilter()">条件重置</span>
                            </p>
                        </div>
                    </form:form>

                    <div class="rank" id="downOrUp">
                        <span class="float_left">排序：</span>
                        <!--<span class="float_left rank_child_left rank_child_left_one rank_active">默认</span>-->
                        <span class="float_left rank_child_left rank_child_left_two" id="orderTime"
                              onclick="byOrderTimeBegin()">下单时间
                            <i class="relative">
                                <em class="t1_2 rank_up"></em>
                                <em class="t1_2 rank_down"></em>
                            </i>
                        </span>
                        <span class="float_left rank_child_left" id="orderDate" onclick="byGroupOpenDate()">出团日期
                            <i class="relative">
                                <em class="t1_2 rank_up"></em>
                                <em class="t1_2 rank_down"></em>
                            </i>
                        </span>
                        <span class="float_right">
                            ${page2 }
                        </span>
                    </div>
                    <div class="min_height">
                        <table class="table_width ">
                            <thead class="groupOrder J_m_nav groupHomerOrder " id="J_m_nav">
                            <tr>
                                <th width="455px" class="first_t">产品信息</th>
                                <th width="100px">出团日期</th>
                                <th width="60px">订单人数</th>
                                <th width="110px" class="t_right">结算价</th>
                                <th width="110px" class="t_right table_layout">实付款</th>
                                <th width="95px">联系人</th>
                                <th width="100px" class=" last_t">订单状态</th>
                            </tr>
                            </thead>
                        </table>
                        <c:forEach items="${page.list }" var="orders" varStatus="s">
                            <div class="table_down">
                                <div class="table_down_div">
                                <span class="order_number">
                                    <span>订单编号：</span>
                                    <span>${orders.orderNum}</span>
                                </span>
                                    <span>
                                    <span class="table_down_head">下单时间：</span>
                                    <span><fmt:formatDate value="${orders.orderTime}"
                                                          pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                </span>
                                    <c:if test="${orders.confirmationFileId ne null && orders.confirmationFileId ne ''}">
	                               <span class="table_down_load downloadSpan" onclick="downloadConfirm('${orders.id}')">
	                                	<em></em>
	                                   <span>下载确认单</span>
	                                </span>
                                    </c:if>
                                </div>
                                <table class="table_width table_layout">
                                    <tbody>
                                    <tr>
                                        <td width="440px" class="first_t">
                                            <c:choose>
                                                <c:when test="${orders.isT1 eq 1}">
                                                    <p title="${orders.acitivityName}"><a href="javascript:void(0)" onclick="details('${orders.activityId}','','${ctx}')">${orders.acitivityName}</a>
                                                    </p>
                                                </c:when>
                                                <c:otherwise>
                                                    <p title="${orders.acitivityName}"><a href="javascript:void(0)" onclick="details_jbox()">${orders.acitivityName}</a>
                                                    </p>
                                                </c:otherwise>
                                            </c:choose>
                                            <p>
                                                <span>供应商：</span><span>${orders.officeName}</span>
                                                <span class="margin_left_30">团号：</span>${orders.groupCode}<span></span>
                                            </p>
                                        </td>
                                        <td width="100px">
                                            <p>${orders.groupOpenDate}</p>
                                        </td>
                                        <td width="50px">
                                            <p>${orders.orderPersonNum}</p>
                                        </td>
                                        <td width="110px" class="t_right " title="${orders.totalMoney}">
                                            <c:choose>
                                                <c:when test="${orders.totalMoney eq null}"><p>-</p></c:when>
                                                <c:otherwise>
                                                    <p>${orders.totalMoney}</p>
                                                    <c:if test="${not empty orders.differenceFlag and orders.differenceFlag eq '1'}">
                                                        <div class="gray">含门市结算价差额返还：${orders.differenceMoney}</div>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td width="110px" class="t_right table_layout" title="${orders.accountedMoney}">
                                            <c:choose>
                                                <c:when test="${orders.accountedMoney eq null }"><p>-</p></c:when>
                                                <c:otherwise><p>${orders.accountedMoney}</p></c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td width="90px">
                                            <p>
                                                <span>${orders.salerName}</span><br/>
                                                <span>${orders.salerMobile}</span>
                                            </p>
                                        </td>
                                        <td width="100px" class="last_t">
                                            <p>
                                            <span><c:choose>
                                                <c:when test="${orders.orderStatus eq '1'}">未支付全款</c:when>
                                                <c:when test="${orders.orderStatus eq '2'}">未支付订金</c:when>
                                                <c:when test="${orders.orderStatus eq '3'}">已占位</c:when>
                                                <c:when test="${orders.orderStatus eq '4'}">已支付订金</c:when>
                                                <c:when test="${orders.orderStatus eq '5'}">已支付全款</c:when>
                                                <c:when test="${orders.orderStatus eq '7'}">待计调确认</c:when>
                                                <c:when test="${orders.orderStatus eq '8'}">待财务确认</c:when>
                                                <c:when test="${orders.orderStatus eq '9'}">已撤销占位</c:when>
                                                <c:when test="${orders.orderStatus eq '99'}">已取消</c:when>
                                                <c:when test="${orders.orderStatus eq '111'}">已删除</c:when>
                                                <c:otherwise>${fns:getDictLabel(orders.orderStatus, "order_pay_status", "")}</c:otherwise>
                                            </c:choose>
											</span><br/>
                                                <a href="javascript:void(0)" onClick="quauqOrderDetail(${orders.id});">订单详情</a>
                                            </p>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </c:forEach>
                    </div>

                    <div class="page">
                        <div class="pagination">
                            <div class="endPage">
                                ${page}
                                <div style="clear:both;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--main end-->

    </div>

    <!--footer start-->
    <%@ include file="../../../modules/homepage/t1footer.jsp"%><!--footer end-->
    <%--    <div class="footer">
            Copyright © 2016 旅游交易预订系统　　客服电话：010-85718666
        </div>--%>


    <!--footer end-->
</div>
</body>
<script>
    var nt = !1;
    $(window).bind("scroll",
            function () {
                var st = $(document).scrollTop();//往下滚的高度
                nt = nt ? nt : $(".J_m_nav").offset().top;
                var sel = $(".J_m_nav");
                if (nt < st) {
                    sel.addClass("nav_fixed_home");
                } else {
                    sel.removeClass("nav_fixed_home");
                }
            });
</script>
</html>
