var $tableTaxDictionary;
var $tableTaxExcept;
var ctx;


function getTravelTypesByTaxExceptionType(taxExceptionType){
    //todo:需要再处理
    return $.parseJSON($("#travelerTypes").val());
}
function checkDuplicateTaxExceptDate(taxTypes, travelTypes, exceptionType, exceptionName) {
    var errorString = "";
    var isPassed = true;
    var duplicatedList = [];
    for (var i in taxTypes) {
        var duplicatedList_taxType = [];
        for (var j in travelTypes) {
            var duplicatedList_travel = [];
            var periods = getCheckingExceptPeriod(exceptionType, taxTypes[i].taxType, travelTypes[j].uuid);
            checkReduplicated(periods, duplicatedList_travel);
            if (duplicatedList_travel.length > 0) {
                duplicatedList_taxType.push({
                    travelType: travelTypes[j],
                    duplicatedList_travel: duplicatedList_travel
                });
            }
        }
        if (duplicatedList_taxType.length > 0) {
            duplicatedList.push({
                taxType: taxTypes[i],
                duplicatedList_taxType: duplicatedList_taxType
            });
        }
    }
    if (duplicatedList.length > 0) {
        isPassed = false;
        infoBox("您填写的日期区间有重复或交叉，请重新输入。");
    }
    return isPassed;
}

/**
 * 保存酒店价单节日餐
 */
$(document).on("click", "#btnSaveTax", function (e) {
	
	var status = 1;
	//当updateFlag为1或相应模块的保存状态为1时
	if($("#updateFlag").val() == "2" || $("#hotelTaxSaveFlag").val() == "2") {
		status = 2;
	} else {
		$("#hotelTaxSaveFlag").val("2");
	}
	
	var url = ctx + "/hotelPl/saveHotelPlTax";
	var hotelPlTaxPriceJsonData = JSON.stringify(getSaveTaxDictionary());
	var hotelPlTaxExceptionJsonData = JSON.stringify(getSaveTaxException());
	
	var taxArithmetic = $("input[name='taxArithmetic']:checked").val(); 
	ajaxStart();
	$.ajax({
		type: "POST",
	   	url: url,
	   	async: false,
	   	data: {
				"hotelPlTaxPriceJsonData":hotelPlTaxPriceJsonData,
				"hotelPlTaxExceptionJsonData":hotelPlTaxExceptionJsonData,
				"status":status,
				"hotelPlUuid":baseInfo.uuid,
				"taxArithmetic":taxArithmetic
			  },
		dataType: "json",
	   	success: function(data){
	   		ajaxStop();
	   		if(data){
	   			if(data.result == 1) {
	   				$.jBox.tip("酒店税金保存成功！");
	   			} else if(data.result == 2) {
	   				$.jBox.tip("酒店税金更新成功！");
	   			} else if(data.result == 3) {
	   				$.jBox.tip(data.message);
	   			}
	   		}
	   	}
	});
	saveButtonCli($(this));
});


