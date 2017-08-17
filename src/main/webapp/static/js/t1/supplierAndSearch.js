/**
 * Created by wanglijun on 2016/11/23.
 * 区域下拉框以及模糊搜索框 相关js
 */
var ctx;
ctx=$("#ctx").val();
var t1img;
//bug 详情页面 导航栏图片不显示 start
t1img=$("#ctxStatic").val()+"/t1img";
//end
var ctxStatic;
var str;
var st;
var isSus = false;
var url= ctx+'/t1/jumpParam';
var navSelected={//筛选条件对象
    keywordQ:"",//搜索关键字
    supplierParaQ:[],//供应商多选的时候
    tourOutInQ:"100000",//标签页切换以及初始化，出境游或者国内游,
    pageNo:1,//当前页码（点击的页码）
    pageSize:10,//每页显示记录数
    orderBy:[],//排序
    fromDetail:"true"
};
var freshInApplication=window.freshInApplication||function (){
        var param=sessionStorage.getItem("param");
        var paramObject=JSON.parse(sessionStorage.getItem("param"));
        var pts = JSON.parse(paramObject.travelSelected);
        pts["supplierParaQ"]=screenSelected.supplierParaQ;//供应商
        pts["startCityParaQ"]=screenSelected.startCityParaQ;
        pts["endCityParaQ"]=screenSelected.endCityParaQ;
        pts["targetCity"]=screenSelected.targetCity;
        pts["linePlay"]=screenSelected.linePlay;
        pts["dayParaQ"]=screenSelected.dayParaQ;
        pts["priceParaQ"]=screenSelected.priceParaQ;
        pts["freeParaQ"]=screenSelected.freeParaQ;
        pts["groupDateParaQ"]=screenSelected.groupDateParaQ;
        pts["pageNo"]=screenSelected.pageNo;
        pts["pageSize"]=screenSelected.pageSize;
        pts["orderBy"]=screenSelected.orderBy;
        paramObject.travelSelected=JSON.stringify(pts)
        sessionStorage.setItem("param",JSON.stringify(paramObject));
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

function rakeList(){
    var keyword = $("#keyword").val();
    if(keyword =="" || keyword == null){
        return false;
    }
    resetSearch();//清空筛选条件
    navSelected.tourOutInQ = "";
    navSelected.travelAreaIdQ = "";
    navSelected.keywordQ = keyword;
    navSelected.pageNo = 1;
    var tab = sessionStorage.getItem("defaultSelect");
    sessionStorage.setItem("nowSelect",tab);
    sessionStorage.setItem("navSelected",JSON.stringify(navSelected));
    //t1页面刷新，搜索数据缓存  目前只涉及keyWord   ymx
    var param = JSON.parse(sessionStorage.param);
    param.tabText  = keyword;
    param.travelSelected="";//modify by wlj at 20161212
    sessionStorage.setItem("param",JSON.stringify(param));

    window.open(url,'_self');
}
function resetSearch(){
    navSelected={//筛选条件对象
        // keywordQ:"",//搜索关键字
        supplierParaQ:[],
        tourOutInQ:"100000",//标签页切换以及初始化，出境游或者国内游,
        pageNo:1,//当前页码（点击的页码）
        pageSize:10,//每页显示记录数
        orderBy:[],//排序
        fromDetail:"true"
    };
}
function showWaiting(id){
    $("#"+id).show();
}
function hideWaiting(id){
    $("#"+id).hide();
}
/*
 * 点击区域搜索列表数据
 * */
function changeArea(obj){
    navSelected.keywordQ = "";
    navSelected.pageNo =1;
    $("#keyword").val("");
    $(".top-suspend").hide();
    //修改标签字段
    var id = $(obj).attr("id");
    $("#travelAreaId").val(id);
    var text = $(obj).text();
    $("#travelAreaName").val(text);
    var str = $(".main-nav").children("li[class='select']").text() + "/" + text;
    $("#reginType").text(str);
    resetSearch();//清空筛选条件
    var io = $(".main-nav").children("li[class='select']").attr("id");
    navSelected.tourOutInQ = io;
    navSelected.travelAreaIdQ = id;
    var tab = sessionStorage.getItem("defaultSelect");
    sessionStorage.setItem("nowSelect",tab);
    sessionStorage.setItem("navSelected",JSON.stringify(navSelected));
//modfiy by wlj for F5
    //如果详情页面的输入框值不为空，则把改值写在缓存中
    var _tempObj= JSON.parse(sessionStorage.getItem("param"));
    _tempObj.tabText="";
    sessionStorage.setItem("param",JSON.stringify(_tempObj));
//modfiy by wlj for F5
    window.open(url,'_self');
}
(function(){

    var href = window.location.href;
    if (href.indexOf("huitengguoji.com", href) != -1||href.indexOf("travel.jsjbt", href) != -1) {
    } else {
        var param = JSON.parse(sessionStorage.param);
        var productId =param.productId;
        /*  var _inOutArea =param.inOutArea;
         $("#reginType").text(_inOutArea);
         param.inOutArea="";
         sessionStorage.setItem("param",JSON.stringify(param))*/
        $.ajax({
            type: "post",
            url:ctx+"/t1/getNav",
            data: {"productId":productId},
            async: true,
            success:function(data){
                $("#reginType").text(data);
            },
            error:function (e) {
            },
            complete:function () {

            }
            
        })
    }
    $(".top-suspend").live("hover",function(event){
        event = event || window.event;
        if(event.type=='mouseover' || event.type == 'mouseenter'){
            $(this).show();
            isSus = true;
        }else{
            $(this).hide();
            isSus = false;
        }
    })

    $(".top-tab").live("hover",function(event){
        event = event || window.event;
        //切换选中样式
        if(event.type=='mouseover' || event.type == 'mouseenter'){
            isSus = true;
            getArea("","",true,true);
            $(".top-suspend").show();
        }else{
            isSus = false;
            $(".top-suspend").hide();
        }
    })
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
                            /*navSelected.tourOutInQ = tourOutInList[i].id;*/
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
                                appHtml += '<img src="'+ctx+ '/person/info/getLogo?id=' + appList[j].logoUrl+'" alt="">';
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

    //点击供应商多选展开logo和选择框
    $(".choose_more").live("click",function () {
        var $par = $(this).parent().parent();
        $par.addClass("select_model");
        if(isSus){
            $par.children(".homepage_list_bottom").show();
            $(this).hide();
        }
        //modify by wlj at1128 for 16926
        var $middle = $(this).parent().parent().find(".homepage_middle");
        $middle.css("overflow-y","auto");
        $middle.css("maxHeight","209px");
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
        //modify by wlj at1128 for 16926
        var $middle = $(this).parent().parent().find(".homepage_middle");
        $middle.css("overflow-y","hidden");
        $middle.css("maxHeight","140px");
    });

    //点击供应商图标
    //导航栏供应商-点击清空搜索条件
    $(".homepage_middle a").live("click",function () {
        var tab = sessionStorage.getItem("defaultSelect");
        sessionStorage.setItem("nowSelect",tab);
        //判断当前页面是从哪里进入的
        var pageFrom=$("#jspId").val();
        if(pageFrom!="theSecondPage"){
            navSelected["fromDetail"]=true;
            navSelected.pageNo = 1;
            navSelected.orderBy =[];
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
                var dataId = $(this).parent().attr('data-id');
                $(this).parent().parent().parent().parent().parent().hide();
                isSus = false;
                resetSearch();
                navSelected.keywordQ = "";
                $("#keyword").val("");
                $("#reginType").text(str);
                var io = $(".main-nav").children("li[class='select']").attr("id");
                var ti = $(".sub-nav").children("ul").children("li[class='select']").attr("id");
                navSelected.tourOutInQ = io;
                navSelected.travelAreaIdQ = ti;
                navSelected.supplierParaQ.push(dataId);
                sessionStorage.setItem("navSelected",JSON.stringify(navSelected));

                //点击单个的供应商图标的时候，清空所有原先的缓存条件
                var param = JSON.parse(sessionStorage.param);
                param.tabText  = "";
                sessionStorage.setItem("param",JSON.stringify(param));





                window.open(url,'_self');
            }}else {//次首页  列表页进来的，不做跳转，否则做跳转
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
                if(isSus){//悬浮窗上的供应商图标
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
                    //点击单个的供应商图标的时候，清空所有原先的缓存条件
                    var param = JSON.parse(sessionStorage.param);
                    param.tabText  = "";
                    param.travelSelected="";
                    sessionStorage.setItem("param",JSON.stringify(param));
                }else{//非悬浮窗上的供应商图标，筛选条件列的   这个时候如果有关键字，关键字不消失，继续生效
                    $(this).parent().parent().parent().parent().remove();
                }
                var dataId = $(this).parent().attr('data-id');
                screenSelected.supplierParaQ.push(dataId);
                freshInApplication();
                getScreenData();//获取筛选条件
                getListData();//获取列表数据
            }
        }
    });
    //供应商多选后的确定按钮点击事件
    $(".homepage_list_bottom  .ensure").live("click",function () {
        //判断当前页面是从哪里进入的
        var pageFrom=$("#jspId").val();
        var tab = sessionStorage.getItem("defaultSelect");
        sessionStorage.setItem("nowSelect",tab);
        if(pageFrom!="theSecondPage"){
            navSelected.pageNo =1;
            if($(this).hasClass("unable")){
                return false;
            }
            navSelected.orderBy =[];
            if(isSus){
                $(this).parent().parent().parent().hide();
                isSus = false;
                resetSearch();
                navSelected.keywordQ = "";
                $("#keyword").val("");
                $("#reginType").text(str);
                var io = $(".main-nav").children("li[class='select']").attr("id");
                var ti = $(".sub-nav").children("ul").children("li[class='select']").attr("id");
                navSelected.tourOutInQ = io;
                navSelected.travelAreaIdQ = ti;
            }
            var liLists = $(this).parent().parent().children(".homepage_middle_pa").children("ul").find("li[class='travel_logo icon_choose']");
            for (var i = 0; i < liLists.length; i++) {
                navSelected.supplierParaQ.push($(liLists[i]).attr("data-id"));
            }
            sessionStorage.setItem("navSelected",JSON.stringify(navSelected));
            window.open(url,'_self');
        }else{//次首页  列表页进来的，做跳转，否则不做跳转
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
            freshInApplication();
            getScreenData();//获取筛选条件
            getListData();//获取列表数据
        }
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
                $(".top-suspend").children(".homepage_content_list.model-top").html("");
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
                }, 100);
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
                navSelected.tourOutInQ = id;
                getArea(id, "", true, false);
            }, 0);
        }else{
            clearTimeout(st);
        }
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
            navSelected.pageNo =1;
            var ids = $(obj).parent().parent().parent().parent().parent().children("dt").attr("id");
            str = start +"-"+end;
            switch(ids){
                case "fromAreaList":navSelected.startCityParaQ=[];navSelected.startCityParaQ.push(str);break;//出发城市
                case "targetCountry":navSelected.endCityParaQ=[];navSelected.endCityParaQ.push(str);break;//目的地
                case "targetCity":navSelected.targetCity=[];navSelected.targetCity.push(str);break;//抵达城市
                case "linePlay":navSelected.linePlay=[];navSelected.linePlay.push(str);break;//游玩线路
                case "travelDays":navSelected.dayParaQ=[];navSelected.dayParaQ.push(str);break;//行程天数
                case "priceRange":navSelected.priceParaQ=[];navSelected.priceParaQ.push(str);break;//价格区间
                case "remainingSeat":navSelected.freeParaQ=[];navSelected.freeParaQ.push(str);break;//余位
                case "startDate":navSelected.groupDateParaQ=[];navSelected.groupDateParaQ.push(str);break;//出团日期
                case  "supplierInfos":navSelected.supplierParaQ=[];navSelected.supplierParaQ.push(str);break;//供应商
            }
            sessionStorage.setItem("navSelected",JSON.stringify(navSelected));
            window.open(url,'_self');
        }
    }
    /*
     *搜索框绑定回车事件
     * */
    $("#keyword").keydown(function(e){

        var curKey = e.which;
        if(curKey == 13){
            rakeList();
        }
    });
})()
