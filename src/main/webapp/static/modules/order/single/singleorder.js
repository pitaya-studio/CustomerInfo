$(function() {
	var activityKind = $("#activityKind").val();
	var priceJson = $("#priceJson").val();
	if(priceJson != null && priceJson != "") {
		addPriceJson2View(priceJson, activityKind);
	}

	var orderId = $("#orderid").val();
	if (orderId && orderId != "") {
		window.location.href = "../../orderCommon/manage/getOderInfoById?productorderById=" + orderId;
	}

	$("textarea[name=specialDemand]").focusin(function(){
		$(this).removeAttr("placeholder");
	}).focusout(function(){
		$(this).attr("placeholder","最多可输入500字");
	});


	// 第一步点击下一步 点击后验证表单通过后保存基本订单信息
	$("#oneToSecondStepDiv").click(function() {
		// 检查3种人群的价格
		if (disableInput4NullPrice()) {
			top.$.jBox.tip("获取团期价格失败，请更换渠道来源或使用其他团期", 'error');
			return false;
		}
		// 检查最大儿童人数
		var flag = checkMaxChildrenCount();
		if (!flag) {
			return false;
		}
		// 检查最大特殊人数
		var flag = checkMaxSpecialPeopleCount();
		if (!flag) {
			return false;
		}
		outerrorList = new Array();
		// 验证页面有效性和余位数检查
		//如果是补位订单，则不校验余位，最后保存时后台会校验
		var groupCoverId = $("#groupCoverId").val();
		var groupCoverNum = $("#groupCoverNum").val();
		if (!groupCoverId) {
			 if (!checkFreePosition()) {
				 return;
			 }
		} else {
			if (!validateCoverNum(groupCoverNum)) {
				return;
			}
		}


		//如果是游轮预定
		if(activityKind == "10"){
			var roomNumber = $("#roomNumber").val();
			var freePosition = $("#freePosition").val();
			if(Number(roomNumber) <= 0){
				top.$.jBox.tip('总计必须大于零', 'error');
				return false;
			}
			if(Number(roomNumber) > Number(freePosition)){
				top.$.jBox.tip('总计不能大于余位', 'error');
				return false;
			}
		}

		//验证订单联系人
		_doValidateorderpersonMesdtail();
//		createDivInDiv(outerrorList);
		if (outerrorList.length > 0) {
			return;
		}
		//只读
		readOnlyAllContactInfo();
		//验证直客价（当选择直客价并填写了相应的游客后）
		if($("#priceType").val()==1){
			validAdultSuggest(activityKind);
		} else {
			first2SecondStep();
		}

		if($("#priceType").val() == 1) {
			$(this).parents().find("#travelerTemplate").find(".peer").remove();
			$(this).parents().find("#travelerTemplate").find(".notPeer").show();
			$(this).parents().find("input[name='applyDiscount']").hide();
		} else if($("#priceType").val() == 0) {
			$(this).parents().find("#travelerTemplate").find(".peer").show();
			$(this).parents().find("#travelerTemplate").find(".notPeer").remove();
			$(this).parents().find("input[name='applyDiscount']").show();
		}
	});

	var submit_times = 0;
	//保存 保存并支付
	$("#thirdToFourthStepDiv").click(function() {	
		var currencyPrice = paramClearCurrenctPrice.join(",");
		if (currencyPrice.indexOf("-") > -1) {
			$.jBox.confirm("结算价出现负数，是否确认保存？", "提示", function(v, h, f){
				if (v == "ok") {
					canSaveOrder();
				}
			});
		} else {
			canSaveOrder();
		}
	});
	
	function canSaveOrder() {

		//防止多次提交
		if (submit_times == 0) {
			loading('正在提交，请稍等...');
			submit_times++;
		} else {
			top.$.jBox.info("您已提交，请耐心等待", "警告");
			return false;
		}
		
		//成本价信息
		var costCurrencyId = paramCurrencyId.join(",");
		var costCurrencyPrice = paramCurrenctPrice.join(",");
		//结算价信息
		var currencyId = paramClearCurrencyId.join(",");
		var currencyPrice = paramClearCurrenctPrice.join(",");
		// 如果是QUAUQ订单且是预报名产生订单，则需要把门店返还金额计算在结算价内
//		if ($("#differenceFlag").length > 0 && $("#differenceFlag").val() == "1") {
//			for (var i=0; i<paramClearCurrencyId.length; i++) {
//				var currencyId = $("input[name=adultCurrencyId]").val();
//	            if (paramClearCurrencyId[i] == currencyId) {
//	            	var price = $("#differenceMoney").val();
//	            	if (!price) {
//	            		price = 0;
//	            	}
//	            	paramClearCurrenctPrice[i] = Number(paramClearCurrenctPrice[i]) + Number(price);
//	            	currencyPrice = paramClearCurrenctPrice.join(",");
//	            }  
//	        }  
//		}
		
		var totalCurrencyId = currencyId;
		var totalCurrencyPrice = currencyPrice;
		// 20150812 预订返佣
		var rebatesCurrency = $("#rebatesCurrency").val(); // 预订团队返佣币种ID
		var rebatesMoney = $("#rebatesMoney").val(); // 预订团队返佣币值
		var ordercontacts = new Array();
		var contacttables = $("#ordercontact ul[name=orderpersonMes]");
		
		$.each(contacttables, function(key, valueout) {
			var contactinputs = $(valueout).find("input");
			var datacontact = {};
			$.each(contactinputs, function(key, value) {
				var _nametemp = $(value).attr("name");
				datacontact[_nametemp] = $(value).val();
			});
			contactinputs = $(valueout).find("textarea");
			$.each(contactinputs, function(key, value) {
				var _nametemp = $(value).attr("name");
				datacontact[_nametemp] = $(value).val();
			});
			ordercontacts.push(datacontact);
		});
		
		var newOrderFlag = $("#newOrderFlag").val(); // 新生成的订单的标识
		
		$.ajax({
			type : "POST",
			url : "../../orderCommon/manage/lastSave",
			data : {
				orderCompany : $("#orderCompany").val(),
				orderCompanyName : $("#orderCompanyName").val(),
				orderCompanyNameShow:$("#orderCompanyNameShow").val(),
				productOrderId : $("#orderid").val(),
                specialDemand : $('#specialDemand').val(),
				specialDemandFileIds : $("#fileIdList").val(),
                costCurrencyId:costCurrencyId,	//成本价信息
                costCurrencyPrice:costCurrencyPrice,
                currencyId : currencyId,	//结算价信息
                currenctPrice : currencyPrice,
                totalCharge : $("#totalCharge").val(),
                rebatesCurrency : rebatesCurrency,
                rebatesMoney : rebatesMoney,
                preOrderId : $("#preOrderId").val(),
                differenceMoney : $("#differenceMoney").val(),
				groupHandleId : $("#groupHandleId").val(),
				groupCoverId : $("#groupCoverId").val(),
				salerId:$("#salerId").val(),
				orderContactsJSON : JSON.stringify(ordercontacts),
				newOrderFlag : newOrderFlag
			},
			success : function(msg) {
				if (msg.errorMsg) {
					top.$.jBox.tip(msg.errorMsg, 'error');
					submit_times = 0;
					return false;
				} else {
					var payMode = msg.productOrder.payMode;
					var orderStatus = msg.productOrder.orderStatus;
					// payMode 1：订金占位 3：全款支付
					if(payMode == '1' || payMode == '3'){
						var payPriceType;
						if(payMode == '3'){
							payPriceType = 1;
						}else{
							payPriceType = 3;
							//如果是订金支付传递参数应该为订金
							currencyId = $("#payDepositCurrencyId").val();
							currencyPrice = Number($("#payDeposit").val()) * Number($("#orderPersonelNum").val());
						}
						var param = "orderId="+ msg.productOrder.id
						+ "&orderNum="+ msg.productOrder.orderNum
						+ "&payPriceType="+ payPriceType
						+ "&orderType=" + orderStatus
						+ "&businessType=1"
						+ "&isCommonOrder=yes"
						+ "&orderDetailUrl=" + $("#ctx").val() + "/orderCommon/manage/orderDetail/" + msg.productOrder.id
						+ "&agentId=" + $("#agentId").val()
						+ "&paramCurrencyId=" + currencyId
						+ "&paramCurrencyPrice=" + currencyPrice
						+ "&paramTotalCurrencyId=" + totalCurrencyId
						+ "&paramTotalCurrencyPrice=" + totalCurrencyPrice;
						window.location.href = "../../orderPay/pay?"+ param;
					}else{
						var childMenuId;
						if(orderStatus == 1){
							childMenuId = 418;
						}else if(orderStatus == 2){
							childMenuId = 419;
						}else if(orderStatus == 3){
							childMenuId = 420;
						}else if(orderStatus == 4){
							childMenuId = 421;
						}else if(orderStatus == 5){
							childMenuId = 422;
						}else if(orderStatus == 10){
							childMenuId = 748;
						}
						if (payMode == '7') {
							window.location.href = "../../orderList/manage/showOrderList/7/" + msg.productOrder.orderStatus + ".htm?_m=417&_mc=" + childMenuId + "&orderNumOrGroupCode="+ msg.productOrder.orderNum;
						} else {
							window.location.href = "../../orderList/manage/showOrderList/0/" + msg.productOrder.orderStatus + ".htm?_m=417&_mc=" + childMenuId + "&orderNumOrGroupCode="+ msg.productOrder.orderNum;
						}
					}
				}
			}
		});
	}

	// for 109 优惠 (保存并提交审批)
	$("#saveThenSubmit").click(function() {
		// 申请优惠的游客信息
		var applyTravelerIds = $("#applyTravelerIds").val();
		//成本价信息
		var costCurrencyId = paramCurrencyId.join(",");
		var costCurrencyPrice = paramCurrenctPrice.join(",");
		//结算价信息
		var currencyId = paramClearCurrencyId.join(",");
		var currencyPrice = paramClearCurrenctPrice.join(",");
		var totalCurrencyId = currencyId;
		var totalCurrencyPrice = currencyPrice;
		// 20150812 预订返佣
		var rebatesCurrency = $("#rebatesCurrency").val(); // 预订团队返佣币种ID
		var rebatesMoney = $("#rebatesMoney").val(); // 预订团队返佣币值
		$.ajax({
			type : "POST",
			url : "../../orderCommon/manage/saveOrderAndApplyReview",
			data : {
				orderCompany : $("#orderCompany").val(),
				orderCompanyName : $("#orderCompanyName").val(),
				productOrderId : $("#orderid").val(),
				specialDemand : $('#specialDemand').val(),
				costCurrencyId:costCurrencyId,	//成本价信息
				costCurrencyPrice:costCurrencyPrice,
				currencyId : currencyId,	//结算价信息
				currencyPrice : currencyPrice,
				rebatesCurrency : rebatesCurrency,
				rebatesMoney : rebatesMoney,
				groupHandleId : $("#groupHandleId").val(),
				applyTravelerIds : applyTravelerIds
			},
			success : function(data) {
				if(data.result == '1') {
					$.jBox.tip("申请成功！", "success");
					window.location.href = "../../orderList/manage/showOrderList/0/2?orderNumOrGroupCode=" + data.groupCode;
				} else {
					$.jBox.tip(data.msg, "warning");
				}
			}
		});
	});
});

