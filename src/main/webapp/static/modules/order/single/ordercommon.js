var flag = false;//是否要重新保存游客结算价（点击应用全部的时候，结算价不会保存）
var count = 0;
outerrorList = new Array();
//需要传递给订单的参数 成本价
var paramCurrencyId = new Array();
var paramCurrenctPrice = new Array();
//需要传递给订单的参数 结算价
var paramClearCurrencyId = new Array();
var paramClearCurrenctPrice = new Array();
//计算所有游客费用数组 
var travelerTotalPriceArr = new Array();	//成本价
var travelerTotalClearPriceArr = new Array();//结算价
var travelerClearDiffArr = new Array();  // 结算价差额
var saveStatusArray = new Array();  // 游客保存状态（0：未保存 1：已保存）（新增、修改 置为0 ； 保存成功置为 1）
var oldOrderPersonNumAdult = 0;
var oldOrderPersonNumChild = 0;
var oldOrderPersonNumSpecial = 0;
var formatNumberArray = new Array();
// 拉美途uuid
var lameitourUuid = '7a81a26b77a811e5bc1e000c29cf2586';
var priceType = $("#priceType").val();
var showFlag = true;

//formatNumberArray.push("sum");
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

//定义分币种金额对象
function CurrencyMoney(currencyList){
	var obj = {};
	$.each(currencyList, function(key, val) {
		var currencyId = val.id;
		obj[currencyId] = 0.00;
 　　});   
	return obj;
}

$(function(){
	//各块信息展开与收起
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
    //初始化预定操作按钮事件
    initStep();
    //游客选择是否需要联运 如果需要初始价格和联运区域
    initIntermodal();
    //游客选择自备签国家
    selZbqCountry();
    //改变游客数量
    changeTravelerCount();
    //改变游客类型
    changePersonType();
    //游客名称失去焦点时触发
    getTravelerNamePinyin();
    //发证日期失去焦点时触发
    getPassportValidity();
	//添加游客点击事件
    addTraveler();
    //删除游客点击事件
    delTraveler();
	//删除其他费用时触发
    delOtherCost();
	//改变住房要求时触发
	changeHotelDemand();
	//绑定日期控件
	dodatePicker();
	//游客上传资料悬浮时间
	travelerMouseover();
	
	// 删除多余单击事件
	$(".zksx").unbind("click");
	
	if ($("#priceType").val() == 2) {
		var quauqProductChargeRate = $("#quauqProductChargeRate").val();
		var partnerProductChargeRate = $("#partnerProductChargeRate").val();
		var quauqOtherChargeRate = $("#quauqOtherChargeRate").val();
		var partnerOtherChargeRate = $("#partnerOtherChargeRate").val();
		if ((!quauqProductChargeRate || Number(quauqProductChargeRate) == 0)
			&& (!partnerProductChargeRate || Number(partnerProductChargeRate) == 0)
			&& (!quauqOtherChargeRate || Number(quauqOtherChargeRate) == 0)
			&& (!partnerOtherChargeRate || Number(partnerOtherChargeRate) == 0)) {
			showFlag = false;
		}
	}
});

function initStep(){

	// 当没有价格时，对应类型的游客不能报名
	disableInput4NullPrice();
	
	// 第二步点击下一步
	$("#secondToThirdStepDiv").click(function() {
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
		
		//游客信息 
		$("#manageOrder_new").disableContainer( {
			blankText : "—",
			formatNumber : formatNumberArray,
		});
		//特殊需求
		$("#manageOrder_m").disableContainer( {
			blankText : "—",
			formatNumber : formatNumberArray,
		});
		$(".downloadFile").removeClass("displayClick");
		//费用
		var sums = $(".tourist-right").find("input[name='sum']");
		$.each(sums, function(key, value) {
			var _$span = $(value).next();
			_$span.text(_$span.text());
			
		});

		$("#secondDiv").hide();
		$("#thirdDiv").show();
		$(".orderPersonMsg").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step3");

		// for 109 优惠
		var applyTravelerIds = $("#applyTravelerIds").val();
		if(applyTravelerIds != null && $.trim(applyTravelerIds).length > 0) {
			$("#saveThenSubmit").show();
			$("#saveThenSubmit").prev().hide();
		} else {
			$("#saveThenSubmit").hide();
		}
	});
	
	// 第二步点击上一步
	$("#secondToOneStepDiv").click(function() {
		//恢复可写
		back2WritableContactInfo();
		
		$("#oneToSecondStepDiv").show();
		$("#productOrderTotal").undisableContainer();
		$("#orderpersonMesdtail").undisableContainer();
		$("ul[name='orderpersonMes']").undisableContainer();
		$("ul[name='orderpersonMes'] .ydbz_x").show();
		$("#ordercontact .yd1AddPeople").show();
		$("#oneToSecondOutStepDiv").show();
		$("#secondDiv").hide();
		$("#manageOrder_new").hide();
		$("#manageOrder_m").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step1");
		var _closeOrExpand = $(".closeOrExpand").eq(0);
		$(".orderPersonMsg").hide();
		if (_closeOrExpand.hasClass("ydClose")) {
			_closeOrExpand.click();
		}
		$("input[name=priceTypeRadio]").attr("disabled",true);
	});
	
	
	//第三步点击上一步
	$("#thirdToSecondTStepDiv").click(function() {
		$("#manageOrder_new").undisableContainer();
		// 游客信息只读
		$("#manageOrder_new").find("form").each(function(index, element){
			var saveBtn = $(element).find("input[name=saveBtn]");
			saveTravelerAfter(saveBtn, element, "save");
			// 设置所有的select效果
			$(element).find("select").each(function(ind, ele){
				$(ele).disabledselect();
			});
			// 使用“修改”按钮是为了实现其他按钮等展示正常，对于游客信息来说上一步已经做了只读。需要把保存状态恢复一下
		});
		$("#manageOrder_m").undisableContainer();
		$("#secondDiv").show();
		$("#thirdDiv").hide();
		$("#stepbar").removeClass("yd-step3").addClass("yd-step2");
	});
}

/**
 * 当没有价格时，对应类型的游客不能报名
 */
function disableInput4NullPrice() {
	// 如果游客价格为空，则不允许预定
	var settlementAdultPriceSrc = $("[name=settlementAdultPriceSrc]").val();  // 同行价
	var settlementChildPriceSrc = $("[name=settlementChildPriceSrc]").val();
	var settlementSpecialPriceSrc = $("[name=settlementSpecialPriceSrc]").val();
	var suggestAdultPrice = $("[name=suggestAdultPrice]").val();  // 直客价
	var suggestChildPrice = $("[name=suggestChildPrice]").val();
	var suggestSpecialPrice = $("[name=suggestSpecialPrice]").val();
	var retailAdultPrice = $("[name=retailAdultPrice]").val();  // 推广价
	var retailChildPrice = $("[name=retailChildPrice]").val();
	var retailSpecialPrice = $("[name=retailSpecialPrice]").val();
	
	var illegalAdultPrice = false;  // 是否无 成人价 illegalAdultPrice
	var illegalChildPrice = false;  // 是否无 儿童价
	var illegalSpecialPrice = false;  // 是否无 特殊人群价
	// 分析判断不同价格类型下的价格
	if ($("#agentSourceType").val() == "2") {  // 渠道来源（quauq渠道）
		if (!retailAdultPrice || Number(retailAdultPrice) < 0) {illegalAdultPrice = true;}
		if (!retailChildPrice || Number(retailChildPrice) < 0) {illegalChildPrice = true;}
		if (!retailSpecialPrice || Number(retailSpecialPrice) < 0) {illegalSpecialPrice = true;}
	} else{  // （自有渠道）
		if (priceType == "1" || $("#priceType").val() == "1") {  // 直客价
			if (!suggestAdultPrice || Number(suggestAdultPrice) < 0) {illegalAdultPrice = true;}
			if (!suggestChildPrice || Number(suggestChildPrice) < 0) {illegalChildPrice = true;}
			if (!suggestSpecialPrice || Number(suggestSpecialPrice) < 0) {illegalSpecialPrice = true;}
		} else {
			if (!settlementAdultPriceSrc || Number(settlementAdultPriceSrc) < 0) {illegalAdultPrice = true;}  // 同行价
			if (!settlementChildPriceSrc || Number(settlementChildPriceSrc) < 0) {illegalChildPrice = true;}
			if (!settlementSpecialPriceSrc || Number(settlementSpecialPriceSrc) < 0) {illegalSpecialPrice = true;}
		}
	}
	// 无价格，则对应类型的游客不可以报名 (有价格的解除disable)
	if (illegalAdultPrice) {
		$("#orderPersonNumAdult").attr("disabled", true);
	} else {
		$("#orderPersonNumAdult").attr("disabled", false);
	}
	if (illegalChildPrice) {
		$("#orderPersonNumChild").attr("disabled", true);
	} else {
		$("#orderPersonNumChild").attr("disabled", false);
	}
	if (illegalSpecialPrice) {
		$("#orderPersonNumSpecial").attr("disabled", true);
	} else {
		$("#orderPersonNumSpecial").attr("disabled", false);
	}
	//
	return (illegalAdultPrice && illegalChildPrice && illegalSpecialPrice);
}

