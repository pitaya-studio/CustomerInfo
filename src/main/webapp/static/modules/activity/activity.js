$(function(){
    $("#closeBeforeDays").append($("<option value='0'>请选择</option>"));
    $("#visaBeforeDays").append($("<option value='0'>请选择</option>"));
    var orderType = $("#activityKind").val();
    var tuijian = $("#tuijian").val();
    for(var i=1;i<=365;i++){
        var option1 = $("<option value="+i+">"+i+"</option>");
        var option2 = $("<option value="+i+">"+i+"</option>");
        $("#closeBeforeDays").append(option1);
        $("#visaBeforeDays").append(option2);
    }
    $("#closeBeforeDays").bind("change",function(){
        //debugger;
        var closeBeforeVal = "0";
        var value = parseInt($(this).find("option:selected").text())+1;
        if(isNaN(value)){
            value = 0;
        }
//		top.$.jBox.confirm('您确定要修改提前天数吗，这将会影响之前的结果','系统提示',function(v){
//			if(v=='ok'){
        var groupOpenDate = $("#groupOpenDate").val();
        if((new Date(groupOpenDate).getTime()-value*24*3600*1000)<=new Date().getTime()){
            top.$.jBox.info('提前天数比最小出团日期提前了', '警告');
            $("#closeBeforeDays option[value='"+closeBeforeVal+"']").attr("selected",true);
        }else{
            closeBeforeVal = value;
            $("#contentTable tbody").children().each(function(index,obj){
                var opendate;
                //if(orderType == 2 & tuijian == 'true'){
                //	opendate = $(obj).find("input[name=groupOpenDate]").val();//$(obj).find("td:eq(1) input").val();
                //}else{
                //opendate = $(obj).find("td:eq(0) input").val();
                //}
                opendate = $(obj).find("input[name=groupOpenDate]").val();
                var curDate = new Date();
                curDate.setTime($(document).parseISO8601(opendate).getTime()-value*24*3600*1000);
                if(orderType == 2 & tuijian == 'true'){
                    if(value == 0){
                        $(obj).find("input[name=groupCloseDate]").val("");
                    }else{
                        $(obj).find("input[name=groupCloseDate]").val(curDate.getFullYear()+"-"+($.trim((curDate.getMonth()+1)).length==1?"0"+(curDate.getMonth()+1):(curDate.getMonth()+1))
                            +"-"+($.trim((curDate.getDate())).length==1?"0"+(curDate.getDate()):(curDate.getDate())));
                    }
                }else{
                    if(value == 0){
                        $(obj).find("input[name=groupCloseDate]").val("");
                    }else{
                        $(obj).find("input[name=groupCloseDate]").val(curDate.getFullYear()+"-"+($.trim((curDate.getMonth()+1)).length==1?"0"+(curDate.getMonth()+1):(curDate.getMonth()+1))
                            +"-"+($.trim((curDate.getDate())).length==1?"0"+(curDate.getDate()):(curDate.getDate())));
                    }
                }
            });
        }
//			}else{
//				$("#closeBeforeDays option[value='"+closeBeforeVal+"']").attr("selected",true);
//			}
//		},{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });
    $("#visaBeforeDays").bind("change",function(){
        var visaBeforeVal = 0;
        var value = parseInt($(this).find("option:selected").text())+1;
        if(isNaN(value)){
            value = 0;
        }
//		top.$.jBox.confirm('您确定要修改提前天数吗，这将会影响之前的结果','系统提示',function(v){
//			if(v=='ok'){
        var groupOpenDate = $("#groupOpenDate").val();
        if((new Date(groupOpenDate).getTime()-value*24*3600*1000)<=new Date().getTime()){
            top.$.jBox.info('提前天数比最小出团日期提前了', '警告');
            $("#visaBeforeDays option[value='"+visaBeforeVal+"']").attr("selected",true);
        }else{
            visaBeforeVal = value;
            $("#contentTable tbody").children().each(function(index,obj){
                //var opendate;
                //if(orderType == 2 & tuijian == 'true'){
                //	var opendate = $(obj).find("input[name=groupOpenDate]").val();
                //}else{
                //var opendate = $(obj).find("td:eq(0) input").val();
                //}

                var opendate = $(obj).find("input[name=groupOpenDate]").val();
                var curDate = new Date();
                curDate.setTime($(document).parseISO8601(opendate).getTime()-value*24*3600*1000);
                if(orderType == 2 & tuijian == 'true'){
                    if(value == 0){
                        $(obj).find("input[name=visaDate]").val("");
                    }else{
                        $(obj).find("input[name=visaDate]").val(curDate.getFullYear()+"-"+($.trim((curDate.getMonth()+1)).length==1?"0"+(curDate.getMonth()+1):(curDate.getMonth()+1))
                            +"-"+($.trim((curDate.getDate())).length==1?"0"+(curDate.getDate()):(curDate.getDate())));
                    }
                }else{
                    if(value == 0){
                        $(obj).find("input[name=visaDate]").val("");
                    }else{
                        $(obj).find("input[name=visaDate]").val(curDate.getFullYear()+"-"+($.trim((curDate.getMonth()+1)).length==1?"0"+(curDate.getMonth()+1):(curDate.getMonth()+1))
                            +"-"+($.trim((curDate.getDate())).length==1?"0"+(curDate.getDate()):(curDate.getDate())));
                    }
                }

            });
        }
//			}else{
//				$("#visaBeforeDays option[value='"+visaBeforeVal+"']").attr("selected",true);
//			}
//		},{buttonsFocus:1});
        top.$('.jbox-body .jbox-icon').css('top','55px');
    });




    $(document).on('click','[name="yingFuDate"]',function(){
        var $this = $(this);
        var minDate = $this.parents('tr:first').find('[name="groupOpenDate"]').val();
        WdatePicker({minDate:minDate});
    })



    //默认显示第一条汇率
//	$("#selectCurrency").next("span").html("汇率：" + $("#selectCurrency").children("option:selected").attr("var"));
    $(".add2_nei").children().find("input[type='text'][name!='singleDiffUnit'][name!='specialRemark']").before("<span>" + $("#selectCurrency").children("option:selected").attr("id") + "</span>");

    //每个价格输入框前都有币种选择
    var _choCur = $(".choCur").clone();
    $(".add2_nei").find("span[class='fr']").after(_choCur.html());


	//299v2  将列表中的"酒店房型"逗号隔开，分开显示//518修改格式
	$(".hotelAndHouse").each(function(){
		var groupHotel = $(this).find("input[name=groupHotel]").val().split(",");
		var groupHouseType = $(this).find("input[name=groupHouseType]").val().split(",");
		var html = "";
		if(groupHotel == "" || groupHouseType == ""){
			html += "<p><label>酒店：</label></p>";
			// html += "&nbsp&nbsp";
			html += "<p><label>房型：</label></p>"
		}else {
			for (var i = 0; i < groupHotel.length; i++) {
				html += "<p>";
				html += "<label>酒店：</label>"
				html += "<span class='houseAndType word-break-all'>" + groupHotel[i] + "</span>";
				html +=	"</p>";
				// html += "&nbsp&nbsp";
				html += "<p>"
				html += "<label>房型：</label>";
				html += "<span class='houseAndType word-break-all'>" + groupHouseType[i] + "</span>";
				html += "</p>";
			}
		}
		$(this).append(html);
	});


    //299v2  将列表中的"酒店房型"逗号隔开，分开显示
    /*$(".hotelAndHouse").each(function(){
        var groupHotel = $(this).find("input[name=groupHotel]").val().split(",");
        var groupHouseType = $(this).find("input[name=groupHouseType]").val().split(",");
        var html = "";
        if(groupHotel == "" || groupHouseType == ""){
            html += "<p><label>酒店：</label>";
            html += "&nbsp&nbsp";
            html += "<label>房型：</label></p>"
        }else {
            for (var i = 0; i < groupHotel.length; i++) {
                html += "<p>";
                html += "<label>酒店:</label>"
                html += "<span class='houseAndType'>" + groupHotel[i] + "</span>";
                html += "&nbsp&nbsp";
                html += "<label>房型:</label>";
                html += "<span class='houseAndType'>" + groupHouseType[i] + "</span>";
                html += "</p>";
            }
        }
        $(this).append(html);
    });*/


});

function visaCopy(){
    var visacountry = $("#visaCountryCopy").val();
    if(visacountry!=""){
        $("#contentTable tbody").find("input[name='visaCountry']").val(visacountry);
    }

}
function positionCopy(obj){
    var planPosition = $(obj).prev().prev().val();
    if(planPosition!="") {
        $("#contentTable tbody").find("input[name='"+$(obj).attr("id").substring(0,12)+"']").val(planPosition);
    }
}

//0258懿洋假期 发票税 专用
function positionCopy4YYJQ(obj){
    var planPosition = $(obj).prev().prev().val();
    if(planPosition!="") {
        $("#contentTable tbody").find("input[name='"+$(obj).attr("id").substring(0,10)+"']").val(planPosition);
    }
}

/**
 * 对应需求号 0258 wxw  added
 * 添加发票税  复制功能
 * @param obj
 */
function positionCopy4InvoiTax(obj){
    var planPosition = $(obj).prev().prev().val();
    if(planPosition!="") {
        $("#contentTable tbody").find("input[name='"+$(obj).attr("id").substring(0,10)+"']").val(planPosition);
    }
}

function delAllGroupDate(){
    $(document).datepickerRefactor.delAllGroupDate();
}

function delGroupDateValue() {
    $("#dateList").val("");
    $("[name='groupOpenDateBegin']").val("");
    $("[name='groupCloseDateEnd']").val("");
}

Array.prototype.contains = function (element) {

    for (var i = 0; i < this.length; i++) {
        if (this[i] == element) {
            return true;
        }
    }
    return false;
}

//是否符合金额规则
function isMoney(obj){
    var rr = $(obj).val();
    var rule = /^[^0-9|\.]$/;
    if(rr.length>0){
        var newStr='';
        //过滤掉非字（不过滤小数点）
        for(var i=0;i<rr.length;i++){
            var c = rr.substr(i,1);
            if(!rule.test(c)){
                newStr+=c;
            }
        }
        if(newStr!=''){
            //只能有一个小数点，并去掉多余的0
            var szfds = newStr.split('.');
            var zs = '';
            var xs = '';
            if(szfds.length>1){
                zs=szfds[0];
                xs=szfds[1];
                for(var i=1;i<zs.length;i++){
                    var zs_char = zs.substr(0,1);
                    if(zs_char=='0'){
                        zs = zs.substring(1,zs.length);
                    }
                }
                //保留两位小数
                if(xs.length>2){
                    xs = xs.substring(0,2);
                }
                newStr = zs+'.'+xs;
            }else{
                zs=szfds[0];
                for(var i=1;i<zs.length;i++){
                    var zs_char = zs.substr(0,1);
                    if(zs_char=='0'){
                        newStr = zs.substring(1,zs.length);
                    }
                }
            }

            //'.'之前没有数字会自动补0
            if(newStr.indexOf('.')==0){
                newStr='0'+newStr;
            }
        }


        $(obj).val(newStr);//asdf090980123
    }
}

//产品列表-修改产品
function savegroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url,chargeRate){
    // 433需求 只有预收或只有余位数变更后，保存时，弹出提示
    var planPositon = $(obj).parent().parent().find("input[name='planPosition']").val();
    var freePositon = $(obj).parent().parent().find("input[name='freePosition']").val();

    var groupId = $(obj).parents("tr:first").find("td").eq(0).find("input").attr("id");
    var flag;
    $.ajax({
        type:"POST",
        url:url+"/activity/manager/getGroupPlanAndFreePositon",
        dataType:"json",
        contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
        data:{	groupId:groupId},
        success:function(result){
            if(planPositon != result.planPositon && freePositon == result.freePositon){// 只修改了预收数，没有修改余位数
                $.jBox.confirm("预收数已发生变化，是否要修改余位数？", "系统提示", function (v, h, f) {
                    if (v == 'ok') {
                        return;
                    } else if (v == 'cancel') {
                        confirmSaveGroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url,chargeRate);
                    }
                });
            }else if(planPositon == result.planPositon && freePositon != result.freePositon){// 只修改了余位数，没有修改预收数时
                $.jBox.confirm("余位数已发生变化，是否要修改预收数？", "系统提示", function (v, h, f) {
                    if (v == 'ok') {
                        return;
                    } else if (v == 'cancel') {
                        confirmSaveGroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url,chargeRate);
                    }
                });
            }else{ // 当预收数和余位数都不变化或都变化时
                confirmSaveGroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url,chargeRate);
            }
        }
    })
    // 433需求 只有预收或只有余位数变更后，保存时，弹出提示
}

