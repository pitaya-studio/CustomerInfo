$(document).ready(function () {

	// 标题上方显示时间
	showNowTime();

	//点击页面上的按钮弹出窗体
    $("#orderTracking_btn").click(function(){
    	
    	$(".orderT_cont_table tbody").empty();
        //淡入
        $(".orderTracking_back").fadeIn(350);
        $(".orderT_close,.orderT_top").show();
		$("#sea,.sea,.header").addClass("blur_background");
        $("body").css("overflow","hidden");
        
        // 动态加载数据
        findOrderProgressTracking('');
    });

    //点击窗体上的close按钮关闭页面
    $(".orderT_close").click(function(){
        //淡出
        $(".orderTracking_back").fadeOut(350);
        $(".orderT_close,.orderT_top").hide();
        $("#sea,.sea,.header").removeClass("blur_background");
        $("body").css("overflow","auto");
    })

	// 点击窗体上的top按钮，回到页面顶部
	var $ot_all = $('.orderTracking_all'); // 获取整体
	$('.orderT_top').click(function () { // 向上按钮绑定单击事件
		if (!$ot_all.is(":animated")) { // 判断是否处于动画
			$ot_all.animate({
				scrollTop : "0"
			}, 500);
			$('#mainId').animate({
				top : "0"
			}, 500);
		}
	})

	// 加载更多数据的按钮
	$(".ot_table_bottom").click(function () {

		// 解除绑定单击事件
		$(".orderT_cont_table tbody").unbind();

		var nowPageNo = $("#pageNoForProgress").val();
		$("#pageNoForProgress").val(Number(nowPageNo) + 1);
		loadProgressDate();
	});
	
	//表头固定
    $(".orderTracking_all").scroll(function(){
        var $top_dis = $(".orderT_cont_table thead");
        var $body_dis = $(".orderT_cont_table");
        if($body_dis.offset().top<-40){
            $top_dis.addClass("title_not_move");
        }else{
            $top_dis.removeClass("title_not_move");
        }
    });
});

function set() {
	var w = $(".orderT_close").width();
	var h = $(".orderT_close").height();
	var x = w < h ? w : h; //宽高中取最小值

	var minusW = ($(".orderTracking_content").width() - $(".orderT_content").width()) / 3;
	var ww = $(".orderTracking_content").width() - minusW;
	$(".orderT_close").css("marginLeft", ww + "px");
	$(".orderT_top").css("marginLeft", ww + "px");
	$(".orderT_close,.orderT_top").css("right", "auto");
	//具体高度需要与UI沟通
	// $(".orderT_close").css("marginTop",(-1*x/2)+"px");
}
$(window).resize(function () {
	set();
});

$(window).scroll(function () {
	set();
});

/**
 * ajax加载数据订单跟踪数据
 * @returns
 */
function loadProgressDate() {
	
	ajaxLoadStart();
	
	var selectType = $("#selectType").val();
	var pageNo = $("#pageNoForProgress").val();
	$.ajax({
		type : "POST",
//		async : false,
		url : $("#progressCtx").val() + "/orderProgressTracking/manage/ajaxList?dom=" + Math.random(),
		data : {
			pageNo : pageNo,
			selectType : selectType
		},
		success : function (msg) {
			if (msg && msg != null) {
				var progressArr = eval(msg);
				if (progressArr.length > 0) {
					addOrderProgressTrackingData(progressArr);
					initLoadData();
					var isLastPage = progressArr[0].isLastPage;
					if (isLastPage == "1") {
						$(".ot_table_bottom").hide();
					} else {
						$(".ot_table_bottom").show();
					}
				} else {
					$(".ot_table_bottom").hide();
				}
				setScrollBar();
			}
			ajaxLoadStop();
		}
	});
}

/**
 * 设置滚动条
 */
