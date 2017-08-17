
function bind(){
  $("#bt_cancel").click(function(){
     var orderId = $("#orderId").val();
     document.location = g_context_url + "/order/refund/viewList?orderId=" + orderId;
  });
  
  $("#bt_submit").click(function(){
	  //先做退款审核互斥校验
	   var submitArray = [];
	   var orderId = $("#orderId").val();
	   var traIds = "";
	   var flag = true;
	   
	   var times01 = $("#contentTable > tbody > tr").length + 1;
	   // 游客没退款个数
	   var times02 = 0;
	   //获取游客退款信息   目前只有游客退款
	   var arrs = [];
	   var travelerIds = [];
	   $("#contentTable > tbody > tr").each(function(i, n){
		   var arr = getTravelerRecords(n);
		   var travelerId = $("input[name='travelerId']", n).val();
		   var refunds = $("input[name='refund']", n).val();
		   var refundNames = $("input[name='refundName']", n).val();
		   
		   if(arr.length == 0 && travelerId != null) {
			   times02++;
			   if((refunds == "" && refundNames != "") || (refunds != "" && refundNames == "")) {
				   $.jBox.tip('请输入退款款项和金额!', 'info');
				   return;
			   }
		   }
		   if(arr.length != 0 && refunds.length > 0) {
			   arrs.push(arr);
		   }
		   
	   });
	   
	   
	   for(var i = 0; i < arrs.length; i++){
		   if(i==0){
			   traIds += arrs[i][0].travelerId; 
		   } else {
			   traIds += "," + arrs[i][0].travelerId;
		   }
	   }
	   
	   //团队退款信息
	   var array = getGroupRecords();
	   if(array.length>0){
		   
		   if(traIds==""){
			   traIds+="0";
		   }else{
			   traIds += ",0" ;
		   }
	   }else {
		   times02++;
	   }
	   if(times02 == times01) {
		   $.jBox.tip('请输入退款款项和金额!', 'info');
		   return;
	   }
	   $.ajax({
           type: "POST",
           url: g_context_url+"/refundReview/beforeAddReview",
           data: {
           orderId : orderId,
           travelerids : traIds,
           reviewFlowId : 1//退款	            
           },
           async : false,
           success: function(msg){
	           	if(msg.result == ''){
	           		submitRefund();//提交申请
	           	}else{
	           		top.$.jBox.tip(msg.result);
	           	}
           }
       });
  
  });
}
//获取游客退款信息
function getTravelerRecords(trObj){
	var obj = {};
	var objArray = [];
	var travelerId = $("input[name='travelerId']", trObj).val();
	var travelerName = $("input[name='travelerName']", trObj).val();
	var payPrice = $("input[name='payPrice']", trObj).val();
	var currencyId = $("#orderCurrencyId").val();
	var rerundPrice, refundName;
    $(".refundTable tr", trObj).each(function(i, n){
    	rerundPrice = $(this).find("input[name='refund']").val();
    	refundName = $(this).find("input[name='refundName']").val();
    	if(rerundPrice != "" && refundName != ""){
    		obj = {};
        	obj.travelerId = travelerId;
        	obj.travelerName = travelerName;
        	obj.refundName = refundName;
        	obj.currencyId = $(this).find("select[name='currency']").val();
        	obj.currencyName = currencyObj[obj.currencyId].currencyName;
        	obj.currencyMark = currencyObj[obj.currencyId].currencyMark;
        	obj.payPrice = payPrice;//(currencyId == obj.currencyId) ? payPrice : 0; 存储应收金额不区分币种
        	obj.refundPrice = rerundPrice;
        	obj.remark = $(this).find("input[name='remark']").val();
        	obj.status = 1;
        	objArray.push(obj);
    	} 
    	
    });
	return objArray;
}

//获取团队退款信息
function getGroupRecords(){
	var obj = {};
	var objArray = [];
	var travelerId = 0;
	var travelerName = "团队";
	var payPrice = $("#orderPrice").val();
	var currencyId = $("#orderCurrencyId").val();
	var rerundPrice, refundName;
    $(".gai-price-ol li").each(function(i, n){
    	refundName = $(this).find("input[name='refundName']").val();
    	refundPrice = $(this).find("input[name='refund']").val();
    	if(refundName != "" && refundPrice != ""){
    		obj = {};
        	obj.travelerId = travelerId;
        	obj.travelerName = travelerName;
        	obj.refundName = refundName;
        	obj.currencyId = $(this).find("select[name='currency']").val();
        	obj.currencyName = currencyObj[obj.currencyId].currencyName;
        	obj.currencyMark = currencyObj[obj.currencyId].currencyMark;
        	obj.payPrice = (currencyId == obj.currencyId) ? payPrice : 0;
        	obj.refundPrice = refundPrice;
        	obj.remark = $(this).find("input[name='remark']").val();
        	obj.status = 1;
        	objArray.push(obj);
    	}
    	
    });
	return objArray;
}


function submitRefund(){
   var submitArray = [];
   var orderId = $("#orderId").val();
   //获取游客退款信息
   $("#contentTable > tbody > tr").each(function(i, n){
	   $.merge(submitArray, (getTravelerRecords(n)));
	   
   });
   
   //获取团队退款信息
   $.merge(submitArray,(getGroupRecords()));
   
   if(submitArray.length == 0){
	   top.$.jBox.tip("请输入有效的退款信息!", 'warnning');
	   return;
   }
   
   var reviewId = $("#reviewId").val();
   var postData = {refundRecords : JSON.stringify(submitArray), orderId : orderId, reviewId : reviewId}
   
   $.ajax({
	   type: "POST",
		  url: g_context_url + "/order/refund/submitReview",
		  dataType: "json",
		  data : postData,
		  async: false,
		  success:function(msg){
			  if(msg){
				  if(msg.error){
					  //show error
					  top.$.jBox.tip(msg.error, 'error');
					  return;
				  }else{
					  document.location = g_context_url + "/order/refund/viewList?orderId=" + orderId;
				  }
			  }
		  }
   });

}


