var $dvContainer_BaseMeal;
var $btnAddRiseMeal;
var $tabHeader_BaseMeal;
var $btnToggleRiseMeal;
var $btnAddRiseMeal;

//tab展开的时候，设置各个tab的title
function setRiseScrollListTitle() {
    $dvContainer_BaseMeal.find(".title_tabScroll_list").remove();
    $dvContainer_BaseMeal.find("[name='tabContent_BaseMeal']").has("[name='tbRiseMeal']").each(function (index) {
        var $this = $(this);
        var baseMealUuid = $this.attr("base-meal-uuid");
        var baseMealText = $tabHeader_BaseMeal.find("[base-meal-uuid='" + baseMealUuid + "']").text();
        var titleTheme = "<div class='title_tabScroll_list'>" + (index + 1) + "." + baseMealText + "</div>"
        $this.prepend(titleTheme);
    });
}
$(document).ready(function () {
    $dvContainer_BaseMeal = $("#dvContainer_BaseMeal");
    $btnAddRiseMeal = $("#btnAddRiseMeal");
    $tabHeader_BaseMeal = $("#tabHeader_BaseMeal");
    $btnToggleRiseMeal = $("#btnToggleRiseMeal");
    $btnAddRiseMeal = $("#btnAddRiseMeal");
    $btnAddRiseMeal.click(fn_PopRiseMeal);
    $tabHeader_BaseMeal.parent().on("tabScroll.displayChange", function (e, displayType) {
        $dvContainer_BaseMeal.data("displayType", displayType);
        if (displayType == "list") {
            $btnAddRiseMeal.addClass("tabScroll_list");
            setRiseScrollListTitle();
        } else {
            $btnAddRiseMeal.removeClass("tabScroll_list");
            $dvContainer_BaseMeal.find(".title_tabScroll_list").remove();
        }
    });
    $tabHeader_BaseMeal.parent().on("tabScroll.change", function () {
        if ($dvContainer_BaseMeal.find("[name='tabContent_BaseMeal']:visible [name='tbRiseMeal']").length == 0) {
            $btnAddRiseMeal.text("新建升级餐型");
        } else {
            $btnAddRiseMeal.text("编辑升级餐型");
        }
    });
    $dvContainer_BaseMeal.on("datarow.delete", "[datarow-emptyRow-behavior]", function (e, tr) {
        var $tabContent_BaseMeal = $(this);
        var $tr = $(tr);
        var riseMealUuid = $tr.attr("rise-meal-uuid");
        var $firstRow;
        if ($tr.has("[name='riseMealName']").length > 0) {
            var riseMealName = $tr.find("[name='riseMealName']").text();
            $firstRow = $tabContent_BaseMeal.find("[rise-meal-uuid='" + riseMealUuid + "']").first();
            $firstRow.prepend('<td class="tc" name="riseMealName">' + riseMealName + '</td>');
        }
        else {
            $firstRow = $tabContent_BaseMeal.find("[rise-meal-uuid='" + riseMealUuid + "']").first();
        }
        var riseMealDateLength = $tabContent_BaseMeal.find("[rise-meal-uuid='" + riseMealUuid + "']").length;
        $firstRow.find("[name='riseMealName']").attr("rowspan", riseMealDateLength);
    });
    $dvContainer_BaseMeal.on("emptied", "[datarow-emptyRow-behavior]", function () {
        if ($dvContainer_BaseMeal.data("displayType") == "list") {
            setRiseScrollListTitle();
        }
        if ($dvContainer_BaseMeal.find("[name='tabContent_BaseMeal'] [name='tbRiseMeal']").length == 0) {
            $dvContainer_BaseMeal.trigger("modeChange");
        }
    });
    //
    $dvContainer_BaseMeal.on("modeChange", function () {
        var $btnEditRiseMeal = $("#btnEditRiseMeal");
        if ($dvContainer_BaseMeal.find("[name='tabContent_BaseMeal'] [name='tbRiseMeal']").length == 0) {
            $dvContainer_BaseMeal.data("mode", "create");
            //$btnToggleRiseMeal.hide();
            $btnAddRiseMeal.text("新建升级餐型");
            $btnAddRiseMeal.addClass("edit_button");
            $btnEditRiseMeal.removeClass("edit_button");
            $btnEditRiseMeal.addClass("region_disabled");
        }
        else {
            $dvContainer_BaseMeal.data("mode", "modify");
            //$btnToggleRiseMeal.show();
            $btnAddRiseMeal.removeClass("edit_button");
            $btnAddRiseMeal.removeClass("region_disabled");
            $btnEditRiseMeal.addClass("edit_button");
            $btnEditRiseMeal.addClass("region_disabled");
        }
    });
});
function getHotelRiseMealList(baseMealUuid) {
    var riseBaseMealList = [];
    for (var i in hotelMeals) {
        if (hotelMeals[i].uuid != baseMealUuid) {
            riseBaseMealList.push({
                riseMealUuid: hotelMeals[i].uuid,
                riseMealName: hotelMeals[i].mealName
            });
        }
    }
    return riseBaseMealList;
}
function getAddedRiseMeals(pop) {
    var riseMeals = [];
    pop.find("input[type='checkbox']:checked").each(function () {
        var $this = $(this);
        riseMeals.push({
            riseMealUuid: $this.attr("rise-meal-uuid"),
            riseMealName: $this.parent().text()
        });
    });
    return riseMeals;
}
function getAddendRiseMealDates(pop, riseMeals) {
    var riseMealDates = [];
    for (var i in riseMeals) {
        pop.find(".riseDate").each(function () {
            var $this = $(this);
            var startDate = $this.find("[name='dtpRiseMealStartDate']").val();
            var endDate = $this.find("[name='dtpRiseMealEndDate']").val();
            riseMealDates.push({
                riseMealUuid: riseMeals[i].riseMealUuid,
                riseMealName: riseMeals[i].riseMealName,
                startDate: startDate,
                endDate: endDate
            });
        });
    }
    return riseMealDates;
}
function setPopRiseMeal(baseMealUuid, pop) {
    var $pop_riseMeal = pop.find("#pop_riseMeal");
    $pop_riseMeal.empty();
    var hotelRiseMealList = getHotelRiseMealList(baseMealUuid);
    for (var i in hotelRiseMealList) {
        var riseMeal = hotelRiseMealList[i];
        var liTheme = '<label class="input_label_lg"> <input type="checkbox" style="margin: 0px" name="riseMeal"  rise-meal-uuid="' + riseMeal.riseMealUuid + '" />' + riseMeal.riseMealName + '</label>';
        $pop_riseMeal.append(liTheme);
    }
    pop.find(".riseDate:gt(0)").remove();
    pop.find(".riseDate:first input").val("");
}
function setPopRiseMealDates(baseMealUuid, pop) {
    pop.find(".riseDate:gt(0)").remove();
    pop.find(".riseDate:first input").val("");
    var riseMeals = getAddedRiseMeals(pop);
    var riseMealDates = getRiseMealDatesByCheckedRiseMeal(baseMealUuid, riseMeals);
    var groupedRiseMealDates = [];
    groupRiseMealDatesByRiseMeal(riseMealDates, groupedRiseMealDates);
    var isSame = isSameRiseMealDate(riseMeals, groupedRiseMealDates);
    if (!isSame) {
        pop.find(".riseDate:gt(0)").remove();
        pop.find(".riseDate:first input").val("");
        pop.data("mode", "create");
    } else {
        var firstRiseMealDates = groupedRiseMealDates[0];
        for (var i in firstRiseMealDates) {
            var startDate = firstRiseMealDates[i].startDate;
            var endDate = firstRiseMealDates[i].endDate;
            if (i == 0) {
                pop.find(".riseDate:first [name='dtpRiseMealStartDate']").val(startDate);
                pop.find(".riseDate:first [name='dtpRiseMealEndDate']").val(endDate);
            }
            else {
                var rowTheme = '';
                rowTheme += '<tr class="riseDate">';
                rowTheme += '<td class="tr"><span class="xing">*</span>日期</td>';
                rowTheme += '<td>';
                rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRiseMealStartDate" readonly="readonly">';
                rowTheme += ' 至 ';
                rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRiseMealEndDate" readonly="readonly">';
                rowTheme += '<i class="price_sale_house_02"></i>';
                rowTheme += '</td>';
                rowTheme += '</tr>';
                var $row = $(rowTheme);
                $row.find("[name='dtpRiseMealStartDate']").val(startDate);
                $row.find("[name='dtpRiseMealEndDate']").val(endDate);
                pop.find(".riseDate:last").after($row);
            }

        }
        pop.data("mode", "modify");
    }
    pop.trigger("modeChange");
}
function fn_PopRiseMeal() {
    var html = $("#popBaseMeal").html();
    var pop = $.jBox(html, {
        title: "编辑基础餐型：",
        buttons: { '保存': 1 },
        submit: function (v, h, f) {
            if (v == "1") {
                var baseMealUuid = pop.find("input[type='radio']:checked").attr("base-meal-uuid");
                var riseMeals = getAddedRiseMeals(pop);
                var riseMealDates = getAddendRiseMealDates(pop, riseMeals);
                if (checkRiseMealDates(baseMealUuid, riseMeals, riseMealDates, pop)) {
                    addRiseMealDates(baseMealUuid, riseMeals, riseMealDates, pop);
                    return true;
                } else {
                    return false;
                }
            }
        }, width: 480, height: 350
    });
    pop.on("change", "input[type='radio']", function () {
        var baseMealUuid = $(this).attr("base-meal-uuid");
        setPopRiseMeal(baseMealUuid, pop);
    });
    var $pop_baseMeal = pop.find("#pop_BaseMeal");
    for (var i in hotelMeals) {
        var baseMeal = hotelMeals[i];
        var liTheme = '<label class="input_label_lg"> <input type="radio" style="margin: 0px" name="baseMeal"  base-meal-uuid="' + baseMeal.uuid + '" />' + baseMeal.mealName + '</label>';
        $pop_baseMeal.append(liTheme);
    }
    $dvContainer_BaseMeal.children(":visible").each(function () {
        var content_BaseMeal = $(this);
        var baseMealUuid = $tabHeader_BaseMeal.find(".active").attr("base-meal-uuid");
        var $chkBaseMeal = pop.find("input[type='radio'][base-meal-uuid='" + baseMealUuid + "']");
        $chkBaseMeal.attr("checked", true);
        $chkBaseMeal.trigger("change");
    });
    pop.on("click", ".price_sale_house_01", function () {
        var $tr = $(this).parents("tr:first");
        var $new_tr = $tr.clone();
        $new_tr.find("input").val("");
        $new_tr.find(".price_sale_house_01").removeClass("price_sale_house_01").addClass("price_sale_house_02");
        $tr.parent().append($new_tr);
    });
    pop.on("click", ".price_sale_house_02", function () {
        $(this).parents("tr:first").remove();
    });
    pop.on("change", "[name='riseMeal']", function () {
        var baseMealUuid = pop.find("[name='baseMeal']:checked").attr("base-meal-uuid");
        setPopRiseMealDates(baseMealUuid, pop);
    });
    pop.on("modeChange", function () {
        var popMode = pop.data("mode");
        if (popMode == "create") {
            if (pop.find(".createMsg").length == 0) {
                var createMsgTheme = '<tr class="createMsg"><td></td><td  style="color:#FF0000">您选择的升级餐型日期不一致，只能进行新增操作。</td></tr>';
                pop.find(".riseDate:first").before(createMsgTheme);
            }
        } else {
            pop.find(".createMsg").remove();
        }
    });

};
function getRiseMealDatesByCheckedRiseMeal(baseMealUuid, riseMeals) {
    var $content_BaseMeal = $dvContainer_BaseMeal.find("[base-meal-uuid=" + baseMealUuid + "]");
    var existRiseMeals = [];
    for (var i in riseMeals) {
        $content_BaseMeal.find("tr[rise-meal-uuid='" + riseMeals[i].riseMealUuid + "']").each(function () {
            var $this = $(this);
            var periodString = $this.find("[name='riseMealPeriod']").text();
            var riseMealUuid = $this.attr("rise-meal-uuid");
            var riseMealName = $content_BaseMeal.find("tr[rise-meal-uuid='" + riseMealUuid + "']:first [name='riseMealName']").text();
            existRiseMeals.push({
                riseMealUuid: riseMealUuid,
                riseMealName: riseMealName,
                startDate: periodString.split("~")[0],
                endDate: periodString.split("~")[1]
            });
        });
    }
    return existRiseMeals;
}
function getExistRiseMealDates(baseMealUuid) {
    var $content_BaseMeal = $dvContainer_BaseMeal.find("[base-meal-uuid=" + baseMealUuid + "]");
    var existRiseMeals = [];
    $content_BaseMeal.find("tr[rise-meal-uuid]").each(function () {
        var $this = $(this);
        var periodString = $this.find("[name='riseMealPeriod']").text();
        var riseMealUuid = $this.attr("rise-meal-uuid");
        var riseMealName = $content_BaseMeal.find("tr[rise-meal-uuid='" + riseMealUuid + "']:first [name='riseMealName']").text();
        existRiseMeals.push({
            riseMealUuid: riseMealUuid,
            riseMealName: riseMealName,
            startDate: periodString.split("~")[0],
            endDate: periodString.split("~")[1]
        });
    });
    return existRiseMeals;
}

