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
<title>退款审批详情</title>
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
.wpr20 label{ width:60px; text-align:right;}
.fl{float:left;}
.fr{float:right;}
.ydbz_tit{
	background-color:#f3f3f3;
	line-height:33px;color:#333333;
	font-weight:bold;padding-left:32px;margin-top:10px;margin-bottom:10px;
	position:relative; *height:33px;
	font-size:14px; border-radius:1px;
	overflow:hidden;
}
label{ cursor:inherit;}
</style>

</head>

<body>
<div id="sea">
				<!--右侧内容部分开始-->
				<div class="mod_nav">销售签证订单管理&nbsp;&nbsp;>退款审批&nbsp;&nbsp;>审批详情</div>
				<%-- <c:set var="hashMap" value="${fns:getReviewRefundInfo(param.type)}"/> --%>
				
				<div class="ydbz_tit">订单详情</div>
				<div class="orderdetails1">
             <table border="0" style="margin-left: 25px" width="98%">
                 <tbody>
                     <tr>
                         <td class="mod_details2_d1">下单人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                         <td class="mod_details2_d1">下单时间：</td>
                         <td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate}"/></td> 
                         <td class="mod_details2_d1">团队类型：</td>
                         <td class="mod_details2_d2">
                         	<c:choose>
                         		<c:when test="${empty activityGroup.travelActivity.activityTypeName}">单办签</c:when>
                         		<c:otherwise>${activityGroup.travelActivity.activityTypeName}</c:otherwise>
                         	</c:choose>
                       	 </td>	
                         <td class="mod_details2_d1">收客人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                                
                     </tr>
                     <tr> 
                         <td class="mod_details2_d1">订单编号：</td>
                         <td class="mod_details2_d2">${visaOrder.orderNo}</td>
                         <td class="mod_details2_d1">订单团号：</td>
                         <td class="mod_details2_d2">${visaProduct.groupCode}</td>
                         <td class="mod_details2_d1">订单总额：</td>
                         <td class="mod_details2_d2"><em class="tdred">${fns:getMoneyAmountBySerialNum(visaOrder.totalMoney,2)}</em></td>
                         <td class="mod_details2_d1">订单状态：</td>
                         <td class="mod_details2_d2">
							 <!-- 0：未支付;1:已支付;2:已取消;100:订单创建中（创建未完成，不能使用） -->
							 <c:choose>
								 <c:when test="${'0' eq visaOrder.visaOrderStatus}">
									 未收款
								 </c:when>
								 <c:when test="${ '1' eq visaOrder.visaOrderStatus}">
									 已收款
								 </c:when>
								 <c:when test="${ '2' eq visaOrder.visaOrderStatus}">
									 已取消
								 </c:when>
								 <c:when test="${ '100' eq visaOrder.visaOrderStatus}">
									 订单创建中
								 </c:when>
								 <c:otherwise>${visaOrder.visaOrderStatus}</c:otherwise>
							 </c:choose>
						 </td>
                         </tr>
                     <tr>
                         <td class="mod_details2_d1">操作人：</td>
                         <td class="mod_details2_d2" colspan="7">${fns:getUser().name}</td>     
                     </tr>
                 </tbody>
         	</table>	
        </div>
	
				<div class="ydbz_tit">
					<span class="fl">
						<c:if test="${not empty (travelerInfo)}">游客退款</c:if>
						<c:if test="${empty (travelerInfo)}">团队退款</c:if>
					</span>
					<c:if test="${!empty reviewInfo.updateDate}">
						<span class="fr wpr20">报批日期：<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${reviewInfo.updateDate}"/></span>
					</c:if>
        		</div>
				<table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                        <tr>
                            
                            <th width="10%">
                            <c:if test="${not empty travelerInfo}">游客</c:if>
					        <c:if test="${empty travelerInfo}">团队</c:if>
                            </th>
                            <th width="15%">款项</th>
                            <th width="15%">币种</th>
                            <th width="15%">应收金额</th>
                            <th width="15%">退款金额</th>
                            
                            <th width="15%">退款原因</th>
                        </tr>
                    </thead>
                    
                    <tbody>     
                        <c:forEach items="${review}" var="r">    
                        <c:if test="${r.id eq rid}">       
						<tr>
							<td>
							<c:if test="${not empty reviewInfo}">${reviewInfo.travellerName}</c:if>
							<c:if test="${ empty travelerInfo}">团队</c:if>
							</td>
						    
							<td>${r.refundName}</td>
							<td>${r.currencyName}</td>
							
							<td class="tr"><span class="tdred">
						    <c:if test="${not empty travelerInfo}">${r.payPrice}</c:if>
							<c:if test="${ empty travelerInfo}">${fns:getMoneyAmountBySerialNum(productOrder.totalMoney,1)}</c:if>
							
							</span></td>	
						    <td>
							${r.refundPrice}
							</td>						
							<td>${reviewInfo.remark}
							<input name="travelerId"value="${reviewInfo.travellerId}" type="hidden">
							<input name="travelerName"value="${reviewInfo.travellerName}" type="hidden">
							</td>					
						</tr>
					</c:if>
					</c:forEach>
                    </tbody>                    
                </table>
                
				<%-- <div style="margin-top:20px;"></div>
				 <div class="ydbz_tit">
					<span class="fl">审批动态</span>
        		</div>
				<ul class="spdtai">
				<c:if test="${not empty reviewLogInfo}">
					<c:forEach items="${reviewLogInfo}" var="log" varStatus="s">
					<li><fmt:formatDate value="${log.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;&nbsp;
					 【${fns:getUserNameById(log.createBy)}】&nbsp;&nbsp;${log.result}</li>
					</c:forEach>
				
				</c:if>
				<c:if test="${empty reviewLogInfo}">
				<li>暂无审批动态</li>
				</c:if>
				</ul>
				<c:if test="${ reviewInfo.status ==0}">
				<div class="ydbz_tit">
					<span class="fl">驳回理由</span>
        		</div>
                <ul class="spdtai">
					<li>${ reviewInfo.denyReason}</li>
				</ul>
				</c:if>	 --%>	
			    <!-- 引入静态页面-tgy-start -->
			    <div class="ydbz_tit">
					<span class="fl">审批动态</span>
				</div>		
				<%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>	
				<!-- 引入静态页面-tgy-end -->	
				
				<div class="ydBtn ydBtn2"><a class="ydbz_s gray" href="javascript:history.go(-1)">关闭</a></div>
				<!--右侧内容部分结束-->
			</div>     
                
</body>
</html>
