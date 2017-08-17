<%@ page contentType="text/html;charset=UTF-8" %>
<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/activity/managerforOrder/list/${showType}/${activityKind}" method="post">
	<div class="lmels-ts" style="display: none;"><img src="${ctxStatic}/logo/lmels-ts.png" />如需预定，请与浪漫俄罗斯相关销售人员联系  010-52877517；010-52899377；010-52906039；13581525134</div>
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
	<input id="agentId" name="agentId" type="hidden" value="${agentId }" />
	<input id="departmentId" name="departmentId" type="hidden" value="${departmentId }" />
	<input id="productOrGroup" name="productOrGroup" type="hidden" value="${productOrGroup}" />
	<input id="productType" name="productType" type="hidden" value="${productType}" />
	<c:set var="companyUuid" value="${fns:getUser().company.uuid}"></c:set>
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2 pr" >
            <input name="wholeSalerKey" id="wholeSalerKey" class="txtPro inputTxt searchInput" value="${wholeSalerKey}"placeholder="输入团号、产品名称"/>
        </div>
		<div class="zksx">筛选</div>
        <div class="form_submit">
            <input class="btn btn-primary ydbz_x" id="seachbutton" type="button" value="搜索" onclick="search(1)"/>
			<shiro:hasPermission name="looseActivity:downloadYw">
				<c:if test="${activityKind == 2}">
					<input class="btn ydbz_x" value="下载余位表" type="button" onclick="search(2)" title="此处仅支持下载今天（包含）以后出团的团期余位信息">
				</c:if>
			</shiro:hasPermission>
			<%--<input class="btn ydbz_x" type="reset" value="清空所有条件"/>--%>
            <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
    	</div>

        <c:if test="${fns:getUser().userType ==1}">
       	    <input type="hidden" name="agentId" class="inputagentId" value="${fns:getUser().agentId}" />
       	</c:if>
		<div class="ydxbd" style="display: none;">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">行程天数：</div>
				<input class="spinner" maxlength="3" name="activityDuration" value="${travelActivity.activityDuration }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')">
			</div>  
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">出发地：</div>
				<div class="selectStyle">
					<form:select path="fromArea" itemValue="key" itemLabel="value">
						<form:option value="">不限</form:option>
						<form:options items="${fromAreas}" />
					</form:select>
				</div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">出团日期：</label><input id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDate"
					value='<fmt:formatDate value="${travelActivity.groupOpenDate }" pattern="yyyy-MM-dd"/>'	 onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})" readonly />
				<span> 至</span>
				<input id="groupCloseDate" class="inputTxt dateinput" name="groupCloseDate"
					value='<fmt:formatDate value="${travelActivity.groupCloseDate }" pattern="yyyy-MM-dd"/>' onClick="WdatePicker()" readonly />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">目的地：</div>
				<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList" labelValue="${targetAreaNames}"
					title="区域" url="/activity/manager/filterTreeData1?kind=${activityKind}" checked="true"/>
			</div>
			<!-- 出散拼、游轮其他产品线可见；或散拼非大洋批发商可见 -->
           	<c:if test="${(activityKind != 2 && activityKind != 10) || (activityKind == 2 && companyUuid ne '7a81a03577a811e5bc1e000c29cf2586')}">
           		<div class="activitylist_bodyer_right_team_co1">
	                <div class="activitylist_team_co3_text">航空公司：</div>
					<div class="selectStyle">
						<form:select id="trafficName" path="trafficName" >
							<form:option value="" >不限</form:option>
							<form:options items="${trafficNames}" itemValue="airlineCode" itemLabel="airlineName"/>
						</form:select>
					</div>
            	</div>
            </c:if>
            <c:if test="${activityKind == 10}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">舱型：</div>
					<div class="selectStyle">
					<select name="spaceType" id="spaceType">
						<option value="">请选择</option>
						<c:forEach items="${cruiseTypeList }" var="cruiseType">
							<option value="${cruiseType.value }" <c:if test="${spaceType == cruiseType.value }">selected="selected"</c:if> >${cruiseType.label }</option>
						</c:forEach> 
					</select>
					</div>
				</div>
            </c:if>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">同行价格：</label><input id="settlementAdultPriceStart" maxlength="8" class="inputTxt" name="settlementAdultPriceStart" value="${settlementAdultPriceStart }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
				<span> 至</span>
				<input id="settlementAdultPriceEnd" class="inputTxt" name="settlementAdultPriceEnd" maxlength="8" value="${settlementAdultPriceEnd }" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')"/>
			</div>

			<!-- 出散拼、游轮其他产品线可见；或散拼非大洋批发商可见 -->
			<c:if test="${(activityKind != 2 && activityKind != 10) || (activityKind == 2 && companyUuid ne '7a81a03577a811e5bc1e000c29cf2586')}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">旅游类型：</div>
					<div class="selectStyle">
						<form:select  path="travelTypeId" itemValue="key" itemLabel="value" >
							<form:option value="">请选择</form:option>
							<form:options items="${travelTypes}" />
						</form:select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">产品系列：</div>
					<div class="selectStyle">
						<form:select id="activityLevelId" path="activityLevelId" itemValue="key" itemLabel="value" >
							<form:option value="">请选择</form:option>
							<form:options items="${productLevels}" />
						</form:select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">产品类型：</div>
					<div class="selectStyle">
						<form:select id="activityTypeId" path="activityTypeId" itemValue="key" itemLabel="value" >
							<form:option value="">请选择</form:option>
							<form:options items="${productTypes}" />
						</form:select>
					</div>
				</div>
			</c:if>
			<c:if test="${activityKind == 2}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">余位：</div>
					<div class="selectStyle">
						<select name="haveYw" id="haveYw">
							<option value="">请选择</option>
							<option value="1" <c:if test="${haveYw=='1'}">selected="selected"</c:if>>有</option>
							<option value="0" <c:if test="${haveYw=='0'}">selected="selected"</c:if>>无</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">切位：</div>
					<div class="selectStyle">
						<select name="haveQw" id="haveQw">
							<option value="">请选择</option>
							<option value="1" <c:if test="${haveQw=='1'}">selected="selected"</c:if>>有</option>
							<option value="0" <c:if test="${haveQw=='0'}">selected="selected"</c:if>>无</option>
						</select>
					</div>
				</div>
			</c:if>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">操作员：</label> 
				<select name="activityCreate" id="activityCreate" >
					<option value="-99999">不限</option>
					<c:forEach var="user" items="${userList}">
						<option value="${user.id }"
							<c:if test="${activityCreate == user.id}">selected="selected"</c:if>>${user.name}
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="kong"></div>
		</div>
	</div>
</form:form>