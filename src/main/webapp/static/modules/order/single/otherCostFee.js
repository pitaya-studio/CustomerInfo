$(function(){
	// 添加其他费用时触发
	$("#traveler").delegate("a[name='addcost']", "click", function () {
		jbox_add_other_cost_pop($(this));
	});
});

/**
 * 添加其他费用弹出框
 * @param $this
 */
function jbox_add_other_cost_pop($this) {
	
	// 如果有其他费用，则展示在弹出框中
	addOtherCostInit($this);
	
	var $travler_other_cost_template = $this.closest("form").find(".add_other_cost");
	var $other_cost_div = $this.closest("form").find(".payfor-otherDiv");
	
	$pop = $.jBox($travler_other_cost_template.html(), {
			title : "其他费用",
			buttons : {
				'保存' : 1
			},
			loaded:function(){
                $(".jbox-content").css("max-height","400px")
            },
			submit : function (v, h, f) {
				if (v == "1") {
					// 保存触发操作
					var validateFlag = saveCost(h, $other_cost_div);
					if (!validateFlag) {
						return false;
					}
				}
			},
			width : 950
		});
}

// 初始化弹出框
function addOtherCostInit($this) {
	
	var $travler_other_cost_template = $this.closest("form").find(".add_other_cost");
	var $other_cost_div = $this.closest("form").find(".payfor-otherDiv");
	
	var costNames = $other_cost_div.find("input[name=costNames]").val();
	var costCurrencyIds = $other_cost_div.find("input[name=costCurrencyIds]").val();
	var costNums = $other_cost_div.find("input[name=costNums]").val();
	var costPrices = $other_cost_div.find("input[name=costPrices]").val();
	var businessTypes = $other_cost_div.find("input[name=businessTypes]").val();
	
	if (costNames && costCurrencyIds && costNums && costPrices) {
		$travler_other_cost_template.find("tbody").find("tr:not(:eq(0))").remove();
		var $tr = $travler_other_cost_template.find("tbody").find("tr:eq(0)");
		
		var costNameArr = costNames.split(";:;");
		var costCurrencyIdArr = costCurrencyIds.split(";:;");
		var costNumArr = costNums.split(";:;");
		var costPriceArr = costPrices.split(";:;");
		var businessTypeArr = businessTypes.split(";:;");
		
		for (var i = 0; i < costNameArr.length; i++) {
			var costName = costNameArr[i];
			var costCurrencyId = costCurrencyIdArr[i];
			var costNum = costNumArr[i];
			var costPrice = costPriceArr[i];
			var businessType = businessTypeArr[i];
			if (i == 0) {
				$tr.find(":input:eq(0)").attr("value", costName);
				// 把所有option的selected属性都置成false
				$tr.find("select option").attr("selected", false);
				$tr.find("select option[value=" + costCurrencyId +"]").attr("selected", true);
				$tr.find(":input:eq(2)").attr("value", costNum);
				$tr.find(":input:eq(3)").attr("value", costPrice);
				
				// 如果是审核通过后添加其他费用，则不能修改和删除
				if (businessType == "1") {
					$tr.find("select").attr("disabled", true);
					$tr.find(":input").attr("disabled", true);
					$tr.find("td:eq(5)").find("a:last").hide();
					$tr.attr("businessType", "1");
				} else {
					$tr.find("select").attr("disabled", false);
					$tr.find(":input").attr("disabled", false);
					$tr.find("td:eq(5)").find("a:last").show();
					$tr.attr("businessType", "0");
				}
			} else {
				var $tempTr = $tr.clone(this);
				$tempTr.find(":input:eq(0)").attr("value", costName);
				// 把所有option的selected属性都置成false
				$tempTr.find("select option").attr("selected", false);
				$tempTr.find("select option[value=" + costCurrencyId +"]").attr("selected", true);
				$tempTr.find(":input:eq(2)").attr("value", costNum);
				$tempTr.find(":input:eq(3)").attr("value", costPrice);
				$tempTr.find(".currency").text("");
				$tempTr.find(".result").text("");
				$tr.after($tempTr);
				
				// 如果是审核通过后添加其他费用，则不能修改和删除
				if (businessType == "1") {
					$tempTr.find("select").attr("disabled", true);
					$tempTr.find(":input").attr("disabled", true);
					$tempTr.find("td:eq(5)").find("a:last").hide();
					$tempTr.attr("businessType", "1");
				} else {
					$tempTr.find("select").attr("disabled", false);
					$tempTr.find(":input").attr("disabled", false);
					$tempTr.find("td:eq(5)").find("a:last").show();
					$tempTr.attr("businessType", "0");
				}
			}
		}
	} else {
		// 如果没有值则还原游客模板中列
		$travler_other_cost_template.find("tbody tr:not(:eq(0))").remove();
		$firstTr = $travler_other_cost_template.find("tbody tr:eq(0)");
		setTrDefaultValue($firstTr);
	}
	// 模拟币种change事件
	$travler_other_cost_template.find("select").each(function(index, obj) {
		$(this).trigger("change");
	});
}

