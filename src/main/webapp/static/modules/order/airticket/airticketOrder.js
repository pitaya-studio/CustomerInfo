var agentList = [];
var submit_times = 0;
var jprice = '';
var isdoSave = false;
var count = 0;
//拉美途uuid
var lameitourUuid = '7a81a26b77a811e5bc1e000c29cf2586';
outerrorList = new Array();
var formatNumberArray = new Array();
formatNumberArray.push("sum");
jQuery(function($) {
	$.fn.datepicker = function(option) {
		var opt = $.extend( {}, option);
		$(this).click(function() {
			WdatePicker(opt);
		});
	}
});




$(function() {

	$("#addTraveler").click(
			function() {
				var $table = $("#travelerTemplate").children();
				var _travelerForm = $table.clone().addClass("travelerTable");
				_travelerForm.find(':input[name=nationality] option[value=461]').attr('selected',true);
				_travelerForm.find("input[name^='personType']").attr("name","personTypeCount" + count++);
				_travelerForm.find(':input[name=nationality] option[value=461]').attr('selected',true);

				//默认添加游客信息时，判断什么游客类型
				var selFlag = false;
				var personType = 0;
				var personval = 0;
				if ($("#orderPersonNumAdult").val() > countAdult()){
					selFlag = true;
					personType = 0;
					personval = 1;
				}
				if(!selFlag){
					if ($("#orderPersonNumChild").val() > countChild()){
						selFlag = true;
						personType = 1;
						personval = 2;
					}
				}
				if(!selFlag){
					if ($("#orderPersonNumSpecial").val() > countSpecial()){
						selFlag = true;
						personType = 2;
						personval = 3;
					}
				}
				$("#traveler").append(_travelerForm);
				$("input[name^='personType']",_travelerForm)[personType].checked = true;
				_travelerForm.find("input[name^='personType']:checked").trigger("change");
				$($("input[name^='personType']",_travelerForm)[personType]).change();
				dodatePicker();
				// 填充游客默认姓名 （“游客  + index”）
				fillTravelerDefaultName();
			});

	$("#orderPersonNumChild").blur(function() {
		changeorderPersonelNum(this);
		checkMaxChildrenCount();
	});

	$("#orderPersonNumAdult").blur(function() {
		changeorderPersonelNum(this);
	});
	
	$("#orderPersonNumSpecial").blur(function() {
		changeorderPersonelNum(this);
		checkMaxSpecialPeopleCount();//限制特殊人群数量
	});

	$("#orderPersonelNum").blur(function() {
		var travelerNum = $(this).val();
		checkFreePosition();
		var deposit = $("#payDeposit").val();
		$("#frontMoney").val(deposit * travelerNum);
		changeTotalPrice();
	});

	dodatePicker();

	$("#traveler").delegate(
			"input[name='travelerName']",
			"blur",
			function() {
				var srcname = $(this).val();
				
				var name = $(this).closest("form").find(".tourist-t-off .name").text(srcname);
				
				if ($.trim(srcname).length <= 0) {
					return false;
				}

				var pinying = $(this).closest("form").find(
						"input[name='travelerPinyin']").eq(0);
				$.ajax( {
					type : "POST",
					url : "../../orderCommon/manage/getPingying",
					data : {
						srcname : $.trim(srcname)
					},
					success : function(msg) {
						pinying.val(msg);
					}
				});
				
			});

	$("#traveler").delegate(
			"a[name='deleteTraveler']",
			"click",
			function() {
				var $this = $(this);
				var travelerId = $(this).closest('.travelerTable').find(
						"input.traveler[name='id']").val();
				$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
					if (v == 'ok') {
						// 显示按钮
						$("#addTraveler").parent().show();
						if (travelerId == "") {
						// 无需记录
							$this.closest('.travelerTable').remove();
							changeTotalPrice();
						} else {
							$this.closest('.travelerTable').remove();
							changeTotalPrice();
							//订单总结算价重新计算
							$("#travelerSumPrice_js").html(calculateSumSettlement());
						}
				} else if (v == 'cancel') {

				}
			}	);
				
		
			});
	

	// 改变人员类型时
	$("#traveler").delegate(
			"input[name^='personType']",
			"change",
			function() {
				var texamt = Number($("#texamt").val());//税
				var airticketbz = $("#airticketbz").val();//币种
				var etj = Number($("#etj").val());//儿童
				var crj = Number($("#crj").val());//成人
				var tsj = Number($("#tsj").val());//特殊人群
				var jsarray=new Array();
				var isly=$(this).closest("form").find("input[name='ydbz2intermodalType']:checked").val();//是否联运
				var lyinfo=$(this).closest("form").find("select[name='lysel']").val();//联运信息
				var currency = $('#bz').val();
				var bzobj= {currencyId:"-1",currencyName:"-1",currencyMark:""+currency+"" ,je:"0"};
				if("1"==isly){
					$(this).parents("form").find("[data-flag='star']").show();
					var lyvalues=lyinfo.split(",");
					bzobj.currencyId=lyvalues[1];
					bzobj.currencyName=lyvalues[2];
					bzobj.currencyMark=lyvalues[3];
					bzobj.je=lyvalues[4];
					jsarray.push(bzobj);
				}else{
					$(this).parents("form").find("[data-flag='star']").hide();
				}
				
				var airticketbzObj = $.parseJSON(airticketbz);//机票币种
				var jsj_yk;
				if ($(this).val() == 2) {//儿童
					airticketbzObj.je=etj+Number(texamt);
					jsj_yk=etj;
				} else if ($(this).val() == 1) {//成人
					airticketbzObj.je=crj+Number(texamt);
					jsj_yk=crj;
				} else {//特殊人群
					airticketbzObj.je=tsj+Number(texamt);
					jsj_yk=tsj;
				}
				
				jsarray.push(airticketbzObj);
				var jsj_yk_ly=getbzStr(jsarray);//游客联运结算价
				jprice = jsj_yk_ly;
				$(this).closest("form").find("input[name='payPrice']").val(jsj_yk);//隐藏结算价
				$(this).closest("form").find("input[name='lyPrice']").val(bzobj.currencyId+","+bzobj.currencyName+","+bzobj.currencyMark+","+bzobj.je);//隐藏联运价
				$(this).closest("form").find("input[name='payPrice']").prev().text(jsj_yk_ly);//下面显示
				$(this).closest("form").find(".tourist-t-off .ydFont2").text(jsj_yk_ly);//上面显示
				
				//可编辑的结算价
				var $nowJs = $(this).closest("form").find("[data='newJsPrice']");
				var jsHTML = getbzHTML(jsarray,$nowJs);
				$nowJs.html(jsHTML);
				
				//订单总价重新计算
				changeTotalPrice();
				
				//订单总结算价重新计算
				$("#travelerSumPrice_js").html(calculateSumSettlement());
			});

	// 第一步的下一步
	$("#oneToSecondStepDiv").click(function() {
		var flag = checkMaxSpecialPeopleCount();
		if (!flag) {
			return false;
		}
		flag = checkMaxChildrenCount();
		if (!flag) {
			return false;
		}
		outerrorList = new Array();
		_doValidateorderpersonMesdtail();// 订单联系人 验证
		_doValidateproductOrderTotal();//
//		createDivInTable(outerrorList);
		var totalPrice = $("#travelerSumPrice").text().replace(/\,/g,'');
		
		$("#travelerSumPrice_js").html(calculateSumSettlement());
		
		// 验证页面有效性和余位数检查
		if (outerrorList.length > 0 || !checkFreePosition()) {
			return;
		}
		
		$("#productOrderTotal").disableContainer( {
			blankText : "—",
			formatNumber : formatNumberArray
		});
//		$("#orderpersonMesdtail").disableContainer( {
//			blankText : "—",
//			formatNumber : formatNumberArray
//		});
		$("#addContact").hide();
		$("#orderpersonMesdtail span.gray").hide();
		
		$("#manageOrder_new").show();
		$("#manageOrder_m").show();
		$("#oneToSecondOutStepDiv").hide();
		$("#ordercontact").find(".yd1AddPeople").hide();
		$("#secondDiv").show();

		var _closeOrExpand = $(".closeOrExpand").eq(0);
		if (_closeOrExpand.hasClass("ydExpand")) {
			_closeOrExpand.click();
		}
		_closeOrExpand.parent().next().next(".orderPersonMsg").show();
		$("#stepbar").removeClass("yd-step1").addClass("yd-step2");
		changeTotalPrice();
		readOnlyAllContactInfo();
	});

	// 第二步的上一步
	$("#secondToOneStepDiv").click(function() {
		$("#productOrderTotal").undisableContainer();
		$("#orderpersonMesdtail").undisableContainer();
		$("#addContact").show();
		$("#orderpersonMesdtail span.gray").show();
		$("#oneToSecondOutStepDiv").show();
		$("#ordercontact").find(".yd1AddPeople").show();
		$("#secondDiv").hide();
		$("#manageOrder_new").hide();
		$("#manageOrder_m").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step1");
		back2WritableContactInfo();
		var _closeOrExpand = $(".closeOrExpand").eq(0);
		_closeOrExpand.parent().next().next(".orderPersonMsg").hide();
		if (_closeOrExpand.hasClass("ydClose")) {
			_closeOrExpand.click();
		}

	});
	// 第二步的下一步
	$("#secondToThirdStepDiv").click(
			function() {
				
				// 如果有没保存游客则需要先保存
				var isSaveTravelerFlag = true;
				$("form.travelerTable").each(function(index, obj) {
					if ($("a[name=savePeople]", this).text() == "保存") {
						isSaveTravelerFlag = false;
			            return false;
					}
				});
				if (!isSaveTravelerFlag) {
					top.$.jBox.tip('请先保存游客', 'error');
		            return false;
				}
				
				var adultNum = $("#orderPersonNumAdult").val();
				var childNum = $("#orderPersonNumChild").val();
				var specialNum = $("#orderPersonNumSpecial").val();
				if (countAdult() > adultNum) {
					top.$.jBox.tip('成人人数与初始值不匹配请修改', 'error');
		            return false;
				}
				if (countChild() > childNum) {
					top.$.jBox.tip('儿童人数与初始值不匹配请修改', 'error');
		            return false;
				}
				if (countSpecial() > specialNum) {
					top.$.jBox.tip('特殊人数与初始值不匹配请修改', 'error');
		            return false;
				}
				var isly = document.getElementsByName("ydbz2intermodalType");
				var lysel = document.getElementsByName("lysel");
				for(i = 2; i < isly.length; i++) {
					if (isly[i].value == 1 && isly[i].checked) {
						if (lysel[(i-1)/2].value == 0) {
							top.$.jBox.tip('请选择联运方式', 'error');
				            return false;
						}
					}
				}
				$("a[name='savePeople']:visible").each(function(index, obj) {
					$(this).trigger("click");
				});
				outerrorList = new Array();
				_doValidatecontactForm();
				_doValidatetravelerForm();
				_doValidateorderpersonMesdtail();
				_doValidateproductOrderTotal();

				createDivInDiv(outerrorList);

				if (outerrorList.length > 0) {
					return;
				}
				
				//保存信息
				//saveAllSecondInfo();
				
				$("#productOrderTotal").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#orderpersonMesdtail").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#manageOrder_new").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#manageOrder_m").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				var sums = $(".tourist-right").find("input[name='sum']");
				$.each(sums, function(key, value) {
					var currency = $('#bz').val();
					var _$span = $(value).next();
					_$span.text(""+currency+"" + _$span.text());
					;
				});

//				if(flag) {
//					//游客身份证类型不能修改
//					$("input[name='papersType']").attr("disabled", true);
//				}
				
				$("#secondDiv").hide();
				$("#thirdDiv").show();
				var _closeOrExpand = $(".closeOrExpand").eq(0);
				_closeOrExpand.parent().next().next(".orderPersonMsg").hide();
				$("#stepbar").removeClass("yd-step2").addClass("yd-step3");
				
				// 金额字体过小，把影响字体样式去掉
				$("span[data=newJsPrice] span").removeClass("disabledshowspan");
			});
	$("#thirdToSecondTStepDiv").click(function() {
		//游客身份证类型可以修改
		$("input[name='papersType']").attr("disabled", false);
		$("#manageOrder_new").undisableContainer();
		$("#manageOrder_m").undisableContainer();

		if ($("#orderid").val() != "") {
			$("#productOrderTotal").undisableContainer();
			$("#orderpersonMesdtail").undisableContainer();
		}

		$("#secondDiv").show();
		$("#thirdDiv").hide();
		// 如果是修改 这个地方不能显示

			if ($("#orderid").val() == "") {
				var _closeOrExpand = $(".closeOrExpand").eq(0);
				_closeOrExpand.parent().next().next(".orderpersonMesdtail").show();
			}

			$("#stepbar").removeClass("yd-step3").addClass("yd-step2");
			
			// 金额字体过小，把影响字体样式去掉
			$("span[data=newJsPrice] span").remove();
		});

	$("#thirdToFourthStepDiv").click(function() {
		//防止多次提交
		if (submit_times == 0) {
			loading('正在提交，请稍等...');
			submit_times++;
		} else {
			top.$.jBox.info("您已提交，请耐心等待", "警告");
			return false;
		}
		saveData(true);
	});

	$(".closeOrExpand").click(function() {
		var obj_this = $(this);
		if (obj_this.attr("class").match("ydClose")) {
			obj_this.removeClass("ydClose");
			obj_this.parent().next("[flag=messageDiv]").eq(0).show();
		}
	});
});

