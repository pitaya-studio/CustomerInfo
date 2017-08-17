<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 团期标题 -->
<thead style="background:#403738;" id="orderOrGroup_group_thead">
	<tr>			
		<th width="20%"><span class="tuanhao on">团号</span>/<span class="chanpin">产品名称</span></th>
		<th width="15%">计调</th>
		<th width="20%">出团日期</th>
		<th width="15%">剩余/总人数</th>
		<th width="5%">操作</th>
		<th width="5%">下载</th>
	</tr>
</thead>

<!-- 团期查询列表显示 -->
<tbody id="orderOrGroup_group_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
			<td colspan="6" style="text-align: center;">
				暂无搜索结果
			</td>
		</tr>
	</c:if>
       
	<!-- 查询结果团期列表循环显示 -->
	<c:forEach items="${page.list }" var="groups" varStatus="s">
		<tr class="toptr">
			<td>
				<div class="tuanhao_cen onshow">
					${groups.groupCode}
				</div>
				<div class="chanpin_cen qtip" title="${groups.activityName}">
					<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${groups.activityUuid}?type=product')">${groups.activityName}</a>
				</div>
			</td>
			<td>${groups.carateUserName}</td>
			<td class="p0">
				<div class="out-date"><fmt:formatDate value="${groups.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
			</td>
			<td class="p0">
				<div class="out-date">${groups.freePosition}</div>
				<div class="close-date">${groups.planPosition}</div>
			</td>
			<td class="p0">
				<dl class="handle">
					<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
					<dd class="">
						<p>
							<span></span>
							<shiro:hasPermission name="islandOrder:operation:view">
								<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${groups.activityUuid}?type=product')">产品详情</a>
							</shiro:hasPermission>
							<a onclick="expand('#order_${groups.groupUuid}',this,1080)">展开</a>
						</p>
					</dd>
				</dl>
			</td>
			
			<td class="p0">
				<dl class="handle">
					<dt><img title="下载" src="${ctxStatic}/images/handle_xz.png"></dt>
					<dd class="">
						<p>
							<span></span>
							<shiro:hasPermission name="islandOrder:download:travelerInfo">
								<a href="javascript:void(0)"  onClick="downloadGroupFiles('${groups.groupUuid}')">下载资料</a>
							</shiro:hasPermission>
						</p>
					</dd>
				</dl>
			</td>
		</tr>
		<tr name="subtr" class="activity_team_top1" id="order_${groups.groupUuid}" style="display: none">
			<td colspan="13" class="team_top" style="background-color:#dde7ef;">
				<table  class="table activitylist_bodyer_table" style="margin:0 auto;">
					<thead>
						<tr>
							<th width="10%" class="tc">订单号</th>
							<th width="10%" class="tc">下单人</th>
							<th width="12%" class="tc">预定时间</th>
							<th width="5%" class="tc">人数</th>
							<th width="8%" class="tc">订单状态</th>
							<th width="8%" class="tc">转报名时间</th>
							<th width="8%" class="tr">订单总额</th>
							<th width="8%" class="tr">应收总额</th>
							<th width="8%" class="tr">已收总额</th>
							<th width="8%" class="tr">达账总额</th>
							<th width="5%" class="tr">未收金额</th>
							<th width="5%" class="tc">操作</th>
							<th width="5%" class="tc">下载</th>
							<th width="5%" class="tc">财务</th>
						</tr>
					</thead> 
					<c:forEach items="${orders}" var="orders">
						<!-- 如果订单团期编号等于正在循环团期UUID，则显示，否则不显示 -->
						<c:if test="${orders.groupUuid eq groups.groupUuid}">
							<c:set var="orderUuids" value="${orders.orderUuid},${orderUuids}"></c:set>
							<tr>
								<td class="tc">
									${orders.orderNum }
								</td>
								<td class="tc">${orders.carateUserName}</td>
		                   		<td class="tc">
			                      <fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
			                    </td>
								<td class="tc">
									${orders.orderPersonNum }
								</td>
								<td class="tc">
									<c:choose>
										<c:when test="${orders.orderStatus == 1}">
											待确认报名
										</c:when>
										<c:when test="${orders.orderStatus == 2}">
											已确认报名
										</c:when>
										<c:otherwise>
											已取消
										</c:otherwise>
									</c:choose>
								</td>
								<td class="tr"><span class="tdorange fbold"><center>
									<c:if test="${orders.orderStatus==1 }">----</c:if>
									<c:if test="${orders.orderStatus==3 }">----</c:if><span> 
									<c:if test="${orders.orderStatus == 2}"><fmt:formatDate value="${orders.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/> </c:if>
								</center></span></td>
								<td class="tr"><span class="tdorange fbold">${orders.costMoney}</span></td>
								<td class="tr"><span class="tdorange fbold">${orders.totalMoney}</span></td>
								<td class="tr"><span class="tdorange fbold">${orders.payedMoney}</span></td>
								<td class="tr"><span class="tdorange fbold">${orders.accountedMoney}</span></td>
								<td class="tr"><span class="tdorange fbold">${orders.notPayedMoney}</span></td>
								<%@ include file="/WEB-INF/views/modules/island/islandorder/orderOperation.jsp"%>
                            </tr>
                            <!-- 支付记录 -->
							<%@ include file="/WEB-INF/views/modules/island/islandorder/orderPayList.jsp"%>
						</c:if>
						<input id="group_${groups.groupUuid }" value="${orderUuids }" type="hidden">
					</c:forEach>
				</table>
			</td>
		</tr>
	</c:forEach>
</tbody>

