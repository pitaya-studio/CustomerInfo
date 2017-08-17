<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<td>
	<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
	<span class="sqcq-fj">
		<input type="checkbox" class="tdCheckBox" value="${order.orderNo}@<c:if test="${not empty order.confirmationFileId}">${order.id}</c:if>" name="airticketOrderId" onclick="airticketOrderCheckchg()" />
		${(page.pageNo-1)*page.pageSize + s.count}
	</span>
</td>
<td>
	<c:if test="${not empty order.agentName }">
		<c:choose>
			<c:when test="${order.agentinfoId == '-1'}">
				<div class="ycq">
					<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
						<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
							直客
						</c:if>
						<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
							非签约渠道
						</c:if>
					</c:if>	
					<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
						未签
					</c:if>
				</div>
				${order.nagentName }
			</c:when>
			<c:otherwise>				
				<c:if test="${not empty order.paymentStatus }">
					<c:choose>
						<c:when test="${order.paymentStatus == 2 }">
							<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
								<div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 按月结算 </div>
							</c:if>
							<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
								<div class="ycq"> 已签  </div><div class="ycq yj" style="margin-top:1px;"> 月结 </div>
							</c:if>
						</c:when>
						<c:when test="${order.paymentStatus == 3 }">
							<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
								<div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 担保结算 </div>
							</c:if>
							<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
								<div class="ycq"> 已签  </div><div class="ycq yj" style="margin-top:1px;"> 担保结算 </div>
							</c:if>
						</c:when>
						<c:when test="${order.paymentStatus == 4 }">
							<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
								<div class="ycq"> 签约渠道 </div><div class="ycq yj" style="margin-top:1px;"> 后付费 </div>
							</c:if>
							<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
								<div class="ycq"> 已签 </div><div class="ycq yj" style="margin-top:1px;"> 后付费 </div>
							</c:if>
						</c:when>
						<c:otherwise>
							<div class="ycq">
								<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
									签约渠道
								</c:if>
								<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
									已签
								</c:if>
							</div>
						</c:otherwise>
					</c:choose>
				</c:if>				
			${order.agentName }
		</c:otherwise>
		</c:choose>		
	</c:if>
	<br>
</td>
<!-- 200 针对优加，订单列表加入"渠道联系人" -->
<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
	<td>
		<span class="agentContactsNameSpan">${order.contactsName}</span>
		<input class="agentContactsName" value="${order.contactsName}" style="display: none"/>
	</td>
</c:if>
<c:choose>
	<c:when test="${dayangCompanyUuid==companyUuid}">
	<!-- 该列隐藏掉 -->
	</c:when>
	<c:otherwise>
		<td class="que_parent">
			<%-- 575 wangyang 2017.1.4 --%>
			<c:if test="${queryType eq 1 }"><c:set var="type" value="Sale"></c:set></c:if>
			<c:if test="${queryType eq 2 }"><c:set var="type" value="Op"></c:set></c:if>
			<shiro:hasPermission name="airticketOrderFor${type }:operation:customerConfirm">
				<c:if test="${order.seizedConfirmationStatus eq 1 && order.order_state != 99 && order.order_state != 111}">
					<span class="confirmed_occupied">已确认</span>
				</c:if>
			</shiro:hasPermission>
			<c:if test="${not empty order.orderNo }">${order.orderNo }</c:if>
			<c:if test="${order.confirmFlag }"><i class="que"></i></c:if>
			<c:choose>
				<c:when test="${companyUuid == '58a27feeab3944378b266aff05b627d2'}">
					<c:if test="${isNeedNoticeOrder == 1 && order.seenFlag == '0'}">
						<span class="new-tips" style="inline-block;"></span>
					</c:if>
				</c:when>
				<c:otherwise>
					<c:if test="${isNeedNoticeOrder == 1 && order.seenFlag == '0' && (fns:getUser().id == order.activityCreateBy or isOpManager) && queryType eq 2}">
						<span class="new-tips" style="inline-block;"></span>
					</c:if>
				</c:otherwise>
			</c:choose>			
		</td>
	</c:otherwise>
</c:choose>
<td>
	<div class="tuanhao_cen onshow"><span style="word-break:break-all; display:block; word-wrap:break-word;">${order.orderGroupCode}</span></div>
	<div class="chanpin_cen qtip">
		<a href="${ctx}/airTicket/actityAirTickettail/${order.airticketId}" target="_blank">${order.chanpName}</a>
	</div>
</td>
<td>
	<c:choose>
		<c:when test="${order.orderType == 1 }">单办</c:when>
		<c:when test="${order.orderType == 2 }">
			<c:if test="${order.orderStatus == 1 }">单团</c:if>
			<c:if test="${order.orderStatus == 2 }">散拼</c:if>
			<c:if test="${order.orderStatus == 3 }">游学</c:if>
			<c:if test="${order.orderStatus == 4 }">大客户</c:if>
			<c:if test="${order.orderStatus == 5 }">自由行</c:if>
		</c:when>
	</c:choose>
