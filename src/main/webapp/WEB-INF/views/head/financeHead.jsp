<%@ page contentType="text/html;charset=UTF-8" %>
<%--<%@ include file="/WEB-INF/views/include/taglib.jsp"%>--%>
<script type="text/javascript">
    $(function(){
        <%--var showType = '<sitemesh:getProperty property="showType" />';--%>
        <%--var orderStatus = '<sitemesh:getProperty property="orderStatus" />';--%>
        <%--var current = '<sitemesh:getProperty property="current" />';--%>
        var showType = '${showType}';
        var orderStatus = '${orderStatus}';
        var current = '${current}';
        if(current == 'dealList'){
            showType = '89';
        }else if(current == 'agingList'){
            showType = '99';
        }else if(current == 'payList'){
            showType = '111';
        }else if(current == 'visaList'){
            showType = '1000';
        }else if(current == 'visaOrder'){
            showType = '1001';
        }else if(current == 'visa_batch_borrowmoney'){
            showType = '204';//签证批量借款
        }
        var mark = '';
        var markTail = '';
        switch (showType)
        {
            case '0' :
                mark = 'all';
                break;
            case '1' :
                mark = 'notPaid';
                markTail = 'notPaidAll';
                break;
            case '2' :
                mark = 'notPaid';
                markTail = 'notPaidHead';
                break;
            case '3' :
                mark = 'occupied';
                break;
            case '199' :
                mark = 'paid';
                markTail = 'paidHead';
                break;
            case '101' :
                mark = 'paid';
                markTail = 'paidAll';
                break;
            case '89' :
                mark = 'dealList';
                break;
            case '99' :
                mark = 'agingList';
                break;
            case '111' :
                mark = 'payList';
                markTail='payListCB';
                break;
            case '201':
                mark = 'payList';
                markTail='payListTK';
                 break;
            case '202':
                mark = 'payList';
                markTail='payListFY';
                 break;
            case '203':
                mark = 'payList';
                markTail='payListJK';
                break;
            case '204':
                mark = 'payList';
                markTail='payListBatchJK';
                break;
            case '205':
                mark = 'payList';
                markTail = 'payListServiceCharge';
                break;
            case '1000' :
            	mark = 'paid';
                markTail = 'paidVisa';
                break;
            case '1001' :
            	mark = 'paid';
                markTail = 'paidVisaOrder';
                break;
            case '':
            	mark = 'paid';
                markTail = 'paidOther';
                break;
        }
        if(mark != ''){
            $('#' + mark).addClass('active');
        }
        if(markTail != '' && mark != ''){
            $('#' + markTail).children('a').addClass('active');
            $('#' + mark).addClass('current');
            $('#' + mark).parent('ul').addClass('hasNav');
        }
        
        $(".mainMenu-ul_new").hover(function(){
			$(this).next().find("span").show();
		},function(){
// 			$(this).next().find("span").show();
		});

    });
    
    function visaReceivMoney(){
    	var timestmp = (new Date()).valueOf();
    	window.location.href="${ctx}/visaOrderPayLog/manage/showVisaOrderTravelPayList/199/1.htm?option=visaOrder&timestamp="+timestmp;
    }
      
