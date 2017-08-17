$(function(){
   $(".content-item").each(function(){
        var ThisEle = $(this);
        $(this).click(function(){
            var parentEle = ThisEle.parent();
            $(parentEle).children().each(function(){
                $(this).removeClass("selected");
            });
            ThisEle.addClass("selected");
            $("input[name='"+$(parentEle).attr('id')+"']").val(ThisEle.children("input").val());
            if($(parentEle).attr('id') == "departureDate"){
            	$("#startDate").val("");
            	$("#endDate").val("");
            	$("input[name='startDate']").val("");
            	$("input[name='endDate']").val("");
            }
        });
   });
   setTimeout(function(){
       $(".first-remind").hide();
   },5000);
   
   $('#nameCode').keypress(function(event){  
	    var keycode = (event.keyCode ? event.keyCode : event.which);  
	    if(keycode == '13'){  
	    	searchSqActivityStrategy();
	    }  
	});  

   
   var groupIds = '';
   $("#wantSet tr td.th-first input").each(function(){
	   var tempVal = $(this).val();
	   groupIds = groupIds + tempVal.split("_")[0]+",";
   });
   /*if(groupIds != ''){
	   var ctx = $('#ctx').val();
	   $.ajax({
			type:"POST",
			url:ctx+"/pricingStrategy/manager/getEyelessAgentCount",
			data:{groupIds:groupIds,activityKind:'2'},
			success:function(data){
				if(data.hasResult){
					var counts = data.counts;
					var countFlag = 0;
					$("#wantSet tr.warniniglx td:eq(0)").each(function(){
						$(this).text("温馨提示：目前"+counts[countFlag]+"家门市不能查看该产品");
						countFlag++;
					});
				}
			}
		})
   }*/
   
   //522需求
   $("#ProTab tbody tr").hover(function(){
       if($(this).next().hasClass("tr_child")){
           $(this).next().addClass("tr_hover");
       }
   },function(){
       $(this).next().removeClass("tr_hover");
   });
   $("#ProTab tbody tr.tr_child").hover(function(){
           $(this).prev().addClass("tr_hover");
   },function(){
       $(this).prev().removeClass("tr_hover");
   });
   $("#ProTab tbody tr.tr_child").prev().css("border-bottom","none")

   
 /*//选择上架状态
   $(".shelf-status").click(function(e){
       $(this).children("ul").toggle();
       e = getEvent();
       if(window.event){
           e.cancelBubble = true;
       }else{
           e.stopPropagation();
       }
   });
   //切换选中的上架状态
   $(".shelf-status ul li").click(function(e){
       $(this).parent().parent().children("span").text($(this).text());
       $("input[name='onlineState']").val($(this).val());
       $(this).parent().hide();
       e = getEvent();
       if(window.event){
           e.cancelBubble = true;
       }else{
           e.stopPropagation();
       }
   });
   //点击空白处关闭上架状态下拉框
   $(document).click(function(){
       $(".shelf-status ul").hide();
   });*/

	//ymx 2017/3/10 改版样式调整 进入页面选中排序标签 Start
	$(".table-sort").find(".not_choose").siblings().removeClass("choose_sort");
	$(".sort-icon-up").parent().addClass("choose_sort");
	$(".sort-icon-down").parent().addClass("choose_sort");
	//ymx 2017/3/10 改版样式调整 End

});

String.prototype.trim = function () {
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
}

window.onload = function () {
	$("#queryList div").children().each(function(){
		var idFlag = $(this).attr('name');
		if(idFlag == "contentWrap1" ||idFlag == "contentWrap2" ||idFlag == "travelType" ||idFlag == "activityType" ||idFlag == "activityLevel" || idFlag == "departureDate" || idFlag == "onlineState"){
			if($(this).val() != null && $(this).val().trim() != ""){
				var tempVal = $(this).val();
				$("#"+idFlag).children(".content-item").each(function(){
					$(this).removeClass("selected");
					if($(this).children('input').val() == tempVal){
						$(this).addClass('selected');
					}
				});
			}
		}
		/*if(idFlag == "onlineState"){
			var tempVal = $(this).val();
			 $(".shelf-status ul li").each(function(e){
			      if(tempVal == $(this).val()){
			    	  $(this).parent().parent().children("span").text($(this).text());
			      }
			   });
		}*/
    });
	
//	
};

