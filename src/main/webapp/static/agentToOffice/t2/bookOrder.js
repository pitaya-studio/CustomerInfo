var sysCtx;
$(function () {
	sysCtx = $("#ctx").val();
});

function validatePrice(obj, salerId, orderId) {
	$.ajax({
		async : false,
		type : "POST",
		data : {
			orderId : orderId
		},
		url : sysCtx + "/t1/preOrder/manage/validatePrice",
		success : function (msg) {
			if (msg.result == "success") {
				agentType(obj, salerId, orderId);
			} else {
				$.jBox.confirm("您的产品价格有所变动！", "提示", function(v, h, f) {
					if (v == 'ok') {
						agentType(obj, salerId, orderId);
					} else if (v == 'cancel') {

					}
				});
			}
		}
	});
}

function agentType(obj, salerId, orderId) {
	
	changeSeenFlag(obj, orderId);
	
	// 删除多余样式
	$(".sign").removeClass("sign");
	
	//给点击的按钮添加指定的标记，方便找到对应的触发按钮
	$(obj).addClass("sign");
	//销售
	var salerHtml = getQuauqAgentSalers(salerId);
	//弹出框
	var $div = $("<div class=\"tanchukuang\"></div>");
	$div.append($('<div class="add_intermodalType"><label><span class="xing">*</span>订单来源：</label>').append("实时连通渠道"));
	var agentName = getAgentName(orderId);
	var agentInfoMain = $('<div class="add_intermodalType"><label><span class="xing">*</span>渠道选择：</label>').append(agentName);
	$div.append(agentInfoMain).append('<div class="add_intermodalType">' + salerHtml + '</div>');
	
	var payModeArr = $(obj).parent().children("input[name=payMode]").val().split(",");
	
	//付款方式
	var _orderTypeSelect = '<select class="orderTypeSelected">';
	; 
	
	if ($.inArray("1", payModeArr) != -1) {
		_orderTypeSelect += '<option value="1">订金占位</option>';
	}
	if ($.inArray("2", payModeArr) != -1) {
		_orderTypeSelect += '<option value="2">预占位</option>';
	}
	if ($.inArray("3", payModeArr) != -1) {
		_orderTypeSelect += '<option value="3">全款占位</option>';
	}
	if ($.inArray("4", payModeArr) != -1) {
		_orderTypeSelect += '<option value="4">资料占位</option>';
	}
	if ($.inArray("5", payModeArr) != -1) {
		_orderTypeSelect += '<option value="5">担保占位</option>';
	}
	if ($.inArray("6", payModeArr) != -1) {
		_orderTypeSelect += '<option value="6">确认单占位</option>';
	}
	if ($.inArray("7", payModeArr) != -1) {
		_orderTypeSelect += '<option value="7">计调确认占位</option>';
	}
	if ($.inArray("8", payModeArr) != -1) {
		_orderTypeSelect += '<option value="8">财务确认占位</option>';
	}
	_orderTypeSelect += '</select>';
	
	$div.append($('<div class="add_intermodalType"><div class="activitylist_team_co3_text">付款方式：</div>').append(_orderTypeSelect));
	$div.append("</div>");
	
	var html = $div.html();
	$.jBox(html, {
		title : "选择渠道和付款方式",
		persistent : true,
		buttons : {
			'预定' : 1
		},
		submit : function (v, h, f) {
			bookOrder(salerId, orderId);
		},
		height : 300,
		width : 600
	});
}

/**
 * quauq渠道显示批发商所有用户，默认当前登录用户
 */
function getQuauqAgentSalers(salerId) {
	var salerHtml = '<div class="activitylist_team_co3_text">销售：</div>';
	$.ajax({
		async : false,
		type : "POST",
		url : sysCtx + "/orderCommon/manage/getSalers4QUAUQ",
		success : function (msg) {
			var jsonLength = 0;
			var tempStr = '';
			for (var prop in msg) {
				if (prop == 'loginUserId' || prop == 'userNum') {
					continue;
				}
				jsonLength++;
				if (salerId == prop) {
					tempStr += '<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>';
				} else {
					tempStr += '<option value=' + prop + '>' + msg[prop] + '</option>';
				}
			}
			salerHtml += '<select id="salerId">';
			salerHtml = salerHtml+tempStr;
		}
	});
	return salerHtml;
}

function bookOrder(salerId, orderId) {
	$.ajax({
		async : false,
		type : "POST",
		data : {
			salerId : salerId,
			orderId : orderId
		},
		url : sysCtx + "/t1/preOrder/manage/validateBookOrder?dom" + Math.random(),
		success : function (data) {
			if (data.result == "success") {
				var payMode = $(".orderTypeSelected").val();
				//判断支付方式 1：未支付全款 2：未支付订金 3：预占位 7：计调占位
				var payStatus = "";
				if (payMode == "1") {
					payStatus = 2;
				} else if (payMode == "3") {
					payStatus = 1;
				} else if (payMode == "7") {
					payStatus = 7;
				} else if (payMode == "8") {
					payStatus = 8;
				} else {
					payStatus = 3;
				}
				var salerId = $("#salerId").val();
				window.open(sysCtx + "/orderCommon/manage/showforModify?payMode=" + payMode + "&payStatus=" + payStatus 
						+ "&productId=" + data.productId + "&productGroupId=" + data.productGroupId + "&agentId=" + data.agentId + "&placeHolderType=0" 
						+ "&differenceFlag=1" + "&preOrderId=" + data.preOrderId + "&activityKind=2" + "&salerId=" + salerId + "&agentSourceType=2");
			} else {
				top.$.jBox.tip(data.msg);
			}
		}
	});
}

function getAgentName(orderId) {
	var agentName = "";
	$.ajax({
		async : false,
		type : "POST",
		data : {
			orderId : orderId
		},
		url : sysCtx + "/t1/preOrder/manage/getAgentName?dom" + Math.random(),
		success : function (data) {
			if (data.result == "success") {
				agentName = data.agentName;
			} else {
				top.$.jBox.tip(data.msg);
			}
		}
	});
	return agentName;
}

/**
 * 修改没查看订单状态，改为已查看，并更新左边菜单栏没查看订单数字
 * @param obj
 * @param orderId
 */
function changeSeenFlag(obj, orderId) {
		$.ajax({
			type : "POST",
			url : sysCtx + "/t1/preOrder/manage/changeNotSeenOrderFlag?dom=" + Math.random(),
			cache : false,
			data : {
				orderId : orderId
			},
			success : function (data) {
				if (data.result = "success") {
					$(obj).parents("tr").find(".arrow_new").remove();
					var href = "t1/preOrder/manage/showT2OrderList";
					$("li[id^=childMenu]").each(function (index, obj) {
						if ($(this).html().indexOf(href) != -1) {
							var leftNum = $("span", this).text() - 1;
							if (leftNum == 0) {
								$("span", this).parent().remove();
								$("h2:contains('下单通知')").children("span:last").remove();
							} else {
								$("span", this).text(leftNum);
							}
							return false;
						}
					});
					
				}
			}
		});
}