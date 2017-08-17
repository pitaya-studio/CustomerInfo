var ctxStatic;
var groupInfoList; //团期json数据
var groupMonths = []; //获取团期年月（不包括日）
var monthDivWidth = 59; //控件月份宽度
var compareArr = []; //团期对比数组
var RowCount = 9;
var leaveActionID = null;
$(function () {
	ctxStatic = $("#ctxStatic").val();
	ctx = $("#ctx").val();
	
	//加载初始团期对比数据
	//getCompareGroupInfo();
	//当鼠标移动到出截团日期上：对比div显示
	$("#contentTable").on("mouseenter", "tbody tr td[name='outDate']", function () {
		var $this = $(this);
		var activityId = $this.find("[name=activityId]").val();
		var groupId = $this.find("[name=groupId]").val();
		groupInfoList = getGroupInfoByActivityId($this);
		groupMonths = GetGroupMonths(groupInfoList);
		var leaveActionID = $this.data('leaveActionID');
		if (leaveActionID) {
			clearTimeout(leaveActionID);
			$this.data('leaveActionID', '');
		}
		var enterActionID = $this.data('enterActionID');
		if (!enterActionID) {
			enterActionID = setTimeout(function () {
					if (!$this.find(".container_calendar").length) {
						var currentYearMonth;
						for (var i = 0; i < groupInfoList.length; i++) {
							if (groupInfoList[i].isCurrent == 1) {
								var tmp1 = new Date(groupInfoList[i].groupOpenDate);
								currentYearMonth = tmp1.getFullYear() + "-" + (Number(tmp1.getMonth()) + 1);
							}
						}
						if (!currentYearMonth) {
							var tmp1 = new Date(groupInfoList[0].groupOpenDate);
							currentYearMonth = tmp1.getFullYear() + "-" + (Number(tmp1.getMonth()) + 1);
						}
						ShowCalendar_Month(groupMonths, currentYearMonth.split('-')[0], currentYearMonth.split('-')[1]);
						CreateCalendar(currentYearMonth.split('-')[0], currentYearMonth.split('-')[1]);
						$this.find(".close-date").after($(".container_calendar"));
					}
					$(".container_calendar").addClass("calendarShow");
					$(".container_calendar").show();
					$(".month-childdiv .month").each(function () {
						var year = $this.attr("year");
						var month = $this.attr("month");
						if ((year + "-" + month) == currentYearMonth) {
							$this.addClass("active");
							$this.siblings().removeClass('active');
						}
					});
				}, 500);
			$this.data('enterActionID', enterActionID);
		}
		
		//悬浮框上复选框选中与否和内容显示（全部取消对比或全部加入对比）
		setcCeckboxStatus(groupInfoList);
	});
	
	//当鼠标离开日历弹出框，则关闭日历弹出框（开启500毫毛一次定时隐藏任务）
	$("#contentTable").on("mouseleave", "tbody tr td[name='outDate']", function () {
		var $this = $(this);
		var enterActionID = $this.data('enterActionID');
		if (enterActionID) {
			clearTimeout(enterActionID);
			$this.data('enterActionID', '');
		}
		var leaveActionID = $this.data('leaveActionID');
		if (!leaveActionID) {
			//日历弹出框定时任务，500毫毛执行一次隐藏
			leaveActionID = setTimeout(function () {
					$(".container_calendar").hide();
				}, 500);
			$this.data('leaveActionID', leaveActionID);
		}
	});
});

$(window).load(function() {
	
	$(".pagination li").each(function(index, obj) {
		$(this).html($(this).html().replace("page(", "calendarPage("));
	});
	
	//加载初始团期对比数据
	getCompareGroupInfo();
	
	//如果产品下的团期有余位或有切位则产品出团日期为高亮，否则为灰色，且不会弹出日历
	$("tr[id^=parent]").each(function(index, obj) {
		var $groupTr = $(this).next();
		if ($("input[type=button].btn-primary", $groupTr).length > 0) {
			$(this).find(".out-date").css("color", "#08c");
		}
	});
});

/**
 * 悬浮框上复选框选中与否和内容显示（全部取消对比或全部加入对比）
 * @param groupInfoList
 */
function setcCeckboxStatus(groupInfoList) {
	
	var groupCodes = "";
	$("#compareTable tr.newdataBgColor").each(function(index, obj) {
		var groupCode = $(obj).children("td:first").find("span").text();
		groupCodes += groupCode + ",";
	})
	
	var flag = true;
	for (var i = 0; i < groupInfoList.length; i++) {
		var groupInfo = groupInfoList[i];
		if (groupInfo) {
			var groupCode = groupInfo.groupCode;
			if (groupCodes.indexOf(groupCode) >= 0) {
				continue;
			} else {
				flag = false;
				break;
			}
		}
	}
	
	if (flag) {
		$("input[type='checkbox']").attr("checked", true);
		$("input[type='checkbox']").siblings().html("全部取消对比");
	} else {
		$("input[type='checkbox']").attr("checked", false);
		$("input[type='checkbox']").siblings().html("全部加入对比");
	}
}

/**
 * 获取团期对比信息
 */
function getCompareGroupInfo () {
	$.ajax({
		type : "POST",
		async : false,
		url: ctx + "/activity/calendarforOrder/getCompareGroupIds",
		data: {},
		success: function(result){
			if (result && result != "") {
				getGroupInfoByGroupIds(eval(result));
			}
        }
	});

	$("#compareTableContainer").hide();
	$("#expandCompare").text("展开对比");
}

