var contextPath;
var orderStatus;
$(function(){
	contextPath = $("#ctx").val();
	orderStatus = $("#orderStatus").val();
	
    if("${param.saveinvoiceMSG}" =="1") {
		top.$.jBox.tip('操作已成功!','success');	
	}
    $(".qtip").tooltip({
        track: true
    });
    
    $(document).scrollLeft(0);
    
	/* 前端js效果部分 */
			
    $("#contentTable").delegate("ul.caption > li","click",function() {
        var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
        $(this).addClass("on").siblings().removeClass('on');
        $(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
    });
		
    $('.handle').hover(function() {
    	if(0 != $(this).find('a').length){
    		$(this).addClass('handle-on');
			$(this).find('dd').addClass('block');
    	}
	},function(){$(this).removeClass('handle-on');$(this).find('dd').removeClass('block');});
  	// 文本框中提示信息
  	inputTips();
    //展开筛选按钮
    launch();
	//如果展开部分有查询条件的话，默认展开，否则收起	
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='orderNumOrGroupCode']");
	var searchFormselect = $("#searchForm").find("select[id!='orderShowType']");
	var inputRequest = false;
	var selectRequest = false;
	for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" && 
				$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if($("#orderShowType").length > 0 && $("#orderShowType").children("option:selected").val() != "0") {
		selectRequest = true;
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
			
    
    $("#contentTable").delegate(".tuanhao","click",function(){
        $(this).addClass("on").siblings().removeClass('on');
        $('.chanpin_cen').removeClass('onshow');
        $('.tuanhao_cen').addClass('onshow');
    });
    
    $("#contentTable").delegate(".chanpin","click",function(){
         $(this).addClass("on").siblings().removeClass('on');
         $('.tuanhao_cen').removeClass('onshow');
         $('.chanpin_cen').addClass('onshow');
        
    });

    /* 展示支付凭证 */
    $(document).delegate(".showpayVoucher","click",function() {
    	var orderIDValue = $(this).attr("lang");
    	$.ajax({
            type: "POST",
            url: contextPath + "/sys/docinfo/payVoucherList/"+orderIDValue,
            data: {
                orderId : orderIDValue,
                orderType : 12
            },
            success: function(msg){
            	var htmls = "<table class='activitylist_bodyer_table t-type-jbox'><thead><tr><th>凭证所属ID</th><th>凭证名称</th><th>下载链接</th></tr></thead><tbody>";
            	$.each(msg,function(key,value){
            		var docName = value.docName;
            		var orderId = value.payOrderId;
            		var id = value.id;
            		htmls = htmls+"<tr><td>"+orderId+"</td><td>"+docName+"</td><td><a class='downloadzfpz' lang="+id+">支付凭证</a></td></tr>";
            	});
            	htmls = htmls+"</tbody></table>";
            	$.jBox.open(htmls,"凭证列表",600,240);
            }
         });
    });
    
    $(document).delegate(".downloadzfpz","click",function(){
        window.open (contextPath + "/sys/docinfo/download/"+$(this).attr("lang"));
    });
    $( ".spinner" ).spinner({
		spin: function( event, ui ) {
			if ( ui.value > 365 ) {
                $( this ).spinner( "value", 1 );
                return false;
            } else if ( ui.value < 0 ) {
                $( this ).spinner( "value", 365 );
                return false;
            }
		}
	});

    //排序样式
	var _$orderBy = $("#orderBy").val();
	if (!_$orderBy || _$orderBy == "") {
		//默认按出团日期降序排序
	    _$orderBy = "groupOpenDate DESC";
	}
	var orderBy = _$orderBy.split(" ");
	// $(".filter_sort a").each(function() {
	//     if ($(this).attr("id") == orderBy[0]) {
	//     	if (orderBy[1] && orderBy[1].toUpperCase() == "ASC") {
	//     		$(this).find("i").attr("class", "i_sort i_sort_up");
	//     	} else {
	//     		$(this).find("i").attr("class", "i_sort i_sort_up i_sort_down");
	//     	}
	//     }
	// });
    //UG_V2修改
    $(".activitylist_paixu_left li").each(function() {

        if ($(this).hasClass(orderBy[0])) {
            // $(this).find("i").attr("class", "i_sort i_sort_up");
            _$orderBy = orderBy[1].toUpperCase() == "ASC"? "up" : "down";
            var arrow = "<i class=\"icon icon-arrow-" + _$orderBy + "\"></i>"
            $(this).find("a").eq(0).html($(this).find("a").eq(0).html() + arrow);
            $(this).addClass("activitylist_paixu_moren").removeClass("activitylist_paixu_left_biankuang");
        }
    });

	//订单列表和团期列表初始化：隐藏订单那或团期，判断显示那个标签
	// var isOrder = $("#isOrder").val();
	// if ("true" == isOrder) {
	// 	$("#orderLabel").attr("checked", true);
	// } else {
	// 	$("#groupLabel").attr("checked", true);
	// }
    //UG_V2修改
    var isOrder = $("#isOrder").val();
    if ("true" == isOrder) {
        $("#orderLabel").addClass("select")
    } else {
        $("#groupLabel").addClass("select")
    }
	
	//收款确认提醒
	$(".notice_price").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	})
	
	//将渠道选择改为可输入的select
	$("#orderCompany").comboboxInquiry();
	
	changeAgent();

});

