<%@ page contentType="text/html;charset=UTF-8" %>
<tr id="child${s.count}" style="display:none" class="activity_team_top1">
    <td class="team_top" style="background-color:#d1e5f5;" name="colspanCount">
        <div style="width:100%;" class="divScroll">
            <table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;table-layout:fixed;"
                   name="colspanTable">
                <thead>
                <tr name="colspanTr">
                    <th class="tc" width="98px">团号</th>
                    <th class="tc" width="78px">出/截团日期</th>
                    <!-- 284 散拼报名 产品列表 针对越柬行踪屏蔽 -->
                    <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
                        <th class="tc" width="78px">资料截止日期</th>
                    </c:if>

                    <shiro:hasPermission name="price:project">
                        <th class="tc" width="118px">酒店房型</th>
                    </shiro:hasPermission>
                    <th class="tc" width="178px" class="t-th2">同行价
                                <%--518 特殊人群备注 S--%>
                            <c:if test="${not empty activity.specialRemark}">
                                <dl class="handle special-info">
                                    <dt>
                                        <i class="fa fa-info-circle" aria-hidden="true"></i>
                                    </dt>
                                    <dd class="">
                                        <p>
                                            <span></span>
                                            特殊人群备注：
                                                ${activity.specialRemark}
                                        </p>
                                    </dd>
                                </dl>
                            </c:if>
                                <%--518 特殊人群备注 E--%>
                    </th>
                    <!--远程度假暂时隐藏直客价C381 -->     <!-- 284 散拼报名 产品列表 针对越柬行踪屏蔽直客价 -->
                    <c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
                        <th class="tc" width="178px" class="t-th2">直客价</th>
                    </c:if>
                    <%-- <c:if test="${user.quauqBookOrderPermission eq '1' and activity.isT1 eq '1' and officeShelfRightsStatus ne '0'}">
                        <th class="tc" width="178px" class="t-th2">供应价</th>
                    </c:if> --%>
                    <th class="tc" width="80px">订金</th>
                    <th class="tc" width="80px">单房差</th>
                    <th class="tc" width="60px">预收</th>
                    <th class="tc" width="60px">
                        <div class="qiwei-liebiaot">已切位</div>
                        <div class="scqiwei-liebiaot">售出切位</div>
                    </th>
                    <shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
						<th width="60px">已确认人数</th>
                        <th width="60px">已占位人数</th>
					</shiro:hasPermission>
                    <th class="tc" width="60px">余位</th>
                    <th class="tc" style="display:none" width="60px">切位</th>
                    <th class="tc" width="90px">操作</th>
                </tr>

                </thead>
                <c:forEach items="${activity.activityGroups}" var="group" varStatus="s2">
                    <tbody>
                    <tr name="proGroupMainTr">
                        <td class="word-break-all group-num">
                            <input type="checkbox" name="groupid" value="${group.id}"
                                   <c:if test="${fn:contains(groupIds,fn:trim(group.id))}">checked="checked"</c:if>
                                   onchange="idcheckchg(this)"/>
							<input style="display: none" name="quauqAdultPrice" id="quauqAdultPrice" value="${group.quauqAdultPrice }">
							<input style="display: none" name="quauqChildPrice" id="quauqChildPrice" value="${group.quauqChildPrice }">
							<input style="display: none" name="quauqSpecialPrice" id="quauqSpecialPrice" value="${group.quauqSpecialPrice }">
                                ${group.groupCode}
                        </td>
                        <td class="p0" width="8%">
                            <div class="out-date"><fmt:formatDate pattern="yyyy-MM-dd"
                                                                  value="${group.groupOpenDate}"/></div>
                            <div class="close-date"><fmt:formatDate pattern="yyyy-MM-dd"
                                                                    value="${group.groupCloseDate }"/></div>
                        </td>
                        <!-- 284 散拼报名 产品列表 针对越柬行踪屏蔽 -->
                        <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
                            <td class="tc">
                                <span><fmt:formatDate pattern="yyyy-MM-dd" value="${group.visaDate }"/></span>
                            </td>
                        </c:if>

                        <!-- 299v2 酒店房型 -->
                        <shiro:hasPermission name="price:project">
                            <td class="tc hotelAndHouse">
                                <input type="hidden" name="groupHotel" value="${group.groupHotel}"/>
                                <input type="hidden" name="groupHouseType" value="${group.groupHouseType}"/>
                            </td>
                        </shiro:hasPermission>
                        <td>
                            <div class="price-list">
                                <span class="price-title">成人：</span>
                                <c:if test="${empty group.settlementAdultPrice}"><span
                                        class="price-content">—</span></c:if>
                                <c:if test="${not empty group.settlementAdultPrice}">
								<span class="price-content word-break-all">
									${fns:getCurrencyInfo(group.currencyType,0,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                          value="${group.settlementAdultPrice }"/></span>
								</span>
                                </c:if>
                            </div>

                            <div class="price-list">
                                <span class="price-title">儿童：</span>
                                <c:if test="${empty group.settlementcChildPrice}"><span
                                        class="price-content">—</span></c:if>
                                <c:if test="${not empty group.settlementcChildPrice}">
								<span class="price-content word-break-all">
									${fns:getCurrencyInfo(group.currencyType,1,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                          value="${group.settlementcChildPrice }"/></span>
								</span>
                                </c:if>
                            </div>

                            <div class="price-list">
                                <span class="price-title">特殊人群：</span>
                                <c:if test="${empty group.settlementSpecialPrice}"><span
                                        class="price-content">—</span></c:if>
                                <c:if test="${not empty group.settlementSpecialPrice}">
								<span class="price-content word-break-all">
									${fns:getCurrencyInfo(group.currencyType,2,'mark')}
									<span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                          value="${group.settlementSpecialPrice }"/></span>
								</span>
                                </c:if>
                            </div>

                        </td>
                        <!-- C323大洋屏蔽 --><!--远程度假暂时隐藏直客价C381 -->  <!-- 284 散拼报名 产品列表 针对越柬行踪屏蔽直客价 -->
                        <c:if test="${companyUuid ne '7a81a03577a811e5bc1e000c29cf2586' and companyUuid ne '7a81b63e77a811e5bc1e000c29cf2586' and companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
                            <c:if test="${fns:getUser().company.uuid  ne '7a81b21a77a811e5bc1e000c29cf2586' }">
                                <td>
                                    <div class="price-list">
                                        <span class="price-title">成人：</span>
                                        <c:if test="${empty group.suggestAdultPrice}"><span
                                                class="price-content">—</span></c:if>
                                        <c:if test="${not empty group.suggestAdultPrice}">
										<span class="price-content word-break-all">
											${fns:getCurrencyInfo(group.currencyType,3,'mark')}
											<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                   value="${group.suggestAdultPrice }"/></span>
										</span>
                                        </c:if>
                                    </div>

                                    <div class="price-list">
                                        <span class="price-title">儿童：</span>
                                        <c:if test="${empty group.suggestChildPrice}"><span
                                                class="price-content">—</span></c:if>
                                        <c:if test="${not empty group.suggestChildPrice}">
										<span class="price-content word-break-all">
											${fns:getCurrencyInfo(group.currencyType,4,'mark')}
											<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                   value="${group.suggestChildPrice }"/></span>
										</span>
                                        </c:if>
                                    </div>
                                    <div class="price-list">
                                        <span class="price-title">特殊人群：</span>
                                        <c:if test="${empty group.suggestSpecialPrice}"><span
                                                class="price-content">—</span></c:if>
                                        <c:if test="${not empty group.suggestSpecialPrice}">
										<span class="price-content word-break-all">
											${fns:getCurrencyInfo(group.currencyType,5,'mark')}
											<span class="tdblue"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                                   value="${group.suggestSpecialPrice }"/></span>
										</span>
                                        </c:if>
                                    </div>
                                </td>
                            </c:if>
                        </c:if>
                        <%-- <c:if test="${user.quauqBookOrderPermission eq '1' and activity.isT1 eq '1' and officeShelfRightsStatus ne '0'}">
                            <td>
                                <div class="price-list">
                                    <span class="price-title">成人：</span>
                                    <c:if test="${empty group.quauqAdultPrice or group.isT1 ne '1'}">—</c:if>
                                    <c:if test="${not empty group.quauqAdultPrice and group.isT1 eq '1'}">
                                        ${fns:getCurrencyInfo(group.currencyType,0,'mark')}
                                        <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                              value="${group.adultRetailPrice }"/></span>
                                        161009 BUG16200 将供应价中 hidden="hidden"替换成display:none 兼容ie
                                        <input style="display:none" name="quauqAdultPrice" id="quauqAdultPrice"
                                               value="${group.quauqAdultPrice }">
                                    </c:if>
                                </div>
                                <div class="price-list">
                                    <span class="price-title">儿童：</span>
                                    <c:if test="${empty group.quauqChildPrice or group.isT1 ne '1'}">—</c:if>
                                    <c:if test="${not empty group.quauqChildPrice and group.isT1 eq '1'}">
                                        ${fns:getCurrencyInfo(group.currencyType,1,'mark')}
                                        <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                              value="${group.childRetailPrice }"/></span>
                                        <input style="display:none" name="quauqChildPrice" id="quauqChildPrice"
                                               value="${group.quauqChildPrice }">
                                    </c:if>
                                </div>
                                <div class="price-list">
                                    <span class="price-title">特殊人群：</span>
                                    <c:if test="${empty group.quauqSpecialPrice or group.isT1 ne '1'}">—</c:if>
                                    <c:if test="${not empty group.quauqSpecialPrice and group.isT1 eq '1'}">
                                        ${fns:getCurrencyInfo(group.currencyType,2,'mark')}
                                        <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                              value="${group.specialRetailPrice }"/></span>
                                        <input style="display:none" name="quauqSpecialPrice" id="quauqSpecialPrice"
                                               value="${group.quauqSpecialPrice }">
                                    </c:if>
                                </div>
                            </td>
                        </c:if> --%>
                        <td class="tc">
                            <c:if test="${not empty group.payDeposit}">${fns:getCurrencyInfo(group.currencyType,99,'mark')}</c:if>
                            <span class="tdorange"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                     value="${group.payDeposit }"/></span>
                        </td>
                        <td class="tc">
                            <c:if test="${not empty group.singleDiff}">${fns:getCurrencyInfo(group.currencyType,100,'mark')}</c:if>
                            <span class="tdred"><fmt:formatNumber type="currency" pattern="#,##0.00"
                                                                  value="${group.singleDiff }"/></span>
                        </td>
                        <td class="tc">
                            <span>${group.planPosition }</span>
                        </td>
                        <td class="p0" width="7%">
                            <div class="out-date">${group.payReservePosition }</div>
                            <div class="close-date">${group.soldPayPosition }</div>
                        </td>
                        <shiro:hasPermission name="${orderTypeStr }Order:operation:customerConfirm">
                            <td class="tc">
                                    ${fns:getConfirmedNums(group.id)}
                            </td>
                            <td class="tc">
                                    ${group.nopayReservePosition}
                            </td>
                        </shiro:hasPermission>
                        <td class="tc">
                            <span>${group.freePosition}</span>
                        </td>
                        <td style="display:none;" class="soldPayPosition${group.id} tc" width="4%">
                            <span class="tdred">${group.payReservePosition }</span>
                        </td>
                        <td class="tc">
                            <%@ include file="/WEB-INF/views/modules/order/forOrder/payModeType.jsp" %>
                        </td>
                    </tr>
                    <!--299-散拼产品列表-start-->
                    <tr class="pricePlanContainer" style="display:none;">
                        <td name="colspanCount" colspan="">
                            <table name="pricePlanTable" id="pricePlanTable"
                                   class="table activitylist_bodyer_table border-table-spread"
                                   style="margin: 0 auto">
                                <thead>
                                <tr>
                                    <th rowspan="2" class="tc">序号</th>
                                    <th rowspan="2" class="tc" style="width: 700px">
                                        价格方案
                                    </th>
                                    <th colspan="3" class="tc t-th2">同行价</th>
                                    <th colspan="3" class="tc t-th2">直客价</th>
                                    <th rowspan="2" class="tc">备注</th>
                                </tr>
                                <tr>
                                    <th class="tc">成人</th>
                                    <th class="tc">儿童</th>
                                    <th class="tc">特殊人群</th>
                                    <th class="tc">成人</th>
                                    <th class="tc">儿童</th>
                                    <th class="tc">特殊人群</th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    <!--299-散拼产品列表-end-->
                    <%@ include file="/WEB-INF/views/modules/order/forOrder/salerDetail.jsp" %>

					<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
					
                    <!-- 285 越柬行踪 散拼报名 产品列表默认展开备注 -->
                    <!--<tr class="groupNoteCol"
                        <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">style="display: none;"</c:if>>
                        <td name="colspanCount" colspan="">备注:
                            <span class="groupNoteContent">${group.groupRemark}</span>
                        </td>
                    </tr>-->
                    <!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
                    <tr class="groupNoteCol"
                        <c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">style="display: none;"</c:if>>
                        <td name="colspanCount" colspan="">备注:
                            <span class="groupNoteContent">${group.groupRemark}</span>
                        </td>
                    </tr>
                    
                    <!-- 576需求-报名团期备注默认展开***end*** -->
                    
                    </tbody>
                </c:forEach>
            </table>
        </div>
    </td>
</tr>