/**
 * 限制特殊人群数量
 */
function checkMaxSpecialPeopleCount() {
	var flag = true;
	var orderPersonNumSpecialVal = parseInt($('#orderPersonNumSpecial').val());
	var maxPeopleCountVal = parseInt($("#maxPeopleCount").val());
	var lastPeopleCountVal = parseInt($("#lastPeopleCount").val());
	if(maxPeopleCountVal != null && orderPersonNumSpecialVal > (maxPeopleCountVal-lastPeopleCountVal)) {
		$.jBox.tip('特殊人群最高人数为'+maxPeopleCountVal+'人，剩余：'+(maxPeopleCountVal-lastPeopleCountVal)+'，请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
		flag = false;
	}
	return flag;
}
/**
 * 儿童最高人数
 */
function checkMaxChildrenCount() {
	var flag = true;
	var orderPersonNumChildren = parseInt($('#orderPersonNumChild').val());
	var maxChildrenCountVal = parseInt($("#maxChildrenCount").val());
	var lastChildrenCountVal = parseInt($("#lastChildrenCount").val());
	if(maxChildrenCountVal != null && orderPersonNumChildren >( maxChildrenCountVal-lastChildrenCountVal)) {
		$.jBox.tip('儿童最高人数为'+maxChildrenCountVal+'人，剩余：'+( maxChildrenCountVal-lastChildrenCountVal)+'，请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
		flag = false;
	}
	return flag;
}

//预定第一步添加信息
function addPeople(obj){
	var contactPeopleNum = $("ul[name=orderpersonMes]").length;
	var newNum = contactPeopleNum+1;
	$('#ordercontact').append('<ul class="ydbz_qd" name="orderpersonMes">'+
		'<li><label><span class="xing">*</span>渠道联系人<font>' + (newNum) +'</font>：</label><input maxlength="10" type="text" name="contactsName_'+newNum+'" value="" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>'+
		'<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="15" type="text" name="contactsTel_'+newNum+'" value="" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,\'\',\'2\')">展开全部</div><span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span></li>'+
		'<div flag="messageDiv" style="display:none" class="ydbz_qd_close">'+
		'<li><label>固定电话：</label><input type="text" name="contactsTixedTel_'+newNum+'"/></li>'+
		'<li><label>联系人地址：</label><input type="text" name="contactsAddress_'+newNum+'"/></li>'+
        '<li><label>传真：</label><input maxlength="" type="text" name="contactsFax_'+newNum+'"/></li>'+
        '<li><label>QQ：</label><input maxlength="" type="text" name="contactsQQ_'+newNum+'"/></li>'+
        '<li><label>Email：</label><input maxlength="" type="text" name="contactsEmail_'+newNum+'"/></li>'+
        '<li><label>邮编：</label><input maxlength="" type="text" name="contactsZipCode_'+newNum+'"/></li>'+
        '<li><label>其他：</label><input maxlength="" type="text" name="remark_'+newNum+'"/></li>'+
        '</div></ul>');
}

//保存 isPay是否支付
function saveData(isPay){
	var blresult = false;
	var activityId = $("#productId").val();
	var payMode = $("#payMode").val();
	var orderid =$("#orderid").val();
	var orderno = $("#orderno").val();
	var salerId = $("#salerId").val();
	var orderPersonNumChild = $("#orderPersonNumChild").val();
	var orderPersonNumAdult = $("#orderPersonNumAdult").val();
	var orderPersonNumSpecial = $("#orderPersonNumSpecial").val();
	var nagentName = $("#nagentName").val();
	var orderPersionNum = $("#orderPersonelNum").val();
	var agentId = $("#agentId").val();
	var totalPrice = $("#travelerSumPrice").text().replace(/\,/g,'');
	var casttotalPrice = $("#travelerSumPrice_js").text().replace(/\,/g,'');
	var placeHolderType = $("#placeHolderType").val();
	var specialRemark = $("form.contactTable textarea").val();
	// 联系人
	//var contacttables = $("#ordercontact ul");
	var contacttables = $("#ordercontact .ydbz_qd");
	var datacontacts = new Array();
	$.each(contacttables, function(key, valueout) {
		var contactinputs = $(valueout).find("input");
		var datacontact = {};
		$.each(contactinputs, function(key, value) {
			
			var _nametemp = $(value).attr("name");
			if(_nametemp.indexOf("_")>0){
				_nametemp = _nametemp.substring(0,_nametemp.indexOf("_"));
			}
			datacontact[_nametemp] = $(value).val();
		});
		datacontacts.push(datacontact);
	});
	
	//游客
	var forms = $("#traveler form");
	var travelerArray = new Array();
	$.each(forms, function(key, value) {
		travelerArray.push(getTravelerFormData(value));
	});
	
	//组织一个money_amount的list，用以装载所有游客的返佣信息
	var rebates = new Array();
	$.each(forms, function(key, value) {
		rebates.push(getRebateObj(value));
	});
	var rbform = $("form[name=groupRebates]");
	var groupRebatesMoney = $("input[name=groupRebatesMoney]",rbform).val();	//返佣费用金额	
    var groupRebatesCurrency = $("select[name=groupRebatesCurrency]",rbform).val();	//返佣费用币种
    var orderType = $("#orderType").val();

	var amountObj=getAmountCurrencyObjs();//返回多币种id，金额
	
	var tmpData = {"activityId" : activityId, 
			        "payMode" : payMode, 
			        "orderid" : orderid, 
			        "orderno" : orderno,
			        "salerId" : salerId,
			        "orderPersonNumChild" : orderPersonNumChild, 
			        "orderPersonNumAdult" : orderPersonNumAdult, 
			        "orderPersonNumSpecial" : orderPersonNumSpecial, 
			        "orderPersionNum" : orderPersionNum,
			        "orderContactsJSON" : JSON.stringify(datacontacts),
			        "orderTravelerJSON" : JSON.stringify(travelerArray),
			        "amountJSON" : JSON.stringify(amountObj),
			        "agentId" : agentId,
			        "totalPrice" : totalPrice, //成本价
			        "casttotalPrice" : casttotalPrice, //结算价
			        "placeHolderType" : placeHolderType,
			        "specialRemark" : specialRemark,
			        "nagentName" : nagentName,
			        "rebatesJSON" : JSON.stringify(rebates),
			        "groupRebatesMoney" : groupRebatesMoney,
			        "groupRebatesCurrency" : groupRebatesCurrency,
			        "orderType" : orderType
			        };
	
	$.ajax({
		  type: "POST",
		  url: g_context_url + "/order/airticket/saveAirticketOrder",
		  dataType: "json",
		  data: tmpData,
		  async: false,
		  success:function(msg){
			  if (msg && msg.orderId) {
				  if(isPay){
					  orderid=msg.orderId;
					  orderno = msg.orderNo;
					  $("#orderid").val(orderid);
					  $("#orderno").val(orderno);
					  doPay();
				  }
			  }else if (msg.errorMsg) {
					top.$.jBox.tip(msg.errorMsg, 'error');
			  }else {
				  submit_times = 0;
				  if (msg.errorMsg) {
					  alert(msg.errorMsg);
				  } else {
					  alert("保存失败！");
				  }
			  }
		  }
		});
	
	
}

//判断预定时候的价格 ，如果没有价格不可以添加人数
$("document").ready(function(){
	
	var sAdultPrice = $("#sAdultPrice").html();
	var sChildPrice =$("#sChildPrice").html();
	var sSpecialPrice = $("#sSpecialPrice").html();
	if(sAdultPrice==".00"){
		$("#orderPersonNumAdult").attr("readonly","readonly");
	}
	if(sChildPrice==".00"){
		$("#orderPersonNumChild").attr("readonly","readonly");
	}
	if(sSpecialPrice==".00"){
		$("#orderPersonNumSpecial").attr("readonly","readonly");
	}
});


//支付
function doPay(){
	var orderId = $("#orderid").val();
	var orderno = $("#orderno").val();
	var orderType = $("#orderType").val();
	var payMode = $("#payMode").val();
	var price = $("#travelerSumPrice").text().replace(/\,/g,'');
	var agentId = $("#agentId").val();
	var orderPersionNum = $("#orderPersonelNum").val();
	var businessType = $("#businessType").val();
	var isCommonOrder = $("#isCommonOrder").val();
	var casttotalPrice = $("#travelerSumPrice_js").text().replace(/\,/g,'');
	var orderno = $("#orderno").val();
	var url = "";
	
	var nvalue = "";
	for(var i= 0;i<casttotalPrice.length;i++){
		
		if(casttotalPrice[i] >=0 || casttotalPrice[i] <=9 || casttotalPrice[i] =='.' || casttotalPrice[i] =='+'){
			nvalue += casttotalPrice[i] ;
		}
		
	}
	var zje = "";
	if(nvalue.indexOf("+") != -1){
		 zje =  nvalue.replace(/\+/g,","); 
	}else{
		zje = nvalue;
	}
	
	
	var payPriceType = "";
	if(payMode == '1' || payMode == '3'){
		if(payMode == '3'){
			payPriceType = 1;
		}else{
			payPriceType = 3;
		}
	}
	
	var amountObj=getAmountCurrencyObjs();//返回多币种id，金额
	var url = g_context_url + "/airticketOrderList/manage/airticketOrderList/1?orderNumOrOrderGroupCode=" + orderno;
	var detailUrl = g_context_url + "/order/manage/airticketOrderDetail?orderId=" + orderId;
	if (payPriceType == "1") {//全款
		url = g_context_url + "/orderPay/pay?orderId="+orderId 
	    + "&payPriceType=" + payPriceType + "&orderType="+orderType
	    +"&agentId=" + agentId + "&orderNum=" + orderno
	    +"&paramCurrencyId=" + amountObj.bzid + "&paramCurrencyPrice=" + zje+"&businessType="+businessType+"&isCommonOrder="+isCommonOrder+"&paramTotalCurrencyId=" + amountObj.bzid + "&paramTotalCurrencyPrice=" +zje + "&orderDetailUrl="+detailUrl;
	} else if (payPriceType == "3") {//订金
		var djje=Number($("#depositamt").val())*Number(orderPersionNum);//订金金额
		var airticketbz = $("#airticketbz").val();//机票币种
		var airticketbzObj = $.parseJSON(airticketbz);//机票币种
		url = g_context_url + "/orderPay/pay?orderId="+orderId 
	    + "&payPriceType=" + payPriceType + "&orderType="+orderType
	    +"&agentId=" + agentId + "&orderNum=" + orderno
	    +"&paramCurrencyId=" + airticketbzObj.currencyId + "&paramCurrencyPrice=" + djje+"&businessType="+businessType+"&isCommonOrder="+isCommonOrder+"&paramTotalCurrencyId=" + amountObj.bzid + "&paramTotalCurrencyPrice=" + zje+ "&orderDetailUrl="+detailUrl ;
	}
	document.location = url;
}

function saveAllSecondInfo(){
	var forms = $("#traveler form");
	var travelerArray = new Array();
	$.each(forms, function(key, value) {
		travelerArray.push(getTravelerFormData(value));
	});
	var specialRemark = $("form.contactTable textarea").val();
	var orderid =$("#orderid").val();
	var dataObj = {"travelerJSON" : JSON.stringify(travelerArray), "specialRemark" : specialRemark, "orderId" : orderid};
	
	$.ajax({
		  type: "POST",
		  url: g_context_url + "/order/airticket/secondStepSave",
		  dataType: "json",
		  data: dataObj,
		  async: false,
		  success:function(msg){
			  
		  }
		});
	
}

//获取游客信息
function getTravelerFormData(form){
	var personType = $("input[name^='personType']:checked", form).val();
	var travelerName = $("input[name='travelerName']", form).val();
	var travelerPinyin = $("input[name='travelerPinyin']", form).val();
	var travelerSex = $("select[name='travelerSex']", form).val();
	var nationality = $("select[name='nationality']", form).val();
	var birthDay = $("input[name='birthDay']", form).val();
	var telephone = $("input[name='telephone']", form).val();
	var passportCode = $("input[name='passportCode']", form).val();
	var validityDate = $("input[name='validityDate']", form).val();
	var remark = $("textarea[name='remark']", form).val();
	var orderid =$("#orderid").val();
	var travelerId = $("input[name='id']", form).val();
	var idCard = $("input[name='idCard']", form).val();
	var srcPrice = $("input[name='payPrice']", form).val();
	var jsPrice = $("input[name='jsPrice']", form).val();
	var lyPrice = $("input[name='lyPrice']", form).val();
	var intermodalId=$("input[name='intermodalId']",form).val();
	var rebatesMoney = $("input[name=rebatesMoney]",form).val();	//返佣费用金额
    var rebatesCurrencyId = $("select[name=rebatesCurrency]",form).val();	//返佣费用币种
	var $jsPrice = $("input[name='jsPrice']",form);
	var intermodalType=0;
	var intermodalId;
	//结算价-多币种对象：{币种ID，金额}
	var costsettlementPrice = new Array();
	if(lyPrice){//联运价格
		var strs=lyPrice.split(",");
		if(strs[0]!="-1"){
			intermodalType=1;
//			intermodalId=strs[0];
		}
	}
	
	//结算价多币种循环拼接字符串
	$.each($jsPrice,function(index,element){
		var singleCurrency = $(element).attr('data-currencyId') + ":" +$(element).val();
		costsettlementPrice.push(singleCurrency);
	});
	
	var obj = { 
			personType : personType,
			name : travelerName,
			nameSpell : travelerPinyin,
			sex : travelerSex,
			nationality : nationality,
			birthDay : birthDay,
			telephone : telephone,
			passportCode : passportCode,
			passportValidity : validityDate,
			remark : remark,
			id:travelerId,
			idCard : idCard,
			orderId : orderid,
			srcPrice : srcPrice,
			jsPrice : jsPrice,
			payPrice : lyPrice,
			rebatesMoney : rebatesMoney,
			rebatesCurrencyId : rebatesCurrencyId,
			intermodalType:intermodalType,
			intermodalId:intermodalId,
			costsettlementPrice : costsettlementPrice
	};
	
	return obj;
}

//add by 蒋扬 获取每个游客对应的返佣信息  2015年8月3日
function getRebateObj(form) {
	
	var rebatesMoney = $("input[name=rebatesMoney]",form).val();	//返佣费用金额
    var rebatesCurrencyId = $("select[name=rebatesCurrency]",form).val();	//返佣费用币种
    
	var obj = {
			rebatesMoney : rebatesMoney,
			rebatesCurrencyId : rebatesCurrencyId
	};
	return obj;
}

//修改总价
function changeTotalPrice() {
	recountIndexTraveler();
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	// 预订人数
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	var travelerTables = $("#traveler form.travelerTable");
	var travelerCount = travelerTables.length;
	var payStatus = $("#payStatus").val();
	if (travelerTables.length == allPersonNum) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}

	if (travelerTables.length > allPersonNum) {
		$("#orderPersonelNum").val(travelerTables.length);
	}

	//所有数据相加
	var travelerPriceTotal = 0;
	//金额单位
	var texamt = Number($("#texamt").val());//税费
	var crj = $("#crj").val();
	var crPrice = Number(adultNum) * parseFloat(crj);
	travelerPriceTotal += Number(crPrice);
	var etj = $("#etj").val();
	var etPrice = Number(childNum) * parseFloat(etj);
	travelerPriceTotal += Number(etPrice);
	var tsj = $("#tsj").val();
	var tsPrice = Number(specialNum) * parseFloat(tsj);
	travelerPriceTotal += Number(tsPrice);
	var texamtPrice = (Number(adultNum)+Number(childNum)+Number(specialNum)) * parseFloat(texamt);
	travelerPriceTotal += Number(texamtPrice);
	var jsarray=new Array();
	var airticketbz = $("#airticketbz").val();//币种
	var airticketbzObj = $.parseJSON(airticketbz);//机票币种
	airticketbzObj.je=travelerPriceTotal;
	jsarray.push(airticketbzObj);

	var lyPrices = $("#traveler input[name='lyPrice']");
	var bzobj= null;
	$.each(lyPrices, function(key, value) {//获取所有联运
		var isly=$(this).closest("form").find("input[name='ydbz2intermodalType']:checked").val();//是否联运
		if ("1"==isly) {
			var tempstr = $(value).val().split(",");
			bzobj=new Object();
			bzobj.currencyId=tempstr[0];
			bzobj.currencyName=tempstr[1];
			bzobj.currencyMark=tempstr[2];
			bzobj.je=tempstr[3];
			jsarray.push(bzobj);
		}
	});
	var jsj=getbzStr(jsarray);
	if(jsj==""){
		$("#travelerSumPrice").text(airticketbzObj.currencyName+"0.00");
	}else{
		$("#travelerSumPrice").text(jsj);
		
	}
	if($("#traveler form").length==0){
		$("#travelerSumPrice_js").text(jsj);
	}
	var topPrice = jsj;
	return topPrice;
}

function dodatePicker() {
	var birthdays = $("#traveler input[name='birthDay']");
	var validityDates = $("#traveler input[name='validityDate']");

	$.each(birthdays, function(key, value) {
		$(value).datepicker( {
			maxDate : new Date()
		});
	});
	$.each(validityDates, function(key, value) {
		$(value).datepicker();
	});
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	var travelerTables = $("#traveler form.travelerTable");
	var travelerCount = travelerTables.length;
	var payStatus = $("#payStatus").val();
	if (travelerTables.length == allPersonNum) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}

	if (travelerTables.length > allPersonNum) {
		$("#orderPersonelNum").val(travelerTables.length);
	}
	//changeTotalPrice();
}

