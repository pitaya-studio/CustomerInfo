<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
    <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
         <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			<title>宣传费记录</title>
		</c:when>
         <c:otherwise>
               <title>返佣记录</title>
          </c:otherwise>
     </c:choose>   
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/modules/order/rebates/rebatesList.js" type="text/javascript"></script>
	<script>
		$(function(){
			operateHandler();
		});
	</script>
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
				  <div class="mod_nav">订单 > ${fns:getStringOrderStatus(orderType)} > 宣传费记录</div>
					<div class="ydbz_tit orderdetails_titpr">宣传费记录</div>
			</c:when>
            <c:otherwise>
                    <div class="mod_nav">订单 > ${fns:getStringOrderStatus(orderType)} > 返佣记录</div>
					<div class="ydbz_tit orderdetails_titpr">返佣记录</div>
             </c:otherwise>
        </c:choose>   
	<table id="contentTable" class="activitylist_bodyer_table">
	   <thead>
		  <tr>
			 <th width="10%">报批日期</th>
             <th width="8%">姓名</th>
			 <th width="8%">币种</th>
             <th width="10%">款项</th>
             <th width="10%">应收金额</th>
             	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		             <c:choose>
			         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
							 <th width="10%">预计宣传费金额</th>
							<th width="10%">原宣传费金额</th>
							<th width="10%">宣传费差额</th>
						</c:when>
			            <c:otherwise>
			                   <th width="10%">预计返佣金额</th>
							   <th width="10%">原返佣金额</th>
			 				   <th width="10%">返佣差额</th>
			             </c:otherwise>
		         </c:choose>   
			 <th width="10%">状态</th>
			 <th width="10%">备注</th>
			 <th width="5%">操作</th>
		  </tr>
	   </thead>
	   <tbody>
	   	<c:forEach items="${rebatesList}" var="rebates">
	   		<tr>
				<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd" value="${rebates.createDate}"/></td>
				<td><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if></td>
				<td>${rebates.currency.currencyName}</td>
				<td>${rebates.costname}</td>
				<td class="tr">${fns:getMoneyAmountBySerialNum(rebates.totalMoney,1)}</td>
	             <c:if test="${not empty rebates.traveler}"><td class="tr">${fns:getScheduleByUUID(rebates.traveler.rebatesMoneySerialNum) }</td></c:if><!-- 个人 -->
	             <c:if test="${empty rebates.traveler}"><td class="tr">${currencyMark} ${amount}</td></c:if><!-- 团队 -->
				<td class="tr">${fns:getMoneyAmountBySerialNum(rebates.oldRebates,2)}</td>
				<td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
				<td class="invoice_no" title="">
					${fns:getNextReview(rebates.review.id)}
				</td>
				<td>${rebates.remark}</td>
				<td class="p0">
					<dl class="handle">
						<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
						<dd class="">
							<p>
							<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
								<c:choose>
						         	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
										<a href="javascript:rebates.showDetail(${rebates.id});">宣传费详情</a>
									</c:when>
						            <c:otherwise>
						                  <a href="javascript:rebates.showDetail(${rebates.id});">返佣详情</a>
						             </c:otherwise>
							   </c:choose>   
								<c:if test="${rebates.review.status == 1}">
									<a href="javascript:rebates.cancleRebates(${rebates.review.id});">取消申请</a>
								</c:if>
							</p>
						</dd>
					</dl>
				</td>
			</tr>
	   	</c:forEach>
	   </tbody>
	</table>
	<div class="ydBtn"><a onclick="window.close();" class="ydbz_s gray">关闭</a></div>
	<!--右侧内容部分结束-->
</body>
</html>