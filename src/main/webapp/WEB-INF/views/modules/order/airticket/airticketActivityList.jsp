<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page import="com.trekiz.admin.common.config.Context" language="java"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta name="decorator" content="wholesaler" />
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<%@ include file="/WEB-INF/views/include/commonVar.jsp"%>
<style type='text/css'>
.ui-front {
	z-index: 2100;
}
.ico-remarks-td {
	height: 16px;
	width: 31px;
	background-image: url("${ctxStatic}/images/ico-remarks-td.png");
	background-repeat: no-repeat;
	position: absolute;
	top: -1px;
	left: 0px;
	cursor: pointer;
}
</style>
<!-- <script type="text/javascript" src="${ctxStatic}/js/common.js"></script> --%>
<!-- <script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script> -->
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js"
	type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script type="text/javascript"
	src="${ctxStatic}/modules/order/airticket/airticketActivityList.js"></script>

<title>预定-机票</title>
<script type="text/javascript">
	var yuejianxingzong = '7a81b21a77a811e5bc1e000c29cf2586';  // 越柬行踪 uuid
//增加渠道商链接
function addAgentinfo(){
	window.open(contextPath+"/agent/manager/firstForm");
	//移除弹窗（选择渠道和付款方式）
	window.parent.window.jBox.close();
}
$(function(){
g_context_url = "${ctx}";
	//展开筛选
	launch();
	//文本框提示信息显示隐藏
	inputTips();
	//操作浮窗
	operateHandler();
	//如果展开部分有查询条件的话，默认展开，否则收起
	validExpand();
	
	//可输入select
	$(".selectinput" ).comboboxSingle();
	$("#activityCreate" ).comboboxInquiry();
	//renderSelects(selectQuery());
	var _$orderBy = $("#orderBy").val();
	if(_$orderBy==""){
		_$orderBy="startingDate DESC";
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
              if ($(this).hasClass("li"+orderBy[0])){
                  orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
                  $(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
                  $(this).attr("class","activitylist_paixu_moren");
              }
          });
    
	
	//selectairline($('#trafficName').val(),$('#trafficName'));
	
	// 如果批发商是诚品旅游，则默认打开全部团期
    //var compayUuid = $("#companyUuid").val();
    //if (compayUuid == "ed88f3507ba0422b859e6d7e62161b00") {
    //	$("a[id^=close]").each(function(index, obj) {
    //   	$(this).trigger("click");
    //    });
    //}
	
});
function selectQuery(){
	$("#activityCreate" ).comboboxInquiry();
}
//选择渠道、付款方式、占位方式弹出框
function agentType(objId,payMode_full,payMode_op,payMode_cw,payMode_deposit,payMode_advance){

	var agentId;
    var options = [];//渠道
    $.ajax({
		type : "POST",
		url : g_context_url + "/order/airticket/agentList",
		dataType : "json",
		async : false,
		success:function(msg) {
			if (msg && msg.agentList) {
				$.each(msg.agentList,function(i,n) {
					if (i == 0) {
						agentId = n.id;
					}
					options.push('<option value="' + n.id + '">' + n.agentName + '</option>	');
				});
			}
		}
	});
	var pay_options = "";//付款方式	
	if(payMode_full==1)
		pay_options += '<option value="3">全款</option>';
	if(payMode_op==1)
		pay_options += '<option value="7">计调确认占位</option>';
	if(payMode_cw==1)
		pay_options += '<option value="8">财务确认占位</option>';
	if(payMode_deposit==1)
		pay_options += '<option value="1">订金占位</option>';
	if(payMode_advance==1)
		pay_options += '<option value="2">预占位</option>';

	var html = '<div class="jbox_type">';
	//添加渠道商
	var addAgentinfoHtml = '';
	var isAddAgent = $('#isAddAgent').val();
    if(isAddAgent == 1){
    	addAgentinfoHtml = '<input class="btn btn-primary" type="button" onclick="addAgentinfo()" value="新增渠道" style="width:100px;height:30px;margin-left:20px">'
    }
	html += '<p><label><span class="xing">*</span>渠道：</label>';
	html += '<select id="agentId" name="agentId" style="display: inline-block;" class="typeSelected" onchange="agentChange('+objId+'),getSalerByAgentId(this)">';
	<c:if test="${fns:getUser().company.id ne 68}">
		agentId = -1;                                                                                                                                                                                                                          //针对越柬行踪 315需求 将非签约渠道改为直客
		html += '<option value="-1"><c:choose><c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586'}">未签</c:when><c:otherwise><c:if test="${companyUuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">直客</c:if><c:if test="${companyUuid ne  '7a81b21a77a811e5bc1e000c29cf2586'}">非签约渠道</c:if></c:otherwise></c:choose></option>';
	</c:if>
	html += options.join("");
	html += '</select>';
	html += addAgentinfoHtml;
	html += '</p>';	
	html += getSalerByAgentId($("#agentId"));
	html += '<p><label>订单方式：</label>';
	html += '<select id="placeHolderType" name="placeHolderType" style="display: inline-block;" class="typeSelected" disabled="disabled">';
	html +='<option value="1">切位订单</option><option value="0" selected="selected">占位订单</option></select></p>';
	html += '<p><label>付款方式：</label>';
	html += '<select name="payType" style="display: inline-block;" class="typeSelected">';
	html += pay_options;
	html += '</select></div>';
	$.jBox(html, { title: "预订-选择付款方式",buttons:{
		'预定':1
	},submit: function (v, h, f) {
		var placeHolderType;
		if(f.agentId=="-1"){
			placeHolderType=0;
		}else{
			placeHolderType=$("#placeHolderType").val();
		}
		
	    doNext(objId, f.agentId,placeHolderType, f.payType);
	},height:350,width:700});
	
	//防止切位不显示
	$("#agentId").trigger("change");
	$("#agentId").addClass("ui-front");
	$("#agentId").comboboxInquiry().on('comboboxinquiryselect', function(event, obj){
		getSalerByAgentId(this);
		agentChange(objId);
	});
	// 渠道选择下拉框的可输入框中，失去焦点事件：1.根据渠道获取销售 2.回显非签约渠道（由于控件使用特性，在粘贴前是否失去焦点，直接影响粘贴渠道全称能否正确被控件识别出粘贴内容是属于下拉数据的某一条。如果未被识别，则渠道依旧是非签约渠道）
	$("#agentId").next().find("input").blur(function(){
		if($("#agentId").val() == '-1'){
			if($("#companyUuid").val() == yuejianxingzong){					
				$("#agentId").next().find("input").val("直客");
			} else {					
				$("#agentId").next().find("input").val("非签约渠道");
			}
		}
		getSalerByAgentId($("#agentId"));
	});
}