// 订单联系人 验证
function _doValidateorderpersonMesdtail() {
	var flag = true;
	var pot = $("#orderpersonMesdtail").validate( {
		showErrors : function(errorMap, errorList) {
			this.defaultShowErrors();
			outerrorList = outerrorList.concat(errorList);
		}
	}).form();
	if (!pot) {
		flag = false;
	}
	return outerrorList;
}
// 填写游客信息 验证
function _doValidateproductOrderTotal() {
	var flag = true;
	var pot = $("#productOrderTotal").validate( {
		showErrors : function(errorMap, errorList) {
			this.defaultShowErrors();
			outerrorList = outerrorList.concat(errorList);
		}
	}).form();
	if (!pot) {
		flag = false;
	}
	return outerrorList;
}

function _doValidatecontactForm() {
	var forms = $("#contact form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = outerrorList.concat(errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	return outerrorList;
}

function _doValidatetravelerForm() {
	var forms = $("#traveler form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = adderrorList(outerrorList, errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	return outerrorList;
}

function validate() {
	var flag = true;
	var forms = $("#contact form");

	var pot = $("#productOrderTotal").validate( {}).form();
	if (!pot) {
		flag = false;
	}

	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = outerrorList.concat(errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	forms = $("#traveler form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = adderrorList(outerrorList, errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});

	if (isdoSave) {
		createDiv(outerrorList);
	}

	return flag;
}
/**
 * 重新计算游客编号
 * @return
 */
function recountIndexTraveler() {
	var travelerTables = $("#traveler form");
	if (travelerTables.length <= 0) {
		$(".warningtravelerNum").text("暂无游客信息");
	} else {
		$(".warningtravelerNum").text("");
	}
	$.each(travelerTables, function(key, value) {
		var index = key + 1;
		$(value).find(".travelerIndex").text(index);
	});
}

/**
 * 填充新添加游客的默认姓名（游客 + index）
 */
function fillTravelerDefaultName(){
	// 只给拉美途做403功能   
//	if ($("#companyUuid").val() == lameitourUuid) {  // 0569需求,做成全平台
		var travelerTables = $("#traveler form");
		var $thisTraveler = $(travelerTables[travelerTables.length - 1]);
		var tempName = "游客" + travelerTables.length;
		// 填充输入框姓名
		$thisTraveler.find("input[name=travelerName]").val(tempName);
		// 机票报名暂无游客展示姓名的span
//	}
}

function checkFreePosition() {
	// 现在预订人数
	var orderPersonelNum = $("#orderPersonelNum").val();
	// 余位数
	var freePosition = $("#freePosition").val();

	if (Number(orderPersonelNum) <= 0) {
		top.$.jBox.tip('出行人数必须大于零', 'error');
		return false;
	}

	if ((Number(freePosition)  - Number(orderPersonelNum)) < 0) {
		top.$.jBox.tip('余位数不足', 'error');
		return false;
	}
	return true;
}

//显示错误信息
function createDivInTable(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>");
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		//var textTemp = $(valuein.element).parent().text().replace(/[\:\：]/g, '');
		var textTemp = $(valuein.element).prev('label').text().replace(/[\:\：]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text().replace(/[\:\：]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus();
			};
		}(valuein.element));
		textTemp = textTemp.replace(valuein.message, "");
		textTemp = textTemp.replace(/人\s人/g, "人");
		textTemp = textTemp.replace(/儿童\s人/g, "儿童");
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	// letDivCenter(div[0]);
	isdoSave = false;
}

function createDivInDiv(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>");
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		var textTemp = $(valuein.element).parent().find("span").eq(0).text()
				.replace(/[\:\：\*]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text()
					.replace(/[\:\：\*]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus();
			};
		}(valuein.element));
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	// letDivCenter(div[0]);
	isdoSave = false;
}

function letDivCenter(divName) {
	var top = ($(window).height() - $(divName).height());
	var left = ($(window).width() - $(divName).width());
	var left = 0;
	// var scrollTop = $(document).scrollTop();
	// var scrollLeft = $(document).scrollLeft();
	var scrollTop = 0;
	var scrollLeft = 0;
	$(divName).css( {
		'top' : top + scrollTop,
		'left' : left + scrollLeft
	}).show();
}

function adderrorList(outerrorList, errorList) {
	outerrorList = outerrorList.concat(errorList);
	return outerrorList;
}
//汇总所有人数
function changeorderPersonelNum(obj) {
	if($(obj).val() == ""){
		$(obj).val(0);
	}
	$("#orderPersonelNum").val(
			Number($("#orderPersonNumChild").val())
					+ Number(($("#orderPersonNumAdult").val() == "" ? 0 : $("#orderPersonNumAdult").val()))
					+ Number(($("#orderPersonNumSpecial").val() == "" ? 0 : $("#orderPersonNumSpecial").val()))).blur();
}


function countAdult() {//添加的成人数
	var radios = $("#traveler").find("input[name^='personTypeCount'][value=1]:checked");
	return radios.length;
}
function countChild() {//儿童
	var radios =  $("#traveler").find("input[name^='personTypeCount'][value=2]:checked");
	return radios.length;
}
function countSpecial() {//特殊人群
	var radios =  $("#traveler").find("input[name^='personTypeCount'][value=3]:checked");
	return radios.length;
}


//预定保存游客信息
function savePeopleTableData(obj){
	if($(obj).text()=="保存"){
		//游客姓名
		$(obj).parent().parent().find("input[name='travelerName']").each(function(index, obj) {
			if($(this).val() == "") {
				var name = $(this).parent().children("label").text();
				top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
                $(this).focus();
                throw "error！"; 
			}
		});
		/**
		var isRequired = $(obj).parent().parent().find("input[name='passportCode']").attr("class").indexOf("required");
		
		if(isRequired > 0) {
			//护照号
			$(obj).parent().parent().find("input[name='passportCode']").each(function(index, obj) {
				if($(this).val() == "") {
					var name = $(this).parent().children("label").text();
					top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
	                $(this).focus();
	                throw "error！"; 
				}
			});
		}
		
		var isRequired2 = $(obj).parent().parent().find("input[name='validityDate']").attr("class").indexOf("required");
		
		if(isRequired2 > 0 ) {
			//护照有效期
			$(obj).parent().parent().find("input[name='validityDate']").each(function(index, obj) {
				if($(this).val() == "") {
					var name = $(this).parent().children("label").text();
					top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
					$(this).focus();
					throw "error！"; 
				}
			});
		}
		
		//选择联运时身份证号码必填
		$(obj).parent().parent().find("input[name='idCard']").each(function(index, obj) {
			var ly=$(this).closest("form").find("input[name='ydbz2intermodalType']:checked").val();//联运
			if(ly=="1"&&$(this).val() == "") {
				var name = $(this).parent().children("label").text();
				top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
                $(this).focus();
                throw "error！"; 
			}
		});
		**/
		
		//结算价不能为空
		$(obj).parent().parent().find("input[name='jsPrice']").each(function(index, obj) {
			if($(this).val() == "") {
			//	var name = $(this).parent().children("label").text();
				top.$.jBox.tip('请填写*结算价','warning');
                $(this).focus();
                throw "error！"; 
                return;
			}
		});

		var html='';
		$(obj).parent().parent().find("input[name='jsPrice']").each(function(index, obj) {
			if(index!=0){
				html +="+";
			}
			html +=$(this).prev().html();
			html +=formatNum($(this).val());
			
		});
		$(obj).parent().parent().find("span[id='spjg']").html(html);
		//alert(html);
	}
	function formatNum(strNum) {
		if (strNum.length <= 3) {
		return strNum;
		}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
		return strNum;
		}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
		b = b.replace(re, "$1,$2$3");
		}
		 return a + "" + b + "" + c;
		}

	
	var input=$(obj).parent().parent().find("input");
	var textarea=$(obj).parent().parent().find("textarea");
	var selects=$(obj).parent().parent().find("select");
	if($(input).prop("disabled")){
			    $(input).removeAttr("disabled","disabled");
			}else{
				$(input).attr("disabled","disabled");
			}
	if($(textarea).prop("disabled")){
			    $(textarea).removeAttr("disabled","disabled");
			}else{
				$(textarea).attr("disabled","disabled");
			}
	if($(selects).prop("disabled")){
			    $(selects).removeAttr("disabled","disabled");
			}else{
				$(selects).attr("disabled","disabled");
			}
	if($(obj).text()=="保存"){
		    $(obj).text("修改");
			$(obj).parent().prev().hide();
			$(obj).parent().parent().find('.tourist-t-off').css("display","inline");
			$(obj).parent().parent().find('.tourist-t-on').hide();
			$(obj).parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd');
		}else{
			$(obj).text("保存");
			$(obj).parent().prev().show();
			$(obj).parent().parent().find('.tourist-t-off').css("display","none");
			$(obj).parent().parent().find('.tourist-t-on').show();
			$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd');
		}
	
	//删除			
	var deleltecost=$(obj).parent().parent().find("a[name='deleltecost']");
	if($(deleltecost).css("display")=="none") {
			$(deleltecost).show();
				}else{
			$(deleltecost).hide();
				}
	//应用全部			
	var useall=$(obj).parent().parent().find(".yd-total a");
	if($(useall).css("display")=="none") {
		$(useall).show();
	}else{
		$(useall).hide();
	}
}

