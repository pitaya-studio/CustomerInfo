
//展示关闭公告信息
function showANDetail(obj){
	if($(obj).html() == "[查看]"){
	$(obj).parent().siblings('.note-list-content').show();
	var str ="[关闭]";
	$(obj).html(str);
	}else{
	$(obj).parent().siblings('.note-list-content').hide();
	var str2 = "[查看]";
	$(obj).html(str2);
	}
	}
function closeTheDetail(obj){
	$(obj).parent("div").hide();
	$(obj).parent("div").siblings("p").find("span").html("[查看]");
	
	}





