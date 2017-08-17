(function($) {
    $.extend($.fn,{
         disabledselect:function(options){
            var opts = $.extend({}, $.fn.disabledshow.defaults, options);
            //$(this).attr("disabled","disabled");
            if($(this).hasClass("disabledClass")){
                return ;
            }
            
            $(this).addClass("disabledClass");
            $(this).hide();
            var nodeName =$(this)[0].nodeName; 
            var value = $(this).val();            
            var showText = value;
            if(value==""){
                showText = opts.blankText;
            }else{
                if(nodeName=="SELECT"){                	
                    showText=$(this).find("option:selected").text();
                }else if(nodeName=="INPUT"){
                    showText=value;
                }
            }
            var showVal = showText;
            //如果传入明知  就改为千位付格式化 
            if($.inArray($(this).attr("name"), opts.formatNumber)>=0){
                showVal = showVal.formatNumberMoney('#,###');
            }
            
            if($.inArray($(this).attr("id"), opts.tipTarget)>=0){
            	if(showText.length>10){
                	showVal = showText.substring(0,7)+"...";
                	var $span = $("<span class='disabledshowspan' title='"+showText+"'>"+showVal+"</span>");
                }else{
                	$span = $("<span class='disabledshowspan'>"+showVal+"</span>");
                }
            }else{
            	$span = $("<span class='disabledshowspan'>"+showVal+"</span>");
            }
            if($(this).attr('id')=='trafficName'){
            	
            	var trafficModeValue=$("#trafficMode option:selected").val();
            	if(trafficModeValue) {
					$(this).after($span);
				}
				
            }else{
	            $(this).after($span);
            }
        },
        disabledradio:function(options){
            var opts = $.extend({}, $.fn.disabledshow.defaults, options);
            if($(this).attr("checked")){
                $(this).addClass("disabledClass").hide();
            }else{
                $(this).parent().addClass("disabledClass").hide();
            }
        },
        removedisabledradio:function(){
             if($(this).attr("checked")){
                $(this).removeClass("disabledClass").show();
            }else{
                if($(this).parent().hasClass("disabledClass")){
                    $(this).parent().removeClass("disabledClass");
                    $(this).parent().show();
                }
                
            }
        },
        removedisabledselect:function(){
        	if($(this).hasClass("disabledClass")){
        		//$(this).removeAttr("disabled");
        		$(this).removeClass("disabledClass");
                $(this).show();
                var tempSpan = $(this).next();
                if(tempSpan.hasClass("disabledshowspan"))
                	tempSpan.remove();
        	}           
        },
        disableContainer:function(options,tips){
			var opts = $.extend({}, $.fn.disabledshow.defaults, options);
            this.find("a").addClass("displayClick");
            this.find(".xing").addClass("displayClick");
            this.find("input[type='button']").addClass("displayClick");
            this.find("input[type='submit']").addClass("displayClick");
            this.find("button").addClass("displayClick");
            //input type=text
            var texts = this.find("input[type='text']");
            $.each(texts,function(key,value){
                $(value).disabledselect(options,tips);
            });
            //input not type attr
            texts = this.find("input:not([type])");
            $.each(texts,function(key,value){
                $(value).disabledselect(options,tips);
            });
            texts = this.find("input[type='radio']");
            $.each(texts,function(key,value){
                $(value).disabledradio(options);
            });
            //select 
			if(1 == opts.selectIsVisible){
            	texts = this.find("select:visible");
			}else{
				texts = this.find("select");
			}
            $.each(texts,function(key,value){
                $(value).disabledselect(options,tips);
            });
            
            texts = this.find("textarea");
            $.each(texts,function(key,value){
                $(value).disabledselect(options,tips);
            });
            
        },
        undisableContainer:function(){
            this.find("a").removeClass("displayClick");
            this.find(".xing").removeClass("displayClick");
            this.find("input[type='button']").removeClass("displayClick");
            this.find("input[type='submit']").removeClass("displayClick");
            this.find("button").removeClass("displayClick");
            //input type=text
            var texts = this.find("input[type='text']");
            $.each(texts,function(key,value){
                $(value).removedisabledselect();
            });
            texts = this.find("input[type='radio']");
            $.each(texts,function(key,value){
                $(value).removedisabledradio();
            });
            //input not type attr
            texts = this.find("input:not([type])");
            $.each(texts,function(key,value){
                $(value).removedisabledselect();
            });
            //select 
            texts = this.find("select");
            $.each(texts,function(key,value){
                $(value).removedisabledselect();
            });
            
            texts = this.find("textarea");
            $.each(texts,function(key,value){
                //$(value).adddisabledClass();
                $(value).removedisabledselect();
            });
    }
    });
    $.fn.disabledshow={
        defaults:{
            blankText : "—",
            formatNumber:[],
            tipTarget:[],
			selectIsVisible:1//1:select需要显示，0不限
        }
    };
})(jQuery);

