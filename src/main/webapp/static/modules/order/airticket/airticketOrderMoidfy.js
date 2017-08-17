var formatNumberArray = new Array();
var jsTotalPrice = null;
var outerrorList = null;
//拉美途uuid
var lameitourUuid = '7a81a26b77a811e5bc1e000c29cf2586';
$(function(){
    // 计算游客编号
    recountIndexTraveler();
    // 添加游客点击事件
    addTraveler();
    // 计算结算总价
    calculateJsTotalPrice();
    // 计算同行总价
    calculateThTotalPrice();
    // 计算游客同行价
    travelerTradePrice();
    // 是否显示添加游客按钮
    isShowAddTravelerBtn();
    // 设置游客显示效果
    travelerEffect();
    // 删除游客时触发
    delTraveler();
    // 游客名称失去焦点时触发
    getTravelerNamePinyin();
    // 下一步 上一步 保存
    modifyOneSecondSave();
    // 游客Form头结算价显示
    showTravelerJsPrice();

});

/**
 * 计算同行总价(当前所有游客数量+所有联运价格)
 */
function calculateThTotalPrice() {
    var thTotalPrice = new Array();
    // 各类型的游客数量
    var adultNum = $("#orderPersonNumAdult").val();
    var childNum = $("#orderPersonNumChild").val();
    var specialNum = $("#orderPersonNumSpecial").val();

    // 各类型游客单价和税费之和
    var crjTexamt = Number($("#texamt").val()) + Number($("#crj").val());
    var etjTexamt = Number($("#texamt").val()) + Number($("#etj").val());
    var tsjTexamt = Number($("#texamt").val()) + Number($("#tsj").val());

    // 所有游客单价和税费之和
    var thPirce = new Object();
    thPirce.currencyId = $("#currencyId").val();
    thPirce.je = Number(adultNum) * crjTexamt + Number(childNum) * etjTexamt + Number(specialNum) * tsjTexamt;
    thTotalPrice.push(thPirce);

    // 计算所有的联运价格
    $("form.travelerTable").each(function() {
        var intermodalMoneyAmount = $(this).find(".orgIntermodalMoneyAmount").val();
        var intermodalCurrencyId = $(this).find(".orgIntermodalCurrencyId").val();
        var intermodalPrice = new Object();
        intermodalPrice.je = intermodalMoneyAmount;
        intermodalPrice.currencyId = intermodalCurrencyId;
        thTotalPrice.push(intermodalPrice);
    });

    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    var thj=getbzStr(thTotalPrice);
    if(thj==""){
        $("#travelerSumPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumPrice").text(thj);
    }
}

/**
 * 计算结算总价
 */
function calculateJsTotalPrice() {
    // 添加的游客数量
    var adultNum = $("#orderPersonNumAdult").val();
    var childNum = $("#orderPersonNumChild").val();
    var specialNum = $("#orderPersonNumSpecial").val();

    // 游客form数量
    var adultTravelerFroms = $("form.travelerTable").find("[name='personType'][value='1']:checked");
    var childTravelerFroms = $("form.travelerTable").find("[name='personType'][value='2']:checked");
    var specialTravelerFroms = $("form.travelerTable").find("[name='personType'][value='3']:checked");

    var adultJsTotalPrice = calculateAdultJsTotalPrice(adultTravelerFroms, adultNum);

    var childJsTotalPrice = calculateChildJsTotalPrice(childTravelerFroms, childNum);

    var specialJsTotalPrice = calculateSpecialJsTotalPrice(specialTravelerFroms, specialNum);

    jsTotalPrice = new Array();
    jsTotalPrice = jsTotalPrice.concat(adultJsTotalPrice, childJsTotalPrice, specialJsTotalPrice);

    var airticketbz = $("#airticketbz").val();//币种
    var airticketbzObj = $.parseJSON(airticketbz);//机票币种
    var jsj=getbzStr(jsTotalPrice);
    if(jsj==""){
        $("#travelerSumClearPrice").text(airticketbzObj.currencyName+"0.00");
    }else{
        $("#travelerSumClearPrice").text(jsj);
    }

}

/**
 * 计算成人总结算价
 */