/**
 * Date 2015-0614
 * 渠道选择：如果是非签约渠道，则弹出非签约渠道输入框；
 * 			 如果是签约渠道，则收起非签约渠道输入框
 */
function changeAgent() {
	var agentId = $("#orderCompany").val();
	if (agentId == -1) {
		$("#orderCompanyNameDiv").show();
	} else {
		$("#orderCompanyNameDiv").hide();
		$("#orderCompanyName").val('');
	}
}

/**
 * 订单删除
 * 
 * param orderId
 */
function deleteOrderByFlag(orderId) {
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
	            type: "POST",
	            url: contextPath + "/orderCommon/manage/deleteOrderByFlag",
	            data: {
	            	orderId:orderId
	            },
	            success: function(msg) {
	            	top.$.jBox.tip('删除成功','warning');
	            	location.reload(true);
	            }
            });
		} else if (v == 'cancel') {
                
		}
	});
}

/**
 * 取消订单
 * Date 2015-6-14
 * param orderUuid
 */
function cancelOrder(orderUuid) {
    var $div = $('<div class=\"tanchukuang\"></div>')
                .append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入取消原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
    var html = $div.html();
    var submit = function(v, h, f){
    	if(v === 0){
            return true;
        }
        if(f.description.length > 100){
            top.$.jBox.tip('输入字数为100字以内。','warning');
            return false;
        }
        else if(v === 1){
            $.ajax({
                type: "POST",
                url: contextPath + "/islandOrder/cancelOrder",
                data: {
                	orderUuid : orderUuid,
                    description : f.description
                },
                success: function(msg){
                	if(msg == 'fail') {
                    	top.$.jBox.tip('订单不能取消','warning');
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    } else if(msg == 'ok') {
                    	$("#btn_search").click();
                    } else {
                    	top.$.jBox.tip(msg,'warning');
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    }
                }
             });
        }
        return false;
    };
    $.jBox(html, {title: "取消原因", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
}

/**
 * 订单激活
 * Date 2015-6-14
 * param orderUuid
 */
function invokeOrder(orderUuid) {
	$.ajax({
        type: "POST",
        url: contextPath + "/islandOrder/invokeOrder?dom=" + Math.random(),
        data: {
        	orderUuid : orderUuid
        },
        success: function(msg) {
            if(msg == 'fail') {
            	top.$.jBox.tip('激活失败','warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
            } else if(msg == 'success') {
            	top.$.jBox.tip('激活成功','warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
                $("#btn_search").click();
            } else {
            	top.$.jBox.tip(msg,'warning');
                top.$('.jbox-body .jbox-icon').css('top','55px');
            }
        }
    });
}

/**
 * 修改支付凭证
 * Date 2015-6-16
 * @param payUuid 支付订单UUID
 * @param orderUuid 订单UUID
 */
function changepayVoucher(payUuid, orderUuid){
	window.open (contextPath + "/islandOrder/modifypayVoucher/" + payUuid + "/" + orderUuid);
}

/**
 * 支付记录
 * 
 * param orderId
 * param obj
 */
function showOrderPay(orderId, obj){
	var sbrtr = $(obj).parents("tr").next();
    var sbrtd = sbrtr.children().eq(0);
    var table = sbrtr.find("table[id=table_orderPay]");
    if(table.length<=0){
        $.ajax({
            type: "POST",
            url: contextPath + "/orderCommon/manage/getPayList",
            data: {
                orderId:orderId
            },
            success: function(msg){
                var $table = $("<table class=\"table activitylist_bodyer_table\" style=\"margin:0 auto;\"></table>").append("<thead style=\"background:#62AFE7\"><tr><th>付款方式</th><th>金额</th><th>日期</th><th>支付款类型</th><th>是否已确认达账</th><th>支付凭证</th><th>操作</th></tr></thead>");
             
                $.each(msg.orderList,function(key,value){
                    var payTypeName = value.payTypeName;
                    var isAsAccount = value.isAsAccount;
                    
                    if(payTypeName==null||payTypeName==undefined){
                        payTypeName="";
                    }
                    
                    if(isAsAccount=="0"||isAsAccount=="null"||isAsAccount==undefined){
                        isAsAccount="否";
                    }else if(isAsAccount=="1"){
                        isAsAccount="是";
                    }
                    
                    var payvoucher = "<td><a class=\"downloadzfpz\" lang=\""+value.payVoucher+"\">支付凭证ee</a></td>";
                    
                    if(payvoucher==""||payvoucher==undefined||payvoucher==null){
                        payvoucher = "<td>暂无支付凭证</td>";
                    }
                    
                    var paypricechange="<td><a class=\"changepayPrice\" lang=\""+value.payVoucher+"\""+"payprice=\""+value.payPrice+"\">修改</a></td>"
                    $table.append($("<tr></tr>")
                            .append("<td>"+payTypeName+"</td>")
                            .append("<td>"+value.payPrice+"</td>")
                            .append("<td>"+value.createDate+"</td>")
                            .append("<td>"+isAsAccount+"</td>")
                            .append(payvoucher)
                            .append(paypricechange)
                    )
                });
                sbrtd.append($table);
            }
         });
    }
    if($(obj).hasClass("jtk")){
        var td = $(obj).closest("td");
        if(sbrtr.is(":hidden")){
            sbrtr.show();
            td.addClass("td-extend");
        }else{
            sbrtr.hide();
            td.removeClass("td-extend");
        }
    }
}

//刷新 
function refresh(){
	setTimeout(location.reload(true),10000);   
}

/**
 * 订单支付
 * @param orderUuid
 * @param orderNum
 * @param orderType
 * @param orderCompany
 * @param totalMoney
 * @param orderDetailUrl
 */
function orderPay(orderUuid, orderNum, orderCompany, totalMoney, orderDetailUrl) {
	$.ajax({
        type: "POST",
        async:false,
        url: contextPath + "/islandOrder/whetherCanPay",
        dataType:"json",
        data:{orderUuid : orderUuid},
        success : function(result) {
        	var data = eval(result);
        	if(data && data[0].flag == "true") {
        		var cancelPayUrl = "/islandOrder/list/0";
        		var param = "resultCurrency=" + data[0].moneyCurrencyId + "&resultAmount=" + data[0].moneyCurrencyPrice + "&cancelPayUrl=" + cancelPayUrl;
        		window.open(contextPath + "/islandOrder/payIslandOrder/" + orderUuid + "?" + param);
        	} else {
        		var tips = data[0].warning;
        		top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
				top.$('.jbox-body .jbox-icon').css('top','55px');
        	}
		}
	});
}

/**
 * 翻页
 * @param n 条数
 * @param s 页数
 * @returns {Boolean}
 */
function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#searchForm").submit();
    return false;
}

/**
 * 发票申请
 *
 * param groupid
 * param orderid
 * param invoiceid
 * param orderCompany
 */
function makinvoice(groupid, orderid, invoiceid, orderCompany) {
	window.location.href = contextPath + "/invoice/limit/agentinvoice/"+groupid+"/"+orderid+"?invoiceid="+invoiceid+"&agentid="+orderCompany;
}


/**
 * 转款申请
 *
 * param orderId
 */

function transfersMoney(orderId){
	window.open(contextPath + "/orderCommon/transferMoney/transfersMoneyHref/" + orderId );
}

$(function(){
    $.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }
    
    $("#groupOpenDate").datepicker({
        dateFormat:"yy-mm-dd",
       dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
       closeText:"关闭", 
       prevText:"前一月", 
       nextText:"后一月",
       monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
       });
    
    $("#groupCloseDate").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
           
   $("#orderTimeBegin").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
	$("#orderTimeEnd").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
           });
    
});