// 保存
function confirmSaveGroup(srcActivityId,modbtn,delbtn,cancelbtn,obj,childform,childtr,url,chargeRate){
    //debugger;
    var groupCodeSpan = $(obj).parents("tr:first").find("td").eq(0).find("span").text();
    var groupCodeInput = $(obj).parents("tr:first").find("td").eq(0).find("input").val();
    var rate = new Number(chargeRate)+1;
    if(groupCodeSpan!=groupCodeInput){

        /**
         * 对应需求  c460
         * 团期进行修改操作时给出提示信息
         */
        $.jBox.confirm("团号修改后该团期下订单数据、财务数据、审批数据对应的团号会相应变化，确认修改？", "提示", function(v, h, f){
            if (v == 'ok') {

                var validator1 = $(childform).validate();
                //$(childform).removeAllRules();
                var groupId = $(obj).parent().parent().find("input[name='groupid']").val();
                var recommend;
                var recommendCheck = $(obj).parent().parent().find("input[name='recommend'][flag='mod']");
                if(recommendCheck.attr("checked") == "checked"){
                    recommend = "1";
                }else{
                    recommend = "0";
                }
                $(childtr).addRules();
                //	srcActivityId,url,groupId
                if(validator1.form()){
                    $.ajax({
                        type:"POST",
                        url:url+"/activity/manager/savegroup2/"+srcActivityId,
                        dataType:"json",
                        contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
                        data:{	srcActivityId:srcActivityId,
                            groupid:groupId,
                            recommend:recommend,
                            groupOpenDate:$(obj).parent().parent().find("input[name='groupOpenDate']").val(),
                            groupCloseDate:$(obj).parent().parent().find("input[name='groupCloseDate']").val(),
                            groupCode:$(obj).parent().parent().find("input[name='groupCode']").val(),
                            settlementAdultPrice:$(obj).parent().parent().find("input[name='settlementAdultPrice']").val(),
                            settlementcChildPrice:$(obj).parent().parent().find("input[name='settlementcChildPrice']").val(),
                            settlementSpecialPrice:$(obj).parent().parent().find("input[name='settlementSpecialPrice']").val(),
                            settlementAdultCurrencyType:$(obj).parent().parent().find("input[name=settlementAdultPrice]").parent().find(".rm").text(),//当前币种的符号
                            suggestAdultCurrencyType:$(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find(".rm").text(),
                            suggestAdultPrice:$(obj).parent().parent().find("input[name='suggestAdultPrice']").val(),
                            suggestChildPrice:$(obj).parent().parent().find("input[name='suggestChildPrice']").val(),
                            suggestSpecialPrice:$(obj).parent().parent().find("input[name='suggestSpecialPrice']").val(),
                            //-------t1t2添加quauq价--开始---------------------------------------------------------
                            quauqAdultPrice:$(obj).parent().parent().find("input[name='quauqAdultPrice']").val(),
                            quauqChildPrice:$(obj).parent().parent().find("input[name='quauqChildPrice']").val(),
                            quauqSpecialPrice:$(obj).parent().parent().find("input[name='quauqSpecialPrice']").val(),
                            //-------t1t2添加quauq价--结束---------------------------------------------------------
                            maxChildrenCount:$(obj).parent().parent().find("input[name='maxChildrenCount']").val(),
                            maxPeopleCount:$(obj).parent().parent().find("input[name='maxPeopleCount']").val(),
                            payDeposit:$(obj).parent().parent().find("input[name='payDeposit']").val(),
                            singleDiff:$(obj).parent().parent().find("input[name='singleDiff']").val(),
                            planPosition:$(obj).parent().parent().find("input[name='planPosition']").val(),
                            freePosition:$(obj).parent().parent().find("input[name='freePosition']").val(),
                            //0258需求,新增发票税,展开团期,修改->确认--s//
                            invoiceTax:$(obj).parent().parent().find("input[name='invoiceTax']").val(),
                            //0258需求,新增发票税,展开团期,修改->确认--e//
                            visaDate:$(obj).parent().parent().find("input[name='visaDate']").val(),
                            visaCountry:$(obj).parent().parent().find("input[name='visaCountry']").val(),
                            groupRemark:$(obj).parent().parent().next().find('[name="groupNote"]').val()
                        },
                        success:function(result){
                            if(result==null){
                                top.$.jBox.info("该团期已经存在", "警告");
                            }else if(result.groupCode=="groupCodeRepeat") {
                                top.$.jBox.info("该团号已经存在", "警告");
                                return;
                            }else if(result.error == "errorChildNum"){
                                top.$.jBox.info("儿童最高人数不能小于团期已报名儿童人数", "警告");
                                return;
                            }else if(result.error == "errorSpecialNum"){
                                top.$.jBox.info("特殊人去最高人数不能小于团期已报名特殊人数", "警告");
                                return;
                            }else{
                                var group = result.group;
                                if(result.groupopendate && result.groupclosedate){
                                    if(result.groupopendate == result.groupclosedate){
                                        $("#groupdate"+srcActivityId+" #truedate").find("span").html(result.groupopendate);
                                    }
                                    else{
                                        $("#groupdate"+srcActivityId+" #truedate").find("span").html(result.groupopendate+"至"+result.groupclosedate);
                                    }

                                    $("#groupdate"+srcActivityId+" #truedate").find("a").html("关闭全部团期");
                                    $("#groupdate"+srcActivityId+" #truedate").show();
                                    $("#groupdate"+srcActivityId+" #falsedate").hide();

                                }
                                else{
                                    $("#groupdate"+srcActivityId+" #falsedate").show();
                                    $("#groupdate"+srcActivityId+" #truedate").show();
                                }
                                if(result.settlementadultprice){
                                    if(result.settlementAdultPriceCMark=="-1"){
                                        var settlementAdultPriceCMark =$(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").text();
                                        $("#settleadultprice"+srcActivityId).html(settlementAdultPriceCMark+"<span class='tdred fbold'>"+$(document).commafy(result.settlementadultprice)+"</span>起");
                                    }else{
                                        $("#settleadultprice"+srcActivityId).html(result.settlementAdultPriceCMark+"<span class='tdred fbold'>"+$(document).commafy(result.settlementadultprice)+"</span>起");
                                    }
                                }else{
                                    $("#settleadultprice"+srcActivityId).html("价格待定");
                                }
                                //					if(result.trekiz)
                                //						$("#trekiz"+srcActivityId).html("¥"+"<span style=\"color:#eb0301;font-size:20px;\">"+$(document).commafy(result.trekiz)+"</span>起");
                                //					else
                                //						$("#trekiz"+srcActivityId).html("价格待定");
                                if(result.suggestadultprice){
                                    if(result.suggestAdultPriceCMark=="-1"){
                                        var suggestAdultPriceCMark =$(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").text();
                                        $("#suggestadultprice"+srcActivityId).html(suggestAdultPriceCMark+"<span class='tdblue fbold'>"+$(document).commafy(result.suggestadultprice)+"</span>起");
                                    }else{
                                        $("#suggestadultprice"+srcActivityId).html(result.suggestAdultPriceCMark+"<span class='tdblue fbold'>"+$(document).commafy(result.suggestadultprice)+"</span>起");
                                    }
                                    //						$("#suggestadultprice"+srcActivityId).html("¥"+"<span class='tdblue fbold'>"+$(document).commafy(result.suggestadultprice)+"</span>起");
                                    //$("#suggestadultprice"+srcActivityId).find("span").text($(document).commafy(result.suggestadultprice));
                                }else{
                                    $("#suggestadultprice"+srcActivityId).html("价格待定");
                                }

                                if(group.recommend=='1'){
                                    $(obj).parent().parent().find("input[name='recommend']").attr("checked","checked");
                                }
                                if(group.recommend=='0'){
                                    $(obj).parent().parent().find("input[name='recommend']").attr("checked",false);
                                }

                                $(obj).parent().parent().find("input[name='groupid']").css("display","none").attr("disabled",true);
                                var groupOpenDate = $(obj).parent().parent().find("input[name='groupOpenDate']")[0];
                                $(groupOpenDate).val(group.groupOpenDate);
                                $(groupOpenDate).prev().prev().html(group.groupOpenDate);

                                $(obj).parent().parent().find("input[name='groupCloseDate']").val(group.groupCloseDate);
                                $(obj).parent().parent().find("input[name='groupCloseDate']").parent().find("span").html(group.groupCloseDate);

                                $(obj).parent().parent().find("input[name='visaCountry']").val(group.visaCountry);
                                $(obj).parent().parent().find("input[name='visaCountry']").parent().find("span").html(group.visaCountry);

                                $(obj).parent().parent().find("input[name='groupCode']").val(group.groupCode);
                                $(obj).parent().parent().find("input[name='groupCode']").parent().find("span").html(group.groupCode);
                                $(obj).parent().parent().find("input[name='settlementAdultPrice']").val(getFloatPrice(group.settlementAdultPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").length)){
                                    if(group.settlementAdultPrice!=0){
                                        $(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().append("<span class='tdred'>"+getFloatPrice(group.settlementAdultPrice)+"</span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementAdultPrice)));
                                }

                                $(obj).parent().parent().find("input[name='settlementcChildPrice']").val(getFloatPrice(group.settlementcChildPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().find("span[class=rm]").length)){
                                    if(group.settlementcChildPrice!=0){
                                        $(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().append("<span class='rm'><span class='tdred'>"+(getFloatPrice(group.settlementcChildPrice))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementcChildPrice)));
                                }


                                $(obj).parent().parent().find("input[name='settlementSpecialPrice']").val(getFloatPrice(group.settlementSpecialPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().find("span[class=rm]").length)){
                                    if(group.settlementSpecialPrice!=0){
                                        $(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().append("<span class='rm'><span class='tdred'>"+(getFloatPrice(group.settlementSpecialPrice))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementSpecialPrice)));
                                }

                                //			$(obj).parent().parent().find("input[name='trekizPrice']").val(group.trekizPrice==0?"":group.trekizPrice);
                                //			$(obj).parent().parent().find("input[name='trekizPrice']").parent().find("span").html("¥"+(group.trekizPrice==0?"":group.trekizPrice));

                                //			$(obj).parent().parent().find("input[name='trekizChildPrice']").val(group.trekizChildPrice==0?"":group.trekizChildPrice);
                                //			$(obj).parent().parent().find("input[name='trekizChildPrice']").parent().find("span").html("¥"+(group.trekizChildPrice==0?"":group.trekizChildPrice));

                                $(obj).parent().parent().find("input[name='suggestAdultPrice']").val(getFloatPrice(group.suggestAdultPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").length)){
                                    if(group.suggestAdultPrice!=0){
                                        $(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>"+(getFloatPrice(group.suggestAdultPrice))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestAdultPrice)));
                                }

                                $(obj).parent().parent().find("input[name='suggestChildPrice']").val(getFloatPrice(group.suggestChildPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='suggestChildPrice']").parent().find("span[class=rm]").length)){
                                    if(group.suggestChildPrice!=0){
                                        $(obj).parent().parent().find("input[name='suggestChildPrice']").parent().append("<span class='rm'><span class='tdblue'>"+(getFloatPrice(group.suggestChildPrice))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='suggestChildPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestChildPrice)));
                                }

                                $(obj).parent().parent().find("input[name='suggestSpecialPrice']").val(getFloatPrice(group.suggestSpecialPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().find("span[class=rm]").length)){
                                    if(group.suggestSpecialPrice!=0){
                                        $(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>"+(getFloatPrice(group.suggestSpecialPrice))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestSpecialPrice)));
                                }


                                //----quauq价修改---start-----------------------------------------------------
                                $(obj).parent().parent().find("input[name='quauqAdultPrice']").val(getFloatPriceForQuauq(result.quauqAdultPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class=rm]").length)){
                                    if(result.quauqAdultPrice!=0){
                                        $(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqAdultPrice)+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class!='rm']").html((getFloatPriceForQuauq(result.quauqAdultPrice)));
                                }

                                $(obj).parent().parent().find("input[name='quauqChildPrice']").val(getFloatPriceForQuauq(result.quauqChildPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class=rm]").length)){
                                    if(result.quauqChildPrice!=0){
                                        $(obj).parent().parent().find("input[name='quauqChildPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqChildPrice)+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(result.quauqChildPrice));
                                }

                                $(obj).parent().parent().find("input[name='quauqSpecialPrice']").val(getFloatPriceForQuauq(result.quauqSpecialPrice));
                                if(spanRMlength($(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class=rm]").length)){
                                    if(result.quauqSpecialPrice!=0){
                                        $(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqSpecialPrice)+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(result.quauqSpecialPrice));
                                }
                                //----quauq价修改---end-----------------------------------------------------

                                //----供应价修改---start-----------------------------------------------------
                                if(result.quauqAdultPrice!=null){
                                    $(obj).parent().parent().find("input[name='supplyAdultPrice']").val(getFloatPriceForQuauq(result.quauqAdultPrice*rate));
                                    if(spanRMlength($(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().find("span[class=rm]").length)){
                                        if(result.quauqAdultPrice!=0){
                                            $(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqAdultPrice*rate)+"</span></span>");
                                        }
                                    }else{
                                        $(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().find("span[class!='rm']").html((getFloatPriceForQuauq(result.quauqAdultPrice*rate)));
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='supplyAdultPrice']").val("");
                                    $(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().find("span[class!='rm']").html("");
                                }

                                if(result.quauqChildPrice!=null){
                                    $(obj).parent().parent().find("input[name='supplyChildPrice']").val(getFloatPriceForQuauq(result.quauqChildPrice*rate));
                                    if(spanRMlength($(obj).parent().parent().find("input[name='supplyChildPrice']").parent().find("span[class=rm]").length)){
                                        if(result.quauqChildPrice!=0){
                                            $(obj).parent().parent().find("input[name='supplyChildPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqChildPrice*rate)+"</span></span>");
                                        }
                                    }else{
                                        $(obj).parent().parent().find("input[name='supplyChildPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(result.quauqChildPrice*rate));
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='supplyChildPrice']").val("");
                                    $(obj).parent().parent().find("input[name='supplyChildPrice']").parent().find("span[class!='rm']").html("");
                                }

                                if(result.quauqSpecialPrice!=null){
                                    $(obj).parent().parent().find("input[name='supplySpecialPrice']").val(getFloatPriceForQuauq(group.quauqSpecialPrice*rate));
                                    if(spanRMlength($(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().find("span[class=rm]").length)){
                                        if(result.quauqSpecialPrice!=0){
                                            $(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(group.quauqSpecialPrice*rate)+"</span></span>");
                                        }
                                    }else{
                                        $(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(group.quauqSpecialPrice*rate));
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='supplySpecialPrice']").val("");
                                    $(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().find("span[class!='rm']").html("");
                                }
                                //----供应价修改---end-----------------------------------------------------



                                //			$(obj).parent().parent().find("input[name='maxPeopleCount']").val(group.maxPeopleCount);
                                //			if(spanRMlength($(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span[class=rm]").length)){
                                //				if(group.payDeposit!=0){
                                //					$(obj).parent().parent().find("input[name='maxPeopleCount']").parent().append("<span class='rm'><span class='tdorange'>"+(group.maxPeopleCount)+"</span></span>");
                                //				}
                                //			}else{
                                //				$(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span[class!='rm']").html((group.maxPeopleCount));
                                //			}
                                //

                                if(group.maxChildrenCount!=0){
                                    $(obj).parent().parent().find("input[name='maxChildrenCount']").val(group.maxChildrenCount);
                                    $(obj).parent().parent().find("input[name='maxChildrenCount']").parent().find("span").html(group.maxChildrenCount);
                                }else{
                                    $(obj).parent().parent().find("input[name='maxChildrenCount']").val("");
                                    $(obj).parent().parent().find("input[name='maxChildrenCount']").parent().find("span").html("");
                                }
                                if(group.maxPeopleCount!=0){
                                    $(obj).parent().parent().find("input[name='maxPeopleCount']").val(group.maxPeopleCount);
                                    $(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span").html(group.maxPeopleCount);
                                }else{
                                    $(obj).parent().parent().find("input[name='maxPeopleCount']").val("");
                                    $(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span").html("");
                                }
                                $(obj).parent().parent().find("input[name='payDeposit']").val(getFloatPrice(group.payDeposit));
                                if(spanRMlength($(obj).parent().parent().find("input[name='payDeposit']").parent().find("span[class=rm]").length)){
                                    if(group.payDeposit!=0){
                                        $(obj).parent().parent().find("input[name='payDeposit']").parent().append("<span class='rm'><span class='tdorange'>"+(getFloatPrice(group.payDeposit))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='payDeposit']").parent().find("span[class!='rm']").html((getFloatPrice(group.payDeposit)));
                                }

                                $(obj).parent().parent().find("input[name='singleDiff']").val(getFloatPrice(group.singleDiff));
                                if(spanRMlength($(obj).parent().parent().find("input[name='singleDiff']").parent().find("span[class=rm]").length)){
                                    if(group.singleDiff!=0){
                                        $(obj).parent().parent().find("input[name='singleDiff']").parent().append("<span class='rm'><span class='tdred'>"+(getFloatPrice(group.singleDiff))+"</span></span>");
                                    }
                                }else{
                                    $(obj).parent().parent().find("input[name='singleDiff']").parent().find("span[class!='rm']").html((getFloatPrice(group.singleDiff)));
                                }

                                $(obj).parent().parent().find("input[name='planPosition']").val(group.planPosition);
                                $(obj).parent().parent().find("input[name='planPosition']").parent().find("span").html(group.planPosition);

                                $(obj).parent().parent().find("input[name='freePosition']").val(group.freePosition);
                                $(obj).parent().parent().find("input[name='freePosition']").parent().find("span").html(group.freePosition);

                                //0258-新增发票税-列表-s//
                                $(obj).parent().parent().find("input[name='invoiceTax']").val(group.invoiceTax);
                                //将td中第1个span里的内容变为修改后的值.
                                $(obj).parent().parent().find("input[name='invoiceTax']").parent().find("span").eq(0).html(group.invoiceTax+"&nbsp;%");
                                //0258-新增发票税-列表-e//
                                $(obj).parent().parent().find("input[name='visaDate']").val(group.visaDate);
                                $(obj).parent().parent().find("input[name='visaDate']").parent().find("span").html(group.visaDate);

                                $(obj).parent().parent().find("span").show();
                                $(obj).parent().parent().find("input[type='text']").css("display","none");
                                //0258-发票税-列表-s//
                                //将td中发票税文本框后的span进行隐藏
                                $(obj).parent().parent().find("input[name='invoiceTax']").parent().find("span").eq(1).css("display","none");
                                //0258-发票税-列表-e//
                                $(obj).parent().parent().find("input[type='text']").attr("disabled",true);
                                $(obj).parent().parent().find("input[type='checkbox']").attr("disabled",true);

                                //bug12826 ----- begin -------
                                //debugger;
                                if(result.activityKind==10){//游轮
                                    /*游轮新增舱型产生的相同出团日期的団期，团号修改时同步保持一致*/
                                    //当前団期的团号
                                    var $groupCodeCurrency = $("#groupId"+groupId).text();
                                    //当前団期的出团日期
                                    var $groupOpenDateCurrency = $(obj).parent().parent().find("[name='groupOpenDate']").val();
                                    //循环table下的每个出团日期input,和当前值相等时更新团号值
                                    $(obj).parent().parent().parent().parent().find("[name='groupOpenDate']").each(function(i,n){
                                        if(n.value == $groupOpenDateCurrency && $(n).parent().prev().find("[name='groupCode']").val()==result.groupCodeOld){
                                            $(n).parent().prev().find("span").text($groupCodeCurrency);
                                            $(n).parent().prev().find("[name='groupCode']").val($groupCodeCurrency);
                                        }
                                    });
                                }
                                // ----- end -------

                                $(modbtn).show();
                                $(delbtn).show();
                                $(cancelbtn).hide();
                                $(obj).hide();
                                var remark = $(obj).parent().parent().next().find('[name="groupNote"]').val();
                                $(obj).parent().parent().next().find('[name="groupNoteContent"]').text(remark);
                                $(obj).parent().parent().next().find('div:first').show().next().hide();
                            }
                            top.$.jBox.info("更新成功", "提示");
                            //				location.reload() ;
                            $(obj).parent().parent().find('.expandNotes').text('展开备注');
                            $(obj).parent().parent().find('.expandNotes').show();
                            if(isOnlinePage!=null && isOnlinePage=="0"){//产品上架页调用
                                $(obj).parent().parent().next().hide();//隐藏备注行
                            }

                            if (remark == null || remark == 'undefined' || remark == '') {
                                $(obj).parent().parent().find('.groupNoteTipImg').hide();
                            }else{
                                $(obj).parent().parent().find('.groupNoteTipImg').show();
                            }


                        },
                        error:function(){
                            top.$.jBox.info("更新失败", "警告");
                        }
                    });
                }else{
                    top.$.jBox.info("请先修改完错误再提交", "警告");
                }
            }else if (v == 'cancel'){

            }
        });


    }else{

        var validator1 = $(childform).validate();
        //$(childform).removeAllRules();
        var groupId = $(obj).parent().parent().find("input[name='groupid']").val();
        var recommend;
        var recommendCheck = $(obj).parent().parent().find("input[name='recommend'][flag='mod']");
        if(recommendCheck.attr("checked") == "checked"){
            recommend = "1";
        }else{
            recommend = "0";
        }
        $(childtr).addRules();
//	srcActivityId,url,groupId
        if(validator1.form()){
            $.ajax({
                type:"POST",
                url:url+"/activity/manager/savegroup2/"+srcActivityId,
                dataType:"json",
                contentType: "application/x-www-form-urlencoded;charset=utf-8",//默认按照jsp页面的contentType编码来encode,所以这个可以不用设置
                data:{	srcActivityId:srcActivityId,
                    groupid:groupId,
                    recommend:recommend,
                    groupOpenDate:$(obj).parent().parent().find("input[name='groupOpenDate']").val(),
                    groupCloseDate:$(obj).parent().parent().find("input[name='groupCloseDate']").val(),
                    groupCode:$(obj).parent().parent().find("input[name='groupCode']").val(),
                    settlementAdultPrice:$(obj).parent().parent().find("input[name='settlementAdultPrice']").val(),
                    settlementcChildPrice:$(obj).parent().parent().find("input[name='settlementcChildPrice']").val(),
                    settlementSpecialPrice:$(obj).parent().parent().find("input[name='settlementSpecialPrice']").val(),
                    settlementAdultCurrencyType:$(obj).parent().parent().find("input[name=settlementAdultPrice]").parent().find(".rm").text(),//当前币种的符号
                    suggestAdultCurrencyType:$(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find(".rm").text(),
                    suggestAdultPrice:$(obj).parent().parent().find("input[name='suggestAdultPrice']").val(),
                    suggestChildPrice:$(obj).parent().parent().find("input[name='suggestChildPrice']").val(),
                    suggestSpecialPrice:$(obj).parent().parent().find("input[name='suggestSpecialPrice']").val(),
                    //-------t1t2添加quauq价--开始---------------------------------------------------------
                    quauqAdultPrice:$(obj).parent().parent().find("input[name='quauqAdultPrice']").val(),
                    quauqChildPrice:$(obj).parent().parent().find("input[name='quauqChildPrice']").val(),
                    quauqSpecialPrice:$(obj).parent().parent().find("input[name='quauqSpecialPrice']").val(),
                    //-------t1t2添加quauq价---结束--------------------------------------------------------
                    maxChildrenCount:$(obj).parent().parent().find("input[name='maxChildrenCount']").val(),
                    maxPeopleCount:$(obj).parent().parent().find("input[name='maxPeopleCount']").val(),
                    payDeposit:$(obj).parent().parent().find("input[name='payDeposit']").val(),
                    singleDiff:$(obj).parent().parent().find("input[name='singleDiff']").val(),
                    planPosition:$(obj).parent().parent().find("input[name='planPosition']").val(),
                    freePosition:$(obj).parent().parent().find("input[name='freePosition']").val(),

                    //0258需求,新增发票税,展开团期,修改->确认--s//
                    invoiceTax:$(obj).parent().parent().find("input[name='invoiceTax']").val(),
                    //0258需求,新增发票税,展开团期,修改->确认--e//

                    visaDate:$(obj).parent().parent().find("input[name='visaDate']").val(),
                    visaCountry:$(obj).parent().parent().find("input[name='visaCountry']").val(),
                    groupRemark:$(obj).parent().parent().next().find('[name="groupNote"]').val()
                },
                success:function(result){
                    if(result==null){
                        top.$.jBox.info("该团期已经存在", "警告");
                    }else if(result.groupCode=="groupCodeRepeat") {
                        top.$.jBox.info("该团号已经存在", "警告");
                        return;
                    }else if(result.error == "errorChildNum"){
                        top.$.jBox.info("儿童最高人数不能小于团期已报名儿童人数", "警告");
                        return;
                    }else if(result.error == "errorSpecialNum"){
                        top.$.jBox.info("特殊人去最高人数不能小于团期已报名特殊人数", "警告");
                        return;
                    }else{
                        var group = result.group;
                        if(result.groupopendate && result.groupclosedate){
                            if(result.groupopendate == result.groupclosedate){
                                $("#groupdate"+srcActivityId+" #truedate").find("span").html(result.groupopendate);
                            }
                            else{
                                $("#groupdate"+srcActivityId+" #truedate").find("span").html(result.groupopendate+"至"+result.groupclosedate);
                            }

                            $("#groupdate"+srcActivityId+" #truedate").find("a").html("关闭全部团期");
                            $("#groupdate"+srcActivityId+" #truedate").show();
                            $("#groupdate"+srcActivityId+" #falsedate").hide();

                        }
                        else{
                            $("#groupdate"+srcActivityId+" #falsedate").show();
                            $("#groupdate"+srcActivityId+" #truedate").show();
                        }
                        if(result.settlementadultprice){
                            if(result.settlementAdultPriceCMark=="-1"){
                                var settlementAdultPriceCMark =$(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").text();
                                $("#settleadultprice"+srcActivityId).html(settlementAdultPriceCMark+"<span class='tdred fbold'>"+$(document).commafy(result.settlementadultprice)+"</span>起");
                            }else{
                                $("#settleadultprice"+srcActivityId).html(result.settlementAdultPriceCMark+"<span class='tdred fbold'>"+$(document).commafy(result.settlementadultprice)+"</span>起");
                            }
                        }else{
                            $("#settleadultprice"+srcActivityId).html("价格待定");
                        }
//					if(result.trekiz)
//						$("#trekiz"+srcActivityId).html("¥"+"<span style=\"color:#eb0301;font-size:20px;\">"+$(document).commafy(result.trekiz)+"</span>起");
//					else
//						$("#trekiz"+srcActivityId).html("价格待定");
                        if(result.suggestadultprice){
                            if(result.suggestAdultPriceCMark=="-1"){
                                var suggestAdultPriceCMark =$(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").text();
                                $("#suggestadultprice"+srcActivityId).html(suggestAdultPriceCMark+"<span class='tdblue fbold'>"+$(document).commafy(result.suggestadultprice)+"</span>起");
                            }else{
                                $("#suggestadultprice"+srcActivityId).html(result.suggestAdultPriceCMark+"<span class='tdblue fbold'>"+$(document).commafy(result.suggestadultprice)+"</span>起");
                            }
//						$("#suggestadultprice"+srcActivityId).html("¥"+"<span class='tdblue fbold'>"+$(document).commafy(result.suggestadultprice)+"</span>起");
                            //$("#suggestadultprice"+srcActivityId).find("span").text($(document).commafy(result.suggestadultprice));
                        }else{
                            $("#suggestadultprice"+srcActivityId).html("价格待定");
                        }

                        if(group.recommend=='1'){
                            $(obj).parent().parent().find("input[name='recommend']").attr("checked","checked");
                        }
                        if(group.recommend=='0'){
                            $(obj).parent().parent().find("input[name='recommend']").attr("checked",false);
                        }

                        $(obj).parent().parent().find("input[name='groupid']").css("display","none").attr("disabled",true);
                        var groupOpenDate = $(obj).parent().parent().find("input[name='groupOpenDate']")[0];
                        $(groupOpenDate).val(group.groupOpenDate);
                        $(groupOpenDate).prev().prev().html(group.groupOpenDate);

                        $(obj).parent().parent().find("input[name='groupCloseDate']").val(group.groupCloseDate);
                        $(obj).parent().parent().find("input[name='groupCloseDate']").parent().find("span").html(group.groupCloseDate);

                        $(obj).parent().parent().find("input[name='visaCountry']").val(group.visaCountry);
                        $(obj).parent().parent().find("input[name='visaCountry']").parent().find("span").html(group.visaCountry);

                        $(obj).parent().parent().find("input[name='groupCode']").val(group.groupCode);
                        $(obj).parent().parent().find("input[name='groupCode']").parent().find("span").html(group.groupCode);
                        $(obj).parent().parent().find("input[name='settlementAdultPrice']").val(getFloatPrice(group.settlementAdultPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class=rm]").length)){
                            if(group.settlementAdultPrice!=0){
                                $(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().append("<span class='tdred'>"+getFloatPrice(group.settlementAdultPrice)+"</span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='settlementAdultPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementAdultPrice)));
                        }

                        $(obj).parent().parent().find("input[name='settlementcChildPrice']").val(getFloatPrice(group.settlementcChildPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().find("span[class=rm]").length)){
                            if(group.settlementcChildPrice!=0){
                                $(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().append("<span class='rm'><span class='tdred'>"+(getFloatPrice(group.settlementcChildPrice))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='settlementcChildPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementcChildPrice)));
                        }


                        $(obj).parent().parent().find("input[name='settlementSpecialPrice']").val(getFloatPrice(group.settlementSpecialPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().find("span[class=rm]").length)){
                            if(group.settlementSpecialPrice!=0){
                                $(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().append("<span class='rm'><span class='tdred'>"+(getFloatPrice(group.settlementSpecialPrice))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='settlementSpecialPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.settlementSpecialPrice)));
                        }

//			$(obj).parent().parent().find("input[name='trekizPrice']").val(group.trekizPrice==0?"":group.trekizPrice);
//			$(obj).parent().parent().find("input[name='trekizPrice']").parent().find("span").html("¥"+(group.trekizPrice==0?"":group.trekizPrice));

//			$(obj).parent().parent().find("input[name='trekizChildPrice']").val(group.trekizChildPrice==0?"":group.trekizChildPrice);
//			$(obj).parent().parent().find("input[name='trekizChildPrice']").parent().find("span").html("¥"+(group.trekizChildPrice==0?"":group.trekizChildPrice));

                        $(obj).parent().parent().find("input[name='suggestAdultPrice']").val(getFloatPrice(group.suggestAdultPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class=rm]").length)){
                            if(group.suggestAdultPrice!=0){
                                $(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>"+(getFloatPrice(group.suggestAdultPrice))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='suggestAdultPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestAdultPrice)));
                        }

                        $(obj).parent().parent().find("input[name='suggestChildPrice']").val(getFloatPrice(group.suggestChildPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='suggestChildPrice']").parent().find("span[class=rm]").length)){
                            if(group.suggestChildPrice!=0){
                                $(obj).parent().parent().find("input[name='suggestChildPrice']").parent().append("<span class='rm'><span class='tdblue'>"+(getFloatPrice(group.suggestChildPrice))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='suggestChildPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestChildPrice)));
                        }

                        $(obj).parent().parent().find("input[name='suggestSpecialPrice']").val(getFloatPrice(group.suggestSpecialPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().find("span[class=rm]").length)){
                            if(group.suggestSpecialPrice!=0){
                                $(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>"+(getFloatPrice(group.suggestSpecialPrice))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='suggestSpecialPrice']").parent().find("span[class!='rm']").html((getFloatPrice(group.suggestSpecialPrice)));
                        }


                        //----quauq价修改---start-----------------------------------------------------
                        $(obj).parent().parent().find("input[name='quauqAdultPrice']").val(getFloatPriceForQuauq(result.quauqAdultPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class=rm]").length)){
                            if(result.quauqAdultPrice!=0){
                                $(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqAdultPrice)+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='quauqAdultPrice']").parent().find("span[class!='rm']").html((getFloatPriceForQuauq(result.quauqAdultPrice)));
                        }

                        $(obj).parent().parent().find("input[name='quauqChildPrice']").val(getFloatPriceForQuauq(result.quauqChildPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class=rm]").length)){
                            if(result.quauqChildPrice!=0){
                                $(obj).parent().parent().find("input[name='quauqChildPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqChildPrice)+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='quauqChildPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(result.quauqChildPrice));
                        }

                        $(obj).parent().parent().find("input[name='quauqSpecialPrice']").val(getFloatPriceForQuauq(result.quauqSpecialPrice));
                        if(spanRMlength($(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class=rm]").length)){
                            if(result.quauqSpecialPrice!=0){
                                $(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqSpecialPrice)+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='quauqSpecialPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(result.quauqSpecialPrice));
                        }
                        //----quauq价修改---end-----------------------------------------------------


                        //----供应价修改---start-----------------------------------------------------
                        if(result.quauqAdultPrice!=null){
                            $(obj).parent().parent().find("input[name='supplyAdultPrice']").val(getFloatPriceForQuauq(result.quauqAdultPrice*rate));
                            if(spanRMlength($(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().find("span[class=rm]").length)){
                                if(result.quauqAdultPrice!=0){
                                    $(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqAdultPrice*rate)+"</span></span>");
                                }
                            }else{
                                $(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().find("span[class!='rm']").html((getFloatPriceForQuauq(result.quauqAdultPrice*rate)));
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='supplyAdultPrice']").val("");
                            $(obj).parent().parent().find("input[name='supplyAdultPrice']").parent().find("span[class!='rm']").html("");
                        }

                        if(result.quauqChildPrice!=null){
                            $(obj).parent().parent().find("input[name='supplyChildPrice']").val(getFloatPriceForQuauq(result.quauqChildPrice*rate));
                            if(spanRMlength($(obj).parent().parent().find("input[name='supplyChildPrice']").parent().find("span[class=rm]").length)){
                                if(result.quauqChildPrice!=0){
                                    $(obj).parent().parent().find("input[name='supplyChildPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(result.quauqChildPrice*rate)+"</span></span>");
                                }
                            }else{
                                $(obj).parent().parent().find("input[name='supplyChildPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(result.quauqChildPrice*rate));
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='supplyChildPrice']").val("");
                            $(obj).parent().parent().find("input[name='supplyChildPrice']").parent().find("span[class!='rm']").html("");
                        }

                        if(result.quauqSpecialPrice!=null){
                            $(obj).parent().parent().find("input[name='supplySpecialPrice']").val(getFloatPriceForQuauq(group.quauqSpecialPrice*rate));
                            if(spanRMlength($(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().find("span[class=rm]").length)){
                                if(result.quauqSpecialPrice!=0){
                                    $(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().append("<span class='rm'><span class='tdblue'>"+getFloatPriceForQuauq(group.quauqSpecialPrice*rate)+"</span></span>");
                                }
                            }else{
                                $(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().find("span[class!='rm']").html(getFloatPriceForQuauq(group.quauqSpecialPrice*rate));
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='supplySpecialPrice']").val("");
                            $(obj).parent().parent().find("input[name='supplySpecialPrice']").parent().find("span[class!='rm']").html("");
                        }
                        //----供应价修改---end-----------------------------------------------------




//			$(obj).parent().parent().find("input[name='maxPeopleCount']").val(group.maxPeopleCount);
//			if(spanRMlength($(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span[class=rm]").length)){
//				if(group.payDeposit!=0){
//					$(obj).parent().parent().find("input[name='maxPeopleCount']").parent().append("<span class='rm'><span class='tdorange'>"+(group.maxPeopleCount)+"</span></span>");
//				}
//			}else{
//				$(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span[class!='rm']").html((group.maxPeopleCount));
//			}
//

                        if(group.maxChildrenCount!=0){
                            $(obj).parent().parent().find("input[name='maxChildrenCount']").val(group.maxChildrenCount);
                            $(obj).parent().parent().find("input[name='maxChildrenCount']").parent().find("span").html(group.maxChildrenCount);
                        }else{
                            $(obj).parent().parent().find("input[name='maxChildrenCount']").val("");
                            $(obj).parent().parent().find("input[name='maxChildrenCount']").parent().find("span").html("");
                        }
                        if(group.maxPeopleCount!=0){
                            $(obj).parent().parent().find("input[name='maxPeopleCount']").val(group.maxPeopleCount);
                            $(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span").html(group.maxPeopleCount);
                        }else{
                            $(obj).parent().parent().find("input[name='maxPeopleCount']").val("");
                            $(obj).parent().parent().find("input[name='maxPeopleCount']").parent().find("span").html("");
                        }
                        $(obj).parent().parent().find("input[name='payDeposit']").val(getFloatPrice(group.payDeposit));
                        if(spanRMlength($(obj).parent().parent().find("input[name='payDeposit']").parent().find("span[class=rm]").length)){
                            if(group.payDeposit!=0){
                                $(obj).parent().parent().find("input[name='payDeposit']").parent().append("<span class='rm'><span class='tdorange'>"+(getFloatPrice(group.payDeposit))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='payDeposit']").parent().find("span[class!='rm']").html((getFloatPrice(group.payDeposit)));
                        }

                        $(obj).parent().parent().find("input[name='singleDiff']").val(getFloatPrice(group.singleDiff));
                        if(spanRMlength($(obj).parent().parent().find("input[name='singleDiff']").parent().find("span[class=rm]").length)){
                            if(group.singleDiff!=0){
                                $(obj).parent().parent().find("input[name='singleDiff']").parent().append("<span class='rm'><span class='tdred'>"+(getFloatPrice(group.singleDiff))+"</span></span>");
                            }
                        }else{
                            $(obj).parent().parent().find("input[name='singleDiff']").parent().find("span[class!='rm']").html((getFloatPrice(group.singleDiff)));
                        }

                        $(obj).parent().parent().find("input[name='planPosition']").val(group.planPosition);
                        $(obj).parent().parent().find("input[name='planPosition']").parent().find("span").html(group.planPosition);

                        $(obj).parent().parent().find("input[name='freePosition']").val(group.freePosition);
                        $(obj).parent().parent().find("input[name='freePosition']").parent().find("span").html(group.freePosition);

                        //0258-新增发票税-列表-s//
                        $(obj).parent().parent().find("input[name='invoiceTax']").val(group.invoiceTax);
                        //将td中第1个span里的内容变为修改后的值.
                        $(obj).parent().parent().find("input[name='invoiceTax']").parent().find("span").eq(0).html(group.invoiceTax+"&nbsp;%");
                        //0258-新增发票税-列表-e//

                        $(obj).parent().parent().find("input[name='visaDate']").val(group.visaDate);
                        $(obj).parent().parent().find("input[name='visaDate']").parent().find("span").html(group.visaDate);

                        $(obj).parent().parent().find("span").show();
                        $(obj).parent().parent().find("input[type='text']").css("display","none");

                        //0258-发票税-列表-s//
                        //将td中发票税文本框后的span进行隐藏
                        $(obj).parent().parent().find("input[name='invoiceTax']").parent().find("span").eq(1).css("display","none");
                        //0258-发票税-列表-e//

                        $(obj).parent().parent().find("input[type='text']").attr("disabled",true);
                        $(obj).parent().parent().find("input[type='checkbox']").attr("disabled",true);

                        //bug12826 ----- begin -------
                        //debugger;
                        if(result.activityKind==10){//游轮
                            /*游轮新增舱型产生的相同出团日期的団期，团号修改时同步保持一致*/
                            //当前団期的团号
                            var $groupCodeCurrency = $("#groupId"+groupId).text();
                            //当前団期的出团日期
                            var $groupOpenDateCurrency = $(obj).parent().parent().find("[name='groupOpenDate']").val();
                            //循环table下的每个出团日期input,和当前值相等时更新团号值
                            $(obj).parent().parent().parent().parent().find("[name='groupOpenDate']").each(function(i,n){
                                if(n.value == $groupOpenDateCurrency && $(n).parent().prev().find("[name='groupCode']").val()==result.groupCodeOld){
                                    $(n).parent().prev().find("span").text($groupCodeCurrency);
                                    $(n).parent().prev().find("[name='groupCode']").val($groupCodeCurrency);
                                }
                            });
                        }
                        // ----- end -------

                        $(modbtn).show();
                        $(delbtn).show();
                        $(cancelbtn).hide();
                        $(obj).hide();
                        var remark = $(obj).parent().parent().next().find('[name="groupNote"]').val();
                        $(obj).parent().parent().next().find('[name="groupNoteContent"]').text(remark);
                        $(obj).parent().parent().next().find('div:first').show().next().hide();
                    }
                    top.$.jBox.info("更新成功", "提示");
//				location.reload() ;
                    $(obj).parent().parent().find('.expandNotes').text('展开备注');
                    $(obj).parent().parent().find('.expandNotes').show();
                    if(isOnlinePage!=null && isOnlinePage=="0"){//产品上架页调用
                        $(obj).parent().parent().next().hide();//隐藏备注行
                    }

                    if (remark == null || remark == 'undefined' || remark == '') {
                        $(obj).parent().parent().find('.groupNoteTipImg').hide();
                    }else{
                        $(obj).parent().parent().find('.groupNoteTipImg').show();
                    }


                },
                error:function(){
                    top.$.jBox.info("更新失败", "警告");
                }
            });
        }else{
            top.$.jBox.info("请先修改完错误再提交", "警告");
        }

    }
}





function spanRMlength(length){
    if(length==0){
        return true;
    }else{
        return false;
    }
}


//把后台传回来的价格信息变成带有两位小数的数字
function getFloatPrice(obj) {
    var price = obj + "";
    if(0 == price.length || null == price || 0 == price) {
        return "";
    }else{
        if(price.indexOf(".") < 0) {
            return xround(obj,2);
        }else{
            return xround(obj,2);
        }
    }
}

/**
 * 转化后台数据quauq价  转化规则:null和""转化为""  数字字符串转化为数字
 */
function getFloatPriceForQuauq(obj){
    if(obj == null){
        return "";
    }else{
        var price = obj + "";
        if(0 == price.length || null == price) {
            return "";
        }else{
            return xround(obj,2);
        }
    }
}




/**
 *四舍五入，保留位数为
 *num:要格式化的数字
 *scale: 保留的位数
 */
function xround(num,scale){
    var resultTemp = Math.round(num * Math.pow(10, scale)) / Math.pow(10, scale);
    return resultTemp.toFixed(2);
}



function transportSelect(){
    $(".transport_city").delegate(".transportDel","click",function(){
        $(this).parent().remove();
    });
    //发布单团产品--填写基础信息--联运币种选择
    $(".transport_city").on("change","select[name=templateCurrency]",function(){
        var $this = $(this);
        var theValue = $this.val();
        var oldCurrency = $this.attr("nowClass");
        var newCurrency = $this.children("option:selected").attr("addClass");
        var $currency = $this.siblings(".currency").eq(0);
        if(0 != $currency.length){
            var txt_currency = $this.find("option:selected").text() == "人民币" ? "元" : $this.find("option:selected").text();
            $currency.html(txt_currency);
        }
        $this.next("input[type=text]").removeClass(oldCurrency).addClass(newCurrency);
        $this.attr("nowClass",newCurrency);
    });
}

//发布单团产品--添加团期和价格--币种选择
function selectCurrency(){
    //单个设置币种
    $(".choose-currency").hover(function(){
        $(this).addClass("choose-currency-on");
    },function(){
        $(this).removeClass("choose-currency-on");
    }).on("click","dd p",function(){
        var $this = $(this);
        if(!$this.hasClass("p-checked")){
            var oldCurrency = $this.siblings("p.p-checked").text();
            var oldClass = $this.siblings("p.p-checked").attr("name");
            $this.addClass("p-checked").siblings("p").removeClass("p-checked");
            //设置币种/人
            var $currency = $this.parents(".add2_nei_table").next("td").find(".currency").eq(0);
            if(0 != $currency.length){
                var txt_currency = $this.text() == "人民币" ? "元" : $this.text();
                if("人民币" == oldCurrency){oldCurrency = "元";}//alert(oldCurrency);
                $currency.html($currency.html().replace(oldCurrency,txt_currency));
            }
            //设置币种图标
            var $input_currency = $this.parents(".add2_nei_table").next("td").find(".ipt-currency").eq(0);
            $input_currency.prev("span").remove().end().before("<span>"+$this.attr('addClass')+"</span>");
            //设置对应的select的选中项
            var $select = $this.parents("td.add2_nei_table").find("select");
            $select.find("option:selected").removeAttr("selected");
            $select.children("option[value=" + $this.attr("value") + "]").attr("selected",true);
        }
    });
    //统一设置币种
    $("#selectCurrency").change(function(){
        var theValue = $(this).val();
//		$(this).next("span").html("汇率：" + $(this).children("option:selected").attr("var"));
        $.each($(".choose-currency"),function(index,element){
            var $this_dl = $(element);
            $this_dl.find("dd p[id=" + theValue + "]").click();
        });
    });
}

//生成币种ID字符串
function createCurrencyIdStr() {
    $("#contentTable").children("tbody").find("tr").each(function(index,obj){
        var currencyIdStr = "";
        $(obj).find("input").each(function(index1,obj1) {
            if($(obj1).hasClass("ipt-currency")) {
                currencyIdStr += $(obj1).attr("var") + ",";
            }
        });
        currencyIdStr = currencyIdStr.substring(0,currencyIdStr.length-1);
        if($(obj).find("[name='groupCurrencyType']").length != 0) {
            $(obj).find("[name='groupCurrencyType']").remove();
        }
        $(obj).append("<input type='hidden' name='groupCurrencyType' value='"+currencyIdStr+"' />");
    });
}

function flashChecker()
{
    var hasFlash=0;     //是否安装了flash
    var flashVersion=0;   //flash版本

    if(document.all){
        var swf ;
        try
        {
            swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
        } catch (e) {
            hasFlash=1;
            alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
        }
        if(swf) {
            hasFlash=1;
            VSwf=swf.GetVariable("$version");
            flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]);
        }
    }else{
        if (navigator.plugins && navigator.plugins.length > 0){
            var swf=navigator.plugins["Shockwave Flash"];
            if (swf)  {
                hasFlash=1;
                var words = swf.description.split(" ");
                for (var i = 0; i < words.length; ++i){
                    if (isNaN(parseInt(words[i]))) continue;
                    flashVersion = parseInt(words[i]);
                }
            }
        }
    }
    return {f:hasFlash,v:flashVersion};
}

//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
function uploadFiles(ctx, inputId, obj) {
    var fls=flashChecker();
    var s="";
    if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
    } else {
        alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
        return;
    }

    //新建一个隐藏的div，用来保存文件上传后返回的数据
    if($(obj).parent().find(".uploadPath").length == 0) {
        $(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
        $(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');
    }

    $(obj).addClass("clickBtn");

    /*移除产品行程校验提示信息label标签*/
    $("#modIntroduction").remove();

    $.jBox("iframe:"+ ctx +"/activity/manager/uploadFilesPage", {
        title: "多文件上传",
        width: 340,
        height: 365,
        buttons: {'关闭':true},
        persistent:true,
        loaded: function (h) {},
        submit: function (v, h, f) {
            $(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
            if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
                /*添加<li>之前，先将之前的<li>删除，然后再累加，以防止重复累加问题*/
//				if($(obj).attr("name") != 'costagreement'){
//					$(obj).next(".batch-ol").find("li").remove();
//				}

                $(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
                    //如果是产品行程介绍
                    if($(obj).attr("name") == 'introduction') {
                        $(obj).next().next("#introductionVaildator").val("true").trigger("blur");
                    }
                    //如果是签证资料的文件上传
                    if($(obj).attr("name").indexOf("signmaterial") >= 0) {
                        $(obj).parent().parent().parent().parent().next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:173px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span><a class="batchDel" style="float:left;" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a></li>');
                    }else{
                        $(obj).next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:173px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span><a class="batchDel"  style="float:left;" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><br></li>');
                    }
                });
                if($(obj).parent().find("#currentFiles").children().length != 0) {
                    $(obj).parent().find("#currentFiles").children().remove();
                }
            }

            $(".clickBtn",window.parent.document).removeClass("clickBtn");
        }
    });
    $("#jbox-content").css("overflow", "hidden");
    $(".jbox-close").hide();
}
//上传单个文件
function uploadSingleFile(ctx, inputId, obj) {
    var fls=flashChecker();
    var s="";
    if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
    } else {
        alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
        return;
    }

    //新建一个隐藏的div，用来保存文件上传后返回的数据
    if($(obj).parent().find(".uploadPath").length == 0) {
        $(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
        $(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');
    }

    $(obj).addClass("clickBtn");

    /*移除产品行程校验提示信息label标签*/
    $("#modIntroduction").remove();

    $.jBox("iframe:"+ ctx +"/activity/manager/uploadSingleFilePage", {
        title: "单文件上传",
        width: 330,
        height: 400,
        buttons: {'关闭':true},
        persistent:true,
        loaded: function (h) {
        },
        submit: function (v, h, f) {
            $(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
            if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
                $(obj).next(".batch-ol").children().remove();
                $(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
                    $(obj).next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:110px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span>' +
                        '<a class="batchDl" href="javascript:void(0)" onClick="downloads(\''+$(obj1).prev().val()+'\')">下载</a>' +
                        '<a class="batchDel"  style="margin-left: 2px" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><li>');
                });
                if($(obj).parent().find("#currentFiles").children().length != 0) {
                    $(obj).parent().find("#currentFiles").children().remove();
                }
            }

            $(".clickBtn",window.parent.document).removeClass("clickBtn");
        }
    }).find("#jbox-content").css("overflow-y", "hidden");
    //$("#jbox-content").find("body").css("height", "200px");
    $(".jbox-close").hide();
}

// QU-SDP-微信分销模块start 追加上传单张图片接口添加图片类型，图片大小两个参数  yang.gao 2017-01-06
// 上传单个文件追加两个参数（允许上传的图片类型，允许上传的图片大小最大值）
function uploadSingleFileByParam(ctx, inputId, obj, type, maxSize) {
    var fls=flashChecker();
    var s="";
    if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
    } else {
        alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
        return;
    }

    //新建一个隐藏的div，用来保存文件上传后返回的数据
    if($(obj).parent().find(".uploadPath").length == 0) {
        $(obj).parent().append('<div class="uploadPath" style="display: none" ></div>');
        $(obj).parent().append('<div id="currentFiles" style="display: none" ></div>');
    }

    $(obj).addClass("clickBtn");

    /*移除产品行程校验提示信息label标签*/
    $("#modIntroduction").remove();

    $.jBox("iframe:"+ ctx +"/activity/manager/uploadSingleFilePageByParam/" + type + "/" + maxSize + "", {
        title: "单文件上传",
        width: 330,
        height: 400,
        buttons: {'关闭':true},
        persistent:true,
        loaded: function (h) {
        },
        submit: function (v, h, f) {
            $(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
            if($(obj).parent().find("#currentFiles").find("[name='docOriName']").length != 0) {
                $(obj).next(".batch-ol").children().remove();
                $(obj).parent().find("#currentFiles").find("[name='docOriName']").each(function(index, obj1) {
                    $(obj).next(".batch-ol").append('<li><span style="overflow:hidden;text-overflow:ellipsis;white-space:nowrap;max-width:110px;display:inline-block;float: left;" title="'+$(obj1).val() +'">'+ $(obj1).val() +'</span>' +
                        '<a class="batchDel"  style="margin-left: 2px" href="javascript:void(0)" onclick="deleteFileInfo(\''+ $(obj1).prev().val() +'\',\''+ $(obj).attr("name") +'\',this);">删除</a><li>');
                });
                if($(obj).parent().find("#currentFiles").children().length != 0) {
                    $(obj).parent().find("#currentFiles").children().remove();
                }
            }

            $(".clickBtn",window.parent.document).removeClass("clickBtn");
        }
    }).find("#jbox-content").css("overflow-y", "hidden");
    //$("#jbox-content").find("body").css("height", "200px");
    $(".jbox-close").hide();
}
// QU-SDP-微信分销模块end

//删除现有的文件
function deleteFileInfo(inputVal, objName, obj) {
    top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
        if(v=='ok'){
            if(inputVal != null && objName != null) {
                var delInput = $(".uploadPath").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
                delInput.next().eq(0).remove();
                delInput.next().eq(0).remove();
                delInput.remove();

                /*删除上传文件后，文件信息会存放在id为currentFiles的div中，也需要把该div相关的上传文件信息删除*/
                var docName = $(obj).parent("li").parent("ol").parent().find("#currentFiles").find("input[name='"+ objName +"'][value='"+ inputVal +"']");
                docName.next().eq(0).remove();
                docName.next().eq(0).remove();
                docName.remove();


            }else if(inputVal == null && objName == null) {
                $(obj).parent().remove();
            }
            $(obj).parent("li").remove();

            //如果是产品行程介绍文件删除的话，需要进行必填验证
            if("introduction" == objName) {
                if(0 == $("#introductionVaildator").prev(".batch-ol").find("li").length) {
                    $("#introductionVaildator").val("").trigger("blur");
                }
            }
        }
    },{buttonsFocus:1});
    top.$('.jbox-body .jbox-icon').css('top','55px');
}

//发布产品-交通方式-航空-关联机票产品
//机票产品信息是否为空,默认是
var isBlank;
function linkAirTicket1(ctx, oldProductCode){
    if(!oldProductCode) {
        oldProductCode = "";
    }
    var html = '<div class="add_allactivity"><label>输入机票产品编号：</label>';
    html += '<input type="text" id="productCode" value="' + oldProductCode + '"/>';
    html += '</div>';
    isBlank = true;
    $.jBox(html, { title: "选择机票产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
        if (v=="1"){
            var productCode = $("#productCode").val();
            if(productCode) {
                getAirTicketInfo(productCode, ctx);
                //如果没有机票产品
                if(isBlank) {
                    h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
                    $('<p class="nothisPro" style="display: none;">没有这个产品</p>').appendTo(h).show('slow');
                    return false;
                }else{
                    $.jBox.info('已完成机票关联','系统提示');
                }
            }else{
                h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
                $('<p class="nothisPro" style="display: none;">必填信息</p>').appendTo(h).show('slow');
                return false;
            }
        }
    },height:180,width:500});
}

function getAirTicketInfo(productCode, ctx) {
    $.ajax({
        type:"POST",
        url: ctx + "/activity/manager/getAirticketByProCode?productCode="+productCode,
        async : false,
        success:function(result){
            if(result != null && result.length != 0) {
                isBlank = false;
                //清空先前查询的机票产品信息
                if($(".airInfo").find("div").length != 0) {
                    $(".airInfo-tit1").remove();
                    $(".airInfo-con").remove();
                    $(".otherInfo").remove();
                }
                var airticketAreaType;
                var ticketAreaTypeArr = ["","内陆","国际","内陆+国际","国内"];
                switch(result[0].airType) {
                    case '3':airticketAreaType = "单程" + ticketAreaTypeArr[result[1].ticketAreaType]; break;
                    case '2':airticketAreaType = "往返"; break;
                    case '1':airticketAreaType = "多段"; break;
                    default:airticketAreaType = "无";
                }
                $(".airInfo").append('<div class="airInfo-tit1">航空行程：'+ result[0].leaveCountry + '-' + result[0].destination + '(' + airticketAreaType + ')</div>');
                var outerHtml = $('<div class="airInfo-con"></div>');
                //航段数中文只能处理十以内
                var charArr = ["零","一","二","三","四","五","六","七","八","九","十"];
                var innerHtmlArr = new Array(result.length);
                for(var innerCount = 1; innerCount < result.length;) {
                    var innerData = result[innerCount];
                    var innerHtml = $('<div class="title_samil">第' + charArr[innerData.number] + '段：' + ticketAreaTypeArr[result[innerCount].ticketAreaType] + '</div>'
                        + '<div class="seach25"><p>航空公司：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.airlines
                        + '</span></p></div><div class="seach25"><p>航班号：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.flightNumber
                        + '</span></p></div><div class="kong"></div><div class="seach25"><p>出发城市：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.leaveAirport
                        + '</span></p></div><div class="seach25"><p>出发时刻：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.startTime
                        + '</span></p></div><div class="seach25"><p>舱位等级：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.spaceGrade
                        + '</span></p></div><div class="kong"></div><div class="seach25"><p>到达城市：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.destinationAirpost
                        + '</span></p></div><div class="seach25"><p>到达时刻：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.arrivalTime
                        + '</span></p></div><div class="seach25"><p>舱位：</p><p class="seach_r"><span class="disabledshowspan">'
                        + innerData.airspace
                        + '</span></p></div>');
                    innerHtmlArr[parseInt(innerData.number)] = innerHtml;
                    innerCount++;
                }
                for(var keyCon = 1; keyCon <= innerHtmlArr.length; keyCon++) {
                    $(outerHtml).append(innerHtmlArr[keyCon]);
                }
                var ht="<div class=\"otherInfo\"><div><p class=\"seach_r\">联运类型：<span class=\"disabledshowspan\">"+ result[0].intermodalType+"</span></p></div><div ><p  class=\"seach_r\">出票日期：" +
                    "<span class=\"disabledshowspan\">"+result[0].outTicketTime+"</span></p></div><div><p  class=\"seach_r\">预收人数：" +
                    "<span class=\"disabledshowspan\">"+result[0].reservationsNum+"</span></p></div></div>" ;
                $(".airInfo").append(outerHtml).append(ht);
                if($(".activityAirTicketId").length != 0) {
                    $(".activityAirTicketId").val(result[0].airticketId);
                }
            }
        }
    });
}

//出团通知文件上传    0234 屏蔽上传出团通知
//function uploadGroupFile(ctx, obj) {
//	$(obj).addClass("clickA");
//	var docId = "";
//	if($(obj).attr("id") != undefined && $(obj).attr("id") != "")
//		docId = $(obj).attr("id");
//
//    var iframe = "iframe:" + ctx + "/activity/manager/uploadFileForm?docId="+docId;
//    $.jBox(iframe, {
//            title: "上传出团通知",
//            width: 350,
//            height: 300,
//            buttons: {}
//     });
//    $("#jbox-content").css("overflow", "hidden");
//     return false;
//}

function validatorFloat(obj){
    var money = obj.value;
    if(money=="") {obj.value="0"};
    var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
    var txt = ms.split(".");
    if("" == txt[0]){
        txt[0] = "0";
    }
    obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
}

(function($){
    $.validator.addMethod("areaRequired", function(value,element){
        var inputValue = $(element).next().find(".custom-combobox-input").val();
        if(" " == value || ("" == inputValue)){
            return false;
        }else{
            return true;
        }
    },"必填信息");
})(jQuery);


function replaceStr(obj) {
    var selectionStart = obj.selectionStart;
    //先将全角转换成半角(全角括号除外)
    var tmp = "";
    for (var i = 0; i < obj.value.length; i++) {
        if (obj.value.charCodeAt(i) > 65248 && obj.value.charCodeAt(i) < 65375 && obj.value.charCodeAt(i) != 65288 && obj.value.charCodeAt(i) != 65289) {
            tmp += String.fromCharCode(obj.value.charCodeAt(i) - 65248);
        } else {
            tmp += String.fromCharCode(obj.value.charCodeAt(i));
        }
    }
    obj.value = tmp;
    //删除掉规定外的字符
    obj.value = obj.value.replace(/[^\u4e00-\u9fa5\w\(\)\（\）\-\+/\—]/g, '');
    //设置光标的位置
    if(obj.setSelectionRange)
    {
        obj.focus();
        obj.setSelectionRange(selectionStart,selectionStart);
    }
    else if (obj.createTextRange) {
        var range = obj.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionStart);
        range.moveStart('character', selectionStart);
        range.select();
    }


    $("#groupCode").attr("title", $("#groupCode").val());
}
