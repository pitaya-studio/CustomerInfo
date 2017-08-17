<%@ page contentType="text/html;charset=UTF-8" %>
<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table mainTable activitylist_bodyer_table" name="colspanTable">
		<thead style="background:#403738">
			<tr width="4%" name="colspanTr">
				<th rowspan="2" width="3%">序号</th>
				<th rowspan="2" width="6%">
					<span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span>
				</th>
				<th rowspan="2" width="8%">询价客户</th>
				<th rowspan="2" width="6%">操作员</th>
				<th rowspan="2" width="8%">出/截团日期</th>
				<th rowspan="2" width="6%">出发地</th>
				<!-- 284 单团报名 团期列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<th rowspan="2" width="6%">签证国家</th>
				</c:if>
				<th rowspan="2" width="8%">航空</th>
				<!-- 284 单团报名 团期列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<th rowspan="2" width="8%">资料截止日期</th>
				</c:if>
				<shiro:hasPermission name="price:project">
					<th rowspan="2" width="6%">酒店房型</th>
				</shiro:hasPermission>
				<th colspan="3" width="8%">同行价</th>
				<th rowspan="2" width="5%">订金/人</th>
				<th rowspan="2" width="5%">单房差/间夜</th>
				<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
					<th rowspan="2" width="4%">预收</th>
					<th rowspan="2" width="3%">已确认人数</th>
					<th rowspan="2" width="3%">已占位人数</th>
				</shiro:hasPermission>
				<th rowspan="2" width="4%">余位</th>

				<th rowspan="2" width="8%">操作</th>
			</tr>
			<tr>
				<th width="6%">成人</th>
				<th width="6%">儿童</th>
				<th width="6%">特殊人群</th>
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
					<td>${s.count}<br/><br/></td>
					<td class="word-break-all">
						<div class="tuanhao_cen onshow">
							<input type="hidden" name="groupID" value="${group.id }"/>
							<span style="word-break:break-all; display:block; word-wrap:break-word;">${group.groupCode}</span>
						</div>
						<div class="chanpin_cen qtip" title="${activity.acitivityName}">
							<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail4Groups/${group.srcActivityId}/${group.id}?isOp=0')">${activity.acitivityName}</a>
						</div>
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
					<td>${activity.fromAreaName}</td>
					<!-- 284 单团报名 团期列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td class="tc">
							${group.visaCountry}
						</td>
					</c:if>

					<td>
						<label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label>
					</td>
					<!-- 284 单团报名 团期列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td width="8%" class="tc" >
							<span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate}"/></span>
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
					    <c:if test="${not empty group.settlementcChildPrice}">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</c:if>
					    <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></span>
					</td>
					<td class="tr">
					    <c:if test="${not empty group.settlementSpecialPrice}">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</c:if>
					    <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></span>
					</td>
					<td class="tr">
					    <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</c:if>
					    <span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.payDeposit}" /></span>
					</td>
					<td class="tr">
					    <c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</c:if>
					    <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.singleDiff}" /></span>
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
					<td class="soldPayPosition${group.id}">
						<span>${group.freePosition}</span>
					</td>
					<td class="p0">
						<c:if test="${activity.payMode_full=='1'}">
							<a style="display:none;" href="javascript:void(0)"  class="normalPayType aPayforModePrice${group.id}  
								<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
								<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
								<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},3,${group.freePosition})' </c:if>>
								付全款
							</a>
						</c:if>
						<c:if test="${activity.payMode_op=='1'}">
							<a style="display:none;" href="javascript:void(0)"  class="opPayType aPayforModePrice${group.id}  
								<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
								<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
								<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},7,${group.freePosition})' </c:if>>
								计调确认占位
							</a>
						</c:if>
						<c:if test="${activity.payMode_cw=='1'}">
							<a style="display:none;" href="javascript:void(0)"  class="cwPayType aPayforModePrice${group.id}  
								<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
								<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
								<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},8,${group.freePosition})' </c:if>>
								计调确认占位
							</a>
						</c:if>
						<c:if test="${activity.payMode_deposit=='1'}">
							<a style="display:none;" href="javascript:void(0)"  class="dingjin_PayType aPayforModePrice${group.id}  
								<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
								<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
								<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},1,${group.freePosition})' </c:if>>
								订金占位
							</a>
						</c:if>
						<c:if test="${activity.payMode_advance=='1'}">
							<a style="display:none;" href="javascript:void(0)"  class="yuzhan_PayType aPayforModePrice${group.id}  
								<c:if test="${group.freePosition > 0  && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> canClick </c:if>" style='padding-right:5px; 
								<c:if test="${group.freePosition <= 0 || (group.settlementAdultPrice<=0&&group.settlementcChildPrice<=0&&group.settlementSpecialPrice<=0)}">color:gray;</c:if>' 
								<c:if test="${group.freePosition > 0 && (group.settlementAdultPrice>0||group.settlementcChildPrice>0||group.settlementSpecialPrice>0)}"> onClick='occupied(${group.id},${activity.id},2,${group.freePosition})' </c:if>>
								预占位
							</a>
						</c:if>
						
						<input hidden="hidden" name="bookOrder" value="${group.freePosition},${group.leftdays},${group.settlementAdultPrice},${group.settlementcChildPrice},${group.settlementSpecialPrice},${group.suggestAdultPrice},${group.suggestChildPrice},${group.suggestSpecialPrice}"/>
	                               
						<select style="display:none;">
							<c:if test="${activity.payMode_full=='1'}">
								<option value='3'>全款支付</option>
							</c:if>
							<c:if test="${activity.payMode_op=='1'}">
								<option value='7'>计调确认占位</option>
							</c:if>
							<c:if test="${activity.payMode_cw=='1'}">
								<option value='8'>财务确认占位</option>
							</c:if>
							<c:if test="${activity.payMode_deposit=='1'}">
								<option value='1'>订金占位</option>
							</c:if>
							<c:if test="${activity.payMode_advance=='1'}">
								<option value='2'>预占位</option>
							</c:if>
						</select>
						<dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"/>
							</dt>
							<dd class="">
								<p style="width: 72px">
									<span></span>
									<a href="javascript:void(0)" onclick="groupForOrder(this);"></a>
									<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail4Groups/${group.srcActivityId}/${group.id}')">详 情</a>
									<c:if test="${queryCommonOrderList=='1' && activityKind == 1}">
										<a href="javascript:void(0)" onclick="showOrderPay(1234,this);">已收明细</a>
									</c:if>

									<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
									
									<!-- 285 越柬行踪 单团报名 团期列表默认展开备注 -->
									<!--<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
										<a class="expandNotes" href="javascript:void(0)">收起备注</a>
									</c:if>
									<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
										<a class="expandNotes" href="javascript:void(0)">展开备注</a>
									</c:if>-->
									
									<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
									<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' || companyUuid eq 'cb19b61e52fb4b6ab422aedac38acdfa'}">
										<a class="expandNotes" href="javascript:void(0)">收起备注</a>
									</c:if>
									<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">
										<a class="expandNotes" href="javascript:void(0)">展开备注</a>
									</c:if>
									
									<!-- 576需求-报名团期备注默认展开***end*** -->
									
									<em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em>

									<shiro:hasPermission name="price:project">
										<c:if test="${not empty group.priceJson}">
											<a href="javascript:void(0);" onclick="expandPriceJson(this, ${activityKind})" data='${group.priceJson}'>展开价格方案</a>
										</c:if>
									</shiro:hasPermission>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
				<!--299-单团团期列表-start-->
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
				<!--299-单团团期列表-end-->
				<%@ include file="/WEB-INF/views/modules/order/forOrder/salerDetail.jsp"%>

				<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
				
				<!-- 285 越柬行踪 单团报名 团期列表默认展开备注 -->
				<!--<tr class="groupNoteCol" <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">style="display:none;"</c:if> >
					<td name="colspanCount">备注:
						<span class="groupNoteContent">${group.groupRemark}</span>
					</td>
				</tr>-->
				<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
				<tr class="groupNoteCol" <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">style="display:none;"</c:if> >
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