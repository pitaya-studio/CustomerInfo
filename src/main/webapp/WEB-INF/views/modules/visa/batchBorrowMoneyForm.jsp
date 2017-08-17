<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证借款申请</title>
<meta name="decorator" content="wholesaler" />

<script type="text/javascript">

	$(function(){
		g_context_url = "${ctx}";
		//input获得失去焦点提示信息显示隐藏
		inputTips();
		$("select[class*='borrowCurrency']").bind("change", {}, totalRefund);
		
		//全部还原
		$(".re-storeall").click(function(){
			$(".modifyPrice-table").find(".borrowPrice").each(function(index, element) {
	            $(element).val($(element).attr("defaultvalue")).trigger("focusout");
	            $(element).parents("[group='travler1']").find(".borrowCurrency").find("option").eq(0).attr("selected","selected");
	        	$(element).parents("[group='travler1']").find("[name='trvborrownotes']").val("");
			});
			totalRefund();
		});
		
		employee("employee");
		
	});
	function subForm() {
		if($(".borrowPrice[value!=''][value!='0'][value!='-']").length) {
			$.ajax({
				cache: true,
	            type: "POST",
	            url:g_context_url+"/visa/workflow/borrowmoney/createVisaJK4XXZ",
	            data:$('#addForm').serialize(),
	            async: false,
	            success: function(data) {
	            	top.$.jBox.tip(data.visaJKreply);
	            	window.location.href=g_context_url+"/visa/order/borrowMoneyRecord?orderId="+data.orderId;
	            }
			});
		}else{
			top.$.jBox.tip("借款金额不能为0！");
		}
	}
	
	function totalRefund(){
		var selects=$("select[class*='borrowCurrency']");
		var totalcost="";
		$("#currencyTemplate option").each(function(i, e) {
			var datatype=$(e).text();
			e=$(e).val();
			var money = 0;
			selects.each(function(index, element) {
				var si = $(selects[index]);
				var s = si.val();
				var checkinput=si.parents("tr").find("input[type='checkbox']");
				if(checkinput.prop("checked") || checkinput.length==0){
					if(s == e){
						var n = si.parent().parent().find("input[var='travelerRefundPirce']").val();
						if(n==""){n=0;}else{money += parseFloat(n);}
					}  
				}			
			});
			if(money!=""||money!=0){
				datatype="<font class='tdgreen'> + </font><font class='gray14'>"+datatype+"</font>";
				money="<span class='tdred'>"+milliFormat(money,'1')+"</span>";
				totalcost+=datatype+money;
			}
		});
		if(totalcost==0){
			$('.all-money').find('span').html(0);
		}else{
			$('.all-money').find('span').html(totalcost);
		}
		$('.all-money').find('span').find(".tdgreen").first().hide();
	}
	
	function addProject(obj) {
	var html = '<li><i><input class="gai-price-ipt1" type="text" flag="istips" name="groupborrownames">';
		html += '<span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i>{1}</i>&nbsp;'.replace("{1}", $(".borrowCurrency[name='teamCurrency']").clone().show().prop('outerHTML'));
		html += '<i><input var="travelerRefundPirce" class="gai-price-ipt1 borrowPrice" type="text" value="" onafterpast="validNum(this)" onblur="validNum1(this);refundInput1(this)" flag="istips" name="teamMoney">';
		html += '<span class="ipt-tips ipt-tips2">费用</span></i>';
		html += '&nbsp;<i><input class="gai-price-ipt2" type="text" flag="istips" name="groupborrownodes">';
		html += '<span class="ipt-tips ipt-tips2">备注</span></i>';
		html += '<i><a class="ydbz_s gray clear-btn" onClick="$(this).parent().parent().remove();totalRefund()">删除</a></i></li>';
		
		$(obj).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
		//新增加的退款项目币种选择框绑定事件
		$("select[class*='borrowCurrency']").bind("change", {}, totalRefund);
	}
	
	
	// if(/\D/.test(this.value))
	function validNum1(dom){
		var thisvalue = $(dom).val();
		var minusSign = false;
		if(thisvalue){
			// 	if(/^\-/.test(thisvalue))
			if(/^\-/.test(thisvalue)){
				minusSign = true;
				thisvalue = thisvalue.substring(1);
			}
			//thisvalue = thisvalue.replace(/\D/g,"");
			thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
			var txt = thisvalue.split(".");
	        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
			if(minusSign){
				thisvalue = "-" +thisvalue;
				if(thisvalue.length==1){
					thisvalue="";
				}
				
			}
			$(dom).val(thisvalue);
		}else{
			//$(dom).val(0);
		}
	}
	
	//订单团队退款
	function refundInput1(obj){
		var ms = obj.value.replace(/[^\d\.-]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = ms.split(".");
		obj.value  = txt[0]+(txt.length>1?"."+txt[1]:"");
		totalRefund();
	}
	
	
	function employee(productType){
		$('.modifyPrice-table').on("mouseover",".huanjia",function(){
			
			$(this).addClass("huanjia-hover").find('dt input').attr('defaultValue');
			$(this).find('dd').show();
			
		}).on("mouseout",".huanjia",function(){
			
			$(this).removeClass("huanjia-hover").find('dd').hide();
			
		}).on("click","[flag=appAll]",function(){
			
			var lendvalue = $(this).parents(".huanjia").find("[name='lendPrice']").val();
			
			$("[name='lendPrice']").each(function(){
				$(this).parents(".huanjia").find("[name='lendPrice']").val(lendvalue);
			});
			totalRefund();
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='lendPrice']").val("");
			totalRefund();
			
		});
	}
	

