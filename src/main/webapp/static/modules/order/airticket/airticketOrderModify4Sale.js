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
var formatNumberArray = new Array();

var jsarray = null;                         // 订单总结算价数组
var tradeTotalPriceArray = null;                 // 订单总同行价数组
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

$(function() {
	// 为每个游客编号
	recountIndexTraveler();
    // 计算游客同行价
    travelerTradePrice();
    // 页面加载时，计算订单总额
    init_changeTotalPrice();
    // 修改数量不能小于当前数量，特殊人数，余位不足和切位不足等
    //checkFreePosition();
    // 添加游客点击事件
    addTraveler();
    // 下一步 上一步 保存
    modifyOneSecondSave();
    // 游客名称失去焦点时触发
    getTravelerNamePinyin();
    // 执行一些初始化工作
    initSetting();
    // 初始化渠道信息
    initModifyAgentInfo();
    // 选择不同的渠道,修改对应的渠道信息
    modifyAgentInfo();
    // 游客选择是否需要联运 如果需要初始价格和联运区域
    //initIntermodal();
    // 发证日期失去焦点时触发
    getPassportValidity();
    // 删除游客点击事件
    delTraveler();
    // 绑定日期控件
    dodatePicker();
});

//-----------------------流程函数start-------------------------------------------->
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
 * 计算游客同行价
 */
function travelerTradePrice() {
	// 游客同行价 = 游客单价 + 税费 + 联运价格
	$(".travelerSrcPrice").each(function(){
		// 获取游客同行价币种Name
		var travelerSrcPriceCurrencyName = $(this).next().val();
		// 获取游客单价
		var travelerSrcPriceVal = $(this).val();
		// 获取税费
		var texamtVal = $("#texamt").val();
		// 获取联运价格
		var intermodalCurrencyName = $(this).parents("form").find(".intermodalCurrencyName").text();
		var intermodalMoneyAmount = $(this).parents("form").find(".intermodalMoneyAmount").text();
		var travelerPrice = null;
		if(intermodalCurrencyName == travelerSrcPriceCurrencyName) {
			travelerPrice = travelerSrcPriceCurrencyName + "" + changeTwoDecimal(Number(travelerSrcPriceVal) + Number(texamtVal) + Number(intermodalMoneyAmount));
		} else if(intermodalCurrencyName != "" && intermodalCurrencyName != travelerSrcPriceCurrencyName && intermodalCurrencyName != "无"){
			travelerPrice = travelerSrcPriceCurrencyName + "" + changeTwoDecimal(Number(travelerSrcPriceVal) + Number(texamtVal)) + "+" 
							+ (intermodalCurrencyName + "" + intermodalMoneyAmount);
		} else {
			travelerPrice = travelerSrcPriceCurrencyName + "" + changeTwoDecimal(Number(travelerSrcPriceVal) + Number(texamtVal));
		}
		$(this).parent().find("[data='tradePrice']").text(travelerPrice);
	});
}

/**
 * 页面加载时计算订单总额
 */
function init_changeTotalPrice() {
	jsarray = new Array();
    tradeTotalPriceArray = new Array();
    // 计算此次订单填写的游客总人数
    var totalPersonNum = $("#newPersonNum").val();
    if (totalPersonNum == "" || totalPersonNum == undefined) {
        totalPersonNum = 0;
    }
    // 预订人数
    var adultNum = $("#orderPersonNumAdult").val();
    var childNum = $("#orderPersonNumChild").val();
    var specialNum = $("#orderPersonNumSpecial").val();

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
    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    airticketbzObj.je=travelerPriceTotal;
    jsarray.push(airticketbzObj);
    tradeTotalPriceArray.push(airticketbzObj);

    // 计算联运价格
    $(".intermodalCurrencyName").each(function(){
    	var travelerIndex = $(this).parents("form.travelerTable").find(".travelerIndex").text();
    	var intermodalCurrencyName = $(this).text();													// 联运币种名称
    	var intermodalMoneyAmount = $(this).parent().parent().find(".intermodalMoneyAmount").text();	// 联运价格
    	var intermodalCurrencyId = $(this).parent().parent().find(".intermodalCurrencyId").val();		// 联运币种id
    	if(intermodalMoneyAmount != '0') {
    		bzobj=new Object();
    		bzobj.travelerIndex = travelerIndex;
            bzobj.currencyId=intermodalCurrencyId;
            bzobj.currencyName=intermodalCurrencyName;
            bzobj.je=intermodalMoneyAmount;
            jsarray.push(bzobj);
            tradeTotalPriceArray.push(bzobj);
    	}	
    	
    });
    
    var jsj=getbzStr(jsarray);
    var thj = getbzStr(tradeTotalPriceArray);
    if(jsj==""){
        $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumClearPrice").text(jsj);
    }
    if(thj==""){
        $("#travelerSumPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumPrice").text(thj);
    }
}

/**
 * 修改数量不能小于当前数量，特殊人数，余位不足和切位不足等。
 */
