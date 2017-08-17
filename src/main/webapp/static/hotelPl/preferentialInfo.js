$(function () {
    //操作浮框
    operateHandler();
    if (!updateFlag) {
        $("#btnEditPromotion").remove();
        $("#btnAddPromotion").css("display", "inline-block");
    }
});

//优惠模板集合json
var templatesesJson;

var relevancyFavors = { length: 0, favors: {} },// 可关联的优惠
	favorInfos = {},// 优惠信息列表
	addFavor_box,
	hotelUuid; // 当前酒店ID

$(document).ready(function () {
   
    templatesesJson = jQuery.parseJSON($("#templatesesJson").val());
    // 优惠信息tab页
    addFavor_box = (function () {

    	//初始化页面事件
        function initEvent(options) {
            hotelUuid = baseInfo.hotelUuid;
            
            // 关联房型及餐型对象 根据关联酒店改变
            var relevancyHouseTypes = houseTypes,
            	$table = $("#jbox_yh .jbox-content table:first");
            var i = 0;

            $table.find("tr.trafficTr input:checkbox").attr("data-group", buildID());
            $table.find("tr.relevancyTrafficTr input:checkbox").attr("data-group", buildID());
            $table.find("input[data-group='ydDate']").attr("data-group", buildID());
            $table.find("input[data-group='rzDate']").attr("data-group", buildID());
            $table.find("input[data-group='dtpRoomPriceDate']").attr("data-group", buildID());

            $table.on('click', 'tr.applyHouseTypeTr i.price_sale_house_01', function () {
                // 添加房型
                var $applyHouseTypeTr = $table.find("tr.applyHouseTypeTr:first").clone();
                $applyHouseTypeTr.find('td:first').hide();
                $applyHouseTypeTr.find('i.price_sale_house_01').removeClass('price_sale_house_01').addClass('price_sale_house_02');
                $applyHouseTypeTr.find('input:text').val('');

                $applyHouseTypeTr.find("input:checkbox").attr("data-group", buildID());
                $table.find("tr.applyHouseTypeTr:last").after($applyHouseTypeTr);
                $table.find("tr.applyHouseTypeTr:first td:first").attr("rowspan", $table.find("tr.applyHouseTypeTr").length);
                $applyHouseTypeTr.find("select").change();
            }).on('change', 'tr.applyHouseTypeTr select', function () {
                // 房型改变
                var $td = $table.find("tr.applyHouseTypeTr").has(this).find("td:eq(3)");
                var group = ($td.find("input:checkbox").attr("data-group") || buildID());
                
                if($(this).val() != "" && $(this).val() != null) {
                    var mealTypes = houseTypes[$(this).val()].hotelRoomMeals;
                    var html = ['<label style="margin-right: 10px"> <input type="checkbox" style="margin: 0px" class="chkAll" data-group="', group, '">全部</label>'];
                    for(var i=0; i<mealTypes.length; i++) {
                    	html.push('<label style="margin-right: 10px"><input type="checkbox" style="margin: 0px" value="', mealTypes[i].hotelMealUuid, '" data-text="', mealTypes[i].hotelMealName, '" data-group="', group, '">', mealTypes[i].hotelMealName, '</label>');
                    }
                    $td.html(html.join(''));
                    selectedMealTypeChanged();
                } else {
                	$td.html('');
                }
                
                $(this).attr("title", $(this).find("option:selected").text());
            }).on('click', 'tr.applyHouseTypeTr i.price_sale_house_02', function () {
                // 删除房型
                $table.find("tr.applyHouseTypeTr").has(this).remove();
                $table.find("tr.applyHouseTypeTr:first td:first").attr("rowspan", $table.find("tr.applyHouseTypeTr").length);
                selectedMealTypeChanged();
            }).on('click', 'tr.relevancyHouseTypeTr i.price_sale_house_01', function () {
                // 添加关联房型
                var $relevancyHouseTypeTr = $table.find("tr.relevancyHouseTypeTr:first").clone().show();
                $relevancyHouseTypeTr.find('td:first').hide();
                $relevancyHouseTypeTr.find('i.price_sale_house_01').removeClass('price_sale_house_01').addClass('price_sale_house_02');
                $relevancyHouseTypeTr.find('input:text').val('');

                $relevancyHouseTypeTr.find("input:checkbox").attr("data-group", buildID());
                $table.find("tr.relevancyHouseTypeTr:last").after($relevancyHouseTypeTr);
                $table.find("tr.relevancyHouseTypeTr:first td:first").attr("rowspan", $table.find("tr.relevancyHouseTypeTr").length);
                $relevancyHouseTypeTr.find("select").change();
            }).on('click', 'tr.relevancyHouseTypeTr i.price_sale_house_02', function () {
                // 删除关联房型
                $table.find("tr.relevancyHouseTypeTr").has(this).remove();
                $table.find("tr.relevancyHouseTypeTr:first td:first").attr("rowspan", $table.find("tr.relevancyHouseTypeTr").length);
            }).on('change', 'tr.relevancyHotelTr input:checkbox', function () {
                // 关联酒店
                var checked = $(this).prop("checked");
                $table.find('tr.relevancyHotelTr select').prop({ "disabled": !checked });
                $table.find("tr.relevancyHouseTypeTr")[checked ? "show" : "hide"]();
                $table.find("tr.relevancyTrafficTr")[checked ? "show" : "hide"]();
                checked && $table.find('tr.relevancyHotelTr select').change();
            }).on('change', 'tr.relevancyHotelTr select', function () {
                // 关联酒店改变 临时值
                $.ajax({
            		type: "POST",
            	   	url: ctx + "/hotelPl/getHouseTypesInfo",
            	   	async: false,
            	   	data: {
            				"hotelUuid":$(this).val()
            			  },
            		dataType: "json",
            	   	success: function(data){
            	   		if(data){
            	   			 relevancyHouseTypes = $.parseJSON(data.houseTypes);
	            	   		 var html = [];
	            	   		 html.push('<option value="">请选择</option>');
	                         for (var k in relevancyHouseTypes) {
	                             html.push('<option value="', k, '" data-accommodate="', relevancyHouseTypes[k].occupancyRate, '">', relevancyHouseTypes[k].roomName, '</option>');
	                         }
	                         $table.find("tr.relevancyHouseTypeTr select").html(html.join('')).change();
            	   		}
            	   	}
            	});
                
                $(this).attr("title", $(this).find("option:selected").text());
            }).on('change', 'tr.relevancyHouseTypeTr select', function () {
                // 关联酒店房型改变
                var $td = $table.find("tr.relevancyHouseTypeTr").has(this).find("td:eq(3)");
                var group = ($td.find("input:checkbox").attr("data-group") || buildID());
                if($(this).val() != "" && $(this).val() != null) {
                    var mealTypes = relevancyHouseTypes[$(this).val()].hotelRoomMeals;
                    var html = ['<label style="margin-right: 10px"> <input type="checkbox" style="margin: 0px" class="chkAll" data-group="', group, '">全部</label>'];
                    for(var i=0; i<mealTypes.length; i++) {
                    	html.push('<label style="margin-right: 10px"><input type="checkbox" style="margin: 0px" value="', mealTypes[i].hotelMealUuid, '" data-text="', mealTypes[i].hotelMealName, '" data-group="', group, '">', mealTypes[i].hotelMealName, '</label>');
                    }
                    $td.html(html.join(''));
                } else {
                	$td.html('');
                }
                
                $(this).attr("title", $(this).find("option:selected").text());
            }).on('change', 'select.favorType', function () {
            	
            	// 优惠类型改变
                var $this = $(this);
                var favorType = $(this).find("option:selected").attr("data-favorType");
                var html = '';
                if(favorType == "2") {
                	html += '<span class="xing">*</span>';
                }
                html += getFavorHtml($this.val());
                
                $this.parentsUntil("tr").parent().next().find("td").html(html);
                
                $this.parentsUntil("tr").parent().next().find("td").find("input[class=currency]").attr("amt-code", baseInfo.currencyMark);
                $this.parentsUntil("tr").parent().next().find("td").find("input[class=currency]").addClass("amt");
                resetCurrency();
                
                var $houseSels = $table.find("table.houseChargeTable select");
                var $mealSels = $table.find("table.mealChargeTable select");
                var $trafficSels = $table.find("table.trafficChargeTable select");
                switch (favorType) {
                    case "1":
                        $table.find("tr.chargeTr > td:eq(0)").show();
                        $table.find("tr.chargeTr > td:eq(1)").hide();
                        showSelectItems($houseSels, [0, 4]);
                        showSelectItems($mealSels, [0, 1]);
                        showSelectItems($trafficSels, [0, 1, 2, 3]);
                        break;
                    case "2":
                        $table.find("tr.chargeTr > td:eq(0)").show();
                        $table.find("tr.chargeTr > td:eq(1)").hide();
                        showSelectItems($houseSels, [0, 2, 3]);
                        showSelectItems($mealSels, [0, 1]);
                        showSelectItems($trafficSels, [0, 1, 2, 3]);
                        break;
                    case "3":
                        $table.find("tr.chargeTr > td:eq(0)").show();
                        $table.find("tr.chargeTr > td:eq(1)").hide();
                        $table.find("tr.chargeTr > td:eq(0)").hide();
                        $table.find("tr.chargeTr > td:eq(1)").show();
                        break;
                    case "4":
                        $table.find("tr.chargeTr > td:eq(0)").show();
                        $table.find("tr.chargeTr > td:eq(1)").hide();
                        showSelectItems($houseSels, [0, 2, 3]);
                        showSelectItems($mealSels, [0, 1]);
                        showSelectItems($trafficSels, [0, 1, 2, 3]);
                        break;
                }
            }).on('click', 'td.NAHouseTypeTd i.price_sale_house_01', function () {
                // 添加不适用房型
            	i++;
                var $span = $(this).parent().clone();
                $span.find("i").removeClass("price_sale_house_01").addClass("price_sale_house_02");
                $table.find("td.NAHouseTypeTd").append($span);
            }).on('click', 'td.NAHouseTypeTd i.price_sale_house_02', function () {
                // 删除不适用房型
                $(this).parent().remove();
            }).on('click', 'td.NADateTd i.price_sale_house_01', function () {
            	 // 添加不适用日期
                var $span = $(this).parent().clone();
                // var $span=$(this).parent().parent().find("span:last").clone();
                // $span.find("input[name='dtpRoomPriceStartDate']").attr("data-min",$(this).parent().parent().find("span:last").find("input[name='dtpRoomPriceStartDate']").attr("data-max"));
                // $span.find("input[name='dtpRoomPriceStartDate']").attr("data-max","");
                $span.find("input[name='dtpRoomPriceStartDate']").removeAttr("data-max");
                $span.find("input[name='dtpRoomPriceEndDate']").removeAttr("data-min");
                $span.find("input").attr("data-group", "dtpRoomPriceDate" + i);
                $span.find("i").removeClass("price_sale_house_01").addClass("price_sale_house_02");
                $span.find('input:text').val('');
                $table.find("td.NADateTd").append($span);
                $table.find("input[data-group='dtpRoomPriceDate" + i + "']").attr("data-group", buildID());
            }).on('click', 'td.NADateTd i.price_sale_house_02', function () {
                // 删除不适用日期
                $(this).parent().remove();
            }).on('click', 'div.lib_Menubox li', function () {
                var $this = $(this);
                var index = $this.index();
                $this.siblings().removeClass('hover');
                $this.addClass('hover');
                var $child = $table.find("div.lib_Menubox").has(this).next().children().eq(index);
                $child.show();
                $child.siblings().hide();
            }).on("change", 'tr.applyHouseTypeTr input:checkbox', function () {
                //选择基础餐型
                if ($(this).is(".chkAll")) {
                    setTimeout(selectedMealTypeChanged, 10);
                } else {
                    selectedMealTypeChanged();
                }
            }).on("change", 'tr.trafficTr input:checkbox', function () {
                //选择交通
                if ($(this).is(".chkAll")) {
                    setTimeout(selectedTrafficChanged, 10);
                } else {
                    selectedTrafficChanged();
                }
            }).on('change', 'div.lib_Contentbox select', function () {
                // 点击tab页签
                var $this = $(this);
                var val = $this.val();
                if (val == "0" || val == "4") {
                    $this.siblings().hide();
                } else {
                    $this.siblings().show();
                }
                
                var dataUnitid = $this.find("option:selected").attr("data-unitid");
                if(dataUnitid == 1) {
                	$this.siblings('span.currencySpan').find("em").text("");
                	$this.siblings('span.sign').text($this.find("option:selected").attr("data-unit"));
                } else if(dataUnitid == 2) {
                	$this.siblings('span.currencySpan').find("em").text(baseInfo.currencyMark);
                	$this.siblings('span.sign').text("");
                }
            }).on('click', 'a.resetLink', function () {
                // 重置
                var $tr = $(this).parentsUntil('tr').parent();
                $tr.find("select").prop("selectedIndex", 0).change();
                $tr.find("input:checkbox").prop("checked", false);
                $tr.find("input:text").val("");
            });
            // 选择的基础餐型改变
            function selectedMealTypeChanged() {
                var $tds = $table.find("table.mealChargeTable > tbody > tr > td:nth-child(2)");
                var $mealTypes = $tds.first().find("input:checkbox");
                var selectedMealTypes = [true];
                $table.find("tr.applyHouseTypeTr input:checkbox:not(.chkAll):checked").each(function () {
                    var val = $(this).val();
                    var existed = false;
                    $.each($mealTypes, function (i) {
                        if (val == $(this).val()) {
                            selectedMealTypes[i] = true;
                            existed = true;
                            return false;
                        }
                    });
                    if (!existed) {
                        var text = $(this).attr("data-text");
                        $tds.append('<span><input type="checkbox" style="margin: 0px" data-text="' + text + '" value="' + val + '" />' + text + '</span>');
                    }
                });
                for (var i = $mealTypes.length; i--;) {
                    if (!selectedMealTypes[i]) {
                        $tds.find("span:nth-child(" + (i + 1) + ")").remove();
                    }
                }

                $.each($tds, function () {
                    var group = $(this).find("input:checkbox:first").attr("data-group");
                    $(this).find("input:checkbox").attr("data-group", group);
                    $(this).find("input:checkbox:last").change();
                });
            }
            // 选择交通改变
            function selectedTrafficChanged() {
                var $tds = $table.find("table.trafficChargeTable > tbody > tr > td:nth-child(2)");
                var $traffics = $tds.first().find("input:checkbox");
                var selectedtraffics = [true];
                $table.find("tr.trafficTr input:checkbox:not(.chkAll):checked").each(function () {
                    var val = $(this).val();
                    var existed = false;
                    $.each($traffics, function (i) {
                        if (val == $(this).val()) {
                            selectedtraffics[i] = true;
                            existed = true;
                            return false;
                        }
                    });
                    if (!existed) {
                        var text = $(this).attr("data-text");
                        $tds.append('<span><input type="checkbox" style="margin: 0px" data-text="' + text + '" value="' + val + '" />' + text + '</span>');
                    }
                });
                for (var i = $traffics.length; i--;) {
                    if (!selectedtraffics[i]) {
                        $tds.find("span:nth-child(" + (i + 1) + ")").remove();
                    }
                }

                $.each($tds, function () {
                    var group = $(this).find("input:checkbox:first").attr("data-group");
                    $(this).find("input:checkbox").attr("data-group", group);
                    $(this).find("input:checkbox:last").change();
                });
            }
            $table.find("select.favorType").change();
            $table.find("#txtGroupPrice").addClass("amt").attr("amt-code", baseInfo.currencyMark);
            resetCurrency();
            //初始化房型
            var html = [];
            for (var k in houseTypes) {
                html.push('<option value="', k, '" data-accommodate="', houseTypes[k].occupancyRate, '">', houseTypes[k].roomName, '</option>');
            }
            /*html.unshift('<option value="">不限</option>');*/
            $table.find("tr.applyHouseTypeTr:first select:first").html(html.join('')).change();
            $table.find('td.NAHouseTypeTd select').html(html.join(''))
            
            if (relevancyFavors.length) {
                var id = options.isAdd ? "" : options.uuid;
                var html = [];
                for (var k in relevancyFavors.favors) {
                    if (k != id) {
                        html.push('<label style="width: 160px"> <input type="checkbox" value="', k, '" style="margin: 0px" data-text="', relevancyFavors.favors[k], '">', relevancyFavors.favors[k], '</label>');
                    }
                }
                $table.find("tr.preferentialRel td:eq(1)").html(html.join(''));
                if ($table.find("tr.preferentialRel input:checkbox").length) {
                    $table.on('change', "input:radio[name='allowSuperposed']", function () {
                        // 是否允许优惠叠加
                        var checked = $table.find("input:radio[name='allowSuperposed']:first").prop("checked");
                        $table.find("tr.preferentialRel")[checked ? "show" : "hide"]();
                    });
                    $table.find("input:radio[name='allowSuperposed']").change();
                }
            }
            
        }
        
        function showSelectItems($sel, values) {
            var items = {
                "0": '<option value="0">请选择</option>',
                "1": '<option value="1" data-unitid="2" data-unit="'+baseInfo.currencyMark+'">合计</option>',
                "2": '<option value="2" data-unitid="1" data-unit="%">打折</option>',
                "3": '<option value="3" data-unitid="2" data-unit="'+baseInfo.currencyMark+'">减金额</option>',
                "4": '<option value="4">减最低</option>'
            };
            var val = $sel.val();
            $sel.empty();
            if (values) {
                for (var i = 0, l = values.length; i < l; i++) {
                    $sel.append(items[values[i]]);
                }
                $sel.val("0").change();
            }
        }


        //初始化价单优惠信息
        function initData(data) {
            if (!data) return;
            
            var $dataTrs = $("#jbox_yh .jbox-content table:first > tbody > tr");
            var rIndex = 0;
            // 优惠名称
            $dataTrs.eq(rIndex).find("input").val(data.preferentialName);
            rIndex++;
            // 下单代码
            $dataTrs.eq(rIndex).find("input").val(data.bookingCode);
            rIndex++;
            // 入住日期
            $dataTrs.eq(rIndex).find("input:eq(0)").val(data.inDateString);
            $dataTrs.eq(rIndex).find("input:eq(1)").val(data.outDateString);
            // 预定日期
            $dataTrs.eq(rIndex).find("input:eq(2)").val(data.bookingStartDateString);
            $dataTrs.eq(rIndex).find("input:eq(3)").val(data.bookingEndDateString);
            rIndex++;
            // 适用房型
            var roomList = $.grep(data.preferentialRoomList, function (room) {
                return room.hotelUuid == hotelUuid;
            });
            $.each(roomList, function (i) {
                if (i > 0) {
                    $("#jbox-content tr.applyHouseTypeTr i.price_sale_house_01").click();
                    $dataTrs = $("#jbox-content table:first > tbody > tr")
                }
                $dataTrs.eq(rIndex).find("select").val(this.hotelRoomUuid).change();
                $dataTrs.eq(rIndex).find("input:text").val(this.nights);
                // 基础餐型
                this.hotelMealList && $.each(this.hotelMealList, function () {
                    $dataTrs.eq(rIndex).find("input:checkbox[data-text='" + this.mealName + "']").prop("checked", true);
                });
                $dataTrs.eq(rIndex).find("input:checkbox:last").change();
                rIndex++;
            });
            // 交通
            data.islandWayList && $.each(data.islandWayList, function () {
                $dataTrs.eq(rIndex).find("input:checkbox[data-text='" + this.label + "']").prop("checked", true);
            });
            $dataTrs.eq(rIndex).find("input:checkbox:last").change();
            rIndex++;
            if (data.isRelation == 1) {
                // 关联酒店
                $dataTrs.eq(rIndex).find("input:checkbox").prop("checked", true).change();
                $dataTrs.eq(rIndex).find("select").val(data.hotelPlPreferentialRelHotel.hotelUuid).change();
                rIndex++;
                // 关联适用房型
                roomList = $.grep(data.preferentialRoomList, function (room) {
                    return room.hotelUuid == data.hotelPlPreferentialRelHotel.hotelUuid;
                });
                $.each(roomList, function (i) {
                    if (i > 0) {
                        $("#jbox-content tr.relevancyHouseTypeTr i.price_sale_house_01").click();
                        $dataTrs = $("#jbox-content table:first > tbody > tr")
                    }
                    $dataTrs.eq(rIndex).find("select").val(this.hotelRoomUuid).change();
                    $dataTrs.eq(rIndex).find("input:text").val(this.nights);
                    // 关联基础餐型
                    this.hotelMealList && $.each(this.hotelMealList, function () {
                        $dataTrs.eq(rIndex).find("input:checkbox[data-text='" + this.mealName + "']").prop("checked", true);
                    });
                    $dataTrs.eq(rIndex).find("input:checkbox:last").change();
                    rIndex++;
                });
                // 关联交通
                data.hotelPlPreferentialRelHotel.islandWayList && $.each(data.hotelPlPreferentialRelHotel.islandWayList, function () {
                    $dataTrs.eq(rIndex).find("input:checkbox[data-text='" + this.label + "']").prop("checked", true);
                });
                $dataTrs.eq(rIndex).find("input:checkbox:last").change();
                rIndex++;
            } else {
                rIndex += $dataTrs.filter("tr.relevancyHouseTypeTr").length + 2;
            }
            // 优惠事项  具体的优惠信息需要开发提供接口
            $dataTrs.eq(rIndex).find("select.favorType").val(data.matter.preferentialTemplatesUuid).change();
            
            //优惠事项输入信息
            data.matter.matterValues &&  $.each(data.matter.matterValues, function () {
                $dataTrs.eq(rIndex).find(".favorTypeOutHtml").find("input[name='" + this.myKey + "']").val(this.myValue);
            });
            
            
            $dataTrs.eq(rIndex).find("textarea").val(data.matter.memo);
            if (data.matter.type == "3") {
                // 打包价格优惠
                if (data.matter.preferentialTaxMap["4"]) {
                    var istax = data.matter.preferentialTaxMap["4"][0].istax;
                    if (istax) {
                        var $td = $dataTrs.eq(rIndex).find("tr.chargeTr > td:eq(1)");
                        $.each(istax.split(";"), function () {
                            $td.find("input:checkbox[value='" + this + "']").prop("checked", true).change();
                        });
                    }
                }
            } else {
                // 房费
                if (data.matter.preferentialTaxMap["1"]) {
                    var $trs = $dataTrs.eq(rIndex).find("table.houseChargeTable > tbody > tr");
                    $.each(data.matter.preferentialTaxMap["1"], function (i) {
                        var $tds = $trs.eq(i).find("td");
                        $tds.eq(1).find("select").val(this.preferentialType).change();
                        $tds.eq(1).find("input").val(this.preferentialAmount);
                        $.each(this.istax.split(";"), function () {
                            $tds.eq(2).find("input:checkbox[value='" + this + "']").prop("checked", true).change();
                        });
                    });
                }
                var $div;
                // 餐费
                if (data.matter.preferentialTaxMap["2"]) {
                	$div = $dataTrs.eq(rIndex).find("table.mealChargeTable").parent().show();
                    var $trs = $dataTrs.eq(rIndex).find("table.mealChargeTable > tbody > tr");
                    $.each(data.matter.preferentialTaxMap["2"], function (i) {
                        var $tds = $trs.eq(i).find("td");
                        $.each(this.hotelMealUuids.split(";"), function () {
                            $tds.eq(1).find("input:checkbox[value='" + this + "']").prop("checked", true);
                        });
                        $tds.eq(1).find("input:checkbox:last").change();
                        $tds.eq(2).find("select").val(this.preferentialType).change();
                        $tds.eq(2).find("input").val(this.preferentialAmount);
                    });
                    $div.hide();
                }
                // 交通费
                if (data.matter.preferentialTaxMap["3"]) {
                	$div = $dataTrs.eq(rIndex).find("table.trafficChargeTable").parent().show();
                    var $trs = $dataTrs.eq(rIndex).find("table.trafficChargeTable > tbody > tr");
                    $.each(data.matter.preferentialTaxMap["3"], function (i) {
                        var $tds = $trs.eq(i).find("td");
                        $.each(this.islandWayUuids.split(";"), function () {
                            $tds.eq(1).find("input:checkbox[value='" + this + "']").prop("checked", true);
                        });
                        $tds.eq(1).find("input:checkbox:last").change();
                        $tds.eq(2).find("select").val(this.preferentialType).change();
                        $tds.eq(2).find("input").val(this.preferentialAmount);
                    });
                    $div.hide();
                }
            }
            rIndex++;
            // 要求
            $dataTrs.eq(rIndex).find("input:radio[name='allowSuperposed']").eq(data.require.isSuperposition).prop("checked", true).change();
            $dataTrs.eq(rIndex).find("input.minRoom").val(data.require.bookingNumbers);
            $dataTrs.eq(rIndex).find("input.minNight").val(data.require.bookingNights);
            $dataTrs.eq(rIndex).find("input:radio[name='applicableThirdPerson']").eq(data.require.applicableThirdPerson).prop("checked", true);
            $dataTrs.eq(rIndex).find("textarea").val(data.require.memo);
            $.each(data.require.notApplicableDate.split(";"), function (i) {
                if (i) {
                    $dataTrs.eq(rIndex).find("td.NADateTd i.price_sale_house_01").click();
                }
                $dataTrs.eq(rIndex).find("td.NADateTd input:text:last").siblings('[name="dtpRoomPriceStartDate"]').val(data.require.notApplicableDate.split(";")[i].split("~")[0]);
                $dataTrs.eq(rIndex).find("td.NADateTd input:text:last").val(data.require.notApplicableDate.split(";")[i].split("~")[1]);
                //$dataTrs.eq(rIndex).find("td.NADateTd input:text:last").val(this);
            });
            $.each(data.require.notApplicableRoom.split(";"), function (i) {
                if (i) {
                    $dataTrs.eq(rIndex).find("td.NAHouseTypeTd i.price_sale_house_01").click();
                }
                $dataTrs.eq(rIndex).find("td.NAHouseTypeTd select:last").val(this.value);
            });

            rIndex++;
            // 优惠关联
            data.hotelPlPreferentialRels && $.each(data.hotelPlPreferentialRels, function () {
                $dataTrs.eq(rIndex).find("input:checkbox[value='" + this.relHotelPlPreferentialUuid + "']").prop("checked", true);
            });
            
            rIndex++;
            // 优惠描述
            $dataTrs.eq(rIndex).find("textarea").val(data.description);
        }

        //保存到页面价单优惠信息
        function saveData(data) {
            data = (data || { hotelUuid: hotelUuid });

            var $dataTrs = $("#jbox_yh .jbox-content table:first > tbody > tr");
            var rIndex = 0;
            // 优惠名称
            data.preferentialName = $dataTrs.eq(rIndex).find("input").val();
            rIndex++;
            // 下单代码
            data.bookingCode = $dataTrs.eq(rIndex).find("input").val();
            rIndex++;
            // 入住日期
            data.inDate = $dataTrs.eq(rIndex).find("input:eq(0)").val();
            data.inDateString = data.inDate;
            data.outDate = $dataTrs.eq(rIndex).find("input:eq(1)").val();
            data.outDateString = data.outDate;
            // 预定日期
            data.bookingStartDate = $dataTrs.eq(rIndex).find("input:eq(2)").val();
            data.bookingStartDateString = data.bookingStartDate;
            data.bookingEndDate = $dataTrs.eq(rIndex).find("input:eq(3)").val();
            data.bookingEndDateString = data.bookingEndDate;
            rIndex++;
            // 适用房型
            var rowspan = (+$dataTrs.eq(rIndex).find("td:first").prop("rowspan")) || 1;
            data.preferentialRoomList = [];
            while (rowspan--) {
                var item = {
                    hotelRoomUuid: $dataTrs.eq(rIndex).find("select").val(),
                    hotelRoomText: $dataTrs.eq(rIndex).find("select option:selected").text(),
                    occupancyRate: $dataTrs.eq(rIndex).find("select option:selected").attr("data-accommodate"),
                    nights: $dataTrs.eq(rIndex).find("input:text").val(),
                    hotelUuid: hotelUuid
                };
                // 基础餐型
                item.hotelMealList = [];
                $dataTrs.eq(rIndex).find("input:checkbox:checked:not(.chkAll)").each(function () {
                    item.hotelMealList.push({
                        uuid: $(this).val(),
                        mealName: $(this).attr("data-text")
                    });
                });
                data.preferentialRoomList.push(item);
                rIndex++;
            }
            // 交通
            data.islandWayList = [];
            $dataTrs.eq(rIndex).find("input:checkbox:checked:not(.chkAll)").each(function () {
                data.islandWayList.push({
                    uuid: $(this).val(),
                    label: $(this).attr("data-text")
                });
            });
            rIndex++;
            data.isRelation = $dataTrs.eq(rIndex).find("input:checkbox").prop("checked") ? 1 : 0;
            if (data.isRelation) {
                // 关联酒店
            	var relHotelText = '';
            	if($dataTrs.eq(rIndex).find("select").val() != '') {
            		relHotelText = $dataTrs.eq(rIndex).find("select option:selected").text();
            	}
            	
                data.hotelPlPreferentialRelHotel = {
                    hotelUuid: $dataTrs.eq(rIndex).find("select").val(),
                    hotelText: relHotelText
                };
                rIndex++;
                // 关联适用房型
                rowspan = (+$dataTrs.eq(rIndex).find("td:first").prop("rowspan")) || 1;
                while (rowspan--) {
                	//酒店关联房型显示文本
                	var relHotelRoomText = '';
                	if($dataTrs.eq(rIndex).find("select").val() != '') {
                		relHotelRoomText = $dataTrs.eq(rIndex).find("select option:selected").text();
                	}
                    var item = {
                        hotelRoomUuid: $dataTrs.eq(rIndex).find("select").val(),
                        hotelRoomText: relHotelRoomText,
                        occupancyRate: $dataTrs.eq(rIndex).find("select option:selected").attr("data-accommodate"),
                        nights: $dataTrs.eq(rIndex).find("input:text").val(),
                        hotelUuid: data.hotelPlPreferentialRelHotel.hotelUuid,
                    };
                    // 关联基础餐型
                    item.hotelMealList = [];
                    $dataTrs.eq(rIndex).find("input:checkbox:checked:not(.chkAll)").each(function () {
                        item.hotelMealList.push({
                            uuid: $(this).val(),
                            mealName: $(this).attr("data-text")
                        });
                    });
                    data.preferentialRoomList.push(item);
                    rIndex++;
                }
                // 关联交通
                data.hotelPlPreferentialRelHotel.islandWayList = [];
                $dataTrs.eq(rIndex).find("input:checkbox:checked:not(.chkAll)").each(function () {
                    data.hotelPlPreferentialRelHotel.islandWayList.push({
                        uuid: $(this).val(),
                        label: $(this).attr("data-text")
                    });
                });
                rIndex++;
            } else {
                data.hotelPlPreferentialRelHotel = null;
                rIndex += $dataTrs.filter("tr.relevancyHouseTypeTr").length + 2;
            }
            // 优惠事项  具体的优惠信息需要开发提供接口
            data.matter = {
                preferentialTemplatesUuid: $dataTrs.eq(rIndex).find("select.favorType").val(),
                type: $dataTrs.eq(rIndex).find("select.favorType option:selected").attr("data-favorType"),
                preferentialTemplatesText: $dataTrs.eq(rIndex).find("select.favorType option:selected").text(),
                memo: $dataTrs.eq(rIndex).find("textarea").val()
            };
            
            //优惠事项输入信息
            data.matter.matterValues = [];
            if(data.matter.type != "4") {
            	$dataTrs.eq(rIndex).find(".favorTypeOutHtml").find("input").each(function(){
            		data.matter.matterValues.push({
            			myKey: $(this).attr("name"),
            			myValue: $(this).val()
            		});
            	});
            }
            
            if (data.matter.type == "3") {
                var $td = $dataTrs.eq(rIndex).find("tr.chargeTr > td:eq(1)");
                data.matter.preferentialTaxMap = { "4": [] };
                data.matter.preferentialTaxMap["4"].push({
                    istax: $.map($td.find("input:checkbox:checked"), function (dom) {
                        return $(dom).val();
                    }).join(";"),
                    istaxText: $.map($td.find("input:checkbox:checked"), function (dom) {
                        return $(dom).attr("data-text");
                    }).join(";  "),
                    type: 4
                });
            } else {
                data.matter.preferentialTaxMap = { "1": [], "2": [], "3": [] };
                // 房费
                $dataTrs.eq(rIndex).find("table.houseChargeTable > tbody > tr").each(function () {
                    var $tds = $(this).find("td");
                    data.matter.preferentialTaxMap["1"].push({
                    	currencyId: baseInfo.currencyId,
                        travelerTypeUuid: $tds.eq(0).attr("data-value"),
                        travelerTypeText: $tds.eq(0).text(),
                        chargeType: $tds.eq(1).find("select option:selected").attr("data-unitId"),
                        chargeTypeText: $tds.eq(1).find("select option:selected").attr("data-unit"),
                        preferentialAmount: $tds.eq(1).find("input").val(),
                        preferentialType: $tds.eq(1).find("select").val(),
                        preferentialTypeText: $tds.eq(1).find("select option:selected").text(),
                        istax: $.map($tds.eq(2).find("input:checkbox:checked"), function (dom) {
                            return $(dom).val();
                        }).join(";"),
                        istaxText: $.map($tds.eq(2).find("input:checkbox:checked"), function (dom) {
                            return $(dom).attr("data-text");
                        }).join(";"),
                        type: 1
                    });
                });
                // 餐费
                $dataTrs.eq(rIndex).find("table.mealChargeTable > tbody > tr").each(function () {
                    var $tds = $(this).find("td");
                    data.matter.preferentialTaxMap["2"].push({
                    	currencyId: baseInfo.currencyId,
                        travelerTypeUuid: $tds.eq(0).attr("data-value"),
                        travelerTypeText: $tds.eq(0).text(),
                        hotelMealUuids: $.map($tds.eq(1).find("input:checkbox:not(.chkAll):checked"), function (dom) {
                            return $(dom).val();
                        }).join(";"),
                        hotelMealText: $.map($tds.eq(1).find("input:checkbox:not(.chkAll):checked"), function (dom) {
                            return $(dom).attr("data-text");
                        }).join(";"),
                        chargeType: $tds.eq(2).find("select option:selected").attr("data-unitId"),
                        chargeTypeText: $tds.eq(2).find("select option:selected").attr("data-unit"),
                        preferentialAmount: $tds.eq(2).find("input").val(),
                        preferentialType: $tds.eq(2).find("select").val(),
                        preferentialTypeText: $tds.eq(2).find("select option:selected").text(),
                        type: 2
                    });
                });
                // 交通费
                $dataTrs.eq(rIndex).find("table.trafficChargeTable > tbody > tr").each(function () {
                    var $tds = $(this).find("td");
                    data.matter.preferentialTaxMap["3"].push({
                    	currencyId: baseInfo.currencyId,
                        travelerTypeUuid: $tds.eq(0).attr("data-value"),
                        travelerTypeText: $tds.eq(0).text(),
                        islandWayUuids: $.map($tds.eq(1).find("input:checkbox:not(.chkAll):checked"), function (dom) {
                            return $(dom).val();
                        }).join(";"),
                        islandWayText: $.map($tds.eq(1).find("input:checkbox:not(.chkAll):checked"), function (dom) {
                            return $(dom).attr("data-text");
                        }).join(";"),
                        chargeType: $tds.eq(2).find("select option:selected").attr("data-unitId"),
                        chargeTypeText: $tds.eq(2).find("select option:selected").attr("data-unit"),
                        preferentialAmount: $tds.eq(2).find("input").val(),
                        preferentialType: $tds.eq(2).find("select").val(),
                        preferentialTypeText: $tds.eq(2).find("select option:selected").text(),
                        type: 3
                    });
                });
            }
            rIndex++;
            // 要求
            data.require = {
                isSuperposition: $dataTrs.eq(rIndex).find("input:radio[name='allowSuperposed']").prop("checked") ? 0 : 1,
                bookingNumbers: $dataTrs.eq(rIndex).find("input.minRoom").val(),
                bookingNights: $dataTrs.eq(rIndex).find("input.minNight").val(),
                notApplicableDate: $.grep($.map($dataTrs.eq(rIndex).find("td.NADateTd [name='dtpRoomPriceStartDate']"), function (dom) {
                	if(($(dom).val() != '') && ($(dom).siblings('[name="dtpRoomPriceEndDate"]').val() !='')) {
                        return $(dom).val() + '~' + $(dom).siblings('[name="dtpRoomPriceEndDate"]').val();
                	}
                }), function (date) {
                    return date;
                }).join(';'),
                notApplicableRoom: $.grep($.map($dataTrs.eq(rIndex).find("td.NAHouseTypeTd select"), function (dom) {
                    return $(dom).val();
                }), function (date) {
                    return date;
                }).join(';'),
                notApplicableRoomName: $.grep($.map($dataTrs.eq(rIndex).find("td.NAHouseTypeTd select"), function (dom) {
                    return $(dom).find("option:selected").text();
                }), function (date) {
                    return date;
                }).join(';'),
                applicableThirdPerson: $dataTrs.eq(rIndex).find("input:radio[name='applicableThirdPerson']").prop("checked") ? 0 : 1,
                memo: $dataTrs.eq(rIndex).find("textarea").val()
            };
            rIndex++;
            // 优惠关联
            data.hotelPlPreferentialRels = [];
            if (data.require.isSuperposition == 0) {
                $dataTrs.eq(rIndex).find("input:checkbox:checked").each(function () {
                    data.hotelPlPreferentialRels.push({
                        relHotelPlPreferentialUuid: $(this).val(),
                        relHotelPlPreferentialName: $(this).attr("data-text")
                    });
                })
            }
            rIndex++;
            // 优惠描述
            data.description = $dataTrs.eq(rIndex).find("textarea").val();
            
            return data;
        }

        //优惠信息提交按钮后的回调函数
        return function (options) {
            var boxHtml = $("#popAddPromotion").html();
            options || (options = {});
            options.isAdd = options.isAdd || !options.id;
            $.jBox(boxHtml, {
                title: options.isAdd ? "新增优惠信息" : "修改优惠信息", id: "jbox_yh", buttons: (options.buttons || { '保存': 1 }), submit: function (v, h, f) {
                	var $table = $("#jbox_yh .jbox-content table:first");

                	var txtPromotionName = $table.find("#txtPromotionName").val();
                	if($.trim(txtPromotionName) == '') {
                		$table.find("#txtPromotionName").val("").focus();
                		$.jBox.tip("优惠名称不能为空！");
                		return false;
                	}
                	
                	var preferentialDesc = $table.find("td.preferentialDesc textarea").val();
            		if($.trim(preferentialDesc) == "") {
            			$table.find("td.preferentialDesc textarea").val("").focus();
            			$.jBox.tip("优惠描述不能为空！");
            			return false;
            		}
                	
                	var notNullValidate = true;
                	$table.find(".suitRommType").each(function(){
                		if($(this).val() == "" || $(this).val() == null) {
                			$(this).focus();
                			notNullValidate = false;
                			$.jBox.tip("适用房型不能为空！");
                			return false;
                		}
                	});
                	
                	//关联适用房型取消非空验证 edit by majiancheng 2015-11-8
                	/*if($table.find(".relevancyHotelTr input[type=checkbox]").attr("checked") == "checked") {
                    	notNullValidate && $table.find(".relSuitRommType").each(function(){
                    		if($(this).val() == "" || $(this).val() == null) {
                    			$(this).focus();
                    			notNullValidate = false;
                    			$.jBox.tip("关联适用房型不能为空！");
                    			return false;
                    		}
                    	});
                	}*/
                	
                	notNullValidate && $table.find(".roomNight").each(function(){
                		if($(this).val() == "" || $(this).val() == null) {
                			$(this).focus();
                			notNullValidate = false;
                			$.jBox.tip("房型晚数不能为空！");
                			return false;
                		}
                	});
                	
                	//基础餐型必填
                	notNullValidate && $table.find("tr.applyHouseTypeTr").each(function(){
                		if($(this).find("input:checkbox:not(.chkAll):checked").size() == 0) {
                			notNullValidate = false;
                			$.jBox.tip("请选择基础餐型！");
                			return false;
                		}
                	});
                	
                	//提前预订天数必填
                	if($table.find("select.favorType option:selected").attr("data-favortype") == 2) {
                		var earlyDays = $table.find("td.favorTypeOutHtml").find("input[name=earlyDays]");
                		if(earlyDays.val() == "" || earlyDays.val() < 1 ) {
                    		notNullValidate = false;
                    		earlyDays.focus();
                			$.jBox.tip("提前预订天数最少输入1天，请重新输入！");
                			return false;
                		}
                	}
                	
                	if(!notNullValidate) {
                		return false;
                	}
                	
                	//验证不适用日期非空-----------------------
                    var dateEmptyYN=true;
                    $.each($("td.NADateTd:last").find("span"), function () {
                    	//当开始日期和结束日期有一个
                    	if(($(this).find("input").eq(0).val() == "") && ($(this).find("input").eq(1).val() != "")) {
                    		dateEmptyYN = false;
                    	}
                    	
                    	if(($(this).find("input").eq(0).val() != "") && ($(this).find("input").eq(1).val() == "")) {
                    		dateEmptyYN = false;
                    	}
                    	
                    });
                    if(dateEmptyYN==false){
                        $.jBox.info("不适用日期，开始日期和结束日期填写错误", '系统提示');
                        return false;
                    }
                    //验证不适用日期非空-----------------------

                    //验证日期是否重叠------------------------------------
                    var notApplicableDate = $.grep($.map($table.find("td.NADateTd [name='dtpRoomPriceStartDate']"), function (dom) {
                    	if(($(dom).val() != '') && ($(dom).siblings('[name="dtpRoomPriceEndDate"]').val() !='')) {
                            return $(dom).val() + '~' + $(dom).siblings('[name="dtpRoomPriceEndDate"]').val();
                    	}
                    }), function (date) {
                        return date;
                    }).join(';');
                    
                    var dateRange = notApplicableDate.split(";");
                    var dateArrayList = new Array();
                    var duplicatedList = new Array();
                    for (i = 0; i < dateRange.length; i++) {
                        var dateObj = {};
                        dateObj.startDate = dateRange[i].split("~")[0];
                        dateObj.endDate = dateRange[i].split("~")[1];
                        dateArrayList.push(dateObj);
                    }
                    checkReduplicated(dateArrayList, duplicatedList);
                    if (duplicatedList.length > 0) {
                        $.jBox.info("不适用日期有重叠，请重新选择！", '系统提示');
                        return false;
                    }
                    //验证日期是否重叠------------------------------------
                    
                    
                	
                    var data = saveData(options.data);
                    return options.callback(data, options, { v: v, h: h, f: f });
                }, height: '560', width: 980, persistent: true
            });
            initEvent(options);
            if (options.data) {
                initData(options.data);
            }
        }
    })();
    // 事件注册
    $("#favor").on("click", "a.copyLink", function () {
        // 复制并新增
        var id = $("#favor").children("div").has(this).prop("id");
        var data = jQuery.extend(true, {}, readFavorTr(id));
        data.uuid=undefined;
        var options = {
            isAdd: true,
            data: data,
            callback: writeFavorTr
        }
        addFavor_box(options);
    }).on("click", "a.updateLink", function () {
        // 修改
        var id = $("#favor").children("div").has(this).prop("id");
        var uuid = $("#favor").children("div").has(this).attr("uuid");
        var options = {
            isAdd: false,
            id: id,
            uuid: uuid,
            data: readFavorTr(id),
            callback: writeFavorTr
        }
        addFavor_box(options);
    }).on("click", "a.delLink", function () {
    	var aDelLink = this;
    	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
			if (v == 'ok') {
		        // 删除
		        var $div = $("#favor").children("div").has(aDelLink);
		        var id = $("#favor").children("div").has(aDelLink).prop("id");
		        var uuid = $("#favor").children("div").has(aDelLink).attr("uuid");
		        $div.remove();
		        $("#favor_nav li[data-for='" + id + "']").remove();
		        tabScroll($("#favor_nav"));
		        // 删除优惠信息
		        delete favorInfos[id];
		        // 删除可关联的优惠
		        if (relevancyFavors.favors[uuid]) {
		            delete relevancyFavors.favors[uuid];
		            relevancyFavors.length -= 1;
		        }
			}
		});
    });

    // 新增优惠信息
    $("#btnAddPromotion").on('click', function () {
        var options = {
            isAdd: true,
            callback: writeFavorTr
        }
        addFavor_box(options);
    });
    
    /**
     * 保存酒店价单优惠信息
     */
    $(document).on("click", "#btnSavePromotion", function (e) {
    	var status = 1;
    	//当updateFlag为1或相应模块的保存状态为1时
    	if($("#updateFlag").val() == "2" || $("#preferentialSaveFlag").val() == "2") {
    		status = 2;
    	} else {
    		$("#preferentialSaveFlag").val("2");
    	}
    	
    	var plPreferentialArr = [];
    	for(var key in favorInfos){
    		plPreferentialArr.push(favorInfos[key]);
    	}
    	var url = ctx + "/hotelPl/saveHotelPlPreferential";
    	var hotelPlPreferentialJsonData = JSON.stringify(plPreferentialArr);
    	ajaxStart();
    	$.ajax({
    		type: "POST",
    	   	url: url,
    	   	async: false,
    	   	data: {
    				"hotelPlPreferentialJsonData":hotelPlPreferentialJsonData,
    				"status":status,
    				"hotelPlUuid":baseInfo.uuid
    			  },
    		dataType: "json",
    	   	success: function(data){
    	   		ajaxStop();
    	   		if(data){
    	   			if(data.result == 1) {
    	   				$.jBox.tip("优惠信息保存成功！");
    	   			} else if(data.result == 2) {
    	   				$.jBox.tip("优惠信息更新成功！");
    	   			} else if(data.result == 3) {
    	   				$.jBox.tip(data.message);
    	   			}
    	   			if(data.result == 1 || data.result == 2) {
    	   				var i=0;
    	   				var preferentialRelevancyFavors = jQuery.parseJSON(data.relevancyFavors);
    	   				preferentialRelevancyFavors && $.each(preferentialRelevancyFavors, function () {
    	   	                relevancyFavors.favors[this.uuid] = this.preferentialName;
    	   					i++;
    	   	            });
    	   				relevancyFavors.length = i;
    	   				
    	   				
    	   				var preferentialUuids = jQuery.parseJSON(data.preferentialUuids);
    	   				
    	   				//为已经新增过的优惠进行赋值
    	   				var i=0;
    	   				for(var key in favorInfos){
    	   		    		favorInfos[key].uuid=preferentialUuids[i++];
    	   		    	}
    	   			}
    	   		}
    	   	}
    	});
    	
        saveButtonCli($(this));
    });
    
});

