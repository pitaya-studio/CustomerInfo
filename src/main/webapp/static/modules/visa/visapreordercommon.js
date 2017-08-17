var flag = false;//是否要重新保存游客结算价（点击应用全部的时候，结算价不会保存）
var subtracFlag = true;//是否计算订单订金与游客
var subtracPriceArr = new Array();//是否计算订单订金与游客
var count = 0;
//拉美途uuid
var lameitourUuid = '7a81a26b77a811e5bc1e000c29cf2586';
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
	//如果不是修改或详情，则不计算订单总额与游客结算价差价
	var totalPriceJson = $("#totalPriceJson");
	if (totalPriceJson.length <= 0) {
		subtracFlag = false;
	}
    //游客是否需要联运 如果需要初始价格和联运区域
  //  ydbz2interradio();   //--wxw added ----------
    //游客自备签
   // ydbz2zibeiqian();   //--wxw added ----------
//	$("#mddtargetAreaNames").tooltip( {
//		track : true
//	});
	//添加游客点击事件
	$("#addTraveler").click(function() {
		var $table = $("#travelerTemplate").children();
		var _travelerForm = $table.clone().addClass("travelerTable");
		$("#traveler").append(_travelerForm);
		//默认添加游客信息时，判断什么游客类型
		var selFlag = false;
		var selJsPrice = 0;
		var selSrcPriceCurrency;
		var selSrcPriceCurrencyMark;
		var selSrcPriceCurrencyName;
		var personType = 0;
		if ($("#orderPersonNumAdult").val() >= countAdult()){
			selFlag = true;
			selJsPrice = $('#crj').val();
			selSrcPriceCurrency = $('#crbz').val();
			selSrcPriceCurrencyMark  = $('#crbzm').val();
			selSrcPriceCurrencyName = $('#crbmc').val();
			personType = 0;
		}
		//填充人员类型
		//$("input[name=personType]",_travelerForm)[personType].checked = true;
		//填充游客内部显示的结算价
//		$("span[name=innerJsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		var innerHtmlValue = "<div name='inputClearPriceDiv'>" + selSrcPriceCurrencyName + ":" + "<input type='hidden' name='inputCurreyId' alt='"+selSrcPriceCurrencyName+"' value='"+selSrcPriceCurrency+"' /><input type='text' name='inputClearPrice' class='required ipt3' maxlength='15' style='display: inline-block;' value='' onafterpaste='changeClearPriceSum(this)' onkeyup='changeClearPriceSum(this)' /></div>"
		$("span[name=clearPrice]",_travelerForm).html(innerHtmlValue);
		//填充游客内部显示的成本价
		$("span[name=innerJsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		//存放不同类型游客的同行价
		$("input[name=srcPrice]",_travelerForm).val(selJsPrice);
		//存放不同类型游客的同行价币种
		$("input[name=srcPriceCurrency]",_travelerForm).val(selSrcPriceCurrency);
		//填充单个游客信息收起显示的成本价格
		$("span[name=jsPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		//填充单个游客信息收起显示的结算价格
		$("span[name=travelerClearPrice]",_travelerForm).html(selSrcPriceCurrencyName + selJsPrice.toString().formatNumberMoney('#,##0.00'));
		//填充默认结算价
		$("input[name=inputClearPrice]",_travelerForm).attr("value", selJsPrice.toString());
		//组装每种游客同行价的对象
		var priceObj = new Object();
		priceObj.currencyId = selSrcPriceCurrency;
		priceObj.price =  selJsPrice;
		var priceObjArr = new Array();
		priceObjArr.push(priceObj);
		var travelerJsPrice = new Object();
		travelerJsPrice.jsPrice = priceObjArr;
		travelerTotalPriceArr.push(travelerJsPrice);
		//结算价
		var travelerClearPrice = new Object();
		travelerClearPrice.travelerClearPrice = priceObjArr;
		travelerTotalClearPriceArr.push(travelerClearPrice);
		//绑定生日控件
		dodatePicker();
		//计算游客编号
		recountIndexTraveler();
		// 填充游客默认姓名 （“游客  + index”）
		fillTravelerDefaultName();
		// 变更重新总结算价
		changeTotalPrice();
	});

	$("#orderPersonNumAdult").blur(function() {
		changeorderPersonelNum();
	});
	
	$("#orderPersonelNum").blur(function() {
		var travelerNum = $(this).val();
		
		checkFreePosition();
		var deposit = $("#payDeposit").val();
		$("#frontMoney").val(deposit * travelerNum);
		//changeTotalPrice();
	});

	//游客名称失去焦点时触发
/*	$("#traveler").delegate("input[name='travelerName']","blur", function() {
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
	});*/
	
	
	//删除游客时触发
	/*
	$("#traveler").delegate("a[name='deleteTraveler']", "click", function() {
		var $this = $(this);
		var travelerId = $(this).closest('.travelerTable').find("input.traveler[name='id']").val();
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				// 显示按钮
				$("#addTraveler").parent().show();
				if (travelerId == undefined) {
					// 无需记录
					var travlelerIndex = $this.closest('.travelerTable').find(".travelerIndex").text();
					travelerTotalPriceArr.splice(travlelerIndex-1,1);
					$this.closest('.travelerTable').remove();
					//记录游客删除的标记
					changeTotalPrice();
					//计算游客编号
					recountIndexTraveler();
				} else {
					// 需要删除数据库中记录
					$.ajax( {
						type : "POST",
						url : "../../orderCommon/manage/deleteTraveler",
						data : {
							travelerId : travelerId
						},
						success : function(msg) {
							top.$.jBox.tip('删除成功', 'success');
							$this.closest('.travelerTable').remove();
							changeTotalPrice();
						}
					});
				}
			} else if (v == 'cancel') {
			}
		});
	});
	*/

	//删除其他费用时触发
	$("#traveler").delegate("a[name='deleltecost']", "click", function() {
		var $this = $(this);
		var costId = $this.closest(".cost").find("input[name='costid']").val();
		$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
			if (v == 'ok') {
				if (costId != null && costId != "" && costId != undefined) {
					$.ajax( {  //目前的设计此处不会被调用到
						type : "POST",
						url :g_context_url+ "/orderCommon/manage/deleteCost",
						data : {
							costId : costId
						},
						success : function(msg) {
							top.$.jBox.tip('删除成功', 'success');
							//$this.closest(".cost").find("input[name='sum']").remove();
							$this.closest(".cost").remove();
							changePayPriceByCostChange($this.closest("form"));
							
						}
					});
				} else {
					top.$.jBox.tip('删除成功', 'success');
					var travelerForm = $this.closest("form");
					//$this.closest(".cost").find("input[name='sum']").remove();
					$this.closest(".cost").remove();
					changePayPriceByCostChange(travelerForm);
				}
			} else if (v == 'cancel') {

			}
		});
	});
	
	$("#traveler").delegate("input[name='sum']", "click", function() {
		var _readonly = $(this).attr("readonly");
		if (_readonly != "" && _readonly != undefined) {
			return false;
		} else {
			var _tempVal = $(this).val();
			if (_tempVal == "0") {
				$(this).css("color", "");
				$(this).val("");
			}
		}
	});
	
	// 改变游客类型时 成人1 儿童2 特殊人群3
	$("#traveler").delegate("input[name^='personType']","change",function() {
		if ($(this).val() == 2) {
			$(this).closest("form").find("input[name='srcPrice']").val($("#etj").val());
			$(this).closest("form").find("input[name='srcPriceCurrency']").val($("#etbz").val());
		} else if ($(this).val() == 1) {
			$(this).closest("form").find("input[name='srcPrice']").val($("#crj").val());
			$(this).closest("form").find("input[name='srcPriceCurrency']").val($("#crbz").val());
		} else {
			$(this).closest("form").find("input[name='srcPrice']").val($("#tsj").val());
			$(this).closest("form").find("input[name='srcPriceCurrency']").val($("#tsbz").val());
		}
		changePayPriceByCostChange($(this).closest("form"));
	});
	
	//改变住房要求时触发
	$("#traveler").delegate("select[name='hotelDemand']", "change", function() {
		if ($(this).val() == 1) {
			$(this).closest("form").find("input[name='singleDiff']").val($("#singleDiff").val());
			$(this).closest("form").find("input[name='singleDiff']").prev().text($("#singleDiff").val().toString().formatNumberMoney('#,##0.00'));
		} else {
			$(this).closest("form").find("input[name='singleDiff']").val(0);
			$(this).closest("form").find("input[name='singleDiff']").prev().text(0);
		}
		changePayPriceByCostChange($(this).closest("form"));
	});
	
	$(".closeOrExpand").click(function() {
		var obj_this = $(this);
		if (obj_this.attr("class").match("ydClose")) {
			obj_this.removeClass("ydClose");
			obj_this.parent().next("[flag=messageDiv]").eq(0).show();
		} else {
			// 隐藏前需要先判断是否必填了
			outerrorList = new Array();
			_$form = obj_this.parent().next().find("form");
			var validateForm = _$form.validate({
				showErrors : function(errorMap, errorList) {
					this.defaultShowErrors();
					outerrorList = outerrorList.concat(errorList);
				}
			});
			if(validateForm != null){
				if (validateForm.form()) {
					obj_this.addClass("ydClose");
					obj_this.parent().next("[flag=messageDiv]").eq(0).hide();
					// obj_this.parent().next().next(".orderPersonMsg").show();
				} else {
					createDivInDiv(outerrorList);
					return false;
				}
			}
		}
	});
});

