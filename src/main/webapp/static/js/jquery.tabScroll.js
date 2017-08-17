/**
 * Created by ding on 2015/6/15.
 */
$(document).ready(function () {
    $("div.sub_main_bands_sel").each(function () {
        tabScroll($(this));
    });

    $(".btn_open_in_bands").on("click", function () {
        var $div = $("div.ydbz_tit").has(this).next().find("div.sub_main_bands");
        var $tab = $("div.ydbz_tit").has(this).next().find("div.sub_main_bands_sel");
        var displayType;
        if ($div.is(".sub_main_bands_unfold")) {
            $(this).val($(this).val().replace("收起", "展开"));
            $(this).text($(this).text().replace("收起", "展开"));
            $div.removeClass("sub_main_bands_unfold");
            $div.children("div").hide();
            $tab.show();
            $tab.find("li.active").click();
            displayType="tab";
        } else {
            $(this).val($(this).val().replace("展开", "收起"));
            $(this).text($(this).text().replace("展开", "收起"));
            $div.addClass("sub_main_bands_unfold");
            $tab.hide();
            $div.children("div").show();
            displayType="list";
        }
        $tab.trigger("tabScroll.displayChange",displayType);
    });
});


function tabScroll($tab,defaultTab_Selector,$appendArea) {
    var marginLeft = 35;
    var ulLeft = 35;
    var ulWidth = 0;
    var lisWidth = 0;
    var curIndex = 0;

    var $lis, $ul;

    function bindEvent() {
        $tab.unbind('click');
        $tab.on("click", "i.sub_main_bands_sel_l_actives", function () {
            // 左移
            var width = $lis.eq(curIndex - 1).outerWidth();
            ulLeft = ulLeft + width;
            $ul.css("left", '' + ulLeft + "px");
            curIndex--;
            resetStatus();
        }).on("click", "i.sub_main_bands_sel_r_actives", function () {
            // 右移
            var width = $lis.eq(curIndex).outerWidth();
            ulLeft = ulLeft - width;
            $ul.css("left", '' + ulLeft + "px");
            curIndex++;
            resetStatus();
        }).on("click", "li", function () {
            $lis.filter(".active").removeClass("active");
            $(this).addClass("active");
            var $contentDivs = $tab.next("div.sub_main_bands").children("div").hide();
            $contentDivs.eq($(this).attr("data-index")).show();
            $tab.trigger("tabScroll.change");
        });

        $(window).on("resize", function () {
        	ulWidth = $ul.width();
 		resetStatus();
        });
           
    }

    function init() {
        $ul = $tab.find("ul");
        $lis = $tab.find("ul li");

        lisWidth = 0;
        $lis.each(function (i) {
            lisWidth += $(this).outerWidth();
            $(this).attr("data-index", i);
        });
        var $contentDivs = $tab.next("div.sub_main_bands").children("div");
        if($contentDivs.length>0){
            var index = $lis.filter(defaultTab_Selector).first().attr("data-index");
            if(index>0){
                $tab.next("div.sub_main_bands").children().eq(index-1).after($appendArea);
            }else{
                $tab.next("div.sub_main_bands").children().eq(index).before($appendArea);
            }
        }else{
            $tab.next("div.sub_main_bands").append($appendArea);
        }
    }

    function resetStatus() {
        if (ulWidth - ulLeft + marginLeft >= lisWidth) {
            $tab.find("i.sub_main_bands_sel_r_greys").removeClass("sub_main_bands_sel_r_actives");
        } else {
            $tab.find("i.sub_main_bands_sel_r_greys").addClass("sub_main_bands_sel_r_actives");
        }

        if (ulLeft >= marginLeft) {
            $tab.find("i.sub_main_bands_sel_l_greys").removeClass("sub_main_bands_sel_l_actives");
        } else {
            $tab.find("i.sub_main_bands_sel_l_greys").addClass("sub_main_bands_sel_l_actives");
        }
    }

    init();
    bindEvent();
    $(window).resize();
    if(defaultTab_Selector){
        $lis.filter(defaultTab_Selector).first().click();
    }
    else{
        $lis.filter(".active,:first").first().click();
    }
    return {
        reset: function () {
            init();
            resetStatus();
        }
    }
}