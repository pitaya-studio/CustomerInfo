var isdoSave = false;
var count = 0;
outerrorList = new Array();
var formatNumberArray = new Array();
formatNumberArray.push("sum");
jQuery(function($) {
	$.fn.datepicker = function(option) {
		var opt = $.extend( {}, option);
		$(this).click(function() {
			WdatePicker(opt);
		});
	}
});

var orgTotalPersonNum;

$(function() {
	$("#mddtargetAreaNames").tooltip( {
		track : true
	});

	var birthdays = $("#traveler input[name='birthDay']");
	var issuePlaces = $("#traveler input[name='issuePlace']");
	var validityDates = $("#traveler input[name='validityDate']");
	$.each(birthdays, function(key, value) {
		$(value).datepicker( {
			maxDate : new Date()
		});
	});
	$.each(issuePlaces, function(key, value) {
		$(value).datepicker();
	});
	$.each(validityDates, function(key, value) {
		$(value).datepicker();
	});


	//112
	$("textarea[id=specialDemand]").focusin(function(){
		$(this).removeAttr("placeholder");
	}).focusout(function(){
		$(this).attr("placeholder","最多可输入500字");
	});


	$("#traveler").delegate("input[name='issuePlace']",
			"blur",
			function() {
				// 发证日期
			var _$validityDate = $(this).closest(".zjlx-tips").find(
					"input[name='validityDate']");
			var minDate = $(this).val();
			var curDate = new Date();

			//护照有效期默认为发证日期加10年，然后减一年
			if(minDate != "") {
				var lastDate = new Date(minDate);
				lastDate.setFullYear(lastDate.getFullYear() + 10);
				lastDate.setDate(lastDate.getDate() - 1);
				$(_$validityDate).val(lastDate.getFullYear() + "-" + (lastDate.getMonth() + 1) + "-" + lastDate.getDate());
			}
			
			if (new Date(minDate) < curDate) {
				minDate = curDate;
			}

			_$validityDate.unbind();
			_$validityDate.datepicker( {
				minDate : minDate
			});
		});

	$("#traveler").delegate("input[name='validityDate']",
			"blur",
			function() {
				// 发证日期
			var _$issuePlace = $(this).closest(".zjlx-tips").find(
					"input[name='issuePlace']");
			var maxDate = $(this).val();
			_$issuePlace.unbind();
			_$issuePlace.datepicker( {
				maxDate : maxDate
			});
		});

	$("#placeHolderTypeUl li").click(function() {
		$("#placeHolderTypeUl li").removeClass("active");
		$(this).addClass("active");
		var placeHolderType = $(this).attr("lang");
		$("#placeHolderType").val(placeHolderType);
		if (placeHolderType == 1) {
			$("#qwsspan").show();
			$("#ywsspan").hide();
		} else {
			$("#ywsspan").show();
			$("#qwsspan").hide();
		}

	});

	$("#addTraveler").click(
			function() {
				var $table = $("#travelerTemplate").children();
				var _travelerForm = $table.clone().addClass("travelerTable");
				_travelerForm.find("input[name='personType']").attr("name",
						"personTypeCount" + count++);
	
				//默认添加游客信息时，判断什么游客类型
				var selFlag = false;
				var personType = 0;
				var personval = 0;
				if ($("#orderPersonNumAdult").val() > countAdult()){
					selFlag = true;
					personType = 0;
					personval = 1;
				}
				if(!selFlag){
					if ($("#orderPersonNumChild").val() > countChild()){
						selFlag = true;
						personType = 1;
						personval = 2;
					}
				}
				if(!selFlag){
					if ($("#orderPersonNumSpecial").val() > countSpecial()){
						selFlag = true;
						personType = 2;
						personval = 3;
					}
				}
				$("#traveler").append(_travelerForm);
				$("input[name^='personType']",_travelerForm)[personType].checked = true;
				_travelerForm.find("input[name^='personType']:checked").trigger("change");
				$("input[name^='personType']",_travelerForm)[personType].click();//changePayprice();
				dodatePicker();
			});

	$("#orderPersonNumChild").blur(function() {
		changeorderPersonelNum();
	});

	$("#orderPersonNumAdult").blur(function() {
		changeorderPersonelNum();
	});
	
	$("#orderPersonNumSpecial").blur(function() {
		changeorderPersonelNum();
	});

	$("#orderPersonelNum").blur(function() {
		var travelerNum = $(this).val();
		checkFreePosition();
		var deposit = $("#payDeposit").val();
		$("#frontMoney").val(deposit * travelerNum);
		changeTotalPrice();
	});

	$("#dosave").click(function() {
		isdoSave = true;
		dosave(true);
	});
	$("#donext").click(function() {
		dosave(true, 1)
	});

	$("#paydepositbutton").click(function() {
		dosave(true, 2)
	});
	$("#paywk").click(function() {
		dosave(true, 4)
	});

	dodatePicker();

	$("#traveler").delegate(
			"input[name='travelerName']",
			"blur",
			function() {
				var srcname = $(this).val();
				if ($.trim(srcname).length <= 0) {
					return false;
				}

				var pinying = $(this).closest("form").find(
						"input[name='travelerPinyin']").eq(0);
				$.ajax( {
					type : "POST",
					url : "../../orderCommon/manage/getPingying",
					data : {
						srcname : $.trim(srcname)
					},
					success : function(msg) {
						pinying.val(msg);
					}
				});

			});

	$("#traveler").delegate(
			"a[name='deleteTraveler']",
			"click",
			function() {
				var $this = $(this);
				var travelerId = $(this).closest('.travelerTable').find(
						"input.traveler[name='id']").val();
				$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
					if (v == 'ok') {
						// 显示按钮
						$("#addTraveler").parent().show();
						if (travelerId == "") {
							// 无需记录
						$this.closest('.travelerTable').remove();
						changeTotalPrice();
					} else {
						// 需要删除数据库中记录
						$.ajax( {
							type : "POST",
							url : "../../orderCommon/manage/deleteTraveler",
							data : {
								travelerId : travelerId
							},
							success : function(msg) {
								top.$.jBox.tip('删除成功', 'success');
								$this.closest('.travelerTable').remove();
								changeTotalPrice();
							}
						});
					}
				} else if (v == 'cancel') {

				}
			}	);
			});

	$("#traveler").delegate("input[name='payPrice']", "keyup", function() {
		changeTotalPrice();
	});

	$("#traveler").delegate(
			"a[name='deleltecost']",
			"click",
			function() {
				var $this = $(this);
				var costId = $this.closest(".cost").find("input[name='id']")
						.val();

				$.jBox.confirm("确定要删除数据吗？", "提示", function(v, h, f) {
					if (v == 'ok') {
						if (costId != null && costId != ""
								&& costId != undefined) {
							$.ajax( {
								type : "POST",
								url : "../../orderCommon/manage/deleteCost",
								data : {
									costId : costId
								},
								success : function(msg) {
									top.$.jBox.tip('删除成功', 'success');
									$this.closest(".cost").find(
											"input[name='sum']").remove();
									changePayPriceByCostChange($this);
									$this.closest(".cost").remove();

								}
							});
						} else {
							$this.closest(".cost").find("input[name='sum']")
									.remove();
							changePayPriceByCostChange($this);
							$this.closest(".cost").remove();
						}
					} else if (v == 'cancel') {

					}
				});
			});
	var addcostindex = 0;
	$("#traveler")
			.delegate(
					"a[name='addcost']",
					"click",
					function() {
						var $this = $(this);
						var $table = $this.next();
						var travelerIndex = $this.closest("form").find(
								".travelerIndex");
						addcostindex++;
						var _div = $('<div class="payfor-other cost"><input type="hidden" name="id">'
								+ '<input type="text" name="name" onfocus="payforotherIn(this)" maxlength="50" onblur="payforotherOut(this)" id="costname'
								+ travelerIndex.text()
								+ addcostindex
								+ '" class="required ipt2" />'
								+ '<span class="ipt-tips2" onclick="focusIpt(this)">费用名称</span>'
								+ '<input type="text"  id="costvalue'
								+ travelerIndex.text()
								+ addcostindex
								+ '" name="sum" style="" value="0"  maxlength="15" class="required number ipt3 rmb" >'
								+ '<a name="deleltecost" class="btn-del1"></a>'
								+ '</div>');

						$table.append(_div);
						changePayPriceByCostChange($this);
					});

	$("#traveler").delegate("input[name='sum']", "click", function() {
		var _readonly = $(this).attr("readonly");
		if (_readonly != "" && _readonly != undefined) {
			return false;
		} else {
			var _tempVal = $(this).val();
			if (_tempVal == "0") {
				$(this).css("color", "");
				$(this).val("");
			}
		}
	});

	$("#traveler").delegate("input[name='sum']", "blur", function() {
		var _tempVal = $(this).val();
		_tempVal = _tempVal.replace(/[^0-9\-]*(\-?[0-9]+)*\D*/g, '$1')
		_tempVal = _tempVal.replace(/([0-9]+?)\D+/g, '$1')
		_tempVal = _tempVal.replace(/(\-)*/g, '$1')
		if (_tempVal == "") {
			_tempVal = 0;
			// $(this).css("color","#ccc");
		}
		$(this).val(_tempVal);
		changePayPriceByCostChange($(this));
		changeTotalPrice();
	});
	// 改变人员类型时
