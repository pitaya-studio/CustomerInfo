<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>转团转款</title>
<meta name="decorator" content="wholesaler"/>
	<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<script src="${ctxStatic}/modules/order/transferMoney/transferMoneyApply.js" type="text/javascript"></script>
<script type="text/javascript">
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	init();
});
function init(){
	var errMsg = $("#hiddenMsg").val();
	if(errMsg!=null&&errMsg!=""&&errMsg!="suc"){
		$("#submitButton").hide();
		$.jBox.tip(errMsg,'error');	
	}
}

</script>
</head>
<body>
   <input type="hidden" id="hiddenMsg" name="hiddenMsg" value="${err}"/>
   <form id="sumitForm"  type="post" action="${ctx}/orderCommon/transferMoney/transfersMoneyApplySub">
        <input type="hidden"	name="orderId"  value="${fromBean.productOrderCommon.id}"/>
         <div class="ydbzbox fs">
            <div class="orderdetails">
               <div class="ydbz_tit">转出订单详情</div>
               <div class="orderdetails1">
               		<table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
                            	<td class="mod_details2_d1">订单编号：</td>
								<td class="mod_details2_d2">${fromBean.productOrderCommon.orderNum}</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2"> ${fromBean.activitygroup.groupCode}</td>
                                <td class="mod_details2_d1"><em class="tdred"></em>团队类型：</td>
						        <td class="mod_details2_d2"> 
						        	<c:choose>
											<c:when test="${fromBean.productOrderCommon.orderStatus == 1}">单团</c:when>
											<c:when test="${fromBean.productOrderCommon.orderStatus == 2}">散拼</c:when>
											<c:when test="${fromBean.productOrderCommon.orderStatus == 3}">游学</c:when>
											<c:when test="${fromBean.productOrderCommon.orderStatus == 4}">大客户</c:when>
											<c:when test="${fromBean.productOrderCommon.orderStatus == 5}">自由行</c:when>
								</c:choose>
						        </td>	
                                <td class="mod_details2_d1"><em class="tdred"></em>销售：</td>
						        <td class="mod_details2_d2">${fromBean.productOrderCommon.salerName}</td>
                                       
							</tr>
							<tr> 
                                <td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${fromBean.productOrderCommon.createBy.name}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${fromBean.productOrderCommon.createDate}" /> </td> 
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${fromBean.orderTotalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
										
								${fns:getDictLabel(fromBean.productOrderCommon.payStatus, "order_pay_status", "")}
								<!-- 
												<c:if test="${fromBean.productOrderCommon.orderType==0}">全部</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==1}">全款未支付</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==2}">订金未支付</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==3}">已占位</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==4}">订金已经支付</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==5}">已经支付</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==99}">已经取消订单</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==100}">可操作状态      正向平台同步</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==101}">查看状态      正向平台同步</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==111}">已经删除订单</c:if>
												<c:if test="${fromBean.productOrderCommon.orderType==199}">财务订单</c:if> -->
								 </td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">
