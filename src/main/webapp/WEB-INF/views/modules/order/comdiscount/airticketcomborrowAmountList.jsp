<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		<title>订单-机票-宣传费记录</title>
	</c:when>
	<c:otherwise>
		<title>订单-机票-返佣记录</title>
	</c:otherwise>
</c:choose>

<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<!--<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />-->
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/modules/order/airticketcomdiscount/airticketComdiscount.js"></script>
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	contextPath="${ctx}";
	//操作浮框
	operateHandler();
	yd_dt_Handler();
	loadCurrency();
	
});
</script>
</head>
<body>
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		  <div class="mod_nav">订单 > 机票 > 宣传费记录</div>
	</c:when>
	<c:otherwise>
		  <div class="mod_nav">订单 > 机票 > 返佣记录</div>
	</c:otherwise>
</c:choose>   
  
    <input type="hidden" name="orderId" value="${orderId }"></input> 
<table id="contentTable" class="activitylist_bodyer_table">
	   <thead>
		  <tr>
			 <th width="10%">报批日期</th>
             <th width="8%">姓名</th>
             <th width="10%">款项</th>
             <th width="10%">应收金额</th>
             <!-- add by jiangyang -->
             	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					  <th width="10%">预计宣传费金额</th>
					  <th width="10%">累计宣传费金额</th>
					  <th width="10%">宣传费差额</th>
				</c:when>
				<c:otherwise>
					   <th width="10%">预计返佣金额</th>
					  <th width="10%">累计返佣金额</th>
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
				<td>
					<c:if test="${not empty rebates.traveler}">
						${rebates.traveler.name}
					</c:if>
					<c:if test="${empty rebates.traveler}">
						团队
					</c:if>
				</td>
				<td>${rebates.costname}</td>
				<td class="tr">
					<c:if test="${not empty rebates.traveler}">
						${rebates.rebatesdiffString2}
					</c:if>
					<c:if test="${empty rebates.traveler}">
						${tuantotalmoney }  
					</c:if>
				</td>
				<td class="tr">
					<c:if test="${empty fns:getPreRebatesApplied(rebates.rid)}">——</c:if>${fns:getPreRebatesApplied(rebates.rid)}
				</td>
				<td class="tr">
				${rebates.allCumulative }
				
				</td>
				<td class="tr">
 				      <c:set value="${rebates.rebatesdiffCurrName}" var="currencyNames" />
                      <c:set value=" ${rebates.rebatesdiffString}" var="rebatesDiffs" />
                      <c:forEach items="${fn:split(rebatesDiffs, ',')}" var="rebatesDiff" varStatus="i">
                        <p><span class="fbold tdorange"> 
                        ${fns:getCurrencyNameOrFlag(fn:split(currencyNames, ',')[i.index],"1")}
                        ${rebatesDiff }</p> 
                           </span>  
                      </c:forEach> 
				</td>
				<td class="invoice_no" title="">
					 ${fns:getNextReview(rebates.rid) }
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
											  <a href="javascript:rebates.showDetail(${rebates.rid});">宣传费详情</a>
										</c:when>
										<c:otherwise>
											  <a href="javascript:rebates.showDetail(${rebates.rid});">返佣详情</a>
										</c:otherwise>
									</c:choose>   
								
								<c:if test="${rebates.review.status == 1}">
									<a href="javascript:rebates.cancleRebates(${rebates.rid});">取消申请</a>
								</c:if>
							</p>
						</dd>
					</dl>
				</td>
			</tr>
	   	</c:forEach>
	   </tbody>
	</table>
				<!--右侧内容部分结束-->
</body>
</html>
