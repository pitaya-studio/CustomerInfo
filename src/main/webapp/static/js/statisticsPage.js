/**
 * Created by changying.huo on 2017/3/7.
 */
var ctx = $("#ctx").val();
/**
 * 传输数据
 * @type {{searchDate: string, startDate: string, endDate: string, month: string, year: string}}
 */
var dataUse = {
    analysisType: 2,//1:订单数 2：收客人数 3：订单金额
    overView: 'dd',//dd：订单总览  xd：询单总览
    searchDate: '3', //时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部
    startDate: '', //自定义开始时间
    endDate: '', //自定义结束时间
    month: '',//月 month
    year: '',//年 year
    url: {
        left: ctx + '/order/analysis/getOrderCountAndRate',
        right: ctx + '/order/analysis/getOrderAnalysisDatas',
        proUrl: ctx + '/statistic/orderAnalysis/list'
    },
    color:''
};
/**
 * 刷新保留选项
 */
window.onload = function () {
    endwidth();
    var _dataUseOld, _Condition_div;
    //刷新之后
    if (location.href.endsWith('#save')) {
        _dataUseOld = dataUse;
        _Condition_div = $(".Condition_div");
        dataUse = JSON.parse(sessionStorage.getItem('dataSave'));
        if (dataUse) {
            dataUse.searchDate == '' ? chooseUse('custom') : chooseUse(dataUse.searchDate);
            dataUse.overView == 'dd' ? _Condition_div.show() : _Condition_div.hide();
            $(".Group_tab li[value=" + dataUse.overView + "]").addClass("active").siblings().removeClass("active");
            $(".Condition_tab li[value=" + dataUse.analysisType + "]").addClass("active").siblings().removeClass("active");
            if (dataUse.searchDate != "") {
                $(".time_tab li[data-searchDate=" + dataUse.searchDate + "]").addClass("active").siblings().removeClass("active");
            } else {
                $(".time_tab li").removeClass("active");
                $("#orderTimeBegin").val(dataUse.startDate);
                $("#orderTimeEnd").val(dataUse.endDate);
                $(".make_sure").show();
            }
        }
        else {
            dataUse = _dataUseOld;
            chooseUse('3');
        }
    } else {
        //新打开网页
        sessionStorage.clear();
        location.href = location.href + '#save';
        chooseUse('3');
    }
};
/**
 * endsWidth兼容函数
 */
function endwidth() {
    if (typeof String.prototype.endsWith != 'function') {
        String.prototype.endsWith = function (suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
        };
    }
};
/**
 * 订单询单切换事件
 */
$(".Group_tab li").click(function () {
    var _value = $(".Condition_tab .active").attr("value");
    $(this).addClass('active').siblings().removeClass('active');
    if ($(this).attr("id") == "inquiry_use") {
        $(".Condition_div").hide();
        dataUse.analysisType = 4;
        dataUse.url.left = ctx + '/ask/orderNum/getAskOrderCountAndRate';
        dataUse.url.right = ctx + '/ask/orderNum/getAskOrderNumPicture';
        dataUse.overView = 'xd';//询单
        dataUse.url.proUrl = ctx + '/statistic/enquiryAnalysis/list';
    } else {
        dataUse.analysisType = _value;
        dataUse.overView = 'dd';//订单
        $(".Condition_div").show();
        dataUse.url.left = ctx + '/order/analysis/getOrderCountAndRate';
        dataUse.url.right = ctx + '/order/analysis/getOrderAnalysisDatas';
        dataUse.url.proUrl = ctx + '/statistic/orderAnalysis/list';
    }
    var _objUse = $(".time_tab .active").attr('data-searchdate') || "custom";
    chooseUse(_objUse);
    var _dataUse = JSON.stringify(dataUse);
    sessionStorage.setItem("dataSave", _dataUse);
});
/**
 * 订单金额 收客人数 订单数 切换事件
 */
$(".Condition_tab li").click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    dataUse.analysisType = $(this).attr("value");
    var _objUse = $(".time_tab .active").attr('data-searchdate') || "custom";
    chooseUse(_objUse);
    var _dataUse = JSON.stringify(dataUse);
    sessionStorage.setItem("dataSave", _dataUse);
});
/**
 *时间切换点击事件
 */
