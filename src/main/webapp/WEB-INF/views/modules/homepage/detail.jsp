<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
    <%--<meta http-equiv=X-UA-Compatible content=IE=edge,Chrome=1>--%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1">
    <meta charset="utf-8">
    <title></title>
    <link type="text/css" href="${ctxStatic}/css/t1t2.css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/normalize.css" />
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/viewer.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/main.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/css/imgView/animate.min.css">

    <script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1home.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/jPages.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/viewer.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/picView/main.js"></script>
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->

    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/>
    <!--538-->
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link rel="stylesheet" href="${ctxStatic}/css/t1t2.css"/>
    <link type="text/css" href="${ctxStatic}/css/detail-order.css" rel="stylesheet"/>
    <link type="text/css" href="${ctxStatic}/css/search.css" rel="stylesheet"/>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/search.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/detail-order.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1Common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/t1ForHuiTeng.js"></script>
    <!--538end-->

    <script>

        //金额
        $(document).ready(function(){
            if($(".money_use").text().length>5){
                $(".money_use").css("font-size","18px")
            }
            $('input[class=input_100]').keyup(function(){
                var $this=this;
                $this.value = $this.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
                $this.value = $this.value.replace(/^\./g,""); //验证第一个字符是数字而不是
                $this.value = $this.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
                $this.value = $this.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
                $this.value = $this.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
            })
        });



        //人数
        $(document).ready(function(){
            $('input[class=input_45]').keyup(function(){
                var _value=$(this).val();
                _value = _value.replace(/[^\d]/g, "");
                _value = _value.replace(/([0-9]{4})[0-9]*/, "$1");
//                _value=_value.replace(/^[0-9]{4}$/,"");
//                console.log(_value);
                  $(this).val(_value)
            })
        });

        function downloads(docIds){
            if(null==docIds || ''==docIds || 'undefined'==docIds){ //判断文档id是否为空
                top.$.jBox.tip("没有行程单可供下载!");
                return false;
            }
            //将产品行程介绍打包下载
            window.open("${ctx}/sys/docinfo/zipdownload/"+docIds+"/introduction");
        }

        function downloadFile(docId){
            window.open("${ctx}/sys/docinfo/download/" + docId);
        }

    </script>
