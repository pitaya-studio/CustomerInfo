/**
 * 改价通用JS
 */
$(function(){
	/**
	 * 扩展JQuery 获取浏览器请求参数
	 * 
	 */
	$.extend({
		  getUrlVars: function(){
		    var vars = [], hash;
		    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
		    for(var i = 0; i < hashes.length; i++)
		    {
		      hash = hashes[i].split('=');
		      vars.push(hash[0]);
		      vars[hash[0]] = hash[1];
		    }
		    return vars;
		  },
		  getUrlVar: function(name){
		    return $.getUrlVars()[name];
		  }
		});
	var orderId = $.getUrlVar('orderId');
	var productType = $.getUrlVar('productType');
	var flowType = $.getUrlVar('flowType');
	$("input[name='orderId']").val(orderId);
	$("input[name='productType']").val(productType);
	$("input[name='flowType']").val(flowType);
})

/** 机票改价申请*/
function submitAirtickUpPrices(){
	// Get object of URL parameters
	var allVars = $.getUrlVars();
	// Getting URL var by its nam
	var byid = $.getUrlVar('orderId');
	var ce  = $("#totalPlus").text();
	
	var flag = false; // 是否提交form表单
	var firstM = false; 
	try{
		$("input[name='teamMoney']").each(function(index){
			if(index == 0 &&  this.value=='0.00'){
					firstM = true;
					return false;
			}
			
		});
		$("input[name='teamkx']").each(function(index){
			if(firstM){
				return true;
			}
			if(this.value ==''){
				alert('请输入款项名称再提交改价!');
				flag = true ; 
				return false;  // 当前返回 false 是跳出 each循环
			}
		
		});
	}catch(e){
		alert('出现异常, 异常信息:'+e.message);
	}finally{
		if(flag){
			return false;  // 终止form表单提交
		}
	}
	$("textarea[name='djremark']").click();
	$("textarea[name='travelerremark']").click();
	$("textarea[name='teamremark']").click();
	if(ce =="" || ce == null ){
		var plusdj = $("input[name='plusdj']").val();
		if(plusdj=="" ||plusdj==null ||plusdj=='0.00'){
			alert("请输入差额再提交改价!"); 
			return false;
		}
		$("#form1").submit();
	}else{
		$("#form1").submit();
	}
		
		
	
}
/** 签证的改价申请*/
function submitVisaUpPrices(){
	// Get object of URL parameters
	var allVars = $.getUrlVars();
	// Getting URL var by its nam
	var byid = $.getUrlVar('orderId');
	var ce  = $("#totalPlus").text();

	var flag = false; // 是否提交form表单
	var firstM = false; 
	try{
		$("input[name='teamMoney']").each(function(index){
			if(index == 0 &&  this.value=='0.00'){
					firstM = true;
					return false;
			}
			
		});
		$("input[name='teamkx']").each(function(index){
			if(firstM){
				return true;
			}
			if(this.value ==''){
				alert('请输入款项名称再提交改价!');
				flag = true ; 
				return false;  // 当前返回 false 是跳出 each循环
			}
		
		});
	}catch(e){
		alert('出现异常, 异常信息:'+e.message);
	}finally{
		if(flag){
			return false;  // 终止form表单提交
		}
	}

	$("textarea[name='djremark']").click();
	$("textarea[name='travelerremark']").click();
	$("textarea[name='teamremark']").click();
	if(ce =="" || ce == null ){
		var plusdj = $("input[name='plusdj']").val();
		if(plusdj=="" ||plusdj==null ||plusdj=='0.00'){
			alert("请输入差额再提交改价!"); 
			return false;
		}
		$("#form1").submit();
	}else{
		$("#form1").submit();
	}
}


// 【游客 团队 其它】   改价差额计算(HPT 重写 common.js validNumFinally函数)

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();
	
	if(thisvalue.length >15){
		alert("改价金额位数不合法!");
		thisvalue = '0.00';
	}
	
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		//thisvalue = thisvalue.replace(/\D/g,"");
		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = thisvalue.split(".");
        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}
}
/**
 * 验证机票改价流程是否互斥
 */
