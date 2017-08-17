<%@ page contentType="text/html;charset=UTF-8" %>

<input type="hidden" value="${productorder.quauqProductChargeRate}" id="quauqProductChargeRate" name="quauqProductChargeRate">
<input type="hidden" value="${productorder.partnerProductChargeRate}" id="partnerProductChargeRate" name="partnerProductChargeRate">
<input type="hidden" value="${productorder.quauqOtherChargeType}" id="quauqOtherChargeType" name="quauqOtherChargeType">
<input type="hidden" value="${productorder.quauqOtherChargeRate}" id="quauqOtherChargeRate" name="quauqOtherChargeRate">
<input type="hidden" value="${productorder.partnerOtherChargeType}" id="partnerOtherChargeType" name="partnerOtherChargeType">
<input type="hidden" value="${productorder.partnerOtherChargeRate}" id="partnerOtherChargeRate" name="partnerOtherChargeRate">
<input type="hidden" value="${productorder.cutChargeType}" id="cutChargeType" name="cutChargeType">
<input type="hidden" value="${productorder.cutChargeRate}" id="cutChargeRate" name="cutChargeRate">

<input type="hidden" value="${companyUuid}" id="companyUuid" name="companyUuid">
<input type="hidden" value="${RMB_currencyId}" id="RMB_currencyId" name="RMB_currencyId">

<input type="hidden" value="${productorder.id}" id="orderid" name="orderid" >
<input type="hidden" value="${productorder.orderNum}" id="orderNum" name="orderNum" >
<input type="hidden" value="${productorder.orderPersonNum}" id="orderPosition" name="orderPosition" >
<input type="hidden" value="" id="orgTotalPersonNum" />

<input type="hidden" value="${productorder.placeHolderType}" id="placeHolderType" name="placeHolderType">
<input type="hidden" value="${productorder.payMode}" id="payMode" name="payMode">
<input type="hidden" value="${productorder.remainDays}" id="remainDays" name="remainDays">
<input type="hidden" value="${productorder.payStatus}" id="payStatus" name="payStatus">
<input type="hidden" value="${groupHandleId}" id="groupHandleId" name="groupHandleId">

<input type="hidden" value="${product.id}" id="productId" name="productId">
<input type="hidden" value="${productGroup.id}" id="productGroupId" name="productGroupId">
<input type="hidden" value="${productGroup.freePosition}" id="freePosition" name="freePosition">
<input type="hidden" value="${productGroup.maxPeopleCount}" id="maxPeopleCount" name="maxPeopleCount"><!-- 特殊人群最高人数 -->
<input type="hidden" value="${productGroup.maxChildrenCount}" id="maxChildrenCount" name="maxChildrenCount"><!-- 儿童最高人数 -->
<input type="hidden" value="${currentPeopleCount}" id="currentPeopleCount" name="currentPeopleCount"><!-- 特殊人群已占位人数 -->
<input type="hidden" value="${currentChildrenCount}" id="currentChildrenCount" name="currentChildrenCount"><!-- 儿童已占位人数 -->
<input type="hidden" value="${empty groupReserve.leftpayReservePosition?0:groupReserve.leftpayReservePosition}" id="leftpayReservePosition">

<input type="hidden" value="${agentId}" id="agentId" name="agentId" >
<input type="hidden" value="${activityKind}" id="activityKind" name="activityKind" >
<input type="hidden" value="${travelerKind}" id="travelerKind" name="travelerKind" >
<input type="hidden" value="${priceType}" id="priceType" name="priceType" >

<input type="hidden" value="${adultCurrencyId}" id="crbz" name="adultCurrencyId">
<input type="hidden" value="${childCurrencyId}" id="etbz" name="childCurrencyId">
<input type="hidden" value="${specialCurrencyId}" id="tsbz" name="specialCurrencyId">

<input type="hidden" value="${adultCurrencyMark}" id="crbzm" name="adultCurrencyMark">
<input type="hidden" value="${childCurrencyMark}" id="etbzm" name="childCurrencyMark">
<input type="hidden" value="${specialCurrencyMark}" id="tsbzm" name="specialCurrencyMark">

<input type="hidden" value="${adultCurrencyName}" id="crbmc" name="adultCurrencyName">
<input type="hidden" value="${childCurrencyName}" id="etbmc" name="childCurrencyName">
<input type="hidden" value="${specialCurrencyName}" id="tsbmc" name="specialCurrencyName">

<input type="hidden" value="${priceType eq 2 ? retailAdultPrice : adultPrice}" id="crj" name="settlementAdultPrice">
<input type="hidden" value="${priceType eq 2 ? retailChildPrice : childPrice}" id="etj" name="settlementcChildPrice">
<input type="hidden" value="${priceType eq 2 ? retailSpecialPrice : specialPrice}" id="tsj" name="settlementSpecialPrice">

<input type="hidden" value="${payDepositCurrencyId}" id="payDepositCurrencyId" name="payDepositCurrencyId">
<input type="hidden" value="${payDeposit}" id="payDeposit" name="payDeposit">
<input type="hidden" id="frontMoney" name="frontMoney">

<input type="hidden" value="${singleDiffCurrencyId}" id="singleDiffCurrencyId" name="singleDiffCurrencyId">
<input type="hidden" value="${singleDiffPrice}" id="singleDiff" name="singleDiff">
<input id="ctx" type="hidden" value="${ctx}" />
<input id="orderDetailUrl" type="hidden" value="${ctx}/orderCommon/manage/orderDetail/${productorder.id}"/>
<input id="groupType" type="hidden" value="${productorder.orderStatus }" />
<input id="allowModifyAgentInfo" name="allowModifyAgentInfo" type="hidden" value="${allowModifyAgentInfo }" />
<input id="allowAddAgentInfo" name="allowAddAgentInfo" type="hidden" value="${allowAddAgentInfo }" />
<input id="contactsJsonStr" name="contactsJsonStr" type="hidden" value="${contactsJsonStr}" />
<input id="contactArray" name="contactArray" type="hidden" value="${contactArray }" />

<page:applyDecorator name="show_head">
    <page:param name="desc">订单${orderTitle}</page:param>
</page:applyDecorator>
<c:set var="groupType" value=""></c:set>
<c:choose>
	<c:when test="${productorder.orderStatus == 1}"><c:set var="groupType" value="单团"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 2}"><c:set var="groupType" value="散拼"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 3}"><c:set var="groupType" value="游学"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 4}"><c:set var="groupType" value="大客户"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 5}"><c:set var="groupType" value="自由行"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 10}"><c:set var="groupType" value="游轮"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 11}"><c:set var="groupType" value="酒店"></c:set></c:when>
	<c:when test="${productorder.orderStatus == 12}"><c:set var="groupType" value="海岛游"></c:set></c:when>
</c:choose>

<input type="hidden" value='${differenceFlag}' id="differenceFlag" name="differenceFlag">
<input type="hidden" value='${preOrderId}' id="preOrderId" name="preOrderId">