/*预定第二步联运显示价格*/
function ydbz2lyselect(obj){
	var value=$(obj).find("option:selected").val();
	if(value==0){
		$(obj).parent().find('em').html('');
		$(obj).parent().find('#bzName').html('');
		$(obj).parent().find('#bzMark').html('');
		$(obj).parent().find('input[name="intermodalId"]').attr("value",'');
		$("input[name^='personType']:checked", $(obj).closest("form")).trigger("change");
		top.$.jBox.tip('请重新选择联运方式','warning');
        $(this).focus();
        throw "error！"; 
	}else{
		
		var bzObjs=value.split(",");
		$(obj).parent().find('em').html(bzObjs[4]);
		$(obj).parent().find('#bzName').html(bzObjs[2]);
		$(obj).parent().find('#bzMark').html(bzObjs[3]);
		$(obj).parent().find('input[name="intermodalId"]').attr("value",bzObjs[0]);
		
		$("input[name^='personType']:checked", $(obj).closest("form")).trigger("change");
	}
	
}

//多币种拼接显示
function getbzStr(jsarray){
	var bzpjstr="";
	var json=eval($("#bzJson").val());  
	$.each(json,function(i,element){  
		for(var i=0;i<jsarray.length;i++){
			if(element.currencyId==jsarray[i].currencyId){
				element.je=Number(element.je)+Number(jsarray[i].je);
			}
		}
	});  
	$.each(json,function(i,element){  
		if(element.je!=0)
			bzpjstr+=element.currencyName+element.je.toString().formatNumberMoney('#,##0.00')+"+";
	});  
	return bzpjstr.substring(0,bzpjstr.length-1);
}

