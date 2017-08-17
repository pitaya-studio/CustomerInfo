/**
 * 订单跟踪JS
 * @author yakun.bai
 */

$(document).ready(function () {

    // 设置滚动条
    setScroll();

    // 按钮关闭，不同分辨率，不同窗口，位置相对不变
    setCloseButton();

    // 打开订单跟踪页面
    click4Open();

    // 关闭订单跟踪页面
    click4Close();

    //新增的js ymx bug修改(位置调整)
    $(".orderT_left li:not(:first-child)").css("height", "40px");

});

/**
 * 设置各种滚动条
 * @returns
 */
function setScroll() {

    // bug17707 ie10页面卡顿修改 ymx 2017/3/31 Start

    // 左侧的滚动条
    $(".orderT_left").niceScroll({
        cursorcolor: "#666666", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "6px", // 滚动条的宽度，单位：便素
        cursorborder: "1px solid #666666", // CSS方式定义滚动条边框
        cursorborderradius: "3px",
        scrollspeed: 60, // 滚动速度
        mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        horizrailenabled: true, // nicescroll可以管理水平滚动
        sensitiverail: true // 单击轨道产生滚动
    });

    $(".orderT_right").niceScroll({
        cursorcolor: "#666666", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "6px", // 滚动条的宽度，单位：便素
        cursorborder: "1px solid #666666", // CSS方式定义滚动条边框
        cursorborderradius: "3px",
        scrollspeed: 60, // 滚动速度
        mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        horizrailenabled: true, // nicescroll可以管理水平滚动
        sensitiverail: true // 单击轨道产生滚动
    });

    // 整体的滚动条
    $("#orderTrackingAll").niceScroll({
        cursorcolor: "#666666", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "6px", // 滚动条的宽度，单位：便素
        cursorborder: "1px solid #666666", // CSS方式定义滚动条边框
        cursorborderradius: "3px",
        scrollspeed: 60, // 滚动速度
        mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        horizrailenabled: true, // nicescroll可以管理水平滚动
        sensitiverail: true // 单击轨道产生滚动
    });
    // bug17707 ie10页面卡顿修改 ymx 2017/3/31 End

}

//bug17707 ie10页面卡顿修改 ymx 2017/3/31 Start
//右侧点击事件的滚动条生成，只生成一条数据所需的滚动条
function setdetailOrderScroll(obj){
    obj.find(".contact_panel_content").niceScroll({
        cursorcolor: "#666666", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "6px", // 滚动条的宽度，单位：便素
        cursorborder: "1px solid #666666", // CSS方式定义滚动条边框
        cursorborderradius: "3px",
        scrollspeed: 60, // 滚动速度
        mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        horizrailenabled: true, // nicescroll可以管理水平滚动
        sensitiverail: true // 单击轨道产生滚动
    });

    obj.find(".detailOrder").niceScroll({
        cursorcolor: "#999", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "3px", // 滚动条的宽度，单位：便素
        cursorborder: "1px solid #999", // CSS方式定义滚动条边框
        cursorborderradius: "5px",
        scrollspeed: 60, // 滚动速度
        mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        preservenativescrolling: true, // 你可以用鼠标滚动可滚动区域的滚动条和增加鼠标滚轮事件
        sensitiverail: true // 单击轨道产生滚动
    });
}
//bug17707 ie10页面卡顿修改 ymx 2017/3/31 End

/**
 * 按钮关闭，不同分辨率，不同窗口，位置相对不变
 */
function setCloseButton() {
    var minusW = ($("body").width() - $(".orderT_content").width()) / 3;
    var ww = $("body").width() - minusW;
    $(".orderT_close").css("marginLeft", ww + "px");
    $(".orderT_close").css("right", "auto");
}

/**
 * 打开订单跟踪页面
 */
function click4Open() {
    // 点击页面上的按钮弹出窗体
    $("#orderTracking_btn").click(function () {
        $(".orderT_cont_table tbody").empty();
        // 淡入
        $(".orderTracking_back").fadeIn(350);
        $(".orderT_close,.orderT_top").show();
        $("#sea,.sea,.header").addClass("blur_background");
        $("body").css("overflow", "hidden");

        reloadAgain();
    });
}

/**
 * 关闭订单跟踪页面
 */
function click4Close() {
    //点击窗体上的close按钮关闭页面
    $(".orderT_close").click(function () {
        //淡出
        $(".orderTracking_back").fadeOut(350);
        $(".orderT_close,.orderT_top").hide();
        $("#sea,.sea,.header").removeClass("blur_background");
        $("body").css("overflow", "auto");
    });
}

/**
 * 添加渠道单击事件
 */
function addAgentClick() {
    $('.orderT_left li:not(:first-child)').click(function () {
        var $this = $(this);
        var $moveLi = $(".selectedMoveLi");
        var distance = ($this.index() - $(".selectedMoveLi").index() - 1) * 87;

        if ($this.hasClass("selectStore")) {
        }
        else {
            var indexLi = Number($moveLi.css("top").replace(/\D/g, "")) / 87 + 1;
            $($('.orderT_left li')[indexLi]).animate({
                height: '40px'
            }, 150);

            $moveLi.siblings().removeAttr("style");
            $moveLi.siblings().animate({
                height: '40px'
            }, 0);
            $moveLi.siblings().removeClass("selectStore");

            $this.animate({
                height: '80px',
                width: '380px'
            }, 150);
            $moveLi.animate({
                top: distance + 'px'
            }, 200);
            $this.addClass('selectStore');
        }
    });
}



