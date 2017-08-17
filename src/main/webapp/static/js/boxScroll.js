function BoxScroll($obt, options) {
	if (this instanceof BoxScroll) {
		this.$obt = $obt;
	} else {
		return new BoxScroll($obt);
	}
	var config = $.extend({}, {
		cursorcolor: "#d1d1d1",
		cursoropacitymax: 0.5,
		autohidemode: "false",
		cursorwidth: "7px",
        preservenativescrolling:false,
		cursorborder: "0",
		cursorborderradius: "3px",
		boxzoom: false
	}, options);
	var scroll = $obt.niceScroll(config);
	var scrollId = scroll.id;
    //默认隐藏
    $("#" + scrollId + " div, #" + scrollId + "-hr div").hide();
	$("#" + scrollId + ", #" + scrollId + "-hr").on("mouseover mouseout", function(event) {
		if (event.type == 'mouseover') {
			event.preventDefault();
			event.stopPropagation();
			$(this).children("div").css("background-color", "#828282");
			$(this).children("div").show();
		} else {
			event.preventDefault();
			event.stopPropagation();
			$(this).children("div").css("background-color", "#d1d1d1");
			$(this).children("div").hide();
		}
	});

	$("#" + scrollId + " div, #" + scrollId + "-hr div").on("mouseover mouseout", function(event) {
		if (event.type == 'mouseover') {
			event.preventDefault();
			event.stopPropagation();
			$(this).css("background-color", "#828282");
			$(this).show();
		} else {
			event.preventDefault();
			event.stopPropagation();
			$(this).css("background-color", "#d1d1d1");
			$(this).hide();
		}
	});

	$obt.on("mouseover mouseout", function(event) {
		if (event.type == 'mouseover') {
			//event.preventDefault();
			//event.stopPropagation();

			$("#" + scrollId + " div, #" + scrollId + "-hr div").css("background-color", "#d1d1d1");
			$("#" + scrollId + " div, #" + scrollId + "-hr div").show();
		} else {
			//event.preventDefault();
			//event.stopPropagation();
			$("#" + scrollId + " div, #" + scrollId + "-hr div").hide();
		}
	});

	/*$.each($obt.children(), function() {
		if (this.addEventListener) {
			this.addEventListener("resize", scroll.resize, false);
		} else {
			this.attachEvent("onresize", scroll.resize);
		}
	});
	$obt[0].onresize = scroll.resize;*/
	
	$(window).resize(scroll.resize);


	this.resize = scroll.resize;
	this.show = scroll.show;
	this.hide = scroll.hide;
};