/**
 * 获取游客的结算价格
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
/*
function getTravelerPayPrice(traveler){
	var travelerPrice = [];
	var priceObj = new Object();
	//获取其他费用
	var otherCostObj = $(".payfor-otherDiv .payfor-other",traveler);
	$.each(otherCostObj, function(key, value) {
		priceObj = new Object();
		priceObj.currencyId = $(value).find("select[name='currency']").val();
		//alert($(value).find("input[name='sum']").val())

		priceObj.price =  $(value).find("input[name='sum']").val();
		//alert(priceObj.price)
		travelerPrice.push(priceObj);
	});
	
	//获取游客单价
	var src = traveler.find("input[name='srcPrice']").val();
	var srcPriceCurrency = traveler.find("input[name='srcPriceCurrency']").val();
	priceObj = new Object();
	priceObj.currencyId = srcPriceCurrency;
	priceObj.price = src;
	travelerPrice.push(priceObj);
	
	//获取游客联运价格
	var intermodalType = $("input[name=travelerIntermodalType]:checked",traveler).val();
    if(intermodalType == "1"){
    	var intermodalCurrencyId = $("select[name=intermodalStrategy]",traveler).find("option:selected").attr("priceCurrencyId");
    	var intermodalPrice = $("select[name=intermodalStrategy]",traveler).find("option:selected").val();
    	priceObj = new Object();
		priceObj.currencyId = intermodalCurrencyId;
		priceObj.price = intermodalPrice;
		travelerPrice.push(priceObj);
    }
    return travelerPrice;
}
*/