/*多币种拼接显示--结算价
@jsarray:币种金额对象
@$parent：Jquery对象，结算价HTML的父节点
*/
function getbzHTML(jsarray,$parent){
	var bzpjhtml="";
	var json=eval($("#bzJson").val());  
	$.each(json,function(index,element){  
		for(var i=0;i<jsarray.length;i++){
			if(element.currencyId==jsarray[i].currencyId){
				//element.je=Number(element.je)+Number(jsarray[i].je);
			 	element.je=(Number(element.je)+Number(jsarray[i].je)).toString().formatNumberMoney('#,##0.00');
			}
		}
	});
	//结算价的币种对象
	var $em = $parent.find("em");
	//保存已有的结算价币种及其文本框，填充或删除多余的币种机器文本框
	$.each(json,function(i,element){  
		if(element.je!=0){
			
			//给可输入文本框赋初始值
			var jtext = jprice;
			var nvalue = "";
			for(var i= 0;i<jtext.length;i++){
				if(jtext[i]>=0 || jtext[i]<=9 || jtext[i] =='.'){
					nvalue += jtext[i];
				}
			}
			var valueIpt = nvalue;//input的value值
			
			$em.each(function(index,obj){
				if(element.currencyName == $(obj).text()){
					valueIpt = $(obj).next().val();
				}
			});
			if("" != bzpjhtml){
				bzpjhtml += ' + ';
			}
			bzpjhtml += '<em style="vertical-align: baseline;">' + element.currencyName + ":" +'</em><input class="ipt-rebates" type="text" maxlength="10" name="jsPrice" value="' + element.je +'" onblur="refundInputs(this,1)" onafterpaste="seetlementKeyUp(this))" onkeyup="seetlementKeyUp(this)" data-currencyId="' + element.currencyId + '" />';
		}
	}); 
	return bzpjhtml;
}

