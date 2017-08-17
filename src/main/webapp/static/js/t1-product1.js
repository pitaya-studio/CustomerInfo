var screenSelected={//筛选条件对象
    keywordQ:"",//搜索关键字
    tourOutInQ:"",//标签页切换以及初始化，出境游或者国内游,
    travelAreaIdQ:"",//标签页切换以及初始化，东南亚,
    startCityParaQ:[],//出发城市
    endCityParaQ:[],//目的地
    targetCity:[],//抵达城市
    linePlay:[],//游玩线路
    supplierParaQ:[],//供应商
    groupDateParaQ:[],//出团日期开始日期
    dayParaQ:[],//行程天数开始天数
    priceParaQ:[],//价格
    freeParaQ:[],//开始余位
    pageNo:1,//当前页码（点击的页码）
    pageSize:10,//每页显示记录数
    orderBy:[]//排序
};
var pageTest={
    count:0,//数据总条数*
    pageSize:10,//每页条数*
    pageNo:1,//当前页码*
}
var ctx;
var t1img;
var ctxStatic;
var str;
var st;
/*
 * 鼠标hover时显示供应商筛选
 * */
var isSus = false;
var jumpType = 1;
var travelAreaId ="";
var travelAreaName = "";
var travelInOutId = 100000;
var travelInOutName = "出境游";
var tabText = "";
var version;
var param;
$(document).ready(function() {

    var productThead = '';
    var groupThead = '';

    productThead +='<tr style="background-color: #fffbf8">' +
        '<th width="80px" class="first_t">序号</th>' +
        '<th width="700px">产品信息</th>' +
        '<th width="250px">供应商信息</th>' +
        '<th width="220px" class=" last_t t_right">系统结算价</th>' +
        '</tr>';
    groupThead +='<tr style="background-color: #fffbf8">' +
        '<th width="35px" class="first_t">序号</th>' +
        '<th width="400px">产品信息</th>' +
        '<th width="90px">团号</th>' +
        '<th width="90px">出团日期</th>' +
        '<th width="140px">出发城市</th>' +
        '<th width="50px">余位</th>' +
        '<th width="80px" class="t_right">建议直客价</th>' +
        '<th width="125px" class=" last_t t_right">系统结算价</th>' +
        '</tr>';

    sessionStorage.setItem("productThead",productThead);
    sessionStorage.setItem("groupThead",groupThead);
    var nowSelect = $('.activeList').attr('id').slice(0,-3);
    sessionStorage.setItem("nowSelect",nowSelect);
    sessionStorage.setItem("defaultSelect",nowSelect);


    var doc=document,
    inputs=doc.getElementsByTagName('input'),
    supportPlaceholder='placeholder'in doc.createElement('input'),

    placeholder=function(input){
        var text=input.getAttribute('placeholder'),
            defaultValue=input.defaultValue;
        if(defaultValue==''){
            input.value=text
            $(input).css('color','#ccc')
        }
        input.onfocus=function(){
            if(input.value===text)
            {
                this.value=''

            }
            $(input).css('color','#333')
        };
        input.onblur=function(){
            if(input.value===''){
                this.value=text
                $(input).css('color','#ccc')
            }
        }
    };

    if(!supportPlaceholder){
        for(var i=0,len=inputs.length;i<len;i++){
            var input=inputs[i],
                text=input.getAttribute('placeholder');
            if(input.type==='text'&&text){
                placeholder(input)
            }
        }
    }
    param = JSON.parse(sessionStorage.param);
    version = checkIe();
    ctx = $("#ctx").val();
    ctxStatic = $("#ctxStatic").val();
    t1img = $("#t1img").val();
    //获取页面参数
    jumpType = param.jumpType;//从首页跳转的方式
    var pts = JSON.parse(param.travelSelected);
    if(pts.travelInOut.length>0){
        travelInOutId = pts.travelInOut[0].id;//出境游和国内游id（目前系统只有这两个标签）
        travelInOutName = pts.travelInOut[0].name;//出境游or国内游
        $("#travelAreaId").val(travelInOutId);
        $("#travelAreaId").val(travelInOutName);
    }
    if(pts.travelArea.length>0){
        travelAreaId = pts.travelArea[0].id;//产品所属区域id
        travelAreaName = pts.travelArea[0].name;//产品所属区域名称
        $("#travelAreaId").val(travelAreaId);
        $("#travelAreaName").val(travelAreaName);
    }
    tabText = param.tabText;//搜索框中的内容
    $("#tabText").val(tabText);
    //供应商数组
    var trals = [];
    if(pts.travelId.length>0){
        trals = pts.travelId;
    }
    /*初始刷筛选对象*/
    var str = travelInOutName + "/" + travelAreaName;
    screenSelected.tourOutInQ = travelInOutId;//标签页切换以及初始化，出境游或者国内游;
    if (jumpType == 1) {
        screenSelected.travelAreaIdQ = travelAreaId;
        $("#reginType").text(str);
    }else if(jumpType == 2 || jumpType == 3){
        screenSelected.orderBy=[];
        screenSelected.travelAreaIdQ = travelAreaId;//标签页切换以及初始化，东南亚;
        for (var k = 0; k < trals.length; k++) {
            var tid = $(trals[k]).attr("id");
            screenSelected.supplierParaQ.push(tid);
        }
        $("#reginType").text(str);
    }else if(jumpType == 4){
        $("#reginType").text("导航");
        $("#keyword").val(tabText);
        emptyObj();
        screenSelected.tourOutInQ = "";
        screenSelected.keywordQ = tabText;
    }
    // var tab = sessionStorage.getItem("nowSelect");
    getScreenData();//获取筛选条件
    getListData();//获取列表数据
    /*展开更多搜索条件*/
    $(".more-screen").live("click",function () {
        if ($(this).children().last().hasClass("fa fa-angle-double-down")) {//显示更多
            $(".search-container").children().removeClass('display-none');
            $(this).html('隐藏更多筛选条件<i class="fa fa-angle-double-up" aria-hidden="true"></i>');
            $(".search-container").children().eq(4).css('border-bottom', '1px dashed #d9d9d9');
        } else {
            var count = Number($(".search-container").children().length-1);
            for(var i=count ;i>4;i--){
                $(".search-container").children().eq(i).addClass('display-none');
            }
            $(this).html('显示更多筛选条件<i class="fa fa-angle-double-down" aria-hidden="true"></i>');
            $(".search-container").children().eq(4).css('border-bottom', 'none');
        }
    });
    //点击供应商更多展开图标,并把更多内容修改为收起
    $(".more_icon_show").live('click',function () {
        var $middle = $(this).parent().parent().find(".homepage_middle");
        // $middle.find("li").removeClass("travek_logo");
        if($(this).html()=="更多 <i class=\"fa fa-angle-down\"></i>"){
            $(this).html("收起 <i class=\"fa fa-angle-up\"></i>");
            $middle.css("overflow-y","auto");
            $middle.css("maxHeight","209px");
        }else{
            $(this).html("更多 <i class=\"fa fa-angle-down\"></i>");
            $middle.css("overflow-y","hidden");
            $middle.css("maxHeight","140px");
        }
    });
    //点击除供应商之外的筛选条件的更多按钮
    $(".more-item").live('click',function(){
        var className = $(this).attr('class');
        if (className.indexOf("multi-select") != -1) {
            return false;
        }
        if ($(this).children().last().hasClass("fa fa-angle-down")) {
            $(this).parent().parent().parent().css('height', 'auto');
            $(this).parent().parent().parent().parent().css('height', 'auto');
            $(this).parent().parent().parent().parent().children("dt").css('height', 'auto');
            $(this).html("<a href='#'>收起</a><i class='fa fa-angle-up' aria-hidden='true'></i>");
        } else {
            $(this).parent().parent().parent().css('height', '50px');
            $(this).parent().parent().parent().parent().css('height', '50px');
            $(this).parent().parent().parent().parent().children("dt").css('height', '50px');
            $(this).html("<a href='#'>更多</a><i class='fa fa-angle-down' aria-hidden='true'></i>");
        }
        //兼容ie
        if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
            var ddH = $(this).parent().parent().parent().height();
            $(this).parent().parent().parent().prev().height(ddH);
        }
    });
    //点击供应商多选展开logo和选择框
    $(".choose_more").live("click",function () {
        var $par = $(this).parent().parent();
        $par.addClass("select_model");
        if(isSus){
            $par.children(".homepage_list_bottom").show();
            $(this).hide();
        }
    });
    //点击除供应商之外的筛选条件的多选按钮
    $(".more-item.multi-select").live('click',function(){
        $(this).parent().parent().parent().css('height', 'auto');
        $(this).parent().parent().parent().parent().css('height', 'auto');
        $(this).parent().parent().parent().parent().children("dt").css('height', 'auto');
        $(this).next().hide();
        $(this).hide()
        $(this).parent().parent().parent().parent().addClass('sel_mul');
        $(this).parent().parent().parent().children("div").last().show();
        //兼容ie
        if ((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)) {
            var ddH = $(this).parent().parent().parent().height();
            $(this).parent().parent().parent().prev().height(ddH);
        }
        $(this).parent().parent().parent().parent().prev().css('border-bottom','none');
    });
    //供应商多选后的取消按钮点击事件收起图标和选择框
    $(".cancel").live("click",function () {
        if($(this).parent().attr('class').indexOf('homepage_list_bottom') == -1){
            return  false;
        }
        var $middle = $(this).parent().parent();
        $middle.removeClass("select_model");
        $middle.children(".homepage_right").children(".choose_more").show();
        $middle.find("li").removeClass("icon_choose");
        $(this).parent().children("span").first().addClass("unable");
        if(isSus){//浮窗上的多选
            $(this).parent().hide();
        }else{
            $middle.css("overflow-y", "hidden");
            $(".homepage_middle").scrollTop(0);
        }
    });
    //点击供应商之外的多选后的取消
    $(".cancel").live("click",function(){
        if($(this).parent().attr('class').indexOf('sure_btn') == -1){
            return  false;
        }
        $(this).parent().parent().parent().prev().css('border-bottom','1px dashed #ddd');
        $(this).parent().parent().parent().css('height', '50px');
        $(this).parent().parent().css('height', '50px');
        $(this).parent().parent().parent().children("dt").css('height', '50px');
        $(this).parent().hide();
        $(this).parent().parent().parent().removeClass('sel_mul');
        $(this).parent().parent().children("div").first().children("div").children(".more-item").show();
        var  moresel = $(this).parent().parent().children("div").first().children("div").children(".more-item").last();
        if($(moresel).attr("class").indexOf("multi-select") == -1){
            $(moresel).children("a").text("更多");
            $(moresel).children("i").attr("class","fa fa-angle-down");
        }
        $(this).parent().parent().children("div").first().children("div").children(".more-item").last().children("a").text("更多");
        $(this).parent().parent().children("div").first().children("div").children(".more-item").last().children("i").attr("class","fa fa-angle-down");
        $(this).parent().children(".ensure").addClass('unable');
        $(this).parent().parent().children("div").first().children("div").children("span").removeClass('selected');
        $(this).parent().parent().children("div").first().children("div").children("span").first().addClass('selected');
    });
    //点击供应商图标
    $(".homepage_middle a").live("click",function () {
        screenSelected.pageNo = 1;
        screenSelected.orderBy =[];
        var $judge = $(this).parent().parent().parent().parent();
        if ($judge.hasClass("select_model")) {//是否多选
            var $par_li = $(this).parent();
            if ($par_li.hasClass("icon_choose")) {
                $par_li.removeClass("icon_choose");
            } else {
                $par_li.addClass("icon_choose");
            }
            if ($(".icon_choose").length > 0) {
                $($judge).find(".ensure").removeClass("unable");
            } else {
                $($judge).find(".ensure").addClass("unable");
            }
        } else {
            if(isSus){
                $(this).parent().parent().parent().parent().parent().hide();
                isSus = false;
                emptyObj();
                screenSelected.keywordQ = "";
                $("#keyword").val("");
                $("#reginType").text(str);
                var io = $(".main-nav").children("li[class='select']").attr("id");
                var ti = $(".sub-nav").children("ul").children("li[class='select']").attr("id");
                screenSelected.tourOutInQ = io;
                screenSelected.travelAreaIdQ = ti;
            }else{
                $(this).parent().parent().parent().parent().remove();
            }
            var dataId = $(this).parent().attr('data-id');
            screenSelected.supplierParaQ.push(dataId);
            var tab = sessionStorage.getItem("defaultSelect");
            sessionStorage.setItem("nowSelect",tab);
            getScreenData();//获取筛选条件
            getListData();//获取列表数据
        }
    });
    /*点击除供应商之外的筛选条件*/
    $(".search-item .items-container span").live("click",function () {
            screenSelected.pageNo = 1;
            if($(this).parent().get(0).tagName.toLowerCase() == "dt"){//选择的条件
                 return  false;
            }
            if($(this).html()=="全部"&&$(this).hasClass("selected")){//全部按钮不可点击
                return  false;
            }
            /*输入框*/
            if($(this).parent().attr("class") == "date-text" ||$(this).attr("class") == "date-text"){
                return  false;
            }
            if ($(this).hasClass('clear-all')) {//与清空筛选条件重复
                return false;
            }
            var isMulti = $(this).parent().parent().parent().parent().hasClass('sel_mul');
            if (isMulti || isMulti == true) {
                if ($(this).hasClass("selected")) {
                    $(this).removeClass("selected");
                } else {
                    if ($(this).text() == "全部") {//全部与其他选项互斥
                        $(this).parent().find("span").removeClass("selected");
                        $(this).parent().find("span").find(".dateinput").val("");
                    }
                    $(this).parent().find("span").first().removeClass("selected");
                    $(this).addClass("selected");
                }
                var sl = $(this).parent().find("span[class='selected']").length;
                if (sl >= 1 && $(this).text() != "全部") {
                    $(this).parent().parent().parent().find(".sure_btn").find(".ensure").removeClass("unable");
                } else {
                    $(this).parent().parent().parent().find(".sure_btn").find(".ensure").addClass("unable");
                }
            } else {
                $(this).parent().parent().parent().parent().remove();
                var textId = $(this).parent().parent().parent().parent().children("dt").first().attr("id");//供应商id
                switch(textId){
                    case "fromAreaList":screenSelected.startCityParaQ.push($(this).attr("id"));break;//出发城市
                    case "targetCountry":screenSelected.endCityParaQ.push($(this).attr("id"));break;//目的地
                    case "targetCity":screenSelected.targetCity.push($(this).attr("id"));break;//抵达城市
                    case "linePlay":screenSelected.linePlay.push($(this).attr("id"));break;//游玩线路
                    case "travelDays":screenSelected.dayParaQ.push($(this).attr("id"));break;//行程天数
                    case "priceRange":screenSelected.priceParaQ.push($(this).attr("id"));break;//价格区间
                    case "remainingSeat":screenSelected.freeParaQ.push($(this).attr("id"));break;//余位
                    case "startDate":screenSelected.groupDateParaQ.push($(this).attr("id"));break;//出团日期
                }
                // var tab = sessionStorage.getItem("nowSelect");
                getScreenData();//获取筛选条件
                getListData();//获取列表数据////////////
            }
        });
    //供应商多选后的确定按钮点击事件
    $(".homepage_list_bottom  .ensure").live("click",function () {
        screenSelected.pageNo =1;
        if($(this).hasClass("unable")){
            return false;
        }
        screenSelected.orderBy =[];
        if(isSus){
            $(this).parent().parent().parent().hide();
            isSus = false;
            emptyObj();
            screenSelected.keywordQ = "";
            $("#keyword").val("");
            $("#reginType").text(str);
            var io = $(".main-nav").children("li[class='select']").attr("id");
            var ti = $(".sub-nav").children("ul").children("li[class='select']").attr("id");
            screenSelected.tourOutInQ = io;
            screenSelected.travelAreaIdQ = ti;
        }
        var liLists = $(this).parent().parent().children(".homepage_middle_pa").children("ul").find("li[class='travel_logo icon_choose']");
        for (var i = 0; i < liLists.length; i++) {
            screenSelected.supplierParaQ.push($(liLists[i]).attr("data-id"));
        }
        var tab = sessionStorage.getItem("nowSelect");
        getScreenData();//获取筛选条件
        getListData(defaultSelect);//获取列表数据
    });
    //点击供应商之外的多选后的确定
    $(".ensure").live("click",function () {
        screenSelected.pageNo =1;
        if($(this).hasClass("unable")){
            return false;
        }
        var textId = $(this).parent().parent().parent().children("dt").attr("id");//选择的筛选类型id（清除筛选条件时选用）
        var liLists = $(this).parent().parent().children("div").children("div").find("span[class='selected']");
        for (var i = 0; i < liLists.length; i++) {
            switch(textId){
                case "fromAreaList":screenSelected.startCityParaQ.push($(liLists[i]).attr("id"));break;//出发城市
                case "targetCountry":screenSelected.endCityParaQ.push($(liLists[i]).attr("id"));break;//目的地
                case "targetCity":screenSelected.targetCity.push($(liLists[i]).attr("id"));break;//抵达城市
                case "linePlay":screenSelected.linePlay.push($(liLists[i]).attr("id"));break;//游玩线路
                case "travelDays":screenSelected.dayParaQ.push($(liLists[i]).attr("id"));break;//行程天数
                case "priceRange":screenSelected.priceParaQ.push($(liLists[i]).attr("id"));break;//价格区间
                case "remainingSeat":screenSelected.freeParaQ.push($(liLists[i]).attr("id"));break;//余位
                case "startDate":screenSelected.groupDateParaQ.push($(liLists[i]).attr("id"));break;//出团日期
            }
        }
        // var tab = sessionStorage.getItem("nowSelect");
        getScreenData();//获取筛选条件
        getListData();//获取列表数据
    });
    //清空筛选条件
    $(".clear-all").live('click',function () {
        screenSelected.pageNo =1;
        emptyObj();
        // var tab = sessionStorage.getItem("nowSelect");
        getScreenData();//获取筛选条件
        getListData();//获取列表数据
    });
    /*
     *搜索框绑定回车事件
     * */
    $("#keyword").keydown(function(e){
        var curKey = e.which;
        if(curKey == 13){
            rakeList();
        }
    });
    /*
     * 鼠标hover时显示供应商筛选浮窗
     * */
    $(".top-tab").hover(function() {
        isSus = true;
        getArea("","",true,true);
        $(".top-suspend").show();
    },function(){
        isSus = false;
        $(".top-suspend").hide();
    });
    $(".top-suspend").hover(function(){
        $(this).show();
        isSus = true;
    },function(){
        $(this).hide();
        isSus = false;
    });
    /*
     * hover切换区域
     * */
    $("li[name='appHover']").live('hover',function(event){
        event = event || window.event;
        //切换选中样式
        if(event.type=='mouseover' || event.type == 'mouseenter'){
            var This = this;
            if($(This).hasClass("select")){
                return false;
            }else{
                $(This).parent().children("li").removeClass("select");
                $(This).addClass("select");
                st = setTimeout(function(){
                    //修改标签字段
                    var id = $(This).attr("id");
                    $("#travelAreaId").val(id);
                    var text = $(This).text();
                    $("#travelAreaName").val(text);
                    str = $("#travelInOutName").val() + "/" + text;
                    var io = $(".main-nav").children("li[class='select']").attr("id");
                    getApply(io,id,false,false);
                }, 500);
            }
        }else{
            clearTimeout(st);
        }
    });
    /*
     * 切换出境游和国内游
     * */
    $("li[name='inOut']").live('hover',function(event){
        event = event || window.event;
        if(event.type == "mouseover" ||event.type == "mouseenter") {
            var This = this;
            if ($(This).hasClass("select")) {
                return false;
            }
            //切换选中样式
            $(This).parent().children("li").removeClass("select");
            $(This).addClass("select");
            //修改标签字段
            st = setTimeout(function(){
                var id = $(This).attr("id");
                $("#travelInOutId").val(id);
                var text = $(This).text();
                $("#travelInOutName").val(text);
                str = text + "/" + $("#travelAreaName").val();
                screenSelected.tourOutInQ = id;
                getArea(id, "", true, false);
            }, 0);
        }else{
            clearTimeout(st);
        }
    });
});
//点击确定按钮
function  clcSure(obj){
    var str = "";
    var inputs = $(obj).parent().children("input");
    var start = $(inputs[0]).val();
    var end = $(inputs[1]).val();
    if(start =="" && end == ""){
        return false;
    }else{
        screenSelected.pageNo =1;
        var ids = $(obj).parent().parent().parent().parent().parent().children("dt").attr("id");
        str = start +"-"+end;
        switch(ids){
            case "fromAreaList":screenSelected.startCityParaQ=[];screenSelected.startCityParaQ.push(str);break;//出发城市
            case "targetCountry":screenSelected.endCityParaQ=[];screenSelected.endCityParaQ.push(str);break;//目的地
            case "targetCity":screenSelected.targetCity=[];screenSelected.targetCity.push(str);break;//抵达城市
            case "linePlay":screenSelected.linePlay=[];screenSelected.linePlay.push(str);break;//游玩线路
            case "travelDays":screenSelected.dayParaQ=[];screenSelected.dayParaQ.push(str);break;//行程天数
            case "priceRange":screenSelected.priceParaQ=[];screenSelected.priceParaQ.push(str);break;//价格区间
            case "remainingSeat":screenSelected.freeParaQ=[];screenSelected.freeParaQ.push(str);break;//余位
            case "startDate":screenSelected.groupDateParaQ=[];screenSelected.groupDateParaQ.push(str);break;//出团日期
            case  "supplierInfos":screenSelected.supplierParaQ=[];screenSelected.supplierParaQ.push(str);break;//供应商
        }
        // var tab = sessionStorage.getItem("nowSelect");
        getScreenData();//获取筛选条件
        getListData();//获取列表数据
    }
}
//向上翻页
function getUp(obj){
    var top = $(obj).parent().children("ul").scrollTop();
    $(obj).parent().children("ul").get(0).scrollTop = (top-324);
}
//向下翻页
function  getDown(obj){
    var top = $(obj).parent().children("ul").scrollTop();
    $(obj).parent().children("ul").get(0).scrollTop = (top+324);
}
/*
 * 点击区域搜索列表数据
 * */
