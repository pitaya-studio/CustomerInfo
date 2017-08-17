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
<!-- S109 优惠申请信息 -->
<input type="hidden" value='${priceJson}' id="priceJson">
<input type="hidden" value="" id="applyTravelerIds">
<!-- E109 优惠申请信息 -->
<!-- S109 团期优惠信息 -->
<input type="hidden" value="${adultDiscountCurrencyId}" id="adultDiscountCurrencyId" />
<input type="hidden" value="${childDiscountCurrencyId}" id="childDiscountCurrencyId" />
<input type="hidden" value="${specialDiscountCurrencyId}" id="specialDiscountCurrencyId" />
<input type="hidden" value="${adultDiscountPrice}" id="adultDiscountPrice" />
<input type="hidden" value="${childDiscountPrice}" id="childDiscountPrice" />
<input type="hidden" value="${specialDiscountPrice}" id="specialDiscountPrice" />
<!-- E109 团期优惠信息 -->

<input type="hidden" value="${productorder.id}" id="orderid" name="orderid" autocomplete="off">
<input type="hidden" value="${productorder.orderPersonNum}" id="orderPosition" name="orderPosition" >
<input type="hidden" value="${productorder.orderCompany}" id="orderCompany" name="orderCompany">
<input type="hidden" value="${productorder.orderCompanyName}" id="orderCompanyName" name="orderCompanyName">
<input type="hidden" value="${groupHandleId}" id="groupHandleId" name="groupHandleId">

<input type="hidden" value="${productorder.placeHolderType}" id="placeHolderType" name="placeHolderType">
<input type="hidden" value="${productorder.payMode}" id="payMode" name="payMode">
<input type="hidden" value="${productorder.remainDays}" id="remainDays" name="remainDays">
<input type="hidden" value="${productorder.payStatus}" id="payStatus" name="payStatus">
<input type="hidden" value="${agentSourceType}" id="agentSourceType" name="agentSourceType">

<input type="hidden" value="${product.id}" id="productId" name="productId">
<input type="hidden" value="${productGroup.id}" id="productGroupId" name="productGroupId">
<input type="hidden" value="${productGroup.freePosition}" id="freePosition" name="freePosition">
<input type="hidden" value="${empty groupReserve.leftpayReservePosition?0:groupReserve.leftpayReservePosition}" id="leftpayReservePosition">

<input type="hidden" value="${agentId}" id="agentId" name="agentId" >
<input type="hidden" value="${activityKind}" id="activityKind" name="activityKind" >
<input type="hidden" value="${travelerKind}" id="travelerKind" name="travelerKind" >

<!-- 使用价 -->
<input type="hidden" value="${adultCurrencyId}" id="crbz" name="adultCurrencyId">
<input type="hidden" value="${childCurrencyId}" id="etbz" name="childCurrencyId">
<input type="hidden" value="${specialCurrencyId}" id="tsbz" name="specialCurrencyId">

<input type="hidden" value="${adultCurrencyMark}" id="crbzm" name="adultCurrencyMark">
<input type="hidden" value="${childCurrencyMark}" id="etbzm" name="childCurrencyMark">
<input type="hidden" value="${specialCurrencyMark}" id="tsbzm" name="specialCurrencyMark">

<input type="hidden" value="${adultCurrencyName}" id="crbmc" name="adultCurrencyName">
<input type="hidden" value="${childCurrencyName}" id="etbmc" name="childCurrencyName">
<input type="hidden" value="${specialCurrencyName}" id="tsbmc" name="specialCurrencyName">

<input type="hidden" value="${adultPrice}" id="crj" name="settlementAdultPrice">
<input type="hidden" value="${childPrice}" id="etj" name="settlementcChildPrice">
<input type="hidden" value="${specialPrice}" id="tsj" name="settlementSpecialPrice">

<!-- 同行价 -->
<input type="hidden" value="${adultCurrencyId}" id="thcrbz" name="settlementAdultCurrencyId">
<input type="hidden" value="${childCurrencyId}" id="thetbz" name="settlementChildCurrencyId">
<input type="hidden" value="${specialCurrencyId}" id="thtsbz" name="settlementSpecialCurrencyId">

<input type="hidden" value="${adultCurrencyMark}" id="thcrbzm" name="settlementAdultCurrencyMark">
<input type="hidden" value="${childCurrencyMark}" id="thetbzm" name="settlementChildCurrencyMark">
<input type="hidden" value="${specialCurrencyMark}" id="thtsbzm" name="settlementSpecialCurrencyMark">

<input type="hidden" value="${adultCurrencyName}" id="thcrbmc" name="settlementAdultCurrencyName">
<input type="hidden" value="${childCurrencyName}" id="thetbmc" name="settlementChildCurrencyName">
<input type="hidden" value="${specialCurrencyName}" id="thtsbmc" name="settlementSpecialCurrencyName">

<input type="hidden" value="${adultPrice}" id="thcrj" name="settlementAdultPriceSrc">
<input type="hidden" value="${childPrice}" id="thetj" name="settlementChildPriceSrc">
<input type="hidden" value="${specialPrice}" id="thtsj" name="settlementSpecialPriceSrc">