/**
 * 排序
 * @param sortBy
 * @param obj
 */
function sortby(sortBy,obj) {
    var temporderBy = $("#orderBy").val();
    if (temporderBy.match(sortBy)) {
        sortBy = temporderBy;
        if (sortBy.match(/ASC/g)) {
            sortBy = $.trim(sortBy.replace(/ASC/gi,"")) + " DESC";
        } else {
            sortBy = $.trim(sortBy.replace(/DESC/gi,"")) + " ASC";
        }
    } else {
        sortBy = sortBy+" DESC";
    }
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}

/**
 * 退团
 *
 * param orderId
 */
function applyLeague(orderId){
    
    var $div = $('<div class=\"tanchukuang\"></div>')
    .append('<div class="msg-orderCancel"><div class="msg-orderCancel-t">请输入退团原因（输入字数为100字以内）：</div><textarea cols="30" rows="3" name="description"></textarea></div>');
    var html = $div.html();
    var submit = function(v, h, f) {
        if(v === 0){
            return true;
        }
        if(f.description.length > 100) {
            top.$.jBox.tip('输入字数为100字以内。','warning');
            return false;
        }
        else if(v === 1) {
            $.ajax({
                type: "POST",
                url: contextPath + "/orderCommon/manage/applyLeague",
                data: {
                    orderId:orderId,
                    description : f.description
                },
                success: function(msg){
                    location.reload();
                }
            });
        }
        return false;
    };
    $.jBox(html, {title: "退团原因", buttons:{'确定' : 1, '取消' : 0 }, submit: submit});
}