$(window).resize(function () {
    setCloseButton();
    tableHeight();
    countLast();
});

$(window).scroll(function () {
    setCloseButton();
});

//计算表格的高度，进行自适应
function tableHeight() {
    var clientH = document.documentElement.clientHeight - 54; //可视区域高度
    var innerH = clientH - 5; //表格内部区域高度
    $(".orderT_info").css("height", clientH + "px");
    $(".orderT_left").height(innerH + "px");
    $(".orderT_right").height(innerH + "px");
}

//将订单的最后4条向上显示
function countLast() {
    var li_num = $(".orderT_right_ul>li").length;
    var wh = document.documentElement.clientHeight - 54;
    $(".orderT_right_ul>li").removeClass("upShowPanel");

    for (var i = 0; i < li_num; i++) {
        if (i > li_num - 5) {
            var liHeight = i * 70;
            var compare = wh - liHeight;
            if (compare < 370) {
                $($(".orderT_right_ul>li")[i]).addClass("upShowPanel");
            }
        }
    }
}

//更改表内用时时间
function useTime() {

    $(".useTime").each(function () {

        var time_str;

        if ($(this).parent(".last_no_light").length == 0) {
            time_str = $(this).parent().prev("div").find(".upward_time").html(); //"2015-11-12<br>22:11:36"
        } else {
            time_str = $(this).parent(".last_no_light").find(".upward_time").html();
        }
        if (time_str && time_str != "") {
            time_str = time_str.replace(/<br>/g, ",").replace(/-/g, "/"); //将获取到的字符串里面的-更改为/
            var starttime = new Date(time_str); //询价时间
            var nowtime = new Date(); //当前时间

            var lefttime = parseInt((nowtime.getTime() - starttime.getTime()) / 1000);
            var h = parseInt(lefttime / 3600);
            var m = parseInt((lefttime / 60) % 60);

            $(this).html(h + "：" + m);
            $(this).html("用时：" + h + "小时" + m + "分");
        }
    });

    setTimeout("useTime()", 60000);
    loadOrderCountNum();
}

/**
 * 刷新数据, 重新加载数据更新订单状态
 */
function reloadAgain(selectType) {
    // 删除订单统计数字
    $(".data_number:eq(0)").text("");
    $(".data_number:eq(1)").text("");
    $(".data_number:eq(2)").text("");

    // 选中所有订单
    $(".dataStatistics").removeClass("selectData");
    $(".dataStatistics:eq(1)").addClass("selectData");

    // 清空渠道信息
    $(".orderT_left").html('<li class="selectedMoveLi"></li>');

    // 清空订单信息
    $(".orderT_right_ul").empty();
    loadProgressDate(selectType);
}

/**
 * 加载全部数据
 */
function loadProgressDate(selectType) {

    // 加载等待图标
    ajaxLoadStart();

    // 加载订单总数：今日新增、所有订单、超时订单
    loadOrderCountNum();

    // 加载渠道统计信息
    loadAgentCountNum(selectType);

    // 删除等待图标
    ajaxLoadStop();
}

/**
 * 加载订单总数：今日新增、所有订单、超时订单
 */
function loadOrderCountNum() {
    $.ajax({
        type: "POST",
//		async : false,
        url: $("#progressCtx").val() + "/orderProgressTracking/manage/loadOrderCountNum?dom=" + Math.random(),
        data: {},
        success: function (result) {
            var countArr = result.split(";");
            if (countArr.length == 3) {
                $(".data_number:eq(0)").text(countArr[0]);
                $(".data_number:eq(1)").text(countArr[1]);
                $(".data_number:eq(2)").text(countArr[2]);
            }
        }
    });
}

var randomForAgent;

/**
 * 加载渠道和对应的订单数
 */
function loadAgentCountNum(selectType) {
    randomForAgent = Math.random();
    $.ajax({
        type: "POST",
//		async : false,
        url: $("#progressCtx").val() + "/orderProgressTracking/manage/loadAgentCountNum?dom=" + randomForAgent,
        data: {
            selectType: selectType,
            randomForAgent: randomForAgent
        },
        success: function (msg) {
            if (msg && msg != null) {
                var result = eval(msg);
                if (result.randomForAgent != randomForAgent || !result.josnList) {
                    return false;
                }
                var agentArr = result.josnList;
                var firstAgentIds;

                // 判断是否有数据
                if (agentArr && agentArr.length == 0) {
                    $(".no_data").show();
                    $(".orderT_right,.orderT_left").hide();
                } else {
                    $(".no_data").hide();
                    $(".orderT_right,.orderT_left").show();
                }
                for (var i = 0; i < agentArr.length; i++) {
                    var agent = agentArr[i];
                    var agentName = agent.agentName;
                    var agentBrand = agent.agentBrand;
                    var countNum = agent.countNum;
                    var agentAddress = agent.agentAddress;
                    var agentIds = agent.agentIds;
                    var agentHtml = "";
                    if (i == 0) {
                        agentHtml += "<li class='selectStore' style='width:380px;height:80px;' onClick=loadProgressByAgentIds('" + agentIds + "')>";
                        firstAgentIds = agentIds;
                    } else {
                        agentHtml += "<li onClick=loadProgressByAgentIds('" + agentIds + "')>";
                    }
                    agentHtml += "<i class='storeIcon'></i><div class='innerTitle'>";
                    agentHtml += "<span>" + agentBrand + "</span><br>";
                    agentHtml += "<span>" + agentName + "</span><br>";
                    agentHtml += "</div>";
                    agentHtml += "<span class='saler_number'>" + countNum + "</span>";
                    if (agentAddress && agentAddress != "") {
                        agentHtml += "<p>地址：" + agentAddress + "</p>";
                    } else {
                        agentHtml += "<p>地址：-</p>";
                    }
                    agentHtml += "</li>";
                    $(".orderT_left").append(agentHtml);
                }
                // 添加渠道单击事件
                addAgentClick();
                // 加载渠道数据（第一个）
                loadProgressByAgentIds(firstAgentIds, selectType);

                //bug 17451 ymx 订单跟踪门店名称高度 Start
                $(".orderT_left li:gt(1)").css("height", "40px");
                //bug 17451 ymx 订单跟踪门店名称高度 End
            }
        }
    });
}

