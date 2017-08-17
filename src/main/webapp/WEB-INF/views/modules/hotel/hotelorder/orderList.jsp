<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderHeadInfo.jsp"%>
</head>

<body>
<!-- 顶部菜单列表 -->
<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/topMenu.jsp"%>
	
<div class="activitylist_bodyer_right_team_co_bgcolor" >
	<!-- 搜索查询 -->
	<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderForm.jsp"%>
	<!-- 订单列表与团期列表转换、订单排序 -->
	<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/swithAndOrderByDiv.jsp"%>
	<!-- 订单列表 -->
	<table id="contentTable" class="activitylist_bodyer_table mainTable">
		<c:choose>
			<c:when test="${hotelOrderQuery.isOrder}">
				<!-- 订单标题 -->
				<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/orderThead.jsp"%>
				<!-- 订单列表 -->
				<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/showOrderList.jsp"%>
			</c:when>
			<c:otherwise>
				<!-- 团期列表 -->
				<%@ include file="/WEB-INF/views/modules/hotel/hotelorder/showGroupList.jsp"%>
			</c:otherwise>
		</c:choose>
	</table>
</div>
<div class="pagination clearFix">
   ${page}
</div>
</body>
</html>
