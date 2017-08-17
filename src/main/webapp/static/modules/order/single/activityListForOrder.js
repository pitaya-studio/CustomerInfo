var sysCtx = "";
var agentSourceType = "1";
var yuejianxingzong = '7a81b21a77a811e5bc1e000c29cf2586';
$(document).ready(function () {
	var groupCoverOrderListFlag = $("#groupCoverOrderListFlag").val();
	if (groupCoverOrderListFlag == "1") {
		return false;
	}
	sysCtx = $("#sysCtx").val();
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
	
	if ($("#orderBy").length > 0) {
		var _$orderBy = $("#orderBy").val();
		if (_$orderBy == "") {
			_$orderBy = "groupOpenDate";
		}
		var orderBy = _$orderBy.split(" ");
		$(".activitylist_paixu_left li").each(function () {
			if ($(this).hasClass("li" + orderBy[0])) {
				orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
				$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
				$(this).attr("class", "activitylist_paixu_moren");
			}
		});
	}

	if ($("#tempUserName").val() == "lmelsguest") {
		$(".lmels-ts").show();
		$("#agentIdSel").attr("disabled", true);

	}

	$.fn.datepicker = function (option) {
		var opt = {}

		 || option;
		this.click(function () {
			WdatePicker(option);
		});
	};
	//滚动条
	$('.team_top').find('.table_activity_scroll').each(function (index, element) {
		var _gg = $(this).find('tr').length;
		if (_gg >= 20) {
			$(this).prev().wrap("<div class='group_h_scroll_top'></div>");
			$(this).addClass("group_h_scroll");
		}
	});
	//产品名称获得焦点显示隐藏
	$("#wholeSalerKey").focusin(function () {
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function () {
		var obj_this = $(this);
		if (obj_this.val() != "") {
			obj_this.next("span").hide();
		} else
			obj_this.next("span").show();
	});
	if ($("#wholeSalerKey").val() != "") {
		$("#wholeSalerKey").next("span").hide();
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
	//展开筛选按钮
	$('.zksx').click(function () {
		if ($('.ydxbd').is(":hidden") == true) {
			$('.ydxbd').show();
			//$(this).text('收起筛选');
			$(this).addClass('zksx-on');
		} else {
			$('.ydxbd').hide();
			//$(this).text('展开筛选');
			$(this).removeClass('zksx-on');
		}
	});
	
	//团期备注展开与否
    $(document).on('click', '.expandNotes', function () {
        var $this = $(this);
        if ($this.parents('tr:first').nextAll(".groupNoteCol:first").css('display') == 'none') {
            $this.text('收起备注');
            $this.parents('tr:first').nextAll(".groupNoteCol:first").show();
        } else {
            $this.text('展开备注');
            $this.parents('tr:first').nextAll(".groupNoteCol:first").hide();
        }
    });
	
	//设置备注是否显示
	remarkVisible();
	
	//如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='button'][type!=submit][id!='wholeSalerKey']");
	var searchFormselect = $("#searchForm").find("select[name!='currencyType']");
	var inputRequest = false;
	var selectRequest = false;
	for (var i = 0; i < searchFormInput.length; i++) {
		if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for (var i = 0; i < searchFormselect.length; i++) {
		if ($(searchFormselect[i]).children("option:selected").val() != "" && $(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if ($("#activityCreate").val() == "-99999" || $("#activityCreateCalendar").val() == "-99999") {
		selectRequest = false;
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}

	//预定按钮处理
	bookOrder();
	
	//订单列表和团期列表初始化：隐藏订单那或团期，判断显示那个标签
	var productOrGroup = $("#productOrGroup").val();
	if (productOrGroup == "product") {
		$("#productLabel").addClass("select");
	} else if (productOrGroup == "group") {
		$("#groupLabel").addClass("select");
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
	
	$('.handle').hover(function () {
		if (0 != $(this).find('a').length) {
			$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
		}
	}, function () {
		$(this).removeClass('handle-on');
		$(this).find('dd').removeClass('block');
	});
	
	$(".p0 input").each(function(index, obj) {
		$(this).hide();
		var v = $(this).val();
		$(this).parent().find(".handle").find("a:eq(0)").text(v);
	});
	
	
	// 产品系列
	$("[name=productTypeHref]").each(function(index, obj) {
		var typeVal = $(obj).text();
		$("#activityLevelId option").each(function(i, o) {
			if (typeVal == $(o).val()) {
				$(obj).text($(o).text());
			}
		});
	});
	if ($("#activityLevelId").val() == "") {
		$("[name=productTypeHref]:eq(0)").addClass("select");
	} else {
		$("[name=productTypeHref]").each(function(index, obj) {
			var typeVal = $(obj).text();
			var optionText = $("#activityLevelId option:selected").text()
			if (typeVal == optionText) {
				$(obj).addClass("select");
			}
		});
	}

	//299v2  将列表中的"酒店房型"逗号隔开，分开显示//518 格式修改
	$(".hotelAndHouse").each(function(){
		var groupHotel = $(this).find("input[name=groupHotel]").val().split(",");
		var groupHouseType = $(this).find("input[name=groupHouseType]").val().split(",");
		var html = "";
		if(groupHotel == "" || groupHouseType == ""){
			html += "<p><label>酒店：</label></p>";
			//html += "&nbsp&nbsp";
			html += "<p><label>房型：</label></p>"
		}else {
			for (var i = 0; i < groupHotel.length; i++) {
				html += "<p>";
				html += "<label>酒店:</label>"
				html += "<span class='word-break-all'>" + groupHotel[i] + "</span>";
				html += "</p>";
				html += "<p>";
				//html += "&nbsp&nbsp";
				html += "<label>房型:</label>";
				html += "<span class='word-break-all'>" + groupHouseType[i] + "</span>";
				html += "</p>";
			}
		}
		$(this).append(html);
	});
	
	// 设置合并单元格
	$("[name=colspanCount]").each(function(index, obj) {
		// 获取tr对象
		var $tr = $(this).closest("[name=colspanTable]").find("[name=colspanTr]:eq(0)");
		// 查看有多少th，即要合并数
		var colspanCount = $tr.find("th").length;
		// 查看是否有列合并情况
		var rowspanCount = $tr.find("th[rowspan=2]").length;
		// 查询th
		var $ths = $tr.find("th");
		// 如果有列合并情况，则要加上多出来的列
		if (rowspanCount != 0) {
			$ths.each(function(i, o) {
				// 如果是隐藏的列，则减去一行
				if ($(this).css("display") != "none") {
					if ($(this).attr("colspan") && $(this).attr("colspan") > 1) {
						colspanCount += ($(this).attr("colspan") - 1);
					}
				} else {
					colspanCount = colspanCount - 1;
				}
			});
		}else{
			$ths.each(function(i, o) {
				// 如果是隐藏的列，则减去一行
				if ($(this).css("display") == "none") {
					colspanCount = colspanCount - 1;
				}
			});
		}
		$(this).attr("colspan", colspanCount);
	});
});

function productType(productType) {
	$("#activityLevelId").val(productType);
	$("#searchForm").submit();
}

function remarkVisible() {
	$('.groupNoteContent').each(function () {
		if ($(this).html() == '') {
			$(this).parent().parent().prev().find('.groupNoteTipImg').hide();
		} else {
			$(this).parent().parent().prev().find('.groupNoteTipImg').show()
		}
	})
}

function bookOrder() {
	$("input[name='bookOrder']").each(function (index, obj) {
		var showType = $("#showType").val();
		var isAllowSupplement = $("#isAllowSupplement").val();
		var bookVal = $(this).val();
		if (bookVal && bookVal != '') {
			var bookArr = bookVal.split(",");
			if (bookArr.length == 15) {
				var freePosition = bookArr[0]; //团期余位
				var payReservePosition = bookArr[1]; //各渠道总的切位人数
				var soldPayPosition = bookArr[2]; //已售出切位人数
				var leftdays = bookArr[3]; //剩余天数：小于0表示还有剩余天数，大于或等于0则表示没有剩余天数
				var settlementAdultPrice = bookArr[4]; //成人同行价
				var settlementcChildPrice = bookArr[5]; //儿童同行价
				var settlementSpecialPrice = bookArr[6]; //特殊人群同行价
				var suggestAdultPrice = bookArr[7]; //成人直客价
				var suggestChildPrice = bookArr[8]; //儿童直客价
				var suggestSpecialPrice = bookArr[9]; //特殊人群直客价
				var activityId = bookArr[10]; //产品id
				var groupId = bookArr[11]; //团期id
				var leftpayReservePosition = bookArr[12]; //剩余切位人数
				var groupcoverNum = bookArr[13];//补位记录
				var groupCoverFlag = bookArr[14];//补位标识
				var bookHtml = "";
				//预定
				if (showType == '1') {
					//是否有补位权限：0表示没有，1表示有
					if (groupCoverFlag == "0") {
						if ((freePosition > 0 || (payReservePosition > 0 && leftpayReservePosition > 0))
							 && (settlementAdultPrice > 0 || settlementcChildPrice > 0 || settlementSpecialPrice > 0 || suggestAdultPrice > 0 || suggestChildPrice > 0 || suggestSpecialPrice > 0)) {
							if (leftdays > 0) {
								bookHtml = '<input class="btn btn-primary" type="button" value="预 定" onClick="agentType(this)"/>';
							} else {
								if (isAllowSupplement == '1') {
									bookHtml = '<input class="btn btn-primary" type="button" value="补 单" onClick="agentType(this)"/>';
								} else {
									bookHtml = '<input class="btn gray" type="button" value="预 定"/>';
								}
							}
						} else {
							bookHtml = '<input class="btn gray" type="button" value="预 定"/>';
						}
					} else {
						if (leftdays > 0 && ((groupcoverNum && groupcoverNum > 0) || (freePosition && freePosition == 0))) {
							bookHtml = '<input class="btn btn-primary" type="button" value="补 位" onclick="coverFunction(\'' + sysCtx + '/groupCover/list/' + groupId + '\')"/>';
						} else {
							if ((freePosition > 0 || (payReservePosition > 0 && leftpayReservePosition > 0))
								 && (settlementAdultPrice > 0 || settlementcChildPrice > 0 || settlementSpecialPrice > 0 || suggestAdultPrice > 0 || suggestChildPrice > 0 || suggestSpecialPrice > 0)) {
								if (leftdays > 0) {
									bookHtml = '<input class="btn btn-primary" type="button" value="预 定" onClick="agentType(this)"/>';
								} else {
									if (isAllowSupplement == '1') {
										bookHtml = '<input class="btn btn-primary" type="button" value="补 单" onClick="agentType(this)"/>';
									} else {
										bookHtml = '<input class="btn gray" type="button" value="预 定"/>';
									}
								}
							} else {
								bookHtml = '<input class="btn gray" type="button" value="预 定"/>';
							}
						}
					}
				}
				//预报名
				else if (showType == '2') {
					if (freePosition > 0 && (settlementAdultPrice > 0 || settlementcChildPrice > 0 || settlementSpecialPrice > 0)) {
						bookHtml = '<input class="btn btn-primary" type="button" value="预报名" onClick="applyOrder(' + activityId + ',' + groupId + ')"/>';
					} else if (freePosition <= 0 || (settlementAdultPrice <= 0 && settlementcChildPrice <= 0)) {
						bookHtml = '<input class="btn gray" type="button" value="预报名"/>';
					}
				}
				$(this).after(bookHtml).remove();
			} else if (bookArr.length == 8) {
				var freePosition = bookArr[0]; //团期余位
				var leftdays = bookArr[1]; //剩余天数：小于0表示还有剩余天数，大于或等于0则表示没有剩余天数
				var settlementAdultPrice = bookArr[2]; //成人同行价
				var settlementcChildPrice = bookArr[3]; //儿童同行价
				var settlementSpecialPrice = bookArr[4]; //特殊人群同行价
				var suggestAdultPrice = bookArr[5]; //成人直客价
				var suggestChildPrice = bookArr[6]; //儿童直客价
				var suggestSpecialPrice = bookArr[7]; //特殊人群直客价
				var bookHtml = "";

				if (freePosition > 0 && (settlementAdultPrice > 0 || settlementcChildPrice > 0 || settlementSpecialPrice > 0 || suggestAdultPrice > 0 || suggestChildPrice > 0 || suggestSpecialPrice > 0)) {
					if (leftdays > 0) {
						bookHtml = '<input class="btn btn-primary" type="button" value="预 定" onClick="agentType(this)"/>';
					} else {
						if (isAllowSupplement == '1') {
							bookHtml = '<input class="btn btn-primary" type="button" value="补 单" onClick="agentType(this)"/>';
						} else {
							bookHtml = '<input class="btn gray" type="button" value="预 定"/>';
						}
					}
				} else {
					bookHtml = '<input class="btn gray" type="button" value="预 定"/>';
				}
				// 新增，已收明细
				//bookHtml += '<input class="btn btn-primary" value="已收明细"  onclick="javascript:showOrderPay(1234,this);" type="button">';
				$(this).after(bookHtml).remove();
			}
		}
	});
}
/**
 * 点击显示"销售明细"详情列表
 * @param 团期ID
 */
function showOrderPay(group_id, obj) {
	// 获取团期ID
	var groupID = $(obj).parents("tr").find("input[name=groupID]").val();
	if (!groupID) {
		groupID = group_id;
	}
	if (groupID) {
		// 根据团期ID，获取订单列表详情
		$.ajax({
			type : "POST",
			url : "../../activityInfo",
			data : {
				activityGroupID : groupID
			},
			dataType : 'json',
			success : function (msg) {
				if (msg.res == "success") {
					// 将团期详情写入下拉列表中
					var json = msg.activityInfoList;
					var sbrtr = $(obj).parents("tr").nextAll("tr[name=subtr]:first");
					var body = sbrtr.find("tbody");
					body.empty();
					var indexs = 0;
					for (var o in json) {
						if (indexs < json.length) {
							indexs++;
							body.append("<tr>");
							body.append('<td class="tc">' + indexs + '</td>');
							if (sbrtr.find("th[name=orderNo]").length > 0) {
								body.append('<td class="tc">' + json[o].orderNo + '</td>');
							}
							if (sbrtr.find("th[name=agentName]").length > 0) {
								body.append('<td class="tc">' + json[o].agentName + '</td>');
							}
							if (sbrtr.find("th[name=shell]").length > 0) {
								body.append('<td class="tc">' + json[o].shell + '</td>');
							}
							if (sbrtr.find("th[name=orderUser]").length > 0) {
								body.append('<td class="tc">' + json[o].orderUser + '</td>');
							}
							if (sbrtr.find("th[name=reserveDate]").length > 0) {
								body.append('<td class="tc">' + json[o].reserveDate + '</td>');
							}
							if (sbrtr.find("th[name=personNum]").length > 0) {
								body.append('<td class="tc">' + json[o].personNum + '</td>');
							}
							if (sbrtr.find("th[name=orderStatus]").length > 0) {
								body.append('<td class="tc">' + json[o].orderStatus + '</td>');
							}
							if (sbrtr.find("th[name=totalAmount]").length > 0) {
								body.append('<td class="tc">' + json[o].totalAmount + '</td>');
							}
							if (sbrtr.find("th[name=payedAmount]").length > 0) {
								body.append('<td class="p0"><div class="out-date">' + json[o].payedAmount + '</div><div class="close-date">' + json[o].accountedAmount + '</div></td>');

							}
							body.append("</tr>");
						}
					}
					// 展开团期详情
					sbrtr.toggle();
				} else {
					$.jBox.tip("该团期目前没有有效订单");
				}
			},
			error : function (e) {
				$.jBox.tip("该团期目前没有有效订单");
			}
		})
	} else {
		$.jBox.tip("该团期目前没有有效订单");
	}
}

$(function () {
	$('.team_a_click').toggle(function () {
		$(this).addClass('team_a_click2')
	}, function () {
		$(this).removeClass('team_a_click2')
	});
});

/**
 * 点击列表预报名按钮
 * @param productId
 * @param groupId
 */
function applyOrder(productId, groupId) {
	//渠道选择
	var _agentSelect = $("#agentIdSel").clone();
	$(_agentSelect).addClass("agentSelected");
	_agentSelect.show();

	var salerHtml = getSalerByAgentId(_agentSelect);

	//弹出框
	var $div = $("<div class=\"tanchukuang\"></div>")
		.append($('<div class="add_allactivity"><label>渠道选择：</label>').append(_agentSelect)).append(salerHtml);
	$div.append("</div>");
	var html = $div.html();
	$.jBox(html, {
		title : "选择渠道",
		persistent : true,
		buttons : {
			'预报名' : 1
		},
		submit : function (v, h, f) {
			apply(productId, groupId)
		},
		height : 200
	});
}

/**
 * 点击弹框预报名按钮
 * @param productId
 * @param productGroupId
 * @param payMode
 */
function apply(productId, productGroupId, payMode) {
	//渠道id
//	var selAgentdata = $(".agentSelected option:selected");
	var agentId = getBookAgent();
	//销售
	var salerId = $("#salerId").val();
	var agentSourceType = "1";
	if($('#agentSourceType') && $('#agentSourceType').size() > 0) {
		agentSourceType = $('#agentSourceType').val()
	}
	var param = "productId=" + productId + "&productGroupId=" + productGroupId + "&agentId="
		 + agentId + "&payMode=" + payMode + "&salerId=" + salerId + "&agentSourceType=" + agentSourceType;
	var activityKind = $('#activityKind').val();
	if (activityKind) {
		param += "&activityKind=" + activityKind;
	}
	var payStatus = "";
	if (payMode == "1") {
		payStatus = 2;
	} else if (payMode == "3") {
		payStatus = 1;
	} else {
		payStatus = 3;
	}
	param += "&payStatus=" + payStatus;
	window.open(sysCtx + "/orderCommon/manage/applyshowforModify?" + param);
}

//var requestTime = 0;  
//点击预定选择渠道弹出框
function agentType(obj) {
	
	// 删除多余样式
	$(".sign").removeClass("sign");
	
	// 初始化tempSaler
	tempSaleId = 0;  // 原自有渠道的销售人员
	tempQuauqSaleId = 0;  // 原quauq渠道的销售人员
	// T1--T2 先选择
	var activityType = $("#activityKind").val();
	var $theirOwnAgents = getTheirOwnAgents();  // 自有渠道
	var $quauqAgents = $("#quauqAgentTemp").clone();  // quauq渠道select
	$quauqAgents.attr("id", "quauqAgent").addClass("ui-front");
	//如果有待补位记录，则直接跳转到申请补位页面
	var groupId = $(obj).parents("tr:eq(0)").find("[name=groupid]").val();
	if (hasCoverOrder(groupId)) {
		if ($("#groupCoverFlag").val() == "1") {
			window.open(sysCtx + "/groupCover/list/" + groupId);
		} else {
			$.jBox.tip("存在待补位订单，请稍后进行报名");
		}
		return false;
	}
	
	//C183 点击预定，需要重新从后台请求渠道商
	var sel = document.createElement("select");
	var uuid=$("#companyUuid").val();
	$.ajax({
		async : false,
		type : "POST",
		url : sysCtx + "/activity/managerforOrder/getAgentinfoList",
		success : function (sss) {
			var msg = eval('(' + sss + ')');
			if (msg.res == "success") {
				var array = msg.agentinfos;
				for (var i = 0; i < array.length; i++) {
					var aname = "";
					//315需求,针对越柬行踪，将非签约渠道改为签约渠道
					if(uuid=='7a81b21a77a811e5bc1e000c29cf2586' && array[i].agentName=='非签约渠道'){
						aname='直客';
					}else{
						aname = array[i].agentName;
					}
					var aid = array[i].id;
					var newOpt = new Option(aname, aid);
					sel.options.add(newOpt);
				}
			}
		}
	});
	// 判断渠道数量，如果渠道数量大于0，弹出对话框
	var agentNum = 0;
	// 团期平台上架状态   0未上架 1已上架
	var agpT1 = -1; 
	// 批发商上架权限状态  0启用 1禁用
	//var officeT1 = -1;
	agentNum = $theirOwnAgents.children().size() + $quauqAgents.children().size();
	if (agentNum > 0) {
		// 0518需求,根据批发商上架权限状态,团期平台上架状态来展示订单来源
		$.ajax({
			async : false,
			type : "POST",
			url : sysCtx + "/activity/managerforOrder/getGroupAndOfficeT1PermissionStatus",
			data : {
				groupId : groupId
			},
			success : function (data) {
				if(data.result == "success"){
					agpT1 = data.agpT1;
					//officeT1 = data.officeT1;
				}else{
					return;
				}
			}
		});
		// 0518需求,根据批发商上架权限状态,团期平台上架状态来展示订单来源
		
		// 渠道来源选择
		var $_agentTypeSelect = $("#agentSourceTypeTemp").clone();
		$_agentTypeSelect.attr("id", "agentSourceType");
		$_agentTypeSelect.show();
		//渠道选择(初始为自有渠道)
		var _agentSelect = $theirOwnAgents.clone();
		_agentSelect.attr("id", "agentIdSelCl");
		$(_agentSelect).addClass("ui-front");
		$(_agentSelect).addClass("agentSelected");
		_agentSelect.show();
		
		//给点击的按钮添加指定的标记，方便找到对应的触发按钮
		$(obj).addClass("sign");
		//添加渠道商
		var addAgentinfoHtml = "";
		if ($("#isAddAgent").length > 0) {
			addAgentinfoHtml = "<input name='addAgentinfo' class='btn btn-primary' type='button' onclick='addAgentinfo()' value='新增渠道' style='width:100px;height:30px;margin-left:20px'>";
		}
		//销售
		var salerHtml = getSalerByAgentId(_agentSelect);
		//弹出框
		var $div = $("<div class=\"tanchukuang\"></div>");
		if (activityType == "2" && $("#quauqBookOrderPermission").val() == "1" && agpT1 == "1" && $("#officeShelfRightsStatus").val() == "0") {
			$div.append($('<div class="add_intermodalType"><label><span class="xing">*</span>订单来源：</label>').append($_agentTypeSelect));
		}
		var agentInfoMain = $('<div class="add_intermodalType"><label><span class="xing">*</span>渠道选择：</label>').append(_agentSelect).append($quauqAgents);
		agentInfoMain.append(addAgentinfoHtml);
		$div.append(agentInfoMain).append('<div class="add_intermodalType">' + salerHtml + '</div>');
		
		//付款方式
		var _orderTypeSelect = $(obj).next().clone();
		$(_orderTypeSelect).addClass("orderTypeSelected");
		$(_orderTypeSelect).show();
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
				if ($('#agentSourceType') && $('#agentSourceType').val() == '2' && !$("#quauqAgent").val()) {
					top.$.jBox.tip('尚未选择渠道！', 'info');
					return false;
				}
				orderPay();
			},
			height : 300,
			width : 600
		});
		// 渠道选择下拉框注册控件“可输入搜索的下拉框”，并绑定选择事件：根据渠道获取销售
		$("#agentIdSelCl").comboboxInquiry().on('comboboxinquiryselect', function (event, obj) {
			getSalerByAgentId(this);
		});
		// 渠道选择下拉框的可输入框中，失去焦点事件：1.根据渠道获取销售 2.回显非签约渠道（由于控件使用特性，在粘贴前是否失去焦点，直接影响粘贴渠道全称能否正确被控件识别出粘贴内容是属于下拉数据的某一条。如果未被识别，则渠道依旧是非签约渠道）
		$("#agentIdSelCl").next().find("input").blur(function(){
			if($("#agentIdSelCl").val() == '-1'){
				if($("#companyUuid").val() == yuejianxingzong){					
					$("#agentIdSelCl").next().find("input").val("直客");
				} else {					
					$("#agentIdSelCl").next().find("input").val("非签约渠道");
				}
			}
			getSalerByAgentId($("#agentIdSelCl"));
		});
		$("#quauqAgent").comboboxInquiry().next().hide();
	} else {
		$.jBox.tip("请配置渠道商", "警告");
	}
	
	$("#agentSourceType").val("2").trigger("change");
	
//	requestTime = 0;
}

/**
 * 是否有待补位记录
 * @returns {Boolean}
 */
function hasCoverOrder(groupId) {
	var flag = false;
	$.ajax({
		async : false,
		type : "POST",
		data : {
			groupId : groupId
		},
		url : sysCtx + "/groupCover/getGroupCoverInfo",
		success : function (result) {
			if (Number(result.groupCoverNum) > 0) {
				flag = true;
			}
		}
	});
	return flag;
}

//增加渠道商链接
function addAgentinfo() {
	//移除弹窗（选择渠道和付款方式）
	window.parent.window.jBox.close();

	window.open(sysCtx + "/agent/manager/firstForm");
}

//占位
function occupied(groupId, srcActivityId, payMode, yws) {
	var orderType = $('#activityKind').val();
	if (orderType == '2' || orderType == '10') {
		showQwOrYw("预订", srcActivityId, groupId, payMode, yws);
	} else {
		showYw("预订", srcActivityId, groupId, payMode, yws);
	}
}

/**
 * 选择渠道和付款方式后点击弹出框中的预定按钮执行
 */
function orderPay() {
	var selectType = $(".orderTypeSelected option:selected").val();
	//根据所选付款方式找到对应的执行方法
	if (selectType == 3) {
		$(".sign").prevAll(".normalPayType").click();
	} else if (selectType == 7) {
		$(".sign").prevAll(".opPayType").click();
	} else if (selectType == 8) {
		$(".sign").prevAll(".cwPayType").click();
	} else if (selectType == 1) {
		$(".sign").prevAll(".dingjin_PayType").click();
	} else if (selectType == 2) {
		$(".sign").prevAll(".yuzhan_PayType").click();
	} else if (selectType == 4) {
		$(".sign").prevAll(".data_PayType").click();
	} else if (selectType == 5) {
		$(".sign").prevAll(".guarantee_PayType").click();
	} else if (selectType == 6) {
		$(".sign").prevAll(".express_PayType").click();
	}
	//移除添加的指定标记
	$(".sign").removeClass("sign");
}

/**
 * 报名确定后，获取最终选择的渠道
 */
function getBookAgent(){
	var agentSource = "1";
	if($("#agentSourceType")){
		agentSource = $("#agentSourceType").val();
	}
	var agentId = -1;
	if (agentSource == "2") {
		agentId = $("#quauqAgent").val();
	} else {
		var selAgentdata = $(".agentSelected option:selected");
		if (selAgentdata.length == 0) {
			selAgentdata = $(".inputagentId");
		}
		agentId = selAgentdata.val();
	}
	return agentId;
}

/**
 * 判断切位还是余位
 * @param title 显示标题
 * @param productId 产品ID
 * @param productGroupId 团期ID
 * @param payMode 付款类型
 * @param _this 按钮对象
 */
function showQwOrYw(title, productId, productGroupId, payMode, yws) {
	var agentId = getBookAgent();
	//获取销售
	var salerId = $("#salerId").val();
	//获取所选渠道的切位情况
	var leftNum = getLeftPayReservePosition(productId, productGroupId, agentId);
	//切位数
	var qws = leftNum;
	//余位数
	var flag = false;
	var placeHolderType = 0;
	if (yws <= 0 && qws <= 0) {
		top.$.jBox.tip('余位数不足，不能预订', 'error');
		return;
	} else if (yws <= 0) {
		placeHolderType = 1;
		flag = true;
	} else if (qws <= 0) {
		placeHolderType = 0;
		flag = true;
	}
	if (flag) {
		showforModify(productId, productGroupId, payMode, agentId, placeHolderType, salerId);
	} else {
		var $div = $("<div class=\"tanchukuang\"></div>")
			.append('<div class="add_allactivity"><label>订单类型：</label><p><span><input type="radio" name="flytype" checked="checked" value="1" class="radio">切位订单，剩余' + qws + '</span><br><span><input type="radio" name="flytype" value="0" class="radio">余位订单，剩余' + yws + '</span></p>')
			.append('<div class="add_intermodalType"><label>付款方式：' + $(".orderTypeSelected option:selected").text() + '</label>');
		var html = $div.html();
		$.jBox(html, {
			title : "选择渠道和付款方式",
			persistent : true,
			buttons : {
				'预定' : 1
			},
			submit : function (v, h, f) {
				//选择切位订单或者余位订单 1：切位 0：余位
				showforModify(productId, productGroupId, payMode, agentId, $("input[name='flytype']:checked").val(), salerId);
			},
			height : 245,
			width : 450
		});
	}
}

function showYw(title, productId, productGroupId, payMode, yws) {
	var agentId = getBookAgent();
	//获取销售
	var salerId = $("#salerId").val();
	if (yws <= 0) {
		top.$.jBox.tip('余位数不足，不能预订', 'error');
		return;
	} else {
		showforModify(productId, productGroupId, payMode, agentId, 0, salerId);
	}
}

/**
 * 获取所选渠道商剩余的切位人数
 * @param productId 产品ID
 * @param productGroupId 团期ID
 * @param agentId 渠道商ID
 */
function getLeftPayReservePosition(productId, productGroupId, agentId) {
	var leftNum = 0;
	$.ajax({
		async : false,
		type : "POST",
		url : sysCtx + "/orderCommon/manage/getLeftPayReservePosition",
		data : {
			productId : productId,
			productGroupId : productGroupId,
			agentId : agentId
		},
		success : function (msg) {
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

function showforModify(productId, productGroupId, payMode, agentId, placeHolderType, salerId) {
	var param = "&agentId=" + agentId + "&placeHolderType=" + placeHolderType;
	var activityKind = $('#activityKind').val();
	if (activityKind) {
		param += "&activityKind=" + activityKind;
	}
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
	var agentSourceType = "1";
	if($('#agentSourceType') && $('#agentSourceType').size() > 0){
		agentSourceType = $('#agentSourceType').val();
	}
	// 如果三种游客类型都没有quauq价，则不允许报名
	if(agentSourceType == "2"){
		var $signTr = $(".sign").closest("tbody");
		if(!$signTr.find("#quauqAdultPrice").val() && !$signTr.find("#quauqChildPrice").val() && !$signTr.find("#quauqSpecialPrice").val()){
			top.$.jBox.tip("本团暂无供应价！",'info');
			return;
		}
		if(Number($signTr.find("#quauqAdultPrice").val()) < 0 || Number($signTr.find("#quauqChildPrice").val() < 0) || Number($signTr.find("#quauqSpecialPrice").val() < 0)){
			top.$.jBox.tip("本团存在负QUAUQ价格，负QUAUQ价格对应类型的游客不允许报名！",'警告');
		}
	}
	window.open(sysCtx + "/orderCommon/manage/showforModify?payMode=" + payMode + "&payStatus=" + payStatus + "&productId=" + productId + "&productGroupId=" + productGroupId + param + "&salerId=" + salerId + "&agentSourceType=" + agentSourceType);
}

function letDivCenter(divName) {
	var top = ($(window).height() - $(divName).height()) / 2;
	$(divName).css({
		'top' : top
	}).show();
}
function sortby(sortBy, obj) {
	var temporderBy = $("#orderBy").val();
	if (temporderBy.match(sortBy)) {
		sortBy = temporderBy;
		if (sortBy.match(/ASC/g)) {
			sortBy = sortBy.replace(/ASC/g, "");
		} else {
			sortBy = $.trim(sortBy) + " ASC";
		}
	}
	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
}

function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#searchForm").submit();
}

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	var showType = $("#showType").val();
	var activityKind = $("#activityKind").val();
	$("#searchForm").attr("action", sysCtx + "/activity/managerforOrder/list/" + showType + "/" + activityKind);
	$("#searchForm").submit();
}

function downloads(docid) {
	window.location.href = sysCtx + "/sys/docinfo/download/" + docid;
}

function takeOpenDate(obj) {
	var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
	return groupOD;
}

function takeVisaDate(obj) {
	var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
	return groupOD;
}

function expand(child, obj, showType) {
	if ($(child).is(":hidden")) {
		if (showType == '1') {
			$(obj).html("关闭团期预定");
		} else {
			$(obj).html("关闭团期预报名");
		}
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
		divScroll();//518 展开团期预定时判断是否添加滚动条
	} else {
		if (!$(child).is(":hidden")) {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			if (showType == '1') {
				$(obj).html("展开团期预定");
			} else {
				$(obj).html("展开团期预报名");
			}
		}
	}
}

/**
 * 获取批发商自有渠道
 */
function getTheirOwnAgents(){
	var sel = document.createElement("select");
	var uuid=$("#companyUuid").val();
	$.ajax({
		async : false,
		type : "POST",
		url : sysCtx + "/activity/managerforOrder/getAgentinfoList",
		success : function (sss) {
			var msg = eval('(' + sss + ')');
			if (msg.res == "success") {
				var array = msg.agentinfos;
				for (var i = 0; i < array.length; i++) {
					var aname = "";
					//315需求,针对越柬行踪，将非签约渠道改为签约渠道
					if(uuid=='7a81b21a77a811e5bc1e000c29cf2586' && array[i].agentName=='非签约渠道'){
						aname='直客';
					}else{
						aname = array[i].agentName;
					}
					var aid = array[i].id;
					var newOpt = new Option(aname, aid);
					sel.options.add(newOpt);
				}
			}
		}
	});
	return $(sel);
}

var tempSaleId = 0;  // 原自有渠道的销售人员
var tempQuauqSaleId = 0;  // 原quauq渠道的销售人员
/**
 * 改变渠道来源类型（自有渠道、quauq渠道）获取对应所有渠道
 * @param obj
 */
function changeAgentSource(obj){
	var agentSource = "1";
	agentSource = $(obj).val();
	if (agentSource == "2") {  // quauq渠道
		tempSaleId = $("#salerId").val();
		$("#agentIdSelCl").next().hide();
		$("#quauqAgent").next().show();
		$("input[name=addAgentinfo]").hide();
		// 销售人员展示所有人，默认当前用户
		getQuauqAgentSalers();
		if(tempQuauqSaleId != 0) {
			$("#salerId").val(tempQuauqSaleId);
		}
		agentSourceType = "2";
	} else {  // 自有渠道
		tempQuauqSaleId = $("#salerId").val();  // 切换到自有渠道，在改变销售之前记录原quauq渠道销售
		$("#agentIdSelCl").next().show();
		$("#quauqAgent").next().hide();
		$("input[name=addAgentinfo]").show();
		getAgentSalers($("#agentIdSelCl").val());
		$("input[name=addAgentinfo]").show();
		$("#salerId").val(tempSaleId);
		agentSourceType = "1";
	}
}

/**
 * quauq渠道显示批发商所有用户，默认当前登录用户
 */
function getQuauqAgentSalers() {
	$.ajax({
		async : false,
		type : "POST",
		url : sysCtx + "/orderCommon/manage/getSalers4QUAUQ",
		success : function (msg) {
			$("#salerId").empty();
			for (var prop in msg) {
				if (prop == 'loginUserId' || prop == 'userNum') {
					continue;
				}
				if (msg.loginUserId == prop) {
					$("#salerId").append('<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>');
				} else {
					$("#salerId").append('<option value=' + prop + '>' + msg[prop] + '</option>');
				}
			}
		}
	});
}

/**
 * quauq渠道显示批发商所有用户，默认当前登录用户
 */
function getAgentSalers(agentId) {
	$.ajax({
		async : false,
		type : "POST",
		url : sysCtx + "/orderCommon/manage/getSalerByAgentId",
		data : {
			agentId : agentId
		},
		success : function (msg) {
			$("#salerId").empty();
			for (var prop in msg) {
				if (prop == 'loginUserId' || prop == 'userNum') {
					continue;
				}
				if (msg.loginUserId == prop) {
					$("#salerId").append('<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>');
				} else {
					$("#salerId").append('<option value=' + prop + '>' + msg[prop] + '</option>');
				}
			}
		}
	});
}

/**
 * 获取渠道跟进销售
 * @param obj
 */
function getSalerByAgentId(obj) {
	var salerHtml = '<div class="activitylist_team_co3_text">销售：</div>';
	if ($("#salerId").length > 0) {
		$("#salerId").empty();
		$.ajax({
			async : false,
			type : "POST",
			url : sysCtx + "/orderCommon/manage/getSalerByAgentId",
			data : {
				agentId : $(obj).val()
			},
			success : function (msg) {
				var jsonLength = 0;
				for (var prop in msg) {
					if (prop == 'loginUserId' || prop == 'userNum') {
						continue;
					}
					jsonLength++;
					if (msg.loginUserId == prop) {
						$("#salerId").append('<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>');
					} else {
						$("#salerId").append('<option value=' + prop + '>' + msg[prop] + '</option>');
					}
				}
//				if( jsonLength == 1 && $(obj).val() == '-1'){
//					$("#salerId").attr("disabled","disabled"); 
//				}else{
//					$('#salerId').removeAttr("disabled"); 
//				}
			}
		});
	} else {
		$.ajax({
			async : false,
			type : "POST",
			url : sysCtx + "/orderCommon/manage/getSalerByAgentId",
			data : {
				agentId : $(obj).val()
			},
			success : function (msg) {
				var jsonLength = 0;
				var tempStr = '';
				for (var prop in msg) {
					if (prop == 'loginUserId' || prop == 'userNum') {
						continue;
					}
					jsonLength++;
					if (msg.loginUserId == prop) {
						tempStr += '<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>';
					} else {
						tempStr += '<option value=' + prop + '>' + msg[prop] + '</option>';
					}
				}
//				if( jsonLength == 1 && $(obj).val() == '-1'){
//					salerHtml += '<select id="salerId" disabled="disabled">';
//				}else{
//					salerHtml += '<select id="salerId">';
//				}
				salerHtml += '<select id="salerId">';
				salerHtml = salerHtml+tempStr;
			}
		});
	}
	salerHtml += '</select>';
	return salerHtml;
}

function search(type) {
	if (type == 1) {
		$("#searchForm").attr("action", sysCtx + "/activity/managerforOrder/list/" + $("#showType").val() + "/" + $("#activityKind").val()).submit();
	} else {
		window.open(sysCtx + "/activity/managerforOrder/downloadAllYw/" + $("#showType").val() + "/" + $("#activityKind").val() + "?" + $('#searchForm').serialize());
		$("#searchForm").attr("action", sysCtx + "/activity/managerforOrder/list/" + $("#showType").val() + "/" + $("#activityKind").val()).submit();
	}
}

/**
 * 下载团期余位
 * @param groupId
 */
function downloadYw(groupId) {
	$("#groupId").val(groupId);
	$("#exportForm").submit();
}

//部分下载余位表
function partsOfYWDownload2() {
	var groupIds = "";
	$("input[name=groupid]:checked").each(function (index, obj) {
		groupIds += $(this).val() + ",";
	});
	if (groupIds == "") {
		alert('请选择团期', 'error');
		return false;
	} else {
		$("#paramGroupIds").val(groupIds);
		$("#partsForm").submit();
	}
}



/**
 * 订单或团期列表转换
 *
 */
function productOrGroupList(type) {
	$("#orderBy").val("");
	if (type == "product") {
		$("#productOrGroup").val("product");
	} else if (type == "group") {
		$("#productOrGroup").val("group");
	}
	$("#searchForm").submit();
}

function groupForOrder(obj) {
	$(obj).parents(".p0").find("input:eq(0)").trigger("click");
}

function coverFunction(url){
	window.open(url, "_blank");
}