$(".time_tab li:not(:last)").click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    $(".make_sure").hide();
    $(".dateinput").val("");
    dataUse.searchDate = $(this).attr("data-searchDate");
    dataUse.startDate = "";
    dataUse.endDate = "";
    chooseUse($(this).attr('data-searchdate'));
    var _dataUse = JSON.stringify(dataUse);
    sessionStorage.setItem("dataSave", _dataUse);
});
/**
 * 自定义按钮点击事件
 */
$(".dateinput").click(function () {
    $(".make_sure").show();
});
/**
 * 自定义确定按钮点击事件
 */
$(".make_sure").click(function () {
    var input = $(".dateinput");
    if (input.eq(0).val() == "" && input.eq(1).val() == "") {
        $.jBox.tip('请填写日期', 'warnning');
    } else {
        dataUse.searchDate = "";
        dataUse.month = "month";
        dataUse.startDate = input.eq(0).val();
        dataUse.endDate = input.eq(1).val();
        $(this).parent().siblings().removeClass("active");
        chooseUse("custom");
        var _dataUse = JSON.stringify(dataUse);
        sessionStorage.setItem("dataSave", _dataUse);
    }
});
/**
 * 类型颜色等样式判断
 * @param obj 时间周期字段使用
 */
function chooseUse(obj) {
    var _color1, _color2, _color3, _taggle_use, unit, _text1, _text2;
    _taggle_use = $('.taggle_use');
    unit = $('.unit');
    switch (dataUse.analysisType) {
        case 1:
            _text1 = '订单数';
            _text2 = '单';
            _color1 = '#53a4ff';
            _color2 = '#4491e7';
            _color3 = ['#114987', '#1964b9', '#2176d5', '#2484ed', '#5099ea', '#81b8f5'];
            break;
        case 2:
            _text1 = '收客人数';
            _text2 = '人';
            _color1 = '#39f5e5';
            _color2 = '#1edccc';
            _color3 = ['#12867c', '#17a79b', '#1bc7b8', '#1edccc', '#52e7db', '#a5ede7'];
            break;
        case 3:
            _text1 = '订单金额';
            _text2 = '元';
            _color1 = '#ffab47';
            _color2 = '#f08200';
            _color3 = ['#874f11', '#aa6415', '#c97619', '#df821b', '#eaa150', '#eebb82'];
            break;
        default:
            //询单操作
            _text1 = '询单数';
            _text2 = '单';
            _color1 = '#53a4ff';
            _color2 = '#4491e7';
            _color3 = ['#114987', '#1964b9', '#2176d5', '#2484ed', '#5099ea', '#81b8f5'];
    }
    dataUse.color=_color2;
    var _dataUse = JSON.stringify(dataUse);
    sessionStorage.setItem("dataSave", _dataUse);
    lineAjax(obj, _text1, _text2, _color2);
    increaseAjax(_text2, _color2, obj);
    _taggle_use.text(_text1);
    unit.text(_text2);
    same_use(dataUse, _color1, _color2, _color3, _text1, _text2);
}
/**
 * 防抖动
 * @param method 函数名
 * @param context this
 * @param time 防抖动时间
 * @param args 传入的参数
 */

function throttle(method, context, time, args) {
    clearTimeout(method.tId);
    method.tId = setTimeout(function () {
        method.apply(context, args);
    }, time);
}
/**
 * 增长率模块数字跳动
 * @param count      数据number数
 * @param time       总跳动时间
 * @param jump_time  跳动间隔
 * @param decimalPoint 保留小数位数
 * @param text 单位
 */
function count_use(count, time, jump_time, decimalPoint, text) {
    numberLength(fmoney(count, decimalPoint));
    var div = document.getElementById('count');
    var num = 0;
    var num_use;
    var _new_num;
    var _new_count;
    if (time / count <= 10) {
        num_use = count / time * jump_time;
    } else {
        num_use = 1;
    }
    var interval = setInterval(function () {
        num = Number((num + num_use).toFixed(2));
        if (num <= count) {
            _new_num = fmoney(num, decimalPoint);
            div.innerHTML = _new_num + '<span class="font_18">' + text + '</span>';
        } else {
            _new_count = fmoney(count, decimalPoint);
            div.innerHTML = _new_count + '<span class="font_18">' + text + '</span>';
        }
        num >= count ? clearInterval(interval) : null;
    }, jump_time)
}

/**
 * 增长率模块数字长度控制
 * @param num      数据number数
 */
