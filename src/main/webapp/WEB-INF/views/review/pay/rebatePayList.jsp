<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 -->
<c:choose>
	<c:when
		test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}">
		<title>宣传费付款</title>
	</c:when>
	<c:otherwise>
		<title>返佣付款</title>
	</c:otherwise>
</c:choose>
<meta name="decorator" content="wholesaler" />
<link href="${ctxStatic}/css/Remark_New.css" type="text/css" rel="stylesheet"/>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/cost/finance_common.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.nicescroll.min.js" type="text/javascript"></script>
<script type="text/javascript">
//如果展开部分有查询条件的话，默认展开，否则收起	
function closeOrExpand(){
	var searchFormInput = $("#searchForm").find("input[type!='hidden'][type!='submit'][type!='button'][id!='wholeSalerKey'][id!='groupCode']");
	var searchFormselect = $("#searchForm").find("select");
	var inputRequest = false;
	var selectRequest = false;
	for(var i=0;i<searchFormInput.length;i++) {
		var inputValue = $(searchFormInput[i]).val();
		if(inputValue != "" && inputValue != null) {
			inputRequest = true;
			break;
		}
	}
	for(var i=0;i<searchFormselect.length;i++) {
		var selectValue = $(searchFormselect[i]).children("option:selected").val();
		if(selectValue != "" && selectValue != null && selectValue != 0) {
			selectRequest = true;
			break;
		}
	}
	if(inputRequest||selectRequest) {
		$('.zksx').click();
	}
}
$(function(){
	//展开、收起筛选
	launch();
	//如果展开部分有查询条件的话，默认展开，否则收起
	closeOrExpand();
	//操作浮框
	operateHandler();
	//计调模糊匹配
	//$("[name=jd]").comboboxSingle();
	//$("#orderTypesRebate" ).comboboxInquiry();
	$("#agentIdSRebate" ).comboboxInquiry();
	$("#creatorrefundRebate" ).comboboxInquiry();
	$("#salerrefundRebate" ).comboboxInquiry();
	$("#jdsalerSRebate" ).comboboxInquiry();
	$("#fromBankName" ).comboboxInquiry();

	$(document).delegate(".downloadzfpz","click",function(){
		window.open ("${ctx}/sys/docinfo/download/"+$(this).attr("lang"));
	});
		
	$("#contentTable").delegate("ul.caption > li","click",function(){
		var iIndex = $(this).index();/*index() 方法返回指定元素相对于其他指定元素的 index 位置。*/
		$(this).addClass("on").siblings().removeClass('on');
		$(this).parent().siblings('.leven').removeClass('onshow').eq(iIndex).addClass('onshow');
	});
	
		
	$("#contentTable").delegate(".tuanhao","click",function(){
		$(this).addClass("on").siblings().removeClass('on');
		$('.chanpin_cen').removeClass('onshow');
		$('.tuanhao_cen').addClass('onshow');
	});
		
			
	$("#contentTable").delegate(".chanpin","click",function(){
		 $(this).addClass("on").siblings().removeClass('on');
		 $('.tuanhao_cen').removeClass('onshow');
		 $('.chanpin_cen').addClass('onshow');
		
	});
		
	var _$orderBy = $("#orderBy").val();
	if(_$orderBy==""){
		_$orderBy="updatetime DESC";
		$("#orderBy").val(_$orderBy);
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
		if ($(this).hasClass("li"+orderBy[0])){
			orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
			$(this).attr("class","activitylist_paixu_moren");
		}
	});

	$(document).scrollLeft(0);
	$("#targetAreaId").val("${travelActivity.targetAreaIds}");
	$("#targetAreaName").val("${travelActivity.targetAreaNamess}");
});
$().ready(function(){
	//产品名称获得焦点显示隐藏
	$("#wholeSalerKey").focusin(function(){
		var obj_this = $(this);
			obj_this.next("span").hide();
	}).focusout(function(){
		var obj_this = $(this);
		if(obj_this.val()!="") {
		obj_this.next("span").hide();
	}else
		obj_this.next("span").show();
	});
	if($("#wholeSalerKey").val()!="") {
		$("#wholeSalerKey").next("span").hide();
	}
});

// 548,549需求
$(function () {
	$('.remark_div_child').niceScroll({
		cursorcolor: "#ccc",//#CC0071 光标颜色
		cursoropacitymax: 1, //改变不透明度非常光标处于活动状态（scrollabar“可见”状态），范围从1到0
		touchbehavior: false, //使光标拖动滚动像在台式电脑触摸设备
		cursorwidth: "5px", //像素光标的宽度
		cursorborder: "0", //     游标边框css定义
		cursorborderradius: "5px",//以像素为光标边界半径
		autohidemode: false //是否隐藏滚动条
	});
	$(".remark_548").mouseover(function () {
		$(this).children().show();
		$(".remark_div_child").getNiceScroll().resize();
	});
	$(".remark_548").mouseleave( function () {
		$(this).children().hide();
		if ($(this).find("input").is(':checked')) {
			var reviewUuid = $(this).find("input")[0].value;
			$(this).remove();
			$.ajax({
				type:"post",
				url:"${ctx}/review/common/web/cancelShowRemark",
				data:{reviewUuid:reviewUuid}
			});
		}
	})
});

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

$(function(){
	$.fn.datepicker=function(option){
		var opt = {}||option;
		this.click(function(){
			WdatePicker(option);
		});
	};
	
	$("#groupOpenDate").datepicker({
		dateFormat:"yy-mm-dd",
		dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
		closeText:"关闭", 
		prevText:"前一月", 
		nextText:"后一月",
		monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
	});
	
	$("#groupCloseDate").datepicker({
		dateFormat:"yy-mm-dd",
		dayNamesMin:["日", "一", "二", "三", "四", "五", "六"],
		closeText:"关闭", 
		prevText:"前一月", 
		nextText:"后一月",
		monthNames:["1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月"]
	});
	//首次打印提醒
	$(".uiPrint").hover(function(){
		$(this).find("span").show();
	},function(){
		$(this).find("span").hide();
	});
	
});

