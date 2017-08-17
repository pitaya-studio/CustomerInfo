<%@ page contentType="text/html;charset=UTF-8"%>
<title>
	<c:if test="${empty showType or showType==0}">全部订单</c:if>
	<c:if test="${showType==5}">已收全款</c:if>
	<c:if test="${showType==4}">已收订金</c:if>
	<c:if test="${showType==1}">未收全款</c:if>
	<c:if test="${showType==2}">未收订金</c:if>
	<c:if test="${showType==3}">已占位</c:if>
	<c:if test="${showType==99}">已取消</c:if>
	<c:if test="${showType==111}">已删除</c:if>
	<c:if test="${showType==7}">待确认订单</c:if>
</title>
<meta name="decorator" content="wholesaler"/>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/airticketOrderList.js" type="text/javascript"></script>
<%--<link rel="stylesheet" type="text/css" href="${ctxStatic}/css/huanqiu-style.css" />--%>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<!-- 需求111 -->
<style type="text/css">
    .td-extend .handle {
    	background-image: none;
	}
	.xuanfu {
	    position: absolute;
	    /* right: -6px; */
	    top: 0px;
	    left: 0;
	    right:130px;
	}
	#hoverWindow {
	    text-align:left;
	    width:35em;
	    top: 29%;
	    left: 0%;
	    word-wrap: break-word;
	}
	.activity_team_top1 .team_top table thead tr th{
	    border-right: 1px solid #c1cde3;
	}
</style>
