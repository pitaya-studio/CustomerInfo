//点击预定选择渠道弹出框
function agentType(obj) {

    // 删除多余样式
    $(".sign").removeClass("sign");

    // 初始化tempSaler
    tempSaleId = 0;  // 原自有渠道的销售人员
    tempQuauqSaleId = 0;  // 原quauq渠道的销售人员
    // T1--T2 先选择
    var activityType = $("#activityKind").val();
    var $theirOwnAgents = getTheirOwnAgents();  // 自有渠道
    // var $quauqAgents = $("#quauqAgentTemp").clone();  // quauq渠道select
    // $quauqAgents.attr("id", "quauqAgent").addClass("ui-front");
    var $quauqAgents=$('<input placeholder="请输入渠道名称">').attr("id","quauqAgentInput").addClass('inputTxt');
    var $quauqAgent=$('<input type="hidden">').attr("id","quauqAgent");
    //如果有待补位记录，则直接跳转到申请补位页面
    var groupId = $(obj).parents("tr:eq(0)").find("[name=groupid]").val();
    if (hasCoverOrder(groupId)) {
        if ($("#groupCoverFlag").val() == "1") {
            window.open(sysCtx + "/groupCover/list/" + groupId);
        } else {
            $.jBox.tip("存在待补位订单，请稍后进行报名");
        }
        return false;
    }

    //C183 点击预定，需要重新从后台请求渠道商
    var sel = document.createElement("select");
    var uuid=$("#companyUuid").val();
    $.ajax({
        async : false,
        type : "POST",
        url : sysCtx + "/activity/managerforOrder/getAgentinfoList",
        success : function (sss) {
            var msg = eval('(' + sss + ')');
            if (msg.res == "success") {
                var array = msg.agentinfos;
                for (var i = 0; i < array.length; i++) {
                    var aname = "";
                    //315需求,针对越柬行踪，将非签约渠道改为签约渠道
                    if(uuid=='7a81b21a77a811e5bc1e000c29cf2586' && array[i].agentName=='非签约渠道'){
                        aname='直客';
                    }else{
                        aname = array[i].agentName;
                    }
                    var aid = array[i].id;
                    var newOpt = new Option(aname, aid);
                    sel.options.add(newOpt);
                }
            }
        }
    });
    // 判断渠道数量，如果渠道数量大于0，弹出对话框
    var agentNum = 0;
    // 团期平台上架状态   0未上架 1已上架
    var agpT1 = -1;
    // 批发商上架权限状态  0启用 1禁用
    //var officeT1 = -1;
    agentNum = $theirOwnAgents.children().size() + $('#quauqAgentTemp').children().size();//170328 by tlw
    if (agentNum > 0) {
        // 0518需求,根据批发商上架权限状态,团期平台上架状态来展示订单来源
        $.ajax({
            async : false,
            type : "POST",
            url : sysCtx + "/activity/managerforOrder/getGroupAndOfficeT1PermissionStatus",
            data : {
                groupId : groupId
            },
            success : function (data) {
                if(data.result == "success"){
                    agpT1 = data.agpT1;
                    //officeT1 = data.officeT1;
                }else{
                    return;
                }
            }
        });
        // 0518需求,根据批发商上架权限状态,团期平台上架状态来展示订单来源

        // 渠道来源选择
        var $_agentTypeSelect = $("#agentSourceTypeTemp").clone();
        $_agentTypeSelect.attr("id", "agentSourceType");
        $_agentTypeSelect.show();
        //渠道选择(初始为自有渠道)
        var _agentSelect = $theirOwnAgents.clone();
        _agentSelect.attr("id", "agentIdSelCl");
        $(_agentSelect).addClass("ui-front");
        $(_agentSelect).addClass("agentSelected");
        _agentSelect.show();

        //给点击的按钮添加指定的标记，方便找到对应的触发按钮
        $(obj).addClass("sign");
        //添加渠道商
        var addAgentinfoHtml = "";
        if ($("#isAddAgent").length > 0) {
            addAgentinfoHtml = "<input name='addAgentinfo' class='btn btn-primary' type='button' onclick='addAgentinfo()' value='新增渠道' >";//去掉多余的style 170328
        }
        //销售
        var salerHtml = getSalerByAgentId(_agentSelect);
        //弹出框
        var $div = $("<div class=\"tanchukuang\"></div>");
        if (activityType == "2" && $("#quauqBookOrderPermission").val() == "1" && agpT1 == "1" && $("#officeShelfRightsStatus").val() == "0") {
            $div.append($('<div class="add_intermodalType"><label class="activitylist_team_co3_text"><span class="xing">*</span>订单来源：</label>').append($_agentTypeSelect));
        }
        var agentInfoMain = $('<div class="add_intermodalType"><label class="activitylist_team_co3_text"><span class="xing">*</span>渠道选择：</label>').append(_agentSelect).append($quauqAgents).append($quauqAgent);
        agentInfoMain.append(addAgentinfoHtml);
        $div.append(agentInfoMain).append('<div class="add_intermodalType">' + salerHtml + '</div>');

        //付款方式
        var _orderTypeSelect = $(obj).next().clone();
        $(_orderTypeSelect).addClass("orderTypeSelected");
        $(_orderTypeSelect).show();
        $div.append($('<div class="add_intermodalType"><div class="activitylist_team_co3_text">付款方式：</div>').append(_orderTypeSelect));
        $div.append("</div>");
        var html = $div.html();
         $.jBox(html, {
            title : "选择渠道和付款方式",
            persistent : true,
            buttons : {
                '预定' : 1
            },
            submit : function (v, h, f) {
                if ($('#agentSourceType') && $('#agentSourceType').val() == '2' && !$("#quauqAgent").val()) {
                    top.$.jBox.tip('尚未选择渠道！', 'info');
                    return false;
                }
                orderPay();
            },
            height : 300,
            width : 600
        });
        //判断渠道类型   added by ruiqi.zhang  2017-3-29 14:55:30
        changeAgentSource(obj);
        // 渠道选择下拉框注册控件“可输入搜索的下拉框”，并绑定选择事件：根据渠道获取销售
        $("#agentIdSelCl").comboboxInquiry().on('comboboxinquiryselect', function (event, obj) {
            getSalerByAgentId(this);
        });
        // 渠道选择下拉框的可输入框中，失去焦点事件：1.根据渠道获取销售 2.回显非签约渠道（由于控件使用特性，在粘贴前是否失去焦点，直接影响粘贴渠道全称能否正确被控件识别出粘贴内容是属于下拉数据的某一条。如果未被识别，则渠道依旧是非签约渠道）
        $("#agentIdSelCl").next().find("input").blur(function(){
            if($("#agentIdSelCl").val() == '-1'){
                if($("#companyUuid").val() == yuejianxingzong){
                    $("#agentIdSelCl").next().find("input").val("直客");
                } else {
                    $("#agentIdSelCl").next().find("input").val("非签约渠道");
                }
            }
            getSalerByAgentId($("#agentIdSelCl"));
        });
        // $("#quauqAgentInput").comboboxInquiry().next().hide();
    } else {
        $.jBox.tip("请配置渠道商", "警告");
    }

    $("#agentSourceType").val("2").trigger("change");

//	requestTime = 0;
}