function changeArea(obj){
    screenSelected.keywordQ = "";
    screenSelected.pageNo =1;
    $("#keyword").val("");
    $(".top-suspend").hide();
    //修改标签字段
    var id = $(obj).attr("id");
    $("#travelAreaId").val(id);
    var text = $(obj).text();
    $("#travelAreaName").val(text);
    var str = $(".main-nav").children("li[class='select']").text() + "/" + text;
    $("#reginType").text(str);
    emptyObj();//清空筛选条件
    var io = $(".main-nav").children("li[class='select']").attr("id");
    screenSelected.tourOutInQ = io;
    screenSelected.travelAreaIdQ = id;
     var tab = sessionStorage.getItem("defaultSelect");
    sessionStorage.setItem("nowSelect",tab);
    getScreenData();//获取筛选条件
    getListData();//获取列表数据
}
/*
 * 获取浮窗供应商
 * */
function getApply(tourOutIn,tourDistrictId,flag,isAppend){
    var params = {"tourOutIn": tourOutIn,"tourDistrictId": tourDistrictId,"flag":flag};
    $.ajax({
        type: "post",
        url: ctx+"/t1/getT1LogoList",
        data: params,
        async: true,
        dataType: 'json',
        beforeSend: function(){
            showWaiting("waitingImgSus");
        },
        complete: function () {
            hideWaiting("waitingImgSus");
        },
        success: function (data) {
            var data = data.results;
            var innerHtml = '';
            //查询结果
            var logoResultList = data.tourDistrict;
            var tourOutInList = data.tourOutInList;
            /*
             * 选中区域的供应商
             * */
            var appHtml = '<div class="homepage_middle_pa" ><ul class="homepage_middle  middle-top">';
            /*供应商区域*/
            for (var i = 0; i < logoResultList.length; i++) {
                if(logoResultList[i].tourDistrictId == tourDistrictId){
                    var appList = logoResultList[i].travelAgency;
                    for(var j=0;j<appList.length;j++){
                        appHtml += '<li class="travel_logo" data-id="'+appList[j].id+'"><a  title="'+appList[j].title+'">';
                        if(appList[j].path != null && appList[j].path != ""){
                            appHtml += '<img  src="'+t1img+'/'+appList[j].path+'" alt="">';
                        }
                        appHtml += '&nbsp;'+appList[j].title+'</a><span class="icon_chose_check"><i class="fa fa-check"></i></span></li>';
                    }
                }
            }
            appHtml += '</ul></div><div class="homepage_right"><a class="choose_more">+ 多选</a></div><div class="homepage_list_bottom"><span class="ensure unable">确定</span><span class="cancel">取消</span></div>';
            $(".top-suspend").children(".homepage_content_list.model-top").html(appHtml);
        }
    });
}
//获取浮窗上的供应商区域
function  getArea(tourOutIn,tourDistrictId,flag,isAppend){
    var params = {"tourOutIn": tourOutIn,"tourDistrictId": tourDistrictId,"flag":flag};
    $.ajax({
        type: "post",
        url: ctx+"/t1/getDistricts",
        data: params,
        async: true,
        dataType: 'json',
        success: function (data) {
            var data = data.results;
            var innerHtml = '';
            //查询结果
            var logoResultList = data.tourDistrict;
            var tourOutInList = data.tourOutInList;
            /*出境游和国内游*/
            innerHtml += '<ul  class="main-nav">';
            for (var i = 0; i < tourOutInList.length; i++) {
                if(tourOutIn == "" || tourOutIn == null){
                    if(i==0){//默认第一个选中
                        innerHtml += '<li  class="select" name="inOut"  id="'+tourOutInList[i].id+'">'+tourOutInList[i].name+'</li>';
                        tourOutIn = tourOutInList[i].id;
                        /*screenSelected.tourOutInQ = tourOutInList[i].id;*/
                    }else{
                        innerHtml += '<li  name="inOut"  id="'+tourOutInList[i].id+'">'+tourOutInList[i].name+'</li>';
                    }
                }else{
                    if(tourOutInList[i].id == tourOutIn){
                        innerHtml += '<li  class="select" name="inOut"  id="'+tourOutInList[i].id+'">'+tourOutInList[i].name+'</li>';
                    }else{
                        innerHtml += '<li  name="inOut"  id="'+tourOutInList[i].id+'">'+tourOutInList[i].name+'</li>';
                    }
                }
            }
            innerHtml += '</ul><div  class="sub-nav"><i  onclick="getUp(this);" class="fa fa-chevron-up" aria-hidden="true"></i><ul>';
            /*供应商区域*/
            for (var i = 0; i < logoResultList.length; i++) {
                if(tourDistrictId == "" || tourDistrictId == null){
                    if(i == 0){//默认选择第一个
                        innerHtml += '<li  name="appHover" class="select"  onclick="changeArea(this)"  id="'+logoResultList[i].tourDistrictId+'">'+logoResultList[i].tourDistrictName+'</li>';
                        tourDistrictId = logoResultList[i].tourDistrictId;
                    }else{
                        innerHtml += '<li  name="appHover" onclick="changeArea(this)"  id="'+logoResultList[i].tourDistrictId+'">'+logoResultList[i].tourDistrictName+'</li>';
                    }
                }else{
                    if(logoResultList[i].tourDistrictId == tourDistrictId){
                        innerHtml += '<li  class="select"　name="appHover" onclick="changeArea(this)"  id="'+logoResultList[i].tourDistrictId+'">'+logoResultList[i].tourDistrictName+'</li>';
                    }else{
                        innerHtml += '<li  name="appHover" onclick="changeArea(this)"  id="'+logoResultList[i].tourDistrictId+'">'+logoResultList[i].tourDistrictName+'</li>';
                    }
                }
            }
            innerHtml += '</ul><i onclick="getDown(this);" class="fa fa-chevron-down" aria-hidden="true"></i></div><div class="homepage_content_list  model-top">';
            $(".top-suspend").html('<img  class="waiting-img  sus" id="waitingImgSus" src="'+
                ctxStatic+'/images/cool-load.gif">');
            $(".top-suspend").append(innerHtml);
            getApply(tourOutIn,tourDistrictId,flag,isAppend);
        }
    });
}
/*
 * 获取供应商
 * */