/**
 * 查询条件重置
 * 
 */
var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#orderShowType').val('${showType}');
}

/**
 * 转正
 *
 * param preproductorderId
 * param obj
 */
function changeToOrder(preproductorderId, obj) { 
	window.open(contextPath + "/orderCommon/manage/applygetOderInfoById?preproductorderById="+preproductorderId);
}

/**
 * 搜索查询
 * @param orderStatus 订单状态
 */
function query(orderStatus) {
	if ($("#orderShowType").size()>0) {
		orderStatus = $("#orderShowType").val();
	}
	$('#searchForm').attr("action", contextPath + "/islandOrder/list/" + orderStatus + ".htm");
	$('#searchForm').submit();
}

/**
 * 订单或团期列表转换
 * 
 */
function orderOrGroupList(type) {
	//清空排序值
	$("#orderBy").val("");
	//获取订单查询按团期或订单展示值并给参数赋值
	if(type == "order") {
		$("#isOrder").val(true);
	} else if(type == "group") {
		$("#isOrder").val(false);
	}
	//清空订单状态
	$('#orderShowType').val("");
	$("#searchForm").submit();
}

/**
 * 展开收起
 */
function expand(child, obj) {
	if($(child).is(":hidden")) {
		$(obj).html("收起");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	} else {
		if(!$(child).is(":hidden")) {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开");
		}
	}
}

