<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 团期标题 -->
<thead id="orderOrGroup_group_thead">

		<tr>
			<th width="9%"><span class="tuanhao on">团号</span>/<span class="riqi">日期</span></th>
			<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
			<th width="12%"><!-- <span class="kongfang on" style="display:none;">控房单号/</span> --><span>产品名称</span></th>
			<th width="7%">岛屿</th>
			<th width="5%"> 计调</th>
			<th width="8%">酒店&amp;星级</th>
			<th width="7%">房型*晚数</th>
			<th width="6%">基础餐型</th>
			<th width="5%">上岛方式</th>
			<th width="9%">同行价/人</th>
			<th width="7%">余位/间数/预报名</th>
			<th width="6%">单房差</th>
			<th width="5%">需交订金</th>
			<th width="5%">操作</th>
			<th width="9%">下载</th>
		</tr>

</thead>

<!-- 团期查询列表显示 -->
<tbody id="orderOrGroup_group_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
			<td colspan="14" style="text-align: center;">
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
				<div class="riqi_cen qtip">
					${groups.groupOpenDate}
				</div>
			</td>
			<td>
				<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
				<%-- <div class="kongfang_cen onshow" style="display:none;">
					${groups.activitySerNum}
				</div> --%>
				<div class="chanpin_cen qtip onshow" title="${groups.activityName}">
				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${groups.activityUuid}?type=product')">${groups.activityName}</a>
				</div>
			</td>
			<td>${groups.islandName}</td>
			<td>${groups.carateUserName}</td>
			<td class="tc" >${groups.hotelName}<br /><span class="y_xing"><c:forEach begin="1" end="${groups.hotelLevel}">★</c:forEach></span></td>
			<td>
				<c:forEach items="${groups.roomInfo}" var="room">
					<p>${room.roomName}*${room.roomNights}</p>
				</c:forEach>
			</td>
			<td class="tc">
				<c:forEach items="${groups.meals}" var="meal">
					<p>${meal}</p>
				</c:forEach>
			</td>
			<td class="tc">
				<c:forEach items="${fn:split(groups.islandWay,';')}" var="var">
					<p>
						<trekiz:defineDict name="island_way" type="islands_way" defaultValue="${var}" readonly="true" />
					</p>
				</c:forEach>
			</td>
			<td>
				<c:forEach items="${groups.activityHotelGroupPriceList }" var="price">
					<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${price.type}" />:
					<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${price.currencyId }" />
					<fmt:formatNumber type="currency" pattern="#,##0.00" value="${price.price}" /><br />
				</c:forEach>
			</td>
			<td class="tc">
				<c:set var="groupOrderNum" value="0"/>
				${groups.freePosition }/${groups.freePosition }/<span name="groupOrderTotalNum">${groups.bookingRoomNum}</span>
			</td>
			<td class="tr">
				<span data-value="￥"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groups.singleCurrencyId}" /></span><span class="fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${groups.singlePrice}"></fmt:formatNumber></span>
			</td>
			<td class="tr">
				<span data-value="￥"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${groups.frontCurrencyId}" /></span><span class="fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${groups.frontMoney}"></fmt:formatNumber></span>
			</td>
			<td class="p0">
				<dl class="handle">
					<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
					<dd class="">
						<p>
							<span></span>
							<shiro:hasPermission name="hotelOrder:operation:view">
								<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${groups.activityUuid}?type=product')">产品详情</a>
							</shiro:hasPermission>
							<a onclick="expand('#order_${groups.groupUuid}',this,1080)">展开</a>
						</p>
					</dd>
				</dl>
			</td>
			
			<td class="p0">
				<dl class="handle">
					<dt><img title="下载" src="${ctxStatic}/images/handle_xz_rebuild.png"></dt>
					<dd class="">
						<p>
							<span></span>
							<shiro:hasPermission name="hotelOrder:download:travelerInfo">
								<a href="javascript:void(0)"  onClick="downloadGroupFiles('${groups.groupUuid}')">下载资料</a>
							</shiro:hasPermission>
						</p>
					</dd>
				</dl>
			</td>
		</tr>
		<tr name="subtr" class="activity_team_top1" id="order_${groups.groupUuid}" style="display: none">
			<td colspan="14" class="team_top" style="background-color:#dde7ef;">
				<table  class="table activitylist_bodyer_table" style="margin:0 auto;">
					<thead>
						<tr>
							<th width="10%" class="tc">订单号</th>
							<th width="5%" class="tc">人数</th>
							<th width="10%" class="tc">下单人</th>
							<th width="12%" class="tc">预定时间</th>
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
								<td  class="tc">
									${orders.orderPersonNum }
								</td>
								<td class="tc">${orders.carateUserName}</td>
		                   		<td class="tc">
			                      <fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
			                    </td>
								<td class="tc">
									<c:choose>
										<c:when test="${orders.orderStatus == 1}">
											<c:set var="groupOrderNum" value="${groupOrderNum + orders.forecaseReportNum}"></c:set>
											<span name="groupOrderNum" style="display: none">${groupOrderNum}</span>
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
								<td class="tr"><span class="tdorange fbold"><center><c:if test="${orders.orderStatus==1 }">----</c:if><c:if test="${orders.orderStatus==3 }">----</c:if><span>
								<c:if test="${orders.orderStatus==2 }">${orders.applyTime}</c:if> </center></span></td>
								<td class="tr"><span class="tdorange fbold">${orders.costMoney}</span></td>
								<td class="p0 tr"><div class="dzje_dd"><span class="fbold">${orders.totalMoney}</span></div></td>
								<td class="tr"><div class="dzje_dd"><span class="fbold">${orders.payedMoney}</span></div></td>
								<td class="tr"><span class="tdgreen fbold">${orders.accountedMoney}</span></td>
								<td class="p0 tr"><span class="tdorange fbold">${orders.notPayedMoney}</span></td>
								<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderOperation.jsp"%>
                            </tr>
                            <!-- 支付记录 -->
							<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderPayList.jsp"%>
						</c:if>
						<input id="group_${groups.groupUuid }" value="${orderUuids }" type="hidden">
					</c:forEach>
				</table>
			</td>
		</tr>
	</c:forEach>
</tbody>