function getSuspenData(tourOutIn,tourDistrictId,flag){
    var params = {"tourOutIn": tourOutIn,"tourDistrictId": tourDistrictId,"flag":flag};
    $.ajax({
        type: "post",
        url: ctx+"/t1/getT1LogoList",
        data: params,
        async: true,
        dataType: 'json',
        success: function (data) {
                var data = data.results;
                var innerHtml = '';
                //查询结果
                var logoResultList = data.tourDistrict;
                var tourOutInList = data.tourOutInList;
                /*出境游和国内游*/
                innerHtml += '<ul  class="main-nav">';
                for (var i = 0; i < tourOutInList.length; i++) {
                    innerHtml += '<li  name="inOut"  id="'+tourOutInList[i].id+'">'+tourOutInList[i].name+'</li>';
                }
                innerHtml += '</ul><div  class="sub-nav"><i  onclick="getUp(this);" class="fa fa-chevron-up" aria-hidden="true"></i><ul>';
                /*
                 * 选中区域的供应商
                 * */
                var appHtml = '<div class="homepage_content_list  model-top"><div class="homepage_middle_pa"><ul class="homepage_middle  middle-top">';
                /*供应商区域*/
                for (var i = 0; i < logoResultList.length; i++) {
                    if(logoResultList[i].tourDistrictId == $("#travelAreaId").val()){
                        innerHtml += '<li  name="appHover" onclick="changeArea(this)"  id="'+logoResultList[i].tourDistrictId+'">'+logoResultList[i].tourDistrictName+'</li>';
                        var appList = logoResultList[i].travelAgency;
                        for(var j=0;j<appList.length;j++){
                            appHtml += '<li class="travel_logo" data-id="'+appList[j].id+'"><a  title="'+appList[j].title+'">';
                            if(appList[j].path != null && appList[j].path != ""){
                                appHtml += '<img  src="'+t1img+'/'+appList[j].path+'" alt="">';
                            }
                            appHtml += '&nbsp;'+appList[j].title+'</a><span class="icon_chose_check"><i class="fa fa-check"></i></span></li>';
                        }
                    }else{
                        innerHtml += '<li  name="appHover" onclick="changeArea(this)"  id="'+logoResultList[i].tourDistrictId+'">'+logoResultList[i].tourDistrictName+'</li>';
                    }
                }
                innerHtml += '</ul><i onclick="getDown(this);" class="fa fa-chevron-down" aria-hidden="true"></i></div>';
                appHtml += '</ul></div><div class="homepage_right">';
                appHtml += '<a class="choose_more">+ 多选</a>';
                appHtml += '</div><div class="homepage_list_bottom"><span class="ensure unable">确定</span><span class="cancel">取消</span></div>';
                if(flag == true) {
                    $(".top-suspend").html("");
                    $(".top-suspend").html(innerHtml + appHtml);
                }else {
                    $(".top-suspend").children(".homepage_content_list.model-top").replaceWith(appHtml);
                }
            }
    });
}
//分页
function culPage(obj,flag){
    var page =1;
    var pageSize = screenSelected.pageSize;
    var total = Math.ceil(pageTest.count/screenSelected.pageSize);
    if(flag == false && screenSelected.pageNo <total){
        page = screenSelected.pageNo+1;
    }else if(flag == true && screenSelected.pageNo >1){
        page = screenSelected.pageNo-1;
    }else{
        return false;
    }
    goPage(page,pageSize);
}
function goPage(page,pageSize){
    // var tab = sessionStorage.getItem("nowSelect");
    screenSelected.pageNo=page,//当前页码（点击的页码）
    screenSelected.pageSize=pageSize,//每页显示记录数
    pageTest.pageNo=page,//当前页码（点击的页码）
    pageTest.pageSize=pageSize,//每页显示记录数
    getListData();//获取列表数据
}
//排序
function  sortby(id,obj){
    screenSelected.orderBy=[];
    var ems = $(obj).children("i").children("em");
    if(id=='defaultSort'){//默认排序
        screenSelected.orderBy.push(id+"  DESC");
    }else{
        if ($(ems[1]).attr("class").indexOf("rank_down_checked") != -1) {//当前是降序
            screenSelected.orderBy.push(id + "  ASC");
        } else if ($(ems[0]).attr("class").indexOf("rank_up_checked") != -1) {
            screenSelected.orderBy.push(id + "  DESC");
        } else {//默认是升序
            screenSelected.orderBy.push(id + "  ASC");
        }
    }
    // var tab = sessionStorage.getItem("nowSelect");
    getListData();//获取列表数据
}