//结算价文本框keyup事件
function seetlementKeyUp(obj){
	//数值验证
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	//订单总结算价计算
	$("#travelerSumPrice_js").html(calculateSumSettlement());
}


//计算订单总结算价
function calculateSumSettlement(){
	//所有的币种对象
	var json=eval($("#bzJson").val());
	//累计结算价
	var xsj='';
	//结算价的所有币种和金额input
	$("[data='newJsPrice'] input[name='jsPrice']").each(function(index, element) {
		//结算价币种的id
        var ipt_currencyId = $(element).attr('data-currencyId');
        //结算价金额
		var ipt_value = $(element).val()=='' ? 0 : $(element).val();
		//处理结算价金额
		var numIpt_value=ipt_value.replace(/,/g,"");
		//单个币种结算价总和
		xsj=Number(xsj)+Number(numIpt_value);
		
		//循环批发商币种匹配相同币种
		$.each(json,function(i,e){
			if(e.currencyId == ipt_currencyId){

				var etj = Number($("#etj").val()) ;//儿童价
				var crj = Number($("#crj").val()) ;//成人价
				var tsj = Number($("#tsj").val()) ;//特殊人群价
				var taxamt = $("#taxamt").val();//税费
				// 预订人数
				var adultNum = $("#orderPersonNumAdult").val();
				var childNum = $("#orderPersonNumChild").val();
				var specialNum = $("#orderPersonNumSpecial").val();
				
				//获取预定总人数
				var shu= Number(adultNum) + Number(childNum ) + Number(specialNum);
				//已经添加游客信息个数
				var show=$("#traveler form.travelerTable").length;
				
				var wxs='';
				
				//如果已经添加游客小于总预定人数
				if(show < shu){
					//循环添加的游客列表	
					$('.travelerTable').each(function(i, element) {
						//去除预定人数中的已添加游客
						if($(element).find('#personTypeinner1').prop('checked')){
							adultNum--;
						}else if($(element).find('#personTypeinner2').prop('checked')){
							childNum--;
						}else if($(element).find('#personTypeinner3').prop('checked')){
							specialNum--;
						}

						//未添加有的的总金额
						var topPrice
						//如果不是游客的单价币种，则未添加游客金额不计算，这个币种金额也不计算
						var currencyMark = $("[name=currencyMark]").val();
						if (e.currencyMark != currencyMark) {
							topPrice = Number(0);//未显示的价格
							xsj = Number(0);
						} else {
							//未添加的游客的总金额
							topPrice = Number(adultNum) * crj + Number(childNum ) * etj + Number(specialNum) * tsj;
						}
						
						if (taxamt != null){
							var totalTaxAMT = taxamt * (Number(adultNum) + Number(childNum) + Number(specialNum));
							wxs = parseFloat(topPrice) + parseFloat(totalTaxAMT);
							
						}	
							
					});

					e.je = Number(wxs) + xsj;
					
				}else{
					e.je = Number(e.je) + Number(numIpt_value);
				}
			}
		
		});
    });
	//结算价拼接字符串
	var HTML_jsPrice = '';
	$.each(json,function(index,element){  
		if(element.je!=0)
			HTML_jsPrice += element.currencyMark+element.je.toString().formatNumberMoney('#,##0.00')+"+";
	}); 

	if('' == HTML_jsPrice){
		if($("#traveler form").length==0){
			var jsPrice=$("#travelerSumPrice").text();
			return jsPrice;
		}else{
			return 0;
		}
	}else{
		return HTML_jsPrice.substring(0,HTML_jsPrice.length-1);
	}
		
}


