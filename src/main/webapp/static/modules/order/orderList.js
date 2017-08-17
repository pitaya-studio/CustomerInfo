var contextPath;
var orderStatus;
var orderShowType;
$(function () {
	
	$("#activityCreate").comboboxInquiry();
	$("#proCreateBy").comboboxInquiry();
	$("#salerId").comboboxInquiry();
	$("#modifyAgentInfo").comboboxInquiry();
	contextPath = $("#ctx").val();
	orderStatus = $("#orderStatus").val();

	if ("${param.saveinvoiceMSG}" == "1") {
		top.$.jBox.tip('操作已成功!', 'success');
	}
	$(".qtip").tooltip({
		track : true
	});

	$(document).scrollLeft(0);
	$("#targetAreaId").val($("#tempTargetAreaIds").val());
	$("#targetAreaName").val($("#tempTargetAreaNamess").val());

	/* 前端js效果部分 */

	$("#contentTable").delegate("ul.caption > li", "click", function () {
		var iIndex = $(this).index();
		/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
		$(this).addClass("on").siblings().removeClass('on');
		$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
	});

	$('.handle').hover(function () {
		if ($(this).attr("delSomeCancelOp")) {
			delSomeCancelOp($(this).attr("delSomeCancelOp"), this);
		}
		if (0 != $(this).find('a').length) {
			$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
		}
	}, function () {
		$(this).removeClass('handle-on');
		$(this).find('dd').removeClass('block');
	});
	// 文本框中提示信息
	inputTips();
	//展开筛选按钮
	$('.zksx').click(function () {
		if ($('.ydxbd').is(":hidden") == true) {
			$('.ydxbd').show();
			// $(this).text('收起筛选');
			$(this).addClass('zksx-on');
		} else {
			$('.ydxbd').hide();
			// $(this).text('展开筛选');
			$(this).removeClass('zksx-on');
		}
	});
	//如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='orderNumOrGroupCode']");
	var searchFormselect = $("#searchForm").find("select[id!='orderShowType']");
	var inputRequest = false;
	var selectRequest = false;
	for (var i = 0; i < searchFormInput.length; i++) {
		if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			var inputVal = $(searchFormInput[i]).val();
			if (inputVal != null && inputVal != "不限" && inputVal != "全部") {
				inputRequest = true;
			}
		}
	}
	for (var i = 0; i < searchFormselect.length; i++) {
		if ($(searchFormselect[i]).children("option:selected").val() != "" &&
			$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if ($("#orderShowType").length > 0 && $("#orderShowType").children("option:selected").val() != "0") {
		selectRequest = true;
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}

	//团号与产品相互切换
	$("#contentTable").delegate(".tuanhao", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.chanpin_cen').removeClass('onshow');
		$('.tuanhao_cen').addClass('onshow');
	});

	$("#contentTable").delegate(".chanpin", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.tuanhao_cen').removeClass('onshow');
		$('.chanpin_cen').addClass('onshow');

	});

	//销售与下单人相互切换
	$("#contentTable").delegate(".salerId", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.createBy_cen').removeClass('onshow');
		$('.salerId_cen').addClass('onshow');
	});

	$("#contentTable").delegate(".createBy", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.salerId_cen').removeClass('onshow');
		$('.createBy_cen').addClass('onshow');

	});

	//总额与余额相互切换
	$("#contentTable").delegate(".total", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.remainder_cen').removeClass('onshow');
		$('.total_cen').addClass('onshow');
	});

	$("#contentTable").delegate(".remainder", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.total_cen').removeClass('onshow');
		$('.remainder_cen').addClass('onshow');

	});

	/* 展示支付凭证 */
	$(document).delegate(".showpayVoucher", "click", function () {
		var orderIDValue = $(this).attr("lang");
		$.ajax({
			type : "POST",
			url : contextPath + "/sys/docinfo/payVoucherList/" + orderIDValue,
			data : {
				orderId : orderIDValue
			},
			success : function (msg) {
				var htmls = "<table class='activitylist_bodyer_table t-type-jbox'><thead><tr><th>凭证所属ID</th><th>凭证名称</th><th>下载链接</th></tr></thead><tbody>";
				$.each(msg, function (key, value) {
					var docName = value.docName;
					var orderId = value.payOrderId;
					var id = value.id;
					var tempDocName = docName;
					if (tempDocName.length > 25) {
						tempDocName = tempDocName.substring(0, 25) + "...";
					}
					htmls = htmls + "<tr><td>" + orderId + "</td><td title='" + docName + "'>" + tempDocName + "</td><td><a class='downloadzfpz' lang=" + id + ">收款凭证</a></td></tr>";
				});
				htmls = htmls + "</tbody></table>";
				$.jBox.open(htmls, "凭证列表", 600, 240);
			}
		});
	});

	$(document).delegate(".downloadzfpz", "click", function () {
		window.open(contextPath + "/sys/docinfo/download/" + $(this).attr("lang"));
	});
	$(".spinner").spinner({
		spin : function (event, ui) {
			if (ui.value > 365) {
				$(this).spinner("value", 1);
				return false;
			} else if (ui.value < 0) {
				$(this).spinner("value", 365);
				return false;
			}
		}
	});

	var _$orderBy = $("#orderBy").val();
	if (!_$orderBy || _$orderBy == "") {
		_$orderBy = "id DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function () {
		if ($(this).hasClass("li" + orderBy[0])) {
			orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
			$(this).attr("class", "activitylist_paixu_moren");
		}
	});

	//订单列表和团期列表初始化：隐藏订单那或团期，判断显示那个标签
	var orderOrGroup = $("#orderOrGroup").val();
	if (orderOrGroup == "order") {
		$("#orderLabel").addClass("select");
	} else if (orderOrGroup == "group") {
		$("#groupLabel").addClass("select");
		if ($("#isNeedNoticeOrder").val() == 1) {
			//团期没查看订单
			$(".toptr").each(function (index, obj) {
				var notSeenOrderNum = $(this).next().find("[name=seenFlag][value=0]").length;
				if (notSeenOrderNum && notSeenOrderNum != 0) {
					var html = '<ul class="mainMenu-ul_new"><li><em><span>' + notSeenOrderNum + '</span><p></p></em></li></ul>';
					$("td:eq(0)", this).prepend(html);
				}
			});
		}
	}

	//收款确认提醒
	$(".notice_price").hover(function () {
		$(this).find("span").show();
	}, function () {
		$(this).find("span").hide();
	})

	//渠道联系人增加"..."
	sliceAgentContacts();
	
	//确认计调占位：只有第一次点击时才起作用
	$("a[name=confirmOrder]").each(function(index, obj){
		var orderId = $(this).attr("orderId");
		var sysCompanyUuid = $("#sysCompanyUuid").val();
		if (sysCompanyUuid == "dfafad3ebab448bea81ca13b2eb0673e") {
			$(this).on("click",function(){
				confirmOrder(orderId);
			});
		} else {
			$(this).one("click",function(){
				confirmOrder(orderId);
			});
		}
	});
	
	
	$(".transGroup").hover(function(event){
		$(this).next().show();
	}, function(event){
		$(this).next().hide();
	});

	orderShowType = $("#orderShowType").val();
});

$(window).load(function() {
	//获取订单返佣提醒
	getOrderRebates();
	//判断是否可转款
	transferCheck();
});

