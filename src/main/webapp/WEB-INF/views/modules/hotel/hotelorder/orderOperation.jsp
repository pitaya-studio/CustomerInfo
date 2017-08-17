<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 操作 -->
<td class="p0">
	<dl class="handle">
		<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
		<dd class="">
			<p>
				<span></span>
				<shiro:hasPermission name="hotelOrder:operation:view">
					<a href="${ctx}/hotelOrder/hotelOrderDetail/${orders.orderUuid}"  target="_blank">详情</a>
				</shiro:hasPermission>
				
				<!-- 如果团期已锁定或订单已锁定，则不能有已下操作 -->
				<c:if test="${orders.settleLockStatus != 1 &&  orders.lockStatus != 1}">
					<!-- 修改 -->
					<shiro:hasPermission name="hotelOrder:operation:edit">
						<!-- 如果订单已确认或已取消，则不能修改订单 -->
						<c:if test="${orders.orderStatus == 1}">
							<a href="${ctx}/hotelOrder/toHotelOrderUpdatePage?orderUuid=${orders.orderUuid}&isTransfer=false"  target="_blank">修改</a>
							<a href="${ctx}/hotelOrder/toHotelOrderUpdatePage?orderUuid=${orders.orderUuid}&isTransfer=true"  target="_blank">转报名</a>
						</c:if>
					</shiro:hasPermission>
					
					<c:if test="${orders.orderStatus == 2}">
						<!-- 改价 -->
						<shiro:hasPermission name="hotelOrder:operation:changeprice">
							<a href="javascript:void(0)" onClick="viewHotelChangePriceList('${orders.id}','${orders.orderUuid}')"> 改价</a>
						</shiro:hasPermission>
						
						<!-- 返佣 -->
						<shiro:hasPermission name="hotelOrder:operation:returnCommission">
							<a href="javascript:rebatesInfo.rebatesOrder('${orders.id}','11','${orders.orderUuid }');">返佣</a>
						</shiro:hasPermission>
						
						<!--申请退团：已删除与已取消不能退团-->
						<!-- 
							<shiro:hasPermission name="hotelOrder:operation:toapplyLeague">
								<a href="javascript:void(0)" onClick="viewExitGroup('${orders.orderUuid}','${orderStatus}')">退团</a>
							</shiro:hasPermission>
						-->
						
						<!-- 转团 -->
						<!--  
							<shiro:hasPermission name="hotelOrder:operation:changeGroup">
								<a href="javascript:void(0)"  onClick="changeGroup(${orders.orderUuid})">转团</a>
							</shiro:hasPermission>
						-->
						<!-- 转款 -->
						<c:if test="${orders.transferMoneyCheck==1}">
							<shiro:hasPermission name="hotelOrder:operation:transfersMoney">
								<a href="javascript:void(0)" onclick="transfersMoney(${orders.id})">转款</a>
							</shiro:hasPermission>
						</c:if>
						<!-- 借款 -->
						<shiro:hasPermission name="hotelOrder:operation:borrowingList">
								<a href="javascript:void(0)" onClick="viewBorrowingList('${orders.id}','11','${orders.orderUuid }')">借款</a>
						</shiro:hasPermission>
						
						<!-- 退款-->
						<c:if test="${not empty orders.accountedMoney && orders.accountedMoney!='¥0.00'}">
							<shiro:hasPermission name="hotelOrder:operation:refund">
								<a href="javascript:viewGroupRefund('${orders.id}','${orderStatus}','${orders.orderUuid}')">退款</a>
							</shiro:hasPermission>
						</c:if>
					</c:if>
				</c:if>
				
				<shiro:hasPermission name="hotelOrder:operation:cancle">
					<!-- 只有待确认状态下订单可取消；订单锁定后不能取消；订单已有实收金额后不能取消 -->
					<c:if test="${orders.orderStatus == 1 && orders.lockStatus != 1 && empty orders.orderPayList}">
						<a href="javascript:cancelOrder('${orders.orderUuid}');">取消</a>
					</c:if>
				</shiro:hasPermission>
				
				<!-- 订单锁死或解锁 -->
				<shiro:hasPermission name="hotelOrder:operation:lock">
				    <c:if test="${orders.settleLockStatus != 1 && orders.lockStatus == 1 }">
				        <a href="javascript:unLockOrder('${orders.orderUuid}');">解锁</a>
				    </c:if>
				    <c:if test="${orders.settleLockStatus != 1 && (orders.lockStatus == 0 || orders.lockStatus == null) }">
				        <a href="javascript:lockOrder('${orders.orderUuid}');">锁死</a>
				    </c:if>
				</shiro:hasPermission>
				
				<!-- 上传订单资料 -->
				<shiro:hasPermission name="hotelOrder:upload">
					<a href="javascript:void(0)" onclick="uploadFiles('${orders.orderUuid}', this)">上传资料</a>
				</shiro:hasPermission>
				
				
                           
				<shiro:hasPermission name="hotelOrder:operation:invokeOrder">
					<!-- 99：已经取消订单：支付方式为预占位和订金占位  已经有支付记录的订单不允许激活（驳回订单） -->
					<c:if test="${orders.orderStatus==3 && orders.lockStatus != 1 && empty orders.orderPayList}">
						<a href="javascript:void(0)" onClick="invokeOrder('${orders.orderUuid}')">激活</a>
					</c:if>
				</shiro:hasPermission>
				
				<!-- 到账金额有钱显示发票申请按钮  -->
				<!--  暂时隐藏 
				<c:if test="${(not empty orders.accountedMoney) and (orders.usePrice >0 ) and (orders.createStatus eq 1 or empty orders.createStatus) or (orders.verifyStatus eq 2)}">
					<shiro:hasPermission name="hotelOrder:saler:invoice">
						<a href="javascript:void(0)" onClick="makinvoice(${orders.gruopId},${orders.id},'${orders.invoiceid}',${orders.orderCompany})">发票申请</a>
					</shiro:hasPermission>
				</c:if>
				-->
			</p>
		</dd>
	</dl>
