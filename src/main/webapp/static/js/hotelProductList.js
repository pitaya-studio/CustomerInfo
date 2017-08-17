// JavaScript source code
var map={};
var addGroup_box = (function() {
	var pathName = window.document.location.pathname;
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 1);
	// var $ctx = projectName+"/a";

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
	    	   	url: $ctx+"/activityIsland/getOptions?"+Math.floor(Math.random() * ( 1000 + 1)),
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
		$dataTrs.eq(rIndex).find("input").val(data.no);
		rIndex++;
		// 日期
		if(typeof(data.date)!="undefined"){
			var d = new Date(data.date); 
			$dataTrs.eq(rIndex).find("input").val(formatToDate(d));
		}else{
			//$dataTrs.eq(rIndex).find("input").val(getLocalTime(data.date));
			$dataTrs.eq(rIndex).find("input").val("");
		}
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
		       	   	url: $ctx+"/activityIsland/getOptions?"+Math.floor(Math.random() * ( 100000 + 1)),
		       	   	data:{
		       			type:"baseMeal",
		       			uuid:roomUuid
		       		},
		       		dataType: "json",
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
                          	//去掉逗号后判断是否是整数，如果是只保留整数部分
    						var sc = $.trim(this.price);
    						sc=sc.replace(',','');
    						if(parseInt(sc)==sc){
    							sc=parseInt(sc)
    						}
                            $lastP = $('#jbox_tq p.upMealType:last');
                            $lastP.find("select:eq(0)").val(this.mealType.value);
                            $lastP.find("select:eq(1)").val(this.currency.value);
                            $lastP.find("input").val(sc);
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
		data.trafficWays && $.each(data.trafficWays, function() {
//			alert(this.trafficWaysval);
			$td.find("input:checkbox[value='" + this.trafficWaysval + "']").prop("checked", true);
		});
		rIndex++;
		// 参考航班
		$dataTrs.eq(rIndex).find("input").val(data.airlines);
		rIndex++;
		// 同行价
		$td = $dataTrs.eq(rIndex).find("td:eq(1)");
		data.tradePrices && $.each(data.tradePrices, function(i) {
		/*	$td.find("select").eq(i).val(this.currency.value);
			$td.find("input").eq(i).val(this.price);*/
			var currency = this.currency.value;
			var uuid = this.uuid;
			var price = this.price;
			//去掉逗号后判断是否是整数，如果是只保留整数部分
			var txj = $.trim(this.price);
			txj=txj.replace(',','');
			if(parseInt(txj)==txj){
				txj=parseInt(txj)
			}
			$.each($td.find("div"),function(i){
				if(uuid==$td.find("div").eq(i).attr("data-value")){
					$td.find("select").eq(i).val(currency);
					$td.find("input").eq(i).val(txj);
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
		//去掉逗号后判断是否是整数，如果是只保留整数部分
        var dfx = $.trim(data.priceDiff.price);
        dfx=dfx.replace(',','');
		if(parseInt(dfx)==dfx){
			dfx=parseInt(dfx)
		}
		$td.find("input").val(dfx);
		$td.find("select:eq(1)").val(data.priceDiff.unit&&data.priceDiff.unit.value);
		// 需交定金
		$td = $dataTrs.eq(rIndex).find("td:eq(3)");
		$td.find("select").val(data.deposit.currency.value);
		//去掉逗号后判断是否是整数，如果是只保留整数部分
        var dj = $.trim(data.deposit.price);
        dj=dj.replace(',','');
		if(parseInt(dj)==dj){
			dj=parseInt(dj)
		}
		$td.find("input").val(dj);
		rIndex++;
		// 备注
		$dataTrs.eq(rIndex).find("textarea").val(data.comment);
		//团期UUid
		rIndex++;
		$dataTrs.eq(rIndex).find("input").val(data.hotelGroupUuid);
		//酒店UUID
		rIndex++;
		$dataTrs.eq(rIndex).find("input").val(data.hotelUuid);
	}

	function saveData(data) {
		data = (data || {});
		var $dataTrs = $("#jbox_tq table.table_product_info > tbody > tr");
		var rIndex = 0;
		var $td;
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
				houseType : {
					value : $tr.find("select:eq(0)").val(),
					text : $tr.find("select:eq(0) option:selected").text()
				},
				night : $tr.find("input:eq(0)").val()
			};
			rIndex++;
			obj.baseMealTypes = [];
			$tr = $dataTrs.eq(rIndex);
			while ($tr.is(".mealTypeTr")) {
				var item = {
					mealType : {
						value : $tr.find("select:eq(0)").val(),
						text : $tr.find("select:eq(0) option:selected").text()
					},
					upMealTypes : []
				};
				$tr.find("td:eq(3):visible p").each(
						function() {
							var $this = $(this);
							item.upMealTypes.push({
								mealType : {
									value : $this.find("select:eq(0)").val(),
									text : $this.find(
											"select:eq(0) option:selected")
											.text()
								},
								currency : {
									value : $this.find("select:eq(1)").val(),
									text : $this.find(
											"select:eq(1) option:selected")
											.text()
								},
								price : $this.find("input:eq(0)").val()
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
		$dataTrs.eq(rIndex).find("td:eq(1) input:checkbox:checked").each(
				function() {
					// data.trafficWays.push($(this).attr('data-text'));
					data.trafficWays.push({
						trafficWaystext : $(this).attr('data-text'),
						trafficWaysval : $(this).attr('value')
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
				uuid : $this.attr("data-value"),
				type : $this.find("span").text(),
				currency : {
					value : $this.find("select").val(),
					text : $this.find("select option:selected").text()
				},
				price : $this.find("input").val().trim()
			});
		});
		rIndex++;

		// 控房间数
		data.ctrlRoom = (+$dataTrs.eq(rIndex).find("input:eq(0)").val()) || 0;
		// 控房的UUID 和 NUM
		data.ctrlRooms = [];
		$dataTrs.eq(rIndex).find("td:eq(1) > input[name='detailUuid']").each(
				function() {
					data.ctrlRooms.push({
						uuid : $(this).val(),
						num : $(this).next().val()
					});
				});

		// 非控房间数
		data.unCtrlRoom = (+$dataTrs.eq(rIndex).children("td:eq(3)").find(
				"input:eq(0)").val()) || 0;
		rIndex++;
		// 优先扣减
		data.ctrlRoomPriority = $dataTrs.eq(rIndex).find("input:radio:first")
				.prop("checked");
		rIndex++;
		// 单房差
		$td = $dataTrs.eq(rIndex).find("td:eq(1)");
		data.priceDiff = {
			currency : {
				value : $td.find("select:eq(0)").val(),
				text : $td.find("select:eq(0) option:selected").text()
			},
			price : $td.find("input").val(),
			unit : {
				value : $td.find("select:eq(1)").val(),
				text : $td.find("select:eq(1) option:selected").text()
			}
		}
		// 需交定金
		$td = $dataTrs.eq(rIndex).find("td:eq(3)");
		data.deposit = {
			currency : {
				value : $td.find("select").val(),
				text : $td.find("select option:selected").text()
			},
			price : $td.find("input").val()
		}
//		debugger
		rIndex++;
		// 备注
		data.comment = $dataTrs.eq(rIndex).find("textarea").val();

		rIndex++;
		data.hotelGroupUuid = $dataTrs.eq(rIndex).find("input").val();
		
		rIndex++;
		data.hotelUuid = $dataTrs.eq(rIndex).find("input").val();
		
		if(data.hotelUuid ==""){
			data.hotelUuid = $("#activityUuid").val();
		}
		
		return data;
	}
	
	
    /**
     * 团期新增
     * @param jsoninput
     */
    function SubmitAdd(jsoninput,statusStr,showType){
//    	alert(statusStr);
    	var url = $ctx+"/activityHotel/saveActivityHotelForGroup";   	
    	//$(target).attr("disabled", "disabled");
    	$(".jbox-button").attr("disabled", "disabled");
    	$.post(
    		url,
    		{	"jsonstr":jsoninput,
    			"statusStr":statusStr
    		},
    		function(data){
    			if(data.message=="1"){
    				$.jBox.tip("添加成功!");
    				setTimeout(function(){window.location.href=$ctx+"/activityHotel/activityHotelList?showType="+showType;},500);
    			}else if(data.message=="2"){
    				$.jBox.tip(data.error,'warning');
    				$(".jbox-button").removeAttr("disabled");
    			}else if(data.message=="3"){
    				$.jBox.tip(data.error,'warning');
    				$(".jbox-button").removeAttr("disabled");
    			}else{
    				$.jBox.tip('系统异常，请重新操作!','warning');
    				$(".jbox-button").removeAttr("disabled");
    			}
    		});
    }
    
    /**
     * 修改团期
     * 
     */
    
    
    function SubmitUpdate(jsoninput,statusStr,showType){
//    	alert(statusStr);
    	var url = $ctx+"/activityHotel/updateActivityHotelForGroup";   	
    	$(".jbox-button").attr("disabled", "disabled");
    	$.post(
    		url,
    		{	"jsonstr":jsoninput,
    			"statusStr":statusStr
    		},
    		function(data){
    			if(data.message=="1"){
    				$.jBox.tip("修改成功!");
    				setTimeout(function(){window.location.href=$ctx+"/activityHotel/activityHotelList?showType="+showType;},500);
    			}else if(data.message=="2"){
    				$.jBox.tip(data.error,'warning');
    				$(".jbox-button").removeAttr("disabled");
    			}else if(data.message=="3"){
    				$.jBox.tip(data.error,'warning');
    				$(".jbox-button").removeAttr("disabled");
    			}else{
    				$.jBox.tip('系统异常，请重新操作!','warning');
    				$(".jbox-button").removeAttr("disabled");
    			}
    		});
    }
    

	return function(options) {
//		debugger
		//判断弹出层团号是否可以修改如果点击新增可以修改，反之 
//		if(options.tag=="hotelproductgroup_copylink"||options.tag=="hotelproduct_copylink"){
//			$("#igroupcode").attr("readonly","readonly");
//		}else{
//			$("#igroupcode").removeAttr("readonly");
//		}
		var boxHtml = $("#jbox_hotel_add_product_fab").html();
		options || (options = {});
		options.isAdd = options.isAdd || !options.id;
		$.jBox(boxHtml,
				{
					title : options.isAdd ? "新增团期信息项" : "修改团期信息项",
					id : "jbox_tq",
					buttons : (options.buttons || {
						'保存' : 3
					}),
					submit : function(v, h, f) {
						var igroupcode = h.find("input").attr('id','igroupcode').val();
						var tdate = h.find("[name=tdate]").val();
						if(v!=3){
//							alert(tdate);
							if(tdate==''){
						 		$.jBox.tip("日期不能为空！！！");
						 		return false;
						 	}else if(igroupcode==null || igroupcode==''){
						 		$.jBox.tip("团号不能为空！！！");
						 		return false;
						 	}
						}
						/*if(igroupcode in map) {
							$.jBox.tip("所增加团号不能相同！！！");
					 		return false;
						}*/
						//团号唯一性验证（和数据库中进行比对）
						var showType=$("#showType").val();
							$.ajax({
								type:"post",
								url:$ctx+"/activityHotel/checkedGroup",
								data:{
									"groupCode":igroupcode
								},
								success:function(data){
//									alert(igroupcode);
//									var showType=$("#showType").val();
									if(v!=3&&data && data.message=="true"&&options.updateg!="updateg"&&igroupcode!=""&&typeof(igroupcode) != undefined){
										$.jBox.tip("该团号已存在！！！");
									} else {
										map[igroupcode] = 1;
										var data = saveData(options.data);
										if(v==3){
											//保存
											//团期列表复制并新增
											if(options.tag=="hotelproductgroup_copylink"||options.tag=="hotelproduct_copylink" ||options.tag=="hotelproduct_addlink"){
												data.hotelGroupUuid="";
												SubmitAdd(JSON.stringify(data),v,showType);
												//团期列表修改
											}else if(options.tag=="hotelproductgroup_updatelink"||options.tag=="hotelproduct_updatelink"){
												SubmitUpdate(JSON.stringify(data),v,showType);
											}
										}else{
											//提交
											//只有新增有提交,修改没有提交只有保存
											data.hotelGroupUuid="";
											SubmitAdd(JSON.stringify(data),v,showType);
											//window.location.href=$ctx+"/activityHotel/activityHotelList";
										}
										//提交
										//只有新增有提交,修改没有提交只有保存
//										data.hotelGroupUuid="";
//										SubmitAdd(JSON.stringify(data),v,showType);
										//window.location.href=$ctx+"/activityHotel/activityHotelList";
//										return options.callback(data, options, {
//											v : v,
//											h : h,
//											f : f
//										});
									}
								}
							});
						return false;
					},
					height : '560',
					width : 980
				});
		var obj = options.hotelUuid;
		var obj_island = options.islandwayUuid;
		var hol = $("#hotelUid").val(obj);
		var isl = $("#islandUid").val(obj_island);
		getAjaxSelects('roomtype',hol);
		getAjaxSelects('hotelrank',hol);
		getAjaxSelects('islandway',isl);
        getAjaxSelects('travelerTypeRelations',hol);
		initEvent();
		if (options.id && options.data) {
			initData(options.data);
		}
	}
})();

function confirmBox(msg, callback) {
	top.$.jBox.confirm(msg, '系统提示', function(v) {
		if (v == 'ok') {
			return !callback || callback();
		}
	}, {
		buttonsFocus : 1
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
 		$.jBox.tip("团号不能为空");
 		return false;
 	}
 	
	$.ajax({
		type:"post",
		url:g_context_url+"/activityHotel/checkedGroup",
		data:{
			"groupCode":$(obj).val()
		},
		success:function(data){
			if(data && data.message=="true"){
				$.jBox.tip("该团号已存在");
				validate = false;
			}
		}
	});
	
	if(!validate) {
		return false;
	}
	return true;
}
