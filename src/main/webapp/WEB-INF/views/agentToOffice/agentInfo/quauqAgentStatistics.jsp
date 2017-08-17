<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler" />
<title>渠道订单统计</title>
<script type="text/javascript" src="${ctxStatic }/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic }/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/agentToOffice/agentInfo/quauqAgentStatistics.js" type="text/javascript"></script>
</head>
<body>
	<form:form id="searchForm" method="post" action="${ctx}/quauqAgent/manage/quauqAgentStatistics" modelAttribute="agentinfo">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
		
		<div style="display: block;" class="ydxbd">
			<div class="activitylist_bodyer_right_team_co1"  style="width:206px;">
				<label class="activitylist_team_co3_text">渠道名称：</label>
				<select name="agentId" id="agentId">
					<option value="" selected="">全部</option>
					<c:forEach var="agentinfo" items="${agentList }">
						<option value="${agentinfo.id}" <c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1" style="width:206px;">
				<div class="activitylist_team_co3_text" style="width:90px;">订单状态：</div>
				<select name="orderStatus" id="orderStatus">
					<option value="" <c:if test="${empty orderStatus}">selected="selected"</c:if>>全部订单</option>
					<option value="1" <c:if test="${orderStatus==1 }">selected="selected"</c:if>>未支付全款</option>
					<option value="2" <c:if test="${orderStatus==2 }">selected="selected"</c:if>>未支付订金</option>
					<option value="5" <c:if test="${orderStatus==5 }">selected="selected"</c:if>>已支付全款</option>
					<option value="4" <c:if test="${orderStatus==4 }">selected="selected"</c:if>>已支付订金</option>
					<option value="3" <c:if test="${orderStatus==3 }">selected="selected"</c:if>>已占位</option>
					<option value="7" <c:if test="${orderStatus==7 }">selected="selected"</c:if>>待计调确认</option>
					<option value="8" <c:if test="${orderStatus==8 }">selected="selected"</c:if>>待财务确认</option>
					<option value="9" <c:if test="${orderStatus==9 }">selected="selected"</c:if>>已撤销占位</option>
					<option value="99" <c:if test="${orderStatus==99 }">selected="selected"</c:if>>已取消</option>
					<option value="111" <c:if test="${orderStatus==111 }">selected="selected"</c:if>>已删除</option>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co2" >
				<div class="activitylist_team_co3_text" style="width:90px;">下单日期：</div>
				<input readonly="readonly" onclick="WdatePicker()" value="${orderTimeBegin}" name="orderTimeBegin" class="inputTxt dateinput">
				<span>至</span>
				<input readonly="readonly" onclick="WdatePicker()" value="${orderTimeEnd}" name="orderTimeEnd" class="inputTxt dateinput">
			</div>
			<div class="activitylist_bodyer_right_team_co1" style="width:206px;">
				<input value="搜索" id="seachbutton" class="btn btn-primary ydbz_x" type="button" onclick="search(1)">
			</div>
			<div class="kong"></div>
			<div class="activitylist_bodyer_right_team_co1" style="width:206px;">
				<label class="activitylist_team_co3_text">订单号：</label>
				<input id="orderNum" name="orderNum" value="${orderNum}" type="text" style="width:100px;"/>
			</div>
			<div class="activitylist_bodyer_right_team_co1" style="width:206px;">
				<div class="activitylist_team_co3_text" style="width:90px;">团号：</div>
				<input id="groupCode" name="groupCode" value="${groupCode}" type="text"/>
			</div>
			<div class="activitylist_bodyer_right_team_co2" >
				<div class="activitylist_team_co3_text" style="width:90px;">供应商：</div>
				<select name="companyId" id="companyId">
					<option value="" selected="">全部</option>
					<c:forEach var="office" items="${officeList}">
						<option value="${office.id}" <c:if test="${companyId==office.id}">selected="selected"</c:if>>${office.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1" style="width:206px;">
				<input class="btn btn-primary" value="导出Excel" type="button" onclick="search(2)">
			</div>
			<div class="kong"></div>
		</div>
	</form:form>
	
	<table class="activitylist_bodyer_table" id="contentTable_quauq">
		<thead>
			<tr>
				<th width="4%">序号</th>
				<th width="8%">渠道名称</th>
				<th width="12%">下单日期</th>
				<th width="8%">订单号</th>
				<th width="8%">团号</th>
				<th width="4%">订单人数</th>
				<th width="8%">订单结算价</th>
				<th width="8%">QUAUQ结算价</th>
				<th width="8%">服务费</th>
				<th width="8%">订单状态</th>
				<th width="8%">供应商</th>
				<th width="8%">操作</th>
			</tr>
		</thead>
		<tbody class="orderOrGroup_group_tbody">
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
					<td colspan="12" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>
       
			<c:forEach items="${page.list}" var="order" varStatus="s">
				<tr>
					<td class="tc">${s.index + 1}</td>
					<td class="tc">${order.agentName}</td>
					<td class="tc"><fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td class="tc">${order.orderNum}</td>
					<td class="tc">${order.groupCode}</td>
					<td class="tc">${order.orderPersonNum}</td>
					<td class="tc">${order.totalMoney}</td>
					<td class="tc">${order.quauqTotalMoney}</td>
					<td class="tc">${order.quauqServiceCharge}</td>
					<td class="tc">
						<c:choose>
							<c:when test="${order.delFlag eq '3'}">待生成订单</c:when>
							<c:when test="${order.delFlag eq '4'}">未生成订单</c:when>
							<c:otherwise>${fns:getDictLabel(order.orderStatus, "order_pay_status", "")}</c:otherwise>
						</c:choose>
					</td>
					<td class="tc">${order.officeName}</td>
					<td class="tc">
						<a href="${ctx}/orderCommon/manage/orderDetail/${order.orderId}" target="_blank">订单详情</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div class="pagination clearFix">
		${page}
		<div style="clear:both;"></div>
	</div>
</body>
</html>