function checkFreePosition(obj, num) {
    var idVal = $(obj).attr("id");              // 当前标签 id
    var val = parseInt($(obj).val());           // 当前标签值
    var personType = $(obj).attr("data-id");    // 当前游客类型

    var adultNum = $("#orderPersonNumAdult").val();
    var childNum = $("#orderPersonNumChild").val();
    var specialNum = $("#orderPersonNumSpecial").val();
    var totalNum = parseInt(adultNum) + parseInt(childNum) + parseInt(specialNum) ; // 计算总得出行人数

    var freePositionNum = parseInt($("#freePosition").val());                     // 余位
    var leftpayReservePosition = parseInt($("#leftpayReservePosition").val());    // 切位
    var placeHolderType = $("#placeHolderType").val();                            // 占位方式
    var personNum = parseInt($("#personNum").val());                              // 原订单人数

    // 修改数量不能小于当前数量
    if(num > val) {
        $(obj).val(num);
        totalNum = getTotalNum();
        $("#newPersonNum").val(totalNum);
        $.jBox.tip("修改人数不能小于当前人数！", "warning", { focusId: idVal });
        return;
    }
    
    // 最高儿童人数校验  儿童占位校验
    if("orderPersonNumChild" == idVal) {
    	var maxChildrenCount = parseInt($("#maxChildrenCount").val());
    	var currentChildrenCount = parseInt($("#currentChildrenCount").val());
        if(maxChildrenCount != null &&  parseInt(val) > parseInt(maxChildrenCount)) {
            $(obj).val(num);
            totalNum = getTotalNum();
            $("#newPersonNum").val(totalNum);
            $.jBox.tip('儿童最高人数为'+maxChildrenCount+'人，请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
            return;
        }
        if(currentChildrenCount != null &&  parseInt(val) > parseInt(currentChildrenCount)) {
            $(obj).val(num);
            totalNum = getTotalNum();
            $("#newPersonNum").val(totalNum);
            $.jBox.tip('儿童最高人数为'+maxChildrenCount+'人,剩余'+currentChildrenCount+'人，请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
            return;
        }
    }
    
    // 最高特殊人数校验  特殊人数占位校验
    if("orderPersonNumSpecial" == idVal) {
    	var maxPeopleCount = parseInt($("#maxPeopleCount").val());
    	var currentPeopleCount = parseInt($("#currentPeopleCount").val());//团期已占位人数
        if(maxPeopleCount != null && val > maxPeopleCount) {
            $(obj).val(num);
            totalNum = getTotalNum();
            $("#newPersonNum").val(totalNum);
            $.jBox.tip('特殊人群最高人数为'+maxPeopleCount+'人，请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
            return;
        }
        if(currentPeopleCount != null &&  parseInt(val) > parseInt(currentPeopleCount)) {
            $(obj).val(num);
            totalNum = getTotalNum();
            $("#newPersonNum").val(totalNum);
            $.jBox.tip('特殊人群最高人数为'+maxPeopleCount+'人,剩余'+currentPeopleCount+'人，请重新填写！', 'warning', { focusId: 'orderPersonNumSpecial' });
            return;
        }
    }
    if (placeHolderType == 1) {                                         // 切位
        if ((totalNum - personNum) > leftpayReservePosition) {
            $(obj).val(num);
            totalNum = getTotalNum();
            $("#newPersonNum").val(totalNum);
            $.jBox.tip("余位不足！", "warning", { focusId: idVal });
            return;
        }
    } else if (placeHolderType == 0) {                                  // 占位
        if ((totalNum - personNum) > freePositionNum) {
            $(obj).val(num);
            totalNum = getTotalNum();
            $("#newPersonNum").val(totalNum);
            $.jBox.tip("余位不足！", "warning", { focusId: idVal });
            return;
        }
    }
    // 将总人数赋值为修改后的总人数
    $("#newPersonNum").val(totalNum);
    // 判断是否显示添加游客按钮
    util_isShowAddTravelerBtn();
    // 改变订单总结算价
    fn_changeTotalJsPrice(personType, num);
    // 改变订单总同行价
    fn_changeTotalTradePrice();
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
            selSrcPriceCurrency = $.parseJSON($("#airticketbz").val()).currencyId;
            selSrcPriceCurrencyMark  = $.parseJSON($("#airticketbz").val()).currencyMark;
            selSrcPriceCurrencyName = $.parseJSON($("#airticketbz").val()).currencyName;
            personType = 0;
        }
        if(!selFlag){
            if ($("#orderPersonNumChild").val() > countChild()){
                selFlag = true;
                selJsPrice = $('#etj').val()==""?0:$("#etj").val();
                selSrcPriceCurrency = $.parseJSON($("#airticketbz").val()).currencyId;
                selSrcPriceCurrencyMark  = $.parseJSON($("#airticketbz").val()).currencyMark;
                selSrcPriceCurrencyName = $.parseJSON($("#airticketbz").val()).currencyName;
                personType = 1;
            }
        }
        if(!selFlag){
            if ($("#orderPersonNumSpecial").val() > countSpecial()){
                selFlag = true;
                selJsPrice = $('#tsj').val()==""?0:$('#tsj').val();
                selSrcPriceCurrency = $.parseJSON($("#airticketbz").val()).currencyId;
                selSrcPriceCurrencyMark  = $.parseJSON($("#airticketbz").val()).currencyMark;
                selSrcPriceCurrencyName = $.parseJSON($("#airticketbz").val()).currencyName;
                personType = 2;
            }
        }
        // 填充人员类型
        $("input[name=personType]",_travelerForm)[personType].checked = true;
        // 计算游客结算价（税费+单价）
        var texamtVal = $("#texamt").val();
        var jsPriceVal = Number(texamtVal) + Number(selJsPrice);
        // 填充游客内部显示的结算价
        var appendString = "";
		appendString += "<em class='jsPriceCurrencyNameClass' style='vertical-align: baseline;'>"+ selSrcPriceCurrencyName +"</em>:";
		appendString += "<input type='text' maxlength='10' class='jsPriceClass' name='jsPrice' value="+ jsPriceVal +" data="+ jsPriceVal +" ";
		appendString += "onblur='changeClearPrice(this)' onafterpaste='seetlementKeyUp(this))' onkeyup='seetlementKeyUp(this)' ";
		appendString += "data-currencyid='"+ selSrcPriceCurrency +"' data-src='"+ jsPriceVal +"'/>";
		var appendStringHidden = "<input type='hidden' name='jsSrcPrice' value='"+ jsPriceVal +"'/>";
	    appendStringHidden += "<input type='hidden' name='jsSrcPriceCurrencyId' value='"+ selSrcPriceCurrency +"'/>";
        $("span[data=newJsPrice]",_travelerForm).html(appendString);
        $("span[data=newJsPrice]",_travelerForm).after(appendStringHidden);
        // 默认情况下，新添加的游客同行价和结算价是相同的
        var tradePrice = selSrcPriceCurrencyName + jsPriceVal.toString().formatNumberMoney('#,##0.00');
        var tradePriceHidden = "<input type='hidden' class='traveler travelerSrcPrice' value='"+ selJsPrice +"'/>";
        tradePriceHidden += "<input type='hidden' class='traveler travelerSrcPriceCurrencyName' value='"+ selSrcPriceCurrencyName +"'/>";
        tradePriceHidden += "<input type='hidden' class='traveler travelerSrcPriceCurrencyId' value='"+ selSrcPriceCurrency +"'/>";
        $("span[data=tradePrice]",_travelerForm).html(tradePrice);
        $("span[data=tradePrice]",_travelerForm).after(tradePriceHidden);
        // 计算游客编号
        recountIndexTraveler();
        // 添加游客之后，要判断是否继续显示添加游客按钮
        var travelerTableSize = $(".travelerTable").length;
    	if(getTotalNum() > travelerTableSize) {
    		$("#addTraveler").show();
    	} else {
    		$("#addTraveler").hide();
    	}
    });
}

/**
 * 初始化设置
 */
function initSetting() {
    // 设置游客的显示效果
    fn_travelerEffect();
    util_isShowAddTravelerBtn();
}

/**
 * 选择不同的渠道,修改对应的渠道信息
 */
function modifyAgentInfo() {
    var a = "";
    $("#modifyAgentInfo").change(function(){
        var objValue = $(this).val();
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
    });
}

/**
 * 初始化渠道信息
 */
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

/**
 * 下一步 上一步 保存
 */