/**
 * 填充新添加游客的默认姓名（游客 + index）
 */
function fillTravelerDefaultName(){
	// 只给拉美途做403功能
	if ($("#companyUuid").val() == lameitourUuid) {
		var travelerTables = $("#traveler form");
		var $thisTraveler = $(travelerTables[travelerTables.length - 1]);
		var tempName = "游客" + travelerTables.length;
		// 填充输入框姓名
		$thisTraveler.find("input[name=travelerName]").val(tempName);
		// 填充展示span姓名
		$thisTraveler.find("span[name=tName]").html(tempName);
	}
}

/**
 * 费用发生变化时修改单个游客结算价格
 * @param traveler 游客表单对象
 */
function changePayPriceByCostChange(traveler) {
	var travelerPrice = getTravelerPayPrice(traveler);
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalPrice(travelerPrice, 1);//各种币种相加的结算结果
	var showClearPrice = showClearPriceInput(travelerPrice,1); //显示结算价输入框样式
	travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
	//销售签证订单修改页面提示有误所以删除  万磊
	//if (totalPrice == '') {
	//	top.$.jBox.tip('游客结算价不可小于0', 'error');
	//	return;
	//}
	$("span[name=innerJsPrice]",traveler).html(totalPrice);
	$("span[name=clearPrice]",traveler).html(showClearPrice);
	changeClearPriceByInputChange(traveler);
	$("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	changeTotalPrice();
}

/**
 * 费用发生变化时修改订单总额
 */
function changeTotalPrice() {
	var payStatus = $("#payStatus").val();
	//计算此次订单填写的游客总人数
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	
	//获取添加的游客的table数组
	var travelerTables = $("#traveler form.travelerTable");
	//获取添加的游客数量
	var travelerCount = travelerTables.length;
	//根据添加有游客人数显示添加游客按钮
	if (travelerCount >= allPersonNum) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}

	//if (travelerCount > allPersonNum) {
		// $("#orderPersonelNum").val(travelerTables.length);
	//}
	
	var travelerPriceTotal = new Array();
	// 未添加的游客同行价数组
	var noTravelerPrice = new Array();
	// 获取未添加游客的结算价格 人数*同行价
	if (Number(allPersonNum) - Number(travelerCount) > 0) {
		// 预订人数
		var adultNum = $("#orderPersonNumAdult").val();
		var childNum = $("#orderPersonNumChild").val();
		var specialNum = $("#orderPersonNumSpecial").val();
		//获取成人、儿童、特殊人群的同行价
		if(adultNum - countAdult() > 0){
			var crj = $("#crj").val();
			var crCurrency = $('#crbz').val();
			var crPrice = Number(adultNum - countAdult()) * parseFloat(crj);
			var priceObj = new Object();
			priceObj.currencyId = crCurrency;
			priceObj.price = crPrice;
			//noTravelerPrice.push(priceObj);
			travelerPriceTotal.push(priceObj);
		}
		
	}
	var travelerPrice = new Array();
	//获取已添加游客的结算价格
	for(var i=0; i < travelerTotalPriceArr.length; i++){
		var travelerJsPrice = travelerTotalPriceArr[i].jsPrice;
		for(var j=0;j<travelerJsPrice.length;j++){
			//travelerPrice.push(travelerJsPrice[j]);
			travelerPriceTotal.push(travelerJsPrice[j]);
		}
	}
	if (subtracFlag) {
		//求出结算价和订单总额差价：币种id和币种金额
		subtracPriceArr = initSubtract(travelerPriceTotal);
	}
	var totalPriceArr = travelerPriceTotal.concat(subtracPriceArr);
	var totalPrice = getOrderToltalPrice(travelerPriceTotal, 2);
	$("#travelerSumPrice").text(totalPrice);
	
		/**
	 * 获取已添加游客的结算价格
	 * 以下代码为新增
	 */
	var travelerClearPriceTotal = new Array();
	for(var i = 0; i < travelerTotalClearPriceArr.length; i++){
		var travelerClearPrice = travelerTotalClearPriceArr[i].travelerClearPrice;
		for(var j = 0; j < travelerClearPrice.length; j++){
			travelerClearPriceTotal.push(travelerClearPrice[j]);
		}
	}
//	if (subtracFlag) {
//		//求出结算价和订单总额差价：币种id和币种金额
//		subtracClearPriceArr = initSubtract(travelerClearPriceTotal);
//	}
//	var totalClearPriceArr = travelerClearPriceTotal.concat(subtracClearPriceArr);
	var totalClearPrice = getOrderToltalClearPrice(travelerClearPriceTotal, 2);
	$("#travelerSumClearPrice").text(totalClearPrice);
	return totalPrice;
}

