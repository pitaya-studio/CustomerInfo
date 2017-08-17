<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>订单-签证-改价申请</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
  <%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/upPrices.js"></script>
<script type="text/javascript">
var ctx = '${ctx}';
$(function(){
	//input获得失去焦点提示信息显示隐藏
	inputTips();
	gaijia2('visa');
});
</script>


<style type="text/css">

      .disableCss{

pointer-events:none;

color:#afafaf;

cursor:default

}

   </style>

  </head>
  
  <body>
  <!-- 声明返回对象 -->
  <c:set var="resultMaps" value="${resultMaps}"></c:set>
  <c:set var="travelers" value="${resultMaps['travelers']}" scope="application"></c:set>
  <c:set var="visaMoney" value="${resultMaps['visaMoney']}"></c:set>
<!--右侧内容部分开始-->
                <!--币种模板开始-->
                <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
                      <c:forEach items="${curlist}" var="currency">
                         <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                     </c:forEach>
                </select>
                <!--币种模板结束-->
                <div class="mod_nav">订单-签证-改价申请</div>
				<div class="ydbz_tit"><span class="fl">游客改价</span></div>
				<form:form action="${ctx}/visaUpProces/applyForUpVisaPrices" method="post" id="form1">
				<table class="activitylist_bodyer_table modifyPrice-table">
				   <thead>
					  <tr>
						 <th width="7%">姓名</th>
                         <th width="13%">币种</th>
						 <th width="15%">原始签证费</th>
						 <th width="15%">当前签证费</th>
						 <th width="15%">签证费改价差额</th>
						 <th width="25%">备注</th>
                         <th width="15%">改后签证费</th>
					  </tr>
				   </thead>
				   <tbody>
				  		<c:choose>
				  		<c:when test="${resultMaps['html'] == ''}">
				  		<tr><td colspan="7" style="text-align: center;">暂无游客...</td></tr>
				  		</c:when>
				  		<c:otherwise>
				  			${resultMaps['html']}
				  		</c:otherwise>
				  		</c:choose>
				   </tbody>
				</table>
				<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
					<div class="ydbz_x fl re-storeall">全部还原</div>
                    <div class="fr f14 all-money">游客改价差额：<span id="totalTravelerPlus"></span></div>
				</div>
                <!--订金改价开始
                <div class="ydbz_tit"><span class="fl">团队签证改价</span></div>
                <table class="activitylist_bodyer_table modifyPrice-table" id="teamVisa">
				   <thead>
					  <tr>
						 <th width="7%">款项</th>
                         <th width="13%">币种</th>
						 <th width="15%">原始签证费</th>
						 <th width="15%">当前签证费</th>
						 <th width="15%">改价差额</th>
						 <th width="25%">备注</th>
                         <th width="15%">改后签证费</th>
					  </tr>
				   </thead>
				   <tbody>
				   <c:forEach var="visaMoney" items="${visaMoney}">
					<tr valign="middle">
						 <td><input type="hidden" name="djkx" value="签证费"/>签证费</td> 
						 <td><input type="hidden" name="djbz" value="${visaMoney['currencyid'] }"/><span name="gaijiaCurency">${visaMoney['currencyname'] }</span></td>
                         <td class="tr"><input type="hidden" name="djysdj" value="${visaMoney['oldtotalmoney'] }"/>${fns:getCurrencyInfo(visaMoney.currencyid,0,'mark')} <span class="tdorange fbold">${visaMoney['oldtotalmoney'] } </span></td>
                         <td class="tr"><input type="hidden" name="djdqdj" value="${visaMoney['curtotalmoney'] }"/>${fns:getCurrencyInfo(visaMoney.currencyid,0,'mark')} <span class="tdorange fbold" flag="beforedj">${visaMoney['curtotalmoney'] }</span></td>
						 <td class="tc"><input name="plusdj" type="text" class="" value="0.00" onkeyup="validNum(this)" onafterpast="validNum(this)" /></td>
                         <td class="tc"><!-- <input name="djremark" type="text" value="" /> -->
                         <!-- <textarea name="djremark" cols="180" rows="1" onclick="this.innerHTML=''">备注</textarea></td>
						 <td class="tr"><input type="hidden" name="djghdj" value="${visaMoney['curtotalmoney'] }"/>${fns:getCurrencyInfo(visaMoney.currencyid,0,'mark')} <span class="tdorange fbold" flag="afterdj">${visaMoney['curtotalmoney'] }</span></td>
					  </tr>
					  </c:forEach>
				   </tbody>
				</table> -->
                <!--订金改价结束
               <div style="margin-top:20px;"></div>
				<div class="ydbz_tit"><span class="fl">其它改价</span></div>
				<div>
                	<ol class="gai-price-ol">
                    	<li>
                        	<i><input type="text" name="teamkx" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>
                        	<i><select name="teamCurrency">
                            	   <c:forEach items="${curlist}" var="currency">
			                         <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
			                     </c:forEach>
                            </select></i>
                            <i><input type="text" name="teamMoney" class="gai-price-ipt1" flag="istips" value="0.00" onkeyup="validNum(this)" onafterpast="validNum(this)" value="" /><span class="ipt-tips ipt-tips2">费用</span></i>
                            <i><textarea name='teamremark' cols='180' rows='1' onclick="this.innerHTML=''">备注</textarea></i>
                            <i><a class="ydbz_s gai-price-btn">+增加</a></i>
                        </li>
                    </ol>
				</div>-->
                <!--<div class="ydbz_tit"><span class="fl">备注</span></div>
                <dl class="gai-price-tex"><dd><textarea class="" rows="" cols="" name=""></textarea></dd></dl> -->
                
				<div class="allzj tr f18">
                	当前金额：<span id="totalBefore"><font class="f14" flag="bz" value="150">人民币</font><span class="f20">150</span> <span class="tdgreen">+</span> <font class="f14" flag="bz" value="150">美元</font><span class="f20">150</span></span><br />
                    改价差额：<span id="totalPlus"></span><br />
                    <div class="all-money">改后总额：<span id="totalAfter"></span></div>
				</div>
				<div class="dbaniu" style="width:150px;">
					<a href="${ctx}/visaUpProces/list?orderId=${orderId}&productType=${productType}&flowType=${flowType}"  class="ydbz_s gray">取消</a>
					
					<a href="javascript:void(0)"    onclick="check_visa_uppricess1(this);"  class="ydbz_x">申请改价</a>
				</div>
				<input type="hidden" name="orderId" />
				<input type="hidden" name="productType" />
				<input type="hidden" name="flowType" />
				</form:form>
				<!--右侧内容部分结束-->
  </body>
</html>
