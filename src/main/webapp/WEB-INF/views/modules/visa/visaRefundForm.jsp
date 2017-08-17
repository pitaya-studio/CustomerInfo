<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证退款申请</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/visa/visaRefund.js" type="text/javascript"></script>
    
<script type="text/javascript">
var validate;
var wait = 60;
$(function(){
	//input获得失去焦点提示信息显示隐藏
	inputTips();
	//退款
	refunds('visa');
	totalRefund();
	changeRefund();
	validate = $("#addForm").validate({
		errorPlacement: function(error, element) {
            error.appendTo ( element.parent() ); 
        }
	});
});

function checkall(obj){
	if($(obj).attr("checked")){
		$("input[name='allChk']").attr("checked",'true');
		$("input[name='onceChk']").attr("checked",'true');
		$("input[name='onceChk']:checked").each(function(i,a){
			idcheckchg(a);
		});
	}else{
		$("input[name='allChk']").removeAttr("checked");
		$("input[name='onceChk']:checked").each(function(i,a){
			$(a).removeAttr("checked");
			idcheckchg(a);
		});
	}
		
}

function idcheckchg(obj){
	if($(obj).attr("checked")){
// 		alert($(obj).parent().parent().find("input[name^='refundProject']").val());
// 		alert($(obj).parent().parent().find("input[name='refund']").val());
		$(obj).parent().parent().find("input[name^='refundProject']").rules("add",{required:true,messages:{required:"必填信息"}});
		$(obj).parent().parent().find("input[name^='travelerRefund']").rules("add",{required:true,messages:{required:"必填信息"}});
	}else{
		$(obj).parent().parent().find("input[name^='refundProject']").rules("remove");
		$(obj).parent().parent().find("input[name^='travelerRefund']").rules("remove");
	}
	totalRefund();
}

function subForm() {
	///var str=document.getElementsByName(onceChk);
	var str=$("input[name^='onceChk']");
	if($('input[name=onceChk]').is(':checked') == false){
		top.$.jBox.tip('至少选择一位游客');
        return false;
	}
	/**
	if($("input[name^='travelerRefund']").val() == "")
	{
		top.$.jBox.tip('退款金额不能为空！');
        return false;
	}
	**/
	
	// travelerRefund
	
	
	//$("input[@ name ^='travelerRefund']")
	
	if(wait == 60){//防止重复提交申请
		wait = 0;
		$("input[name='travelerId'][value='0']").each(function(i,a){
			var groupRefundProduct = $(a).parent().parent().find("input[name^='refundProject']").val();
			var groupRefundPirce = $(a).parent().parent().find("input[name='refund']").val();
			if(groupRefundProduct && groupRefundPirce) {
				if($(a).next("input[id='onceChk']").length != 0) {
					$(a).next("input[id='onceChk']").remove();
				}
				$(a).after('<input id="onceChk" type="checkbox" value="-1" checked="true" name="onceChk" style="display: none;">');
			}
		});
		
		if(validate.form()) {
			
			$("#addForm").find("[name='onceChk']").each(function(i,a) {
				if($(a).attr("checked")) {
					$(a).parent().parent().find("input[name^='refundProject']").attr("name","refundProject");
					$(a).parent().parent().find("input[name^='travelerRefund']").attr("name","refund");
				}else{
					$(a).parent().parent().find("[name='travelerId']").attr("name","");
					$(a).parent().parent().find("[name='travelerName']").attr("name","");
					$(a).parent().parent().find("[name='payPrice']").attr("name","");
					$(a).parent().parent().find("[name='refundCurrency']").attr("name","");
					$(a).parent().parent().find("[name='refundMark']").attr("name","");
				}
			});
			
			$.ajax({
	            cache: true,
	            type: "POST",
	            url:"${ctx}/order/manager/visaRefund/refundApply?proId=${param.proId}",
	            data:$('#addForm').serialize(),// 你的formid
	            async: false,
	            success: function(data) {
	            	if("" != data) {
	            		$.jBox.info(data, "系统提示");
	            		return;
	            	}else{
	            		window.location.href = "${ctx}/order/manager/visaRefund/refundList?proId=${param.proId}";
	            	}
	            }
	        });
		}
	}
	

}
	
</script>