var tempAgentIds;

/**
 * 加载渠道和对应的订单数
 */
function loadProgressByAgentIds(agentIds, selectType) {
    tempAgentIds = agentIds;
    if (!selectType) {
        selectType = $(".selectData").attr("lang");
    }
    // 清空订单信息
    $(".orderT_right_ul").empty();
    $.ajax({
        type: "POST",
		// async : false,
        url: $("#progressCtx").val() + "/orderProgressTracking/manage/loadProgressByAgentIds?dom=" + Math.random(),
        data: {
            agentIds: agentIds,
            selectType: selectType
        },
        success: function (msg) {
            if (msg && msg != null) {
                var progressArr = eval(msg);
                if (agentIds != tempAgentIds) {
                    return false;
                }
                for (var i = 0; i < progressArr.length; i++) {
                    // 订单进度对象
                    var progress = progressArr[i];
                    // 获取订单进度对象属性
                    var id = progress.id;
                    var orderId = progress.orderId;
                    var confirmationFileSalerId = progress.confirmationFileSalerId;
                    var orderStatus = progress.orderStatus ? progress.orderStatus : "0";

                    // 填充数据到页面
                    var progressHtml = '';

                    progressHtml += '<li>';

                    // 门店销售html
                    var storeSalerHtml = addStoreSalerHtml(progress);
                    progressHtml += storeSalerHtml;

                    // 销售、计调、财务、详情html
                    progressHtml += '<div class="allDetail">';

                    if (orderId && orderId != "" && orderStatus != 2) {

                        // 销售、计调、财务html
                        progressHtml += '<div class="lighting_part" lang="' + id + '">';

                        // 销售html
                        var salerHtml = addSalerHtml(progress);
                        progressHtml += salerHtml;

                        // 没有上传确认单时html
                        if ((!confirmationFileSalerId || confirmationFileSalerId == "")) {
                            var noOpHtml = addNoOpHtml(progress);
                            progressHtml += noOpHtml;
                        } else {
                            // 计调html
                            var opHtml = addOpHtml(progress);
                            progressHtml += opHtml;
                        }

                        // 财务html
                        var cwHtml = addCwHtml(progress);
                        progressHtml += cwHtml;

                        progressHtml += '</div>';
                    } else {
                        var noSalerHtml = addNoSalerHtml(progress);
                        progressHtml += noSalerHtml;
                    }
                    // 询单具体信息
                    progressHtml += '<div class="separate_part"></div>';

                    // 订单详情html
                    var detailHtml = addDetailHtml(progress);
                    progressHtml += detailHtml;

                    progressHtml += '</div>';
                    progressHtml += '</li>';
                    $(".orderT_right_ul").append(progressHtml);
                }

                // 添加悬浮时间
                allHover();

                // 用时计算
                useTime();

                // 订单向上显示代码
                countLast();

                tableHeight();

                // 订单样式
                setOrderColour();

                // 获取联系人信息
                getOrderLinkPerson();
            }
        }
    });
}

/**
 * 添加门店销售html
 * @param progress
 */
function addStoreSalerHtml(progress) {

    var storeSalerHtml = "";

    var contactList = progress.contactList;
    var askTime = progress.askTime;
    var askNum = progress.askNum; //判断来源有无关系
    var t1Flag = progress.t1Flag;


    storeSalerHtml += '<div class="store_saler">';

    // 如果有询单时间则显示时间，如果没有则不显示
    if (askTime && askTime != "") {
        var askTimeArr = askTime.split(" ");
        storeSalerHtml += '<div class="saler_up_time">' + askTimeArr[0] + '<br>' + askTimeArr[1] + '</div>';
    } else {

    }

    //如果是微信和电脑端的才加电话标志
    if (askNum == null) {
        storeSalerHtml += '<i class="store_saler_icon"></i>';
        storeSalerHtml += '· · · ·<i class="fa"></i> · · · ';
    } else {
        if (t1Flag == 1) {
            storeSalerHtml += '<i class="store_saler_icon fromWechat"></i>';
        } else if (t1Flag == 0) {
            storeSalerHtml += '<i class="store_saler_icon fromComputer"></i>';
        }
        storeSalerHtml += '· · ·<i class="fa fa-phone"></i>· · ·';
    }

    // storeSalerHtml += '<i class="store_saler_icon"></i>';
    // storeSalerHtml += '· · · · · · ·';
    // storeSalerHtml += '<i class="store_saler_icon fromComputer"></i>';
    // storeSalerHtml += '<i class="fromWechat store_saler_icon"></i>';
    // storeSalerHtml += '· · ·<i class="fa fa-phone"></i>· · ·';

    storeSalerHtml += '<div class="saler_phone">';
    storeSalerHtml += '<em class="upArrow"></em>';
    // 添加门店销售信息
    if (contactList && contactList != "") {
        storeSalerHtml += '<dl>';
        storeSalerHtml += '<dt>姓名：</dt><dd>' + contactList.contactName + '</dd>';
        storeSalerHtml += '<dt>电话：</dt><dd>' + contactList.contactMobile + '</dd>';
        storeSalerHtml += '</dl>';
    }
    storeSalerHtml += '<em class="downArrow"></em>';
    storeSalerHtml += '</div>';
    storeSalerHtml += '</div>';

    return storeSalerHtml;
}