function loadCurrency(){
   $.ajax({
		  type: "GET",
		  url: g_context_url + "/order/refund/currencyJson",
		  dataType: "json",
		  async: false,
		  success:function(msg){
			  if(msg && msg.currencyList){
			      var options = [];
			      var curr = $("#orderCurrencyId").val();
			      var sel = "";
			      $.each(msg.currencyList, function(i,n){
			         currencyObj[n.id] = n;
			         sel = "";
			         if(curr == n.id){
			             sel = " selected='selected'";
			         }
			         
			         options.push("<option " + sel + " value='" + n.id + "'>" + n.currencyName + "</option>");
			      });
			      
			      $("#currencyTemplate, select.selectrefund").append(options.join(""));
			      
			      var reviewId = $("#reviewId").val();
			      if(reviewId != ""){
			   	   $("select.selectrefund").val($("#reviewId").attr("data-type"));
			      }
			  }
		  }
		});
}


//团队退款
function refunds(){
	$('.refund-price-btn').click(function() {
		var html = '<li><i><input type="text" name="refundName" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund" name="currency">{0}<select></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" class="gai-price-ipt1" name="refund" data-type="rmb" flag="istips" onblur="refundInputs(this)" onkeyup="refundInput(this)" onafterpaste="refundInput(this)"/><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips();
		changeRefund();
	});
	//删除团队退款一项
	$('.gai-price-ol').on("click",".clear-btn",function(){
		$(this).parents('li').remove();
		totalRefund();
	});
	$('.bgMainRight').on("click",'.gaijia-delete',function(){
		$(this).parent().parent().parent().remove();
		totalRefund();
		})
	$('.bgMainRight').on("click",'.gaijia-add',function(){
		//$(this).parent().parent().parent().remove();
		var html='<tr>';
            html+='<td class="refundtd"><input type="text" name="refundName"><div class="pr"><i class="gaijia-delete" title="删除款项"></i></div></td>';
			html+='<td class="tc"><select class="selectrefund" name="currency" style="width:90%;">{0}</select></td>'.replace("{0}",$("#currencyTemplate").html());
			html+='<td class="tr"><span class="tdgreen"></span></td>';
			html+='<td><input type="text" name="refund" onkeyup="refundInput(this)" onblur="refundInputs(this)" onafterpaste="refundInput(this)" name="refund" data-type="eur"></td>';
			html+='<td><input type="text" name="remark"></td></tr>';
		$(this).parents('.refundTable').append(html);
		changeRefund();
		
		$(this).parents('.refundTable').find("tr:last select.selectrefund").trigger("change");
		})		
}
//订单团队退款
function refundInput(obj){
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	totalRefund();
}

function refundInputs(obj){
	   objs=obj.value;
	   if(objs == ""){
		   $(obj).val(objs);
		   return;
	   }
	   
	   objs=objs.replace(/^(\d*)$/,"$1.");
	   objs=(objs+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	   objs= objs.replace(/^\./,"0.");
	   $(obj).val(objs)
       $(obj).next("span").hide()
}

function changeRefund(){
	  $(".bgMainRight").delegate("select[class='selectrefund']","change",function(){
//	   var spanObj = $(this).closest("tr").find("span.tdgreen");
//	   if(spanObj.length > 0){
//		   var currencyId = $(this).val();
//		   var orderCurrencyId = $("#orderCurrencyId").val();
//		   var payPrice = $("input[name='payPrice']").val();
//		   payPrice = payPrice.replace(/[^\d\.]/g,"");
//		   if(currencyId != orderCurrencyId){
//			   payPrice = "";
//		   }
//		   
//		   spanObj.text(currencyObj[currencyId].currencyMark + milliFormat(payPrice,1));
//	   }
	   
	   totalRefund();
	 })
}

function totalRefund(){
	 var selects=$("select[class='selectrefund']");
	 var inputs=$("input[name='refund']");
	 var currencyCalObj = {};
	 
	 for(u=0;u<selects.length;u++){
	      var money_sel = selects.eq(u).val();
		  var money_input = selects.eq(u).parent().parent().find("input[name='refund']");
		  $(money_input).attr("data-type",money_sel);
	    }
	 inputs.each(function(index, element) {
		  var money_text=$(this).val();
		  if(money_text==""){
			  money_text=0;
		  }else{
			  money_text=parseFloat(money_text)
	      }
		  var datatype =$(this).attr("data-type");
		  var currObj = currencyCalObj[datatype];
		  //存在对象
		  if(currObj){
			  money_text += currObj;
		  }
		  if(money_text != 0 ){
			  currencyCalObj[datatype] = money_text;
		  }
		  
	 });
	 
	var showArray = [];
	var cObj = null;
	for(var key in currencyCalObj){
		cObj = currencyObj[key];
		showArray.push("<font class='gray14'>" + cObj.currencyName + "</font><span class='tdred'>"+milliFormat(currencyCalObj[key].toString(),1)+"</span>");
	}
	$('.all-money').find('span').html(showArray.join(""));	 

}

