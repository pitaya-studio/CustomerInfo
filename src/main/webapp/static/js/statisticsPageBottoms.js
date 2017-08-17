/**
 * Created by quauq on 2017/3/7.
 */
var ctx = $("#ctx").val();
//定义所需发送的参数
/**
 * 传输数据
 * @type {{searchDate: string, startDate: string, endDate: string, month: string, year: string}}
 */
var dataUse = {
    analysisType: 1,
    overView: 'dd',//dd：订单总览  xd：询单总览
    searchDate: '3', //时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部
    startDate: '', //自定义开始时间
    endDate: '', //自定义结束时间
    month: '',//月 month
    year: '',//年 year
};
/**
 * 订单询单切换事件
 */
$(".Group_tab li").click(function () {
    var _value=$(".Condition_tab .active").attr("value");
    $(this).addClass('active').siblings().removeClass('active');
    if ($(this).attr("id") == "inquiry_use") {
        $(".Condition_div").hide();
        dataUse.analysisType = 1;
        dataUse.overView = 'xd';//询单
        dataUse.url = ctx + '/ask/orderNum/getAskOrderNumPicture';
    } else {
        dataUse.analysisType = _value;
        dataUse.overView = 'dd';//订单
        $(".Condition_div").show();
        dataUse.url = ctx + '/order/analysis/getOrderAnalysisDatas';
    }
    getting_data(obj, dataUse);
});
/**
 * 订单金额 收客人数 订单数 切换事件
 */
$(".Condition_tab li").click(function () {
    $(this).addClass('active').siblings().removeClass('active');
    var _text = $(this).text();
    $(".analysisType").text(_text);
    dataUse.analysisType = $(this).attr("value");
    getting_data(obj, dataUse);
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
    // lineAjax(dataUse, $(this).attr('data-searchdate'));
    getting_data(obj, dataUse);
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
    if (input.eq(0).val() == "" || input.eq(1).val() == "") {
        alert("请正确填写开始日期");
    } else {
        dataUse.searchDate = "";
        dataUse.startDate = input.eq(0).val();
        dataUse.endDate = input.eq(1).val();
        // lineAjax(dataUse, "custom");
        $(this).parent().siblings().removeClass("active");
        getting_data(obj, dataUse);
    }
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
// 页面刷新时调用饼图和柱状图方法,后台接口完成后再做修改****
$(function () {
    getting_data(obj, dataUse)
});

/**
 * 数据调用起始函数
 * @param obj 时间周期字段使用
 * @param dataParameter 数据
 */
function getting_data(obj, dataParameter) {
    // var use_num;
    choose_use(dataParameter, obj);
}
/*function createPicUrl(param,param2,param3,url){

 }*/

/**
 * 类型颜色等样式判断
 * @param  _data 数据
 * @param  ctx   路径
 * @param  use_num   判断刷新范围
 * @param obj 时间周期字段使用
 */
function choose_use(_data, obj) {
    var _color1, _color2, _color3, _taggle_use, unit, _text1, _text2, _header_taggle;
    _taggle_use = $('.taggle_use');
    unit = $('.unit');
    switch (_data.analysisType) {
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
            _color3 = ['#12867c', '#17a79b', '#1bc7b8', '#1edccc', '#52e7db','#a5ede7'];
            break;
        case 3:
            _text1 = '订单金额';
            _text2 = '元';
            _color1 = '#ffab47';
            _color2 = '#f08200';
            _color3 = ['#874f11', '#aa6415', '#c97619', '#df821b', '#eaa150', '#eebb82'];
            break;
        default:
        //默认操作
    }
    _taggle_use.text(_text1);
    unit.text(_text2);
    same_use(_data, _color1, _color2, _color3, _text1, _text2);
    // if (_data.searchDate != '') {
    //     _header_taggle.text($(obj).text());
    // } else {
    //     _header_taggle.text('');
    // }
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
 * 渠道统计请求ajax
 */
function getChannelData(dataObj,color1,text1,text2){
    // console.log(dataObj)
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
            console.log(data)
            var _body = $(".pieChart_body ");
            var _data = data.list;
            var _tempArray;
            var _summary = 0;
            if(_data&&_data.length > 0){
                _body.html('');
                _tempArray = _data.sort(function (a, b) {
                    return a.value - b.value
                });
                pieChart(_tempArray,color1,text1,text2);
                console.log(_tempArray)
                //判断渠道数据为0时显示为空**************************
               /* for (var i= 0,len=_data.length;i <len;i++){
                    _summary += _data[i].value;
                }
                if(_summary > 0){
                    //some code here
                }*/
            }else{
                _body.addClass('noData_01_chart').removeAttr('style').html('');
            }
        },
        error: function(){
            $(".pieChart_body ").text('请求失败');
        }
    })
}
/**
 * 销售ajax
 */