function delSomeCancelOp(orderIdAndOrderStatus, obj) {
	var orderId = orderIdAndOrderStatus.split("_")[0];
	var orderStatus = orderIdAndOrderStatus.split("_")[1];
	if (orderStatus == 1 || orderStatus == 2 || orderStatus == 3) {
		$.ajax({
			type : "POST",
			url : contextPath + "/orderCommon/manage/getOrderInfoById?dom=" + Math.random(),
			async : false,
			data : {
				orderId : orderId
			},
			success : function (msg) {
				if (msg && msg != "") {
					if (msg.orderType == "99" || msg.outOfTime == "1") {
						$("a", obj).each(function(index, object) {
							if ($(this).text() == "确认占位" || $(this).text() == "修改" || $(this).text() == "改价" || $(this).text() == "" ||
									$(this).text() == "取消" || $(this).text() == "返佣" || $(this).text() == "退团" ||
									$(this).text() == "转团" || $(this).text() == "解锁" || $(this).text() == "锁死" || 
									$(this).text() == "上传资料" || $(this).text() == "上传确认单" || $(this).text() == "退款" || $(this).text() == "借款") {
								$(this).attr("href", "javascript:void(0)");
								$(this).attr("onclick", "");
								$(this).click(function() {
									reloadPage();
								});
							}
						});
					}
				}
			}
		});
	}
}

function getOrderRebates() {
	$("input[name=orderNumForRebate]").each(function(index, obj) {
		var orderNo = $(this).val();
		$this = $(this);
		$.ajax({
			type : "POST",
			url : contextPath + "/orderList/manage/getOrderRebates?dom=" + Math.random(),
			async : true,
			data : {
				orderNo : orderNo
			},
			success : function (msg) {
				if (msg && msg != "") {
					var rebateArr = msg.split(";");
					var prebt = rebateArr[0];
					var infbt = rebateArr[1];
					if (prebt && infbt && prebt != "" && infbt != "") {
						var html = '<span class="icon-rebate"><span>预计返佣:' + prebt + '</br>实际返佣:' + infbt + '</span></span>';
						$("input[name=orderNumForRebate][value=" + orderNo + "]").after(html);
					}
				}
			}
		});
	});
}

function transferCheck() {
	$("a[name=transferMoney]").each(function(index, obj) {
		var orderId = $(this).attr("value");
		$this = $(obj);
		async : true,
		$.ajax({
			type : "POST",
			url : contextPath + "/orderList/manage/transferCheck?dom=" + Math.random(),
			data : {
				orderId : orderId
			},
			success : function (msg) {
				if (msg && msg == "true") {
					$(obj).show();
				}
			}
		});
	});
}

String.prototype.replaceColonChars = function (regEx) {
	if (!regEx) {
		regEx = /[\'\？]/g;
	}
	return this.replace(regEx, '');
}

/**
 * 订单删除
 *
 * param orderId
 */
function deleteOrderByFlag(orderId) {
	var flag = false;
	$.ajax({
		type : "POST",
		url : contextPath + "/orderCommon/manage/canCancelOrDelOrder",
		async : false,
		data : {
			orderId : orderId,
			type : 2
		},
		success : function (result) {
			if (result == "0") {
				flag = true;
			} else {
				top.$.jBox.tip(result, 'warning');
			}
		}
	});
	if (!flag) {
		return false;
	}
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function (v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				url : contextPath + "/orderCommon/manage/deleteOrderByFlag",
				data : {
					orderId : orderId
				},
				success : function (msg) {
					if (msg == 'ok') {
						top.$.jBox.tip('删除成功', 'warning');
						location.reload(true);
					} else {
						top.$.jBox.tip(msg, 'warning');
					}

				}
			});
		} else if (v == 'cancel') {}
	});
}

/**
 * 取消订单
 *
 * param orderId
 */
function cancelOrder(orderId) {

	$.ajax({
		type : "POST",
		url : contextPath + "/orderCommon/manage/canCancelOrDelOrder",
		data : {
			orderId : orderId,
			type : 1
		},
		success : function (result) {
			if (result == '0') {
				var $div = $('<div class=\"tanchukuang\"></div>')
					.append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入取消原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
				var html = $div.html();
				var submit = function (v, h, f) {
					if (v === 0) {
						return true;
					}
					if (f.description.length > 100) {
						top.$.jBox.tip('输入字数为100字以内。', 'warning');
						return false;
					} else if (v === 1) {
						$.ajax({
							type : "POST",
							url : contextPath + "/orderCommon/manage/cancelOrder",
							data : {
								orderId : orderId,
								description : f.description
							},
							success : function (msg) {
								if (msg == 'fail') {
									top.$.jBox.tip('订单不能取消', 'warning');
									top.$('.jbox-body .jbox-icon').css('top', '55px');
								} else if (msg == 'ok') {
									$("#btn_search").click();
								} else {
									top.$.jBox.tip(msg, 'warning');
									top.$('.jbox-body .jbox-icon').css('top', '55px');
								}
							}
						});
					}
					return false;
				};
				$.jBox(html, {
					title : "取消原因",
					buttons : {
						'确定' : 1,
						'取消' : 0
					},
					submit : submit
				});
			} else {
				top.$.jBox.tip(result, 'warning');
			}
		}
	});

}

/**
 * 撤销占位
 *
 * param orderId
 */
function revokeOrder(orderId) {

	$.jBox.confirm("确定撤销吗？", "系统提示", function (v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				url : contextPath + "/orderCommon/manage/revokeOrder",
				data : {
					orderId : orderId
				},
				success : function (msg) {
					if (msg == 'fail') {
						top.$.jBox.tip('订单不能撤销占位', 'warning');
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					} else if (msg == 'ok') {
						$("#btn_search").click();
					} else {
						top.$.jBox.tip(msg, 'warning');
						top.$('.jbox-body .jbox-icon').css('top', '55px');
					}
				}
			});
		} else if (v == 'cancel') {}
	});
}

/**
 * 修改支付凭证
 *
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId) {
	window.open(contextPath + "/orderCommon/manage/modifypayVoucher/" + payId + "/" + orderId);
}

/**
 * 支付记录
 *
 * param orderId
 * param obj
 */