//从列表读取优惠信息
function readFavorTr(id) {
    return favorInfos[id];
}

function getTextByAttribute(attribute) {
	if(attribute == null || attribute == undefined) {
		return "";
	} else {
		return attribute;
	}
}


//向列表写优惠信息
function writeFavorTr(data, options) {
    var $div;
    if (options.isAdd) {
        options.id = buildID();
        $div = $("#favorTemp").clone().attr("id", options.id);
        options.uuid = data.uuid||"";
        $div.attr("uuid", options.uuid);
        $("#favor").append($div);
        $("#favor_nav ul").append('<li data-for="' + options.id + '"></li>');
    } else {
        $div = $("#" + options.id);
    }
    favorInfos[options.id] = data;

    $("#favor_nav li[data-for='" + options.id + "']").text(data.preferentialName);
    tabScroll($("#favor_nav"), "[data-for='" + options.id + "']");

    var html = [];
    var $span = $div.find("div.preferential_information_list span");
    // 优惠名称
    $span.eq(0).text(data.preferentialName);
    // 下单代码
    $span.eq(1).text(data.bookingCode);
    // 入住日期
    $span.eq(2).text(formatDate([data.inDateString, data.outDateString]));
    // 预定日期
    $span.eq(3).text(formatDate([data.bookingStartDateString, data.bookingEndDateString]));

    var $tbody = $div.find("table.favorTable > tbody");
    $tbody.children("tr:not(:first)").remove();
    // 适用房型
    var roomList = $.grep(data.preferentialRoomList, function (room) {
        return room.hotelUuid == hotelUuid
    });
    $.each(roomList, function (i) {
        if (i) {
            $tbody.append('<tr><td class="tc"></td><td class="tc"></td></tr>');
        }
        var $tr = $tbody.children("tr:last");
        html = [];
        html.push('<p>房型*晚数：<span>', this.hotelRoomText, '*', this.nights, '晚</span></p>');
        html.push('<p>', this.occupancyRate, '</p>')
        $tr.find("td:first").html(html.join(''));
        // 基础餐型
        html = [];
        if(this.hotelMealList){
            $.each(this.hotelMealList, function () {
                html.push('<p>', this.mealName, '</p>');
            });
        }
        $tr.find("td:eq(1)").html(html.join(''));

        if (i == 0) {
            // 交通
            html = [];
            data.islandWayList && $.each(data.islandWayList, function () {
                html.push('<p>', this.label, '</p>');
            });
            $tr.find("td:eq(2)").html(html.join('')).attr("rowspan", roomList.length);
        }
    });
    // 关联酒店
    if (data.isRelation) {
        roomList = $.grep(data.preferentialRoomList, function (room) { return room.hotelUuid == data.hotelPlPreferentialRelHotel.hotelUuid });
        $.each(roomList, function (i) {
            $tbody.append('<tr class="relevancy"><td class="tc"></td><td class="tc"></td></tr>');
            var $tr = $tbody.children("tr:last");
            html = [];
            html.push('<p>关联岛屿:<span>', data.hotelPlPreferentialRelHotel.hotelText, '</span></p>');
            html.push('<p>房型*晚数：<span>', this.hotelRoomText, '*', this.nights, '晚</span></p>');
            html.push('<p>', this.occupancyRate, '</p>')
            $tr.find("td:first").html(html.join(''));
            // 基础餐型
            html = [];
            if(this.hotelMealList){
            	$.each(this.hotelMealList, function () {
            		html.push('<p>', this.mealName, '</p>');
            	});
            }
            $tr.find("td:eq(1)").html(html.join(''));

            if (i == 0) {
                $tr.append('<td class="tc"></td>');
                // 交通
                html = [];
                data.hotelPlPreferentialRelHotel.islandWayList && $.each(data.hotelPlPreferentialRelHotel.islandWayList, function () {
                    html.push('<p>', this.label, '</p>');
                });
                $tr.find("td:eq(2)").html(html.join('')).attr("rowspan", roomList.length);
            }
        });
    }
    var $tds = $tbody.children("tr:first").children("td");
    // 优惠事项
    $tds.eq(3).find("span.favorType").text(data.matter.preferentialTemplatesText);
    
    //拼接优惠类型描述
    var favorTypeOutHtml;
    
    var liveNights;
	var freeNights;
	var earlyDays;
	var totalPrice;
	data.matter.matterValues && $.each(data.matter.matterValues, function(){
		if(this.myKey == "liveNights") {
			liveNights = this.myValue;
		} else if(this.myKey == "freeNights") {
			freeNights = this.myValue;
		} else if(this.myKey == "earlyDays") {
			earlyDays = this.myValue;
		} else if(this.myKey == "totalPrice") {
			totalPrice = this.myValue;
		}
	});
	
    if(data.matter.type == "1") {
    	favorTypeOutHtml = "住："+ getTextByAttribute(liveNights) +"晚，免："+ getTextByAttribute(freeNights) +"晚";
    } else if(data.matter.type == "2") {
    	favorTypeOutHtml = "提前预定天数："+ getTextByAttribute(earlyDays) +"天";
    } else if(data.matter.type == "3") {
    	favorTypeOutHtml = "合计：" + baseInfo.currencyMark + getTextByAttribute(totalPrice);
    }
    $tds.eq(3).find("span.favorTypeOutHtml").text(favorTypeOutHtml);
    
    if (data.matter.type == "3") {
        $tds.eq(3).find("div.houseCharge").hide();
        $tds.eq(3).find("div.mealCharge").hide();
        $tds.eq(3).find("div.trafficCharge").hide();
        if (data.matter.preferentialTaxMap["4"]) {
            var item = data.matter.preferentialTaxMap["4"][0];
            var $div = $tds.eq(3).find("div.taxDiv");
            if (item && item.istaxText) {
                $tds.eq(3).find("div.taxDiv").text(item.istaxText).show();
            }
        }
    } else {
        $tds.eq(3).find("div.taxDiv").hide();
        // 房费
        //增加非空验证 update by zhanghao
        if(data.matter.preferentialTaxMap["1"]){
        	var houseCharges = $.grep(data.matter.preferentialTaxMap["1"], function (obj) {
                return obj.preferentialType != "0";
            });
            var $houseChange = $tds.eq(3).find("div.houseCharge")[houseCharges.length ? "show" : "hide"]().find("table > tbody");
            if (houseCharges.length) {
                $houseChange.find("tr:not(:first)").remove();
                $.each(houseCharges, function (i) {
                    if (i) {
                        $houseChange.append($houseChange.find("tr:first").clone());
                    };
                    var $lastTds = $houseChange.find("tr:last td");
                    $lastTds.eq(0).html(this.travelerTypeText);
                    if(this.chargeType == 2) {
                        $lastTds.eq(1).html(getTextByAttribute(this.preferentialTypeText) + " " + (this.chargeTypeText||"") + (this.preferentialAmount||""));
                    } else {
                        $lastTds.eq(1).html(getTextByAttribute(this.preferentialTypeText) + " " + (this.preferentialAmount||"") + (this.chargeTypeText||""));
                    }
                    $lastTds.eq(2).html(this.istaxText);
                });
            }
        }
        
        // 餐费
        //增加非空验证 update by zhanghao
        if(data.matter.preferentialTaxMap["2"]){
        	var mealCharges = $.grep(data.matter.preferentialTaxMap["2"], function (obj) {
                return obj.preferentialType != "0";
            });
            var $mealCharge = $tds.eq(3).find("div.mealCharge")[mealCharges.length ? "show" : "hide"]().find("table > tbody");
            if (mealCharges.length) {
                $mealCharge.find("tr:not(:first)").remove();
                $.each(mealCharges, function (i) {
                    if (i) {
                        $mealCharge.append($mealCharge.find("tr:first").clone());
                    };
                    var $lastTds = $mealCharge.find("tr:last td");
                    $lastTds.eq(0).text(this.travelerTypeText);
                    $lastTds.eq(1).text(this.hotelMealText);
                    if(this.chargeType == 2) {
                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.chargeTypeText||"") + getTextByAttribute(this.preferentialAmount));
                    } else {
                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.preferentialAmount) + getTextByAttribute(this.chargeTypeText||""));
                    }
                    $lastTds.eq(3).text(this.istaxText);
                });
            }
        }
        
        // 交通费
        //增加非空验证 update by zhanghao
        if(data.matter.preferentialTaxMap["3"]){
        	var trafficCharges = $.grep(data.matter.preferentialTaxMap["3"], function (obj) {
                return obj.preferentialType != "0";
            });
            var $trafficCharge = $tds.eq(3).find("div.trafficCharge")[trafficCharges.length ? "show" : "hide"]().find("table > tbody");
            if (trafficCharges.length) {
                $trafficCharge.find("tr:not(:first)").remove();
                $.each(trafficCharges, function (i) {
                    if (i) {
                        $trafficCharge.append($trafficCharge.find("tr:first").clone());
                    };
                    var $lastTds = $trafficCharge.find("tr:last td");
                    $lastTds.eq(0).text(this.travelerTypeText);
                    $lastTds.eq(1).text(this.islandWayText);
                    if(this.chargeType == 2) {
                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.chargeTypeText||"") + getTextByAttribute(this.preferentialAmount));
                    } else {
                        $lastTds.eq(2).text(getTextByAttribute(this.preferentialTypeText) + "： " + getTextByAttribute(this.preferentialAmount) + getTextByAttribute(this.chargeTypeText||""));
                    }
                    $lastTds.eq(3).text(this.istaxText);
                });
            }
        }
        
    }
    $tds.eq(3).find("p.comment").text("备注 ： " + data.matter.memo);
    // 要求
    html = [];
    html.push('<p>起订晚数 ： ');
    if (+(data.require.bookingNights) > 0) {
        html.push(data.require.bookingNights, "晚</p>");
    } else {
        html.push("不限</p>");
    }
    html.push('<p>起订间数 ： ');
    if (+(data.require.bookingNumbers) > 0) {
        html.push(data.require.bookingNumbers, "间</p>");
    } else {
        html.push("不限</p>");
    }
    html.push('<p>不适用日期 ： ', data.require.notApplicableDate || '不限', "</p>");
    //html.push('<p>不适用房型 ： ', data.require.notApplicableRoomName || '不限', "</p>");
    html.push('<p>适用第三人 ： ', (data.require.applicableThirdPerson ? '否' : '是'), '</p>');
    html.push(data.require.isSuperposition ? '<p class="tdred">优惠叠加 ： 不允许</p>' : '<p class="tdgreen">优惠叠加 ： 允许</p>');
    html.push('<p>备注 ： ', data.require.memo, '</p>');
    $tds.eq(4).html(html.join(''));
    // 优惠关联
    html = [];
    data.hotelPlPreferentialRels && $.each(data.hotelPlPreferentialRels, function (i) {
        html.push('<p>', (i + 1), '、<span>', this.relHotelPlPreferentialName, '</span></p>');
    });
    $tbody.children("tr:first").children("td:eq(5)").html(html.join(''));
    //
    $tbody.children("tr:first").children("td:gt(2)").attr("rowspan", $tbody.children("tr").length);
}

