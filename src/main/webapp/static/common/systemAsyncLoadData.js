var sysCtx;
$(window).load(function() {
	sysCtx = $("#sysCtx").val();
	var sysIsShow = $("#sysIsShow").val();
	if (sysIsShow == "1") {
		// 异步统计未查看订单数量（如果供应商配置不提醒则不执行此方法）
		loadAsynchronousOrderData();
		// 财务提醒
		loadCWData();
	}
})

function loadAsynchronousOrderData() {
	// 加载团期类订单未查看信息
	getDTNotSeenOrderNum();
	// 加载机票订单未查看信息
	getJPNotSeenOrderNum();
	// 查看签证订单未查看信息
	getQZNotSeenOrderNum();
	// 查看下单通知未查看信息
	getT1NotSeenOrderNum();
}

function addOrderNotice() {
	if ($("#menu_417").children("span.untreated_verify").length == 0) {
		$("#menu_417").children("span").after("<span class='untreated_verify'></span>");
	}
}

/**
 * 单团订单未查看统计
 */
function getDTNotSeenOrderNum() {
	$.ajax({
		type :"POST",
		url : sysCtx + "/orderStatistics/manage/getDTNotSeenOrderNum",
		cache : false,
		async : true,
		data : {},
		success:function(data) {
			var resultArr = data.split(";");
			for (var i=0;i<resultArr.length;i++) {
				var orderData = resultArr[i];
				if (orderData != "") {
					var orderTypeAndNum = orderData.split(",");
					var href = "orderList/manage/showOrderList/0/" + orderTypeAndNum[0];
					$("li[id^=childMenu]").each(function(index, obj) {
						if ($(this).html().indexOf(href) != -1 && data != 0) {
							addOrderNotice();
							$("a", this).append("<em><span>" + orderTypeAndNum[1] + "</span><p></p></em>");
							return false;
						}
					});
				}
			}
		}
	});
}

/**
 * 机票订单未查看统计
 */
function getJPNotSeenOrderNum() {
	$.ajax({
		type :"POST",
		url : sysCtx + "/orderStatistics/manage/getJPNotSeenOrderNum",
		cache : false,
		async : true,
		data : {},
		success:function(data) {
			// 如果是日信观光的用户，则销售机票订单也展示提醒数值
			var companyUuid = $("#sysCompanyUuid").val();
			var href = "airticketOrderList/manage/airticketOrderList/2";
			if (companyUuid == "58a27feeab3944378b266aff05b627d2") {
				href = "airticketOrderList/manage/airticketOrderList";
			}
			$("li[id^=childMenu]").each(function(index, obj) {
				if ($(this).html().indexOf(href) != -1 && data != 0) {
					addOrderNotice();
					$("a", this).append("<em><span>" + data + "</span><p></p></em>");
				}
			});
		}
	});
}

/**
 * 签证订单未查看统计
 */
function getQZNotSeenOrderNum() {
	$.ajax({
		type :"POST",
		url : sysCtx + "/orderStatistics/manage/getQZNotSeenOrderNum",
		cache : false,
		async : true,
		data : {},
		success:function(data) {
			// 如果是日信观光的用户，则销售机票订单也展示提醒数值
			var companyUuid = $("#sysCompanyUuid").val();
			var href = "visa/order/list";
			var qwOrderNum = data.split(";")[0]
			var xsOrderNum = data.split(";")[1]
			$("li[id^=childMenu]").each(function(index, obj) {
				if ($(this).html().indexOf(href) != -1 && qwOrderNum != 0) {
					addOrderNotice();
					$("a", this).append("<em><span>" + qwOrderNum + "</span><p></p></em>");
				}
			});
			if (companyUuid == "58a27feeab3944378b266aff05b627d2") {
				href = "visa/order/searchxs";
				$("li[id^=childMenu]").each(function(index, obj) {
					if ($(this).html().indexOf(href) != -1 && xsOrderNum != 0) {
						addOrderNotice();
						$("a", this).append("<em><span>" + xsOrderNum + "</span><p></p></em>");
					}
				});
			}
		}
	});
}

