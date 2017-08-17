<%--
  Created by IntelliJ IDEA.
  User: wanglijun
  Date: 2016/11/23
  Time: 9:52
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
    <meta charset='utf-8'/>
    <title>产品团期详情</title>
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
    <link rel="stylesheet" href="${ctxStatic}/css/common.css"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/newHomePage.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/detail-order.css"/>
    <%--491--%>
    <link rel="stylesheet" href="${ctxStatic}/css/proGroupDetail.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/menology.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/normalize.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/viewer.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/main.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/animate.min.css">
    <%--筛选项样式表--%>
    <link rel="stylesheet" href="${ctxStatic}/css/search.css"/>
    <%--产品页样式表--%>
    <link rel="stylesheet" href="${ctxStatic}/css/t1-product.css"/>
    <!--字体图标-->
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/newHomePage.css"/>
    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/jPages.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
</head>
<body>
<!--header start-->
<%@ include file="T1Head.jsp"%><!--header end-->
<div class="wrapper" id="sea">

    <%@ include file="supplierAndSearch.jsp"%><!--header end-->
    <input id="ctxStatic" type="hidden" value="${ctxStatic}">
    <input id="contextPath" type="hidden" value="${pageContext.request.contextPath}">
    <div class="Details_491">
        <div class="bread_new bread_new_491"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('/a');">首页</a>
            &gt; 详情
        </div>
        <%--详情头部位置--%>
        <div class="Details_header">

        </div>
        <%--详情头部位置end--%>
        <!--日历-->
        <div class="divParent" id="divParent">
            <div class="divParentBorder"></div>
            <div class="date_top">
                <div class="date_top_left"><i></i></div>
                <div class="date_top_center">
                    <!--   <div class="date_top_use active">
                           <div>2016年12月</div>
                           <div></div>
                       </div>
                       <div class="date_top_use ">
                           <div>2016年12月</div>
                           <div>无团期</div>
                       </div>-->
                </div>
                <div class="date_top_right"><i></i></div>
            </div>
            <div class="divChild dc_1 orange">日</div>
            <div class="divChild dc_1 ">一</div>
            <div class="divChild dc_1">二</div>
            <div class="divChild dc_1">三</div>
            <div class="divChild dc_1">四</div>
            <div class="divChild dc_1">五</div>
            <div class="divChild dc_1 orange">六</div>

        </div>
    <%--测试--%>
       <%-- <div  class="htmleaf-container">
            <div class="docs-galley">
                <ul class="docs-pictures certificate clearfix"  style="display:none">
                        <li><div><img data-original="${ctx}/person/info/getLogo?id=${docInfo.id}" alt="${docInfo.docName}" src="${ctx}/person/info/getLogo?id=${docInfo.id}"></div></li>
                </ul>
                <ul class="docs-pictures license clearfix"  style="display:none">
                        <li><div onclick="console.log(1)"><img data-original="/a/person/info/getLogo?id=39214" alt="123" src="/a/person/info/getLogo?id=39214"></div></li>
                </ul>
                <ul class="docs-pictures protocol clearfix"  style="display:none">
                        <li><div onclick="console.log(1)"><img data-original="${ctx}/person/info/getLogo?id=${docInfo.id}" alt="${docInfo.docName}" src="${ctx}/person/info/getLogo?id=${docInfo.id}"></div></li>
                </ul>
            </div>
        </div>
