/**
 * Created by ymx on 2017/1/22.
 */

var   inputDetail={
    "pageNo":"",
    "pageSize":""
};

$(function(){

    // var tabNum = sessionStorage.getItem("tabNum")||0;
    // $(".nav-tabs li").eq(tabNum).siblings().removeClass("active");
    // $(".nav-tabs li").eq(tabNum).addClass("active");

    //从后台获取值
    getListData();


    $(".unbind_reason_content").niceScroll({
        cursorcolor: "#c9c9c9", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "6px", // 滚动条的宽度，单位：便素
        cursorborder: "none", // CSS方式定义滚动条边框
        cursorborderradius: "3px", // 滚动条圆角（像素）zindex: "auto" | <number>, // 改变滚动条的DIV的z-index值
        iframeautoresize: true, // 在加载事件时自动重置iframe大小
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        preservenativescrolling: true, // 你可以用鼠标滚动可滚动区域的滚动条和增加鼠标滚轮事件
        railoffset: false, // 可以使用top/left来修正位置
        spacebarenabled: true, // 当按下空格时使页面向下滚动
        sensitiverail: true, // 单击轨道产生滚动
        hidecursordelay: 3000, // 设置滚动条淡出的延迟时间（毫秒）
        cursordragspeed: 0.3, // 设置拖拽的速度
        preventmultitouchscrolling: true // 防止多触点事件引发滚动
    });

    searchWechat();
});


function getListData() {
    // console.log($(".nav-tabs li.active").index());
}

function switchConnectTab(num) {
    var $this = $($(".nav-tabs li")[num]);
    $this.siblings().removeClass("active");
    $this.addClass("active");
    $(".wechat_search input:lt(6)").val("");
    // sessionStorage.setItem("tabNum", num);

    // var $we_op = $("#wechatList .we_operate");
    var $we_op = $("#wechatList thead th:gt(5)");
    var width_use=$(".width_use");
    if(num == 0){
        $(".change_th").html("所匹配到的登录名");
        $("#wechatList thead th:nth-child(6)").removeClass("right_border");
        $we_op.show();
        width_use.attr("width","11%");
    }else if(num == 1){
        $(".change_th").html("已关联登录名");
        $("#wechatList thead th:nth-child(6)").removeClass("right_border");
        $we_op.show();
        width_use.attr("width","11%");
    }else if(num ==2){
        $(".change_th").html("所匹配到的登录名");
        $("#wechatList thead th:nth-child(6)").addClass("right_border");
        $we_op.hide();
        width_use.attr("width","15%");
    }
    searchWechat();
}

