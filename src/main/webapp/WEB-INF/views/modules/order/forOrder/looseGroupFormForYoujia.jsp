<%--
  Created by IntelliJ IDEA.
  Date: 2016/4/12
  Time: 11:08
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<form id="groupform" name="groupform" action="" method="post" >
    <table id="contentTable" class="table mainTable activitylist_bodyer_table">
        <thead>
        <th width="3%">序号</th>
        <th width="5%">团号</th>
        <th width="8%">产品名称</th>
        <th width="6%">同行价</th>
        <c:if test="${user.quauqBookOrderPermission eq '1' }">
	        <th width="6%">QUAUQ价</th>
        </c:if>
        <th width="4%">出团日期</th>
        <th width="4%">资料截止日期</th>
        <th width="4%">出发地</th>
        <th width="4%">航空</th>
        <th width="4%">订金</th>
        <th width="4%">预收</th>
        <th width="4%">已收</th>
        <th width="4%">余位</th>
        <th width="4%">补位</th>
        <th width="4%">预报名</th>
        <th width="6%">
            <div>已切位</div>
            <div>售出切位</div>
        </th>
        <th width="7%">操作员</th>
        <th width="5%">操作</th>
        </thead>
        <c:if test="${fn:length(page.list) <= 0 }">
            <tbody>
            <tr class="toptr" >
                <td colspan="18" style="text-align: center;">
                    暂无搜索结果
                </td>
            </tr>
            </tbody>
        </c:if>
        <c:forEach items="${page.list}" var="group" varStatus="s">
            <c:set var="activity" value="${group.travelActivity}"></c:set>
            <tbody>
            <tr>
                <td>
                    <div class="table_borderLeftN">
                        <input type="checkbox" name="groupid" value="${group.id}" <c:if test="${fn:contains(groupIds,fn:trim(group.id))}">checked="checked"</c:if> onchange="idcheckchg(this)" />
                            ${s.count}<br/><br/>
                    </div>
                </td>
                <td class="word-break-all">
                    <div class="">
                        <input type="hidden" name="groupID" value="${group.id}"/>
                        <span style="word-break:break-all; display:block; word-wrap:break-word;">${group.groupCode}</span>
                    </div>
                </td>
                <td>
                    <div class="" title="${activity.acitivityName}">
                        <a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail4Groups/${group.srcActivityId}/${group.id}')">${activity.acitivityName}</a>
                    </div>
                </td>
                <td class="tr">     <!-- 同行价 -->
                    <c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if>
                    <c:if test="${activity.settlementAdultPrice>0}">
                        ${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<span class="tdred fbold"><fmt:formatNumber value="${activity.settlementAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起
                    </c:if>
                </td>
                <c:if test="${user.quauqBookOrderPermission eq '1' }">
	                <td class="tr">     <!-- QUAUQ价 -->
	                    <c:if test="${group.quauqAdultPrice==0}">价格待定</c:if>
	                    <c:if test="${group.quauqAdultPrice>0}">
	                        ${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<span class="tdred fbold"><fmt:formatNumber value="${group.quauqAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起
	                    </c:if>
	                </td>
                </c:if>
                <td class="p0" width="8%">
                    <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></div>
                        <%--<div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></div>--%>
                </td>
                <td width="8%" class="tc">
                    <span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate}"/></span>
                </td>
                <td>${activity.fromAreaName}</td>
                <td>
                    <label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label>
                </td>
                <td class="tr">
                    <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
                    <span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit}" /></span>
                </td>
                <td class="tc">   <%-- 预收 --%>
                    <span>${group.planPosition }</span>
                </td>
                <td class="tc">  <%-- 已收 --%>
                    <span>${group.totalOrderPersonNum}</span>
                </td>
                <td class="soldPayPosition${group.id}"> <%-- 余位 --%>
                    <span>${group.freePosition}</span>
                </td>
                <td>0</td> <%-- 补位暂时为0 --%>
                <td class="tc">
                    <span>${group.orderPersonNum }</span>
                </td>
                <td class="p0" width="7%">
                    <div class="out-date">${group.payReservePosition}</div>
                    <div class="close-date">${group.soldPayPosition}</div>
                </td>
                <td class="tc" title="电话：${group.createBy.mobile}">
                        ${group.createBy.name}
                </td>
                <td class="p0">
                    <%@ include file="/WEB-INF/views/modules/order/forOrder/payModeType.jsp"%>
                </td>
            </tr>

            <!--299-散拼团期列表-start-->
            <tr class="pricePlanContainer" style="display:none;">
                <td colspan="22">
                    <table name="pricePlanTable" id="pricePlanTable"
                           class="table activitylist_bodyer_table border-table-spread"
                           style="margin: 0 auto">
                        <thead>
                        <tr>
                            <th rowspan="2" class="tc">序号</th>
                            <th rowspan="2" class="tc" style="width: 700px">
                                价格方案
                            </th>
                            <th colspan="3" class="tc t-th2">同行价</th>
                            <th colspan="3" class="tc t-th2">直客价</th>
                            <th rowspan="2" class="tc">备注</th>
                        </tr>
                        <tr>
                            <th class="tc">成人</th>
                            <th class="tc">儿童</th>
                            <th class="tc">特殊人群</th>
                            <th class="tc">成人</th>
                            <th class="tc">儿童</th>
                            <th class="tc">特殊人群</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </td>
            </tr>
            <!--299-散拼团期列表-end-->
            <%@ include file="/WEB-INF/views/modules/order/forOrder/salerDetail.jsp"%>
            
            <!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
            
            <!-- 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
            <tr class="groupNoteCol"
                 <c:if test="${companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">style="display: none;"</c:if>>
                 <td name="colspanCount" colspan="">备注:
                     <span class="groupNoteContent">${group.groupRemark}</span>
                 </td>
             </tr>
            <%-- <tr class="groupNoteCol" style="display: table-row;">
                <td colspan="23">备注:
                    <span class="groupNoteContent">${group.groupRemark}</span>
                </td>
            </tr> --%>
			
            <!-- 576需求-报名团期备注默认展开***end*** -->
            
            </tbody>
        </c:forEach>
    </table>
    <c:set var="userinfo" value="${fns:getUser()}"/>
    <input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
    <!-- 获取批发商是否允许补单的值：0为否，1为是 -->
    <c:set var="isAllowSupplement" value="${userinfo.company.isAllowSupplement}"></c:set>
    <input type="hidden" id="isAllowSupplement" value="${isAllowSupplement}"/>
</form>