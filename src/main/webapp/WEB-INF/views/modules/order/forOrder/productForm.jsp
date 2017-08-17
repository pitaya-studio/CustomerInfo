<%@ page contentType="text/html;charset=UTF-8" %>
<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table mainTable activitylist_bodyer_table" name="colspanTable">
		<thead style="background:#403738">
			<tr name="colspanTr">
				<th width="5%">序号</th>
				<th width="23%">产品名称</th>
				<th width="10%">出发地</th>
				<th width="5%">航空</th>
				<th width="7%">同行价</th>
				<th width="15%">最近出团日期</th>
			</tr>
		</thead>

		<c:if test="${fn:length(page.list) <= 0 }">
			<tr class="toptr" >
				<td name="colspanCount" style="text-align: center;">
					暂无搜索结果
				</td>
			</tr>
		</c:if>
		<c:forEach items="${page.list}" var="activity" varStatus="s">
			<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
			<c:set var="freePositions" value="0"></c:set>
			<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
				<c:if test="${activity.groupOpenDate eq group.groupOpenDate}">
					<c:set var="freePositions" value="${freePositions + group.freePosition }"></c:set>
				</c:if>
			</c:forEach>
			<tr id="parent${s.count}">
				<td>${s.count}<br/><br/></td>
				<td class="activity_name_td" >
					<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}?isOp=0')">${activity.acitivityName}</a>
					<c:if test="${not empty activity.activityAirTicket}">
						<c:if test="${not empty activity.activityAirTicket.intermodalStrategies[0]}">
	                   		<c:choose>
	                    		<c:when test="${activity.activityAirTicket.intermodalStrategies[0].type == 1}">
		                            <span class="lianyun_name">全国联运</span>
		                        </c:when>
		                        <c:when test="${activity.activityAirTicket.intermodalStrategies[0].type == 2}">
		                            <span class="lianyun_name">分区联运</span>
		                        </c:when>
		                        <c:otherwise>
		                            <span class="lianyun_name">无联运</span>
		                        </c:otherwise>
		                    </c:choose>
                    	</c:if>   
                	</c:if>
				</td>
				<td>${activity.fromAreaName}</td>
				<td>
					<label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label>
				</td>
				<td class="tr"><c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if><c:if test="${activity.settlementAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<span class="tdred fbold"><fmt:formatNumber value="${activity.settlementAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起</c:if></td>
				<td>
					<c:if test="${groupsize ne 0 }">
						<c:choose>
							<c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
							<c:otherwise>${activity.groupOpenDate}</c:otherwise>
						</c:choose>
						(余位：${freePositions})
						<br/>
						<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${showType})" onMouseenter="if($(this).html()=='团期预定'){$(this).html('展开团期预定')}" onMouseleave="if($(this).html()=='展开团期预定'){$(this).html('团期预定')}">团期预定</a>
					</c:if>
					<c:if test="${groupsize == 0 }">
						日期待定
					</c:if> 
				</td>
			</tr>
			<tr id="child${s.count}" style="display:none" class="activity_team_top1">
				<td colspan="6" class="team_top" style="background-color:#d1e5f5;">
					<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;" name="colspanTable">
						<thead> 
							<tr name="colspanTr"> 
								<th class="tc" width="8%" rowspan="2">团号</th>
								<th class="tc" width="8%" rowspan="2">询价客户</th> 
								<th class="tc" width="8%" rowspan="2">操作员</th> 
								<th class="tc" width="10%" rowspan="2">出/截团日期</th>
								<!-- 284 单团报名 产品列表 针对越柬行踪屏蔽 -->
								<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
									<th class="tc" width="7%" rowspan="2">签证国家</th>
									<th class="tc" width="9%" rowspan="2">资料截止日期</th>
								</c:if>

								<shiro:hasPermission name="price:project">
									<th class="tc" width="12%" rowspan="2" >酒店房型</th>
								</shiro:hasPermission>
								<th width="21%" colspan="3" class="t-th2">同行价</th>
								<th class="tr" width="6%" rowspan="2">订金/人</th>
								<th class="tr" width="7%" rowspan="2">单房差/间夜</th>
								<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
									<th rowspan="2" width="4%">预收</th>
									<th rowspan="2" width="4%">已确认人数</th>
									<th rowspan="2" width="4%">已占位人数</th>
								</shiro:hasPermission>
								<th style="display:none" width="4%">余位</th>
								<th class="tc" width="10%" rowspan="2">操作</th>
							</tr> 
							<tr> 
								<th class="tr" width="7%">成人</th>
								<th class="tr" width="7%">儿童</th>
								<th class="tr p0 pr" width="7%"><span class="ico-remarks-td" title="${activity.specialRemark}"></span>特殊人群</th>
							</tr> 
						</thead>
							<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
								<tbody>
								<tr>
									<td class="word-break-all">
										<input type="hidden" name="groupID" value="${group.id }"/>
										<span style="word-break:break-all; display:block; word-wrap:break-word;">${group.groupCode}</span>
									</td>
									<td class="tc">
										<c:if test="${not empty activity.estimatePriceRecord}">
											${activity.estimatePriceRecord.baseInfo.customerName}
										</c:if>
									</td>
									<td class="tc" title="电话：${group.createBy.mobile}">
										${group.createBy.name}
									</td>
									<td class="p0" width="8%">
										<div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></div>
										<div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></div>
									</td>

									<!-- 284 单团报名 产品列表 针对越柬行踪屏蔽 -->
									<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
										<td class="tc">
											${group.visaCountry}
										</td>
										<td width="8%" class="tc" >
											<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
										</td>
									</c:if>

									<!-- 299v2 酒店房型 -->
									<shiro:hasPermission name="price:project">
										<td class="tc hotelAndHouse">
											<input type="hidden" name="groupHotel" value="${group.groupHotel}" />
											<input type="hidden" name="groupHouseType" value="${group.groupHouseType}" />
										</td>
									</shiro:hasPermission>
									<td class="tr">
										<c:if test="${not empty group.settlementAdultPrice}">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</c:if>
										<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></span>
									</td>
									<td class="tr">
									    <c:if test="${not empty group.settlementcChildPrice}">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></span>
									</td>
									<td class="tr">
									    <c:if test="${not empty group.settlementSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></span>
									</td>
									<td class="tr">
									    <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></span>
									</td>
									<td class="tr">
									    <c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if><span  class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></span>
									</td>
									<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
										<td class="tr">
												${group.planPosition}
										</td>
										<td class="tr">
												${fns:getConfirmedNums(group.id)}
										</td>
										<td class="tr">
												${group.nopayReservePosition}
										</td>
									</shiro:hasPermission>
									<td style="display:none;" class="soldPayPosition${group.id}" width="4%">
										<span>${group.freePosition}</span>
									</td>
									<td class="tc">
										<input type="hidden" name="companyUuid" id="companyUuid" value="${fns:getUser().company.uuid}">
										<%@ include file="/WEB-INF/views/modules/order/forOrder/payModeTypeCommon.jsp"%>
									</td>
								</tr>
								<!--299-单团产品列表-start-->
								<tr class="pricePlanContainer" style="display:none;">
									<td name="colspanCount">
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
												<c:if test="${activityKind == '2'}">
													<th colspan="3" class="tc t-th2">直客价</th>
												</c:if>
												<th rowspan="2" class="tc">备注</th>
											</tr>
											<tr>
												<th class="tc">成人</th>
												<th class="tc">儿童</th>
												<th class="tc">特殊人群</th>
												<c:if test="${activityKind == '2'}">
													<th class="tc">成人</th>
													<th class="tc">儿童</th>
													<th class="tc">特殊人群</th>
												</c:if>
											</tr>
											</thead>
											<tbody>
											</tbody>
										</table>
									</td>
								</tr>
								<!--299-单团产品列表-end-->
								<%@ include file="/WEB-INF/views/modules/order/forOrder/salerDetail.jsp"%>
								
								
								<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
								
								<!-- 285 越柬行踪 单团报名 产品列表默认展开备注 -->
								<!--<tr class="groupNoteCol" <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">style="display: none;"</c:if> >
									<td name="colspanCount">备注:
										<span class="groupNoteContent">${group.groupRemark}</span>
									</td>
								</tr>-->
								<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
								<tr class="groupNoteCol" <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">style="display: none;"</c:if> >
									<td name="colspanCount">备注:
										<span class="groupNoteContent">${group.groupRemark}</span>
									</td>
								</tr>
								
								<!-- 576需求-报名团期备注默认展开***end*** -->
								
								
								</tbody>
							</c:forEach>
						</table>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:set var="userinfo" value="${fns:getUser()}"/>
	<input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
	<!-- 获取批发商是否允许补单的值：0为否，1为是 -->
	<c:set var="isAllowSupplement" value="${userinfo.company.isAllowSupplement}"></c:set>
	<input type="hidden" id="isAllowSupplement" value="${isAllowSupplement}"/>
</form>