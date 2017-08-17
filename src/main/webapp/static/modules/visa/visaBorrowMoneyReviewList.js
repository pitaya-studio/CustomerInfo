/**
 * Created by yunpeng.zhang on 2015/12/4.
 */

//游客列表信息
function getTravelerList(obj, ctx, busynessType) {

    var groupCodeName;
    if("7a816f5077a811e5bc1e000c29cf2586"==companeyUUID){
    	groupCodeName = "订单团号"
    }else{
    	groupCodeName = "团号"
    }
	
    if("false" == $(obj).attr("openFlag")) {
        var parents = $(obj).parents("tr");
        var batchNo = parents.find("td.batchNoTD").text();
        if(parents.next().attr("class") != 'activity_team_topNew') {
            $.ajax({
                type:"POST",
                url:ctx+"/visa/borrowMoney/review/getTravelerList?dom=" + Math.random(),
                data:{
                    batchNo : batchNo,
                    businessType : busynessType
                },
                success:function(travelerList){
                    //table thead部分
                    if(null != travelerList) {
                        var table = $('<tr class="activity_team_top1"></tr>')
                            .append('<td class="team_top" colspan="15">'
                                + '<table id="teamTable" class="table activitylist_bodyer_table" style="margin:0 auto;">'
                                + '<thead><tr>'
                                + '<th class="tc" width="6%">序号</th>'
                                + '<th class="tc" width="12%">订单号</th>'
                                + '<th class="tc" width="12%">'+groupCodeName+'</th>'
                                + '<th class="tc" width="8%">下单人</th>'
                                + '<th class="tc" width="8%">游客</th>'
                                + '<th class="tc" width="10%">签证类型</th>'
                                + '<th class="tc" width="10%">签证国家</th>'
                                + '<th class="tc" width="10%">借款金额</th>'
                                + '<th class="tc" width="10%">' + ('1'==busynessType?'还收据':'借款') + '原因</th>'
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
        }
        else{
            parents.next("tr.activity_team_topNew").show();
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
        href += ctx + '/visa/borrowMoney/review/visaReturnReceiptReviewDetail4Activiti?'
            + 'orderId=' + traveler.orderId
            + '&travelerId=' + traveler.tid
            + '&flag=1'
            + '&revid=' + traveler.reviewId
            + '&flowType=5';
    }

    var tbody;
    if("1" == busynessType) {//还收据
    	
    	var remark = (traveler.returnReceiptRemark == undefined)?"":traveler.returnReceiptRemark; 
    	
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
                + '<td class="tc">' + remark + '</td>'
                + '<td class="tc"><a onclick="" href="' + href + '">查看</a></td>'
                + '</tr>');
    }else if("2" == busynessType) {//借款
    	
    	var remark = (traveler.remark == undefined)?"":traveler.remark; 
    	
        tbody=$('<tbody></tbody>')
            .append('<tr>'
                + '<td class="tc">' + traveler.num + '</td>'
                + '<td class="tc">' + traveler.orderNo + '</td>'
                + '<td class="tc">' + traveler.groupCode + '</td>'
                + '<td class="tc">' + traveler.createBy + '</td>'
                + '<td class="tc">' + traveler.travellerName + '</td>'
                + '<td class="tc">' + traveler.visaType + '</td>'
                + '<td class="tc">' + traveler.visaCountry + '</td>'
                + '<td class="tc">' + traveler.borrowCurrency + '&nbsp;' + traveler.borrowAmount + '</td>'
                + '<td class="tc">' + remark + '</td>'
                + '<td class="tc"><a onclick="" href="' + href + '">查看</a></td>'
                + '</tr>');
    }

    table.find("table").append(tbody);
}