/**
 * 添加销售html
 * @param progress
 * @returns {String}
 */
function addSalerHtml(progress) {

    var orderCreateTime = progress.orderCreateTime;

    var salerHtml = "";
    salerHtml += '<div class="small_lighting">';
    var orderTimeArr = orderCreateTime.split(" ");
    salerHtml += '<div class="upward_time">' + orderTimeArr[0] + '<br>' + orderTimeArr[1] + '</div>';
    salerHtml += '<span></span>';
    salerHtml += '<div class="linkman">';
    salerHtml += '<div class="contact_panel">';
    salerHtml += '<em class="upArrow"></em>';
    // 销售联系人
    salerHtml += '<div class="contact_panel_content salerPerson">';
    salerHtml += '</div>';
    salerHtml += '<em class="downArrow"></em>';
    salerHtml += '</div>';
    salerHtml += '</div>';
    salerHtml += '</div>';
    return salerHtml;
}

/**
 * 添加未生成订单html
 * @param progress
 * @returns {String}
 */
function addNoSalerHtml(progress) {

    var id = progress.id;
    var setting = progress.setting;
    var askTime = progress.askTime;
    var orderStatus = progress.orderStatus;

    var noSalerHtml = "";
    if (orderStatus != 2 && orderStatus != 4 && orderStatus != 5) {
        //有颜色的图标和时间变化
        //绿灯分钟数，黄灯分钟数
        var greenTime = 10;
        var yellowTime = 20;
        if (setting && setting != "") {
            var setArr = setting.split("+");
            for (var j = 0; j < setArr.length; j++) {
                var sets = setArr[j].split(" ");
                var settingType = sets[0];
                var greenLightTimeType = sets[1];
                var greenTime = sets[3];
                var yellowTime = sets[6];
                if (settingType == 1) {
                    if (greenLightTimeType == 1) {
                        greenTime = greenTime * 24 * 60;
                        yellowTime = yellowTime * 24 * 60;
                    } else if (greenLightTimeType == 2) {
                        greenTime = greenTime * 60;
                        yellowTime = yellowTime * 60;
                    } else {
                        greenTime = greenTime;
                        yellowTime = yellowTime;
                    }
                    break;
                }
            }
        }
        var date = "";
        var time = "";
        if (askTime && askTime != "") {
            var askTimeArr = askTime.split(" ");
            date = askTimeArr[0];
            time = askTimeArr[1];
        }

        // 没有生成订单时html
        noSalerHtml += '<div class="lighting_part" lang="' + id + '">';
        noSalerHtml += '<div class="small_lighting">';

        noSalerHtml += '<div class="wait_light" date="' + date + '" time="' + time + '" greenTime="' + greenTime + '" yellowTime="' + yellowTime
            + '"></div>';
        noSalerHtml += '<div class="linkman">';
        noSalerHtml += '<div class="contact_panel">';
        noSalerHtml += '<em class="upArrow"></em>';
        // 销售联系人
        noSalerHtml += '<div class="contact_panel_content salerPerson">';
        noSalerHtml += '</div>';
        noSalerHtml += '<em class="downArrow"></em>';
        noSalerHtml += '</div>';
        noSalerHtml += '</div>';
        noSalerHtml += '</div>';

        noSalerHtml += '<div class="other_lighting"></div>';
        noSalerHtml += '<div class="other_lighting"></div>';
        noSalerHtml += '</div>';
    } else {
        noSalerHtml += '<div class="lighting_part" lang="' + id + '">';
        noSalerHtml += '<div class="small_lighting"><div class="upward_time"></div><span class="closeOrder"></span>';

        noSalerHtml += '<div class="linkman">';
        noSalerHtml += '<div class="contact_panel">';
        noSalerHtml += '<em class="upArrow"></em>';
        // 销售联系人
        noSalerHtml += '<div class="contact_panel_content salerPerson">';
        noSalerHtml += '</div>';
        noSalerHtml += '<em class="downArrow"></em>';
        noSalerHtml += '</div>';
        noSalerHtml += '</div>';

        noSalerHtml += '</div>';
        noSalerHtml += '</div>';
    }

    return noSalerHtml;
}

/**
 * 添加未上传确认单html
 * @param progress
 * @returns {String}
 */
