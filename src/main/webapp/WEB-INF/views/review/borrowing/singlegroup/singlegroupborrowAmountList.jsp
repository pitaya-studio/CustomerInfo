<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>借款记录</title>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
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
function  cancelBorrowAmount(status,id){
	if (status!=1){
		top.$.jBox.tip('当前状态不能取消!');
		return false;
	}
	top.$.jBox.confirm('确认要取消流程申请吗？','系统提示',function(v){
		if(v=='ok'){
			$.ajax({                 
				cache: true,                 
				type: "POST",                 
				url:g_context_url+ "/singlegrouporder/lendmoney/cancelBorrowAmount",                 
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
						 window.location.href=g_context_url+"/singlegrouporder/lendmoney/borrowAmountList?flowType=19&productType="+${productType}+"&orderId="+${orderId };
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
		url:g_context_url+ "/singlegrouporder/lendmoney/beforeAddLendMoneyApply",                 
		data:{orderId:orderId},                
		async: false,                 
		error: function(request) {                     
			top.$.jBox.tip('操作失败');
		},                 
		success: function(data) { 
			  if(data!=""){
				 top.$.jBox.tip(data,'warning');
				 window.location.href=g_context_url+"/singlegrouporder/lendmoney/borrowAmountList?flowType=19&productType=7&orderId="+${orderId };
				 return false;}
			window.open(g_context_url+"/singlegrouporder/lendmoney/LendMoneyApply?orderId="+${orderId});
		}             
	});
} 
</script>
</head>
<body>
				<!--右侧内容部分开始-->
                <div class="ydbz_tit orderdetails_titpr">订单 > 
                <c:if test="${productType eq '1' }">
					单团
				</c:if>
				<c:if test="${productType eq '2' }">
					散拼
				</c:if>
				<c:if test="${productType eq '3' }">
					游学
				</c:if>
				<c:if test="${productType eq '4' }">
					大客户
				</c:if>
				<c:if test="${productType eq '5' }">
					自由行
				</c:if>
				<c:if test="${productType eq '10' }">
					游轮
				</c:if>
                 > 借款记录
                	<a href="${ctx}/singlegrouporder/lendmoney/applyBorrowing?orderId=${orderId}&productType=${productType}"  class="ydbz_x"   target="_blank"  >申请借款</a> 
                </div>
                 <input type="hidden" name="orderId" value="${orderId }"></input> 
				<table id="contentTable" class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th width="10%">报批日期</th>
							<th width="10%">游客/团队</th>
							<th width="12%">借款金额</th>
							<th width="12%">申请人</th>
							<th width="19%">状态</th>
							<th width="6%">操作</th>
						</tr>
					</thead>
					<tbody>
					<c:if test="${processList.size()<=0}">
		                 <tr class="toptr" >
		                 	<td colspan="15" style="text-align: center;"> 暂无搜索结果</td>
		                 </tr>
        		    </c:if>
					 <c:forEach var="plist" items="${processList}" >
					<tr>
						<td class="tc">
							<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${plist.applyDate }"/>
						</td>
						<td class="tc">${plist.travelerName}</td>
						<td class="tc">${plist.currencyIds}</td>
						<td>
							${fns:getUserNameByIds(plist.createBy)}
						</td>
						<td class="tc">
								${fns:getChineseReviewStatus(plist.status,plist.currentReviewer)}
						</td>
						<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
								<dd class="">
									<p>
										<span></span> 
											<a href="${ctx}/singlegrouporder/lendmoney/airticketLendMoneyByReviewId?orderId=${orderId }&reviewId=${plist.reviewId }&productType=${productType}" target="_blank">借款详情</a>
										<c:if test="${fns:isShowCancel(plist.reviewId)}">
											<a href="javascript:void(0)" onclick="cancelBorrowAmount(${plist.status },'${plist.reviewId }')">取消申请</a>
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
</body>
</html>
