<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>财务-财务审核-退签证押金审批</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type = "text/javascript">
	//驳回
	function jbox_bohui_refund(travelerId,revId,denyReason,orderId,orderTypeSub,moneyAmount,currencyId,flag){
		$("#failBtn").attr('disabled',true);
		$("#succBtn").attr('disabled',true);
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$.ajax({
	            	type : "post",
					url : "${ctx}/depositenew/depositeReview",
					data:{ 
						"travelerId":travelerId,
						"revId" : revId,
						"result" : 0,
						"denyReason" : f.reason,
						"orderId" : orderId,
						"orderTypeSub" : orderTypeSub,
						"moneyAmount" : moneyAmount,
						"currencyId" : currencyId
					},
					success : function(msg) {
						if(msg.flag == 'success'){
							window.opener.$("#searchForm").submit();
							window.close();
						} else {
							$.jBox.tip(msg.msg); 
							$("#failBtn").attr('disabled',false);
							$("#succBtn").attr('disabled',false);
						}
					}
	            });
			} else {
				$("#failBtn").attr('disabled',false);
				$("#succBtn").attr('disabled',false);
			}
		},height:250,width:500});
	}
	//审核通过
	function review(travelerId,revId,denyReason,orderId,orderTypeSub,moneyAmount,currencyId,flag){
		$("#failBtn").attr('disabled',true);
		$("#succBtn").attr('disabled',true);
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$.ajax({
	            	type : "post",
					url : "${ctx}/depositenew/depositeReview",
					data:{ 
						"travelerId":travelerId,
						"revId" : revId,
						"result" : 1,
						"denyReason" : f.reason,
						"orderId" : orderId,
						"orderTypeSub" : orderTypeSub,
						"moneyAmount" : moneyAmount,
						"currencyId" : currencyId
					},
					success : function(msg) {
						if(msg.flag == 'success'){
							window.opener.$("#searchForm").submit();
							window.close();
						} else {
							$.jBox.tip(msg.msg); 
							$("#failBtn").attr('disabled',false);
							$("#succBtn").attr('disabled',false);
						}
					}
	            });
			} else {
				$("#failBtn").attr('disabled',false);
				$("#succBtn").attr('disabled',false);
			}
		},height:250,width:500});
	}
</script>
</head>
<body>
				<!--右侧内容部分开始-->
				<div class="mod_nav"> 审核 > 退签证押金 > 退签证押金审批</div>
				<div class="ydbz_tit">订单详情</div>
				<div class="orderdetails1">
					<table border="0" width="98%" style="margin-left: 25px">
						<tbody>
							<tr>
								<td class="mod_details2_d1">下单人：</td>
								<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.ordercreate)}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${orderDetail.orderdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<td class="mod_details2_d1">团队类型：</td>
								<td class="mod_details2_d2">${fns:getDictLabel(orderDetail.prdtype,'order_type', 'null')}</td>
								<td class="mod_details2_d1">收客人：</td>
								<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.ordercreate)}</td>
			
							</tr>
							<tr>
								<td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${orderDetail.orderno}</td>
								<td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2">${orderDetail.groupno}</td>
								<td class="mod_details2_d1">销售：</td>
								<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.salerId)}</td>
							</tr>
							<tr>
								<td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2">
									<!-- ¥ --> <em class="tdred">${orderDetail.totalmoney}</em>
								</td>
								<td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
									<c:if test = "${orderDetail.orderstatus == 0}">全部</c:if>
									<c:if test = "${orderDetail.orderstatus == 1}">全款未收款 </c:if>
									<c:if test = "${orderDetail.orderstatus == 2}">订金未收款  </c:if>
									<c:if test = "${orderDetail.orderstatus == 3}">已占位 </c:if>
									<c:if test = "${orderDetail.orderstatus == 4}">订金已收款</c:if>
									<c:if test = "${orderDetail.orderstatus == 5}">已支付全款</c:if>
								</td>
								<td class="mod_details2_d1">操作人：</td>
								<td class="mod_details2_d2">${fns:getUserNameById(orderDetail.updateby)}</td>
							</tr>
						</tbody>
					</table>
				</div>
                <div class="ydbz_tit">
					<span class="fl">产品信息</span>
				</div>
                <div class="orderdetails1">
					<p class="ydbz_mc">${orderDetail.prdname}</p>
					<ul class="ydbz_info">
						<!-- <li><span>有效期：</span>15~30天</li> 干掉了-->
