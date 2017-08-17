$(window).load(function() {
	//异步加载修改数据及审核数据
	loadAsynchronousData();
})
//加载异步数据
function loadAsynchronousData() {
	$.ajax({
		type :"POST",
		url : $("#ctx").val() + "/islandOrder/loadAsynchronousData",
		cache : false,
		data : {islandOrderId : $("#islandOrderId").val()},
		success:function(data) {
			//修改记录
			var modidyData = data.modifyData;
			if (modidyData && modidyData.length > 0) {
				var modifyHtml = '<div class="ydbz_tit pl20"><span class="fl">修改记录</span></div>';
				modifyHtml += '<div class="order_sea_orderlist_detail"><ol>';
				$(modidyData).each(function(index, obj) {
					modifyHtml += '<li><span>' + obj[0] + '</span> <span>' + obj[1] + ' 修改</span> <span>订单信息</span></li>';
				});
				modifyHtml += '</ol></div>';
				$(".release_next_add").before(modifyHtml);
			}
			//转团记录
			var changeGroupData = data.changeGroupData;
			if (changeGroupData && changeGroupData.length > 0) {
				var changeGroupHtml = '<div class="ydbz_tit pl20 maring_bottomz5"><span class="fl">转团记录</span></div>';
				changeGroupHtml += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom: 40px;">';
				changeGroupHtml += '<thead><tr>' 
											+ '<th width="12%">申请日期</th>' 
											+ '<th width="10%">游客姓名</th>' 
											+ '<th width="10%">舱位等级</th>'
											+ '<th width="7%">游客类型</th>' 
											+ '<th width="12%">转团原因</th>'
											+ '<th width="5%">转入团</th>' 
											+ '<th width="15%">申请人</th>' 
											+ '<th width="11%">审批状态</th>' 
										+ '</tr></thead><tbody>';
				$(changeGroupData).each(function(index, obj) {
					changeGroupHtml += '<tr group="travler1">' 
											+ '<td class="tc">' + obj.KEY_APPLYDATE + '</td>' 
											+ '<td class="tc">' + obj.KEY_TRAVELERNAME + '</td>' 
											+ '<td class="tc">' + getSpaceGradeTypeData('spaceGrade_Type', 'value', obj.KEY_TRAVELERLEVEL, 'label')+ '</td>'
											+ '<td class="tc">' + getData('traveler_type', 'uuid', obj.KEY_TRAVELERTYPE, 'name') + '</td>' 
											+ '<td class="tc">' + obj.KEY_REMARK + '</td>' 
											+ '<td class="tr">' + obj.KEY_NEWGROUPCODE + '</td>' 
											+ '<td class="tc">' + getData('sys_user', 'id', obj.createBy, 'name') + '</td>' 
											+ '<td class="tc tdgreen">已审批</td>' 
										+ '</tr>';
				});
				changeGroupHtml += '</tbody></table>';
				$(".release_next_add").before(changeGroupHtml);
			}
			
			//退团记录
			var exitGroupData = data.exitGroupData;
			if (exitGroupData && exitGroupData.length > 0) {
				var exitGroupHtml = '<div class="ydbz_tit pl20 maring_bottomz5"><span class="fl">退团记录</span></div>';
				exitGroupHtml += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom: 40px;">';
				exitGroupHtml += '<thead><tr>' 
										+ '<th width="12%">申请日期</th>' 
										+ '<th width="10%">游客姓名</th>' 
										+ '<th width="10%">舱位等级</th>'
										+ '<th width="7%">游客类型</th>' 
										+ '<th width="10%">应收金额</th>' 
										+ '<th width="10%">退团原因</th>'
										+ '<th width="7%">申请人</th>' 
										+ '<th width="6%">审批状态</th>' 
									+ '</tr></thead><tbody>';
				$(exitGroupData).each(function(index, obj) {
					exitGroupHtml += '<tr group="travler1">' 
											+ '<td class="tc">' + obj.createDate + '</td>' 
											+ '<td class="tc">' + obj.travelerName + '</td>' 
											+ '<td class="tc">' + obj.spaceLevelName + '</td>'
											+ '<td class="tc">' + obj.personTypeName + '</td>' 
											+ '<td class="tr">' + obj.payPrice + '</td>'
											+ '<td class="tc">' + obj.exitReason + '</td>'
											+ '<td class="tl">' + obj.createByName + '</td>' 
											+ '<td class="tc tdgreen">已审批</td>' 
										+ '</tr>';
				});
				exitGroupHtml += '</tbody></table>';
				$(".release_next_add").before(exitGroupHtml);
			}
			
			//改价记录
			var changePriceData = data.changePriceData;
			if (changePriceData && changePriceData.length > 0) {
				var changePriceHtml = '<div class="ydbz_tit pl20 maring_bottomz5"><span class="fl">改价记录</span></div>';
				changePriceHtml += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom: 40px;">';
				changePriceHtml += '<thead><tr>' 
											+ '<th width="12%">申请日期</th>' 
											+ '<th width="10%">姓名/团队</th>' 
											+ '<th width="10%">舱位等级</th>'
											+ '<th width="7%">游客类型</th>' 
											+ '<th width="8%">款项</th>' 
											+ '<th width="12%">改前金额</th>' 
											+ '<th width="12%">改后金额</th>'
											+ '<th width="5%">申请人</th>' 
											+ '<th width="15%">备注</th>' 
											+ '<th width="11%">审批状态</th>' 
										+ '</tr></thead><tbody>';
				$(changePriceData).each(function(index, obj) {
					changePriceHtml += '<tr group="travler1">' 
											+ '<td class="tc">' + obj.createDate + '</td>' 
											+ '<td class="tc">' + getData('island_traveler', 'id', obj.travelerId, 'name') + '</td>' 
											+ '<td class="tc">' + getSpaceGradeTypeData('spaceGrade_Type', 'value', obj.spaceLevel, 'label') + '</td>'
											+ '<td class="tc">' + getData('traveler_type', 'uuid', obj.personType, 'name') + '</td>'  
											+ '<td class="tc">' + obj.changedfund + '</td>' 
											+ '<td class="tr">' + obj.curtotalmoney + '</td>' 
											+ '<td class="tr">' + obj.changedtotalmoney + '</td>'
											+ '<td class="tc">' + getData('sys_user', 'id', obj.createBy, 'name') + '</td>' 
											+ '<td class="tl">' + obj.remark + '</td>' 
											+ '<td class="tc tdgreen">已审批</td>' 
										+ '</tr>';
				});
				changePriceHtml += '</tbody></table>';
				$(".release_next_add").before(changePriceHtml);
			}
			
			//返佣记录
			var rebatesData = data.rebatesData;
			if (rebatesData && rebatesData.length > 0) {
				var rebatesHtml = '<div class="ydbz_tit pl20 maring_bottomz5"><span class="fl">返佣记录</span></div>';
				rebatesHtml += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom: 40px;">';
				rebatesHtml += '<thead><tr>' 
										+ '<th width="12%">申请日期</th>' 
										+ '<th width="9%">姓名/团队</th>' 
										+ '<th width="9%">舱位等级</th>'
										+ '<th width="7%">游客类型</th>' 
										+ '<th width="7%">款项</th>' 
										+ '<th width="8%">应收款金额</th>' 
										+ '<th width="9%">累计返佣金额</th>'
										+ '<th width="6%">币种</th>' 
										+ '<th width="9%">本次返佣金额</th>'
										+ '<th width="13%">备注</th>' 
										+ '<th width="10%">审批状态</th>' 
									+ '</tr></thead><tbody>';
				$(rebatesData).each(function(index, obj) {
					rebatesHtml += '<tr group="travler1">' 
										+ '<td class="tc">' + obj.createDate + '</td>' 
										+ '<td class="tc">' + obj.traveler.name + '</td>' 
										+ '<td class="tc">' + getSpaceGradeTypeData('spaceGrade_Type', 'value', obj.traveler.spaceLevel, 'label')+ '</td>'
										+ '<td class="tc">' + getData('traveler_type', 'uuid', obj.traveler.personType, 'name') + '</td>' 
										+ '<td class="tc">' + obj.costname + '</td>' 
										+ '<td class="tr">' + getData('island_money_amount', 'serialNum', obj.totalMoney, 'amount') + '</td>' 
										+ '<td class="tr">' + obj.rebatesdiffString1 + '</td>'
										+ '<td class="tc">' + obj.currency.currencyName + '</td>' 
										+ '<td class="tc">' + obj.currency.currencyMark + obj.rebatesDiff + '</td>' 
										+ '<td class="tl">' + obj.remark + '</td>' 
										+ '<td class="tc tdgreen">已审批</td>' 
									+ '</tr>';
				});
				rebatesHtml += '</tbody></table>';
				$(".release_next_add").before(rebatesHtml);
			}
			
			//退款记录
			var refundData = data.refundData;
			if (refundData && refundData.length > 0) {
				var refundHtml = '<div class="ydbz_tit pl20 maring_bottomz5"><span class="fl">退款记录</span></div>';
				refundHtml += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom: 40px;">';
				refundHtml += '<thead><tr>' 
										+ '<th width="12%">申请日期</th>' 
										+ '<th width="10%">姓名/团队</th>' 
										+ '<th width="10%">舱位等级</th>'
										+ '<th width="7%">游客类型</th>' 
										+ '<th width="10%">应收金额</th>' 
										+ '<th width="10%">累计退款金额</th>' 
										+ '<th width="15%">本次退款金额</th>'
										+ '<th width="7%">申请人</th>' 
										+ '<th width="6%">审批状态</th>' 
									+ '</tr></thead><tbody>';
				$(refundData).each(function(index, obj) {
					refundHtml += '<tr group="travler1">' 
										+ '<td class="tc">' + obj.applyDate + '</td>' 
										+ '<td class="tc">' + (obj.travelerName ? obj.travelerName : "") + '</td>' 
										+ '<td class="tc">' + getSpaceGradeTypeData('spaceGrade_Type', 'value', obj.spaceLevel, 'label') + '</td>'
										+ '<td class="tc">' + getData('traveler_type', 'uuid', obj.personType, 'name') + '</td>' 
										+ '<td class="tr">' + getData('island_money_amount', 'serialNum', obj.totalMoney, 'amount') + '</td>' 
										+ '<td class="tr">' + (obj.refundTotal ? obj.refundTotal : "") + '</td>'
										+ '<td class="tc">' + obj.currencyMark + obj.refundPrice + '</td>' 
										+ '<td class="tl">' + getData('sys_user', 'id', obj.createBy, 'name') + '</td>' 
										+ '<td class="tc tdgreen">已审批</td>' 
									+ '</tr>';
				});
				refundHtml += '</tbody></table>';
				$(".release_next_add").before(refundHtml);
			}
			
			//借款记录
			var borrowingData = data.borrowingData;
			if (borrowingData && borrowingData.length > 0) {
				var borrowingHtml = '<div class="ydbz_tit pl20 maring_bottomz5"><span class="fl">借款记录</span></div>';
				borrowingHtml += '<table class="activitylist_bodyer_table modifyPrice-table activitylist_bodyer_table_new" style="margin-bottom: 40px;">';
				borrowingHtml += '<thead><tr>' 
											+ '<th width="12%">申请日期</th>' 
											+ '<th width="10%">姓名/团队</th>' 
											+ '<th width="10%">舱位等级</th>'
											+ '<th width="7%">游客类型</th>' 
											+ '<th width="10%">游客结算价</th>' 
											+ '<th width="10%">累计借款金额</th>' 
											+ '<th width="15%">本次借款金额</th>'
											+ '<th width="7%">申请人</th>' 
											+ '<th width="6%">审批状态</th>' + 
										'</tr></thead><tbody>';
				$(borrowingData).each(function(index, obj) {
					borrowingHtml += '<tr group="travler1">' 
										+ '<td class="tc">' + obj.applyDate + '</td>' 
										+ '<td class="tc">' + (obj.travelerName ? obj.travelerName : "") + '</td>' 
										+ '<td class="tc">' + getSpaceGradeTypeData('spaceGrade_Type', 'value', obj.spaceLevel, 'label') + '</td>'
										+ '<td class="tc">' + getData('traveler_type', 'uuid', obj.personType, 'name') + '</td>' 
										+ '<td class="tr">' + getData('island_money_amount', 'serialNum', obj.totalMoney, 'amount') + '</td>' 
										+ '<td class="tr">' + (obj.borrowingTotal? obj.borrowingTotal : "") + '</td>'
										+ '<td class="tc">' + obj.currencyMark + obj.lendPrice + '</td>' 
										+ '<td class="tl">' + getData('sys_user', 'id', obj.createBy, 'name') + '</td>' 
										+ '<td class="tc tdgreen">已审批</td>' 
									+ '</tr>';
				});
				borrowingHtml += '</tbody></table>';
				$(".release_next_add").before(borrowingHtml);
			}
		}
	});
}