function setScrollBar() {
	set();
	// 检测滚动条是否重置大小（当窗口改变大小时）
	$("#orderTrackingAll").getNiceScroll().resize();
	// 整体的滚动条
    $("#orderTrackingAll").niceScroll({
        cursorcolor: "#666666", // 改变滚动条颜色，使用16进制颜色值
        cursorwidth: "6px", // 滚动条的宽度，单位：便素
        cursorborder: "1px solid #666666", // CSS方式定义滚动条边框
        cursorborderradius: "3px",
        scrollspeed: 60, // 滚动速度
        mousescrollstep: 40, // 鼠标滚轮的滚动速度 (像素)
        cursorminheight: 32, // 设置滚动条的最小高度 (像素)
        preservenativescrolling: true, // 你可以用鼠标滚动可滚动区域的滚动条和增加鼠标滚轮事件
        disableoutline: true, // 当选中一个使用nicescroll的div时，chrome浏览器中禁用outline
        horizrailenabled: true, // nicescroll可以管理水平滚动
        enablemousewheel: true, // nicescroll可以管理鼠标滚轮事件
        enablekeyboard: true, // nicescroll可以管理键盘事件
        smoothscroll: true, // ease动画滚动
        sensitiverail: true, // 单击轨道产生滚动
        enablemouselockapi: true, // 可以用鼠标锁定API标题 (类似对象拖动)
    });
    // 内部的滚动条
    $(".orderdetail_bottom").niceScroll({
        cursorcolor: "#aaaaaa",//#CC0071 光标颜色
        cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
        touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
        cursorwidth: "8px", //像素光标的宽度
        cursorborder: "0", //     游标边框css定义
        cursorborderradius: "5px",//以像素为光标边界半径
        autohidemode: false,//是否隐藏滚动条
        horizrailenabled:false
    });
}

/**
 * 填充数据到页面
 * @param progrssArr
 * @returns
 */
