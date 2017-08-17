<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<title>借款记录</title>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
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
		top.$.jBox.tip('当前状态不能撤销');
		return false;
	}
	$.ajax({                 
		cache: true,                 
		type: "POST",                 
		url:g_context_url+ "/order/lendmoney/cancelBorrowAmount",                 
		data:{id:id},                
		async: false,                 
		error: function(request) {                     
			top.$.jBox.tip('操作失败');
		},                 
		success: function(data) { 
			 if(data!=""){
				 top.$.jBox.tip(data,'warning');
				 return false;}
			top.$.jBox.tip('操作成功');
			window.location.href=g_context_url+"/order/lendmoney/borrowAmountList";
		}             
	});
	}
	function cancleReview(rid){
$.jBox.confirm("确定要取消审核吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
	                type: "POST",
	                url: "${ctx}/orderCommon/manage/cancleAudit",
	                data: {
	                    id:rid
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
				<!--右侧内容部分开始-->
                <div class="mod_nav">订单&nbsp;&nbsp;>酒店&nbsp;&nbsp;>借款记录</div>
		<div class="filter_btn">
		<a class="btn btn-primary"
			href="${ctx}/island/review/applyHotelBorrowing?orderId=${orderId }&productType=11&orderUuid=${orderUuid}">申请借款</a>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table_new">
		<thead>
			<tr>
				<th width="7%">申请日期</th>
				<th width="7%">姓名</th>
				<th width="7%">游客类型</th>
				<th width="9%">游客结算价</th>
				<th width="9%">累计借款金额</th>
				<th width="9%">本次借款金额</th>
				<th width="6%">申请人</th>
				<th width="12%">备注</th>
				<th width="6%">审批状态</th>
				
				
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>
			<!-- 无查询结果 -->
			<c:if test="${fn:length(bAList) <= 0 }">
				<tr class="toptr">
					<td colspan="12" style="text-align: center;">暂无借款记录</td>
				</tr>
			</c:if>
			<c:forEach items="${bAList}" var="rebates">
				<tr>
					<td class="tc"><fmt:formatDate value="${rebates.applyDate }" pattern="yyyy-MM-dd" /></td>
					<td class="tc">${rebates.travelerName}</td>
					<td class="tc"><trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${rebates.personType}"/></td>
					<td class="tr">${fns:getHotelMoneyAmountBySerialNum(rebates.totalMoney,2)}</td>
					<td class="tr">${rebates.sumLendPrice}</td>
					<td class="tr">${rebates.currencyMark}${rebates.lendPrice}</td>
					<td class="tc tdgreen" title="">${fns:getUserNameById(rebates.createBy)}</td>
					<td>${rebates.remark}</td>
					<td class="tc tdgreen" title="">审核通过</td>
					
					<td class="tc">
						<a href="${ctx}/island/review/viewHotelBorrowingInfo?orderId=${orderId }&reviewId=${rebates.reviewId }&orderUuid=${orderUuid}" >借款详情</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>		
	<div class="ydBtn"><a class="ydbz_s" href="javascript:window.opener=null;window.close();">关闭</a></div>			
				
</body>
</html>