//	$("#traveler").delegate(
//			"input[name^='personType']",
//			"change",
//			function() {
//				if ($(this).val() == 2) {
//					$(this).closest("form").find("input[name='srcPrice']").val(
//							$("#etj").val());
//					$(this).closest("form").find("input[name='srcPrice']")
//							.prev().text(
//									$("#etj").val().toString()
//											.formatNumberMoney('#,##0.00'));
//				} else if ($(this).val() == 1) {
//					$(this).closest("form").find("input[name='srcPrice']").val(
//							$("#crj").val());
//					$(this).closest("form").find("input[name='srcPrice']")
//							.prev().text(
//									$("#crj").val().toString()
//											.formatNumberMoney('#,##0.00'));
//				} else {
//					$(this).closest("form").find("input[name='srcPrice']").val(
//							$("#tsj").val());
//					$(this).closest("form").find("input[name='srcPrice']")
//							.prev().text(
//									$("#tsj").val().toString()
//											.formatNumberMoney('#,##0.00'));
//				}
//				var zjbutton = $(this).closest("form")
//						.find("a[name='addcost']");
//				changePayPriceByCostChange(zjbutton);
//			});

	$("#traveler").delegate(
			"select[name='hotelDemand']",
			"change",
			function() {
				if ($(this).val() == 1) {
					$(this).closest("form").find("input[name='singleDiff']")
							.val($("#singleDiff").val());
					$(this).closest("form").find("input[name='singleDiff']")
							.prev().text(
									$("#singleDiff").val().toString()
											.formatNumberMoney('#,##0.00'));
				} else {
					$(this).closest("form").find("input[name='singleDiff']")
							.val(0);
					$(this).closest("form").find("input[name='singleDiff']")
							.prev().text(0);
				}
				var zjbutton = $(this).closest("form")
						.find("a[name='addcost']");
				changePayPriceByCostChange(zjbutton);
			});
	// 第一步的下一步
	$("#oneToSecondStepDiv").click(function() {
		outerrorList = new Array();
		_doValidateorderpersonMesdtail();
		_doValidateproductOrderTotal();
		createDivInTable(outerrorList);

		// 验证页面有效性和余位数检查
			if (outerrorList.length > 0 || !checkFreePosition()) {
				return;
			}
			$("#productOrderTotal").disableContainer( {
				blankText : "—",
				formatNumber : formatNumberArray
			});
			$("#orderpersonMes").disableContainer( {
				blankText : "—",
				formatNumber : formatNumberArray
			});
			$("#manageOrder_new").show();
			$("#manageOrder_m").show();
			$("#oneToSecondOutStepDiv").hide();
			$("#secondDiv").show();

			var _closeOrExpand = $(".closeOrExpand").eq(0);
			if (_closeOrExpand.hasClass("ydExpand")) {
				_closeOrExpand.click();
			}
			_closeOrExpand.parent().next().next(".orderPersonMsg").show();
			$("#stepbar").removeClass("yd-step1").addClass("yd-step2");
		});

	// 第二步的上一步
	$("#secondToOneStepDiv").click(function() {
		$("#productOrderTotal").undisableContainer();
		$("#orderpersonMes").undisableContainer();

		$("#oneToSecondOutStepDiv").show();
		$("#secondDiv").hide();
		$("#manageOrder_new").hide();
		$("#manageOrder_m").hide();
		$("#stepbar").removeClass("yd-step2").addClass("yd-step1");
		var _closeOrExpand = $(".closeOrExpand").eq(0);
		_closeOrExpand.parent().next().next(".orderPersonMsg").hide();

		if (_closeOrExpand.hasClass("ydClose")) {
			_closeOrExpand.click();
		}

	});
	// 第二步的下一步
	$("#secondToThirdStepDiv").click(
			function() {
				//如果游客护照有效期不足半年，则提醒
				$("input[name='validityDate']:visible").each(function(index, obj) {
					var nowDate = new Date();
					var validityDate = new Date($(this).val());
					var diff = validityDate.getTime() - nowDate.getTime();
					if(diff/(24*60*60*1000) < 183) {
						var name = $(obj).parents("div.tourist-left").children().children().children("input[name=travelerName]").val();
						alert('游客 ' + name + ' 护照有效期不足半年，请注意');
					}
				});
				//验证号码必填
				var flag = true;
				$("input[name='idCard']:visible").each(function(index, obj) {
					if($(this).val() == "") {
						var name = $(this).parent().children("label").text();
						top.$.jBox.tip('请填写' + name.replace("：",""),'warning');
		                top.$('.jbox-body .jbox-icon').css('top','55px');
		                $(this).focus();
		                flag = false;
		                return false;
					}
				});
				
				if(!flag) {
					return false;
				} 
				
				if ($("#orderPersonNumAdult").val() < countAdult()
						|| $("#orderPersonNumChild").val() < countChild()
						|| $("#orderPersonNumSpecial").val() < countSpecial()) {
					top.$.jBox.tip('成人、儿童或特殊人数与初始值不匹配请修改', 'error');
					return false;
				}
				outerrorList = new Array();
				_doValidatecontactForm();
				_doValidatetravelerForm();
				_doValidateorderpersonMesdtail();
				_doValidateproductOrderTotal();

				createDivInDiv(outerrorList);

				if (outerrorList.length > 0) {
					return;
				}

				$("#productOrderTotal").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#orderpersonMes").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#manageOrder_new").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				$("#manageOrder_m").disableContainer( {
					blankText : "—",
					formatNumber : formatNumberArray
				});
				var sums = $(".tourist-right").find("input[name='sum']");
				$.each(sums, function(key, value) {
					var _$span = $(value).next();
					_$span.text("￥" + _$span.text());
					;
				});

				if(flag) {
					//游客身份证类型不能修改
					$("input[name='papersType']").attr("disabled", true);
				}
				
				$("#secondDiv").hide();
				$("#thirdDiv").show();
				var _closeOrExpand = $(".closeOrExpand").eq(0);
				_closeOrExpand.parent().next().next(".orderPersonMsg").hide();
				$("#stepbar").removeClass("yd-step2").addClass("yd-step3");
			});
	$("#thirdToSecondTStepDiv").click(function() {
		//游客身份证类型可以修改
		$("input[name='papersType']").attr("disabled", false);
		$("#manageOrder_new").undisableContainer();
		$("#manageOrder_m").undisableContainer();

		if ($("#orderid").val() != "") {
			$("#productOrderTotal").undisableContainer();
			$("#orderpersonMes").undisableContainer();
		}

		$("#secondDiv").show();
		$("#thirdDiv").hide();
		// 如果是修改 这个地方不能显示

			if ($("#orderid").val() == "") {
				var _closeOrExpand = $(".closeOrExpand").eq(0);
				_closeOrExpand.parent().next().next(".orderPersonMsg").show();
			}

			$("#stepbar").removeClass("yd-step3").addClass("yd-step2");
		});
	$("#thirdToFourthStepDiv").click(function() {
			isdoSave = true;
			dosave(true);
		});

	$(".closeOrExpand").click(function() {
		var obj_this = $(this);
		if (obj_this.attr("class").match("ydClose")) {
			obj_this.removeClass("ydClose");
			obj_this.parent().next("[flag=messageDiv]").eq(0).show();
		} else {
			// 隐藏前需要先判断是否必填了
			outerrorList = new Array();
			_$form = obj_this.parent().next().find("form");
			if (_$form.validate( {
				showErrors : function(errorMap, errorList) {
					this.defaultShowErrors();
					outerrorList = outerrorList.concat(errorList);
				}
			}).form()) {
				obj_this.addClass("ydClose");
				obj_this.parent().next("[flag=messageDiv]").eq(0).hide();
				// obj_this.parent().next().next(".orderPersonMsg").show();
			} else {
				createDivInDiv(outerrorList);
				return false;
			}
		}
	});
});


