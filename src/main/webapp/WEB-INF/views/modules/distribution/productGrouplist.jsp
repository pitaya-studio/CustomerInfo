<%@ page contentType="text/html;charset=UTF-8" %>
<script>
	$(function(){
		//团号和产品切换
		switchNumAndPro();
	})
</script>
<form id="groupform" name="groupform" action="" method="post" style="width:100%;margin-bottom:20px;">
	<table id="contentTable_groupList" class="table activitylist_bodyer_table mainTable" style="table-layout: fixed; display: table;" name="colspanTable">
		<thead>
			<tr width="4%" name="colspanTr">
				<th width="50px">序号</th>
				<th width="90px">
					<span class="tuanhao on">团号</span>
						/
					<span class="chanpin">产品名称</span>
				</th>
				<th width="70px">计调</th>
				<th width="70px">出发城市</th>
				<th width="80px">出团日期</th>
				<th width="80px">截团日期</th>
				<th width="160px">同行价</th>
				<th width="160px">直客价</th>
				<th width="50px">预收</th>
				<th width="50px">余位</th>
				<th width="65px">操作</th>
			</tr>
        </thead>
        <tbody>
        	<c:if test="${fn:length(page.list) eq 0 }">
				<tr>
					<td colspan="11" class='tc' style="height: 40px;">经搜索暂无数据，请尝试改变搜索条件再次搜索</td>
				</tr>
			</c:if>
	        <c:forEach items="${page.list}" var="activityGroup" varStatus="s">
				<tr>
					<td>
						<div class="table_borderLeftN">
							<input type="checkbox" name="activityId" value="${activityGroup.id}" <c:if test="${fn:contains(activityIds,fn:trim(activityGroup.id))}">checked="checked"</c:if> onchange="idcheckchg(this)">
							<br><br>
						</div>
					</td>
					<td class="word-break-all">
						<div class="tuanhao_cen onshow">
							<input type="hidden" name="groupID" value="24442">
							<span style="word-break:break-all; display:block; word-wrap:break-word;">${activityGroup.groupCode}</span>
						</div>
						<div class="chanpin_cen qtip" title="${activityGroup.acitivityName}">
							<a href="javascript:void(0)" onclick="javascript:window.open('${ctx}/activity/manager/detail/${activityGroup.acitivityId}')">${activityGroup.acitivityName}</a>
						</div>
					</td>
					<td class="tc" title="电话：${activityGroup.opMobile}">
						 ${activityGroup.opName}
					</td>
					<td class="tc">${activityGroup.fromAreaName}</td>
					<td class="p0 tc" width="8%">
						${activityGroup.groupOpenDate}
					</td>
					<td class="p0 tc" width="8%">
						 ${activityGroup.groupCloseDate}
					</td>
					<td class="tc">
						<div class="price-list">
							<span class="price-title">成人：</span>
							<span class="price-content word-break-all">
							<c:choose>
								<c:when test="${activityGroup.settlementAdultPrice > 0 }">
									${fns:getCurrencyInfo(activityGroup.currencyType,0,'mark')}
									<span class="tdred"> 
										<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.settlementAdultPrice }" />
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
							<span class="price-content word-break-all">
								<c:choose>
									<c:when test="${activityGroup.settlementcChildPrice > 0 }">
										${fns:getCurrencyInfo(activityGroup.currencyType,1,'mark')}
										<span class="tdred">
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.settlementcChildPrice }" />
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
							<span class="price-content word-break-all">
								<c:choose>
									<c:when test="${activityGroup.settlementSpecialPrice > 0 }">
										${fns:getCurrencyInfo(activityGroup.currencyType,2,'mark')}
										<span class="tdred">
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.settlementSpecialPrice }" />
										</span>
									</c:when>
									<c:otherwise>
										<span>—</span>
									</c:otherwise>
								</c:choose>
							</span>
						</div>
					</td>
					<td class="tc">
						<div class="price-list">
							<span class="price-title">成人：</span>
							<span class="price-content word-break-all">
								<c:choose>
									<c:when test="${activityGroup.suggestAdultPrice > 0 }">
										${fns:getCurrencyInfo(activityGroup.currencyType,3,'mark')}
										<span class="tdblue">
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.suggestAdultPrice }" />
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
							<span class="price-content word-break-all">
								<c:choose>
									<c:when test="${activityGroup.suggestChildPrice > 0 }">
										${fns:getCurrencyInfo(activityGroup.currencyType,3,'mark')}
										<span class="tdblue">
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.suggestChildPrice }" />		
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
							<span class="price-content word-break-all">
								<c:choose>
									<c:when test="${activityGroup.suggestSpecialPrice > 0 }">
										${fns:getCurrencyInfo(activityGroup.currencyType,5,'mark')}
										<span class="tdblue">
											<fmt:formatNumber type="currency" pattern="#,##0.00" value="${activityGroup.suggestSpecialPrice }" />
										</span>
									</c:when>
									<c:otherwise>
										<span>—</span>
									</c:otherwise>
								</c:choose>
							</span>
						</div>
					</td>
					<td class="tc">
						${activityGroup.planPosition }
					</td>
					<td class="tc">
						${activityGroup.freePosition }
					</td>
					<td class="tc">
                        <%--微信二维码转发调整 ymx 2017/3/24 Start--%>
						<dl class="handle">
							<dt><img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"></dt>
							<dd style="left:45%;">
								<p>
									<span></span>
									<a href="javascript:void(0)" data-id="${activityGroup.id}" onclick="SingleWeCode($(this));">生成二维码</a>
								</p>
							</dd>
						</dl>
						<%--<div class="relative wechat_hover">--%>
							<%--<img class="getweChat" data-id="${activityGroup.id}" src="${ctxStatic}/images/wechat/wechat_1.png">--%>
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
                        <%--微信二维码转发调整 ymx 2017/3/24 End--%>
					</td>
				</tr>
			</c:forEach>
        </tbody>
    </table>
    <input type="hidden" id="tempUserName" value="testadmin1">
    <input type="hidden" id="isAllowSupplement" value="1">
</form>