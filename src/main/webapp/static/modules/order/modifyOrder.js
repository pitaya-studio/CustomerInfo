var flag = false;// 是否要重新保存游客结算价（点击应用全部的时候，结算价不会保存）
var subtracPriceArr = new Array();// 是否计算订单订金与游客
var count = 0;
outerrorList = new Array();
// 需要传递给订单的参数 成本价
var paramCurrencyId = new Array();
var paramCurrenctPrice = new Array();
// 需要传递给订单的参数 结算价
var paramClearCurrencyId = new Array();
var paramClearCurrenctPrice = new Array();
// 计算所有游客费用数组
var travelerTotalPriceArr = new Array();	// 成本价
var travelerTotalClearPriceArr = new Array();// 结算价
var travelerClearDiffArr = new Array();  // 结算价差额
var saveStatusArray = new Array();  // 游客保存状态（0：未保存 1：已保存）（新增、修改 置为0 ； 保存成功置为 1）
var oldOrderPersonNumAdult = 0;
var oldOrderPersonNumChild = 0;
var oldOrderPersonNumSpecial = 0;
var formatNumberArray = new Array();
//拉美途uuid
var lameitourUuid = '7a81a26b77a811e5bc1e000c29cf2586';
//是否
var isYoujia = false;
jQuery(function($) {
	$.fn.datepicker = function(option) {
		var opt = $.extend( {}, option);
		$(this).click(function() {
			WdatePicker(opt);
		});
	}
});

String.prototype.replaceSpecialChars = function(regEx){
	if(!regEx){
		regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
	}
	return this.replace(regEx,'');
}

//112 特殊需求过滤特殊字符
String.prototype.replaceSpecialDemand= function(regEx){
	if (!regEx){
		regEx = /[\“\”\‘\’\"\'\<\>\\]/g;
	}
	return this.replace(regEx, '');
};

// 定义分币种金额对象
function CurrencyMoney(currencyList){
	var obj = {};
	$.each(currencyList, function(key, val) {
		var currencyId = val.id;
		obj[currencyId] = 0.00;
	});
	return obj;
}
//改之前总人数
var orgTotalPersonNum;
//改之前间数
var orgRoomNum;
$(function() {

	var groupCodeVal = $("#groupCodeEle").text();
	if(groupCodeVal.length > 20) {
		groupCodeVal = groupCodeVal.substring(0, 20) + "...";
	}
	$("#groupCodeEle").text(groupCodeVal);

	orgRoomNum = $("#roomNumber").val();
	orgTotalPersonNum = parseInt($("#orderPersonNumAdult").val()) + parseInt($("#orderPersonNumChild").val())
			+ parseInt($("#orderPersonNumSpecial").val());
	if(!isNaN(orgTotalPersonNum)){		
		$("#orgTotalPersonNum").val(orgTotalPersonNum);
	}else{
		$("#orgTotalPersonNum").val(0);	
		orgTotalPersonNum = 0;
	}
	// 各块信息展开与收起
	$(".ydClose").click(function(){
        var obj_this = $(this);
        if(obj_this.attr("class").match("ydExpand")) {
            obj_this.removeClass("ydExpand");
            obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
        } else {
            obj_this.addClass("ydExpand");
            obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
        }
    });


	$("textarea[name=specialDemand]").focusin(function(){
		$(this).removeAttr("placeholder");
	}).focusout(function(){
		$(this).attr("placeholder","最多可输入500字");
	});

	// 执行一些初始化工作
	initSetting();
	initModifyAgentInfo();
	// 选择不同的渠道,修改对应的渠道信息
//	modifyAgentInfo();  //已在orderJs.jsp页面做了onchange事件
	// 下一步 上一步 保存
	modifyOneSecondSave();
	// 游客选择是否需要联运 如果需要初始价格和联运区域
	initIntermodal();
	// 游客选择自备签国家
	selZbqCountry();
	// 改变游客类型
	changePersonType();
	// 游客名称失去焦点时触发
	getTravelerNamePinyin();
	// 发证日期失去焦点时触发
	getPassportValidity();
	// 添加游客点击事件
	addTraveler();
	// 删除游客点击事件
	delTraveler();
	// 删除其他费用时触发
	delOtherCost();
	// 改变住房要求时触发
	changeHotelDemand();
	// 绑定日期控件
	dodatePicker();
	//游客上传资料悬浮时间
	travelerMouseover();

	$("#modifyAgentInfo").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
//		change(this);
		getAllContactsByAgentId(this);
	});
	//
	parsePriceJson(priceJson);
	// 初始加载设置游客信息只读（只能通过点击“修改按钮”）（由于部分信息是后来js生成，故而于此处添加disable）
	disableTravelerInfo();
	
	// 删除多余单击事件
	$(".zksx").unbind("click");
});

/**
 * 设置游客信息只读
 */
function disableTravelerInfo(){
	var travelerForm = $("#traveler form");
	// 初始加载已有游客，需将其置为只读（或于后面第一游客展开冲突）
	for(var j = 0; j < travelerForm.length; j++){
		var saveBtn = $(travelerForm[j]).find("input[name=saveBtn]");
		saveTravelerAfter(saveBtn, travelerForm[j], "save");
		// 设置所有的select效果
		$(travelerForm[j]).find("select").each(function(ind, ele){
			$(ele).disabledselect();
		});
	}
}

/**
 * 初始化设置
 */
function initSetting() {
	//初始化变量
	initVariables();
	// 没有价格的游客类型不能报名
	disableInput4NullPrice4Mod();
	// 计算游客编号 :为每个游客编号
	recountIndexTraveler();
	// 设置游客的显示效果
	//1.将所有游客的同行价（币种和数量）放入了 travelerTotalPriceArr 中 (没有判断该游客是否转团或退团，在下面减去)
	//2.将所有游客的结算价（币种和数量）放入了 travelerTotalClearPriceArr 中
	//3.changePayPriceByCostChange_forDetail
	fn_travelerEffect();
	// 加载页面的时候需要主动去算一下金额，因为如果没有游客，则会出现金额为空情况
	changeTotalPrice();
	// 如果是转团游客，则游客结算价不能计算到订单总额中
	fn_changeGroup();
}

/**
 * 初始化设置诸变量
 */
function initVariables(){
	//如果是优佳的散拼同行价订单，则显示新模板，同行价显示币种符号
	if($("#isForYouJia").val()=="true" && $("#isLoose").val()=="true" && $("#isSettlement").val()=="true"){
		isYoujia = true;
	}
	//TODO
}

/**
 * 当没有价格时，对应类型的游客不能报名
 */
function disableInput4NullPrice4Mod() {
	// 如果游客价格为空，则不允许预定
	var adultPriceSrc = $("[name=settlementAdultPrice]").val();  // 预定时候，存入订单的价格体系
	var childPriceSrc = $("[name=settlementcChildPrice]").val();
	var specialPriceSrc = $("[name=settlementSpecialPrice]").val();
	// 无价格，则对应类型的游客不可以报名 (有价格的解除disable)
	if (!adultPriceSrc) {
		$("#orderPersonNumAdult").attr("disabled", true);
	} else {
		$("#orderPersonNumAdult").attr("disabled", false);
	}
	if (!childPriceSrc) {
		$("#orderPersonNumChild").attr("disabled", true);
	} else {
		$("#orderPersonNumChild").attr("disabled", false);
	}
	if (!specialPriceSrc) {
		$("#orderPersonNumSpecial").attr("disabled", true);
	} else {
		$("#orderPersonNumSpecial").attr("disabled", false);
	}
}

function change(obj){
	var objValue = $(obj).val();
	var valArr = objValue.split(",");
	$( "input[name=orderCompany]").val(valArr[0]);     // 渠道编号
	$( "input[name=orderCompanyName]" ).val(valArr[1]);     // 渠道名称
	$("input[id=contactsName1]").val(valArr[2]);	// 渠道商联系人
	$("input[id=contactsTel1]").val(valArr[3]);	// 渠道联系人电话
	$("input[id=contactsTixedTel1]").val(valArr[4]);	// 固定电话
	$("input[id=contactsAddress1]").val(valArr[5]);	// 渠道地址
	$("input[id=contactsFax1]").val(valArr[6]);		// 传真
	$("input[id=contactsQQ1]").val(valArr[7]);		// QQ
	$("input[id=contactsEmail1]").val(valArr[8]);	// Email
	$("input[id=contactsZipCode1]").val(valArr[9]);	// 渠道邮编
};


function initModifyAgentInfo() {
	var objValue = $("#modifyAgentInfo").val();
	if (objValue) {
		var valArr = objValue.split(",");
		$("input[name=orderCompany]").val(valArr[0]);	// 渠道编号
		$("input[name=orderCompanyName]").val(valArr[1]);	// 渠道名称
		$("input[name=contactsName]").val(valArr[2]);	// 渠道商联系人
		$("input[name=contactsTel]").val(valArr[3]);	// 渠道联系人电话
		$("input[name=contactsTixedTel]").val(valArr[4]);	// 固定电话
		$("input[name=contactsAddress]").val(valArr[5]);	// 渠道地址
		$("input[name=contactsFax]").val(valArr[6]);		// 传真
		$("input[name=contactsQQ]").val(valArr[7]);		// QQ
		$("input[name=contactsEmail]").val(valArr[8]);	// Email
		$("input[name=contactsZipCode]").val(valArr[9]);	// 渠道邮编
	}
}

function modifyOneSecondSave() {
	// 第一步点击下一步
	$("#oneToSecondStepDiv").click(function() {
		
		var oldDifferenceMoney = $("#oldDifferenceMoney").val();
		var newDifferenceMoney = $("#differenceMoney").val();
		
		if ($("#differenceFlag").length > 0 && $("#differenceFlag").val() == "1") {
			if (Number(newDifferenceMoney) < Number(oldDifferenceMoney)) {
				$.jBox.confirm("您所修改的门店结算价差额返还小于已提交的金额是否确认？", "提示", function(v, h, f){
					if (v == "ok") {
						canOneToSecond();
					}
				});
			} else {
				canOneToSecond();
			}
		} else {
			canOneToSecond();
		}
	});

	// 第二步点击上一步
	$("#modSecondToOneStepDiv").click(function() {
		// 游客类型不匹配bug修改：先获取disabledClass的input，然后再恢复赋值，因为下面方法会把disabledClass给去掉
		var $tempType = $(":input[name=personType][class='traveler valid disabledClass']");
		
		//联系人恢复
		back2WritableContactInfo();
		//点击上一步的时候，设置出行人数为可写。
		var payMode = $("#payMode").val();
		if(payMode != "7") {
			$("#orderPersonNumAdult").attr("type", "text").next("span").remove();
			$("#orderPersonNumChild").attr("type", "text").next("span").remove();
			$("#orderPersonNumSpecial").attr("type", "text").next("span").remove();
		}
		$("#oneToSecondStepDiv").show();
		$("#productOrderTotal").undisableContainer();
		$("#orderpersonMesdtail").undisableContainer();
//		$("ul[name='orderpersonMes']").undisableContainer();
		$("ul[name='orderpersonMes'] .ydbz_x").show();
		//$("#manageOrder_m").hide();
		var _closeOrExpand = $(".closeOrExpand").eq(0);
		$(".orderPersonMsg").hide();
		if (_closeOrExpand.hasClass("ydClose")) {
			_closeOrExpand.click();
		}
		$("#manageOrder_new").undisableContainer();
		// 游客信息只读
		$("#manageOrder_new").find("form").each(function(index, element){
			var saveBtn = $(element).find("input[name=saveBtn]");
			saveTravelerAfter(saveBtn, element, "save");
			// 设置所有的select效果
			$(element).find("select").each(function(ind, ele){
				$(ele).disabledselect();
			});
		});
		$("#manageOrder_new [name=deleteTraveler]").hide();
		$("#manageOrder_m").undisableContainer();
		$("#firstDiv").show();
		$("#secondDiv").hide();		
		$tempType.attr("class", "traveler valid disabledClass");
	});

	// 保存 保存并支付
	$("#secondToThirdStepDiv").click(function() {
		var currencyPrice = paramClearCurrenctPrice.join(",");
		if (currencyPrice.indexOf("-") > -1) {
			$.jBox.confirm("结算价出现负数，是否确认保存？", "提示", function(v, h, f){
				if (v == "ok") {
					canSaveModifyOrder();
				}
			});
		} else {
			canSaveModifyOrder();
		}
	});
}

function canOneToSecond() {
	//点击下一步时，要对产品信息中各个类型的人数和游客信息中的人数类型进行校验
	// 报名信息中的人数，即：填写的人数
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	// 每种类型 已添加游客信息的总人数
	var adultTotalNum = $(":radio[name=personType][value=1][checked=checked]").length;
	var childTotalNum = $(":radio[name=personType][value=2][checked=checked]").length;
	var specialTotalNum = $(":radio[name=personType][value=3][checked=checked]").length;
	// 已转团和已退团的游客
	var adultTypes = $("input[class*=traveler][adultflag][checked]").length;
	var childTypes = $("input[class*=traveler][childflag][checked]").length;
	var specialTypes = $("input[class*=traveler][specialflag][checked]").length;
	// 需要减去游客模板中的 人数 （下面9行代码太过离奇，先注掉。使用之后的3行代替）
//	var adultType = $(":input[name='personType'][value='1']").length - 1 - adultTypes;
//	var childType = $(":input[name='personType'][value='2']").length - 1 - childTypes;
//	var specialType = $(":input[name='personType'][value='3']").length - 1 - specialTypes;
//	
//	var tempAdultType = $(":input[name=personType][value=1][class='traveler valid disabledClass']").length;
//	var tempChildType = $(":input[name=personType][value=2][class='traveler valid disabledClass']").length;
//	var tempSpecialType = $(":input[name=personType][value=3][class='traveler valid disabledClass']").length;
//	
//	var adultTypeVal = adultType - tempChildType - tempSpecialType;
//	var childTypeVal = childType - tempAdultType - tempSpecialType;
//	var specialTypeVal = specialType - tempAdultType - tempChildType;
	
	// 需要减去退团转团
	var adultTypeVal = adultTotalNum - adultTypes;
	var childTypeVal = childTotalNum - childTypes;
	var specialTypeVal = specialTotalNum - specialTypes;

	if(adultTypeVal > parseInt(adultNum) || childTypeVal > parseInt(childNum) || specialTypeVal > parseInt(specialNum)) {
		$.jBox.tip("游客类型人数与初始值不匹配请修改！", "erroring");
		return;
	}
	// 验证点击下一步时，填写的出行人数和是否小于下面填写的游客信息， 若小于，则给出提示
	// 游客信息的长度
	var travelerTableSize = $("#traveler form.travelerTable").length;
	// 总得出行人数
	var orderPersonelNum = $("#orderPersonelNum").val();
	if(travelerTableSize > orderPersonelNum) {
		$.jBox.tip("游客信息比出行人数多，请删除游客或增加出行人数！", "erroring");
		return;
	}
	//点击下一步的时候，设置出行人数为只读。
	var payMode = $("#payMode").val();
	if(payMode != "7") {
		var orderPersonNumAdultTemp = $("#orderPersonNumAdult").val();
		$("#orderPersonNumAdult").attr("type", "hidden").next("span").remove();
		$("#orderPersonNumAdult").after("<span>"+orderPersonNumAdultTemp+"</span>");
		var orderPersonNumChildTemp = $("#orderPersonNumChild").val();
		$("#orderPersonNumChild").attr("type", "hidden").next("span").remove();
		$("#orderPersonNumChild").after("<span>"+orderPersonNumChildTemp+"</span>");
		var orderPersonNumSpecialTemp = $("#orderPersonNumSpecial").val();
		$("#orderPersonNumSpecial").attr("type", "hidden").next("span").remove();
		$("#orderPersonNumSpecial").after("<span>"+orderPersonNumSpecialTemp+"</span>");
	}

	// 验证订单联系人
	outerrorList = new Array();
	_doValidateorderpersonMesdtail();
	createDivInDiv(outerrorList);
	if (outerrorList.length > 0) {
		return;
	}
	var travelerTables = $("#traveler form.travelerTable");
	// 验证游客的必填信息
	outerrorList = new Array();
	if(travelerTables.length > 0){
		for(var i = 0;i < travelerTables.length; i++){
			_doValidatetravelerForm(travelerTables[i]);
		}
	}
	createDivInDiv(outerrorList);
	if (outerrorList.length > 0) {
		return;
	}
	//联系人只读
	readOnlyAllContactInfo();
	// 验证是否保存游客信息 如果有未保存的游客需要提示
	// 获取添加的游客form数组
	var notSaveTravelerName = [];
	var notSaveTravelerIndex = [];
	// 获取未保存游客的下标
	for ( var int = 0; int < saveStatusArray.length; int++) {
		if (saveStatusArray[int] == 0) {
			notSaveTravelerIndex.push(int + 1);
			notSaveTravelerName.push($(travelerTables[int]).find("input[name=travelerName]").val());
		}
	}
	var _add_seachcheck = $("#traveler .add_seachcheck");
	for(var i = 0; i < notSaveTravelerIndex.length; i++){
		if (_add_seachcheck.eq(notSaveTravelerIndex[i] - 1).hasClass("boxCloseOnAdd")) {
			_add_seachcheck.eq(notSaveTravelerIndex[i] - 1).click();
		}
	}
	if(notSaveTravelerName.length > 0){
		top.$.jBox.tip('游客姓名为(' + notSaveTravelerName.join(',') + ')的游客未保存，信息已展开请保存', 'error');
		return;
	}

	outerrorList = new Array();
	_doValidatetravelerForm();
	createDivInDiv(outerrorList);
	if (outerrorList.length > 0) {
		return;
	}
	// 设置只读联系人
//	$("#orderpersonMesdtail").disableContainer( {
//		blankText : "—",
//		formatNumber : formatNumberArray
//	});
	// 游客信息
	$("#manageOrder_new").disableContainer( {
		blankText : "—",
		formatNumber : formatNumberArray
	});
	// 特殊需求
	$("#manageOrder_m").disableContainer( {
		blankText : "—",
		formatNumber : formatNumberArray
	});
	$(".downloadFile").removeClass("displayClick");
	// 移除“价格方案”上的displayClick
	$("a[name=jgfa]").removeClass("displayClick");
	// 改变“价格备注”span的style以便换行展示
	$("textarea[name=priceRemark]").each(function(index, element){
		$(element).parent().find("span[class=disabledshowspan]").attr("style","word-wrap:break-word;word-break:break-all;");
	});
	// 费用
	var sums = $(".tourist-right").find("input[name='sum']");
	$.each(sums, function(key, value) {
		var _$span = $(value).next();
		_$span.text(_$span.text());
	});

	// 隐藏按钮，防止多次提交
	$(this).hide();
	$("#firstDiv").hide();
	$("#secondDiv").show();
	$(".orderPersonMsg").hide();
}

