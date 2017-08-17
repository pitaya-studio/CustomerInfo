var contextPath;
$(function () {
	
	/*$("#fromArea").comboboxInquiry();
	$("#companyId").comboboxInquiry();*/
	contextPath = $("#ctx").val();

	$(document).scrollLeft(0);
	$("#targetAreaId").val($("#tempTargetAreaIds").val());
	$("#targetAreaName").val($("#tempTargetAreaNamess").val());

	// 文本框中提示信息
	inputTips();
	
	// 团期或订单显示样式选择
	var orderOrGroup = $("#orderOrGroup").val();
	if (orderOrGroup == "order") {
		$(".nav-tabs li:eq(1)").addClass("active");
	} else {
		$(".nav-tabs li:eq(0)").addClass("active");
	}
	
	var orderBy = $("#orderBy").val();
	var orderByArr = orderBy.split(" ");
	var orderByType = orderByArr[0];
	var orderByStatus = orderByArr[1];
	if (orderByType == "orderTime") {
		if (orderByStatus == "ASC") {
			$("#orderTime i em:eq(0)").removeClass("rank_up").addClass("rank_up_checked");
		} else {
			$("#orderTime i em:eq(1)").removeClass("rank_down").addClass("rank_down_checked");
		}
	} else {
		if (orderByStatus == "ASC") {
			$("#orderDate i em:eq(0)").removeClass("rank_up").addClass("rank_up_checked");
		} else {
			$("#orderDate i em:eq(1)").removeClass("rank_down").addClass("rank_down_checked");
		}
	}
	
	var inlineStatus = $("#inlineStatus").val();
	if (inlineStatus == "rigth") {
		$("[name=setTab01]").attr("style", "margin:0 0 0 -370px");
		$(".nav_p_right").addClass("nav_p_right_none");
		$(".nav_p_left").removeClass("nav_p_left_none");
	}
	$(".group_nav").show();
	
	setSelectDefaultVal();
	
	launchSearch();
});

function setSelectDefaultVal() {
	var supplierId = $("#supplierId").val();
	if (supplierId != "") {
		var officeArr = supplierId.split(",");
		for (var i = 0; i < officeArr.length; i++) {
			var officeId = officeArr[i];
			$(".item_text[id=" + officeId + "]").parent().find(".item_icon").trigger("click").addClass("selected_box");
			$("#supStorm .butn_sure").trigger("click");
		}
	}
	
	var orderType = $("#orderType").val();
	if (orderType != "") {
		var orderTypeArr = orderType.split(",");
		for (var i = 0; i < orderTypeArr.length; i++) {
			var type = orderTypeArr[i];
			$(".item_text[lang=orderType][value=" + type + "]").parent().find(".item_icon").trigger("click").addClass("selected_box");
			$("#orderState .butn_sure").trigger("click");
		}
	}
}

function launchSearch() {
	var groupOpenDateBegin = $("[name=groupOpenDateBegin]").val();
	var groupOpenDateEnd = $("[name=groupOpenDateEnd]").val();
	var orderTimeBegin = $("[name=orderTimeBegin]").val();
	var orderTimeEnd = $("[name=orderTimeEnd]").val();
	var moneyStrMin = $("[name=moneyStrMin]").val();
	var moneyStrMax = $("[name=moneyStrMax]").val();
	if (groupOpenDateBegin != "" || groupOpenDateEnd != "" || orderTimeBegin != "" || orderTimeEnd != "" 
		|| moneyStrMin != "" || moneyStrMax != "" || $(".selected_box").length > 0) {
		/*high_ranking();*/
		if($(".groupSearch").is(":hidden")){
			$(".groupSearch").show();
			$(".group_nav_child_em").css("background-position","-98px -102px");
		}else{
			$(".groupSearch").show();
			$(".group_nav_child_em").css("background-position","-100px -129px");
		}
	}
}

$(function(){
	$(".groupOrderChildrenTwo").hover(function(){
		$(this).find(".look").css("display","inline-block");
	},function(){
		$(this).find(".look").css("display","none")
	});
	$(".groupHomeOrderChildren").hover(function(){
		$(this).find(".homeLook").css("display","inline-block");
	},function(){
		$(this).find(".homeLook").css("display","none")
	});
	//详情弹窗展开项
	$(".ArrowRight").hover(function(){
		$(".POPUP").show();
	},function(){
		$(".POPUP").hide();
	})
});

