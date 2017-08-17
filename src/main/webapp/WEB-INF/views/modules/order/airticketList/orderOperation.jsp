<%@ page contentType="text/html;charset=UTF-8"%>
<td class="p0">
	<dl class="handle">
		<dt>
			<img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作">
		</dt>
		<dd>
			<p>
				<span></span>
				<a target="_blank" href="${ctx}/order/manage/airticketOrderDetail?orderId=${order.id}">详情 </a>
					<!-- 计调锁死订单时，销售只能进行查看详情操作，update by zhanghao 蔡晓明确认的需求 20150508 -->
					<!-- 订单不可修改的情况：订单在取消、锁定、结算单和预报单锁定 C336, addby yunpeng.zhang 2015年12月29日 -->
					<c:if test="${queryType == 1}">
						<shiro:hasPermission name="airticketOrderSale:modify">
							<c:if test="${order.orderType == 1 && order.lockStatus != 2 and order.lockStatus != 1
									and order.activityLockStatus != 1 and order.activityForcastStatus != 10 and order.order_state != 99 and order.order_state != 7}">
								<a target="_blank" href="${ctx}/order/manage/airticketOrderSale4Modify?orderId=${order.id}&queryType=${queryType}">修改</a>
							</c:if>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${queryType == 2}">
					<shiro:hasPermission name="airticketOrderOprt:modify">
						<c:if test="${order.orderType == 1 && order.lockStatus != 2 && order.lockStatus != 1
									&& order.activityLockStatus != 1 && order.activityForcastStatus != 10 && order.order_state != 99}">
							<a target="_blank" href="${ctx}/order/manage/airticketOrderSale4Modify?orderId=${order.id}&queryType=${queryType}">修改</a>
						</c:if>
					</shiro:hasPermission>
					</c:if>
				<c:if test="${queryType == 1}">
					<shiro:hasPermission name="airticketOrderSale:upload:confirmation">
						<c:if test="${order.orderType == 1 && order.activityLockStatus != 1  && order.lockStatus != 1 && order.order_state != 7}">
							<c:if test="${order.order_state != 99 && order.order_state != 111}">
								<a href="javascript:void(0)"  onclick="uploadConfirmFiles('confirmFiles',this,'${order.id}','/order/manage/uploadConfirmation');">上传确认单</a>
							</c:if>
						</c:if>
					</shiro:hasPermission>
				</c:if>
				<c:if test="${queryType == 2}">
					<shiro:hasPermission name="airticketOrderOprt:upload:confirmation">
						<c:if test="${order.orderType == 1 && order.activityLockStatus != 1  && order.lockStatus != 1 && order.order_state != 7}">
							<c:if test="${order.order_state != 99 && order.order_state != 111}">
								<a href="javascript:void(0)"  onclick="uploadConfirmFiles('confirmFiles',this,'${order.id}','/order/manage/uploadConfirmation');">上传确认单</a>
							</c:if>
						</c:if>
					</shiro:hasPermission>
				</c:if>

				<!-- 客户确认。操纵已占位订单的客户确认状态 需求286 -->
				<%-- 575 wangyang 2017.1.4 --%>
				<c:if test="${queryType eq 1 }"><c:set var="type" value="Sale"></c:set></c:if>
				<c:if test="${queryType eq 2 }"><c:set var="type" value="Op"></c:set></c:if>
				<shiro:hasPermission name="airticketOrderFor${type }:operation:customerConfirm">
					<c:if test="${(order.order_state == 3 || order.order_state == 4 || order.order_state == 5) and order.seizedConfirmationStatus ne 1 }">
						<a href="javascript:void(0)" onclick="seizedConfirm('${order.id}');">客户确认</a>
					</c:if>
				</shiro:hasPermission>
				<c:if test="${order.orderType eq 1 and ((order.order_state eq 7 and queryType eq 2) or queryType eq 1) and order.activityLockStatus ne 1 and order.lockStatus ne 1}">
					<shiro:hasPermission name="airticketOrderForSale:operation:cancel">
						<!-- 订单状态等于全款未支付，预订后没有支付，或者等于订金未支付，订金占位后没有支付，或者等于已占位 ，或者等于（切位订单并且订金已经支付） -->
						<c:if test="${(order.order_state==1 || order.order_state==2 || order.order_state==3 || order.order_state==7 || (order.placeHolderType==1&&order.order_state==4)||(order.occupyType == 8 && empty order.payedMoney)) && fns:canCancel(orderStatus,order.id) == '0' && order.order_state != 10 && order.seizedConfirmationStatus ne 1}">
							<a href="javascript:cancelOrder(${order.id});">取消</a>
						</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="airticketOrderForSale:operation:invoke">
						<c:if test="${order.order_state==99 || order.order_state==2 || order.order_state==3}">
							<a href="javascript:void(0)" onClick="invokeOrder(${order.id})">激活</a>
						</c:if>
					</shiro:hasPermission>
				</c:if>
				
				<c:if test="${queryType == 1 && order.orderType == 1 && order.activityLockStatus != 1  && order.lockStatus != 1 && order.order_state != 7}">
					<c:if test="${order.order_state != 99 && order.order_state != 111}">
						<a href="${ctx}/changeprice/airticket/changePriceList?orderId=${order.id}&productType=7&flowType=10"  target="_blank">改价</a>
						<a href="${ctx}/airticketreturnnew/airticketReturnList?orderId=${order.id}&flowType=3&orderType=7">退票</a>
						<a href="${ctx}/airticketChange/airChange/airticketApprovalHistoryList?orderId=${order.id}"  target="_blank">改签</a>
						
					</c:if>
					<c:if test="${order.order_state != 99 && order.order_state != 111 && order.order_state != 3 && not empty order.accountedMoney && order.accountedMoney!='¥0.00'}">
						<a href="${ctx}/order/refundNew/viewListNew?orderId=${order.id}">退款</a>
					</c:if>
					 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       		<c:choose>
		      			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							<a href="${ctx}/order/newAirticketRebate/airticketRebatesList?flowType=9&productType=7&orderId=${order.id}" target="_blank">宣传费</a>
						</c:when>
		         		<c:otherwise>
		         			<a href="${ctx}/order/newAirticketRebate/airticketRebatesList?flowType=9&productType=7&orderId=${order.id}" target="_blank">返佣</a>
		          		</c:otherwise>
				 </c:choose>   
					
					<a href="javascript:applyInvoice('${order.orderNo}',${order.id},7)">申请发票</a>
					<a href="javascript:applyReceipt('${order.orderNo}',${order.id},7)">申请收据</a>
				</c:if>
				<!-- 订单状态不等于已删除，并且不等于订金已经支付，并且不等于已支付全款，并且结算单没锁定，并且订单没锁定,并且订单不等于计调占位 -->
				<c:if test="${(queryType == 1 or (queryType == 2 and order.order_state eq 7)) && order.order_state!=111 && order.order_state!=4 && order.order_state!=5 && order.activityLockStatus != 1 && order.lockStatus != 1 && fns:canCancel(orderStatus,order.id) == '0' && !(order.occupyType == 8 && not empty order.payedMoney) && order.seizedConfirmationStatus ne 1}">
					<a href="javascript:deleteOrderByFlag(${order.id});">删除</a>
				</c:if>
					
				<c:if test="${queryType == 2 && order.activityLockStatus != 1 && order.lockStatus != 1 && order.order_state == 7}">
					<a href="javascript:void(0)" onClick="confirmOrder(${order.id})">确认占位</a>
                </c:if>
                <c:if test="${fns:getUser().company.orderPayMode eq 1}">
                	<c:if test="${queryType == 2  && order.occupyType == 8 && (order.order_state == 4 || order.order_state == 5)}">
						<a href="javascript:void(0)" onClick="revokeOrder(${order.id})">撤销占位</a>
	                </c:if>
                </c:if>
                <c:if test="${queryType == 2 && order.orderType == 1 && order.order_state != 7}">
					<shiro:hasPermission name="airticketOrderForOp:operation:lock">
						<!-- 如果结算单被锁死，则订单不能解锁和锁死 -->
						<c:if test="${order.activityLockStatus != 1 }">
							<c:if test="${order.lockStatus == 1 }">
						        <a href="javascript:unLockOrder(${order.id});">解锁</a>
						    </c:if>
						    <c:if test="${order.lockStatus == 0 || order.lockStatus == null }">
						        <a href="javascript:lockOrder(${order.id});">锁死</a>
						    </c:if>
						</c:if>
				    </shiro:hasPermission>
				</c:if>
				<c:if test="${order.orderType == 1 && order.activityLockStatus != 1 && order.lockStatus != 1 && order.order_state != 7}">
					<a href="${ctx}/activityOrder/lendmoney/borrowAmountList?flowType=19&productType=7&orderId=${order.id}"  target="_blank">借款</a>
				</c:if>
				<c:if test="${queryType == 2 && isAllowMultiRebateObject == 1 && order.order_state != 7}">
					 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<a href="${ctx}/order/newAirticketRebate/airticketRebatesList?flowType=9&productType=7&orderId=${order.id}"  target="_blank">宣传费</a>
				</c:when>
		         <c:otherwise>
		         		<a href="${ctx}/order/newAirticketRebate/airticketRebatesList?flowType=9&productType=7&orderId=${order.id}"  target="_blank">返佣</a>
		          </c:otherwise>
				 </c:choose>   
				</c:if>
				<a onclick="expand('#child${order.id}',this)">展开</a>
			</p>
		</dd>
	</dl>