function modifyOneSecondSave() {
    // 第一步点击下一步
    $("#oneToSecondStepDiv").click(function() {
        //点击下一步时，要对产品信息中各个类型的人数和游客信息中的人数类型进行校验
        var adultNum = $("#orderPersonNumAdult").val();
        var childNum = $("#orderPersonNumChild").val();
        var specialNum = $("#orderPersonNumSpecial").val();

        // 页面中已存在的游客类型
        var adultTypeVal = $(":input[name='personType'][value='1']:checked").length;
        var childTypeVal = $(":input[name='personType'][value='2']:checked").length;
        var specialTypeVal = $(":input[name='personType'][value='3']:checked").length;

        if(adultTypeVal > parseInt(adultNum) || childTypeVal > parseInt(childNum) || specialTypeVal > parseInt(specialNum)) {
            $.jBox.tip("游客类型人数与初始值不匹配请修改！", "erroring");
            return;
        }

        // 验证订单联系人
        outerrorList = new Array();
        _doValidateorderpersonMesdtail();
//        createDivInDiv(outerrorList);
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
//        createDivInDiv(outerrorList);
        if (outerrorList.length > 0) {
            return;
        }
        // 验证是否保存游客信息 如果有未保存的游客需要提示
        // 获取添加的游客form数组
        var notSaveTravelerName = [];
        var notSaveTravelerIndex = [];
        $.each(travelerTables,function(key,value){
            //var travelerId = $(value).find('input[name=travelerId]').val();
            //if(travelerId == ""){
            //    notSaveTravelerName.push($(value).find("input[name=travelerName]").val());
            //    notSaveTravelerIndex.push(Number($(value).find(".travelerIndex").text()));
            //}else{
            //
            //}

            if(!$('input[name=travelerName]',$(value)).is(":hidden")){
                notSaveTravelerName.push($(value).find("input[name=travelerName]").val());
                notSaveTravelerIndex.push(Number($(value).find(".travelerIndex").text()));
            }

        });
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
//        createDivInDiv(outerrorList);
        if (outerrorList.length > 0) {
            return;
        }
        // 设置只读联系人
//        $("#orderpersonMesdtail").disableContainer( {
//            blankText : "—",
//            formatNumber : formatNumberArray
//        });
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
        // 费用
        var sums = $(".tourist-right").find("input[name='sum']");
        $.each(sums, function(key, value) {
            var _$span = $(value).next();
            _$span.text(_$span.text());
        });

        //点击下一步的时候，设置出行人数为只读。
        var orderPersonNumAdultTemp = $("#orderPersonNumAdult").val();
        $("#orderPersonNumAdult").attr("type", "hidden").after("<span>"+orderPersonNumAdultTemp+"</span>");
        var orderPersonNumChildTemp = $("#orderPersonNumChild").val();
        $("#orderPersonNumChild").attr("type", "hidden").after("<span>"+orderPersonNumChildTemp+"</span>");
        var orderPersonNumSpecialTemp = $("#orderPersonNumSpecial").val();
        $("#orderPersonNumSpecial").attr("type", "hidden").after("<span>"+orderPersonNumSpecialTemp+"</span>");
        
        //联系人只读
        readOnlyAllContactInfo();

        // 隐藏按钮，防止多次提交
        $(this).hide();
        $("#firstDiv").hide();
        $("#secondDiv").show();
        $(".orderPersonMsg").hide();
        $("#ordercontact").find(".yd1AddPeople").hide();
        
        // 金额字体过小，把影响字体样式去掉
		$("span[data=newJsPrice] span").removeClass("disabledshowspan");
    });

    // 第二步点击上一步
    $("#modSecondToOneStepDiv").click(function() {
    	// 还原样式
		$("span[data=newJsPrice] span").addClass("disabledshowspan");
        //点击上一步的时候，设置出行人数为可写。
        $("#orderPersonNumAdult").attr("type", "text").next("span").remove();
        $("#orderPersonNumChild").attr("type", "text").next("span").remove();
        $("#orderPersonNumSpecial").attr("type", "text").next("span").remove();
        $("#oneToSecondStepDiv").show();
        $("#productOrderTotal").undisableContainer();
        $("#orderpersonMesdtail").undisableContainer();
        $("ul[name='orderpersonMes']").undisableContainer();
        $("ul[name='orderpersonMes'] .ydbz_x").show();
        $("#manageOrder_m").hide();
        var _closeOrExpand = $(".closeOrExpand").eq(0);
        $(".orderPersonMsg").hide();
        if (_closeOrExpand.hasClass("ydClose")) {
            _closeOrExpand.click();
        }
        $("#manageOrder_new").undisableContainer();
        $("#manageOrder_m").undisableContainer();
        $("#firstDiv").show();
        $("#secondDiv").hide();
        $("#ordercontact").find(".yd1AddPeople").show();
        back2WritableContactInfo(); //联系人可写恢复
    });

    var submit_times = 0;
    // 保存 保存并支付
    $("#secondToThirdStepDiv").click(function() {
        //防止多次提交
        if (submit_times == 0) {
            loading('正在提交，请稍等...');
            submit_times++;
        } else {
            top.$.jBox.info("您已提交，请耐心等待", "警告");
            return false;
        }

        // 1.出行人数（成人，儿童，特殊人群）
        var adultNum = $("#orderPersonNumAdult").val();                                                     // 订单成人数
        var childNum = $("#orderPersonNumChild").val();                                                     // 订单儿童人数
        var specialNum = $("#orderPersonNumSpecial").val();                                                 // 订单特殊人数
        var orgPersonNum = $("#personNum").val();                                                           // 修改前订单人数
        var newPersonNum = $("#newPersonNum").val();                                                        // 修改后订单人数
        // 2.预定人信息
        var agentId = $("[name='agentShow']").val();                                                        // 预订人渠道信息        
        alert(agentId);
        var contacttables = $("#ordercontact ul[name=orderpersonMes]");                                     // 预订人信息
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
        // 3.特殊需求
        var comments = $(":input[name='comments']").val();                                                  // 特殊需求
        // 4.订单总结算价
        var orderTotalClearPrice = jsarray;
        // 5.团队返佣
        var groupRebatesCurrency = $("[name='groupRebatesCurrency']").val();                                // 团队返佣币种
        var groupRebatesMoney = $(":input[name='groupRebatesMoney']").val();                                // 团队返佣金额
        // 6.游客信息(游客类型，联运信息，基本信息，预计个人返佣，结算价)
        var travelers = new Array();
        $("form.travelerTable").each(function() {
            var traveler = new Object();
            // 游客id
            var travelerId = $(this).find("#travelerId").val();
            if(travelerId == "" || travelerId == undefined) {
                travelerId = "-1";
            }

            // 游客类型
            var personType = $(this).find("[name='personType']:checked").val();
            // 联运信息
            var isIntermodal = $(this).find("[name='ydbz2intermodalType']:checked").val();                  // 是否需要联运（1需要 0不需要）
            var IntermodalCurrencyName = $(this).find(":hidden.orgIntermodalCurrencyName").val();           // 联运币种名称
            var intermodalMoneyAmount = $(this).find(":hidden.orgIntermodalMoneyAmount").val();             // 联运金额
            var intermodalCurrencyId = $(this).find(":hidden.orgIntermodalCurrencyId").val();
            var IntermodalId = $(this).find(":hidden.orgIntermodalId").val();                               // 联运id
            // 联运币种id
            // 基本信息
            var travelerName = $(this).find("[name='travelerName']").val();                                 // 游客姓名
            var travelerPinyin = $(this).find("[name='travelerPinyin']").val();                             // 游客拼音姓名
            var travelerSex = $(this).find("[name='travelerSex']").val();                                   // 游客性别
            var nationality = $(this).find("[name='nationality']").val();                                   // 国籍
            var birthDay = $(this).find("[name='birthDay']").val();                                         // 出生日期
            var telephone = $(this).find("[name='telephone']").val();                                       // 联系电话
            var passportCode = $(this).find("[name='passportCode']").val();                                 // 护照号
            var passportValidity = $(this).find("[name='passportValidity']").val();                         // 护照有效期
            var idCard = $(this).find("[name='idCard']").val();                                             // 身份证号
            var passportType = $(this).find("[name='passportType']").val();                                 // 护照类型（1因私护照 2因公护照）
            var remarks = $(this).find("[name='remarks']").val();                                           // 游客备注
            // 预计个人返佣
            var rebatesCurrency = $(this).find("[name='rebatesCurrency']").val();                           // 预计个人返佣币种
            var rebatesMoney = $(this).find("[name='rebatesMoney']").val();                                 // 预计个人返佣金额
            // 结算价
            var jsPrices = new Array();
            $(this).find("[name='jsPrice']").each(function(){
                var jsPrice = new Object();
                jsPrice.jsAmount = $(this).attr("data");
                jsPrice.currencyId = $(this).attr("data-currencyid");
                jsPrices.push(jsPrice);
            })

            traveler.travelerId = travelerId;
            traveler.personType = personType;
            traveler.isIntermodal = isIntermodal;
            traveler.IntermodalCurrencyName = IntermodalCurrencyName;
            traveler.intermodalMoneyAmount = intermodalMoneyAmount;
            traveler.intermodalCurrencyId = intermodalCurrencyId;
            traveler.IntermodalId = IntermodalId;
            traveler.travelerName = travelerName;
            traveler.travelerPinyin = travelerPinyin;
            traveler.travelerSex = travelerSex;
            traveler.nationality = nationality;
            traveler.birthDay = birthDay;
            traveler.telephone = telephone;
            traveler.passportCode = passportCode;
            traveler.passportValidity = passportValidity;
            traveler.idCard = idCard;
            traveler.passportType = passportType;
            traveler.remarks = remarks;
            traveler.rebatesCurrency = rebatesCurrency;
            traveler.rebatesMoney = rebatesMoney;
            traveler.jsPrices = jsPrices;

            // 将遍历出来的每个游客放入游客数组中,在后台统一保存
            travelers.push(traveler);
        });
        // 7.订单号 和 机票产品id,是否占位
        var orderId = $("#orderId").val();                                                                  // 订单号
        var airticketId = $("#airticketId").val();                                                          // 机票产品号
        var placeHolderType = $("#placeHolderType").val();                                                  // 是否占位（0普通 1切位）
        // 8.发送ajax请求
        $.ajax({
            type : "POST",
            url : "../../order/manage/modifyAirticketOrder",
            data : {
                adultNum : adultNum,
                childNum : childNum,
                specialNum : specialNum,
                orgPersonNum : orgPersonNum,
                newPersonNum : newPersonNum,
                agentId : agentId,
                ordercontacts : JSON.stringify(ordercontacts),
                comments : comments,
                orderTotalClearPrice : JSON.stringify(orderTotalClearPrice),
                groupRebatesCurrency : groupRebatesCurrency,
                groupRebatesMoney : groupRebatesMoney,
                travelers : JSON.stringify(travelers),
                orderId : orderId,
                airticketId : airticketId,
                placeHolderType : placeHolderType
            },
            success : function(msg) {
                alert("!!!");
                console.log(msg);
                submit_times = 0;
                /**
                if (msg.errorMsg) {
                    top.$.jBox.tip(msg.errorMsg, 'error');
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
                        window.location.href = "../../orderList/manage/showOrderList/0/" + msg.productOrder.orderStatus + "?orderNum" + msg.productOrder.orderNum;
                    }
                }
                */
            }
        });
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

/**
 * 护照有效日期
 */
function getPassportValidity(){
    $("#traveler").delegate("input[name='issuePlace']", "blur", function() {
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
    });
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
                            }else{
                                top.$.jBox.tip('删除失败', 'error');
                            }
                        }
                    });
                }else{
                    deleteTravelerAfter($this.closest('form'));
                }
            }
        });
    });
}

