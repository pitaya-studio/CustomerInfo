$(function() {
	// 第一步点击下一步 点击后验证表单通过后保存基本订单信息
	$("#oneToSecondStepDiv").click(function() {
		outerrorList = new Array();
		// 验证页面有效性和余位数检查
		 if(!checkFreePosition()){
			 return;
		 }
		//验证订单联系人
		_doValidateorderpersonMesdtail();
		createDivInDiv(outerrorList);
		if (outerrorList.length > 0) {
			return;
		}
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
		//隐藏按钮，防止多次提交
		$(this).hide();
		$.ajax({
			type : "POST",
			url : "../../applyOrderCommon/manage/appFirstSave",
			data : {
				orderCompany : $("#orderCompany").val(),
				orderCompanyName : $("#orderCompanyName").val(),
				productId : $("#productId").val(),
				productGroupId : $("#productGroupId").val(),
				productOrderId : $("#orderid").val(),
				orderPersonNum : $("#orderPersonelNum").val(),
				orderPersonNumChild : $("#orderPersonNumChild").val(),
				orderPersonNumAdult : $("#orderPersonNumAdult").val(),
				orderPersonNumSpecial : $("#orderPersonNumSpecial").val(),
				adultCurrencyId : $("#crbz").val(),
				childCurrencyId : $("#etbz").val(),
				specialCurrencyId : $("#tsbz").val(),
				singleDiffCurrencyId : $("#singleDiffCurrencyId").val(),
				singleDiff : $("#singleDiff").val(),
				payDepositCurrencyId : $("#payDepositCurrencyId").val(),
				payDeposit : $("#payDeposit").val(),
				payStatus : $("#payStatus").val(),
				payMode : $("#payMode").val(),
				remainDays : $("#remainDays").val(),
				activityKind : $("#activityKind").val(),
				travelerKind : $("#travelerKind").val(),
				salerId : $("#salerId").val(),
				orderContactsJSON : JSON.stringify(ordercontacts)
			},
			success : function(msg) {
				if (msg.errorMsg) {
					top.$.jBox.tip(msg.errorMsg, 'error');
				} else {
					if(msg.productOrder != null){
						firstToSecondEffect();
				        $('#orderid').val(msg.productOrder.id);
				        $('#travelerTemplate input[name=travelerOrderId]').val(msg.productOrder.id);
				        _addTravelCount();
				        top.$.jBox.tip('第一步保存成功', 'success');
					}else{
						top.$.jBox.tip('第一步保存失败', 'error');
					}
				}
			}
		});
	});
	
	//保存 保存并支付
	$("#thirdToFourthStepDiv").click(function() {
		var currencyId = paramCurrencyId.join(",");
		var currencyPrice = paramCurrenctPrice.join(",");
		$.ajax({
			type : "POST",
			url : "../../applyOrderCommon/manage/appLastSave",
			data : {
				productOrderId : $("#orderid").val(),
                specialDemand : $('#specialDemand').val(),
                currencyId : currencyId,
                currenctPrice : currencyPrice
			},
			success : function(msg) {
				if (msg.errorMsg) {
					top.$.jBox.tip(msg.errorMsg, 'error');
					return false;
				} else {
					window.location.href = "../../applyOrderCommon/manage/showApplyOrderList/1000/2.htm?_m=417&_mc=419&orderNumOrGroupCode="+ msg.productOrder.orderNum;
				}
			}
		});
	});
});	
	