/**
 * 获取渠道跟进销售
 * @param obj
 */
function getSalerByAgentId(obj) {
	var salerHtml = '<p><label>销售：</label>';
	if ($("#salerId").length > 0) {
		$("#salerId").empty();
		$.ajax({
	        async: false, 
	        type : "POST",
	        url : g_context_url + "/orderCommon/manage/getSalerByAgentId",
	        data : {
	            agentId : $(obj).val()
	        },
	        success : function(msg) {
	        	var jsonLength = 0;
	            for (var prop in msg) {
	            	if (prop == 'loginUserId' || prop == 'userNum') {
	            		continue;
	            	}
	            	jsonLength++;
	            	if (msg.loginUserId == prop) {
	            		$("#salerId").append('<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>');
	            	} else {
	            		$("#salerId").append('<option value=' + prop + '>' + msg[prop] + '</option>');
	            	}
	            }
	        if(jsonLength == 1 && $(obj).val() =='-1' ){
	       		 $("#salerId").attr("disabled","disabled"); 
	        }
	        }
	    });
	} else {
		$.ajax({
	        async: false,
	        type : "POST",
	        url : g_context_url + "/orderCommon/manage/getSalerByAgentId",
	        data : {
	            agentId : $(obj).val()
	        },
	        success : function(msg) {
	            var jsonLength = 0;
	            var tempStr = '';
	            for (var prop in msg) {
	            	if (prop == 'loginUserId' || prop == 'userNum') {
	            		continue;
	            	}
	            	jsonLength++;
	            	if (msg.loginUserId == prop) {
	            		tempStr += '<option value=' + prop + ' selected="selected">' + msg[prop] + '</option>';
	            	} else {
	            		tempStr += '<option value=' + prop + '>' + msg[prop] + '</option>';
	            	}
	            }
	            if(jsonLength == 1 && $(obj).val() =='-1'){
	            	salerHtml += '<select id="salerId" disabled="disabled">'+tempStr;
	            }else{
	             	salerHtml += '<select id="salerId">'+tempStr;
	            }
	        }
	    });
	}
	salerHtml += '</select>';
    return salerHtml;
}

function doNext(activityId, agentId,placeHolderType, payType){
    $.ajax({
		  type: "POST",
		  url: g_context_url + "/order/airticket/getPosition?activityId=" + activityId+"&placeHolderType=" + placeHolderType + "&agentId=" + agentId,
		  dataType: "json",
		  async: false,
		  success:function(msg){
		      if(msg){
		         var freePosition = 0;
		         if(msg.freePosition){
		            freePosition = msg.freePosition;
		         }
		         if(freePosition == 0){
		            top.$.jBox.tip("该产品的占位方式已无余位，请选择其他产品预订!", 'error');
		            return;
		         }
		         if(typeof(agentId) == "undefined"){
		        	 top.$.jBox.tip("请配置渠道商!", 'error');
			         return;
		         }
		         document.location = g_context_url + "/order/airticket/createOrder?agentId=" + agentId + "&activityId=" 
		                          + activityId + "&payType=" + payType + "&placeHolderType=" + placeHolderType + "&salerId=" + $("#salerId").val();
		      
		      }
		  }
		});
}

