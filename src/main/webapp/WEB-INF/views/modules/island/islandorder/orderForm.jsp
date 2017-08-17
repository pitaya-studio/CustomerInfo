<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 查询form -->
<form:form id="searchForm" modelAttribute="islandOrderQuery" action="${ctx}/islandOrder/list/${islandOrderQuery.orderStatus}.htm" method="post">
	<input id="ctx" type="hidden" value="${ctx}" />
	<input id="orderStatus" type="hidden" value="${islandOrderQuery.orderStatus}" />
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
	<input id="isOrder" name="isOrder" type="hidden" value="${islandOrderQuery.isOrder}" />

	<!-- 订单查询DIV -->
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2 pr">
			<input id="orderNumOrGroupCode" name="orderNumOrGroupCode"
				   class="inputTxtlong inputTxt searchInput" value="${islandOrderQuery.orderNumOrGroupCode }" placeholder="请输入团号、订单号" /><%--bug17537--%>
		</div>
		<a class="zksx">筛选</a>
		<div class="form_submit">
			<input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="query(${islandOrderQuery.orderStatus})" value="搜索" />
			<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件">
		</div>

		<div class="ydxbd">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">产品名称：</label>
				<input value="${islandOrderQuery.activityName }"
					   name="activityName" class="txtPro inputTxt" flag="istips" />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">订单状态：</label>
				<div class="selectStyle">
					<select name="orderShowType" id="orderShowType">
						<shiro:hasPermission name="islandOrder:list:allorder">
							<option value="0"
								<c:if test="${islandOrderQuery.orderStatus==0}">selected="selected"</c:if>>全部订单</option>
						</shiro:hasPermission>
						<shiro:hasPermission name="islandOrder:not:singUp">
							<option value="1"
								<c:if test="${islandOrderQuery.orderStatus==1}">selected="selected"</c:if>>待确认报名</option>
						</shiro:hasPermission>
						<shiro:hasPermission name="islandOrder:already:singUp">
							<option value="2"
								<c:if test="${islandOrderQuery.orderStatus==2}">selected="selected"</c:if>>已确认报名</option>
						</shiro:hasPermission>
						<shiro:hasPermission name="islandOrder:list:cancelorder">
							<option value="3"
								<c:if test="${islandOrderQuery.orderStatus==3}">selected="selected"</c:if>>已取消</option>
						</shiro:hasPermission>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">出团日期：</label> 
				<input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateBegin" value='${islandOrderQuery.groupOpenDateBegin }'
					onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly /> 
					<span style="font-size:12px; font-family:'宋体';">至</span> 
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupOpenDateEnd" value='${islandOrderQuery.groupOpenDateEnd }' onClick="WdatePicker()" readonly />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">联系人：</div>
				<input id="orderPersonName" class="inputTxt" name="orderPersonName"
					value="${islandOrderQuery.orderPersonName}">
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">计调：</label>
				<div class="selectStyle">
					<select name="activityCreateBy" id="activityCreateBy" class="selectinput">
						<option value="">请选择</option>
						<c:forEach items="${users }" var="user">
							<option value="${user.id}"
								<c:if test="${islandOrderQuery.activityCreateBy==user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">下单日期：</label>
				<input id="orderTimeBegin" class="inputTxt dateinput"
					   name="orderTimeBegin" value='${islandOrderQuery.orderTimeBegin}'
					   onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeBegin').value==''){$dp.$('orderTimeBegin').value=vvv;}}})"
					   readonly />
				<span style="font-size:12px; font-family:'宋体';">至</span>
				<input id="orderTimeEnd" class="inputTxt dateinput"
					   name="orderTimeEnd" value='${islandOrderQuery.orderTimeEnd}' onClick="WdatePicker()"
					   readonly />
			</div>
			<div class="activitylist_bodyer_right_team_co3">
				<label class="activitylist_team_co3_text">渠道选择：</label> 
				<select name="orderCompany" id="orderCompany" onchange="changeAgent(this);">
					<option value="">全部</option>
					<option value="-1" <c:if test="${islandOrderQuery.orderCompany==-1}">selected="selected"</c:if>>非签约渠道</option>
					<c:forEach var="agentinfo" items="${agentinfoList }">
						<option value="${agentinfo.id }"
							<c:if test="${islandOrderQuery.orderCompany==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1" id="orderCompanyNameDiv" style="display: none;">
				<label class="activitylist_team_co3_text">渠道名称：</label>
				<input id="orderCompanyName" class="inputTxt" name="orderCompanyName"
					value="${islandOrderQuery.orderCompanyName}">
			</div>
		</div>
		<div class="kong"></div>
	</div>
</form:form>

<!-- 下载 -->
<form id="exportForm" action="${ctx}/orderCommon/manage/downloadData" method="post">
	<input type="hidden" id="orderId" name="orderId">
	<input type="hidden" name="orderType" value="${islandOrderQuery.orderStatus}">
	<input type="hidden" id="downloadType" name="downloadType">
	<input type="hidden" value="${islandOrderQuery.orderNum}" id="orderNum" name="orderNum">
</form>
<!-- 导出 -->
<form id="exportTravelesForm" action="${ctx}/activity/manager/exportExcel" method="post">
	<input type="hidden" id="groupId" name="groupId">
	<input type="hidden" name="orderType" value="${islandOrderQuery.orderStatus}">
	<input type="hidden" id="groupCode" name="groupCode">
</form> 