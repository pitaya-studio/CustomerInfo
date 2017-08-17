<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
   <head>
   <title>批发商开发票详情</title>
   <meta name="decorator" content="wholesaler"/>
   <%@include file="/WEB-INF/views/include/dialog.jsp" %>
   <style type="text/css">
.sort { color: #0663A2; cursor: pointer; }
</style>
   <script src="${ctxStatic}/jquery/jquery-1.9.1.js" type="text/javascript"></script>
   <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js" type="text/javascript"></script>
   <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js" type="text/javascript"></script>
   <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
   <%--	<script src="${ctxStatic}/jqueryUI/ui/i18n/jquery.ui.i18n.js" type="text/javascript"></script>--%>
   <script type="text/javascript">
	    
	    function closeself(){
	    	window.location.href = "${ctx}/invoice/limit/supplyinvoicelist?verifyStatus=1";
	    	window.close();	    	
	    }
	</script>
   </head>
   <body>
   <page:applyDecorator name="show_head">
       <page:param name="desc">批发商开发票详情</page:param>
   </page:applyDecorator>
<div class="sup_detail_top">


<ul class="team_co clearFix vote-ul">
            <li>
                  <label>发票号：</label>
       ${orderinvoice.invoiceNum}
             </li>
        
        <li>
                   <label>开票日期：</label>
      <fmt:formatDate pattern="yyyy-MM-dd" value="${orderinvoice.createDate}"/>
        </li>
        <li>
                 <label>开票类型：</label>
     ${invoiceTypes[orderinvoice.invoiceType]} 
        </li>
        <li>
                 <label>开票方式：</label>
      ${invoiceModes[orderinvoice.invoiceMode] }
        </li>
        <li class="clear"></li>
        <li>
               <label>开票客户：</label>
      ${orderinvoice.invoiceCustomer }</label>
        </li>
        <li>
                    <label>开票项目：</label>
      ${invoiceSubjects[orderinvoice.invoiceSubject] }
        </li>
        
        <li>
        
               <label>开票金额：</label>
      <fmt:formatNumber type="currency" pattern="#,##0.00" value="${orderinvoice.invoiceAmount }" />
        </li>
        </ul>
   </div>
  
<div style="margin-top:8px;">
      <table id="contentTable"  class="activitylist_bodyer_table" style="border-top:1px solid #dddddd">
      <thead>
            <tr>
            <th>团号</th>
            <th>出团日期</th>
            <th>订单号</th>
            <th>预订日期</th>
            <th>人数</th>
            <th>应收团款</th>
            <th>财务到账</th>
            <th>已开发票</th>
            <th>本次开票</th>
         </tr>
         </thead>
      <tbody>
            <tr>
            <td>${limit['groupCode']}</td>
            <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${limit['groupOpenDate']}"/></td>
            <td>${limit['orderNum']}</td>
            <td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${limit['orderTime']}"/></td>
            <td class="tr">${limit['orderPersonNum']}</td>
            <td class="tr"><c:if test="${not empty limit['totalPrices']}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalPrices']}" /></td>
            <td class="tr"><c:if test="${not empty limit['accountedMoney'] }">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['totalAsAcount'] }" /></td>
            <td class="tr"><c:if test="${not empty limit['usePrice']}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['usePrice']}" /></td>
            <td class="tr"><c:if test="${not empty limit['invoiceAmount']}">¥</c:if><fmt:formatNumber type="currency" pattern="#,##0.00" value="${limit['invoiceAmount']}" /></td>
         </tr>
         </tbody>
   </table>
   </div>
<div style="margin-top:8px;display: none">
      <table id="contentTable" class="table-invoice">
      <thead>
           	<tr>
	            <th>开票次数</th>
	            <th>付款日期</th>
	            <th>付款方式</th>
	            <th>付团款金额</th>
	            <th>财务达账金额</th>
	            <th>本次开票金额</th>
         	</tr>
         </thead>
      <tbody>
            <c:set var="len" value="${fn:length(detail)}">
               </c:set>
            
            <c:set var="totalPrice" value="0">
               </c:set>
               
            <c:set var="totalMoney" value="0">
               </c:set>
               
            <c:set var="accountedMoney" value="0">
               </c:set>
            
            <c:set var="invoicePrice" value="0">
               </c:set>
            
        <c:forEach items="${details}" var="detail" varStatus="s">
            <c:set var="totalPrice" value="${totalPrice+detail[5]}"></c:set>
            <c:set var="totalMoney" value="${detail[6]}"> </c:set>
            <c:set var="accountedMoney" value="${detail[7]}"></c:set>
            <c:set var="invoicePrice" value="${invoicePrice+detail[4]}"></c:set>
            
            	<tr <c:if test="${detail[8] %2 == 0 }">class="table-invoice-alt"</c:if> >
            	  <c:if test="${invoiceTimes ne detail[8]}"><td rowspan="${invoiceTimesMap[detail[8]]}" >${detail[8] }</td></c:if> 
                  <td><fmt:formatDate pattern="yyyy-MM-dd" value="${detail[0]}"/></td>
                  <td>${detail[1]}</td>
                  <td>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${detail[2]}" /></td>
                  <td>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${detail[5]}" /> </td>
                  <td>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${detail[4]}" /> </td>
               </tr>
            <c:set var="invoiceTimes" value="${detail[8]}"></c:set>
         </c:forEach>
            <tr>
            <td colspan="3">小计</td>
            <td>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${totalMoney}" /></td>
            <td>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${accountedMoney}" /> </td>
            <td>¥<fmt:formatNumber type="currency" pattern="#,##0.00" value="${invoicePrice}" /></td>
         </tr>
         </tbody>
   </table>  
   </div>
   <br/>
   <div class="ydBtn"><a class="ydbz_x" href="javascript:void(0)" onClick="javascript:window.close();" >关闭</a></div>
    <div style="clear:both"></div>
  
   

</body>
</html>