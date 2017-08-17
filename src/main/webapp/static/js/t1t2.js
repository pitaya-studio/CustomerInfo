// var e=window.event||event;
//
// var targetJQ;
//
// //基本信息/银行账户/资质信息切换 START
// function getEvent(){
// 	var browser=navigator.appName;
// 	// var _target;
//
// 	if(browser=="Microsoft Internet Explorer")
// 	{
// 		alert(e);
// 		targetJQ=e.srcElement;
// 	}else{
// 		targetJQ=e.target;
// 	}
// 	return targetJQ;
// }

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


function changeTab(obj){
	
		if($(obj).attr("id")=="baseInfoTab"){
			$("#baseInfo").show();
			$("#bankAccount").hide();
			$("#credentials").hide();
			$(obj).parent().addClass("active");
			$(obj).parent().siblings().removeClass("active");
		}
		if($(obj).attr("id")=="bankAccountTab"){
			$("#bankAccount").show();
			$("#baseInfo").hide();
			$("#credentials").hide();
			$(obj).parent().addClass("active");
			$(obj).parent().siblings().removeClass("active");

		}
		else if($(obj).attr("id")=="credentialsTab"){

			$("#credentials").show();
			$("#baseInfo").hide();
			$("#bankAccount").hide();
			$(obj).parent().addClass("active");
			$(obj).parent().siblings().removeClass("active");

		}
		if($(obj).attr("id")=="groupTab"){
			$("#group").show();
			$("#order").hide();
			$(obj).parent().addClass("active");
			$(obj).parent().siblings().removeClass("active");
		}else if($(obj).attr("id")=="orderTab"){
			$("#group").hide();
			$("#order").show();
			$(obj).parent().addClass("active");
			$(obj).parent().siblings().removeClass("active");
		}

	}

	//基本信息/银行账户/资质信息切换 END

	//修改 START

	function edit(){
		var div=$(".content >div").not(":hidden").eq(2);
		div.find(".details_d2 span").hide();
		div.find(".details_d3 span").hide();
		div.find(".details_d2 input").show();
		div.find(".details_d2_long select,input").show();
		div.find(".details_d3 input").show();
		div.find(".uploadImg").show();
		div.find(".addContect").show();
		div.find(".ulFile").show();
		div.find(".cancelSave").show();
		div.find(".disabled input").attr("disabled",false);
		div.find(".addAccount").show();
		div.find(".AccountClone").show();
		div.find(".details_d2_long>span").hide();
		if($(".contect").size()=="1"){
			$(".contect").find(".delContect").hide();
			$(".contect").removeClass("contect-hover");
		}else{
			$(".contect").find(".delContect").show();
		}
		if($(".Account").size()=="1"){
			$(".Account").find(".delAccount").hide();
		}else{
			$(".Account").find(".delAccount").show();
		}
	}



	//修改 END
	//function returenBack(obj){
	//	var $this=$(obj);
	//	$this.prev().css("display","inline-block")
	//}
	//function returenNone(obj){
	//	var $this=$(obj);
	//	$this.prev().css("display","none")
	//}
	$(function(){
		// modfiy by huochangying at 2016.10.14 for 538
		// 模拟select
		$(".dl-select input").click(function(event){
			var event = event || window.enent;
			event.stopPropagation();
			$(this).parent().children("ul").toggle();
		});
		$(".dl-select ul li").each(function(){
			$(this).click(function(){
				var value = $(this).text();
				$(this).parent().hide();
				$(this).parent().parent().children("input").val(value);
                var useDiv=$(this).parent().parent().parent();
                if(value=="银行卡"){
                    useDiv.find(".bank_table").show();
                    useDiv.find(".bank_account").show();
                    useDiv.find(".alipay_table").hide();
                    useDiv.find(".alipay_account").hide();
                }else{
                    useDiv.find(".bank_table").hide();
                    useDiv.find(".bank_account").hide();
                    useDiv.find(".alipay_table").show();
                    useDiv.find(".alipay_account").show();
                    if(value=="支付宝"){
                        useDiv.find(".accountChange").eq(0).text("支付宝");
                        useDiv.find(".accountChange").eq(1).text("支付宝");
                    }else{
                        useDiv.find(".accountChange").eq(0).text("微信");
                        useDiv.find(".accountChange").eq(1).text("微信");
                    }
                }
			});
		});
		// modfiy by huochangying at 2016.10.14 for 538
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
			$(this).children(".ascertain").show();
		},function(){
			$(this).removeClass("ascertain_shadow");
			$(this).children(".ascertain").hide();
		})

	});
	//function selectNew(obj){
	//	var $this=$(obj);
	//	if($this.attr("checked","false")){
	//		console.log("a")
	//	}else{
	//		console.log("b")
	//	}
	//}

	//添加联系人 START
	function addContect(){
		$(".contect").addClass("contect-hover");
		$(".contect").find(".delContect").show();
		var contect = $(".contect:first").clone();
		contect.css({"margin-top":"20px","padding-top":"10px"});

		contect.find(".contectNum").text(parseInt($(".contect:last").find(".contectNum").text())+1);



		$(".contect:last").after(contect);


		 //$(".contectNum").each(function(){
          //      var num = $(this).parents(".contect").index();
          //      $(this).text(num);
          //  });



	}

	//添加联系人 END

	//删除联系人 START

	function delContect(obj){

		var num=$(obj).parents(".contect").nextAll().find(".contectNum").length;
		for(var i=0;i<num;i++) {
			$(obj).parents(".contect").nextAll().find(".contectNum").eq(i).text($(obj).parents(".contect").nextAll().find(".contectNum").eq(i).text() - 1);
		}


		$(obj).parents(".contect").remove();

		if($(".contect").size()=="1"){
			$(".contect").find(".delContect").hide();
			$(".contect").removeClass("contect-hover");
		}

		 //$(".contectNum").each(function(){
          //      var num = $(this).parents(".contect").index();
          //      $(this).text(num);
          //  });

	}

	//删除联系人 END


//鼠标hover于文件名时显示图片 START

function showImg(obj){

	var docName = $(obj).text();
	if(!$.trim(docName)){
		return false;
	}
	var e = window.event || $(obj);
	var top = e.pageY+"px";
	var left = e.pageX +"px"; 
	var img = $(obj).parent().find(".showimg");
	img.show(200);
	img.css({
		"top":top,
		"left":left
	});


}