/*
 * 删除某个筛选条件
 * */
function  deleteSel(obj){
    var p = $(obj).parent().attr("id");
    switch(p){
        case "fromAreaList":screenSelected.startCityParaQ=[];break;//出发城市
        case "targetCountry":screenSelected.endCityParaQ=[];break;//目的地
        case "targetCity":screenSelected.targetCity=[];break;//抵达城市
        case "linePlay":screenSelected.linePlay=[];break;//游玩线路
        case "travelDays":screenSelected.dayParaQ=[];break;//行程天数
        case "priceRange":screenSelected.priceParaQ=[];break;//价格区间
        case "remainingSeat":screenSelected.freeParaQ=[];break;//余位
        case "startDate":screenSelected.groupDateParaQ=[];break;//出团日期
        case  "supplierInfos":screenSelected.supplierParaQ=[];break;//供应商
    }
    // var tab = sessionStorage.getItem("nowSelect");
    getScreenData();//获取筛选条件
    getListData();//获取列表数据
}
/*
 * 根据返回数据排序结果
 * arr：排序参数（属性和升序或降序）
 * pageNo：当前页数
 * count：总共的数据条数
 * last：总共有多少页
 * */
function  showSort(arr,pageNo,count,last){
    var spans = $(".rank").children("span");
    for(var i=0;i<spans.length;i++){
        var emh = $(spans[i]).children("i").children("em");
        for(var j=0;j<emh.length;j++) {
            if ($(emh[j]).attr("class").indexOf("rank_up_checked") != -1) {
                $(emh[j]).attr('class', 't1_2 rank_up')
            } else if ($(emh[j]).attr("class").indexOf("rank_down_checked") != -1) {
                $(emh[j]).attr('class', 't1_2 rank_down')
            }
        }
    }
    for(var i=0;i<arr.length;i++){
        var ids = arr[i].property;
        if(ids=='minGroupOpenDate'||ids=='maxGroupOpenDate'){
            ids = 'groupOpenDate';
        }else if(ids=='minQuauqSettlePrice'){
            ids = 'quauqPrice';
        }
        if(arr[i].direction == "ASC"){
            $("#"+ids).children("i").children("em").first().attr('class','t1_2 rank_up_checked');
        }else if(arr[i].direction == "DESC"){
            $("#"+ids).children("i").children("em").last().attr('class','t1_2 rank_down_checked');
        }
    }
    $("#totalList").text(count);
    $("#orange").text(pageNo);
    $("#totalPage").text(last);
}
/*
 * 根据返回数据显示列表数据
 * arr：列表数据
 * */