操作人：</td>
						        <td class="mod_details2_d2">${fromBean.productOrderCommon.createBy.updateBy.name}</td>     
							</tr>
						</tbody>
					</table>
               </div>
               <div class="ydbz_tit">转入订单详情</div>
               <div class="orderdetails1">
               <c:forEach  items="${listOutBean}" var="toBean" varStatus="loop">
               
               
			               <table border="0" style="margin-left: 25px" width="98%">
						<tbody>
							<tr>
                            	<td class="mod_details2_d1">订单${loop.count }编号：</td>
								<td class="mod_details2_d2">${toBean.productOrderCommon.orderNum}</td>
                                <td class="mod_details2_d1">订单团号：</td>
								<td class="mod_details2_d2"> ${toBean.activitygroup.groupCode}</td>
                                <td class="mod_details2_d1"><em class="tdred"></em>团队类型： </td>
						        <td class="mod_details2_d2"> 
						        <c:choose>
											<c:when test="${toBean.productOrderCommon.orderStatus == 1}">单团</c:when>
											<c:when test="${toBean.productOrderCommon.orderStatus == 2}">散拼</c:when>
											<c:when test="${toBean.productOrderCommon.orderStatus == 3}">游学</c:when>
											<c:when test="${toBean.productOrderCommon.orderStatus == 4}">大客户</c:when>
											<c:when test="${toBean.productOrderCommon.orderStatus == 5}">自由行</c:when>
								</c:choose>
						        
						        
						        </td>	
                                <td class="mod_details2_d1"><em class="tdred"></em>销售：</td>
						        <td class="mod_details2_d2">${toBean.productOrderCommon.salerName}</td>
                                       
							</tr>
							<tr> 
                                <td class="mod_details2_d1">下单人：</td>
						        <td class="mod_details2_d2">${toBean.productOrderCommon.createBy.name}</td>
								<td class="mod_details2_d1">下单时间：</td>
								<td class="mod_details2_d2"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${toBean.productOrderCommon.createDate}" /></td> 
                                <td class="mod_details2_d1">订单总额：</td>
								<td class="mod_details2_d2"><em class="tdred">${toBean.orderTotalMoney}</em></td>
                                <td class="mod_details2_d1">订单状态：</td>
								<td class="mod_details2_d2">
								
								${fns:getDictLabel(toBean.productOrderCommon.payStatus, "order_pay_status", "")}
								
								
								<!-- 
												<c:if test="${toBean.productOrderCommon.payStatus==0}">全部</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==1}">全款未支付</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==2}">订金未支付</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==3}">已占位</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==4}">订金已经支付</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==5}">已经支付</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==99}">已经取消订单</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==100}">可操作状态      正向平台同步</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==101}">查看状态      正向平台同步</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==111}">已经删除订单</c:if>
												<c:if test="${toBean.productOrderCommon.payStatus==199}">财务订单</c:if>
												
												 -->
								 </td>	 
                                </tr>
							<tr>
								<td class="mod_details2_d1">
操作人：</td>
						        <td class="mod_details2_d2">${toBean.productOrderCommon.createBy.updateBy.name}</td>     
							</tr>
						</tbody>
					</table>
					
					</c:forEach>
               </div>
            
		               	<div class="ydbz_tit">游客转款</div>
						<table id="" class="table activitylist_bodyer_table">
		                    <thead>
		                        <tr>
		                            <th width="10%">游客</th>
		                            <th width="10%">团号</th>
		                            <th width="10%">应收金额</th>
		                            <th width="20%">转款款项</th>
		                             <!-- 
		                            <th width="15%">转款金额</th> -->
		                        </tr>
		                    </thead>
		                    <tbody>
		                    
		                    <c:forEach    items="${listTraveler}" var="show" varStatus="loop">
											<tr>
											<input type="hidden" name="travelorId" value="${show.traveler.id}"/>
											<input type="hidden" name="travelorName" value="${show.traveler.name}"/>
											<input type="hidden" name="inOrderId" value="${show.inOrderId}"/>
												<td>${show.traveler.name} </td>
												<td >${show.groupNo}</th>
												<td class="tr">
														${show.payPriceMoney}
												 </td>
												<td>
													  <div>                                               
		                 
											                   <ol class="gai-price-ol" id=li${loop.count}  val=${loop.count}>
											                    	<li>
											                        	<i>
											                        	<select style="width:90%;" class="selectrefund" name="currency" id="currencyTemplate">
																		 <c:forEach items="${show.currencyList}" var="cur" varStatus="b">
																		 <option value="${cur.id }">${cur.currencyName }</option>
																		 </c:forEach>
																		</select></i>
											                            <i><input type="text" name="refund" data-type="rmb" onkeyup="transInputs(this)" onafterpaste="transInputs(this)"  onblur="transInputs(this)" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">费用</span></i>
											                            <i><a class="ydbz_s refund-price-btn">+增加</a></i>
											                        </li>
											                    </ol>
															</div>
																<div class="allzj tr f18"  style="display:none">
																	<div id="all-money${loop.count}"   class="all-money">转款总金额： <span></span><input type="hidden"  name="transferMoney" class="hidAllMoney"/></div>
															</div>
												
												</td>		
											</tr>
								 </c:forEach>
		                    </tbody>
		                </table>
		                
          
            </div>
            <input id ="myOrderId" type="hidden" name ="myOrderId" value="${orderId}" />
           <div class="ydBtn ydBtn2"><a class="ydbz_s gray" onclick="cancel(${orderId})">取消</a><a id="submitButton" class="ydbz_s" onclick="submitForm()">提交</a></div>
         </div>
    <!--右侧内容部分结束--> 
</div>
      </form>
</body>
</html>