function searchWechat(pageno,pagesize){
    var ctx = $("#ctx").val();

    var type = $(".nav-tabs .active").index()||0;
    var telephone = $("#telephone").val()||"";
    var name = $("#name").val()||"";
    var agentName = $("#agentname").val()||"";
    var wechatCode = $("#wechatcode").val()||"";
    var areaCode = $("#areaCode").val()||"";;
    var phone = $("#phone").val()||"";
    var pageNo = pageno||1;
    var pageSize = pagesize||10;
    // console.log(sessionStorage.getItem("key"));


    $.ajax({
        type: "POST",
        url: ctx + "/mobileUser/mobileUserList/1",
        cache: false,
        async: true,
        dataType: "json",//返回的数据类型
        data: {"type":type,"telephone":telephone, "name":name,"agentName":agentName,
            "wechatCode":wechatCode,"areaCode":areaCode,"phone":phone,"pageNo":pageNo,"pageSize":pageSize},
        success: function (data){
            if(data){
                var html="";
                var count=data.count;
                var _webVersion="t2";
                var _pageTest={
                    count:count,
                    pageSize:pageSize,
                    pageNo:pageNo
                };
                var _pageHtml= doPage(_pageTest,_webVersion,goPage);
                $(".pagination").empty();

                if(data.mobileUsers.length>0){
                    for(var i = 0; i < data.mobileUsers.length;i++){
                        var matchLogin="";
                        var matchLoginList="";
                        var userDetail = data.mobileUsers[i];

                        html+="<tr userid="+userDetail.id+"><td>"+(i+1)+"</td>"+
                            "<td class='text_left'>";

                        //解除关联数据添加
                        if(userDetail.unbundleReason){

                            var unbindList = JSON.parse(userDetail.unbundleReason);

                            if(unbindList.length > 0){
                                html+="<div class='unbind_hint'>解除关联" +
                                    "<div class='unbind_reason'><em></em>" +
                                    "<div class='unbind_reason_title'>解除关联原因</div>" +
                                    "<ul class='unbind_reason_content'>";

                                if(!unbindList[0].oldName){
                                    html+="<li><i>"+unbindList[0].type+"</i></li>";
                                }else{
                                    for(var z = 0;z<unbindList.length;z++){
                                        html+="<li><label>"+unbindList[z].type+"：</label>" +
                                            "<span class='detail_unbind'>" +
                                            "<span class='before_change'>"+unbindList[z].oldName+"</span> 改为 " +
                                            "<span class='after_change'>"+unbindList[z].newName+"</span></span></li>";
                                    }
                                }
                            }
                            html+="</ul></div>" +
                                "</div> ";
                        }

                        html+= userDetail.agentName+"</td>"+
                            "<td>"+userDetail.name+"</td>" +
                            "<td>"+userDetail.telephone+"</td>" +
                            "<td>"+userDetail.wechatCode+"</td>" +
                            "<td>"+userDetail.phone+"</td>";
                        //匹配到的登录名打出
                        if(userDetail.t1Users){
                            for(var j = 0;j<userDetail.t1Users.length;j++){
                                if(j>0){matchLoginList+="、";}
                                matchLoginList += userDetail.t1Users[j].loginName;
                            }
                        }

                        //判定显示是否显示最后两列
                        if(type == 0){
                            html+="<td><span class='match_login'>"+matchLoginList+"</span></td>"+
                                "<td><em class='fa fa-cog'></em>" +
                                "<div class='handle'><span></span>" +
                                "<ul>" +
                                "<li onclick='linkToChannel("+userDetail.id+")'>关联</li>" +
                                "<li onclick='createAccount("+userDetail.id+")'>生成新账号</li>" +
                                "<li onclick='delete_weChat("+userDetail.id+")'>删除</li>" +
                                "</ul></div></td></tr>";
                        }else if(type == 1){
                            html+="<td><span class='match_login'>"+userDetail.loginName+"</span></td>"+
                                "<td><em class='fa fa-cog'></em>" +
                                "<div class='handle'><span></span>" +
                                "<ul>" +
                                "<li onclick='cancel_bind("+userDetail.id+")'>取消关联</li>" +
                                "</ul></div></td></tr>";
                        }

                    }

                    $(".pagination").append(_pageHtml);
                    $(".pagination .controls input").eq(1).attr("onkeypress", "var e=window.event||event||this;" +
                        "var c=e.keyCode||e.which;" +
                        "if(c==13){if(this.value>50){this.value = 50;}goPage(1,this.value);}");

                }else{
                    html = "<tr><td colspan="+$("#wechatList thead th:visible").length+">暂无搜索数据</td></tr>"
                }

                $(".activitylist_bodyer_table tbody").html(html);
                hoverShowTitle();

            }
        },
        error: function (){
            alert("error");
        }
    });
}

//所匹配到的登录名，hover显示全部的效果
function hoverShowTitle() {
    $(".match_login").hover(function () {
        $(".title_show").remove();
        var title = $(this).html();
        $("<div class='title_show'>"+title+"</div>").insertAfter(this);
    },function () {
        $(".title_show").remove();
    })
}

function cancel_bind(id) {
    var ctx = $("#ctx").val();
    var submit = function (v) {
        if (v == true){
            $.ajax({
                type: "GET",
                url: ctx + "/mobileUser/unBoundMobileUser",
                cache: false,
                async: true,
                dataType: "json",//返回的数据类型
                data: {"mobileUserId":id},
                success: function (data){
                    jBox.tip("取消关联成功", 'success');
                    searchWechat();
                },
                error: function (){
                    jBox.tip("取消关联失败", 'info');
                }
            });
        }
        return true;
    };
    $.jBox.confirm("是否取消关联?", "提示", submit, { buttons: { '确定': true, '取消': false } });
}

function delete_weChat(id) {
    var ctx = $("#ctx").val();
    var del = function (v) {
        if (v == true){
            $.ajax({
                type: "GET",
                url: ctx + "/mobileUser/delMobileUser",
                cache: false,
                async: true,
                dataType: "json",//返回的数据类型
                data: {"mobileUserId":id},
                success: function (data){
                    jBox.tip("删除成功", 'success');
                    searchWechat();
                },
                error: function (){
                    jBox.tip("删除失败", 'info');
                }
            });
        }
        return true;
    };
    $.jBox.confirm("是否删除?", "提示", del, { buttons: { '确定': true, '取消': false } });
}

//调整数量的代码，功能，只输入数字，进行简单地校验功能
function onlyInputNum(content) {
    var val = content.value.replace(/\D/g,'');
    content.value = val;
}

/**
 * 分页的函数
 * @param page
 * @param pageSize
 */
function goPage(page,pageSize){
    inputDetail.pageNo=page;
    inputDetail.pageSize=pageSize;
    searchWechat(page,pageSize);

}