//结算价的币种ID和币种金额
function getJsCurrencyObj(){
	
	
	var costsettlementPrice = new Array();

	//结算价多币种循环拼接字符串
	$.each($jsPrice,function(index,element){
		var singleCurrency = $(element).attr('data-currencyId') + ":" +$(element).val();
		costsettlementPrice.push(singleCurrency);
	});
	
	
	
	var amountObj=new Object();
	amountObj.bzid=bzidstr;
	amountObj.bzje=bzjestr;
	
	return amountObj;
	
}

//获取所有游客价格、联运价格多币种相加返回币种id，金额
function getAmountCurrencyObjs(){
	var jsarray=new Array();
	// 所有游客价格相加
	// 总共需要计算的人数
	var etj = Number($("#etj").val()) ;//儿童价
	var crj = Number($("#crj").val()) ;//成人价
	var tsj = Number($("#tsj").val()) ;//特殊人群价
	var taxamt = $("#taxamt").val();
	
	// 预订人数
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	var topPrice = Number(adultNum) * crj + Number(childNum ) * etj + Number(specialNum) * tsj;//游客总价
	if(taxamt!=null){
		var totalTaxAMT=taxamt*(Number(adultNum)+Number(childNum)+Number(specialNum))
		topPrice=parseFloat(topPrice)+parseFloat(totalTaxAMT);
		
	}
	
	var airticketbz = $("#airticketbz").val();//币种
	var airticketbzObj = $.parseJSON(airticketbz);//机票币种
	airticketbzObj.je=topPrice;
	jsarray.push(airticketbzObj);
	var lyPrices = $("#traveler input[name='lyPrice']");
	var bzobj= null;	
	$.each(lyPrices, function(key, value) {//获取所有联运价格
		var tempstr = $(value).val().split(",");
		bzobj=new Object();
		bzobj.currencyId=tempstr[0];
		bzobj.currencyName=tempstr[1];
		bzobj.currencyMark=tempstr[2];
		bzobj.je=tempstr[3];
		jsarray.push(bzobj);
	});
	
	var jsonobjs=eval($("#bzJson").val());  
	$.each(jsonobjs,function(i,element){  
		for(var i=0;i<jsarray.length;i++){
			if(element.currencyId==jsarray[i].currencyId){
				element.je=Number(element.je)+Number(jsarray[i].je);
			}
		}
	});
	
	var bzidstr="";
	var bzjestr="";
	$.each(jsonobjs,function(i,element){  
		if(element.je!=0){
			bzidstr+=element.currencyId+",";
			bzjestr+=element.je+",";
		}
	});
	var amountObj=new Object();
	amountObj.bzid=bzidstr;
	amountObj.bzje=bzjestr;
	
	return amountObj;
}

