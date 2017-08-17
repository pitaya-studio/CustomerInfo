/**
 * Created by Administrator on 2016/11/21.
 */
$(function() {
    //产品列表和团期列表切换 S
    $("#tab span").on("click",function(){
        if($(this).hasClass("productListTab")){
            $("#productListDiv").show();
            $("#groupListDiv").hide();
            $(this).addClass("activeList").siblings().removeClass("activeList");

        }else if($(this).hasClass("groupListTab")){
            $("#productListDiv").hide();
            $("#groupListDiv").show();
            $(this).addClass("activeList").siblings().removeClass("activeList");
            getListData();
        }
    });
    //产品列表和团期列表切换 E

    //认证详情浮框鼠标hover出现移开隐藏 S
    $(".pop-container").mouseover(function(){
        $(this).find(".pop_permission_show").show();
    }).mouseout(function(){
        $(this).find(".pop_permission_show").hide();
    });
    //认证详情浮框鼠标hover出现移开隐藏 E


});

function setViewMode(){
    var html="";
    html += '<div id="setViewMode">'+
        '<input name="setViewMode" checked="checked" type="radio" id="porductRadio">'+
        '<label for="porductRadio">产品列表</label>'+
        '<input id="groupRadio" name="setViewMode" type="radio">'+
        '<label for="groupRadio">团期列表</label>'+
        '</div>';
    $.jBox(html, { title: "默认设置",
        buttons:{'取消':0,'确认': 1},
        submit:function(v, h, f){
        if (v=="0"){

                }
        },
        height:200,
        width:300
    });

}