function getGroupInfoByGroupIds(groupInfoList) {
	var groupCodes = [];
	var sortName = "";
	for (var i = 0; i < groupInfoList.length; i++) {
		sortName = groupInfoList[i].sortName;
		if (!AddToCompareArrYN(groupInfoList[i].groupCode)) {
			compareArr.unshift(groupInfoList[i]);
			groupCodes.push(groupInfoList[i].groupCode);
		}
	}
	if (sortName && sortName != "") {
		$("#compareTable thead [name=" + sortName + "]").find("a[name=sortHref]").trigger("click");
	}
	var year = $(".month-childdiv .active").attr("year");
	var month = $(".month-childdiv .active").attr("month");
	CreateCalendar(year, month);
	CreateCompareTable(compareArr);
	groupInfoList = [];
}

/**
 * 根据产品查询其所有团期并放入groupInfoList数组
 * @param obj
 * @returns groupInfoList
 */
function getGroupInfoByActivityId(obj) {
	groupInfoList = [];
	var groupInfo = {}; //团期信息对象

	var $activityTr = obj.parent(); //产品tr对象
	var activityId = $("[name=activityId]", $activityTr).val(); //产品id
	var activityTypeName = $("[name=activityTypeName]", $activityTr).val(); //产品系列
	var productName = $("td[name=productName]", $activityTr).text(); //产品名称
	var activityTypeName = $("[name=activityTypeName]", $activityTr).val(); //产品系列
	var startPlace = $("td[name=startPlace]", $activityTr).text(); //出发地
	var airComp = $("td[name=airComp]", $activityTr).text(); //航空公司
	var visaCountry = $("td[name=visaCountry]", $activityTr).text(); //签证国家
	var operater = $("td[name=operater]", $activityTr).text(); //操作员
	
	$("#teamTable tbody tr", $activityTr.next()).each(function(index, obj) {
		
		if ($(this).find("input.gray").length > 0) {
			return;
		}
		
		var groupId = $("[name=groupId]", this).val(); //团期ID
		var recommendGroupYN = $("[name=recommendGroupYN]", this).val(); //是否推荐
		var groupCode = $("td[name=groupCode]", this).text(); //团期编号
		var groupOpenDate = $("td[name=groupDate] div:eq(0)", this).text(); //出团日期
		var groupCloseDate = $("td[name=groupDate] div:eq(1)", this).text(); //截团日期
		var deadlineDate = $("td[name=deadlineDate]", this).text(); //资料截止日期
		var customerPrice = $("td[name=customerPrice]", this).text(); //成人同行价
		var convertedSettlementPrice = $(":hidden[class='convertedSettlementPrice']", this).val();//转换之后的同行价
		var customerchildPrice = $("td[name=customerchildPrice]", this).text(); //儿童同行价
		var customerSpecialPrice = $("td[name=customerSpecialPrice]", this).text(); //特殊人群同行价
		var channelPrice = $("td[name=channelPrice]", this).text(); //成人直客价
		var convertedSuggestPrice = $(":hidden[class='convertedSuggestPrice']", this).val();//转换之后的直客价
		var channelchildPrice = $("td[name=channelchildPrice]", this).text(); //儿童直客价
		var channelSpecialPrice = $("td[name=channelSpecialPrice]", this).text(); //特殊人群直客价
		var roompriceDiff = $("td[name=roompriceDiff]", this).text(); //单房差
		var prePay = $("td[name=prePay]", this).text(); //预收
		var selledPositions = $("td[name=positions] div:eq(0)", this).text(); //售出切位
		var positions = $("td[name=positions] div:eq(1)", this).text(); //已切位
		var freePositions = $("td[name=freePositions]", this).text(); //余位
		var preSign = $("td[name=groupPrePersonNum]", this).text(); //预报名
		var bookInputHtml = $.trim($("td:last input", this).prop("outerHTML")); //预定html
		bookInputHtml = bookInputHtml.replace("agentType(this)" , "compareBookOrder('" + $.trim(groupCode) + "')");
		
		//对象清空
		groupInfo = {};
		
		//添加产品属性
		groupInfo.activityId = $.trim(activityId);
		groupInfo.productName = $.trim(productName);
		groupInfo.activityTypeName = $.trim(activityTypeName);
		groupInfo.activityTypeName = $.trim(activityTypeName);
		groupInfo.startPlace = $.trim(startPlace);
		groupInfo.airComp = $.trim(airComp);
		groupInfo.visaCountry = $.trim(visaCountry);
		groupInfo.operater = $.trim(operater);
		
		//添加团期属性
		groupInfo.groupId = $.trim(groupId);
		groupInfo.recommendGroupYN = $.trim(recommendGroupYN); //是否是推荐团期，0 不是 1 是
		groupInfo.groupCode = $.trim(groupCode);
		groupInfo.groupOpenDate = $.trim(groupOpenDate);
		groupInfo.groupCloseDate = $.trim(groupCloseDate);
		groupInfo.deadlineDate = $.trim(deadlineDate);
		groupInfo.customerPrice = convertNumber($.trim(customerPrice));
		groupInfo.convertedSettlementPrice = $.trim(convertedSettlementPrice) == "" ? 0 : $.trim(convertedSettlementPrice);
		groupInfo.customerchildPrice = convertNumber($.trim(customerchildPrice));
		groupInfo.customerSpecialPrice = convertNumber($.trim(customerSpecialPrice));
		groupInfo.channelPrice = convertNumber($.trim(channelPrice));
		groupInfo.convertedSuggestPrice = $.trim(convertedSuggestPrice)== "" ? 0 : $.trim(convertedSuggestPrice);
		groupInfo.channelchildPrice = convertNumber($.trim(channelchildPrice));
		groupInfo.channelSpecialPrice = convertNumber($.trim(channelSpecialPrice));
		groupInfo.roompriceDiff = convertNumber($.trim(roompriceDiff));
		groupInfo.prePay = $.trim(prePay);
		groupInfo.selledPositions = $.trim(selledPositions);
		groupInfo.positions = $.trim(positions);
		groupInfo.freePositions = $.trim(freePositions);
		groupInfo.preSign = $.trim(preSign);
		groupInfo.bookInputHtml = $.trim(bookInputHtml);
		//添加对象其他属性
		if ($.trim($activityTr.find(".out-date").text()) == $.trim(groupOpenDate)) {
			groupInfo.isCurrent = 1;
		} else {
			groupInfo.isCurrent = 0;
		}
		groupInfo.groupYN = 1; //是否有团期 0：没有 1：有
		groupInfo.addToCompare = 0; //是否已加入对比，0：没有 1：有
		
		//把对象添加到数组
		groupInfoList.push(groupInfo);
	});
	groupInfoList.sort(function (a, b) {
		var date1 = new Date(Date.parse(a.groupOpenDate));  
        var date2 = new Date(Date.parse(b.groupOpenDate));  
		var isTrue = date1.getTime() > date2.getTime();
		if (isTrue) {
			return 1;
		} else {
			return -1;
		}
	});
	return groupInfoList;
}