//112 特殊需求过滤特殊字符
String.prototype.replaceSpecialDemand= function(regEx){
	if (!regEx){
		regEx = /[\“\”\‘\’\"\'\<\>\\]/g;
	}
	return this.replace(regEx, '');
};


function changePayPriceByCostChange($this) {
	var totalcost = 0;
	var costall = $this.parent().parent().parent().eq(0).find(
			"input[name='sum']");
	$.each(costall, function(key, value) {
		totalcost = totalcost + Number($(value).val());
	});
	var travler = $this.closest(".travelerTable");
	var src = travler.find("input[name='srcPrice']").val();
	var singleDiff = travler.find("input[name='singleDiff']").val();

	if (Number(singleDiff) + Number(src) + totalcost <= 0) {
		top.$.jBox.tip('游客结算价不可小于0', 'error');
	}

	travler.find("input[name='payPrice']").val(
			Number(singleDiff) + Number(src) + totalcost);
	travler.find("input[name='payPrice']").parent().find(".ydFont2").text(
			((Number(singleDiff) + Number(src) + totalcost)).toString()
					.formatNumberMoney('#,##0.00'));
	changeTotalPrice();
}

function changeTotalPrice() {
	recountIndexTraveler();
	var allPersonNum = $("#orderPersonelNum").val();
	if (allPersonNum == "" || allPersonNum == undefined) {
		allPersonNum = 0;
	}
	// 计算总人数
	var travelerTables = $("#traveler form.travelerTable");

	var payStatus = $("#payStatus").val();
	if (travelerTables.length == allPersonNum || travelerTables.length > allPersonNum) {
		$("#addTraveler").parent().hide();
	} else {
		$("#addTraveler").parent().show();
	}

	if (travelerTables.length > allPersonNum) {
		$("#orderPersonelNum").val(travelerTables.length);
	}

	// 所有数据相加
	var topPrice = 0;
	var prices = $("#traveler input[name='payPrice']");
	$.each(prices, function(key, value) {
		var tempnum = $(value).val();
		if (tempnum != "") {
			topPrice += Number(tempnum);
		}
	});
	// 总共需要计算的人数
	var len = prices.length;// 有名单的人数

	// 结算单价
	var jg = $("#travelerTemplate input[name='payPrice']").val();

	var etj = $("#etj").val();
	var crj = $("#crj").val();
	var tsj = $("#tsj").val();

	// 预订人数
	var adultNum = $("#orderPersonNumAdult").val();
	var childNum = $("#orderPersonNumChild").val();
	var specialNum = $("#orderPersonNumSpecial").val();
	// 实际人数
	if (Number(allPersonNum) - Number(len) > 0) {
		// 总人数
		topPrice += Number(Math.abs(adultNum - countAdult())) * crj
				+ Number(Math.abs(childNum - countChild())) * etj
				+ Number(Math.abs(specialNum - countSpecial())) * tsj;
	}
	$("#travelerSumPrice").text(topPrice.toString().formatNumberMoney('#,##0.00'));
	return topPrice;
}

function dodatePicker() {
	var birthdays = $("#traveler input[name='birthDay']");
	var issuePlaces = $("#traveler input[name='issuePlace']");
	var validityDates = $("#traveler input[name='validityDate']");
	// birthdays.datepicker({maxDate:new Date()});
	// issuePlaces.datepicker();
	// validityDates.datepicker();

	$.each(birthdays, function(key, value) {
		$(value).datepicker( {
			maxDate : new Date()
		});
	});
	$.each(issuePlaces, function(key, value) {
		$(value).datepicker();
	});
	$.each(validityDates, function(key, value) {
		$(value).datepicker();
	});
	changeTotalPrice();
}

// 订单基本信息 验证
function _doValidateorderpersonMesdtail() {
	var flag = true;
	var pot = $("#orderpersonMesdtail").validate( {
		showErrors : function(errorMap, errorList) {
			this.defaultShowErrors();
			outerrorList = outerrorList.concat(errorList);
		}
	}).form();
	if (!pot) {
		flag = false;
	}
	return outerrorList;
}
// 填写预订人信息 验证
function _doValidateproductOrderTotal() {
	var flag = true;
	var pot = $("#productOrderTotal").validate( {
		showErrors : function(errorMap, errorList) {
			this.defaultShowErrors();
			outerrorList = outerrorList.concat(errorList);
		}
	}).form();
	if (!pot) {
		flag = false;
	}
	return outerrorList;
}

function _doValidatecontactForm() {
	var forms = $("#contact form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = outerrorList.concat(errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	return outerrorList;
}

function _doValidatetravelerForm() {
	var forms = $("#traveler form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = adderrorList(outerrorList, errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	return outerrorList;
}

function validate() {
	var flag = true;
	var forms = $("#contact form");

	var pot = $("#productOrderTotal").validate( {}).form();
	if (!pot) {
		flag = false;
	}

	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = outerrorList.concat(errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});
	forms = $("#traveler form");
	$.each(forms, function(key, value) {
		var tempFlag = $(value).validate( {
			showErrors : function(errorMap, errorList) {
				this.defaultShowErrors();
				outerrorList = adderrorList(outerrorList, errorList);
			}
		}).form();
		if (!tempFlag) {
			flag = false;
		}
	});

	if (isdoSave) {
		createDiv(outerrorList);
	}

	return flag;
}
/**
 * 重新计算游客编号
 * @return
 */
function recountIndexTraveler() {
	var travelerTables = $("#traveler form");
	if (travelerTables.length <= 0) {
		$(".warningtravelerNum").text("暂无游客信息");
	} else {
		$(".warningtravelerNum").text("");
	}
	$.each(travelerTables, function(key, value) {
		var index = key + 1;
		$(value).find(".travelerIndex").text(index);
	});
}

function checkFreePosition() {
	// 现在预订人数
	var orderPersonelNum = $("#orderPersonelNum").val();

	var freePosition = $("#freePosition").val();
	var leftpayReservePosition = $("#leftpayReservePosition").val();
	var placeHolderType = $("#placeHolderType").val();
	if (placeHolderType == 1) {
		// 表示为切位 用切位数去判断余位
		freePosition = leftpayReservePosition;
	}

	// 余位数
	// 现在订单人数
	var orderPosition = $("#orderPosition").val();

	if (orderPosition == "" || orderPosition == undefined) {
		orderPosition = 0;
	}

	if (Number(orderPosition) <= 0 && Number(orderPersonelNum) <= 0) {
		top.$.jBox.tip('出行人数必须大于零', 'error');
		return false;
	}

	if ((Number(freePosition) + Number(orderPosition) - Number(orderPersonelNum)) < 0) {
		top.$.jBox.tip('余位数不足', 'error');
		return false;
	}
	return true;
}

function createDivInTable(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>")
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		var textTemp = $(valuein.element).parent().text()
				.replace(/[\:\：]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text().replace(/[\:\：]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus()
			};
		}(valuein.element));
		textTemp = textTemp.replace(valuein.message, "");
		textTemp = textTemp.replace(/人\s人/g, "人");
		textTemp = textTemp.replace(/儿童\s人/g, "儿童");
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	// letDivCenter(div[0]);
	isdoSave = false;
}

function createDivInDiv(errorList) {
	if ($("#showErrorDiv")) {
		$("#showErrorDiv").remove();
	}
	if (errorList.length <= 0) {
		return;
	}
	var div = $("<div id='showErrorDiv' class='show_m_div'></div>");
	var _closeSpan = $("<span title=\"关闭\" class=\"show_m_div_close\">&times;</span>")
	div.append($("<div class=\"show_m_div_title\">提示信息</div>").append(
			_closeSpan));
	_closeSpan.click(function() {
		$("#showErrorDiv").remove();
	});
	var _ul = $("<ul></ul>");
	_ul.appendTo(div);
	$.each(errorList, function(keyin, valuein) {
		var textTemp = $(valuein.element).parent().find("span").eq(0).text()
				.replace(/[\:\：\*]/g, '');
		if (!$.trim(textTemp)) {
			textTemp = $(valuein.element).prev().text()
					.replace(/[\:\：\*]/g, '');
		}
		if (!$.trim(textTemp)) {
			if ($(valuein.element).attr("name") == "sum") {
				textTemp = "金额";
			} else if ($(valuein.element).attr("name") == "name") {
				textTemp = "费用变更";
			}
		}
		textTemp = $.trim(textTemp) + "为";
		var modifyButton = $("<input type='button' value='修改'/>");
		modifyButton.click(function(element) {
			return function() {
				$(element).focus()
			};
		}(valuein.element));
		_ul.append($("<li></li>").append(
				$("<em>" + textTemp + valuein.message + "</em>")).append(
				modifyButton));
	});
	div.appendTo(document.body);
	// letDivCenter(div[0]);
	isdoSave = false;
}

function letDivCenter(divName) {
	var top = ($(window).height() - $(divName).height());
	var left = ($(window).width() - $(divName).width());
	var left = 0;
	// var scrollTop = $(document).scrollTop();
	// var scrollLeft = $(document).scrollLeft();
	var scrollTop = 0;
	var scrollLeft = 0;
	$(divName).css( {
		'top' : top + scrollTop,
		'left' : left + scrollLeft
	}).show();
}

function adderrorList(outerrorList, errorList) {
	outerrorList = outerrorList.concat(errorList);
	return outerrorList;
}

function changeorderPersonelNum() {
	$("#orderPersonelNum").val(
			Number($("#orderPersonNumChild").val())
					+ Number(($("#orderPersonNumAdult").val() == "" ? 0 : $(
							"#orderPersonNumAdult").val()))
					+ Number(($("#orderPersonNumSpecial").val() == "" ? 0 : $(
					"#orderPersonNumSpecial").val()))).blur();
}

function countAdult() {
	var radios = $("#traveler").find("input[name^='personType'][value=1]:checked");
	return radios.length;
}
function countChild() {
	var radios = $("#traveler").find("input[name^='personType'][value=2]:checked");
	return radios.length;
}
function countSpecial() {
	var radios = $("#traveler").find("input[name^='personType'][value=3]:checked");
	return radios.length;
}
/**
 * 从身份证信息处得到出生年月
 * @param obj
 * @return
 */
function getBirthday(obj){
	var sId = obj.value;
	var iSum=0 
    var sBirthday="" 
    if(sId.length==18) {
        if(!/^\d{17}(\d|x)$/i.test(sId)) {
			alert("号码不符合规定!前17位号码应全为数字,18位号码末位可以为数字或X");
			return false;
        }
        sId=sId.replace(/x$/i,"a"); 
        sBirthday=sId.substr(6,4)+"-"+Number(sId.substr(10,2))+"-"+Number(sId.substr(12,2)); 
        var d=new Date(sBirthday.replace(/-/g,"/")) 
        if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())) {
        	alert("非法生日");
        	return false;
        }
        for(var i = 17;i>=0;i --) iSum += (Math.pow(2,i) % 11) * parseInt(sId.charAt(17 - i),11) 
        if(iSum%11!=1) {
           alert("Error:非法证号");
           return false;
        }
        $(obj).parents("div.tourist-left").children().children().children("[name=travelerSex]").val(sId.substr(16,1)%2?"1":"2");
    } else if(sId.length==15) {
        if (!(/(^\d{15}$)/.test(sId))) {
        	alert("号码不符合规定!15位号码应全为数字");
        	return false;
        }
        sBirthday="19"+sId.substr(6,2)+"-"+Number(sId.substr(8,2))+"-"+Number(sId.substr(10,2));
        var d=new Date(sBirthday.replace(/-/g,"/")) 
        if(sBirthday!=(d.getFullYear()+"-"+ (d.getMonth()+1) + "-" + d.getDate())) {
        	alert("非法生日");
        	return false;
        }
    } else {
    	if (sId.length != 0) {
    		alert("身份证号码为15位或18位，请检查！");
    		return false;
    	}
	}
	$(obj).parents("div.tourist-left").children().children().children("input[name=birthDay]").val(sBirthday);
}
