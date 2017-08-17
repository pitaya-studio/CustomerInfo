<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>审批-新行者签证借款</title>
<!-- 页面左边和上边的装饰 -->
<meta name="decorator" content="wholesaler" />


<link type="text/css" rel="stylesheet"  href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet"  href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"  src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"  src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
	$(function() {
		//AA码
		AAHover();
		if ("${reply}" != null && "${reply}" != '') {
			alert("${reply}");
		}
	});
	
	//驳回
	function jbox_bohui(){
		//alert("驳回");
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				$("#result").val(0);//代表驳回按钮
				$("#denyReason").val(f.reason);
				$("#searchForm").submit();
			}
		},height:250,width:500});
	};
	//审核通过
	function review(){
		//alert("审批");
		var html = '<div class="add_allactivity"><label>请填写审批备注信息!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" name="reason" cols="" rows="" maxlength="100" oninput="this.value=this.value.substring(0,100)" ></textarea>';
		html += '</div>';
	
		$.jBox(html, { title: "审核备注",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				
				/* if(window.opener){
					window.opener.parent.close();
				} */
				$("#result").val(1);//代表审批
				$("#denyReason").val(f.reason);
				$("#searchForm").submit();
				
				
			}
		},height:250,width:500});
		
		//$("#result").val(1);//代表审核通过按钮
		//$("#searchForm").submit();
	}
	

	
