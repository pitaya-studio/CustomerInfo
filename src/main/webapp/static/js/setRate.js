$(function(){
	$(".sub-store").mouseenter(function(event){
        $(this).parent().children(".tip-container").show();
    });

    $(".sub-store").mouseleave(function(event){
    	var $hide_div = $(this).parent().children(".tip-container");
        //当鼠标移到下面详细信息上时，不关闭信息窗口
        $(this).parent().children(".tip-container").hover(function(){
            $hide_div.show();
        },function(){
        	$hide_div.hide();
        });
        $hide_div.hide();
    });
	
	/*点击下拉款*/
	$(".dl-select input").click(function(event){
		var event = event || window.enent;
		event.stopPropagation();
		var flag = $(this).parent().children("ul").is(":hidden");
		$(this).parent().parent().parent().parent().children("tr").children("td").children(".dl-select").children("ul").hide();
		$(".top-distant").hide();
		if(flag){
			$(this).parent().children("ul").hide();
		}else{
            $(this).parent().children("ul").show();
		}
	});
	$(".rate").click(function(event){
		var event = event || window.enent;
		event.stopPropagation();
		var flag = $(this).parent().children("ul").is(":hidden");
		$(".table_four").find("tbody").children("tr").children("td").children(".dl-select").children("ul").hide();
		$(".top-distant").hide();
		if(flag){
			$(this).parent().children("ul").hide();
		}else{
            $(this).parent().children("ul").show();
		}
	});
	//关闭下拉选项
	$(document).click(function(){
		$(".dl-select ul").hide();
		$(".sub-store").parent().children("div").children("div").hide();
	});
	//切换已设置和未设置标签
	$(".tab-switch li").each(function(){
		$(this).click(function(){
			$(this).parent().children(".select-tab").removeClass('select-tab');
			$(this).addClass("select-tab");
		});
	});
	//清空所有条件
	$("#clearAll").click(function(){
		$("#searchName").val("");
		$("#wholeSearch").click();
	});
	//全选
	$("#checkAll").click(function(){
		var flag = $(this).is(':checked');
		if(flag){
			$("input[name='indexBox']").each(function(){
				$(this).prop('checked',flag);
			});
		}else{
			$("input[name='indexBox']").each(function(){
				$(this).removeAttr('checked');
			});
		}

	});
	//反选
	$("#reverseCheck").click(function(){
		if($("#checkAll").is(':checked')){
			$("#checkAll").removeAttr("checked")
		}
		if($("input[name='indexBox']:checked").length==0){
			$("#checkAll").prop("checked",true);
		}
		$("input[name='indexBox']").each(function(){
			if($(this).is(':checked')){
				$(this).removeAttr('checked');
			}else{
				$(this).prop('checked',true);
			}
		});
	});
	//批量设置
	$("#batchFill").click(function(){
		var checkedInput = $('input:checkbox[name=indexBox]:checked');
		var len = checkedInput.length;
		if(len<1){
			top.$.jBox.tip("请先选择批量设置的客户");
			return false;
		}
		//验证百分比情况下是在0~100之间
		var check=checkRate($("#rateValue"));
		if(check){
			return;
		}
		//费率类型
		var rateType = $("#rateType").val();
		if(rateType == "" ||rateType == null){
			top.$.jBox.tip("请选择费率类型");
			return false;
		}
		//费率值
		var rateValue = $("#rateValue").val();
		if(rateValue == "" ||rateValue == null){
			top.$.jBox.tip("请填写费率值");
			return false;
		}
		if(rateValue.indexOf('.') == rateValue.length-1 && rateValue.length == 1){
			rateValue ="0.00"
		}else if(rateValue.indexOf('.') == rateValue.length-1){
			rateValue =rateValue+"00";
		}else if(rateValue.indexOf('.') == 0 && rateValue.length > 1){
			top.$.jBox.tip("请填写正确的数据格式");
			return false;
//			rateValue ="0"+rateValue;
		}
		//费率单位
		var rateUnit = $("#rateUnit").val();
		if(rateUnit == "" ||rateUnit == null){
			top.$.jBox.tip("请选择费率单位");
			return false;
		}
		var  j=3;
		for(var i=0;i<checkedInput.length;i++){
			var This = checkedInput[i];
			if(rateType == "QUAUQ产品费率"){
                j=3;
			}else if(rateType == "QUAUQ其他费率"){
			    j=4;
			}
			else if(rateType == "渠道产品费率"){
				j=5;
			}else{
				j=6;
			}
			$(This).parent().parent().children("td").eq(j).children(".rate-input").val(rateValue);
            $(This).parent().parent().children("td").eq(j).children(".dl-select").children("input").val(rateUnit);
            if(rateUnit=="金额"){
            	$(This).parent().parent().children("td").eq(j).children(".rate-input").next().val(1);
            }else{
            	$(This).parent().parent().children("td").eq(j).children(".rate-input").next().val(0);
            }
		}
	});
});

	//判断输入的百分比 或 金额 格式是否正确
	function checkRateItm(obj){
    var money = obj.value;
    if(money && money != ""){
        if(money >= 0){
            var ms = obj.value.replace(/[^\d\.]/g,"").replace(/(\.\d{2}).+$/,"$1").replace(/^0+([1-9])/,"$1").replace(/^0+$/,"0");
            var txt = ms.split(".");
            if(!txt[0]){
           		txt[0] = 0;
           	}
           	if(txt[0] &&  txt[0] == 100 && txt.length>1){
           		txt[1] = 0;
           	}
            if($(obj).parent().prevAll().children("span.small-select").children("input").val() == "百分比" || $(obj).prevAll("div.small-select").children("input").val()=="百分比"){
            	if(txt[0] &&  txt[0] > 100){
            		txt[0] = txt[0].substring(0,2);
            	}
            }else if($(obj).parent().prevAll().children("span.small-select").children("input").val() == "金额" || $(obj).prevAll("div.small-select").children("input").val()=="金额"){
            	if(txt[0] &&  txt[0] > 100000){
            		txt[0] = txt[0].substring(0,5);
            	}
            }
            if(txt[1] && txt[1] >100){
            		txt[1] = txt[1].substring(0,2);
           	}
           	if(txt.length > 1 && !txt[1]){
           		txt[1] = 0;
           	}
            obj.value = txt[0]+(txt.length>1?"."+txt[1]:"");
        }else{
            obj.value = '';
        }
    }
}

//费率方式改变，费率输入框值清零
function cleanInputVal(obj){
	if($(obj).parent().parent().next("input.rate-input").length == 1){
		$(obj).parent().parent().next("input.rate-input").val("");
	}else if($(obj).parent().parent().parent().next().children("input.rate-input").length == 1){
		$(obj).parent().parent().parent().next().children("input.rate-input").val("");
	} 
}


