/**
 * Created by changying huo IDEA.
 * User: quauq
 * Date: 2016/12/28
 */
var ctx = $("#ctx").val();
//定义所需发送的参数
var dataParameter = {
    serverTime:'',//获取系统返回的时间，日期格式为'2017-02-15 16:34:55'
    orderType: 0,//产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮)
    analysisType: 3,//分析类型（1：订单数，2：收客人数，3：订单金额）
    searchDate: 1,//时间 1：今日 2：本周 3：本月 4：本年 5：全部
    startDate: '',//自定义开始时间
    endDate: '',//自定义结束时间
    unit: '',//查询单位（查询维度）(h:时、d:天、w:周、m:月)
    unitDays:'',//自定义时间跨度天数
    totalBtn_Date:'',//定义全部按钮从后端取到的开始截止日期数据
    totalDate_gap:''//定义全部所取得的日期天数
}
$(function () {
    //定义分析类型（1：订单数，2：收客人数，3：订单金额）的三种对应颜色
    var dataColor = {
        order_color: ['#ff69c1', '#0092ff', '#6a7984'],
        person_color: ['#ff69c1', '#1edccc', '#6a7984'],
        money_color: ['#ff69c1', '#f08200', '#6a7984']
    }
    $('.orderTrend_right .time .type2').css('display', 'none');
    $('.time_div .time_tab .inputTxt').val('');//清空自定义时间的值
    // 页面刷新时ajax请求加载全部产品类型
    $.ajax({
        type: "post",
        url: ctx + '/statisticHome/orderList',
        async: true,
        success: function (data) {
            if (data) {
                $('.Group_tab>li:first').siblings().remove();
                var data_result = JSON.parse(data);
                var _liHtml = "<li value='0'>全部</li>"
                for (var i = 0; i < data_result.length; i++) {
                    _liHtml += "<li value='" + data_result[i].typeValue + "'>" + data_result[i].typeName + "</li>"
                }
                $('.bgMainRight .Group_tab').html(_liHtml);
                $('.Group_tab>li:first').addClass('active');
                $('.orderTrend_right .line .line2 ').css('backgroundPosition', '-74px -243px');
                // 当打开页面时发送参数请求默认数据
                unitDefault();
                orderTrend(dataParameter, dataColor, false, false);
                $('.orderTrend_right .time .type2').css('display', 'none');
            }
        },
        error: function () {
            alert('请求数据失败，请重新刷新页面');
        }
    });
    getting_data($('.time_div>.time_tab .active'), dataParameter);
    //单团散拼签证点击事件
    $('.main-right .Group_tab>li').live('click', function () {
        $(this).addClass('active').siblings('li').removeClass('active');
        $('.orderTrend_right .compare div').removeClass('active');
        if ($(this).index() > 0) {
            $('.orderTrend_right .compare').css('display', 'block');
        } else {
            $('.orderTrend_right .compare').css('display', 'none');
        }
        unitDefault();
        dataParameter.orderType = $(this).attr('value');
        getting_data($('.time_div>.time_tab .active'), dataParameter);
        orderTrend(dataParameter, dataColor, false, false);
    });
    //订单数收客人数订单金额点击事件
    $('.Condition_div>.Condition_tab>li').click(function () {
        $(this).addClass('active').siblings('li').removeClass('active');
        $('.orderTrend_right .compare div').removeClass('active');
        dataParameter.analysisType = $(this).attr('value');//由于后期将订单金额更改设置为默认，所以value值并不是按照页面1,2,3的顺序进行排列，
        unitDefault();
        var _color = (dataParameter.analysisType == 1 ? dataColor.order_color : (dataParameter.analysisType == 2 ? dataColor.person_color : dataColor.money_color));
        $('.orderTrend_right .time .active').css('backgroundColor', _color[1]);
        orderTrend(dataParameter, dataColor, false, false);
        switch (dataParameter.analysisType) {
            case 1:
                lineColorComm('-104px -108px', '-74px -191px', '-144px -191px', '-10px -191px');
                break;
            case 2:
                lineColorComm('-104px -87px', '-75px -215px', '-144px -215px', '-10px -215px');
                break;
            case 3:
                lineColorComm('-104px -127px', '-74px -244px', '-144px -244px', '-10px -244px');
                break;
        }
        getting_data($('.time_div>.time_tab .active'), dataParameter);
    });
    //今日本周本月本年点击事件
    $('.time_div>.time_tab>li:lt(4)').click(function () {
        $('.time_tab .back .make_sure').css({
            'color': '#333',
            'background': '#e3e3e3',
            'display': 'none'
        })
        $(this).addClass('active')
            .siblings('li')
            .removeClass('active');
        dataParameter.unitDays = '';
        dataParameter.startDate = '';
        dataParameter.endDate = '';
        $('.orderTrend_right .compare div').removeClass('active');
        $('.time_div .time_tab .inputTxt').val('');//清空自定义时间的值
        dataParameter.searchDate = $(this).index() + 1;
        unitDefault();
        orderTrend(dataParameter, dataColor, false, false);
        getting_data(this, dataParameter);
    });
    //全部按钮的点击事件，从后端传来起始和截止日期显示出正常的时天周月年
    $('.time_div>.time_tab>li:eq(4)').click(function () {
        $(this).addClass('active')
            .siblings('li')
            .removeClass('active');
        dataParameter.unitDays = '';
        dataParameter.startDate = '';
        dataParameter.endDate = '';
        $('.time_div .time_tab .inputTxt').val('');//清空自定义时间的值
        $.ajax({
            type: "post",
            url: ctx + "/statisticAnalysis/orderData/getTime",
            async: true,
            success: function (data) {
                if (data) {
                    dataParameter.totalBtn_Date = data;
                    var _maxDateArray = data.maxTime.split(' ');
                    var _maxDateArrayFront = _maxDateArray[0].split('-');
                    var _maxDateArrayBack = _maxDateArray[1].split(':');
                    if (_maxDateArrayBack.length == 2) {
                        _maxDateArrayBack.push("00");
                    }
                    var _minDateArray = data.minTime.split(' ');
                    var _minDateArrayFront = _minDateArray[0].split('-');
                    var _minDateArrayBack = _minDateArray[1].split(':');
                    if (_minDateArrayBack.length == 2) {
                        _minDateArrayBack.push("00");
                    }
                    var _maxdefineDate = new Date(_maxDateArrayFront[0], _maxDateArrayFront[1] - 1, _maxDateArrayFront[2], _maxDateArrayBack[0], _maxDateArrayBack[1], _maxDateArrayBack[2]);
                    var _mindefineDate = new Date(_minDateArrayFront[0], _minDateArrayFront[1] - 1, _minDateArrayFront[2], _minDateArrayBack[0], _minDateArrayBack[1], _minDateArrayBack[2]);
                    dataParameter.totalDate_gap = Math.ceil((_maxdefineDate - _mindefineDate) / 86400000) + 1;
                    dataParameter.searchDate = 5;
                    unitDefault();
                    orderTrend(dataParameter, dataColor, false, false);
                    //当点击全部按钮时转为true，让增长率文字不显示,即最后一个参数
                    getting_data(this, dataParameter);
                }
            },
            error: function () {
                alert('传输数据失败');
            }
        });

    })
    //自定义时间input框点击，显示确定按钮
    $('.time_tab .back .inputTxt').click(function () {
        $('.time_tab .back .make_sure').css('display', 'inline-block');
    })
    //自定义时间的确定点击事件
    $('.time_tab .back .make_sure').click(function () {
        $(this).css({
            'color': '#FFFFFF',
            'background': '#4491e9',
        })
        $('.orderTrend_right .line .line_center ').css('display', 'none');
        $('.orderTrend_right .line .line_left ').css('display', 'none');
        $('.time_div>.time_tab>.front').removeClass('active');
        $('.orderTrend_right .compare').css('display', 'none');
        dataParameter.searchDate = "";
        dataParameter.startDate = $("#orderTimeBegin").val();
        dataParameter.endDate = $("#orderTimeEnd").val();
        if (dataParameter.startDate == '') {
            $.jBox.tip('请正确填写开始日期', 'warnning');
        } else if (dataParameter.endDate == '') {
            $.jBox.tip('请正确填写截止日期', 'warnning');
        } else {
            var _startDateArray = dataParameter.startDate.split('-');
            var _endDateArray = dataParameter.endDate.split('-');
            var defineDateStart = new Date(_startDateArray[0],_startDateArray[1]-1,_startDateArray[2],"","","");
            var defineDateEnd = new Date(_endDateArray[0],_endDateArray[1]-1,_endDateArray[2],"","","");
            dataParameter.unitDays = Math.ceil((defineDateEnd - defineDateStart) / 86400000)+1;
            getting_data(this, dataParameter);
            unitDefault();
            orderTrend(dataParameter, dataColor, false, false);
        }
    })
    //图表中时与天的点击事件
    $('.orderTrend_div .orderTrend_right>.time>div').click(function () {
        $('.orderTrend_right .compare .compare_square').removeClass('active')
        $('.orderTrend_right .line .line_center ').css('display', 'none');
        $('.orderTrend_right .line .line_left ').css('display', 'none');
        //查询单位（查询维度）定义
        var self = $(this);
        var _paraCondition = conditionJudgeArr(dataParameter);
        if (_paraCondition[0]) {
            dataParameter.unit = "h";
        } else if (_paraCondition[1]) {
            lineUnitComm("h", "d", self)
        } else if (_paraCondition[2]) {
            dataParameter.unit = "d";
        } else if (_paraCondition[3]) {
            lineUnitComm("d", "w", self)
        } else if (_paraCondition[4]) {
            lineUnitComm("w", "m", self)
        } else if (_paraCondition[5]) {
            lineUnitComm("m", "y", self)
        }
        orderTrend(dataParameter, dataColor, false, false);
        $(this).addClass('active').siblings('div').removeClass('active');
    });
    //图表中上一周点击事件
    $('.orderTrend_right #compare1').click(function () {
        $(this).find('.compare_square').toggleClass('active');
        if ($(this).find('.compare_square').hasClass('active')) {
            $('.orderTrend_right .line .line_left').css('display', 'inline-block');
        } else {
            $('.orderTrend_right .line .line_left').css('display', 'none');
        }
        switch (dataParameter.analysisType) {
            case 1:
                lineColorComm('-104px -108px', null, '-144px -191px', null);
                break;
            case 2:
                lineColorComm('-104px -87px', null, '-144px -215px', null);
                break;
            case 3:
                lineColorComm('-104px -127px', null, '-144px -244px', null);
                break;
        }
        if (($(this).find('.compare_square').hasClass('active')) && ($('.orderTrend_right #compare2 .compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, true, true);
        } else if (($(this).find('.compare_square').hasClass('active')) && (!$('.orderTrend_right #compare2 .compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, true, false);
        } else if ((!$(this).find('.compare_square').hasClass('active')) && ($('.orderTrend_right #compare2 .compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, false, true);
        } else if ((!$(this).find('.compare_square').hasClass('active')) && (!$('.orderTrend_right #compare2 .compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, false, false);
        }
    });
    //图表中上月同期点击事件
    $('.orderTrend_right .compare #compare2').click(function () {
        $(this).find('.compare_square').toggleClass('active');
        if ($(this).find('.compare_square').hasClass('active')) {
            $('.orderTrend_right .line .line_center').css('display', 'inline-block');
        } else {
            $('.orderTrend_right .line .line_center').css('display', 'none');
        }
        switch (dataParameter.analysisType) {
            case 1:
                lineColorComm('-104px -108px', null, null, '-12px -191px');
                break;
            case 2:
                lineColorComm('-104px -87px', null, null, '-11px -215px');
                break;
            case 3:
                lineColorComm('-104px -127px', null, null, '-11px -244px');
                break;
        }
        if (($(this).find('.compare_square').hasClass('active')) && ($('.orderTrend_right #compare1').find('.compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, true, true);
        } else if (($(this).find('.compare_square').hasClass('active')) && (!$('.orderTrend_right #compare1').find('.compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, false, true);
        } else if ((!$(this).find('.compare_square').hasClass('active')) && ($('.orderTrend_right #compare1').find('.compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, true, false);
        } else if ((!$(this).find('.compare_square').hasClass('active')) && (!$('.orderTrend_right #compare1').find('.compare_square').hasClass('active'))) {
            orderTrend(dataParameter, dataColor, false, false);
        }
    });
    //点击右上角刷新折线图数据
    $('.orderTrend_div .pull-right .left').click(function () {
        $('.orderTrend_right .compare .compare_square').removeClass('active');
        unitDefault();
        orderTrend(dataParameter, dataColor, false, false);
    });
    getting_data($('.time_div>.time_tab .active'), dataParameter);
});

/**
 * 新增趋势图整体包装成函数
 * @param dataParameter 传入后台的参数对象
 * @param yAxisName 对坐标轴单位名称的设定传入
 * @param _color 相对应分析类型的颜色
 * @param boolean1 布尔值决定前一日前一周前一月或者去年的显示与否，false为不显示
 * @param boolean2 布尔值决定上周同期上月同期或者去年同期的显示与否，false为不显示
 * @author liuzhaoli
 * @createDate 2017/1/1
 */
function orderTrend(dataParameter, dataColor, boolean1, boolean2) {
    $('#increaseShow').empty().addClass('loading_img');
    if (dataParameter.unitDays || dataParameter.searchDate == 5) {
        $('.orderTrend_right .compare').css('display', 'none')
    }
    $.ajax({
        type: "post",
        url: ctx + "/statisticAnalysis/orderData/orderDataHomePage",
        data: {
            'orderType': dataParameter.orderType,
            'analysisType': dataParameter.analysisType,
            'searchDate': dataParameter.searchDate,
            'startDate': dataParameter.startDate,
            'endDate': dataParameter.endDate,
            'unit': dataParameter.unit
        },
        async: true,
        success: function (data) {
            //对点击后的页面展示进行设置
            //当选择时间不为自定义时间时隐藏确定按钮
            var lineShow_para = linefaceShow(dataParameter);
            var _color = (dataParameter.analysisType == 1 ? dataColor.order_color : (dataParameter.analysisType == 2 ? dataColor.person_color : dataColor.money_color));
            var yAxisName = (dataParameter.analysisType == 1 ? '订单数（单位：单）' : (dataParameter.analysisType == 2 ? '收客人数（单位：人）' : '订单金额（单位：元）'));
            $('.orderTrend_right .time .active').css({
                'background-color': _color[1],
                'color': '#F6F9FE'
            }).siblings('div')
                .css({
                    'background-color': '#fff',
                    'color': '#333'
                });
            //对data数据进行处理
            if (data) {
                var data = JSON.parse(data);
                var _newNumUse=fmoney(data.newNum,0);
                $('.orderTrend_div_header .orderTrend_new_header span').empty().text(lineShow_para[0]);
                var increaseShow_html = "";
                if ($(".time_div .time_tab .active").text() == "") {
                    increaseShow_html = '<p><span class="header_taggle">' + $("#orderTimeBegin").val() + '&nbsp至&nbsp' + $("#orderTimeEnd").val() + '</span></p>';
                } else if ($(".time_div .time_tab .active").text() == "全部") {
                    increaseShow_html = '<p><span class="header_taggle">' + dataParameter.totalBtn_Date.minTime.substring(0, 10) + '&nbsp至&nbsp' + dataParameter.totalBtn_Date.maxTime.substring(0, 10) + '</span></p>';
                } else {
                    increaseShow_html =
                        '<p><span class="header_taggle">' + $(".time_div .time_tab .active").text() + '</span>新增：</p>';
                }
                if (data.newNum || data.newNum == 0) {
                    if (dataParameter.analysisType == 3) {
                        increaseShow_html += '<h3><span id="increase_rmb">￥</span><span class="increase_number">' + data.newNum + '</span><em class="incease_type"></em></h3>'//' + lineShow_para[1] + '加入em后先后后面单位元
                    } else {
                        increaseShow_html += '<h3><span class="increase_number">' + _newNumUse + '</span><em class="incease_type">' + lineShow_para[1] + '</em></h3>'
                    }
                }
                if (data.incrementRate) {
                    if (data.incrementRate == '0%') {
                        increaseShow_html += '<h4><i>增长率：未变化</i></h4>'
                    } else {
                        increaseShow_html += '<h4><i>增长率：</i><span class="incease_rate">' + data.incrementRate + '</span><em class="increase_arrow DA_img"></em></h4>'
                    }
                    $('#increaseShow').empty().append(increaseShow_html);
                    var regIncrementRate = /[-]/;
                    if (regIncrementRate.test(data.incrementRate)) {
                        $('#increaseShow h4 .incease_rate').css('color', '#33C400');
                        $('#increaseShow h4 .increase_arrow').css('backgroundPosition', '-50px -99px');
                    }
                } else {
                    increaseShow_html += '';
                    $('#increaseShow').empty().append(increaseShow_html);
                }
                $('#increaseShow').removeClass('loading_img');
                $('#increaseShow h3').css('color', _color[1]);
                $('#increaseShow h4').css('background-color', '#f2f6fc');
                if(data.serverTime){
                    $('.orderTrend_new_header .pull-right .center .hover_box>div:eq(2)').text(data.serverTime);
                    dataParameter.serverTime = data.serverTime;
                }
                var timeRegionArray = [];
                var ordernumArray_dot = [];
                var ordernumArray = [];
                var fordernumArray = [];
                var sordernumArray = [];
                if (data.list) {
                    for (var i = 0; i < data.list.length; i++) {
                        if (data.list[i] && data.list[i].timeRegion) {
                            timeRegionArray.push(data.list[i].timeRegion);
                        }
                        if (data.list[i] && data.list[i].fordernum) {
                            fordernumArray.push(data.list[i].fordernum);
                        }
                        if (data.list[i] && data.list[i].sordernum) {
                            sordernumArray.push(data.list[i].sordernum);
                        }
                        if (data.list[i] && data.list[i].ordernum) {
                            ordernumArray.push(data.list[i].ordernum);
                            ordernumArray_dot.push("");
                        }
                    }
                    //若为自定义时间，设置非当天时间，没有虚线，若有当天则最后一天为虚线
                    if (data.list[data.list.length - 2] && data.list[data.list.length - 1] && data.list[data.list.length - 1].ordernum && data.list[data.list.length - 2].ordernum) {
                        //此处将订单数数组中最后一个改为""，将点折线的最后两个改为订单数数组的最后两个数字，
                        ordernumArray.pop();
                        ordernumArray.push("");
                        ordernumArray_dot.pop();
                        ordernumArray_dot.pop();
                        ordernumArray_dot.push(data.list[data.list.length-2].ordernum);
                        ordernumArray_dot.push(data.list[data.list.length-1].ordernum);
                        if(dataParameter.unitDays){
                            var today = dataParameter.serverTime.substr(0,10);
                            var _endDate = dataParameter.endDate;
                            if(today != _endDate){//若自定义结束时间为当日则保留虚线，否则没有虚线
                                ordernumArray_dot = [];
                                ordernumArray.pop();
                                ordernumArray.pop();
                                ordernumArray.push(data.list[data.list.length - 2].ordernum);
                                ordernumArray.push(data.list[data.list.length - 1].ordernum);
                            }
                        }
                    }
                    var lineArray = [timeRegionArray, ordernumArray, fordernumArray, sordernumArray, ordernumArray_dot];
                    //调用折线图
                    if (boolean1 == false) {
                        lineArray[2] = lineArray[3];
                        lineShow_para[2][1] = lineShow_para[2][2];
                    } else if (boolean1 == true) {
                    }
                    if (boolean2 == false) {
                        lineArray[3] = [];
                    } else if (boolean2 == true) {
                    }
                    LineCharts(lineArray, yAxisName, _color, lineShow_para, boolean1);
                }
            }
        },
        error: function () {
            $('#lineErrorImg').css('display', 'inline-block')
        }
    });
}
/**
 * 重置浏览器窗口图表随之变化
 * @param Chart 图表
 */
function resize_window(Chart) {
    $(window).resize(function () {
        Chart.resize();
    });
}
/**
 * 数据更新时间
 * @param obj 本页中指的是dataParameter对象
 */
// function setTime(obj) {
//     var date, Y, M, D, h, m, s, now_time;
//     if(obj.serverTime){
//         var serverTime = obj.serverTime;
//         date = setServerTimeDate(serverTime);
//         Y = date.getFullYear() + '-';
//         M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
//         D = date.getDate() < 10 ? '0' + date.getDate() + ' ' : date.getDate() + ' ';
//         h = date.getHours() < 10 ? '0' + date.getHours() + ':' : date.getHours() + ':';
//         m = date.getMinutes() < 10 ? '0' + date.getMinutes() + ':' : date.getMinutes() + ':';
//         s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
//         now_time = Y + M + D + h + m + s;
//     }
//     return now_time;
// }
/**
 * 显示数据格式化
 */
function fmoney(s, n) {
    n = n > -1 && n <= 20 ? n : 2;
    s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
    var l = s.split(".")[0].split("").reverse(), r = s.split(".")[1];
    var t = "";
    for (i = 0; i < l.length; i++) {
        t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
    }
    if (n == 0) {
        return t.split("").reverse().join("");
    }
    return t.split("").reverse().join("") + "." + r;
}
/**
 * 数据调用起始函数
 * @param obj 时间周期字段使用
 * @param dataParameter 数据
 */
function getting_data(obj, dataParameter) {
    // var use_num;
    choose_use(ctx, dataParameter, undefined, obj);
}
/**
 * 类型颜色等样式判断
 * @param  _data 数据
 * @param  ctx   路径
 * @param  use_num   判断刷新范围
 * @param obj 时间周期字段使用
 */
function choose_use(ctx, _data, use_num, obj) {
    var _color1, _color2, _color3, _taggle_use, unit, _text1, _text2, _header_taggle;
    _header_taggle = $('.header_taggle');
    _taggle_use = $('.taggle_use');
    unit = $('.unit');
    switch (_data.analysisType) {
        case 1:
            _text1 = '订单数';
            _text2 = '单';
            _color1 = '#53a4ff';
            _color2 = '#4491e7';
            _color3 = ['#114987', '#155baa', '#196cc9', '#1b78df', '#5099ea'];
            break;
        case 2:
            _text1 = '收客人数';
            _text2 = '人';
            _color1 = '#39f5e5';
            _color2 = '#1edccc';
            _color3 = ['#12867c', '#17a79b', '#1bc7b8', '#1edccc', '#52e7db'];
            break;
        case 3:
            _text1 = '订单金额';
            _text2 = '元';
            _color1 = '#ffab47';
            _color2 = '#f08200';
            _color3 = ['#874f11', '#aa6415', '#c97619', '#df821b', '#eaa150'];
            break;
        default:
        //默认操作
    }
    _taggle_use.text(_text1);
    unit.text(_text2);
    same_use(ctx, _data, _color1, _color2, _color3, _text1, _text2, use_num);
    if (_data.searchDate != '') {
        _header_taggle.text($(obj).text());
    } else {
        _header_taggle.text('');
    }
}
/**
 * 饼状图刷新
 */
$('.pieChart_header').find('.left').click(function () {
    var _use_num = 1;
    var _obj = $('.time_div>.time_tab .active');
    choose_use(ctx, dataParameter, _use_num, _obj);
});
/**
 *跳转
 */
$('.href_use').click(function () {
    var _id = $(this).attr('data-id');
    var _data = dataParameter;
    window.open(ctx + "/agent/statistic/agentStatistic?orderType=" + _data.orderType +
        "&analysisType=" + _data.analysisType + "&searchDate=" + _data.searchDate +
        "&startDate=" + _data.startDate + "&endDate=" + _data.endDate + "&pageTab=" + _id);
});
/**
 * 柱状图刷新
 */
$('.barChart_header').find('.left').click(function () {
    var _use_num = 2;
    var _obj = $('.time_div>.time_tab .active');
    choose_use(ctx, dataParameter, _use_num, _obj);
});
/**
 * 更改颜色单位的样式的公共方法
 * @param ctx 路径
 * @param _data 数据
 * @param color1 图表颜色
 * @param color2 图表颜色
 * @param color3 图表颜色（数组）
 * @param text1  数据类型：人数、金额等
 * @param text2 单位：单、人、元
 * @param  use_num   判断刷新范围
 */
function same_use(ctx, _data, color1, color2, color3, text1, text2, use_num) {
    var _color_use = $('.color_blue');
    var _chart_footer = $('.chart_footer');
    if (use_num == 1) {
        channel_Customer(ctx, _data, color3, text1, text2);
    } else if (use_num == 2) {
        sales_Customer(ctx, _data, color1, color2, text1, text2);
    } else {
        channel_Customer(ctx, _data, color3, text1, text2);
        sales_Customer(ctx, _data, color1, color2, text1, text2);
        product_Customer(ctx, _data, color2, text2);
    }
    _color_use.css('color', color2);
    _chart_footer.css('color', color2);
}
/**
 * 页面底部渠道订单
 * @param ctx 路径
 * @param _data 数据
 * @param color  颜色
 * @param text1   数据类型：人数、金额等
 * @param text2   单位：单、人、元
 */
function channel_Customer(ctx, _data, color, text1, text2) {
    var _time = $(".pieChart").find(".hover_box div").eq(2);
    var _pie_other = $("#pie_other");
    var _data_use = JSON.stringify(_data);
    $.ajax({
        type: "POST",
        url: ctx + '/agent/statistic/agentStatisticPicture',
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            param: _data_use
        },
        beforeSend: function () {
            var _body = $('.pieChart_body');
            _body.removeClass('noData_01').text('加载中...');
        },
        success: function (data) {
            var _body = $('.pieChart_body');
            var _list = data.list;
            var _tempArray;
            if (_list && _list.length > 0) {
                _body.html('');
                _tempArray = _list.sort(function (a, b) {
                    return a.value - b.value
                });
                pieChart(_tempArray, color, text1, text2);
                _body.next().children().find('.taggle_use_left').text('其他渠道总');
                _body.next().children().find('.taggle_use_right').text('（包含非签约渠道）：');
                _body.next().children().show();
            } else if (data.otheragent == 0 || data.otheragent == '' || data.otheragent == null) {
                // _body.text('当前图表暂无数据');
                _body.addClass('noData_01').removeClass('noWord_01').removeAttr('style').text('');
                _body.next().children().hide();
            } else {
                _body.addClass('noWord_01').removeClass('noData_01').removeAttr('style').text('');
                _body.next().children().find('.taggle_use_left').text('非签约渠道');
                _body.next().children().find('.taggle_use_right').text('：');
                _body.next().children().show();
            }
            // _time.text(setTime(dataParameter));
            _time.text(data.serverTime);
            _pie_other.text(data.otheragent)
        },
        error: function () {
            var _body = $('.pieChart_body');
            _body.text('请求失败');
        }
    })
}
/**
 * 饼状图标函数
 * @param _tempArray    从小到大的数据
 * @param color         图表用到的颜色
 * @param text1         数据类型：人数、金额等
 * @param text2         单位：单、人、元
 * @param shadowBlur         IE 没有阴影
 */
function pieChart(_tempArray, color, text1, text2, shadowBlur) {
    var pieChart = echarts.init(document.getElementById('pieChart'));
    var pieChart_option = {

        backgroundColor: '#FFFFFF',
        tooltip: {
            show: true,
            trigger: 'item',
            formatter: function (val) {
                var _value;
                if (text2 == '元') {
                    _value = fmoney(val.value, 2);
                    return val.name + "<br/>" + text1 + "：" + _value + " " + text2;
                } else {
                    _value = fmoney(val.value, 0);
                    return val.name + "<br/>" + text1 + "：" + _value + " " + text2;
                }
            },
            padding: [5, 10]
        },
        color: color,
        series: [
            {
                name: '旅行社',
                type: 'pie',
                radius: '65%',
                center: ['50%', '50%'],
                data: _tempArray,
                // roseType: 'angle',
                label: {
                    position: 'top',
                    normal: {
                        textStyle: {
                            color: '#333'
                        },
                        formatter: function (val) {
                            //return val.name.replace(/(.{5})/g,'$1\n'); // 让series 中的文字进行换行
                            if (val.name.length > 5) {
                                var _val = val.name.substring(0, 5) + "...";  // 让series 中的文字超出5个显示...
                            }
                            return _val;
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        shadowColor: 'rgba(0, 0, 0, 0.5)',
                        shadowBlur: shadowBlur
                    }
                }
            }
        ]
    };
    pieChart.setOption(pieChart_option);
    resize_window(pieChart);
}
/**
 * 页面底部销售订单
 * @param ctx 路径
 * @param _data 数据
 * @param color1 图表颜色
 * @param color2 图表颜色
 * @param text1  数据类型：人数、金额等
 * @param text2 单位：单、人、元
 */
function sales_Customer(ctx, _data, color1, color2, text1, text2) {
    var _time = $(".barChart").find(".hover_box div").eq(2);
    var _pie_other = $("#bar_other");
    var _data_use = JSON.stringify(_data);
    $.ajax({
        type: "POST",
        url: ctx + '/statisticAnalysis/sale/getSaleTop',
        cache: false,
        dataType: "json",//返回的数据类型
        data: {
            param: _data_use
        },
        beforeSend: function () {
            var _body = $('.barChart_body');
            _body.removeClass('noData_02').text('加载中...');
        },
        success: function (data) {
            var otherData = data.otherData ? data.otherData : 0;
            var _body = $('.barChart_body');
            var _list = data.saleData;
            var _salerName = [];
            var _orderNum = [];
            if (data && _list && _list.length > 0 && _list[0].saleNum != 0) {
                _body.html('');
                for (var i = 0; i < _list.length; i++) {
                    _salerName.unshift(_list[i].saleName);
                    _orderNum.unshift(_list[i].saleNum);
                }
                barChart(_salerName, _orderNum, color1, color2, text1, text2);
                if (data.saleSum && data.saleSum > 5) {
                    _body.next().children().show();
                } else {
                    _body.next().children().hide();
                }
            } else {
                // _body.text('当前图表暂无数据');
                _body.addClass('noData_02').removeAttr('style').text('');
                _body.children().hide();
                _body.next().children().hide();
            }
            // _time.text(setTime(dataParameter));
            _time.text(data.serverTime);
            _pie_other.text(otherData);
        },
        error: function () {
            var _body = $('.barChart_body');
            _body.text('请求失败');
        }
    })
}
/**
 * 柱状图图表数据
 * @param salerName  图表Y轴数值
 * @param orderNum   图表X周数值
 * @param color1     图表用到的颜色
 * @param color2     图表用到的颜色
 * @param text1      数据类型：人数、金额等
 * @param text2      单位：单、人、元
 */
function barChart(salerName, orderNum, color1, color2, text1, text2) {
    var _max_value;
    var barChart = echarts.init(document.getElementById('barChart'));
    var barChart_option = {
        color: ['#3398DB'],
        tooltip: {
            formatter: function (val) {
                var _value;
                if (text2 == '元') {
                    _value = fmoney(val[0].value, 2);
                    return val[0].name + "<br/>" + text1 + "：" + _value + " " + text2;
                } else {
                    _value = fmoney(val[0].value, 0);
                    return val[0].name + "<br/>" + text1 + "：" + _value + " " + text2;
                }
            },
            trigger: 'axis',
            axisPointer: {            // 坐标轴指示器,坐标轴触发有效
                type: false        // 默认为直线,可选为：'line' | 'shadow'
            },
            padding: [5, 10]
        },
        grid: {
            left: '6%',
            right: 100,
            top: '10%',
            bottom: '9%',
            containLabel: true
        },
        yAxis: [
            {
                type: 'category',
                data: salerName,

                axisTick: {
                    show: false
                },
                splitLine: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: '#b3b3b3'
                    }
                }
            }
        ],
        xAxis: [
            {
                type: 'value',
                name: '(' + text2 + ')',
                nameLocation: 'end',
                nameGap: 15,
                axisTick: {
                    show: false
                },
                axisLine: {
                    lineStyle: {
                        color: '#b3b3b3'
                    }
                },
                splitLine: {
                    show: false
                },
                min: 0,
                minInterval: 1
            }
        ],
        series: [
            {
                name: text1,
                type: 'bar',
                label: {
                    normal: {
                        show: true,
                        position: 'right',
                        formatter: function (val) {
                            var _value;
                            if (text2 == '元') {
                                _value = fmoney(val.value, 2);
                                return _value;
                            } else {
                                _value = fmoney(val.value, 0);
                                return _value;
                            }
                        },
                        textStyle: {
                            color: '#333'
                        }
                    }
                },
                barWidth: '25px',
                data: orderNum,
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 1, 0, [{   //渐变
                            offset: 0,
                            color: color1                                       //浅
                        }, {
                            offset: 1,
                            color: color2                                       //深
                        }])
                    },
                    emphasis: {
                        color: color2
                    }
                }
            }
        ]
    };
    _max_value = Math.max.apply(null, orderNum);
    if (_max_value == 1) {
        barChart_option.xAxis[0].splitNumber = 1;
    } else {
        barChart_option.xAxis[0].splitNumber = 2;
    }
    barChart.setOption(barChart_option);
    resize_window(barChart);
}
/**
 * 页面底部产品订单
 * @param ctx 路径
 * @param _data 数据
 * @param color2 颜色
 * @param _text 单位：单、人、元
 */
function product_Customer(ctx, _data, color2, _text) {
    var _data_use = JSON.stringify(_data);
    $.ajax({
        type: "POST",
        url: ctx + '/statisticHome/productHomeList',
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            param: _data_use
        },
        beforeSend: function () {
            var orderList_body = $(".orderList_body");
            orderList_body.text('加载中...');
        },
        success: function (data) {
            var _order_right, _border,_num;
            var orderList_body = $(".orderList_body");
            orderList_body.empty().removeClass('noData_03');
            var _html = '';
            if (data && data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    if (data[i].productName) {
                        _html += '<div class="orderList_use">';
                        _html += '<div class="order_left" title="' + data[i].productName + '">' + data[i].productName + '</div>';
                        if(_data_use.analysisType!=3){
                             _num=fmoney(data[i].num,0);
                        }
                        _html += '<div class="order_right"><span class="font_14">' + _num + '</span>&nbsp<span class="unit">' + _text + '</span></div></div>';
                    }
                }

                orderList_body.append(_html);
                _border = $('.orderList_use');
                _order_right = $('.order_right');
                _order_right.css('color', color2);
                _border.css('border-left', '3px solid ' + color2);
            } else {
                // orderList_body.text('当前暂无数据');
                orderList_body.addClass('noData_03').removeAttr('style');
            }
        },
        error: function () {
            var orderList_body = $(".orderList_body");
            orderList_body.text('当前暂无数据');
        }
    })
}
/**
 * 折线图图标函数
 * @param lineArray  每条折现显示数据组成的数组
 * @param yAxisName    Y轴显示单位
 * @param _color         每条折线的颜色
 * @param lineShow_para[2]  浮层显示文字
 * @param lineShow_para[1]  浮层显示文字的单位
 * @param boolean1  判断前一日或者前一周或者前一月折线是否显示，true为显示，false为不显示
 */
function LineCharts(lineArray, yAxisName, _color, lineShow_para, boolean1) {
    /*此部分为新增订单趋势echarts图----开始*/
    var containerCharts = echarts.init(document.getElementById("container"));
    //封装成函数，将悬浮层字符串return出来，防止改变数组
    if (boolean1 == false) {
        lineArray[2] = [];
    }
    // 浮层显示几行的判断
    var maxNumArr = [];//取每条折线的最大值组成数组maxNumArr，然后取所有最大值中的最小值maxArrNum
    for (var i = 1; i <= 3; i++) {
        if (lineArray[i].length > 0) {
            maxNumArr.push(Math.max.apply(null, lineArray[i]));
        }
    }
    maxNumArr.sort(function (a, b) {
        return a - b;
    });
    var maxArrNum = maxNumArr.shift();
    var splitNumberLine = null;
    if (maxArrNum) {
        if (maxArrNum > 0 && maxArrNum <= 4) {
            splitNumberLine = maxArrNum;
            if (maxArrNum == 1) {
                splitNumberLine = 1;
            }
        } else if (maxArrNum === 0) {
            splitNumberLine = 1;
        } else {
            splitNumberLine = 5;
        }
    } else {
        splitNumberLine = 1;
    }
    var containerOption = {
        title: {
            show: false
        },
        tooltip: {
            show: true,
            borderWidth: 0,
            trigger: 'axis',
            confine: false,
            transitionDuration: 0.4,
            formatter: function (val) {
                var stringBuffer = '<div class="float_layer"><div class="float_layer2">'
                var tempString = '';
                var _isOrdernumShow = lineFloatlayerJudge(lineArray[1]);//判断订单数是否需要展示，Boolean
                var _isOrdernumShow_dot = lineFloatlayerJudge(lineArray[4]);//判断虚线订单数是否需要展示，Boolean
                var _isFordernumShow = lineFloatlayerJudge(lineArray[2]);//判断第一对比订单数是否需要展示，Boolean
                var _isSordernumShow = lineFloatlayerJudge(lineArray[3]);//判断第2对比订单数是否需要展示，Boolean
                var _lineName = [val[0].seriesName, val[1].seriesName, val[2].seriesName];//讲需要在弹框中展示的数据名，放入数组中
                var _value = null;
                var _timeShow = null;
                var _val_next_hour = val[3].name.substring(val[3].name.length - 5, val[3].name.length - 3);
                if (dataParameter.unitDays) {
                    if (dataParameter.unitDays > 0 && dataParameter.unitDays <= 8) {
                        _timeShow = val[3].name + "-" + _val_next_hour + ":59"//如果x轴显示小时，变量表示"02-01 8:00-8:59"
                        if (dataParameter.unit == 'd') {
                            _timeShow = val[3].name;
                        }
                    } else {
                        _timeShow = val[3].name;
                    }
                } else {
                    if (dataParameter.unit == 'h') {
                        _timeShow = val[3].name + "-" + _val_next_hour + ":59"
                    } else {
                        _timeShow = val[3].name;
                    }
                }
                if (lineShow_para[1] == '元') {//若为金额，则讲金额格式化为没三位数字用逗号隔开，否则不做处理
                    _value = [fmoney(val[2].value, 2), fmoney(val[1].value, 2), fmoney(val[0].value, 2)];
                    _value[0] = val[2].value ? fmoney(val[2].value, 2) : fmoney(val[3].value, 2);

                } else {
                    _value = [fmoney(val[2].value, 0), fmoney(val[1].value, 0), fmoney(val[0].value, 0)];
                    _value[0] = val[2].value ? fmoney(val[2].value, 0) : fmoney(val[3].value, 0);
                }
                //以下为将数据呈现在弹出框中，拼接字符串
                if (_timeShow.length == 4) {
                    stringLengthJudge(0,0);
                }else if (_timeShow.length == 5) {
                    stringLengthJudge(4,0);
                } else if (_timeShow.length == 11) {
                    if(_timeShow.indexOf(':')>0){
                        stringLengthJudge(0,6);
                    }else{
                        stringLengthJudge(0,9);
                    }
                } else if (_timeShow.length == 17) {
                    stringLengthJudge(0,16);
                } else if (_timeShow.length == 16) {
                    stringLengthJudge(0,15);
                }
                stringBuffer += tempString;
                stringBuffer += '</div>';
                return stringBuffer;
                /**
                 * 信息提示弹框中日期后的空格个数判断，为了上下冒号对齐
                 * @param num1为第一行日期后的空格个数
                 * @param num2为第二、三行日期后的空格个数
                 * @author liuzhaoli
                 * @createDate  2017/2/21
                 */
                function stringLengthJudge(num1,num2){
                    var _tempString1 = '';
                    for(var i = 0;i<num1;i++){
                        _tempString1+='&nbsp;'
                    }
                    var _tempString2 = '';
                    for(var j = 0;j<num2;j++){
                        _tempString2+='&nbsp;'
                    }
                    (_isOrdernumShow || _isOrdernumShow_dot) ? (tempString += _timeShow + _tempString1 +'：' + _value[0] + lineShow_para[1] + '<br />') : (tempString += '' );
                    if (boolean1 == true) {
                        _isFordernumShow ? (tempString += _lineName[1]+ _tempString2 + '：' + _value[1] + lineShow_para[1] + '<br />') : (tempString += '' );
                    } else if (boolean1 == false) {
                        _isFordernumShow ? (tempString += _lineName[1]+ _tempString2 + '：' + _value[2] + lineShow_para[1] + '<br />') : (tempString += '' );
                    }
                    _isSordernumShow ? (tempString += _lineName[0]+ _tempString2 + '：' + _value[2] + lineShow_para[1] + '<br />') : (tempString += '' );
                }
            },
            padding: 0,
            axisPointer: {
                type: 'line'
            },
            position: function (point, params, dom) {
                var _float_layer2 = $('.float_layer>.float_layer2');
                var _float_layer = $('.float_layer');
                if (( $("#compare1 .compare_square").hasClass('active')) && ( $("#compare2 .compare_square").hasClass('active'))) {
                    _float_layer2.css('height', '100px');
                    _float_layer.css('height', '120px');
                } else if (( !$("#compare1 .compare_square").hasClass('active')) && ( !$("#compare2 .compare_square").hasClass('active'))) {
                    _float_layer2.css('height', '40px');
                    _float_layer.css('height', '40px');
                } else {
                    _float_layer2.css('height', '80px');
                    _float_layer.css('height', '80px');
                }
                _float_layer.parent().css("background", "inherit");
                _float_layer.css({
                    'min-width': '120px',
                    'display': 'inline-block',
                    '*display': 'inline',
                    '*zoom': '1',
                    'background': 'none',
                    'min-height': '60px',
                    'border-radius': '5px',
                    'overflow': 'hidden'
                });
                _float_layer2.css({
                    'min-width': '120px',
                    'display': 'inline-block',
                    '*display': 'inline',
                    '*zoom': '1',
                    'overflow': 'hidden',
                    'background-color': '#000',
                    'padding': '10px 10px 0px 15px',
                    'line-height': '30px',
                    'position': 'relative',
                    'border-radius': '5px',
                    'color': '#fff',
                    'opacity': '0.7'
                });
                var _top = null;
                var _floatlayerWidth = ($('.float_layer').width()) / 2;
                if (point[0] + _floatlayerWidth * 2 + 100 > document.body.clientWidth) {
                    _top = point[0] - _floatlayerWidth * 2;
                } else {
                    _top = point[0] - _floatlayerWidth;
                }

                return [_top, point[1] - 80];
            }
        },
        grid: {
            left: '5%',
            right: '4%',
            top: '10%',
            bottom: '7%',
            containLabel: true
        },

        xAxis: {
            type: 'category',
            nameLocation: 'end',
            nameGap: '10',
            nameTextStyle: {
                color: '#999999'
            },
            boundaryGap: false,
            data: lineArray[0],
            axisLine: {
                lineStyle: {
                    color: '#b3b3b3'
                }
            },
            axisLable: {
                textStyle: {
                    color: '#515974'
                }
            },
            splitLine: {
                show: false,
            },
            axisTick: {
                show: true,
                inside: true
            }

        },
        yAxis: {

            type: 'value',
            name: yAxisName,
            nameLocation: 'end',
            nameGap: '10',
            nameTextStyle: {
                color: '#999999'
            },
            minInterval: 1,
            min: 0,
            axisTick: 'show',
            axisLabel: {formatter: '{value} '},
            splitNumber: splitNumberLine,
            axisLine: {
                lineStyle: {
                    color: '#B3B3B3'
                }
            },
            splitLine: {
                show: true,
                lineStyle: {
                    color: '#f5f5f5'
                }
            }
        },
        series: [
            {
                name: lineShow_para[2][2],
                type: 'line',
                symbol: 'circle',
                symbolSize: 8,
                showSymbol: true,
                lineStyle: {
                    normal: {
                        width: 3,
                        color: _color[2]
                    }
                },
                data: lineArray[3],
                itemStyle: {
                    normal: {
                        color: _color[2]
                    },
                    emphasis: {
                        color: "#515974",
                        borderWidth: "2",
                        shadowColor: "#fff",
                        opacity: "1"
                    }
                }
            },//上周同期
            {
                name: lineShow_para[2][1],
                type: 'line',
                symbol: 'circle',
                symbolSize: 8,
                showSymbol: true,
                lineStyle: {
                    normal: {
                        width: 3,
                        color: _color[0]
                    }
                },
                data: lineArray[2],
                itemStyle: {
                    normal: {
                        color: _color[0]
                    },
                    emphasis: {
                        color: "#515974",
                        borderWidth: "2",
                        shadowColor: "#fff",
                        opacity: "1"
                    }
                }
            },//昨日
            {
                name: lineShow_para[2][0],
                type: 'line',
                symbol: 'circle',
                symbolSize: 8,
                lineStyle: {
                    normal: {
                        type: 'dotted',
                        color: _color[1],
                        width: 3
                    }
                },
                data: lineArray[4],
                itemStyle: {
                    normal: {
                        color: _color[1]
                    },
                    emphasis: {
                        color: "#515974",
                        borderWidth: "2",
                        shadowColor: "#fff",
                        opacity: "1"
                    }
                }
            },//点
            {
                name: lineShow_para[2][0],
                type: 'line',
                symbol: 'circle',
                symbolSize: 8,
                showSymbol: true,
                lineStyle: {
                    normal: {
                        width: 3,
                        color: _color[1]
                    }
                },
                data: lineArray[1],
                itemStyle: {
                    normal: {
                        color: _color[1]
                    },
                    emphasis: {
                        color: "#515974",
                        borderWidth: "2",
                        shadowColor: "#fff",
                        opacity: "1"
                    }
                }
            }//今日
        ]
    };

    containerCharts.setOption(containerOption);
    /*此部分为新增订单趋势echarts图----结束*/
    resize_window(containerCharts);
}
/**
 * dataParameter.unit参数的默认值
 * 当点击时dataParameter.unit参数的默认值设置,对比框及提示线默认不显示，
 */
function unitDefault() {
    $('.orderTrend_right .compare #compare2,#compare1').removeClass('active');
    $('.orderTrend_right .compare .compare_square').removeClass('active')
    $('.orderTrend_right .line .line_center ').css('display', 'none');
    $('.orderTrend_right .line .line_left ').css('display', 'none');
    $('.orderTrend_div .orderTrend_right>.time>.type1').addClass('active')
        .siblings('div')
        .removeClass('active');
    //dataParameter.unit参数的默认值
    var _paraCondition = conditionJudgeArr(dataParameter);
    if (_paraCondition[0] || _paraCondition[1]) {
        dataParameter.unit = "h";
    } else if (_paraCondition[2] || _paraCondition[3]) {
        dataParameter.unit = "d";
    } else if (_paraCondition[4]) {
        dataParameter.unit = "w";
    } else if (_paraCondition[5]) {
        dataParameter.unit = "m";
    }
}
/**
 * 折线图左上角和右上角显示默认设置
 * 对右上角时、天、周、月及可选择对比进行页面展示
 * 自定义时间的时天周显示
 * @return [_analysisText, _analysisUnit, floatLayerText]['类型如订单','单位如单','浮层显示文字如前一周']
 */
function linefaceShow(dataParameter) {
    //确保非自定义时间时'确定'按钮隐藏
    if (dataParameter.searchDate != 6) {
        $('.time_tab .back .make_sure').css('display', 'none');
    }
    var _analysisText, _analysisUnit;
    switch (dataParameter.analysisType) {
        case 1:
            _analysisText = '订单';
            _analysisUnit = '单';
            break;
        case 2:
            _analysisText = '收客人';
            _analysisUnit = '人';
            break;
        case 3:
            _analysisText = '订单金额';
            _analysisUnit = '元';
            break;
    }
    //定义变量判断浮层上每一层都会显示什么字
    var floatLayerText = [];
    //对右上角时、天、周、月及可选择对比进行页面展示
    switch (dataParameter.searchDate) {
        case 1:
            compareShowText('前一日', '上周同期', '前一日订单', '今日订单', '上周同期订单', true, false);
            floatLayerText = ['今日', '昨日同期', '上周同期'];
            // $('.orderTrend_right .time .type1').text('时').css('display', 'inline-block')
            //     .siblings().css('display', 'none')
            break;
        case 2:
            compareShowText('前一周', null, '前一周订单', '本周订单', null, true, false);
            $('#compare2').css('display', 'none');
            floatLayerText = ['本周', '上周同期'];
            break;
        case 3:
            compareShowText('前一月', '去年同期', '前一月订单', '本月订单', '去年同期订单', true, false);
            floatLayerText = ['本月', '上月同期', '去年同期'];
            break;
        case 4:
            compareShowText('前一年', null, '前一年订单', '本年订单', null, true, false);
            $('#compare2').css('display', 'none');
            floatLayerText = ['本年', '去年同期'];
            break;
        case 5:
            floatLayerText[0] = (dataParameter.analysisType == 1 ? '订单' : (dataParameter.analysisType == 2 ? '人数' : '金额'));
            compareShowText(null, null, null, '全部' + floatLayerText[0], null, false, true, floatLayerText);
            break;
        default:
            floatLayerText[0] = (dataParameter.analysisType == 1 ? '订单' : (dataParameter.analysisType == 2 ? '人数' : '金额'));
            compareShowText(null, null, null, floatLayerText[0] + "趋势", null, null, true, floatLayerText);
    }
    // 自定义时间的时天周显示
    var _paraCondition = conditionJudgeArr(dataParameter);
    if (_paraCondition[0]) {
        isHDWMYShow('时')
    }
    if (_paraCondition[1]) {
        isHDWMYShow('时', '天');
    }
    if (_paraCondition[2]) {
        isHDWMYShow('天');
    }
    if (_paraCondition[3]) {
        isHDWMYShow('天', '周');
    }
    if (_paraCondition[4]) {
        isHDWMYShow('周', '月');
    }
    if (_paraCondition[5]) {
        isHDWMYShow('月', '年');
    }
    return [_analysisText, _analysisUnit, floatLayerText];
}
/**
 * 对于时天周月年显示的提取函数
 * @param text1 .type1显示的text文字
 * @param text2 .type2显示的text文字
 * @author liuzhaoli
 * @createDate  2017/1/18
 */
function isHDWMYShow(text1, text2) {
    $('.orderTrend_right .time .type1').text(text1).css('display', 'inline-block');
    if (text2) {
        $('.orderTrend_right .time .type2').text(text2).css('display', 'inline-block');
    } else {
        $('.orderTrend_right .time .type2').css('display', 'none');
    }
}
/**
 * 提取共同代码成函数
 * @param text1 .type1显示的text文字
 * @param text2 .type2显示的text文字
 * @param text3 .type2显示的text文字
 * @param text4 .type2显示的text文字
 * @param text5 .type2显示的text文字
 * @param boolean1 boolean1判断两个对比框是否显示 true为显示false为不显示若为null则为什么都不做
 * @param boolean2 boolean2判断 部分floatLayerText的情况显示 true为显示false为不显示
 * @author liuzhaoli
 * @createDate  2017/1/18
 */
function compareShowText(text1, text2, text3, text4, text5, boolean1, boolean2, floatLayerText) {
    if (boolean1 == true) {
        $('.orderTrend_right .compare').css('display', 'block');
        $('#compare1,#compare2').css('display', 'inline-block');
    } else if (boolean1 == false) {
        $('.orderTrend_right .compare').css('display', 'none');
    } else if (boolean1 == null) {
    }
    $('#compare1 .compare1').text(text1);
    $('#compare2 .compare2').text(text2);
    $('.line .compare_line1').text(text3);
    $('.line .compare_line2').text(text4);
    $('.line .compare_line3').text(text5);
    if (boolean2) {
        floatLayerText[0] = (dataParameter.analysisType == 1 ? '订单' : (dataParameter.analysisType == 2 ? '人数' : '金额'));
        floatLayerText[1] = '';
        floatLayerText[2] = '';
    }
}
/**
 * 折线图图例三条线的颜色设置提取共同代码
 * @param text1 .type1显示对比小方框中对号的颜色图片位置
 * @param text2 .type2显示今日折线图例
 * @param text3 .type2显示前一日折线图例
 * @param text4 .type2显示上周折线图例
 * @author liuzhaoli
 * @createDate  2017/1/19
 */
function lineColorComm(_text1, _text2, _text3, _text4) {
    $('.orderTrend_right .compare .active').css('backgroundPosition', _text1);
    $('.orderTrend_right .line .line2 ').css('backgroundPosition', _text2);
    $('.orderTrend_right .line .line1 ').css('backgroundPosition', _text3);
    $('.orderTrend_right .line .line3 ').css('backgroundPosition', _text4);
}
/**
 * 折线图时与天点击时时间维度参数设置提取公用代码
 * @param text1 'h'   unit中的第一个参数天时中的时
 * @param text2 'd'   unit中的第二个参数天时中的天
 * @param self 因为函数提取，因策$(this)指向改成了window，此处予以定义为self
 * @author liuzhaoli
 * @createDate  2017/1/19
 */
function lineUnitComm(text1, text2, self) {
    if (self.index() == 0) {
        dataParameter.unit = text1;
    } else if (self.index() == 1) {
        dataParameter.unit = text2;
    }
}
/**
 * 折线图中浮层显示几行的判断函数
 * @param array为传入的数组
 * @author liuzhaoli
 * @createDate  2017/1/19
 */
function lineFloatlayerJudge(array) {
    var _tf;
    array.filter(function (x) {
        if (x != '' || x) {
            _tf = true;
        } else {
            _tf = false;
        }
    });
    return _tf;
}
/**
 * 判断是否为IE浏览器
 * @returns {boolean}
 */
function isIE() { //ie?
    if (!!window.ActiveXObject || "ActiveXObject" in window)
        return 0;
    else
        return 10;
}
/**
 * 对searchDate类型的几种情况作出判断函数
 * @param obj为传入的参数对象
 * @author liuzhaoli
 * @createDate  2017/2/15
 */
function conditionJudgeArr(obj){
    if(obj.serverTime){
        var serverTime = obj.serverTime;
        var today = setServerTimeDate(serverTime);
        serverTime = serverTime.replace(' ','T');
        var date_year_num = (Date.parse(serverTime) - Date.parse("Jan 1," + today.getFullYear())) / 86400000;//本年共包含多少天，
    }
    var totalDate_gap = obj.totalDate_gap;
    var conditionJudgeH = (obj.searchDate == 1 || (obj.searchDate == 2 && today.getDay() == 1) || (obj.searchDate == 3 && today.getDate() == 1) || (obj.searchDate == 4&&date_year_num == 1)||(obj.searchDate == 5&&totalDate_gap <= 1) || obj.unitDays == 1);
    var conditionJudgeHD =((obj.unitDays >= 2 && obj.unitDays <= 8) || (obj.searchDate == 2 && today.getDay() > 1) || (obj.searchDate == 3 && (today.getDate() >= 2 && today.getDate() <= 8)) || (obj.searchDate == 4&&date_year_num >= 2 && date_year_num <= 8) || (obj.searchDate == 5&&totalDate_gap>=2&&totalDate_gap<=8));
    var conditionJudgeD = ((obj.unitDays >= 9 && obj.unitDays <= 14) || (obj.searchDate == 3 && (today.getDate() >= 9 && today.getDate() <= 14)) || (obj.searchDate == 4 && (date_year_num >= 9 && date_year_num <= 14)) || (obj.searchDate == 5&&totalDate_gap>=9&&totalDate_gap<=14));
    var conditionJudgeDW =((obj.searchDate == 3 && (today.getDate() >= 15 && today.getDate() <= 31)) || (obj.unitDays >= 15 && obj.unitDays <= 58) || (obj.searchDate == 4 && (date_year_num >= 15 && date_year_num <= 58)) || (obj.searchDate == 5&&totalDate_gap>=15&&totalDate_gap<=58));
    var conditionJudgeWM = ((obj.unitDays >= 59 && obj.unitDays <= 365) || (obj.searchDate == 4&&date_year_num >= 59&&date_year_num <=365) || ( obj.searchDate == 5 && totalDate_gap>=59&&totalDate_gap<=365));
    var conditionJudgeMY = (obj.unitDays > 365 || (obj.searchDate == 5 && totalDate_gap>365));
    return [conditionJudgeH,conditionJudgeHD,conditionJudgeD,conditionJudgeDW,conditionJudgeWM,conditionJudgeMY];//将时天周月年所有的条件判断放到该数组中
}

/**
 * 对searchDate类型的几种情况作出判断函数
 * @param serverTime为传入的获取到的系统时间
 * @author liuzhaoli
 * @createDate  2017/2/15
 */
function setServerTimeDate(serverTime){
        var serverTimeArray = serverTime.split(' ');
        var serverTimeArray0 = serverTimeArray[0].split('-');
        var serverTimeArray1 = serverTimeArray[1].split(':');
        var today = new Date(serverTimeArray0[0],serverTimeArray0[1]-1,serverTimeArray0[2],serverTimeArray1[0],serverTimeArray1[1],serverTimeArray1[2]);
    return today;
}