$(document).ready(function () {
	ctx = $("#hotelPlCtx").val();
	
    $tableTaxDictionary = $("#tableTaxDictionary");
    $tableTaxExcept = $("#tableTaxExcept");
    $tableTaxExcept.find('[name="taxtExceptionTravelList"]').each(function(){
        var $this = $(this);
        var taxExceptionType=$this.parents("tr:first").attr("exception-type");
        var currentTravelTypes = getTravelTypesByTaxExceptionType(taxExceptionType);
        for(var i in currentTravelTypes){
            var  travelType = currentTravelTypes[i];
            $this.append('<label class="input_label_sm"> <input type="checkbox" style="margin: 0px" name="travelTypeUuid" value="'+travelType.uuid+'" traveler-type="'+travelType.uuid+'" />'+travelType.name+'</label>');
        }
    });

    $("#btnEditTax").click(function () {
        $(this).text("修改");
    });

    //添加税金期间
    $tableTaxDictionary.on("click", ".price_sale_house_01", function () {
        var $tr = $(this).parents("tr:first");
        var taxType = $tr.attr("tax-type");

        //获取当前税金的第一行的税金名称单元格 并将税金名称单元格跨的行数增加1
        var $tdTaxName = $tableTaxDictionary.find("tr[tax-type='" + taxType + "']:first td[name='taxName']");
        var originalRowSpan = parseInt($tdTaxName.attr("rowspan"));
        if (isNaN(originalRowSpan)) {
            originalRowSpan = 1;
        }
        $tdTaxName.attr("rowspan", originalRowSpan + 1);
        //复制一行，并初始化数据，添加到对应的税金后
        var $new_tr = $tr.clone();
        $new_tr.find("td[name='taxName']").remove();
        $new_tr.find("input[type=text]").val("");
        $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
        $tableTaxDictionary.find("tr[tax-type='" + taxType + "']:last").after($new_tr);
    });
    //删除税金期间
    $tableTaxDictionary.on("click", ".price_sale_house_02", function () {
        var $tr = $(this).parents("tr:first");
        var taxType = $tr.attr("tax-type");
        //获取当前税金的第一行的税金名称单元格 并将税金名称单元格跨的行数减少1
        var $tdTaxName = $tableTaxDictionary.find("tr[tax-type='" + taxType + "']:first td[name='taxName']");
        var originalRowSpan = parseInt($tdTaxName.attr("rowspan"));
        $tdTaxName.attr("rowspan", originalRowSpan - 1);
        $tr.remove();
    });
    //税金字典的日期变更的时候，需要进行重复验证
    $tableTaxDictionary.on("change", ".dateinput", function () {
        var $this = $(this);
        var $tr = $this.parents("tr:first");
        var taxType = $tr.attr("tax-type");
        var taxName = $tableTaxDictionary.find("tr[tax-type='" + taxType + "']:first td[name='taxName']").text();
        var isPassed = true;
        var errorString = "";
        if ($this.is(".dateinput")) {
            var startDate = $tr.find("[name='dtpTaxDictionaryStartDate']").val();
            var endDate = $tr.find("[name='dtpTaxDictionaryEndDate']").val();
            if (endDate && startDate > endDate) {
                isPassed = false;
                errorString = "开始日期不能大于结束日期";
                infoBox(errorString);
            }
        }
        if (isPassed) {
            var taxPeriods = [];
            $tableTaxDictionary.find("tr[tax-type='" + taxType + "']").each(function () {
                var startDate = $(this).find("[name='dtpTaxDictionaryStartDate']").val();
                var endDate = $(this).find("[name='dtpTaxDictionaryEndDate']").val();
                taxPeriods.push({
                    startDate: startDate,
                    endDate: endDate
                });
            });
            var duplicatedList = [];
            checkReduplicated(taxPeriods, duplicatedList);
            if (duplicatedList.length > 0) {
                var errorString = taxName + "存在重复期间<br/>";
                for (var j in duplicatedList) {
                    var period = duplicatedList[j].period;
                    var targetPeriod = duplicatedList[j].targetPeriod;
                    errorString += "重复期间：" + period.startDate + "~" + period.endDate + "<br/>";
                    errorString += "对比期间：" + targetPeriod.startDate + "~" + targetPeriod.endDate + "<br/>";
                }
                infoBox("您填写的日期区间有重复或交叉，请重新输入。");
                isPassed = false;
            }
        }
        if (!isPassed) {
            $tr.find(".dateinput").val("");
        }

    });
    //添加税金例外期间
    $tableTaxExcept.on("click", ".price_sale_house_01", function () {
        var $tr = $(this).parents("tr:first");
        var exceptionType = $tr.attr("exception-type");

        //获取当前例外类型的第一行的里往外名称单元格 并将例外名称单元格跨的行数增加1
        var $tdExceptionName = $tableTaxExcept.find("tr[exception-type='" + exceptionType + "']:first td[name='exceptionName']");
        var originalRowSpan = parseInt($tdExceptionName.attr("rowspan"));
        if (isNaN(originalRowSpan)) {
            originalRowSpan = 1;
        }
        $tdExceptionName.attr("rowspan", originalRowSpan + 1);

        //复制一行，并初始化数据，添加到对应的税金例外后
        var $new_tr = $tr.clone();
        $new_tr.find("td[name='exceptionName']").remove();
        $new_tr.find("input").val("");
        $new_tr.find("input").attr("original_value", "");
        $new_tr.find("input[type='checkbox']").removeAttr("checked");
        $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
        $tableTaxExcept.find("tr[exception-type='" + exceptionType + "']:last").after($new_tr);
    });
    //删除税金例外期间
    $tableTaxExcept.on("click", ".price_sale_house_02", function () {
        var $tr = $(this).parents("tr:first");
        var exceptionType = $tr.attr("exception-type");
        //获取当前税金例外的第一行的税金例外名称单元格 并将税金例外名称单元格跨的行数减少1
        var $tdExceptionName = $tableTaxExcept.find("tr[exception-type='" + exceptionType + "']:first td[name='exceptionName']");
        var originalRowSpan = parseInt($tdExceptionName.attr("rowspan"));
        $tdExceptionName.attr("rowspan", originalRowSpan - 1);
        $tr.remove();
    });
    //税金例外的各项值发生变化的时候，需要验证重复
    $tableTaxExcept.on("change", "input[checkbox-action!='All']", function () {
        var $this = $(this);
        var $tr = $this.parents("tr:first");
        var $td = $this.parents("td:first");
        if ($td.find("input[checkbox-action!='All']").length == $td.find("input[checkbox-action!='All']:checked").length) {
            $td.find("input[checkbox-action='All']").attr("checked", true);
        } else {
            $td.find("input[checkbox-action='All']").removeAttr("checked");
        }
        var exceptionType = $tr.attr("exception-type");
        var exceptionName = $tableTaxExcept.find("tr[exception-type='" + exceptionType + "']:first td[name='exceptionName']").text();
        var isPassed = true;

        var errorString = "";
        if ($this.is(".dateinput")) {
            var startDate = $tr.find("[name='dtpTaxExceptStartDate']").val();
            var endDate = $tr.find("[name='dtpTaxExceptEndDate']").val();
            if (endDate && startDate > endDate) {
                isPassed = false;
                errorString = "开始日期不能大于结束日期";
                infoBox(errorString);
            }
        }
        if (isPassed) {
            var taxTypes = [];
            $tr.find("[name='taxType']:checked").each(function () {
                var $taxType = $(this);
                var taxName = $taxType.parent().text();
                var taxType = $taxType.attr("tax-type");
                taxTypes.push({
                    taxName: taxName,
                    taxType: taxType
                });
            });
            var currentTravelTypes = [];
            $tr.find("[name='travelTypeUuid'][checkbox-action!='All']:checked").each(function () {
                var $travelType = $(this);
                var travelTypeText = $travelType.parent().text();
                var travelType = $travelType.attr("traveler-type");
                currentTravelTypes.push({
                    name: travelTypeText,
                    uuid: travelType
                });
            });
            isPassed = checkDuplicateTaxExceptDate(taxTypes, currentTravelTypes, exceptionType, exceptionName);
        }
        if (!isPassed) {
            $tr.find(".dateinput").val("");
            $tr.find("[type='checkbox']").removeAttr("checked");
        }
    });
    $tableTaxExcept.on("change", "[checkbox-action='All']", function () {
        var $this = $(this);
        var $sibings = $this.parent().parent().find("input[checkbox-action!='All']");
        if ($this.is(":checked")) {
            var $tr = $this.parents("tr:first");
            var exceptionType = $tr.attr("exception-type");
            var exceptionName = $tableTaxExcept.find("tr[exception-type='" + exceptionType + "']:first td[name='exceptionName']").text();
            var taxTypes = [];
            $tr.find("[name='taxType']:checked").each(function () {
                var $taxType = $(this);
                var taxName = $taxType.parent().text();
                var taxType = $taxType.attr("tax-type");
                taxTypes.push({
                    taxName: taxName,
                    taxType: taxType
                });
            });
            var currentTravelTypes = [];
            $sibings.each(function () {
                var $travelType = $(this);
                var travelTypeText = $travelType.parent().text();
                var travelType = $travelType.attr("traveler-type");
                currentTravelTypes.push({
                	name: travelTypeText,
                    uuid: travelType
                });
            });

            $sibings.attr("checked", true);
            var isPassed = checkDuplicateTaxExceptDate(taxTypes, currentTravelTypes, exceptionType, exceptionName);
            if (!isPassed) {
                $tr.find(".dateinput").val("");
                $tr.find("[type='checkbox']").removeAttr("checked");
            }
        } else {
            $sibings.removeAttr("checked");
        }
    });
});

