var contextPath;
$(function () {
	contextPath = $("#ctx").val();	
	
    $('.closeNotice').click(function () {
        var par = $(this).parent().parent();
        par.hide();
        par.prev().removeClass('border-bottom');
        par.prev().find('.notice-date').show();
    });
    $('.showNotice').click(function () {
        $(this).parent().hide();
        var par = $(this).parent().parent();
        par.addClass('border-bottom');
        par.next().show();
    });
    $('.main-nav li').click(function () {
        $(this).addClass('select').siblings().removeClass('select');
    });
});

$(window).load(function() {
	//等数据加载完成后，删除网页中rowspan等于0的html
	$("#costTable2 td[rowspan=0]").each(function(index, obj) {
		$(this).removeAttr("rowspan");
	});
})
//切换渠道的次数
var changeAgentTime = 0;
$(document).ready(function (e) {
    var leftmenuid = $("#leftmenuid").val();
    $(".main-nav").find("li").each(function (index, element) {
        if ($(this).attr("menuid") == leftmenuid) {
            $(this).css("background", "url(/resource/images/img/pfs_nav_hover.png)");
        }
    });
    //渠道与非渠道之间切换
    $("#channelType").on("change", function () {
    	//切换到签约渠道    	
        if ($("#signChannel").is(":hidden")) {
            $("#signChannel").show();
            $("#signChannelList").show();
            $("#nonChannel").hide();
            $("#nonChannelList").hide();
            //如果是第一次切换渠道，则要使用排序第一的渠道
            if(changeAgentTime == 0){
            	getAllContactsByAgentId(firstAgentId);
            }
            changeAgentTime++;
        } else {
            $("#signChannel").hide();
            $("#signChannelList").hide();
            $("#nonChannel").show();
            $("#nonChannelList").show();
            if(changeAgentTime == 0){            	
            	$("#orderCompanyName").val("");
            }
            changeAgentTime++;
        }
    });
    // 联系人模板
    var $contactsTemp = $("#orderpersonMesdtail ul:first").clone();
    //旧的增删联系人按钮
//    $("#orderpersonMesdtail").on("click", "span.yd1AddPeople", function () {
//        var $newContacts = $contactsTemp.clone();
//        	$newContacts.find("input[name='contactsId']").val("");
//        	$newContacts.find('span.yd1AddPeople').replaceWith('<span class="ydbz_x yd1DelPeople gray">删除联系人</span>');
//        var $container = $("#signChannelList").has(this);
//        	$container.append($newContacts);
//    }).on("click", "span.yd1DelPeople", function () {
//        $(this).parent().parent().remove();
//        var orderContactsId = $(this).attr("data");
//        //删除联系人
//        if(orderContactsId && orderContactsId != null && orderContactsId != ''){
//        	delete_contacts(orderContactsId);
//        }
//    });
    $("#passengerInfoTable").on('click', 'i.price_sale_house_01', function () {
        // 添加签证
        var tempP = $(this).parent().clone();
        var inputHidden_visaUuid =  tempP.find("input[name='travelerVisaUuid']");	//签证
        if(inputHidden_visaUuid != undefined){
        	inputHidden_visaUuid.val("");
        }
        var inputHidden_papersUuid =  tempP.find("input[name='travelerPapersUuid']");//证件类型
        if(inputHidden_papersUuid != undefined){
        	inputHidden_papersUuid.val("");
        }
        var inputHidden_moneyUuid =  tempP.find("input[name='travelerMoneyUuid']");	//价格
        if(inputHidden_moneyUuid != undefined){
        	inputHidden_moneyUuid.val("");
        }
        tempP.find('i.price_sale_house_01').replaceWith('<i class="price_sale_house_02"></i>');
        $(this).parent().parent().append(tempP);
    }).on('click', 'i.price_sale_house_02', function () {
        // 删除签证
        $(this).parent().remove();
    });
//    .on('click', "a.delLink", function () {
//        //删除游客信息
//        $('#passengerInfoTable tr').has(this).remove();
//        updateSequence();
//    });
    $("#costTable").on("change", "input:text", function () {
        peopleCountChange();
    });
    $("#adjustCost").on("change", "input:text.price", function () {
        offersChange();
    }).on("change", "input:radio", function () {
        offersChange();
    }).on("change", "select", function () {
        offersChange();
    });
    $("#avg_btn").on('click', function () {
        if (orderInfo.totalCount < 1 || !orderInfo.accounts) return;
        var avgCost = {},
            lastCost = {};
        for (var k in orderInfo.accounts) {
            if (orderInfo.accounts[k]) {
                avgCost[k] = Math.round(orderInfo.accounts[k] / orderInfo.totalCount * 100) / 100;
                lastCost[k] = Math.round((avgCost[k] + (orderInfo.accounts[k] - avgCost[k] * orderInfo.totalCount)) * 100) / 100;
            }
        }
        var $trs = $("#passengerInfoTable > tbody >tr:visible");
        $trs.each(function (i) {
            var cost = (i == $trs.length - 1) ? lastCost : avgCost;
            var $td = $(this).find("td:eq(7)");
            var first = true;
            $td.find("p:not(:first)").remove();
            for (var k in cost) {
                if (!first) {
                    $td.find("i.price_sale_house_01").click();
                }
                first = false;
                var $p = $td.find("p:last");
                $p.find("select option[data-currency='" + k + "']").prop("selected", true);
                $p.find("input").val(cost[k]);
            }
        });
    });
    //添加游客信息
    $("#addpassengerInfo").on('click', function () {
        if ($('#passengerInfoTable tbody tr').length >= orderInfo.totalCount) return;
        $('#passengerInfoTable').show();
        var html = '<tr>' + $('#add_tours_obj_tr').html() + '</tr>';
        $('#passengerInfoTable tbody').append(html);
        updateSequence();
    });
    $("#kfSel").on('click', function () {
        // 控房选择
        $(this).find("div.pop_inner_outer").show();
        event.stopPropagation();
    }).on('change', 'input:checkbox.procurement', function () {
        // 內采 外采
        var text = $(this).attr("data-text");
        var checked = $(this).prop("checked");
        $("div.pop_inner_outer").has(this).find("tr[data-type='" + text + "']")[checked ? "show" : "hide"]();
    }).on('click', "div.pop_inner_outer input.btn_confirm_inner_outer02", function (event) {
        // 选择控票数 确定
        var $div = $("div.pop_inner_outer").has(this);
        var sum = 0;
        $div.find("tr:visible td:last-child").each(function () {
            sum += (parseInt($(this).find("input").val()) || 0);
        });
        $("#subControlNum").val(sum);
        $div.hide();
        event.stopPropagation();
        ticketAmount();
    });
    // 非控房改变
    $("#subUnControlNum").on("change", ticketAmount);
    $(document).click(function (event) {
        if (!$("#kfSel").has(event.target).length && !$("#kfSel").is(event.target)) {
            $("div.pop_inner_outer").hide();
        }
    }).on('click', "#addOtherCharges", function () {
    	addOtherCharges();
    }).on('click', "i.delOtherCharges", function () {
        // 删除其他费用
        $(this).parent().parent().remove();
        var hotelOrderPriceUuid = $(this).attr("data");
        if(hotelOrderPriceUuid && hotelOrderPriceUuid != null && hotelOrderPriceUuid != ''){
        	delete_orderPrice(hotelOrderPriceUuid);
        }
        offersChange();
    });

    //酒店间数合计和机票张数合计
    ticketAmount();
    
    //计算合计人数和合计金额
    peopleCountChange();
});

