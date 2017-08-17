<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>${payOrderTitle}</title>
<meta name="decorator" content="wholesaler" />

<script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
<%-- <script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script> --%>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.js"></script>
<script type="text/javascript" src="${ctxStatic}/common/jquery.file.filter.js"></script>
<%-- <script type="text/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script> --%>
<script type="text/javascript" src="${ctxStatic}/modules/order/single/singleorder.js"></script>
<script type="text/javascript" src="${ctxStatic}/common/jquery.disabled.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/activity/activity.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<style>
	div.jbox .jbox-content {
	    min-height: 24px;
	    line-height: 18px;
	    color: #444444;
	    -ms-overflow-y: hidden !important;
	}
</style>
<script type="text/javascript">
    function getPostData(obj){
    var postData = {
        requestType:"mtour data",
        param:base64encode(JSON.stringify(obj))
    }
    return postData;
}
    $(document).on('click','[name="addCharge"]',function(){
         var $new = $(this).parent().clone();
         $new.find('[name="addCharge"]').attr('name','removeCharge').addClass('del');
         $new.find('input[type=text]').val('');
         $(this).parents('.charge-operator-name').append($new);
         
         //添加手续费校验
         var $newVali = $(this).parent().next().clone();
         $newVali.find("span").text('');
         $newVali.hide();
         $(this).parents('.charge-operator-name').append($newVali);
     });
     $(document).on('click','[name="removeCharge"]',function(){
         $(this).parent().next().remove();
         $(this).parent().remove();
     });
    
	String.prototype.endWith = function(s) {
		if (s == null || s == "" || this.length == 0 || s.length > this.length)
			return false;
		if (this.substring(this.length - s.length) == s)
			return true;
		else
			return false;
		return true;
	};
	
	//来款行和来款账号提示必填信息      -----start---------------
	$(document).on('blur', '[name="bankName"]+.custom-combobox input', function(){
		var bankName = $('[name="bankName"]+.custom-combobox input').val();
    		
    	if(!bankName.trim() || bankName == "--请选择--"){
			$("#errorfmBankName").show();
		}else{
			$("#errorfmBankName").hide();
		}
	});
	$(document).on('blur', '[name="bankAccount"]+.custom-combobox input', function(){
		var bankAccount = $('[name="bankAccount"]+.custom-combobox input').val();
    		
    	if(!bankAccount.trim() || bankAccount == "--请选择--"){
			$("#errorfmBankAccount").show();
		}else{
			$("#errorfmBankAccount").hide();
		}
	});
	//来款行和来款账号提示必填信息      -----end---------------
	
	function InputBankAccount(){
		if($("#toBankAccount").val() != ""){
			$("#errorBankAccount").hide();
		}else{
			$("#errorBankAccount").show();
		}
	}

	function ChooseToBankNname(){
		if($("#toBankNname").val() != ""){
			$("#errorBankNname").hide();
		}else{
			var options = '<option value=""></option>';
			$("select[id=toBankAccount]").html('');
			$("select[id=toBankAccount]").append(options);
			$("#errorBankNname").show();
		}
		
		if($("#toBankAccount").val() != ""){
			$("#errorBankAccount").hide();
		}else{
			$("#errorBankAccount").show();
		}	
	}
	//表单验证
	$().ready(function() {
		$("#offlineform_1").validate({
			onsubmit : false
		});
		$("#offlineform_2").validate({
			onsubmit : false
		});
		$("#offlineform_3").validate({
			onsubmit : false
		});
		$("#offlineform_4").validate({
			onsubmit : false
		});
		$("#offlineform_5").validate({
			onsubmit : false
		});
		$("#offlineform_6").validate({
			onsubmit : false
		});
		$("#offlineform_7").validate({
			onsubmit : false
		});
		$("#offlineform_8").validate({
			onsubmit : false
		});
		$("#offlineform_9").validate({
			onsubmit : false
		});
		jQuery.extend(jQuery.validator.messages, {
			required : "必填信息",
			number : "输入信息有误",
			digits : "输入信息有误",
			email : "email格式错误",
			maxlength : jQuery.validator.format("请输入一个 长度最多是 {0} 的字符串")
		});
	});

	jQuery(function($) {

		$.fn.datepicker = function(option) {
			var opt = $.extend({}, option);
			$(this).click(function() {
				WdatePicker(opt);
			});
		};
	});

	var submit_times = 0;
	$(function() {
		$("[name='invoiceDate']").datepicker();
		//  S 89需求-->
         $("[name='bankName']").comboboxInquiry({
             removeIfInvalid:false
         });
         $("[name='bankAccount']").comboboxInquiry({
             removeIfInvalid:false
         });

          $("[name='accountName']").comboboxInquiry();
/**
         $('[name="bankName"]+.custom-combobox').on( "autocompleteselect", function( event, ui ) {
             alert(ui.item.option.value);
             //仅为展示效果
             var data = [[0,0,0,0,0,'6223 7343 7321 1244 987'],[0,0,0,0,0,'6223 7343 1233 1421 453']];
             var options = '';
             if (data != null) {
                 for ( var i = 0; i < data.length; i++) {
                     options += '<option value="'+data[i][5]+'">'
                     + data[i][5] + '</option>';
                 }
             }
             $("#bankAccount").html('');
             $("#bankAccount").append(options);
         });
         */
         $('[name="bankName"]+.custom-combobox').on( "autocompletechange", function( event, ui ) {
             var value = $(this).children('input').val(),
                     valueLowerCase = value.toLowerCase(),
                     valid = false;
             $(this).prev().children("option").each(function () {
                 if ($(this).text().toLowerCase() === valueLowerCase) {
                     this.selected = valid = true;
                     return false;
                 }
             });
             if (valid) {
                 //this._trigger("afterInvalid", null, value);
                 return;
             }
             if(!valid){
                 $("#bankAccount").html('<option value=""></option>');
             }
         });

		$('[name="bankName"]').on("comboboxinquiryselect", function (){
			var select=$(this);
			var selectVal = $(this).val();
			var bankName = encodeURI(encodeURI(selectVal));
			var supplierId = $(this).attr('agentId');
			if (bankName != "") {
				$.ajax({
					type : "POST",
					url : "${ctx}/orderPayMore/getBankAccount/" + supplierId,
					cache : false,
					async : false,
					data : "bankName=" + bankName,
					dataType : "json",//返回的数据类型
					success : function(data) {
						var options = '';
						if (data != null) {
							for ( var i = 0; i < data.length; i++) {
								options += '<option value="'+data[i][5]+'">'
										+ data[i][5] + '</option>';
							}
						}
						select.parent().parent().next().find("select").html('');
						select.parent().parent().next().find("select").append(options);
					},
					error : function() {
						alert('返回数据失败');
					}
				});
			}else{
				var options = '<option value="" select="select">--请选择--</option>';
				$('[name="bankAccount"]+.custom-combobox input').val("--请选择--");
				$("#bankAccount").html('');
				$("#bankAccount").append(options);
			}
		});
		
         $('[name="bankName"]+.custom-combobox input').on('keyup',function(){
             var value = this.value.replace(/[^\a-\z\A-\Z0-9\u4E00-\u9FA5]/g,'');
             this.value = value.length>100?value.substr(0,100):value;
         });
         $('[name="bankAccount"]+.custom-combobox input').on('keyup',function(){
             var  value = this.value.replace(/[^0-9\s]/g,'');
             this.value = value.length>100?value.substr(0,100):value;
         });
         //E 89需求-->
		$(".patorder_a1").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					var name = $(this).attr("name");

					$(this).parent().siblings().children('#offlinebox_1')
							.show().siblings('div.payDiv').hide();
				});

		$(".patorder_a2").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_3')
							.show().siblings('div.payDiv').hide();
				});

		$(".patorder_a3").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_4')
							.show().siblings('div.payDiv').hide();
				});

		$(".patorder_a4").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_5')
							.show().siblings('div.payDiv').hide();
				});
		$(".patorder_a6").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_6')
							.show().siblings('div.payDiv').hide();
				});
		$(".patorder_a7").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_7')
							.show().siblings('div.payDiv').hide();
				});
		$(".patorder_a8").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_8')
							.show().siblings('div.payDiv').hide();
				});
		//add by zhangchao 因公支付宝 224需求
		$(".patorder_a9").click(
				function() {
					$(this).css({
						"color" : "#3A7851",
						"backgroundColor" : "#FFF"
					}).siblings().css({
						"color" : "#000",
						"backgroundColor" : ""
					});
					$(this).parent().siblings().children('#offlinebox_9')
							.show().siblings('div.payDiv').hide();
				});

		var companyId = $('input[name=companyId]').val();
		if(companyId == 68){
			$(".patorder_a1").click();
		} else if(companyId == 71){
			$(".patorder_a1").click();
		} else {
			$(".patorder_a1").click();
		}				


		//201507020150 add by zhanghao 环球行需要隐藏支付类型 start				
		if(companyId == 68){
			$(".patorder_a6").hide();
			$(".patorder_a7").hide();
			$(".patorder_a8").hide();
		}
		//201507020150 add by zhanghao 环球行需要隐藏支付类型 end		

		$payforRadio = $('input[name=paymentStatus]');
		$payforRadio.change(function() {
			var payforRadioVal = $(this).val();
			if (payforRadioVal != 1) {
/* 				top.$.jBox.confirm('您是否按月结支付？', '系统提示', function(v) {
					if (v == 'ok') { */
						$('#payBtn').text('确认');
/* 					} else {
						$('input[name=paymentStatus]')[1].checked = true;
						$('#payBtn').text('确认支付');
					}
				}, {
					buttonsFocus : 1
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
				return false; */
			} else {
				$('#payBtn').text('确认支付');
			}
		});
		
		
		//汇率是否合计
		$('.exchange-radio').find('input').click(function(){
			if($(this).parent().index()==1){
				$(this).parents('tr').siblings().find('.exchange').find('label').show();
				$(this).parents('tr').siblings(".exchange-total").show();
				exchange($(this).parents('tr').next().find('.exchange-rate'));
				exchange_total($(this).parents('tr').next().find('.exchange-rate'));
			}else{
				$(this).parents('tr').siblings().find('.exchange').find('label').hide();
				$(this).parents('tr').siblings(".exchange-total").hide();
			}
		});
		
		//initMergePay();
		
		//汇率计算
		//$('.exchange').find('input').each(function(index, element) {
		//	exchange(this);
	    //});
		//汇率计算总价
		$('.exchange').find('input').blur(function(){
			exchange_total(this);
		});
	});
	
	function getActivePanelDivId(){
		// 判断当前活动DIV
		var payDivId = ""; // 活动ID
		$(".payDiv").each(function() {
			var block = $(this).css('display');
			if (block == 'block') {
				payDivId = $(this).attr('id');
			}
		});
		
		return payDivId;
	}

	function changeBank(supplierId, bankName, eleId) {
		bankName = encodeURI(encodeURI(bankName));
		if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPayMore/getBankAccount/" + supplierId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
						}
					}
					$("#" + eleId).html('');
					$("#" + eleId).append(options);
				},
				error : function(e) {
					alert(e.responseText);
				}
			});
		}
	}
	
	function changePayTypeBank(supplierId, bankName, eleName, payType) {
		bankName = encodeURI(encodeURI(bankName));
		if (bankName != -1) {
			$.ajax({
				type : "POST",
				url : "${ctx}/orderPayMore/getBankAccount/" + supplierId,
				cache : false,
				async : false,
				data : "bankName=" + bankName,
				dataType : "json",//返回的数据类型  
				success : function(data) {
					var options = '';
					if (data != null) {
						for ( var i = 0; i < data.length; i++) {
							options += '<option value="'+data[i][5]+'">'
									+ data[i][5] + '</option>';
						}
					}
					
					$("#" + payType).find("select[name="+eleName+"]").html('');
					$("#" + payType).find("select[name="+eleName+"]").append(options);
				},
				error : function(e) {
					alert(e.responseText);
				}
			});
		}
	}

	function formSunbmit() {
		// 判断当前活动DIV
		var payDivId = ""; // 活动ID
		$(".payDiv").each(function() {
			var block = $(this).css('display');
			if (block == 'block') {
				payDivId = $(this).attr('id');
			}
		});

		// 保存月结/后付费的状态
		var payforRadio = 1;
		if ($('input:radio[name=paymentStatus]:checked').length > 0) {
			payforRadio = $('input:radio[name=paymentStatus]:checked').val();
		}

		var $paymentStatus = $('#' + payDivId).find('input[name=paymentStatus]');
		$paymentStatus.val(payforRadio);
		var errorDocInfo = '请上传收款凭证！';
		if('${payType==2}' == 'true') {
			errorDocInfo = '请上传支付凭证！';
		}
		
		// 立即支付的情况下
		if (payforRadio == 1) {
			//校验支票付款
			if ("offlinebox_1" == payDivId) {

				var r = $("#offlineform_1").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});

				if (!r.form()) {
					return false;
				}
				
				if (${fns:getUser().company.uuid == "7a81b21a77a811e5bc1e000c29cf2586" } ) {
					if(!checkifuploadZFPZ()){
					top.$.jBox.tip(errorDocInfo);
					submit_times == 0;
					return;
					}
				} 
				/* if($("#kuanxiang").val()==' '){
					top.$.jBox.tip("请选择款项 ！");
			 		submit_times == 0;
			 		return;
				} */
				//校验手续费输入框
				if(!validateFee($("#offlineform_1"))) {
					return false;
				}
				
				var $payDivId = $('#' + payDivId);
				
				var $prices = $payDivId.find('input[name="dqzfprice"]');
				if(!checkMoney(payDivId,$prices)){
					return false;
				}

				/* if ('${fns:getUser().company.id != 68 || payType!=2}' == 'true' && !checkifuploadZFPZ()) {
					top.$.jBox.tip(errorDocInfo);
					submit_times == 0;
					return;
				} */
			} 
			// 快速支付
			else if ("offlinebox_5" == payDivId) {
				var r = $("#offlineform_5").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});

				if (!r.form()) {
					return false;
				}
				var $mergePayFlag = $('#' + payDivId).find('input[name=mergePayFlag]:checked');
				var mergePayFlag = $mergePayFlag.val();
				
				if(mergePayFlag == 1){
					var bCheckFlag = true;
					var $convertLowest = $('#' + payDivId).find('input[name=convertLowest]');
					$convertLowest.each(function(index,element){
						var convert = $(element).val();
						if(!validConvert(convert)){
							top.$.jBox.tip("请输入正确的汇率");
							bCheckFlag = false;
							$(element).focus();
							return false;
						}
					});
					if(!bCheckFlag){
						return false;
					}
				}

				if (!checkifuploadZFPZ()) {
					top.$.jBox.tip(errorDocInfo);
					return false;
				}
			}
			
			// 现金
			else if ("offlinebox_3" == payDivId) {
				var r = $("#offlineform_3").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});
			
				if (!r.form()) {
					return false;
				}
				if (${fns:getUser().company.uuid == "7a81b21a77a811e5bc1e000c29cf2586" } ) {
					if(!checkifuploadZFPZ()){
						if("${payType}"!=2){
							top.$.jBox.tip(errorDocInfo);
							submit_times == 0;
							return;
						}
					
					}
				} 
				
				var $payDivId = $('#' + payDivId);
				
				var $prices = $payDivId.find('input[name="dqzfprice"]');
				if(!checkMoney(payDivId,$prices)){
					return false;
				}
				
				var $mergePayFlag = $('#' + payDivId).find('input[name=mergePayFlag]:checked');
				var mergePayFlag = $mergePayFlag.val();
				
				if(mergePayFlag == 1){
					var $mergeCurrencyPrice = $payDivId.find('input[name="mergeCurrencyPrice"]');
					var totalNum=parseFloat(($mergeCurrencyPrice.val().substring(1)).replace(/,/g,""));
					
					if (totalNum > 999999999.99) {
						top.$.jBox.tip("金额不能大于999999999.99");
						$mergeCurrencyPrice.focus();
						return false;
					}
				}
				
				if(mergePayFlag == 1){
					var bCheckFlag = true;
					
					var $convertLowest = $('#' + payDivId).find('input[name=convertLowest]');
					$convertLowest.each(function(index,element){
						var convert = $(element).val();
						if(!validConvert(convert)){
							top.$.jBox.tip("请输入正确的汇率");
							bCheckFlag = false;
							$(element).focus();
							return false;
						}
					});
					if(!bCheckFlag){
						return false;
					}
				}
			}
			
			// 汇款
			else if ("offlinebox_4" == payDivId) {
				var r = $("#offlineform_4").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});
				
                var isSHZL = "${isSHZL}";
                if(isSHZL){
                	if($("#toBankNname").val() == ""){
                		$("#errorBankNname").show();
                	}
                	if($("#toBankAccount").val() == ""){
						$("#errorBankAccount").show();
					}
                }
                
				var bankName = $('[name="bankName"]+.custom-combobox input').val();
				var bankAccount = $('[name="bankAccount"]+.custom-combobox input').val();
				var pass = true;
				if(!bankName.trim() || bankName == "--请选择--"){
					pass = false;
					$("#errorfmBankName").show();
				}
				if(!bankAccount.trim() || bankAccount == "--请选择--"){
					pass = false;
					$("#errorfmBankAccount").show();
				}
				if(!pass){
					return false;
				}
				
				if (!r.form()) {
					return false;
				}
				/*var bankNameChoice = $("select[name=bankName]").val();
				if(bankNameChoice == ""){
					top.$.jBox.tip("请选择来款行！");
					return;
				}
				
				var bankAccountChoice = $("selece[name=bankAccount]").val();
				if(bankAccountChoice == ""){
					top.$.jBox.tip("请选择来款账号！");
					return;
				}*/
				if(isSHZL){
					var toBankNnameChoice = $("select[name=toBankNname]").val();
					if(toBankNnameChoice == ""){
						top.$.jBox.tip("请选择收款行！");
			 			return;
					}
					var toBankAccountChoice = $("select[id=toBankAccount]").val();
					if(toBankAccountChoice == ""){
						top.$.jBox.tip("请选择收款账号！");
			 			return;
					}
					
					var toBankNnameInput = $("input[id=toBankNname]").val();
					if(toBankNnameInput == ""){
						top.$.jBox.tip("请填写收款行！");
			 			return;
					}
					var toBankAccountInput = $("input[id=toBankAccount]").val();
					if(toBankAccountInput == ""){
						top.$.jBox.tip("请填写收款账号！");
			 			return;
					}
				}
				 
				//校验手续费输入框
				if(!validateFee($("#offlineform_4"))) {
					return false;
				}
				
				if (${fns:getUser().company.uuid == "7a81b21a77a811e5bc1e000c29cf2586" } ) {
					if(!checkifuploadZFPZ()){
					if("${payType}"!=2){
						top.$.jBox.tip(errorDocInfo);
						submit_times == 0;
						return;
					}
					
					}
				} 
				
				var $payDivId = $('#' + payDivId);
				
				var $prices = $payDivId.find('input[name="dqzfprice"]');
				if(!checkMoney(payDivId,$prices)){
					return false;
				}
				
				var $mergePayFlag = $('#' + payDivId).find('input[name=mergePayFlag]:checked');
				var mergePayFlag = $mergePayFlag.val();
				
				if(mergePayFlag == 1){
					var $mergeCurrencyPrice = $payDivId.find('input[name="mergeCurrencyPrice"]');
					var totalNum=parseFloat(($mergeCurrencyPrice.val().substring(1)).replace(/,/g,""));
					
					if (totalNum > 999999999.99) {
						top.$.jBox.tip("金额不能大于999999999.99");
						$mergeCurrencyPrice.focus();
						return false;
					}
				}
				
				if(mergePayFlag == 1){
					var bCheckFlag = true;
					var $convertLowest = $('#' + payDivId).find('input[name=convertLowest]');
					$convertLowest.each(function(index,element){
						var convert = $(element).val();
						if(!validConvert(convert)){
							top.$.jBox.tip("请输入正确的汇率");
							bCheckFlag = false;
							$(element).focus();
							return false;
						}
					});
					if(!bCheckFlag){
						return false;
					}
				}
				
				/* if ('${fns:getUser().company.id != 68 || payType!=2}' == 'true' && !checkifuploadZFPZ()) {
					top.$.jBox.tip(errorDocInfo);
					submit_times = 0;
					return;
				} */
			}
			// 银行转账
			else if ("offlinebox_6" == payDivId) {
				var r = $("#offlineform_6").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});
				
				if (!r.form()) {
					return false;
				}
				if (${fns:getUser().company.uuid == "7a81b21a77a811e5bc1e000c29cf2586" } ) {
					if(!checkifuploadZFPZ()){
						if("${payType}"!=2){
							top.$.jBox.tip(errorDocInfo);
							submit_times == 0;
							return;
						}
					}
				} 
				
				//校验手续费输入框
				if(!validateFee($("#offlineform_6"))) {
					return false;
				}
				
				/* if ('${fns:getUser().company.id != 68 || payType!=2}' == 'true' && !checkifuploadZFPZ()) {
					top.$.jBox.tip(errorDocInfo);
					submit_times = 0;
					return;
				} */
				
			}
			// 汇票
			else if ("offlinebox_7" == payDivId) {
				var r = $("#offlineform_7").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});
	               
				if (!r.form()) {
					return false;
				}
				if (${fns:getUser().company.uuid == "7a81b21a77a811e5bc1e000c29cf2586" } ) {
					if(!checkifuploadZFPZ()){
						if("${payType}"!=2){
							top.$.jBox.tip(errorDocInfo);
							submit_times == 0;
							return;
						}
					}
				} 
				
				//校验手续费输入框
				if(!validateFee($("#offlineform_7"))) {
					return false;
				}
				
				/* if ('${fns:getUser().company.id != 68 || payType!=2}' == 'true' && !checkifuploadZFPZ()) {
					top.$.jBox.tip(errorDocInfo);
					submit_times = 0;
					return;
				} */
			}
			// POS机刷卡
			else if ("offlinebox_8" == payDivId) {
				var r = $("#offlineform_8").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});
	               
				if (!r.form()) {
					return false;
				}
				if (${fns:getUser().company.uuid == "7a81b21a77a811e5bc1e000c29cf2586" } ) {
					if(!checkifuploadZFPZ()){
						if("${payType}"!=2){
							top.$.jBox.tip(errorDocInfo);
							submit_times == 0;
							return;
						}
					}
				} 
				
				//校验手续费输入框
				if(!validateFee($("#offlineform_8"))) {
					return false;
				}
				
				/* if ('${fns:getUser().company.id != 68 || payType!=2}' == 'true' && !checkifuploadZFPZ()) {
					top.$.jBox.tip(errorDocInfo);
					submit_times = 0;
					return;
				} */
			}
			else if ("offlinebox_9" == payDivId) {
				var r = $("#offlineform_9").validate({
					//-------校验开始--------
					onfocusout : function(element) { //失去焦点就进行校验
						$(element).valid();
					}
				//--------校验结束--------
				});
				 if (!r.form()) {
					return false;
				 }
				 if (!checkifuploadZFPZ()) {
						top.$.jBox.tip("请上传支付凭证！");
						return false;
				 }
				var payerName = $("input[id=payerNameID_9 ]").val();
				if (${payType} != 2 && $.trim(payerName).length==0){
					top.$.jBox.tip("请填写来款单位！");
			 		return false;
				}
				var fromAlipayName = $("#fromAlipayName").val();
				if (${payType} == 2 && $.trim(fromAlipayName).length==0){
					top.$.jBox.tip("请填写来款支付宝名称！");
					return false;
				}
				var fromAlipayAccount = $("#fromAlipayAccount").val();
				if (${payType} == 2 && $.trim(fromAlipayAccount).length==0){
					top.$.jBox.tip("请填写来款支付宝账号！");
					return false;
				}
				var payee = $("#alipay_payee").val();
				if (${payType} == 2 && $.trim(payee).length==0){
					top.$.jBox.tip("请填写收款单位！");
					return false;
				}
			}
		}
		
		
		if (submit_times == 0) {
			var payType=$("#"+payDivId).find("input[name=payType]").val();
			//var bankName = $('[name="bankName"]+.custom-combobox input').val();
			//var bankAccount	= $('[name="bankAccount"]+.custom-combobox input').val();
			var bankName = $("#"+payDivId).find("[name=bankName]+.custom-combobox input").val();
			var bankAccount	= $("#"+payDivId).find("[name=bankAccount]+.custom-combobox input").val();
			//$('[name="realBankName"]').val(bankName);
			//$('[name="realBankAccount"]').val(bankAccount);
			$("#"+payDivId).find("input[name=realBankName]").val(bankName);
			$("#"+payDivId).find("input[name=realBankAccount]").val(bankAccount);
			submit_times++;
			$(".payDiv>form:visible").submit();
		}
	}

	//判断是否上传了支付凭证
	function checkifuploadZFPZ() {
		var pathnum = $(".batch-ol li").length;
		if (pathnum <= 0) {
			return false;
		}
		return true;
	}

	function valdate(flag, obj, chushiValue, bizhong) {
		if (!isNaN(obj.value)) {
			if ("false" == flag) {
				if (parseInt(obj.value) < parseInt(chushiValue)) {
					obj.focus();
					top.$.jBox.tip("当前金额是" + obj.value + "," + bizhong
							+ "金额不能低于" + chushiValue);
				}
			}
		} else {
			obj.focus();
			top.$.jBox.tip(bizhong + "金额只能输入数字");
		}
	}

	function nownotpay(url) {
		if (window.opener) {
			window.close();
		} else {
			window.location.href = url;
		}
	}

	function checkMoney(payDivId,obj) {
		var $prices = obj;
		var price = "";
		var bCheckFlag = true;
		$prices.each(function(index, element) {
			price = $(element).val();
			if (price == "") {
				price = 0;
			}
			
			price = price.replace("-","");
			
			if(!isNumber(price)){
				top.$.jBox.tip("请输入正确的金额");
				$(element).focus();
				bCheckFlag = false;
				return false;
			}
			
			if(!isMoney(price)){
				top.$.jBox.tip("最多能输入两位小数");
				$(element).focus();
				bCheckFlag = false;
				return false;
			}
			
			var money = parseFloat(price);
			if (money > 999999999.99) {
				top.$.jBox.tip("金额不能大于999999999.99");
				$(element).focus();
				bCheckFlag = false;
				return false;
			}
		});
		if(!bCheckFlag){
			return false;
		}
		return true;
	}
	
	//汇率计算
	function exchange(obj){
		if($(obj).hasClass('exchange-rate')){
			
			if($(obj).val()==null || $(obj).val()==""){
				$(obj).parents('tr').find('span.exchange-zje').text('');
			//}else if($(obj).val()== 0){
				//$(obj).parent().next().find('span.exchange-zje').text('￥'+$(obj).parent().prev('input').val().toString().formatNumberMoney('#,##0.00')+'');	
			}else{
				if(!validConvert($(obj).val())){
					top.$.jBox.tip("请输入正确的汇率");
					$(obj).focus();
					return;
				}
			
				var total=Number($(obj).val()).mul($(obj).parent().prev().val());
				$(obj).parent().next().find('span.exchange-zje').text('￥'+total.toString().formatNumberMoney('#,##0.00')+'');
			}
			
		}else{
			if($(obj).val()==null || $(obj).val()==""){
				$(obj).parents('tr').find('span.exchange-zje').text('');
			}else{
				if(!isMoney($(obj).val())){
					top.$.jBox.tip("请输入正确的金额");
					$(obj).focus();
					return;
				}
				var money = $(obj).val();
				money = parseFloat(money);
				if (money > 999999999.99) {
					top.$.jBox.tip("金额不能大于999999999.99");
					$(obj).focus();
					return;
				}
				var total=Number($(obj).val()).mul($(obj).next().find('input').val());
				$(obj).siblings().find('span.exchange-zje').text('￥'+total.toString().formatNumberMoney('#,##0.00')+'');
			}
			
		}
	}
	
	function exchange_total(obj){
		var total=0;
		$(obj).parents('table').find('.exchange-zje').each(function(index, element) {
	        var totalNum=parseFloat(($(element).text().substring(1)).replace(/,/g,""));
			if(isNaN(totalNum)==true){
				totalNum=0;
			}
			total=totalNum+parseFloat(total);
	
	    });
		$(obj).parents('table').find('.exchange-total').val('￥'+total.toString().formatNumberMoney('#,##0.00')+'');
	
	}
	
	String.prototype.formatNumberMoney = function(pattern) {
		var strarr = this ? this.toString().split('.') : [ '0' ];
		var fmtarr = pattern ? pattern.split('.') : [ '' ];
		var retstr = '';
		var str = strarr[0];
		var fmt = fmtarr[0];
		var i = str.length - 1;
		var comma = false;
		for ( var f = fmt.length - 1; f >= 0; f--) {
			switch (fmt.substr(f, 1)) {
			case '#':
				if (i >= 0)
					retstr = str.substr(i--, 1) + retstr;
				break;
			case '0':
				if (i >= 0)
					retstr = str.substr(i--, 1) + retstr;
				else
					retstr = '0' + retstr;
				break;
			case ',':
				comma = true;
				retstr = ',' + retstr;
				break;
			}
		}
		if (i >= 0) {
			if (comma) {
				var l = str.length;
				for (; i >= 0; i--) {
					retstr = str.substr(i, 1) + retstr;
					if (i > 0 && ((l - i) % 3) == 0)
						retstr = ',' + retstr;
				}
			} else
				retstr = str.substr(0, i + 1) + retstr;
		}

		retstr = retstr + '.';

		str = strarr.length > 1 ? strarr[1] : '';
		fmt = fmtarr.length > 1 ? fmtarr[1] : '';
		i = 0;
		for ( var f = 0; f < fmt.length; f++) {
			switch (fmt.substr(f, 1)) {
			case '#':
				if (i < str.length)
					retstr += str.substr(i++, 1);
				break;
			case '0':
				if (i < str.length)
					retstr += str.substr(i++, 1);
				else
					retstr += '0';
				break;
			}
		}
		return retstr.replace(/^,+/, '').replace(/\.$/, '');
	};
	
	/**
	 * 必须为数字或小数点的校验
	 */
	function validConvert(oNum) {
		if(!oNum) return false;
		var strP=/^\d+(\.\d+)?$/;
		if(!strP.test(oNum)) return false;
		try{
			if(parseFloat(oNum)!=oNum) return false;
		}catch(ex){
			return false;
		}
		
		if(parseFloat(oNum) < 0){
			return false;
		}
		
		return true;
	}
	
	/**
	 *校验金额（必须是正负数且小数点后只能两位）
	 */
	function isMoney(money){
		if(!money) return false;
		var strP=/^-?\d+\.?\d{0,2}$/;
		
		if(!strP.test(money)) return false;
		try{
			if(parseFloat(money)!=money) return false;
		}catch(ex){
			return false;
		}
		return true;	
	}
	
	/**
	 * 必须为正负数字或小数点的校验
	 */
	function isNumber(oNum) {
		if(!oNum) return false;
		var strP=/^([+-]?)\d+(\.\d+)?$/;
		if(!strP.test(oNum)) return false;
		try{
			if(parseFloat(oNum)!=oNum) return false;
		}catch(ex){
			return false;
		}
		return true;
	}
	
	//乘法  
	Number.prototype.mul = function (arg)  
	{  
	    var m=0,s1=this.toString(),s2=arg.toString();  
	    try{m+=s1.split(".")[1].length}catch(e){}  
	    try{m+=s2.split(".")[1].length}catch(e){}  
	    return Number(s1.replace(".",""))*Number(s2.replace(".",""))/Math.pow(10,m)  
	} 
	
	//校验手续费
	function validateFee($feeDiv) {
		var validateFlag = true;
		$feeDiv.find("p.feeInfo").each(function(){
			var feeName = $(this).find("input[name=feeName]").val();
			var feeAmount = $(this).find("input[name=feeAmount]").val();
			var rowVali = false;
			
			if(feeName == '' && feeAmount != '') {
				rowVali = true;
				validateFlag = false;
				$(this).next().find("span.feeName").text("必填信息");
				$(this).find("input[name=feeName]").focus();
			} else {
				$(this).next().find("span.feeName").text("");
			}
			
			if(feeName != '' && feeAmount == '') {
				rowVali = true;
				validateFlag = false;
				$(this).next().find("span.feeAmount").text("必填信息");
				$(this).find("input[name=feeAmount]").focus();
			} else {
				$(this).next().find("span.feeAmount").text("");
			}
			
			//该行的校验信息是否显示
			if(rowVali) {
				$(this).next().show();
			} else {
				$(this).next().hide();
			}
			
			//校验金额
			if(feeAmount != '') {
				if(!checkMoney($feeDiv, $(this).find("input[name=feeAmount]"))) {
					validateFlag = false;
					$(this).find("input[name=feeAmount]").focus();
					return false;
				}
			}
		});
		return validateFlag;
	}

	
	var $node;
	function relationInvoiceList() {
		var $pop = $.jBox($("#relationInvoice").html(), {
	    	title    : "发票列表", buttons: {'提交':1 ,'关闭':0}, submit: function (v, h, f) {
	        	if (v == "0") {
	        	}else if(v == "1"){
	        		var checks = $("input[name='invoiceName']:checked");
	        		var invoiceIds = "";
	        		checks.each(function(i,element){
	        			invoiceIds += $(element).val();
	        			invoiceIds += ",";
	        		})
	        		invoiceIds = invoiceIds.slice(0,-1);
	        		$("input[name='relationInvoiceIds']").val(invoiceIds);
	        		// $.jBox.tip("选定关联发票成功！");
	        		if(invoiceIds == ""){
        			$.jBox.tip("请先选择要关联的发票!");
        				return false;
        			} 
	        	}
			},loaded:function(h, f){
				$node = h.find("#relationInvoiceTable");
				searchRelationInvoice();
			}, height: 350, width: 700}
		);
	}
	
	// ajax进入后台查询，返回json再解析
	function searchRelationInvoice(){
		var orderId = $("input[name=orderId]").val();
		$.ajax({
			type: "POST",
	        url: sysCtx + "/orderPay/relationInvoiceList",
	        data: {
	        	orderId : orderId
	        },
			success: function(result){
				var html = "";
				if(result == undefined || result == null){
					top.$.jBox.tip("获取关联发票记录失败！");
				}
				if(result == ''){
					$node.empty().append(html);
				}
				var json = eval(result);
				// json数组个数
				var jsonLength = json.length;
				// 判断为空
				if (jsonLength && jsonLength != 0) {
					// 循环获取html组合
					for (var i = 0; i < jsonLength; i++) {
						// 序列值
						var indexVal = i + 1;
						html += "<tr>";
						// 选择
						html += "<td><input name='invoiceName' type='checkbox' value='" + json[i][0] + "'></td>";
						// 发票号
						html += "<td name='operatorName' class='tc'><span>" + json[i][1] + "</span></td>";
						// 团号
						html += "<td name='operation' class='tc'><span>" + json[i][2] + "</span></td>";
						// 申请人
						html += "<td name='operationTime' class='tc'><span>" + json[i][5] + "</span></td>";
						// 开票状态
						html += "<td name='mainContext' class='tc'><span>" + json[i][3] + "</span></td>";
						// 开票金额
						html += "<td name='mainContext' class='tr'><span>¥ " + json[i][4] + "</span></td>";
						html += "</tr>";
					}
					$node.empty().append(html);
				}
			}
		});
	}


	function getAlipayAccount(){
		var name=$("select[name='toAlipayName']").val();
		$.ajax({
			type:"post",
			url:"${ctx}/orderPay/getAlipayAccount",
			data:{
				"name":name
			},
			success:function(result){
				var select = $("select[name='toAlipayAccount']");
				select.children().remove();
				var html="";
				if(name==""){
					html+="<option value=''>请选择</option>";
				}else{
					$.each(result,function(i,a){
						html+="<option value='"+a.account+"'>"+a.account+"</option>";
					});
				}
				select.append(html);
			}
		});
	}

	function getAlipayAccount1(obj){
		var name = obj.value;
		$.ajax({
			type:"post",
			url:"${ctx}/orderPay/getAlipayAccount",
			data:{
				"name":name
			},
			success:function(result){
				var select = $("#fromAlipayAccount");
				select.children().remove();
				var html="";
				if(name==""){
					html+="<option value=''>请选择</option>";
				}else{
					$.each(result,function(i,a){
						html+="<option value='"+a.account+"'>"+a.account+"</option>";
					});
				}
				select.append(html);
			}
		});
	}

