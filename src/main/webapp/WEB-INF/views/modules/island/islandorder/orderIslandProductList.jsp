<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>报名--海岛游--产品列表</title>
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
//		用launch()替换 new1 new2  for UG_V2 by tlw
		launch();
//		launch_new1();
//		launch_new2();
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
		var status = '${activityStatus}';
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

	function query() {
		$("#searchForm").submit();
	}

	//分页
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
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

	function changelist() {
		window.open("${ctx}/islandOrder/orderIslandList?activityStatus=5&showType=1",'_self');
	}

	// 判定不为空值
	function isNotEmpty(str) {
		if (str != "" && str != "0" && str != null) {
			return true;
		}
		return false;
	}

	//有查询条件的时候，DIV不隐藏
	function showSearchPanel() {
		var activityName = "${activityIslandQuery.activityName }";
		var hotelUuid = "${activityIslandQuery.hotel }";
		var currencyId = "${activityIslandQuery.currencyId}";
		var startPrice = "${activityIslandQuery.startPrice }";
		var endPrice = "${activityIslandQuery.endPrice }";
		var islandUuid = "${activityIslandQuery.island}";

		if (isNotEmpty(activityName) || isNotEmpty(hotelUuid)
				|| isNotEmpty(currencyId) || isNotEmpty(startPrice)
				|| isNotEmpty(endPrice) || isNotEmpty(islandUuid)
				) {
			$('.zksx').click();
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

	//更新海岛游产品状态
	function updateStatus(mess, uuid, status) {
		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				$.post("${ctx}/activityIsland/batchUpdateIslandStatus", {
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
</script>
</head>
<body>
	<page:applyDecorator name="activity_island_order"></page:applyDecorator>
	<%--modified by tlw for去掉面包屑 for UG_V2 AT 20170307--%>
	<%--<div class="mod_nav">报名 &gt; 海岛游列表 &gt; 产品列表</div>--%>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form method="post"
			action="${ctx}/islandOrder/orderIslandList?status=${status}&showType=2"
			id="searchForm" modelAttribute="activityIslandQuery">
			<input id="activityStatus" name="activityStatus"
				value="${activityStatus}" type="hidden" />
			<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden" />
			<input id="pageSize" name="pageSize" value="${pageSize}"
				type="hidden"> <input id="orderBy" name="orderBy"
				value="${orderBy}" type="hidden"> <input id="showType"
					name="showType" value="${showType}" type="hidden">
						<div class="activitylist_bodyer_right_team_co">
							<div class="activitylist_bodyer_right_team_co2 pr">
								<%--<div class="activitylist_team_co3_text">产品名称：</div>--%>
								<input type="text" value="${activityIslandQuery.activityName }"
									class="inputTxt searchInput inputTxtlong" name="activityName"
									id="activityName" flag="istips" placeholder="输入产品名称" />
									<%--<span class="ipt-tips">输入产品名称</span>--%>
							</div>
							<a class="zksx" id="zksx2">筛选</a>
							<div class="form_submit">
								<input type="button" value="搜索" onclick="query(100)"
									class="btn btn-primary ydbz_x" />
							</div>
							
							<div class="ydxbd" id="ydxbd2" style="display:none;">
								<span></span>
							<div class="activitylist_bodyer_right_team_co3">
								<label class="activitylist_team_co3_text">岛屿名称：</label>
								<div class="selectStyle">
									<select name="island" id="island"
										onchange="getAjaxSelect('islandway',this);">
										<option value="" selected="selected">不限</option>
									</select>
								</div>
							</div>
								<div class="activitylist_bodyer_right_team_co3">
									<label class="activitylist_team_co3_text">酒店名称：</label>
									<div class="selectStyle">
										<select name="hotel" id="hotel" onchange="getAjaxSelect('roomtype',this);">
											<option value="" selected="selected">不限</option>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co4">
									<label class="activitylist_team_co3_text">同行价格：</label>
									<div class="selectStyle">
										<form:select path="currencyId">
											<option value="">不限</option>
											<form:options items="${curencyList}" itemValue="id"
												itemLabel="currencyName" />
										</form:select>
									</div>
									<input type="text" id="startPrice" class=" inputTxt"
										name="startPrice" value="${activityIslandQuery.startPrice }"
										onafterpaste="this.value=this.value.replace(/\D/g,'')"
										onkeyup="this.value=this.value.replace(/\D/g,'')"
										maxlength="11" /> <span>至</span> <input type="text"
										id="endPrice" class=" inputTxt" name="endPrice"
										value="${activityIslandQuery.endPrice }"
										onafterpaste="this.value=this.value.replace(/\D/g,'')"
										onkeyup="this.value=this.value.replace(/\D/g,'')"
										maxlength="11" />
								</div>
								
							</div>
							<div class="kong"></div>
						</div>
		</form:form>
		<!--查询结果筛选条件排序开始-->
		<div class="filterbox">
			<div class="filter_num">
				查询结果 <strong>${count }</strong>条
			</div>
			<div class="filter_sort">
				<span>表单排序：</span>
				<c:choose>
					<c:when test="${orderBy=='' || orderBy==null}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort "></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.createDate DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.createDate ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.updateDate DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.updateDate ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='aigl.price DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort  i_sort_down"></i></a>
					</c:when>
					<c:when test="${orderBy=='aigl.price ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort  i_sort_up"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.island_uuid ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.island_uuid DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.island_uuid',this)">岛屿名称<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
				</c:choose>
			</div>
		</div>
		<!--查询结果筛选条件排序结束-->


		<table id="contentTable"
			class="table activitylist_bodyer_table sea_rua_table mainTable">
			<thead>
				<tr>
					<th width="5%">序号</th>
					<th width="13%">产品名称</th>
					<th width="10%">国家</th>
					<th width="10%">岛屿</th>
					<th width="9%">酒店&amp;星级</th>
					<th width="10%">同行价格</th>
					<th width="5%">操作</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${empty page.list}">
					<tr class="toptr">
						<td colspan="7" style="text-align: center;">暂无相关信息</td>
					</tr>
				</c:if>
				<c:forEach items="${page.list }" var="record" varStatus="s">
					<tr>
						<td class="table_borderLeftN">${s.index+1 }</td>
						<td class="tc">${record.activityName}</td>
						<td class="tc"><trekiz:autoId2Name4Table
								tableName="sys_geography" sourceColumnName="uuid"
								srcColumnName="name_cn" value="${record.country}" /></td>
						<td class="tc"><trekiz:autoId2Name4Table tableName="island"
								sourceColumnName="uuid" srcColumnName="island_name"
								value="${record.island_uuid}" /></td>
						<td class="tc"><trekiz:autoId2Name4Table tableName="hotel"
								sourceColumnName="uuid" srcColumnName="name_cn"
								value="${record.hotel_uuid}" /><br /> <span class="y_xing">
								<c:forEach begin="1" end="${record.hotel_star }">
								★
								</c:forEach>
						</span></td>
						<td class="tr"><c:forEach items="${lowPriceList[s.index]}"
								var="variable">
								<p>
									<trekiz:autoId2Name4Table tableName="currency"
										sourceColumnName="currency_id" srcColumnName="currency_mark"
										value="${variable.currency_id}" />
									<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${variable.price}" />/起
								</p>
							</c:forEach></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
								</dt>
								<dd>
									<p>
										<span></span> <a onclick="expand('#child${record.uuid}',this)"
											href="javascript:void(0)" class="">展开</a> <a
											href="${ctx}/activityIsland/showActivityIslandDetail/${record.uuid}?type=product"
											target="_blank">详情</a>
									</p>
								</dd>
							</dl>
						</td>
					</tr>
					<!--二级表格 开始-->
					<tr class="pro_two_table_bg" id="child${record.uuid}"
						style="display:none">
						<td colspan="7">
							 <table style="width:100%" class="hotel_table_openbg subTable product_twolist_table2">
								<thead>
									<tr>
										<th class="tc" width="3%"></th>
										<th class="tc" width="10%">团号/日期</th>
										<th class="tc" width="10%">房型*晚数</th>
										<th class="tc" width="5%">
											<p>基础餐型</p>
										</th>
										<th class="tc" width="5%">上岛方式</th>
										<th class="tc" width="13%">航班 <br /> 起飞到达时间
										</th>
										<th class="tc" width="12%">舱位等级&amp;价格&amp;余位</th>
										<!-- <th class="tr" width="6%">余位/票数总计</th> -->
										<th class="tr" width="6%">预收/预报名</th>
										<th class="tc" width="10%">单房差</th>
										<th class="tc" width="10%">需交订金</th>
										<th class="tc" width="5%">状态</th>
										<th class="tc" width="5%">操作</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${groupList[s.index]}" var="entry"
										varStatus="t">
										<tr id="${entry.uuid}" data-tag="${entry.uuid}">
											<td rowspan="${entry.rowspanNum }" class="table_borderLeftN" hotel-uuid="${entry.hotel_uuid }" activityIsland-uuid="${entry.island_uuid }">
											<input type="checkbox" name="ids" value="${entry.uuid}" />${t.index+1}
											</td>
											<td rowspan="${entry.rowspanNum }" class="tc">
											<a href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date" target="_blank">${entry.groupCode}</a>
												<br /> <span><fmt:formatDate
														value="${entry.groupOpenDate}" pattern="yyyy-MM-dd" /></span></td>
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

											<td rowspan="${entry.rowspanNum }" class="tc"><c:forEach
													items="${fn:split(entry.island_way,';')}" var="var">
													<p>
														<trekiz:defineDict name="island_way" type="islands_way"
															defaultValue="${var}" readonly="true" />
													</p>
												</c:forEach></td>
											
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
											<td rowspan="${entry.rowspanNum }" class="tc"><c:set value="0"
													var="sum" /> <c:forEach items="${entry.spaceMap}"
													var="space">
													<div class="cw_thjg_yw"
														data-kp="${fn:split(space.key,',')[2] }"
														data-fkp="${fn:split(space.key,',')[3] }">
														<p>
															${fns:getDictLabel(fn:split(space.key,',')[0],"spaceGrade_Type",fn:split(space.key,',')[0])}(
															<span class="or_color over_handle_cursor"
																title="控票：${fn:split(space.key,',')[2] } 非控票：${fn:split(space.key,',')[3] }">余位：${fn:split(space.key,',')[1] }</span>)
														</p>
														<c:set
															value="${sum+(fn:split(space.key,',')[2])+(fn:split(space.key,',')[3]) }"
															var="sum" />
														<c:forEach items="${space.value }" var="price">
															<p>
																<%-- <trekiz:defineDict name="traveler_type"
																	type="traveler_type" defaultValue="${price.type }"
																	readonly="true" /> --%>
				                                 <trekiz:autoId2Name4Table tableName="traveler_type" sourceColumnName="uuid" srcColumnName="name" value="${price.type}" />
																: <span data-value="${price.currency_id }"><trekiz:autoId2Name4Table
																		tableName="currency" sourceColumnName="currency_id"
																		srcColumnName="currency_mark"
																		value="${price.currency_id }" /></span> <span>
																		<fmt:formatNumber  type="currency" pattern="#,##0.00" value="${price.price }" />
																		</span>
															</p>
														</c:forEach>
													</div>
												</c:forEach></td>
											<%-- <td rowspan="${entry.rowspanNum }" class="tc"><span
												class="tdred over_handle_cursor">${entry.remNumber }</span>/<span>${sum}</span> --%>
											</td>
											<td rowspan="${entry.rowspanNum }" class="tc"><span
												class="over_handle_cursor" title="${entry.advNumber}">${entry.advNumber}</span>/<span>${entry.total_num==null?0:entry.total_num}</span></td>
											<td rowspan="${entry.rowspanNum }" class="tr"><span
												data-value="${entry.currency_id}"><trekiz:autoId2Name4Table
														tableName="currency" sourceColumnName="currency_id"
														srcColumnName="currency_mark" value="${entry.currency_id}" /></span>
												<span><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${entry.singlePrice}" /></span> <span data-value="/人">/人</span>
											</td>
											<td rowspan="${entry.rowspanNum }" class="tr tdgreen"><span
												data-value="${entry.front_money_currency_id}"><trekiz:autoId2Name4Table
														tableName="currency" sourceColumnName="currency_id"
														srcColumnName="currency_mark"
														value="${entry.front_money_currency_id}" /></span> <span
												class="fbold">${entry.front_money}</span></td>
											<td rowspan="${entry.rowspanNum }" class="tc"><span class="">
													<c:if test="${entry.status=='49'||entry.status=='1'}">已上架</c:if>
							<c:if test="${entry.status=='50'||entry.status=='2'}">已下架</c:if> 
							<c:if test="${entry.status=='51'||entry.status=='3'}">草稿箱</c:if> 
							<c:if test="${entry.status=='52'||entry.status=='4'}">已删除</c:if>
											</span></td>
											<td rowspan="2" class="tl" style="display:none;">${entry.memo}</td>
											<td rowspan="${entry.rowspanNum }" class="p0">
												<dl class="handle">
													<dt>
														<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
													</dt>
													<dd>
														<p>
															<span></span>
															<c:if test="${entry.status=='49'||entry.status=='1'}"><a
																href="${ctx}/islandOrder/toSaveIslandOrder/${entry.uuid}?type=date"
																target="_blank">预报名</a></c:if><a
																href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date"
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
						</td>
					</tr>
					<!--二级表格 结束-->
				</c:forEach>
			</tbody>
		</table>
		<div class="page">
			<div class="pagination">
				<!-- <dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox">全选





						
					</dt>
					<dd>
						<a onclick="batchUpdateGroupStatus('需要将选择的产品下架吗',2)">批量下架</a> <a
							onclick="batchUpdateGroupStatus('删除所有选择的产品吗',4)">批量删除</a>
					</dd>
				</dl> -->
			</div>
		</div>
		<!--分页部分开始-->
		<div class="pagination clearFix">${pageStr}</div>
		<!--分页部分结束-->
		<!--右侧内容部分结束2-->
	</div>
</body>
<script type="text/javascript">
	$(document).ready(
			function() {
				var $team = $("div.activitylist_bodyer_right_team_co_bgcolor");
				$("input:radio[name='filterTable']").on(
						'click',
						function() {
							var $first = $(this).parent().parent().find(
									'input:radio:first');
							if ($first.prop('checked')) {
								$team.eq(0).find('input:radio:eq(0)').prop(
										'checked', true);
								$team.eq(0).show();
								$team.eq(1).hide();
							} else {
								$team.eq(1).find('input:radio:eq(1)').prop(
										'checked', true);
								$team.eq(1).show();
								$team.eq(0).hide();
							}
						})
				//添加房型
				var $trHouseType = $("tr.trHouseType").clone();
				$trHouseType.find('a.addHouseType').replaceWith(
						'<a class="ydbz_x gray delHouseType">删除</a>');
				$(document).on(
						'click',
						'a.addHouseType',
						function() {
							$("table.table_product_info").has(this).find(
									"tr.trHouseType:last").after(
									$trHouseType.clone());
						}).on('click', 'a.delHouseType', function() {
					$(this).parent().parent().remove();
				});
			});
</script>
</html>