function loadTable(arr,pageNo,count,pageSize){
    var html = '';
    if(arr.length>0) {
        for (var i = 0; i < arr.length; i++) {
            var ti;
            if ((i + 1) < 10) {
                ti = "0" + (i + 1);
            } else {
                ti = i + 1;
            }
            if ((i%2) != 0) {
                html += '<tr class="addOdd"><td width="35px" class="first_t"><p>' + ti + '</p></td><td width="400px">';
            } else {
                html += '<tr><td width="35px" class="first_t"><p>' + ti + '</p></td><td width="400px">';
            }
            html += '<p title="' + arr[i].activityName + '"><a href="javascript:void(0)"  onclick="details(' + arr[i].travelactivity_id + ',\'' + arr[i].groupCode + '\',\''+ctx+'\')">' + arr[i].activityName + '</a></p><br/><p> <span>行程天数：</span><span>' + arr[i].activityDuration + '天</span><span class="margin_left_20">供应商：</span><span>' + arr[i].supplierName + '</span></p></td>';
            html += '<td width="90px"><p>' + arr[i].groupCode + '</p></td><td width="90px"><p>' + arr[i].groupOpenDate + '</p></td><td width="140px"><p>' + arr[i].fromArea + '</p></td>';
            html += '<td width="50px" class="relative"><p class="surplus">' + arr[i].freePosition + '</p>';
            if(arr[i].)'<i class="realTime">实时</i></td><td width="70px" class="t_right ">';
            html += '<p>'+arr[i].suggestAdultPrice + '</p></td><td width="120px" class="last_t t_right">';
            var quauqPrice = arr[i].quauqPrice
            // 0522需求，对于设置定价策略导致供应价大于同行价的团期,供应价以同行价展示
//            var settlementPrice = arr[i].settlementPrice;
//        	if(Number(quauqPrice) > Number(settlementPrice)){
//        		quauqPrice = settlementPrice;
//        	}
        	quauqPrice = formatMoney(quauqPrice ,2);
        	// 0522需求，对于设置定价策略导致供应价大于同行价的团期,供应价以同行价展示
            if (arr[i].quauqPrice != "" && arr[i].quauqPrice != null) {
                html += '<p>' + arr[i].currencyids + '<span class="money_color">' + quauqPrice + '</span></p>';
            }
            html += '<br/><p class="trade_price">同行价：<span title=""><del>'+ arr[i].settlementAdultPrice + '</del></span></p></td></tr>';
        }
    }else{
       html += '<tr class="no-color"><td  colspan="8"  class="no-match" style="text-align:center;"><p>很抱歉！无法搜索到符合您要求的产品，请"<span>重新输入关键词</span>"</p><br/><span  class="sub-tips">您可以简化、缩短关键词或减小筛选范围再进行搜索 或 </span><br/><div class="home-btn" onclick="reload();">返回首页</div></td></tr>';
    }
    $("#content").empty();
    $("#content").append(html);
    if($("#content tr").first().hasClass('no-color')){
        $("#startDate").parent().remove();
    }
    //分页信息
    pageTest.count=count;//数据总条数
    pageTest.pageSize=pageSize;//数据总条数
    pageTest.pageNo=pageNo;//数据总条数
    var pageHtml= doPage(pageTest);
    $("#page").empty();
    $("#page").append(pageHtml);
    if(version == 'ie8'){
        $(".skip").css('vertical-align','middle');
        $(".skip input").css('vertical-align','middle');
        $(".skip input").css('line-height','28px');
    }
}