function pushParm(parmFlag,val){
	
}
function showQueryMore(obj){
    $(".criteria-hide").toggle();
    var span = $(obj).children().first();
    var em = $(obj).children().last()
    if(span.html() == "更多条件"){
        $("#departureDate").removeClass('border-none');
        span.html("收起条件");
        em.addClass("icon-up");
    }else{
        $("#departureDate").addClass('border-none');
        span.html("更多条件");
        em.removeClass("icon-up");
    }
}
function clearAllCon(){
     $(".content-item").each(function(){
        if($(this).html() == "全部"){
            $(this).addClass("selected");
        }else{
            $(this).removeClass("selected");
        }
     });
     $(".search-text").val("");
     $(".start-date").each(function(){
        $(this).val("");
     });
     
     $("#queryList div").children().each(function(){
    	 $(this).val("");
     });
}
function checkAll(obj){
    $("#ProTab").find("input[type='checkbox']").each(function(){
        this.checked = obj.checked;
    });
}
function checkReverse(){
	$("#checkAll").each(function(){
		this.checked = false;
	});
    $("#ProTab").find("input[type='checkbox']").each(function(){
       this.checked = !(this.checked);
    }); 
}
function sortTable(obj,parmFlag){
	var cls = $(obj).find(".sort-icon");
	var upFlag =  cls.attr('class').indexOf('sort-icon-up') != -1;
	var downFlag = cls.attr('class').indexOf('sort-icon-down') != -1;

	//ymx 2017/3/10 改版样式调整 切换排序标签位置 Start
	$(obj).siblings().removeClass("choose_sort");
	$(obj).addClass("choose_sort");
	//ymx 2017/3/10 改版样式调整 End

	$("span.sort-items").children(".sort-icon").each(function(){
		$(this).removeClass('sort-icon-up');
		$(this).removeClass('sort-icon-down');
	});
	$("input[name='orderByQuauqPrice']").val("");
	$("input[name='orderByIndustryPrice']").val("");
	$("input[name='orderByDate']").val("");
	if(upFlag){
		 cls.addClass('sort-icon-down');
		 $("input[name='"+parmFlag+"']").val("desc");
	}else if(downFlag){
		 cls.addClass('sort-icon-up');
		 $("input[name='"+parmFlag+"']").val("asc");
	}else{
		 cls.addClass('sort-icon-up');
		 $("input[name='"+parmFlag+"']").val("asc");
	}
    $("#queryList").submit();
}
function showMoreCities(obj,i){
    var className = $("#contentWrap"+i).attr('class');
    if($(obj).children("span").text()=='更多'){
    	$(obj).children("span").text('收起');
    	$(obj).children("em").addClass("icon-up");
    }else{
    	$(obj).children("span").text('更多');
    	$(obj).children("em").removeClass("icon-up");
    }
    //没展开
    if(className.indexOf("expand-content") == -1){
        $("#contentWrap"+i).addClass("expand-content");
        $("obj").children().first().html("收起");
        $("obj").children().last().addClass("icon-up");
    }else{
        $("#contentWrap"+i).removeClass("expand-content");
        $("obj").children().first().html("更多");
        $("obj").children().last().removeClass("icon-up");
    }
}

function  changeColor(obj){
    if(obj.checked || obj.checked == true){
        $(obj).parent().parent().addClass("selectedColor");
    }else{
        $(obj).parent().parent().removeClass("selectedColor");
    }
}
<!-- $.jBox(html, { title: "批量还收据",buttons:{'保存': 1,'提交': 2,'取消':0}, submit:function(v, h, f){}}) -->