</script>

<style type="text/css">
			.custom-combobox-toggle {
				height: 26px;
				margin: 0 0 0 -1px;
				padding: 0;
				/* support: IE7 */
				*height: 1.7em;
				*top: 0.1em;
			}
			
			.custom-combobox-input {
				margin: 0;
				padding: 0.3em;
				width: 166px;
			}
			
			.ui-autocomplete {
				height: 200px;
				overflow: auto;
			}
			
			.sort {
				color: #0663A2;
				cursor: pointer;
			}
			
			.custom-combobox input[type="text"] {
				height: 26px;
				width: 166px;
			}
			
			#house_h35 {
				margin-bottom: 0px;
			}
			
			#house_h35 th {
				height: 35px;
				padding: 0px 8px 0px 8px;
				text-align: left;
				background-color: #edf0f1;
			}
			
			#house_h35 th p {
				line-height: 35px;
				float: right;
				display: inline-block;
				font-weight: normal;
				color: #00a0e9;
			}
			
			#house_h35 th p a:link {
				color: #00a0e9;
				text-decoration: none;
			}
			
			#house_h35 th p a:hover {
				color: #00a0e9;
			}
			
			#house_h35 th p a:visited {
				color: #00a0e9;
				text-decoration: none;
			}
			
			#house_h35 input[type="checkbox"] {
				padding: 0px;
				margin: 0px 8px 0px 0px;
			}
			
			.activitylist_bodyer_table {
				width: 100%;
				margin-top: 10px;
				border-right: 1px solid #cccccc;
				*border-bottom: 1px solid #ddd;
				border-top: 1px solid #ddd;
			}
			
			.hotel_list_n_all {
				display: inline-block;
				line-height: 35px;
				float: left;
				font-weight: normal;
			}
			
			.hotel_list_n_all td {
				padding: 0px;
				margin: 0px;
			}
			
			#pad_mar_none td {
				padding: 0px;
				margin: 0px;
			}
			
			.list_date_hotel {
				font-weight: bold;
				padding: 0px 5px;
				line-height: 30px;
				float: left;
			}
			
			.list_date_hotel .date {
				width: 18px;
				height: 18px;
				float: left;
				margin-top: 6px;
				background-attachment: scroll;
				background-color: transparent;
				background-image: url(images/ico_list01.png);
				background-repeat: no-repeat;
				background-position: 0px 0px;
				display: inline-block;
				cursor: pointer;
			}
			
			.list_date_hotel .date2 {
				width: 18px;
				height: 18px;
				float: left;
				margin-top: 6px;
				background-attachment: scroll;
				background-color: transparent;
				background-image: url(images/ico_list01.png);
				background-repeat: no-repeat;
				background-position: -25px 0px;
				display: inline-block;
				cursor: pointer;
			}
			
			.list_date_hotel .hotel {
				width: 18px;
				height: 18px;
				float: left;
				margin-top: 10px;
				background-attachment: scroll;
				background-color: transparent;
				background-image: url(images/ico_list01.png);
				background-repeat: repeat;
				background-position: 0px -29px;
				display: inline-block;
				cursor: pointer;
			}
			
			.list_date_hotel .hotel2 {
				width: 18px;
				height: 18px;
				float: left;
				margin-top: 10px;
				background-attachment: scroll;
				background-color: transparent;
				background-image: url(images/ico_list01.png);
				background-repeat: repeat;
				background-position: -25px -29px;
				display: inline-block;
				cursor: pointer;
			}
			/*.t_table_nohover{width: 100%;
margin-top: 10px;
border-right: 1px solid #CCC;
border-top: 1px solid #DDD;}*/
			
			.t_table_nohover tr td {
				background-color: #ffffff !important;
				margin: 0px!important;
				padding: 0px !important;
			}
			
			.t_table_nohover tr:hover td {
				background-color: #ffffff !important;
			}
			
			#house_h35 tr td {
				background-color: #ffffff !important;
				padding: 0px !important;
			}
			
			#house_h35 tr:hover td {
				background-color: #ffffff !important;
			}
			
			.house_35 thead th {
				background-color: #edf0f1 !important;
				background: none !important;
				height: 35px !important;
			}
			
			.house_35 thead tr th {
				background-color: #edf0f1 !important;
			}
			
			.house_35 thead tr {
				background-color: #edf0f1 !important;
			}
		</style>
