// JavaScript Document
var sysCtx;
$(function(){
	
	//通知信息数字
	//noticeNum();
	//约签
	$('.head-contract dd').click(function(){
		if($(this).parent().hasClass('head-contract-on')){
			$(this).siblings().animate({width:"0px"},function(){$(this).parent().removeClass('head-contract-on');}).hide(10);
			
		}else{
			$(this).siblings().show().animate({width:"320px"});
			$(this).parent().addClass('head-contract-on');
			
		}
		
	});
	$('.head-contract dt i').click(function(){
		$(this).parent().animate({width:"0px"},function(){$(this).parent().removeClass('head-contract-on');}).hide(10);
	});
	//右侧头部二级菜单
	//子菜单位置设置
	ernav();
	$(window).resize(function(){
		
		ernav();
	});
	
	function ernav(){
		$("li.ernav").each(function(index, element){
			var $li = $(element);
			var $dl = $li.children("dl");
			var $dt = $dl.find("dt");
			var uiLeft=$li.parents("ul.nav-tabs").offset().left
			var theLeft = $li.offset().left-uiLeft;
			var dtWidth=0;	
			$dt.each(function(i,e){
				var dtWidths=$(e).width();
				dtWidth+=dtWidths;
			});
			var thePaddingLeft =theLeft+($li.width()-dtWidth)/2;
			var paddingLeft_dl = parseInt($dl.eq(0).css("padding-left").replace(/px/g,''));
			if(0 > thePaddingLeft){
				thePaddingLeft = 40;
			}else{
				var condition = $(".main-right").eq(0).width() - thePaddingLeft;
				if(condition < dtWidth){
					thePaddingLeft -= dtWidth - condition;
				}
			}
			$dl.css({'padding-left':thePaddingLeft});
		});
	}
	//鼠标移入移除设置
	$('.nav-tabs li').hover(function(){
		$('.nav-tabs li').removeClass('current');
		$(this).addClass('current');
		if($(this).hasClass('ernav')){
			 $(this).parent().addClass('hasNav');
		 }else{
			 $(this).parent().removeClass('hasNav');
		 }
	},function(){
		$('.nav-tabs li').removeClass('current');
		$(this).parent().removeClass('hasNav');
		var _active = $(".nav-tabs li.active").eq(0);
		if(_active.hasClass('ernav')){
			_active.addClass('current');
			$(this).parent().addClass('hasNav');
		}
	});

	if ($(".ofAnchor").length) {
		var $anchorNav = $(".ofAnchor");
		var anchorNav_top = $anchorNav.offset().top;
		var ofAnchor_height = $('.ofAnchor').height();
		//设置快速定位导航ul的宽度
		var $ofAnchor_li = $('.ofAnchor').find("li");
		var ofAnchor_li_width = $ofAnchor_li.eq(0).width() + parseInt($ofAnchor_li.eq(0).css("margin-right").replace(/\D/g, "")) + parseInt($ofAnchor_li.eq(0).css("margin-left").replace(/\D/g, "")) + parseInt($ofAnchor_li.eq(0).css("padding-left").replace(/\D/g, "")) + parseInt($ofAnchor_li.eq(0).css("padding-left").replace(/\D/g, ""));
		$('.ofAnchor').width(ofAnchor_li_width * $ofAnchor_li.length).parent().height(ofAnchor_height);

		//点击定位
		$('.ofAnchor').find('a').click(function() {
			$('.ofAnchor').find('a').removeClass('anchorOn');
			$(this).addClass('anchorOn');
			var toTop = $('#' + $(this).attr("data-for")).offset().top;
			try {
				document.body.scrollTop = toTop - ofAnchor_height + 1;
				document.documentElement.scrollTop = toTop - ofAnchor_height + 1;
			} catch (e) {
				document.documentElement.scrollTop = toTop - ofAnchor_height + 1;
			}
		});
		//页面滚动
		$(window).scroll(function() {
			var theScrollTop = document.documentElement.scrollTop + document.body.scrollTop;
			if (theScrollTop > anchorNav_top) {
				$anchorNav.addClass('ofAnchor-fixed');
			} else {
				$anchorNav.removeClass('ofAnchor-fixed');
			}
			var pageScrollTop = document.documentElement.scrollTop || document.body.scrollTop;
			$("[id^='ofAnchor']").each(function(index, item) {
				var _top = $(this).offset().top - ofAnchor_height;
				if (_top <= pageScrollTop) {
					$anchorNav.find("a").removeClass("anchorOn");
					$anchorNav.find("a").eq(index).addClass("anchorOn");
				}
			});
			//水平滚动条滚动
			$anchorNav.css("left", $anchorNav.parent().offset().left - parseInt($(this).scrollLeft()) + "px");
			//判断滚动条是否到达底部
			//var scrollTop = $(this).scrollTop();
			//			var scrollHeight = $(document).height();
			//			var windowHeight = $(this).height();
			//			if(scrollTop + windowHeight == scrollHeight){
			//				$anchorNav.find("a").removeClass("anchorOn");
			//				$anchorNav.find("a:last").addClass("anchorOn");
			//			}
		});
		//窗口变换大小
		$(window).resize(function() {
			$anchorNav.css("left", $anchorNav.parent().offset().left - parseInt($(this).scrollLeft()) + "px");
		});
		$(window).trigger("scroll");
	}
	sysCtx = $("#sysCtx").val();
});
/*通知信息数字 备注：这段代码在wholesaler.jsp 中
function noticeNum(){
	var iNumb=$('.head-notice strong').text();
	if(iNumb==''){
	}else{
		var str_html = '';
		if(iNumb.length > 2){
			str_html +='<i class="iNumb9"></i><i class="iNumb9"></i><i class="iNumbAdd"></i>';
		}else{
			for(var i=0;i<iNumb.length; i++){
				str_html += '<i class="iNumb'+ iNumb[i] +'"></i>';
			}
		}
		
		if($('.head-notice b').length==0){
			$('.head-notice strong').before('<b>'+str_html+'</b>');
		}else{
			$('.head-notice b').html(str_html);
		}
	}
	
}*/

//展开筛选按钮
function launch(){
	$('.zksx').click(function() {
		if($('.ydxbd').is(":hidden")==true) {
			$('.ydxbd').show();
			$(this).text('收起筛选');
			$(this).addClass('zksx-on');
		}else{
			$('.ydxbd').hide();
			$(this).text('展开筛选');
			$(this).removeClass('zksx-on');
		}
	});
}

//产品名称获得焦点显示隐藏
function inputTips(){
	$("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	if($("input[flag=istips]").val()!=""){
		$("input[flag=istips]").next("span").hide();
	}
	$(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}

//产品名称获得焦点显示隐藏--动态添加的元素
function inputTipsDynamic(parentObj){
	parentObj.find("input[flag=istips]").focusin(function(){
		var obj_this = $(this);
		obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!=""){
			obj_this.next("span").hide();
		}else{
			obj_this.next("span").show();
		}
	});
	parentObj.find("input[flag=istips]").each(function(index, element) {
        if($(element).val()!=""){
			$(element).next("span").hide();
		}
    });
	parentObj.find(".ipt-tips").click(function(){
		var obj_this = $(this);
		obj_this.prev("input").focus();
	});
}

//接待社回复价 底部添加
function tianjia(){
		$('.ydbz_s').click(function() {		
			$(this).parents('.wbyu').find('dl:last').after("<dl class='wbyu-bot'><dt><label>其它：</label><input name='' type='text' /></dt><dt><label>价格：</label><input class='rmbp17' type='text' />元/人 <input class='seach_shortinput' type='text' />人</dt><dd class='ydbz_s gray clear-btn'>删除</dd></dl>");
			$('.clear-btn').click(function(){$(this).parent().remove();});
		});
		
}

//改价记录 底部增加
function gaijia(){
		$('.gai-price-btn').click(function() {		
			$(this).parents('.gai-price').find('.gai-price-nr:last').after("<dl class='gai-price-nr'><dt><label>款项：</label><input type='text' name=''></dt><dd><select name=''><option value=''>人民币</option></select></dd><dt><label>费用：</label><input type='text' class='rmb' name=''></dt><dt><label>备注：</label><input type='text' name=''></dt><dd><a class='ydbz_s gray clear-btn'>删除</a></dd></dl>");
			$('.clear-btn').click(function(){$(this).parents('.gai-price-nr').remove();});
		});
		
}
//返佣 底部增加
function fanyong(){
		$('.fan-price-btn').click(function() {		
			$(this).parents('.gai-price').find('.gai-price-nr:last').after("<dl class='gai-price-nr'><dt><label>返佣名称：</label><input type='text' name=''></dt><dd><select name=''><option value=''>人民币</option></select></dd><dt><label>费用：</label><input type='text' class='rmb' name=''></dt><dt><label>备注：</label><input type='text' name=''></dt><dd><a class='ydbz_s gray clear-btn'>删除</a></dd></dl>");
			$('.clear-btn').click(function(){$(this).parents('.gai-price-nr').remove();});
		});
		
}
//发布产品3步-添加其他文件
function addfile(obj){
	var file = "<div style=\"margin-top:8px;\">"+
				$("#othertemplate").clone().html()+                                 
				"</div>";
	
	$(obj).parent().parent().append(file);
}
//发布产品3步-添加签证资料
function addvisafile(obj){
	var html = 
	"<div id=\"visafile\" class=\"mod_information_d8_2\" style=\"margin-top:5px;\">"+$("#signtemplate").clone().html()+"</div>";
	$("#otherflag").prev().prev().after(html);

	//$("#thirdStepDiv .mod_information_d8_2 select[name='country']").combobox();
}

//新增询价第一到第二
    function oneToTwo(){
	   $('.inquiry_box1').hide();
	   $('.inquiry_box2').show();
	   $('.inquiry_num').addClass('inquiry_num2')
	}
//新增询价第二到第三
	function TwoToThree (){
	  $('.inquiry_box2').hide();
	  $('.inquiry_box3').show();
	  $('.inquiry_num').addClass('inquiry_num3');
	   inquiry_box3_check(); inquiry_radio_flights();
	 }
//新增询价第三到第二
	function ThreeToTwo(){
	   $('.inquiry_box3').hide();
	   $('.inquiry_box2').show();
	   $('.inquiry_num').removeClass().addClass('inquiry_num inquiry_num2')
		}
//新增询价第二到第一
	function TwoToOne(){
	   $('.inquiry_box2').hide();
	   $('.inquiry_box1').show();
	   $('.inquiry_num').removeClass().addClass('inquiry_num');
		}
	// 机票询价，第一到第三
	function oneToThree(){
		 $('.inquiry_box1').hide();
		 $('.inquiry_box3').show();
		 $('.inquiry_num').addClass('inquiry_num2');
		 inquiry_box3_check();
	}
	// 机票询价：第三道第一
	function ThreeToOne(){
		 $('.inquiry_box3').hide();
		 $('.inquiry_box1').show();
		 $('.inquiry_num').removeClass().addClass('inquiry_num_ticket inquiry_num');
//		 $('.inquiry_num').addClass('inquiry_num');
	}
//是否申请机票
	function inquiry_box3_check(){
		if($("input[name='isAppFlight']").prop("checked")){
			$('.inquiry_box3_check').show();
			}else{
			$('.inquiry_box3_check').hide();
			}
		}
//询价客户类型
	function inquiry_radio_peoples(){
		if($("#inquiry_radio_people3").prop("checked")){
			 $('.inquiry_radio_people1').hide();
			 $('.inquiry_radio_people2').hide();
			 $('.inquiry_radio_people3').show();
			}else if($("#inquiry_radio_people2").prop("checked")){
			 $('.inquiry_radio_people1').hide();
			 $('.inquiry_radio_people3').hide();
			 $('.inquiry_radio_people2').show();
			}
			else if($("#inquiry_radio_people").prop("checked")){
			 $('.inquiry_radio_people2').hide();
			 $('.inquiry_radio_people3').hide();
			 $('.inquiry_radio_people1').show();
				}
		}
//机票种类
	function inquiry_radio_flights(){
		if($("#inquiry_radio_flights1").prop("checked")){
			 $('.inquiry_flights1').show();
			 $('.inquiry_flights2').hide(); $('.inquiry_flights3').hide();
			}else if($("#inquiry_radio_flights2").prop("checked")){
				 $('.inquiry_flights2').show();
			 $('.inquiry_flights1').hide(); $('.inquiry_flights3').hide();
			}else if($("#inquiry_radio_flights3").prop("checked")){
			 $('.inquiry_flights3').show();
			 $('.inquiry_flights1').hide(); $('.inquiry_flights2').hide();
			}
		}