</script>
<content tag="three_level_menu">
    <shiro:hasPermission name="order:list:paidForCheck">
        <c:set var="dzcount" value="${fns:getCountForOrderListDZ(1) }"></c:set>
        	  <c:set var="reservecount" value="${fns:getCountForOrderListDZ(2) }"></c:set>
        	  <c:set var="visacount" value="${fns:getCountForOrderListDZ(3) }"></c:set>
        	  <c:set var="visaordercount" value="${fns:getCountForOrderListDZ(4) }"></c:set>
        	   <c:set var="paidother" value="${fns:getNotAccountedCountOtherIncome() }"/>
        	  <c:set var="count" value="${dzcount+reservecount+visacount+visaordercount+paidother }"/>
        	  <c:set var="isShow" value="${fns:getUser().company.isNeedAttention}"></c:set>
			 
        <li id="paid" class="ernav"><a href="javascript:void(0);">收款确认<c:if test="${count>0 and isShow eq 1}"><em><span >${count}</span><p></p></em></c:if><i></i></a>
        	<dl>
        		<dt id="paidAll"><a class="mainMenu-ul_new" href="${ctx}/finance/manage/showFinanceList/101/1.htm?option=order">订单收款<c:if test="${dzcount>0 and isShow eq 1}"><em style="width: auto;"><span style="width: auto;">${dzcount }</span><p></p></em></c:if></a><span>丨</span></dt>
        		<dt id="paidHead"><a class="mainMenu-ul_new" href="${ctx}/finance/manage/showFinanceList/199/1.htm?option=reserve">切位收款<c:if test="${reservecount>0 and isShow eq 1}"><em style="width: auto;"><span style="width: auto;">${reservecount }</span><p></p></em></c:if></a><span>丨</span></dt>
        		<dt id="paidVisa"><a class="mainMenu-ul_new" href="${ctx}/finance/manage/showFinanceList/199/1.htm?option=visa">签证押金收款<c:if test="${visacount>0 and isShow eq 1}"><em style="width: auto;"><span style="width: auto;">${visacount }</span><p></p></em></c:if></a></a><span>丨</span></dt>
        		<dt id="paidVisaOrder"><a class="mainMenu-ul_new" onclick="visaReceivMoney();">签证订单收款<c:if test="${visaordercount>0 and isShow eq 1}"><em style="width: auto;"><span style="width: auto;">${visaordercount }</span><p></p></em></c:if></a><span>丨</span></dt>
        	    <dt id="paidOther"><a class="mainMenu-ul_new" href="${ctx}/orderCommon/manage/showOrderListForOther">其他收入收款<c:if test="${fns:getNotAccountedCountOtherIncome() > 0 and isShow eq 1}"><em><span>${fns:getNotAccountedCountOtherIncome()}</span><p></p></em></c:if></a></dt>
        	</dl>
        </li>
    </shiro:hasPermission>
    <shiro:hasPermission name="invoice:showOrderList:detail">
        <li id="dealList"><a href="${ctx}/finance/manage/showFinanceList/199/1.htm?option=detail">交易明细</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="invoice:showOrderList:account">
        <li id="agingList"><a href="${ctx}/finance/manage/showFinanceList/199/1.htm?option=account">账龄查询</a></li>
    </shiro:hasPermission>
    <shiro:hasPermission name="invoice:showOrderList:pay">
    	<c:set var="orderPayCount" value="${fns:getOrderPayCount() }"></c:set>
    	<c:set var="refundCount" value="${fns:getRefundCount() }"></c:set>
    	<c:set var="rebateCount" value="${fns:getRebateCount() }"></c:set>
    	<c:set var="borrowMoneyCount" value="${fns:getBorrowMoneyNotPayedCount()}"></c:set>
        <c:set var="batchBorrowMoneyCount" value="${fns:getBatchBorrowMoneyNotPayedCount()}"></c:set>
        <c:set var="serviceChargeCount" value="${fns:getServiceChargeCount()}"></c:set>
    	<c:set var="isShow" value="${fns:getUser().company.isNeedAttention}"></c:set>
        <li id="payList" class="ernav"><a href="javascript:void(0);">付款<c:if test="${orderPayCount+refundCount+rebateCount+borrowMoneyCount+batchBorrowMoneyCount+serviceChargeCount > 0 and isShow eq 1}"><em><span>${orderPayCount+refundCount+rebateCount+borrowMoneyCount+batchBorrowMoneyCount+serviceChargeCount}</span><p></p></em></c:if><i></i></a>
           <dl>
            <dt id="payListCB"><a class="mainMenu-ul_new" href="${ctx}/finance/manage/showFinanceList/199/1.htm?option=pay">成本付款<c:if test="${orderPayCount > 0 and isShow eq 1}"><em><span>${orderPayCount}</span><p></p></em></c:if></a><span>丨</span></dt>
       		<dt id="payListTK"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/payList/201">退款付款<c:if test="${refundCount > 0 and isShow eq 1}"><em><span>${refundCount}</span><p></p></em></c:if></a><span>丨</span></dt>
       		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			     <c:choose>
			    	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					      <dt id="payListFY"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/payList/202">宣传费付款<c:if test="${rebateCount > 0 and isShow eq 1}"><em><span>${rebateCount}</span><p></p></em></c:if></a><span>丨</span></dt>
					</c:when>
			       <c:otherwise>
			       		  <dt id="payListFY"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/payList/202">返佣付款<c:if test="${rebateCount > 0 and isShow eq 1}"><em><span>${rebateCount}</span><p></p></em></c:if></a><span>丨</span></dt>
			        </c:otherwise>
			    </c:choose>
       		    <!-- 此处只用于环球行借款，update by shijun.liu 详见：com.trekiz.admin.modules.sys.security.SystemAuthorizingRealm -->
                <!-- 除环球行和新行者之外的批发商都显示两个菜单 -->
               <shiro:hasPermission name="visa:common:borrowMoney">
                   <dt id="payListJK"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/payList/203">借款付款<c:if test="${borrowMoneyCount > 0 and isShow eq 1}"><em><span>${borrowMoneyCount}</span><p></p></em></c:if></a><span>丨</span></dt>
                   <dt id="payListBatchJK"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/borrowMoneyForTTSQZ/203">签证批量借款付款<c:if test="${batchBorrowMoneyCount > 0 and isShow eq 1}"><em><span>${batchBorrowMoneyCount}</span><p></p></em></c:if></a><span>丨</span></dt>
               </shiro:hasPermission>
                <!-- 新行者只显示公共的借款付款功能 -->
               <shiro:hasPermission name="xxz:common:borrowMoney">
                   <dt id="payListJK"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/payList/203">借款付款<c:if test="${borrowMoneyCount > 0 and isShow eq 1}"><em><span>${borrowMoneyCount}</span><p></p></em></c:if></a><span>丨</span></dt>
               </shiro:hasPermission>
               <!-- 环球行只显示批量借款付款菜单 -->
               <shiro:hasPermission name="tts:visa:borrowMoney">
       			    <dt id="payListJK"><a class="mainMenu-ul_new" href="${ctx}/costNew/payManager/borrowMoneyForTTSQZ/203">借款付款<c:if test="${batchBorrowMoneyCount > 0 and isShow eq 1}"><em><span>${batchBorrowMoneyCount}</span><p></p></em></c:if></a><span>|</span></dt>
               </shiro:hasPermission>
               <dt id="payListServiceCharge"><a class="mainMenu-ul_new" href="${ctx}/finance/serviceCharge/list">代收服务费付款<c:if test="${serviceChargeCount > 0 and isShow eq 1}"><em><span>${serviceChargeCount}</span><p></p></em></c:if></a></dt>
      	  </dl>
        </li>
    </shiro:hasPermission>
</content>