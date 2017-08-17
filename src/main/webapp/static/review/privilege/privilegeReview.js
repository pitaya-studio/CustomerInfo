var contextPath;
$(function () {
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	contextPath = $("#ctx").val();
	// 文本框中提示信息
  	inputTips();

	var _$orderBy = $("#orderBy").val();
	if (_$orderBy == "") {
		_$orderBy = "r.update_date DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function () {
		if ($(this).hasClass("li" + orderBy[0])) {
			orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "DESC" ? "down" : "up";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
			$(this).attr("class", "activitylist_paixu_moren");
		}
	});

	$("#agentId").comboboxInquiry();
	$("#operator").comboboxInquiry();
	$("#saler").comboboxInquiry();
	$("#picker").comboboxInquiry();

	//首次打印提醒
	$(".uiPrint").hover(function () {
		$(this).find("span").show();
	}, function () {
		$(this).find("span").hide();
	});

	//如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='wholeOrderNum']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for (var i = 0; i < searchFormInput.length; i++) {
		var inputVal = $(searchFormInput[i]).val();
		if (inputVal != "" && inputVal != null && inputVal != "不限" && inputVal != "全部") {
			inputRequest = true;
		}
	}
	for (var i = 0; i < searchFormselect.length; i++) {
		var selectVal = $(searchFormselect[i]).children("option:selected").val();
		if (selectVal != "" && selectVal != null) {
			if (selectVal == "0" && $(searchFormselect[i]).attr("name") == "productType") {
				
			} else {
				selectRequest = true;
			}
		}
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}

	$(".bgMainRight").delegate("select[class='selectType']", "change", function () {
		$("#searchForm").submit();
	})
	var _$orderBy = $("#orderBy").val();
	if (_$orderBy == "") {
		_$orderBy = "id DESC";
	}
	var orderBy = _$orderBy.split(" ");

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
	$("#searchForm").submit();
}
function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
}
function statusChooses(rid) {
	$("#tabStatus").val(rid);
	$("#searchForm").submit();
}

function refundInputs(obj) {
	var ms = obj.value.replace(/[^\d\.]/g, "").replace(/(\.\d{2}).+$/, "$1").replace(/^0+([1-9])/, "$1").replace(/^0+$/, "0");
	var txt = ms.split(".");
	obj.value = txt[0] + (txt.length > 1 ? "." + txt[1] : "");

}
function statusChoose(statusNum) {
	$("#reviewStatus").val(statusNum);
	$("#searchForm").submit();
};

function backReview(rid) {

	$.jBox.confirm("确定要撤销审批吗？", "提示", function (v, h, f) {
		console.log("${ctx}/privilegeReview/backReview/" + rid);
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				async : false,
				url : $("#ctx").val() + "/privilegeReview/backReview/" + rid + "?dom=" + Math.random(),
				data : {},
				success : function (data) {
					if (data.result == "success") {
						jBox.tip("撤销成功！");
						$("#searchForm").submit();
					} else {
						jBox.tip(data.msg);
					}
				}
			});
		}
	});

}

String.prototype.replaceColonChars = function(regEx){
	if (!regEx) {
		regEx = /[\'\？]/g;
	}
	return this.replace(regEx,'');
}	