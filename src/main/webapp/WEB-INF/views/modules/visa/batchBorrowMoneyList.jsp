<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>订单-签务身份订单-借款列表</title>

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css"/>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaOrderList.js"></script>
<script type="text/javascript">
$(function(){
	g_context_url = "${ctx}";
	//搜索条件显示隐藏
	launch();
	//文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	document.getElementById("jiehuzhaoliebiao").className="";
    document.getElementById("jiekuanliebiao").className="select";
	//可输入select
	$(".selectinput").comboboxSingle();
});
	
function showTravelerList(data,visaIds){
	var batchNo=(data.substring(data.indexOf('_')+1));
	$.ajax({
		cache:true,
		type:"POST",
		url:"${ctx}/visa/order/getBorrowMoneyTravelerList",
		data:{ 
			batchNo:batchNo,
			visaIds:visaIds
		},
		async:false,
		success:function(traveler){
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
				html += '<tr><td width="4%" class="p0"><span class="sqcq-fj"><input type="checkbox" name="activityId" onclick="idcheckchg(this)" trallerId="'+traveler[i].tid+'" visaId="'+traveler[i].visaId+'" value="'+traveler[i].tid+'"/>'+traveler[i].tname+'</span></td>';
// 				html += '<input type="hidden" id="passportStatus_'+traveler[i].tid+'" value="'+traveler[i].passportStatus+'">';
				html += '<td width="7%">'+traveler[i].passportCode+'<br /></td>';
				html +='<td width="7%">'+traveler[i].passportTypeDesc+'</td>';
				html +='<td width="6%">'+traveler[i].visaCountry+'</td>';
				html +='<td width="7%">'+traveler[i].startOut+'</td>';
				html +='<td width="7%">'+traveler[i].contract+'</td>';
				html +='<td width="7%">'+traveler[i].visaStatusDesc+'</td>';
				if(traveler[i].passportStatus=='4'){
					html +='<td id="visa_changtime_'+traveler[i].tid+'" width="7%">'+traveler[i].passportoperatetime+'</td>';}
				else{
					html +='<td id="visa_changtime_'+traveler[i].tid+'" width="7%"></td>';}
				html +='<td width="6%">'+traveler[i].createByName+'</td>';
				html +='<td width="3%" class="tc"><a href="javascript:void(0)" onclick="jbox_hsj_qianwu('+traveler[i].tid+');">还收据</a></td></tr>';
			}
			html += '<tr class="checkalltd"><td colspan="11" class="tl"><label><input type="checkbox" name="allChk" onclick="checkall(this,\''+batchNo+'\')">全选</label><label><input type="checkbox" name="allChkNo" onclick="checkallNo(this,\''+batchNo+'\')">反选</label><a onclick="batchHsj(\''+batchNo+'\')">批量还收据</a></td></tr>';
			//html +='</tbody>';
			$("#travelerList_"+batchNo).empty();
			$("#travelerList_"+batchNo).append(html);
		}
	});
}

//批量还收据  orderId:订单号
function batchHsj(orderId){
	//标志位 判断是否有选中
	var travellerIds ="";
	var visaIds ="";
	//游客界面
	$("#travelerList_"+orderId+" input[type=checkbox][name=activityId]").each(function(){
			if($(this).attr("checked")){
				var trallerId = $(this).attr("trallerId");
				var visaId = $(this).attr("visaId");
				var trallerName = $(this).attr("trallerName");
				 travellerIds+=trallerId+",";
				 visaIds+=visaId+",";
			}
	});
	if(travellerIds==""){
		top.$.jBox.tip("请至少选择一名游客",'warning');
		return;
	}
	 $.ajax({
		cache: true,
		type: "POST",
		url:g_context_url+ "/visa/order/checkBatchHsj",
		data:{ 
			"orderId":orderId,
			"travellerIds":travellerIds,
			"visaIds":visaIds
			},
		async: false,
		 success: function(msg){
			if(msg.msg!=null&&msg.msg!=""){
				top.$.jBox.tip(msg.msg,'warning');
			}else{
				batchHsj1(msg);
			}
		}
	});
}

//批量还护照
function returnReceiptand(orderId,visaIds,travellerIds){
	$.ajax({
		cache: true,
		type: "POST",
		url:g_context_url+ "/visa/order/checkBatchHsj",
		data:{ 
			"orderId":orderId,
			"travellerIds":travellerIds,
			"visaIds":visaIds+","
			},
		async: false,
		success: function(msg){
			if(msg.msg!=null&&msg.msg!=""){
				top.$.jBox.tip(msg.msg,'warning');
			}else{
				batchHsj1(msg);
			}
		}
	});
}

