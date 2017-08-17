<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${payTypeDesc}完成</title>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link rel="stylesheet" href="css/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div class="ydbzbox fs">
		<c:if test="${navigationImgFlag == 0}">
			<div class="ydbz yd-step5 ydbz-get">&nbsp;</div>
		</c:if>
		<div class="payforDiv">
			<div class="payforok">
				<div class="payforok-inner">
					<h3 class="payforok-title">${msg}</h3>
					<table>
						<tbody>
							<tr>
								<c:if test="${!empty orderNum}">
									<th>订单号</th>
								</c:if>
								<c:if test="${!empty refundMoneyTypeDesc}">
									<th>款项</th>
								</c:if>
								<c:if test="${!empty payee}">
									<th>收款单位</th>
								</c:if>
								<th>${payTypeDesc}方式</th>
								<th>总金额</th>
							</tr>
							<tr>
								<c:if test="${!empty orderNum}">
									<td>${orderNum}</td>
								</c:if>
								<c:if test="${!empty refundMoneyTypeDesc}">
									<td>${refundMoneyTypeDesc}</td>
								</c:if>
								<c:if test="${!empty payee}">
									<td>${payee}</td>
								</c:if>
								<td>${payTypeName}</td>
								<td>
									<c:if test="${mergePayFlag == 0}">
									<c:if test="${!empty currencyIdPrice}">
										<c:forEach items="${currencyIdPrice}" var="currencyId"
										varStatus="status">
											<c:forEach items="${curlist}" var="cur">
												<c:if test="${cur.id==currencyId}">
													<p>
														<span class="gray14">${cur.currencyMark}</span><span class="tdred f20"><fmt:formatNumber
												type="currency" pattern="#,##0.00"
												value="${dqzfprice[status.index]}"/></span>
													</p>
												</c:if>
											</c:forEach>
										</c:forEach>
									</c:if>
									</c:if>
									<c:if test="${mergePayFlag == 1}">
										${mergeCurrencyPrice}
									</c:if>
								</td>
							</tr>
						</tbody>
					</table>
					<p class="payforokbtn">
						<c:if test="${!empty orderDetailUrl}">
							<a href="${orderDetailUrl}" target="_blank">订单详情</a>
						</c:if>
						<c:if test="${!empty paymentListUrl}">
							<a href="${ctx}/${paymentListUrl} ">付款列表</a>
						</c:if>
						<a href="${ctx}">回到首页</a>
						<c:if test="${!empty entryOrderUrl}">
							<a class="payforokbtn3" href="${ctx}${entryOrderUrl}">进入订单</a>
						</c:if>
					</p>
				</div>
			</div>
			<p class="payforoktip">
				<c:if test="${!empty orderListUrl}">                      
					<span>提示：</span>尊敬的客户，恭喜您${payTypeDesc}成功，请登录“<a href="${ctx}${orderListUrl}">我的订单</a>”补充您的游客相关信息，感谢您的配合！
				</c:if>
			</p>
			<div style="overflow:hidden">
				<div class="kongr"></div>
			</div>
		</div>
	</div>
	<!--右侧内容部分结束-->
</body>
</html>