function setPricingStrategy(obj,groupid,activityid,activityName,isCheck){
	activityName = activityName.substring(1,activityName.length-1);
	openPricingStrategy(isCheck,obj,groupid,activityid,activityName,'/pricingStrategy/manager/editPricingStrategy/'+groupid+"/"+activityid,"/pricingStrategy/manager/addActivityStrategy");
}

function setBatchPricingStrategy(){
	var isCheck = false;
	var setIds = "";
	var count = 0;
	//获取所有选中的团期
	$("#wantSet tr").children(".th-first").each(function(){
		if($(this).children("input:checked").val()){
			count++;
			var temp = $(this).children("input:checked").val();
			var temps = temp.split("_");
			if(!isCheck)isCheck = temps[2];
			setIds = setIds+temp+",";
		}
	});
	if(count <=0){
		$.jBox.confirm("请选择要操作的数据","提示",function(v,h,f){
			if(v=='ok'){
    			return;
    		}
    		if(v=='cancel'){
    			return;
    		}
		});
	}else{
		openBatchPricingStrategy(isCheck,setIds,'/pricingStrategy/manager/editBatchPricingStrategy',"/pricingStrategy/manager/addBatchActivityStrategy");
	}
}

function openBatchPricingStrategy(isCheck,ids,url,addUrl){
	if($("#hiddenParm")){
		$("#hiddenParm").remove();
		$(".clickBtn",window.parent.document).removeClass("clickBtn");
	}
	var ctx = $('#ctx').val();
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($("#wantSet").parent().find("#hiddenParm").length == 0)
		$("#wantSet").parent().append('<div  style="display: none" id="hiddenParm"><input type="hidden" id="isBatch" value="true"></div>');
	$("#wantSet").addClass("clickBtn");
	$.jBox("iframe:"+ ctx + url, {
		title: "批量设置价格策略",
		width: 773,
		height: 400,
		buttons: {'提交':true,'取消':false},
		persistent:true,
		loaded: function (h) {},
		submit: function (v, h, f) {
			//获取价格策略值
			var adultPri = $("#adultPri").val();
			var childrenPri = $("#childrenPri").val();
			var specialPri = $("#specialPri").val();
			$("#hiddenParm").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			if(!v){
				return true;
			}else if(isCheck == "true"){
				$.jBox.confirm("被选中的记录中存在已设置QUAUQ价的记录，是否替换","提示",function(v,h,f){
					if(v=='ok'){
						loading('正在提交，请稍等...');
						if(v){
							$.ajax({
								type:"POST",
								url:ctx+addUrl,
								data:{ids:ids,adultPri:adultPri,childrenPri:childrenPri,specialPri:specialPri},
								success:function(data){
									if('ok' == data.flag){
										$("#queryList").submit();
									}else{
										$.jBox.confirm("设置失败","提示",function(v,h,f){
											if(v=='ok'){
												return;
											}
											if(v=='cancel'){
												return;
											}
										});
									}
								}
							})
						}
		    			return;
		    		}
		    		if(v=='cancel'){
		    			return;
		    		}
				});
				return false;
			}else{
				if(v){
					loading('正在提交，请稍等...');
					$.ajax({
						type:"POST",
						url:ctx+addUrl,
						data:{ids:ids,adultPri:adultPri,childrenPri:childrenPri,specialPri:specialPri},
						success:function(data){
							if('ok' == data.flag){
								$("#queryList").submit();
							}else{
								$.jBox.confirm("设置失败","提示",function(v,h,f){
									if(v=='ok'){
										return;
									}
									if(v=='cancel'){
										return;
									}
								});
							}
						}
					})
				}
    			return;
			}
		}
	});
	$("#jbox-content").css("overflow","hidden");
	$("#jbox-content").css("overflow-y","hidden");
}