function blurSize() {
    $("#quauqAgent").val($("#quauqAgentInput").data('id'));
}



/**
 * 改变渠道来源类型（自有渠道、quauq渠道）获取对应所有渠道
 * @param obj
 */
function changeAgentSource(obj){
    $("#quauqAgent").val("");
    var agentSource = "1",
    agentSource = $(obj).val();
    if (agentSource == "2") {  // quauq渠道
        //生成备选框
        url = sysCtx+'/activity/managerforOrder/getAgenList',
              param = {'agentType':'2'};
        $('#quauqAgentInput').inputToSelect({'showDivId':'agentSelectedDiv',
                                             'url':url,
                                             'param':param,
                                             'paraName':'agentName',
                                             'message':'没有找到相应渠道',
                                             'blur':blurSize
                                            });
        tempSaleId = $("#salerId").val();
        $("#agentIdSelCl").next().hide();
        $("#quauqAgentInput").show();
        $("input[name=addAgentinfo]").hide();
        // 销售人员展示所有人，默认当前用户
        getQuauqAgentSalers();
        if(tempQuauqSaleId != 0) {
            $("#salerId").val(tempQuauqSaleId);
        }
        agentSourceType = "2";
    } else {  // 自有渠道
        tempQuauqSaleId = $("#salerId").val();  // 切换到自有渠道，在改变销售之前记录原quauq渠道销售
        $("#agentIdSelCl").next().show();
        $("#quauqAgentInput").hide();
        $("input[name=addAgentinfo]").show();
        getAgentSalers($("#agentIdSelCl").val());
        $("input[name=addAgentinfo]").show();
        $("#salerId").val(tempSaleId);
        agentSourceType = "1";
    }
}