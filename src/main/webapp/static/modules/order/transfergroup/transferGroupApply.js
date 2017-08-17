$(function () {
	/**
	 * 选择游客(单选)
	 */
	$("input.funCheckBox").change(function () {
		var check = $(this).attr("checked");

		if (check == "checked") {
			//alert("选中");
			var attrname = $(this).attr('name');
			var remark = $.trim($("input[type=text][name=theRemark" + attrname + "]").val());
			// 选中后不可修正
			$("input[type=text][name=theRemark" + attrname + "]").attr("disabled", "disabled");

			if (remark == "") {
				remark = "-";
			}
			var id = $.trim($("input[type=hidden][name=theTravelId" + attrname + "]").val());
			var text = $.trim($("input[type=hidden][name=theTravelName" + attrname + "]").val());

			$("#travelName").append("<span class='" + attrname + "'>&nbsp;" + text + "&nbsp;</span>");
			$("#changeGroupForm").append("<input type='hidden' name='paramTravelId' value='" + id + "'>");
			$("#changeGroupForm").append("<input type='hidden' name='paramRemark" + attrname + "' class='paramRemark' value='" + remark + "'>");
			///////////////////////////////////////////
			/**
			 * add by ruyi.chen
			 * add date 2015-04-13
			 * 流程互斥判断，增加游客名称组合
			 */
			$("#changeGroupForm").append("<input type='hidden' name='paramTravelerName' value='" + text + "'>");
			//////////////////////////////////////////
		} else {
			//alert("未选中");
			$("input[name=allChk]").removeAttr("checked");
			var attrname = $(this).attr('name');
			var id = $.trim($("input[type=hidden][name=theTravelId" + attrname + "]").val());
			var text = $.trim($("input[type=hidden][name=theTravelName" + attrname + "]").val());
			// 取消选中后可修正
			$("input[type=text][name=theRemark" + attrname + "]").removeAttr("disabled");
			var getName = $.trim($("#travelName span." + attrname + "").text());
			//var remark = $.trim($("input[type=text][name=theRemark"+attrname+"]").val());
			// 如果存在该用户，则删掉他
			if (getName) {
				$("#travelName span." + attrname + "").remove();
				$("#changeGroupForm input[type=hidden][name=paramTravelId][value=" + id + "]").remove();
				$("#changeGroupForm input[type=hidden][name=paramRemark" + attrname + "]").remove();
				$("#changeGroupForm input[type=hidden][name=paramTravelerName][value=" + text + "]").remove();
			}
		}
	});

	/**
	 * 选择游客(多选)
	 */
	$("input[name=allChk]").change(function () {
		//alert("全选");
		var allCheck = $(this);
		var check = $(this).attr("checked");
		// 选中全部的游客
		if (check == "checked") {
			// 选中前先清掉已选的游客
			$("#travelName").empty();
			$("#changeGroupForm input[type=hidden][name=paramTravelId]").remove();
			$("#changeGroupForm input[type=hidden][class=paramRemark]").remove();
			$("#changeGroupForm input[type=hidden][name=paramTravelerName]").remove();
			// 循环遍历全部游客信息
			$("input.funCheckBox").each(function () {
				var attrname = $(this).attr('name');
				$(this).attr("checked", "checked");
				var id = $.trim($("input[type=hidden][name=theTravelId" + attrname + "]").val());
				var text = $.trim($("input[type=hidden][name=theTravelName" + attrname + "]").val());
				var remark = $.trim($("input[type=text][name=theRemark" + attrname + "]").val());
				// 选中后不可修正
				$("input[type=text][name=theRemark" + attrname + "]").attr("disabled", "disabled");
				if (remark == "") {
					remark = "-";
				}
				$("#travelName").append("<span class='" + attrname + "'>&nbsp;" + text + "&nbsp;</span>");
				$("#changeGroupForm").append("<input type='hidden' name='paramTravelId' value='" + id + "'>");
				$("#changeGroupForm").append("<input type='hidden' name='paramRemark" + attrname + "' class='paramRemark' value='" + remark + "'>");
				$("#changeGroupForm").append("<input type='hidden' name='paramTravelerName' value='" + text + "'>");
			});
		} else { // 清掉全部游客
			$("input.funCheckBox").each(function () {
				$(this).removeAttr("checked");
			});
			$("#travelName").empty();
			$("#changeGroupForm input[type=hidden][name=paramTravelId]").remove();
			$("#changeGroupForm input[type=hidden][name=paramRemark]").remove();
			$("#changeGroupForm input[type=hidden][name=paramTravelerName]").remove();
			$("input.funCheckBox").each(function () {
				var attrname = $(this).attr('name');
				// 取消选中后可修正
				$("input[type=text][name=theRemark" + attrname + "]").removeAttr("disabled");
			});
		}

	});
	/**
	 * 选择占位方式（连带占位天数），放入form表单
	 */
	$("#payType").live("change", function () {
		var payType = $(this).val(); // 支付方式
		var remdays = $("#remDay").find("span[class=" + $(this).val() + "]").text(); // 保留天数
		// 清掉form中原有的支付方式和保留天数
		$("#changeGroupForm input[type=hidden][name=paramPayType]").remove();
		$("#changeGroupForm input[type=hidden][name=paramRemainDays]").remove();
		// 往form中添加支付方式和保留天数
		$("#changeGroupForm").append("<input type='hidden' name='paramPayType' value='" + payType + "'>");
		if (remdays) {
			$("#changeGroupForm").append("<input type='hidden' name='paramRemainDays' value='" + remdays + "'>");
		}
	});
});
//驳回
function applySwitchTeam() {
	var html = '<div class="jboxTips">余位不足</div>';
	$.jBox(html, {
		title : "提示",
		buttons : {
			"确 定" : "1"
		},
		submit : function (v, h, f) {
			if (v == "1") {}
		},
		height : 150,
		width : 250
	});
}
/**
 * ajax 异步成功后，填充转团信息
 */
