$(document).ready(function() {
	//选择框可输入选择
	$(".selectinput" ).comboboxInquiry({
			"afterInvalid":function(event,data){
				getUserInfo(this);
			}
	});
});

/**
 * 获取用户姓名和角色信息
 * @param obj
 * @returns
 */
function getUserInfo(obj) {
	if ($(obj).val() != "") {
		$.ajax({
			type: "POST",
			url: $("#getUserUrl").val(),
			data: {
				id : $(obj).val()
			},
			success: function(msg) {
				var userType = $(obj).attr("name");
				if ("leaveUserId" == userType) {
					$("#leave_users_msg").show().text("姓名：" + $(obj).find("option:selected").text() + " | ");
					$("#leave_users_msg2").show().text("角色：" + msg);
				} else {
					$("#transfer_users_msg").show().text("姓名：" + $(obj).find("option:selected").text() + " | ");
					$("#transfer_users_msg2").show().text("角色：" + msg);
				}
			}
		});
	} 
}

function transferLeaveAccount() {
	
	//判断是否有离职账号和转入账号
	var leaveUserId = $("#leaveUserId").val();
	var transferUserId = $("#transferUserId").val();
	if (leaveUserId == "") {
		top.$.jBox.info("请先选择离职账号", "警告");
	    top.$('.jbox-body .jbox-icon').css('top','55px');
	    return;
	}
	if (transferUserId == "") {
		top.$.jBox.info("请先选择转入账号", "警告");
	    top.$('.jbox-body .jbox-icon').css('top','55px');
	    return;
	}
	
	//确认是否要进行数据转移
	var mess = "确认要把 '" + $("#leaveUserId").find("option:selected").text() + "' 的数据转入'" + $("#transferUserId").find("option:selected").text() + "' 账户下吗？";
	top.$.jBox.confirm(mess,'系统提示',function(v) {
		if(v=='ok'){
			loading('正在进行数据转移，请稍等...');
			$.ajax({
				type: "POST",
				url: $("#searchForm").attr("action"),
				data: {
					businessType : $("#businessType").val(),
					modulesType : $("#modulesType").val(),
					leaveUserId : $("#leaveUserId").val(),
					transferUserId : $("#transferUserId").val()
				},
				success: function(msg) {
					$("#jbox-states").empty();
					top.$.jBox.tip(msg, 'warning');
	                top.$('.jbox-body .jbox-icon').css('top','55px');
	                $("#btn_search").click();
				}
			});
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}