function compareRiseMealDate(existMealDates, addedMealDates) {
    var retainMealDateList = [];
    var removeMealDateList = [];
    var appendMealDateList = [];
    for (var i in existMealDates) {
        var existMealCode = existMealDates[i].riseMealUuid;
        var existStartDate = existMealDates[i].startDate;
        var existEndData = existMealDates[i].endDate;
        for (var j in addedMealDates) {
            var addedMealType = addedMealDates[j].riseMealUuid;
            var addedStartDate = addedMealDates[j].startDate;
            var addedEndData = addedMealDates[j].endDate;
            if (existMealCode == addedMealType && existStartDate == addedStartDate && existEndData == addedEndData) {
                retainMealDateList.push({
                    riseMealUuid: existMealCode,
                    riseMealName: existMealDates[i].riseMealName,
                    startDate: existMealDates[i].startDate,
                    endDate: existMealDates[i].endDate
                });
                break;
            }
        }
    }
    for (var i in existMealDates) {
        var existMealType = existMealDates[i].riseMealUuid;
        var existStartDate = existMealDates[i].startDate;
        var existEndData = existMealDates[i].endDate;
        var removeChk = true;
        for (var j in retainMealDateList) {
            var retainMealType = retainMealDateList[j].riseMealUuid;
            var retainStartDate = retainMealDateList[j].startDate;
            var retainEndData = retainMealDateList[j].endDate;
            if (existMealType == retainMealType && existStartDate == retainStartDate && existEndData == retainEndData) {
                removeChk = false;
                break;
            }
        }
        if (removeChk) {
            removeMealDateList.push({
                riseMealUuid: existMealType,
                riseMealName: existMealDates[i].riseMealName,
                startDate: existStartDate,
                endDate: existEndData
            });
        }
    }
    for (var i in addedMealDates) {
        var addedMealType = addedMealDates[i].riseMealUuid;
        var addedStartDate = addedMealDates[i].startDate;
        var addedEndData = addedMealDates[i].endDate;
        var appendChk = true;
        for (var j in retainMealDateList) {
            var retainMealType = retainMealDateList[j].riseMealUuid;
            var retainStartDate = retainMealDateList[j].startDate;
            var retainEndData = retainMealDateList[j].endDate;
            if (addedMealType == retainMealType && addedStartDate == retainStartDate && addedEndData == retainEndData) {
                appendChk = false;
                break;
            }
        }
        if (appendChk) {
            appendMealDateList.push({
                riseMealUuid: addedMealType,
                riseMealName: addedMealDates[i].riseMealName,
                startDate: addedStartDate,
                endDate: addedEndData
            });
        }
    }
    return {
        removeMealDateList: removeMealDateList,
        appendMealDateList: appendMealDateList
    };
}
function groupRiseMealDatesByRiseMeal(riseMealDates, groupedRiseMealDates) {
    if (riseMealDates.length == 0) {
        return;
    }
    var firstRiseMealDate = riseMealDates[0];
    var currentRiseMealDates = [];
    currentRiseMealDates.push(firstRiseMealDate);
    var retainRiseMealDates = [];
    for (var i = 1; i < riseMealDates.length; i++) {
        if (firstRiseMealDate.riseMealUuid == riseMealDates[i].riseMealUuid) {
            currentRiseMealDates.push(riseMealDates[i]);
        } else {
            retainRiseMealDates.push(riseMealDates[i]);
        }
    }
    groupedRiseMealDates.push(currentRiseMealDates);
    groupRiseMealDatesByRiseMeal(retainRiseMealDates, groupedRiseMealDates);
}
function isSameLength(groupedRiseMealDates) {
    var length = groupedRiseMealDates[0].length;
    for (var i = 1; i < groupedRiseMealDates.length; i++) {
        if (groupedRiseMealDates[i].length != length) {
            return false;
        }
    }
    return true;
}
function isSameRiseMealDate(riseMeals, groupedRiseMealDates) {
    if (groupedRiseMealDates.length == 0) {
        return true;
    }
    if (riseMeals.length != groupedRiseMealDates.length) {
        return false;
    }
    if (!isSameLength(groupedRiseMealDates)) {
        return false;
    }
    var isSame = true;
    var firstRiseMealDates = groupedRiseMealDates[0];
    var dateLength = firstRiseMealDates.length;
    var riseMealLength = groupedRiseMealDates.length;
    for (var i = 1; i < riseMealLength; i++) {
        for (var j = 0; j < dateLength; j++) {
            var firstRiseMealDate = firstRiseMealDates[j];
            var riseMealDate = groupedRiseMealDates[i][j];
            if (!(firstRiseMealDate.startDate == riseMealDate.startDate && firstRiseMealDate.endDate == riseMealDate.endDate)) {
                return false;
            }
        }
    }
    return isSame;
}
function checkRiseMealDates(baseMealUuid, riseMeals, riseMealDates, pop) {
    if (riseMeals.length == 0) {
        infoBox("没有选择升级餐型");
        return false;
    }
    for (var i in riseMealDates) {
        var riseMealDate = riseMealDates[i];
        if (!riseMealDate.startDate || !riseMealDate.endDate) {
            infoBox("日期不能为空");
            return false;
        }
        if (riseMealDate.endDate && riseMealDate.startDate > riseMealDate.endDate) {
            infoBox("开始日期不能大于结束日期");
            return false;
        }
    }
    var popMode = pop.data("mode");
    var allRiseMealDates = [];
    if (popMode == "modify") {
        allRiseMealDates = allRiseMealDates.concat(riseMealDates);
    } else {
        allRiseMealDates = allRiseMealDates.concat(riseMealDates).concat(getExistRiseMealDates(baseMealUuid));
    }
    var groupedRiseMealDates = [];
    groupRiseMealDatesByRiseMeal(allRiseMealDates, groupedRiseMealDates);
    var groupReduplicatedPeriod = [];
    for (var i in groupedRiseMealDates) {
        var riseMealDates = groupedRiseMealDates[i];
        var reduplicatedPeriods = [];
        checkReduplicated(riseMealDates, reduplicatedPeriods);
        if (reduplicatedPeriods.length > 0) {
            groupReduplicatedPeriod.push({
                riseMealUuid: riseMealDates[0].riseMealUuid,
                riseMealName: riseMealDates[0].riseMealName,
                reduplicatedPeriods: reduplicatedPeriods
            });
        }
    }
    if (groupReduplicatedPeriod.length > 0) {
        var errorString = "存在重复期间<br/>";
        var baseMealText = $tabHeader_BaseMeal.find("[base-meal-uuid='" + baseMealUuid + "']").text();;
        errorString += "基础餐型：" + baseMealText + "<br/>";
        for (var i in groupReduplicatedPeriod) {
            errorString += "升餐类型：" + groupReduplicatedPeriod[i].riseMealName + "<br/>";
            var reduplicatedPeriods = groupReduplicatedPeriod[i].reduplicatedPeriods;
            for (var j in reduplicatedPeriods) {
                var period = reduplicatedPeriods[j].period;
                var targetPeriod = reduplicatedPeriods[j].targetPeriod;
                errorString += "重复期间：" + period.startDate + "~" + period.endDate + "<br/>";
                errorString += "对比期间：" + targetPeriod.startDate + "~" + targetPeriod.endDate + "<br/>";
            }
        }
        errorString = "您填写的日期区间有重复或交叉，请修改。";
        infoBox(errorString);
        return false;
    }
    else {
        return true;
    }
}
function addRiseMealDates(baseMealUuid, riseMeals, riseMealDates, pop) {
    var tabHeader_BaseMeal = $("#tabHeader_BaseMeal");
    var $content_BaseMeal = $dvContainer_BaseMeal.find("[base-meal-uuid=" + baseMealUuid + "]");
    var $riseMealList = $content_BaseMeal.find("[name='riseMealList']");
    if ($riseMealList.length == 0) {
        var contentTheme = '';
        contentTheme += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" name="tbRiseMeal">';
        contentTheme += '<thead>';
        contentTheme += '<tr>';
        contentTheme += '<th width="130px">升级餐型</th>';
        contentTheme += '<th width="170px">日期</th>';
        for(var t in  travelerTypes){
            contentTheme += '<th>'+travelerTypes[t].name+'</th>';
        }
        contentTheme += '<th width="100px">操作</th>';
        contentTheme += '</tr>';
        contentTheme += '</thead>';
        contentTheme += '<tbody name="riseMealList">';
        contentTheme += '</tbody>';
        contentTheme += '</table>';
        contentTheme += '<table style="width: 100%;margin-top: 10px">';
        contentTheme += '<tbody><tr>';
        contentTheme += '<td class="tr" style="width:80px;vertical-align: top;">备注：</td>';
        contentTheme += '<td>';
        contentTheme += '<textarea name="riseMealMemo" style="width:99%;height: 50px;"></textarea>';
        contentTheme += '</td>';
        contentTheme += '</tr>';
        contentTheme += '</tbody>';
        contentTheme += '</table>';
        $content_BaseMeal.append(contentTheme);
    }
    var appendList = [];
    if (pop.data("mode") == "modify") {
        var compare = compareRiseMealDate(getRiseMealDatesByCheckedRiseMeal(baseMealUuid, riseMeals), riseMealDates);
        appendList = compare.appendMealDateList;
        $content_BaseMeal.find("[rise-meal-uuid]").each(function () {
            var $this = $(this);
            var existRiseMealUuid = $this.attr("rise-meal-uuid");
            var periodString = $this.find("[name='riseMealPeriod']").text();
            var startDate = periodString.split("~")[0];
            var endDate = periodString.split("~")[1];

            for (var i in compare.removeMealDateList) {
                var riseMealDate = compare.removeMealDateList[i];
                if (existRiseMealUuid == riseMealDate.riseMealUuid
                    && startDate == riseMealDate.startDate
                    && endDate == riseMealDate.endDate
                ) {
                    var $tabContent_BaseMeal = $this.parents("[datarow-emptyrow-behavior]:first");
                    $this.remove();
                    $tabContent_BaseMeal.trigger("datarow.delete", $this);
                }
            }
        });
    } else {
        appendList = riseMealDates;
    }
    for (var i in appendList) {
        var riseMealUuid = appendList[i].riseMealUuid;
        var riseMealName = appendList[i].riseMealName;
        var startDate = appendList[i].startDate;
        var endDate = appendList[i].endDate;
        var rowTheme = '';
        rowTheme += '<tr rise-meal-uuid="' + riseMealUuid + '" rise-meal-mame="'+riseMealName+'">';
        rowTheme += '<td class="tc" name="riseMealPeriod">' + startDate + "~" + endDate + '</td>';
        for(var t in  travelerTypes){
            rowTheme += '<td class="tc"><input data-type="amount" maxlength="9" traveler-type-uuid="'+travelerTypes[t].uuid+'" type="text" class="tr amt" amt-code="' +  baseInfo.currencyMark + '" style="width: 90%"></td>';
        }
        rowTheme += '<td class="tc">' + delButtonTheme + '</td>';
        rowTheme += '</tr>';
        var $row = $(rowTheme);
        var $tbody = $content_BaseMeal.find("tbody[name='riseMealList']");
        var $sameRiseTr = $content_BaseMeal.find("[rise-meal-uuid='" + riseMealUuid + "']");
        $sameRiseTr.find('td[name="riseMealName"]').remove();
        $sameRiseTr.sortedInsert($row,$tbody, function (el) {
            return $(el).find('[name="riseMealPeriod"]').text();
        });
        $sameRiseTr=$content_BaseMeal.find("[rise-meal-uuid='" + riseMealUuid + "']");
        $sameRiseTr.first().prepend('<td class="tc" name="riseMealName" rowspan="'+$sameRiseTr.length+'">' + riseMealName + '</td>');


    }
    tabScroll(tabHeader_BaseMeal.parent(), "[base-meal-uuid='" + baseMealUuid + "']");
    $dvContainer_BaseMeal.trigger("modeChange");
    resetCurrency();
}