//联运选择
function lyChange(obj){
	//var personType = $("input[name^='personType']:checked", $(obj).closest("form")).val();
	//$(obj).closest("form").find("input[id='personTypeinner'"+personType+"]").trigger("change");
	$("input[name^='personType']:checked", $(obj).closest("form")).trigger("change");
}

//保存关闭订单
function closeOrder(){
	saveData(false);
	document.location = g_context_url + "/order/airticket/activityList";
}

/**
 * 订单修改，第一步“下一步”，readonly所有联系人所有信息，隐藏增删按钮
 * @param errorList
 */
function readOnlyAllContactInfo(){
	//渠道选择只读
	$("#modifyAgentInfo").attr("disabled", true).parent().find("span[name=showAgentName]").remove();
//	$("#modifyAgentInfo").after("<span name='showAgentName'>" + $("#modifyAgentInfo").val() + "</span>");
	//联系人
	$("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		//下拉控件隐藏，追加span(先删)
		var contactSpan = $(element).find("span[name=channelConcat]");
		contactSpan.hide().parent().find("span[name=showName]").remove();
		contactSpan.after("<span name='showName'>" + contactSpan.find("input[name=contactsName]").val() + "</span>");
		//其他input disabled掉		
		$(element).find("input").attr("disabled", true);
		//增删按钮隐藏
//		if($("#allowAddAgentInfo").val() == 0){
			$(element).find("span[name=addContactButton]").hide();
			$(element).find("span[name=delContactButton]").hide();
//		}
    });
}

/**
 * 订单修改，第二步“上一步”，恢复原有读写状态（注意批发商可写可增配置）
 * @param errorList
 */
function back2WritableContactInfo(){
	//渠道选择只读
	$("#modifyAgentInfo").attr("disabled", false).parent().find("span[name=showAgentName]").remove();
	//联系人
	$("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		//下拉控件显示，删除回显span
		var contactSpan = $(element).find("span[name=channelConcat]");
		contactSpan.show().parent().find("span[name=showName]").remove();
//		contactSpan.after("<span name='showName'>" + contactSpan.find("input[name=contactsName]").val() + "</span>");
		if($("#allowAddAgentInfo").val() == 1){
			//所有input 可写
			$(element).find("input").attr("disabled", false);
		} else {
			$(element).find("input[name=remark]").attr("disabled", false);
		}
		//增删按钮隐藏
//		if($("#allowAddAgentInfo").val() == 0){
			$(element).find("span[name=addContactButton]").show();
			$(element).find("span[name=delContactButton]").show();
//		}
		
	});
}
