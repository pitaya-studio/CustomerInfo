<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 操作：支付订单状态为7的时候代表是待确认订单，其余为正常订单 -->
<c:choose>
	<%--散拼优惠特有的操作--%>
	<c:when test="${orders.delFlag eq '4' or orders.delFlag eq '5'}">
		<td class="p0">
			<dl class="handle" >
				<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
				<dd class="">
					<p <c:if test="${operation==false && fns:getUser().id != orders.createBy}">style="display: none;"</c:if>>
						<span></span>
						<shiro:hasPermission name="${orderTypeStr}Order:operation:privilege:review">
							<a href="${ctx}/singlegroup/privilege/privilegeApplyList?orderId=${orders.id}" >优惠</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${orderTypeStr}Order:operation:order:generate">
							<a href="javascript:void(0)" onclick="generateOrderValidate(${orders.id});">生成订单</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${orderTypeStr}Order:operation:view">
							<a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id});">详情</a>
						</shiro:hasPermission>
						<c:if test="${not fns:hasPrivilegeProcessingReviews(orders.id)}">
							<shiro:hasPermission name="${orderTypeStr}Order:operation:edit">
								<a href="${ctx}/orderCommon/manage/getOderInfoById?productorderById=${orders.id }"  target="_blank">修改</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:operation:deleteByFlag">
									<a href="javascript:void(0)" onClick="deleteOrderByFlag(${orders.id});">删除</a>
							</shiro:hasPermission>
						</c:if>
					</p>
				</dd>
			</dl>
		</td>
		<td class="p0">
			<dl class="handle">
				<dt><img title="下载" src="${ctxStatic}/images/handle_xz_rebuild.png"></dt>
				<dd class="">
					<c:if test="${not empty orders.specialDemandFileIds}">
						<a href="javascript:void(0)"  onClick="downloadFiles4SepcialDemand('${orders.specialDemandFileIds}')">特殊需求</a>
					</c:if>
					<p <c:if test="${operation==false && fns:getUser().id != orders.createBy}">style="display: none;"</c:if>>
						<span></span>
						<c:if test="${orders.payStatus != 7}">
							<shiro:hasPermission name="${orderTypeStr}Order:download:travelerInfo">
								<a href="javascript:void(0)"  onClick="downloadData('${orders.id}', 'traveler')">游客资料</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:download:visa">
								<a target="_blank" href="${ctx}/orderCommon/manage/interviewNotice/${orders.id }">面签通知</a>
							</shiro:hasPermission>
							<%-- <shiro:hasPermission name="${orderTypeStr}Order:download:group">
								<c:if test="${not empty orders.open_date_file}">
									<a href="javascript:void(0)"  onClick="downloadData('${orders.open_date_file}', 'group')">出团通知</a>
								</c:if>
							</shiro:hasPermission> --%>
							<shiro:hasPermission name="${orderTypeStr}Order:download:group">
								<c:if test="${not empty orders.openNoticeFileId}">
									<a href="javascript:void(0)"  onClick="downloadData('${orders.openNoticeFileId}','group')">出团通知</a>
								</c:if>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:download:confirmation">
								<c:if test="${not empty orders.confirmationFileId}">
									<a href="javascript:void(0)"  onClick="downloadConfirm('${orders.id}','/orderCommon/manage/downloadConfirmFiles')">确认单</a>
								</c:if>
							</shiro:hasPermission>
						</c:if>
					</p>
				</dd>
			</dl>
		</td>
		<td class="p0">
			<dl class="handle" <c:if test="${(operator==3&&orders.roleType!=3) || (operator==4&&orders.roleType!=3&&orders.roleType!=4)}">style="display: none;"</c:if>>
				<dt><img title="财务" src="${ctxStatic}/images/handle_fk_rebuild.png"></dt>
				<dd class="">
				</dd>
			</dl>
		</td>
	</c:when>
	<c:when test="${showType == 7}">
		<td class="p0">
			<dl class="handle" <c:if test="${(operator==3&&orders.roleType!=3) || (operator==4&&orders.roleType!=3&&orders.roleType!=4)}">style="display: none;"</c:if>>
				<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
				<dd class="">
					<p <c:if test="${operation==false && fns:getUser().id != orders.createBy}">style="display: none;"</c:if>>
						<span></span>
						<shiro:hasPermission name="${orderTypeStr}Order:operation:view">
							<a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id});">详情 </a>
						</shiro:hasPermission>
		                <c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1 }">
		                	<shiro:hasPermission name="${orderTypeStr}Order:operation:confirm">
		                		<a href="javascript:void(0)" name="confirmOrder" orderId="${orders.id}">确认占位</a>
		                	</shiro:hasPermission>
		                </c:if>
		                <shiro:hasPermission name="${orderTypeStr}Order:operation:cancle">
							<a href="javascript:cancelOrder(${orders.id},${orderStatus});">取消</a>
						</shiro:hasPermission>
						<shiro:hasPermission name="${orderTypeStr}Order:operation:deleteByFlag">
							<c:if test="${orders.payStatus!=111 && orders.payStatus!=4 && orders.payStatus!=5 && !(orders.payMode == '8' && not empty orders.payedMoney) && orders.seizedConfirmationStatus ne 1}">
								<a href="javascript:void(0)" onClick="deleteOrderByFlag(${orders.id});">删除</a>
							</c:if>
						</shiro:hasPermission>        
					</p>
				</dd>
			</dl>
		</td>
	</c:when>
	<c:otherwise>
		<td class="p0">
			<dl class="handle" delSomeCancelOp="${orders.id}_${orders.payStatus}" <c:if test="${(operator==3&&orders.roleType!=3) || (operator==4&&orders.roleType!=3&&orders.roleType!=4)}">style="display: none;"</c:if>>
				<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
				<dd class="">
					<p <c:if test="${operation==false && fns:getUser().id != orders.createBy}">style="display: none;"</c:if>>
						<span></span>
						<shiro:hasPermission name="${orderTypeStr}Order:operation:view">
							<a href="javascript:void(0)" onClick="javascript:orderDetail(${orders.id});">详情</a>
						</shiro:hasPermission>
						<c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1 && orders.payStatus == 7}">
							<shiro:hasPermission name="${orderTypeStr}Order:operation:confirm">
								<a href="javascript:void(0)" name="confirmOrder" orderId="${orders.id}">确认占位</a>
							</shiro:hasPermission>
		                </c:if>
		                <c:if test="${fns:getUser().company.orderPayMode eq 1}">
		                	<c:if test="${orders.payMode == '8' && (orders.payStatus == 4 || orders.payStatus == 5)}">
								<shiro:hasPermission name="${orderTypeStr}Order:operation:cx">
									<a href="javascript:void(0)" onClick="revokeOrder(${orders.id})">撤销占位</a>
								</shiro:hasPermission>
			                </c:if>
		                </c:if>
		                <c:if test="${orders.payStatus !=99 && orders.payStatus !=111 && orders.settleLockStatus != 1 && orders.lockStatus != 1}">
			                <shiro:hasPermission name="${orderTypeStr}Order:operation:cancle">
								<c:if test="${(orders.payStatus==1||orders.payStatus==2||orders.payStatus==3||orders.payStatus==7||(orders.placeHolderType==1&&orders.payStatus==4)||(orders.proPayMode == '8' && empty orders.payedMoney)) && orders.lockStatus != 1 && fns:canCancel(orderStatus,orders.id) == '0' && orders.payStatus != 10 && orders.seizedConfirmationStatus ne 1}">
									<a href="javascript:cancelOrder(${orders.id},${orderStatus});">取消</a>
								</c:if>
							</shiro:hasPermission>
						</c:if>
						<!--99：已经取消订单 111：已删除订单 7：待确认订单 -->             
						<c:if test="${orders.payStatus !=99 && orders.payStatus !=111 && orders.payStatus != 7 && orders.settleLockStatus != 1 && orders.lockStatus != 1}">
							<shiro:hasPermission name="${orderTypeStr}Order:operation:edit">
								<a href="${ctx}/orderCommon/manage/getOderInfoById?productorderById=${orders.id }"  target="_blank">修改</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:operation:changeprice">
								<c:if test="${orders.payStatus!=99&&orders.payStatus!=111&&orders.payStatus!=89 && orders.settleLockStatus != 1 &&  orders.lockStatus != 1}">
									<a href="${ctx }/newChangePrice/list?orderId=${orders.id}&productType=${orderStatus}" target="_blank">改价</a>
								</c:if>
							</shiro:hasPermission>
							<!-- 返佣 -->
							<shiro:hasPermission name="${orderTypeStr}Order:operation:returnCommission">
								<c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1 }">
									<c:choose>
									<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
											<a href="${ctx }/rebatesNew/list?orderId=${orders.id}&orderType=${orderStatus}" target="_blank">宣传费</a>
									</c:when>
									<c:otherwise>
									<a href="${ctx }/rebatesNew/list?orderId=${orders.id}&orderType=${orderStatus}" target="_blank">返佣</a>
									</c:otherwise>
								</c:choose>	
								</c:if>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:operation:toapplyLeague">
								<!--申请退团：已删除与已取消不能退团-->
								<c:if test="${orders.payStatus!=99&&orders.payStatus!=111  && orders.settleLockStatus != 1 && orders.lockStatus != 1}">
									<a href="javascript:void(0)" onClick="newViewExitGroup('${orders.id}','${orderStatus}')">退团</a>
								</c:if>
							</shiro:hasPermission>
							<!-- 转团 -->
							<shiro:hasPermission name="${orderTypeStr}Order:operation:changeGroup">
								<c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1 }">
									<a href="${ctx }/newTransferGroup/list/${orders.id}" target="_blank">转团</a>
								</c:if>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:operation:transfersMoney">
								<a href="javascript:void(0)" onclick="transfersMoneyNew(${orders.id})" name="transferMoney" value="${orders.id}" style="display: none;">转款</a>
							</shiro:hasPermission>
							<c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1 }">
								<a href="${ctx}/singlegrouporder/lendmoney/borrowAmountList?flowType=19&productType=${orderStatus}&orderId=${orders.id}"  target="_blank">借款</a>
							</c:if>
						</c:if>
						<%--109需求，奢华和优加 已经生成的订单 散拼--%>
						<c:if test="${(not empty isForYouJia) and (isForYouJia eq true) and (not empty isLoose) and (isLoose eq true)}">
							<a href="${ctx}/singlegroup/privilege/privilegeApplyList?orderId=${orders.id}"  target="_blank">优惠</a>
						</c:if>