function agentChange(objId){
		agentSelectId=$("#agentId").val();
		if(agentSelectId==-1){
		 $("#placeHolderType").attr("disabled","disabled");
			               $("#placeHolderType").val("0");
		   return;
		}else{
		   $.ajax({
			  type: "POST",
			  url: g_context_url + "/order/airticket/getReservePosition?activityId=" + objId+ "&agentId=" + agentSelectId,
			  dataType: "json",
			  async: false,
			  success:function(msg){
			     if(msg){
			           var freePosition = 0;
			         if(msg.freePosition){
			            freePosition = msg.freePosition;
			             if(msg.feePosition==0){
			             $("#placeHolderType").attr("disabled","disabled");
			             $("#placeHolderType").val("0");
			          }else{
			            $("#placeHolderType").removeAttr("disabled");
			          }
			         }else{
			               $("#placeHolderType").attr("disabled","disabled");
			               $("#placeHolderType").val("0");
			         }
			         
			     }
			       
			      }
	
			});
		}
}

//航空公司级联
function selectairline(airlineCode,obj){
   $.ajax( {
		type : "POST",
		url :"${ctx}/airTicket/getspaceLevelList.htm",
		data : {
			airlineCode : airlineCode
		},
		success : function(msg) {
			var dataObj = eval('(' + msg + ')'); 
			$("#spaceGrade").empty();  
            $("#spaceGrade").append("<option value=''>不限</option>");  
            $.each(dataObj, function(key, value) {  
                $("#spaceGrade").append("<option value=" + key + ">" + value+ "</option>");  
            });  
			
		}
	}); 
	
}

//选择订单类型弹出框
function orderType(activityId, agentId, payType){
	var html = '<div class="add_allactivity">';
	html += '<label>订单方式：</label>';
	html += '<input type="radio" name="placeHolderType" value="1" checked="checked">切位订单</input>';
	html += '<input type="radio" name="placeHolderType" value="0">占位订单</input>';
	html += '</div>';
	$.jBox(html, { title: "订单方式选择",buttons:{
		'预定':1
	},submit: function (v, h, f) {
		//点击预定后，进行的操作放这里
		document.location = g_context_url + "/order/airticket/createOrder?agentId=" + agentId + "&activityId=" + activityId + "&payType=" + payType + "&placeHolderType=" + f.placeHolderType;
	},height:175});
}

function viewActivityDetail(activityId){
   window.open(g_context_url + "/airTicket/actityAirTickettail/" + activityId);
}


function search(type) {
	if (type == 1) {
		$("#searchForm").attr("action", g_context_url + "/order/airticket/activityList").submit();
	} else {
		window.open(g_context_url + "/order/airticket/downloadAllYw" + "?" + $('#searchForm').serialize());
		$("#searchForm").attr("action", g_context_url + "/order/airticket/activityList").submit();
	}
}

/**
 * 下载团期余位
 * @param activityId
 */
function downloadYw(activityId) {
	$("#activityId").val(activityId);
	$("#exportForm").submit();
}

