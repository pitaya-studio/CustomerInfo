/**
 * Created by Administrator on 2016/11/21.
 */
$(function() {

    $("#tab span").on("click",function(){
        var tab = $(this).attr("id").slice(0,-3);
        changeTab(tab);//标签切换
        sessionStorage.setItem("nowSelect",tab);
        if(screenSelected){
            screenSelected.keywordQ=$("#keyword").val();
        }
        var screenSelected=window.screenSelectedT1;
        screenSelected.pageNo=1

        getListData();
    });
    //产品列表和团期列表切换 E

    //认证详情浮框鼠标hover出现移开隐藏 S
    $("#content").on("mouseover",['.pop-container'],[{"a":1}],function(event){
        // console.log(event.data[0].a);
        $(this).find(".pop_permission_show").show();
    }).on("mouseout",['.pop-container'],[{"a":1}],function(event){
        $(this).find(".pop_permission_show").hide();
    });
    //认证详情浮框鼠标hover出现移开隐藏 E

});

function setViewMode(){//设置默认查看方式
    var selected = sessionStorage.getItem("defaultSelect");
    var html="";
    html += '<div id="setViewMode">'+
        '<input name="setViewMode" type="radio" id="porductRadio"';
    if(selected =="productList"){
        html +='checked="checked"'
    }
    html +='>'+
        '<label for="porductRadio">产品列表</label>'+
        '<input id="groupRadio" name="setViewMode" type="radio"';
    if(selected =="groupList"){
        html +='checked="checked"'
    }
    html +='>'+
        '<label for="groupRadio">团期列表</label>'+
        '</div>';
    var $pop =  $.jBox(html, { title: "默认设置",
        buttons:{'取消':0,'确认': 1},
        submit:function(v, h, f){
            if (v=="1"){
                var data = "";
                var selected = $pop.find("input[type=radio]:checked").attr("id");
                if(selected == "porductRadio"){
                    data=1;
                }else{
                    data=0;
                }
                $.ajax({
                    type: "post",
                    url: ctx+"/t1/changeT1ListShowFlag",
                    data: {"t1ListFlag":data},
                    async: true,
                    success:function(){
                        var defaultSelect = "";
                        if(data==1){
                            defaultSelect = "productList";
                        }else{
                            defaultSelect = "groupList";
                        }
                        sessionStorage.setItem("defaultSelect",defaultSelect);//将选择的默认模式存入缓存
                        // location.reload();
                    }
                });
            }
        },
        height:200,
        width:300
    });
}

function changeTab(tab){//标签切换
    var tabId = tab +"Tab";
    $("div#tab span").removeClass("activeList");
    $("div#tab").find("#"+tabId).addClass("activeList");
}