</td>
<td class="p0">
	<dl class="handle">
		<dt><img title="下载" src="${ctxStatic}/images/handle_xz.png"></dt>
		<dd class="">
			<p <c:if test="${operation==false && fns:getUser().id != orders.createBy}">style="display: none;"</c:if>>
				<span></span>
				<shiro:hasPermission name="hotelOrder:download">
					<a href="javascript:void(0)"  onClick="downloadFiles('${orders.orderUuid}')">下载资料</a>
				</shiro:hasPermission>
			</p>
		</dd>
	</dl>
</td>
<td class="p0">
	<dl class="handle" <c:if test="${(operator==3&&orders.roleType!=3) || (operator==4&&orders.roleType!=3&&orders.roleType!=4)}">style="display: none;"</c:if>>
		<dt><img title="财务" src="${ctxStatic}/images/handle_fk.png"></dt>
		<dd class="">
			<p>
				<span></span>
				<shiro:hasPermission name="hotelOrder:operation:view">
					<c:if test="${fn:length(orders.orderPayList)>0 }">
						<a href="javascript:void(0)" onClick="javascript:showOrderPay(${orders.id},this);" class="jtk">收款记录</a>
						<c:set var="shiroOption" value="trues"></c:set>
					</c:if>
				</shiro:hasPermission>
				<shiro:hasPermission name="hotelOrder:operation:topay">
					<c:if test="${orders.orderStatus !=3 }">
						<a href="javascript:void(0)" onClick="orderPay('${orders.orderUuid}', '${orders.orderNum}', '${orders.orderCompany}', '${orders.totalMoney}', '${ctx}/orderCommon/manage/orderDetail/${orders.id}');">收款</a>
						<c:set var="shiroOption" value="trues"></c:set>
					</c:if>
				</shiro:hasPermission>
				<!--  暂时隐藏 
				<shiro:hasPermission name="hotelOrder:saler:invoice">
					<a href="javascript:viewInvoiceInfo('${orders.id}','${orders.orderNum }','${orderStatus }')" class="jtk">发票明细</a>
					<c:set var="shiroOption" value="trues"></c:set>
				</shiro:hasPermission>
				-->
			</p>
		</dd>
	</dl>
</td>