/**
 * 页面模式为修改时,绑定数据
 * @param hotelRiseMealMap
 * @param hotelPlRiseMealMemoList
 */
function bindRiseMeal(hotelRiseMealMap,hotelPlRiseMealMemoList){
    for(var baseMealUuid in hotelRiseMealMap){
        var baseMealPrices = hotelRiseMealMap[baseMealUuid];
        var $tabContent_BaseMeal = $dvContainer_BaseMeal.find('[name="tabContent_BaseMeal"][base-meal-uuid=' + baseMealUuid + ']');
        var contentTheme = '';
        contentTheme += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" name="tbRiseMeal">';
        contentTheme += '<thead>';
        contentTheme += '<tr>';
        contentTheme += '<th width="130px">升级餐型</th>';
        contentTheme += '<th width="170px">日期</th>';
        for(var t in  travelerTypes){
            contentTheme += '<th>'+travelerTypes[t].name+'</th>';
        }
        contentTheme += '<th width="100px">操作</th>';
        contentTheme += '</tr>';
        contentTheme += '</thead>';
        contentTheme += '<tbody name="riseMealList">';
        contentTheme += '</tbody>';
        contentTheme += '</table>';
        var $contentTheme =$(contentTheme);
        var riseMealPrices =groupArrayByProp(baseMealPrices,"hotelMealRiseUuid");
        for (var r in riseMealPrices) {
            var $tbody = $contentTheme.find('tbody[name="riseMealList"]');
            var riseMealPrice = riseMealPrices[r];
            var hotelMealRiseUuid = riseMealPrice[0].hotelMealRiseUuid;
            var hotelMealRiseText = riseMealPrice[0].hotelMealRiseText;
            var dayMealPrices = groupArrayByProp(riseMealPrice,"startDate");
            var dayLength = dayMealPrices.length;
            for(var d =0;d<dayLength;d++){
                var dayMealPrice=dayMealPrices[d];
                var startDate = dayMealPrice[0].startDate;
                var endDate = dayMealPrice[0].endDate;

                var $tr=$('<tr rise-meal-uuid="'+hotelMealRiseUuid+'" rise-meal-mame=" '+hotelMealRiseText+'"></tr>');
                if(d==0){
                    $tr.append('<td class="tc" name="riseMealName" rowspan="'+dayLength+'">'+hotelMealRiseText+'</td>');
                }
                $tr.append('<td class="tc" name="riseMealPeriod">'+startDate+'~'+endDate+'</td>');
                for(var i in travelerTypes){
                    var travelerTypeUuid = travelerTypes[i].uuid;
                    var travelerPrice= getObjectByProp(dayMealPrice,"travelerTypeUuid",travelerTypeUuid);
                    var amount="";
                    var uuid="";
                    if(travelerPrice){
                    	if((travelerPrice.amount != null) || (travelerPrice.amount != undefined)) {
                            amount = travelerPrice.amount;
                    	}
                        uuid= travelerPrice.uuid;
                    }
                    $tr.append('<td class="tc"><input data-type="amount" maxlength="9" value="'+amount+'" uuid="'+uuid+'" traveler-type-uuid="'+travelerTypeUuid+'" type="text" class="tr amt" amt-code="' + baseInfo.currencyMark + '" style="width: 90%;"/></td>');
                }

                $tr.append('<td class="tc"><a class="deleteButton" href="javascript:void(0)">删除</a></td>');
                $tbody.append($tr);
            }
        }

        $tabContent_BaseMeal.append($contentTheme);
        var  riseMealMemo = getObjectByProp(hotelPlRiseMealMemoList,"hotelMealUuid",baseMealUuid);
        var memo="";
        var memoUuid="";
        if(riseMealMemo){
            memo=riseMealMemo.memo;
            memoUuid = riseMealMemo.uuid;
        }
        var memoTheme="";
        memoTheme += '<table style="width: 100%;margin-top: 10px">';
        memoTheme += '<tbody><tr>';
        memoTheme += '<td class="tr" style="width:80px;vertical-align: top;">备注：</td>';
        memoTheme += '<td>';
        memoTheme += '<textarea name="riseMealMemo" uuid="'+memoUuid+'" style="width:99%;height: 50px;">'+memo+'</textarea>';
        memoTheme += '</td>';
        memoTheme += '</tr>';
        memoTheme += '</tbody>';
        memoTheme += '</table>';
        $tabContent_BaseMeal.append(memoTheme);
    }
    var $btnAddRiseMeal = $("#btnAddRiseMeal");
    $btnAddRiseMeal.removeClass("edit_button");
    var $btnEditRiseMeal = $("#btnEditRiseMeal");
    $btnEditRiseMeal.addClass("edit_button");
    var $btnSaveRiseMeal = $("#btnSaveRiseMeal");
    var $region = $btnSaveRiseMeal.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditRiseMeal.removeClass("region_disabled");
    $btnSaveRiseMeal.addClass("region_disabled");
}

