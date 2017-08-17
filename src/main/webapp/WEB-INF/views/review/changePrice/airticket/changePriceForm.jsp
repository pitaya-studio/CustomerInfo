<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>    
    	<title>订单-机票-改价详情</title>
    
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<!-- 页面左边和上边的装饰 -->
		<meta name="decorator" content="wholesaler" />

		<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

		<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
		
		<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
		<script type="text/javascript" src="${ctxStatic}/review/changePrice/airticket/changePrice.js"></script>
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
                <div class="mod_nav">显示订单 > 机票 > 改价记录 > 改价申请</div>
                <%@ include file="/WEB-INF/views/modules/order/airticketOrderBaseInfo.jsp"%>
                
				<div class="ydbz_tit"><span class="fl">游客改价</span></div>
				<form:form action="${ctx}/changeprice/airticket/applyForUpAirPrices" method="post" id="form1">
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
				   </tbody>
				</table>
				<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
					<div class="ydbz_x fl re-storeall">全部还原</div>
				</div>
				
				<div class="allzj tr f18">
                	当前金额：
                	<span id="totalBefore">
                		<font class="f14" flag="bz" value="150">人民币</font>
                		<span class="f20">150</span>
                		<span class="tdgreen">+</span>
                		<font class="f14" flag="bz" value="150">美元</font>
                		<span class="f20">150</span>
                	</span><br />
                    <div class="all-money">改后总额：<span id="totalAfter"></span></div>
				</div>
				<div class="dbaniu" style="width:150px;">
					<a class="ydbz_s gray" href="${ctx}/changeprice/airticket/changePriceList?orderId=${orderId}&productType=${productType}&flowType=${flowType}">取消</a> 
					<input type="button" value="申请改价" class="btn btn-primary" onclick="check_airticket_uppricess();"> 
				</div>
				<input type="hidden" name="orderId" />
				<input type="hidden" name="productType" />
				<input type="hidden" name="flowType" />
				</form:form>
				<!--右侧内容部分结束-->
  </body>
</html>
