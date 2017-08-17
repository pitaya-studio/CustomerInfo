
//<!--基本信息-->
var ctx;
var areas = [];
var currentArea;
var currentCountry;
var currentIsland;
var currentHotel;
var suppliers=[];


var houseTypes;

var travelerTypes;
var hotelGuestTypeList;
var hotelRoomMap=[];

var islandWays;

var hotelMeals;
var baseInfo={};//基础信息的汇总

function initRoomTypePrice() {
    var $dvContainer_HotelRoomPrice = $("#dvContainer_HotelRoomPrice");
    var $tabHeader_HotelRoomPrice = $("#tabHeader_HotelRoomPrice");
    for (var i in hotelRoomMap) {
        var roomType = hotelRoomMap[i];
        var nightNumStr = "";
        if(roomType.roomNum != undefined && roomType.roomNum != null && roomType.roomNum != '') {
        	nightNumStr = "(" + roomType.roomNum + ")";
        }
        
        var liTheme = "<li hotel-room-uuid='" + roomType.hotelRoomUuid + "'>" + roomType.hotelRoomText + nightNumStr + "</li>";
        $tabHeader_HotelRoomPrice.append(liTheme);
        var tabContent_HotelRoomPriceTheme = "";
        tabContent_HotelRoomPriceTheme += '<div style="display: none;" name="tabContent_HotelRoomPrice" hotel-room-uuid="' + roomType.hotelRoomUuid + '" datarow-emptyRow-behavior="emptyRow"></div>';
        $dvContainer_HotelRoomPrice.append(tabContent_HotelRoomPriceTheme);
    }
    $dvContainer_HotelRoomPrice.trigger("modeChange");
    tabScroll($tabHeader_HotelRoomPrice.parent());
}
function initIslandWay() {
    var $dvContainer_IslandWay = $("#dvContainer_IslandWay");
    var $tabHeader_IslandWay = $("#tabHeader_IslandWay");
    for (var i in islandWays) {
        var islandWay = islandWays[i];
        var liTheme = "<li island-way-uuid='" + islandWay.uuid + "'>" + islandWay.label + "</li>";
        $tabHeader_IslandWay.append(liTheme);
        var tabContent_IslandWayTheme = "";
        tabContent_IslandWayTheme += '<div style="display: none;" name="tabContent_IslandWay" island-way-uuid="' + islandWay.uuid + '" datarow-emptyRow-behavior="emptyRow"></div>';
        $dvContainer_IslandWay.append(tabContent_IslandWayTheme);
    }
    $dvContainer_IslandWay.trigger("modeChange");
    tabScroll($tabHeader_IslandWay.parent());
}
function initAdditionalMeal() {
    var $tabHeader_BaseMeal = $("#tabHeader_BaseMeal");
    for (var i in hotelMeals) {
        var baseMeal = hotelMeals[i];
        var liTheme = "<li base-meal-uuid='" + baseMeal.uuid + "'>" + baseMeal.mealName + "</li>";
        $tabHeader_BaseMeal.append(liTheme);
        var tabContent_BaseMealTheme = '<div name="tabContent_BaseMeal"  base-meal-uuid="' + baseMeal.uuid + '"  datarow-emptyRow-behavior="emptyRow" ></div>';
        $dvContainer_BaseMeal.append(tabContent_BaseMealTheme);
    }
    $dvContainer_BaseMeal.trigger("modeChange");
    tabScroll($tabHeader_BaseMeal.parent());
}
function initHolidayMeal(){
    var $theadOperation = $("#tbHolidayMeal [name='theadOperation']");
    for(var i in travelerTypes){
        $theadOperation.before(' <th>'+travelerTypes[i].name+'</th>');
    }
}
function initHolidayMeal(){
    var $theadOperation = $("#tbHolidayMeal [name='theadOperation']");
    for(var i in travelerTypes){
        $theadOperation.before(' <th>'+travelerTypes[i].name+'</th>');
    }
}
function initCurrency() {

	//将所有隐藏域的币种都进行初始化
	$(".currencyId").val(baseInfo.currencyId);
    $("[name=txtTaxAddValue][tax-type='3']").attr("amt-code", baseInfo.currencyMark);
    $("[name=txtTaxAddValue][tax-type='3']").addClass("amt");
    $("[name=txtTaxAddValue][tax-type='4']").attr("amt-code", baseInfo.currencyMark);
    $("[name=txtTaxAddValue][tax-type='4']").addClass("amt");
    
    $("#txtChangePrice").attr("amt-code", baseInfo.currencyMark);
    $("#txtChangePrice").addClass("amt");

    $("#txtMixedPrice").attr("amt-code", baseInfo.currencyMark);
    $("#txtMixedPrice").addClass("amt");
    
    $("input[name=txtPreferentialPrice]").attr("amt-code", baseInfo.currencyMark);
    $("input[name=txtPreferentialPrice]").addClass("amt");

    resetCurrency();
}


