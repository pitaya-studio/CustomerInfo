<%@ page contentType="text/html;charset=UTF-8" %>
<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table mainTable activitylist_bodyer_table" name="colspanTable">
		<thead>
			<tr width="4%" name="colspanTr">
				<th rowspan="2" width="4%">序号</th>
				<th rowspan="2" width="6%">
					<span class="tuanhao on">团号</span>
					/
					<span class="chanpin">产品名称</span>
				</th>
				<th rowspan="2" width="4%">操作员</th>
				<th rowspan="2" width="6%">出/截团日期</th>
				<!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<th rowspan="2" width="6%">资料截止日期</th>
				</c:if>
				<th rowspan="2" width="4%">出发地</th>
				<!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<th rowspan="2" width="6%">签证国家</th>
				</c:if>

				<th rowspan="2" width="4%">航空</th>
				<th rowspan="2" width="4%">舱型</th>
				<th colspan="2" width="12%">同行价</th>
				<!-- C323大洋屏蔽 --><!--远程度假暂时隐藏直客价C381 --> <!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽直客价 -->
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<th colspan="2" width="12%">直客价</th>
				</c:if>
				<th rowspan="2" width="4%">订金/人</th>
				<th rowspan="2" width="4%">单房差/人</th>
				<th rowspan="2" width="6%">预收/间</th>
				<th rowspan="2" width="4%">余位/间</th>
				<th rowspan="2" width="5%">预报名/人</th>
				<th rowspan="2" width="5%">操作</th>
			</tr>
			<tr>
				<th width="6%">1/2同行价</th>
				<th width="6%">3/4同行价</th>
				<!-- C323大洋屏蔽 --><!--远程度假暂时隐藏直客价C381 -->  <!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽直客价 -->
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<th width="6%">1/2同行价</th>
						<th width="6%">3/4同行价</th>
				</c:if>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
					<td name="colspanCount" style="text-align: center;">
						暂无搜索结果
					</td>
				</tr>
			</c:if>
			<c:forEach items="${page.list}" var="group" varStatus="s">
				<c:set var="activity" value="${group.travelActivity}"></c:set>
				<tr>
					<td>${s.count}<br/><br/></td>
					<td class="word-break-all">
						<div class="tuanhao_cen onshow">
							<input type="hidden" name="groupID" value="${group.id }"/>
							<span style="word-break:break-all; display:block; word-wrap:break-word;">${group.groupCode}</span>
						</div>
						<div class="chanpin_cen qtip" title="${activity.acitivityName}">
							<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail4Groups/${group.srcActivityId}/${group.id}')">${activity.acitivityName}</a>
						</div>
					</td>
					<td class="tc" title="电话：${group.createBy.mobile}">
						${group.createBy.name}
					</td>
					<td class="p0" width="8%">
						<div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/></div>
						<div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></div>
					</td>
					<!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td width="8%" class="tc" >
							<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate}"/></span>
						</td>
					</c:if>

					<td>${activity.fromAreaName}</td>
					<!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td class="tc">
							${group.visaCountry}
						</td>
					</c:if>

					<td>
						<label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label>
					</td>
					<td class="tc">
						<span>${fns:getDictLabel(group.spaceType, "cruise_type", "-") }</span>
					</td>
					<td class="tr">
						<c:if test="${not empty group.settlementAdultPrice}">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</c:if>
						<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></span>
					</td>
					<td class="tr">
					    <c:if test="${not empty group.settlementcChildPrice}">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</c:if>
					    <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></span>
					</td>
					<!-- C323大洋屏蔽 --><!--远程度假暂时隐藏直客价C381 --> <!-- 284 游轮报名 团期列表 针对越柬行踪屏蔽直客价 -->
					<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
		              			<td class="tr">
								<c:if test="${not empty group.suggestAdultPrice}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
								<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></span>
							</td>
							<td class="tr">
								<c:if test="${not empty group.suggestChildPrice}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if>
								<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></span>
							</td>
					</c:if>
					<td class="tr">
					    <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
					    <span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit}" /></span>
					</td>
					<td class="tr">
					    <c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if>
					    <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff}" /></span>
					</td>
					<td class="tc">
						<span>${group.planPosition }</span>
					</td>
					<td class="soldPayPosition${group.id}">
						<span>${group.freePosition}</span>
					</td>
					<td class="tc">
						<span>${group.orderPersonNum }</span>
					</td>
					<td class="p0">
						<%@ include file="/WEB-INF/views/modules/order/forOrder/payModeType.jsp"%>
					</td>
				</tr>
				
				<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
				
				<!-- 285 越柬行踪 游轮报名 团期列表默认展开备注 -->
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
				
			</c:forEach>
		</tbody>
	</table>
	<c:set var="userinfo" value="${fns:getUser()}"/>
	<input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
	<!-- 获取批发商是否允许补单的值：0为否，1为是 -->
	<c:set var="isAllowSupplement" value="${userinfo.company.isAllowSupplement}"></c:set>
	<input type="hidden" id="isAllowSupplement" value="${isAllowSupplement}"/>
</form>