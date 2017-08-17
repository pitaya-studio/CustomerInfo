var sysCtx = $("#sysCtx").val();
var groupId = $("#groupId").val();
function submitForm() {
	var groupId = $("#groupId").val();
	var coverPosition = $("#coverPosition").val();
	var remarks = $("#remarks").val();
	var freePosition = $("#freePosition").val();
	var groupcoverNum = $("#groupcoverNum").val();
	//	var freePosition = $("ul li:only-child").val();
	// 补位人数需大于余位人数
	if ((parseInt(coverPosition) > parseInt(freePosition) || parseInt(groupcoverNum) > 0) && parseInt(coverPosition) > 0) {
		$.ajax({
			type : "POST",
			url : sysCtx + "/groupCover/applyGroupCover",
			data : {
				groupId : groupId,
				coverPosition : coverPosition,
				remarks : remarks,
				freePosition : freePosition,
			},
			error : function (request) {
				top.$.jBox.tip("保存失败", "warning");
			},
			success : function (data) {
				if (data.result == "success") {
					top.$.jBox.tip("保存成功", "success");
					window.location.href = sysCtx + "/groupCover/list/" + groupId;
				} else {
					top.$.jBox.tip(data.error, "warning");
				}
			}
		});
	} else {
		top.$.jBox.tip("余位为" + freePosition + ",可直接进行预订", "warning");
	}
}

/**
 * 使用补位进行报名
 * @param coverId 补位记录id
 */
function bookByCover(coverId) {
	agentType4Cover(coverId);
}

//点击预定选择渠道弹出框
function agentType4Cover(coverId) {
	
	var $theirOwnAgents = getTheirOwnAgents();  // 自有渠道
	var $quauqAgents = $("#quauqAgentTemp").clone();  // quauq渠道select
	$quauqAgents.attr("id", "quauqAgent").addClass("ui-front");
	// 获取产品id 团期id 余位数
	var groupId = "";
	var srcActivityId = "";
	var groupCoverNum = 0;
	$.ajax({
		async : false,
		type : "POST",
		data : {
			coverId : coverId
		},
		url : sysCtx + "/groupCover/getCoverInfo",
		success : function (result) {
			groupId = result.groupId;
			srcActivityId = result.activityId;
			groupCoverNum = result.groupCoverNum;
		}
	});

	// 判断渠道数量，如果渠道数量大于0，弹出对话框
	var agentNum = 0;
	// 团期平台上架状态   0未上架 1已上架
	var agpT1 = -1; 
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
		//添加渠道商
		var addAgentinfoHtml = "<input class='btn btn-primary' type='button' onclick='addAgentinfo()' value='新增渠道' style='width:100px;height:30px;margin-left:20px'>";
		//销售
		var salerHtml = getSalerByAgentId(_agentSelect);
		//弹出框
		if ($("#quauqBookOrderPermission").val() == "1") {
			$div = $("<div class=\"tanchukuang\"></div>");
			if (agpT1 == "1") {
				$div.append($('<div class="add_allactivity"><label><span class="xing">*</span>订单来源：</label>').append($_agentTypeSelect));
			}
			$div.append($('<div class="add_allactivity"><label><span class="xing">*</span>渠道选择：</label>').append(_agentSelect).append($quauqAgents));
			$div.append(salerHtml);
		} else {			
			if ($('#isAddAgent').val() == 1) {
				var $div = $("<div class=\"tanchukuang\"></div>")
				.append($('<div class="add_allactivity"><label><span class="xing">*</span>渠道选择：</label>').append(_agentSelect).append(addAgentinfoHtml)).append(salerHtml);
			} else {
				var $div = $("<div class=\"tanchukuang\"></div>")
				.append($('<div class="add_allactivity"><label><span class="xing">*</span>渠道选择：</label>').append(_agentSelect)).append(salerHtml);
			}
		}
		//付款方式
		var _orderTypeSelect = "<select><option value='2' selected='selected'>预占位</option></select>";
		$div.append($('<div class="add_intermodalType"><label>付款方式：</label>').append(_orderTypeSelect));
		$div.append("</div>");
		var html = $div.html();
		// 弹窗
		$.jBox(html, {
			title : "选择渠道和付款方式",
			persistent : true,
			buttons : {
				'预定' : 1
			},
			submit : function (v, h, f) {
				bookOrderForCover(srcActivityId, groupId, coverId, groupCoverNum);
			},
			height : 300,
			width : 600
		});
		// 注册可输入可下拉控件、及下拉动作
		$("#agentIdSelCl").comboboxInquiry().on('comboboxinquiryselect', function (event, obj) {
			getSalerByAgentId(this);
		});
		$("#quauqAgent").comboboxInquiry().next().hide();
	} else {
		$.jBox.tip("请配置渠道商", "警告");
	}
}

function bookOrderForCover(productId, productGroupId, coverId, groupCoverNum) {
	//获取所选渠道
	var selAgentdata = $(".agentSelected option:selected");
	if (selAgentdata.length == 0) {
		selAgentdata = $(".inputagentId");
	}
	//获取销售
	var salerId = $("#salerId").val();
	var agentId = getBookAgent();
	var agentSourceType = "1";
	if($('#agentSourceType') && $('#agentSourceType').size() > 0){
		agentSourceType = $('#agentSourceType').val();
	}
	var param = "&agentId=" + agentId + "&placeHolderType=0&coverId=" + coverId + "&groupCoverNum=" + groupCoverNum;
	param += "&activityKind=2";
	window.open(sysCtx + "/orderCommon/manage/showforModify?payMode=2&payStatus=3&productId=" + productId + "&productGroupId=" + productGroupId + param + "&salerId=" + salerId + "&agentSourceType=" + agentSourceType);
}
