<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>借款审核</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>

<script type="text/javascript">


function closeCurWindow(){
	this.close();
}
function reviewPass(rid,roleId){
	var html = '<div class="add_allactivity"><label>请填写审核备注!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "审核备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var htm=$("#denyReason").val();
			$("input[name='denyReason']").val(htm);
			$.ajax({
		            type: "POST",
		            url: "${ctx}/orderReview/manage/reviewBorrowing",
		            data: {
		            rid:rid,
		            roleId:roleId,
		            result:1,
		            userLevel:"${userLevel}",
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
	},height:250,width:500});
	}
	//驳回
function jbox_bohui(rid,roleId){
	var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
	html += '<textarea style="width:80%; margin:10px auto;" id="denyReason" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			var htm=$("#denyReason").val();
			$("input[name='denyReason']").val(htm);
			$.ajax({
		            type: "POST",
		            url: "${ctx}/orderReview/manage/reviewBorrowing",
		            data: {
		            rid:rid,
		            roleId:roleId,
		            result:0,
		            userLevel:"${userLevel}",
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
	},height:250,width:500});
	
}
</script>
</head>

<body>
	<!-- tab -->
	<page:applyDecorator name="airticket_order_detail">
	</page:applyDecorator>
	<!--右侧内容部分开始-->
	<input type="hidden" value="${orderId }" id="orderId">
		<div class="mod_nav">订单&nbsp;&nbsp;> 
                <c:if test="${orderType==1 }">单团</c:if>
				<c:if test="${orderType==2 }">散拼</c:if>
				<c:if test="${orderType==3 }">游学</c:if>
				<c:if test="${orderType==4 }">大客户</c:if>
				<c:if test="${orderType==5 }">自由行</c:if>
				<c:if test="${orderType==10 }">游轮</c:if>&nbsp;&nbsp;
				> 借款审核</div>
		<%@ include file="/WEB-INF/views/modules/order/rebates/rebatesBaseInfo.jsp"%>
		
		<div class="ydbz_tit"><span class="fl">借款</span></div>
				<span style="padding-left:30px;">报批日期：</span><fmt:formatDate pattern="yyyy-MM-dd" value="${applyDate}"/>
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
				<div class="activitylist_bodyer_right_team_co2">
					<label>还款日期：</label>
					<fmt:formatDate value="${refundDate}" pattern="yyyy-MM-dd" />
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
			
		<%@ include file="/WEB-INF/views/modules/order/airticketOrderReviewInfo.jsp"%>
		
		<div style="margin-top:20px;"></div>				
				<div class="dbaniu">
					<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a>
					<input type="submit" id = "failBtn" value="驳回" class="btn btn-primary" onclick="jbox_bohui('${rid}','${roleId }');">
					<input name="denyReason" type="hidden"/>
					<input type="submit" id = "succBtn" value="审核通过" class="btn btn-primary" onclick="reviewPass('${rid}','${roleId }');">
				</div>
	<!--右侧内容部分结束-->
	
	
</body>
</html>