function jsonSuccess(json) {

	var groupType = json.groupType;
	var groupCode = json.groupCode;
	var leaveSit = json.leaveSit;
	var productName = json.productName;
	var createBy = json.createBy;
	
	var payMode_advance =  json.payMode_advance;
	var payMode_deposit =  json.payMode_deposit;
	var payMode_full =  json.payMode_full;
	var payMode_op =  json.payMode_op;
	var payMode_cw =  json.payMode_cw;
	var payMode_data =  json.payMode_data;
	var payMode_guarantee =  json.payMode_guarantee;
	var payMode_express =  json.payMode_express;

	var remainDays_advance = json.remainDays_advance;
	var remainDays_deposit = json.remainDays_deposit;
	var remainDays_express = json.remainDays_express;
	var remainDays_guarantee = json.remainDays_guarantee;
	var remainDays_data = json.remainDays_data;

	$("#payType").empty();
	$("#remDay").empty();
	$("#payType").append("<option value=''>请选择</option>");
	if (payMode_advance == 1) {
		$("#payType").append("<option value='2'>预占位</option>");
		$("#remDay").append("<span class='2' style='display:none'>" + remainDays_advance + "</span>");
	}
	if (payMode_deposit == 1) {
		$("#payType").append("<option value='1'>订金占位</option>");
		$("#remDay").append("<span class='1' style='display:none'>" + remainDays_deposit + "</span>");
	}
	if (payMode_full == 1) {
		$("#payType").append("<option value='3'>全款支付</option>");
	}
	if (payMode_op == 1) {
		$("#payType").append("<option value='7'>计调确认占位</option>");
	}
	if(payMode_cw==1){
		$("#payType").append("<option value='8'>财务确认占位</option>");
	}
	if(payMode_data==1){
		$("#payType").append("<option value='4'>资料占位</option>");
		$("#remDay").append("<span class='4' style='display:none'>" + remainDays_data + "</span>");
	}
	if (payMode_guarantee == 1) {
		$("#payType").append("<option value='5'>担保占位</option>");
		$("#remDay").append("<span class='5' style='display:none'>" + remainDays_guarantee + "</span>");
	}
	if (payMode_express == 1) {
		$("#payType").append("<option value='6'>确认单占位</option>");
		$("#remDay").append("<span class='6' style='display:none'>" + remainDays_express + "</span>");
	}

	$("#leaveSit").empty();
	$("#leaveSit").text(leaveSit);

	$("#groupType").empty();
	$("#groupType").text(groupType);

	$("#groupCode").empty();
	$("#groupCode").text(groupCode);

	$("#productName").empty();
	$("#productName").text(productName);

	$("#createBy").empty();
	$("#createBy").text(createBy);

	// 将团号放入上传form
	var gro = $("input[type=hidden][name=paramGroupCode]");
	if (gro) {
		// 放入团号前先清掉上次放入的团号
		$("input[type=hidden][name=paramGroupCode]").remove();
	}
	$("#changeGroupForm").append("<input type='hidden' name='paramGroupCode' value='" + groupCode + "'>");
}