function hideImg(obj){
	var img = $(obj).parent().find(".showimg");
	img.hide(200);

}

function moveImg(obj){

	var docName = $(obj).text();
	if(!$.trim(docName)){
		return false;
	}
	var e = window.event || $(obj);

	var img = $(obj).parent().find(".showimg");
	var top = e.pageY+10+"px";
	var left = e.pageX +10+"px";
	img.css({
		"top":top,
		"left":left
	});


}
/*////顶部定位
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
    /!*if (nt < st) {
    	sel.addClass("nav_fixed");
    } else {
    	sel.removeClass("nav_fixed");
    }*!/
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
    });*/
//弹框

function details(){
	$.jBox("iframe:（T1-T2打通-T1）详情弹窗 - 修改.html", {
		title: "详情",
		width: 880,
		height: 630,
		persistent: true,
		buttons:false
	});

}
function change_password(){	
	$.jBox("iframe:" + $("#ctx").val() + "/t1/password/manage/modifyPwd", {
		title : "修改密码",
		width : 520,
		height : 330,
		persistent : true,
		buttons : false
	}, function(){
		
	});
}
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

//添加账户
function addAccount(){
	var table=$(".Account:first").clone();
	//var div='<div class="delAccount hide" onclick="delAccount(this)"></div>'
	//table.find(".append").append(div)
	$(".Account:last").after(table);
	if($(".Account").size()!="1"){
		$(".Account").find(".delAccount").show();
	}
}
//删除账户
function delAccount(obj){
	var $this=$(obj);
	$this.parent().parent().parent().parent().remove();
	if($(".Account").size()=="1"){
		$(".Account").find(".delAccount").hide();
	}
}

//取消
function cancelthis(){
	var div=$(".content >div").not(":hidden").eq(2);
	div.find(".details_d2 span").show();
	div.find(".details_d3 span").show();
	div.find(".details_d2 input").hide();
	div.find(".details_d3 input").hide();
	div.find(".uploadImg").hide();
	div.find(".addContect").hide();
	div.find(".ulFile").hide();
	div.find(".cancelSave").hide();
	div.find(".disabled input").attr("disabled",true);
	div.find(".addAccount").hide();
	div.find(".AccountClone").hide();
	div.find(".details_d2_long>span").show();
	div.find(".details_d2_long select,input").hide();

}
//保存
function savethis(){
	var div=$(".content >div").not(":hidden").eq(2);
	div.find(".details_d2 span").show();
	div.find(".details_d3 span").show();
	div.find(".details_d2 input").hide();
	div.find(".details_d3 input").hide();
	div.find(".uploadImg").hide();
	div.find(".addContect").hide();
	div.find(".ulFile").hide();
	div.find(".cancelSave").hide();
	div.find(".disabled input").attr("disabled",true);
	div.find(".addAccount").hide();
	div.find(".AccountClone").hide();
	div.find(".details_d2_long>span").show();
	div.find(".details_d2_long select,input").hide();
}
//选择目的地
function chooseDestination(){
	$.jBox("iframe:目的地弹窗.html", {
		title: "选择区域",
		width: 260,
		height: 360,
		persistent: true,
		buttons:{"确认":"1","关闭":"0"}
	});
}
function product() {
	if ($(".productChild").css("display") == "block"){
		$(".productChild").css("display", "none");
		$(".unwind_bottom").addClass("unwind_right");
		$(".unwind_right").removeClass("unwind_bottom");
	}else{
		$(".productChild").css("display", "block");
		$(".unwind_bottom").removeClass("unwind_right");
		$(".unwind_right").addClass("unwind_bottom");
	}
}
function reservation(){
	if ($(".reservationDivDiv").css("display") == "block"){
		$(".reservationDivDiv").css("display", "none");
		$(".unwind_bottom2").addClass("unwind_right2");
		$(".unwind_right2").removeClass("unwind_bottom2");
	}else{
		$(".reservationDivDiv").css("display", "block");
		$(".unwind_bottom2").removeClass("unwind_right2");
		$(".unwind_right2").addClass("unwind_bottom2");
	}
}