/**
 * 查看T1预报名订单没查看订单条数
 */
function getT1NotSeenOrderNum() {
	
	var href = "t1/preOrder/manage/showT2OrderList";
	$("li[id^=childMenu]").each(function(index, obj) {
		if ($(this).html().indexOf(href) != -1) {
			$.ajax({
				type :"POST",
				url : sysCtx + "/orderStatistics/manage/getT1NotSeenOrderNum",
				cache : false,
				async : true,
				data : {},
				success:function(data) {
					if (data != 0) {
						$("h2.mainMenu").each(function(i, o) {
							if ($(this).html().indexOf("下单通知") != -1) {
								if ($(o).children("span.untreated_verify").length == 0) {
									$(o).children("span").after("<span class='untreated_verify' style='margin-left: 28px;'></span>");
								}
							}
						});
						$("a", obj).append("<em><span>" + data + "</span><p></p></em>");
					}
				}
			})
		}
	});
}

function loadCWData() {
	$.ajax({
		type :"POST",
		url : sysCtx + "/orderStatistics/manage/getCWNotSeenNum",
		cache : false,
		async : true,
		data : {},
		success:function(data) {
			// 订单未收款确认的条数：1 订单收款 ,2 切位收款,3 签证押金收款，4 签证订单收款
			var countForOrderListDZ1 = data.countForOrderListDZ1;
			var countForOrderListDZ2 = data.countForOrderListDZ2;
			var countForOrderListDZ3 = data.countForOrderListDZ3;
			var countForOrderListDZ4 = data.countForOrderListDZ4;
			
			// 新增成本付款总数
			var orderPayCount = data.orderPayCount;
			// 新增退款付款总数
			var refundCount = data.refundCount;
			// 新增返佣付款总数
			var rebateCount = data.rebateCount;
			// 公版借款付款未付款的数目
			var borrowMoneyCount = data.borrowMoneyCount;
			// 新增代收服务费付款总数
			var serviceChargeCount = data.serviceChargeCount;
			// 其他收入收款未达账金额的数目
			var paidother = data.paidother;
			// 待处理的收据数量
			var receiptNumAll = data.receiptNumAll;
			// 待处理的发票数量
			var invoiceNumAll = data.invoiceNumAll;
			
			// 财务合计总数
			var all_count = orderPayCount + refundCount + rebateCount + countForOrderListDZ1 + countForOrderListDZ2 + countForOrderListDZ3 + 
							countForOrderListDZ4 + borrowMoneyCount + paidother + receiptNumAll + invoiceNumAll;
			// 结算总数
			var waitNum = orderPayCount + refundCount + rebateCount + countForOrderListDZ1 + countForOrderListDZ2 + countForOrderListDZ3 + countForOrderListDZ4 + 
						borrowMoneyCount + serviceChargeCount + paidother;
			
			// 财务
			if ($("#menu_90").length > 0 && all_count > 0) {
				$("#menu_90").append('<span class="untreated_verify_f"></span>');
			}
			// 结算
			if ($("#childMenu_202").length > 0 && waitNum > 0) {
				$("#childMenu_202 a").append('<em><span>' + waitNum + '</span><p></p></em>');
			}
			// 收据
			var href = "/receipt/limit/supplyreceiptlist/9";
			$("li[id^=childMenu]").each(function(index, obj) {
				if ($(this).html().indexOf(href) != -1 && receiptNumAll > 0) {
					$("a", this).append("<em><span>" + receiptNumAll + "</span><p></p></em>");
					flag = true;
				}
			});
			// 发票
			if ($("#childMenu_201").length > 0 && invoiceNumAll > 0) {
				$("#childMenu_201 a").append('<em><span>' + invoiceNumAll + '</span><p></p></em>');
			}
		}
	});
}