var submit_times = 0;
function canSaveModifyOrder() {
	//防止多次提交
	if (submit_times == 0) {
		loading('正在提交，请稍等...');
		submit_times++;
	} else {
		top.$.jBox.info("您已提交，请耐心等待", "警告");
		return false;
	}
	// 联系人
	var contacttables = $("#ordercontact ul[name=orderpersonMes]");
	var ordercontacts = new Array();
	$.each(contacttables, function(key, valueout) {
		var contactinputs = $(valueout).find("input");
		var datacontact = {};
		$.each(contactinputs, function(key, value) {
			var _nametemp = $(value).attr("name");
			datacontact[_nametemp] = $(value).val();
		});
		contactinputs = $(valueout).find("textarea");
		$.each(contactinputs, function(key, value) {
			var _nametemp = $(value).attr("name");
			datacontact[_nametemp] = $(value).val();
		});
		ordercontacts.push(datacontact);
	});
	// 成本价信息
	var costCurrencyId = paramCurrencyId.join(",");
	var costCurrencyPrice = paramCurrenctPrice.join(",");
	// 结算价信息
	var currencyId = paramClearCurrencyId.join(",");
	var currencyPrice = paramClearCurrenctPrice.join(",");
	// 如果是QUAUQ订单且是预报名产生订单，则需要把门店返还金额计算在结算价内
//	if ($("#differenceFlag").length > 0 && $("#differenceFlag").val() == "1") {
//		for (var i=0; i<paramClearCurrencyId.length; i++) {
//			var currencyId = $("input[name=adultCurrencyId]").val();
//            if (paramClearCurrencyId[i] == currencyId) {
//            	var price = $("#differenceMoney").val();
//            	if (!price) {
//            		price = 0;
//            	}
//            	paramClearCurrenctPrice[i] = Number(paramClearCurrenctPrice[i]) + Number(price);
//            	currencyPrice = paramClearCurrenctPrice.join(",");
//            }  
//        }  
//	}
	var totalCurrencyId = currencyId;
	var totalCurrencyPrice = currencyPrice;

	$.ajax({
		type : "POST",
		url : "../../orderCommon/manage/modSaveOrder",
		data : {
			orderCompany : $("#orderCompany").val(),
			orderCompanyName : $("#orderCompanyName").val(),
			orderCompanyNameShow : $("#orderCompanyNameShow").val(),
			productId : $("#productId").val(),
			productGroupId : $("#productGroupId").val(),
			productOrderId : $("#orderid").val(),
			orderPersonNum : $("#orderPersonelNum").val(),
			orderPersonNumChild : $("#orderPersonNumChild").val(),
			orderPersonNumAdult : $("#orderPersonNumAdult").val(),
			orderPersonNumSpecial : $("#orderPersonNumSpecial").val(),
			payStatus : $("#payStatus").val(),
			frontMoney : $("#frontMoney").val(),
			payDepositCurrencyId : $("#payDepositCurrencyId").val(),
			payMode : $("#payMode").val(),
			remainDays : $("#remainDays").val(),
			payDeposit : $("#payDeposit").val(),
			activityKind : $("#activityKind").val(),
			travelerKind : $("#travelerKind").val(),
			orderContactsJSON : JSON.stringify(ordercontacts),
			orgTotalPersonNum : orgTotalPersonNum,
			roomNumber : $("#roomNumber").val(),
			specialDemand : $('#specialDemand').val(),
			specialDemandFileIds : $("#fileIdList").val(),
			preOrderId : $("#preOrderId").val(),
            differenceMoney : $("#differenceMoney").val(),
			currencyId : currencyId,// 结算价
			currenctPrice : currencyPrice,
			costCurrencyId:costCurrencyId,	// 成本价信息
			costCurrencyPrice:costCurrencyPrice,
			rebatesCurrency: $("#rebatesCurrency").val(),
			rebatesMoney:$("#rebatesMoney").val()
		},
		success : function(msg) {
			if (msg.errorMsg) {
				top.$.jBox.tip(msg.errorMsg, 'error');
				submit_times = 0;
				return false;
			} else {
				var payStatus = msg.productOrder.payStatus;
				if (payStatus == 1 || payStatus == 2) {
					var payPriceType;
					if (payStatus == 1){// 订单全款未支付
						payPriceType = 1;
					} else if(payStatus == 2){// 订单订金未支付
						payPriceType = 3;
						// 如果是订金支付传递参数应该为订金
						currencyId = $("#payDepositCurrencyId").val();
						currencyPrice = Number($("#payDeposit").val()) * Number($("#orderPersonelNum").val());
					}
					var param = "orderId="+ msg.productOrder.id
							+ "&orderNum="+ msg.productOrder.orderNum
							+ "&payPriceType="+ payPriceType
							+ "&orderType=" + msg.productOrder.orderStatus
							+ "&businessType=1"
							+ "&isCommonOrder=yes"
							+ "&orderDetailUrl=" + $("#orderDetailUrl").val()
							+ "&agentId=" + $("#agentId").val()
							+ "&paramTotalCurrencyId=" + totalCurrencyId
							+ "&paramTotalCurrencyPrice=" + totalCurrencyPrice
							+ "&paramCurrencyId=" + currencyId
							+ "&paramCurrencyPrice=" + currencyPrice;
					window.location.href = "../../orderPay/pay?"+ param;
				} else {
					window.location.href = "../../orderList/manage/showOrderList/0/" + msg.productOrder.orderStatus + "?orderNumOrGroupCode=" + msg.productOrder.orderNum;
				}
			}
		}
	});
}

/**
 * 游客是否需要联运 如果需要初始价格和联运区域
 */
function initIntermodal(){
	var obj=$('.tourist-t-r');
	$(obj).each(function() {
		var value = $(this).find('input:checked').val();
		if("2" == value){
			$(this).find('span').show();
			setIntermodal($(this));
		} else{
			$(this).find('span').hide();
		}
	});
}

/**
 * 点击自备签国家
 */
function selZbqCountry(){
	$("#traveler").on("click","input[name=zibeiqian]",function(){
		var $this = $(this);
		var $siblingsCkb = $this.parents(".tourist-ckb").children("input[type=checkbox]");
		var thisIndex = $siblingsCkb.index($this);
		var $tips = $this.parents(".ydbz_tit_child").siblings(".zjlx-tips").eq(0);
		var visaTable = $this.parents(".zbqinfo").children(".ydbz_scleft");
		var tr = $("."+$this.val(),visaTable);
		if($this.attr('checked')) {
			if(!$tips.is(":visible")) {
				$tips.show();
			}
			tr.hide();
			$tips.children("ul").eq(thisIndex).show();
			if($this.siblings("input[name='zibeiqian']").not("input:checked").length == 0){
				$tips.nextAll().hide();
			}else{
				$tips.nextAll().show();
			}
		} else {
			$tips.children("ul").eq(thisIndex).hide(500,function() {
				var isshow = 0;
				$tips.children("ul").each(function(index, element) {
					if($(element).is(":visible")){
						isshow++;
					}
				});
				if(0 == isshow) {
					$tips.hide();
					$tips.closest("form.travelerTable").find("input[name='idCard']").removeClass('required');
					$tips.closest("form.travelerTable").find("input[name='idCard']").prev().find("span").hide();
				}
			});
			tr.show();
			$tips.nextAll().show();
		}
	});
}

/**
 * 改变游客类型 成人1 儿童2 特殊人群3
 */
function changePersonType(){
	$("#traveler").delegate("input[name^='personType']","change",function() {
		var travelerForm = $(this).closest("form");
		if ($(this).val() == 2) {
			travelerForm.find("input[name='srcPrice']").val(Number($("#etj").val()==""?0:$("#etj").val()));
			travelerForm.find("input[name='srcPriceCurrency']").val(Number($("#etbz").val()));
		} else if ($(this).val() == 1) {
			travelerForm.find("input[name='srcPrice']").val(Number($("#crj").val()==""?0:$("#crj").val()));
			travelerForm.find("input[name='srcPriceCurrency']").val(Number($("#crbz").val()));
		} else {
			travelerForm.find("input[name='srcPrice']").val(Number($("#tsj").val()==""?0:$("#tsj").val()));
			travelerForm.find("input[name='srcPriceCurrency']").val(Number($("#tsbz").val()));
		}
		// 置空对应的结算价差额
		resetPriceDiff(travelerForm);
		changePayPriceByCostChange(travelerForm);
		changeClearPriceByInputChange(travelerForm);
	});
}

/**
 * 切换游客类型时，置空对应的结算价差额
 * @param $travelerForm
 */
function resetPriceDiff($travelerForm){
	var travelerIndex = $travelerForm.find(".travelerIndex").text();
	// 组织最新的差额
	var diffArr = new Array();
	var diffObject = new Object();
	diffObject.price = Number(0);
	diffObject.currencyId = $travelerForm.find("input[name='srcPriceCurrency']").val();
	diffArr.push(diffObject);
	travelerClearDiffArr[travelerIndex-1].diffPrice = diffArr;
}

/**
 * 获取游客姓名拼音
 */