</head>
<body>
	<div class="mod_nav">订单 > 签证 > 申请退款</div>
	<div class="ydbz_tit">订单详情</div>
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
                         <td class="mod_details2_d2">单办签</td>	
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
                         <td class="mod_details2_d2">
                         	${visaProdcut.createBy.name}
                         </td>     
                         <td class="mod_details2_d1">销售：</td>
                         <td class="mod_details2_d2">${visaOrder.salerName}</td>
                     </tr>
                 </tbody>
         	</table>	
        </div>
        <div class="ydbzbox fs" style="overflow: hidden;">
			<div class="ydbz_tit">游客退款</div>
			<select id="currencyTemplate" style="display:none;" name="currencyTemplate">
				<c:forEach items="${currencyList}" var="currency">
					<option value="${currency.id}">${currency.currencyName}</option>
				</c:forEach>
			</select>
			<form id="addForm" action="" method="post">
                 <table id="contentTable" class="table activitylist_bodyer_table">
                    <thead>
                    	<tr>
                    		<th width="5%">
								全选</br>
								<input id="allChk" type="checkbox" name="allChk" onClick="checkall(this)">
							</th>
							<th width="5%">游客</th>
							<th width="20%">退款款项</th>
							<th width="10%">币种</th>
							<th width="20%">应收金额</th>
							<th width="20%">退款金额</th>
							<th width="20%">备注</th>
						</tr>
                    </thead>
                    <tbody>
	                    <c:forEach items="${travelerList}" var="traveler" varStatus="s">
	                     <tr>
	                     	<td width="5%" class="tc"><input id="onceChk" type="checkbox" name="onceChk" onClick="idcheckchg(this)"></td>
	                     	<td width="5%">${traveler[0].name}</td>
							<td class="p0" width="90%" style="border:none" colspan="5">
								<table class="refundTable" width="100%" style="border:none">
									<tbody>
										<tr>
											<input type="hidden" value="${traveler[0].id}" name="travelerId"/>
											<input type="hidden" value="${traveler[0].name}" name="travelerName"/>
											<td class="refundtd" width="22%">
												<input id="refundProject${s.count}-1" class="inputTxt" type="text" name="refundProject${s.count}-1"/>
												
											</td>
											<td class="tc" width="11%">
												<select class="selectrefund" name="refundCurrency" style="width:90%;">
													<c:forEach items="${currencyList}" var="currency">
														<option value="${currency.id}" <c:if test="${currency.currencyName eq '人民币'}">selected="selected"</c:if> >${currency.currencyName}</option>
													</c:forEach>
												</select>
											</td>
											<td class="tr" width="22%">
												<span class="tdgreen">${fns:getMoneyAmountBySerialNum(traveler[1],2)}<input type="hidden" value="${fns:getMoneyAmountBySerialNum(traveler[1],2)}" name="payPrice"/></span>
											</td>
											<td width="22%">
												<input type="text" id="travelerRefund${s.count}-1" onblur="refundInputs(this,1)" onafterpaste="refundInput(this)" onkeyup="refundInput(this)" name="travelerRefund${s.count}-1" var="travelerRefundPirce"/>
											</td>
											<td width="22%">
												<input type="text" name="refundMark">
											</td>
										</tr>
									</tbody>
								</table>
							</td>
	                     </tr>
	                    </c:forEach>
	                    <c:if test="${empty travelerList}">
	                    	   <td colspan="7"><div class="wtjqw">暂无可以申请退款的游客，请确认游客款项已达账。</div></td>
	                    </c:if>
                    </tbody>
                 </table>
<!--                  <div class="ydbz_tit">团队退款</div> -->
<!--                  <div> -->
<!-- 					<ol class="gai-price-ol"> -->
<!-- 						<li> -->
<!-- 							<i> -->
<!-- 								<input type="hidden" value="0" name="travelerId"/> -->
<!-- 								<input type="hidden" value="团队退款" name="travelerName"/> -->
<!-- 								<input class="gai-price-ipt1" type="text" flag="istips" id="refundProject0-1" name="refundProject1"> -->
<!-- 								<span class="ipt-tips ipt-tips2">款项</span> -->
<!-- 							</i> -->
<!-- 							<i> -->
<!-- 								<select class="selectrefund" name="refundCurrency" style="width:90%;"> -->
<!-- 									<c:forEach items="${currencyList}" var="currency"> -->
<!-- 										<option value="${currency.id}" <c:if test="${currency.currencyName eq '人民币'}">selected="selected"</c:if> >${currency.currencyName}</option> -->
<!-- 									</c:forEach> -->
<!-- 								</select> -->
<!-- 							</i> -->
<!-- 							<i> -->
<!-- 								<input type="hidden" value="${fns:getMoneyAmountBySerialNum(visaOrder.totalMoney,2) }" name="payPrice"/> -->
<!-- 								<input class="gai-price-ipt1" var="travelerRefundPirce" type="text" flag="istips" onblur="refundInputs(this,1)" onafterpaste="refundInput(this))" onkeyup="refundInput(this)" name="refund"> -->
<!-- 								<span class="ipt-tips ipt-tips2">费用</span> -->
<!-- 							</i> -->
<!-- 							<i> -->
<!-- 								<input class="gai-price-ipt2" type="text" flag="istips" name="refundMark"> -->
<!-- 								<span class="ipt-tips ipt-tips2">备注</span> -->
<!-- 							</i> -->
<!-- 							<i><a class="ydbz_s refund-price-btn">+增加</a></i> -->
<!-- 						</li> -->
<!-- 					</ol> -->
<!-- 				 </div> -->
				 <div class="allzj tr f18">
					 <div class="all-money">
					  	退款总金额：
						<span>0</span>
					 </div>
				 </div>
	             <div class="ydBtn ydBtn2">
	             	 <input class="ydbz_x" type="button" onClick="subForm()" value="提交"/>
	                 <input class="ydbz_x gray" type="button" value="返回" onClick="history.go(-1)"/>
	             </div>
			</form>
		</div>
</body>
</html>