/**
 * Created by wanglijun on 2016/10/20.
 * note：不做业务逻辑控制，提供面包屑功能、基本兼容、以及公共方法
 */
Function.prototype.method=function (name,func) {
    if(!this.prototype[name]){
        this.prototype[name]=func;
    }
    return this;
};

Array.method("indexOf",function(elt){
    var len=this.length >>> 0;
    var from=Number(arguments[1])||0;
    from=(from<0)?Math.ceil(from):Math.floor(from);
    if(from<0){
        from+=len;
    }
    for(;from<len;from++){
        if(from in this && this[from]===elt ){
            return from;
        }
    }
    return -1;
});
/**
 * 面包屑，返回首页
 */
function goHomePage(ctx){
    //modify by wlj at 2016.11.24 for huiteng-start
    var domain=window.location.host;
    if(domain.indexOf("huitengguoji.com")!=-1||domain.indexOf("travel.jsjbt")!=-1) {
    // if(domain.indexOf("huitengguoji")==-1) {
        location.href = ctx + "/t1/jumpParam";
    }else{
        location.href = ctx + "/t1/newHome";
    }
    //modify by wlj at 2016.11.24 for huiteng-end
}
/**
 * 获取不同浏览器下的event对象
 */
function getCommonEvent(){
    if(window.event)    {return window.event;}
    func=getCommonEvent.caller;
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
var getEventSrc=(function (){
    if(window.event){
        return function(){
            var event= getCommonEvent();
            return event.srcElement;
        }
    }else{
        return function(){
            var event= getCommonEvent();
            return event.target;
        }
    }
})();
/**
 * 取消时间冒泡行为
 */
function stopBubble(){
    if(e&&e.stopPropagation()){
        e.stopPropagation();
    }else{
        window.event.cancelBubble=true;
    }
}
/**
 * 阻止事件的默认行为
 */
function stopDefault(){
    if(e&&e.stopPropagation()){
        e.preventDefault();
    }else{
        window.event.returnValue=false;
    }
}
/**
 * 排序的升序或者降序
 *可共用 保持html结构即可
 */
function downOrUp(fun){
    $(".downOrUp").bind("click",function(e){
        var targetJQ = $(getEventSrc());
        //判断是span标签
        if(getEventSrc().nodeName=="SPAN"){
            targetJQ.find("em").each(function(){
                if(this.className.indexOf("rank_up_checked")>-1){
                    //说明此次点击降续
                    $("#downOrUp").find("i>em").removeClass("rank_down_checked rank_up_checked");
                    targetJQ.find("em :last").addClass("rank_down_checked");
                    returnStr=targetJQ.attr("data-id")+" DESC";
                    //调用后台
                    fun.call(this,returnStr);
                    return false;
                }
                if(this.className.indexOf("rank_down_checked")>-1||(this.className.indexOf("rank_up_checked")<0&&this.className.indexOf("rank_down_checked")<0)){
                    //说明此次点击升续
                    $("#downOrUp").find("i>em").removeClass("rank_down_checked rank_up_checked");
                    targetJQ.find("em :first").addClass("rank_up_checked");
                    returnStr=targetJQ.attr("data-id")+" ASC";
                    fun.call(this,returnStr);
                    return false;
                }
            })
        }
        if(getEventSrc().nodeName=="EM"){
            if(targetJQ[0].className.indexOf("rank_down")>-1){
                //说明此次点击降续
                $("#downOrUp").find("i>em").removeClass("rank_down_checked rank_up_checked");
                targetJQ.addClass("rank_down_checked");
                returnStr=targetJQ.parent().parent().attr("data-id")+" DESC";
                fun.call(this,returnStr);
                return false;
            }
            if(targetJQ[0].className.indexOf("rank_up")>-1){
                //说明此次点击升续
                $("#downOrUp").find("i>em").removeClass("rank_down_checked rank_up_checked");
                targetJQ.addClass("rank_up_checked");
                returnStr=targetJQ.parent().parent().attr("data-id")+" ASC";
                fun.call(this,returnStr);
                return false;
            }

        }
        //此处添加调用后台的ajax //貌似还得传递其他参数，比如标签页
    })
}
/**
 * 产品详情
 * @param productId 产品Id
 * @param groupCode 团期Id
 * @param ctx   项目路径
 *
 */
function details(productId,groupCode,ctx,ctxStatic){
    var param = sessionStorage.param ?JSON.parse(sessionStorage.param):{};
    param.productId=productId;
    // param["inOutArea"]=$("#reginType").text();
    sessionStorage.param =JSON.stringify(param);
    var url = ctx+'/t1/proGroupDetail';
    var searchDetail={"activityId":productId,"groupId":groupCode}
    sessionStorage.setItem("searchDetail",JSON.stringify(searchDetail));
    window.open(url,'_blank');
}
/**
 * 已下架的提示
 */
function details_jbox(){
    jBox.tip("该团期已下架", 'info');
}
/**
 * 设置ajax全局事件
 * 解决session过期问题
 */

/*
(function setAllAjax(){
    var ctx=$("#ctx_T2Login").val();
    $.ajaxSetup({
        dataFilter:function(data,type){
                if(type=='script'){
                    return data;
                }
                if(type==undefined){//针对返回格式非标准json形式的
                    return data;
                }
                var _returnData=data==""?{}:JSON.parse(data);
                if(_returnData.responseType=='null'&&_returnData.responseCode=='authentication'){
                    var url = ctx+'/t1/login';
                    window.location=url;
                    return {};
                }
            return arguments[0];
        }
    });
})();*/