/**
 * 导出订单中关于游客信息
 * param orderId 订单ID或下载文件ID
 * param downloadType 下载类型：游客资料、出团通知、确认单、面签通知
 */
function downloadData(orderId, downloadType) {
	if("traveler" == downloadType) {
		if(existData(orderId)) {
			$("#orderId").val(orderId);
			$("#downloadType").val(downloadType);
			$("#exportForm").submit();
		}
	} else if("confirmation" == downloadType || "group" == downloadType) {
		window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/download/" + orderId)));
	} 
}

//导出团期中关于游客信息
function exportExcel(groupId, groupCode, orderType) {
	$.ajax({
        type: "POST",
        url: contextPath + "/activity/manager/existExportData",
        dataType:"json",
        cache:false,
        data:{groupId : groupId, status : 'customer', orderType : orderType},
        success : function(result){
        	var data = eval(result);
        	if(data && data[0].flag == "true") {
        		$("#groupId").val(groupId);
        		$("#groupCode").val(groupCode);
				$("#exportTravelesForm").submit();
        	} else {
        		var tips = data[0].warning;
        		top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
				top.$('.jbox-body .jbox-icon').css('top','55px');
        	}
		}
	});
}

/**
 * 验证订单是否有游客信息
 * param orderId 订单ID
 */
function existData(orderId) {
	var flag = false;
	$.ajax({
        type: "POST",
        async:false,
        url: contextPath + "/orderCommon/manage/existExportData",
        dataType:"json",
        data:{orderId : orderId},
        success : function(result) {
        	var data = eval(result);
        	if(data && data[0].flag == "true") {
        		flag = true;
        	} else {
        		var tips = data[0].warning;
        		top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
				top.$('.jbox-body .jbox-icon').css('top','55px');
        	}
		}
	});
	return flag;
}
/**
 * 查看团签
 * param id   订单唯一标识
 * author     chenry
 * createDate 2014-11-03
 */
function viewGroupVisa(id) {
	window.open(contextPath + "/orderCommon/manage/viewGroupVisa?id="+265);
}

/**
 * 退团
 * Date 2015-6-17
 * @param orderUuid 订单UUID
 */
function viewExitGroup(orderUuid) {	
	window.open(contextPath + "/islandOrder/viewExitGroup?flowType=8&orderUuid=" + orderUuid);
}
/**
 * 改价
 * Date 2015-6-29
 * @param orderUuid 订单UUID
 */
function viewIslandChangePriceList(orderId,orderUuid) {	
	window.open(contextPath + "/island/review/islandChangePriceList?orderUuid="+orderUuid+"&orderId="+orderId+"&productType=12&flowType=10");
}
function viewIslandChangePriceListNew(orderId,orderUuid) {	
	window.open(contextPath + "/islandOrder/changePrice/islandChangePriceRecorderList?orderUuid="+orderUuid+"&orderId="+orderId+"&productType=12&flowType=10");
}
/**
 * 团队退款
 * param id   订单唯一标识
 * author     chenry
 * createDate 2014-11-04
 */
function viewGroupRefund(id,orderType,orderUuid) {	
	window.open(contextPath + "/orderCommon/manage/viewIslandGroupRefund?productType=12&&flowType=1&orderId="+id+"&orderUuid="+orderUuid);
}

/**
 * 转团
 * @param orderId
 */
function changeGroup(orderId){
	window.open(contextPath + "/islandapplications/goToIslandOrderList/"+orderId);
}
/**
 * 申请借款列表
 * param id   订单唯一标识
 * author     chenry
 * createDate 2015-05-12
 */
