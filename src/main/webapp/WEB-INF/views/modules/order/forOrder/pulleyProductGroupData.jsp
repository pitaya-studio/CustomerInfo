<%@ page contentType="text/html;charset=UTF-8" %>
<tr id="child${s.count}" style="display:none" class="activity_team_top1">
	<td class="team_top" style="background-color:#d1e5f5;" name="colspanCount">
		<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;" name="colspanTable">
			<thead> 
				<tr name="colspanTr"> 
					<th class="tc" width="6%" rowspan="2">出/截团日期</th> 
					<th class="tc" width="6%" rowspan="2">团号</th>
					<!-- 284 游轮报名 产品列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<th class="tc" width="8%" rowspan="2">资料截止日期</th>
					</c:if>

					<th class="tc" width="8%" rowspan="2">舱型</th> 
					<th class="tc" width="18%" colspan="2" class="t-th2">同行价</th>
					<!-- 284 游轮报名 产品列表 针对越柬行踪屏蔽直客价 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<th class="tc" width="18%" colspan="2" class="t-th2">直客价</th>
					</c:if>

					<th class="tr" width="5%" rowspan="2">订金/人</th> 
					<th class="tr" width="6%" rowspan="2">单房差/人</th> 
					<th class="tr" width="4%" rowspan="2">预收/间</th> 
					<th class="tr" width="4%" rowspan="2">余位/间</th> 
					<th class="tr" width="5%" rowspan="2">预报名/人</th> 
					<th class="tr" rowspan="2" style="display:none" width="4%">切位</th> 
					<th class="tc" width="3%" rowspan="2">操作</th> 
				</tr> 
				<tr> 
					<th class="tr" width="6%">1/2同行价</th> 
					<th class="tr" width="6%">3/4同行价</th> 
					<th class="tr" width="6%" style="display: none">特殊人群</th>
					<!-- 284 游轮报名 产品列表 针对越柬行踪屏蔽直客价-->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<th class="tr" width="6%">1/2直客价</th>
						<th class="tr" width="6%">3/4直客价</th>
						<th class="tr" width="6%" style="display: none">特殊人群</th>
					</c:if>

				</tr> 
			</thead>
			<tbody>
				<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
					<tr>
						<td width="8%">
							<div class="out-date">
								<span ><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate }"/></span>
							</div>
							<div class="close-date">
								<span ><fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate }"/></span>
							</div>
						</td>
						<td class="word-break-all">
							${group.groupCode}
						</td>

						<!-- 284 游轮报名 产品列表 针对越柬行踪屏蔽 -->
						<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
							<td class="tc">
								<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
							</td>
						</c:if>

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
						<td class="tr" style="display: none">
							<c:if test="${not empty group.settlementSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,6,'mark')}</c:if><span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></span>
						</td>
						<!-- 284 游轮报名 产品列表 针对越柬行踪屏蔽直客价 -->
						<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
							<td class="tr">
								<c:if test="${not empty group.suggestAdultPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</c:if>
								<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }" /></span>
							</td>
							<td class="tr">
								<c:if test="${not empty group.suggestChildPrice}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
								<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }" /></span>
							</td>
							<td class="tr"  style="display: none">
								<c:if test="${not empty group.suggestSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,7,'mark')}</c:if>
								<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }" /></span>
							</td>
						</c:if>

						<td class="tr">
							<c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if><span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit }" /></span>
						</td>
						<td class="tr">
							<c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</c:if>
							<span  class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff }" /></span>
						</td>
						<td class="tr">
							<span>${group.planPosition }</span>
						</td>
						<td class="tr tdred">
							<span>${group.freePosition }</span>
						</td>
						<td style="display:none;" class="soldPayPosition${group.id}" width="4%">
							<span class="tdred" >${group.freePosition }</span>
						</td>
						<td class="tr">
							<span>${group.orderPersonNum }</span>
						</td>
						<td class="tc">
							<%@ include file="/WEB-INF/views/modules/order/forOrder/payModeType.jsp"%>
						</td>
					</tr>

					<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
					
					<!-- 285 越柬行踪 游轮报名 产品列表默认展开备注 -->
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
	</td>
</tr>