function convertNumber(num) {
	if (num && num.length > 3) {
		return num.substr(0, num.length - 3);
	} else {
		return "";
	}
}

function compareBookOrder(groupCode) {
	$(".container_calendar").hide();
	$("td[name=bookeOrder_" + groupCode + "] input").trigger("click");
}

function CreateCalendar(year, month) {
	var daysArr = new Array();
	$("#div_calendar").empty();
	if (year == "" && month == "") {
		daysArr = GetSelectedMonDays(groupInfoList);
	} else {
		daysArr = drawCalendar(year, month);
	}
	ShowCalendar_days(CombineDateDays(daysArr, groupInfoList), groupInfoList);
}
function drawCalendar(Year, Month) {
	if (Year == "" && Month == "") {
		Year = new Date().getFullYear();
		Month = new Date().getMonth() + 1;
	}
	firstDay = new Date(Year, Month - 1, 1).getDay();
	monthDay = new Date(Year, Month, 0).getDate();
	var Days = new Array();
	var lastOrder = 0;
	for (var i = 0; i < firstDay; i++) {
		var day = {};
		day.order = i + 1;
		day.witchMonth = -1;
		if (Month - 1 == 0) {
			day.month = 12;
			day.year = Year - 1;
		} else {
			day.month = Month - 1;
			day.year = Year;
		}
		day.currentday = new Date(Year, Month - 1, 0).getDate() - firstDay + i + 1;
		day.date = day.year + "-" + ("0" + day.month).substr(("0" + day.month).length - 2, 2) + "-" + ("0" + day.currentday).substr(("0" + day.currentday).length - 2, 2);
		day.week = i;
		Days.push(day);
	}
	var currentmonthWeekday = firstDay;
	for (var i = 1; i <= monthDay; i++) {
		var day = {};
		day.order = i + firstDay;
		day.witchMonth = 0;
		day.year = Year;
		day.month = Month;
		day.currentday = i;
		day.date = day.year + "-" + ("0" + day.month).substr(("0" + day.month).length - 2, 2) + "-" + ("0" + day.currentday).substr(("0" + day.currentday).length - 2, 2);
		day.week = currentmonthWeekday;
		currentmonthWeekday++;
		if (currentmonthWeekday > 6) {
			currentmonthWeekday = 0
		}
		lastOrder = day.order;
		Days.push(day);
	}
	var nextMonthday = 1;
	var nextmonthWeekday = currentmonthWeekday + 1;
	if (lastOrder < 42) {
		var deadlineOrder = (Math.ceil(lastOrder / 7)) * 7;
		if (lastOrder < deadlineOrder) {
			for (var i = lastOrder + 1; i <= deadlineOrder; i++) {
				var day = {};
				day.order = i;
				day.witchMonth = 1;
				if (Month + 1 == 13) {
					day.year = Year + 1;
					day.month = 1;
				} else {
					day.year = Year;
					day.month = Month + 1;
				}
				day.currentday = nextMonthday;
				day.date = day.year + "-" + ("0" + day.month).substr(("0" + day.month).length - 2, 2) + "-" + ("0" + day.currentday).substr(("0" + day.currentday).length - 2, 2);
				nextMonthday++;
				day.week = nextmonthWeekday;
				if (nextmonthWeekday > 6) {
					nextmonthWeekday = 0
				}
				Days.push(day);
			}
		}
	}
	return Days;
}
$(document).ready(function () {
	$("#div_calendar").on('mouseenter', '.day', function () {
		var Day = $(this).data('Day');
		if (Day.groupYN == 1) {
			$(this).find(".dayNum>span").css("color", "white");
			if (Day.recommendGroupYN != 1) {
				$(this).find(".dayNum").removeClass("daybgImgRec");
				$(this).find(".dayNum").addClass("daybgImgT");
			}
		}
	});
	$("#div_calendar").on('mouseleave', '.day', function () {
		var Day = $(this).data('Day');
		if (Day.groupYN == 1) {
			if (Day.recommendGroupYN != 1) {
				$(this).find(".dayNum>span").css("color", "#ccc");
			}
			$(this).find(".dayNum").removeClass("daybgImgT");
		}
	});
	$("#div_calendar").on('mouseenter', '.dayNum', function () {
		var $this = $(this);
		var enterActionID = $this.data('enterActionID');
		if (!enterActionID) {
			enterActionID = setTimeout(function () {
					var $dayDiv = $this.parent();
					var Day = $dayDiv.data('Day');
					var freePositions = (Day.freePositions == undefined) ? "" : Day.freePositions;
					var customerPrice = (Day.customerPrice == undefined) ? "" : Day.customerPrice;
					var channelPrice = (Day.channelPrice == undefined) ? "" : Day.channelPrice;
					if (Day.groupYN == 1) {
						if ($dayDiv.find('.groupDetail').length) {
							$this.find('.groupDetail').show();
						} else {
							var groupDetail = $(".groupDetail[templete='true']").clone();
							groupDetail.removeAttr("templete");
							groupDetail.find("div[name='dayNumAddCom'] a").before("<span>" + Day.currentday + "</span>");
							groupDetail.find("div[name='dayNumAddCom']").addClass("daybgImgRec");
							groupDetail.find("div[name='dayNumAddCom']").addClass("addToCompare");
							groupDetail.find("#freePosition").html("<div class='fl divInline detail1 bgcolor'>余</div><div class='fr divInline'><span class='detail1'>" + Day.freePositions + "</span><span class='detail2'>/</span><span class='detail2'>" + Day.positions + "</span></div>");
							if (Day.customerPrice == "") {
								groupDetail.find("#customerPrice").html("<div class='fl divInline detail1 bgcolor'>同</div><div class='fr divInline'><span class='detail2'></span><span class='detail1'>" + Day.customerPrice + "</span><span class='detail2'></span></div>");
							} else {
								groupDetail.find("#customerPrice").html("<div class='fl divInline detail1 bgcolor'>同</div><div class='fr divInline'><span class='detail1'>" + Day.customerPrice + "</span><span class='detail2'>起</span></div>");
							}
							if (Day.channelPrice == "") {
								groupDetail.find("#channelPrice").html("<div class='fl divInline detail1 bgcolor'>直</div><div class='fr divInline'><span class='detail2'></span><span class='detail1'>" + Day.channelPrice + "</span><span class='detail2'></span></div>");
							} else {
								groupDetail.find("#channelPrice").html("<div class='fl divInline detail1 bgcolor'>直</div><div class='fr divInline'><span class='detail1'>" + Day.channelPrice + "</span><span class='detail2'>起</span></div>");
							}
							$this.append(groupDetail);
							if (AddToCompareArrYN(Day.groupCode)) {
								groupDetail.find("div[name='dayNumAddCom']").addClass("addCompared");
								groupDetail.find("a").unbind("click");
							} else {
								groupDetail.find("a").on("click", function () {
									if ($("#compareTableContainer").css("display") == "none") {
										$("#compareTableContainer").show();
									}
									fn_AddToCompare(groupDetail, Day);
								});
							}
							groupDetail.find("div[name='dayNumAddCom'] span").css("color", "white");
							groupDetail.find("div[name='goToBook']").on("click", function () {
								var groupCodeVal = $(this).parent().parent().parent().data("Day").groupCode;
								compareBookOrder(groupCodeVal);
							});
							groupDetail.show();
						}
					}
				}, 500);
			$this.data('enterActionID', enterActionID);
		}
	});
	$("#div_calendar").on('mouseleave', '.dayNum', function () {
		var $this = $(this);
		var enterActionID = $this.data('enterActionID');
		if (enterActionID) {
			clearTimeout(enterActionID);
			$this.data('enterActionID', '');
		}
		$(this).parent().find('.groupDetail').hide();
	});
	$(document).on("click", ".container_month .month", function () {
		var $month = $(this);
		CreateCalendar($month.attr("year"), $month.attr("month"));
		$month.addClass('active');
		$month.siblings().removeClass('active');
	});
	
	/* 出团日期、资料截止日期、同行价、直客价排序 */
	$("#compareTable thead").find("a[name=sortHref]").on("click", function() {
		
		//获取排序名称
		var sortName = $(this).parent().attr("name");
		
		//如果以前没有排序，则默认按升序排序
		if ($(this).parent().attr("firstTime") == "1") {
			$(this).parent().attr("firstTime", "0");
			$(this).parent().attr("orderType", "asc");
			$(this).siblings().attr("src", ctxStatic + "/images/asc.png");
		}
		
		//对比数组排序
		SortByProsName(compareArr, sortName, $(this).parent().attr("orderType"));
		
		//循环四个排序列，除了当前列，其余排序列属性firstTime都要置成1
		$("#compareTable thead").find("a[name=sortHref]").each(function(index, obj) {
			if (sortName != $(this).parent().attr("name")) {
				$(this).parent().attr("firstTime" , 1);
			}
		});
		
		//修改排序列orderType属性并修改对应排序图片
		if ($(this).parent().attr("orderType") == "asc") {
			$(this).parent().attr("orderType", "desc");
			$(this).siblings().attr("src", ctxStatic + "/images/asc.png");
		} else {
			$(this).parent().attr("orderType", "asc");
			$(this).siblings().attr("src", ctxStatic + "/images/desc.png");
		}
		
		//删除默认黄色
		RemoveBgColor();
		
		//排序值传递到后台供前端加载调用
		$.ajax({
			type : "POST",
			url : ctx + "/activity/calendarforOrder/saveCompareSortName?dom=" + Math.random(),
			data : {sortName : sortName},
			success : function(result) {
	        }
		});
	});
	
	$("input[type='checkbox']").on("click", function () {
		if ($(this).prop("checked")) {
			if ($("#compareTableContainer").css("display") == "none") {
				$("#compareTableContainer").show();
			}
			var groupCodes = [];
			for (var i = 0; i < groupInfoList.length; i++) {
				if (!AddToCompareArrYN(groupInfoList[i].groupCode)) {
					compareArr.unshift(groupInfoList[i]);
					groupCodes.push(groupInfoList[i].groupCode);
				}
			}
			$(this).parent().data("activityId", groupInfoList[0].activityId);
			$(this).parent().data("groupId", groupInfoList[0].groupId);
			$(this).siblings().html("全部取消对比");
			// 将一个产品下的所有团期全部加入对比
			$.ajax({
				type: "POST",
				traditional:true,
				url: ctx + "/activity/calendarforOrder/saveAllActivityGroupCompares",
				data: {'groupCodes': groupCodes},
				success: function(result){
		        }
			});
			
		} else {
			var length = compareArr.length;
			var groupCodes = [];
			for (var i = length - 1; i >= 0; i--) {
				groupCodes.push(compareArr[i].groupCode);
				if (compareArr[i].activityId == $(this).parent().data("activityId")) {
					compareArr[i].isSorted = false;
					compareArr.remove(compareArr[i]);
				}
			}
			$(this).siblings().html("全部加入对比");
			
			// 将一个产品下的所有团期全部取消对比
			$.ajax({
				type: "POST",
				traditional:true,
				url: ctx + "/activity/calendarforOrder/delAllActivityGroupCompares",
				data: {'groupCodes': groupCodes},
				success: function(result){
		        }
			});
			
		}
		var year = $(".month-childdiv .active").attr("year");
		var month = $(".month-childdiv .active").attr("month");
		CreateCalendar(year, month);
		CreateCompareTable(compareArr);
		$("#compareTableContainer").show();
		if (compareArr.length == 0) {
			$("#compareTableContainer").hide();
		}
	});
	$("#expandCompare").on("click", function () {
		if ($("#compareTableContainer").is(":hidden")) {
			$("#compareTableContainer").show();
			CreateCompareTable(compareArr);
			$(this).html("收起对比");
		} else {
			$("#compareTableContainer").hide();
			$(this).html("展开对比");
		}
	});
	$("#expandCompare").hover(function () {
		$(this).removeClass("mouseleaved");
		$(this).addClass("hovering");
	}, function () {
		$(this).removeClass("hovering");
		$(this).addClass("mouseleaved");
	});
	
	//点击清空按钮：对比div隐藏、对比内容清空、对比数组清空、设置对比为展开
	$(".operatorContainer #clear").on("click", function () {
		top.$.jBox.confirm('是否要清空对比列表','系统提示',function(v) {	
			if (v == 'ok') {
				clearCompareGroupInfo();
            }
		},{buttonsFocus:1});
		top.$('.jbox-body .jbox-icon').css('top','55px');
		return false;  
	});
});

