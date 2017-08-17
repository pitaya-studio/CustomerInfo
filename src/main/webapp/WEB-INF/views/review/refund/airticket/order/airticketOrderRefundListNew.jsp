<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-机票-退款</title>

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
  g_context = "${ctx}";
	//操作浮框
	operateHandler();
	$("#newRefund").click(function(){
	   var orderId = $("#orderId").val();
	   createNewRecord(orderId);
	});
		
});

function createNewRecord(orderId){
	document.location= g_context + "/order/refundNew/createRefundNew?orderId=" + orderId;
}

function orderDetail(){
    var orderId = $("#orderId").val();
    window.open(g_context + "/order/manage/airticketOrderDetail?orderId=" + orderId);
    
}

function refundTwo(reviewId){
  var orderId = $("#orderId").val();
  document.location= g_context + "/order/refund/createRefund?orderId=" + orderId + "&reviewId=" + reviewId;

}

function cancelRefund(reviewId){
    var orderId = $("#orderId").val();
    $.ajax({
	   type: "POST",
		  url: g_context + "/order/refundNew/cancelRefundNew",
		  dataType: "json",
		  data : {"reviewId" : reviewId},
		  async: false,
		  success:function(msg){
			  if(msg){
				  if(msg.error){
					  //show error
					  top.$.jBox.tip(msg.error, 'error');
					  return;
				  }
				  document.location = g_context + "/order/refundNew/viewListNew?orderId=" + orderId;
			  }
		  }
   });
  
}

</script>
</head>
<body>
<input type="hidden" value="${orderId }" id="orderId">
<!--右侧内容部分开始-->
<div class="mod_nav">订单 > 机票 > 退款记录</div>
<div class="ydbz_tit orderdetails_titpr">退款记录
	<a class="ydbz_x" id="newRefund">申请退款</a>
</div>
<table id="contentTable" class="activitylist_bodyer_table">
	<thead>
		<tr>
			<th width="10%">游客</th>
			<th width="10%">款项</th>
			<th width="10%">报批日期</th>
			<th width="10%">币种</th>
			<th width="10%">应收金额</th>
			<th width="10%">退款金额</th>
			<th width="20%">备注</th>
			<th width="10%">状态</th>
			<th width="10%">操作</th>
		</tr>
	</thead>
	<tbody>
		 <c:if test="${fn:length(viewMap) == 0}">
		       <tr class="toptr" >
		       <td colspan="9" style="text-align: center;">
		                                          无退款申请记录
		       </td>
		       </tr>
	    </c:if>

     <c:forEach items="${viewMap }" var="bean">
	     <tr>
		    <!--<c:if test="${s1.first}">
		        <td rowspan="${fn:length(map.value)}">${bean.travelerName }</td>
		    </c:if> -->
		    <td>${bean.travelerName }</td>
	        <td>${bean.refundName }</td>
			<td class="tc"><fmt:formatDate value="${bean.applyDate }" pattern="yyyy-MM-dd"/></td>
			<td>${bean.currencyName }</td>
			<td class="tr"><span class="tdgreen">${bean.payPrice }</span></td>
			<td class="tr">${bean.currencyMark }<span class="tdred"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${bean.refundPrice }" /></span></td>
			<td>${bean.remark }</td>
			<td>
				${fns:getChineseReviewStatus(bean.status,bean.currentReviewer)}
            </td>
			<td class="p0">
				<dl class="handle">
					<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
					<dd class="">
						<p>
							<span></span> 
							<a href="javascript:void(0)" onclick="orderDetail()">订单详情</a>
							<a href="${ctx}/order/refundNew/refundPriceNew/${orderId}/${bean.id}">退款详情</a>
							<c:if test="${bean.status == 0 || bean.status == 1}">
						        <!--  <a href="javascript:void(0)" onclick="refundTwo('${bean.reviewId}')">重新申请</a> //以前用，现在不用了-->	
						    </c:if>
						     <!-- ${bean.status == 1 || bean.status == 2} //以前用这个 -->
						    <c:if test="${fns:isShowCancel(bean.id)}">
						        <a href="javascript:void(0)" onclick="cancelRefund('${bean.id}')">取消申请</a>	
						    </c:if>									
						</p>
					</dd>
				</dl>						
			</td>
		</tr>
     </c:forEach>

	</tbody>
</table>
<!--右侧内容部分结束-->
</body>
</html>


