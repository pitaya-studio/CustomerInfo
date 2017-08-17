var $dvContainer_HotelRoomPrice;
var $tableHotelRoomPrice_CompetitionPrice;
var $tabHeader_HotelRoomPrice;

//tab展开的时候，设置各个tab的title
function setHotelRoomPriceScrollListTitle() {
    $dvContainer_HotelRoomPrice.find(".title_tabScroll_list").remove();
    $dvContainer_HotelRoomPrice.find("[name='tabContent_HotelRoomPrice']").has("[name='tabContent_BaseMeal_HotelRoomPrice']").each(function (index) {
        var $this = $(this);
        var roomType = $this.attr("hotel-room-uuid");
        var hotelRoomText = $tabHeader_HotelRoomPrice.find("[hotel-room-uuid='" + roomType + "']").text();
        var titleTheme = "<div class='title_tabScroll_list'>" + (index + 1) + "." + hotelRoomText + "</div>"
        $this.prepend(titleTheme);
    });
}
$(document).ready(function () {
    $dvContainer_HotelRoomPrice = $("#dvContainer_HotelRoomPrice");
    $tableHotelRoomPrice_CompetitionPrice = $("#tableHotelRoomPrice_CompetitionPrice");
    $tabHeader_HotelRoomPrice = $("#tabHeader_HotelRoomPrice");
    $("#btnHotelRoomPrice").click(fn_PopHotelRoomPrice);
    $("#btnSetCompetitionPrice").click(function () {
        var changePrice = parseFloat($("#txtChangePrice").val());
        if (isNaN(changePrice)) {
            return;
        }
        if ($("#slChangType").val() == "减少") {
            changePrice *= -1;
        }
        $dvContainer_HotelRoomPrice.find("[price-type='1']").each(function () {
            var txtCompetitionPrice = $(this);
            var price = parseFloat(txtCompetitionPrice.parents("td:first").prev().find("input").val());
            if (!isNaN(price)) {
                var competitionPrice = price + changePrice;
                txtCompetitionPrice.val(competitionPrice);
            }

        });
        /*$("#txtChangePrice").val("");*/
    });
    $dvContainer_HotelRoomPrice.on("click", "[name='btnBatchRoomPrice']", function () {
        $(this).parents("tr:first").find("input[type=text]").each(function (index) {
            var price = $(this).val();
            if (price) {
                $(this).parents("table:first").find("[name='roomDatePriceList'] tr").each(function () {
                    $(this).find("input").eq(index).val(price);
                });
                $(this).val("");
            }
        });
    });

    $tabHeader_HotelRoomPrice.parent().on("tabScroll.displayChange", function (e, displayType) {
        $dvContainer_HotelRoomPrice.data("displayType", displayType);
        if (displayType == "list") {
            $("#btnHotelRoomPrice").addClass("tabScroll_list");
            setHotelRoomPriceScrollListTitle();
        } else {
            $("#btnHotelRoomPrice").removeClass("tabScroll_list");
            $dvContainer_HotelRoomPrice.find(".title_tabScroll_list").remove();
        }
    });
    //            $tabHeader_HotelRoomPrice.parent().on("tabScroll.change", function () {
    //                var roomType = $(this).find(".active").attr("hotel-room-uuid");
    //                if ($dvContainer_HotelRoomPrice.find("[name='tabContent_HotelRoomPrice'][hotel-room-uuid='" + roomType + "']").children().length > 0) {
    //                    $tableHotelRoomPrice_CompetitionPrice.show();
    //                } else {
    //                    $tableHotelRoomPrice_CompetitionPrice.hide();
    //                }
    //            });
    //每个房间类型下面的基本餐型check变更事件
    $dvContainer_HotelRoomPrice.on("change", "[name='spRoomBaseMeal'] [type='checkbox']", function () {
        var $this = $(this);
        var baseMealUuid = $this.attr("base-meal-uuid");
        var baseMealText = $this.parent().text();
        var $tabContent_HotelRoomPrice = $this.parents("[name='tabContent_HotelRoomPrice']");
        var baseMealUuidList = [];
        $this.parent().parent().find("[type='checkbox']").each(function () {
            baseMealUuidList.push($(this).attr("base-meal-uuid"));
        });
        var changedbaseMealUuid = $this.attr("base-meal-uuid");
        var ischecked = $this.is(":checked");
        if (ischecked) {
            var samebaseMealUuidCheckBox = $tabContent_HotelRoomPrice.find("[base-meal-uuid='" + baseMealUuid + "']:checked");
            if (samebaseMealUuidCheckBox.length > 1) {
                var msg = "餐型已存在，确定要修改吗？<br/>如确定修改设置，则该餐型已维护好的价格将被清空，需重新填写价格。";
                confirmBox(msg, function () {
                    var $other = samebaseMealUuidCheckBox.not($this);
                    $other.removeAttr("checked");
                    $other.trigger("change");
                }, function () {
                    $this.removeAttr("checked");
                });
            }
        }
        else {
            var sibingBaseMealList = [];
            $this.parent().parent().find("[type='checkbox']:checked").each(function () {
                sibingBaseMealList.push($(this).attr("base-meal-uuid"));
            });
            //基础餐型都移除的时候，移除该table{如果该房型下只剩下一个table的时候例外}
            if (sibingBaseMealList.length == 0 && $tabContent_HotelRoomPrice.find("[name='tabContent_BaseMeal_HotelRoomPrice']").length > 1) {
                $this.parents("[name='tabContent_BaseMeal_HotelRoomPrice']").remove();
            }
        }
        var checkedbaseMealUuidList = [];
        $tabContent_HotelRoomPrice.find("[name='spRoomBaseMeal'] [type='checkbox']:checked").each(function () {
            checkedbaseMealUuidList.push($(this).attr("base-meal-uuid"));
        });
        var uncheckbaseMealUuidList = [];
        for (var i in baseMealUuidList) {
            var tmpbaseMealUuid = baseMealUuidList[i];
            var isUnchecked = true;
            for (var j in checkedbaseMealUuidList) {
                if (tmpbaseMealUuid == checkedbaseMealUuidList[j]) {
                    isUnchecked = false;
                    break;
                }
            }
            if (isUnchecked) {
                uncheckbaseMealUuidList.push(tmpbaseMealUuid);
            }
        }
        if (uncheckbaseMealUuidList.length == 0) {
            $tabContent_HotelRoomPrice.find("[name='btnCopyRoomPrice']").addClass("basemeal-allSelected");
        } else {
            $tabContent_HotelRoomPrice.find("[name='btnCopyRoomPrice']").removeClass("basemeal-allSelected");
        }
    });

    $dvContainer_HotelRoomPrice.on("click", "[name='btnCopyRoomPrice']", function () {
        var $this = $(this);
        var hotelRoomUuid = $this.parents("[name='tabContent_HotelRoomPrice']").attr("hotel-room-uuid");
        var $tabContent_HotelRoomPrice = $this.parents("[name='tabContent_HotelRoomPrice']");
        var $tabContent_BaseMeal_HotelRoomPrice = $this.parents("[name='tabContent_BaseMeal_HotelRoomPrice']");
        var baseMealList = getRoomTypeBaseMealList(hotelRoomUuid)
        var checkedBaseMealList = [];
        $tabContent_HotelRoomPrice.find("[name='spRoomBaseMeal'] [type='checkbox']:checked").each(function () {
            checkedBaseMealList.push($(this).attr("base-meal-uuid"));
        });
        var uncheckBaseMealList = [];
        for (var uuid in baseMealList) {
            var baseMealUuid =uuid;
            var isUnchecked = true;
            for (var j in checkedBaseMealList) {
                if (baseMealUuid == checkedBaseMealList[j]) {
                    isUnchecked = false;
                    break;
                }
            }
            if (isUnchecked) {
                uncheckBaseMealList.push(baseMealUuid);
            }
        }
        var $tabContent_BaseMeal_HotelRoomPrice_Copy = $tabContent_BaseMeal_HotelRoomPrice.clone();
        $tabContent_HotelRoomPrice.find("[name='roomTypeRemark']").before($tabContent_BaseMeal_HotelRoomPrice_Copy);
        $tabContent_HotelRoomPrice.find("[name='btnCopyRoomPrice']").addClass("baseMeal-allSelected");
        $tabContent_BaseMeal_HotelRoomPrice_Copy.find("[type='text']").val("");
        $tabContent_BaseMeal_HotelRoomPrice_Copy.find("[name='spRoomBaseMeal'] [type='checkbox']").each(function () {
            var $checkBox = $(this);
            var baseMealUuid = $checkBox.attr("base-meal-uuid");
            var isUnchecked = false;
            for (var i in uncheckBaseMealList) {
                if (baseMealUuid == uncheckBaseMealList[i]) {
                    isUnchecked = true;
                    break;
                }
            }
            if (isUnchecked) {
                $checkBox.attr("checked", true);
            } else {
                $checkBox.removeAttr("checked");
            }
        });
    });
    //酒店房型的期间被删除后,需要同步该房型下其他餐型的期间
    $dvContainer_HotelRoomPrice.on("datarow.delete", "[name='tabContent_HotelRoomPrice']", function (e, row) {
        var $tabContent_HotelRoomPrice = $(this);
        var period = $(row).find(".roomTypeDate").text();
        $tabContent_HotelRoomPrice.find("[name='roomDatePriceList'] tr").each(function () {
            var $this = $(this);
            if ($this.find(".roomTypeDate").text() == period) {
                $this.remove();
            }
        });
    });

    //酒店房型下的所有价格期间被删除后,需要清空该房型下的表头和备注
    $dvContainer_HotelRoomPrice.on("emptied", "[datarow-emptyRow-behavior]", function () {
        if ($dvContainer_HotelRoomPrice.data("displayType") == "list") {
            setHotelRoomPriceScrollListTitle();
        }
        var roomType = $tabHeader_HotelRoomPrice.find(".active").attr("hotel-room-uuid");
        if ($dvContainer_HotelRoomPrice.find("[name='tabContent_BaseMeal_HotelRoomPrice']").length == 0) {
            $dvContainer_HotelRoomPrice.trigger("modeChange");
        }
    });
    //酒店房型价格的状态监测事件
    $dvContainer_HotelRoomPrice.on("modeChange", function () {
        var $btnToggleRoomPrice = $("#btnToggleRoomPrice");
        var $btnHotelRoomPrice = $("#btnHotelRoomPrice");
        //当所有的房型都没有价格期间的时候，需要将状态变更为新增
        var $btnEditHotelRoomPrice = $("#btnEditHotelRoomPrice");
        if ($dvContainer_HotelRoomPrice.find("[name='tabContent_BaseMeal_HotelRoomPrice']").length == 0) {
            $dvContainer_HotelRoomPrice.data("mode", "create");
            //$btnToggleRoomPrice.hide();
            $btnHotelRoomPrice.text("新建日期");
            $btnHotelRoomPrice.addClass("edit_button");
            $btnEditHotelRoomPrice.removeClass("edit_button");
            $btnEditHotelRoomPrice.addClass("region_disabled");
        }
        //当有一个以上房型存在价格期间的时候，需要将状态变更为修改.
        else {
            $dvContainer_HotelRoomPrice.data("mode", "modify");
            //$btnToggleRoomPrice.show();
            $btnHotelRoomPrice.text("修改日期");
            $btnHotelRoomPrice.removeClass("edit_button");
            $btnHotelRoomPrice.removeClass("region_disabled");
            $btnEditHotelRoomPrice.addClass("edit_button");
            $btnEditHotelRoomPrice.addClass("region_disabled");
        }
    });
});

