<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>申请退团
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
	
	$("#allChk").click(function(){
		if($(this).attr('checked') == 'checked'){
			$("input[name='activityId']").attr('checked','checked');
		}else{
			$("input[name='activityId']:checked").removeAttr('checked');
		}
	});
});

$(function(){
     //全选
     $("#allChk").click(function(){
		//所有checkbox跟着全选的checkbox走。
		$('[name=activityId]:checkbox').attr("checked", this.checked );
	 });
	 $('[name=activityId]:checkbox').click(function(){
		//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
		var $tmp=$('[name=activityId]:checkbox');
		//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
		$('#allChk').attr('checked',$tmp.length==$tmp.filter(':checked').length);
	 });
});
var submit_times = 0;

function submitForm() {
	if(submit_times != 0) {
		return ;
	}
	submit_times++;
	var str=[];
	var travelerId=[];
	var travelerName=[];
	var flag=true;
	$("input[name='activityId']:checked").each(function(){
		var exitReason =$($(this).parent().parent()).find("input[name='exitReason']").val().replace(/\s+/g,"");
		var travelerIds=$($(this).parent().parent()).find("input[name='travelerId']").val();
		var travelerNames=$($(this).parent().parent()).find("input[name='travelerName']").val();
		if (undefined == exitReason || "" == exitReason){
			exitReason = " ";
		}
		str.push(exitReason);
		travelerId.push(travelerIds);
		travelerName.push(travelerNames);
	});
	if (flag) {
		if (str.length > 0) {
			saveExitGroupInfo(str, travelerId, travelerName);
		} else {
			top.$.jBox.tip('请选择需退团的游客！', 'error', { focusId: 'name' });
		}
	}
}

/** 验证流程互斥情况，只有当无互斥流程及其他流程性操作时，发起退团审核流程，否则返回错误信息*/
function saveExitGroupInfo(str,travelerId,travelerName) {
	$.ajax({
		type: "POST",
		url: "${ctx}/islandOrder/saveExitGroupInfo",
		data: {
			exitReason : str.join(','),
			travelerId : travelerId.join(','),
			travelerName : travelerName.join(','),
			flowType : "${flowType}",
			orderUuid : "${orderUuid}"
		},
		success: function(msg){
			if (msg.result == 2) {
				window.location.href ="${ctx}/islandOrder/viewExitGroup?orderUuid=${orderUuid}";
			} else {
				top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
			}
		}
	});
}
</script>

</head>

<body>
<page:applyDecorator name="show_head">
    <page:param name="desc">退团</page:param>
</page:applyDecorator>
<div class="bgMainRight">
	<div class="mod_nav">订单 > 海岛游 > 退团详情</div>
	<!-- 订单信息 -->
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderIslandBaseinfo.jsp"%>
	<!-- 费用及人数 -->
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostAndNumInfo.jsp"%>
	<!-- 费用结算 -->
	<%@ include file="/WEB-INF/views/modules/island/islandorder/orderCostInfo.jsp"%>
	
	<div class="ydbz_tit pl20"><span class="fl">退团</span></div>
	<form id="submitForm" method="post" >
		<table id="contentTable" class="table activitylist_bodyer_table activitylist_bodyer_table_new">
			<thead>
				<tr>
					<th class="table_borderLeftN" width="5%">全选<input name="allChk" id="allChk" type="checkbox"></th>
					<th width="15%">游客</th>
					<th width="15%">舱位等级</th>
					<th width="15%">游客类型</th>
					<th width="20%">应收款金额</th>
					<th width="30%">备注</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${travelerList}" var="tra" varStatus="s">
					<tr>
						<td class="table_borderLeftN"><input name="activityId" type="checkbox"></td>
						<td class="tc">
							<c:if test="${tra.delFlag == 4 }">
								<div class="ycq yj" style="margin-top:1px;">
							 		转团审核中
								</div>
							</c:if>
							${tra.name}
						</td>
						<td class="tc">${tra.spaceLevelName}</td>
						<td class="tc">${tra.personTypeName}</td>
						<td class="tr"><span class="tdred">${tra.payPrice}</span></td>
						<td>
							<input type="text" name="exitReason"/>
							<input name="travelerId"value="${tra.id}" type="hidden">
							<input name="travelerName"value="${tra.name}" type="hidden">
						</td>					
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	
	<div class="dbaniu cl-both" >
		<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">取消</a>
		<a class="ydbz_s" href="javascript:submitForm()">提交</a>
	</div>
</div>

</body>
</html>