function getTravelerNamePinyin(){
	$("#traveler").delegate("input[name='travelerName']","blur", function() {
		var srcname = $(this).val();
		if ($.trim(srcname).length <= 0) {
			return false;
		}
		var pinying = $(this).closest("form").find("input[name='travelerPinyin']").eq(0);
		var tName = $(this).closest("form").find("span[name='tName']").eq(0).html(srcname);
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
}

function getPassportValidity(){
	$("#traveler").delegate("input[name='issuePlace']", "blur", function() {
		if($("#isForYouJia").val()=="true"){
			// 有效日期
			var _$validityDate = $(this).closest(".tourist-info1").find("input[name='passportValidity']");
			var minDate = $(this).val();
			var curDate = new Date();
			
			// 护照有效期默认为发证日期加10年，然后减一年
			if(minDate != "") {
				var lastDate = new Date(minDate.split('/')[2] + '/' + minDate.split('/')[1] + '/' + minDate.split('/')[0]);
				lastDate.setFullYear(lastDate.getFullYear() + 10);
				lastDate.setDate(lastDate.getDate() - 1);
				$(_$validityDate).val(lastDate.getDate() + "/" + (lastDate.getMonth() + 1) + "/" + lastDate.getFullYear());
			}
			
			if (new Date(minDate) < curDate) {
				minDate = curDate;
			}
			
			_$validityDate.unbind();
			_$validityDate.datepicker( {
				minDate : minDate,
				dateFmt : 'dd/MM/yyyy'
			});
		} else {			
			// 有效日期
			var _$validityDate = $(this).closest(".tourist-info1").find("input[name='passportValidity']");
			var minDate = $(this).val();
			var curDate = new Date();
			
			// 护照有效期默认为发证日期加10年，然后减一年
			if(minDate != "") {
				var lastDate = new Date(new Date(minDate.replace(/-/g,'/')));
				lastDate.setFullYear(lastDate.getFullYear() + 10);
				lastDate.setDate(lastDate.getDate() - 1);
				$(_$validityDate).val(lastDate.getFullYear() + "-" + (lastDate.getMonth() + 1) + "-" + lastDate.getDate());
			}
			
			if (new Date(minDate) < curDate) {
				minDate = curDate;
			}
			
			_$validityDate.unbind();
			_$validityDate.datepicker( {
				minDate : minDate
			});
		}
	});
}

/**
 * 添加游客
 */
function addTraveler(){
	$("#addTraveler").click(function() {
		var $table = $("#travelerTemplate").children();
		var _travelerForm = $table.clone().addClass("travelerTable");
		$("#traveler").append(_travelerForm);
		// 默认添加游客信息时，判断什么游客类型
		var selFlag = false;
		var selJsPrice = 0;
		var selSrcPriceCurrency;
		var selSrcPriceCurrencyMark;
		var selSrcPriceCurrencyName;
		var personType = 0;
		if ($("#orderPersonNumAdult").val() > countAdult()){
			selFlag = true;
			selJsPrice = $('#crj').val()==""?0:$("#crj").val();
			selSrcPriceCurrency = $('#crbz').val();
			selSrcPriceCurrencyMark  = $('#crbzm').val();
			selSrcPriceCurrencyName = $('#crbmc').val();
			personType = 0;
		}
		if(!selFlag){
			if ($("#orderPersonNumChild").val() > countChild()){
				selFlag = true;
				selJsPrice = $('#etj').val()==""?0:$("#etj").val();
				selSrcPriceCurrency = $('#etbz').val();
				selSrcPriceCurrencyMark  = $('#etbzm').val();
				selSrcPriceCurrencyName = $('#etbmc').val();
				personType = 1;
			}
		}
		if(!selFlag){
			if ($("#orderPersonNumSpecial").val() > countSpecial()){
				selFlag = true;
				selJsPrice = $('#tsj').val()==""?0:$('#tsj').val();
				selSrcPriceCurrency = $('#tsbz').val();
				selSrcPriceCurrencyMark  = $('#tsbzm').val();
				selSrcPriceCurrencyName = $('#tsbmc').val();
				personType = 2;
			}
		}
		// 填充人员类型
		$("input[name=personType]",_travelerForm)[personType].checked = true;
		
		// for 109 优惠
		var isForYouJia = $("[name='isForYouJia']").val();
		var activityKind = $("#activityKind").val();
		var isPeer = $("#priceType").val();

		if(isYoujia) {
			if(personType == 0) {	// 成人
				$(_travelerForm).find(".adultDiscount").addClass("show").show();
				$(_travelerForm).find(".childDiscount").removeClass("show").hide();
				$(_travelerForm).find(".specialDiscount").removeClass("show").hide();
			} else if (personType == 1) {	// 儿童
				$(_travelerForm).find(".childDiscount").addClass("show").show();
				$(_travelerForm).find(".adultDiscount").removeClass("show").hide();
				$(_travelerForm).find(".specialDiscount").removeClass("show").hide();
			} else if(personType == 2) {	// 特殊人
				$(_travelerForm).find(".specialDiscount").addClass("show").show();
				$(_travelerForm).find(".adultDiscount").removeClass("show").hide();
				$(_travelerForm).find(".childDiscount").removeClass("show").hide();
			}
		}
		// 填充游客内部显示的结算价
		if(isYoujia) {
			var innerHtmlValue = "<span name='inputClearPriceDiv'>" + selSrcPriceCurrencyMark + selJsPrice 
							+ "<input type='hidden' name='inputClearPrice' maxlength='9' style='display:none;' value='" + selJsPrice + "'/>"
							+ "<input type='hidden' name='inputCurreyId' alt='" + selSrcPriceCurrencyName+"' value='" + selSrcPriceCurrency + "' /></span>";
			$("span[name=clearPrice]",_travelerForm).html(innerHtmlValue);
		} else {
			var innerHtmlValue = "<span name='inputClearPriceDiv'>" + selSrcPriceCurrencyName + ":" 
							+ "<input type='hidden' name='inputCurreyId' alt='"+selSrcPriceCurrencyName+"' value='"+selSrcPriceCurrency+"' />" 
							+ "<input type='hidden' name='inputClearPrice' value='" + selJsPrice + "'/>" 
							+ selJsPrice + "</span></BR>";
			$("span[name=clearPrice]",_travelerForm).html(innerHtmlValue);
		}
		// 填充游客内部同行结算价
		if(isYoujia) {
			$("span[name=settlementClearPrice]",_travelerForm).html(selSrcPriceCurrencyMark + selJsPrice);  //同行结算价
			$("span[name=totalDiscount]",_travelerForm).html(0);  //总优惠价
		}
		// 填充游客内部显示的同行价
		if(isYoujia) {
			$("span[name=settlementPrice]",_travelerForm).html(selSrcPriceCurrencyMark + selJsPrice.toString().formatNumberMoney('#,##0.00'));  //同行价
		} else {
			$("span[name=innerJsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));  //成本价
		}
		// 填充其他费用总计
		if(isYoujia){
			$("span[name=totalOtherCostPrice]",_travelerForm).html(selSrcPriceCurrencyMark + "0".toString().formatNumberMoney('#,##0.00'));  //其他费用总计
		}
		// 给原始优惠定额赋值
		if(isYoujia){
			var orgDiscountPrice = Number($(_travelerForm).find(".discount").find(".show").find(".activityDiscountAmount").text());
			$("input[name=orgDiscountPrice]",traveler).val(orgDiscountPrice);
		}
		
		// 存放不同类型游客的同行价
		$("input[name=srcPrice]",_travelerForm).val(selJsPrice);
		// 存放不同类型游客的同行价币种
		$("input[name=srcPriceCurrency]",_travelerForm).val(selSrcPriceCurrency);
		// 如果是优佳的散拼同行价订单，则显示新模板，同行价就是同行价
		if (isYoujia) {
			$("span[name=jsPrice]",_travelerForm).html(getSettlementPrice(_travelerForm)[0].str_name);
		} else {
			// 填充单个游客信息收起显示的成本价格
			$("span[name=jsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		}
		// 新增游客的结算价差额
		initTravelerDiffPrice(_travelerForm);
		// 初始化游客保存状态（新增为0，未保存）
		initSaveStatus(_travelerForm, "add");
		// 组装每种游客同行价的对象
		var priceObj = new Object();
		priceObj.currencyId = selSrcPriceCurrency;
		priceObj.price =  selJsPrice;
		var priceObjArr = new Array();
		priceObjArr.push(priceObj);
		var travelerJsPrice = new Object();
		travelerJsPrice.jsPrice = priceObjArr;
		travelerTotalPriceArr.push(travelerJsPrice);
		var travelerClearPrice = new Object();
		travelerClearPrice.travelerClearPrice = priceObjArr;
		travelerTotalClearPriceArr.push(travelerClearPrice);
		// 绑定日期控件
		dodatePicker();
		// 计算游客编号
		recountIndexTraveler();
		// 填充游客默认姓名 （“游客  + index”）
		fillTravelerDefaultName();
		changeTotalPrice();
		//游客上传资料悬浮时间
		travelerMouseover();
	});
}

/**
 * 初始化游客保存状态
 * @param traveler
 */
function initSaveStatus(_travelerForm, type) {
	if (type == "add") {
		saveStatusArray.push(0);
	} else {
		saveStatusArray.push(1);
	}
}

/**
 * 删除游客时触发
 */
function delTraveler(){
	$("#traveler").delegate("a[name='deleteTraveler']", "click", function() {
		var $this = $(this);
		var travelerId = $(this).closest('.travelerTable').find("input[name='travelerId']").val();
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				if (travelerId != undefined && travelerId != '') {
					$.ajax( {
						type : "POST",
						url : "../../traveler/manage/deleteTraveler",
						data : {
							travelerId : travelerId
						},
						success : function(msg) {
							if(msg == 'ok'){
								deleteTravelerAfter($this.closest('form'));
								top.$.jBox.tip('删除成功', 'success');
								// 成功删除游客后移除对应的价格差额
								removeDiffPrice($this.closest('form'));
								removeSaveStatus($this.closest('form'));
							}else{
								top.$.jBox.tip('删除失败', 'error');
							}
						}
					});
				}else{
					deleteTravelerAfter($this.closest('form'));
					removeSaveStatus($this.closest('form'));
				}
			}
		});
	});
}

/**
 * 删除游客成功后 移除对应的保存状态
 * @param $traveler
 */
function removeSaveStatus($traveler){
	var travelerIndex = $traveler.find(".travelerIndex").text();
	saveStatusArray.splice(travelerIndex-1, 1);
}

/**
 * 成功删除游客后移除对应的价格差额
 * @param $traveler
 */
function removeDiffPrice($traveler){
	var travelerIndex = $traveler.find(".travelerIndex").text();
	// 删除之
	travelerClearDiffArr.splice(travelerIndex-1, 1);
}

//保存游客信息
function saveTraveler(obj, travelerForm, orderType) {
	
	$inputClearPrices = $("input[name=inputClearPrice]", travelerForm);
	
	if ($inputClearPrices.length > 0) {
		var flag = true;
		// 如果金额出现负值则提示
		$("input[name=inputClearPrice]", travelerForm).each(function(index, o) {
			if ($(this).val().indexOf("-") != -1) {
				flag = false;
				var travelerName = $("input[name=travelerName]", travelerForm).val();
				$.jBox.confirm(travelerName + "游客结算价出现负数是否确认保存", "提示", function(v, h, f){
					if (v == "ok") {
						canSaveTraveler(obj, travelerForm, orderType);
					}
				});
			}
			if (flag && index == $("input[name=inputClearPrice]", travelerForm).length -1 ) {
				canSaveTraveler(obj, travelerForm, orderType);
			}
		});
	} else {
		top.$.jBox.tip('游客结算价不能为空', 'error');
	}
}

function canSaveTraveler(obj, travelerForm, orderType) {
	if($("#isForYouJia").val()=="true"){		
		// 校验申请办签信息
		var result = validateVisaInfo(obj);
		if(!result) {
			return;
		}
	}
	//验证表单必填项
	outerrorList = new Array();
	_doValidatetravelerForm(travelerForm);
	createDivInDiv(outerrorList);
	if(outerrorList.length > 0) {
		return;
	}
	//验证游客类型
	if(!validutePersonType(travelerForm)){
		return;
	}
	//验证身份证必填 如果需要联运和自备签身份证信息必填
	var intermodalType = $("input[name=travelerIntermodalType]:checked",travelerForm).val();
	var idCard = $("input[name=idCard]",travelerForm).val();
	if(intermodalType == "1"){
		if(idCard==""){
			top.$.jBox.tip('需要联运时，身份证信息必填', 'error');
			return false;
		}
	}
	//获取自备签签证国家及自备签有效期
	var zbqCkb = $(".tourist-ckb input[type=checkbox]",travelerForm);
	var zbqCountryArr = [];
	for(var i=0; i < zbqCkb.length; i++){
		var chk = zbqCkb[i];
		if(chk.checked){
			zbqCountryArr.push(chk.value);
		}
	}

	//处理
	var visaTable = $(".zbqinfo .ydbz_scleft .table-visa",travelerForm);
	var trVisaInfo = $("tr[name=visainfo]",visaTable);
	var zbqVisaDate = $("input[name=zbqVisaDate]",travelerForm);
	var datavisa = [];	
	
	//如果是优佳的散拼同行价订单，则显示新模板,无自备签期限
	if ($("#isForYouJia").val()=="true") {
		for(var i = 0; i < trVisaInfo.length; i++){
			var trClassName = trVisaInfo[i].className;
			var td = $(trVisaInfo[i]).children('td');
			var visaInfo = {};
			visaInfo.orgVisaId = $("input[name=orgVisaId]",trVisaInfo[i]).val();
			visaInfo.orgVisaCountryId = $("input[name=orgVisaCountryId]",trVisaInfo[i]).val();
			visaInfo.orgVisaType = $("input[name=orgVisaType]",trVisaInfo[i]).val();
			visaInfo.applyCountryId =$(trVisaInfo[i]).find("[name='countrySelect']").val();
			visaInfo.manorId = $("select[name=manor]",td.eq(1)).val();
			visaInfo.visaTypeId = $("select[name=visaType]",td.eq(2)).val();
			visaInfo.groupOpenDate = td.eq(3).text();
			visaInfo.contractDate = $("input[type=text]",td.eq(4)).val();
			datavisa.push(visaInfo);
		}		
	} else {		
		for(var i = 0; i < trVisaInfo.length; i++){
			var trClassName = trVisaInfo[i].className;
			var td = $(trVisaInfo[i]).children('td');
			var visaInfo = {};
			visaInfo.applyCountryId = $("input[type=hidden]",td.eq(0)).val();
			if(zbqCountryArr.join(',').indexOf(trClassName) < 0){
				visaInfo.manorId = $("select[name=manor]",td.eq(1)).val();
				visaInfo.visaTypeId = $("select[name=visaType]",td.eq(2)).val();
				visaInfo.groupOpenDate = $("input[type=text]",td.eq(3)).val();
				visaInfo.contractDate = $("input[type=text]",td.eq(4)).val();
				visaInfo.visaDate = '';
				visaInfo.zbqType = 0;
			}else{
				visaInfo.manorId = '';
				visaInfo.visaTypeId = '';
				visaInfo.groupOpenDate = td.eq(3).text();
				visaInfo.contractDate = '';
				visaInfo.visaDate = zbqVisaDate[i].value;
				visaInfo.zbqType = 1;
			}
			datavisa.push(visaInfo);
		}
	}
	
	//游客签证附件
	//游客费用
	var datacost = new Array();
	var t_id = $('input[name=travelerId]',$(travelerForm)).val();
	setOtherCostObjToArr($(travelerForm), datacost, t_id);

	var cbCurrencyId = paramCurrencyId.join(",");
	var cbCurrencyPrice = paramCurrenctPrice.join(",");
	
	// 结算价信息
	var jsCurrencyId = paramClearCurrencyId.join(",");
	var jsCurrencyPrice = paramClearCurrenctPrice.join(",");

	var travelerCost = new Array();
	//保存游客其他费用
	if (flag) {
		var travelerForms = $("form[name=travelerForm]");
		$.each(travelerForms, function(index, obj) {
			var travelerId = $('input[name=travelerId]',$(this)).val();
			if (travelerId && travelerId != '' && travelerId != t_id) {
				setOtherCostObjToArr($(this), travelerCost, travelerId);
			}
		});
	}
	$(obj).attr("disabled","disabled");

	var rebatesMoney = $("input[name=rebatesMoney]",travelerForm).val();	//返佣费用金额
	var rebatesCurrencyId = $("select[name=rebatesCurrency]",travelerForm).val();	//返佣费用币种
	var orderPersonelNum = $("#orderPersonelNum").val();
	var roomNumber = $("#roomNumber").val();
	var isForYouJia = false;
	if (isYoujia) {
		isForYouJia = true;
	}
	
	var url;
	if ($("#isForYouJia").val()=="true") {
		url = "../../traveler/manage/saveTraveler4YouJiaModify";
	} else {
		url = "../../traveler/manage/save";
	}
	
	$.ajax({
		type : "POST",
		url : url,
		data : {
			travelerInfo: JSON.stringify($(travelerForm).serializeObject()),
			travelerCost: JSON.stringify(travelerCost),
			costs: JSON.stringify(datacost),
			visas: JSON.stringify(datavisa),
			orderType: orderType,
			payPrice: JSON.stringify(getTravelerClearPrice($(travelerForm))),	//结算价
			costPrice: JSON.stringify(getTravelerPayPrice($(travelerForm))),	//成本价
			orderModifyFlag : "true",
			isForYouJia : isForYouJia,
			cbCurrencyId : cbCurrencyId,
			cbCurrencyPrice : cbCurrencyPrice,
			jsCurrencyId : jsCurrencyId,
			jsCurrencyPrice : jsCurrencyPrice,
			totalCharge : $("#totalCharge").val(),
			rebatesMoney: rebatesMoney,
			rebatesCurrencyId : rebatesCurrencyId,
			orderPersonelNum : orderPersonelNum,
			roomNumber : roomNumber,
			orderId : $("#orderid").val(),
			orderNum : $("#orderNum").val(),
			productId : $("#productId").val(),
			productGroupId : $("#productGroupId").val(),
			activityKind : $("#activityKind").val(),
			agentId : $("#agentId").val(),
			groupHandleId : $("#groupHandleId").val(),
			orderPersonNumChild : $("#orderPersonNumChild").val(),
			orderPersonNumAdult : $("#orderPersonNumAdult").val(),
			orderPersonNumSpecial : $("#orderPersonNumSpecial").val()
		},
		success : function(data) {
			// 回团日期非空校验
			if(data.result=="error"){
				$.jBox.tip(data.error);
				$(obj).removeAttr("disabled");
				return;
			}
			if($("#isForYouJia").val()=="true"){
				if (data!="") {
					if (data.traveler.id) {
						//设置表单ID
						$('input[name=travelerId]',travelerForm).val(data.traveler.id);
						saveTravelerAfter(obj,travelerForm,"save");
						$.jBox.tip("游客保存成功");
						setOneTravelerClearPriceShow(travelerForm,1);
						// 保存成功后更新爆更新保存状态
						var travelerIndex = $(travelerForm).find(".travelerIndex").text();
						saveStatusArray[travelerIndex-1] = 1;
					}
					if(data.msg == '1') {
						$.jBox.tip("游客总数已与人数相同");
					}
					if(data.groupHandleId != "") {
						$("#groupHandleId").val(data.groupHandleId);
					}
					if(data.groupHandleVisaIdStr != "") {
						var visaIds = data.groupHandleVisaIdStr.split(",");
						$("input[name=orgVisaId][value='']",travelerForm).each(function(index, element){
							$(element).val(visaIds[index]);
						});
					}
					if(data.orgVisaTypeStr != "") {
						var visaTypes = data.orgVisaTypeStr.split(",");
						$("input[name=orgVisaType][value='']",travelerForm).each(function(index, element){
							$(element).val(visaTypes[index]);
						});
					}
					// 清空待删除文件隐藏域tobeDelFiles
					$("input[name=tobeDelFiles]",travelerForm).val("");
				} else {
					$.jBox.tip("游客保存失败");
				}
				$(obj).removeAttr("disabled");
			} else {
				if (data!="") {
					if (data.id) {
						//设置表单ID
						$('input[name=travelerId]',travelerForm).val(data.id);
						saveTravelerAfter(obj,travelerForm,"save");
						$.jBox.tip("游客保存成功");
						setOneTravelerClearPriceShow(travelerForm,1);
						// 保存成功后更新爆更新保存状态
						var travelerIndex = $(travelerForm).find(".travelerIndex").text();
						saveStatusArray[travelerIndex-1] = 1;
					} else {
						$.jBox.tip("游客总数已与人数相同");
					}
				} else {
					$.jBox.tip("游客保存失败");
				}
				$(obj).removeAttr("disabled");
			}
		},
		error: function(e){
			$.jBox.tip("保存失败"+e);
			$(obj).removeAttr("disabled");
		}
	});
}

/**
 * 验证签证信息
 */
function validateVisaInfo(obj) {
	var $travelerForm = $(obj).parents("form[name='travelerForm']");
	var isSelect = $travelerForm.find("[name='countrySelect']").is("select");
	if(!isSelect) {
		return true;
	}
	var isContinue = true;
	$travelerForm.find("[name='countrySelect']").each(function() {
		var countrySelectVal = $(this).val();
		var manorVal = $(this).parents("tr").find("[name='manor']").val();
		var visaTypeVal = $(this).parents("tr").find("[name='visaType']").val();
		if(countrySelectVal != '-1') {
			if(manorVal == '-1' || visaTypeVal == '-1') {
				$.jBox.tip('请将申请办签信息补充完整!', 'warning');
				isContinue = false;
			}
		} else if (countrySelectVal == '-1') {
			$.jBox.tip("申请国家为必填项!");
			$(this).parents("tr").find("[name='manor']").val('-1');
			$(this).parents("tr").find("[name='visaType']").val('-1');
			isContinue = false;
		}
	});
	return isContinue;
}

//添加147
function addQz(obj) {
	var $currentUl = $(obj).parents('tbody[id=qztemplate]');
	var $newUl = $currentUl.clone();
	$newUl.find("#adddel").empty();
	$newUl.find("input,select").each(function(){
		this.value='';
	});
	$newUl.find("#adddel").append('<a class="add"  href="javascript:void(0)" onclick="addQz(this)">+</a>' +
			'<a class="del" href="javascript:void(0)" onclick="delQz(this)">-</a>');
	$currentUl.parents("table[class=table-visa]").append($newUl);
}

//删除147
function delQz(obj) {
	$(obj).parent().parent().parent().remove();
}

/**
 * 删除游客其他费用
 */
function delOtherCost(){
	$("#traveler").delegate("a[name='deleltecost']", "click", function() {
		var $this = $(this);
		var costId = $this.closest(".cost").find("input[name='id']").val();
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				if (costId != null && costId != "" && costId != undefined) {
					$.ajax( {
						type : "POST",
						url : "../../orderCommon/manage/deleteCost",
						data : {
							costId : costId
						},
						success : function(msg) {
							top.$.jBox.tip('删除成功', 'success');
							$this.closest(".cost").remove();
							changePayPriceByCostChange($this.closest("form"));
							changeClearPriceByInputChange($this.closest("form"));	// add by zhangcl

						}
					});
				} else {
					var travelerForm = $this.closest("form");
					$this.closest(".cost").remove();
					changePayPriceByCostChange(travelerForm);
					changeClearPriceByInputChange(travelerForm);	// add by zhangcl
				}
			} else if (v == 'cancel') {
			}
		});
	});
}