function addOrderProgressTrackingData(progressArr) {

	for (var i = 0; i < progressArr.length; i++) {
		// 订单进度对象
		var progress = progressArr[i];
		// 获取订单进度对象属性
		var id = progress.id;
		var askNum = progress.askNum;
		var askTime = progress.askTime;
		var askUserId = progress.askUserId;
		var companyId = progress.companyId;
		var activityId = progress.activityId;
		var groupId = progress.groupId;
		var groupOpenDate = progress.groupOpenDate;
		var orderId = progress.orderId;
		var orderNum = progress.orderNum;
		var orderPersonNumStr = progress.orderPersonNumStr;
		var orderStatusStr = progress.orderStatusStr;
		var orderCreateTime = progress.orderCreateTime;
		var orderCreateName = progress.orderCreateName;
		var confirmationFileSalerId = progress.confirmationFileSalerId;
		var confirmationFileSalerTime = progress.confirmationFileSalerTime;
		var activityCreateName = progress.activityCreateName;
		var firstOrderPayTime = progress.firstOrderPayTime;
		var lastOrderPayTime = progress.lastOrderPayTime;
		var lastOrderPayName = progress.lastOrderPayName;
		var orderStatus = progress.orderStatus ? progress.orderStatus : "0";
		// 获取渠道信息
		var agentName = progress.agentName;
		var contactList = progress.contactList;
		// 获取其他信息
		var activityName = progress.acitivityName;
		var groupCode = progress.groupCode;
		var freePosition = progress.freePosition;
		var orderCreateTime = progress.orderCreateTime;
		var officeName = progress.officeName;
		var contactPeople = progress.contactPeople;
		var setting = progress.setting;

		// 填充数据到页面
		var progressHtml = '';
		progressHtml += '<tbody><tr orderStatus=' + orderStatus + '>';
		// 订单号\询单号
		progressHtml += '<td class="ot_th_largest"><em class="fa fa-caret-right"></em><em class="fa fa-caret-down"></em><div class="ot_tb_num">';
		if (orderNum && orderNum != null) {
			progressHtml += '订单号：' + orderNum;
		} else {
			progressHtml += '询单号：' + askNum;
		}
		progressHtml += '</div>';
		progressHtml += '<div class="orderdetail"><div class="orderdetail_content"><div class="orderdetail_angle"></div>';
		if (orderNum && orderNum != null) {
			progressHtml += '<div class="orderdetail_title">订单号：' + orderNum + '</div>';
		} else {
			progressHtml += '<div class="orderdetail_title">询单号：' + askNum + '</div>';
		}
		progressHtml += '<div class="orderdetail_bottom"><ul class="orderdetail_ul">';
		progressHtml += '<li><label>产品名称：</label><span class="over_text">' + activityName + '</span></li>';
		progressHtml += '<li><label>产品团号：</label><span class="over_text">' + groupCode + '</span></li>';

		if (orderNum && orderNum != null) {
			progressHtml += '<li><label>出团日期：</label><span>' + groupOpenDate + '</span></li>';
			progressHtml += '<li><label>订单状态：</label><span>' + orderStatusStr + '</span></li>';
			progressHtml += '<li><label>下单日期：</label><span>' + orderCreateTime + '</span></li>';
			progressHtml += '<li><label>订单人数：</label><span>' + orderPersonNumStr + '</span></li>';
		} else {
			progressHtml += '<li><label>余位：</label><span>' + freePosition + '</span></li>';
			progressHtml += '<li><label>询单时间：</label><span>' + askTime + '</span></li>';
		}
		var isQuauqUser = $("#isQuauqUser").val();
		if (isQuauqUser == "0") {
			progressHtml += '<li><label>渠道名称：</label><span class="over_text">' + agentName + '</span></li>';
			if (contactList && contactList != "") {

				var contactArr = contactList.split("+");
				for (var j = 0; j < contactArr.length; j++) {
					var otherInfo = "";
					if (j == 0) {
						otherInfo = "渠道联系人：";
					}
					var contact = contactArr[j];
					var conArr = contact.split(" ");
					if (conArr.length == 1) {
						progressHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] + '</span><em>( <i class="fa fa-phone"></i> )</em></li>';
					} else {
						progressHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] +
						'</span><em>( <i class="fa fa-phone"></i> ' + conArr[1] + ' )</em></li>';
					}
				}

			}
		}
		if (isQuauqUser == "1" || $("#isQuauqAdmin").val() == "true") {
			progressHtml += '<li><label>供应商名称：</label><span class="over_text">' + officeName + '</span></li>';
			// 添加供应商联系人
			if (contactPeople && contactPeople != "") {
				var contactArr = contactPeople.split("+");
				for (var j = 0; j < contactArr.length; j++) {
					var otherInfo = "";
					if (j == 0) {
						otherInfo = "供应商联系人：";
					}
					var contact = contactArr[j];
					var conArr = contact.split(" ");
					if (conArr.length == 1) {
						progressHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] + '</span><em>( <i class="fa fa-phone"></i> )</em></li>';
					} else {
						progressHtml += '<li><label>' + otherInfo + '</label><span>' + conArr[0] +
						'</span><em>( <i class="fa fa-phone"></i> ' + conArr[1] + ' )</em></li>';
					}
				}

			} else {
				progressHtml += '<li><label>供应商联系人：</label><span></span><em>( <i class="fa fa-phone"></i>  )</em></li>';
			}
		}
		progressHtml += '</ul></div></div></div></td>';
		// 门店询单
		if (askTime && askTime != "") {
			var askTimeArr = askTime.split(" ");
			progressHtml += '<td class="ot_th_small"><div class="time_up"><div>' + askTimeArr[0] + '</div><div>' + askTimeArr[1] + '</div></div><i class="fa fa-check-circle" ></i></td>';
		} else {
			progressHtml += '<td class="ot_th_small"><em class="fa ot_false_cirl"></em></td>';
		}

		if (orderId && orderId != "") {
			var orderTimeArr = orderCreateTime.split(" ");
			// 销售处理中
			progressHtml += '<td class="ot_th_middle"><div class="time_up other"><div>' + orderTimeArr[0]
			 + '</div><div>' + orderTimeArr[1] + '</div></div><span class="ot_suc"></span><div class="time_bottom"></div>';
			// 销售是否已上传确认单
			if (confirmationFileSalerId && confirmationFileSalerId != "") {
				progressHtml += '<i class="fa check-square-o"></i>';
			}
			progressHtml += '<div class="operation_person">操作人：<span>' + orderCreateName + '</span></div></td>';
		} else {
			//有颜色的图标和时间变化
			//绿灯分钟数，黄灯分钟数
			var greenTime = 10;
			var yellowTime = 20;
			if (setting && setting != "") {
				var setArr = setting.split("+");
				for (var j = 0; j < setArr.length; j++) {
					var sets = setArr[j].split(" ");
					var settingType = sets[0];
					var greenLightTimeType = sets[1];
					var greenTime = sets[3];
					var yellowTime = sets[6];
					if (settingType == 1) {
						if (greenLightTimeType == 1) {
							greenTime = greenTime * 24 * 60;
							yellowTime = yellowTime * 24 * 60;
						} else if (greenLightTimeType == 2) {
							greenTime = greenTime * 60;
							yellowTime = yellowTime * 60;
						} else {
							greenTime = greenTime;
							yellowTime = yellowTime;
						}
						break;
					}
				}
			}
			// 销售处理中
			progressHtml += '<td  class="ot_th_middle" greenTime="' + greenTime + '" yellowTime="' + yellowTime + '"><span class="ot_wait"></span><div class="time_bottom"></div></td>';
		}
		// 计调处理中
		if (orderId && orderId != "") {
			if (confirmationFileSalerId && confirmationFileSalerId != "") {
				var date = "";
				var time = "";
				var opName = "";
				if (confirmationFileSalerTime && confirmationFileSalerTime != "") {
					var dateArr = confirmationFileSalerTime.split(" ");
					date = dateArr[0];
					time = dateArr[1];
				}
				progressHtml += '<td class="ot_th_middle"><div class="time_up other"><div>' + date + '</div><div>' + time + '</div></div><span class="ot_suc"></span>';
				progressHtml += '<div class="operation_person">操作人：<span>' + activityCreateName + '</span></div></td>';
			} else {

				//有颜色的图标和时间变化
				//绿灯分钟数，黄灯分钟数
				var greenTime = 10;
				var yellowTime = 20;
				if (setting && setting != "") {
					var setArr = setting.split("+");
					for (var j = 0; j < setArr.length; j++) {
						var sets = setArr[j].split(" ");
						var settingType = sets[0];
						var greenLightTimeType = sets[1];
						var greenTime = sets[3];
						var yellowTime = sets[6];
						if (settingType == 2) {
							if (greenLightTimeType == 1) {
								greenTime = greenTime * 24 * 60;
								yellowTime = yellowTime * 24 * 60;
							} else if (greenLightTimeType == 2) {
								greenTime = greenTime * 60;
								yellowTime = yellowTime * 60;
							} else {
								greenTime = greenTime;
								yellowTime = yellowTime;
							}
							break;
						}
					}
				}
				// 计调处理中
				progressHtml += '<td class="ot_th_middle" greenTime="' + greenTime + '" yellowTime="' + yellowTime + '"><span class="ot_wait"></span><div class="time_bottom"></div></td>';
			}
		} else {
			progressHtml += '<td></td>';
		}
		// 财务收款
		if (firstOrderPayTime && firstOrderPayTime != "") {
			if (lastOrderPayTime && lastOrderPayTime != "") {
				var dateArr = lastOrderPayTime.split(" ");
				progressHtml += '<td class="caiwu"><div class="time_up other ot_wait_cw"><div>' + dateArr[0] + '</div><div>' + dateArr[1] + '</div></div>' +
				'<span class="ot_suc last"></span><div class="time_bottom"></div>' +
				'<div class="operation_person">操作人：<span>' + lastOrderPayName + '</span></div></td>';
			} else {

				var dateArr = firstOrderPayTime.split(" ");
				date = dateArr[0];
				time = dateArr[1];

				//有颜色的图标和时间变化
				//绿灯分钟数，黄灯分钟数
				var greenTime = 10;
				var yellowTime = 20;
				if (setting && setting != "") {
					var setArr = setting.split("+");
					for (var j = 0; j < setArr.length; j++) {
						var sets = setArr[j].split(" ");
						var settingType = sets[0];
						var greenLightTimeType = sets[1];
						var greenTime = sets[3];
						var yellowTime = sets[6];
						if (settingType == 3) {
							if (greenLightTimeType == 1) {
								greenTime = greenTime * 24 * 60;
								yellowTime = yellowTime * 24 * 60;
							} else if (greenLightTimeType == 2) {
								greenTime = greenTime * 60;
								yellowTime = yellowTime * 60;
							} else {
								greenTime = greenTime;
								yellowTime = yellowTime;
							}
							break;
						}
					}
				}
				// 计调处理中
				progressHtml += '<td class="ot_th_large" date="' + date + '" time="' + time + '" greenTime="' + greenTime + '" yellowTime="' + yellowTime
				 + '"><span class="ot_wait last"></span><div class="time_bottom"></div></td>';
			}
		} else {
			progressHtml += '<td class="ot_th_large"></td>';
		}
		progressHtml += '</tr></tbody>';

		$(".orderT_cont_table table").append(progressHtml);
	}

	changeTime();
}