function details_all(obj){
	var $this=$(obj);
	$this.parent().addClass("hide");
	$this.parent().next().removeClass("hide");
}
function details_less(obj){
	var $this=$(obj);
	$this.parent().addClass("hide");
	$this.parent().prev().removeClass("hide");
}
function informationSpread(obj){
	var $this=$(obj);
	var div=$this.parent().parent().parent().children(":last");
	var div_siblings=$(".channel_brand");
	var use=div_siblings.find(".redact_use");
	if(div.is(":hidden")){
		div_siblings.children("div").slideUp();
		div_siblings.css("background","none");
		use.each(function(){
			var $this2=$(this);
			if(!$this2.hasClass("redact")){
				$this2.addClass("redact");
				$this2.children(":last").hide();
				$this2.children(":first").show();
			}
		});
		div.slideDown();
		$this.hide();
		$this.parent().removeClass("redact");
		$this.siblings().show();
		div.parent().css("background","#fafafa");
	}else{
		div.slideUp();
		$this.hide();
		$this.parent().addClass("redact");
		$this.siblings().show();
		div.parent().css("background","none");
	}
}
//编辑
function copyReader(obj){
	var $this=$(obj);
	$this.hide();
	$this.siblings().show();
	$this.parent().siblings().find(".slide_open_close").css("background-position","-145px -48px");
	var div=$this.parent().parent().next();
	div.slideDown();
	div.find("span").hide();
	div.find(".input").css("display","inline-block");
	div.find("input,textarea").show();
	$this.parent().parent().find(".head_hide").hide();
	$this.parent().parent().find("input").show();
}
//保存
function save_contact(obj){
	var $this=$(obj);
	$this.hide();
	$this.siblings().show();
	var div=$this.parent().parent().next();
	div.find("span").show();
	div.find(".input").hide();
	div.find("input,textarea").hide();
	$this.parent().parent().find(".head_hide").css("display","inline-block");
	$this.parent().parent().find("input").hide();
}
//展开收起
function slide_open_close(obj){
	var $this=$(obj);
	var div=$this.parent().parent().next();
	if(div.is(":hidden")){
		div.slideDown();
		$this.children().css("background-position","-145px -48px");
	}else{
		div.slideUp();
		$this.children().css("background-position"," -121px -47px");
	}
}
//新建联系人
function new_contact(){
	var div='<div class="contact_information">'
		+'<p class="head_background">'
		+'<span><font>*</font>联系人：</span><span class="contact_information_name head_hide hide"></span><input type="text" class="contact_information_input"/>'
		+'<span><font>*</font>手机号：</span><span class="head_hide hide"></span><input type="text" class="contact_information_input" />'
		+'<span class="float_right ">'
		+'<span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>'
		+'</span>'
		+'<span class="float_right hide delete_save">'
		+'<span class="delete " onclick="delete_contact(this)"><em class="t1_2 delete_t1_2"></em> 删除</span><span class="save" onclick="save_contact(this);"><em class="t1_2 save_t1_2"></em> 保存</span><span class="compile hide" onclick="copyReader(this);"><em class="t1_2 compile_t1_2"></em> 编辑</span>'
		+'</span>'
		+'</p>'
		+'<div class="border_top">'
		+'<table class="channel_information_table">'
		+'<tr>'
		+'<td width="95">固定电话：</td>'
		+'<td width="400">'
		+'<span class="channel_phone hide">'
		+'<span></span>-<span></span>'
		+'</span>'
		+'<span class="input">'
		+'<input type="text" class="min_input "/>-'
		+'<input type="text" class="min_right_input "/>'
		+'</span>'
		+'</td>'
		+'<td  width="95">QQ：</td>'
		+'<td  width="360">'
		+'<span class="hide"></span>'
		+'<input type="text" />'
		+'</td>'
		+'</tr>'
		+'<tr>'
		+'<td>传真：</td>'
		+'<td>'
		+'<span class="channel_phone hide">'
		+'<span></span>-<span></span>'
		+'</span>'
		+'<span class="input">'
		+'<input type="text" class="min_input "/>-'
		+'<input type="text" class="min_right_input"/>'
		+'</span>'
		+'</td>'
		+'<td>电子邮箱：</td>'
		+'<td>'
		+'<span class="hide"></span>'
		+'<input type="text" />'
		+'</td>'
		+'</tr>'
		+'<tr>'
		+'<td>描述：</td>'
		+'<td>'
		+'<span class="hide"></span>'
		+'<textarea name="" id="" cols="30" rows="10" class="contact_information_text "></textarea>'
		+'</td>'
		+'<td></td>'
		+'<td></td>'
		+'</tr>'
		+'</table>'
		+'</div>'
		+'</div>';
	var p=$("#contact_information>p");
	p.after(div);
}
//新建账户
function new_bank(){
	var div='<div class="contact_information">'
		+' <p class="head_background">'
		+'<span><font>*</font>账户名称：</span><span class="contact_information_name head_hide hide"></span><input type="text" class=" contact_information_input"/> '
		+'<span><font>*</font>账户号码：</span><span class="contact_information_body head_hide hide"></span><input type="text" class=" contact_information_input" /> '
		+'<span class="setDefault setDefault_hover" onclick="setDefault(this)">设为默认</span>'
		+'<span class="default hide">默认账户</span>'
		+'<span class="float_right ">'
		+'<span class="slide" onclick="slide_open_close(this)"><em class="t1_2 slide_open_close"></em></span>'
		+'</span>'
		+'<span class="float_right hide delete_save">'
		+'<span class="delete " onclick="delete_contact(this)"><em class="t1_2 delete_t1_2"></em> 删除</span><span class="save " onclick="save_contact(this);"><em class="t1_2 save_t1_2"></em> 保存</span><span class="compile hide" onclick="copyReader(this);"><em class="t1_2 compile_t1_2"></em> 编辑</span>'
		+'</span>'
		+'</p>'
		+'<div class="">'
		+'<table class="channel_information_table">'
		+'<tr>'
		+'<td width="98"><font>*</font>开户行名称：</td>'
		+'<td width="400">'
		+'<span class="hide">'
		+'</span>'
		+'<input type="text" />'
		+'</td>'
		+'<td  width="95"></td>'
		+'<td  width="360">'
		+'</td>'
		+'</tr>'
		+'<tr>'
		+'<td>开户行地址：</td>'
		+'<td>'
		+'<span class=" hide">'
		+'</span>'
		+'<input type="text" />'
		+'</td>'
		+'<td></td>'
		+'<td>'
		+'</td>'
		+'</tr>'
		+'<tr>'
		+'<td>描述：</td>'
		+'<td>'
		+'<span class="hide"></span>'
		+'<textarea name="" id="" cols="30" rows="10" class="contact_information_text "></textarea>'
		+'</td>'
		+'<td></td>'
		+'<td></td>'
		+'</tr>'
		+'</table>'
		+'</div>'
		+'</div>';
	var p=$("#contact_information>p");
	p.after(div);
}
//删除
function delete_contact(obj){
	var $this=$(obj);
	$this.parent().parent().parent().remove();
}

//显示更多筛选条件
function more_less(){
	if($(".more_less_div").is(":hidden")){
		$(".more_less_div").slideDown();
		$(".show_more").hide();
		$(".show_less").show();
		$("#groupHomeSearch_down_border").css("border-bottom","1px dashed #dddddd");
	}else{
		$(".more_less_div").slideUp();
		$(".show_more").show();
		$(".show_less").hide();
		$("#groupHomeSearch_down_border").css("border-bottom","none");
	}
}

