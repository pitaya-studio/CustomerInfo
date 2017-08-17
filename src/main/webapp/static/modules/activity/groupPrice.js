$(window).load(function() {
//	$("[name=hotel]").comboboxInquiry();
//	$("[name=houseType]").comboboxInquiry();
});
$(function () {
	var groupPriceFlag = $("#groupPriceFlag").val();
	// 只有修改的时候才执行此方法
	if ($("#proId").length > 0 && groupPriceFlag  == "true") {
		$("#modTable tbody tr.noteTr").each(function(index, obj) {
			
			var colspan = 0;
			var firstSum = $("#modTable thead tr:eq(0) th").length;
			var secondSum = $("#modTable thead tr:eq(1) th").length;
			if (secondSum == 3) {
				colspan = firstSum + 2;
			} else {
				colspan = firstSum + 4;
			}
			//t1t2添加供应价
			colspan = colspan + 4;
			var priceHtml = "";
			var jsonVal = $("[name=priceJson]", this).val();
			
			if (!jsonVal || jsonVal == "") {
				priceHtml += '<td name="groupPriceTd" colspan="' + colspan + '" >' + $('#pricingTableTem').html() + '</td>';
			} else {
				priceHtml += '<td class="display-none" name="groupPriceTd" colspan="' + colspan + '">' + '<table id="pricePlanTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son"><thead>';
				priceHtml += $('#pricingTableTem thead').html() + '</thead><tbody>';
				
				var json = eval(jsonVal);
				
				// json数组个数
				var jsonLength = json.length;

				// 判断为空
				if (jsonLength && jsonLength != 0) {

					// 循环获取html组合
					for (var i = 0; i < json.length; i++) {

						// 序列值
						var indexVal = i + 1;
						priceHtml += "<tr>";
						priceHtml += "<td name='index'>" + indexVal + "</td>";

						// 酒店
						var hotelArr = json[i].hotel;
						// 房型
						var houseTypeArr = json[i].houseType;
						var hotelAndhouseTypeHtml = "<td class='t1'>";
						var hotelLength = hotelArr.length;
						if (hotelLength && hotelLength != 0) {
							for (var j = 0; j < hotelArr.length; j++) {
								var hotelObj = hotelArr[j];
								var houseTypeObj = houseTypeArr[j];
								hotelAndhouseTypeHtml += "<p>";
								hotelAndhouseTypeHtml += "<span><label>酒店：</label></span>";
								hotelAndhouseTypeHtml += "<input width='4%' type='text' name='hotel' class='pricing-scheme' maxlength='50' value='" + hotelObj.name + "'>";
								hotelAndhouseTypeHtml += "<span><label>房型：</label></span>";
								hotelAndhouseTypeHtml += "<input width='4%' type='text' name='houseType' class='pricing-scheme' maxlength='50' value='" + houseTypeObj.name + "'>";
								hotelAndhouseTypeHtml += "<span class='addAndRemove'><em class='add-select' name='addPricing'></em>";
								if (hotelLength > 1) {
									hotelAndhouseTypeHtml += "<em class='remove-selected' name='deletePricing' style='display: inline-block;'></em>";
								} else {
									hotelAndhouseTypeHtml += "<em class='remove-selected' name='deletePricing'></em>";
								}
								hotelAndhouseTypeHtml += "</span></p>";
							}
						}
						hotelAndhouseTypeHtml += "</td>";
						priceHtml += hotelAndhouseTypeHtml;
						// 同行成人价
						priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span><input width='4%' type='text' name='thcr' class='ipt-currency' data-type='amount' maxlength='8' value='" + json[i].thcr + "'></td>";
						// 同行儿童价
						priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span><input width='4%' type='text' name='thet' class='ipt-currency' data-type='amount' maxlength='8' value='" + json[i].thet + "'></td>";
						// 同行特殊人群价
						priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span><input width='4%' type='text' name='thts' class='ipt-currency' data-type='amount' maxlength='8' value='" + json[i].thts + "'></td>";
						if (secondSum == 6 || secondSum == 12) {
							// 直客成人价
							priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span><input width='4%' type='text' name='zkcr' class='ipt-currency' data-type='amount' maxlength='8' value='" + json[i].zkcr + "'></td>";
							// 直客儿童价
							priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span><input width='4%' type='text' name='zket' class='ipt-currency' data-type='amount' maxlength='8' value='" + json[i].zket + "'></td>";
							// 直客特殊人群价
							priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span><input width='4%' type='text' name='zkts' class='ipt-currency' data-type='amount' maxlength='8' value='" + json[i].zkts + "'></td>";
							
						}
						// 备注
						priceHtml += "<td class='tc'><input type='text' name='remark' class='nopadding' maxlength='50' value='" + json[i].remark + "'></td>";
						//操作
						priceHtml += "<td><em class='add-select' name='addPricingRow'></em>";
						if (json.length > 1) {
							priceHtml += "<em class='remove-selected' name='deletePricingRow' style='display: inline-block;'></em>";
						}
						priceHtml += "</td>";
						priceHtml += "</tr>";
					}
				}
				priceHtml += "</tbody></table></td>";
			}
			$(this).after(priceHtml);
		});
		
		//隐藏操作按钮
		$("#modTable .addAndRemove").hide();
		$("#modTable .add-select").hide();
		$("#modTable .remove-selected").hide();
	}
});

