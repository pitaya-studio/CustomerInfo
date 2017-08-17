/**
 * Created by wanglijun on 2016/9/22.
 */
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
var ctx;
var t1img;
var travelSelected={
    travelArea:[],//区域id（东南亚等）
    travelInOut:[],//国内和出境id
    travelId:[]//供应商id
};
$(function(){
    //获取页面根路径
    ctx = $("#ctx").val();
    t1img = $("#t1img").val();
    //ajax获取logo相关信息
    getT1LogoList("","",true);
});
//获取页面供应商显示信息
function getT1LogoList(tourOutIn,tourDistrictId,flag) {
    var params = {"tourOutIn": tourOutIn,"tourDistrictId": tourDistrictId,"flag":flag};
    $.ajax({
        type: "post",
        url: ctx+"/t1/getT1LogoList",
        data: params,
        async: true,
        beforeSend: function(){
           showWaiting();
        },
        complete: function () {
            hideWaiting();
            $(".homepage_content").css('min-height','auto');
        },
        dataType: 'json',
        success: function (data) {
                var  data = data.results;
                var innerHtml = "";
                //查询结果
                var logoResultList = data.tourDistrict;//供应商logo
                var tourOutInList = data.tourOutInList;//出境游和国内游
                // 暂时策略，待删除
                var lingxianwangshuai = data.lingxianwangshuai;
                $("#out").text(tourOutInList[0].name).attr('data-id',tourOutInList[0].id);
               $("#home").text(tourOutInList[1].name).attr('data-id',tourOutInList[1].id);
                if(tourOutInList[0].id == data.tourOutIn){
                    $("#out").addClass("active");
                }else{
                    $("#in").addClass("active");
                }
                for (var i = 0; i < logoResultList.length; i++) {
                    var _logoDetail = logoResultList[i];
                    var tourDistrictName = _logoDetail.tourDistrictName;//区域名字
                    var tourDistrictId = _logoDetail.tourDistrictId;//区域Id
                    innerHtml += ' <div class="homepage_content_list ">' ;
                    innerHtml+= ' <div class="homepage_left"><span  data-name="'+tourDistrictName+'" onclick="getInToT1Index(this,1)" class="tarvel_Area" data-id="' + tourDistrictId + '">' + tourDistrictName + '<i class="fa fa-chevron-right"></i></span></div>' +
                        '<div class="homepage_middle_pa"><ul class="homepage_middle">';
                    var travelAgency = _logoDetail.travelAgency;
                    for (var j = 0, k = travelAgency.length; j < k; j++) {
                    	// 暂时策略，待删除
                    	if (lingxianwangshuai == 1 && travelAgency[j].id == 426) {
                    		continue;
                    	} else if (lingxianwangshuai == 0 && travelAgency[j].id == 451) {
                            continue;
                        }
                        if(travelAgency[j].path){
                            innerHtml += '<li onclick="getInToT1Index(this,2)"  class="travel_logo" data-id="' + travelAgency[j].id + '"><a title="' + travelAgency[j].title + '" ><img src="'+ctx+'/person/info/getLogo?id='+travelAgency[j].logoUrl+'">&nbsp;' + travelAgency[j].title + '</a><span class="icon_chose_check"><i class="fa fa-check"></i></span></li>';
                        }else{
                            innerHtml += '<li onclick="getInToT1Index(this,2)"  class="travel_logo" data-id="' + travelAgency[j].id + '"><a title="' + travelAgency[j].title + '" >&nbsp;' + travelAgency[j].title + '</a><span class="icon_chose_check"><i class="fa fa-check"></i></span></li>';
                        }
                    }
                    var selectMore = "+ 多选";
                    innerHtml += '</ul></div><div class="homepage_right"> <a class="choose_more">' + selectMore + '</a>' +
                        '<a class="more_icon_show">更多 <i class="fa fa-angle-down"></i></a></div>' +
                        '<div class="homepage_list_bottom">' +
                        '<span  onclick="getInToT1Index(this,3)"  class="ensure unable">确定</span>' +
                        '<span class="cancel">取消</span>' +
                        '</div>' +
                        '</div>';
                }
                $(".homepage_content").empty().append(innerHtml);
                init();//初始化页面操作
        }
    });
}
function showWaiting(){
    $("#waitingImg").show();
}
function hideWaiting(){
    $("#waitingImg").hide();
}
/*点击quauq图标返回首页*/
function reload() {
    window.location.href = ctx+"/t1/newHome";
}
/*初始化页面操作*/
function init (){
    //点击空白处弹框消失
    $(document).click(function(e){
        var _con = $('.userCenterList,.header_child_div');   // 设置目标区域
        var _con2=$(".search_more,.main_container,.provider_a");
        if(!_con.is(getEventSrc()) && _con.has(getEventSrc()).length === 0){
            $('.userCenterList').animate({
                    height:"0"
                }
            );
            $('.userCenterList').removeClass("expended");
            $(".user_management").css("background-position","-100px -129px")
        }
        if(!_con2.is(getEventSrc()) && _con2.has(getEventSrc()).length === 0){
            $(".main_container").hide();
        }
    });
    //是否显示多选
    $(".homepage_middle").each(function(){
        var  lil = $(this).children().length;
        if(lil<=0 || lil == "undefined"){
            $(this).parent().parent().children(".homepage_right").children(".choose_more").hide();
        }
    });


    //判断更多是否显示
    $(".homepage_middle").each(function() {
        var $li_num = $(this).find("li");
        if ($li_num.length<=12) {
            $(this).parent().parent().find(".more_icon_show").hide();
        }
    });
    //点击更多展开图标,并把更多内容修改为收起
    $(".more_icon_show").click(function () {
        var $middle = $(this).parent().parent().find(".homepage_middle");
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
    //点击多选展开图标和选择框
    $(".choose_more").click(function () {
        var $par = $(this).parent().parent();
        //清除跳转函数，点击确定才可跳转
        $par.children(".homepage_middle_pa").children("ul").children("li").attr("onclick",'');
        var $middle = $par.parent().find("div.select_model").find(".homepage_middle");
        $par.siblings().removeClass("select_model");
        $par.addClass("select_model");
        //将历史选中区域下面选中的li标签清除选中
        $middle.find("li").removeClass("icon_choose");
    });
    //点击取消收起图标和选择框
    $(".cancel").click(function () {
        var $middle = $(this).parent().parent();
        //添加单个供应商点击事件
        $middle.children(".homepage_middle_pa").children("ul").children("li").attr("onclick",'getInToT1Index(this,2)');
        $(this).parent().parent().removeClass("select_model");
        $middle.css("overflow-y","hidden");
        $(".choose_more").show();
        $(".homepage_middle").scrollTop(0);

        $middle.find("li").removeClass("icon_choose");
        //.bind("click",everyLogoSelected)
        $(this).parent().children("span").first().addClass("unable");
    })
    //点击选中图标
    $(".homepage_middle a").click(function () {
        var $judge = $(this).parent().parent().parent().parent();
        if ($judge.hasClass("select_model")) {//是否多选
            var $par_li = $(this).parent();
            if ($par_li.hasClass("icon_choose")) {
                $par_li.removeClass("icon_choose");
            } else {
                $par_li.addClass("icon_choose");
                /*var areaId = $judge.children("div").first().children("span").attr('data-id');
                var area = {id:areaId,name:$judge.children("div").first().children("span").attr('data-name')};
                travelSelected.travelArea = [];
                travelSelected.travelArea.push(area);
                var travelIds = {id:$(this).parent().attr("data-id"),name:$(this).attr("title")}
                travelSelected.travelId.push(travelIds);*/
            }
            if ($(".icon_choose").length > 0) {
                $($judge).find(".ensure").removeClass("unable");
            } else {
                $($judge).find(".ensure").addClass("unable");
            }
        }else{
            var areaId = $judge.children("div").first().children("span").attr('data-id');
            var area = {id:areaId,name:$judge.children("div").first().children("span").attr('data-name')};
            travelSelected.travelArea = [];
            travelSelected.travelArea.push(area);
            travelSelected.travelId = [];
            var travelIds = {id:$(this).parent().attr("data-id"),name:$(this).attr("title")}
            travelSelected.travelId.push(travelIds);
        }
    });
}
/*
 *搜索框绑定回车事件
 * */
$("#keywords").keydown(function(e){
    var curKey = e.which;
    if(curKey == 13 ){
        getInToT1Index(this,4);
        return false;
    }
});
/*
 * index:1点击旅游区域跳转，2：点击单个供应商跳转，3：多选供应商点击确定按钮跳转,4:从搜索框跳转至列表页
 * */
function getInToT1Index(obj,index){
    var tabText = "";
    if(index == 1){
        var area = {id:$(obj).attr("data-id"),name:$(obj).attr("data-name")};
        travelSelected.travelArea.push(area);
    }else if(index == 4){
        tabText = $("#keywords").val();
        if(tabText == "" || tabText == null ){
            return false;
        }
        travelSelected.travelId=[];
        travelSelected.travelArea=[];
        travelSelected.travelInOut=[];
    }else if(index == 3){
        if($(obj).hasClass("unable")){
            return false;
        }
        var selectedApp = $(obj).parent().parent().children(".homepage_middle_pa").find("li[class='travel_logo icon_choose']");
        var $judge = $(obj).parent().parent();
        var areaId = $judge.children("div").first().children("span").attr('data-id');
        var area = {id:areaId,name:$judge.children("div").first().children("span").attr('data-name')};
        travelSelected.travelArea = [];
        travelSelected.travelArea.push(area);
        for(var i=0;i<selectedApp.length;i++){
            var travelIds = {id:$(selectedApp[i]).attr("data-id"),name:$(selectedApp[i]).children("a").attr("title")}
            travelSelected.travelId.push(travelIds);
        }
    }
    var url = ctx+'/t1/jumpParam';
    // var urlMiddle = ctx+'/t1/jumpParamMiddle';
    var para = {"jumpType":index,"travelSelected":JSON.stringify(travelSelected),"tabText":tabText,"pageDisplay":"productList"};
    sessionStorage.param = JSON.stringify(para);
    window.open(url,'_self');
}
/**
 * 获取不同浏览器下的event对象
 */
function getCommonEvent(){
    if(window.event)    {return window.event;}
    func=getE.caller;
    while(func!=null){
        var arg0=func.arguments[0];
        if(arg0){
            if((arg0.constructor==Event || arg0.constructor ==MouseEvent
                || arg0.constructor==KeyboardEvent)
                ||(typeof(arg0)=="object" && arg0.preventDefault
                && arg0.stopPropagation)){
                return arg0;
            }
        }
        func=func.caller;
    }
    return null;
}
/**
 * 获取不同浏览器下，event对象的元素
 */
function getEventSrc(){
    var event= getCommonEvent();
    var srcEle="";
    if(window.event){
        srcEle=event.srcElement;
    }else{
        srcEle=event.target;
    }
    return srcEle;
}
//切换tab页（出境游和国内游）
function getLogo(obj){
    var travelId=$(obj).attr('data-id');
    var inout = {id:travelId,name:$(obj).text()};
    travelSelected.travelInOut = [];
    travelSelected.travelInOut.push(inout);
    $(obj).siblings().removeClass("active");
    $(obj).addClass("active");
    getT1LogoList(travelId,"",true);
}