/**
 * 酒店价单详情页面,绑定数据
 * @param hotelRiseMealMap
 * @param hotelPlRiseMealMemoList
 */
function bindDetailRiseMeal(hotelRiseMealMap,hotelPlRiseMealMemoList){
    for(var baseMealUuid in hotelRiseMealMap){
        var baseMealPrices = hotelRiseMealMap[baseMealUuid];
        var $tabContent_BaseMeal = $dvContainer_BaseMeal.find('[name="tabContent_BaseMeal"][base-meal-uuid=' + baseMealUuid + ']');
        var contentTheme = '';
        contentTheme += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" name="tbRiseMeal">';
        contentTheme += '<thead>';
        contentTheme += '<tr>';
        contentTheme += '<th width="130px">升级餐型</th>';
        contentTheme += '<th width="170px">日期</th>';
        for(var t in  travelerTypes){
            contentTheme += '<th>'+travelerTypes[t].name+'</th>';
        }
        contentTheme += '</tr>';
        contentTheme += '</thead>';
        contentTheme += '<tbody name="riseMealList">';
        contentTheme += '</tbody>';
        contentTheme += '</table>';
        var $contentTheme =$(contentTheme);
        var riseMealPrices =groupArrayByProp(baseMealPrices,"hotelMealRiseUuid");
        for (var r in riseMealPrices) {
            var $tbody = $contentTheme.find('tbody[name="riseMealList"]');
            var riseMealPrice = riseMealPrices[r];
            var hotelMealRiseUuid = riseMealPrice[0].hotelMealRiseUuid;
            var hotelMealRiseText = riseMealPrice[0].hotelMealRiseText;
            var dayMealPrices = groupArrayByProp(riseMealPrice,"startDate");
            var dayLength = dayMealPrices.length;
            for(var d =0;d<dayLength;d++){
                var dayMealPrice=dayMealPrices[d];
                var startDate = dayMealPrice[0].startDate;
                var endDate = dayMealPrice[0].endDate;

                var $tr=$('<tr rise-meal-uuid="'+hotelMealRiseUuid+'" rise-meal-mame=" '+hotelMealRiseText+'"></tr>');
                if(d==0){
                    $tr.append('<td class="tc" name="riseMealName" rowspan="'+dayLength+'">'+hotelMealRiseText+'</td>');
                }
                $tr.append('<td class="tc" name="riseMealPeriod">'+startDate+'~'+endDate+'</td>');
                for(var i in travelerTypes){
                    var travelerTypeUuid = travelerTypes[i].uuid;
                    var travelerPrice= getObjectByProp(dayMealPrice,"travelerTypeUuid",travelerTypeUuid);
                    var amount="";
                    var uuid="";
                    if(travelerPrice){
                    	if((travelerPrice.amount != null) || (travelerPrice.amount != undefined)) {
                            amount = travelerPrice.amount;
                    	}
                        uuid= travelerPrice.uuid;
                    }
                    $tr.append('<td class="tc">' + baseInfo.currencyMark + formatCurrency(amount) + '</td>');
                }

                $tbody.append($tr);
            }
        }

        $tabContent_BaseMeal.append($contentTheme);
        var  riseMealMemo = getObjectByProp(hotelPlRiseMealMemoList,"hotelMealUuid",baseMealUuid);
        var memo="";
        var memoUuid="";
        if(riseMealMemo){
            memo=riseMealMemo.memo;
            memoUuid = riseMealMemo.uuid;
        }
        var memoTheme="";
        memoTheme += '<div class="note_msg_bg_tb">';
        memoTheme += '<label class="note_msg_labs">备注：</label>';
        memoTheme += '<div class="note_msgs">';
        memoTheme += memo.replace(/\n/g,"<br/>");
        memoTheme += '</div>';
        memoTheme += '</div>';
        $tabContent_BaseMeal.append(memoTheme);
    }
    var $btnAddRiseMeal = $("#btnAddRiseMeal");
    $btnAddRiseMeal.removeClass("edit_button");
    var $btnEditRiseMeal = $("#btnEditRiseMeal");
    $btnEditRiseMeal.addClass("edit_button");
    var $btnSaveRiseMeal = $("#btnSaveRiseMeal");
    var $region = $btnSaveRiseMeal.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditRiseMeal.removeClass("region_disabled");
    $btnSaveRiseMeal.addClass("region_disabled");
}

