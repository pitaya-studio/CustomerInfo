<%@ page contentType="text/html;charset=UTF-8" %>
<c:if test="${activity.payMode_full=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="normalPayType"
		onClick='occupied(${group.id},${activity.id},3,${group.freePosition})' >
		付全款
	</a>
</c:if>
<c:if test="${activity.payMode_op=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="opPayType"
		onClick='occupied(${group.id},${activity.id},7,${group.freePosition})' >
		计调确认占位
	</a>
</c:if>
<c:if test="${activity.payMode_cw=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="cwPayType"
		onClick='occupied(${group.id},${activity.id},8,${group.freePosition})' >
		财务确认占位
	</a>
</c:if>
<c:if test="${activity.payMode_deposit=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="dingjin_PayType"
		onClick='occupied(${group.id},${activity.id},1,${group.freePosition})'>
		订金占位
	</a>
</c:if>
<c:if test="${activity.payMode_advance=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="yuzhan_PayType"
		onClick='occupied(${group.id},${activity.id},2,${group.freePosition})'>
		预占位
	</a>
</c:if>
<c:if test="${activity.payMode_data=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="data_PayType"
		onClick='occupied(${group.id},${activity.id},4,${group.freePosition})'>
		资料占位
	</a>
</c:if>
<c:if test="${activity.payMode_guarantee=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="guarantee_PayType"
		onClick='occupied(${group.id},${activity.id},5,${group.freePosition})'>
		担保占位
	</a>
</c:if>
<c:if test="${activity.payMode_express=='1'}">
	<a style="display:none;" href="javascript:void(0)" class="express_PayType"
		onClick='occupied(${group.id},${activity.id},6,${group.freePosition})'>
		确认单占位
	</a>
</c:if>

<input hidden="hidden" name="bookOrder" value="${group.freePosition},${group.payReservePosition},${group.soldPayPosition},${group.leftdays},${group.settlementAdultPrice},${group.settlementcChildPrice},${group.settlementSpecialPrice},${group.suggestAdultPrice},${group.suggestChildPrice},${group.suggestSpecialPrice},${activity.id},${group.id},${group.leftpayReservePosition},${group.groupcoverNum},${groupCoverFlag}"/>

<select style="display:none;">
	<c:if test="${activity.payMode_full=='1'}">
		<option value='3'>全款支付</option>
	</c:if>
	<c:if test="${activity.payMode_op=='1'}">
		<option value='7'>计调确认占位</option>
	</c:if>
	<c:if test="${activity.payMode_cw=='1'}">
		<option value='8'>财务确认占位</option>
	</c:if>
	<c:if test="${activity.payMode_deposit=='1'}">
		<option value='1'>订金占位</option>
	</c:if>
	<c:if test="${activity.payMode_advance=='1'}">
		<option value='2'>预占位</option>
	</c:if>
	<c:if test="${activity.payMode_data=='1'}">
		<option value='4'>资料占位</option>
	</c:if>
	<c:if test="${activity.payMode_guarantee=='1'}">
		<option value='5'>担保占位</option>
	</c:if>
	<c:if test="${activity.payMode_express=='1'}">
		<option value='6'>确认单占位</option>
	</c:if>
