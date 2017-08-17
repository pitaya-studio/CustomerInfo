<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>

	<title>申请担保变更列表</title>
	<!-- 页面左边和上边的装饰 -->
	<meta name="decorator" content="wholesaler" />
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/guarantee.js"></script>
	<script type="text/javascript">
		$(function(){
			//操作浮框
			operateHandler();
		});

		function cancelApply(reviewUuid) {
			$.jBox.confirm("确定要取消担保变更申请吗？", "提示", function(v, h, f) {
				if (v == 'ok') {
					$.ajax({
						type : "POST",
						url : "${ctx}/guaranteeMod/cancelApply",
						data : {
							reviewUuid : reviewUuid
						},
						error: function(request) {
							top.$.jBox.tip("取消失败","warning");
						},
						success: function(data) {
							if (data.result == "success") {
								top.$.jBox.tip("取消成功","success");
								window.location.href = "${ctx}/guaranteeMod/list/${orderId}";
							} else {
								top.$.jBox.tip("取消失败","warning");
							}
						}
					});
				}
			});
		}
	</script>
</head>
<body>
	<div class="mod_nav">订单 > 签证 > 担保变更记录</div>
	<div class="ydbz_tit orderdetails_titpr">担保变更记录
		<a class="ydbz_x" href="${ctx}/guaranteeMod/guaranteeApply?orderId=${orderId}&flag=apply" target="_blank">申请担保变更</a>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="5%">游客</th>
				<th width="5%">签证国家</th>
				<th width="7%">订单号</th>
				<!-- <th width="10%">下单时间</th> -->
				<th width="10%">渠道</th>
				<th width="5%">销售</th>
				<th width="10%">申请时间</th>
				<th width="10%">原担保类型</th>
				<th width="10%">原押金金额</th>
				<th width="10%">申请担保类型</th>
				<th width="10%">申请交押金金额</th>
				<th width="10%">审批状态</th>
				<th width="10%">操作</th>

			</tr>
		</thead>
		<tbody>
			<c:forEach items="${travelerList}" var="traveler">
				<tr>
					<td>${traveler.travelerName}</td>
					<td>${traveler.visaCountry}</td>
					<td>${traveler.visaorderNo}</td>
					<td>${fns:getAgentName(traveler.agentinfoId)}</td>
					<td>${traveler.salerName}</td>
					<td class="tc">
						<div class="yfje_dd">
							<span class="fbold">
								<fmt:formatDate value="${traveler.createDate}" pattern="yyyy-MM-dd"/>
							</span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">
								<fmt:formatDate value="${traveler.createDate}" pattern="HH:mm:ss"/>
							</span>
						</div>
					</td>
					<td class="fbold">
						<c:choose>
							<c:when test="${traveler.guaranteeStatus eq 1}">担保</c:when>
							<c:when test="${traveler.guaranteeStatus eq 2}">担保+押金</c:when>
							<c:when test="${traveler.guaranteeStatus eq 3}">押金</c:when>
							<c:when test="${traveler.guaranteeStatus eq 4}">无需担保</c:when>
						</c:choose>
					</td>
					<td class="tr fbold">${traveler.totalDeposit}</td>
					<td class="fbold">
						<c:choose>
							<c:when test="${traveler.newGuaranteeType eq 1}">担保</c:when>
							<c:when test="${traveler.newGuaranteeType eq 2}">担保+押金</c:when>
							<c:when test="${traveler.newGuaranteeType eq 3}">押金</c:when>
							<c:when test="${traveler.newGuaranteeType eq 4}">无需担保</c:when>
						</c:choose>
					</td>
					<td class="tr fbold"><c:if test="${traveler.newGuaranteeType eq 2 or traveler.newGuaranteeType eq 3}">${fns:getCurrencyNameOrFlag(traveler.currencyId, '0') }${traveler.amount}</c:if></td>

					<td class="">${fns:getChineseReviewStatus(traveler.reviewStatus, traveler.current_reviewer)}</td>
					<td class="tc">
						<dl class="handle">
							<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
							<dd class="">
								<p>
									<span></span>
									<a href="${ctx}/guaranteeMod/guaranteeApply?orderId=${orderId}&flag=detail&review=1&travelerId=${traveler.id}&nav=1" target="_blank">详情</a>
									<c:if test="${traveler.reviewStatus eq 1}"><a href="javascript:cancelApply('${traveler.reviewUuid}');">取消申请</a></c:if>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