//第一步到第二步页面效果
function firstToSecondEffect(){
	//设置只读人数
	$("#productOrderTotal").disableContainer( {
        blankText : "0",
        formatNumber : formatNumberArray
    });
	$("#productOrderTotal").attr("style","");
	//设置只读联系人
//    $("#orderpersonMesdtail").disableContainer( {
//        blankText : "—",
//        formatNumber : formatNumberArray
//    });
    
    $("#ordercontact .yd1AddPeople").hide();
    $("#manageOrder_new").show();
    $("#manageOrder_m").show();
    $("#oneToSecondOutStepDiv").hide();
    $("#secondDiv").show();
    $(".orderPersonMsg").show();
    $("#stepbar").removeClass("yd-step1").addClass("yd-step2");
    $("ul[name='orderpersonMes'] .ydbz_x").hide();
    $("#priceTypeSettlement").show();
}

function changeManor(obj){
	var visatds = $(obj).closest("tr").children("td");
	var selVisaType = $("select[name=visaType]",visatds.eq(2));
	var tempVisaType = $("input[name=tempVisaType]", visatds.eq(2)).val();
	var countryId =  $("input[name=sysCountryId]",visatds.eq(0)).val();
	var manor = $(obj).val();
	selVisaType.empty();
	$.ajax( {
		type : "POST",
		url : "../../traveler/manage/getVisaType",
		data : {
			countryId : countryId,
			manor: manor
		},
		success : function(msg) {
			var visaTypeJson = $.parseJSON(msg);
			$.each(visaTypeJson, function(key, val){
				$.each(val, function(key, val){
					selVisaType.append("<option value='" + key + "'>" + val + "</option>");
				});
		    });
			if (tempVisaType) {
				selVisaType.val(tempVisaType);
			}
		}
	});
	
}


/**
 * 添加渠道联系人
 */
function addAgentContact(obj){
	var contactPeopleNum = $("ul[name=orderpersonMes]").length;
	$('#ordercontact').append('<ul class="ydbz_qd" name="orderpersonMes">'+
		'<li><label><span class="xing">*</span>渠道联系人<font>' + (contactPeopleNum+1) +'</font>：</label>'+
		'<input id="contactsName'+(contactPeopleNum+1)+' maxlength="10" type="text" name="contactsName" value="" class="required"/></li>'+
		'<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label>'+
		'<input id="contactsTel'+(contactPeopleNum+1)+' maxlength="15" type="text" name="contactsTel" value="" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,\'\',\'2\')">展开全部</div><span class="ydbz_x gray" onclick="delAgentContact(this)">删除联系人</span></li>'+
		'<li flag="messageDiv" style="display:none" class="ydbz_qd_close"><ul>'+
		'<li><label>固定电话：</label><input type="text" name="contactsTixedTel" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"/></li>'+
		'<li><label>联系人地址：</label><input type="text" name="contactsAddress"/></li>'+
        '<li><label>传真：</label><input maxlength="" type="text" name="contactsFax"/></li>'+
        '<li><label>QQ：</label><input class="qq" maxlength="" type="text" name="contactsQQ"/></li>'+
        '<li><label>Email：</label><input class="email" maxlength="" type="text" name="contactsEmail"/></li>'+
        '<li><label>邮编：</label><input class="zipCode" maxlength="" type="text" name="contactsZipCode"/></li>'+
        '<li><label>其他：</label><input maxlength="" type="text" name="remark"/></li>'+
        '</ul></li></ul>')
}

/**
 * 删除渠道联系人
 */
