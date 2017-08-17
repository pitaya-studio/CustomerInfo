<!--
author:yunpeng.zhang
describe:单团订单退团记录页,适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2015年11月18日
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>退团记录</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	g_context_url = "${ctx}";
    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');
	}
    $(".qtip").tooltip({
        track: true
    });

    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");

	<%-- 前端js效果部分 --%>
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });

    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});

});

/**
 * 退团
 * param id   订单唯一标识
 * author     chenry
 * createDate 2014-11-04
 */
function viewExitGroup() {
	window.open("${ctx}/orderCommon/manage/viewExitGroup?productType=${productType}&flowType=8&orderId=${orderId}", "_blank");
}

/**
 * 申请退团
 */
function newApplyExitGroup() {
	window.location.href ="${ctx}/singleOrder/exitGroup/newExitGroupDetails?id=${orderId}&productType=${productType}";
}

/**
 * 取消退团申请
 */
function cancelExitGroup(status, reviewId, travelerId) {
	$.jBox.confirm("确定要取消退团申请吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				cache: false,
				type: "POST",
				url:g_context_url+ "/singleOrder/exitGroup/cancelExitGroup",
				data:{
					reviewId: reviewId,
					status:status,
					travelerId:travelerId
				},
				error: function(request) {
					top.$.jBox.tip('操作失败');
				},
				success: function(result) {
					if(result.msg == "success"){
						$.jBox.tip('操作成功!');
						window.location.href ="${ctx}/singleOrder/exitGroup/newExitGroupList?orderId=${orderId}&productType=${productType}";
					} else {
						$.jBox.tip('操作失败！');
					}
				}
			});
		}
	});
}


</script>

<style type="text/css">
a {
	display: inline-block;
}

* {
	margin: 0px;
	padding: 0px;
}

body {
	background: #fff;
	margin: 0px auto;
}

.pop_gj {
	padding: 10px 24px;
	margin: 0px;
	border-bottom: #b3b3b3 1px dashed;
	overflow: hidden;
}

.pop_gj dt {
	float: left;
	width: 100%;
}

.pop_gj dt span {
	float: left;
	width: 80px;
	text-align: right;
	color: #333;
	font-size: 12px;
	overflow: hidden;
	height: 25px;
	line-height: 180%;
}

.pop_gj dt p {
	float: left;
	width: 300px;
	color: #000;
	font-size: 12px;
	line-height: 180%;
}

.pop_xg {
	padding: 10px 4px;
	margin: 0px;
	overflow: hidden;
}

.pop_xg dt {
	float: left;
	width: 100%;
	margin-top: 10px;
	height: 30px;
}

.pop_xg dt span {
	float: left;
	width: 100px;
	text-align: right;
	color: #333;
	font-size: 12px;
	overflow: hidden;
	height: 30px;
	line-height: 30px;
}

.pop_xg dt p {
	float: left;
	width: 110px;
	color: #333;
	font-size: 12px;
	height: 30px;
	line-height: 30px;
	overflow: hidden;
	position: relative;
}

.pop_xg dt p font {
	color: #e60012;
	font-size: 12px;
}

.pop_xg dt p input {
	width: 60px;
	height: 28px;
	line-height: 28px;
	padding: 0px 5px 0px 18px;
	color: #403738;
	font-size: 12px;
	position: relative;
	z-index: 3;
}

.pop_xg dt p i {
	position: absolute;
	height: 28px;
	top: 2px;
	width: 10px;
	text-align: center;
	left: 5px;
	z-index: 5;
	font-style: normal;
	line-height: 28px;
}

.release_next_add button {
	cursor: pointer;
	border-radius: 4px;
}

label {
	cursor: inherit;
}

.activitylist_bodyer_table tr td {
	text-align: center;
}
</style>
</head>
<body>
<div id="sea">
	<div class="mod_nav">
		订单&nbsp;&nbsp;>单团&nbsp;&nbsp;>退团记录
	</div>
	<!-- 顶部参数 -->
	<div class="activitylist_bodyer_right_team_co_bgcolor" style="float: left; width: 100%">
		<!--右侧内容部分开始-->
		<div class="ydbz_tit orderdetails_titpr">
			退团记录
			<a class="ydbz_x" onclick="newApplyExitGroup()">申请退团</a>
		</div>
		<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="5%">游客</th>
				<th width="10%">下单时间</th>
				<th width="10%">报批日期</th>
				<th width="10%">应收金额</th>
				<th width="10%">退团后应收</th>
				<th width="10%">退款金额</th>
				<th width="15%">退团原因</th>
				<th width="10%">状态</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${processList }" var="process">
			<tr>
				<td>${process.travellerName }</td>
				<td><fmt:formatDate value="${process.orderTime }" type="both"/></td>
				<td><fmt:formatDate value="${process.createDate }" type="both"/></td>
				<td style="text-align: right">${process.travelerPayPrice }</td>
				<td style="text-align: right">
					<span>${process.afterString }</span>
				</td>
				<td style="text-align: right">
					<span>${process.refundString }</span>
				</td>
				<td>${process.remark }</td>
				<td>
					${fns:getChineseReviewStatus(process.status,process.currentReviewer)}
				</td>
				<td>
					<dl class="handle">
						<dt><img title="操作" src="${ctxStatic }/images/handle_cz.png"></dt>
						<dd class="">
						<p>
							<span></span>
					   	 	<a href="${ctx}/singleOrder/exitGroup/newExitGroupInfo?orderId=${orderId}&reviewId=${process.id}&productType=${productType}" target="_blank">退团详情 </a>
					   	 	<c:if test="${process.status eq '1' and process.createBy eq userId}">
						   	 	<a href="javascript:cancelExitGroup('${process.status }','${process.id}', '${process.travellerId }');">取消 </a>
					   	 	</c:if>
					   	</p>
						</dd>
					</dl>
				</td>
			</tr>
		</c:forEach>
		</tbody>
		</table>
		<!--右侧内容部分结束-->
	</div>
</div>
</body>
</html>