</head>
<body >
<div class="pop_content">
    <div class="pop_content_th"><span>出团日期：${detail.groupOpenDate}</span><span class="ullage"> 余位：${detail.freePosition}</span></div>
    <c:if test="${not empty businessCertificate or not empty businessLicense or not empty cooperationProtocol }">
        <a id="pop_permission_detail" class="pop_permission" href="${ctx}/wholesalers/certification/officeDetail?companyId=${office.id}" target="_blank"><em></em>认证详情</a>
    </c:if>
    <div class="pop_permission_show">
        <em class="pop_per_angle"></em>
        <div class="pop_per_content">
            <span>该批发商已经通过认证</span>
            <a href="${ctx}/wholesalers/certification/officeDetail?companyId=${office.id}" target="_blank">认证详情 <i class="fa fa-angle-double-right"></i></a>
            <ul class="auth_icon">
            <c:choose>
                <c:when test="${not empty businessCertificate}">
                    <li onclick="showName('certificate')">
                        <a><em class="real_name"></em><br>资质证书</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a class="not_click_a"><em class="real_name_none"></em><br>资质证书</a>
                    </li>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${not empty businessLicense}">
                    <li onclick="showName('license')">
                        <a><em class="business_licen"></em><br>营业执照</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li>
                        <a class="not_click_a"><em class="business_licen_none"></em><br>营业执照</a>
                    </li>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${not empty cooperationProtocol}">
                    <li onclick="showName('protocol')" class="auth_icon_lastli">
                        <a><em class="coop_deal"></em><br>合作协议</a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="auth_icon_lastli">
                        <a class="not_click_a"><em class="coop_deal_none"></em><br>合作协议</a>
                    </li>
                </c:otherwise>
            </c:choose>
            </ul>
        </div>
    </div>

    <div  class="htmleaf-container">
        <div class="docs-galley">
            <ul class="docs-pictures certificate clearfix"  style="display:none">
                <c:forEach items="${businessCertificate}" var="docInfo">
                    <li><div><img data-original="${ctx}/person/info/getLogo?id=${docInfo.id}" alt="${docInfo.docName}" src="${ctx}/person/info/getLogo?id=${docInfo.id}"></div></li>
                </c:forEach>
            </ul>
            <ul class="docs-pictures license clearfix"  style="display:none">
                <c:forEach items="${businessLicense}" var="docInfo">
                    <li><div onclick="console.log(1)"><img data-original="${ctx}/person/info/getLogo?id=${docInfo.id}" alt="${docInfo.docName}" src="${ctx}/person/info/getLogo?id=${docInfo.id}"></div></li>
                </c:forEach>
            </ul>
            <ul class="docs-pictures protocol clearfix"  style="display:none">
                <c:forEach items="${cooperationProtocol}" var="docInfo">
                    <li><div onclick="console.log(1)"><img data-original="${ctx}/person/info/getLogo?id=${docInfo.id}" alt="${docInfo.docName}" src="${ctx}/person/info/getLogo?id=${docInfo.id}"></div></li>
                </c:forEach>
            </ul>
        </div>
    </div>

    <div class="big_head">
        <h2  title="${detail.activityName}">${detail.activityName}</h2>
        <%--<div class="relative download_float_right" onclick="downloads('${docIds}')">下载行程单 <em class="downloadG"></em></div>--%>
        <div class="relative download_float_right" onclick="showDownLoad('${docIds}',this);">
            <i class="fa fa-paperclip" aria-hidden="true"  style="margin-left:9px;"></i>行程单附件
            <em class="trangle-top"></em>
            <div class="travel-attach">
                <a class="batch-attach" onclick="downloads('${docIds}')"><i class="fa fa-download" aria-hidden="true"></i>下载全部</a>
                <ul>
                    <c:forEach items="${activityFiles}" var="activityFile">
                        <li><span>${activityFile.docInfo.docName} <a href="#" onclick="downloadFile(${activityFile.srcDocId})">下载</a></span></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
    </div>
    <div class="relative">
    <div class="pop_link">
        <dl>
            <dt>团号：</dt>
            <dd title="${detail.groupCode}">${detail.groupCode}</dd>
        </dl>
        <dl>
            <dt>出发城市：</dt>
            <dd>
                <c:forEach items="${fromAreas}" var="fromArea">
                    <c:if test="${fromArea.value eq detail.fromArea }">
                        ${fromArea.label}
                    </c:if>
                </c:forEach>
            </dd>
        </dl>
        <dl>
            <dt>行程天数：</dt>
            <dd>${detail.activityDuration}天</dd>
        </dl>
        <dl>
            <dt>交通工具：</dt>
            <dd>
                <c:forEach items="${trafficModeList}" var="trafficMode">
                    <c:if test="${trafficMode.value eq detail.trafficMode }">
                        ${trafficMode.label}
                    </c:if>
                </c:forEach>
            </dd>
        </dl>

    </div>
    </div>



    <div style="clear: both;"></div>
    <div class="pop_sales_parent">
    <div class="pop_sales">
        <table>
            <thead>
                <tr class="fontsize_14">
                    <th class="tr" width="55px"></th>
                    <th class="tr" width="70px">建议直客价</th>
                    <th class="tr" width="70px">同行价</th>
                    <th class="tr" width="110px">系统结算价</th>
                </tr>
            </thead>
            <tbody>
            <tr class="firsttr">
                <td  class="td_first">成人</td>
                <td  class="tr">
                    <c:if test="${not empty detail.suggestAdultPrice}">
                        ${fns:getCurrencyInfo(detail.currencyIds,3,'mark')}<fmt:formatNumber  type="currency" pattern="###0.00" value="${detail.suggestAdultPrice}" />/人
                    </c:if>
                </td>
                <td class="tr">
                    <c:if test="${not empty detail.settlementAdultPrice}">
                        ${fns:getCurrencyInfo(detail.currencyIds,0,'mark')}<fmt:formatNumber  type="currency" pattern="###0.00" value="${detail.settlementAdultPrice}" />/人
                    </c:if>
                </td>
                <td  class="tr">
                    <c:choose>
                        <c:when test="${not empty detail.quauqAdultPrice}">
                            <span class="money_1">${fns:getCurrencyInfo(detail.currencyIds,0,'mark')}</span><span class="orange orange-price money_use" id="adult_price">
                            <c:set var="currencyId" value="${fns:getCurrencyInfo(detail.currencyIds,0,'id')}" />
                            <fmt:formatNumber  type="currency" pattern="###0.00" value="${fns:getRetailPrice(detail.groupId, 2, detail.settlementAdultPrice, detail.quauqAdultPrice, currencyId)}" /></span>/人
                        </c:when>
                        <c:otherwise>
                            <span class="money_1" style="display: none">${fns:getCurrencyInfo(detail.currencyIds,0,'mark')}</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr class="firsttr">
                <td class="td_first">儿童</td>
                <td class="tr">
                    <c:if test="${not empty detail.suggestChildPrice}">
                        ${fns:getCurrencyInfo(detail.currencyIds,4,'mark')}<fmt:formatNumber  type="currency" pattern="###0.00" value="${detail.suggestChildPrice}" />/人
                    </c:if>
                </td>
                <td class="tr">
                    <c:if test="${not empty detail.settlementcChildPrice}">
                        ${fns:getCurrencyInfo(detail.currencyIds,1,'mark')}<fmt:formatNumber  type="currency" pattern="###0.00" value="${detail.settlementcChildPrice}" />/人
                    </c:if>
                </td>
                <td class="tr">
                    <c:choose>
                        <c:when test="${not empty detail.quauqChildPrice}">
                            <span class="money_2">${fns:getCurrencyInfo(detail.currencyIds,1,'mark')}</span><span class="orange orange-price money_use" id="child_price">
                            <c:set var="currencyId" value="${fns:getCurrencyInfo(detail.currencyIds,1,'id')}" />
                            <fmt:formatNumber  type="currency" pattern="###0.00" value="${fns:getRetailPrice(detail.groupId, 2, detail.settlementcChildPrice, detail.quauqChildPrice, currencyId)}" /></span>/人
                        </c:when>
                        <c:otherwise>
                            <span class="money_2" style="display: none">${fns:getCurrencyInfo(detail.currencyIds,1,'mark')}</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr class="firsttr">
                <td class="td_first">特殊人群</td>
                <td class="tr">
                    <c:if test="${not empty detail.suggestSpecialPrice}">
                        ${fns:getCurrencyInfo(detail.currencyIds,5,'mark')}<fmt:formatNumber  type="currency" pattern="###0.00" value="${detail.suggestSpecialPrice}" />/人
                    </c:if>
                </td>
                <td class="tr">
                    <c:if test="${not empty detail.settlementSpecialPrice}">
                        ${fns:getCurrencyInfo(detail.currencyIds,2,'mark')}<fmt:formatNumber  type="currency" pattern="###0.00" value="${detail.settlementSpecialPrice}" />/人
                    </c:if>
                </td>
                <td class="tr">
                    <c:choose>
                        <c:when test="${not empty detail.quauqSpecialPrice}">
                           <span class="money_3">${fns:getCurrencyInfo(detail.currencyIds,2,'mark')}</span><span class="orange orange-price money_use" id="special_price">
                            <c:set var="currencyId" value="${fns:getCurrencyInfo(detail.currencyIds,2,'id')}" />
                            <fmt:formatNumber  type="currency" pattern="###0.00" value="${fns:getRetailPrice(detail.groupId, 2, detail.settlementSpecialPrice, detail.quauqSpecialPrice, currencyId)}" /></span>/人
                        </c:when>
                        <c:otherwise>
                            <span class="money_3" style="display: none">${fns:getCurrencyInfo(detail.currencyIds,2,'mark')}</span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="note_parent">
        <p class="note">注：请与联系人电话联系并进行交易</p>
    </div>
    </div>
    <div class="profit_count_one" >
        <div class="profit_count_one_head profit_count_one_head_background"><em class="t1_2 count_img_one"></em>利润计算</div>
        <div>
            <table class="profit_count_one_table">
                <tbody>
                <tr>
                    <td width="170px" class="t_top t_left padding_left_15"><span class="t_top_child">  实际结算价（成人）：</span></td>
                    <td width="155px" class="t_right">
                        <input type="text" class="input_100" name="sort" id="adult_money" maxlength="12"/> 元/人 <br/>
                        <span class="people_down">共 <input type="text" class="input_45" name="sort" id="adult"/> 人</span>
                    </td>
                </tr>
                <tr>
                    <td width="170px" class="t_top t_left padding_left_15"><span class="t_top_child">  实际结算价（儿童）：</span></td>
                    <td width="155px" class="t_right">
                        <input type="text" class="input_100" name="sort" id="child_money" maxlength="12"/> 元/人 <br/>
                        <span class="people_down">共 <input type="text" class="input_45" name="sort" id="child"/> 人</span>
                    </td>
                </tr>
                <tr>
                    <td width="190px" class="t_top t_left padding_left_15"><span class="t_top_child">  实际结算价（特殊人群）：</span></td>
                    <td width="155px" class="t_right">
                        <input type="text" class="input_100" name="sort" id="special_money" maxlength="12"/> 元/人 <br/>
                        <span class="people_down">共 <input type="text" class="input_45" name="sort" id="special"/> 人</span>
                    </td>
                </tr>
                </tbody>
            </table>
            <div class="count_parent">
                <span class="count"  onclick="count()">计 算</span>
            </div>
        </div>
    </div>
    <div class="profit_count_two">
        <div class="profit_count_one_head profit_count_two_head"><em class="t1_2 count_img_two"></em>此单利润</div>
        <div>
            <ul>
                <li>成人：${fns:getCurrencyInfo(detail.currencyIds,0,'mark')}<span class="orange" id="adult_last">9000</span>元</li>
                <li>儿童：${fns:getCurrencyInfo(detail.currencyIds,1,'mark')}<span class="orange" id="child_last">9000</span>元</li>
                <li>特殊人群：${fns:getCurrencyInfo(detail.currencyIds,2,'mark')}<span class="orange" id="special_last">9000</span>元</li>
                <li class="multi_currency">此单利润：
                    <%--<span class="money_head1"></span><span class="orange font_44 all_last1" id="all_last"></span>--%>
                    <%--<span class="money_head2"></span><span class="orange font_44 all_last2"></span>--%>
                    <%--<span class="money_head3"></span><span class="orange font_44 all_last3"></span>元--%>
                </li>
                <li class="gray">此单利润=（实际结算价-系统结算价）× 人数</li>
            </ul>
            <div class="count_parent">
                <span class="count_again count_gray" onclick="count_again()">重新计算</span>
                <c:if test="${fns:getUser().differenceRights eq 1}">
                	<span class="order" onclick="order_this()">按此价格下单</span>
                </c:if>
            </div>
        </div>
    </div>
    <div id="order" class="order_bounced">
        <div class="order_bounced_head"><em class="t1_2 order_img"></em>选择销售下单
            <div class="return_count" onclick="return_count()"><i class="fa fa-undo"></i>返回计算</div>
        </div>
        <%--538--%>

        <div class="order_bounced_body">
            <div  class="htmleaf-container">
                <div class="docs-galley">
                    <ul class="docs-pictures  clearfix forSaleOrder"  style="display:none;">
                    <c:forEach items="${salers}" var="saler">
                        <c:if test="${not empty saler.cardId}">
                                <li><div><img data-imgId="${saler.cardId}" data-original="${ctx}/person/info/getLogo?id=${saler.cardId}" alt="${saler.docName}" src="${ctx}/person/info/getLogo?id=${saler.cardId}"></div></li>
                        </c:if>
                    </c:forEach>
                    </ul>
                </div>
            </div>

            <c:forEach items="${salers}" var="saler">
                <div class="order_contact">
                    <div class="ordering" onclick="placeOrder('${detail.groupId}','${saler.userId}','${ctx}')">下单</div>
                    <c:choose>
                        <c:when test="${not empty saler.photoId}">
                            <img src="${ctx}/person/info/getLogo?id=${saler.photoId}" alt="${saler.name}" style="margin-right:15px;width:90px;height:110px;border-radius: 8px;"/>
                        </c:when>
                        <c:otherwise>
                            <img src="${ctxStatic}/images/T1T2/photo.png" alt="默认头像" style="margin-right:15px;width:90px;height:110px;border-radius: 8px;"/>
                        </c:otherwise>
                    </c:choose>
                    <ul class="font_12" style="width: 170px;">
                        <c:choose>
                            <c:when test="${not empty saler.cardId}">
                                <li class="order_contact_name"><span title="${saler.name}" class="order_contact_name_width">${saler.name}</span><a onclick="showName('${saler.cardId}','forSaleOrder','order')"  href="#"  title="名片"></a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="order_contact_name"><span title="${saler.name}" class="order_contact_name_width">${saler.name}</span></li>
                            </c:otherwise>
                        </c:choose>
                        <li><label>职位：</label>销售</li>
                        <li class=""><label>手机：</label>${saler.mobile}</li>
                        <li><label>座机：</label>${saler.phone}</li>
                        <li><label>邮箱：</label><span style="display: inline-flex;width:130px;">${saler.email}</span></li>
                        <li><label>微信：</label><span style="display: inline-flex;width:130px;">${saler.weixin}</span></li>
                    </ul>
                </div>
            </c:forEach>
        </div>
    </div>
    <div id="close_parent" class="close_parent">
        <span class="close" onclick="window.parent.jBox.close();">关 &nbsp 闭</span>
        <div class="relative inline" onclick="stopPop(this);">
            <span class="link_phone" onclick="link_phone('${ctx}',${detail.groupId},this)">联系供应商下单</span>
            <div  class="pos-popup popup-bottom"></div>
            <div  class="pos-popup popup-top">

                <div  class="htmleaf-container">
                    <div class="docs-galley">
                        <ul class="docs-pictures  clearfix forSaleOrder"  style="display:none">
                        <c:forEach items="${salers}" var="saler">
                            <c:if test="${not empty saler.cardId}">
                                    <li><div><img data-imgId="${saler.cardId}" data-original="${ctx}/person/info/getLogo?id=${saler.cardId}" alt="${saler.docName}" src="${ctx}/person/info/getLogo?id=${saler.cardId}"></div></li>
                            </c:if>
                        </c:forEach>
                        </ul>
                    </div>
                </div>


                <i class="fa fa-times" aria-hidden="true"  onclick="hidePopup();"></i>
                <em class="inverTriangle"></em>
                <div class="new_top_pop">
                    <div class="po_img_title">
                        <c:choose>
                            <c:when test="${not empty docInfoId}">
                                <img src="${ctx}/person/info/getLogo?id=${docInfoId}" alt="logo"/>
                            </c:when>
                            <c:otherwise>
                                <img src="${ctxStatic}/images/T1T2/no_logo.png" alt="暂无logo"/>
                            </c:otherwise>
                        </c:choose>
                        <span>${office.name}</span>
                    </div>
                    <p title="${office.phone}">投诉电话：${office.phone}</p>
                    <p title="${office.webSite}">网址：&nbsp;${office.webSite}</p>
                </div>
                <div class="new_content_pop">
                    <c:forEach items="${salers}" var="saler">
                        <div class="pop_contact">
                            <c:choose>
                                <c:when test="${not empty saler.photoId}">
                                    <div class="divOld"><img src="${ctx}/person/info/getLogo?id=${saler.photoId}" alt="${saler.name}"/></div>
                                </c:when>
                                <c:otherwise>
                                    <div class="divOld"><img src="${ctxStatic}/images/T1T2/photo.png" alt="默认头像"/></div>
                                </c:otherwise>
                            </c:choose>
                            <ul>
                                <c:choose>
                                    <c:when test="${not empty saler.cardId}">
                                        <li class="pop_contact_name">${saler.name}<a onclick="showName('${saler.cardId}','forSaleOrder','close_parent')"  href="#"  title="名片"></a></li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="pop_contact_name">${saler.name}</li>
                                    </c:otherwise>
                                </c:choose>
                                <li><label>职位：</label>销售</li>
                                <li class="pop_phone_num"><label>手机：</label>${saler.mobile}</li>
                                <li><label>座机：</label>${saler.phone}</li>
                                <li><label>邮箱：</label><span class="pop_e_mail">${saler.email}</span></li>
                                <li><label>微信：</label><span class="pop_e_mail">${saler.weixin}</span></li>
                            </ul>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        <!--538临时添加测试用-->
     <%--   <span  style="margin-left:20px;border:1px solid blue;background-color: #999;cursor:pointer;" onclick="placeOrder();">下单</span>--%>

    </div>
