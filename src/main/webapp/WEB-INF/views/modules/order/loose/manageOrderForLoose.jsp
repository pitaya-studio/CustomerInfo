<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>a
<head>
<title>填写订单信息</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/modules/order/common/orderLooseCommonJS.jsp"%>
<script src="${ctxStatic}/modules/order/single/singleorder.js?v=1209" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/single/otherCostFee.js?v=1209" type="text/javascript"></script>
<link href="${ctxStatic}/css/order-style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${ctxStatic}/jquery/jquery.nicescroll.min.js"></script>

<%--bug 16327 Start--%>
<%--报名散拼的预定页面,目的地的提示样式--%>
<style>
	#targetAreaShow{
		overflow: visible;
		position: relative;
	}
	.hint_show_sty{
		overflow: hidden;
		word-break: break-all;
		position: absolute;
		max-width: 305px!important;
		display: none;
		max-height: 88px;
		padding: 5px;
		top: 19px;
		left: 86px;
		background-color: #ffffff;
		border: 1px solid #dddddd;
		z-index: 100;
		white-space: normal;
		line-height: 19px;
	}
	#targetAreaShow .inhe{
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
		width: 70%;
		display: inline-block;
		vertical-align: bottom;
		color: #009535;
		cursor: pointer;
		text-align: left;
	}
</style>
<script>
	$(function () {
		$(".hint_show_sty").niceScroll({
			cursorcolor: "#ccc",//#CC0071 光标颜色
			cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
			touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
			cursorwidth: "5px", //像素光标的宽度
			cursorborder: "0", //     游标边框css定义
			cursorborderradius: "5px",//以像素为光标边界半径
			autohidemode: false //是否隐藏滚动条
		});
		$("#targetAreaShow .inhe").mouseenter(function(){
			$(".hint_show_sty").show();
		});
		$("#targetAreaShow").mouseleave(function(){
			$(".hint_show_sty").hide();
		});
	})
</script>
<%--bug 16327 End--%>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/order/common/travelerTemplate.jsp"%>
<div class="ydbzbox fs">
  	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfoHide.jsp"%>
	<div class="ydbz yd-step1" id="stepbar">&nbsp;</div>
	<div class="ydbz_tit">订单基本信息</div>
	<p class="ydbz_mc">${product.acitivityName}</p>
	<ul class="ydbz_info">
		<li><span>团号：</span><em class="fArial" title="${productGroup.groupCode}">${productGroup.groupCode}</em></li>
		<li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
		<%--bug 16327 Start--%>
		<%--报名散拼的预定页面,目的地的提示样式，新添加了个div和ID--%>
			<li id="targetAreaShow"><span>目的地：</span><span class="inhe">${product.targetAreaNames}</span>
			<div class="hover-title hint_show_sty" tabindex="5000">
				${product.targetAreaNames}
			</div></li>
		<%--bug 16327 End--%>
		<li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
		<li><span>行程天数：</span>${product.activityDuration}天</li>
		<c:choose>
			<c:when test="${activityKind == '10'}">
				<li><span>预收间数：</span>${productGroup.planPosition}</li>
				<li><span>余位间数：</span>${productGroup.freePosition}</li>
			</c:when>
	      	<c:otherwise>
	      		<li><span>预收人数：</span>${productGroup.planPosition}</li>
				<li><span>已切位数：</span>${empty groupReserve.payReservePosition?0:groupReserve.payReservePosition}</li>
				<li><span>余位人数：</span>${productGroup.freePosition}</li>
				<li><span>预报名数：</span>${productGroup.orderPersonNum}</li>
	      	</c:otherwise>
      	</c:choose>
      	<li><span>销售：</span>${fns:getUserById(productorder.salerId).name}</li>
		<li><span>下单人：</span>${fns:getUser().name}</li>
	</ul>
	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfo.jsp"%>
	<%@ include file="/WEB-INF/views/modules/order/common/orderTravelerAndOperate.jsp"%>
</div>
</body>
</html>