function updateSequence() {
    $("#passengerInfoTable").children("tbody").children("tr").each(function (i) {
        $(this).children("td").first().text(i+1);
    });
}

//增加其他费用
var radio_id=0;
function addOtherCharges(){
	radio_id += 1;
	var html = $('.add_other_charges').clone().show();
	html.removeAttr("style");	
	html.removeClass("add_other_charges");
	html.find('td.tr_other_u').replaceWith('<td class="tr"><input name="other_u'+radio_id+'" type="radio" class="dis_inlineblock" id="u138_input" value="1" checked="checked" data-label="增加" /><label for="u138_input">增加</label> <input id="u138_input2" value="0" data-label="减少" name="other_u'+radio_id+'" type="radio" /><label for="u140_input">减少：</label></td>');
    $('#add_other_charges_table').append(html);
}

//酒店间数合计和机票张数合计
function ticketAmount() {
	//酒店间数合计
    $("#hotelRoomTotalNumber").text((+($("#subControlNum").val()) || 0) + (+($("#subUnControlNum").val()) || 0));
}

//房型添加或减少
function hotel_room_xing_add_con() {
	var r_c_add = $('.hotel_room_xing_add_con').html();
	$('.hotel_room_xing_add_con').after(r_c_add);
}

function hotel_room_xing_del_con(obj) {
	$(obj).parent().remove();
}