//询价添加询价要求
 function inquiry2AddIpt(){
	  $(".seach100").delegate(".inquiry2AddIpt","click",function(){
                $(this).parent().before('<div class="seach25 seach100 pr jd-xs"><p>&nbsp;</p><input type="text" class="seach_longinput" onblur="inquiry2AddOut(this)" onfocus="inquiry2AddFoc(this)"><span class="ipt-tips">填写新要求</span><a onclick="inquiry2DelIpt(this)">删除</a></div>')
            });
	 }
	 function inquiry2DelIpt(obj){
		 $(obj).parent().remove();
		 }
	//得到焦点事件：隐藏填写费用名称提示
	function inquiry2AddFoc(obj) {
	    $(obj).siblings(".ipt-tips").hide();
	}
	//失去焦点事件：如果输入框中没有值,显示提示信息
	function inquiry2AddOut(obj){
	    var obj = $(obj);
	    if(!obj.val()){
	        obj.siblings(".ipt-tips").show();
	    }
	}
//成本价滑过显示具体内容
  function inquiryPriceCon(){
	   $('.Inquiry_c').hover(function(){
		   $(this).find('.inquiry_mouse').show();
		 },function(){
		    $(this).find('.inquiry_mouse').hide();
		 })
	  }	    
		 //新增第二步增加删除询价要求
		 function inquiryTwoText(){
            $(".seach100").delegate(".inquiry_zxdel","click",function(){
				
/*				 $(this).parent().prev().addClass('seach_labelnow')
				 $(this).parent().parent().find('a').show();*/
				 $(this).parent().remove();
            });
		 }
		//新增第二步添加复选条件
		/*function inquiryAddSeachcheck(){
			$(".add_seachcheck").click(function(){
				$(this).prev().removeClass('seach_labelnow')
				var inputName=$(this).prev().find('input').attr('name');
    $(this).before('<span class="seach_check"><input type="radio" name='+inputName+'><input type="text"><a class="inquiry_zxdel">删除</a></span>')
	$(this).parent().find('.add_seachcheck').hide();
		})
	  }*/
	  
//新增询价多选计调和线路国家
function  inquiryCheckBOX(){
$(".seach_checkbox").on("click","em",function(){
	$(this).parent().remove();
})
$(".seach_checkbox").on("hover","a",function(){
	$(this).append("<em></em>")
})
$(".seach_checkbox").on("mouseleave","a",function(){
	$(this).parent().find('em').remove();
})
}
//各块信息展开与收起
function  boxCloseOn(){
	$(".closeOrExpand").click(function(){
		var obj_this = $(this);alert(obj_this.attr("class"));
		if(obj_this.attr("class").match("ydClose")) {
			obj_this.removeClass("ydClose");
			obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
		} else {
			obj_this.addClass("ydClose");
			obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
		}
	});
}

/*//上传 动作
function btfile(){
	$('.input-file').change(function(){
		var inputval=$(this).val();
		$(this).siblings('input[type=text]').val(inputval);								 
	});
	$('.sc-chuan').click(function(){				 
		$(this).siblings('.input-file').click();	
	});

}*/
//上传 动作
function btfile(){
	$('.bgMainRight').delegate(".sc-chuan","click",function(){
		$(this).siblings('.input-file').click();		
		})				 
	$('.bgMainRight').delegate(".input-file","change",function(){
		var inputval=$(this).val();
		$(this).siblings('input[type=text]').val(inputval);	
     })						 
}
/*联运*/
function transportchg(){
	var value=$("#intermodalType option:selected").attr("id");
	if("none" == value){
		$('#nationalTrans').hide();
		$('#groupTrans').hide();
//                $('#intermodalType').parent().next().show();
//                $('#intermodalType').parent().next().next().hide();
	} else if ("group" == value){
		$('#nationalTrans').hide();
		$('#groupTrans').show();
//                $('#intermodalType').parent().next().hide();
//                $('#intermodalType').parent().next().next().show();
	} else if("national" == value){
		$('#nationalTrans').show();
		$('#groupTrans').hide();
//                $('#intermodalType').parent().next().hide();
//                $('#intermodalType').parent().next().next().hide();
	}else{
		$('#nationalTrans').hide();
		$('#groupTrans').hide();
	}
	

}
//预定第一步添加信息
function yd1AddPeople(obj){
	var contactPeopleNum = $("ul[name=orderpersonMes]").length;
	$('#ordercontact').append('<ul class="ydbz_qd" name="orderpersonMes">'+
		'<li><label><span class="xing">*</span>渠道联系人<font>' + (contactPeopleNum+1) +'</font>：</label><input maxlength="10" type="text" name="contactsName" value="" class="required" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>'+
		'<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input maxlength="15" type="text" name="contactsTel" value="" class="required" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"/><div class="zksx boxCloseOnAdd" onclick="boxCloseOnAdd(this,\'\',\'2\')">展开全部</div><span class="ydbz_x gray" onclick="yd1DelPeople(this)">删除联系人</span></li>'+
		'<li flag="messageDiv" style="display:none" class="ydbz_qd_close"><ul>'+
		'<li><label>固定电话：</label><input type="text" name="contactsTixedTel"/></li>'+
		'<li><label>联系人地址：</label><input type="text" name="contactsAddress"/></li>'+
        '<li><label>传真：</label><input maxlength="" type="text" name="contactsFax"/></li>'+
        '<li><label>QQ：</label><input maxlength="" type="text" name="contactsQQ"/></li>'+
        '<li><label>Email：</label><input maxlength="" type="text" name="contactsEmail"/></li>'+
        '<li><label>邮编：</label><input maxlength="" type="text" name="contactsZipCode"/></li>'+
        '<li><label>其他：</label><input maxlength="" type="text" name="remark"/></li>'+
        '</ul></li></ul>')
}
/* function yd1AddPeople(obj){
	 $(obj).parent().parent().append(' <li class="ydbz_qd_lilong"><span class="pr jd-xs"><label>添加名称：</label><input type="text" onfocus="inquiry2AddFoc(this)" onblur="inquiry2AddOut(this)"/><span class="ipt-tips">例如Email</span></span><span class="pr jd-xs"><label>添加内容：</label><input type="text" onfocus="inquiry2AddFoc(this)" onblur="inquiry2AddOut(this)"/><span class="ipt-tips">123@trekiz.com</span></span><span class="ydbz_x gray yd1AddPeople" onclick="yd1DelPeople(this)">删除</span></li>')
	 }*/
//预定第一步删除信息
function yd1DelPeople(obj){
	$(obj).parent().parent().remove();
	//重置联系人序号
	$("ul[name=orderpersonMes]").each(function(index, element) {
        $(element).children("li").eq(0).find("font").text(index+1);
    });
   
}
//预定第二步是否联运
function ydbz2intermodal(obj){
	if($(obj).val()==0){
		    $(obj).parent().parent().find('span').hide();
		} else{
			$(obj).parent().parent().find('span').show();
		}
	
	if ($.isFunction(window.lyChange)){
		lyChange(obj);
 	}	
}

function ydbz2interradio(){
    var obj=$('.tourist-t-r');
	$(obj).each(function() {
	var value=$(this).find('input:checked').val();
	  if("1" == value){
                $(this).find('span').show();
            } else{
                 $(this).find('span').hide();
            }
    });
	}
/*预定第二步联运显示价格*/
function ydbz2interselect(obj){
	var value=$(obj).find("option:selected").val();
	$(obj).parent().find('em').html(value);
	}
	
//预定第二步签证类型为其他时,show
function ydbz2CardSelChgs(obj){
	var value=$(obj).find("option:selected").val();
	if("100" == value){
		$(obj).parent().next().show();
	} else{
		$(obj).parent().next().hide();
	}
}
//function ydbz2CardSel(){
//	var obj=$('.ydbz2CardSelChg');
//	$(obj).each(function() {
//		var value=$(this).find('option:selected').val();
//		if("100" == value){
//			$(this).parent().next().show();
//		} else{
//			$(this).parent().next().hide();
//		}
//    });
//}
//预定第二步自备签
function ydbz2zibeiqian(){
	$("#traveler").on("click","input[name=zibeiqian]",function(){
		var $this = $(this);
		var $siblingsCkb = $this.parents(".tourist-ckb").children("input[type=checkbox]");
		var thisIndex = $siblingsCkb.index($this);
		var $tips = $this.parents(".ydbz_tit_child").siblings(".zjlx-tips").eq(0);
		if($this.attr('checked')) {
			if(!$tips.is(":visible")) {
				$tips.show();
			}
			$tips.children("ul").eq(thisIndex).show();
		} else {
			$tips.children("ul").eq(thisIndex).hide(500,function() {
				var isshow = 0;
				$tips.children("ul").each(function(index, element) {
					if($(element).is(":visible")){
						isshow++;
					}
				});
				if(0 == isshow) {
					$tips.hide();
				}
			});
			
		}
	});
}
//预定步骤展开收起模块
function boxCloseOnAdd(obj,val,text){
    var obj_this = $(obj);
    if(obj_this.attr("class").match("boxCloseOnAdd")) {
        obj_this.removeClass("boxCloseOnAdd");
        obj_this.parent().nextAll("[flag=messageDiv]").eq(0).show();
        if(text==2){
            obj_this.text("收起全部")
        }
        if(val==1){
            obj_this.parent().find('.tourist-t-off').css("display","none");
            obj_this.parent().find('.tourist-t-on').show();
        }
    } else {
        obj_this.addClass("boxCloseOnAdd");
        obj_this.parent().nextAll("[flag=messageDiv]").eq(0).hide();
        if(text==2){
            obj_this.text("展开全部");
        }
        if(val==1){
            obj_this.parent().find('.tourist-t-off').css("display","inline-block");
            obj_this.parent().find('.tourist-t-on').hide();
        }
    }
}

/*预定第二步上传资料*/
	function ydbz2interfile(obj){
			var dest = $(obj).parent().find("span");
    			var res = $(obj).val();      		
        		$(dest).html(res);
				var ishave=$(obj).parent().find('.visaImg');
				if(ishave.length>0){
					$(obj).parent().find('.visaImg').html('<img width="300" onclick="showBigImg(\'images/body-bg.jpg\')" title="点击查看大图" src="images/body-bg.jpg">')
					}else{
						$(obj).parent().append('<div class="visaImg"><img width="300" onclick="showBigImg(\'images/body-bg.jpg\')" title="点击查看大图" src="images/body-bg.jpg"></div>')
						}
    	}	
//发布单团产品--添加团期和价格--币种选择
function selectCurrency(){
	//单个设置币种
	$(".choose-currency").hover(function(){
		$(this).addClass("choose-currency-on");
	},function(){
		$(this).removeClass("choose-currency-on");
	}).on("click","dd p",function(){
		var $this = $(this);
		if(!$this.hasClass("p-checked")){
			var oldCurrency = $this.siblings("p.p-checked").text();
			var oldClass = $this.siblings("p.p-checked").attr("addClass");
			$this.addClass("p-checked").siblings("p").removeClass("p-checked");
			//设置币种/人
			var $currency = $this.parents(".add2_nei_table").next("td").find(".currency").eq(0);
			if(0 != $currency.length){
				var txt_currency = $this.text() == "人民币" ? "元" : $this.text();
				if("人民币" == oldCurrency){oldCurrency = "元";}//alert(oldCurrency);
				$currency.html($currency.html().replace(oldCurrency,txt_currency));
			}
			//设置币种图标
			var $input_currency = $this.parents(".add2_nei_table").next("td").find(".ipt-currency").eq(0);
//			$input_currency.removeClass(oldClass).addClass($this.attr("addClass"));
			$input_currency.prev("span").remove().before("<span>" + $this.attr("addClass") + "</span>");
			//设置对应的select的选中项
			var $select = $this.parents("td.add2_nei_table").find("select");
			$select.find("option:selected").removeAttr("selected");
			$select.children("option[value=" + $this.attr("value") + "]").attr("selected",true);
		}
	});
	//统一设置币种
	$("#selectCurrency").change(function(){
		var theValue = $(this).val();
		$.each($(".choose-currency"),function(index,element){
			var $this_dl = $(element);
			$this_dl.find("dd p[value=" + theValue + "]").click();
		});
	});
}
//展开关联机票产品信息
function showAirInfo(dom) {
		var $this = $(dom);
		var $thisParent = $this.parents("tr");
		var $airInfo;
		if ($thisParent.length == 0) {
			$airInfo = $this.parents(".mod_information_d2").next();
		} else {
			$airInfo = $thisParent.next().find(".airInfo");
		}
		if ($airInfo.is(":visible")) {
			$airInfo.slideUp();
			$this.siblings(".airInfo-arrow").hide();
			$this.text($this.text().replace("收起", "展开"));
		} else {
			$airInfo.slideDown();
			$this.siblings(".airInfo-arrow").show();
			$this.text($this.text().replace("展开", "收起"));
		}
	}