//选择税金日期后触发变更事件
function taxDictionaryDateChange() {
    var $this = $(this);
    $this.trigger("change");

}
//选择税金例外日期后触发变更事件
function taxExceptDateChange() {
    var $this = $(this);
    $this.trigger("change");
}
function getCheckingExceptPeriod(exceptionType, taxType, travelType) {
    var periods = [];
    $tableTaxExcept.find("tr[exception-type='" + exceptionType + "']")
        .has("[name='taxType'][tax-type='" + taxType + "']:checked")
        .has("[name='travelTypeUuid'][traveler-type='" + travelType + "']:checked")
        .each(function () {
            var $this = $(this);
            var startDate = $this.find("[name='dtpTaxExceptStartDate']").val();
            var endDate = $this.find("[name='dtpTaxExceptEndDate']").val();
            periods.push({
                startDate: startDate,
                endDate: endDate
            });
        });
    return periods;
}

/**
 * 页面模式为编辑模式的时候，绑定税金字典数据
 * @param hotelPlTaxPriceList 税金字典数据源
 */
function bindTaxDictionary(hotelPlTaxPriceList){
    for(var i in hotelPlTaxPriceList){
        var taxPrice = hotelPlTaxPriceList[i];
        var $firstTr = $tableTaxDictionary.find("tr[tax-type='" + taxPrice.taxType + "']:first");
        var $tdTaxName = $firstTr.find("td[name='taxName']");
        var originalRowSpan = parseInt($tdTaxName.attr("rowspan"));
        if (isNaN(originalRowSpan)) {
            originalRowSpan = 1;
        }
        var $new_tr;
        if(!$firstTr.attr("uuid")){
            $new_tr =$firstTr;
        }else{
            $tdTaxName.attr("rowspan", originalRowSpan + 1);
            $new_tr = $firstTr.clone();
            $new_tr.find("td[name='taxName']").remove();
            $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
            $tableTaxDictionary.find("tr[tax-type='" + taxPrice.taxType + "']:last").after($new_tr);
        }
        $new_tr.find("[name='dtpTaxDictionaryStartDate']").val(taxPrice.startDate);
        $new_tr.find("[name='dtpTaxDictionaryEndDate']").val(taxPrice.endDate);
        $new_tr.find("[name='txtTaxAddValue']").val(taxPrice.amount);
        $new_tr.attr("uuid",taxPrice.uuid);

    }
}