function delAgentContact(obj){
	//删除联系人触发
	var $this = $(obj);
//	$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
//		if (v == 'ok') {
			$this.closest('.ydbz_qd').remove();
			$("ul[name=orderpersonMes]").each(function(index, element) {
		        $(element).children("li").eq(0).find("font").text(index+1);
		        $(element).children("li").eq(0).find("input[name='contactsName']").attr("id","contactsName"+index+1);
		        $(element).children("li").eq(1).find("input[name='contactsTel']").attr("id","contactsTel"+index+1);
		    });
//		} 
//	});
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
	var isForYouJia = $('[name=isForYouJia]:input').val();
	$("#traveler").delegate("input[name='issuePlace']", "blur", function() {
		// 有效日期
		var _$validityDate = $(this).closest(".tourist-info1").find("input[name='passportValidity']");
		var minDate = $(this).val();
		var curDate = new Date();
		
		//护照有效期默认为发证日期加10年，然后减一年
		if(minDate != "") {
			var lastDate = null;
			if(isForYouJia == "true") {
				var lastDate = new Date(minDate.split('/')[2] + '/' + minDate.split('/')[1] + '/' + minDate.split('/')[0]);
			} else {
				var lastDate = new Date(new Date(minDate.replace(/-/g,'/')));
			}
			lastDate.setFullYear(lastDate.getFullYear() + 10);
			lastDate.setDate(lastDate.getDate() - 1);
			if(isForYouJia == "false") {
				$(_$validityDate).val(lastDate.getFullYear() + "-" + (lastDate.getMonth() + 1) + "-" + lastDate.getDate());
			} else {
				$(_$validityDate).val(lastDate.getDate() +"/"+ (lastDate.getMonth() + 1) +"/"+ lastDate.getFullYear());
			}

		}

		if (new Date(minDate) < curDate) {
			minDate = curDate;
		}

		_$validityDate.unbind();
		if(isForYouJia == "true") {
			_$validityDate.datepicker( {
				minDate : minDate,
				dateFmt : 'dd/MM/yyyy'
			});
		} else {
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
		if($("#priceType").val() == 2){
			$("span[name=labelSPGPrice]").text("QUAUQ价");
			$("span[name=labelSPG]").text("QUAUQ价");
			// 如果是推广价，则隐藏这段
			$("span[name=labelSPGPrice]").closest(".fr").hide();
		} else if($("#priceType").val() == 1){
			$("span[name=labelSPGPrice]").text("直客价");
			$("span[name=labelSPG]").text("直客价");
		}else{
			$("span[name=labelSPGPrice]").text("同行价");
			$("span[name=labelSPG]").text("同行价");
		}
		var _travelerForm = $table.clone().addClass("travelerTable");
		$("#traveler").append(_travelerForm);
		
		//默认添加游客信息时，判断什么游客类型
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
		//填充人员类型
		$("input[name=personType]",_travelerForm)[personType].checked = true;
		//for 109 优惠
		var isForYouJia = $("[name='isForYouJia']").val();
		var activityKind = $("#activityKind").val();
		var isPeer = $("#priceType").val();

		if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
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
		//填充游客内部显示的结算价
		if(isForYouJia == 'true' && activityKind == '2' && isPeer == '0') {
			var innerHtmlValue = "<span name='inputClearPriceDiv'>" + selSrcPriceCurrencyMark + selJsPrice + "<input type='hidden' name='inputClearPrice' maxlength='9' style='display:none;' value='"+selJsPrice+"'/><input type='hidden' name='inputCurreyId' alt='"+selSrcPriceCurrencyName+"' value='"+selSrcPriceCurrency+"' /></span>";
			$("span[name=clearPrice]",_travelerForm).html(innerHtmlValue);
		} else {
			var innerHtmlValue = "<span name='inputClearPriceDiv'>" + selSrcPriceCurrencyName + ":" 
							+ "<input type='hidden' name='inputCurreyId' alt='" + selSrcPriceCurrencyName + "' value='" + selSrcPriceCurrency + "' />" 
							+ "<input type='hidden' name='inputClearPrice' value='" + selJsPrice + "' />" 
							+ selJsPrice + "</span></BR>";
			if ($("#priceType").val() == "2" && showFlag) {
				innerHtmlValue += "（含服务费）";
			}
			$("span[name=clearPrice]",_travelerForm).html(innerHtmlValue);
		}

		// 填充游客内部同行结算价
		if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
			var peerJs = "<span name='peerJs'>" + selSrcPriceCurrencyMark + selJsPrice + "</span>（优惠减<span name='discountAmount'>0</span>）";
			$("span.peerJsPrice",_travelerForm).html(peerJs);
		}

		//填充游客内部显示的同行价
		if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
			$("span[name=innerJsPrice]",_travelerForm).html(selSrcPriceCurrencyMark + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		} else {
			$("span[name=innerJsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		}

		//存放不同类型游客的同行价
		$("input[name=srcPrice]",_travelerForm).val(selJsPrice);
		//存放不同类型游客的同行价币种
		$("input[name=srcPriceCurrency]",_travelerForm).val(selSrcPriceCurrency);
		//填充单个游客信息收起显示的成本价格
		$("span[name=jsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		// 初始化游客差额
		initTravelerDiffPrice(_travelerForm);
		// 初始化游客保存状态（新增为0，未保存）
		initSaveStatus(_travelerForm);
		//组装每种游客同行价的对象
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
		//绑定日期控件
		dodatePicker();
		//计算游客编号
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
function initSaveStatus(_travelerForm) {
	saveStatusArray.push(0);
}

/**
 * 初始化游客结算价差额 (由于报名都是新增游客，初始差额必定是0)
 * @param traveler
 * 返回多币种对象组成的数组
 */
function initTravelerDiffPrice(traveler){
	var diffPriceArr = new Array();
	var allCurrency = new CurrencyMoney(currencyList);
	
	var diffPrice = new Object();
	$.each(allCurrency, function(key, value) {
		if(value > 0){
			diffPrice = new Object();
			diffPrice.currencyId = key;
			diffPrice.price = Number(value);
			diffPriceArr.push(diffPrice);
		}
	});
	
	var travelerDiff = new Object();
	travelerDiff.diffPrice = diffPriceArr;
	travelerClearDiffArr.push(travelerDiff);
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
 * 删除游客后保存订单总额
 */
function saveOrderTotalMoney(){
	var currencyId = paramCurrencyId.join(",");
	var currencyPrice = paramCurrenctPrice.join(",");
	$.ajax({
		type : "POST",
		url : "../../orderCommon/manage/lastSave",
		data : {
			productOrderId : $("#orderid").val(),
            currencyId : currencyId,
            currenctPrice : currencyPrice
		},
		success : function(msg) {
			if(msg.productOrder == null){
				top.$.jBox.tip('订单金额计算有误！', 'error');
			}
		}
	});
}

/**
 * 删除游客其他费用
 */
function delOtherCost(){
	$("#traveler").delegate("a[name='deleltecost']", "click", function() {
		var $this = $(this);
		var costId = $this.closest(".cost").find("input[name='id']").val();
		//for 109 优惠
		var isForYouJia = $("[name='isForYouJia']").val();
		var activityKind = $("#activityKind").val();
		var isPeer = $("#priceType").val();

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
							if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
								changeOtherTotalPrice($this.closest("form"));
								changePayPriceForYouJia($this.closest("form"));
							} else {
								changePayPriceByCostChange($this.closest("form"));
							}
							changeClearPriceByInputChange($this.closest("form"));	// add by zhangcl
							
						}
					});
				} else {
					var travelerForm = $this.closest("form");
					$this.closest(".cost").remove();
					if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
						changeOtherTotalPrice(travelerForm);
						changePayPriceForYouJia($this.closest("form"));
					} else {
						changePayPriceByCostChange(travelerForm);
					}
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
		//for 109 优惠
		var isForYouJia = $("[name='isForYouJia']").val();
		var activityKind = $("#activityKind").val();
		var isPeer = $("#priceType").val();

		if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
			changePayPriceForYouJia($(this).closest("form"));		// 修改游客结算价
		} else {
			changePayPriceByCostChange($(this).closest("form"));
		}

		changeClearPriceByInputChange($(this).closest("form"));  //add by zhangcl

	});
}

/**
 * 获取游客的成本价格
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerPayPrice(traveler){
	//如果游客已经退团或转团则成本价即为游客对应的成本价（不再计算其他费用、单房差、单价等之和）
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	if(changeGroupFlag == '3' || changeGroupFlag == '5') {
		var costMoney = traveler.find("input[name='jsPriceJson']").val();
		return eval(costMoney);
	}
	
	//正常游客需要计算各种费用之和（其他费用、单房差、单价等）
	var travelerPrice = [];
	var travelerPayPrice = new CurrencyMoney(currencyList); 
	var priceObj = new Object();

	//获取其他费用
	travelerPayPrice = addOtherCostFee(traveler, travelerPayPrice);
	
	//计算单房差小计
	var sumNight = traveler.find("input[name='sumNight']").val();
	var singleDiff = traveler.find("input[name='singleDiff']").val();
	if(singleDiff == undefined || singleDiff == "")	{
		singleDiff = 0;
	}
	var singleDiffCurrency = $('#singleDiffCurrencyId').val(); 
	var singleDiffSum = Number(sumNight) * parseFloat(singleDiff);
	singleDiffSum = Math.round(singleDiffSum*100)/100;
	$(".ydFont1",traveler).html(singleDiffSum.toString().formatNumberMoney('#,##0.00'));// 单房差小计
	$(".forPeer",traveler).html(singleDiffSum.toString().formatNumberMoney('#,##0.00'));// for 订单优惠
	//获取单房差费用
	if(singleDiffSum != 0){
		travelerPayPrice[singleDiffCurrency] += singleDiffSum;
	}
	
	//获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();
	travelerPayPrice[srcPriceCurrency] += Number(src);
	//获取游客联运价格
	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
    if(intermodalType == "1"){
    	var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
    	var intermodalPrice = Number($("select[name=intermodalStrategy]",traveler).find("option:selected").val());
		travelerPayPrice[intermodalCurrencyId] += intermodalPrice;
    }
    $.each(travelerPayPrice, function(key, value) {
    	if (value && value != "" && value != 0) {
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
 * 修改游客结算价
 */
function getTravelerPayPriceForYouJia(traveler) {
	//如果游客已经退团或转团则成本价即为游客对应的成本价（不再计算其他费用、单房差、单价等之和）
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	if(changeGroupFlag == '3' || changeGroupFlag == '5') {
		var costMoney = traveler.find("input[name='jsPriceJson']").val();
		return eval(costMoney);
	}

	//正常游客需要计算各种费用之和（其他费用、单房差、单价等）
	var travelerPrice = [];
	var travelerPayPrice = new CurrencyMoney(currencyList);
	var priceObj = new Object();
	
	//获取其他费用
	travelerPayPrice = addOtherCostFee(traveler, travelerPayPrice);

	//计算单房差小计
	var sumNight = traveler.find("input[name='sumNight']").val();
	var singleDiff = traveler.find("input[name='singleDiff']").val();
	if(singleDiff == undefined || singleDiff == "")	{
		singleDiff = 0;
	}
	var singleDiffCurrency = $('#singleDiffCurrencyId').val();
	var singleDiffSum = Number(sumNight) * parseFloat(singleDiff);
	singleDiffSum = Math.round(singleDiffSum*100)/100;
	$(".ydFont1",traveler).html(singleDiffSum.toString().formatNumberMoney('#,##0.00'));// 单房差小计
	$(".forPeer",traveler).html(singleDiffSum.toString().formatNumberMoney('#,##0.00'));// for 订单优惠
	//获取单房差费用
	if(singleDiffSum != 0){
		travelerPayPrice[singleDiffCurrency] += singleDiffSum;
	}

	//获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();
	travelerPayPrice[srcPriceCurrency] += Number(src);

	// 获取游客优惠
	var discountPrice = traveler.find("input[name='discountPrice']").val();
	if(discountPrice == undefined || discountPrice == null || discountPrice == ""){
		discountPrice = 0;
	}
	travelerPayPrice[srcPriceCurrency] -= Number(discountPrice);

	//获取游客联运价格
	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
	if(intermodalType == "1"){
		var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
		var intermodalPrice = Number($("select[name=intermodalStrategy]",traveler).find("option:selected").val());
		travelerPayPrice[intermodalCurrencyId] += intermodalPrice;
	}
	$.each(travelerPayPrice, function(key, value) {
		if(value > 0){
			priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			travelerPrice.push(priceObj);
		}
	});
	return travelerPrice;

}

/**
 * 获取游客同行价
 */
function getTravelerPeerPriceForYouJia(traveler) {
	var travelerPrice = [];
	var travelerPayPrice = new CurrencyMoney(currencyList);
	var priceObj = new Object();
	//获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();
	travelerPayPrice[srcPriceCurrency] += Number(src);
	//获取游客联运价格
	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
	if(intermodalType == "1"){
		var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
		var intermodalPrice = Number($("select[name=intermodalStrategy]",traveler).find("option:selected").val());
		travelerPayPrice[intermodalCurrencyId] += intermodalPrice;
	}
	$.each(travelerPayPrice, function(key, value) {
		if(value > 0){
			priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			travelerPrice.push(priceObj);
		}
	});
	return travelerPrice;
}


/**
 * 获取游客同行结算价
 */
function getTravelerJsPeerPriceForYouJia(traveler) {
	var travelerPrice = [];
	var travelerPayPrice = new CurrencyMoney(currencyList);
	var priceObj = new Object();
	//获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();
	travelerPayPrice[srcPriceCurrency] += Number(src);
	//获取游客联运价格
	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
	if(intermodalType == "1"){
		var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
		var intermodalPrice = Number($("select[name=intermodalStrategy]",traveler).find("option:selected").val());
		travelerPayPrice[intermodalCurrencyId] += intermodalPrice;
	}
	// 获取优惠价格
	var discountPrice = $(traveler).find("input[name='discountPrice']").val();
	travelerPayPrice[srcPriceCurrency] -= Number(discountPrice);

	$.each(travelerPayPrice, function(key, value) {
		if(value > 0){
			priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			travelerPrice.push(priceObj);
		}
	});
	return travelerPrice;
}

/**
 * 费用发生变化时修改单个游客成本价格
 * @param traveler 游客表单对象
 */
function changePayPriceByCostChange(traveler) {
	var travelerPrice = getTravelerPayPrice(traveler);
	var totalPrice = getOrderToltalPrice(travelerPrice, 1);			//各种币种相加的结算结果(币种名称)
	var travelerIndex = traveler.find(".travelerIndex").text();
	var travelerDiffPrice = travelerClearDiffArr[travelerIndex-1].diffPrice;  // 结算价差额
	var travelerClearPrice = calculatePriceByCurrency(travelerPrice, travelerDiffPrice, "ADDITION");  // 计算好的最终结算价
	var showClearPrice = showClearPriceInput(travelerClearPrice,1); //显示结算价输入框样式
	//如果游客已退团或转团，则成本价不计入订单成本价
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	if(changeGroupFlag != '3' && changeGroupFlag != '5') {
		travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
	}
	$("span[name=innerJsPrice]",traveler).html(totalPrice);
	if ($("#priceType").val() == "2" && showFlag) {
		$("span[name=clearPrice]",traveler).html(showClearPrice + "（含服务费）");
	} else {
		$("span[name=clearPrice]",traveler).html(showClearPrice);
	}
	$("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	changeTotalPrice();
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
 * 费用发生变化时修改单个游客同行价格/结算价
 */
function changePayPriceForYouJia(traveler) {
	var travelerJsPrice = getTravelerPayPriceForYouJia(traveler);
	var totalTravelerJsPrice = getOrderToltalPrice(travelerJsPrice, 1);
	var showClearPrice = showClearPriceInputForYouJia(travelerJsPrice,1); 		//显示结算价输入框样式
	$("span[name=clearPrice]",traveler).html(showClearPrice);
	changeTotalPrice();
}

/**
 * 修改游客同行价
 */
function changePeerPriceForYouJia(traveler) {
	var travelerPeerPrice = getTravelerPeerPriceForYouJia(traveler);
	var totalTravelerPeerPrice = getOrderToltalPrice4YouJia(travelerPeerPrice, 1);
	$("span[name=innerJsPrice]",traveler).html(totalTravelerPeerPrice);

	var totalTravelerJsPrice = getOrderToltalPrice(travelerPeerPrice, 1);
	$("span[name=jsPrice]",traveler).text(totalTravelerJsPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
}

/**
 * 修改游客结算同行价
 */
function changeJsPeerPriceForYouJia(traveler) {
	var travelerJsPeerPrice = getTravelerJsPeerPriceForYouJia(traveler);
	var totalTravelerJsPeerPrice = getOrderToltalPrice4YouJia(travelerJsPeerPrice, 1);
	$("span[name=peerJs]",traveler).html(totalTravelerJsPeerPrice);	// 同行结算价

	var discountPrice = $(traveler).find("input[name='discountPrice']").val();
	$("span[name='discountAmount']", traveler).html(discountPrice);	// 优惠减

}

/**
 * 获取游客的结算价
 * @param traveler 游客form表单
 */
function changeClearPriceByInputChange(traveler) {
	var travelerPrice = getTravelerClearPrice(traveler);	//获取游客的结算价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalClearPrice(travelerPrice, 1);//各种币种相加的结算结果
	travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerPrice;
	//填充单个游客信息收起显示的结算价格   add by  zhangcl
	$("span[name=travelerClearPrice]",traveler).html(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	changeTotalPrice();
}

function changeOtherTotalPrice(traveler) {
	//正常游客需要计算各种费用之和（其他费用、单房差、单价等）
	var travelerPrice = [];
	var travelerOtherPrice = new CurrencyMoney(currencyList);
	var priceObj = new Object();
	
	//获取其他费用
	travelerOtherPrice = addOtherCostFee(traveler, travelerOtherPrice);
	
	$.each(travelerOtherPrice, function(key, value) {
		if(value > 0){
			priceObj = new Object();
			priceObj.currencyId = key;
			priceObj.price = value;
			travelerPrice.push(priceObj);
		}
	});

	var totalPrice = getOrderToltalPrice4YouJia(travelerPrice, 1);	//各种币种相加的结算结果(币种标记)
	$(traveler).find("span.otherPrice").text(totalPrice);
	return totalPrice;
}

/**
 * 修改优惠价格
 */
function changeDiscountPrice(obj) {
	var money = obj.value;
	if(money=="") {obj.value=""};
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}

	var discountPrice = null;
	var personType = $(obj).parents("[name='travelerForm']").find(":radio[name='personType']:checked").val();
	if(personType == 1) {			// 成人
		discountPrice = $("#adultDiscountPrice").val();
	} else if (personType == 2) {	// 儿童
		discountPrice = $("#childDiscountPrice").val();
	} else if(personType == 3) {		// 特殊人
		discountPrice = $("#specialDiscountPrice").val();
	}

	if(Number($(obj).val()) > Number(discountPrice)) {
		$.jBox.tip("输入不能超过产品优惠额度，可以申请优惠!", "warning");
		$(obj).val(discountPrice);
	}

	//for 109 优惠
	var isForYouJia = $("[name='isForYouJia']").val();
	var activityKind = $("#activityKind").val();
	var isPeer = $("#priceType").val();

	if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
		changePayPriceForYouJia($(obj).closest("form"));		// 修改游客结算价
		changeJsPeerPriceForYouJia($(obj).closest("form"));		// 修改游客同行结算价
	} else {
		changePayPriceByCostChange($(obj).closest("form"));
	}

	changeClearPriceByInputChange($(obj).closest("form"));
}

/**
 * 展示单个游客成本价格和结算价格(修改订单会用到此方法)
 * @param traveler 游客表单对象
 * @param type 1:详情 2:修改
 */
function changePayPriceByCostChange_forDetail(traveler,type){
	//成本价信息
	var travelerPrice = getTravelerPayPrice(traveler);	//获取成本价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalPrice(travelerPrice, 1);//各种币种相加的结算结果
	var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
	if(changeGroupFlag != '3' && changeGroupFlag != '5') {
		travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
	}
	$("span[name=innerJsPrice]",traveler).html(totalPrice);
	$("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	
	//结算价信息
	showClearPriceForOrderUpdate(traveler,type);
	var travelerClearPrice = getTravelerClearPrice(traveler);	//获取游客的结算价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalClearPrice = getOrderToltalClearPrice(travelerClearPrice, 1);//各种币种相加的结算结果
	travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerClearPrice;
	$("span[name=travelerClearPrice]",traveler).text(totalClearPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	$("span[name=travelerClearPrice]",traveler).html(totalClearPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	if(!this.isYoujia){//如果是优加（需求号109），不执行此逻辑
		changeTotalPrice();
	}
}

/**
 * 拼接结算价展示信息
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
			var oneInput = "";
			if(i > 0) {
				oneInput = "+";
			}
			//订单详情页结算价展示方式
			if(type == 1){
				if(isYoujia){
					oneInput += "<span name='inputClearPriceDiv'>" + moneyObject.currencyMark + "" + milliFormat(moneyObject.price,1)+ "<input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" + "<input type='hidden' name='inputClearPrice'  maxlength='9' class='required ipt3' value='"+ moneyObject.price+"'  /></span></BR>";
				}else{
					oneInput += "<span name='inputClearPriceDiv'>" + moneyObject.currencyName + "" + milliFormat(moneyObject.price,1)+ "<input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" + "<input type='hidden' name='inputClearPrice'  maxlength='9' class='required ipt3' value='"+ moneyObject.price+"'  /></span></BR>";
				}

			}else{
				//订单修改页面结算价展示方式
				oneInput += "<span name='inputClearPriceDiv'>" + moneyObject.currencyName + ":" 
								+ "<input type='hidden' name='inputCurreyId' alt='" + moneyObject.currencyName + "' value='" + moneyObject.currencyId+"'/>"
								+ "<input type='hidden' name='inputClearPrice' value='" + moneyObject.price + "' />"
								+ moneyObject.price + "</span></BR>";
			}
			showInput += oneInput; 
		}
	}
	if (type == 1 && $("#priceType").val() == "2" && showFlag) {
		$("span[name=clearPrice]",traveler).html(showInput.replace(/<br>/g,"").replace(/<BR>/g,"") + "（含服务费）");
	} else {
		$("span[name=clearPrice]",traveler).html(showInput.replace(/<br>/g,"").replace(/<BR>/g,""));
	}
}

/**
 * 结算价手输时计算差额
 * @author yang.jiang 2016-3-29 15:18:34
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
			if (ms != money) {
				var txt = ms.split(".");
				obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
			}
		}else{
			obj.value = '0';
		}
	}
}

/**
 * 显示结算价输入框
 * @param priceArr 所有价格数组
 * @param type 需要显示的标记 1:每个游客需要显示的结算价格 2：订单总额显示的结算价格
 * @param currencyList 公共参数 当前数据库里的币种对象集合 包含id currencyName currencyMark
 * 
 * @return 返回字符串:如 人民币：<input type="text" /> + 美元：<input type="text" />
 */
function showClearPriceInput(priceArr,type){
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
			var oneInput = "";
			if(i > 0) {
				oneInput = "+";
			}
			oneInput += "<span name='inputClearPriceDiv'>" + moneyObject.currencyName + ":" 
							+ "<input type='hidden' name='inputCurreyId' alt='" + moneyObject.currencyName + "' value='" + moneyObject.currencyId+"'/>"
							+ "<input type='hidden' name='inputClearPrice' value='" + moneyObject.price + "' />"
							+ moneyObject.price + "</span></BR>";
			showInput += oneInput;
		}
	}
	return showInput;
}

/**
 * 显示结算价输入框
 */
function showClearPriceInputForYouJia(priceArr,type) {
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
			var oneInput = "";
			if(i > 0) {
				oneInput = "+";
			}
			oneInput += "<span name='inputClearPriceDiv'>" + moneyObject.currencyMark + moneyObject.price.toFixed(2) + "<input type='hidden' name='inputClearPrice' style='display:none;' maxlength='9' value='" + moneyObject.price.toFixed(2) + "'/>" + "<input type='hidden' name='inputCurreyId' alt='" + moneyObject.currencyName + "' value='" + moneyObject.currencyId + "'/></span>";
			showInput += oneInput;
		}
	}
	return showInput;
}

/**
 * 费用发生变化时修改订单总额
 */
function changeTotalPrice() {
	//计算此次订单填写的游客总人数
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	//获取添加的游客的table数组
	var travelerTables = $("#traveler form.travelerTable");
	
	var exitOrChangeNum = $("#traveler form.travelerTable input[name=changeGroupFlag][value=3]").length 
							+ $("#traveler form.travelerTable input[name=changeGroupFlag][value=5]").length;
	//获取添加的游客数量
	var travelerCount = travelerTables.length - exitOrChangeNum;
	//根据添加有游客人数显示添加游客按钮
	if (travelerCount >= parseInt(allPersonNum)) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}
	
	if (travelerCount > allPersonNum) {
		$("#orderPersonelNum").val(travelerTables.length);
	}

	var travelerPriceTotal = new Array();	//成本价
	var travelerClearPriceTotal = new Array();	//结算价
	// 未添加的游客同行价数组
	// 获取未添加游客的结算价格 人数*同行价
	if (Number(allPersonNum) - Number(travelerCount) > 0) {
		// 预订人数
		var adultNum = $("#orderPersonNumAdult").val();
		var childNum = $("#orderPersonNumChild").val();
		var specialNum = $("#orderPersonNumSpecial").val();
		//获取成人、儿童、特殊人群的同行价
		if(adultNum - countAdult() > 0){
			var crj = $("#crj").val()==""?0:$("#crj").val();
			var crCurrency = $('#crbz').val();
			var crPrice = Number(adultNum - countAdult()) * parseFloat(crj);
			var priceObj = new Object();
			priceObj.currencyId = crCurrency;
			priceObj.price = crPrice;
			travelerPriceTotal.push(priceObj);	//成本价
			travelerClearPriceTotal.push(priceObj);//结算价
		}
		if(childNum - countChild() > 0){
			var etj = $("#etj").val()==""?0:$("#etj").val();
			var etCurrency = $('#etbz').val();
			var etPrice = Number(childNum - countChild()) * parseFloat(etj);
			var priceObj = new Object();
			priceObj.currencyId = etCurrency;
			priceObj.price = etPrice;
			travelerPriceTotal.push(priceObj);//成本价
			travelerClearPriceTotal.push(priceObj);//结算价
		}
		if(specialNum - countSpecial() > 0){
			var tsj = $("#tsj").val()==""?0:$("#tsj").val();
			var tsCurrency = $('#tsbz').val();
			var tsPrice = Number(specialNum - countSpecial()) * parseFloat(tsj);
			var priceObj = new Object();
			priceObj.currencyId = tsCurrency;
			priceObj.price = tsPrice;
			travelerPriceTotal.push(priceObj);//成本价
			travelerClearPriceTotal.push(priceObj);//结算价
		}
	}
	//获取已添加游客的成本价格
	for(var i = 0; i < travelerTotalPriceArr.length; i++){
		var travelerJsPrice = travelerTotalPriceArr[i].jsPrice;
		for(var j = 0; j < travelerJsPrice.length; j++){
			travelerPriceTotal.push(travelerJsPrice[j]);
		}
	}
	var totalPrice = getOrderToltalPrice(travelerPriceTotal, 2);
	$("#travelerSumPrice").text(totalPrice);
	
	/**
	 * 获取已添加游客的结算价格
	 * 以下代码为新增
	 */
	if(travelerTotalClearPriceArr && travelerTotalClearPriceArr.length > 0){
		for(var i = 0; i < travelerTotalClearPriceArr.length; i++){
			var travelerClearPrice = travelerTotalClearPriceArr[i].travelerClearPrice;
			for(var j = 0; j < travelerClearPrice.length; j++){
				travelerClearPriceTotal.push(travelerClearPrice[j]);
			}
		}
	}
	// 订单总结算价
	var totalClearPrice = getOrderToltalClearPrice(travelerClearPriceTotal, 2);
	$("#travelerSumClearPrice").text(totalClearPrice);
	if ($("#priceType").val() == "2") {		
		// 订单总额
		if (showFlag) {
			$("#orderTotalPrice").text(getS4SFromArray(travelerClearPriceTotal, 1) + "（含服务费）");
		} else {
			$("#orderTotalPrice").text(getS4SFromArray(travelerClearPriceTotal, 1));
		}
		
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
 * 获取游客单一币种其他费用
 * @param travlerPrice
 */
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
 * 获取总额（quauq预定初始总同行价 + 服务费）
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
				totalPrice += totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
				paramCurrencyId.push(totalPriceArr[m].currencyId);
				paramCurrenctPrice.push(totalPriceArr[m].price);
			}
			else{
				if(type == 1){
					totalPrice += '<br>+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
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
 * for 109 优惠
 */
function getOrderToltalPrice4YouJia(priceArr,type){
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
				totalPrice += totalPriceArr[m].currencyMark + milliFormat(totalPriceArr[m].price,1);
				paramCurrencyId.push(totalPriceArr[m].currencyId);
				paramCurrenctPrice.push(totalPriceArr[m].price);
			}
			else{
				if(type == 1){
					totalPrice += '+' + totalPriceArr[m].currencyMark + milliFormat(totalPriceArr[m].price,1);
				}else{
					totalPrice += '+' + totalPriceArr[m].currencyMark + milliFormat(totalPriceArr[m].price,1);;
				}
				paramCurrencyId.push(totalPriceArr[m].currencyId);
				paramCurrenctPrice.push(totalPriceArr[m].price);
			}
		}
	}
	return totalPrice;
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

/**
 *游客是否需要联运 如果需要初始价格和联运区域
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
 *点击自备签国家
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
                $tips.show();  // 签证有效期
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
 * 改变游客数量
 */
function changeTravelerCount(){
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
		//如果是补位订单，则不校验余位，最后保存时后台会校验
		var groupCoverId = $("#groupCoverId").val();
		var groupCoverNum = $("#groupCoverNum").val();
		//如果是补位订单，则校验补位数与订单数是否一致，否则按原先流程判断
		if (!groupCoverId) {
			checkFreePosition();
		} else {
			validateCoverNum(groupCoverNum);
		}
		var deposit = $("#payDeposit").val();
		$("#frontMoney").val(deposit * travelerNum);
	});
}

