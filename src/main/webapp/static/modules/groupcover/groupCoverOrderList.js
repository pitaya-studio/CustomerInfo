var sysCtx = $("#sysCtx").val();
$(function () {

	$("#createBy").comboboxInquiry();
	$("#activityCreate").comboboxInquiry();

	$(".qtip").tooltip({
		track : true
	});

	$(document).scrollLeft(0);

	$('.handle').hover(function () {
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
			//$(this).text('收起筛选');//bug17536
			$(this).addClass('zksx-on');
		} else {
			$('.ydxbd').hide();
			//$(this).text('展开筛选');//bug17536
			$(this).removeClass('zksx-on');
		}
	});
	//如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='coverCodeOrGroupCode']");
	var searchFormselect = $("#searchForm").find("select");
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
		var selectVal = $(searchFormselect[i]).children("option:selected").val();
		if (selectVal != "" && selectVal != "0" && selectVal != null) {
			selectRequest = true;
		}
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}

	var _$orderBy = $("#orderBy").val();
	if (!_$orderBy || _$orderBy == "") {
		_$orderBy = "gc.createDate DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function () {
		if ($(this).hasClass("li" + orderBy[0])) {
			orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
			$(this).attr("class", "activitylist_paixu_moren");
		}
	});

});

String.prototype.replaceColonChars = function (regEx) {
	if (!regEx) {
		regEx = /[\'\？]/g;
	}
	return this.replace(regEx, '');
}

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

function coversortby(sortBy, obj) {
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
	$("#searchForm").submit();
}

/**
 * 订单查询
 * @param orderStatus 订单状态
 * @param orderType 订单类型
 * @param orderOrGroup 订单或者团期
 */
function getOrderList(orderStatus, orderType, orderOrGroup) {
	window.location.href = sysCtx + "/orderList/manage/showOrderList/" + orderStatus + "/" + orderType + ".htm";
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
	$('#createBy').val('');
	$('#activityCreate').val('');
	$('#proCreateBy').val('');
	$('#modifyAgentInfo').val('').next("span:eq(0)").children("input").attr("title", "全部");
	$('#orderShowType').val('${showType}');
}

/**
 * 确认补位申请
 */
function jbox_buweiconfirm(coverId) {
	$.jBox.confirm("是否确认该补位？", "提示", function (v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				url : sysCtx + "/groupCover/confirmCover",
				data : {
					coverId : coverId
				},
				error : function (request) {
					top.$.jBox.tip("确认失败", "warning");
				},
				success : function (data) {
					if (data.result == "success") {
						top.$.jBox.tip("确认成功", "success");
						$("#searchForm").submit();
					} else {
						top.$.jBox.tip("确认失败", "warning");
					}
				}
			});
		}
	});
}

/**
 * 取消补位申请
 */
function jbox_nobuweiconfirm(coverId) {
	$.jBox.confirm("确定取消该补位？", "提示", function (v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				url : sysCtx + "/groupCover/cancelCover",
				data : {
					coverId : coverId
				},
				error : function (request) {
					top.$.jBox.tip("取消失败", "warning");
				},
				success : function (data) {
					if (data.result == "success") {
						top.$.jBox.tip("取消成功", "success");
						$("#searchForm").submit();
					} else {
						top.$.jBox.tip("取消失败", "warning");
					}
				}
			});
		}
	});
}

/**
 * 驳回补位申请
 */
function jbox_bohuibuweiconfirm(coverId) {
	$.jBox.confirm("是否驳回该补位？", "提示", function (v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				url : sysCtx + "/groupCover/rejectCover",
				data : {
					coverId : coverId
				},
				error : function (request) {
					top.$.jBox.tip("驳回失败", "warning");
				},
				success : function (data) {
					if (data.result == "success") {
						top.$.jBox.tip("驳回成功", "success");
						$("#searchForm").submit();
					} else {
						top.$.jBox.tip("驳回失败", "warning");
					}
				}
			});
		}
	});
}
