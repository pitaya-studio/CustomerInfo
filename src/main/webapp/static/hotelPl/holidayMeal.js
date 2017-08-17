var $tbHolidayMeal;
$(document).ready(function () {
    $("#btnAddHolidayMeal").click(fn_AddHolidayMeal);
    $tbHolidayMeal = $("#tbHolidayMeal");
    $tbHolidayMeal.on("change", ".dateinput", function () {
        var periods = [];
        $tbHolidayMeal.find("tbody tr").each(function () {
            var $tr = $(this);
            var startDate = $tr.find("[name='dtpHolidayMealStartDate']").val();
            var endDate = $tr.find("[name='dtpHolidayMealEndDate']").val();
            periods.push({
                startDate: startDate,
                endDate: endDate
            });
        });
        var isPassed = true;
        for (var i in periods) {
            var period = periods[i];
            if (period.endDate && period.startDate > period.endDate) {
                isPassed = false;
                infoBox("开始日期不能大于结束日期");
                break;
            }
        }
        if (isPassed) {
            var duplicateList = [];
            checkReduplicated(periods, duplicateList);
            if (duplicateList.length > 0) {
                isPassed = false;
                var errorString = "存在重复期间<br/>";
                for (var j in duplicateList) {
                    var period = duplicateList[j].period;
                    var targetPeriod = duplicateList[j].targetPeriod;
                    errorString += "重复期间：" + period.startDate + "~" + period.endDate + "<br/>";
                    errorString += "对比期间：" + targetPeriod.startDate + "~" + targetPeriod.endDate + "<br/>";
                }
                infoBox("您填写的日期区间有重复或交叉，请重新输入。");
            }
        }
        if (!isPassed) {
            var $tr = $(this).parents("tr:first");
            $tr.find(".dateinput").val("");
        }
    });
});
function fn_AddHolidayMeal() {
    var rowTheme = "";
    rowTheme += '<tr>';
    rowTheme += '<td class="tc"><input type="text" name="holidayMealName"   style="width:90%"/>';
    rowTheme += '</td>';
    rowTheme += '<td class="tc">';
    rowTheme += '<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxHolidayDateChange})" class="dateinput w106 required " name="dtpHolidayMealStartDate" readonly="readonly">';
    rowTheme += ' 至 ';
    rowTheme += '<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxHolidayDateChange})" class="dateinput w106 required " name="dtpHolidayMealEndDate" readonly="readonly">';
    rowTheme += '</td>';
    for (var i in travelerTypes) {
        var travelerType = travelerTypes[i];
        rowTheme += '<td class="tc"><input data-type="amount" maxlength="9" type="text" name="travelerType" traveler-type="' + travelerType.uuid + '"  ' +
        'uuid=""' +
        'class="tr amt" amt-code="' + baseInfo.currencyMark + '" ' +
        'style="width: 85%"/></td>';
    }

    rowTheme += '<td class="tc">' + delButtonTheme + '</td>';
    rowTheme += '</tr>';
    $tbHolidayMeal.find("tbody").append(rowTheme);
    $tbHolidayMeal.parent("[datarow-emptyRow-behavior]").show();
    resetCurrency();

    var $btnAddHolidayMeal = $("#btnAddHolidayMeal");
    $btnAddHolidayMeal.removeClass("edit_button");
    var $btnEditHolidayMeal = $("#btnEditHolidayMeal");
    $btnEditHolidayMeal.addClass("edit_button");
    $btnEditHolidayMeal.click();

}
function taxHolidayDateChange() {
    $(this).trigger("change");
}

/**
 * 页面模式为编辑模式的时候,绑定强制性节日餐
 * @param hotelPlHolidayMealList 待绑定的数据
 */
