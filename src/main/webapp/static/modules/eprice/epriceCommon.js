$(function(){
	//文本框提示信息显示隐藏
	$("input:text[flag=istips]").flagTips();
	
	// 关闭当前页
	$("a[name=page-clock]").click(function(){
		location.href= "javascript:window.opener=null;window.close();";
	});
	
	// 打开或关闭人数明细
	$("a[name=expand_detailsSum]").click(function(){
		var val = $("#expand_detailsSum").html();
		if("收起"==val){
			$("#expand_detailsSum").html("展开");
			$("#ifShow_detailsSum").hide();
			$("#base_allPersonSum").removeAttr("readonly");
		}else{
			$("#expand_detailsSum").html("收起");
			$("#ifShow_detailsSum").show();
			$("#base_allPersonSum").attr("readonly","readonly");
		}
	});
	
	// 下载文件
	$("#downloads").click(function(){
		var docid = $("#downloadId").val();
		window.open(contextPath+"/eprice/manager/ajax/project/file/download/"+docid);
    });
	
});

(function($){
	$.fn.flagTips=function(){
		$(this).each(function(){
			var tips = $(this).next("span.ipt-tips");
			if(tips.length<1){
				if ($(this).attr("title")) {
					$(this).after('<span class="ipt-tips">'+$(this).attr("title")+'</span>');
				} else {
					$(this).after('<span class="ipt-tips"></span>');
				}
				tips = $(this).next("span.ipt-tips");
			}
			if($(this).val()!=""){
				$(tips).hide();
			}
			
			$(this).next(".ipt-tips").bind("click",function(){
				var obj_this = $(this);
				obj_this.prev("input").focus();
			});
		});
		
		$(this).bind("focus",function(){
			var obj_this = $(this);
			obj_this.next("span").hide();
		}).bind("blur",function(){
			var obj_this = $(this);
			if(obj_this.val()!=""){
				obj_this.next("span").hide();
			}else{
				obj_this.next("span").show();
			}
		});
		
	};
	
})(jQuery);

/**
 * @version 0.0.1
 * @author:xulh
 * @description 将时间戳转换成以pattern日期格式转成对应的日期字符串
 * @param  {Object} s 
 * 				s为json对象(对象直接体)，下面为此json对象的组成:	<br/><br/>
 * 				time:Number 时间戳;	<br/><br/>
 * 				pattern:String 日期日期格式;	<br/><br/>
 * @example
 * 
 * 	$.trekizFormatDate({time:10000000,pattern: "yyyy-MM-dd hh:mm:ss"});
 * 			
 * @return {String}
 */
 
 jQuery.trekizFormatDate = function (s){
		   var d ;
		   var pattern;
		   if(typeof(s)=="number"){
			   d = new Date(parseInt(s));
			   s = {};
		   }else if(typeof(s.time)=="number" || typeof(s.time)=="string"){
		   		d = new Date(parseInt(s.time)); 
		   }else if(typeof(s.time)=="object"){
		   		d = new Date(parseInt(s.time)); 
		   }else{
		   		d = new Date();
		   }
		   
		   pattern = s.pattern || "yyyy-MM-dd hh:mm:ss";
		    
		   var y = d.getFullYear();
		   var MM = d.getMonth() + 1;
		   var dd = d.getDate();
		   var hh = d.getHours();
		   var mm = d.getMinutes();
		   var ss = d.getSeconds();
		   
		   return	format(y,MM,dd,hh,mm,ss,pattern);
		  
			
		   function format(y,M,d,h,m,s,pattern){
				
				var yy = (y+"").substring(2);
				var MM = M;
				var dd = d;
				var hh = h;
				var mm = m;
				var ss = s;
				
				if(MM<10){
					MM="0"+MM;
				}
				
				if(dd<10){
					dd="0"+dd;
				}
				
				if(hh<10){
					hh="0"+hh;
				}
				
				if(mm<10){
					mm="0"+mm;
				}
				
				if(ss<10){
					ss="0"+ss;
				}
				
				pattern = pattern.replace(/[y]{4}/g,y);
				pattern = pattern.replace(/[y]{2}/g,yy);
				pattern = pattern.replace(/[M]{2}/g,MM);
				pattern = pattern.replace(/[M]{1}/g,M);
				pattern = pattern.replace(/[d]{2}/g,dd);
				pattern = pattern.replace(/[d]{1}/g,d);
				pattern = pattern.replace(/[h]{2}/g,hh);
				pattern = pattern.replace(/[h]{1}/g,h);
				pattern = pattern.replace(/[m]{2}/g,mm);
				pattern = pattern.replace(/[m]{1}/g,m);
				pattern = pattern.replace(/[s]{2}/g,ss);
				pattern = pattern.replace(/[s]{1}/g,s);
				
				return pattern;
			}
};



/**
 * js分页页码展示组件，算法通过java里的分页算法转换出来的
 * @author lihua.xu
 * @时间 2014年9月30日
 */