function groupPriceOpHide() {
	$(".addAndRemove").hide();
	$(".add-select").hide();
	$(".remove-selected").hide();
}

function groupPriceOpShow() {
	$(".addAndRemove").show();
	$(".add-select").show();
	$(".remove-selected").show();
}

function copyGroupPrice() {
	var remarkLength = $("#contentTable tbody .noteTr").length;
	var priceLength = $("#contentTable tbody [name=groupPriceTd]").length;
	if (remarkLength != priceLength) {
		$("#contentTable tbody:eq(0)").append($("#contentTable tbody [name=groupPriceTd]:eq(0)").clone(true));
		$("#contentTable tbody:eq(0) [name=expandPricing]:last").text($("#contentTable tbody:eq(0) [name=expandPricing]:first").text());
	}
	// 酒店房型
	if($("#contentTable tbody").children("tr[class!=noteTr]").size() > 1){
		var $target = $("#contentTable tbody:eq(0) [name=hotelhouse]:last");
		$target.after($("#contentTable tbody:eq(0) [name=hotelhouse]:first").clone(true));
		$target.remove();
	}
	
	$("td[name=groupPriceTd]").each(function(index, obj) {
		$this = $(this);
		$this.prev().prev().children(".tdCurrency").each(function(i, o) {
			$td = $(this);
			$("tr", $this).each(function(a, b) {
				$("td.tdCurrency", this).each(function(a, b) {
					$("[name=currencyMark]",this).text($td.children("span").text());
				});
			});
		});
	});
}

