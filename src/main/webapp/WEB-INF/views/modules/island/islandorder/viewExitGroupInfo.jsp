<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>退团审核详情
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
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


</script>
</head>

<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">退团详情</page:param>
</page:applyDecorator>
<div class="bgMainRight">
	<div class="mod_nav">订单 > 海岛游 > 退团详情</div>
	<!-- 订单信息 -->
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>
	<!-- 费用及人数 -->
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostAndNumInfo.jsp"%>
	<!-- 费用结算 -->
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostInfo.jsp"%>

	<div class="ydbz_tit pl20"><span class="fl">退团记录</span></div>
	<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new">
		<thead>
			<tr>
				<th width="10%">游客姓名</th>
				<th width="10%">舱位等级</th>
				<th width="10%">游客类型</th>
				<th width="15%">申请时间</th>
				<th width="15%">应收款</th>
				<th width="20%">退团原因</th>
				<th width="10%">申请人</th>
				<th width="10%">审批状态</th>
			</tr>
		</thead>
		<tbody>
			<c:if test="${not empty hashMap.travelerInfo}">
				<tr group="travler1">
					<td class="tc">${hashMap.travelerInfo.name}</td>
					<td class="tc">${hashMap.travelerInfo.spaceLevelName}</td>
					<td class="tc">
						<c:if test="${empty hashMap.travelerInfo.personTypeName }"></c:if>
						<c:if test="${not empty hashMap.travelerInfo.personTypeName }">
							${hashMap.travelerInfo.personTypeName}
						</c:if>
					</td>
					<td class="tc">
						<fmt:formatDate value="${hashMap.reviewInfo.createDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td class="tr">${hashMap.travelerInfo.payPrice}</td>
					<td>${hashMap.reviewInfo.createReason}</td>
					<td class="tc">${fns:getUserById(hashMap.reviewInfo.createBy).name}</td>
					<td class="tc tdgreen">审核通过</td>
				</tr>
			</c:if>
			<c:if test="${empty hashMap.travelerInfo}">
				<ul class="spdtai">
					<li>暂无审核动态</li>
				</ul>
			</c:if>
		</tbody>
	</table>
	<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a></div>
</div>

</body>
</html>