function calculateAdultJsTotalPrice(obj, travelerSize) {
    var adultJsTotalPrice = new Array();
    // 获取成人单价和税费之和
    var crjTexamt = Number($("#texamt").val()) + Number($("#crj").val());//税费
    var currencyId = $("#currencyId").val();

    if(travelerSize > $(obj).length) {
        var notAdultFormSize =  travelerSize - $(obj).length;           // 添加的成人游客数量减去成人表单数量
        var adultJsPrice = new Object();
        adultJsPrice.currencyId =currencyId;
        adultJsPrice.je = crjTexamt * notAdultFormSize;
        adultJsTotalPrice.push(adultJsPrice);
    }

    // 计算每个成人表单结算价
    $(obj).each(function() {
        $(this).parents("form.travelerTable").find(".jsPriceClass").each(function() {
            var adultJsPrice = new Object();
            adultJsPrice.currencyId =$(this).attr("data-currencyid");
            adultJsPrice.je = $(this).val();
            adultJsTotalPrice.push(adultJsPrice);
        })
    });

    return adultJsTotalPrice;
}

/**
 * 计算儿童总结算价
 */
function calculateChildJsTotalPrice(obj, travelerSize) {
    var childJsTotalPrice = new Array();
    // 获取儿童单价和税费之和
    var etjTexamt = Number($("#texamt").val()) + Number($("#etj").val());//税费
    var currencyId = $("#currencyId").val();

    if(travelerSize > $(obj).length) {
        var notChildFormSize =  travelerSize - $(obj).length;           // 添加的儿童游客数量减去成人表单数量
        var childJsPrice = new Object();
        childJsPrice.currencyId =currencyId;
        childJsPrice.je = etjTexamt * notChildFormSize;
        childJsTotalPrice.push(childJsPrice);
    }

    // 计算每个儿童表单结算价
    $(obj).each(function() {
        $(this).parents("form.travelerTable").find(".jsPriceClass").each(function() {
            var childJsPrice = new Object();
            childJsPrice.currencyId =$(this).attr("data-currencyid");
            childJsPrice.je = $(this).val();
            childJsTotalPrice.push(childJsPrice);
        })
    });

    return childJsTotalPrice;

}

/**
 * 计算特殊人群总结算价
 */
function calculateSpecialJsTotalPrice(obj, travelerSize) {
    var specialJsTotalPrice = new Array();
    // 获取特殊人群单价和税费之和
    var tsjTexamt = Number($("#texamt").val()) + Number($("#tsj").val());//税费
    var currencyId = $("#currencyId").val();

    if(travelerSize > $(obj).length) {
        var notSpecialFormSize =  travelerSize - $(obj).length;           // 添加的特殊人群游客数量减去成人表单数量
        var specialJsPrice = new Object();
        specialJsPrice.currencyId =currencyId;
        specialJsPrice.je = tsjTexamt * notSpecialFormSize;
        specialJsTotalPrice.push(specialJsPrice);
    }

    // 计算每个特殊人群表单结算价
    $(obj).each(function() {
        $(this).parents("form.travelerTable").find(".jsPriceClass").each(function() {
            var specialJsPrice = new Object();
            specialJsPrice.currencyId =$(this).attr("data-currencyid");
            specialJsPrice.je = $(this).val();
            specialJsTotalPrice.push(specialJsPrice);
        })
    });

    return specialJsTotalPrice;

}

/**
 * 计算游客结算价
 */
function changeJsPrice(obj) {
    calculateJsTotalPrice();
    showTravelerJsPrice();
}

/**
 * 修改数量不能小于当前数量，特殊人数，余位不足和切位不足等。
 */