function addNoOpHtml(progress) {

    var setting = progress.setting;
    var askTime = progress.askTime;
    var firstOrderPayTime = progress.firstOrderPayTime;
    var orderStatus = progress.orderStatus;

    //有颜色的图标和时间变化
    //绿灯分钟数，黄灯分钟数
    var greenTime = 10;
    var yellowTime = 20;
    if (setting && setting != "") {
        var setArr = setting.split("+");
        for (var j = 0; j < setArr.length; j++) {
            var sets = setArr[j].split(" ");
            var settingType = sets[0];
            var greenLightTimeType = sets[1];
            var greenTime = sets[3];
            var yellowTime = sets[6];
            if (settingType == 2) {
                if (greenLightTimeType == 1) {
                    greenTime = greenTime * 24 * 60;
                    yellowTime = yellowTime * 24 * 60;
                } else if (greenLightTimeType == 2) {
                    greenTime = greenTime * 60;
                    yellowTime = yellowTime * 60;
                } else {
                    greenTime = greenTime;
                    yellowTime = yellowTime;
                }
                break;
            }
        }
    }

    var orderCreateTime = progress.orderCreateTime;
    var orderTimeArr = orderCreateTime.split(" ");
    var date = orderTimeArr[0];
    var time = orderTimeArr[1];

    var noOpHtml = "";
    // 没有上传确认单时html
    noOpHtml += '<div class="other_lighting">';
    noOpHtml += '<div class="wait_light" date="' + date + '" time="' + time + '" greenTime="' + greenTime + '" yellowTime="' + yellowTime
        + '"></div>';
    noOpHtml += '<div class="useTime"></div>';


    noOpHtml += '<div class="linkman">';
    noOpHtml += '<div class="contact_panel">';
    noOpHtml += '<em class="upArrow"></em>';
    // 计调联系人
    noOpHtml += '<div class="contact_panel_content opPerson">';
    noOpHtml += '</div>';
    noOpHtml += '<em class="downArrow"></em>';
    noOpHtml += '</div>';
    noOpHtml += '</div>';


    noOpHtml += '</div>';
    if ((orderStatus != 3) && (!firstOrderPayTime || firstOrderPayTime == "")) {
        noOpHtml += '<div class="other_lighting"></div>';
    }

    return noOpHtml;
}

function addOpHtml(progress) {
    var confirmationFileSalerId = progress.confirmationFileSalerId;
    var confirmationFileSalerTime = progress.confirmationFileSalerTime;
    var activityCreateName = progress.activityCreateName;
    var firstOrderPayTime = progress.firstOrderPayTime;
    var opHtml = "";
    // 计调信息部分
    if (confirmationFileSalerId && confirmationFileSalerId != "") {
        var date = "";
        var time = "";
        var opName = "";
        if (confirmationFileSalerTime && confirmationFileSalerTime != "") {
            var dateArr = confirmationFileSalerTime.split(" ");
            date = dateArr[0];
            time = dateArr[1];
        }
        opHtml += '<div class="other_lighting">';
        opHtml += '<div class="upward_time">' + date + '<br>' + time + '</div>';
        opHtml += '<span></span>';
        opHtml += '<div class="gray_div"></div>';
        opHtml += '<div class="linkman">';
        opHtml += '<div class="contact_panel">';
        opHtml += '<em class="upArrow"></em>';
        opHtml += '<div class="contact_panel_content opPerson">';
        opHtml += '</div>';
        opHtml += '<em class="downArrow"></em>';
        opHtml += '</div>';
        opHtml += '</div>';
        opHtml += '</div>';
    } else {
        if (firstOrderPayTime && firstOrderPayTime != "") {
            opHtml += '<div class="other_lighting">';
            opHtml += '<span class="closeOrder"></span>';
            opHtml += '<div class="gray_div"></div>';
            opHtml += '</div>';
            opHtml += '<div class="other_lighting"></div>';
        }
    }
    return opHtml;
}

