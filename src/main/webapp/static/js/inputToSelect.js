;(function($,window,document,undefined){
	var ToSelect = function(ele,opt){
		this.$element = ele,
		this.pageNo = 0,
		this.defaults = {
			'url' : '',//请求地址
			'param':{},//ajax请求的参数
			'paraName':'name',//参数中接收input输入值的键名
			'showDivId': 'inputSelect',//生成div的ID
			'message':'没有搜索结果',//没有搜索结果时提示字段
            'blur':'',//失去焦点时触发的事件
            'focus':''//获取焦点时触发的事件
		},
		this.options = $.extend({}, this.defaults, opt)
	}
	ToSelect.prototype = {
		render : function(){
			var self = this;
			//输入框获取焦点时显示备选框
			self.$element.on('focus',function(){
				if(!$('#'+self.options.showDivId).length>0) {
					self._creatShowDiv();
					self._events();
					if(self.$element.val()!=""){
						self.pageNo = 1;
						self.options.param["pageNo"] = self.pageNo;
						self.util.get(self.options.url,self.options.param,self)
					}else{
						self._appendChild();
					}
				}
			});
			//输入框val改变
			self.$element.on('input propertychange', self.util.debounce(function() {
				if(self.options.url){
					self.pageNo = 1;//input value改变后将分页重置为1
					self.util.get(self.options.url,self.options.param,self);
                    if(self.$div) {
                        self.$div.scrollTop(0);
                    }
				}
			}));
			//点击页面别处时隐藏备选框
			$(document).on( 'click', function(e) {
				//备选框未生成的情况下不需要后续操作
				if(!self.$div){
					return;
				}
				var evt = e || window.event;
				var target = evt.srcElement || evt.target;
				while(target) { //循环判断至根节点，防止点击的是span子元素
					if(target.id && (target.id == self.options.showDivId||target.id==self.$element.get(0).id)) {
						return;
				}
					target = target.parentNode;
				}
				//被选框中存在匹配选项 默认选择第一个;没有匹配项则清空输入框
				if(!self.$div.find(".noResult").length>0){
					if($('#'+self.options.showDivId).length>0) {
						var value = self.$div.find("li").get(0).innerHTML;
						self.$element.val(value);
						self._hideDiv();
					}
				}else{
					self.$element.val("");
					self._hideDiv();
				}
			});
		},
		//创建下拉DIV
		_creatShowDiv: function(){
			var self = this;
            if(self.options.focus){
                self.options.focus();
            }
			// self.$element.parent().css("position","relative");
			var width = self.$element.get(0).offsetWidth,
			height=self.$element.offset().top+self.$element[0].offsetHeight,
			left = self.$element.offset().left;
			// height=self.$element.get(0).offsetHeight+(+self.$element.parent().css('padding-top').slice(0,-2)),
			// left = self.$element.get(0).offsetLeft;
			var $div = $('<div>').addClass('toSelect').css({"width":width-2,"top":height+1,"left":left}).append('<ul>');
			//参数有设置下拉DIV的ID
			if(self.options.showDivId){
				$div.attr("id",self.options.showDivId);
			}
			// self.$element.after($div);
            $('body').append($div);
			self.$div = $div;
		},
		//移除下拉DIV
		_hideDiv : function(){
			var self = this;
			$('#'+self.options.showDivId).remove();
            if(self.options.blur){
                self.options.blur();
            }
		},
		//向被选框中添加内容 data 数据；pageNo如果是第一页则清空备选框，如果非1则在当前数据后追加
		_appendChild : function(data, pageNo){
			var self = this;
			if(data&&data.length>0){
				//有返回数据
				var list = self._buildLi(data).join("");
				var $ul = pageNo==1?$("#"+self.options.showDivId).find("ul").html(""):$("#"+self.options.showDivId).find("ul");
				$ul.append(list);
			}else{
				var list ="<li class='noResult'>"+self.options.message+"</li>";
				$("#"+self.options.showDivId).find("ul").html("").append(list);
			}
		},
		//添加选中状态
		_beActive : function(obj){
			$(obj).addClass('activeLi').siblings().removeClass('activeLi');
			$(obj).focus().siblings().blur();
		},
		//生成li标签
		_buildLi : function(data){
			var listData=[];
				for(var i = 0,length=data.length;i<length;i++){
					var dataLi = data[i];
					listData.push("<li tabindex = '0' id='"+dataLi.id+"'title='"+dataLi.agentName+"'>"+dataLi.agentName+"</li>");
				}
				return listData;
		},
		//绑定事件
		_events : function() {
			var self = this;
			//为下拉框添加点击事件
			self.$div.on( 'click', 'li',function(e) {
           		 	var evt = e || window.event;
           		 	var target = evt.srcElement || evt.target;
           			 if(!$(target).hasClass('noResult')){
           				 var value = target.innerHTML,
							 id = target.id;
					self.$element.val(value).attr('data-id',id);
            				self._hideDiv();
            			}
            		});
			//为下拉框添加鼠标悬浮事件
		 	self.$div.on('mouseover','li:not(".noResult")',function(e){
				self.util.stopBubble(e);
				var evt = e || window.event;
				var target = evt.srcElement || evt.target;
				self._beActive(this);
			});
			self.$div.on('mouseout','li',function(e){
				self.util.stopBubble(e);
				// $(this).removeClass('activeLi').blur();
			});
			//为下拉框添加键盘事件——上下键和enter
			$(document).off('keydown');
			$(document).on('keydown',function(e){
				if((!self.$div||self.$div.css("display")=="none") ){
					return;
				}
				var evt=e||window.event,
					index = $('.activeLi').index(),
					$li = self.$div.find('li');
				//按up键
				if(evt.keyCode == 38){
					if(index==0 || self.$div.find('.noResult').length>0){
						return;
					}else if(index!==-1 ){
						self._beActive($li.eq(index-1));
					}
					self.util.stopBubble(evt);
					self.util.stopDefault(evt);
				//按down键
				}else if(evt.keyCode == 40){
					if(index==$li.last().index() || self.$div.find('.noResult').length>0){
						return;
					}else{
						self._beActive($li.eq(index+1));
					}
					self.util.stopBubble(evt);
					self.util.stopDefault(evt);
				//按enter
				}else if(evt.keyCode == 13){
					self.$element.val($('.activeLi').text()).attr('data-id',$('.activeLi').attr('id'));
					self.pageNo = 1;
					self._hideDiv();
				}
			});
			//滚动事件，滚动到底部异步加载
			self.$div.on('scroll',function(){
				var scrollHeight = $(this).find('ul').height()+20,
					scrollTop = $(this)[0].scrollTop,
					height = $(this).height();
				if(scrollTop + height >= scrollHeight){//20是ul的padding
					self.util.get(self.options.url,self.options.param,self);
				}
			});
		},
		util: {
			//ajaxUrl--请求地址，paraObj--传的参数
			get:function(ajaxUrl, paraobj,obj) {
				var self = obj;
				paraobj[self.options.paraName] =self.$element.val().replace(/^\s+|\s+$/g,'');//去掉前后空格
				if(paraobj[self.options.paraName]==""){
					//输入为空或空格时，显示没有搜索结果
					self._appendChild();
					return;
				}else if(self.pageNo==-1){
					return;
				}
				paraobj["pageNo"] = self.pageNo;
				param = JSON.stringify(paraobj);
				$.ajax({
        				type: "POST",
				       url: ajaxUrl,
				       data: {'param':param},
				       dataType:'json'
				}).done(function(data){
					if(data&&data.length>0) {
						self._appendChild(data, self.pageNo);
						self.pageNo++;
						if(data.length<100){self.pageNo=-1}//如果下一页没有数据
					}else{
						self._appendChild();
					}
				}).fail(function(data){
					console.log('ajax failed')
				});
			},
			stopBubble: function(e){
				 if ( e && e.stopPropagation ){
					e.stopPropagation();
				 }else{
				 	window.event.cancelBubble = true;
				}
			},
			stopDefault : function(e){
				if ( e && e.preventDefault ){
					e.preventDefault();
				}
				else{
					window.event.returnValue = false;
					return false;
				}
			},
			debounce : function(fn){
				var timer = null;
				function delay() {
					var target = this;
					var args = arguments;
					return setTimeout(function(){
						fn.apply(target, args);
					}, 350);
				}
				return function() {
					if (timer) {
						clearTimeout(timer);
					}
					timer = delay.apply(this, arguments);
				}
			}
	   }
	}
	$.fn.inputToSelect = function(options){
		var toSelect = new ToSelect(this,options)
		return toSelect.render();
	}
})(jQuery, window, document);