</td>
<td class="p0">
	<dl class="handle">
		<dt>
			<img src="${ctxStatic}/images/handle_xz_rebuild.png" title="下载">
		</dt>
		<dd>
			<p>
				<span></span> 
				<c:if test="${order.order_state != 7 }">
					<a onclick="" href="${ctx}/order/manage/airticketOrderTravelExport?orderId=${order.id}">游客资料</a>
					<a target="" href="${ctx}/order/manage/airticketOrderNameList?orderId=${order.id}">出票名单</a>
					<c:if test="${queryType == 1}">
						<shiro:hasPermission name="airticketOrderSale:download:confirmation">
							<c:if test="${not empty order.confirmationFileId}">
								<a href="javascript:void(0)"  onClick="downloadConfirm('${order.id}','/order/manage/downloadConfirmFiles')">确认单</a>
							</c:if>
						</shiro:hasPermission>
					</c:if>
					<c:if test="${queryType == 2}">
						<shiro:hasPermission name="airticketOrderOprt:download:confirmation">
							<c:if test="${not empty order.confirmationFileId}">
								<a href="javascript:void(0)"  onClick="downloadConfirm('${order.id}','/order/manage/downloadConfirmFiles')">确认单</a>
							</c:if>
						</shiro:hasPermission>
					</c:if>
				</c:if>
			</p>
		</dd>
	</dl>