/**
 * 限制特殊人群数量
 */
function checkMaxSpecialPeopleCount() {
	var flag = true;
	var orderPersonNumSpecialVal = parseInt($('#orderPersonNumSpecial').val());
	var maxPeopleCountVal = parseInt($("#maxPeopleCount").val());
	var lastPeopleCountVal = parseInt($("#lastPeopleCount").val());
	
	//发布产品时，若特殊人群人数不填写，则默认为0；当预定时，特殊人群不可以填写大于0的数字
//	if(!maxPeopleCountVal){
//		maxPeopleCountVal = 0;
//	}	
	
	if(maxPeopleCountVal != null && orderPersonNumSpecialVal > (maxPeopleCountVal-lastPeopleCountVal)) {
		$.jBox.tip('特殊人群最高人数为'+maxPeopleCountVal+'人，剩余：'+(maxPeopleCountVal-lastPeopleCountVal)+',请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
		flag = false;
	}
	return flag;
}

/**
 * 限制儿童人群数量
 */
function checkMaxChildrenCount() {
	var flag = true;
	var activityKind = $('#activityKind').val();
	if(activityKind != 10){
		var orderChildrenVal = parseInt($('#orderPersonNumChild').val());
		var maxChildrenCountVal = parseInt($("#maxChildrenCount").val());
		var lastChildrenCountVal = parseInt($("#lastChildrenCount").val());
		
		
		if(maxChildrenCountVal != null && orderChildrenVal > (maxChildrenCountVal-lastChildrenCountVal)) {
			$.jBox.tip('儿童最高人数为'+maxChildrenCountVal+'人，剩余：'+(maxChildrenCountVal-lastChildrenCountVal)+',请重新填写！', 'warning', { focusId: 'orderPersonNumChild' });
			flag = false;
		}
	}
	return flag;
}

/**
 * 	改变游客类型 成人1 儿童2 特殊人群3
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
		//for 109 优惠
		var isForYouJia = $("[name='isForYouJia']").val();
		var activityKind = $("#activityKind").val();
		var isPeer = $("#priceType").val();

		if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
			changePayPriceForYouJia(travelerForm);			// 改变游客结算价
			changePeerPriceForYouJia(travelerForm);			// 改变游客同行价
			changeJsPeerPriceForYouJia(travelerForm);		// 改变游客同行结算价

			if($(this).val() == 1) {			// 成人
				$(travelerForm).find(".adultDiscount").addClass("show").show();
				$(travelerForm).find(".childDiscount").removeClass("show").hide();
				$(travelerForm).find(".specialDiscount").removeClass("show").hide();
			} else if ($(this).val() == 2) {	// 儿童
				$(travelerForm).find(".childDiscount").addClass("show").show();
				$(travelerForm).find(".adultDiscount").removeClass("show").hide();
				$(travelerForm).find(".specialDiscount").removeClass("show").hide();
			} else if($(this).val() == 3) {		// 特殊人
				$(travelerForm).find(".specialDiscount").addClass("show").show();
				$(travelerForm).find(".adultDiscount").removeClass("show").hide();
				$(travelerForm).find(".childDiscount").removeClass("show").hide();
			}
			// 同时将游客优惠金额置为0
			$(travelerForm).find("[name='discountPrice']").val("0");
			changeDiscountPrice($(travelerForm).find("[name='discountPrice']")[0]);
		} else {
			changePayPriceByCostChange(travelerForm);
		}

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
 *点击是否需要联运
 */
function isIntermodal(obj){
    var $objParent = $(obj).parent().parent();
    if($(obj).val() == 0){
        $objParent.find('span').hide();
        changePayPriceByCostChange($objParent.closest("form"));
        changeClearPriceByInputChange($objParent.closest("form"));
    } else{
        $objParent.find('span').show();
        setIntermodal($objParent);
    }
}


/*预定第二步更改联运显示价格*/
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

//游客信息展开收起后显示姓名和结算价
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
        // add by zhangcl  结算价展示信息
        setOneTravelerClearPriceShow(curForm,2);
    } else {
        var curForm = obj_this.parent().parent();
        obj_this.addClass("boxCloseOnAdd");
        obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide(); 
        obj_this.parent().find('.tourist-t-off').css("display","inline");
//        var jsPrice = $("span[name=innerJsPrice]",curForm).html();
//        $("span[name=jsPrice]",curForm).text(jsPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
        
        // add by zhangcl  结算价展示信息
        setOneTravelerClearPriceShow(curForm,1);
        
        obj_this.parent().find('.tourist-t-on').hide();
        $('input[name=saveBtn]',travelerForm).hide();
        $('input[name=editBtn]',travelerForm).show();
        $(".ydbz_xwt").removeClass("displayClick");
    }
}

