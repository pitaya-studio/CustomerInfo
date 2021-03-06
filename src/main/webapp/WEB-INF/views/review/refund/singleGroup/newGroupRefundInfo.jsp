<!-- 
author:yunpeng.zhang
describe:单团类退款审批详情页
createDate：2015年12月2日
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>退款详情</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
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
a {
	display: inline-block;
}

* {
	margin: 0px;
	padding: 0px;
}

body {
	background: #fff;
	margin: 0px auto;
}

.pop_gj {
	padding: 10px 24px;
	margin: 0px;
	border-bottom: #b3b3b3 1px dashed;
	overflow: hidden;
}

.pop_gj dt {
	float: left;
	width: 100%;
}

.pop_gj dt span {
	float: left;
	width: 80px;
	text-align: right;
	color: #333;
	font-size: 12px;
	overflow: hidden;
	height: 25px;
	line-height: 180%;
}

.pop_gj dt p {
	float: left;
	width: 300px;
	color: #000;
	font-size: 12px;
	line-height: 180%;
}

.pop_xg {
	padding: 10px 4px;
	margin: 0px;
	overflow: hidden;
}

.pop_xg dt {
	float: left;
	width: 100%;
	margin-top: 10px;
	height: 30px;
}

.pop_xg dt span {
	float: left;
	width: 100px;
	text-align: right;
	color: #333;
	font-size: 12px;
	overflow: hidden;
	height: 30px;
	line-height: 30px;
}

.pop_xg dt p {
	float: left;
	width: 110px;
	color: #333;
	font-size: 12px;
	height: 30px;
	line-height: 30px;
	overflow: hidden;
	position: relative;
}

.pop_xg dt p font {
	color: #e60012;
	font-size: 12px;
}

.pop_xg dt p input {
	width: 60px;
	height: 28px;
	line-height: 28px;
	padding: 0px 5px 0px 18px;
	color: #403738;
	font-size: 12px;
	position: relative;
	z-index: 3;
}

.pop_xg dt p i {
	position: absolute;
	height: 28px;
	top: 2px;
	width: 10px;
	text-align: center;
	left: 5px;
	z-index: 5;
	font-style: normal;
	line-height: 28px;
}

.release_next_add button {
	cursor: pointer;
	border-radius: 4px;
}

.wpr20 label {
	width: 60px;
	text-align: right;
}

.fl {
	float: left;
}

.fr {
	float: right;
}

.ydbz_tit {
	background-color: #f3f3f3;
	line-height: 33px;
	color: #333333;
	font-weight: bold;
	padding-left: 32px;
	margin-top: 10px;
	margin-bottom: 10px;
	position: relative;
	*height: 33px;
	font-size: 14px;
	border-radius: 1px;
	overflow: hidden;
}

label {
	cursor: inherit;
}

.activitylist_bodyer_table tr td {
	text-align: center;
}
</style>
</head>
<body>
<div id="sea">
	<!--右侧内容部分开始-->
	<div class="mod_nav">
		<c:if test="${productType==1 }">单团</c:if>
		<c:if test="${productType==2 }">散拼</c:if>
		<c:if test="${productType==3 }">游学</c:if>
		<c:if test="${productType==4 }">大客户</c:if>
		<c:if test="${productType==5 }">自由行</c:if>
		<c:if test="${productType==10 }">游轮</c:if>
		&nbsp;&nbsp;>退款&nbsp;&nbsp;>退款详情
	</div>
	<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
	<div class="ydbz_tit">
		<span class="fl">
			<c:if test="${reviewDetail.travellerId ne '0'}">
				游客退款
			</c:if>
			<c:if test="${reviewDetail.travellerId eq '0'}">
				团队退款
			</c:if>
		</span>
		<span class="fr wpr20">
			报批日期：<fmt:formatDate value="${reviewDetail.createDate }" type="both" />
		</span>
	</div>
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="10%">
					<c:if test="${reviewDetail.travellerId ne '0'}">
						游客
					</c:if>
					<c:if test="${reviewDetail.travellerId eq '0'}">
						团队
					</c:if>
				</th>
				<th width="15%">款项</th>
                <th width="15%">币种</th>
                <th width="15%">应收金额</th>
                <th width="15%">退款金额</th>
                <th width="15%">退款原因</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>${reviewDetail.travelerName }</td>
				<td>${reviewDetail.refundName }</td>
				<td>${reviewDetail.currencyName }</td>
				<td>${reviewDetail.payPrice }</td>
				<td>${reviewDetail.currencyMark }${reviewDetail.refundPrice }</td>
				<td>${reviewDetail.remark }</td>
			</tr>
		</tbody>
	</table>
	<div style="margin-top: 20px;"></div>
	<div class="ydbz_tit">
		<span class="fl">审批动态</span>
	</div>
	<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
	<div class="ydBtn ydBtn2">
		<a class="ydbz_s gray"
			href="javascript:window.opener=null;window.close();">关闭</a>
	</div>
	<!--右侧内容部分结束-->
</div>
</body>
</html>