/**
 * 动态加载完数据后
 */
function initLoadData() {

	/*表格内不同样式的实现*/
	//ot_wait标签----等待的样式
	//ot_false标签----交易失败的样式
	//ot_false_last标签----交易失败的最后一个位置的样式
	//ot_suc标签----交易成功的样式

	//table中不同的数据对应插入的html内容，直接在table中添加对应标签即可
	$(".ot_wait,.ot_suc,.ot_false").empty().append("<span> • • • • • • • • • • <i class='fa fa-chevron-right'></i> • • • • • • • • • •</span>"); //中间是箭头的格式

	//闪烁部分样式较为特殊，单独进行判定和设置
	$(".ot_wait").append("<i class='fa fa-dot-circle-o' style='display:none'></i>");
	$(".ot_wait").each(function () {
		$(".ot_wait").addClass("white_text");
		$(".ot_wait").parents("tr").find(".ot_tb_num").addClass("white_text");
		$(".ot_wait").next(".time_bottom").addClass("alwaysshow");
	});

	$(".ot_suc").append("<i class='fa fa-check-circle'></i>");
	$(".ot_false").append("<em class='fa ot_false_cirl'></em>");
	$(".ot_false_last").append("<span> • • • • • • • • • • <i class='fa fa-times'></i> • • • • • • • • • •</span>"); //中间是x的格式
	$(".ot_false_last").prev(".time_up").addClass("notsee");

	$(".last").children().empty();
	$(".last").next(".time_bottom").removeClass("alwaysshow");

	// 点击时表格时会出现详细信息
	$(".orderT_cont_table tbody").click(function (event) {

		var $this_tbody = $(this).children();
		var clickOrder = $(event.target).parents().hasClass("orderdetail");
		if (clickOrder) {
			// 如果点击的是弹出的窗口，则不会有任何动作
		} else {
			// 如果点击的是表格的tbody本身，则判定，是否已经展开了。
			if ($this_tbody.height() != 140) {
				// 如果没有展开，点击展开该行表格
				$this_tbody.find(".fa-caret-down,.time_up,.time_bottom,.operation_person").show(); // 显示第一格内的向下三角符号，显示详细时间的div
				$this_tbody.find(".fa-caret-right").hide(); // 隐藏第一格内的向左三角符号
				$this_tbody.addClass("ot_td_select"); // 改变行高为130px
				$this_tbody.children("td:last-child").find(".time_bottom").addClass("alwaysshow");
				$("#mainId").parent().remove();
			} else {
				// 点击收起该行表格，实现效果与上段代码相反
				$this_tbody.find(".fa-caret-down,.time_up,.time_bottom,.operation_person").hide();
				$this_tbody.find(".fa-caret-right").show();
				$this_tbody.children("td:last-child").find(".time_bottom").removeClass("alwaysshow");
				$this_tbody.removeClass("ot_td_select");
			}
			setScrollBar();
		}
	});
	
    //当第一格的单号被hover时，判定对应的行，显示详细内容
    $(".ot_tb_num").mouseenter(function(event){
        var $orderdetail = $(this).parent().find(".orderdetail");
        var pointY = event.pageY;
        var wheight = $(window).height();
        var deheight = 5+$orderdetail.height();
        var shheight = -35-$orderdetail.height();
        if(wheight-pointY>330){
            $orderdetail.show();
        }else{
            $orderdetail.addClass("change_position").show();
            $(".change_position").css("margin-top",shheight+"px");
            $(".change_position").find(".orderdetail_angle").addClass("oppsite_angle");
            $(".oppsite_angle").css("top",deheight+"px");
        }
    });

    $(".ot_tb_num").mouseleave(function(event){
        var $orderdetail = $(this).parent().find(".orderdetail");
        var deheight = 5+$orderdetail.height();
        var shheight = -35-$orderdetail.height();
        //当鼠标移到下面详细信息上时，不关闭信息窗口
        $(this).next(".orderdetail").hover(function(){
            // console.log($orderdetail.hasClass("change_position"));
            if($(window).height()-event.pageY>300){
                $orderdetail.show();
            }else{
                $orderdetail.addClass("change_position").show();
                $(".change_position").css("margin-top",shheight+"px");
                $(".change_position").find(".orderdetail_angle").addClass("oppsite_angle");
                $(".oppsite_angle").css("top",deheight+"px");
            }
        },function(){
            $orderdetail.hide();
        });
        $orderdetail.removeClass("change_position").hide();
        $orderdetail.find(".orderdetail_angle").removeClass("oppsite_angle");
        $orderdetail.css("margin-top","0px");
    });
	
	
	
	
	
	
	
	// 判定，当最后一个表格的class是ot_suc，即默认整行都完成了，则在最后一格的下方添加完成字样
	$("tbody tr td:nth-child(6) span").each(function () {
		if ($(this).attr("class") == "ot_suc") {}
	});

	// 每个表格下方时间
	mostchange();

	// 处理取消和已删除订单
	$("tr[orderStatus=99],tr[orderStatus=111]").each(function (index, obj) {
		// 已完成的
		$(this).find(".fa-check-circle").removeClass("fa-check-circle").addClass("ot_false_cirl");
		// 处理中的
		$(this).find(".ot_wait").removeClass(); // 闪烁样式及亮度样式
		$(this).find(".fa-chevron-right:last").removeClass("fa-chevron-right").addClass("fa-times"); // 向右箭头改为中断箭头
		$(this).find(".fa-dot-circle-o").removeClass("fa-dot-circle-o").addClass("ot_false_cirl"); // 待操作原点改为实心原点
		//$(this).find(".alwaysshow").removeClass().addClass("time_bottom alwaysshow");
		$(this).find(".alwaysshow").remove();
	});
}

