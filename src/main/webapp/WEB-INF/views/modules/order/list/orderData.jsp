<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<td>

	<span class="sqcq-fj">
		<input type="checkbox" class="tdCheckBox" value="${orders.orderNum}@<c:if test="${not empty orders.confirmationFileId}">${orders.id}</c:if>" name="productOrderId" onclick="productOrderCheckchg()" />
		${(page.pageNo-1)*page.pageSize + s.count}
	</span>
</td>

<td>
	<div class="ycq">
		<!-- 20151102 C322 针对大洋需求，非签约渠道改为非签、签约渠道改为已签、按月结算改为月结 -->
		<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
		<c:choose>
			<c:when test="${orders.orderCompany == '-1'}">
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
					<c:choose>
					   <c:when test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}"> 
					       直客
					   </c:when>
					   <c:otherwise>
					       非签约渠道
					   </c:otherwise>
					</c:choose> 
				</c:if>
				<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
					未签
				</c:if>
			</c:when>
			<c:when test="${orders.orderCompany ne '-1' and fns:getAgentById(orders.orderCompany).isQuauqAgent eq '1' }">
				实时连通渠道
			</c:when>
			<c:otherwise>
				<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
					签约渠道
				</c:if>
				<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
					已签
				</c:if>
			</c:otherwise>
		</c:choose>
	</div>
	<c:if test="${not empty orders.paymentType && orders.paymentType != 1 && orders.paymentType != 0}">
		<div class="ycq yj" style="margin-top:1px;">
			<c:choose>
				<c:when test="${orders.paymentType == 2 }">
					<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
						按月结算
					</c:if>
					<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
						月结
					</c:if>
				</c:when>
				<c:when test="${orders.paymentType == 3 }">担保结算</c:when>
				<c:when test="${orders.paymentType == 4 }">后付费</c:when>
			</c:choose>
		</div>
	</c:if>
	${orders.orderCompanyName }
</td>


<!-- 200 针对优加，订单列表加入"渠道联系人" -->
<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
	<td>
		<c:set var="agentContactsName" value="${orders.orderPersonName}"></c:set>
		<span class="agentContactsNameSpan">${fn:substring(agentContactsName,1,agentContactsName.length()-1)}</span>
		<input class="agentContactsName" value="${fn:substring(agentContactsName,1,agentContactsName.length()-1)}" style="display: none"/>
	</td>
</c:if>

	
		<td class="que_parent">
			<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
				<c:if test="${orders.seizedConfirmationStatus eq 1 && orders.payStatus != 99 && order.order_state != 111 }">
					<span class="confirmed_occupied">已确认</span>
				</c:if>
			</shiro:hasPermission>
		${orders.orderNum }<c:if test="${orders.confirmFlag }"><i class="que"></i></c:if>
		<c:choose>
			<c:when test="${companyUuid eq '58a27feeab3944378b266aff05b627d2' or isOpManager}">
				<c:if test="${isNeedNoticeOrder == 1 && orders.seenFlag == '0'}">
					<span class="new-tips" style="inline-block;"></span>
				</c:if>
			</c:when>
			<c:otherwise>
				<c:if test="${isNeedNoticeOrder == 1 && orders.seenFlag == '0' && fns:getUser().id == orders.activityCreateBy}">
					<span class="new-tips" style="inline-block;"></span>
				</c:if>
			</c:otherwise>
		</c:choose>
	</td>
<td>
	<div class="tuanhao_cen onshow">
		<span style="word-break:break-all; display:block; word-wrap:break-word;">${orders.groupCode}</span>
	</div>
	<div class="chanpin_cen qtip" title="${orders.acitivityName}">
		<c:set var="isOp" value="0"></c:set>
		<c:if test="${orders.priceType eq 2}">
			<c:set var="isOp" value="1"></c:set>
		</c:if>
		<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${orders.activityId}?isOp=${isOp}')">${orders.acitivityName}</a>
	</div>
	<c:choose>
		<c:when test="${orders.intermodalType == 1}">
			<span class="lianyun_name">全国联运</span>
		</c:when>
		<c:when test="${orders.intermodalType == 2}">
			<span class="lianyun_name">分区联运</span>
		</c:when>
		<c:otherwise>
			<span class="lianyun_name">无联运</span>
		</c:otherwise>
	</c:choose>