/**
 * 不同币种的金额统计
 * @param priceArr 所有价格数组
 * @param type 需要显示的标记 1:每个游客需要显示的结算价格 2：订单总额显示的结算价格
 * @return 返回统计字符串
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
 *游客是否需要联运 如果需要初始价格和联运区域
 */
function ydbz2interradio(){
    var obj=$('.tourist-t-r');
    $(obj).each(function() {
      var value = $(this).find('input:checked').val();
      if("2" == value){
           $(this).find('span').show();
           setIntermal($(this));
       } else{
           $(this).find('span').hide();
       }
    });
}

/**
 *点击自备签国家
 */
function ydbz2zibeiqian(){
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
                }
            });
            tr.show();
        }
    });
}

/**
 *点击是否需要联运
 */
function isIntermodal(obj){
    var $objParent = $(obj).parent().parent();
    if($(obj).val() == 0){
        $objParent.find('span').hide();
        changePayPriceByCostChange($objParent.closest("form"));
    } else{
        $objParent.find('span').show();
        setIntermal($objParent);
    }
}


/*预定第二步更改联运显示价格*/
function setIntermal(selObj){
    var value=selObj.find("option:selected").val();
    var text=selObj.find("option:selected").text();
    var intermodalId = selObj.find("option:selected").attr("intermodalId");
    var priceCurrency = selObj.find("option:selected").attr("priceCurrency");
    selObj.parent().find('label[name=intermodalPrice]').html(value);
    selObj.parent().find('input[name=intermodalGroupPart]').val(text);
    selObj.parent().find('input[name=intermodalId]').val(intermodalId);
    selObj.parent().find('label[name=priceCurrency]').html(priceCurrency);
    changePayPriceByCostChange(selObj.closest("form"));
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
        if($('input[name=travelerName]',travelerForm).prop("disabled")){
        	$('input[name=saveBtn]',travelerForm).hide();
            $('input[name=editBtn]',travelerForm).show();
        }else{
        	$('input[name=saveBtn]',travelerForm).show();
            $('input[name=editBtn]',travelerForm).hide();
        }
    } else {
        var curForm = obj_this.parent().parent();
        obj_this.addClass("boxCloseOnAdd");
        obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide(); 
        obj_this.parent().find('.tourist-t-off').css("display","inline");
        var jsPrice = $("span[name=innerJsPrice]",curForm).html();
        $("span[name=jsPrice]",curForm).text(jsPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
        obj_this.parent().find('.tourist-t-on').hide();
        $('input[name=saveBtn]',travelerForm).hide();
        $('input[name=editBtn]',travelerForm).show();
    }
}
/**
 *渠道变化触发
 */