function numberLength(num) {
    var _div = $(".count_big");
    var _length = (num + '').length;
    if (_length <= 7) {
        _div.css("font-size", "80px");
    } else if (7 < _length && _length <= 12) {
        _div.css("font-size", "50px");
    } else if (12 < _length && _length <= 24) {
        _div.css("font-size", "30px");
    } else {
        _div.css("font-size", "16px");
    }
}

/**
 * 增长率模块ajax
 * @param color 不同维度的颜色
 * @param objTime 时间维度判断字段
 * @param text 单位字段
 */
function increaseAjax(text, color, objTime) {
    var _data_use = JSON.stringify(dataUse);
    var _loading_img = $(".loading_img");
    var _loading_data = _loading_img.next();
    $.ajax({
        type: "POST",
        url: dataUse.url.left,
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            param: _data_use
        },
        beforeSend: function () {
            _loading_img.show();
            _loading_data.hide();
            $("#count").text("");
        },
        success: function (data) {
            _loading_data.show();
            var _inceaseRate = $(".incease_rate");
            var _inceaseUse = $(".inceaseUse");
            var _inceaseDiv = $(".color_use");
            var _border_triangle = $(".border_triangle");
            var _hover_box = $(".hover_box");
            var _incease = data.incrementRate;
            var _point, _text;
            _inceaseDiv.css("color", color);
            $(".colorRefresh").css("color", color);
            $(".header_use").css("background", color);
            dataUse.analysisType == 3 ? _point = 2 : _point = 0;
            throttle(count_use, window, 400, [data.newNum, 300, 50, _point, text]);
            //判断增长率显示条件和颜色变化
            if (data.incrementRate == "-1") {
                _inceaseRate.css("visibility", "hidden");
            } else {
                _inceaseRate.css("visibility", "visible");
                if (data.incrementRate.substring(0, 1) == "-") {
                    _inceaseUse.css("color", "#0ba100");
                    _inceaseUse.next().attr("class", "DA_img down");
                } else if (data.incrementRate.substring(0, 1) == "0") {
                    _inceaseUse.next().attr("class", "");
                    _incease = "未变化"
                } else {
                    _inceaseUse.next().attr("class", "DA_img up");
                    _inceaseUse.css("color", "#e70e0e");
                }
                _inceaseUse.text(_incease);
            }
            //数据更新时间
            if (objTime == "custom" || objTime == "5") {
                if (data.startDateSpec == "") {
                    $(".timeUse").text('');
                    _border_triangle.removeAttr("style");
                    _hover_box.removeAttr("style");
                } else {
                    $(".timeUse").text(data.startDateSpec + "至" + data.endDateSpec);
                    _border_triangle.css("right", "16px");
                    _hover_box.css("left", "-200px");
                }
            } else {
                _border_triangle.removeAttr("style");
                _hover_box.removeAttr("style");
                _text = $(".time_tab .active").text();
                $(".timeUse").text(_text);
            }
        },
        error: function () {
            alert("没有接收到数据")
        }
    })
}

/**
 * 折线图 年月维度切换
 */
$(".right_header span").click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    if ($(this).text() == "年") {
        dataUse.year = "year";
        dataUse.month = "";
    }
    else {
        dataUse.year = "";
        dataUse.month = "month";
    }
    var _objUse = $(".time_tab .active").attr('data-searchdate') || "custom";
    chooseUse(_objUse);
    var _dataUse = JSON.stringify(dataUse);
    sessionStorage.setItem("dataSave", _dataUse);
});
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
 * 显示数据格式化
 * @param s 数字
 * @param n 保留的小数位数
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
 * 折线图请求
 * @param objTime 点击的按钮
 * @param text1 维度字符
 * @param text2 单位
 * @param color 颜色
 */