//增加
function addone(obj) {
	$tr = $(obj).closest("tr");
	$tr.after($tr.clone(this));
	$lastTr = $tr.next();
	setTrDefaultValue($lastTr);
}
//删除
function deletethis(obj) {
	$(obj).parent().parent().remove();
}

function setTrDefaultValue($tr) {
	// 设置可以修改其他费用
	$tr.attr("businessType", "0");
	$tr.find("select").attr("disabled", false);
	$tr.find(":input").attr("disabled", false);
	$tr.find("td:eq(5)").find("a:last").show();
	// 设置默认值
	$tr.find("input").attr("value", "");
	$tr.find("input[name=num]").attr("value", "1");
	$("select", $tr).trigger("change");
}

// 值校验
function validateCost(costName, costNum, costPrice, costTotal) {
	var flag = true;
	// 如果费用名称为空，则提示错误
	if (!costName || costName == "") {
		top.$.jBox.tip("费用名称不能为空", 'warning');
		flag = false;
	}
	// 如果数量为空，则提示错误
	if (!costNum || costNum == "" || costNum <= 0) {
		top.$.jBox.tip("数量不能为空或小于0", 'warning');
		flag = false;
	}
	// 如果单价为空，则提示错误
	if (!costPrice || costPrice == "") {
		top.$.jBox.tip("单价不能为空", 'warning');
		flag = false;
	}
	// 总价不能大于一千万
	if (costTotal > 10000000) {
		top.$.jBox.tip("总价不能大于10000000", 'warning');
		flag = false;
	}
	return flag;
}