function changeAgent(option){
    var optionVal = option.value;
    var optionText = option.text;
    //给隐藏域赋渠道信息
    $('#orderCompany').val(optionVal);
    $('#orderCompanyName').val(optionText);
    $('#agentId').val(optionVal);
    $('#agentcontact').html('');
    if(optionVal !=-1){
        $.ajax({
            type : "POST",
            url : "../../orderCommon/manage/getAgentContacts",
            data : {
                agentId:optionVal
            },
            success : function(data) {
                 if(data!=null){
                    $.each(data,function(key,val){
                        creatContacts(val.contactsName,val.contactsTel,val.contactsTixedTel,val.contactsAddress,val.contactsFax,val.contactsQQ,val.contactsEmail,val.contactsZipCode);
                    });
                 }
            },
            error: function(e){
                $.jBox.tip("失败"+e);
            }
        });
    }
    
}
/**
 * 渠道拼接联系人div
 */
function creatContacts(contactsName,contactsTel,contactsTixedTel,contactsAddress,contactsFax,contactsQQ,contactsEmail,contactsZipCode){
    $('#agentcontact').append('<ul class="ydbz_qd" name="orderpersonMes">'+
    '<li><label>渠道联系人：</label><input type="text" class="required" value="'+contactsName+'"  disabled="disabled"/></li>'+
    '<li><label>渠道联系人电话：</label><input type="text" class="required" value="'+contactsTel+'"  disabled="disabled"/></li>'+
    '<li><label>固定电话：</label><input type="text" class="required" value="'+contactsTixedTel+'"  disabled="disabled"/></li>'+
    '<li><label>渠道地址：</label><input type="text" class="required" value="'+contactsAddress+'"  disabled="disabled"/></li>'+
    '<li><label>传真：</label><input type="text" class="required" value="'+contactsFax+'"  disabled="disabled"/></li>'+
    '<li><label>QQ：</label><input type="text" class="required" value="'+contactsQQ+'"  disabled="disabled"/></li>'+
    '<li><label>Email：</label><input type="text" class="required" value="'+contactsEmail+'"  disabled="disabled"/></li>'+
    '<li><label>渠道邮编：</label><input type="text" class="required" value="'+contactsZipCode+'"  disabled="disabled"/></li>'+
    '</ul>')
}

/**
 *修改单房差几晚
 */
function changeSumNight(obj){
    obj.value = obj.value.replace(/[^\d\+\-]/g,'');
    changePayPriceByCostChange($(obj.form));
}