/**
 * 点击清空按钮：对比div隐藏、对比内容清空、对比数组清空、设置对比为展开
 */
function clearCompareGroupInfo() {
	$("#compareTableContainer").hide();
	$("#compareTable tbody").empty();
	$("#div_calendar .comparedBgColor").removeClass("comparedBgColor");
//	$("input[type='checkbox']").trigger("click");
	compareArr = [];
	$("#expandCompare").text("展开对比");
	
	// 发送 Ajax 请求，清楚所有对比信息
	$.ajax({
		type: "POST",
		url: ctx + "/activity/calendarforOrder/clearAllActivityGroupCompares",
		success: function(result){
        }
	});
}

function getObjectByProp(arr, propName, propValue) {
	if (!arr || arr.length == 0) {
		return null;
	}
	for (var index in arr) {
		if (arr[index][propName] == propValue) {
			return arr[index];
		}
	}
}

function GetGroupMonths(groupInfoList) {
	groupMonths = [];
	groupMonths.push(new Date(groupInfoList[0].groupOpenDate).getFullYear() + "-" + (new Date(groupInfoList[0].groupOpenDate).getMonth() + 1));
	var tmp;
	for (var i = 1; i < groupInfoList.length; i++) {
		tmp = new Date(groupInfoList[i].groupOpenDate);
		if (groupMonths.indexOf(tmp.getFullYear() + "-" + (tmp.getMonth() + 1)) == -1) {
			groupMonths.push(tmp.getFullYear() + "-" + (tmp.getMonth() + 1));
		}
	}
	return groupMonths;
}
function GetSelectedMonDays(groupInfoList) {
	var daysArr = new Array();
	var tmp1;
	for (var i = 1; i < groupInfoList.length; i++) {
		if (groupInfoList[i].isCurrent == 1) {
			tmp1 = new Date(groupInfoList[i].groupOpenDate);
			daysArr = drawCalendar(Number(tmp1.getFullYear()), Number(tmp1.getMonth() + 1));
		}
	}
	return daysArr;
}
function CombineDateDays(daysArr, groupInfoList) {
	var infoDay;
	var groupCodeInfo;
	for (var i = 0; i < daysArr.length; i++) {
		var day = daysArr[i];
		day.groupCode = "";
		infoDay = getObjectByProp(groupInfoList, "groupOpenDate", day.date)
		$.extend(day, infoDay);
	}
	return daysArr;
}
function ShowCalendar_days(Days, groupInfoList) {
	for (var i = 0; i < Days.length; i++) {
		var dayDiv = "";
		var freePositions = (Days[i].freePositions == undefined) ? "" : Days[i].freePositions;
		var customerPrice = (Days[i].customerPrice == undefined) ? "" : Days[i].customerPrice;
		var channelPrice = (Days[i].channelPrice == undefined) ? "" : Days[i].channelPrice;
		var groupYN = (Days[i].groupYN == undefined) ? 0 : Days[i].groupYN;
		if (freePositions == "") {
			dayDiv += "<div class='day' witchMonth='" + Days[i].witchMonth + "'>" + "   <div class='dayNum'>" + "       <span>" + Days[i].currentday + "</span>" + "   </div>" + "   <div name='goToBook'>" + "   <div class='freePositionCss'><span class='fpContent'>" +
			freePositions + "  </span> </div>";
		} else {
			dayDiv += "<div class='day' witchMonth='" + Days[i].witchMonth + "'><div class='dayNum'><span>" +
			Days[i].currentday + "</span></div><div name='goToBook'><div class='freePositionCss'><span class='fpTitle'>余位</span><span class='fpContent'>" +
			freePositions + "</span></div>";
		}
		if (customerPrice == "" && channelPrice != "") {
			dayDiv += "<div class='price'>" + channelPrice + "</div></div>";
		} else if (customerPrice != "") {
			dayDiv += "<div class='price'><span class='fpTitle'>" + "</span><span class='fpContent'>" + customerPrice + "</span></div></div>";
		} else {
			dayDiv += "</div></div>";
		}
		var $dayDiv = $(dayDiv);
		if (Days[i].recommendGroupYN == 1) {
			$dayDiv.find(".dayNum").addClass("daybgImgRec");
			$dayDiv.find(".dayNum span").css("color", "white");
		}
		if (AddToCompareArrYN(Days[i].groupCode)) {
			$dayDiv.addClass("comparedBgColor");
		}
		if ($dayDiv.attr("witchMonth") == -1 || $dayDiv.attr("witchMonth") == 1) {
			$dayDiv.addClass("notCurrentMonthDay");
		} else {
			$dayDiv.addClass("currentMonthDay");
		}
		$dayDiv.data('Day', Days[i]);
		if (Days[i].groupCode != undefined) {
            $dayDiv.find("div[name='goToBook']").on("click", function () {
                var groupCodeVal = $(this).parent().data("Day").groupCode;
                compareBookOrder(groupCodeVal);
            })
        }
		$("#div_calendar").append($dayDiv);
	}
}
function ShowCalendar_Month(groupMonths, year, month) {
	var months = "<div class='month-parentdiv'><div class='month-childdiv'>";
	$(".container_month :not(div.scollBtn)").remove();
	for (var i = 0; i < groupMonths.length; i++) {
		var tmpY = groupMonths[i].split('-')[0];
		var tmpM = groupMonths[i].split('-')[1];
		if (year + month == tmpY + tmpM) {
			months += "<div class='month active' year='" + tmpY + "' month='" + tmpM + "'>" + tmpM + "月" + "</div>"
		} else {
			months += "<div class='month' year='" + tmpY + "' month='" + tmpM + "'>" + tmpM + "月" + "</div>"
		}
	}
	months += "</div></div>";
	$(".container_month div:first-child").after(months);
	$(".container_month .month").css("width", monthDivWidth + "px");
	var leftOffset = $(".month-childdiv").css("left").substr(0, $(".month-childdiv").css("left").length - 2);
	var offsetCount = parseInt(Math.abs(($(".month-childdiv").css("left").substr(0, $(".month-childdiv").css("left").length - 2)) / monthDivWidth));
	if (leftOffset == 0) {
		$(".container_month div:first").addClass("clickDisabled");
	}
	var rightMonth,
	monthsCount = $(".container_month .month").length;
	if (monthsCount <= 6) {
		rightMonth = $(".container_month .month:last-child").html();
	} else {
		rightMonth = $(".container_month .month").eq(offsetCount + 6 - 1).html();
	}
	if ((rightMonth.substr(0, rightMonth.length - 1) == groupMonths[groupMonths.length - 1].split('-')[1]) || (monthsCount <= 6)) {
		$(".container_month div:last").addClass("clickDisabled");
	}
}
function prevMonth() {
	var leftOffset = $(".month-childdiv").css("left").substr(0, $(".month-childdiv").css("left").length - 2);
	var offsetCount = parseInt(Math.abs(($(".month-childdiv").css("left").substr(0, $(".month-childdiv").css("left").length - 2)) / monthDivWidth));
	var leftMonth = $(".container_month .month").eq(offsetCount).html();
	if (leftMonth.substr(0, leftMonth.length - 1) == groupMonths[0].split('-')[1]) {
		$(".container_month div:first").addClass("clickDisabled");
	} else {
		var newToLeft = Number(leftOffset) + (monthDivWidth + 3) + "px";
		$(".month-childdiv").css("left", newToLeft);
		leftMonth = $(".container_month .month").eq(offsetCount - 1).html();
		if (leftMonth.substr(0, leftMonth.length - 1) == groupMonths[0].split('-')[1]) {
			$(".container_month div:first").addClass("clickDisabled");
		}
		$(".container_month div:last").removeClass("clickDisabled");
	}
}
function nextMonth() {
	var leftOffset = $(".month-childdiv").css("left").substr(0, $(".month-childdiv").css("left").length - 2);
	var offsetCount = parseInt(Math.abs(($(".month-childdiv").css("left").substr(0, $(".month-childdiv").css("left").length - 2)) / monthDivWidth));
	var rightMonth,
	monthsCount = $(".container_month .month").length;
	if (monthsCount <= 6) {
		rightMonth = $(".container_month .month:last-child").html();
	} else {
		rightMonth = $(".container_month .month").eq(offsetCount + 6 - 1).html();
	}
	if ((rightMonth.substr(0, rightMonth.length - 1) == groupMonths[groupMonths.length - 1].split('-')[1]) || (monthsCount <= 6)) {
		$(".container_month div:last").addClass("clickDisabled");
	} else {
		var newToLeft = leftOffset - (monthDivWidth + 3) + "px";
		$(".month-childdiv").css("left", newToLeft);
		rightMonth = $(".container_month .month").eq(offsetCount + 1 + 6 - 1).html();
		if ((rightMonth.substr(0, rightMonth.length - 1) == groupMonths[groupMonths.length - 1].split('-')[1])) {
			$(".container_month div:last").addClass("clickDisabled");
		}
		$(".container_month div:first").removeClass("clickDisabled");
	}
}
function AddToCompareArrYN(groupcode) {
	for (var i = 0; i < compareArr.length; i++) {
		if (compareArr[i].groupCode == groupcode) {
			compareArr[i].addToCompare = 1;
			return 1;
		}
	}
	return 0;
}
function CreateCompareTable(arrs) {
	$("#compareTable tbody").empty();
	for (var i = 0; i < arrs.length; i++) {
		var tr = "<tr>";
		var data = arrs[i];
		tr += "<td style='color: #08c'><span>" + data.groupCode + "</span></td><td style='color: #08c'><span>" + data.productName + "</span></td>";
		tr += "<td><div>" + data.groupOpenDate + "</div><div>" + data.groupCloseDate + "</div></td><td><span>" + data.deadlineDate + "</span></td>";
		tr += "<td class='tr' style='color: red;'><span>" + data.customerPrice + "</span></td><td class='tr'><span>" + 
		data.customerchildPrice + "</span></td><td class='tr'><span>" + data.customerSpecialPrice + "</span></td>";
		tr += "<td class='tr' style='color: red;'><span >" + data.channelPrice + "</span></td><td class='tr'><span>" + data.channelchildPrice + "</span></td><td class='tr'><span>" + data.channelSpecialPrice + "</span></td>";
		tr += "<td class='tr'><span>" + data.roompriceDiff + "</span></td><td><span>" + data.preSign + "</span>/<span>" + data.prePay + "</span></td>";
		tr += "<td><span>" + data.selledPositions + "</span>/<span>" + data.positions + "</span></td>";
		tr += "<td><span>" + data.freePositions + "</span></td><td><span>" + data.activityTypeName + "</span></td>";
		tr += "<td><span>" + data.startPlace + "</span></td><td><span>" + data.airComp + "</span></td>";
		tr += "<td><span>" + data.visaCountry + "</span></td><td><span>" + data.operater + "</span></td>";
		tr += "" + "<td class='tc' style='width: 50px'><div style='width:80px'>" + data.bookInputHtml + "   <span  name='removeData' class='removeData' style='float:right'>" + "       <a></a>" + "   </span>" + "</div></td>";
		tr += "</tr>";
		var $tr = $(tr);
		if (!data.isSorted) {
			$tr.addClass("newdataBgColor");
		}
		$tr.data('Day', data);
		$tr.hover(function () {
			$(this).find("span[name='removeData'] a").addClass("cancelComp");
			$(this).find("span[name='removeData'] a").css("display", "inline-block");
			$(this).find("td:last span").hover(function () {
				$(this).find("a").removeClass("cancelComp");
				$(this).find("a").addClass("cancelCompHover");
				$(this).on("click", function () {
					fn_cancelCompare($(this));
				})
			}, function () {
				$(this).find("a").addClass("cancelComp");
				$(this).find("a").removeClass("cancelCompHover");
			})
		}, function () {
			$(this).find("td:last span a").hide();
		});
		$("#compareTable tbody").append($tr);
		if ($("#compareTable").attr("display") == "none") {
			$("#compareTable").show();
		}
	}
}
function fn_cancelCompare($this) {
	var currentData = $this.parent().parent().parent().data("Day");
	var length = compareArr.length;
	for (var i = length - 1; i >= 0; i--) {
		if (compareArr[i].groupCode == currentData.groupCode) {
			compareArr[i].isSorted = false;
			compareArr.remove(compareArr[i]);
		}
	}
	var currentY = $(".month-parentdiv .month.active").attr("year");
	var currentM = $(".month-parentdiv .month.active").attr("month");
	CreateCalendar(currentY, currentM);
	var $currentTr = $this.parent().parent().parent();
	var currentHeight = $currentTr.height();
	$currentTr.css({
		height : currentHeight
	});
	$currentTr.find('td').css({
		padding : 0
	});
	$currentTr.find('td').empty();
	$currentTr.slideUp(300, function () {
		$currentTr.remove();
	});
	
	// 发送 Ajax 请求，删除此条对比信息
	$.ajax({
		type: "POST",
		url: ctx + "/activity/calendarforOrder/delActivityGroupCompare",
		data: {'groupCode': currentData.groupCode},
		success: function(result){
        }
	});
}
function SortByProsName(arr, propName, orderType) {
	var sortedArr = arr.sort(function (a, b) {
			var isTrue;
			if (orderType == "asc") {
				if ("groupOpenDate" == propName || "deadlineDate" == propName) {
					var date1 = new Date(Date.parse(a[propName]));  
			        var date2 = new Date(Date.parse(b[propName]));  
					var isTrue = date1.getTime() > date2.getTime();
				} else {
					isTrue = parseInt(a[propName]) > parseInt(b[propName]);
				}
			} else {
				if ("groupOpenDate" == propName || "deadlineDate" == propName) {
					var date1 = new Date(Date.parse(a[propName]));  
			        var date2 = new Date(Date.parse(b[propName]));  
					var isTrue = date1.getTime() < date2.getTime();
				} else {
					isTrue = parseInt(a[propName]) < parseInt(b[propName]);
				}
			}
			if (isTrue) {
				return 1;
			} else {
				return -1;
			}
		});
	CreateCompareTable(sortedArr);
	for (var i = 0; i < arr.length; i++) {
		arr[i].isSorted = true;
	}
}
function RemoveBgColor() {
	$("#compareTable tbody tr").each(function () {
		$(this).removeClass("newdataBgColor");
	})
}
function CompareTablePaging(arr, pageIndex) {
	var pagedArr = [];
	pagedArr = arr.paging(pageIndex);
	return pagedArr;
}
//下载当前对比信息列表
function downloadCurrentCompare() {
	var groupCodes = [];
	
	$("#compareTable tr.newdataBgColor").each(function(index, obj){
	  var groupCode = $(obj).children("td:first").find("span").text();
	  groupCodes.push(groupCode);
	})
	// 发送 Ajax 请求，下载对比列表
	$.ajax({
		type: "POST",
		traditional:true,
		async:false,
		url: ctx + "/activity/calendarforOrder/downloadActivityGroupCompares",
		data: {'groupCodes': groupCodes},
		success: function(result){
        }
	});
}
function download() {
	var groupCodes = [];
	$("#compareTable tr").each(function(index, obj){
		var groupCode = $(obj).children("td:first").find("span").text();
		if (groupCode && groupCode != "") {
			groupCodes.push(groupCode);
		}
	});
	$("#groupform").attr("action", 
			ctx + "/activity/calendarforOrder/downloadActivityGroupCompares?groupCodes=" + groupCodes).submit();
	
}