/**
 * @todo 需要后台根据房间类型获取对应的基础餐型
 **/
function getRoomTypeBaseMealList(hotelRoomUuid) {

    return getObjectByProp(hotelRoomMap,"hotelRoomUuid",hotelRoomUuid).hotelMealMap;
}
//        获取pop中所有选中的房型
function getSelectedRoomTypes(pop) {
    var roomTypes = [];
    //获取所有的房间类型
    pop.find("input:checked[hotel-room-uuid!='All']").each(function () {
        var $this = $(this);
        roomTypes.push(
            getObjectByProp(hotelRoomMap,"hotelRoomUuid",$this.attr("hotel-room-uuid"))
    );
    });
    return roomTypes;
}
//        获取pop中所有添加的日期
function getRoomPriceAddedDates(pop) {
    var roomPriceDates = [];
    pop.find(".roomPriceDate").each(function () {
        var $this = $(this);
        var startDate = $this.find("[name=dtpRoomPriceStartDate]").val();
        var endDate = $this.find("[name=dtpRoomPriceEndDate]").val();
        roomPriceDates.push({
            startDate: startDate,
            endDate: endDate
        });

    });
    return roomPriceDates;
}
function setPopRoomPriceDate(existDatesList, pop) {
    pop.find(".roomPriceDate:gt(0)").remove();
    pop.find(".roomPriceDate [name='dtpRoomPriceStartDate']").val("");
    pop.find(".roomPriceDate [name='dtpRoomPriceEndDate']").val("");
    if (isSamePeriod(existDatesList)) {
        pop.data("mode", "modify");
        var existDates = existDatesList[0];
        for (var index in existDates) {
            var datePeriod = existDates[index];
            if (index > 0) {
                var rowTheme = '<tr class="roomPriceDate">';
                rowTheme += '<td class="tr"><span class="xing">*</span>日期：</td>';
                rowTheme += '<td colspan="2">';
                rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRoomPriceStartDate" readonly="readonly" />';
                rowTheme += ' 至 ';
                rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRoomPriceEndDate" readonly="readonly" />';
                rowTheme += '<i class="price_sale_house_02"></i>';
                rowTheme += '</td>';
                rowTheme += '</tr>';
                pop.find(".roomPriceDate").parent().append(rowTheme);
            }
            pop.find(".roomPriceDate:eq(" + index + ") [name='dtpRoomPriceStartDate']").val(datePeriod.startDate);
            pop.find(".roomPriceDate:eq(" + index + ") [name='dtpRoomPriceEndDate']").val(datePeriod.endDate);
        }
    } else {
        pop.data("mode", "create");
    }
    pop.trigger("modeChange");
}
function fn_PopHotelRoomPrice() {
    var html = $("#popHotelRoomPrice").html();
    //弹出窗
    var pop = $.jBox(html, {
        title: "编辑日期", buttons: { '保存': 1 }, submit: function (v, h, f) {
            if (v == "1") {
                var roomTypes = getSelectedRoomTypes(pop);
                var roomPriceDates = getRoomPriceAddedDates(pop);
                if (checkRoomPrice(roomTypes, roomPriceDates, pop)) {
                    addRoomTypePrices(roomTypes, roomPriceDates, pop);
                    $("#btnHotelRoomPrice").text("编辑日期");
                    return true;
                } else {
                    return false;
                }
            }
        }, width: 600, height: 350
    });
    pop.on("click", ".price_sale_house_01,.price_sale_house_02", function () {
        var $this = $(this);
        //添加日期
        if ($this.is("#btnAddRoomPriceDate")) {
            var rowTheme = '<tr class="roomPriceDate">';
            rowTheme += '<td class="tr"><span class="xing">*</span>日期：</td>';
            rowTheme += '<td colspan="2">';
            rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRoomPriceStartDate" readonly="readonly" />';
            rowTheme += ' 至 ';
            rowTheme += '<input type="text" onclick="WdatePicker()" class="dateinput w106 required " name="dtpRoomPriceEndDate" readonly="readonly" />';
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
    });
    pop.on("change", "[type='checkbox']", function () {
        if ($(this).is("input[checkbox-action!='All']")) {
            var $roomType = pop.find("#pop_roomPrice_roomType");
            if ($roomType.find("input[checkbox-action!='All']").length == $roomType.find("input[checkbox-action!='All']:checked").length) {
                pop.find("input[checkbox-action='All']").attr("checked", true);
            } else {
                pop.find("input[checkbox-action='All']").removeAttr("checked");
            }
        }
        var selectedRoomTypes = getSelectedRoomTypes(pop);
        var existRoomPriceDatesList = [];
        for (var index in selectedRoomTypes) {
            var hotelRoomUuid = selectedRoomTypes[index].hotelRoomUuid;
            existRoomPriceDatesList.push(getExistRoomPriceDates(hotelRoomUuid));
        }
        setPopRoomPriceDate(existRoomPriceDatesList, pop);
    });
    pop.on("modeChange", function () {
        var popMode = pop.data("mode");
        if (popMode == "create") {
            if (pop.find(".createMsg").length == 0) {
                var createMsgTheme = '<tr class="createMsg"><td></td><td  style="color:#FF0000">您选择的房型日期不一致，只能进行新增操作。</td></tr>';
                pop.find(".roomPriceDate:first").before(createMsgTheme);
            }
        } else {
            pop.find(".createMsg").remove();
        }
    });
    var $dvContainer_HotelRoomPrice = $("#dvContainer_HotelRoomPrice");
    //设置弹出窗中默认勾选的房间类型
    var $pop_roomPrice_roomType = pop.find("#pop_roomPrice_roomType");
    $pop_roomPrice_roomType.empty();
    for (var i in hotelRoomMap) {
        var roomType = hotelRoomMap[i];
        
        var popRoomTypeTheme = '<div class="hotel-style-list-out"><div class="hotel-style-sel"><input checked="checked" hotel-room-uuid="' + roomType.hotelRoomUuid + '" type="checkbox"></div><div class="hotel-style-name-sel "><div class="subwrap"><div class="content"  title="' + roomType.hotelRoomText + '"> ' + roomType.hotelRoomText + ' </div></div></div></div>';
        /*var popRoomTypeTheme = '<label class="title-lg"><input type="checkbox" style="margin: 0px" hotel-room-uuid="' + roomType.hotelRoomUuid + '" />' + roomType.hotelRoomText + '</label>';*/
        $pop_roomPrice_roomType.append(popRoomTypeTheme);
    }
    if ($dvContainer_HotelRoomPrice.data("mode") == "modify") {
        $dvContainer_HotelRoomPrice.children(":visible").each(function () {
            var content_HotelRoomPrice = $(this);
            var hotelRoomUuid = content_HotelRoomPrice.attr("hotel-room-uuid");
            pop.find("input[type='checkbox']").removeAttr("checked");
            pop.find("input[type='checkbox'][hotel-room-uuid='" + hotelRoomUuid + "']").attr("checked", true);
            pop.find("input[type='checkbox'][hotel-room-uuid='" + hotelRoomUuid + "']").trigger("change");
        });
    }
    else {
        pop.find("input[type='checkbox']").attr("checked", true);
    }
}
function getExistRoomPriceDates(hotelRoomUuid) {
    var dvContainer_HotelRoomPrice = $("#dvContainer_HotelRoomPrice");
    var $tabContent_BaseMeal_HotelRoomPrice = dvContainer_HotelRoomPrice.find("[hotel-room-uuid=" + hotelRoomUuid + "] [name='tabContent_BaseMeal_HotelRoomPrice']:first");
    var existRoomDates = [];
    $tabContent_BaseMeal_HotelRoomPrice.find("td.roomTypeDate").each(function () {
        var datePeriod = $(this).text();
        existRoomDates.push({
            startDate: datePeriod.split("~")[0],
            endDate: datePeriod.split("~")[1]
        });
    });
    return existRoomDates;
}
/**
 * 比较已经存在的 期间和pop中添加的期间
 * @param existDates 已经存在的期间
 * @param addedDates pop 添加的期间
 * @result {removePriceDateList:需要移除的,appendPriceDateList:需要添加的}
 */
function compareRoomPriceDates(existDates, addedDates) {
    var retainPriceDateList = [];
    var removePriceDateList = [];
    var appendPriceDateList = [];
    for (var i in existDates) {
        var existStartDate = existDates[i].startDate;
        var existEndDate = existDates[i].endDate;
        for (var j in addedDates) {
            var addedStartDate = addedDates[j].startDate;
            var addedEndDate = addedDates[j].endDate;
            if (existStartDate == addedStartDate && existEndDate == addedEndDate) {
                retainPriceDateList.push({
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
        for (var j in retainPriceDateList) {
            var retainStartDate = retainPriceDateList[j].startDate;
            var retainEndDate = retainPriceDateList[j].endDate;
            if (existStartDate == retainStartDate && existEndDate == retainEndDate) {
                removeChk = false;
                break;
            }
        }
        if (removeChk) {
            removePriceDateList.push({
                startDate: existStartDate,
                endDate: existEndDate
            });
        }
    }
    for (var i in addedDates) {
        var addedDate = addedDates[i].startDate;
        var addedEndDate = addedDates[i].endDate;
        var appendChk = true;
        for (var j in retainPriceDateList) {
            var retainStartDate = retainPriceDateList[j].startDate;
            var retainEndDate = retainPriceDateList[j].endDate;
            if (addedDate == retainStartDate && addedEndDate == retainEndDate) {
                appendChk = false;
                break;
            }
        }
        if (appendChk) {
            appendPriceDateList.push({
                startDate: addedDate,
                endDate: addedEndDate
            });
        }
    }
    return {
        removePriceDateList: removePriceDateList,
        appendPriceDateList: appendPriceDateList
    };
}
function checkRoomPrice(roomTypes, roomPriceDates, pop) {
    var popMode = pop.data("mode");
    var reduplicatedList = [];
    for (var i in roomPriceDates) {
        var startDate = roomPriceDates[i].startDate;
        var endDate = roomPriceDates[i].endDate;
        if (!startDate || !endDate) {
            infoBox("日期不能为空!!");
            return false;
        }
        if (endDate && startDate > endDate) {
            infoBox("开始日期不能大于结束日期!!");
            return false;
        }
    };
    var dvContainer_HotelRoomPrice = $("#dvContainer_HotelRoomPrice");
    for (var i in roomTypes) {
        var roomType = roomTypes[i];
        var existRoomDates = getExistRoomPriceDates(roomType.hotelRoomUuid);
        var allRoomDates = [];
        if (popMode == "modify") {
            allRoomDates = allRoomDates.concat(roomPriceDates);
        } else {
            allRoomDates = allRoomDates.concat(existRoomDates);
            allRoomDates = allRoomDates.concat(roomPriceDates);
        }
        var reduplicatedPeriods = [];
        checkReduplicated(allRoomDates, reduplicatedPeriods);
        if (reduplicatedPeriods.length > 0) {
            reduplicatedList.push({
                roomType: roomType,
                reduplicatedPeriods: reduplicatedPeriods
            });
        }
    }
    if (reduplicatedList.length > 0) {
        var errorString = "存在重复期间<br/>";
        for (var i in reduplicatedList) {
            var roomType = reduplicatedList[i].roomType;
            var reduplicatedPeriods = reduplicatedList[i].reduplicatedPeriods;
            errorString += "房间类型：" + roomType.hotelRoomText + "<br/>";
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
    
    if(roomTypes.length == 0) {
    	infoBox("至少选择一种房型！");
    	return false;
    }
    return true;
};
/**
 * 给每个房间类型添加一个日期行
 * @param priceDate 日期
 * @param content_HotelRoomPrice 房间类型价格日期的容器
 */
function addRoomPriceToContent(priceDate, content_HotelRoomPrice) {
    var startDate = priceDate.startDate;
    var endDate = priceDate.endDate;

    var rowTheme = "";
    rowTheme += '<tr>';
    rowTheme += '<td class="tc roomTypeDate">' + startDate + '~' + endDate + '</td>';
    var hotelRoomUuid = content_HotelRoomPrice.attr('hotel-room-uuid');
    var hotelGuestTypeList = getObjectByProp(hotelRoomMap,'hotelRoomUuid',hotelRoomUuid).hotelGuestTypeList;
    for (var i in hotelGuestTypeList) {
        rowTheme += '<td class="tc"><input uuid="" data-type="amount" price-type="0" hotel-guest-type-uuid="' + hotelGuestTypeList[i].hotelGuestTypeUuid + '" maxlength="9" class="tr amt" amt-code="' +  baseInfo.currencyMark + '" type="text" style="width: 80%"/></td>';
        rowTheme += '<td class="tc"><input uuid="" data-type="amount" price-type="1" hotel-guest-type-uuid="' + hotelGuestTypeList[i].hotelGuestTypeUuid + '" maxlength="9" class="tr amt" amt-code="' +  baseInfo.currencyMark + '" type="text" style="width: 80%"/></td>';
    }
    rowTheme += '<td class="tc">';
    rowTheme += delButtonTheme;
    rowTheme += '</td>';
    rowTheme += '</tr>';
   content_HotelRoomPrice.find("tbody[name='roomDatePriceList']").each(function(){
       var $tbody = $(this);
       $tbody.find("tr").sortedInsert(rowTheme,$tbody,function(el){
           return $(el).find("td:first").text();
       });
   });
}
function addRoomTypePrices(roomTypes, roomPriceDates, pop) {
    var $tabHeader_HotelRoomPrice = $("#tabHeader_HotelRoomPrice");
    var $dvContainer_HotelRoomPrice = $("#dvContainer_HotelRoomPrice");
    for (var i in roomTypes) {

        var hotelRoomUuid = roomTypes[i].hotelRoomUuid;
        var hotelRoomText = roomTypes[i].hotelRoomText;
        var occupancyRate = roomTypes[i].occupancyRate;
        var occupancyRateNote = getTextByAttribute(roomTypes[i].memo);
        var $tabContent_HotelRoomPrice = $dvContainer_HotelRoomPrice.find("[hotel-room-uuid='" + hotelRoomUuid + "']");
        var addedRoomPriceList = [];
        if ($tabContent_HotelRoomPrice.find("[name='tabContent_BaseMeal_HotelRoomPrice']").length == 0) {
            var baseMealMap = getRoomTypeBaseMealList(hotelRoomUuid);
            var contentTheme = "";
            contentTheme += '<div style="padding-top:20px"  name="tabContent_BaseMeal_HotelRoomPrice">';
            contentTheme += '<label class="tr" style="width: 60px">容住率：</label><span title="'+occupancyRateNote+'">'+occupancyRate+'</span>';
            contentTheme += '<span name="spRoomBaseMeal" style="margin-left: 20px">';
            contentTheme += '<span class="xing">*</span>基础餐型：';
            for (var uuid in baseMealMap) {
                var baseMealUuid = uuid;
                var baseMealText = baseMealMap[uuid];
                contentTheme += '<label  class="input_label_sm"><input type="checkbox" checked="checked" style="margin: 0px;" base-meal-uuid="' + baseMealUuid + '">' + baseMealText + '</label>';
            }
            contentTheme += '<button type="button" class="btn btn-info basemeal-allSelected" style="height: 28px" name="btnCopyRoomPrice">新增</button>';
            contentTheme += '</span>';
            contentTheme += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new">';
            contentTheme += '<thead>';
            contentTheme += '<tr>';
            contentTheme += '<th style="width: 170px;max-width: 200px;min-width: 170px" rowspan="2">日期</th>';
            var hotelGuestTypeList = getObjectByProp(hotelRoomMap,'hotelRoomUuid',hotelRoomUuid).hotelGuestTypeList;
            for (var i in hotelGuestTypeList) {
                contentTheme += '<th>' + hotelGuestTypeList[i].hotelGuestTypeName + '价</th>';
                contentTheme += '<th>' + hotelGuestTypeList[i].hotelGuestTypeName + '同行价</th>';
            }
            contentTheme += '<th width="100px">操作</th>';
            contentTheme += '</tr>';
            contentTheme += '<tr>';
            for (var i in hotelGuestTypeList) {
                contentTheme += '<th class="tc"><input data-type="amount" class="tr amt" amt-code="' +  baseInfo.currencyMark + '" type="text" style="width: 80%"/></th>';
                contentTheme += '<th  class="tc"><input  data-type="amount" class="tr amt" amt-code="' +  baseInfo.currencyMark + '" type="text" style="width: 80%"/></th>';
            }
            contentTheme += '<th class="tc"><input type="button" name="btnBatchRoomPrice" value="批量录入" class="btn btn-info" style="height: 28px" /></th>';
            contentTheme += '</tr>';
            contentTheme += '</thead>';
            contentTheme += '<tbody name="roomDatePriceList">';
            contentTheme += '</tbody>';
            contentTheme += '</table>';

            contentTheme += '</div>';
            contentTheme += '<table style="width: 100%;margin-top: 10px" name="roomTypeRemark">';
            contentTheme += '<tr>';
            contentTheme += '<td class="tr" style="width:80px;vertical-align: top;">备注：</td>';
            contentTheme += '<td>';
            contentTheme += '<textarea name="roomMemo" style="width:99%;height: 50px;" uuid=""></textarea>';
            contentTheme += '</td>';
            contentTheme += '</tr>';
            contentTheme += '</table>';
            var $contentTheme = $(contentTheme);
            $contentTheme.addClass('fixedTable')
            $tabContent_HotelRoomPrice.append($contentTheme);
            
        }
        var content_HotelRoomPrice = $dvContainer_HotelRoomPrice.find("[hotel-room-uuid=" + hotelRoomUuid + "]");
        if (pop.data("mode") == "modify") {
            var compareDate = compareRoomPriceDates(getExistRoomPriceDates(hotelRoomUuid), roomPriceDates);
            addedRoomPriceList = compareDate.appendPriceDateList;
            content_HotelRoomPrice.find("tbody[name='roomDatePriceList']").children().each(function () {
                var $this = $(this);
                var datePeriodString = $(this).find("td.roomTypeDate").text();
                for (var j in compareDate.removePriceDateList) {
                    if (datePeriodString == compareDate.removePriceDateList[j].startDate + "~" + compareDate.removePriceDateList[j].endDate) {
                        $this.remove();
                    }
                }
            });
        }
        else {
            addedRoomPriceList = roomPriceDates;
        }
        for (var j in addedRoomPriceList) {
            var priceDate = addedRoomPriceList[j];
            addRoomPriceToContent(priceDate, content_HotelRoomPrice);
        }
        $tabContent_HotelRoomPrice.find('.fixedTable table').each(function(){
        	if($(this).width()<$(this).parent().width()){
        		$(this).parent().removeClass('fixedTable');
             }
        });
    }
    tabScroll($tabHeader_HotelRoomPrice.parent(), "[hotel-room-uuid='" + roomTypes[0].hotelRoomUuid + "']");
    $dvContainer_HotelRoomPrice.trigger("modeChange");
    resetCurrency();
}

/**
 * 页面模式为编辑时,绑定数据
 * @param allRoomPrices 待绑定的所有房型价格
 */
function bindRoomTypePrices(allRoomPrices,hotelPlRoomMemoList){
    var firstHotelRoomUuid;
    for(var hotelRoomUuid in allRoomPrices){
        if(!firstHotelRoomUuid){
            firstHotelRoomUuid= hotelRoomUuid;
        }
        var roomPrices = allRoomPrices[hotelRoomUuid];
        var hotelRoom = getObjectByProp(hotelRoomMap,"hotelRoomUuid",hotelRoomUuid);
        if(hotelRoom == undefined) {
        	continue;
        }
        var hotelRoomText = hotelRoom.hotelRoomText;
        var occupancyRate = hotelRoom.occupancyRate;
        var occupancyRateNote = getTextByAttribute(hotelRoom.memo);
        var $tabContent_HotelRoomPrice = $dvContainer_HotelRoomPrice.find("[hotel-room-uuid='" + hotelRoomUuid + "']");
        var baseMealMap = getRoomTypeBaseMealList(hotelRoomUuid);

        var mealRoomPrices = groupArrayByProp(roomPrices,"hotelMealUuids");
        for(var m in mealRoomPrices){
           var mealRoomPrice = mealRoomPrices[m];
            var contentTheme = "";
            contentTheme += '<div style="padding-top:20px"  name="tabContent_BaseMeal_HotelRoomPrice">';
            contentTheme += '<label class="tr" style="width: 60px">容住率：</label><span title="'+occupancyRateNote+'">'+occupancyRate+'</span>';
            contentTheme += '<span name="spRoomBaseMeal" style="margin-left: 20px">';
            contentTheme += '<span class="xing">*</span>基础餐型：';
            for (var uuid in baseMealMap) {
                var baseMealUuid = uuid;
                var baseMealText = baseMealMap[uuid];
                contentTheme += '<label  class="input_label_sm"><input type="checkbox" checked="checked" style="margin: 0px;" base-meal-uuid="' + baseMealUuid + '">' + baseMealText + '</label>';
            }
            contentTheme += '<button type="button" class="btn btn-info basemeal-allSelected" style="height: 28px" name="btnCopyRoomPrice">新增</button>';
            contentTheme += '</span>';
            contentTheme += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new">';
            contentTheme += '<thead>';
            contentTheme += '<tr>';
            contentTheme += '<th style="width: 170px;max-width: 200px;min-width: 170px" rowspan="2">日期</th>';
            var hotelGuestTypeList = getObjectByProp(hotelRoomMap,'hotelRoomUuid',hotelRoomUuid).hotelGuestTypeList;
            for (var i in hotelGuestTypeList) {
                contentTheme += '<th>' + hotelGuestTypeList[i].hotelGuestTypeName + '价</th>';
                contentTheme += '<th>' + hotelGuestTypeList[i].hotelGuestTypeName + '同行价</th>';
            }
            contentTheme += '<th width="100px">操作</th>';
            contentTheme += '</tr>';
            contentTheme += '<tr>';
            for (var i in hotelGuestTypeList) {
                contentTheme += '<th class="tc"><input data-type="amount" maxlength="9" class="tr amt" amt-code="' + baseInfo.currencyMark + '" type="text" style="width: 80%"/></th>';
                contentTheme += '<th  class="tc"><input  data-type="amount" maxlength="9" class="tr amt" amt-code="' + baseInfo.currencyMark + '" type="text" style="width: 80%"/></th>';
            }
            contentTheme += '<th class="tc"><input type="button" name="btnBatchRoomPrice" value="批量录入" class="btn btn-info" style="height: 28px" /></th>';
            contentTheme += '</tr>';
            contentTheme += '</thead>';
            contentTheme += '<tbody name="roomDatePriceList">';
            contentTheme += '</tbody>';
            contentTheme += '</table>';
            contentTheme += '</div>';
            var $contentTheme = $(contentTheme);
            $contentTheme.find('[name="spRoomBaseMeal"] input').checkedList(function(checkbox){
                var $checkbox = $(checkbox);
                return $checkbox.attr("base-meal-uuid");
            },mealRoomPrice[0].hotelMealUuids.split(";"));

            var dayMealRoomPrices = groupArrayByProp(mealRoomPrice,"startDate");
            for(var d in dayMealRoomPrices){
                var dayMealRoomPrice = dayMealRoomPrices[d];
                var $tr =$("<tr></tr>");
                $tr.append('<td class="tc roomTypeDate">'+dayMealRoomPrice[0].startDate+'~'+dayMealRoomPrice[0].endDate+'</td>');
                var customPrices = groupArrayByProp(dayMealRoomPrice,"hotelGuestTypeUuid");
                for(var guestIndex in hotelGuestTypeList){
                    var  hotelGuestTypeUuid=hotelGuestTypeList[guestIndex].hotelGuestTypeUuid;
                    var basePrice=null;
                    var competitionPrice=null;
                    for (var c in customPrices) {
                        if(customPrices[c][0].hotelGuestTypeUuid ==hotelGuestTypeUuid )
                        {
                            basePrice=getObjectByProp(customPrices[c],"priceType","0");
                            competitionPrice = getObjectByProp(customPrices[c],"priceType","1");
                        }
                    }
                    var baseAmount="";
                    var baseUuid ="";
                    var competitionAmount="";
                    var competitionUuid="";
                    if(basePrice){
                    	if((basePrice.amount != null) || (basePrice.amount != undefined)) {
                            baseAmount=basePrice.amount;
                    	}
                        baseUuid=basePrice.uuid;
                    }
                    if(competitionPrice){
                    	if((competitionPrice.amount != null) || (competitionPrice.amount != undefined)) {
                            competitionAmount=competitionPrice.amount;
                    	}
                        competitionUuid=competitionPrice.uuid;
                    }
                    $tr.append('<td class="tc"><input data-type="amount" value="'+baseAmount+'"  uuid="'+baseUuid+'"  price-type="0" hotel-guest-type-uuid="' +hotelGuestTypeUuid + '" maxlength="9" class="tr amt" amt-code="' + baseInfo.currencyMark + '" type="text" style="width: 80%"/></td>');
                    $tr.append('<td class="tc"><input data-type="amount" value="'+competitionAmount+'" uuid="'+competitionUuid+'" maxlength="9" price-type="1" hotel-guest-type-uuid="' + hotelGuestTypeUuid + '" class="tr amt" amt-code="' + baseInfo.currencyMark + '" type="text" style="width: 80%"/></td>');
                }

                $tr.append('<td class="tc"><a class="deleteButton" href="javascript:void(0)">删除</a></td>');
                $contentTheme.find('tbody[name="roomDatePriceList"]').append($tr);
            }
            $contentTheme.addClass('fixedTable');
            $tabContent_HotelRoomPrice.append($contentTheme);

        }
        //by sy 20150729
        var hotelRoomMemo = getObjectByProp(hotelPlRoomMemoList,"hotelRoomId",hotelRoomUuid);
        var memoTheme ='';
        memoTheme += '<table style="width: 100%;margin-top: 10px" name="roomTypeRemark">';
        memoTheme += '<tr>';
        memoTheme += '<td class="tr" style="width:80px;vertical-align: top;">备注：</td>';
        memoTheme += '<td>';
        if(hotelRoomMemo != null) {
            memoTheme += '<textarea name="roomMemo" style="width:99%;height: 50px;" uuid="'+hotelRoomMemo.uuid+'">'+hotelRoomMemo.memo+'</textarea>';
        } else {
        	memoTheme += '<textarea name="roomMemo" style="width:99%;height: 50px;" uuid=""></textarea>';
        }
        memoTheme += '</td>';
        memoTheme += '</tr>';
        memoTheme += '</table>';

        $tabContent_HotelRoomPrice.append(memoTheme);
        $tabContent_HotelRoomPrice.find('.fixedTable table').each(function(){
        	if($(this).width()<$(this).parent().width()){
        		$(this).parent().removeClass('fixedTable');
             }
        });
    }
    tabScroll($tabHeader_HotelRoomPrice.parent(), "[hotel-room-uuid='" + firstHotelRoomUuid + "']");
    $dvContainer_HotelRoomPrice.trigger("modeChange");
    var $btnHotelRoomPrice = $("#btnHotelRoomPrice");
    $btnHotelRoomPrice.removeClass("edit_button");
    var $btnEditHotelRoomPrice = $("#btnEditHotelRoomPrice");
    $btnEditHotelRoomPrice.addClass("edit_button");
    var $btnSaveHotelRoomPrice = $("#btnSaveHotelRoomPrice");
    var $region = $btnSaveHotelRoomPrice.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditHotelRoomPrice.removeClass("region_disabled");
    $btnSaveHotelRoomPrice.addClass("region_disabled");
}

/**
 * 绑定价单详情
 * @param allRoomPrices
 * @param hotelPlRoomMemoList
 */
function bindDetailRoomTypePrices(allRoomPrices,hotelPlRoomMemoList){
    var firstHotelRoomUuid;
    for(var hotelRoomUuid in allRoomPrices){
        if(!firstHotelRoomUuid){
            firstHotelRoomUuid= hotelRoomUuid;
        }
        var roomPrices = allRoomPrices[hotelRoomUuid];
        var hotelRoom = getObjectByProp(hotelRoomMap,"hotelRoomUuid",hotelRoomUuid);
        var hotelRoomText = hotelRoom.hotelRoomText;
        var occupancyRate = hotelRoom.occupancyRate;
        var hotelRoomMemo = getTextByAttribute(hotelRoom.memo);
        var $tabContent_HotelRoomPrice = $dvContainer_HotelRoomPrice.find("[hotel-room-uuid='" + hotelRoomUuid + "']");
        var baseMealMap = getRoomTypeBaseMealList(hotelRoomUuid);

        var mealRoomPrices = groupArrayByProp(roomPrices,"hotelMealUuids");
        for(var m in mealRoomPrices){
           var mealRoomPrice = mealRoomPrices[m];
            var contentTheme = "";
            contentTheme += '<div style="padding-top:20px"  name="tabContent_BaseMeal_HotelRoomPrice">';
            contentTheme += '<label class="tr" style="width: 60px">容住率：</label><span title="'+hotelRoomMemo+'">'+occupancyRate+'</span>';
            contentTheme += '<span name="spRoomBaseMeal" style="margin-left: 10px">';
            contentTheme += '餐型：';
            
            var hotelMealText = '';
            var checkBaseMeals = mealRoomPrice[0].hotelMealUuids.split(";");
            for(var i=0; i<checkBaseMeals.length; i++) {
            	var baseMealUuid = checkBaseMeals[i];
            	var baseMealText = baseMealMap[baseMealUuid];
                hotelMealText += '<label  class="input_label_sm">' + baseMealText + '</label>';
                hotelMealText += '、';
            }
            
            if(hotelMealText != '') {
            	contentTheme = contentTheme + hotelMealText.substring(0, hotelMealText.length-1);
            }
            contentTheme += '</span>';
            contentTheme += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new">';
            contentTheme += '<thead>';
            contentTheme += '<tr>';
            contentTheme += '<th style="width: 170px;max-width: 200px;min-width: 170px" rowspan="2">日期</th>';
            var hotelGuestTypeList = getObjectByProp(hotelRoomMap,'hotelRoomUuid',hotelRoomUuid).hotelGuestTypeList;
            for (var i in hotelGuestTypeList) {
                contentTheme += '<th>' + hotelGuestTypeList[i].hotelGuestTypeName + '价</th>';
                contentTheme += '<th>' + hotelGuestTypeList[i].hotelGuestTypeName + '同行价</th>';
            }
            contentTheme += '</tr>';
            contentTheme += '</thead>';
            contentTheme += '<tbody name="roomDatePriceList">';
            contentTheme += '</tbody>';
            contentTheme += '</table>';
            contentTheme += '</div>';
            var $contentTheme = $(contentTheme);
            $contentTheme.find('[name="spRoomBaseMeal"] input').checkedList(function(checkbox){
                var $checkbox = $(checkbox);
                return $checkbox.attr("base-meal-uuid");
            },mealRoomPrice[0].hotelMealUuids.split(";"));

            var dayMealRoomPrices = groupArrayByProp(mealRoomPrice,"startDate");
            for(var d in dayMealRoomPrices){
                var dayMealRoomPrice = dayMealRoomPrices[d];
                var $tr =$("<tr></tr>");
                $tr.append('<td class="tc roomTypeDate">'+dayMealRoomPrice[0].startDate+'~'+dayMealRoomPrice[0].endDate+'</td>');
                var customPrices = groupArrayByProp(dayMealRoomPrice,"hotelGuestTypeUuid");
                for(var guestIndex in hotelGuestTypeList){
                    var  hotelGuestTypeUuid=hotelGuestTypeList[guestIndex].hotelGuestTypeUuid;
                    var basePrice=null;
                    var competitionPrice=null;
                    for (var c in customPrices) {
                        if(customPrices[c][0].hotelGuestTypeUuid ==hotelGuestTypeUuid )
                        {
                            basePrice=getObjectByProp(customPrices[c],"priceType","0");
                            competitionPrice = getObjectByProp(customPrices[c],"priceType","1");
                        }
                    }
                    var baseAmount="";
                    var baseUuid ="";
                    var competitionAmount="";
                    var competitionUuid="";
                    if(basePrice){
                    	if((basePrice.amount != null) || (basePrice.amount != undefined)) {
                            baseAmount=basePrice.amount;
                    	}
                        baseUuid=basePrice.uuid;
                    }
                    if(competitionPrice){
                    	if((competitionPrice.amount != null) || (competitionPrice.amount != undefined)) {
                            competitionAmount=competitionPrice.amount;
                    	}
                        competitionUuid=competitionPrice.uuid;
                    }
                    $tr.append('<td class="tc">' + baseInfo.currencyMark + formatCurrency(baseAmount)+'</td>');
                    $tr.append('<td class="tc">' + baseInfo.currencyMark + formatCurrency(competitionAmount)+'</td>');
                }

                $contentTheme.find('tbody[name="roomDatePriceList"]').append($tr);
            }
            $contentTheme.addClass('fixedTable');
            $tabContent_HotelRoomPrice.append($contentTheme);
           
        }
        //by sy 20150729
        var hotelRoomMemo = getObjectByProp(hotelPlRoomMemoList,"hotelRoomId",hotelRoomUuid);
        var memoTheme ='';
        memoTheme += '<div class="note_msg_bg_tb">';
        memoTheme += '<label class="note_msg_labs">备注：</label>';
        hotelRoomMemo.memo = hotelRoomMemo.memo.replace(/\n/g,"<br/>");
        if(hotelRoomMemo != null) {
            memoTheme += '<div class="note_msgs">'+hotelRoomMemo.memo+'</div>';
        } else {
            memoTheme += '<div class="note_msgs"></div>';
        }
        memoTheme += '</div>';

        $tabContent_HotelRoomPrice.append(memoTheme);
        $tabContent_HotelRoomPrice.find('.fixedTable table').each(function(){
        	if($(this).width()<$(this).parent().width()){
        		$(this).parent().removeClass('fixedTable');
             }
        });
    }
    tabScroll($tabHeader_HotelRoomPrice.parent(), "[hotel-room-uuid='" + firstHotelRoomUuid + "']");
    $dvContainer_HotelRoomPrice.trigger("modeChange");
    var $btnHotelRoomPrice = $("#btnHotelRoomPrice");
    $btnHotelRoomPrice.removeClass("edit_button");
    var $btnEditHotelRoomPrice = $("#btnEditHotelRoomPrice");
    $btnEditHotelRoomPrice.addClass("edit_button");
    var $btnSaveHotelRoomPrice = $("#btnSaveHotelRoomPrice");
    var $region = $btnSaveHotelRoomPrice.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditHotelRoomPrice.removeClass("region_disabled");
    $btnSaveHotelRoomPrice.addClass("region_disabled");
}

/**
 * 保存酒店房型价格数据
 */
$(document).on("click", "#btnSaveHotelRoomPrice", function (e) {
	//添加校验
	var flag = true;
	var hotelRoomUuid = '';
	//基础餐型不能为空
	$dvContainer_HotelRoomPrice.find('[name="tabContent_HotelRoomPrice"]').each(function(){
		var checkFlag = false;
		if($(this).find('table').size() == 0) {
			var checkFlag = true;
		}
		
		$(this).find('input[type=checkbox]').each(function(){
			if ($(this).attr("checked")) {
				checkFlag = true;
				return false;
			}
		});
		
		if(!checkFlag) {
			flag = false;
			hotelRoomUuid = $(this).attr("hotel-room-uuid");
			return false;
		}
	});
	
	if(!flag) {
		var hotelRoomText = getObjectByProp(hotelRoomMap,"hotelRoomUuid",hotelRoomUuid).hotelRoomText;
		$.jBox.tip(hotelRoomText + "的基础餐型最少选择一个！");
		$('#tabHeader_HotelRoomPrice').find('li').each(function(){
			if($(this).attr('hotel-room-uuid') == hotelRoomUuid) {
				$(this).click();
			}
		});
		return false;
	}
	
	var status = 1;
	//当updateFlag为1或相应模块的保存状态为1时
	if($("#updateFlag").val() == "2" || $("#hotelHotelRoomPriceSaveFlag").val() == "2") {
		status = 2;
	} else {
		$("#hotelHotelRoomPriceSaveFlag").val("2");
	}
	
	var url = ctx + "/hotelPl/saveHotelPlPrice";
	var hotelPlPriceJsonData = JSON.stringify(getSaveRoomTypePrice());
	var roomMemoJsonData = JSON.stringify(getSaveRoomMemo());
	ajaxStart();
	$.ajax({
		type: "POST",
	   	url: url,
	   	async: false,
	   	data: {
				"hotelPlPriceJsonData":hotelPlPriceJsonData,
				"roomMemoJsonData":roomMemoJsonData,
				"status":status,
				"hotelPlUuid":baseInfo.uuid,
				"mixliveCurrencyId":baseInfo.currencyId,
				"mixliveAmount":$("#txtMixedPrice").val()
			  },
		dataType: "json",
	   	success: function(data){
	   		ajaxStop();
	   		if(data){
	   			if(data.result == 1) {
	   				$.jBox.tip("酒店房型价格保存成功！");
	   			} else if(data.result == 2) {
	   				$.jBox.tip("酒店房型价格更新成功！");
	   			} else if(data.result == 3) {
	   				$.jBox.tip(data.message);
	   			}
	   		}
	   	}
	});

    saveButtonCli($(this));
});

/**
 * 获取保存的数据
 * @returns {Array}
 */
function getSaveRoomTypePrice(){
    var allRoomPrices=[];
    $dvContainer_HotelRoomPrice.find('[name="tabContent_HotelRoomPrice"]').each(function(){
        var $tabContent_HotelRoomPrice =$(this);
        var hotelRoomUuid = $tabContent_HotelRoomPrice.attr('hotel-room-uuid');
        var hotelRoomText = getObjectByProp(hotelRoomMap,"hotelRoomUuid",hotelRoomUuid).hotelRoomText;
        $tabContent_HotelRoomPrice.find('[name="tabContent_BaseMeal_HotelRoomPrice"]').each(function(){
            var $tabContent_BaseMeal_HotelRoomPrice = $(this);
            var hotelMealUuids = $tabContent_BaseMeal_HotelRoomPrice.find('[name="spRoomBaseMeal"] input[type="checkbox"]').checkedList(function(checkbox){
                return $(checkbox).attr('base-meal-uuid');
            }).toString();
            
            $tabContent_BaseMeal_HotelRoomPrice.find('tbody[name="roomDatePriceList"] tr').each(function(){
                var $tr =$(this);
                var period = $tr.find('td:first').text();
                var startDate = period.split("~")[0];
                var endData =  period.split("~")[1];
                var hotelGuestTypeList = getObjectByProp(hotelRoomMap,'hotelRoomUuid',hotelRoomUuid).hotelGuestTypeList;
                for(var i in hotelGuestTypeList){
                    var basePrice = $tr.find('[hotel-guest-type-uuid="'+hotelGuestTypeList[i].hotelGuestTypeUuid+'"][price-type="0"]');
                    var competitionPrice = $tr.find('[hotel-guest-type-uuid="'+hotelGuestTypeList[i].hotelGuestTypeUuid+'"][price-type="1"]');
                    allRoomPrices.push({
                        "amount": basePrice.val(),
                        "currencyId": baseInfo.currencyId,
                        "currencyMark":  baseInfo.currencyMark,
                        "endDate": endData,
                        "endDateString": endData,
                        "hotelGuestTypeUuid":hotelGuestTypeList[i].hotelGuestTypeUuid,
                        "hotelMealUuids":hotelMealUuids,
                        "hotelPlUuid": baseInfo.uuid,
                        "hotelRoomText": hotelRoomText,
                        "hotelRoomUuid": hotelRoomUuid,
                        "priceType": 0,
                        "startDate": startDate,
                        "startDateString": startDate,
                        "uuid": basePrice.attr("uuid"),
                    });
                    allRoomPrices.push({
                        "amount": competitionPrice.val(),
                        "currencyId": baseInfo.currencyId,
                        "currencyMark":  baseInfo.currencyMark,
                        "endDate": endData,
                        "endDateString": endData,
                        "hotelGuestTypeUuid":hotelGuestTypeList[i].hotelGuestTypeUuid,
                        "hotelMealUuids":hotelMealUuids,
                        "hotelPlUuid": baseInfo.uuid,
                        "hotelRoomText": hotelRoomText,
                        "hotelRoomUuid": hotelRoomUuid,
                        "priceType": 1,
                        "startDate": startDate,
                        "startDateString": startDate,
                        "uuid": competitionPrice.attr("uuid")
                    });
                }
            });
        });
    });
    return allRoomPrices;
}

function getSaveRoomMemo(){
    var allRoomMemo=[];
    $dvContainer_HotelRoomPrice.find('[name="tabContent_HotelRoomPrice"]').each(function(){
        var $tabContent_HotelRoomPrice =$(this);
        var hotelRoomUuid = $tabContent_HotelRoomPrice.attr('hotel-room-uuid');
        var hotelRoomText = getObjectByProp(hotelRoomMap,"hotelRoomUuid",hotelRoomUuid).hotelRoomText;
        var $roomMemo = $tabContent_HotelRoomPrice.find('[name="roomMemo"]');
        if($roomMemo.length>0){
            allRoomMemo.push({
                "hotelPlUuid":baseInfo.uuid ,
                "hotelRoomId": hotelRoomUuid,
                "memo": $roomMemo.val(),
                "uuid":$roomMemo.attr("uuid")
            });
        }
    });
    return allRoomMemo;
}