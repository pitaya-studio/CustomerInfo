<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>订单-改价列表</title>
	<!-- 页面左边和上边的装饰 -->
	<meta name="decorator" content="wholesaler" />
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script type="text/javascript">
		$(function(){
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
			//操作浮框
			operateHandler();

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
						tempObj[tempAttr[i]]=Number(tempValue[i].replace(/,/g, ""));
					})
				}
				return tempObj;
			}
			(function countToatl(){
				var returnObj={};
				$("tbody").find("tr").each(function(){
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
		
		/**
		 * 取消改价申请
		 */
		function cancelChangePrice(reviewId) {
			$.jBox.confirm("确定要取消改价申请吗？", "提示", function(v, h, f) {
				if (v == 'ok') {
					$.ajax({                 
						type : "POST",                 
						url : "${ctx}/newChangePrice/cancelChangePrice",                 
						data : {
							reviewId : reviewId
						},                
						error: function(request) {                     
							top.$.jBox.tip("取消失败","warning");
						},                 
						success: function(data) { 
							if (data.result == "success") {
								top.$.jBox.tip("取消成功","success");	
								window.location.href = "${ctx}/newChangePrice/list?orderId=${orderId}&productType=${productType}";
							} else {
								top.$.jBox.tip("取消失败","warning");
							} 
						}             
					});
				}
			});	
		}
	</script>
</head>
<body>
	<div id="sea">
		<div class="mod_nav">订单 > ${orderTypeStr} > 改价记录</div>
		<div class="ydbz_tit orderdetails_titpr">改价记录
			<a class="ydbz_x" href="${ctx}/newChangePrice/changePricePage?orderId=${orderId}&productType=${productType}">申请改价</a>
		</div>
		
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
					<th width="10%">审批动态</th>
					<th width="10%">备注</th>
					<th width="10%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${changePriceList}" var="changePrice">
					<tr>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${changePrice.createDate}"/></td>
						<td>${changePrice.travellerName}</td>
						<%--<td class="tc"><span>${changePrice.currencyname}</span></td>--%>
						<td  class="tc">
                                <c:forEach items="${fns:getMoneyBeforeCP(changePrice.travellerId,changePrice.orderId,changePrice.createDate)}" var="money" varStatus="count">
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
						<td class="invoice_yes">
							${fns:getChineseReviewStatus(changePrice.status, changePrice.currentReviewer)}
						</td>
						<td>
							<c:choose>
								<c:when test="${not empty changePrice.remark}">${changePrice.remark}</c:when>
								<c:otherwise>无</c:otherwise>
							</c:choose>
						</td>
						<td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"/></dt>
								<dd class="">
									<p>
										<span></span> 
										<a href="${ctx}/newChangePrice/changePriceInfo?reviewId=${changePrice.id}&orderId=${changePrice.orderId}&productType=${changePrice.productType}">改价详情</a>
										<c:if test="${fns:isShowCancel(changePrice.id)}">
									   	 	<a href="javascript:cancelChangePrice('${changePrice.id}');">取消申请 </a>
								   	 	</c:if>
									</p>
								</dd>
							</dl> 
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<div class="ydBtn">
		<%--<a onclick="window.close();" class="ydbz_s gray">关闭</a>--%>
		<input type="button" class="btn" value="关闭" onclick="window.close();">
	</div>
</body>
</html>