function lineAjax(objTime, text1, text2, color) {
    var _data_use = JSON.stringify(dataUse);
    var _div = $(".right_header");
    var _divLeft = $(".right_header .left");
    var _divRight = $(".right_header .right");
    var _divActive = _divRight.children(".active");
    $.ajax({
        type: "POST",
        url: dataUse.url.right,
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            param: _data_use
        },
        beforeSend: function () {
            $("#lineCharts").html("加载中...");
            _div.removeClass('visible');
        },
        success: function (data) {
            // start 增长率模块的数据在这里传过来直接用了
            $("#refreshTime").text(data.today);
            $(".analysisType").text(text1);
            // end
            var _monthUse=$("#month");
            if (data.askOrderDateList.length == 0) {
                $("#lineCharts").html("暂无数据...");
            } else {
                _divLeft.text(text1 + "（" + text2 + "）");
                if(dataUse.year == "year") {
                    _monthUse.prev().addClass("active").siblings().removeClass("active") ;
                    _monthUse.prev()[0].style.background=dataUse.color;
                    _monthUse.prev()[0].style.color="#fff";
                    _monthUse[0].style.background="#fff";
                    _monthUse[0].style.color="#333";
                }else{
                    _monthUse.addClass("active").siblings().removeClass("active") ;
                    _monthUse[0].style.background=dataUse.color;
                    _monthUse[0].style.color="#fff";
                    _monthUse.prev()[0].style.background="#fff";
                    _monthUse.prev()[0].style.color="#333";
                }
                _div.addClass('visible');
                var _askOrderNumList = data.askOrderNumList;
                var _askOrderDateList = data.askOrderDateList;
                var _dottedDate = [];
                var _month = Number(data.today.substring(5, 7));
                var _day = Number(data.today.substring(8, 10));
                var _hour = Number(data.today.substring(11, 13));
                var _orderTimeEnd=$('#orderTimeEnd');
                var _dottedUse;
                switch (objTime) {
                    case '1':
                        _askOrderNumList.length = _hour + 1;
                        break;
                    case '3':
                        _askOrderNumList.length = _day;
                        break;
                    case '4':
                        _askOrderNumList.length = _month;
                        break;
                    case '5':
                        (_askOrderDateList[0]+'').indexOf(':') != -1?_askOrderNumList.length = _hour + 1:null;
                        break;
                    case 'custom':
                        if((_askOrderDateList[0]+'').indexOf(':') != -1 && (data.today.substring(0,10)==_orderTimeEnd.val()||_orderTimeEnd.val()=='')){
                            _askOrderNumList.length = _hour + 1;
                            _dottedUse=true;
                        }
                        break;
                    default:
                }
                var _index = _askOrderNumList.length - 1;
                var _data_use = data.today.substring(0, _askOrderDateList[_index].length);
                //自定义是否包含虚线boolean值
                var _customUse = (objTime == "custom" && (_data_use == _askOrderDateList[_index]||_dottedUse));
                //其他按钮是否包含虚线boolean值
                var _otherUse = (objTime == '5' || objTime == '1' || objTime == '3' || objTime == '4');
                if ((_customUse || _otherUse) && _index >= 1) {
                    _dottedDate[_index - 1] = _askOrderNumList[_index - 1];
                    _dottedDate[_index] = _askOrderNumList[_index];
                    _askOrderNumList.length = _index;
                }
                var _time,_dayUse;
                if(objTime == "custom"){
                    _time=$("#orderTimeBegin").val().substring(5, 10);
                }else if(objTime == "1"||objTime == "5"){
                    _time=data.today.substring(5, 10);
                }else {
                    _day>10?_dayUse=_day-1:_dayUse="0"+(_day-1);
                    _time=data.today.substring(5, 7)+'-'+_dayUse;
                }
                lineCharts(_askOrderNumList, _askOrderDateList, _dottedDate, color, text2,_time);
                data.days === true ? _divRight.show() : _divRight.hide();
            }
        },
        error: function (data) {
            if(data.responseText=="暂无数据"){
                $("#lineCharts").html("暂无数据...");
            }else{
                $("#lineCharts").html("请求失败,请刷新重试");
            }
            _div.removeClass('visible');
        }
    })
}

/**
 * 折线图
 * @param askOrderNumList 实线数据（数组）
 * @param askOrderDateList X轴数据（数组）
 * @param dottedDate 虚线数据（数组）
 * @param color 切换所需颜色
 * @param text 切换所需单位
 * @param time tooptip所需时间
 */
