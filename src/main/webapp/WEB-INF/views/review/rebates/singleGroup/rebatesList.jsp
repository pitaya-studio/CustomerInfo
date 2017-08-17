<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
		<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
         <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			<title>宣传费记录</title>
		</c:when>
         <c:otherwise>
               <title>返佣记录</title>
          </c:otherwise>
     </c:choose>   
	<!-- 页面左边和上边的装饰 -->
	<meta name="decorator" content="wholesaler" />
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script type="text/javascript">
		$(function(){
			//操作浮框
			operateHandler();
		});
		
		/**
		 * 取消返佣申请
		 */
		function cancelRebates(reviewId) {
			if($("#inputId").val()=='049984365af44db592d1cd529f3008c3'){
				$.jBox.confirm("确定要取消宣传费申请吗？", "提示", function(v, h, f) {
					if (v == 'ok') {
						$.ajax({                 
							type : "POST",                 
							url : "${ctx}/rebatesNew/cancelRebates",                 
							data : {
								reviewId : reviewId
							},                
							error: function(request) {                     
								top.$.jBox.tip("取消失败","warning");
							},                 
							success: function(data) { 
								if (data.result == "success") {
									top.$.jBox.tip("取消成功","success");	
									window.location.href = "${ctx}/rebatesNew/list?orderId=${orderId}&orderType=${orderType}";
								} else {
									top.$.jBox.tip("取消失败","warning");
								} 
							}             
						});
					}
				});	
			}else{
			$.jBox.confirm("确定要取消返佣申请吗？", "提示", function(v, h, f) {
				if (v == 'ok') {
					$.ajax({                 
						type : "POST",                 
						url : "${ctx}/rebatesNew/cancelRebates",                 
						data : {
							reviewId : reviewId
						},                
						error: function(request) {                     
							top.$.jBox.tip("取消失败","warning");
						},                 
						success: function(data) { 
							if (data.result == "success") {
								top.$.jBox.tip("取消成功","success");	
								window.location.href = "${ctx}/rebatesNew/list?orderId=${orderId}&orderType=${orderType}";
							} else {
								top.$.jBox.tip("取消失败","warning");
							} 
						}             
					});
				}
			});	
			}
		}
	</script>
</head>
<body>
	<input type="hidden" value="${fns:getUser().company.uuid}" id="inputId">
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
				  <div class="mod_nav">订单 > ${orderTypeStr} > 宣传费记录</div>
				  <div class="ydbz_tit orderdetails_titpr">宣传费记录
				  <a class="ydbz_x" href="${ctx}/rebatesNew/rebatesPage?orderId=${orderId}&orderType=${orderType}">申请宣传费</a>
			</c:when>
            <c:otherwise>
                    <div class="mod_nav">订单 > ${orderTypeStr} > 返佣记录</div>
                    <div class="ydbz_tit orderdetails_titpr">返佣记录
                    <a class="ydbz_x" href="${ctx}/rebatesNew/rebatesPage?orderId=${orderId}&orderType=${orderType}">申请返佣</a>
             </c:otherwise>
        </c:choose>   
	</div>
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
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${rebates.createDate}"/></td>
					<td><c:if test="${not empty rebates.traveler}">${rebates.traveler.name}</c:if><c:if test="${empty rebates.traveler}">团队</c:if></td>
					<td>${rebates.currency.currencyName}</td>
					<td>${rebates.costname}</td>
					<td class="tr">
						<c:if test="${not empty rebates.traveler}">${fns:getMoneyAmountBySerialNum(rebates.traveler.payPriceSerialNum,1)}</c:if><!-- 个人 -->
						<c:if test="${empty rebates.traveler}">${fns:getMoneyAmountBySerialNum(rebates.totalMoney,1)}</c:if><!-- 团队 -->
					</td>
					<td class="tr">
						<c:if test="${not empty rebates.traveler}">${fns:getScheduleByUUID(rebates.traveler.rebatesMoneySerialNum)}</c:if><!-- 个人 -->
						<c:if test="${empty rebates.traveler}">${currencyMark} ${amount}</c:if><!-- 团队 -->
					</td>
					<td class="tr">${fns:getMoneyAmountBySerialNum(rebates.oldRebates,2)}</td>
					<td class="tr">${rebates.currency.currencyMark}${rebates.rebatesDiff}</td>
					<td class="invoice_yes">
						${fns:getChineseReviewStatus(rebates.review.status, rebates.review.currentReviewer)}
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
												<a href="${ctx}/rebatesNew/rebatesInfo?rebatesId=${rebates.id}" target="_blank">宣传费详情</a>
											</c:when>
								            <c:otherwise>
								                  <a href="${ctx}/rebatesNew/rebatesInfo?rebatesId=${rebates.id}" target="_blank">返佣详情</a>
								             </c:otherwise>
							         </c:choose>   
									<c:if test="${fns:isShowCancel(rebates.review.id)}">
										<a href="javascript:cancelRebates('${rebates.review.id}');">取消申请</a>
									</c:if>
								</p>
							</dd>
						</dl>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>