// 附件管理弹窗
function up_files_pop(obj) {
	var up_files_pop = $(obj).parent().find("ul");
	var html = '<div style="min-width:300px;margin:0 auto;padding:20px;">';
	html += $(obj).next().html();
	html += '</div>';
	$.jBox(html, {
		title : "附件管理",
		buttons : {
			'关闭' : 1
		},
		submit : function(v, h, f) {
			if (v == "1") {
				up_files_pop.html("").html($(h).find("ul").html());
				return true;
			}
		},
		width : 400
	});
}

/**
 * 上传游客文件
 * @param travelerUuid 游客UUID
 * @param obj
 * @returns
 */
function uploadFiles(obj) {
	var fls = flashChecker();
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
			var fileNameList = "";
			var filePathList = "";
			if($(obj).parent().find("[name='docID']").length != 0) {
				$(obj).parent().find("[name='docID']").each(function(index, obj) {
					fileIDList += $(obj).val() + ";";
				});
			}
			if($(obj).parent().find("[name='docOriName']").length != 0) {
				$(obj).parent().find("[name='docOriName']").each(function(index, obj) {
					fileNameList += $(obj).val() + ";";
				});
			}
			if($(obj).parent().find("[name='docPath']").length != 0) {
				$(obj).parent().find("[name='docPath']").each(function(index, obj) {
					filePathList += $(obj).val() + ";";
				});
			}
			//上传成功后绑定订单
			if (fileIDList != "") {
				commenFunction(obj,fileIDList,fileNameList,filePathList);
			}
			$("#uploadPathDiv").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			fileNameList = "";
   		}
	});
	$(".jbox-close").hide();
}

//上传文件成功后绑定游客(文件上传函数在common.js中)
function commenFunction(obj,fileIDList,fileNameList,filePathList) {
	var fileIdArr = fileIDList.split(";");
	var fileNameArr = fileNameList.split(";");
	var filePathArr = filePathList.split(";");
	for(var i=0; i<fileIdArr.length-1; i++) {
		var html = '<li><a class="padr10" href="javascript:void(0)" onclick="downloads('+ fileIdArr[i] +')">'+ fileNameArr[i] +'</a>';
		html += '<span class="tdred" style="cursor:pointer;" onclick="deleteFileInfo(null,this)">删除</span>';
		html += '<input type="hidden" name="hotelAnnexUuid" />';
		html += '<input type="hidden" name="docId" value="' + fileIdArr[i] + '" />';
		html += '<input type="hidden" name="docName" value="'+ fileNameArr[i] +'"/>';
		html += '<input type="hidden" name="docPath" value="'+ filePathArr[i] +'"/>';
		html += '</li>';
		$(obj).parent().find("ul").append(html);
	};
}