// 保存触发操作
function saveCost($travler_other_cost_template, $other_cost_div) {
	// 把保存值放到隐藏域中
	var costNames = "";
	var costCurrencyIds = "";
	var costNums = "";
	var costPrices = "";
	var costTotals = "";
	var businessTypes = "";
	var $tr = $travler_other_cost_template.find("tbody").find("tr");
	
	var validateFlag = true;
	
	$other_cost_div.find("tbody").empty();
	
	$tr.each(function(index, obj) {
		var costName = $(this).find(":input:eq(0)").val();
		var costCurrencyId = $(this).find("td:eq(1) select").val();
		var costCurrencyMark = $(this).find("td:eq(1) select").find("option:selected").attr("lang");
		var costCurrencyName = $(this).find("td:eq(1) select:selected").text();
		var costNum = $(this).find(":input:eq(2)").val();
		var costPrice = $(this).find(":input:eq(3)").val();
		var costTotal = $(this).find("td[name=result]").find(".result").text();
		var businessType = $(this).attr("businessType");
		
		// 校验费用名称、数量、单价是否为空
		var flag = validateCost(costName, costNum, costPrice, costTotal);
		if (!flag) {
			validateFlag = false;
			return false;
		}
		
		if (index != $tr.length - 1) {
			costNames += costName + ";:;";
			costCurrencyIds += costCurrencyId + ";:;";
			costNums += costNum + ";:;";
			costPrices += costPrice + ";:;";
			costTotals += costTotal + ";:;";
			businessTypes += businessType + ";:;";
		} else {
			costNames += costName;
			costCurrencyIds += costCurrencyId;
			costNums += costNum;
			costPrices += costPrice;
			costTotals += costTotal;
			businessTypes += businessType;
		}
		var html = "<tr businessType='" + businessType + "'>" +
						"<td class='t1'><div style='margin:0 5px 0 10px;word-break: break-all;'>" + costName + "</div></td>" +
						"<td class='t1'><div style='margin:0 5px;word-break: break-all;'>" +costNum + "</div></td>" +
						"<td class='tr'><div style='margin:0 5px;word-break: break-all;'>" + costCurrencyMark + costPrice + "</div></td>" +
						"<td class='tr'><div style='margin:0 10px;word-break: break-all;'>" + costCurrencyMark + " " + costTotal + "</div></tr>";
		$other_cost_div.find("tbody").append(html);
	});
	
	// 如果校验失败则返回，如果成功则执行下面方法
	if (validateFlag) {
		$other_cost_div.find("input[name=costNames]").val(costNames);
		$other_cost_div.find("input[name=costCurrencyIds]").val(costCurrencyIds);
		$other_cost_div.find("input[name=costNums]").val(costNums);
		$other_cost_div.find("input[name=costPrices]").val(costPrices);
		$other_cost_div.find("input[name=costTotals]").val(costTotals);
		$other_cost_div.find("input[name=businessTypes]").val(businessTypes);
		
		// 如果有保存其他费用，则添加其他费用改为编辑
		var firstCostName = $travler_other_cost_template.find("tbody").find("input:eq(0)").val();
		if (firstCostName && firstCostName != "") {
			$other_cost_div.show();
			$other_cost_div.parent().find("[name=addcost]").text("编辑其他费用");
		} else {
			$other_cost_div.hide();
			$other_cost_div.parent().find("[name=addcost]").text("添加其他费用");
		}
		
		// 触发价格更改事件
		changePayPriceByCostChange($other_cost_div.closest("form"));
		changeClearPriceByInputChange($other_cost_div.closest("form"));
		if ($("input[name=isForYouJia]").val() == "true") {
			if ($("#orderid").length == 0) {
				changeOtherTotalPrice($other_cost_div.closest("form"));
			}
		}
		if($("#priceType").val() == "2") {
			var html = $("span[name=clearPrice]", $other_cost_div.closest("form")).html().replace("（含服务费）", "");
			$("span[name=clearPrice]", $other_cost_div.closest("form")).html(html + "（含服务费）");
			$("span[name=inputClearPriceDiv]:not(:eq(0))", $other_cost_div.closest("form")).each(function(index, obj) {
				var html = $(this).html();
				if ($(this).parent().html().indexOf("+") == -1) {
					$(this).html("+" + html);
				}
			});
		}
	}
	return validateFlag;
}

/**
 * 改变其他费用数量时调用函数
 * @param obj
 */