//发布单团产品--基础信息填写--联运选择
function transportAdd(element, index){
	var $selectCurrency = $("#templateCurrency").clone();
	var html_selectCurrency = $selectCurrency.removeAttr("id").removeAttr("style")[0].outerHTML;
	var middle = 'onkeyup="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')" onafterpaste="this.value=this.value.replace(/[^\\d\\+\\-]/g,\'\')"';
	$(element).parent().parent().append('<p><label class="transport_city_label"><span class="xing">*</span>联运城市分区：</label><input id="intermodalGroupPart' + (index+1) + '" name="intermodalGroupPart" maxlength="50" type="text"><label>联运价格：</label>' + html_selectCurrency +'&nbsp;<input id="intermodalGroupPrice" name="intermodalGroupPrice" maxlength="8" type="text" ' + middle + ' /><span class="currency">元</span><a class="ydbz_s gray transportDel">删除</a></p>');
	$('.transportAdd').attr('onclick', 'transportAdd(this, ' + (index + 1) + ')');
}
function transportSelect(){
	$(".transport_city").delegate(".transportDel","click",function(){
		$(this).parent().remove();
	});
	//发布单团产品--填写基础信息--联运币种选择
	$(".transport_city").on("change","select[name=selectCurrency]",function(){
		var $this = $(this);
		var theValue = $this.val();
		var newCurrency = $this.children("option:selected").text();
		var $currency = $this.siblings(".currency").eq(0);
		if(0 != $currency.length){
			var txt_currency = newCurrency == "人民币" ? "元" : newCurrency;
			$currency.html(txt_currency);
		}
	});
}
//发布单团产品-交通方式-航空
function trafficchg(){
	var value=$("#trafficMode option:selected").val();
	if("1,".indexOf(value)>=0&&value!="")
		$("#trafficName").css("display","inline-block");
	else {
		$("#trafficName").css("display","none");
		$("#trafficName option[value='']").attr("selected", true);
	}
}
//发布产品-交通方式-航空-关联机票产品
function linkAirTicket(){
	var html = '<div class="add_allactivity"><label>输入机票产品编号：</label>';
	html += '<input type="text" />';
	html += '</div>';
	$.jBox(html, { title: "选择机票产品",buttons:{"提 交":"1"}, submit:function(v, h, f){
		if (v=="1"){
			h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			//如果没有机票产品
			$('<p class="nothisPro" style="display: none;">没有这个产品</p>').appendTo(h).show('slow');
			return false;
		}
	},height:180,width:500});
}
//订单-返佣记录-返佣详情-重新申请
function jbox_cxsq() {
	var submit = function (v, h, f) {
		if(v==true)
		{
			
		}
		return true;
	};

	jBox.warning("“重新申请”会取消本次返佣申请，并发起新的返佣申请。", "提示", submit, { buttons: { '取消': false, '重新申请': true} });
}
//订单-机票订单-航班备注
function jbox_hbbz(content) {
    var html = '<div style="padding:10px; text-align:center;">';
    html += '<textarea name="" cols="" rows="" style="margin:10px auto; width:90%;">'+(content==null?"":content)+'</textarea>';
    html += '</div>';
    $.jBox(html, { title: "航班备注",buttons:{'确定': true,'取消': false }, submit:function(v, h, f){
        if (v==true){

            return true;
        }
    },height:200,width:500});

	
}
//订单-计调订单-退团原因
function jbox_ttyy() {
	var html = '<div style="padding:10px; text-align:center;">';
	html += '<textarea name="" cols="" rows="" style="margin:10px auto; width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "退团原因",buttons:{'确定': true,'取消': false }, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:200,width:500});

	
}

//订单-签证订单-借款
function jbox_jk() {
	var html = '<div style="margin-top:20px;"><label style="width:90px; text-align:right; line-height:30px;">借款金额：</label>';
	html += '<input type="text" /><br /><label style="width:90px; text-align:right; line-height:30px;">备注：</label><textarea name="" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "借款",buttons:{'申请借款': true}, submit:function(v, h, f){
		if (v==true){
			jbox_jkmx();
			 return true;
		}
	},height:220,width:380});

	
}

//订单-签务身份订单-申请借款
function jbox_sqjk() {
	var html = '<div style="margin-top:20px;text-align:center;">';
	html += '<p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">姓名:刘铭</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">护照号:UHOG70934589</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">下单人:销售_张三</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">下单时间:2014-09-02 11:38:56</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">团队类型:单办签</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">收客人:李四</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">订单编号:TES347943553</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">操作人:王海，马云，腾飞</span></p><p><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:30px;"><label style="width:60px; text-align:right; line-height:30px; float:left;">借款金额：</label><input type="text" class="rmb" value="5000" style="width:70px; margin-left:10px;"></span></p><p ><span style="width:90%; line-height:180%; display:inline-block; text-align:left;overflow:hidden; height:auto;"><label style="width:60px; text-align:right; line-height:30px; float:left;">备注：</label><textarea name="" cols="" rows="" style="margin-left:10px; width:380px;"></textarea></span></p>';
	html += '</div>';
	$.jBox(html, { title: "申请借款",buttons:{'提交申请': true}, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:380,width:550});

					
}

//订单-签证订单-借款明细
function jbox_jkmx() {
	var html = '<div style="margin-top:20px;text-align:center;">';
	html += '<p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">姓名:刘铭</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">护照号:UHOG70934589</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">下单人:销售_张三</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">下单时间:2014-09-02 11:38:56</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">团队类型:单办签</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">收客人:李四</span></p><p><span style="width:45%; display:inline-block; text-align:left;overflow:hidden; height:28px;">订单编号:TES347943553</span><span style="width:45%; display:inline-block; text-align:left; overflow:hidden; height:28px;">操作人:王海，马云，腾飞</span></p><p><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:28px;">借款金额:¥5000</span></p><p ><span style="width:90%; display:inline-block; text-align:left;overflow:hidden; height:auto;">备注:</span></p>';
	html += '</div>';
	$.jBox(html, { title: "借款明细",buttons:{'确定': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			
			 return true;
		}
	},height:330,width:550});

					
}

//订单-签证订单-借护照
function jbox_jhz() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label style="width:100px; text-align:right; line-height:30px;">护照借出领取人：</label><input name="" type="text" /><br /><label style="width:100px; text-align:right; line-height:30px;">护照借出时间：</label><input name="" type="text" /><br /><label style="width:100px; text-align:right; line-height:30px;">护照借出备注：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "借护照",buttons:{'护照领取单': 1,'确认领取': 2}, submit:function(v, h, f){
		if (v=="2"){
		
			 return true;
		}
	},height:220,width:380});

	
}

//订单-签证订单-还护照
function jbox_hhz() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label style="width:100px; text-align:right; line-height:30px;">护照归还领取人：</label><input name="" type="text" /><br /><label style="width:100px; text-align:right; line-height:30px;">护照归还时间：</label><input name="" type="text" /><br /><label style="width:100px; text-align:right; line-height:30px;">护照归还备注：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还护照",buttons:{'确认归还': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:380});

	
}

//订单-签证订单-还护照
function jbox_hsj() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label style="width:90px; text-align:right; line-height:30px;">收据金额：</label>¥<span class="tdred">615</span><br /><label style="width:90px; text-align:right; line-height:30px;">收据领取人：</label><input name="" type="text" /><br />					<label style="width:90px; text-align:right; line-height:30px;">领取时间：</label><input name="" type="text" />';
	html += '</div>';
	$.jBox(html, { title: "还收据",buttons:{'签收收据': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:380});

	
}

//订单-签证订单-办签资料
function jbox_qszl() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label style="width:100px; text-align:right; line-height:30px;">资料原件：</label><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;" />护照借出</span><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;"/>身份证借出</span><br /><label style="width:100px; text-align:right; line-height:30px;">复印件：</label><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;"/>户口本</span><span style=" width:80px; display:inline-block;"><input name="" type="checkbox" value="" style="margin:0; margin-right:5px;" />房产证</span>';
	html += '</div>';
	$.jBox(html, { title: "办签资料",buttons:{'确定': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:380});

	
}

//订单-签证-销售身份-参团签证订单修改-撤签
function jbox_cq() {
	var html = '<div style="margin-top:20px; font-size:20px; text-align:center;">';
	html += '是否提交撤签申请';
	html += '</div>';
	$.jBox(html, { title: "签证已送签",buttons:{'确定': 1,'取消': 0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			$('.tourist-ckb').find('.traveler:eq(0)').removeAttr('checked');
		}
	},height:150,width:380});

	
}


//订单-签务身份订单-预约表
function jbox_yyb() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<div class="seach25 seach100" style="width:90%; padding-bottom:5px;"><p style="width:50px;">办签人：</p><p class="seach_r" style="width:auto;"><span class="seach_check"><select name="vendorOperator"><option value="0" selected="selected">办签人</option><option value="1">张三</option><option value="2">李四</option><option value="2">王五五</option><option value="2">赵紫龙</option><option value="2">田日照</option></select></span><span class="seach_checkbox" id="vendorOperatorShow"><a>张三</a></span></p></div><label style="width:50px; text-align:right; line-height:30px;">备注：</label><textarea name="" cols="" rows="" ></textarea>';
	html += '</div>';
	$.jBox(html, { title: "编辑预约表",buttons:{'确定': 1}, submit:function(v, h, f){
		if (v=="1"){
			
			 return true;
		}
	},height:260,width:380});
	$("select[name='vendorOperator']" ).comboboxInquiry({
		"afterInvalid":function(event,data){
			var Array_default = new Array("办签人");
			if(-1 == $.inArray(data,Array_default)){
				var isIncluded = 0;
				$("#vendorOperatorShow a").each(function(index, element) {
					if(data == $(element).text()){
						isIncluded = 1;
						return;
					}
				});
				if(isIncluded){
					jBox.tip("您已选择");
				}else{
					$("#vendorOperatorShow").append('<a>{0}</a>'.replace("{0}",data));
				}
			}
		}
	});
	inquiryCheckBOX();
}


//询价-销售询价记录
function jbox_xzxj(){
	var html = '<div class="add_allactivity"><label>产品类型：</label>';
	html += '<select><option>单团</option><option>机票</option><option>签证</option><option>自由行</option><option>大客户</option><option>游学</option></select>';
	html += '</div>';
	$.jBox(html, { title: "询价产品类型选择",buttons:{'提交': 1}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}
	},height:220,width:410});	
}