function addCwHtml(progress) {
    var firstOrderPayTime = progress.firstOrderPayTime;
    var lastOrderPayTime = progress.lastOrderPayTime;
    var lastOrderPayName = progress.lastOrderPayName;
    var setting = progress.setting;
    var orderStatus = progress.orderStatus;
    var cwHtml = "";
    if (orderStatus != 3) {
        // 财务信息部分
        if (firstOrderPayTime && firstOrderPayTime != "") {
            cwHtml += '<div class="other_lighting last_no_light">';
            if (lastOrderPayTime && lastOrderPayTime != "") {
                var dateArr = lastOrderPayTime.split(" ");
                cwHtml += '<div class="upward_time">' + dateArr[0] + '<br>' + dateArr[1] + '</div>';
                cwHtml += '<span></span>';
                cwHtml += '<div class="linkman right_contact">';
                cwHtml += '<div class="contact_panel">';
                cwHtml += '<em class="upArrow"></em>';
                cwHtml += '<div class="contact_panel_content cwPerson">';
            } else {

                //有颜色的图标和时间变化
                //绿灯分钟数，黄灯分钟数
                var greenTime = 10;
                var yellowTime = 20;
                if (setting && setting != "") {
                    var setArr = setting.split("+");
                    for (var j = 0; j < setArr.length; j++) {
                        var sets = setArr[j].split(" ");
                        var settingType = sets[0];
                        var greenLightTimeType = sets[1];
                        var greenTime = sets[3];
                        var yellowTime = sets[6];
                        if (settingType == 3) {
                            if (greenLightTimeType == 1) {
                                greenTime = greenTime * 24 * 60;
                                yellowTime = yellowTime * 24 * 60;
                            } else if (greenLightTimeType == 2) {
                                greenTime = greenTime * 60;
                                yellowTime = yellowTime * 60;
                            } else {
                                greenTime = greenTime;
                                yellowTime = yellowTime;
                            }
                            break;
                        }
                    }
                }

                var orderCreateTime = progress.orderCreateTime;
                var orderTimeArr = orderCreateTime.split(" ");
                var date = orderCreateTime[0];
                var time = orderCreateTime[1];


                var dateArr = firstOrderPayTime.split(" ");
                date = dateArr[0];
                time = dateArr[1];
                cwHtml += '<div class="upward_time" style="display:none">' + dateArr[0] + '<br>' + dateArr[1] + '</div>';
                cwHtml += '<div class="wait_light" date="' + dateArr[0] + '" time="' + dateArr[1] + '" greenTime="' + greenTime + '" yellowTime="' + yellowTime
                    + '"></div>';
                cwHtml += '<div class="useTime"></div>';
                cwHtml += '<div class="linkman right_contact">';
                cwHtml += '<div class="contact_panel">';
                cwHtml += '<em class="upArrow"></em>';
                cwHtml += '<div class="contact_panel_content cwPerson">';
            }
            cwHtml += '</div>';
            cwHtml += '<em class="downArrow"></em>';
            cwHtml += '</div>';
            cwHtml += '</div>';
            cwHtml += '</div>';
        }
    } else {
        cwHtml += '<div class="other_lighting last_no_light"><div class="upward_time"></div><span class="closeOrder"></span>';

        cwHtml += '<div class="linkman">';
        cwHtml += '<div class="contact_panel">';
        cwHtml += '<em class="upArrow"></em>';
        // 财务联系人
        cwHtml += '<div class="contact_panel_content cwPerson">';
        cwHtml += '</div>';
        cwHtml += '<em class="downArrow"></em>';
        cwHtml += '</div>';
        cwHtml += '</div>';

        cwHtml += '</div>';
    }

    return cwHtml;
}

/**
 * 添加订单详细信息
 * @param progress
 * @returns {String}
 */
function addDetailHtml(progress) {

	var orderNum = progress.orderNum;
	var askNum = progress.askNum;
	var activityName = progress.acitivityName;
	var groupCode = progress.groupCode;
	var groupOpenDate = progress.groupOpenDate;
	var orderStatusStr = progress.orderStatusStr;
	var orderCreateTime = progress.orderCreateTime;
	var orderPersonNumStr = progress.orderPersonNumStr;
	var freePosition = progress.freePosition;
	var askTime = progress.askTime;
	var agentName = progress.agentName;
	var contactList = progress.contactList;
	var contactPeople = progress.contactPeople;
	var confirmationFileSalerId = progress.confirmationFileSalerId;
	
	var detailHtml = "";
	detailHtml += '<div class="detaile_part">';
	detailHtml += '<span class="order_information"></span>';
	detailHtml += '<div class="orderDetail_bottom">';
	detailHtml += '<em class="upArrow"></em>';
	detailHtml += '<div class="detailOrder">';
	if (orderNum && orderNum != null) {
		detailHtml += '<div class="orderdetail_title">订单号 : ' + orderNum + '</div>';
	} else {
		detailHtml += '<div class="orderdetail_title">询单号 : ' + askNum + '</div>';
	}
	detailHtml += '<div>';
	detailHtml += '<ul class="orderdetail_ul">';
	detailHtml += '<li><label>产品名称：</label><span class="over_text">' + activityName + '</span></li>';
	/*detailHtml += '<li><label>产品团号：</label><span class="over_text">' + groupCode + '</span></li>';
	detailHtml += '<li><label>出团日期：</label><span>' + groupOpenDate + '</span></li>';
	if (orderNum && orderNum != null) {
		detailHtml += '<li><label>订单状态：</label><span>' + orderStatusStr + '</span></li>';
//		detailHtml += '<li><label>下单日期：</label><span>' + orderCreateTime + '</span></li>';
        detailHtml += '<li><label>订单人数：</label><span>' + orderPersonNumStr + '</span></li>';
    } else {
        detailHtml += '<li><label>余位：</label><span>' + freePosition + '</span></li>';
//		detailHtml += '<li><label>询单时间：</label><span>' + askTime + '</span></li>';
	}*/
//	var isQuauqUser = $("#isQuauqUser").val();
//	if (isQuauqUser == "0") {
//		detailHtml += '<li><label>渠道名称：</label><span class="over_text">' + agentName + '</span></li>';
//		if (contactList && contactList != "") {
//
//			var contactArr = contactList.split("+");
//			for (var j = 0; j < contactArr.length; j++) {
//				var otherInfo = "";
//				if (j == 0) {
//					otherInfo = "渠道联系人：";
//				}
//				var contact = contactArr[j];
//				var conArr = contact.split(" ");
//				if (conArr.length == 1) {
//					detailHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] + '</span><em>( <i class="fa fa-phone"></i> )</em></li>';
//				} else {
//					detailHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] +
//					'</span><em>( <i class="fa fa-phone"></i> ' + conArr[1] + ' )</em></li>';
//				}
//			}
//
//		}
//	}
//	if (isQuauqUser == "1" || $("#isQuauqAdmin").val() == "true") {
//		detailHtml += '<li><label>供应商名称：</label><span class="over_text">' + officeName + '</span></li>';
//		// 添加供应商联系人
//		if (contactPeople && contactPeople != "") {
//			var contactArr = contactPeople.split("+");
//			for (var j = 0; j < contactArr.length; j++) {
//				var otherInfo = "";
//				if (j == 0) {
//					otherInfo = "供应商联系人：";
//				}
//				var contact = contactArr[j];
//				var conArr = contact.split(" ");
//				if (conArr.length == 1) {
//					detailHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] + '</span><em>( <i class="fa fa-phone"></i> )</em></li>';
//				} else {
//					detailHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] +
//					'</span><em>( <i class="fa fa-phone"></i> ' + conArr[1] + ' )</em></li>';
//				}
//			}
//
//		} else {
//			detailHtml += '<li><label>供应商联系人：</label><span></span><em>( <i class="fa fa-phone"></i>  )</em></li>';
//		}
//	}
    detailHtml += '</ul>';
    detailHtml += '</div>';
    detailHtml += '</div>';
    detailHtml += '<em class="downArrow"></em>';
    detailHtml += '</div>';
    if (confirmationFileSalerId && confirmationFileSalerId != "") {
        detailHtml += '<span class="confirm_slip"></span>';
        detailHtml += '<div class="confirm_bottom">';
        detailHtml += '<em class="upArrow"></em>';
        detailHtml += '<span>确认单已上传</span>';
    } else {
        detailHtml += '<span class="confirm_slip unVisibility"></span>';
        detailHtml += '<div class="confirm_bottom">';
        detailHtml += '<em class="upArrow"></em>';
    }
    detailHtml += '<em class="downArrow"></em>';
    detailHtml += '</div>';
    detailHtml += '</div>';
    return detailHtml;
}