</td>
<c:if test="${queryType eq 1}">
	<td class="p0">
		<dl class="handle">
			<dt>
				<img src="${ctxStatic}/images/handle_fk_rebuild.png" title="财务">
				
			</dt>
			<dd>
				<p <c:if test="${order.orderType == 2 }">style="display: none;" </c:if> >
					<span></span>
					<c:if test="${fn:length(order.orderPayList)>0 && order.order_state != 7}">
						<a href="javascript:void(0)" onClick="javascript:showOrderPay(${order.id},this);" class="jtk">收款记录</a>
					</c:if>
					<!-- 付定金、付全款、付尾款 -->
					<c:if test="${order.order_state!=99&&order.order_state!=111&&order.order_state!=89&&order.activityLockStatus != 1&&order.lockStatus != 1 && order.order_state != 7}">
						<c:if test="${(empty order.isDJPayed || order.isDJPayed == 0) && (empty order.isQKPayed || order.isQKPayed == 0)}">
							<a href="javascript:void(0)" onClick="orderPay('1', '${order.id}', '${order.orderNo }', '7', '${order.agentId}','${order.totalMoney }','${ctx}/order/manage/airticketOrderDetail?orderId=${order.id}','${order.main_order_id }');">收全款</a>
							<a href="javascript:void(0)" onClick="orderPay('3', '${order.id}', '${order.orderNo }', '7', '${order.agentId}','${order.totalMoney }','${ctx}/order/manage/airticketOrderDetail?orderId=${order.id}','${order.main_order_id }');">收订金</a>
							<c:set var="shiroOption" value="trues"></c:set>
						</c:if>
						<c:if test="${(not empty order.isDJPayed && order.isDJPayed == 1) || (not empty order.isQKPayed && order.isQKPayed == 1)}">
							<a href="javascript:void(0)" onClick="orderPay('2', '${order.id}', '${order.orderNo }', '7', '${order.agentId}','${order.totalMoney }','${ctx}/order/manage/airticketOrderDetail?orderId=${order.id}','${order.main_order_id }');">收尾款</a>
							<c:set var="shiroOption" value="trues"></c:set>
						</c:if>
					</c:if>
					<!-- 关联发票  0444需求-->
					<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
						<c:if test="${order.applyInvoiceWay eq 1 }">
						<a href="javascript:relationInvoiceList('${order.id}')" class="jtk">关联发票</a>
						</c:if>
					</c:if>
					<!-- 发票明细 -->
					<c:if test="${order.order_state != 7 }">
						<a  href="javascript:showInvoiceInfo('${order.id}','${order.orderNo}','${order.airticketId}')">发票明细</a><br>
						<a  href="javascript:showReceptInfo('${order.id}','${order.orderNo}','${order.airticketId}')">收据明细</a>
					</c:if>
					<shiro:hasPermission name="airticketOrderForSale:list:costpayl">
					<c:if test="${order.airticketId != null }">
							<a href="${ctx }/cost/manager/settleList/${order.airticketId}/7" target="_blank">结算单</a>
					</c:if></shiro:hasPermission>
				</p>
			</dd>
		</dl>
	</td>
</c:if>