//运控-散拼库存切位-切位
function jbox_qw() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>您好，请选择需要切位对象  ！</p><dl style="width:300px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:inline-block;"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 内部切位</span><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" /> 渠道切位</span></dt><dd style="display:none; margin:0; float:left; width:100%;"><select name="" ><option>全部</option><option>大客户部</option><option>自由行部</option><option>游学部</option></select></dd><dd style="display:none; margin:0; float:left; width:100%;"><select name=""><option>飞扬假期</option><option>大洋国际</option></select></dd></dl>';
	html += '</div>';
	$.jBox(html, { title: "切位对象选择",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:220,width:380});
	qiewei();
}
//运控-散拼库存切位-归还切位
function jbox_ghqw() {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p>当前余位数量：30</p><dl style="overflow:hidden; padding-right:5px;"><dt style="height:30px; float:left; width:100%;"><span style="width:50%; display:inline-block;"><input name="qiewei" type="radio" style="margin:0;" value="" checked /> 内部切位</span><span style="width:50%; display:inline-block;"><input name="qiewei" style="margin:0;" type="radio" value="" /> 渠道切位</span></dt><dd style="display:none; margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 3</td><td class="tc">自由行部</td><td class="tc">10</td><td class="tc">0</td></tr></tbody></table></dd><dd style="display:none; margin:0; float:left; width:100%;"><table class="table activitylist_bodyer_table"><thead><tr><th width="25%">序号</th><th width="25%">部门</th><th width="25%">切位</th><th width="25%">已售出切位</th></tr></thead>	<tbody><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 1</td><td class="tc">飞扬假期</td><td class="tc">10</td><td class="tc">0</td></tr><tr><td class="tc"><input name="" type="radio" value="" style="margin:0;" /> 2</td><td class="tc">大洋国际</td><td class="tc">10</td><td class="tc">0</td></tr</tbody></table></dd></dl>';
	html += '<label style="width:90px; text-align:right; line-height:30px; height:30px; float:left;">归还还位数量：</label>';
	html += '<input type="text" /><br /><label style="width:100%; line-height:30px;">请填写您的还位原因！</label><textarea name="" cols="" rows="" style="width:90%;"></textarea>';
	html += '</div>';
	$.jBox(html, { title: "归还切位录入框",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
	
			 return true;
		}else{
			return true;
		}
	},height:320,width:480});
	qiewei();
}
function qiewei(){
	var qw;
	$('input[name=qiewei]').each(function(){
		if($(this).is(':checked'))
		{
			
			qw=$(this).parent().index();
			$(this).parents('dt').siblings('dd').eq(qw).show().siblings('dd').hide();
		}
	
	});
	
	$('input[name=qiewei]').click(function(){
		qw=$(this).parent().index();
		$(this).parents('dt').siblings('dd').eq(qw).show().siblings('dd').hide();
								   
	});
}

//运控-成本录入-添加项目
function jbox_tjxm(){
	var html = '<div class="costBox">';
	var $templateClone = $("#addItem").clone(false);
	//重置表单元素的id和name值
	$templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
	html += $templateClone.html();
	html += '</div>';
	$.jBox(html, { title: "成本录入",buttons:{'提交': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			var container = "";
			var tbodyFirstTdTxt = "";
			var tfootFirstTdTxt = "";
			var name = $.trim(f.name);
            var price = $.trim(f.price);
			var account = $.trim(f.account);
            var comment = f.comment;
            var currencyId=f.currencyType;
            var company=f.supply;
          //为境内还是境外项目分类,1为境内项目,0为境外项目
			var overseasStatus = f.detailType;
			var airTicketId = $("#airTicketId").val();
			var budgetStatus = $("#budgetStatus").val();

			//判断是境内还是境外明细
			if("1" == f.detailType){
				container = "#costDomestic";
				firstTrTdTxt = "境内付款明细";
				tfootFirstTdTxt = "境内小计";
			}else{
				container = "#costForeign";
				firstTrTdTxt = "境外付款明细";
				tfootFirstTdTxt = "境外小计";
			}
			//为空判断
			if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {
				//向table中添加数据
				var html_tr = '<tr value="{10}">'.replace("{10}",f.detailType);
				if($(container).find("tbody tr").length){
					//行合并
					var $firstTd = $(container).find("tbody tr:first td:first")
					var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
					$firstTd.attr("rowspan",rowspan_count + 1);
				}else{
					html_tr += '<td rowspan="1">{6}</td>'.replace("{6}",firstTrTdTxt);
					$(container).find("tfoot").append('<tr><td>{0}</td><td colspan="7"></td></tr>'.replace("{0}",tfootFirstTdTxt));
				}
				$.ajax({
						 type: "POST",
						 url: sysCtx + "/cost/manager/saveAirTicketPreRecord/",                       
				         cache: false,
				         async: false,
				         dataType: "json",//返回的数据类型  
				         data: {format:"json",name : name, account: account,price : price,currencyId : currencyId, comment : comment,overseasStatus : overseasStatus,airTicketId : airTicketId,budgetStatus : budgetStatus,company : company},
				         success: function (data){
				        	
				        	html_tr +='<input type="hidden" name="itemAirTicketId" id="itemId" value="';
				        	html_tr +=data;
				        	html_tr +='"/>';
				         },
				         error: function (){
				       	 alert('返回数据失败');
				         }
				    });
				html_tr += '<td width="17%" name="tdName">{0}</td><td class="tr" width="10%" name="tdAccount">{1}</td><td width="15%" name="tdSupply">{2}</td><td width="10%" name="tdCurrencyName">{3}</td><td class="tr" width="10%" name="tdPrice">{4}</td><td width="20%" name="tdComment">{5}</td><td width="8%" class="tc"><a href="javascript:void(0)" onclick="modifyCost(this)">修改</a>&nbsp;&nbsp;&nbsp;<a href="javascript:void(0)" onclick="deleteCost(this)">删除</a></td></tr>'.replace("{0}",name).replace("{1}",account).replace("{2}",$("#supply option[value=" + f.supply + "]").text()).replace("{3}",$("#currencyType option[value=" + f.currencyType + "]").text()).replace("{4}",milliFormat(price,'1')).replace("{5}",comment);
				$(container).find("tbody").append(html_tr);
				costSum($(container));
				return true;
            }
		}
	},height:400,width:450});
}
//运控-成本录入-添加项目--小计
function costSum(obj){
	var objMoney = {};
	obj.find("tbody tr").each(function(index, element) {
		//var currencyName = $(element).find("td[name='tdCurrencyName']").text();
		var thisAccount = $(element).find("td[name='tdAccount']").text();
		var thisPrice = $(element).find("td[name='tdPrice']").text();
		var border=2;
		//去掉两边空格
		thisPrice=thisPrice.replace(/(^\s*)|(\s*$)/g, "");
		//找到金额中第一个数字位置
        for(var i=0;i<thisPrice.length;i++){
         if(thisPrice.substring(i,i+1).match(/^[0-9].*$/)){
           border=i;		  
		   break;
		  }
		}
        var currencyName =thisPrice.substring(0,border);
		//金额去掉第一个字符(币种)
		thisPrice=thisPrice.substring(border);        
		if(typeof objMoney[currencyName] == "undefined"){
			objMoney[currencyName] = parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
		}else{
			objMoney[currencyName] += parseFloat(thisAccount,10) * parseFloat(thisPrice.replace(",",""),10);
		}
	});
	//输出结果
	var strCurrency = "";
	var sign = " + ";
	var $footTd = obj.find("tfoot tr:first td:last");
	for(var i in objMoney){
		var isNegative = /^\-/.test(objMoney[i]);
		if(isNegative){
			sign = " - ";
		}
		if(strCurrency != '' || (strCurrency == '' && isNegative)){
			strCurrency += sign;
		}
		strCurrency += i + milliFormat(objMoney[i].toString().replace(/^\-/g,''),'1');
	}
	if($footTd.length){
		$footTd.html(strCurrency);
	}
}
//运控-成本录入-修改项目
function modifyCost(dom){
	var itemId=$(dom).parents("tr").find("#itemId").val();
	//清除正在编辑的状态
	$("tr[ismodify='1']").each(function(index, element) {
        $(element).removeAttr("ismodify");
    });
	//设置正在编辑项目tr
	var $thisTr = $(dom).parents("tr");
	$thisTr.attr("ismodify","1");
	//弹出框中加载数据
	var thisDetailType = $thisTr.attr("value");/*境内、境外*/
	var thisSupply = $thisTr.find("td[name='tdSupply']").text();/*批发商*/
	var thisName = $thisTr.find("td[name='tdName']").text();/*项目名称*/
	var thisCurrencyType = $thisTr.find("td[name='tdCurrencyName']").text();/*币种*/
	var thisPrice = $thisTr.find("td[name='tdPrice']").text();/*单价*/
	var thisAccount = $thisTr.find("td[name='tdAccount']").text();/*数量*/
	var thisComment = $thisTr.find("td[name='tdComment']").text();/*备注*/
	//绑定数据
	var $templateClone = $("#addItem").clone(false);
	//重置表单元素的id和name值
	$templateClone.find("[id]").each(function(index, element) {
        $(element).attr("id",$(element).attr("id").replace("0","")).attr("name",$(element).attr("name").replace("0",""));
    });
	//去除select的选项
	$templateClone.find("option:selected").removeAttr("selected");
	//境内、境外
	$templateClone.find("#detailType option[value='" + thisDetailType + "']").attr("selected","selected");
	//批发商
	$templateClone.find("#supply option").each(function(index, element) {
        if($(element).text() == thisSupply){
			$(element).attr("selected","selected");
			return;
		}
    });
	//项目名称
	$templateClone.find("#name").attr("value",thisName);
	//币种
	$templateClone.find("#currencyType option").each(function(index, element) {
        if($(element).text() == thisCurrencyType){
			$(element).attr("selected","selected");
			return;
		}
    });
	//单价
	$templateClone.find("#price").attr("value",thisPrice.replace(/,/g,''));
	//数量
	$templateClone.find("#account").attr("value",thisAccount);
	//备注
	$templateClone.find("#comment").text(thisComment);
	$.jBox('<div class="costBox">'+$templateClone.html()+'</div>', { title: "成本修改",buttons:{'提交': true,'取消': false}, submit:function(v, h, f){
		if (v==true){
			var $containerTr = $("tr[ismodify='1']");
			var name = $.trim(f.name);
            var price = $.trim(f.price);
			var account = $.trim(f.account);
            var comment = f.comment;
            var currencyId=f.currencyType;
            var company=f.supply;
            var overseasStatus = f.detailType;
			
			//为空判断
			if(name==""){
                top.$.jBox.tip('项目名称不能为空', 'error', { focusId: 'name' }); return false;
            }else if(price==""){
                top.$.jBox.tip('单价不能为空', 'error', { focusId: 'price' }); return false;
            }else if(comment.length > 200){
                top.$.jBox.tip('项目备注不能大于200字','error' ,{ focusId: 'comment' }); return false;
            }else if(account==""){
                top.$.jBox.tip('数量不能为空', 'error', { focusId: 'account' }); return false;
            }else {
            	
            	
            	$.ajax({
					 type: "POST",
					 url: sysCtx + "/cost/manager/updateAirTicketRecord/",                       
			         cache: false,
			         async: false,
			         dataType: "json",//返回的数据类型  
			         data: {format:"json",name : name, account: account,price : price,currencyId : currencyId, comment : comment,overseasStatus : overseasStatus,company : company},
			         success: function (data){
			         },
			         error: function (){
			       	 alert('返回数据失败');
			         }
			    });
            	
            	
            	
            	
				//修改table中的数据
				$containerTr.find("td[name='tdSupply']").text($("#supply option[value=" + f.supply + "]").text());
				$containerTr.find("td[name='tdName']").text(name);
				$containerTr.find("td[name='tdCurrencyName']").text($("#currencyType option[value=" + f.currencyType + "]").text());
				$containerTr.find("td[name='tdPrice']").text(milliFormat(price,'1'));
				$containerTr.find("td[name='tdAccount']").text(account);
				$containerTr.find("td[name='tdComment']").text(comment);
				
				var containerFrom;
				var containerTo;
				var tbodyFirstTdTxtTo = "";
				var tfootFirstTdTxtTo = "";
				
				//判断是境内还是境外明细
				if("1" == f.detailType){
					containerTo = "#costDomestic";
					firstTrTdTxtTo = "境内付款明细";
					tfootFirstTdTxtTo = "境内小计";
					containerFrom = "#costForeign";
				}else{
					containerTo = "#costForeign";
					firstTrTdTxtTo = "境外付款明细";
					tfootFirstTdTxtTo = "境外小计";
					containerFrom = "#costDomestic";
				}
				//若修改境内境外类型
				if(thisDetailType != f.detailType){					
					var tdFromTxt = "";
					//被移除行是否是第一行
					if(!$containerTr.index()){
						tdFromTxt = $containerTr.find("td:first").text();
					}
					//设置境内境外标志
					$containerTr.attr("value",f.detailType).appendTo($(containerTo).find("tbody"));
					$containerTr = $(containerTo).find("tbody tr:last");
					//移入行操作
					if(1 == $(containerTo).find("tbody tr").length){
						if(7 == $containerTr.find("td").length){
							$containerTr.prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}","1").replace("{1}",firstTrTdTxtTo));
						}else{
							$containerTr.find("td:first").text(firstTrTdTxtTo).attr("rowspan","1");
						}
						$(containerTo).find("tfoot").append('<tr><td width="10%">{0}</td><td colspan="7"></td></tr>'.replace("{0}",tfootFirstTdTxtTo));
					}else{
						if(8 == $containerTr.find("td").length){
							$containerTr.find("td:first").remove();
						}
						//合并行
						var $firstTd = $(containerTo).find("tbody tr:first td:first")
						var rowspan_count = $firstTd.attr("rowspan") ? Number($firstTd.attr("rowspan")) : 1;
						$firstTd.attr("rowspan",rowspan_count + 1);
					}
					
					//被移出行操作
					if(0 == $(containerFrom).find("tbody tr").length){
						$(containerFrom).find("tbody").empty();
						$(containerFrom).find("tfoot").empty();
					}else{
						if("" != tdFromTxt){
							$(containerFrom).find("tbody tr:first").prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}",$(containerFrom).find("tbody tr").length).replace("{1}",tdFromTxt));
						}else{
							$(containerFrom).find("tbody tr:first td:first").attr("rowspan",$(containerFrom).find("tbody tr").length);
						}
					}
					costSum($(containerFrom));				
				}
				costSum($(containerTo));
				return true;
            }
		}
	},height:400,width:450});
}

