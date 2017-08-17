<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
	<div class="tuanhao_cen onshow">
		${orders.groupCode}
	</div>
	<div class="chanpin_cen qtip" title="${orders.activityName}">
		<a href="javascript:void(0)" onClick="javascript:window.open('${ctx}/activityIsland/showActivityIslandDetail/${orders.activityUuid}?type=product')">${orders.activityName}</a>
	</div>
</td>
<td>${orders.activityCreateUserName}</td>
<td>${orders.carateUserName}</td>
<td class="tc"><fmt:formatDate value="${orders.orderTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
<td class="tc">
	<div class="out-date"><fmt:formatDate value="${orders.groupOpenDate}" pattern="yyyy-MM-dd"/></div>
</td>
<td class="tr">${orders.orderPersonNum }</td>
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
<td class="tr"><span class="tdorange fbold"><center><c:if test="${orders.orderStatus==1 }">----</c:if><c:if test="${orders.orderStatus==3 }">----</c:if><span>
<c:if test="${orders.orderStatus==2 }"><fmt:formatDate value="${orders.applyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></c:if> </center></span></td>
<td class="tr"><span class="tdorange fbold">${orders.costMoney}</span></td>
<td class="tr"><span class="tdorange">${orders.totalMoney}</span></td>
<td class="tr"><span class="tdorange">${orders.payedMoney}</span></td>
<td class="tr">
	<c:if test="${not empty orders.promptStr}">
		<div class="notice_price"><span>${orders.promptStr }</span></div>
	</c:if>
	<span class="tdorange">${orders.accountedMoney}</span>
</td>
<td class="tr"><span class="tdorange">${orders.notPayedMoney}</span></td>