/**
 * 保存酒店价单升餐费用
 */
$(document).on("click", "#btnSaveRiseMeal", function (e) {
	var status = 1;
	//当updateFlag为1或相应模块的保存状态为1时
	if($("#updateFlag").val() == "2" || $("#hotelRiseMealSaveFlag").val() == "2") {
		status = 2;
	} else {
		$("#hotelRiseMealSaveFlag").val("2");
	}
	
	var url = ctx + "/hotelPl/saveHotelPlRiseMeal";
	var hotelPlRiseMealJsonData = JSON.stringify(getSaveRiseMeal());
	var riseMealMemoJsonData = JSON.stringify(getSaveRiseMealMemo());
	ajaxStart();
	$.ajax({
		type: "POST",
	   	url: url,
	   	async: false,
	   	data: {
				"hotelPlRiseMealJsonData":hotelPlRiseMealJsonData,
				"riseMealMemoJsonData":riseMealMemoJsonData,
				"status":status,
				"hotelPlUuid":baseInfo.uuid
			  },
		dataType: "json",
	   	success: function(data){
	   		ajaxStop();
	   		if(data){
	   			if(data.result == 1) {
	   				$.jBox.tip("升餐费用保存成功！");
	   			} else if(data.result == 2) {
	   				$.jBox.tip("升餐费用更新成功！");
	   			} else if(data.result == 3) {
	   				$.jBox.tip(data.message);
	   			}
	   		}
	   	}
	});
	
	saveButtonCli($(this));
});