function checkFreePosition(obj, num) {
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

    var idVal = $(obj).attr("id");              // 当前标签 id
    var val = parseInt($(obj).val());           // 当前标签值

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
    isShowAddTravelerBtn();
    calculateJsTotalPrice();
    calculateThTotalPrice();
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
    var currencyName = $(obj).find("option:selected").attr('currenyname');
    // 改变之后的币种id
    var currencyId = $(obj).find("option:selected").attr('currenyid');
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
            appendString += "<input type='text' maxlength='8' class='jsPriceClass ipt-rebates' name='jsPrice' value="+ value +" data="+ value +" ";
            appendString += "onblur='changeJsPrice(this)' data-type='amount'";
            appendString += "data-currencyid='"+ currencyId +"' data-src='"+ value +"'/>";
            $(this).parent().append(appendString);
        }
    });
    // 保存修改之前的联运价格
    $(obj).parent().find('.orgIntermodalCurrencyName').val(currencyName);
    $(obj).parent().find('.orgIntermodalMoneyAmount').val(value);
    $(obj).parent().find('.orgIntermodalCurrencyId').val(currencyId);
    $(obj).parent().find('.orgIntermodalId').val(intermodalId);
    // 计算订单总结算价
    calculateJsTotalPrice();
    calculateThTotalPrice();
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
        $(this).parents("form.travelerTable").find(".showThPrice").text(travelerPrice);
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
        // 校验游客类型
        var adultNum = $("#orderPersonNumAdult").val();
        var childNum = $("#orderPersonNumChild").val();
        var specialNum = $("#orderPersonNumSpecial").val();

        var adultFormSize = countAdult();
        var childFormSize = countChild();
        var specialFormSize = countSpecial();

        if(adultNum <= adultFormSize) {
            _travelerForm.find("input[name=personType][value='1']").attr("disabled", true);
        }
        if(childNum <= childFormSize) {
            _travelerForm.find("input[name=personType][value='2']").attr("disabled", true);
        }
        if(specialNum <= specialFormSize) {
            _travelerForm.find("input[name=personType][value='3']").attr("disabled", true);
        }

        // 计算游客结算价（税费+单价）
        var texamtVal = $("#texamt").val();
        var jsPriceVal = Number(texamtVal) + Number(selJsPrice);
        // 填充游客内部显示的结算价
        var appendString = "";
        appendString += "<em class='jsPriceCurrencyNameClass' style='vertical-align: baseline;'>"+ selSrcPriceCurrencyName +"</em>:";
        appendString += "<input type='text' maxlength='8' class='jsPriceClass ipt-rebates' name='jsPrice' value="+ jsPriceVal +" data="+ jsPriceVal +" ";
        appendString += "onblur='changeJsPrice(this)' data-type='amount'";
        appendString += "data-currencyid='"+ selSrcPriceCurrency +"' data-src='"+ jsPriceVal +"'/>";
        $("span[data=newJsPrice]",_travelerForm).html(appendString);
        // 默认情况下，新添加的游客同行价和结算价是相同的
        var tradePrice = selSrcPriceCurrencyName + jsPriceVal.toString().formatNumberMoney('#,##0.00');
        var tradePriceHidden = "<input type='hidden' class='traveler travelerSrcPrice' value='"+ selJsPrice +"'/>";
        tradePriceHidden += "<input type='hidden' class='traveler travelerSrcPriceCurrencyName' value='"+ selSrcPriceCurrencyName +"'/>";
        tradePriceHidden += "<input type='hidden' class='traveler travelerSrcPriceCurrencyId' value='"+ selSrcPriceCurrency +"'/>";
        $("span[data=tradePrice]",_travelerForm).html(tradePrice);
        $("span[data=tradePrice]",_travelerForm).after(tradePriceHidden);
        // 计算游客编号
        recountIndexTraveler();
        // 填充游客默认姓名 （“游客  + index”）
		fillTravelerDefaultName();
        // 添加游客之后，要判断是否继续显示添加游客按钮
        isShowAddTravelerBtn();
        // 计算游客同行价
        travelerTradePrice();
        showTravelerJsPrice();
    });
}

/**
 * 单个保存游客
 */
