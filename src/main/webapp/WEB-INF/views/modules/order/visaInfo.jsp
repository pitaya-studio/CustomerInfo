<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证信息</title>
<meta name="decorator" content="wholesaler" />
<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script> 
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/modules/order/orderList.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/visa/visaOrderList.js" type="text/javascript"></script>
</head>
<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">签证信息</page:param>
</page:applyDecorator>
<!--右侧内容部分开始-->
<div class="mod_nav">订单 > ${orderType} > 签证信息</div>
<div class="ydbz_tit orderdetails_titpr">游客列表</div>
<table id="contentTable" class="activitylist_bodyer_table">
	<thead>
		<tr>
			<th width="5%">姓名</th>
			<th width="7%">护照号</th>
			<th width="7%">AA码</th>
			<th width="6%">签证类别</th>
			<th width="6%">签证国家</th>
			<th width="7%">预计出团时间</th>
			<th width="7%">预计约签时间</th>
			<th width="7%">实际出团时间</th>
			<th width="7%">实际约签时间</th>
			<th width="6%">签证状态</th>
			<th width="6%">护照状态</th>
			<th width="6%">担保状态</th>
			<th width="6%">应收押金<br/>达账押金</th>
			<th width="3%">操作</th>
			<th width="3%">下载</th>
			<th width="3%">财务</th>
		</tr>
	</thead>
	
	<tbody>
	<tags:message content="${message}"/>
	<c:forEach items="${travelers}" var="traveler">
		<tr>
			<td class="tc">${traveler.travelerName}</td>
			<td class="tc">${traveler.passportId}</td>
			<td class="tc">${traveler.AACode}</td>
			<td class="tc">${traveler.visaType}</td>
			<td class="tc">${traveler.visaCountry}</td>
			<td class="tc">${traveler.forecastStartOut}</td>
			<td class="tc">${traveler.forecastContract}</td>
			<td class="tc">${traveler.startOut}</td>
			<td class="tc">${traveler.contract}</td>
			<td class="tc">
				<c:forEach items="${visaStatusList}" var="visaStatus">
					<c:choose>
						<c:when test="${visaStatus.value eq traveler.visaStatus}">
							${visaStatus.label}
						</c:when>
					</c:choose>
				</c:forEach>
			</td>
			<td class="tc">
				<c:choose>
					<c:when test="${ '1' eq traveler.passportStatus}">
						借出
					</c:when>
					<c:when test="${ '2' eq traveler.passportStatus}">
						归还客户
					</c:when>
					<c:when test="${ '3' eq traveler.passportStatus}">
						未签收
					</c:when>
					<c:when test="${ '4' eq traveler.passportStatus}">
						已签收
					</c:when>
				</c:choose>
			</td>
			<td class="tc">
				<c:choose>
					<c:when test="${ '1' eq traveler.guaranteeStatus}">
						担保
					</c:when>
					<c:when test="${ '2' eq traveler.guaranteeStatus}">
						担保+押金
					</c:when>
					<c:when test="${ '3' eq traveler.guaranteeStatus}">
						押金
					</c:when>
				</c:choose>
			</td>
			<td class="p0 tr">	
				<div class="yfje_dd">	
					<span class="fbold" id="traveleryingshouyajin${traveler.visaId}${traveler.id}">
					<c:choose>
						<c:when test="${empty  traveler.totalDeposit}">
						0
						</c:when>
						<c:otherwise>
						${traveler.totalDeposit}
						</c:otherwise>
					</c:choose>
					</span>
				</div>
				<div class="dzje_dd">
					<span class="fbold" id="accountedDeposit">
					<c:choose>
						<c:when test="${ empty  traveler.accountedDeposit}">
						0
						</c:when>
						<c:otherwise>
						${traveler.accountedDeposit}
						</c:otherwise>
					</c:choose>
					</span>
				</div>
			</td>
			<td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
					<dd>
						<p>
							<span></span>   
							<a target="_blank" href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId=${traveler.visaorderId}&mainOrderCode=${mainOrderId}&details=1">详情 </a>
							<c:if test="${traveler.visaStatus != 4}">
								<a href="javascript:void(0)" onclick="updateVisaStatus('${traveler.AACode}', '4', '${traveler.passportStatus}', '${traveler.guaranteeStatus}', '${traveler.startOut}', '${traveler.contract}', '${traveler.id }', '${traveler.visaId }')">撤签</a>
							</c:if>
						</p>
					</dd>
				</dl>
			</td>
			<td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_xz.png"></dt>
					<dd class="">
						<p>
							<span></span>
							<a target="_blank" href="${ctx}/visa/order/interviewNotice?type=order&id=${result.orderId}">面签通知</a>
						</p>
					</dd>
				</dl>
			</td>
			 <td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_fk.png"></dt>
					<dd class="">
						<p>
							<span></span> 
							<a href="javascript:void(0)" onclick="jbox_hsj('${traveler.id}');">还收据</a>
							<a target="_blank" href="${ctx}/orderPay/pay?orderId=${result.orderId}&orderNum=${result.orderCode}&payPriceType=8&businessType=2&orderType=${result.groupTypeId}&agentId=${result.agentId}&paramCurrencyId=${result.huobiId}&paramCurrencyPrice=${result.jine}">付款</a>    
							<a href="javascript:void(0)" onclick="jbox_jkmx('${traveler.travelerName}','${traveler.passportId}','${result.creatUser}','${result.createTime}','${result.groupType}','${result.creatUser}','${result.orderCode}','${traveler.jiekuanCreateUser}','${traveler.jiekuanBizhong}${ traveler.jiekuanAmount}','${traveler.jiekuanRemarks }');">借款明细</a>
							<a href="javascript:void(0)" onclick="jbox_jk('${traveler.id}');">借款</a>
							<a href="javascript:void(0)" onclick="xiugaiyajin('${traveler.visaId}','${traveler.totalDepositUUID }','${traveler.id }','${result.orderId}') ">修改押金</a>
					   </p>
					</dd>
				</dl>
			</td>
		</tr> 
	</c:forEach>
	</tbody>
</table>
<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.close();">关闭</a></div>
</div>
</body>
</html>