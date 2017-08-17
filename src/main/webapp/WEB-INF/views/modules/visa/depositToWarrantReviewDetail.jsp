<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证押金转担保审批</title>
<meta name="decorator" content="wholesaler" />
    
<script type="text/javascript">

//驳回
function jbox_bohui(){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" id="denyReasonText" name="" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var denyReasonText = $("#denyReasonText").text();
			$("#denyReason").val(denyReasonText);
			$("#subForm").attr("action","${ctx}/review/deposit/reviewDispose?result=0");
			$("#subForm").submit();
		}
	},height:250,width:500});

}
</script>

</head>
<body>
	<div class="mod_nav"> 签证押金转担保审核记录</div>
	<div class="ydbz_tit">订单详情</div>
		<div class="orderdetails1">
             <table border="0" style="margin-left: 25px" width="98%">
                <tbody>
                            <tr>
                                <td class="mod_details2_d1">销售：</td>
                                <td class="mod_details2_d2">${visaOrder.salerName}</td>
                                <td class="mod_details2_d1">下单时间：</td>
                                <td class="mod_details2_d2">
                                   <c:if test="${not empty visaOrder.createDate}">
                         		      <fmt:formatDate pattern="yyyy-MM-dd" value="${visaOrder.createDate}"/>
                         	       </c:if></td> 
                                <td class="mod_details2_d1">团队类型：</td>
                                <td class="mod_details2_d2">${activityGroup.travelActivity.activityTypeName}</td>	
                                <td class="mod_details2_d1">收客人：</td>
                                <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                                       
                            </tr>
                            <tr> 
                                <td class="mod_details2_d1">订单编号：</td>
                                <td class="mod_details2_d2">${visaOrder.orderNo}</td>
                                <td class="mod_details2_d1">订单团号：</td>
                                <td class="mod_details2_d2">${visaOrder.groupCode}</td>
                                <td class="mod_details2_d1">下单人：</td>
								<td class="mod_details2_d2">${visaOrder.createBy.name}</td>
								<td></td>
								<td></td>
							</tr>
							<tr>
								<td class="mod_details2_d1">订单总额：</td>
                                <td class="mod_details2_d2">
                                  <em class="tdred">
                         			<c:if test="${not empty visaOrder.totalMoney}">
                         			${fns:getMoneyAmountBySerialNum(visaOrder.totalMoney,1) }
                         			</c:if>
                         		  </em>
                                </td>
                                <td class="mod_details2_d1">订单状态：</td>
                                <td class="mod_details2_d2">
                                	<c:if test="${not empty visaOrder.visaOrderStatus}">
										<c:choose>
											<c:when test="${visaOrder.visaOrderStatus eq '0'}">未收款</c:when>
											<c:when test="${visaOrder.visaOrderStatus eq '1'}">已收款</c:when>
											<c:when test="${visaOrder.visaOrderStatus eq '2'}">已取消</c:when>
										</c:choose>
									</c:if></td>	 
                                <td class="mod_details2_d1">操作人：</td>
                                <td class="mod_details2_d2">${fns:getUser().name}</td> 
								<td></td>
								<td></td>    
                            </tr>
                        </tbody>
         	</table>	
        </div>
        <div class="ydbz_tit">
	        <span class="fl">签证押金转担保</span>
			<span class="fr wpr20">报批日期：${fn:substring(reviewMap.createDate,0,10)} </span>
		</div>
		<table id="contentTable" class="activitylist_bodyer_table">
			<thead>
				<tr>
					<th width="10%">游客</th>
					<th width="10%">销售</th>
					<th width="10%">下单时间</th>
					<th width="10%">币种</th>
					<th width="10%">押金金额</th>
					<th width="10%">签证状态</th>
					<th width="10%">转担保类型</th>
					<th width="20%">原因</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>${reviewMap.travelerName}</td>
					<td>${fns:getUserById(reviewMap.createBy).name}</td>
					<td class="tc">
						<c:if test="${not empty visaOrder.createDate}">
                       		<fmt:formatDate pattern="yyyy-MM-dd" value="${visaOrder.createDate}"/>
                       	</c:if>
					</td>
					<td>${reviewMap.priceCurrency}</td>
					<td class="tr">${reviewMap.price}</td>
					<td>
						<c:if test="${not empty reviewMap.visaStatus}">
							<c:choose>
								<c:when test="${reviewMap.visaStatus eq '0'}">未送签</c:when>
								<c:when test="${reviewMap.visaStatus eq '1'}">送签</c:when>
								<c:when test="${reviewMap.visaStatus eq '2'}">约签</c:when>
								<c:when test="${reviewMap.visaStatus eq '3'}">出签</c:when>
								<c:when test="${reviewMap.visaStatus eq '4'}">申请撤签</c:when>
								<c:when test="${reviewMap.visaStatus eq '5'}">撤签成功</c:when>
								<c:when test="${reviewMap.visaStatus eq '6'}">撤签失败</c:when>
								<c:when test="${reviewMap.visaStatus eq '7'}">拒签</c:when>
							</c:choose>
						</c:if>
					</td>
					<td>
						<c:if test="${not empty reviewMap.warrantType}">
							<c:choose>
								<c:when test="${reviewMap.warrantType eq 1}">担保</c:when>
								<c:otherwise>担保+押金</c:otherwise>
							</c:choose>
						</c:if>
					</td>
					<td>${reviewMap.reasonMark}</td>
				</tr>
			</tbody>
		</table>
			    <%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
			<div class="dbaniu">
				<a class="ydbz_s gray" onClick="history.go(-1)">返回</a>
			</div>
</body>
</html>