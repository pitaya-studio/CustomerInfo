<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转款申请列表</title>
<meta name="decorator" content="wholesaler"/>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jquery/ajaxfileupload.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	//成本价滑过显示具体内容
	inquiryPriceCon();
});

//分页
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}

//取消申请
function cancelApp(orderId, reviewId, travellerId){
	$.jBox.confirm("确定要取消转款申请吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				cache: false,              
				type: "POST",                 
				url:contextPath+"/singlegrouporder/transfermoney/transfersMoneyCancel",
				data:{
					reviewId:reviewId,
					travellerId:travellerId
				},
				error: function(request) {                     
					top.$.jBox.tip('操作失败');
				},                
				success: function(result) {
					if(result.res == "success"){
						$.jBox.tip('操作成功!');
						window.location.href ="${ctx}/singlegrouporder/transfermoney/transfersMoneyApplyList/"+orderId;
					} else {
						$.jBox.tip('操作失败！' + result.reply);
					} 
				}
			});
		}
	});
}

/**
 * 转款申请
 */
function toApply(orderId){
	$.ajax({
		type : "POST",
		url : contextPath+"/singlegrouporder/transfermoney/validAccountCurrent/",
		data:{
			orderId:orderId
		},
		dataType : "text",
		success : function(data){
			var json;
			try{
				json = $.parseJSON(data);
			}catch(e){
				json = {res:"parse error"};							
			}
			if(json.res=="success"){
				window.location.href=contextPath+"/singlegrouporder/transfermoney/toTransfersMoneyApply/"+orderId;
			} else {
				jBox.tip(json.res, 'error');
				return;
			}
		}
	});	
}

/**
 * 旧转款申请记录
 * param orderId
 */
function transfersMoney(orderId){
	window.open(contextPath + "/orderCommon/transferMoney/transfersMoneyHref/" + orderId );
}

</script>
</head>
<body>
<input type="hidden" id="orderId" value="${orderId }">
<div id="sea">
	<!--右侧内容部分开始-->
	<div class="mod_nav">
		订单 > 
		<c:choose>
			<c:when test="${order.orderStatus == 1}">单团</c:when>
			<c:when test="${order.orderStatus == 2}">散拼</c:when>
			<c:when test="${order.orderStatus == 3}">游学</c:when>
			<c:when test="${order.orderStatus == 4}">大客户</c:when>
			<c:when test="${order.orderStatus == 5}">自由行</c:when>
			<c:when test="${order.orderStatus == 10}">游轮</c:when>
		</c:choose>
		> 转款记录
	</div>
	
	<div class="ydbz_tit orderdetails_titpr">
		转款记录
		<a class="ydbz_x" href="javascript:void" onclick="toApply(${orderId})">申请转款</a>
	</div>
	
	<table class="table activitylist_bodyer_table" id="contentTable">
		<thead>
			<tr>
				<th width="7%">游客</th>
				<th width="11%">下单时间</th>
				<th width="10%">报批日期</th>
				<th width="10%">转出团结算价</th>
				<th width="10%">转入团结算价</th>
				<th width="10%">转款金额</th>
				<th width="15%">备注</th>
				<th width="15%">状态</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${empty processList or fn:length(processList) <= 0 }">
	        	<tr class="toptr" >
	            	<td colspan="12" style="text-align: center;"> 暂无搜索结果</td>
	        	</tr>
        	</c:if>
			<c:forEach items="${processList }" var="show" varStatus="status">
				<tr class="toptr">					
					<td class="tc">${show.travellerName}</td>
					<td class="tc"><fmt:formatDate value="${show.orderTime }" type="both" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td class="tc"><fmt:formatDate value="${show.createDate }" type="both"/></td>
					<td class="tr"><c:if test="${empty show.oldPayPriceMoney }"><font color="gray">转团信息不足，无此项</font></c:if><span class="fbold tdgreen">${show.oldPayPriceMoney }</span></td>
					<td class="tr"><c:if test="${empty show.newPayPriceMoney }"><font color="gray">转团信息不足，无此项</font></c:if><span class="fbold tdgreen">${show.newPayPriceMoney }</span></td>
					<td class="tr"><span class="fbold tdorange">${fns:getReviewMoneyStrByUUID(show.transferMoneyUuid, 'mark')}</span></td>
					<td class="tc">${show.remark }</td>
					<td class="invoice_yes tc">
						<c:if test="${show.status eq '0'}">
							审批驳回
						</c:if>
						<c:if test="${show.status eq '1'}">
							待${fns:getUserNameByIds(show.currentReviewer) }审批
						</c:if>
						<c:if test="${show.status eq '2'}">
							审批通过
						</c:if>
						<c:if test="${show.status eq '3'}">
							审批取消
						</c:if>
                    </td>
					<td>
						<input type="hidden" name="reviewID" value="${show.id }">
						<input type="hidden" name="travellerID" value="${show.travellerId }">
						<dl class="handle">
							<dt>
								<img src="${ctxStatic}/images/handle_cz.png" title="操作">
							</dt>
							<dd>
								<p>
									<span></span>
									<a target="_blank" href="${ctx}/singlegrouporder/transfermoney/transferMoneyDetails/${show.id}">审批详情</a>
									<c:if test="${fns:isShowCancel(show.id)}">
										<a onclick="javascript:cancelApp('${orderId }', '${show.id }', '${show.travellerId }');">取消申请</a>
									</c:if>
								</p>			
							</dd>
						</dl>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</body>
</html>
