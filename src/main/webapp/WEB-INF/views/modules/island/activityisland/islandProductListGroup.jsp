<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<title>海岛游--团期列表</title>
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
 <script type="text/javascript">var $ctx = "${ctx}";</script>
<script type="text/javascript" src="${ctxStatic}/js/activityIslandGroup.js"></script>
<script type="text/javascript">
	$(function() {
		//全选
	     $("#allChk").click(function(){
			//所有checkbox跟着全选的checkbox走。
			$('[name=ids]:checkbox').attr("checked", this.checked );
		 });
		 $('[name=ids]:checkbox').click(function(){
			//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
			var $tmp=$('[name=ids]:checkbox');
			//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
			$('#allChk').attr('checked',$tmp.length==$tmp.filter(':checked').length);
		 });
		//搜索条件筛选
		launch_new1();
		launch_new2();
		showSearchPanel();
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
		//产品名称团号切换
		switchNumAndPro();
		//收款确认提醒
		paymentTips();
		//初始化下拉
		getAjaxSelect('island', '80415d01488c4d789494a67b638f8a37',
				"${activityIslandQuery.island}");
		//初始化
		getAirlineAjaxSelect("airline", "", "${activityIslandQuery.airline}");
	});
	//展开筛选按钮
	function launch_new1() {
		$('#zksx1').click(function() {
			if ($('#ydxbd1').is(":hidden") == true) {
				$('#ydxbd1').show();
				$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			} else {
				$('#ydxbd1').hide();
				$(this).text('展开筛选');
				$(this).removeClass('zksx-on');
			}
		});
	}
	function launch_new2() {
		$('#zksx2').click(function() {
			if ($('#ydxbd2').is(":hidden") == true) {
				$('#ydxbd2').show();
				$(this).text('收起筛选');
				$(this).addClass('zksx-on');
			} else {
				$('#ydxbd2').hide();
				$(this).text('展开筛选');
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

	//更新团期状态
	function updateGroupStatus(mess, uuid, status) {
		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				$.post("${ctx}/activityIsland/batchUpdateIslandGroupStatus", {
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
	//批量更新团期状态
	function batchUpdateGroupStatus(mess, status) {
		var idArray = [];
		$('input[name="ids"]:checked').each(function() {
			idArray.push($(this).val());
		});
		if (idArray == 0) {
			top.$.jBox.tip("请选择行记录!", 'message');
			return false;
		}
		if(status=='4'){
			for(var i=0;i<idArray.length;i++){
				var hideId = $("#status"+idArray[i]).val();
				if(status==hideId){
					$.jBox.tip("已选中的团期包含已删除状态,不能进行批量删除操作,请重新选择.");
	        		return;
				}
			}
		}
		if(status=='2'){
			for(var i=0;i<idArray.length;i++){
				var hideId = $("#status"+idArray[i]).val();
				if(hideId!=1){
					$.jBox.tip("只允许对上架状态的团期进行批量下架操作,请重新选择.");
	        		return;
				}
			}
		}
		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				$.post("${ctx}/activityIsland/batchUpdateIslandGroupStatus", {
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
	//有查询条件的时候，DIV不隐藏
	function showSearchPanel() {
		var startGroupDate = "${activityIslandQuery.startGroupDate }";
		var endGroupDate = "${activityIslandQuery.endGroupDate }";
		var currencyId = "${activityIslandQuery.currencyId}";
		var startPrice = "${activityIslandQuery.startPrice }";
		var endPrice = "${activityIslandQuery.endPrice }";
		var acitivityName = "${activityIslandQuery.activityName}";
		var island = "${activityIslandQuery.island}";
		var hotel = "${activityIslandQuery.hotel}";
		var roomtype = "${activityIslandQuery.roomtype}";
		var hotelstar = "${activityIslandQuery.hotelstar}";
		var foodtype = "${activityIslandQuery.foodtype}";
		var islandway = "${activityIslandQuery.islandway}";
		var airline = "${activityIslandQuery.airline}";
		var flightnumber = "${activityIslandQuery.flightnum}";
		var spacelevel = "${activityIslandQuery.spacelevel}";

		if (isNotEmpty(startGroupDate) || isNotEmpty(endGroupDate)
				|| isNotEmpty(currencyId) || isNotEmpty(startPrice)
				|| isNotEmpty(endPrice) || isNotEmpty(acitivityName)
				|| isNotEmpty(island) || isNotEmpty(hotel)
				|| isNotEmpty(roomtype) || isNotEmpty(hotelstar)
				|| isNotEmpty(foodtype) || isNotEmpty(islandway)
				|| isNotEmpty(airline) || isNotEmpty(flightnumber)
				|| isNotEmpty(spacelevel)) {
			$('.zksx').click();
		}
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
		window.open("${ctx}/activityIsland/islandProductList?activityStatus=5&showType=2",'_self');
	}

	function changePageByStatus(obj) {
		var showType = '${showType}';
		window.open("${ctx}/activityIsland/islandProductList?status=" + obj.value + "&showType=" + showType, '_self');
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
							$("#hotel").val("${activityIslandQuery.hotel}");
							getAjaxSelect(tranferObj("hotel"), $("#hotel"),
									tranferValue("hotel"));
						}else{
							$("#hotel").val("${activityIslandQuery.hotel}");
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
			return '${activityIslandQuery.island}';
		} else if (obj == 'island') {
			return '${activityIslandQuery.islandway}';
		} else if (obj == 'hotel') {
			return '${activityIslandQuery.roomtype}';
		} else if (obj == 'roomtype') {
			return '${activityIslandQuery.foodtype}';
		}
		return '';
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
									"activityIslandQuery.airline");
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
									"activityIslandQuery.flightnum");
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
	
	/* $(function(){
     //全选
     $("#allChk").click(function(){
		//所有checkbox跟着全选的checkbox走。
		$('[name=activityId]:checkbox').attr("checked", this.checked );
	 });
	 $('[name=activityId]:checkbox').click(function(){
		//定义一个临时变量，避免重复使用同一个选择器选择页面中的元素，提升程序效率。
		var $tmp=$('[name=activityId]:checkbox');
		//用filter方法筛选出选中的复选框。并直接给CheckedAll赋值。
		$('#allChk').attr('checked',$tmp.length==$tmp.filter(':checked').length);
	 });
}); */
</script>
</head>
<body>
	<page:applyDecorator name="activity_island"></page:applyDecorator>
	<div class="mod_nav">产品 &gt; 海岛游产品 &gt; 海岛游团期列表</div>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form method="post"
			action="${ctx}/activityIsland/islandProductList?status=${status}&showType=1"
			id="searchForm" modelAttribute="activityIslandQuery">
			<input id="activityStatus" name="activityStatus"
				value="${activityStatus}" type="hidden" />
			<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden" />
			<input id="pageSize" name="pageSize" value="${pageSize}"
				type="hidden" />
			<input id="orderBy" name="orderBy" value="${orderBy}" type="hidden" />
			<input id="showType" name="showType" value="${showType}" type="hidden" />
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 wpr20 pr">
					<div class="activitylist_team_co3_text">团号：</div>
					<input type="text" value="${activityIslandQuery.groupCode}"
						class="inputTxt inputTxtlong" name="groupCode" id="groupCode"
						flag="istips" /> <span class="ipt-tips">输入团号</span>
				</div>
				<div class="form_submit">
					<!-- <input type="button" value="搜索" onclick="query(100)"
						class="btn btn-primary ydbz_x" /> -->
						<button type="submit"  onclick="query(100)"
						class="btn btn-primary ydbz_x" >搜索</button>
				</div>
				<a class="zksx" id="zksx1">展开筛选</a>
				<div class="ydxbd" id="ydxbd1" style="display:none;">

					<div class="activitylist_bodyer_right_team_co2">
						<label class="activitylist_team_co3_text">日期：</label> <input
							type="text" id="startGroupDate" class="inputTxt dateinput"
							name="startGroupDate"
							value="${activityIslandQuery.startGroupDate}"
							onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('endGroupDate').value==''){$dp.$('endGroupDate').value=vvv;}},maxDate:'#F{$dp.$D(\'endGroupDate\')}'})"
							readonly="readonly" /> <span> 至 </span> <input type="text"
							id="endGroupDate" class="inputTxt dateinput" name="endGroupDate"
							value="${activityIslandQuery.endGroupDate}"
							onclick="WdatePicker({minDate:'#F{$dp.$D(\'startGroupDate\')}'})"
							readonly="readonly" />
					</div>
					<div class="activitylist_bodyer_right_team_co1"
						style="min-width:460px;">
						<div class="activitylist_team_co3_text">同行价格：</div>
						<form:select path="currencyId">
							<option value="">不限</option>
							<form:options items="${currencyList}" itemValue="id"
								itemLabel="currencyName" />
						</form:select>
						<input type="text" id="startPrice" class=" inputTxt"
							name="startPrice" value="${activityIslandQuery.startPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" /> <span>至</span>
						<input type="text" id="endPrice" class=" inputTxt" name="endPrice"
							value="${activityIslandQuery.endPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" />
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">产品名称：</div>
						<input type="text" value="${activityIslandQuery.activityName }"
							name="activityName" id="activityName" class="inputTxt" />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">岛屿名称：</div>
						<select name="island" id="island"
							onchange="getAjaxSelect('islandway',this);" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">酒店名称：</div>
						<select name="hotel" id="hotel"
							onchange="getAjaxSelect('roomtype',this);" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">酒店星级：</div>
						<form:select path="hotelstar">
							<option value="">不限</option>
							<form:options items="${hotelStarList}" itemValue="uuid"
								itemLabel="label" />
						</form:select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">房型：</div>
						<select name="roomtype" id="roomtype"
							onchange="getAjaxSelect('foodtype',this);" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">基础餐型：</div>
						<select name="foodtype" id="foodtype" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">上岛方式：</div>
						<select class="sel-w1" name="islandway" id="islandway"
							onchange="getAjaxSelect('',this);" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">航空公司：</div>
						<select name="airline" id="airline"
							onchange="getAirlineAjaxSelect('flightnum',this)" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">航班号：</div>
						<select name="flightnum" id="flightnum"
							onchange="getAirlineAjaxSelect('spacelevel',this)" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">舱位等级：</div>
						<select name="spacelevel" id="spacelevel" style="min-width:220px;">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
				</div>
				<div class="kong"></div>
			</div>
		</form:form>
		<!--查询结果筛选条件排序开始-->
		<div class="filterbox">
			<c:if test="${canPublish }">
			<div class="filter_btn">
				<a class="btn btn-primary" href="${ctx}/activityIsland/form"
					target="_blank" >发布新产品</a>
			</div>
			</c:if>
			<div class="filter_num">
				查询结果 <strong>${count}</strong>条
			</div>
			<div class="filter_check">
				<span>团期状态：</span> <label><input type="radio" name="status"
					value="0"
					${status=='0' || status=='' || status==null?'checked':'' }
					onclick="changePageByStatus(this)" />不限</label> <label><input
					type="radio" name="status" value="1" ${status=='1'?'checked':'' }
					onclick="changePageByStatus(this)" />已上架</label> <label><input
					type="radio" name="status" value="2" ${status=='2'?'checked':'' }
					onclick="changePageByStatus(this)" />已下架</label> <label><input
					type="radio" name="status" value="3" ${status=='3'?'checked':'' }
					onclick="changePageByStatus(this)" />保存草稿</label> <label><input
					type="radio" name="status" value="4" ${status=='4'?'checked':'' }
					onclick="changePageByStatus(this)" />已删除</label>
			</div>
			<div class="filter_sort">
				<span>表单排序：</span>
				<c:choose>
					<c:when test="${orderBy=='' || orderBy==null}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort "></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.createDate DESC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.createDate ASC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.updateDate DESC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.updateDate ASC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.sumRemnum DESC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort  i_sort_down"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.sumRemnum ASC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort  i_sort_up"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.groupOpenDate ASC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='u.groupOpenDate DESC'}">
						<a onclick="cantuansortby('u.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('u.groupOpenDate',this)">日期<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('u.sumRemnum',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
				</c:choose>
			</div>
		</div>
		<!--查询结果筛选条件排序结束-->
		<table id="contentTable" class="table activitylist_bodyer_table sea_rua_table">
			<thead>
				<tr>
					<th width="3%">序号</th>
					<th width="10%">团号/日期</th>
					<th width="6%" align="right">产品名称</th>
					<th width="4%">岛屿</th>
					<th width="7%">酒店&amp;星级</th>
					<th width="8%">房型 * 晚数</th>
					<th width="4%">基础餐型</th>
					<th style="display:none;">升级餐型&升餐价格</th>
					<th width="4%">上岛方式</th>
					<th width="5%">航班 <br /> 起飞到达时间 </th>
					<th width="10%">舱位等级&amp;价格&amp;余位</th>
					<!-- <th width="6%">余位/票数总计</th> -->
					<th width="6%">余位</th>
					<th width="5%">预收/预报名</th>
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
						<td colspan="17" style="text-align: center;">暂无相关信息</td>
					</tr>
				</c:if>
				<c:forEach items="${page.list }" var="entry" varStatus="s">
					<tr id="${entry.uuid}" style="display: table-row;" data-tag="${entry.uuid}">
						<td rowspan="${entry.rowspanNum }" class="table_borderLeftN"  hotel-uuid="${entry.hotel_uuid }" activityIsland-uuid="${entry.ai_uuid }" 
						    island_uuid="${entry.island_uuid }" groupCode="${entry.groupCode }" currency="${entry.currency }">
							<input type="checkbox" onclick="" name="ids" value="${entry.uuid}" />${s.index+1}
							<input type="hidden" id="status${entry.uuid }" value="${entry.status }">
							<input type="hidden" id="activityIslandUuid" name="" value="${entry.activityIslandUuid}">
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc"><a href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date" target="_blank">${entry.groupCode}</a><br />
							<span><fmt:formatDate value="${entry.groupOpenDate}" pattern="yyyy-MM-dd" /></span>
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc">${entry.activityName}</td>
						<td rowspan="${entry.rowspanNum }" class="tc">
							<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${entry.island_uuid}" />
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc" data-value="${entry.hotel_uuid}">
							<trekiz:autoId2Name4Table tableName="hotel" sourceColumnName="uuid" srcColumnName="name_cn" value="${entry.hotel_uuid}" />
							<br /><span class="y_xing"><c:forEach begin="1" end="${entry.hotel_star }">★</c:forEach></span>
						</td>
						<!-- 房型 * 晚数 -->
						<c:choose>
							<c:when test="${fn:length(entry.groupRoomList)==0}">
                                         		<td></td><td></td>
                            </c:when>
							<c:otherwise>
								<c:forEach begin="0" end="0" var="room" items="${entry.groupRoomList}">
		                        	<td rowspan="${fn:length(room.activityIslandGroupMealList)==0?1:fn:length(room.activityIslandGroupMealList)}" class="tc">
		                            	<p><span data-value="${room.uuid}">
								           <trekiz:autoId2Name4Class classzName="HotelRoom" sourceProName="uuid" srcProName="roomName" value="${room.hotelRoomUuid }"/>
					                   	   </span>
					                   	   *<span data-value="${room.nights}"> ${room.nights }</span>晚
		                                </p>
		                            </td>
		                            <c:choose>
		                            	<c:when test="${fn:length(room.activityIslandGroupMealList)==0}">
		                            		<td></td>
		                            	</c:when>
		                            	<c:otherwise>
			                            	<c:forEach begin="0" end="0" var="mealbase" items="${room.activityIslandGroupMealList}">
			                            	<td class="tc" data-value="${mealbase.hotelMealUuid}">
			                                    <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
			                                </td>
			                             </c:forEach>
		                            	</c:otherwise>
		                            </c:choose>
		                        </c:forEach>
								</c:otherwise>
						</c:choose>
						<td rowspan="${entry.rowspanNum }" class="tc">
							<c:forEach items="${fn:split(entry.island_way,';')}" var="var">
								<p><trekiz:defineDict name="island_way" type="islands_way" defaultValue="${var}" readonly="true" /></p>
							</c:forEach>
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc">
						<c:if test="${entry.groupAirlines ne null && fn:length(entry.groupAirlines)>0}">
									<span
										data-value="${entry.groupAirlines[0].airline},${entry.groupAirlines[0].flightNumber}">${entry.groupAirlines[0].flightNumber}</span>
									<br />
									<span
										data-start="<fmt:formatDate value="${entry.groupAirlines[0].departureTime}" pattern="HH:mm" />"
										data-end="<fmt:formatDate value="${entry.groupAirlines[0].arriveTime}" pattern="HH:mm" />"
										data-day="${entry.groupAirlines[0].dayNum}" class="lieHt30 fbold">
										<fmt:formatDate value="${entry.groupAirlines[0].departureTime}"
											pattern="HH:mm" />-<fmt:formatDate
											value="${entry.groupAirlines[0].arriveTime}" pattern="HH:mm" />
									</span>
									<span class="lianyun_name next_day_icon">+
										${entry.groupAirlines[0].dayNum}</span>
									<br />
						</c:if>
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc"><c:set value="0" var="sum" /> 
							<c:forEach items="${entry.spaceMap}" var="space">
								<div class="cw_thjg_yw" data-kp="${fn:split(space.key,',')[2] }" data-fkp="${fn:split(space.key,',')[3] }">
									<p>
										${fns:getDictLabel(fn:split(space.key,',')[0],"spaceGrade_Type",fn:split(space.key,',')[0])}(
										<span class="or_color over_handle_cursor" title="控票：${fn:split(space.key,',')[2] } 非控票：${fn:split(space.key,',')[3] }">余位：${fn:split(space.key,',')[1] }</span>)
									</p>
									<c:set value="${sum+(fn:split(space.key,',')[2])+(fn:split(space.key,',')[3]) }" var="sum" />
									<c:forEach items="${space.value }" var="price">
										<p>
											<%-- <trekiz:defineDict name="traveler_type" type="traveler_type" defaultValue="${price.type }" readonly="true" /> --%>
											<trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${price.type}" />
											:
											<span data-value="${price.currency_id }"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${price.currency_id }" /></span>
											<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${price.price }" /></span>
										</p>
									</c:forEach>
								</div>
							</c:forEach>
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc"><span class="tdred over_handle_cursor" title="${entry.remNumber}">${entry.remNumber}</span><%-- /<span>${sum }</span> --%></td>
						<td rowspan="${entry.rowspanNum }" class="tc" data-ctrlPriority="${entry.priorityDeduction }"><span class="over_handle_cursor" title="${entry.advNumber}">${entry.advNumber}</span>/<span>${entry.total_num==null?0:entry.total_num }</span></td>
						<td rowspan="${entry.rowspanNum }" class="tr">
							<span data-value="${entry.currency_id}"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.currency_id}" /></span>
							<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${entry.singlePrice}" /></span>
							<!-- <span data-value="/人">/人</span> -->
							<span data-value="${islandGroup.singlePriceUnit}">
								<c:choose>
									<c:when test="${islandGroup.singlePriceUnit==1}">
									/人
									</c:when>
									<c:when test="${islandGroup.singlePriceUnit==2}">
									/间
									</c:when>
									<c:when test="${islandGroup.singlePriceUnit==3}">
									/晚
									</c:when>
								</c:choose>
							</span>
							
						</td>
						<td rowspan="${entry.rowspanNum }" class="tr tdgreen">
							<span data-value="${entry.front_money_currency_id}"><trekiz:autoId2Name4Table tableName="currency" sourceColumnName="currency_id" srcColumnName="currency_mark" value="${entry.front_money_currency_id}" /></span>
							<span class="fbold"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${entry.front_money}" /></span>
						</td>
						<td rowspan="${entry.rowspanNum }" class="tc">
							<span class="">
							<c:if test="${entry.status=='49'||entry.status=='1'}">已上架</c:if>
							<c:if test="${entry.status=='50'||entry.status=='2'}">已下架</c:if> 
							<c:if test="${entry.status=='51'||entry.status=='3'}">草稿箱</c:if> 
							<c:if test="${entry.status=='52'||entry.status=='4'}">已删除</c:if>
							</span>
						</td>
						<td  rowspan="${entry.rowspanNum }" class="tl" style="display:none;">${entry.memo}</td>
						<td  rowspan="${entry.rowspanNum }" class="p0">
							<dl class="handle">
								<dt>
									<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
								</dt>
								<dd>
									<p>
										<span></span>
										<c:if test="${entry.status=='49'||entry.status=='1'}">
											<a class="copyLink" href="javascript:void(0)">复制并新增</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要下架该产品吗？', '${entry.uuid}', 2)">下架</a>
											<a href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
										</c:if>

										<c:if test="${entry.status=='50'||entry.status=='2'}">
											<a class="copyLink" href="javascript:void(0)">复制并新增</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要上架该产品吗？', '${entry.uuid}', 1)">上架</a>
											<a class="updateLink" href="javascript:void(0)">修改</a>
											<a href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
										</c:if>

										<c:if test="${entry.status=='51'||entry.status=='3'}">
											<a class="copyLink" href="javascript:void(0)">复制并新增</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要上架该产品吗？', '${entry.uuid}', 1)">上架</a>
											<a class="updateLink" href="javascript:void(0)">修改</a>
											<a href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要删除该产品吗?', '${entry.uuid}', 4)">删除</a>
										</c:if>

										<c:if test="${entry.status=='52'||entry.status=='4'}">
											<a href="javascript:void(0)" onclick="updateGroupStatus('要恢复该产品吗?', '${entry.uuid}', 2)">恢复</a>
											<a href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="delFlag('${entry.uuid}')">清空</a>
										</c:if>
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
                           	 	    <c:forEach begin="1" var="mealbase" items="${room.activityIslandGroupMealList}">
                           	 	    	<tr data-tag="${entry.uuid}">
                                            <td class="tc" data-value="${mealbase.hotelMealUuid}">
                                 	            <trekiz:autoId2Name4Class classzName="HotelMeal" sourceProName="uuid" srcProName="mealName" value="${mealbase.hotelMealUuid}"/>
                                            </td>
                                            
                                        </tr> 
                                    </c:forEach>
                           	 	</c:when>
	                           <c:otherwise>
	                           <c:forEach var="mealbase" items="${room.activityIslandGroupMealList}" varStatus="sss">
	                     	       <c:choose>
	                     	           <c:when test="${sss.index==0 }">
	                          	 	       <tr data-tag="${entry.uuid}">
	                        	               <td rowspan="${fn:length(room.activityIslandGroupMealList)}" class="tc">
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
		<div class="page">
			<div class="pagination">
				<dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox" id="allChk"/>全选
					</dt>
					<dd>
						<a onclick="batchUpdateGroupStatus('需要将选择的产品下架吗','2')">批量下架</a> <a
							onclick="batchUpdateGroupStatus('删除所有选择的产品吗','4')">批量删除</a>
					</dd>
				</dl>
			</div>
		</div>
		<!--分页部分开始-->
		<div class="pagination clearFix">${pageStr}</div>
		<!--分页部分结束-->
	</div>
	 <!--新增团期信息项弹出层开始-->
    <div id="jbox_haidaoyou_fab">
        <div class="add_product_info new_hotel_p_table">
            <table class="table_product_info " style="width:900px !important; ">
                <tr>
                    <td class="tr" style="width:121px !important;">团号</td>
                    <td width="710" colspan="3"><input type="text" name="NO" class="inputTxt w106 spread" /></td>
                </tr>
                <tr>
                    <td class="tr">日期</td>
                    <td colspan="3"><input type="text" onclick="WdatePicker()" class="dateinput w106 required " readonly="readonly" /></td>
                </tr>
                <tr class="houseTypeTr">
                    <td class="tr">房型*晚数</td>
                    <td colspan="3">
                        <p class="houseType">
                            <span>
                                <select>
                                    <option>水上屋</option>
                                    <option>沙滩屋</option>
                                </select>
                                <span class="w50_30">*</span>
                                <input type="text" data-type="number" data-min="1" class="inputTxt w50_30" />
                                <span class="w50_30">晚</span> <a class="ydbz_x  addHouseType">新增</a>
                            </span>
                        </p>
                    </td>
                </tr>
                <tr class="houseTypeTr mealTypeTr">
                    <td class="tr">基础餐型</td>
                    <td colspan="3">
                        <select class="w80">
                        	<option value="">不限</option>
                        </select>
                        <a class="ydbz_x addMealType">新增</a> <span class="padr10"></span> <span>
                            <input class="redio_martop_4" type="checkbox" />
                            升级餐型
                        </span>
                    </td>
                    <td class="tr new_hotel_p_table2_tdblue" style="display:none;"><span>升级餐型</span></td>
                    <td style="display:none;">
                        <p class="upMealType">
                            <span>
                                <select name="select10" class="w80 mr3">
	                                 <option value="">不限</option>
	                             </select>
                                <select name="select10" class="w50_30 mr3 currency">
	 								<c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" >
								    		${item.currencyMark}
								    	</option>
									</c:forEach>
                                </select>
                                <input type="text" data-type="float" class="inputTxt w50_30" />
                                <a class="ydbz_x addUpMealType">新增</a>
                            </span>
                        </p>
                    </td>
                </tr>
                <tr class="islandTr">
                    <td class="tr">上岛方式</td>
                    <td width="350">
                        <input class="redio_martop_4" type="checkbox" data-text="水飞" />
                        水飞 <span class="mr25"></span>
                        <input class="redio_martop_4" type="checkbox" data-text="内飞" />
                        内飞 <span class="mr25"></span>
                        <input class="redio_martop_4" type="checkbox" data-text="快艇" />
                        快艇
                    </td>
                    <td width="130" class="tr new_hotel_p_table2_tdblue">单房差</td>
                    <td width="350">
                        <span class="add_jbox_repeat_thj">
                            <select class="w50_30 currency">
								<c:forEach items="${currencyList}" var="item">
								    <option value="${item.id}" >
							    		${item.currencyMark}
							    	</option>
								</c:forEach>
                            </select>
                            <input type="text" data-type="float" class="inputTxt w50_30 babyPrice mr25" />
                            <select class="w50_30 currency">
                                 <option value="1">/人</option>
	                             <option value="2">/间</option>
	                             <option value="3">/晚</option>
                            </select>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="tr">航空公司</td>
                    <td>
                        <select class="w125_sel_pop_addnewtimt selAirline">
                             <option value=''>请选择</option>
                        </select>
                    </td>
                    <td class="tr new_hotel_p_table2_tdblue">航班号</td>
                    <td>
                        <select class="w125_sel_pop_addnewtimt fltNo"></select>
                    </td>
                </tr>
                <tr>
                    <td class="tr new_hotel_p_table2_tdblue">起飞时间</td>
                    <td class="tl">
                        <input type="text" onclick="WdatePicker({ dateFmt: 'HH:mm' })" class="dateinput w106 required startTime" readonly="readonly" />
                    </td>
                    <td class="tr new_hotel_p_table2_tdblue">到达时间 </td>
                    <td><input type="text" onclick="WdatePicker({ dateFmt: 'HH:mm' })" class="dateinput w106 required endTime" readonly="readonly" /> + <input type="text" data-type="number" class="inputTxt w50_30 days" value="0" data-min="0" /> 天</td>
                </tr>
                <tr>
                    <td colspan="4" class="up_load_visa_info_td01 hotel_air_price">
                        <table class="new_hotel_p_table2" style="width:900px !important; display:none;">
                            <thead>
                                <tr>
                                    <th style="-webkit-width:112px;-moz-width:110px;" class="tr new_hotel_p_table2_tdblue2 new_hotel_p_table2_tdblue2_mr20">舱位等级</th>
                                    <th width="237">经济舱</th>
                                    <th width="237">公务舱</th>
                                    <th width="236">头等舱</th>
                                    <th width="31">合计</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${travelerTypes}" var="travelerType">
	                            	 <tr>
	                                    <td width="103" class="tr nnew_hotel_p_table2_tdblue" data-text="${travelerType.name}" data-value="${travelerType.uuid}">
	                                       	 ${travelerType.name}同行<br />（机+酒）价/人
	                                    </td>
	                                    <td width="776" style="text-align:left !important; ">
	                                        <span class="add_jbox_repeat_thj2">
	                                             <select class="w50_30 currency"  name="peoplePrice">
													<c:forEach items="${currencyList}" var="currency">
														    <option value="${currency.id}" >
													    		${currency.currencyMark}
													    	</option>
													</c:forEach>
					                            </select>
	                                            <input type="text" data-type="float" class="inputTxt w50_30 babyPrice mr25" />
	                                        </span>
	                                     </td>
	                                    <td style="display:none"></td>
	                                </tr>
                            	</c:forEach>
                                <tr>
                                    <td class="tr new_hotel_p_table2_tdblue">控票数</td>
                                    <td class="tc" style="text-align:left !important;">
                                        <input type="text" data-type="number" class="inputTxt w50_30 spread fl " value="0" readonly="readonly" />
                                        <a class="fl maring_left10"> <span>选择</span> <span class="new_flight_control">该处需先选择航空公司才可以填写。</span></a>
                                    </td>
                                    <td style="display:none"></td>
                                </tr>
                                <tr>
                                    <td class="tr new_hotel_p_table2_tdblue">非控票数</td>
                                    <td class="tl" style="text-align:left !important; "><input type="text" data-type="number" data-min="0" class="inputTxt w50_30 spread fkpNum" /></td>
                                    <td style="display:none"></td>
                                </tr>
                                <tr style="display:none;">
                                    <td class="tr new_hotel_p_table2_tdblue">票数总计</td>
                                    <td style="text-align:left !important; "></td>
                                    <td style="display:none"></td>
                                </tr>
                                <tr style="display:none;">
                                    <td class="tr new_hotel_p_table2_tdblue">余位</td>
                                    <td style="text-align:left !important; "></td>
                                    <td style="display:none"></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td class="tr">优先扣减</td>
                    <td colspan="3">
                        <span class="mar_left_35">
                            <input class="redio_martop_4" type="radio" name="kp_radio" id="kp_radio" checked="checked" />
                            <label for="kp_radio">控票数</label>
                        </span>
                        <span class="mar_left_35">
                            <input class="redio_martop_4" type="radio" name="kp_radio" id="fkp_radio" />
                            <label for="fkp_radio">非控票数</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="tr">预收</td>
                    <td>
                        <input type="text" data-type="number" data-min="0" class="inputTxt w50_30 spread" />
                        人
                    </td>
                    <td class="tr new_hotel_p_table2_tdblue">需交订金</td>
                    <td>
                        <select name="select13" class="w50_30">
                           <c:forEach items="${currencyList}" var="item">
								    <option value="${item.id}" >
							    		${item.currencyMark}
							    	</option>
							</c:forEach>
                        </select>
                        <input type="text" data-type="float" class="inputTxt w50_30 spread" />
                    </td>
                </tr>
                <tr>
                    <td class="tr valign_top">备注</td>
                    <td colspan="3"><textarea class="inputTxt spread" style=" width:90%; height:100px;"></textarea></td>
                </tr>
            </table>
        </div>
    </div>
    <!--新增团期信息项弹出层结束-->
	<script type="text/javascript">
	$(document).ready(function() {
		$("#contentTable").on('click', 'a.copyLink', function() {
			//复制并新增
			var id = $("#contentTable tbody tr").has(this).prop('id');
			var hotelUuid = $("#"+id).find("td:eq(0)").attr("hotel-uuid");
			var activityislandUuid = $("#"+id).find("td:eq(0)").attr("activityIsland-uuid");
			var islandUuid = $("#"+id).find("td:eq(0)").attr("island_uuid");
			var groupCode = $("#"+id).find("td:eq(0)").attr("groupCode");
			var v_currency = $("#"+id).find("td:eq(0)").attr("currency");
        	$.ajax({
        		type:"post",
        		url:$ctx+"/activityIsland/getActivityIslandGroupJson",
        		data:{
        			"activityIslandGroupUuid":id 
        		},
        		success:function(data){
        		   data = $.parseJSON(data);
       			   var options = {
                        isAdd: true,
                        id: id,
                        data:data,
                        buttons: { '保存': 0, '提交': 1 },
                        tag:"islandProductListGroup_copyLink",
                        hotelUuid:hotelUuid,
                        activityislandUuid:activityislandUuid,
                        islandUuid:islandUuid,
                        v_currency:v_currency
                    };
                    addGroup_box(options);
        		}
        	});   
			
		}).on('click', 'a.updateLink', function() {
				// 修改
				var id = $("#contentTable tbody tr").has(this).prop('id');
				var hotelUuid = $("#"+id).find("td:eq(0)").attr("hotel-uuid");
				var activityislandUuid = $("#"+id).find("td:eq(0)").attr("activityIsland-uuid");
				var islandUuid = $("#"+id).find("td:eq(0)").attr("island_uuid");
				var groupCode = $("#"+id).find("td:eq(0)").attr("groupCode");
				var v_currency = $("#"+id).find("td:eq(0)").attr("currency");
            	$.ajax({
            		type:"post",
            		url:$ctx+"/activityIsland/getActivityIslandGroupJson",
            		data:{"activityIslandGroupUuid":id},
            		success:function(datastr){
            		   datastr = $.parseJSON(datastr);
           			   var options = {
           					   isAdd: true,
           					   uuid: id,
                               data:datastr,
                               buttons: { '保存': 0, '提交': 1 },
                               tag:"islandProductListGroup_updateLink",
                               hotelUuid:hotelUuid,
                               activityislandUuid:activityislandUuid,
                               islandUuid:islandUuid,
                               groupCode:groupCode,
                               v_currency:v_currency
                          }; 
                          addGroup_box(options);
            		}
            	});   
		}).on('click', 'a.clearLink', function() {
			// 清空
			var id = $("#contentTable tbody tr").has(this).prop('id');
			$("#contentTable tbody tr[data-tag='" + id + "']").remove();
			updateSequence();
		}).on('click', 'a.shelveLink', function() {
			// 上架
			updateStatus($("#contentTable tbody tr").has(this), 1);
		}).on('click', 'a.unShelveLink,a.recoverLink', function() {
			// 下架 恢复
			updateStatus($("#contentTable tbody tr").has(this), 2);
		}).on('click', 'a.delLink', function() {
			// 删除
			updateStatus($("#contentTable tbody tr").has(this), 3);
		});
		// 更新序号
		function updateSequence() {
				$("#contentTable tbody tr td:visible").has("input[name='activityId']").each(function(i) {
					$(this).find("input").val(i + 1);
					$(this).find("span").text(i + 1);
				});
			}
			// 修改状态

		function updateStatus($tr, s) {
				if (s >= 0) {
					$tr.find("td:eq(-3)").html(status[s].html);
					$tr.find("td:last dd p").html(status[s].operation);
				}
			}
		
		//var airlinesData = ${airlineInfoAll};
		var status = (function() {
			var copy = '<a class="copyLink" href="javascript:void(0)">复制并新增</a>',
				shelve = '<a class="shelveLink" href="javascript:void(0)">上架</a>',
				update = '<a class="updateLink" href="javascript:void(0)">修改</a>',
				details = '<a class="detailsLink" href="javascript:void(0)">详情</a>',
				del = '<a class="delLink" href="javascript:void(0)">删除</a>',
				unShelve = '<a class="unShelveLink" href="javascript:void(0)">下架</a>',
				recover = '<a class="recoverLink" href="javascript:void(0)">恢复</a>',
				clear = '<a class="clearLink" href="javascript:void(0)">清空</a>',
				span = "<span></span>";
			return [{
				html: '<span>保存草稿</span>',
				operation: [span, copy, shelve, update, details, del].join('')
			}, {
				html: '<span class=" tdgreen">已上架</span>',
				operation: [span, copy, unShelve, details, del].join('')
			}, {
				html: '<span class=" tdred">已下架</span>',
				operation: [span, copy, shelve, update, details, del].join('')
			}, {
				html: '<span>已删除</span>',
				operation: [span, recover, details, clear].join('')
			}]
		})();
	});
</script>
</body>
<script type="text/javascript">
	
</script>
</html>