//删除现有的文件
function deleteFileInfo(fileUuid,obj) {
	top.$.jBox.confirm('确定要删除该文件？','系统提示',function(v){
		if(v=='ok'){
			if(fileUuid != null && fileUuid != "") {
				$.ajax({
					type:"POST",
					url: contextPath + "/hotelAnnex/delete",
					cache:false,
					data:{
						"uuid":fileUuid
					},
					success:function(data){}
				});
			}
			$(obj).parent("li").remove();
		}
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');		
}


//下载文件
function downloads(docid){
	window.open(contextPath+"/sys/docinfo/download/"+docid);
}

// 保存游客信息
function save_tours_obj(obj) {
	if ($(obj).text() == "保存") {
		$('#passengerInfoTable tr').has(obj).find('input,select').attr('disabled', 'disabled');
		$(obj).text('修改');
	} else {
		$('#passengerInfoTable tr').has(obj).find('input,select').removeAttr('disabled');
		$(obj).text('保存');
	}
}

var removeTravelers = new Array();
//计算合计人数和合计金额
function peopleCountChange() {
	 // 总人数
    var totalCount = 0;
    // 总费用
    var totalCost = {};
    for (var l = orderInfo.traveller.length; l--;) {
        var traveller = orderInfo.traveller[l];
        var count = parseInt($("#" + traveller.type).find("td:eq(2) input").val());
        if($.trim(count).length == 0 || isNaN(count) == true){
        	count = 0;
        	parseInt($("#" + traveller.type).find("td:eq(2) input").val(0));
        }
        
        var cost = {};
        for (var k in traveller.cost) {
            totalCost[k] || (totalCost[k] = { code: traveller.cost[k].code, price: 0 });
            cost[k] = { code: traveller.cost[k].code, price: traveller.cost[k].price * count };
            totalCost[k].price += cost[k].price;
        }
        $("#" + traveller.type).find("td:eq(3)").text(formatCost(cost));
        totalCount += count;
    }
    orderInfo.totalCost = totalCost;
    orderInfo.totalCount = totalCount;
    $("#totalPeopleCount").text(totalCount);//合计人数
    $("#totalPeopleMoney").text(formatCost(totalCost));	//合计金额
    offersChange();	//计算费用调整
    var $tbody = $("#passengerInfoTable > tbody");
    var subCount = totalCount - $tbody.children("tr:visible").length;
    
    if (subCount < 0) {
    	if(totalCount == 0 ) {
        	$tbody.children("tr").each(function() {
        		var removeTravelerUuid = $(this).find("input[name='travelerUuid']").val();
        		removeTravelers.push(removeTravelerUuid);
        	});
        	$tbody.children("tr").remove();
    	} else {
        	$tbody.children("tr:gt(" + (totalCount - 1) + ")").each(function() {
        		var removeTravelerUuid = $(this).find("input[name='travelerUuid']").val();
        		removeTravelers.push(removeTravelerUuid);
        	});
            $tbody.children("tr:gt(" + (totalCount - 1) + ")").remove();
    	}
    	
    }
}


//费用调整操作
function offersChange() {
	var offersCost = {};
    $("#adjustCost tr:visible").each(function (i) {
        var $this = $(this);
        var currency, money, code;
        if (i < 3) {        	
            if ($this.find("input:text:first").is(".price")) {
                currency = $this.find("select").val();
                code = $this.find("select option:selected").attr("data-currency");
                money = parseFloat($this.find("input:text:first").val().replace(/,/g,""));
            }
        } else {
            currency = $this.find("select").val();
            code = $this.find("select option:selected").attr("data-currency");
            money = parseFloat($this.find("input:text:eq(1)").val().replace(/,/g,""));
            if ($this.find("input:radio").prop("checked") && money) {
                money = 0 - money;
            }
        }
        if (currency && money) {
            offersCost[currency] || (offersCost[currency] = { code: code, price: 0 });
            offersCost[currency].price += money;
        }
    });
    orderInfo.offersCost = offersCost;
    showCostMsg();
}

//显示费用结算
function showCostMsg() {
	orderInfo.accounts = subCost(orderInfo.totalCost, orderInfo.offersCost);
    orderInfo.unReceipted = subCost(orderInfo.accounts, orderInfo.receipted);
    // 订单总额
    $("span.totalCost").text(formatCost(orderInfo.totalCost));
    // 结算总额
    $("span.accounts").text(formatCost(orderInfo.accounts));
    // 已收金额
    $("span.payedMoney").text(formatCost(orderInfo.receipted));
    // 未收总额
    $("span.unReceipted").text(formatCost(orderInfo.unReceipted));
}

function formatCost(cost) {
	var str = [];
    for (var k in cost) {
        if (cost[k].price) {
            if (cost[k].price >= 0 && str.length) {
                str.push(" + ");
            } else if (cost[k].price < 0) {
                str.push(" - ");
            }
            var price = Math.abs(cost[k].price);
            str.push(cost[k].code + price.toFixed(2));
//            str.push(cost[k].code + Math.abs(cost[k].price));
        }
    }
    return str.join('');
}

function subCost(cost1, cost2) {
	var cost = $.extend(true, {}, cost1);
    if (!cost2) return cost;
    for (var k in cost2) {
        cost[k] || (cost[k] = { code: cost2[k].code, price: 0 });
        cost[k].price = cost[k].price  - cost2[k].price;
    }
    return cost;
}

String.prototype.formatNumberMoney = function (pattern) {
    var strarr = this ? this.toString().split('.') : ['0'];
    var fmtarr = pattern ? pattern.split('.') : [''];
    var retstr = '';
    var str = strarr[0];
    var fmt = fmtarr[0];
    var i = str.length - 1;
    var comma = false;
    for (var f = fmt.length - 1; f >= 0; f--) {
        switch (fmt.substr(f, 1)) {
            case '#':
                if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                break;
            case '0':
                if (i >= 0) retstr = str.substr(i--, 1) + retstr;
                else retstr = '0' + retstr;
                break;
            case ',':
                comma = true;
                retstr = ',' + retstr;
                break;
        }
    }
    if (i >= 0) {
        if (comma) {
            var l = str.length;
            for (; i >= 0; i--) {
                retstr = str.substr(i, 1) + retstr;
                if (i > 0 && ((l - i) % 3) == 0) retstr = ',' + retstr;
            }
        } else retstr = str.substr(0, i + 1) + retstr;
    }
    retstr = retstr + '.';
    str = strarr.length > 1 ? strarr[1] : '';
    fmt = fmtarr.length > 1 ? fmtarr[1] : '';
    i = 0;
    for (var f = 0; f < fmt.length; f++) {
        switch (fmt.substr(f, 1)) {
            case '#':
                if (i < str.length) retstr += str.substr(i++, 1);
                break;
            case '0':
                if (i < str.length) retstr += str.substr(i++, 1);
                else retstr += '0';
                break;
        }
    }
    return retstr.replace(/^,+/, '').replace(/\.$/, '');
};
String.prototype.replaceSpecialChars = function (regEx) {
    if (!regEx) {
        regEx = /[\`\"\~\!\@#$\%\^\&\*\(\)\+\=\|\{\}\'\:\;\'\,\/\/\[\/\/\]\.\<\>\/\?\~\！\@#\¥\%\…\…\&\*\（\）\—\—\+\|\{\}\【\】\‘\；\：\”\“\’\。\，\、\？]/g;
    }
    return this.replace(regEx, '');
};
function jbox_finance_change_add_records() {
//	$.jBox($("#jbox_finance_change_add_records").html(), {
//	    title: "提醒", buttons: { '确定': 1 }, submit: function (v, h, f) {
//	    
//	    }, height: '560', width: 400
//	});


    //检验渠道联系人& 渠道联系人电话是否为空
    var channelType = $("#channelType").val();
    var ChannelFlag = true;
    var tipInfo = '';
    if(channelType == '-1'){
        $("#nonChannelList").find("input[name=contactsName]").each(function(){
            if($(this).val().trim() == '') {
                tipInfo = "请输入渠道联系人";
                $(this).focus();
                ChannelFlag = false;
                return false;
            }
        });
        $("#nonChannelList").find("input[name=contactsTel]").each(function(){
            if($(this).val().trim() == '') {
                tipInfo = "请输入渠道联系人电话";
                $(this).focus();
                ChannelFlag = false;
                return false;
            }
        });
    } else {
        $("#signChannelList").find("input[name=contactsName]").each(function () {
            if ($(this).val().trim() == '') {
                tipInfo = "请输入渠道联系人";
                $(this).focus();
                ChannelFlag = false;
                return false;
            }
        });
        $("#signChannelList").find("input[name=contactsTel]").each(function () {
            if ($(this).val().trim() == '') {
                tipInfo = "请输入渠道联系人电话";
                $(this).focus();
                ChannelFlag = false;
                return false;
            }
        });
    }
    if(!ChannelFlag) {
        $.jBox.tip(tipInfo);
        return false;
    }


	var flag = false;
	$("#passengerInfoTable :input[name='travelerMoney']").each(function() {
		var travelerMoneyVal = $(this).val();
		if($.trim(travelerMoneyVal).length <= 0 ) {
			flag = true;
		}
	});
	if(flag) {
		$.jBox.info('请填写每个游客的价格！', '警告');
		return ;
	}
	var map = {}; // Map map = new HashMap();
		
	$("#passengerInfo select[name=personTypeSelect] option:selected").each(function(){
		var key = $(this).text();
		if(key in map) {
			map[key] = map[key] + 1;
		} else {
			map[key] = 1;
		}
	});

	var personTypeVali = false;
	var personTypeName = '';
	var personTypeNum = 0;
	//游客信息验证
	$(".groupPrices_tr").each(function(){
		var orderPersonNum = $(this).find("input[name=orderPersonNum]").val();
		var travelerType = $(this).find("input[name=groupPriceUuid]").val();
		var personType = $.trim($(this).find("span[class=personType]").text());
		orderPersonNum = orderPersonNum == '' ? 0 : orderPersonNum;
		if(map[personType] > orderPersonNum) {
			personTypeName = personType;
			personTypeNum = orderPersonNum;
			personTypeVali = true;
			return false;
		}
	});

	if(personTypeVali) {
		top.$.jBox.tip(personTypeName+'游客人数不能大于'+personTypeNum+'！');
		return false;
	} 
	updateHotelOrder();
}



//加载渠道商联系人信息
function loadAgentInfo() {
	var agentId = $("#orderCompany").val();
	$.ajax({
		type:"POST",
		url: contextPath + "/hotelOrder/loadAgentInfo",
		cache:false,
		data:{
			"id":agentId
		},
		success:function(data){
			//初始化渠道商联系人信息
			var signChannel = $("#signChannelList ul").eq(0);
			signChannel.find("input[name=contactsName]").val(data.agentinfo.agentContact);
			signChannel.find("input[name=contactsTel]").val(data.agentinfo.agentContactMobile);
			signChannel.find("input[name=contactsTixedTel]").val(data.agentinfo.agentContactTel);
			signChannel.find("input[name=contactsAddress]").val(data.agentinfo.agentAddress);
			signChannel.find("input[name=contactsFax]").val(data.agentinfo.agentContactFax);
			signChannel.find("input[name=contactsQQ]").val(data.agentinfo.agentContactQQ);
			signChannel.find("input[name=contactsEmail]").val(data.agentinfo.agentContactEmail);
			signChannel.find("input[name=contactsZipCode]").val(data.agentinfo.agentPostcode);
			signChannel.find("input[name=remark]").val(data.agentinfo.remarks);
		}
	});
}
submit_times = 0;
//修改海岛游订单信息
function updateHotelOrder(){
	if(submit_times!==0){
		top.$.jBox.tip('勿多次提交');
		return;
	}
	submit_times++;
	var  isTransfer = $("#isTransfer").val();//是否是转报名   
	var  orderCompany = $("#channelType").val();//渠道
	var  orderCompanyName = "";			//渠道名称
	//非签约渠道
	if(orderCompany == "-1"){
		orderCompanyName = $("#orderCompanyName").val();
	}else{
		orderCompany = $("#orderCompany option:selected").val();	
		orderCompanyName=$("#orderCompany option:selected").text().trim();
	}
	//联系人列表
	var contactsArr = [];
	//remove掉被隐藏的联系人，以免被误添加入联系人列表
	var $contactListPre;
	if($("#signChannelList").is(":hidden")){
		$contactListPre = $("#nonChannelList")
	} else {
		$contactListPre = $("#signChannelList")
	}
	//组织联系人数据传往后台存储
	$contactListPre.children("ul").each(function(index,item){
		var contactsId = $(item).find("input[name='contactsId']").val();	//联系人主键
		var contactsName = $(item).find("input[name='contactsName']").val();		//联系人
		var contactsTel = $(item).find("input[name='contactsTel']").val();			//联系电话
		var contactsTixedTel = $(item).find("input[name='contactsTixedTel']").val();//固定电话
		var contactsAddress = $(item).find("input[name='contactsAddress']").val();	//渠道地址
		var contactsFax = $(item).find("input[name='contactsFax']").val();			//传真
		var contactsQQ = $(item).find("input[name='contactsQQ']").val();			//QQ
		var contactsEmail = $(item).find("input[name='contactsEmail']").val();		//Email
		var contactsZipCode = $(item).find("input[name='contactsZipCode']").val();	//渠道邮编
		var remark = $(item).find("input[name='remark']").val();					//其他
		var contactsObj = {
				contactsId:contactsId,
				contactsName:contactsName,
				contactsTel:contactsTel,
				contactsTixedTel:contactsTixedTel,
				contactsAddress:contactsAddress,
				contactsFax:contactsFax,
				contactsQQ:contactsQQ,
				contactsEmail:contactsEmail,
				contactsZipCode:contactsZipCode,
				remark:remark
		};
		contactsArr.push(contactsObj);
	});
	
	//费用及人数
	var moneyAndPeopleArr = [];
	$("#moneyAndPeopleTab tbody>tr.groupPrices_tr").each(function(index,tr){
		var groupPriceUuid = $(tr).find("input[name='groupPriceUuid']").val();	//团期价格表UUID
		var orderPersonNum = $(tr).find("input[name='orderPersonNum']").val();  //订单价格表人数
		var obj = {
			groupPriceUuid:groupPriceUuid,
			orderPersonNum:orderPersonNum
		};
		moneyAndPeopleArr.push(obj);
	});
	
	//酒店扣减间数
	var subControlNum = $("#subControlNum").val();	//控房
	var subUnControlNum =$("#subUnControlNum").val();//非控房
	
	//费用调整
	var moneyChangeArr = [];
	$("#add_other_charges_table tr").each(function(index,tr){
		var orderPriceId = $(tr).find("input[name='orderPriceId']").val();	//订单价格表id
		var orderPriceUuid = $(tr).find("input[name='orderPriceUuid']").val();	//订单价格表Uuid
		var priceType  = $(tr).find("input[name='priceType']").val();		//价格类型
		var currencyId = $(tr).find("#currencyId option:selected").val();	//币种
		var orderPrice = $(tr).find("input[name='orderPrice']").val();		//价格
		var orderPriceRemark = $(tr).find("input[name='orderPriceRemark']").val();	//备注
		var priceName = "";	//费用名称
		//如果是金额较少
		var radionValue = $(tr).find("input[type=radio]:checked").val();
		if(radionValue != undefined){
			priceName = $(tr).find("input[name='priceName']").val();
			if(radionValue == '0'){
				orderPrice = -orderPrice;	//金额前面加负号
			};
		}
		var obj ={
			orderPriceId:orderPriceId,
			orderPriceUuid:orderPriceUuid,
			priceName:priceName,
			priceType:priceType,
			currencyId:currencyId,
			orderPrice:orderPrice,
			orderPriceRemark:orderPriceRemark
		};
		moneyChangeArr.push(obj);
	});
	
	//费用结算
	var costMoney = $("#costMoneySpan").text().trim();	//订单总额
	var totalMoney = $("#totalMoneySpan").text().trim();//结算总额,应收金额
	var payedMoney = $("#payedMoneySpan").text().trim();//已收金额
	var costMoneyUuid = $("#costMoneyUuid").val();//订单总额uuid
	var totalMoneyUuid = $("#totalMoneyUuid").val();//结算总额uuid
	var payedMoneyUuid = $("#payedMoneyUuid").val();//已收金额uuid
	
	//游客信息
	var travelerArr = [];
	$("#passengerInfoTable > tbody > tr").each(function(i,tr){
		var travelerUuid = $(tr).find("input[name='travelerUuid']").val();	//游客uuid
		var travelerName = $(tr).find("input[name='travelerName']").val();	//姓名
		var nameSpell = $(tr).find("input[name='nameSpell']").val();		//英文名称
		var spaceLevel = $(tr).find("#spaceLevelSelect option:selected").val();	//舱位等级
		var personType = $(tr).find("#personTypeSelect option:selected").val();	//游客类型
		var sex = $(tr).find("#sexSelect option:selected").val();	//性别
		
		var visaArr = [];	//签证信息
		$(tr).find("#travelerVisa_td > p").each(function(k,p){
			var travelerVisaUuid = $(p).find("input[name='travelerVisaUuid']").val();	//签证类型uuid
			var country = $(p).find("#countrySelect option:selected").val();	//签证国家
			var visaType = $(p).find("#visaTypeSelect option:selected").val();	//签证类型
			visaArr.push({travelerVisaUuid:travelerVisaUuid,country:country,visaType:visaType});	
		});
		
		var papersTypeArr = [];	//证件信息
		$(tr).find("#travelerPapers_td > p").each(function(k,p){
			var travelerPapersUuid = $(p).find("input[name='travelerPapersUuid']").val();	//证件类型uuid
			var papersType = $(p).find("#papersTypeSelect option:selected").val();	//证件类型
			var idCard = $(p).find("input[name='idCard']").val();	//证件号码
			var validityDate = $(p).find("input[name='validityDate']").val();	//有效期
			papersTypeArr.push({travelerPapersUuid:travelerPapersUuid,papersType:papersType,idCard:idCard,validityDate:validityDate});
		});
		
		var travelerMoneyArr = [];	//价格
		$(tr).find("#moneyAmount_td > p").each(function(k,p){
			var travelerMoneyUuid = $(p).find("input[name='travelerMoneyUuid']").val();	//金额表uuid
			var currencyId = $(p).find("#currencyIdSelect option:selected").val();//币种
			var travelerMoney = $(p).find("input[name='travelerMoney']").val();//金额
			travelerMoneyArr.push({travelerMoneyUuid:travelerMoneyUuid,currencyId:currencyId,travelerMoney:travelerMoney});
		});
		
		var fileArr = [];	//附件
		$(tr).find("#up_files_pop > ul > li").each(function(k,li){
			var hotelAnnexUuid = $(li).find("input[name='hotelAnnexUuid']").val();	//hotel_annex表uuid
			//只保存没有上传的文件
			if(hotelAnnexUuid == null || hotelAnnexUuid == ""){
				var docId = $(li).find("input[name='docId']").val();	//附件表ID
				var docName = $(li).find("input[name='docName']").val();//文件名称
				var docPath = $(li).find("input[name='docPath']").val();//文件路径
				fileArr.push({docId:docId,docName:docName,docPath:docPath});
			}
		});
		
		var travelerRemark = $(tr).find("input[name='travelerRemark']").val();	//备注
		
		var oneTravelerObj = {
			travelerUuid:travelerUuid,
			travelerName:travelerName,
			nameSpell:nameSpell,
			spaceLevel:spaceLevel,
			personType:personType,
			sex:sex,
			visaArr:visaArr,
			papersTypeArr:papersTypeArr,
			travelerMoneyArr:travelerMoneyArr,
			fileArr:fileArr,
			travelerRemark:travelerRemark
		};
		travelerArr.push(oneTravelerObj);
	});
	
	//订单备注
	var hotelOrderRemark = $("#hotelOrderRemark").val();
	var hotelOrderUuid = $("#hotelOrderUuid").val();
	var hotelOrderId = $("#hotelOrderId").val();
	
	//订单总额
	var costMoneyArr=[];
	if(orderInfo.totalCost){
		for(var k in  orderInfo.totalCost){
			var obj ={};
			obj[k]= orderInfo.totalCost[k].price;
			costMoneyArr.push(obj);
		}
	}
	
	//结算总额
	var totalCostArr=[];
	if(orderInfo.accounts){
		for(var k in  orderInfo.accounts){
			var obj ={};
			obj[k]= orderInfo.accounts[k].price;
			totalCostArr.push(obj);
		}
	}
	
	//提交的数据结构
	saveData={
		hotelOrderId:hotelOrderId,	//订单id
		hotelOrderUuid:hotelOrderUuid,	//订单uuid
		isTransfer:isTransfer,
		orderCompany:orderCompany,
		orderCompanyName:orderCompanyName,
		contactsArr : contactsArr,
		moneyAndPeopleArr:moneyAndPeopleArr,
		subControlNum:subControlNum,
		subUnControlNum:subUnControlNum,
		moneyChangeArr:moneyChangeArr,
		costMoney:costMoney,
		totalMoney:totalMoney,
		payedMoney:payedMoney,
		costMoneyUuid:costMoneyUuid,
		totalMoneyUuid:totalMoneyUuid,
		payedMoneyUuid:payedMoneyUuid,
		travelerArr:travelerArr,
		hotelOrderRemark:hotelOrderRemark,
		costMoneyArr:costMoneyArr,	//订单总额(成本价)
		totalCostArr:totalCostArr,	//结算总额
	};  
    console.log(saveData);
	
	$.ajax({
		type:"POST",
		url: contextPath + "/hotelOrder/updateHotelOrder",
		dataType:"json",      
		contentType:"application/json",               
		data:JSON.stringify(saveData), 
		success:function(data){
			if(data&&data.code == '1'){
				if(isTransfer == "true") {
					$.jBox.tip("订单转报名成功!");		
				} else {
					$.jBox.tip("修改成功!");
				}
				window.location.href = contextPath + "/hotelOrder/list/0";
			}else if(data&&data.code == '0'){
				if(isTransfer == "true") {
					$.jBox.tip("订单转报名失败!");		
				} else {
					$.jBox.tip("修改失败!");
				}
				submit_times = 0;
			};
		}
	});
}

//删除游客
function delete_traveler(obj,travelerUuid){
	$.jBox.confirm("确定要删除游客吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			//删除游客信息
		    $('#passengerInfoTable tr').has(obj).remove();
		    updateSequence();
		    if(travelerUuid && travelerUuid != ''){
		    	$.ajax({
		    		type:"POST",
		    		url: contextPath + "/hotelTraveler/delete",
		    		data:{
		    			uuids:travelerUuid
		    		}, 
		    		success:function(data){
		                  
		    		}
		    	});
		    };
		}
	});
}

//删除订单价格
function delete_orderPrice(hotelOrderPriceUuid){
	if(hotelOrderPriceUuid && hotelOrderPriceUuid != ''){
    	$.ajax({
    		type:"POST",
    		url: contextPath + "/hotelOrderPrice/delete",
    		data:{
    			uuids:hotelOrderPriceUuid
    		}, 
    		success:function(data){
                  
    		}
    	});
    };
}

//删除联系人
function delete_contacts(orderContactsId){
	if(orderContactsId && orderContactsId != ''){
    	$.ajax({
    		type:"POST",
    		url: contextPath + "/hotelOrder/deleteOrderContacts",
    		data:{
    			orderContactsId:orderContactsId
    		}, 
    		success:function(data){
                  
    		}
    	});
    };
}
