<!-- 
author:chenry
describe:订单详情页，订单操作中 团签 功能跳转页面,适用于单团订单，散拼订单，游学订单，大客户订单，自由行订单功能列表
createDate：2014-11-03
 -->
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html>
<head>
<title>切位退款记录
</title>
<meta name="decorator" content="wholesaler"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript">

$(function(){
    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
    
	<%-- 前端js效果部分 --%>
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  		
});

function applyGroupRefund() {
	if("${airTicketMark}") {
		window.location.href="${ctx}/stock/manager/apartGroup/applyAirTicketRefund?aid=${orderId}";	
	}else{
		window.location.href="${ctx}/stock/manager/apartGroup/applyGroupRefund?aid=${orderId}";
	}
}
function viewOrderInfo(productType,rid,orderId){
	var productType = 9;
	if("${airTicketMark}") {
		productType = 10;
	}
	window.open("${ctx}/stock/manager/apartGroup/viewApplyRefundInfo?aid=${orderId}&orderId="+orderId+"&rid="+rid+"&productType=" + productType);
}
function cancleReview(rid){
$.jBox.confirm("确定要取消审核吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				$.ajax({
	                type: "POST",
	                url: "${ctx}/orderCommon/manage/cancleAudit",
	                data: {
	                    id:rid
	                },
	                success: function(msg){
	                    location.reload();
	                }
	            });
			}
		});
			
}
</script>

<style type="text/css">
a{
    display: inline-block;
}

*{ margin:0px; padding:0px;}
body{ background:#fff; margin:0px auto;}
.pop_gj{ padding:10px 24px; margin:0px; border-bottom:#b3b3b3 1px dashed; overflow:hidden;}
.pop_gj dt{ float:left; width:100%;}
.pop_gj dt span{ float:left; width:80px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:25px; line-height:180%;}
.pop_gj dt p{ float:left; width:300px;color:#000; font-size:12px;line-height:180%;}
.pop_xg{ padding:10px 4px; margin:0px; overflow:hidden;}
.pop_xg dt{ float:left; width:100%; margin-top:10px; height:30px;}
.pop_xg dt span{ float:left; width:100px; text-align:right; color:#333; font-size:12px; overflow:hidden; height:30px; line-height:30px;}
.pop_xg dt p{ float:left; width:110px;color:#333; font-size:12px;height:30px; line-height:30px;overflow:hidden; position:relative;}
.pop_xg dt p font{ color:#e60012; font-size:12px;}
.pop_xg dt p input{width:60px; height:28px; line-height:28px; padding:0px 5px 0px 18px; color:#403738; font-size:12px; position:relative; z-index:3; }
.pop_xg dt p i{ position:absolute; height:28px; top:2px; width:10px; text-align:center; left:5px; z-index:5; font-style:normal; line-height:28px;}
.release_next_add button{ cursor:pointer; border-radius:4px;}
label{ cursor:inherit;}
.main-right-topbutt a {top: -10px;}
</style>

</head>

<body>

<div id="sea">
<div class="mod_nav">
切位退款</div>
	<!-- 顶部参数 -->
<div class="activitylist_bodyer_right_team_co_bgcolor" style="float:left;width: 100%">
    
 <!--右侧内容部分开始-->
 
				<!--右侧内容部分开始-->
				<p class="main-right-topbutt">
				<c:if test="${sign == 0 }">
				<a href="javascript:applyGroupRefund()">申请切位退款</a>
				</c:if>
				</p>
				<div class="ydbz_tit">切位退款记录</div>
				<table id="contentTable" class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="5%">渠道商</th>
						 <th width="10%">款项</th>
						 <th width="10%">报批日期</th>
                         <th width="10%">币种</th>
						 <th width="10%">应收金额</th>
                         <th width="10%">退款金额</th>
						 <th width="15%">备注</th>
						 <th width="10%">状态</th>
						 <th width="10%">操作</th>
					  </tr>
				   </thead>
				   <tbody>
				   <c:forEach items="${page }" var="map">
				   		<c:forEach items="${map.value}" var="o" varStatus="s">
				   				   
                      <tr>
						  <td>${o.travelerName }</td>
                         <td>${o.refundName }</td>
						 <td class="tc">
						 <fmt:formatDate value="${o.applyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
						 </td>
                         <td>
                          ${o.currencyName}                       
                         </td>
						 <td class="tr">${o.payPrice}</td>
                         <td class="tr">￥${o.refundPrice}</td>
						 <td>${o.remark}</td>
						 <td>
						 <c:if test="${o.status=='0'}">
						   	 已驳回
					       </c:if>
					       <c:if test="${o.status=='1'}">
						   	 审核中
					       </c:if>
					       <c:if test="${o.status=='2'}">
						   	 审核成功
					       </c:if>
					       <c:if test="${o.status=='3'}">
						   	 操作完成
					       </c:if>
					       <c:if test="${o.status=='4'}">
						   	 已取消
					       </c:if>
						 </td>
						 <td class="p0">
							<dl class="handle">
								<dt><img title="操作" src="${ctxStatic}/images/handle_cz.png"></dt>
								<dd class="">
								   <p>
										<span></span> 
										 <c:if test="${o.status=='0'}">
									   	 	<a href="javascript:viewOrderInfo('${productType}','${o.reviewId}','${orderId}')">审核详情 </a>
						<!--  			   	 	<a href="javascript:reapplyReview(${orders.id})">重新申请</a>-->
								      	 </c:if>
										<c:if test="${o.status=='1'}">
									   	 	<a href="javascript:viewOrderInfo('${productType}','${o.reviewId}','${orderId}')">审核详情 </a>
									   	 	<a href="javascript:cancleReview(${o.reviewId})">取消申请 </a>
								      	 </c:if>
								      	 <c:if test="${o.status=='2'}">
									   	 	<a href="javascript:viewOrderInfo('${productType}','${o.reviewId}','${orderId}')">审核详情 </a>									   	 	
								      	 </c:if>
								      	 <c:if test="${o.status=='3'}">
									   	 	<a href="javascript:viewOrderInfo('${productType}','${o.reviewId}','${orderId}')">审核详情 </a>									   	 	
								      	 </c:if>
								      	 <c:if test="${o.status=='4'}">
									   	 	<a href="javascript:viewOrderInfo('${productType}','${o.reviewId}','${orderId}')">审核详情 </a>									   	 	
								      	 </c:if>
								   </p>
								</dd>
							</dl>				 
						</td>
					  </tr>
					  </c:forEach>
				   </c:forEach>
				   
				   </tbody>
				</table>
				<!--右侧内容部分结束-->
		</div>
    
</div>
</body>
</html>
