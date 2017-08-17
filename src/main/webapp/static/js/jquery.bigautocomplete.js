(function($){
	var bigAutocomplete = new function(){
		currentInputText = null;
		this.functionalKeyArray = [9,20,13,16,17,18,91,92,93,45,36,33,34,35,37,39,112,113,114,115,116,117,118,119,120,121,122,123,144,19,145,40,38,27];//�����Ϲ��ܼ��ֵ����
		this.holdText = null;
		
		this.init = function(){
			$("body").append("<div id='bigAutocompleteContent' class='bigautocomplete-layout'></div>");
			$(document).bind('mousedown',function(event){
				var $target = $(event.target);
				if((!($target.parents().andSelf().is('#bigAutocompleteContent'))) && (!$target.is($(currentInputText)))){
					bigAutocomplete.hideAutocomplete();
				}
			})
			
			$("#bigAutocompleteContent").delegate("tr", "mouseover", function() {
				$("#bigAutocompleteContent tr").removeClass("ct");
				$(this).addClass("ct");
			}).delegate("tr", "mouseout", function() {
				$("#bigAutocompleteContent tr").removeClass("ct");
			});		
			
			$("#bigAutocompleteContent").delegate("tr", "click", function() {
				$(currentInputText).val( $(this).find("div:last").html());
				var callback_ = $(currentInputText).data("config").callback;
				if($("#bigAutocompleteContent").css("display") != "none" && callback_ && $.isFunction(callback_)){
					callback_($(this).data("jsonData"));	
				}				
				bigAutocomplete.hideAutocomplete();
			})			
		}
		
		this.autocomplete = function(param){
			
			if($("body").length > 0 && $("#bigAutocompleteContent").length <= 0){
				bigAutocomplete.init();
			}			
			var $this = this;
			var $bigAutocompleteContent = $("#bigAutocompleteContent");
			this.config = {
			               width:0,
			               url:null,
			               data:null,
			               callback:null};
			$.extend(this.config,param);
			$this.data("config",this.config);
		
			$this.keydown(function(event) {
				var node = event.currentTarget;
				switch (event.keyCode) {
				case 40:
					
					if($bigAutocompleteContent.css("display") == "none")return;
					
					var $nextSiblingTr = $bigAutocompleteContent.find(".ct");
					if($nextSiblingTr.length <= 0){
						$nextSiblingTr = $bigAutocompleteContent.find("tr:first");
					}else{
						$nextSiblingTr = $nextSiblingTr.next();
					}
					$bigAutocompleteContent.find("tr").removeClass("ct");
					
					if($nextSiblingTr.length > 0){
						$nextSiblingTr.addClass("ct");
						$(node).val($nextSiblingTr.find("div:last").html());
						$bigAutocompleteContent.scrollTop($nextSiblingTr[0].offsetTop - $bigAutocompleteContent.height() + $nextSiblingTr.height() );
						
					}else{
						$(node).val(bigAutocomplete.holdText);
					}
					
					break;
				case 38:
					if($bigAutocompleteContent.css("display") == "none")return;
					
					var $previousSiblingTr = $bigAutocompleteContent.find(".ct");
					if($previousSiblingTr.length <= 0){
						$previousSiblingTr = $bigAutocompleteContent.find("tr:last");
					}else{
						$previousSiblingTr = $previousSiblingTr.prev();
					}
					$bigAutocompleteContent.find("tr").removeClass("ct");
					
					if($previousSiblingTr.length > 0){
						$previousSiblingTr.addClass("ct");
						$(node).val($previousSiblingTr.find("div:last").html());
						$bigAutocompleteContent.scrollTop($previousSiblingTr[0].offsetTop - $bigAutocompleteContent.height() + $previousSiblingTr.height());
					}else{
						$(node).val(bigAutocomplete.holdText);
					}
					break;
				case 27:
					bigAutocomplete.hideAutocomplete();
					break;
				}
			});		
			
			$this.keyup(function(event) {
				var k = event.keyCode;
				var node = event.currentTarget;
				var ctrl = event.ctrlKey;
				var isFunctionalKey = false;//���µļ��Ƿ��ǹ��ܼ�
				for(var i=0;i<bigAutocomplete.functionalKeyArray.length;i++){
					if(k == bigAutocomplete.functionalKeyArray[i]){
						isFunctionalKey = true;
						break;
					}
				}

				if(!isFunctionalKey && (!ctrl || (ctrl && k == 67) || (ctrl && k == 88)) ){
					var config = $(node).data("config");
					
					var offset = $(node).offset();
					if(config.width <=0){
						config.width  = $(node).outerWidth() - 2
					}
					$bigAutocompleteContent.width(config.width);
					var h = $(node).outerHeight() - 1;
					$bigAutocompleteContent.css({"top":offset.top + h,"left":offset.left});
					
					var data = config.data;
					var url = config.url;
					var keyword_ = $.trim($(node).val());
					if(keyword_ == null || keyword_ == ""){
						bigAutocomplete.hideAutocomplete();
						return;
					}					
					if(data != null && $.isArray(data) ){
						var data_ = new Array();
						for(var i=0;i<data.length;i++){
							if(data[i].title.indexOf(keyword_) > -1){
								data_.push(data[i]);
							}
						}
						
						makeContAndShow(data_);
					}else if(url != null && url != ""){//ajax�������
						/*$.post(url,{keyword:keyword_},function(result){
							makeContAndShow(result.data)
						},"json")*/ //原始的是这个，但它不支持中文传输，故换掉
						$.ajax({ 
					         type: "POST", 
					         contentType: "application/x-www-form-urlencoded; charset=UTF-8", 
					         url: url, 
					         data:{keyword:keyword_}, 
					         dataType:"json", 
					         success:function(result){    
					        	 makeContAndShow(result.data);
					         } 
					    });
					}

					bigAutocomplete.holdText = $(node).val();
				}
				if(k == 13){
					var callback_ = $(node).data("config").callback;
					if($bigAutocompleteContent.css("display") != "none"){
						if(callback_ && $.isFunction(callback_)){
							callback_($bigAutocompleteContent.find(".ct").data("jsonData"));
						}
						$bigAutocompleteContent.hide();						
					}
				}
				
			});	
			
			function makeContAndShow(data_){
				if(data_ == null || data_.length <=0 ){
					return;
				}		
				var cont = "<table><tbody>";
				for(var i=0;i<data_.length;i++){
					cont += "<tr><td><div>" + data_[i].title + "</div></td></tr>"
				}
				cont += "</tbody></table>";
				$bigAutocompleteContent.html(cont);
				$bigAutocompleteContent.show();
				

				$bigAutocompleteContent.find("tr").each(function(index){
					$(this).data("jsonData",data_[index]);
				})
			}			
					
			$this.focus(function(event){
				currentInputText = event.currentTarget;
			});
			
		}

		this.hideAutocomplete = function(){
			var $bigAutocompleteContent = $("#bigAutocompleteContent");
			if($bigAutocompleteContent.css("display") != "none"){
				$bigAutocompleteContent.find("tr").removeClass("ct");
				$bigAutocompleteContent.hide();
			}			
		}
		
	};
	
	$.fn.bigAutocomplete = bigAutocomplete.autocomplete;
	
})(jQuery)

/*自己添加的统一自动匹配事件
 * className：标签的class名称
 * controller：后台处理控制器
 * value:输入框中的值
 * start：从第几位字开始自动匹配
 */
function getAutoCompleteTagFromDatabase(className,controller,obj,start,callbackStr){
	if(null != start && "" != start && "1" != start){
		var startNum = parseInt(start);
		if((obj.value.length + 1) >= startNum){
			if(null != callbackStr && "" != callbackStr){
				$("#" + className).bigAutocomplete({
					url:controller,
					callback:function(data){
						var firstNum = callbackStr.indexOf("(");
						var callbackFunction = callbackStr.substring (0,firstNum);
						var fun = window.eval(callbackFunction);
						fun(data);
					}
				});
			}else{
				$("#" + className).bigAutocomplete({
					url:controller
				});
			}	
		}
	}else{
		if(null != callbackStr && "" != callbackStr){
			$("#" + className).bigAutocomplete({
				url:controller,
				callback:function(data){
					var firstNum = callbackStr.indexOf("(");
					var callbackFunction = callbackStr.substring (0,firstNum);
					var fun = window.eval(callbackFunction);
					fun(data);
				}
			});
		}else{
			$("#" + className).bigAutocomplete({
				url:controller
			});
		}
	}
}