/**
 * 改变住房要求
 */
function changeHotelDemand(){
	$("#traveler").delegate("select[name='hotelDemand']", "change", function() {
		if ($(this).val() == 1) {
			$(this).closest("form").find("input[name='singleDiff']").val($("#singleDiff").val());
			$(this).closest("form").find("input[name='singleDiff']").prev().text($("#singleDiff").val().toString().formatNumberMoney('#,##0.00'));
		} else {
			$(this).closest("form").find("input[name='singleDiff']").val(0);
			$(this).closest("form").find("input[name='singleDiff']").prev().text(0);
		}
		changePayPriceByCostChange($(this).closest("form"));
		changeClearPriceByInputChange($(this).closest("form"));  // add by zhangcl
	});
}

/**
 * 设置生日控件最大日期为当前系统日期
 */
function dodatePicker() {
	var birthdays = $("#traveler input[name='birthDay']");
	$.each(birthdays, function(key, value) {
		$(value).datepicker( {
			maxDate : new Date()
		});
	});
	var issuePlaces = $("#traveler input[name='issuePlace']");
	$.each(issuePlaces, function(key, value) {
		$(value).datepicker({
			maxDate : new Date()
		});
	});
	var passportValiditys = $("#traveler input[name='passportValidity']");
	$.each(passportValiditys, function(key, value) {
		$(value).datepicker( {
			minDate : new Date()
		});
	});
}


// --------------------------------------------------------------------------------------------------------------------------

// 设置游客的显示效果
function fn_travelerEffect(){
	// 处理除了第一个游客其他游客内容收缩
	var _add_seachcheck = $("#traveler .add_seachcheck");
	for(var i = 0; i< _add_seachcheck.length;i++){
		if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
			_add_seachcheck.eq(i).click();
		}
	}
	var travelerForm = $("#traveler form");
	// 成本价
	for(var j = 0; j < travelerForm.length; j++){
		// 如果游客已退团或转团，则成本价不计入订单成本价
		var changeGroupFlag = $("input[name=changeGroupFlag]", travelerForm[j]).val();
		if(changeGroupFlag == '3' || changeGroupFlag == '5') {
			// continue;
		}
		var jsPriceJson = $("input[name=jsPriceJson]",travelerForm[j]).val();
		var priceObjArr = new Array();
		$.each($.parseJSON(jsPriceJson), function(key, value){
			priceObjArr.push(value);
		});
		var travelerJsPrice = new Object();
		travelerJsPrice.jsPrice = priceObjArr;
		travelerTotalPriceArr.push(travelerJsPrice);
	}
	// 结算价
	for(var j = 0; j < travelerForm.length; j++){
		var jsPriceJson = $("input[name=travelerClearPriceJson]",travelerForm[j]).val();
		var priceObjArr = new Array();
		$.each($.parseJSON(jsPriceJson), function(key, value){
			priceObjArr.push(value);
		});
		var travelerClearPrice = new Object();
		travelerClearPrice.travelerClearPrice = priceObjArr;
		travelerTotalClearPriceArr.push(travelerClearPrice);
	}
	// 结算价差额
	for(var j = 0; j < travelerForm.length; j++){
		initTravelerDiffPrice($(travelerForm[j]));
	}
	// 保存状态
	for(var j = 0; j < travelerForm.length; j++){
		initSaveStatus($(travelerForm[j]), "old");
	}
	for(var j = 0; j < travelerForm.length; j++){
		changePayPriceByCostChange_forDetail($(travelerForm[j]),2);	// add by zhangcl
	}
}

// 如果是转团或退团游客则把form名称给置空并把保存按钮给屏蔽
function fn_changeGroup() {
	$(".travelerTable").each(function(index, obj) {
		var changeGroupFlag = $("input[name=changeGroupFlag]",this).val();
		if(changeGroupFlag == '3' || changeGroupFlag == '5') {
			$(this).attr("name",'');
			$(".rightBtn",this).hide();
			$(this).removeClass();
			// 因为上面执行不如下面方法快，而下面方法会控制 添加按钮 是否显示 所有需要再执行一遍如下方法
			changeTotalPrice();
		}
	});
}

function setIntermodal(selObj){
	var value=selObj.find("option:selected").val();
	var text=selObj.find("option:selected").text();
	var intermodalId = selObj.find("option:selected").attr("intermodalId");
	var priceCurrency = selObj.find("option:selected").attr("priceCurrency");
	selObj.parent().find('label[name=intermodalPrice]').html(value);
	selObj.parent().find('input[name=intermodalGroupPart]').val(text);
	selObj.parent().find('input[name=intermodalId]').val(intermodalId);
	selObj.parent().find('label[name=priceCurrency]').html(priceCurrency);
	changePayPriceByCostChange(selObj.closest("form"));
	changeClearPriceByInputChange(selObj.closest("form"));
}
// -------------------------------------------------------------------------------------------------------------------------
// add by yunpeng.zhang
// 功能：验证了特殊人群最高限制，修改数量不能小于当前数量，余位不足和散拼切位不足等。//add by jyang 增加散拼游轮直客价时对空值0值的校验
function checkFreePosition2(obj, num) {
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	if(adultNum == '') {
		$("#orderPersonNumAdult").val('0')
	}
	if(childNum == '') {
		$("#orderPersonNumChild").val('0')
	}
	if(specialNum == '') {
		$("#orderPersonNumSpecial").val('0')
	}

	var priceType = $("#priceType").val();
	var groupType = $("#groupType").val();
	var idVal = $(obj).attr("id");// 当前标签 id
	var val = $(obj).val();// 当前标签值
	//add by yang.jiang
	if((groupType =="2" || groupType =="10") && priceType == 1 && parseInt(val) > 0){
		if(idVal.indexOf("Adult") > 0 ){
			if(groupType =="2"){
				if($("#crj").val() == undefined || $("#crj").val() == null || $("#crj").val() == ""){
					$.jBox.tip("成人直客价为空不能报名！","error");
					$(obj).val(0);
				}else if(Number($("#crj").val() == 0)){
					$.jBox.confirm("成人直客价为"+$("#crbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
						if (v == 'ok') {
							$(obj).val(val);
						} else {
							$(obj).val(0);
						}
					});
				}
			}else if(groupType =="10"){
				if($("#crj").val() == undefined || $("#crj").val() == null || $("#crj").val() == ""){
					$.jBox.tip("1/2直客价为空不能报名！","error");
					$(obj).val(0);
				}else if(Number($("#crj").val() == 0)){
					$.jBox.confirm("1/2直客价为"+$("#crbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
						if (v == 'ok') {
							$(obj).val(val);
						} else {
							$(obj).val(0);
						}
					});
				}
			}
		}
		if(idVal.indexOf("Child") > 0 ){
			if(groupType =="2"){
				if($("#etj").val() == undefined || $("#etj").val() == null || $("#etj").val() == ""){
					$.jBox.tip("儿童直客价为空不能报名！","error");
					$(obj).val(0);
				}else if(Number($("#etj").val() == 0)){
					$.jBox.confirm("儿童直客价为"+$("#etbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
						if (v == 'ok') {
							$(obj).val(val);
						} else {
							$(obj).val(0);
						}
					});
				}
			}else if(groupType =="10"){
				if($("#etj").val() == undefined || $("#etj").val() == null || $("#etj").val() == ""){
					$.jBox.tip("3/4直客价为空不能报名！","error");
					$(obj).val(0);
				}else if(Number($("#etj").val() == 0)){
					$.jBox.confirm("3/4直客价为"+$("#etbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
						if (v == 'ok') {
							$(obj).val(val);
						} else {
							$(obj).val(0);
						}
					});
				}
			}
		}
		if(idVal.indexOf("Special") > 0 ){
			if(groupType =="2"){
				if($("#tsj").val() == undefined || $("#tsj").val() == null || $("#tsj").val() == ""){
					$.jBox.tip("直客价为空不能报名！","error");
					$(obj).val(0);
				}else if(Number($("#tsj").val() == 0)){
					$.jBox.confirm("特殊人群直客价为"+$("#tsbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
						if (v == 'ok') {
							$(obj).val(val);
						} else {
							$(obj).val(0);
						}
					});
				}
			}
		}
	}
	// 计算总得出行人数
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	var totalNum = parseInt(adultNum) + parseInt(childNum) + parseInt(specialNum) ;

	// 获取余位和切位剩余数
	var freePositionNum = $("#freePosition").val();// 余位
	var leftpayReservePosition = $("#leftpayReservePosition").val();// 切位
	
	//如果单签类型为儿童  则要验证儿童最高人数  、儿童已占位人数  限制
	if(idVal == 'orderPersonNumChild' && groupType !="10"){
		var maxChildrenCount = $("#maxChildrenCount").val();
		var currentChildrenCount = $("#currentChildrenCount").val();
		if(maxChildrenCount && parseInt(val) > parseInt(maxChildrenCount)) {
			$(obj).val(num);
			$.jBox.tip("儿童最高人数为"+maxChildrenCount+"人，请重新填写！", "warning", { focusId: idVal });
			return;
		}
		if(currentChildrenCount && parseInt(val) > parseInt(currentChildrenCount)) {
			$(obj).val(num);
			$.jBox.tip("儿童最高人数为"+maxChildrenCount+"人，剩余"+currentChildrenCount+"人，请重新填写！", "warning", { focusId: idVal });
			return;
		}
	}

	// 如果当前类型为特殊人群，则要验证特殊人最高人数 特殊人群已占位人数限制
	if(idVal == 'orderPersonNumSpecial' && groupType !="10"){
		var maxPeopleCount = $("#maxPeopleCount").val();// 团期下特殊最高人数限制
		var currentPeopleCount = $("#currentPeopleCount").val();//团期已占位人数
		if(parseInt(val) > parseInt(maxPeopleCount) && maxPeopleCount != null) {
			$(obj).val(num);
			$.jBox.tip("特殊人群最高人数为"+maxPeopleCount+"人，请重新填写！", "warning", { focusId: idVal });
			return;
		}
		if(parseInt(val) > parseInt(currentPeopleCount) && currentPeopleCount != null) {
			$(obj).val(num);
			$.jBox.tip("特殊人群最高人数为"+maxPeopleCount+"人，剩余"+currentPeopleCount+"人，请重新填写！", "warning", { focusId: idVal });
			return;
		}
	}

	// 修改数量不能小于当前数量
	if(groupType != '10' && num > Number(val)) {
		$(obj).val(num);
		$.jBox.tip("修改人数不能小于当前人数！", "warning", { focusId: idVal });
		return;
	}

	// 修改数量不能小于当前数量
	if(groupType == '10' && idVal == 'roomNumber' && num > Number(val)) {
		$(obj).val(num);
		$.jBox.tip("修改间数不能小于当前间数！", "warning", { focusId: idVal });
		return;
	}
	// 修改数量不能小于当前数量
	if(groupType == '10' && idVal != 'roomNumber' && num > Number(val)) {
		$(obj).val(num);
		$.jBox.tip("修改人数不能小于当前人数！", "warning", { focusId: idVal });
		return;
	}

	// 除散拼产品切位修改外，不存在切位情况，只需要考虑余位
	if(groupType != '10' && (totalNum - orgTotalPersonNum) > Number(freePositionNum)) {
		$(obj).val(num);
		$.jBox.tip("余位不足！", "warning", { focusId: idVal });
		return;
	}

	if(groupType == '10' && idVal == 'roomNumber' && (val - orgRoomNum) > Number(freePositionNum)) {
		$(obj).val(num);
		$.jBox.tip("余位不足！", "warning", { focusId: idVal });
		return;
	}
	// placeHolderType 为 1表示切位
	var placeHolderType = $("#placeHolderType").val();

	if(placeHolderType == 1 && (totalNum - orgTotalPersonNum) > Number(leftpayReservePosition)) {
		$(obj).val(num);
		$.jBox.tip("余位不足！", "warning", { focusId: idVal });
		return;
	}

	// 将总人数赋值为修改后的总人数
	$("#orderPersonelNum").val(totalNum);

	changeTotalPrice();
}

// ------------------------------------------------------------------------------------------------------------------------
/**
 * 展示单个游客成本价格和结算价格(修改订单会用到此方法)
 * @param traveler 游客表单对象
 * @param type 1:详情 2:修改
 */
