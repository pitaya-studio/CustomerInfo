currencyObj ={};

$(function(){
	$(".gai-price-btn").on("click",function(){
		var html = '<li><i><input type="hidden" name="travelerId" value="0"><input type="text" name="lendName" class="gai-price-ipt1" flag="istips" /><span class="ipt-tips ipt-tips2">其他款项</span></i>&nbsp;';
		html += '<i><select class="selectrefund" name="currencyId" onchange="chageCurr(this);">{0}<select><input type=hidden name="currencyName" value=""><input type=hidden name="currencyExchangerate" value=""><input type=hidden name="currencyMark" value=""></i>&nbsp;'.replace("{0}",$("#currencyTemplate").html());
		html += '<i><input type="text" name="lendPrice" class="gai-price-ipt1" flag="istips" onkeyup="lendInput(this)" onblur="lendInputs(this)" /><span class="ipt-tips ipt-tips2">费用</span></i>&nbsp;';
		html += '<i><input type="text" name="remark" class="gai-price-ipt2" flag="istips" /><span class="ipt-tips ipt-tips2">备注</span></i>&nbsp;';
		html += '<i><a class="ydbz_s gray clear-btn" onclick="delgroup(this);">删除</a></i>';
		$(this).parents('.gai-price-ol').append(html);
		chageCurr($(this).parents('.gai-price-ol').find("li:last").find("select[name='currencyId']"));
		//新增改价项目中，input获得失去焦点提示信息显示隐藏
		inputTips($(this).parents('.gai-price-ol').find("li:last"));
	});
})

//关闭窗口
function closeWindows(){
	
	  window.opener.location.href = window.opener.location.href;
	  window.close(); 
	
}


function bind() {
	var orderId = $("#orderId").val();
	if($(".borrowPrice[value!=''][value!='0'][value!='-']").length > 0 || $(".gai-price-ipt1[value!=''][value!='0'][value!='-']").length > 0) {
		//$("#bt_submit").click(function() {
		    $.ajax({                 
		        type: "POST",                 
		        url:g_context_url+ "/order/lendmoney/beforeAddLendMoneyApply",                 
		        data:{orderId:orderId},                
		        async: false,                 
		        success: function(data) { 
		              if(data!=""){
		                 alert(data);
		                 return false;
		              }else{
		            	  submitRefund();
		              }
		        }             
		    });
		//});
	} else {
		$.jBox.tip("借款金额不能为空！", "warning");
	}
}
function delgroup(obj){
	
	$(obj).parents('li').remove();
	totallend();
}



//提交审核流程
function submitRefund(){
	   totallend();
		   var submitArray = [];
		   var orderId = $("#orderId").val();
		   var reviewId = $("#reviewId").val();
		   var token = $("[name=token]").val();
		   $.ajax({
			   type: "POST",
				  url: g_context_url + "/orderCommon/manage/saveBorrowing?token="+token,
				  dataType: "text",
				  data : $("#f1").serialize(),
				  async: false,
				  success:function(msg){
					  if(msg==""){
						  jBox.tip("不可以重复提交！");
					  }else{
						  top.$.jBox.tip(msg, 'warnning');
					  }
					  document.location = g_context_url + "/orderCommon/manage/viewBorrowingList?flowType=19&productType="+$("#myOrderType").val()+"&orderId=" + orderId;
				  }
		   	});  
}





//获取游客借款信息
/*function getTravelerRecords(trObj){
	var obj = {};
	var objArray = [];
	var travelerId = $("input[name='travelerId']");
	var travelerName = $("input[name='travelerName']", trObj).val();
	var payPrice = $("input[name='payPrice']", trObj).val();
	var currencyId = $("#orderCurrencyId").val();
	$.each(travelerId,function(){
		alert($(this).val());
	});
	return objArray;
}*/

//取币种符号 ， 币种名称
function chageCurr(obj){
	var currencyName = $(obj).find("option:selected").attr("currencyName");
	var currencyMark = $(obj).find("option:selected").attr("currencyMark");
	var currencyExchangerate = $(obj).find("option:selected").attr("currencyExchangerate");
	//alert($(obj).parent().find("input[name='currencyName']").val());
	$(obj).parent().find("input[name='currencyName']").val(currencyName);
	$(obj).parent().find("input[name='currencyMark']").val(currencyMark);
	$(obj).parent().find("input[name='currencyExchangerate']").val(currencyExchangerate);
//	$(obj).parent().find("input[name='currencyMark']").each(function(i,o){
//		alert($(o).val());
//		$(o).val(currencyMark);
//		alert(currencyMark);
//	});
	 totallend();
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
				         options.push("<option " + sel + "   currencyName ="+n.currencyName+"   currencyExchangerate="+n.convertLowest+"  currencyMark ="+n.currencyMark+"  value='" + n.id + "'>" + n.currencyName + "</option>");
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


//验证数字合法性(包括正负数)
function lendInput(obj){
	var thisvalue = $(obj).val();
	if(thisvalue==0){
		alert("请输入正确数字!");
		thisvalue = "";
	}
	if(thisvalue.length >15){
		alert("借款金额位数不合法!");
		thisvalue = '0.00';
	}
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = thisvalue.split(".");
      thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(obj).val(thisvalue);
	}
	if(thisvalue.length==1 && thisvalue == "-"){
		return false;
	}else{
		totallend();
	}
}



