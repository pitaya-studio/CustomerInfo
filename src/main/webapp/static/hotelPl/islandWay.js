var $dvContainer_IslandWay;
var $tabHeader_IslandWay;
function setIslandWayScrollListTitle() {
    $dvContainer_IslandWay.find(".title_tabScroll_list").remove();
    $dvContainer_IslandWay.find("[name='tabContent_IslandWay']").has("[name='tbIslandWay']").each(function (index) {
        var $this = $(this);
        var islandWay = $this.attr("island-way-uuid");
        var islandWayText = $tabHeader_IslandWay.find("[island-way-uuid='" + islandWay + "']").text();
        var titleTheme = "<div class='title_tabScroll_list'>" + (index + 1) + "." + islandWayText + "</div>"
        $this.prepend(titleTheme);
    });
}
$(document).ready(function () {
    $dvContainer_IslandWay = $("#dvContainer_IslandWay");
    $tabHeader_IslandWay = $("#tabHeader_IslandWay");
    $("#btnAddIslandWay").click(fn_PopIslandWay);
    $tabHeader_IslandWay.parent().on("tabScroll.displayChange", function (e, displayType) {
        $dvContainer_IslandWay.data("displayType", displayType);
        if (displayType == "list") {
            $("#btnAddIslandWay").addClass("tabScroll_list");
            setIslandWayScrollListTitle();
        } else {
            $("#btnAddIslandWay").removeClass("tabScroll_list");
            $dvContainer_IslandWay.find(".title_tabScroll_list").remove();
        }
    });
    $dvContainer_IslandWay.on("datarow.delete", "[name='tabContent_IslandWay']", function (e, row) {
        //                var $tabContent_IslandWay = $(this);
        //                var period = $(row).find(".roomTypeDate").text();
        //                $tabContent_IslandWay.find("[name='IslandWayDateList'] tr").each(function () {
        //                    var $this = $(this);
        //                    if ($this.find(".roomTypeDate").text() == period) {
        //                        $this.remove();
        //                    }
        //                });
    });
    $dvContainer_IslandWay.on("emptied", "[datarow-emptyRow-behavior]", function () {
        if ($dvContainer_IslandWay.data("displayType") == "list") {
            setIslandWayScrollListTitle();
        }
        if ($dvContainer_IslandWay.find("[name='tabContent_IslandWay'] [name='tbIslandWay']").length == 0) {
            $dvContainer_IslandWay.trigger("modeChange");
        }
    });
    $dvContainer_IslandWay.on("modeChange", function () {
        var $btnToggleIslandWay = $("#btnToggleIslandWay");
        var $btnAddIslandWay = $("#btnAddIslandWay");
        var $btnEditIslandWay = $("#btnEditIslandWay");
        if ($dvContainer_IslandWay.find("[name='tabContent_IslandWay'] [name='tbIslandWay']").length == 0) {
            $dvContainer_IslandWay.data("mode", "create");
            //$btnToggleIslandWay.hide();
            $btnAddIslandWay.text("新建上岛方式日期");
            $btnAddIslandWay.addClass("edit_button");
            $btnEditIslandWay.removeClass("edit_button");
            $btnEditIslandWay.addClass("region_disabled");
        } else {
            $dvContainer_IslandWay.data("mode", "modify");
            //$btnToggleIslandWay.show();
            $btnAddIslandWay.text("修改上岛方式日期");
            $btnAddIslandWay.removeClass("edit_button");
            $btnAddIslandWay.removeClass("region_disabled");
            $btnEditIslandWay.addClass("edit_button");
            $btnEditIslandWay.addClass("region_disabled");
        }
    });
});

/**
 * 获取pop中选中的交通方式
 * @param pop
 * @returns {Array}
 */
function getIslandWays(pop) {
    var islandWays = [];
    //获取所有的房间类型
    pop.find("input:checked[island-way-uuid!='All']").each(function () {
        var $this = $(this);
        islandWays.push({
            uuid: $this.attr("island-way-uuid"),
            label: $this.parent().text()
        });
    });
    return islandWays;
}
/**
 * 获取pop中所有添加的日期
 * @param pop
 * @returns {Array}
 */