// 299
function addPriceJson2View(priceJson, activityKind) {
	var json = eval(priceJson);
	// json数组个数
	var jsonLength = json.length;

	// 判断为空
	if (jsonLength && jsonLength != 0) {
		// 循环获取html组合
		for (var i = 0; i < json.length; i++) {
			// 序列值
			var indexVal = i + 1;
			var html = "<tr>";
			html += "<td name='index'>" + indexVal + "</td>";
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
					hotelAndhouseTypeHtml += "<p class='pricing'>";
					hotelAndhouseTypeHtml += "<label>酒店：</label>";
					hotelAndhouseTypeHtml += "<span class='ellipsis-text' title='" + hotelObj.name + "'>" + hotelObj.name + "</span>";
					hotelAndhouseTypeHtml += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					hotelAndhouseTypeHtml += "<label>房型：</label>";
					hotelAndhouseTypeHtml += "<span class='ellipsis-text' title='" + houseTypeObj.name + "'>" + houseTypeObj.name + "</span>";
					hotelAndhouseTypeHtml += "<p>";
				}
			}
			hotelAndhouseTypeHtml += "</td>";
			html += hotelAndhouseTypeHtml;
			// 同行成人价
			html += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + json[i].thcr + "</span></td>";
			// 同行儿童价
			html += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + json[i].thet + "</span></td>";
			// 同行特殊人群价
			html += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + json[i].thts + "</span></td>";
			if (json[i].zkcr) {
				// 直客成人价
				html += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + json[i].zkcr + "</span></td>";
				// 直客儿童价
				html += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + json[i].zket + "</span></td>";
				// 直客特殊人群价
				html += "<td class='tr tdCurrency'><span>" + json[i].currencyMark + json[i].zkts + "</span></td>";
			} else if(activityKind == '2') {
				html += "<td class='tr tdCurrency'></td><td class='tr tdCurrency'></td><td class='tr tdCurrency'></td>";
			}
			// 备注
			html += "<td class='tc'><span>" + json[i].remark + "</span></td>";

			html += "</tr>";
			//$("#test").append(html);
			$("table[name=pricePlanTable] tbody").append(html);
		}

	}
}