/**
 *修改单房差几晚
 */
function changeSumNight(obj){
    obj.value = obj.value.replace(/[^\d\+\-]/g,'');
	//for 109 优惠
	var isForYouJia = $("[name='isForYouJia']").val();
	var activityKind = $("#activityKind").val();
	var isPeer = $("#priceType").val();

	if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
		changePayPriceForYouJia($(obj.form));		// 修改游客结算价
	} else {
		changePayPriceByCostChange($(obj.form));
	}

    changeClearPriceByInputChange($(obj.form));  //add by zhangcl
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
	//验证身份证必填 如果需要联运身份证信息必填(自备签呢？)
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

	var visaTable = $(".zbqinfo .ydbz_scleft .table-visa",travelerForm);
	var trVisaInfo = $("tr[name=visainfo]",visaTable);
	var zbqVisaDate = $("input[name=zbqVisaDate]",travelerForm);
	var datavisa = [];
	for(var i = 0; i < trVisaInfo.length; i++){
		var trClassName = trVisaInfo[i].className;
		var td = $(trVisaInfo[i]).children('td');
		var visaInfo = {};
		visaInfo.applyCountryId = $("input[type=hidden]",td.eq(0)).val();
		if(zbqCountryArr.join(',').indexOf(trClassName) < 0){
			visaInfo.manorId = $("select[name=manor]",td.eq(1)).val();
			visaInfo.visaTypeId = $("select[name=visaType]",td.eq(2)).val();
			visaInfo.groupOpenDate = td.eq(3).text();
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
	//游客签证附件
	//游客费用
	var datacost = new Array();
	
	setOtherCostObjToArr($(travelerForm), datacost, null);
	
	var currencyId = paramCurrencyId.join(",");
	var currencyPrice = paramCurrenctPrice.join(",");

	var travelerCost = new Array();
	//保存游客其他费用
	if (flag) {
		var travelerForms = $("form[name=travelerForm]");
		$.each(travelerForms, function(index, obj) {
			var travelerId = $('input[name=travelerId]',$(this)).val();
			if (travelerId && travelerId != '') {
				setOtherCostObjToArr($(this), travelerCost, travelerId);
			}
		});
	}
	$(obj).attr("disabled","disabled");

	var rebatesMoney = $("input[name=rebatesMoney]",travelerForm).val();	//返佣费用金额
	var rebatesCurrencyId = $("select[name=rebatesCurrency]",travelerForm).val();	//返佣费用币种
	var orderPersonelNum = $("#orderPersonelNum").val();
	var newOrderFlag = $("#newOrderFlag").val();  // 是否是新生成的订单标识
	$.ajax({
		type : "POST",
		url : "../../traveler/manage/save",
		data : {
			travelerInfo: JSON.stringify($(travelerForm).serializeObject()),
			travelerCost: JSON.stringify(travelerCost),
			costs: JSON.stringify(datacost),
			visas: JSON.stringify(datavisa),
			orderType: orderType,
			payPrice: JSON.stringify(getTravelerClearPrice($(travelerForm))),	//结算价
			costPrice: JSON.stringify(getTravelerPayPrice($(travelerForm))),	//成本价
			currencyId : currencyId,
			currenctPrice : currencyPrice,
			rebatesMoney: rebatesMoney,
			rebatesCurrencyId : rebatesCurrencyId,
			orderPersonelNum : orderPersonelNum,
			newOrderFlag : newOrderFlag
		},
		success : function(data) {
			if (data) {
				if(data.result=="error"){
					$.jBox.tip(data.error);
					$(obj).removeAttr("disabled");
					return;
				}
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
		},
		error: function(e){
			$.jBox.tip("保存失败"+e);
			$(obj).removeAttr("disabled");
		}
	});

}

// S-147/109




/**
 * 保存游客信息，针对147和109需求，签证信息和游客优惠信息
 */
function saveTraveler4YouJia(obj, travelerForm, orderType) {
	
	
	
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
						canSaveTraveler4YouJia(obj, travelerForm, orderType);
					}
				});
			}
			if (flag && index == $("input[name=inputClearPrice]", travelerForm).length -1 ) {
				canSaveTraveler4YouJia(obj, travelerForm, orderType);
			}
		});
	} else {
		top.$.jBox.tip('游客结算价不能为空', 'error');
	}
}

