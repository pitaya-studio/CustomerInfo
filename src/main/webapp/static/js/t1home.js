function getEvent(){
    if(window.event){
        return window.event.srcElement;
    }
    func=getEvent.caller;
    while(func!=null){
        var arg0=func.arguments[0];
        if(arg0){
            if((arg0.constructor==Event || arg0.constructor ==MouseEvent
                || arg0.constructor==KeyboardEvent)
                ||(typeof(arg0)=="object" && arg0.preventDefault
                && arg0.stopPropagation)){
                return arg0.target;
            }
        }
        func=func.caller;
    }
    return null;
}

function getEvent1(){
    if(window.event)    {return window.event;}
    func=getEvent1.caller;
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

var spanWithDivId={
    "startCity":"limit_container_sc",
    "endCity":"limit_container_ec",
    "groupHomeSearch_down_border":"limit_container_su"
}
//function returenBack(obj){
//	var $this=$(obj);
//	$this.prev().css("display","inline-block")
//}
//function returenNone(obj){
//	var $this=$(obj);
//	$this.prev().css("display","none")
//}
$(function(){
    if (!Array.prototype.indexOf)
    {
        Array.prototype.indexOf = function(elt /*, from*/)
        {
            var len = this.length >>> 0;
            var from = Number(arguments[1]) || 0;
            from = (from < 0)
                ? Math.ceil(from)
                : Math.floor(from);
            if (from < 0)
                from += len;
            for (; from < len; from++)
            {
                if (from in this &&
                    this[from] === elt)
                    return from;
            }
            return -1;
        };
    }
    $(".return").hover(function(){
        $(this).prev().css("display","inline-block")
    },function(){
        $(this).prev().css("display","none")
    });
    $(".groupOrderChildrenTwo").hover(function(){
        $(this).find(".look").css("display","inline-block");
    },function(){
        $(this).find(".look").css("display","none")
    });
    $(".groupHomeOrderChildren").hover(function(){
        $(this).find(".homeLook").css("display","inline-block");
    },function(){
        $(this).find(".homeLook").css("display","none")
    });
    //详情弹窗展开项
    $(".ArrowRight").hover(function(){
        $(".POPUP").show();
    },function(){
        $(".POPUP").hide();
    });
    //$("input[type=checkbox]").bind("click",function(){
    //	document.getElementsByTagName("input[type=checkbox]").innerHTML=selectNew(this);
    //});
    $(".selectWidth label").on('click',function(i,n){
        if($(this).hasClass("selectSearchFalse")) {
            $(this).attr('class', 'selectSearchTrue');
        }else{
            $(this).attr('class', 'selectSearchFalse');
        }
    });
    //超出显示省略号（...）
    $(".title").each(function(){
        if($(this).text().length>36){
            var str = $(this).text().substr(0,36) + " ...";
            $(this).text(str);
        }
        if($(this).parent(".aheight").css("height")!="20px"){
            $(this).parent().parent(".groupChildren1").css("margin-bottom","-7px");
        }else{
            $(this).parent().parent(".groupChildren1").css("margin-bottom","-17px");
        }
    });
    $(".surplus").each(function(){
        if($(this).html()>10){
            $(this).siblings(".hot").css("display","none");
        }else{
            $(this).siblings(".hot").css("display","inline-block");
        }
    });
    $(".td_less").each(function(){
        if($(this).text().length>116){
            var str = $(this).text().substr(0,116) + " ...";
            $(this).text(str);
        }
    });

    /*$(".search_spare").click(function(){
     //$(this).parent().children(".search_spare").removeClass("groupHomeSearch_down_active");
     $(this).addClass("groupHomeSearch_down_active");
     })
     */
    //T1首页input
    $(".groupHomeSearch_down input").parent().hover(function(){
        $(this).addClass("ascertain_shadow");
        //$(this).children(".ascertain").show();
    },function(){
        $(this).removeClass("ascertain_shadow");
        //$(this).children(".ascertain").hide();
    })
 /*   /!*注册事件*!/
    if(document.addEventListener){
        document.addEventListener('DOMMouseScroll',scrollFunc,false);
    }
    //IE/Opera/Chrome
    window.onmousewheel=document.onmousewheel=scrollFunc;*/
});

//顶部定位
var nt = !1;
//判断滚动距离
function scrollFunc(){
    $(window).height();//是文档窗口高度
    $("div").offset().top//是标签距离顶部高度
    $(document).scrollTop();//是滚动条高度
    $("div").height();//是标签高度
    //你要的高度+$("div").height()+[$("div").offset().top-$(document).scrollTop()]=$(window).height();
    var st = $(document).scrollTop();//往下滚的高度
    nt = nt ? nt: $(".J_m_nav").offset().top;
    var sel=$(".J_m_nav");
    if (nt < st) {
        sel.addClass("nav_fixed");
    } else {
        sel.removeClass("nav_fixed");
    }
}
//显示更多筛选条件
function more_less(){
    if($(".more_less_div").is(":hidden")){
        $(".more_less_div").slideDown();
        $(".show_more").hide();
        $(".show_less").show();
        $("#groupHomeSearch_down_border").css("border-bottom","1px dashed #dddddd");
        nt =$(".J_m_nav").offset().top;
        nt_use=nt;
        var changePX=$('.show_less').offset().top-200;
        nt+=changePX;
    }else{
        $(".more_less_div").slideUp();
        $(".show_more").show();
        $(".show_less").hide();
        $("#groupHomeSearch_down_border").css("border-bottom","none");
        nt=nt_use;
    }
}
function getElementsByClassName(className, root, tagName) {    //root：父节点，tagName：该节点的标签名。 这两个参数均可有可无
    if (root) {
        root = typeof root == "string" ? document.getElementById(root) : root;
    } else {
        root = document.body;
    }
    tagName = tagName || "*";
    if (document.getElementsByClassName) {                    //如果浏览器支持getElementsByClassName，就直接的用
        return root.getElementsByClassName(className);
    } else {
        var tag = root.getElementsByTagName(tagName);    //获取指定元素
        var tagAll = [];                                    //用于存储符合条件的元素
        for (var i = 0; i < tag.length; i++) {                //遍历获得的元素
            for (var j = 0, n = tag[i].className.split(' ') ; j < n.length; j++) {    //遍历此元素中所有class的值，如果包含指定的类名，就赋值给tagnameAll
                if (n[j] == className) {
                    tagAll.push(tag[i]);
                    break;
                }
            }
        }
        return tagAll;
    }
}
//点击更多操作
function switchTip(obj){
    $(".main_container").hide();
    //清空历史勾选项
 /*   $(".main_content").find("li>em").each(function (){
        if(this.className.indexOf("selected_box") !=-1){
            this.className = "item_icon";
        }
    })*/
    //获取选中的，然后将其再多选框中选中
    var divId=$(obj).parent().attr("id");
    //var spanId=spanWithDivId.get(divId);
    var _selectFromRigt=[];
    $("#"+divId).find("p .groupHomeSearch_right_child").each(function(){
        $(this).text();
        _selectFromRigt.push($(this).text());
    });

    $("#"+divId).find("ul span").each(function() {
        if(_selectFromRigt.indexOf($(this).val()==-1)){
            $(this).prev().attr("class", "item_icon");
        }
    })

    for(var i=0;i<_selectFromRigt.length;i++){
        setUnchecked(divId,_selectFromRigt[i],"2");
    }



    var parentNode = obj.parentNode;
    var divTip = getElementsByClassName("main_container",parentNode)[0];
    var displayCss =  divTip.currentStyle ? divTip.currentStyle['display'] : document.defaultView.getComputedStyle(divTip,false)['display'];
    if(displayCss != 'none'){
        divTip.style.display = 'none';
    }else{
        divTip.style.display = 'inline-block';
    }

    $('.city_list').niceScroll({
        cursorcolor: "#ccc",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "5px", //像素光标的宽度
        cursorborder: "0", //     游标边框css定义
        cursorborderradius: "5px",//以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });

}

//关闭更多选择框
function closeTip(obj){
    var divTip = obj.parentNode.parentNode;
    if(divTip.className == 'main_container'||divTip.className.indexOf("main_container main_container_order")>-1){
        var displayCss =  divTip.currentStyle ? divTip.currentStyle['display'] : document.defaultView.getComputedStyle(divTip,false)['display'];
        if(displayCss != 'none'){
            divTip.style.display = 'none';
        }
    }
}

//模糊搜索出发地、目的地、供应商等
function fuzzySearch(obj){
    var inputValue = getElementsByClassName("se_input",obj.parentNode)[0].value;
    // var inputValue = obj.parentNode.getElementsByClassName("se_input")[0].value;
    var lis = obj.parentNode.parentNode.getElementsByTagName("li");
    for(var i=0;i<lis.length;i++){
        var spanValue = lis[i].getElementsByTagName("span")[0].innerHTML;
        if(spanValue.indexOf(inputValue) == -1){
            lis[i].style.display = 'none';
        }else{
            lis[i].style.display = 'block';
        }
    }
}

//切换出发地、目的地选中状态
function switchStatus(obj){
    if(obj.className.indexOf("selected_box") !=-1){
        obj.className = "item_icon";
    }else{
        obj.className = "item_icon  selected_box";
    }
}
//多选框的确定提交按钮操作第一步，先清空上次从多选框选择的
function clearFromRight(spanId,divId){
    var _selectFromRigt=[];
        $("#"+divId).find("p .groupHomeSearch_right_child").each(function(){
            $(this).text();
            _selectFromRigt.push($(this).text());
            $(this).remove();
        })
    $("#"+spanId).find("span").each(function(){
        if(_selectFromRigt.indexOf($(this).text())>-1){
            $(this).remove();
        }
    })
}
//点击确定按钮，获取出发地、目的地等 1:出发地、4：目的地、3：供应商 2:抵达城市
function  getEle(obj,flag){
    var lis = obj.parentNode.parentNode.getElementsByTagName("ul")[0].getElementsByTagName("li");
    //获取此搜索条件的常用条件
    var commonCon=[];
    var commonConP=$(obj).parent().parent().parent().find("p>span");
    $(obj).parent().parent().parent().find("p>span").each(function(){
        commonCon.push($(this).text());
    })
    var rightDivId=$(obj).parent().parent().parent().attr("id");
//此处加个判断，如果点击多选框的取消（二次勾中）时应先获得对应的筛选条件下DIV中新加的标签，然后与现状这次提交的做对比。如果以前存在，则去掉，如果没有则继续新加
    if(flag==1){
        clearFromRight("limit_container_sc","startCity")
    }
    if(flag==2){
        clearFromRight("limit_container_ec","endCity")
    }
    if(flag==3){
        clearFromRight("limit_container_su","groupHomeSearch_down_border")
    }
    if(flag==4){
        clearFromRight("limit_container_cy","country")
    }
    for(var i=0;i<lis.length;i++){
        var flg=true;
        //判断是否选中
        if(lis[i].getElementsByTagName("em")[0].className.indexOf("selected_box") != -1){
            var val = lis[i].getElementsByTagName("span")[0].innerHTML;
            var id = lis[i].getElementsByTagName("span")[0].id;

                var emphasis = document.createElement("em");
                emphasis.setAttribute('class','t1_2');
                var spanEle = document.createElement("span");
                spanEle.setAttribute('class','groupHomeSearch_right_child');
                spanEle.setAttribute('style','display:none');
                spanEle.innerHTML = val;
                spanEle.appendChild(emphasis);
                //spanEle.setAttribute('id',id);
                obj.parentNode.parentNode.parentNode.getElementsByTagName("p")[0].appendChild(spanEle);
            //}
            //把前面的“全部”的选中状态给移除了
            $(obj).parent().parent().parent().find("p>span :first").attr("class","search_spare");
            var p_container = document.getElementById("selected_condition");
            var spans = getElementsByClassName("groupHomeSearch_ml",p_container);
            //判断是否包含出发城市
            if(flag == 1) {
                var departure_city = false;
                var dc_index = 0;
                for (var j = 0; j < spans.length; j++) {
                    var content = getElementsByClassName("groupHomeSearch_left",spans[j])[0].innerHTML;
                    if (content.indexOf("出发城市") != -1) {
                        departure_city = true;
                        dc_index = j;
                        break;
                    }
                }
                //包含出发城市
                if (flg) {
                    if (departure_city) {
                        addInclude(spans[dc_index], val,id);
                    } else {
                        addNoInclude(p_container, '出发城市', val,id);
                    }
                }
                $("#limit_container_sc").children().each(function(){
                    if($(this).text()=="全部"){
                        $(this).remove();
                    }
                })
                dealDom("limit_container_sc");
            }else if(flag == 2){
                var departure_city = false;
                var dc_index = 0;
                for(var j=0;j<spans.length;j++){
                    var content = getElementsByClassName("groupHomeSearch_left",spans[j])[0].innerHTML;
                    if(content.indexOf("抵达城市") != -1){
                        departure_city = true;
                        dc_index = j;
                        break;
                    }
                }
                //包含出发城市
                if (flg) {
                    if (departure_city) {
                        addInclude(spans[dc_index], val, id);
                    } else {
                        addNoInclude(p_container, '抵达城市', val);
                    }
                }
                $("#limit_container_ec").children().each(function(){
                    if($(this).text()=="全部"){
                        $(this).remove();
                    }
                })
                dealDom("limit_container_ec");
            }else if(flag == 3){
                var departure_city = false;
                var dc_index = 0;
                for(var j=0;j<spans.length;j++){
                    var content = getElementsByClassName("groupHomeSearch_left",spans[j])[0].innerHTML;
                    if(content.indexOf("供应商") != -1){
                        departure_city = true;
                        dc_index = j;
                        break;
                    }
                }
                //包含出发城市
                if (flg) {
                    if (departure_city) {
                        addInclude(spans[dc_index], val, id);
                    } else {
                        addNoInclude(p_container, '供应商', val);
                    }
                }
                $("#limit_container_su").children().each(function(){
                    if($(this).text()=="全部"){
                        $(this).remove();
                    }
                })
                dealDom("limit_container_su");
            }else if(flag == 4){
                var departure_city = false;
                var dc_index = 0;
                for(var j=0;j<spans.length;j++){
                    var content = getElementsByClassName("groupHomeSearch_left",spans[j])[0].innerHTML;
                    if(content.indexOf("目的地") != -1){
                        departure_city = true;
                        dc_index = j;
                        break;
                    }
                }
                //包含出发城市
                if (flg) {
                    if (departure_city) {
                        addInclude(spans[dc_index], val, id);
                    } else {
                        addNoInclude(p_container, '目的地', val);
                    }
                }
                $("#limit_container_cy").children().each(function(){
                    if($(this).text()=="全部"){
                        $(this).remove();
                    }
                })
                dealDom("limit_container_cy");
            }
        }
    }
    //addLimit(flag);
    dealDom(rightDivId);
    closeTip(obj);
}

function addInclude(ele,val,id){
    //是否包含一个
    //为了兼容ie8
    // var spanlength = ele.getElementsByClassName("groupHomeSearch_right")[0].getElementsByClassName("limit_container")[0].getElementsByTagName("span");
    var _first=getElementsByClassName("groupHomeSearch_right",ele)[0];
    var _second=getElementsByClassName("limit_container",_first)[0];
    var spanlength=_second.getElementsByTagName("span");

    if(spanlength.length == 1){
        spanlength[0].className = 'groupHomeSearch_right_child';
    }
    var emele1 = document.createElement("em");
    emele1.setAttribute('class','t1_2');
    var spanele1 = document.createElement("span");
    spanele1.setAttribute('class','groupHomeSearch_right_child');
    spanele1.setAttribute('id',id);
    spanele1.innerHTML = val;
    spanele1.appendChild(emele1);
    // var List = ele.getElementsByClassName("groupHomeSearch_right")[0].getElementsByClassName("limit_container")[0];
    var _list1=getElementsByClassName("groupHomeSearch_right",ele)[0];
    var List=getElementsByClassName("limit_container",_list1)[0];
    List.appendChild(spanele1);
}

function addNoInclude(ele,val,selval,id){
    var emele1 = document.createElement("em");
    emele1.setAttribute('class','t1_2');
    var spanele1 = document.createElement("span");
    spanele1.setAttribute('class','');
    spanele1.innerHTML = selval;
    spanele1.appendChild(emele1);
    var spanele2 = document.createElement("span");
    spanele2.setAttribute('class','limit_container');
    spanele2.appendChild(spanele1);
    var emele2 = document.createElement("em");
    emele2.setAttribute('class','t1_2');
    var spanele3 = document.createElement("span");
    spanele3.setAttribute('class','groupHomeSearch_right');
    spanele3.appendChild(spanele2);
    spanele3.appendChild(emele2);
    var spanele4 = document.createElement("span");
    spanele4.setAttribute('class','groupHomeSearch_left');
    spanele4.innerHTML = val;
    var spanele5 = document.createElement("span");
    spanele5.setAttribute('class','groupHomeSearch_ml');
    spanele5.appendChild(spanele4);
    spanele5.appendChild(spanele3);
    var pl = del_ff(ele).childNodes.length-2;
    ele.insertBefore(spanele5,del_ff(ele).childNodes[pl]);

}

////验证只能输入数字
//var partten = /^[0-9]+$/;
//$(document).ready(function(){
//    $('input[name=sort]').keyup(function(){
//        if(!partten.test($(this).val())){
//            var a= $(this).val();
//            var b=a.replace(/[^0-9]+/gi,'');
//            $(this).val(b)
//        }
//    })
//});



function count_again(){
    $(".profit_count_one input").val("");
    $(".profit_count_one").show();
    $(".profit_count_two").hide();
}

//517开始
// $(function(){
//     $(".pop_permission").mouseenter(function(event){
//         $(".pop_permission_show").show();
//     }).mouseout()
//
//     $(".pop_permission").mouseleave(function(event){
//         //当鼠标移到下面详细信息上时，不关闭信息窗口
//         $(".pop_permission_show").hover(function(){
//             $(".pop_permission_show").show();
//         },function(){
//             $(".pop_permission_show").hide();
//         });
//         $(".pop_permission_show").hide();
//     });
//
// });
//517结束
var flag = true;
// function link_phone(ctx,groupId,obj,event) {
//
// 	$('.new_content_pop').niceScroll({
// 		cursorcolor: "#ccc",//#CC0071 光标颜色
// 		cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
// 		touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
// 		cursorwidth: "8px", //像素光标的宽度
// 		cursorborder: "0", //     游标边框css定义
// 		cursorborderradius: "8px",//以像素为光标边界半径
// 		autohidemode: false //是否隐藏滚动条
// 	});
//
//
// 	$(".new_content_pop").css("overflow", "visible");
// 	$(".new_content_pop").css("overflow", "hidden");
//
// 	// 添加询单记录
//     addAsk(ctx, groupId);
//
//     event = getEvent1();
//     if(flag == true){
//         $(".pos-popup").fadeIn();
//         $(".trangle-top,.travel-attach").fadeOut();
//         flag = false;
//         fadeFlag=true;
//     }else{
//         $(".pos-popup").fadeOut();
//         flag = true;
//     }
//
//     if(window.event){
//         event.cancelBubble=true;//阻止冒泡
//     }else{
//         event.stopPropagation();
//     }
//
//
//     $.ajax({
//         type:"post",
//         url:ctx+"/log/productT1/save",
//         data:{
//             groupId : groupId
//         }
//     });
// }
//联系供应商下单
/*var flag = true;
function link_phone(ctx,groupId,obj,event) {
	$('.new_content_pop').niceScroll({
        cursorcolor: "#ccc",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "8px", //像素光标的宽度
        cursorborder: "0", //     游标边框css定义
        cursorborderradius: "8px",//以像素为光标边界半径
        autohidemode: false //是否隐藏滚动条
    });

    $(".new_content_pop").css("overflow", "visible");
    $(".new_content_pop").css("overflow", "hidden");
    
    event = getEvent();
    if(flag == true){
        $(".pos-popup").fadeIn();
        $(".trangle-top,.travel-attach").fadeOut();
        flag = false;
        fadeFlag=true;
    }else{
        $(".pos-popup").fadeOut();
        flag = true;
    }
    if(window.event){
        event.cancelBubble=true;//阻止冒泡
    }else{
        event.stopPropagation();
    }

    $.ajax({
        type:"post",
        url:ctx+"/log/productT1/save",
        data:{
            groupId : groupId
        }
    });
    // 添加询单记录
    addAsk(ctx, groupId);
}*/

/*//联系供应商下单
var flag = true;
function link_phone(ctx,groupId,obj,event){
    //$(".link_phone_div").toggle();
    //$('.contact_phone').niceScroll({
    //    cursorcolor: "#ccc",//#CC0071 光标颜色
    //    cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
    //    touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
    //    cursorwidth: "5px", //像素光标的宽度
    //    cursorborder: "0", //     游标边框css定义
    //    cursorborderradius: "5px",//以像素为光标边界半径
    //    autohidemode: false //是否隐藏滚动条
    //});
    event = getEvent4Detail();
    if(flag == true){
        $(".pos-popup").fadeIn();
        $(".trangle-top,.travel-attach").fadeOut();
        flag = false;
        fadeFlag=true;
    }else{
        $(".pos-popup").fadeOut();
        flag = true;
    }
    if(window.event){
        event.cancelBubble=true;//阻止冒泡
    }else{
        event.stopPropagation();
    }

    // event.stopPropagation();
    $.ajax({
        type:"post",
        url:ctx+"/log/productT1/save",
        data:{
            groupId : groupId
        }
    });
    
    // 添加询单记录
    addAsk(ctx, groupId);
}*/

function stopPop(obj,event){
    event = getEvent4Detail();
    var target ;
    if(window.event){
        target = event.srcElement;
    }else{
        target = event.target;
    }
    if($(target) != $(obj).children(".link_phone")){
        if(window.event){
            event.cancelBubble=true;//阻止冒泡
        }else{
            event.stopPropagation();
        }
    }
}
function hidePopup(){
    // $(".pos-popup").hide();
}

function addAsk(ctx, groupId) {
	$.ajax({
        type : "post",
        url : ctx + "/orderProgressTracking/manage/save?dom=" + Math.random(),
        data : {
            groupId : groupId,
            orderType : 1
        }
    });
}

function dealDom(id){
    var idx="#"+id;
    var browser=navigator.appName;
    if(browser=="Microsoft Internet Explorer")
    {
        if($(idx).children().length>0){
            $(idx).parent().parent().attr("style","display: table");
            $(idx).parent().parent().attr("data-abc","nimei");
        }else{
            $(idx).parent().parent().attr("style","display: none");
        }
    }else{
        if($(idx).children().length>0){
            $(idx).parent().parent().show();
        }else{
            $(idx).parent().parent().hide();
        }
    }
    var lim1=$(".limit_container").children().length;
    if(lim1==0){
        $("#groupHomeSearch_one").hide();
        //所有的条件都删除之后呢，待选条件均为全部
        $(".groupHomeSearch_down_p").each(function(){
            $(this).children(":first").attr("class","groupHomeSearch_down_active search_spare");
        })
    }
    if(lim1>0){
        $("#groupHomeSearch_one").show();
    }
}

$(document).ready(function(){
    var lim1=$(".limit_container").children().length;
    if(lim1>0){
        $("#groupHomeSearch_one").show();
    }else{
        $("#groupHomeSearch_one").hide();
    }
    dealDom("limit_container_sc");
    dealDom("limit_container_cy");
    dealDom("limit_container_ec");
    dealDom("limit_container_su");
    dealDom("limit_container_date");
    dealDom("limit_container_day");
    dealDom("limit_container_price");
    dealDom("limit_container_seat");

    //一种类型 过滤条件的删除
    function deleteFilter(spanId,divId){
        //如果清空的时候只剩最后一个 则进行刷新操作，否则点击确定之后才会提交刷新（_count剩下的个数）
        var _count=0;
        $("#selected_condition").find("span.groupHomeSearch_ml").each(function(){
                if($(this).attr("style")=="display: table;"||$(this).attr("style")=="DISPLAY: table"){
                    _count+=1;
                }
            })
        if(_count>0){
            $("#"+divId).find("p>span").each(function(){
                if($(this).find("em").length>0){
                    $(this).remove();
                }
                $(this).attr("class","search_spare");
            })
            $("#"+divId).find("p>span :first").attr("class","search_spare groupHomeSearch_down_active");
            //清空所有被选中的
            $("#"+divId).find("ul em").each(function() {
                $(this).attr("class", "item_icon");
            })
        }else{
            //刷新
            $("#homeSearchingForm").submit();
        }

    }
    $(".t1_2.dif").bind("click",function(e){

        // var browser=navigator.appName;
        var whatFilter="";

        //bug t1首页点击删除时间不生效 Start
        var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1//判断是否IE浏览器
        var fIEVersion;

        if (isIE)
        {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            fIEVersion = parseFloat(RegExp["$1"]);
            if(fIEVersion <= 8) {
                whatFilter=getEvent().previousSibling.id;
            } else {
                whatFilter=getEvent().previousElementSibling.id;
            }//IE版本过低
        }else{
            whatFilter=getEvent().previousElementSibling.id;
        }
        //isIE end
        //bug t1首页点击删除时间不生效 End

        // if(browser=="Microsoft Internet Explorer")
        // {
        //     whatFilter=getEvent().previousElementSibling.id;
        // }else{
        //     whatFilter=getEvent().previousElementSibling.id;
        // }
        $("#"+whatFilter).empty();
        dealDom(whatFilter);
        switch (whatFilter){
            case "limit_container_sc":
                deleteFilter("limit_container_sc","startCity");
                break;
            case "limit_container_cy":
                deleteFilter("limit_container_cy","country");
                break;
            case "limit_container_ec":
                deleteFilter("limit_container_ec","endCity");
                break;
            case "limit_container_su":
                deleteFilter("limit_container_su","groupHomeSearch_down_border");
                break;
            case "limit_container_date":
                deleteFilter("limit_container_date","startDate");
                break;
            case "limit_container_day":
                deleteFilter("limit_container_day","tourDays");
                break;
            case "limit_container_price":
                deleteFilter("limit_container_price","priceRange");
                break;
            case "limit_container_seat":
                deleteFilter("limit_container_seat","remainSeat");
                break;
        }
    });
    //清除所有筛选条件  的 绑定事件
    $(".cleared_condition").bind("click",function(){
        $(".limit_container").empty();
        //获取所有的筛选条件栏
        $(".groupHomeSearch_down").find("p>span").each(function(){
            if($(this).text()=="全部"){
                $(this).attr("class","search_spare groupHomeSearch_down_active");
            }else{
                if($(this).find("em").length>0){
                    $(this).remove();
                }else{
                    $(this).attr("class","search_spare");
                }
            }
        });
        //清空所有多选框的选择项
        $(".groupHomeSearch_down").find("ul em").each(function() {
            $(this).attr("class", "item_icon");
        });
        $(".limit_container").each(function(){
            dealDom($(this).attr("id"));
        });
        /*dealDom("limit_container_sc");
         dealDom("limit_container_ec");
         dealDom("limit_container_su");*/
    });
    //$("#groupHomeSearch_one").hide();
    //绑定已选中条件的单个移除事件
    $(".limit_container").bind("click",function(e){
        var filterWhich= getEvent().parentNode.parentNode.id;
        var targetJQ=$(getEvent());
        switch (filterWhich){
            case "limit_container_sc":
                deleteFilterAlone("limit_container_sc","startCity",targetJQ);
                break;
            case "limit_container_cy":
                deleteFilterAlone("limit_container_cy","country",targetJQ);
                break;
            case "limit_container_ec":
                deleteFilterAlone("limit_container_ec","endCity",targetJQ);
                break;
            case "limit_container_day":
                deleteFilterAlone("limit_container_day","tourDays",targetJQ);
                break;
            case "limit_container_su":
                deleteFilterAlone("limit_container_su","groupHomeSearch_down_border",targetJQ);
                break;
            case "limit_container_date":
                deleteFilterAlone("limit_container_date","startDate",targetJQ);
                break;
            case "limit_container_price":
                deleteFilterAlone("limit_container_price","priceRange",targetJQ);
                break;
            case "limit_container_seat":
                deleteFilterAlone("limit_container_seat","remainSeat",targetJQ);
                break;
        }
    });
    //为输入框的确定按钮绑定事件
    $(".ascertain").bind("click",function(e){
        if($(this).text()=="确定") {
            var firstVal = $(this).prev().prev().val();
            var secondVal = $(this).prev().val();
            //首先清空当前行的前面的选中状态
            var groupHomeJQ = $(this).parent().parent();
            groupHomeJQ.find(".search_spare").each(function () {
                $(this).attr("class", "search_spare");
            });
            //然后删除上面以选中的 换成自己输入的
            var filterWhich = $(getEvent()).parent().parent().parent().attr("id");
            var targetJQ = $(getEvent());
            switch (filterWhich) {
                case "tourDays":
                    bindFilterFromI("tourDays", "limit_container_day", targetJQ);
                    break;
                case "startDate":
                    bindFilterFromI("startDate", "limit_container_date", targetJQ);
                    break;
                case "priceRange":
                    bindFilterFromI("priceRange", "limit_container_price", targetJQ);
                    break;
                case "remainSeat":
                    bindFilterFromI("remainSeat", "limit_container_seat", targetJQ);
                    break;
            }
            $(".groupHomeSearch_down_p").find("input").each(function () {
                $(this).val("");
            });
        }
    });
    //绑定添加点击事件
    $(".groupHomeSearch_down_p").bind("click",function(e){
        //查看是哪个筛选条件
        var conditionWhich=getEvent().parentNode.parentNode.id;
        //看看是不是X号
        var conditionWhichX=getEvent().parentNode.parentNode.parentNode.id;
        if(!conditionWhich){
            if(conditionWhichX){
                var targetJQ=$(getEvent());
                if(getEvent().nodeName=="EM"){
                    switch(conditionWhichX) {
                        case "startCity":
                            deleteFilterFromQ("limit_container_sc","startCity",targetJQ);
                            break;
                        case "country":
                            deleteFilterFromQ("limit_container_cy","country",targetJQ);
                            break;
                        case "endCity":
                            deleteFilterFromQ("limit_container_ec","endCity",targetJQ);
                            break;
                        case "groupHomeSearch_down_border":
                            deleteFilterFromQ("limit_container_su","groupHomeSearch_down_border",targetJQ);
                            break;
                    }
                }
            }
            return;
        }
        //获取被点击的事件
        var targetJQ=$(getEvent());
        switch (conditionWhich){
            case "startCity":
                getAll(targetJQ,"limit_container_sc",e);
                break;
            case "country":
                getAll(targetJQ,"limit_container_cy",e);
                break;
            case "endCity":
                getAll(targetJQ,"limit_container_ec",e);
                break;
            case "groupHomeSearch_down_border":
                getAll(targetJQ,"limit_container_su",e);
                break;
            case "tourDays":
                getAll(targetJQ,"limit_container_day",e);
                break;
            case "startDate":
                getAll(targetJQ,"limit_container_date",e);
                break;
            case "priceRange":
                getAll(targetJQ,"limit_container_price",e);
                break;
            case "remainSeat":
                getAll(targetJQ,"limit_container_seat",e);
                break;

        }
        //addLimit();
    })
});

//点击全部的时候进行的操作
function getAll(targetJQ,id,e){

    var DXcondition=["limit_container_date","limit_container_price","limit_container_seat"];
    var radioOrBox=DXcondition.indexOf(id)>-1;
    //点击全部
    if(targetJQ[0].innerHTML=="全部"){
        //if(radioOrBox){
        if(false){
            $("#"+id).empty();
            $(getEvent()).parent().children().each(function(){
                $(this).attr("class","search_spare");
                //如果是从多选框添加的
            })
        }else{
            $(getEvent()).parent().children().each(function(){
                if($(this).find("em").length>0){
                    $(this).remove();
                }else{
                    $(this).attr("class","search_spare");
                }
                //如果是从多选框添加的
            })
            $("#"+id).empty();
            $("#"+id).append('<span class="groupHomeSearch_right_child">'+targetJQ[0].innerHTML +'<em class="t1_2"></em></span>');
        }
        targetJQ.addClass("groupHomeSearch_down_active");

    }else{
        if(targetJQ.hasClass("groupHomeSearch_down_active")||targetJQ.hasClass("groupHomeSearch_right_child")){
            //如果点击的选择框选择的数据附带的X号，那么执行删除操作
        }else{
            //确保选中的是值而不是外标签
            //var DXcondition=["limit_container_date","limit_container_day","limit_container_price","limit_container_seat"];

            if(targetJQ.is(".search_spare")&&!targetJQ.is(".ascertain_shadow") ){

                if(radioOrBox){
                    $("#"+id).empty();
                    targetJQ.parent().children("span").each(function(){
                        $(this).attr("class","search_spare");
                    })
                    targetJQ.parent().children("span").last().attr("class","");
                }
                //行程天数 基础条件和输入条件互斥
                if(id=="limit_container_day"){
                    $("#"+id).find("span").each(function(){
                        if($(this).is(".non_class")){
                            $(this).remove();
                        }
                    })
                }

                //点击单个的
                targetJQ.addClass("groupHomeSearch_down_active");
                $(getEvent()).parent().children(":first").removeClass("groupHomeSearch_down_active");
                //如果已选择项里面有全部，则把全部去掉
                $("#"+id).children().each(function(){
                    if($(this).text()=="全部"){
                        $(this).remove();
                    }
                })
                if(radioOrBox){
                    $("#"+id).append('<span class="groupHomeSearch_right_child" id="'+targetJQ[0].id+'">'+targetJQ[0].innerHTML +'</span>');
                }else{
                    $("#"+id).append('<span class="groupHomeSearch_right_child" id="'+targetJQ[0].id+'">'+targetJQ[0].innerHTML +'<em class="t1_2"></em></span>');
                }

            }
        }

    }
    dealDom(id);
}

/**
 * 将多选框的勾选状态(1)去掉、(2)选中
 */
function setUnchecked(divId,objValue,type){

    $("#"+divId).find("ul span").each(function(){
        if($(this).text()==objValue){
            if(type=="1"){
                $(this).prev().first().attr("class","item_icon");
            }else if(type=="2"){
                $(this).prev().first().attr("class","item_icon  selected_box");
            }
        }
    })
}
/**
 * 上面的已选择条件单个删除的时候进行的操作
 */
function deleteFilterAlone(spanId,divId,targetJQ){
    var val=targetJQ.parent().text();
    targetJQ.parent().remove();






    //同时移除下面的选定状态
    $("#"+divId).find("p>span").each(function(){
        if($(this).text()==val){
            //如果是从多选框中选择的
            if($(this).find("em").length>0){
                $(this).remove();
                //并且多选框的勾选状态去掉
                setUnchecked(divId,val,"1");
            }else{
                $(this).attr("class","search_spare");
            }
        }
    })
    dealDom(spanId);
    var _flag=$("#"+spanId).children('span').length>0;
    if(_flag){
        //doNothing;
    }else{
        var _count=0;
        $("#selected_condition").find("span.groupHomeSearch_ml").each(function(){
            if($(this).attr("style")=="display: table;"){
                _count+=1;
            }
        })
        if(_count>0){
            $("#"+divId).find("p>span").each(function(){
                if($(this).find("em").length>0){
                    $(this).remove();
                }
                $(this).attr("class","search_spare");
            })
            $("#"+divId).find("p>span :first").attr("class","search_spare groupHomeSearch_down_active");
            //清空所有被选中的
            $("#"+divId).find("ul em").each(function() {
                $(this).attr("class", "item_icon");
            })
        }else{
            //刷新
            $("#homeSearchingForm").submit();
        }
    }



}

//从多选框中选择的查询条件的x删除
function deleteFilterFromQ(spanId,divId,targetJQ){
    var val=targetJQ.parent().text();
    targetJQ.parent().remove();
    setUnchecked(divId,val,"1");
    //同时移除上面的选中的过滤条件
    $("#"+spanId).find("span").each(function(){
        if($(this).text()==val){
            $(this).remove();
        }
    });
    dealDom(spanId);
}

//输入框的值提交
function bindFilterFromI(divId,spanId,targetJQ){
    var firstVal=targetJQ.prev().prev().val();
    var secondeVal=targetJQ.prev().val();
    /*	if(secondeVal<firstVal){
     alert("数值区间有误，请重新输入");
     }*/
    if(firstVal||secondeVal){
        //先判断下该条件上面是否有过滤条件，如果有，清空，如果没有，要显示出来啊
        $("#"+spanId).empty();
        if(spanId=="limit_container_seat"||spanId=="limit_container_date"){
            $("#"+spanId).append('<span class="groupHomeSearch_right_child">'+firstVal+'-'+secondeVal+'</span>');
        }
        if(spanId=="limit_container_day"){
            if(!firstVal){
                firstVal=0;
            }
            if(!secondeVal){
                secondeVal=""
            }
            $("#"+spanId).append('<span class="groupHomeSearch_right_child  non_class">'+firstVal+'天-'+secondeVal+'天<em class="t1_2"></em></span>');
        }
        if(spanId=="limit_container_price"){
            $("#"+spanId).append('<span class="groupHomeSearch_right_child">'+firstVal+'元-'+secondeVal+'元</span>');
        }

        dealDom(spanId);
    }

}

//点击空白处弹框消失
$(document).click(function(e){
    var _con = $('.userCenterList,.header_child_div');   // 设置目标区域
    var _con2=$(".search_more,.main_container,.provider_a");
    if(!_con.is(getEvent()) && _con.has(getEvent()).length === 0){
        $('.userCenterList').animate({
                height:"0"
            }
        );
        $('.userCenterList').removeClass("expended");
        $(".user_management").css("background-position","-100px -129px")
    }
    if(!_con2.is(getEvent()) && _con2.has(getEvent()).length === 0){
        $(".main_container").hide();
    }
});

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
//个人中心展开end

function close_parent(obj){
    $(obj).parent().hide();
}

//切换出境游和国内游tab页
function changeStatus(obj,type){
    $(obj).parent().children().each(function(){
        $(this).removeClass("active");
    });
    //$(obj).addClass("active");
    $("#pageNo").val(1);
    $("#keyword").val("");
    $("#keywordHidden").val("");
    $("#type").val(type);
    $("#homeSearchingForm").submit();
}

/*显示现在行程单*/
var fadeFlag = true;
function  showDownLoad(docIds,obj,event){
        event = getEvent4Detail();
        var target ;
        if(window.event){
            target = event.srcElement;
        }else{
            target = event.target;
        }
        if(target != obj){
            fadeFlag = true;
        }
        if(fadeFlag || fadeFlag == true){
            if(null==docIds || ''==docIds || 'undefined'==docIds) { //判断文档id是否为空
                $(".travel-attach").html("暂无行程单附件！");
            }
            $(".trangle-top,.travel-attach").fadeIn();
            // $(".pos-popup").fadeOut();
            fadeFlag=false;
            flag = true;
        }else{
            $(".trangle-top,.travel-attach").fadeOut();
            fadeFlag=true;
        }
        if(window.event){
            event.cancelBubble=true;//阻止冒泡
        }else{
            event.stopPropagation();
        }

}

/*点击空白处隐藏悬浮窗*/
$(document).click(function(){
    $(".trangle-top,.travel-attach").fadeOut();
    // $(".pos-popup").fadeOut();
    fadeFlag=true;
    flag = true;
});

function getEvent4Detail(){
    if(window.event)    {return window.event;}
    func=getEvent4Detail.caller;
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


var $pop;
var productDetailTitle="";
function details(activityId,groupCode,ctx,ctxStatic){
    // var para = {"travelSelected":JSON.stringify(travelSelected),"tabText":tabText,"pageDisplay":"productList"};
    var param = JSON.parse(sessionStorage.param);
    param.pageDisplay="productDetail";
    sessionStorage.param =JSON.stringify(param);
    var url = ctx+'/t1/jumpParam';
    // $("body").append('<script type="text/javascript" sr="'+ctxStatic+'/js/t1/t1ProAndGroupDetail.js"></script>');
    var oHead = document.getElementsByTagName('HEAD').item(0);
    //modify by wlj at 2016.11.24 for huiteng-start
    var title;
    var domain=window.location.host;
    // if(domain.indexOf("huitengguoji")==-1){
    if(domain.indexOf("huitengguoji.com")==-1){
        title="产品详情 &nbsp;&nbsp;|&nbsp;&nbsp; <span class='title_tiny'>在操作过程中遇到问题请联系: 010-85718666</span>";
    }else{
        title="产品详情";
    }
    //modify by wlj at 2016.11.24 for huiteng -end
    var oScript= document.createElement("script");

    oScript.type = "text/javascript";

    oScript.src=ctxStatic+"/js/t1/t1ProAndGroupDetail.js";

    oHead.appendChild( oScript);






    window.open(url,'_self');
   /* var iframe = "iframe:"+ctx+"/activity/manager/viewDetail/" + activityId + "/" + encodeURI(groupCode);
    $pop=$.jBox(iframe, {
        title: title,
        width: 880,
        height: 630,
        persistent: true,
        buttons:false,
        loaded:function(){
            window.productDetailTitle=$pop.find(".jbox-title").html()
            if($(window).height()<800){
                $("#jbox", window.parent.document).css("top","0");
            }
        }
    });*/

}
function return_count(){
    $("#order").hide();
    $(".profit_count_two").show();
    $(".order_bounced_body").getNiceScroll().resize();
}

/**
 * 显示名片 图集
 *
 * imgId 该函数的第一个参数，别被名字误解了
 * modify by wlj at 2016/11/3 for 气人
 */
function showName(imgId){
    if(arguments[1]&&arguments[1]=="forSaleOrder"){
        var parentDivId=arguments[2];
        $("#"+parentDivId+" .forSaleOrder").find("img").each(function(){
            if($(this).attr("data-imgId")&&$(this).attr("data-imgId")==imgId){
                $(this).trigger("click");
            }
        })
    }else{
        $("."+imgId).eq(0).find("img").eq(0).trigger("click");
    }
}