<!-- 直客价 -->
<input type="hidden" value="${suggestAdultCurrencyId}" id="zkcrbz" name="suggestAdultCurrencyId">
<input type="hidden" value="${suggestChildCurrencyId}" id="zketbz" name="suggestChildCurrencyId">
<input type="hidden" value="${suggestSpecialCurrencyId}" id="zktsbz" name="suggestSpecialCurrencyId">

<input type="hidden" value="${suggestAdultCurrencyMark}" id="zkcrbzm" name="suggestAdultCurrencyMark">
<input type="hidden" value="${suggestChildCurrencyMark}" id="zketbzm" name="suggestChildCurrencyMark">
<input type="hidden" value="${suggestSpecialCurrencyMark}" id="zktsbzm" name="suggestSpecialCurrencyMark">

<input type="hidden" value="${suggestAdultCurrencyName}" id="zkcrbmc" name="suggestAdultCurrencyName">
<input type="hidden" value="${suggestChildCurrencyName}" id="zketbmc" name="suggestChildCurrencyName">
<input type="hidden" value="${suggestSpecialCurrencyName}" id="zktsbmc" name="suggestSpecialCurrencyName">

<input type="hidden" value="${suggestAdultPrice}" id="zkcrj" name="suggestAdultPrice">
<input type="hidden" value="${suggestChildPrice}" id="zketj" name="suggestChildPrice">
<input type="hidden" value="${suggestSpecialPrice}" id="zktsj" name="suggestSpecialPrice">

<!-- 推广价 -->
<input type="hidden" value="${retailAdultCurrencyId}" id="lscrbz" name="retailAdultCurrencyId">
<input type="hidden" value="${retailChildCurrencyId}" id="lsetbz" name="retailChildCurrencyId">
<input type="hidden" value="${retailSpecialCurrencyId}" id="lstsbz" name="retailSpecialCurrencyId">

<input type="hidden" value="${retailAdultCurrencyMark}" id="lscrbzm" name="retailAdultCurrencyMark">
<input type="hidden" value="${retailChildCurrencyMark}" id="lsetbzm" name="retailChildCurrencyMark">
<input type="hidden" value="${retailSpecialCurrencyMark}" id="lstsbzm" name="retailSpecialCurrencyMark">

<input type="hidden" value="${retailAdultCurrencyName}" id="lscrbmc" name="retailAdultCurrencyName">
<input type="hidden" value="${retailChildCurrencyName}" id="lsetbmc" name="retailChildCurrencyName">
<input type="hidden" value="${retailSpecialCurrencyName}" id="lstsbmc" name="retailSpecialCurrencyName">

<input type="hidden" value="${retailAdultPrice}" id="lscrj" name="retailAdultPrice">
<input type="hidden" value="${retailChildPrice}" id="lsetj" name="retailChildPrice">
<input type="hidden" value="${retailSpecialPrice}" id="lstsj" name="retailSpecialPrice">
 
<!-- 定金 -->
<input type="hidden" value="${payDepositCurrencyId}" id="payDepositCurrencyId" name="payDepositCurrencyId">
<input type="hidden" value="${payDeposit}" id="payDeposit" name="payDeposit">
<input type="hidden" id="frontMoney" name="frontMoney">

<!-- 单房差 -->
<input type="hidden" id="ctx" value="${ctx}">
<input type="hidden" value="${singleDiffCurrencyId}" id="singleDiffCurrencyId" name="singleDiffCurrencyId">
<input type="hidden" value="${singleDiffPrice}" id="singleDiff" name="singleDiff">

<input type="hidden" value="${productorder.salerId}" id="salerId" name="salerId">
<!-- 联系人json -->
<input type="hidden" value='${contactsJsonStr}' id="contactsJsonStr" name="contactsJsonStr">
<!-- 订单是否允许添加多个渠道联系人信息 -->
<input type="hidden" value='${allowAddAgentInfo}' id="allowAddAgentInfo" name="allowAddAgentInfo">
<!-- 订单是否允许渠道联系人信息输入和修改 -->
<input type="hidden" value='${allowModifyAgentInfo}' id="allowModifyAgentInfo" name="allowModifyAgentInfo">
<!-- 补位订单ID -->
<input type="hidden" value='${groupCoverId}' id="groupCoverId" name="groupCoverId">
<!-- 补位订单人数 -->
<input type="hidden" value='${groupCoverNum}' id="groupCoverNum" name="groupCoverNum">

<input type="hidden" value='${differenceFlag}' id="differenceFlag" name="differenceFlag">
<input type="hidden" value='${preOrderId}' id="preOrderId" name="preOrderId">
<input type="hidden" value='${allNum}' id="allNum" name="allNum">
<input type="hidden" value='${adultNum}' id="adultNum" name="adultNum">
<input type="hidden" value='${childNum}' id="childNum" name="childNum">
<input type="hidden" value='${specialNum}' id="specialNum" name="specialNum">
