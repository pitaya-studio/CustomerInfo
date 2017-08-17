/**
 * Created by tlw on 2016/12/21.
 */

$(function(){
    //如果没有页签则nav变为白色
    if($(".nav-tabs").children().length==0){
        $(".nav-tabs").css({"background":"#fff","border-color":"#fff"});
    }

    rightHeight();

    $(window).resize(function(){
        rightHeight();
    });
});
//给右侧主要内容增加min-height避免窗口下方出现空白
function rightHeight(){
    var cHeight = document.documentElement.clientHeight;
    var leftHeight = cHeight-60;
    $(".main-right").css("min-height",leftHeight)
}