//删除成本录入
function deleteCost(dom){
	var $thisTr = $(dom).parents("tr");
	var tableName = "#" + $(dom).parents("table").attr("id");
	var $firstTd = $(tableName).find("tr:first td:first");
	var itemId=$thisTr.find("#itemId").val();
	if(1 == $(tableName).find("tbody tr").length){
		$(tableName).find("tbody").empty();
		$(tableName).find("tfoot").empty();
	}else{
		if($thisTr.index()){
			$firstTd.attr("rowspan",Number($firstTd.attr("rowspan"))-1);
		}else{
			$thisTr.next().prepend('<td width="10%" rowspan="{0}">{1}</td>'.replace("{0}",$(tableName).find("tbody tr").length-1).replace("{1}",$firstTd.text()));
		}
		$thisTr.remove();
	}
	$.ajax({
		 type: "POST",
		 url: sysCtx + "/cost/manager/deleteAirTicketRecord/",                       
      cache: false,
      async: false,
      dataType: "json",//返回的数据类型  
      data: {format:"json",itemId:itemId},
      success: function (data){
      },
      error: function (){
    	 alert('返回数据失败');
      }
 });
	costSum($(tableName));
}





//发布签证产品--价格--币种选择
function selectCurrencyVisa(){
	$(".add2_nei_table_typetext input.ipt-currency").prev("span").text($("#selectCurrency").children("option:selected").attr("id"));
	$("#selectCurrency").change(function(){
		var theValue = $(this).val();
		var oldCurrency = $(this).attr("nowClass");
		var newCurrency = $(this).children("option:selected").attr("id");
		$(".add2_nei_table_typetext input.ipt-currency").prev("span").text(newCurrency);
//		$(this).attr("nowClass",newCurrency);
	});
}


/**notice 公告**/
function notice(){
	

	
	$.fn.myScroll = function(options){
	//默认配置
	var defaults = {
		speed:40,  //滚动速度,值越大速度越慢
		rowHeight:20 //每行的高度
	};
	
	var opts = $.extend({}, defaults, options),intId = [];
	
	function marquee(obj, step){
	
		obj.find("ul").animate({
			marginTop: '-=1'
		},0,function(){
				var s = Math.abs(parseInt($(this).css("margin-top")));
				if(s >= step){
					$(this).find("li").slice(0, 1).appendTo($(this));
					$(this).css("margin-top", 0);
				}
			});
		}
		
		this.each(function(i){
			var sh = opts["rowHeight"],speed = opts["speed"],_this = $(this);
			intId[i] = setInterval(function(){
				if(_this.find("ul").height()<=_this.height()){
					clearInterval(intId[i]);
				}else{
					marquee(_this, sh);
				}
			}, speed);

			_this.hover(function(){
				clearInterval(intId[i]);
			},function(){
				intId[i] = setInterval(function(){
					if(_this.find("ul").height()<=_this.height()){
						clearInterval(intId[i]);
					}else{
						marquee(_this, sh);
					}
				}, speed);
			});
		
		});

	}

}

//订单上传资料补充资料
function orderAddifile(){
	$(".orderAddifile").click(function(){
		$(this).parent().before('<li>名称：<input type="text" disabled="disabled" class="seach_longinput"><input type="button" class="btn btn-primary sc-chuan" value="上传"><input type="file" name="" class="input-file"><a class="ydbz_x inquiry_zxdel">删除</a></li>');
	})
}
//申请发票合开发票
//开票方式为合并类型时，显示添加合开订单按钮
function invoiceTypeChg(){
	 var value=$(".invoiceTypeChg option:selected").val();
	  if("100" == value){
                $('.invoiceAdd').show();
            } else{
                $('.invoiceAdd').hide();
            }
	}
//添加合开订单
 function invoiceOrder(){
		$('.invoiceAdd').delegate(".ydbz_x","click",function(){
			    var obj=$(this);
				var html = '<div class="add_allactivity"><label>订单号：</label>';
	html += '<input type="text" class="invoiceOrderInput"/>';
	html += '</div>';
	$.jBox(html, { title: "添加合开订单", submit:function(v, h, f){
		if(v=='ok'){
			var values=h.find('.invoiceOrderInput').val();
			if(values==""){
				 h.find('.nothisPro').hide('fast', function () { $(this).remove(); });
			     //如果没订单
			     $('<p class="nothisPro" style="display: none;">没有这个订单</p>').appendTo(h).show('slow');
			     return false;
				}else{
					$(obj).parent().before('<div class="invoiceOrder"><div class="ydbz_tit orderdetails_titpr">订单<a class="ydbz_x" onclick=invoiceOrderDel(this)>删除</a></div><div class="orderdetails1"><table border="0"><tbody><tr><td class="mod_details2_d1">团号：</td><td class="mod_details2_d2">rtreter</td><td class="mod_details2_d1">出团日期：</td><td class="mod_details2_d2">2014-09-12</td><td class="mod_details2_d1">订单号：</td><td class="mod_details2_d2">JHG140912049</td><td class="mod_details2_d1">预定日期：</td><td class="mod_details2_d2">2014-09-05 20:19:17</td></tr><tr> <td class="mod_details2_d1">人数：</td><td class="mod_details2_d2">1</td><td class="mod_details2_d1">应收尾款：</td><td class="mod_details2_d2">¥1,000</td><td class="mod_details2_d1">财务到账：</td><td class="mod_details2_d2">¥20</td><td class="mod_details2_d1">已开发票：</td><td class="mod_details2_d2">¥0</td></tr></tbody></table></div><table class="activitylist_bodyer_table"><thead><tr><th width="10%">付款日期</th><th width="10%">付款方式</th><th width="10%">付款类型</th><th width="10%">付款金额</th><th width="10%">达账金额</th><th width="10%">已开票金额</th><th width="10%">可开票金额</th><th width="10%">本次开票金额</th></tr></thead><tbody><tr><td class="tc">2014-09-05</td><td>切位金额划转</td><td>交订金</td><td class="tr">¥20</td><td class="tr">¥20</td><td class="tr">¥0</td><td class="tr">¥20</td><td class="tc"><input type="text" class="rmb"></td></tr><tr><td class="tc">2014-09-05</td><td>快速支付</td><td>支付尾款</td><td class="tr">¥980</td><td class="tr">¥20</td><td class="tr">¥0</td><td class="tr">¥20</td><td class="tc"><input type="text" class="rmb"></td></tr><tr><td colspan="3"  class="tc">小计</td><td class="tr">¥1,000</td><td class="tr">¥20</td><td class="tr">¥0</td><td class="tr">¥20</td><td class="tr">¥20</td></tr></tbody></thead></table></div>');
					}
			}	
	},height:180,width:500});
		
	
		})	
	 }
//删除合开订单
	function invoiceOrderDel(obj){
		$(obj).parent().parent().remove();
    }
	
//订单-签证-参团签证订单修改  查看大图
function showBigImg(imgsrc){
	var loaddingPositionTop = $(window).height()/3;
	var html = '<div class="jboxblack"></div>';
	html += '<div class="bigImg" style="top:' + $(window).scrollTop() +'px;"><div class="loading" style="padding-top:' + loaddingPositionTop +'px;"><img src="images/loading.gif" width="64" height="64" /></div></div>';
	$("html,body").attr("style","overflow:hidden;");
	$("body").append(html);
	//按ESC键盘
	$(document).keydown(function(e){
		if(e.keyCode == 27){
			$(".jboxblack").remove();
			$(".bigImg").remove();
			$("html,body").removeAttr("style");
		}
	});
	// 创建对象
	var img = new Image();
	//图片加载完以后
	img.onload = function(){
		var perInit = 1;
		var rate_w = Math.round((window.screen.availWidth/img.width)*1000)/1000;
		var rate_h = Math.round(((window.screen.availHeight-70)/img.height)*1000)/1000;
		var bigImgPaddingTop = 0;
		if(rate_w < rate_h){
			if(perInit > rate_w){
				perInit = rate_w;
			}
		}else{
			if(perInit > rate_h){
				perInit = rate_h;
			}
		}
		if(1 == perInit){
			if($(window).height() > img.height){
				bigImgPaddingTop = ($(window).height() - img.height)/2;
			}
		}
		var html_loadImg = '<div style="padding-top:' + bigImgPaddingTop +'px;"><img src="' + imgsrc +'" height="' + img.height*perInit + '"';
		html_loadImg += '" /></div>';
		html_loadImg += '<div class="tips-exit">按<strong>ESC</strong>键退出全屏浏览</div>';
		
		$(".bigImg").html(html_loadImg);
		//返回顶部
		//document.documentElement.scrollTop = document.body.scrollTop = 0;
	};
	// 改变图片的src
	img.src = imgsrc;
}
//转入团号
 function changeGroups(obj){
	 $(obj).parent().find("div").show();
	 }
//新增航段
function inquiryFlights3Add(obj){
	var id=$(obj).parent().parent().find('.addFlights3Div').length;
	var cloneDiv = $(".addFlights3None").clone(true);
	//重置label与input的id、name、for
	var $lable = cloneDiv.find(".title_samil .seach_check label");
	$lable.each(function(index, element) {
		var str_flag = "-" + (index+1);
        $(element).attr("for","radio3" + id + str_flag);
		var $input = $(element).find("input[type=radio]");
		$input.attr("id","radio3" + id + str_flag).attr("name","searchRadio3" + id);
    });
	cloneDiv.appendTo($(obj).parent().parent());
	cloneDiv.show().removeClass('addFlights3None').find('em').text(id);
}
//删除航段
function inquiryFlights3Del(obj){
	$(obj).parent().parent().parent().remove();
	$('.addFlights3Div').each(function(index, element){
		$(this).find('em').text(index);
		//重置label与input的id、name、for
		if(2 < index){
			var num_i = index;
			var $lable = $(element).find(".title_samil .seach_check label");
			$lable.each(function(index, element) {
				var str_flag = "-" + (index+1);
				$(element).attr("for","radio3" + num_i + str_flag);
				var $input = $(element).find("input[type=radio]");
				$input.attr("id","radio3" + num_i + str_flag).attr("name","searchRadio3" + num_i);
			});
		}
	});
}