$("#traveler").delegate("a[name='deleteTraveler']", "click", function() {
		var $this = $(this);
		var travelerId = $(this).closest('.travelerTable').find("input.traveler[name='id']").val();
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
	});
});

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

//-----------------------流程函数end-------------------------------------------->

//-----------------------自调用函数start----------------------------------------->
/**
 * 修改游客数量时，更改游客总结算价
 */
function fn_changeTotalJsPrice(personType, orgNum) {
    // 在更改游客结算价时，先将联运价格从结算数组中删除
    for(var i = jsarray.length; i >= 0; i--) {
        for(var p in jsarray[i]) {
            if(p == "travelerIndex") {
                jsarray.pop(jsarray[i]);
                continue;
            }
        }
    }

    // 预订人数
    var adultNum = $("#orderPersonNumAdult").val();
    var childNum = $("#orderPersonNumChild").val();
    var specialNum = $("#orderPersonNumSpecial").val();

    if(personType == '1') {
        adultNum = Number(adultNum) - Number(orgNum);
        childNum = 0;
        specialNum = 0;
    } else if(personType == '2') {
        childNum = Number(childNum) - Number(orgNum);
        adultNum = 0;
        specialNum = 0;
    } else if (personType == '3') {
        specialNum = Number(specialNum) - Number(orgNum);
        adultNum = 0;
        childNum = 0;
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
    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    airticketbzObj.je=travelerPriceTotal;
    if(travelerPriceTotal != 0) {
        jsarray.push(airticketbzObj);
    }

    // 计算联运价格
    $(".intermodalCurrencyName").each(function(){
        var travelerIndex = $(this).parents("form.travelerTable").find(".travelerIndex").text();
        var intermodalCurrencyName = $(this).text();													// 联运币种名称
        var intermodalMoneyAmount = $(this).parent().parent().find(".intermodalMoneyAmount").text();	// 联运价格
        var intermodalCurrencyId = $(this).parent().parent().find(".intermodalCurrencyId").val();		// 联运币种id
        if(intermodalMoneyAmount != '0') {
            bzobj=new Object();
            bzobj.travelerIndex = travelerIndex;
            bzobj.currencyId=intermodalCurrencyId;
            bzobj.currencyName=intermodalCurrencyName;
            bzobj.je=intermodalMoneyAmount;
            jsarray.push(bzobj);
        }

    });

    var jsj=getbzStr(jsarray);
    if(jsj==""){
        $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumClearPrice").text(jsj);
    }

}

/**
 * 修改联运价格时，更改游客总结算价
 */
function fn_changeTotalJsPrice4Intermodal() {
    // 在更改游客结算价时，先将联运价格从结算数组中删除
    for(var i = jsarray.length; i >= 0; i--) {
        for(var p in jsarray[i]) {
            if(p == "travelerIndex") {
                jsarray.pop(jsarray[i]);
                continue;
            }
        }
    }

    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种

    // 计算联运价格
    $(".intermodalCurrencyName").each(function(){
        var travelerIndex = $(this).parents("form.travelerTable").find(".travelerIndex").text();
        var intermodalCurrencyName = $(this).text();													// 联运币种名称
        var intermodalMoneyAmount = $(this).parent().parent().find(".intermodalMoneyAmount").text();	// 联运价格
        var intermodalCurrencyId = $(this).parent().parent().find(".intermodalCurrencyId").val();		// 联运币种id
        if(intermodalMoneyAmount != '0') {
            bzobj=new Object();
            bzobj.travelerIndex = travelerIndex;
            bzobj.currencyId=intermodalCurrencyId;
            bzobj.currencyName=intermodalCurrencyName;
            bzobj.je=intermodalMoneyAmount;
            jsarray.push(bzobj);
        }

    });

    var jsj=getbzStr(jsarray);
    if(jsj==""){
        $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumClearPrice").text(jsj);
    }
}

/**
 * 修改游客数量时，更改游客总同行价
 */
function fn_changeTotalTradePrice() {
    tradeTotalPriceArray = new Array();
    // 计算此次订单填写的游客总人数
    var totalPersonNum = $("#newPersonNum").val();
    if (totalPersonNum == "" || totalPersonNum == undefined) {
        totalPersonNum = 0;
    }
    // 预订人数
    var adultNum = $("#orderPersonNumAdult").val();
    var childNum = $("#orderPersonNumChild").val();
    var specialNum = $("#orderPersonNumSpecial").val();

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
    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    airticketbzObj.je=travelerPriceTotal;
    tradeTotalPriceArray.push(airticketbzObj);

    // 计算联运价格
    $(".intermodalCurrencyName").each(function(){
        var travelerIndex = $(this).parents("form.travelerTable").find(".travelerIndex").text();
        var intermodalCurrencyName = $(this).text();													// 联运币种名称
        var intermodalMoneyAmount = $(this).parent().parent().find(".intermodalMoneyAmount").text();	// 联运价格
        var intermodalCurrencyId = $(this).parent().parent().find(".intermodalCurrencyId").val();		// 联运币种id
        if(intermodalMoneyAmount != '0') {
            bzobj=new Object();
            bzobj.travelerIndex = travelerIndex;
            bzobj.currencyId=intermodalCurrencyId;
            bzobj.currencyName=intermodalCurrencyName;
            bzobj.je=intermodalMoneyAmount;
            tradeTotalPriceArray.push(bzobj);
        }

    });

    var thj=getbzStr(tradeTotalPriceArray);
    if(thj==""){
        $("#travelerSumPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumPrice").text(thj);
    }


}
//-----------------------自调用函数end------------------------------------------->


//-----------------------页面直接调用函数start------------------------------------>
/**
 * 改变订单结算价时，更改订单总额
 */
function changeClearPrice(obj) {
	var changedPrice = $(obj).val();										// 改变后的计算价
    var changedPriceData = $(obj).attr("data");                             // 改变之前的结算价
	var changedPriceCurrencyId = $(obj).attr("data-currencyid");			// 改变后的结算价币种id
	var personType = $(obj).parents("form[class='travelerTable']").find("[name='personType']:checked").val();	//游客类型
	var travelerIndex = $(obj).parents("form.travelerTable").find(".travelerIndex").text();					//游客编号
	
	var texamt = Number($("#texamt").val());//税费
	var crjTexamt = Number($("#crj").val()) + texamt;
	var etjTexamt = Number($("#etj").val()) + texamt;
	var tsjTexamt = Number($("#tsj").val()) + texamt;
	
	var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    // 获取联运价格
    var intermodalCurrencyName = $(obj).parents("form[class='travelerTable']").find(".orgIntermodalCurrencyName").val();
    var intermodalMoneyAmount = $(obj).parents("form[class='travelerTable']").find(".orgIntermodalMoneyAmount").val();
    var intermodalCurrencyId = $(obj).parents("form[class='travelerTable']").find(".orgIntermodalCurrencyId").val();
    
    $(jsarray).each(function() {
    	var isBreak = false;
    	var currencyId = $(this)[0].currencyId;
    	var currencyName = $(this)[0].currencyName;
    	var currencyMark = $(this)[0].currencyMark;
    	var je = $(this)[0].je;
    	var jsTravelerIndex = $(this)[0].travelerIndex;
    	if((changedPriceCurrencyId == currencyId) && (currencyId == airticketbzObj.currencyId) && (jsTravelerIndex == "" || jsTravelerIndex == undefined)) {
    		je = je - changedPriceData;
    		je += Number(changedPrice);
    		$(this)[0].je = je;
    		isBreak = true;
    	}
    	if((changedPriceCurrencyId == currencyId) && (currencyId != airticketbzObj.currencyId) && jsTravelerIndex != "" && jsTravelerIndex == travelerIndex) {
            je = je - changedPriceData;
            je += Number(changedPrice);
            $(this)[0].je = je;
            isBreak = true;
    	}
    	
    	var jsj=getbzStr(jsarray);
        if(jsj==""){
            $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
        }else{
            $("#travelerSumClearPrice").text(jsj);
        }
        if(isBreak) {
            $(obj).attr("data", changedPrice);
            return false;
        }
    });
}

/**
 * 验证返佣费用合法性
 */
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

//-----------------------页面直接调用函数end-------------------------------------->

//-----------------------工具函数start------------------------------------------>
function countAdult() {
    var radios = $(".travelerTable input[name='personType'][value='1']:checked");
    return radios.length;
}
function countChild() {
    var radios = $(".travelerTable input[name='personType'][value='2']:checked");
    return radios.length;
}
function countSpecial() {
    var radios = $(".travelerTable input[name='personType'][value='3']:checked");
    return radios.length;
}
function getTotalNum() {
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	var totalNum = parseInt(adultNum) + parseInt(childNum) + parseInt(specialNum) ;
	return totalNum;
}
function valdGR(grvalue){
    var result = -1;
    //整数
    var rex0 = new RegExp("\^\\d+$");
    if (rex0.test(grvalue)) {
        result = 0;
    }
    //一位小数
    var rex1 = new RegExp("\^\\d+\\.\\d$");
    if (rex1.test(grvalue)) {
        result = 1;
    }
    //两位小数
    var rex2 = new RegExp("\^\\d+\\.\\d{2}$");
    if (rex2.test(grvalue)) {
        result = 2;
    }
    return result;
}
/**
 * 多币种拼接显示
 */
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

/**
 * 保留小数点后两位，如果不存在则补0
 */
function changeTwoDecimal(v) {
    if (isNaN(v)) {//参数为非数字
        return 0;
    }
    var fv = parseFloat(v);
    fv = Math.round(fv * 100) / 100; //四舍五入，保留两位小数
    var fs = fv.toString();
    var fp = fs.indexOf('.');
    if (fp < 0) {
        fp = fs.length;
        fs += '.';
    }
    while (fs.length <= fp + 2) { //小数位小于两位，则补0
        fs += '0';
    }
    return fs;
}

function seetlementKeyUp() {
	
}

/** 判断是否显示添加游客按钮 */
function util_isShowAddTravelerBtn() {
    var travelerTableSize = $(".travelerTable").length;
    if(getTotalNum() > travelerTableSize) {
        $("#addTraveler").show();
    } else {
        $("#addTraveler").hide();
    }
}

//-----------------------工具函数end-------------------------------------------->

//-----------------------初始化函数---------------------------------------------->

/**
 * 设置游客的显示效果
 */
function fn_travelerEffect(){
    // 处理除了第一个游客其他游客内容收缩
    var _add_seachcheck = $("#traveler .add_seachcheck");
    for(var i = 0; i< _add_seachcheck.length;i++){
        if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
            if(i > 0) {
                _add_seachcheck.eq(i).click();
            } else if (i == 0) {
                $(_add_seachcheck.eq(0)).parents("form.travelerTable").find(".rightBtn>a").text("保存");
            }
        }
    }
}