/**
 * 页面模式为编辑模式的时候,绑定税金例外
 * @param hotelPlTaxExceptionList 税金例外数据
 */
function bindTaxException(hotelPlTaxExceptionList){
    for(var i in hotelPlTaxExceptionList){
        var taxException = hotelPlTaxExceptionList[i];
        var $firstTr = $tableTaxExcept.find("tr[exception-type='"+taxException.exceptionType+"']:first");
        var $tdExceptionName =  $firstTr.find("td[name='exceptionName']");
        var originalRowSpan = parseInt($tdExceptionName.attr("rowspan"));
        if (isNaN(originalRowSpan)) {
            originalRowSpan = 1;
        }
        var $new_tr;
        if(!$firstTr.attr("uuid")){
            $new_tr =$firstTr;
        }else{
            $tdExceptionName.attr("rowspan", originalRowSpan + 1);
            $new_tr = $firstTr.clone();
            $new_tr.find("td[name='exceptionName']").remove();
            $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
            $tableTaxExcept.find("tr[exception-type='" + taxException.exceptionType + "']:last").after($new_tr);
        }
        $new_tr.find("[name='dtpTaxExceptStartDate']").val(taxException.startDate);
        $new_tr.find("[name='dtpTaxExceptEndDate']").val(taxException.endDate);
        $new_tr.find("[name='taxType']").checkedList(function(checkbox){
            var $checkbox = $(checkbox);
            return  $checkbox.attr("tax-type");
        },taxException.taxType.split(";"));
        $new_tr.find("[name='travelTypeUuid']").checkedList(function (checkbox) {
            var $checkbox = $(checkbox);
            return  $checkbox.attr("traveler-type");
        }, taxException.travelType.split(";")).last().trigger("change");
        $new_tr.attr("uuid",taxException.uuid);
    }
}