function getSellerData(dataObj, color1, color2, text1, text2){
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
            console.log(data)
            var _body = $(".barChart_body ");
            var _data = data.saleData;
            var other_data = data.otherData;
            var sellerArr = [];
            var dataNum = [];
            _body.html('');
            if(_data && _data.length > 0 && data.saleSum > 0){
                var _sum = 0;
                for(var i=0,_length=_data.length; i<_length; i++){
                    var sellerName = _data[i].saleName;
                    var _num = _data[i].num;
                    _sum += _num;
                    sellerArr.unshift(sellerName);
                    dataNum.unshift(_num);
                }
                //调用条形图函数
                barChart(sellerArr,dataNum, color1, color2, text1, text2);
                if(_sum > 0){
                    //当销售人数小于5人时,隐藏其他销售字段
                    if(_data.length < 6){
                        _body.next().html('');
                    }else{
                        var other_formated = formatData(text1, other_data)
                        //其他销售数据项赋值
                        $('#bar_other').text(other_formated)
                    }
                }else{
                    _body.addClass('noData_01').removeAttr('style').html('');
                    _body.next().html('');
                }
            }else{
                _body.addClass('noData_01').removeAttr('style').html('');
                _body.next().html('');
            }
        },
        error: function(){
            $(".barChart_body ").text('请求失败');
            $(".barChart_body ").next().html('');
        }
    })
}
/**
 * 页面底部产品订单
 * @param ctx 路径
 * @param _data 数据
 * @param color2 颜色
 * @param _text 单位：单、人、元
 */
