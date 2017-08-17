function sortby(sortBy,obj){
   var temporderBy = $("#orderBy").val();
   if(temporderBy.match(sortBy)){
	   sortBy = temporderBy;
	   if(sortBy.match(/ASC/g)){
		   sortBy = sortBy.replace(/ASC/g,"");
	   }else{
		   sortBy = $.trim(sortBy)+" ASC";
	   }
   }
   $("#orderBy").val(sortBy);
   $("#searchForm").submit();
}

function page(pn, ps)
{
   $("#pageNo").val(pn);
   $("#pageSize").val(ps);
   $("#searchForm").submit();
}

//展开、关闭航程
function expand(child,obj){
	if($(child).css("display")=="none"){
			$(obj).html("关闭航程");
			$(child).show();
			$(obj).parents("td").addClass("td-extend").parent("tr").addClass("tr-hover");
		//}
	}else{
		$(child).hide();
		$(obj).parents("td").removeClass("td-extend").parent("tr").removeClass("tr-hover");
		$(obj).html("展开航程");
	}
}