function lineCharts(askOrderNumList, askOrderDateList, dottedDate, color, text,time) {
    var lineCharts = echarts.init(document.getElementById("lineCharts"));
    var option = {
        tooltip: {
            formatter: function (val) {
                var _point;
                dataUse.analysisType == 3 ? _point = 2 : _point = 0;
                var _valueOne = fmoney(val[1].value, _point);
                var _valueTwo = fmoney(val[0].value, _point);
                var _val, _divHead, _divBody, _divHour;
                var _divNaN = '暂无数据';
                if (val[1].value !== "" && typeof(val[1].value ) != "undefined") {
                    _val = val[1];
                    _divBody = _valueOne;
                } else if (typeof(val[1].value ) == "undefined" && typeof(val[0].value ) == "undefined") {
                    _divHead = "";
                    _divBody = _divNaN;
                    return _divHead + _divBody;
                } else {
                    _val = val[0];
                    _divBody = _valueTwo;
                }
                _divHour = time+" "+_val.name + "-" + _val.name.replace(/:00/g, ":59") + "：";
                _val.name.indexOf(':') != -1 ? _divHead = _divHour : _divHead = _val.name + "：";
                return _divHead + _divBody + text;
            },
            padding: [5, 20],
            backgroundColor: 'rgba(0,0,0,0.8)',
            show: true,
            trigger: 'axis',
            confine: true,
            position: function (point, params, dom) {
                var width = point[0] - dom.offsetWidth / 2;
                var height = point[1] - dom.offsetHeight - 20;
                return [width, height];
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: askOrderDateList,
            axisLine: {
                lineStyle: {
                    color: '#b3b3b3'
                }
            },
            axisTick: {
                show: true,
                inside: true
            }
        },
        grid: {
            top: '5%',
            left: '30px',
            right: '35px',
            bottom: '0%',
            containLabel: true
        },
        yAxis: {
            type: 'value',
            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
            splitLine: {
                lineStyle: {
                    color: '#f5f5f5'
                }
            },
            minInterval: 1,
            min: 0,
            axisLabel: {
                textStyle: {
                    color: '#b3b3b3'
                },
                formatter: function (val) {
                    var _point,_val;
                    dataUse.analysisType == 3 ? _point = 2 : _point = 0;
                    _val=fmoney(val, _point);
                    return _val
                }
            }
        },
        series: [
            {
                type: 'line',
                symbol: 'circle',
                symbolSize: 8,
                lineStyle: {
                    normal: {
                        type: 'dotted',
                        color: color,
                        width: 3
                    }
                },
                data: dottedDate,
                itemStyle: {
                    normal: {
                        color: color
                    },
                    emphasis: {
                        color: "#515974"
                    }
                }
            },
            {
                type: 'line',
                symbol: 'circle',
                symbolSize: 8,
                showSymbol: true,
                lineStyle: {
                    normal: {
                        width: 3,
                        color: color
                    }
                },
                // data: askOrderNumList,
                data: askOrderNumList,
                itemStyle: {
                    normal: {
                        color: color
                    },
                    emphasis: {
                        color: "#515974"
                    }
                }
            }
        ]
    };
    var _max_value1 = Math.max.apply(null, askOrderNumList);
    var _max_value2 = Math.max(dottedDate[dottedDate.length - 1], dottedDate[dottedDate.length - 2]);
    var _max_value = _max_value1 < _max_value2 ? _max_value2 : _max_value1;
    if (_max_value == 1 || _max_value == 0) {
        option.yAxis.splitNumber = 1;
    } else if (_max_value == 2) {
        option.yAxis.splitNumber = 2;
    } else {
        option.yAxis.splitNumber = 3;
    }
    lineCharts.setOption(option);
    resize_window(lineCharts);
}

/**
 * 更改颜色单位的样式的公共方法
 * @param ctx 路径
 * @param _data 数据
 * @param color1 图表颜色
 * @param color2 图表颜色
 * @param color3 图表颜色（数组）
 * @param text1  数据类型：人数、金额等
 * @param text2 单位：单、人、元
 * @author ruiqi.zhang
 */
function same_use(_data, color1, color2, color3, text1, text2) {
    var _color_use = $('.color_blue');
    var _chart_footer = $('.chart_footer');
    getChannelData(_data, color3, text1, text2);
    getSellerData(_data, color1, color2, text1, text2);
    getProductData(_data, color2, text2)
    _color_use.css('color', color2);
    _chart_footer.css('color', color2);
}

/**
 * @param dataObj 请求参数对象集合
 * @param color1 图表颜色
 * @param text1  数据类型：人数、金额等
 * @param text2 单位：单、人、元
 * @author ruiqi.zhang
 */
function getChannelData(dataObj, color1, text1, text2) {
    var _dataObj = JSON.stringify(dataObj);
    $.ajax({
        type: "POST",
        url: ctx + '/enquiry/agent/statistic/agentPercentChart',
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            param: _dataObj
        },
        beforeSend: function () {
            var pieChart = $(".pieChart_body ");
            pieChart.text('加载中...');
        },
        success: function (data) {
            var _body = $(".pieChart_body ");
            var _data = data.list;
            var _tempArray;
            var _length = _data.length;
            //var _summary = 0;
            if (_data && _data.length > 0) {
                _body.html('');
                //截取除了其它的数据项
                _tempArray = _data.slice(0, _length - 1);
                //将其它数据项推入数组
                _tempArray.push(_data[_length-1]);
                /*_tempArray = _data.sort(function (a, b) {
                    return b.value - a.value
                });*/
                pieChart(_tempArray, color1, text1, text2);
                //判断渠道数据为0时显示为空**************************以后可能会用到
                /* for (var i= 0,len=_data.length;i <len;i++){
                 _summary += _data[i].value;
                 }
                 if(_summary > 0){
                 //some code here
                 }*/
            } else {
                _body.addClass('noData_01_chart').removeAttr('style').html('');
            }
        },
        error: function () {
            $(".pieChart_body ").text('请求失败');
        }
    })
}
/**
 * 销售ajax
 * @param dataObj 请求参数对象集合
 * @param color1 图表颜色
 * @param color2 图表颜色
 * @param text1  数据类型：人数、金额等
 * @param text2 单位：单、人、元
 * @author ruiqi.zhang
 */
