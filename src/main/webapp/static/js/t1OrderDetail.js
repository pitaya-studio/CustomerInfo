////顶部定位
function details_nav(obj,i){
	var arr = ['order1','pay1','reservation1','special1','tourist1'];
	var eid = arr[i];
	var eHeight = $("#"+eid).offset().top;
	var sHeight = eHeight - 72;
	$('body,html').animate({scrollTop:sHeight},1,function(){
		var scrollTop=$(window).scrollTop();
		var bottom=scrollTop>=$(document).height()-$(window).height();
		var pTop = $(document).scrollTop();
		var  checkedDomTop=$($(obj).attr("hrefs")).parent().offset().top;
		var flagView=checkedDomTop-scrollTop;
		if(pTop >= 120){
			$(obj).parent().addClass("nav_fixed");
			$("#backup").show();
		}else{
			$(obj).parent().removeClass("nav_fixed");
			$("#backup").hide();
		}
		if(bottom||i==4||flagView>72){
			$(obj).parent().children().removeClass("details_nav_active");
			$(obj).addClass("details_nav_active");
		}else{
		}
	});
}
//顶部定位
var nt = !1;
$(window).bind("scroll",
	function() {
		$(window).height();//是文档窗口高度
		$("div").offset().top//是标签距离顶部高度
		$(document).scrollTop();//是滚动条高度
		$("div").height();//是标签高度
		//你要的高度+$("div").height()+[$("div").offset().top-$(document).scrollTop()]=$(window).height();
		var st = $(window).scrollTop();//往下滚的高度
		var detailsTotop=$("#details").offset().top;
		nt = nt ? nt: $(".J_m_nav").offset().top;
		var sel=$(".J_m_nav");
		if (detailsTotop < st) {
			sel.addClass("nav_fixed");
			$("#backup").show();
		} else {
			sel.removeClass("nav_fixed");
			$("#backup").hide();
		}
		/*if (nt < st) {
		 sel.addClass("nav_fixed");
		 } else {
		 sel.removeClass("nav_fixed");
		 }*/
		var order=$(".order");
		var pay=$(".pay");
		var reservation=$(".reservation");
		var special=$(".special");
		var tourist=$(".touristA");
		var title=50;
		//元素距离文档顶端偏移值
		var orderTotop=$("#order").parent().offset().top;
		var payTotop=$("#pay").parent().offset().top;
		var reservationTotop=$("#reservation").parent().offset().top;
		var specialTotop=$("#special").parent().offset().top;
		var touristTotop=$("#tourist").parent().offset().top;
		//网页被卷起来的高度/宽度（即浏览器滚动条滚动后隐藏的页面内容高度）
		var scrollTop=$(window).scrollTop();
		//页面元素距离浏览器工作区顶端的距离 = 元素距离文档顶端偏移值 - 网页被卷起来的高度
		var orderCha= orderTotop-scrollTop;
		var payCha= payTotop-scrollTop;
		var reservationCha= reservationTotop-scrollTop;
		var specialCha=specialTotop-scrollTop;
		var touristCha=touristTotop-scrollTop;
		var bottom=scrollTop>=$(document).height()-$(window).height();
		if(!bottom) {
			if (orderCha < 75 && payCha > 75) {
				order.parent().children().removeClass("details_nav_active");
				order.addClass("details_nav_active");
			} else if (payCha < 75 && reservationCha > 75) {
				pay.parent().children().removeClass("details_nav_active");
				pay.addClass("details_nav_active");
			} else if (reservationCha < 75 && specialCha > 75) {
				reservation.parent().children().removeClass("details_nav_active");
				reservation.addClass("details_nav_active");
			} else if (touristCha > 75 && specialCha < 75) {
				special.parent().children().removeClass("details_nav_active");
				special.addClass("details_nav_active");
			}
		}
	});