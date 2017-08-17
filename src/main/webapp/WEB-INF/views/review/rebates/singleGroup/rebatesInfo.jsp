<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
    <meta name="decorator" content="wholesaler"/>
     <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			 <title>宣传费-详情</title>
		</c:when>
         <c:otherwise>
         		<title>返佣-详情</title>
          </c:otherwise>
     </c:choose>   
    <%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctxStatic}/review/rebates/singleGroup/rebatesList.js"></script>
	<style type="text/css">
		.rabateInfo {
			margin-left: 50px;
		}
	</style>
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

	<%--返佣对象信息--%>
	<c:if test="${not empty multiRebateObject and multiRebateObject eq true and not empty reviewInfos}">
		<div class="ydbz_tit">
			 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
       <c:choose>
      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			<span class="fl">宣传费对象</span>
		</c:when>
         <c:otherwise>
         	<span class="fl">返佣对象</span>
          </c:otherwise>
     </c:choose>   
		</div>
		<div>
			<span>
				<label>对象类型：</label>
				<c:choose>
					<c:when test="${reviewInfos.relatedObjectType eq 2}"><span>供应商</span></c:when>
					<c:otherwise><span>渠道</span></c:otherwise>
				</c:choose>
			</span>
			<span class="rabateInfo">
				<c:choose>
					<c:when test="${reviewInfos.relatedObjectType eq 2}">
						<label>供应商名称：</label>
						<span>${reviewInfos.relatedObjectName}</span>
					</c:when>
					<c:otherwise>
						<label>渠道名称：</label>
						<span>${reviewInfos.agentName}</span>
					</c:otherwise>
				</c:choose>
			</span>
			<c:if test="${reviewInfos.relatedObjectType eq 2}">
				<span class="rabateInfo">
					<label>账户类型：</label>
					<c:choose>
						<c:when test="${reviewInfos.rebatesObjectAccountType eq '2'}"><span>境外账户</span></c:when>
						<c:when test="${reviewInfos.rebatesObjectAccountType eq '1'}"><span>境外账户</span></c:when>
					</c:choose>
				</span>
				<span class="rabateInfo">
					<label>开户行名称：</label>
					<span>${reviewInfos.rebatesObjectAccountBank}</span>
				</span>
				<span class="rabateInfo">
					<label>账户号码：</label>
					<span>${reviewInfos.rebatesObjectAccountCode}</span>
				</span>
			</c:if>
		</div>
	</c:if>
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
			 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					 <th width="10%">预计宣传费金额</th>
					 <th width="12%">原宣传费金额</th>
					 <th width="13%">宣传费差额</th>
				</c:when>
		         <c:otherwise>
		         	 <th width="10%">预计返佣金额</th>
					 <th width="12%">原返佣金额</th>
					 <th width="13%">返佣差额</th>
		          </c:otherwise>
		     </c:choose>   
			 <th width="20%">备注</th>
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
		  </tr>
	   </tbody>
	</table>
	<div class="allzj tr f18">
	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		       <c:choose>
		      	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					原宣传费金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      					宣传费差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
				</c:when>
		         <c:otherwise>
		         	 原返佣金额：${fns:getHtmlMoneyAmountBySerialNum(rebates.oldRebates)}<br />
      				返佣差额：<font class="f14">${rebates.currency.currencyName}</font><span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${rebates.rebatesDiff}" /></span><br />
		          </c:otherwise>
		     </c:choose>   
	</div>
	
	<div class="ydbz_tit">
		<span class="fl">审批动态</span>
	</div>
	<c:set var="rid" value="${rebates.review.id}"></c:set>
	<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>	
	
	<div class="dbaniu" style="width:230px;">
		<a class="ydbz_s" href="javascript:window.close();">返回</a>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>