</select>
<c:choose>
	<c:when test="${productOrGroup == 'product'}">
		<%--<c:if test="${queryCommonOrderList=='1' && activityKind == 2}">--%>
			<%--<div class="kong"></div>--%>
			<%--<input class="btn btn-primary" value="已收明细"  onclick="javascript:showOrderPay(${group.id},this);" type="button"/>--%>
			<%--<div class="kong"></div>--%>
		<%--</c:if>--%>
		<!-- 518 价格表 S-->
			
		<shiro:hasPermission name="loose:book:priceTable">
			<c:if test="${group.isT1 == 1 and officeShelfRightsStatus eq 0}">
				<a style="display:block" onclick="priceListJbox(this,'${fns:getCurrencyInfo(group.currencyType,0,'mark')}','${fns:getCurrencyInfo(group.currencyType,1,'mark')}','${fns:getCurrencyInfo(group.currencyType,2,'mark')}');" href="javascript:void(0)"> 价格表 </a>
			</c:if>
		</shiro:hasPermission>
			
			
			
		<!-- 518 价格表 E-->
		<c:if test="${queryCommonOrderList=='1' && activityKind == 2}">
				<a style="display:block" onclick="javascript:showOrderPay(${group.id},this);" href="javascript:void(0)"> 已收明细 </a>
		</c:if>
		<!-- 285 越柬行踪 报名 产品列表默认展开备注 -->
		<!-- 518 将展开备注和展开价格方案放进更多操作 S-->

		<dl class="handle more-op-style">
			<dt style="width:65px;">
				<a class="more-op" href="javascript:void(0)"> 更多操作 <i class="fa fa-angle-down" aria-hidden="true"></i> </a>
			</dt>
			<dd class="">
				<p>
					<span></span>

					<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->
					
					<!--<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
						<a class="expandNotes" href="javascript:void(0)"> 收起备注 <em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em></a>
					</c:if>
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<a class="expandNotes" href="javascript:void(0)"> 展开备注 <em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em></a>
					</c:if>-->
					
					<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
					<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' || companyUuid eq 'cb19b61e52fb4b6ab422aedac38acdfa'}">
						<a class="expandNotes" href="javascript:void(0)"> 收起备注 <em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em></a>
					</c:if>
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">
						<a class="expandNotes" href="javascript:void(0)"> 展开备注 <em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em></a>
					</c:if>
					
					<!-- 576需求-报名团期备注默认展开***end*** -->

					<shiro:hasPermission name="price:project">
						<c:if test="${not empty group.priceJson}">
							<br/>
							<a href="javascript:void(0);" onclick="expandPriceJson(this, ${activityKind})" data='${group.priceJson}'>展开价格方案</a>
						</c:if>
					</shiro:hasPermission>
				</p>
			</dd>
		</dl>

	</c:when>
	<c:otherwise>
		<dl class="handle">
			<dt>
				<img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png"/>
			</dt>
			<dd class="">
				<p>
					<span></span>
					<a href="javascript:void(0)" onclick="groupForOrder(this);"></a>
					<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activity/manager/detail4Groups/${group.srcActivityId}/${group.id}?isOp=0')">详 情</a>
					<c:if test="${queryCommonOrderList=='1' && activityKind == 2}">
						<a href="javascript:void(0)" onclick="showOrderPay(${group.id},this);">已收明细</a>
					</c:if>
					<shiro:hasPermission name="looseActivity:downloadYw">
						<c:if test="${activityKind == 2}">
							<a href="javascript:void(0)" onclick="downloadYw(${group.id});">下载余位表</a>
						</c:if>
					</shiro:hasPermission>
					
					
					<!-- 576需求-报名团期备注默认展开***start*** author-yang.gao Date-2016-12-30 -->

					<!-- 285 越柬行踪 报名 团期列表默认展开备注 -->
					<!--<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
						<a class="expandNotes" href="javascript:void(0)">收起备注</a>
					</c:if>
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586'}">
						<a class="expandNotes" href="javascript:void(0)">展开备注</a>
					</c:if>-->
					
					<!-- 越柬行踪：7a81b21a77a811e5bc1e000c29cf2586 优加国际:cb19b61e52fb4b6ab422aedac38acdfa -->
					<c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586' || companyUuid eq 'cb19b61e52fb4b6ab422aedac38acdfa'}">
						<a class="expandNotes" href="javascript:void(0)">收起备注</a>
					</c:if>
					<c:if test="${companyUuid ne '7a81b21a77a811e5bc1e000c29cf2586' && companyUuid ne 'cb19b61e52fb4b6ab422aedac38acdfa'}">
						<a class="expandNotes" href="javascript:void(0)">展开备注</a>
					</c:if>
					
					<!-- 576需求-报名团期备注默认展开***end*** -->
					
					
					
					<em class="groupNoteTipImg" <c:if test="${empty group.groupRemark}">style="display:none;"</c:if>></em>

					<shiro:hasPermission name="price:project">
						<c:if test="${not empty group.priceJson}">
							<br/>
							<a href="javascript:void(0);" onclick="expandPriceJson(this, ${activityKind})" data='${group.priceJson}'>展开价格方案</a>
						</c:if>
					</shiro:hasPermission>
				</p>
			</dd>
		</dl>
	</c:otherwise>
</c:choose>