/**
 * 从数据库根据表名和条件获取指定的值
 * @param tableName 表名
 * @param queryName 查询字段名
 * @param queryValue 查询字段名值
 * @param getName 查询值
 * @returns {String}
 */
function getData(tableName, queryName, queryValue, getName) {
	var returnValue = "";
	$.ajax({
		type :"POST",
		url: $("#ctx").val() + "/islandOrder/getData?dom=" + Math.random(),
		async: false,
		cache : false,
		data : {
				tableName : tableName,
				queryName : queryName,
				queryValue : queryValue,
				getName : getName
			},
		success:function(data) {
			returnValue = data;
		}
	});
	return returnValue;
}
/**
 * 从数据库根据表名和条件获取指定的值
 * @param tableName 表名
 * @param queryName 查询字段名
 * @param queryValue 查询字段名值
 * @param getName 查询值
 * @returns {String}
 */
function getSpaceGradeTypeData(type, queryName, queryValue, getName) {
	var returnValue = "";
	$.ajax({
		type :"POST",
		url: $("#ctx").val() + "/islandOrder/getSpaceGradeTypeData?dom=" + Math.random(),
		async: false,
		cache : false,
		data : {
				type : type,
				queryName : queryName,
				queryValue : queryValue,
				getName : getName
			},
		success:function(data) {
			returnValue = data;
		}
	});
	return returnValue;
}