$(function(){
	$('.nav-tabs li').hover(function(){
		$('.nav-tabs li').removeClass('current');
		$(this).parent().removeClass('nav_current');
		if($(this).hasClass('ernav')) {
			if(!$(this).hasClass('current')){
				$(this).addClass('current');
				$(this).parent().addClass('nav_current');
			}
		}
	},function(){
			$('.nav-tabs li').removeClass('current');
			$(this).parent().removeClass('nav_current');
			var _active = $(".totalnav .active").eq(0);
			if(_active.hasClass('ernav')){
				_active.addClass('current');
				$(this).parent().addClass('nav_current');
			}
		});
	//展开收起搜索条件
	//操作浮框
	operateHandler();
	//输入金额提示
	inputTips();
});

function sortby(sortBy,obj){
	var temporderBy = $("#orderBy").val();
	if(temporderBy.match(sortBy)){
		sortBy = temporderBy;
		if(sortBy.match(/ASC/g)){
			sortBy = $.trim(sortBy.replace(/ASC/gi,""))+" DESC";
		}else{
			sortBy = $.trim(sortBy.replace(/DESC/gi,""))+" ASC";
		}
	}else{
		sortBy = sortBy+" DESC";
	}
	
	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
}
function query() {
	$('#searchForm').submit();
}

// 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22
function exportExcel() {
    $('#searchForm').attr('action', '${ctx}/costNew/payManager/getRebatePayListExcel/202');
    $('#searchForm').submit();
	$('#searchForm').attr('action', '${ctx}/costNew/payManager/payList/${payType }.htm');
}

