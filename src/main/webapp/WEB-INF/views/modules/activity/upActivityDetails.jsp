<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%
String path = request.getContextPath();
// String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
// String basePath = request.getHeader("X-Forwarded-Proto")+"://"+request.getServerName()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>订单-改价详情</title>
    
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
<style>
.mod_details2_d1, .mod_details2_d2 {
    vertical-align: top;
    padding-top: 4px;
    padding-bottom: 4px;
    overflow: hidden;
    text-overflow: ellipse;
    white-space: nowrap;}
</style>
<script type="text/javascript">
var ctx = '${ctx}';
$(function(){
	//input获得失去焦点提示信息显示隐藏
	inputTips();
	gaijia();
	
	//全选
	var $items = $(":checkbox[name='travelerId']");
	
	$("#checkedAllBox").click(function(){
		$(":checkbox[name='travelerId']").prop("checked",this.checked);
	});
	$items.click(function(){
		$("#checkedAllBox").prop("checked",($items.filter(":checked").length == $items.length));
	});
	
	var groupCodeVal = $("#groupCodeEle").text();
	if(groupCodeVal.length > 20) {
		groupCodeVal = groupCodeVal.substring(0, 20) + "...";
	}
	$("#groupCodeEle").text(groupCodeVal);
	//针对大唐国旅订单团号过长进行限制、
	var groupCodeVal = $(".orderdetails1").find("tr").eq(1).find("td").eq(3).text();
	if (groupCodeVal.length > 20) {
		groupCodeVal = groupCodeVal.substring(0, 20) + "...";
	}
	$(".orderdetails1").find("tr").eq(1).find("td").eq(3).text(groupCodeVal)
	
	
});
</script>
  </head>
  
  <body>
  <!-- 声明返回对象 -->
  <c:set var="resultMaps" value="${resultMaps}"></c:set>
  <c:set var="travelers" value="${resultMaps['travelers']}" scope="application"></c:set>
  <c:set var="frontmoney" value="${resultMaps['frontmoney']}"></c:set>
<!--右侧内容部分开始-->
                <!--币种模板开始-->
                <select name="currencyTemplate" id="currencyTemplate" style="display:none;">
                    <c:forEach items="${curlist}" var="currency">
                         <option value="${currency.id}" <c:if test="${airticket.currency_id==currency.id}">selected="selected"</c:if>>${currency.currencyName}</option>
                     </c:forEach>
                </select>
                <!--币种模板结束-->
                <div class="mod_nav">订单 > <c:set var="productType" value="${productTypeSecond}"></c:set><c:if test="${productType==1}">单团</c:if><c:if test="${productType==2}">散拼</c:if><c:if test="${productType==3}">游学</c:if><c:if test="${productType==4}">大客户</c:if><c:if test="${productType==5}">自由行</c:if><c:if test="${productType==10}">游轮</c:if> > 改价记录 > 改价申请</div>
                <!-- add by jyang -->
                <%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
                               
				<div class="ydbz_tit"><span class="fl">游客改价</span></div>
				<form:form action="${ctx}/activityUpProces/applyForUpAirPrices" method="post" id="form1">
				<table class="activitylist_bodyer_table modifyPrice-table">
				   <thead>
					  <tr>
					  	 <th width="7%" class="">全选</br><input type="checkbox" onclick="" id="checkedAllBox" name=""></th>
						 <th width="7%">姓名</th>	
                         <th width="13%">币种</th>
						 <th width="15%">原始应收价</th>
						 <th width="15%">当前应收价</th>
                         <th width="15%">改后应收价</th>
						 <th width="25%">备注</th>
					  </tr>
				   </thead>
				   <tbody>
				  		<!-- mod start by jyang -->
						<c:choose>
					  		<c:when test="${empty travelerList }">
					  			<tr>
					  				<td colspan="7" style="text-align: center;">暂无游客...</td>
					  			</tr>
					  		</c:when>
					  		<c:otherwise>
					  			<c:forEach var='traveler' items='${travelerList }' varStatus='statusTraveler'>
									<c:forEach var="moneyInfo" items='${traveler.travelers }' varStatus='status'>
										<tr group='travler${statusTraveler.count }'>																					
											<c:if test="${status.count==1 }">
												<td rowspan='${traveler.travelers.size() }' class="table_borderLeftN"><input type="checkbox" name="travelerId" value="${moneyInfo.id }" <c:if test="">checked="checked"</c:if> onclick=""></td>
												<td rowspan='${traveler.travelers.size() }'> ${traveler.travelername } </td>
											</c:if>
											<td>
												<input type='hidden' name='travelerids' value='${moneyInfo.id }'/>
												<input type='hidden' name='gaijiaCurency' value='${moneyInfo.currency_id }'/>
												<span name='gaijiaCurency'>${moneyInfo.currency_name }</span>
											</td>
											<td class='tr'>
												${fns:getCurrencyInfo(moneyInfo.currency_id,0,'mark')}
												<span class='tdorange fbold' flag='original'> ${moneyInfo.oldtotalmoney } </span>
											</td>
											<td class='tr'>
												${fns:getCurrencyInfo(moneyInfo.currency_id,0,'mark')}
												<span class='tdorange fbold' flag='beforeys'> ${moneyInfo.amount } </span>
											</td>
											<td class='tc'>
												<dl class='huanjia'>
													<dt>
														<input name='plusys' type='text' class='' value='${moneyInfo.amount }' onkeyup='validNum(this)' onafterpaste='validNum(this)' />
														<input name='plusysTrue' type='hidden' value='0.00' defaultValue='0.00' >
													</dt>
													<dd>
														<div class='ydbz_x' flag='appAll'>应用全部</div>
														<div class='ydbz_x gray' flag='reset'>还原</div>
													</dd>
												</dl>
											</td>
											<td class='tc'>
												<textarea name='travelerremark' cols='180' rows='1' onclick="this.innerHTML=''"></textarea>
											</td>
										</tr>
									</c:forEach>									
					  			</c:forEach>
								<tr>
									<td colspan='3'>总价</td>
									<td id='totalOriginal'>原始应收总价：</td>
									<td id='totalNowtime'>当前应收总价：</td>
									<td id='totalFuture'>改后应收总价：</td>
									<td></td>
								</tr>
							</c:otherwise>					  		
				  		</c:choose>
				  		<!-- mod end   by jyang -->
				  		
				   </tbody>
				</table>
				<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
					<div class="ydbz_x fl re-storeall">全部还原</div>
