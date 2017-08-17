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
					url: "${ctx}/visa/rebate/cancelVisaRebates",
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

function addRebates(orderId, orderType){
	/*
	新流程审批互斥规则是根据游客判断，此处无法判断当前要进行的申请操作的游客，所以屏蔽此处申请互斥判断直接跳入申请页面，在申请页面根据选择的不同游客进行互斥验证 update by zhanghao
	$.ajax( {
		type : "POST",
		async: false, 
		url : contextPath + "/visa/rebate/checkMutex",
		data : {
			visaOrderId : orderId,
			orderType : orderType
		},
		success : function(data) {
			if(data.success == true){
				window.open(contextPath + "/visa/rebate/visaRebateForm/" + orderId);
			}else{
				jBox.tip(data.messageString);
			}
		}
	});
	*/
	window.open(contextPath + "/visa/rebate/visaRebateForm/" + orderId);
}

</script>
</head>
<body>
	<form:form id="searchForm" modelAttribute="travelActivity" action="${ctx}/visa/rebate/visaRebateList?orderId=${orderId}" method="post" >
	 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		<div class="mod_nav">订单 > 签证 > 宣传费记录</div>
		<div class="ydbz_tit orderdetails_titpr">宣传费记录<a class="ydbz_x" href="javascript:addRebates(${orderId},${orderType});">申请宣传费</a></div>
	</c:when>
	<c:otherwise>
		<div class="mod_nav">订单 > 签证 > 返佣记录</div>
		<div class="ydbz_tit orderdetails_titpr">返佣记录<a class="ydbz_x" href="javascript:addRebates(${orderId},${orderType});">申请返佣</a></div>
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
		<c:if test="${fn:length(rebateRecordList) <= 0 }">
			<tr class="toptr" >
				<td colspan="15" style="text-align: center;">暂无申请记录</td>
			</tr>
		</c:if>
		<c:forEach items="${rebateRecordList }" var="rebateRecord">
		<tr>
			<td class="tc"><fmt:formatDate value="${rebateRecord.createDate }" pattern="yyyy-MM-dd"/></td>
			<td class="tc">${rebateRecord.showName }</td>
			<td class="tc">签证返佣</td>
			<td class="tr">${fns:getMoneyAmountBySerialNum(rebateRecord.yingshouJe,2)}</td>
			<td class="tr">${rebateRecord.yujiRebates}</td>
			<td class="tr">${fns:jsonDataPrice2ShwoPrice(rebateRecord.extend3) }</td>
			<td class="tr">${rebateRecord.markTotalMoney }</td>
			<td>${fns:getUserNameById(rebateRecord.createBy)}</td>
			<!--审批状态--已驳回0, 处理中1,已通过2,已取消3 -->
			<c:choose>
				<c:when test="${rebateRecord.status eq 0}">
					<td class="invoice_back" title="${rebateRecord.denyReason}">${fns:getChineseReviewStatus(rebateRecord.status,rebateRecord.currentReviewer)}</td>
				</c:when>
				<c:when test="${rebateRecord.status eq 1}">
					<td class="invoice_yes">${fns:getChineseReviewStatus(rebateRecord.status,rebateRecord.currentReviewer)}</td>
				</c:when>
				<c:when test="${rebateRecord.status eq 2}">
					<td class="invoice_no">${fns:getChineseReviewStatus(rebateRecord.status,rebateRecord.currentReviewer)}</td>
				</c:when>
				<c:when test="${rebateRecord.status eq 3}">
					<td class="invoice_back">${fns:getChineseReviewStatus(rebateRecord.status,rebateRecord.currentReviewer)}</td>
				</c:when>
			</c:choose>
			<td>${rebateRecord.allNotes }</td>
			<td class="p0">
				<dl class="handle">
					<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
					<dd class="">
						<p>
							 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
				<c:choose>
					<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
						<a href="${ctx}/visa/rebate/visaRebateDetail?orderId=${rebateRecord.orderId}&travelerId=${rebateRecord.travelerId}&flag=1&reviewId=${rebateRecord.id}&flowType=${rebateRecord.flowType}&nowLevel=" target="_blank">宣传费详情</a>
					</c:when>
					<c:otherwise>
						<a href="${ctx}/visa/rebate/visaRebateDetail?orderId=${rebateRecord.orderId}&travelerId=${rebateRecord.travelerId}&flag=1&reviewId=${rebateRecord.id}&flowType=${rebateRecord.flowType}&nowLevel=" target="_blank">返佣详情</a>
					</c:otherwise>
				</c:choose>   
							
							<c:if test="${rebateRecord.status eq 1 && currUserId == rebateRecord.createBy}">
								<a href="javascript:void(0)" onClick="javascript:cancelConfirm('${rebateRecord.id}',this);">取消申请</a>
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