function saveTraveler(element){
    var _this = $(element);
    var travelerForm = _this.closest(".travelerTable");
    var obj = element;
    if($(obj).text()=="保存"){
        $(obj).parent().parent().find("input[name='travelerName']").each(function(index, obj) {
            if($(this).val() == "") {
                var name = $(this).parent().children("label").text();
                top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
                $(this).focus();
                throw "error！";
            }
        });

        var birthDay = travelerForm.find(":input[name='birthDay']").val();
        if(birthDay != null && birthDay != ""){
            var curDate = new Date();
            if (new Date(birthDay) > curDate) {
                $.jBox.tip('出生日期不能大于当前时间', 'error', { focusId: 'birthDay' });
                return false;
            }
        }

        //结算价不能为空
        travelerForm.find(".jsPriceClass").each(function() {
            if($(this).val() == "") {
                $.jBox.tip('请填写结算价！','warning');
                $(this).focus();
                throw "error！";
                return;
            }
        });

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
    var travelName = travelerForm.find(":input[name='travelerName']").val();
    var payPrice = travelerForm.find(":input[name='jsSrcPrice']").val();
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
}

/**
 * 删除游客
 */
function delTraveler(){
    $("#traveler").delegate("a[name='deleteTraveler']", "click", function() {
        var $this = $(this);
        $.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                // 显示按钮
                var travelerTableSize = $(".travelerTable").length;
                if(getTotalNum() > travelerTableSize) {
                    $("#addTraveler").show();
                } else {
                    $("#addTraveler").hide();
                }
                $this.closest('.travelerTable').remove();
                // 判断是否显示添加游客按钮
                isShowAddTravelerBtn();
                calculateJsTotalPrice();
                calculateThTotalPrice();
            }
        });
    });
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

        var travelerTables = $("#traveler form.travelerTable");
        var notSaveTravelerName = [];
        var notSaveTravelerIndex = [];
        $.each(travelerTables,function(key,value){
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
        _doValidateorderpersonMesdtail();
        if(travelerTables.length > 0){
            for(var i = 0;i < travelerTables.length; i++){
                _doValidatetravelerForm(travelerTables[i]);
            }
        }
        createDivInDiv(outerrorList);
        if (outerrorList.length > 0) {
            return;
        }
        //disabled联系人诸多信息（注意：必填项被disabled之后便不能正常validate了）
        readOnlyAllContactInfo();

        $("a[name='savePeople']:visible").each(function(index, obj) {
            $(this).trigger("click");
        });

        $("#productOrderTotal").disableContainer( {
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

        // 隐藏按钮，防止多次提交
        $("#firstDiv").hide();
        $("#secondDiv").show();
        $("#ordercontact").find(".yd1AddPeople").hide();
        
        // 金额字体过小，把影响字体样式去掉
		$("span[data=newJsPrice] span").removeClass("disabledshowspan");
    });

    // 第二步点击上一步
    $("#modSecondToOneStepDiv").click(function() {
    	// 还原样式
		$("span[data=newJsPrice] span").addClass("disabledshowspan");
        $("#manageOrder_new").undisableContainer();
        $("#manageOrder_m").undisableContainer();

        $("#productOrderTotal").undisableContainer();

        $("#traveler form.travelerTable").each(function() {
            $(this).find("a[name='savePeople']").text("修改");
        });
        travelerEffect();
        back2WritableContactInfo();
        $("#firstDiv").show();
        $("#secondDiv").hide();
        $("#ordercontact").find(".yd1AddPeople").show();
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
        if(agentinfoId == -1){agentId = agentinfoId;}
        var currencyId = $("#currencyId").val();
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
        var orderTotalClearPrice = jsTotalPrice;
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

            var srcPrice = 0.00;
            if(personType == '1') {
                srcPrice = Number($("#crj").val());
            } else if(personType == '2') {
                srcPrice = Number($("#etj").val());
            } else if(personType == '3') {
                srcPrice = Number($("#tsj").val());
            }

            // 联运信息
            var isIntermodal = $(this).find("[name='ydbz2intermodalType']:checked").val();                  // 是否需要联运（1需要 0不需要）
            var IntermodalCurrencyName = $(this).find(":hidden.orgIntermodalCurrencyName").val();           // 联运币种名称
            var intermodalMoneyAmount = $(this).find(":hidden.orgIntermodalMoneyAmount").val();             // 联运金额
            var intermodalCurrencyId = $(this).find(":hidden.orgIntermodalCurrencyId").val();
            var IntermodalId = $(this).find(":hidden.orgIntermodalId").val() == undefined ? "" : $(this).find(":hidden.orgIntermodalId").val();// 联运id
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
                jsPrice.jsAmount = $(this).val();
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
            traveler.srcPriceCurrency = currencyId;
            traveler.srcPrice = srcPrice;

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
            	orderCompanyName : $("input[name=orderCompanyName]").val(),
                adultNum : adultNum,
                childNum : childNum,
                specialNum : specialNum,
                orgPersonNum : orgPersonNum,
                newPersonNum : newPersonNum,
                agentId : agentId,
                orderContacts : JSON.stringify(ordercontacts),
                comments : comments,
                orderTotalClearPrice : JSON.stringify(orderTotalClearPrice),
                groupRebatesCurrency : groupRebatesCurrency,
                groupRebatesMoney : groupRebatesMoney,
                travelers : JSON.stringify(travelers),
                orderId : orderId,
                airticketId : airticketId,
                placeHolderType : placeHolderType
            },
            success : function(data) {
                if(data.result == "1") {
                    $.jBox.tip("保存成功!", "success");
                    var queryType = $("#queryType").val();
                    window.location.href = "../../airticketOrderList/manage/airticketOrderList/"+queryType+"/";
                } else if(data.result == "2") {
                    $.jBox.tip(data.msg, "error");
                } else {
                    $.jBox.tip("程序出错!", "error");
                }
                submit_times = 0;
            }
        });
    });

}

