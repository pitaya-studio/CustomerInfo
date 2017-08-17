<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
	<head>
		<title>订单-改价详情</title>
		<!-- 页面左边和上边的装饰 -->
		<meta name="decorator" content="wholesaler" />
		<script type="text/javascript">
			$(function() {
				if (!Object.assignObj) {
					Object.defineProperty(Object, "assignObj", {
						enumerable: false,
						configurable: true,
						writable: true,
						value: function (target, firstSource) {
							"use strict";
							if (target === undefined || target === null)
								throw new TypeError("Cannot convert first argument to objZZect");
							var to = Object(target);
							for (var i = 1; i < arguments.length; i++) {
								var nextSource = arguments[i];
								if (nextSource === undefined || nextSource === null) continue;
								var keysArray = Object.keys(Object(nextSource));
								for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
									var nextKey = keysArray[nextIndex];
									var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
									var flag = to.hasOwnProperty(nextKey);
									if (desc !== undefined && desc.enumerable)
										if (flag) {
											to[nextKey] += nextSource[nextKey];
										} else {
											to[nextKey] = nextSource[nextKey];
										}
								}
							}
							return to;
						}
					});
				}

				function getObj($this){
					var tempObj={};
					if($this.find("span").length>0){
						var tempAttr=[];
						var tempValue=[];
						$this.find("span:even").each(function () {
							tempAttr.push($(this).text());
						});
						$this.find("span:odd").each(function () {
							tempValue.push($(this).text().trim())
						});
						$.each(tempAttr,function(i,v){
							tempObj[tempAttr[i]]=Number(tempValue[i].replace(",", ""));
						})
					}
					return tempObj;
				}
				(function countToatl(){
					var returnObj={};
					$("#cpbody").find("tr").each(function(){
						var innerHtml1='';
						var oldPrice=$(this).find("td").eq("2");
						var nowPrice=$(this).find("td").eq("4");
						var tempObj={};
						var oldPriceObj=getObj(oldPrice);
						var nowPriceObj=getObj(nowPrice);
						returnObj=Object.assignObj(nowPriceObj,oldPriceObj);
						for(var _a in returnObj){
							innerHtml1+='<span>'+_a+'</span> <span class="tdorange fbold">'+Number(returnObj[_a]).toFixed(2)+'</span>+';
						}
						innerHtml1=innerHtml1.slice(0,-1);
						$(this).find("td").eq("3").empty().html(innerHtml1);
					})
				})();
			});
		</script>
	</head>
<body>
	<div id="sea">
		<div class="mod_nav">订单 > ${orderStatusStr} > 改价详情</div>
		<!--右侧内容部分开始-->
		<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
		<div class="ydbz_tit orderdetails_titpr">改价详情</div>
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="12%">报批日期</th>
					<th width="8%">游客</th>
					<%--<th width="8%">币种</th>--%>
					<th width="10%">改前金额</th>
					<th width="10%">改后金额</th>
					<th width="10%">改价差额</th>
					<th width="10%">申请人</th>
					<th width="10%">备注</th>
				</tr>
			</thead>
			<tbody id="cpbody">
				<tr>
					<td class="tc">
						<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${changePrice.createDate}"/>
					</td>
					<td>${changePrice.travellerName}</td>
					<%--<td class="tc"><span>${changePrice.currencyname}</span></td>--%>
					<td class="tc">
						<%--<c:forEach items="${changePrice.payMoneyList}" var="payMoney" varStatus="c">--%>
							<%--<c:choose>--%>
								<%--<c:when test="${!c.last}">--%>
									<%--<span>${fns:getCurrencyInfo(payMoney.currencyId,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${payMoney.amount}" /></span> +--%>
								<%--</c:when>--%>
								<%--<c:otherwise>--%>
									<%--<span>${fns:getCurrencyInfo(payMoney.currencyId,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${payMoney.amount}" /></span>--%>
								<%--</c:otherwise>--%>
							<%--</c:choose>--%>
						<%--</c:forEach>--%>
							<c:forEach items="${fns:getMoneyBeforeCP(changePrice.travellerId,orderId,changePrice.createDate)}" var="money" varStatus="count">
								<c:choose>
									<c:when test="${not count.last}">
										<span>${fns:getCurrencyNameOrFlag(money.key, "0")}</span> <span class='tdorange fbold' flag='original'>${money.value}</span> +
									</c:when>
									<c:otherwise>
										<span>${fns:getCurrencyNameOrFlag(money.key, "0")}</span> <span class='tdorange fbold' flag='original'>${money.value}</span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
					</td>
					<td class="tc">

					</td>
					<td class="tc">
						<c:forEach items="${changePrice.moneyList}" var="money" varStatus="cc">
							<c:choose>
								<c:when test="${!cc.last}">
									<span>${fns:getCurrencyInfo(money.key,0,'mark')}</span> <span class="tdorange fbold">${money.value}</span> +
								</c:when>
								<c:otherwise>
									<span>${fns:getCurrencyInfo(money.key,0,'mark')}</span> <span class="tdorange fbold">${money.value}</span>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</td>
					<td class="tc">${fns:getUserNameById(changePrice.createBy)}</td>
					<td>
						<c:choose>
							<c:when test="${not empty changePrice.remark}">${changePrice.remark}</c:when>
							<c:otherwise>无</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</tbody>
		</table>

		<%--<div class="allzj tr f18">当前金额：--%>
			<%--<span id="totalBefore"><font class="f14" flag="bz" value="">--%>
				<%--<c:forEach items="${changePrice.originalMoneyList}" var="originalMoney" varStatus="c">--%>
					<%--<c:choose>--%>
						<%--<c:when test="${!c.last}">--%>
							<%--<span>${fns:getCurrencyInfo(originalMoney.currencyId,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${originalMoney.amount}" /></span> +--%>
						<%--</c:when>--%>
						<%--<c:otherwise>--%>
							<%--<span>${fns:getCurrencyInfo(originalMoney.currencyId,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${originalMoney.amount}" /></span>--%>
						<%--</c:otherwise>--%>
					<%--</c:choose>--%>
				<%--</c:forEach>--%>
			<%--</span>--%>
			<%--<br/>					--%>
			<%--<div class="all-money">改后总额：--%>
				<%--<span id="totalAfter">--%>
					<%--<c:forEach items="${changePrice.moneyList}" var="money" varStatus="cc">--%>
						<%--<c:choose>--%>
							<%--<c:when test="${!cc.last}">--%>
								<%--<span>${fns:getCurrencyInfo(money.key,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${money.value}" /></span> +--%>
							<%--</c:when>--%>
							<%--<c:otherwise>--%>
								<%--<span>${fns:getCurrencyInfo(money.key,0,'mark')}</span> <span class="tdorange fbold"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${money.value}" /></span>--%>
							<%--</c:otherwise>--%>
						<%--</c:choose>--%>
					<%--</c:forEach>--%>
				<%--</span>--%>
			<%--</div>--%>
		</div>
		
		<div class="ydbz_tit">
			<span class="fl">审批动态</span>
		</div>
		<c:set var="rid" value="${changePrice.id}"></c:set>
		<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>		
			
		<!--右侧内容部分结束-->
	<div class="ydBtn"><a class="ydbz_s" href="javascript:history.go(-1);">返回</a></div>
</body>
</html>
