var rebatesReview = {
	showReviewDetail: function(reviewId){
		window.open(contextPath + "/airticketRebates/showAirticketRebatesDetail/" + reviewId);
	},
	showRebatesReview: function(reviewId,userLevel){
		$.ajax({
			type : "POST",
			async: false, 
			url : contextPath + "/order/rebates/review/validReviewRebates",
			data : {
				rid: reviewId,
				userLevel : userLevel
			},
			success : function(msg) {
				//by sy 2015.8.5
				if( msg == "error"){
					jBox.tip("审核验证失败！");
					
				}
//				else if(msg == "false"){
//					jBox.tip("此记录已审核！");
//					location.reload();
//				}
				else{
					window.open(contextPath + "/airticketRebates/showAirticketRebatesReview/"+reviewId+"/"+userLevel);
				}
			}
		});
	}
};
$(function(){
	//搜索条件筛选
	launch();
	//操作浮框
	operateHandler();
	//团号和产品切换
	switchNumAndPro();	
	$(".searchForm").delegate("select[class='selectType']","change",function(){
		 $("#searchForm").submit();
	});
	var _$orderBy = $("#orderBy").val();
	if(_$orderBy==""){
	    _$orderBy="id DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
	    if ($(this).hasClass("li"+orderBy[0])){
	        orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
	        $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
	        $(this).attr("class","activitylist_paixu_moren");
	    }
	});
	$.fn.datepicker=function(option){
        var opt = {}||option;
        this.click(function(){
           WdatePicker(option);            
        });
    }        
   	$("#orderTimeBegin").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
    });
	$("#orderTimeEnd").datepicker({
        dateFormat:"yy-mm-dd",
           dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
           closeText:"关闭", 
           prevText:"前一月", 
           nextText:"后一月",
           monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
    });
	//如果展开部分有查询条件的话，默认展开，否则收起	
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" && 
				$(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
});
function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
};
var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
    $('#channel').val('');
    $('#saler').val('');
    $('#meter').val('');
    $('#picker').val('');
}
function orderBySub(orderNum) {
	$("#orderBy").val(orderNum);
	$("#searchForm").submit();
};
function statusChoose(statusNum) {
	$("#reviewStatus").val(statusNum);
	$("#searchForm").submit();
};
function sortby(sortBy,obj) {
    var temporderBy = $("#orderBy").val();
    if(temporderBy.match(sortBy)) {
        sortBy = temporderBy;
        if(sortBy.match(/ASC/g)) {
            sortBy = $.trim(sortBy.replace(/ASC/gi,"")) + " DESC";
        } else {
            sortBy = $.trim(sortBy.replace(/DESC/gi,"")) + " ASC";
        }
    } else {
        sortBy = sortBy+" DESC";
    }
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
//同一审核流程切换审核角色查询
function chooseRole(rid){
	$("#rid").val(rid);
	$("#searchForm").submit();
}
