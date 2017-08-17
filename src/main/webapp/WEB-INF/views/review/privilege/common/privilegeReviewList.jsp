<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>优惠审批-列表</title>
    <meta name="decorator" content="wholesaler"/>
    <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jbox.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css"/>
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css"/>
    <script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/review/privilege/privilegeReview.js"></script>
    <script type="text/javascript" src="${ctxStatic}/review/privilege/batchPrivilegeReview.js"></script>
    <style type="text/css">
        .text-more-new .activitylist_team_co3_text, .text-more-new .activitylist_bodyer_right_team_co2 label {
            width: 90px;
            text-align: right;
        }

        .text-more-new .activitylist_bodyer_right_team_co1, .text-more-new .activitylist_bodyer_right_team_co3 {
            min-width: 230px;
        }

        .text-more-new .activitylist_bodyer_right_team_co2 {
            min-width: 400px;
        }
    </style>
</head>
<body>
<input type="hidden" id="ctx" value="${ctx}"/>
<form id="searchForm" action="${ctx}/privilegeReview/list" method="post">
    <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
    <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
    <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
    <input id="tabStatus" name="tabStatus" type="hidden" value="${conditionsMap.tabStatus}"/>
    <input id="ctx" type="hidden" value="${ctx}"/>
    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2 pr">
            <label></label>
            <input id="groupCode" name="groupCode"
                   class="inputTxt inputTxtlong searchInput" value="${conditionsMap.groupCode}"
                   flag="istips" onkeyup="this.value=this.value.replaceColonChars()"
                   onafterpaste="this.value=this.value.replaceColonChars()" placeholder="请输入团号/产品名称/订单号"/>
            <%--<span class="ipt-tips" style="display: block;">团号/产品名称/订单号</span>--%>
        </div>
        <a class="zksx">筛选</a>
        <div class="form_submit">
            <input class="btn btn-primary ydbz_x" value="搜索" type="submit">
        </div>
        <%--<div class="ydxbd text-more-new">--%>
        <div class="ydxbd">
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">产品类型：</label>
                <div class="selectStyle">
                    <select name="productType">
                        <c:forEach var="order" items="${fns:getDictList('order_type')}">
                            <c:if test="${order.value=='2' or order.value=='0'}">
                                <option value="${order.value }"
                                        <c:if test="${conditionsMap.productType==order.value}">selected="selected"</c:if>>${order.label}
                                </option>
                            </c:if>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co3">
                <label class="activitylist_team_co3_text">渠道选择：</label>
                <select name="agentId" id="agentId" class="width-select-channel">
                    <option value="">全部</option>
                    <c:if test="${not empty fns:getAgentListAddSort()}">
                        <c:forEach items="${fns:getAgentListAddSort()}" var="agentinfo">
                            <option value="${agentinfo.id }"
                                    <c:if test="${conditionsMap.agentId==agentinfo.id }">selected="selected"</c:if>>${agentinfo.agentName}</option>
                        </c:forEach>
                    </c:if>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">审批发起人：</label>
                <select name="applyPerson" id="saler">
                    <option value="" selected="selected">全部</option>
                    <!-- 用户类型  1 代表销售 -->
                    <c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
                        <option value="${userinfo.id }"
                                <c:if test="${conditionsMap.applyPerson==userinfo.id }">selected="selected"</c:if>>${userinfo.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <%-- 	<div class="activitylist_bodyer_right_team_co2">
                    <label class="activitylist_team_co3_text">优惠金额：</label>
                    <input id="rebatesDiffBegin" class="inputTxt " name="rebatesDiffBegin"  value='${conditionsMap.rebatesDiffBegin }' onblur="refundInputs(this)"/>
                    <span style="font-size:12px; font-family:'宋体';"> 至</span>
                    <input id="rebatesDiffEnd" class="inputTxt " name="rebatesDiffEnd" value='${conditionsMap.rebatesDiffEnd }'onblur="refundInputs(this)" />
                </div> --%>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">出纳确认：</label>
                <div class="selectStyle">
                    <select id="payStatus" name="payStatus">
                        <option value="">全部</option>
                        <option value="0" <c:if test="${conditionsMap.payStatus == '0' }"> selected="selected" </c:if>>未付
                        </option>
                        <option value="1" <c:if test="${conditionsMap.payStatus == '1' }"> selected="selected" </c:if>>已付
                        </option>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">审批状态：</label>
                <div class="selectStyle">
                    <select name="reviewStatus" id="reviewStatus">
                        <option value="" selected="selected">全部</option>
                        <option value="1" <c:if test="${conditionsMap.reviewStatus == '1' }"> selected="selected" </c:if>>
                            审批中
                        </option>
                        <option value="2" <c:if test="${conditionsMap.reviewStatus == '2' }"> selected="selected" </c:if>>
                            审批通过
                        </option>
                        <option value="0" <c:if test="${conditionsMap.reviewStatus == '0' }"> selected="selected" </c:if>>
                            审批驳回
                        </option>
                        <option value="3" <c:if test="${conditionsMap.reviewStatus == '3' }"> selected="selected" </c:if>>
                            取消申请
                        </option>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <label class="activitylist_team_co3_text">申请日期：</label>
                <input id="" class="inputTxt dateinput" name="applyDateFrom" value="${conditionsMap.applyDateFrom}"
                       onclick="WdatePicker()" readonly="readonly"/>
                <span>至 </span>
                <input id="" class="inputTxt dateinput" name="applyDateTo" value="${conditionsMap.applyDateTo}"
                       onclick="WdatePicker()" readonly="readonly"/>
            </div>
            <div class="kong"></div>
        </div>
        <div class="kong"></div>
    </div>
</form>
<div class="activitylist_bodyer_right_team_co_paixu">
    <div class="activitylist_paixu_left">
        <ul>
            <%--<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>--%>
            <li class="activitylist_paixu_left_biankuang lir.create_date"><a
                    onClick="sortby('r.create_date',this)">创建时间</a></li>
            <li class="activitylist_paixu_left_biankuang lir.update_date"><a
                    onClick="sortby('r.update_date',this)">更新时间</a></li>
        </ul>
    </div>
</div>
<!--状态开始-->
<div class="supplierLine">
    <a href="javascript:void(0)" id="all" onClick="statusChooses('0')"
       <c:if test="${conditionsMap.tabStatus == null || conditionsMap.tabStatus == 0 || conditionsMap.tabStatus == '0'}">class="select" </c:if>>全部</a>
    <a id="todo" href="javascript:void(0)" onClick="statusChooses('1')"
       <c:if test="${conditionsMap.tabStatus == '1' || conditionsMap.tabStatus == 1}">class="select" </c:if>> 待本人审批</a>
    <a id="todo" href="javascript:void(0)" onClick="statusChooses('2')"
       <c:if test="${conditionsMap.tabStatus == '2' || conditionsMap.tabStatus == 2}">class="select" </c:if>>本人已审批</a>
    <a id="todoing" href="javascript:void(0)" onClick="statusChooses('3')"
       <c:if test="${conditionsMap.tabStatus == '3' || conditionsMap.tabStatus == 3}">class="select" </c:if>>非本人审批</a>
</div>
<!--状态结束-->
<table id="contentTable" class="table activitylist_bodyer_table mainTable">
    <thead>
    <tr>
        <th width="5%">序号</th>
        <th width="8%">订单号</th>
        <th width="9%">
            <span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span>
        </th>
        <th width="6%">产品类型</th>
        <th width="6%">申请时间</th>
        <th width="6%">审批发起人</th>
        <th width="10%">渠道商</th>
        <th width="8%">优惠额度</th>
        <th width="6%">上一环节<br/>审批人</th>
        <th width="10%">审批状态</th>
        <th width="4%">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${fn:length(page.list) <= 0 }">
        <tr class="toptr">
            <td colspan="13" style="text-align: center;">
                暂无搜索结果
            </td>
        </tr>
    </c:if>
    <c:forEach items="${page.list}" var="reviewInfos" varStatus="s">
        <tr>
            <td>
                <c:if test="${conditionsMap.tabStatus == '1'}">
                    <input type="checkbox" value="${reviewInfos.id}@${reviewInfos.productType}" name="activityId">
                </c:if>
                    ${s.count}
            </td>
            <td>${reviewInfos.orderNum}</td>
            <td>
                <div title="${reviewInfos.groupCode}" class="tuanhao_cen onshow">${reviewInfos.groupCode}</div>
                <div title="${reviewInfos.productName}" class="chanpin_cen qtip">
                    <a href="${ctx}/activity/manager/detail/${reviewInfos.productId}"
                       target="_blank">${reviewInfos.productName}</a>
                </div>
            </td>
            <td>散拼</td>
            <td class="p0">
                <div class="out-date"><fmt:formatDate value="${reviewInfos.createDate}" pattern="yyyy-MM-dd"/></div>
                <div class="close-date time"><fmt:formatDate value="${reviewInfos.createDate}"
                                                             pattern="HH:mm:ss"/></div>
            </td>
            <td class="tc">${fns:getUserNameById(reviewInfos.createBy)}</td>
            <td class="tc">${reviewInfos.agentName}</td>
            <td>${reviewInfos.previlege}</td>
            <td class="tc">${fns:getUserNameById(reviewInfos.lastReviewer)}</td>
            <c:choose>
                <c:when test="${reviewInfos.status == 0}">
                    <td class="invoice_back tc">${reviewInfos.statusdesc}</td>
                </c:when>
                <c:otherwise>
                    <td class="invoice_yes tc">${reviewInfos.statusdesc}</td>
                </c:otherwise>
            </c:choose>
            <td class="p0">
                <dl class="handle">
                    <dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
                    <dd class="">
                        <p>
                            <a href="${ctx}/singlegroup/privilege/privilegeDetail?reviewId=${reviewInfos.id}&orderId=${reviewInfos.orderId}"
                               target="_blank">查看</a><%--查看跳转到申请时的详情页 --%>
                            <c:if test="${reviewInfos.isOrdered ==0 }">
                                <a href="${ctx}/cost/manager/forcastList/${reviewInfos.groupId}/2"
                                   target="_blank">预报单</a>
                                <a href="${ctx}/cost/manager/settleList/${reviewInfos.groupId}/2"
                                   target="_blank">结算单</a>
                            </c:if>
                            <c:if test="${reviewInfos.isCurReviewer == true}">
                                <a href="${ctx}/privilegeReview/show?reviewId=${reviewInfos.reviewId}" target="_blank">审批</a>
                            </c:if>
                            <c:if test="${reviewInfos.isBackReview == true}">
                                <a href="javascript:void(0)" onClick="backReview('${reviewInfos.reviewId}')">撤销</a>
                            </c:if>
                        </p>
                    </dd>
                </dl>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<c:if test="${conditionsMap.tabStatus == '1'}">
    <div class="page">
        <div class="pagination">
            <dl>
                <dt>
                    <input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
                </dt>
                <dt>
                    <input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
                </dt>
                <dd>
                    <%--<a target="_blank" id="piliang_o_${result.orderId}"--%>
                       <%--onclick="javascript:payedConfirmNew('${ctx}','/privilegeReview/batchReview');">批量审批</a>--%>
                    <input class="btn ydbz_x" value="批量审批" type="button" onclick="javascript:payedConfirmNew('${ctx}','/privilegeReview/batchReview');">
                </dd>
            </dl>
        </div>
    </div>
</c:if>
<div class="pagination clearFix">${page}</div>
<!--右侧内容部分结束-->
</body>
</html>