function bindHolidayMeal(hotelPlHolidayMealList) {
    var $tbody = $tbHolidayMeal.find("tbody");
    var groupedMealList = groupArrayByProp(hotelPlHolidayMealList,"startDate")
    for (var i in groupedMealList) {
        var holidayMeals = groupedMealList[i];
        var $row = $("<tr></tr>");
        $row.append('<td  class="tc"><input type="text" name="holidayMealName" value="'+holidayMeals[0].holidayMealName+'" style="width:90%"></td>');
        $row.append('<td class="tc">' +
        '<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxHolidayDateChange})" ' +
        'value="' + holidayMeals[0].startDate + '" class="dateinput w106 required " name="dtpHolidayMealStartDate">' +
        ' 至 ' +
        '<input type="text" onclick="WdatePicker({readOnly:true,onpicked:taxHolidayDateChange})" ' +
        'value="' + holidayMeals[0].endDate + '" class="dateinput w106 required " name="dtpHolidayMealEndDate" >' +
        '</td>');
        for (var j in travelerTypes) {
            var travelerTypeUuid = travelerTypes[j].uuid;
            var travelerMeal = getObjectByProp(holidayMeals,"travelerTypeUuid",travelerTypeUuid);
            var uuid = travelerMeal?travelerMeal.uuid:"";
            var amount = travelerMeal?travelerMeal.amount:"";
            if(amount == undefined) {
            	amount = "";
            }
            $row.append('<td class="tc">' +
            '<input data-type="amount" maxlength="9" type="text" name="travelerType" traveler-type="' + travelerTypeUuid + '" ' +
            'uuid="'+uuid+'"' +
            'value="' + amount + '" ' +
            'class="tr amt" amt-code="' + baseInfo.currencyMark + '" ' +
            'style="width: 85%; padding-left: 15px;" />' +
            '</td>');
        }
        $row.append('<td class="tc"><a class="deleteButton" href="javascript:void(0)">删除</a></td>');
        $tbody.append($row);
    }
    $tbHolidayMeal.parent("[datarow-emptyRow-behavior]").show();
    var $btnAddHolidayMeal = $("#btnAddHolidayMeal");
    $btnAddHolidayMeal.removeClass("edit_button");
    var $btnEditHolidayMeal = $("#btnEditHolidayMeal");
    $btnEditHolidayMeal.addClass("edit_button");
    var $btnSaveHolidayMeal = $("#btnSaveHolidayMeal");
    var $region = $btnSaveHolidayMeal.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditHolidayMeal.removeClass("region_disabled");
    $btnSaveHolidayMeal.addClass("region_disabled");
}

/**
 * 酒店价单详情页面->绑定强制性节日餐
 * @param hotelPlHolidayMealList 待绑定的数据
 */
function bindDetailHolidayMeal(hotelPlHolidayMealList) {
	//去除操作文本显示
	var controlCol = $tbHolidayMeal.find("thead th[name=theadOperation]");
	controlCol.remove();
	
    var $tbody = $tbHolidayMeal.find("tbody");
    var groupedMealList = groupArrayByProp(hotelPlHolidayMealList,"startDate");
    for (var i in groupedMealList) {
        var holidayMeals = groupedMealList[i];
        var $row = $("<tr></tr>");
        $row.append('<td  class="tc">'+holidayMeals[0].holidayMealName+'</td>');
        $row.append('<td class="tc">' + holidayMeals[0].startDate +'~'+ holidayMeals[0].endDate + '</td>');
        for (var j in travelerTypes) {
            var travelerTypeUuid = travelerTypes[j].uuid;
            var travelerMeal = getObjectByProp(holidayMeals,"travelerTypeUuid",travelerTypeUuid);
            var uuid = travelerMeal?travelerMeal.uuid:"";
            var amount = travelerMeal?travelerMeal.amount:"";
            if(amount == undefined) {
            	amount = "";
            }
            $row.append('<td class="tc">' + baseInfo.currencyMark + formatCurrency(amount) + '</td>');
        }
        $tbody.append($row);
    }
    $tbHolidayMeal.parent("[datarow-emptyRow-behavior]").show();
    var $btnAddHolidayMeal = $("#btnAddHolidayMeal");
    $btnAddHolidayMeal.removeClass("edit_button");
    var $btnEditHolidayMeal = $("#btnEditHolidayMeal");
    $btnEditHolidayMeal.addClass("edit_button");
    var $btnSaveHolidayMeal = $("#btnSaveHolidayMeal");
    var $region = $btnSaveHolidayMeal.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditHolidayMeal.removeClass("region_disabled");
    $btnSaveHolidayMeal.addClass("region_disabled");
}

/**
 * 保存酒店价单节日餐
 */