function fn_AddToCompare($div, dayData) {
	
	$div.find("div[name='dayNumAddCom']").removeClass("addToCompare");
	$div.find("div[name='dayNumAddCom']").addClass("addCompared");
	dayData.isSorted = false;
	compareArr.unshift(dayData);
	$("#compareTable").show();
	$("#compareTable tbody").empty();
	if ($("#compareTableContainer").css("display") == "none") {
		$("#compareTableContainer").show();
	}
	CreateCalendar(dayData.year, dayData.month);
	CreateCompareTable(compareArr);
	// 发送 Ajax 请求，保存对比信息
	$.ajax({
		type: "POST",
		url: ctx + "/activity/calendarforOrder/saveActivityGroupCompare",
		data: {'groupCode': dayData.groupCode},
		success: function(result){
        }
	});
}

if (!Array.prototype.indexOf) {
	Array.prototype.indexOf = function (elt) {
		var len = this.length ? this.length : 0;
		var from = Number(arguments[1]) || 0;
		from = (from < 0) ? Math.ceil(from) : Math.floor(from);
		if (from < 0) {
			from += len;
		}
		for (; from < len; from++) {
			if (from in this && this[from] === elt) {
				return from;
			}
		}
		return -1;
	};
}
if (!Array.prototype.remove) {
	Array.prototype.remove = function (item) {
		var index = this.indexOf(item);
		if (index === -1) {
			return false;
		}
		for (var i = 0, n = 0; i < this.length; i++) {
			if (this[i] !== this[index]) {
				this[n++] = this[i];
			}
		}
		this.length -= 1;
		return true;
	};
}
if (!Array.prototype.paging) {
	Array.prototype.paging = function (index) {
		if (!index) {
			index = 1;
		}
		return this.slice(RowCount * (index - 1), RowCount * (index));
	};
}


function calendarSearch(type) {
	if (type == 1) {
		$("#searchForm").attr("action", contextPath + "/activity/calendarforOrder/list/" + $("#showType").val() + "/" + $("#activityKind").val()).submit();
	} else {
		window.open(contextPath + "/activity/managerforOrder/downloadAllYw/" + $("#showType").val() + "/" + $("#activityKind").val() + "?" + $('#searchForm').serialize());
		$("#searchForm").attr("action", contextPath + "/activity/managerforOrder/list/" + $("#showType").val() + "/" + $("#activityKind").val()).submit();
	}
}

function calendarPage(n,s){
    $("#pageNo").val(n);
    $("#pageSize").val(s);
    var showType=$("#showType").val();
    var activityKind=$("#activityKind").val();
    $("#searchForm").attr("action",contextPath+"/activity/calendarforOrder/list/"+showType+"/"+activityKind);
    $("#searchForm").submit();
}