$(document).on('click', '[name="expandPricing"]', function () {
	var $this = $(this);
	var $tr = $this.parents('tr:first');
	//获取备注行
	var $noteTr = $tr.next();
	//此处数值应为表格列数
	if ($this.text() == '展开价格方案') {
		$this.text('收起价格方案');
		var colspan = 0;
		var firstSum = $("#contentTable thead tr:eq(0) th").length;
		var secondSum = $("#contentTable thead tr:eq(1) th").length;
		if (secondSum == 3) {
			colspan = firstSum + 2;
		} else {
			colspan = firstSum + 4;
		}
		//t1t2增加quauq同行价、供应价
		colspan = colspan + 4;
		if ($this.attr('show')) {
			if ($("#proId").length > 0) {
				if ($(this).parents("table#modTable").length > 0) {  // 原有团期
					if ($noteTr.next().next("td[name=groupPriceTd]").size() > 0) {						
						$noteTr.next().next("td[name=groupPriceTd]").removeClass("display-none");
					} else {
						top.$.jBox.info("无更多价格方案！","提示");
						$this.text('展开价格方案');
						return;
					}
				} else {
					if($noteTr.next("td[name=groupPriceTd]").size() > 0){						
						$noteTr.next("td[name=groupPriceTd]").removeClass("display-none");
						$noteTr.next("td[name=groupPriceTd]").show();
					} else {
						top.$.jBox.info("无更多价格方案！","提示");
						$this.text('展开价格方案');
						return;
					}
				}
			} else {
				if ($noteTr.next().next("td[name=groupPriceTd]").size() > 0) {					
					$noteTr.next().next("td[name=groupPriceTd]").removeClass("display-none");
				} else {
					top.$.jBox.info("无更多价格方案！","提示");
					$this.text('展开价格方案');
					return;
				}
			}
		} else {
			//判断是否已存在价格方案
			if ($this.attr('hasPricing') || $this.attr('show')) {  // 目前这个判断貌似不准确。
				// add by yang.jiang 如果没有TD，直接提示并中断展开动作
				if ($noteTr.next("td[name=groupPriceTd]").size() > 0) {
					$noteTr.next().show();
				} else {
					top.$.jBox.info("无更多价格方案！","提示");
					$this.text('展开价格方案');
					return;
				}
			} else {
				$this.attr('hasPricing', true);
				var $td = $('<td name="groupPriceTd"><input name="priceJson" value="" type="hidden"></td>')
											.prop('colspan', colspan).appendTo($('<tr></tr>')).insertAfter($noteTr);
				$td.append($('#pricingTableTem table').clone(true));
				$("td[name=groupPriceTd]").each(function(index, obj) {
					$this = $(this);
					$("td.tdCurrency", this).each(function(i, o) {
						$("[name=currencyMark]",this).text($this.prev().prev().children(".tdCurrency:eq(" + i + ")").children("span").text());
					});
				});
			}
		}
	} else {
		$this.text('展开价格方案');
		if ($this.attr('show')) {
			if ($("#proId").length > 0) {
				if ($(this).parents("table#modTable").length > 0) {
					$noteTr.next().next().addClass("display-none");
				} else {
					$noteTr.next().addClass("display-none");
				}
			} else {
				$noteTr.next().next().addClass("display-none");
			}
		} else {
			if($noteTr.next("td[name=groupPriceTd]").size() > 0){				
				$noteTr.next().hide();
			}
		}
	}
});
//增删价格方案
$(document).on('click', '[name="addPricing"]', function () {
	var $this = $(this);
	$this.next().css("display", "inline-block");
	var $pricingArea = $this.parents('p:first').clone();
	$pricingArea.find('input').val('');
	$this.parents('td:first').append($pricingArea);
})
$(document).on('click', '[name="deletePricing"]', function () {
	var $this = $(this);
	var $td = $this.parents('td:first');
	$this.parents('p:first').remove();
	if ($td.children().length == 1) {
		$td.find('[name="deletePricing"]').hide();
	}
});

$(document).on('click', '[name="addPricingRow"]', function () {
	var $this = $(this);
	$this.next().css("display", "inline-block");
	var $cloneTr = $('#pricingTableTem tbody tr').clone();
	$cloneTr.find('[name="deletePricingRow"]').css("display", "inline-block");
	$this.parents('tbody:first').append($cloneTr);
	renderIndex($this.parents('table:first'));
	$("td[name=groupPriceTd]").each(function(index, obj) {
		$this = $(this);
		$("td.tdCurrency", $cloneTr).each(function(i, o) {
			$("[name=currencyMark]",this).text($cloneTr.prev().children(".tdCurrency:eq(" + i + ")").children("span").text());
		});
	});
});

$(document).on('click', '[name="deletePricingRow"]', function () {
	var $this = $(this);
	var $tbody = $this.parents('tbody:first');
	$this.parents('tr:first').remove();
	if ($tbody.children('tr').length == 1) {
		$tbody.find('[name="deletePricingRow"]').hide();
	}
	renderIndex($tbody);
});
 
