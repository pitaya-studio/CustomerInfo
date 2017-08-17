<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<title>酒店产品--团期列表</title>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
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
<script type="text/javascript" src="${ctxStatic }/js/hotelProductList.js"></script>
<script type="text/javascript">
  	var $ctx = "${ctx}";
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

	//更新酒店团期状态
	function updateGroupStatus(mess, uuid, status) {
		$.jBox.confirm(mess, "提示", function(v, h, f) {
			if (v == 'ok') {
				if(mess=="要上架该产品吗？"){
					var gcode =	$.trim($("#"+uuid+" >td:eq(1)").find("a").text());
					var gdate =	$.trim($("#"+uuid+" >td:eq(1)").find("span").text());
					if(gcode==""||gdate==""){
						$.jBox.tip("请将团号或日期信息填写完整！");	
				 		return false;
					}else{
						$.post("${ctx}/activityHotel/checkedGroup", {
							"groupCode" : gcode,
						}, function(data) {
							if (data.message == "false") {
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
							} else {
// 								top.$.jBox.tip(data.message, 'warning');
								$.jBox.tip("该团号已存在！！！");
							}
						});
					}
				}else{
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
				}
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
		window.open("${ctx}/activityIsland/islandProductList?activityStatus=5&showType=2",'_self');
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
				$.post("${ctx}/activityHotel/deleteGroup", {
					"uuids" : uuid
				}, function(data) {
					if (data.result == "1") {
						/* $.jBox.prompt("清空成功!", 'success', 'info', {
							closed : function() { */
								$("#searchForm").submit();
							/* }
						}); */
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
			return '${activityHotelQuery.island}';
		} else if (obj == 'island') {
			return '${activityHotelQuery.islandway}';
		} else if (obj == 'hotel') {
			return '${activityHotelQuery.roomtype}';
		} else if (obj == 'roomtype') {
			return '${activityHotelQuery.foodtype}';
		}
		return '';
	}
</script>

<script >

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
						$("#hotel").val("${activityHotelQuery.hotel}");
						getAjaxSelect(tranferObj("hotel"), $("#hotel"),
								tranferValue("hotel"));
					}else{
						$("#hotel").val("${activityHotelQuery.hotel}");
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

		function getLocalTime(nS) {
	       	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ');
		} 
		 //时间戳的转换
		function   formatToDate(now)   {     
            var   year=now.getFullYear(); 
            var   month=now.getMonth()+1; 
            if(month<10){
            	month = "0"+month;
            }
            var   date=now.getDate();  
            if(date<10){
            	date = "0"+date;
            }
            return   year+"-"+month+"-"+date;     
            } 
		//控房使用
		function   formatForControlDate(nowTime)   {  
			var now = new Date(nowTime);
            var year=now.getFullYear(); 
            var month=now.getMonth()+1; 
            var date = now.getDate();
            return year+"-"+month+"-"+date;     
            } 
 		//级联查询
        function getAjaxSelects(type,obj){
        	$.ajax({
        		type: "POST",
        	   	url: "${ctx}/hotelControl/ajaxCheck",
        	   	async: false,
        	   	data: {
        				"type":type,
        				"uuid":$(obj).val()
        			  },
        		dataType: "json",
        	   	success: function(data){
        	   		if(type == "island"){
        	   			$("#islandUuid").empty();
        	   			$("#islandUuid").append("<option value=''>请选择</option>");
        	   			$("#hotelUuid").empty();
        		   		$("#hotelUuid").append("<option value=''>请选择</option>");
        		   		$("#hotelrank").html("");
        	   		} else if(type == "hotel"){
        	   			$("#hotelUuid").empty();
        	   			$("#hotelrank").html("");
        		   		$("#hotelUuid").append("<option value=''>请选择</option>");
        	   		}else if(type =="islandway"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(4).find("td:eq(1)").html('');
        	   		}else if(type=="foodtype"){
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").empty();
        	   			$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append("<option value=''>请选择</option>");
        	   		}else{
        		   		$("#"+type).empty();
        		   		$("#tableappend >tbody").empty();
        	   		}
        	   		if(data){
        	   			if(type=="hotel"){ 
        		   			$.each(data.hotelList,function(i,n){
        		   			     $("#hotelUuid").append($("<option/>").text(n.nameCn).attr("value",n.uuid));
        		   			});
        	   			}else if(type=="roomtype"){
        	   				var occuStr ="";
        	   				
        	   				$.each(data.roomtype,function(i,n){
        	   					
        	   					if(n.occupancyRate!=null && n.occupancyRate!=""){
        	   						
        	   						occuStr = "("+n.occupancyRate+")";
        	   					}else{
        	   						occuStr="";
        	   					}
//         	   					debugger
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(2).find("select:eq(0)").append($("<option/>").text(n.roomName+occuStr).attr("value",n.uuid));
        	   				
        	   				});
        	   			}
//         	   			else if(type=="foodtype"){
        	   				
//         	   				$.each(data.roomMeals,function(i,n){
        	   					
//         	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(1) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   			
//         	   				});
        	   				
//         	   			}
        	   			else if(type=="islandway"){
	        	   			$.each(data.listIslandWay,function(i,n){
	        	   				var islandwayTd = $("#jbox_tq table.table_product_info > tbody > tr").eq(4).find("td:eq(1)");
                     			$(islandwayTd).append($("<input/>").attr("class","redio_martop_4").attr("type","checkbox").attr("data-text",n.label).val(n.uuid));
                     			$(islandwayTd).append(n.label);
                     			$(islandwayTd).append($("<span/>").attr("class","mr25"));
                     			
        	   				});
        	   			}else if(type=="island"){
        	   				$.each(data.islandList,function(i,n){
        	   					 $("#islandUuid").append($("<option/>").text(n.islandName).attr("value",n.uuid));
        	   				});
        	   			}else if(type=="hotelrank"){
        	   				var stsrtnum =""; 
	        	   			for (var i = 0; i < data.hotelrank; i++){
	        	   				stsrtnum +='★';
	        	   			}
	        	   			var html="";
	        	   			$.each(data.delists,function(i,n){
	        	   				if(n.purchaseType==0){
	       	   					    html+=" <tr data-type='內采'>";
	        	   				}else{
	       	   					    html+=" <tr data-type='外采'>";
	        	   				}
	        	   				//n.hotelControlDetailUuid
	        	   				html+=" <td class='tc font_c66 new_hotel_p_table2_tdf' style='display: none'><span data-text='hotelControlDetailUuid' >"+n.hotelControlDetailUuid+"</span></td>";
//        	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+new Date(parseInt(n.inDate)*1000).toLocaleString()+"</td>";
//         	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+n.inDate+"</td>";
//         	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+new Date(parseInt(n.inDate) * 1000).toLocaleString().replace(/:\d{1,2}$/,' ')+"</td>";
        	   						html+=" <td class='tc font_c66 new_hotel_p_table2_tdf'>"+formatForControlDate(n.inDate)+"</td>";
       	   						var roomNameNight="";
       	   						$.each(n.rooms,function(i,n){
       	   							if(n.roomName==null){
//        	   							roomNameNight+=""+"*"+n.night+"晚</t>";
       	   								roomNameNight+="";
       	   							}else{
	       	   							roomNameNight+=n.roomName+"*"+n.night+"晚</br>";
       	   							}
       	   						});
       	   						
       	   						html+="	<td class='tc font_c66 '><p>"+roomNameNight+"</p></td>";
       	   						if(n.hotelMealName==null){
	       	   						html+=" <td class='tc font_c66'><p></p></td>";
       	   						}else{
	       	   						html+=" <td class='tc font_c66'><p>"+n.hotelMealName+"</p></td>";
       	   						}
       	   						if(n.islandWayName==null){
	       	   						html+=" <td class='tc font_c66'></td>";
       	   						}else{
	       	   						html+=" <td class='tc font_c66'>"+n.islandWayName+"</td>";
       	   						}
       	   						html+=" <td class='tc font_c66'>";
       	   						html+=""+n.stock+" </td>";
       	   						if(n.groundSupplier!=null){
	       	   						html+=" <td class='tc font_c66'>"+n.groundSupplier+"</td>";
       	   						}else{
       	   							html+=" <td class='tc font_c66'></td>";
       	   						}
	       	   					if(n.purchaseType==0){
	       	   						html+=" <td class='tc font_c66'>内采</td>";
	        	   				}else{
	       	   						html+=" <td class='tc font_c66'>外采</td>";
	        	   				}
       	   						html+=" <td class='tc'>";
       	   						html+=" <input type='text' data-type='number' data-min='0' class='inputTxt w50_30 spread ' />";
       	   						html+=" </td>";
       	   					    html+=" </tr>";
       	   					});
	        	   			//升餐
        	   				$.each(data.hotelMeals,function(i,n){
        	   					$("#jbox_tq table.table_product_info > tbody > tr").eq(3).find("td:eq(3) select:eq(0)").append($("<option/>").text(n.mealName).attr("value",n.uuid));
        	   				});
        	   				$("#tableappend >tbody").append(html);
	        	   		    $("#hotelrank").html(stsrtnum);
        	   			}else if(type=="travelerTypeRelations"){
        	   				//酒店下绑定的游客类型
        	   				var html="";
        	   				var travelerType = $("#jbox_tq table.table_product_info > tbody > tr").eq(6).find("td:eq(1)");
	        	   			$.each(data.travelerTypeRelations,function(i,n){
								//$(travelerType).append("<div class='hotel_price_same_industry' name='hotelPrice' data-value='"+n.travelerTypeUuid+"'>");
						   		html+="<div class='hotel_price_same_industry' name='hotelPrice' data-value='"+n.travelerTypeUuid+"'>";
						   		html+="<span>"+n.travelerTypeName+"</span>";
						   		html+="<select name='peoplePrice' class='w50_30 currency'>";
					   			$.each(data.currencyList,function(i,m){
					   				html+="<option value='"+m.id+"'>"+m.currencyMark+"</option>";
					   			});
						   		html+="</select>";
						   		html+="<input type='text' data-type='float' value='' name='orderPersonName2' class='inputTxt w50_30 adultPrice mr25' />";
						   		html+="</div>";
	        	   			});
						   	$(travelerType).append(html);
        	   			}
        	   		}
        	   	}
        	});
        }
</script>
</head>
<body>
	<page:applyDecorator name="activity_hotel"></page:applyDecorator>
	<div class="mod_nav">产品 &gt; 酒店产品 &gt; 酒店团期列表</div>
	<div class="activitylist_bodyer_right_team_co_bgcolor">
		<form:form method="post"
			action="${ctx}/activityHotel/activityHotelList?showType=1"
			id="searchForm" modelAttribute="activityHotelQuery">
			<input id="pageNo" name="pageNo" value="${pageNo}" type="hidden" />
			<input id="pageSize" name="pageSize" value="${pageSize}"
				type="hidden" />
			<input id="orderBy" name="orderBy" value="${orderBy}" type="hidden" />
			<input id="showType" name="showType" value="${showType}"
				type="hidden" />
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">团号：</div>
					<input type="text" id="groupCode" name="groupCode"
						value="${activityHotelQuery.groupCode }" />
						<input  id="islandUid" type="hidden"/>
						<input  type="hidden"  id="hotelUid"/>
				</div>
				<div class="form_submit">
						<button type="submit"  onclick="query(100)"
						class="btn btn-primary ydbz_x" >搜索</button>
				</div>
				<a class="zksx" id="zksx2">展开筛选</a>
				<div class="ydxbd" id="ydxbd2" style="display:none;">
				<!-- 根据需求隐藏控房单号 @author zhangchao 2016/01/07 -->
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">控房单号：</div>
					<input type="text" id="activitySerNum" name="activitySerNum"
						value="${activityHotelQuery.activitySerNum }" />
				</div> --%>
					<div class="activitylist_bodyer_right_team_co2 wpr20 pr">
						<div class="activitylist_team_co3_text">产品名称：</div>
						<input type="text" value="${activityHotelQuery.activityName }"
							class="inputTxt inputTxtlong" name="activityName"
							id="activityName" flag="istips" /> <span class="ipt-tips">输入产品名称</span>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">团期状态：</div>
						<select name="status" id="status">
							<option value="0">不限</option>
							<option value="1">已上架</option>
							<option value="2">已下架</option>
							<option value="3">保存草稿</option>
							<option value="4">已删除</option>
						</select>
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">岛屿名称：</div>
						<select name="island" id="island"
							onchange="getAjaxSelect('islandway',this);">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">酒店名称：</div>
						<select name="hotel" id="hotel"
							onchange="getAjaxSelect('roomtype',this);">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">酒店星级：</div>
						<form:select path="hotelstar">
							<option value="">不限</option>
							<form:options items="${hotelStarList}" itemValue="uuid"
								itemLabel="label" />
						</form:select>
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">房型：</div>
						<select name="roomtype" id="roomtype"
							onchange="getAjaxSelect('foodtype',this);">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">基础餐型：</div>
						<select name="foodtype" id="foodtype">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">上岛方式：</div>
						<select class="sel-w1" name="islandway" id="islandway"
							onchange="getAjaxSelect('',this);">
							<option value="" selected="selected">不限</option>
						</select>
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1"
						style="min-width:460px;">
						<div class="activitylist_team_co3_text">同行价格：</div>
						<form:select path="currencyId">
							<option value="">不限</option>
							<form:options items="${currencyList}" itemValue="id"
								itemLabel="currencyName" />
						</form:select>
						<input type="text" id="startPrice" class=" inputTxt"
							name="startPrice" value="${activityHotelQuery.startPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11" />
						<span>至</span> <input type="text" id="endPrice" class=" inputTxt"
							name="endPrice" value="${activityHotelQuery.endPrice }"
							onafterpaste="this.value=this.value.replace(/\D/g,'')"
							onkeyup="this.value=this.value.replace(/\D/g,'')" maxlength="11" />
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co2">
						<label class="activitylist_team_co3_text">日期：</label> <input
							type="text" id="startGroupDate" class="inputTxt dateinput"
							name="startGroupDate"
							value="${activityHotelQuery.startGroupDate}"
							onfocus="WdatePicker({onpicking:function(dp){var vvv=dp.cal.getNewDateStr();if($dp.$('endGroupDate').value==''){$dp.$('endGroupDate').value=vvv;}},maxDate:'#F{$dp.$D(\'endGroupDate\')}'})"
							readonly="readonly" /> <span> 至 </span> <input type="text"
							id="endGroupDate" class="inputTxt dateinput" name="endGroupDate"
							value="${activityHotelQuery.endGroupDate}"
							onclick="WdatePicker({minDate:'#F{$dp.$D(\'startGroupDate\')}'})"
							readonly="readonly" />
					</div>
				</div>
			</div>
		</form:form>
		<!--查询结果筛选条件排序开始-->
		<div class="filterbox">
		<c:if test="${canPublish }">
			<div class="filter_btn">
				<a class="btn btn-primary" href="${ctx}/activityHotel/activityHotelForm"
					target="_blank">发布新产品</a>
			</div>
			</c:if>
			<div class="filter_num">
				查询结果 <strong>${count}</strong>条
			</div>
			<div class="filter_sort">
				<span>表单排序：</span>
				<c:choose>
					<c:when test="${orderBy=='' || orderBy==null}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort "></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.createDate DESC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.createDate ASC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.updateDate DESC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.updateDate ASC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.remNumber DESC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort  i_sort_down"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.remNumber ASC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort  i_sort_up"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.groupOpenDate ASC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort  i_sort_up"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
					<c:when test="${orderBy=='ahg.groupOpenDate DESC'}">
						<a onclick="cantuansortby('ahg.createDate',this)">创建时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.updateDate',this)">更新时间<i
							class="i_sort"></i></a>
						<a onclick="cantuansortby('ahg.groupOpenDate',this)">日期<i
							class="i_sort  i_sort_down"></i></a>
						<a onclick="cantuansortby('ahg.remNumber',this)">余位<i
							class="i_sort"></i></a>
					</c:when>
				</c:choose>
			</div>
		</div>
		<!--查询结果筛选条件排序结束-->
		<table id="contentTable"
			class="table activitylist_bodyer_table sea_rua_table">
			<thead>
				<tr>
					<th width="3%">序号</th>
					<th width="10%">团号/日期</th>
																	<!-- 根据需求隐藏控房单号 @author zhangchao 2016/01/07 -->
					<th width="6%" align="right"><!-- <span style="display:none;">控房单号/</span> -->产品名称</th>
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
						<!-- 产品名称 -->									                   <!-- 根据需求隐藏控房单号 @author zhangchao 2016/01/07 -->
						<td rowspan="${entry.baseMealNum }" class="tc"><%-- <span style="display:none;">${entry.activitySerNum}<br/></span> --%>${entry.activityName}</td>
						<!-- 岛屿 -->
						<td rowspan="${entry.baseMealNum }" class="tc" data-value="${entry.island_uuid}"  data-country="${entry.country }">
							<trekiz:autoId2Name4Table tableName="island" sourceColumnName="uuid" srcColumnName="island_name" value="${entry.island_uuid}" />
						</td>
						<!-- 酒店/星级 -->
						<td rowspan="${entry.baseMealNum }"  class="tc" data-value="${entry.hotel_uuid}"  >
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
					                   	   *<span data-value="${room.nights}">${room.nights }</span>晚
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
						    <c:forEach items="${fn:split(v_islandway,';')}" var="var">
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
									<img title="操作" src="${ctxStatic}/images/handle_cz.png" />
								</dt>
								<dd>
									<p>
										<span></span>
										<c:if test="${entry.status=='49'||entry.status=='1'}">
											<a class="copyLink" href="javascript:void(0)">复制并新增</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要下架该产品吗？', '${entry.uuid}', 2)">下架</a>
											<a href="${ctx}/activityHotel/showActivityHotelDetail/${entry.uuid}?type=group" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
										</c:if>

										<c:if test="${entry.status=='50'||entry.status=='2'}">
											<a class="copyLink" href="javascript:void(0)">复制并新增</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要上架该产品吗？', '${entry.uuid}', 1)">上架</a>
											<a href="${ctx}/activityHotel/showActivityHotelDetail/${entry.uuid}?type=group" target="_blank">详情</a>
											<a class="updateLink" href="javascript:void(0)">修改</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要删除该产品吗？', '${entry.uuid}', 4)">删除</a>
										</c:if>

										<c:if test="${entry.status=='51'||entry.status=='3'}">
											<a class="copyLink" href="javascript:void(0)">复制并新增</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要上架该产品吗？', '${entry.uuid}', 1)">上架</a>
											<a class="updateLink" href="javascript:void(0)">修改</a>
											<a href="${ctx}/activityHotel/showActivityHotelDetail/${entry.uuid}?type=group" target="_blank">详情</a>
											<a href="javascript:void(0)" onclick="updateGroupStatus('要删除该产品吗?', '${entry.uuid}', 4)">删除</a>
										</c:if>

										<c:if test="${entry.status=='52'||entry.status=='4'}">
											<a href="javascript:void(0)" onclick="updateGroupStatus('要恢复该产品吗?', '${entry.uuid}', 2)">恢复</a>
											<a class="updateLink" href="javascript:void(0)">修改</a>
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
		<div class="page">
			<div class="pagination">
				<dl>
					<dt>
						<input name="allChk" onclick="checkall(this)" type="checkbox" />全选
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
		    <!--新增团期信息项弹出层开始-->
    <div id="jbox_hotel_add_product_fab" style="display:none;">
        <div class="add_product_info new_hotel_p_table">
            <table class="table_product_info " style="width:900px !important; " id="productInfoTable">
                <tr>
                    <td width="133" class="tr" style="width:121px !important;"><span class="xing">*</span>团号</td>
                    <td colspan="3"><input type="text" class="inputTxt w106 spread" id="igroupcode"  onblur="checkedMassage(this)"/> <span class="new_flight_control"></span></td>
                </tr>
                <tr>
                    <td class="tr"><span class="xing">*</span>日期</td>
                    <td colspan="3">
                        <input type="text"  name="tdate" onclick="WdatePicker()" class="dateinput w106 required " readonly="readonly" />
<!--                         <a href="产品-酒店产品-优惠信息.html" target="_blank">[价单优惠信息]</a> <span class="padr10"></span>-->
							 <a  name="price_Info" href="产品-酒店产品-价格价单信息.html" target="_blank">[价单价格信息]</a> 
                    </td>
                </tr>
                <tr class="houseTypeTr">
                    <td class="tr" id="dash_line_blue">房型(容住率)*晚数</td>
                    <td colspan="3" id="dash_line_blue">
                        <p class="houseType">
                         	    <span>
                                	<select name="roomtype" id="roomtype" onchange="getAjaxSelects('foodtype',this);">
									<option value="">不限</option>
								</select>
                                <span class="w50_30">*</span>
                                <input type="text" data-type="number" data-min="1" class="inputTxt w50_30" />
                                <span class="w50_30">晚数</span> <a class="ydbz_x  addHouseType">新增</a>
                            </span>
                        </p>
                    </td>
                </tr>
                <tr class="houseTypeTr mealTypeTr">
                    <td class="tr">基础餐型</td>
                    <td colspan="3">
                        <select class="w80" id="foodtype">
                           <option value="">不限</option>
                        </select>
                        <a class="ydbz_x addMealType">新增</a> <span class="padr10"></span> <span>
                            <input class="redio_martop_4" type="checkbox" onselect=""/>
                            升级餐型
                        </span>
                    </td>
                    <td class="tr new_hotel_p_table2_tdblue dash_line_blue" style="display:none;"><span>升级餐型</span></td>
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
                <tr>
                    <td class="tr">上岛方式</td>
                    <td colspan="3">
							
                    </td>
                </tr>
                <tr>
                    <td class="tr">参考航班</td>
                    <td colspan="3">
                        <input type="text" class="inputTxt w106 spread" />
                        <span class="new_flight_control">注：多个航班号请用逗号隔开</span></a>
                    </td>
                </tr>
                <tr>
                    <td class="tr">同行价格/人</td>
                    <td colspan="3">
<!--                     	<c:forEach items="${travelerTypes }" var="traveler"> -->
<!-- 	                        <div class="hotel_price_same_industry" id="hotelPrice" data-value="${traveler.uuid }"> -->
<!-- 	                            <span>${traveler.name }</span> -->
<!-- 	                            <select name="select8" class="w50_30 currency"> -->
<!-- 	                                 <c:forEach items="${currencyList}" var="item"> -->
<!-- 									    <option value="${item.id}" > -->
<!-- 								    		${item.currencyMark} -->
<!-- 								    	</option> -->
<!-- 								     </c:forEach> -->
<!-- 	                            </select> -->
<!-- 	                            <input type="text" data-type="float" value="" name="orderPersonName2" class="inputTxt w50_30 adultPrice mr25" /> -->
<!-- 	                        </div> -->
<!--                     	</c:forEach> -->
                    </td>
                </tr>
                <tr>
                    <td class="tr">控房间数</td>
                    <td >
                        <input type="text" data-type="number" class="inputTxt w50_30 spread fl " value="0" readonly="readonly" />

                        <a class="fl maring_left10 selLink" style="position:relative;">
                                        选择
                            <div class="pop_inner_outer_hotel">
                                <div class="confirm_inner_outer_sel">
                                    <span class="mr25">
                                        <input class="redio_martop_4 procurement" type="checkbox" data-text="內采" checked="checked" />
                                        內采
                                    </span>
                                    <span class="mr25">
                                        <input class="redio_martop_4 procurement" type="checkbox" data-text="外采" />
                                        外采
                                    </span>
                                </div>
                                <table class="table  activitylist_bodyer_table_new" id="tableappend">
                                    <thead>
                                        <tr>
                                 	        <th width="16%" style="display: none">uuid</th>
                            	            <th width="16%">入住日期</th>
                                            <th width="14%">房型&amp;晚数</th>
                                            <th width="10%">基础餐型</th>
                                            <th width="11%">上岛方式</th>
                                            <th width="12%">余位/库存</th>
                                            <th width="16%">地接供应商</th>
                                            <th width="9%">采购类型</th>
                                            <th width="12%">使用库存数</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                      		
                                    </tbody>
                                </table>
                                <div class="btn_confirm_inner_outer"><input type="button" class="btn_confirm_inner_outer02 maring_bottom10 up_load_visa_info_btn_del" value="确定" /></div>
                            </div>
                        </a>
<!--                         <input  name="detailUuid" value=""/> -->
<!--                     	<input  name="detailNum" value=""/> -->
                    </td>
                    <td width="133" class="tr new_hotel_p_table2_tdblue">非控房间数</td>
                    <td><input type="text" data-type="number" class="inputTxt w106 spread" /></td>
                </tr>
                <tr>
                    <td class="tr">优先扣减</td>
                    <td colspan="3">
                        <span class="mar_left_35">
                            <input class="redio_martop_4" type="radio" name="kp_radio" id="kp_radio2" checked="checked" />
                            <label for="kp_radio2">控票数</label>
                        </span> <span class="mar_left_35">
                            <input class="redio_martop_4" type="radio" name="kp_radio" id="fkp_radio2" />
                            <label for="fkp_radio2">非控票数</label>
                        </span>
                    </td>
                </tr>
                <tr>
                    <td class="tr">单房差</td>
                    <td>
                        <span class="add_jbox_repeat_thj">
                            <select name="select9" class="w50_30 currency">
                                  <c:forEach items="${currencyList}" var="item">
									    <option value="${item.id}" >
								    		${item.currencyMark}
								    	</option>
								  </c:forEach>
                            </select>
                            <input type="text" data-type="float" class="inputTxt w50_30 babyPrice mr25" />
                            <select name="select9" class="w60_30 currency">
                                <option>/人</option>
                                <option>/间</option>
                                <option>/晚</option>
                            </select>
                        </span>
                    </td>
                    <td width="133" class="tr new_hotel_p_table2_tdblue">需交订金</td>
                    <td>
                        <select name="select11" class="w50_30">
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
                    <td colspan="3"><textarea name="textarea" class="inputTxt spread" style=" width:90%; height:100px;"></textarea></td>
                </tr>
                 <tr style="display: none">
                    <td class="tr valign_top" >hotelGroupUuid</td>
                    <td colspan="3" >
                    	<input  name="hotelGroupUuid" value=""/>
                    </td>
                </tr>
                <tr style="display: none">
                    <td class="tr valign_top" >hotelUuid</td>
                    <td colspan="3" >
                    	<input  name="hotelUuid" value=""/>
                    </td>
                </tr>
            </table>
        </div>
    </div>
    <!--新增团期信息项弹出层结束-->
	</div>
	 <script type="text/javascript">
        $(document).ready(function () {
            $("#contentTable").on('click', 'a.copyLink', function () {
                // 复制并新增
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var islandway = $("#contentTable tbody tr").has(this).find("td:eq(3)").attr("data-value");	
                var hotel = $("#contentTable tbody tr").has(this).find("td:eq(4)").attr("data-value");	
                var countryVal = $("#contentTable tbody tr").has(this).find("td:eq(3)").attr("data-country");
              	$.ajax({
            		type:"post",
            		url:$ctx+"/activityHotel/getActivityGroupJson",
            		data:{
            			"activityGroupUuid":id 
            		},
            		success:function(data){
            		   data = $.parseJSON(data);
           		       var options = {
    	                    isAdd: true,
//     	                    callback: writeRow,
    	               		id: id,
    	               		tag:"hotelproductgroup_copylink",
    	               		data:data,
    	                 	hotelUuid:hotel,
    	                 	islandwayUuid:islandway,
           	       			buttons: { '保存': 3, '提交': 1 }
           	           }
	                   var aurl = "${ctx }/hotelPl/list?country="+countryVal+"&hotelUuid="+hotel+"&islandUuid="+islandway;
	                   $("[name=price_Info]").attr("href",aurl);
           	           addGroup_box(options);
            		}
            	});
         
            }).on('click', 'a.updateLink', function () {
               
			    // 修改
                var id = $("#contentTable tbody tr").has(this).prop('id');
                var islandway = $("#contentTable tbody tr").has(this).find("td:eq(3)").attr("data-value");
                var hotel = $("#contentTable tbody tr").has(this).find("td:eq(4)").attr("data-value");
                var countryVal = $("#contentTable tbody tr").has(this).find("td:eq(3)").attr("data-country");	
				
              	$.ajax({
            		type:"post",
            		url:$ctx+"/activityHotel/getActivityGroupJson",
            		data:{
            			"activityGroupUuid":id 
            		},
            		success:function(data){
            		   data = $.parseJSON(data);
           		       var options = {
    	                    isAdd: false,
//     	                    callback: writeRow,
    	               		id: id,
    	               		tag:"hotelproductgroup_updatelink",
    	                 	data:data,
    	                 	hotelUuid:hotel,
    	                 	islandwayUuid:islandway,
    	                 	updateg : "updateg"
//            	       	    buttons: { '保存': 3, '提交': 1 }
           	           }
           		       var aurl = "${ctx }/hotelPl/list?country="+countryVal+"&hotelUuid="+hotel+"&islandUuid="+islandway;
	                   $("[name=price_Info]").attr("href",aurl);
           	           addGroup_box(options);
            		}
            	});

            }).on('click', 'a.clearLink', function () {
                // 清空
                var id = $("#contentTable tbody tr").has(this).prop('id');
                $("#contentTable tbody tr[data-tag='" + id + "']").remove();
                setSequence();
            }).on('click', 'a.shelveLink', function () {
                // 上架
                updateStatus($("#contentTable tbody tr").has(this), 1);
            }).on('click', 'a.unShelveLink,a.recoverLink', function () {
                // 下架 恢复
                updateStatus($("#contentTable tbody tr").has(this), 2);
            }).on('click', 'a.delLink', function () {
                // 删除
                updateStatus($("#contentTable tbody tr").has(this), 3);
            });

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

                var data = {};

                var $tds = $row.find("td");
                var cIndex = 0;
                var $td;
                // 序号
                cIndex++;
                // 团号
                data.NO = $tds.eq(cIndex).find('a').text();
                // 日期
                data.date = $tds.eq(cIndex).find('span').text();
                cIndex++;
                // 控房单号/产品名称
                cIndex++;
                // 岛屿
                cIndex++;
                // 酒店&星级
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
                cIndex += 3;
                // 上岛方式
                data.trafficWays = [];
                $tds.eq(cIndex).find('p').each(function () {
                    data.trafficWays.push($(this).text());
                });
                cIndex++;
                // 参考航班
                data.airlines = $tds.eq(cIndex).text();
                cIndex++;
                // 同行价
                data.tradePrices = [];
                $tds.eq(cIndex).find('p').each(function () {
                    var $this = $(this);
                    data.tradePrices.push({
                        currency: { value: $this.find("span:eq(1)").attr("data-value") },
                        price: $this.find("span:eq(2)").text(),
                    });
                });
                cIndex++;
                // 余位/间数/预报名
                data.usedRoom = $tds.eq(cIndex).find("span:eq(1)").text() - $tds.eq(cIndex).find("span:eq(0)").text();
                data.ctrlRoom = $tds.eq(cIndex).find("span:eq(1)").attr("data-ctrlRoom");
                data.unCtrlRoom = $tds.eq(cIndex).find("span:eq(1)").attr("data-unCtrlRoom");
                data.ctrlRoomPriority = $tds.eq(cIndex).find("span:eq(0)").attr("data-ctrlRoomPriority");
                cIndex++;
                // 单房差
                data.priceDiff = {
                    currency: { value: $tds.eq(cIndex).find("span:eq(0)").attr("data-value") },
                    price: $tds.eq(cIndex).find("span:eq(1)").text(),
                    unit: { value: $tds.eq(cIndex).find("span:eq(2)").attr("data-value") }
                };
                cIndex++;
                // 需交定金
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

            function writeRow(data, options) {
                var $row = $("#" + options.id);

                if (options.isAdd) {
                    options.id = buildID();
                    $row.length || ($row = $("#contentTable tbody tr:first"));
                    $row = $row.clone().attr({ "id": options.id, "data-tag": options.id }).show();
                    $("#contentTable tbody").append($row);
                }

                $row.nextAll("tr[data-tag='" + options.id + "']").remove();
                var $tds = $row.find("td");

                var $td;

                var cIndex = 0;
                var html = [];
                // 序号
                setSequence();
                cIndex++;
                // 团期
                $tds.eq(cIndex).find('a').text(data.NO);
                // 日期
                $tds.eq(cIndex).find('span').text(data.date);
                cIndex++;
                // 控房单号/产品名称
                cIndex++;
                // 岛屿
                cIndex++;
                // 酒店&星级
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
                cIndex += 3;
                // 上岛方式
                html = [];
                $.each(data.trafficWays, function () {
                    html.push('<p>', this, '</p>');
                });
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 参考航班
                $tds.eq(cIndex).text(data.airlines);
                cIndex++;
                // 同行价
                html = [];
                $.each(data.tradePrices, function () {
                    html.push('<p><span>', this.type, '</span>：<span data-value="', this.currency.value, '">', this.currency.text, '</span><span>', this.price, '</span></p>');
                });
                $tds.eq(cIndex).html(html.join(''));
                cIndex++;
                // 余位/间数/预报名
                data.usedRoom = (data.usedRoom || 0);
                html = ['<span class="tdred over_handle_cursor" data-ctrlRoomPriority="', data.ctrlRoomPriority, '" ',
                        'title="剩余控房：', data.ctrlRoom, '间', (data.ctrlRoomPriority ? '（优先扣减）' : ''), '+剩余非控房：', data.unCtrlRoom, '间', (!data.ctrlRoomPriority ? '（优先扣减）' : ''), '">', (data.ctrlRoom + data.unCtrlRoom - data.usedRoom), '</span>',
                        '/<span data-ctrlRoom="', data.ctrlRoom, '" data-unCtrlRoom="', data.unCtrlRoom, '">', (data.ctrlRoom + data.unCtrlRoom), '</span>/200'];
                $tds.eq(cIndex).html(html.join(''));
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

            // 设置列序列
            function setSequence() {
                $("#contentTable tbody tr td:first-child span.sequence").each(function (i) {
                    $(this).text(i);
                });
            }

            // 修改状态
            function updateStatus($tr, s) {
                if (s >= 0) {
                    $tr.find("td:eq(-3)").html(status[s].html);
                    $tr.find("td:last dd p").html(status[s].operation);
                }
            }
        });
    </script>
</body>
</html>