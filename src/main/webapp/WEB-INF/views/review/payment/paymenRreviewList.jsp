<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>成本付款-列表</title>
    <!-- 页面左边和上边的装饰 -->
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <link href="http://www.trekiz.com/favicon.ico" rel="shortcut icon"/>
    <!--[if lte IE 6]>
    <link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css"/>
    <script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
    <![endif]-->
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"/>
    <script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
    <%-- <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script> --%>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/review/payment/paymentReviewList.js"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
<form id="searchForm" action="${ctx}/review/payment/web/getPaymentReviewList" method="post">
    <input id="tabStatus" name="tabStatus" type="hidden" value="${paymentParam.tabStatus}">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2 pr">
            <%--<label></label>--%>
            <input class="searchInput inputTxtlong" id="wholeSalerKey"
                   name="groupCodeProductName" value="${paymentParam.groupCodeProductName}" placeholder="团号/产品名称"
                   type="text"/>
            <%--<span style="display: block;" class="ipt-tips">团号/产品名称</span>--%>
        </div>
        <a class="zksx">筛选</a>
        <div class="form_submit">
            <input class="btn btn-primary" value="搜索" type="submit">
        </div>
        <div class="ydxbd">
            <span></span>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">产品类型：</label>
                <div class="selectStyle">
                    <select name="orderType">
                        <c:forEach var="type" items="${orderTypes }">
                            <option value="${type.value }"
                                    <c:if test="${paymentParam.orderType==type.value}">selected="selected"</c:if>>${type.label }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co3">
                <label class="activitylist_team_co3_text">地接社：</label>
                <select name="supplyId" id="supplyId" class="width-select-channel">
                    <option value="" selected="selected">全部</option>
                    <c:forEach var="supplier" items="${supplierList }">
                        <option value="${supplier.id}"
                                <c:if test="${paymentParam.supplyId==supplier.id}">selected="selected"</c:if>>${supplier.supplierName}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">申请日期：</label>
                <input id="" class="inputTxt dateinput" name="applyBeginDate" value="${paymentParam.applyBeginDate}"
                       onclick="WdatePicker()" readonly="readonly"/>
                <span> 至 </span>
                <input id="" class="inputTxt dateinput" name="applyEndDate" value="${paymentParam.applyEndDate}"
                       onclick="WdatePicker()" readonly="readonly"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">审批发起人：</label>
                <select name="reviewerId" id="reviewerId">
                    <option value="" selected="selected">全部</option>
                    <c:forEach var="reviewer" items="${reviewerList }">
                        <option value="${reviewer.id}"
                                <c:if test="${paymentParam.reviewerId==reviewer.id}">selected="selected"</c:if>>${reviewer.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co3">
                <label class="activitylist_team_co3_text">渠道选择：</label>
                <select name="agentId" id="agentId" class="width-select-channel">
                    <option value="">全部</option>
                    <c:forEach var="agent" items="${agentList }">
                        <c:choose>
                            <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agent.agentName=='非签约渠道'}">
                                <option value="${agent.id }"
                                        <c:if test="${paymentParam.agentId==agent.id}">selected="selected"</c:if>>直客
                                </option>
                            </c:when>
                            <c:otherwise>
                                <option value="${agent.id }"
                                        <c:if test="${paymentParam.agentId==agent.id}">selected="selected"</c:if>>${agent.agentName}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">出团日期：</label>
                <input id="" class="inputTxt dateinput" name="groupOpenDateBegin" value="${groupOpenDateBegin}"
                       onclick="WdatePicker()" readonly="readonly"/>
                <span> 至 </span>
                <input id="" class="inputTxt dateinput" name="groupOpenDateEnd" value="${groupOpenDateEnd}"
                       onclick="WdatePicker()" readonly="readonly"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">审批状态：</label>
                <div class="selectStyle">
                    <select name="reviewStatus">
                        <option value="-1" <c:if test="${paymentParam.reviewStatus eq '-1'}">selected="selected"</c:if>>全部
                        </option>
                        <option value="1" <c:if test="${paymentParam.reviewStatus eq '1'}">selected="selected"</c:if>>审批中
                        </option>
                        <option value="2" <c:if test="${paymentParam.reviewStatus eq '2'}">selected="selected"</c:if>>审批通过
                        </option>
                        <option value="0" <c:if test="${paymentParam.reviewStatus eq '0'}">selected="selected"</c:if>>审批驳回
                        </option>
                        <option value="3" <c:if test="${paymentParam.reviewStatus eq '3'}">selected="selected"</c:if>>取消申请
                        </option>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">付款金额：</label>
                <input type="text" value="${paymentParam.payMoneyBegin}"
                       name="payMoneyBegin" id="startMoney"
                       class="inputTxt"
                       placeholder="输入金额"
                       onkeyup="validNum(this)"
                       onafterpaste="validNum(this))"/>
                <span>至</span>
                <input type="text" value="${paymentParam.payMoneyEnd}"
                       name="payMoneyEnd" id="endMoney"
                       class="inputTxt"
                       placeholder="输入金额"
                       onkeyup="validNum(this)"
                       onafterpaste="validNum(this))"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">出纳确认：</div>
                <div class="selectStyle">
                    <select name="payStatus">
                        <option value="-1" <c:if test="${paymentParam.payStatus eq '-1'}">selected="selected"</c:if>>全部
                        </option>
                        <option value="0" <c:if test="${paymentParam.payStatus eq '0'}">selected="selected"</c:if>>未付
                        </option>
                        <option value="1" <c:if test="${paymentParam.payStatus eq '1'}">selected="selected"</c:if>>已付
                        </option>
                    </select>
                </div>
            </div>
            <%-- <div class="activitylist_bodyer_right_team_co1">
                    <label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
                    <select name="paymentType">
                        <option value="">不限</option>
                        <c:forEach var="pType" items="${fns:findAllPaymentType()}">
                            <option value="${pType[0] }"
                                <c:if test="${paymentParam.paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
                        </c:forEach>
                    </select>
                </div> --%>
        </div>
        <div class="kong"></div>
    </div>
