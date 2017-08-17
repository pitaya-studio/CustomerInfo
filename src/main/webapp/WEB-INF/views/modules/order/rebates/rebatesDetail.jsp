<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			 <title>宣传费详情</title>
		</c:when>
         <c:otherwise>
         		<title>返佣详情</title>
          </c:otherwise>
     </c:choose>   
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/rebates/rebatesList.js" type="text/javascript"></script>
</head>
<body>
	<!-- 顶部参数 -->
    <page:applyDecorator name="show_head">
    	 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			 <page:param name="desc">宣传费详情</page:param>
		</c:when>
         <c:otherwise>
         		<page:param name="desc">返佣详情</page:param>
          </c:otherwise>
     </c:choose>   
	</page:applyDecorator>
	<!--右侧内容部分开始-->
		 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			  <div class="mod_nav">订单 > ${fns:getStringOrderStatus(productOrder.orderStatus)} > 宣传费记录 > 宣传费详情</div>
		</c:when>
         <c:otherwise>
         		 <div class="mod_nav">订单 > ${fns:getStringOrderStatus(productOrder.orderStatus)} > 返佣记录 > 返佣详情</div>
          </c:otherwise>
     </c:choose>   
	<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
	 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			  <div class="ydbz_tit"><span class="fl">宣传费审批</span></div>
		</c:when>
         <c:otherwise>
         		<div class="ydbz_tit"><span class="fl">返佣审批</span></div>
          </c:otherwise>
     </c:choose>   
	<table id="contentTable" class="activitylist_bodyer_table">
	   	<thead>
		  <tr>
			 <th width="8%">姓名</th>
	         <th width="12%">币种</th>
	         <th width="11%">款项</th>
			 <th width="12%">应收金额</th>
			 <!-- 20150813 预计个人/团队返佣金额 start -->
			  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					 	<th width="10%">预计宣传费金额</th>
			             <!-- 20150813 预计个人/团队返佣金额 end -->
						 <th width="12%">原宣传费金额</th>
						 <th width="13%">宣传费差额</th>
				</c:when>
		         <c:otherwise>
		         		<th width="10%">预计返佣金额</th>
			             <!-- 20150813 预计个人/团队返佣金额 end -->
						 <th width="12%">原返佣金额</th>
						 <th width="13%">返佣差额</th>
		          </c:otherwise>
		     </c:choose>   
			 <th width="20%">备注</th>
<!-- 	         <th width="13%">改后返佣金额</th> -->
		  </tr>
	  	</thead>
	   	<tbody>
		  <tr>
			 <td rowspan="2"><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if></td>
             <td>${rebates.currency.currencyName}</td>
			 <td class="tr">${rebates.costname}</td>
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.totalMoney,2)}</td>
			 <!-- 20150813 预计个人/团队返佣金额 start -->
			 <td class="tr">
			 	<c:if test="${not empty rebates.traveler }">
			 		<c:if test="${not empty rebates.traveler.rebatesMoneySerialNum }">${fns:getScheduleByUUID(rebates.traveler.rebatesMoneySerialNum) }</c:if>
			 		<c:if test="${empty rebates.traveler.rebatesMoneySerialNum }">-</c:if>
			 	</c:if><!-- 个人 -->
			 	<c:if test="${empty rebates.traveler}">
			 		<c:if test="${not empty amount}">${currencyMark} ${amount}</c:if>
			 		<c:if test="${empty amount}">-</c:if>
			 	</c:if><!-- 团队 -->
			 </td>
	         <!-- 20150813 预计个人/团队返佣金额 end -->
			 <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.oldRebates,2)}</td>
             <td class="tr">${rebates.currency.currencyMark}<fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></td>
			 <td>${rebates.remark}</td>
			<!-- <td class="tr">${fns:getMoneyAmountBySerialNum(rebates.newRebates,2)}</td> -->
		  </tr>
	   </tbody>
	</table>
	<div class="allzj tr f18">
		 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			 	宣传费佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
     		 	宣传费差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
		</c:when>
         <c:otherwise>
         		原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      			返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
          </c:otherwise>
     </c:choose>   
	<!--<div class="all-money">改后返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.newRebates)}</div> -->
	</div>
	<div class="ydbz_tit">
		<span class="fl">审核动态</span>
	</div>
	<ul class="spdtai">
		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			 	<li><fmt:formatDate value="${rebates.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> 【${rebates.createBy.name}】申请宣传费</li>
		</c:when>
         <c:otherwise>
         		<li><fmt:formatDate value="${rebates.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> 【${rebates.createBy.name}】申请返佣</li>
          </c:otherwise>
     </c:choose>   
		<c:forEach items="${reviewLogList}" var="reviewLog">
			<li><fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/> 【${fns:getUserById(reviewLog.createBy).name}】${reviewLog.result}</li>
		</c:forEach>
	</ul>
	<c:if test="${rebates.review.status == 0}">
		<div class="ydbz_tit">
			<span class="fl">驳回理由</span>
		</div>
		<ul class="spdtai">
			<li>${rebates.review.denyReason}</li>
		</ul>
	</c:if>
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">返回</a>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>