/**
 * 根据转入团号，展开转团信息
 */
function changeGroups() {
	// 新团号
	var gId = $.trim($("#intoGroupId").val());
	// 原订单团号
	var gIdOld = $.trim($("#oldGroupCode").text());
	if (gId == gIdOld) {
		jBox.tip("不可使用原团号。", 'error');
		$("#intoGroupId").val("");
		return;
	}
	$.ajax({
		type : "POST",
		url : contextPath + "/orderTransFerGroup/newGroupInfoAjax",
		data : {
			groupId : gId
		},
		dataType : "text",
		success : function (html) {
			var json;
			try {
				json = $.parseJSON(html);
				//json = html;
			} catch (e) {
				json = {
					res : "error"
				};
				// 隐藏提交按钮
				$("#formSubmit").hide();
			}
			if (json.res == "success") {
				jsonSuccess(json);
				$("#hiddenGroup").show();
				// 展开提交按钮
				$("#formSubmit").show();

			} else {
				jBox.tip("找不到对应团期。", 'error');
				$("#hiddenGroup").hide();
				$("#formSubmit").hide();
			}
		}
	});
}
var tokenTimes = 0;
/**
 * ajax 申请转团
 */
function upForm() {

	//扣减金额不能为空
	var flag = true;
	$("input.funCheckBox:checked").each(function (index, obj) {
		var subtractMoney = $($(this).parent().parent()).find("input[name='subtractMoney']").val();
		if (!subtractMoney || subtractMoney == "" || subtractMoney == null) {
			flag = false;
			return false;
		}
	});
	if (!flag) {
		jBox.tip("转团后应收金额不能为空", 'error');
		return;
	}
	var formTravelId = $("#changeGroupForm input[type=hidden][name=paramTravelId]").val();
	var fromGroupCode = $("input[type=hidden][name=paramGroupCode]").val();
	var formPayType = $("#changeGroupForm input[type=hidden][name=paramPayType]").val();
	$("#formSubmit").attr("readOnly", "readOnly");
	// 当游客被选中时，才允许转团
	if (formTravelId && fromGroupCode && $.isNumeric(formPayType)) {
		var the_param = paramFormSerialize();
		if(!validate4QUAUQ()){
			return false;
		}
		var flag = validateTransferGroup();
		if (!flag) {
			return false;
		}
		
		//禁止多次提交，提交成功后不能再次提交
		tokenTimes++;
		if (tokenTimes != 1) {
			return false;
		}
		
		$.ajax({
			type : "POST",
			url : contextPath + "/newTransferGroup/applyTransferGroup",
			data : the_param.replace(/\+/g, "%2B"),
			dataType : "text",
			success : function (html) {
				var msg;
				try {
					msg = $.parseJSON(html);
				} catch (e) {
					msg = {
						result : "error"
					};
				}
				if (msg.result == "success") {
					jBox.tip("提交成功", 'info');
					$("#formSubmit").hide();
					var orderID = $("#orderID").val();
					location.href = contextPath + "/newTransferGroup/list/" + orderID;
				} else {
					top.$.jBox.info(msg.msg,'系统提示',{submit:function(){
						tokenTimes = 0;
						$("#formSubmit").removeAttr("readOnly");
					}});
				}
			}
		});
	} else {
		jBox.tip("请选择要转团的用户或者查找指定团号并选择支付方式");
	}
}

/**
 * 校验（转入团对应游客类型必须有quauq价才能转团）
 */
function validate4QUAUQ(){
	var flag = true;
	//  散拼、quauq订单
	if($("#productType").val() =='2' && $("#priceType").val() == '2') {
		flag = false;
		var travelIds = new Array(); // 游客ID组
		$("#changeGroupForm input[type=hidden][name=paramTravelId]").each(function (obj, index) {
			travelIds.push($(this).val());
		});
		var newGroupCode = $("input[type=hidden][name=paramGroupCode]").val();
		var orderId = $("#orderID").val();
		$.ajax({
			type : "POST",
			async : false,
			url : contextPath + "/newTransferGroup/checkQuauqPrice4TransferGroup",
			data : {
				travelerIds : travelIds.join(','),
				orderId : orderId,
				newGroupCode : newGroupCode
			},
			success: function(resultMap){
				if (resultMap.flag == "success") {
					flag = true;
				} else {					
					top.$.jBox.info(resultMap.message,'系统提示',{});
					flag = false;
				}
			}
		});
	}
	return flag;
}

