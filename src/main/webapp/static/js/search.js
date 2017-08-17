$(function(){
	/*切换出发城市选中状态*/
	$(".search-item .items-container span").each(function(){
		$(this).click(function(){
			$(this).parent().children("span[class='selected']").removeClass("selected");
			if(!$(this).hasClass("date-text")){
				$(this).addClass("selected");
				if($(this).parent().parent().attr('id') == 'departureDate'){
					$(".dateinput").val("");
				}
			}
		});
	});
	/*展开更多出发城市*/
	$(".more-item").each(function(){
		$(this).click(function(){
			if($(this).children().last().hasClass("fa fa-caret-down")){
				$(this).parent().parent().css('height','auto');
				$(this).children().last().attr('class','fa fa-caret-up');
			}else{
				$(this).parent().parent().css('height','50px');
				$(this).children().last().attr('class','fa fa-caret-down');
			}
			//兼容ie
			if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
				var ddH = $(this).parent().parent().height();
				$(this).parent().parent().prev().height(ddH);
			}
		});
	});
	/*展开更多搜索条件*/
	$(".oper-condition").click(function(){
		if($(this).children().last().hasClass("fa fa-caret-down")){
			$(this).parent().children("dl[class='search-item display-none']").removeClass('display-none');
			$("#departureDate").removeClass("last-item");
			$(this).html('收起条件<i class="fa fa-caret-up" aria-hidden="true"></i>');
		}else{
			$(this).parent().children("dl[name='hideItem']").addClass('display-none');
			$("#departureDate").addClass("last-item");
			$(this).html('更多条件<i class="fa fa-caret-down" aria-hidden="true"></i>');
		}
	});
	/*点击下拉款*/
	$(".dl-select input").click(function(){
		   $(this).parent().children("ul").toggle();
		    var event = getE();
		    if((navigator.userAgent.indexOf('MSIE') >= 0) && (navigator.userAgent.indexOf('Opera') < 0)){
		        event.cancelBubble = true;
		    }else{
		        event.stopPropagation();
		    }
	});
	$(".dl-select ul li").each(function(){
		$(this).click(function(){
			var value = $(this).text();
			$(this).parent().hide();
			$(this).parent().parent().children("input").val(value);
		});
	});
});

//兼容各浏览器的event对象
function getE(){
     if(window.event)    {return window.event;}
     func=getE.caller;
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
