
function productModify(proId){
	$("#searchForm").attr("action",ctx + "/visa/visaProducts/mod/"+proId);
	$("#searchForm").submit();
}


function idcheckchg(obj){
	var value = $(obj).val();
	if($(obj).attr("checked")){
		if(visaProductIds.indexOf($(obj).val()) < 0){
			visaProductIds = visaProductIds+$(obj).val()+",";
		}
	}			
	else{
		if($("input[name='allChk']").attr("checked"))
			$("input[name='allChk']").removeAttr("checked");
		if(visaProductIds.indexOf($(obj).val()) >= 0){
			
			visaProductIds = visaProductIds.replace($(obj).val()+",","");
		}
	}
	$("#visaProductIds").val(visaProductIds);
		
}


 //是否选中产品
function confirmBatchIsNull(mess,sta) {
	if(visaProductIds != ""){
		if(sta=='off'){
			confirmBatchOff(mess);
		}else if(sta=='del'){
			confirmBatchDel(mess);
		}else{
			confirmBatchRelease(mess);
		}
	}else{
		$.jBox.error('未选择产品','系统提示');
	}
}

// 批量删除确认对话框
function confirmBatchDel(mess){
	top.$.jBox.confirm(mess,'系统提示',function(v){
	if(v=='ok'){
		//loading('正在提交，请稍等...');
		$("#searchForm").attr("action",ctx + "/visa/visaProducts/batchdelVisaProducts/"+visaProductIds);
		$("#searchForm").submit();
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}
// 批量下架确认对话框
function confirmBatchOff(mess){
	top.$.jBox.confirm(mess,'系统提示',function(v){
	if(v=='ok'){
		//loading('正在提交，请稍等...');
		$("#searchForm").attr("action",ctx + "/visa/visaProducts/batchOnOrOffVisaProducts/2"+visaProductIds);
		$("#searchForm").submit();
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}
// 批量上架确认对话框
function confirmBatchRelease(mess){
	top.$.jBox.confirm(mess,'系统提示',function(v){
	if(v=='ok'){
		//loading('正在提交，请稍等...');
		$("#searchForm").attr("action",ctx + "/visa/visaProducts/batchOnOrOffVisaProducts/3"+visaProductIds);
		$("#searchForm").submit();
		}
	},{buttonsFocus:1});
	top.$('.jbox-body .jbox-icon').css('top','55px');
	return false;
}


//单个产品上架
function releaseProduct(mess,proId){
	top.$.jBox.confirm(mess,'系统提示',function(v){
		if(v=='ok'){
			loading('正在提交，请稍等...');
			$("#searchForm").attr("action",ctx + "/visa/visaProducts/release/"+proId);
			$("#searchForm").submit();
			}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
		return false;
}


function onLineProduct(activityId){
    $.jBox.confirm("确定要上架该产品吗？", "提示", function(v, h, f){
        if (v == 'ok') {
        	   $("#searchForm").attr("action",ctx + "/activity/manager/batchrelease/"+activityId);
        	   $("#searchForm").submit();
                }else if (v == 'cancel'){
                    
                }
            });
}

/**
 * 按部门查询订单
 * 
 * param departmentId
 */
function getDepartment(departmentId) {
	$("#departmentId").val(departmentId);
	$("#searchForm").submit();
}



/**
 * 对应需求号：c361
 * xinwei.wang add 20151113
 * 签证产品销售明细   开始
 */

function getProductOrderList(obj, ctx, visaproductid) {
	if("false" == $(obj).attr("openFlag")) {
		var parents = $(obj).parents("tr");
		var batchNo = parents.find("td.batchNoTD").text();
		if(parents.next().attr("class") != 'activity_team_top1') {
			$.ajax({
				type:"POST",
				url:ctx+"/visa/preorder/getProductOrderList",
				data:{
					visaproductid : visaproductid
				},
				success:function(orderList){
					oldlength = orderList.lengt;
					//table thead部分
					if(null != orderList) {
						var table = $('<tr class="activity_team_top1"></tr>')
						.append('<td class="team_top" colspan="10">'
								+ '<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">'
								+ '<thead><tr>'
								+ '<th class="tc" width="6%">序号</th>'
								+ '<th class="tc" width="10%">订单号</th>'
								+ '<th class="tc" width="10%">销售</th>'
								+ '<th class="tc" width="10%">下单人</th>'
								+ '<th class="tc" width="7%">预定时间</th>'
								+ '<th class="tc" width="7%">人数</th>'
								+ '<th class="tc" width="7%">订单状态</th>'
								+ '<th class="tc" width="7%">订单总额</th>'
								+ '<th class="tc" width="8%"><div class="out-date">已付金额</div><div class="close-date">到账金额</div></th>'
								+ '</tr></thead></table></td>');
						//tbody部分
						if(orderList.length>0){
							for(var i = 0; i<orderList.length; i++) {
								addHtmlTr(orderList[i], ctx, table);
							}
							
						}else{
							addHtmlTrBlank(table);
						}
						
						
						parents.after(table);
					}/*else{
						$.jBox.tip("没有有效订单！");
					}*/
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

function addHtmlTr(order, ctx, table) {
	var tbody;
		 tbody=$('<tbody></tbody>')
			.append('<tr>'
				+ '<td class="tc">' + order.num + '</td>'
				+ '<td class="tc">' + order.order_no + '</td>'
				+ '<td class="tc">' + order.salerName + '</td>'
				+ '<td class="tc">' + order.create_by + '</td>'
				+ '<td class="tc">' + order.create_date + '</td>'
				+ '<td class="tc">' + order.travel_num + '</td>'
				+ '<td class="tc">' + order.payStatus + '</td>'
				+ '<td class="tc">' + order.total_money + '</td>'
				+ '<td class="tc"><div class="out-date">' + order.payed_money + '</div><div class="close-date">' + order.accounted_money + '</div></td>'
				+ '</tr>');
	table.find("table").append(tbody);
}

function addHtmlTrBlank(table) {
	var tbody;
		 tbody=$('<tbody></tbody>')
			.append('<tr>'
				+ '<td class="tc" colspan="9">没有有效订单！</td>'
				
				+ '</tr>');
	table.find("table").append(tbody);
}

/**
 * 对应需求号：c361
 * xinwei.wang add 20151113
 * 签证产品销售明细   结束
 */

	    
	    