function getSellerData(dataObj, color1, color2, text1, text2) {
    $.ajax({
        type: "POST",
        url: ctx + '/statisticSale/getSaleTop',
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            overView: dataObj.overView,
            searchDate: dataObj.searchDate,
            startDate: dataObj.startDate,
            endDate: dataObj.endDate,
            analysisType: dataObj.analysisType
        },
        beforeSend: function () {
            var barChart = $(".barChart_body ");
            barChart.text('加载中...');
        },
        success: function (data) {
            var _body = $(".barChart_body ");
            var _data = data.saleData;
            var other_data = data.otherData;
            var sellerArr = [];
            var dataNum = [];
            _body.html('');
            if (_data && _data.length > 0 && data.saleSum > 0) {
                var _sum = 0;
                for (var i = 0, _length = _data.length; i < _length; i++) {
                    var sellerName = _data[i].saleName;
                    var _num = _data[i].num;
                    _sum += _num;
                    sellerArr.unshift(sellerName);
                    dataNum.unshift(_num);
                }
                if (_sum > 0) {
                    //调用条形图函数
                    barChart(sellerArr, dataNum, color1, color2, text1, text2);
                    //当销售人数小于5人时,隐藏其他销售字段
                    if (data.saleSum < 6) {
                        _body.next().children().hide();
                    } else {
                        _body.next().children().show();
                        var other_formated = formatData(text2, other_data)
                        //其他销售数据项赋值
                        $('#bar_other').text(other_formated)
                    }
                } else {
                    _body.addClass('noData_02').removeAttr('style').html('');
                    _body.next().children().hide();
                }
            } else {
                _body.addClass('noData_02').removeAttr('style').html('');
                _body.next().children().hide();
            }
        },
        error: function () {
            $(".barChart_body ").text('请求失败');
            $(".barChart_body ").next().html('');
        }
    })
}

/**
 * 页面底部产品订单
 * @param dataObj 数据
 * @param color2 颜色
 * @param text2 单位：单、人、元
 * @author ruiqi.zhang
 */
