<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务-结算管理-结算单</title>

<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap.min.css"/>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jh-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/huanqiu-style.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-migrate-1.1.1.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
function locker(activityId,orderType,lockStatus,_this){
	   var $this = $(_this);
	   var message = (lockStatus == 0) ? "确认要锁定该结算单吗？" : "确认要解锁该结算单吗？";
	   var status = (lockStatus == 0) ? 1 : 0;
		$.jBox.confirm(message, "系统提示", function(v, h, f){
			if (v == 'ok') {
				$.ajax({
			        type: "POST",
			        url: "${ctx}/cost/manager/locker",
			        data: {
			        	activityId:activityId,
			        	orderType:orderType,
			        	lockStatus:status
			        },
			        success: function(msg){
			            top.$.jBox.tip('确定成功','success');
			            $this.remove();
			            location.replace(location.href);
			        }
		     	});
			}else if (v == 'cancel'){
				
			}
	});
}
</script>
</head>
<body>
<div id="sea">
	<!--右侧内容部分开始-->
	<div class="bgMainRight" style="background-image:none;">
		<h3 class="tc">结算单</h3>
		<div class="tr">
			<c:if test="${lockStatus == 0 }"><a href="javascript:void(0)" onclick="javascript:locker(${activityId},${orderType },${lockStatus },this);" class="dyzx-add">锁定</a></c:if>
			<c:if test="${lockStatus == 1 }"><a href="javascript:void(0)" onclick="javascript:locker(${activityId},${orderType },${lockStatus },this);" class="dyzx-add">解锁</a></c:if>
			<a href="${ctx}/cost/manager/downloadList?id=${activityId}&orderType=${orderType}&type=2" target="_blank" class="dyzx-add">下载</a>
			<a href="javascript:void(0)" onclick="javascript:window.print()" target="_self" class="dyzx-add">打印</a>
		</div>
		<div class="ydbz_tit">结算单基本信息</div>
		<div>
		<table border="0" width="90%">
			<tbody><tr>
			  <td class="mod_details2_d1">填表人：</td>
			  <td class="mod_details2_d2"><c:if test="${orderType eq '7' }">${activityAirTicket.createBy.name }</c:if>
              <c:if test="${orderType eq '6' }">${visaProducts.createBy.name }</c:if>
              <c:if test="${orderType ne  '6' && orderType ne  '7' }">${activityGroup.createBy.name }</c:if></td>
			  <td class="mod_details2_d1">操作部负责人：</td>
			  <td class="mod_details2_d2"><c:forEach items="${userList}" var="u">
              		${u.name }&nbsp;&nbsp;
              </c:forEach></td>
			  <td class="mod_details2_d1">销售：</td>
			  <c:forEach items="${orderList }" var="ol">
		   	  	<c:set var="createUserNames" value="${createUserNames}${ol.createUserName }  " />
		   	  </c:forEach>
              <td class="mod_details2_d2">${createUserNames }</td>
			</tr>
			<tr>
			  <td class="mod_details2_d1">团号：</td>
			  <td class="mod_details2_d2"><c:if test="${orderType ne  '6' && orderType ne  '7' }">${activityGroup.groupCode }</c:if></td></td>
			  <td class="mod_details2_d1">线路：</td>
			  <td>
              <c:if test="${orderType eq '6' }">${visaProducts.productName }</c:if>
              <c:if test="${orderType ne  '6' && orderType ne  '7' }">${travelActivity.acitivityName }</c:if></td>
			  <td class="mod_details2_d1">客人单位：</td>
			  <td class="mod_details2_d2"></td>
			</tr>
			<tr>
			  <td class="mod_details2_d1">人数：</td>
			  <td class="mod_details2_d2"><c:if test="${orderType eq '7' }">${activityAirTicket.reservationsNum}</c:if>
              <c:if test="${orderType ne  '6' && orderType ne  '7' }">${activityGroup.planPosition }</c:if></td>
			  <td class="mod_details2_d1">报价：</td>
			  <td class="mod_details2_d2"><c:if test="${orderType eq '7' }">人民币${activityAirTicket.settlementAdultPrice}
              </c:if><c:if test="${orderType eq '6' }">人民币${visaProducts.visaPrice}</c:if>
              <c:if test="${orderType ne  '6' && orderType ne  '7' }">${activityGroup.settlementAdultPrice }</c:if></td>
			  <td class="mod_details2_d1">领队：</td>
			  <td class="mod_details2_d2"><c:if test="${orderType ne  '6' && orderType ne  '7' }">${travelActivity.groupLead }</c:if></td>
			</tr>
			<tr>
			  <td class="mod_details2_d1">时间：</td>
			  <td class="mod_details2_d2"><c:if test="${orderType eq '7' }"><fmt:formatDate value="${activityAirTicket.startingDate}" pattern="yyyy-MM-dd"/> </c:if>
              <c:if test="${orderType eq '6' }"><fmt:formatDate value="${visaProducts.createDate}" pattern="yyyy-MM-dd"/> </c:if>
              <c:if test="${orderType ne  '6' && orderType ne  '7' }"><fmt:formatDate value="${activityGroup.groupOpenDate }" pattern="yyyy-MM-dd"/></c:if></td>
			  <td class="mod_details2_d1">汇率：</td>
			  <td class="mod_details2_d2"></td>
			  <td class="mod_details2_d1">接待社：</td>
			  <c:forEach items="${list }" var="ls">
		   	  	<c:set var="agents" value="${agents}${ls.agentName}  " />
		   	  </c:forEach>
              <td class="mod_details2_d2">${agents }</td>
			</tr>
		  </tbody>
		</table>
		</div>
		<div style="height:10px;"></div>
		
		<div class="ydbz_tit">境内支付明细</div>
		<table class="activitylist_bodyer_table">
		   <thead>
			  <tr>
				 <th width="20%">项目</th>
				 <th width="20%">单价</th>
				 <th width="20%">数量</th>
				 <th width="20%">金额</th>
				 <th width="20%">批发商</th>
			  </tr>
		   </thead>
		   <tbody>
			  <c:set var="totalPrice" value="0" />
		   	 <c:forEach items="${actualInList}" var="ac">
			  <tr>
				 <td>${ac.name }</td>
				 <c:forEach items="${curlist}" var="currency">
				 	<c:if test="${ac.currencyId == currency.id }">
				 		<c:set var="currencyName" value="${currency.currencyName }"></c:set>
				 		<c:set var="currencyExchangerateIn" value="${currency.currencyExchangerate }"></c:set>
				 	</c:if>
				 </c:forEach>
				 <td class="tr">${currencyName }<fmt:formatNumber  type="currency" pattern="#,##0.00" minFractionDigits="2" value="${ac.price }" /></td>
				 <td class="tr">${ac.quantity }</td>
				 <td class="tr">人民币<fmt:formatNumber  type="currency" pattern="#,##0.00" minFractionDigits="2" value="${ac.price * ac.quantity * currencyExchangerateIn}" /></td>
				 <td>${ac.supplyName }</td>
				 <c:set var="price" value="${ac.price * ac.quantity * currencyExchangerateIn}" />
				 <c:set var="totalPrice" value="${totalPrice + price}" />
			  </tr>
			  </c:forEach>
		   </tbody>
		</table>
		<div class="ydbz_foot">
			<div class="fr f16">合计金额：<c:if test="${totalPrice != 0}"><font class="gray14">人民币</font><span class="tdred"><fmt:formatNumber minFractionDigits="2" type="currency" pattern="#,##0.00" value="${totalPrice}" /></span></c:if></div>
		</div>
		<div style="height:10px;"></div>
		
		<div class="ydbz_tit">应付境外支付明细</div>
		<table class="activitylist_bodyer_table">
		   <thead>
			  <tr>
				 <th width="20%">项目</th>
				 <th width="20%">币种</th>
				 <th width="20%">外币金额</th>
				 <th width="20%">汇率</th>
				 <th width="20%">人民币金额</th>
			  </tr>
		   </thead>
		   <tbody>
			  <c:set var="totalOutPrice" value="0" />
		   <c:forEach items="${actualOutList}" var="ac">
			  <tr>
				 <td>${ac.name }</td>
				 <td>
				 <c:forEach items="${curlist}" var="currency">
				 	<c:if test="${ac.currencyId == currency.id }">
				 		${currency.currencyName }
				 		<c:set var="currencyName" value="${currency.currencyName }"></c:set>
				 		<c:set var="currencyExchangerate" value="${currency.currencyExchangerate }"></c:set>
				 	</c:if>
				 </c:forEach>
				 </td>
				 <td class="tr">${currencyName }<fmt:formatNumber  type="currency" pattern="#,##0.00" minFractionDigits="2" value="${ac.price * ac.quantity}" /></td>
				 <td class="tr">${currencyExchangerate }</td>
				 <td class="tr"><fmt:formatNumber  type="currency" pattern="#,##0.00" minFractionDigits="2" value="${ac.price * ac.quantity * currencyExchangerate}" /></td>
				 <c:set var="outPrice" value="${ac.price * ac.quantity * currencyExchangerate}" />
				 <c:set var="totalOutPrice" value="${totalOutPrice + outPrice}" />
			  </tr>
			</c:forEach>
		   </tbody>
		</table>
		<div class="ydbz_foot">
			<div class="fl f16">转款：</div>
			<div class="fr f16">合计金额：<c:if test="${totalOutPrice != 0}"><font class="gray14">人民币</font><span class="tdred"><fmt:formatNumber minFractionDigits="2" type="currency" pattern="#,##0.00" value="${totalOutPrice}" /></span></c:if></div>
		</div>
		<div style="height:10px;"></div>
		
		<div class="ydbz_tit">团队合计</div>
		<table class="activitylist_bodyer_table">
		   <thead>
			  <tr>
				 <th width="20%">收入合计</th>
				 <th width="20%">退款合计</th>
				 <th width="20%">实际收入</th>
				 <th width="15%">支出合计</th>
				 <th width="15%">毛利</th>
				 <th width="10%">毛利率</th>
			  </tr>
		   </thead>
		   <tbody>
			  <tr>
				 <td class="tr">${sumTotalMoney }</td>
				 <td class="tr">${sumBackMoney }</td>
				 <td class="tr">${sumAccountedMoney }</td>
				 <td class="tr"><c:if test="${totalPrice + totalOutPrice != 0}"><font class="gray14">人民币</font><fmt:formatNumber minFractionDigits="2" type="currency" pattern="#,##0.00" value="${totalPrice + totalOutPrice}" /></c:if></td>
				 <td class="tr"></td>
				 <td class="tr"></td>
			  </tr>
		   </tbody>
		</table>
		<c:if test="${not empty list}">
		<div style="height:10px;"></div> 
		<div class="ydbz_tit">经理确认</div>
		</c:if>
		<c:forEach items="${list }" var="ls">
		<div class="ydbz_foot">
			<div class="fl f16">${ls.agentName}</div>
		</div>
		<table class="activitylist_bodyer_table">
		<c:if test="${not empty ls.agentName}">
		   <thead>
			  <tr>
				 <th width="25%">销售名称</th>
				 <th width="25%">人数</th>
				 <th width="25%">金额</th>
			  </tr>
		   </thead>
		  </c:if>
		   <tbody>
		   <c:forEach items="${agentOrderList }" var="ol">
		   <c:if test="${ls.agentName eq ol.agentName}">
			  <tr>
				  <td>${ol.createUserName }</td>
				 <td>${ol.person_num }</td>
				 <td>${ol.amount }</td>
			  </tr>
			 </c:if>
			</c:forEach>
		   </tbody>
		</table>
		<c:if test="${not empty ls.agentName}">
		<table class="activitylist_bodyer_table">
		   <thead>
			  <tr>
				 <th width="25%">收入合计</th>
				 <th width="25%">支出合计</th>
				 <th width="25%">退/返款</th>
				 <th width="25%">毛利</th>
			  </tr>
		   </thead>
		   <tbody>
			  <tr>
				 <td>&nbsp;</td>
				 <td>&nbsp;</td>
				 <td>&nbsp;</td>
				 <td>&nbsp;</td>
			  </tr>
		   </tbody>
		</table>
		</c:if>
		</c:forEach>
		<div class="release_next_add">
		   <input type="button" value="关闭" onclick="javascript:window.close();" class="btn btn-primary">
		</div>
	</div>
	<!--右侧内容部分结束--> 
</div>


</body>
</html>