//112 特殊需求过滤特殊字符
String.prototype.replaceSpecialDemand = function(regEx){
	if (!regEx){
		regEx = /[\“\”\‘\’\"\'\<\>\\]/g;
	}
	return this.replace(regEx, '');
};

/**
 * 检查adult直客价
 */
function validAdultSuggest(activityKind){
	if($("#orderPersonNumAdult").val() > 0 && isAdultBlank()){
		if(activityKind == "10"){
			top.$.jBox.tip('1/2直客价为空不能报名！', 'error');
		}else{					
			top.$.jBox.tip('成人直客价为空不能报名！', 'error');
		}
		return false;
	} else if($("#orderPersonNumAdult").val() > 0 && Number($("input[name=suggestAdultPrice]").val()) == 0){
		if(activityKind == "10"){
			$.jBox.confirm("1/2直客价为"+$("#crbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
				if (v == 'ok') {
					validChildSuggest(activityKind);
				}
			});
		}else{
			$.jBox.confirm("成人直客价为"+$("#crbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
				if (v == 'ok') {
					validChildSuggest(activityKind);
				}
			});
		}
		return;
	} else {
		validChildSuggest(activityKind);
	}
}
/**
 * 检查child直客价
 */
function validChildSuggest(activityKind){
	if($("#orderPersonNumChild").val() > 0 && isChildBlank()){
		if(activityKind == "10"){
			top.$.jBox.tip('3/4直客价为空不能报名！', 'error');
		}else{					
			top.$.jBox.tip('儿童直客价为空不能报名！', 'error');
		}
		return false;
	} else if ($("#orderPersonNumChild").val() > 0 && Number($("input[name=suggestChildPrice]").val()) == 0){
		if(activityKind == "10"){
			$.jBox.confirm("3/4直客价为"+$("#etbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
				if (v == 'ok') {
					validSpecialSuggest();
				}
			});
		}else{
			$.jBox.confirm("儿童直客价为"+$("#etbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
				if (v == 'ok') {
					validSpecialSuggest();
				}
			});
		}
		return;
	} else {
		validSpecialSuggest();
	}
}
/**
 * 检查special直客价
 */
function validSpecialSuggest(){
	if($("#orderPersonNumSpecial").val() > 0 && isSpecialBlank()){
		top.$.jBox.tip('特殊人群直客价为空不能报名！', 'error');
		return false;
	} else if ($("#orderPersonNumSpecial").val() > 0 && Number($("input[name=suggestSpecialPrice]").val()) == 0){
		$.jBox.confirm("特殊人群直客价为"+$("#tsbzm").val()+"0.00, 是否确认报名？", "提示", function(v, h, f) {
			if (v == 'ok') {
				first2SecondStep();
			}
		});
		return;
	} else {
		first2SecondStep();
	}
}

/**
 * 主要执行保存等其他主要业务操作（校验主要在click中写，当点击直客价0值的时候弹起，点击确定继续）
 */
function first2SecondStep(){
	//联系人
	var contacttables = $("#ordercontact ul[name=orderpersonMes]");
	var ordercontacts = new Array();
	$.each(contacttables, function(key, valueout) {
		var contactinputs = $(valueout).find("input");
		var datacontact = {};
		$.each(contactinputs, function(key, value) {
			var _nametemp = $(value).attr("name");
			datacontact[_nametemp] = $(value).val();
		});
		contactinputs = $(valueout).find("textarea");
		$.each(contactinputs, function(key, value) {
			var _nametemp = $(value).attr("name");
			datacontact[_nametemp] = $(value).val();
		});
		ordercontacts.push(datacontact);
	});
	
	var orderCompanyName = "";
	if ($("#orderCompany").val() == '-1') {
		orderCompanyName = $("input[name='orderCompanyNameShow']").val();
	} else {
		orderCompanyName = $("#orderCompanyName").val();
	}
	// 增加 团队返佣
	
	//点击下一步的时候，设置出行人数为只读，价格选项为只读
	$("#orderPersonNumAdult").attr("readonly","readonly");
	$("#orderPersonNumChild").attr("readonly","readonly");
	$("#orderPersonNumSpecial").attr("readonly","readonly");
	$("input[name=priceTypeRadio]").attr("disabled",true);
	//隐藏按钮，防止多次提交
	$(this).hide();
	$.ajax({
		type : "POST",
		url : "../../orderCommon/manage/firstSave",
		data : {
			placeHolderType : $("#placeHolderType").val(),
			salerId : $("#salerId").val(),
			orderCompany : $("#orderCompany").val(),
			orderCompanyName : orderCompanyName,
			orderCompanyNameShow:$("#orderCompanyNameShow").val(),
			productId : $("#productId").val(),
			productGroupId : $("#productGroupId").val(),
			productOrderId : $("#orderid").val(),
			orderPersonNum : $("#orderPersonelNum").val(),
			orderPersonNumChild : $("#orderPersonNumChild").val(),
			orderPersonNumAdult : $("#orderPersonNumAdult").val(),
			orderPersonNumSpecial : $("#orderPersonNumSpecial").val(),
			roomNumber:$("#roomNumber").val(),	//add 
			adultCurrencyId : $("#crbz").val(),
			childCurrencyId : $("#etbz").val(),
			specialCurrencyId : $("#tsbz").val(),
			suggestAdultCurrencyId : $("#zkcrbz").val(),
			suggestChildCurrencyId : $("#zketbz").val(),
			suggestSpecialCurrencyId : $("#zktsbz").val(),
			singleDiffCurrencyId : $("#singleDiffCurrencyId").val(),
			singleDiff : $("#singleDiff").val(),
			payDepositCurrencyId : $("#payDepositCurrencyId").val(),
			payDeposit : $("#payDeposit").val(),
			payStatus : $("#payStatus").val(),
			payMode : $("#payMode").val(),
			remainDays : $("#remainDays").val(),
			activityKind : $("#activityKind").val(),
			travelerKind : $("#travelerKind").val(),
			createDate : $("#createDate").val(),
			isAfterSupplement : $("#isAfterSupplement").val(),
			// 20150729预定团队返佣
			scheduleBackCurrency : $("#scheduleBackCurrency option:selected").val(),
			scheduleBackPrice : $("#scheduleBackPrice").val(),
			priceType : $("#priceType").val(),
			groupCoverId : $("#groupCoverId").val(),
			orderContactsJSON : JSON.stringify(ordercontacts)
		},
		success : function(msg) {
			if (msg.errorMsg) {
				top.$.jBox.tip(msg.errorMsg, 'error');
			} else {
				if(msg.productOrder != null){
					firstToSecondEffect();
			        $('#orderid').val(msg.productOrder.id);
			        $('#orderCompany').val(msg.agentId);
			        $('#travelerTemplate input[name=travelerOrderId]').val(msg.productOrder.id);
			        _addTravelCount();
			        top.$.jBox.tip('第一步保存成功', 'success');
				}else{
					
					top.$.jBox.tip(msg.msg + '！保存失败！', 'error');
				}
			}
		},
		error : function(msg) {
			top.$.jBox.tip('！保存失败！' + msg.msg, 'error');
		}
	});
}

/**
 * 订单修改，第一步“下一步”，readonly所有联系人所有信息，隐藏增删按钮
 * @param errorList
 */
function readOnlyAllContactInfo(){
	//渠道选择只读
	$("#modifyAgentInfo").attr("disabled", true).parent().find("span[name=showAgentName]").remove();
	//联系人
	$("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		//下拉控件隐藏，追加span(先删)
		var contactSpan = $(element).find("span[name=channelConcat]");
		contactSpan.hide().parent().find("span[name=showName]").remove();
		contactSpan.after("<span name='showName'>" + contactSpan.find("input[name=contactsName]").val() + "</span>");
		//其他input disabled掉		
		$(element).find("input").attr("disabled", true);
		//增删按钮隐藏
			$(element).find("span[name=addContactButton]").hide();
			$(element).find("span[name=delContactButton]").hide();
    });
}