</script>

</head>
<body>
	<div class="mod_nav">订单 &gt; 签证 &gt; 借款记录 &gt; 借款申请</div>
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
    	<p class="ydbz_mc">${visaProduct.productName }</p>
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
		<form id="addForm" action="" method="post">
		       <input type="hidden" value="${visaOrderId}" name="visaOrderId" />
		
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
		            <select id="currencyTemplate" style="display:none;" name="currencyTemplate">
						<c:forEach items="${currencyList}" var="currency">
							<option value="${currency.id}">${currency.currencyName}</option>
						</c:forEach>
					</select>
                    <c:forEach items="${travelerList}" var="traveler" varStatus="s">
                    <tr group="travler1">
				  	  <td class="tc">
				  	  	 ${traveler.name }
				  	  	 <input type="hidden" value="${traveler.id }" name="trvids" />
				  	  	 <input type="hidden" value="${traveler.name }" name="trvnames" />
		  	  		  </td>
				  	  <td class="tc">
				  	  	 <select name="refundCurrency" class="borrowCurrency" >
					  	  	 <c:forEach items="${currencyList }" var="currency">
					  	  	 	<option value="${currency.id }">${currency.currencyName }</option>
					  	  	 </c:forEach>
				  	  	 </select>
				  	  </td>
				  	  <td class="tc">
				  	  	 <c:if test="${not empty traveler.payPriceSerialNum }">
				  	  		${fns:getMoneyAmountBySerialNum(traveler.payPriceSerialNum,2) }
				  	  		<input type="hidden" value="${fns:getMoneyAmountBySerialNum(traveler.payPriceSerialNum,2) }" name="trvsettlementprices" />
				  	  	 </c:if>
			  	  	  </td>
				  	  <td class="tc">
				  	  	 <dl class="huanjia">
							<dt>
								<input var="travelerRefundPirce" class="borrowPrice" type="text" onafterpaste="validNum1(this))" onblur="validNum1(this);refundInput1(this)" defaultvalue="" value="" name="lendPrice">
							</dt>
							<dd>
								<div class="ydbz_x" flag="appAll">应用全部</div>
								<div class="ydbz_x gray" flag="reset">清空</div>
							</dd>
						 </dl>
	  	  			  </td>
		  	  		  <td class="tc">
		  	  		  	<input type="text" name="trvborrownotes" />
		  	  		  </td>
                    </tr>
                    </c:forEach>
                    <c:if test="${empty travelerList}">
                    	   <td colspan="7"><div class="wtjqw"></div></td>
                    </c:if>
                 </tbody>
              </table>
             
             	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
					<div class="ydbz_x fl re-storeall">全部重置</div>
<!-- 					<div class="fr f14 all-money" style="font-size:18px;font-weight:bold;"> -->
<!-- 						游客借款差额： -->
<!-- 						<span></span> -->
<!-- 					</div> -->
				</div>
				<div class="ydbz_tit">
					<span class="fl">团队借款</span>
				</div>
				<div>
					<ol class="gai-price-ol">
						<li>
							<i>
								<input class="gai-price-ipt1" type="text" flag="istips" name="groupborrownames">
								<span class="ipt-tips ipt-tips2">其他款项</span>
							</i>
							<i>
								<select name="teamCurrency" class="borrowCurrency">
							  	  	 <c:forEach items="${currencyList }" var="currency">
							  	  	 	<option value="${currency.id }">${currency.currencyName }</option>
							  	  	 </c:forEach>
						  	  	 </select>
							</i>
							<i>
								<input var="travelerRefundPirce" class="gai-price-ipt1 borrowPrice" type="text" value="" onafterpast="validNum1(this)" onblur="validNum1(this);refundInput1(this)" flag="istips" name="teamMoney">
								<span class="ipt-tips ipt-tips2">费用</span>
							</i>
							<i>
								<input class="gai-price-ipt2" type="text" flag="istips" name="groupborrownodes">
								<span class="ipt-tips ipt-tips2">备注</span>
							</i>
							<i>
								<a class="ydbz_s gai-price-btn" onClick="addProject(this)">+增加</a>
							</i>
						</li>
					</ol>
				</div>
				<div class="ydbz_tit">
					<span class="fl">备注</span>
				</div>
				<dl class="gai-price-tex">
					<dd>
						<textarea class="" name="otherRemarks" cols="" rows=""></textarea>
					</dd>
				</dl>
				<div class="allzj tr f18">
					<div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">
						借款差额：
						<span></span>
					</div>
				</div>
             <div class="dbaniu" style="width:122px;">
             	 <input class="ydbz_x" type="button" onClick="subForm()" value="提交"/>
                 <input class="ydbz_x gray" type="button" value="关闭" onClick="javaScript:window.close();"/>
             </div>
		</form>
	</body>
</html>