//保存游客信息
function saveTraveler(obj,travelerForm,orderType){
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
   var $siblingsCkb = $(".tourist-ckb input[type=checkbox]",travelerForm);
   var visaTable = $(".zbqinfo .ydbz_scleft .table-visa",travelerForm);
   var zbqVisaDateArr = [];
   var zbqCountryArr = [];
   for(var i=0; i < $siblingsCkb.length; i++){
       var chk = $siblingsCkb[i];
       if(chk.checked){
           zbqCountryArr.push(chk.value);
       }
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
   if(zbqCountryArr.length > 0){
        if(idCard==""){
            top.$.jBox.tip('自备签证时，身份证信息必填', 'error');
            return false;
        }
   }
   //获取游客费用
   var costtr = $('.cost',$(travelerForm));
   var datacost = new Array();
   $.each(costtr, function(keycost, valuecost) {
       var costs = {};
       var costinputs = $(valuecost).find("input");
       $.each(costinputs, function(keyin, valuein) {
           costs[$(valuein).attr("name")] = $(valuein).val();
       });
       var costselects = $(valuecost).find("select");
       $.each(costselects, function(keyin, valuein) {
           costs[$(valuein).attr("name")] = $(valuein).val();
       });
       datacost.push(costs);
   });
   $.ajax({
        type : "POST",
        url : "../../traveler/manage/save",
        data : {
            traveler:JSON.stringify($(travelerForm).serializeObject()),
            zbqCountry:zbqCountryArr.join(','),
            costs:JSON.stringify(datacost),
            orderType:orderType,
            moneyType:$('#payStatus').val(),
            payPrice:JSON.stringify(getTravelerPayPrice($(travelerForm)))
        },
        success : function(data) {
             if(data!=""){
                //设置表单ID
                $('input[name=travelerId]',travelerForm).val(data.id);
                saveTravelerAfter(obj,travelerForm,"save");
                $.jBox.tip("游客保存成功");
             }
             else{
                $.jBox.tip("游客保存失败");
             }
        },
        error: function(e){
            $.jBox.tip("保存失败"+e);
        }
    });
}
/**
 * 获取游客的结算价格
 * @param traveler 游客对象
 * @returns {Array} 结算价数组
 */
function getTravelerClearPrice(traveler){
	var travelerPrice = [];
	$("div[name=inputClearPriceDiv]",traveler).each(function(index,element){
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
    var input=$(travelerForm).find("input");
    var textarea=$(travelerForm).find("textarea");
    var selects=$(travelerForm).find("select");
    if(type == "save"){
        $(input).attr("disabled","disabled");
        $(textarea).attr("disabled","disabled");
        $(selects).attr("disabled","disabled");
	    var sums = $("input[name='sum']",travelerForm);
	    $.each(sums, function(key, value) {
	        var _$span = $(value).next();
	        _$span.text(_$span.text());
	        ;
	    });
	}
    else{
    	$(input).removeAttr("disabled","disabled");
    	$(textarea).removeAttr("disabled","disabled");
    	$(selects).removeAttr("disabled","disabled");
    }
    //添加费用
    var addcost=$("a[name='addcost']",travelerForm);
    //删除            
    var deleltecost=$("a[name='deleltecost']",travelerForm);
    //应用全部          
    var useall=$("a[name='bjyyqb']",travelerForm);
    if(type == "save"){
       $(obj).hide();
       $(addcost).hide();
       $(deleltecost).hide();
       $(useall).hide();
       $('input[name=editBtn]',travelerForm).show();
       $(obj).parent().prev().hide();
       $('.tourist-t-off',travelerForm).css("display","inline");
       $('.tourist-t-on',travelerForm).hide();
       $('.add_seachcheck',travelerForm).addClass('boxCloseOnAdd')
    }
    else{
       $(obj).hide();
       $(addcost).show();
       $(deleltecost).show();
       $(useall).show();
       $(obj).parent().prev().show();
       $('.tourist-t-off',travelerForm).css("display","none");
       $('.tourist-t-on',travelerForm).show();
       $('.add_seachcheck',travelerForm).removeClass('boxCloseOnAdd')
       $('input[name=saveBtn]',travelerForm).show();
    }
    $('input[name=editBtn]',travelerForm).removeAttr("disabled","disabled");
    $('input[name=saveBtn]',travelerForm).removeAttr("disabled","disabled");
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
    
/**
 * 附件上传回调方法
 * @param {Object} obj button对象
 * @param {Object} fileIDList  文件表id
 * @param {Object} fileNameList 文件原名称
 * @param {Object} filePathList 文件url
 */
function commenFunction(obj,fileIDList,fileNameList,filePathList){
    //  var pathBase = "file:///F:/apache-tomcat-7.0.54/bin/";
    //  var newfilePath = filePathList.replace(/\\/g,"/");
    //      newfilePath = newfilePath.replace(";","");
        var name = obj.name;
        $("#"+name).remove();
        $(obj).prev().val(fileNameList);
        $(obj).parent().append('<div style="display: none" id="'+obj.name+'"></div>');
        $("#"+name).append("<input type='hidden' name="+name+"docID value='" +fileIDList.replace(";","")+ "' />")
                    .append("<input type='hidden' name="+name+"docName value='" +fileNameList.replace(";","")+ "' />")
                    .append("<input type='hidden' name="+name+"docPath value='" +filePathList.replace(";","")+ "' />");
        $(obj).prev().show();
         
    //  ydbz2interfile(obj,pathBase+newfilePath);
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
function changeorderPersonelNum() {
	$("#orderPersonelNum").val(
		Number($("#orderPersonNumAdult").val())).blur();
}

function countAdult() {
	//var radios = $("#traveler input[name='personType'][value=1]:checked");
	var radios = $("#traveler form");
	return radios.length;
}
function countChild() {
	var radios = $("#traveler input[name='personType'][value=2]:checked");
	return radios.length;
}
function countSpecial() {
	var radios = $("#traveler input[name='personType'][value=3]:checked");
	return radios.length;
}

/**
 * 根据出行人数添加游客信息
 */
function _addTravelCount(){
	//获取游客条数大于已存在的条数，则需要继续添加， 如果已添加多人则不需要重复添加
	var orderPersonNum = Number($("#orderPersonelNum").val());
	var travelerCount = $("#traveler .tourist").size();
	if(travelerCount < orderPersonNum){
		for(var i = 0; i < orderPersonNum - travelerCount; i++){
			$("#addTraveler").click();
		}
		//计算总价格
		changeTotalPrice();
	}
	//处理除了第一个游客其他游客内容收缩
	var _add_seachcheck = $("#traveler .add_seachcheck");
	for(var i = 1; i< _add_seachcheck.length;i++){
		if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
			_add_seachcheck.eq(i).click();
		}
	}
}

/**
 * 计算游客编号
 */
/*
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
*/

/**
 * 费用报价应用全部
 */
function useAllPrice(obj){
    var travelerFormList = $("#traveler form[name=travelerForm]");
    var saveFlag = false;
    for(var i=0; i < travelerFormList.length; i++){
       travelerForm = $(travelerFormList[i]);
       var travelerId = $("input[name='travelerId']",travelerForm).val();
       if(travelerId != ""){
           saveFlag = true;
       }
    }
    var curTravelerForm = $(obj).closest("form[name=travelerForm]");
    var hotelDemandVal = $("select[name=hotelDemand]",curTravelerForm).val();
	var costSelect=$(".payfor-otherDiv select",curTravelerForm);
	var CostSelectData = {};
	$(costSelect).each(function(index, element) {
		CostSelectData[index] = $(element).val();
    });
    var cloneDiv = $(".bj-info",curTravelerForm).clone(true);
    $("#traveler .bj-info").remove();
    $("#traveler .tourist-right").prepend(cloneDiv);
    var travelerForm = null;
    for(var i=0; i < travelerFormList.length; i++){
       travelerForm = $(travelerFormList[i]);
       $("select[name=hotelDemand]",travelerForm).val(hotelDemandVal);
	   $(".payfor-otherDiv select",travelerForm).each(function(index, element) {
          var elseCostsel=CostSelectData[index];
		  $(element).find("option").each(function(index, element){
		   if($(this).val()==elseCostsel){
		     $(this).attr("selected","selected");
		    }else{
			$(this).removeAttr("selected");
			}
		  })
		})
       var formState = $(".rightBtn .btn",travelerForm).text();
       if(formState != "保存"){
         $(".bj-info",travelerForm).find("a[name='deleltecost']").hide();
         $(".bj-info",travelerForm).find(".yd-total a").hide();
         $(".bj-info",travelerForm).find(".btn-addBlue").hide();
         $(".bj-info",travelerForm).find("input").attr("disabled","disabled");
         $(".bj-info",travelerForm).find("select").attr("disabled","disabled");
       }
	   changePayPriceByCostChange(travelerForm);
   }
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
}

/**
 * 验证渠道联系人及订单联系人
 * @returns
 */
/*
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
*/

/**
 * 验证特殊需求
 * @returns
 */
/*
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
*/

/**
 * 验证游客
 * @returns
 */
/*
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
*/

/**
 * 验证剩余切位或者余位
 * @returns {Boolean}
 */
/*
function checkFreePosition() {
	// 现在预订人数
	var orderPersonelNum = $("#orderPersonelNum").val();

	var freePosition = $("#freePosition").val();
	var leftpayReservePosition = $("#leftpayReservePosition").val();
	var placeHolderType = $("#placeHolderType").val();
	if (placeHolderType == 1) {
		// 表示为切位 用切位数去判断余位
		freePosition = leftpayReservePosition;
	}

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

	if ((Number(freePosition) + Number(orderPosition) - Number(orderPersonelNum)) < 0) {
		top.$.jBox.tip('余位数不足', 'error');
		return false;
	}
	return true;
}

*/
/**
 * 生成右下角弹出的错误信息列表
 * @param errorList
 */
/*
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
*/

/*
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
*/

/*
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
*/
//币种改变时-----
function changeCostCurrency(obj){
	changePayPriceByCostChange($(obj).closest("form"));
}
//费用改变时-----
//var stmp="";
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
 * 获取游客的结算价
 * @param traveler
 */
function changeClearPriceByInputChange(traveler) {
	var travelerPrice = getTravelerClearPrice(traveler);	//获取游客的结算价
	var travelerIndex = traveler.find(".travelerIndex").text();
	var totalPrice = getOrderToltalClearPrice(travelerPrice, 1);//各种币种相加的结算结果
	travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerPrice;
	if (totalPrice == '') {
		top.$.jBox.tip('游客结算价不可小于0', 'error');
		return;
	}
	//$("span[name=travelerClearPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	//填充单个游客信息收起显示的结算价格   add by  zhangcl
	$("span[name=travelerClearPrice]",traveler).html(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
	changeTotalPrice();
}

//其它费用改变时
function changeClearPriceSum(obj){
   var money = obj.value;  
   var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
   changeClearPriceByInputChange($(obj).closest("form"));
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
			var currencyPrice=moneyObject.price;
			for(var j = 0 ; j < currencyList.length ; j++){
				var currencyObj = currencyList[j];
				if(currencyObj.id == moneyObject.currencyId){
					moneyObject.currencyName = currencyObj.currencyName;
					moneyObject.currencyMark = currencyObj.currencyMark;
					break;
				}
			}
			var oneInput = "<div name='inputClearPriceDiv'>" + moneyObject.currencyName + ":" + "<input type='hidden' name='inputCurreyId' alt='"+moneyObject.currencyName+"' value='"+ moneyObject.currencyId+"'/>" + "<input type='text' name='inputClearPrice'  maxlength='15' class='required ipt3' value='"+ currencyPrice+"' onafterpaste='changeClearPriceSum(this)' onkeyup='changeClearPriceSum(this)' /></div><br/>";
			showInput += oneInput; 
		}
	}
	return showInput;
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
		if(""==priceObj.price){//处理钱数为空出现非法数值错误的问题
			priceObj.price=0;
		}

		for(var j = 0; j < currencyList.length; j++){
			var currency = currencyList[j];
			if(priceObj.currencyId == currency.id){
				if(totalPriceArr[j] == undefined){
					var priceTotalObject = new Object();
					if(parseFloat(priceObj.price) > 0){
						priceTotalObject.price = parseFloat(priceObj.price);
						priceTotalObject.currencyId = currency.id;
						priceTotalObject.currencyName = currency.currencyName;
						priceTotalObject.currencyMark = currency.currencyMark;
						totalPriceArr[j] = priceTotalObject;
					}
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
				paramClearCurrenctPrice.push(totalPriceArr[m].price);
			}
			else{
				if(type == 1){
					totalPrice += '<br>+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);
				}else{
					totalPrice += '+' + totalPriceArr[m].currencyName + milliFormat(totalPriceArr[m].price,1);;
				}
				paramClearCurrencyId.push(totalPriceArr[m].currencyId);
				paramClearCurrenctPrice.push(totalPriceArr[m].price);
			}
		}
	}
	return totalPrice;
}