function changePayPriceByCostChange_forDetail(traveler,type){
	// 成本价信息
	var travelerPrice = getTravelerPayPrice(traveler);	// 获取成本价
	var travelerSrcPayPrice = getTravelerSrcPayPrice(traveler);	// 获取实际结算价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalPrice(travelerPrice, 1);// 各种币种相加的结算结果
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	//优佳散拼同行价订单使用新模板
	if (isYoujia) {
		var totalDiscount = getTravelerTotalDiscount(traveler);	// 获取总优惠价
		var settlementPrice = getSettlementPrice(traveler);	// 获取游客同行价
		var totalPrice2 = getOrderToltalPrice(settlementPrice, 2);// 各种币种相加的结算结果(中文名称)
		var settlementClearPrice = getSettlementClearPrice(traveler);	// 获取同行结算价（同行 - 总优惠）
		$("span[name=totalDiscount]",traveler).html(totalDiscount.price);
		$("span[name=settlementPrice]",traveler).html(settlementPrice[0].str_mark);
		$("span[name=settlementClearPrice]",traveler).html(settlementClearPrice);
		$("span[name=jsPrice]",traveler).text(totalPrice2.replace(/<br>/g,"").replace(/<BR>/g,""));
		var otherCostPrice = getTravelerOtherCost(traveler);
		var totalOtherCostPrice = getOrderToltalPrice(otherCostPrice, 2);// 各种币种相加的结算结果
		$("span[name=totalOtherCostPrice]",traveler).html(totalOtherCostPrice);  //显示“其他费用总计”
		showClearPriceForOrderUpdateForCalculation(traveler,type,travelerPrice);
	} else {
		if(changeGroupFlag != '3' && changeGroupFlag != '5') {
			travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
		}
		$("span[name=innerJsPrice]",traveler).html(totalPrice);
		$("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
		showClearPriceForOrderUpdate(traveler,type);
	}
	// 结算价信息
	var travelerClearPrice = getTravelerClearPrice(traveler);	// 获取游客的结算价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalClearPrice = getOrderToltalClearPrice(travelerClearPrice, 1);// 各种币种相加的结算结果
	travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerClearPrice;
//	$("span[name=travelerClearPrice]",traveler).text(totalClearPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	$("span[name=travelerClearPrice]",traveler).html(totalClearPrice.replace(/<br>/g,"").replace(/<BR>/g,""));  //填充收起后的游客结算价
	changeTotalPrice();
}
/**
 * 费用发生变化时修改订单总额
 */
function changeTotalPrice() {
	// 计算此次订单填写的游客总人数
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	// 获取添加的游客的table数组
	var travelerTables = $("#traveler form.travelerTable");
	var exitOrChangeNum = $("#traveler form.travelerTable input[name=changeGroupFlag][value=3]").length
			+ $("#traveler form.travelerTable input[name=changeGroupFlag][value=5]").length;
	// 获取添加的游客数量
	var travelerCount = travelerTables.length - exitOrChangeNum;
	// 根据添加有游客人数显示添加游客按钮
	if (travelerCount >= allPersonNum) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}
	var travelerPriceTotal = new Array();	// 成本价
	var travelerClearPriceTotal = new Array();	// 结算价
	// 未添加的游客同行价数组
	// 获取未添加游客的结算价格 人数*同行价
	if (Number(allPersonNum) - Number(travelerCount) > 0) {
		// 预订人数
		var adultNum = $("#orderPersonNumAdult").val();
		var childNum = $("#orderPersonNumChild").val();
		var specialNum = $("#orderPersonNumSpecial").val();
		// 获取成人、儿童、特殊人群的同行价
		if(adultNum - countAdult() > 0){
			var priceObj = getAdultSettlementPrice(Number(adultNum - countAdult()));
			travelerPriceTotal.push(priceObj);	// 成本价(C147-C109指的是同行价)
			travelerClearPriceTotal.push(priceObj);// 结算价
		}
		if(childNum - countChild() > 0){
			var priceObj = getChildSettlementPrice(Number(childNum - countChild()));
			travelerPriceTotal.push(priceObj);// 成本价
			travelerClearPriceTotal.push(priceObj);// 结算价
		}
		if(specialNum - countSpecial() > 0){
			var priceObj = getSpecialSettlementPrice(Number(specialNum - countSpecial()));
			travelerPriceTotal.push(priceObj);// 成本价
			travelerClearPriceTotal.push(priceObj);// 结算价
		}
	}
	// 获取已添加游客的成本价格(即“订单总同行价”显示的值)
	if(isYoujia){
		travelerPriceTotal.push(getAdultSettlementPrice(countAdult()));	// 成人游客(C147-C109指的是同行价)
		travelerPriceTotal.push(getChildSettlementPrice(countChild()));	// 儿童游客(C147-C109指的是同行价)
		travelerPriceTotal.push(getSpecialSettlementPrice(countSpecial()));	// 特殊人群游客(C147-C109指的是同行价)
	} else {		
		for(var i = 0; i < travelerTotalPriceArr.length; i++){
			var travelerJsPrice = travelerTotalPriceArr[i].jsPrice;
			for(var j = 0; j < travelerJsPrice.length; j++){
				travelerPriceTotal.push(travelerJsPrice[j]);
			}
		}
	}
	var totalPrice = getOrderToltalPrice(travelerPriceTotal, 2);
	$("#travelerSumPrice").text(totalPrice);
	/**
	 * 获取已添加游客的结算价格
	 */
	if(travelerTotalClearPriceArr && travelerTotalClearPriceArr.length > 0){
		for(var i = 0; i < travelerTotalClearPriceArr.length; i++){
			var travelerClearPrice = travelerTotalClearPriceArr[i].travelerClearPrice;
			for(var j = 0; j < travelerClearPrice.length; j++){
				travelerClearPriceTotal.push(travelerClearPrice[j]);
			}
		}
	}
	// 订单总结算价S4S
	var totalClearPrice = getOrderToltalClearPrice(travelerClearPriceTotal, 2);
	$("#travelerSumClearPrice").text(totalClearPrice);
	if ($("priceType").val() == "2" || priceType == "2") {
	    // 订单总额
	    $("#orderTotalPrice").text(getS4SFromArray(travelerClearPriceTotal, 1) + "（含服务费）");
	    
	    if ($("#differenceFlag").length > 0 && $("#differenceFlag").val() == "1") {
			var obj = {};
			obj.currencyId = $("input[name=adultCurrencyId]").val();
			obj.price = $("#differenceMoney").val();
			travelerClearPriceTotal.push(obj);
			$("#allTotoalMoney").text(getS4SFromArray(travelerClearPriceTotal, 1));
		}
	}
	
	return totalPrice;
}

/**
 * 修改门店结算价返还金额后总结算价金额变化
 * @param obj
 */
function setAllTotalMoney(obj) {
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	var money = obj.value;
	if (money == "") {
		obj.value="0"
	}
	
	changeTotalPrice();
}

/**
 * 获取总额
 * @param clearArr
 * @param totalCharge
 */
function getOrderTotalPrice (clearArr, totalCharge) {
	var orderTotalArray = new Array();
	for (var i = 0; i < clearArr.length; i++) {
		var tempObj = new Object();
		tempObj.currencyId = clearArr[i].currencyId;
		tempObj.price = clearArr[i].price;
		orderTotalArray.push(tempObj);
	}
	for (var i = 0; i < orderTotalArray.length; i++) {
		if (orderTotalArray[i].currencyId.toString() == totalCharge.id.toString()) {
			orderTotalArray[i].price = Number(orderTotalArray[i].price) + Number(totalCharge.price);
		} else {
			var newCurrTotal = new Object();
			newCurrTotal.currencyId = totalCharge.id;
			newCurrTotal.price = Number(totalCharge.price);
			orderTotalArray.push(newCurrTotal);
		}
		break;
	}
	return orderTotalArray;
}

/**
 * 给定人数计算成人的同行价之和
 * @param personNum 成人人数
 */
function getAdultSettlementPrice(personNum){
	var priceObj = new Object();
	var crj = $("#crj").val()==""?0:$("#crj").val();
	var crCurrency = $('#crbz').val();
	var crPrice = Number(personNum) * parseFloat(crj);
	priceObj.currencyId = crCurrency;
	priceObj.price = crPrice;
	return priceObj;
}
/**
 * 给定人数计算儿童的同行价之和
 * @param personNum 儿童人数
 */
function getChildSettlementPrice(personNum){
	var priceObj = new Object();
	var etj = $("#etj").val()==""?0:$("#etj").val();
	var etCurrency = $('#etbz').val();
	var etPrice = Number(personNum) * parseFloat(etj);
	priceObj.currencyId = etCurrency;
	priceObj.price = etPrice;
	return priceObj;
}
/**
 * 给定人数计算特殊人群的同行价之和
 * @param personNum 特殊人群人数
 */
function getSpecialSettlementPrice(personNum){
	var priceObj = new Object();
	var tsj = $("#tsj").val()==""?0:$("#tsj").val();
	var tsCurrency = $('#tsbz').val();
	var tsPrice = Number(personNum) * parseFloat(tsj);
	priceObj.currencyId = tsCurrency;
	priceObj.price = tsPrice;
	return priceObj;
}

// 计算游客编号
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
	/*if ($("#companyUuid").val() == lameitourUuid) {*/
		var travelerTables = $("#traveler form");
		var $thisTraveler = $(travelerTables[travelerTables.length - 1]);
		var tempName = "游客" + travelerTables.length;
		// 填充输入框姓名
		$thisTraveler.find("input[name=travelerName]").val(tempName);
		// 填充展示span姓名
		$thisTraveler.find("span[name=tName]").html(tempName);
	/*}*/
}

/**
 * 验证渠道联系人及渠道联系人
 * @returns
 */
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

/**
 * 订单修改，第一步“下一步”，readonly所有联系人所有信息，隐藏增删按钮
 * @param errorList
 */
function readOnlyAllContactInfo(){
	
	$(".yd1AddPeople").hide();
	
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
		//删除按钮隐藏
		$(element).find("span[name=delContactButton]").hide();
    });
	$("#orderCompany").attr("disabled", true);
}

/**
 * 订单修改，第二步“上一步”，恢复原有读写状态（注意批发商可写可增配置）
 * @param errorList
 */
function back2WritableContactInfo(){
	$(".yd1AddPeople").show();
	//渠道选择只读
	$("#modifyAgentInfo").attr("disabled", false).parent().find("span[name=showAgentName]").remove();
	//联系人
	$("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		//下拉控件显示，删除回显span
		var contactSpan = $(element).find("span[name=channelConcat]");
		contactSpan.show().parent().find("span[name=showName]").remove();
//		contactSpan.after("<span name='showName'>" + contactSpan.find("input[name=contactsName]").val() + "</span>");
		if($("#orderContact_modifiability").val() == 1){
			//所有input 可写
			$(element).find("input").attr("disabled", false);
		} else {
			$(element).find("input[name=remark]").attr("disabled", false);
		}
		//增删按钮隐藏
		$(element).find("span[name=delContactButton]").show();
	});
	if($("#agentinfo_modifiability") && $("#agentinfo_modifiability").val() == '1'){		
		$("#orderCompany").attr("disabled", false);
	}
}

function createDivInDiv(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>")
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		var textTemp = $(valuein.element).parent().find("span").eq(0).text().replace(/[\:\：\*]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text().replace(/[\:\：\*]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}else if($(valuein.element).attr("name") == "inputClearPrice"){
				textTemp = "结算价";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus()
			};
		}(valuein.element));
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	isdoSave = false;
}

/**
 * 验证游客
 * @returns
 */