//select模糊匹配插件 使用jquery.ui的widget
(function( $ ) {
	if($.widget){
		//下拉框模糊匹配多选
		$.widget( "custom.comboboxInquiry", {
			_create: function() {
				this.wrapper = $( "<span>" ).addClass( "custom-combobox" ).insertAfter( this.element );
				this.element.hide();
				this._createAutocomplete();
				this._createShowAllButton();
			},
	   
			_createAutocomplete: function() {
				var selected = this.element.children( ":selected" ),
				value = selected.val() ? selected.text() : "";
				
				this.input = $("<input>").appendTo( this.wrapper ).val( value ).attr( "title", "" ).addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" ).autocomplete({
					delay: 0,
					minLength: 0,
					source: $.proxy( this, "_source" )
				}).tooltip({
					tooltipClass: "ui-state-highlight"
				});
				
				this._on( this.input, {
					autocompleteselect: function( event, ui ) {
						ui.item.option.selected = true;
						this._trigger( "select", event, {
							item: ui.item.option
						});
					},autocompletechange: "_removeIfInvalid"
				});
			},
	   
			_createShowAllButton: function() {
				var input = this.input,
				wasOpen = false;
				
				$( "<a>" ).attr( "tabIndex", -1 ).attr( "title", "选择" ).tooltip().appendTo( this.wrapper ).button({
					icons: {primary: "ui-icon-triangle-1-s"},
					text: false
				}).removeClass( "ui-corner-all" ).addClass( "custom-combobox-toggle ui-corner-right" ).mousedown(function() {
					wasOpen = input.autocomplete( "widget" ).is( ":visible" );
				}).click(function() {
					input.focus();
					
					// Close if already visible
					if ( wasOpen ) {
					  return;
					}
	   
					// Pass empty string as value to search for, displaying all results
					input.autocomplete( "search", "" );
				});
			},
	   
			_source: function( request, response ) {
				var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
				response( this.element.children( "option" ).map(function() {
					var text = $( this ).text();
					if ( this.value && ( !request.term || matcher.test(text) ) )
					return {
						label: text,
						value: text,
						option: this
					};
				}));
			},
	   
			_removeIfInvalid: function( event, ui ) {
	   
				// Selected an item, nothing to do
				if ( ui.item ) {//console.log(ui.item);
					this._trigger("afterInvalid",null,ui.item.value);
					return;
				}
	   
				// Search for a match (case-insensitive)
				var value = this.input.val(),
					valueLowerCase = value.toLowerCase(),
					valid = false;
				this.element.children( "option" ).each(function() {
					if ( $( this ).text().toLowerCase() === valueLowerCase ) {
						this.selected = valid = true;
						return false;
					}
				});
	   
				// Found a match, nothing to do
				if ( valid ) {
					this._trigger("afterInvalid",null,value);
					return;
				}
	   
				// Remove invalid value
				this.input.val( "" ).attr( "title", value + "" ).tooltip( "open" );
				this.element.val( "" );
				this._delay(function() {
					this.input.tooltip( "close" ).attr( "title", "" );
				}, 2500 );
				this.input.data( "ui-autocomplete" ).term = "";
			},
	   
			_destroy: function() {
				this.wrapper.remove();
				this.element.show();
			}
		});
		//下拉框模糊匹配
		$.widget( "custom.comboboxSingle", {
			_create: function() {
			  this.wrapper = $( "<span>" )
				.addClass( "custom-combobox" )
				.insertAfter( this.element );
	   
			  this.element.hide();
			  this._createAutocomplete();
			  this._createShowAllButton();
			},
	   
			_createAutocomplete: function() {
			  var selected = this.element.children( ":selected" ),
				value = selected.val() ? selected.text() : "";
	   
			  this.input = $( "<input>" )
				.appendTo( this.wrapper )
				.val( value )
				.attr( "title", "" )
				.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
				.autocomplete({
				  delay: 0,
				  minLength: 0,
				  source: $.proxy( this, "_source" )
				})
	   
			  this._on( this.input, {
				autocompleteselect: function( event, ui ) {
				  ui.item.option.selected = true;
				  this._trigger( "select", event, {
					item: ui.item.option
				  });
				},
	   
				autocompletechange: "_removeIfInvalid"
			  });
			},
	   
			_createShowAllButton: function() {
			  var input = this.input,
				wasOpen = false;
	   
			  $( "<a>" )
				.attr( "tabIndex", -1 )
				.attr( "title", "选择" )
				.tooltip()
				.appendTo( this.wrapper )
				.button({
				  icons: {
					primary: "ui-icon-triangle-1-s"
				  },
				  text: false
				})
				.removeClass( "ui-corner-all" )
				.addClass( "custom-combobox-toggle ui-corner-right" )
				.mousedown(function() {
				  wasOpen = input.autocomplete( "widget" ).is( ":visible" );
				})
				.click(function() {
				  input.focus();
	   
				  // Close if already visible
				  if ( wasOpen ) {
					return;
				  }
	   
				  // Pass empty string as value to search for, displaying all results
				  input.autocomplete( "search", "" );
				});
			},
	   
			_source: function( request, response ) {
			  var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
			  response( this.element.children( "option" ).map(function() {
				var text = $( this ).text();
				if ( this.value && ( !request.term || matcher.test(text) ) )
				  return {
					label: text,
					value: text,
					option: this
				  };
			  }) );
			},
	   
			_removeIfInvalid: function( event, ui ) {
	   
			  // Selected an item, nothing to do
			  if ( ui.item ) {
				return;
			  }
	   
			  // Search for a match (case-insensitive)
			  var value = this.input.val(),
				valueLowerCase = value.toLowerCase(),
				valid = false;
			  this.element.children( "option" ).each(function() {
				if ( $( this ).text().toLowerCase() === valueLowerCase ) {
				  this.selected = valid = true;
				  return false;
				}
			  });
	   
			  // Found a match, nothing to do
			  if ( valid ) {
				return;
			  }
	   
			  // Remove invalid value
			  this.input
				.val( "" )
				.attr( "title", value + "" )
				.tooltip( "open" );
			  this.element.val( "" );
			  this._delay(function() {
				this.input.tooltip( "close" ).attr( "title", "" );
			  }, 2500 );
			  this.input.data( "ui-autocomplete" ).term = "";
			},
	   
			_destroy: function() {
			  this.wrapper.remove();
			  this.element.show();
			}
		});
	}
})( jQuery );

//table中“团号“、”产品“切换
function switchNumAndPro(){
	//点击团号
	$(".activitylist_bodyer_table").delegate(".tuanhao","click",function(){
        $(this).addClass("on").siblings().removeClass('on');
        $('.chanpin_cen').removeClass('onshow');
        $('.tuanhao_cen').addClass('onshow');
    });
    //点击产品
    $(".activitylist_bodyer_table").delegate(".chanpin","click",function(){
         $(this).addClass("on").siblings().removeClass('on');
         $('.tuanhao_cen').removeClass('onshow');
         $('.chanpin_cen').addClass('onshow');
    });
}
function switchSalerAndPicker() {
	//点击销售
    $(".activitylist_bodyer_table").on("click", ".order-saler-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').addClass('onshow');
        $table.find('.order-picker').removeClass('onshow');
    });
  //点击下单人
    $(".activitylist_bodyer_table").on("click", ".order-picker-title", function() {
        $(this).addClass("on").siblings().removeClass('on');
        var $table=$(this).parents('table:first');
        $table.find('.order-saler').removeClass('onshow');
        $table.find('.order-picker').addClass('onshow');
    });
}

//是否分段报价
	function flyDivCheck(){
		if($("input[name='flyDivInput']").prop("checked")){
			$('.flyMoreDiv').show();
			}else{
			$('.flyMoreDiv').hide();
			}
		}
//预定保存游客信息
function SavePeopleTable(obj){
	var input=$(obj).parent().parent().find("input");
	var textarea=$(obj).parent().parent().find("textarea");
	var selects=$(obj).parent().parent().find("select");
	if($(input).prop("disabled")){
			    $(input).removeAttr("disabled","disabled");
			}else{
				$(input).attr("disabled","disabled");
			}
	if($(textarea).prop("disabled")){
			    $(textarea).removeAttr("disabled","disabled");
			}else{
				$(textarea).attr("disabled","disabled");
			}
	if($(selects).prop("disabled")){
			    $(selects).removeAttr("disabled","disabled");
			}else{
				$(selects).attr("disabled","disabled");
			}
	if($(obj).text()=="保存"){
		    $(obj).text("修改");
			$(obj).parent().prev().hide();
			$(obj).parent().parent().find('.tourist-t-off').css("display","inline");
			$(obj).parent().parent().find('.tourist-t-on').hide();
			$(obj).parent().parent().find('.add_seachcheck').addClass('boxCloseOnAdd')
		}else{
			$(obj).text("保存");
			$(obj).parent().prev().show();
			$(obj).parent().parent().find('.tourist-t-off').css("display","none");
			$(obj).parent().parent().find('.tourist-t-on').show();
			$(obj).parent().parent().find('.add_seachcheck').removeClass('boxCloseOnAdd')
			}
	//添加费用
	var addcost=$(obj).parent().parent().find(".btn-addBlue");
	if($(addcost).css("display")=="none") {
			$(addcost).show();
				}else{
			$(addcost).hide();
				}
	//删除			
	var deleltecost=$(obj).parent().parent().find("a[name='deleltecost']");
	if($(deleltecost).css("display")=="none") {
			$(deleltecost).show();
				}else{
			$(deleltecost).hide();
				}
	//应用全部			
	var useall=$(obj).parent().parent().find(".yd-total a");
	if($(useall).css("display")=="none") {
			$(useall).show();
				}else{
			$(useall).hide();
				}
	}
/*AA码记录*/
	function AAHover(){
		$(".AAHover").hover(function(){
			$(this).find(".AAboxmain").show();
			},function(){
			$(this).find(".AAboxmain").hide();
			})
		}
/**
 * 左侧菜单点选事件
 * @param href 请求url地址
 * @param element dom元素
 */
function selectMenu(href, element){
    var id = $(element).parent().attr('id');
    id = id.replace("menu_", "");
    var param = {_m : id};
    href = appendParam(href, param);
    window.location.href = href;
}

/**
 * 左侧菜单子项点选事件
 * @param href 请求url地址
 * @param element dom元素
 * modified by can_do
 */
function selectChildMenu(href, element){

    var childId = $(element).parent().attr('id');
    childId = childId.replace("childMenu_", "");
    var fId = $(element).parent().parent().prev('h2').attr('id');
    fId = fId.replace("menu_", "");

    var param = {_m : fId, _mc : childId};
    href = appendParam(href, param);
    window.location.href = href;
}

/**
 * 在url上加入参数
 * @param href 请求url地址
 * @param param 参数对象
 */
function appendParam(href, param){
    var urlPatterns = href.split('?');
    if(urlPatterns.length > 1 && urlPatterns[1]){
        for(var element in param){
            href += ('&' + element + "=" + param[element]);
        }
    }else{
        href += '?';
        var tmp = '';
        for(var element in param){
            tmp += ('&' + element + "=" + param[element]);
        }
        href += tmp.substring(1);
    }
    return href;

}

function flashChecker()
{
	var hasFlash=0;        //是否安装了flash
	var flashVersion=0;    //flash版本
	
	if(document.all){
		var swf ;
		try{
		   swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash'); 
		} catch (e) {
			hasFlash=1;
			alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件");
		}
		if(swf) { 
			hasFlash=1;
			VSwf=swf.GetVariable("$version");
			flashVersion=parseInt(VSwf.split(" ")[1].split(",")[0]); 
		}
	}else{
		if (navigator.plugins && navigator.plugins.length > 0){
			var swf=navigator.plugins["Shockwave Flash"];
			if (swf)  {
			   hasFlash=1;
		       var words = swf.description.split(" ");
		       for (var i = 0; i < words.length; ++i){
		         if (isNaN(parseInt(words[i]))) continue;
		         flashVersion = parseInt(words[i]);
			   }
			}
		}
	}
	return {f:hasFlash,v:flashVersion};
}

//var fls=flashChecker();
//var s="";
//
//if(fls.f) document.write("您安装了flash,当前flash版本为: "+fls.v+".x");
//else document.write("您没有安装flash"); 