function showOrderPay(orderId, orderType, payStatus, obj) {
	var sbrtr = $(obj).parents("tr").next();
	var sbrtd = sbrtr.children().eq(0);
	var table = sbrtr.find("table[id=table_orderPay]");
	table.parent().attr("colspan", $(obj).parents("tr").children("td").length);
	var flag = true;
	if ($("tr", table).length <= 1) {
		$.ajax({
			type : "POST",
			async : false,
			url : contextPath + "/orderList/manage/getPayList",
			data : {
				orderId : orderId,
				orderType : orderType,
				payStatus : payStatus
			},
			success : function (msg) {
				
				var orderPayList = msg.orderPayList;
				if (!orderPayList) {
					top.$.jBox.tip("没有收款记录", 'warning');
					flag = false;
					return false;
				}
				for (var i = 0; i < orderPayList.length; i++) {
					var orderPay = orderPayList[i];
					var html = '<tr>';
					html += '<td class="tc">' + orderPay.payTypeName + '</td>';
					html += '<td class="tr">' + orderPay.payMoney + '</td>';
					if (orderPay.differenceMoney != "") {
						html += '<td class="tr">' + orderPay.differenceMoney + '</td>';
					}
					html += '<td class="tc">' + orderPay.createDate + '</td>';
					var payPriceType = orderPay.payPriceType;
					if (payPriceType == "1") {
						payPriceType = "收全款";
					} else if (payPriceType == "3") {
						payPriceType = "收订金";
					}
					if (payPriceType == "2") {
						payPriceType = "收尾款";
					}
					html += '<td class="tc">' + payPriceType + '</td>';
					var notice;
					if (!orderPay.isAsAccount) {
						notice = "未达账";
					} else if (orderPay.isAsAccount == 0) {
						notice = "已撤销";
					} else if (orderPay.isAsAccount == 1) {
						notice = "是";
					} else if (orderPay.isAsAccount == 2) {
						if (!orderPay.rejectReason) {
							notice = "已驳回";
						} else {
							notice += '<div  class="pr xuanfudiv">已驳回<div class="ycq xuanfu" style="width: 24px;">备注</div>';
							notice += '<div class="hover-title team_top hide" id="hoverWindow" >' + orderPay.rejectReason + '</div>';
						}
					}
					html += '<td class="tc">' + notice + '</td>';
					if (!orderPay.payVoucher) {
						html += '<td class="tc">暂无收款凭证</td>';
					} else {
						html += '<td class="tc"><a class="showpayVoucher" lang="' + orderPay.id + '">收款凭证</a></td>';
					}
					if (payStatus == 4 || payStatus == 5) {
						html += '<td class="tc"><a href="javascript:void(0)" onClick="changepayVoucher(' + orderPay.id + ',' + orderId + ')">改收款凭证</a></td>';
					}
					html += "<tr>";
					$("thead", table).after(html);
				}
				table.show();
			}
		});
	}
	if (flag && $(obj).hasClass("jtk")) {
		var td = $(obj).closest("td");
		if (sbrtr.is(":hidden")) {
			sbrtr.show();
			td.addClass("td-extend");
		} else {
			sbrtr.hide();
			td.removeClass("td-extend");
		}
	}
}

//刷新
function refresh() {
	setTimeout(location.reload(true), 10000);
}

/**
 * 订单详情
 *
 * param orderId
 */
function orderDetail(orderId) {
	window.open(contextPath + "/orderCommon/manage/orderDetail/" + orderId, "_blank");
}

/**
 * 生成订单前的验证
 * xu.wang
 * param orderId
 */
function generateOrderValidate(orderId) {
	$.ajax({
		type : "POST",
		async : false,
		url : contextPath + "/orderCommon/manage/generateOrderValidate",
		dataType : "json",
		data : {
			orderId : orderId		
		},
		success : function (result) {
			if(result.code == "1"){
				$.jBox.confirm(result.msg,'系统提示',function(v){
					if(v=='ok'){
						generateOrder(orderId);
					}
				},
				{buttonsFocus:1});
			}else if(result.code == "2"){
				generateOrder(orderId);
			}else{
				$.jBox.tip('系统异常，请重新操作!', 'warning');
			}
		}
	});
}

/**
 * 生成订单
 * xu.wang
 * param orderId
 */
function generateOrder(orderId) {
	$.ajax({
		type : "POST",
		async : false,
		url : contextPath + "/orderCommon/manage/generateOrder",
		dataType : "json",
		data : {
			orderId : orderId		
		},
		success : function (result) {
			if(result.code == "1"){
				$.jBox.tip('成功生成订单', 'info');
				query(0);
			}else if (result.code == "2"){
				$.jBox.tip('余位不足，未生成订单!', 'warning');
				query(0);
			}else{
				$.jBox.tip('系统异常，请重新操作!', 'warning');
			}
		}
	});
}

/**
 * 查看订单是否可支付
 * @param orderId
 * @param payPriceType
 * @returns {Boolean}
 */