(function($){
	$.fn.pageDom=function(option){
		
		option = option || {};
		
		function pageDomDeal($div,eventFun,count,pageNo,pageSize){
			var sb = "";
			var plength = 8;// 显示页面长度
			var slider = 1;// 前后显示页面长度
			var first;// 首页索引
			var last;// 尾页索引
			var prev;// 上一页索引
			var next;// 下一页索引
			
			var totalPage;
		
			var firstPage;//是否是第一页
			var lastPage;//是否是最后一页
			
			
			initialize();
			
			sb += '<ul>';
			if (pageNo == first) {// 如果是首页
				sb += '<li class="disabled"><a >&#171; 上一页</a></li>';
			} else {
				sb += '<li><a pn="'+(pageNo-1)+'" >&#171; 上一页</a></li>';
			}
	
			var begin = pageNo - parseInt(plength / 2);
	
			if (begin < first) {
				begin = first;
			}
	
			var end = begin + plength - 1;
	
			if (end >= last) {
				end = last;
				begin = end - plength + 1;
				if (begin < first) {
					begin = first;
				}
			}
			
			
			
			if (begin > first) {
				var i = 0;
				for (i = first; i < first + slider && i < begin; i++) {
					sb += '<li><a pn="'+i+'">'+ (i + 1 - first) + '</a></li>';
				}
				if (i < begin) {
					sb += '<li class="disabled"><a >...</a></li>';
				}
			}
	
			for (var i = begin; i <= end; i++) {
				if (i == pageNo) {
					sb += '<li class="active"><a pn="'+i+'">' + (i + 1 - first)+ '</a></li>';
				} else {
					sb += '<li><a pn="'+i+'">'+ (i + 1 - first) + '</a></li>';
				}
			}
	
			if (last - end > slider) {
				sb += '<li class="disabled"><a href="javascript:">...</a></li>';
				end = last - slider;
			}
	
			for (var i = end + 1; i <= last; i++) {
				sb += '<li><a pn="'+i+'">'+ (i + 1 - first) + '</a></li>';
			}
	
			if (pageNo == last) {
				sb += '<li class="disabled"><a >下一页 &#187;</a></li>';
			} else {
				sb += '<li><a pn="'+(pageNo+1)+'" >下一页 &#187;</a></li>';
			}
	
			sb += '<li class="disabled controls"><a >第';
			sb += '<input type="text" value="'+pageNo+'" name="pninput" onclick="this.select();"/>/';
			
			// 计算总页数
			var num = Math.ceil(count/pageSize);
			if(!isNaN(num)){
				totalPage = num;
			}else{
				totalPage = 0;
			}
			
			sb += totalPage+'页 ,   每页';
			
			sb += '<input type="text" value="'+pageSize+'" name="psinput" onclick="this.select();"/> 条，';
			sb += '共 ' + count + ' 条</a></li>';
	
			sb += '</ul><div style="clear:both;"></div>';
			
			sb = $(sb);
			
			$(sb).find("a[pn]").click(function(){
				eventFun($(this).attr("pn"), pageSize);
			});
			
			
			$(sb).find("input[name=pninput]").keypress(function(e){
				var c=e.which;
				if(c==13){
					pageNo = $(this).val();
					eventFun(pageNo, pageSize);
				}
			});
			
			$(sb).find("input[name=psinput]").keypress(function(e){
				var c=e.which;
				if(c==13){
					pageSize = $(this).val();
					if(pageSize <= 0){
						pageSize = 10;
					}
					eventFun(pageNo, pageSize);
				}
			});
			
			$($div).html(sb);
			
			/**
		 * 初始化参数
		 */
		function initialize(){
					
			first = 1;
			
			
			
			last = parseInt(count / (pageSize < 1 ? 20 : pageSize)) + first - 1;
			
			if (count % pageSize != 0 || last == 0) {
				last++;
			}
	
			if (last < first) {
				last = first;
			}
			
			if (pageNo <= 1) {
				pageNo = first;
				firstPage=true;
			}
	
			if (pageNo >= last) {
				pageNo = last;
				lastPage=true;
			}
	
			if (pageNo < last - 1) {
				next = pageNo + 1;
			} else {
				next = last;
			}
	
			if (pageNo > 1) {
				prev = pageNo - 1;
			} else {
				prev = first;
			}
			
			//2
			if (pageNo < first) {// 如果当前页小于首页
				pageNo = first;
			}
	
			if (pageNo > last) {// 如果当前页大于尾页
				pageNo = last;
			}
			
			pageNo = parseInt(pageNo);
			
		}
	}
	
	pageDomDeal(this,option.eventFun,option.count,option.pageNo,option.pageSize);
		
	};
	
})(jQuery);

/**
 * 新增询价，询同行客户，当选择渠道时，填充联系人及电话
 */
function fillContactByAgent(obj) {
	$.ajax({
		async : false,
		type : "POST",
		data : {
			agentId : $(obj).val()
		},
		url : sysCtx + "/agent/manager/getFirstContact",
		success : function (result) {
			if (result) {
				if (result.flag == "success" && result.data) {
			    	$("#togetherman").val(result.data.contactName);
			    	$("#togetherphone").val(result.data.contactMobile);
				} else {
					$.jBox.tip(result.message, "警告");
				}
			} else {
				$.jBox.tip("获取客户联系人信息失败", "警告");
			}
		}
	});
}

//bug17577
function resetSearchParams() {
	$(':input', '#eprice-search-form-id')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
	$('#eprice-search-form-id').find('.custom-combobox').each(function(){
		$(this).prev().val("");
	})

}