</td>
<td>${orders.activityCreateUserName}</td>
<td>
	<div class="salerId_cen onshow">
		${orders.salerName}
	</div>
	<div class="createBy_cen qtip">
		${orders.carateUserName}
	</div>
</td>
<c:choose>
	<c:when test="${showType ne 99}">
		<td class="p0">
			<div class="out-date"><fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/></div>
			<div class="close-date">
				<c:choose>
					<c:when test="${orders.seizedConfirmationStatus eq 1 }">无</c:when>
					<c:otherwise>${orders.leftDays}</c:otherwise>
				</c:choose>
			</div>
		</td>
	</c:when>
	<c:otherwise>
		<td class="tc">
			<fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
		</td>
	</c:otherwise>
</c:choose>
<td class="p0">
	<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
	<div class="close-date"><fmt:formatDate value="${orders.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
</td>
<td class="tr">
	${orders.orderPersonNum }
</td>
<td class="tc" title="${flx:getStrFromVal(orders.traName) }">
	${flx:getStrFromOne2two(orders.traName) }
</td>
<!-- 20151102 C322 针对大洋需求，屏蔽发票和收据明细 -->
<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
	<td class="tc">
		${fns:getOrderInvoiceReceiptStatus(1,orderStatus,orders.id) }<br/>
		${fns:getOrderInvoiceReceiptStatus(2,orderStatus,orders.id) }
	</td>
</c:if>
<td class="tc" style="position:relative;">
	<c:choose>
		<%--散拼优惠中待生成订单和未生成订单状态--%>
		<c:when test="${orders.delFlag eq '3'}">待生成订单</c:when>
		<c:when test="${orders.delFlag eq '4'}">未生成订单</c:when>
		<c:otherwise>${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}<c:if test="${orders.paymentStatus == 0 && (orders.payStatus==1 or orders.payStatus == 2)}"><br/>(未占位)</c:if></c:otherwise>
	</c:choose>
	<c:if test="${orders.group_code ne null and orders.order_no ne null}">
		<div class="transGroup" style="cursor:pointer;display: inline-block;width: 20px;height: 20px;position: absolute;top: 0;right: 0;border-left: 1px solid #d9d9d9;border-bottom: 1px solid #d9d9d9;">转</div>
	</c:if>
	<div style="display: none;position: absolute;z-index: 10;border: 1px solid #d9d9d9;background-color: white;top: 0;left: 100%;width: 180px;text-align: left;padding: 8px;">
		<span>转出团团号：${orders.group_code }</span><br/>
		<span>转出订单号：${orders.order_no }</span>
	</div>
</td>

<td class="tr pr" name="orderRebates">
	<input type="hidden" name="orderNumForRebate" value="${orders.orderNum}">
	<span class="tdorange fbold">
		<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">
			<span>${orders.totalMoney}</span>
		</c:if>
		<%-- C360 大唐国旅 显示未收余额 --%>
		<c:if test="${companyUuid eq '7a45838277a811e5bc1e000c29cf2586' }">		
			<div class="total_cen onshow">
				<span>${orders.totalMoney}</span>
			</div>
			<div class="remainder_cen qtip">
				${orders.remainderMoney}
			</div>
		</c:if>
		<c:if test="${not empty orders.differenceFlag and orders.differenceFlag eq '1'}">
			<span style="display: inline-block;color:#999">（含差额返还：${orders.differenceMoney}）</span>
		</c:if>
	</span>
</td>

<c:if test="${showType==99}"><td>${orders.cancelDescription}</td></c:if>
<c:if test="${showType!=99}">
	<td class="p0 tr">	
		<c:if test="${not empty orders.promptStr}">
			<div class="notice_price"><span>${orders.promptStr }</span></div>
		</c:if>
		<div class="yfje_dd">	
			<span class="fbold">${orders.payedMoney}</span>
		</div>
		<div class="dzje_dd">
			<span class="fbold">${orders.accountedMoney}</span>
		</div>
	</td>
</c:if>
