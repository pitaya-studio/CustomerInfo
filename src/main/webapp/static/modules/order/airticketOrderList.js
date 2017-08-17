var contextPath;//程序路径
function selectQuery(){
	$("#saler").comboboxInquiry();
	$("#picker").comboboxInquiry();
	$("#agentId").comboboxInquiry();
}
$(function () {
	//搜索是否展开
	ifLaunch();
	contextPath = $("#ctx").val();
	selectQuery();
	//操作浮框
	operateHandler();
	//搜索聚焦失焦
	inputTips();
	//团号和产品切换
	switchNumAndPro();
	//收款确认提醒
	$(".notice_price").hover(function () {
		$(this).find("span").show();
	}, function () {
		$(this).find("span").hide();
	});

	//销售与下单人相互切换
	$("#contentTable").delegate(".salerId", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.createBy_cen').removeClass('onshow');
		$('.salerId_cen').addClass('onshow');
	});

	$("#contentTable").delegate(".createBy", "click", function () {
		$(this).addClass("on").siblings().removeClass('on');
		$('.salerId_cen').removeClass('onshow');
		$('.createBy_cen').addClass('onshow');

	});
	
	//总额与余额相互切换
    $("#contentTable").delegate(".total","click",function(){
        $(this).addClass("on").siblings().removeClass('on');
        $('.remainder_cen').removeClass('onshow');
        $('.total_cen').addClass('onshow');
    });
    
    $("#contentTable").delegate(".remainder","click",function(){
         $(this).addClass("on").siblings().removeClass('on');
         $('.total_cen').removeClass('onshow');
         $('.remainder_cen').addClass('onshow');
        
    });
	
	//订单列表和团期列表初始化：隐藏订单那或团期，判断显示那个标签
	var orderOrGroup = $("#orderOrGroup").val();
	if(orderOrGroup == "order") {
		$("#orderLabel").addClass("select");
	} else if(orderOrGroup == "group") {
		$("#groupLabel").addClass("select");
		if ($("#isNeedNoticeOrder").val() == 1) {
			//团期没查看订单
			$(".toptr").each(function (index, obj) {
				var notSeenOrderNum = $(this).next().find("[name=seenFlag][value=0]").length;
				if (notSeenOrderNum && notSeenOrderNum != 0) {
					var html = '<ul class="mainMenu-ul_new"><li><em><span>' + notSeenOrderNum + '</span><p></p></em></li></ul>';
					$("td:eq(0)", this).prepend(html);
				}
			});
		}
	}

	//渠道联系人增加"..."
	sliceAgentContacts();
	
	//初始化排序
	var _$orderBy = $("#orderBy").val();
	if(!_$orderBy || _$orderBy==""){
	    _$orderBy="id DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
	    if ($(this).hasClass("li"+orderBy[0])){
	        orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
	        $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
	        $(this).attr("class","activitylist_paixu_moren");
	    }
	});
	
	$(".transGroup").hover(function(event){
		$(this).next().show();
	}, function(event){
		$(this).next().hide();
	});
});

/**
 * 订单或团期列表转换
 * 
 */
function orderOrGroupList(type) {
	$("#orderBy").val("");
	if(type == "order") {
		$("#orderOrGroup").val("order");
	} else if(type == "group") {
		$("#orderOrGroup").val("group");
	}
	$('#showType').val("");
	$("#searchForm").submit();
}

/**
 * 订单排序
 * @param sortBy 排序字段
 * @param obj 对象
 */
function sortby(sortBy,obj) {
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)) {
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)) {
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
 * 订单查询
 * @param orderStatus 订单状态
 * @param orderOrGroup 订单或者团期
 */
function getOrderList(orderStatus, orderOrGroup) {
	var queryType = $("#queryType").val();
	resetSearchParams();
	$("#queryType").val(queryType)
	$('#showType').val(orderStatus);
	$('#orderOrGroup').val(orderOrGroup);
	$('#searchForm').submit();
}

/**
 * 按部门查询订单
 * 
 * param departmentId
 */
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	var showType=$("#showType").val();
	$('#showType').val("");
	$("#searchForm").submit();
}

/**
 * 是否要展开搜索
 * 		如果有输入框有内容或下拉框有值则展开
 */
