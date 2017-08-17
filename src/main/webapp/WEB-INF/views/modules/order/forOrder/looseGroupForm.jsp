<%@ page contentType="text/html;charset=UTF-8" %>
<form id="groupform" name="groupform" action="" method="post" style="width:100%;margin-bottom:20px;" >
	<table id="contentTable" class="table mainTable activitylist_bodyer_table" style="table-layout:fixed;" name="colspanTable">
		<thead>
			<tr width="4%" name="colspanTr">
				<th width="50px">序号</th>
				<th  width="90px">
					<span class="tuanhao on">团号</span>
					/
					<span class="chanpin">产品名称</span>
				</th>
				<th width="70px">操作员</th>
				<th width="80px">出/截团日期</th>
				<!-- 284 散拼报名 团期列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<%--<th width="80px">资料截止日期</th>--%>
				</c:if>
				<th width="70px">出发地</th>
				<!-- 284 散拼报名 团期列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<th width="70px">签证国家</th>
				</c:if>
				<shiro:hasPermission name="price:project">
					<%--<th width="150px">酒店房型</th>--%>
				</shiro:hasPermission>
				<th width="160px">同行价</th>
				<!--远程度假暂时隐藏直客价C381 --> <!-- 284 散拼报名 团期列表 针对越柬行踪屏蔽直客价 -->
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<th width="160px">直客价</th>
				</c:if>
				<%-- <c:if test="${user.quauqBookOrderPermission eq '1' && officeShelfRightsStatus ne '0'}">
					<th class="tc" width="360px" colspan="3"">供应价</th>
				</c:if> --%>
				<th width="70px">订金</th>
				<%--<th width="70px">单房差</th>--%>
				<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
					<th width="50px">
						<div>预收</div>
						<div>余位</div>
					</th>
					<th width="50px">
						<div>已确认人数</div>
						<div>已占位人数</div>
					</th>
				</shiro:hasPermission>
				<th width="60px">
					<div>已切位</div>
					<div>售出切位</div>
				</th>
				<c:if test="${isSeizedConfirmation ne true}">
					<th rowspan="2" width="50px">余位</th>
				</c:if>
				<th rowspan="2" width="65px">操作</th>
			</tr>
		</thead>
			<c:if test="${fn:length(page.list) <= 0 }">
				<tbody>
				<tr class="toptr" >
					<td name="colspanCount" style="text-align: center;">
						暂无搜索结果
					</td>
				</tr>
				</tbody>
			</c:if>
			<c:forEach items="${page.list}" var="group" varStatus="s">
				<tbody>
				<c:set var="activity" value="${group.travelActivity}"></c:set>
				<tr>
					<td>
						<div class="table_borderLeftN">
							<input type="checkbox" name="groupid" value="${group.id}" <c:if test="${fn:contains(groupIds,fn:trim(group.id))}">checked="checked"</c:if> onchange="idcheckchg(this)" />
							<input type="hidden" name="quauqAdultPrice" id="quauqAdultPrice" value="${group.quauqAdultPrice }">
							<input type="hidden" name="quauqChildPrice" id="quauqChildPrice" value="${group.quauqChildPrice }">
							<input type="hidden" name="quauqSpecialPrice" id="quauqSpecialPrice" value="${group.quauqSpecialPrice }">
							${s.count}<br/><br/>
						</div>
					</td>
					<td class="word-break-all">
						<div class="tuanhao_cen onshow">
							<input type="hidden" name="groupID" value="${group.id }"/>
							<span style="word-break:break-all; display:block; word-wrap:break-word;">${group.groupCode}</span>
						</div>
						<div class="chanpin_cen qtip" title="${activity.acitivityName}">
							<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail4Groups/${group.srcActivityId}/${group.id}?isOp=0')">${activity.acitivityName}</a>
						</div>
					</td>
					<td class="tc" title="电话：${group.createBy.mobile}">
						${group.createBy.name}
					</td>
					<td class="p0" width="8%">
						<div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></div>
						<div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></div>
					</td>


					<!-- 284 散拼报名 团期列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<%--<td width="8%" class="tc">--%>
							<%--<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate}"/></span>--%>
						<%--</td>--%>
					</c:if>

					<td class="tc">${activity.fromAreaName}</td>

					<!-- 284 散拼报名 团期列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td class="tc">
							${group.visaCountry}
						</td>
					</c:if>
					<!-- 299v2 酒店房型 -->
					<shiro:hasPermission name="price:project">
						<%--<td class="tc hotelAndHouse">--%>
							<%--<input type="hidden" name="groupHotel" value="${group.groupHotel}" />--%>
							<%--<input type="hidden" name="groupHouseType" value="${group.groupHouseType}" />--%>
						<%--</td>--%>
					</shiro:hasPermission>
					<td class="tc">
						<div class="price-list">
							<span class="price-title">成人：</span>
							<span class="price-content word-break-all">
                                <c:if test="${empty group.settlementAdultPrice}">—</c:if>
                                <c:if test="${not empty group.settlementAdultPrice}">
									${fns:getCurrencyInfo(group.currencyType,0,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></span>
								</c:if>
                            </span>
						</div>
						<div class="price-list">
							<span class="price-title">儿童：</span>
							<span class="price-content word-break-all">
                                <c:if test="${empty group.settlementcChildPrice}">—</c:if>
                                <c:if test="${not empty group.settlementcChildPrice}">
									${fns:getCurrencyInfo(group.currencyType,1,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></span>
								</c:if>
                            </span>
						</div>
						<div class="price-list">
							<span class="price-title">特殊人群：</span>
							<span class="price-content word-break-all">
                                <c:if test="${empty group.settlementSpecialPrice}">—</c:if>
					    <c:if test="${not empty group.settlementSpecialPrice}">
							${fns:getCurrencyInfo(group.currencyType,2,'mark')}
							<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></span>
						</c:if>
                            </span>
						</div>

					</td>
					<!-- C323大洋屏蔽 --><!--远程度假暂时隐藏直客价C381 -->  <!-- 284 散拼报名 团期列表 针对越柬行踪屏蔽直客价 -->
					<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td class="tc">
							<div class="price-list">
								<span class="price-title">成人：</span>
								<span class="price-content word-break-all">
                                        <c:if test="${empty group.suggestAdultPrice}">—</c:if>
                                        <c:if test="${not empty group.suggestAdultPrice}">
											${fns:getCurrencyInfo(group.currencyType,3,'mark')}
											<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></span>
										</c:if>
                                    </span>
							</div>
							<div class="price-list">
								<span class="price-title">儿童：</span>
								<span class="price-content word-break-all">
                                        <c:if test="${empty group.suggestChildPrice}">—</c:if>
                                        <c:if test="${not empty group.suggestChildPrice}">
											${fns:getCurrencyInfo(group.currencyType,4,'mark')}
											<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></span>
										</c:if>
                                    </span>
							</div>
							<div class="price-list">
								<span class="price-title">特殊人群：</span>
								<span class="price-content word-break-all">
                                        <c:if test="${empty group.suggestSpecialPrice}">—</c:if>
                                        <c:if test="${not empty group.suggestSpecialPrice}">
											${fns:getCurrencyInfo(group.currencyType,5,'mark')}
											<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></span>
										</c:if>
                                    </span>
							</div>
						</td>
					</c:if>
					<%-- <c:if test="${user.quauqBookOrderPermission eq '1' and group.travelActivity.isT1 eq '1' and officeShelfRightsStatus ne '0'}">
						<c:if test="${group.travelActivity.isT1 eq '1'}">
							<td class="tc">
								<c:if test="${empty group.quauqAdultPrice or group.isT1 ne '1'}">—</c:if>
								<c:if test="${not empty group.quauqAdultPrice and group.isT1 eq '1'}">
									${fns:getCurrencyInfo(group.currencyType,0,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.adultRetailPrice }" /></span>
								</c:if>
								<input hidden="hidden" name="quauqAdultPrice" id="quauqAdultPrice" value="${group.quauqAdultPrice }">
							</td>
							<td class="tc">
								<c:if test="${empty group.quauqChildPrice or group.isT1 ne '1'}">—</c:if>
								<c:if test="${not empty group.quauqChildPrice and group.isT1 eq '1'}">
									${fns:getCurrencyInfo(group.currencyType,1,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.childRetailPrice }" /></span>
									<input hidden="hidden" name="quauqChildPrice" id="quauqChildPrice" value="${group.quauqChildPrice }">
								</c:if>
							</td>
							<td class="tc">
								<c:if test="${empty group.quauqSpecialPrice or group.isT1 ne '1'}">—</c:if>
								<c:if test="${not empty group.quauqSpecialPrice and group.isT1 eq '1'}">
									${fns:getCurrencyInfo(group.currencyType,2,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.specialRetailPrice }" /></span>
									<input hidden="hidden" name="quauqSpecialPrice" id="quauqSpecialPrice" value="${group.quauqSpecialPrice }">
								</c:if>
							</td>
						</c:if> --%>
						<%-- <c:if test="${group.travelActivity.isT1 ne '1'}">
							<td class="tc">—</td>
							<td class="tc">—</td>
							<td class="tc">—</td>
						</c:if> --%>
					<%--</c:if>--%>
					<%-- <c:if test="${user.quauqBookOrderPermission eq '1' and group.travelActivity.isT1 eq '0' && officeShelfRightsStatus ne '0'}">
						<c:if test="${group.travelActivity.isT1 ne '1'}">
							<td class="tc">—</td>
							<td class="tc">—</td>
							<td class="tc">—</td>
						</c:if>
					</c:if> --%>
					<td class="tc">
					    <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,99,'mark')}</c:if>
					    <span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit}" /></span>
					</td>
					<%--<td class="tc">--%>
					    <%--<c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,100,'mark')}</c:if>--%>
					    <%--<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff}" /></span>--%>
					<%--</td>--%>
					<%-- <shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
						<td class="tc"><span>${group.planPosition }</span></td>
						<td class="tr">${fns:getConfirmedNums(group.id)}</td>
						<td class="tr">${group.nopayReservePosition}</td>
					</shiro:hasPermission> --%>
					<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
					<%-- <c:if test="${isSeizedConfirmation == 1}"> --%>
						<td class="p0 tc">
							<div class="out-date">${group.planPosition }</div>
							<div class="close-date soldPayPosition${group.id}">${group.freePosition}</div>
						</td>
						<td class="p0 tr">
							<div class="out-date">${fns:getConfirmedNums(group.id)}</div>
							<div class="close-date">${group.nopayReservePosition}</div>
						</td>
					<%-- </c:if> --%>
					</shiro:hasPermission>
					<td class="p0">
						<div class="out-date">${group.payReservePosition }</div>
						<div class="close-date">${group.soldPayPosition }</div>
					</td>
					<c:if test="${isSeizedConfirmation ne true}">
						<td class="tc soldPayPosition${group.id}">
							<span>${group.freePosition}</span>
						</td>
					</c:if>
					<td class="p0">
						<%@ include file="/WEB-INF/views/modules/order/forOrder/payModeType.jsp"%>
					</td>
				</tr>
				<!--299-散拼团期列表-start-->
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
				
				<!-- 285 越柬行踪 散拼报名 团期列表默认展开备注 -->
				<!--<tr class="groupNoteCol" <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">style="display: none;"</c:if>>
					<td name="colspanCount">备注:
						<span class="groupNoteContent">${group.groupRemark}</span>
					</td>
				</tr>-->
				
				<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
				<tr class="groupNoteCol" <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">style="display: none;"</c:if>>
					<td name="colspanCount">备注:
						<span class="groupNoteContent">${group.groupRemark}</span>
					</td>
				</tr>
				
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