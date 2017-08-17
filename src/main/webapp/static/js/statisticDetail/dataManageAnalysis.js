var typeTab;
var analysisType;
//分页数据
var   inputDetail={
    "pageNo":"",
    "pageSize":""
};

$(function(){
    var ctx = $("#ctx").val();
    $.ajax({
        type: "POST",
        url: ctx + "/statisticHome/orderList",
        async: false,
        success: function (data){
            if(data) {
                var jsondata = JSON.parse(data);
                var optio = "<option value='0'>全部</option>";
                for (var i = 0; i < jsondata.length; i++) {
                    optio += "<option value=" + (jsondata[i].typeValue) + ">" + jsondata[i].typeName + "</option>"
                }
                $("#orderTypeId").html(optio);

                //产品类型
                var orderTypeF = $("#orderTypeF").val()||0;
                // 解决游轮标签跳转过来页面不可用问题 changying.huo
                $("#orderTypeId option[value="+orderTypeF+"]").selected=true;

            }
        },
        error: function (){
            alert("失败");
        }
    });//a

    typeTab = $("#pageTab").val()||1;//b

    //选择时间
    var searchDatef = $("#searchDateF").val();
    if(searchDatef == ""){
        $(".selected_span").removeClass("selected_span");
        if($("#startDateF").val() == "" && $("#endDateF").val() == ""){
            $(".tab_change span").eq(4).addClass("selected_span");
        }
    }else{
        $(".tab_change span").eq(searchDatef-1).addClass("selected_span");
    }
    $("#startDate").val($("#startDateF").val());
    $("#endDate").val($("#endDateF").val());
    showButton();

    if($("#analysisTypeF").val()){
        analysisType = parseInt($("#analysisTypeF").val());
    }else{
        analysisType = 1;
    }
    $(".table_third .sel_sort").removeClass("sel_sort");
    $(".table_third th").eq(analysisType+1).find(".fa-caret-down").addClass("sel_sort");

    switchTab(typeTab);

    //搜索条件筛选
    launch();

    //查找时间的区间的格式切换
    $(".tab_change span:not(:last-child)").click(function(){
        $(this).siblings().removeClass("selected_span").end().addClass("selected_span");

        clearDateSearch();
        //获取新数据
        searchAndSort();
    });


    //点击排序，切换并判定是哪一种类型的排序
    $(".sort_thead").click(function(){
        if($(this).find(".sel_sort").length>0){
            var $sibling_i = $(this).find(".sel_sort");
            $sibling_i.removeClass("sel_sort");
            $sibling_i.siblings().addClass("sel_sort");
            searchAndSort();//倒序
        }else {
            $(this).parent().siblings().find(".sel_sort").removeClass("sel_sort");
            $(this).find(".fa-caret-down").addClass("sel_sort");
            //进行ajax获取数据
            searchAndSort();
        }
    });

    searchAndSort();

    $(".txtPro").focus(function () {
        $(this).next("span").hide();
    });

    $(".txtPro").blur(function () {
        if($(this).val()==""){
            $(this).next("span").show();
        }
    });

    $(".ipt-tips").click(function(){
        var obj_this = $(this);
        obj_this.prev("input").focus();
    });
});

//1:渠道分析;2:销售分析;3:产品分析;
function switchTab(typeTab){
    var titletype ="";
    $(".nav-tabs li").removeClass("active");
    if (typeTab==1){
        titletype ="渠道名称";
        $(".nav-tabs li").eq(0).addClass("active");
        $(".ipt-tips").html("输入渠道名称");
    }else if (typeTab==2){
        titletype ="销售名称";
        $(".nav-tabs li").eq(1).addClass("active");
        $(".ipt-tips").html("输入销售名称");
    }else if (typeTab==3){
        titletype ="产品名称";
        $(".nav-tabs li").eq(2).addClass("active");
        $(".ipt-tips").html("输入产品名称");
    }

    var $seach = $(".activitylist_bodyer_right_team_co");
    $seach.find("input[type=text]").val("");
    $(".title_of_type").html(titletype);
    searchAndSort();
}

//点击搜索按钮
function searchAllSearch() {
    searchAndSort();
}

//tab切换部分，日期显示的判定
function showButton(){
    $("#startDate").val()!=''||$("#endDate").val()!=''?$('#sureButton').show():$('#sureButton').hide();
}

//条件重置
function resetAllSearch(){
    var $seach = $(".activitylist_bodyer_right_team_co");
    $seach.find("input[type=text]").val("");
    $("#orderTypeId option")[0].selected=true;

    searchAndSort();
}

//选择订单数倒序
function orderSort() {
    //点击排序，切换并判定是哪一种类型的排序
    var stype = $(".table_third .sel_sort").parent().parent().index() - 1;
    var posi = $(".table_third .sel_sort").index();
    var orderNum = stype*2-posi;
    return orderNum;
}

//自定义
function customized(){
    $(".tab_change>span").removeClass("selected_span");
    searchAndSort();
}