function getIslandLoadAddedDates(pop) {
    var islandLoadDates = [];
    pop.find(".islandLoadDate").each(function () {
        var $this = $(this);
        var startDate = $this.find("[name=dtpIslandWayStartDate]").val();
        var endDate = $this.find("[name=dtpIslandWayEndDate]").val();
        islandLoadDates.push({
            startDate: startDate,
            endDate: endDate
        });

    });
    return islandLoadDates;
}
function getExistIslandLoadDates(islandWay) {
    var dvContainer_IslandWay = $("#dvContainer_IslandWay");
    var content_IslandWay = dvContainer_IslandWay.find("[island-way-uuid=" + islandWay + "]");
    var existIslandLoadDates = [];
    content_IslandWay.find("td.islandLoadDate").each(function () {
        var datePeriod = $(this).text();
        existIslandLoadDates.push({
            startDate: datePeriod.split("~")[0],
            endDate: datePeriod.split("~")[1]
        });
    });
    return existIslandLoadDates;
}
function setPopIslandLoadDate(existDatesList, pop) {
	pop.find(".islandLoadDate:gt(0)").remove();
    pop.find(".islandLoadDate [name='dtpIslandWayStartDate']").val("");
    pop.find(".islandLoadDate [name='dtpIslandWayEndDate']").val("");
    
    if (isSamePeriod(existDatesList)) {
        pop.data("mode", "modify");
        var existDates = existDatesList[0];
        for (var index in existDates) {
            var datePeriod = existDates[index];
            if (index > 0) {
                var rowTheme = '<tr class="islandLoadDate">';
                rowTheme += '<td class="tr"><span class="xing">*</span>日期：</td>';
                rowTheme += '<td colspan="2">';
                rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpIslandWayStartDate" readonly="readonly" />';
                rowTheme += ' 至 ';
                rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpIslandWayEndDate" readonly="readonly" />';
                rowTheme += '<i class="price_sale_house_02"></i>';
                rowTheme += '</td>';
                rowTheme += '</tr>';
                pop.find(".islandLoadDate").parent().append(rowTheme);
            }
            pop.find(".islandLoadDate:eq(" + index + ") [name='dtpIslandWayStartDate']").val(datePeriod.startDate);
            pop.find(".islandLoadDate:eq(" + index + ") [name='dtpIslandWayEndDate']").val(datePeriod.endDate);
        }
    }
    else {
        pop.data("mode", "create");
        pop.find(".islandLoadDate:gt(0)").remove();
        pop.find(".islandLoadDate [name='dtpIslandWayStartDate']").val("");
        pop.find(".islandLoadDate [name='dtpIslandWayEndDate']").val("");
    }
    pop.trigger("modeChange");
}
function fn_PopIslandWay() {
    var html = $("#popIslandWay").html();
    var pop = $.jBox(html, {
        title: "编辑上岛方式及日期：",
        buttons: { '保存': 1 },
        submit: function (v, h, f) {
            if (v == "1") {
                //获取上岛类型
                var islandTypes = getIslandWays(pop);
                var islandLoadDates = getIslandLoadAddedDates(pop);
                if (checkIslandWay(islandTypes, islandLoadDates, pop)) {
                    addIslandWays(islandTypes, islandLoadDates, pop);
                    $("#btnAddIslandWay").text("编辑上岛方式及日期");
                    return true;
                } else {
                    return false;
                }
            }
        }, width: 480, height: 350
    });

    pop.on("click", ".price_sale_house_01,.price_sale_house_02", function () {
        var $this = $(this);
        //添加日期
        if ($this.is("#btnAddIslandWayDate")) {
            var rowTheme = '<tr class="islandLoadDate">';
            rowTheme += '<td class="tr"><span class="xing">*</span>日期：</td>';
            rowTheme += '<td colspan="3">';
            rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpIslandWayStartDate" readonly="readonly" />';
            rowTheme += ' 至 ';
            rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpIslandWayEndDate" readonly="readonly" />';
            rowTheme += '<i class="price_sale_house_02"></i>';
            rowTheme += '</td>';
            rowTheme += '</tr>';
            $this.parents("table:first").append(rowTheme);
        }
        //删除日期
        else {
            $this.parents("tr:first").remove();
        }
    });
    pop.on("click", "[checkbox-action='All']", function () {
        var allChecked = $(this).is(":checked");
        if (allChecked) {
            pop.find("[type='checkbox']").attr("checked", allChecked);
        } else {
            pop.find("[type='checkbox']").removeAttr("checked");
        }
        $(this).trigger("change");
    })
    pop.on("change", "[type='checkbox']", function () {
        if ($(this).is("input[checkbox-action!='All']")) {
            var $islandWay = pop.find("#pop_IslandWay");
            if ($islandWay.find("input[checkbox-action!='All']").length == $islandWay.find("input[checkbox-action!='All']:checked").length) {
                pop.find("input[checkbox-action='All']").attr("checked", true);
            } else {
                pop.find("input[checkbox-action='All']").removeAttr("checked");
            }
        }
        var selectedIslandWays = getIslandWays(pop);
        var existIslandLoadDatesList = [];
        for (var index in selectedIslandWays) {
            var islandWay = selectedIslandWays[index].uuid;
            existIslandLoadDatesList.push(getExistIslandLoadDates(islandWay));
        }
        setPopIslandLoadDate(existIslandLoadDatesList, pop);
    });
    pop.on("modeChange", function () {
        var popMode = pop.data("mode");
        if (popMode == "create") {
            if (pop.find(".createMsg").length == 0) {
                var createMsgTheme = '<tr class="createMsg"><td></td><td  style="color:#FF0000">您选择的上岛方式日期不一致，只能进行新增操作。</td></tr>';
                pop.find(".islandLoadDate:first").before(createMsgTheme);
            }
        } else {
            pop.find(".createMsg").remove();
        }
    });
    var $dvContainer_IslandWay = $("#dvContainer_IslandWay");
    var $pop_roomPrice_roomType = pop.find("#pop_IslandWay");
    for (var i in islandWays) {
        var islandWay = islandWays[i];
        var popIslandWayTheme = '<label class="title-lg"><input type="checkbox"  style="margin: 0px" name="IslandWay" island-way-uuid="' + islandWay.uuid + '" />' + islandWay.label + '</label>';
        $pop_roomPrice_roomType.append(popIslandWayTheme);
    }
    if ($dvContainer_IslandWay.data("mode") == "modify") {
        $dvContainer_IslandWay.children(":visible").each(function () {
            var content_IslandWay = $(this);
            var islandWayCode = content_IslandWay.attr("island-way-uuid");
            pop.find("input[type='checkbox']").removeAttr("checked");
            pop.find("input[type='checkbox'][island-way-uuid='" + islandWayCode + "']").attr("checked", true);
            pop.find("input[type='checkbox'][island-way-uuid='" + islandWayCode + "']").trigger("change");
        });
    } else {
        pop.find("input[type='checkbox']").attr("checked", true);
    }
}
function compareIslandLoadDates(existDates, addedDates) {
    var retainIslandLoadDateList = [];
    var removeIslandLoadDateList = [];
    var appendIslandLoadDateList = [];
    for (var i in existDates) {
        var existStartDate = existDates[i].startDate;
        var existEndDate = existDates[i].endDate;
        for (var j in addedDates) {
            var addedStartDate = addedDates[j].startDate;
            var addedEndDate = addedDates[j].endDate;
            if (existStartDate == addedStartDate && existEndDate == addedEndDate) {
                retainIslandLoadDateList.push({
                    startDate: existStartDate,
                    endDate: existEndDate
                });
                break;
            }
        }
    }
    for (var i in existDates) {
        var existStartDate = existDates[i].startDate;
        var existEndDate = existDates[i].endDate;
        var removeChk = true;
        for (var j in retainIslandLoadDateList) {
            var retainStartDate = retainIslandLoadDateList[j].startDate;
            var retainEndDate = retainIslandLoadDateList[j].endDate;
            if (existStartDate == retainStartDate && existEndDate == retainEndDate) {
                removeChk = false;
                break;
            }
        }
        if (removeChk) {
            removeIslandLoadDateList.push({
                startDate: existStartDate,
                endDate: existEndDate
            });
        }
    }
    for (var i in addedDates) {
        var addedDate = addedDates[i].startDate;
        var addedEndDate = addedDates[i].endDate;
        var appendChk = true;
        for (var j in retainIslandLoadDateList) {
            var retainStartDate = retainIslandLoadDateList[j].startDate;
            var retainEndDate = retainIslandLoadDateList[j].endDate;
            if (addedDate == retainStartDate && addedEndDate == retainEndDate) {
                appendChk = false;
                break;
            }
        }
        if (appendChk) {
            appendIslandLoadDateList.push({
                startDate: addedDate,
                endDate: addedEndDate
            });
        }
    }
    return {
        removeIslandLoadDateList: removeIslandLoadDateList,
        appendIslandLoadDateList: appendIslandLoadDateList
    };
}
function checkIslandWay(islandWays, islandLoadDates, pop) {
    var popMode = pop.data("mode");
    var reduplicatedList = [];
    for (var i in islandLoadDates) {
        var startDate = islandLoadDates[i].startDate;
        var endDate = islandLoadDates[i].endDate;
        if (!startDate || !endDate) {
            infoBox("日期不能为空!!");
            return false;
        }
        if (endDate && startDate > endDate) {
            infoBox("开始日期不能大于结束日期");
            return false;
        }
    };
    var dvContainer_IslandWay = $("#dvContainer_IslandWay");
    for (var i in islandWays) {
        var islandWay = islandWays[i];
        var existIslandLoadDates = getExistIslandLoadDates(islandWay.uuid);
        var allIslandWay = [];
        if (popMode == "modify") {
            allIslandWay = allIslandWay.concat(islandLoadDates);
        } else {
            allIslandWay = allIslandWay.concat(existIslandLoadDates);
            allIslandWay = allIslandWay.concat(islandLoadDates);
        }
        var reduplicatedPeriods = [];
        checkReduplicated(allIslandWay, reduplicatedPeriods);
        if (reduplicatedPeriods.length > 0) {
            reduplicatedList.push({
                islandWay: islandWay,
                reduplicatedPeriods: reduplicatedPeriods
            });
        }
    }
    if (reduplicatedList.length > 0) {
        var errorString = "存在重复期间<br/>";
        for (var i in reduplicatedList) {
            var islandWay = reduplicatedList[i].islandWay;
            var reduplicatedPeriods = reduplicatedList[i].reduplicatedPeriods;
            errorString += "房间类型：" + islandWay.label + "<br/>";
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
    
    if(islandWays.length == 0) {
    	infoBox("至少选择一种上岛方式！");
    	return false;
    }
    
    return true;
};
/**
 * 将上岛的交通方式的价格期间添加到价格期间的容器中
 * @param islandLoadDate 期间
 * @param content_IslandWay 容器
 */
function addIslandDateToContent(islandLoadDate, content_IslandWay) {
    var startDate = islandLoadDate.startDate;
    var endDate = islandLoadDate.endDate;
    var rowTheme = "";
    rowTheme += '<tr >';
    rowTheme += '<td class="tc islandLoadDate">' + startDate + '~' + endDate + '</td>';
    for(var t in travelerTypes){
        rowTheme +=  '<td class="tc"><input data-type="amount" maxlength="9" uuid="" traveler-type-uuid="'+travelerTypes[t].uuid+'" type="text"class="tr amt" amt-code="' +  baseInfo.currencyMark + '" style="width: 90%"/></td>';;
    }
    rowTheme += '<td class="tc">' + delButtonTheme + '</td>';
    rowTheme += '</tr>';
    var $tbody = content_IslandWay.find("tbody[name='IslandWayDateList']");
    $tbody.find("tr").sortedInsert(rowTheme,$tbody,function(el){
            return $(el).find("td:first").text();
    });
}
function addIslandWays(islandTypes, islandLoadDates, pop) {
    var tabHeader_IslandWay = $("#tabHeader_IslandWay");
    var dvContainer_IslandWay = $("#dvContainer_IslandWay");

    for (var i in islandTypes) {
        var islandWayCode = islandTypes[i].uuid;
        var islandWayText = islandTypes[i].label;
        var $tabContent_IslandWay = dvContainer_IslandWay.find("[island-way-uuid='" + islandWayCode + "']");
        var addedIslandList = [];

        if ($tabContent_IslandWay.find("table").length == 0) {
            var contentTheme = "";
            contentTheme += '<table style="padding-top:20px" class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" name="tbIslandWay">';
            contentTheme += '<thead>';
            contentTheme += '<tr>';
            contentTheme += '<th width="170px">日期</th>';
            for(var t in travelerTypes){
                contentTheme += '<th>'+travelerTypes[t].name+'</th>';
            }
            contentTheme += '<th width="100px">操作</th>';
            contentTheme += '</tr>';
            contentTheme += '</thead>';
            contentTheme += '<tbody name="IslandWayDateList">';

            contentTheme += '</tbody>';
            contentTheme += '</table>';
            contentTheme += '<table style="width: 100%;margin-top: 10px">';
            contentTheme += '<tr>';
            contentTheme += '<td class="tr" style="width:80px;vertical-align: top;">备注：</td>';
            contentTheme += '<td>';
            contentTheme += '<textarea uuid="" name="islandWayMemo" style="width:99%;height: 50px;"></textarea>';
            contentTheme += '</td>';
            contentTheme += '</tr>';
            contentTheme += '</table>';
            $tabContent_IslandWay.append(contentTheme);
        }

        if (pop.data("mode") == "modify") {
            var compareDate = compareIslandLoadDates(getExistIslandLoadDates(islandWayCode), islandLoadDates);
            addedIslandList = compareDate.appendIslandLoadDateList;
            $tabContent_IslandWay.find("tbody[name='IslandWayDateList']").children().each(function () {
                var $this = $(this);
                var datePeriodString = $(this).find("td.islandLoadDate").text();
                for (var j in compareDate.removeIslandLoadDateList) {
                    if (datePeriodString == compareDate.removeIslandLoadDateList[j].startDate + "~" + compareDate.removeIslandLoadDateList[j].endDate) {
                        $this.remove();
                    }
                }
            });
        } else {
            addedIslandList = islandLoadDates;
        }

        for (var j in addedIslandList) {
            var islandLoadDate = addedIslandList[j];
            addIslandDateToContent(islandLoadDate, $tabContent_IslandWay);
        }
    }
    tabScroll(tabHeader_IslandWay.parent(), "[island-way-uuid='" + islandTypes[0].uuid + "']");
    $dvContainer_IslandWay.trigger("modeChange");
    resetCurrency();
}

function bindIslandWay(islandWayList,islandWayMemoList){
	
    var firstIslandUuid = "";
    for (var islandWayUuid in islandWayList) {
        if(!firstIslandUuid){
            firstIslandUuid = islandWayUuid;
        }
        var islandWay = getObjectByProp(islandWays, "uuid", islandWayUuid)
        var islandWayPrice = islandWayList[islandWayUuid];
        if(islandWayPrice.length==0){
            continue;
        }
        var $tabContent_IslandWay = $dvContainer_IslandWay.find('[name="tabContent_IslandWay"][island-way-uuid="' + islandWayUuid + '"]');
        var contentTheme = "";
        contentTheme += '<table style="padding-top:20px" class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" name="tbIslandWay">';
        contentTheme += '<thead>';
        contentTheme += '<tr>';
        contentTheme += '<th width="170px">日期</th>';
        for(var t in travelerTypes){
            contentTheme += '<th>'+travelerTypes[t].name+'</th>';
        }
        contentTheme += '<th width="100px">操作</th>';
        contentTheme += '</tr>';
        contentTheme += '</thead>';
        contentTheme += '<tbody name="IslandWayDateList">';

        contentTheme += '</tbody>';
        contentTheme += '</table>';
        var $contentTheme = $(contentTheme);
        var $tbody = $contentTheme.find('tbody[name="IslandWayDateList"]');
        var dayIslandWayPrices = groupArrayByProp(islandWayPrice,"startDate");
        for(var d in dayIslandWayPrices){
            var dayIslandWayPrice = dayIslandWayPrices[d];

            var $row = $("<tr></tr>");
            $row.append('<td class="tc islandLoadDate">'+dayIslandWayPrice[0].startDate+'~'+dayIslandWayPrice[0].endDate+'</td>');
            for (var tt in travelerTypes) {
                var travelerTypeUuid = travelerTypes[tt].uuid;
                var travelerPrice = getObjectByProp(dayIslandWayPrice,"travelerTypeUuid",travelerTypeUuid);
                var amount="";
                var uuid="";
                if(travelerPrice){
                    uuid=travelerPrice.uuid;
                    if((travelerPrice.amount != null) || (travelerPrice.amount != undefined)) {
                        amount = travelerPrice.amount;
                    }
                }
                $row.append('<td class="tc"><input data-type="amount" maxlength="9" value="'+amount+'" uuid="'+uuid+'" traveler-type-uuid="' + travelerTypeUuid + '" ' +
                'type="text" class="tr amt" amt-code="' + baseInfo.currencyMark + '" ' +
                'style="width: 90%; padding-left: 15px;" ></td>');
            }
            $row.append('<td class="tc"><a class="deleteButton" href="javascript:void(0)">删除</a></td>');
            $tbody.append($row);
        }
        $tabContent_IslandWay.append($contentTheme);
        var islandWayMemo = getObjectByProp(islandWayMemoList,"islandWay",islandWayUuid);
        var memo="";
        var memoUuid = "";
        if(islandWayMemo){
            memo= islandWayMemo.memo;
            memoUuid = islandWayMemo.uuid;
        }
        var memoTheme="";
        memoTheme += '<table style="width: 100%;margin-top: 10px">';
        memoTheme += '<tr>';
        memoTheme += '<td class="tr" style="width:80px;vertical-align: top;">备注：</td>';
        memoTheme += '<td>';
        memoTheme += '<textarea uuid="'+memoUuid+'" name="islandWayMemo" style="width:99%;height: 50px;">'+memo+'</textarea>';
        memoTheme += '</td>';
        memoTheme += '</tr>';
        memoTheme += '</table>';
        $tabContent_IslandWay.append(memoTheme);
    }
    $dvContainer_IslandWay.trigger("modeChange");
    tabScroll($tabHeader_IslandWay.parent(), "[island-way-uuid='" + firstIslandUuid + "']");
    var $btnAddIslandWay = $("#btnAddIslandWay");
    $btnAddIslandWay.removeClass("edit_button");
    var $btnEditIslandWay = $("#btnEditIslandWay");
    $btnEditIslandWay.addClass("edit_button");
    var $btnSaveIslandWay = $("#btnSaveIslandWay");
    var $region = $btnSaveIslandWay.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditIslandWay.removeClass("region_disabled");
    $btnSaveIslandWay.addClass("region_disabled");
}

function bindDetailIslandWay(islandWayList,islandWayMemoList){
	
    var firstIslandUuid = "";
    for (var islandWayUuid in islandWayList) {
        if(!firstIslandUuid){
            firstIslandUuid = islandWayUuid;
        }
        var islandWay = getObjectByProp(islandWays, "uuid", islandWayUuid)
        var islandWayPrice = islandWayList[islandWayUuid];
        if(islandWayPrice.length==0){
            continue;
        }
        var $tabContent_IslandWay = $dvContainer_IslandWay.find('[name="tabContent_IslandWay"][island-way-uuid="' + islandWayUuid + '"]');
        var contentTheme = "";
        contentTheme += '<table style="padding-top:20px" class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" name="tbIslandWay">';
        contentTheme += '<thead>';
        contentTheme += '<tr>';
        contentTheme += '<th width="170px">日期</th>';
        for(var t in travelerTypes){
            contentTheme += '<th>'+travelerTypes[t].name+'</th>';
        }
        contentTheme += '</tr>';
        contentTheme += '</thead>';
        contentTheme += '<tbody name="IslandWayDateList">';

        contentTheme += '</tbody>';
        contentTheme += '</table>';
        var $contentTheme = $(contentTheme);
        var $tbody = $contentTheme.find('tbody[name="IslandWayDateList"]');
        var dayIslandWayPrices = groupArrayByProp(islandWayPrice,"startDate");
        for(var d in dayIslandWayPrices){
            var dayIslandWayPrice = dayIslandWayPrices[d];

            var $row = $("<tr></tr>");
            $row.append('<td class="tc islandLoadDate">'+dayIslandWayPrice[0].startDate+'~'+dayIslandWayPrice[0].endDate+'</td>');
            for (var tt in travelerTypes) {
                var travelerTypeUuid = travelerTypes[tt].uuid;
                var travelerPrice = getObjectByProp(dayIslandWayPrice,"travelerTypeUuid",travelerTypeUuid);
                var amount="";
                var uuid="";
                if(travelerPrice){
                    uuid=travelerPrice.uuid;
                    if((travelerPrice.amount != null) || (travelerPrice.amount != undefined)) {
                        amount = travelerPrice.amount;
                    }
                }
                $row.append('<td class="tc">' + baseInfo.currencyMark + formatCurrency(amount) +'</td>');
            }
            $tbody.append($row);
        }
        $tabContent_IslandWay.append($contentTheme);
        var islandWayMemo = getObjectByProp(islandWayMemoList,"islandWay",islandWayUuid);
        var memo="";
        var memoUuid = "";
        if(islandWayMemo){
            memo= islandWayMemo.memo;
            memoUuid = islandWayMemo.uuid;
        }
        var memoTheme="";
        memoTheme += '<div class="note_msg_bg_tb">';
        memoTheme += '<label class="note_msg_labs">备注：</label>';
        memoTheme += '<div class="note_msgs">';
        memoTheme += memo.replace(/\n/g,"<br/>");
        memoTheme += '</div>';
        memoTheme += '</div>';
        $tabContent_IslandWay.append(memoTheme);
    }
    $dvContainer_IslandWay.trigger("modeChange");
    tabScroll($tabHeader_IslandWay.parent(), "[island-way-uuid='" + firstIslandUuid + "']");
    var $btnAddIslandWay = $("#btnAddIslandWay");
    $btnAddIslandWay.removeClass("edit_button");
    var $btnEditIslandWay = $("#btnEditIslandWay");
    $btnEditIslandWay.addClass("edit_button");
    var $btnSaveIslandWay = $("#btnSaveIslandWay");
    var $region = $btnSaveIslandWay.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditIslandWay.removeClass("region_disabled");
    $btnSaveIslandWay.addClass("region_disabled");
}


/**
 * 保存酒店价单交通费用
 */
$(document).on("click", "#btnSaveIslandWay", function (e) {
	var status = 1;
	//当updateFlag为1或相应模块的保存状态为1时
	if($("#updateFlag").val() == "2" || $("#hotelIslandWaySaveFlag").val() == "2") {
		status = 2;
	} else {
		$("#hotelIslandWaySaveFlag").val("2");
	}
	
	var url = ctx + "/hotelPl/saveHotelPlIslandWay";
	var hotelPlIslandWayJsonData = JSON.stringify(getSaveIslandWayPrice());
	var islandWayMemoJsonData = JSON.stringify(getSaveIslandWayMemo());
	ajaxStart();
	$.ajax({
		type: "POST",
	   	url: url,
	   	async: false,
	   	data: {
				"hotelPlIslandWayJsonData":hotelPlIslandWayJsonData,
				"islandWayMemoJsonData":islandWayMemoJsonData,
				"status":status,
				"hotelPlUuid":baseInfo.uuid
			  },
		dataType: "json",
	   	success: function(data){
	   		ajaxStop();
	   		if(data){
	   			if(data.result == 1) {
	   				$.jBox.tip("交通费用 保存成功！");
	   			} else if(data.result == 2) {
	   				$.jBox.tip("交通费用 更新成功！");
	   			} else if(data.result == 3) {
	   				$.jBox.tip(data.message);
	   			}
	   		}
	   	}
	});
	
	saveButtonCli($(this));
});

function getSaveIslandWayPrice(){
    var islandWayList=[];
    $dvContainer_IslandWay.find('[name="tbIslandWay"]').each(function () {
        var $tbIslandWay = $(this);
        var islandWayUuid = $tbIslandWay.parents('[name="tabContent_IslandWay"]').attr("island-way-uuid");
        $tbIslandWay.find('tbody[name="IslandWayDateList"] tr').each(function(){
            var $tr = $(this);
            var period = $tr.find('td:first').text();
            var startDate = period.split("~")[0];
            var endDate = period.split("~")[1];
            for(var t in travelerTypes){
                var travelerTypeUuid = travelerTypes[t].uuid;
                var $travelerPrice = $tr.find('[traveler-type-uuid="'+travelerTypeUuid+'"]');
                islandWayList.push(
                    {
                        "amount": $travelerPrice.val(),
                        "currencyId": baseInfo.currencyId,
                        "currencyMark": baseInfo.currencyMark,
                        "endDate": endDate,
                        "endDateString": endDate,
                        "hotelPlUuid": baseInfo.uuid,
                        "islandWay": islandWayUuid,
                        "startDate": startDate,
                        "startDateString": startDate,
                        "travelerTypeUuid": travelerTypeUuid,
                        "uuid": $travelerPrice.attr('uuid')
                    }
                );
            }
        });
    });
    return islandWayList;
}

function getSaveIslandWayMemo(){
    var islandWayMemoList=[];
    $dvContainer_IslandWay.find('[name="islandWayMemo"]').each(function () {
        var $islandWayMemo = $(this);
        var islandWayUuid = $islandWayMemo.parents('[name="tabContent_IslandWay"]').attr("island-way-uuid");
        islandWayMemoList.push({

            "hotelPlUuid": baseInfo.uuid,
            "islandWay": islandWayUuid,
            "memo": $islandWayMemo.val(),
            "uuid": $islandWayMemo.attr("uuid")
        });
    });
    return islandWayMemoList;
}