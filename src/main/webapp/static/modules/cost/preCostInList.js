var activityIds = "";
$(function() {
	$(".spinner").spinner({
				spin : function(event, ui) {
					if (ui.value > 365) {
						$(this).spinner("value", 1);
						return false;
					} else if (ui.value < 0) {
						$(this).spinner("value", 365);
						return false;
					}
				}
			});
	$(".qtip").tooltip({
				track : true
			});

	// 展开筛选按钮
	$('.zksx').click(function() {
				if ($('.ydxbd').is(":hidden") == true) {
					$('.ydxbd').show();
					$(this).text('收起筛选');
					$(this).addClass('zksx-on');
				} else {
					$('.ydxbd').hide();
					$(this).text('展开筛选');
					$(this).removeClass('zksx-on');
				}
			});
	// 如果展开部分有查询条件的话，默认展开，否则收起
	var searchFormInput = $("#searchForm")
			.find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='orderNum']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for (var i = 0; i < searchFormInput.length; i++) {
		if ($(searchFormInput[i]).val() != ""
				&& $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for (var i = 0; i < searchFormselect.length; i++) {
		if ($(searchFormselect[i]).children("option:selected").val() != ""
				&& $(searchFormselect[i]).children("option:selected").val() != "100"
				&& $(searchFormselect[i]).children("option:selected").val() != null) {
			selectRequest = true;
		}
	}
	if (inputRequest || selectRequest) {
		$('.zksx').click();
	}
	
	
	$("a[name=cost-del-name]").click(function(){
		var gid = $(this).attr("groupid");
		deleteCost(gid,this);
		
	});

});
$(document).ready(function() {
	activityIds = "${activityIds}";
	$("#groupform").validate({});
	jQuery.extend(jQuery.validator.messages, {
				required : "必填信息"
			});

	// 产品名称获得焦点显示隐藏
	$("#wholeSalerKey").focusin(function() {
				var obj_this = $(this);
				obj_this.next("span").hide();
			}).focusout(function() {
				var obj_this = $(this);
				if (obj_this.val() != "") {
					obj_this.next("span").hide();
				} else
					obj_this.next("span").show();
			});
	if ($("#wholeSalerKey").val() != "") {
		$("#wholeSalerKey").next("span").hide();
	}

	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");

	var _$orderBy = $("#orderBy").val();
	if (_$orderBy == "") {
		_$orderBy = "id DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function() {
		if ($(this).hasClass("li" + orderBy[0])) {
			orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC"
					? "up"
					: "down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html()
					+ "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
			$(this).attr("class", "activitylist_paixu_moren");
		}
	});
	$('.team_top').find('.table_activity_scroll').each(
			function(index, element) {
				var _gg = $(this).find('tr').length;
				if (_gg >= 20) {
					$(this).addClass("group_h_scroll");
				}
			});
});


		
	    function expand(child,obj,srcActivityId){
            if($(child).is(":hidden")){
                
                if("${userType}"=="1") {
                    $.ajax({
                        type:"POST",
                        url:"${ctx}/stock/manager/payReservePosition",
                        data:{
                            srcActivityId:srcActivityId
                        },
                        success:function(msg) {
                        	$(obj).html("关闭成本");
                        	$(child).show();
                            $(obj).parents("td").attr("class","td-extend");
                        	if(msg.length>0){
                                $(child+" [class^='soldPayPosition']").show();
                        	}
                        	$.each(msg,function(keyin,valuein){
                                $("td .soldPayPosition"+(valuein.activityGroupId)).find("span").text(valuein.leftpayReservePosition);
                        	});
                        }
                    });
                }else{
                	$(obj).html("关闭成本");
                	$(child).show();
                    $(obj).parents("td").attr("class","td-extend");
                }
            }else{
                if(!$(child).is(":hidden")){           
                	$(child).hide();
                    $(obj).parents("td").attr("class","");
                    $(obj).html("查看成本");
                }
            }
        }
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/cost/manager/list/${type}");
			$("#searchForm").submit();
	    }

	    $(function(){
			$('.team_a_click').toggle(function(){
				$(this).addClass('team_a_click2');
			},function(){
				$(this).removeClass('team_a_click2');
			});	
		 });
	    
	    function getCurDate(){
			var curDate = new Date();
			return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
		}
	  //控制截团时间	
		function takeOrderOpenDate(obj){
			var groupOD = $(obj).parent().prev().children("input[name='groupOpenDate']").val();
			return groupOD;
		}
		function takeModVisaDate(obj) {
			var groupOD = $(obj).parent().parent().children().first().children("input[name='groupOpenDate']").val();
			return groupOD;
		}
		function comparePositionMod(obj){
    		var plan = $(obj).val();
    		$(obj).parent().next().find("input").val(plan);
    		$(obj).parent().next().find("input").focus();
    		$(obj).parent().next().find("input").blur();
    	}
		
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

        function groupDetail(url) {
        	window.open(encodeURI(encodeURI(url)));
        }
        
        
        
      var deleteCost = function(id,$this){
        $.jBox.confirm("确定要删除所有的成本数据吗？", "提示", function(v, h, f) {
            if (v == 'ok') {
                $.ajax({
                    type: "POST",
                    url: contextPath+"/cost/manager/delByGroup",
                    cache:false,
                    async:false,
                    data:{gid : id},
                    success: function(e){
                        if(e == 'true'){
                           $($this).parent("td[name=cost-manager-name]").find("a[name=cost-add-name]").html("成本录入");
                           $($this).parent("td[name=cost-manager-name]").children(":not(a[name=cost-add-name])").remove();
                        }else{
                            top.$.jBox.tip('请求失败。','error');
                            return false;
                        }
                        
                    },
                    error : function(e){
                        top.$.jBox.tip('请求失败。','error');
                        return false;
                    }
                 });
            }
        });
        
      }