//填写数字之后格式化
function lendInputs(obj){
	   objs=obj.value;
	   if(objs == ""){
		   $(obj).val(objs);
		   return;
	   }
	   objs=objs.replace(/^(\d*)$/,"$1.");
	   if(objs.indexOf("-") > -1&&objs.indexOf(".00")<0&&objs.length>1){
		   objs=(objs+".00").replace(/(\d*\.\d\d)\d*/,"$1");   
	   }else if(objs.indexOf("-") > -1 && objs.length==1){
		   objs="";
	   }else if(objs==0){
		   objs="";
	   }else{
		   objs=(objs+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	   }
	   objs= objs.replace(/^\./,"0.");
	   $(obj).val(objs);
     $(obj).next("span").hide();
}


function totallend(){
	 var selects=$("select[class='selectrefund']");
	 var inputs=$("input[name='lendPrice']");
	 var currencyCalObj = {};
	 for(u=0;u<selects.length;u++){
	      var money_sel = selects.eq(u).val();
		  var money_input = selects.eq(u).parent().parent().find("input[name='lendPrice']");
		  $(money_input).attr("data-type",money_sel);
	    }
	 
	 inputs.each(function(index, element) {
		  var money_text=$(this).val();
		  if(money_text==""){
			  money_text=0;
		  }else{
			  money_text=parseFloat(money_text);
	      }
		  var datatype =$(this).attr("data-type");
		  var currObj = currencyCalObj[datatype];
		  //存在对象
		  if(currObj){
			  money_text += currObj;
		  }
	    currencyCalObj[datatype] = money_text;
		  
	 });
	 
	var showArray = [];
	
	var currencyIds = [];
	var currencyNames = [];
	var currencyMarks = [];
	var borrowPrices = [];
	var currencyExchangerates =[];
	
	var cObj = null;
	for(var key in currencyCalObj){
		cObj = currencyObj[key];
		if(currencyCalObj[key].toString()==0){
			showArray.push("<font class='gray14'>"+""+"</font><span class='tdred'>"+""+"</span>");
		}else{
			showArray.push("<font class='gray14'>"+cObj.currencyName+"</font><span class='tdred'>"+milliFormat(currencyCalObj[key].toString(),1)+"</span>");
		}
		if(currencyCalObj[key]!=""){
			currencyIds.push(cObj.id);
			currencyNames.push(cObj.currencyName);
			currencyMarks.push(cObj.currencyMark);
			borrowPrices.push(milliFormat(currencyCalObj[key].toString(),1));
			currencyExchangerates.push(cObj.convertLowest);
		}
	}
	$('#currencyIds').val(currencyIds.join("#"));
	$('#currencyNames').val(currencyNames.join("#"));
	$('#currencyMarks').val(currencyMarks.join("#"));
	$('#borrowPrices').val(borrowPrices.join("#"));
	$('#currencyExchangerates').val(currencyExchangerates.join("#"));
	
/*	if(currencyCalObj[key].toString()==0){
		$('.all-money').find('span').html(showArray.join(""));
	}else{
		$('.all-money').find('span').html(showArray.join("+"));
	}*/
	$('.all-money').find('span').html(showArray.join(""));
}



//产品名称获得焦点显示隐藏
function inputTips(){
	$("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	$("input[flag=istips]").each(function(index, element) {
  if($(element).val()!=""){
			$(element).next("span").hide();
		}
	});
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}


//隐藏显示操作按钮
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
		totallend();
	}).on("click","[flag=reset]",function(){
		
		$(this).parents(".huanjia").find("[name='lendPrice']").val("");
		totallend();
		
	});
}

//全部重置
function allreset (){
	 $("[flag=ylendPrice]").val("");
	 $("[flag=yremark]").val("");
	 $("#contentTable").find("[flag=yselectrefund]").each(function(){
		 $(this).val($("#orderCurrencyId").val());
	 });
	 totallend();
}