$(window).load(function() {
	//获取订单返佣提醒
	getOrderRebates();
});

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
						var html = '预计返佣:' + prebt + '</br>实际返佣:' + infbt;
						$("input[name=orderNumForRebate][value=" + orderNo + "]").after(html);
						$("input[name=orderNumForRebate][value=" + orderNo + "]").parent().after("<em class='return'></em>");
						$(".return").hover(function(){
							$(this).prev().css("display","inline-block")
						},function(){
							$(this).prev().css("display","none")
						});
					}
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

function formSubmit() {
	$("#orderNumOrProductNameOrGroupCode").val($("#threeSerch").val());
	var supplierId ="";
	$checkedOffice = $("#searchByOffice").parent().find(".selected_box");
	$checkedOffice.each(function(index, item) {
		if (index == $checkedOffice.length -1) {
			supplierId += $(item).parent().find(".item_text").attr("id");
		} else {
			supplierId += $(item).parent().find(".item_text").attr("id") + ",";
		}
    });
	$("#supplierId").val(supplierId);
	var payStatusOther ="";
	$checkedOrderType = $("#orderState").parent().find(".selected_box");
	$checkedOrderType.each(function(index, item) {
		if (index == $checkedOrderType.length -1) {
			payStatusOther += $(item).parent().find(".item_text").attr("value");
		} else {
			payStatusOther += $(item).parent().find(".item_text").attr("value") + ",";
		}
	});
	$("#orderType").val(payStatusOther);
	$("#searchForm").submit();
}

//按下单时间排序
function byOrderTimeBegin() {
	var orderBy = $("#orderBy").val();
	if(orderBy == "orderTime ASC"){
		$("#orderBy").val("orderTime DESC");
	}else{
		$("#orderBy").val("orderTime ASC");
	}
	
	$("#searchForm").submit();
}
//开团日期排序
function byGroupOpenDate() {
	var orderBy = $("#orderBy").val();
	if(orderBy == "groupOpenDate ASC"){
		$("#orderBy").val("groupOpenDate DESC");
	}else{
		$("#orderBy").val("groupOpenDate ASC");
	}
	$("#searchForm").submit();
}

//根据支付状态筛选
function searchByPayStatus(payStatus){
	$("#payStatus").val(payStatus);
	$("#searchForm").submit();
}



/**
 * 订单详情
 *
 * param orderId
 */
function quauqOrderDetail(orderId) {
	contextPath = $("#ctx").val();
	
	window.open(contextPath + "/QuauqOrderCommon/manage/orderDetail/" + orderId, "_blank");
}

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
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
	$('#fromArea').val('');
	$('#companyId').val('');
}


/**
 * 订单或团期列表转换
 *
 */
function changeTab(obj,type) {
	$("#orderBy").val("");
	if (type == "order") {
		$("#orderOrGroup").val("order");
	} else if (type == "group") {
		$("#orderOrGroup").val("group");
	}
	$("#searchForm").submit();
}

function downloads(docIds) {
	if(null==docIds || ''==docIds || 'undefined'==docIds){ //判断文档id是否为空
        top.$.jBox.tip("没有行程单可供下载!");
        return false;
    }
	contextPath = $("#ctx").val();
	window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/zipdownload/" + docIds + "/行程单")));
}


/**
 * 导出订单中关于游客信息 downloadData
 * param orderId 订单ID或下载文件ID
 * param downloadType 下载类型：游客资料、出团通知、确认单、面签通知
 */
function downloadData(orderId, downloadType) {
	//debugger;
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

function downloadConfirm(orderId){
	if(orderId != ""){
		$.jBox("iframe:"+ contextPath +"//t1/orderList/manage/downloadConfirmFiles?orderId="+orderId, {
			title: "下载确认单",
			width: 340,
			height: 'auto',
			buttons: {'关闭':true},
			persistent:true,
			loaded: function (h) {},
			submit: function (v, h, f) {},
			closed: function () {
			}
		});
		$(".jbox-close").hide();
	}else{
		
	}
}


