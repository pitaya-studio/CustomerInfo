<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>上传完成</title>
	<meta name="decorator" content="wholesaler" />
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" rel="stylesheet" />
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
</head>
<body>


<!--右侧内容部分开始-->
         <div class="ydbzbox fs">
            <div class="payforDiv">
                <div class="payforok">
                    <div class="payforok-inner">
                        <h3 class="payforok-title">&nbsp;&nbsp;恭喜！凭证上传成功</h3>
							<table>
								<tr>
									<th>订单号</th>
									<th>付款方式</th>
									<th>总金额</th>
								</tr>
								<tr>
									<td>${hotelOrder.orderNum}</td>
									<td>${orderPay.payTypeName}</td>
									<td class="red"><span>${orderPay.moneySerialNum}</span>
									</td>
								</tr>
							</table>
							<p class="payforokbtn">
								<a href="${ctx}/hotelOrder/list/0.htm?orderNumOrGroupCode=${hotelOrder.orderNum}">我的订单</a>
								<a href="${ctx}/hotelOrder/hotelOrderDetail/${hotelOrder.uuid}"  target="_blank">查看订单详情</a>
								<a href="${ctx}" class="payforokbtn3" >回到首页</a>
							</p>
                    </div>
                </div>
                <div style="overflow:hidden">
                   <div class="kongr"></div>
                </div>
             </div>
         </div>
         <!--右侧内容部分结束--> 
</body>
</html>