//如果展开部分有查询条件的话，默认展开，否则收起	
function validExpand(){
	
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='groupCodeOrActSer']");	
	var searchFormselect = $("#searchForm").find("select[id!='orderShowType']");
	var inputRequest = false;
	var selectRequest = false;
	for(var i = 0; i<searchFormInput.length; i++) {
		if($(searchFormInput[i]).val() != "" && $(searchFormInput[i]).val() != null) {
			inputRequest = true;
		}
	}
	for(var i = 0; i<searchFormselect.length; i++) {
		if($(searchFormselect[i]).children("option:selected").val() != "" && 
				$(searchFormselect[i]).children("option:selected").val() != null &&
				$(searchFormselect[i]).children("option:selected").val() != 0) {
			selectRequest = true;
		}
	}
	if($("#orderShowType").length > 0 && $("#orderShowType").children("option:selected").val() != "0") {
		selectRequest = true;
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}	
}


			var groupIds="";	
		//点击某一团期选择
		function idcheckchg(obj){
			var value = $(obj).val();
			var arr = groupIds.split(",");
			if($(obj).attr("checked")){
				if(arr.indexOf(value) < 0){
					groupIds = groupIds+value+",";
				}
			}			
			else{
				if($("input[name='allChk']").attr("checked"))
					$("input[name='allChk']").removeAttr("checked");
				if(arr.indexOf(value) >= 0){					
					groupIds = groupIds.replace(value+",","");
				}
			}
			$("#groupIds").val(groupIds);				
		}
		//点击某一团选择
		function acidcheckchg(obj, count){
			var acId = $(obj).val();
			var grId = "";
			if($(obj).attr("checked")){
				$(obj).parents("tr[id=parent" + count + "]").next().find("input[name=groupid]").attr("checked", true);
			} else {
				$(obj).parents("tr[id=parent" + count + "]").next().find("input[name=groupid]").attr("checked", false);
			}
			t_allchk();
		}
		//全选 add by jyang
	   function checkall(obj){
			if($(obj).attr("checked")){
				$("input[name='allChk']").attr("checked",'true');
				$("input[name='groupid']").attr("checked",'true');
				$("input[name='groupid']:checked").each(function(i,a){
					var arr = groupIds.split(",");
					if(arr.indexOf(a.value) < 0){
						groupIds = groupIds + a.value+",";
					}
  				});
			}
			else{
				$("input[name='allChk']").removeAttr("checked");
				$("input[name='groupid']").removeAttr("checked");
				$("input[name='groupid']").each(function(i,a){
					var arr = groupIds.split(",");
					if(arr.indexOf(a.value) >= 0){					
						groupIds = groupIds.replace(a.value+",","");
					}
  				});
			}
			$("#groupIds").val(groupIds);
		}
		// 反选
		function checkallReverse(obj){
			$("input[name=groupid]").each(function(){
				$(this).attr("checked",!$(this).attr("checked"));				
			});
			var ids = "";
			$("input[name=groupid]").each(function(i,a){
				var arr = ids.split(",");
				if($(this).attr("checked") == "checked"){
					if(arr.indexOf(a.value) < 0){
						ids = ids + a.value+",";
					}					
				} else {
					if(arr.indexOf(a.value) >= 0){
						ids = ids.replace(a.value+",","");
					}
				}
			});
			$("#groupIds").val(ids);
			t_allchk();
		}
		// 如变成全选，则需要勾选全选框
		function t_allchk() {
			var chknum = $("input[name='groupid']").size();
			var chk = 0;
			$("input[name=groupid]").each(function() {
				if ($(this).attr("checked") == "checked") {
					chk++;
				}
			});
			if (chknum == chk) {//全选 
				$("input[name='allChk']").attr("checked", true);
			} else {//不全选 
				$("input[name='allChk']").attr("checked", false);
			}
		}
		
		//部分下载余位表
function partsOfYWDownload2() {
	var groupIds = "";
	$("input[name=groupid]:checked").each(function(index, obj) {
		groupIds += $(this).val() + ",";
	});
	if (groupIds == "") {
		alert('请选择团期','error');
        return false;	
	} else {
		$("#paramGroupIds").val(groupIds);
		$("#partsForm").submit();
	}
}

/**
 * 点击显示"销售明细"详情列表
 * @param 团期ID
 */
function showOrderPay(orderId, obj){
	// 获取团期ID
	var groupID = $(obj).parents("tr").find("input[name=groupid]").val();
	console.log(groupID);
	if(groupID){
		// 根据团期ID，获取订单列表详情
		$.ajax({
			type:"POST",
			url:"../../order/airticket/airticketInfo",
			data: {
				ticketGroupID : groupID
			},
			dataType: 'json',
			success: function(msg){
				if(msg.res=="success"){
					// 将团期详情写入下拉列表中
					var json = msg.activityInfoList;
				    var sbrtr = $(obj).parents("tr").next().next();
					var body = sbrtr.find("tbody");
					body.empty();
					var indexs = 0;
					for(var o in json){
					if (indexs < json.length) {
						var indexs = parseInt(o)+1;
						body.append("<tr>");
						body.append('<td class="tc">'+indexs+'</td>');
						if('${orderNo}'){
							body.append('<td class="tc">'+json[o].orderNo+'</td>');
						}
						if('${agentName}'){
							body.append('<td class="tc">'+json[o].agentName+'</td>');
						}
						if('${shell}'){
							body.append('<td class="tc">'+json[o].shell+'</td>');
						}
						if('${typeName}'){
							body.append('<td class="tc">'+json[o].typeName+'</td>');
						}
						if('${orderUser}'){
							body.append('<td class="tc">'+json[o].orderUser+'</td>');
						}
						if('${reserveDate}'){
							body.append('<td class="tc">'+json[o].reserveDate+'</td>');
						}
						if('${airTypeName}'){
							body.append('<td class="tc">'+json[o].airTypeName+'</td>');
						}
						if('${personNum}'){
							body.append('<td class="tc">'+json[o].personNum+'</td>');
						}
						if('${orderStatus}'){
							body.append('<td class="tc">'+json[o].orderStatus+'</td>');
						}
						if('${totalAmount}'){
							body.append('<td class="tc">'+json[o].totalAmount+'</td>');
						}
						if('${payedAmount}'){
							body.append('<td class="p0"><div class="out-date">'+json[o].payedAmount+'</div><div class="close-date">'+json[o].accountedAmount+'</div></td>');
						}
						body.append("</tr>");
						}
					}
					// 展开团期详情
				   	sbrtr.toggle();
				}else{
					$.jBox.tip("该团期目前没有有效订单");
				}
			},
			error : function(e){
				$.jBox.tip("该团期目前没有有效订单");
			}
		})
	}else{
		$.jBox.tip("该团期目前没有有效订单");
	}
}