function orderPay(payPriceType, orderId, orderNum, orderType, orderCompany, totalMoney, orderDetailUrl) {
	$.ajax({
		type : "POST",
		async : false,
		url : contextPath + "/orderCommon/manage/whetherCanPay",
		dataType : "json",
		data : {
			orderId : orderId,
			payPriceType : payPriceType
		},
		success : function (result) {
			var data = eval(result);
			if (data && (data[0].flag == "true" || data[0].flag == "choose")) {

				var param = "orderId=" + orderId
					 + "&orderNum=" + orderNum
					 + "&payPriceType=" + data[0].payPriceType
					 + "&orderType=" + orderType
					 + "&businessType=1"
					 + "&isCommonOrder=yes"
					 + "&agentId=" + orderCompany
					 + "&paramCurrencyId=" + data[0].moneyCurrencyId
					 + "&paramCurrencyPrice=" + data[0].moneyCurrencyPrice
					 + "&paramTotalCurrencyId=" + data[0].totalMoneyCurrencyId
					 + "&paramTotalCurrencyPrice=" + data[0].totalMoneyCurrencyPrice
					 + "&orderDetailUrl=" + decodeURIComponent(orderDetailUrl);

				if (data[0].flag == "choose") {
					top.$.jBox.confirm(data[0].warning, '系统提示', function (v) {
						if (v == 'ok') {
							window.open(contextPath + "/orderPay/pay?" + param);
						} else {
							return;
						}
					}, {
						buttonsFocus : 1
					});
					top.$('.jbox-body .jbox-icon').css('top', '55px');
					return false;
				} else {
					window.open(contextPath + "/orderPay/pay?" + param);
				}
			} else {
				var tips = data[0].warning;
				top.$.jBox.info(tips, "警告", {
					width : 250,
					showType : "slide",
					icon : "info",
					draggable : "true"
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
}

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	reloadPage();
}

function reloadPage() {
	var showType = $("#showType").val();
	if (showType == '1000') {
		$('#searchForm').attr("action", contextPath + "/applyOrderCommon/manage/showApplyOrderList/" + showType + "/" + orderStatus + ".htm");
	} else {
		$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + showType + "/" + orderStatus + ".htm");
	}
	$("#searchForm").submit();
	return false;
}

/**
 * 发票申请
 *
 * param groupid
 * param orderid
 * param invoiceid
 * param orderCompany
 */
function makinvoice(orderNum, orderId, orderType) {
	window.open(contextPath + "/orderInvoice/manage/applyInvoice?orderId=" + orderId + "&orderNum=" + orderNum + "&orderType=" + orderType);
}
function makReceipt(orderNum, orderId, orderType) {
	window.open(contextPath + "/orderReceipt/manage/applyReceipt?orderId=" + orderId + "&orderNum=" + orderNum + "&orderType=" + orderType);
}

/**
 * 转款申请
 * param orderId
 */
function transfersMoney(orderId) {
	window.open(contextPath + "/orderCommon/transferMoney/transfersMoneyHref/" + orderId);
}
/**
 * 转款申请(新)
 * param orderId 订单ID
 */
function transfersMoneyNew(orderId) {
	window.open(contextPath + "/singlegrouporder/transfermoney/transfersMoneyApplyList/" + orderId);
}

$(function () {
	$.fn.datepicker = function (option) {
		var opt = {}

		 || option;
		this.click(function () {
			WdatePicker(option);
		});
	}

	$("#groupOpenDate").datepicker({
		dateFormat : "yy-mm-dd",
		dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
		closeText : "关闭",
		prevText : "前一月",
		nextText : "后一月",
		monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
	});

	$("#groupCloseDate").datepicker({
		dateFormat : "yy-mm-dd",
		dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
		closeText : "关闭",
		prevText : "前一月",
		nextText : "后一月",
		monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
	});

	$("#orderTimeBegin").datepicker({
		dateFormat : "yy-mm-dd",
		dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
		closeText : "关闭",
		prevText : "前一月",
		nextText : "后一月",
		monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
	});
	$("#orderTimeEnd").datepicker({
		dateFormat : "yy-mm-dd",
		dayNamesMin : ["日", "一", "二", "三", "四", "五", "六"],
		closeText : "关闭",
		prevText : "前一月",
		nextText : "后一月",
		monthNames : ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"]
	});

});

function sortby(sortBy, obj) {
	var temporderBy = $("#orderBy").val();
	if (temporderBy.match(sortBy)) {
		sortBy = temporderBy;
		if (sortBy.match(/ASC/g)) {
			sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
		} else {
			sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
		}
	} else {
		sortBy = sortBy + " DESC";
	}

	$("#orderBy").val(sortBy);
	var showType = $("#showType").val();
	if (showType == '1000') {
		$('#searchForm').attr("action", contextPath + "/orderCommon/manage/showApplyOrderList/" + showType + "/" + orderStatus + ".htm");
	} else {
		$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + showType + "/" + orderStatus + ".htm");
	}
	$("#searchForm").submit();
}

function exportExcelUserList(){
	var showType = $("#showType").val();
	if (showType == '1000') {
		alert("1");
//		$('#searchForm').attr("action", contextPath + "/orderCommon/manage/showApplyOrderList/" + showType + "/" + orderStatus + ".htm");
	} else {
		$('#searchForm').attr("action", contextPath + "/orderList/manage/ExportExcelOfUserlist/" + showType + "/" + orderStatus + ".htm");
	}
	$("#searchForm").submit();
}

/**
 * 退团
 *
 * param orderId
 */
function applyLeague(orderId) {

	var $div = $('<div class=\"tanchukuang\"></div>')
		.append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入退团原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
	var html = $div.html();
	var submit = function (v, h, f) {
		if (v === 0) {
			return true;
		}
		if (f.description.length > 100) {
			top.$.jBox.tip('输入字数为100字以内。', 'warning');
			return false;
		} else if (v === 1) {
			$.ajax({
				type : "POST",
				url : contextPath + "/orderCommon/manage/applyLeague",
				data : {
					orderId : orderId,
					description : f.description
				},
				success : function (msg) {
					location.reload();
				}
			});
		}
		return false;
	};
	$.jBox(html, {
		title : "退团原因",
		buttons : {
			'确定' : 1,
			'取消' : 0
		},
		submit : submit
	});
}

/**
 * 订单激活
 *
 * param orderId
 */
function submitInvokeOrder(orderId, type, invokeDay, invokeHour, invokeMin) {
	var noticeType;
	if (type == 'invoke') {
		noticeType = "激活";
	} else {
		noticeType = "延时";
	}
	$.ajax({
		type : "POST",
		url : contextPath + "/orderCommon/manage/invokeOrder?dom=" + Math.random(),
		data : {
			orderId : orderId,
			type : type,
			invokeDay : invokeDay,
			invokeHour : invokeHour,
			invokeMin : invokeMin
		},
		success : function (msg) {
			if (msg == 'fail') {
				top.$.jBox.tip(noticeType + '失败', 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			} else if (msg == 'success') {
				top.$.jBox.tip(noticeType + '成功', 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
				$("#btn_search").click();
			} else {
				top.$.jBox.tip(msg, 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
}

function invokeOrder(orderId, type) {

	if (type == "delay") {
		var $pop = $.jBox($('#jbox-delay-o').html(), {
				title : '延长占位时间',
				buttons : {
					'提交' : '1',
					'关闭' : '2'
				},
				persistent : true,
				submit : function (v, h, f) {
					if (v == '1') {
						var invokeDay = h.find("input:eq(0)").val();
						var invokeHour = h.find("input:eq(1)").val();
						var invokeMin = h.find("input:eq(2)").val();
						if (invokeDay == "" && invokeHour == "" && invokeMin == "") {
							$pop.find(".spinner_day").val("");
							top.$.jBox.info("保留时限不能为空", "警告");
							return false;
						}
						submitInvokeOrder(orderId, type, invokeDay, invokeHour, invokeMin);
					}
				},

				width : 500,
				height : 'auto'
			});
	} else {
		var flag = true;
		var day = 0;
		var hour = 0;
		var minute = 0;
		$.ajax({
			type : "POST",
			async : false,
			url : contextPath + "/orderCommon/manage/getProductRemainDayInfo?dom=" + Math.random(),
			data : {
				orderId : orderId
			},
			success : function (msg) {
				if (msg.result == 'success') {
					// 如果占位方式存在保留时限，则需要弹框填写保留时限
					if (msg.payMode == 3 || msg.payMode == 7 || msg.payMode == 8) {
						submitInvokeOrder(orderId, type, null, null, null);
						flag = false;
					}
					$("input[name=spinner_activityDay]").attr("value", msg.day);
					$("input[name=spinner_activityHour]").attr("value", msg.hour);
					$("input[name=spinner_activityMin]").attr("value", msg.minute);
				} else {
					top.$.jBox.tip(msg.msg, 'warning');
					top.$('.jbox-body .jbox-icon').css('top', '55px');
					flag = false;
				}
			}
		});
		if (!flag) {
			return false;
		}
		var $pop = $.jBox($('#jbox-active-o').html(), {
				title : '激活时限',
				buttons : {
					'提交' : '1',
					'关闭' : '2'
				},
				persistent : true,
				submit : function (v, h, f) {
					if (v == '1') {
						var invokeDay = h.find("input:eq(0)").val();
						var invokeHour = h.find("input:eq(1)").val();
						var invokeMin = h.find("input:eq(2)").val();
						if (invokeDay == "" && invokeHour == "" && invokeMin == "") {
							$pop.find(".spinner_day").val("");
							top.$.jBox.info("保留时限不能为空", "警告");
							return false;
						}
						submitInvokeOrder(orderId, type, invokeDay, invokeHour, invokeMin);
					}
				},
				width : 500,
				height : 'auto'
			});
	}
	setRemainDayValidatePara($pop);
}

function setRemainDayValidatePara ($pop) {
	
	$pop.find("input[name^='spinner']").keyup(function () {

		if ($pop.find(".spinner_day").val() > 365) {
			$pop.find(".spinner_day").val("");
			top.$.jBox.info("保留天数需为0~365的正整数!", "警告");
			return;
		}
		if ($pop.find(".spinner_hour").val() > 23) {
			$pop.find(".spinner_hour").val("");
			top.$.jBox.info("保留小时数需为0~23的正整数!", "警告");
			return;
		}
		if ($pop.find(".spinner_min").val() > 59) {
			$pop.find(".spinner_min").val("");
			top.$.jBox.info("保留分钟数需为0~59的正整数!", "警告");
			return;
		}

	});

	$pop.find('.spinner_day').spinner({
		spin : function (event, ui) {
			if (ui.value > 365) {
				$(this).spinner("value", 1);
				return false;
			} else if (ui.value <= 0) {
				$(this).spinner("value", 365);
				return false;
			}
		}
	});

	$pop.find(".spinner_hour").spinner({
		spin : function (event, ui) {
			if (ui.value > 23) {
				$(this).spinner("value", 0);
				return false;
			} else if (ui.value < 0) {
				$(this).spinner("value", 23);
				return false;
			}
		}
	});
	$pop.find(".spinner_min").spinner({
		spin : function (event, ui) {
			if (ui.value > 59) {
				$(this).spinner("value", 0);
				return false;
			} else if (ui.value < 0) {
				$(this).spinner("value", 59);
				return false;
			}
		}
	});
}


/**
 * 确认占位
 *
 * param orderId
 */
function confirmOrder(orderId) {
	
	var sysCompanyUuid = $("#sysCompanyUuid").val();
	if (sysCompanyUuid == "dfafad3ebab448bea81ca13b2eb0673e") {
		var $pop = $.jBox($('#jbox-confirm-o').html(), {
			title : '保留时限',
			buttons : {
				'提交' : '1',
				'关闭' : '2'
			},
			persistent : true,
			submit : function (v, h, f) {
				if (v == '1') {
					var invokeDay = h.find("input:eq(0)").val();
					var invokeHour = h.find("input:eq(1)").val();
					var invokeMin = h.find("input:eq(2)").val();
					if (invokeDay == "" && invokeHour == "" && invokeMin == "") {
						$pop.find(".spinner_day").val("");
						top.$.jBox.info("保留时限不能为空", "警告");
						return false;
					}
					submitConfirmOrder(orderId, invokeDay, invokeHour, invokeMin);
				}
			},

			width : 500,
			height : 'auto'
		});
		setRemainDayValidatePara($pop);
	} else {
		submitConfirmOrder(orderId, 0, 0, 0);
	}
}

function submitConfirmOrder(orderId, invokeDay, invokeHour, invokeMin) {
	$.ajax({
		type : "POST",
		url : contextPath + "/orderCommon/manage/confirmOrder?dom=" + Math.random(),
		data : {
			orderId : orderId,
			invokeDay : invokeDay,
			invokeHour : invokeHour,
			invokeMin : invokeMin
		},
		success : function (msg) {
			if (msg == 'fail') {
				top.$.jBox.tip('占位失败', 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			} else if (msg == 'success') {
				top.$.jBox.tip('占位成功', 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
				$("#btn_search").click();
			} else {
				top.$.jBox.tip(msg, 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
}

/**
 * 查询条件重置
 *
 */
var resetSearchParams = function () {
	$(':input', '#searchForm')
	.not(':button, :submit, :reset, :hidden')
	.val('')
	.removeAttr('checked')
	.removeAttr('selected');
	$('#targetAreaId').val('');
	$('#activityCreate').val('');
	$('#proCreateBy').val('');
	$('#salerId').val('');
	$('#modifyAgentInfo').val('').next("span:eq(0)").children("input").attr("title", "全部");
	$('#orderShowType').val('0');
}

/**
 * 转正
 *
 * param preproductorderId
 * param obj
 */
function changeToOrder(preproductorderId, obj) {
	window.open(contextPath + "/orderCommon/manage/applygetOderInfoById?preproductorderById=" + preproductorderId);
}

/**
 * 订单查询
 * @param orderStatus 订单状态
 * @param orderType 订单类型
 * @param orderOrGroup 订单或者团期
 */
function getOrderList(orderStatus, orderType, orderOrGroup) {
	resetSearchParams();
	$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + orderStatus + "/" + orderType + ".htm");
	$('#orderOrGroup').val(orderOrGroup);
	$('#searchForm').submit();
}

function query(showType) {
	if (showType == '1000') {
		$('#searchForm').attr("action", contextPath + "/applyOrderCommon/manage/showApplyOrderList/" + showType + "/" + orderStatus + ".htm");
	} else {
		if ($("#orderShowType").size() > 0) {
			showType = $("#orderShowType").val();
		}
		$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + showType + "/" + orderStatus + ".htm");
	}
	$('#searchForm').submit();
}

/**
 * 按部门查询订单
 *
 * param departmentId
 */
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	var showType = $("#showType").val();
	if (showType == '1000') {
		$('#searchForm').attr("action", contextPath + "/applyOrderCommon/manage/showApplyOrderList/" + showType + "/" + orderStatus + ".htm");
	} else {
		$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + showType + "/" + orderStatus + ".htm");
	}
	$('#orderShowType').val("");
	$("#searchForm").submit();
}

/**
 * 订单或团期列表转换
 *
 */
function orderOrGroupList(type) {
	$("#orderBy").val("");
	if (type == "order") {
		$("#orderOrGroup").val("order");
	} else if (type == "group") {
		$("#orderOrGroup").val("group");
	}

	var showType = $("#showType").val();
	if (showType == '1000') {
		$('#searchForm').attr("action", contextPath + "/applyOrderCommon/manage/showApplyOrderList/" + showType + "/" + orderStatus + ".htm");
	} else {
		$('#searchForm').attr("action", contextPath + "/orderList/manage/showOrderList/" + showType + "/" + orderStatus + ".htm");
	}
	$('#orderShowType').val("");
	$("#searchForm").submit();
}

/**
 * 展开收起
 */
function expand(child, obj) {
	if ($(child).is(":hidden")) {
		$(obj).html("收起");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
		if ($("#isNeedNoticeOrder").val() == 1) {
			changeSeenFlag(child);
		}
	} else {
		if (!$(child).is(":hidden")) {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开");
		}
	}
}

/**
 * 修改没查看订单状态，改为已查看，并更新左边菜单栏没查看订单数字
 * @param child
 */
function changeSeenFlag(child) {
	var groupId = child.split("_")[1];
	var notSeenOrderIds = "";
	$("[name=" + groupId + "] :input[name=seenFlag][value=0]").each(function (index, obj) {
		notSeenOrderIds += "," + $(this).attr("id").split("_")[1];
	});
	if (notSeenOrderIds != "") {
		$.ajax({
			type : "POST",
			url : contextPath + "/orderCommon/manage/changeNotSeenOrderFlag?dom=" + Math.random(),
			cache : false,
			data : {
				notSeenOrderIds : notSeenOrderIds
			},
			success : function (data) {
				if (data.result = "success") {
					//去除团期团号旁边提醒
					$(child).prev().find("td:eq(0)").find("ul").remove();
					$(child).prev().find("td:eq(0)").find("div").remove();

					//更改没查看订单状态，改为已查看
					$("[name=" + groupId + "] :input[name=seenFlag][value=0]").each(function (index, obj) {
//						$(this).value("1");
						$(this).attr("value",1);
					});

					//更新左边菜单栏没查看订单数字
					var href = "/" + $("#orderStatus").val();
					$("li[id^=childMenu]").each(function (index, obj) {
						if ($(this).html().indexOf(href) != -1) {
							var leftNum = $("span", this).text() - data.changeSum;
							if (leftNum == 0) {
								$("span", this).parent().remove();
								if ($("li[id^=childMenu] a em").length == 0) {
									$("h2:contains('订单')").children("span:last").remove();
								}
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
}

var notSeenIds = "";
function changeSeenFlagByMouse(orderId, obj) {
	if ($("[name=seenFlag]", obj).val() == 0) {
		notSeenIds += "," + orderId;
	}
	changeOrderSeenFlag();
}

function changeOrderSeenFlag() {
	if (notSeenIds != "") {
		$.ajax({
			type : "POST",
			url : contextPath + "/orderCommon/manage/changeNotSeenOrderFlag?dom=" + Math.random(),
			cache : false,
			data : {
				notSeenOrderIds : notSeenIds
			},
			success : function (data) {
				if (data.result = "success") {
					//更改没查看订单状态，改为已查看
					var idArr = notSeenIds.split(",");
					for (var i = 0; i < idArr.length; i++) {
						$("#order_" + idArr[i]).val("1");
						$("#order_" + idArr[i]).parent().parent().find(".new-tips").remove();
					}

					//更新左边菜单栏没查看订单数字
					var href = "/" + $("#orderStatus").val();
					$("li[id^=childMenu]").each(function (index, obj) {
						if ($(this).html().indexOf(href) != -1) {
							var leftNum = $("span", this).text() - data.changeSum;
							if (leftNum == 0) {
								$("span", this).parent().remove();
								if ($("li[id^=childMenu] a em").length == 0) {
									$("h2:contains('订单')").children("span:last").remove();
								}
							} else {
								$("span", this).text(leftNum);
							}
							notSeenIds = "";
							return false;
						}
					});
				}
			}
		});
	}
}

//setInterval("changeOrderSeenFlag()", 1000);

/**
 * 导出订单中关于游客信息
 * param orderId 订单ID或下载文件ID
 * param downloadType 下载类型：游客资料、出团通知、确认单、面签通知
 */
function downloadData(orderId, downloadType) {
	if ("traveler" == downloadType) {
		if (existData(orderId)) {
			$("#orderId").val(orderId);
			$("#downloadType").val(downloadType);
			$("#exportForm").submit();
		}
	} else if ("confirmation" == downloadType || "group" == downloadType) {
		window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/download/" + orderId)));
	}
}

function downloadFiles4SepcialDemand(ids) {
	window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/zipdownload/" + ids + "/特殊需求")));
}

//导出团期中关于游客信息
function exportExcel(groupId, groupCode, orderType) {
	$.ajax({
		type : "POST",
		url : contextPath + "/activity/manager/existExportData",
		dataType : "json",
		cache : false,
		data : {
			groupId : groupId,
			status : 'customer',
			orderType : orderType
		},
		success : function (result) {
			var data = eval(result);
			if (data && data[0].flag == "true") {
				$("#groupId").val(groupId);
				$("#groupCode").val(groupCode);
				$("#exportTravelesForm").submit();
			} else {
				var tips = data[0].warning;
				top.$.jBox.info(tips, "警告", {
					width : 250,
					showType : "slide",
					icon : "info",
					draggable : "true"
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
}
/**
 *以excel表格的形式导出团控单--0507需求
 * 
 * @author zhanyu.gu 
 */

function exportActivityGroupExcel(groupId,orderType,groupCode,orderIds) {
	$.ajax({
		type : "POST",
		url : contextPath + "/activity/manager/existExportGroupData",
		dataType : "json",
		cache : false,
		data : {
			groupId : groupId,
			orderType : orderType,
			/*groupCode : groupCode,*/
			orderIds : orderIds
		},
		success : function (result) {
			var data = eval(result);
			if (true) {
				$("#groupId1").val(groupId);
				$("#groupCode1").val(groupCode);
				$("#orderIds1").val(orderIds);
				$("#exportTravelesForm1").submit();
			} else {
				var tips = data[0].warning;
				top.$.jBox.info(tips, "警告", {
					width : 250,
					showType : "slide",
					icon : "info",
					draggable : "true"
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
}




/**
 * 上传资料
 * param type 上传类型：游客资料、确认单
 * param orderId 订单ID
 */
function uploadData(type, orderId) {
	if ("traveler" == type && existData(orderId)) {
		window.open(contextPath + "/orderCommon/manage/uploadTravelerInfoHref/" + orderId + "/" + orderStatus + "");
	} else if ("confirmation" == type) {
		window.open(contextPath + "/orderCommon/manage/uploadConfirmationHref/" + orderId + "/" + orderStatus + "");
	}
}

/**
 * 验证订单是否有游客信息
 * param orderId 订单ID
 */
function existData(orderId) {
	var flag = false;
	$.ajax({
		type : "POST",
		async : false,
		url : contextPath + "/orderCommon/manage/existExportData",
		dataType : "json",
		data : {
			orderId : orderId
		},
		success : function (result) {
			var data = eval(result);
			if (data && data[0].flag == "true") {
				flag = true;
			} else {
				var tips = data[0].warning;
				top.$.jBox.info(tips, "警告", {
					width : 250,
					showType : "slide",
					icon : "info",
					draggable : "true"
				});
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
	return flag;
}
/**
 * 查看团签
 * param id   订单唯一标识
 * author     chenry
 * createDate 2014-11-03
 */
function viewGroupVisa(id) {
	window.open(contextPath + "/orderCommon/manage/viewGroupVisa?id=" + 265);
}
/**
 * 退团
 * param id   订单唯一标识
 * author     chenry
 * createDate 2014-11-04
 */
function viewExitGroup(id, orderType) {
	window.open(contextPath + "/orderCommon/manage/viewExitGroup?productType=" + orderType + "&flowType=8&orderId=" + id);
}

/**
 * 退团(new)
 * param id   订单唯一标识
 * author     yunpeng.zhang
 * createDate 2015年11月18日12:30:54
 */
function newViewExitGroup(id, orderType) {
	window.open(contextPath + "/singleOrder/exitGroup/newExitGroupList?productType=" + orderType + "&flowType=8&orderId=" + id);
}

/**
 * 团队退款
 * param id   订单唯一标识
 * author     chenry
 * createDate 2014-11-04
 */
function viewGroupRefund(id, orderType) {
	window.open(contextPath + "/orderCommon/manage/viewGroupRefund?productType=" + orderType + "&&flowType=1&orderId=" + id);
}

/**
 * 新的退款
 * param id   订单唯一标识
 * author     yunpeng.zhang
 * createDate 2015年11月30日
 */
function newViewGroupRefund(id, orderType) {
	window.open(contextPath + "/singleOrder/refund/newGroupRefundList?productType=" + orderType + "&&flowType=1&orderId=" + id);
}

/**
 * 申请借款列表
 * param id   订单唯一标识
 * author     chenry
 * createDate 2015-05-12
 */
function viewBorrowingList(id, orderType) {
	window.open(contextPath + "/orderCommon/manage/viewBorrowingList?productType=" + orderType + "&flowType=19&orderId=" + id);
}
/**
 * 申请借款
 * param id   订单唯一标识
 * author     chenry
 * createDate 2015-05-12
 */
function viewBorrowing(id, orderType) {
	window.open(contextPath + "/orderCommon/manage/applyBorrowing?productType=" + orderType + "&orderId=" + id);
}
/**
 * 订单页面查看订单发票信息列表
 * param orderNum   订单号
 * author     chenry
 * createDate 2014-11-14
 */
function viewInvoiceInfo(orderId, orderNum, orderType, activityId) {
	window.open(contextPath + "/orderInvoice/manage/getInvoiceListByOrderNum?orderId=" + orderId + "&orderNum=" + orderNum + "&orderType=" + orderType + "&activityId=" + activityId);

}

function applyInvoiceInfo(orderId, orderNum, orderType) {
	$.ajax({
		type : "post",
		async : false,
		url : contextPath + "/orderInvoice/manage/validateOrder",
		data : {
			orderNum : orderNum,
			orderId : orderId,
			orderType : orderType
		},
		success : function (msg) {
			if ("success" == msg.msg) {
				window.open(contextPath + "/orderInvoice/manage/applyInvoice?orderId=" + orderId + "&orderNum=" + orderNum + "&orderType=" + orderType);
			} else if ("canOrDel" == msg.msg) {
				top.$.jBox.tip('已取消或已删除订单不能开发票！', 'success');
			} else {
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！', 'success');
			}
		}
	});

}

function applyReceiptInfo(orderId, orderNum, orderType) {
	$.ajax({
		type : "post",
		async : false,
		url : contextPath + "/orderInvoice/manage/validateOrder",
		data : {
			orderNum : orderNum,
			orderId : orderId,
			orderType : orderType
		},
		success : function (msg) {
			if ("success" == msg.msg)
				window.open(contextPath + "/orderReceipt/manage/applyReceipt?orderId=" + orderId + "&orderNum=" + orderNum + "&orderType=" + orderType);
			else if ("canOrDel" == msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开收据！', 'success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！', 'success');
		}
	});

}
function getExitGroupReviewList() {
	window.open(contextPath + "/orderReview/manage/getExitGroupReviewList?reviewStatus=0&userLevel=1");
}
/**
 * 订单页面查看订单收据信息列表
 * param orderNum   订单号
 * author     chenry
 * createDate 2015-07-29
 */
function viewReceiptInfo(orderId, orderNum, orderType, activityId) {
	window.open(contextPath + "/orderInvoice/manage/supplyreceiptlist?orderId=" + orderId + "&orderNum=" + orderNum + "&orderType=" + orderType + "&activityId=" + activityId);

}

/**
 * 预报名订单对象
 * @author xiaoyang.tao
 * @date  2014-11-14
 */
var applyOrderInfo = {
	//
	_orderType : 2,
	/**
	 * 点击恢复取消预报名订单时触发
	 * @param orderId 订单ID
	 * @param obj 当前按钮对象
	 * @param orderType 预报名订单类型 已报名：0 转正：1 已取消：2
	 */
	changeOrderType : function (orderId, obj, orderType) {
		if (orderType == this._orderType) {
			$.jBox.confirm("取消预报名订单，您确定吗？", "提示", function (v, h, f) {
				if (v == "ok") {
					applyOrderInfo.ajax.changeOrderType(orderId, obj, orderType);
				}
			});
		} else {
			$.jBox.confirm("恢复预报名订单，您确定吗？", "提示", function (v, h, f) {
				if (v == "ok") {
					applyOrderInfo.ajax.changeOrderType(orderId, obj, orderType);
				}
			});
		}
	},
	changeToOrder : function (groupId, orderId, freePosition, orderPersonNum) {
		if (orderPersonNum > freePosition) {
			top.$.jBox.tip('预订数大于剩余数，不能预订', 'error');
		} else {
			window.location.href = contextPath + "/orderCommon/manage/applyshowforModify?preproductorderById=" + orderId;
		}
	},
	ajax : {
		changeOrderType : function (orderId, obj, orderType) {
			var url_param = "orderId=" + orderId + "&orderType=" + orderType;
			var btnObj = $(obj);
			$.ajax({
				type : "POST",
				url : contextPath + "/applyOrderCommon/manage/changeOrderType?" + url_param,
				success : function (msg) {
					if (msg.sucess) {
						var btnObj = $(obj);
						//获取按钮的父对象td
						var btnObjPar = btnObj.parent();
						if (orderType == applyOrderInfo._orderType) {
							$("input[name=positive]", btnObjPar).hide();
							$("input[name=recover]", btnObjPar).show();
							btnObjPar.prev().text("已取消");
						} else {
							$("input[name=positive]", btnObjPar).show();
							$("input[name=cancle]", btnObjPar).show();
							btnObjPar.prev().text("已报名");
						}
						btnObj.hide();
						jBox.tip("操作成功", 'info');
					} else {
						jBox.tip(msg.error, 'error');
					}
				}
			});
		},
		getFreePosition : function (groupId) {
			$.ajax({
				type : "POST",
				url : contextPath + "/applyOrderCommon/manage/getFreePosition?groupId=" + groupId,
				success : function (msg) {}
			});
		}
	}
};
var rebatesInfo = {
	rebatesOrder : function (orderId, orderType) {
		window.open(contextPath + "/order/rebates/showRebatesList?orderId=" + orderId + "&orderType=" + orderType);
	}
};

function lockOrder(orderId) {
	doOrderLockStatus(orderId, "lockOrder", "锁定成功");
}

function unLockOrder(orderId) {
	doOrderLockStatus(orderId, "unLockOrder", "解锁成功");
}

function doOrderLockStatus(orderId, actionName, tipMsg) {
	$.ajax({
		type : "POST",
		url : contextPath + "/orderCommon/manage/" + actionName,
		data : {
			"orderId" : orderId
		},
		success : function (msg) {
			if (msg) {
				if (msg.success) {
					top.$.jBox.tip(tipMsg, 'warning');
					$("#btn_search").click();
				} else {
					top.$.jBox.tip(msg.error, 'warning');
				}
			}

		}
	});

}

function openBlankWindow(url) {
	var $newWindow = $('<a href="' + url + '" target="_blank">a</>');
	$('body').append($newWindow);
	$newWindow[0].click();
	$newWindow.remove();
}
/**
 * 已占位订单的 确认操作
 * @param orderId 订单id
 */
function seizedConfirm(orderId){
	$.jBox.confirm("是否确认该订单占位?", "系统提示", function (v, h, f) {
		if (v == 'ok') {
			// 到后台修改订单 相关状态seizedConfirmationStatus的值为1
			$.ajax({
				type : "POST",
				url : contextPath + "/orderCommon/manage/seizedConfirm",
				data : {
					"orderId" : orderId
				},
				success : function (returnBack) {
					if (returnBack) {
						if (returnBack.flag == "success") {
							top.$.jBox.tip("确认成功！", 'info');
							// 确认成功，重新发起搜索刷新页面
							$("#btn_search").click();
						} else {
							top.$.jBox.tip("确认失败！" + returnBack.message, 'warning');
							// TODO
						}
					}
				}
			});
		} else if (v == 'cancel') {}
	});
}

/**
 * 200 渠道联系人显示20个字符，其余的用"..."代替
 */
function sliceAgentContacts(){
	$("input[class=agentContactsName]").each(function(index) {
		var agentContactsName = $(this).val();
		$(this).siblings().attr("title", agentContactsName);
		if(agentContactsName!=null && agentContactsName!=undefined && agentContactsName.length>=20){
			agentContactsName = agentContactsName.substring(0,20) + "...";
		}
		$(this).siblings().html(agentContactsName);
	});
}

/*  批量下载单团类确认单
 * @param checkName checkbox标签的name属性值，用于获取checkbox标签
 * add by xianglei.dong  2016-03-22
 * updated by xianglei.dong 2016-03-30
 */
function product_orderBatchDownload(checkName){
	var check = document.getElementsByName(checkName);
	var objarray = check.length;
	var orderIds = '';
	var docIds = '';
	//记录选中的个数
	var checkedCount = 0;
	for (var i=0; i<objarray; i++) {
	  if(check[i].checked == true) {
		  checkedCount++;
		  var order_doc_id = check[i].value.split('@');
		  if(order_doc_id[1]=='' || order_doc_id[1]==null || order_doc_id[1]==undefined) {
			  continue;
		  }
		  orderIds = orderIds + ',' + order_doc_id[0];
		  docIds = docIds + ',' + order_doc_id[1];
	  }
	}
	
	if(checkedCount ==0){
		top.$.jBox.tip('请选择订单');
        return false;
	}else if(docIds == '') {
		top.$.jBox.tip('选择的订单中没有可下载的确认单，请先上传确认单');
		return false;
	}else{
		docIds = docIds.substr(1, docIds.length);
		window.open(encodeURI(encodeURI(contextPath+"/orderCommon/manage/zipconfirmdownload/" + docIds )));
		location.reload(true);
	}		
}

/* 订单列表全选
 * @param obj 全选checkbox标签的对象
 * @param groupid  团号id，在团号列表中用于表示同一团号下的checkbox标签
 * add by xianglei.dong
 * 2016-03-22
 */
function product_orderAllChecked(obj, groupid) {
	if(groupid==null || groupid == undefined || groupid == '') {    //订单列表
		if (obj.checked) {
			$("input[name='productOrderId']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='product_orderAllChk']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='productOrderId']:checked").each(function() {
				this.checked = false;
			});
			$("input[name='product_orderAllChk']:checked").each(function() {
				this.checked = false;
			});
		}
	}else {    //团号列表
		if (obj.checked) {
			$("input[name='productOrderId_"+groupid+"']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='product_orderAllChk_"+groupid+"']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='productOrderId_"+groupid+"']").each(function() {
				this.checked = false;
			});
			$("input[name='product_orderAllChk_"+groupid+"']:checked").each(function() {
				this.checked = false;
			});
		}
	}
	
}

/* 订单列表反选
 * @param obj 全选checkbox标签的对象
 * @param groupid  团号id，在团号列表中用于表示同一团号下的checkbox标签
 * add by xianglei.dong
 * 2016-03-22
 */
function product_orderAllNoCheck(groupid) {
	if(groupid==null || groupid == undefined || groupid == '') {   //订单列表
		$("input[name='productOrderId']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		}); 
	}else {  //团号列表
		$("input[name='productOrderId_"+groupid+"']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
	}
	pr_allchk(groupid);
}

function pr_allchk(groupid) {
	var chk = 0;
	if(groupid==null || groupid == undefined || groupid == '') {       //订单列表
		var chknum = $("input[name='productOrderId']").size();		
		$("input[name='productOrderId']").each(function() {
			if ($(this).attr("checked") == 'checked') {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='product_orderAllChk']").attr("checked", true);
		} else {//不全选 
			$("input[name='product_orderAllChk']").attr("checked", false);
		}
	}else {   //团号列表
		var chknum = $("input[name='productOrderId_"+groupid+"']").size();		
		$("input[name='productOrderId_"+groupid+"']").each(function() {
			if ($(this).attr("checked") == 'checked') {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='product_orderAllChk_"+groupid+"']").attr("checked", true);
		} else {//不全选 
			$("input[name='product_orderAllChk_"+groupid+"']").attr("checked", false);
		}
	}	
}

/* 每行中的复选框
 * @param obj checkbox对象
 * @param groupid 组号
 * add by xianglei.dong
 * 2016-03-23
 */
function productOrderCheckchg(groupid) {
	if(groupid==null || groupid == undefined || groupid == '') {    //订单列表
		if ($("input[name='productOrderId']").not("input:checked").length) {
			$("input[name='product_orderAllChk']").attr("checked",false);
		}else{

			$("input[name='product_orderAllChk']").attr("checked",true);
		}
	}else {  //团号列表 
		if ($("input[name='productOrderId_"+groupid+"']").not("input:checked").length) {
			$("input[name='product_orderAllChk_"+groupid+"']").attr("checked",false);
		}else{

			$("input[name='product_orderAllChk_"+groupid+"']").attr("checked",true);
		}
	}	
}


function downloadConfirm(orderId){
	if(orderId != ""){
		var ctx = $("#ctx").val();
		$.jBox("iframe:"+ ctx +"//orderCommon/manage/downloadConfirmFiles?orderId="+orderId, {
			title: "下载确认单",
			width: 340,
			height: 'auto',
			buttons: {'关闭':true},
			persistent:true,
			loaded: function (h) {},
			submit: function (v, h, f) {},
			closed: function () {
				location.reload(true);
			}
		});
		$(".jbox-close").hide();
	}
}


var $node;
function relationInvoiceList(orderId) {
	var $pop = $.jBox($("#relationInvoice").html(), {
    	title    : "发票列表", buttons: {'提交':1 ,'关闭':0}, submit: function (v, h, f) {
        	if (v == "0") {
        	}else if(v == "1"){
        		var checks = $("input[name='invoiceName']:checked");
        		var invoiceIds = "";
        		checks.each(function(i,element){
        			invoiceIds += $(element).val();
        			invoiceIds += ",";
        		})
        		invoiceIds = invoiceIds.slice(0,-1);
        		
        		if(invoiceIds == ""){
        			$.jBox.tip("请先选择要关联的发票!");
        			return false;
        		}
        			
        		$.ajax({
        			type: "POST",
	        		url: sysCtx + "/orderPay/changeInvoiceReceivedPayStatus",
	        		data: {
	        			invoiceIds : invoiceIds
	        		},
        			success: function(result){
        				if(result.flag == 'success'){
        					$.jBox.tip("发票关联成功！"); 
        				}
        			}
        		});
        	}
		},loaded:function(h, f){
			$node = h.find("#relationInvoiceTable");
			searchRelationInvoice(orderId);
		}, height: 350, width: 700}
	);
}

// ajax进入后台查询，返回json再解析
function searchRelationInvoice(orderId){
	$.ajax({
		type: "POST",
        url: sysCtx + "/orderPay/relationInvoiceList",
        data: {
        	orderId : orderId
        },
		success: function(result){
			var html = "";
			if(result == undefined || result == null){
				top.$.jBox.tip("获取关联发票记录失败！");
			}
			if(result == ''){
				$node.empty().append(html);
			}
			var json = eval(result);
			// json数组个数
			var jsonLength = json.length;
			// 判断为空
			if (jsonLength && jsonLength != 0) {
				// 循环获取html组合
				for (var i = 0; i < jsonLength; i++) {
					// 序列值
					var indexVal = i + 1;
					html += "<tr>";
					// 选择
					html += "<td><input name='invoiceName' type='checkbox' value='" + json[i][0] + "'></td>";
					// 发票号
					html += "<td name='operatorName' class='tc'><span>" + json[i][1] + "</span></td>";
					// 团号
					html += "<td name='operation' class='tc'><span>" + json[i][2] + "</span></td>";
					// 申请人
					html += "<td name='operationTime' class='tc'><span>" + json[i][5] + "</span></td>";
					// 开票状态
					html += "<td name='mainContext' class='tc'><span>" + json[i][3] + "</span></td>";
					// 开票金额
					html += "<td name='mainContext' class='tr'><span>¥ " + json[i][4] + "</span></td>";
					html += "</tr>";
				}
				$node.empty().append(html);
			}
		}
	});
}
