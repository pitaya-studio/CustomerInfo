<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <title>返佣详情</title>
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
	    <page:param name="desc">返佣审批</page:param>
	</page:applyDecorator>
	<!--右侧内容部分开始-->
    <div class="mod_nav">审核 > 返佣申请 > 返佣审批</div>
	<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
	<div class="ydbz_tit">
		<span class="fl">
			<c:if test="${not empty rebates.traveler}">个人</c:if>
			<c:if test="${empty rebates.traveler}">团队 </c:if>返佣
		</span>
	</div>
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
			 <th width="12%">预计返佣金额</th>
             <!-- 20150813 预计返佣金额 start 
             <th width="10%">预计返佣金额</th>
              20150813 预计返佣金额 end -->
             <!-- 20150813 预计返佣金额 start -->
             <th width="10%">预计返佣金额</th>
             <!-- 20150813 预计返佣金额 end -->
			 <th width="12%">原返佣金额</th>
			 <th width="13%">返佣差额</th>
			 <th width="20%">备注</th>
<!-- 	         <th width="13%">改后返佣金额</th> -->
		  </tr>
	  	</thead>
	   	<tbody>
		  <tr>
			 <td rowspan="2"><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if></td>
             <td>${rebates.currency.currencyName}</td>
			 <td class="tr">${rebates.costname}</td>
			 <td class="tr">
			 	<c:if test="${not empty rebatesStr}">${rebatesStr}</c:if>
			 	<c:if test="${empty rebatesStr}">-</c:if>
			 </td>
			<!-- 20150812 预计返佣金额  start
			<td class="tr">
				<c:if test="${not empty rebates.traveler}">${fns:getScheduleByUUID(rebates.traveler.rebatesMoneySerialNum) }</c:if>
				<c:if test="${empty rebates.traveler}">${currencyMark} ${amount }</c:if>
			</td>
			20150812 预计返佣金额  end -->
			<!-- 20150812 预计返佣金额  start-->
			<td class="tr">
				<c:if test="${not empty rebates.traveler}">${fns:getScheduleByUUID(rebates.traveler.rebatesMoneySerialNum) }</c:if>
				<c:if test="${empty rebates.traveler}">${currencyMark} ${amount }</c:if>
			</td>
			<!-- 20150812 预计返佣金额  end -->
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.oldRebates,2)}</td>
             <td class="tr">${rebates.currency.currencyMark} <fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></td>
			 <td>${rebates.remark}</td>
<!-- 			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.newRebates,2)}</td> -->
		  </tr>
	   </tbody>
	</table>
	
	<div class="allzj tr f18">
      	原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      	返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
<!-- 		<div class="all-money">改后返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.newRebates)}</div> -->
	</div>
	<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.opener=null;window.close();">关闭</a>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>