function _doValidatetravelerForm(travelerForm) {
	var forms = $(travelerForm);
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

/**
 * 费用发生变化时修改单个游客成本价格
 * @param traveler 游客表单对象
 */
function changePayPriceByCostChange(traveler) {
	var travelerPrice = getTravelerPayPrice(traveler);
	var otherCostPrice = getTravelerOtherCost(traveler);
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalPrice(travelerPrice, 1);// 各种币种相加的结算结果
	var totalPrice2 = getOrderToltalPrice(travelerPrice, 2);// 各种币种相加的结算结果
	var totalOtherCostPrice = getOrderToltalPrice(otherCostPrice, 2);// 各种币种相加的结算结果
	var travelerDiffPrice = travelerClearDiffArr[travelerIndex-1].diffPrice;  // 结算价差额
	var travelerClearPrice = calculatePriceByCurrency(travelerPrice, travelerDiffPrice, "ADDITION");  // 计算好的最终结算价
	var showClearPrice;
	showClearPrice = showClearPriceInput(travelerClearPrice,1); // 显示结算价输入框样式
	// 如果游客已退团或转团，则成本价不计入订单成本价
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	if(changeGroupFlag != '3' && changeGroupFlag != '5') {
		travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
	}
	$("span[name=innerJsPrice]",traveler).html(totalPrice);  //显示同行价（优佳之外的同行价跟优佳的同行价不同）
	$("span[name=totalOtherCostPrice]",traveler).html(totalOtherCostPrice);  //显示“其他费用总计”
	$("span[name=clearPrice]",traveler).html(showClearPrice);
	if (isYoujia){
		$("span[name=jsPrice]",traveler).text(totalPrice2.replace(/<br>/g,"").replace(/<BR>/g,""));
	} else {
		$("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	}
	changeTotalPrice();
}

/**
 * 获取其他费用
 * @param traveler
 */
function getTravelerOtherCost(traveler){
	var otherCostArray = [];
	var otherCostPrice = new CurrencyMoney(currencyList);
	// 获取其他费用
	otherCostPrice = addOtherCostFee(traveler, otherCostPrice);
	//组装。如果没有项则展示：同行价币种0.00
	var priceObj = new Object();
	$.each(otherCostPrice, function(key, value) {
		if(value && value != 0 && value != ""){
			priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			otherCostArray.push(priceObj);
		}
	});
	if(otherCostArray.length == 0){
		var priceObj = new Object();
		priceObj.currencyId = $("input[name=srcPriceCurrency]",traveler).val();
		priceObj.price = 0;
		otherCostArray.push(priceObj);
	}
	return otherCostArray;
}

/**
 * 优惠价格发生变化时，重新计算并展示“同行结算价”“结算价”
 * add by yang.jiang 2016-1-29 22:13:18
 */
function changeInnerDiscount(obj){
	var discount = $(obj).val();
	var traveler = $(obj).parents("form[name=travelerForm]");
	var orgDiscountPrice = traveler.find("input[name=orgDiscountPrice]").val();
	//如果优惠定额不存在，则查找对应游客类型的优惠定额
	if(orgDiscountPrice == undefined || orgDiscountPrice == null || orgDiscountPrice == ''){
		orgDiscountPrice = Number(traveler.find(".discount").find(".show").find(".activityDiscountAmount").text());
		$("input[name=orgDiscountPrice]",traveler).val(orgDiscountPrice);
	}
	var fixedDiscount = traveler.find("input[name=org_fixedDiscount]").val();	
	
	if(discount=="" || discount==null || discount==undefined) {discount="0"};
	var ms = discount.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	var txt = ms.split(".");
	discount = stmp = txt[0]+(txt.length>1?"."+txt[1]:"");
	$(obj).val(discount);

	//进行比较
	if(Number($(obj).val()) > Number(orgDiscountPrice)) {
		$.jBox.tip("输入不能超过产品优惠额度!", "warning");
		$(obj).val(fixedDiscount);
	}
	//填写隐藏域的值
	$("input[name=fixedDiscount]",traveler).val($(obj).val());
	
	var settlementClearPrice = getSettlementClearPrice(traveler);	// 获取同行结算价（同行 - 总优惠）
	$("span[name=settlementClearPrice]",traveler).html(settlementClearPrice);
	var travelerPrice = getTravelerPayPrice(traveler);
	var travelerIndex = traveler.find(".travelerIndex").text();
	var travelerDiffPrice = travelerClearDiffArr[travelerIndex-1].diffPrice;
	var travelerClearPrice = calculatePriceByCurrency(travelerPrice, travelerDiffPrice, "ADDITION");
	var showClearPrice = showClearPriceInput(travelerClearPrice,1); // 显示结算价输入框样式
	$("span[name=clearPrice]",traveler).html(showClearPrice);
	//由于输入的优惠导致游客结算价变化，不能立即在页面中获取结算价，所以先计算优惠输入前后的差值，再计算出游客应该有的结算价，最后使用到总结算价的计算中
	var travelerNewPrice = getTravelerNewPrice(discount, traveler);  //获取优惠输入前后差值 //获取新结算价  TODO 暂时未用上
	//添加游客的结算价到游客结算价数组中，以计算总结算价
	travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerClearPrice;
	
	var totalDiscount = getTravelerTotalDiscount(traveler);	// 获取总优惠价
	$("span[name=totalDiscount]",traveler).html(totalDiscount.price);
	var totalPrice = getOrderToltalPrice(travelerPrice, 2);// 各种币种相加的结算结果
	$("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	$("input[name=fixedDiscount]",traveler).val(discount);
	changeTotalPrice();
}
/**
 * 获取优惠输入前后差值
 */
function getTravelerNewPrice(discount, traveler){
	var fixedDiscount = $("input[name=fixedDiscount]",traveler).val();
	var diffObj = new Object();
	diffObj.price = fixedDiscount - discount;
	diffObj.currencyId = $("input[name=disCurrencyId]",traveler).val();
	
	var travelerPrice = getTravelerClearPrice(traveler);	// 获取游客的结算价	
	//使用差值计算
	for(var i= 0; i < travelerPrice.length; i++){
		if(travelerPrice[i].currencyId == diffObj.currencyId || Number(travelerPrice[i].currencyId) == Number(diffObj.currencyId)){
			travelerPrice[i].price = Number(travelerPrice[i].price) + Number(diffObj.price);
			break;
		}
	}
	return travelerPrice;
}

function getTravelerChargeRate(currencyId, travlerPrice) {
	// 如果单笔金额为负值或0，则不添加服务费
	if (Number(travlerPrice) < 0) {
		var tempPrice = 0;
		var quauqOtherChargeType = $("#quauqOtherChargeType").val();
		var quauqOtherChargeRate = $("#quauqOtherChargeRate").val();
		var partnerOtherChargeType = $("#partnerOtherChargeType").val();
		var partnerOtherChargeRate = $("#partnerOtherChargeRate").val();
		if (quauqOtherChargeType == 0) {
			tempPrice = Number(travlerPrice) * Number(quauqOtherChargeRate);
		} else {
			for(var j = 0; j < currencyList.length; j++) {
				var currency = currencyList[j];
				if (currencyId == currency.id) {
					tempPrice = (Number(quauqOtherChargeRate) / Number(currency.convertLowest)).toFixed(2);
				}
			}
		}
		if (partnerOtherChargeType == 0) {
			tempPrice = Number(tempPrice) + Number(travlerPrice) * Number(partnerOtherChargeRate);
		} else {
			for(var j = 0; j < currencyList.length; j++) {
				var currency = currencyList[j];
				if (currencyId == currency.id) {
					tempPrice = Number(tempPrice) + Number((Number(partnerOtherChargeRate) / Number(currency.convertLowest)).toFixed(2));
				}
			}
		}
		return Number(travlerPrice) + Number(tempPrice);
	} else {
		return Number(travlerPrice);
	}
}

/**
 * 获取游客的结算价
 *
 * @param traveler
 */
function changeClearPriceByInputChange(traveler) {
	var travelerPrice = getTravelerClearPrice(traveler);	// 获取游客的结算价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalClearPrice(travelerPrice, 1);// 各种币种相加的结算结果
	travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerPrice;
	// 填充单个游客信息收起显示的结算价格 add by zhangcl
	$("span[name=travelerClearPrice]",traveler).html(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	changeTotalPrice();
}

function countAdult() {
	var radios = $(".travelerTable input[name='personType'][value=1]:checked");
	return radios.length;
}
function countChild() {
	var radios = $(".travelerTable input[name='personType'][value=2]:checked");
	return radios.length;
}
function countSpecial() {
	var radios = $(".travelerTable input[name='personType'][value=3]:checked");
	return radios.length;
}

/**
 * 删除游客后调整游客和价格
 * @param travelerForm 游客表单对象
 */
function deleteTravelerAfter(travelerForm){
	var travlelerIndex = travelerForm.find(".travelerIndex").text();
	travelerTotalPriceArr.splice(travlelerIndex - 1,1);
	travelerTotalClearPriceArr.splice(travlelerIndex - 1,1);
	travelerForm.remove();
	changeTotalPrice();
	recountIndexTraveler();
}

/**
 * 不同币种的金额统计
 * @param priceArr 所有价格数组
 * @param type 需要显示的标记 1:每个游客需要显示的成本价格 2：订单总额显示的成本价格
 * @return 返回统计字符串
 * @param currencyList 公共参数 当前数据库里的币种对象集合 包含id currencyName currencyMark
 */
function getOrderToltalPrice(priceArr,type){
	var totalPrice = '';
	paramCurrencyId = new Array();
	paramCurrenctPrice = new Array();
	var totalPriceArr = new Array(currencyList.length);
	for(var i= 0; i < priceArr.length; i++){
		var priceObj = priceArr[i];
		for(var j = 0; j < currencyList.length; j++){
			var currency = currencyList[j];
			if(priceObj.currencyId == currency.id){
				if(totalPriceArr[j] == undefined){
					var priceTotalObject = new Object();
					priceTotalObject.price = parseFloat(priceObj.price);
					priceTotalObject.currencyId = currency.id;
					priceTotalObject.currencyName = currency.currencyName;
					priceTotalObject.currencyMark = currency.currencyMark;
					totalPriceArr[j] = priceTotalObject;
				}else{
					totalPriceArr[j].price += parseFloat(priceObj.price);
				}
			}
		}
	}
	for(var m = 0; m < totalPriceArr.length; m++){
		if(totalPriceArr[m] != undefined){
			if(totalPrice == ''){
				if(isYoujia){
					if(type == 1){
						totalPrice += totalPriceArr[m].currencyMark + milliFormat(totalPriceArr[m].price,1);
					} else {						
						totalPrice += totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
					}
				} else {					
					totalPrice += totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
				}
				paramCurrencyId.push(totalPriceArr[m].currencyId);
				paramCurrenctPrice.push(totalPriceArr[m].price);
			}
			else{
				if(type == 1){
					totalPrice += (isYoujia ? '+' + totalPriceArr[m].currencyMark : '<br>+' + totalPriceArr[m].currencyName) + milliFormat(totalPriceArr[m].price,1);
				}else{
					totalPrice += '+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);;
				}
				paramCurrencyId.push(totalPriceArr[m].currencyId);
				paramCurrenctPrice.push(totalPriceArr[m].price);
			}
		}
	}
	return totalPrice;
}

/**
 * 不同币种的金额字符串拼接
 * @param priceArr 所有币种价格数组
 * @param type 需要显示的币种标记 1:币种符号 2：币种名字
 * @return 返回统计字符串
 * @param currencyList 公共参数 当前数据库里的币种对象集合 包含id currencyName currencyMark
 */
function getPriceStr(priceArr,type){
	var totalPrice = '';
	var totalPriceArr = new Array(currencyList.length);
	for(var i= 0; i < priceArr.length; i++){
		var priceObj = priceArr[i];
		for(var j = 0; j < currencyList.length; j++){
			var currency = currencyList[j];
			if(priceObj.currencyId == currency.id){
				if(totalPriceArr[j] == undefined){
					var priceTotalObject = new Object();
					priceTotalObject.price = parseFloat(priceObj.price);
					priceTotalObject.currencyId = currency.id;
					priceTotalObject.currencyName = currency.currencyName;
					priceTotalObject.currencyMark = currency.currencyMark;
					totalPriceArr[j] = priceTotalObject;
				}else{
					totalPriceArr[j].price += parseFloat(priceObj.price);
				}
			}
		}
	}
	for(var m = 0; m < totalPriceArr.length; m++){
		if(totalPriceArr[m] != undefined){
			if(totalPrice == ''){
				totalPrice += (isYoujia ? totalPriceArr[m].currencyMark : totalPriceArr[m].currencyName) + milliFormat(totalPriceArr[m].price,1);
			}
			else{
				if(type == 1){
					totalPrice += (isYoujia ? '+' + totalPriceArr[m].currencyMark : '<br>+' + totalPriceArr[m].currencyName) + milliFormat(totalPriceArr[m].price,1);
				}else{
					totalPrice += '+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);;
				}
			}
		}
	}
	return totalPrice;
}

/**
 * 显示结算价输入框
 * @param priceArr 所有价格数组
 * @param type 需要显示的标记 1:每个游客需要显示的结算价格 2：订单总额显示的结算价格
 * @param currencyList 公共参数 当前数据库里的币种对象集合 包含id currencyName currencyMark
 * @return 返回字符串:如 人民币：<input type="text" /> + 美元：<input type="text" />
 */
function showClearPriceInput(priceArr,type){
	var showInput = '';
	if(priceArr.length > 0){
		for(var i = 0 ;i < priceArr.length ; i++){
			var moneyObject = priceArr[i];
			
			if (moneyObject.price == 0) {
				continue;
			}
			
			for(var j = 0 ; j < currencyList.length ; j++){
				var currencyObj = currencyList[j];
				if(currencyObj.id == moneyObject.currencyId){
					moneyObject.currencyName = currencyObj.currencyName;
					moneyObject.currencyMark = currencyObj.currencyMark;
					break;
				}
			}
			//如果是优佳的散拼同行价订单，则显示新模板，同行价显示币种符号
			var oneInput;
			var plusmark = "";
			if(i != 0){
				plusmark = "+";
			}
			if (isYoujia) {
				oneInput = "<span name='inputClearPriceDiv'>" + plusmark + moneyObject.currencyMark + "<em>"+moneyObject.price 
					+ "</em><input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" 
					+ "<input type='hidden' name='inputClearPrice' maxlength='9' class='required ipt3' value='" 
					+ moneyObject.price+"' onmouseout='changeClearPriceSum(this)' onblur='changeClearPriceSum(this)' onkeyup='changeClearPriceSum(this)'  /></span>";				
			}else{				
				oneInput = "<span name='inputClearPriceDiv'>" + moneyObject.currencyName + ":" 
							+ "<input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" 
							+ "<input type='hidden' name='inputClearPrice' value='" + moneyObject.price + "'/>" 
							+ moneyObject.price + "</span></BR>";
			}
			showInput += oneInput;
		}
	}
	return showInput;
}

/**
 * 获取游客的成本价格
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerPayPrice(traveler){
	// 如果游客已经退团或转团则成本价即为游客对应的成本价（不再计算其他费用、单房差、单价等之和）
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	if(changeGroupFlag == '3' || changeGroupFlag == '5') {
		var costMoney = traveler.find("input[name='jsPriceJson']").val();
		return eval(costMoney);
	}
	// 正常游客需要计算各种费用之和（其他费用、单房差、单价等）
	var travelerPrice = [];
	var travelerPayPrice = new CurrencyMoney(currencyList);
	var priceObj = new Object();
	// 获取其他费用
	travelerPayPrice = addOtherCostFee(traveler, travelerPayPrice);
	// 计算单房差小计
	var sumNight = traveler.find("input[name='sumNight']").val();
	var singleDiff = traveler.find("input[name='singleDiff']").val();
	if(singleDiff == undefined || singleDiff == "")	{
		singleDiff = 0;
	}
	var singleDiffCurrency = $('#singleDiffCurrencyId').val();
	var singleDiffSum = Number(sumNight) * parseFloat(singleDiff);
	singleDiffSum = Math.round(singleDiffSum*100)/100;
	$(".ydFont1",traveler).html(singleDiffSum.toString().formatNumberMoney('#,##0.00'));
	$("span[name=singleDiffSubtotal]",traveler).html(singleDiffSum.toString().formatNumberMoney('#,##0.00')); //优佳散拼同行价订单使用新模板
	// 获取单房差费用
	if(singleDiffSum != 0){
		travelerPayPrice[singleDiffCurrency] += singleDiffSum;
	}
	
	// 获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();	
	travelerPayPrice[srcPriceCurrency] += Number(src);
	// 获取游客联运价格
	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
	if(intermodalType == "1"){
		var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
		var intermodalPrice = Number($("select[name=intermodalStrategy]",traveler).find("option:selected").val());
		travelerPayPrice[intermodalCurrencyId] += intermodalPrice;
	}
	//如果是优佳的散拼同行价订单，则显示新模板，要减去总优惠价格
	if (isYoujia) {
		//获取总优惠价
		var totalDiscount = getTotalDiscount(traveler);
		travelerPayPrice[totalDiscount.currencyId] -= Number(totalDiscount.price);
	}
	
	$.each(travelerPayPrice, function(key, value) {
		if(value && value != 0 && value != ""){
			priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			travelerPrice.push(priceObj);
		}
	});	
	// 如果因为添加其他费用而导致所有币种金额都为0，则需要添加一个默认值
    if (travelerPrice.length == 0) {
		 priceObj = new Object();
		 priceObj.currencyId = $("#RMB_currencyId").val();
		 priceObj.price = "0.00";
		 travelerPrice.push(priceObj);
    }
	return travelerPrice;
}

/**
 * 从后台获取游客的实际结算价，与游客各项价格计算结果有出入
 * @param traveler
 * 返回多币种对象组成的数组
 */
function getTravelerSrcPayPrice(traveler){
	var jsPriceJson = $("input[name=travelerClearPriceJson]",traveler).val();
	var priceObjArr = new Array();
	if(jsPriceJson != undefined && jsPriceJson != null && jsPriceJson != ""){		
		$.each($.parseJSON(jsPriceJson), function(key, value){
			priceObjArr.push(value);
		});
	}
	return priceObjArr;
}

/**
 * 初始化游客结算价差额
 * @param traveler
 * 返回多币种对象组成的数组
 */
function initTravelerDiffPrice(traveler){
	var diffPriceArr = new Array();
	var srcPay = getTravelerSrcPayPrice(traveler);  // 后台取出实际结算价
	var costPay = getTravelerPayPrice(traveler);  // 计算得出成本结算价
	var settlementPrice = getSettlementPrice(traveler);  // 获取游客同行价对象
	if(srcPay.length > 0){		
		for(var i = 0 ;i < srcPay.length ; i++){
			for(var j = 0 ;j < costPay.length ; j++){
				if(srcPay[i].currencyId == costPay[j].currencyId || Number(srcPay[i].currencyId) == Number(costPay[j].currencyId)){
					var diffPrice = new Object();
					diffPrice.currencyId = srcPay[i].currencyId;
					diffPrice.price = Number(srcPay[i].price) - Number(costPay[j].price);
					diffPriceArr.push(diffPrice);
					continue;
				}
			}
		}
	} else {
		var diffPrice = new Object();
		diffPrice.currencyId = settlementPrice[0].currencyId;
		diffPrice.price = Number(0);
		diffPriceArr.push(diffPrice);
	}
	var travelerDiff = new Object();
	travelerDiff.diffPrice = diffPriceArr;
	travelerClearDiffArr.push(travelerDiff);
}

/**
 * 获取游客总优惠价字符串
 */
function getTravelerTotalDiscount(traveler){
	var resultObj = new Object();
	//获取后台传入的已通过审批的优惠价
	var disCurrencyMark = $("input[name=disCurrencyMark]",traveler).val();
	var reviewedDiscount = $("input[name=reviewedDiscount]",traveler).val();
	var fixedDiscount = $("input[name=discount]",traveler).val();
	//组装（金额、币种id、币种名称、币种符号、价格字符串）
	resultObj.price = Number(reviewedDiscount) + Number(fixedDiscount);
	resultObj.currencyId = "";
	resultObj.currencyName = "";
	resultObj.currencyMark = disCurrencyMark;
	resultObj.str_name = "";
	resultObj.str_mark = disCurrencyMark + (Number(reviewedDiscount) + Number(fixedDiscount));
	
	return resultObj;
}

/**
 * 获取游客总优惠价数目
 */
function getTotalDiscount(traveler){
	//获取后台传入的已通过审批的优惠价
	var srcPriceCurrency = $("input[name='srcPriceCurrency']",traveler).val();
	var disCurrencyMark = $("input[name=disCurrencyMark]",traveler).val();
	var reviewedDiscount = $("input[name=reviewedDiscount]",traveler).val();
	var fixedDiscount = $("input[name=discount]",traveler).val();	
	//组装
	var priceObj = new Object();
	priceObj.currencyId = srcPriceCurrency;
	priceObj.currencyMark = disCurrencyMark;
	priceObj.price = Number(reviewedDiscount) + Number(fixedDiscount);
	return priceObj;
}

/**
 * 获取游客同行结算价
 */
function getSettlementClearPrice(traveler){
	//总优惠价
	var totalDiscount = getTravelerTotalDiscount(traveler);
	//同行价
	var settlementPrice = $("input[name=srcPrice]",traveler).val();
	return totalDiscount.currencyMark + (Number(settlementPrice) - totalDiscount.price).toFixed(2);
}

/**
 * 获取游客同行价
 */
function getSettlementPrice(traveler){
	var resultArray = []
	var resultObj = new Object();
	//同行价
	var settlementPrice = $("input[name=srcPrice]",traveler).val();
	//同行价币种mark
	var srcPriceCurrency = $("input[name=srcPriceCurrency]",traveler).val();
	var srcPriceCurrencyMark = $("input[name=srcPriceCurrencyMark]",traveler).val();
	var srcPriceCurrencyName = $("input[name=srcPriceCurrencyName]",traveler).val();
	//组装
	resultObj.price = settlementPrice;
	resultObj.currencyId = srcPriceCurrency;
	resultObj.currencyMark = srcPriceCurrencyMark;
	resultObj.currencyName = srcPriceCurrencyName;
	resultObj.str_mark = srcPriceCurrencyMark + settlementPrice;
	resultObj.str_name = srcPriceCurrencyName + settlementPrice;
	resultArray.push(resultObj);
	return resultArray;
}

/**
 * 拼接结算价展示信息
 *
 * @param traveler 游客表单对象
 * @param type 1:详情 2:修改
 */
function showClearPriceForOrderUpdate(traveler,type){
	var jsPriceJson = $("input[name=travelerClearPriceJson]",traveler).val();
	var priceArr = new Array();
	$.each($.parseJSON(jsPriceJson), function(key, value){
		priceArr.push(value);
	});
	var showInput = '';
	if(priceArr.length > 0){
		for(var i = 0 ;i < priceArr.length ; i++){
			var moneyObject = priceArr[i];
			for(var j = 0 ; j < currencyList.length ; j++){
				var currencyObj = currencyList[j];
				if(currencyObj.id == moneyObject.currencyId){
					moneyObject.currencyName = currencyObj.currencyName;
					moneyObject.currencyMark = currencyObj.currencyMark;
					break;
				}
			}
			// 订单详情页结算价展示方式
			if(type == 1){
				var oneInput = "<span name='inputClearPriceDiv'>" + (i != 0 ? "+" : "") + moneyObject.currencyName + ":" + milliFormat(moneyObject.price,1)
							+ "<input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" 
							+ "<input type='hidden' name='inputClearPrice' value='"+ moneyObject.price+"'  /></span></BR>";
			}else{
				// 订单修改页面结算价展示方式
				var oneInput = "<span name='inputClearPriceDiv'>" + (i != 0 ? "+" : "") + moneyObject.currencyName + ":" 
							+ "<input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" 
							+ "<input type='hidden' name='inputClearPrice' value='" + moneyObject.price + "'/></span>" 
							+ moneyObject.price + "</BR>";
			}
			showInput += oneInput;
		}
	}
	if ($("priceType").val() == "2" || priceType == "2") {
		$("span[name=clearPrice]",traveler).html(showInput.replace(/<br>/g,"").replace(/<BR>/g,"") + "（含服务费）");
	} else {
		$("span[name=clearPrice]",traveler).html(showInput.replace(/<br>/g,"").replace(/<BR>/g,""));
	}
}

/**
 * 拼接结算价展示信息
 *
 * @param traveler 游客表单对象
 * @param type 1:详情 2:修改
 */
function showClearPriceForOrderUpdateForCalculation(traveler,type,travelerPrice){
	var travelerIndex = traveler.find(".travelerIndex").text();
	var travelerDiffPrice = travelerClearDiffArr[travelerIndex-1].diffPrice;
	var clearPriceArr = calculatePriceByCurrency(travelerPrice, travelerDiffPrice, "ADDITION");
	var jsPriceJson = $("input[name=travelerClearPriceJson]",traveler).val();
	var priceArr = new Array();
	$.each(clearPriceArr, function(key, value){
		priceArr.push(value);
	});
	var showInput = '';
	if(priceArr.length > 0){
		for(var i = 0 ;i < priceArr.length ; i++){
			var moneyObject = priceArr[i];
			for(var j = 0 ; j < currencyList.length ; j++){
				var currencyObj = currencyList[j];
				if(currencyObj.id == moneyObject.currencyId || Number(currencyObj.id) == Number(moneyObject.currencyId)){
					moneyObject.currencyName = currencyObj.currencyName;
					moneyObject.currencyMark = currencyObj.currencyMark;
					break;
				}
			}
			// 订单详情页结算价展示方式
			if(type == 1){
				var oneInput = "<span name='inputClearPriceDiv'>" + moneyObject.currencyMark + ":" 
							+ milliFormat(moneyObject.price,1)+ "<input type='hidden' name='inputCurreyId' alt='" 
							+ moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" 
							+ "<input type='hidden' name='inputClearPrice' value='" + moneyObject.price + "'  /></span></BR>";
			}else{
				// 订单修改页面结算价展示方式
				var oneInput = "<span name='inputClearPriceDiv'>" + moneyObject.currencyMark + "<span><em>" + moneyObject.price 
							+ "</em></span><input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName + "' value='" 
							+ moneyObject.currencyId+"'/>" 
							+ "<input type='hidden' name='inputClearPrice' readonly='readonly' value='"+ moneyObject.price+"' onmouseout='changeClearPriceSum(this)' onblur='changeClearPriceSum(this)' onkeyup='changeClearPriceSum(this)' /></span>" + (isYoujia ? "+" : "</br>");
			}
			showInput += oneInput;
		}
		if(isYoujia){
			showInput = showInput.substring(0, showInput.length - 2);
		}
	}
	$("span[name=clearPrice]",traveler).html(showInput.replace(/<br>/g,"").replace(/<BR>/g,""));
}

/**
 * 多币种计算(同币种之间计算)
 * @param arr0 计算数组1
 * @param arr1 计算数组2
 * @param operation 计算类别
 */
function calculatePriceByCurrency(arr0, arr1, operation){
	//获取两组数据包含的币种
	var currencys = new Array();
	for(var i = 0 ;i < arr0.length ; i++){
		if(currencys.indexOf(arr0[i].currencyId) == -1){
			currencys.push(arr0[i].currencyId);
		}
	}
	for(var j = 0 ;j < arr1.length ; j++){
		if(currencys.indexOf(arr1[j].currencyId.toString()) == -1){
			currencys.push(arr1[j].currencyId.toString());
		}
	}
	//创建结果数组
	var resultArray = new Array();
	for(var k = 0 ;k < currencys.length ; k++){
		var tempPrice = new Object();
		tempPrice.currencyId = currencys[k];
		tempPrice.price = 0;
		resultArray.push(tempPrice);
	}
	//处理数组1
	for(var i = 0 ;i < resultArray.length ; i++){
		for(var j = 0 ;j < arr0.length ; j++){
			//如果币种相等则直接计算
			if(resultArray[i].currencyId == arr0[j].currencyId || Number(resultArray[i].currencyId) == Number(arr0[j].currencyId)){				
				resultArray[i].price = (Number(resultArray[i].price) + Number(arr0[j].price)).toFixed(2);
			}
		}
	}
	//处理数组2
	for(var i = 0 ;i < resultArray.length ; i++){
		for(var j = 0 ;j < arr1.length ; j++){
			//如果币种相等则直接计算
			if(resultArray[i].currencyId == arr1[j].currencyId || Number(resultArray[i].currencyId) == Number(arr1[j].currencyId)){				
				if(operation == "ADDITION" || operation == "+"){				
					resultArray[i].price = (Number(resultArray[i].price) + Number(arr1[j].price)).toFixed(2);
				} else if(operation == "SUBSTRACTION" || operation == "-"){
					resultArray[i].price = (Number(resultArray[i].price) - Number(arr1[j].price)).toFixed(2);
				}
			}
		}
	}
	return resultArray;
}

/**
 * 获取游客的结算价格
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerClearPrice(traveler){
	var travelerPrice = [];
	$("span[name=inputClearPriceDiv]",traveler).each(function(index,element){
		var priceObj = new Object();
		var inputArr = $(element).find("input");
		$(inputArr).each(function(index,element){
			if($(element).attr("name") == "inputCurreyId"){
				priceObj.currencyId = $(element).val();
				priceObj.currencyName = $(element).attr("alt");
			}
			if($(element).attr("name") == "inputClearPrice"){
				priceObj.price = $(element).val();
			}
		});
		travelerPrice.push(priceObj);
	});

	return travelerPrice;
}

/**
 * 不同币种的金额统计
 * @param priceArr 所有价格数组
 * @param type 需要显示的标记 1:每个游客需要显示的结算价格 2：订单总额显示的结算价格
 * @return 返回统计字符串
 * @param currencyList 公共参数 当前数据库里的币种对象集合 包含id currencyName currencyMark
 */
function getOrderToltalClearPrice(priceArr,type){
	var totalPrice = '';
	paramClearCurrencyId = new Array();
	paramClearCurrenctPrice = new Array();
	var totalPriceArr = new Array(currencyList.length);
	for(var i= 0; i < priceArr.length; i++){
		var priceObj = priceArr[i];
		for(var j = 0; j < currencyList.length; j++){
			var currency = currencyList[j];
			if(priceObj.currencyId == currency.id){
				if(totalPriceArr[j] == undefined){
					var priceTotalObject = new Object();
					priceTotalObject.price = parseFloat(priceObj.price);
					priceTotalObject.currencyId = currency.id;
					priceTotalObject.currencyName = currency.currencyName;
					priceTotalObject.currencyMark = currency.currencyMark;
					totalPriceArr[j] = priceTotalObject;
				}else{
					totalPriceArr[j].price += parseFloat(priceObj.price);
				}
			}
		}
	}
	for(var m = 0; m < totalPriceArr.length; m++){
		if(totalPriceArr[m] != undefined){
			if(totalPrice == ''){
				totalPrice += totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
				paramClearCurrencyId.push(totalPriceArr[m].currencyId);
				paramClearCurrenctPrice.push(Number(totalPriceArr[m].price).toFixed(2));
			}
			else{
				if(type == 1){
					totalPrice += '<br>+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
				}else{
					totalPrice += '+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);;
				}
				paramClearCurrencyId.push(totalPriceArr[m].currencyId);
				paramClearCurrenctPrice.push(Number(totalPriceArr[m].price).toFixed(2));
			}
		}
	}
	return totalPrice;
}

/**
 * 根据金额数组，组装用以显示的字符串
 */
function getS4SFromArray(array, type) {
	var totalPrice = '';
	// 同币种相加
	var totalPriceArr = new Array(currencyList.length);	
	for(var i= 0; i < array.length; i++){
		var priceObj = array[i];
		for(var j = 0; j < currencyList.length; j++){
			var currency = currencyList[j];
			if(priceObj.currencyId == currency.id){
				if(totalPriceArr[j] == undefined){
					var priceTotalObject = new Object();
					priceTotalObject.price = parseFloat(priceObj.price);
					priceTotalObject.currencyId = currency.id;
					priceTotalObject.currencyName = currency.currencyName;
					priceTotalObject.currencyMark = currency.currencyMark;
					totalPriceArr[j] = priceTotalObject;
				}else{
					totalPriceArr[j].price += parseFloat(priceObj.price);
				}
			}
		}
	}
	// string 4 show
	for(var m = 0; m < totalPriceArr.length; m++){
		if(totalPriceArr[m] != undefined){
			if(totalPrice != ''){
				if(type == 1){
					totalPrice += '+';
				} else if (type == 2){
					totalPrice += '</br>';
				} else {
					totalPrice += '+';
				}
			}
			totalPrice += totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
		}
	}
	return totalPrice;
}

// 游客信息展开收起后显示姓名和结算价
function travelerBoxCloseOnAdd(obj){
	var obj_this = $(obj);
	var travelerForm = obj_this.closest("form");
	if(obj_this.attr("class").match("boxCloseOnAdd")) {
		obj_this.removeClass("boxCloseOnAdd");
		obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
		obj_this.parent().find('.tourist-t-off').css("display","none");
		obj_this.parent().find('.tourist-t-on').show();
		if($('input[name=travelerName]',travelerForm).is(":hidden")){
			$('input[name=saveBtn]',travelerForm).hide();
			$('input[name=editBtn]',travelerForm).show();
		}else{
			$('input[name=saveBtn]',travelerForm).show();
			$('input[name=editBtn]',travelerForm).hide();
		}
		// add by zhangcl 结算价展示信息
		setOneTravelerClearPriceShow(travelerForm,2);  //注意第二个参数的意义
	} else {
//		var curForm = obj_this.parent().parent();
		obj_this.addClass("boxCloseOnAdd");
		obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
		obj_this.parent().find('.tourist-t-off').css("display","inline");
		// add by zhangcl 结算价展示信息
		setOneTravelerClearPriceShow(travelerForm,1);
		
		obj_this.parent().find('.tourist-t-on').hide();
		$('input[name=saveBtn]',travelerForm).hide();
		$('input[name=editBtn]',travelerForm).show();
		$(".ydbz_xwt").removeClass("displayClick");
	}
}

/**
 * 设置单个游客结算价展示信息
 * @param traveler 游客对象
 * @returns type 1:游客信息隐藏 2:游客信息展示
 */
function setOneTravelerClearPriceShow(travelerForm,type){
	var jsPrice = getTravelerClearPrice(travelerForm);
	// 隐藏游客信息时，结算价展示方式
	if(type ==1){
		var showMessage = "";
		if(jsPrice.length > 0){
			for(var i = 0 ; i < jsPrice.length;i++){
				var currencyName = jsPrice[i].currencyName;
				var price = jsPrice[i].price;
				showMessage += currencyName+milliFormat(price,1)+"+";
			}
		}
		showMessage = showMessage.substring(0,showMessage.length-1);
		$("span[name=travelerClearPrice]",travelerForm).text(showMessage);
	}else{
		// TODO 展示游客信息时，结算价展示方式 待优化
	}
}

/**
 * 去除数组中重复元素
 * @param data
 * @returns
 */
function uniqueArray(data){
	data = data || [];
	var a = {};
	for (var i=0; i<data.length; i++) {
		var v = data[i];
		if (typeof(a[v]) == 'undefined'){
			a[v] = 1;
		}
	};
	data.length=0;
	for (var i in a){
		data[data.length] = i;
	}
	return data;
}

function adderrorList(outerrorList, errorList) {
	outerrorList = outerrorList.concat(errorList);
	return outerrorList;
}

/**
 *游客保存成功后，修改游客信息
 */
function saveTravelerAfter(obj,travelerForm,type){

	var input=$(travelerForm).find("input:checkbox");
	if(type == "save"){
		var sums = $("input[name='sum']",travelerForm);
		$.each(sums, function(key, value) {
			var _$span = $(value).next();
			_$span.text(_$span.text());
			;
		});
		$(obj).parent().parent().disableContainer( {
			blankText : "—",
			formatNumber : formatNumberArray
		});
		input.each(function() {
			if($(this).attr("checked")){
				$(this).addClass("disabledClass").hide();
			}else{
				$(this).addClass("disabledClass").hide();
				$(this).next().addClass("disabledClass").hide();
			}
		})
	}
	else{
		$(travelerForm).undisableContainer();
		input.each(function() {
			$(this).removeClass("disabledClass").show();
			$(this).next().removeClass("disabledClass").show();
		})
	}
	//添加费用
	var addcost=$("a[name='addcost']",travelerForm);
	//删除
	var deleltecost=$("a[name='deleltecost']",travelerForm);
	//应用全部
	var useall=$("a[name='bjyyqb']",travelerForm);
	if(type == "save"){
		$(obj).hide().removeClass("displayClick");
		$(addcost).hide();
		$(deleltecost).hide();
		$(useall).hide();
		$('input[name=editBtn]',travelerForm).show().removeClass("displayClick");
		$(obj).parent().prev().hide();
		$('.tourist-t-off',travelerForm).css("display","inline");
		$('.tourist-t-on',travelerForm).hide();
		$('.add_seachcheck',travelerForm).addClass('boxCloseOnAdd')
		// 移除“价格方案”上的displayClick
		$("a[name=jgfa]").removeClass("displayClick");
		// 改变“价格备注”span的style以便换行展示
		$("textarea[name=priceRemark]").each(function(index, element){
			$(element).parent().find("span[class=disabledshowspan]").attr("style","word-wrap:break-word;word-break:break-all;");
		});
	}
	else{
		$(obj).hide().removeClass("displayClick");
		$(addcost).show();
		$(deleltecost).show();
		$(useall).show();
		$(obj).parent().prev().show();
		$('.tourist-t-off',travelerForm).css("display","none");
		$('.tourist-t-on',travelerForm).show();
		$(":radio[name='personType'][class='temp']").hide();
		$('.add_seachcheck',travelerForm).removeClass('boxCloseOnAdd')
		$('input[name=saveBtn]',travelerForm).show();
		// 修改展开后，设置保存状态为未保存
    	var travelerIndex = $(travelerForm).find(".travelerIndex").text();
    	saveStatusArray[travelerIndex-1] = 0;
	}
}

/**
 * 费用报价应用全部
 */
function useAllPrice(obj){
	flag = true;
	var travelerFormList = $("#traveler form[name=travelerForm]");
	var curTravelerForm = $(obj).closest("form[name=travelerForm]");
	var costCurrencyIds = $("input[name=costCurrencyIds]",curTravelerForm).val();
	if(travelerFormList.length>1 && costCurrencyIds && costCurrencyIds != ""){
		$.jBox.confirm("是否将其它费用中所有款项应用于全部已添加的游客", "提示", function(v, h, f){
			if (v == 'ok') {
				
				$("#traveler .payfor-otherDiv").each(function(i, o) {
					$(this).html(curTravelerForm.find(".payfor-otherDiv").html());
					$(this).show();
					$(this).parent().find("[name=addcost]").text("编辑其他费用");
				})

				var travelerForm = null;
				for(var i=0; i < travelerFormList.length; i++){
					travelerForm = $(travelerFormList[i]);
					if($("input[name='saveBtn']",travelerForm).is(":hidden")){
						$("div[flag='messageDiv']",travelerForm).show();
						$(".bj-info",travelerForm).find("a[name='deleltecost']").hide();
						$(".payfor-otherDiv",travelerForm).disableContainer( {
							blankText : "—",
							formatNumber : formatNumberArray
						});
						$("div[flag='messageDiv']",travelerForm).hide();
					}
					changePayPriceByCostChange(travelerForm);
					changeClearPriceByInputChange(travelerForm);
				}
			}
		});
	}
}

/**
 * 附件上传回调方法
 * @param {Object} obj button对象
 * @param {Object} fileIDList  文件表id
 * @param {Object} fileNameList 文件原名称
 * @param {Object} filePathList 文件url
 */
function commenFunction(obj,fileIDList,fileNameList,filePathList){
	if($("#isForYouJia").val()=="true") {
		var name = obj.name;
		if (fileIDList.length > 0) {
			var fileIds = fileIDList.split(";");
			fileIds.pop();
			var fileNames = fileNameList.split(";");
			fileNames.pop();
			var filePaths = filePathList.split(";");
			filePaths.pop();
			$.each(fileIds, function(i, val) {
				$(obj).after("<span class='seach_r' style='position:relative;margin-left:67px'>" +
						"<b>" + fileNames[i] + "</b>" + 
						"<input type='hidden' name="+name+"docID value='" + val + "' />" + 
						"<input type='hidden' name="+name+"docName value='" +fileNames[i]+ "' />" + 
						"<input type='hidden' name="+name+"docPath value='" +filePaths[i]+ "' />" + 
						"<a class='deleteicon' style='margin-left:10px; position:absolute; top:9px;' " +
						"href='javascript:void(0)' onclick='deleteFiles(" + val + ", this)'>x</a>" +
						"</span>");
			});
		}
	} else {
		if (fileNameList && filePathList) {
			var name = obj.name;
			$("#"+name).remove();
			$(obj).prev().val(fileNameList);
			$(obj).parent().append('<div style="display: none" id="'+obj.name+'"></div>');
			$("#"+name).append("<input type='hidden' name="+name+"docID value='" +fileIDList.replace(";","")+ "' />")
					.append("<input type='hidden' name="+name+"docName value='" +fileNameList.replace(";","")+ "' />")
					.append("<input type='hidden' name="+name+"docPath value='" +filePathList.replace(";","")+ "' />");
			$(obj).prev().show();
		}
	}
}

/**
 * 删除现有的文件
 */
function deleteFiles(id, obj) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			//获取待删除文件的id
			var delFileId = $(obj).parent().find("input[name$=docID]").val();
			var $tobeDelFiles = $(obj).parents("ul[name=visafiles]").find("input[name=tobeDelFiles]");
			var tempStr = "";
			if($tobeDelFiles.val() && $tobeDelFiles.val() != ""){
				tempStr = $tobeDelFiles.val() + "," + delFileId;
			} else {
				tempStr = delFileId;
			}
			$tobeDelFiles.val(tempStr);
			//移除包含文件信息的节点
			$(obj).parents("span[class=seach_r]").remove();
		}
	},{buttonsFocus:1});
}

/**
 * 结算价手输时计算差额
 * @author yang.jiang 2016-03-28 17:23:19
 * @param priceOld 输入前结算价
 * @param priceNew 输入后结算价
 * @param traveler 参与计算的游客
 */
function changeClearPriceDiff(priceOld, priceNew, traveler){
	var diffPriceArr = new Array();
	// 计算前后差额
	for(var i = 0 ;i < priceOld.length ; i++){
		for(var j = 0 ;j < priceNew.length ; j++){
			if(priceOld[i].currencyId == priceNew[j].currencyId || Number(priceOld[i].currencyId) == Number(priceNew[j].currencyId)){
				var diffPrice = new Object();
				diffPrice.currencyId = priceOld[i].currencyId;
				diffPrice.price = Number(priceNew[j].price) - Number(priceOld[i].price);
				diffPriceArr.push(diffPrice);
				continue;
			}
		}
	}
	// 游客原有差额
	var travelerIndex = traveler.find(".travelerIndex").text();
	var srcDiff = travelerClearDiffArr[travelerIndex-1].diffPrice;
	// 与原先差额再次计算，得出最终差额
	for ( var i = 0; i < diffPriceArr.length; i++) {
		for ( var j = 0; j < srcDiff.length; j++) {
			if(diffPriceArr[i].currencyId == srcDiff[j].currencyId || Number(diffPriceArr[i].currencyId) == Number(srcDiff[j].currencyId)){
				var diffPrice = new Object();
				diffPrice.currencyId = diffPriceArr[i].currencyId;
				diffPrice.price = Number(diffPriceArr[i].price) + Number(srcDiff[j].price);
				diffPriceArr[i] = diffPrice;
				continue;
			}
		}
	}
	// 组装
	var travelerDiff = new Object();
	travelerDiff.diffPrice = diffPriceArr;
	travelerClearDiffArr[travelerIndex-1] = travelerDiff;
}

//验证返佣费用合法性
function checkRebatesValue(obj){
	var money = obj.value;
	if(money){
		if(money >= 0){
			var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
			// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
			if (ms != obj.value) {
				var txt = ms.split(".");
				obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
			}
		}else{
			obj.value = '0';
		}
	}
}

//得到焦点事件：隐藏填写费用名称提示
function payforotherIn(doc) {
	var obj = $(doc);
	obj.siblings(".ipt-tips2").hide();
}

//失去焦点事件：如果输入框中没有值，则提示填写费用名称
function payforotherOut(doc){
	var obj = $(doc);
	if(!obj.val()){
		obj.siblings(".ipt-tips2").show();
	}
}

//点击提示错误信息中 "修改" 后错误输入框得到焦点
function focusIpt(doc){
	$(doc).parent().find('input[type=text].ipt2').trigger("focus");
}

/**
 *修改单房差几晚
 */
function changeSumNight(obj){
	obj.value = obj.value.replace(/[^\d\+\-]/g,'');
	changePayPriceByCostChange($(obj.form));
	changeClearPriceByInputChange($(obj.form));  //add by zhangcl
}

/**
 * 币种改变时
 * @param obj
 */
function changeCostCurrency(obj){
	changePayPriceByCostChange($(obj).closest("form"));
	changeClearPriceByInputChange($(obj).closest("form"));  //add by zhangcl
}

/**
 * 费用改变时
 */
var stmp="";
function changeSum(obj){
	var money = obj.value;
	if(money=="") {obj.value="0"};
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	changePayPriceByCostChange($(obj).closest("form"));
	changeClearPriceByInputChange($(obj).closest("form"));  //add by zhangcl
}
/**
 * 从身份证信息处得到出生年月
 * @param obj
 * @return
 */
function getBirthday(obj){
	var sId = obj.value;
	var iSum=0
	var sBirthday=""
	if(sId.length==18) {
		if(!/^\d{17}(\d|x)$/i.test(sId)) {
			alert("号码不符合规定!前17位号码应全为数字,18位号码末位可以为数字或X");
			return false;
		}
		sId=sId.replace(/x$/i,"a");
		sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2));
		var d=new Date(sBirthday.replace(/-/g,"/"))
		if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())) {
			alert("非法生日");
			return false;
		}
		for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11)
		if(iSum%11!=1) {
			alert("Error:非法证号");
			return false;
		}
		$(obj).parents("div.tourist-left").children().children().children("[name=sex]").val(sId.substr(16,1)%2?"1":"2");
	} else if(sId.length==15) {
		if (!(/(^\d{15}$)/.test(sId))) {
			alert("号码不符合规定!15位号码应全为数字");
			return false;
		}
		sBirthday="19"+sId.substr(6,2)+"-"+Number(sId.substr(8,2))+"-"+Number(sId.substr(10,2));
		var d=new Date(sBirthday.replace(/-/g,"/"))
		if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())) {
			alert("非法生日");
			return false;
		}
	} else {
		if (sId.length != 0) {
			alert("身份证号码为15位或18位，请检查！");
			return false;
		}
	}
	$(obj).parents("div.tourist-left").children().children().children("input[name=birthDay]").val(sBirthday);
}

