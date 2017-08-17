<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>酒店产品--团期列表</title>
<!--[if lte IE 6]>
			<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
			<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
		<![endif]-->
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet"
	href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript"
	src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<!--订单模块的脚本-->
<script type="text/javascript" src="${ctxStatic}/js/tmp.orderform.js"></script>
<script type="text/javascript">
	$(function() {

		//搜索条件筛选
		launch_new1();
		launch_new2();
		//产品名称文本框提示信息
		inputTips();
		//操作浮框
		operateHandler();
		//天数插件
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

		if ($("#orderBy").length > 0) {
			var _$orderBy = $("#orderBy").val();
			if (_$orderBy == "") {
				_$orderBy = "ah.createDate";
			}
			var orderBy = _$orderBy.split(" ");
			var orderName = orderBy[0]=="ahgl.price"?orderBy[0].slice(5):orderBy[0].slice(3);
			$(".activitylist_paixu_left li").each(function () {
				if ($(this).hasClass("li" + orderName)) {
					orderBy[1] = orderBy[1] && orderBy[1].toUpperCase() == "ASC" ? "up" : "down";
					$(this).find("a").eq(0).html($(this).find("a").eq(0).html() + "<i class=\"icon icon-arrow-" + orderBy[1] + "\"></i>");
					$(this).attr("class", "activitylist_paixu_moren");
				}
			});
		}


		//产品名称团号切换
		switchNumAndPro();
		//收款确认提醒
		paymentTips();
		showSearchPanel();
		//初始化下拉
		//初始化下拉
		getAjaxSelect('island', 'c89e0a6661b64d1e809d8873cf85bc80',
				"${activityHotelQuery.island}");
	});
	//展开筛选按钮
	function launch_new1() {
		$('#zksx1').click(function() {
			if ($('#ydxbd1').is(":hidden") == true) {
				$('#ydxbd1').show();
				//$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			} else {
				$('#ydxbd1').hide();
				//$(this).text('展开筛选');
				$(this).removeClass('zksx-on');
			}
		});
	}
	function launch_new2() {
		$('#zksx2').click(function() {
			if ($('#ydxbd2').is(":hidden") == true) {
				$('#ydxbd2').show();
//				$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			} else {
				$('#ydxbd2').hide();
//				$(this).text('展开筛选');
				$(this).removeClass('zksx-on');
			}
		});
	}
	//展开收起
	function expand(child, obj) {
		if ($(child).is(":hidden")) {
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");
			$(obj).html("收起");
		} else {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
			$(obj).html("展开");
		}
	}
	//展开收起支付记录
	function expandShowPay(child, obj) {
		if ($(child).is(":hidden")) {
			$(child).show();
			$(obj).parents("td").addClass("td-extend");
			$(obj).parents("tr").addClass("tr-hover");
		} else {
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
		}
	}
	//展开、关闭
	function expand(child, obj, srcActivityId) {
		if ($(child).css("display") == "none") {
			$(obj).html("收起");
			$(child).show();
			$(obj).addClass('team_a_click2');
			$(obj).parents("td").addClass("td-extend").parent("tr").addClass(
					"tr-hover");
		} else {
			$(child).hide();
			$(obj).removeClass('team_a_click2');
			$(obj).parents("td").removeClass("td-extend").parent("tr")
					.removeClass("tr-hover");
			$(obj).html("展开");
		}
	}

	//更新酒店团期状态
	function updateGroupStatus(mess, uuid, status) {
		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				$.post("${ctx}/activityHotel/updateGroupStatusByUuid", {
					"uuids" : uuid,
					"status" : status
				}, function(data) {
					if (data.result == "success") {
						top.$.jBox.tip(data.message, 'success', {
							closed : $("#searchForm").submit()
						});
					} else {
						top.$.jBox.tip(data.message, 'warning');
					}
				});
			} else if (v == 'cancel') {

			}
		});
	}

	//更新酒店产品状态
	function updateGroupStatusByActivityUuid(mess, uuid, status) {
		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				$.post("${ctx}/activityHotel/updateGroupStatusByActivityUuid",
						{
							"uuids" : uuid,
							"status" : status
						}, function(data) {
							if (data.result == "success") {
								top.$.jBox.tip(data.message, 'success', {
									closed : $("#searchForm").submit()
								});
							} else {
								top.$.jBox.tip(data.message, 'warning');
							}
						});
			} else if (v == 'cancel') {

			}
		});
	}
	//批量更新酒店团期状态
	function batchUpdateGroupStatus(mess, status) {
		var idArray = [];
		$('input[name="ids"]:checked').each(function() {
			idArray.push($(this).val());
		});

		if (idArray == 0) {
			top.$.jBox.tip("请选择修改行记录!", 'message');
			return false;
		}

		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				$.post("${ctx}/activityHotel/updateGroupStatusByUuid", {
					"uuids" : idArray.join(";"),
					"status" : status
				}, function(data) {
					if (data.result == "success") {
						top.$.jBox.tip(data.message, 'success', {
							closed : $("#searchForm").submit()
						});
					} else {
						top.$.jBox.tip(data.message, 'warning');
					}
				});
			} else if (v == 'cancel') {

			}
		});
	}

	//有查询条件的时候，DIV不隐藏
	function showSearchPanel() {
		var activityName = "${activityHotelQuery.activityName }";
		var activitySerNum = "${activityHotelQuery.activitySerNum }";
		var currencyId = "${activityHotelQuery.currencyId}";
		var startPrice = "${activityHotelQuery.startPrice }";
		var endPrice = "${activityHotelQuery.endPrice }";
		var island = "${activityHotelQuery.island}";
		var hotel = "${activityHotelQuery.hotel }";
		var hotelstar = "${activityHotelQuery.hotelstar }";
		var startGroupDate = "${activityHotelQuery.startGroupDate }";
		var roomtype = "${activityHotelQuery.roomtype}";
		var foodtype = "${activityHotelQuery.foodtype}";
		var islandway = "${activityHotelQuery.islandway}";
		var endGroupDate = "${activityHotelQuery.endGroupDate }";
		//var status = "${status }";
		if (isNotEmpty(activityName) || isNotEmpty(hotel)
				|| isNotEmpty(currencyId) || isNotEmpty(startPrice)
				|| isNotEmpty(endPrice) || isNotEmpty(island)
				|| isNotEmpty(hotelstar) || isNotEmpty(activitySerNum)
				|| isNotEmpty(startGroupDate) || isNotEmpty(endGroupDate)
				|| isNotEmpty(roomtype) || isNotEmpty(foodtype)
				|| isNotEmpty(islandway)) {
			$('.zksx').click();
		}
	}

	//排序
	function cantuansortby(sortBy, obj) {
		var temporderBy = $("#orderBy").val();
		if (temporderBy.match(sortBy)) {
			sortBy = temporderBy;
			if (sortBy.match(/ASC/g)) {
				sortBy = $.trim(sortBy.replace(/ASC/gi, "")) + " DESC";
			} else {
				sortBy = $.trim(sortBy.replace(/DESC/gi, "")) + " ASC";
			}
		} else {
			sortBy = sortBy + " DESC";
		}
		$("#orderBy").val(sortBy);
		$("#searchForm").submit();
	}

	//分页
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	// 判定不为空值
	function isNotEmpty(str) {
		if (str != "" && str != null) {
			return true;
		}
		return false;
	}
	//全选
	function checkall(obj) {
		if ($(obj).attr("checked")) {
			$("input[name='ids']").attr("checked", 'true');
			$("input[name='allChk']").attr("checked", 'true');
		} else {
			$("input[name='ids']").removeAttr("checked");
			$("input[name='allChk']").removeAttr("checked");
		}
	}

	//列表展示切换
	function changelist() {
		//var status = ${activityStatus};
		//window.open(g_context_url+"/hotelControl/hotelControlList?showType=1&activityStatus="+status,'_self');
		window
				.open(
						"${ctx}/activityIsland/islandProductList?activityStatus=5&showType=2",
						'_self');
	}

	function changePageByStatus(obj) {
		var showType = '${showType}';
		window.open("${ctx}/activityIsland/islandProductList?status="
				+ obj.value + "&showType=" + showType, '_self');
	}

	//清空方法
	function delFlag(uuid) {
		$.jBox.confirm("确定要清空数据吗？", "系统提示", function(v, h, f) {
			if (v == 'ok') {
				$.jBox.tip("正在清空数据...", 'loading');
				$.post("${ctx}/activityIsland/deleteGroup", {
					"uuids" : uuid
				}, function(data) {
					if (data.result == "1") {
						$.jBox.prompt("清空成功!", 'success', 'info', {
							closed : function() {
								$("#searchForm").submit();
							}
						});
					} else {
						top.$.jBox.tip(data.message, 'warning');
					}
				});

			} else if (v == 'cancel') {

			}
		});
	}

	function query() {
		$("#searchForm").submit();
	}

	function getAirlineAjaxSelect(type, obj, value) {
		$.ajax({
			type : "POST",
			url : "${ctx}/activityIsland/airlineAjaxCheck",
			data : {
				"type" : type,
				"uuid" : $(obj).val(),
				"airline" : $(airline).val(),
				"flightnum" : $(flightnum).val()
			},
			dataType : "json",
			success : function(data) {
				if (data) {
					if (type == "airline") {
						$.each(data.airline, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.airlineName).attr(
											"value", n.airlineCode));
						});
						if (value) {
							$("#" + type).val(value);
							getAirlineAjaxSelect("flightnum", $("#" + type),
									"activityHotelQuery.airline");
						}
					} else if (type == "flightnum") {
						$("#" + type).text("").append(
								$("<option/>").text("不限").attr("value", ""));
						$.each(data.flightnum, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.flightnumber).attr(
											"value", n.flightnumber));
						});
						if (value) {
							$("#" + type).val(value);
							getAirlineAjaxSelect("spacelevel", $("#" + type),
									"activityHotelQuery.flightnum");
						}
					} else if (type == "spacelevel") {
						$("#" + type).text("").append(
								$("<option/>").text("不限").attr("value", ""));
						$.each(data.spacelevel, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.space).attr("value",
											n.spaceLevel));
						});
						if (value) {
							$("#" + type).val(value);
						}
					}
				}
			}
		});
	}

	function getAjaxSelect(type, obj, value) {
		$.ajax({
			type : "POST",
			url : "${ctx}/hotelControl/ajaxCheck",
			data : {
				"type" : type,
				"uuid" : $(obj).val()
			},
			dataType : "json",
			success : function(data) {
				if (type != "islandway") {
					$("#" + type).empty();
					$("#" + type).append("<option value=''>不限</option>");
				} else {
					$("#islandway").empty();
					$("#hotel").empty();
					$("#roomtype").empty();
					$("#islandway").append("<option value=''>不限</option>");
					$("#hotel").append("<option value=''>不限</option>");
					$("#roomtype").append("<option value=''>不限</option>");
				}
				if (data) {
					if (type == "hotel") {
						$.each(data.hotelList, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.nameCn).attr("value",
											n.uuid));
						});
						if (value) {
							$("#" + type).val(value);
							getAjaxSelect(tranferObj(type), $("#" + type),
									tranferValue(type));
						}
					} else if (type == "roomtype") {
						$.each(data.roomtype, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.roomName).attr(
											"value", n.uuid));
						});
						if (value) {
							$("#" + type).val(value);
							getAjaxSelect(tranferObj(type), $("#" + type),
									tranferValue(type));
						}
					} else if (type == "foodtype") {
						$.each(data.hotelMeals, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.mealName).attr(
											"value", n.uuid));
						});
						if (value) {
							$("#" + type).val(value);
							getAjaxSelect(tranferObj(type), $("#"
									+ tranferObj(type)), tranferValue(type));
						}
					} else if (type == "islandway") {
						$.each(data.listIslandWay, function(i, n) {
							$("#" + type).append(
									$("<option/>").text(n.label).attr("value",
											n.uuid));
						});
						$.each(data.hotelList, function(i, n) {
							$("#hotel").append(
									$("<option/>").text(n.nameCn).attr("value",
											n.uuid));
						});
						if (value) {
							$("#islandway").val(value);
							$("#hotel").val("${hotelControlQuery.hotel}");
							getAjaxSelect(tranferObj("hotel"), $("#hotel"),
									tranferValue("hotel"));
						}
					} else if (type == "island") {
						$.each(data.islandList, function(i, n) {
							$("#island").append(
									$("<option/>").text(n.islandName).attr(
											"value", n.uuid));
						});
						if (value) {
							$("#" + type).val(value);
							getAjaxSelect(tranferObj(type), $("#" + type),
									tranferValue(type));
						}
					}
				}
			}
		});
	}

	function tranferObj(obj) {
		if (obj == 'country') {
			return 'island';
		} else if (obj == 'island') {
			return 'islandway';
		} else if (obj == 'hotel') {
			return 'roomtype';
		} else if (obj == 'roomtype') {
			return 'foodtype';
		}
		return '';
	}
	function tranferValue(obj) {
		if (obj == 'country') {
			return '${hotelControlQuery.island}';
		} else if (obj == 'island') {
			return '${hotelControlQuery.islandway}';
		} else if (obj == 'hotel') {
			return '${hotelControlQuery.roomtype}';
		} else if (obj == 'roomtype') {
			return '${hotelControlQuery.foodtype}';
		}
		return '';
	}