</script>
</head>
<body>
		
	    <!--右侧内容部分开始-->
		<div class="mod_nav">审批 > 新行者签证借款 > 新行者签证借款审批</div>
		<div class="ydbz_tit">订单信息</div>
		<div class="orderdetails1">
             <table border="0" style="margin-left: 25px" width="98%">
                 <tbody>
                     <tr>
                         <td class="mod_details2_d1">下单人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                         <td class="mod_details2_d1">下单时间：</td>
                         <td class="mod_details2_d2">
                         	<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaOrder.createDate}"/>
                       	</td> 
                         <td class="mod_details2_d1">团队类型：</td>
                         <td class="mod_details2_d2">
                         	<c:choose>
                         		<c:when test="${empty visaOrder}">单办签</c:when>
                         		<c:otherwise>参团</c:otherwise>
                         	</c:choose>
						 </td>	
                         <td class="mod_details2_d1">收客人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                     </tr>
                     <tr> 
                         <td class="mod_details2_d1">订单编号：</td>
                         <td class="mod_details2_d2">${visaOrder.orderNo}</td>
                         <td class="mod_details2_d1">订单团号：</td>
                         <td class="mod_details2_d2">${visaOrder.groupCode}</td>
                         <td class="mod_details2_d1">订单总额：</td>
                         <td class="mod_details2_d2"><em class="tdred">${fns:getMoneyAmountBySerialNum(visaOrder.totalMoney,2)}</em></td>
                         <td class="mod_details2_d1">订单状态：</td>
                         <td class="mod_details2_d2">
                         	<c:if test="${not empty visaOrder.visaOrderStatus}">
								<c:choose>
									<c:when test="${visaOrder.visaOrderStatus eq '0'}">未收款</c:when>
									<c:when test="${visaOrder.visaOrderStatus eq '1'}">已收款</c:when>
									<c:when test="${visaOrder.visaOrderStatus eq '2'}">已取消</c:when>
								</c:choose>
							</c:if>
						 </td>	 
                     </tr>
                     <tr>
                         <td class="mod_details2_d1">操作人：</td>
                         <td class="mod_details2_d2">${visaProduct.createBy.name}</td>     
                         <td class="mod_details2_d1">办签人数：</td>
                         <td class="mod_details2_d2">${visaOrder.travelNum }</td>
                         <td class="mod_details2_d1">销售：</td>
                         <td class="mod_details2_d2">${visaOrder.salerName}</td>
                     </tr>
                 </tbody>
         	</table>	
        </div>
    <div class="ydbz_tit">产品信息</div>
    <div class="mod_information_dzhan_d mod_details2_d" style="overflow:hidden;">
    	<p class="ydbz_mc">${visaProduct.productName}</p>
    	<table border="0" width="90%" style="margin-left: 25px">
    		<tbody>
    			<tr>
    				<td class="mod_details2_d1">产品编号：</td>
    				<td class="mod_details2_d2">${visaProduct.productCode }</td>
    				<td class="mod_details2_d1">签证国家：</td>
    				<td class="mod_details2_d2">
    					<c:if test="${not empty visaProduct.sysCountryId }">
	    					${fns:getCountryName(visaProduct.sysCountryId) }
    					</c:if>
    				</td>
    				<td class="mod_details2_d1">签证类别：</td>
    				<td class="mod_details2_d2">
    					<c:if test="${not empty visaProduct.visaType }">
	    					${fns:getDictLabel(visaProduct.visaType,'new_visa_type','') }
    					</c:if>
   					</td>
    				<td class="mod_details2_d1">领区：</td>
    				<td class="mod_details2_d2">
						<c:if test="${not empty visaProduct.collarZoning }">
                          	${fns:getDictLabel(visaProduct.collarZoning,'from_area','')}
                      	</c:if>
					</td>
    			</tr>
    			<tr>
    				<td class="mod_details2_d1">应收价格：</td>
    				<td class="mod_details2_d2">${fns:getCurrencyInfo(visaProduct.currencyId,0,'mark') }&nbsp;${visaProduct.visaPay }/人</td>
    				<td class="mod_details2_d1">创建时间：</td>
    				<td class="mod_details2_d2">
    					<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate }"/>
    				</td>
    			</tr>
    		</tbody>
    	</table>
    </div>
    <div class="ydbz_tit">
		<span class="fl">游客借款</span>
	</div>
    <span style="padding-left:30px;">报批日期：</span>${revCreateDate}
    <table class="activitylist_bodyer_table modifyPrice-table">
     <thead>
        <tr>
        <th width="8%">姓名</th>
  	   <th width="12%">币种</th>
	   <th width="11%">游客结算价</th>
	   <th width="13%">借款金额</th>
	   <th width="20%">备注</th>
        </tr>
     </thead>
     
      <tbody>
      <c:forEach items="${travelerList}" var="traveler" varStatus="s">
      <tr group="travler1">
	  	  <td class="tc">${traveler.tname}</td>
	  	  <td class="tc">${traveler.crrencyName}</td>
	  	  <td class="tc">${traveler.trvsettlementprice}</td>
	  	  <td class="tc">${traveler.trvamount}</td>
	  	  <td class="tc">${traveler.trvborrownote}</td>
        </tr>
       </c:forEach>
         <c:if test="${empty travelerList}">
         	   <td colspan="5"><div class="wtjqw"></div></td>
         </c:if>
      </tbody>
      </table>
      
      

      
      <div class="ydbz_tit">
		<span class="fl">团队借款</span>
	 </div>
	 <table class="activitylist_bodyer_table modifyPrice-table">
	     <thead>
	        <tr>
	        <th width="8%">费用名称</th>
	  	   <th width="12%">币种</th>
		   <th width="11%">订单金额</th>
		   <th width="13%">借款金额</th>
		   <th width="20%">备注</th>
	        </tr>
	     </thead>
	     
	      <tbody>
	      <c:forEach items="${groupList}" var="groupborrowitem" varStatus="s">
	      <tr group="travler1">
		  	  <td class="tc">${groupborrowitem.groupborrowname}</td>
		  	  <td class="tc">${groupborrowitem.groupborrowcurrent}</td>
		  	  <td class="tc">${fns:getMoneyAmountBySerialNum(totalMoney,'2')}</td>
		  	  <td class="tc">${groupborrowitem.groupborrowamount}</td>
		  	  <td class="tc">${groupborrowitem.groupborrownode}</td>
	        </tr>
	       </c:forEach>
	         <c:if test="${empty groupList}">
	         	   <td colspan="5"><div class="wtjqw"></div></td>
	         </c:if>
	      </tbody>
      </table>
   	  <div  class="ydbz_foot">
         <div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">借款金额：
                <!--                 
                <span style="font-size:12px;">￥</span>
                <span class="red" >800</span>
                <span style="color:green;">+</span>
                <span style="font-size:12px;">$</span>
                <span class="red" >800</span>
                 -->
                ${totalborrowamount}
          </div>
	  </div>
      
      
	<div class="ydbz_tit">
	    <span class="fl">申请备注</span>
	</div>
	<dl class="gai-price-tex">
		<dd>
			<textarea class="" name="otherRemarks" cols="" rows="" disabled="disabled">${grouptotalborrownode}</textarea>
		</dd>
	</dl>
	<!-- 270_添加还款日期_djw_start_ -->
	<div class="allzj">
		<td class="mod_details2_d1">&nbsp;&nbsp;还款日期：</td>
    	<td class="mod_details2_d2">${refundDate}</td>
	</div> 
	<div class="allzj"></div>
	<!-- 270_添加还款日期_djw_end_ -->
		
	<!-- 新行者  审核  -->
    <!-- %@ include file="/WEB-INF/views/review/newActivitiReviewLogInfo.jsp"%-->
    <!-- 新行者  审核 -->
    <%@ include file="/WEB-INF/views/review/common/reviewLogBaseInfo.jsp"%>
                                         
	<form id="searchForm" action="${ctx}/visa/xxz/borrowmoney/review4XXZVisaBorrowMoneyNew" method="post">
			<!-- 添加提交请求所需数据 -->
			<input type = "hidden"  id = "revid" name="revid" value = "${revid}"/>
			<!-- 1：审核通过按钮，0：驳回按钮 -->
			<input type = "hidden"  id = "result" name="result"/>
			<input type = "hidden"  id = "denyReason" name="denyReason"/>
<%-- 			<input type = "hidden"  id = "nowLevel" name="nowLevel" value = "${nowLevel}"/> --%>
			<input type = "hidden"  id = "orderId" name="orderId" value = "${orderId}"/>
			<input type = "hidden"  id = "travelerId" name="travelerId" value = "${travelerId}"/>
			<input type = "hidden"  id = "flowType" name="flowType" value = "${flowType}"/>
			<input type = "hidden"  id = "flag" name="flag" value = "${flag}"/>
			<div class="dbaniu"  style="text-align:center;">
			      <c:choose> 
				     <c:when test="${flag==0}"> 
		      				<a class="ydbz_s gray" onclick="javaScript:history.back();">取消</a>
							<a class="ydbz_s" onclick="jbox_bohui();">驳回</a>
							<a class="ydbz_s" onclick="review();">审批通过</a>
				     </c:when> 
				     <c:otherwise> 
				      	   <a class="ydbz_s gray" onclick="javaScript:history.back();">返回</a>
				      	   <a class="ydbz_s" onclick="javaScript:window.close();">关闭</a>
				     </c:otherwise>
				</c:choose>
			</div>
	</form>
	<!--右侧内容部分结束-->
</body>
</html>