/**
 * 刷新数据, 重新加载数据更新订单状态
 * @returns
 */
function myrefresh() {
	findOrderProgressTracking('');
}

/**
 * 时间和日期js函数
 * @param i
 * @returns
 */
function checkTime(i) {
	if (i < 10) {
		i = "0" + i
	}
	return i
}

// 将周几转化为汉字
function transChinese(i) {
	switch (i) {
	case 1:
		i = "星期一";
		break;
	case 2:
		i = "星期二";
		break;
	case 3:
		i = "星期三";
		break;
	case 4:
		i = "星期四";
		break;
	case 5:
		i = "星期五";
		break;
	case 6:
		i = "星期六";
		break;
	case 0:
		i = "星期日";
		break;
	}
	return i;
}

// 表头上面获取当前日期
function showNowTime() {
	var nowtime = new Date(); // 获取到当前时间;
	var newtime = checkTime(nowtime.getDate()) + " / " + checkTime(nowtime.getMonth() + 1) + " / " + nowtime.getFullYear();
	document.getElementById("ot_week").innerHTML = transChinese(nowtime.getDay());
	document.getElementById("ot_now_time").innerHTML = newtime;
}

/**
 * 更改表内时间
 * @param tdId 表格td的ID
 * @param mi 绿灯时间
 * @param min 黄灯时间
 */
