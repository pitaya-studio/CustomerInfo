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
.wpr20 label{ width:60px; text-align:right;}
.fl{float:left;}
.fr{float:right;}
.ydbz_tit{
	background-color:#f3f3f3;
	line-height:33px;color:#333333;
	font-weight:bold;padding-left:32px;margin-top:10px;margin-bottom:10px;
	position:relative; *height:33px;
	font-size:14px; border-radius:1px;
	overflow:hidden;
}
label{ cursor:inherit;}
</style>

</head>

<body>
<div id="sea">
<div class="mod_nav">酒店&nbsp;&nbsp;>退团审核&nbsp;&nbsp;>审核详情</div>
<!-- 订单信息 -->

<!-- 费用及人数 -->

<!-- 费用结算 -->

<div class="ydbz_tit pl20"><span class="fl">申请退团</span></div>
<table id="contentTable" class="table activitylist_bodyer_table activitylist_bodyer_table_new">
	<thead>
		<tr>
			<th width="10%">游客</th>
			<th width="10%">舱位等级</th>
			<th width="10%">游客类型</th>
			<th width="20%">申请时间</th>
			<th width="20%">应收款金额</th>
			<th width="30%">退团原因</th>
		</tr>
	</thead>
	<tbody>
		<td class="tc">${hashMap.travelerInfo.name}</td>
		<td class="tc">${hashMap.travelerInfo.spaceLevelName}</td>
		<td class="tc">${hashMap.travelerInfo.personTypeName}</td>
		<td class="tc"><fmt:formatDate value="${hashMap.reviewInfo.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
		<td class="tr"><span class="tdgreen">${hashMap.travelerInfo.payPrice}</span></td>
		<td>${hashMap.reviewInfo.createReason}</td>	
	</tbody>
</table>

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
		<c:if test="${not empty hashMap.reviewLogInfo}">
			<c:forEach items="${hashMap.reviewLogInfo}" var="log" varStatus="s">
				<tr group="travler1">
				<td class="tc">${hashMap.travelerInfo.name}</td>
				<td class="tc">${hashMap.travelerInfo.spaceLevelName}</td>
				<td class="tc">${hashMap.travelerInfo.personTypeName}</td>
				<td class="tc">2015-05-01</td>
				<td class="tr">￥90</td>
				<td>身体原因</td>
				<td class="tc">张三</td>
				<td class="tc">未审批</td>
			</tr>
			</c:forEach>
		</c:if>
		<c:if test="${empty hashMap.reviewLogInfo}">
			<ul class="spdtai">
				<li>暂无审核动态</li>
			</ul>
		</c:if>
	</tbody>
</table>
<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a></div>
<!--右侧内容部分结束-->
</div>
</body>
</html>