</td>
<td>
	<c:choose>
		<c:when test="${order.orderType == 1 }">
			<div style="color: #b6b6b6;text-align: center;">
				无此项
			</div>
		</c:when>
		<c:when test="${order.orderType == 2 }">
			<a href="${ctx}/orderCommon/manage/orderDetail/${order.joinOrderId}" target="_blank">${order.joinOrderNum}</a>
			</br>
			<a href="${ctx}/activity/manager/detail/${order.srcActivityId}" target="_blank">${order.joinGroupCode}</a>
		</c:when>
	</c:choose>
</td>
<td>
	<div class="salerId_cen onshow">
		${order.salerName}
	</div>
	<div class="createBy_cen qtip">
		${order.createUserName}
	</div>
</td>
<td class="p0">
	<div class="out-date">
		<fmt:formatDate value="${order.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
	</div>
	<div class="close-date">
		${order.leftDays }
	</div>
</td>
<td class="p0">
	<c:choose>
		<c:when test="${order.orderType == 1 }">
			<div style="color: #b6b6b6;text-align: center;">
				无此项
			</div>
		</c:when>
		<c:when test="${order.orderType == 2 }">
			<div class="out-date">
				<c:if test ="${not empty order.groupOpenDate }"><fmt:formatDate value="${order.groupOpenDate }" pattern="yyyy-MM-dd"/></c:if>
			</div>
			<div class="close-date">
				<c:if test ="${not empty order.groupCloseDate }"><fmt:formatDate value="${order.groupCloseDate }" pattern="yyyy-MM-dd"/></c:if>
			</div>
		</c:when>
	</c:choose>
</td>
<td>
	<c:choose>
		<c:when test="${order.airType==1 }">多段</c:when>
		<c:when test="${order.airType==2 }">往返</c:when>
		<c:when test="${order.airType==3 }">单程</c:when>
	</c:choose>
</td>
<td><c:if test="${not empty order.personNum }">${order.personNum }</c:if></td>
 <td class="tc" title="${flx:getStrFromVal(order.traName) }">
	${flx:getStrFromOne2two(order.traName) }
 </td>
<c:if test="${queryType eq 1 && companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
	<td>
		<c:choose>
			<c:when test="${order.orderType == 2}">
				<div style="color: #b6b6b6;text-align: center;">
					无此项
				</div>
			</c:when>
			<c:otherwise>
				${fns:getOrderInvoiceReceiptStatus(1,order.realOrderType,order.id) }<br/>
				${fns:getOrderInvoiceReceiptStatus(2,order.realOrderType,order.id) }
			</c:otherwise>
		</c:choose>
	</td>
</c:if>
<td style="position:relative;">
	${fns:getDictLabel(order.order_state,"order_pay_status" , "无")}
	<c:if test="${order.group_code ne null and order.order_no ne null}">
		<div class="transGroup" style="cursor:pointer;display: inline-block;width: 20px;height: 20px;position: absolute;top: 0;right: 0;border-left: 1px solid #d9d9d9;border-bottom: 1px solid #d9d9d9;">改</div>
	</c:if>
	<div style="display: none;position: absolute;z-index: 10;border: 1px solid #d9d9d9;background-color: white;top: 0;left: 100%;width: 180px;text-align: left;padding: 8px;">
		<span>改签团团号：${order.group_code }</span><br/>
		<span>改出订单号：${order.order_no }</span>
	</div>
</td>
<td class="tr pr">
	<c:choose>
		<c:when test="${order.orderType == 2}">
			<div style="color: #b6b6b6;text-align: center;">
				无此项
			</div>
		</c:when>
		<c:otherwise>
			<c:set var="isrb" value="false"></c:set>
			<c:forEach var="rebat" items="${order.airticketOrderRebatesList }">							
				<c:if test="${not empty rebat.prebt or not empty rebat.infbt }"><c:set var="isrb" value="true"></c:set></c:if>							
			</c:forEach>
			<c:if test="${isrb == true }">
				<span class="icon-rebate">
					<span>
						<c:forEach var="rebat" items="${order.airticketOrderRebatesList }">
							<c:if test="${not empty rebat.prebt }">预计返佣:${rebat.prebt }</br></c:if>
							<c:if test="${not empty rebat.infbt }">实际返佣:${rebat.infbt }</c:if>
						</c:forEach>
					</span>
				</span>
			</c:if>
			<!-- 总额与余额相互切换 -->
			<span class="tdorange fbold">
			<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">
				${order.totalMoney}
			</c:if>
			<%-- C360 大唐国旅 显示未收余额 --%>
			<c:if test="${companyUuid eq '7a45838277a811e5bc1e000c29cf2586' }">		
				<div class="total_cen onshow">
					${order.totalMoney}
				</div>
				<div class="remainder_cen qtip">
					${order.remainderMoney}
				</div>
			</c:if>
			</span>
		</c:otherwise>
	</c:choose>
</td>
<td class="p0 tr">
	<c:choose>
		<c:when test="${order.orderType == 2}">
			<div style="color: #b6b6b6;text-align: center;">
				无此项
			</div>
		</c:when>
		<c:otherwise>
			<div class="yfje_dd">
				<c:if test="${not empty order.orderPrompt}">
					<div class="notice_price"><span>${order.orderPrompt }</span></div>
				</c:if>	
				<span class="fbold">${order.payedMoney }</span>
			</div>
			<div class="dzje_dd">
				<span class="fbold">${order.accountedMoney }</span>
			</div>
		</c:otherwise>
	</c:choose>
</td>