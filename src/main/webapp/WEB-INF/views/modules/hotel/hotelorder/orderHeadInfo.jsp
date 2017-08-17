<%@ page contentType="text/html;charset=UTF-8"%>
<title><c:if test="${showType==0}">全部订单</c:if><c:if test="${showType==5}">已支付全款</c:if><c:if test="${showType==4}">已支付订金</c:if><c:if test="${showType==1}">未支付全款</c:if>
<c:if test="${showType==2}">未支付订金</c:if><c:if test="${showType==3}">已占位</c:if><c:if test="${showType==101}">需达账审核</c:if><c:if test="${showType==89}">已申请退团</c:if>
<c:if test="${showType==99}">已取消</c:if>
<c:if test="${showType==111}">已删除</c:if>
<c:if test="${showType==1000}">预报名订单</c:if>
</title>
<meta name="decorator" content="wholesaler"/>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<%-- <script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script> --%>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/hotelOrderList.js" type="text/javascript"></script>
