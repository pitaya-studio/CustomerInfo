<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
 <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
   <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		 	<title>订单-签证-宣传费记录</title>
	</c:when>
	<c:otherwise>
		    <title>订单-签证-返佣记录</title>
	</c:otherwise>
</c:choose>

<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/modules/order/rebates/visarebatesList.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
	yd_dt_Handler();
});

function cancelConfirm(revId,_this){
	var $this = $(_this);
		$.jBox.confirm("确定取消吗？", "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
					type: "POST",
					url: "${ctx}/visa/order/cancelVisaRebates",
					async:false,
					cache:false,
					data: {
						revId:revId
					},
					success: function(data){
						if (data.success) {
							top.$.jBox.tip('取消成功','success');
							$this.remove();
							$("#searchForm").submit();
						} else {
							top.$.jBox.tip('取消失败','fail');
						}
					}
				});
			}else if (v == 'cancel'){
				
			}
	});
}
</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/visa/order/showRebatesList?orderId=${orderId}" method="post" >
	  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		 		    <div class="mod_nav">订单 > 签证 > 宣传费记录</div>
	<div class="ydbz_tit orderdetails_titpr">宣传费记录
<!-- 	<a class="ydbz_x" href="javascript:rebates.addRebates(${orderId},${orderType});">申请返佣</a> -->
	</div>
	</c:when>
	<c:otherwise>
		    <div class="mod_nav">订单 > 签证 > 返佣记录</div>
	<div class="ydbz_tit orderdetails_titpr">返佣记录
<!-- 	<a class="ydbz_x" href="javascript:rebates.addRebates(${orderId},${orderType});">申请返佣</a> -->
	</div>
	</c:otherwise>
</c:choose>   


	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="5%">申请返佣日期</th>
				<th width="5%">游客/团队</th>
				<th width="5%">款项</th>
				<th width="10%">应收金额</th>
				  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
				<c:choose>
					<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<th width="10%">预计宣传费金额</th>
						<th width="10%">累计宣传费金额</th>
						<th width="10%">宣传费差额</th>
					</c:when>
					<c:otherwise>
						<th width="10%">预计返佣金额</th>
						<th width="10%">累计返佣金额</th>
						<th width="10%">返佣差额</th>
					</c:otherwise>
				</c:choose>   
				<th width="6%">申请人</th>
				<th width="17%">状态</th>
				<th width="17%">备注</th>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:if test="${fn:length(borrowMoneyRecordList) <= 0 }">
			<tr class="toptr" >
				<td colspan="15" style="text-align: center;">暂无申请记录</td>
			</tr>
		</c:if>
		<c:forEach items="${borrowMoneyRecordList }" var="list">
		<tr>
			<td class="tc">${list.createDate }</td>
			<td class="tc">${list.travelerName }</td>
			 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
				<c:choose>
					<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<td class="tc">签证宣传费</td>
					</c:when>
					<c:otherwise>
						<td class="tc">签证返佣</td>
					</c:otherwise>
				</c:choose>   
			<td class="tr">${fns:getMoneyAmountBySerialNum(list.yingshouJe,2)}</td>
			<td class="tr">${list.yujiRebates}</td>
			<td class="tr">${fns:jsonDataPrice2ShwoPrice(list.totalRebatesJe) }</td>
			<td class="tr">${list.totalrebatesamount }</td>
			<td>${fns:getUserNameById(list.createBy)}</td>
			<c:choose>
				<c:when test="${list.status eq 0}">
					<td class="invoice_back" title="${list.denyReason}">${fns:getNextReview(list.id) }</td>
				</c:when>
				<c:when test="${list.status eq 2}">
					<td class="invoice_yes">${fns:getNextReview(list.id) }</td>
				</c:when>
				<c:when test="${list.status eq 3 or list.status eq 1}">
					<td class="invoice_no">${fns:getNextReview(list.id) }</td>
				</c:when>
				<c:when test="${list.status eq 4}">
					<td class="invoice_back">${fns:getNextReview(list.id) }</td>
				</c:when>
			</c:choose>
			<td>${list.rebatesnodes }</td>
			<td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
					<dd class="">
						<p>
							<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
							<c:choose>
								<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
										<a href="${ctx}/visa/order/visaRebatesDetail?orderId=${list.orderId}&travelerId=${list.travelerId}&flag=1&reviewId=${list.id}&flowType=${list.flowType}&nowLevel=" target="_blank">宣传费详情</a>
								</c:when>
								<c:otherwise>
										<a href="${ctx}/visa/order/visaRebatesDetail?orderId=${list.orderId}&travelerId=${list.travelerId}&flag=1&reviewId=${list.id}&flowType=${list.flowType}&nowLevel=" target="_blank">返佣详情</a>
								</c:otherwise>
							</c:choose>   
						
							<c:if test="${list.status eq 1}">
								<a href="javascript:void(0)" onClick="javascript:cancelConfirm('${list.id}',this);">取消申请</a>
							</c:if>
						</p>
					</dd>
				</dl>
			</td>
		</tr>
		</c:forEach>
		</tbody>
	</table>
	</form:form>
	<div class="ydBtn">
		<a href="javascript:window.close();" class="ydbz_s">关闭</a>
	</div>
</body>
</html>