function changeTime() {

	// ot_wait样式表示未完成操作下划线
	$(".ot_wait").each(function () {

		// 获取红绿灯时间
		var $td = $(this).parent();
		var mi = $td.attr("greenTime");
		var min = $td.attr("yellowTime");

		// 包含时间div
		var $output_time = $(this).next(".time_bottom");

		// 财务时间
		$td = $(this).parent();
		var cw_date = $td.attr("date");
		var cw_time = $td.attr("time");

		var data_str;
		var time_str;
		// 如果是财务则需单独计算
		if (cw_date && cw_time) {
			data_str = cw_date;
			time_str = cw_time;
		} else {
			data_str = $(this).parents("tr").find(".time_up:not(.ot_wait_cw):last div:first-child").html();
			time_str = $(this).parents("tr").find(".time_up:not(.ot_wait_cw):last div:last-child").html();
		}

		var remindTime = data_str + "," + time_str;

		// 之前的类型为2016-06-05 10:08:44,必须转化为2008/04/02 10:08:44格式才能实例化Date对象
		var str = remindTime.toString();

		str = str.replace(/-/g, "/"); // 将获取到的字符串里面的-更改为/
		var starttime = new Date(str); // 询价时间
		var nowtime = new Date(); // 当前时间

		var lefttime = parseInt((nowtime.getTime() - starttime.getTime()) / 1000);
		var d = parseInt(lefttime / 3600 / 24);
		var h = parseInt((lefttime / 3600) % 24);
		var m = parseInt((lefttime / 60) % 60);
		var s = parseInt(lefttime % 60);
		m = checkTime(m);
		s = checkTime(s);

		// 填写操作步骤已经过了多少时间
		$output_time.html(d + "天" + h + "小时" + m + "分" + s + "秒");

		// 获取本次操作已用时多少分钟
		var countMinute = lefttime / 60;
		// 获取待操作样式
		var changecolor = $(this).find(".fa-dot-circle-o");
		// 显示
		changecolor.show();
		// 根据批发商设置黄绿灯时间设置显示时间颜色及频率
		if (countMinute <= mi) {
			$(changecolor).removeClass("orange_text").addClass("green_text");
			$(this).next(".time_bottom").removeClass("orange_text").addClass("green_text");
			$(changecolor).fadeOut(1000).fadeIn(1000);
		} else if (countMinute > mi && countMinute <= min) {
			$(changecolor).removeClass("green_text").addClass("orange_text");
			$(this).next(".time_bottom").removeClass("green_text").addClass("orange_text");
			$(changecolor).fadeOut(500).fadeIn(500);
		} else {
			$(changecolor).removeClass("green_text  orange_text");
			$(this).next(".time_bottom").removeClass("green_text  orange_text").addClass("red_text");
			$(changecolor).fadeOut(250).fadeIn(250).fadeOut(250).fadeIn(250);
		}
	});
	setTimeout("changeTime()", 1000);
}