function canSaveTraveler4YouJia(obj, travelerForm, orderType) {
	// 校验申请办签信息
	var result = validateVisaInfo(obj);
	if(!result) {
		return;
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

	//获取自备签签证国家及自备签有效期
	var visaTable = $(".zbqinfo .ydbz_scleft .table-visa",travelerForm);
	var trVisaInfo = $("tr[name=visainfo]",visaTable);
	var datavisa = [];
	for(var i = 0; i < trVisaInfo.length; i++){
		var trClassName = trVisaInfo[i].className;
		var td = $(trVisaInfo[i]).children('td');
		var visaInfo = {};
		visaInfo.applyCountryId =$(trVisaInfo[i]).find("[name='countrySelect']").val();
		visaInfo.manorId = $("select[name=manor]",td.eq(1)).val();
		visaInfo.visaTypeId = $("select[name=visaType]",td.eq(2)).val();
		visaInfo.groupOpenDate = td.eq(3).text();
		visaInfo.contractDate = $("input[type=text]",td.eq(4)).val();
		//需求号  0211 星辉四海  添加  预计反团时间 update by pengfei.shang=================================================================
//		visaInfo.groupBackDate = $("input[name=groupBackDate]",td.eq(5)).val();
		datavisa.push(visaInfo);
	}
	
	var currencyId = paramCurrencyId.join(",");
	var currencyPrice = paramCurrenctPrice.join(",");
	
	//游客费用
	var datacost = new Array();
	
	setOtherCostObjToArr($(travelerForm), datacost, null);
	
	var currencyId = paramCurrencyId.join(",");
	var currencyPrice = paramCurrenctPrice.join(",");

	var travelerCost = new Array();
	//保存游客其他费用
	if (flag) {
		var travelerForms = $("form[name=travelerForm]");
		$.each(travelerForms, function(index, obj) {
			var travelerId = $('input[name=travelerId]',$(this)).val();
			if (travelerId && travelerId != '') {
				setOtherCostObjToArr($(this), travelerCost, travelerId);
			}
		});
	}

	$(obj).attr("disabled","disabled");

	var rebatesMoney = $("input[name=rebatesMoney]",travelerForm).val();						//返佣费用金额
	var rebatesCurrencyId = $("select[name=rebatesCurrency]",travelerForm).val();				//返佣费用币种
	var orderPersonelNum = $("#orderPersonelNum").val();
	var discountPrice = $("input[name='discountPrice']", travelerForm).val(); 					//销售填写的优惠金额
	if(!discountPrice) {
		discountPrice = "";
	}
	var activityDiscountPrice = $.trim($("span.activityDiscountAmount:visible", travelerForm).text());	//团期优惠金额
	// 区分优佳奢华-散拼-同行价 优惠，其余情况走原有价格逻辑
	var activityKind = $("#activityKind").val();
	var isPeer = $("#priceType").val();
	var payPrice = new Array();
	var costPrice = new Array();
	if(activityKind == '2' && isPeer == '0') {
		payPrice = JSON.stringify(getTravelerPayPriceForYouJia($(travelerForm)));	//结算价;
		costPrice = JSON.stringify(getTravelerPeerPriceForYouJia($(travelerForm))); //成本价;
	} else {
		payPrice = JSON.stringify(getTravelerClearPrice($(travelerForm)));	//结算价
		costPrice = JSON.stringify(getTravelerPayPrice($(travelerForm)));	//成本价
	}
	
	$.ajax({
		type : "POST",
		url : "../../traveler/manage/save4YouJia",
		data : {
			travelerInfo: JSON.stringify($(travelerForm).serializeObject()),
			travelerCost: JSON.stringify(travelerCost),
			costs: JSON.stringify(datacost),
			visas: JSON.stringify(datavisa),
			orderType: orderType,
			payPrice: payPrice,	//结算价
			costPrice: costPrice,	//成本价
			currencyId : currencyId,
			currenctPrice : currencyPrice,
			rebatesMoney: rebatesMoney,
			rebatesCurrencyId : rebatesCurrencyId,
			orderPersonelNum : orderPersonelNum,
			productId : $("#productId").val(),
			productGroupId : $("#productGroupId").val(),
			activityKind : $("#activityKind").val(),
			agentId : $("#agentId").val(),
			groupHandleId : $("#groupHandleId").val(),
			discountPrice : discountPrice,
			activityDiscountPrice : activityDiscountPrice
		},
		success : function(data) {
			if (data!="") {
				if(data.traveler.id) {
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
			} else {
				$.jBox.tip("游客保存失败");
			}
			$(obj).removeAttr("disabled");
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

// E-147/109

/**
 * 设置单个游客结算价展示信息
 * @param traveler 游客对象
 * @returns type 1:游客信息隐藏 2:游客信息展示
 */
function setOneTravelerClearPriceShow(travelerForm,type){
	var jsPrice = getTravelerClearPrice(travelerForm);
    //隐藏游客信息时，结算价展示方式
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
    	//展示游客信息时，结算价展示方式          待优化
//    	var spanObj = $("span[name=clearPrice] span.disabledshowspan",travelerForm);
//    	if(spanObj && spanObj.length > 0){
//    		for(var i = 0; i < spanObj.length ; i++){
//    			var spanVal = spanObj.eq(i).text();
//    			spanObj.eq(i).removeClass("disabledshowspan");
//    			spanObj.eq(i).text(milliFormat(spanVal,1));
//    		}
//    	}
    }
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
    	$('.add_seachcheck',travelerForm).addClass('boxCloseOnAdd');
    }
    else{
    	$(obj).hide().removeClass("displayClick");
    	$(addcost).show();
    	$(deleltecost).show();
    	$(useall).show();
    	$(obj).parent().prev().show();
    	$('.tourist-t-off',travelerForm).css("display","none");
    	$('.tourist-t-on',travelerForm).show();
    	$('.add_seachcheck',travelerForm).removeClass('boxCloseOnAdd')
    	$('input[name=saveBtn]',travelerForm).show();
    	// 修改展开后，设置保存状态为未保存
    	var travelerIndex = $(travelerForm).find(".travelerIndex").text();
    	saveStatusArray[travelerIndex-1] = 0;
    }
}

/**
 * 照片上传公用方法
 */
 function inFileName(obj){
	var flag = $(obj).fileInclude({includes:[".doc",".docx"]});
	var dest = $(obj).parent().parent().find("input[name='fileLogo']")[0];
	if(flag){
	    var res = $(obj).val();             
	    $(dest).val(res);
	}else{
	    $(obj).val("");
	    $(dest).val("");
	    top.$.jBox.info("文件上传仅支持带有.doc,.docx后缀名的文件", "警告");
	    top.$('.jbox-body .jbox-icon').css('top','55px');
    }
}
 
//文件下载
function downloads(docid){
	window.open("/sys/docinfo/download/"+docid);
}

//文件下载
function downloads4SpecialDeman(docid) {
	var ctx = $("#ctx").val();
	window.open(ctx + "/sys/docinfo/download/"+docid);
}

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
			$(targetElement).parent().prev().remove();
			$(targetElement).parent().remove();
		}
	},{buttonsFocus:1});
}
    
