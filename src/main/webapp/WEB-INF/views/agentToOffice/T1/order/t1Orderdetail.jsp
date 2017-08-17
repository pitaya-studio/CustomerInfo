<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page import="com.trekiz.admin.common.config.Context" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8,Chrome=1"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>详情</title>
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
    <link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>


    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script src="${ctxStatic }/common/common.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1t2.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1OrderDetail.js"></script>
    <script src="${ctxStatic}/agentToOffice/t1/order/t1Orderdetail.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <link href="${ctxStatic}/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css"/>
    <script>

        //回到首页函数
        function backHomePage() {
            window.location.href = "${ctx}/activity/manager/homepagelist";//回到首页
        }

        //批发商认证
        function targetToOfficeList() {
            window.location.href = "${ctx}/wholesalers/certification/getOfficeList"
        }

    </script>
</head>
<body>
<%@ include file="../../../modules/homepage/T1Head.jsp" %>
<div class="sea sea_background">
    <input id="ctx" value=${ctx } type="hidden"/>
    <!--main start-->
    <div class="main">
        <div class="mainHomePage">
            <div class="contentHomePage">
                <div class="bread breadLeft breadHome"><i></i>您的位置：<a href="javascript:void(0)"
                                                                      onclick="goHomePage('${ctx}');">首页</a> > <a
                        href='${ctx}/t1/orderList/manage/showOrderList/2.htm'>订单管理</a> > 订单详情
                </div>
            </div>
            <div class="details" id="details">
                <div class="details_nav J_m_nav">
                    <a hrefs="#order" class="details_nav_span details_nav_active" onclick="details_nav(this,0)">订单信息 <em
                            class="details_nav_right"></em></a>
                    <a hrefs="#pay" class="details_nav_span pay" onclick="details_nav(this,1)">支付信息<em
                            class="details_nav_right"></em></a>
                    <a hrefs="#reservation" class="details_nav_span reservation" onclick="details_nav(this,2)">预订人信息<em
                            class="details_nav_right"></em></a>
                    <a hrefs="#special" class="details_nav_span special" onclick="details_nav(this,3)">特殊需求<em
                            class="details_nav_right"></em></a>
                    <a hrefs="#tourist" class="details_nav_span touristA" onclick="details_nav(this,4)">游客信息</a>
                </div>
                <div id="backup" style="width:100%;height:52px;margin-bottom:20px;display:none;">
                </div>

                <div class="main_middle">
                    <p id="order1" class="href_title relative"><span id="order" class="href_title_absolute"></span>订单信息
                    </p>
                    <div class="productDiv background_fff">
                        <p>
                            <c:choose>
                                <c:when test="${productGroup.isT1 eq 1}">
                                    <span class="product_em_right"><a href="javascript:void(0)"
                                                                      onclick="details('${productGroup.srcActivityId}','','${ctx }')">${product.acitivityName}</a></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="product_em_right"><a href="javascript:void(0)"
                                                                      onclick="details_jbox()">${product.acitivityName}</a></span>
                                </c:otherwise>
                            </c:choose>

                            <%--<span class="float_right">
                                <em class="product_download"></em>
                                <span class="product_download_right"><a href="javascript:void(0)" onclick="downloads('${docInfoId}')">下载产品行程单</a></span>
                            </span>--%>
                        </p>
                        <p class=" gray_p mb10">
                        <span><span>订单状态：</span><span><c:choose>
                            <c:when test="${productorder.payStatus eq '1'}">未支付全款</c:when>
                            <c:when test="${productorder.payStatus eq '2'}">未支付订金</c:when>
                            <c:when test="${productorder.payStatus eq '3'}">已占位</c:when>
                            <c:when test="${productorder.payStatus eq '4'}">已支付订金</c:when>
                            <c:when test="${productorder.payStatus eq '5'}">已支付全款</c:when>
                            <c:when test="${productorder.payStatus eq '7'}">待计调确认</c:when>
                            <c:when test="${productorder.payStatus eq '8'}">待财务确认</c:when>
                            <c:when test="${productorder.payStatus eq '9'}">已撤销占位</c:when>
                            <c:when test="${productorder.payStatus eq '99'}">已取消</c:when>
                            <c:when test="${productorder.payStatus eq '111'}">已删除</c:when>
                            <c:otherwise>${fns:getDictLabel(productorder.payStatus, "order_pay_status", "")}</c:otherwise>
                        </c:choose></span></span>
                            <span>订单编号：<span>${productorder.orderNum}</span></span>
                            <span>供应商：<span>${office.name }</span></span>
                        </p>
                        <div class="productChild">
                            <div class="orderDiv">
                                <table>
                                    <tbody>
                                    <tr>
                                        <td class="td-odd">下单人：</td>
                                        <td class="td-even">${productorder.createBy.name}</td>
                                        <td class="td-odd">下单时间：</td>
                                        <td class="td-even"><fmt:formatDate value="${productorder.orderTime}"
                                                                            pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                        <td class="td-odd">销售：</td>
                                        <td class="td-even">${productorder.salerName}</td>
                                    </tr>
                                    <tr>
                                        <td class="td-odd">发票号：</td>
                                        <td class="td-even"><c:forEach var="invoice" items="${invoices }"
                                                                       varStatus="varStatus">
                                            <c:if test="${varStatus.count > 1 }"></br></c:if>
                                            <%-- <a target="_blank" href="${ctx}/orderInvoice/manage/viewInvoiceInfo/${invoice.uuid}/-2/${productorder.orderStatus}"> --%>
                                            <span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;"
                                                  title="${invoice.invoiceNum }">
                                                    ${invoice.invoiceNum }
                                            </span></a>
                                        </c:forEach></td>
                                        <td class="td-odd">收据号：</td>
                                        <td class="td-even"><c:forEach var="receipt" items="${receipts }"
                                                                       varStatus="varStatus">
                                            <c:if test="${varStatus.count > 1 }"></br></c:if>
                                            <%-- <a target="_blank" href="${ctx}/receipt/limit/supplyviewrecorddetail/${receipt.uuid}/-2/${productorder.orderStatus}"> --%>
                                            <span style="text-overflow:ellipsis; white-space:nowrap; overflow:hidden; padding-left:0px; width:90%; display:inline-block;"
                                                  title="${receipt.invoiceNum }">
                                                    ${receipt.invoiceNum }
                                            </span></a>
                                        </c:forEach></td>
                                        <td class="td-odd">操作人：</td>
                                        <td class="td-even">${product.createBy.name}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="orderDivBottom">
                                <p>订单总额</p>
                                <p>
                                    <!-- <span>C$
                                        <span class="big_money">0.00</span>
                                    </span>+ -->
                                    <span><!-- ￥ -->
                            <span class="big_money">${travelerSumClearPrice}</span>
                        </span>
                                </p>
                            </div>
                        </div>
                    </div>

                    <p id="pay1" class="href_title relative"><span id="pay" class="href_title_absolute"></span>支付信息</p>
                    <div class="payDiv background_fff">
                        <table>
                            <thead>
                            <tr class="border_bottom_tr">
                                <th width="71">序号</th>
                                <th width="150">付款时间</th>
                                <th width="150">付款凭证</th>
                                <th width="150">支付款类型</th>
                                <th width="120">是否到账</th>
                                <th width="120">付款方式</th>
                                <th width="120" class="tr">付款金额</th>
                            </tr>
                            </thead>
                            <c:choose>
                                <c:when test="${empty orderPayList }">
                                    <tbody class="tbody_none">
                                    <tr>
                                        <td colspan="7" class="tc">暂无任何数据</td>
                                    </tr>
                                    </tbody>
                                </c:when>
                                <c:otherwise>
                                    <tbody>
                                    <c:forEach items="${orderPayList}" var="orderPay" varStatus="v">
                                        <tr>
                                            <c:choose>
                                                <c:when test="${v.count lt 10}">
                                                    <td>0${v.count}</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>${v.count}</td>
                                                </c:otherwise>
                                            </c:choose>

                                            <td><fmt:formatDate value="${orderPay.createDate }"
                                                                pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                            <td>
                                                <c:if test="${empty orderPay.payVoucher}">
                                                    暂无支付凭证
                                                </c:if>
                                                <c:if test="${not empty orderPay.payVoucher}">
                                                    <a class="showpayVoucher" href="javascript:void(0)"
                                                       onclick="downLoadZfpz('${orderPay.payVoucher}');">支付凭证</a>
                                                </c:if>
                                            </td>
                                            <td>${fns:getDictLabel(orderPay.payPriceType, "payprice_Type", "")}</td>
                                            <td><c:choose>
                                                <c:when test="${empty orderPay.isAsAccount}">
                                                    未到账
                                                </c:when>
                                                <c:when test="${orderPay.isAsAccount == 0}">
                                                    未到账
                                                </c:when>
                                                <c:when test="${orderPay.isAsAccount == 99}">
                                                    未到账
                                                </c:when>
                                                <c:when test="${orderPay.isAsAccount == 2}">
                                                    已驳回
                                                </c:when>
                                                <c:otherwise>
                                                    已到账
                                                </c:otherwise>
                                            </c:choose></td>
                                            <td>${orderPay.payTypeName}</td>
                                            <td class="tr">${orderPay.moneySerialNum }</td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </c:otherwise>
                            </c:choose>
                        </table>
                    </div>
                    <p id="reservation1" class="href_title relative"><span id="reservation"
                                                                           class="href_title_absolute"></span>预订人信息<span
                            class="titleSpan">预订渠道：</span><span>${agentinfo.agentName}</span></p>
                    <c:forEach items="${orderContacts}" var="orderContact">
                        <div class="reservationDiv background_fff">
                            <p class="mb10">
                                <em class="unwind unwind_bottom2" onclick="reservation()"></em>
                                <span class="em_right_one"><a class="a_color" onclick="reservation()"
                                                              href="javascript:void(0)">渠道联系人：<span>${orderContact.contactsName}</span></a></span>
                                <span class="em_right_two">渠道联系人电话：<span>${orderContact.contactsTel}</span></span>
                            </p>
                            <div class="reservationDivDiv">
                                <table>
                                    <tbody>
                                    <tr>
                                        <td class="tr" width="80">固定电话：</td>
                                        <td class="tl" width="300">${orderContact.contactsTixedTel}</td>
                                        <td class="tr" width="80">渠道地址：</td>
                                        <td class="tl" width="300" title="${orderContact.contactsAddress}"
                                            style="overflow: hidden;text-overflow: ellipsis;">${orderContact.contactsAddress}</td>
                                        <td class="tr" width="80">渠道邮编：</td>
                                        <td class="tl" width="300">${orderContact.contactsZipCode}</td>
                                    </tr>
                                    <tr>
                                        <td class="tr" width="80">QQ：</td>
                                        <td class="tl" width="300">${orderContact.contactsQQ}</td>
                                        <td class="tr" width="80">Email：</td>
                                        <td class="tl" width="300" title="${orderContact.contactsEmail}"
                                            style="overflow: hidden;text-overflow: ellipsis;">${orderContact.contactsEmail}</td>
                                        <td class="tr" width="80">传真：</td>
                                        <td class="tl" width="300">${orderContact.contactsFax}</td>
                                    </tr>
                                    <tr>
                                        <td class="tr" width="80">其他：</td>
                                        <td class="tl" width="300">${orderContact.remark}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </c:forEach>
                    <p id="special1" class="href_title relative"><span id="special" class="href_title_absolute"></span>特殊需求
                    </p>
                    <div class="specialDiv background_fff">
                        <table>
                            <tbody>
                            <tr>
                                <td width="94" class="tr">特殊要求：</td>
                                <td class="td_bottom">
                            	<span class="td_less">
                                    ${productorder.specialDemand}
                                </span>
                                    <c:if test="${fn:length(productorder.specialDemand) gt 116  }">
                                        <a class="orange all" onclick="details_all(this)">显示全部>></a>
                                    </c:if>
                                </td>
                                <td class="td_bottom hide"><span>${productorder.specialDemand}</span><a
                                        class="orange less" onclick="details_less(this)">&lt&lt收起</a></td>
                            </tr>
                            <tr>
                                <td width="94" class="tr">附件下载：</td>
                                <td>
                                    <c:forEach items="${fns:getDocInfosByIds(productorder.specialDemandFileIds)}"
                                               var="docInfo">
									<span>
										<%-- <a href="javascript:void(0)" class="downloadFile" onclick="downloads4SpecialDeman(${docInfoId})">${docInfo.docName}</a> --%>
										<a href="javascript:void(0)" class="downloadFile"
                                           onclick="downloads4SpecialDeman(${docInfo.id})">${docInfo.docName}</a>
									</span>
                                    </c:forEach>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <p id="tourist1" class="href_title relative"><span id="tourist" class="href_title_absolute"></span>游客信息
                    </p>
                    <div class="touristDiv background_fff">
                        <p>
                        <span class="float_right">
                            <em class="product_download"></em>
                            <span class="product_download_right"><a href="javascript:void(0)"
                                                                    onClick="downloadData('${orderId}', 'traveler' )">下载游客信息</a></span>
                            <form id="exportForm" method="post" action="${ctx }/orderCommon/manage/downloadData">
								<input id="orderId" type="hidden" name="orderId" value="${orderId}">
								<input type="hidden" value="2" name="orderType">
								<input id="downloadType" type="hidden" name="downloadType" value="traveler">
								<input id="orderNum" type="hidden" name="orderNum" value="${productorder.orderNum }">
							</form>
                            
                        </span>
                        </p>
                        <table>
                            <thead>
                            <tr>
                                <th width="100">序号</th>
                                <th width="200">姓名</th>
                                <th width="100">性别</th>
                                <th width="100">类型</th>
                                <th style="text-align: right;" width="200" class="tr">结算价</th>
                                <th width="354">备注</th>
                            </tr>
                            </thead>
                        </table>
                        <div class="tourist_table background_fff">
                            <table>
                                <tbody>
                                <c:choose>
                                    <c:when test="${not empty travelers }">
                                        <c:forEach items="${travelers}" var="travelerone" varStatus="s">
                                            <tr><c:choose>
                                                <c:when test="${s.count lt 10 }">
                                                    <td class="width90">00${s.count }</td>
                                                </c:when>
                                                <c:when test="${s.count ge 10 and s.count lt 100 }">
                                                    <td class="width90">0${s.count }</td>
                                                </c:when>
                                                <c:when test="${s.count ge 100 }">
                                                    <td class="width90">${s.count }</td>
                                                </c:when>
                                            </c:choose>
                                                <td class="width186">${travelerone.traveler.name }</td>
                                                <td class="width92"><c:if
                                                        test="${travelerone.traveler.sex=='1' }"><span>男</span></c:if>
                                                    <c:if test="${travelerone.traveler.sex=='2' }"><span>女</span></c:if>
                                                </td>
                                                <td class="width88">
                                                    <c:if test="${travelerone.traveler.personType == '1'}">成人</c:if>
                                                    <c:if test="${travelerone.traveler.personType == '2'}">儿童</c:if>
                                                    <c:if test="${travelerone.traveler.personType == '3'}">特殊人群</c:if>
                                                </td>
                                                <td style="text-align: right" class="width190 tr"
                                                    title="${travelerone.traveleTotalMoney }">${travelerone.traveleTotalMoney }</td>
                                                <td class="width333"
                                                    title="${travelerone.traveler.remark}">${travelerone.traveler.remark}</td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="7" class="tc">暂无任何数据</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <!--main end-->

        </div>


    </div>
    <!--footer end-->
    <!--footer start-->

    <%@ include file="/WEB-INF/views/modules/homepage/t1footer.jsp"%><!--footer end-->

</body>

</html>
