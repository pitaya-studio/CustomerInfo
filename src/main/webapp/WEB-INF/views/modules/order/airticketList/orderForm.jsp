<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 查询form -->
<form id="searchForm" action="${ctx}/airticketOrderList/manage/airticketOrderList/${queryType}" method="post" class="formpaixu">
	<input id="ctx" type="hidden" value="${ctx}" />
	<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}" />
	<input id="orderOrGroup" name="orderOrGroup" type="hidden" value="${orderOrGroup}" />
	<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
	<input id="queryType" name="queryType" type="hidden" value="${queryType}" />
	<input id="isNeedNoticeOrder" type="hidden" value="${isNeedNoticeOrder}" />
	<c:set var="companyUuid" value="${fns:getUser().company.uuid}"></c:set>
	<div class="activitylist_bodyer_right_team_co">
		<div class="activitylist_bodyer_right_team_co2 pr">
			<%--<div class="activitylist_team_co3_text">搜索：</div>--%>
			<input value="${orderNumOrOrderGroupCode }" placeholder="输入团号、订单号、产品编号" class="inputTxt inputTxtlong" name="orderNumOrOrderGroupCode" id="orderNumOrOrderGroupCode" flag="istips">
			<%--<span class="ipt-tips">输入团号、订单号、产品编号</span>--%>
		</div>
		<a class="zksx">筛选</a>
		<div class="form_submit">
			<input type="submit" id="btn_search" value="搜索"  class="btn btn-primary ydbz_x" onclick="submitSearch();">
			<input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
		</div>
		<div class="ydxbd">
			<span></span>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">机票类型：</div>
                <div class="selectStyle">
                    <select name="airType">
                        <option value="" selected="selected">不限</option>
                        <option value="3" <c:if test="${airType==3 }"> selected="selected"</c:if> >单程</option>
                        <option value="2" <c:if test="${airType==2 }"> selected="selected"</c:if>>往返</option>
                        <option value="1" <c:if test="${airType==1 }"> selected="selected"</c:if>>多段</option>
                    </select>
                </div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">航班类型：</div>
                <div class="selectStyle">
                    <select name="ticketType">
                        <option value="" selected="selected">不限</option>
                        <option value="4" <c:if test="${ticketType==4 }"> selected="selected"</c:if> >国内</option>
                        <option value="2" <c:if test="${ticketType==2 }"> selected="selected"</c:if> >国际</option>
                        <option value="1" <c:if test="${ticketType==1 }"> selected="selected"</c:if> >内陆</option>
                        <option value="3" <c:if test="${ticketType==3 }"> selected="selected"</c:if> >国际+内陆</option>
                    </select>
                </div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">出发城市：</div>
                <div class="selectStyle">
					<select name="fromAreaId" >
						<option value="">不限</option>
							<c:forEach items="${fromAreasMap}" var="entry">
								<option value="${entry.key}"  <c:if test="${fromAreaId==entry.key }">selected="selected"</c:if> >${entry.value}</option>
							</c:forEach>
					</select>
                </div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">出发日期：</div>
				<input class="inputTxt dateinput" name="startAirTime" value="${startAirTime}" onclick="WdatePicker()" readonly />至
				<input class="inputTxt dateinput" name="endAirTime" value="${endAirTime}" onclick="WdatePicker()" readonly />
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">参团类型：</div>
                <div class="selectStyle">
                    <select name="joinGroup">
                        <option value="">不限</option>
                        <option value="0" <c:if test="${joinGroup=='0' }"> selected="selected"</c:if> >单办</option>
                        <option value="1" <c:if test="${joinGroup=='1' }"> selected="selected"</c:if> >单团</option>
                        <option value="2" <c:if test="${joinGroup=='2' }"> selected="selected"</c:if> >散拼</option>
                        <option value="3" <c:if test="${joinGroup=='3' }"> selected="selected"</c:if> >游学</option>
                        <option value="4" <c:if test="${joinGroup=='4' }"> selected="selected"</c:if> >大客户</option>
                        <option value="5" <c:if test="${joinGroup=='5' }"> selected="selected"</c:if> >自由行</option>
                    </select>
                </div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">订单状态：</div>
                <div class="selectStyle">
					<select id="showType" name="showType">      
						<option value="0"  <c:if test="${showType == 0 }">selected="selected"</c:if> >全部订单</option>
						<option value="1"  <c:if test="${showType == 1 }">selected="selected"</c:if> >未收全款</option>
						<option value="2"  <c:if test="${showType == 2 }">selected="selected"</c:if> >未收订金</option>
						<option value="5"  <c:if test="${showType == 5 }">selected="selected"</c:if> >已收全款</option>
						<option value="4"  <c:if test="${showType == 4 }">selected="selected"</c:if> >已收订金</option>
						<option value="3"  <c:if test="${showType == 3 }">selected="selected"</c:if> >已占位</option>
						<option value="7"  <c:if test="${showType == 7 }">selected="selected"</c:if> >待计调确认</option>
						<c:if test="${fns:getUser().company.orderPayMode eq 1}">
							<option value="8"  <c:if test="${showType == 8 }">selected="selected"</c:if> >待财务确认</option>
							<option value="9"  <c:if test="${showType == 9 }">selected="selected"</c:if> >已撤销占位</option>
						</c:if>
						<!-- 20151102 C322 针对大洋需求，屏蔽已取消、已删除搜索条件 -->
						<%-- <c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' || showType ne 0}"> --%>
						<%--<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' }">--%>
						<%--<c:if test="${office.isShowCancelOrder == 0}">--%>
							<option value="99"  <c:if test="${showType == 99 }">selected="selected"</c:if> >已取消</option>
						<%--</c:if>
						<c:if test="${office.isShowDeleteOrder == 0}">--%>
							<option value="111"  <c:if test="${showType == 111 }">selected="selected"</c:if> >已删除</option>
						<%--</c:if>--%>
						<%--</c:if>--%>
					</select>
                </div>
			</div>
			<div class="activitylist_bodyer_right_team_co1">  
				<div class="activitylist_team_co3_text">到达城市：</div>
				<tags:treeselect id="targetArea" name="targetAreaIdList" value="${targetAreaIdList }"  labelName="targetAreaNameList" labelValue="${targetAreaNameList }" title="区域" url="/sys/area/treeData" cssClass="required targetArea_no_input" checked="true"/>
			</div>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">返回日期：</div>
				<input class="inputTxt dateinput" name="returnStartAirTime" value="${returnStartAirTime}" onclick="WdatePicker()" readonly />至
				<input class="inputTxt dateinput" name="returnEndAirTime" value="${returnEndAirTime}" onclick="WdatePicker()" readonly />
			</div>
			<div class="activitylist_bodyer_right_team_co3">
				<div class="activitylist_team_co3_text">渠道选择：</div>
				<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
				<select id="agentId" name="agentId">
					<option value="">全部</option>
					<c:if test="${not empty agentinfoList }">
						<c:forEach items="${agentinfoList}" var="agentinfo">
							<c:choose>
								<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and agentinfo.agentName eq '非签约渠道' }">
									<option value="${agentinfo.id }"  <c:if test="${agentinfo.id == agentId}">selected="selected"</c:if> >未签</option>
								</c:when>
								<c:when test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' and agentinfo.agentName eq '非签约渠道' }">
									<option value="${agentinfo.id }"  <c:if test="${agentinfo.id == agentId}">selected="selected"</c:if> >直客</option>
								</c:when>
								<c:otherwise><option value="${agentinfo.id }"  <c:if test="${agentinfo.id == agentId}">selected="selected"</c:if> >${agentinfo.agentName }</option></c:otherwise>
							</c:choose>
						</c:forEach>
					</c:if>
				</select>
			</div>
			<!-- C322 大洋屏蔽 2015-11-02 yakun.bai -->
			<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">联系人：</div>
					<input type="text" class="" name="contact" value="${contact }">
				</div>
			</c:if>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">计调：</div>
				<input type="text" class="" name="op" value="${op }">
			</div>
			<!-- C322 大洋屏蔽 2015-11-02 yakun.bai -->
			<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">下单人：</div>
					<span style="position:absolute">
						<select id="picker" name="picker" >
							<option value="" >不限</option>
							<c:forEach items="${users}" var="userinfo">
								<option value="${userinfo.id }" <c:if test="${userinfo.id eq picker}">selected="selected"</c:if>>${userinfo.name }</option>
							</c:forEach>
						</select>
					</span>
				</div>
			</c:if>
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">销售：</div>
				<span style="position:absolute">
					<select id="saler" name="saler"  >
						<option value="" >不限</option>
						<c:forEach items="${users}" var="userinfo">
							<option value="${userinfo.id }" <c:if test="${userinfo.id eq saler}">selected="selected"</c:if>>${userinfo.name }</option>
						</c:forEach>
					</select>
				</span>
			</div>
			<!-- C322 大洋屏蔽 2015-11-02 yakun.bai -->
			<c:if test="${queryType eq 1 && companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">发票状态：</div>
                    <div class="selectStyle">
                        <select id="" name="invoiceStatus">
                            <option value="" >不限</option>
                            <option value="1" <c:if test="${invoiceStatus==1}">selected="selected"</c:if>>未开发票</option>
                            <option value="2" <c:if test="${invoiceStatus==2}">selected="selected"</c:if>>已开发票</option>
                        </select>
                    </div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">收据状态：</div>
                    <div class="selectStyle">
                        <select id="" name="receiptStatus">
                            <option value="" >不限</option>
                            <option value="1" <c:if test="${receiptStatus==1}">selected="selected"</c:if>>未开收据</option>
                            <option value="2" <c:if test="${receiptStatus==2}">selected="selected"</c:if>>已开收据</option>
                        </select>
                    </div>
				</div>
			</c:if>	
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
			<%-- <div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
				<select name="paymentType" id="paymentType" >
					<option value="">不限</option>
					<c:forEach items="${fns:findAllPaymentType()}" var="pType">
						<!-- 用户类型  1 代表销售 -->
						<option value="${pType[0]}" <c:if test="${pType[0] eq paymentType}">selected="selected"</c:if>>${pType[1] }</option>
					</c:forEach> 
				</select>
			</div> --%>
			
			<div class="activitylist_bodyer_right_team_co1">
				<div class="activitylist_team_co3_text">搜索参团：</div>
				<input value="${joinGroupCodeOrOrderNum }" class="inputTxt inputTxtlong" name="joinGroupCodeOrOrderNum" id="joinGroupCodeOrOrderNum" flag="istips"> 
				<span class="ipt-tips" <c:if test="${not empty joinGroupCodeOrOrderNum}">style="display: none;"</c:if> >团号、订单号</span>
			</div>
			<%-- 575 wangyang 2017.1.4 --%>
			<c:if test="${queryType eq 1 }"><c:set var="type" value="Sale"></c:set></c:if>
			<c:if test="${queryType eq 2 }"><c:set var="type" value="Op"></c:set></c:if>
			<shiro:hasPermission name="airticketOrderFor${type }:operation:customerConfirm">
				<div class="activitylist_bodyer_right_team_co1">
					<label class="activitylist_team_co3_text">是否确认占位：</label>
					<div class="selectStyle"><%-- modified by tlw for 统一select样式--%>
						<select name="confirmOccupy">
							<option value="">不限</option>
							<option value="1" <c:if test="${confirmOccupy eq 1}">selected="selected"</c:if>>已确认</option>
							<option value="0" <c:if test="${not empty confirmOccupy and confirmOccupy ne 1}">selected="selected"</c:if>>未确认</option>
						</select>
					</div>
				</div>
			</shiro:hasPermission>
		</div>
	</div>
</form>
<!-- 导出 -->
<form id="exportTravelesForm" action="${ctx}/order/manage/exportExcel" method="post">
	<input type="hidden" id="activityId" name="activityId">
</form>