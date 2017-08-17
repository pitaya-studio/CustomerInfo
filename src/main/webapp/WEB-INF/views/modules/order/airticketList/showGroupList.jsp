<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 团期标题 -->
<thead style="background:#403738;" id="orderOrGroup_group_thead">
	<tr>			
		<th width="20%">团号</th>
		<th width="20%">产品编号</th>
		<th width="10%">机票类型</th>
		<th width="20%">计调</th>
		<th width="10%">剩余/总人数</th>
		<th width="10%">操作</th>
		<th width="10%">下载</th>
	</tr>
</thead>

<!-- 团期查询列表显示 -->
<tbody id="orderOrGroup_group_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
			<td colspan="7" style="text-align: center;">
				暂无搜索结果
			</td>
		</tr>
	</c:if>
       
	<!-- 查询结果团期列表循环显示 -->
	<c:forEach items="${page.list }" var="groups" varStatus="s">
		<tr class="toptr">
			<td><span style="word-break:break-all; display:block; word-wrap:break-word;">${groups.groupCode}</span></td>
			<td class="que_parent">
				<a href="${ctx}/airTicket/actityAirTickettail/${groups.id}" target="_blank">${groups.productCode}</a>
				<c:if test="${groups.confirmFlag > 0 }"><i class="que"></i></c:if>
			</td>
			<td>
				<c:choose>
					<c:when test="${groups.airType eq 1}">多段</c:when>
					<c:when test="${groups.airType eq 2}">往返</c:when>
					<c:when test="${groups.airType eq 3}">单程</c:when>
				</c:choose>
			</td>
			<td>${fns:getUserById(groups.createBy).name}</td>
			<td class="p0">
				<div class="out-date">${groups.freePosition}</div>
				<div class="close-date">${groups.planPosition}</div>
			</td>
			<td class="p0">
				<dl class="handle" <c:if test="${(operator==3&&groups.roleType!=3) || (operator==4&&groups.roleType!=3&&groups.roleType!=4)}">style="display: none;"</c:if>>
					<%-- bug17566 ymx 2017/3/13 【UG-V2】订单模块部分页面按钮样式未改变 Start --%>
					<dt><img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png"></dt>
					<%-- bug17566 ymx 2017/3/13 【UG-V2】订单模块部分页面按钮样式未改变 End --%>
					<dd class="" style="left:45%;">
						<p>
							<span></span>
							<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/airTicket/actityAirTickettail/${groups.id}')">产品详情</a>
							<a onclick="expand('#order_${groups.id}',this,1080)">展开</a>
						</p>
					</dd>
				</dl>
			</td>
			
			<td class="p0">
				<dl class="handle" <c:if test="${(operator==3&&groups.roleType!=3) || (operator==4&&groups.roleType!=3&&groups.roleType!=4)}">style="display: none;"</c:if>>
					<%-- bug17566 ymx 2017/3/13 【UG-V2】订单模块部分页面按钮样式未改变 Start --%>
					<dt><img title="下载" src="${ctxStatic}/images/handle_xz_rebuild.png"></dt>
					<%-- bug17566 ymx 2017/3/13 【UG-V2】订单模块部分页面按钮样式未改变 End --%>
					<dd class="" style="left:45%;">
						<p>
							<span></span>
							<a href="javascript:void(0)"  onClick="exportExcel('${groups.id}', 1)">游客资料</a>
							<a href="javascript:void(0)"  onClick="exportExcel('${groups.id}', 2)">出票名单</a>
							<%-- <c:if test="${queryType == 2}">
								<a href="javascript:void(0)"  onClick="downloadData('${groups.id}', 'confirmation')">确认单</a>
							</c:if> --%>
						</p>
					</dd>
				</dl>
			</td>
		</tr>
		<tr name="subtr" class="activity_team_top1" id="order_${groups.id}" style="display: none">
			<td colspan="16" class="team_top" style="background-color:#dde7ef;">
				<table  class="table activitylist_bodyer_table" style="margin:0 auto;">
					<thead>
						<tr>
							<th width="6%" class="tc">序号</th>
							<th class="tc" width="8%">预定渠道</th>
							<!-- 200 针对优加，机票订单 团号列表加入"渠道联系人" -->
							<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
								<th class="tc" width="8%">渠道联系人</th>
							</c:if>
							<th class="tc" width="6%">订单号</th>
							<th class="tc" width="6%">参团类型</th>
							<th class="tc" width="8%">参团订单号<br> 参团团号</th>
							<th width="8%"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
							<th class="tc" width="8%">预订/剩余时间</th>
							<th class="tc" width="8%">出/截团日期</th>
							<th class="tc" width="6%">人数</th>
							<th class="tc" width="6%">游客</th>
							<c:if test="${queryType eq 1 && companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
								<th class="tc" width="5%">发票/收据</th>
							</c:if>
							<th class="tc" width="6%">订单状态</th>
							<th class="tr" width="6%">
								<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">订单总额</c:if>
								<%-- C360 大唐国旅 显示未收余额 --%>
								<c:if test="${companyUuid eq '7a45838277a811e5bc1e000c29cf2586' }"><span class="total on">订单总额</span>/<span class="remainder">未收余额</span></c:if>
							</th>
							<th class="tr" width="6%">已收金额<br> 到账金额 </th>
							<th class="tc" width="6%">操作</th>
							<th class="tc" width="6%">下载</th>
							<c:if test="${queryType eq 1}">
								<th class="tc" width="4%">财务</th>
							</c:if>
						</tr>
					</thead> 
					<!-- 如果订单团期编号等于正在循环团期ID，则显示，否则不显示 -->
					<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
					<c:set var="orderIndex" value="0"></c:set>
					<c:forEach items="${orders }" var="order">
						<c:if test="${order.airticketId eq groups.id}">
						<c:set var="orderIndex" value="${orderIndex+1}"></c:set>
							<tr name="${groups.id}">
								
								<c:choose>
									<c:when test="${companyUuid eq '58a27feeab3944378b266aff05b627d2'}">
										<c:if test="${isNeedNoticeOrder == 1}">
											<input type="hidden" name="seenFlag" value="${order.seenFlag}" id="order_${order.id}"/>
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if test="${isNeedNoticeOrder == 1 && (fns:getUser().id eq groups.createBy or isOpManager) && queryType eq 2}">
											<input type="hidden" name="seenFlag" value="${order.seenFlag}" id="order_${order.id}"/>
										</c:if>
									</c:otherwise>
								</c:choose>
								
								
								<td>
									<span class="sqcq-fj">
										<input type="checkbox" name="airticketOrderId_${groups.id}" class="tdCheckBox" value="${order.orderNo}@<c:if test="${not empty order.confirmationFileId}">${order.id}</c:if>" onclick="airticketOrderCheckchg(${groups.id})" />
										${orderIndex}
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
														非签
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
								</td>
								<!-- 200 针对优加，机票订单 团号列表加入"渠道联系人" -->
								<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
									<td>
										<span class="agentContactsNameSpan">${order.contactsName}</span>
										<input class="agentContactsName" value="${order.contactsName}" style="display: none"/>
									</td>
								</c:if>
								<td class="que_parent">
									<c:if test="${not empty order.orderNo }">
										<%-- 575 wangyang 2017.1.4 --%>
										<c:if test="${queryType eq 1 }"><c:set var="type" value="Sale"></c:set></c:if>
										<c:if test="${queryType eq 2 }"><c:set var="type" value="Op"></c:set></c:if>
										<shiro:hasPermission name="airticketOrderFor${type }:operation:customerConfirm">
											<c:if test="${order.seizedConfirmationStatus eq 1 && order.order_state != 99 && order.order_state != 111}">
												<span class="confirmed_occupied">已确认</span>
											</c:if>
										</shiro:hasPermission>
										${order.orderNo }
										<c:if test="${order.confirmFlag }"><i class="que"></i></c:if> 
									</c:if>
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
								<td>${order.personNum }</td>
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
											<span class="tdorange fbold">
												<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">${order.totalMoney}</c:if>
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
								<%@ include file="/WEB-INF/views/modules/order/airticketList/orderOperation.jsp"%>
                            </tr>
                            <!-- 支付记录 -->
							<%@ include file="/WEB-INF/views/modules/order/airticketList/orderPayList.jsp"%>
							<!-- 航班信息 -->
							<%@ include file="/WEB-INF/views/modules/order/airticketList/flightInfo.jsp"%>
						</c:if>
					</c:forEach>
					<tr class="checkalltd">
						<td colspan='19' class="t1">
							<label> <input type="checkbox" name="airticket_orderAllChk_${groups.id}" onclick="airticket_orderAllChecked(this, ${groups.id})" /> 全选 </label> 	
							<label> <input type="checkbox" name="airticket_orderAllChkNo_${groups.id}" onclick="airticket_orderAllNoCheck(${groups.id})" /> 反选 </label>
							<c:if test="${queryType == 1 }">   <!-- 销售机票订单 -->
								<shiro:hasPermission name="airticketOrderSale:download:confirmation">
									<input type="button" class="btn btn-primary" value="批量下载确认单" onclick="airticket_orderBatchDownload('airticketOrderId_' + ${groups.id})">
								</shiro:hasPermission>
								<shiro:lacksPermission name="airticketOrderSale:download:confirmation">
									<input type="button" class="btn btn-primary gray" value="批量下载确认单" disabled="disabled">
								</shiro:lacksPermission>
							</c:if>	
							<c:if test="${queryType == 2 }">    <!-- 计调机票订单 -->
								<shiro:hasPermission name="airticketOrderOprt:download:confirmation">
									<input type="button" class="btn btn-primary" value="批量下载确认单" onclick="airticket_orderBatchDownload('airticketOrderId_' + ${groups.id})">
								</shiro:hasPermission>
								<shiro:lacksPermission name="airticketOrderOprt:download:confirmation">
									<input type="button" class="btn btn-primary gray" value="批量下载确认单" disabled="disabled">
								</shiro:lacksPermission>
							</c:if>			
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</c:forEach>
</tbody>

