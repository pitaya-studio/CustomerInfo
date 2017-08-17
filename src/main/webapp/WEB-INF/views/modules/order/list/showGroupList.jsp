<%@ page contentType="text/html;charset=UTF-8"%>
<!-- 团期标题 -->
<thead style="background:#403738;" id="orderOrGroup_group_thead">
	<tr>			
		<th width="20%">团号</th>
		<th width="20%">产品名称</th>
		<th width="17%">计调</th>
		<th width="20%">出/截团日期</th>
		<c:choose>
			<c:when test="${orderStatus == 10}"><th width="7%">剩余/总间数</th></c:when>
			<c:otherwise><th width="7%">剩余/总人数</th></c:otherwise>
		</c:choose>
		<th width="8%">操作</th>
		<th width="8%">下载</th>
	</tr>
</thead>

<!-- 团期查询列表显示 -->
<tbody id="orderOrGroup_group_tbody">
   	<!-- 无查询结果 --> 
	<c:if test="${fn:length(page.list) <= 0 }">
		<tr class="toptr" >
			<td colspan="${showType==89?7:7}" style="text-align: center;">
				暂无搜索结果
			</td>
		</tr>
	</c:if>
       
	<!-- 查询结果团期列表循环显示 -->
	<c:forEach items="${page.list }" var="groups" varStatus="s">
		<tr class="toptr">
			<td class="que_parent">
				<span style="word-break: break-all;display: block;word-wrap: break-word;">${groups.groupCode}</span>
				<c:if test="${groups.confirmFlag > 0 }"><i class="que"></i></c:if>
			</td>
			<td>
				<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${groups.activityId}?isOp=0')">${groups.acitivityName}</a>
			</td>
			<td>${groups.carateUserName}</td>
			<td class="p0">
				<div class="out-date"><fmt:formatDate value="${groups.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
				<div class="close-date"><fmt:formatDate value="${groups.groupCloseDate}" pattern="yyyy-MM-dd"/></div>
			</td>
			<td class="p0">
				<div class="out-date">${groups.freePosition}</div>
				<div class="close-date">${groups.planPosition}</div>
			</td>
			<td class="p0">
				<dl class="handle" <c:if test="${(operator==3&&groups.roleType!=3) || (operator==4&&groups.roleType!=3&&groups.roleType!=4)}">style="display: none;"</c:if>>
					<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
					<dd class="" style="left:45%;">
						<p>
							<span></span>
							<shiro:hasPermission name="${orderTypeStr}Order:operation:view">
								<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail/${groups.activityId}?isOp=0')">产品详情</a>
							</shiro:hasPermission>
							<%--<shiro:hasPermission name="${orderTypeStr}Order:upload:travelerInfo">
								--%><a onclick="expand('#order_${groups.id}',this,1080)">展开</a>
							<%--</shiro:hasPermission>
						--%></p>
					</dd>
				</dl>
			</td>
			
			<td class="p0">
				<dl class="handle" <c:if test="${(operator==3&&groups.roleType!=3) || (operator==4&&groups.roleType!=3&&groups.roleType!=4)}">style="display: none;"</c:if>>
					<dt><img title="下载" src="${ctxStatic}/images/handle_xz_rebuild.png"></dt>
					<dd class="" style="left:45%;">
						<p>
							<span></span>
							<shiro:hasPermission name="${orderTypeStr}Order:download:travelerInfo">
								<a href="javascript:void(0)"  onClick="exportExcel('${groups.id}', '${groups.groupCode}', '${orderStatus}')">游客资料</a>
							</shiro:hasPermission>
						
							<c:if test="${fns:getUser().company.uuid eq '980e4c74b7684136afd89df7f89b2bee'}">
								<shiro:hasPermission name="${orderTypeStr}Order:group:control">
									<a href="javascript:void(0)"  onClick="exportActivityGroupExcel('${groups.id}','${orderStatus}','${groups.groupCode}','${orderIds}')">团控单-骡子</a>
								</shiro:hasPermission>
							</c:if>
							
							<shiro:hasPermission name="${orderTypeStr}Order:download:group">
								<c:if test="${not empty groups.open_date_file}">
									<a href="javascript:void(0)"  onClick="downloadData('${groups.open_date_file}', 'group')">出团通知</a>
								</c:if>
							</shiro:hasPermission>
						</p>
					</dd>
				</dl>
			</td>
		</tr>
		<tr name="subtr" class="activity_team_top1" id="order_${groups.id}" style="display: none">
			<td colspan="${showType==89?10:9}" class="team_top" style="background-color:#dde7ef;">
				<table  class="table activitylist_bodyer_table" style="margin:0 auto;">
					<thead>
						<tr>
							<th width="4%" class="tc">序号</th>
							<!-- 200 针对优加，单团订单 团号列表加入"预订渠道"和"渠道联系人" -->
							<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
								<th width="9%">预定渠道</th>
								<th width="8%">渠道联系人</th>
							</c:if>
							<th width="8%" class="tc">订单号</th>
							<c:if test="${companyUuid ne '7a81c5d777a811e5bc1e000c29cf2586' and orderStatus eq '2'}">
								<th width="9%">预定渠道</th>
							</c:if>
							<th width="8%" class="tc"><span class="salerId on">销售</span>/<span class="createBy">下单人</span></th>
							<th width="10%" class="tc">预定时间</th>
							<th width="3%" class="tc">人数</th>
							<th width="8%" class="tc">游客</th>
							<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
								<th width="9%" class="tc">发票/收据</th>
							</c:if>
							<th width="8%" class="tc">订单状态</th>
							<th width="8%" class="tr">
								<c:if test="${companyUuid ne '7a45838277a811e5bc1e000c29cf2586' }">订单总额</c:if>
								<%-- C360 大唐国旅 显示未收余额 --%>
								<c:if test="${companyUuid eq '7a45838277a811e5bc1e000c29cf2586' }"><span class="total on">订单总额</span>/<span class="remainder">未收余额</span></c:if>
							</th>
							<c:choose>
								<c:when test="${showType==99}">
									<th width="7%" class="tc">取消原因</th>
								</c:when>
								<c:otherwise>
									<th width="9%" class="tr">已收金额<br/>到账金额</th>
								</c:otherwise>
							</c:choose>
							<th width="5%" class="tc">操作</th>
							<c:if test="${showType != 7}">
								<th width="5%" class="tc">下载</th>
								<th width="5%" class="tc">财务</th>
							</c:if>
						</tr>
					</thead>
					<!-- 如果订单团期编号等于正在循环团期ID，则显示，否则不显示 -->
					<c:set var="orderIndex" value="0"></c:set>
					<c:forEach items="${orders}" var="orders">
						<c:if test="${orders.productGroupId eq groups.id}">
							<c:set var="orderIndex" value="${orderIndex+1}"></c:set>
							<tr name="${groups.id}">
								<!--序号 -->
								<td>
									<span class="sqcq-fj">
										<input type="checkbox" class="tdCheckBox" value="${orders.orderNum}@<c:if test="${not empty orders.confirmationFileId}">${orders.id}</c:if>" name="productOrderId_${groups.id}" onclick="productOrderCheckchg(${groups.id})" />
										${orderIndex}
									</span>
									<c:choose>
										<c:when test="${companyUuid == '58a27feeab3944378b266aff05b627d2' or isOpManager}">
											<c:if test="${isNeedNoticeOrder == 1}">
												<input type="hidden" name="seenFlag" value="${orders.seenFlag}" id="order_${orders.id}"/>
											</c:if>
										</c:when>
										<c:otherwise>
											<c:if test="${isNeedNoticeOrder == 1 && fns:getUser().id eq groups.createBy}">
												<input type="hidden" name="seenFlag" value="${orders.seenFlag}" id="order_${orders.id}"/>
											</c:if>
										</c:otherwise>
									</c:choose>
								</td> 
								<!-- 200 针对优加，单团订单 团号列表加入"预订渠道"和"渠道联系人" -->
								<c:if test="${companyUuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
										<td>
											<div class="ycq">
												<!-- 20151102 C322 针对大洋需求，非签约渠道改为非签、签约渠道改为已签、按月结算改为月结 -->
												<c:choose>
													<c:when test="${orders.orderCompany == '-1'}">
														<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
															非签约渠道
														</c:if>
														<c:if test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">
															未签
														</c:if>
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
										<td>
											<c:set var="agentContactsName" value="${orders.orderPersonName}"></c:set>
											<span class="agentContactsNameSpan">${fn:substring(agentContactsName,1,agentContactsName.length()-1)}</span>
											<input class="agentContactsName" value="${fn:substring(agentContactsName,1,agentContactsName.length()-1)}" style="display: none"/>
										</td>
								</c:if>
								<td class="que_parent">
									<shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
										<c:if test="${orders.seizedConfirmationStatus eq 1 && orders.payStatus != 99 && order.order_state != 111}">
											<span class="confirmed_occupied">已确认</span>
										</c:if>
									</shiro:hasPermission>
									${orders.orderNum }<c:if test="${orders.confirmFlag }"><i class="que"></i></c:if>
								</td>
								<c:if test="${companyUuid ne '7a81c5d777a811e5bc1e000c29cf2586' and orderStatus eq '2' }">
									<td>
										<div class="ycq">
											<!-- 20151102 C322 针对大洋需求，非签约渠道改为非签、签约渠道改为已签、按月结算改为月结 -->
											<c:choose>
												<c:when test="${orders.orderCompany == '-1'}">
													<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
														非签约渠道
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
								</c:if>
								<td class="tc">
									<div class="salerId_cen onshow">
										${orders.salerName}
									</div>
									<div class="createBy_cen qtip">
										${orders.carateUserName}
									</div>
								</td>
								
		                   		<td class="tc">
			                      <fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
			                    </td>
								<td class="tc">
									${orders.orderPersonNum }
								</td>
								 <td class="tc" title="${flx:getStrFromVal(orders.traName) }">
			                     ${flx:getStrFromOne2two(orders.traName) }
			                    </td>
								<c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586'}">
									<td class="tc">
										${fns:getOrderInvoiceReceiptStatus(1,orderStatus,orders.id) }<br/>
										${fns:getOrderInvoiceReceiptStatus(2,orderStatus,orders.id) }
									</td>
								</c:if>
								<td class="tc" style="position:relative;">
									<c:choose>
										<%--散拼优惠中待生成订单和未生成订单状态--%>
										<c:when test="${orders.delFlag eq '4'}">待生成订单</c:when>
										<c:when test="${orders.delFlag eq '5'}">未生成订单</c:when>
										<c:otherwise>${fns:getDictLabel(orders.payStatus, "order_pay_status", "")}<c:if test="${orders.payStatus==1 or orders.payStatus == 2}"><br/>(未占位)</c:if></c:otherwise>
									</c:choose>
									<c:if test="${orders.group_code ne null and orders.order_no ne null}">
										<div class="transGroup" style="cursor:pointer;display: inline-block;width: 20px;height: 20px;position: absolute;top: 0;right: 0;border-left: 1px solid #d9d9d9;border-bottom: 1px solid #d9d9d9;">转</div>
									</c:if>
									<div style="display: none;position: absolute;z-index: 10;border: 1px solid #d9d9d9;background-color: white;top: 0;left: 100%;width: 180px;text-align: left;padding: 8px;">
										<span>转出团团号：${orders.group_code }</span><br/>
										<span>转出订单号：${orders.order_no }</span>
									</div>
								</td>
								<!-- mod start by jiangyang -->
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
								<!-- mod end   by jiangyang -->
								<c:if test="${showType==99 || showType==89}"><td class="tc">${orders.cancelDescription}</td></c:if>
								<c:if test="${showType!=99 && showType!=89}">
									<td class="p0 tr">	
										<div class="yfje_dd">
											<span class="fbold">${orders.payedMoney}</span>
										</div>
										<div class="dzje_dd">
											<span class="fbold">${orders.accountedMoney}</span>
										</div>
									</td>
								</c:if>
								
								<%@ include file="/WEB-INF/views/modules/order/list/orderOperation.jsp"%>
                            </tr>
                            <!-- 支付记录 -->
							<%@ include file="/WEB-INF/views/modules/order/list/orderPayList.jsp"%>
						</c:if>
					</c:forEach>
					<tr class="checkalltd">
						<td colspan='19' class="t1">
							<label> <input type="checkbox" name="product_orderAllChk_${groups.id}" onclick="product_orderAllChecked(this, ${groups.id})" />全选 </label> 
							<label> <input type="checkbox" name="product_orderAllChkNo_${groups.id}" onclick="product_orderAllNoCheck(${groups.id})" />反选 </label>
							<shiro:hasPermission name="${orderTypeStr}Order:download:confirmation">			
								<input type="button" class="btn btn-primary" value="批量下载确认单" onclick="product_orderBatchDownload('productOrderId_' + ${groups.id})">
							</shiro:hasPermission>
							<shiro:lacksPermission name="${orderTypeStr}Order:download:confirmation">
								<input type="button" class="btn btn-primary gray" value="批量下载确认单" disabled="disabled">
							</shiro:lacksPermission>				
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</c:forEach>
</tbody>