//上传文件时，点击后弹窗进行上传文件(多文件上传)
//方法参数：${ctx}、显示上传文件名称input的id、当前点击元素
function uploadFiles2(ctx, inputId, obj, isSimple) {
	//debugger;
	var fls=flashChecker();
	var s="";
	if(fls.f) {
//		alert("您安装了flash,当前flash版本为: "+fls.v+".x");
	} else {
		alert("请您退出当前浏览器,并安装当前浏览器的flash player软件,然后才能上传文件"); 
		return;
	}
	
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find(".uploadPath").length == 0)
		$(obj).parent().append('<div class="uploadPath" style="display: none" id="uploadPathDiv"></div>');
	
	$(obj).addClass("clickBtn");
	
	//默认为多文件上传
	if(isSimple == null) {
		isSimple = "false";
	}
	
	$.jBox("iframe:"+ ctx +"/MulUploadFile/uploadFilesPage?isSimple=" + isSimple, {
	//$.jBox("iframe:"+ ctx, {
	    title: "文件上传",
		width: 340,
   		height: 365,
   		buttons: {'完成上传':true},
   		persistent:true,
   		loaded: function (h) {},
   		submit: function (v, h, f) {
			$(h.find("iframe")[0].contentWindow.file_upload).next("p").find(".successUpload").click();
			//这里拼接本次上传文件的原名称
			var fileIDList = "";
			var fileNameList = "";
			var filePathList = "";
			//
			if($(obj).parent().find("[name='docID']").length != 0) {
				$(obj).parent().find("[name='docID']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						fileIDList = $(obj).val();
					}else{
						fileIDList += $(obj).val() + ",";
					}
				});
			}
			if($(obj).parent().find("[name='docOriName']").length != 0) {
				$(obj).parent().find("[name='docOriName']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						fileNameList = $(obj).val();
					}else{
						fileNameList += $(obj).val() + ";";
					}
				});
			}
			if($(obj).parent().find("[name='docPath']").length != 0) {
				$(obj).parent().find("[name='docPath']").each(function(index, obj) {
					if(null != isSimple && "false"!=isSimple) {
						filePathList = $(obj).val();
					}else{
						filePathList += $(obj).val() + ";";
					}
				});
			}
			//在这里将原名称写入到指定id的input中
			//if(inputId)
			//	$("#" + inputId).val(fileNameList);
			//该函数各自业务jsp都写一个，里面的内容根据自身页面要求自我实现
			commenFunction2(obj,fileIDList,fileNameList,filePathList);
			$("#uploadPathDiv").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			fileNameList = "";
   		}
	});
	$(".jbox-close").hide();
}

/**
 * 附件上传回调方法
 * @param {Object} obj button对象
 * @param {Object} fileIDList  文件表id
 * @param {Object} fileNameList 文件原名称
 * @param {Object} filePathList 文件url
 */
function commenFunction2(obj,fileIDList,fileNameList,filePathList){
    //  var pathBase = "file:///F:/apache-tomcat-7.0.54/bin/";
    //  var newfilePath = filePathList.replace(/\\/g,"/");
    //      newfilePath = newfilePath.replace(";","");
	//debugger;
        var name = obj.name;
        $("#"+name).remove();
        $(obj).prev().val(fileNameList);
        $(obj).parent().append('<div style="display: none" id="'+obj.name+'"></div>');
        $("#"+name).append("<input type='hidden' name="+name+"docID value='" +fileIDList.replace(";","")+ "' />")
                    .append("<input type='hidden' name="+name+"docName value='" +fileNameList.replace(";","")+ "' />")
                    .append("<input type='hidden' name="+name+"docPath value='" +filePathList.replace(";","")+ "' />");
        $(obj).prev().show();
         
    //  ydbz2interfile(obj,pathBase+newfilePath);
    }



//验证是否是数值
function formatNum(number){
	return Number(number) ? Number(number) : 0;
}

//正负数字验证
function validNum(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		//thisvalue = thisvalue.replace(/\D/g,"");
		thisvalue = thisvalue.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
		var txt = thisvalue.split(".");
        thisvalue = txt[0]+(txt.length>1?"."+txt[1]:"");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}else{
		//$(dom).val(0);
	}
}
//失去焦点格式化带两位小数的浮点数
function validNumFinally(dom){
	var thisvalue = $(dom).val();
	var minusSign = false;
	if(thisvalue){
		if(/^\-/.test(thisvalue)){
			minusSign = true;
			thisvalue = thisvalue.substring(1);
		}
		thisvalue = (thisvalue.replace(/^(\d*)$/,"$1.") + "00").replace(/(\d*\.\d\d)\d*/,"$1").replace(/^\./,"0.");
		if(minusSign){
			thisvalue = '-' + thisvalue;
		}
		$(dom).val(thisvalue);
	}else{
		$(dom).val("0.00");
	}
}

//数字添加千位符
function milliFormat(s,isFloat){
	if(isFloat){//弥补JavaScript浮点运算的一个bug
//		alert(s);
		try{
			s = s.toFixed(2);
		}catch (e){
			
		}
	}
	var minusSign = false;
	if((typeof s) != String){
		s = s.toString();
	}
	if(/^\-/.test(s)){
		minusSign = true;
		s = s.substring(1);
	}
	if(/[^0-9\.]/.test(s)) return "invalid value";
	s=s.replace(/^(\d*)$/,"$1.");
	s=(s+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	s=s.replace(".",",");
	var re=/(\d)(\d{3},)/;
	while(re.test(s)){
		s=s.replace(re,"$1,$2");
	}
	if(isFloat){
		s=s.replace(/,(\d\d)$/,".$1");
	}else{
		s=s.replace(/,(\d\d)$/,"");
	}
	if(minusSign){
		s= '-' + s;
	}
	return s.replace(/^\./,"0.");
}


//数字去掉千位符
function rmoney(s)   
{   
   return parseFloat(s.replace(/[^\d\.-]/g, ""));   
} 

//订单团队退款
function refundInput(obj){
	var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
	// 只有当检测到不合法字符时才进行替换，因为实时替换会导致光标乱窜
	if (ms != obj.value) {
		var txt = ms.split(".");
		obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
	}
	totalRefund();
}
function refundInputs(obj,isempty){//obj:this对象；isempty:this的value为空时，是否返回空
	objs=obj.value;
	objs=objs.replace(/^(\d*)$/,"$1.");
	objs=(objs+"00").replace(/(\d*\.\d\d)\d*/,"$1");
	objs= objs.replace(/^\./,"0.");
	if((objs == "0.00") && isempty){
		$(obj).val("");
		$(obj).parent().parent().find("input[name^='refundProject']").rules("remove");
		$(obj).parent().parent().find("input[name^='refundProject']").nextAll(".error").hide();
	}else{
		$(obj).val(objs);
		$(obj).next("span").hide();
	}
}


//批量还护照
function jbox_passportReturn(){
	var html = '<div style="margin-top:20px;padding:0 10px">';
	html += '<p>不满足条件用户：</p><table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>护照状态</th><th>原因</th></tr></thead><tbody><tr><td>张三</td><td>2323555</td><td>已借</td><td>xxxx</td></tr><tr><td>李四</td><td>2323555</td><td>已借</td><td>xxxx</td></tr></tbody></table>';
	html += '<p>满足条件用户：</p><table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">护照状态</th><th width="10%">归还日期</th><th width="10%">护照归还人</th><th width="10%">备注</th></tr></thead><tbody><tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="张三" /></td><td><input type="text" /></td></tr><tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr></tbody></table>';
	html += '</div>';
	$.jBox(html, { title: "批量还护照",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:'auto',width:580});
}

//批量还收据
function jbox_receiptMore(){
	var html = '<div style="margin-top:20px;padding:0 10px">';
	html += '<p>不满足条件用户：</p><table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>签证费用</th><th>借款状态</th><th>不满足原因</th></tr></thead><tbody><tr><td>张三</td><td>2323555</td><td class="tr">$1000.00</td><td>已借</td><td>尚无借款</td></tr><tr><td>李四</td><td>2323555</td><td class="tr">$1000.00</td><td>已借</td><td>收据已还</td></tr></tbody></table>';
	html += '<p>满足条件用户：</p><table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">签证费用</th><th width="10%">借款状态</th><th width="10%">借款金额</th><th width="10%">收据金额</th><th width="10%">归还时间</th><th width="10%">还收据人</th><th width="10%">备注</th></tr></thead><tbody><tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td class="tr">$1000.00</td><td>未借</td><td class="tr">$1000.00</td><td class="tr">$1000.00</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="张三" /></td><td><input type="text" /></td></tr><tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td class="tr">$1000.00</td><td>未借</td><td class="tr">$1000.00</td><td class="tr">$1000.00</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="张三" /></td><td><input type="text" /></td></tr></tbody></table>';
	html += '</div>';
	$.jBox(html, { title: "批量还收据",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:'auto',width:700});
}
//批量借款
function jbox_moneyBorrow(){
	var html = '<div style="margin-top:20px;padding:0 10px">';
	html += '<p>不满足条件用户：</p><table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>签证费用</th><th>借款状态</th><th>不满足原因</th></tr></thead><tbody><tr><td>张三</td><td>2323555</td><td class="tr">$1000.00</td><td>已借</td><td>尚无借款</td></tr><tr><td>李四</td><td>2323555</td><td class="tr">$1000.00</td><td>已借</td><td>收据已还</td></tr></tbody></table>';
	html += '<p>满足条件用户：</p><table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">签证费用</th><th width="11%">借款日期</th><th width="10%">借款人</th><th width="10%">借款金额</th><th width="10%">借款状态</th><th width="10%">备注</th></tr></thead><tbody><tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td class="tr">$1000.00</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="张三" /></td><td><input type="text" value="" /></td><td>未借</td><td><input type="text" /></td></tr><tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td class="tr">$1000.00</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="张三" /></td><td><input type="text" value="" /></td><td>未借</td><td><input type="text" /></td></tr></tbody></table>';
	html += '</div>';
	$.jBox(html, { title: "批量借款",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			return true;
		}
	},height:'auto',width:680});

}

/**
* 左侧菜单子项点选事件
* @param href 请求url地址
* @param element dom元素
* added by can_do on 2015-05-06
*/
function selectChildMenu_new(href, element) {

    var isEnabled=href.indexOf('/visa/order/') < 0?true:false;
	
    var childId = $(element).parent().attr('id');
    childId = childId.replace("childMenu_", "");
    var fId = $(element).parent().parent().prev('h2').attr('id');
    fId = fId.replace("menu_", "");

    var param = {_m : fId, _mc : childId};
    href = appendParam(href, param);
	
		
	///////////////begin//////////
	//过滤掉：签证的菜单
	if (isEnabled) {
		// 显示 loading
		 var html = [
                '<div style="top: 0; left: 0; position: fixed; z-index: 10000;background: #000; opacity: 0.5; filter: alpha(opacity=50); width:100%; height:100%;"></div>',
                '<div style="top: 50%; left: 50%; position: fixed; z-index: 10001;margin-left:-49px, margin-top:-30px; font-size:14px; font-family:微软雅黑;line-height:24px; color:#FFF;">',
                '<img src="/static/images/loading_little.gif" style="margin-left:30px" /><br/>正在加载，请等待……</div>'
            ];
			
			$("body").append(html.join(''));
	
	        //ajax提交
			$.ajax({
				type: "get",
				url: href,
				success: function(data) {
					var map = {
					    href: href,
					    title: element.innerText
					};
					//原来页面刷新
					//not need to refresh history page
					//window.history.pushState && window.history.pushState(map, '', href);
					//urite(data);
					//document.write = urite;
					//document.getElementById("content").innerHTML=data;
					document.write(data);
					document.close();
					
				},
				error: function() {
					throw Error("加载" + href + "失败");
				}
			});
	} else {
	window.location.href = href;
	}
	/////////////////end///////////
}

				
/**
* 显示 loading 效果
* remedied by can_do on 2015-05-07
*/
function showLoading() {
			if (!$("div.loadingMasklayer").length) {
				var html = [
					'<div class="loadingMasklayer" style="top: 0; left: 0; position: fixed; z-index: 10000;background: #000; opacity: 0.5; filter: alpha(opacity=50); width:100%; height:100%;"></div>',
					'<div class="loading">正在加载，请等待...</div>'
				];
				$("body").append(html.join(''));
			} else {
				$("div.loadingMasklayer").show();
				$("div.loading").show();
			}
		}

/**
* 隐藏 loading 效果
* remedied by can_do on 2015-05-07
*/
function hideLoading() {
	$("div.loadingMasklayer").hide();
	$("div.loading").hide();
}
//关联信息展开收起
function showText(dom) {
	var $this = $(dom);
	var $airInfo = $this.parent().next();
	if ($airInfo.is(":visible")) {
		$airInfo.slideUp();
		$this.html('' + $this.text().replace("收起", "展开") + '<i></i>');
	} else {
		$airInfo.slideDown();
		$this.html('' + $this.text().replace("展开", "收起") + '<i class="showText-on"></i>');
	}
}


/************** 新添加 *********************/