function allHover() {
    //联系人，hover时利用ajax调取后台相应联系人的数据,并打出
    $(".linkman").hover(function () {
        $(this).find(".contact_panel").show();
    }, function () {
        $(".contact_panel").hover(function () {
            $(this).show();
        }, function () {
            $(this).hide();
        });
        $(this).find(".contact_panel").hide();
    });

    //门店销售hover
    $(".store_saler .store_saler_icon").hover(function () {
        $(this).next().next(".saler_phone").show();
        // $(this).next().next(".saler_phone1").show();
    }, function () {
        $(".saler_phone").hover(function () {
            $(this).show();
        }, function () {
            $(this).hide();
        });
        $(this).next().next(".saler_phone").hide();
    });

    //订单列表hover
    $(".order_information").hover(function () {
        $(this).next(".orderDetail_bottom").show();
    }, function () {
        $(".orderDetail_bottom").hover(function () {
            $(this).show();
        }, function () {
            $(this).hide();
        });
        $(this).next(".orderDetail_bottom").hide();
    });

    //确认单hover
    $(".confirm_slip").hover(function () {
        $(this).next(".confirm_bottom").show();
    }, function () {
        $(this).next(".confirm_bottom").hide();
    });

    //点击切换订单跟踪的数据
    $(".dataStatistics").click(function () {
        $(this).siblings().removeClass("selectData");
        $(this).addClass("selectData");
    });

    //点击右侧列表展示订单的详细信息
    $(".orderT_right_ul li").click(function (event) {
        var $clickevent = $(event.target).parents();
        $this = $(this).closest("li");
        if ($this.hasClass("orRight_li_select")) {
            if ($clickevent.hasClass("linkman") || $clickevent.hasClass("saler_phone") || $clickevent.hasClass("orderDetail_bottom")) {
            } else {
                $this.removeClass("orRight_li_select");
            }
        } else {
            if ($clickevent.hasClass("linkman") || $clickevent.hasClass("saler_phone") || $clickevent.hasClass("orderDetail_bottom")) {
            } else {
                $this.siblings().removeClass("orRight_li_select");
                $this.addClass("orRight_li_select");
            }
        }
    });
}

/**
 * 添加数据加载等待标识
 */
function ajaxLoadStart() {
    var html = '<div class="ajaxLoadingMask"></div><div class="ajaxLoading">正在加载，请等待...</div>';
    $("body").append(html);
}

/**
 * 删除数据加载等待标识
 */
function ajaxLoadStop() {
    $("div.ajaxLoadingMask, div.ajaxLoading").remove();
}

function setOrderColour() {
    // ot_wait样式表示未完成操作下划线
    $(".wait_light").each(function () {

        // 获取红绿灯时间
        var mi = $(this).attr("greenTime");
        var min = $(this).attr("yellowTime");
        var data_str = $(this).attr("date");
        var time_str = $(this).attr("time");

        if (data_str == "" || time_str == "") {
            return true;
        }

        var remindTime = data_str + "," + time_str;

        // 之前的类型为2016-06-05 10:08:44,必须转化为2008/04/02 10:08:44格式才能实例化Date对象
        var str = remindTime.toString();

        str = str.replace(/-/g, "/"); // 将获取到的字符串里面的-更改为/
        var starttime = new Date(str); // 询价时间
        var nowtime = new Date(); // 当前时间

        var lefttime = parseInt((nowtime.getTime() - starttime.getTime()) / 1000);
        var d = parseInt(lefttime / 3600 / 24);
        var h = parseInt((lefttime / 3600) % 24);
        var m = parseInt((lefttime / 60) % 60);
        var s = parseInt(lefttime % 60);
        m = checkTime(m);
        s = checkTime(s);

        // 获取本次操作已用时多少分钟
        var countMinute = lefttime / 60;

        // 根据批发商设置黄绿灯时间设置显示时间颜色及频率
        if (countMinute <= mi) {
            $(this).removeClass("yellow_light").removeClass("red_light").addClass("blue_light");
        } else if (countMinute > mi && countMinute <= min) {
            $(this).removeClass("blue_light").removeClass("red_light").addClass("yellow_light");
        } else {
            $(this).removeClass("blue_light").removeClass("yellow_light").addClass("red_light");
        }
    });
    setTimeout("setOrderColour()", 1000);
}

