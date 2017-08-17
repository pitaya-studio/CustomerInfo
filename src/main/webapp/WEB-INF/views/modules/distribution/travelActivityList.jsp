<%@ page contentType="text/html;charset=UTF-8" %>
<table id="contentTable" class="table activitylist_bodyer_table mainTable">
	<thead>
		<tr>
			<th width="4%">序号</th>
            <th class="table_borderLeftN" width="4%">全选<input name="allChk" type="checkbox" onclick="checkallForActivity(this)"/>
            </th>
            <th width="14%">产品名称</th>
            <th width="8%">计调</th>
            <th width="8%">出发城市</th>
            <th width="6%">航空</th>
            <th width="8%">签证</th>
            <th width="16%">最近出团日期</th>
            <th width="10%">成人同行价</th>
            <th width="10%">成人直客价</th>
            <th width="4%">操作</th>
		</tr>
	</thead>
	<tbody>
		<c:if test="${fn:length(page.list) eq 0 }">
			<tr>
				<td colspan="11" class='tc'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
			</tr>
		</c:if>
		<c:if test="${fn:length(page.list) > 0 }">
			<c:forEach items="${page.list }" var="activity" varStatus="s">
				<c:set var="groupsize" value="${fn:length(activity.activityGroups)}"></c:set>
				<tr id="parent${s.count }">
					<%-- 序号 --%>
					<td class="tc">${s.count }</td>
					<%-- 全选 --%>
					<td class="table_borderLeftN">
						<input type="checkbox" name="activityId" value="${activity.id }" onclick="idcheckchgForActivity(this)"/><br/><br/>
                    </td>
                    <%-- 产品名称 --%>
                    <td class="activity_name_td">
                    	<a href="javascript:void(0)"
                    		onClick="javascript:window.open('${ctx}/activity/manager/detail/${activity.id}')">${activity.acitivityName}</a>
                	</td>
                	<%-- 计调 --%>
                	<td class="tc" title="电话：${activity.createBy.phone}">${activity.createBy.name}</td>
                	<%-- 出发城市 --%>
                	<td class="tc">${activity.fromAreaName}</td>
                	<%-- 航空 --%>
                	<td align="center">
                    	<label class="qtip" title="${activity.trafficNameDesc}">
                        	<c:set var="fligthInfoStr" value=""></c:set>
                        	<c:forEach items="${activity.activityAirTicket.flightInfos }" var="fligthInfo">
                            	<c:set var="fligthInfoStr"
                                	value="${fligthInfoStr }${fn:replace(fligthInfo.airlines,'-1','-')},"></c:set>
                        	</c:forEach>
                            ${fligthInfoStr }
                    	</label>
                	</td>
                	<%-- 签证 --%>
                	<td align="center">
                    	<c:if test="${!empty visaMapList}">
                        	<c:forEach items="${visaMapList}" var="visas">
                            	<c:if test="${activity.id eq visas.srcActivityId}">
                                	<a href="javascript:void(0)"
                                   		onClick="downloads('${visas.docInfoId}','${fns:getCountryName(visas.countryId)}',null,true)">${fns:getCountryName(visas.countryId)}</a>
                            	</c:if>
                        	</c:forEach>
                    	</c:if>
               		</td>
               		<%-- 最近出团日期 --%>
               		<td id="groupdate${activity.id }" align="center">
               			<c:if test="${groupsize ne 0 }">
               				<div id="truedate">
								<span>
									<c:choose>
                                    	<c:when test="${activity.groupOpenDate eq activity.groupCloseDate}">
                                    		<fmt:formatDate value="${activity.groupOpenDate }" pattern="yyyy-MM-dd"/>
                                    	</c:when>
                                    	<c:when test="${empty activity.groupCloseDate}">
                                    		<fmt:formatDate value="${activity.groupOpenDate }" pattern="yyyy-MM-dd"/>
                                    	</c:when>
                                    	<c:otherwise>
                                    		<fmt:formatDate value="${activity.groupOpenDate }" pattern="yyyy-MM-dd"/>
                                    		至
                                    		<fmt:formatDate value="${activity.groupCloseDate }" pattern="yyyy-MM-dd"/>
                                    	</c:otherwise>
                                	</c:choose>
								</span><br>
                        		<a id="close${s.count}" href="javascript:void(0)" class="team_a_click"
                           			onClick="expand('#child${s.count}',this);"
                           			onMouseenter="if($(this).html()=='团期列表'){$(this).html('展开团期列表')}"
                           			onMouseleave="if($(this).html()=='展开团期列表'){$(this).html('团期列表')}">团期列表</a>
                    		</div>
               			</c:if>
               			<c:if test="${groupsize eq 0 }">
               				<div id="falsedate">日期待定</div>
               			</c:if>
                	</td>
                	<%-- 成人同行价 --%>
                	<td id="settleadultprice${activity.id }" class="tr">
                		<c:if test="${activity.settlementAdultPrice==0}">价格待定</c:if>
                		<c:if test="${activity.settlementAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,0,'mark')}
                    		<span class="tdred fbold">
                    			<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.settlementAdultPrice}"/>
                    		</span>起
                    	</c:if>
                	</td>
                	<%-- 成人直客价 --%>
                	<td id="suggestadultprice${activity.id }" class="tr">
                		<c:if test="${activity.suggestAdultPrice==0}">价格待定</c:if>
                		<c:if test="${activity.suggestAdultPrice>0}">${fns:getCurrencyInfo(activity.currencyType,1,'mark')}
                    		<span class="tdblue fbold">
                    			<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activity.suggestAdultPrice}"/>
                    		</span>起
                    	</c:if>
                	</td>
                	<%-- 操作 --%>
                	<td>

						<dl class="handle">
							<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
							<dd style="left:45%;">
								<p>
									<span></span>
									<a href="javascript:void(0)" data-id="${groupIds[s.count-1]}" onclick="SingleWeCode($(this));">生成二维码</a>
								</p>
							</dd>
						</dl>

                		<%--<div class="relative wechat_hover">--%>
                        	<%--<img class="getweChat" data-id="${groupIds[s.count-1]}" src="${ctxStatic }/images/wechat/wechat_1.png"/>--%>
                            <%--<div class="absolute wechat_box">--%>
                            	<%--<em class="wechat_2"></em>--%>
	                            <%--<div class="pull-left wechat_left">--%>
	                            	<%--<img name="oneIdImgName" src="">--%>
	                            <%--</div>--%>
                                <%--<div class="pull-left wechat_right">--%>
                                	<%--<div class="wechat_top">--%>
                                    	<%--<div>扫描二维码</div>--%>
                                        <%--<div>分销至微信好友、朋友圈</div>--%>
                                    <%--</div>--%>
                                	<%--<div class="wechat_bottom">--%>
                                    	<%--<div>打开微信，进入“扫一扫”</div>--%>
                                        <%--<div>从“发现”进入“扫一扫”</div>--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                    	<%--</div>--%>
                	</td>
				</tr>
				<tr id="child${s.count }" style="display:none" class="activity_team_top1">
					<td colspan="11" class="team_top" style="background-color:#d1e5f5;">
						<div class="divScroll">
							<table id="teamTable" class="table activitylist_bodyer_table table-mod2-group" style="margin:0 auto;table-layout:fixed;">
								<thead>
									<tr>
										<th class="tc" width="98px">团号</th>
                                        <th class="tc" width="78px">出团日期</th>
                                        <th class="tc" width="78px">截团日期</th>
                                        <th class="tc" width="178px">同行价</th>
                                        <th class="tc" width="178px">直客价</th>
                                        <th class="tc" width="60px">预收</th>
                                        <th class="tc" width="60px">余位</th>
                                        <th class="tc" width="90px">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${activity.activityGroupList }" var="group" varStatus="g">
										<c:if test="${group.isT1 == 1 and group.suggestAdultPrice > 0}">
											<tr>
												<%-- 团号 --%>
												<td class="word-break-all group-num">
													<div class="withdraw-relative">	
														<em class="grounding-hover grounding-one">已上架旅游交易系统</em>
                                                    	<em class="g-w grounding"></em>
														<input type="checkbox" name="groupId" value="${group.id }" onchange="idcheckchgChild(this, ${s.count })" style="width: auto;"/>
														<span>${group.groupCode }</span>
													</div>
												</td>
												<%-- 出团日期 --%>
												<td class="tc">
													<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupOpenDate}"/>
												</td>
												<%-- 截团日期 --%>
												<td class="tc">
													<fmt:formatDate pattern="yyyy-MM-dd" value="${group.groupCloseDate}"/>
												</td>
												<%-- 同行价 --%>
												<td>
													<div class="price-list">
                                                        <span class="price-title">成人：</span>
                                                        <span class="price-content">
                                                        	<c:choose>
                                                        		<c:when test="${group.settlementAdultPrice > 0 }">
                                                        			<span class="rm">${fns:getCurrencyInfo(group.currencyType,0,'mark')}</span>
                                                        			<span class="tdred">
                                                        				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }"/>
                                                                    </span>
                                                        		</c:when>
                                                        		<c:otherwise>
                                                        			<span>—</span>
                                                        		</c:otherwise>
                                                        	</c:choose>
                                                        </span>
                                                    </div>
                                                    <div class="price-list">
                                                        <span class="price-title">儿童：</span>
                                                        <span class="price-content">
                                                        	<c:choose>
                                                        		<c:when test="${group.settlementcChildPrice > 0 }">
                                                        			<span class="rm">${fns:getCurrencyInfo(group.currencyType,1,'mark')}</span>
                                                        			<span class="tdred">
                                                        				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }"/>
                                                                    </span>
                                                        		</c:when>
                                                        		<c:otherwise>
                                                        			<span>—</span>
                                                        		</c:otherwise>
                                                        	</c:choose>
                                                        </span>
                                                    </div>
                                                    <div class="price-list">
                                                        <span class="price-title">特殊人群：</span>
                                                        <span class="price-content">
															<c:choose>
                                                        		<c:when test="${group.settlementSpecialPrice > 0 }">
                                                        			<span class="rm">${fns:getCurrencyInfo(group.currencyType,2,'mark')}</span>
                                                        			<span class="tdred">
                                                        				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }"/>
                                                                    </span>
                                                        		</c:when>
                                                        		<c:otherwise>
                                                        			<span>—</span>
                                                        		</c:otherwise>
                                                        	</c:choose>
														</span>
                                                    </div>
												</td>
												<%-- 直客价 --%>
												<td>
													<div class="price-list">
                                                        <span class="price-title">成人：</span>
                                                        <span class="price-content">
                                                        	<c:choose>
                                                        		<c:when test="${group.suggestAdultPrice > 0 }">
                                                        			<span class="rm">${fns:getCurrencyInfo(group.currencyType,3,'mark')}</span>
                                                        			<span class="tdblue">
                                                        				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestAdultPrice }"/>
                                                                    </span>
                                                        		</c:when>
                                                        		<c:otherwise>
                                                        			<span>—</span>
                                                        		</c:otherwise>
                                                        	</c:choose>
                                                        </span>
                                                    </div>
                                                    <div class="price-list">
                                                        <span class="price-title">儿童：</span>
                                                        <span class="price-content">
                                                        	<c:choose>
                                                        		<c:when test="${group.suggestChildPrice > 0 }">
                                                        			<span class="rm">${fns:getCurrencyInfo(group.currencyType,4,'mark')}</span>
                                                        			<span class="tdblue">
                                                        				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestChildPrice }"/>
                                                                    </span>
                                                        		</c:when>
                                                        		<c:otherwise>
                                                        			<span>—</span>
                                                        		</c:otherwise>
                                                        	</c:choose>
                                                        </span>
                                                    </div>
                                                    <div class="price-list">
                                                        <span class="price-title">特殊人群：</span>
                                                        <span class="price-content">
															<c:choose>
                                                        		<c:when test="${group.suggestSpecialPrice > 0 }">
                                                        			<span class="rm">${fns:getCurrencyInfo(group.currencyType,5,'mark')}</span>
                                                        			<span class="tdblue">
                                                        				<fmt:formatNumber type="currency" pattern="#,##0.00" value="${group.suggestSpecialPrice }"/>
                                                                    </span>
                                                        		</c:when>
                                                        		<c:otherwise>
                                                        			<span>—</span>
                                                        		</c:otherwise>
                                                        	</c:choose>
														</span>
                                                    </div>
												</td>
												<%-- 预收 --%>
												<td class="tc">
													<span class="tdorange">${group.planPosition }</span>
												</td>
												<%-- 余位 --%>
												<td class="tc">
													<span class="tdred">${group.freePosition }</span>
												</td>
												<%-- 操作 --%>
												<td>
													<%--微信二维码转发调整 ymx 2017/3/28 Start--%>
													<dl class="handle">
														<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
														<dd style="left:45%;">
															<p>
																<span></span>
																<a href="javascript:void(0)" data-id="${group.id }" onclick="SingleWeCode($(this));">生成二维码</a>
															</p>
														</dd>
													</dl>
													<%--<div class="relative wechat_hover">--%>
                        								<%--<img class="getweChat" data-id="${group.id}" src="${ctxStatic }/images/wechat/wechat_1.png"/>--%>
                            							<%--<div class="absolute wechat_box">--%>
                            								<%--<em class="wechat_2"></em>--%>
	                            							<%--<div class="pull-left wechat_left">--%>
	                            								<%--<img name="oneIdImgName" src="">--%>
	                            							<%--</div>--%>
                                							<%--<div class="pull-left wechat_right">--%>
                                								<%--<div class="wechat_top">--%>
                                    								<%--<div>扫描二维码</div>--%>
                                        							<%--<div>分销至微信好友、朋友圈</div>--%>
                                    							<%--</div>--%>
                                    							<%--<div class="wechat_bottom">--%>
                                    								<%--<div>打开微信，进入“扫一扫”</div>--%>
                                        							<%--<div>从“发现”进入“扫一扫”</div>--%>
                                    							<%--</div>--%>
                                							<%--</div>--%>
                            							<%--</div>--%>
                    								<%--</div>--%>
													<%--微信二维码转发调整 ymx 2017/3/28 End--%>
												</td>
											</tr>
										</c:if>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</td>
				</tr>
			</c:forEach>
		</c:if>
	</tbody>
</table>