/**
 * 修改设置时限
 * @param settingType 设置类型 销售处理中 1；计调处理中 2；财务收款 3
 * @param greenLightTimeType 绿灯时间单位：天 1；小时 2；分 3
 * @param greenLightTimeStart 绿灯时间：多少时间内显示
 * @param greenLightTimeEnd 绿灯时间：多少时间内显示
 * @param yellowLightTimeType 黄灯时间单位：天 1；小时 2；分 3
 * @param yellowLightTimeStart 黄灯时间：多少时间内显示
 * @param yellowLightTimeEnd 黄灯时间：多少时间内显示
 * @param redLightTimeType 黄灯时间单位：天 1；小时 2；分 3
 * @param redLightTimeStart 黄灯时间：多少时间内显示
 * @param redLightTimeEnd 黄灯时间：多少时间内显示
 */
function jbox_order_tracking_setting(id, settingType, greenLightTimeType, greenLightTimeStart, greenLightTimeEnd, yellowLightTimeType, 
		yellowLightTimeStart, yellowLightTimeEnd, redLightTimeType, redLightTimeStart) {
	
	// 如果有其他费用，则展示在弹出框中
	settingInit(id, settingType, greenLightTimeType, greenLightTimeStart, greenLightTimeEnd, yellowLightTimeType, 
			yellowLightTimeStart, yellowLightTimeEnd, redLightTimeType, redLightTimeStart);
	
}

// 初始化弹出框
function settingInit(id, settingType, greenLightTimeType, greenLightTimeStart, greenLightTimeEnd, yellowLightTimeType, 
		yellowLightTimeStart, yellowLightTimeEnd, redLightTimeType, redLightTimeStart) {
	// 获取到内部的第一个步骤名称
	var pop_title = "销售处理中";
    if (settingType == 1) {
    	pop_title = "销售处理中";
    } else if (settingType == 2) {
    	pop_title = "计调处理中";
    } else if (settingType == 3) {
    	pop_title = "财务收款";
    }
    // 时限单位
    var lightTimeType = "分钟";
    if (greenLightTimeType == 1) {
    	lightTimeType = "天";
    } else if (greenLightTimeType == 2) {
    	lightTimeType = "小时";
    } else if (greenLightTimeType == 3) {
    	lightTimeType = "分钟";
    }
    $("em[name=sameTimeType]").text(lightTimeType);
    $("input.light_other").attr("value", lightTimeType);
    
    $pop = $.jBox($("#pop_order_track").html(), {
        	title : "信号灯时限设置",
        	buttons: {'取消': 1,'确定':0},
        	submit: function (v, h, f) {
	            if (v == "0") {
	            	var flag = saveOrderSetting(id, h, settingType);
	            	if (!flag) {
	            		return false;
	            	}
	            }
	        },
	        width: 700,
	        height:295
    });
    $pop.find(".dl-select input").click(function(event){
        var event = event || window.enent;
        event.stopPropagation();
        $(this).parent().children("ul").toggle();
    });
    $pop.find(".dl-select ul li").each(function(){
        $(this).click(function(){
            var value = $(this).text();
            $(this).parent().hide();
            $(this).parents(".pop_order_content").find("input.light_other").val(value);
            $("em[name=sameTimeType]").text(value);
        });
    });
    $pop.find(".pop_title").html(pop_title);
    $pop.find("#greenEndTime").val(greenLightTimeEnd);
    $pop.find("#yellowStartTime").text(greenLightTimeEnd);
    $pop.find("#yellowEndTime").val(yellowLightTimeEnd);
    $pop.find("#redStartTime").text(yellowLightTimeEnd);
}

function saveOrderSetting(id, $pop, settingType) {
	var greenEndTime = $pop.find("input[id=greenEndTime]").val();
	var yellowStartTime = greenEndTime;
	var yellowEndTime = $pop.find("#yellowEndTime").val();
	var redStartTime = yellowEndTime;
	var lightTimeType = $pop.find("em[name=sameTimeType]:eq(0)").text();
	if (lightTimeType == "天") {
		lightTimeType = 1;
	} else if (lightTimeType == "小时") {
		lightTimeType = 2;
	} else if (lightTimeType == "分钟") {
		lightTimeType = 3;
	}
	
	var flag = validatePara(greenEndTime, yellowEndTime);
	if (flag) {
		$.ajax({
			type : "POST",
			url : $("#sysCtx").val() + "/orderTrackingSetting/manage/saveSetting?dom=" + Math.random(),
			data : {
				settingId : id,
				settingType : settingType,
				lightTimeType : lightTimeType,
				greenEndTime : greenEndTime,
				yellowStartTime : yellowStartTime,
				yellowEndTime : yellowEndTime,
				redStartTime : redStartTime
			},
			success : function (msg) {
				if (msg == "success") {
					window.location.reload();
				} else {
					top.$.jBox.tip("提交参数有误", 'warning');
				}
			}
		});
	}
	return flag;
}

/**
 * 时间参数校验
 * @param greenEndTime
 * @param yellowEndTime
 * @returns
 */
function validatePara(greenEndTime, yellowEndTime) {
	var flag = true;
	var re = /^[0-9]+.?[0-9]*$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/ 
	if (!re.test(greenEndTime) || !re.test(yellowEndTime)) {
		top.$.jBox.tip("时间参数只能为数字", 'warning');
		flag = false;	
	}
	if (Number(greenEndTime) <= 0 || Number(yellowEndTime) <= 0) {
		top.$.jBox.tip("时间参数不能小于0", 'warning');
		flag = false;
	}
	if (Number(greenEndTime) >= Number(yellowEndTime)) {
		top.$.jBox.tip("黄灯开始时间不能大于或等于结束时间", 'warning');
		flag = false;
	}
	return flag;
}

function greenEndTimeChange(obj) {
	$("span[id=yellowStartTime]").text($(obj).val());
}

function yellowEndTimeChange(obj) {
	$("span[id=redStartTime]").text($(obj).val());
}