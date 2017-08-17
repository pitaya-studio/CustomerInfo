// JavaScript source code
// 航空公司--航班号--起飞时间--到达时间联动数据结构
var map={};
function getJsonAirlineInfo(){
	var res;
	$.ajax({
		type: "POST",
		async:false,
	   	url: $ctx+"/activityIsland/getJsonAirlineInfo",
	   	data: {},
		dataType: "json",
	   	success: function(data){
	   		res = data;
	   	}
	});
	return res;
}
var airlines = getJsonAirlineInfo();
//查询舱位等级
function getSpaceLevelAjax(type,airlineCode,flightnum){
	var _data = {};
	var res = null;
	if(type=='space_level'){//舱位等级
		if(airlineCode==null || airlineCode=='' || flightnum==null || flightnum==''){
			return null;
		}
		_data={"type":type,"airlineCode":airlineCode,"flightnum":flightnum};
	}else if(type=='traveler_type'){
		_data={"type":type,"hotelUuid":airlineCode};
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
        var $airlineSel = $table.find("select.selAirline").empty();
        html = ["<option value='$'>请选择</option>"];
        for (var key in airlines) {
            html.push("<option value='", key, "'>", airlines[key].name, "</option>");
        }
        $airlineSel.append(html.join(''));
        //币种自动选择
        $table.find("select[name=peoplePrice]").val($("#currencyId").val());
        var $table = $("#jbox_tq table.table_product_info");
        // 房型模板 餐型模板 升级餐型模板

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
        	var $tr = $("tr.houseTypeTr").has(this);
        	 // 房型改变
        	var roomUuid = $(this).val(); 
            $.ajax({
        		type: "POST",
        	   	url: $ctx+"/activityIsland/getOptions?"+Math.floor(Math.random() * ( 1000 + 1)),
        	   	data:{
        			type:"baseMeal",
        			uuid:roomUuid,
        			islandUuid:""
        		},
        		dataType: "json",
        	   	success: function(data){
        	   		//弹出层基础餐型下拉列表option赋值 开始
        	   		
        	   		var $mealTypeSels = $tr.nextUntil("tr.houseTypeTr:not(.mealTypeTr), tr:not(.houseTypeTr)").children("td:nth-child(2)").find("select");
        	   		var baseMealTypes = data.baseMeal;
        	   		$mealTypeSels.empty();
//        	   		$mealTypeSels.append($("<option/>").val('').text('请选择'));
        	   		if(baseMealTypes!=undefined){
        	   			$(baseMealTypes).each(function(){
        	   				$mealTypeSels.append($("<option/>").val(this.uuid).text(this.mealName));
        	   			});
        	   		}
        	   		//弹出层基层餐型下拉列表option赋值 结束
        	   	}
        	});
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
                options = ["<option value=''>请选择</option>"];
                for (var key in airline.flights) {
                    var flight = airline.flights[key];
                    options.push("<option value='", key, "' data-start='", flight.start, "' data-end='", flight.end, "' data-day='", (flight.days || 0), "'>", flight.name, "</option>");
                }
            }
            $table.find("select.fltNo").empty().append(options.join('')).change();
        }).on('change', 'select.fltNo', function () {
        	var airlineCode = $("#jbox_tq table.table_product_info .selAirline").val();//二字码
     		var space_level = $("#jbox_tq table.table_product_info .fltNo").val();//航班号
            // 更改航班
            /*if ($(this).prop("selectedIndex") <= 0) {
                $table.find("td.hotel_air_price > table:eq(0)").hide();
                $table.find("td.hotel_air_price > table:eq(1)").show();
                $table.find("input.startTime").attr("disabled", "disabled").val('');
                $table.find("input.endTime").attr("disabled", "disabled").val('');
                $table.find("input.days").attr("disabled", "disabled").val('');
            } else {*/
            	//动态开始
            	var res_1 = getSpaceLevelAjax('space_level',airlineCode,space_level);
            	if(!res_1){
            		res_1 = {"space_level":[{"space_level":null}]}
            	}
            	//如果航班下只有一个舱位等级并且为空   则不再展示舱位等级
              	if(res_1!=null){
	              	var res_2 = getSpaceLevelAjax('traveler_type',options.hotelUuid,'');
	              	var res_3 = getSpaceLevelAjax('currency_list','','');
	              	//动态航班--余位部分  开始
	              	//动态 舱位等级
	              	var tableNode = $table.find("td.hotel_air_price > table:eq(0)");
	              	$(tableNode).html('');
	              	var trNode1 = $("<tr/>");
	              	$(trNode1).append($("<th/>").attr("style","width:112px").attr("class","tr new_hotel_p_table2_tdblue2 new_hotel_p_table2_tdblue2_mr20").text("舱位等级"));
	              	$(res_1.space_level).each(function(i,item){
	              		$(trNode1).append($("<th/>").attr("style","width:"+(671/($(res_1.space_level).length))+"px").attr("data-value",item.spaceLevel).text(item.space||'无'));
	              	});
	              	$(trNode1).append($("<th/>").attr("style","width:31").text("合计"));
	              	var theadNode = $("<thead/>").append(trNode1);
	              	$(tableNode).append(theadNode);
	              	//动态 同行价
	              	var tbodyNode = $("<tbody/>");
	              	$(res_2.traveler_type).each(function(i,item){
	                  	var trNode2 = $("<tr/>").attr("name",item.uuid);
	              		var tdNode2_1 = $("<td/>").attr("class","tr new_hotel_p_table2_tdblue").attr("data-text",item.name).attr("data-value",item.uuid).html(item.name+"同行<br/>（机+酒）价/人");
	              		$(trNode2).append(tdNode2_1);
	              		$(res_1.space_level).each(function(j,n){
	              			var tdNode2_2 = $("<td/>").attr("style","text-align:left !important; ").attr("name",item.uuid+j);
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
	                  $(trNode3).append($("<td/>").attr("class","tr new_hotel_p_table2_tdblue").text("控票数"));
	                  $(res_1.space_level).each(function(){
	                  	var tdNode3_2 = $("<td/>").attr("class","tc").attr("style","width:215px !important");
	                  	$(tdNode3_2).append($("<input/>").attr("type","text").attr("class","inputTxt w50_30 spread mar_left_35 kpNum"));
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
	              		var tdNode6_2 = $("<td/>").text(10);
	              		$(trNode6).append(tdNode6_2);
	              	 });
	                  var tdNode6_3 = $("<td/>").text(30);
	                  $(trNode6).append(tdNode6_3);   
	                  $(tbodyNode).append(trNode6); 
	                  $(tableNode).append(tbodyNode);
                 // }
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
        }).on("change", "input.fkpNum,input.kpNum", function () {
            // 非空票数失去焦点事件
            ticketAmount();
        });
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
    function initData(data,trId) {
        if (!data) return;
        var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
        var rIndex = 0;//行数
        var $td;
        // 团号
        $dataTrs.eq(rIndex).find("input[name='islandGroupUuid']").val(data.uuid);
        $dataTrs.eq(rIndex).find("input[name='NO']").val(data.no);

        rIndex++;
        // 日期
        $dataTrs.eq(rIndex).find("input").val(data.date);

        rIndex++;
        // 房型
        $.each(data.houseTypes, function (i) {
        	//debugger
        	if (i > 0) {
                $dataTrs.find("a.addHouseType").click();
            }
            var $lastTr = $('#jbox_tq tr.houseTypeTr:not(.mealTypeTr):last');
            $lastTr.find("select:eq(0)").val(this.houseType.value).change();
            
            var $tr = $("tr.houseTypeTr").has($lastTr.find("select:eq(0)"));
            
            //默认选择的 基础餐型
            var selectBaseMealTypes = this.baseMealTypes;
            
	       	// 房型改变
	       	var roomUuid = this.houseType.value; 
	           $.ajax({
	       		type: "POST",
	       	   	url: $ctx+"/activityIsland/getOptions?"+Math.floor(Math.random() * ( 100000 + 1)),
	       	   	data:{
	       			type:"baseMeal",
	       			uuid:roomUuid,
	       			islandUuid:""
	       		},
	       		dataType: "json",
	       		async: false,//同步获取基础餐型，避免数据绑定不成功
	       	   	success: function(data){
	       	   	//debugger
	       	   		//弹出层基础餐型下拉列表option赋值 开始
	       	   		var $mealTypeSels = $tr.nextUntil("tr.houseTypeTr:not(.mealTypeTr), tr:not(.houseTypeTr)").children("td:nth-child(2)").find("select");
	       	   		var baseMealTypes = data.baseMeal;
	       	   		$mealTypeSels.empty();
	       	   		$mealTypeSels.append($("<option/>").val('').text('请选择'));
	       	   		if(baseMealTypes!=undefined){
	       	   			$(baseMealTypes).each(function(){
	       	   				$mealTypeSels.append($("<option/>").val(this.uuid).text(this.mealName));
	       	   			});
	       	   		}
	       	   		$mealTypeSels.each(function(i,e){
	       	   			$(this).val(selectBaseMealTypes[i].mealType.value);
	       	   		});
	       	   		
	       	   		//弹出层基层餐型下拉列表option赋值 结束
	       	   	}
	       	});
            
            $lastTr.find("input").val(this.night);
            rIndex++;
            if(this.baseMealTypes && this.baseMealTypes.length){
            $.each(this.baseMealTypes, function (j) {
                if (j > 0) {
                    $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
                    $dataTrs.find("a.addMealType").last().click();
                }
                $lastTr = $('#jbox_tq tr.mealTypeTr:last');
                $lastTr.find("select:eq(0)").val(this.mealType.value).change();
                if (this.upMealTypes && this.upMealTypes.length) {
                	$lastTr.find("input:checkbox").prop("checked",true).change();
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
            }else{
            	rIndex++;
            }
        });
        $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
        // 上岛方式
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        $.each(data.trafficWays, function () {
            $td.find("input:checkbox[value='" + this.value + "']").prop("checked", true);
        });
        // 单房差
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        $td.find("select:eq(0)").val(data.priceDiff.currency.value);
        $td.find("input").val(data.priceDiff.price);
        $td.find("select:eq(1)").val(data.priceDiff.unit.value);
        rIndex++;
        // 航空公司
        $td = $dataTrs.eq(rIndex).find("td:eq(1)");
        $td.find("select").val(data.airline.value).change();
        // 航班号
        $td = $dataTrs.eq(rIndex).find("td:eq(3)");
        $td.find("select").val(data.airline.flight.value).change();
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
        /*var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:visible > tbody > tr");
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
        }*/
        var $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:visible > tbody");
        var prices = data.airline.prices;
        for (var i = 0; i < prices.length; i++) {
        	var islandprice = prices[i].islandprice;
            // 6.29 价格结构修改
        	for (var j = 0; j < islandprice.length; j++) {
        		var $td = $trs.find("tr[name='"+islandprice[j].t_uuid+"']").find('td[name='+(islandprice[j].t_uuid+j)+']');
	        	$td.find("select:eq(0)").val(islandprice[j].currency.value);
	        	$td.find("input").val(islandprice[j].price);
            }
            // end
        }
        // 控票  非控票 
        $trs = $dataTrs.eq(rIndex).find("td.hotel_air_price > table:visible > tbody >tr");
        for (var i = 0, l = data.airline.ctrlTickets.length; i < l ; i++) {
            $trs.eq(-4).children("td").eq(i + 1).find("input").val(data.airline.ctrlTickets[i]);
            $trs.eq(-3).children("td").eq(i + 1).find("input").val(data.airline.unCtrlTickets[i]);
        }
        ticketAmount();
        rIndex++;
        // 优先扣减
        $dataTrs.eq(rIndex).find("input:radio").eq(data.ctrlTicketPriority ? 0 : 1).attr("checked", "checked");
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
        data.no = $dataTrs.eq(rIndex).find("input[name='NO']").val();
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
        data.ctrlTicketPriority = $dataTrs.eq(rIndex).find("input:radio:first").prop("checked")=="true"?1:2;
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
    /**
     * 团期新增
     * @param jsoninput
     */
    function formSubmitAdd(jsoninput,showType){
    	var url = $ctx+"/activityIsland/saveActivityIslandGroup";   	
    	$.post(
    		url,
    		{"jsoninput":jsoninput},
    		function(data){
    			if(data.message=="1"){
    				$.jBox.tip("添加成功!");
    				setTimeout(function(){window.location.href=$ctx+"/activityIsland/islandProductList?showType="+showType;},500);
    			}else if(data.message=="2"){
    				$.jBox.tip("修改成功!");
    			}else if(data.message=="3"){
    				$.jBox.tip(data.error,'warning');
    				$(obj).attr("disabled", false);
    			}else{
    				$.jBox.tip('系统异常，请重新操作!','warning');
    				$(obj).attr("disabled", false);
    			}
    		});
    }
	/**
	 * 团期修改
	 * @param jsoninput
	 */
    function formSubmitEdit(jsoninput){
    	var url = $ctx+"/activityIsland/updateAcitivityIslandGroup";
    	$.post(
    		url,
    		{"jsoninput":jsoninput},
    		function(data){
    			if(data.message=="1"){
    				$.jBox.tip("添加成功!");
    			}else if(data.message=="2"){
    				$.jBox.tip("修改成功!");
    			}else if(data.message=="3"){
    				$.jBox.tip(data.error,'warning');
    				$(obj).attr("disabled", false);
    			}else{
    				$.jBox.tip('系统异常，请重新操作,没有返回啊!','warning');
    				$(obj).attr("disabled", false);
    			}
    		});
    }
    return function (options) {
    	options || (options = {});
        options.isAdd = options.isAdd || !options.id;
        $.ajax({
    		type: "POST",
    	   	url: $ctx+"/activityIsland/getOptions?date="+new Date(),
    	   	data:{
    			type:"room_upMeal_islandWay",
    			uuid:options.hotelUuid,
    			islandUuid:options.islandUuid
    		},
    		dataType: "json",
    	   	success: function(item){
    	   		//弹出层房型下拉列表option赋值 开始
    	   		var roomOptions = item.room;
    	   		var roomSelect = $("#jbox_haidaoyou_fab tr.houseTypeTr:first").find("select:eq(0)");
    	   		roomSelect.empty();
    	   		$(roomSelect).append($("<option/>").val('').text('请选择'));
    	   		if(roomOptions!=undefined){
    	   			$(roomOptions).each(function(){
    	   				$(roomSelect).append($("<option/>").val(this.uuid).text(this.roomName));
    	   			});
    	   		}
    	   		//弹出层房型下拉列表option赋值 结束
    	   		//弹出层升级餐型下拉列表option赋值 开始
    	   		var upMealOptions = item.upMeal;
    	   		var upMealSelect = $("#jbox_haidaoyou_fab tr.mealTypeTr:first td:eq(3)").find("select:eq(0)");
    	   		upMealSelect.empty();
    	   		upMealSelect.append($("<option/>").val('').text('请选择'));
    	   		if(upMealOptions!=undefined){
    	   			$(upMealOptions).each(function(){
    	   				$(upMealSelect).append($("<option/>").val(this.uuid).text(this.mealName));
    	   			});
    	   		}
    	   		//弹出层升级餐型下拉列表option赋值 结束
    	   		//上岛方式复选框赋值 开始
    	   		var islandWays = item.islandWay;
    	   		var islandTd = $("#jbox_haidaoyou_fab tr.islandTr").find("td:eq(1)");
    	   		islandTd.empty();
    	   		if(islandWays!=undefined){
    	   			$(islandWays).each(function(i,item){
    	   				islandTd.append($("<input/>").addClass("redio_martop_4").attr("type","checkbox").attr("data-text",item.label).val(item.uuid));
    	    	   		islandTd.append(item.label);
    	    	   		if(i<(islandWays.length-1)){
    	    	   			islandTd.append($("<span/>").addClass("mr25"));	
    	    	   		}
    	   			});
    	   		}
    	   		//上岛方式复选框赋值 结束
    	   		var boxHtml = $("#jbox_haidaoyou_fab").html(); 
    	   		$.jBox(boxHtml, {
    	            title: options.isAdd ? "新增团期信息项" : "修改团期信息项",
    	            id: "jbox_tq",
    	            buttons: (options.buttons || {'保存': 1}),
    	            submit: function (v, h, f) {
//    	            	var no = h.find("input[name=NO]").val();
    	              	var tdate = h.find("#tdate").val();
    	            	var tairlinenumb = h.find("#tairlinenumb").val();
//    	        		if(options.updateg == "updateg"){
//    						var rgroup = $("#rgroup").val();
//    						if(rgroup !=""){
//    							delete map[rgroup];
//    						}
//    					}
    	            	if(tdate==''){
    				 		$.jBox.tip("日期不能为空！！！");
    				 		return false;
    				 	}else if(tairlinenumb==""){
    				 		$.jBox.tip("航空公司不能为空！！！");
    				 		return false;
    				 	}
//    				 	else if(no in map) {
//    						$.jBox.tip("所增加团号不能相同！！！");
//    				 		return false;
//    					}
    	            	//团号唯一性验证（和数据库中进行比对）
//						$.ajax({
//							type:"post",
//							url:$ctx+"/activityIsland/checkedGroup",
//							data:{
//								"groupCode":no
//							},
//							success:function(data){
//							if(data && data.message=="true"){
//								$.jBox.tip("该团号已存在！！！");
//							} else {
							var showType=$("#showType").val();
//							map[no] = 1;
	    	            	var data = saveData(options.data);
	    	            	window.parent.window.jBox.close();

	    	            	//---团期uuid赋值 开始---
	    	            	//海岛游产品发布（新增、复制新增）、海岛游产品列表（复制并新增）、海岛游团期列表、海岛游产品新增列表（复制并新增）json串没有团期uuid
	    	                if(options.tag=="toSaveActivityIslandProduct" 
	    	                	|| options.tag=="toSaveActivityIslandProduct_copyLink" || options.tag=="IslandProductListProduct_addGroup"
	    	                		|| options.tag=="islandProductListGroup_copyLink" || options.tag=="islandProductListProduct_copyLink" || options.tag=="islandProductListProduct_addLink"){//新增
	    	                	data.uuid = '';
	    	                	data.status = v;
	    	                	if(data.status ==0){
	    	                		data.status = 3;
	    	                	}else if(data.status ==1){
	    	                		data.status =1;
	    	                	}
	    	                //海岛游团期列表（修改）、海岛游产品列表展开（修改）、海岛游产品（修改）json串有团期uuid
	    	                }else if(options.tag=="islandProductListGroup_updateLink" || options.tag=="islandProductListProduct_updateLink" || options.tag=="editActivityIslandProduct"){
	    	                	data.uuid = options.uuid;	
	    	                	data.status = v;
	    	                	if(data.status ==0){
	    	                		data.status = 3;
	    	                	}else if(data.status ==1){
	    	                		data.status =1;
	    	                	}
	    	                }
	    	                
	    	                //---提交 开始---
	    	                //海岛游团期列表（复制并新增）或者  海岛游产品列表展开（复制并新增） 即：海岛游团期（新增）
	    	                if(options.tag=="IslandProductListProduct_addGroup" 
	    	                	|| options.tag=="islandProductListGroup_copyLink" || options.tag=="islandProductListProduct_copyLink"||options.tag=="islandProductListProduct_addLink"){
	    	                	//列表项中的团期新增 增加
	    	                	data.islandUUid = options.activityislandUuid;
	    	                	formSubmitAdd(JSON.stringify(data),showType);
	    	                }
	    	                //海岛游团期列表（修改）或者  海岛游团期列表展开（修改）即：海岛游团期（修改）
	    	                if(options.tag=="islandProductListGroup_updateLink" || options.tag=="islandProductListProduct_updateLink"){
	    	                	data.islandUUid = options.activityislandUuid;
	    	                	formSubmitEdit(JSON.stringify(data));
	    	                }
	    	                //---提交 结束---
	    	                return options.callback(data, options, {
	    	                    v: v,
	    	                    h: h,
	    	                    f: f
	    	                });
					return false;
    	            },
    	            height: "560",
    	            width: 980
    	        });
    	        initEvent(options);
    	        if (options.data) {
    	            initData(options.data,options.uuid);
    	        }
    	   	}
    	});
        
    };
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


function checkedMassage(obj){
	var validate = true;
 	if($(obj).val()==null || $(obj).val()==''){
 		$.jBox.tip("团号不能为空！！！");
 		return false;
 	}
 	
	$.ajax({
		type:"post",
		url:$ctx+"/activityIsland/checkedGroup",
		data:{
			"groupCode":$(obj).val()
		},
		success:function(data){
			if(data && data.message=="true"){
				$.jBox.tip($(obj).val()+"，该团号已存在！！！");
				validate = false;
			}
		}
	});
	
	if(!validate) {
		return false;
	}
	return true;
}