function viewBorrowingList(id,orderType,uuid) {	
	window.open(contextPath + "/island/review/viewBorrowingList?productType="+orderType+"&flowType=19&orderId="+id+"&orderUuid="+uuid);
}
/**
 * 订单页面查看订单发票信息列表
 * param orderNum   订单号
 * author     chenry
 * createDate 2014-11-14
 */
function viewInvoiceInfo(orderId,orderNum,orderType){
	window.open(contextPath + "/orderInvoice/manage/getInvoiceListByOrderNum?orderId="+orderId+"&orderNum="+orderNum+"&orderType="+orderType);
}
function getExitGroupReviewList(){
	window.open(contextPath + "/orderReview/manage/getExitGroupReviewList?reviewStatus=0&userLevel=1");
}



/**
 * 预报名订单对象
 * @author xiaoyang.tao
 * @date  2014-11-14
 */
var applyOrderInfo = {
	//
	_orderType: 2,
	/**
	 * 点击恢复取消预报名订单时触发
	 * @param orderId 订单ID
	 * @param obj 当前按钮对象
	 * @param orderType 预报名订单类型 已报名：0 转正：1 已取消：2
	 */
	changeOrderType: function(orderId, obj, orderType){
		if(orderType == this._orderType){
			$.jBox.confirm("取消预报名订单，您确定吗？","提示",function(v, h, f){
				 if(v == "ok"){
					 applyOrderInfo.ajax.changeOrderType(orderId, obj, orderType);
				 }
			});
		}else{
			$.jBox.confirm("恢复预报名订单，您确定吗？","提示",function(v, h, f){
				 if(v == "ok"){
					 applyOrderInfo.ajax.changeOrderType(orderId, obj, orderType);
				 }
			});
		}
	},
	changeToOrder: function(groupId, orderId, freePosition, orderPersonNum){
		if(orderPersonNum > freePosition){
			top.$.jBox.tip('预订数大于剩余数，不能预订','error');
		}else{
			window.location.href = contextPath + "/orderCommon/manage/applyshowforModify?preproductorderById=" + orderId;
		}
	},
	ajax: {
		changeOrderType:function(orderId, obj, orderType){
			var url_param = "orderId=" + orderId + "&orderType=" + orderType;
			var btnObj = $(obj);
			$.ajax({
				type : "POST",
				url : contextPath + "/applyOrderCommon/manage/changeOrderType?" + url_param,
				success : function(msg){
					if(msg.sucess){
						var btnObj = $(obj);
						//获取按钮的父对象td
						var btnObjPar = btnObj.parent();
						if(orderType == applyOrderInfo._orderType){
							$("input[name=positive]",btnObjPar).hide();
							$("input[name=recover]",btnObjPar).show();
							btnObjPar.prev().text("已取消");
						}else{
							$("input[name=positive]",btnObjPar).show();
							$("input[name=cancle]",btnObjPar).show();
							btnObjPar.prev().text("已报名");
						}
						btnObj.hide();
						jBox.tip("操作成功", 'info');
					}else{
						jBox.tip(msg.error, 'error');
					}
				}
			});
		},
		getFreePosition:function(groupId){
			$.ajax({
				type : "POST",
				url : contextPath + "/applyOrderCommon/manage/getFreePosition?groupId=" + groupId,
				success : function(msg){
				}
			});
		}
	}
};
var rebatesInfo = {
	rebatesOrder: function(orderId,orderType,uuid){
		window.open(contextPath + "/order/rebates/showIslandRebatesList?orderId=" + orderId + "&orderType=" + orderType+"&orderUuid="+uuid);
	}
};

/**
 * 订单锁定
 * @param uuid 订单UUID
 */
function lockOrder(uuid){
   doOrderLockStatus(uuid, "lockOrder", "锁定成功");
}

/**
 * 订单解锁
 * @param uuid 订单UUID
 */
function unLockOrder(uuid){
   doOrderLockStatus(uuid, "unLockOrder", "解锁成功");
}