function openPricingStrategy(isCheck,obj,groupid,activityid,activityName,url,addUrl){
	if($("#hiddenParm")){
		$("#hiddenParm").remove();
		$(".clickBtn",window.parent.document).removeClass("clickBtn");
	}
	var ctx = $('#ctx').val();
	//新建一个隐藏的div，用来保存文件上传后返回的数据
	if($(obj).parent().find("#hiddenParm").length == 0)
		$(obj).parent().append('<div  style="display: none" id="hiddenParm"></div>');
	$(obj).addClass("clickBtn");
	$.jBox("iframe:"+ ctx + url, {
		title: activityName,
		width: 773,
		height: 400,
		buttons: {'提交':true,'取消':false},
		persistent:true,
		loaded: function (h) {},
		submit: function (v, h, f) {
			//获取价格策略值
			var adultPri = $("#adultPri").val();
			var childrenPri = $("#childrenPri").val();
			var specialPri = $("#specialPri").val();
			$("#hiddenParm").remove();
			$(".clickBtn",window.parent.document).removeClass("clickBtn");
			if(!v){
				return true;
			}else if(isCheck){
				 $.jBox.confirm("已存在QUAUQ价，是否替换","提示",function(v,h,f){
					if(v=='ok'){
						if(v){
							loading('正在提交，请稍等...');
							$.ajax({
								type:"POST",
								url:ctx+addUrl,
								data:{groupid:groupid,activityid:activityid,adultPri:adultPri,childrenPri:childrenPri,specialPri:specialPri},
								success:function(data){
									if('ok' == data.flag){
										$("#queryList").submit();
									}else{
										$.jBox.confirm("设置失败","提示",function(v,h,f){
											if(v=='ok'){
												return;
											}
											if(v=='cancel'){
												return;
											}
										});
									}
								}
							});
						}
		    			return;
		    		}
		    		if(v=='cancel'){
		    			flag = true;
		    			return true;
		    		}
				});
				return false;
			}else{
				if(v){
					loading('正在提交，请稍等...');
					$.ajax({
						type:"POST",
						url:ctx+addUrl,
						data:{groupid:groupid,activityid:activityid,adultPri:adultPri,childrenPri:childrenPri,specialPri:specialPri},
						success:function(data){
							if('ok' == data.flag){
								$("#queryList").submit();
							}else{
								$.jBox.confirm("设置失败","提示",function(v,h,f){
									if(v=='ok'){
										return;
									}
									if(v=='cancel'){
										return;
									}
								});
							}
						}
					});
				}
			}
		}
	});
	$("#jbox-content").css("overflow-y","hidden");
}


function searchSqActivityStrategy(){
	$("input[name='nameCode']").val($("#nameCode").val());
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
	
	$("#allDate").addClass("selected");
	$($("#allDate").siblings()).removeClass("selected");
	$("input[name='departureDate']").val("");
}

function initNameCode(obj){
	$("input[name='nameCode']").val($(obj).val());
}

function threeLevelSearch(obj,addFlag){
	$('#queryList input[name="allActivity"]').remove();
	if(addFlag && !$("#allActivity").val()){
		$('#queryList').append('<input id="allActivity" name="allActivity" type="hidden" value="allActivity">');
	}
	$("#threeLevelMenu").children().each(function(){
		$(this).removeClass("current");
	});
	$(obj).parent().addClass("current");
	$("#queryList").submit();
}

/**
 * 翻页
 * n 为页数
 * s 为条数
 */
function page(n, s) {
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    $("#queryList").submit();
    return false;
}

function showRemind(obj){
    $(obj).parent().children().last().show();
}

function hideRemind(obj){
    $(obj).parent().children().last().hide();
}

/**
 * 修改记录
 * @param groupid
 * @param srcid
 */
function changeList(groupid,srcid){
	var ctx = $('#ctx').val();
	$.jBox("iframe:"+ ctx + "/pricingStrategy/manager/changeList/"+groupid+"/"+srcid, {
		title: "变更记录",
		width: 773,
		height: 400,
		buttons: {},
		persistent:true,
		loaded: function (h) {
			$(".jbox-content").css({'overflow':'hidden','overflow-y':'hidden'});
		}
	});
}


String.prototype.trim = function () {
	return this .replace(/^\s\s*/, '' ).replace(/\s\s*$/, '' );
}