</div>
<!--538下单弹窗-->
<div  id="ord"  style="display: none;">
    <%--<div class="product-info">MU-美东西海岸瀑布芝加哥夏威夷14天MU-美东西海岸瀑布芝加哥夏威夷14天MU-美东西海岸瀑布芝加哥夏威夷14天MU-美东西海岸瀑布芝加哥夏威夷14天</div>
    <div class="product-item">
        <span class="item-detail">团号：13125654444444444455422121232</span>
        <span class="item-detail">出团日期：2016-10-10</span>
        <span class="item-detail">出发城市：北京、美国、南非</span>
        <span class="item-detail">行程天数：80天</span>
        <span class="item-detail">交通工具：飞机</span>
    </div>
    <label>联系人</label>
    <div  class="contacts">
        批发商：<span class="saler-name">迈图国旅（十大旅游）</span>
        联系人：<div  class="dl-select small-dl">
            <input style="width: 90px;" type="text" placeholder="请选择">
            <ul style="width: 102px;"  class="select-option">
                <li  phone="123">张三</li>
                <li  phone="456">李四</li>
            </ul>
        </div>
        电话：<span class="saler-name">123456789</span>
    </div>
    <label for="">交易明细</label>
    <table   class="sus-table">
        <thead>
            <tr>
                <th></th>
                <th>成人</th>
                <th>儿童</th>
                <th>特殊人群</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>实际结算价</td>
                <td>￥101/人</td>
                <td>￥101/人</td>
                <td>￥101/人</td>
            </tr>
            <tr>
                <td  colspan="4"  class="summary">门店结算价差额返还总计：￥<span>200</span></td>
            </tr>
        </tbody>
    </table>
    <label >备注</label>
    <textarea  name="" id=""  maxlength="150" class="remark-order">12233</textarea>
    <div  class="buttons">
        <span  class="unable" onclick="modifyOrder()">上一步</span><span  onclick="getPayMethod();">下一步</span>
    </div>--%>