function getSaveRiseMeal(){
    var hotelRiseMeals=[];
    $dvContainer_BaseMeal.find('[name="tbRiseMeal"]').each(function(){
        var $tbRiseMeal = $(this);
        var $tabContent_BaseMeal = $tbRiseMeal.parents('[name="tabContent_BaseMeal"]');
        var baseMealUuid = $tabContent_BaseMeal.attr('base-meal-uuid');
        $tbRiseMeal.find('tbody[name="riseMealList"] tr').each(function(){
            var $tr= $(this);
            var hotelMealRiseUuid =$tr.attr('rise-meal-uuid');
            var hotelMealRiseText =$tr.attr('rise-meal-mame');
            var period = $tr.find('td[name="riseMealPeriod"]').text();
            var startDate= period.split('~')[0];
            var endDate= period.split('~')[1];
            for(var t in travelerTypes){
                var $travelerPrice = $tr.find('[traveler-type-uuid="'+travelerTypes[t].uuid+'"]');
                hotelRiseMeals.push({
                    "amount": $travelerPrice.val(),
                    "currencyId": baseInfo.currencyId,
                    "currencyMark":baseInfo.currencyMark,
                    "endDate":endDate,
                    "endDateString": endDate,
                    "hotelMealRiseText": hotelMealRiseText,
                    "hotelMealRiseUuid": hotelMealRiseUuid,
                    "hotelMealUuid": baseMealUuid,
                    "hotelPlUuid": baseInfo.uuid,
                    "startDate": startDate,
                    "startDateString":startDate,
                    "travelerTypeUuid":travelerTypes[t].uuid,
                    "uuid":  $travelerPrice.attr("uuid")
                });
            }
        });
    });
    return hotelRiseMeals;
}

function getSaveRiseMealMemo(){
    var riseMealMemoList=[];
    $dvContainer_BaseMeal.find('[name="riseMealMemo"]').each(function () {
        var $riseMealMemo = $(this);
        var baseMealUuid = $riseMealMemo.parents('[name="tabContent_BaseMeal"]').attr("base-meal-uuid");
        riseMealMemoList.push({
            "hotelPlUuid": baseInfo.uuid,
            "hotelMealUuid": baseMealUuid,
            "memo": $riseMealMemo.val(),
            "uuid": $riseMealMemo.attr("uuid")
        });
    });
    return riseMealMemoList;
}