/**
 * 附件上传回调方法
 * @param {Object} obj button对象
 * @param {Object} fileIDList  文件表id
 * @param {Object} fileNameList 文件原名称
 * @param {Object} filePathList 文件url
 */
function commenFunction(obj,fileIDList,fileNameList,filePathList){
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

/**
 * 修改预定人数设置订单总人数
 */
function changeorderPersonelNum(obj) {
	if($(obj).val() == ""){
		$(obj).val(0);
	}
	$("#orderPersonelNum").val(
		Number($("#orderPersonNumChild").val() == "" ? 0 : $("#orderPersonNumChild").val())
		+ Number(($("#orderPersonNumAdult").val() == "" ? 0 : $("#orderPersonNumAdult").val()))
		+ Number(($("#orderPersonNumSpecial").val() == "" ? 0 : $("#orderPersonNumSpecial").val()))).blur();
}

function countAdult() {
	var radios = $(".travelerTable input[name='personType'][value='1']:checked");
	$(radios).each(function(index, obj) {
		var notLength = $(this).parents(".travelerTable").find("input[name='changeGroupFlag'][value='3'],input[name='changeGroupFlag'][value='5']").length;
		if (notLength != 0) {
			$(this).remove();
		}
	});
	return radios.length;
}
function countChild() {
	var radios = $(".travelerTable input[name='personType'][value='2']:checked");
	$(radios).each(function(index, obj) {
		var notLength = $(this).parents(".travelerTable").find("input[name='changeGroupFlag'][value='3'],input[name='changeGroupFlag'][value='5']").length;
		if (notLength != 0) {
			$(this).remove();
		}
	});
	return radios.length;
}
function countSpecial() {
	var radios = $(".travelerTable input[name='personType'][value='3']:checked");
	$(radios).each(function(index, obj) {
		var notLength = $(this).parents(".travelerTable").find("input[name='changeGroupFlag'][value='3'],input[name='changeGroupFlag'][value='5']").length;
		if (notLength != 0) {
			$(this).remove();
		}
	});
	return radios.length;
}

/**
 * 根据出行人数添加游客信息
 */
function _addTravelCount(){
	//获取游客条数大于已存在的条数，则需要继续添加， 如果已添加多人则不需要重复添加
	var orderPersonNum = Number($("#orderPersonelNum").val());
	var travelerTables = $("#traveler form.travelerTable");
	var travelerCount = $("#traveler .tourist").size();
	if(oldOrderPersonNumAdult == 0 && oldOrderPersonNumChild == 0 && oldOrderPersonNumSpecial == 0){
		oldOrderPersonNumAdult = $("#orderPersonNumAdult").val();
		oldOrderPersonNumChild = $("#orderPersonNumChild").val();
		oldOrderPersonNumSpecial = $("#orderPersonNumSpecial").val();
		changeTotalPrice();
	}
	else if(oldOrderPersonNumAdult == $("#orderPersonNumAdult").val() 
			&& oldOrderPersonNumChild == $("#orderPersonNumChild").val() 
			&& oldOrderPersonNumSpecial == $("#orderPersonNumSpecial").val()){
		
	}else{
		if(travelerTables.length > 0){
			for(var i = 0; i < travelerTables.length; i++){
				var travelerForm = $(travelerTables[i]);
				var travelerId = $("input[name=travelerId]",travelerForm).val();
				if (travelerId != undefined && travelerId != '') {
					$.ajax({
						type : "POST",
						async: false, 
						url : "../../traveler/manage/deleteTraveler",
						data : {
							travelerId : travelerId
						},
						success : function(msg) {
							deleteTravelerAfter(travelerForm);
						}
					});
				}else{
					deleteTravelerAfter(travelerForm);
				}
			}
		}
		else{
			changeTotalPrice();
		}
		oldOrderPersonNumAdult = $("#orderPersonNumAdult").val();
		oldOrderPersonNumChild = $("#orderPersonNumChild").val();
		oldOrderPersonNumSpecial = $("#orderPersonNumSpecial").val();
	}
	
//	处理除了第一个游客其他游客内容收缩
//	var _add_seachcheck = $("#traveler .add_seachcheck");
//	for(var i = 1; i< _add_seachcheck.length;i++){
//		if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
//			_add_seachcheck.eq(i).click();
//		}
//	}
}

/**
 * 计算游客编号
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
					//for 109 优惠
					var isForYouJia = $("[name='isForYouJia']").val();
					var activityKind = $("#activityKind").val();
					var isPeer = $("#priceType").val();

					if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
						changePayPriceForYouJia(travelerForm);
					} else {
						changePayPriceByCostChange(travelerForm);
					}

				   changeClearPriceByInputChange(travelerForm);
			   }
			}
		});	
	}
}

/**
 * 设置生日控件最大日期为当前系统日期
 */
function dodatePicker() {
	var birthdays = $("#traveler input[name='birthDay']");
	var issuePlaces = $("#traveler input[name='issuePlace']");
	var passportValiditys = $("#traveler input[name='passportValidity']");
	var isForYoujia = $('[name=isForYouJia]:input').val();

	if(isForYoujia == "false") {
		$.each(birthdays, function(key, value) {
			$(value).datepicker( {
				maxDate : new Date()
			});
		});
		$.each(issuePlaces, function(key, value) {
			$(value).datepicker({
				maxDate : new Date()
			});
		});
		$.each(passportValiditys, function(key, value) {
			$(value).datepicker( {
				minDate : new Date()
			});
		});
	} else {
		$.each(birthdays, function(key, value) {
			$(value).datepicker( {
				maxDate : new Date(),
				dateFmt : 'dd/MM/yyyy'
			});
		});
		$.each(issuePlaces, function(key, value) {
			$(value).datepicker({
				maxDate : new Date(),
				dateFmt : 'dd/MM/yyyy'
			});
		});
		$.each(passportValiditys, function(key, value) {
			$(value).datepicker( {
				minDate : new Date(),
				dateFmt : 'dd/MM/yyyy'
			});
		});

	}

	$("#createDate").datepicker({
		maxDate : new Date()
	});
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
 * 验证特殊需求
 * @returns
 */
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
 * 验证剩余切位或者余位
 * @returns {Boolean}
 */
function checkFreePosition() {
	// 现在预订人数
	var orderPersonelNum = $("#orderPersonelNum").val();

	var groupId = $("#productGroupId").val();
	var freePosition = getGroupFreePosition(groupId);
	var leftpayReservePosition = $("#leftpayReservePosition").val();
	var placeHolderType = $("#placeHolderType").val();
	if (placeHolderType == 1) {
		// 表示为切位 用切位数去判断余位
		freePosition = leftpayReservePosition;
	}

	// 余位数
	// 现在订单人数
	var orderPosition = $("#orderPosition").val();

	if (orderPosition == "" || orderPosition == undefined || $("#preOrderId").val() != "") {
		orderPosition = 0;
	}

	if (Number(orderPosition) <= 0 && Number(orderPersonelNum) <= 0) {
		top.$.jBox.tip('出行人数必须大于零', 'error');
		return false;
	}
	
	//如果是游轮预定,不需要判断1/2人出行人数是否大于余位
	var activityKind = $("#activityKind").val();
	if(activityKind != "10"){
		if ((Number(freePosition) + Number(orderPosition) - Number(orderPersonelNum)) < 0) {
			// 如果有补位权限，则弹出申请补位弹出框，否则提示余位不足
			if ($("#hasCoverRight").val() == '1'){
				$.jBox.confirm("余位不足，是否发起补位申请?", "系统提示", function (v, h, f) {
					if (v == 'ok') {
						window.location.href = "../../groupCover/list/"+groupId;
					} else if (v == 'cancel') {}
				});
			} else {
				top.$.jBox.tip('余位数不足', 'error');
			}
			return false;
		}
	}
	
	return true;
}

/**
 * 根据团期ID查询团期余位数
 * @returns {Number}
 */
function getGroupFreePosition(groupId) {
	var freePosition = 0;
	$.ajax({
		type : "POST",
		async : false,
		url : "../../activity/manager/getGroupDetail",
		data : {
			groupId :groupId
		},
		success : function (data) {
			freePosition = data.num;
		}
	});
	return freePosition;
}

/**
 * 验证剩余切位或者余位
 * @returns {Boolean}
 */
function validateCoverNum(groupCoverNum) {
	// 现在预订人数
	var orderPersonelNum = $("#orderPersonelNum").val();
	
	// 余位数
	// 现在订单人数
	var orderPosition = $("#orderPosition").val();

	if (orderPosition == "" || orderPosition == undefined) {
		orderPosition = 0;
	}

	if (Number(orderPosition) <= 0 && Number(orderPersonelNum) <= 0) {
		top.$.jBox.tip('出行人数必须大于零', 'error');
		return false;
	}
	
	if ((Number(groupCoverNum) != Number(orderPersonelNum))) {
		top.$.jBox.tip('报名人数与补位人数不一致，请重新填写', 'error');
		return false;
	}
	
	return true;
}

/**
 * 生成右下角弹出的错误信息列表
 * @param errorList
 */
function createDivInTable(errorList) {
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
		var textTemp = $(valuein.element).parent().text()
				.replace(/[\:\：]/g, '');
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
		if($(valuein.element).attr("name") =="contactsName"){
			textTemp = "*渠道联系人";
		}
		if($(valuein.element).attr("name") =="contactsTel"){
			textTemp = "*渠道联系人电话";
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus()
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
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>")
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

//币种改变时
function changeCostCurrency(obj){
	//for 109 优惠
	var isForYouJia = $("[name='isForYouJia']").val();
	var activityKind = $("#activityKind").val();
	var isPeer = $("#priceType").val();

	if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
		changeOtherTotalPrice($(obj).closest("form"));		// 改变其他费用总和
		changePayPriceForYouJia($(obj).closest("form"));	// 改变结算价
	} else {
		changePayPriceByCostChange($(obj).closest("form"));
	}

	changeClearPriceByInputChange($(obj).closest("form"));  //add by zhangcl
}
//费用改变时
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
	//for 109 优惠
	var isForYouJia = $("[name='isForYouJia']").val();
	var activityKind = $("#activityKind").val();
	var isPeer = $("#priceType").val();
	if(isForYouJia == "true" && activityKind == '2' && isPeer == '0') {
		changeOtherTotalPrice($(obj).closest("form"));
		changePayPriceForYouJia($(obj).closest("form"));
	} else {
		changePayPriceByCostChange($(obj).closest("form"));
	}

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

//添加147
function addQz(obj) {
	var $currentUl = $(obj).parents('tbody[id=qztemplate]');
	var $newUl = $currentUl.clone();
	$newUl.find("#adddel").empty();
	$newUl.find("#adddel").append('<a class="add"  href="javascript:void(0)" onclick="addQz(this)">+</a>' +
			'<a class="del" href="javascript:void(0)" onclick="delQz(this)">-</a>');
	$currentUl.parents("table[class=table-visa]").append($newUl);
}

//删除147
function delQz(obj) {
	$(obj).parent().parent().parent().remove();
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

//var browserMatch = uaMatch(userAgent.toLowerCase());  
//if (browserMatch.browser) {  
//	browser = browserMatch.browser;  
//	version = browserMatch.version;
//}

//JS判断访问设备(userAgent)加载不同页面。代码如下：
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
