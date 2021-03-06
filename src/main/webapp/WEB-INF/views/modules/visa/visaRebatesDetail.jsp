<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>签证返佣详情</title>
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
	            url:g_context_url+"/visa/order/visaRebatesAppliy",
	            data:$('#addForm').serialize(),
	            async: false,
	            success: function(data) {
	            	top.$.jBox.tip(data.visaJKreply);
	            	window.location.href=g_context_url+"/visa/order/showRebatesList?orderId="+data.orderId;
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
	
	//驳回
	function jbox_bohui(){
		var html = '<div class="add_allactivity"><label>请填写您的驳回原因!</label>';
		html += '<textarea style="width:80%; margin:10px auto;" id="denyReasonText" name="" cols="" rows="" ></textarea>';
		html += '</div>';
		$.jBox(html, { title: "驳回原因",buttons:{"提 交":"1"}, submit:function(v, h, f){
			if (v=="1"){
				var denyReasonText = $("#denyReasonText").text();
				$("#denyReason").val(denyReasonText);
				$("#addForm").attr("action","${ctx}/review/visaRebates/dispose?result=0");
				$("#addForm").submit();
			}
		},height:250,width:500});

	}
</script>

</head>
<body>
	<div class="mod_nav">订单 &gt; 签证 &gt; 返佣记录 &gt; 返佣详情</div>
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
                         <td class="mod_details2_d1">下单人：</td>
                         <td class="mod_details2_d2">${visaOrder.createBy.name}</td>
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
		<span class="fl">个人返佣</span>
	</div>
		<form id="addForm" action="${ctx}/review/visaRebates/dispose?result=1" method="post">
		       <input type="hidden" value="${visaOrderId}" name="visaOrderId" />
		       <input type="hidden" value="${shenfen}" name="shenfen" />
		
              <table class="activitylist_bodyer_table modifyPrice-table">
                 <thead>
                    <tr>
                       <th width="6%">姓名</th>
				  	   <th width="8%">币种</th>
				  	   <th width="8%">款项</th>
					   <th width="8%">应收金额</th>
					   <!-- 0820上  -->
					   <th width="8%">预计个人返佣</th>
					  
					   <th width="8%">累计个人返佣金额</th>
					   <th width="8%">返佣差额</th>
					   <th width="20%">备注</th>
                    </tr>
                 </thead>
                 <tbody>
                    <c:forEach items="${travelerList}" var="traveler" varStatus="s">
                    <tr group="group1">
				  	  <td class="tc">
				  	  	 ${traveler.travelerName }
				  	  	 <input type="hidden" value="${traveler.travelerId }" name="trvids" />
				  	  	 <input type="hidden" value="${traveler.travelerName }" name="trvnames" />
		  	  		  </td>
				  	  <td class="tc">
				  	  	 ${traveler.trvcurrencyName }
				  	  </td>
				  	  <td class="tc">
		  	  		  	${traveler.trvborrowname }
		  	  		  </td>
				  	  <td class="tc">
				  	  	 ${traveler.trvsettlementprice }
			  	  	  </td>
			  	  	  
			  	  	  <!-- 0820上 -->
			  	  	  <td class="tc">
				  	  	 ${fns:getMoneyAmountBySerialNum(traveler.rebatesMoneySerialNum,2)}
			  	  	  </td>
			  	  	  
			  	  	  
			  	  	  <td class="tc">${traveler.totalRebateJe }</td>
				  	  <td class="tc">
				  	  	 ${traveler.trvcurrencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${traveler.trvamount }" />
	  	  			  </td>
		  	  		  <td class="tc">
		  	  		  	${traveler.trvrebatesnote }
		  	  		  </td>
                    </tr>
                    </c:forEach>
                    <c:if test="${empty travelerList}">
                    	   <tr class="toptr" >
				                 	<td colspan="15" style="text-align: center;">暂无游客申请记录</td>
				           </tr>
                    </c:if>
                 </tbody>
              </table>
             
             	<div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
				</div>
				<div class="ydbz_tit">
					<span class="fl">团队返佣</span>
				</div>
				<div>
					<table class="activitylist_bodyer_table modifyPrice-table">
                 <thead>
                    <tr>
                       <th width="6%">序号</th>
                       <th width="15%">款项</th>
				  	   <th width="20%">币种</th>
				  	   <th width="20%">预计团队返佣金额</th>
					   <th width="20%">返佣差额</th>
					   <th width="40%">备注</th>
                    </tr>
                 </thead>
                 <tbody>
                    <c:forEach items="${groupList}" var="groupList" varStatus="s">
	                    <tr group="travler1">
	                    	<td class="tc">${s.index+1 }</td>
							<td class="tc">
								${groupList.grouprebatesname }
							</td>
							<td class="tc">
								${groupList.groupcurrencyName }
							</td>
							<td class="tc">
								${groupRebates }
							</td>
							<td class="tc">
								${groupList.groupcurrencyMark }<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${groupList.grouprebatesamount }" />
							</td>
							<td>
								${groupList.grouprebatesnode }
							</td>
						</tr>
					</c:forEach>
                    <c:if test="${empty groupList}">
                    	   <tr class="toptr" >
				                 	<td colspan="15" style="text-align: center;">暂无团队申请记录</td>
				           </tr>
                    </c:if>
                 </tbody>
              </table>
              <div class="ydbz_foot" style="margin-top:10px; padding-bottom:10px;">
				</div>
				</div>
				<div class="allzj tr f18">
					<div class="fr f14 all-money" style="font-size:18px;font-weight:bold;">
						返佣差额：
						<span>${totalrebatesamount }</span>
					</div>
				</div>
			 <%@ include file="/WEB-INF/views/modules/review/reviewLogBaseInfo.jsp"%>
			 <c:if test="${param.reviewFlag eq 1 }">
				<input type="hidden" name="reviewId" value="${param.reviewId}"/>
				<input type="hidden" name="nowLevel" value="${param.nowLevel}"/>
				<input type="hidden" name="denyReason" id="denyReason" value=""/>
				
				<div class="dbaniu">
					<a class="ydbz_s gray" onClick="history.go(-1)">返回</a>
					<c:if test="${not empty param.nowLevel}">
						<a class="ydbz_s" onclick="jbox_bohui();">驳回</a>
						<input class="btn btn-primary" type="submit" value="审核通过">
					</c:if>
				</div>
			</c:if>
			<c:if test="${param.reviewFlag ne 1 }">
             <div class="dbaniu" style="width:150px;">
                 <input class="ydbz_x gray" type="button" value="关闭" onClick="javaScript:window.close();"/>
             </div>
            </c:if>
		</form>
	</body>
</html>