function ifLaunch() {
	//展开、收起的单击事件
	$('.zksx').click(function(){
		if ($('.ydxbd').is(":hidden") == true) {
			$('.ydxbd').show();
			$(this).text('筛选');
			$(this).addClass('zksx-on');
		} else {
			$('.ydxbd').hide();
			$(this).text('筛选');
			$(this).removeClass('zksx-on');
		}
	});

	//如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='orderNumOrOrderGroupCode']");
	var searchFormselect = $("#searchForm").find("select[id!='showType']");
	var inputRequest = false;
	var selectRequest = false;
	for (var i = 0; i<searchFormInput.length; i++) {
		if ($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for (var i = 0; i<searchFormselect.length; i++) {
		if ($(searchFormselect[i]).children("option:selected").val() != "" &&
				$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if ($("#showType").length > 0 && $("#showType").children("option:selected").val() != "0") {
		selectRequest = true;
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}
}

/**
 * 修改支付凭证
 *
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId) {
	window.open(contextPath + "/order/manage/modifypayAirticketVoucher/" + payId + "/" + orderId);
}

// 判定不为空值
function isNotEmpty(str) {
	if (str != "") {
		return true;
	}
	return false;
}

//展开子table
function expand(child, obj) {
	if ($(child).css("display") == "none") {
		$(obj).html("收起").parent("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
		if ($("#isNeedNoticeOrder").val() == 1) {
			changeSeenFlag(child);
		}
	} else {
		$(child).hide();
		$(obj).parents("td").removeClass("td-extend");
		$(obj).html("展开").parent("tr").removeClass("tr-hover");
	}
}

//展开子table 支付记录专用
function expandPayRecord(child, obj) {
	if ($(child).css("display") == "none") {
		$(obj).parents("tr").addClass("tr-hover"); //html("收起").
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	} else {
		$(child).hide();
		$(obj).parents("td").removeClass("td-extend");
		$(obj).parents("tr").removeClass("tr-hover"); //html("展开").
	}
}

//分页
function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

//查询条件重置
function resetSearchParams() {
	$(':input', '#searchForm')
	.not(':button, :submit, :reset, :hidden')
	.val('')
	.removeAttr('checked')
	.removeAttr('selected');
	$("#targetAreaId").val('');
	$('#saler').val('');
	$('#picker').val('');
	$("#agentId").val('');
}

//排序
function sort(element, sortNum) {
	$("#sortNum").val(sortNum);
	var _this = $(element);

	//按钮高亮
	_this.parent("li").attr("class", "activitylist_paixu_moren");
	//原先高亮的同级元素置灰
	_this.parent("li").siblings(".activitylist_paixu_moren").attr("class", "activitylist_paixu_left_biankuang");

	//高亮按钮隐藏input赋值
	_this.next().val("activitylist_paixu_moren");

	//原先高亮按钮隐藏input值清空
	_this.parent("li").siblings(".activitylist_paixu_moren").children("a").next().val("");

	var sortFlag = _this.children().attr("class");
	//降序
	if (sortFlag == undefined || sortFlag == "icon icon-arrow-up") {

		//改变箭头的方向
		_this.children().attr("class", "icon icon-arrow-down");

		//降序
		_this.prev().val("desc");
	}
	//降序
	else if (sortFlag == "icon icon-arrow-down") {
		//改变箭头方向
		_this.children().attr("class", "icon icon-arrow-up");

		//shengx
		_this.prev().val("asc");
	}

	$("#searchForm").submit();

	return false;
}

function clickChoose() {
	if ($("#showChoose").html() == "展开筛选") {
		$("#showChooseSelect").val(1);
	} else {
		$("#showChooseSelect").val(0);
	}
}

/**
 * 上传出票确认单(C358 yang.jiang 2016-1-7 14:13:15)
 * @param orderId 订单id
 */
function uploadTicket(orderId) {
	/*window.open(contextPath + "/order/manage/toUploadConfirmation?orderId=" + orderId + "");*/
}
/**
 * 下载出票确认单
 * @param docid 文件id
 */
function downloads(docid) {
	window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/download/" + docid)));
}

/**
 *  机票订单页面查看订单发票信息列表
 * param orderNum   订单号
 * author     ruyi.chen
 * createDate 2014-12-04
 */
function showInvoiceInfo(orderId, orderNum, activityId) {
	window.open(contextPath + "/orderInvoice/manage/getInvoiceListByOrderNum?orderNum=" + orderNum + "&orderId=" + orderId + "&activityId=" + activityId  + "&orderType=7");
}

function showReceptInfo(orderId, orderNum, activityId) {
	window.open(contextPath + "/orderInvoice/manage/supplyreceiptlist?orderNum=" + orderNum + "&orderId=" + orderId + "&activityId=" + activityId  + "&orderType=7");
}

//下载团期下所有订单的确认单
function downloadData(activityId, downloadType) {
	if("confirmation" == downloadType) {
		var attachmentIds = "";
		var attachmentIdList = new Array();
		//通过团期id查找所有的确认单id
		$.ajax({
	        type : "POST",
	        url: contextPath + "/order/manage/findAttachmentIdsByGroupId",
	        dataType : "json",
	        cache : false,
	        data : {activityId : activityId},
	        success : function(result) {
	        	var data = eval(result);	        	
	        	if(!data){
	        		top.$.jBox.tip("没有确认单");
	        	}else{
	        		for(i in data){
	        			attachmentIdList.push(data[i]);
	        		}
	        		if(attachmentIdList != null && attachmentIdList.length > 0){			
	        			attachmentIds += attachmentIdList[0];
	        			if(attachmentIdList.length > 1){				
	        				for(var i = 1; i < attachmentIdList.length; i++){
	        					attachmentIds += "," + attachmentIdList[i]
	        				}
	        			}
	        		}
	        		var fileName = activityId + "确认单";
	        		window.open(encodeURI(encodeURI(contextPath + "/sys/docinfo/zipdownload/" + attachmentIds + "/" + fileName)));
	        	}

			}
		});
		
	} 
}

/**
 * 取消订单
 *
 * param orderId
 */
function cancelOrder(orderId) {
	$.ajax({
        type: "POST",
        url:  contextPath +"/airticketOrderList/manage/canCancelOrDelOrder",
        data: {
            orderId:orderId,
            type : 1
        },
        success: function(result){
            if(result == '0'){
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
                url: contextPath+"/order/manage/cancelOrder",
                data: {
                    orderId:orderId,
                    description : f.description
                },
                success: function(msg){
                	if(msg == 'fail') {
                    	top.$.jBox.tip('订单不能取消','warning');
                        top.$('.jbox-body .jbox-icon').css('top','55px');
                    } else if(msg == 'ok') {
                       top.$.jBox.tip('订单已取消','warning');
                       top.$('.jbox-body .jbox-icon').css('top','55px');
                    	  $("#searchForm").submit();
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
            }else{
            	top.$.jBox.tip(result,'warning');
            }
        }
     });
    
}

/**
 * 撤销占位
 * 
 * param orderId
 */
function revokeOrder(orderId) {
	$.jBox.confirm("确定撤销吗？", "系统提示", function(v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type: "POST",
				url: contextPath + "/order/manage/revokeOrder",
				data: {
					orderId:orderId
				},
				success: function(msg){
					if(msg == 'fail') {
						top.$.jBox.tip('订单不能撤销占位','warning');
						top.$('.jbox-body .jbox-icon').css('top','55px');
					} else if(msg == 'ok') {
						$("#btn_search").click();
					} else {
						top.$.jBox.tip(msg,'warning');
						top.$('.jbox-body .jbox-icon').css('top','55px');
					}
				}
			});
		}  else if (v == 'cancel') {
                
		}
	});
}

/**
 * 订单删除
 *
 * param orderId
 */
function deleteOrderByFlag(orderId) {
	
	var flag = false;
	$.ajax({
        type : "POST",
        url : contextPath + "/airticketOrderList/manage/canCancelOrDelOrder",
        async : false,
        data : {
            orderId : orderId,
            type : 2
        },
        success: function(result){
        	if (result == "0") {
        		flag = true;
        	} else {
        		top.$.jBox.tip(result,'warning');
        	}
        }
	}); 
	if (!flag) {
		return false;
	}
	$.jBox.confirm("确定要删除数据吗？", "系统提示", function (v, h, f) {
		if (v == 'ok') {
			$.ajax({
				type : "POST",
				url : contextPath + "/order/manage/deleteOrderByFlag",
				data : {
					orderId : orderId
				},
				success : function (msg) {
					if(msg == 'ok'){
						top.$.jBox.tip('删除成功', 'warning');
						$("#btn_search").click();
					}else{
						top.$.jBox.tip(msg, 'warning');
					}
					
				}
			});
		} else if (v == 'cancel') {}
	});
}

/**
 * 订单激活
 *
 * param orderId
 */
function invokeOrder(orderId) {
	$.ajax({
		type : "POST",
		url : contextPath + "/order/manage/invokeOrder",
		data : {
			orderId : orderId
		},
		success : function (msg) {
			if (msg == 'fail') {
				top.$.jBox.tip('激活失败', 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			} else if (msg == 'success') {
				top.$.jBox.tip('激活成功', 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
				$("#btn_search").click();
			} else {
				top.$.jBox.tip(msg, 'warning');
				top.$('.jbox-body .jbox-icon').css('top', '55px');
			}
		}
	});
}

/**
 * 查看订单是否可支付
 * @param orderId
 * @param payPriceType
 * @returns {Boolean}
 */
function orderPay(payPriceType, orderId, orderNum, orderType, orderCompany, totalMoney, orderDetailUrl, mainId) {
	if (mainId != "" && payPriceType == "2") {
		top.$.jBox.tip('参团机票订单无需付款', 'warning');
		top.$('.jbox-body .jbox-icon').css('top', '55px');
		return false;
	} else {
		$.ajax({
			type : "POST",
			async : false,
			url : contextPath + "/order/manage/whetherCanPay",
			dataType : "json",
			data : {
				orderId : orderId,
				payPriceType : payPriceType
			},
			success : function (result) {
				var data = eval(result);
				if (data && data[0].flag == "true") {

					var param = "orderId=" + orderId
						 + "&orderNum=" + orderNum
						 + "&payPriceType=" + data[0].payPriceType
						 + "&orderType=" + orderType
						 + "&businessType=1"
						 + "&isCommonOrder=airticket"
						 + "&agentId=" + orderCompany
						 + "&paramCurrencyId=" + data[0].moneyCurrencyId
						 + "&paramCurrencyPrice=" + data[0].moneyCurrencyPrice
						 + "&paramTotalCurrencyId=" + data[0].totalMoneyCurrencyId
						 + "&paramTotalCurrencyPrice=" + data[0].totalMoneyCurrencyPrice
						 + "&orderDetailUrl=" + decodeURIComponent(orderDetailUrl);

					window.open(contextPath + "/orderPay/pay?" + param);
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
	}
}

/**
 * 支付记录
 *
 * param orderId
 * param obj
 */
function showOrderPay(orderId, obj) {
	var sbrtr = $(obj).parents("tr").next();
    var sbrtd = sbrtr.children().eq(0);
    var table = sbrtr.find("table[id=table_orderPay]");
	if (table.length <= 0) {
		$.ajax({
			type : "POST",
			url : contextPath + "/orderCommon/manage/getPayList",
			data : {
				orderId : orderId
			},
			success : function (msg) {
				var $table = $("<table class=\"table activitylist_bodyer_table\" style=\"margin:0 auto;\"></table>").append("<thead style=\"background:#62AFE7\"><tr><th>付款方式</th><th>金额</th><th>日期</th><th>支付款类型</th><th>是否已确认达账</th><th>支付凭证</th><th>操作</th></tr></thead>");

				$.each(msg.orderList, function (key, value) {
					var payTypeName = value.payTypeName;
					var payPriceType = value.payPriceType;
					var isAsAccount = value.isAsAccount;

					if (payTypeName == null || payTypeName == undefined) {
						payTypeName = "";
					}
					if (payPriceType == "1") {
						payPriceType = "支付全款";
					} else if (payPriceType == "2") {
						payPriceType = "交订金";
					}
					if (payPriceType == "3") {
						payPriceType = "支付尾款";
					}

					if (isAsAccount == "0" || isAsAccount == "null" || isAsAccount == undefined) {
						isAsAccount = "否";
					} else if (isAsAccount == "1") {
						isAsAccount = "是";
					}

					var payvoucher = "<td><a class=\"downloadzfpz\" lang=\"" + value.payVoucher + "\">支付凭证ee</a></td>";

					if (payvoucher == "" || payvoucher == undefined || payvoucher == null) {
						payvoucher = "<td>暂无支付凭证</td>";
					}

					var paypricechange = "<td><a class=\"changepayPrice\" lang=\"" + value.payVoucher + "\"" + "payprice=\"" + value.payPrice + "\">修改</a></td>"
						$table.append($("<tr></tr>")
							.append("<td>" + payTypeName + "</td>")
							.append("<td>${fns:getMoneyAmountBySerialNum(" + value.moneySerialNum + ",2)}</td>")
							.append("<td>" + value.createDate + "</td>")
							.append("<td>" + payPriceType + "</td>")
							.append("<td>" + isAsAccount + "</td>")
							.append(payvoucher)
							.append(paypricechange))
				});
				sbrtd.append($table);
			}
		});
	}
	if ($(obj).hasClass("jtk")) {
		var td = $(obj).closest("td");
		if (sbrtr.is(":hidden")) {
			sbrtr.show();
			td.addClass("td-extend");
		} else {
			sbrtr.hide();
			td.removeClass("td-extend");
		}
	}
}

//申请发票
function applyInvoice(orderNum, orderId, orderType) {
	$.ajax({
		type : "post",
		url : contextPath + "/orderInvoice/manage/validateOrder",
		data : {
			orderNum : orderNum,
			orderId : orderId,
			orderType : orderType
		},
		success : function (msg) {
			debugger;
			if ("success" == msg.msg)
				window.open(contextPath + "/orderInvoice/manage/applyInvoice?orderNum=" + orderNum + "&orderId=" + orderId + "&orderType=" + orderType);
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开发票！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！', 'success');
		}
	});

}

// 申请收据
function applyReceipt(orderNum, orderId, orderType) {
	$.ajax({
		type : "post",
		url : contextPath + "/orderInvoice/manage/validateOrder",
		data : {
			orderNum : orderNum,
			orderId : orderId,
			orderType : orderType
		},
		success : function (msg) {
			if ("success" == msg.msg)
				window.open(contextPath + "/orderReceipt/manage/applyReceipt?orderNum=" + orderNum + "&orderId=" + orderId + "&orderType=" + orderType);
			else if("canOrDel"==msg.msg)
				top.$.jBox.tip('已取消或已删除订单不能开收据！','success');
			else
				top.$.jBox.tip('申请失败，订单创建人和申请人不一致！', 'success');
		}
	});

}

/**
 * 修改支付凭证
 *
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId) {
	window.open(contextPath + "/order/manage/modifypayVoucher/" + payId + "/" + orderId);
}

/* 展示支付凭证 */
$(document).delegate(".showpayVoucher", "click", function () {
	var orderIDValue = $(this).attr("lang");
	$.ajax({
		type : "POST",
		url : contextPath + "/sys/docinfo/payVoucherList/" + orderIDValue,
		data : {
			orderId : orderIDValue
		},
		success : function (msg) {
			var htmls = "<table class='activitylist_bodyer_table t-type-jbox'><thead><tr><th>凭证所属ID</th><th>凭证名称</th><th>下载链接</th></tr></thead><tbody>";
			$.each(msg, function (key, value) {
				var docName = value.docName;
				var orderId = value.payOrderId;
				var id = value.id;
				htmls = htmls + "<tr><td>" + orderId + "</td><td>" + docName + "</td><td><a class='downloadzfpz' lang=" + id + ">收款凭证</a></td></tr>";
			});
			htmls = htmls + "</tbody></table>";
			$.jBox.open(htmls, "凭证列表", 600, 240);
		}
	});
});

$(document).delegate(".downloadzfpz", "click", function () {
	window.open(contextPath + "/sys/docinfo/download/" + $(this).attr("lang"));
});

function writableMenuEvaluation(obj) {
	var menuVal = $(obj).find('option:selected').text();
	$(obj).parent().next().children().val(menuVal);
}

/**
 * 导出游客资料或出票名单
 * @param activityId 机票产品ID
 * @param type 1 游客资料 2 出票名单
 */
function exportExcel(activityId, type) {
	$.ajax({
        type : "POST",
        url: contextPath + "/order/manage/existExportData",
        dataType : "json",
        cache : false,
        data : {activityId : activityId},
        success : function(result) {
        	var data = eval(result);
        	if(data && data[0].flag == "true") {
        		$("#activityId").val(activityId);
        		if (type == 1) {
        			$("#exportTravelesForm").attr("action", contextPath + "/order/manage/exportExcel");
        		} else {
        			$("#exportTravelesForm").attr("action", contextPath + "/order/manage/exportAirticketNameExcel");
        		}
				$("#exportTravelesForm").submit();
        	} else {
        		var tips = data[0].warning;
        		top.$.jBox.info(tips, "警告", { width: 250, showType:"slide", icon: "info",draggable: "true" });
				top.$('.jbox-body .jbox-icon').css('top','55px');
        	}
		}
	});
}

function lockOrder(orderId){
   doOrderLockStatus(orderId, "lockOrder", "锁定成功");
}


function unLockOrder(orderId){
   doOrderLockStatus(orderId, "unLockOrder", "解锁成功");
}

function doOrderLockStatus(orderId, actionName, tipMsg){
    $.ajax({
        type: "POST",
        url: contextPath + "/order/manage/" + actionName,
        data: {
            "orderId":orderId
        },
        success: function(msg){
           if(msg){
	           if(msg.success){
	                top.$.jBox.tip(tipMsg,'warning');
	                $("#btn_search").click();
	            }else{
	                top.$.jBox.tip(msg.error,'warning');
	            }
           }
            
        }
     });
}

/**
 * 确认占位
 *
 * param orderId
 */
function confirmOrder(orderId) {
	$.ajax({
		type: "POST",
		url: contextPath + "/order/manage/confirmOrder?dom=" + Math.random(),
		data: {
			orderId : orderId
		},
		success: function(msg) {
			if(msg == 'fail') {
				top.$.jBox.tip('占位失败','warning');
				top.$('.jbox-body .jbox-icon').css('top','55px');
			} else if(msg == 'success') {
				top.$.jBox.tip('占位成功','warning');
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
 * 修改没查看订单状态，改为已查看，并更新左边菜单栏没查看订单数字
 * @param child
 */
function changeSeenFlag(child) {
	var groupId = child.split("_")[1];
	var notSeenOrderIds = "";
	$("[name=" + groupId + "] :input[name=seenFlag][value=0]").each(function (index, obj) {
		notSeenOrderIds += "," + $(this).attr("id").split("_")[1];
	});
	if (notSeenOrderIds != "") {
		$.ajax({
			type : "POST",
			url : contextPath + "/order/manage/changeNotSeenOrderFlag?dom=" + Math.random(),
			cache : false,
			data : {
				notSeenOrderIds : notSeenOrderIds
			},
			success : function (data) {
				if (data.result = "success") {
					//去除团期团号旁边提醒
					$(child).prev().find("td:eq(0)").find("ul").remove();
					$(child).prev().find("td:eq(0)").find("div").remove();

					//更改没查看订单状态，改为已查看
					$("[name=" + groupId + "] :input[name=seenFlag][value=0]").each(function (index, obj) {
//						$(this).value("1");
						$(this).attr("value",1);
					});

					//更新左边菜单栏没查看订单数字
					var href = "airticketOrderList/manage/airticketOrderList";
					$("li[id^=childMenu]").each(function (index, obj) {
						if ($(this).html().indexOf(href) != -1) {
							var leftNum = $("span", this).text() - data.changeSum;
							if (leftNum == 0) {
								$("span", this).parent().remove();
								if ($("li[id^=childMenu] a em").length == 0) {
									$("h2:contains('订单')").children("span:last").remove();
								}
							} else {
								$("span", this).text(leftNum);
							}
						}
					});
				}
			}
		});
	}
}

var notSeenIds = "";
function changeSeenFlagByMouse(orderId, obj) {
	if ($("[name=seenFlag]", obj).val() == 0) {
		notSeenIds += "," + orderId;
	}
	changeOrderSeenFlag();
}

function changeOrderSeenFlag() {
	if (notSeenIds != "") {
		$.ajax({
			type :"POST",
			url : contextPath + "/order/manage/changeNotSeenOrderFlag?dom=" + Math.random(),
			cache : false,
			data : {notSeenOrderIds : notSeenIds},
			success:function(data) {
				if (data.result = "success") {
					//更改没查看订单状态，改为已查看
					var idArr = notSeenIds.split(",");
					for (var i=0;i<idArr.length;i++) {
						$("#order_" + idArr[i]).val("1");
						$("#order_" + idArr[i]).parent().find(".new-tips").remove();
					}
					
					//更新左边菜单栏没查看订单数字
					var href = "airticketOrderList/manage/airticketOrderList";
					$("li[id^=childMenu]").each(function(index, obj) {
						if ($(this).html().indexOf(href) != -1) {
							var leftNum = $("span", this).text() - data.changeSum;
							if (leftNum == 0) {
								$("span", this).parent().remove();
								if ($("li[id^=childMenu] a em").length == 0) {
									$("h2:contains('订单')").children("span:last").remove();
								}
							} else {
								$("span", this).text(leftNum);
							}
							notSeenIds = "";
						}
					});
				}
			}
		});
	}
}

/**
 * 已占位订单的 确认操作
 * @param orderId 订单id
 */
function seizedConfirm(orderId){
	$.jBox.confirm("是否确认该订单占位?", "系统提示", function (v, h, f) {
		if (v == 'ok') {
			// 到后台修改订单 相关状态seizedConfirmationStatus的值为1
			$.ajax({
				type : "POST",
				url : contextPath + "/airticketOrderList/manage/seizedConfirm",
				data : {
					"orderId" : orderId
				},
				success : function (returnBack) {
					if (returnBack) {
						if (returnBack.flag == "success") {
							top.$.jBox.tip("确认成功！", 'info');
							// 确认成功，重新发起搜索刷新页面
							$("#btn_search").click();
						} else {
							top.$.jBox.tip("确认失败！" + returnBack.message, 'warning');
							// TODO
						}
					}
				}
			});
		} else if (v == 'cancel') {}
	});
}

/**
 * 200 渠道联系人显示20个字符，其余的用"..."代替
 */
function sliceAgentContacts(){
	$("input[class=agentContactsName]").each(function(index) {
		var agentContactsName = $(this).val();
		$(this).siblings().attr("title", agentContactsName);
		if(agentContactsName!=null && agentContactsName!=undefined && agentContactsName.length>=20){
			agentContactsName = agentContactsName.substring(0,20) + "...";
		}
		$(this).siblings().html(agentContactsName);
	});
}

/*  批量下载机票类确认单
 * @param checkName checkbox标签的name属性值，用于获取checkbox标签
 * add by xianglei.dong 2016-03-23
 * updated by xianglei.dong 2016-03-30
 */
function airticket_orderBatchDownload(checkName){
	var check = document.getElementsByName(checkName);
	var objarray = check.length;
	var orderIds = '';
	var docIds = '';
	//记录选中的个数
	var checkedCount = 0;
	for (var i=0; i<objarray; i++) {
	  if(check[i].checked == true) {
		  checkedCount++;
		  var order_doc_id = check[i].value.split('@');
		  if(order_doc_id[1]=='' || order_doc_id[1]==null || order_doc_id[1]==undefined) {
			  continue;
		  }
		  orderIds = orderIds + ',' + order_doc_id[0];
		  docIds = docIds + ',' + order_doc_id[1];
	  }
	}
	
	if(checkedCount ==0){
		top.$.jBox.tip('请选择订单');
        return false;
	}else if(docIds == '') {
		top.$.jBox.tip('选择的订单中没有可下载的确认单，请先上传确认单');
		return false;
	}else{
		docIds = docIds.substr(1, docIds.length);
		window.open(encodeURI(encodeURI(contextPath+"/order/manage/zipconfirmdownload/" + docIds )));
		location.reload(true);
	}		
}

/* 机票类订单列表全选
 * @param obj 全选checkbox标签的对象
 * @param groupid  团号id，在团号列表中用于表示同一团号下的checkbox标签
 * add by xianglei.dong
 * 2016-03-23
 */
function airticket_orderAllChecked(obj, groupid) {
	if(groupid==null || groupid == undefined || groupid == '') {    //订单列表
		if (obj.checked) {
			$("input[name='airticketOrderId']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='airticket_orderAllChk']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='airticketOrderId']:checked").each(function() {
				this.checked = false;
			});
			$("input[name='airticket_orderAllChk']:checked").each(function() {
				this.checked = false;
			});
		}
	}else {    //团号列表
		if (obj.checked) {
			$("input[name='airticketOrderId_"+groupid+"']").not("input:checked").each(function() {
				this.checked = true;
			});
			$("input[name='airticket_orderAllChk_"+groupid+"']").not("input:checked").each(function() {
				this.checked = true;
			});
		} else {
			$("input[name='airticketOrderId_"+groupid+"']").each(function() {
				this.checked = false;
			});
			$("input[name='airticket_orderAllChk_"+groupid+"']:checked").each(function() {
				this.checked = false;
			});
		}
	}
	
}

/* 机票类订单列表反选
 * @param obj 全选checkbox标签的对象
 * @param groupid  团号id，在团号列表中用于表示同一团号下的checkbox标签
 * add by xianglei.dong
 * 2016-03-23
 */
function airticket_orderAllNoCheck(groupid) {
	if(groupid==null || groupid == undefined || groupid == '') {   //订单列表
		$("input[name='airticketOrderId']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		}); 
	}else {  //团号列表
		$("input[name='airticketOrderId_"+groupid+"']").each(function() {
			$(this).attr("checked", !$(this).attr("checked"));
		});
	}
	pr_allchk(groupid);
}

function pr_allchk(groupid) {
	var chk = 0;
	if(groupid==null || groupid == undefined || groupid == '') {       //订单列表
		var chknum = $("input[name='airticketOrderId']").size();		
		$("input[name='airticketOrderId']").each(function() {
			if ($(this).attr("checked") == 'checked') {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='airticket_orderAllChk']").attr("checked", true);
		} else {//不全选 
			$("input[name='airticket_orderAllChk']").attr("checked", false);
		}
	}else {   //团号列表
		var chknum = $("input[name='airticketOrderId_"+groupid+"']").size();		
		$("input[name='airticketOrderId_"+groupid+"']").each(function() {
			if ($(this).attr("checked") == 'checked') {
				chk++;
			}
		});
		if (chknum == chk) {//全选 
			$("input[name='airticket_orderAllChk_"+groupid+"']").attr("checked", true);
		} else {//不全选 
			$("input[name='airticket_orderAllChk_"+groupid+"']").attr("checked", false);
		}
	}	
}

/* 每行中的复选框
 * @param obj checkbox对象
 * @param groupid 组号
 * add by xianglei.dong
 * 2016-03-23
 */
function airticketOrderCheckchg(groupid) {
	if(groupid==null || groupid == undefined || groupid == '') {    //订单列表
		if ($("input[name='airticketOrderId']").not("input:checked").length) {
			$("input[name='airticket_orderAllChk']").attr("checked",false);
		}else{
			$("input[name='airticket_orderAllChk']").attr("checked",true);
		}
	}else {  //团号列表 
		var num = $("input[name='airticketOrderId_"+groupid+"']").not("input:checked").length;
		if ($("input[name='airticketOrderId_"+groupid+"']").not("input:checked").length) {
			$("input[name='airticket_orderAllChk_"+groupid+"']").attr("checked",false);
		}else{
			$("input[name='airticket_orderAllChk_"+groupid+"']").attr("checked",true);
		}
	}
}

contextPath = $("#ctx").val();
function exportExcelUserList(){
	var queryType = $("#queryType").val();
	$('#searchForm').attr("action", contextPath + "/airticketOrderList/manage/exportAirticketUserList/" + queryType);
	$("#searchForm").submit();
}

function submitSearch(){
	var queryType = $("#queryType").val();
	$('#searchForm').attr("action", contextPath + "/airticketOrderList/manage/airticketOrderList/" + queryType);
}

var $node;
function relationInvoiceList(orderId) {
	var $pop = $.jBox($("#relationInvoice").html(), {
    	title    : "发票列表", buttons: {'提交':1 ,'关闭':0}, submit: function (v, h, f) {
        	if (v == "0") {
        	}else if(v == "1"){
        		var checks = $("input[name='invoiceName']:checked");
        		var invoiceIds = "";
        		checks.each(function(i,element){
        			invoiceIds += $(element).val();
        			invoiceIds += ",";
        		})
        		invoiceIds = invoiceIds.slice(0,-1);
        		
        		$.ajax({
        			type: "POST",
	        		url: sysCtx + "/orderPay/changeInvoiceReceivedPayStatus",
	        		data: {
	        			invoiceIds : invoiceIds
	        		},
        			success: function(result){
        				if(result.flag == 'success'){
        					$.jBox.tip("发票关联成功！"); 
        				}
        			}
        		});
        	}
		},loaded:function(h, f){
			$node = h.find("#relationInvoiceTable");
			searchRelationInvoice(orderId);
		}, height: 350, width: 700}
	);
}

// ajax进入后台查询，返回json再解析
function searchRelationInvoice(orderId){
	$.ajax({
		type: "POST",
        url: sysCtx + "/orderPay/relationInvoiceList",
        data: {
        	orderId : orderId
        },
		success: function(result){
			var html = "";
			if(result == undefined || result == null){
				top.$.jBox.tip("获取关联发票记录失败！");
			}
			if(result == ''){
				$node.empty().append(html);
			}
			var json = eval(result);
			// json数组个数
			var jsonLength = json.length;
			// 判断为空
			if (jsonLength && jsonLength != 0) {
				// 循环获取html组合
				for (var i = 0; i < jsonLength; i++) {
					// 序列值
					var indexVal = i + 1;
					html += "<tr>";
					// 选择
					html += "<td><input name='invoiceName' type='checkbox' value='" + json[i][0] + "'></td>";
					// 发票号
					html += "<td name='operatorName' class='tc'><span>" + json[i][1] + "</span></td>";
					// 团号
					html += "<td name='operation' class='tc'><span>" + json[i][2] + "</span></td>";
					// 申请人
					html += "<td name='operationTime' class='tc'><span>" + json[i][5] + "</span></td>";
					// 开票状态
					html += "<td name='mainContext' class='tc'><span>" + json[i][3] + "</span></td>";
					// 开票金额
					html += "<td name='mainContext' class='tr'><span>¥ " + json[i][4] + "</span></td>";
					html += "</tr>";
				}
				$node.empty().append(html);
			}
		}
	});
}