/**
 * 验证游客类型是否和所选人数匹配
 */
function validutePersonType(travelerForm){
	var personType = $('input[name="personType"]:checked',travelerForm).val();
	if(personType == 1){
		if($("#orderPersonNumAdult").val() < countAdult()){
			top.$.jBox.tip('成人人数与初始值不匹配请修改', 'error');
			return false;
		}
	}else if(personType == 2){
		if($("#orderPersonNumChild").val() < countChild()){
			top.$.jBox.tip('儿童人数与初始值不匹配请修改', 'error');
			return false;
		}
	}else{
		if($("#orderPersonNumSpecial").val() < countSpecial()){
			top.$.jBox.tip('特殊人数与初始值不匹配请修改', 'error');
			return false;
		}
	}
	return true;
}

function travelerMouseover() {
	$("li.seach25.seach33").each(function(index, obj) {
		$(obj).mouseover(function() {
			getTavelerDataInfo(obj)
		});
	});
}

function getTavelerDataInfo(obj) {
	$(obj).attr("title", $("input:eq(0)", obj).val());
}

$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name]) {
			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

function milliFormat(s,isFloat){
	var minusSign = false;
	if(isFloat){//弥补JavaScript浮点运算的一个bug
		try{
			s = s.toFixed(2);
		}catch (e){

		}
	}

	if((typeof s) != String){
		s = s.toString();
	}
	if(/^\-/.test(s)){
		minusSign = true;
		s = s.substring(1);
	}
	if(/[^0-9\.]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	if(isFloat){
		s=s.replace(/,(\d\d)$/,".$1");
	}else{
		s=s.replace(/,(\d\d)$/,"");
	}
	if(minusSign){
		s= '-' + s;
	}
	return s.replace(/^\./,"0.");
}

/**
 * 文件上传后续处理（报名时特殊需求处的附件上传）
 */
function commenFunction4SpecialDeman(obj,fileIDList,fileNameList,filePathList) {
	var name = obj.name;
	if (fileIDList.length > 0) {
		var fileIds = fileIDList.split(";");
		fileIds.pop();
		var fileNames = fileNameList.split(";");
		fileNames.pop();
		var filePaths = filePathList.split(";");
		filePaths.pop();
		if($("#fileIdList").val() != "") {
			$("#fileIdList").val($("#fileIdList").val()+ "," + fileIds);
		} else {
			$("#fileIdList").val(fileIds);
		}
		$.each(fileIds, function(i, val) {
			$("input[name='"+obj.name+"']").after('<br><span class="seach_checkbox_2" style="position:relative;margin-left:105px">' +
				'<a href="javascript:void(0)" class="downloadFile" onclick="downloads4SpecialDeman('+fileIds[i]+')">'+fileNames[i]+'</a> ' +
				'<a style="margin-left:10px;" class="deleteicon" href="javascript:void(0)" onclick="deleteFiles4SpecialDeman('+fileIds[i]+',event)">x</a></span>');
		});
	}
}

//文件下载
function downloads4SpecialDeman(docid) {
	var ctx = $("#ctx").val();
	window.open(ctx + "/sys/docinfo/download/"+docid);
}

//文件删除
function deleteFiles4SpecialDeman(id, event) {
	var targetElement = event.target;
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			var fileIds = $(targetElement).parents("p.pr").find("#fileIdList").val();
			var fileIdArr = fileIds.split(",");
			var index = fileIdArr.indexOf(id+"");
			if(index >= 0) {
				fileIdArr.splice(index, 1);
			}
			var finalFileIds = fileIdArr.join(",");
			$(targetElement).parents("p.pr").find("#fileIdList").val(finalFileIds);
			$(targetElement).parent().remove();
		}
	},{buttonsFocus:1});
}

