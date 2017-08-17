<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<input type="hidden" name="orderId" value="${orderId}" /> 
<input type="hidden" name="orderNum" value="${orderNum}" /> 
<input type="hidden" name="orderType" value="${orderType}" /> 
<input type="hidden" name="businessType" value="${businessType}" /> 
<input type="hidden" name="payPriceType" value="${payPriceType}" /> 
<input type="hidden" name="isCommonOrder" value="${isCommonOrder}" />
<input type="hidden" name="fileIDList" class="fileIDList" /> 
<input type="hidden" name="fileNameList" class="fileNameList" /> 
<input type="hidden" name="filePathList" class="filePathList" />
<input type="hidden" name="visaId" value="${visaId}" /> 
<input type="hidden" name="paymentStatus" value="" /> 
<input type="hidden" name="orderDetailUrl" value="${orderDetailUrl}" />
<input type="hidden" name="paymentStatusLbl" value="${paymentStatusLbl}" />