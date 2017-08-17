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
				payStatus : $("#payStatus").val(),
				frontMoney : $("#frontMoney").val(),
				payDepositCurrencyId : $("#payDepositCurrencyId").val(),
				payMode : $("#payMode").val(),
				remainDays : $("#remainDays").val(),
				payDeposit : $("#payDeposit").val(),
				activityKind : $("#activityKind").val(),
				travelerKind : $("#travelerKind").val(),
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
				        top.$.jBox.tip('第一步保存成功', 'success');
				        recountIndexTraveler();
				        travelerEffect();
					}else{
						top.$.jBox.tip('第一步保存失败', 'error');
					}
				}
			}
		});
	});
	//第一步到第二步页面效果
	function firstToSecondEffect(){
		//设置只读人数
		$("#productOrderTotal").disableContainer( {
            blankText : "—",
            formatNumber : formatNumberArray
        });
		//设置只读联系人
        $("#orderpersonMesdtail").disableContainer( {
            blankText : "—",
            formatNumber : formatNumberArray
        });
        
        $("#ordercontact .yd1AddPeople").hide();
        $("#manageOrder_new").show();
        $("#manageOrder_m").show();
        $("#oneToSecondOutStepDiv").hide();
        $("#secondDiv").show();
        $(".orderPersonMsg").show();
        $("#stepbar").removeClass("yd-step1").addClass("yd-step2");
        $("ul[name='orderpersonMes'] .ydbz_x").hide();
	}
	
	//第一步到第二步处理游客的效果
	function travelerEffect(){
		//处理除了第一个游客其他游客内容收缩
		var _add_seachcheck = $("#traveler .add_seachcheck");
		for(var i = 1; i< _add_seachcheck.length;i++){
			if (!_add_seachcheck.eq(i).hasClass("ydExpand")) {
				_add_seachcheck.eq(i).click();
			}
		}
		var travelerForm = $("#traveler form");
		if(travelerForm.length > 0){
			for(var j = 0; j < travelerForm.length; j++){
				var jsPriceJson = $("input[name=jsPriceJson]",travelerForm[j]).val();
				var priceObjArr = new Array();
				$.each($.parseJSON(jsPriceJson), function(key, value){
					priceObjArr.push(value);
				});
				var travelerJsPrice = new Object();
				travelerJsPrice.jsPrice = priceObjArr;
				alert(6);
				travelerTotalPriceArr.push(travelerJsPrice);
			}
			for(var j = 0; j < travelerForm.length; j++){
				changePayPriceByCostChange($(travelerForm[j]));
			}
		}else{
			changeTotalPrice();
		}
	}
	
	// 第二步点击下一步
	$("#secondToThirdStepDiv").click(function() {
		//验证游客信息 如果有未保存的游客需要提示
		var flag = true;
		var travelerIdArr = $('#traveler input[name=travelerId]');
		$.each(travelerIdArr, function(key, value) {
			var travelerId = $(value).val();
			if(travelerId == ""){
				top.$.jBox.tip('有未保存的游客信息，请保存', 'error');
				flag = false;
			}
		});
		if(!flag){
			return;
		}
		outerrorList = new Array();
		_doValidatetravelerForm();
		createDivInDiv(outerrorList);
		if (outerrorList.length > 0) {
			return;
		}
		//游客信息 
		$("#manageOrder_new").disableContainer( {
			blankText : "—",
			formatNumber : formatNumberArray
		});
		//特殊需求
		$("#manageOrder_m").disableContainer( {
			blankText : "—",
			formatNumber : formatNumberArray
		});
		//费用
		var sums = $(".tourist-right").find("input[name='sum']");
		$.each(sums, function(key, value) {
			var _$span = $(value).next();
			_$span.text(_$span.text());
			;
		});

		$("#secondDiv").hide();
		$("#thirdDiv").show();
		$(".orderPersonMsg").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step3");
	});
	
	// 第二步点击上一步
	$("#secondToOneStepDiv").click(function() {
		$("#oneToSecondStepDiv").show();
		$("#productOrderTotal").undisableContainer();
		$("#orderpersonMesdtail").undisableContainer();
		$("ul[name='orderpersonMes']").undisableContainer();
		$("ul[name='orderpersonMes'] .ydbz_x").show();
		$("#ordercontact .yd1AddPeople").show();
		$("#oneToSecondOutStepDiv").show();
		$("#secondDiv").hide();
		$("#manageOrder_new").hide();
		$("#manageOrder_m").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step1");
		var _closeOrExpand = $(".closeOrExpand").eq(0);
		$(".orderPersonMsg").hide();
		if (_closeOrExpand.hasClass("ydClose")) {
			_closeOrExpand.click();
		}

	});
	
	
	//第三步点击上一步
	$("#thirdToSecondTStepDiv").click(function() {
		$("#manageOrder_new").undisableContainer();
		$("#manageOrder_m").undisableContainer();
		$("#secondDiv").show();
		$("#thirdDiv").hide();
		$("#stepbar").removeClass("yd-step3").addClass("yd-step2");
	});
	
	//转正订单
	$("#thirdToFourthStepDiv").click(function() {
		var _selectType = $(this).next().clone();
		$(_selectType).addClass("typeSelected");
		$(_selectType).show();
		var $div = $("<div class=\"tanchukuang\"></div>").append($('<div class="add_allactivity"><label>付款方式：</label>').append(_selectType));
		var html = $div.html();
		$.jBox(html, {title: "转正 -选择付款方式",buttons:{'确 定':1},submit: function (v, h, f) {selOrderType();},height:230});
	});
	function selOrderType(){
		var agentId = $("#agentId").val();
		var productId = $("#productId").val();
		var	productGroupId = $("#productGroupId").val();
		var yws = getFreePosition(productGroupId);
		var qws = getLeftPayReservePosition(productId, productGroupId, agentId);
		var applyPersonNum = Number($("#orderPersonelNum").val());
		var flag = false;
		var placeHolderType = 0;
		if(yws-applyPersonNum < 0 && qws - applyPersonNum < 0){
			top.$.jBox.tip('余位数不足，不能预订','error');
	        return;		
		}else if(applyPersonNum - qws > 0 && applyPersonNum - yws > 0){
			var $div = $("<div class=\"tanchukuang\"></div>")
	        .append('<div class="add_allactivity"><label>订单类型：</label><p><span><input type="radio" name="flytype" checked="checked" value="1" class="radio">切位订单，剩余' + qws + '</span><br><span><input type="radio" name="flytype" value="0" class="radio">余位订单，剩余' + yws + '</span></p>')
	        .append('<div class="add_intermodalType"><label>付款方式：'+ $(".orderTypeSelected option:selected").text() +'</label>');
	        var html = $div.html();
	        $.jBox(html, {title: "选择订单类型",buttons:{'确 定':1},submit: function (v, h, f) {applyToOrder($("input[name='flytype']:checked").val());},height:245});
		}else if(yws - applyPersonNum >= 0){
			placeHolderType = 0;
			applyToOrder(placeHolderType);
		}else if(qws - applyPersonNum >= 0){
			placeHolderType = 1;
			applyToOrder(placeHolderType);
		}
	}
	function applyToOrder(placeHolderType){
		var payMode = $(".typeSelected option:selected").val();
		var payStatus = "";
		if (payMode == "1") {
			payStatus = 2;
		} else if(payMode == "3") {
			payStatus = 1;
		} else if (payMode == "7") {
			payStatus = 7;
		} else if (payMode == "8") {
			payStatus = 8;
		} else {
			payStatus = 3;
		}
		var currencyId = paramCurrencyId.join(",");
		var currencyPrice = paramCurrenctPrice.join(",");
		$.ajax({
			type : "POST",
			url : "../../applyOrderCommon/manage/applyToOrder",
			data : {
				productOrderId : $("#orderid").val(),
                specialDemand : $('#specialDemand').val(),
                placeHolderType : placeHolderType,
                payMode : payMode,
                payStatus : payStatus,
                currencyId : currencyId,
                currenctPrice : currencyPrice
			},
			success : function(msg) {
				if (msg.errorMsg) {
					top.$.jBox.tip(msg.errorMsg, 'error');
					return false;
				} else {
					window.setTimeout(function() {
						window.location.href = "../../orderList/manage/showOrderList/0/2.htm?orderNum=" + msg.productOrder.orderNum;
					}, 1000);
				}
			}
		});
	}
	/**
	 * 获取所选渠道商剩余的切位人数
	 * @param productId 产品ID
	 * @param productGroupId 团期ID
	 * @param agentId 渠道商ID
	 */
	function getLeftPayReservePosition(productId,productGroupId,agentId){
	    var leftNum = 0;
	    $.ajax({
	        async: false, 
	        type : "POST",
	        url : "../../orderCommon/manage/getLeftPayReservePosition",
	        data : {
	            productId : productId,
	            productGroupId : productGroupId,
	            agentId: agentId
	        },
	        success : function(msg) {
	            if (msg.errorMsg) {
	                top.$.jBox.tip(msg.errorMsg, 'error');
	                return false;
	            } else {
	                leftNum = msg.leftNum;
	            }
	        }
	    });
	    return leftNum;
	}
	/**
	 * 获取团期所剩下的余位数
	 * @param productId 产品ID
	 * @param productGroupId 团期ID
	 * @param agentId 渠道商ID
	 */
	function getFreePosition(productGroupId){
	    var leftNum = 0;
	    $.ajax({
	        async: false, 
	        type : "POST",
	        url : "../../applyOrderCommon/manage/getFreePosition",
	        data : {
	            productGroupId : productGroupId
	        },
	        success : function(msg) {
	            if (msg.errorMsg) {
	                top.$.jBox.tip(msg.errorMsg, 'error');
	                return false;
	            } else {
	                leftNum = msg.leftNum;
	            }
	        }
	    });
	    return leftNum;
	}
});	
	