<!--                     <div class="fr f14 all-money">游客改价差额：<span id="totalTravelerPlus"></span></div> -->
				</div>
                <!--订金改价开始
                <div class="ydbz_tit"><span class="fl">订金改价</span></div>
                <table class="activitylist_bodyer_table modifyPrice-table">
				   <thead>
					  <tr>
						 <th width="7%">款项</th>
                         <th width="13%">币种</th>
						 <th width="15%">原始订金</th>
						 <th width="15%">当前订金</th>
						 <th width="15%">改价差额</th>
						 <th width="25%">备注</th>
                         <th width="15%">改后订金</th>
					  </tr>
				   </thead>
				   <tbody>
					<tr valign="middle">
						 <td><input type="hidden" name="djkx" value="${frontmoney['changedfund'] }"/>${frontmoney['changedfund'] }</td> 
						 <td><input type="hidden" name="djbz" value="${frontmoney['currencyname'] }"/><span name="gaijiaCurency">${frontmoney['currencyname'] }</span></td>
                         <td class="tr"><input type="hidden" name="djysdj" value="${frontmoney['oldtotalmoney'] }"/>${fns:getCurrencyInfo(frontmoney.currencyid,0,'mark')} <span class="tdorange fbold">${frontmoney['oldtotalmoney'] }</span></td>
                         <td class="tr" ><input type="hidden" name="djdqdj" value="${frontmoney['curtotalmoney'] }"/>${fns:getCurrencyInfo(frontmoney.currencyid,0,'mark')} <span class="tdorange fbold" flag="beforedj">${frontmoney['curtotalmoney'] }</span></td>
						 <td class="tc"><input name="plusdj" type="text" class="" value="0.00" onkeyup="validNum(this)" onafterpast="validNum(this)" /></td>
                         <td class="tc"><!-- <input name="djremark" type="text" value="" /> 
                         	<textarea name="djremark" cols="180" rows="1" onclick="this.innerHTML=''">备注</textarea>
                         </td>
						 <td class="tr"><input type="hidden" name="djghdj" value="${frontmoney['curtotalmoney'] }"/>${fns:getCurrencyInfo(frontmoney.currencyid,0,'mark')} <span class="tdorange fbold" flag="afterdj">${frontmoney['curtotalmoney'] }</span></td>
					  </tr>
				   </tbody>
				</table>-->
				<!--订金改价结束 -->
                <!-- <div style="margin-top:20px;"></div>
				<div class="ydbz_tit"><span class="fl">团队改价</span></div>
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
                               <textarea name='teamremark' cols='180' rows='1' onclick="this.innerHTML=''">备注</textarea>
                               <span class="ipt-tips ipt-tips2"></span></i>
                            <i><a class="ydbz_s gai-price-btn">+增加</a></i>
                        </li>
                    </ol>
				</div> -->
                <!--<div class="ydbz_tit"><span class="fl">备注</span></div>
                <dl class="gai-price-tex"><dd><textarea class="" rows="" cols="" name=""></textarea></dd></dl> -->
                <!--  -->
				<div class="allzj tr f18">
                	当前金额：<span id="totalBefore"><font class="f14" flag="bz" value=""></span><br/>
                    <div class="all-money">改后总额：<span id="totalAfter"></span></div>
				</div>
				<div class="dbaniu" style="width:150px;">
					<a class="ydbz_s gray" href="javascript:history.go(-1);" onclick="return confirm('是否确认取消该申请？');">取消</a>
					<input type="button" value="申请改价" class="btn btn-primary" onclick="check_activity_uppricess();">
				</div>
				<input type="hidden" name="orderId" />
				<input type="hidden" name="productType" value=""/>
				<input type="hidden" name="flowType" value=""/>
				</form:form>
				<!--右侧内容部分结束-->
  </body>
</html>