function renderIndex($tbody) {
	$("[name=index]", $tbody).each(function(index, obj) {
		$(this).text(index + 1);
	});
}

function groupPriceJosn() {
	$("[name=groupPriceTd] input[name=priceJson],tr.noteTr input[name=priceJson]").each(function(index, obj) {
		var $td;
		if ($(this).parents("tr.noteTr").length > 0) {
			$td = $(this).parents("tr.noteTr").next();
		} else {
			$td = $(this).parent();
		}
		if ($td.length > 0) {
			
			var priceArr = [];
			
			$("tbody tr", $td).each(function(a, b) {
				
				var $tr = $(this);
				
				var priceJson = {};
				
				var hotelArr = [];
				$("[name=hotel]", $tr).each(function(i, o) {
					var hotel = {};
//					var id = $(this).val();
//					var name = $("option:selected", this).text();
					var name = $(this).val();
//					hotel.id = id;
					hotel.id = 1;
					hotel.name = name;
					hotelArr.push(hotel);
				});
				priceJson.hotel = hotelArr;
				
				var houseTypeArr = [];
				$("[name=houseType]", $tr).each(function(i, o) {
					var houseType = {};
//					var id = $(this).val();
//					var name = $("option:selected", this).text();
					var name = $(this).val();
//					houseType.id = id;
					houseType.id = 1;
					houseType.name = name;
					houseTypeArr.push(houseType);
				});
				priceJson.houseType = houseTypeArr;
				
				priceJson.currencyId = $("#selectCurrency").val();
				priceJson.currencyMark = $("#selectCurrency option:selected").attr("id");
				priceJson.thcr = $("input[name=thcr]", $tr).val();
				priceJson.thet = $("input[name=thet]", $tr).val();
				priceJson.thts = $("input[name=thts]", $tr).val();
				priceJson.zkcr = $("input[name=zkcr]", $tr).val();
				priceJson.zket = $("input[name=zket]", $tr).val();
				priceJson.zkts = $("input[name=zkts]", $tr).val();
				priceJson.remark = $("input[name=remark]", $tr).val();
				priceArr.push(priceJson);
			});
			
			var priceStr = JSON.stringify(priceArr);
			$(this).val(priceStr);
		}
	});
}

/**
 * 添加酒店房型
 * @param obj
 */
function addHotelAndHouseType(obj){
	var $currentTr = $(obj).parents('tr:first');
	var $currentTd = $currentTr.find("td[name=hotelhouse]");
	var $currentDiv = $currentTd.find("div").has($(obj));
	if($currentDiv.find("em[class=remove-selected]").size() == 0){
		$currentDiv.append("<em class='remove-selected' onclick='delHotelAndHouseType(this)' style='display:inline-block;height: 19px;'></em>");
	}
	var $tempTd = $currentTd.clone();
	$tempTd.find("input").each(function(index, element){
		$(element).val("");
		$(element).attr("value","");
	});
	if($tempTd.find("div:first").find("em[class=remove-selected]").size() == 0){
		$tempTd.find("div:first").append("<em class='remove-selected' onclick='delHotelAndHouseType(this)' style='display:inline-block;'></em>");
	}
	var currentDiv = $tempTd.find("div:first").html();
	$currentTd.append("<div name='hotelhouseDiv'>" + currentDiv + "</div>");
}

/**
 * 移除酒店房型
 * @param obj
 */
function delHotelAndHouseType(obj){
	var $currentTd = $(obj).parents("td[name=hotelhouse]");
	var $currentDiv = $currentTd.find("div").has($(obj));
	// 判断如果是只剩一个，隐藏删除
	if ($currentDiv.siblings("div[name=hotelhouseDiv]").size() == 1) {
		$currentDiv.siblings("div[name=hotelhouseDiv]").find("em[class=remove-selected]").remove();
	}
	$currentDiv.remove();
}
