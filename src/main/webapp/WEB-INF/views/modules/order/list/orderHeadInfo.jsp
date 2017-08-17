<%@ page contentType="text/html;charset=UTF-8"%>
<title><c:if test="${showType==0}">全部订单</c:if><c:if test="${showType==5}">已收全款</c:if><c:if test="${showType==4}">已收订金</c:if><c:if test="${showType==1}">未收全款</c:if>
<c:if test="${showType==2}">未收订金</c:if><c:if test="${showType==3}">已占位</c:if><c:if test="${showType==101}">需达账审核</c:if>
<c:if test="${showType==99}">已取消</c:if>
<c:if test="${showType==111}">已删除</c:if>
<c:if test="${showType==7}">待确认订单</c:if>
  <c:choose>
		<c:when test="${youjiaCompanyUuid==companyUuid}">
			
		</c:when>
		<c:otherwise>
			<c:if test="${showType==1000}">预报名订单</c:if>
		</c:otherwise>
	</c:choose>
</title>
<meta name="decorator" content="wholesaler"/>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<%-- <script src="${ctxStatic}/js/jquery-ui.js" type="text/javascript"></script> --%>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/orderList.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.theme.css" />
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<!-- 需求223 -->
<shiro:hasPermission name="cruiseshipStockList:stock:orderboard">
	<c:if test="${orderStatus eq '10' }">
	    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
	    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
        <%--t2改版 去掉重复引用的样式 modified by Tlw--%>
	    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
	    <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	    <script type="text/javascript">
	    	var $ctx = '${ctx}';
	    </script>
		<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
	    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
	    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
	    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
	    <script type="text/javascript" src="${ctxStatic}/modules/store/billboard.js"></script>
    </c:if>
</shiro:hasPermission>
<!-- 需求223 -->
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


