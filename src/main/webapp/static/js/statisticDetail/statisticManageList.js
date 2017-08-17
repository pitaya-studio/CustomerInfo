/**
 * Created by ymx on 2017/3/7.
 */


//分页数据
var   inputDetail={
    "pageNo":"",
    "pageSize":""
};
var pageTab;

/**
 * 刷新保留选项
 */
window.onload = function () {
    //刷新之后
    endwidth();
    if (location.href.endsWith('#save')) {

        $("#search_context").val(sessionStorage.getItem('searchValue'));
        $("#search_context").focus().blur();

        //判定是否存有排序值
        if (sessionStorage.getItem('searchDate')) {
            $(".tab_change .selected_span").removeClass("selected_span");
            $(".tab_change span[search-data=" + sessionStorage.getItem('searchDate') + "]").addClass("selected_span");
        }

        if (sessionStorage.getItem('startDate')||sessionStorage.getItem('endDate')) {
            $("#startDate").val(sessionStorage.getItem('startDate'));
            $("#endDate").val(sessionStorage.getItem('endDate'));
            showButton();
        }

        //判定是否存有排序值
        if(sessionStorage.getItem('orderBy')){
            $("thead tr .sel_sort").removeClass("sel_sort");
            $("thead tr [order-by="+sessionStorage.getItem('orderBy')+"]").addClass("sel_sort");
        }

        searchAllContent();
    } else {
        sessionStorage.clear();
        //新打开网页
        location.href = location.href + '#save';

        //获取从上个页面带过来的时间和日期
        var searchDatef = $("#searchDateF").val();
        $(".tab_change span").removeClass("selected_span");
        //如果选择时间的标签为空，且日期也是空，则选择全部的标签
        if(searchDatef == ""){
            $(".selected_span").removeClass("selected_span");
            if($("#startDateF").val() == "" && $("#endDateF").val() == ""){
                $(".tab_change span").eq(0).addClass("selected_span");
            }
        }else{
            $(".tab_change span[search-data="+searchDatef+"]").addClass("selected_span");
            sessionStorage.setItem("searchDate", $(".tab_change .selected_span").attr("search-data"));
        }

        // bug刷新页面，值保留问题 ymx 2017/3/27 Start
        var startDate = $("#startDateF").val();
        var endDate = $("#endDateF").val();
        $("#startDate").val(startDate);
        $("#endDate").val(endDate);
        sessionStorage.setItem("startDate", startDate);
        sessionStorage.setItem("endDate", endDate);
        // bug刷新页面，值保留问题 ymx 2017/3/27 End

        showButton();
        searchAllContent();
    }
};

/**
 * endsWidth兼容函数
 */
function endwidth() {
    if (typeof String.prototype.endsWith != 'function') {
        String.prototype.endsWith = function(suffix) {
            return this.indexOf(suffix, this.length - suffix.length) !== -1;
        };
    }
}

