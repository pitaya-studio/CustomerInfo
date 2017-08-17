<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="decorator" content="wholesaler" />
<title>订单跟踪设置</title>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/orderTrack.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/orderTrackSet.css"/>
<link type="text/css"  rel="stylesheet" href="${ctxStatic}/css/font-awesome-4.6.3/css/font-awesome.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/order/orderTrackingSetting/orderTrackingSetting.js"></script>
</head>
<body>
	<table class="activitylist_bodyer_table mainTable table_adjust_set" id="contentTable_quauq">
		<thead>
			<tr>
				<th width="20%">步骤</th>
				<th width="20%">绿灯时间</th>
				<th width="20%">黄灯时间</th>
				<th width="20%">红灯时间</th>
				<th width="20%">操作</th>
			</tr>
		</thead>
		<tbody class="orderOrGroup_group_tbody">
			<c:if test="${fn:length(page.list) <= 0 }">
				<tr class="toptr" >
					<td colspan="5" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>
       
			<c:forEach items="${page.list}" var="setting" varStatus="s">
				<tr>
					<td class="tc">
						<c:choose>
							<c:when test="${setting.settingType eq 1}">销售处理中</c:when>
							<c:when test="${setting.settingType eq 2}">计调处理中</c:when>
							<c:when test="${setting.settingType eq 3}">财务收款</c:when>
						</c:choose>
					</td>
					<!-- 绿灯 -->
					<c:set var="greenLightTimeType" value=""></c:set>
					<c:choose>
						<c:when test="${setting.greenLightTimeType eq 1}"><c:set var="greenLightTimeType" value="天"></c:set></c:when>
						<c:when test="${setting.greenLightTimeType eq 2}"><c:set var="greenLightTimeType" value="小时"></c:set></c:when>
						<c:when test="${setting.greenLightTimeType eq 3}"><c:set var="greenLightTimeType" value="分钟"></c:set></c:when>
					</c:choose>
					<!-- 黄灯 -->
					<c:set var="yellowLightTimeType" value=""></c:set>
					<c:choose>
						<c:when test="${setting.yellowLightTimeType eq 1}"><c:set var="yellowLightTimeType" value="天"></c:set></c:when>
						<c:when test="${setting.yellowLightTimeType eq 2}"><c:set var="yellowLightTimeType" value="小时"></c:set></c:when>
						<c:when test="${setting.yellowLightTimeType eq 3}"><c:set var="yellowLightTimeType" value="分钟"></c:set></c:when>
					</c:choose>
					<!-- 红灯 -->
					<c:set var="redLightTimeType" value=""></c:set>
					<c:choose>
						<c:when test="${setting.redLightTimeType eq 1}"><c:set var="redLightTimeType" value="天"></c:set></c:when>
						<c:when test="${setting.redLightTimeType eq 2}"><c:set var="redLightTimeType" value="小时"></c:set></c:when>
						<c:when test="${setting.redLightTimeType eq 3}"><c:set var="redLightTimeType" value="分钟"></c:set></c:when>
					</c:choose>
					<td class="tc">${setting.greenLightTimeStart}~${setting.greenLightTimeEnd}${greenLightTimeType}
					</td>
					<td class="tc">${setting.yellowLightTimeStart}~${setting.yellowLightTimeEnd}${yellowLightTimeType}
					</td>
					<td class="tc">${setting.redLightTimeStart}${redLightTimeType}至以上</td>
					<td class="tc">
						<a href="javascript:void(0)" onclick="jbox_order_tracking_setting(${setting.id}, ${setting.settingType},
						 ${setting.greenLightTimeType}, ${setting.greenLightTimeStart}, ${setting.greenLightTimeEnd},
						 ${setting.yellowLightTimeType}, ${setting.yellowLightTimeStart}, ${setting.yellowLightTimeEnd},
						 ${setting.redLightTimeType}, ${setting.redLightTimeStart})">修改时限</a>
					</td>
				</tr> 
			</c:forEach>
		</tbody>
	</table>
	
	<div id="pop_order_track" style="display: none;">
		<div class="pop_order_content">
			<label>步骤：</label><span class="pop_title"></span><br>
			<label><em class="fa fa-dot-circle-o green-color not-move"></em> 绿灯开启：</label>从<span>0</span><em name="sameTimeType"></em>至
			<input class="input_box" type="text" id="greenEndTime" maxlength="5" onkeyup="greenEndTimeChange(this)"/>
			<div class="dl-select">
				<input type="text" class="light_other" readonly placeholder="请选择">
				<ul class="select-option">
					<li>分钟</li>
					<li>小时</li>
					<li>天</li>
				</ul>
			</div>
			<label><em class="fa fa-dot-circle-o yellow-color not-move"></em> 黄灯开启：</label>从<span id="yellowStartTime">0</span><em name="sameTimeType"></em>至
			<input class="input_box" type="text" id="yellowEndTime" maxlength="5" onkeyup="yellowEndTimeChange(this)"/>
			<div class="dl-select">
				<input type="text" class="light_other" readonly placeholder="请选择">
				<ul class="select-option">
					<li>分钟</li>
					<li>小时</li>
					<li>天</li>
				</ul>
			</div>
			<label style="margin-top: 8px"><em class="fa fa-dot-circle-o red-color not-move"></em> 红灯开启：</label>从<span id="redStartTime">0</span><em name="sameTimeType"></em>起
		</div>
	</div>
</body>
</html>

