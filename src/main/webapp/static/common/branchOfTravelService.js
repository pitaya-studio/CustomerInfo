$(function () {
    $(document).on("click", function(){
        $("#msml_display").hide();
    });
	$("#msml_display").on("click", function(e){
		$("#msml_display").show();
	    e.stopPropagation();
	});

	if ($("div.wel_bg").length > 0 || $("div.denglu-hy").length > 0) {
		$("#msml_display").show();
	} else {
		$("#msml_display").hide();
	}
})

function openMsml() {
	var s = document.getElementById("msml_display").style.display;
	if (s == "none") {
		document.getElementById("msml_display").style.display = "block";
	} else {
		document.getElementById("msml_display").style.display = "none";
	}
	cancelBubble();
}
function getEvent() {
	if (window.event) {
		return window.event;
	}
	func = getEvent.caller;
	while (func != null) {
		var arg0 = func.arguments[0];
		if (arg0) {
			if ((arg0.constructor == Event || arg0.constructor == MouseEvent
					 || arg0.constructor == KeyboardEvent)
				 || (typeof(arg0) == "object" && arg0.preventDefault
					 && arg0.stopPropagation)) {
				return arg0;
			}
		}
		func = func.caller;
	}
	return null;
}
function cancelBubble() {
	var e = getEvent();
	if (window.event) {
		//e.returnValue=false;//阻止自身行为
		e.cancelBubble = true; //阻止冒泡
	} else if (e.preventDefault) {
		//e.preventDefault();//阻止自身行为
		e.stopPropagation(); //阻止冒泡
	}
}