$(function () {

    //选择对应的详情页面
    pageTab = $("#pageTab").val()||1;
    if(pageTab==1){
        changeInside("渠道","名称");
    }else if(pageTab==2){
        changeInside("销售","姓名");
    }else if(pageTab==3){
        changeInside("产品","名称");
    }

    //查找时间的区间的格式切换
    $(".tab_change span:not(:last-child)").click(function(){
        $(this).siblings().removeClass("selected_span").end().addClass("selected_span");
        sessionStorage.setItem("searchDate",$(this).attr("search-data"));

        // bug刷新页面，值保留问题 ymx 2017/3/27 Start
        sessionStorage.setItem("startDate", "");
        sessionStorage.setItem("endDate", "");
        // bug刷新页面，值保留问题 ymx 2017/3/27 End

        clearDateSearch();
        //获取新数据
        searchAllContent();
    });

    // 选择对应的排序类型
    var analysisType = parseInt($("#analysisType").val())||1;// 分析类型首页传过来，1：订单数2：收客人数3：订单金额4：代表询单
    var analyType;
    //选择到对应类型的倒序位置
    switch (analysisType){
        case 1 : analyType = 1;break;
        case 2 : analyType = 5;break;
        case 3 : analyType = 3;break;
        case 4 : analyType = 7;break;
    }
    $(".table_third .sel_sort").removeClass("sel_sort");
    $(".table_third th i[order-by="+analyType+"]").addClass("sel_sort");

    //点击排序，切换并判定是哪一种类型的排序
    $(".sort_thead").click(function(){
        if($(this).find(".sel_sort").length>0){
            var $sibling_i = $(this).find(".sel_sort");
            $sibling_i.removeClass("sel_sort");
            $sibling_i.siblings().addClass("sel_sort");
        }else {
            $(this).parent().siblings().find(".sel_sort").removeClass("sel_sort");
            $(this).find(".fa-caret-down").addClass("sel_sort");
        }
        sessionStorage.setItem("orderBy",$(".sel_sort").attr("order-by"));
        //进行ajax获取数据
        searchAllContent();
    });

    //点击搜索框里面的文字，文字消失
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

//改变页面上内部内容
function changeInside(text,unit) {
    $(".bottom-first").html(text+"详情");
    $(".ipt-tips").html("请输入"+text+unit);
    $(".title_of_type").html(text+unit);
}

//点击搜索按钮
function searchInput() {
    var trimInput = $("#search_context").val().replace(/(^\s*)|(\s*$)/g, "");
    $("#search_context").val(trimInput);
    sessionStorage.setItem("searchValue",trimInput);
    searchAllContent();
}

//tab切换部分，日期显示的判定
function showButton(){
    $("#startDate").val()!=''||$("#endDate").val()!=''?$('#sureButton').show():$('#sureButton').hide();
}

//清空日期
function clearDateSearch() {
    $(".date-text .dateinput").val("");
    $("#sureButton").hide();
}

//自定义日期筛选
function customized(){
    $(".tab_change>span").removeClass("selected_span");
    sessionStorage.setItem('searchDate',"");
    sessionStorage.setItem("startDate",$("#startDate").val());
    sessionStorage.setItem("endDate",$("#endDate").val());
    searchAllContent();
}

//查找内容代码
function searchAllContent(pageno,pagesize){
    var ctx = $("#ctx").val();
    var url;
    pageno = pageno||1;
    // bug刷新页面，页码值保留问题 ymx 2017/3/27 Start
    var updatePage = sessionStorage.getItem("updatePage");
    pagesize = pagesize||updatePage||10;
    // bug刷新页面，页码值保留问题 ymx 2017/3/27 End

    //判定查找详情列表的url：1渠道、2销售、3产品
    if(pageTab==1){
        url = ctx+"/enquiry/agent/statistic/agentDetailList";
    }else if(pageTab==2){
        url = ctx+"/statisticSale/saleDetailList";
    }else if(pageTab==3){
        url = ctx+"/statisticOrder/productList/list";
    }

    var _data = returnData();

    if (_data.searchDate==null && _data.startDate=="" && _data.endDate == "") {
        _data.searchDate=5;
        $(".tab_change span").eq(0).addClass("selected_span");
    }

    var str = {pageTab:_data.pageTab,searchDate:_data.searchDate,orderType:2,
        startDate:_data.startDate,endDate:_data.endDate,searchValue:_data.searchValue,
        pageNo:pageno,orderBy:_data.orderBy,pageSize:pagesize};
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
                        "<td colspan='6'>暂无查询数据</td>" +
                        "</tr>";
                } else {
                    for (var i = 0, listLength = data.list.length; i < listLength; i++) {
                        switch(data.list[i].rankNum){
                            case 1:html += "<tr><td class='sort_logo'><span class='first_logo'></span>"+data.list[i].rankNum+"</td>"; break;
                            case 2:html += "<tr><td class='sort_logo'><span class='second_logo'></span>"+data.list[i].rankNum+"</td>"; break;
                            case 3:html += "<tr><td class='sort_logo'><span class='third_logo'></span>"+data.list[i].rankNum+"</td>"; break;
                            default:html += "<tr><td>"+data.list[i].rankNum+"</td>"; break;
                        }
                        html += "<td>"+data.list[i].analysisTypeName+"</td>" +
                            "<td class='text_right_side'>"+data.list[i].orderNum+"</td>" +
                            "<td class='text_right_side'>"+data.list[i].orderPersonNum+"</td>" +
                            "<td class='text_right_side'>"+data.list[i].orderMoney+"</td>" +
                            "<td class='text_right_side'>"+data.list[i].orderPreNum+"</td>" +
                            "</tr>";
                    }
                    $(".pagination").append(_pageHtml);
                    // bug刷新页面，值保留问题 ymx 2017/3/27 Start
                    $(".pagination .controls input").eq(1).attr("id","updatePage");
                    // bug刷新页面，值保留问题 ymx 2017/3/27 End
                }
                $(".table_third tbody").html(html);
            }
        },
        error: function (){
            alert("查找失败！");
        }
    });
}

