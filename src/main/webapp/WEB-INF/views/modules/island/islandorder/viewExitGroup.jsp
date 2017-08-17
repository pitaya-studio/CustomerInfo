<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>退团记录</title>
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

function applyExitGroup(){
	window.location.href ="${ctx}/islandOrder/applyExitGroup?orderUuid=${orderUuid}";
}

function viewOrderInfo(productType, rid, orderUuid){
	window.open("${ctx}/islandOrder/viewApplyExitGroupInfo?orderUuid=" + orderUuid + "&rid=" + rid + "&productType=" + productType);
}

function cancleReview(rid) {
$.jBox.confirm("确定要取消审核吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
	                type: "POST",
	                url: "${ctx}/islandOrder/cancleAudit",
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

<page:applyDecorator name="show_head">
    <page:param name="desc">退团记录</page:param>
</page:applyDecorator>
<div class="bgMainRight">
	<div class="mod_nav">订单 > 海岛游 > 退团记录</div>
	<div class="filter_btn"><a class="btn btn-primary" href="javascript:void(0)" onclick="applyExitGroup()">申请退团</a> </div>
	<table id="contentTable" class="activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="10%">姓名</th>
				<th width="10%">舱位等级</th>
				<th width="10%">游客类型</th>
				<th width="15%">申请日期</th>
				<th width="10%">应收款</th>
				<th width="15%">备注</th>
				<th width="10%">申请人</th>
				<th width="10%">审批状态</th>
				<th width="10%">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page}" var="reviews" varStatus="s">
				<tr>
					<td class="tc">${reviews.travelerName}</td>
					<td class="tc">${reviews.spaceLevelName}</td>
					<td class="tc">${reviews.personTypeName}</td>
					<td class="tc">${reviews.createDate}</td>
					<td class="tr">${reviews.payPrice}</td>
					<td>${reviews.exitReason}</td>
					<td class="tc">${reviews.createByName}</td>
					<td class="tc tdgreen">审核通过</td>
					<td class="tc"><a href="javascript:viewOrderInfo('${productType}','${reviews.id}','${orderUuid}')">查看详情</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="ydBtn"><a class="ydbz_s" href="javascript:window.opener=null;window.close();">关闭</a></div>
</div>

</body>
</html>