</form>
<div class="supplierLine">
    <a href="javascript:void(0)" onclick="changeIsRecord(0)" id="tabStatus_0">全部</a>
    <a href="javascript:void(0)" onclick="changeIsRecord(1)" id="tabStatus_1">待本人审批</a>
    <a href="javascript:void(0)" onclick="changeIsRecord(2)" id="tabStatus_2">本人已审批</a>
    <a href="javascript:void(0)" onclick="changeIsRecord(3)" id="tabStatus_3">非本人审批</a>
</div>
<div class="activitylist_bodyer_right_team_co_paixu">
    <div class="activitylist_paixu">
        <div class="activitylist_paixu_left">
            <ul>
                <%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
                <li class="activitylist_paixu_left_biankuang licreate_date"><a
                        onclick="sortby('create_date',this)">创建时间</a></li>
                <li class="activitylist_paixu_left_biankuang liupdate_date"><a
                        onclick="sortby('update_date',this)">更新时间</a></li>
            </ul>
        </div>
        <div class="activitylist_paixu_right">查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条</div>
        <div class="kong"></div>
    </div>
</div>
<table id="contentTable" class="table mainTable activitylist_bodyer_table">
    <thead>
    <tr>
        <th width="4%">序号</th>
        <th width="10%"><span class="tuanhao on">团号</span> / <span class="chanpin">产品名称</span></th>
        <th width="5%">产品类型</th>
        <th width="5%">申请时间</th>
        <th width="7%">审批发起人</th>
        <th width="6%"><span class="supplier on">地接社</span> / <span class="agent">渠道商</span></th>
        <c:choose>
            <c:when test="${CompanyId eq 'f5c8969ee6b845bcbeb5c2b40bac3a23' }">
                <th width="6%">游客</th>
            </c:when>
            <c:otherwise>
                <th width="6%">出团日期<br/>截团日期</th>
            </c:otherwise>
        </c:choose>
        <th width="6%">预收/余位</th>
        <th width="6%">款项名称</th>
        <th width="6%">付款金额</th>
        <th width="7%">汇率</th>
        <th width="6%">上一环节审批人</th>
        <th width="7%">审批状态</th>
        <th width="7%">出纳确认</th>
        <th width="6%">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${fn:length(page.list) <= 0 }">
        <tr class="toptr">
            <td colspan="30" style="text-align: center;">暂无搜索结果</td>
        </tr>
    </c:if>
    <c:forEach items="${page.list}" var="item" varStatus="s">
        <tr>
            <td><c:if test="${paymentParam.tabStatus eq '1'}">
                <input type="checkbox" name="batchItem" value="${item.review_uuid }">
            </c:if>${s.count}</td>
            <td>
                <div title="${item.group_code}" class="tuanhao_cen onshow">${item.group_code}</div>
                <div title="${item.product_name}" class="chanpin_cen qtip">${item.product_name}</div>
            </td>
            <td class="tc">${fns:getStringOrderStatus(item.product_type)}</td>
            <td class="p0">
                <div class="out-date"><fmt:formatDate value="${item.create_date}" pattern="yyyy-MM-dd"/></div>
                <div class="close-date time"><fmt:formatDate value="${item.create_date}" pattern="HH:mm:ss"/></div>
            </td>
            <td class="tc">${fns:getUserNameById(item.create_by)}</td>
            <td class="tc">
                <div title="${item.supplyName}" class="supplier_cen onshow">${item.supplyName}</div>
                <div title="${item.agentName}" class="agent_cen qtip">
                    <c:choose>
                        <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && item.agentName=='非签约渠道'}">
                            直客
                        </c:when>
                        <c:otherwise>
                            ${item.agentName}
                        </c:otherwise>
                    </c:choose>
                </div>
            </td>
            <c:if test="${CompanyId eq 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                <td class="p0">
                    <c:choose>
                        <c:when test="${6 eq item.product_type }">
                            <div title="${item.comment}">${fns:abbr(item.comment,20)}</div>
                        </c:when>
                        <c:otherwise> </c:otherwise>
                    </c:choose>
                </td>
            </c:if>
            <c:if test="${CompanyId ne 'f5c8969ee6b845bcbeb5c2b40bac3a23'}">
                <td class="p0">
                    <div class="out-date">${item.groupOpenDate}</div>
                    <div class="close-date">${item.groupCloseDate}</div>
                </td>
            </c:if>
            <td class="p0">
                <div class="out-date">
                    <c:choose>
                        <c:when test="${-1 eq item.planPosition }"> -- </c:when>
                        <c:otherwise>${item.planPosition}</c:otherwise>
                    </c:choose>
                </div>
                <div class="close-date">
                    <c:choose>
                        <c:when test="${-1 eq item.freePosition }"> -- </c:when>
                        <c:otherwise>${item.freePosition}</c:otherwise>
                    </c:choose>
                </div>
            </td>
            <td class="tc">${item.name}</td>
            <td class="tr">${fns:getCurrencyNameOrFlag(item.currencyId,'0') }<span
                    class="fbold tdred">${item.price}</span></td>
            <td class="tc">${item.rate}</td>
            <td class="tc">${fns:getUserNameById(item.last_reviewer)}</td>
            <td class="invoice_yes tc">${fns:getChineseReviewStatus(item.status,item.current_reviewer)}</td>
            <td class="invoice_yes tc">
                <c:if test="${item.payStatus eq '1'}">已付</c:if>
                <c:if test="${item.payStatus eq '0'}">未付</c:if>
                <c:if test="${item.payStatus ne '1' and item.payStatus ne '0'}">其他</c:if>
            </td>
            <td class="p0">
                <dl class="handle">
                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"/></dt>
                    <dd class="">
                        <p><span></span>
                            <!-- 单团，散拼，自由行，大客户，游学，游轮 -->
                            <c:if test="${item.product_type < 6 or item.product_type == 10}">
                                <a href="${ctx}/review/activity/payment/activityRead/${item.productId}/${item.groupId}?costId=${item.costId}"
                                   target="_blank">查看</a>
                                <a href="${ctx}/cost/manager/forcastList/${item.groupId}/${item.product_type}"
                                   target="_blank">预报单</a>
                                <a href="${ctx}/cost/manager/settleList/${item.groupId}/${item.product_type}"
                                   target="_blank">结算单</a>
                                <c:if test="${paymentParam.tabStatus eq '1'}"> <!-- 待本人审批 -->
                                    <a href="${ctx}/review/activity/payment/activityReview/${item.productId}/${item.groupId}?costId=${item.costId}"
                                       target="_blank">审批</a>
                                </c:if>
                                <c:if test="${fns:isBackReview(item.review_uuid) and paymentParam.tabStatus eq '2'}"> <!-- 本人已审批 -->
                                    <a href="javascript:void(0)" onclick="back_review('${ctx}','${item.review_uuid}')">撤消</a>
                                </c:if>
                            </c:if>
                            <!-- 机票和签证 -->
                            <c:if test="${item.product_type == 6 or item.product_type == 7}">
                                <c:if test="${item.product_type == 6}">
                                    <a href="${ctx}/review/visa/payment/visaRead/${item.productId}?costId=${item.costId}"
                                       target="_blank">查看</a>
                                    <a href="${ctx}/cost/manager/forcastList/${item.productId}/${item.product_type}"
                                       target="_blank">预报单</a>
                                    <a href="${ctx}/cost/manager/settleList/${item.productId}/${item.product_type}"
                                       target="_blank">结算单</a>
                                    <c:if test="${paymentParam.tabStatus eq '1'}"> <!-- 待本人审批 -->
                                        <a href="${ctx}/review/visa/payment/visaReview/${item.productId}?costId=${item.costId}"
                                           target="_blank">审批</a>
                                    </c:if>
                                    <c:if test="${fns:isBackReview(item.review_uuid) and paymentParam.tabStatus eq '2'}"> <!-- 本人已审批 -->
                                        <a href="javascript:void(0)"
                                           onclick="back_review('${ctx}','${item.review_uuid}')">撤消</a>
                                    </c:if>
                                </c:if>
                                <c:if test="${item.product_type == 7}">
                                    <a href="${ctx}/review/airticket/payment/airTicketRead/${item.productId}?costId=${item.costId}"
                                       target="_blank">查看</a>
                                    <a href="${ctx}/cost/manager/forcastList/${item.productId}/${item.product_type}"
                                       target="_blank">预报单</a>
                                    <a href="${ctx}/cost/manager/settleList/${item.productId}/${item.product_type}"
                                       target="_blank">结算单</a>
                                    <c:if test="${paymentParam.tabStatus eq '1'}"> <!-- 待本人审批 -->
                                        <a href="${ctx}/review/airticket/payment/airTicketReview/${item.productId}?costId=${item.costId}"
                                           target="_blank">审批</a>
                                    </c:if>
                                    <c:if test="${fns:isBackReview(item.review_uuid) and paymentParam.tabStatus eq '2'}"> <!-- 本人已审批 -->
                                        <a href="javascript:void(0)"
                                           onclick="back_review('${ctx}','${item.review_uuid}')">撤消</a>
                                    </c:if>
                                </c:if>
                            </c:if>
                        </p>
                    </dd>
                </dl>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<div class="page">
    <c:if test="${paymentParam.tabStatus eq '1'}">
        <div class="pagination">
            <dl>
                <dt><input name="allChk" onclick="checkall(this)" type="checkbox">全选</dt>
                <dt><input name="reverseChk" onclick="checkreverse(this)" type="checkbox">反选</dt>
                <dd>
                    <input class="btn ydbz_x" type="button" value="批量审批"
                           onclick="jbox__shoukuanqueren_chexiao_fab('${ctx}');">
                </dd>
            </dl>
        </div>
    </c:if>
    <div class="pagination">${page}
        <div style="clear:both;"></div>
    </div>
</div>
<!-- 批量审批弹出框 -->
<div class="batch-verify-list" id="batch-verify-list" style="padding:20px;">
    <table width="100%" style="padding:10px !important; border-collapse: separate;">
        <tr>
            <td></td>
        </tr>
        <tr>
            <td><p></p></td>
        </tr>
        <tr>
            <td><p>备注：</p></td>
        </tr>
        <tr>
            <td>
                <label>
                    <textarea name="comment_val" id="comment_val" style="width: 290px;" maxlength="100"
                              oninput="this.value=this.value.substring(0,100)"></textarea>
                </label>
            </td>
        </tr>
    </table>
</div>
</body>
</html>