//需要传给后台的数据（需要复用）
function returnData() {
    var searchValue = $("#search_context").val();

    //searchTab搜索框上面的切换标签: 1：今日-1：昨日3：本月 -3：上月4：本年-4：去年 5：全部
    var searchDate = $(".tab_change .selected_span").attr("search-data");

    //自定义区间范围
    var startDate = "";
    var endDate = "";
    if(searchDate==null){
        startDate = $("#startDate").val() || "";
        endDate = $("#endDate").val() || "";
    }

    //排序1：订单总数降序  2：订单总数升序  3：订单金额降序  4：订单金额升序  5：收客人数降序  6：收客人数升序  7：询单数降序  8：询单数升序
    //判定是哪一种类型的排序
    var orderNum = $(".table_third .sel_sort").attr("order-by");
    var orderBy = orderNum||"";

    var str = {pageTab:pageTab,searchDate:searchDate,orderType:2,
        startDate:startDate,endDate:endDate,searchValue:searchValue,
        orderBy:orderBy};

    return str;
}

//导出excel
function exportExcel() {

    var ctx = $("#ctx").val();
    var url;
    //判定导出Excel的url：1渠道、2销售、3产品
    if(pageTab==1){
        url = ctx+"/enquiry/agent/statistic/exportAgentDetailToExcel";
    }else if(pageTab==2){
        url = ctx+"/statisticSale/getSaleDetailListExcel";
    }else if(pageTab==3){
        url = ctx+"/statisticOrder/productList/exportExcel";
    }

    var pageno = parseInt($(".pagination li.active a").html())||"";
    var pagesize = parseInt($(".pagination li.controls").find("input:nth-child(2)").val())||"";

    var _data = returnData();

    //日期和选择的时间标签，全部为空的时候导出全部
    if (_data.searchDate==null && _data.startDate=="" && _data.endDate == "") {
        _data.searchDate=5;
        // $(".tab_change span").eq(0).addClass("selected_sp an");
    }

    var str = {pageTab:_data.pageTab,searchDate:_data.searchDate,orderType:2,
        startDate:_data.startDate,endDate:_data.endDate,searchValue:_data.searchValue,
        pageNo:pageno,orderBy:_data.orderBy,pageSize:pagesize};
    var jsonData = JSON.stringify(str);

    $("#excelparam").val(jsonData);
    $("#excelform").attr("action",url);
    $("#excelform").submit();
}

/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(page,pageSize){
    inputDetail.pageNo=page;
    inputDetail.pageSize=pageSize;

    //bug 17722 最大列表条数限制  ruiqi.zhang  2017-4-1 15:09:01
    pageSize > 500? pageSize = 500:null;
    // bug刷新页面，页码值保留问题 ymx 2017/3/27 Start
    sessionStorage.setItem("updatePage",pageSize);
    // bug刷新页面，页码值保留问题 ymx 2017/3/27 End
    searchAllContent(page,pageSize);
}