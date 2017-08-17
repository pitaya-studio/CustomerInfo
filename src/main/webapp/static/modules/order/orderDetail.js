$(window).load(function() {
	initSetting();
});

//是否是优加 需求号109
var isYoujia=false;

$(function() {
	var groupCodeVal = $("#groupCodeEle").text();
	if(groupCodeVal.length > 20) {
		groupCodeVal = groupCodeVal.substring(0, 20) + "...";
	}
	$("#groupCodeEle").text(groupCodeVal);
	parsePriceJson(priceJson);
});

	
//初始化设置
function initSetting() {
	//设置只读人数
	$("#productOrderTotal").disableContainer( {
        blankText : "—",
        formatNumber : formatNumberArray
    });
	//设置只读联系人
    $("#orderpersonMesdtail").disableContainer( {
        blankText : "—",
        formatNumber : formatNumberArray
    });

	if($("#isForYouJia").val()=="true" && $("#isLoose").val()=="true" && $("#isSettlement").val()=="true"){
		isYoujia = true;
	}

    //计算游客编号
    recountIndexTraveler();
    //设置游客的显示效果
    travelerEffect();

	if(!isYoujia){//如果批发商不是优加，采用原来计算逻辑，优加的话直接在页面显示后台传入的值
		//加载页面的时候需要主动去算一下金额，因为如果没有游客，则会出现金额为空情况
		changeTotalPrice();
	}
    //游客设置为只读
    oneTwoSecond();
    //显示关闭按钮
    $("#closeOperation").show();
    $("#closeOperation .ydbz_s").removeClass("displayClick");
    $(".orderdetails6 a").removeClass("displayClick");
    $(".downloadFiles").removeClass("displayClick");
    //显示全部下载按钮
    $(".downloadFiles").show();

	removeTravelerFilesDisabledEffect();
	
	// 移除“价格方案”上的displayClick
	$("a[name=jgfa]").removeClass("displayClick");
	// 改变“价格备注”span的style以便换行展示
	$("textarea[name=priceRemark]").each(function(index, element){
		$(element).parent().find("span[class=disabledshowspan]").attr("style","word-wrap:break-word;word-break:break-all;");
	});
	
	// 删除多余单击事件
	$(".zksx").unbind("click");
	
	// 如果游客已转团或退团则把name删除
//	$("#traveler form").each(function(index, obj) {
//		//如果游客已退团或转团，则成本价不计入订单成本价
//		var changeGroupFlag = $("input[name=changeGroupFlag]", this).val();
//		if(changeGroupFlag == '3' || changeGroupFlag == '5') {
//			$(this).attr("name", "");
//			$(this).removeClass();
//		}
//	});
}

//游客资料部分中，如果存在上传文件，清除文件的"-"显示
function removeTravelerFilesDisabledEffect(){

	$(".disabledshowspan").each(function(index,element){
		if($(element).parent().find("span.seach_r").length>0){
			$(element).remove();
		}
	});
}

//设置游客的显示效果
function travelerEffect(){
	var travelerForm = $("#traveler form");
	for(var j = 0; j < travelerForm.length; j++){
		var jsPriceJson = $("input[name=jsPriceJson]",travelerForm[j]).val();
		var priceObjArr = new Array();
		$.each($.parseJSON(jsPriceJson), function(key, value){
			priceObjArr.push(value);
		});
		var travelerJsPrice = new Object();
		travelerJsPrice.jsPrice = priceObjArr;
		travelerTotalPriceArr.push(travelerJsPrice);
		
		if (isYoujia) {
			var otherTotalPrice = changeOtherTotalPrice(travelerForm[j]);
			$(travelerForm[j]).find("span[name=totalOtherCostPrice]").text(otherTotalPrice);
		}
	}
	//结算价
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
	for(var j = 0; j < travelerForm.length; j++){
		changePayPriceByCostChange_forDetail($(travelerForm[j]),1);
	}
	
}

// 第一步点击下一步效果
function oneTwoSecond() {
	//游客信息、特殊需求
	$("#manageOrder_new,#manageOrder_m").disableContainer( {
		blankText : "—",
		formatNumber : formatNumberArray
	});
	$(".downloadFile").removeClass("displayClick");
	//费用
	var sums = $(".tourist-right").find("input[name='sum']");
	$.each(sums, function(key, value) {
		var _$span = $(value).next();
		_$span.text(_$span.text());
		;
	});

	$("#firstDiv").hide();
	$(".orderPersonMsg").hide();
	
	//处理除了第一个游客其他游客内容收缩
	var _add_seachcheck = $("#traveler .add_seachcheck");
	for(var i = 1; i< _add_seachcheck.length;i++){
		if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
			_add_seachcheck.eq(i).click();
		}
	}
}

function downloadFiles(docIds) {
	var ctx = $("#ctx").val();
	window.open(encodeURI(encodeURI(ctx + "/sys/docinfo/zipdownload/" + docIds + "/游客资料")));
}