<!-- 						<li><span>签证国家：</span>${orderDetail.countryid}</li> -->
<!-- 						<li><span>签证类别：</span>${orderDetail.visatype}</li> -->
<!-- 						<li><span>领区：</span>${orderDetail.collarea}</li> -->
						<li><span>签证国家：</span>${country.countryName_cn }</li>
						<li><span>签证类别：</span>${visaType.label }</li>
						<li><span>领区：</span>
							<c:if test="${not empty orderDetail.collarea }">
						                           ${fns:getDictLabel(orderDetail.collarea,'from_area','')}
					         </c:if>
						</li>
						<li><span>应收价格：</span>${orderDetail.visapay}</li>
						<li><span>办签人数：</span>${orderDetail.tnum}人</li>
					</ul>
				</div>
				<div class="ydbz_tit">
					<span class="fl">退签证押金</span><span class="fr wpr20">${reviewdetail.createDate}</span>
        		</div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="12%">游客</th>
                          <th width="10%">币种</th>
                          <th width="20%">押金金额</th>
                          <th width="10%">已达账押金</th>
                          <th width="10%">申请金额</th>
                          <th width="30%">原因</th>
					  </tr>
				   </thead>
				   <tbody>
					  <tr>
						 <td rowspan="2">${reviewdetail.travelerName }</td>
						 <td>${reviewdetail.currencyName }</td>
                         <td class="tr">${reviewdetail.depositPrice }</td>
						 <td class="tr">${reviewdetail.payPrice }</td>
						 <td class="tr">${reviewdetail.refundPrice }</td>
						 <td>${reviewdetail.remark }</td>
					  </tr>
				   </tbody>
				</table>
				<div style="margin-top:20px;"></div>
				<div class="allzj tr f18">
                    <div class="all-money">退签证押金总金额：<font class="f14">${fns:getCurrencyInfo(reviewdetail.depositPriceCurrency,0,'mark')}</font><span class="f20">${reviewdetail.refundPrice }</span></div>
				</div>
				<c:if test="${flag == 0}">
					<div class="dbaniu">
						<form id="searchForm" action="${ctx}/deposite/depositeReview" method="post">
							<!-- 添加提交请求所需数据 -->
							<input type = "hidden" id = "travelerId" name="travelerId" value = "${reviewdetail.travelerId}"/>
							<input type = "hidden" id = "revId" name="revId" value = "${reviewdetail.id}"/>
<!-- 							<input type = "hidden" id = "nowlevel" name="nowlevel" value = "${nowlevel}"/> -->
							<input type = "hidden" id = "result" name="result"/>
							<input type = "hidden" id = "denyReason" name="denyReason"/>
							<input type = "hidden" id = "orderId" name="orderId" value = "${orderDetail.orderid}"/>
							<input type = "hidden" id = "orderTypeSub" name="orderTypeSub" value = "${orderDetail.prdtype}"/>
							<input type = "hidden" id = "moneyAmount" name="moneyAmount" value = "${reviewdetail.refundPrice}"/>
							<input type = "hidden" id = "currencyId" name="currencyId" value = "${reviewdetail.depositPriceCurrency}"/>
							<input type = "hidden" id = "flag" name="flag" value = "${flag}"/>
							<a class="ydbz_s gray" href = "javascript:window.close();">返回</a> <a class="ydbz_s"
								onclick="jbox_bohui_refund(${reviewdetail.travelerId},${reviewdetail.id},'',${orderDetail.orderid},${orderDetail.prdtype},${reviewdetail.refundPrice},${reviewdetail.depositPriceCurrency},${flag});">驳回</a> <input type="button" value="审核通过" onclick="review(${reviewdetail.travelerId},${reviewdetail.id},'',${orderDetail.orderid},${orderDetail.prdtype},${reviewdetail.refundPrice},${reviewdetail.depositPriceCurrency},${flag})"
								class="btn btn-primary">
						</form>
					</div>
				</c:if>	
				<c:if test="${flag == 1}">
					<div class=" ydbz_button">
                            <a  class="ydbz_x  gray"  href = "javascript:window.close();">返回</a>
                                                    </div>
				</c:if>
				<br/>
				<%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
				<!--右侧内容部分结束-->
</body>
</html>
