<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 查询form -->
<form:form id="searchForm" modelAttribute="hotelOrderQuery" action="${ctx}/hotelOrder/list/${hotelOrderQuery.orderStatus}.htm" method="post">
	<input id="ctx" type="hidden" value="${ctx}" />
	<input id="orderStatus" type="hidden" value="${hotelOrderQuery.orderStatus}" />
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
	<input id="isOrder" name="isOrder" type="hidden" value="${hotelOrderQuery.isOrder}" />



	<!-- 订单查询DIV -->
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2 pr">
			<input id="orderNumOrGroupCode" name="orderNumOrGroupCode"
				   class="inputTxt searchInput inputTxtlong" value="${hotelOrderQuery.orderNumOrGroupCode }" placeholder="请输入团号、订单号" />
		</div>
		<a class="zksx">筛选</a>

		<div class="form_submit">
			<input id="btn_search" class="btn btn-primary ydbz_x" type="button" onclick="query(${hotelOrderQuery.orderStatus})" value="搜索" />
			<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件">
		</div>

		<div class="ydxbd">
			<span></span>

			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">产品名称：</label>
				<input value="${hotelOrderQuery.activityName }"
					   name="activityName" class="txtPro inputTxt" />
			</div>
			<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
			<%-- <div class="activitylist_bodyer_right_team_co1">
				<label>控房单号：</label> <input value="${hotelOrderQuery.activitySerNum }"
					name="activitySerNum" class="txtPro inputTxt" flag="istips" />
			</div> --%>

			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">订单状态：</label>
				<div class="selectStyle">
					<select name="orderShowType" id="orderShowType">
						<shiro:hasPermission name="hotelOrder:list:allorder">
							<option value="0"
								<c:if test="${hotelOrderQuery.orderStatus==0}">selected="selected"</c:if>>全部订单</option>
						</shiro:hasPermission>
						<shiro:hasPermission name="hotelOrder:not:singUp">
							<option value="1"
								<c:if test="${hotelOrderQuery.orderStatus==1}">selected="selected"</c:if>>待确认报名</option>
						</shiro:hasPermission>
						<shiro:hasPermission name="hotelOrder:already:singUp">
							<option value="2"
								<c:if test="${hotelOrderQuery.orderStatus==2}">selected="selected"</c:if>>已确认报名</option>
						</shiro:hasPermission>
						<shiro:hasPermission name="hotelOrder:list:cancelorder">
							<option value="3"
								<c:if test="${hotelOrderQuery.orderStatus==3}">selected="selected"</c:if>>已取消</option>
						</shiro:hasPermission>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">国家：</label>
				<div class="selectStyle">
					<select name="countryUuid" id="countryUuid" onchange="getAjaxSelect('island',$('#countryUuid'))">
						<option value="">请选择</option>
						<c:forEach items="${countryList}" var="country">
							<option value="${country.uuid}"
								<c:if test="${hotelOrderQuery.countryUuid==country.uuid}">selected="selected"</c:if>>${country.nameCn}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">岛屿：</label>
				<div class="selectStyle">
					<select name="islandUuid" id="islandUuid" class="selectinput" onchange="getAjaxSelect('hotel',this);">
						<option value="">请选择</option>
						<c:forEach items="${islandList}" var="island">
							<option value="${island.uuid}"
								<c:if test="${hotelOrderQuery.islandUuid==island.uuid}">selected="selected"</c:if>>
								${island.islandName}
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<input type="hidden" id="islandUuidValue" value="${hotelOrderQuery.islandUuid}">
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">酒店名称：</label>
				<div class="selectStyle">
					<select id="hotelUuid" name="hotelUuid" onchange="getAjaxSelect('roomtype',this);">
						<option value="" selected="selected">请选择</option>
					</select>
				</div>
			</div>
			<input type="hidden" id="hotelUuidValue" value="${hotelOrderQuery.hotelUuid}">
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">酒店星级：</label>
				<div class="selectStyle">
					<select name="hotelStarUuid" id="hotelStarUuid" class="selectinput">
						<option value="">请选择</option>
						<c:forEach items="${hotelStarList}" var="hotelStar">
							<option value="${hotelStar.uuid}"
								<c:if test="${hotelOrderQuery.hotelStarUuid==hotelStar.uuid}">selected="selected"</c:if>>
								${hotelStar.label}
							</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">房型：</label>
				<div class="selectStyle">
					<select name="hotelRoomUuid" id="roomType">
						<option value="" selected="selected">不限</option>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">下单人：</label>
				<div class="selectStyle">
					<select name="createBy" id="createBy" class="selectinput">
						<option value="">请选择</option>
						<c:forEach items="${users }" var="user">
							<option value="${user.id}"
								<c:if test="${hotelOrderQuery.createBy==user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">计调：</label>
				<div class="selectStyle">
					<select name="activityCreateBy" id="activityCreateBy" class="selectinput">
						<option value="">请选择</option>
						<c:forEach items="${users }" var="user">
							<option value="${user.id}"
								<c:if test="${hotelOrderQuery.activityCreateBy==user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co3">
				<label class="activitylist_team_co3_text">渠道选择：</label> 
				<select name="orderCompany" id="orderCompany" onchange="changeAgent(this);">
					<option value="">全部</option>
					<option value="-1" <c:if test="${hotelOrderQuery.orderCompany==-1}">selected="selected"</c:if>>非签约渠道</option>
					<c:forEach var="agentinfo" items="${agentinfoList }">
						<option value="${agentinfo.id }"
							<c:if test="${hotelOrderQuery.orderCompany==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}</option>
					</c:forEach>
				</select>
			</div>
			<div class="activitylist_bodyer_right_team_co1" id="orderCompanyNameDiv" style="display: none;">
				<label class="activitylist_team_co3_text">渠道名称：</label>
				<input id="orderCompanyName" class="inputTxt" name="orderCompanyName"
					value="${hotelOrderQuery.orderCompanyName}">
			</div>
		</div>
		<div class="kong"></div>
	</div>
</form:form>

<!-- 下载 -->
<form id="exportForm" action="${ctx}/orderCommon/manage/downloadData" method="post">
	<input type="hidden" id="orderId" name="orderId">
	<input type="hidden" name="orderType" value="${hotelOrderQuery.orderStatus}">
	<input type="hidden" id="downloadType" name="downloadType">
	<input type="hidden" value="${hotelOrderQuery.orderNum}" id="orderNum" name="orderNum">
</form>
<!-- 导出 -->
<form id="exportTravelesForm" action="${ctx}/activity/manager/exportExcel" method="post">
	<input type="hidden" id="groupId" name="groupId">
	<input type="hidden" name="orderType" value="${hotelOrderQuery.orderStatus}">
	<input type="hidden" id="groupCode" name="groupCode">
</form> 