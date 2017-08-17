<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<html>
<head>
<title>转团详情</title>
<meta name="decorator" content="wholesaler"/>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/modules/order/transfergroup/transferGroupApply.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
});
// 跳转到产品详情
function gotoProduct(productId){
	window.open(contextPath + "/activity/manager/detail/"+productId);
}
</script>
</head>
<body>
	<page:applyDecorator name="show_head">
    	<page:param name="desc">转团详情</page:param>
	</page:applyDecorator>
         <!--右侧内容部分开始-->
         <div class="mod_nav"> 订单 > ${orderStatus} > 转团详情</div>
         <div class="ydbzbox fs">
            <div class="orderdetails">
               <div class="orderdetails_tit">订单信息</div>
               <div class="orderdetails1">
               		<table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
								<td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${order.createBy.name }</td>
						        <td class="mod_details2_d1">销售：</td>
						        <td class="mod_details2_d2">${order.salerName}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${order.orderTime}" /></td> 
                                <td class="mod_details2_d1">团队类型：</td>
                                <td class="mod_details2_d2">
                                	<c:if test="${order.orderStatus==1}">单团</c:if>
                                	<c:if test="${order.orderStatus==2}">散拼</c:if>
                                	<c:if test="${order.orderStatus==3}">游学</c:if>
                                	<c:if test="${order.orderStatus==4}">大客户</c:if>
                                	<c:if test="${order.orderStatus==5}">自由行</c:if>
                                </td>
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
									<c:if test="${order.payStatus==99 }">已取消</c:if></td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">操作人：</td>
						        <td class="mod_details2_d2">${user.name }</td>     
							</tr>
						</tbody>
					</table>
               </div>
               <div class="orderdetails_tit">产品信息</div>
               <div class="orderdetails2">
                  <p class="ydbz_mc">${order.orderNum }-导出游客信息</p>
                 	<ul class="ydbz_info">
                     <li><span>出发城市：</span>${fromArea.name }</li>
                     <li><span>出团日期：</span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${group.groupOpenDate}" /></li>
                     <li><span>行程天数：</span>${product.activityDuration }天</li>
                     <li><span>离境城市：</span>${outArea.name }</li>
                     <li><span></span></li>
                     <li id="mddtargetAreaNames" title="" class="orderdetails2_text"><span >目的地：</span>
                     	<c:forEach items="${product.targetAreaNameList }"  var="city">
                     		<span> city </span>
                     	</c:forEach>
                     </li>
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
               <div class="orderdetails_tit">转入团</div>
               <table class="activitylist_bodyer_table">
               	<thead>
               		<tr>
               			<th width="10%">团号</th>
               			<th width="15%">操作人</th>
               			<th width="15%">出/截团日期</th>
               			<th width="15%">签证国家</th>
               			<th width="15%">资料截止日期</th>
               			<th width="5%">预收</th>
               			<th width="5%">已切位</th>
               			<th width="5%">余位</th>
               			<th width="10%">操作</th>
               		</tr>
               	</thead>
               	<tbody>
               		<tr>
               			<td>${newGroup.groupCode }</td>
               			<td>${user.name}</td>
               			<td><fmt:formatDate value="${newGroup.groupOpenDate }" pattern="yyyy-MM-dd"/> <br/>
               						<fmt:formatDate value="${newGroup.groupCloseDate }" pattern="yyyy-MM-dd"/></td>
               			<td>${newGroup.visaCountry }</td>
               			<td><fmt:formatDate value="${newGroup.visaDate }" pattern="yyyy-MM-dd"/></td>
               			<td>${newGroup.planPosition }</td>
               			<td>${newGroup.soldPayPosition }</td>
               			<td>${newGroup.freePosition }</td>
               			<td><a class="ydbz_x" onclick="gotoProduct(${newGroup.srcActivityId})">产品详情</a></td>
               		</tr>
               	</tbody>
               </table>
               
               <!-- 游客转团 -->
               <div class="orderdetails_tit">游客转团</div>
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
						 <td><fmt:formatDate value="${travel.passportValidity}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						 <td>${review.createReason }</td>
					  </tr>
				   </tbody>
				</table>
             <!-- 审核动态 -->
             <div class="orderdetails_tit">审核动态</div>
             
             <div class="orderdetails2">
             	<ul>
             		<c:if test="${empty logList}"><li>尚未开始审核。</li></c:if>
             		<c:forEach items="${logList }" var="ReviewLog">
             			<li><span><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${ReviewLog.createDate}" />  [ ${fns:getUserById(ReviewLog.createBy).name} ]  ${ ReviewLog.result}</span></li>
             		</c:forEach>
             	</ul>
             	<c:if test="${review.status ==0}">
					<div class="ydbz_tit">
						<span class="fl">驳回理由</span>
	        		</div>
	                <ul class="spdtai">
						<li>${review.denyReason}</li>
					</ul>
				 </c:if>
             </div>
             	
             
            </div>
			<div class="ydBtn ydBtn2">
           		<a class="ydbz_s" onClick="window.close();">关闭</a>
           </div>        
           </div>
</body>
</html>
