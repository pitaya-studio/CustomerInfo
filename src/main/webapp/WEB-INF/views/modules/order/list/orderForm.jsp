<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 查询form -->
<form:form id="searchForm" modelAttribute="travelActivity"
	action="${ctx}/orderCommon/manage/showOrderList/${showType}/${orderStatus}.htm"
	method="post">
	<input id="ctx" type="hidden" value="${ctx}" />
	<input id="orderStatus" type="hidden" value="${orderStatus}" />
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}" />
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
	<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
	<input id="showType" name="showType" type="hidden" value="${showType}" />
	<input id="orderOrGroup" name="orderOrGroup" type="hidden" value="${orderOrGroup}" />
	<input id="isNeedNoticeOrder" type="hidden" value="${isNeedNoticeOrder}" />

	<!-- 订单查询DIV -->
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2 pr">
			<%--<div class="activitylist_team_co3_text">搜索：</div>--%>
			<input id="orderNumOrGroupCode" name="orderNumOrGroupCode"
				class="inputTxt inputTxtlong searchInput" value="${orderNumOrGroupCode }"
				flag="istips" onkeyup="this.value=this.value.replaceColonChars()"placeholder="输入团号、订单号"  onafterpaste="this.value=this.value.replaceColonChars()"/>
			<%--<span class="ipt-tips" style="display: block;">输入团号、订单号</span>--%>
		</div>
		<a class="zksx">筛选</a>

		<div class="form_submit">
			<input id="btn_search" class="btn btn-primary ydbz_x" type="button"
				onclick="query(${showType})" value="搜索" /> <input
				class="btn ydbz_x" type="button"
				onclick="resetSearchParams()" value="清空所有条件" />
		</div>


		<div class="ydxbd">
			<span></span>

			<div class="activitylist_bodyer_right_team_co1">  
				<div class="activitylist_team_co3_text">目的地：</div>
				<input type="hidden" value="${travelActivity.targetAreaIds}" id="tempTargetAreaIds">
				<input type="hidden" value="${travelActivity.targetAreaNamess}" id="tempTargetAreaNamess">
				<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIds}" labelName="targetAreaNameList"  labelValue="${targetAreaNames}"
	                     title="区域" url="/activity/manager/filterTreeData1?kind=${orderStatus}" checked="true"/>
            </div>

			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">产品名称：</div> <input value="${travelActivity.acitivityName }"
					name="acitivityName" class="txtPro inputTxt" flag="istips" 
					onkeyup="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()"/>
			</div>

			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">
					<c:if test="${showType!=1000}">订单状态：  </c:if>
				</div>

				<c:if test="${showType!=1000}">
					<div class="selectStyle">
						<select name="orderShowType" id="orderShowType">
							<shiro:hasPermission name="${orderTypeStr}Order:list:allorder">
								<option value="0"
									<c:if test="${showType==0 }">selected="selected"</c:if>>全部订单</option>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:list:ordernopay">
								<option value="1"
									<c:if test="${showType==1 }">selected="selected"</c:if>>未收全款</option>
							</shiro:hasPermission>
							<shiro:hasPermission
								name="${orderTypeStr}Order:list:frontMoneyoccupyNoPay">
								<option value="2"
									<c:if test="${showType==2 }">selected="selected"</c:if>>未收订金</option>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:list:paid">
								<option value="5"
									<c:if test="${showType==5 }">selected="selected"</c:if>>已收全款</option>
							</shiro:hasPermission>
							<shiro:hasPermission
								name="${orderTypeStr}Order:list:frontMoneyPay">
								<option value="4"
									<c:if test="${showType==4 }">selected="selected"</c:if>>已收订金</option>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:list:occupyNoPay">
								<option value="3"
									<c:if test="${showType==3 }">selected="selected"</c:if>>已占位</option>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:list:op">
								<option value="7"
									<c:if test="${showType==7 }">selected="selected"</c:if>>待计调确认</option>
							</shiro:hasPermission>
							<c:if test="${fns:getUser().company.orderPayMode eq 1}">
								<shiro:hasPermission name="${orderTypeStr}Order:list:cw">
									<option value="8"
										<c:if test="${showType==8 }">selected="selected"</c:if>>待财务确认</option>
								</shiro:hasPermission>
								<shiro:hasPermission name="${orderTypeStr}Order:list:cw_cx">
									<option value="9"
										<c:if test="${showType==9 }">selected="selected"</c:if>>已撤销占位</option>
								</shiro:hasPermission>
							</c:if>
							<!-- 20151102 C322 针对大洋需求，屏蔽已取消、已删除搜索条件 -->
							<%-- <c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' || orderStatus ne 0}"> --%>
							<%--<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' }">--%>
							<%--<c:if test="${office.isShowCancelOrder == 0}">
								<shiro:hasPermission name="${orderTypeStr}Order:list:cancelorder">--%>
									<option value="99"
										<c:if test="${showType==99 }">selected="selected"</c:if>>已取消</option>
								<%--</shiro:hasPermission>
							</c:if>--%>
							<%--<c:if test="${office.isShowDeleteOrder == 0}">
								<shiro:hasPermission name="${orderTypeStr}Order:list:deleteByFlag">--%>
									<option value="111"
										<c:if test="${showType==111 }">selected="selected"</c:if>>已删除</option>
								<%--</shiro:hasPermission>
							</c:if>--%>
							<%--</c:if>--%>
						</select>
					</div>
				</c:if>
			</div>

			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">出团日期：</label> <input
					id="groupOpenDate" class="inputTxt dateinput" name="groupOpenDateBegin"
					value='${groupOpenDateBegin }'
					onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
					readonly /> <span style="font-size:12px; font-family:'宋体';">
					至</span> <input id="groupCloseDate" class="inputTxt dateinput"
					name="groupOpenDateEnd"
					value='${groupOpenDateEnd }'
					onClick="WdatePicker()" readonly />
			</div>
			<%--<div class="kong"></div>--%>

			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">销售：</label> 
					<select name="salerId" id="salerId" >
						<option value="">不限</option>
						<c:forEach var="user" items="${userList}">
							<option value="${user.id }"
								<c:if test="${salerId == user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
			</div>

			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">计调：</label> 
					<select name="activityCreate" id="activityCreate" >
						<option value="">不限</option>
						<c:forEach var="user" items="${userList}">
							<option value="${user.id }"
								<c:if test="${activityCreate == user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
			</div>

			<c:set var="userinfo" value="${fns:getUser()}"/>
			<c:if test="${userinfo.userType ==3}">
				<div class="activitylist_bodyer_right_team_co3">
					<label class="activitylist_team_co3_text">渠道选择： </label> 
					<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
					<select id="modifyAgentInfo" name="agentId">
						<option value="">全部</option>
						<c:choose>
						<c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
							<option value="-1" <c:if test="${agentId == -1}">selected="selected"</c:if>> 直客</option>
						</c:when>
						<c:otherwise>
							<option value="-1" <c:if test="${agentId == -1}">selected="selected"</c:if>>非签约渠道</option>
						</c:otherwise>
						</c:choose>
						
						<c:forEach var="agentinfo" items="${agentinfoList }">
							<option value="${agentinfo.id }"
								<c:if test="${agentId==agentinfo.id}">selected="selected"</c:if>>${agentinfo.agentName}</option>
						</c:forEach>
					</select>
				</div>
			</c:if>
			<c:if test="${userinfo.userType ==1}">
				<input type="hidden" name="agentId"
					value="${fns:getUser().company.id}" />
			</c:if>
			<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">下单日期：</label> <input
					id="orderTimeBegin" class="inputTxt dateinput"
					name="orderTimeBegin" value='${orderTimeBegin}'
					onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('orderTimeBegin').value==''){$dp.$('orderTimeBegin').value=vvv;}}})"
					readonly /> <span style="font-size:12px; font-family:'宋体';">
					至</span> <input id="orderTimeEnd" class="inputTxt dateinput"
					name="orderTimeEnd" value='${orderTimeEnd}' onClick="WdatePicker()"
					readonly />
			</div>
			<%--<div class="kong"></div>--%>
			
			<!-- 20151102 C322 针对大洋需求，屏蔽以下搜索条件 -->
			<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text" style="line-height:28px;">联系人：</div>
					<input id="orderPersonName" class="inputTxt" name="orderPersonName"
						value="${orderPersonName}" onkeyup="this.value=this.value.replaceColonChars()"  onafterpaste="this.value=this.value.replaceColonChars()">
				</div>
				
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">下单人：</label> 
					<select name="proCreateBy" id="proCreateBy" >
						<option value="">不限</option>
						<c:forEach var="user" items="${userList}">
							<option value="${user.id }"
								<c:if test="${proCreateBy == user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
				</div>
				
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">发票状态：</label>
					<div class="selectStyle">
						<select	name="invoiceStatus">
							<option value="">不限</option>
							<option value="1" <c:if test="${invoiceStatus==1}">selected="selected"</c:if>>未开发票</option>
							<option value="2" <c:if test="${invoiceStatus==2}">selected="selected"</c:if>>已开发票</option>
						</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">收据状态：</label>
					<div class="selectStyle">
						<select name="receiptStatus">
							<option value="">不限</option>
							<option value="1" <c:if test="${receiptStatus==1}">selected="selected"</c:if>>未开收据</option>
							<option value="2" <c:if test="${receiptStatus==2}">selected="selected"</c:if>>已开收据</option>
						</select>
					</div>
				</div>
				<%--<div class="kong"></div>--%>
				
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">借款状态：</label>
					<div class="selectStyle">
						<select name="jiekuanStatus">
							<option value="">不限</option>
							<option value="1" <c:if test="${jiekuanStatus==1}">selected="selected"</c:if>>审批中</option>
							<option value="2" <c:if test="${jiekuanStatus==2}">selected="selected"</c:if>>已借</option>
							<option value="3" <c:if test="${jiekuanStatus==3}">selected="selected"</c:if>>未借</option>
						</select>
					</div>
				</div>
			</c:if>
			
			<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">是否确认占位：</label>
                    <div class="selectStyle">
                        <select name="confirmOccupy">
                            <option value="">不限</option>
                            <option value="1" <c:if test="${confirmOccupy eq 1}">selected="selected"</c:if>>已确认</option>
                            <option value="0" <c:if test="${not empty confirmOccupy and confirmOccupy ne 1}">selected="selected"</c:if>>未确认</option>
                        </select>
                    </div>
				</div>
			</shiro:hasPermission>
		</div>
		<div class="kong"></div>
	</div>
