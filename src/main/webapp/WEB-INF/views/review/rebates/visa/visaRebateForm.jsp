<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
		<title>签证宣传费申请</title>
	</c:when>
	<c:otherwise>
		<title>签证返佣申请</title>
	</c:otherwise>
</c:choose>   

<meta name="decorator" content="wholesaler" />
<style type="text/css">
     .rebateObj select {
         margin-top: 0px;
     }
	.supplyNameSpan,
	.supplyInfoSpan {
	    display: none;
	}
	.rabateInfo {
	    margin-left: 200px;
	}
	.rebateObj select {
	    margin-top: 10px;
	    width: 130px;
	    height: 28px;
	}
</style>
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
	            $(element).parents("[group='travler1']").find("[name='trvborrownames']").val("");
	        	$(element).parents("[group='travler1']").find("[name='trvborrownotes']").val("");
			});
			totalRefund();
		});
		
		employee("employee");
		
		//返佣对象的选择 如果为供应商则带出供应商信息---C4750--start
	    $('.supplyName').comboboxInquiry();
	    $(document).on('change', '.rebateTarget', function () {
			emptyAndAppend(new Array('supplyInfo','accountType','accountName','accountNo'));
	        if ($(this).find('option:selected').val() == '2') {
	        	$("#supplyInfo").comboboxInquiry('reset');
	        	getAjaxSelect('supplyInfo',this);
	            $('.supplyNameSpan').show();
	        }
	        else {
	            $('.supplyNameSpan').hide();
	            $('.supplyInfoSpan').hide();
	        }
	    });
	    $(document).on('comboboxinquiryselect', '.supplyName', function () {
			emptyAndAppend(new Array('accountType','accountName','accountNo'));
	        var selectedSupply = $(this).find('option:selected').val();
	        if (selectedSupply != '0') {
	            $('.supplyInfoSpan').show();
	        }
	        else {
	            $('.supplyInfoSpan').hide();
	        }
	    });
	    $(document).on('change', '.accountType', function () {
			emptyAndAppend(new Array('accountName','accountNo'));
	    	var accountType = $(this).find('option:selected').val();
	        if (accountType != '') {
	        	var obj = $("#supplyInfo");
	        	getAjaxSelect('accountType',obj,accountType);
	            $('.supplyNameSpan').show();
	        }
	    });
	    $(document).on('change', '.accountName', function () {
			emptyAndAppend(new Array('accountNo'));
	    	var accountName = $(this).find('option:selected').val();
	        if (accountType != '') {
	        	var obj = $("#supplyInfo");
	        	var accountType = $("#accountType").val();
	        	getAjaxSelect('accountNo',obj,accountType,accountName);
	            $('.supplyNameSpan').show();
	        }
	    });
	    //返佣对象的选择 如果为供应商则带出供应商信息---C4750--end
		
	});
	function subForm() {
		if($("#rebateObject").val()==1){
			if($("#rebateTarget").val()=="请选择"){
				 jBox.tip("请选择返佣对象！");
				 return false;
			}
			if($("#rebateTarget").val()==2){
				if($("#supplyInfo").val()=="请选择"){
					 jBox.tip("请选择供应商名称！");
					 return false;
				}
				if($("#accountType").val()=="请选择"){
					 jBox.tip("请选择账户类型！");
					 return false;
				}
				if($("#accountName").val()=="请选择"){
					 jBox.tip("请选择开户行名称！");
					 return false;
				}
				if($("#accountNo").val()=="请选择"){
					 jBox.tip("请选择账户号码！");
					 return false;
				}
			}
		}
		var flag = true;
		$("#supplyName").val($("#supplyInfo").find("option:selected").text());
		if($(".borrowPrice[value!=''][value!='0'][value!='-']").length) {
			$("#rebatesId").addClass("disableCss");
			$.ajax({
				url:g_context_url+'/visa/rebate/checkMutex',
				type:'post',
				data:$('#addForm').serialize(),
				success:function(d){
					var obj = eval(d);
					if(obj.success == true){
						$.ajax({
							cache: true,
				            type: "POST",
				            url:g_context_url+"/visa/rebate/visaRebatesAppliy",
				            data:$('#addForm').serialize(),					            	
				            success: function(data) {
				            	if(data.code == 200) {
				            		top.$.jBox.tip(data.visaJKreply);
				            		flag = false;
				            		$("#rebatesId").removeClass("disableCss");
				            		return false;
				            	}
				            	top.$.jBox.tip(data.visaJKreply);
				            	window.location.href=g_context_url+"/visa/rebate/visaRebateList?orderId="+$("input[name=visaOrderId]").val();
				            }
						});
						
					}else{
						top.$.jBox.info(obj.messageString,'系统提示');
					}
				}
				
			});
			
		}else{
			top.$.jBox.tip("返佣差额不能为0！");
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
	var html = '<li><i><input class="gai-price-ipt1" type="text" flag="istips" name="groupborrownames" maxlength="15">';
		html += '<span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i>{1}</i>&nbsp;'.replace("{1}", $(".borrowCurrency[name='teamCurrency']").clone().show().prop('outerHTML'));
		html += '<i><input var="travelerRefundPirce" class="gai-price-ipt1 borrowPrice" type="text" value="" onafterpast="validNum(this)" onblur="validNum1(this);refundInput1(this)" flag="istips" name="teamMoney" maxlength="15">';
		html += '<span class="ipt-tips ipt-tips2">费用</span></i>';
		html += '&nbsp;<i><input class="gai-price-ipt2" type="text" flag="istips" name="groupborrownodes" maxlength="30">';
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
	//级联查询
	function getAjaxSelect(type,obj,accountType,accountName){
		$.ajax({
			type: "POST",
		   	url: "${ctx}/order/newAirticketRebate/ajaxCheck",
		   	async: false,
		   	data: {
					"type":type,
					"val":$(obj).val(),
					"accountType":accountType,
					"accountName":accountName
				  },
			dataType: "json",
		   	success: function(data){
		   		if(data){
		   			if(type=="supplyInfo"){ 
			   			$.each(data,function(i,n){
			   			     $("#supplyInfo").append($("<option/>").text(n.name).attr("value",n.id));
			   			});
		   			}else if(type=="accountType"){
		 	   			$.each(data,function(i,n){
			   			     $("#accountName").append($("<option/>").text(n.name).attr("value",n.name));
			   			});
		   		    }else{
		   		    	$.each(data,function(i,n){
		   		    		$("#accountNo").empty();
		   		    		$("#accountNo").append($("<option/>").text(n.no).attr("value",n.id));
			   			});
		   		    }
		   		}
		   	}
		});
	 }
	 //清空select选项信息
	 function emptyAndAppend(targets){
		 for(var i=0;i<targets.length;i++){
			 var target = targets[i];
			 if(target != 'accountType'){
				 $("#"+target).empty();
				 $("#"+target).append("<option>请选择</option>");
			 }else{
				 $("#"+target).empty();
				 $("#"+target).append("<option>请选择</option>");
				 $("#"+target).append("<option value='1'>境内</option>");
				 $("#"+target).append("<option value='2'>境外</option>"); 
			 }
		}
	 }
</script>
<style type="text/css">
.disableCss{
pointer-events:none;
color:#afafaf;
cursor:default
} 
</style>
</head>
<body>
	<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
	<c:choose>
		<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
			<div class="mod_nav">订单 &gt; 签证 &gt; 宣传费记录 &gt; 宣传费申请</div>
		</c:when>
		<c:otherwise>
			<div class="mod_nav">订单 &gt; 签证 &gt; 返佣记录 &gt; 返佣申请</div>
		</c:otherwise>
	</c:choose>   
	
	<input type="hidden" value="${isAllowMultiRebateObject}" id="rebateObject"/>
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
                         <td class="mod_details2_d2">单办签</td>
                         <td class="mod_details2_d1">收客人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
                                
                     </tr>
                     <tr> 
                         <td class="mod_details2_d1">订单编号：</td>
                         <td class="mod_details2_d2">${visaOrder.orderNo}</td>
                         <!-- C460V3 所有批发商团号取自产品团号 -->
                         <c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
                              <td class="mod_details2_d1">订单团号：</td>
                         </c:if>
                          <c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
                              <td class="mod_details2_d1">团号：</td>
                         </c:if>
                         <%-- <td class="mod_details2_d2">${visaProduct.groupCode}</td> --%>
                         <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-订单->签务签证订单->操作->返佣 -->
                            <c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行 -->
		  					<td class="mod_details2_d2">${visaOrder.groupCode }</td>
							</c:if>
							<c:if test="${fns:getUser().company.uuid ne'7a816f5077a811e5bc1e000c29cf2586'}"><!-- 非环球行 -->
		  					<td class="mod_details2_d2">${visaProduct.groupCode }</td>
							</c:if>
                          <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e -->
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
    				<td class="mod_details2_d2"><font color="red">${fns:getCurrencyInfo(visaProduct.currencyId,0,'mark') }&nbsp;${visaProduct.visaPay }/人</font></td>
    				<td class="mod_details2_d1">创建时间：</td>
    				<td class="mod_details2_d2">
    					<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${visaProduct.createDate }"/>
    				</td>
    			</tr>
    		</tbody>
    	</table>
    </div>
    <%-- C475新增返佣对象 add by wangxu --%>
 <form id="addForm" action="" method="post">
 <c:if test="${isAllowMultiRebateObject == 1 }">
	<div class="ydbz_tit">
		 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
		<c:choose>
			<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
				 <span class="fl">宣传费对象</span>
			</c:when>
			<c:otherwise>
				 <span class="fl">返佣对象</span>
			</c:otherwise>
		</c:choose>   
        
    </div>
    <div class="rebateObj">
		<span>
			<label><em class="xing">*</em>对象类型：</label>
			<select class="rebateTarget" id="rebateTarget">
			    <option>请选择</option>
			    <option value="1">渠道</option>
			    <option value="2">供应商</option>
			</select>
		</span>
		<span class="supplyNameSpan">
			<label><em class="xing">*</em>供应商名称：</label>
			<select class="supplyName" id="supplyInfo" name="supplyInfo">
			    <option value="0">请选择</option>
			</select>
		</span>
		<input id="supplyName" type="hidden" name="supplyName" value="">
		<span class="supplyInfoSpan">
			<label><em class="xing">*</em>账户类型：</label>
			<select class="accountType" id="accountType">
			    <option>请选择</option>
			    <option value="1">境内</option>
			    <option value="2">境外</option>
			</select>
			<label><em class="xing">*</em>开户行名称：</label>
			<select class="accountName" id="accountName">
			    <option>请选择</option>
			</select>
			<label><em class="xing">*</em>账户号码：</label>
			<select id="accountNo" name="accountNo">
			    <option>请选择</option>
			</select>
		</span>
    </div>
  </c:if>
	<div class="ydbz_tit">
			 <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
			<c:choose>
				<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
					 <span class="fl">个人宣传费</span>
				</c:when>
				<c:otherwise>
					 <span class="fl">个人返佣</span>
				</c:otherwise>
			</c:choose>   
	</div>
		      <input type="hidden" value="${visaOrderId}" name="visaOrderId" />
              <table class="activitylist_bodyer_table modifyPrice-table">
                 <thead>
                    <tr>
                       <th width="6%">姓名</th>
				  	   <th width="8%">币种</th>
				  	   <th width="8%">款项</th>
					   <th width="8%">应收金额</th>
					    <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
								 	<th width="8%">预计个人宣传费金额</th>
									 <th width="8%">累计宣传费金额</th>
									 <th width="8%">宣传费差额</th>
							</c:when>
							<c:otherwise>
								    <th width="8%">预计个人返佣金额</th>
									 <th width="8%">累计返佣金额</th>
									 <th width="8%">返佣差额</th>
							</c:otherwise>
						</c:choose>   
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
		  	  		  	<input type="text" name="trvborrownames" maxlength="15"/>
		  	  		  </td>
				  	  <td class="tc">
				  	  	 <c:if test="${not empty traveler.payPriceSerialNum }">
				  	  		${fns:getMoneyAmountBySerialNum(traveler.payPriceSerialNum,2) }
				  	  		<input type="hidden" value="${fns:getMoneyAmountBySerialNum(traveler.payPriceSerialNum,2) }" name="trvsettlementprices" />
				  	  	 </c:if>
			  	  	  </td>
			  	  	  <td class="tc">${fns:getMoneyAmountBySerialNum(traveler.rebatesMoneySerialNum,2) }</td>
			  	  	  <td class="tc"><c:if test="${not empty traveler.totalRebateJe }">${traveler.totalRebateJe }</c:if></td>
				  	  <td class="tc">
				  	  	 <dl class="huanjia">
							<dt>
								<input var="travelerRefundPirce" class="borrowPrice" type="text" onafterpaste="validNum1(this))" onblur="validNum1(this);refundInput1(this)" defaultvalue="" value="" name="lendPrice" maxlength="15">
							</dt>
							<dd>
								<div class="ydbz_x" flag="appAll">应用全部</div>
								<div class="ydbz_x gray" flag="reset">清空</div>
							</dd>
						 </dl>
	  	  			  </td>
		  	  		  <td class="tc">
		  	  		  	<input type="text" name="trvborrownotes" maxlength="30"/>
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
					  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
								 	<span class="fl">团队宣传费</span>
									<label class="ydLable2 ydColor1" style="width: 120px;">预计团队宣传费金额：</label>
							</c:when>
							<c:otherwise>
								    <span class="fl">团队返佣</span>
									<label class="ydLable2 ydColor1" style="width: 120px;">预计团队返佣金额：</label>
							</c:otherwise>
						</c:choose>   
					<span class="disabledshowspan">${groupRebates }</span>
				</div>
				<div>
					<ol class="gai-price-ol">
						<li>
							<i>
								<input class="gai-price-ipt1" type="text" flag="istips" name="groupborrownames" maxlength="15">
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
								<input var="travelerRefundPirce" class="gai-price-ipt1 borrowPrice" type="text" value="" onafterpast="validNum1(this)" onblur="validNum1(this);refundInput1(this)" flag="istips" name="teamMoney" maxlength="15">
								<span class="ipt-tips ipt-tips2">费用</span>
							</i>
							<i>
								<input class="gai-price-ipt2" type="text" flag="istips" name="groupborrownodes" maxlength="30">
								<span class="ipt-tips ipt-tips2">备注</span>
							</i>
							<i>
								<a class="ydbz_s gai-price-btn" onClick="addProject(this)">+增加</a>
							</i>
						</li>
					</ol>
				</div>
				<div class="allzj tr f18">
					<div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">
						  <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
								 	宣传费差额：
							</c:when>
							<c:otherwise>
								    返佣差额：
							</c:otherwise>
						</c:choose>   
						<span></span>
					</div>
					
				</div>
				<input type=hidden name="totalRebatesJe" value="${totalRebatesJe }">
				<input type=hidden name="totalRebatesJe4update" value="${totalRebatesJe4update }">
             <div class="dbaniu" style="width:156px;">
                 <input class="ydbz_x gray" type="button" value="关闭" onClick="javaScript:window.close();"/>
                   <!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
						<c:choose>
							<c:when test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
								 	<input class="ydbz_x" type="button" id="rebatesId" onClick="subForm()"   value="宣传费申请"/>
							</c:when>
							<c:otherwise>
								   <input class="ydbz_x" type="button" id="rebatesId" onClick="subForm()"   value="返佣申请"/>
							</c:otherwise>
						</c:choose>   
             	 
             </div>
		</form>
	</body>
</html>