function loadProductTable(arr,pageNo,count,pageSize){
    var html = '';
    if(arr.length>0) {
        for (var i = 0; i < arr.length; i++) {
            var ti;
            if ((i + 1) < 10) {
                ti = "0" + (i + 1);
            } else {
                ti = i + 1;
            }
            if(arr[i].acitivityName.length>60){
                var activityname = arr[i].acitivityName.substring(0,60);
            }else{
                var activityname = arr[i].acitivityName;
            }
            //序号
            html += '<tr><td class="first_t"><p>' + ti + '</p></td>';
            //productInfo
            html += '<td class="productInfo"><p title="' + arr[i].acitivityName + '"><a onclick="details(' + arr[i].travelactivity_id + ',\'' + arr[i].groupCode + '\',\''+ctx+'\')"href="javascript:void(0)" target="_blank">' + activityname +'</a></p>'+
                    '<p><span>出发城市</span>'+ arr[i].fromArea +'</p>' +
                    '<p><span>行程天数</span>' + arr[i].activityDuration+ '天</p>'+
                    '<p><span>出团日期</span>' + arr[i].groupOpenDate ;
            if(arr[i].dateSizeFlag==1){
                html +='<a class="moreInfo" onclick="details(' + arr[i].travelactivity_id + ',\'' + arr[i].groupCode + '\',\''+ctx+'\')" href="javascript:void(0);">更多<i class="fa fa-angle-double-right" aria-hidden="true"></i></a>';
            }
            html+='</p></td>';
            //supplierInfo
            html += '<td class="supplierInfo"><p>' + arr[i].officeName + '</p>' ;
            var _bc = arr[i].businessCertificate,
                _bl = arr[i].businessLicense,
                _cp = arr[i].cooperationProtocol;

            if( _bc || _bl || _cp) {
                html += '<div class="pop-container"> <a class="pop_permission" href="javascript:void(0);" target="_blank"><em></em>认证详情</a>' +
                        '<div class="pop_permission_show" style="display: none;"><em class="pop_per_angle"></em>' +
                        '<div class="pop_per_content"><span>该批发商已经通过认证</span>' +
                        '<a href="'+ctx+'/wholesalers/certification/officeDetail?companyId='+ arr[i].officeId +'" target="_blank">认证详情 <i class="fa fa-angle-double-right"></i></a><ul class="auth_icon">' ;
                var info = [[_bc,"certificate","real_name","资质证书"],[_bl,"license","business_licen","营业执照"],[_cp,"protocol","coop_deal","合作协议"]];
                for(var _i=0,j=info.length;_i<j;_i++){
                    var inf = info[_i];
                    if(inf[0]){
                        html+='<li class="'+ (_i==2?"auth_icon_lastli":"") +'" onclick="showName(\''+inf[1]+'\')"><a><em class="'+inf[2]+'"></em><br>'+inf[3]+'</a></li>';
                    }else{
                        html+='<li  class="'+ (_i==2?"auth_icon_lastli":"") +'" onclick="showName(\''+inf[1]+'\')"><a class="not_click_a"><em class="'+inf[2]+'_none"></em><br>'+inf[3]+'</a></li>';
                    }

                }
                html +='</ul></div></div>';
                html += '<div class="htmleaf-container">';
                html += '<div class="docs-galley">';
                html += '<ul class="docs-pictures certificate clearfix" style="display:none">';
                // 资质证书图片

                if (_bc) {
                    for (var l = 0; l < _bc.length; l++) {
                        var businessCertificatesdocId = _bc[l].id;
                        var businessCertificatesdocName = _bc[l].name;
                        html += '<li><div ><img data-original="' + ctx + '/person/info/getLogo?id=' + businessCertificatesdocId + '" alt="' + businessCertificatesdocName + '" src="' + ctx + '/person/info/getLogo?id=' + businessCertificatesdocId + '" class="viewer-toggle"></div></li>';
                    }
                }
                html += '</ul><ul class="docs-pictures license clearfix" style="display:none">';
                // 营业执照图片
                if (_bl) {
                    for (var m = 0; m < _bl.length; m++) {
                        var businessLicensesdocId = _bl[m].id;
                        var businessLicensesdocName = _bl[m].name;
                        html += '<li><div onclick="console.log(1)"><img data-original="' + ctx + '/person/info/getLogo?id=' + businessLicensesdocId + '" alt="' + businessLicensesdocName + '" src="' + ctx + '/person/info/getLogo?id=' + businessLicensesdocId + '" class="viewer-toggle"></div></li>';
                    }
                }
                html += '</ul>';
                html += '<ul class="docs-pictures protocol clearfix" style="display:none">';
                // 合作协议图片
                if (_cp) {
                    for (var n = 0; n < _cp.length; n++) {
                        var cooperationProtocolsdocId = _cp[n].id;
                        var cooperationProtocolsdocName = cp[n].name;
                        html += '<li><div><img data-original="' + ctx + '/person/info/getLogo?id=' + cooperationProtocolsdocId + '" alt="' + cooperationProtocolsdocName + '" src="' + ctx + '/person/info/getLogo?id=' + cooperationProtocolsdocId + '" class="viewer-toggle"></div></li>';
                    }
                }
                html += '</ul></div></div>';
            }
            html +='</td>';

                //price
            html += '<td class="last_t t_right systemPrice"><p><span class="unit_color">'+arr[i].quauqCurrencyMark+'</span><span class="money_color">'+ arr[i].quauqSettlePrice +'</span>起</p>'+
                    '<p class="trade_price">同行价：<span><del>'+ arr[i].settlementPrice +'起</del>'+
                    '</span></p><input class="buyNow" onclick="details(' + arr[i].travelactivity_id + ',\'' + arr[i].groupCode + '\',\''+ctx+'\')" type="button" value="立即采购"></td></tr>';
        }
    }else{
        html += '<tr class="no-color"><td  colspan="8"  class="no-match" style="text-align:center;"><p>很抱歉！无法搜索到符合您要求的产品，请"<span>重新输入关键词</span>"</p><br/><span  class="sub-tips">您可以简化、缩短关键词或减小筛选范围再进行搜索 或 </span><br/><div class="home-btn" onclick="reload();">返回首页</div></td></tr>';
    }
    $("#content").empty();
    $("#content").append(html);
    if($("#content tr").first().hasClass('no-color')){
        $("#startDate").parent().remove();
    }
    //分页信息
    pageTest.count=count;//数据总条数
    pageTest.pageSize=pageSize;//数据总条数
    pageTest.pageNo=pageNo;//数据总条数
    var pageHtml= doPage(pageTest);
    $("#page").empty();
    $("#page").append(pageHtml);
    if(version == 'ie8'){
        $(".skip").css('vertical-align','middle');
        $(".skip input").css('vertical-align','middle');
        $(".skip input").css('line-height','28px');
    }
    //动态引入js
    $("body #script").empty();
    $("#script").append('<script type="text/javascript" src="/static/js/picView/viewer.js"></script>');
     $("#script").append('<script type="text/javascript" src="/static/js/picView/main.js"></script>');

}