//级联查询
function getAjaxSelect(type,obj){
	ctx = $("#hotelPlCtx").val();
	
	if($("input[name='areaType']:checked").val() == 1) {
		type="countryToHotel";
	}
	
	$.ajax({
		type: "POST",
	   	url: ctx + "/hotelControl/ajaxCheck",
	   	async: false,
	   	data: {
				"type":type,
				"uuid":$(obj).val()
			  },
		dataType: "json",
	   	success: function(data){
	   		if(type == "island"){
	   			$("#slIsland").empty();
	   			$("#slIsland").append("<option value=''>不限</option>");
	   			
	   			$("#slHotel").empty();
		   		$("#slHotel").append("<option value=''>不限</option>");
	   		} else if(type == "hotel"){
	   			$("#slHotel").empty();
		   		$("#slHotel").append("<option value=''>不限</option>");
		   		
		   		//add by majiancheng 20151110
            	var $input = $("#slHotel").next().find("input");
            	$input.data('ui-tooltip-title',$("#slHotel").find('option:selected').text());
                $input.tooltip('close');
                $input.val('');
                
	   		} else if(type=="countryToHotel") {
	   			$("#slHotel").empty();
		   		$("#slHotel").append("<option value=''>不限</option>");
	   		} else {
		   		$("#"+type).empty();
		   		$("#"+type).append("<option value=''></option>");
	   		}
	   		if(data){
	   			if(type=="hotel"){ 
		   			$.each(data.hotelList,function(i,n){
		   			     $("#slHotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   			});
		   			//基础信息海岛游部分赋值
		   			baseInfo.islandText = $(obj).find("option:selected").text();
		   			
		   			$("#slIsland").attr("title", baseInfo.islandText);
	   			}else if(type=="island"){
	   				$.each(data.islandList,function(i,n){
	   					 $("#slIsland").append($("<option/>").text(n.islandName).attr("value",n.uuid));
	   				});
	   				
	   				//add by majiancheng 20151028
	            	var $input = $("select[name='hotelUuid']").next().find("input");
	            	$input.data('ui-tooltip-title',$("select[name='hotelUuid']").find('option:selected').text());
	                $input.tooltip('close');
	                $input.val('');
	                
	                $("#hotel_hotelGroup").text("");
		            $("#hotel_hotelStar").text("");
		            $("#hotel_address").text("");
		            $("#hotel_tel").text("");
	   			} else if(type=="countryToHotel") {
	   				$.each(data.hotelList,function(i,n){
		   			     $("#slHotel").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
		   			});
	   			}
	   		}
	   	}
	});
}

$(document).ready(function () {
	//初始化酒店住客类型集合
	hotelGuestTypeList = $.parseJSON($("#hotelGuestTypes").val());
	travelerTypes = $.parseJSON($("#travelerTypes").val());
	
	var $slSupplier = $("#slSupplier");
    for (var i in suppliers) {
        $slSupplier.append("<option value='" + suppliers[i].supplierInfoId + "'>" + suppliers[i].supplierInfoText + "</option>");
    }
    $(document).on("change", "[name='area']", function () {
        var areaType = $("[name='area']:checked").attr("area-code");
        currentArea = getObjectByProp(areas, "areaType", areaType);
        var $country = $("#slCountry");
        $country.empty();
        for (var index in currentArea.countries) {
            var country = currentArea.countries[index];
            var optionTheme = "<option value='" + country.country + "'>" + country.countryText + "</option>";
            $country.append(optionTheme);
        }
        $country.trigger("change");
    });
    
    
    $(document).on("click", "#btnSaveOtherInfo", function (e) {
    	
    	var url = ctx + "/hotelPl/updateHotelPlMemo";
    	var hotelPlMemo = $("textarea[name=hotelPlMemo]").val();
    	$.ajax({
    		type: "POST",
    	   	url: url,
    	   	async: false,
    	   	data: {
    				"hotelPlMemo":hotelPlMemo,
    				"hotelPlUuid":baseInfo.uuid,
    			  },
    		dataType: "json",
    	   	success: function(data){
    	   		if(data){
    	   			if(data.result == 1) {
    	   				$.jBox.tip("酒店备注保存成功！");
    	   			} else if(data.result == 2) {
    	   				$.jBox.tip("酒店备注更新成功！");
    	   			} else if(data.result == 3) {
    	   				$.jBox.tip(data.message);
    	   			}
    	   		}
    	   	}
    	});
    	
    	saveButtonCli($(this));
    });
    
    $(document).on("click", "#btnSaveBaseInfo", function (e) {
        var $this = $(this);
        var status = $this.parents(".price_region:first").data("status");
        baseInfo.name = $("#txtPriceMealName").val();
        baseInfo.supplierInfoId = $("#slSupplier").val();
        baseInfo.purchaseType= $("[name='purchaseType']:checked").attr("purchase-type");
        baseInfo.areaType = $("[name='areaType']:checked").attr("areaType-code") || baseInfo.areaType;
        
        //添加价单验证信息
        if($.trim($("#txtPriceMealName").val())=="") {
        	$.jBox.tip("价单名称不能为空!");
        	$("#txtPriceMealName").focus();
        	return false;
        }
        
        if($.trim($("#txtPriceMealName").val())=="&nbsp;") {
        	$.jBox.tip("价单名称不能使用特殊字符!");
        	$("#txtPriceMealName").val('');
        	$("#txtPriceMealName").focus();
        	return false;
        }
        
        if($("#slSupplier").val()=="") {
        	$.jBox.tip("地接社供应商不能为空!");
        	$("#slSupplier").focus();
        	return false;
        }
        
        if($("#slCountry").val()=="") {
        	$.jBox.tip("国家不能为空!");
        	$("#slCountry").focus();
        	return false;
        }

        
        if((baseInfo.areaType == 2) && ($("#slIsland").val()=="")) {
        	$.jBox.tip("岛屿名称不能为空!");
        	$("#slIsland").focus();
        	return false;
        }
        if($("#slHotel").val()=="") {
        	$.jBox.tip("酒店名称不能为空!");
        	$("#slHotel").focus();
        	return false;
        }
        
        var flag = true;
        //第一次点击保存的时候，需要根据选择的基本信息初始化页面
        //以后的保存只能修改价单名称，地接社供应商，采购类型
        if (!status) {
        	
        	//唯一性校验
            $.ajax({
        		type: "POST",
        	   	url: ctx + "/hotelPl/findIsExist",
        	   	async: false,
        	   	data: {
        				"hotelUuid":$("#slHotel").val(),
        				"purchaseType":baseInfo.purchaseType,
        				"supplierInfoId":baseInfo.supplierInfoId
        			  },
        		dataType: "json",
        	   	success: function(validate){
        	   		ajaxStop();
        	   		if(validate){
        	   			if(validate.result == 0) {
        	   				$.jBox.tip(validate.message);
        	   				flag = false;
        	   				return false;
        	   			}
        	   		}
        	   	}
        	});
            if(flag) {
                var msg = '以上信息您已经核实无误了吗？<br/>为了方便您接下来的操作，点击"确定"后，“币种”、"国家"、"岛屿"及"酒店名称"将不可更改。';
                var isConfirm = confirmBox(msg, function () {
    				var url = ctx + "/hotelPl/saveBaseInfo";
    				ajaxStart();
    				
    				$.post(url,$("#baseInfoInputForm").serialize(),function(data){
    					ajaxStop();
    					if(data.result=="1"){
    						$.jBox.tip("保存成功!");
    						
    						baseInfo.uuid = data.hotelPlUuid;
    						
    						//基础信息保存后直接跳转到当前价单的修改页面---解决国家保存后还能修改的bug update by zhanghao
    						window.location.href=ctx + "/hotelPl/toUpdateHotelPl/"+baseInfo.uuid;
    						
    						$("input[name=hotelPlUuid]").val(data.hotelPlUuid);
    						hotelRoomMap = $.parseJSON(data.hotelRoomList);
    						islandWays = $.parseJSON(data.islandWays);
    						hotelMeals = $.parseJSON(data.hotelMeals);
    						houseTypes = $.parseJSON(data.houseTypes);
    						
    						//为关联酒店赋值
    						var relevancyHotels = $.parseJSON(data.relevancyHotels);
    						var html=[];
    						html.push('<option>请选择</option>');
    						$.each(relevancyHotels,function(index,item){
    							html.push('<option value="', item.uuid, '">', item.nameCn, '</option>');
    						});
    						$("#relevancyHotel").html(html.join('')).change();
    						
    						var $slCurrency= $("#slCurrency");
    		                baseInfo.currencyMark = $slCurrency.find("option:selected").attr("currency-mark");
    		                baseInfo.currencyText = $slCurrency.find("option:selected").text();
    		                baseInfo.currencyId = $slCurrency.val();
    		                $slCurrency.parent().append(baseInfo.currencyText);
    		                $slCurrency.remove();
    		                
    		                //为币种初始化
    		                $(".selectWithCurrMark").each(function(){
    		                	$(this).find("option[data-unitid=2]").each(function(){
    		                		$(this).attr("data-unit", baseInfo.currencyMark);
    		                	});
    		                });

    		                var $position = $("[name='position']:checked");
    		                baseInfo.position = $position.attr("position-code");
    		                $position.parent().parent().append($position.parent().text());
    		                $("[name='position']").parent().remove();

    		                var $slCountry = $("#slCountry");
    		                baseInfo.country = $slCountry.val();
    		                $slCountry.parent().append(baseInfo.countryText);
    		                $slCountry.remove();

    		                var $areaType =  $("[name='areaType']:checked");
    		                baseInfo.areaType = $areaType.attr("areaType-code");
    		                $areaType.parent().parent().append($areaType.parent().text());
    		                $("[name='areaType']").parent().remove();

    		                var $slIsland =  $("#slIsland");
    		                baseInfo.islandUuid = $slIsland.val();
    		                $slIsland.parent().append(baseInfo.islandText);
    		                $slIsland.remove();

    		                var $slHotel = $("#slHotel");
    		                baseInfo.hotelUuid =  $("#slHotel").val();
    		                $slHotel.parent().append(baseInfo.hotelText);
    		                $slHotel.remove();
    		                initRoomTypePrice();
    		                initIslandWay();
    		                initAdditionalMeal();
    		                initHolidayMeal();
    		                initCurrency();
    		                $("#priceMenuDetail").show();
    		                $this.addClass("save_button");
    		                var $region = $this.parents(".price_region:first");
    		                setRegionDisabled($region);
    		                $(".edit_button").removeClass("region_disabled");
    		                $this.addClass("region_disabled");
    						
    					}else if(data.result=="2"){
    						$.jBox.tip("修改成功!");
    					}else{
    						$.jBox.tip('操作异常！','warning');
    						flag = false;
    					}
    					
    			        if(flag) {
    			            saveButtonCli($this);
    			        }
    				});
                });
            }
        } else {
            var url = ctx + "/hotelPl/updatePlBaseInfo";
            
            $.ajax({
        		type: "POST",
        	   	url: url,
        	   	async: false,
        	   	data: {
        				"name":baseInfo.name,
        				"supplierInfoId":baseInfo.supplierInfoId,
        				"purchaseType":baseInfo.purchaseType,
        				"hotelPlUuid":baseInfo.uuid,
        				"areaType":baseInfo.areaType
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		ajaxStop();
        	   		if(data){
        	   			if(data.result == 1) {
        	   				$.jBox.tip("价单基本信息保存成功！");
        	   			} else if(data.result == 2) {
        	   				$.jBox.tip("价单基本信息更新成功！");
        	   			} else if(data.result == 3) {
        	   				$.jBox.tip(data.message);
        	   				flag = false;
        	   			}

        	   	        if(flag) {
        	   	            saveButtonCli($this);
        	   	        }
        	   		}
        	   	}
        	});
        }
        
    });
    
});
/**
 * 页面模式为编辑模式的时候，绑定基本信息
 * @param baseInfo 基本信息数据
 */
function bindBaseInfo(baseInfo){
	$("#txtPriceMealName").val(baseInfo.name);
    $("#slCurrency").parent().append(baseInfo.currencyText);
    $("#slCurrency").remove();

    $("[name='areaType'][areaType-code='"+baseInfo.areaType+"']").attr("checked",true);
    $("[name='areaType']").parent().parent().append($("[name='areaType']:checked").parent().text());
    $("[name='areaType']").parent().remove();

    $("#slCountry").parent().parent().append(baseInfo.countryText);
    $("#slCountry").parent().remove();
    if(baseInfo.areaType == 2){
        $("#slIsland").parent().append(baseInfo.islandText);
    } else if (baseInfo.areaType == 1){
    	$("#slIsland").parent().remove();
    }
    $("#slIsland").remove();
    $("#slHotel").parent().append(baseInfo.hotelText);
    $("#slHotel").remove();
    $("[name='position'][position-code='"+baseInfo.position+"']").attr("checked",true);
    $("[name='position']").parent().parent().append($("[name='position'][position-code='"+baseInfo.position+"']").parent().text());
    $("[name='position']").parent().remove();

    $("#hotel_hotelGroup").text(baseInfo.hotelGroup);
    $("#hotel_hotelStar").text(baseInfo.hotelStar);
    $("#hotel_address").text(baseInfo.hotelAddress);
    $("#hotel_tel").text(baseInfo.contactPhone);

    initCurrency();
    initRoomTypePrice();
    initIslandWay();
    initAdditionalMeal();
    initHolidayMeal();
    $("#priceMenuDetail").show();
    var $btnSave = $("#btnSaveBaseInfo");
    $btnSave.addClass("save_button");
    var $region = $btnSave.parents(".price_region:first");
    setRegionDisabled($region);
    $(".edit_button").removeClass("region_disabled");
    $btnSave.addClass("region_disabled");
}