//点击更多操作
function switchTip(obj){
	var $parentNode = $(obj).parent();
	var $divTip = $parentNode.find(".main_container");
	var displayCss =  $divTip.css("display");

	//161009 bug16207
	if(displayCss != 'none'){
		$divTip.css("display","none");
	}else {
		$(".main_container").hide();
		$('.city_list').niceScroll({
			cursorcolor: "#ccc",//#CC0071 光标颜色
			cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
			touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
			cursorwidth: "5px", //像素光标的宽度
			cursorborder: "0", //     游标边框css定义
			cursorborderradius: "5px",//以像素为光标边界半径
			autohidemode: false //是否隐藏滚动条
		});

		//清空历史勾选项
//	$(".main_content").find("li>em").each(function (){
//		if(this.className.indexOf("selected_box") !=-1){
//			this.className = "item_icon";
//		}
//	})
		$divTip.css("display","inline-block");
	}
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
	var inputValue = obj.parentNode.getElementsByClassName("se_input")[0].value;
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
//点击确定按钮，获取出发地、目的地等 1:出发地、2：目的地、3：供应商
function  getEle(obj,flag){
	var lis = obj.parentNode.parentNode.getElementsByTagName("ul")[0].getElementsByTagName("li");
	//获取此搜索条件的常用条件
	var commonCon=[];
	var commonConP=$(obj).parent().parent().parent().find("p>span");
	$(obj).parent().parent().parent().find("p>span").each(function(){
		commonCon.push($(this).text());
	})

	for(var i=0;i<lis.length;i++){
		var flg=true;
		//判断是否选中
		if(lis[i].getElementsByTagName("em")[0].className.indexOf("selected_box") != -1){
			var val = lis[i].getElementsByTagName("span")[0].innerHTML;
			//判断下是否从多选框选择的在常用条件中已存在，若存在，则将其设为选中状态
			if(commonCon.indexOf(val)!=-1){
				commonConP.each(function(){
					if($(this).text()==val){
						/**
                         * 判断下改节点是否处于选中状态，如果是，则终止操作，如果没有被选中，则选中
						 */
						if($(this).is(".groupHomeSearch_down_active")||$(this).is(".groupHomeSearch_right_child")){
							flg=false;
							closeTip(obj);
							//return false;
						}else{
							$(this).attr("class","search_spare groupHomeSearch_down_active");

						}

					}
				})
			}else{//若不存在，则进行添加操作
				var emphasis = document.createElement("em");
				emphasis.setAttribute('class','t1_2');
				var spanEle = document.createElement("span");
				spanEle.setAttribute('class','groupHomeSearch_right_child');
				spanEle.innerHTML = val;
				spanEle.appendChild(emphasis);
				obj.parentNode.parentNode.parentNode.getElementsByTagName("p")[0].appendChild(spanEle);
			}
			//把前面的“全部”的选中状态给移除了
			$(obj).parent().parent().parent().find("p>span :first").attr("class","search_spare");
			var p_container = document.getElementById("selected_condition");
			var spans = p_container.getElementsByClassName("groupHomeSearch_ml");
			//判断是否包含出发城市
			if(flag == 1) {
				var departure_city = false;
				var dc_index = 0;
				for (var j = 0; j < spans.length; j++) {
					var content = spans[j].getElementsByClassName("groupHomeSearch_left")[0].innerHTML;
					if (content.indexOf("出发城市") != -1) {
						departure_city = true;
						dc_index = j;
						break;
					}
				}
				//包含出发城市
				if (flg) {
					if (departure_city) {
						addInclude(spans[dc_index], val);
					} else {
						addNoInclude(p_container, '出发城市', val);
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
					var content = spans[j].getElementsByClassName("groupHomeSearch_left")[0].innerHTML;
					if(content.indexOf("目的地") != -1){
						departure_city = true;
						dc_index = j;
						break;
					}
				}
				//包含出发城市
				if (flg) {
					if (departure_city) {
						addInclude(spans[dc_index], val);
					} else {
						addNoInclude(p_container, '目的地', val);
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
					var content = spans[j].getElementsByClassName("groupHomeSearch_left")[0].innerHTML;
					if(content.indexOf("供应商") != -1){
						departure_city = true;
						dc_index = j;
						break;
					}
				}
				//包含出发城市
				if (flg) {
					if (departure_city) {
						addInclude(spans[dc_index], val);
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
			}
		}
	}
	closeTip(obj);
}

function addInclude(ele,val){
	//是否包含一个
	var spanlength = ele.getElementsByClassName("groupHomeSearch_right")[0].getElementsByClassName("limit_container")[0].getElementsByTagName("span");
	if(spanlength.length == 1){
		spanlength[0].className = 'groupHomeSearch_right_child';
	}
	var emele1 = document.createElement("em");
	emele1.setAttribute('class','t1_2');
	var spanele1 = document.createElement("span");
	spanele1.setAttribute('class','groupHomeSearch_right_child');
	spanele1.innerHTML = val;
	spanele1.appendChild(emele1);
	var List = ele.getElementsByClassName("groupHomeSearch_right")[0].getElementsByClassName("limit_container")[0];
	List.appendChild(spanele1);	
}

function addNoInclude(ele,val,selval){
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
function del_ff(elem){
	var elem_child = elem.childNodes;
		for(var i=0; i<elem_child.length;i++){
			if(elem_child[i].nodeName == "#text" && !/\s/.test(elem_child.nodeValue)){
				elem.removeChild(elem_child[i])
			}
		}
	return elem;
}
//高级展开
function high_ranking(){
	if($(".groupSearch").is(":hidden")){
		$(".groupSearch").slideDown();
		$(".group_nav_child_em").css("background-position","-98px -102px");
	}else{
		$(".groupSearch").slideUp();
		$(".group_nav_child_em").css("background-position","-100px -129px");
	}
}
//验证只能输入数字
var partten = /^[0-9]+$/;
$(document).ready(function(){
	//为IE8添加数组indexOF方法
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
	};
	$('input[name=sort]').keyup(function(){
		if(!partten.test($(this).val())){
			var a= $(this).val();
			var b=a.replace(/[^0-9]+/gi,'');
			$(this).val(b)
		}
	})
});
//详情弹窗计算功能
function count(){
	var adult_price=$("#adult_price").text();
	var adult_money=$("#adult_money").val();
	var adult=$("#adult").val();
	var child_price=$("#child_price").text();
	var child_money=$("#child_money").val();
	var child=$("#child").val();
	var special_price=$("#special_price").text();
	var special_money=$("#special_money").val();
	var special=$("#special").val();
	var adult_last=(adult_money-adult_price)*adult;
	var child_last=(child_money-child_price)*child;
	var special_last=(special_money-special_price)*special;
	$("#adult_last").text(adult_last);
	$("#child_last").text(child_last);
	$("#special_last").text(special_last);
	$("#all_last").text(adult_last+child_last+special_last);
	$(".profit_count_one").hide();
	$(".profit_count_two").show();
}
function count_again(){
	$(".profit_count_one").show();
	$(".profit_count_two").hide();
}

function stopPop(obj,event){
    event = getEvent();
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
    $(".pos-popup").hide(); 
}
//银行账户设为默认
function setDefault(obj){
	var $this=$(obj);
	$this.removeClass("setDefault_hover");
	$this.next().show();
	$this.parents(".contact_information").siblings(".contact_information").find(".default").hide();
	$this.parents(".contact_information").siblings(".contact_information").find(".setDefault").addClass("setDefault_hover");
}
function dealDom(id){
	var idx="#"+id;
	if($(idx).children().length>0){
		$(idx).parent().parent().show();
	}else{
		$(idx).parent().parent().hide();
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
function dealDom(id){
	var idx="#"+id;
	if($(idx).children().length>0){
		$(idx).parent().parent().show();
	}else{
		$(idx).parent().parent().hide();
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
		dealDom("limit_container_sc");
		dealDom("limit_container_ec");
		dealDom("limit_container_su");
		dealDom("limit_container_date");
		dealDom("limit_container_day");
		dealDom("limit_container_price");
		dealDom("limit_container_seat");
	}else{
		$("#groupHomeSearch_one").hide();
		dealDom("limit_container_sc");
		dealDom("limit_container_ec");
		dealDom("limit_container_su");
		dealDom("limit_container_date");
		dealDom("limit_container_day");
		dealDom("limit_container_price");
		dealDom("limit_container_seat");
	}
	//一种类型 过滤条件的删除
	function deleteFilter(filterId,conditionId){
		$("#"+conditionId).find("p>span").each(function(){
			if($(this).find("em").length>0){
				$(this).remove();
			}
			$(this).attr("class","search_spare");
		})
		$("#"+conditionId).find("p>span :first").attr("class","search_spare groupHomeSearch_down_active");
	}
	$(".t1_2.dif").bind("click",function(e){

		var whatFilter=getEvent().previousElementSibling.id;
		$("#"+whatFilter).empty();
		dealDom(whatFilter);
		switch (whatFilter){
			case "limit_container_sc":
				deleteFilter("limit_container_sc","startCity");
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
		$(".limit_container").each(function(){
			dealDom($(this).attr("id"));
		})
		/*dealDom("limit_container_sc");
		 dealDom("limit_container_ec");
		 dealDom("limit_container_su");*/
	});
	//$("#groupHomeSearch_one").hide();
	//绑定选中条件的单个移除事件
	$(".limit_container").bind("click",function(e){
		var filterWhich= getEvent().parentNode.parentNode.id;
		var targetJQ=$(getEvent());
		switch (filterWhich){
			case "limit_container_sc":
				deleteFilterAlone("limit_container_sc","startCity",targetJQ);
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
		var targetJQ=$(event.target);
		switch (conditionWhich){
			case "startCity":
				getAll(targetJQ,"limit_container_sc",e);
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
	})
});

//点击全部的时候进行的操作
function getAll(targetJQ,id,e){
	var DXcondition=["limit_container_date","limit_container_price","limit_container_seat"];
	var radioOrBox=DXcondition.indexOf(id)>-1;
	//点击全部
	if(targetJQ[0].innerHTML=="全部"){
		if(radioOrBox){
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

//输入框的值提交
function bindFilterFromI(divId,spanId,targetJQ){
	var firstVal=targetJQ.prev().prev().val();
	var secondeVal=targetJQ.prev().val();
	/*	if(secondeVal<firstVal){
	 alert("数值区间有误，请重新输入");
	 }*/
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
		$("#"+spanId).append('<span class="groupHomeSearch_right_child  non_class">'+firstVal+'天-'+secondeVal+'天</span>');
	}
	if(spanId=="limit_container_price"){
		$("#"+spanId).append('<span class="groupHomeSearch_right_child">'+firstVal+'元-'+secondeVal+'元</span>');
	}

	dealDom(spanId);
}

//点击确定按钮，获取出发地、目的地等 1:出发地、2：目的地、3：供应商
function  getEle(obj,flag){
	var lis = obj.parentNode.parentNode.getElementsByTagName("ul")[0].getElementsByTagName("li");
	//获取此搜索条件的常用条件
	var commonCon=[];
	var commonConP=$(obj).parent().parent().parent().find("p>span");
	$(obj).parent().parent().parent().find("p>span").each(function(){
		commonCon.push($(this).text());
	})
	var flg=true;
	for(var i=0;i<lis.length;i++){
		//判断是否选中
		if(lis[i].getElementsByTagName("em")[0].className.indexOf("selected_box") != -1){
			var val = lis[i].getElementsByTagName("span")[0].innerHTML;
			//判断下是否从多选框选择的在常用条件中已存在，若存在，则将其设为选中状态
			if(commonCon.indexOf(val)!=-1){
				commonConP.each(function(){
					if($(this).text()==val){
						/**
						 * 判断下改节点是否处于选中状态，如果是，则终止操作，如果没有被选中，则选中
						 */
						if($(this).is(".groupHomeSearch_down_active")||$(this).is(".groupHomeSearch_right_child")){
							flg=false;
							closeTip(obj);
							return false;
						}else{
							$(this).attr("class","search_spare groupHomeSearch_down_active")
						}

					}
				})
			}else{//若不存在，则进行添加操作
				var emphasis = document.createElement("em");
				emphasis.setAttribute('class','t1_2');
				var spanEle = document.createElement("span");
				spanEle.setAttribute('class','groupHomeSearch_right_child');
				spanEle.innerHTML = val;
				spanEle.appendChild(emphasis);
				obj.parentNode.parentNode.parentNode.getElementsByTagName("p")[0].appendChild(spanEle);
			}
			//把前面的全部的选中状态给移除了
			$(obj).parent().parent().parent().find("p>span :first").attr("class","search_spare");
			var p_container = document.getElementById("selected_condition");
			var spans = p_container.getElementsByClassName("groupHomeSearch_ml");
			//判断是否包含出发城市
			if(flag == 1) {
				var departure_city = false;
				var dc_index = 0;
				for (var j = 0; j < spans.length; j++) {
					var content = spans[j].getElementsByClassName("groupHomeSearch_left")[0].innerHTML;
					if (content.indexOf("出发城市") != -1) {
						departure_city = true;
						dc_index = j;
						break;
					}
				}
				//包含出发城市
				if (flg) {
					if (departure_city) {
						addInclude(spans[dc_index], val);
					} else {
						addNoInclude(p_container, '出发城市', val);
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
					var content = spans[j].getElementsByClassName("groupHomeSearch_left")[0].innerHTML;
					if(content.indexOf("目的地") != -1){
						departure_city = true;
						dc_index = j;
						break;
					}
				}
				//包含出发城市
				if (flg) {
					if (departure_city) {
						addInclude(spans[dc_index], val);
					} else {
						addNoInclude(p_container, '目的地', val);
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
					var content = spans[j].getElementsByClassName("groupHomeSearch_left")[0].innerHTML;
					if(content.indexOf("供应商") != -1){
						departure_city = true;
						dc_index = j;
						break;
					}
				}
				//包含出发城市
				if (flg) {
					if (departure_city) {
						addInclude(spans[dc_index], val);
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
			}
		}
	}
	closeTip(obj);
}
function addInclude(ele,val){
	//是否包含一个
	var spanlength = ele.getElementsByClassName("groupHomeSearch_right")[0].getElementsByClassName("limit_container")[0].getElementsByTagName("span");
	if(spanlength.length == 1){
		spanlength[0].className = 'groupHomeSearch_right_child';
	}
	var emele1 = document.createElement("em");
	emele1.setAttribute('class','t1_2');
	var spanele1 = document.createElement("span");
	spanele1.setAttribute('class','groupHomeSearch_right_child');
	spanele1.innerHTML = val;
	spanele1.appendChild(emele1);
	var List = ele.getElementsByClassName("groupHomeSearch_right")[0].getElementsByClassName("limit_container")[0];
	List.appendChild(spanele1);
}
function addNoInclude(ele,val,selval){
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
function dealDom(id){
	var idx="#"+id;
	if($(idx).children().length>0){
		$(idx).parent().parent().show();
	}else{
		$(idx).parent().parent().hide();
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
		dealDom("limit_container_sc");
		dealDom("limit_container_ec");
		dealDom("limit_container_su");
	}else{
		$("#groupHomeSearch_one").hide();
		dealDom("limit_container_sc");
		dealDom("limit_container_ec");
		dealDom("limit_container_su");
	}
	$(".t1_2.dif").bind("click",function(e){
		var whatFilter=getEvent().previousElementSibling.id;
		$("#"+whatFilter).empty();
		dealDom(whatFilter);
		//同时把下面的选中状态去掉
		if(whatFilter=="limit_container_sc"){
			$("#startCity").find("p>span").each(function(){
				$(this).attr("class","search_spare");
			})
			$("#startCity").find("p>span :first").attr("class","search_spare groupHomeSearch_down_active");
		}
		if(whatFilter=="limit_container_ec"){
			$("#endCity").find("p>span").each(function(){
				$(this).attr("class","search_spare");
			})
			$("#endCity").find("p>span :first").attr("class","search_spare groupHomeSearch_down_active");
		}
		if(whatFilter=="limit_container_su"){
			$("#groupHomeSearch_down_border").find("p>span").each(function(){
				$(this).attr("class","search_spare");
			})
			$("#groupHomeSearch_down_border").find("p>span :first").attr("class","search_spare groupHomeSearch_down_active");
		}
	});
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
		$(".limit_container").each(function(){
			dealDom($(this).attr("id"));
		})
		/*dealDom("limit_container_sc");
		 dealDom("limit_container_ec");
		 dealDom("limit_container_su");*/
	});
	//$("#groupHomeSearch_one").hide();
	//绑定点击移除事件
	$(".limit_container").bind("click",function(e){
		var filterWhich= getEvent().parentNode.parentNode.id;
		var targetJQ=$(getEvent());
		switch (filterWhich){
			case "limit_container_sc":
				deleteFilterAlone("limit_container_sc","startCity",targetJQ);
				break;
			case "limit_container_ec":
				deleteFilterAlone("limit_container_ec","endCity",targetJQ);
				break;
			case "limit_container_su":
				deleteFilterAlone("limit_container_su","groupHomeSearch_down_border",targetJQ);
				break;
		}
	})
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
		var targetJQ=$(event.target);
		switch (conditionWhich){
			case "startCity":
				getAll(targetJQ,"limit_container_sc",e);
				break;
			case "endCity":
				getAll(targetJQ,"limit_container_ec",e);
				break;
			case "groupHomeSearch_down_border":
				getAll(targetJQ,"limit_container_su",e);
				break;
			case "tourDay":
				getAll(targetJQ,"limit_container_day",e);
				break;

		}
	})
});
//点击全部的时候进行的操作
function getAll(targetJQ,id,e){
	if(targetJQ[0].innerHTML=="全部"){
		//点击全部
		$(getEvent()).parent().children().each(function(){
			if($(this).find("em").length>0){
				$(this).remove();
			}else{
				$(this).attr("class","search_spare");
			}
			//如果是从多选框添加的
		})
		targetJQ.addClass("groupHomeSearch_down_active");
		$("#"+id).empty();
		$("#"+id).append('<span class="groupHomeSearch_right_child">'+targetJQ[0].innerHTML +'<em class="t1_2"></em></span>');
	}else{
		if(targetJQ.hasClass("groupHomeSearch_down_active")||targetJQ.hasClass("groupHomeSearch_right_child")){
			//如果点击的选择框选择的数据附带的X号，那么执行删除操作
		}else{
			//点击单个的
			targetJQ.addClass("groupHomeSearch_down_active");
			$(getEvent()).parent().children(":first").removeClass("groupHomeSearch_down_active");
			//如果已选择项里面有全部，则把全部去掉
			$("#"+id).children().each(function(){
				if($(this).text()=="全部"){
					$(this).remove();
				}
			})
			$("#"+id).append('<span class="groupHomeSearch_right_child" id="'+targetJQ[0].id+'">'+targetJQ[0].innerHTML +'<em class="t1_2"></em></span>');
		}
	}
	dealDom(id);
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
			}else{
				$(this).attr("class","search_spare");
			}
		}
	})
	dealDom(spanId);
}

//从多选框中选择的查询条件的x删除
function deleteFilterFromQ(spanId,divId,targetJQ){
	var val=targetJQ.parent().text();
	targetJQ.parent().remove();
	//同时移除上面的选中的过滤条件
	$("#"+spanId).find("span").each(function(){
		if($(this).text()==val){
			$(this).remove();
		}
	});
	dealDom(spanId);
}

/**
 *订单管理-修改
 */

//点击更多操作
function switchTip1(obj){
	//清空历史勾选项
	$(".main_content").find("li>em").each(function (){
		if(this.className.indexOf("selected_box") !=-1){
			this.className = "item_icon";
		}
	})
	$(obj).next();
	var parentNode = $(obj).parent();
	var divTip = parentNode.find(".main_container")[0];
	var displayCss =  divTip.currentStyle ? divTip.currentStyle['display'] : document.defaultView.getComputedStyle(divTip,false)['display'];
	if(displayCss != 'none'){
		divTip.style.display = 'none';
	}else{
		divTip.style.display = 'inline-block';
	}
}
/**
 * 条件重置
 */
function resertFilter(){
	$(".groupSearch").find("input").each(function(){
		$(this).val("");
	})
	$(".provider_input.font_12").each(function(){
		$(this).empty();
	})
	$(".item_icon").removeClass("selected_box");
}
/**
 * 下单时间的排序
 */
/*$(document).ready(function(){
	var time=1;//1升0降
	var date=1;
	$("#downOrUp").bind("click",function(e){
		var targetJQ = $(getEvent());
		if(targetJQ[0].id=="orderTime"||targetJQ[0].id=="orderDate"){
			targetJQ.find("em").each(function(){
				if(this.className.indexOf("rank_up_checked")>-1){
					//说明此次点击降续
					targetJQ.find("em :first").attr("class","t1_2 rank_up");
					targetJQ.find("em :first").next().attr("class","t1_2 rank_down_checked");
					targetJQ.siblings().find("i").children(":first").attr("class","t1_2 rank_up");
					targetJQ.siblings().find("i").children(":first").next().attr("class","t1_2 rank_down");
					if(targetJQ[0].id=="orderTime"){
						time=0;
					}
					if(targetJQ[0].id=="orderDate"){
						date=0;
					}
					//调用后台
					return false;
				}
				if(this.className.indexOf("rank_down_checked")>-1||(this.className.indexOf("rank_up_checked")<0&&this.className.indexOf("rank_down_checked")<0)){
					//说明此次点击升续
					targetJQ.find("em :first").attr("class","t1_2 rank_up_checked");
					targetJQ.find("em :first").next().attr("class","t1_2 rank_down");
					targetJQ.siblings().find("i").children(":first").attr("class","t1_2 rank_up");
					targetJQ.siblings().find("i").children(":first").next().attr("class","t1_2 rank_down");
					if(targetJQ[0].id=="orderTime"){
						time=1;
					}
					if(targetJQ[0].id=="orderDate"){
						date=1;
					}
					return false;
				}

			})
		}
		if(getEvent().nodeName=="EM"){

			if(targetJQ[0].className.indexOf("rank_down")>-1){
				//说明此次点击降续
				targetJQ.prev().attr("class","t1_2 rank_up");
				targetJQ.attr("class","t1_2 rank_down_checked");
				targetJQ.parent().parent().siblings().find("i").children(":first").attr("class","t1_2 rank_up");
				targetJQ.parent().parent().siblings().find("i").children(":first").next().attr("class","t1_2 rank_down");
				if(targetJQ.parent().parent()[0].id=="orderTime"){
					time=0;
				}
				if(targetJQ.parent().parent()[0].id=="orderDate"){
					date=0;
				}
			}
			if(targetJQ[0].className.indexOf("rank_up")>-1){
				//说明此次点击升续
				targetJQ.attr("class","t1_2 rank_up_checked");
				targetJQ.next().attr("class","t1_2 rank_down");
				targetJQ.parent().parent().siblings().find("i").children(":first").attr("class","t1_2 rank_up");
				targetJQ.parent().parent().siblings().find("i").children(":first").next().attr("class","t1_2 rank_down");
				if(targetJQ.parent().parent()[0].id=="orderTime"){
					time=1;
				}
				if(targetJQ.parent().parent()[0].id=="orderDate"){
					date=1;
				}
			}

		}
		//此处添加调用后台的ajax //貌似还得传递其他参数，比如标签页
	})

})*/
/**
 * 订单管理-修改页面的标签切换
 */
function setTabForOrderManage(index){
	//group_nav_child_active
	$(event.target).parent().find("span").each(function(){
		if($(this).is(".group_nav_child")){
			this.className="group_nav_child";
			$(event.target).attr("class","group_nav_child group_nav_child_active");
		}
	});
}
/**
 * 订单管理-修改页面的多选框确定按钮
 */
function  getEle2(obj,flag) {
	$(obj).parents(".relative.inline").find("#searchByOffice").empty();
	var lis = obj.parentNode.parentNode.getElementsByTagName("ul")[0].getElementsByTagName("li");
	//获取此搜索条件的常用条件
	var commonCon = [];
	var commonConP = $(obj).parent().parent().parent().find("p").text();
	$(obj).parent().parent().parent().find("p>span").each(function () {
		commonCon.push($(this).text());
	});
	for (var i = 0; i < lis.length; i++) {
		//判断是否选中

		//判断是否选中
		if (lis[i].getElementsByTagName("em")[0].className.indexOf("selected_box") != -1) {
			var val = lis[i].getElementsByTagName("span")[0].innerHTML;
			//modify by wlj at 2016.10.20 for 538-start
			//此处需要取得隐藏的id
			var data_id=lis[i].getElementsByTagName("span")[0].getAttribute("data-id");
			//判断下是否从多选框选择的在常用条件中已存在，若存在，则将其设为选中状态
			if (commonCon.indexOf(val) != -1) {
				/*commonConP.each(function () {
					/!*if ($(this).text() == val) {
						$(this).attr("class", "search_spare groupHomeSearch_down_active")
					}*!/
				})*/
			} else {//若不存在，则进行添加操作
				var emphasis = document.createElement("em");
				emphasis.setAttribute('class', 't1_2');
				var spanEle = document.createElement("span");
				spanEle.setAttribute('class', 'groupHomeSearch_right_child');
				spanEle.setAttribute('data-id', data_id);
				spanEle.innerHTML = val;
				spanEle.appendChild(emphasis);
				$(obj).parent().parent().parent().find("p")[0].appendChild(spanEle);
			}

		}
		closeTip(obj);
	}
	addEleDleClick();
}

function addEleDleClick() {
	$("em.t1_2").each(function(index, obj) {
		$(obj).click(function() {
			var officeName = $.trim($(obj).parent().text());
			$("span.item_text").each(function(i, o) {
				if ($(this).text() == officeName) {
					$(this).parent().find("em.selected_box").removeClass("selected_box");
				}
			});
		});
	});
}

//订单管理-修改的多选框的删除按钮
$(document).ready(function(){
	$("#groupSearch").bind("click",function(e){
		var targetJQ=$(getEvent());
		var conId=targetJQ.parent().parent().parent().parent().attr("id");
		var conClass=targetJQ.parent();
		if(conId&&conClass.is(".groupHomeSearch_right_child")){
			//if(conId=="supStorm"){//说明是删除已选择的供应商
				targetJQ.parent().remove();
			//}
		}
	})
});

//保存密码
function save_password(){
	var new_password=$("#new_password").val();
	var confirm_password=$("#confirm_password").val();
	if(new_password!=confirm_password){
		$(".confirm_password").show();
		$("#confirm_password").addClass("input_active")
	}else{
		$(".confirm_password").hide();
		$("#confirm_password").removeClass("input_active")
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
function close_parent(obj){
	$(obj).parent().hide();
}
//基本信息页，保存
function save_input(obj){
	var $this=$(obj);
	var text=$this.prev().val();
	if(text==""){
		return;
	}
  	$this.parent().parent().siblings().find(".channel_information_distance").text(text);
}
function save_select(obj){
	var $this=$(obj);
	var select1=$("#belongsArea option:selected").text();
	var select2=$("#belongsAreaProvince option:selected").text();
	var select3=$("#belongsAreaCity option:selected").text();
	if(select1=='国家'){
		return;
	}
	if(select2=='省（直辖市）'){
		select2="";
	}
	if(select3=='市（区）'){
		select3="";
	}
	$this.parent().parent().siblings().find(".channel_information_distance").text(select1+select2+select3);
}
function save_select_input(obj){
	var $this=$(obj);
	var select1=$("#agentAddress option:selected").text();
	var select2=$("#agentAddressProvince option:selected").text();
	var select3=$("#agentAddressCity option:selected").text();
	var input=$("#road").val();
	if(select1=='国家'){
		return;
	}
	if(select2=='省（直辖市）'){
		select2="";
	}
	if(select3=='市（区）'){
		select3="";
	}
	$this.parent().parent().siblings().find(".channel_information_distance").text(select1+select2+select3+input);
}
function save_number(obj){
	var $this=$(obj);
	var front=$this.siblings().children(".min_input").val();
	var after=$this.siblings().children(".min_right_input").val();
	if(front==""){
		return;
	}
	if(after==""){
		return;
	}
	$this.parent().parent().siblings().find(".channel_information_distance").text(front+"-"+after);
}

/**
 * 解析后台额条件格式，将前台的条件按钮选中展示
 */
$(document).ready(function(){
	var jsonStr='[{"出发城市":[{"id":"21","name":"乌鲁木齐"},{"id":"1","name":"北京"}]},{"目的地":[{"id":"200025","name":"华盛顿"},{"id":"200028","name":"旧金山"}]},{"供应商":[{"id":"1","name":"接待社内部"},{"id":"8","name":"LINA"}]},{"出团日期":[{"id":"2016-06-23-2016-06-23","name":"2016-06-23-2016-06-23"}]},{"行程天数":[{"id":"d1","name":"1天"},{"id":"d3","name":"3天"}]},{"价格区间":[{"id":"p0","name":"3000元以下"}]},{"余位":[{"id":"f0","name":"10以下"}]}]';
	if(typeof JSON == 'undefined') {
		$('head').append($("<script type='text/javascript' src='../json/json2.js'>"));
	}
	// var jsonObj=jQuery.parseJSON( jsonStr );
	var jsonObj=JSON.parse(jsonStr);
	if(jsonObj&&jsonObj.length>0)
		for(var i=0,j=jsonObj.length;i<j;i++){
			for (var param in jsonObj[i]){
				switch (param) {
					case "出发城市":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_sc", "startCity", _startCity);
						break;
					case "目的地":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_ec", "endCity", _startCity);
						break;
					case "供应商":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_su", "groupHomeSearch_down_border", _startCity);
						break;
					case "出团日期":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_date", "startDate", _startCity);
						break;
					case "行程天数":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_day", "tourDays", _startCity);
						break;
					case "价格区间":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_price", "priceRange", _startCity);
						break;
					case "余位":
						var _startCity=jsonObj[i][param];
						setChecked("limit_container_seat", "remainSeat", _startCity);
						break;
				}
			}
		}

})

function setChecked(spanId,divId,Obj){
	//首先 将该div的全部标签去除选中，并将上面的span显示
	$("#"+divId).find("p>span").first().attr("class","search_spare");
	var _upHtml="";
	$("#"+spanId).append();
	//先获得div下面的常用标签，判断需要设定额标签是否在此之内，如果在，设为选定；若不在，添加新标签，并设为选中
	Obj.name;
	var objName=[];
	for(var i=0,j=Obj.length;i<j;i++){
		objName.push(Obj[i].name);
	}
	var _html="";
	var exitText=[];
	$("#"+divId).find("p>span").each(function(){
		exitText.push($(this).text());
	})
	//需要新加的
	var newCondition=[];
	var nowCondition=[];
	if(objName.length>0){
		for(var i=0;i<objName.length;i++){
			if(exitText.indexOf(objName[i])>-1){
				nowCondition.push(objName[i]);
			}else{
				newCondition.push(objName[i]);
			}
			_upHtml+='<span class="groupHomeSearch_right_child">'+objName[i]+'<em class="t1_2"></em></span>';
		}
	}
	for(var i=0;i<nowCondition.length;i++){
		$("#"+divId).find("p>span").each(function(){
			if($(this).text()==nowCondition[i]){
				$(this).attr("class","groupHomeSearch_right_child");
			}
		})
	}
	for(var i=0;i<newCondition.length;i++){
		_html+='<span class="groupHomeSearch_right_child">'+newCondition[i]+'<em class="t1_2"></em></span>';
	}
	if(["startCity","endCity","groupHomeSearch_down_border"].indexOf(divId)>-1){
		$("#"+divId).find("p").append(_html);
	}
	$("#"+spanId).append(_upHtml);
	dealDom(spanId);

}

//导航状态滚动
function nav_p_right(){
	$("#inlineStatus").val("rigth");
	$("[name=setTab01]").animate({
		margin:"0 0 0 -370px"
	},"slow");
	$(".nav_p_right").addClass("nav_p_right_none");
	$(".nav_p_left").removeClass("nav_p_left_none");
}
function nav_p_left(){
	$("#inlineStatus").val("left");
	$("[name=setTab01]").animate({
		margin:"0"
	},"slow");
	$(".nav_p_left").addClass("nav_p_left_none");
	$(".nav_p_right").removeClass("nav_p_right_none")
}