function formatMoney(s, type) {//金额格式化-s:原始金额 type:小数点后位数
    if (/[^0-9\.]/.test(s))
        return "0";
    if (s == null || s == "")
        return "0";
    s = s.toString().replace(/^(\d*)$/, "$1.");
    s = (s + "00").replace(/(\d*\.\d\d)\d*/, "$1");
    s = s.replace(".", ",");
    var re = /(\d)(\d{3},)/;
    while (re.test(s))
        s = s.replace(re, "$1,$2");
    s = s.replace(/,(\d\d)$/, ".$1");
    if (type == 0) {// 不带小数位(默认是有小数位)
        var a = s.split(".");
        if (a[1] == "00") {
            s = a[0];
        }
    }
    return s;
}
/*
 * 调用后台，加载列表数据
 * */
function getListData() {
    var listType = sessionStorage.getItem("nowSelect");
    changeTab(listType);
    $("table thead").empty();
    $("table tbody").empty();
    if(listType =='productList'){
        $("table").attr("id","productTable");
        var productTh = sessionStorage.getItem("productThead");
        $("table thead").append(productTh);
        $.ajax({
            type: "post",
            url: ctx + "/t1/homePageActivityList",
            data: {"params": JSON.stringify(screenSelected)},
            async: true,
            dataType: 'json',
            success: function (data) {
                if (data.responseCode == "authentication") {
                    window.location.href = ctx + "/login";
                } else {
                    var obj = eval(data.page);
                    var arr = [];
                    arr.push(obj["list"]);//列表数据
                    var sort = [];
                    sort.push(obj["sort"]);
                    //加载表格数据
                    loadProductTable(arr[0], obj["pageNo"], obj["count"], obj["pageSize"]);
                    showSort(sort[0], obj["pageNo"], obj["count"], obj["last"]);
                }
            }
        });
    }else if(listType =='groupList') {
        $("table").removeAttr("id");
        var groupTh = sessionStorage.getItem("groupThead");
        $("table thead").append(groupTh);
        $.ajax({
            type: "post",
            url: ctx + "/t1/homePageList",
            data: {"params": JSON.stringify(screenSelected)},
            async: true,
            dataType: 'json',
            success: function (data) {
                if (data.responseCode == "authentication") {
                    window.location.href = ctx + "/login";
                } else {
                    var obj = eval(data.page);
                    var arr = [];
                    arr.push(obj["list"]);//列表数据
                    var sort = [];
                    sort.push(obj["sort"]);
                    //加载表格数据
                    loadTable(arr[0], obj["pageNo"], obj["count"], obj["pageSize"]);
                    showSort(sort[0], obj["pageNo"], obj["count"], obj["last"]);
                }
            }
        });
    }
}
/*
 * 显示已选择的筛选项
 * arr:选择的筛选项（eg:天马旅游、quauq）
 * id：选择的筛选类型id
 * title：选择的筛选类型name(eg:供应商)
 * */
function addSelected(arr, id, title) {
    var str = "";
    for(var i=0;i<arr.length;i++){
        str += arr[i].name + "、";
    }
    var subStr = str.substring(0, str.length - 1);
    var html = "<dl  class='selt-items'  id='" + id + "'><dt  title='"+subStr+"'>" + title + "：<span>" + subStr + "</span></dt><dd  class='compatibleIe' onclick='deleteSel(this)'>" ;
    if(version == "ie8" || version == "ie10" ||version == "ie9"){
        html += "<em style='position:relative;top:-7px;' class='delt-all'></em>";
    }else{
        html += "<em class='delt-all'></em>";
    }
    html += "</dd></dl>";
    $(html).insertBefore($("#addSelect").find("#addContainer").children("span").last());
    if(version == "ie8" || version == "ie9"  || version == "ie10"){
        $("#addSelect").css('display', 'block');
    }else{
        $("#addSelect").css('display', 'flex');
    }
}
/*
 *加载除供应商之外的筛选条件
 * id：筛选类型id
 * name：少选类型（eg：出发城市）
 * arr：筛选数据（数组）
 * */
function  loadScreen(id,name,arr){
    var html = "";
    html += "<dl class='search-item'>";
    html += "<dt  id='"+id+"'>"+name+"</dt><dd><div  style='padding-left:15px;'><div class='items-container'><span class='selected'>全部</span>";
    for(var i=0;i<arr.length;i++){
        html += "<span id='"+arr[i].id+"'>"+arr[i].name+"</span>";
    }
    if(id=="startDate" ) {
        html += "<span class='date-text'><input type='text' class='dateinput  homeInput' onclick='WdatePicker();'> — <input type='text' class='dateinput  homeInput' onclick='WdatePicker();'><span  onclick='clcSure(this);'  class='ascertain-btn'>确定</span></span>";
    }else if(id=="travelDays" || id=="priceRange" || id=="remainingSeat"){
        html += "<span class='date-text'><input type='text'  class='dateinput  homeInput'> — <input type='text' class='dateinput  homeInput'><span  onclick='clcSure(this);'  class='ascertain-btn'>确定</span></span>";
    }
    html += "<em class='more-item  multi-select'><a href='#'>+多选</a></em>";
    html += "<em class='more-item'><a href='#'>更多</a><i class='fa fa-angle-down' aria-hidden='true'></i></em>";
    html += "</div></div><div class='sure_btn'><span  class='ensure unable'>确定</span><span class='cancel'>取消</span></div></dd></dl>";
    $(".search-container").append(html);
}
/*
 * 根据返回结果显示供应商
 * id：筛选类型id
 * name：筛选类型（eg：出发城市）
 * arr：筛选数据（数组）
 * */
function  loadAppply(id,name,arr){
    var html = '';
    html += '<div class="homepage_content_list "><div class="homepage_left product_left"><span  class="tarvel_Area"  id="'+id+'">'+name+'</span></div>';
    if(version == 'ie9' || version == "ie10"){
        html += '<div class="homepage_middle_pa" style="padding-right:49px;"><ul class="homepage_middle">';
    }else{
        html += '<div class="homepage_middle_pa" style="margin-right:19px;"><ul class="homepage_middle">';
    }
    for(var i=0;i<arr.length;i++){
        html += '<li class="travel_logo" data-id="'+arr[i].id+'"><a title="'+arr[i].name+'">';
        if(arr[i].path != ""){
            html += '<img  src="'+t1img+'/'+arr[i].path+'" alt="">';
        }
        html += '&nbsp;'+arr[i].name+'</a><span class="icon_chose_check"><i class="fa fa-check"></i></span></li>';
    }
    html += '</ul></div><div class="homepage_right"><a class="choose_more">+ 多选</a>';
    if(arr.length >12){
        html += '<a class="more_icon_show">';
    }else{
        html += '<a style="display:none;" class="more_icon_show">';
    }
    html += '更多 <i class="fa fa-angle-down"></i></a></div><div class="homepage_list_bottom  product"><span  class="ensure unable">确定</span><span class="cancel">取消</span></div></div>';
    $(".search-container").append(html);
}
/*
 * 调用后台接口刷新筛选条件
 * */
