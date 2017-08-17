(function($) {

	$.extend(
					$.fn,
					{
						// 格式化CST日期的字串
						formatCSTDate : function(strDate, formats) {
							var opts = $.extend({}, $.fn.dateformat.defaults,
									formats);
							var format = opts.format;
							return formatDate(new Date(strDate), format);
						},
						// 格式化日期,
						formatDate : function(date, format) {
							var paddNum = function(num) {
								num += "";
								return num.replace(/^(\d)$/, "0$1");
							};
							// 指定格式字符
							var cfg = {
								yyyy : date.getFullYear(),
								yy : date.getFullYear().toString().substring(2),
								M : date.getMonth() + 1,
								MM : paddNum(date.getMonth() + 1),
								d : date.getDate(),
								dd : paddNum(date.getDate()),
								hh : date.getHours(),
								mm : date.getMinutes(),
								ss : date.getSeconds()
							};
							format || (format = "yyyy-MM-dd hh:mm:ss");
							return format.replace(/([a-z])(\1)*/ig,
									function(m) {
										return cfg[m];
									});
						},
						//返回指定格式的日期 解决IE7 NaN的兼容性问题
						parseISO8601 : function(dateStringInRange) {
							var	isoExp = /^\s*(\d{4})-(\d{2})-(\d{2})\s*$/, 
								date = new Date(NaN),
								month,
								parts = isoExp.exec(dateStringInRange);
							if (parts) {
								month = +parts[2];
								date.setFullYear(parts[1], month - 1, parts[3]);
								if (month != date.getMonth() + 1) {
									date.setTime(NaN);
								}
							}
							return date;
						}
					});
	$.fn.dateformat = {
		defaults : {
			format : "yyyy-MM-dd"
		}
	};
})(jQuery);