/**
 * 酒店价单详情页面,绑定税金价格字典
 * @param hotelPlTaxPriceList 税金字典数据源
 */
function bindDetailTaxDictionary(hotelPlTaxPriceList){
    for(var i in hotelPlTaxPriceList){
        var taxPrice = hotelPlTaxPriceList[i];
        var $firstTr = $tableTaxDictionary.find("tr[tax-type='" + taxPrice.taxType + "']:first");
        var $tdTaxName = $firstTr.find("td[name='taxName']");
        var originalRowSpan = parseInt($tdTaxName.attr("rowspan"));
        if (isNaN(originalRowSpan)) {
            originalRowSpan = 1;
        }
        var $new_tr;
        if(!$firstTr.attr("uuid")){
            $new_tr =$firstTr;
        }else{
            $tdTaxName.attr("rowspan", originalRowSpan + 1);
            $new_tr = $firstTr.clone();
            $new_tr.find("td[name='taxName']").remove();
            $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
            $tableTaxDictionary.find("tr[tax-type='" + taxPrice.taxType + "']:last").after($new_tr);
        }
        $new_tr.find("td[name=taxStartEndDate]").text(getTextByAttribute(taxPrice.startDate) + "至" + getTextByAttribute(taxPrice.endDate));
        if(taxPrice.taxType == '1' || taxPrice.taxType == '2') {
            $new_tr.find("td[name=taxAmount]").text(getTextByAttribute(taxPrice.amount) + '%');
        } else if(taxPrice.taxType == '3' || taxPrice.taxType == '4') {
        	$new_tr.find("td[name=taxAmount]").text(baseInfo.currencyMark + formatCurrency(taxPrice.amount));
        }
        $new_tr.attr("uuid",taxPrice.uuid);

    }
}

/**
 * 酒店价单详情页面,绑定税金例外
 * @param hotelPlTaxExceptionList 税金例外数据
 */
function bindDetailTaxException(hotelPlTaxExceptionList){
	
	//将初始化的游客类型多选框注释掉
	$tableTaxExcept.find("td[name=taxtExceptionTravelList]").text('');
	
    for(var i in hotelPlTaxExceptionList){
        var taxException = hotelPlTaxExceptionList[i];
        var $firstTr = $tableTaxExcept.find("tr[exception-type='"+taxException.exceptionType+"']:first");
        var $tdExceptionName =  $firstTr.find("td[name='exceptionName']");
        var originalRowSpan = parseInt($tdExceptionName.attr("rowspan"));
        if (isNaN(originalRowSpan)) {
            originalRowSpan = 1;
        }
        var $new_tr;
        if(!$firstTr.attr("uuid")){
            $new_tr =$firstTr;
        }else{
            $tdExceptionName.attr("rowspan", originalRowSpan + 1);
            $new_tr = $firstTr.clone();
            $new_tr.find("td[name='exceptionName']").remove();
            $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
            $tableTaxExcept.find("tr[exception-type='" + taxException.exceptionType + "']:last").after($new_tr);
        }
        $new_tr.find("td[name=exceptionDate]").text(getTextByAttribute(taxException.startDate) +"至"+ getTextByAttribute(taxException.endDate));
        
        var exceptionTax = '';
        var taxTypeArr = taxException.taxType.split(";");
        for(var i=0 ; i<taxTypeArr.length; i++) {
        	if(taxTypeArr[i] == '1') {
            	exceptionTax += '政府税';
        	} else if(taxTypeArr[i] == '2') {
            	exceptionTax += '服务税';
        	} else if(taxTypeArr[i] == '3') {
            	exceptionTax += '床税';
        	} else if(taxTypeArr[i] == '4') {
        		exceptionTax += '环保税';
        	}
        	if(i != taxTypeArr.length-1) {
            	exceptionTax += '、';
        	}
        }
        $new_tr.find("td[name=exceptionTax]").text(exceptionTax);
        
        var travelTypeArr =  taxException.travelType.split(";");
        var currentTravelTypes = getTravelTypesByTaxExceptionType('');
        var travelTypeTxt = '';
        for(var i=0; i<travelTypeArr.length; i++) {
        	var flag = false;
        	for(var j in currentTravelTypes){
                var travelType = currentTravelTypes[j];
                if(travelType.uuid == travelTypeArr[i]) {
                    travelTypeTxt += travelType.name;
                    flag = true;
                    break;
                }
            }
        	
        	if(flag && (i != travelTypeArr.length-1)) {
            	travelTypeTxt += '、';
        	}
        }
        $new_tr.find("td[name=taxtExceptionTravelList]").text(travelTypeTxt);
        
        $new_tr.attr("uuid",taxException.uuid);
    }
}