function getOrderLinkPerson() {
    $(".lighting_part").each(function (index, obj) {
        $liObj = $(this).closest("li");
        $liObj.one("click", function () {
            var orderId = $(obj).attr("lang");
            $.ajax({
                type: "POST",
                async: false,
                url: $("#progressCtx").val() + "/orderProgressTracking/manage/getOrderLinkPerson?dom=" + Math.random(),
                data: {
                    orderId: orderId
                },
                success: function (result) {
                    if (result && result != "") {
                        var personInfoObj = eval(result);
                        // 添加销售联系人
                        var salerPersonArr = personInfoObj.salerPersonInfo;
                        if (salerPersonArr && salerPersonArr.length > 0) {
                            var salerHtml = "";
                            var cancleDateHtml = "";
                            for (var i = 0; i < salerPersonArr.length; i++) {
                                var salerObj = salerPersonArr[i];
                                salerHtml += '<dl>';
                                salerHtml += '<dt>姓名：</dt><dd>' + salerObj.name + '</dd>';
                                salerHtml += '<dt>电话：</dt><dd>' + salerObj.mobile + '</dd>';
                                salerHtml += '</dl>';

                                // 添加取消或删除时间
                                var updateDate = salerObj.updateDate;
                                if (updateDate && updateDate != "") {
                                    var updateDateArr = updateDate.split(" ");
                                    cancleDateHtml += updateDateArr[0] + "<br>" + updateDateArr[1];
                                }
                            }
                            $(obj).find(".salerPerson").html(salerHtml);
                            if (cancleDateHtml != "") {
                                $(obj).find(".upward_time:first").html(cancleDateHtml);
                            }
                            if (salerPersonArr.length == 1) {
                                $(obj).find(".salerPerson").closest(".linkman").removeClass("more_person").addClass("one_person");
                            } else {
                                $(obj).find(".salerPerson").closest(".linkman").removeClass("one_person").addClass("more_person");
                            }
                        }

                        // 添加计调联系人
                        var opPersonArr = personInfoObj.opPersonInfo;
                        if (opPersonArr && opPersonArr.length > 0) {
                            var opHtml = "";
                            for (var i = 0; i < opPersonArr.length; i++) {
                                var opObj = opPersonArr[i];
                                opHtml += '<dl>';
                                opHtml += '<dt>姓名：</dt><dd>' + opObj.name + '</dd>';
                                opHtml += '<dt>电话：</dt><dd>' + opObj.mobile + '</dd>';
                                opHtml += '</dl>';
                            }
                            $(obj).find(".opPerson").html(opHtml);
                            if (opPersonArr.length == 1) {
                                $(obj).find(".opPerson").closest(".linkman").removeClass("more_person").addClass("one_person");
                            } else {
                                $(obj).find(".opPerson").closest(".linkman").removeClass("one_person").addClass("more_person");
                            }
                        }

                        // 添加计调联系人
                        var cwPersonArr = personInfoObj.cwPersonInfo;
                        if (cwPersonArr && cwPersonArr.length > 0) {
                            var cwHtml = "";
                            var cancleDateHtml = "";
                            for (var i = 0; i < cwPersonArr.length; i++) {
                                var opObj = cwPersonArr[i];
                                cwHtml += '<dl>';
                                cwHtml += '<dt>姓名：</dt><dd>' + opObj.name + '</dd>';
                                cwHtml += '<dt>电话：</dt><dd>' + opObj.mobile + '</dd>';
                                cwHtml += '</dl>';

                                // 添加取消或删除时间
                                var updateDate = opObj.updateDate;
                                if (updateDate && updateDate != "") {
                                    var updateDateArr = updateDate.split(" ");
                                    cancleDateHtml += updateDateArr[0] + "<br>" + updateDateArr[1];
                                }
                            }
                            $(obj).find(".cwPerson").html(cwHtml);
                            if (cancleDateHtml != "") {
                                $(obj).find(".upward_time:last").html(cancleDateHtml);
                            }
                            if (cwPersonArr.length == 1) {
                                $(obj).find(".cwPerson").closest(".linkman").removeClass("more_person").addClass("one_person");
                            } else {
                                $(obj).find(".cwPerson").closest(".linkman").removeClass("one_person").addClass("more_person");
                            }
                        }
                    }

                    // bug17707 ie10页面卡顿修改 ymx 2017/3/31 Start
                    // 设置滚动条
                    setdetailOrderScroll($(obj));
                    // setScroll();
                    //bug17707 ie10页面卡顿修改 ymx 2017/3/31 End

                }
            });
        });
    });
}

/**
 * 时间和日期js函数
 * @param i
 * @returns
 */
function checkTime(i) {
    if (i < 10) {
        i = "0" + i
    }
    return i
}