--%>

        <!--日历结束-->
        <!--价格表开始-->
        <div class="pop_sales_parent">
            <div class="pop_sales">
                <table>
                    <thead>
                    <tr class="fontsize_14">
                        <th class="tr" width="80px"></th>
                        <th class="tr" width="160px">建议直客价</th>
                        <th class="tr" width="160px">同行价</th>
                        <th class="tr" width="220px">系统结算价</th>
                    </tr>
                    </thead>
                    <tbody id="tbody_491">

                    </tbody>
                </table>
            </div>
            <div class="note_parent">
                <p class="note">注：请与联系人电话联系并进行交易</p>
            </div>
        </div>
        <!--价格表end-->
        <!--利润计算开始-->
        <div class="math_491">
            <div class="profit_count_one" style="display: block">
                <div class="profit_count_one_head profit_count_one_head_background"><em class="t1_2 count_img_one"></em>利润计算
                </div>
                <div>
                    <table class="profit_count_one_table">
                        <tbody>
                        <tr>
                            <td width="205px" class="t_top  padding_left_20">
                                <div class="math_p_right">
                                    <span class="t_top_child">  实际售价（成人）：</span>
                                    <input type="text" class="input_100" name="sort" id="adult_money" maxlength="12">
                                    元/人 <br>
                                    <span class="people_down">共 <input type="text" class="input_45" name="sort"
                                                                       id="adult"> 人</span>
                                </div>
                            </td>
                            <td width="230px" class="t_top  padding_center_45">
                                <div class="math_p_right">
                                    <span class="t_top_child details_491_m_left">  实际售价（儿童）：</span>
                                    <input type="text" class="input_100" name="sort" id="child_money" maxlength="12">
                                    元/人 <br>
                                    <span class="people_down">共 <input type="text" class="input_45" name="sort"
                                                                       id="child"> 人</span>
                                </div>
                            </td>
                            <td width="205px" class="t_top  padding_right_20">
                                <div class="math_p_right">
                                    <span class="t_top_child">  实际售价（特殊人群）：</span>
                                    <input type="text" class="input_100" name="sort" id="special_money" maxlength="12">
                                    元/人 <br>
                                    <span class="people_down">共 <input type="text" class="input_45" name="sort"
                                                                       id="special"> 人</span>
                                </div>
                            </td>
                        </tr>

                        </tbody>
                    </table>
                    <div class="count_parent">
                        <span class="count" onclick="count()">计 算</span>
                    </div>
                </div>
            </div>
            <div class="profit_count_two" style="display:none;">
                <div class="profit_count_one_head profit_count_two_head"><em class="t1_2 count_img_two"></em>此单利润</div>
                <div>
                    <ul>
                        <li>成人：¥<span class="orange" id="adult_last">-920.12</span>元</li>
                        <li>儿童：¥<span class="orange" id="child_last">-1021.12</span>元</li>
                        <li>特殊人群：¥<span class="orange" id="special_last">0</span>元</li>
                        <li class="multi_currency">此单利润：
                            <span class="money_head1">¥</span><span class="orange font_44 all_last1" id="all_last"
                                                                    style="font-size: 18px;">-1941.24</span></li>
                    </ul>
                    <div class="gray_491 padding_left_20 inline">
                        此单利润=（实际结算价-系统结算价）× 人数
                    </div>
                    <div class="count_parent inline">
                        <span class="count_again count_gray" onclick="count_again()">重新计算</span>
                            <span class="order" onclick="order_this()">按此价格下单</span>
                    </div>
                </div>
            </div>
            <div id="order" class="order_bounced" style="display: none;">
                <div class="order_bounced_head"><em class="t1_2 order_img"></em>选择销售下单
                    <div class="return_count" onclick="return_count()"><i class="fa fa-undo"></i>返回计算</div>
                </div>
                <div class="order_bounced_body_left t_left"><i></i></div>
                <div class="order_bounced_body">

                </div>
                <div class="order_bounced_body_right t_right"><i></i></div>
            </div>
        </div>
        <div style="clear: both;"></div>
        <!--利润计算end-->
        <!--联系供应商下单-->
        <div class="Details_footer_491">
            <div class="order_head_491">
                <ul>
                    <c:choose>
                        <c:when test="${fn:contains(pageContext.request.requestURL,'huitengguoji.com')}">
                        </c:when>
                        <c:when test="${fn:contains(pageContext.request.requestURL,'travel.jsjbt')}">
                        </c:when>
                        <c:otherwise>
                            <li class="phone_order_491"><i></i></li>
                            <li class="order_491_l_height">在操作过程中遇到问题请联系：</li>
                            <li class="order_491_l_height orange">010-85718666</li>
                        </c:otherwise>
                    </c:choose>
                </ul>
                <%--订单追踪v2.0.1  2017-3-16 15:21:16    modefied by ruiqi.zhang--%>
                <%--<div class="link_phone">联系供应商下单 <i class="show"></i></div>--%>
                <div class="link_phone">选择销售下单 </div>
            </div>
            <div class="order_footer_491">
                <div class="pos-popup popup-top">
                    <div class="pos-popup popup-bottom" style="display: block;">
                        <div class="new_top_pop">
                            <div class="po_img_title">
                                <%--<img src="/a/person/info/getLogo?id=37468" alt="logo">--%>
                                <span>QUAUQ内部测试批发商1</span>
                            </div>
                            <p title="11212121212121212121212">投诉电话：11212121212121212121212</p>
                            <p title="www.gth.comwww.gth.comwww.gth.comwww.gth.comwww.gt">网址：&nbsp;www.gth.comwww.gth.comwww.gth.comwww.gth.comwww.gt</p>
                            <ul class="auth_icon auth_icon_491">
                                <li onclick="showName('certificate')">
                                    <a><em class="real_name"></em><br>资质证书</a>
                                </li>
                                <li onclick="showName('license')">
                                    <a><em class="business_licen"></em><br>营业执照</a>
                                </li>
                                <li onclick="showName('protocol')" class="auth_icon_lastli">
                                    <a><em class="coop_deal"></em><br>合作协议</a>
                                </li>
                            </ul>
                        </div>
                        <div class="new_content_pop">
                            <!--联系人部分-->
                            <div class="link_491_left b_left"><i></i></div>
                            <div class="link_491">
                                <%--<div class="pop_contact">
                                    &lt;%&ndash;<div class="divOld"><img src="/a/person/info/getLogo?id=37442" alt="JACK管理员"></div>&ndash;%&gt;
                                    <ul>
                                        <li class="pop_contact_name">1JACK管理员<a
                                                onclick="showName('37526','forSaleOrder','close_parent')"
                                                title="名片"></a></li>
                                        <li><label>职位：</label>销售</li>
                                        <li class="pop_phone_num"><label>手机：</label>54555522</li>
                                        <li><label>座机：</label></li>
                                        <li><label>微信：</label><span class="pop_e_mail">55555222</span></li>
                                        <li><label>邮箱：</label><span class="pop_e_mail">hahaahahhah@qq.com</span></li>
                                    </ul>
                                </div>
                                <div class="pop_contact">
                                    <div class="divOld"><img src="/a/person/info/getLogo?id=37442" alt="JACK管理员"></div>
                                    <ul>
                                        <li class="pop_contact_name">2JACK管理员<a
                                                onclick="showName('37526','forSaleOrder','close_parent')" href="#"
                                                title="名片"></a></li>
                                        <li><label>职位：</label>销售</li>
                                        <li class="pop_phone_num"><label>手机：</label>54555522</li>
                                        <li><label>座机：</label></li>
                                        <li><label>微信：</label><span class="pop_e_mail">55555222</span></li>
                                        <li><label>邮箱：</label><span class="pop_e_mail">hahaahahhah@qq.com</span></li>
                                    </ul>
                                </div>
                                <div class="pop_contact">
                                    <div class="divOld"><img src="/a/person/info/getLogo?id=37442" alt="JACK管理员"></div>
                                    <ul>
                                        <li class="pop_contact_name">3JACK管理员<a
                                                onclick="showName('37526','forSaleOrder','close_parent')" href="#"
                                                title="名片"></a></li>
                                        <li><label>职位：</label>销售</li>
                                        <li class="pop_phone_num"><label>手机：</label>54555522</li>
                                        <li><label>座机：</label></li>
                                        <li><label>微信：</label><span class="pop_e_mail">55555222</span></li>
                                        <li><label>邮箱：</label><span class="pop_e_mail">hahaahahhah@qq.com</span></li>
                                    </ul>
                                </div>
                                <div class="pop_contact">
                                    <div class="divOld"><img src="/a/person/info/getLogo?id=37442" alt="JACK管理员"></div>
                                    <ul>
                                        <li class="pop_contact_name">4JACK管理员<a
                                                onclick="showName('37526','forSaleOrder','close_parent')" href="#"
                                                title="名片"></a></li>
                                        <li><label>职位：</label>销售</li>
                                        <li class="pop_phone_num"><label>手机：</label>54555522</li>
                                        <li><label>座机：</label></li>
                                        <li><label>微信：</label><span class="pop_e_mail">55555222</span></li>
                                        <li><label>邮箱：</label><span class="pop_e_mail">hahaahahhah@qq.com</span></li>
                                    </ul>
                                </div>--%>
                            </div>
                            <div class="link_491_right b_right"><i></i></div>
                            <!--联系人部分end-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--联系供应商下单end-->
    </div>
</div>
<div class="footer">
    Copyright © 2016 旅游交易预订系统　　客服电话：010-85718666
</div>
<div id="use_img_491"></div>
<script type="text/javascript" src="${ctxStatic}/js/t1/dateGroupDisplay.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/t1/proGroupDetail.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/placeholder.js"></script>

</body>
</html>