/**
 * 如果是转团或退团游客则把form名称给置空并把保存按钮给屏蔽
 */
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

/**
 * 展示单个游客成本价格和结算价格(修改订单会用到此方法)
 * @param traveler 游客表单对象
 * @param type 1:详情 2:修改
 */
function changePayPriceByCostChange_forDetail(traveler,type){
    // 成本价信息
    var travelerPrice = getTravelerPayPrice(traveler);	// 获取成本价
    var travelerIndex = traveler.find(".travelerIndex").text();
    var totalPrice = getOrderToltalPrice(travelerPrice, 1);// 各种币种相加的结算结果
    var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
    if(changeGroupFlag != '3' && changeGroupFlag != '5') {
        travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
    }
    $("span[name=innerJsPrice]",traveler).html(totalPrice);
    $("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
    // 结算价信息
    showClearPriceForOrderUpdate(traveler,type);
    var travelerClearPrice = getTravelerClearPrice(traveler);	// 获取游客的结算价
    var travelerIndex = traveler.find(".travelerIndex").text();
    var totalClearPrice = getOrderToltalClearPrice(travelerClearPrice, 1);// 各种币种相加的结算结果
    travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerClearPrice;
    $("span[name=travelerClearPrice]",traveler).text(totalClearPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
    $("span[name=travelerClearPrice]",traveler).html(totalClearPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
    changeTotalPrice();
}

$("#traveler").delegate("input[name^='personType']", "change",
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
 * 获取游客的结算价
 *
 * @param traveler
 */
function changeClearPriceByInputChange(traveler) {
    var travelerPrice = getTravelerClearPrice(traveler);	// 获取游客的结算价
    var travelerIndex = traveler.find(".travelerIndex").text();
    var totalPrice = getOrderToltalClearPrice(travelerPrice, 1);// 各种币种相加的结算结果
    travelerTotalClearPriceArr[travelerIndex-1].travelerClearPrice = travelerPrice;
    if (totalPrice == '') {
        top.$.jBox.tip('游客结算价不可小于0', 'error');
        return;
    }
    $("span[name=travelerClearPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
    // 填充单个游客信息收起显示的结算价格 add by zhangcl
    $("span[name=travelerClearPrice]",traveler).html(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
    changeTotalPrice();
}

/**
 * 费用发生变化时修改单个游客成本价格
 * @param traveler 游客表单对象
 */
function changePayPriceByCostChange(traveler) {
    var travelerPrice = getTravelerPayPrice(traveler);
    var travelerIndex = traveler.find(".travelerIndex").text();
    var totalPrice = getOrderToltalPrice(travelerPrice, 1);// 各种币种相加的结算结果
    var showClearPrice = showClearPriceInput(travelerPrice,1); // 显示结算价输入框样式
    // 如果游客已退团或转团，则成本价不计入订单成本价
    var changeGroupFlag = $("input[name=changeGroupFlag]", traveler).val();
    if(changeGroupFlag != '3' && changeGroupFlag != '5') {
        travelerTotalPriceArr[travelerIndex-1].jsPrice = travelerPrice;
    }
    if (totalPrice == '') {
        top.$.jBox.tip('游客成本价不可小于0', 'error');
        return;
    }
    $("span[name=innerJsPrice]",traveler).html(totalPrice);
    $("span[name=clearPrice]",traveler).html(showClearPrice);
    $("span[name=jsPrice]",traveler).text(totalPrice.replace(/<br>/g,"").replace(/<BR>/g,""));
    changeTotalPrice();
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
                    if(parseFloat(priceObj.price) >= 0){
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
 * 游客信息展开收起后显示姓名和结算价
 */
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
        setOneTravelerClearPriceShow(curForm,2);
    } else {
        var curForm = obj_this.parent().parent();
        obj_this.addClass("boxCloseOnAdd");
        obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
        obj_this.parent().find('.tourist-t-off').css("display","inline");
        // add by zhangcl 结算价展示信息
        setOneTravelerClearPriceShow(curForm,1);
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
        // 展示游客信息时，结算价展示方式 待优化
        // var spanObj = $("span[name=clearPrice] span.disabledshowspan",travelerForm);
        // if(spanObj && spanObj.length > 0){
        // for(var i = 0; i < spanObj.length ; i++){
        // var spanVal = spanObj.eq(i).text();
        // spanObj.eq(i).removeClass("disabledshowspan");
        // spanObj.eq(i).text(milliFormat(spanVal,1));
        // }
        // }
    }
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
    }
    else{
        $(obj).hide().removeClass("displayClick");
        $(addcost).show();
        $(deleltecost).show();
        $(useall).show();
        $(obj).parent().prev().show();
        $('.tourist-t-off',travelerForm).css("display","none");
        $('.tourist-t-on',travelerForm).show();
        $(":radio[name='personType'][class!='traveler']").hide();
        $('.add_seachcheck',travelerForm).removeClass('boxCloseOnAdd')
        $('input[name=saveBtn]',travelerForm).show();
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
 * 其它费用改变时
 */
function changeClearPriceSum(obj){
    var money = obj.value;
    if(money && money != ""){
        if(money > 0){
            var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
            // 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
        	if (ms != obj.value) {
        		var txt = ms.split(".");
        		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
        	}
        }else{
            obj.value = '';
        }
    }
    changeClearPriceByInputChange($(obj).closest("form"));
}

/**
 * 验证返佣费用合法性
 */
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

/**
 * 得到焦点事件：隐藏填写费用名称提示
 */
function payforotherIn(doc) {
    var obj = $(doc);
    obj.siblings(".ipt-tips2").hide();
}

/**
 * 失去焦点事件：如果输入框中没有值，则提示填写费用名称
 */
function payforotherOut(doc){
    var obj = $(doc);
    if(!obj.val()){
        obj.siblings(".ipt-tips2").show();
    }
}

/**
 * 点击提示错误信息中 "修改" 后错误输入框得到焦点
 */
function focusIpt(doc){
    $(doc).parent().find('input[type=text].ipt2').trigger("focus");
}

/**
 * 币种改变时
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

/**
 * 保存航班备注和预订人信息
 */
function saveRemarkAndAgentInfo(){
    $("#traveler form").each(function(index, element) {
        var _this=$(element).find(".rightBtn a");
        saveTravel(_this, 1);
    })
    var orderId = $("#orderId").val();
    var airticketId = $("#airticketId").val();
    var flightRemark = $(":input[name='remark']").val();//航班备注
    var agentId = $(":input[name='agent']").val();
    var agentName = $(":input[name='agentName']").val();
    var agentContact = $(":input[name='agentContact']").val();
    var agentTel = $(":input[name='agentTel']").val();
    var agentAddress = $(":input[name='agentAddress']").val();
    var agentFax = $(":input[name='agentFax']").val();
    var agentQQ = $(":input[name='agentQQ']").val();
    var agentEmail = $(":input[name='agentEmail']").val();
    var agentRemarks = $(":input[name='agentRemarks']").val();
    var orderAllP = $(":input[name='orderAllP']").val();

    var data = {};
    data['orderId']=orderId;
    data['airticketId']=airticketId;
    data['flightRemark']=flightRemark;
    data['agentId']=agentId;
    data['agentName']=agentName;
    data['agentContact']=agentContact;
    data['agentTel'] = agentTel;
    data['agentAddress']=agentAddress;
    data['agentFax']=agentFax;
    data['agentQQ']=agentQQ;
    data['agentEmail']=agentEmail;
    data['agentRemarks']=agentRemarks;
    data['orderAllP']=orderAllP;

    $.ajax({
        type:"POST",
        url:"${ctx}/order/manage/airticketOrderAgent",
        data:data,
        success:function(data){
            if(data.result == "success"){
                top.$.jBox.tip("保存成功。。。");
                window.location.href="${ctx}/airticketOrderList/manage/airticketOrderList/1";
            }
        }
    });
}

/**
 * 改变预订人信息
 */
function changeAgent(element){
    var _this = $(element);
    var agentForm = _this.parent().parent();
    var agentId = _this.val();
    $.ajax({
        type:"POST",
        url:"${ctx}/order/manage/airticketOrderChangeAgent",
        data:{agentId:agentId},
        success:function(data){

            agentForm.find(":input[name='agentContact']").val(data.agentContact);
            agentForm.find(":input[name='agentTel']").val(data.agentTel);
            agentForm.find(":input[name='agentAddress']").val(data.agentAddress);
            agentForm.find(":input[name='agentFax']").val(data.agentFax);
            agentForm.find(":input[name='agentQQ']").val(data.agentQQ);
            agentForm.find(":input[name='agentEmail']").val(data.agentEmail);
            agentForm.find(":input[name='agentRemarks']").val(data.agentRemarks);
        }
    });
}

function orderAllPrice(curValue2,curValue3,monValue2,monValue3){
    var curFlag = false;//计算当前联运价格标志
    var oriFlag = false;//计算原始联运价格标志
    var ppvalue = $(".orderAllPrice").html();
    var ppvalues = ppvalue.split('+');
    var resultMoney = "";
    for(var i = 0; i < ppvalues.length; i++){
        var tempValue = ppvalues[i].trim();
        if(tempValue){
            var n1 = tempValue.indexOf('-');
            var curValue;
            var rollFlag = false;
            if(n1 == -1){
                curValue = /^\D+(?=\d)/.exec(tempValue)[0];//币种
            }else{
                curValue = tempValue.substr(0,n1);
                rollFlag = true;
            }
            var monValue = tempValue.split(curValue)[1];//钱数信息
            if(curValue == curValue2){//币种相同 相加
                if(rollFlag == true){
                    monValue = monValue2 - (monValue.substr(1));
                }else{
                    monValue =  rmoney(monValue) + rmoney(monValue2);
                    monValue = milliFormat(monValue,'1');
                }
                curFlag = true;
            }
            if(curValue == curValue3){//币种相同 相减
                if(rollFlag == true){
                    monValue = - monValue3 - (monValue.substr(1)) ;
                } else {
                    monValue = rmoney(monValue) - rmoney(monValue3);
                    monValue = milliFormat(monValue,'1');
                }
                oriFlag = true;
            }
            if(monValue == 0){
                ppvalues[i] = "";
            } else {
                ppvalues[i] = curValue + monValue;
                if(resultMoney == ""){
                    resultMoney += ppvalues[i];
                } else {
                    resultMoney += "+" + ppvalues[i];
                }
            }
        }
    }
    if(curFlag == false && oriFlag == false){
        if(curValue2 == curValue3){
            if((monValue2 - monValue3) != 0){
                resultMoney +=  "+" + curValue2 + (monValue2 - monValue3);
            }
        } else if(monValue3==""){
            resultMoney +=  "+" + curValue2 + monValue2 ;
        }else {
            resultMoney +=  "+" + curValue2 + monValue2 + "+" + curValue3 + (-monValue3);
        }
    } else if (curFlag == false){
        if(curValue2!=""){resultMoney +=  "+" + curValue2 + monValue2};
    } else if (oriFlag == false){
        if(monValue3!=""){resultMoney +=  "+" + (curValue3 + (-monValue3));}
    }
    //计算更改后的结算价 end
    $(".orderAllPrice").html(resultMoney);
    $("input[name='orderAllP']").val(resultMoney);
}

function closeCurWindow(){
    this.close();
}
$(document).ready(function(e) {
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function(index, element) {
        if($(this).attr("menuid")==leftmenuid){
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
});
$(function(){
    $('.closeNotice').click(function(){
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function(){
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
});
$(function(){
    $('.main-nav li').click(function(){
        $(this).addClass('select').siblings().removeClass('select');
    })
})

String.prototype.formatNumberMoney= function(pattern){
    var strarr = this?this.toString().split('.'):['0'];
    var fmtarr = pattern?pattern.split('.'):[''];
    var retstr='';
    var str = strarr[0];
    var fmt = fmtarr[0];
    var i = str.length-1;
    var comma = false;
    for(var f=fmt.length-1;f>=0;f--){
        switch(fmt.substr(f,1)){
            case '#':
                if(i>=0 ) retstr = str.substr(i--,1) + retstr;
                break;
            case '0':
                if(i>=0) retstr = str.substr(i--,1) + retstr;
                else retstr = '0' + retstr;
                break;
            case ',':
                comma = true;
                retstr=','+retstr;
                break;
        }
    }
    if(i>=0){
        if(comma){
            var l = str.length;
            for(;i>=0;i--){
                retstr = str.substr(i,1) + retstr;
                if(i>0 && ((l-i)%3)==0) retstr = ',' + retstr;
            }
        }
        else retstr = str.substr(0,i+1) + retstr;
    }

    retstr = retstr+'.';

    str=strarr.length>1?strarr[1]:'';
    fmt=fmtarr.length>1?fmtarr[1]:'';
    i=0;
    for(var f=0;f<fmt.length;f++){
        switch(fmt.substr(f,1)){
            case '#':
                if(i<str.length) retstr+=str.substr(i++,1);
                break;
            case '0':
                if(i<str.length) retstr+= str.substr(i++,1);
                else retstr+='0';
                break;
        }
    }
    return retstr.replace(/^,+/,'').replace(/\.$/,'');
}

String.prototype.replaceSpecialChars=function(regEx){
    if(!regEx){
        regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
    }
    return this.replace(regEx,'');

};


/**
 * 单个保存游客  新加flag标志 1标示提交数据 0标示不提交
 */
function saveTraveler(element, nFlag){
    //获取游客信息
    var _this = $(element);
    var travelerForm = _this.closest(".travelerTable");
    var orderId = $("#orderId").val();
    var airticketId = $("#airticketId").val();
    var travelId = travelerForm.find(":input[name='travelId']").val();
    var travelName = travelerForm.find(":input[name='travelerName']").val();
    var travelNamePinyin = travelerForm.find(":input[name='travelerPinyin']").val();
    var travelerSex = travelerForm.find(":input[name='travelerSex']").val();
    var nationality = travelerForm.find(":input[name='nationality']").val();
    var birthDay = travelerForm.find(":input[name='birthDay']").val();
    var telephone = travelerForm.find(":input[name='telephone']").val();
    var passportCode = travelerForm.find(":input[name='passportCode']").val();
    var passportValidity = travelerForm.find(":input[name='passportValidity']").val();
    var idCard = travelerForm.find(":input[name='idCard']").val();
    var remarks = travelerForm.find(":input[name='remarks']").val();
    var personType = travelerForm.find(":input[name='personType']:checked").val();
    var passportType = travelerForm.find(":input[name='passportType']").val();
    var intermodalType = travelerForm.find(":input[name='ydbz2intermodalType']:checked").val();
    var currencyId = $("#currencyId").val();
    var payPrice = travelerForm.find(":input[name='jsSrcPrice']").val();//游客结算价（包含税费，不包含联运价格）
    var intermodalId = null;

    //需要联运
    if(intermodalType == 1){
        intermodalId = travelerForm.find(":input[name='intermodal'] :selected").attr("id");
    }

    var obj = element;

    if($(obj).text()=="保存"){
        //游客姓名
        //var outerrorList = _doValidatetravelerForm(travelForm);
        //createDivInDiv(outerrorList);
        //if(outerrorList.length > 0) {
        //    return false;
        //}
        $(obj).parent().parent().find("input[name='travelerName']").each(function(index, obj) {
            if($(this).val() == "") {
                var name = $(this).parent().children("label").text();
                top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
                $(this).focus();
                throw "error！";
            }
        });

        if(birthDay != null && birthDay != ""){
            var curDate = new Date();
            if (new Date(birthDay) > curDate) {
                $.jBox.tip('出生日期不能大于当前时间', 'error', { focusId: 'birthDay' });
                return false;
            }
        }

        /**
        //选择联运时身份证号码必填(暂时没有这个要求，屏蔽掉)
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
        var adultNum = $("#orderPersonNumAdult").val();
        var childNum = $("#orderPersonNumChild").val();
        var specialNum = $("#orderPersonNumSpecial").val();
        if (countAdult() > adultNum) {
            top.$.jBox.tip('成人人数与初始值不匹配请修改', 'error');
            throw "error！";
        }
        if (countChild() > childNum) {
            top.$.jBox.tip('儿童人数与初始值不匹配请修改', 'error');
            throw "error！";
        }
        if (countSpecial() > specialNum) {
            top.$.jBox.tip('特殊人数与初始值不匹配请修改', 'error');
            throw "error！";
        }
    }
    
    var data = data || {};
    data['orderId']=orderId;
    data['airticketId']=airticketId;
    data['travelId']=travelId;
    data['travelName']=travelName;
    data['travelNamePinyin']=travelNamePinyin;
    data['travelerSex']=travelerSex;
    data['nationality']=nationality;
    data['birthDay']=birthDay;
    data['telephone']=telephone;
    data['passportCode']=passportCode;
    data['passportValidity']=passportValidity;
    data['idCard']=idCard;
    data['remarks']=remarks;
    data['personType']=personType;
    data['passportType']=passportType;
    data['intermodalType'] = intermodalType;
    data['intermodalId']= intermodalId;
    data['currencyId'] = currencyId;
    data['payPrice'] = payPrice;
    if(nFlag == 0){
        if(_this.text()=="保存"){
            _this.text("修改");
            _this.parent().prev().hide();
            _this.parent().parent().find('.tourist-t-off').css("display","inline");
            _this.parent().parent().find('.tourist-t-off em').html(travelName);
            _this.parent().parent().find('.tourist-t-off').find(".ydFont2").text(payPrice);
            _this.parent().parent().find('.tourist-t-on').hide();
            _this.parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd');
        }else{
            _this.text("保存");
            _this.parent().prev().show();
            _this.parent().parent().find('.tourist-t-off').css("display","none");
            _this.parent().parent().find('.tourist-t-on').show();
            _this.parent().parent().find('.tourist-t-on').find("input[name='personType']").attr("disabled","disabled");//游客中的人员类型不允许修改
            _this.parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd');
        }
        return;
    }
    
    var rebatesMoney = $("input[name=rebatesMoney]",travelerForm).val();	//返佣费用金额
    var rebatesCurrencyId = $("select[name=rebatesCurrency]",travelerForm).val();	//返佣费用币种
    
    //ajax请求
    $.ajax({
        type:"POST",
        //url:"${ctx}/order/manage/airticketOrderTravel",
        data:data,
        success:function(data){
            if(data.result == 'success' && data.msg != ""){
                travelForm.find(":input[name='travelId']").val(data.msg);
            }
            var input=_this.parent().parent().find("input");
            var textarea=_this.parent().parent().find("textarea");
            var selects=_this.parent().parent().find("select");
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
    }
});
}

/**
 * 联运：需要/不需要 修改 *
 */
function ydbz2intermodalSale(obj){
    if($(obj).val()==0){
    	//当联运选择为不需要时，更改游客同行价和结算价（模拟需要时，点击请选择）
    	 $(obj).parent().parent().find('#intermodalAreaChoose').children('option').each(function(){
    		 if($(this).attr('id') == '-1') {
    			 $(this).attr("selected", "selected");
    			 ydbz2interselectSale($(obj).parent().parent().find('#intermodalAreaChoose')[0]);
    		 }
    	 });
    	 $(obj).parent().parent().find('span').hide();
    } else{
        $(obj).parent().parent().find('span').show();
    }
    var thisPersonType=$(obj).closest("form").find("input[name='personType']:checked");
}

/**
 * 联运显示价格
 */
function ydbz2interselectSale(obj){
	// 改变之后的联运价格
    var value = $(obj).find("option:selected").val();
    // 改变之后的币种名称
    var currencyName = $(obj).find("option:selected").attr('currenyName');
    // 改变之后的币种id
    var currencyId = $(obj).find("option:selected").attr('currenyId');
    // 联运id
    var intermodalId = $(obj).find("option:selected").attr('id');
    // 联运币种名称
    $(obj).parent().find('.intermodalCurrencyName').text(currencyName);
    // 联运金额
    $(obj).parent().find('.intermodalMoneyAmount').text(value);
    // 联运币种
    $(obj).parent().find('.intermodalCurrencyId').val(currencyId);
    // 更改游客同行价
    travelerTradePrice();
    // 改变之前的币种名称
    var orgIntermodalCurrencyName = $(obj).parent().find('.orgIntermodalCurrencyName').val();
    // 改变之前的联运价格
    var orgIntermodalMoneyAmount = $(obj).parent().find('.orgIntermodalMoneyAmount').val();
    
    // 先将查出来的联运价格从结算价中减去
    $(obj).parents("form").find(".jsPriceCurrencyNameClass").each(function(){
    	var jsPrice = $(this).next().attr("data");
    	var jsPriceCurrencyName = $(this).text();
    	if(jsPriceCurrencyName == orgIntermodalCurrencyName) {
    		if(jsPrice == orgIntermodalMoneyAmount) {
    			$(this).prev([name='addSign']).remove();
    			this.nextSibling.remove();
    			$(this).next([name='jsPrice']).remove();
    			$(this).remove();
    		} else if(Number(jsPrice) > Number(orgIntermodalMoneyAmount)) {
    			var jsPriceReduce = Number(jsPrice) - Number(orgIntermodalMoneyAmount);
    			$(this).next().val(jsPriceReduce);
    			$(this).next().attr("data", jsPriceReduce);
    		}
    	}
    });
    // 再将改后的联运价格加到结算价中
    $(obj).parents("form").find(".jsPriceCurrencyNameClass").each(function(){
    	var jsPrice = $(this).next().attr("data");
    	var jsPriceCurrencyName = $(this).text();
    	if(jsPriceCurrencyName == currencyName) {
    		var jsPriceAdd = Number(value) + Number(jsPrice);
    		$(this).next().val(jsPriceAdd);
    		$(this).next().attr("data", jsPriceAdd);
    	} else if(jsPriceCurrencyName != currencyName && currencyName != "无") {
    		var appendString = "<span name='addSign'>+</span>";
    		appendString += "<em class='jsPriceCurrencyNameClass' style='vertical-align: baseline;'>"+ currencyName +"</em>:";
    		appendString += "<input type='text' maxlength='10' class='jsPriceClass' name='jsPrice' value="+ value +" data="+ value +" ";
    		appendString += "onblur='changeClearPrice(this)' onafterpaste='seetlementKeyUp(this))' onkeyup='seetlementKeyUp(this)' ";
    		appendString += "data-currencyid='"+ currencyId +"' data-src='"+ value +"'/>";
    		$(this).parent().append(appendString);
    	}
    });
    // 保存修改之前的联运价格
    $(obj).parent().find('.orgIntermodalCurrencyName').val(currencyName);
    $(obj).parent().find('.orgIntermodalMoneyAmount').val(value);
    $(obj).parent().find('.orgIntermodalCurrencyId').val(currencyId);
    $(obj).parent().find('.orgIntermodalId').val(intermodalId);
    // 改变订单总结算价
    fn_changeTotalJsPrice4Intermodal();
    // 改变订单总同行价
    fn_changeTotalTradePrice();
};

/**
 * 选择不同人群，改变结算价
 */
function changePersonType(element){
    var _this = $(element);
    var _travelerForm = _this.closest(".travelerTable");
    var personType = _this.val();

    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    // 改变游客类型时，需要先将当前游客的结算价从总的数组中减去
    _travelerForm.find("[name='jsPrice']").each(function(){
        var currentAmount = $(this).attr("data");
        var tradePrice = $(this).attr("data-src")
        var currentCurrencyId = $(this).attr("data-currencyid");

        var subJsObj = new Object();
        subJsObj.je = -Number(currentAmount);
        subJsObj.currencyId = currentCurrencyId;
        jsarray.push(subJsObj);
        var jsj=getbzStr(jsarray);
        if(jsj==""){
            $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
        }else{
            $("#travelerSumClearPrice").text(jsj);
        }

        var subTradeObj = new Object();
        subTradeObj.je = -Number(tradePrice);
        subTradeObj.currencyId = currentCurrencyId;
        tradeTotalPriceArray.push(subTradeObj);
        var thj=getbzStr(tradeTotalPriceArray);
        if(thj==""){
            $("#travelerSumPrice").text(airticketbzObj.currencyName+"0.00");
        }else{
            $("#travelerSumPrice").text(thj);
        }

    });
    // todo changePayprice 改变游客类型时，需要先将当前游客同行价从总的数组中减去

    var selSrcPriceCurrency = $.parseJSON($("#airticketbz").val()).currencyId;			//游客结算价、同行价币种id
    var selSrcPriceCurrencyMark  = $.parseJSON($("#airticketbz").val()).currencyMark;	//游客结算价、同行价币种标识
    var selSrcPriceCurrencyName = $.parseJSON($("#airticketbz").val()).currencyName;	//游客结算价、同行价币种名称
    
    var crj = $('#crj').val() == "" ? 0 : $("#crj").val();								//成人价
    var etj = $('#etj').val() == "" ? 0 : $("#etj").val();								//儿童价
    var tsj = $('#tsj').val() == "" ? 0 : $("#tsj").val();								//特殊价
    var texamt = $("#texamt").val();													//税费
    
    var selJsPrice = "";
    if(personType == '1') {
    	selJsPrice = crj;
    } else if(personType == '2') {
    	selJsPrice = etj;
    } else if(personType == '3') {
    	selJsPrice = tsj;
    }
    
    // 填充人员类型
    $("input[name=personType]",_travelerForm)[personType-1].checked = true;
    // 将联运改为不需要
    $(element).parent().parent().find("[name='ydbz2intermodalType'][value='0']").attr("checked", true);
    _travelerForm.find("#intermodalAreaChoose").find("option[id='-1']").attr("selected", true);
    // 改变之后的联运价格
    var value = _travelerForm.find("#intermodalAreaChoose").find("option:selected").val();
    // 改变之后的币种名称
    var currencyName = _travelerForm.find("#intermodalAreaChoose").find("option:selected").attr('currenyName');
    // 改变之后的币种id
    var currencyId = _travelerForm.find("#intermodalAreaChoose").find("option:selected").attr('currenyId');
    // 联运id
    var intermodalId = _travelerForm.find("#intermodalAreaChoose").find("option:selected").attr('id');
    // 联运币种名称
    _travelerForm.find('.intermodalCurrencyName').text(currencyName);
    // 联运金额
    _travelerForm.find('.intermodalMoneyAmount').text(value);
    // 联运币种
    _travelerForm.find('.intermodalCurrencyId').val(currencyId);
    // 将需要联运隐藏掉
    _travelerForm.find("#intermodalNeed").parent().next().hide();

    // 计算游客结算价（税费+单价）
    var texamtVal = $("#texamt").val();
    var jsPriceVal = Number(texamtVal) + Number(selJsPrice);
    // 填充游客内部显示的结算价
    var appendString = "";
	appendString += "<em class='jsPriceCurrencyNameClass' style='vertical-align: baseline;'>"+ selSrcPriceCurrencyName +"</em>:";
	appendString += "<input type='text' maxlength='10' class='jsPriceClass' name='jsPrice' value="+ jsPriceVal +" data="+ jsPriceVal +" ";
	appendString += "onblur='changeClearPrice(this)' onafterpaste='seetlementKeyUp(this))' onkeyup='seetlementKeyUp(this)' ";
	appendString += "data-currencyid='"+ selSrcPriceCurrency +"' data-src='"+ jsPriceVal +"'/>";
    $("span[data=newJsPrice]",_travelerForm).html(appendString);
    var appendStringHidden = "<input type='hidden' name='jsSrcPrice' value='"+ jsPriceVal +"'/>";
    appendStringHidden += "<input type='hidden' name='jsSrcPriceCurrencyId' value='"+ selSrcPriceCurrency +"'/>";
    $("span[data=newJsPrice]",_travelerForm).next().remove();
    $("span[data=newJsPrice]",_travelerForm).next().remove();
    $("span[data=newJsPrice]",_travelerForm).after(appendStringHidden);
    // 默认情况下，新添加的游客同行价和结算价是相同的
    var tradePrice = selSrcPriceCurrencyName + jsPriceVal.toString().formatNumberMoney('#,##0.00');
    var tradePriceHidden = "<input type='hidden' class='traveler travelerSrcPrice' value='"+ selJsPrice +"'/>";
    tradePriceHidden += "<input type='hidden' class='traveler travelerSrcPriceCurrencyName' value='"+ selSrcPriceCurrencyName +"'/>";
    tradePriceHidden += "<input type='hidden' class='traveler travelerSrcPriceCurrencyId' value='"+ selSrcPriceCurrency +"'/>";
    $("span[data=tradePrice]",_travelerForm).html(tradePrice);
    $("span[data=tradePrice]",_travelerForm).next().remove();
    $("span[data=tradePrice]",_travelerForm).next().remove();
    $("span[data=tradePrice]",_travelerForm).next().remove();
    $("span[data=tradePrice]",_travelerForm).after(tradePriceHidden);

    // 改变游客类型后，需要将改变后的结算价加到总结算价和总同行价中
    _travelerForm.find("[name='jsPrice']").each(function(){
        var currentAmount = $(this).attr("value");
        var currentCurrencyId = $(this).attr("data-currencyid");
        var addObj = new Object();
        addObj.je = Number(currentAmount);
        addObj.currencyId = currentCurrencyId;
        jsarray.push(addObj);
        var jsj=getbzStr(jsarray);
        if(jsj==""){
            $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
        }else{
            $("#travelerSumClearPrice").text(jsj);
        }

        tradeTotalPriceArray.push(addObj);
        var thj=getbzStr(tradeTotalPriceArray);
        if(thj==""){
            $("#travelerSumPrice").text(airticketbzObj.currencyName+"0.00");
        }else{
            $("#travelerSumPrice").text(thj);
        }

    });

    // todo changePayprice 改变游客类型后，需要将改变后的同行价加到总结算价和总同行价中
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
		if($("#orderContact_modifiability").val() == 1){
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