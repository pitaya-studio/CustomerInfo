$(function () {
	
	var colspan = $("#modForm thead tr th").length;
	
	// 只有修改的时候才执行此方法
	$("#modForm .groupdetailNote").each(function(index, obj) {
		
		var priceHtml = "";
		var jsonVal = $("[name=priceJson]", $(this).parent()).val();
		
		if (!jsonVal || jsonVal == "") {
			
		} else {

			priceHtml += '<tr><td name="groupPriceTd" colspan="' + colspan + '">';//bug16435 td外加tr
			priceHtml += '<table id="pricePlanTable" name="pricePlanTable" class="table table-striped table-bordered table-condensed table-mod2-group psf_table_son"><thead>';
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
					var hotelAndhouseTypeHtml = "<td class='t1'><p>";
					var hotelLength = hotelArr.length;
					if (hotelLength && hotelLength != 0) {
						for (var j = 0; j < hotelArr.length; j++) {
							var hotelObj = hotelArr[j];
							var houseTypeObj = houseTypeArr[j];
							hotelAndhouseTypeHtml += "<p>";
							hotelAndhouseTypeHtml += "<span><label>酒店：</label></span>";
							hotelAndhouseTypeHtml += hotelObj.name;
							hotelAndhouseTypeHtml += "<span><label>房型：</label></span>";
							hotelAndhouseTypeHtml += houseTypeObj.name;
						}
					}
					hotelAndhouseTypeHtml += "</td>";
					priceHtml += hotelAndhouseTypeHtml;
					// 同行成人价
					priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span>" + json[i].thcr + "</td>";
					// 同行儿童价
					priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span>" + json[i].thet + "</td>";
					// 同行特殊人群价
					priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span>" + json[i].thts + "</td>";
					if ($("#activityKind").val() == 2) {
						// 直客成人价
						priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span>" + json[i].zkcr + "</td>";
						// 直客儿童价
						priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span>" + json[i].zket + "</td>";
						// 直客特殊人群价
						priceHtml += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + "</span>" + json[i].zkts + "</td>";
						
					}
					// 备注
					priceHtml += "<td class='tc'>" + json[i].remark + "</td>";

					priceHtml += "</tr>";
				}
			}
			priceHtml += "</tbody></table></td></tr>";
		}
		$(this).parent().after(priceHtml);
	});

	//$("#pricePlanTable tbody tr:even").find("td").css("background-color", "#ebccd1");
	//$("#pricePlanTable tbody tr:odd").find("td").css("background-color", "#d4e2ef");

});