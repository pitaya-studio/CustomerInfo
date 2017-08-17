<%@ page contentType="text/html;charset=UTF-8"%>
<div class="groupOrder J_m_nav" id="J_m_nav">
	<span class="groupOne">订单信息</span>
	<span class="groupTwo">总计</span>
	<span class="groupThree">实付款</span>
	<span class="groupFour">待确认付款</span>
	<span class="groupFive">订单状态</span>
	<span class="groupSix">操作</span>
</div>
                        
<!-- 团期列表 -->
<c:forEach items="${page.list}" var="group" varStatus="s">
	<div class="groupOrderChildren">
		<p class="groupOrderChildrenOne">
			<span class="tuanhao">团号：${group.groupCode}</span>
			<span>出团日期：${group.groupOpenDate}</span>
			<span title="${group.acitivityName}">${group.acitivityName}</span>
			<span>余位：${group.freePosition}</span>
			<em title="下载行程单" class="downLoad" onclick="downloads('${group.docIds}')"></em>
		</p>

		<c:forEach items="${orders}" var="orders">
			<c:if test="${orders.productGroupId eq group.id}">
				<p class="groupOrderChildrenTwo">
					<span class="groupChildrenOne">
						<span>
							<span>订单号：${orders.orderNum}</span>
							<span><fmt:formatDate value="${orders.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
						</span>
						<span>
							<span><em class="contact"></em>${orders.salerName}</span>
							<span><em class="phone"></em>${orders.salerMobile}</span>
						</span>
						<span>
							<span>儿童×${orders.orderPersonNumChild }</span>
							<span>成人×${orders.orderPersonNumAdult }</span>
							<span>特殊人群×${orders.orderPersonNumSpecial }</span>
						</span>
					</span>
					<span class="groupChildrenTwo">
						<span class="white">
							<input name="orderNumForRebate" value="${orders.orderNum}" type="hidden">
						</span>
						<span class="groupM">${orders.totalMoney}</span>
					</span>
					<span class="groupChildrenThree">
						<span>${orders.payedMoney}</span>
					</span>
					<span class="groupChildrenFour">
						<span>${orders.notAccountedMoney}</span>
					</span>
					<span class="groupChildrenFive">
						<span>
							<c:choose>
								<c:when test="${orders.delFlag eq '3'}">待生成订单</c:when>
								<c:when test="${orders.delFlag eq '4'}">未生成订单</c:when>
								<c:otherwise>${fns:getDictLabel(orders.orderStatus, "order_pay_status", "")}</c:otherwise>
							</c:choose>
						</span>
					</span>
					<span class="groupChildrenSix">
						<span><a href="javascript:void(0)" onClick="quauqOrderDetail(${orders.id});"><em title="详情" class="look"></em></a></span>
					</span>
				</p>
			</c:if>
		</c:forEach>
	</div>
</c:forEach>