<%-- 						<c:if test="${orderStatus eq '2' }">
 							<a href="${ctx}/singlegroup/privilege/privilegeApplyList?orderId=${orders.id}"  target="_blank">优惠</a>
 						</c:if>--%>
						
						<c:if test="${orders.payStatus !=99 && orders.payStatus != 111 && orders.payStatus != 7}">
						
							<!-- 订单锁死或解锁 -->
							<shiro:hasPermission name="${orderTypeStr}Order:operation:lock">
							    <c:if test="${orders.settleLockStatus != 1 && orders.lockStatus == 1 }">
							        <a href="javascript:unLockOrder(${orders.id});">解锁</a>
							    </c:if>
							    <c:if test="${orders.settleLockStatus != 1 && (orders.lockStatus == 0 || orders.lockStatus == null) }">
							        <a href="javascript:lockOrder(${orders.id});">锁死</a>
							    </c:if>
							</shiro:hasPermission> 
							
							<!-- 上传游客资料 -->
							<shiro:hasPermission name="${orderTypeStr}Order:upload:travelerInfo">
								<a href="javascript:void(0)" onclick="uploadData('traveler', '${orders.id}');">上传资料</a>
							</shiro:hasPermission>
							
							 <!-- 上传出团通知 -->
							<shiro:hasPermission name="${orderTypeStr}Order:upload:openNotice">
								<input type="hidden" name="openNoticeFile" >
								<a href="javascript:void(0)" onclick="uploadConfirmFiles('openNoticeFile',this,'${orders.id}','/orderCommon/manage/uploadConfirmation');">上传出团通知</a>
							</shiro:hasPermission>
							
							<shiro:hasPermission name="${orderTypeStr}Order:upload:confirmation">
							 <!-- 上传确认单 -->
								<input type="hidden" name="confirmFiles" >
								<a href="javascript:void(0)" onclick="uploadConfirmFiles('confirmFiles',this,'${orders.id}','/orderCommon/manage/uploadConfirmation');">上传确认单</a>
							</shiro:hasPermission>
						</c:if>
						
						<!-- 客户确认。操纵已占位订单的客户确认状态 需求575 -->
						<c:if test="${(orders.payStatus == 3 || orders.payStatus == 4 || orders.payStatus == 5) and orders.seizedConfirmationStatus ne 1 }">
							<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
								<a href="javascript:void(0)" onclick="seizedConfirm('${orders.id}');">客户确认</a>
							</shiro:hasPermission>
						</c:if>
						
						<!-- 签证信息 -->
						<shiro:hasPermission name="${orderTypeStr}Order:operation:groupVisa">
							<c:if test="${orders.payStatus != 7}">
								<c:choose>
									<c:when test="${orderOrGroup == 'order'}">
										<a href="${ctx}/visa/order/searchxs?mainOrderId=${orders.id}&_m=417&_mc=580"  target="_blank">签证信息</a>
									</c:when>
									<c:otherwise>
										<a href="${ctx}/visa/order/searchqw?mainOrderId=${orders.id}&_m=417&_mc=575"  target="_blank">签证信息</a>
									</c:otherwise>
								</c:choose>
							</c:if>
						</shiro:hasPermission>
						<!--  111 订单删除  -->
						<c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1}">
							<shiro:hasPermission name="${orderTypeStr}Order:operation:deleteByFlag">
								<c:if test="${orders.payStatus!=111 && orders.payStatus!=4 && orders.payStatus!=5 && !(orders.payMode == '8' && not empty orders.payedMoney) && orders.seizedConfirmationStatus ne 1}">
									<a href="javascript:void(0)" onClick="deleteOrderByFlag(${orders.id});">删除</a>
								</c:if>
							</shiro:hasPermission>
						</c:if>
		                <c:if test="${orders.settleLockStatus != 1 && orders.lockStatus != 1 && orders.payStatus != 7}">
		                	<!-- 退款-->
							<c:if test="${not empty orders.accountedMoney&&orders.payStatus!=111&&orders.payStatus!=99&&orders.accountedMoney!='¥0.00'}">
								<shiro:hasPermission name="${orderTypeStr}Order:operation:refund">
									<%--<a href="javascript:viewGroupRefund('${orders.id}','${orderStatus}')">退款</a>--%>
									<a href="javascript:newViewGroupRefund('${orders.id}','${orderStatus}')">退款</a>
								</shiro:hasPermission>
							</c:if>
		                	
								<!-- 99：已经取消订单：支付方式为预占位和订金占位  已经有支付记录的订单不允许激活（驳回订单） -->
								<c:if test="${orders.lockStatus != 1 && empty orders.payedMoney}">
									<c:if test="${orders.payStatus==99}">
										<shiro:hasPermission name="${orderTypeStr}Order:operation:invokeOrder">
											<a href="javascript:void(0)" onClick="invokeOrder(${orders.id}, 'invoke')">激活</a>
										</shiro:hasPermission>
									</c:if>
									<c:if test="${(orders.payStatus==2 || orders.payStatus==3) and (not empty orders.leftDays and orders.leftDays != '无')}">
										<shiro:hasPermission name="${orderTypeStr}Order:operation:delayOrder">
											<a href="javascript:void(0)" onClick="invokeOrder(${orders.id}, 'delay')">延时</a>
										</shiro:hasPermission>
									</c:if>
								</c:if>
								
							
							
							<shiro:hasPermission name="${orderTypeStr}Order:saler:invoice">
								<!-- 0474需求 -->
								<c:choose>
								    <c:when test="${fns:getUser().company.isRemoveApplyInvoiceLimit eq 1 }">
								    	<c:if test="${ orders.settleLockStatus != 1}">
											<a href="javascript:applyInvoiceInfo('${orders.id}','${orders.orderNum }','${orderStatus }')" class="jtk">发票申请</a>
											<c:set var="shiroOption" value="trues"></c:set>
										</c:if>
								    </c:when>
								    <c:otherwise>
										<c:if test="${(fns:getUser().id == orders.createBy || fns:getUser().id == orders.salerId) && orders.settleLockStatus != 1}">
											<a href="javascript:applyInvoiceInfo('${orders.id}','${orders.orderNum }','${orderStatus }')" class="jtk">发票申请</a>
											<c:set var="shiroOption" value="trues"></c:set>
										</c:if>
								    </c:otherwise>
								</c:choose>
							</shiro:hasPermission>
							<!-- 20150709 新增  收据申请； 20151102 C322 针对大洋屏蔽发票申请  -->
							<c:if test="${(fns:getUser().id == orders.createBy || fns:getUser().id == orders.salerId) && orders.settleLockStatus != 1}">
								<a href="javascript:applyReceiptInfo('${orders.id}','${orders.orderNum }','${orderStatus }')" class="jtk">收据申请</a>
							</c:if>
		                </c:if>
					</p>
				</dd>
			</dl>
		</td>
		<td class="p0">
			<dl class="handle">
				<dt><img title="下载" src="${ctxStatic}/images/handle_xz_rebuild.png"></dt>
				<dd class="">
					<p <c:if test="${operation==false && fns:getUser().id != orders.createBy}">style="display: none;"</c:if>>
						<span></span>
						<c:if test="${not empty orders.specialDemandFileIds}">
							<a href="javascript:void(0)"  onClick="downloadFiles4SepcialDemand('${orders.specialDemandFileIds}')">特殊需求</a>
						</c:if>
						<c:if test="${orders.payStatus != 7}">
							<shiro:hasPermission name="${orderTypeStr}Order:download:travelerInfo">
								<a href="javascript:void(0)"  onClick="downloadData('${orders.id}', 'traveler')">游客资料</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:download:visa">
								<a target="_blank" href="${ctx}/orderCommon/manage/interviewNotice/${orders.id }">面签通知</a>
							</shiro:hasPermission>
							<%-- <shiro:hasPermission name="${orderTypeStr}Order:download:group">
								<c:if test="${not empty orders.open_date_file}">
									<a href="javascript:void(0)"  onClick="downloadData('${orders.open_date_file}', 'group')">出团通知</a>
								</c:if>
							</shiro:hasPermission> --%>
							<shiro:hasPermission name="${orderTypeStr}Order:download:group">
								<c:if test="${not empty orders.openNoticeFileId}">
									<a href="javascript:void(0)"  onClick="downloadData('${orders.openNoticeFileId}','group')">出团通知</a>
								</c:if>
							</shiro:hasPermission>
							<shiro:hasPermission name="${orderTypeStr}Order:download:confirmation">
								<c:if test="${not empty orders.confirmationFileId}">
									<a href="javascript:void(0)"  onClick="downloadConfirm('${orders.id}','/orderCommon/manage/downloadConfirmFiles')">确认单</a>
								</c:if>
							</shiro:hasPermission>
						</c:if>
					</p>
				</dd>
			</dl>
		</td>
		<td class="p0">
			<dl class="handle" <c:if test="${(operator==3&&orders.roleType!=3) || (operator==4&&orders.roleType!=3&&orders.roleType!=4)}">style="display: none;"</c:if>>
				<dt><img title="财务" src="${ctxStatic}/images/handle_fk_rebuild.png"></dt>
				<dd class="">
					<p <c:if test="${operation==false && fns:getUser().id != orders.createBy && orders.payStatus == 7}">style="display: none;"</c:if>>
						<span></span>
						<c:if test="${orders.payStatus != 7}">
							<shiro:hasPermission name="${orderTypeStr}Order:operation:view">
									<a href="javascript:void(0)" onClick="javascript:showOrderPay(${orders.id},${orderStatus},${orders.payStatus},this);" class="jtk">收款记录</a>
									<c:set var="shiroOption" value="trues"></c:set>
							</shiro:hasPermission>
							<c:if test="${orders.payStatus !=99 && orders.payStatus != 111 && orders.payStatus!=89 && orders.settleLockStatus != 1 && orders.lockStatus != 1}">
								<shiro:hasPermission name="${orderTypeStr}Order:operation:topay">
									<c:if test="${(orders.payStatus == 1 || orders.payStatus == 2 || orders.payStatus == 3) || (orders.payStatus == 8 && empty orders.payedMoney)}">
										<a href="javascript:void(0)" onClick="orderPay('1', '${orders.id}', '${orders.orderNum}', '${orderStatus}', '${orders.orderCompany}', '${orders.totalMoney}', '${ctx}/orderCommon/manage/orderDetail/${orders.id}');">收全款</a>
										 <a href="javascript:void(0)" onClick="orderPay('3', '${orders.id}', '${orders.orderNum}', '${orderStatus}', '${orders.orderCompany}', '${orders.totalMoney}', '${ctx}/orderCommon/manage/orderDetail/${orders.id}');">收订金</a>
										<c:set var="shiroOption" value="trues"></c:set>
									</c:if>
									 <c:if test="${(orders.payStatus == 4 || orders.payStatus == 5) || (orders.payStatus == 8 && not empty orders.payedMoney)}">
										<a href="javascript:void(0)" onClick="orderPay('2', '${orders.id}', '${orders.orderNum}', '${orderStatus}', '${orders.orderCompany}', '${orders.totalMoney}', '${ctx}/orderCommon/manage/orderDetail/${orders.id}');">收尾款</a>
										<c:set var="shiroOption" value="trues"></c:set>
									</c:if>
								</shiro:hasPermission>
							</c:if>
							
							
							<!-- 关联发票  0444需求-->
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${1 eq orders.applyInvoiceWay }">
								<a href="javascript:relationInvoiceList('${orders.id}')" class="jtk">关联发票</a>
								</c:if>
							</c:if>
							<!-- 发票明细 -->
							<shiro:hasPermission name="${orderTypeStr}Order:saler:invoice">
								<a href="javascript:viewInvoiceInfo('${orders.id}','${orders.orderNum }','${orderStatus }','${orders.productGroupId}')" class="jtk">发票明细</a>
								<c:set var="shiroOption" value="trues"></c:set>
							</shiro:hasPermission>
							<!-- 收据明细 -->
							<a href="javascript:viewReceiptInfo('${orders.id}','${orders.orderNum }','${orderStatus }','${orders.productGroupId}')" class="jtk">收据明细</a>
							<!-- 结算单 -->
							<shiro:hasPermission name="${orderTypeStr}Order:list:costpayl">
							<c:if test="${orders.productGroupId != null }">
							<a href="${ctx }/cost/manager/settleList/${orders.productGroupId}/${orderStatus}" target="_blank">结算单</a>
							</c:if>
							<c:if test="${orders.gruopId != null }">
							<a href="${ctx }/cost/manager/settleList/${orders.gruopId}/${orderStatus}" target="_blank">结算单</a>
							</c:if></shiro:hasPermission>
							<!-- 借款记录 -->
							<%-- <shiro:hasPermission name="${orderTypeStr}Order:operation:borrowingList">
								<a href="javascript:void(0)" onClick="viewBorrowingList('${orders.id}','${orderStatus}')">借款记录</a>
								<c:set var="shiroOption" value="trues"></c:set>
							</shiro:hasPermission> --%>
						</c:if>
					</p>
				</dd>
			</dl>
		</td>
	</c:otherwise>
</c:choose>