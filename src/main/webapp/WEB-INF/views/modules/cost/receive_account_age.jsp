<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>财务-应收账款账龄查询</title>
    <meta name="decorator" content="wholesaler"/>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepickerRefactor.js" type="text/javascript"></script>
    <script src="${ctxStatic}/json/jquery.json.ui.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/jquery.validate.extension.js" type="text/javascript"></script>    
    <script src="${ctxStatic}/jquery-other/jquery-combobox_prop.js" type="text/javascript"></script>
    <script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
    <script src="${ctxStatic}/finance/receive_account_age.js" type="text/javascript"></script>
</head>
<body>
<form method="post" action="${ctx}/receivepay/manager/getReceive" id="searchForm">
    <input type="hidden" value="${page.pageNo}" name="pageNo" id="pageNo">
    <input type="hidden" value="${page.pageSize}" name="pageSize" id="pageSize">
    <input type="hidden" value="${page.orderBy}" name="orderBy" id="orderBy">
    <div class="activitylist_bodyer_right_team_co">
        <div class="activitylist_bodyer_right_team_co2">
            <input class="txtPro inputTxt searchInput" id="wholeSalerKey" name="groupCode" value="${param.groupCode}" placeholder="请输入团号">
        </div>
        <div class="zksx">筛选</div>
        <div class="form_submit">
            <input type="submit" value="搜索" id="seachbutton" class="btn btn-primary ydbz_x">
            <input class="btn ydbz_x" onclick="resetSearchParams();" value="清空所有条件" type="button">
            <input class="btn ydbz_x" onclick="exportToExcel('${ctx}')" value="导出Excel" type="button">
        </div>
        <div style="display:none;" class="ydxbd">
            <span></span>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">类型选择：</div>
                <div class="selectStyle">
                    <select name="orderType">
                        <c:forEach var="type" items="${orderTypes }">
                            <option value="${type.value }" <c:if test="${params.orderType==type.value}">selected="selected"</c:if>>${type.label }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">订单编号：</div>
                <input value="${params.orderNum }" class="inputTxt" name="orderNum" id="orderNum" type="text"/>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">渠道：</div>
                <div class="selectStyle">
                    <select name="agentId">
                        <option value="" selected="selected">全部</option>
                        <c:forEach var="agentinfo" items="${agentinfoList }">
                            <option value="${agentinfo.id }" <c:if test="${params.agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">销售：</div>
                <select name="salerId" id="salerId">
                    <option value="" selected="selected">全部</option>
	                <c:forEach var="salers" items="${salerList }">
	                	<option value="${salers.key }" <c:if test="${params.salerId==salers.key}">selected="selected"</c:if>>${salers.value }</option>
	                </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">部门：</div>
                <div class="selectStyle">
                    <select name="deptId" id="deptId">
                        <option value="" selected="selected">全部</option>
                        <c:forEach var="departmentlist" items="${departmentList}">
                            <option value="${departmentlist.id}" <c:if test="${params.deptId == departmentlist.id}">selected="selected"</c:if>>${departmentlist.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">计调：</div>
                <select name="operatorId" id="operatorId">
                    <option value="">全部</option>
                    <c:forEach var="jd" items="${agentJd }">
	                   	<option value="${jd.id }" <c:if test="${params.operatorId==jd.id}">selected="selected"</c:if>>${jd.name }</option>
	                </c:forEach>
                </select>
            </div>
            <div class="activitylist_bodyer_right_team_co1">
                <div class="activitylist_team_co3_text">坏账标识：</div>
                <div class="selectStyle">
                    <select name="badAccount" id="badAccount">
                        <option value="" selected="selected">全部</option>
                        <option value="Y" <c:if test="${params.badAccount=='Y'}">selected="selected"</c:if>>是</option>
                        <option value="N" <c:if test="${params.badAccount=='N'}">selected="selected"</c:if>>否</option>
                    </select>
                </div>
            </div>
            <%-- <div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
					<select name="paymentType">
						<option value="">不限</option>
						<c:forEach var="pType" items="${fns:findAllPaymentType()}">
							<option value="${pType[0] }"
								<c:if test="${paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
						</c:forEach>
					</select>
				</div> --%>
        </div>
    </div>
</form>
<table class="table activitylist_bodyer_table mainTable" id="contentTable">
    <thead>
        <tr>
            <th width="3%">序号</th>
            <th width="8%">部门</th>
            <th width="4%">计调</th>
            <th width="4%">销售</th>
            <th width="5%">团号</th>
            <th width="6%">订单号</th>
            <th width="7%">渠道</th>
            <th width="6%">应收总额</th>
            <th width="5%">回款总额</th>
            <th width="5%">应收余额</th>
            <th width="4%">回款率</th>
            <th width="5%">应收账期</th>
            <th width="6%">1-30天</th>
            <th width="6%">31-60天</th>
            <th width="6%">61-90天</th>
            <th width="6%">91-180天</th>
            <th width="6%">181-360天</th>
            <th width="4%">坏账标识</th>
        </tr>
    </thead>
    <tbody>
    	<c:if test="${fn:length(page.list) <= 0 }">
	        <tr class="toptr" >
	        	<td colspan="30" style="text-align: center;">暂无搜索结果</td>
	        </tr>
        </c:if>
        <c:forEach items="${page.list }" var="order" varStatus="s">
	        <tr>
	            <td class="tc">${s.count}</td>
	            <td class="tc">${order.department }</td>
	            <td class="tc">${order.operator }</td>
	            <td class="tc">${order.salerName }</td>
	            <td class="tc">${order.groupCode }</td>
	            <td class="tc">${order.orderNum }</td>
	            <td class="tc">${order.agentName }</td>
	            <td class="tr">¥${order.totalMoney }</td>
	            <td class="tr">¥${order.accountedMoney }</td>
	            <td class="tr">¥${order.unreceivedMoney }</td>
	            <td class="tc">${order.rate }%</td>
	            <td class="tc">${order.receivePayDate }</td>
	            <td class="tr">¥${order.accountAge30 }</td>
	            <td class="tr">¥${order.accountAge60 }</td>
	            <td class="tr">¥${order.accountAge90 }</td>
	            <td class="tr">¥${order.accountAge180 }</td>
	            <td class="tr">¥${order.accountAge360 }</td>
	            <td class="tc"><c:choose>
	            					<c:when test="${order.unreceivedMoney ne '0.00' and order.days gt 360}">是</c:when>
	            					<c:otherwise>否</c:otherwise>
	            			   </c:choose>
	            </td>
	        </tr>
        </c:forEach>
    </tbody>
</table>
<div class="pagination">${page}
    <div style="clear:both;"></div>
</div>
</body>
</html>