//预订人信息添加
function textBoxAddPeople(obj) {
	var contactPeopleNum = $("ul[name=orderpersonMes]").length;
	$(obj).parents('.centerNew').append('<div class="textBox">' +
		'<div class="textBox-delete" onclick="textBox_del(this)">删除</div><div class="kong"></div>' +
		'<ul class="ydbz_qd" name="orderpersonMes">' +
		'<li class="labelPr"><label><span class="xing">*</span>渠道联系人<font>' + (contactPeopleNum + 1) + '</font>：</label><input class="required" maxlength="10" type="text" id="orderPersonName' + (contactPeopleNum + 1) + '" name="orderPersonName" value="" onkeyup="this.value=this.value.replaceSpecialChars()"  onafterpaste="this.value=this.value.replaceSpecialChars()"/></li>' +
		'<li class="ydbz_qd_lilong"><label><span class="xing">*</span>渠道联系人电话：</label><input class="required" maxlength="15" type="text" id="orderPersonPhoneNum' + (contactPeopleNum + 1) + '" name="orderPersonPhoneNum" value="" onkeyup="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"  onafterpaste="this.value=this.value.replace(/[^\\d\+\\-]/g,\'\')"/><button class="efx-button" onclick="showText(this)" type="button">展开全部<i></i></button></li>' +
		'<li flag="messageDiv" class="ydbz_qd_close"><ul>' +
		'<li><label>固定电话：</label><input type="text" /></li>' +
		'<li><label>渠道地址：</label><input type="text"/></li>' +
		'<li><label>传真：</label><input maxlength="" type="text"/></li>' +
		'<li><label>QQ：</label><input maxlength="" type="text"/></li>' +
		'<li><label>Email：</label><input maxlength="" type="text"/></li>' +
		'<li><label>渠道邮编：</label><input maxlength="" type="text"/></li>' +
		'<li><label>其他：</label><input maxlength="" type="text"/></li>' +
		'</ul></li></ul>' +
		'</div>');
}

//预订人信息删除
function textBox_del(obj) {
	var $container = $(obj).parents(".centerNew");
	$.jBox.confirm("确定要删除该渠道联系人吗？", "提示", function(v, h, f) {
		if (v == 'ok') {
			$(obj).parent().remove();
			//提示信息
			top.$.jBox.tip('删除成功', 'success');
			//重置联系人序号	
			$container.find(".textBox").each(function(index, element) {
				$(element).find("ul[name=orderpersonMes] li").eq(0).find("font").text(index + 1);
				$(element).find("input[type='text'][id]").each(function(i, obj) {
					$(obj).attr("id", $(obj).attr("id").replace(/\d+$/, index + 1));
				});
				$(element).find("label.error").each(function(i, obj) {
					$(obj).attr("for", $(obj).attr("for").replace(/\d+$/, index + 1));
				});
			});
		} else if (v == 'cancel') {}
	});
}

//发布海岛产品--显示联运价格
function islandShowPrice() {
	$("[data-show='showPrice']").hover(function(event) {
		var $showDiv = $(".proPrice");
		var showDiv_width = $showDiv.width();
		var $flagDiv = $(this).parents(".hotelList");
		var maxWidth = $flagDiv.width();
		var mousePos = mousePosition(event);
		if (showDiv_width > maxWidth) {
			$showDiv.width(maxWidth).css("left", $flagDiv.offset().left + "px");
			showDiv_width = $showDiv.width();
		} else {
			if (mousePos.x < showDiv_width / 2) {
				$showDiv.css("left", $flagDiv.offset().left + "px");
			} else if (($flagDiv.offset().left + maxWidth - $(this).offset().left) < showDiv_width / 2) {
				$showDiv.css("left", ($flagDiv.offset().left + maxWidth - showDiv_width) + "px");
			} else {
				$showDiv.css("left", (mousePos.x - showDiv_width / 2) + "px");
			}
		}
		var showDiv_height = $showDiv.height();
		$showDiv.css("top", ($(this).offset().top - showDiv_height - 15) + "px").show();
	}, function() {
		$(".proPrice").hide();
	});
}

//定义分币种金额对象
function CurrencyMoney(ObjCurrency) {
	var obj = {};
	ObjCurrency.each(function(index, element) {
		var currencyNameShow = $(element).text().replace(/[ ]/g, "");
		obj[currencyNameShow] = 0.00;
	});
	return obj;
}

//验证是否是数值
function isFloat(str) {
	return parseFloat(str) ? parseFloat(str) : 0.00;
}

//暂无游客信息的显示与隐藏
function noTraveler() {
	var num = $("#traveler form").length;
	if (0 == $("#traveler form").length) {
		$("#noVisitor").show();
	} else {
		$("#noVisitor").hide();
	}
	return num;
}

//产品模块：上传资料--团期价格展开收起
function groupStage() {
	$('#secondStepTitle span:last').toggle(function() {
			$(this).text("展开");
			$("#secondStepEnd").hide();
		},
		function() {
			$(this).text("收起");
			$("#secondStepEnd").show();
		});
}

	//机票信息：展开收起机票行程、联运价格（产品、审核）

function airTicketToggle() {
	//展开机票行程
	$(".proList2-expand").click(function() {
		var $showDiv = $("#airTicketDiv");
		if ($showDiv.is(":visible")) {
			$(this).removeClass("proList2-close").text("展开全部");
			$showDiv.hide();
		} else {
			$(this).addClass("proList2-close").text("收起全部");
			$showDiv.show();
		}
	});
	//显示联运价格
	$("[data-show='showPrice']").hover(function(event) {
		var $showDiv = $(".proPrice");
		var showDiv_width = $showDiv.width();
		var $flagDiv = $(this).parents(".proCon");
		var maxWidth = $flagDiv.width();
		var mousePos = mousePosition(event);
		if (showDiv_width > maxWidth) {
			$showDiv.width(maxWidth).css("left", $flagDiv.offset().left + "px");
			showDiv_width = $showDiv.width();
		} else {
			if (mousePos.x < showDiv_width / 2) {
				$showDiv.css("left", $flagDiv.offset().left + "px");
			} else if (($flagDiv.offset().left + maxWidth - $(this).offset().left) < showDiv_width / 2) {
				$showDiv.css("left", ($flagDiv.offset().left + maxWidth - showDiv_width) + "px");
			} else {
				$showDiv.css("left", (mousePos.x - showDiv_width / 2) + "px");
			}
		}
		var showDiv_height = $showDiv.height();
		$showDiv.css("top", ($(this).offset().top - showDiv_height - 15) + "px").show();
	}, function() {
		$(".proPrice").hide();
	});
}

//复制表单元素的值和html
(function($) {
	var oldHTML = $.fn.html;
	$.fn.formhtml = function() {
		if (arguments.length) return oldHTML.apply(this, arguments);

		$("input,textarea,button", this).each(function() {
			this.setAttribute('value', this.value);
		});
		$(":radio,:checkbox", this).each(function() {
			// im not really even sure you need to do this for "checked"
			// but what the heck, better safe than sorry
			if (this.checked) this.setAttribute('checked', 'checked');
			else this.removeAttribute('checked');
		});
		$("option", this).each(function() {
			// also not sure, but, better safe...
			if (this.selected) this.setAttribute('selected', 'selected');
			else this.removeAttribute('selected');
		});
		return oldHTML.apply(this);
	};

	//optional to override real .html() if you want
	// $.fn.html = $.fn.formhtml;
})(jQuery);

/*==============基础信息-产品信息-产品类型 begin=============*/
//基础信息-产品信息-产品类型添加产品类型
function shopProAdd(obj) {
		var id = $(obj).parent().parent().parent().find('div').length;
		var cloneDiv = $(".shopProNone").clone(true);
		cloneDiv.appendTo($(obj).parent().parent().parent());
		cloneDiv.show().removeClass('shopProNone').addClass("shopProP").find('em').text(id);
	}
	//基础信息-产品信息-产品类型删除产品类型

function shopProDel(obj) {
		$(obj).parent().parent().remove();
		$('.shopProP').each(function(index, element) {
			$(this).find('em').text(index + 1);
		});
	}
/*==============基础信息-产品信息-产品类型 end=============*/
//成本html拼接  运控、财务模块
function sumToStrCost(str, currencyName, thenumber) {
		var html = str;
		if (thenumber && thenumber != 0) {
			html += '<li>';
			var theMoney = milliFormat(thenumber, 1);
			var sign = '+';
			var isNegative = /^\-/.test(theMoney);
			if (isNegative) {
				sign = '-';
				theMoney = theMoney.replace(/^\-/g, '');
			}
			if (html != '<li>' || (html == '<li>' && isNegative)) {
				html += sign;
			}
			html += '<font class="gray14">' + currencyName + '</font><span data-money="' + thenumber + '" class="tdred">' + theMoney + '</span></li>';
		}
		return html;
	}

/*==============财务-发票管理-开票（列表、开具发票、开票明细）、订单-发票申请 begin=============*/
//table中“付款金额”列，滑过显示汇率
function exchangerateDiv() {
		$("td[name='exchangeratetd']").hover(function() {
			$(".exchangerate_mouse").show();
			$(this).css("cursor", "pointer")
			var left = $(this).offset().left;
			var top = $(this).parents("tr").offset().top;
			$(".exchangerate_mouse").css({
				"left": left + 10,
				"top": top + 30
			}).show();
		}, function() {
			$(".exchangerate_mouse").hide();
		});
		$(".exchangerate_mouse").hover(function() {
			$(".exchangerate_mouse").show();
		}, function() {
			$(".exchangerate_mouse").hide();
		})
	}
/*==============财务-发票管理-开票（列表、开具发票、开票明细）、订单-发票申请 end=============*/

/*============订单初始脚本设置 begin============*/
function expandAndClose() {
		$(".visitorTit .visitorTit-label").click(function() { //游客信息展开收起
			var $span = $(this).find("span").eq(0);
			var $parent = $(this).parents(".textBox");
			if ($span.hasClass("efx-expand")) { //如果是展开状态
				$parent.find(".forVisitorName").text($parent.find("[name='travelerName']").val());
				$parent.find(".visitorTit-off").show();
				$parent.find(".visitorTit-on").hide();
				$parent.find(".visitorCon").hide();
				$span.removeClass("efx-expand");
			} else {
				$parent.find(".visitorTit-off").hide();
				$parent.find(".visitorTit-on").show();
				$parent.find(".visitorCon").show();
				$span.addClass("efx-expand");
			}
		});
		$(".visitorCon-tit .visitorTit-label").click(function() { //基本信息展开收起
			var $span = $(this).find("span").eq(0);
			var $parent = $(this).parent();
			if ($span.hasClass("efx-expand")) { //如果是展开状态
				$parent.next("[flag='messageDiv']").hide();
				$span.removeClass("efx-expand");
			} else {
				$parent.next("[flag='messageDiv']").show();
				$span.addClass("efx-expand");
			}
		});
	}
/*============订单初始脚本设置 end============*/

//添加其他费用
function addCosts(obj) {
	var $thisForm = $(obj).parents("form");
	$(obj).parent().before($("#otherExpensesTemplate").html());
	var index_form = $thisForm.find(".visitorTit-label i").text();
	//设置其他费用名的id
	$(obj).parent().prev().find("[name='costName']").attr("id", "costName" + index_form + "-" + $thisForm.find("[name='costName']").length);
}

//获取鼠标位置
function mousePosition(ev) {
	if (ev.pageX || ev.pageY) {
		return {
			x: ev.pageX,
			y: ev.pageY
		};
	}
	return {
		x: ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		y: ev.clientY + document.body.scrollTop - document.body.clientTop
	};
}
//操作浮框
function operateHandler() {
    $(document).on("mouseover mouseout", ".handle", function (event) {
        if (event.type == "mouseover") {
            //鼠标悬浮
            if (0 != $(this).find('a').length) {
                $(this).addClass('handle-on');
                $(this).find('dd').addClass('block');
            }
        } else if (event.type == "mouseout") {
            //鼠标离开
            $(this).removeClass('handle-on');
            $(this).find('dd').removeClass('block');
        }
    });
}

//20150716 ajax 异步请求的加载等待
function ajaxStart(){
	var html ='<div class="ajaxLoadingMask"></div><div class="ajaxLoading">正在加载，请等待...</div>';
	$("body").append(html);
}
function ajaxStop(){
	$("div.ajaxLoadingMask, div.ajaxLoading").remove();
}