/**
 * 计算总共用了多少时间
 */
function mostchange() {
	// 时间计算
	$(".ot_false,.ot_false_last,.ot_suc").each(function () {
		var $output_time = $(this).next(".time_bottom");
		// 上一个节点的时间值
		var data_str1 = $(this).parent().prev("td").find(".time_up:last div:first-child").html();
		var time_str1 = $(this).parent().prev("td").find(".time_up:last div:last-child").html();
		// 这个节点的时间值
		var data_str2 = $(this).parent().find(".time_up div:first-child").html();
		var time_str2 = $(this).parent().find(".time_up div:last-child").html();

		if (!data_str1 || !time_str1 || !data_str2 || !time_str2) {
			return true;
		}

		// 返回时间的差值
		var friTime = data_str1 + "," + time_str1;
		var secTime = data_str2 + "," + time_str2;
		counttime(friTime, secTime, $output_time);
	});

	// 财务用时
	$("td.caiwu").each(function () {

		$time = $(this).find(".ot_suc");

		var $output_time = $time.next(".time_bottom");
		// 上一个节点的时间值：销售下单时间
		var data_str1 = $time.parent().prev("td").prev("td").find(".time_up:last div:first-child").html();
		var time_str1 = $time.parent().prev("td").prev("td").find(".time_up:last div:last-child").html();
		// 这个节点的时间值
		var data_str2 = $time.parent().find(".time_up div:first-child").html();
		var time_str2 = $time.parent().find(".time_up div:last-child").html();

		if (!data_str1 || !time_str1 || !data_str2 || !time_str2) {
			return false;
		}

		// 返回时间的差值
		var friTime = data_str1 + "," + time_str1;
		var secTime = data_str2 + "," + time_str2;
		counttime(friTime, secTime, $output_time);
	});
};

