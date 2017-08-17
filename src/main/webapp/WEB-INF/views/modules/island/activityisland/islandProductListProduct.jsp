<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
	<title>海岛游--产品列表</title>
<!--[if lte IE 6]>
			<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
			<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
		<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
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
 <script type="text/javascript" src="${ctxStatic}/js/activityIslandProGroupEdit.js"></script>
 
<script type="text/javascript">
	$(function() {
		//搜索条件筛选
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
		//取消一个checkbox 就要联动取消 全选的选中状态
		$("input[name*='children']").click(function(){
			if($(this).attr("checked")){
				
			}else{
				$("input[name='input']").removeAttr("checked");
			}
		});
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
	function confirmBatchIsNull(mess, sta) {
		if (activityIds != "") {
			if (sta == 'off') {
				confirmBatchOff(mess);
			} else if (sta == 'del') {
				confirmBatchDel(mess);
			}
		} else {
			$.jBox.info('未选择产品', '系统提示');
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

	//有查询条件的时候，DIV不隐藏
	function showSearchPanel() {
		var activityName = "${activityIslandQuery.activityName }";
		var currencyId = "${activityIslandQuery.currencyId}";
		var startPrice = "${activityIslandQuery.startPrice }";
		var endPrice = "${activityIslandQuery.endPrice }";
		var island = "${activityIslandQuery.island}";
		var hotel = "${activityIslandQuery.hotel }";
		var hotelstar = "${activityIslandQuery.hotelstar }";
		if (isNotEmpty(activityName) || isNotEmpty(hotel)
				|| isNotEmpty(currencyId) || isNotEmpty(startPrice)
				|| isNotEmpty(endPrice) || isNotEmpty(island)
				|| isNotEmpty(hotelstar)) {
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

	function query() {
		$("#searchForm").submit();
	}

	//列表展示切换
	function changelist() {
		//var status = ${activityStatus};
		//window.open(g_context_url+"/hotelControl/hotelControlList?showType=1&activityStatus="+status,'_self');
		window
				.open(
						"${ctx}/activityIsland/islandProductList?activityStatus=5&showType=1",
						'_self');
	}

	function changePageByStatus(obj) {
		var showType = '${showType}';
		window.open("${ctx}/activityIsland/islandProductList?status="
				+ obj.value + "&showType=" + showType, '_self');
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

	// 判定不为空值
	function isNotEmpty(str) {
		if (str != "" && str != "0" && str != null) {
			return true;
		}
		return false;
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
											"value", n.uuid).attr("title",n.islandName));
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
	function checkedMassage(obj){
//         	if(obj.value==null || obj.value==''){
//         		$.jBox.tip("团号不能为空");
//         		return false;
//         	}
        	$.ajax({
        		type:"post",
        		url:g_context_url+"/activityHotel/checkedGroup",
        		data:{
        			"groupCode":obj.value
        		},
        		success:function(data){
        			if(data.message=="true"){
        				$.jBox.tip("该团号已存在");
        				obj.value="";
        				return false;
        			}
        		}
        	});
        	
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
	function checkall(obj){
		var parent=obj.substring(6);
		if($("#"+obj).attr("checked")){
			$("input[name='children"+parent+"']").each(function(){
				$(this).attr("checked",'true');
			});
			$("#parent"+parent).attr("checked",'true');
		}else{
			$("input[name='children"+parent+"']").each(function(){
				$(this).removeAttr("checked");
			});
			$("#parent"+parent).removeAttr("checked");
		}
	}
	//更新海岛游产品状态
	function updateGroupStatusByActivityUuid(mess, uuid, status,obj) {
		var idArray = [];
		$('input[name*="children"]:checked').each(function() {
			idArray.push($(this).val());
		});
		
		if (idArray == 0) {
			if($(obj).parent().find("a:first").html()=="展开"){
                top.$.jBox.tip("请展开产品,选择团期再进行操作!", 'message');
            }else{
                top.$.jBox.tip("请选择团期再进行操作!", 'message');
            }
			return false;
		}
		
		if(status=='4'){
			for(var i=0;i<idArray.length;i++){
				var hideId = $("#status"+idArray[i]).val();
				if(hideId==4){
					$.jBox.tip("已选中的团期包含已删除状态,不能进行批量删除操作,请重新选择.");
	        		return;
				}
			}
		}
		if(status=='3'){
			for(var i=0;i<idArray.length;i++){
				var hideId = $("#status"+idArray[i]).val();
				if(hideId!=4){
					$.jBox.tip("只允许对删除状态的团期进行批量下架操作,请重新选择.");
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
		if(status=='1'){
			for(var i=0;i<idArray.length;i++){
				var hideId = $("#status"+idArray[i]).val();
				if(hideId==4||hideId==1){
					$.jBox.tip("只允许对下架状态或草稿状态的团期进行批量下架操作,请重新选择.");
	        		return;
				}
			}
		}
	
		/* $.jBox.confirm(mess, "提示", function(v, h, f) {
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
		}); */
		if(status=='3'){
			status='2';
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
	
</script>
</head>
<body>
	<page:applyDecorator name="activity_island"></page:applyDecorator>
	<%--<div class="mod_nav">产品 &gt; 海岛游产品 &gt; 海岛游产品列表</div>--%>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form method="post"
			action="${ctx}/activityIsland/islandProductList?status=${status}&showType=2"
			id="searchForm" modelAttribute="activityIslandQuery">
			<input id="activityStatus" name="activityStatus" value="${status}"
				type="hidden" />
			<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden" />
			<input id="pageSize" name="pageSize" value="${pageSize}"
				type="hidden" />
			<input id="orderBy" name="orderBy" value="${orderBy}" type="hidden" />
			<input id="showType" name="showType" value="${showType}"
				type="hidden" />
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 pr">
					<%--<div class="activitylist_team_co3_text">产品名称：</div>--%>
					<input type="text" value="${activityIslandQuery.activityName }"
						class="inputTxt searchInput inputTxtlong" name="activityName" placeholder="输入产品名称"
						id="activityName" flag="istips" />
					<%--<span class="ipt-tips">输入产品名称</span>--%>
				</div>
				<a class="zksx" id="zksx2">筛选</a>
				<div class="form_submit">
					<%--<button type="submit"  onclick="query(100)"--%>
						<%--class="btn btn-primary ydbz_x" >搜索</button>--%>
					<input type="submit" onclick="query(100)" class="btn btn-primary ydbz_x" value="搜索">
				</div>
				<p class="main-right-topbutt">
					<a class="primary" href="${ctx}/activityIsland/form" target="_blank">发布新产品</a>
				</p>
				<div class="ydxbd" id="ydxbd2" style="display:none;">
					<span></span>
					<div class="activitylist_bodyer_right_team_co4">
						<label class="activitylist_team_co3_text">同行价格：</label>
						<div class="selectStyle">
						<form:select path="currencyId">
							<option value="">不限</option>
							<form:options items="${currencyList}" itemValue="id"
								itemLabel="currencyName" />
						</form:select>
						</div>
						<input type="text" id="startPrice" class=" inputTxt"
							name="startPrice" value="${activityIslandQuery.startPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11" />
						<span>至</span> <input type="text" id="endPrice" class=" inputTxt"
							name="endPrice" value="${activityIslandQuery.endPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11" />
					</div>
					<div class="activitylist_bodyer_right_team_co3">
						<div class="activitylist_team_co3_text">岛屿名称：</div>
						<div class="selectStyle">
							<select name="island" id="island"
								onchange="getAjaxSelect('islandway',this);">
								<option value="" selected="selected">不限</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co3">
						<div class="activitylist_team_co3_text">酒店名称：</div>
						<div class="selectStyle">
							<select name="hotel" id="hotel"
								onchange="getAjaxSelect('roomtype',this);">
								<option value="" selected="selected">不限</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">酒店星级：</label>
						<div class="selectStyle">
						<form:select path="hotelstar">
							<option value="">不限</option>
							<form:options items="${hotelStarList}" itemValue="uuid"
								itemLabel="label" />
						</form:select>
					</div>
				</div>
				<div class="kong"></div>
			</div>
			</div>
		</form:form>
		<!--查询结果筛选条件排序开始-->
		<div class="filterbox"style="margin-bottom: 10px;">
			<c:if test="${canPublish }">
			<%--<div class="filter_btn">--%>
				<%--<a class="btn btn-primary" href="${ctx}/activityIsland/form"--%>
					<%--target="_blank" >发布新产品</a>--%>
			<%--</div>--%>
			</c:if>
			<div class="filter_num">
				查询结果 <strong>${count }</strong>条
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
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort "></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.createDate DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.createDate ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.updateDate DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.updateDate ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='aigl.price DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort  i_sort_down"></i></a>
					</c:when>
					<c:when test="${orderBy=='aigl.price ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort  i_sort_up"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.island_uuid ASC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ai.island_uuid DESC'}">
						<a onclick="cantuansortby('ai.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ai.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('aigl.price',this)">同行价格<i
							class="i_sort"></i></a>
					</c:when>
				</c:choose>
			</div>
		</div>
		<!--查询结果筛选条件排序结束-->


		 <table id="contentTable" class="table activitylist_bodyer_table sea_rua_table mainTable">
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
					<tr id="${record.uuid}">
						<td class="table_borderLeftN" pro_hotel_uuid="${record.hotel_uuid}" islandUuid="${record.island_uuid}" currency="${record.currency_id }">
							${s.index+1 }
						</td>
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
						</span>
						</td>
						<td class="tc"><c:forEach items="${lowPriceList[s.index]}"
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
											href="javascript:void(0)" class="">展开</a> 
										<a href="${ctx}/activityIsland/edit/${record.uuid}" target="_blank">修改</a>
										<a href="javascript:void(0)" onclick="updateGroupStatusByActivityUuid('要上架该产品吗？', '${record.uuid}', 1,this)">上架</a> 
										<a href="javascript:void(0)" onclick="updateGroupStatusByActivityUuid('要下架该产品吗？', '${record.uuid}', 2,this)">下架</a>
										<a href="javascript:void(0)" onclick="updateGroupStatusByActivityUuid('要删除该产品吗？', '${record.uuid}', 4,this)">删除</a>
										<a href="javascript:void(0)" onclick="updateGroupStatusByActivityUuid('要恢复该产品吗？', '${record.uuid}', 3,this)">恢复</a>
										<a href="${ctx}/activityIsland/showActivityIslandDetail/${record.uuid}?type=product" target="_blank">详情</a> 
										<a href="javascript:void(0)" class="addGroup">增加团期</a>
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
										<th class="tc" width="3%">
										<input name="input" id="parent${record.uuid}" type="checkbox" data-group="child${record.uuid}" class="chkAll" onclick="checkall('parent${record.uuid}')"/>
										</th>
										<th class="tc" width="10%">团号/日期</th>
										<th class="tc" width="10%">房型*晚数</th>
										<th class="tc" width="5%">
											<p>基础餐型</p>
										</th>
										<th class="tc" width="5%">上岛方式</th>
										<th class="tc" width="13%">航班 <br /> 起飞到达时间
										</th>
										<th class="tc" width="12%">舱位等级&amp;同行价格&amp;余位</th>
										<!-- <th class="tr" width="6%">余位/票数总计</th> -->
										<th class="tc" width="6%">余位<!-- /票数总计 --></th>
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
											<td rowspan="${entry.rowspanNum }" class="table_borderLeftN" activityIslandGroup-uuid="${entry.uuid}"  
											activityIsland-uuid="${entry.activityIslandUuid }" groupCode="${entry.groupCode}" 
											hotelUuid="${record.hotel_uuid }" islandUuid="${record.island_uuid }" currency="${record.currency_id }">
											<input type="checkbox" name="children${record.uuid}" value="${entry.uuid}" id="children${record.uuid}"/>${t.index+1}
											<input type="hidden" id="status${entry.uuid }" value="${entry.status }" />
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
													var="sum" /> 
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
											<%-- <td rowspan="${entry.rowspanNum }" class="tc"><span
												class="tdred over_handle_cursor">${entry.remNumber }</span>/<span>${sum}</span> --%>
											</td>
											<td rowspan="${entry.rowspanNum }" class="tc"><span class="tdred over_handle_cursor" title="${entry.remNumber}">${entry.remNumber}</span><%-- /<span>${sum }</span> --%></td>
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
												class="fbold"><fmt:formatNumber  type="currency" pattern="#,##0.00" value="${entry.front_money}" /></span></td>
											<td rowspan="${entry.rowspanNum }" class="tc"><span class="">
													<c:if test="${entry.status=='49'}">已上架</c:if> <c:if
														test="${entry.status=='50'}">已下架</c:if> <c:if
														test="${entry.status=='51'}">草稿箱</c:if> <c:if
														test="${entry.status=='52'}">已删除</c:if>
											</span></td>
											<td rowspan="2" class="tl" style="display:none;">${entry.memo}</td>
											<td rowspan="${entry.rowspanNum }" class="p0">
												<dl class="handle">
													<dt>
														<img title="操作" src="${ctxStatic}/images/handle_cz_rebuild.png" />
													</dt>
													<dd>
														<p>
															<span></span>
															<c:if test="${entry.status=='49'}">
																<a class="copyLink" href="javascript:void(0)">复制并新增</a>
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要下架该产品吗？', '${entry.uuid}', 2)">下架</a>
																<a
																	href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date"
																	target="_blank">详情</a>
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
															</c:if>

															<c:if test="${entry.status=='50'}">
																<a class="copyLink" href="javascript:void(0)">复制并新增</a>
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要上架该产品吗？', '${entry.uuid}', 1)">上架</a>
																<a class="updateLink" href="javascript:void(0)">修改</a>
																<a
																	href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date"
																	target="_blank">详情</a>
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
															</c:if>

															<c:if test="${entry.status=='51'}">
																<a class="copyLink" href="javascript:void(0)">复制并新增</a>
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要上架该产品吗？', '${entry.uuid}', 1)">上架</a>
																<a class="updateLink" href="javascript:void(0)">修改</a>
																<a
																	href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date"
																	target="_blank">详情</a>
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
															</c:if>

															<c:if test="${entry.status=='52'}">
																<a href="javascript:void(0)"
																	onclick="updateGroupStatus('要恢复该产品吗?', '${entry.uuid}', 2)">恢复</a>
																<a
																	href="${ctx}/activityIsland/showActivityIslandDetail/${entry.uuid}?type=date"
																	target="_blank">详情</a>
																<a href="javascript:void(0)"
																	onclick="delFlag('${entry.uuid}')">清空</a>
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
						</td>
					</tr>
					<!--二级表格 结束-->
				</c:forEach>
			</tbody>
		</table>
	</div>
		<div class="page">
			<div class="pagination">
				<!-- <dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox" />全选
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
	 <!--新增团期信息项弹出层开始-->
    <div id="jbox_haidaoyou_fab">
        <div class="add_product_info new_hotel_p_table">
            <table class="table_product_info " style="width:900px !important; ">
                <tr>
                    <td class="tr" style="width:121px !important;"><span class="xing">*</span>团号</td>
                    <td width="710" colspan="3"><input type="text" name="NO" class="inputTxt w106 spread" /></td>
                </tr>
                <tr>
                    <td class="tr"><span class="xing">*</span>日期</td>
                    <td colspan="3"><input type="text" onclick="WdatePicker()" class="dateinput w106 required " readonly="readonly" id="tdate"/></td>
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
                    <td class="tr"><span class="xing">*</span>航空公司</td>
                    <td>
                        <select class="w125_sel_pop_addnewtimt selAirline" id="tairlinenumb">
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
                        <select name="select13" class="w50_30 currency">
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
      $(document).ready(function () {//新增
    	  $("#contentTable").on('click', 'a.addGroup', function () {
              var id = $("#contentTable tbody tr").has(this).prop('id');
              var hotelUuid = $("#"+id).find("td:eq(0)").attr("pro_hotel_uuid");
              var islandUuid = $("#"+id).find("td:eq(0)").attr("islandUuid");
              var v_currency = $("#"+id).find("td:eq(0)").attr("currency");
              var options = {
                  isAdd: true,
                  callback: writeRow,
                  id: id,
                  activityislandUuid:id,
                  buttons: { '保存': 0, '提交': 1 },
                  tag:"IslandProductListProduct_addGroup",
                  hotelUuid:hotelUuid,
                  islandUuid:islandUuid,
                  v_currency:v_currency
              };
              addGroup_box(options);
          }).on('click', 'a.copyLink', function () {
              // 复制并新增
              var id = $("#contentTable table.subTable tr").has(this).prop('id');
              var hotelUuid = $("#"+id).find("td:eq(0)").attr("hotelUuid");
              var activityislandUuid = $("#"+id).find("td:eq(0)").attr("activityIsland-uuid");
              var islandUuid = $("#"+id).find("td:eq(0)").attr("islandUuid");
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
                          activityislandUuid:id,
                          data:data,
                          buttons: { '保存': 0, '提交': 1 },
                          tag:"islandProductListProduct_copyLink",
                          hotelUuid:hotelUuid,
                          activityislandUuid:activityislandUuid,
                          islandUuid:islandUuid,
                          v_currency:v_currency
                      };
                      addGroup_box(options);
          		}
          	});
          }).on('click', 'a.updateLink', function (){ // 修改
             
              var id = $("#contentTable table.subTable tr").has(this).prop('id');
              var hotelUuid = $("#"+id).find("td:eq(0)").attr("hotelUuid");
              var activityislandUuid = $("#"+id).find("td:eq(0)").attr("activityisland-uuid");
              var activityislandGroupUuid = $("#"+id).find("td:eq(0)").attr("activityIslandGroup-uuid");
              var islandUuid = $("#"+id).find("td:eq(0)").attr("islandUuid");
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
         					 isAdd: false,
                             id: id,
                             uuid:activityislandGroupUuid,
                             data:data,
                             buttons: { '保存': 0, '提交': 1 },
                             tag:"islandProductListProduct_updateLink",
                             hotelUuid:hotelUuid,
                             activityislandUuid:activityislandUuid,
                             islandUuid:islandUuid,
                             groupCode:groupCode,
                             v_currency:v_currency
                        }; 
                        addGroup_box(options);
          		}
          	});   
              
          }).on('click', 'a.clearLink', function () {
              // 清空
              var id = $("#contentTable table.subTable tr").has(this).prop('id');
              $("#contentTable table.subTable tr[data-tag='" + id + "']").remove();
          }).on('click', 'a.shelveLink', function () {
              // 上架
              updateStatus($("#contentTable table.subTable tr").has(this), 1);
          }).on('click', 'a.unShelveLink, a.recoverLink', function () {
              // 下架 恢复
              updateStatus($("#contentTable table.subTable tr").has(this), 2);
          }).on('click', 'a.delLink', function () {
              // 删除
              updateStatus($("#contentTable table.subTable tr").has(this), 3);
          });
          // 修改状态
          function updateStatus($tr, s) {
              if (s >= 0) {
                  $tr.find("td:eq(-3)").html(status[s].html);
                  $tr.find("td:last dd p").html(status[s].operation);
              }
          }

          var status = (function () {
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

          function readRow(id) {
              var $row = $("#" + id);
              if (!$row.length) return;
			  //alert("islandProductListProduct  readRow");
              var data = {};

              var $tds = $row.find("td");
              var cIndex = 0;
              var $td;
              // 复选框
              cIndex++;
              // 团号
              data.no = $tds.eq(cIndex).find('a').text();
              // 日期
              data.date = $tds.eq(cIndex).find('span').text();
              cIndex++;
              // 房型 * 晚数
              data.houseTypes = [];
              var $tr = $row;
              for (var i = 0; $tr.is("tr[data-tag='" + id + "']") ; i++) {
                  $td = i ? $tr.find("td:first") : $tds.eq(cIndex);
                  var obj = {
                      houseType: { value: $td.find("span:eq(0)").attr("data-value") },
                      night: $td.find("span:eq(1)").text()
                  };
                  obj.baseMealTypes = [];
                  var rowspan = (+$td.prop("rowspan") || 1);
                  for (var j = 0; j < rowspan; j++) {
                      var item = {};
                      if (j) {
                          $tr = $tr.next();
                          $td = $tr.find("td:first");
                      } else {
                          $td = $td.next();
                      }
                      // 基础餐型
                      item.mealType = { value: $td.attr("data-value") };
                      // 升级餐型
                      $td = $td.next();
                      item.upMealTypes = [];
                      $td.find("p").each(function () {
                          item.upMealTypes.push({
                              mealType: { value: $(this).find("span:eq(0)").attr("data-value") },
                              currency: { value: $(this).find("span:eq(1)").attr("data-value") },
                              price: $(this).find("span:eq(2)").text()
                          });
                      });
                      obj.baseMealTypes.push(item);
                  }
                  data.houseTypes.push(obj);
                  $tr = $tr.next();
              }
              cIndex += 2;
              // 上岛方式
              data.trafficWays = [];
              $tds.eq(cIndex).find('p').each(function () {
                  data.trafficWays.push({text:$(this).text(), value:$(this).attr("data-value")});
              });
              cIndex++;
              // 航空公司 航班号
              var arr = ($tds.eq(cIndex).find('span:eq(0)').attr("data-value") || '').split(',');
              data.airline = {
                  value: arr[0],
                  flight: { value: arr[1] }
              };
              // 起飞时间
              data.airline.flight.start = $tds.eq(cIndex).find('span:eq(1)').attr("data-start");
              // 到达时间
              data.airline.flight.end = $tds.eq(cIndex).find('span:eq(1)').attr("data-end");
              // 到达天数
              data.airline.flight.days = ($tds.eq(cIndex).find('span:eq(1)').attr("data-days") || 0);
              cIndex++;
              // 舱位等级&价格&余位
              data.airline.prices = [];
              data.airline.ctrlTickets = [];
              data.airline.unCtrlTickets = [];
              data.airline.usedTickets = [];
              $tds.eq(cIndex).find("div").each(function (i) {
                  var $this = $(this);
                  data.airline.ctrlTickets.push($this.attr("data-ctrl"));
                  data.airline.unCtrlTickets.push($this.attr("data-unCtrl"));
                  data.airline.usedTickets.push($this.attr("data-used"));
                  $this.find("p").each(function (j) {
                     if (i == 0) {
                         data.airline.prices.push({ islandprice: [] });
                     }
                     data.airline.prices[j].islandprice.push({
                         currency: {
                             value: $(this).find("span:eq(0)").attr("data-value"),
                         },
                         price: $(this).find("span:eq(1)").text()
                     });
                  });
              });
              cIndex++;
              // 余位/票数总计
              cIndex++;
              // 优先扣减
              data.ctrlTicketPriority = $tds.eq(cIndex).attr("data-ctrlPriority") == "true";
              // 预收/预报名
              data.predictCount = $tds.eq(cIndex).find('span:eq(0)').text();
              cIndex++;
              // 单房差
              data.priceDiff = {
                  currency: { value: $tds.eq(cIndex).find("span:eq(0)").attr("data-value") },
                  price: $tds.eq(cIndex).find("span:eq(1)").text(),
                  unit: { value: $tds.eq(cIndex).find("span:eq(2)").attr("data-value") }
              };
              cIndex++;
              // 需交订金
              data.deposit = {
                  currency: { value: $tds.eq(cIndex).find('span:eq(0)').attr("data-value") },
                  price: $tds.eq(cIndex).find('span:eq(1)').text()
              };
              cIndex++;
              // 状态
              cIndex++;
              // 备注
              data.comment = $tds.eq(cIndex).text();
              return data;
          }

          function writeRow(data, options, args) {
              var $row = $("#" + options.id);
              //alert("islandProductListProduct  writeRow");
              if (options.isAdd) {
                  options.id = buildID();
                  $row.length || ($row = options.table.find("tbody tr:first"));
                  $row = $row.clone().attr({ "id": options.id, "data-tag": options.id }).show();
                  options.table.find("tbody").append($row);
              }

              $row.nextAll("tr[data-tag='" + options.id + "']").remove();
              var $tds = $row.find("td");

              var $td;

              var cIndex = 0;
              var html = [];
              // 复选框
              cIndex++;
              // 团期
              $tds.eq(cIndex).find('a').text(data.no);
              // 日期
              $tds.eq(cIndex).find('span').text(data.date);
              cIndex++;
              // 房型
              var $tr = $row;
              $.each(data.houseTypes, function (i) {
                  if (i > 0) {
                      $tr.after('<tr><td class="tc"></td><td class="tc"></td><td class="tc" style="display:none;"></td></tr>');
                      $tr = $tr.next();
                      $tr.attr('data-tag', options.id);
                      $td = $tr.find("td:first");
                  } else {
                      $td = $tds.eq(cIndex);
                  }
                  $td.attr("rowspan", this.baseMealTypes.length);
                  html = ['<span data-value="', this.houseType.value, '">', this.houseType.text, '</span>*<span>', this.night, '</span>晚'];
                  $td.html(html.join(''));
                  // 基础餐型
                  $.each(this.baseMealTypes, function (j) {
                      if (j) {
                          $tr.after('<tr><td class="tc"></td><td class="tc" style="display:none;"></td></tr>');
                          $tr = $tr.next();
                          $tr.attr('data-tag', options.id);
                          $td = $tr.find("td:first");
                      } else {
                          $td = $td.next();
                      }
                      $td.text(this.mealType.text).attr("data-value", this.mealType.value);
                      //升级餐型
                      $td = $td.next();
                      html = [];
                      $.each(this.upMealTypes, function () {
                          var $this = $(this);
                          html.push('<p>');
                          html.push('<span data-value="', this.mealType.value, '">', this.mealType.text, '</span>');
                          html.push('<span data-value="', this.currency.value, '">', this.currency.text, '</span>');
                          html.push('<span>', this.price, '</span>/人');
                          html.push('</p>');
                      });
                      $td.html(html.join(''));
                  });
              });
              $tds.not(":eq(" + cIndex + "),:eq(" + (cIndex + 1) + "),:eq(" + (cIndex + 2) + ")").attr("rowspan", $("#contentTable tr[data-tag='" + options.id + "']").length);
              cIndex += 2;
              // 上岛方式
              html = [];
              $.each(data.trafficWays, function () {
                  html.push('<p data-value="',this.value,'">', this.text, '</p>');
              });
              $tds.eq(cIndex).html(html.join(''));
              cIndex++;
              // 航空公司
              html = [];
              if (data.airline.flight.value) {
                  // 航班号
                  html.push('<span data-value="', data.airline.value, ',', data.airline.flight.value, '">', data.airline.flight.text, '</span><br>');
                  // 航班
                  html.push('<span data-start="', data.airline.flight.start, '" data-end="', data.airline.flight.end, '" data-days="', data.airline.flight.days, '" class="lieHt30 fbold">', data.airline.flight.start, '-', data.airline.flight.end, '</span>');
                  if (data.airline.flight.days > 0) {
                      html.push('<span class="lianyun_name next_day_icon">', data.airline.flight.days == 1 ? '次日' : ('+' + data.airline.flight.days), '</span>');
                  }
              }
              $tds.eq(cIndex).html(html.join(''));
              cIndex++;
              // 舱位等级&价格&余位
              $tds.eq(cIndex).empty();
              $.each(data.airline.prices, function (i) {
                  var islandprice = data.airline.prices[i].islandprice;
                  $.each(islandprice, function (j) {
                      html = [];
                      if (i == 0) {
                          $tds.eq(cIndex).append('<div class="cw_thjg_yw" data-ctrl="' + data.airline.ctrlTickets[j] + '" data-unCtrl="' + data.airline.unCtrlTickets[j] + '"></div>');
                          // 舱位等级
                          if (data.airline.spaceGrade[j]) {
                              html.push('<span style="display:block" data-value="',data.airline.spaceGrade[j].value,'">', data.airline.spaceGrade[j].text, '(<span class="or_color over_handle_cursor" title="控票：', data.airline.ctrlTickets[j], ' 非控票：', data.airline.unCtrlTickets[j], '">余位：', data.airline.remainTickets[j], '</span>)</span>');
                          }
                      }
                      var $div = $tds.eq(cIndex).find("div").eq(j);
                      html.push('<p>', data.airline.touristType[i], '：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
                      $div.append(html.join(''));
                  });
              });
              cIndex++;
              //余位/票数总计
              html = ['<span class="tdred over_handle_cursor">', data.airline.amount.remainTicket, '</span>/<span>', data.airline.amount.ticket, '</span>'];
              $tds.eq(cIndex).html(html.join(''));
              cIndex++;
              // 优先扣减
              $tds.eq(cIndex).attr("data-ctrlPriority", data.ctrlTicketPriority);
              // 预收/预报名
              $tds.eq(cIndex).find('span:first').text(data.predictCount);
              cIndex++;
              // 单房差
              html = ['<span data-value="', data.priceDiff.currency.value, '">', data.priceDiff.currency.text, '</span>',
                      '<span>', data.priceDiff.price, '</span>',
                      '<span data-value="', data.priceDiff.unit.value, '">', data.priceDiff.unit.text, '</span>'];
              $tds.eq(cIndex).html(html.join(''));
              cIndex++;
              // 需交定金
              html = ['<span data-value="', data.deposit.currency.value, '">', data.deposit.currency.text, '</span>',
                      '<span class="fbold">', data.deposit.price, '</span>'];
              $tds.eq(cIndex).html(html.join(''));
              cIndex++;
              // 状态
              options.isAdd && updateStatus($row, args.v);
              cIndex++;
              // 备注
              $tds.eq(cIndex).html(data.comment);
               
              return true;
          }
      });
  </script>
</body>
</html>