/**
 * 订单修改，第二步“上一步”，恢复原有读写状态（注意批发商可写可增配置）
 * @param errorList
 */
function back2WritableContactInfo(){
	//渠道选择只读
	$("#modifyAgentInfo").attr("disabled", false).parent().find("span[name=showAgentName]").remove();
	//联系人
	$("#ordercontact").find("ul[name=orderpersonMes]").each(function(index, element){
		//下拉控件显示，删除回显span
		var contactSpan = $(element).find("span[name=channelConcat]");
		contactSpan.show().parent().find("span[name=showName]").remove();
		if($("#orderContact_modifiability").val() == 1){
			//所有input 可写
			$(element).find("input").attr("disabled", false);
		} else {
			$(element).find("input[name=remark]").attr("disabled", false);
		}
		//增删按钮隐藏
			$(element).find("span[name=addContactButton]").show();
			$(element).find("span[name=delContactButton]").show();
		
	});
}


// S-147
/**
 * 文件上传后续处理
 */
function commenFunction(obj,fileIDList,fileNameList,filePathList) {
	var companyUuid = $("#companyUuid").val();
	if("75895555346a4db9a96ba9237eae96a5" == companyUuid || '7a81c5d777a811e5bc1e000c29cf2586' == companyUuid) {
		var name = obj.name;
		$("#"+name).remove();
		if (fileIDList.length > 0) {
			$(obj).parent().find("span.seach_r").remove();
			$(obj).parents("li").find(":input:first").attr("type", "hidden")
					.val(fileNameList.substring(0, fileNameList.length - 1))
					.next(":input[type=text]").remove();
			$(obj).parents("li").find(":input:first")
					.after('<input type="text" readonly="readonly" />');
			$(obj).parent().append('<div style="display: none" id="'+obj.name+'"></div>');
			var fileIds = fileIDList.split(";");
			fileIds.pop();
			var fileNames = fileNameList.split(";");
			fileNames.pop();
			var filePaths = filePathList.split(";");
			filePaths.pop();
			$.each(fileIds, function(i, val) {
				$(obj).after("<span class='seach_r' style='position:relative;margin-left:67px'>" +
						"<b>" + fileNames[i] + "</b>" +
						"<a class='deleteicon' style='margin-left:10px; position:absolute; top:9px;' " +
						"href='javascript:void(0)' onclick='deleteFiles(" + val + ","+ i +", this)'>x</a>" +
						"</span>");
				$("#"+name).append("<input type='hidden' name="+name+"docID value='" + val + "' />")
						.append("<input type='hidden' name="+name+"docName value='" +fileNames[i]+ "' />")
						.append("<input type='hidden' name="+name+"docPath value='" +filePaths[i]+ "' />");
			});
		}
	} else {
		if (fileNameList && filePathList) {
			var name = obj.name;
			$("#"+name).remove();
			$(obj).prev().val(fileNameList);
			$(obj).parent().append('<div style="display: none" id="'+obj.name+'"></div>');
			$("#"+name).append("<input type='hidden' name="+name+"docID value='" +fileIDList.replace(";","")+ "' />")
					.append("<input type='hidden' name="+name+"docName value='" +fileNameList.replace(";","")+ "' />")
					.append("<input type='hidden' name="+name+"docPath value='" +filePathList.replace(";","")+ "' />");
			$(obj).prev().show();
		}
	}
}

