$(window).load(function () {
	//判断是否显示更多
	$("em.more-item").each(function(){
		var heightItem = $(this).parent().height();
		if( heightItem <= 37){
			$(this).remove();
		}
	});
	
	$('#nameCode').keypress(function(event){  
	    var keycode = (event.keyCode ? event.keyCode : event.which);  
	    if(keycode == '13'){  
	       queryList(); 
	    }  
	});  
})


function queryList(){
	//设置查询参数
	$("#queryList div input[name='activityKind']").val($("#activityKind li.select-tab input").val());
	if($("#activityKind li.select-tab input").val() != 2){
		top.$.jBox.info("暂不支持散拼之外的类型", "警告");
		return;
	}
	$("#queryList div input[name='nameCode']").val($("#nameCode").val());
	$("#queryList div input[name='contentWrap1']").val($("#contentWrap1 span.selected").children('input').val());
	$("#queryList div input[name='contentWrap2']").val($("#contentWrap2 span.selected").children('input').val());
	$("#queryList div input[name='contentWrap3']").val($("#contentWrap3 span.selected").children('input').val());
	var orderByQuauqPrice;
	$("#orderByQuauqPrice span").children('i.order_selected').each(function(){
		if($(this).hasClass('fa-sort-asc')){
			orderByQuauqPrice='asc';
		}else if($(this).hasClass('fa-sort-desc')){
			orderByQuauqPrice='desc';
		}
	});
	$("#queryList div input[name='orderByQuauqPrice']").val(orderByQuauqPrice);
	var orderByIndustryPrice;
	$("#orderByIndustryPrice span").children('i.order_selected').each(function(){
		if($(this).hasClass('fa-sort-asc')){
			orderByIndustryPrice='asc';
		}else if($(this).hasClass('fa-sort-desc')){
			orderByIndustryPrice='desc';
		}
	});
	$("#queryList div input[name='orderByIndustryPrice']").val(orderByIndustryPrice);
	var orderByDate;
	$("#orderByDate span").children('i.order_selected').each(function(){
		if($(this).hasClass('fa-sort-asc')){
			orderByDate='asc';
		}else if($(this).hasClass('fa-sort-desc')){
			orderByDate='desc';
		}
	});
	$("#queryList div input[name='orderByDate']").val(orderByDate);
	var departureDate;
	if($('#departureDate span.selected')){
		departureDate = $('#departureDate span.selected').children('input').val();
	}
	$("#queryList div input[name='departureDate']").val(departureDate);
	$("#queryList div input[name='startDate']").val($("#startDate").val());
	$("#queryList div input[name='endDate']").val($("#endDate").val());
	$("#queryList").submit();
}

function changeDate(obj,val){
	var startdate = $("#startDate").val();
	var starttimes = 0;
	if(startdate != null && startdate.trim() != ""){
		var arr=startdate.split("-");    
		var starttime=new Date(arr[0],arr[1],arr[2]);    
		starttimes=starttime.getTime();   
	}
	  
	var endDate =  $("#endDate").val();
	var lktimes = 0;
	if(endDate != null && endDate.trim() != ""){
		var arrs=endDate.split("-");    
		var lktime=new Date(arrs[0],arrs[1],arrs[2]);    
		lktimes=lktime.getTime();   
	}
	if(starttimes > lktimes  && lktimes >0){
		$(obj).val("");
	}
	
	$("input[name='"+val+"']").val($(obj).val());
	
	$("#departureDate span:first").addClass("selected");
	$("#departureDate span:gt(0)").removeClass("selected");
	$("#queryList div input[name='departureDate']").val("");
}


/**
 *   批量设置团期费率
 */
function batchSetGroupRate(url,companyuuid,activitykind){
	var groupIds = "";
	var activityKind = $("#activityKind li.select-tab input").val();
	if( activityKind != 2){
		top.$.jBox.info("暂不支持散拼之外的类型", "警告");
		return;
	}
	var inputs=$("input[name='indexBox']:checked");
	if(inputs.length <= 0){
		top.$.jBox.info("请选择要设置费率的团期", "警告");
		return;
	}
	for(var i = 0; i < inputs.length; ++i){
		groupIds = groupIds+","+$(inputs[i]).val();
	}
	window.location.href = url+"?companyUuid="+companyuuid+"&productType="+activitykind+"&groupIds="+groupIds;
}


/**
 * 翻页
 * n 为页数
 * s 为条数
 */
function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    queryList();
    return false;
}