function check_airticket_uppricess(){
	var form1 = $("#form1").serialize();
	var orderId = $.getUrlVar('orderId');
	var productType = $.getUrlVar('productType');
	var flowType = $.getUrlVar('flowType');
	
	$.ajax({
		url:ctx+'/airTicketUpProces/checkMutex',
		type:'post',
		data:form1,
		async:false, 
		success:function(d){
			var obj = eval(d);
			if(obj.mutex_code == '0'){
				flag = true;
				$.ajax({
					url:ctx+'/airTicketUpProces/applyForUpAirPrices',
					type:'post',
					data:form1,
					async:false,
					success:function(dd){
						var obj1 = eval(dd);
						if(obj1.sbinfo != ''){
							alert(obj1.sbinfo);
						}else{
							window.location.href = ctx+'/airTicketUpProces/list?orderId='+orderId+'&productType='+productType+'&flowType='+flowType+' ';
						}
					}
				});
			}else{
				 alert(obj.message);
			}
		}
		
	});
}
/**
 * 验证签证改价流程是否互斥
 */
function check_visa_uppricess(){
	var form1 = $("#form1").serialize();
	var orderId = $.getUrlVar('orderId');
	var productType = $.getUrlVar('productType');
	var flowType = $.getUrlVar('flowType');
	$.ajax({
		url:ctx+'/visaUpProces/checkMutex',
		type:'post',
		data:form1,
		async:false, 
		success:function(d){
			var obj = eval(d);
			if(obj.mutex_code == '0'){
				flag = true;
				$.ajax({
					url:ctx+'/visaUpProces/applyForUpVisaPrices',
					type:'post',
					data:form1,
					async:false,
					success:function(dd){
						var obj1 = eval(dd);
						if(obj1.sbinfo != ''){
							alert(obj1.sbinfo);
						}else{
							window.location.href = ctx+'/visaUpProces/list?orderId='+orderId+'&productType='+productType+'&flowType='+flowType+' ';
						}
					}
				});
			}else{
				 alert(obj.message);
			}
		}
		
	});
}
var submit_times = 0;
/**
 * 验证单团改价流程是否互斥
 */
function check_activity_uppricess(){
	var gaijiaCurencyVal = $(":hidden[name='gaijiaCurency']").val();
	if(gaijiaCurencyVal.trim() == "" ) {
		alert("游客信息缺失，不允许申请改价！");
		return;
	}
	
	var notCheckedEle = $(":checkbox[name='travelerId']:not(:checked)");	
	var allCheckBoxEle = $(":checkbox[name='travelerId']");	
	if(notCheckedEle.length == allCheckBoxEle.length) {
		alert('请选择需要改价的游客!');
		return;
	}
	
	$(":checkbox[name='travelerId']:not(:checked)").parents("tr").each(function(index, obj) {
		var test = $(this).attr("group");
		$("#form1").find("tr[group][group='"+ test +"']").each(function(){
			$(this).find("input[name='plusysTrue']").val("0.00");
		});
	});
	
	
	var form1 = $("#form1").serialize();
	var orderId = $.getUrlVar('orderId');
	var productType = $.getUrlVar('productType');
	var flowType = $.getUrlVar('flowType');
	
	if(submit_times != 0) {
		return ;
	}
	$.ajax({
		url:ctx+'/activityUpProces/checkMutex',
		type:'post',
		data:form1,
		async:false, 
		success:function(d){
			submit_times++;
			flag = true;
			$.ajax({
				url:ctx+'/activityUpProces/applyForUpIslandPrices?orderUuid=' + $("#orderUuid").val(),
				type:'post',
				data:form1,
				async:false,
				success:function(dd){
					var obj1 = eval(dd);
					if(obj1.sbinfo != ''){
						alert(obj1.sbinfo);
					}else{
						window.location.href = ctx+'/island/review/islandChangePriceList?orderId='+orderId+'&productType=12&flowType='+flowType+'&orderUuid='+$("#orderUuid").val();
					}
				}
			});
		}
		
	});
}