$(document).on("click", "#btnSaveHolidayMeal", function (e) {
	var validateFlag = true;
	$tbHolidayMeal.find("tbody tr").each(function(){
        var $tr = $(this);
    	//保存今日餐添加校验
    	var startDate =  $tr.find('[name="dtpHolidayMealStartDate"]').val();
        var endDate =  $tr.find('[name="dtpHolidayMealEndDate"]').val();
        var holidayMealName = $tr.find('[name="holidayMealName"]').val();
        if(startDate == "") {
        	$tr.find('[name="dtpHolidayMealStartDate"]').focus();
        	validateFlag = false;
        	$.jBox.tip("强制性节日餐开始日期不能为空！");
        	return false;
        }
        
        if(endDate == "") {
        	$tr.find('[name="dtpHolidayMealEndDate"]').focus();
        	validateFlag = false;
        	$.jBox.tip("强制性节日餐结束日期不能为空！");
        	return false;
        }
        
        if(holidayMealName == "" || holidayMealName.replace(/^\s+|\s+$/g, "") == "") {
        	$tr.find('[name="holidayMealName"]').focus();
        	validateFlag = false;
        	$.jBox.tip("强制性节日餐名称不能为空！");
        	return false;
        }
	});
	
	if(!validateFlag) {
		return false;
	}
	
	//添加保存时的效果---------
	 var $this = $(this);
     var $region = $this.parents(".price_region:first");
     setRegionDisabled($region);
     $(".edit_button").removeClass("region_disabled");
     $this.addClass("region_disabled");
 	//添加保存时的效果---------
	
	var status = 1;
	//当updateFlag为1或相应模块的保存状态为1时
	if($("#updateFlag").val() == "2" || $("#hotelHolidayMealSaveFlag").val() == "2") {
		status = 2;
	} else {
		$("#hotelHolidayMealSaveFlag").val("2");
	}
	
	var url = ctx + "/hotelPl/saveHotelPlHolidayMeal";
	var hotelPlHolidayMealJsonData = JSON.stringify(getSaveHolidayMeal());
	var galamealMemo = $("textarea[name=galamealMemo]").val();
	ajaxStart();
	$.ajax({
		type: "POST",
	   	url: url,
	   	async: false,
	   	data: {
				"hotelPlHolidayMealJsonData":hotelPlHolidayMealJsonData,
				"galamealMemo":galamealMemo,
				"status":status,
				"hotelPlUuid":baseInfo.uuid
			  },
		dataType: "json",
	   	success: function(data){
	   		ajaxStop();
	   		if(data){
	   			if(data.result == 1) {
	   				$.jBox.tip("强制性节日餐保存成功！");
	   			} else if(data.result == 2) {
	   				$.jBox.tip("强制性节日餐更新成功！");
	   			} else if(data.result == 3) {
	   				$.jBox.tip(data.message);
	   			}
	   		}
	   	}
	});

    saveButtonCli($this);
});

/**
 * 获取保存的强制节日餐
 * @returns {Array} 需要保存的数组
 */
function  getSaveHolidayMeal(){
    var hotelPlHolidayMealList=[];
    $tbHolidayMeal.find("tbody tr").each(function(){
        var $tr = $(this);
        for(var i in travelerTypes){
            var holidayMealName = $tr.find('[name="holidayMealName"]').val();
            var startDate =  $tr.find('[name="dtpHolidayMealStartDate"]').val();
            var endDate =  $tr.find('[name="dtpHolidayMealEndDate"]').val();
            var $amount =  $tr.find('[name="travelerType"][traveler-type="'+travelerTypes[i].uuid+'"]');
            var amount = $amount.val();
            var travelerTypeUuid = $amount.attr('traveler-type');
            var uuid = $amount.attr('uuid');
            hotelPlHolidayMealList.push({
                "amount": amount,
                "currencyId": baseInfo.currencyId,
                "currencyMark": baseInfo.currencyMark,
                "endDate": endDate,
                "endDateString": endDate,
                "holidayMealName":holidayMealName,
                "hotelPlUuid": baseInfo.uuid,
                "startDate": startDate,
                "startDateString": startDate,
                "travelerTypeUuid":travelerTypeUuid,
                "uuid":uuid
            });
        }
    });
    return hotelPlHolidayMealList;
}