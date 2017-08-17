// JavaScript Document
//select模糊匹配插件 使用jquery.ui的widget
(function ($) {
    if ($.widget) {
        //下拉框模糊匹配多选
        $.widget("custom.comboboxInquiry", {
        	widgetEventPrefix: "comboboxInquiry",
            options: {
                removeIfInvalid: true
            },
        	_create: function () {
                this.wrapper = $("<span>").addClass("custom-combobox").insertAfter(this.element);
                this.element.hide();
                this._createAutocomplete();
                this._createShowAllButton();
                this.reset();
            },

            _createAutocomplete: function () {
                var selected = this.element.children(":selected"),
				value = selected.val() ? selected.text() : "";

                this.input = $("<input>").appendTo(this.wrapper).val(value).attr("title", "").addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left").autocomplete({
                    delay: 0,
                    minLength: 0,
                    source: $.proxy(this, "_source")
                });
               var input = this.input;
                this._on(this.input, {
                    autocompleteselect: function (event, ui) {
                        ui.item.option.selected = true;
                        this._trigger("select", event, {
                            item: ui.item.option
                        });
                    }, autocompletechange: "_removeIfInvalid"
                });
            },

            _createShowAllButton: function () {
                var input = this.input,
				wasOpen = false;

                $("<a>").attr("tabIndex", -1).attr("title", "选择").tooltip().appendTo(this.wrapper).button({
                    icons: { primary: "ui-icon-triangle-1-s" },
                    text: false
                }).removeClass("ui-corner-all").addClass("custom-combobox-toggle ui-corner-right").mousedown(function () {
                    wasOpen = input.autocomplete("widget").is(":visible");
                }).click(function () {
                	if(input.is(':disabled')){
                        return;
                    }
                    input.focus();

                    // Close if already visible
                    if (wasOpen) {
                        return;
                    }
                    	
                    // Pass empty string as value to search for, displaying all results
                    input.autocomplete("search", "");
                });
            },
            showTitle: function () {
                var $sl = this.element;
                var $input = this.input;
                $sl.on('comboboxinquiryselect', function () {
                    //改变ui-title中存储的title值
                    $input.data('ui-tooltip-title', $sl.find('option:selected').text());
                    $input.tooltip('close');//关闭的时候,会将ui-tooltip-title 付给title
                });
                $input.attr('title', $sl.find('option:selected').text());
                $input.tooltip({
                    position: {my: "left center", at: "left top-20"}
                });
            },

            _source: function (request, response) {
                var matcher = new RegExp($.ui.autocomplete.escapeRegex(request.term), "i");
                response(this.element.children("option").map(function () {
                    var text = $(this).text();
                    if ((!request.term || matcher.test(text)))
                        return {
                            label: text,
                            value: text,
                            option: this
                        };
                }));
            },

            _removeIfInvalid: function (event, ui) {
                if (!this.options.removeIfInvalid) {
                    return;
                }
                // Selected an item, nothing to do
                if (ui.item) {//console.log(ui.item);
                    this._trigger("afterInvalid", null, ui.item.value);
                    return;
                }

                // Search for a match (case-insensitive)
                var value = this.input.val(),
					valueLowerCase = value.toLowerCase(),
					valid = false;
                this.element.children("option").each(function () {
                    if ($(this).text().toLowerCase() === valueLowerCase) {
                        this.selected = valid = true;
                        return false;
                    }
                });

                // Found a match, nothing to do
                if (valid) {
                    this._trigger("afterInvalid", null, value);
                    return;
                }


                this.element.val("");
                this.reset();
                this.input.data("ui-autocomplete").term = "";
            },

            _destroy: function () {
                this.wrapper.remove();
                this.element.show();
            },
            //下拉框的数据源发送变化的时候调用该方法
            reset:function(){
                this.input.val(this.element.children(':selected').text());
                this.input.attr('title',this.element.children(':selected').text());
            },
			resetEmpty:function(){
				this.input.val('请选择');
				this.input.attr('title','');
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

/**
 * 因为不能使用上面uploadFiles方法，这里再添加一个。（特殊需求里的附件上传）
 */

function downloadConfirm(orderId,downloadUrl){
	if(orderId != ""){
		var ctx = $("#ctx").val();
		$.jBox("iframe:"+ ctx +"/"+downloadUrl+"?orderId="+orderId, {
			title: "确认单",
			width: 340,
			height: 'auto',
			buttons: {'关闭':true},
			persistent:true,
			loaded: function (h) {},
			submit: function (v, h, f) {},
			closed: function () {
				location.reload(true);
			}
		});
		$(".jbox-close").hide();
	}}
//299-end


