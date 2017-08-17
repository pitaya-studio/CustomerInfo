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
<title>查看转团申请
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
function cancel(){

	alert("关闭");
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
				<!--右侧内容部分开始-->
				<div class="mod_nav">审核 > 转团申请 > 审批详情</div>
				<div class="orderdetails_tit"><span>1</span>订单信息</div>
               <div class="orderdetails1">
               		<table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
								<td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${order.createBy.name }</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate value="${order.orderTime }" pattern="yyyy-MM-dd"/></td> 
                                <td class="mod_details2_d1">团队类型：</td>
                                <td class="mod_details2_d2">
                                	<c:if test="${order.orderStatus==1}">单团</c:if>
                                	<c:if test="${order.orderStatus==2}">散拼</c:if>
                                	<c:if test="${order.orderStatus==3}">游学</c:if>
                                	<c:if test="${order.orderStatus==4}">大客户</c:if>
                                	<c:if test="${order.orderStatus==5}">自由行</c:if>
                                </td>
                                <td class="mod_details2_d1">收客人：</td>
						        <td class="mod_details2_d2">${order.orderSaler.name }</td>
							</tr>
							<tr> 
                                <td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${order.orderNum }</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2" id="oldGroupCode">${group.groupCode }</td>
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${totalMoney }</em></td>
								<td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
									<c:if test="${order.payStatus==1 }">未收全款</c:if>
									<c:if test="${order.payStatus==2 }">未收订金</c:if>
									<c:if test="${order.payStatus==3 }">已占位</c:if>
									<c:if test="${order.payStatus==4 }">已收订金</c:if>
									<c:if test="${order.payStatus==5 }">已收全款</c:if>
									<c:if test="${order.payStatus==99 }">已取消</c:if>
								</td>	 	 
                            </tr>
                            <tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${user.name }</td>   
						        <td class="mod_details2_d1">销售：</td>
		       				   <c:set value="${order.salerId}" var="saler"></c:set>
							   <td class="mod_details2_d2">${fns:getUserNameById(saler)}</td>  
							</tr>
						</tbody>
					</table>
               </div>
               <div class="orderdetails_tit"><span>2</span>产品信息</div>
               <div class="orderdetails2">
                  <p class="ydbz_mc">${product.acitivityName}</p>
                 	<ul class="ydbz_info">
                     <li><span>出发城市：</span>${fns:getDictLabel(product.fromArea, "from_area", "")}</li>
                     <li><span>出团日期：</span><fmt:formatDate value="${group.groupOpenDate }" pattern="yyyy-MM-dd"/></li>
                     <li><span>行程天数：</span>${product.activityDuration }天</li>
                     <li title="${product.targetAreaNames}"><span>目的地：</span>${product.targetAreaNames}</li>
                     <li><span></span></li>
                  </ul>
                  <ul class="ydbz_dj specialPrice">
                     <li><span class="ydtips">单价</span>
                        <p>成人：${fns:getCurrencyInfo(group.currencyType, 0, 'mark') }<c:if test="${not empty group.settlementAdultPrice }"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${group.settlementAdultPrice }" /></c:if>
                        					
                        <p>儿童：${fns:getCurrencyInfo(group.currencyType, 1, 'mark') }<c:if test="${not empty group.settlementcChildPrice }"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${group.settlementcChildPrice }" /></c:if>
                        					
                        <p>特殊人群：${fns:getCurrencyInfo(group.currencyType, 2, 'mark') }<c:if test="${not empty group.settlementSpecialPrice }"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${group.settlementSpecialPrice }" /></c:if>
                        					
                     </li>
                     <li><span class="ydtips"> 出行人数</span>
                        <p>成人：<span> <c:if test="${not empty order.orderPersonNumAdult }">${order.orderPersonNumAdult }</c:if> 
                        									<c:if test="${empty order.orderPersonNumAdult }">0</c:if> </span> 人</p>
                        <p>儿童：<span> <c:if test="${not empty order.orderPersonNumChild }">${order.orderPersonNumChild }</c:if> 
                        									<c:if test="${empty order.orderPersonNumChild }">0</c:if></span> 人</p>
                        <p>特殊人群：<span><c:if test="${not empty order.orderPersonNumSpecial }">${order.orderPersonNumSpecial }</c:if> 
                        									<c:if test="${empty order.orderPersonNumSpecial }">0</c:if> </span> 人</p>
                     </li>

                  </ul>
               </div>
               <!-- 转入团 -->
               <div class="orderdetails_tit"><span>3</span>转入团</div>
               <table class="activitylist_bodyer_table">
               	<thead>
               		<tr>
               			<th width="10%">团号</th>
               			<th width="15%">出/截团日期</th>
               			<th width="15%">签证国家</th>
               			<th width="15%">资料截止日期</th>
               			<th width="5%">预收</th>
               			<th width="5%">已切位</th>
               			<th width="5%">余位</th>
               		</tr>
               	</thead>
               	<tbody>
               		<tr>
               			<td>${newGroup.groupCode }</td>
               			<td><fmt:formatDate value="${newGroup.groupOpenDate }" pattern="yyyy-MM-dd"/> /
               						<fmt:formatDate value="${newGroup.groupCloseDate }" pattern="yyyy-MM-dd"/></td>
               			<td>${newGroup.visaCountry }</td>
               			<td><fmt:formatDate value="${newGroup.visaDate }" pattern="yyyy-MM-dd"/></td>
               			<td>${newGroup.planPosition }</td>
               			<td>${newGroup.soldPayPosition }</td>
               			<td>${newGroup.freePosition }</td>
               		</tr>
               	</tbody>
               </table>
               
               <!-- 游客转团 -->
               <div class="orderdetails_tit"><span>4</span>游客转团</div>
               <p><span>报批日期：<fmt:formatDate value="${review.createDate }" pattern="yyyy-MM-dd"/></span></p>
               <table class="activitylist_bodyer_table">
				   <thead>
					  <tr>
						 <th width="10%">姓名</th>
						 <th width="10%">签证状态</th>
						 <th width="12%">实际约签时间</th>
						 <th width="12%">应收金额</th>
						 <th width="10%">护照号</th>
						 <th width="10%">护照有效期</th>
						 <th width="16%">转团原因</th>
					  </tr>
				   </thead>
				   <tbody>
				   	<tr>
						 <td>${travel.name }</td>
						 <td>
								<c:if test="${travel.visa.visaStatus==0 }">未送签</c:if>
								<c:if test="${travel.visa.visaStatus==1 }">送签</c:if>			
								<c:if test="${travel.visa.visaStatus==2 }">约签</c:if>			
								<c:if test="${travel.visa.visaStatus==3 }">出签</c:if>			
								<c:if test="${travel.visa.visaStatus==4 }">申请撤签</c:if>			
								<c:if test="${travel.visa.visaStatus==5 }">撤签成功</c:if>			
								<c:if test="${travel.visa.visaStatus==6 }">撤签失败</c:if>			
								<c:if test="${travel.visa.visaStatus==7 }">拒签</c:if>	
								<c:if test="${empty travel.visa.visaStatus }">无签证</c:if>										 
						 </td>
						 <td>${travel.visa.contract }</td>
						 <td>${travel.payPriceSerialNumInfo }</td>
						 <td>${travel.passportCode }</td>
						 <td><fmt:formatDate value="${travel.passportValidity }" pattern="yyyy-MM-dd"/></td>
						 <td>${review.createReason }</td>
					  </tr>
				   </tbody>
				</table>
				<!-- 审核动态 -->
             <div class="orderdetails_tit"><span>5</span>审核动态</div>
             
             <div class="orderdetails2">
             	<ul>
             		<c:if test="${empty  logList}"><li>暂无审核动态</li></c:if>
             		<c:forEach items="${logList }" var="ReviewLog">
             			<li><span>${ReviewLog. createDate}  [ ${fns:getUserById(ReviewLog.createBy).name} ]  ${ ReviewLog.result}</span></li>
             		</c:forEach>
             	</ul>
             	<c:if test="${review.status ==0}">
					<div class="ydbz_tit">
						<span class="fl">驳回理由</span>
	        		</div>
	                <ul class="spdtai">
						<li>${ review.denyReason}</li>
					</ul>
				 </c:if>
             </div>
				<div style="margin-top:20px;"></div>				
				<div class="dbaniu">
					<a class="ydbz_s gray" href="javascript:window.opener=null;window.close();">关闭</a>
				</div>
				<!--右侧内容部分结束-->
</body>
</html>
