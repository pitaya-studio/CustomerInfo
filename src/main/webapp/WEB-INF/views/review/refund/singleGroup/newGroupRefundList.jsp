<!-- 
author:yunpeng.zhang
describe:单团类(单团订单，散拼订单，游学订单，大客户订单，自由行订单) 退款记录列表
createDate：2015年11月30日
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>退款记录</title>
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
 * 跳转到退款申请页面
 * @author yunpeng.zhang
 * @createDate 2015年11月30
 */
function newApplyGroupRefund() {
	window.location.href ="${ctx}/singleOrder/refund/newExitRefundDetails?orderId=${orderId}&productType=${productType}";
}

/**
 * 取消退款申请
 * @author yunpeng.zhang
 * @createDate 2015年12月2日
 */
function cancelGroupRefund(status, reviewId, travelerId) {
	$.jBox.confirm("确定要取消退款申请吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({                 
				cache: false,                 
				type: "POST",                 
				url:g_context_url+ "/singleOrder/refund/cancelGroupRefund",                 
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
						window.location.href ="${ctx}/singleOrder/refund/newGroupRefundList?orderId=${orderId}&productType=${productType}";
					} else {
						$.jBox.tip('操作失败！' + result.repley);
						window.location.reload();
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

.ydbz_tit{
	margin-top:30px;
	margin-bottom:30px;
	background-color:#f3f3f3;
	line-height:33px;color:#333333;
	font-weight:bold;padding-left:32px;margin-top:10px;margin-bottom:10px;
	position:relative; *height:33px;
	font-size:14px; border-radius:1px;
	overflow:hidden;
}

.ydbz_x,.ydbz_s{
	color:#fff;
	border:medium none;
	background:#5f7795;
	box-shadow: none;
	text-shadow: none;
	font-size:12px;
	border-radius:4px;
	height:28px;
	line-height:28px;}

.ydbz_x:hover,.ydbz_s:hover{
	text-decoration:none;
	background:#28b2e6;
	color:#fff;
}
</style>
</head>

<body>
<div id="sea">
	<div class="mod_nav">
		<c:if test="${productType==1 }">单团</c:if>
		<c:if test="${productType==2 }">散拼</c:if>
		<c:if test="${productType==3 }">游学</c:if>
		<c:if test="${productType==4 }">大客户</c:if>
		<c:if test="${productType==5 }">自由行</c:if>
		<c:if test="${productType==10 }">游轮</c:if>
		&nbsp;&nbsp;>退款&nbsp;&nbsp;>退款记录
	</div>
	<!-- 顶部参数 -->
	<div class="activitylist_bodyer_right_team_co_bgcolor" style="float: left; width: 100%">
		<!--右侧内容部分开始-->
		<p class="ydbz_tit orderdetails_titpr">
			退款记录
			<a class="ydbz_x" href="javascript:newApplyGroupRefund()">申请退款</a>
		</p>
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="5%" class="tc">游客</th>
					<th width="10%" class="tc">款项</th>
					<th width="10%" class="tc">报批日期</th>
					<th width="10%" class="tc">币种</th>
					<th width="10%" class="tr">应收金额</th>
					<th width="10%" class="tr">退款金额</th>
					<th width="15%" class="tc">退款备注</th>
					<th width="10%" class="tc">状态</th>
					<th width="10%" class="tc">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${processList }" var="process">
					<tr>
						<td>${process.travellerName }</td>
						<td>${process.refundName }</td>
						<td><fmt:formatDate value="${process.createDate }" type="both"/></td>
						<td>${process.currencyName }</td>
						<td class="tr">${process.payPrice }</td>
						<td class="tr">${process.currencyMark }${process.refundPrice }</td>
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
							   	 	<a href="${ctx}/singleOrder/refund/newGroupRefundInfo?orderId=${orderId}&reviewId=${process.id}&productType=${productType}" target="_blank">退款详情 </a>
							   	 	<c:if test="${process.status eq '1'}">
								   	 	<a href="javascript:cancelGroupRefund('${process.status }','${process.id}', '${process.travellerId }');">取消申请</a>
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
