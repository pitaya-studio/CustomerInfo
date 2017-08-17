<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转款-审批</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<!-- 静态资源 -->

<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">

function closeCurWindow(){
	this.close();
}
function reviewPass(rid){
		        
	var html = '<div class="add_allactivity"><label>请填写审批备注!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" name="denyReason" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "审批备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
		var remark = f.denyReason;
			$.ajax({
	            type: "POST",
	            url: "${ctx}/singlegrouporder/transfermoney/transferMoneyReview",
	            data: {
	            rid:rid,
	            result:1,
	            remark:remark
	            },
	            success: function(msg){
	            	if(msg.flag == 1){
	            		top.$.jBox.tip('操作成功！');
	            		window.opener.location.href = window.opener.location.href;
           				window.close();
	            	}else{
	            		top.$.jBox.tip(msg.message, 'error');
	            	}
	            }
			});
		}
	},height:250,width:500});
}
	//驳回
function jbox_bohui(rid){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" name="denyReason" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
		var remark = f.denyReason;
			$.ajax({
		            type: "POST",
		            url: "${ctx}/singlegrouporder/transfermoney/transferMoneyReview",
		            data: {
		            rid:rid,
		            result:0,
		            remark:remark
		            },
		            success: function(msg){
		            	if(msg.flag == 1){
		            		top.$.jBox.tip('操作成功！');
		            		window.opener.location.href = window.opener.location.href;
	           				window.close();
		            	}else{
		            		top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
		            	}
		            }
		        });
		}
	},height:250,width:500});
	
}
</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<input type="hidden" value="${orderId }" id="orderId">
		<div class="mod_nav">订单&nbsp;&nbsp;>
		<c:choose>
			<c:when test="${oldBean.productOrderCommon.orderStatus == 1}">单团</c:when>
			<c:when test="${oldBean.productOrderCommon.orderStatus == 2}">散拼</c:when>
			<c:when test="${oldBean.productOrderCommon.orderStatus == 3}">游学</c:when>
			<c:when test="${oldBean.productOrderCommon.orderStatus == 4}">大客户</c:when>
			<c:when test="${oldBean.productOrderCommon.orderStatus == 5}">自由行</c:when>
			<c:when test="${oldBean.productOrderCommon.orderStatus == 10}">游轮</c:when>
		</c:choose>
		&nbsp;&nbsp;>转款审批</div>
		
		<%@ include file="/WEB-INF/views/review/transfermoney/singlegroup/transferMoneyDetialsBaseInfo.jsp"%>
		<div class="ydbz_tit">
			<span class="fl">审批动态</span>
		</div>
		<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
		<div style="margin-top:20px;"></div>
		<div class="dbaniu">
			<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a>
			<input type="submit" id = "failBtn" value="驳回" class="btn btn-primary" onclick="jbox_bohui('${rid}');">
			<%-- <input name="denyReason" type="hidden"/> --%>
			<input type="submit" id = "succBtn" value="审批通过" class="btn btn-primary" onclick="reviewPass('${rid}');">
		</div>
	<!--右侧内容部分结束-->
	
</body>
</html>