</script>

</head>
<body>
	<!--右侧内容部分开始-->
	<form method="post" action="${ctx}/order/airticket/activityList"
		id="searchForm" modelAttribute="activityAirTicket">
		<input id="orderBy" name="orderBy" type="hidden" value="${orderBy}" />
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<input id="groupIds" type="hidden" name="groupIds" value="" />
		<input id="companyUuid" type="hidden" value="${companyUuid}" />
		<shiro:hasPermission name="airticket:book:addAgent">
			<input id="isAddAgent" name="isAddAgent" type="hidden" value="1" />
		</shiro:hasPermission>

		<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				 <input
					class="txtPro inputTxt searchInput"
					value="${groupCodeOrActSer }"
					name="groupCodeOrActSer" id="groupCodeOrActSer" placeholder="输入团号、产品编号"/>
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input class="btn btn-primary ydbz_x" id="seachbutton" type="button"
					value="搜索" onclick="search(1)">
				<%--<input type="reset" value="清空所有条件"class="btn ydbz_x" />--%>
                <input class="btn ydbz_x" type="button"	onclick="resetSearchParams()" value="清空所有条件" />
				<shiro:hasPermission name="airticketActivity:downloadYw">
					<input class="btn ydbz_x" value="下载余位表" type="button"
						onclick="search(2)" title="此处仅支持下载今天（包含）以后出团的团期余位信息">
				</shiro:hasPermission>
			</div>
			<div class="ydxbd">
				<span></span>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">机票类型：</div>
					<div class="selectStyle">
					<form:select name="airType" id="airType"
						path="activityAirTicket.airType" itemValue="key" itemLabel="value">
						<form:option value="">不限</form:option>
						<form:option value="3">单程</form:option>
						<form:option value="2">往返</form:option>
						<form:option value="1">多段</form:option>
						<%-- <form:options items="${air_Type }"  itemValue="value" itemLabel="label" /> --%>
					</form:select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">航班类型：</div>
					<div class="selectStyle">
					<form:select name="ticket_area_type" id="ticket_area_type"
						path="activityAirTicket.ticket_area_type" itemValue="key"
						itemLabel="value">
						<form:option value="">不限</form:option>
						<form:option value="4">国内</form:option>
						<form:option value="2">国际</form:option>
						<form:option value="1">内陆</form:option>
						<form:option value="3">国际+内陆</form:option>
						<%-- <form:options items="${ticket_area_type }"  itemValue="value" itemLabel="label" /> --%>
					</form:select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">余位：</div>
					<div class="selectStyle">
					<select name="haveYw" id="haveYw">
						<option value="">请选择</option>
						<option value="1"
							<c:if test="${haveYw=='1'}">selected="selected"</c:if>>有</option>
						<option value="0"
							<c:if test="${haveYw=='0'}">selected="selected"</c:if>>无</option>
					</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">切位：</div>
					<div class="selectStyle">
					<select name="haveQw" id="haveQw">
						<option value="">请选择</option>
						<option value="1"
							<c:if test="${haveQw=='1'}">selected="selected"</c:if>>有</option>
						<option value="0"
							<c:if test="${haveQw=='0'}">selected="selected"</c:if>>无</option>
					</select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">出发城市：</div>
					<form:select path="activityAirTicket.departureCity" itemValue="id"
						itemLabel="name" name="departureCity" id="departureCity"
						class="selectinput">
						<form:option value="0">不限</form:option>
						<form:options items="${from_Areas}" itemValue="value"
							itemLabel="label" />
					</form:select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">出发日期：</div>
					<input name="startingDate"
						value='<fmt:formatDate value="${activityAirTicket.startingDate }" pattern="yyyy-MM-dd"/>'
						class="dateinput" type="text" onclick="WdatePicker()" />
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">航空公司：</div>
					<div class="selectStyle">
					<form:select name="trafficName" id="trafficName"
						path="activityAirTicket.airlines" itemValue="key"
						onchange="selectairline(this.options[this.options.selectedIndex].value,this);"
						itemLabel="value">
						<form:option value="">不限</form:option>
						<form:options items="${traffic_namelist}" itemValue="airlineCode"
							itemLabel="airlineName" />
					</form:select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">预收人数：</div>
					<input name="reserveNumber" class="" type="text"
						value="${activityAirTicket.reserveNumber}"
						onkeyup="this.value=this.value.replace(/[^\d]/g,'')"
						onafterpaste="this.value=this.value.replace(/[^\d]/g,'')" />
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">到达城市：</div>
					<form:select path="activityAirTicket.arrivedCity" itemValue="id"
						itemLabel="name" name="arrivedCity" id="arrivedCity"
						class="selectinput">
						<form:option value="0">不限</form:option>
						<form:options items="${arrivedareas}" itemValue="id"
							itemLabel="name" />
					</form:select>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">返回日期：</div>
					<input name="returnDate"
						value='<fmt:formatDate value="${activityAirTicket.returnDate }" pattern="yyyy-MM-dd"/>'
						class="dateinput" type="text" onclick="WdatePicker()" />
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">舱位等级：</div>
					<div class="selectStyle">
					<form:select name="spaceGrade" id="spaceGrade"
						path="activityAirTicket.spaceGrade" itemValue="key"
						itemLabel="value">
						<form:option value="">不限</form:option>
						<%-- <form:options items="${spaceGradelist }" itemValue="value" itemLabel="label" /> --%>
					</form:select>
					</div>
				</div>
				<div class="activitylist_bodyer_right_team_co1">
				<label class="activitylist_team_co3_text">操作人：</label> 
					<select name="activityCreate" id="activityCreate" >
						<option value="-99999">不限</option>
						<c:forEach var="user" items="${userList}">
							<option value="${user.id }"
								<c:if test="${activityCreate == user.id}">selected="selected"</c:if>>${user.name}</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</div>
	</form>

	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li class="activitylist_paixu_left_biankuang licreateDate"><a
						onclick="sortby('createDate',this)">创建时间</a></li>
					<li class="activitylist_paixu_left_biankuang liupdateDate"><a
						onclick="sortby('updateDate',this)">更新时间</a></li>
					<li
						class="activitylist_paixu_left_biankuang lisettlementAdultPrice"><a
						onclick="sortby('settlementAdultPrice',this)">价格</a></li>
					<li class="activitylist_paixu_left_biankuang listartingDate"><a
						onclick="sortby('startingDate',this)">出发时间</a></li>
				</ul>
			</div>
			<div class="activitylist_paixu_right">
				查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
			</div>
			<div class="kong"></div>
		</div>
	</div>

	<form id="groupform" name="groupform" action="" method="post">
		<table id="contentTable" class="table mainTable activitylist_bodyer_table">
			<thead style="background: #403738">
				<tr>
					<th width="4%">序号</th>
					<th width="6%">团号</th>
					<th width="6%">产品编号</th>
					<th width="6%">操作人</th>
					<th width="6%">出发城市</th>
					<th width="6%">到达城市</th>
					<th width="4%">预收</th>
					<c:if test="${hasPermission}">
						<th width="4%">已确认人数</th>
						<th width="4%">已占位人数</th>
					</c:if>
					<th width="6%">已切位/占位</th>
					<th width="4%">余位</th>
					<th width="5%">预报名</th>
					<th width="6%">成人价/税费</th>
					<th width="7%">操作</th>
					<th width="3%">预定</th>
				</tr>
			</thead>
			<tbody>
				<c:if test="${page.count <= 0 }">
					<tr class="toptr">
						<td colspan="15" style="text-align: center;">无搜索结果</td>
					</tr>
				</c:if>

				<c:forEach items="${page.list}" var="p" varStatus="s">
					<tr id="parent${s.count}">
						<td><div class="table_borderLeftN">${s.count}&nbsp&nbsp<input
									type="checkbox" name="groupid" value="${p.id }"
									onclick="acidcheckchg(this, ${s.count})" />
							</div></td>
						<td><span
							style="word-break: break-all; display: block; word-wrap: break-word;">${p.groupCode}</span></td>
						<td>${p.productCode}</td>
						<td title="电话：${p.createBy.mobile }">${p.createBy.name }</td>

						<td>${fns:getDictLabel(p.departureCity, 'from_area', '')}</td>
						<td><c:forEach items="${arrivedareas}" var="tn">
								<c:if test="${tn.id == p.arrivedCity}">
									      ${tn.name}
									    </c:if>
							</c:forEach></td>
						<td>${p.reservationsNum }</td>
						<c:if test="${hasPermission}">
							<td>${fns:getAirticketConfirmedNums(p.id)}</td>
							<td>${p.nopayReservePosition}</td>
						</c:if>
						<td>${p.payReserveNumber }/${p.nopayReservePosition}</td>
						<td>${p.freePosition }</td>
						<td></td>
						<td class="tr"><c:forEach items="${currency }" var="curr">
								<c:if test="${p.currency_id == curr.id }">
										  ${curr.currencyMark }
										</c:if>
							</c:forEach><span class="tdred fbold"><fmt:formatNumber
									type="currency" pattern="#,##0.00"
									value="${p.settlementAdultPrice }" /></span><br />
						<c:forEach items="${currency }" var="curr">
								<c:if test="${p.currency_id == curr.id }">
										  ${curr.currencyMark }
										</c:if>
							</c:forEach> <span class="tdorange fbold"><fmt:formatNumber
									type="currency" pattern="#,##0.00" value="${p.taxamt }" /></span></td>
						<td class="tda tc"><a
							onclick="expand('#child${s.count}',this)"
							href="javascript:void(0)" id="close${s.count}">展开航程</a> <a
							href="javascript:void(0)" onclick="viewActivityDetail(${p.id})">详情</a><br />
							<shiro:hasPermission name="airticketActivity:downloadYw">
								<a href="javascript:void(0)" onclick="downloadYw(${p.id});">下载余位表</a>
							</shiro:hasPermission></td>
						<td class="tc"><c:if
								test="${p.reservationStutas eq '0' && p.freePosition ne '0'}">
								<input type="button"
									onclick="agentType(${p.id},${p.payMode_full},${p.payMode_op},${p.payMode_cw},${p.payMode_deposit},${p.payMode_advance})"
									value="补单" class="btn btn-primary" />
							</c:if>
							<c:if test="${p.reservationStutas ne '0' && (p.freePosition ne '0' or (p.allLeftPayReservePosition gt '0'))}">
								<input type="button" onclick="agentType(${p.id},${p.payMode_full},${p.payMode_op},${p.payMode_cw},${p.payMode_deposit},${p.payMode_advance})"
									value="预 定" class="btn btn-primary" />
							</c:if>
							<c:if test="${p.freePosition eq '0' and (p.allLeftPayReservePosition eq '0')}">
								<input type="button" class="btn gray" value="预 定" />
							</c:if> 
							<c:if test="${queryAirticketOrderList eq 1}">
								<input class="btn btn-primary" value="已收明细"
								onclick="javascript:showOrderPay(1234,this);" type="button" />
							</c:if>
							</td>
					</tr>
					<tr class="activity_team_top1" id="child${s.count}"
						style="display: none;">
						<td class="team_top" colspan="15">
							<table style="margin: 0 auto;"
								class="table activitylist_bodyer_table" id="">
								<thead>
									<tr>
										<th class="tc" width="5%">序号</th>
										<th class="tc" width="10%">航空公司</th>
										<th class="tc" width="7%">航班号</th>
										<th class="tc" width="7%">舱位</th>
										<th class="tc" width="11%">出发机场</th>
										<th class="tc" width="11%">到达机场</th>
										<th class="tc" width="9%">起飞时间</th>
										<th class="tc" width="9%">到达时间</th>
										<th class="tr" width="9%">成人同行价</th>
										<th class="tr" width="9%">儿童同行价</th>
										<th class="p0 tr pr" width="10%"><span class="ico-remarks-td" title="${p.specialremark}"></span>
										特殊人群同行价</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${p.flightInfos }" var="flight"
										varStatus="s1">
										<tr>
											<td class="tc"><c:if test="${p.airType == 3}">${s1.count }</c:if>
												<c:if test="${p.airType == 2}">
													<c:if test="${s1.first}">去程</c:if>
													<c:if test="${s1.last}">返程</c:if>
												</c:if> <c:if test="${p.airType == 1}">第${flight.number}段</c:if></td>
											<td class="tc">
												${fns:getAirlineNameByAirlineCode(flight.airlines)}</td>
											<td class="tc">${flight.flightNumber }</td>
											<td class="tc"><c:choose>
													<c:when
														test="${flight.spaceGrade=='-1' || flight.spaceGrade=='' }">不限</c:when>
													<c:otherwise>
														<c:forEach items="${spaceGradelist}" var="spaceGrade">
															<c:if test="${spaceGrade.value eq flight.spaceGrade}">${spaceGrade.label}</c:if>
														</c:forEach>
													</c:otherwise>
												</c:choose></td>
											<td class="tc"><c:forEach items="${airports}" var="atn">
													<c:if test="${atn.id == flight.leaveAirport}">
													       ${atn.airportName }
													    </c:if>
												</c:forEach></td>
											<td class="tc"><c:forEach items="${airports}" var="atn">
													<c:if test="${atn.id == flight.destinationAirpost}">
													       ${atn.airportName }
													    </c:if>
												</c:forEach></td>
											<td class="tc"><fmt:formatDate
													value="${flight.startTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<td class="tc"><fmt:formatDate
													value="${flight.arrivalTime }"
													pattern="yyyy-MM-dd HH:mm:ss" /></td>
											<c:choose>
												<c:when test="${p.airType == 2}">
													<td class="tr">-</td>
													<td class="tr">-</td>
													<td class="tr">-</td>
												</c:when>
												<c:when test="${p.airType == 3 }">
													<td class="tr">${fns:getCurrencyInfo(p.currency_id, 0, "mark")}<span
														class="tdred fbold">${p.settlementAdultPrice}</span>
													</td>
													<td class="tr">${fns:getCurrencyInfo(p.currency_id, 0, "mark")}<span
														class="tdred fbold">${p.settlementcChildPrice}</span>
													</td>
													<td class="tr">${fns:getCurrencyInfo(p.currency_id, 0, "mark")}<span
														class="tdred fbold">${p.settlementSpecialPrice}</span>
													</td>
												</c:when>
												<c:otherwise>
													<td class="tr">
														${fns:getCurrencyInfo(flight.currency_id, 0, "mark")}<span
														class="tdred fbold">${flight.settlementAdultPrice}</span>
													</td>
													<td class="tr">
														${fns:getCurrencyInfo(flight.currency_id, 0, "mark")}<span
														class="tdred fbold">${flight.settlementcChildPrice}</span>
													</td>
													<td class="tr">
														${fns:getCurrencyInfo(flight.currency_id, 0, "mark")}<span
														class="tdred fbold">${flight.settlementSpecialPrice}</span>
													</td>
												</c:otherwise>
											</c:choose>
										</tr>

									</c:forEach>

								</tbody>
							</table>
						</td>
					</tr>
					<tr name="subtr" style="display: none;" class="activity_team_top1">
						<td class="team_top" colspan="15">
							<table class="table activitylist_bodyer_table"
								style="margin: 0 auto;" id="table_orderPay">
								<thead>
									<tr><th class="tc">序号</th>
                                    	<c:if test="${not empty orderNo}"><th class="tc" name="orderNo">订单号</th></c:if>
                                        <c:if test="${not empty agentName}"><th class="tc" name="agentName">渠道</th></c:if>
                                        <c:if test="${not empty shell }"><th class="tc" name="shell">销售</th></c:if>
                                        <c:if test="${not empty typeName }"><th class="tc" name="typeName">参团类型</th></c:if>
                                        <c:if test="${not empty orderUser }"><th class="tc" name="orderUser">下单人</th></c:if>
                                        <c:if test="${not empty reserveDate }"><th class="tc" name="reserveDate">预定时间</th></c:if>
                                        <c:if test="${not empty airTypeName }"><th class="tc" name="airTypeName">机票类型</th></c:if>
                                        <c:if test="${not empty personNum}"><th class="tc" name="personNum">人数</th></c:if>
                                        <c:if test="${not empty orderStatus }"><th class="tc" name="orderStatus">订单状态</th></c:if>
                                        <c:if test="${not empty totalAmount }"><th class="tc" name="totalAmount">订单总额</th></c:if>
                                        <c:if test="${not empty payedAmount }"><th class="tc" name="payedAmount">已付金额<br/>到账金额</th></c:if>
									</tr>
								</thead>
								<tbody>

								</tbody>
							</table>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</form>
	<div class="pagination">
		<dl>
			<dt>
				<input name="allChk" type="checkbox" onclick="checkall(this)" />全选
			</dt>
			<dt>
				<input name="allChkRevs" type="checkbox"
					onclick="checkallReverse(this)" />反选
			</dt>
			<dd>
				<input type="button" class="btn ydbz_x"
					onclick="partsOfYWDownload2($('#groupIds').val())" value="下载余位表" style="width: auto;height: 28px;">
			</dd>
		</dl>
		<div class="pagination clearFix">${page}</div>
		<div style="clear: both;"></div>
	</div>
	<!-- 下载余位 -->
	<form id="exportForm" action="${ctx}/order/airticket/downloadYw"
		method="post">
		<input type="hidden" id="activityId" name="activityId">
	</form>
	<form id="partsForm" action="${ctx}/order/airticket/downloadYwChosed"
		method="post">
		<input type="hidden" name="paramGroupIds" id="paramGroupIds">
	</form>
</body>
</html>
