<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<title>返佣付款</title>
<meta name="decorator" content="wholesaler" />
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script src="${ctxStatic}/modules/order/payOrRefund.js" type="text/javascript"></script>
<script src="${ctxStatic}/js/common.js" type="text/javascript"></script>
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
	}else{
		$('.zksx').text('展开筛选');
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
		_$orderBy="updateTime DESC";
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
		if($(this).hasClass('ernav')){
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

Date.prototype.Format = function(fmt){ //author: meizz   
	var o = {
		"M+" : this.getMonth()+1,				 //月份   
		"d+" : this.getDate(),					//日   
		"h+" : this.getHours(),				   //小时   
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
function expand(child,obj,id,type,index,orderType) {
	$.ajax({
		url:"${ctx}/orderCommon/manage/refundPayInfo/",
		type:"POST",
		data:{id:id,type:type,orderType:orderType},
		success:function(data){
			var htmlstr="";
			var num = data.length;
			if(num>0){
				var payType = '返佣付款';
				for(var i=0;i<num;i++){
					var status = data[i].status;//状态：0表示撤销
					var statusName = "";
					if(status == 0) {
						statusName = "已撤销";
					}else{
						statusName = "未撤销";
					}
					var payOrder = '';//支付凭证
					var payOrderArray = data[i].payvoucher.split("|");
					for(var j=0;j<payOrderArray.length;j++){
						payOrder+=payOrderArray[j]+"<br/>";
					}
					if(4 != data[i].a){
						payType = '其他';
					}
					htmlstr+="<tr><td class='tc'>"+data[i].label+"</td><td class='tc'>"+data[i].amount+
							"</td><td class='tc'>"+(new Date(data[i].createdate)).Format("yyyy-MM-dd hh:mm:ss")
							+"</td><td class='tc'>"+data[i].a+
							"</td><td class='tc'>"+payOrder+"</td><td class='tc'>"+statusName+
							"</td><td class='tc'>";
					if(data[i].opstatus=='已支付'){
						htmlstr+="<a onclick=\"showPayDetailInfo('${ctx}','"+data[i].id+"',2,cancelDetailOper,"+data[i].orderType+",this)\" href='javascript:void'>查看</a>&nbsp;&nbsp;<a onclick=\"cancelOper('${ctx}','"+data[i].id+"','"+data[i].orderType+"',this)\" >撤销</a></td></tr>";
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
	$.jBox("iframe:"+ctx+"/refund/manager/cancelPayInfo?refundId="+str+"&flag=edit&orderType="+orderType,{
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
							$(obj).remove();
		
						}
					}
					
				 });
			}
		}
	});
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
//	 $('#groupCode').val('');
//	 $('select[name=orderTypes]').val('');
//	 $('select[name=agents]').val('');
//	 $('select[name=jds]').val('');
//	 $('select[name=payStatus]').val('');
//	 $('select[name=currency]').val('');
//	 $('#startMoney').val('');
//	 $('#endMoney').val('');
//	 $('select[name=salers]').val('');
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
$(document).on('change','#contentTable, [type="checkbox"]',function(){
	var $this = $(this);
	var $contentTable = $("#contentTable");
	var $all =$contentTable.find('[check-action="All"]');
	var $reverse = $contentTable.find('[check-action="Reverse"]');
	var $chks=$contentTable.find('[check-action="Normal"]:enabled');
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
	<page:applyDecorator name="finance_op_head">
		<page:param name="showType">${payType }</page:param>
	</page:applyDecorator>
	<div class="activitylist_bodyer_right_team_co_bgcolor"
		style="float:left;width: 100%">
		<form:form id="searchForm" modelAttribute="travelActivity"
			action="${ctx}/cost/payManager/payList/${payType }.htm"	method="post">
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				value="${page.pageSize}" />
			<input id="orderBy" name="orderBy" type="hidden"
				value="${page.orderBy}" />
			<!--<div class="order_bill"></div>-->
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co2 wpr20">
					<label>团号：</label><input id="groupCode" name="groupCode" class="txtPro inputTxt" value="${groupCode }" />
				</div>
				<div class="form_submit">
					<input class="btn btn-primary ydbz_x" type="button"	onclick="query()" value="搜索" />
					<input class="btn ydbz_x" type="button" onclick="resetSearchParams()" value="清空所有条件" />
				</div>
				<div class="zksx">筛选</div>
				<div class="ydxbd">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">团队类型：</div>
						<select name="orderTypes" id="orderTypesRebate">
							<c:forEach var="orderType" items="${orderTypeList }">
								<c:choose>
									<c:when test="${fn:contains(name,'俄风行') or fn:contains(name,'九州风行') }">
										<c:if test="${orderType.value eq 0 or orderType.value eq 11 or orderType.value eq 12 }">
											<option value="${orderType.value }"
												<c:if test="${orderTypes==orderType.value}">selected="selected"</c:if>>${orderType.label }</option>
										</c:if>
									</c:when>
									<c:otherwise>
										<option value="${orderType.value }"
											<c:if test="${orderTypes==orderType.value}">selected="selected"</c:if>>${orderType.label }</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</div>

					<div class="activitylist_bodyer_right_team_co1">
						<label class="activitylist_team_co3_text">渠道选择：</label> 
						<select	name="agents" id="agentIdSRebate">
							<option value="">全部</option>
							<c:forEach var="s" items="${agentList}">
								<c:choose>
									<c:when test="${companyUuid eq '7a81a03577a811e5bc1e000c29cf2586' and s.agentName eq '非签约渠道' }">
										<option value="${s.id}" <c:if test="${params.agentId==s.id}">selected="selected"</c:if>>未签</option>
									</c:when>
									<c:otherwise>
										<option value="${s.id}" <c:if test="${params.agentId==s.id}">selected="selected"</c:if>>${s.agentName}</option>
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
								<option value="${jd.key }" <c:if test="${jds==jd.key}">selected="selected"</c:if>>${jd.value }</option>
							</c:forEach>
						</select>
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">打印状态：</div>
						<select name="printFlag">
							<option value="">请选择</option>
							<option value="0" <c:if test="${printFlag eq '0' }">selected="selected"</c:if>>未打印</option>
							<option value="1" <c:if test="${printFlag eq '1' }">selected="selected"</c:if>>已打印</option>
						</select>
					</div>
					<div class="kong"></div>
					<c:if test="${confirmPay eq 1 }">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">付款状态：</div>
							<select name="payStatus">
								<option value="">请选择</option>
								<option value="0"
									<c:if test="${payStatus eq '0'}">selected="selected"</c:if>>未付款</option>
								<option value="1"
									<c:if test="${payStatus eq '1'}">selected="selected"</c:if>>已付款</option>
							</select>
						</div>
					</c:if>
					<c:if test="${fns:getUser().userType ==1}">
						<input type="hidden" name="agentId"
							value="${fns:getUser().company.id}" />
					</c:if>
					
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">下单人：</div>
						<select name="creators" id="creatorrefundRebate">
							<option value="">请选择</option>
							<c:forEach var="creator" items="${creatorList }">
								<option value="${creator.key }" <c:if test="${creators==creator.key}">selected="selected"</c:if>>${creator.value }</option>
							</c:forEach>
						</select>
					</div>
					<c:if test="${orderTypes ne 11 and orderTypes ne 12 }">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">销售：</div>
							<select name="salers" id="salerrefundRebate">
								<option value="">请选择</option>
								<c:forEach var="saler" items="${salerList }">
									<option value="${saler.key }" <c:if test="${salers==saler.key}">selected="selected"</c:if>>${saler.value }</option>
								</c:forEach>
							</select>
						</div>
					</c:if>
					
					<div class="activitylist_bodyer_right_team_co2">
						<div class="activitylist_team_co3_text">付款金额：</div>
						<select id="currency" name="currency" style="width:90px;margin-bottom:0">
							<option value="">币种选择</option>
							<c:forEach items="${currencyList}" var="c">
								<option value="${c.id}"	<c:if test="${currency==c.id}">selected="selected"</c:if>>
									${c.currencyName}
								</option>
							</c:forEach>
						</select> 
						<span class="pr" style="display:inline-block;">
							<input type="text" value="${startMoney }" name="startMoney"
								id="startMoney" class="inputTxt" flag="istips"
								style="width:70px;" onkeyup="validNum(this)"
								onafterpaste="validNum(this))" />
							<span class="ipt-tips ipt-tips2">输入金额</span>
						</span> ~ 
						<span class="pr" style="display:inline-block;">
							<input type="text" value="${endMoney }" name="endMoney" id="endMoney"
								class="inputTxt" flag="istips" style="width:70px;"
								onkeyup="validNum(this)" onafterpaste="validNum(this))" />
							<span class="ipt-tips ipt-tips2">输入金额</span>
						</span>
					</div>
					<div class="kong"></div>
					<c:if test="${confirmPay eq 0 }">
						<div class="activitylist_bodyer_right_team_co1">
							<div class="activitylist_team_co3_text">已付金额：</div>
							<select id="payedMoney" name="payedMoney">
								<option value="">全部</option>
								<option value="1"
									<c:if test="${payedMoney eq 1}">selected="selected"</c:if>>未付</option>
							</select>
						</div>
					</c:if>

				</div>
			</div>
			<div class="kong"></div>
	</div>
	</form:form>

	<div class="activitylist_bodyer_right_team_co_paixu">
		<div class="activitylist_paixu">
			<div class="activitylist_paixu_left">
				<ul>
					<li	style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
					<li class="activitylist_paixu_left_biankuang licreateTime">
						<a onClick="sortby('createTime',this)">创建时间</a>
					</li>
					<li class="activitylist_paixu_left_biankuang liupdateTime">
						<a onClick="sortby('updateTime',this)">更新时间</a>
					</li>
				</ul>
			</div>
			<div class="activitylist_paixu_right">
				查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
			</div>
			<div class="kong"></div>
		</div>
	</div>

	<table id="contentTable" class="table activitylist_bodyer_table"
		style="margin-left:0px;">
		<thead style="background:#403738;">
			<tr>
				<th <c:if test="${confirmPay eq 1 }">colspan="2"</c:if> width="6%">序号</th>
				<th width="8%">申请日期</th>
				<th width="6%">团队类型</th>
				<th width="10%">团号</th>
				<th width="10%">团队名称</th>
				<th width="10%">渠道商</th>
				<th width="8%">款项</th>
				<th width="7%">付款金额<br>已付金额</th>
				<th width="8%">计调</th>
				<th width="8%">
					<c:choose>
						<c:when test="${orderTypes eq 11 or orderTypes eq 12 }">下单人</c:when>
						<c:otherwise>
							<span class="tuanhao on">销售</span>/
							<span class="chanpin">下单人</span>
						</c:otherwise>
					</c:choose>
				</th>
				<c:if test="${confirmPay eq 1 }"><th width="6%">出纳确认</th></c:if>
				<th width="6%">打印状态</th>
				<th width="6%">操作</th>
			</tr>
		</thead>
		<tbody>

			<c:if test="${fn:length(page.list) <= 0 }">
				<tr>
					<td colspan="14" style="text-align: center;">暂无搜索结果</td>
				</tr>
			</c:if>

			<c:forEach items="${page.list }" var="orders" varStatus="s">
				<tr>
					<c:if test="${confirmPay eq 1 }"><td class="tc" ><input type="checkbox" name="checkBox" value="${orders.revid }" check-action="Normal" <c:if test="${orders.payStatus eq '1' }">disabled="disabled"</c:if>/></td></c:if>
					<td class="tc">${s.count }</td>
					<td class="tc"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${orders.createtime }" /></td>
					<td class="tc">${fns:getStringOrderStatus(orders.prdtype) }</td>
					<td class="tc">${orders.groupcode }</td>
					<c:if test="${orders.prdtype !=11 && orders.prdtype !=12}">
						<td class="tc">${fns:getProductName(orders.chanpid, orders.prdtype)}</td>
					</c:if>
					<c:if test="${orders.prdtype ==11 || orders.prdtype ==12}">
						<td>${orders.chanpname}</td>
					</c:if>
					<td class="tc">${fns:getAgentName(orders.agentid) }</td>
					<td class="tc">
						<c:choose>
							<c:when test="${orders.prdtype == 6 }">签证返佣</c:when>
							<c:when test="${orders.prdtype == 7 }">机票返佣</c:when>
							<c:otherwise>${orders.costname }</c:otherwise>
						</c:choose>
					</td>
					<td class="p0 tr">
						<div class="yfje_dd">
							<span class="fbold">${fns:getCurrencyNameOrFlag(orders.currencyId, "0")}<fmt:formatNumber type="currency" currencySymbol="" value="${orders.rebatesDiff}" /></span>
						</div>
						<div class="dzje_dd">
							<span class="fbold">${fns:getRefundPayedMoney(orders.revid, "3",orders.prdtype)}</span>
						</div>
					</td>
					<td class="tc">${fns:getUserNameById(orders.jidcreateby )}</td>
					<td class="tc">
						<c:choose>
							<c:when test="${orderTypes eq 11 or orderTypes eq 12 }">${fns:getUserNameById(orders.salecreateby )}</c:when>
							<c:otherwise>
								<div class="tuanhao_cen onshow" title="${orders.salerName }">${orders.salerName }</div>
								<div class="chanpin_cen qtip" title="${fns:getUserNameById(orders.salecreateby )}">${fns:getUserNameById(orders.salecreateby )}</div>
							</c:otherwise>
						</c:choose>
						
					</td>
					<c:if test="${confirmPay eq 1 }">
						<td class="tc">
							<c:if test="${orders.payStatus eq '0' }">
								<font style="color:red">未付</font>
							</c:if>
							<c:if test="${orders.payStatus eq '1' }">
								<font style="color:green">已付</font>
							</c:if>
						</td>
					</c:if>
					<td class="tc">
						<c:if test="${empty orders.printFlag or orders.printFlag eq 0 }"><font style="color:red">未打印</font></c:if>
						<c:if test="${orders.printFlag eq 1 }"><p class="uiPrint"><font style="color:green">已打印</font><span style="display: none;color:purple;">首次打印时间<br><fmt:formatDate value="${orders.printTime}" pattern="yyyy-MM-dd HH:mm"/></span></p></c:if>
					</td>
					<td class="p0">
						<dl class="handle">
							<dt>
								<img title="操作" src="${ctxStatic }/images/handle_cz.png">
							</dt>
							<dd>
								<p>
									<span></span>
									<a href="${ctx}/cost/payManager/pay/202?orderType=${orders.prdtype}&orderId=${orders.orderid}&travelerId=${orders.travelerId}&currencyId=${orders.currencyId}&payPrice=${orders.rebatesDiff}&reviewId=${orders.revid}&agentId=${orders.agentid}" target="_blank">付款 </a>

									<c:if test="${orders.prdtype < 6 || orders.prdtype == 10}">
										<a href="${ctx }/cost/review/read/${orders.chanpid }/${orders.groupid }/0?from=operatorPre&menuid=3" target="_blank">查看</a>
									</c:if>
									<c:if test="${orders.prdtype == 6 }">
										<a href="${ctx }/cost/review/visaRead/${orders.chanpid }/0?menuid=3" target="_blank">查看</a>
									</c:if>
									<c:if test="${orders.prdtype == 7 }">
										<a href="${ctx }/cost/review/airTicketRead/${orders.chanpid }/0?menuid=3" target="_blank">查看</a>
									</c:if>
									<c:if test="${orders.prdtype == 11}">
										<a target="_blank" href="${ctx}/cost/review/hotelRead/${orders.chanpid}/${orders.groupid}/11?from=operatorPre&menuid=3">查看</a>
									</c:if>
									<c:if test="${orders.prdtype == 12}">
										<a target="_blank" href="${ctx}/cost/review/islandRead/${orders.chanpid}/${orders.groupid}/12?from=operatorPre&menuid=3">查看</a>
									</c:if>
									<c:choose>
										<c:when test="${orders.prdtype < 6 or orders.prdtype == 10 }">

											<a href="${ctx }/cost/manager/forcastList/${orders.groupid}/${orders.prdtype}"
												target="_blank">预报单</a>
											<a href="${ctx }/cost/manager/settleList/${orders.groupid}/${orders.prdtype}"
												target="_blank">结算单</a>
										</c:when>
										<c:when test="${orders.prdtype == 11 or orders.prdtype == 12}">
											<a href="${ctx }/cost/manager/forcastList/${orders.groupid}/${orders.prdtype}"
												target="_blank">预报单</a>
											<a href="${ctx }/cost/manager/settleList/${orders.groupid}/${orders.prdtype}"
												target="_blank">结算单</a>
											<a href="${ctx }/cost/manager/returnMoneyList/${orders.revid}/${orders.prdtype}"
												target="_blank">打印</a>
										</c:when>
										<c:otherwise>
											<a href="${ctx }/cost/manager/forcastList/${orders.chanpid}/${orders.prdtype}"
												target="_blank">预报单</a>
											<a href="${ctx }/cost/manager/settleList/${orders.chanpid}/${orders.prdtype}"
												target="_blank">结算单</a>
										</c:otherwise>
									</c:choose>
									<a onclick="expand('#child1_${orders.revid}_${s.count}',this,${orders.revid},'3',${s.count},${orders.prdtype})"
										href="javascript:void(0)">支付记录</a>
									<c:if test="${confirmPay eq 1 }">
										<c:choose>
											<c:when test="${orders.payStatus eq '0' }">
												<a onclick="confirmOrCannePay('${ctx }',${orders.revid},'3','1',${orders.payStatus })"
													href="javascript:void(0)">确认付款</a>
											</c:when>
											<c:otherwise>
												<a onclick="confirmOrCannePay('${ctx }',${orders.revid},'3','0',${orders.payStatus })"
													href="javascript:void(0)">撤销付款</a>
											</c:otherwise>
										</c:choose>
									</c:if>
									<!-- 取消青岛凯撒打印限制  -->
									<c:choose>
										<c:when test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype eq 6 }">
											<a  target="_blank"  href="${ctx}/review/visaRebates/visaRebatesReviewPrint?reviewId=${orders.revid}&orderId=${orders.orderid}">打印</a>
										</c:when>
										<c:when test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype eq 7 }">
											<a href="${ctx}/airticketRebates/airticketRebatesReviewPrint?reviewId=${orders.revid}&rebatesId=${orders.rid}&groupCode=${orders.groupcode}" target="_blank">打印</a><br/>
										</c:when>
										<c:when test="${(orders.revstatus == '2' or orders.revstatus == '3') and orders.prdtype ne 6 and orders.prdtype ne 7 and orders.prdtype ne 11 and orders.prdtype ne 12 }">
											<a href="${ctx}/order/rebates/review/rebatesReviewPrint?reviewId=${orders.revid}&rebatesId=${orders.id}&groupCode=${orders.groupcode}" target="_blank">打印</a><br/>
										</c:when>
									</c:choose>

								</p>
							</dd>
						</dl>
					</td>
				</tr>

				<tr id="child1_${orders.revid}_${s.count}" class="activity_team_top1"
					style="display:none">
					<td colspan="15" class="team_top">
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
	<c:if test="${confirmPay eq 1 }">
		<div id="contentTable_foot">
			<label><input check-action="All" type="checkbox"> 全选</label>
			<label><input check-action="Reverse" type="checkbox"> 反选</label>
			<input type="button" class="btn-primary" value="批量确认付款" onclick="batchConfirmOrCannePay('${ctx }','checkBox','3',1)" />
		</div>
	</c:if>
	</div>
	<div class="pagination clearFix">${page}</div>
	
</body>
</html>