/**
 * 设置游客的显示效果
 */
function travelerEffect(){
    // 处理除了第一个游客其他游客内容收缩
    var _add_seachcheck = $("#traveler .add_seachcheck");
    for(var i = 0; i< _add_seachcheck.length;i++){
        if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
            _add_seachcheck.eq(i).click();
        }
    }
}

/**
 * 游客Form头结算价显示
 */
function showTravelerJsPrice() {
    $("form.travelerTable").each(function() {
        var travelerJsPrice = new Array();
        $(this).find(".jsPriceClass").each(function(){
            var jsPrice = new Object();
            jsPrice.je = $(this).val();
            jsPrice.currencyId = $(this).attr("data-currencyid");
            travelerJsPrice.push(jsPrice);
        });
        var airticketbz = $("#airticketbz").val();//币种
        var airticketbzObj = $.parseJSON(airticketbz);//机票币种
        var tJsPrice=getbzStr(travelerJsPrice);
        if(tJsPrice==""){
            $(this).find(".showJsPrice").text(airticketbzObj.currencyName+"0.00");
        }else{
            $(this).find(".showJsPrice").text(tJsPrice);
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

//-------------------------
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

function adderrorList(outerrorList, errorList) {
    outerrorList = outerrorList.concat(errorList);
    return outerrorList;
}

//------------------------ 工具方法

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
//	if ($("#companyUuid").val() == lameitourUuid) { // 0569需求,做成全平台
		var travelerTables = $("#traveler form");
		var $thisTraveler = $(travelerTables[travelerTables.length - 1]);
		var tempName = "游客" + travelerTables.length;
		// 填充输入框姓名
		$thisTraveler.find("input[name=travelerName]").val(tempName);
		// 填充展示span姓名
		$thisTraveler.find("span[name=tName]").html(tempName);
//	}
}

/**
 * 判断是否显示添加游客按钮
 */
function isShowAddTravelerBtn() {
    var travelerTableSize = $(".travelerTable").length;
    if(getTotalNum() > travelerTableSize) {
        $("#addTraveler").show();
    } else {
        $("#addTraveler").hide();
    }
}

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
 * 默认选中中国
 */
$(document).ready(function() {
    $(':input[name=nationality] option[value="461"]').attr('selected',true);
});

/**
 * 订单修改，第一步“下一步”，readonly所有联系人所有信息，隐藏增删按钮
 * @param errorList
 */
function readOnlyAllContactInfo(){
	//渠道选择只读
	$("#modifyAgentInfo").attr("disabled", true).parent().find("span[name=showAgentName]").remove();
	//联系人
	$("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		//下拉控件隐藏，追加span(先删)
		var contactSpan = $(element).find("span[name=channelConcat]");
		contactSpan.hide().parent().find("span[name=showName]").remove();
		contactSpan.after("<span name='showName'>" + contactSpan.find("input[name=contactsName]").val() + "</span>");
		//其他input disabled掉		
		$(element).find("input").attr("disabled", true);
		//增删按钮隐藏
		$(element).find("span[name=addContactButton]").hide();
		$(element).find("span[name=delContactButton]").hide();
    });
	//关闭渠道商	
	$("#orderCompanyName").attr("disabled", true);
	$("#agentShow").attr("disabled", true);
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
		if($("#orderContact_modifiability").val() == 1){
			//所有input 可写
			$(element).find("input").attr("disabled", false);
		} else {
			$(element).find("input[name=remark]").attr("disabled", false);
		}
		//增删按钮隐藏
			$(element).find("span[name=addContactButton]").show();
			$(element).find("span[name=delContactButton]").show();
		
	});
	//放开渠道商	
	if($("#agentinfo_modifiability") && $("#agentinfo_modifiability").val() == '1'){		
		$("#orderCompanyName").attr("disabled", false);
		$("#agentShow").attr("disabled", false);
	}
}

//112 特殊需求过滤特殊字符
String.prototype.replaceSpecialDemand= function(regEx){
    if (!regEx){
        regEx = /[\“\”\‘\’\"\'\<\>\\]/g;
    }
    return this.replace(regEx, '');
};