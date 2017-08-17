<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>借款-审批</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<!-- 静态资源 -->
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">
function closeCurWindow(){
	this.close();
}
// 审批通过
function reviewPass(rid){
	var html = '<div class="add_allactivity"><label>请填写审批备注!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="100" oninput="this.value=this.value.substring(0,100)"></textarea>';
	html += '</div>';
	$.jBox(html, { 
		title: "审批备注", 
		buttons:{"提 交":"1"}, 
		height:250, 
		width:500,
		submit:function(v, h, f) {
			if (v=="1"){
				$.ajax({
		    		type: "POST",
		    		url: "${ctx}/newOrderReview/manage/reviewBorrowing",
		        	data: {
		            	rid:rid,
		            	result:1,
		            	denyReason:$("#denyReason").val()
		        	},
		       	 	success: function(msg){
		        		if(msg.flag == 1){
		            		top.$.jBox.tip('操作成功！');
		            		window.opener.location.href = window.opener.location.href;
	           				window.close();
		            	}else{
		            		top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
		            	}
		        	}
				});
			}
		}
	});
}

//驳回
function jbox_bohui(rid,roleId){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" maxlength="200"></textarea>';
	html += '</div>';
	$.jBox(html, { 
		title: "驳回原因", 
		buttons:{"提 交":"1"}, 
		height:250, 
		width:500,
		submit:function(v, h, f){
			if (v=="1"){
				$.ajax({
		            type: "POST",
		            url: "${ctx}/newOrderReview/manage/reviewBorrowing",
		            data: {
		            	rid:rid,
		            	denyReason:$("#denyReason").val(),
		           	 	result:0
		            },
		            success: function(msg){
		            	if(msg.flag == 1){
		            		top.$.jBox.tip('操作成功！');
		            		window.opener.location.href = window.opener.location.href;
	           				window.close();
		            	}else{
		            		top.$.jBox.tip(msg.message, 'error', { focusId: 'name' });
		            	}
		            }
		        });
			}
		}
	});
}
</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<input type="hidden" value="${orderId }" id="orderId">
		<div class="mod_nav">订单>
			<c:if test="${productOrder.orderStatus == '1'}">单团</c:if>
			<c:if test="${productOrder.orderStatus == '2'}">散拼</c:if>
			<c:if test="${productOrder.orderStatus == '3'}">游学</c:if>
			<c:if test="${productOrder.orderStatus == '4'}">大客户</c:if>
			<c:if test="${productOrder.orderStatus == '5'}">自由行</c:if>
			<c:if test="${productOrder.orderStatus =='10'}">游轮</c:if>
            >借款审批
        </div>
		<%@ include file="/WEB-INF/views/review/borrowing/singlegroup/singlegrouporderBaseInfo.jsp"%>
		
		<div class="ydbz_tit"><span class="fl">借款</span></div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="8%">姓名</th>
                         <th width="12%">币种</th>
                         <th width="11%">游客结算价</th>
						 <th width="12%">借款金额</th>
						 <th width="20%">备注</th>
					  </tr>
				   </thead>
				   <tbody>
					   <c:if test="${fn:length(tralist) == 0 }">
							<tr>
								<td colspan="5" style="text-align: center;">无游客借款信息</td>
							</tr>
					   </c:if>
					   <c:forEach items="${tralist}" var="borrowing">
					    <tr>
					     <td class="tc">${borrowing.travelerName} 
							<input  type="hidden" name="travelerId" value="${borrowing.travelerId }" />
							<input type="hidden" name="travelerName" value="${borrowing.travelerName }" /> 
							<input type="hidden" name="payPrice" value="${borrowing.payPrice }" /></td>
						 <td class="tc">${borrowing.currencyName} </td>
						 <td class="tc">${borrowing.payPrice} </td>
						 <td class="tc">${borrowing.lendPrice} </td>
						 <td class="tc">${borrowing.remark} </td>	
					   </tr>
					   </c:forEach>
				   </tbody>
				   </table>	 
						
        		<div class="ydbz_tit">
					<span class="fl">团队借款</span>
        		</div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="8%">费用名称</th>
                         <th width="12%">币种</th>
						 <th width="12%">借款金额</th>
						 <th width="20%">备注</th>
					  </tr>
				   </thead>
				   <tbody>
						  <c:if test="${fn:length(teamlist) == 0 }">
								<tr>
									<td colspan="5" style="text-align: center;">无团队借款信息</td>
								</tr>
							</c:if>
						   <c:forEach items="${teamlist}" var="team">
						    <tr>
						     <td class="tc">${team.lendName} </td>
							 <td class="tc">${team.currencyName} </td>
							 <td class="tc">${team.lendPrice} </td>
							 <td class="tc">${team.remark} </td>	
						   </tr>
						   </c:forEach>
				   </tbody>
				</table>
				<br>
				<!-- 270 借款详情页增加还款日期 -->
				<div>
					<p>还款日期：<span><fmt:formatDate value="${refundDate}" pattern="yyyy-MM-dd" /></span></p>
				</div>
				<div  class="ydbz_foot">
                    <div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">借款金额：
                         <c:if test="${fn:length(borrowList) == 0 }"></c:if>
                         <c:forEach items="${borrowList}" var="borrow" varStatus = "s">
						    <span style="font-size:12px;">${borrow.currencyMarks}</span>
							<span class="red" >${borrow.borrowPrices}</span>
							<c:if test="${s.index lt (fn:length(borrowList)-1)}">
								<span style="color:green;">+</span>
							</c:if>
						   </c:forEach>
                    </div>
				</div>
		<div class="ydbz_tit"><span class="fl">审批动态</span></div>	
		<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
		
		<div style="margin-top:20px;"></div>				
				<div class="dbaniu">
					<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a>
					<input type="submit" id = "failBtn" value="驳回" class="btn btn-primary" onclick="jbox_bohui('${rid}','${roleId }');">
					<input name="denyReason" type="hidden"/>
					<input type="submit" id = "succBtn" value="审批通过" class="btn btn-primary" onclick="reviewPass('${rid}');">
				</div>
	<!--右侧内容部分结束-->
	
	
</body>
</html>