<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>填写散拼订单信息</title>
<meta name="decorator" content="wholesaler" />
<%@ include file="/WEB-INF/views/modules/order/common/orderLooseCommonJS.jsp"%>
<script src="${ctxStatic}/modules/order/single/applyToOrder.js" type="text/javascript"></script>
</head>
<body>
<%@ include file="/WEB-INF/views/modules/order/common/travelerTemplate.jsp"%>
<div class="ydbzbox fs">
  	<%@ include file="/WEB-INF/views/modules/order/common/orderBaseInfoHide.jsp"%>
	<div class="ydbz yd-step1" id="stepbar">&nbsp;</div>
	<div class="ydbz_tit">订单基本信息</div>
	<p class="ydbz_mc">${product.acitivityName}</p>
	<!-- 产品基本信息 -->
	<ul class="ydbz_info">
		  <li><span>团号：</span><em class="fArial">${productGroup.groupCode}</em></li>
	   <li><span>出团日期：</span><fmt:formatDate value="${productGroup.groupOpenDate}" pattern="yyyy-MM-dd"/></li>
	   <li title="${product.targetAreaNames}"><span>目的地：</span>${product.targetAreaNames}</li>
	   <li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
	   <li><span>行程天数：</span>${product.activityDuration}天</li>
	   <li><span>预收人数：</span>${productGroup.planPosition}</li>
	   <li><span>已切位数：</span>${empty groupReserve.payReservePosition?0:groupReserve.payReservePosition}</li>
	   <li><span>余位人数：</span>${productGroup.freePosition}</li>
	   <li><span>预报名数：</span>${productGroup.orderPersonNum}</li>
	</ul>
	<%@ include file="/WEB-INF/views/modules/order/common/applyOrderBaseInfo.jsp"%>
	<!-- 游客信息 -->
	<div id="manageOrder_new" style="display:none;">
		<div class="ydbz_tit">请填写游客信息</div>
		<div class="warningtravelerNum">暂无游客信息</div>
		<!--填充游客列表信息-->
		<div id="traveler">
			<%@ include file="/WEB-INF/views/modules/order/common/travelerList.jsp"%>
		</div>
		<!--添加游客按钮开始-->
		<div class="touristBtn"><a class="btn-addGrey" id="addTraveler">添加游客</a></div>
		<!--添加游客按钮结束-->
		<div class="ydbz_sxb" id="secondDiv" style="display:none;" >
		    <div class="ydBtn ydBtn2">
		        <a class="ydbz_s" id="secondToOneStepDiv">上一步</a>
		        <a class="ydbz_x" id="secondToThirdStepDiv">下一步</a>
		    </div>
		</div>
		<div class="ydbz_sxb" id="thirdDiv" style='display:none;'>
		   <div class="ydBtn ydBtn2">
		      <div class="ydbz_s" id="thirdToSecondTStepDiv" >上一步</div>
		      <div class="ydbz_x" id="thirdToFourthStepDiv" onClick="$(this).attr('id','');$(this).attr('class','ydbz_s gray');">转正订单</div>
		      <select style="display:none;">
                <c:if test="${product.payMode_full=='1'}">
            		<option value='3'>全款支付</option>
            	</c:if>
                <c:if test="${product.payMode_op=='1'}">
            		<option value='7'>计调确认占位</option>
            	</c:if>
                <c:if test="${product.payMode_cw=='1'}">
            		<option value='8'>财务确认占位</option>
            	</c:if>
            	<c:if test="${product.payMode_deposit=='1'}">
            		<option value='1'>订金占位</option>
            	</c:if>
            	<c:if test="${product.payMode_advance=='1'}">
            		<option value='2'>预占位</option>
            	</c:if>
            	<c:if test="${product.payMode_data=='1'}">
            		<option value='4'>资料占位</option>
            	</c:if>
            	<c:if test="${product.payMode_guarantee=='1'}">
            		<option value='5'>担保占位</option>
            	</c:if>
            	<c:if test="${product.payMode_express=='1'}">
            		<option value='6'>确认单占位</option>
            	</c:if>
               </select>
		   </div>
		</div>
		<div style="text-align:right; font-size:12px; margin-top:20px;  padding-top:10px; padding-right:10px;">
		<b style="font-size:18px">订单总额：</b><span id="travelerSumPrice" class="tdred f20"></span>
		</div>
	</div>
</div>
</body>
</html>