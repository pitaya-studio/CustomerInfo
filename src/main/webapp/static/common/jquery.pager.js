/*
 * jQuery pager plugin
 * Version 1.0 (12/22/2008)
 * @requires jQuery v1.2.6 or later
 * Download by http://www.codefans.net
 * Example at: http://jonpauldavies.github.com/JQuery/Pager/PagerDemo.html
 *
 * Copyright (c) 2008-2009 Jon Paul Davies
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Read the related blog post and contact the author at http://www.j-dee.com/2008/12/22/jquery-pager-plugin/
 *
 * This version is far from perfect and doesn't manage it's own state, therefore contributions are more than welcome!
 *
 * Usage: .pager({ pagenumber: 1, pagecount: 15, buttonClickCallback: PagerClickTest });
 *
 * Where pagenumber is the visible page number
 *       pagecount is the total number of pages to display
 *       buttonClickCallback is the method to fire when a pager button is clicked.
 *
 * buttonClickCallback signiture is PagerClickTest = function(pageclickednumber) 
 * Where pageclickednumber is the number of the page clicked in the control.
 *
 * The included Pager.CSS file is a dependancy but can obviously tweaked to your wishes
 * Tested in IE6 IE7 Firefox & Safari. Any browser strangeness, please report.
 */
(function($) {

	$.fn.pager = function(options) {
		var opts = $.extend({}, $.fn.pager.defaults, options);
		options = opts;
		if($.fn.pager.defaults.doFirst){
		      getPageData(1,options.url,options,$(this),options.buttonClickCallback);
		      $.fn.pager.defaults.doFirst = false;
		}
		
		return this.each(function() {
			         var pager = $(this);
			         
			         //对回调进行处理   按钮点击处理
			         var callBackFunction = function(data,appendDate){
			         	var optsTemp = $.extend(opts, {pagenumber : data},appendDate);
			             //获取数据库数据
			             getPageData(data,options.url,optsTemp,pager,options.buttonClickCallback);
			         }
			         
					// empty out the destination element and then render out the pager with the supplied options
					$(this).empty().append(
					renderpager(parseInt(options.pagenumber),
							    parseInt(options.pagecount),
							    parseInt(options.pageRecordNum),
                                options,
							    callBackFunction)
							    );
                            
					// specify correct cursor activity
					$('.pages li').mouseover(function() {
								document.body.style.cursor = "pointer";
							}).mouseout(function() {
								document.body.style.cursor = "auto";
							});
				});
	};
	//这里应该考虑传入用户自定义数据的情况
	function getPageData(data,url,options,pager,successFunction) {
		var dataTemp = $.extend({
                       pagenumber : options.pagenumber,//当前页
                       pagecount : options.pagecount,//总页数
                       pageRecordNum : options.pageRecordNum//每页记录数
                   },options.userData);
		
                $.ajax({
                   type: "POST",
                   url: url,
                   data: dataTemp,
                   success: function(msg){
                   	    //回调处理重新注册
                   	    options = $.extend(options,{pagecount: msg.pagecount,recordNum:msg.recordNum});
                        pager.pager(options);
                        successFunction(msg);
                   }
                });
     };
     
	// render and return the pager with the supplied options
	function renderpager(pagenumber, pagecount,pageRecordNum,options, buttonClickCallback) {
		// setup $pager to hold render
		var $pager = $('<ul class="pages"></ul>');
		// add in the previous and next buttons
		$pager
		.append(renderButton('首页', pagenumber, pagecount,buttonClickCallback,"first"))
		.append(renderButton('上一页', pagenumber,pagecount, buttonClickCallback,"prev"));
		// pager currently only handles 10 viewable pages ( could be easily parameterized, maybe in next version ) so handle edge cases
		var startPoint = 1;
		var endPoint = 9;

		if (pagenumber > 4) {
			startPoint = pagenumber - 4;
			endPoint = pagenumber + 4;
		}

		if (endPoint > pagecount) {
			startPoint = pagecount - 8;
			endPoint = pagecount;
		}

		if (startPoint < 1) {
			startPoint = 1;
		}

        var begin = Number(Number(pagenumber) - 4);
        if (begin < 1) {
            begin = 1;
        }
        
        var end = Number(begin + 7);

        if (end >= Number(pagecount)) {
            end = Number(pagecount);
            begin = end - 7;
            if (begin < 1) {
                begin = 1;
            }
        }
        
        //alert(begin+"....."+end);
        
        if (begin > 1) {
            //var i = 0;
            for (var i = 1; i < 2 && i < begin; i++) {
                var $li = $('<li class="page-number">' + (i)+ '</li>');
                
                i == pagenumber? $li.addClass('pgCurrent'):$li.click(function() {
                                buttonClickCallback(this.firstChild.data);
                            });
                $pager.append($li);
            }
            if (i < begin) {
                $pager.append($('<li class="page-number">...</li>'));
            }
        }
        
        
        
        
        for (var i = begin; i <= end; i++) {
            if (i == Number(pagenumber)) {
                var $li = $('<li class="page-number">' + (i)+ '</li>');
            i == pagenumber
                    ? $li.addClass('pgCurrent')
                    : $li.click(function() {
                                buttonClickCallback(this.firstChild.data);
                            });
            $pager.append($li);
            } else {
                var $li = $('<li class="page-number">' + (i)+ '</li>');
            $li.click(function() {
                              buttonClickCallback(this.firstChild.data);
                          });
            $pager.append($li);
            }
        }

        if (Number(pagecount) - end > 1) {
            $pager.append($('<li class="page-number">...</li>'));
            end = Number(pagecount) - 1;
        }

        for (var i = end + 1; i <= Number(pagecount); i++) {
            var $li = $('<li class="page-number">' + (i)+ '</li>');
            i == pagenumber
                    ? $li.addClass('pgCurrent')
                    : $li.click(function() {
                                buttonClickCallback(this.firstChild.data);
                            });
            $pager.append($li);
        }
        
		// loop thru visible pages and render buttons
//		for (var page = startPoint; page <= endPoint; page++) {
//
//			var currentButton = $('<li class="page-number">' + (page)
//					+ '</li>');
//
//			page == pagenumber
//					? currentButton.addClass('pgCurrent')
//					: currentButton.click(function() {
//								buttonClickCallback(this.firstChild.data);
//							});
//			currentButton.appendTo($pager);
//		}

		// render in the next and last buttons before returning the whole rendered control back.
		$pager.append(renderButton('下一页', pagenumber, pagecount,
				buttonClickCallback,"next")).append(renderButton('末页', pagenumber,
				pagecount, buttonClickCallback,"last"));
        //$pager.append($("<div><span>当前</span><input value="+pagenumber+">/<input value="+pageRecordNum+">条,共"+options.recordNum+"条</div>"));
        $pager.append($("<div><span>当前</span></div>")
        .append(renderInputpagenumber(pagenumber,pageRecordNum,buttonClickCallback,pagecount))
        .append($("<span>/</span>"))
        .append(renderInputpageRecordNum(pagenumber,pageRecordNum,buttonClickCallback))
        .append($("<span>条,共"+options.recordNum+"条</span>"))
        );
		return $pager;
	}

    
    function renderInputpagenumber(pagenumber,pageRecordNum,buttonClickCallback,pagecount){
        //当前页数
        var input = $("<input value="+pagenumber+">").keydown(function(event){ 
            var c=event.keyCode||event.which;
            if(c==13){
                $(this).val($(this).val().replace(/\D/g,""));
                if($(this).val()==""||$(this).val()=="0"){return;}
                if(Number($(this).val()>pagecount)){
                    $(this).val(pagecount);
                }
                buttonClickCallback($(this).val());
            }
        }); 
       return input;
    }
    function renderInputpageRecordNum(pagenumber,pageRecordNum,buttonClickCallback){
        //每页记录条数
         //当前页数
        var input = $("<input value="+pageRecordNum+">").keydown(function(event){ 
            var c=event.keyCode||event.which;
            if(c==13){
            $(this).val($(this).val().replace(/\D/g,""));
            if($(this).val()==""||$(this).val()=="0"){return;}
                buttonClickCallback(pagenumber,{pageRecordNum:$(this).val()});
            }
        }); 
       return input;
    }
    
	// renders and returns a 'specialized' button, ie 'next', 'previous' etc. rather than a page number button
	function renderButton(buttonLabel, pagenumber, pagecount,
			buttonClickCallback,pageId) {

		var $Button = $('<li class="pgNext" id="'+pageId+'">' + buttonLabel + '</li>');

		var destPage = 1;

		// work out destination page for required button type
		switch (pageId) {
			case "first" :
				destPage = 1;
				break;
			case "prev" :
				destPage = pagenumber - 1;
				break;
			case "next" :
				destPage = pagenumber + 1;
				break;
			case "last" :
				destPage = pagecount;
				break;
		}

		// disable and 'grey' out buttons if not needed.
		if (pageId == "first" || pageId == "prev") {
			pagenumber <= 1 ? $Button.addClass('pgEmpty') : $Button.click(
					function() {
						buttonClickCallback(destPage);
					});
		} else {
			pagenumber >= pagecount ? $Button.addClass('pgEmpty') : $Button
					.click(function() {
								buttonClickCallback(destPage);
							});
		}
		return $Button;
	}

	// pager defaults. hardly worth bothering with in this case but used as placeholder for expansion in the next version
	$.fn.pager.defaults = {
		pagenumber : 1,
		pagecount : 1,
		pageRecordNum : 20,//每页记录数
		doFirst : true
	};

})(jQuery);
