<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<td>
	<div class="ycq">
		<c:choose>
			<c:when test="${orders.orderCompany == '-1'}">
				非签约渠道
			</c:when>
			<c:otherwise>
				签约渠道
			</c:otherwise>
		</c:choose>
	</div>
	${orders.orderCompanyName }
</td>
<td>${orders.orderNum }</td>
<td>
	<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
	<%-- <div class="kongfang_cen onshow" style="display:none">
		${orders.activitySerNum}
	</div> --%>
	<div class="chanpin_cen qtip onshow" title="${orders.activityName}">
		<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityHotel/showActivityHotelDetail/${orders.activityUuid}?type=product')">${orders.activityName}</a>
	</div>
</td>
<td>${orders.islandName}</td>
<td>
	${orders.hotelName}<br />
	<span class="y_xing">
		<c:forEach begin="1" end="${orders.hotelLevel}">
			★
		</c:forEach>
	</span>
</td>
<td>
	<c:forEach items="${orders.roomInfo}" var="room">
		<p>${room.roomName}*${room.roomNights}</p>
	</c:forEach>
</td>
<td class="tc">
	<c:forEach items="${orders.meals}" var="meal">
		<p>${meal}</p>
	</c:forEach>
</td>
<td class="tc">
	<c:forEach items="${fn:split(orders.islandWay,';')}" var="var">
		<p>
			<trekiz:defineDict name="island_way" type="islands_way" defaultValue="${var}" readonly="true" />
		</p>
	</c:forEach>
</td>
<td class="tr">${orders.orderPersonNum }</td>
<td>${orders.carateUserName}</td>
<td>${orders.activityCreateUserName}</td>
<td class="tc"><fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
<td class="tr">
	<c:choose>
		<c:when test="${orders.orderStatus == 1}">
			待确认报名
		</c:when>
		<c:when test="${orders.orderStatus == 2}">
			已确认报名
		</c:when>
		<c:otherwise>
			已取消
		</c:otherwise>
	</c:choose>
</td>
<td class="tr"><span class="tdorange"><center><c:if test="${orders.orderStatus==1 }">----</c:if><c:if test="${orders.orderStatus==3 }">----</c:if><span>
<c:if test="${orders.orderStatus==2 }"><fmt:formatDate value="${orders.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/>  </c:if> </center></span></td>
<td class="tr"><span class="tdorange">${orders.totalMoney}</span></td>
<td class="tr"><span class="tdorange">${orders.payedMoney}</span></td>
<td class="tr">
	<c:if test="${not empty orders.promptStr}">
		<div class="notice_price"><span>${orders.promptStr }</span></div>
	</c:if>
	<span class="tdorange">${orders.accountedMoney}</span>
</td>
<td class="tr"><span class="tdorange">${orders.notPayedMoney}</span></td>