function getProductData(dataObj, color2, text2) {
    $.ajax({
        type: "POST",
        url: ctx + '/statistic/enquiryAnalysis/list',
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {
            overView: dataObj.overView,
            searchDate: dataObj.searchDate,
            startDate: dataObj.startDate,
            endDate: dataObj.endDate
        },
        beforeSend: function () {
            var orderList_body = $(".orderList_body");
            orderList_body.text('加载中...');
        },
        success: function (data) {
            console.log(data)
            var _border;
            var otherData;
            var orderList_body = $(".orderList_body");
            orderList_body.html('');
            orderList_body.removeClass('noData_01')
            var _html = '';
            var sum = 0;
            if (data && data.length > 0) {
                for (var i = 0; i < data.length-1; i++) {
                    if (data[i].productName) {
                        _html += '<div class="orderList_use">';
                        _html += '<div class="order_left" title="' + data[i].productName + '">' + data[i].productName + '</div>';
                        _html += '<div class="order_right"><span class="font_14">' + formatData(text2, data[i].orderNum) + '</span>&nbsp<span class="unit">' + text2 + '</span></div></div>';
                    }
                    sum += data[i].orderNum;
                };
                //判断产品维度数据项总和为0时，显示"无数据"
                if(sum > 0){
                    orderList_body.append(_html);
                    _border = $('.orderList_use');
                    var _i = data.length-1;
                    otherData = data[_i].orderNum;
                    _border.css('border-left', '3px solid' + color2);
                    var other_formated = formatData(text2, otherData)
                    //其他销售数据项赋值
                    $('#orderList_other').text(other_formated)

                }else{
                    orderList_body.addClass('noData_01').removeAttr('style');
                    orderList_body.next().html('');
                }

            } else {
                // orderList_body.text('当前暂无数据');
                orderList_body.addClass('noData_01').removeAttr('style');
                orderList_body.next().html('');
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
 * 页面底部渠道订单
 */
function pieChart(channelData,color,text1,text2) {
    var pieChart = echarts.init(document.getElementById('pieChart'));
    pieChart.setOption({
        tooltip : {
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
            },//单位参数
            padding: [5,10]
        },
        color: color,
        series: [
            {
                name:'订单金额',
                type:'pie',
                radius : '70%',
                center: ['50%', '50%'],
                data:channelData,
                roseType: false,
                label: {
                    position: 'top',
                    normal: {
                        textStyle: {
                            color: 'rgba(0, 0, 0, 0.8)'
                        },
                        formatter: function (val) {
                            // console.log(val)
                            //return val.name.replace(/(.{5})/g,'$1\n'); // 让series 中的文字进行换行
                            if (val.name.length > 6) {
                                var _val = val.name.substring(0, 5) + "...";  // 让series 中的文字超出5个显示...
                            }
                            return _val;
                        }
                    }
                },
                labelLine: {
                    normal: {
                        length: 10,
                        length2: 30
                    }
                },
                itemStyle: {
                    normal: {
                        shadowBlur: 2,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    })
    resize_window(pieChart);
}


/**
 * 页面底部销售订单
 * @param sellerArr 前五名销售组成的数组
 * @param dataNum 销售对应的数据
 */
function barChart(sellerArr,dataNumList,color1, color2, text1, text2) {
    var barChart = echarts.init(document.getElementById('barChart'));
    var option = {
        //color: ['#3398DB'],
        tooltip:{
            trigger: 'axis',
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
        yAxis: {
            type: 'category',
            data: sellerArr,
            axisLabel: {
                inside: false,
                textStyle: {
                    color: '#333'
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
                }
            },
            splitLine: {
                show: false
            },
            splitNumber: 2,
            min: 0

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
    }else if(_max_value == 2){
        option.xAxis.splitNumber = 2;
    } else {
        option.xAxis.splitNumber = 3;
    }
    barChart.setOption(option);
    resize_window(barChart);
}


//刷新加载假数据
/*$(function() {
    var orderList_body = $(".orderList_body");
    var data= [

        {
            'productName':'hahahahhahahahah什么东西啊这是键盘不好使啊擦擦擦擦擦',
            'num':5024
        },
        {
            'productName':'hahahahhahahahah什么东西啊这是键盘不好使啊擦擦擦擦擦',
            'num':54
        },
        {
            'productName':'hahahahhahahahah什么东西啊这是键盘不好使啊擦擦擦擦擦',
            'num':524
        },
        {
            'productName':'hahahahhahahahah什么东西啊这是键盘不好使啊擦擦擦擦擦',
            'num':524324
        },
        {
            'productName':'hahahahhahahahah什么东西啊这是键盘不好使啊擦擦擦擦擦',
            'num':4545645524
        }
    ];
    var _text = '单'
    var _html = '';
    if (data && data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            if (data[i].productName) {
                _html += '<div class="orderList_use">';
                _html += '<div class="order_left" title="' + data[i].productName + '">' + data[i].productName + '</div>';
                _html += '<div class="order_right"><span class="font_14">' + data[i].num + '</span>&nbsp<span class="unit">' + _text + '</span></div></div>';
            }
        }

        orderList_body.append(_html);
    }
})*/

/*页面刷新时ajax请求加载全部产品类型*/
/*
$(function () {
    $.ajax({
        type: "get",
        url: "${adminPath}/statistic/productList/list",
        async: true,
        success: function (data) {
            // alert('已经执行');
            console.log(data);
            /!*$('.main-right .Group_tab').empty();
             var htmlProductype = '';*!/

        }
    });
});*/
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
 * 其他销售/产品数据的格式化
 */
function formatData(unitText,num){
    if(unitText === '元'){
        return fmoney(num, 2);
    }else{
        return fmoney(num, 0);
    }
}