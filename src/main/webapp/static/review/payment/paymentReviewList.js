function selectQuery(){
	$("#reviewerId").comboboxInquiry();
	$("#supplyId").comboboxInquiry();
	$("#agentId").comboboxInquiry();
}
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();
	//地接社、渠道商切换
	switchSupplierAndAgent();
	//查询条件是否展开
	closeOrExpand();
	inputTips();

	//renderSelects(selectQuery());
	selectQuery();

	//$("#supplyId").comboboxInquiry();
	//renderSelects(selectQuery());
	//$("#reviewerId").comboboxInquiry();
	// $("#agentId").comboboxInquiry();
	//审核页签切换
	var _$review = $("#tabStatus").val() || '';
	$("#tabStatus_"+_$review).addClass("select");
	//默认排序
	var _$orderBy = $.trim($("#orderBy").val() || "create_date DESC");
	$("#orderBy").val(_$orderBy);
    var orderBy = _$orderBy.split(" ");
    $(".activitylist_paixu_left li").each(function(){
        if($(this).hasClass("li"+orderBy[0])){
            orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
            $(this).attr("class","activitylist_paixu_moren");
         }
    });
    var val = $("#wholeSalerKey").val();
    if(val){
    	$("#wholeSalerKey").next("span").hide(); 
    }
    //获得焦点显示隐藏
	$("#wholeSalerKey").focusin(function () {
		var obj_this = $(this);  
		obj_this.next("span").hide(); 
	}).focusout(function () { 
		var obj_this = $(this);  
		if (obj_this.val() != "") {
			obj_this.next("span").hide();  
		} else {
			obj_this.next("span").show();  
		}
	});
})

//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i=0;i<searchFormInput.length;i++) {
		var inputValue = $(searchFormInput[i]).val();
		if(inputValue) {
			inputRequest = true;
			break;
		}
	}
	for(var i=0;i<searchFormselect.length;i++) {
		var selectValue = $(searchFormselect[i]).children("option:selected").val();
		if(selectValue && selectValue != 0 && selectValue != -1) {
			selectRequest = true;
			break;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
}

function changeIsRecord(itemRecord){
	$("#tabStatus").val(itemRecord);
	$(".supplierLine .select").removeClass("select");
	$("#tabStatus_"+itemRecord).addClass("select");
	$("#searchForm").submit();
}

function checkall(obj){
    if($(obj).attr("checked")){
        $('#contentTable input[type="checkbox"]').attr("checked",'true');
        $("input[name='allChk']").attr("checked",'true');
    }else{
        $('#contentTable input[type="checkbox"]').removeAttr("checked");
        $("input[name='allChk']").removeAttr("checked");
    }
}

function checkreverse(obj){
    var $contentTable = $('#contentTable');
    $contentTable.find('input[type="checkbox"]').each(function(){
        var $checkbox = $(this);
        if($checkbox.is(':checked')){
            $checkbox.removeAttr('checked');
        }else{
            $checkbox.attr('checked',true);
        }
    });
}

function jbox__shoukuanqueren_chexiao_fab(ctx){
	var $contentTable = $('#contentTable');
	var items_val = '';
	var count = 0;
	$contentTable.find('input[name="batchItem"]').each(function(){
		if ($(this).is(":checked")){
			var temp = $(this).val();
			if(!items_val){
				items_val = temp;
			}else{
				items_val += ',' + temp;
			}
			count++;
		}
	})
	if(count == 0){
		$.jBox.tip("请选择需要审批的项目", 'error');
		return false;
	}
	$('#batch-verify-list').find('p:first').text('您好，当前您提交了'+count+'个审批项目，是否执行批量操作？');
	$.jBox($("#batch-verify-list").html(),{
		title: "批量审批", buttons: { '取消': 0,'驳回': 1, '通过': 2 }, submit: function (v, h, f) {
			var token = $('#token').val() || "";
			if (v == "1") {
				var comment = f.comment_val || '';
				return _denyCost(ctx, items_val, comment);
			}else if(v == "2"){
				var comment = f.comment_val || '';
				return _passCost(ctx, items_val,comment);
			}
		}, height: 250, width: 350
	});
}
function _passCost(ctx, items, comment){
	$.ajax({
		type: "POST",
		url: ctx + "/review/payment/web/batchApprove",
		cache:false,
		data:{items:items,comment:comment},
		success:function(data){
			if(data.flag){
				$.jBox.tip('审批通过', 'success');
				window.location.reload();
			}else{
				$.jBox.tip(data.msg, 'error');
				return false;
			}
		},
		error : function(e){
			$.jBox.tip("提交失败", 'error');
			return false;
		}
	});
}
function _denyCost(ctx, items, comment){
	$.ajax({
		type: "POST",
		url: ctx + "/review/payment/web/batchReject",
		cache:false,
		data:{items:items,comment:comment},
		success:function(data){
			if(data.flag){
				$.jBox.tip('驳回成功', 'success');
				window.location.reload();
			}else{
				$.jBox.tip(data.msg, 'error');
				return false;
			}
		},
		error : function(e){
			$.jBox.tip("提交失败", 'error');
			return false;
		}
	});
}
/**
 * 付款审批撤销功能
 * @param reviewUuid
 */
function back_review(ctx, reviewUuid){
	$.jBox.confirm("确定要撤销此审批吗？","提示",function(v,h,f){
		if(v == 'ok'){
			_back(ctx, reviewUuid, '');
		}
	})
}

function _back(ctx, reviewUuid, comment){
	$.ajax({
		type: "POST",
		url: ctx + "/review/payment/web/backReview",
		cache:false,
		data:{reviewId:reviewUuid,comment:comment},
		success:function(data){
			if(data.flag){
				$.jBox.tip('撤销成功', 'success');
				window.location.reload();
			}else{
				$.jBox.tip(data.msg, 'error');
				return false;
			}
		},
		error : function(e){
			$.jBox.tip("提交失败", 'error');
			return false;
		}
	});
}
//排序函数
function sortby(sortBy,obj){
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)){
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)){
            sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
        }else{
            sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
        }
    }else{
        sortBy = sortBy+" DESC";
    }
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
//分页查询函数
function page(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}