</div>
<!--538支付方式弹窗-->
<div  id="pay"  style="display: none;">
    <div  class="pay-tip">请选择您要收款的方式：</div>
    <div class="tab-container">
        <ul class="tabs">
            <li onclick="payTabChange(this,1)" class="active"><a href="#bank">银行卡</a></li>
            <li onclick="payTabChange(this,2)" class=""><a href="#alipay">支付宝</a></li>
            <li onclick="payTabChange(this,3)" class=""><a href="#weChat">微信</a></li>
        </ul>
        <div id="pay-child" class="tab_containers"  style="clear: both;">
            <div id="bank" class="tab_content" style="display: block;">
                <ul  class="bank-list">
                    <li><input name="bankNo" type="radio"><span  class="bank-info"  title="北京银行商业银行">北京银行商业银行</span><span  class="card-info">尾号3646</span><span>姓名：张三</span></li>
                    <li><input name="bankNo" type="radio"><span  class="bank-info"  title="北京银行商业银行">北京银行商业银行</span><span  class="card-info">尾号2356</span><span>姓名：张三</span></li>
                    <li><input name="bankNo" type="radio"><span  class="bank-info"  title="北京银行商业银行">北京银行商业银行</span><span  class="card-info">尾号2569</span><span>姓名：张三</span></li>
                </ul>
            </div>

            <div id="alipay" class="tab_content" style="display: none;">
                <ul class="bank-list">
                    <li><input name="bankNo" type="radio"><span class="alipy_img"></span><span  class="card-info">尾号3646</span><span>姓名：张三</span></li>
                    <li><input name="bankNo" type="radio"><img src="${ctxStatic}/images/alipay.jpg"  alt=""><span  class="card-info">尾号2356</span><span>姓名：张三</span></li>
                    <li><input name="bankNo" type="radio"><img src="${ctxStatic}/images/alipay.jpg"  alt=""><span  class="card-info">尾号2569</span><span>姓名：张三</span></li>
                </ul>
            </div>

            <div id="weChat" class="tab_content" style="display: none;">
                <ul class="bank-list">
                    <li><input name="wechatNo" type="radio"><span class="alipy_img"></span><span   class="card-info">微信号</span><span>姓名：张三</span></li>
                </ul>
            </div>
        </div>
    </div>
    <div  class="buttons">
        <span  class="unable" onclick="previousStep(2)">上一步</span><span  onclick="submitOrderDetail('${ctx}')">提交</span>
    </div>
</div>
<!--538end-->
</body>
</html>