// 格式化日期范围
function formatDate(dates) {
//    var newDates = [];
//    $.each(dates, function () {
//        newDates.push(this.replace(/-/g, "."));
//    });
    return dates.join("-");
}



function getFavorHtml(val, obj) {
	var outHtml ;
	for(var i=0; i<templatesesJson.length; i++) {
		if(val == templatesesJson[i].uuid) {
			outHtml = templatesesJson[i].outHtml;
			break;
		}
	}
    return outHtml;
}

function getFavorObj(type, $dom) {
    return {
        data: {},
        render: '住3晚免1晚'
    }
}

function bindPreferential(favors) {
    //todo
    hotelUuid = baseInfo.hotelUuid;
    for (var i = 0, l = favors.length; i < l; i++) {
        writeFavorTr(favors[i], { isAdd: true });
    }

    var $btnAddPromotion = $("#btnAddPromotion");
    $btnAddPromotion.removeClass("edit_button");
    var $btnEditPromotion = $("#btnEditPromotion");
    $btnEditPromotion.addClass("edit_button");
    var $btnSavePromotion = $("#btnSavePromotion");
    var $region = $btnSavePromotion.parents(".price_region:first");
    setRegionDisabled($region);
    $btnEditPromotion.removeClass("region_disabled");
    $btnSavePromotion.addClass("region_disabled");

}