/**
 * 订单锁定解锁调用的方法
 * @param uuid 订单UUID
 * @param actionName 调用后台的方法名
 * @param tipMsg 成功后提示值
 */
function doOrderLockStatus(uuid, actionName, tipMsg){
	$.ajax({
		type: "POST",
        url: contextPath + "/islandOrder/" + actionName,
        data: {
            "uuid" : uuid
        },
        success: function(msg) {
           if (msg) {
	           if (msg.success) {
	                top.$.jBox.tip(tipMsg,'warning');
	                $("#btn_search").click();
	            } else {
	                top.$.jBox.tip(msg.error,'warning');
	            }
           }
        }
    });
}

/**
 * 上传订单文件
 * @param orderUuid 订单UUID
 * @param obj
 * @returns
 */
function uploadFiles(orderUuid, obj) {
	var fls = flashChecker();
	var s="";
	if(!fls.f) {
		alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
		return;
	}
	
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find(".uploadPath").length == 0)
		$(obj).parent().append('<div class="uploadPath" style="display: none" id="uploadPathDiv"></div>');
	
	$(obj).addClass("clickBtn");
	
	$.jBox("iframe:"+ contextPath +"/MulUploadFile/uploadFilesPage?isSimple=false", {
	    title: "文件上传",
		width: 340,
   		height: 365,
   		buttons: {'完成上传':true},
   		persistent:true,
   		loaded: function (h) {},
   		submit: function (v, h, f) {
			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
			var fileIDList = "";
			if($(obj).parent().find("[name='docID']").length != 0) {
				$(obj).parent().find("[name='docID']").each(function(index, obj) {
					fileIDList += $(obj).val() + ",";
				});
			}
			//上传成功后绑定订单
			if (fileIDList != "") {
				setOrderFiles(orderUuid, fileIDList);
			}
			$("#uploadPathDiv").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			fileNameList = "";
   		}
	});
	$(".jbox-close").hide();
}

/**
 * 把订单关联文件
 * @param orderUuid 订单UUID
 * @param fileIDList 文件ids
 */
function setOrderFiles(orderUuid, fileIds) {
	$.ajax({
        type: "POST",
        url: contextPath + "/islandOrder/setOrderFiles",
        data: {
        	orderUuid : orderUuid,
        	fileIds : fileIds
        },
        success: function(msg) {
        	//上传成功
        }
	});
}

/**
 * 下载订单资料
 */
function downloadFiles(orderUuid) {
	$.ajax({
        type: "POST",
        url: contextPath + "/islandOrder/getFilesInfo?dom=" + Math.random(),
        data: {
        	orderUuid : orderUuid
        },
        success: function(msg) {
    		var htmls = "<table class='activitylist_bodyer_table t-type-jbox'><thead><tr><th>文件名称</th><th>下载链接</th></tr></thead><tbody>";
        	$.each(msg, function(key,value){
        		var docName = value.docName;
        		var orderId = value.payOrderId;
        		var id = value.id;
        		htmls = htmls+"<tr><td>" + docName + "</td><td><a class='downloadzfpz' lang=" + id + ">文件下载</a></td></tr>";
        	});
        	htmls = htmls+"</tbody></table>";
        	$.jBox.open(htmls,"文件列表",600,240);
        }
     });
}

/**
 * 下载团期下订单资料
 * @param groupUuid
 * @returns {Boolean}
 */
function downloadGroupFiles(groupUuid) {
	var orderUuids = $("#group_" + groupUuid).val();
	if(!orderUuids || orderUuids == "") {
		alert("没有要下载的游客资料");
		return false;
	} else {
		$.ajax({
	        type: "POST",
	        url: contextPath + "/islandOrder/downloadGroupFiles?dom=" + Math.random(),
	        data: {
	        	orderUuids : orderUuids
	        },
	        success: function(msg) {
	        	if (msg == "") {
	        		alert("没有要下载的游客资料");
	        	} else {
	        		window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/zipdownload/" + msg + "/订单资料")));
	        	}
	        }
	     });
	}
}