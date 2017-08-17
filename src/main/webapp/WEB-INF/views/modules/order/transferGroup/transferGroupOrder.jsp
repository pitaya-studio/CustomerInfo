<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>转团记录</title>
	<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//操作浮框
	operateHandler();
});

//跳转到申请转团
function gotoGroup(orderId){
	//window.open(contextPath + "/orderTransFerGroup/transferGroupApply/"+orderId);
	location.href = "${ctx}/orderTransFerGroup/transferGroupApply/"+orderId;
}
// 
/**
 * 跳转到转团详情
 *	newGroupCode  新团期COde
 *	travelerId 转团乘客id
 *	orderId	原订单Id
 */
function gotoGroupInfo(newGroupCode,travelerId,orderId,reviewId){
	window.open("${ctx}/orderTransFerGroup/transferGroupInfo/"+newGroupCode+"/"+travelerId+"/"+orderId+"/"+reviewId);
	//location.href =contextPath + "/orderTransFerGroup/transferGroupInfo/"+newGroupCode+"/"+travelerId+"/"+orderId+"/"+reviewId;
}
/**
 * 取消转团
 */
function cancelGotoGroup(reviewInfoId){
	$.jBox.confirm("确定要取消审核吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
                type: "POST",
                url: "${ctx}/orderCommon/manage/cancleAudit",
                data: {
                    id:reviewInfoId
                },
                success: function(msg){
                    location.reload();
                }
            });
		}
	});
}
</script>
</head>
<body>
	<page:applyDecorator name="show_head">
    	<page:param name="desc">转团记录</page:param>
	</page:applyDecorator>
				<!--右侧内容部分开始-->
                <div class="mod_nav">订单 > ${orderStatus } > 转团记录</div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="10%">游客</th>
						 <th width="10%">下单时间</th>
						 <th width="10%">报批日期</th>
						 <th width="10%">应收金额</th>
						 <th width="10%">转团后应收</th>
						 <th width="15%">转团原因</th>
						 <th width="15%">状态</th>
						 <th width="10%">操作</th>
					  </tr>
				   </thead>
				   <tbody>
					   <c:if test="${not empty list }">
					   		<c:forEach items="${list}"  var="transFerGroup">
					   		<tr>
					   			<td class="tc">${transFerGroup.travelerName }</td>
					   			<td class="tc"><fmt:formatDate value="${transFerGroup.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
					   			<td class="tc"><fmt:formatDate value="${transFerGroup.applyDate}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
					   			<td class="tc">${transFerGroup.money }</td>
					   			<td class="tc">${transFerGroup.subtractMoney }</td>
					   			<td class="tc">${transFerGroup.remark }</td>
					   			<td class="tc">
					   				<!-- 审核状态0: 已驳回 (审核失败);1: 待审核;2: 审核成功;3: 操作完成 (审核成功后，操作员完成退款退团等操作),4:取消申请 -->
					   			<!--  	<c:if test="${transFerGroup.status==1 }">待审核</c:if>
					   				<c:if test="${transFerGroup.status==2 }">审核成功</c:if>
					   				<c:if test="${transFerGroup.status==3 }">操作完成</c:if>
					   				<c:if test="${transFerGroup.status==4 }">取消申请</c:if>
					   				<c:if test="${transFerGroup.status==0 }">已驳回 </c:if>-->
					   				${fns:getNextReview(transFerGroup.reviewId)}
					   			</td>
					   			<td class="p0">
					   				<dl class="handle">
					   					<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
					   					<dd>
					   						<p>
												<span></span>
												<c:if test="${transFerGroup.status=='0'}">
						   							<a href="javascript:gotoGroupInfo('${transFerGroup.newGroupCode }','${transFerGroup.travelerId }','${orderId }','${transFerGroup.reviewId }')">审核详情 </a>
												</c:if>
												<c:if test="${transFerGroup.status=='1'}">
						   							<a href="javascript:gotoGroupInfo('${transFerGroup.newGroupCode }','${transFerGroup.travelerId }','${orderId }','${transFerGroup.reviewId }')">审核详情 </a>
													<a href="javascript:cancelGotoGroup('${transFerGroup.reviewId }')">取消审核</a>
												</c:if>
												<c:if test="${transFerGroup.status=='2'}">
						   							<a href="javascript:gotoGroupInfo('${transFerGroup.newGroupCode }','${transFerGroup.travelerId }','${orderId }','${transFerGroup.reviewId }')">审核详情 </a>
												</c:if>
												<c:if test="${transFerGroup.status=='3'}">
						   							<a href="javascript:gotoGroupInfo('${transFerGroup.newGroupCode }','${transFerGroup.travelerId }','${orderId }','${transFerGroup.reviewId }')">审核详情 </a>
												</c:if>
												<c:if test="${transFerGroup.status=='4'}">
						   							<a href="javascript:gotoGroupInfo('${transFerGroup.newGroupCode }','${transFerGroup.travelerId }','${orderId }','${transFerGroup.reviewId }')">审核详情 </a>
												</c:if>
											</p>
					   					</dd>
					   				</dl>
								</td>
					   		</tr>
					   	</c:forEach>
					   </c:if>
					   <c:if test="${empty list }">
					   		<td class="tc" colspan="8">暂无转团信息</td>
					   </c:if>
				   </tbody>
				</table>
                <div class="ydBtn"><a onclick="window.close();" class="ydbz_s gray">关闭</a></div>
				<!--右侧内容部分结束-->
</body>
</html>