function searchAndSort(pageno,pagesize){
    var titleType = $(".nav-tabs li[class*=active]").index()+1;
    var url = "";
    var ctx = $("#ctx").val();
    if(titleType==1){
        url = ctx+"/agent/statistic/agentStatisticList";
    }else if(titleType==2){
        url = ctx+"/statisticAnalysis/sale/saleList";
    }else if(titleType==3){
        url = ctx+"/statistic/productList/list";
    }

    //searchTab搜索框上面的切换标签: 1：今日；2：本周；3：本月；4：本年；5:全部
    var searchDate = $(".tab_change .selected_span").index()+1;
    //自定义区间范围
    var startDate = "";
    var endDate = "";
    if(searchDate<=0){
        startDate = $("#startDate").val() || "";
        endDate = $("#endDate").val() || "";
    }

    if (searchDate<=0 && startDate=="" && endDate == "") {
		$(".tab_change span").eq(4).addClass("selected_span");
    }

    //分析类型analysisType
    analysisType = analysisType||0;
    //搜索内容searchValue
    var searchValue = $(".txtPro").val();

    var orderType = $("#orderTypeId option:selected").val();//产品线
    var orderNumBegin = $("#orderNumStart").val();//订单数0-3
    var orderNumEnd = $("#orderNumEnd").val();
    var orderMoneyBegin = $("#orderMoneyStart").val();//订单数0-3
    var orderMoneyEnd = $("#orderMoneyEnd").val();//订单数0-3
    var orderPersonNumBegin = $("#orderPersonNumStart").val();//订单数0-3
    var orderPersonNumEnd = $("#orderPersonNumEnd").val();//订单数0-3
    pageno = pageno||1;
    pagesize = pagesize||10;
    var sort = orderSort()||"";
    var orderBy = sort;//1：订单数倒序 2：订单数正序 3：收客人数倒序 4：收客人数正序 5订单金额倒序 6：订单金额正序

    var str = {orderType:orderType,searchDate:searchDate,startDate:startDate,
        endDate:endDate,analysisType:analysisType,searchValue:searchValue,
        orderNumBegin:orderNumBegin,orderNumEnd:orderNumEnd,
        orderPersonNumBegin:orderPersonNumBegin,orderPersonNumEnd:orderPersonNumEnd,
        orderMoneyBegin:orderMoneyBegin,orderMoneyEnd:orderMoneyEnd,
        pageNo:pageno,orderBy:orderBy,pageSize:pagesize};
    var jsonData = JSON.stringify(str);

    $.ajax({
        type: "POST",
        url: url,
        cache: false,
        dataType:"json",//返回的数据类型
        data: {
            param:jsonData
        },
        success: function (data){
            if(data) {
                var count = data.count;

                var data_count = "<li>订单总数：<span class='red_count_style'>" + data.orderTotalNum + "单</span></li>" +
                    "<li>收客总人数：<span class='red_count_style'>" + fmoney(data.orderTotalPersonNum, 0) + "人</span></li> " +
                    "<li>订单总金额：<span class='red_count_style'>" + data.orderTotalMoney + "</span></li>";

                $(".activitylist_paixu_left ul").html(data_count);
                $(".activitylist_paixu_right strong").html(count);

                var html = "";

                var _webVersion = "t2";
                var _pageTest = {
                    count: count,
                    pageSize: pagesize,
                    pageNo: pageno
                };
                var _pageHtml = doPage(_pageTest, _webVersion, goPage);
                $(".pagination").empty();

                if (count == 0) {
                    html += "<tr>" +
                        "<td colspan='5'>暂无查询数据</td>" +
                        "</tr>";
                } else {
                    for (var i = 0, listLength = data.list.length; i < listLength; i++) {
                        var deName = data.list[i].agentName || data.list[i].saleName || data.list[i].productName;
                        html += "<tr>" +
                            "<td>" + (i + 1) + "</td>" +
                            "<td>" + deName + "</td>" +
                            "<td>" + data.list[i].orderNum + "</td>" +
                            "<td>" + data.list[i].orderPersonNum + "</td>" +
                            "<td class='text_right_side'>" + data.list[i].currencyMark + data.list[i].orderMoney + "</td>" +
                            "</tr>";
                    }
                    $(".pagination").append(_pageHtml);
                }

                $(".table_third tbody").html(html);
            }
        },
        error: function (){
            alert("查找失败！");
        }
    });

}

//数字验证1
function inputint(dom) {

    var val = $(dom).val().replace(/\D/g,"");
    $(dom).val(val);
}


//数字验证2
function inputNum(dom){
    var thisvalue = $(dom).val();
    var minusSign = false;
    if(thisvalue){
        if(/^\-/.test(thisvalue)){
            minusSign = true;
            thisvalue = thisvalue.substring(1);
        }
        thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
        var txt = thisvalue.split(".");
        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
        if(minusSign){
            thisvalue = "-" + thisvalue;
        }
        $(dom).val(thisvalue);
    }else{
        //$(dom).val(0);
    }
}

function clearDateSearch() {
    $(".date-text .dateinput").val("");
    $("#sureButton").hide();
}


function dependButton() {
    if($("#endDate").val() == "" && $("#endDate").val() == ""){
        $("#sureButton").hide();
    }else {
        $("#sureButton").show();
    }
}
/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(page,pageSize){
    inputDetail.pageNo=page;
    inputDetail.pageSize=pageSize;
    searchAndSort(page,pageSize);
}
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