Date.prototype.Format = function(fmt) { //author: meizz
	var o = {
		"M+" : this.getMonth()+1,				 //月份	
		"d+" : this.getDate(),					//日	
		"h+" : this.getHours(),					//小时	
		"m+" : this.getMinutes(),				 //分	
		"s+" : this.getSeconds(),				 //秒	
		"q+" : Math.floor((this.getMonth()+3)/3), //季度	
		"S"  : this.getMilliseconds()			 //毫秒	
	};
	if(/(y+)/.test(fmt))
		fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
	for(var k in o)
		if(new RegExp("("+ k +")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
			return fmt;
};
function expand(child,obj,id,type,index,orderType,reviewUUID,groupCode,revStatus,reviewFlag) {
	$.ajax({
		url:"${ctx}/orderCommon/manage/refundPayInfo/",
		type:"POST",
		data:{id:id,type:type,orderType:orderType},
		success:function(data){
			var htmlstr="";
			var num = data.length;
			if(num>0){
				var payType;
				if("${companyUuid}"=='049984365af44db592d1cd529f3008c3'){
					payType='宣传费付款';
				}else{
					payType= '返佣付款';
				}
				for(var i=0;i<num;i++){
					var status = data[i].status;//状态：0表示撤销
					var statusName = "";
					if(status == 0) {
						statusName = "已撤销";
					}else{
						statusName = "已支付";
					}
					var payOrder = '';//支付凭证
					var payOrderArray = data[i].payvoucher.split("|");
					for(var j=0;j<payOrderArray.length;j++){
						payOrder+=payOrderArray[j]+"<br/>";
					}
					if(4 != data[i].a){
						payType = '其他';
					}
					if("${companyUuid}"=='049984365af44db592d1cd529f3008c3'){
						data[i].a='宣传费付款';
					}
					if (data[i].label == null){ <%-- 拉美图的因公支付宝结算方式暂未保存到字典表中。后期如做调整可删除 --%>
						data[i].label = "因公支付宝";
					}
					htmlstr+="<tr><td class='tc'>"+data[i].label+"</td><td class='tc'>"+data[i].amount+
							"</td><td class='tc'>"+(new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss")
							+"</td><td class='tc'>"+data[i].a+
							"</td><td class='tc'>"+payOrder+"</td><td class='tc'>"+statusName+
							"</td><td class='tc'>";
					if(data[i].opstatus=='已支付'){
						if(orderType=='11' || orderType=='12'){
							htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>"+
								"&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\" >撤销</a></td></tr>";
						}else {
							htmlstr += "<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>"+
								"&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\">撤销</a>";
							
							if(revStatus=='2' || revStatus=='3') {
								if(reviewFlag == 1) {		//旧审批数据
									if(orderType=='6') {	//签证
										htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/review/visaRebates/visaRebatesReviewPrint?reviewId="+id+"&payId="+data[i].id+"&option=pay"+"\" target=\"_blank\">打印</a>";
									}else if(orderType=='7') {
										htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/airticketRebates/airticketRebatesReviewPrint?reviewId="+id+"&groupCode="+groupCode+"&payId="+data[i].id+"&option=pay"+"\" target=\"_blank\">打印</a><br/>";
									}else {
										htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/order/rebates/review/rebatesReviewPrint?reviewId="+id+"&groupCode="+groupCode+"&payId="+data[i].id+"&option=pay"+"\" target=\"_blank\">打印</a><br/>";
									}      
									 
								}else {
									if(orderType=='6') {	//签证
										htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/newRebatesReview/rebatesReviewPrint4forVisaProduct?reviewId="+reviewUUID+"&groupCode="+groupCode+"&payId="+data[i].id+"\" target=\"_blank\">打印</a>";
									}else if(orderType=='7') {
										htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/order/newAirticketRebate/airticketRebatesNewPrint?reviewId="+reviewUUID+"&groupCode="+groupCode+"&payId="+data[i].id+"\" target=\"_blank\">打印</a><br/>";
									}else {
										htmlstr += "&nbsp;&nbsp;<a href=\"${ctx}/newRebatesReview/rebatesReviewPrint?reviewId="+reviewUUID+"&groupCode="+groupCode+"&payId="+data[i].id+"\" target=\"_blank\">打印</a><br/>";
									}      
								}
							}
							htmlstr += "</td></tr>";
						}						
					}else{
						htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;</td></tr>";
					}
				}
			}else{
				htmlstr+="<tr><td colspan='7' style='text-align: center;'>经搜索暂无数据，请尝试改变搜索条件再次搜索</td></tr>";
			}
			$("#rpi_" + id + "_" + index).html(htmlstr);
		}
	});
	if ($(child).is(":hidden")) {
		$(obj).html("收起");
		$(obj).parents("tr").addClass("tr-hover");
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
	}else{
		if (!$(child).is(":hidden")) {
			$(obj).parents("tr").removeClass("tr-hover");
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("支付记录");
		}
	}
}

function cancelOper(ctx,str,orderType,obj){
	$.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&payType=2&orderType="+orderType,{
		title: "付款记录",
		width:830,
		height: 500,
		buttons:{'撤销': 1,'关闭':0},
		persistent:true,
		loaded: function(h){},
		submit:function(v,h,f){
			if(v==1){
				var refundId =  $(h.find("iframe")[0].contentWindow.refundId).val();
				var recordId =  $(h.find("iframe")[0].contentWindow.recordId).val();
				$.ajax({
					type:"GET",
					url:ctx+"/refund/manager/undoRefundPayInfo",
					dataType:"json",
					data:{refundId:refundId,recordId:recordId},
					success:function(data){
						if(data.flag=='ok'){
							var payed = $(obj).parent().parent().parent().parent().parent().parent().prev().find('.dzje_dd .fbold').html();
							var temp1 = payed.match(/\d+/g);
							var numpayed = temp1[0] + '.' + temp1[1];
							
							var cny = payed.replace(numpayed,'');
							
							var onepayed = $(obj).parent().prev().prev().prev().prev().prev().html();
							var temp2 = onepayed.match(/\d+/g);
							var numonepayed = temp2[0] + '.' + temp2[1];
							
							var result = (parseFloat(numpayed).toFixed(2)-parseFloat(numonepayed).toFixed(2)).toFixed(2);
							
							if(result == 0) {
								$(obj).parent().parent().parent().parent().parent().parent().prev().find('.dzje_dd .fbold').html("");
							}else{
								$(obj).parent().parent().parent().parent().parent().parent().prev().find('.dzje_dd .fbold').html(cny + result);
							}
							
							$(obj).parent().prev().html('已撤销');
							$(obj).next().remove();   //移除打印按钮
							$(obj).remove();							
						}
					}
				});
			}
		}
	}).find("#jbox-content").css("overflow", "hidden");
}

function cancelDetailOper(ctx,refundId,recordId,obj){
	$.ajax({
		type:"GET",
		url:ctx+"/refund/manager/undoRefundPayInfo",
		dataType:"json",
		data:{refundId:refundId,recordId:recordId},
		success:function(data){
			if(data.flag=='ok'){
				$(obj).parents("table[id=contentTable]").siblings('#searchForm').submit();
			}
		}});
}
	
/**
 * 查询条件重置
 * 
 */
var resetSearchParams = function(){
	var inputArray = $('#searchForm').find("input[type!='hidden'][type!='submit'][type!='button']");
	var selectArray = $('#searchForm').find("select");
	for(var i=0;i<inputArray.length;i++){
		if($(inputArray[i]).val()){
			$(inputArray[i]).val('');
		}
	}
	for(var i=0;i<selectArray.length;i++){
		var selectOption = $(selectArray[i]).children("option");
		$(selectOption[0]).attr("selected","selected");
	}
};

//批量付款的处理
$(document).on('change','#contentTable,#contentTable_foot, [type="checkbox"]',function(){
	var $this = $(this);
	var $contentContainer = $("#contentTable,#contentTable_foot");
	var $all =$contentContainer.find('[check-action="All"]');
	var $reverse = $contentContainer.find('[check-action="Reverse"]');
	var $chks=$contentContainer.find('[check-action="Normal"]:enabled');
	if($this.is('[check-action="All"]')){
		if($this.is(':checked')){
			$chks.attr('checked',true);
		}else{
			$chks.removeAttr('checked');
		}
	}
	if($this.is('[check-action="Reverse"]')){
		$chks.each(function(){
			var $chk = $(this);
			if($chk.is(':checked')){
				$chk.removeAttr('checked');
			}else{
				$chk.attr('checked',true);
			}
		});
	}
	if($chks.length && ($chks.length ==$chks.filter(':checked').length)){
		$all.attr('checked',true);
	}else{
		$all.removeAttr('checked');
	}
});

	function batchPrintQuery(){
		var datas = []; 
		var boxes = $("input[name='checkBox']:checked");
		if(boxes.length == 0){
			top.$.jBox.tip("请选择数据！");
			return;
		}
		for(var i = 0 ; i < boxes.length ; i ++){
			var data = {}; 
			var box = $(boxes[i]);
			var values = box.val();
			var str = values.split("_");
			if(str[2]==1){
				data.reviewId = str[0];
				data.productType = str[1];
				data.orderId = box.parent().find("input[name='orderId']").val();
				data.rebatesId = box.parent().find("input[name='rebatesId']").val();
				data.groupCode = box.parent().find("input[name='groupCode']").val();
				data.option = box.parent().find("input[name='option']").val();
				data.revstatus = box.parent().find("input[name='revstatus']").val();
				data.reviewFlag = str[2];
			}else{
				data.reviewId  = str[0];
				data.productType = str[1];
				data.rebatesId = box.parent().find("input[name='rebatesId']").val();
				data.groupCode = box.parent().find("input[name='groupCode']").val();
				data.reviewFlag = str[2];
				data.revstatus = box.parent().find("input[name='revstatus']").val();
			}
			datas.push(data);
		}
		$("#params").val(JSON.stringify(datas));
		$("#printForm").submit();
		//window.open("${ctx}/newRebatesReview/batchGroupRebatesPrintforReview?params="+JSON.stringify(datas));
	}
</script>

<style type="text/css">
a {
	display: inline-block;
}

label {
	cursor: inherit;
}
</style>

</head>
<body>
	<form action="${ctx}/newRebatesReview/batchGroupRebatesPrintforReview" id = "printForm" method="post" target="_blank">
		<input name = "params" value ="" type="hidden" id="params">
	</form>
	<c:set var="showType" value="${payType }" />
	<%@ include file="/WEB-INF/views/head/financeHead.jsp"%>
	<div class="activitylist_bodyer_right_team_co_bgcolor"
		style="float:left;width: 100%">
		<form:form id="searchForm" modelAttribute="travelActivity"
			action="${ctx}/costNew/payManager/payList/${payType }.htm"
			method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" />
			<input id="orderBy" name="orderBy" type="hidden"
				value="${page.orderBy}" />
			<!--<div class="order_bill"></div>-->
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2">
					<input id="groupCode" name="groupCode" class="txtPro searchInput inputTxt" value="${groupCode }" placeholder="请输入团号" />
				</div>
				<div class="zksx">筛选</div>
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" type="button" onclick="query()" value="搜索" />
					<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件" />
					<!-- 579需求：财务模块付款类列表页面，增加Excel导出功能 gaoyang 2017-03-22 -->
					<input class="btn ydbz_x" type="button" onclick="exportExcel()" value="导出Excel" />
				</div>
				<div class="ydxbd">
					<span></span>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">团队类型：</div>
						<div class="selectStyle">
							<select name="orderTypes" id="orderTypesRebate">
								<c:forEach var="orderType" items="${orderTypeList }">
									<c:choose>
										<c:when
											test="${fn:contains(name,'俄风行') or fn:contains(name,'九州风行') }">
											<c:if
												test="${orderType.value eq 0 or orderType.value eq 11 or orderType.value eq 12 }">
												<option value="${orderType.value }"
													<c:if test="${orderTypes==orderType.value}">selected="selected"</c:if>>${orderType.label
													}</option>
											</c:if>
										</c:when>
										<c:otherwise>
											<option value="${orderType.value }"
												<c:if test="${orderTypes==orderType.value}">selected="selected"</c:if>>${orderType.label
												}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">渠道选择：</label>
						<select name="agents" id="agentIdSRebate">
							<option value="">全部</option>
							<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 -->
							<c:choose>
								<c:when
									test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586'}">
									<option value="-1"
										<c:if test="${agents==-1}"> selected="selected"</c:if>>
										直客</option>
								</c:when>
								<c:otherwise>
									<option value="-1"
										<c:if test="${agents==-1}"> selected="selected"</c:if>>非签约渠道</option>
								</c:otherwise>
							</c:choose>
							<c:forEach var="agent" items="${agentList }">
								<c:choose>
									<c:when
										test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && agent.agentName=='非签约渠道'}">
										<option value="${agent.id}"
											<c:if test="${agents==agent.id}">selected="selected"</c:if>>直客</option>
									</c:when>
									<c:otherwise>
										<option value="${agent.id}"
											<c:if test="${agents==agent.id}">selected="selected"</c:if>>${agent.agentName}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">计调：</div>
						<select name="jds" id="jdsalerSRebate">
							<option value="">请选择</option>
							<c:forEach var="jd" items="${jdList }">
								<option value="${jd.id }"
									<c:if test="${jds==jd.id}">selected="selected"</c:if>>${jd.name
									}</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co4">
						<div class="activitylist_team_co3_text">付款金额：</div>
						<div class="selectStyle">
							<select id="currency" name="currency">
								<option value="">币种选择</option>
								<c:forEach items="${currencyList}" var="c">
									<option value="${c.id}"
										<c:if test="${currency==c.id}">selected="selected"</c:if>>
										${c.currencyName}</option>
								</c:forEach>
							</select>
						</div>
						<input type="text" value="${startMoney }" name="startMoney" id="startMoney" class="inputTxt" onkeyup="validNum(this)"
							   onafterpaste="validNum(this))" placeholder="输入金额"/>
						<span>~</span>
						<input type="text" value="${endMoney }" name="endMoney" id="endMoney" class="inputTxt"
							   onkeyup="validNum(this)" onafterpaste="validNum(this))" placeholder="输入金额"/>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">打印状态：</div>
						<div class="selectStyle">
							<select name="printFlag">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${printFlag eq '0' }">selected="selected"</c:if>>未打印</option>
								<option value="1"
									<c:if test="${printFlag eq '1' }">selected="selected"</c:if>>已打印</option>
							</select>
						</div>
					</div>
					<%--<div class="activitylist_bodyer_right_team_co1">--%>
					<%--<div class="activitylist_team_co3_text">游客：</div>--%>
					<%--<select name="travelers" id="travellersRebate">--%>
					<%--<option value="">请选择</option>--%>
					<%--<c:forEach var="traveler" items="${travelerList }">--%>
					<%--<option value="${traveler.id }" <c:if test="${travelers==traveler.id}">selected="selected"</c:if>>${traveler.name }</option>--%>
					<%--</c:forEach>--%>
					<%--</select>--%>
					<%--</div>--%>
					<c:if test="${orderTypes ne 11 and orderTypes ne 12 }">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">销售：</div>
							<select name="salers" id="salerrefundRebate">
								<option value="">请选择</option>
								<c:forEach var="saler" items="${salerList }">
									<option value="${saler.id }"
										<c:if test="${salers==saler.id}">selected="selected"</c:if>>${saler.name
										}</option>
								</c:forEach>
							</select>
						</div>
					</c:if>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">下单人：</div>
						<select name="creators" id="creatorrefundRebate">
							<option value="">请选择</option>
							<c:forEach var="creator" items="${salerList }">
								<option value="${creator.id }"
									<c:if test="${creators==creator.id}">selected="selected"</c:if>>${creator.name}</option>
							</c:forEach>
						</select>
					</div>
					<!-- 0477需求，添加申请日期筛选条件 yang.wang 2016.7.25 -->
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">申请日期：</label>
						<input id="createTimeMin" name="createTimeMin" class="inputTxt dateinput" 
							value="${createTimeMin}" readonly onClick="WdatePicker()" /> 至
						<input id="createTimeMax" name="createTimeMax" class="inputTxt dateinput"
							value="${createTimeMax}" readonly onClick="WdatePicker()" />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">来款银行：</div>
						<select name="fromBankName" id="fromBankName">
							<option value="">请选择</option>
							<c:forEach var="frombankname" items="${fromBankNames}">
								<option value="${frombankname.bankName}"
									<c:if test="${fromBankName eq frombankname.bankName}">selected="selected"</c:if>>${frombankname.bankName}</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">支付方式：</div>
						<div class="selectStyle">
							<select name="payMode">
								<option value="">请选择</option>
								<option value="1"
									<c:if test="${payMode eq '1'}">selected="selected"</c:if>>支票</option>
								<option value="3"
									<c:if test="${payMode eq '3'}">selected="selected"</c:if>>现金支付</option>
								<option value="4"
									<c:if test="${payMode eq '4'}">selected="selected"</c:if>>汇款</option>
								<option value="6"
									<c:if test="${payMode eq '6'}">selected="selected"</c:if>>银行转账</option>
								<option value="7"
									<c:if test="${payMode eq '7'}">selected="selected"</c:if>>汇票</option>
								<option value="8"
									<c:if test="${payMode eq '8'}">selected="selected"</c:if>>POS机刷卡</option>
								<c:if test="${isLMT }">
									<option value="9"
										<c:if test="${payMode eq '9'}">selected="selected"</c:if>>因公支付宝</option>
								</c:if>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">付款状态：</div>
						<div class="selectStyle">
							<select name="payStatus">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${payStatus eq '0'}">selected="selected"</c:if>>未付款</option>
								<option value="1"
									<c:if test="${payStatus eq '1'}">selected="selected"</c:if>>已付款</option>
							</select>
						</div>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">出纳确认时间：</label> <input
							id="cashierConfirmDateBegin" name="cashierConfirmDateBegin"
							class="inputTxt dateinput" value="${cashierConfirmDateBegin}"
							readonly onClick="WdatePicker()" /> 至 <input
							id="cashierConfirmDateEnd" name="cashierConfirmDateEnd"
							value="${cashierConfirmDateEnd}" readonly onClick="WdatePicker()"
							class="inputTxt dateinput" />
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">游客：</div>
						<input id="travelers" name="travelers"
							class="inputTxt inputTxtlong" value="${travelers }" />
					</div>
					<c:if test="${fns:getUser().userType ==1}">
						<input type="hidden" name="agentId"
							value="${fns:getUser().company.id}" />
					</c:if>
					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text" style="width:85px;">渠道结算方式：</label>
						<div class="selectStyle">
							<select name="paymentType">
								<option value="">不限</option>
								<c:forEach var="pType" items="${fns:findAllPaymentType()}">
									<option value="${pType[0] }"
										<c:if test="${paymentType == pType[0]}">selected="selected"</c:if>>${pType[1]}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</div>

			</div>
		</form:form>
		<div class="activitylist_bodyer_right_team_co_paixu">
			<div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
					<ul>
						<li
						<li class="activitylist_paixu_left_biankuang licreatetime"><a
							onClick="sortby('createtime',this)">创建时间</a></li>
						<li class="activitylist_paixu_left_biankuang liupdatetime"><a
							onClick="sortby('updatetime',this)">更新时间</a></li>
					</ul>
				</div>
				<div class="activitylist_paixu_right">
					查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
				</div>
				<div class="kong"></div>
			</div>
		</div>
		<table id="contentTable" class="table mainTable activitylist_bodyer_table">
			<!--  需求0307 起航假期  王洋   -->
			<thead>
				<tr>
					<th colspan="2" width="6%">序号</th>
					<th width="8%">申请日期</th>
					<th width="5%">团队类型</th>
					<c:if test="${isHQX}">
						<th width="8%">订单团号</th>
					</c:if>
					<c:if test="${!isHQX}">
						<th width="8%">团号</th>
					</c:if>
					<th width="9%">团队名称</th>
					<th width="7%">渠道商</th>
					<th width="6%">游客</th>
					<th width="7%">款项</th>
					<th width="7%">付款金额<br>已付金额</th>
					<th width="8%">计调</th>
					<th width="8%"><c:choose>
							<c:when test="${isQHJQ }">
								<div>销售</div>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${params.prdType eq 11 or params.prdType eq 12 }">下单人</c:when>
									<c:otherwise>
										<span class="tuanhao on">销售</span>/
									<span class="chanpin">下单人</span>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose></th>
					<th width="5%">出纳确认</th>
					<th width="6%">出纳确认日期</th>
					<th width="5%">打印状态</th>
					<th width="6%">操作</th>
				</tr>
			</thead>

			<tbody>
				<c:if test="${fn:length(page.list) <= 0 }">
					<tr>
						<td colspan="16" style="text-align: center;">暂无搜索结果</td>
					</tr>
				</c:if>

				<c:forEach items="${page.list }" var="orders" varStatus="s">
					<tr>
						<c:if test="${orders.reviewflag eq 1 }">
							<td class="tc"><input type="checkbox" name="checkBox"
								value="${orders.revid }_${orders.prdtype }_${orders.reviewflag }"
								check-action="Normal"  />
								<input type="hidden" name = "orderId" value="${orders.orderid}"/>
								<input type="hidden" name = "rebatesId" value="${orders.rid}_${orders.id}"/>
								<input type="hidden" name = "groupCode" value="${orders.groupcode }"/>
								<input type="hidden" name = "option" value="order"/>
								<input type="hidden" name = "revstatus" value="${orders.revstatus }"/>
							</td>
						</c:if>
						<c:if test="${orders.reviewflag eq 2 }">
							<td class="tc"><input type="checkbox" name="checkBox"
								value="${orders.reviewUuid }_${orders.prdtype }_${orders.reviewflag }"
								check-action="Normal" />
								<input type="hidden" name = "rebatesId" value="${orders.id}"/>
								<input type="hidden" name = "groupCode" value="${orders.groupcode }"/>
								<input type="hidden" name = "revstatus" value="${orders.revstatus }"/>
							</td>
						</c:if>
						<td class="tc">${s.count }</td>
						<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss"
								value="${orders.createdate }" />
						</td>
						<td class="tc">${fns:getStringOrderStatus(orders.prdtype) }</td>
						<td class="tc">${orders.groupcode }</td>
						<c:if test="${orders.prdtype !=11 && orders.prdtype !=12}">
							<td class="tc">${fns:getProductName(orders.chanpid,
								orders.prdtype)}</td>
						</c:if>
						<c:if test="${orders.prdtype ==11 || orders.prdtype ==12}">
							<td>${orders.chanpname}</td>
						</c:if>
						<td class="tc">
							<!-- 315需求,针对越柬行踪，将非签约渠道改为签约渠道 --> <c:choose>
								<c:when
									test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' && orders.agentid=='-1'}"> 
							       直客
							   </c:when>
								<c:otherwise>
							      ${fns:getAgentName(orders.agentid) }
							   </c:otherwise>
							</c:choose></td>
						<td class="tc"><c:choose>
								<c:when test="${empty orders.travelerid}">
									---
								</c:when>
								<c:otherwise>
									${fns:getTravelerNameById(orders.travelerid)}
								</c:otherwise>
							</c:choose></td>
						<td class="tc">
							<!-- 265需求，针对鼎鸿假，将返佣字段改为宣传费 --> <c:choose>
								<c:when test="${orders.prdtype == 6 }">
									<c:choose>
										<c:when
											test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
										签证宣传费
									</c:when>
										<c:otherwise>
							       		签证返佣
							        </c:otherwise>
									</c:choose>
								</c:when>
								<c:when test="${orders.prdtype == 7 }">
									<c:choose>
										<c:when
											test="${fns:getUser().company.uuid eq '049984365af44db592d1cd529f3008c3'}"> 
										机票宣传费
									</c:when>
										<c:otherwise>
							       		机票返佣
							        </c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>${orders.costname }</c:otherwise>
							</c:choose></td>
						<td class="p0 tr">
							<div class="yfje_dd">
								<c:if test="${orders.reviewflag eq 1 }">
									<span class="fbold">${fns:getCurrencyNameOrFlag(orders.currencyId,
										"0")}<fmt:formatNumber type="currency" pattern="#,##0.00"
											value="${orders.rebatesDiff}" />
									</span>/<span style="color:#000">${orders.rate}</span>
								</c:if>
								<c:if test="${orders.reviewflag eq 2 }">
									<c:forEach items="${fns:getPayedMoney(orders.reviewUuid)}"
										var="pay" varStatus="ss">
										<c:choose>
											<c:when test="${not ss.last}">
												<span class="fbold">${fns:getCurrencyNameOrFlag(pay.currencyId,
													"0")}<fmt:formatNumber type="currency" pattern="#,##0.00"
														value="${pay.rebatesDiff}" />
												</span>/<span style="color:#000">${pay.rate}</span> +
										</c:when>
											<c:otherwise>
												<span class="fbold">${fns:getCurrencyNameOrFlag(pay.currencyId,
													"0")}<fmt:formatNumber type="currency" pattern="#,##0.00"
														value="${pay.rebatesDiff}" />
												</span>/<span style="color:#000">${pay.rate}</span>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</c:if>
							</div>
							<div class="dzje_dd">
								<span class="fbold"> <c:if
										test="${orders.reviewflag eq 1 }">
									${fns:getRefundPayedMoney(orders.revid, "3", orders.prdtype)}
								</c:if> <c:if test="${orders.reviewflag eq 2 }">
									${fns:getRefundPayedMoney(orders.revid, "9", orders.prdtype)}
								</c:if> </span>
							</div></td>
						<td class="tc">${fns:getUserNameById(orders.jidcreateby )}</td>
						<td class="tc"><c:choose>
								<c:when test="${isQHJQ }">
									<div title="${orders.salerName }">${orders.salerName }</div>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${orderTypes eq 11 or orderTypes eq 12 }">${fns:getUserNameById(orders.salecreateby )}</c:when>
										<c:otherwise>
											<div class="tuanhao_cen onshow" title="${orders.salerName }">${orders.salerName
												}</div>
											<div class="chanpin_cen qtip"
												title="${fns:getUserNameById(orders.salecreateby )}">${fns:getUserNameById(orders.salecreateby
												)}</div>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose></td>
						<td class="tc">
							<c:if test="${orders.payStatus eq '0' }">
								<font style="color:red" class="noPay">未付</font>
							</c:if> <c:if test="${orders.payStatus eq '1' }">
								<font style="color:green" class = "noPay">已付</font>
							</c:if>

							<c:if test="${orders.reviewflag eq 2}">
								<c:set var="reviewLogs" value="${fns:getHavingRemarkReviewLogs(orders.reviewUuid)}"/>
								<c:if test="${not empty reviewLogs}">
								<c:set var="review" value="${fns:getReviewNewByUuid(orders.reviewUuid)}" />
								<c:if test="${empty review.extend4 or review.extend4 eq '0'}">
								<div class="relative div_548">
									<div class="remark_548">备注
										<div class="remark_div">
											<em class="arrows_548"></em>
											<div class="remark_div_head">
												<div class="remark_head_left">审批备注</div>
												<div class="remark_head_right">
													<input type="checkbox" value="${orders.reviewUuid}"/>
													<div>不再显示</div>
												</div>
											</div>
											<div class="remark_div_body">
												<div class="remark_div_child">
													<c:forEach var="reviewLog" items="${reviewLogs}" varStatus="status">
														<c:choose>
															<%-- 驳回状态 --%>
															<c:when test="${reviewLog.operation eq 2}">
																<ul>
																	<li class="left_548">
																		<div class="color_black">
																			<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																		</div>
																		<div class="green_548">${reviewLog.operationDescription}</div>
																	</li>
																	<li class="center_548"><em class="point-time stop"></em></li>
																	<li class="right_548">
																		<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																		( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																		<div class="right_body">
																			<div class="remarks">备注：</div>${reviewLog.remark}
																		</div>
																	</li>
																</ul>
																<ul>
																	<li class="left_548">
																		<div class="color_black"></div>
																		<div class="green_548">审批驳回</div>
																	</li>
																	<li class="center_548"><em class="point-time end"></em></li>
																	<li class="right_548">
																		<div class="right_head"></div>
																	</li>
																</ul>
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${(review.status eq 2) and status.last}">
																		<%--bug 17110 添加了people_name_548的span 修改了样式 start--%>
																		<ul>
																			<li class="left_548">
																				<div class="color_black">
																					<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																				</div>
																				<div class="green_548">${reviewLog.operationDescription}</div>
																			</li>
																			<li class="center_548"><em class="point-time step_yes"></em></li>
																			<li class="right_548">
																				<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																				( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																				<div class="right_body">
																					<div class="remarks">备注：</div>${reviewLog.remark}
																				</div>
																			</li>
																		</ul>
																		<ul>
																			<li class="left_548">
																				<div class="color_black"></div>
																				<div class="green_548">审批通过</div>
																			</li>
																			<li class="center_548"><em class="point-time yes"></em></li>
																			<li class="right_548">
																				<div class="right_head"></div>
																			</li>
																		</ul>
																	</c:when>
																	<c:when test="${(review.status eq 1) and status.last}">
																		<ul>
																			<li class="left_548">
																				<div class="color_black">
																					<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																				</div>
																				<div class="green_548">${reviewLog.operationDescription}</div>
																			</li>
																			<li class="center_548"><em class="point-time step_yes"></em></li>
																			<li class="right_548">
																				<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																				( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																				<div class="right_body">
																					<div class="remarks">备注：</div>${reviewLog.remark}
																				</div>
																			</li>
																		</ul>
																		<ul>
																			<li class="left_548">
																				<div class="color_black"></div>
																				<div class="green_548"></div>
																			</li>
																			<li class="center_548"><em class="point-time next"></em></li>
																			<li class="right_548">
																				<div class="right_head"></div>
																			</li>
																		</ul>
																	</c:when>
																	<c:otherwise>
																		<ul>
																			<li class="left_548">
																				<div class="color_black">
																					<fmt:formatDate value="${reviewLog.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
																				</div>
																				<div class="green_548">${reviewLog.operationDescription}</div>
																			</li>
																			<li class="center_548"><em class="point-time step_yes"></em></li>
																			<li class="right_548">
																				<div class="right_head">${fns:getJobName(reviewLog.tagkey)}</div>
																				( <span class="people_name_548">${fns:getUserNameById(reviewLog.createBy)}</span> )
																				<div class="right_body">
																					<div class="remarks">备注：</div>${reviewLog.remark}
																				</div>
																			</li>
																		</ul>
																	</c:otherwise>
																	<%--bug 17110 添加了people_name_548的span 修改了样式 end--%>
																</c:choose>
															</c:otherwise>
														</c:choose>
													</c:forEach>
												</div>
											</div>
										</div>
									</div>
								</div>
								</c:if>
								</c:if>
							</c:if>

						</td>
						<td class="tc"><c:if test="${orders.payStatus eq '1' }">
								<fmt:formatDate value="${orders.payConfirmDate}"
									pattern="yyyy-MM-dd" />
							</c:if></td>
						<td class="tc"><c:if
								test="${empty orders.printFlag or orders.printFlag eq 0 }">
								<font style="color:green">未打印</font>
							</c:if> <c:if test="${orders.printFlag eq 1 }">
								<p class="uiPrint">
									<font style="color:green">已打印</font><span
										style="display: none;color:purple;">首次打印时间<br>
									<fmt:formatDate value="${orders.printTime}"
											pattern="yyyy-MM-dd HH:mm" />
									</span>
								</p>
							</c:if></td>
						<td class="p0">
							<dl class="handle">
								<dt>
									<img title="操作" src="${ctxStatic }/images/handle_cz_rebuild.png">
								</dt>
								<dd>
									<p>
										<span></span>
										<c:if test="${orders.reviewflag eq 1 }">
											<a
												href="${ctx}/cost/payManager/pay/202?orderType=${orders.prdtype}&orderId=${orders.orderid}&travelerId=${orders.travelerId}&currencyId=${orders.currencyId}&payPrice=${orders.rebatesDiff}&reviewId=${orders.revid}&agentId=${orders.agentid}"
												target="_blank">付款 </a>
										</c:if>
										<c:if test="${orders.reviewflag eq 2 }">
											<a
												href="${ctx}/costNew/payManager/pay/202?orderType=${orders.prdtype}&orderId=${orders.orderid}&travelerId=${orders.travelerId}&currencyId=${fns:getCurrencyId(orders.reviewUuid)}&payPrice=${fns:getMoney(orders.reviewUuid)}&reviewId=${orders.reviewUuid}&agentId=${orders.agentid}"
												target="_blank">付款 </a>
										</c:if>
										<c:choose>
											<c:when test="${orders.reviewflag eq 1 }">
												<c:if test="${orders.prdtype < 6 || orders.prdtype == 10}">
													<a
														href="${ctx }/cost/review/read/${orders.chanpid }/${orders.groupid }/0?from=operatorPre&menuid=3"
														target="_blank">查看</a>
												</c:if>
												<c:if test="${orders.prdtype == 6 }">
													<a
														href="${ctx }/cost/review/visaRead/${orders.chanpid }/0?menuid=3"
														target="_blank">查看</a>
												</c:if>
												<c:if test="${orders.prdtype == 7 }">
													<a
														href="${ctx }/cost/review/airTicketRead/${orders.chanpid }/0?menuid=3"
														target="_blank">查看</a>
												</c:if>
												<c:if test="${orders.prdtype == 11}">
													<a target="_blank"
														href="${ctx}/cost/review/hotelRead/${orders.chanpid}/${orders.groupid}/11?from=operatorPre&menuid=3">查看</a>
												</c:if>
												<c:if test="${orders.prdtype == 12}">
													<a target="_blank"
														href="${ctx}/cost/review/islandRead/${orders.chanpid}/${orders.groupid}/12?from=operatorPre&menuid=3">查看</a>
												</c:if>
											</c:when>
											<c:when test="${orders.reviewflag eq 2 }">
												<c:if test="${orders.prdtype < 6 || orders.prdtype == 10}">
													<a
														href="${ctx }/costReview/activity/activityCostReviewDetail/${orders.chanpid }/${orders.groupid }/3?read=1"
														target="_blank">查看</a>
												</c:if>
												<c:if test="${orders.prdtype == 6 }">
													<a
														href="${ctx }/costReview/visa/visaCostReviewDetail/${orders.chanpid }/3?read=1"
														target="_blank">查看</a>
												</c:if>
												<c:if test="${orders.prdtype == 7 }">
													<a
														href="${ctx }/costReview/airticket/airticketCostReviewDetail/${orders.chanpid }/3?read=1"
														target="_blank">查看</a>
												</c:if>
											</c:when>
										</c:choose>
										<c:choose>
											<c:when test="${orders.prdtype < 6 or orders.prdtype == 10 }">

												<shiro:hasPermission
													name="commissionpay:operation:statement">
													<a
														href="${ctx }/cost/manager/forcastList/${orders.groupid}/${orders.prdtype}"
														target="_blank">预报单</a>
												</shiro:hasPermission>
												<shiro:hasPermission
													name="commissionpay:operation:finalStatement">
													<a
														href="${ctx }/cost/manager/settleList/${orders.groupid}/${orders.prdtype}"
														target="_blank">结算单</a>
												</shiro:hasPermission>
											</c:when>
											<c:when
												test="${orders.prdtype == 11 or orders.prdtype == 12}">
												<shiro:hasPermission
													name="commissionpay:operation:statement">
													<a
														href="${ctx }/cost/manager/forcastList/${orders.groupid}/${orders.prdtype}"
														target="_blank">预报单</a>
												</shiro:hasPermission>
												<shiro:hasPermission
													name="commissionpay:operation:finalStatement">
													<a
														href="${ctx }/cost/manager/settleList/${orders.groupid}/${orders.prdtype}"
														target="_blank">结算单</a>
												</shiro:hasPermission>
												<a href="${ctx }/cost/manager/returnMoneyList/${orders.revid}/${orders.prdtype}" target="_blank">打印</a>
											</c:when>
											<c:otherwise>
												<shiro:hasPermission
													name="commissionpay:operation:statement">
													<a
														href="${ctx }/cost/manager/forcastList/${orders.chanpid}/${orders.prdtype}"
														target="_blank">预报单</a>
												</shiro:hasPermission>
												<shiro:hasPermission
													name="commissionpay:operation:finalStatement">
													<a
														href="${ctx }/cost/manager/settleList/${orders.chanpid}/${orders.prdtype}"
														target="_blank">结算单</a>
												</shiro:hasPermission>
											</c:otherwise>
										</c:choose>
										<c:if test="${orders.reviewflag eq 1 }">
											<a
												onclick="expand('#child1_${orders.revid}_${s.count}',this,${orders.revid},'3',${s.count},${orders.prdtype},'${orders.reviewUuid}','${orders.groupcode}',${orders.revstatus},${orders.reviewflag})"
												href="javascript:void(0)">支付记录</a>
										</c:if>
										<c:if test="${orders.reviewflag eq 2 }">
											<a
												onclick="expand('#child1_${orders.revid}_${s.count}',this,${orders.revid},'9',${s.count},${orders.prdtype},'${orders.reviewUuid}','${orders.groupcode}',${orders.revstatus},${orders.reviewflag})"
												href="javascript:void(0)">支付记录</a>
										</c:if>
										<c:choose>
											<c:when test="${orders.reviewflag eq 1 }">
												<c:choose>
													<c:when test="${orders.payStatus eq '0' }">
														<a
															onclick="jbox_paymentconfirmold('${ctx }',${orders.revid},'3','1',${orders.payStatus },${orders.prdtype})"
															href="javascript:void(0)">确认付款</a>
													</c:when>
													<c:otherwise>
														<a
															onclick="confirmOrCannePay('${ctx }',${orders.revid},'3','0',${orders.payStatus })"
															href="javascript:void(0)">撤销付款</a>
													</c:otherwise>
												</c:choose>
											</c:when>
											<c:when test="${orders.reviewflag eq 2 }">
												<c:choose>
													<c:when
														test="${orders.payStatus eq '0' and (orders.prdtype eq '6' or orders.prdtype eq '7') }">
														<a
															onclick="jbox_paymentconfirm2('${ctx }','${orders.reviewUuid}','1','0',${orders.prdtype})"
															href="javascript:void(0)">确认付款</a>
													</c:when>
													<c:when test="${orders.payStatus eq '0' }">
														<a
															onclick="jbox_paymentconfirm('${ctx }','${orders.reviewUuid}','1','0',${orders.prdtype})"
															href="javascript:void(0)">确认付款</a>
													</c:when>
													<c:otherwise>
														<a
															onclick="confirmOrCannelPay('${ctx }','${orders.reviewUuid}','0','1')"
															href="javascript:void(0)">撤销付款</a>
													</c:otherwise>
												</c:choose>
											</c:when>
										</c:choose>
										<!-- 取消青岛凯撒打印限制  -->
										<shiro:hasPermission name="rebates:operation:print">
											<c:if test="${orders.reviewflag eq 1 }">
												<c:choose>
													<c:when
														test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype eq 6 }">
														<a
															href="${ctx}/review/visaRebates/visaRebatesReviewPrint?reviewId=${orders.revid}&orderId=${orders.orderid}&option=order" 
															target="_blank">打印</a>
													</c:when>
													<c:when
														test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype eq 7 }">
														<a
															href="${ctx}/airticketRebates/airticketRebatesReviewPrint?reviewId=${orders.revid}&rebatesId=${orders.rid}&groupCode=${orders.groupcode}&option=order"
															target="_blank">打印</a>
														<br />
													</c:when>
													<c:when
														test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype ne 6 and orders.prdtype ne 7 and orders.prdtype ne 11 and orders.prdtype ne 12 }">
														<a
															href="${ctx}/order/rebates/review/rebatesReviewPrint?reviewId=${orders.revid}&rebatesId=${orders.id}&groupCode=${orders.groupcode}&option=order"
															target="_blank">打印</a>
														<br />
													</c:when>
												</c:choose>
											</c:if>
											<c:if test="${orders.reviewflag eq 2 }">
												<c:choose>
													<c:when
														test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype eq 6 }">
														<a
															href="${ctx}/newRebatesReview/visaRebatesPrintforReview?reviewId=${orders.reviewUuid}&rebatesId=${orders.id}&groupCode=${orders.groupcode}"
															target="_blank">打印</a>
													</c:when>
													<c:when
														test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype eq 7 }">
														<a
															href="${ctx}/order/newAirticketRebate/airticketRebatesPrintforReview?reviewId=${orders.reviewUuid}&rebatesId=${orders.id}&groupCode=${orders.groupcode }"
															target="_blank">打印</a>
														<br />
													</c:when>
													<c:when
														test="${orders.revstatus == '2' and orders.prdtype ne 6 and orders.prdtype ne 7 and orders.prdtype ne 11 and orders.prdtype ne 12 }">
														<a
															href="${ctx}/newRebatesReview/groupRebatesPrintforReview?reviewId=${orders.reviewUuid}&rebatesId=${orders.id}&groupCode=${orders.groupcode}"
															target="_blank">打印</a>
														<br />
													</c:when>
												</c:choose>
											</c:if>
										</shiro:hasPermission>
									</p>
								</dd>
							</dl>
						</td>
					</tr>

					<tr id="child1_${orders.revid}_${s.count}"
						class="activity_team_top1" style="display:none">
						<td colspan="16" class="team_top">
							<table id="teamTable" class="table activitylist_bodyer_table"
								style="margin:0 auto;">
								<thead>
									<tr>
										<th class="tc" width="10%">付款方式</th>
										<th class="tc" width="10%">金额</th>
										<th class="tc" width="15%">日期</th>
										<th class="tc" width="10%">支付类型</th>
										<th class="tc" width="25%">支付凭证</th>
										<th class="tc" width="10%">状态</th>
										<th class="tc" width="15%">操作</th>
									</tr>
								</thead>
								<tbody id='rpi_${orders.revid}_${s.count}'>

								</tbody>
							</table>
						</td>
					</tr>

				</c:forEach>
			</tbody>
		</table>
		<div class="page" id="contentTable_foot">
			<div class="pagination">
				<dl>
					<dt><input check-action="All" type="checkbox">全选</dt>
					<dt><input check-action="Reverse" type="checkbox">反选</dt>
					<dd>
						<input type="button" class="btn ydbz_x" value="批量确认付款"
							   onclick="paymentconfirmall('${ctx }','checkBox','3',1)" />
						<shiro:hasPermission name="rebates:operation:print">
							&nbsp;<input type="button" class="btn ydbz_x" value="批量打印" onclick="batchPrintQuery()" />
						</shiro:hasPermission>
					</dd>
				</dl>
			</div>
			<div class="pagination clearFix">${page}</div>
		</div>
</div>

</body>
</html>