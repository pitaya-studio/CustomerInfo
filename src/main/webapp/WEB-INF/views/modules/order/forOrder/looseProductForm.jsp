<%@ page contentType="text/html;charset=UTF-8" %>
<form id="groupform" name="groupform" action="" method="post" >
	<table id="contentTable" class="table mainTable activitylist_bodyer_table" style="table-layout:fixed;" name="colspanTable">
		<thead style="background:#403738">
			<tr name="colspanTr">
				<th width="5%">序号</th>
				<th width="23%">产品名称</th>
				<th width="8%">操作员</th>
				<th width="10%">出/截团日期</th>
				<!-- 284 散拼报名 产品列表 针对越柬行踪屏蔽 -->
				<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
					<th width="7%">签证国家</th>
				</c:if>

				<th width="10%">出发地</th>
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
					<th width="5%">航空</th><!-- C323大洋屏蔽 -->
				</c:if>
				<th width="7%">同行价</th>
				<th width="15%">最近出团日期</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
					<td name="colspanCount" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>
			<!-- 循环产品 -->
			<c:forEach items="${page.list}" var="activity" varStatus="s">
				<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
				<c:set var="freePositions" value="0"></c:set>
				<c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
					<c:if test="${activity.groupOpenDate eq group.groupOpenDate}">
						<c:set var="freePositions" value="${freePositions + group.freePosition }"></c:set>
						<c:set var="visaCountry" value="${group.visaCountry}"></c:set>
					</c:if>
					<fmt:formatDate value="${group.groupOpenDate}" var="groupOpenDate" pattern="yyyy-MM-dd"/>
					<fmt:formatDate value="${group.groupCloseDate}" var="groupCloseDate" pattern="yyyy-MM-dd"/>
				</c:forEach>
				<tr id="parent${s.count}">
					<td>
						<div class="table_borderLeftN">
							<c:choose>
								<c:when test="${activityKind == 2}">
									${s.count}&nbsp&nbsp<input type="checkbox" name="activityId" value="${activity.id }" onclick="acidcheckchg(this, ${s.count})"/>
								</c:when>
								<c:otherwise>
									${s.count}
								</c:otherwise>
							</c:choose>
						</div>
					</td>
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
					<td class="tc" title="电话：${activity.createBy.mobile}">
						${activity.createBy.name}
					</td>
					<td class="p0">
						<div class="out-date">${groupOpenDate}</div>
						<div class="close-date">${groupCloseDate}</div>
					</td>
					<!-- 284 散拼报名 产品列表 针对越柬行踪屏蔽 -->
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<td>
							${visaCountry}
						</td>
					</c:if>

					
					<td>${activity.fromAreaName}</td>
					<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
						<td>
							<c:choose>
								<c:when test="${activity.activityAirTicket.airlines == '-1'}">
								</c:when>
								<c:otherwise>
									<label class="qtip" title="${activity.trafficNameDesc}">${activity.activityAirTicket.airlines}</label>
								</c:otherwise>
							</c:choose>
						</td>
					</c:if>
					<td class="tr">
						<c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if>
						<c:if test="${activity.settlementAdultPrice>0}">
							${fns:getCurrencyInfo(activity.currencyType,0,'mark')}<span class="tdred fbold"><fmt:formatNumber value="${activity.settlementAdultPrice}"  type="currency" pattern="#,##0.00"/></span>起
						</c:if>
					</td>
					<td>
						<c:if test="${groupsize ne 0 }">
							<c:choose>
								<c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">${activity.groupOpenDate}</c:when>
								<c:otherwise>${activity.groupOpenDate}</c:otherwise>
							</c:choose>
							(余位：${freePositions})
							<br/>
							<c:if test="${showType=='1'}">
								<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${showType})" onMouseenter="if($(this).html()=='团期预定'){$(this).html('展开团期预定')}" onMouseleave="if($(this).html()=='展开团期预定'){$(this).html('团期预定')}">团期预定</a>
							</c:if>
							<c:if test="${showType=='2'}">
								<a id="close${s.count}" href="javascript:void(0)" class="team_a_click" onClick="expand('#child${s.count}',this,${showType})" onMouseenter="if($(this).html()=='团期预报名'){$(this).html('展开团期预报名')}" onMouseleave="if($(this).html()=='展开团期预报名'){$(this).html('团期预报名')}">团期预报名</a>
							</c:if>
						</c:if>
						<c:if test="${groupsize == 0 }">日期待定</c:if> 
					</td>
				</tr>
				<c:choose>
					<c:when test="${activityKind == 2}">
						<%@ include file="/WEB-INF/views/modules/order/forOrder/looseProductGroupData.jsp"%>
					</c:when>
					<c:otherwise>
						<%@ include file="/WEB-INF/views/modules/order/forOrder/pulleyProductGroupData.jsp"%>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</tbody>
	</table>
	<c:set var="userinfo" value="${fns:getUser()}"/>
	<input type="hidden" id="tempUserName" value="${userinfo.loginName }"/>
	<!-- 获取批发商是否允许补单的值：0为否，1为是 -->
	<c:set var="isAllowSupplement" value="${userinfo.company.isAllowSupplement}"></c:set>
	<input type="hidden" id="isAllowSupplement" value="${isAllowSupplement}"/>
</form>