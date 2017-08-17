<%@ page contentType="text/html;charset=UTF-8"%>
<script type="text/javascript">
	$(window).load(function() {
		//等数据加载完成后，删除网页中rowspan等于0的html
		$("#costTable2 td[rowspan=0]").each(function(index, obj) {
			$(this).removeAttr("rowspan");
		});
	})
</script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="ydbz_tit"><!--  <span class="ydExpand" data-target="#baseInfo"></span>-->基本信息</div>
<div  style="color: #048601; font-size: 18px; font-weight: bold; padding-left: 40px; padding-top: 30px;">${activityIsland.activityName }</div>
<ul class="ydbz_qd">
	<li><label>团号：</label>${activityIslandGroup.groupCode}</li>
	<li><label>计调员：</label>${fns:getUserById(activityIsland.createBy).name}</li>
	<li><label>下单人：</label>${fns:getUserById(islandOrder.createBy).name}</li>
</ul>
<p class="ydbz_qdmc" style="padding: 0; padding-top: 10px;"></p>
<div id="signChannelList2">
	<ul class="ydbz_qd">
		<li><label>国家：</label><trekiz:autoId2Name4Table tableName='sys_geography' sourceColumnName='uuid' srcColumnName='name_cn' value='${activityIsland.country }'/></li>
		<li><label>岛屿名称：</label><trekiz:autoId2Name4Class classzName="Island" sourceProName="uuid" srcProName="islandName" value="${activityIsland.islandUuid }"/></li>
		<li><label>酒店名称：</label><trekiz:autoId2Name4Class classzName="Hotel" sourceProName="uuid" srcProName="nameCn" value="${activityIsland.hotelUuid }"/></li>
		<li>
			<label>酒店星级：</label> 
			<span class="y_xing">
				<c:if test="${not empty hotelLevel}">
					<c:forEach begin="1" end="${hotelLevel}">★</c:forEach>
				</c:if>
			</span>
		</li>
		<li><label>日期：</label><fmt:formatDate value="${activityIslandGroup.groupOpenDate }" pattern="yyyy-MM-dd" /></li>
		<li>
			<label>上岛方式：</label>
			<c:forEach var="islandway" items="${fn:split(activityIslandGroup.islandWay, ';')}">
				<trekiz:autoId2Name4Table tableName="sys_company_dict_view" sourceColumnName="uuid" srcColumnName="label" value="${fn:trim(islandway)}"/>
			</c:forEach>
		</li>
		<li><label>航空公司：</label>${airlineInfo.airlineName}</li>
		<li><label>航班号：</label>${airlineInfo.flightnumber}</li>
		<li>
			<label>起飞时间：</label>
			<fmt:formatDate value="${groupAirline.departureTime }" pattern="HH:mm" />
		</li>
		<li><label>到达时间：</label>
			<fmt:formatDate value="${groupAirline.arriveTime }" pattern="HH:mm" />
			<c:if test="${groupAirline.dayNum >0 }">
				&nbsp;+${groupAirline.dayNum }天 
			</c:if>
		</li>
		<li><label>余位：</label>${activityIslandGroup.remNumber }</li>
		<li><label>预收：</label> ${activityIslandGroup.advNumber } </li>
		<li><label>预报名：</label>${activityIslandGroup.orderNum} </li>
	</ul>
</div>
<table class="table activitylist_bodyer_table_new contentTable_preventive" id="costTable2" style="width:60% !important;">
	<thead>
		<tr>
			<th width="15%">房型&amp;晚数</th>
			<th width="15%">基础餐型</th>
			<th width="15%">升级餐型&amp;升级价格</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${groupRooms }" var="groupRoom" varStatus="groupRoomStatus">
			<c:set var="roomMealRiseNum" value="0"></c:set>
			<c:choose>
				<c:when test="${fn:length(groupRoom.activityIslandGroupMealList) <= 0 }">
					<tr>
						<td class="tc">
							<p><trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${groupRoom.hotelRoomUuid}"/> * ${groupRoom.nights }</p>
						</td>
						<td class="tc">
							<p></p>
						</td>
						<td class="tc">
							<p></p>
						</td>
					</tr>
				</c:when>
				
				<c:otherwise>
					<c:forEach items="${groupRoom.activityIslandGroupMealList }" var="groupMeal" varStatus="groupMealStatus">
						<c:if test="${fn:length(groupMeal.activityIslandGroupMealRiseList) eq 0}">
							<c:set var="roomMealRiseNum" value="${roomMealRiseNum + 1}"></c:set>
						</c:if>
						<c:if test="${fn:length(groupMeal.activityIslandGroupMealRiseList) ne 0}">
							<c:set var="roomMealRiseNum" value="${roomMealRiseNum + fn:length(groupMeal.activityIslandGroupMealRiseList)}"></c:set>
						</c:if>
					</c:forEach>	
					<c:forEach items="${groupRoom.activityIslandGroupMealList }" var="groupMeal" varStatus="groupMealStatus">
						<c:set var="mealRiseNum" value="${fn:length(groupMeal.activityIslandGroupMealRiseList)}"></c:set>
						<c:choose>
							<c:when test="${mealRiseNum <= 0 }">
								<c:if test="${groupMealStatus.index == 0 }">
									<td rowspan="${roomMealRiseNum}" class="tc">
										<p><trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${groupRoom.hotelRoomUuid}"/> * ${groupRoom.nights }</p>
									</td>
								</c:if>
								<td rowspan="${mealRiseNum}" class="tc">
									<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMeal.hotelMealUuid}"/></p>
								</td>
								<td class="tc">
									<p></p>
								</td>
							</c:when>
							<c:otherwise>
								<c:forEach items="${groupMeal.activityIslandGroupMealRiseList }" var="groupMealRise"  varStatus="groupMealRiseStatus">
									<tr>
										<c:if test="${groupMealStatus.index == 0  && groupMealRiseStatus.index == 0}">
											<td rowspan="${roomMealRiseNum}" class="tc">
												<p><trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${groupRoom.hotelRoomUuid}"/> * ${groupRoom.nights }</p>
											</td>
										</c:if>
										<c:if test="${groupMealRiseStatus.index == 0 }">
											<td rowspan="${mealRiseNum}" class="tc">
												<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMeal.hotelMealUuid}"/></p>
											</td>
										</c:if>
										<td class="tc">
											<p><trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${groupMealRise.hotelMealUuid}"/>
											<trekiz:autoId2Name4Table tableName='currency' sourceColumnName='currency_id' srcColumnName='currency_mark' value='${groupMealRise.currencyId }'/>
											<fmt:formatNumber  type="currency" pattern="##0.00" value="${groupMealRise.price}" />/人</p>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</tbody>
</table>
