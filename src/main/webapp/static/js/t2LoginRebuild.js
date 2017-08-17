/**
 * Created by tlw on 2016/12/19.
 * For t2 login page rebuilding.
 */
$(function(){
var that=this;

    function verticalMiddle(){
        var cHeight = $(window).height();
        if(cHeight<572){
            $(".loginDiv").css("margin-top","61px");
            return;
        }
        var margin = (cHeight-510)/2+30;
        $(".loginDiv").css("margin-top",margin);
    }
    verticalMiddle();

    $(window).resize(function(){
        verticalMiddle.bind(that)();
    });
});

/*
//改变浏览器大小时 loginDiv(登录&slogan)始终上下居中
function verticalMiddle(){
    var cHeight = $(window).height();
    if(cHeight<572){
        $(".loginDiv").css("margin-top","61px");
        return;
    }
    var margin = (cHeight-510)/2+30;
    $(".loginDiv").css("margin-top",margin);
}
*/


