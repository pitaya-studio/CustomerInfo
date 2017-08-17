<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>产品-散拼优惠审批-优惠记录</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	g_context_url="${ctx}";
	//操作浮框
	operateHandler();
	yd_dt_Handler();
});
//撤销借款
function  cancelPrivilegePrice(status,id){
	if (status!=1){
		top.$.jBox.tip('当前状态不能取消!');
		return false;
	}
	top.$.jBox.confirm('确认要取消流程申请吗？','系统提示',function(v){
		if(v=='ok'){
			$.ajax({                 
				cache: true,                 
				type: "POST",                 
				url:g_context_url+ "/singlegroup/privilege/cancelPrivilegePrice",                 
				data:{
					id:id,
					status:status
				},                
				async: false,                 
				error: function(request) {                     
					top.$.jBox.tip('操作失败');
				},                 
				success: function(data) { 
					if(data!="取消申请成功"){
						top.$.jBox.tip(data,'warning');
						return false;
					}else{
						top.$.jBox.tip(data,'warning');
						window.location.href=g_context_url+"/singlegroup/privilege/privilegeApplyList?orderId="+${orderId };
					}
				}             
			});
		}
	});
}
//申请借款(流程互斥)
 function ApplyForloan(orderId){
	$.ajax({                 
		cache: true,                 
		type: "POST",                 
		url:g_context_url+ "/activityOrder/lendmoney/beforeAddLendMoneyApply",                 
		data:{orderId:orderId},                
		async: false,                 
		error: function(request) {                     
			top.$.jBox.tip('操作失败');
		},                 
		success: function(data) { 
			  if(data!=""){
				 top.$.jBox.tip(data,'warning');
				 window.location.href=g_context_url+"/activityOrder/lendmoney/borrowAmountList?flowType=19&productType=7&orderId="+${orderId };
				 return false;}
			//window.location.href=g_context_url+"/order/lendmoney/airticketLendMoneyApply?orderId="+${orderId}; 
			window.open(g_context_url+"/activityOrder/lendmoney/airticketLendMoneyApply?orderId="+${orderId});
		}             
	});
} 
</script>
</head>
<body>
<!--右侧内容部分开始-->
<div class="mod_nav">订单 > 散拼 > 优惠记录</div>
 <input type="hidden" name="orderId" value="${orderId }"></input> 
<div class="mod_information">
	<div class="mod_information_d">
		<div class="ydbz_tit fl">优惠记录</div>
		<div class="fr">
			<c:if test="${(orderDelFlag eq '4') or (orderDelFlag eq '5')}">
				<a href="${ctx}/singlegroup/privilege/toPrivilegeApply?orderId=${orderId}"  class="ydbz_x"   target="_blank"  >申请优惠</a>
			</c:if>
<!-- 			<input id="btn_search" class="btn btn-primary ydbz_x" onclick="javascript:window.location.href='产品-散拼优惠审批--申请优惠(c109).html'"  value="申请优惠" type="button"> -->
		</div>
	</div>
</div>
<div class="mod_details2_tabletype">
<!--S--C109--优惠记录-->
	<table class="table" style="width:100%;">
		<thead>
			<tr>
				<th class="tc" width="12%">报批日期</th>
				<th class="tc" width="9%">订单号</th>
				<th class="tc" width="9%">订单人数</th>
<!-- 				<th class="td" width="9%">同行价</th> -->
				<th class="tc" width="7%">申请优惠总额</th>
				<th class="tc" width="7%">申请优惠人数</th>
				<th class="tc" width="7%">申请人</th>
				<th class="tc" width="7%">审批动态</th>
				<th class="tc" width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${processList }" var="processList">
				<tr>
<!-- 					<td class="tc">${processList['createDate'] }</td> -->
					<td class="tc"><fmt:formatDate value="${processList['createDate'] }" pattern="yyyy-MM-dd"/> </td>
					<td class="tc">${processList['orderNo'] }</td>
					<td class="tc">${processList['orderPersonNum'] }</td>
<!-- 					<td class="tr">${processList['thTotalPrice'] }</td> -->
					<td class="tr">${processList['inputsqyhtotalprice'] }</td>
					<%--<td class="tr">${yhTotalPrice }</td>--%>
					<td class="tc">${processList['applyPrivilegePersonNum'] }</td>
					<td class="tc">${fns:getUserNameByIds(processList['createBy'] )}</td>
					<td class="tc">${fns:getChineseReviewStatus(processList.status,processList.currentReviewer)}</td>
					<td class="p0">
						<dl class="handle">
							<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
							<dd>
								<p><span></span>
									<a  href="${ctx}/singlegroup/privilege/privilegeDetail?orderId=${orderId }&reviewId=${processList.id }" target="_blank">优惠详情</a>
									<c:if test="${processList.status == 1 && fns:isShowCancel(processList.id) == true}">
										<a onclick="cancelPrivilegePrice('${processList.status }','${processList.id }')" href="javascript:void(0)">取消申请</a>
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
</body>
</html>
