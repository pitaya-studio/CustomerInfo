//游客列表信息
function getTravelerList(obj, ctx, busynessType) {
	if("false" == $(obj).attr("openFlag")) {
		var parents = $(obj).parents("tr");
		var batchNo = parents.find("td.batchNoTD").text();
		if(parents.next().attr("class") != 'activity_team_top1') {
			$.ajax({
				type:"POST",
				url:ctx+"/visa/workflow/borrowmoney/getTravelerList",
				data:{
					batchNo : batchNo,
					busynessType : busynessType
				},
				success:function(travelerList){
					//table thead部分
					if(null != travelerList) {
						var table = $('<tr class="activity_team_top1"></tr>')
						.append('<td class="team_top" colspan="10">'
								+ '<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">'
								+ '<thead><tr>'
								+ '<th class="tc" width="6%">序号</th>'
								+ '<th class="tc" width="10%">姓名</th>'
								+ '<th class="tc" width="10%">销售</th>'
								+ '<th class="tc" width="10%">团号</th>'
								+ '<th class="tc" width="7%">订单号</th>'
								+ '<th class="tc" width="7%">签证类型</th>'
								+ '<th class="tc" width="7%">签证国家</th>'
								+ '<th class="tc" width="7%">' + ('1'==busynessType?'还收据':'借款') + '金额</th>'
								+ '<th class="tc" width="8%">' + ('1'==busynessType?'还收据':'借款') + '原因</th>'
								+ '<th class="tc" width="8%">操作</th>'
								+ '</tr></thead></table></td>');
						//tbody部分
						for(var i = 0; i<travelerList.length; i++) {
							addHtmlTr(travelerList[i], ctx, table, busynessType);
						}
						parents.after(table);
					}
				}
			});
		}else{
			parents.next("tr.activity_team_top1").show();
		}
		$(obj).attr("openFlag", "true");
	}else{
		$(obj).parents("tr").next("tr").hide();
		$(obj).attr("openFlag", "false");
	}
}

function addHtmlTr(traveler, ctx, table, busynessType) {
	var href = "";
	if("1" == busynessType) {
		href += ctx + '/visa/workflow/returnreceipt/visaReturnReceiptReviewDetail?'
		+ 'orderId=' + traveler.orderId
		+ '&travelerId=' + traveler.tid
		+ '&flag=1'
		+ '&revid=' + traveler.reviewId
		+ '&flowType=4';
	}else if("2" == busynessType) {
		href += ctx + '/visa/workflow/borrowmoney/visaBorrowMoneyReviewDetail?'
		+ 'orderId=' + traveler.orderId
		+ '&travelerId=' + traveler.tid
		+ '&flag=1'
		+ '&revid=' + traveler.reviewId
		+ '&flowType=5';
	}
	
	var tbody;
	if("1" == busynessType) {//还收据
	  tbody=$('<tbody></tbody>')
			.append('<tr>'
				+ '<td class="tc">' + traveler.num + '</td>'
				+ '<td class="tc">' + traveler.tname + '</td>'
				+ '<td class="tc">' + traveler.orderCreateBy + '</td>'
				+ '<td class="tc">' + traveler.groupCode + '</td>'
				+ '<td class="tc">' + traveler.orderNo + '</td>'
				+ '<td class="tc">' + traveler.visaType + '</td>'
				+ '<td class="tc">' + traveler.visaCountry + '</td>'
				+ '<td class="tc">' + traveler.receiptCurrency + '&nbsp;' + traveler.receiptAmount + '</td>'
				+ '<td class="tc">' + traveler.returnReceiptRemark + '</td>'
				+ '<td class="tc"><a onclick="" href="' + href + '">查看</a></td>'
				+ '</tr>');
	}else if("2" == busynessType) {//借款
		 tbody=$('<tbody></tbody>')
			.append('<tr>'
				+ '<td class="tc">' + traveler.num + '</td>'
				+ '<td class="tc">' + traveler.tname + '</td>'
				+ '<td class="tc">' + traveler.orderCreateBy + '</td>'
				+ '<td class="tc">' + traveler.groupCode + '</td>'
				+ '<td class="tc">' + traveler.orderNo + '</td>'
				+ '<td class="tc">' + traveler.visaType + '</td>'
				+ '<td class="tc">' + traveler.visaCountry + '</td>'
				+ '<td class="tc">' + traveler.borrowCurrency + '&nbsp;' + traveler.borrowAmount + '</td>'
				+ '<td class="tc">' + traveler.borrowRemark + '</td>'
				+ '<td class="tc"><a onclick="" href="' + href + '">查看</a></td>'
				+ '</tr>');
	}
	
	table.find("table").append(tbody);
}