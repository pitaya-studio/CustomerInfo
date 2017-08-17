// JavaScript source code
// 航空公司--航班号--起飞时间--到达时间联动数据结构

var map = {};
//查询舱位等级
function getSpaceLevelAjax(type,airlineCode,flightnum){
	var _data = {};
	var res = null;
	if(type=='space_level'){//舱位等级
		if(airlineCode==null || airlineCode=='' || flightnum==null || flightnum=='' || flightnum=='请选择'){
			return null;
		}
		_data={"type":type,"airlineCode":airlineCode,"flightnum":flightnum};
	}else if(type=='traveler_type'){
		_data={"type":type, "hotelUuid":$('#hotelUuid').val()};
	}else if(type=='currency_list'){
		_data={"type":type};
	}else{
		return null;
	}
	$.ajax({
    		type: "POST",
    		async:false,
    	   	data: _data,
    		dataType: "json",
    		url: $ctx+"/activityIsland/getAirlineInfoByType",
    	   	success: function(data){
    	   		res = data;
    	   	}
    	});
		return res;
}

var addGroup_box = (function () {
    function initEvent(options) {
        var $table = $("#jbox_tq table.table_product_info");
        var $houseTypeSel = $table.find("tr.houseTypeTr:not(.mealTypeTr)").children("td:nth-child(2)").find("select").empty();
        var html = [];
        for (var key in options.houseTypes) {
            html.push("<option value='", key, "'>", options.houseTypes[key].name, "</option>");
        }
        $houseTypeSel.append(html.join(''));
        var $airlineSel = $table.find("select.selAirline").empty();
        html = ["<option value=''>请选择</option>"];
        for (var key in airlines) {
            html.push("<option value='", key, "'>", airlines[key].name, "</option>");
        }
        $airlineSel.append(html.join(''));
        var $table = $("#jbox_tq table.table_product_info");
        // 房型模板
        var $trHouseType;

        $table.on('click', 'a.addHouseType', function () {
            // 添加房型
            var $tr = $trHouseType.clone();
            $table.find("tr.houseTypeTr:last").after($tr);
            $tr.find('select:eq(0)').change();
        }).on('click', 'a.delHouseType', function () {
            // 删除房型
            var $tr = $table.find("tr.houseTypeTr").has(this);
            var $next = $tr.next();
            $tr.nextUntil("tr.houseTypeTr:not(.mealTypeTr), tr:not(.houseTypeTr)").remove();
            $tr.remove();
        }).on('click', 'a.addMealType', function () {
            // 添加餐型
            var $tr = $table.find("tr.mealTypeTr").has(this);
            var $firstTd = $tr.find("td:first");
            $firstTd.prop("rowspan", ($firstTd.prop("rowspan") || 1) + 1);
            var $lastTr = $tr.nextUntil("tr:not(.mealTypeTr)").last();
            $lastTr.length || ($lastTr = $tr);

            $tr = $tr.clone();
            $tr.find('a.addMealType').replaceWith('<a class="ydbz_x gray delMealType">删除</a>');
            $tr.find('td:first').hide();
            $tr.find('p').has('a.delUpMealType').remove();
            $tr.find('input').val('');

            $lastTr.after($tr);
            $tr.find('select:first').change();
        }).on('click', 'a.delMealType', function () {
            // 删除餐型
            var $tr = $table.find("tr.mealTypeTr").has(this);
            var $firstTr = $tr.prevUntil("tr:not(.mealTypeTr)").first();
            var $firstTd = $firstTr.find("td:first");
            $firstTd.prop("rowspan", ($firstTd.prop("rowspan") || 1) - 1);
            $tr.remove();
        }).on('click', 'a.addUpMealType', function () {
            // 添加升级餐型
            var $td = $table.find("td").has(this);
            var $pUpMealType = $td.find("p").has(this).clone();
            $pUpMealType.find("a.addUpMealType").replaceWith('<a class="ydbz_x gray delUpMealType">删除</a>');
            $pUpMealType.find("input").val('');
            $td.append($pUpMealType);
        }).on('click', 'a.delUpMealType', function () {
            // 删除升级餐型
            $table.find("p.upMealType").has(this).remove();
        }).on("change", "tr.mealTypeTr input:checkbox", function () {
            // 是否升级餐型
            var checked = $(this).prop("checked");
            $table.find("td").has(this).attr("colspan", checked ? 1 : 3);
            $table.find("td").has(this).nextAll("td")[checked ? "show" : "hide"]();
        }).on("click", "a.selLink", function (event) {
            // 选择控票数
            $(this).find("div.pop_inner_outer_hotel").show();
            event.stopPropagation();
        }).on('change', "tr.houseTypeTr:not(.mealTypeTr) select", function () {
            // 房型改变
            var $this = $(this);
            var houseType = options.houseTypes[$this.val()] || {};
            var $tr = $("tr.houseTypeTr").has(this);
            var $mealTypeSels = $tr.nextUntil("tr.houseTypeTr:not(.mealTypeTr), tr:not(.houseTypeTr)").children("td:nth-child(2)").find("select");
            var baseMealTypes = houseType.baseMealTypes;
            $mealTypeSels.empty();
            for (var key in baseMealTypes) {
                $mealTypeSels.append("<option value='" + key + "'>" + baseMealTypes[key].name + "</option>");
            }
            $mealTypeSels.change();
            
            $this.attr("title", $this.find("option:selected").text());
        }).on('change', "tr.houseTypeTr.mealTypeTr td:nth-child(2) select", function () {
            // 基础餐型改变
            var $this = $(this);
            var $tr = $("tr.houseTypeTr").has(this);
            var $houseTypeTr = $tr.prevUntil("tr.houseTypeTr:not(.mealTypeTr)").prev();
            $houseTypeTr.length || ($houseTypeTr = $tr.prev());
            var houseType = options.houseTypes[$houseTypeTr.find('select').val()];
            if(houseType) {
                var baseMealType = houseType.baseMealTypes[$this.val()];
                if(baseMealType) {
                    var upMealTypes = baseMealType.upMealTypes;
                    var $upMealTypeTr = $tr.find('td:eq(3) p select:nth-child(1)');
                    $upMealTypeTr.empty();
                    for (var key in upMealTypes) {
                        $upMealTypeTr.append("<option value='" + key + "'>" + upMealTypes[key].name + "</option>");
                    }
                    
                    $upMealTypeTr.attr("title", $upMealTypeTr.find("option:selected").text());
                }
            }
            
            $this.attr("title", $this.find("option:selected").text());
        }).on('change', "tr.houseTypeTr.mealTypeTr td.upMealTypeTd select", function () {
            // 升级餐型改变
            var $this = $(this);
            
            $this.attr("title", $this.find("option:selected").text());
        }).on("click", "a.selLink", function (event) {
            // 选择控票数
            var $parentTr = $(this).parentsUntil("tr").parent();
            var offset = $parentTr.next().offset();
            $(this).find("div.pop_inner_outer").show().offset(offset);
            $table.find("div.pop_inner_outer").not($(this).find("div.pop_inner_outer")).hide();
            event.stopPropagation();
        }).on('change', 'select.selAirline', function () {
            // 更改航空公司
            var airline = airlines[$(this).val()];
            var options = [];
            if (airline && airline.flights) {
                options = ["<option value=''>请选择</option>"]
                for (var key in airline.flights) {
                    var flight = airline.flights[key];
                    options.push("<option value='", key, "' data-start='", flight.start, "' data-end='", flight.end, "' data-day='", (flight.days || 0), "'>", flight.name, "</option>");
                }
            }
            $table.find("select.fltNo").empty().append(options.join('')).change();
        }).on('change', 'select.fltNo', function () {
        	var airlineCode = $("#jbox_tq table.table_product_info .selAirline").val();
     		var space_level = $("#jbox_tq table.table_product_info .fltNo").val();
            // 更改航班
            if ($(this).prop("selectedIndex") <= 0) {
                $table.find("td.hotel_air_price > table:eq(0)").hide();
                $table.find("td.hotel_air_price > table:eq(1)").show();
                $table.find("input.startTime").attr("disabled", "disabled").val('');
                $table.find("input.endTime").attr("disabled", "disabled").val('');
                $table.find("input.days").attr("disabled", "disabled").val('');
            } else {
            	//动态开始
           	 	var res_1 = getSpaceLevelAjax('space_level',airlineCode,space_level);
	            if(res_1!=null){
	             	var res_2 = getSpaceLevelAjax('traveler_type','','');
	             	var res_3 = getSpaceLevelAjax('currency_list','','');
	             	//动态航班--余位部分  开始
	             	//动态 舱位等级
	             	var tableNode = $table.find("td.hotel_air_price > table:eq(0)");
	             	$(tableNode).html('');
	             	var trNode1 = $("<tr/>");
	             	$(trNode1).append($("<th/>").attr("style","width:112px").attr("class","tr new_hotel_p_table2_tdblue2 new_hotel_p_table2_tdblue2_mr20").text("舱位等级"));
	             	$(res_1.space_level).each(function(i,item){
	             		$(trNode1).append($("<th/>").attr("style","width:"+(671/($(res_1.space_level).length))+"px").attr("data-value",item.spaceLevel).text(item.space));
	             	});
	             	$(trNode1).append($("<th/>").attr("style","width:31").text("合计"));
	             	var theadNode = $("<thead/>").append(trNode1);
	             	$(tableNode).append(theadNode);
	             	//动态 同行价
	             	var tbodyNode = $("<tbody/>");
	             	$(res_2.traveler_type).each(function(i,item){
	                 	var trNode2 = $("<tr/>");
	             		var tdNode2_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").attr("data-text",item.name).attr("data-value",item.uuid).html(item.name+"同行<br/>（机+酒）价/人");
	             		$(trNode2).append(tdNode2_1);
	             		$(res_1.space_level).each(function(j,n){
	             			var tdNode2_2 = $("<td/>").attr("style","text-align:left !important; ");
	             			var spanNode_ = $("<span/>").attr("class","add_jbox_repeat_thj2");
	             			var selectNode_ = $("<select/>").attr("name","peoplePrice").attr("class","w50_30 currency");
	             			$(res_3.currency_list).each(function(k,m){
	             				var optionNode_ = $("<option/>").val(m.id).text(m.currencyMark);
	             				$(selectNode_).append(optionNode_);
	             			});
	             			$(spanNode_).append(selectNode_);
	             			$(spanNode_).append($("<input/>").attr("type","text").attr("data-type","float").attr("class","inputTxt w50_30 babyPrice mr25"));
	             			$(tdNode2_2).append(spanNode_);
	             			$(trNode2).append(tdNode2_2);
	             		});
	             		var tdNode2_3 = $("<td/>").text("---");
	             		$(trNode2).append(tdNode2_3);
	             		$(tbodyNode).append(trNode2);
	             	});  
	             	//动态 控票数
	                var trNode3 = $("<tr/>");
	                $(trNode3).append($("<td/>").attr("class","tr new_hotel_p_table2_tdblue").attr("style", "width:95px").text("控票数"));
	                $(res_1.space_level).each(function(){
	                	var tdNode3_2 = $("<td/>").attr("class","tc").attr("style","width:215px !important");
	                	$(tdNode3_2).append($("<input/>").attr("type","text").attr("data-type","number").attr("data-min","0").attr("class","inputTxt w50_30 spread mar_left_35 kpNum"));
	             		$(trNode3).append(tdNode3_2);
	             	});
	                $(trNode3).append($("<td/>"));   
	                $(tbodyNode).append(trNode3);
	             	//动态 非控票数
	                var trNode4 = $("<tr/>");
	                var tdNode4_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("非控票数");
	                $(trNode4).append(tdNode4_1);
	                $(res_1.space_level).each(function(){
	             		var tdNode4_2 = $("<td/>").append($("<input/>").attr("type","text").attr("data-type","number").attr("data-min","0").attr("class","inputTxt w50_30 spread mar_left_35 fkpNum"));
	             		$(trNode4).append(tdNode4_2);
	             	});
	             	var tdNode4_3 = $("<td/>");
	                $(trNode4).append(tdNode4_3);   
	                $(tbodyNode).append(trNode4);
	             	//动态 票数总计
	             	var trNode5 = $("<tr/>");
	                var tdNode5_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("票数总计");
	                $(trNode5).append(tdNode5_1);
	                $(res_1.space_level).each(function(){
	                	var tdNode5_2 = $("<td/>");
	             		$(trNode5).append(tdNode5_2);
	             	});
	                var tdNode5_3 = $("<td/>");
	                $(trNode5).append(tdNode5_3);   
	                $(tbodyNode).append(trNode5); 
	             	//动态 余位
	             	var trNode6 = $("<tr/>");
	                var tdNode6_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("余位");
	                $(trNode6).append(tdNode6_1);
	                $(res_1.space_level).each(function(){
	                	var tdNode6_2 = $("<td/>");
	             		$(trNode6).append(tdNode6_2);
	             	});
	                var tdNode6_3 = $("<td/>");
	                $(trNode6).append(tdNode6_3);
	                $(tbodyNode).append(trNode6);
	                $(tableNode).append(tbodyNode);
                }
               //动态  结束
               $table.find("td.hotel_air_price > table:eq(1)").hide();
               $table.find("td.hotel_air_price > table:eq(0)").show();
               $table.find("input.startTime").removeAttr("disabled");
               $table.find("input.endTime").removeAttr("disabled");
               $table.find("input.days").removeAttr("disabled");
               var $option = $(this).find("option:selected");
               $table.find("input.startTime").val($option.attr("data-start") || '');
               $table.find("input.endTime").val($option.attr("data-end") || '');
               $table.find("input.days").val($option.attr("data-day") || '0');
               $table.find("select[name=peoplePrice]").val($("#currencyId").val());
            };
        }).on('change', 'input:checkbox.procurement', function () {
            // 內采 外采改变
            var text = $(this).attr("data-text");
            var checked = $(this).prop("checked");
            $table.find("div.pop_inner_outer").has(this).find("tr[data-type='" + text + "']")[checked ? "show" : "hide"]();
        }).on('click', "div.pop_inner_outer input.btn_confirm_inner_outer02", function (event) {
            // 选择控票数 确定
            var $div = $("div.pop_inner_outer").has(this);
            var sum = 0;
            $div.find("tr:visible td:last-child").each(function () {
                sum += (parseInt($(this).find("input").val()) || 0);
            });
            $("a.selLink").has(this).prev("input").val(sum);
            $div.hide();
            event.stopPropagation();
            ticketAmount();
        }).on("blur", "input.fkpNum", function () {
            // 非控票数失去焦点事件
            ticketAmount();
        }).on("blur", "input.kpNum", function () {
            // 控票数失去焦点事件
            ticketAmount();
        });
        
        
        //--------start--------初始化页面的游客类型
        var $travelerTypePrice = $table.find("#travelerTypePrice");
       	$travelerTypePrice.html('');
        
        var tbodyNode = $("<tbody/>");
        var res_2 = getSpaceLevelAjax('traveler_type','','');
        var res_3 = getSpaceLevelAjax('currency_list','','');
        res_2 && $(res_2.traveler_type).each(function(i,item){
         	var trNode2 = $("<tr/>");
     		var tdNode2_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").attr("data-text",item.name).attr("data-value",item.uuid).html(item.name+"同行<br/>（机+酒）价/人");
     		$(trNode2).append(tdNode2_1);
     		
 			var tdNode2_2 = $("<td/>").attr("style","text-align:left !important; width:70%;");
 			var spanNode_ = $("<span/>").attr("class","add_jbox_repeat_thj2");
 			var selectNode_ = $("<select/>").attr("name","peoplePrice").attr("class","w50_30 currency groupCurrency");
 			$(res_3.currency_list).each(function(k,m){
 				var optionNode_ = $("<option/>").val(m.id).text(m.currencyMark);
 				$(selectNode_).append(optionNode_);
 			});
 			$(spanNode_).append(selectNode_);
 			$(spanNode_).append($("<input/>").attr("type","text").attr("data-type","float").attr("class","inputTxt w50_30 babyPrice mr25"));
 			$(tdNode2_2).append(spanNode_);
 			$(trNode2).append(tdNode2_2);
     		
     		var tdNode2_3 = $("<td/>").text("---");
     		$(trNode2).append(tdNode2_3);
     		$(tbodyNode).append(trNode2);
     	});
     	
     	//动态 控票数
        var trNode3 = $("<tr/>");
        $(trNode3).append($("<td/>").attr("class","tr new_hotel_p_table2_tdblue").attr("style", "width:95px").text("控票数"));
        
       	var tdNode3_2 = $("<td/>").attr("class","tc").attr("style","width:215px !important");
       	$(tdNode3_2).append($("<input/>").attr("type","text").attr("data-type","number").attr("data-min","0").attr("class","inputTxt w50_30 spread mar_left_35 kpNum"));
    	$(trNode3).append(tdNode3_2);
     	
        $(trNode3).append($("<td/>"));   
        $(tbodyNode).append(trNode3);
     	//动态 非控票数
        var trNode4 = $("<tr/>");
        var tdNode4_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("非控票数");
        $(trNode4).append(tdNode4_1);
        var tdNode4_2 = $("<td/>").append($("<input/>").attr("type","text").attr("data-type","number").attr("data-min","0").attr("class","inputTxt w50_30 spread mar_left_35 fkpNum"));
        $(trNode4).append(tdNode4_2);
        
     	var tdNode4_3 = $("<td/>");
        $(trNode4).append(tdNode4_3);   
        $(tbodyNode).append(trNode4);
     	//动态 票数总计
     	var trNode5 = $("<tr/>");
        var tdNode5_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("票数总计");
        $(trNode5).append(tdNode5_1);
        var tdNode5_2 = $("<td/>");
        $(trNode5).append(tdNode5_2);
        
        var tdNode5_3 = $("<td/>");
        $(trNode5).append(tdNode5_3);   
        $(tbodyNode).append(trNode5); 
     	//动态 余位
     	var trNode6 = $("<tr/>");
        var tdNode6_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("余位");
        $(trNode6).append(tdNode6_1);
        var tdNode6_2 = $("<td/>");
        $(trNode6).append(tdNode6_2);
        
        var tdNode6_3 = $("<td/>");
        $(trNode6).append(tdNode6_3);
        $(tbodyNode).append(trNode6);
        
     	$travelerTypePrice.append(tbodyNode);
     	//--------end--------初始化页面的游客类型
     	
        
        $(document).click(function (event) {
            if (!$table.find("div.pop_inner_outer").has(event.target).length && !$table.find("div.pop_inner_outer").is(event.target)) {
                $table.find("div.pop_inner_outer").hide();
            }
        });
        $table.find("input:checkbox.procurement").change();

        $table.find('tr.houseTypeTr:not(.mealTypeTr) select').change();
        // 房型模板
        $trHouseType = $table.find("tr.houseTypeTr").clone();
        $trHouseType.find('a.addHouseType').replaceWith('<a class="ydbz_x gray delHouseType">删除</a>');
        
    }

    function initData(data) {
        if (!data) return;
        var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
        var rIndex = 0;
        var $td;
        // 团号
        $dataTrs.eq(rIndex).find("input").val(data.no);
        rIndex++;
        // 日期
        $dataTrs.eq(rIndex).find("input").val(data.date);
        rIndex++;
        // 房型
        $.each(data.houseTypes, function (i) {
            if (i > 0) {
                $dataTrs.find("a.addHouseType").click();
            }
            var $lastTr = $('#jbox_tq tr.houseTypeTr:not(.mealTypeTr):last');
            $lastTr.find("select:eq(0)").val(this.houseType.value).change();
            $lastTr.find("input").val(this.night);
            rIndex++;
            if (this.baseMealTypes && this.baseMealTypes.length) {
                $.each(this.baseMealTypes, function (j) {
                    if (j > 0) {
                        $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
                        $dataTrs.find("a.addMealType").last().click();
                    }
                    $lastTr = $('#jbox_tq tr.mealTypeTr:last');
                    $lastTr.find("select:eq(0)").val(this.mealType.value).change();
                    if (this.upMealTypes && this.upMealTypes.length) {
                        $lastTr.find("input:checkbox").prop("checked", true).change();
                        $.each(this.upMealTypes, function (k) {
                            if (k > 0) {
                                $lastTr.find("a.addUpMealType").click();
                            }
                            $lastP = $('#jbox_tq p.upMealType:last');
                            $lastP.find("select:eq(0)").val(this.mealType.value);
                            $lastP.find("select:eq(1)").val(this.currency.value);
                            $lastP.find("input").val(this.price);
                        });
                    }
                    rIndex++;
                });
            } else {
                rIndex++;
            }
        });
        $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
        // 上岛方式
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        $.each(data.trafficWays, function () {
            $td.find("input:checkbox[data-text='" + this + "']").prop("checked", true);
        });
        // 单房差
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        $td.find("select:eq(0)").val(data.priceDiff.currency.value);
        $td.find("input").val(data.priceDiff.price);
        $td.find("select:eq(1)").val(data.priceDiff.unit.value);
        rIndex++;
        // 航空公司
        if(data.airline.value) {
            $td = $dataTrs.eq(rIndex).find("td:eq(1)");
            $td.find("select").val(data.airline.value).change();
        }
        // 航班号
        if(data.airline.flight.value) {
            $td = $dataTrs.eq(rIndex).find("td:eq(3)");
            $td.find("select").val(data.airline.flight.value).change();
        }
        rIndex++;
        // 起飞时间
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        $td.find("input").val(data.airline.flight.start);
        // 到达时间
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        $td.find("input:eq(0)").val(data.airline.flight.end);
        $td.find("input:eq(1)").val(data.airline.flight.days);
        rIndex++;
        // 价格
        var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:visible > tbody > tr");
        var prices = data.airline.prices;
        for (var i = 0; i < prices.length; i++) {
            var $tds = $trs.eq(i).find("td");
            // 6.29 价格结构修改
            var islandprice = prices[i].islandprice;
            for (var j = 0; j < islandprice.length; j++) {
                $td = $tds.eq(j + 1);
                $td.find("select:eq(0)").val(islandprice[j].currency.value);
                $td.find("input").val(islandprice[j].price);
            }
            // end
        }
        // 控票  非控票 
        for (var i = 0, l = data.airline.ctrlTickets.length; i < l ; i++) {
            $trs.eq(-4).children("td").eq(i + 1).find("input").val(data.airline.ctrlTickets[i]);
            $trs.eq(-3).children("td").eq(i + 1).find("input").val(data.airline.unCtrlTickets[i]);
        }
        ticketAmount();
        rIndex++;
        // 优先扣减
        $dataTrs.eq(rIndex).find("input:radio").eq(data.ctrlTicketPriority == 1 ? 0 : 1).attr("checked", "checked");
        rIndex++;
        // 预收/预报名
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        $td.find("input:eq(0)").val(data.predictCount);
        // 需交定金
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        $td.find("select").val(data.deposit.currency.value);
        $td.find("input:eq(0)").val(data.deposit.price);
        rIndex++;
        // 备注
        $dataTrs.eq(rIndex).find("textarea").val(data.comment);
    }

    function saveData(data) {
    	data = (data || {});
        var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
        var rIndex = 0;
        var $td;
        // 团号
        data.uuid =$dataTrs.eq(rIndex).find("input[name='islandGroupUuid']").val();
        data.no = $dataTrs.eq(rIndex).find("input").val();
        rIndex++;
        // 日期
        data.date = $dataTrs.eq(rIndex).find("input").val();

        rIndex++;
        // 房型
        data.houseTypes = [];
        var $tr = $dataTrs.eq(rIndex);
        while ($tr.is(".houseTypeTr")) {
            var obj = {
                houseType: {
                    value: $tr.find("select:eq(0)").val(),
                    text: $tr.find("select:eq(0) option:selected").text()
                },
                night: $tr.find("input:eq(0)").val()
            };
            rIndex++;
            obj.baseMealTypes = [];
            $tr = $dataTrs.eq(rIndex);
            while ($tr.is(".mealTypeTr")) {
                var item = {
                    mealType: {
                        value: $tr.find("select:eq(0)").val(),
                        text: $tr.find("select:eq(0) option:selected").text()
                    },
                    upMealTypes: []
                };
                $tr.find("td:eq(3):visible p").each(function () {
                    var $this = $(this);
                    item.upMealTypes.push({
                        mealType: {
                            value: $this.find("select:eq(0)").val(),
                            text: $this.find("select:eq(0) option:selected").text()
                        },
                        currency: {
                            value: $this.find("select:eq(1)").val(),
                            text: $this.find("select:eq(1) option:selected").text()
                        },
                        price: $this.find("input:eq(0)").val()
                    });
                });
                obj.baseMealTypes.push(item);
                rIndex++;
                $tr = $dataTrs.eq(rIndex);
            }
            data.houseTypes.push(obj);
        }
        // 上岛方式
        data.trafficWays = [];
        $dataTrs.eq(rIndex).find("td:eq(1) input:checkbox:checked").each(function () {
            data.trafficWays.push({text : $(this).attr('data-text'), value:$(this).val()});
        });
        // 单房差
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        data.priceDiff = {
            currency: {
                value: $td.find("select:eq(0)").val(),
                text: $td.find("select:eq(0) option:selected").text()
            },
            price: $td.find("input").val(),
            unit: {
                value: $td.find("select:eq(1)").val(),
                text: $td.find("select:eq(1) option:selected").text()
            }
        };
        rIndex++;
        // 航空公司
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        data.airline = {
            value: $td.find("select").val(),
            text: $td.find("select option:selected").text()
        };
        // 航班号
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        data.airline.flight = {
            value: $td.find("select").val(),
            text: $td.find("select option:selected").text()
        };
        rIndex++;
        // 起飞时间
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        data.airline.flight.start = $td.find("input").val();
        // 到达时间
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        data.airline.flight.end = $td.find("input:eq(0)").val();
        data.airline.flight.days = $td.find("input:eq(1)").val();
        rIndex++;
        // 舱位等级&价格&余位
        data.airline.spaceGrade = [];
        var $ths = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:visible > thead > tr >th");
        for (var i = 0, l = $ths.length - 2; i < l; i++) {
            var spaceGradeValue = $ths.eq(i + 1).attr("data-value");
            var spaceGradeText = $ths.eq(i + 1).text();
        	data.airline.spaceGrade.push({value:spaceGradeValue,text:spaceGradeText});
        }
        var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:visible > tbody > tr");
        data.airline.prices = [];
        data.airline.touristType = [];
        for (var i = 0, tl = $trs.length - 4; i < tl; i++) {
            var $tds = $trs.eq(i).find("td");
            var item = [];
            for (var j = 0, dl = $tds.length - 2; j < dl; j++) {
                $td = $tds.eq(j + 1);
                item.push({
                    currency: {
                        value: $td.find("select").val(),
                        text: $td.find("select option:selected").text()
                    },
                    price: $td.find("input").val()
                });
                if (j == 0) {
                    data.airline.touristType.push({value:$tds.eq(0).attr("data-value"),text:$tds.eq(0).attr("data-text")});
                }
            }
         // 6.29 价格结构修改
            data.airline.prices.push({ islandprice: item });
            //end
        }
        var $tds = $trs.eq(-4).children("td");
        data.airline.ctrlTickets = [];
        data.airline.unCtrlTickets = [];
        data.airline.remainTickets = [];
        for (var i = 0, l = $tds.length - 2; i < l; i++) {
            data.airline.ctrlTickets.push($tds.eq(i + 1).find("input:first").val());
            data.airline.unCtrlTickets.push($trs.eq(-3).find("td").eq(i + 1).find("input:first").val());
            data.airline.remainTickets.push($trs.eq(-1).find("td").eq(i + 1).text());
        }
        data.airline.amount = {
            remainTicket: $trs.eq(-1).find("td:last").text(),
            ticket: $trs.eq(-2).find("td:last").text(),
        };
        rIndex++;
        // 优先扣减
        data.ctrlTicketPriority = ($dataTrs.eq(rIndex).find("input:radio:first").attr("checked")=="checked"?1:2);
        rIndex++;
        // 预收
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        data.predictCount = $td.find("input:eq(0)").val();
        // 需交定金
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        data.deposit = {
            currency: {
                value: $td.find("select").val(),
                text: $td.find("select option:selected").text()
            },
            price: $td.find("input").val()
        };
        rIndex++;
        // 备注
        data.comment = $dataTrs.eq(rIndex).find("textarea").val();
        return data;
    }

    function ticketAmount() {
        var $trs = $("#jbox_tq td.hotel_air_price > table:visible > tbody > tr");
        var ctrlSum = 0, unCtrlSum = 0;

        var $tds = $trs.eq(-2).children("td:not(:first,:last)");
        $tds.each(function (i) {
            var ctrlNum = $trs.eq(-4).children("td").eq(i + 1).find('input:first').val();
            ctrlNum = parseInt(ctrlNum) || 0;
            var unCtrlNum = $trs.eq(-3).children("td").eq(i + 1).find('input:first').val();
            unCtrlNum = parseInt(unCtrlNum) || 0;
            $(this).text(ctrlNum + unCtrlNum);
            $trs.last().find("td").eq(i + 1).text(ctrlNum + unCtrlNum);
            ctrlSum += ctrlNum;
            unCtrlSum += unCtrlNum;
        });
        $trs.eq(-4).children("td:last").text(ctrlSum);
        $trs.eq(-3).children("td:last").text(unCtrlSum);
        $trs.eq(-2).children("td:last").text(ctrlSum + unCtrlSum);
        $trs.eq(-1).children("td:last").text(ctrlSum + unCtrlSum);
    }

    return function (options) {
        var boxHtml = $("#jbox_haidaoyou_fab").html();
        options || (options = {});
        options.isAdd = options.isAdd || !options.id;
    	
   		//新增团期弹框
   		$.jBox(boxHtml, {
            title: options.isAdd ? "新增团期信息项" : "修改团期信息项",
            id: "jbox_tq",
            buttons: (options.buttons || {
                '保存': 1
            }),
            submit: function (v, h, f) {
            	var validateFlag = true;
            	var $groupCode = h.find("input[name=groupCode]");
              	var $groupDate = h.find("input[name=groupDate]");
            	var $airlineComp = h.find("input[name=airlineComp]");
            	var groupCode = $groupCode.val();
            	
            	if(!options.isAdd){
					var no = options.data.no;
					if(no != ""){
						delete map[no];
					}
				}
            	
            	if($groupCode.val() == '') {
            		$.jBox.tip("团号不能为空！！！");
            		$groupCode.focus();
			 		return false;
            	}else if($groupDate.val() == ''){
			 		$.jBox.tip("日期不能为空！！！");
			 		$groupDate.focus();
			 		return false;
			 	}else if($airlineComp.val() == ''){
			 		$.jBox.tip("航空公司不能为空！！！");
			 		$airlineComp.focus();
			 		return false;
			 	}else if(groupCode in map) {
					$.jBox.tip("所增加团号不能相同！！！");
					$groupCode.focus();
			 		return false;
				}
            	//团号唯一性验证（和数据库中进行比对）
				$.ajax({
					type : "post",
					async : false,
					url : $ctx+"/activityIsland/checkedGroup",
					data : {
						"groupCode" : groupCode
					},
					success : function(data){
						if(data && data.message=="true"){
							$.jBox.tip("该团号已存在！！！");
							validateFlag = false;
						} else {
							var showType=$("#showType").val();
							map[groupCode] = 1;
		                }
					}
				});
            	
   				if(!validateFlag) {
   					return false;
   				}
                var data = saveData(options.data);
                return options.callback(data, options, {
                    v: v,
                    h: h,
                    f: f
                });
            },
            height: "560",
            width: 980,
            persistent: true
        });
       
        initEvent(options);
        if (options.id && options.data) {
            initData(options.data);
        }
    }
})();


function confirmBox(msg, callback) {
    top.$.jBox.confirm(msg, '系统提示', function (v) {
        if (v == 'ok') {
            return !callback || callback();
        }
    }, {
        buttonsFocus: 1
    });
    return false;
}

function infoBox(msg) {
    $.jBox.info(msg, '系统提示');
    return false;
}