</script>
</head>
<body>
	<page:applyDecorator name="hotel_order"></page:applyDecorator>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form method="post"
			action="${ctx}/hotelOrder/hotelOrderList?showType=1"
			id="searchForm" modelAttribute="activityHotelQuery">
			<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden" />
			<input id="pageSize" name="pageSize" value="${pageSize}"
				type="hidden" />
			<input id="orderBy" name="orderBy" value="${orderBy}" type="hidden" />
			<input id="showType" name="showType" value="${showType}"
				type="hidden" />
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<input type="text"class="searchInput" id="groupCode" name="groupCode"
						   value="${activityHotelQuery.groupCode }" placeholder="请输入团号" />
				</div>
				<a class="zksx" id="zksx2">筛选</a>
				<div class="form_submit">
					<input type="button" value="搜索" onclick="query(100)"
						   class="btn btn-primary ydbz_x" />
                    <%--<input type="reset" value="清空所有条件"class="btn ydbz_x" />--%>
                    <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
				</div>
				<div class="ydxbd" id="ydxbd2" style="display:none;">
					<span></span>
					<%-- <div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">团期状态：</div>
						<select name="status" id="status">
							<option value="0">不限</option>
							<option value="1" <c:if test="${status=='1' }">selected</c:if> >已上架</option>
							<option value="2" <c:if test="${status=='2' }">selected</c:if>>已下架</option>
							<option value="3" <c:if test="${status=='3' }">selected</c:if>>保存草稿</option>
							<option value="4" <c:if test="${status=='4' }">selected</c:if>>已删除</option>
						</select>
					</div> --%>
					<!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
					<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">控房单号：</div>
					<input type="text" id="activitySerNum" name="activitySerNum"
						value="${activityHotelQuery.activitySerNum }" />
				</div> --%>
				<div class="activitylist_bodyer_right_team_co3 pr">
						<div class="activitylist_team_co3_text">产品名称：</div>
						<input type="text" value="${activityHotelQuery.activityName }"
							class="inputTxt inputTxtlong" name="activityName"
							id="activityName" placeholder=""请输入产品名称/>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">岛屿名称：</div>
						<div class="selectStyle">
							<select name="island" id="island"
								onchange="getAjaxSelect('islandway',this);">
								<option value="" selected="selected">不限</option>
							</select>
						</div>

					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">酒店名称：</div>
						<div class="selectStyle">
						<select name="hotel" id="hotel"
							onchange="getAjaxSelect('roomtype',this);">
							<option value="" selected="selected">不限</option>
						</select>
						</div>
						</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">酒店星级：</div>
						<div class="selectStyle">
						<form:select path="hotelstar">
							<option value="">不限</option>
							<form:options items="${hotelStarList}" itemValue="uuid"
								itemLabel="label" />
						</form:select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">房型：</div>
						<div class="selectStyle">
							<select name="roomtype" id="roomtype"
								onchange="getAjaxSelect('foodtype',this);">
								<option value="" selected="selected">不限</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">基础餐型：</div>
						<div class="selectStyle">
							<select name="foodtype" id="foodtype">
								<option value="" selected="selected">不限</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">上岛方式：</div>
						<div class="selectStyle">
							<select class="sel-w1" name="islandway" id="islandway"
								onchange="getAjaxSelect('',this);">
								<option value="" selected="selected">不限</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co4">
						<div class="activitylist_team_co3_text">同行价格：</div>
						<div class="selectStyle">
						<form:select path="currencyId">
							<option value="">不限</option>
							<form:options items="${currencyList}" itemValue="id"
								itemLabel="currencyName" />
						</form:select>
						</div>
						<input type="text" id="startPrice" class=" inputTxt"
							name="startPrice" value="${activityHotelQuery.startPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11" />
						<span>至</span> <input type="text" id="endPrice" class=" inputTxt"
							name="endPrice" value="${activityHotelQuery.endPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11" />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">日期：</label> <input
							type="text" id="startGroupDate" class="inputTxt dateinput"
							name="startGroupDate"
							value="${activityHotelQuery.startGroupDate}"
							onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('endGroupDate').value==''){$dp.$('endGroupDate').value=vvv;}},maxDate:'#F{$dp.$D(\'endGroupDate\')}'})"
							readonly="readonly" style=" width: 102px;"/> <span> 至 </span> <input type="text"
							id="endGroupDate" class="inputTxt dateinput" name="endGroupDate"
							value="${activityHotelQuery.endGroupDate}"
							onclick="WdatePicker({minDate:'#F{$dp.$D(\'startGroupDate\')}'})"
							readonly="readonly" style=" width: 102px;"/>
					</div>
				</div>
			</div>
		</form:form>
		<!--查询结果筛选条件排序开始-->
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul id="sort-list-ul-id">

						<li class="activitylist_paixu_left_biankuang licreateDate" sortc="lastEstimatePriceTime" onclick="cantuansortby('ah.createDate',this)" sortv="1">
							<a>创建时间<i class="icon" style="display: none;"></i></a>
						</li>
						<li class="activitylist_paixu_left_biankuang liupdateDate"sortc="lastOperatorGivenTime" sortv="1" onclick="cantuansortby('ah.updateDate',this)">
							<a>更新时间<i class="icon" style="display: none;"></i></a>
						</li>
						<li class="activitylist_paixu_left_biankuang liprice" sortc="lastCreateProductTime" sortv="1" onclick="cantuansortby('ahgl.price',this)">
							<a> 同行价格<i class="icon" style="display: none;"></i></a>
						</li>

					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong id="eprice-list-count-id">0</strong>&nbsp;条
				</div>
			</div>
		</div>
		<!--查询结果筛选条件排序结束-->
		<table id="contentTable"
			class="table mainTable activitylist_bodyer_table sea_rua_table">
			<thead>
				<tr>
					<th width="3%">序号</th>
					<th width="10%">团号/日期</th><!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
					<th width="6%" align="right"><!-- <span style="display:none">控房单号/</span> -->产品名称</th>
					<th width="4%">岛屿</th>
					<th width="7%">酒店&amp;星级</th>
					<th width="8%">房型 * 晚数</th>
					<th width="4%">基础餐型</th>
					<th style="display:none;">升级餐型&升餐价格</th>
					<th width="4%">上岛方式</th>
					<th width="7%">同行价格
					</th>
					<th width="6%">余位/预报名</th>
					<th width="9%">单房差</th>
					<th width="9%">需交订金</th>
					<th width="4%">状态</th>
					<th style="display:none;">备注</th>
					<th width="4%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty page.list}">
					<tr class="toptr">
						<td colspan="16" style="text-align: center;">暂无相关信息</td>
					</tr>
				</c:if>
				<c:forEach items="${page.list }" var="entry" varStatus="s">
					<tr id="${entry.uuid}" style="display: table-row;"
						data-tag="${entry.uuid}">
						<!-- 单选框 -->
						<td rowspan="${entry.baseMealNum}" class="tc">
							<input type="checkbox" onclick="" name="ids" value="${entry.uuid}" />${s.index+1}
							<input type="hidden" id="status${entry.uuid }" value="${entry.status }"/>	
						</td>
						<!-- 团号/日期 -->
						<td rowspan="${entry.baseMealNum }" class="tc">
						    <a href="${ctx}/activityHotel/showActivityHotelDetail/${entry.uuid}?type=group" target="_blank">${entry.groupCode}</a><br/> 
						    <span><fmt:formatDate value="${entry.groupOpenDate}" pattern="yyyy-MM-dd" /></span>
						</td>
						<!-- 产品名称 --><!-- 根据需求隐藏空房单号  zhangchao 2016/01/07-->
						<td rowspan="${entry.baseMealNum }" class="tc"><%-- <span style="display:none">${entry.activitySerNum}<br/></span> --%>${entry.activityName}</td>
						<!-- 岛屿 -->
						<td rowspan="${entry.baseMealNum }" class="tc" data-value="${entry.island_uuid}">
							<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${entry.island_uuid}" />
						</td>
						<!-- 酒店/星级 -->
						<td rowspan="${entry.baseMealNum }"  class="tc" data-value="${entry.hotel_uuid}">
							<trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${entry.hotel_uuid}" />
							<span class="y_xing"><br/> 
								<c:forEach begin="1" end="${entry.hotel_star }">★</c:forEach>
							</span>
						</td>
						<!-- 房型 * 晚数 -->
						<c:choose>
							<c:when test="${fn:length(entry.groupRoomList)==0}">
                                         		<td></td><td></td>
                                     </c:when>
							<c:otherwise>
								<c:forEach begin="0" end="0" var="room" items="${entry.groupRoomList}">
		                        	<td rowspan="${fn:length(room.activityHotelGroupMealList)==0?1:fn:length(room.activityHotelGroupMealList)}" class="tc">
		                            	<p><span data-value="${room.uuid}">
								           <trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
					                   	   </span>
					                   	   <span data-value="${room.nights}"> ${room.nights }</span>*晚
		                                </p>
		                            </td>
		                            <c:choose>
		                            	<c:when test="${fn:length(room.activityHotelGroupMealList)==0}">
		                            		<td></td>
		                            	</c:when>
		                            	<c:otherwise>
			                            	<c:forEach begin="0" end="0" var="mealbase" items="${room.activityHotelGroupMealList}">
			                            	<td class="tc" data-value="${mealbase.hotelMealUuid}">
			                                    <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
			                                </td>
			                             </c:forEach>
		                            	</c:otherwise>
		                            </c:choose>
		                        </c:forEach>
								</c:otherwise>
						</c:choose>
                        <!-- 上岛方式 -->
						<td rowspan="${entry.baseMealNum }" class="tc">
						    <c:set value="${entry.island_way}" var="v_islandway" /> 
						    <c:forEach items="${fn:split(v_islandway,',')}" var="var">
								<p><trekiz:defineDict name="island_way" type="islands_way" defaultValue="${var}" readonly="true" /></p>
							</c:forEach>
						</td>
						<!-- 同行价格 -->
						<td rowspan="${entry.baseMealNum }" class="tr">
							<c:forEach items="${entry.prices }" var="price">
										<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${price.type}" />:
										<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${price.currencyId }" />
										<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${price.price }" /><br />
							</c:forEach>
						</td>
						<!-- 余位/预报名 -->
						<td rowspan="${entry.baseMealNum }" class="tc">
							<span>${entry.remNumber}</span>/<span>${entry.orderNum }</span>
						</td>
						<!-- 单方差 -->
						<td rowspan="${entry.baseMealNum }" class="tr">
							<trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.currency_id}" /> 
							<span class=" fbold">${entry.singlePrice}</span>
						</td>
						<!-- 定金 -->
						<td rowspan="${entry.baseMealNum }" class="tr">
						    <trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.front_money_currency_id}" />
							${entry.front_money}
					    </td>
						<td rowspan="${entry.baseMealNum }" class="tc">
						    <c:if test="${entry.status=='49'||entry.status=='1'}">已上架</c:if> 
						    <c:if test="${entry.status=='50'||entry.status=='2'}">已下架</c:if> 
						    <c:if test="${entry.status=='51'||entry.status=='3'}">草稿箱</c:if> 
						    <c:if test="${entry.status=='52'||entry.status=='4'}">已删除</c:if>
						</td>
						<td rowspan="${entry.baseMealNum }" class="p0">
							<dl class="handle">
								<dt>
									<img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png" />
								</dt>
								<dd>
									<p>
										<span></span>
										<c:if test="${entry.status=='49'||entry.status=='1' }">										
										 <a
											href="${ctx}/hotelOrder/preReportHotelProduct/${entry.uuid}"
											target="_blank">预报名</a></c:if>
											
											
											<a
										
											href="${ctx}/activityHotel/showActivityHotelDetail/${entry.uuid}?type=group"
											target="_blank">详情</a>
									</p>
								</dd>
							</dl>
						</td>
					</tr>
					<c:choose>
						<c:when test="${fn:length(entry.groupRoomList)==0}">
						
						</c:when>
						<c:otherwise>
						<c:forEach begin="0" var="room" items="${entry.groupRoomList}" varStatus="status">
                          <c:choose>
                           	 	<c:when test="${status.index==0}">
                           	 	    <c:forEach begin="1" var="mealbase" items="${room.activityHotelGroupMealList}">
                           	 	    	<tr data-tag="${entry.uuid}">
                                            <td class="tc" data-value="${mealbase.hotelMealUuid}">
                                 	            <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                            </td>
                                            
                                        </tr> 
                                    </c:forEach>
                           	 	</c:when>
                           <c:otherwise>
                           <c:forEach var="mealbase" items="${room.activityHotelGroupMealList}" varStatus="sss">
                     	       <c:choose>
                     	           <c:when test="${sss.index==0 }">
                          	 	       <tr data-tag="${entry.uuid}">
                        	               <td rowspan="${fn:length(room.activityHotelGroupMealList)}" class="tc">
	                         	 		       <p>
	                         	 			   <span data-value="${room.uuid}">
					                           <trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
				                               </span>*<span data-value="${room.nights}"> ${room.nights }</span>晚
	                                           </p>
                                           </td>
                                           <td class="tc" data-value="${mealbase.hotelMealUuid}">
                           		               <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                           </td>
                                           
                          	            </tr>	
                                  </c:when>
                                  <c:otherwise>
                         		      <tr data-tag="${entry.uuid}">
                                          <td class="tc" data-value="${mealbase.hotelMealUuid}">
                                      	     <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                           </td>
                                           
                                      </tr>      
                              	   </c:otherwise>
                           	</c:choose>
                          </c:forEach>    
                          </c:otherwise>
                       </c:choose>
	                </c:forEach>
						</c:otherwise>
					</c:choose>
					
					
				</c:forEach>
			</tbody>
		</table>
		<!--分页部分开始-->
		<div class="pagination clearFix">${pageStr}</div>
		<!--分页部分结束-->
	</div>
</body>
</html>