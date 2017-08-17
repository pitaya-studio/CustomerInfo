<%--
  Created by IntelliJ IDEA.
  User: wanglijun
  Date: 2016/10/19
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1"/>
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
    <title>下单记录</title>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>

    <!--538-->
    <link type="text/css" href="${ctxStatic}/css/detail-order.css" rel="stylesheet"/>
    <link type="text/css" href="${ctxStatic}/css/search.css" rel="stylesheet"/>
    <!--end-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/page12.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script src="${ctxStatic }/common/common.js" type="text/javascript" ></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1/orderRecord.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/placeholder.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1ForHuiTeng.js"></script>
</head>
<body>
<%@ include file="../../../modules/homepage/T1Head.jsp"%>
<div class="sea">
    <input id="getCtx" type="hidden" value="${ctx}"/>
    <input id="getCtxStatic" type="hidden" value="${ctxStatic}"/>
    <!--main start-->
    <div class="main">
        <div class="mainHomePage">
            <div class="contentHomePage">
                <div class="bread_new"><i></i>您的位置：<a href="javascript:void(0)" onclick="goHomePage('${ctx}');">首页</a> &gt; 下单记录</div>
                <div id="group">
                    <div class="group_nav" >

                        <span class="group_nav_child group_nav_child_active " data-id="0" id="scroll_use" onclick="getOrderRecordByRecord(0)" >全部订单<em class="group_nav_child_right"></em></span>
                        <span class="group_nav_child" data-id="1" onclick="getOrderRecordByRecord(1)">待处理<em class="group_nav_child_right"></em></span>
                        <span class="group_nav_child" data-id="2" onclick="getOrderRecordByRecord(2)">已下单<em class="group_nav_child_right"></em></span>
                        <span class="group_nav_child" data-id="3" onclick="getOrderRecordByRecord(3)">已取消<em class="group_nav_child_right"></em></span>
                        <span class="group_nav_child" data-id="4" onclick="getOrderRecordByRecord(4)">已删除</span>
                    </div>

                    <span class="group_nav_child_float_right ">
                            <input class="search_input" type="text" id="fuzzySearch"  placeholder="产品名称 / 提交编号"/>
                            <span  class="advanced_search_parrent"><em class="t1_2 advanced_search"></em></span>
                            <span class="advanced_search_parrent" onclick="high_ranking()">高级 <em class="t1_2 group_nav_child_em"></em></span>
                        </span>
                </div>
                <div class="line"></div>
                <div class="groupSearch" id="groupSearch">
                    <div>
                            <span class="groupSearchSpan">
                                <span class="groupSearchSpan_head">出团日期：</span>
                                <input id="groupOpenDate" class="inputTxtTwo dateinputTwo"
                                       name="groupOpenDateBegin" value=""
                                       onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
                                       readonly="">&nbsp;—&nbsp;
                                <input id="groupCloseDate" class="inputTxtTwo dateinputTwo"
                                       name="groupOpenDateEnd" value="" onclick="WdatePicker()" readonly="">
                            </span>
                        <span class="groupSearchSpan">
                                <span class="groupSearchSpan_head">提交时间：</span>
                                <input id="groupOpenDateTwo" class="inputTxtTwo dateinputTwo"
                                       name="groupOpenDateBegin" value=""
                                       onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDateTwo').value==''){$dp.$('groupCloseDateTwo').value=vvv;}}})"
                                       readonly="">&nbsp;—&nbsp;
                                <input id="groupCloseDateTwo" class="inputTxtTwo dateinputTwo"
                                       name="groupOpenDateEnd" value="" onclick="WdatePicker()" readonly="">
                            </span>
                        <div class="groupSearchSpan" id="supStorm" data_from="orderRecord">
                            <span class="groupSearchSpan_head">供应商：</span>
                            <div class="relative inline ">
                                <p class="provider_input font_12 write_space write_space_use_one">
                                </p>
                                <a class="provider_a"  onclick="switchTip(this);" href="javascript:void(0)"><em class="t1_2 provider_em"></em></a>
                                <div class="main_container main_container_order">
                                    <div class="main_content main_content_order">
                                        <div class="input_container input_container_order">
                                            <input type="text"  class="se_input se_input_order">
                                            <em class="input_icon input_icon_order"  onclick="fuzzySearch(this);"></em>
                                        </div>
                                        <ul id="supplierSelect"  class="city_list">
                                        </ul>
                                    </div>
                                    <span class="main_footer main_footer_order">
                                             <button class="butn_sure"  onclick="getEle2(this,1);">确定</button>
                                         </span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div>

                        <span class="groupSearchSpan">
                            <span class="groupSearchSpan_head">金额：</span>
                            <input id="inputSmall" class="inputSmall" value="">&nbsp;—&nbsp;
                            <input class="inputSmall" value="">
                        </span>
                        <div class="groupSearchSpan" id="orderState">
                            <span class="groupSearchSpan_head">下单状态：</span>
                            <div class="relative inline font_0">
                                <p class="provider_input font_12 write_space write_space_use_one">
                                </p>
                                <a class="provider_a" href="javascript:void(0)" onclick="switchTip(this);"><em class="t1_2 provider_em"></em></a>
                                <div class="main_container main_container_order">
                                    <div class="main_content main_content_order">
                                        <div class="input_container input_container_order">
                                            <input type="text"  class="se_input se_input_order">
                                            <em class="input_icon input_icon_order"  onclick="fuzzySearch(this);"></em>
                                        </div>
                                        <ul class="city_list">
                                            <%--<li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span data-id="0" class="item_text">全部订单</span></li>--%>
                                            <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span data-id="1" class="item_text">待处理</span></li>
                                            <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span data-id="2" class="item_text">已下单</span></li>
                                            <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span data-id="3" class="item_text">已取消</span></li>
                                            <li class="city_item"><em class="item_icon" onclick="switchStatus(this);"></em><span data-id="4" class="item_text">已删除</span></li>
                                        </ul>
                                    </div>
                                    <span class="main_footer main_footer_order">
                                             <button class="butn_sure"  onclick="getEle2(this,2);">确定</button>
                                     </span>
                                </div>
                            </div>
                        </div>

                    </div>

                    <p class="search">
                        <span id="search_child" class="search_child">搜 索</span>
                        <span class="reset" onclick="resertFilter()">条件重置</span>
                    </p>


                </div>

                <div class="rank" id="downOrUp">
                    <span class="float_left" >排序：</span>
                    <!--<span class="float_left rank_child_left rank_child_left_one rank_active">默认</span>-->
                    <span class="float_left rank_child_left rank_child_left_two downOrUp" data-id="pro.createDate" id="orderTime">提交时间
                            <i class="relative">
                                <em class="t1_2 rank_up"></em>
                                <em class="t1_2 rank_down"></em>
                            </i>
                        </span>
                    <span class="float_left rank_child_left downOrUp" data-id="groupOpenDate" id="orderDate">出团日期
                            <i class="relative">
                                <em class="t1_2 rank_up"></em>
                                <em class="t1_2 rank_down"></em>
                            </i>
                        </span>
                    <span class="float_right">
                            <span class="rank_product_right">共 <span  class="orange font_14 totalPageSize">0</span> 条产品</span>
                            <span>
                                <em class="orange_left t1_2" onclick="goPage('','','prev')"></em>
                                <span class="orange nowPage">1</span>
                                <span>/</span>
                                <span class="totalPage">1</span>
                                <em class="orange_right t1_2" onclick="goPage('','','next')"></em>
                            </span>
                        </span>
                </div>
                <div>
                    <table class="table_width">
                        <thead class="groupOrder J_m_nav groupHomerOrder " id="J_m_nav">
                        <tr>
                            <th width="455px" class="first_t">产品信息</th>
                            <th width="100px">出团日期</th>
                            <th width="60px">人数</th>
                            <th width="110px" class="t_right">实际结算价</th>
                            <th width="110px" class="t_right">系统结算价</th>
                            <th width="95px" >批发商联系人</th>
                            <th width="100px" class=" last_t">下单状态</th>
                        </tr>
                        </thead>
                    </table>

                    <div id="table_down" class="table_down">
                        <table id="noResult" class="table_width">
                            <tbody>
                                <tr>
                                    <td  colspan="7" style="text-align: center;">
                                        暂无搜索结果
                                    </td>
                                </tr>
                        </table>
                    </div>


                </div>

                <div class="page">
                    <div class="pagination">
                        <div id="page" class="endPage">
                        </div>
                    </div>
                </div>

                <div  id="orderRecord"  class="orderRecord" style="display: none">
                    <div class="orderStatusDiv">
                            <%--待处理--%>
                        <div class="orderStatus1 orderRecordStatus">
                            <div class="orderStatusImg StatusImg1"></div>
                            <div class="orderStatusDown1">
                                <div>待处理</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                        </div>
                            <%--已取消--%>
                        <div class="orderStatus2 hide orderRecordStatus">
                            <div class="orderStatusImg StatusImg2"></div>
                            <div class="orderStatusDown1">
                                <div>待处理</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                            <div class="orderStatusDown2">
                                <div>已取消</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                        </div>
                            <%--已删除--%>
                        <div class="orderStatus3 hide orderRecordStatus">
                            <div class="orderStatusImg StatusImg2"></div>
                            <div class="orderStatusDown1">
                                <div>待处理</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                            <div class="orderStatusDown2">
                                <div>已删除</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                        </div>
                            <%--已下单--%>
                        <div class="orderStatus4 hide orderRecordStatus">
                            <div class="orderStatusImg StatusImg3"></div>
                            <div class="orderStatusDown1">
                                <div>待处理</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                            <div class="orderStatusDown2">
                                <div>已下单</div>
                                <div class="dateGray">2010-10-10</div>
                            </div>
                        </div>
                    </div>

                    <%--<div  id="closeOrderDetail" class="buttons">
                        <span  class="unable" >关闭</span>
                    </div>--%>
                </div>

            </div>
        </div>
    </div>
    <!--main end-->

</div>
<%@ include file="/WEB-INF/views/modules/homepage/t1footer.jsp"%><!--footer end-->
</body>
</html>