//复制表单元素的值和html
(function ($) {
	var oldHTML = $.fn.html;
	$.fn.formhtml = function () {
		if (arguments.length) return oldHTML.apply(this, arguments);

		$("input,textarea,button", this).each(function () {
			this.setAttribute('value', this.value);
		});
		$(":radio,:checkbox", this).each(function () {
			// im not really even sure you need to do this for "checked"
			// but what the heck, better safe than sorry
			if (this.checked) this.setAttribute('checked', 'checked');
			else this.removeAttribute('checked');
		});
		$("option", this).each(function () {
			// also not sure, but, better safe...
			if (this.selected) this.setAttribute('selected', 'selected');
			else this.removeAttribute('selected');
		});
		return oldHTML.apply(this);
	};

	//optional to override real .html() if you want
	// $.fn.html = $.fn.formhtml;
})(jQuery);

//获取浏览器版本
var userAgent = navigator.userAgent,
		rMsie = /(msie\s|trident.*rv:)([\w.]+)/,
		rFirefox = /(firefox)\/([\w.]+)/,
		rOpera = /(opera).+version\/([\w.]+)/,
		rChrome = /(chrome)\/([\w.]+)/,
		rSafari = /version\/([\w.]+).*(safari)/;
var browser;
var version;
var ua = userAgent.toLowerCase();
function uaMatch(ua) {
	var match = rMsie.exec(ua);
	if (match != null) {
		return { browser : "IE", version : match[2] || "0" };
	}
	var match = rFirefox.exec(ua);
	if (match != null) {
		return { browser : match[1] || "", version : match[2] || "0" };
	}
	var match = rOpera.exec(ua);
	if (match != null) {
		return { browser : match[1] || "", version : match[2] || "0" };
	}
	var match = rChrome.exec(ua);
	if (match != null) {
		return { browser : match[1] || "", version : match[2] || "0" };
	}
	var match = rSafari.exec(ua);
	if (match != null) {
		return { browser : match[2] || "", version : match[1] || "0" };
	}
	if (match != null) {
		return { browser : "", version : "0" };
	}
}

function detectOS() {
	var sUserAgent = navigator.userAgent;

	var isWin = (navigator.platform === "Win32") || (navigator.platform === "Windows");
	var isMac = (navigator.platform === "Mac68K") || (navigator.platform === "MacPPC") || (navigator.platform === "Macintosh") || (navigator.platform === "MacIntel");
	var bIsIpad = sUserAgent.match(/ipad/i) === "ipad";
	var bIsIphoneOs = sUserAgent.match(/iphone os/i) === "iphone os";
	var isUnix = (navigator.platform === "X11") && !isWin && !isMac;
	var isLinux = (String(navigator.platform).indexOf("Linux") > -1);
	var bIsAndroid = sUserAgent.toLowerCase().match(/android/i) === "android";
	var bIsCE = sUserAgent.match(/windows ce/i) === "windows ce";
	var bIsWM = sUserAgent.match(/windows mobile/i) === "windows mobile";
	if (isMac)
		return "Mac";
	if (isUnix)
		return "Unix";
	if (isLinux) {
		if (bIsAndroid)
			return "Android";
		else
			return "Linux";
	}
	if(bIsCE || bIsWM){
		return 'wm';
	}

	if (isWin) {
		var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1;
		if (isWin2K)
			return "Win2000";
		var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 ||
				sUserAgent.indexOf("Windows XP") > -1;
		if (isWinXP)
			return "WinXP";
		var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1;
		if (isWin2003)
			return "Win2003";
		var isWinVista = sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;
		if (isWinVista)
			return "WinVista";
		var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
		if (isWin7)
			return "Win7";
		var isWin8 = sUserAgent.indexOf("Windows NT 6.2") > -1 || sUserAgent.indexOf("Windows NT 6.3") > -1 || sUserAgent.indexOf("Windows 8") > -1;
		if (isWin8)
			return "Win8";
	}
	return "other";
}

//如果是IE11，则禁止输入姓名事件
$(function() {
	if ("Win8" == detectOS()) {
		$.each($("input[name=travelerName]"), function(index, obj) {
			var inputValue = $(this).val();
			if (inputValue == '') {
				var html = '<input type="text" maxlength="30" name="travelerName" class="traveler required">';
				$(this).after(html).remove();
			}
		});
	}
});