function validateTransferGroup(travelerIds, productType, orderId) {
	var flag = false;
	var travelIds = new Array(); // 游客ID组
	$("#changeGroupForm input[type=hidden][name=paramTravelId]").each(function (obj, index) {
		travelIds.push($(this).val());
	});
	var orderID = $("#orderID").val();
	var productType = $("#productType").val();
	$.ajax({
		type : "POST",
		async : false,
		url : contextPath + "/newTransferGroup/checkSingleGroupExitGroup",
		data : {
			travelerIds : travelIds.join(','),
			productType : productType,
			orderId : orderID
		},
		success: function(msg){
			if (msg.isMutexBoolean == false) {
				flag = true;
			} else {
				if (msg.canCancel == false) {
					top.$.jBox.info(msg.showMsg,'系统提示',{});
				} else {
					top.$.jBox.confirm(msg.showMsg,'系统提示',function(v){
						if (v=='ok') {
							$.ajax({
								type: "POST",
								url: contextPath + "/newTransferGroup/cancelOtherReview",
								data: {
									ids : msg.ids
								},
								async : false,
								success: function(msg){
									if (msg.success == 'success') {
										flag = true;
										upForm();
									} else {
										$.jBox.tip("申请失败!", "warning");
									}
								}
							});
						}
					},{buttonsFocus:1});
				}
			}
		}
	});
	return flag;
}


/**
 * 转团form参数
 */
function paramFormSerialize() {
	var param = "";
	var paramTravelIdList = new Array(); // 游客ID组
	var n1 = 0;
	$("#changeGroupForm input[type=hidden][name=paramTravelId]").each(function () {
		paramTravelIdList[n1] = $(this).val();
		n1++;
	});
	param += "paramTravelId=" + paramTravelIdList;
	var paramRemarkList = new Array(); // 改团原因组
	var num = 0;
	$("#changeGroupForm input[type=hidden][class=paramRemark]").each(function () {
		paramRemarkList[num] = $(this).val();
		num++;
	});
	param += "&paramRemark=" + paramRemarkList;

	var subtractMoneyArr = []; // 转团后应付金额
	//扣减金额处理
	$(":checkbox:checked").each(function (index, obj) {
		var subtractMoney = $($(this).parent().parent()).find("input[name='subtractMoney']").val();
		if (subtractMoney && subtractMoney != "" && subtractMoney != null) {
			var subtractCurrency = $($(this).parent().parent()).find("select[name='subtractCurrency']").find("option:selected").val();
			var travelId = $($(this).parent().parent()).find("input[name^='theTravelId']").val();
			subtractMoneyArr.push(travelId + "#" + subtractCurrency + "#" + subtractMoney);
		}
	});
	param += "&subtractMoneyArr=" + subtractMoneyArr.join(',');

	//////////////////////////////////
	/**
	 * add by ruyi.chen
	 * add date 2015-04-13
	 * 增加游客参数
	 */
	var paramTravelerNameList = new Array(); // 改团原因组
	var num = 0;
	$("#changeGroupForm input[type=hidden][name=paramTravelerName]").each(function () {
		paramTravelerNameList[num] = $(this).val();
		num++;
	});
	param += "&paramTravelerName=" + paramTravelerNameList;
	/////////////////////////////////////

	var paramGroupCode = $("#changeGroupForm input[type=hidden][name=paramGroupCode]").val(); // 新团号
	param += "&paramGroupCode=" + paramGroupCode;
	var paramPayType = $("#changeGroupForm input[type=hidden][name=paramPayType]").val(); // 支付方式
	param += "&paramPayType=" + paramPayType;
	var paramRemainDays = $("#changeGroupForm input[type=hidden][name=paramRemainDays]").val(); // 保留天数
	if ($.isNumeric(paramRemainDays)) {
		param += "&paramRemainDays=" + paramRemainDays;
	}
	return param;
}