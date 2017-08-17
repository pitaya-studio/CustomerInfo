var sysCtx;
var officeId;
$(function () {
	sysCtx = $("#sysCtx").val();
	officeId = $("#officeId").val();
	$("#agentId").comboboxInquiry();
	$("#officeIds").comboboxInquiry();
	$("#companyId").comboboxInquiry();
});

function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

function search(type) {
	if (type == 1) {
		$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/tradeDetail?officeId=" + officeId).submit();
	} else {
		window.open(sysCtx + "/quauqAgent/manage/downloadTradeOrder?officeId="+ officeId + "&" + $('#searchForm').serialize());
		$("#searchForm").attr("action", sysCtx + "/quauqAgent/manage/tradeDetail?officeId=" + officeId).submit();
	}
}

//设为已缴费
function already(){
	var orderIds = "";
    $(".delivery").each(function(){
        var $this=$(this);
        if($this.attr("checked")){
//            $this.parent().siblings().children(".already").removeClass("hide");
//            $this.parent().siblings().children(".notPay").addClass("hide");
            var orderId = $this.attr("value");
            orderIds = orderIds + orderId + ",";
        }
    })
    orderIds = orderIds.slice(0, orderIds.length-1);
    
    if(orderIds != ""){
	    // 后台处理
	    $.ajax({
			type: "POST",
		   	url: sysCtx+"/quauqAgent/manage/changeIsPayedCharge?officeId=" + officeId + "&" + $('#searchForm').serialize(),
		   	data: {orderIds:orderIds,changeType:1},
			dataType: "json",
		   	success: function(data){
		   		if(data.result == "success"){
		   			var orderList = orderIds.split(",");
		   		    for(var i = 0 ; i < orderList.length ; i++){
		   		    	$("#"+orderList[i]).find(".already").removeClass("hide");
		   		    	$("#"+orderList[i]).find(".notPay").addClass("hide");
		   		    	// 已缴费总计和未缴费总计局部更新
		   		    	$("#payedCharge").html(data.payedCharge);
		   		    	$("#unpayedCharge").html(data.unpayedCharge);
		   		    	//window.location.href = sysCtx + "/quauqAgent/manage/tradeDetail?officeId=" + officeId + "&" + $('#searchForm').serialize();
		   		    }
		   		}
		   	}
		});
    }
    
}
//设为已缴费
function notPay(){
	var orderIds = "";
    $(".delivery").each(function(){
        var $this=$(this);
        if($this.attr("checked")){
//            $this.parent().siblings().children(".already").addClass("hide");
//            $this.parent().siblings().children(".notPay").removeClass("hide");
            var orderId = $this.attr("value");
            orderIds = orderIds + orderId + ",";
        }
    })
    orderIds = orderIds.slice(0, orderIds.length-1);
    
    if(orderIds != ""){
	    // 后台处理
	    $.ajax({
			type: "POST",
		   	url: sysCtx+"/quauqAgent/manage/changeIsPayedCharge?officeId=" + officeId + "&" + $('#searchForm').serialize(),
		   	data: {orderIds:orderIds,changeType:0},
			dataType: "json",
		   	success: function(data){
		   		if(data.result == "success"){
		   			var orderList = orderIds.split(",");
		   		    for(var i = 0 ; i < orderList.length ; i++){
		   		    	$("#"+orderList[i]).find(".already").addClass("hide");
		   		    	$("#"+orderList[i]).find(".notPay").removeClass("hide");
		   		    	// 已缴费总计和未缴费总计局部更新
		   		    	$("#payedCharge").html(data.payedCharge);
		   		    	$("#unpayedCharge").html(data.unpayedCharge);
		   		    	//window.location.href = sysCtx + "/quauqAgent/manage/tradeDetail?officeId=" + officeId + "&" + $('#searchForm').serialize();
		   		    }
		   		}
		   	}
		});
    }
    
}

/**
 * 进入订单结算价操作记录页面
 */
function gotoPayPriceLog(obj) {
	
//	$.ajax({
//		type : "POST",
//		url : sysCtx + "/quauqAgent/manage/gotoPayPriceLog",
//		data : {aa : aa, bb : bb},
//		dataType : "json",
//		success : function(resultData){
//			if(resultData && resultData.flag == "success"){
//				
//			}
//		}
//	});
	var orderType = "2";
	var orderId = $(obj).val();
	window.open(sysCtx + "/quauqAgent/manage/gotoPayPriceLog/" + orderType +"/"+ orderId);
	
}
var $node;
function openPayPriceLog(obj) {
	var $pop = $.jBox($("#payprice_log").html(), {
    	title    : "结算价操作记录", buttons: {'关闭':1}, submit: function (v, h, f) {
        	if (v == "1") {
        	}
		},loaded:function(h, f){
			$node = h.find("#paypricelogTable");
			searchLogInfo(obj);
		}, height: '500', width: 900}
	);
}

//点击“选择账号”、“搜索”，发送ajax进入后台查询，返回json再解析
function searchLogInfo(obj){
	var orderType = "2";
	var orderId = $(obj).parents("tr").find("input[name=orderId]").val();
	var companyId = $("#officeId").val();
	$.ajax({
		type: "POST",
        url: sysCtx + "/quauqAgent/manage/gotoPayPriceLog",
        data: {
        	orderType : orderType,
        	orderId : orderId
        },
		success: function(resultMap){
			if(resultMap && resultMap.flag == 'success'){
				if(!parseLogJson2Tbody(resultMap.data)){
					top.$.jBox.tip("获取结算价操作记录失败！");
				};
			} else {
				top.$.jBox.tip("获取结算价操作记录失败！");
			}
		}
	});
}

// 依据json解析成tbody内容展示
function parseLogJson2Tbody(subJson){
	var html = "";
	if(subJson == undefined || subJson == null){
		return false;
	}
	if(subJson == ''){
		$node.empty().append(html);
		return true;
	}
	var json = eval(subJson);
	// json数组个数
	var jsonLength = json.length;
	// 判断为空
	if (jsonLength && jsonLength != 0) {
		// 循环获取html组合
		for (var i = 0; i < jsonLength; i++) {
			// 序列值
			var indexVal = i + 1;
			html += "<tr>";
			// 序号，隐藏域logId
			html += "<td><input name='logId' type='hidden' value='" + json[i].id + "'>" + indexVal + "</td>";
			// 操作人
			html += "<td name='operatorName' class='tc'><span>" + json[i].operatorName + "</span><input type='hidden' name='operatorId' value='" + json[i].operatorId + "'></td>";
			//操作功能
			html += "<td name='operation' class='tc'><span>" + json[i].operation + "</span></td>";
			//操作时间
			html += "<td name='operationTime' class='tc'><span>" + json[i].operationTime + "</span></td>";
			//操作内容
			html += "<td name='mainContext' class='tl'><span>" + json[i].mainContext + "</span></td>";
			html += "</tr>";
			
		}
		$node.empty().append(html);
	}
	return true;
}

/**
 * 订单详情
 *
 * param orderId
 */
function orderDetail(orderId) {
	window.open(sysCtx + "/orderCommon/manage/orderDetail/" + orderId, "_blank");
}