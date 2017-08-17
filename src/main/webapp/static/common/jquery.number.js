(function($){
	
	$.extend($.fn,{
		
		//千为符处理
		commafy:function(num){
			if ($.trim(num + "") == "") {
				return "";
			}
			if (isNaN(num)) {
				return "";
			}
			num = num + "";
			if (/^.*\..*$/.test(num)) {
				var pointIndex = num.lastIndexOf(".");
				var intPart = num.substring(0, pointIndex);
				var pointPart = num.substring(pointIndex + 1, num.length);
				intPart = intPart + "";
				var re = /(-?\d+)(\d{3})/;
				while (re.test(intPart)) {
					intPart = intPart.replace(re, "$1,$2");
				}
				num = intPart + "." + pointPart;
			} else {
				num = num + "";
				var re = /(-?\d+)(\d{3})/;
				while (re.test(num)) {
					num = num.replace(re, "$1,$2");
				}
			}
			return num;
		},
		
		/**
		 * 去除千分位
		 *@param{Object}num
		 */
		delcommafy:function(num){

		   if((num+"").trim()==""){
		      return"";
		   }
		   num=num.replace(/,/gi,'');
		   
		   return num;
		}
	});
})(jQuery);