</head>
<body>
	<div style="dispaly:none;">
	<input type="hidden" value="${payPriceType }" id="payPriceType">
	<input type="hidden" value="${orderId }" name="orderId">
	<form action="${ctx}/orderPayMore/savePay" method="post" id="form1" >
		<input name="input" value="" id="input" type="hidden">
	</form>
	</div>
	<div class="ydbzbox fs">
		<c:if test="${navigationImgFlag == 0}">
			<div class="ydbz yd-step4 ydbz-get">&nbsp;</div>
		</c:if>
		<div class="payforDiv">
			
			<div class="payforprice">
				您需要${payTypeDesc}的最终金额为：
				<c:if test="${  empty paramCurrencyId   }">
					<span><i>0.00</i></span>
				</c:if>
				<c:if test="${ !empty paramCurrencyId}">
					<c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
						<c:forEach items="${curlist}" var="cur">
							<c:if test="${cur.id==currencyId}">
								<em class="gray14">${cur.currencyMark}</em>
								<span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${paramCurrencyPrice[status.index]}" /></span>
							</c:if>
						</c:forEach>
					</c:forEach>
				</c:if>
				<c:if test="${totalCurrencyFlag}">
				<c:if test="${ !empty paramTotalCurrencyId}">
				订单总额为：
					<c:forEach items="${paramTotalCurrencyId}" var="currencyId" varStatus="status">
						<c:forEach items="${curlist}" var="cur">
							<c:if test="${cur.id==currencyId}">
								<em class="gray14">${cur.currencyMark}</em>
								<span class="f20"><fmt:formatNumber type="currency" pattern="#,##0.00" value="${paramTotalCurrencyPrice[status.index]}" /></span>
							</c:if>
						</c:forEach>
					</c:forEach>
				</c:if>
				</c:if>
				<c:if test="${payPriceType == 1 || payPriceType == 3}">
					<i>（尚未占位）</i>
				</c:if>
			</div>

			<div class="payforchose">选择您的${payTypeDesc}类型</div>
			
			<input type="hidden" name="companyId" value="${companyId}">
			
			<div id="offline_paybox" class="pay_clearfix" style="clear: both;">
				<div id="payorderbgcolor" style="display: block; z-index: 2;height:30px; position:relative;">
					<c:if test="${companyId == 71}">
					<!-- <div class="patorder_a4" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; line-height: 29px; color: rgb(58, 120, 81); margin-bottom: -1px; float: left; display: block; cursor: pointer; background-color: rgb(255, 255, 255);">快速支付</div> -->
					</c:if>
					<div class="patorder_a1" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: inherit;">支票</div>
					<div class="patorder_a2" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">现金支付</div>
					<div class="patorder_a3" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇款</div>
					<c:choose>
						<c:when test="${companyId != 68}">
							<div class="patorder_a6" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">银行转账</div>
							<div class="patorder_a7" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">汇票</div>
							<div class="patorder_a8" style="border-width: 1px 1px medium; border-style: solid solid none; border-color:rgb(204, 204, 204) rgb(204, 204, 204) rgb(204, 204, 204); border-radius:3px 3px 0 0; width: 90px; height: 29px; text-align: center; color: rgb(0, 0, 0); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer;">POS机刷卡</div>
						</c:when>
					</c:choose>
					<c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
						<div class="patorder_a9" style="border-width: 1px 1px medium; border-style: solid solid none; border-color: rgb(204, 204, 204); border-radius: 3px 3px 0px 0px; width: 90px; height: 29px; text-align: center; color: rgb(58, 120, 81); line-height: 29px; margin-bottom: -1px; margin-left: 5px; float: left; display: block; cursor: pointer; background-color: rgb(255, 255, 255);">因公支付宝</div>
					</c:if>
				</div>
				<div class="payORDER" style="clear:both; padding:20px; border:1px #cccccc solid; margin-top:-2px; margin-top:-1px;background-color:#FFF;">
					
					<c:if test="${not empty paymentStatusFlag && paymentStatusFlag ne 0}">
						<div class="patorder_radio">
							<em class="xing">*</em>${paymentStatusLblDesc}：
							<c:if test="${paymentTypeRadioFlag == 0}">
								<label><input type="radio" name="paymentStatus" value="${paymentStatus}">是</label>
								<label><input type="radio" name="paymentStatus" value="1" checked="checked">否</label>
							</c:if>
							<c:if test="${paymentTypeRadioFlag eq 1}">
								<label><input type="radio" name="paymentStatus" value="${paymentStatus}" disabled="disabled">是</label>
								<label><input type="radio" name="paymentStatus" value="1" disabled="disabled" checked="checked">否</label>
							</c:if>
						</div>
						<div style="clear:both"></div>
					</c:if>

					<div id="offlinebox_1" class="payDiv" style="display: none;overflow:auto;height:370px;overflow-x:hidden">
						<form id="offlineform_1" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<span style="color:#f00;"><em class="xing">*</em>为确保您的订单能够及时处理，请正确填写以下信息。</span>
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${hasPreOpeninvoice }">
								<span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								</c:if>
							</c:if>
							<input type="hidden" name="token" value="${token}">
							<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' />
							<div class="aaa" > 
							<input type="hidden" name="fileIDList" class="fileIDList" /> 
							<input type="hidden" name="fileNameList" class="fileNameList" />
							<input type="hidden" name="filePathList" class="filePathList" /> 
							<input type="hidden" name="paymentStatus" value="">
							<input type="hidden" name="payType" value="1"> 
							<input type="hidden" name="relationInvoiceIds" value="">
							<table width="100%">
							    <tr>
							        <td width="360">
							<table width="100%" cellpadding="5" cellspacing="0" border="0" class="byCheque">
								<c:if test="${!empty moneyFlag && moneyFlag != 0}">
									<c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
										<c:forEach items="${curlist}" var="cur">
											<c:if test="${cur.id==currencyId}">
												<tr>
													<td width="26%" valign="top" class="trtextaling"><span style="color:#f00;">*</span>${cur.currencyName}金额：${cur.currencyMark}</td>
													<td width="74%" valign="top">
														<c:choose>
															<c:when test="${moneyFlag==1}">
																<input type="text" maxlength="12" value="<fmt:formatNumber type='currency' pattern='###0.00' value='${paramCurrencyPrice[status.index]}' />" name="dqzfprice" class="required"/>
															</c:when>
															<c:when test="${moneyFlag==2}">
																<input type="text" maxlength="12" value="<fmt:formatNumber type='currency' pattern='###0.00' value='${paramCurrencyPrice[status.index]}' />" name="dqzfprice" readonly="readonly" />
															</c:when>
														</c:choose>
													</td>
													<input type="hidden" value="${cur.id}" name="currencyIdPrice" />
												</tr>
											</c:if>
										</c:forEach>
									</c:forEach>
								</c:if>
								<tbody>
									<c:if test="${payType==1 || payType==3}">
										<tr>
											<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
											<td class="trtextalingi"><input type="text" maxlength="25" name="payerName" class="required" value="${payerName}" /></td>
										</tr>
									</c:if>
									<c:if test="${payType==2}">
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
										<c:choose>
											<c:when test="${not empty settleName }">
												<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required"  value="${settleName }"/></td>
											</c:when>
											<c:otherwise>
												<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" /></td>
											</c:otherwise>
										</c:choose>
									</tr>
									</c:if>
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>支票号：</td>
										<td class="trtextalingi"><input class="check_char_or_num required" maxlength="10" type="text" name="checkNumber" id="checkNumber" /></td>
									</tr>
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>开票日期：</td>
										<td class="trtextalingi"><input type="text" name="invoiceDate" class="required" readonly id="invoiceDate" /></td>
									</tr>
									<tr>
										<td class="trtextaling payforFiles-t"><%-- <c:if test="${fns:getUser().company.id != 68 || payType!=2 }"><span style="color:#f00;">*</span></c:if> --%><c:if test="${fns:getUser().company.uuid == '7a81b21a77a811e5bc1e000c29cf2586' }"><span style="color:#f00;">*</span></c:if>支票图片：</td>
										<td class="trtextalingi payforFiles">
											<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
											<ol class="batch-ol">
											</ol>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top">
											<textarea maxlength="254" name="remarks" style="width:200px; resize:none;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
							</td>
					        <td valign="top">
					        	<c:if test="${payType==2}">
						        	<div class="charge-operator-name">
							            <p class="feeInfo">
							            	<em name="addCharge"></em>
							                <input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
							                <select name="feeCurrencyId"  class="currency">
							                	<c:forEach items="${orderCurrencys }" var="currency">
							                    	<option value="${currency.id }">${currency.currencyName }</option>
							                	</c:forEach>
							                </select>
							                <input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12" />
							            </p>
							            <p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
						        	</div>
						        </c:if>
					        </td>
							    </tr>
							</table>
						</div>
						</form>
					</div>
					<div id="offlinebox_2" style="display:none;" class="payDiv">
						<form id="offlineform_2" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<span style="color:#F00"><em class="xing">*</em>为确保您的订单能够及时处理，请${payTypeDesc}后，正确填写POS单号。</span> 
								<input type="hidden" name="payType" value="2">
							<input type="hidden" name="token" value="${token}">
							<table width="100%" cellpadding="5" cellspacing="0" border="0">
								<tbody>
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>POS单号：</td>
										<td class="trtextalingi"><input type="text" name="posNo" maxlength="20" class="required" /></td>
									</tr>
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>POS机终端号：</td>
										<td class="trtextalingi"><input type="text" name="posTagEend" maxlength="20" class="required" /></td>
									</tr>
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>POS机所属银行：</td>
										<td class="trtextalingi"><input type="text" name="posBank" maxlength="20" class="required" /></td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top">
											<textarea name="remarks" maxlength="254" style="width:200px;  resize:none;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
						</form>
					</div>
					<!-- 现金 -->
					<div id="offlinebox_3" style="display:none;overflow:auto;height:280px;overflow-x:hidden" class="payDiv">
						<form id="offlineform_3" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<span style="color:#F00"><em class="xing">*</em>为确保您的订单能够及时处理，请到财务开具证明。</span>
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${hasPreOpeninvoice }">
								<span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								</c:if>
							</c:if>
							<input type="hidden" name="token" value="${token}">
							<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' />
							<div class="bbb" > 
							<input type="hidden" name="fileIDList" class="fileIDList" /> 
							<input type="hidden" name="fileNameList" class="fileNameList" /> 
							<input type="hidden" name="filePathList" class="filePathList" /> 
							<input type="hidden" name="paymentStatus" value="">
							<input type="hidden" name="payType" value="3"> 
							<input type="hidden" name="relationInvoiceIds" value="">

						<table width="100%">
							<tbody>
								
								<tr>
									<td width="360">
										<table width="100%" cellpadding="5" cellspacing="0" border="0" class="byCash">
										<tbody>
											<%@ include file="/WEB-INF/views/modules/order/payMoney.jsp"%>
											<c:if test="${payType==1 || payType==3}">
											
											
												<tr>
													<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
													<td class="trtextalingi">
														<input type="text" maxlength="25" name="payerName" class="required" value="${payerName}" />
													</td>
												</tr>
											</c:if>
											<c:if test="${payType==2}">
												<tr>
													<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
													<c:choose>
															<c:when test="${not empty settleName }">
																<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" value="${settleName }"/></td>
															</c:when>
															<c:otherwise>
																<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" /></td>
															</c:otherwise>
														</c:choose>
												</tr>
											</c:if>
											<tr class="trVoucher_3">
												<td class="trtextaling payforFiles-t"><c:choose><c:when test="${payType == 2 }">支付凭证</c:when><c:otherwise><c:if test="${fns:getUser().company.uuid == '7a81b21a77a811e5bc1e000c29cf2586' }"><span style="color:#f00;">*</span></c:if>收款凭证</c:otherwise></c:choose>：</td>
												<td class="payforFiles">
													<input name="passportfile" type="text" style="display:none;" disabled="disabled">
													<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
													<ol class="batch-ol">
													</ol> 
													<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
												</td>
											</tr>

											<tr>
												<td class="trtextaling" style="">备注信息：</td>
												<td class="trtextalingi" style="vertical-align:top">
													<textarea name="remarks" maxlength="254" style="width:200px;resize:none;"></textarea>
												</td>
											</tr>
										</tbody>
										</table>
									</td>
									<td valign="top"></td>
								</tr>
							</tbody>
						</table>
					</div> 
						</form>
						 
					</div>
					<!-- 汇款 -->
					<div id="offlinebox_4" style="display:none;overflow:auto;height:444px;overflow-x:hidden" class="payDiv">
						<form id="offlineform_4" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<span style="color:#F00"><em class="xing">*</em>为确保您的订单能够及时处理，请到财务开具证明。</span>
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${hasPreOpeninvoice }">
								<span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								</c:if>
							</c:if>
							<div class="ccc" > 
							<input type="hidden" name="token" value="${token}">
							<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' />
							<input type="hidden" name="fileIDList" class="fileIDList" /> 
							<input type="hidden" name="fileNameList" class="fileNameList" /> 
							<input type="hidden" name="filePathList" class="filePathList" /> 
							<input type="hidden" name="paymentStatus" value="">
							<input type="hidden" name="realBankName" value="">
							<input type="hidden" name="realBankAccount" value="">
							<input type="hidden" name="payType" value="4"> 
							<input type="hidden" name="relationInvoiceIds" value="">
							<table width="100%">
								
							    <tr>
							        <td width="360">
							<table width="100%" cellpadding="5" cellspacing="0" border="0">
								<tbody>
									<%@ include file="/WEB-INF/views/modules/order/payMoney.jsp"%>
									<c:if test="${payType==1 || payType==3}">
										<tr>
											<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
											<td class="trtextalingi"><input type="text" maxlength="25" name="payerName" class="required" value="${payerName}" /></td>
										</tr>
									</c:if>
									<tr>
										<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>来款行名称：</td>
										<td class="trtextalingi sel-bank-of-name">
										<!--  
											<c:choose>
												<c:when test="${fn:length(fmBankList)>0}">
													<select id="bankName" name="bankName" agentId="${fmBelongPlatId}">
														<option value="">--请选择--</option>
														<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
															<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
														</c:forEach>
													</select>
												</c:when>
												<c:otherwise>
													<input type="text" name="bankName" maxlength="20"/>
												</c:otherwise>
											</c:choose>
										 -->
										 <select id="bankName" name="bankName" agentId="${fmBelongPlatId}">
											<option value="">--请选择--</option>
											<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
												<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
											</c:forEach>
										</select>
											<label id="errorfmBankName" class="hide" style="color: #ea5200;margin-left: 20px;">必填信息</label>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>来款账号：</td>
										<td class="trtextalingi sel-bank-of-name">
										<!-- 
											<c:choose>
												<c:when test="${fn:length(fmBankList)>0}">
													<select id="bankAccount" name="bankAccount">
														<option value="">--请选择--</option>
													</select>
												</c:when>
												<c:otherwise>
													<input id="bankAccount" type="text" name="bankAccount" maxlength="21"/>
												</c:otherwise>
											</c:choose>
										 -->
										 <select id="bankAccount" name="bankAccount">
											<option value="">--请选择--</option>
										</select>
											<label id="errorfmBankAccount" class="hide" style="color: #ea5200;margin-left: 20px;">必填信息</label>
										</td>
									</tr>
									<c:if test="${payType==2}">
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
										<c:choose>
											<c:when test="${not empty settleName }">
												<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" value="${settleName }" /></td>
											</c:when>
											<c:otherwise>
												<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" /></td>
											</c:otherwise>
										</c:choose>
									</tr>
									</c:if>

										<%-- 68为环球行的公司id --%>

									<!-- 127 奢华之旅  汇款收款行和收款账户必填  王洋  2016.3.30 ${isSHZL } -->
									<c:choose>
										<c:when test="${isSHZL }">
											<tr>						
												<td class="trtextaling"><span style="color:#f00;">*</span>收款行名称：</td>
												<td class="trtextalingi">
													<c:choose>
														<c:when test="${fn:length(toBankList)>0 }">
															<select id="toBankNname" name="toBankNname" value="${defaultAcc.bankName }" onchange="changeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'toBankAccount')" 
																onblur="ChooseToBankNname()">
																<option value="">--请选择--</option>
																<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																<c:if test="${defaultAcc != null }">
																	<option value="${toBankList[status.index]}" selected='${toBankList[status.index]==defaultAcc.bankName? "selected":""}'>${toBankList[status.index]}</option>
																</c:if>
																<c:if test="${defaultAcc == null }">
																	<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
																</c:if>
																</c:forEach>
															</select>
															<label id="errorBankNname" class="hide" style="color: #ea5200;margin-left: 20px;">请选择收款行！</label>
														</c:when>
														<c:otherwise>
															<input type="text" name="toBankNname" maxlength="21" id="toBankNname" onblur="ChooseToBankNname()"/>
															<label id="errorBankNname" class="hide" style="color: #ea5200;margin-left: 20px;">请填写收款行！</label>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling"><span style="color:#f00;">*</span>收款账号：</td>
												<td class="trtextalingi">
													<c:choose>
														<c:when test="${fn:length(toBankList)>0 }">
															<select id="toBankAccount" name="toBankAccount">
																<c:if test="${defaultAcc != null && defaultAcc.bankName!=null }">
																	<option value="${defaultAcc.bankAccountCode}" selected="selected">${defaultAcc.bankAccountCode}</option>
																</c:if>
																<option value="">请选择</option>
															</select>
															<label id="errorBankAccount" class="hide" style="color: #ea5200;margin-left: 20px;">请选择收款账号！</label>
														</c:when>
														<c:otherwise>
															<input type="text" name="toBankAccount" maxlength="21" id="toBankAccount" onblur="InputBankAccount()"/>
															<label id="errorBankAccount" class="hide" style="color: #ea5200;margin-left: 20px;">请填写收款账号！</label>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>						<%-- 68为环球行的公司id --%>
												<td class="trtextaling">收款行名称：</td>
												<td class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="toBankNname" value="${defaultAcc.bankName }" onchange="changeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'toBankAccount')">
															<option value="">--请选择--</option>
															<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																<c:if test="${defaultAcc != null }">
																	<option value="${toBankList[status.index]}" selected='${toBankList[status.index]==defaultAcc.bankName? "selected":""}'>${toBankList[status.index]}</option>
																</c:if>
																<c:if test="${defaultAcc == null }">
																	<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
																</c:if>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="toBankNname" maxlength="21" />
													</c:otherwise>
												</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">收款账号：</td>
												<td class="trtextalingi">
													<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select id="toBankAccount" name="toBankAccount">
														<c:if test="${defaultAcc != null && defaultAcc.bankName!=null }">
															<option value="${defaultAcc.bankAccountCode}" selected="selected">${defaultAcc.bankAccountCode}</option>
														</c:if>
															<option value="">请选择</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="toBankAccount" maxlength="21" />
													</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
									
									
									<tr class="trVoucher_4">
										<td class="trtextaling payforFiles-t"><c:choose><c:when test="${payType == 2 }">支付凭证</c:when><c:otherwise><c:if test="${fns:getUser().company.uuid == '7a81b21a77a811e5bc1e000c29cf2586' }"><span style="color:#f00;">*</span></c:if>收款凭证</c:otherwise></c:choose>：</td>
										<td class="payforFiles">
											<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
											<ol class="batch-ol">
											</ol> 
											<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top"><textarea name="remarks" maxlength="254" style="width:200px; resize:none;"></textarea></td>
									</tr>
								</tbody>
							</table>
							</td>
					        <td valign="top">
					        	<c:if test="${payType==2}">
						        	<div class="charge-operator-name">
							            <p class="feeInfo">
							            	<em name="addCharge"></em>
							                <input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
							                <select name="feeCurrencyId"  class="currency">
							                	<c:forEach items="${orderCurrencys }" var="currency">
							                    	<option value="${currency.id }">${currency.currencyName }</option>
							                	</c:forEach>
							                </select>
							                <input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12" />
							            </p>
							            <p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
						        	</div>
						        </c:if>
					        </td>
							    </tr>
							</table>
							</div>
						</form>
					</div>
					<!-- 快速支付 -->
					<div id="offlinebox_5" class="payDiv">
						<form id="offlineform_5" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<input type="hidden" name="token" value="${token}">
							<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' /> 
							<input type="hidden" name="fileIDList" class="fileIDList" /> 
							<input type="hidden" name="fileNameList" class="fileNameList" /> 
							<input type="hidden" name="filePathList" class="filePathList" /> 
							<input type="hidden" name="paymentStatus" value="">
							<input type="hidden" name="payType" value="5"> 

							<table width="100%">
							    <tr>
							        <td width="360">
							<table width="100%" cellpadding="5" cellspacing="0" border="0">
								<tbody>
									<%@ include file="/WEB-INF/views/modules/order/payMoney.jsp"%>
									<c:if test="${payType==2}">
									<tr>
										<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
										<!--504需求 代收服务费添加 收款单位取值结算方名称 -->
										<c:choose>
											<c:when test="${not empty settleName }">
												<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" value = "${settleName }"/></td>
											</c:when>
											<c:otherwise>
												<td class="trtextalingi"><input type="text" maxlength="25" name="payee" class="required" /></td>
											</c:otherwise>
										</c:choose>
									</tr>
									</c:if>
									<tr class="trVoucher_5">
										<td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>支付凭证：</td>
										<td class="payforFiles">
											<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
											<ol class="batch-ol">
											</ol> 
											<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top">
											<textarea name="remarks" maxlength="254" style="width:200px;resize:none;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
							</td>
					        <td valign="top">
					        	<c:if test="${payType==2}">
						        	<div class="charge-operator-name">
							            <p class="feeInfo">
							            	<em name="addCharge"></em>
							                <input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
							                <select name="feeCurrencyId"  class="currency">
							                	<c:forEach items="${orderCurrencys }" var="currency">
							                    	<option value="${currency.id }">${currency.currencyName }</option>
							                	</c:forEach>
							                </select>
							                <input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12" />
							            </p>
							            <p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
						        	</div>
					        	</c:if>
					        </td>
							    </tr>
							</table>
						</form>
					</div>
					<!-- 银行转账start -->

					<div id="offlinebox_6" class="payDiv" style="overflow:auto;height:444px;overflow-x:hidden">
						<form id="offlineform_6" method="post" enctype="multipart/form-data" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							 <span style="color:#F00"><em class="xing">*</em>为确保您的订单能够及时处理，请到财务开具证明。</span>
							 <c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								 <c:if test="${hasPreOpeninvoice }">
								 <span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								 </c:if>
							 </c:if>
							 <div class="ddd" >
								<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' /> 
								<input type="hidden" name="fileIDList" class="fileIDList" /> 
								<input type="hidden" name="fileNameList" class="fileNameList" /> 
								<input type="hidden" name="filePathList" class="filePathList" /> 
								<input type="hidden" name="realBankName" value="">
								<input type="hidden" name="realBankAccount" value="">
								<input type="hidden" name="paymentStatus" value="">
								<input type="hidden" name="payType" value="6">
								<input type="hidden" name="relationInvoiceIds" value="">


							<table width="100%">
								
							    <tr>
							        <td width="360">
							<table width="100%" cellpadding="5" cellspacing="0" border="0">
								<tbody>
										<%@ include file="/WEB-INF/views/modules/order/payMoney.jsp"%>
									<%-- 收款信息 --%>
									<c:if test="${payType !=2 }">
										<tr>
											<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>来款单位：</td>
											<td colspan="5" class="trtextalingi">
												<input type="text" maxlength="25" name="payerName" class="required" value="${payerName}" />
											</td>
										</tr>
										<tr>
											<td class="trtextaling" style="padding:0px;">来款行名称：</td>
											<td colspan="5" class="trtextalingi">
											<!-- 
												<c:choose>
													<c:when test="${fn:length(fmBankList)>0}">
														<select name="bankName" onchange="changePayTypeBank('${fmBelongPlatId}',[this.options[this.options.selectedIndex].value],'bankAccount','offlineform_6')">
															<option value="">--请选择--</option>
															<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
																<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="bankName" maxlength="20" />
													</c:otherwise>
												</c:choose>
											 -->
											 <select id="bankName" name="bankName" agentId="${fmBelongPlatId}">
												<option value="">--请选择--</option>
												<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
													<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
												</c:forEach>
											</select>
											</td>
										</tr>
										<tr>
											<td class="trtextaling">来款账号：</td>
											<td colspan="5" class="trtextalingi">
											<!-- 
												<c:choose>
													<c:when test="${fn:length(fmBankList)>0}">
														<select name="bankAccount" >
															<option value="">--请选择--</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="bankAccount" maxlength="21" />
													</c:otherwise>
												</c:choose>
											 -->
											 <select id="bankAccount" name="bankAccount" >
												<option value="">--请选择--</option>
											</select>
											</td>
										</tr>
										<tr>
											<td class="trtextaling">收款行名称：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="toBankNname" onchange="changePayTypeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'toBankAccount','offlineform_6')">
															<option value="">--请选择--</option>
															<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="toBankNname" maxlength="21" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
	
										<tr>
											<td class="trtextaling">收款账号：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="toBankAccount" >
															<option value="">请选择</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="toBankAccount" maxlength="21" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<%-- 付款信息 --%>
									<c:if test="${payType==2}">
										<tr>
											<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>汇款行名称：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(fmBankList)>0}">
														<select id="payBankName" name="payBankName" class="required" onchange="changePayTypeBank('${fmBelongPlatId}',[this.options[this.options.selectedIndex].value],'payAccount','offlineform_6')">
															<option value="">--请选择--</option>
															<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
																<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="payBankName" maxlength="20" class="required" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>汇款账号：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(fmBankList)>0}">
														<select name="payAccount" >
															<option value="">--请选择--</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="payAccount" maxlength="21" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${not empty settleName }">
														<input type="text" maxlength="25" name="payee" class="required" value = "${settleName }"/>
													</c:when>
													<c:otherwise>
														<input type="text" maxlength="25" name="payee" class="required" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
										<tr>
											<td class="trtextaling">收款行名称：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="receiveBankName" onchange="changePayTypeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'receiveAccount','offlineform_6')">
															<option value="">--请选择--</option>
															<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="receiveBankName" maxlength="21" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>

										<tr>
											<td class="trtextaling">收款账号：</td>
											<td colspan="5" class="trtextalingi">
												<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="receiveAccount" >
															<option value="">请选择</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="receiveAccount" maxlength="21" />
													</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</c:if>
									<tr class="trVoucher_4">
										<td class="trtextaling payforFiles-t"><c:choose><c:when test="${payType == 2 }">支付凭证：</c:when><c:otherwise><c:if test="${fns:getUser().company.uuid == '7a81b21a77a811e5bc1e000c29cf2586' }"><span style="color:#f00;">*</span></c:if>收款凭证：</c:otherwise></c:choose></td>
										<td class="payforFiles">
											<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
											<ol class="batch-ol">
											</ol> 
											<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top" colspan="6">
											<textarea name="remarks" maxlength="254" style="width:200px; resize:none;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
							</td>
					        <td valign="top">
					        	<c:if test="${payType==2}">
						        	<div class="charge-operator-name">
							            <p class="feeInfo">
							            	<em name="addCharge"></em>
							                <input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
							                <select name="feeCurrencyId"  class="currency">
							                	<c:forEach items="${orderCurrencys }" var="currency">
							                    	<option value="${currency.id }">${currency.currencyName }</option>
							                	</c:forEach>
							                </select>
							                <input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12" />
							            </p>
							            <p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
						        	</div>
					        	</c:if>
					        </td>
							    </tr>
							</table>
						</div>
						</form>
					</div>
					<!-- 银行转账end -->
					<!-- 汇票start -->
					<div id="offlinebox_7" class="payDiv" style="overflow:auto;height:488px;overflow-x:hidden">
						<form id="offlineform_7" method="post" enctype="multipart/form-data" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<span style="color:#F00"><em class="xing">*</em>为确保您的订单能够及时处理，请到财务开具证明。</span>
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${hasPreOpeninvoice }">
								<span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								</c:if>
							</c:if>
							<div class="eee" >
							<input type="hidden" name="token" value="${token}">
							<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' />
							<input type="hidden" name="fileIDList" class="fileIDList" /> 
							<input type="hidden" name="fileNameList" class="fileNameList" /> 
							<input type="hidden" name="filePathList" class="filePathList" />
							<input type="hidden" name="realBankName" value="">
							<input type="hidden" name="realBankAccount" value=""> 
							<input type="hidden" name="paymentStatus" value="">
							<input type="hidden" name="payType" value="7">
							<input type="hidden" name="relationInvoiceIds" value="">
							<table width="100%">
							
							    <tr>
							        <td width="360">
							        
							        
							<table width="100%" cellpadding="5" cellspacing="0" border="0">
								<tbody>
									<%@ include file="/WEB-INF/views/modules/order/payMoney.jsp"%>
									<c:choose>
										<c:when test="${payType == 2 }">
											<tr>
												<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>出票人名称：</td>
												<td colspan="5" class="trtextalingi">
													<input class="required" type="text" maxlength="10" name="drawerName" />
												</td>
											</tr>
											<tr>
												<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>出票人账号：</td>
												<td colspan="5" class="trtextalingi">
													<input class="required" type="text" maxlength="10" name="drawerAccount" />
												</td>
											</tr>
											<tr>
												<td class="trtextaling"><span style="color:#f00;">*</span>付款行全称：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
													<c:when test="${fn:length(fmBankList)>0}">
														<select id="payBankName" name="payBankName" class="required" onchange="changePayTypeBank('${fmBelongPlatId}',[this.options[this.options.selectedIndex].value],'payAccount','offlineform_7')">
															<option value="">--请选择--</option>
															<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
																<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="payBankName" maxlength="20" class="required" />
													</c:otherwise>
												</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">汇票到期日：</td>
												<td colspan="5" class="trtextalingi">
													<input name="draftAccountedDate" class="dateinputBg" onclick="WdatePicker()" readonly="readonly" type="text">
												</td>
											</tr>
											<tr>
												<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
														<c:when test="${not empty settleName }">
															<input type="text" maxlength="25" name="payee" class="required" value="${settleName }"/>
														</c:when>
														<c:otherwise>
															<input type="text" maxlength="25" name="payee" class="required" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">收款行名称：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="receiveBankName" onchange="changePayTypeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'receiveAccount','offlineform_7')">
															<option value="">--请选择--</option>
															<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
															</c:forEach>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="receiveBankName" maxlength="21" />
													</c:otherwise>
												</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">收款账号：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
													<c:when test="${fn:length(toBankList)>0 }">
														<select name="receiveAccount" >
															<option value="">请选择</option>
														</select>
													</c:when>
													<c:otherwise>
														<input type="text" name="receiveAccount" maxlength="21"/>
													</c:otherwise>
												</c:choose>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>来款单位：</td>
												<td colspan="5" class="trtextalingi">
													<input type="text" maxlength="25" name="payerName" class="required" value="${payerName}" />
												</td>
											</tr>
											<tr>
												<td class="trtextaling" style="padding:0px;">来款行名称：</td>
												<td colspan="5" class="trtextalingi">
												<!-- 
													<c:choose>
														<c:when test="${fn:length(fmBankList)>0}">
															<select name="bankName" onchange="changePayTypeBank('${fmBelongPlatId}',[this.options[this.options.selectedIndex].value],'bankAccount','offlineform_7')">
																<option value="">--请选择--</option>
																<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
																	<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
																</c:forEach>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="bankName" maxlength="20" />
														</c:otherwise>
													</c:choose>
												 -->
												 <select id="bankName" name="bankName" agentId="${fmBelongPlatId}">
													<option value="">--请选择--</option>
													<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
														<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
													</c:forEach>
												</select>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">来款账号：</td>
												<td colspan="5" class="trtextalingi">
												<!-- 
													<c:choose>
														<c:when test="${fn:length(fmBankList)>0}">
															<select name="bankAccount" class="required">
																<option value="">--请选择--</option>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="bankAccount" maxlength="21" />
														</c:otherwise>
													</c:choose>
												 -->
												 <select id="bankAccount" name="bankAccount" class="required">
													<option value="">--请选择--</option>
												</select>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">汇票到期日：</td>
												<td colspan="5" class="trtextalingi">
													<input name="draftAccountedDate" class="dateinputBg" onclick="WdatePicker()" readonly="readonly" type="text">
												</td>
											</tr>
											<tr>
												<td class="trtextaling">收款行名称：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
														<c:when test="${fn:length(toBankList)>0 }">
															<select name="toBankNname" onchange="changePayTypeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'toBankAccount','offlineform_7')">
																<option value="">--请选择--</option>
																<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																	<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
																</c:forEach>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="toBankNname" maxlength="21"/>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">收款账号：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
														<c:when test="${fn:length(toBankList)>0 }">
															<select name="toBankAccount">
																<option value="">请选择</option>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="toBankAccount" maxlength="21" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
									<tr class="trVoucher_4">
										<td class="trtextaling payforFiles-t"><c:choose><c:when test="${payType == 2 }">支付凭证：</c:when><c:otherwise><c:if test="${fns:getUser().company.uuid == '7a81b21a77a811e5bc1e000c29cf2586' }"><span style="color:#f00;">*</span></c:if>收款凭证：</c:otherwise></c:choose></td>
										<td class="payforFiles">
											<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
											<ol class="batch-ol">
											</ol> 
											<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top" colspan="6">
											<textarea name="remarks" maxlength="254" style="width:200px; resize:none;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
							</td>
					        <td valign="top">
					        	<c:if test="${payType==2}">
						        	<div class="charge-operator-name">
							            <p class="feeInfo">
							            	<em name="addCharge"></em>
							                <input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
							                <select name="feeCurrencyId"  class="currency">
							                	<c:forEach items="${orderCurrencys }" var="currency">
							                    	<option value="${currency.id }">${currency.currencyName }</option>
							                	</c:forEach>
							                </select>
							                <input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12" />
							            </p>
							            <p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
						        	</div>
					        	</c:if>
					        </td>
							    </tr>
							</table>
						</div>	
							<div>
                          </div>
                     </form>
					</div>
					
					<!-- 汇票end -->
					<!-- POS机刷卡start -->
					<div id="offlinebox_8" class="payDiv" style="overflow:auto;height:352px;overflow-x:hidden">
						<form id="offlineform_8" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;">
							<span style="color:#F00"><em class="xing">*</em>为确保您的订单能够及时处理，请到财务开具证明。</span>
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${hasPreOpeninvoice }">
								<span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								</c:if>
							</c:if>
							<div class="fff" >
							<input type="hidden" name="token" value="${token}">
							<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' />
							<input type="hidden" name="fileIDList" class="fileIDList" /> 
							<input type="hidden" name="fileNameList" class="fileNameList" /> 
							<input type="hidden" name="filePathList" class="filePathList" />
							<input type="hidden" name="realBankName" value="">
							<input type="hidden" name="realBankAccount" value=""> 
							<input type="hidden" name="paymentStatus" value="">
							<input type="hidden" name="payType" value="8">
							<input type="hidden" name="relationInvoiceIds" value="">

							<table width="100%">
								
							    <tr>
							        <td width="360">
							<table width="100%" cellpadding="5" cellspacing="0" border="0">
								<tbody>
									<%@ include file="/WEB-INF/views/modules/order/payMoney.jsp"%>
									<c:choose>
										<c:when test="${payType == 2}">
											<tr>
												<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
													<c:when test="${not empty settleName }">
														<input type="text" maxlength="25" name="payee" class="required" value = "${settleName }"/>
													</c:when>
													<c:otherwise>
														<input type="text" maxlength="25" name="payee" class="required" />
													</c:otherwise>
												</c:choose>
												</td>
											</tr>
											<tr>
												<td class="trtextaling">收款行名称：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
														<c:when test="${fn:length(toBankList)>0 }">
															<select name="receiveBankName" onchange="changePayTypeBank('${toBelongPlatId}',[this.options[this.options.selectedIndex].value],'receiveAccount','offlineform_8')">
																<option value="">--请选择--</option>
																<c:forEach items="${toBankList}" var="bankInfo" varStatus="status">
																	<option value="${toBankList[status.index]}">${toBankList[status.index]}</option>
																</c:forEach>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="receiveBankName" maxlength="21"/>
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
	
											<tr>
												<td class="trtextaling">收款账号：</td>
												<td colspan="5" class="trtextalingi">
													<c:choose>
														<c:when test="${fn:length(toBankList)>0 }">
															<select name="receiveAccount">
																<option value="">请选择</option>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="receiveAccount" maxlength="21" />
														</c:otherwise>
													</c:choose>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="trtextaling" style="padding:0px;"><span style="color:#f00;">*</span>来款单位：</td>
												<td colspan="1" class="trtextalingi">
													<input type="text" maxlength="25" name="payerName" class="required" value="${payerName}" />
												</td>
											</tr>
											<tr>
		                                        <td class="trtextaling" style="padding:0px;">来款行名称：</td>
		                                        <td colspan="5" class="trtextalingi">
		                                        <!-- 
													<c:choose>
														<c:when test="${fn:length(fmBankList)>0}">
															<select name="bankName" onchange="changePayTypeBank('${fmBelongPlatId}',[this.options[this.options.selectedIndex].value],'bankAccount','offlineform_8')">
																<option value="">--请选择--</option>
																<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
																	<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
																</c:forEach>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="bankName" maxlength="20" />
														</c:otherwise>
													</c:choose>
												 -->
												 <select id="bankName" name="bankName" agentId="${fmBelongPlatId}">
													<option value="">--请选择--</option>
													<c:forEach items="${fmBankList}" var="bankInfo" varStatus="status">
														<option value="${fmBankList[status.index]}">${fmBankList[status.index]}</option>
													</c:forEach>
												</select>
		                                        </td>
		                                    </tr>
											<tr>
		                                        <td class="trtextaling" style="padding:0px;">来款账号：</td>
		                                        <td colspan="5" class="trtextalingi">
		                                        <!-- 
													<c:choose>
														<c:when test="${fn:length(fmBankList)>0}">
															<select name="bankAccount">
																<option value="">--请选择--</option>
															</select>
														</c:when>
														<c:otherwise>
															<input type="text" name="bankAccount" maxlength="21" />
														</c:otherwise>
													</c:choose>
												 -->
												 <select id="bankAccount" name="bankAccount">
													<option value="">--请选择--</option>
												</select>
		                                        </td>
		                                    </tr>
										</c:otherwise>
									</c:choose>
									<tr class="trVoucher_4">
										<td class="trtextaling payforFiles-t"><c:choose><c:when test="${payType == 2 }">支付凭证：</c:when><c:otherwise><c:if test="${fns:getUser().company.uuid == '7a81b21a77a811e5bc1e000c29cf2586' }"><span style="color:#f00;">*</span></c:if>收款凭证：</c:otherwise></c:choose></td>
										<td class="payforFiles">
											<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
											<ol class="batch-ol">
											</ol> 
											<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
										</td>
									</tr>
									<tr>
										<td class="trtextaling" style="">备注信息：</td>
										<td class="trtextalingi" style="vertical-align:top" colspan="6">
											<textarea name="remarks" maxlength="254" style="width:200px; resize:none;"></textarea>
										</td>
									</tr>
								</tbody>
							</table>
							</td>
					        <td valign="top">
					        	<c:if test="${payType eq 2}">
						        	<div class="charge-operator-name">
							            <p class="feeInfo">
							            	<em name="addCharge"></em>
							                <input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
							                <select name="feeCurrencyId"  class="currency">
							                	<c:forEach items="${orderCurrencys }" var="currency">
							                    	<option value="${currency.id }">${currency.currencyName }</option>
							                	</c:forEach>
							                </select>
							                <input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12"/>
							            </p>
							            <p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
						        	</div>
					        	</c:if>
					        </td>
							    </tr>
							</table>
							 </div>
							<div>
                        </div>
                    </form>
					</div>
					<!-- POS机刷卡end -->
					<%--拉美图增加因公支付宝 --%>
					<c:if test="${fns:getUser().company.uuid eq '7a81a26b77a811e5bc1e000c29cf2586'}">
						<div id="offlinebox_9" class="payDiv" style="display: none;">
							<span style="color:#f00;">*为确保您的订单能够及时处理，请正确填写以下信息。</span>
							<c:if test="${fns:getUser().company.preOpenInvoice == '1' }">
								<c:if test="${hasPreOpeninvoice }">
								<span style="margin-left: 250px"><a style="color: #0000FF" href="javascript:void(0);" onclick="relationInvoiceList()" target="_blank">关联发票</a></span>
								</c:if>
							</c:if>
									<form class="offlineform_9" id="offlineform_9" method="post" action="${ctx}/orderPayMore/savePay" style="margin:0px; padding:0px;position:relative;" novalidate="novalidate">
									<input type="hidden" name="token" value="${token}">
									<input type="hidden" name="orderPayInputJson" value='${orderPayInputJson}' />
									<input type="hidden" name="fileIDList" class="fileIDList" />
									<input type="hidden" name="fileNameList" class="fileNameList" />
									<input type="hidden" name="filePathList" class="filePathList" />
									<input type="hidden" name="realBankName" value="">
									<input type="hidden" name="realBankAccount" value=""/>
									<input type="hidden" name="paymentStatus" value="">
									<input type="hidden" name="payType" value="9">
									<table width="100%">
									    <tr>
									        <td width="360">
										<table width="100%" cellpadding="5" cellspacing="0" border="0">
											<tbody>
												<c:if test="${!empty moneyFlag && moneyFlag != 0}">
													<c:forEach items="${paramCurrencyId}" var="currencyId" varStatus="status">
														<c:forEach items="${curlist}" var="cur">
															<c:if test="${cur.id==currencyId}">
																<tr>
																	<td width="34%" valign="top" class="trtextaling">${cur.currencyName}金额：${cur.currencyMark}</td>
																	<td width="61%" valign="top">
																		<c:choose>
																			<c:when test="${moneyFlag==1}">
																				<input type="text" maxlength="12" value="<fmt:formatNumber type='currency' pattern='###0.00' value='${paramCurrencyPrice[status.index]}' />" name="dqzfprice" class="required"/>
																			</c:when>
																			<c:when test="${moneyFlag==2}">
																				<input type="text" maxlength="12" value="<fmt:formatNumber type='currency' pattern='###0.00' value='${paramCurrencyPrice[status.index]}' />" name="dqzfprice" readonly="readonly" />
																			</c:when>
																		</c:choose>
																	</td>
																	<input type="hidden" value="${cur.id}" name="currencyIdPrice" />
																</tr>
															</c:if>
														</c:forEach>
													</c:forEach>
												</c:if>
												<!--S 20160113 127-->
												<c:set var="isPayment" value="${payType eq 2}"/><%-- 是否是付款 --%>
												<c:if test="${!isPayment}">
													<tr>
														<td class="trtextaling"><span style="color:#f00;">*</span>来款单位：</td>
														<td class="trtextalingi">
															<input type="text" maxlength="25" name="payerName" class="required" id="payerNameID_9" value="${payerName}">
														</td>
													</tr>
												</c:if>
												<tr>
													<td class="trtextaling"><c:if test="${isPayment}"><span style="color:#f00;">*</span></c:if>支付宝名称（来款）：</td>
													<td class="trtextalingi sel-bank-of-name">
														<c:choose>
															<c:when test="${!isPayment}">
																<input type="text" maxlength="50" name="fromAlipayName" >
															</c:when>
															<c:otherwise>
																<c:if test="${empty alipay }" >
																	<input type="text" maxlength="50" name="fromAlipayName" id="fromAlipayName" >
																</c:if>
																<c:if test="${! empty alipay }">
																	<select name="fromAlipayName" onchange="getAlipayAccount1(this)" id="fromAlipayName">
																		<option value="">请选择</option>
																		<c:forEach items="${alipay }" var="p">
																			<option value="${p.name }" <c:if test="${p.defaultFlag==0 }">selected</c:if>>${p.name }</option>
																		</c:forEach>
																	</select>
																</c:if>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="trtextaling"><c:if test="${isPayment}"><span style="color:#f00;">*</span></c:if>支付宝账号（来款）：</td>
													<td class="trtextalingi sel-bank-of-name">
														<c:choose>
															<c:when test="${!isPayment}">
																<input type="text" maxlength="50" name="fromAlipayAccount" >
															</c:when>
															<c:otherwise>
																<c:if test="${empty alipay }">
																	<input type="text" maxlength="50" name="fromAlipayAccount" class="payerNameID_6" id="fromAlipayAccount">
																</c:if>
																<c:if test="${! empty alipay }">
																	<select name="fromAlipayAccount" id="fromAlipayAccount">
																		<c:if test="${! empty account }">
																			<c:forEach items="${account }" var="p">
																				<option value="${p.account }" <c:if test="${p.defaultFlag==0 }">selected</c:if>>${p.account }</option>
																			</c:forEach>
																		</c:if>
																		<c:if test="${empty account }"><option value="">请选择</option></c:if>
																	</select>
																</c:if>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<c:choose>
														<c:when test="${isPayment}">
															<td class="trtextaling"><span style="color:#f00;">*</span>收款单位：</td>
															<td class="trtextalingi">
																<c:choose>
																	<c:when test="${not empty settleName }">
																		<input type="text" maxlength="25" name="payee" id="alipay_payee" class="required" value="${settleName }"/>
																	</c:when>
																	<c:otherwise>
																		<input type="text" maxlength="25" name="payee" id="alipay_payee" class="required" />
																	</c:otherwise>
																</c:choose>
															</td>
														</c:when>
														<c:otherwise>
															<td class="trtextaling">收款单位：</td>
															<td class="trtextalingi sel-bank-of-name">
																<c:choose>
																	<c:when test="${not empty settleName }">
																		<input type="text" maxlength="25" name="comeOfficeName" value="${settleName }">
																	</c:when>
																	<c:otherwise>
																		<input type="text" maxlength="25" name="comeOfficeName" value="${comOffice }">
																	</c:otherwise>
																</c:choose>
															</td>
														</c:otherwise>
													</c:choose>
												</tr>
												<tr>
													<td class="trtextaling">支付宝名称（收款）：</td>
													<td class="trtextalingi sel-bank-of-name">
														<c:choose>
															<c:when test="${isPayment}">
																<input type="text" maxlength="50" name="toAlipayName" >
															</c:when>
															<c:otherwise>
																<c:if test="${empty alipay }" >
																	<input type="text" maxlength="50" name="toAlipayName1" >
																</c:if>
																<c:if test="${! empty alipay }">
																	<select name="toAlipayName" onchange="getAlipayAccount()">
																		<option value="">请选择</option>
																		<c:forEach items="${alipay }" var="p">
																			<option value="${p.name }" <c:if test="${p.defaultFlag==0 }">selected</c:if>>${p.name }</option>
																		</c:forEach>
																	</select>
																</c:if>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr>
													<td class="trtextaling">支付宝账号（收款）：</td>
													<td class="trtextalingi sel-bank-of-name">
														<c:choose>
															<c:when test="${isPayment}">
																<input type="text" maxlength="50" name="toAlipayAccount" class="payerNameID_6">
															</c:when>
															<c:otherwise>
																<c:if test="${empty alipay }">
																	<input type="text" maxlength="50" name="toAlipayAccount1" class="payerNameID_6">
																</c:if>
																<c:if test="${! empty alipay }">
																	<select name="toAlipayAccount" >
																		<c:if test="${! empty account }">
																			<c:forEach items="${account }" var="p">
																					<option value="${p.account }" <c:if test="${p.defaultFlag==0 }">selected</c:if>>${p.account }</option>
																			</c:forEach>
																		</c:if>
																		<c:if test="${empty account }"><option value="">请选择</option></c:if>
																	</select>
																</c:if>
															</c:otherwise>
														</c:choose>
													</td>
												</tr>
												<tr class="trVoucher_4">
												<c:choose>
													<c:when test="${isPayment }"><td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>支付凭证：</td></c:when>
													<c:otherwise><td class="trtextaling payforFiles-t"><span style="color:#f00;">*</span>收款凭证：</td></c:otherwise>
												</c:choose>
													
													<td class="payforFiles">
														<input type="button" name="DocInfoIds" class="btn btn-primary" value="上传" onclick="uploadFiles('${ctx}',null,this);" />
														<ol class="batch-ol">
														</ol>
														<label style="display:none;font-size: 12px;font-weight: normal;padding-left: 0;margin-left: 10px;padding-bottom: 2px;width: 60px;color: #ea5200">必填信息</label>
													</td>
												</tr>
												<tr>
													<td class="trtextaling" style="">备注信息：</td>
													<td class="trtextalingi" style="vertical-align:top">
														<textarea name="remarks" maxlength="200" style="width:200px; resize:none;"></textarea>
													</td>
												</tr>
											</tbody>
										</table>
											</td>
											<td valign="top">
												<c:if test="${isPayment}">
													<div class="charge-operator-name">
														<p class="feeInfo">
															<em name="addCharge"></em>
															<input class="name" name="feeName" type="text" maxlength="20" placeholder="手续费名称" onkeyup="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onkeydown="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')" onafterpast="this.value=this.value.replace(/[^a-zA-Z0-9\u4e00-\u9fa5]/g,'')"/>
															<select name="feeCurrencyId"  class="currency">
																<c:forEach items="${orderCurrencys }" var="currency">
																	<option value="${currency.id }">${currency.currencyName }</option>
																</c:forEach>
															</select>
															<input class="money" data-type="amount" name="feeAmount" type="text" maxlength="12"/>
														</p>
														<p class="tdred block" style="display: none;"><span class="feeName"></span><span class="feeAmount"></span></p>
													</div>
												</c:if>
											</td>
											</tr>
										</table>
									</form>
							</div>
					</c:if>
				</div>
			</div>
		</div>

		<div style="overflow:hidden">
			<div class="kongr"></div>
		</div>
		<div class="ydbz_sxb ydbz_button">
			<c:if test="${not empty orderDetailUrl}">
				<a target="_blank" class="ydbz_x" href="${ctx}${orderDetailUrl}">查看订单</a>
			</c:if>
			<a id="payBtn" class="ydbz_x" onclick="formSunbmit()">
				<c:choose>
					<c:when test="${payType ne 2}">确认收款</c:when> <%--进行收款显示确认收款 478需求--%>
					<c:otherwise>确认支付</c:otherwise>
				</c:choose>
			</a>
			
			<c:choose>
				<c:when test="${not empty entryOrderUrl}">
					<a id="nopayBtn" class="ydbz_x" href="${ctx}${entryOrderUrl}">
						<c:choose>
							<c:when test="${payType ne 2}">暂不收款</c:when> <%--进行收款显示暂不收款 478需求--%>
							<c:otherwise>暂不支付</c:otherwise>
						</c:choose>
					</a>
				</c:when>
				<c:otherwise>
					<a id="nopayBtn" class="ydbz_x" href="${ctx}${orderListUrl}" <c:if test="${orderListUrl == null || orderListUrl == ''}">onclick="window.close();"</c:if>>
						<c:choose>
							<c:when test="${payType ne 2}">暂不收款</c:when> <%--进行收款显示暂不收款 478需求--%>
							<c:otherwise>暂不支付</c:otherwise>
						</c:choose>
					</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	
	<div id="relationInvoice" class="display-none">
		<div class="select_account_pop" style="padding:20px">
			<div >
				<table class="activitylist_bodyer_table">
					<thead>
						<tr>
							<th>选择</th>
							<th>发票号</th>  
							<th>团号</th>
							<th>申请人</th>
							<th>开票状态</th>
							<th>开票金额</th>
						</tr>
					</thead>
					<tbody id="relationInvoiceTable">
						<tr>
							<td name='logId'  class='tc' value=''><input id="" type="checkbox" value=""></td>
							<td name='operation' class='tc'><span></span></td>
							<td name='operation' class='tc'><span></span></td>
							<td name='operationTime' class='tc'><span></span></td>
							<td name='mainContext' class='tl'><span></span></td>
							<td name='mainContext' class='tl'><span></span></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>