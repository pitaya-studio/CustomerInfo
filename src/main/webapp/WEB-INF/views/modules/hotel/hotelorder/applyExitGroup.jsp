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

function submitForm() {
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
		url: "${ctx}/hotelOrder/saveExitGroupInfo",
		data: {
			exitReason : str.join(','),
			travelerId : travelerId.join(','),
			travelerName : travelerName.join(','),
			flowType : "${flowType}",
			orderUuid : "${orderUuid}"
		},
		success: function(msg){
			if (msg.result == 2) {
				window.location.href ="${ctx}/hotelOrder/viewExitGroup?orderUuid=${orderUuid}";
			} else {
				top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
			}
		}
	});
}
</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}
</style>

</head>

<body>
<div id="sea">
<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
<!--右侧内容部分开始-->
	<div class="ydbzbox fs">
		<div class="orderdetails">
	        <!-- 订单信息 -->
	
			<!-- 费用及人数 -->
	
			<!-- 费用结算 -->
			
			<div class="ydbz_tit">申请退团</div>
			<form id="submitForm" method="post" >
				<table id="contentTable" class="table activitylist_bodyer_table">
					<thead>
						<tr>
							<th class="table_borderLeftN" width="5%">全选<input name="allChk" id="allChk" type="checkbox"></th>
							<th width="20%">游客</th>
							<th width="15%">舱位等级</th>
							<th width="15%">游客类型</th>
							<th width="20%">应收款金额</th>
							<th width="30%">退团原因</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${travelerList}" var="tra" varStatus="s">
							<tr>
								<td class="table_borderLeftN"><input name="activityId" type="checkbox"></td>
								<td>
									<c:if test="${tra.delFlag == 4 }">
										<div class="ycq yj" style="margin-top:1px;">
									 		转团审核中
										</div>
									</c:if>
									${tra.name}
								</td>
								<td class="tr"><span class="tdred">${tra.spaceLevelName}</span></td>
								<td class="tr"><span class="tdred">${tra.personTypeName}</span></td>
								<td><fmt:formatDate value="${tra.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td class="tr"><span class="tdred">${tra.payPrice}</span></td>
								<td><input type="text" name="exitReason"/>
								<input name="travelerId"value="${tra.id}" type="hidden">
								<input name="travelerName"value="${tra.name}" type="hidden">
								</td>					
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</form>
		</div>
	<div class="ydBtn ydBtn2">
		<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a><a class="ydbz_s" href="javascript:submitForm()">提交</a></div>
	</div>
</div>
<!--右侧内容部分结束--> 
</div>
</body>
</html>