/**
 * 文件上传后续处理（报名时特殊需求处的附件上传）
 */
function commenFunction4SpecialDeman(obj,fileIDList,fileNameList,filePathList) {
	var name = obj.name;
	if (fileIDList.length > 0) {
		var fileIds = fileIDList.split(";");
		fileIds.pop();
		var fileNames = fileNameList.split(";");
		fileNames.pop();
		var filePaths = filePathList.split(";");
		filePaths.pop();
		if($("#fileIdList").val() != "") {
			$("#fileIdList").val($("#fileIdList").val()+ "," + fileIds);
		} else {
			$("#fileIdList").val(fileIds);
		}
		$.each(fileIds, function(i, val) {
			$("input[name='"+obj.name+"']").after('<br><span class="seach_checkbox_2" style="position:relative;margin-left:105px">' +
				'<a href="javascript:void(0)" class="downloadFile" onclick="downloads4SpecialDeman('+fileIds[i]+')">'+fileNames[i]+'</a> ' +
				'<a style="margin-left:10px;" class="deleteicon" href="javascript:void(0)" onclick="deleteFiles4SpecialDeman('+fileIds[i]+', event)">x</a></span>');

		});
	}
}

/**
 * 删除现有的文件
 */
function deleteFiles(id, index, obj) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			var $li = $(obj).parents("li");
			var firstInput = $(obj).parent().parent().find("input:eq(0)").val();
			var fileNames = firstInput.split(";");
			fileNames.splice(index, 1);
			$(obj).parents("li").find(":input:first").val(fileNames.toString().replace(/,/g,';'));

			var name = $(obj).parent().parent().find("input:eq(2)").attr("name");
			$(obj).parent().parent().find("#" + name).find("[name='"+ name +"docID']").each(function() {
				if(id == $(this).val()) {
					$(this).next().next().remove();
					$(this).next().remove();
					$(this).remove();
				}
			});

			if($.trim($(obj).parents("li").find(":input:first").val()) == "") {
				$(obj).parents("li").find("div:last:hidden").remove();
			}

			$(obj).parent().remove();
			var len = $li.find("span.seach_r").length;
			$li.find("span.seach_r").each(function(index) {
				var a = $(this).find("a").attr("onclick").split(",");
				a.splice(1, 1, len - 1 - index);
				$(this).find("a").attr("onclick", a.toString());
			})
		}
	},{buttonsFocus:1});
}
// E-147
//S--C109
function jbox__apply_for_tourist_fee__pop_fab() {
	var travelerTables = $("#traveler form.travelerTable");
	// 验证是否保存游客信息 如果有未保存的游客需要提示
	// 获取添加的游客form数组
	var notSaveTravelerName = [];
	var notSaveTravelerIndex = [];
	$.each(travelerTables,function(key,value){
		var travelerId = $(value).find('input[name=travelerId]').val();
		if(travelerId == ""){
			notSaveTravelerName.push($(value).find("input[name=travelerName]").val());
			notSaveTravelerIndex.push(Number($(value).find(".travelerIndex").text()));
		}else{
			if(!$('input[name=travelerName]',$(value)).is(":hidden")){
				notSaveTravelerName.push($(value).find("input[name=travelerName]").val());
				notSaveTravelerIndex.push(Number($(value).find(".travelerIndex").text()));
			}
		}
	});
	var _add_seachcheck = $("#traveler .add_seachcheck");
	for(var i = 0; i < notSaveTravelerIndex.length; i++){
		if (_add_seachcheck.eq(notSaveTravelerIndex[i] - 1).hasClass("boxCloseOnAdd")) {
			_add_seachcheck.eq(notSaveTravelerIndex[i] - 1).click();
		}
	}
	if(notSaveTravelerName.length > 0){
		top.$.jBox.tip('游客姓名为(' + notSaveTravelerName.join(',') + ')的游客未保存，信息已展开请保存', 'error');
		return;
	}

	var times = 0;
	// 将优惠列表进行清空
	$("#apply-for-tourist-fee-pop").find(".travelerDiscountInfo").text("");
	// 遍历每个tavelerForm,将符合条件的优惠信息加入到优惠列表
	$("[name='travelerForm']:visible").each(function() {
		// 产品优惠金额
		var activityDiscountAmount = $.trim($(this).find("span.show>span.activityDiscountAmount").text());
		// 获取用户输入的优惠金额
		var discountPrice = $(this).find("input[name='discountPrice']").val();
		// 申请优惠的游客id
		var applyTravelerId= $(this).find(":hidden[name='travelerId']").val();
		// 如果用户输入的优惠金额和产品优惠金额数量相等,则将此条信息加入到优惠列表
		if(Number(activityDiscountAmount) == Number(discountPrice)) {
			times++;
			var addString = "<tr>" +
					"<td><input type='checkbox'/>" +
					"<input type='hidden' class='applyTravelerId' value='"+ applyTravelerId +"'></td>" +
					"<td>"+ $(this).find("input[name='travelerName']").val() + "</td>						" +
					"<td>"+ $(this).find(":radio[name='personType']:checked").parent().text() +"</td>" +
					"<td><p><span>"+ $(this).find("span[name='innerJsPrice']").text() +"</span></p></td>" +
					"<td><p><span>"+ $(this).find("span[name='peerJs']").text() +"</span></p></td>" +
					"<td>"+ $.trim($(this).find(".discount>span.show").find(".activityDiscountCurrencyMark").text()) +"" +
					"<input type='hidden' name='applyDiscountCurrencyId' " +
					"value='"+$(this).find(".discount>span.show").find("[name='activityDiscountCurrencyId']").val()+"' />" +
					"<input type='text' name='applyDiscountPrice' onkeyup='checkNumber(this)' onafterpaste='checkNumber(this)'/>" +
					"</td>" +
					"</tr>";
			$("#apply-for-tourist-fee-pop").find(".travelerDiscountInfo").append(addString);
		}
	});
	if(times == 0) {
		$.jBox.tip("没有符合“申请优惠”条件的游客", "warning");
		return;
	}

	var $pop = $.jBox($("#apply-for-tourist-fee-pop").html(), {
		title: "申请优惠", buttons: {'关闭': 0, '保存': 1}, submit: function (v, h, f) {
			if (v == "1") {
				var msg;
				$pop.find('input[type="checkbox"]:checked').each(function () {
					if (!$(this).parents('tr:first').find('input[name="applyDiscountPrice"]').val()) {
						msg = "申请优惠金额必须填写！";
						return;
					}
				});
				if (msg) {
					$.jBox.tip(msg);
					return false;
				}

				var len = $pop.find('[name=applyDiscountPrice]:input').parents("tbody").find(":checkbox:checked").length;
				if(len <= 0) {
					msg = "请先选择游客！";
				}
				if (msg) {
					$.jBox.tip(msg);
					return false;
				}
				var applyInfos = new Array();
				var applyTravelerIds = "";
				// 申请优惠信息金额
				$pop.find(".travelerDiscountInfo").find("tr").each(function() {
					var isChecked = $(this).find("td:eq(0)>input:first").attr("checked");
					if(isChecked == 'checked') {
						var applyDiscountPrice = $(this).find("input[name='applyDiscountPrice']").val();	// 优惠金额
						var travelerId = $(this).find(".applyTravelerId").val();							// 游客id
						var applyInfo = new Object();
						applyInfo.travelerId = travelerId;
						applyInfo.applyDiscountPrice = applyDiscountPrice;
						applyInfos.push(applyInfo);
						applyTravelerIds += (travelerId + ",");
					}
				});

				applyTravelerIds = applyTravelerIds.substring(0, applyTravelerIds.length - 1);

				// 更新游客申请优惠信息
				var oldApplyTravelerIds = $("#applyTravelerIds").val();
				if(oldApplyTravelerIds != null || applyInfos.size > 0) {
					$.ajax({
						type : "POST",
						url : "../../traveler/manage/updateApplyDiscount4YouJia",
						data : {
							oldApplyTravelerIds : oldApplyTravelerIds,
							applyInfos :  JSON.stringify(applyInfos)
						},
						success : function (data) {
							if(data.result == '2') {
								$.jBox.tip("保存游客优惠信息出错!", "warning");
								return false;
							}
						}
					});
				}
				// 将申请优惠的游客信息赋值给 applyTravelerIds
				$("#applyTravelerIds").val(applyTravelerIds);

				return true;
			}
		}, height: 400, width: 600,
		persistent: true
	});
	inquiryCheckBOX();
}
// E-109

function checkNumber(obj) {
	var money = obj.value;
	if(money=="") {obj.value=""};
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
}