function getScreenData(){
    $.ajax({
        type: "post",
        url: ctx + "/t1/getFilterCondition",
        data: {"params":JSON.stringify(screenSelected)},
        async: true,
        dataType: 'json',
        beforeSend: function(){
            showWaiting("waitingImg");
        },
        complete: function () {
            hideWaiting("waitingImg");
        },
        success: function (data) {
            if(data.results.nav == ""){
                $("#reginType").text("导航");
            }else{
                $("#reginType").text(data.results.nav);
            }
            $(".search-container").html('<dl id="addSelect" class="search-item  container-selected"><dt>您已选择：</dt><dd><div  style="padding-left:15px;"><div id="addContainer" class="items-container"   style="padding-right: 0px;"><span  class="clear-all">清空筛选条件</span></div></div></dd></dl>');
            var obj = eval(data.results.notChoose);/*未选择的筛选项*/
            var objselected = eval(data.results.isChoose);/*已选择的筛选项*/
            for (var index in obj) {
                var arr = [];
                arr.push(obj[index]);
                switch (index) {
                    case "fromAreaList":
                        if(arr[0].length>0){
                            loadScreen( "fromAreaList", "出发城市", arr[0]);
                        }
                        break;//出发城市
                    case "targetCountry":
                        if(arr[0].length>0) {
                            loadScreen("targetCountry", "目的地", arr[0]);
                        }
                        break;//目的地
                    case "targetCity":
                        if(arr[0].length>0) {
                            loadScreen("targetCity", "抵达城市", arr[0]);
                        }
                        break;//抵达城市
                    case "linePlay":
                        if(arr[0].length>0) {
                            loadScreen("linePlay", "游玩线路", arr[0]);
                        }
                        break;//游玩线路
                    case "travelDays":
                        if(arr[0].length>0) {
                            loadScreen("travelDays", "行程天数", arr[0]);
                        }
                        break;//行程天数
                    case "priceRange":
                        if(arr[0].length>0) {
                            loadScreen("priceRange", "价格区间", arr[0]);
                        }
                        break;//价格区间
                    case "remainingSeat":
                        if(arr[0].length>0) {
                            loadScreen("remainingSeat", "余位", arr[0]);
                        }
                        break;//余位
                    case "supplierInfos":
                        if(arr[0].length>0){
                            loadAppply("supplierInfos", "供应商", arr[0]);
                        }
                        break;//供应商
                    case "startDate":
                        loadScreen("startDate", "出团日期", arr[0]);
                        break;//出团日期
                }
            }
            for (var index2 in objselected) {
                var arr = [];
                arr.push(objselected[index2]);
                switch (index2) {
                    case "fromAreaList":
                        if(arr[0].length>0){
                            addSelected(arr[0], "fromAreaList", "出发城市");
                        }
                        break;//出发城市
                    case "targetCountry":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "targetCountry", "目的地");
                        }
                        break;//目的地
                    case "targetCity":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "targetCity", "抵达城市");
                        }
                        break;//抵达城市
                    case "linePlay":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "linePlay", "游玩线路");
                        }
                        break;//游玩线路
                    case "travelDays":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "travelDays", "行程天数");
                        }
                        break;//行程天数
                    case "priceRange":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "priceRange", "价格区间");
                        }
                        break;//价格区间
                    case "remainingSeat":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "remainingSeat", "余位");
                        }
                        break;//余位
                    case "supplierInfos":
                        if(arr[0].length>0){
                            addSelected(arr[0], "supplierInfos", "供应商");
                        }
                        break;//供应商
                    case "startDate":
                        if(arr[0].length>0) {
                            addSelected(arr[0], "startDate", "出团日期");
                        }
                        break;//出团日期
                }
            }
            judeMore();
            /*判断是否显示更多筛选条件*/
            var childs = $(".search-container").children().length-1;
            if (childs <= 4) {
                $(".more-screen").hide();
            }else{
                $(".more-screen").html('显示更多筛选条件<i class="fa fa-angle-double-down" aria-hidden="true"></i>');
                $(".more-screen").show();
            }
            /*是否显示虚线*/
            judeCount();
            $(".search-container").css('min-height','auto');
            if($("#content tr").first().hasClass('no-color')){
                if($("#startDate").parent().hasClass("search-item")){
                    $("#startDate").parent().remove();
                }
            }
            if(version == "ie8"){
                $(".homeInput").css('vertical-align','middle');
                $(".homeInput").css('line-height','25px');
                $(".homeInput").parent().css('vertical-align','bottom');
            }
            $("#startDate").parent().children("dd").find(".multi-select").remove();
        }
    });
}
function showWaiting(id){
    $("#"+id).show();
}
function hideWaiting(id){
    $("#"+id).hide();
}
/*
 * 判断是否出现更多
 * */
function  judeMore(){
    $(".items-container").each(function(){
        var $par = $(this).parent().parent().parent();
        if(!$par.hasClass("container-selected")){
            var lheight = $(this).parent().parent().height();
            var theigth = $(this).height();
            if(theigth < lheight){
                $(this).children("em").last().remove();
            }
        }
    });
}
//隐藏不常用的筛选
function judeCount() {
    /*筛选项数量*/
    var count = Number($(".search-container").children().length-1);
    var cts = 4;//默认显示4条
    if (count > cts) {
        $(".search-container").children().eq(cts).css('border-bottom', 'none');
        for (var i = count; i >cts; i--) {
            $(".search-container").children().eq(i).addClass('display-none');
        }
    }
    $(".search-container").children("dl").last().css('border-bottom', 'none');
}
function rakeList(){
    var keyword = $("#keyword").val();
    if(keyword =="" || keyword == null){
        return false;
    }
    emptyObj();
    screenSelected.tourOutInQ = "";
    screenSelected.travelAreaIdQ = "";
    screenSelected.keywordQ = keyword;
    screenSelected.pageNo = 1;
    var tab = sessionStorage.getItem("defaultSelect");
    sessionStorage.setItem("nowSelect",tab);
    getScreenData();//获取筛选条件
    getListData();//获取列表数据
}

//清空筛选对象
function emptyObj(){
 /*   screenSelected.tourOutInQ="";//标签页切换以及初始化，出境游或者国内游;
    screenSelected.travelAreaIdQ="";//标签页切换以及初始化，东南亚;*/
    screenSelected.startCityParaQ=[];//出发城市
    screenSelected.endCityParaQ=[];//目的地
    screenSelected.targetCity=[];//抵达城市
    screenSelected.linePlay=[];//游玩线路
    screenSelected.supplierParaQ=[];//供应商
    screenSelected.groupDateParaQ=[];//出团日期开始日期
    screenSelected.dayParaQ=[];//行程天数开始天数
    screenSelected.priceParaQ=[];//价格
    screenSelected.freeParaQ=[];//开始余位
}
//个人中心展开start
function showNoticeList(){
    if($("#userCenterList").hasClass("expended")){
        $(".user_management").css("background-position"," -100px -129px");
        $("#userCenterList").animate({
            height: "0px"
        }, function () {
            $("#userCenterList").removeClass('expended');
        });
    }else{
        $(".user_management").css("background-position","-97px -102px");
        $("#userCenterList").animate({
            height: "70px"
        }, function () {
            $("#userCenterList").addClass('expended');
        });
    }
}
/*点击quauq图标返回登陆页*/
function reload() {
    window.location.href = ctx+"/t1/newHome";
}
//判断ie浏览器版本
function  checkIe(){
    if(navigator.userAgent.indexOf("MSIE 8.0")>0){
        return "ie8";
    }else if(navigator.userAgent.indexOf("MSIE 9.0")>0){
        return  "ie9";
    }else if(navigator.userAgent.indexOf("MSIE 10.0")>0){
        return  "ie10";
    }else if((navigator.userAgent.toLowerCase().indexOf("trident") > -1 && navigator.userAgent.indexOf("rv") > -1)){
        return  "ie11";
    }else{
        return "";
    }
    return "";
}