function getProductData(dataObj, color2, text2) {
    $.ajax({
        type: "POST",
        url: dataUse.url.proUrl,
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            overView: dataObj.overView,
            searchDate: dataObj.searchDate,
            startDate: dataObj.startDate,
            endDate: dataObj.endDate,
            analysisType: dataObj.analysisType

        },
        beforeSend: function () {
            var orderList_body = $(".orderList_body");
            orderList_body.text('加载中...');
        },
        success: function (data) {
            // console.log(data)
            var _border;
            var otherData;
            var orderList_body = $(".orderList_body");
            orderList_body.html('');
            orderList_body.removeClass('noData_03')
            var _html = '';
            var sum = 0;
            if (data && data.length > 0) {
                //只遍历除了前length-1项，最后一项为“其它”
                for (var i = 0; i < data.length - 1; i++) {
                    _html += '<div class="orderList_use">';
                    _html += '<div class="order_left" title="' + data[i].productName + '">' + data[i].productName + '</div>';
                    _html += '<div class="order_right" style ="color:' + color2 + '"><span class="font_14">' + formatData(text2, data[i].orderNum) + '</span>&nbsp<span class="unit">' + text2 + '</span></div></div>';
                    //返回的产品订单数据项总和
                    sum += data[i].orderNum;
                }
                ;
                //判断产品维度数据项总和为0时，显示"无数据"
                if (sum > 0) {
                    orderList_body.append(_html);
                    _border = $('.orderList_use');
                    var _i = data.length - 1;
                    otherData = data[_i].orderNum;
                    //产品条数小于5条时，隐藏“其它”数据展示
                    if (_i < 5) {
                        $(".orderList_body").next().children().hide();
                    } else {
                        $(".orderList_body").next().children().show();
                    }

                    _border.css('border-left', '3px solid ' + color2);
                    var other_formated = formatData(text2, otherData)
                    //其他销售数据项赋值
                    $('#orderList_other').text(other_formated)

                } else {
                    orderList_body.addClass('noData_03').removeAttr('style');
                    orderList_body.next().children().hide();
                }

            } else {
                // orderList_body.text('当前暂无数据');
                orderList_body.addClass('noData_03').removeAttr('style');
                orderList_body.next().children().hide();
            }
        },
        error: function () {
            var orderList_body = $(".orderList_body");
            orderList_body.text('请求失败');
            $(".orderList_body").next().html('');
        }
    })
}

/**
 * 处理渠道名称过长函数
 * @param val 渠道名称
 * @author ruiqi.zhang
 */
function editLength(val){
    var _length = val.length;
    var subLength = Math.floor(_length/2);
    var text1;
    var text2;
    if(_length > 20){
        text1 = val.substring(0,subLength);
        text2 = val.substring(subLength);
        return text1 + "<br/>" +text2;
    }else{
        return val;
    }
}

/**
 * 页面底部渠道订单
 * @param channelData 渠道数据
 * @param color 图标颜色
 * @param text1 数据类型：人数、金额等
 * @param text2 单位：单、人、元
 * @author ruiqi.zhang
 */