/**
 * 获取保存的税金字典
 * @returns {Array} 需要保存的数据
 */
function getSaveTaxDictionary(){
    var plTaxPriceList=[];
    $tableTaxDictionary.find("tbody tr").each(function(){
        var $tr = $(this);
        var $txtAmount = $tr.find("[name='txtTaxAddValue']");
        var taxPrice ={
            "chargeType": $txtAmount.attr("charge-type"),
            "chargeTypeText":$txtAmount.attr("charge-type-text"),
            "currencyId": baseInfo.currencyId,
            "amount":$txtAmount.val(),
            "endDate":  $tr.find("[name='dtpTaxDictionaryEndDate']").val(),
            "endDateString":  $tr.find("[name='dtpTaxDictionaryEndDate']").val(),
            "hotelPlUuid": baseInfo.uuid,
            "startDate": $tr.find("[name='dtpTaxDictionaryStartDate']").val(),
            "startDateString":$tr.find("[name='dtpTaxDictionaryStartDate']").val(),
            "taxName": $tr.attr("tax-name"),
            "taxType":  $tr.attr("tax-type"),
            "uuid":  $tr.attr("uuid")
        };
        plTaxPriceList.push(taxPrice);
    });
    return plTaxPriceList;
}

/**
 * 获取保存的税金例外
 * @returns {Array} 需要保存的数据
 */
function getSaveTaxException(){
    var  plTaxExceptionList=[];
    $tableTaxExcept.find("tbody tr").each(function(){
        var $tr = $(this);
        var taxException ={
            "endDate":$tr.find("[name='dtpTaxExceptEndDate']").val(),
            "endDateString": $tr.find("[name='dtpTaxExceptEndDate']").val(),
            "exceptionName": $tr.attr("exception-name"),
            "exceptionType": $tr.attr("exception-type"),
            "hotelPlUuid": baseInfo.uuid,
            "startDate": $tr.find("[name='dtpTaxExceptStartDate']").val(),
            "startDateString": $tr.find("[name='dtpTaxExceptStartDate']").val(),
            "taxType": $tr.find('[name="taxType"][checkbox-action!="All"]').checkedList(function(checkbox){
                return $(checkbox).attr("tax-type");
            }).toString(),
            "taxTypeText": $tr.find('[name="taxType"][checkbox-action!="All"]').getCheckedTexts(function(checkbox){
                return $(checkbox).parent().text().trim();
            }),
            "travelType": $tr.find('[name="travelTypeUuid"][checkbox-action!="All"]').checkedList(function(checkbox){
                return $(checkbox).attr("traveler-type");
            }).toString(),
            "travelTypeText":$tr.find('[name="travelTypeUuid"][checkbox-action!="All"]').getCheckedTexts(function(checkbox){
                return $(checkbox).parent().text().trim();
            }),
            "uuid":  $tr.attr("uuid")
        };
        plTaxExceptionList.push(taxException);
    });
    return plTaxExceptionList;
}