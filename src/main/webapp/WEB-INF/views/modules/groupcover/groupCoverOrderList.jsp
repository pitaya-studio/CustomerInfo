<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<!-- 上面添加的 -->
<head>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/modules/groupcover/groupCoverPage.js"></script>
<script src="${ctxStatic}/modules/order/single/activityListForOrder.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/groupcover/groupCoverOrderList.js" type="text/javascript"></script>
<style type="text/css">
	.ui-front {z-index: 2100;}
</style>
	<style type="text/css">
		.ui-front {z-index: 2100;}
	</style>
</head>
<body>
	<!-- 顶部菜单列表 -->
	<page:applyDecorator name="order_op_head">
		<page:param name="showType">20</page:param>
		<page:param name="orderStatus">2</page:param>
	</page:applyDecorator>

	<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
		<!-- 搜索查询 -->
		<!-- 查询form -->
		<form:form id="searchForm" modelAttribute="" action="${ctx }/groupCover/coverList" method="post">
			<input id="ctx" type="hidden" value="${ctx }"> 
			<input id="groupCoverOrderListFlag" type="hidden" value="1"> 
			<input id="orderStatus" type="hidden" value="${orderStatus }"> 
			<input id="showType" name="showType" type="hidden" value="${showType }"> 
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo }">
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize }">
			<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy }">
			<input id="isNeedNoticeOrder" type="hidden" value="${isNeedNoticeOrder }">
			<input id="orderOrGroup" type="hidden" value="${orderOrGroup }">
			<input id="quauqBookOrderPermission" name="quauqBookOrderPermission" type="hidden" value="${user.quauqBookOrderPermission }" />
			<!-- 订单查询DIV -->
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input id="coverCodeOrGroupCode" name="coverCodeOrGroupCode"
						   class="inputTxt inputTxtlong searchInput" value="${coverCodeOrGroupCode}"
						   placeholder="请输入团号、补位号"
						   onkeyup="this.value=this.value.replaceColonChars()"
						   onafterpaste="this.value=this.value.replaceColonChars()">
				</div>
				<a class="zksx">筛选</a>
				<div class="form_submit">
					<input id="btn_search" class="btn btn-primary ydbz_x" type="submit" value="搜索"/> 
					<input class="btn ydbz_x" type="button"
						   onclick="resetSearchParams()" value="清空所有条件"/>
				</div>
				<div class="ydxbd">
					<span></span>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">状态：</label>
						<div class="selectStyle">
							<select name="coverStatus" id="coverStatus">
								<option value="0" <c:if test="">selected="selected"</c:if> >不限</option>
								<option value="1" <c:if test="${coverStatus == 1 }">selected="selected"</c:if> >待补位</option>
								<option value="2" <c:if test="${coverStatus == 2 }">selected="selected"</c:if> >已补位</option>
								<option value="3" <c:if test="${coverStatus == 3 }">selected="selected"</c:if> >已驳回</option>
								<option value="4" <c:if test="${coverStatus == 4 }">selected="selected"</c:if> >已取消</option>
								<option value="5" <c:if test="${coverStatus == 5 }">selected="selected"</c:if> >已生成订单</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">申请人：</label>
						<select name="createBy" id="createBy">
							<option value="">不限</option>
							<c:forEach var="user" items="${userList }">
								<option value="${user.id }"
								<c:if test="${createBy == user.id}">selected="selected"</c:if>>${user.name }</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">计调：</label>
						<select	name="activityCreate" id="activityCreate">
							<option value="">不限</option>
							<c:forEach var="user" items="${userList}">
								<option value="${user.id }"
								<c:if test="${activityCreate == user.id}">selected="selected"</c:if>>${user.name }</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">申请提交日期：</label>
						<input id="createDateBegin" class="inputTxt dateinput"
							   name="createDateBegin" value="${createDateBegin }"
							   onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('createDateBegin').value==''){$dp.$('createDateBegin').value=vvv;}}})"
							   readonly />
						<span style="font-size:12px; font-family:'宋体';">至</span>
						<input id="createDateEnd" class="inputTxt dateinput"
							   name="createDateEnd" value="${createDateEnd }" onclick="WdatePicker()" readonly />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">出团日期：</label>
						<input id="groupOpenDate" class="inputTxt dateinput"
							   name="groupOpenDateBegin" value="${groupOpenDateBegin }"
							   onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('groupCloseDate').value==''){$dp.$('groupCloseDate').value=vvv;}}})"
							   readonly />
						<span style="font-size:12px; font-family:'宋体';">至</span>
						<input id="groupCloseDate" class="inputTxt dateinput"
							   name="groupOpenDateEnd" value="${groupOpenDateEnd }" onclick="WdatePicker()" readonly />
					</div>
				</div>
				<div class="kong"></div>
			</div>
		</form:form>

		<!-- 订单排序 -->
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li class="activitylist_paixu_left_biankuang ligc.createDate">
							<a onclick="coversortby('gc.createDate',this)">申请提交时间</a>
						</li>
						<li class="activitylist_paixu_left_biankuang ligc.updateDate">
							<a onclick="coversortby('gc.updateDate',this)">更新时间</a>
						</li>
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong>${page.count }</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>
		
		<!-- 补位列表------------------------------------- -->
		
		<table id="coverTable" class="activitylist_bodyer_table mainTable">
			<!-- 补位列表标题 -->
			<thead style="background:#403738;" id="groupCover_thead">
				<tr>
					<th width="10%">补位号</th>
					<th width="10%">团号</th>
					<th width="8%">计调</th>
					<th width="10%">出/截团日期</th>
					<th width="7%">余位/预收</th>
					<th width="7%">补位人数</th>
					<th width="8%">状态</th>
					<th width="12%">申请提交时间</th>
					<th width="8%">申请人</th>
					<th width="8%">操作</th>
				</tr>
			</thead>
			<!-- 补位查询列表显示 -->
			<tbody id="groupCover_tbody">
				<!-- 无查询结果 -->
					<c:if test="${fn:length(page.list) <= 0 }">
						<tr class="toptr">
							<td colspan="10" style="text-align: center;">暂无搜索结果</td>
						</tr>
					</c:if>

				<!-- 查询结果团期列表循环显示 -->
			<c:forEach items="${page.list}" var="orders" varStatus="s">
				<tr class="toptr">
					<td class="covertc">
						<span style="word-break: break-all;display: block;word-wrap: break-word;">${orders.coverCode }</span>
					</td>
					<td class="covertc">
						<span style="word-break: break-all;display: block;word-wrap: break-word;">${orders.groupCode }</span>
					</td>
					<td class="tc covertc">${orders.createUserName}</td>
					<td class="p0">
						<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate }" pattern="yyyy-MM-dd"/></div>
						<div class="close-date"><fmt:formatDate value="${orders.groupCloseDate }" pattern="yyyy-MM-dd"/></div>
					</td>
					<td class="p0">
						<div class="out-date">${orders.freePosition }</div>
						<div class="close-date">${orders.planPosition }</div>
					</td>
					<td class="tc covertc">${orders.coverPosition }</td>
					<td class="tc covertc">
						<span class="tdred fbold">${orders.coverStatusName}</span>
					</td>
					<td class="tc covertc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orders.createDate}"/></td>
					<td class="tc covertc">${orders.userName}</td>
					<td class="p0 ">
						<dl class="handle">
							<dt>
								<%-- bug17566 ymx 2017/3/13 【UG-V2】订单模块部分页面按钮样式未改变 Start --%>
								<img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png">
								<%--<img title="操作" src="${ctxStatic }/images/handle_cz.png">--%>
								<%-- bug17566 ymx 2017/3/13 【UG-V2】订单模块部分页面按钮样式未改变 End --%>
							</dt>
							<dd class="">
								<p>
									<span></span> 
									<c:choose>
										<c:when test="${orders.coverStatus == 1 }">
											<a href="${ctx}/groupCover/groupCoverInfo/${orders.coverId}" target="_blank">补位详情</a>
											<shiro:hasPermission name="looserOrder:cover:confirm"><a onclick="jbox_buweiconfirm('${orders.coverId}');">确认补位</a></shiro:hasPermission>
											<shiro:hasPermission name="looserOrder:cover:reject"><a onclick="jbox_bohuibuweiconfirm(${orders.coverId });">驳回补位</a></shiro:hasPermission>
											<c:if test="${fns:getUser().id eq orders.createBy}">
												<a href="javascript:void(0)" onclick="jbox_nobuweiconfirm('${orders.coverId}');">取消补位</a>
											</c:if>
										</c:when>
										<c:when test="${orders.coverStatus == 2 }">
											<a href="${ctx}/groupCover/groupCoverInfo/${orders.coverId}" target="_blank">补位详情</a>
											<c:if test="${fns:getUser().id eq orders.createBy}">
												<a href="javascript:bookByCover('${orders.coverId}');">预定</a>
												<a href="javascript:void(0)" onclick="jbox_nobuweiconfirm('${orders.coverId}');">取消补位</a>
											</c:if>
										</c:when>
										<c:when test="${orders.coverStatus == 3 or orders.coverStatus == 4 }">
											<a href="${ctx}/groupCover/groupCoverInfo/${orders.coverId}" target="_blank">补位详情</a>
										</c:when>
										<c:when test="${orders.coverStatus == 5 }">
											<a href="${ctx}/groupCover/groupCoverInfo/${orders.coverId}" target="_blank">补位详情</a>
											<c:if test="${ifCanSeeOrderInfo}">
												<a href="${ctx}/orderCommon/manage/orderDetail/${orders.orderId}" target="_blank">订单详情</a>
											</c:if>
										</c:when>
									</c:choose>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
				</c:forEach>
			</tbody>
		</table>
	<div class="pagination clearFix">
   ${page}
</div>
</div>
<div class="xt-activitylist" style="display:none;">
    <select name="agentSourceType" id="agentSourceTypeTemp" class="typeSelected" onchange="changeAgentSource(this);">
    	<option value="1" selected="selected">非实时渠道</option>
    	<option value="2">实时连通渠道</option>
    </select>
    <select name="quauqAgent" id="quauqAgentTemp" class="typeSelected">
    	<c:forEach var="agentinfo" items="${quauqAgentinfoList }">
            <option value="${agentinfo.id }">${agentinfo.agentName }</option>
        </c:forEach>
    </select>
</div>
</body>
</html>