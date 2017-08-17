// JavaScript source code

var addGroup_box = (function() {
	var pathName=window.document.location.pathname;
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//	var $ctx = projectName+"/a";
	function initEvent() {
		var $table = $("#jbox_tq table.table_product_info");
		// 房型模板
		var $trHouseType = $table.find("tr.houseTypeTr").clone();
		$trHouseType.find('a.addHouseType').replaceWith('<a class="ydbz_x gray delHouseType">删除</a>');
//		// 餐型模板
//		var $trMealType = $table.find("tr.mealTypeTr:first").clone();
//		$trMealType.find('a.addMealType').replaceWith('<a class="ydbz_x gray delMealType">删除</a>');
//		$trMealType.find('td:first').hide();
//		// 升级餐型模板
//		var $trUpMealType = $table.find("p.upMealType:first").clone();
//		$trUpMealType.find('a.addUpMealType').replaceWith('<a class="ydbz_x gray delUpMealType">删除</a>');

		$table.on('click', 'a.addHouseType', function() {
			// 添加房型
//			var $trHouseType = $table.find("tr.houseTypeTr").clone();
//			$trHouseType.find('a.addHouseType').replaceWith('<a class="ydbz_x gray delHouseType">删除</a>');
			var $trMealType = $table.find("tr.mealTypeTr:first").clone();
			$trMealType.find('a.addMealType').replaceWith('<a class="ydbz_x gray delMealType">删除</a>');
			$table.find("tr.houseTypeTr:last").after($trHouseType.clone());
		}).on('click', 'a.delHouseType', function() {
			// 删除房型
			var $tr = $table.find("tr.houseTypeTr").has(this);
			var $next = $tr.next();
			$tr.remove();
			while ($next.is(".mealTypeTr")) {
				$tr = $next;
				$next = $next.next();
				$tr.remove();
			}
		}).on('click', 'a.addMealType', function() {          
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
		}).on('click', 'a.delMealType', function() {
			// 删除餐型
			var $tr = $table.find("tr.mealTypeTr").has(this);
			var $firstTr = $tr.prevUntil("tr:not(.mealTypeTr)").first();
			var $firstTd = $firstTr.find("td:first");
			$firstTd.prop("rowspan", ($firstTd.prop("rowspan") || 1) - 1);
			$tr.remove();
		}).on('click', 'a.addUpMealType', function() {
//			// 升级餐型模板
//			var $trUpMealType = $table.find("p.upMealType:first").clone();
//			$trUpMealType.find('a.addUpMealType').replaceWith('<a class="ydbz_x gray delUpMealType">删除</a>');
//			// 添加升级餐型
//			$table.find("td").has(this).append($trUpMealType.clone());
	        // 添加升级餐型
            var $td = $table.find("td").has(this);
            var $pUpMealType = $td.find("p").has(this).clone();
            $pUpMealType.find("a.addUpMealType").replaceWith('<a class="ydbz_x gray delUpMealType">删除</a>');
            $pUpMealType.find("input").val('');
            $td.append($pUpMealType);
		}).on('click', 'a.delUpMealType', function() {
			// 删除升级餐型
			$table.find("p.upMealType").has(this).remove();
		}).on("change", "tr.mealTypeTr input:checkbox", function() {
			// 是否升级餐型
			var checked = $(this).prop("checked");
			$table.find("td").has(this).attr("colspan", checked ? 1 : 3);
			$table.find("td").has(this).nextAll("td")[checked ? "show" : "hide"]();
		}).on("click", "a.selLink", function(event) {
			// 选择控票数
			$(this).find("div.pop_inner_outer_hotel").show();
			event.stopPropagation();
		}).on('change', 'input:checkbox.procurement', function() {
			//
			var text = $(this).attr("data-text");
			var checked = $(this).prop("checked");
			$table.find("div.pop_inner_outer_hotel").has(this).find("tr[data-type='" + text + "']")[checked ? "show" : "hide"]();
		}).on('click', "div.pop_inner_outer_hotel input.btn_confirm_inner_outer02", function(event) {
			// 选择控票数 确定
			var $div = $("div.pop_inner_outer_hotel").has(this);
			var sum = 0;
			$div.find("tr:visible td:last-child").each(function() {
				sum += (parseInt($(this).find("input").val()) || 0);
			});
			var detailUuid="";
			var detailNum ="";
			var html=[];
			$div.find("tr:visible").each(function() {
				if(!isNaN(parseInt($(this).find("input").val()))){
					html.push('<input class="detailU" type="hidden" name="detailUuid" value="'+$(this).find("span").text()+'"/>');
					html.push('<input class="detailU" type="hidden" name="detailNum"  value="'+$(this).find("input").val()+'"/>');
				}	
			});
			
			$table.find("a.selLink").parent().append(html.join(''));
			$("a.selLink").has(this).prev("input").val(sum);
			$div.hide();
			event.stopPropagation();
		}).on('change', "tr.houseTypeTr:not(.mealTypeTr) select", function () {
			var $tr = $("tr.houseTypeTr").has(this);
			//房型的uuid
			var roomUuid = $(this).val();
			
			$.ajax({
	    		type: "POST",
	    	   	url: $ctx+"/activityIsland/getOptionsHotel?"+Math.floor(Math.random() * ( 1000 + 1)),
	    	   	data:{
	    			type:"baseMeal",
	    			uuid:$(this).val()
	    		},
	    		dataType: "json",
	    	   	success: function(data){
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
	    	   		$mealTypeSels.change();
	    	   		//弹出层基层餐型下拉列表option赋值 结束
	    	   	}
			});

        });
		$(document).click(function(event) {
			if (!$table.find("div.pop_inner_outer").has(event.target).length && !$table.find("div.pop_inner_outer").is(event.target)) {
				$table.find("div.pop_inner_outer").hide();
			}
		});
		$table.find("input:checkbox.procurement").change();
	}
	

	function initData(data) {
		if (!data) return;
		var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
		var rIndex = 0;
		var $td;
		// 团号
		$dataTrs.eq(rIndex).find("input").val(data.NO);
		rIndex++;
		// 日期
		$dataTrs.eq(rIndex).find("input").val(data.date);
		rIndex++;
		// 房型
		$.each(data.houseTypes, function(i) {
			if (i > 0) {
				$dataTrs.find("a.addHouseType").click();
			}
			var $lastTr = $('#jbox_tq tr.houseTypeTr:not(.mealTypeTr):last');
//			alert(this.houseType.value);
			$lastTr.find("select:eq(0)").val(this.houseType.value);
			   var $tr = $("tr.houseTypeTr").has($lastTr.find("select:eq(0)"));
	            
	            //默认选择的 基础餐型
	            var selectBaseMealTypes = this.baseMealTypes;
	            
		       	// 房型改变
		       	var roomUuid = this.houseType.value; 
		           $.ajax({
		       		type: "POST",
		       	   	url: $ctx+"/activityIsland/getOptionsHotel?"+Math.floor(Math.random() * ( 100000 + 1)),
		       	   	data:{
		       			type:"baseMeal",
		       			uuid:roomUuid
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
		$.each(data.trafficWays, function() {
//			alert(this.trafficWaysval);
			$td.find("input:checkbox[value='" + this.trafficWaysval + "']").prop("checked", true);
		});
		rIndex++;
		// 参考航班
		$dataTrs.eq(rIndex).find("input").val(data.airlines);
		rIndex++;
		// 同行价
		$td = $dataTrs.eq(rIndex).find("td:eq(1)");
		$.each(data.tradePrices, function() {
		/*	$td.find("select").eq(i).val(this.currency.value);
			$td.find("input").eq(i).val(this.price);*/
			var currency = this.currency.value;
			var uuid = this.uuid;
			var price = this.price;
			$.each($td.find("div"),function(i){
				if(uuid==$td.find("div").eq(i).attr("data-value")){
					$td.find("select").eq(i).val(currency);
					$td.find("input").eq(i).val(price);
				}
			})
		});
		rIndex++;
		// 控房间数
		$dataTrs.eq(rIndex).find("input:eq(0)").val(data.ctrlRoom);
		// 非控房间数
		$dataTrs.eq(rIndex).children("td:eq(3)").find("input:eq(0)").val(data.unCtrlRoom);
		rIndex++;
		// 优先扣减
		$dataTrs.eq(rIndex).find("input:radio").eq(data.ctrlRoomPriority === "true" ? 0 : 1).attr("checked", "checked");
		rIndex++;
		// 单房差
		$td = $dataTrs.eq(rIndex).find("td:eq(1)");
		$td.find("select:eq(0)").val(data.priceDiff.currency.value);
		$td.find("input").val(data.priceDiff.price);
		$td.find("select:eq(1)").val(data.priceDiff.unit.value);
		// 需交定金
		$td = $dataTrs.eq(rIndex).find("td:eq(3)");
		$td.find("select").val(data.deposit.currency.value);
		$td.find("input").val(data.deposit.price);
		rIndex++;
		// 备注
		$dataTrs.eq(rIndex).find("textarea").val(data.comment);
	}

	function saveData(data) {
		data = (data || {});
		var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
		//取团期id
//		var $dataTrs2 = $("#contentTable table.activitylist_bodyer_table_new sea_rua_table > tbody > tr");
		var rIndex = 0;
		var $td;
		//取团期id
//	    alert($dataTrs2.eq(rIndex).attr("data-tag"));
//		data.hotelGroupUuid = $dataTrs.eq(rIndex).find("div").attr("data-tag");
		// 团号
		data.NO = $dataTrs.eq(rIndex).find("input").val();
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
				$tr.find("td:eq(3):visible p").each(function() {
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
		$dataTrs.eq(rIndex).find("td:eq(1) input:checkbox:checked").each(function() {
//			data.trafficWays.push($(this).attr('data-text'));
			
//			alert($(this).attr('data-text'));
			 data.trafficWays.push({
	            	trafficWaystext:$(this).attr('data-text'),
	            	trafficWaysval :$(this).attr('value')
	         });
		});
	  	rIndex++;
		// 参考航班
		data.airlines = $dataTrs.eq(rIndex).find("input").val();
		rIndex++;
		// 同行价
		data.tradePrices = [];
		$dataTrs.eq(rIndex).find("td:eq(1) > div").each(function() {
			var $this = $(this);
			data.tradePrices.push({
				uuid:$this.attr("data-value"),
				type: $this.find("span").text(),
				currency: {
					value: $this.find("select").val(),
					text: $this.find("select option:selected").text()
				},
				price: $this.find("input").val().trim()
			});
		});
		rIndex++;
		
		// 控房间数
		data.ctrlRoom = (+$dataTrs.eq(rIndex).find("input:eq(0)").val()) || 0;
		//控房的UUID 和  NUM 
		data.ctrlRooms = [];
		$dataTrs.eq(rIndex).find("td:eq(1) > input[name='detailUuid']").each(function(){
			data.ctrlRooms.push({
				uuid: $(this).val(),
				num : $(this).next().val()
			});
		});
		
		// 非控房间数
		data.unCtrlRoom = (+$dataTrs.eq(rIndex).children("td:eq(3)").find("input:eq(0)").val()) || 0;
		rIndex++;
		// 优先扣减
		data.ctrlRoomPriority = $dataTrs.eq(rIndex).find("input:radio:first").prop("checked");
		rIndex++;
		// 单房差
		$td = $dataTrs.eq(rIndex).find("td:eq(1)");
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
			}
			// 需交定金
		$td = $dataTrs.eq(rIndex).find("td:eq(3)");
		data.deposit = {
			currency: {
				value: $td.find("select").val(),
				text: $td.find("select option:selected").text()
			},
			price: $td.find("input").val()
		}
		rIndex++;
		// 备注
		data.comment = $dataTrs.eq(rIndex).find("textarea").val();
		
		var jsonStr = JSON.stringify(data);
        //json
        rIndex++;
        data.json = $dataTrs.eq(rIndex).find("input").val(jsonStr);
//        alert(jsonStr);
        return data;
	}

	return function(options) {
		
		var hotelUuid=$("#inputhotelUuid").val();
		options || (options = {});
		options.isAdd = options.isAdd || !options.id;
		getAjaxSelect('hotelrank',$("#inputhotelUuid"));
//		alert(options.islandUuid);
		 $.ajax({
	    		type: "POST",
	    	   	url: $ctx+"/activityIsland/getOptionsHotel?"+Math.floor(Math.random() * ( 1000 + 1)),
	    	   	data:{
	    			type:"room_upMeal_islandWay",
	    			uuid:hotelUuid,
	    			islandUuid:options.islandUuid
	    		},
	    		dataType: "json",
	    	   	success: function(item){
	    	   		//弹出层房型下拉列表option赋值 开始
//	    	   		debugger
	    	   		var roomOptions = item.room;
	    	   		var roomSelect = $("#jbox_hotel_add_product_fab tr.houseTypeTr:first").find("select:eq(0)");
	    	   		roomSelect.empty();
	    	   		$(roomSelect).append($("<option/>").val('').text('请选择'));
	    	   		var occuStr ="";
	    	   		if(roomOptions!=undefined){
	    	   			$(roomOptions).each(function(){
	    	   				if(this.occupancyRate!=null && this.occupancyRate!=""){
    	   						
    	   						occuStr = "("+this.occupancyRate+")";
    	   					}else{
    	   						occuStr="";
    	   					}
	    	   				
	    	   				$(roomSelect).append($("<option/>").val(this.uuid).text(this.roomName+occuStr));
	    	   			});
	    	   		}
	    	   		//弹出层房型下拉列表option赋值 结束
	    	   		//弹出层升级餐型下拉列表option赋值 开始
	    	   		var upMealOptions = item.upMeal;
	    	   		var upMealSelect = $("#jbox_hotel_add_product_fab tr.mealTypeTr:first td:eq(3)").find("select:eq(0)");
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
	    	   		var islandTd = $("#jbox_hotel_add_product_fab tr.islandTr").find("td:eq(1)");
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
	    	   		var boxHtml = $("#jbox_hotel_add_product_fab").html();
//	    	   		var boxHtml = $("#jbox_haidaoyou_fab").html();
	    	   		
	    	   		$.jBox(boxHtml, {
	    	            title: options.isAdd ? "新增团期信息项" : "修改团期信息项",
	    	            id: "jbox_tq",
	    	            buttons: (options.buttons || {
	    	                '保存': 1
	    	            }),
	    	            submit: function (v, h, f) {
	    	            	var tdate = h.find("#tdate").val();
	    	            	if(tdate==''){
	    				 		$.jBox.tip("日期不能为空！！！");
	    				 		return false;
	    				 	}
	    	                var data = saveData(options.data);
//	    	                formSubmit(data);
	    	                return options.callback(data, options, {
	    	                    v: v,
	    	                    h: h,
	    	                    f: f
	    	                });
	    	            },
	    	            height: "560",
	    	            width: 980
	    	        });
	    	   		//加载酒店下绑定的住客类型
	    	   		getAjaxSelect('travelerTypeRelations',$("#inputhotelUuid"));
	    	        initEvent(options);
	    	        if (options.id && options.data) {
	    	            initData(options.data,options.id);
	    	        }
	    	   	}
	    	});
	}
})();

function confirmBox(msg, callback) {
	top.$.jBox.confirm(msg, '系统提示', function(v) {
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