function changeOtherCostNum(obj) {
	var money = obj.value;
	if (money == "") {
		obj.value="1"
	}
	var ms = obj.value.replace(/[^\d\.\-]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	var txt = ms.split(".");
	obj.value = stmp = txt[0]+(txt.length>1?"."+txt[1]:"");
	
	setTotalMoney(obj);
}

/**
 * 改变其他费用单价时调用函数
 * @param obj
 */
function changeOtherCostPrice(obj) {
	var money = obj.value;
	if (money == "") {
		obj.value="0"
	} else {
		if (obj.value.split("-").length >= 2) {
			if (obj.value.indexOf("-") == 0) {
				obj.value = "-" + obj.value.replace(/-/g, "");
			} else {
				obj.value = obj.value.replace(/-/g, "");
			}
		}
	}
	var ms = obj.value.replace(/[^\d\.\-]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	var txt = ms.split(".");
	obj.value = stmp = txt[0]+(txt.length>1?"."+txt[1]:"");
	
	setTotalMoney(obj);
}

/**
 * 改变其他费用币种时调用函数
 * @param obj
 */
function changeOtherCostCurrency(obj) {
	var currencyMark = $(obj).find("option:selected").attr("lang");
	$(obj).closest("tr").find(".currency").text(currencyMark);
	$tr = $(obj).closest("tr")
	setTotalMoney($tr);
}

/**
 * 获取费用总和
 * @param $tr
 */
function setTotalMoney(obj) {
	$tr = $(obj).closest("tr")
	var num = $tr.find("input[name=num]").val();
	if(num){
		num = num.replace("-", "");
		$tr.find("input[name=num]").val(num);
	}
	var price = $tr.find("input[name=price]").val();
	if (price == "-") {
		price = 0;
	}
	var totalMoney = Number(num) * Number(price);
	var currencyMark = $tr.find("option:selected").attr("lang");
	$tr.find(".currency").text(currencyMark);
	$tr.find(".result").text(totalMoney.toFixed(2));
}

/**
 * 添加其他费用到结算价
 * @param traveler
 * @param travelerPayPrice
 */
function addOtherCostFee(traveler, travelerPayPrice) {
	//获取其他费用
	var otherCostCurrencyIds = $(".payfor-otherDiv input[name=costCurrencyIds]", traveler).val();
	var otherCostTotals = $(".payfor-otherDiv input[name=costTotals]", traveler).val();
	if(otherCostCurrencyIds && otherCostTotals){
		var otherCostCurrencyIdArr = otherCostCurrencyIds.split(";:;");
		var otherCostTotalArr = otherCostTotals.split(";:;");
		for (var i = 0; i < otherCostCurrencyIdArr.length; i++) {
			var otherCostCurrencyId = otherCostCurrencyIdArr[i];
			var otherCostTotal= otherCostTotalArr[i];
			// 如果是quauq渠道报名，则其他费用要加上服务费
			if($("#priceType").val() == "2") {
				otherCostTotal = getTravelerChargeRate(otherCostCurrencyId, otherCostTotal);
			}
			travelerPayPrice[otherCostCurrencyId] += Number(otherCostTotal);
		}
	}
	return travelerPayPrice;
}

/**
 * 把其他费用组装成对象放入数组
 * @param $travlerForm
 * @param arr
 * @param travelerId
 */
function setOtherCostObjToArr($travlerForm, arr, travelerId) {
	var costNames = $travlerForm.find("input[name=costNames]").val();
	var costCurrencyIds = $travlerForm.find("input[name=costCurrencyIds]").val();
	var costNums = $travlerForm.find("input[name=costNums]").val();
	var costPrices = $travlerForm.find("input[name=costPrices]").val();
	var costTotals = $travlerForm.find("input[name=costTotals]").val();
	var businessTypes = $travlerForm.find("input[name=businessTypes]").val();
	
	if (costNames && costNames != "") {
		var costNameArr = costNames.split(";:;");
		var costCurrencyIdArr = costCurrencyIds.split(";:;");
		var costNumArr = costNums.split(";:;");
		var costPriceArr = costPrices.split(";:;");
		var costTotalArr = costTotals.split(";:;");
		var businessTypeArr = businessTypes.split(";:;");
		
		for (var i = 0; i < costNameArr.length; i++) {
			var costName = costNameArr[i];
			var costCurrencyId = costCurrencyIdArr[i];
			var costNum = costNumArr[i];
			var costPrice = costPriceArr[i];
			var costTotal = costTotalArr[i];
			var businessType = businessTypeArr[i];
			
			var costs = {};
			if (travelerId && travelerId != null) {
				costs.travelerId = travelerId;
			}
			costs.name = costName;
			costs.price = costPrice;
			costs.num = costNum;
			costs.currency = costCurrencyId;
			costs.sum = costTotal;
			costs.businessType = businessType;
			arr.push(costs);
		}
	} else {
		if (travelerId && travelerId != null) {
			var costs = {};
			costs.travelerId = travelerId;
			arr.push(costs);
		}
	}
}