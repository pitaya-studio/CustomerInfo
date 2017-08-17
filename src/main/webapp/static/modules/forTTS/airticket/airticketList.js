
$().ready(function() {

});

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action", root+"/airTicket/list/" + productStatus);
	$("#searchForm").submit();
}


var airTicketIds = "";

function checkall(num) {
	if (document.getElementById("allChk" + num).checked == true) {
		document.getElementById("allChk1").checked = true;
		document.getElementById("allChk2").checked = true;
		var ids = document.getElementsByName("airticketId");

		for ( var i = 0; i < ids.length; i++) {
		
			ids[i].checked = true;
			if (airTicketIds.indexOf(ids[i].value) < 0) {
				airTicketIds = airTicketIds + ids[i].value + ",";
			}
		}

	} else {
		document.getElementById("allChk1").checked = false;
		document.getElementById("allChk2").checked = false;
		var ids = document.getElementsByName("airticketId");

		for ( var i = 0; i < ids.length; i++) {
			ids[i].checked = false;
			if (airTicketIds.indexOf(ids[i].value) >= 0) {
				airTicketIds = airTicketIds.replace(ids[i].value + ",", "");

			}
		}

	}
	$("#airTicketIds").val(airTicketIds);
}

function idcheckchg(obj) {
	var value = $(obj).val();
	if ($(obj).attr("checked")) {
		if (airTicketIds.indexOf($(obj).val()) < 0) {
			airTicketIds = airTicketIds + $(obj).val() + ",";
		}
	} else {
		if ($("input[name='allChk']").attr("checked"))
			$("input[name='allChk']").removeAttr("checked");
		if (airTicketIds.indexOf($(obj).val()) >= 0) {

			airTicketIds = airTicketIds.replace($(obj).val() + ",", "");
		}
	}
	$("#airTicketIds").val(airTicketIds);
}

function productModify(proId) {
	$("#searchForm").attr("action", root+"/airTicket/mod/" + proId);
	$("#searchForm").submit();
}

//是否选中产品
function confirmBatchIsNull(mess, sta) {
	if (airTicketIds != "") {
		if (sta == 'off') {
			confirmBatchOff(mess);
		} else if (sta == 'del') {
			confirmBatchDel(mess);
		} else {
			confirmBatchRelease(mess);
		}
	} else {
		$.jBox.error('未选择产品', '系统提示');
	}
}

// 批量删除确认对话框
function confirmBatchDel(mess) {
	top.$.jBox.confirm(mess, '系统提示', function(v) {
		if (v == 'ok') {
			//loading('正在提交，请稍等...');
			$("#searchForm").attr("action",
					root+"/airTicket/batchdelAirticket/" + airTicketIds);
			$("#searchForm").submit();
		}
	}, {
		buttonsFocus : 1
	});
	top.$('.jbox-body .jbox-icon').css('top', '55px');
	return false;
}
// 批量下架确认对话框
function confirmBatchOff(mess) {
	top.$.jBox.confirm(mess, '系统提示', function(v) {
		if (v == 'ok') {
			//loading('正在提交，请稍等...');
			$("#searchForm").attr("action",root+"/airTicket/batchOffAirtickets/2/" + airTicketIds);
			$("#searchForm").submit();
		}
	}, {
		buttonsFocus : 1
	});
	top.$('.jbox-body .jbox-icon').css('top', '55px');
	return false;
}
// 批量上架确认对话框
function confirmBatchRelease(mess) {
	top.$.jBox.confirm(mess, '系统提示', function(v) {
		if (v == 'ok') {
			//loading('正在提交，请稍等...');
			$("#searchForm").attr("action",
					root+"/airTicket/batchOffAirtickets/3/" + airTicketIds);
			$("#searchForm").submit();
		}
	}, {
		buttonsFocus : 1
	});
	top.$('.jbox-body .jbox-icon').css('top', '55px');
	return false;
}



//单个产品上架
function releaseProduct(mess, proId) {
	top.$.jBox.confirm(mess, '系统提示',
			function(v) {
				if (v == 'ok') {
					loading('正在提交，请稍等...');
					$("#searchForm").attr("action",
							root+"/airTicket/release/" + proId);
					$("#searchForm").submit();
				}
			}, {
				buttonsFocus : 1
			});
	top.$('.jbox-body .jbox-icon').css('top', '55px');
	return false;
}

function onLineProduct(activityId) {
	$.jBox.confirm("确定要上架该产品吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$("#searchForm").attr("action",
					root+"/airTicket/batchrelease/" + activityId);
			$("#searchForm").submit();
		} else if (v == 'cancel') {

		}
	});
}

//产品删除
function confirmxDel(mess, proId) {

	top.$.jBox.confirm(mess, '系统提示', function(v) {
		if (v == 'ok') {
			$("#searchForm").attr("action",
					root+"/airTicket/delAirticket/" + proId);
			$("#searchForm").submit();
		}
	}, {
		buttonsFocus : 1
	});
	top.$('.jbox-body .jbox-icon').css('top', '55px');
	return false;
}


//展开收起
 function expand(child, obj, airticketId) {
	if ($(child).is(":hidden")) {

		$(obj).html("收起");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");

	} else {
		if (!$(child).is(":hidden")) {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开航程");
		}
	}
}

function downloads(docid, acitivityName, iszip) {
	if (iszip) {
		var zipname = acitivityName;
		
		var fileUrl =encodeURI(root+"/sys/docinfo/fileExists/" + docid + "/"+zipname);
		 $.ajax({ url: encodeURI(fileUrl),async:false, success: function(result){	 
			  if("文件存在"==result){
				  window.open(encodeURI(encodeURI(root+"/sys/docinfo/zipdownload/" + docid + "/" + zipname)));
			  } else{
				  top.$.jBox.tip("文件不存在", 'warnning');
			  }
		}});
		
		//window.open(encodeURI(encodeURI(root+"/sys/docinfo/zipdownload/" + docid + "/" + zipname)));
	}

	else
		window.open(root+"/sys/docinfo/download/" + docid);
}