function pieChart(channelData, color, text1, text2) {
    var pieChart = echarts.init(document.getElementById('pieChart'));
    pieChart.setOption({
        tooltip: {
            trigger: 'item',
            formatter: function (val) {
                var _value;
                if (text2 == '元') {
                    _value = fmoney(val.value, 2);
                    return editLength(val.name) + "<br/>" + text1 + "：" + _value + " " + text2;
                } else {
                    _value = fmoney(val.value, 0);
                    return editLength(val.name) + "<br/>" + text1 + "：" + _value + " " + text2;
                }
            },//单位参数
            padding: [5, 10],
            confine: true
        },
        color: color,
        series: [
            {
                name: '订单金额',
                type: 'pie',
                radius: '60%',
                center: ['50%', '50%'],
                data: channelData,
                roseType: false,
                label: {
                    position: 'top',
                    normal: {
                        textStyle: {
                            color: 'rgba(0, 0, 0, 0.8)'
                        },
                        formatter: function (val) {
                            //return val.name.replace(/(.{5})/g,'$1\n'); // 让series 中的文字进行换行
                            if (val.name.length > 6) {
                                var _val = val.name.substring(0, 6) + "...";  // 让series 中的文字超出5个显示...
                            }
                            return _val;
                        }
                    }
                },
                labelLine: {
                    normal: {
                        length: 10,
                        length2: 20
                    }
                },
                itemStyle: {
                    normal: {
                        shadowBlur: isIE(),
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    });
    resize_window(pieChart);
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
 * 处理返回的销售名称长度
 */
function splitName(array){
        var _arr = [];
        for(var i=0;i < array.length; i++){
            var _val;
                if (array[i].length > 6) {
                _val = array[i].substring(0, 6) + "...";  // 让series 中的文字超出5个显示...
            }else{
                    _val = array[i];
                }
            _arr.push(_val)
        }
        return _arr;
    }
/**
 * 页面底部销售订单
 * @param sellerArr 前五名销售的数组集合
 * @param dataNumList 条形图数据
 * @param color1 图标颜色
 * @param color2 图标颜色
 * @param text1  数据类型：人数、金额等
 * @param text2 单位：单、人、元
 * @author ruiqi.zhang
 */
function barChart(sellerArr, dataNumList, color1, color2, text1, text2) {
    var barChart = echarts.init(document.getElementById('barChart'));
    var option = {
        //color: ['#3398DB'],
        tooltip: {
            trigger: 'axis',
            formatter: function (val) {
                var _value;
                if (text2 == '元') {
                    _value = fmoney(val[0].value, 2);
                    return editLength(val[0].name) + "<br/>" + text1 + "：" + _value + " " + text2;
                } else {
                    _value = fmoney(val[0].value, 0);
                    return editLength(val[0].name) + "<br/>" + text1 + "：" + _value + " " + text2;
                }
            },
            axisPointer: {            // 坐标轴指示器,坐标轴触发有效
                type: false        // 默认为直线,可选为：'line' | 'shadow'
            },
            padding: [5, 10],
            confine: true
        },
        grid: {
            left: '6%',
            right: '14%',
            top: '10%',
            bottom: '9%',
            containLabel: true
        },
        yAxis: {
            type: 'category',
            data: sellerArr,
            axisLabel: {
                inside: false,
                textStyle: {
                    color: '#999'
                },
                formatter: function (val) {
                    //return val.name.replace(/(.{5})/g,'$1\n'); // 让series 中的文字进行换行
                    if (val.length > 6) {
                        var _val = val.substring(0, 6) + "...";  // 让series 中的文字超出5个显示...
                        return _val;
                    }else{
                        return val;
                    }

                }
            },
            axisTick: {
                show: false
            },
            axisLine: {
                show: true,
                lineStyle: {
                    color: '#999'
                }
            }
        },
        xAxis: {
            name: text2,
            type: 'value',
            axisLine: {
                show: true,
                lineStyle: {
                    color: '#999'
                }
            },
            axisTick: {
                show: false
            },
            axisLabel: {
                textStyle: {
                    color: '#999'
                },
                interval: 1,
                formatter: function (val) {
                    var _point,_val;
                    dataUse.analysisType == 3 ? _point = 2 : _point = 0;
                    _val = fmoney(val, _point);
                    return _val
                }
            },
            splitLine: {
                show: false
            },
            splitNumber: 2,
            minInterval: 0

        },
        series: [
            {
                name: '订单数',
                type: 'bar',
                barWidth: '60%',
                itemStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(
                            0, 0, 1, 0,
                            [
                                {offset: 0, color: color1},//颜色参数
                                {offset: 1, color: color2}
                            ]
                        )
                    },
                    emphasis: {
                        color: color2
                    }
                },
                //条形图数据
                data: dataNumList
            }
        ]
    };
    var _max_value = Math.max.apply(null, dataNumList); 
    if (_max_value == 1 || _max_value == 0) {
        option.xAxis.splitNumber = 1;
    } else if (_max_value == 2) {
        option.xAxis.splitNumber = 2;
    } else {
        option.xAxis.splitNumber = 3;
    }
    barChart.setOption(option);
    resize_window(barChart);
}

/**
 * 其他销售/产品数据的格式化
 * @author ruiqi.zhang
 */
function formatData(unitText, num) {
    if (unitText === '元') {
        return fmoney(num, 2);
    } else {
        return fmoney(num, 0);
    }
}

/**
 * 跳转到详情页
 */

//渠道详情
$('.pull-right').eq(0).children('a').on('click', function () {
    window.open(ctx + "/ask/orderNum/toDetail?pageTab=1&&searchDate=" + dataUse.searchDate + "&&startDate=" + dataUse.startDate + "&&endDate=" + dataUse.endDate + "&&analysisType=" +dataUse.analysisType);
})
//销售详情
$('.pull-right').eq(1).children('a').on('click', function () {
    window.open(ctx + "/ask/orderNum/toDetail?pageTab=2&&searchDate=" + dataUse.searchDate + "&&startDate=" + dataUse.startDate + "&&endDate=" + dataUse.endDate + "&&analysisType=" +dataUse.analysisType);
})
//产品详情
$('.pull-right').eq(2).children('a').on('click', function () {
    window.open(ctx + "/ask/orderNum/toDetail?pageTab=3&&searchDate=" + dataUse.searchDate + "&&startDate=" + dataUse.startDate + "&&endDate=" + dataUse.endDate + "&&analysisType=" +dataUse.analysisType);
})