function counttime(fritime, sectime, outhtml) {
	var fristr = fritime.toString();
	var secstr = sectime.toString();
	fristr = fristr.replace(/-/g, "/"); // 将获取到的字符串里面的-更改为/
	secstr = secstr.replace(/-/g, "/"); // 将获取到的字符串里面的-更改为/

	var starttime = new Date(fristr); // 询价时间
	var nowtime = new Date(secstr); // 当前时间

	var lefttime = parseInt((nowtime.getTime() - starttime.getTime()) / 1000);
	var d = parseInt(lefttime / 3600 / 24);
	var h = parseInt((lefttime / 3600) % 24);
	var m = parseInt((lefttime / 60) % 60);
	var s = parseInt(lefttime % 60);
	m = checkTime(m);
	s = checkTime(s);

	outhtml.html(d + "天" + h + "小时" + m + "分" + s + "秒");
}
/*点击订单状态，切换选中tab页*/
function onSelect(obj) {
	$(obj).parent().children().each(function () {
		$(this).removeClass('ot_choose');
	});
	$(obj).addClass('ot_choose');
}

function findOrderProgressTracking(selectType) {

	// 页签选中
	$(".ot_left span").removeClass("ot_choose");
	if (selectType && selectType == "0") {
		$(".ot_left span:eq(1)").addClass("ot_choose");
	} else if (selectType && selectType == "1") {
		$(".ot_left span:eq(2)").addClass("ot_choose");
	} else {
		$(".ot_left span:eq(0)").addClass("ot_choose");
	}
	$("#pageNoForProgress").val(1);
	$(".orderT_cont_table tbody").empty();
	$("#selectType").val(selectType);
	loadProgressDate();
}

function ajaxLoadStart(){
	var html ='<div class="ajaxLoadingMask"></div><div class="ajaxLoading">正在加载，请等待...</div>';
	$("body").append(html);
}
function ajaxLoadStop(){
	$("div.ajaxLoadingMask, div.ajaxLoading").remove();
}