function totalbatchReturnReceiptand(){
	var visaIds ="";
	$("#totalcheck input[name='totalactivityId']").each(function(){
		if($(this).attr("checked")){
			var visaId = $(this).val();
			visaIds+=visaId+",";
		}
	});
	if(visaIds==""){
		top.$.jBox.tip('请至少选择一条数据！');
		return;
	} else {
		visaIds = visaIds.substring(0,visaIds.length-1)
	}
	returnReceiptand(null,visaIds,null);
}

function batchHsj1(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="returnReceiptWin">';
	html += '<p>不满足条件用户：</p>';
	if(errList.length==0){
		html += ' (无)';
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>签证费用</th><th>借款状态</th><th>原因</th></tr></thead><tbody>';
		for(var i = 0,len = errList.length; i<len; i++){
			html += '<tr><td>'+errList[i].tname+'</td><td>'+errList[i].passportCode+'</td><td>'+errList[i].visaFee+'</td><td>'+errList[i].jiekuanStatus+'</td><td>'+errList[i].errMsg+'</td></tr>';
		}
		html+='</tbody></table>';
	}
	html += '<p>满足条件用户：</p>';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量还收据",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:700});
	}else{
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">签证费用</th><th width="8%">借款状态</th><th width="10%">借款金额</th><th width="10%">收据金额</th><th width="13%">归还时间</th><th width="10%">还收据人</th><th width="10%">备注</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'" trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td>'+rightList[i].jiekuanStatus+'</td><td>¥'+rightList[i].jiekuanJe+'</td><td><input type="text" id="returnReceiptWinJe_'+rightList[i].visaId+'" class="rmb inputTxt" value="'+rightList[i].jiekuanJe+'"/></td><td><input type="text" disabled="disabled" onclick="WdatePicker()" id="returnReceiptWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text" disabled="disabled" id="returnReceiptWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].returnReceiptPerson+'" /></td><td><input id="returnReceiptWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
		}
		html += '</tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量还收据",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var travellerIds="";
			var  visaIds="";
			var je="";
			var dates="";
			var persons="";
			var others ="";
			var t = true;
			$("#returnReceiptWin").find("input[type=checkbox]").each(function(){
				if($(this).attr("checked")){
					var trallerId = $(this).attr("trallerId");
					var visaId = $(this).attr("visaId");
					var name= $(this).attr("trallerName");
					travellerIds+=trallerId+",";
					visaIds+=visaId+",";
					if($("#returnReceiptWinJe_"+visaId).val()==null||$("#returnReceiptWinJe_"+visaId).val()==""){
						top.$.jBox.tip(name+"对应的收据金额不能为空",'warning');
						t=  false;
					}
					je+=$("#returnReceiptWinJe_"+visaId).val()+",";
					dates+=$("#returnReceiptWinDate_"+visaId).val()+",";
					persons+=$("#returnReceiptWinPerson_"+visaId).val()+",";
					others+=$("#returnReceiptWinOther_"+visaId).val()+" "+",";
				}
			});

			if(travellerIds==""){
				top.$.jBox.tip("请勾选对应的游客",'warning');
				return false;
			}

			if(!t){
				return false;
			}
			$.ajax({
				cache: true,
				type: "POST",
				//url:g_context_url+ "/visa/order/batchHsj",   
				url:g_context_url+ "/visa/workflow/returnreceipt/createVisaBatchHsj",
				data:{
					"travellerIds":travellerIds,
					"visaIds":visaIds,
					"returnReceiptJe":je,
					"returnReceiptName":persons,
					"returnReceiptTime":dates,
					"returnReceiptRemark":others},
					async: false,
					success: function(msg){
					if(msg.msg!=null&&msg.msg!=""){
						top.$.jBox.tip(msg.msg,'warning');
					}else{
						top.$.jBox.tip("操作成功",'warning');
						var travellArray = travellerIds.split(",");
						for(var i =0;i<travellArray.length;i++){
							var travelerId = travellArray[i];
							if(travelerId!=null&&travelerId!=""){
								$("#hidden_passportStatus_"+travelerId).val(1);
								$("#passportStatus_"+travelerId+" option").eq(0).attr("selected",true);
							}
						}
					}
				}
			});
			return true;
		}
		},height:'auto',width:700});
	}
}

function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}