</form:form>

<!-- 下载 -->
<form id="exportForm" action="${ctx}/orderCommon/manage/downloadData" method="post">
	<input type="hidden" id="orderId" name="orderId">
	<input type="hidden" name="orderType" value="${orderStatus}">
	<input type="hidden" id="downloadType" name="downloadType">
	<input type="hidden" value="${orderNum}" id="orderNum" name="orderNum">
</form>
<!-- 导出 游客资料-->
<form id="exportTravelesForm" action="${ctx}/activity/manager/exportExcel" method="post">
	<input type="hidden" id="groupId" name="groupId">
	<input type="hidden" name="orderType" value="${orderStatus}">
	<input type="hidden" id="groupCode" name="groupCode">
</form>
<!-- 导出 团空单按钮-->
<form id="exportTravelesForm1" action="${ctx}/activity/manager/exportActivityGroupExcel" method="post">
	<input type="hidden" id="groupId1" name="groupId">
	<input type="hidden" name="orderType" value="${orderStatus}">
	<input type="hidden" id="groupCode1" name="groupCode">
	<input type="hidden" id="orderIds1" name="orderIds">
</form>

<!--351&363 添加延时弹窗 START-->
<div id="jbox-delay-o" style="display: none;">
	<div id="jbox-delay" style="margin:20px">
	<label><span style="color: red;">*</span>延长时限：</label>
		<input class="spinner_day" name="spinner" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">天</span>
		<input class="spinner_hour" name="spinner" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">时</span>
		<input class="spinner_min" name="spinner" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">分</span>
	</div>
</div>
<!--351&363 添加延时弹窗 END-->


<!--351&363 添加激活弹窗 START-->
<div id="jbox-active-o" style="display: none;">
	<div id="jbox-active" style="margin:20px">
		<label><span style="color: red;">*</span>激活时限：</label>
		<input class="spinner_day" name="spinner_activityDay" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">天</span>
		<input class="spinner_hour" name="spinner_activityHour" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">时</span>
		<input class="spinner_min" name="spinner_activityMin" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">分</span>
	</div>
</div>
<!--351&363 添加激活弹窗 END-->

<div id="jbox-confirm-o" style="display: none;">
	<div id="jbox-confirm" style="margin:20px">
		<label><span style="color: red;">*</span>保留时限：</label>
		<input class="spinner_day" name="spinner_activityDay" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">天</span>
		<input class="spinner_hour" name="spinner_activityHour" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">时</span>
		<input class="spinner_min" name="spinner_activityMin" value="" onafterpaste="this.value=this.value.replace(/\D/g,'')" onkeyup="this.value=this.value.replace(/\D/g,'')" autocomplete="off"/><span style="padding-left:5px;">分</span>
	</div>
</div>
