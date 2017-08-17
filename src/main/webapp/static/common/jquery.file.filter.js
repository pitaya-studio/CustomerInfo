/**
 * 文件过滤器
 */
(function($){
	
	$.extend($.fn,
		{
			fileInclude:function(options){
				
				var type = $(this).val();
				var opts = $.extend({},$.fn.fileTypes,options);
				var flag = false;
				$.each(opts.includes,function(index,s){
					var ss = type.substring((type.length-s.length),type.length);
					if(ss == s){
						flag = true;
						return false;
					}
						
				});
				return flag;
			}
		}
	);
	$.fn.fileTypes = {
		includes:[],
		excludes:[]			
	};
})(jQuery);