function expand(batchNo,obj,visaIds){
	if($("#child_"+batchNo).css("display")=="none"){
		showTravelerList("#child_"+batchNo,visaIds);
		$(obj).parents("tr").addClass("tr-hover");
		$(obj).addClass('team_a_click2');
		$("#child_"+batchNo).show();
		$(obj).parents("td").addClass("td-extend");
	}else{
		$("#child_"+batchNo).hide();
		$(obj).parents("td").removeClass("td-extend");
		$(obj).parents("tr").removeClass("tr-hover");
		$(obj).removeClass('team_a_click2');
	}
}

function resetSearchParams(){
	$(':input','#searchForm')
	.not(':button, :submit, :reset, :hidden')
	.val('')
	.removeAttr('checked')
	.removeAttr('selected');
}

//获取当前时间
function getCurDate(){
	var curDate = new Date();
	return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
}

//全选
//var activityIds = "";
function checkall(obj,batchNo){
	if(obj.checked){
		$("#travelerList_"+batchNo+" input[name='activityId']").not("input:checked").each(function(){this.checked=true;});
		$("#travelerList_"+batchNo+" input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
	}else{ 
		$("#travelerList_"+batchNo+" input[name='activityId']:checked").each(function(){this.checked=false;});
		$("#travelerList_"+batchNo+" input[name='allChk']:checked").each(function(){this.checked=false;});
	}
}

function checkallNo(obj,batchNo){
	$("#travelerList_"+batchNo+" input[name='activityId']").each(function(){
		$(this).attr("checked", !$(this).attr("checked"));
	});
	allchk();
}

//每行中的复选框
function idcheckchg(obj){
	if(obj.checked){
		if($("input[name='activityId']").not("input:checked").length == 0){
			$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
		}
	}else{
		$("input[name='allChk']:checked").each(function(){
			this.checked=false;	
		})
	}
}

function allchk(){ 
	var chknum = $("input[name='activityId']").size();
	var chk = 0; 
	$("input[name='activityId']").each(function (){
		if($(this).attr("checked")==true){
			chk++;
		}
	});
	if(chknum==chk){//全选 
		$("input[name='allChk']").attr("checked",true);
	}else{//不全选 
		$("input[name='allChk']").attr("checked",false);
	}
}

function totalcheckall(obj){
	if(obj.checked){ 
		$("#totalcheck input[name='totalactivityId']").not("input:checked").each(function(){this.checked=true;}); 
		$("#totalcheck input[name='totalallChk']").not("input:checked").each(function(){this.checked=true;});
	}else{ 
		$("#totalcheck input[name='totalactivityId']:checked").each(function(){this.checked=false;}); 
		$("#totalcheck input[name='totalallChk']:checked").each(function(){this.checked=false;});	
	}
}

function totalcheckallNo(obj){
	$("#totalcheck input[name='totalactivityId']").each(function () {
		$(this).attr("checked", !$(this).attr("checked"));
	}); 
	totalallchk();
}

function totalallchk(){ 
	var chknum = $("input[name='totalactivityId']").size();
	var chk = 0; 
	$("input[name='totalactivityId']").each(function () {
		if($(this).attr("checked")==true){
			chk++;
		}
	});
	if(chknum==chk){//全选 
		$("input[name='totalallChk']").attr("checked",true);
	}else{//不全选 
		$("input[name='totalallChk']").attr("checked",false);
	}
}

function cantuansortby(sortBy,obj){
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
	$("#searchForm").attr("action","${ctx}/visa/order/batchBorrowMoneyList");
	$("#orderBy").val(sortBy);
	$("#searchForm").submit();
}
</script>
</head>
<body>
	<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
		<form method="post" action="${ctx}/visa/order/batchBorrowMoneyList" id="searchForm">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
			<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">批次编号：</div>
						<input value="${batchNo}" class="inputTxt" name="batchNo" id="">
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">操作人：</div>
						<input value="${txnPerson}" class="inputTxt" name="txnPerson" id="txnPerson">
					</div>
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">记录时间：</label>
						<input readonly="" onclick="WdatePicker()" value="${createDateStart}" name="createDateStart" class="inputTxt dateinput" id="createDateStart"> 
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker()" value="${createDateEnd}" name="createDateEnd" class="inputTxt dateinput" id="createDateEnd">
					</div>
						<div class="kong"></div>
						<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">游客名称：</div>
						<input value="${travellerName}" name="travellerName" class="inputTxt" id="travellerName"> 
					</div>
				<div class="form_submit">
					<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
					<input type="button" value="条件重置" onclick="resetSearchParams()" class="btn btn-primary ydbz_x">
				</div>
			</div>
		</form>
		<div class="supplierLine">
						<a  href="${ctx }/visa/order/batchBorrowMoneyList"  id="jiekuanliebiao">借款列表</a>
						<a  href="${ctx }/visa/order/batchReturnPassportList" id="jiehuzhaoliebiao">借护照列表</a>
		</div>
		<!--查询结果筛选条件排序开始-->
		<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>
							<li >排序</li>
							<c:choose>
								<c:when test="${page.orderBy == 'u.createDate DESC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="cantuansortby('u.createDate',this)">
											创建时间
											<i class="icon icon-arrow-down"></i>
										</a>
									</li>
								</c:when>
								<c:when test="${page.orderBy == 'u.createDate ASC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="cantuansortby('u.createDate',this)">
											创建时间
											<i class="icon icon-arrow-up"></i>
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="activitylist_paixu_left_biankuang lipro.updateDate">
										<a onclick="cantuansortby('u.createDate',this)">
											创建时间
										</a>
									</li>
								</c:otherwise>
							</c:choose>
						</ul>
					</div>
				<div class="activitylist_paixu_right">
				<c:choose>
					<c:when test="${page.count >0}">
					查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
					</c:when>
					<c:otherwise>
					查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
					</c:otherwise>
				</c:choose>
				</div>
					<div class="kong"></div>
				</div>
			</div>
		<!--查询结果筛选条件排序结束-->	 
		<table class="table activitylist_bodyer_table" id="contentTable">
			<thead>
			<tr>
				<th width="10%">批次</th>
				<th width="10%">操作人</th>
				<th width="12%">记录时间</th>
				<th width="12%">借款金额</th>
				<th width="10%">操作</th>
			</tr>
			</thead>
			<tbody id="totalcheck" >
				<c:if test="${fn:length(page.list) <= 0 }">
					<tr class="toptr" >
						<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
					</tr>
				</c:if>
				<c:forEach items="${page.list }" var="record">
				<tr>
					<%-- <td>${record.batchNo }</td> --%>
					<c:choose>
						<c:when test="${record.batchNo != null && record.batchNo !='' }">
							<td ><input type="checkbox" name="totalactivityId" onclick="idcheckchg(this)" value="${record.visaIds }"/>${record.batchNo }</td>
						</c:when>
						<c:otherwise>
							<td ><input type="checkbox" name="totalactivityId" onclick="idcheckchg(this)" value="${record.visaIds }"/>单借</td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${record.batchNo != null && record.batchNo !='' }">
							<td class="tc">${record.txnPerson }</td>
						</c:when>
						<c:otherwise>
							<td class="tc"></td>
						</c:otherwise>
					</c:choose>
					<td class="tc"><fmt:formatDate value="${record.createDate }" pattern="yyyy-MM-dd"/></td>
					<td class="tc">¥ <fmt:formatNumber type="currency" pattern="#,##0.00" value="${record.borrowAmount}" /></td>
					<td class="tc tda">
						<a class="team_a_click" onclick="expand('${record.batchNo }',this,'${record.visaIds }')" href="javascript:void(0)">游客列表</a>
						<a onclick="returnReceiptand(null,'${record.visaIds}',null);"href="javascript:void(0)">还收据</a>
					</td>								
				</tr>
				<tr id="child_${record.batchNo }" class="activity_team_top1" style="display:none">
					<td colspan="5" class="team_top">
						<table class="table activitylist_bodyer_table" style="margin:0 auto;">
							<thead>
								<tr>
									<th width="4%">姓名</th>
									<th width="7%">护照号</th>
									<th width="7%">签证类别</th>
									<th width="6%">签证国家</th>
									<th width="7%">实际出团时间</th>
									<th width="7%">实际约签时间</th>
									<th width="7%">签证状态</th>
									<th width="7%">护照归还时间</th>
									<th width="6%">借款人</th>
									<th width="3%" class="tc">操作</th>
								</tr>
							</thead>
						</table>
						<div class="table_activity_scroll">
							<table class="table activitylist_bodyer_table ">
								<tbody id="travelerList_${record.batchNo }">
								</tbody>
							</table>
						</div> 
					</td>
				</tr>
				</c:forEach>
				<c:if test="${fn:length(page.list) > 0 }">
					<tr class="checkalltd"><td colspan="11" class="tl"><label><input type="checkbox" name="totalallChk" onclick="totalcheckall(this)">全选</label><label><input type="checkbox" name="allChkNo" onclick="totalcheckallNo(this)">反选</label><a onclick="totalbatchReturnReceiptand()">批量还收据</a></td></tr>
				</c:if>
			</tbody>
		</table>
	</div>
	<div class="pagination clearFix">${pageStr}</div>
</body>
</html>