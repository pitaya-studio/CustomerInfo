<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<title>订单-签证-签务身份订单-批量批次展示列表</title>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<%--<link href="css/bootstrap.min.css" type="text/css" rel="stylesheet" />--%>
<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script type="text/javascript" src="${ctxStatic}/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript">
$(function(){
	//搜索条件显示隐藏
	launch();
	//文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	show('${showList}','${saveOrSubmit}');
	//可输入select
	$(".selectinput" ).comboboxSingle();
	var _$orderBy = $("#orderBygua").val();
	if(_$orderBy==""){
		_$orderBy="createTime DESC";
		$("#orderBygua").val("createTime DESC");
	}
	var orderBy = _$orderBy.split(" ");
	$(".activitylist_paixu_left li").each(function(){
		if ($(this).hasClass("li"+orderBy[0])){
			orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="ASC"?"up":"down";
			$(this).find("a").eq(0).html($(this).find("a").eq(0).html()+"<i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
			$(this).attr("class","activitylist_paixu_moren");
		}
	});
});

function page(n,s){
	if("jiekuan"==$("#showList").val()){
		$("#pageNoJk").val(n);
		$("#pageSizeJk").val(s);
		$("#searchFormJk").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
		$("#searchFormJk").submit();
	} else if("huanshouju"==$("#showListHsj").val()){
		$("#pageNoHsj").val(n);
		$("#pageSizeHsj").val(s);
		$("#searchFormHsj").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
		$("#searchFormHsj").submit();
	}
	else if("jiehuzhao"==$("#showListjhz").val()){
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#pageNo_ytj").val(n);
		$("#pageSize_ytj").val(s);
		$("#searchFormjhz").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
		$("#searchFormjhz").submit();
	}
	
	//需求号:0065&0099  addby:djw
	else if("mianqiantongzhi"==$("#showListMqtz").val()){
		$("#pageNoMqtz").val(n);
		$("#pageSizeMqtz").val(s);
		$("#searchFormMqtz").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
		$("#searchFormMqtz").submit();
	}
	else if("guarantee"==$("#showListGua").val()){
		$("#pageNogua").val(n);
		$("#pageSizegua").val(s);

		//TODO:
		$("#searchForGua").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
		$("#searchForGua").submit();
	}
	else{
		$("#pageNohhz").val(n);
		$("#pageSizehhz").val(s);
		$("#pageNohhz_ytj").val(n);
		$("#pageSizehhz_ytj").val(s);
		$("#searchFormhhz").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
		$("#searchFormhhz").submit();
	}
}

//展开筛选按钮
function launch(){
	$('.zksx').click(function() {
		if($('.ydxbd').is(":hidden")==true) {
			$('.ydxbd').show();
			$('.zksx').text('收起筛选');
			$('.zksx').addClass('zksx-on');
		}else{
			$('.ydxbd').hide();
			$('.zksx').text('展开筛选');
			$('.zksx').removeClass('zksx-on');
		}
	});
}

function showJkTravelerList(data,type){
	var batchNo=data.substring(data.indexOf('_')+1);
	$.ajax({
		cache:true,
		type:"POST",
		url:"${ctx}/visa/order/getBatchJkTravelerList",
		data:{ 
			batchNo:batchNo
		},
		async:false,
		success:function(traveler){
			//批发商的uuid
			var companyUuid='${fns:getUser().company.uuid }';
			//alert("------"+(companyUuid=='7a816f5077a811e5bc1e000c29cf2586'));
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
				html += '<tr><td width="4%" class="tc"><span class="sqcq-fj">'+traveler[i].tname+' </span></td>';
				//C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-签务签证订单->批量操作记录->批量借款->待提交/已提交->游客列表->展开游客//
                   if('7a816f5077a811e5bc1e000c29cf2586'==companyUuid){ //环球行
                	   html += '<td width="7%" class="tc">'+traveler[i].orderTuanhaoFromVOTable+'<br /></td>'; 
                   }else{ //非环球行
                	 //C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e//
       				html += '<td width="7%" class="tc">'+traveler[i].groupCode+'<br /></td>'; 
                   }
				
				html +='<td width="7%" class="tc">'+traveler[i].orderNo+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].visaTypeDesc+'</td>';
				html +='<td width="6%" class="tc">'+traveler[i].visaCountry+'</td>';		 	
				html +='<td width="7%" class="tc">'+traveler[i].startOut+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].contract+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].visaStatusDesc+'</td>';
				html +='<td width="6%" class="tc">￥'+traveler[i].travelerBorrowMoney+'</td>';
				html +='<td width="6%" class="tc">'+traveler[i].createByName+'</td>';
				html +='<td width="6%" class="tc"><a href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId='+traveler[i].orderId+'&details=1" target="_blank">查看</a>';
				if (type=="submit") {
					html +='</td></tr>';
				}else{
					html +='<a href="javascript:void(0)">|</a> <a href="javascript:void(0)" onClick="jkTravelerDelete(\''+batchNo+'\', \''+traveler[i].tid+'\')">删除</a></td></tr> ';
				}
			}
			if (type=="save") {
				$("#travelerListJkDtj_"+batchNo).empty();
				$("#travelerListJkDtj_"+batchNo).append(html);
			} else if (type=="submit") {
				$("#travelerListJkYtj_"+batchNo).empty();
				$("#travelerListJkYtj_"+batchNo).append(html);
			}
		}
	});
}

function showGuaTravelerList(data){
	var batchNo = data.substring(data.indexOf('_')+1);
	$.ajax({
		cache:true,
		type:"POST",
		url:"${ctx}/visa/order/getBatchGuaTravelerList",
		data:{
			batchNo:batchNo
		},
		async:false,
		success:function(traveler){
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
				var guaranteeName = "";
				if (traveler[i].guaranteeStatus == 1) {
					guaranteeName = "担保";
				} else if (traveler[i].guaranteeStatus == 2) {
					guaranteeName = "担保+押金";
				} else if (traveler[i].guaranteeStatus == 3) {
					guaranteeName = "押金";
				} else if(traveler[i].guaranteeStatus ==4) {
					guaranteeName = "无需担保";
				}
				html += '<tr>' +
					'<td class="tc">'+traveler[i].salerName+'</td>'+
					'<td class="tc">'+traveler[i].travelerName+'</td>'+
					'<td class="tc">'+traveler[i].passportId+'</td>'+
					'<td class="tc">'+traveler[i].visaCountry+'</td>'+
					'<td class="tc">'+traveler[i].collarZoning+'</td>'+
					'<td class="tc">'+guaranteeName+'</td>'+
					'<td class="tc">'+traveler[i].totalDeposit+'</td>'+
					'<td class="tc"><a href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId='+traveler[i].visaorderId+'&details=1" target="_blank">查看</a></td>'+
					'</tr>';
			}

			$("#travelerGuaList_"+batchNo).empty();
			$("#travelerGuaList_"+batchNo).append(html);

		}
	});
}

function showHsjTravelerList(data,type){
	var batchNo=data.substring(data.indexOf('_')+1);
	$.ajax({
		cache:true,
		type:"POST",
		url:"${ctx}/visa/order/getBatchHsjTravelerList",
		data:{ 
			batchNo:batchNo
		},
		async:false,
		success:function(traveler){
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
				html += '<tr><td width="4%" class="tc"><span class="sqcq-fj">'+traveler[i].tname+' </span></td>';
				html += '<td width="7%" class="tc">'+traveler[i].groupCode+'<br /></td>';
				html +='<td width="7%" class="tc">'+traveler[i].orderNo+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].visaTypeDesc+'</td>';
				html +='<td width="6%" class="tc">'+traveler[i].visaCountry+'</td>';		 	
				html +='<td width="7%" class="tc">'+traveler[i].startOut+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].contract+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].visaStatusDesc+'</td>';
				html +='<td width="6%" class="tc">￥'+traveler[i].travelerBorrowMoney+'</td>';
				html +='<td width="6%" class="tc">'+traveler[i].createByName+'</td>';
				html +='<td width="6%" class="tc"><a href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId='+traveler[i].orderId+'&details=1" target="_blank">查看</a>';
				if (type=="submit") {
					html +='</td></tr>';
				}else{
					html +='<a href="javascript:void(0)">|</a> <a href="javascript:void(0)" onClick="hsjTravelerDelete(\''+batchNo+'\', \''+traveler[i].tid+'\')">删除</a></td></tr> ';
				}
			}
			if (type=="save") {
				$("#travelerListHsjDtj_"+batchNo).empty();
				$("#travelerListHsjDtj_"+batchNo).append(html);
			} else if (type=="submit") {
				$("#travelerListHsjYtj_"+batchNo).empty();
				$("#travelerListHsjYtj_"+batchNo).append(html);
			}
		}
	});
}

function showTravelerList(data,visaIds,type){
	if (visaIds==""||visaIds==null){
		return false;
	}
	var batchNo=(data.substring(data.indexOf('_')+1));
	$.ajax({
		cache:true,
		type:"POST",
		url:"${ctx}/visa/order/getBorrowPassPortTravelerList",
		data:{ 
			batchNo:batchNo,
			visaIds:visaIds
		},
		async:false,
		success:function(traveler){
			//批发商的uuid
			var companyUuid='${fns:getUser().company.uuid }';
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
			 	html += '<tr id="shanchu_'+traveler[i].tid+batchNo+'"><td width="4%" class="tc"><span class="sqcq-fj">'+traveler[i].tname+' </span></td>';
			    html += '<input type="hidden" id="passportStatus_'+traveler[i].tid+'" value="'+traveler[i].passportStatus+'">';
			 	
			    /* html += '<td width="7%" class="tc">'+traveler[i].orderTuanhao+'<br /></td>'; */
			  //C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-签务签证订单->批量操作记录->批量借护照->待提交/已提交->游客列表->展开游客//
			    if('7a816f5077a811e5bc1e000c29cf2586'==companyUuid){ //环球行
			          html += '<td width="7%" class="tc">'+traveler[i].orderTuanhaoFromVOTable+'<br /></td>'; 
			     }else{ //非环球行
			            //C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e//
			           	html += '<td width="7%" class="tc">'+traveler[i].orderTuanhao+'<br /></td>'; 
			            }
			    
			    html +='<td width="7%" class="tc">'+traveler[i].passportCode+'</td>';
			 	html +='<td width="7%" class="tc">'+traveler[i].passportTypeDesc+'</td>';
			 	html +='<td width="6%" class="tc">'+traveler[i].visaCountry+'</td>';		 	
			    html +='<td width="7%" class="tc">'+traveler[i].startOut+'</td>';
			    html +='<td width="7%" class="tc">'+traveler[i].contract+'</td>';
			    html +='<td width="7%" class="tc">'+traveler[i].visaStatusDesc+'</td>';
			 	html +='<td width="6%" class="tc">'+traveler[i].passportoperator+'</td>';
			 	html +='<td width="3%" class="tc"><a href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId='+traveler[i].orderId+'&details=1" target="_blank">查看</a>';
			 	if(type=="submit"){
			 		html +='</td></tr>';
			 	}else{
			 		html +='<a href="javascript:void(0)">|</a> <a href="javascript:void(0)" onClick="travelerDelete(\''+batchNo+'\', \''+traveler[i].tid+'\')">删除</a></td></tr> ';
			 	}
			}
			 $("#travelerList_"+batchNo).empty();
			 $("#travelerList_"+batchNo).append(html);
		}
	});
}


function show(showList,saveOrSubmit){
	if("jiekuan" ==showList){
		$("#showList").val("jiekuan");
		document.getElementById("batchJk").className="select";
		document.getElementById("batchMqtz").className="";
		document.getElementById("batchHsj").className="";
		document.getElementById("batchJhz").className="";
		document.getElementById("batchHhz").className="";
		document.getElementById("jiekuan").style.display='block';
		document.getElementById("mianqiantongzhi").style.display='none';
		document.getElementById("huanshouju").style.display='none';
		document.getElementById("jiehuzhao").style.display='none';
		document.getElementById("huanhuzhao").style.display='none';
		if("save" ==saveOrSubmit) {
			$("#saveOrSubmit").val("save");
			document.getElementById("jkdtj").className="select";
			document.getElementById("jkytj").className="";
			document.getElementById("jkdaitijiao").style.display='block';
			document.getElementById("jkyitijiao").style.display='none';
		} else if ("submit" ==saveOrSubmit) {
			$("#saveOrSubmit").val("submit");
			document.getElementById("jkdtj").className="";
			document.getElementById("jkytj").className="select";
			document.getElementById("jkdaitijiao").style.display='none';
			document.getElementById("jkyitijiao").style.display='block';
		}
	}else if("huanshouju" ==showList){
		$("#showListHsj").val("huanshouju");
		document.getElementById("batchJk").className="";
		document.getElementById("batchMqtz").className="";
		document.getElementById("batchHsj").className="select";
		document.getElementById("batchJhz").className="";
		document.getElementById("batchHhz").className="";
		document.getElementById("jiekuan").style.display='none';
		document.getElementById("mianqiantongzhi").style.display='none';
		document.getElementById("huanshouju").style.display='block';
		document.getElementById("jiehuzhao").style.display='none';
		document.getElementById("huanhuzhao").style.display='none';
		if("save" ==saveOrSubmit) {
			$("#saveOrSubmitHsj").val("save");
			document.getElementById("hsjdtj").className="select";
			document.getElementById("hsjytj").className="";
			document.getElementById("hsjdaitijiao").style.display='block';
			document.getElementById("hsjyitijiao").style.display='none';
		} else if ("submit" ==saveOrSubmit) {
			$("#saveOrSubmitHsj").val("submit");
			document.getElementById("hsjdtj").className="";
			document.getElementById("hsjytj").className="select";
			document.getElementById("hsjdaitijiao").style.display='none';
			document.getElementById("hsjyitijiao").style.display='block';
		}
	}else if("jiehuzhao" ==showList){
		$("#showListjhz").val("jiehuzhao");
		document.getElementById("batchJk").className="";
		document.getElementById("batchMqtz").className="";
		document.getElementById("batchHsj").className="";
		document.getElementById("batchJhz").className="select";
		document.getElementById("batchHhz").className="";
		document.getElementById("jiekuan").style.display='none';
		document.getElementById("mianqiantongzhi").style.display='none';
		document.getElementById("huanshouju").style.display='none';
		document.getElementById("jiehuzhao").style.display='block';
		document.getElementById("huanhuzhao").style.display='none';
		if("save" ==saveOrSubmit){
			$("#saveOrSubmitjhz").val("save");
			document.getElementById("jhzdtj").className="select";
			document.getElementById("jhzytj").className="";
			document.getElementById("jhzdaitijiao").style.display='block';
			document.getElementById("jhzyitijiao").style.display='none';
		} else if ("submit" ==saveOrSubmit) {
			$("#saveOrSubmitjhz").val("submit");
			document.getElementById("jhzdtj").className="";
			document.getElementById("jhzytj").className="select";
			document.getElementById("jhzdaitijiao").style.display='none';
			document.getElementById("jhzyitijiao").style.display='block';
		}	
	}else if("huanhuzhao" ==showList){
		$("#showListhhz").val("huanhuzhao");
		document.getElementById("batchJk").className="";
		document.getElementById("batchMqtz").className="";
		document.getElementById("batchHsj").className="";
		document.getElementById("batchJhz").className="";
		document.getElementById("batchHhz").className="select";
		document.getElementById("jiekuan").style.display='none';
		document.getElementById("mianqiantongzhi").style.display='none';
		document.getElementById("huanshouju").style.display='none';
		document.getElementById("jiehuzhao").style.display='none';
		document.getElementById("huanhuzhao").style.display='block';
		if("save" ==saveOrSubmit){
			$("#saveOrSubmithhz").val("save");
			document.getElementById("hhzdtj").className="select";
			document.getElementById("hhzytj").className="";
			document.getElementById("hhzdaitijiao").style.display='block';
			document.getElementById("hhzyitijiao").style.display='none';
		}else if("submit" ==saveOrSubmit){
			$("#saveOrSubmithhz").val("submit");
			document.getElementById("hhzdtj").className="";
			document.getElementById("hhzytj").className="select";
			document.getElementById("hhzdaitijiao").style.display='none';
			document.getElementById("hhzyitijiao").style.display='block';
		}
	}else if("mianqiantongzhi" ==showList){   //需求号:0065&0099  addby:djw
		$("#showListMqtz").val("mianqiantongzhi");
		document.getElementById("batchJk").className="";
		document.getElementById("batchHsj").className="";
		document.getElementById("batchJhz").className="";
		document.getElementById("batchHhz").className="";
		document.getElementById("batchMqtz").className="select";
		document.getElementById("jiekuan").style.display='none';
		document.getElementById("mianqiantongzhi").style.display='block';
		document.getElementById("huanshouju").style.display='none';
		document.getElementById("jiehuzhao").style.display='none';
		document.getElementById("huanhuzhao").style.display='none';
	}else if("guarantee" == showList){
		$("#showListGua").val("guarantee");
		$('#batchSgt').addClass('select');
		$('#batchSgt').siblings().removeClass('select');
		$('#jiekuan').hide();
		$('#huanshouju').hide();
		$('#jiehuzhao').hide();
		$('#huanhuzhao').hide();
		$('#setGuaranteeType').show();
//	$('#mianqiantongzhi').hide();
		document.getElementById("mianqiantongzhi").style.display='none';

	}
}

function expandJk(batchNo,obj,type){
	if (type=="save") {
		if($("#childJkDtj_"+batchNo).css("display")=="none"){
			showJkTravelerList("#childJkDtj_"+batchNo,type);
			$(obj).parents("tr").addClass("tr-hover");
			$(obj).addClass('team_a_click2');
			$("#childJkDtj_"+batchNo).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$("#childJkDtj_"+batchNo).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
			$(obj).removeClass('team_a_click2');
		}
	} else if (type=="submit") {
		if($("#childJkYtj_"+batchNo).css("display")=="none"){
			showJkTravelerList("#childJkYtj_"+batchNo,type);
			$(obj).parents("tr").addClass("tr-hover");
			$(obj).addClass('team_a_click2');
			$("#childJkYtj_"+batchNo).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$("#childJkYtj_"+batchNo).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
			$(obj).removeClass('team_a_click2');
		}
	}
}

function expandTravelers(batchNo,obj) {
	if($("#child_"+batchNo).css("display")=="none"){
		showGuaTravelerList("#child_"+batchNo);
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

function expandHsj(batchNo,obj,type){
	if (type=="save") {
		if($("#childHsjDtj_"+batchNo).css("display")=="none"){
			showHsjTravelerList("#childHsjDtj_"+batchNo,type);
			$(obj).parents("tr").addClass("tr-hover");
			$(obj).addClass('team_a_click2');
			$("#childHsjDtj_"+batchNo).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$("#childHsjDtj_"+batchNo).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
			$(obj).removeClass('team_a_click2');
		}
	} else if (type=="submit") {
		if($("#childHsjYtj_"+batchNo).css("display")=="none"){
			showHsjTravelerList("#childHsjYtj_"+batchNo,type);
			$(obj).parents("tr").addClass("tr-hover");
			$(obj).addClass('team_a_click2');
			$("#childHsjYtj_"+batchNo).show();
			$(obj).parents("td").addClass("td-extend");
		}else{
			$("#childHsjYtj_"+batchNo).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).parents("tr").removeClass("tr-hover");
			$(obj).removeClass('team_a_click2');
		}
	}
}

function expand(batchNo,obj,visaIds,type){
	if($("#child_"+batchNo).css("display")=="none"){
		//展开游客
		showTravelerList("#child_"+batchNo,visaIds,type);
		
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
var resetSearchParamsJk = function(){
	$(':input','#searchFormJk')
	.not(':button, :submit, :reset, :hidden')
	.val('')
	.removeAttr('checked')
	.removeAttr('selected');
};
var resetSearchParamsHsj = function(){
    $(':input','#searchFormHsj')
     .not(':button, :submit, :reset, :hidden')
     .val('')
     .removeAttr('checked')
     .removeAttr('selected');
};
var resetSearchParamsjhz = function(){
    $(':input','#searchFormjhz')
     .not(':button, :submit, :reset, :hidden')
     .val('')
     .removeAttr('checked')
     .removeAttr('selected');
};
var resetSearchParamshhz = function(){
    $(':input','#searchFormhhz')
     .not(':button, :submit, :reset, :hidden')
     .val('')
     .removeAttr('checked')
     .removeAttr('selected');
};
var resetSearchParamsgua = function(){
	$(':input','#searchForGua')
			.not(':button, :submit, :reset, :hidden')
			.val('')
			.removeAttr('checked')
			.removeAttr('selected');
};
//获取当前时间
function getCurDate(){
	var curDate = new Date();
	return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
}
//全选
//var activityIds = "";
function checkall(obj){
	if(obj.checked){ 
		$("input[name='activityId']").not("input:checked").each(function(){this.checked=true;}); 
		$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
	}else{ 
		$("input[name='activityId']:checked").each(function(){this.checked=false;}); 
		$("input[name='allChk']:checked").each(function(){this.checked=false;});	
	} 
}
function checkallNo(obj){
	$("input[name='activityId']").each(function () {   
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
	$("input[name='activityId']").each(function () {
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

function showJk(){
	$("#showList").val("jiekuan");
	document.getElementById("batchJk").className="select";
	document.getElementById("batchHsj").className="";
	document.getElementById("batchMqtz").className="";
	document.getElementById("batchJhz").className="";
	document.getElementById("batchHhz").className="";
	document.getElementById("jiekuan").style.display='block';
	document.getElementById("huanshouju").style.display='none';
	document.getElementById("mianqiantongzhi").style.display='none';
	document.getElementById("jiehuzhao").style.display='none';
	document.getElementById("huanhuzhao").style.display='none';
	$("#saveOrSubmit").val("save");
	$('#setGuaranteeType').hide();
	document.getElementById("jkdtj").className="select";
	document.getElementById("jkytj").className="";
	document.getElementById("jkdaitijiao").style.display='block';
	document.getElementById("jkyitijiao").style.display='none';
	$("#pageNoJk").val(1);
	$("#pageSizeJk").val(10);
	$("#searchFormJk").submit();
}

function showHsj(){
	$("#showListHsj").val("huanshouju");
	document.getElementById("batchJk").className="";
	document.getElementById("batchMqtz").className="";
	document.getElementById("batchHsj").className="select";
	document.getElementById("batchJhz").className="";
	document.getElementById("batchHhz").className="";
	document.getElementById("jiekuan").style.display='none';
	document.getElementById("mianqiantongzhi").style.display='none';
	document.getElementById("huanshouju").style.display='block';
	document.getElementById("jiehuzhao").style.display='none';
	document.getElementById("huanhuzhao").style.display='none';
	$("#saveOrSubmitHsj").val("save");
	document.getElementById("hsjdtj").className="select";
	document.getElementById("hsjytj").className="";
	document.getElementById("hsjdaitijiao").style.display='block';
	document.getElementById("hsjyitijiao").style.display='none';
	$('#setGuaranteeType').hide();
	$("#pageNoHsj").val(1);
	$("#pageSizeHsj").val(10);
	$("#searchFormHsj").submit();
}

function showJhz(){
	$("#showListjhz").val("jiehuzhao");
	document.getElementById("batchJk").className="";
	document.getElementById("batchMqtz").className="";
	document.getElementById("batchHsj").className="";
	document.getElementById("batchJhz").className="select";
	document.getElementById("batchHhz").className="";
	document.getElementById("jiekuan").style.display='none';
	document.getElementById("mianqiantongzhi").style.display='none';
	document.getElementById("huanshouju").style.display='none';
	document.getElementById("jiehuzhao").style.display='block';
	document.getElementById("huanhuzhao").style.display='none';
	$("#saveOrSubmitjhz").val("save");
	document.getElementById("jhzdtj").className="select";
	document.getElementById("jhzytj").className="";
	document.getElementById("jhzdaitijiao").style.display='block';
	document.getElementById("jhzyitijiao").style.display='none';
	$('#setGuaranteeType').hide();
	$("#pageNo").val(1);
	$("#pageSize").val(10);
	$("#searchFormjhz").submit();
}

function showHhz(){
	$("#showListhhz").val("huanhuzhao");
	document.getElementById("batchJk").className="";
	document.getElementById("batchMqtz").className="";
	document.getElementById("batchHsj").className="";
	document.getElementById("batchJhz").className="";
	document.getElementById("batchHhz").className="select";
	document.getElementById("jiekuan").style.display='none';
	document.getElementById("mianqiantongzhi").style.display='none';
	document.getElementById("huanshouju").style.display='none';
	document.getElementById("jiehuzhao").style.display='none';
	document.getElementById("huanhuzhao").style.display='block';
	$("#saveOrSubmithhz").val("save");
	document.getElementById("hhzdtj").className="select";
	document.getElementById("hhzytj").className="";
	document.getElementById("hhzdaitijiao").style.display='block';
	document.getElementById("hhzyitijiao").style.display='none';
	$('#setGuaranteeType').hide();
	$("#pageNohhz").val(1);
	$("#pageSizehhz").val(10);
	$("#searchFormhhz").submit();
}

//66 START
function showSgt() {
	$("#showListGua").val("guarantee");
	$('#batchSgt').addClass('select');
	$('#batchSgt').siblings().removeClass('select');
	$('#jiekuan').hide();
	$('#huanshouju').hide();
	$('#jiehuzhao').hide();
	$('#huanhuzhao').hide();
	$('#setGuaranteeType').show();
//	$('#mianqiantongzhi').hide();
	document.getElementById("mianqiantongzhi").style.display='none';
	$("#pageNo").val(1);
	$("#pageSize").val(10);
    $("#searchForGua").submit();
}
//66 START

function showJkDtj(){
	
	$("#showList").val("jiekuan");
	$("#saveOrSubmit").val("save");
	document.getElementById("jkdtj").className="select";
	document.getElementById("jkytj").className="";
	document.getElementById("jkdaitijiao").style.display='block';
	document.getElementById("jkyitijiao").style.display='none';
	$("#pageNoJk").val(1);
	$("#pageSizeJk").val(10);
	$("#searchFormJk").submit();
	
}

function showJkYtj(){
	
	$("#showList").val("jiekuan");
	$("#saveOrSubmit").val("submit");
	document.getElementById("jkdtj").className="";
	document.getElementById("jkytj").className="select";
	document.getElementById("jkdaitijiao").style.display='none';
	document.getElementById("jkyitijiao").style.display='block';
	$("#pageNoJk").val(1);
	$("#pageSizeJk").val(10);
	$("#searchFormJk").submit();
	
}

function showHsjDtj(){
	$("#showListHsj").val("huanshouju");
	$("#saveOrSubmitHsj").val("save");
	document.getElementById("hsjdtj").className="select";
	document.getElementById("hsjytj").className="";
	document.getElementById("hsjdaitijiao").style.display='block';
	document.getElementById("hsjyitijiao").style.display='none';
	$("#pageNoHsj").val(1);
	$("#pageSizeHsj").val(10);
	$("#searchFormHsj").submit();
}

function showHsjYtj(){
	$("#showListHsj").val("huanshouju");
	$("#saveOrSubmitHsj").val("submit");
	document.getElementById("hsjdtj").className="";
	document.getElementById("hsjytj").className="select";
	document.getElementById("hsjdaitijiao").style.display='none';
	document.getElementById("hsjyitijiao").style.display='block';
	$("#pageNoHsj").val(1);
	$("#pageSizeHsj").val(10);
	$("#searchFormHsj").submit();
}

function showJhzDtj(){
	$("#showListjhz").val("jiehuzhao");
	$("#saveOrSubmitjhz").val("save");
	document.getElementById("jhzdtj").className="select";
	document.getElementById("jhzytj").className="";
	document.getElementById("jhzdaitijiao").style.display='block';
	document.getElementById("jhzyitijiao").style.display='none';
	$("#pageNo").val(1);
	$("#pageSize").val(10);
	$("#searchFormjhz").submit();
}

function showJhzYtj(){
	$("#showListjhz").val("jiehuzhao");
	$("#saveOrSubmitjhz").val("submit");
	document.getElementById("jhzdtj").className="";
	document.getElementById("jhzytj").className="select";
	document.getElementById("jhzdaitijiao").style.display='none';
	document.getElementById("jhzyitijiao").style.display='block';
	$("#pageNo_ytj").val(1);
	$("#pageSize_ytj").val(10);
	$("#searchFormjhz").submit();
}

function showHhzDtj(){
	$("#showListhhz").val("huanhuzhao");
	$("#saveOrSubmithhz").val("save");
	document.getElementById("hhzdtj").className="select";
	document.getElementById("hhzytj").className="";
	document.getElementById("hhzdaitijiao").style.display='block';
	document.getElementById("hhzyitijiao").style.display='none';
	$("#pageNohhz").val(1);
	$("#pageSizehhz").val(10);
	$("#searchFormhhz").submit();
}

function showHhzYtj(){
	$("#showListhhz").val("huanhuzhao");
	$("#saveOrSubmithhz").val("submit");
	document.getElementById("hhzdtj").className="";
	document.getElementById("hhzytj").className="select";
	document.getElementById("hhzdaitijiao").style.display='none';
	document.getElementById("hhzyitijiao").style.display='block';
	$("#pageNohhz_ytj").val(1);
	$("#pageSizehhz_ytj").val(10);
	$("#searchFormhhz").submit();
}

function jksortby(sortBy,obj){
	var temporderBy = $("#orderByjk").val();
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
	$("#orderByjk").val(sortBy);
	$("#searchFormJk").submit();
}

function hsjsortby(sortBy,obj){
	var temporderBy = $("#orderByhsj").val();
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
	$("#orderByhsj").val(sortBy);
	$("#searchFormHsj").submit();
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
    
    $("#orderBy").val(sortBy);
    $("#searchFormjhz").submit();
}

function cantuansortby_ytj(sortBy,obj){
    var temporderBy = $("#orderBy_ytj").val();
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
    
    $("#orderBy_ytj").val(sortBy);
    $("#orderBy").val(sortBy);
    $("#searchFormjhz").submit();
}

function hhzsortby(sortBy,obj){
    var temporderBy = $("#orderByhhz").val();
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
    
    $("#orderByhhz").val(sortBy);
    $("#searchFormhhz").submit();
}

function guasortby(sortBy,obj){
	var temporderBy = $("#orderBygua").val();
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

	$("#orderBygua").val(sortBy);
	$("#searchForGua").submit();
}

function hhzsortby_ytj(sortBy,obj){
    var temporderBy = $("#orderByhhz_ytj").val();
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
    
    $("#orderByhhz_ytj").val(sortBy);
    $("#orderByhhz").val(sortBy);
    $("#searchFormhhz").submit();
}

function submit4BorrowPassport(batchNo,visaIds){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要提交该批次吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "提交",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
	if (v=="1"){
		$.ajax({
			cache: true,
			type: "POST",
			url:"${ctx}/visa/order/update4Submit",
			data:{ 
				"batchNo":batchNo,
				"visaIds":visaIds},
				async: false,
				success: function(msg){
				if(msg.msg!=null&&msg.msg!=""){
					top.$.jBox.tip(msg.msg,'warning');
				}else{
					top.$.jBox.tip("操作成功",'warning');
					$("#searchFormjhz").submit();	
				}
			}
		});
		return true;
	}
	},height:160,width:420});
}

function submit4ReturnPassport(batchNo,visaIds){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要提交该批次吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "提交",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
	if (v=="1"){
		$.ajax({
			cache: true,
			type: "POST",
			url:"${ctx}/visa/order/update4SubmitReturn",
			data:{ 
				"batchNo":batchNo,
				"visaIds":visaIds},
				async: false,
				success: function(msg){
				if(msg.msg!=null&&msg.msg!=""){
					top.$.jBox.tip(msg.msg,'warning');
				}else{
					top.$.jBox.tip("操作成功",'warning');
					$("#searchFormhhz").submit();	
				}
			}
		});
		return true;
	}
	},height:160,width:420});
}

//批量还护照
function passportReturn(orderId,visaIds , travellerIds,batchNo){
	$.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:"${ctx}/visa/order/checkBatchReturnPassport",                 
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
					 batchReturnPassport(msg,batchNo);
				 }
	        }   
		});
}
function batchReturnPassport(msg,batchNo){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量还护照",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:580});
		
	}else{
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">护照状态</th><th width="15%" >归还日期</th><th width="10%">护照归还人</th><th width="10%">备注</th></tr></thead><tbody>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" disabled="disabled"  checked="checked" />'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].passportStatusDes+'</td><td><input type="text"  onclick="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" /></td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
		}
		html += '</tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量还护照",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var travellerIds="";
			var  visaIds="";
			var dates="";
			var persons="";
			var others ="";
			var t = true;
			 $("#borrowWin").find("input[type=checkbox]").each(function(){
				  
				 if($(this).attr("checked")){
					  var trallerId =   $(this).attr("trallerId");
					  var visaId =   $(this).attr("visaId");
					  var  name=   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
					 if($("#borrowWinDate_"+visaId).val()==null||$("#borrowWinDate_"+visaId).val()==""){
						top.$.jBox.tip(name+"对应的归还日期不能为空",'warning');
						t=  false;
					}
					 dates+=$("#borrowWinDate_"+visaId).val()+",";
					 if($("#borrowWinPerson_"+visaId).val()==null||$("#borrowWinPerson_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的护照归还人不能为空",'warning');
							t=  false;
						}
					 
					 persons+=$("#borrowWinPerson_"+visaId).val()+",";
					 others+=$("#borrowWinOther_"+visaId).val()+""+",";
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
					url:"${ctx}/visa/order/batchUpdatePassport4jhz",                 
					data:{ 
					    "travellerIds":travellerIds,
					    "visaIds":visaIds,
						passportStatus:"4",
						passportOperator:persons,
						passportOperateTime:dates,
						passportOperateRemark:others,
						batchNo:batchNo
						},                
				    	async: false,                 
					   success: function(msg){
						 if(msg.msg!=null&&msg.msg!=""){
							 top.$.jBox.tip(msg.msg,'warning');
						 }else{
							$("#status_"+batchNo).html("已还");
							$("#hhz_"+batchNo).css('display','none');
							top.$.jBox.tip("操作成功",'warning');
						 }
			        }   
				});			
			return true;
		}
		},height:'auto',width:580});	
	}	
}

//批量添加游客到批次---0064需求--djw--
function addVisitors(batchNo,refundDate){
	/* var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确定要为以下批次添加游客吗？</em></p>';
	html += '<p class="tc" style="font-size:16px;">'
	html += batchNo
	html += '</p>'
	html += '</div>';
	
	$.jBox(html,{title: "确定",buttons:{'确定':1,'取消':0}, add:function(v, h, f){
		debugger;
		if(v=="1"){
			$.ajax({
				cache: true,
				type: "POST",
				url:"${ctx}/visa/order/visaBatchAddVisitors",
				data:{
					"batchNo":batchNo
				},
				async:false,
				success:function(msg){
					if(msg.msg!=null&&msg.msg!=""){
						top.$.jBox.tip(msg.msg,'warning');
					}
				}
			});
			return true;
		}
		},height:160,width:420}); */
		window.location.href="${ctx}/visa/order/visaBatchAddVisitors?batchNo="+batchNo+"&refundDate="+refundDate;
}

//批量借款提交按钮
function jkSubmit(batchNo){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要提交该批次吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "提交",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/batchJkSubmit",
					data:{
						"batchNo":batchNo
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("提交成功",'warning');
							showJkYtj();
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}



//---------------批量借款提交按钮4activiti------------------
function jkSubmit4activiti(batchNo){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要提交该批次吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "提交",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/batchJkSubmit4Activiti",
					data:{
						"batchNo":batchNo
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("提交成功",'warning');
							showJkYtj();
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}

//批量还收据提交按钮
function hsjSubmit(batchNo){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要提交该批次吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "提交",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/batchHsjSubmit",
					data:{
						"batchNo":batchNo
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("提交成功",'warning');
							showHsjYtj();
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}


//批量还收据提交按钮
//---------------批量还收据提交按钮4activiti------------------
function hsjSubmit4activiti(batchNo){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要提交该批次吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "提交",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/batchHsjSubmit4Activiti",
					data:{
						"batchNo":batchNo
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("提交成功",'warning');
							showHsjYtj();
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}





//批量借款游客删除
function jkTravelerDelete(batchNo,tid){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要删除该游客吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "删除",buttons:{'删除': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/jkTravelerDelete",
					data:{
						"batchNo":batchNo,
						"tid":tid
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("删除成功",'warning');
							showJkDtj();
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}

//批量还收据游客删除
function hsjTravelerDelete(batchNo,tid){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要删除该游客吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "删除",buttons:{'删除': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/hsjTravelerDelete",
					data:{
						"batchNo":batchNo,
						"tid":tid
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("删除成功",'warning');
							showHsjDtj();
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}

//批量借还护照-删除游客
function travelerDelete(batchNo,tid){
	var html = '<div style="margin-top:20px;padding:0 10px" >';
	html += '<p class="tc" style="font-size:16px;color:red;"><em>您确认要删除该游客吗？</em></p>';
	html += '</div>';

	$.jBox(html, { title: "删除",buttons:{'删除': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			$.ajax({
					cache: true,
					type: "POST",
					url:"${ctx}/visa/order/travelerDelete",
					data:{
						"batchNo":batchNo,
						"tid":tid
						},
					async: false,
					success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							$("#shanchu_"+tid+batchNo).css('display','none');
							var len = $("#travelerList_"+batchNo+" > tr").is(':visible');
							if(len){	
							}else{	
								$("#pici_"+batchNo).css('display','none');
								$("#child_"+batchNo).css('display','none');
							}
							top.$.jBox.tip("删除成功",'warning');
						}
					}
				});
				return true;
		}
	},height:160,width:420});
}

//批量还收据
function returnReceiptand(visaIds){
	$.ajax({
		cache: true,
		type: "POST",
		url:"${ctx}/visa/order/checkBatchHsjHqx",
		data:{
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
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'" orderId ="'+rightList[i].orderId+'" trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked" disabled="disabled"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td>'+rightList[i].jiekuanStatus+'</td><td>¥'+rightList[i].jiekuanJe+'</td><td><input type="text" onkeyup="clearNoNum(this)" id="returnReceiptWinJe_'+rightList[i].visaId+'" class="rmb inputTxt" value="'+rightList[i].jiekuanJe+'"/></td><td><input type="text" disabled="disabled" onclick="WdatePicker()" id="returnReceiptWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text" disabled="disabled" id="returnReceiptWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].returnReceiptPerson+'" /></td><td><input id="returnReceiptWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
		}
		html += '</tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量还收据",buttons:{'保存': 1,'提交': 2,'取消':0}, submit:function(v, h, f){
		if (v!="0"){
			var travellerIds="";
			var visaIds="";
			var je="";
			var dates="";
			var persons="";
			var others ="";
			var t = true;
			var orderIds="";
			$("#returnReceiptWin").find("input[type=checkbox]").each(function(){
				if($(this).attr("checked")){
					var trallerId = $(this).attr("trallerId");
					var visaId = $(this).attr("visaId");
					var orderId = $(this).attr("orderId");
					var name= $(this).attr("trallerName");
					travellerIds+=trallerId+",";
					visaIds+=visaId+",";
					orderIds+=orderId+",";
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
				url:"${ctx}/visa/order/batchHsj",
				data:{
					"type":v,
					"travellerIds":travellerIds,
					"visaIds":visaIds,
					"orderIds":orderIds,
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
						$("#showList").val('jiekuan');
						$("#saveOrSubmit").val('submit');
						$("#searchFormJk").attr("action","${ctx}/visa/order/visaBatchEditRecordList");
						$("#searchFormJk").submit();
					}
				}
			});
			return true;
		}
		},height:'auto',width:700});
	}
}


//面签通知   addby:djw  需求号:0065&0099
function showMqtz(){
	$("#showListMqtz").val("mianqiantongzhi");
	document.getElementById("batchMqtz").className="select";
	document.getElementById("batchJk").className="";
	document.getElementById("batchHsj").className="";
	document.getElementById("batchJhz").className="";
	document.getElementById("batchHhz").className="";
	document.getElementById("mianqiantongzhi").style.display='block';
	document.getElementById("jiekuan").style.display='none';
	document.getElementById("huanshouju").style.display='none';
	document.getElementById("jiehuzhao").style.display='none';
	document.getElementById("huanhuzhao").style.display='none';
	$('#setGuaranteeType').hide();
	$("#pageNoMqtz").val(1);
	$("#pageSizeMqtz").val(10);
	$("#searchFormMqtz").submit();
}

function clearNoNum(obj){
	obj.value = obj.value.replace(/[^-?\d.\d\d+$]/g,""); //清除"数字""-"和"."以外的字符
	obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
	obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
	obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
	obj.value = obj.value.replace(/\-{2,}/g,"-"); //只保留第一个.- 清除多余的
	obj.value = obj.value.replace("-","$#$").replace(/\-/g,"").replace("$#$","-");
	obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
	if (obj.value.length > 1) {
		obj.value = obj.value.replace(/\-$/g,"");
	}
}


/* if($("#childJkDtj_"+batchNo).css("display")=="none"){
	showJkTravelerList("#childJkDtj_"+batchNo,type);
	$(obj).parents("tr").addClass("tr-hover");
	$(obj).addClass('team_a_click2');
	$("#childJkDtj_"+batchNo).show();
	$(obj).parents("td").addClass("td-extend");
}else{
	$("#childJkDtj_"+batchNo).hide();
	$(obj).parents("td").removeClass("td-extend");
	$(obj).parents("tr").removeClass("tr-hover");
	$(obj).removeClass('team_a_click2');
} */


//需求号：0065&0099  addby:djw
function showVisitorList(batchNo,obj){
	if($("#childMqtz_"+batchNo).css("display")=="none"){
		showMqtzTravelerList("#childMqtz_"+batchNo);
		$(obj).parents("tr").addClass("tr-hover");
		$(obj).addClass('team_a_click2');
		$("#childMqtz_"+batchNo).show();
		$(obj).parents("td").addClass("td-extend");
	}else{
		$("#childMqtz_"+batchNo).hide();
		$(obj).parents("td").removeClass("td-extend");
		$(obj).parents("tr").removeClass("tr-hover");
		$(obj).removeClass('team_a_click2');
	}
}

//需求号:0065&0099   addby:djw
function showMqtzTravelerList(data){
	var countryId = '${country}';
	var areaId = '${area}';
	var travelerName = '${travellerName}';
	var batchNo=data.substring(data.indexOf('_')+1);
	$.ajax({
		cache:true,
		type:"POST",
		url:"${ctx}/visa/order/getBatchMqtzTravelerList",
		data:{ 
			batchNo:batchNo,
			countryId:countryId,
			areaId:areaId,
			travelerName:travelerName
		},
		async:false,
		success:function(traveler){
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
				html += '<tr><td width="7%" style="width:7%;">'+traveler[i].salerName+'</td>';
				html += '<td width="7%" class="tc">'+traveler[i].tname+'</td>';
				html +='<td width="10%" class="tc">'+traveler[i].country+'</td>';
				html +='<td width="8%" class="tc">'+traveler[i].area+'</td>';
				html +='<td width="15%" class="tc">'+traveler[i].interviewTime+'</td>';		 	
				html +='<td width="15%" class="tc">'+traveler[i].explanationTime+'</td>';
				html +='<td width="10%" class="tc">'+traveler[i].address+'</td>';
				html +='<td width="7%" class="tc">'+traveler[i].contactMan+'</td>';
				html +='<td width="10%" class="tc">'+traveler[i].contactWay+'</td>';
				html +='<td width="7%" class="tc"><a href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId='+traveler[i].orderId+'&details=1" target="_blank">查看</a>';
				html +='</td></tr>';
			}
			$("#travelerList_"+batchNo).empty();
			$("#travelerList_"+batchNo).append(html);
		}
	});
}

function mqtzSortby(sortBy,obj){
	var temporderBy = $("#orderByMqtz").val();
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
	$("#orderByMqtz").val(sortBy);
	$("#searchFormMqtz").submit();
}

function resetSearch(){
	
	$(':input','#searchFormMqtz')
	.not(':button, :submit, :reset, :hidden')
	.val('')
	.removeAttr('checked')
	.removeAttr('selected');
}

var countryId = "";
var areaId = '${area}';
$(function(){
	$("#country").blur(function(){
		countryId = $("#country").val();
		if(countryId != ""){
			getArea();
		}
		return;
	});
	countryId = $("#country").val();
	if(countryId != ""){
		getArea();
	}
});


//国家、领区联动
function getArea() {
	
		$.ajax({
			type:"POST",
			url:"${ctx}/visa/visaProducts/getArea?countryId="+countryId,
			async : false,
			success:function(result){
				$("#area").empty().append('<option value=" ">全部</option>');
				if(result != null && result.length != 0) {
					for(var index = 0; index < result.length; index++) {
						if(areaId == result[index][0]){
							$("#area").append('<option value=\'' + result[index][0] + '\' selected >' + result[index][1] + '</option>');
						}else{
							$("#area").append('<option value=\'' + result[index][0] + '\'>' + result[index][1] + '</option>');
						}
					}
				}
			}
		});
	
}
</script>
<style type="text/css"  rel="stylesheet">
 	    *{
 	    	font-family: "microsoft yahei";
 	    	font-size: 12px;
 	    }
		.header{
			line-height: 40px;
		}
		.tab{
			display: inline-block;
			text-decoration: none;
		    font-size: 12px;
		    cursor: pointer;
		    border-right: 1px solid #EDF0F2;
		    padding: 0 20px;
		    color: #333;
            background: #DEDEDE;
		}
		.selected{
			background: #62afe7;
            color: #fff;
		}
		.header-search{
			width: 100%;
		    /* padding: 20px 0px; */
		    background-color: #fff;
		    overflow: hidden;
		}
		.search-item{
			width: 18%;
		    height: 40px;
		    float: left;
		    margin-left: 2%;
		    position: relative;
		    left: 0px;
		    top: 0px;
		}
		.search-title{
		    height: 28px;
		    line-height: 40px;
		    width: auto;
		    text-align: right;
		    float: left;
		}
		.inputTxt{
		    height: 26px;
		    line-height: 26px;
		    width: 93px;
		    padding: 0 5px;
		    background-color: #fff;
		    border: 1px solid #ccc;
		    color: #333;
		    border-radius: 4px;
		    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075) inset;
		    cursor: text;
		}
		.form_submit{
			padding-left: 300px;
		}
		.btn{
	    	background-color: #5f7795;
		    border-color: rgba(0, 0, 0, 0);
		    color: white;
		    padding: 5px 15px;
		    border-radius: 4px;
		    cursor: pointer;
	    }
	    .btn:hover{
			background-color: #28b2e6;
	    }
	    .nav{
    	    height: 30px;
   			padding: 10px 5px;
	    }
	    .nav-left{
    	     width: 79%;
   			 float: left;
	    }
	    .nav-left ul{
	    	margin: 0;
	    	padding-left: 0px;
	    }
	    .nav-left ul li{
    	    width: auto;
		    padding: 0 10px;
	        height: 26px;
		    float: left;
		    margin-right: 10px;
		    line-height: 26px;
		    text-align: center;
		    list-style: none;
	    }
	    .bck-img{
	        background: url(../images/activitylist_paixu.png) no-repeat #a8b9d3;
	    }
	     .bck-img:hover{
	        background: url(../images/activitylist_paixu.png) no-repeat #5f7795;
	    }
	     .nav-left ul li a{
     	    display: block;
		    cursor: pointer;
		    color: #fff;
	        text-decoration: none;
		    font-size: 12px;
	     }
	    .nav-right{
    	    width: 20%;
		    float: right;
		    text-align: right;
		    line-height: 28px;
	    }
       .content .main-table{
	        max-width: 100%;
		    background-color: transparent;
		    border-collapse: collapse;
		    border-spacing: 0;
		    width:100%;
	    }
	     .content .main-table tr{
		  /*  background-image: url(../images/table_headerbg_03.jpg);*/
		    height: 30px;
		    background-color: #f5f5f5;
		}
	    .content .main-table tr th{
	        height: 40px;
	        border: 1px solid #dddddd;
	        padding: 6px 5px;   
		    text-align: center;
		    vertical-align: middle;
		    color: #717171;
		    background: url(../images/bg-tabletop.png) #f5f6f6 repeat-x bottom
	    }
	     .ac{
	    	text-align: center  !important;
	    }
	    .content .main-table tr td{
			vertical-align: middle;
			padding: 6px 7px;
		    text-align: left;
		    border-left: 1px solid #ddd;
		    border-bottom: 1px solid #ddd;
		    border-top: 0px;
		    border-right:1px solid #ddd;
		    background-color: #FFFFFF;
		    height: 40px;
	    }
        .user-list{
        	margin: 0 4px;
		    text-align: center;
	        color: #08C;
		    background: url(../images/team_san.png) no-repeat right center;
		    padding-right: 14px;
	        cursor: pointer;
		    line-height: 15px;
	        text-decoration: none;
		    font-size: 12px;
        }
        .sub-table{
    	    margin: 0 auto;
	        border-collapse: collapse;
            border-bottom: 21px solid #f5f5f5;
		    border-left: 10px solid #f5f5f5;
		    border-right: 10px solid #f5f5f5;
		    border-top: 15px solid #f5f5f5;
	       /* border: none;*/
		    background: none;
		    width: 100%;
        }
         .sub-table  tr{
			background: none;
		    height: 30px;
		    border-collapse: separate;
         }
        .sub-table  tr th,.sub-table  tr td{
    	    height: 40px !important;
	        vertical-align: middle;
            word-break: break-word;
            background: none !important;
   			background-color: #f5f5f5;
        	border-right-width: 1px;
           /* border: none !important;*/
    		border-right: 1px solid #c1cde3;
        	padding: 0 5px !important;
       		font-weight: bold;
        	border-radius: 0;
		    text-align: center !important;
        }
        .sub-table  tr td{
        	font-weight: normal !important;
        }
        .sub-tr td{
    	    height: 40px;
	        vertical-align: middle;
            border-radius: 0;
            word-break: break-word;
            border: 1px solid #93a5bb;
		    padding: 0 0 5px 0  !important;
		    margin-bottom: 20px;
		    background-color: #e1e8ed !important;
       		line-height: 20px;
            display: table-cell;
            font-size: 1em;
        }
        .expand-tr td{
        	background-color: #c7dbec !important;
		    background-image: url(../images/tr-hoverbg.gif);
		    background-repeat: repeat-x;
		    color: #394b61;
		    border-bottom: 1px solid #b5c8db;
		    border-left: 1px solid #b5c8db;
        }
        .sub-content{
        	display: block;
        	background-color: #f5f5f5;
    	    border-top: 2px solid #ddd;
        }
        .sub-table-second{
        	border-top: 0;
        }
        .check-detail{
    	    text-decoration: none;
    		color: #08c;
        }
	</style>
</head>
<body>
	<div class="supplierLine">
		<a onclick="showJk();" href="javascript:void(0)" id="batchJk">批量借款</a>
		<a onclick="showHsj();" href="javascript:void(0)" id="batchHsj">批量还收据</a>
		<a onclick="showJhz();" href="javascript:void(0)" id="batchJhz">批量借护照</a>
		<a onclick="showHhz();" href="javascript:void(0)" id="batchHhz">批量还护照</a>
		<a onclick="showMqtz();" href="javascript:void(0)" id="batchMqtz">面签通知</a>
		<a onclick="showSgt();" href="javascript:void(0)" id="batchSgt">批量设置担保类型</a><!--66-->

		<!--以下4个链接测试用 -->
<%-- 		<a href="${ctx }/visa/order/visaBatchReturnReceiptFinanceList" target="_blank" id="piliangcaozuo1">财务-财务审核-还签证收据列表</a> --%>
<%-- 		<a href="${ctx }/visa/order/visaBatchBorrowMoneyFinanceList" target="_blank" id="piliangcaozuo2">财务-财务审核-签证借款财务审核列表</a> --%>
<%-- 		<a href="${ctx }/visa/order/visaBatchReturnReceiptList" target="_blank" id="piliangcaozuo3">审核-还签证收据列表</a> --%>
<%-- 		<a href="${ctx }/visa/order/visaBatchBorrowMoneyList" target="_blank" id="piliangcaozuo4">审核-签证借款财务审核列表</a> --%>
	</div>
	<div id="jiekuan">
		<!--右侧内容部分开始-->
		<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
			<form method="post" action="${ctx}/visa/order/visaBatchEditRecordList" id="searchFormJk">
				<input id="pageNoJk" name="pageNo" type="hidden" value="${pageJk.pageNo}"/>
				<input id="pageSizeJk" name="pageSize" type="hidden" value="${pageJk.pageSize}"/>
				<input id="orderByjk" name="orderBy" type="hidden" value="${pageJk.orderBy}"/>
				<input value="" type="hidden" id="showList" name="showList"/>
				<input value="" type="hidden" id="saveOrSubmit" name="saveOrSubmit"/>
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">批次编号：</div>
						<input value="${batchNo}" class="inputTxt" name="batchNo" id="batchNo">
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">操作人：</div>
						<input value="${txnPerson}" class="inputTxt" name="txnPerson" id="txnPerson">
					</div>
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">申请时间：</label>
						<input readonly="" onclick="WdatePicker()" value="${createTimeStart}" name="createTimeStart" class="inputTxt dateinput" id="createTimeStart">
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker()" value="${createTimeEnt}" name="createTimeEnt" class="inputTxt dateinput" id="createTimeEnt">
					</div>
					
					<div class="kong"></div>
					
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">游客：</div>
						<input value="${travellerName}" name="travellerName" class="inputTxt" id="travellerName"> 
					</div>
					<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">实际约签时间：</label>
						<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeStart }" name="signTimeStart" class="inputTxt dateinput" id="signTimeStart">
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeEnd }" name="signTimeEnd" class="inputTxt dateinput" id="signTimeEnd">
					</div>
					
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
					<div class="form_submit">
						<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
						<input type="button" value="条件重置" onclick="resetSearchParamsJk()" class="btn btn-primary ydbz_x">
					</div>
				</div>
			</form>
			<!--查询结果筛选条件排序开始-->
			<div class="supplierLine">
				<a onclick="showJkDtj();" href="javascript:void(0)" id="jkdtj">待提交</a>
				<a onclick="showJkYtj();" href="javascript:void(0)" id="jkytj">已提交</a>
			</div>
			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>
							<li >排序</li>
							<c:choose>
								<c:when test="${pageJk.orderBy == 'u.createDate DESC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="jksortby('u.createDate',this)">
											创建时间
											<i class="icon icon-arrow-down"></i>
										</a>
									</li>
								</c:when>
								<c:when test="${pageJk.orderBy == 'u.createDate ASC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="jksortby('u.createDate',this)">
											创建时间
											<i class="icon icon-arrow-up"></i>
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="activitylist_paixu_left_biankuang lipro.updateDate">
										<a onclick="jksortby('u.createDate',this)">
											创建时间
										</a>
									</li>
								</c:otherwise>
							</c:choose>
							<!-- 0064需求,游客加入批次-s-更新时间排序-针对批发商环球行 -->
							<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
							  <c:choose>
								<c:when test="${pageJk.orderBy == 'u.updateDate DESC'}">
									<li class="activitylist_paixu_moren" name="li4UpdateTime" style="display:inline-block;">
										<a onclick="jksortby('u.updateDate',this)">
											更新时间
											<i class="icon icon-arrow-down"></i>
										</a>
									</li>
								</c:when>
								<c:when test="${pageJk.orderBy == 'u.updateDate ASC'}">
									<li class="activitylist_paixu_moren" name="li4UpdateTime" style="display:inline-block;">
										<a onclick="jksortby('u.updateDate',this)">
											更新时间
											<i class="icon icon-arrow-up"></i>
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="activitylist_paixu_left_biankuang lipro.updateDate" name="li4UpdateTime" style="display:inline-block;">
										<a onclick="jksortby('u.updateDate',this)">
											更新时间
										</a>
									</li>
								</c:otherwise>
							</c:choose>
							</c:if>
							<!-- 0064需求,游客加入批次-e -->
						</ul>
					</div>
					<div class="activitylist_paixu_right"> 
					<c:choose>
						<c:when test="${pageJk.count >0}">
							查询结果&nbsp;&nbsp;<strong>${pageJk.count}</strong>&nbsp;条
						</c:when>
						<c:otherwise>
							查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
						</c:otherwise>
					</c:choose>
					</div>
					<div class="kong"></div>
				</div>
			</div>
			<div id="jkdaitijiao">
				<!--查询结果筛选条件排序结束-->
				<table class="table activitylist_bodyer_table" id="contentTable">
					<thead>
					<tr>
						<th width="10%">批次</th>
						<th width="10%">操作人</th>
						<!-- 0064需求,游客加入批次-s-批发商环球行-该需求,将申请时间去了,换成了创建时间和更新时间 -->
						<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
						 <th width="12%">创建时间</th>
						  <th width="12%">更新时间</th>
						</c:if>
                        <!-- 0064需求,游客加入批次-e -->
                        <c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 不为环球行的批发商,继续显示申请时间 -->
						   <th width="12%">申请时间</th>
						</c:if>
						<th width="12%">借款金额</th>
						<th width="12%">借款状态</th>
						<th width="12%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(pageJk.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${pageJk.list }" var="recordJkDtj">
							<tr>
								<td>${recordJkDtj.batchNo }</td>
								<td class="tc">${recordJkDtj.txnPerson }</td>
								<!-- 0064需求,游客加入批次-s-批发商环球行 -->
								<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
								   <td class="tc"><fmt:formatDate value="${recordJkDtj.createDate }" pattern="yyyy-MM-dd"/></td>
								    <td class="tc"><fmt:formatDate value="${recordJkDtj.updateDate }" pattern="yyyy-MM-dd"/></td>
								</c:if>
								<!-- 0064需求,游客加入批次-e -->
								<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 0064:不为环球行的用户,保持原有展示 -->
								<td class="tc"><fmt:formatDate value="${recordJkDtj.createDate }" pattern="yyyy-MM-dd"/></td>
								</c:if>
								<td class="tr">￥<fmt:formatNumber value="${recordJkDtj.batchTotalMoney }" pattern="#,##0.00"/></td>
								<td class="tc"><c:if test="${recordJkDtj.reviewStatus=='99' }">未借</c:if></td>
								<td class="tc tda">
								
								<!-- 0064需求,游客加入批次-s-批发商环球行-列表页操作下增加"添加游客" -->
								  <c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
								        <a class="" onClick="addVisitors('${recordJkDtj.batchNo }','${recordJkDtj.refundDate}')" href="javascript:void(0)">添加游客</a>
								  </c:if>

								<!-- 0064需求,游客加入批次-e -->
								    
									<a class="team_a_click" onclick="expandJk('${recordJkDtj.batchNo }',this,'save')" href="javascript:void(0)">游客列表</a>
									
									
								    <c:if test="${recordJkDtj.isnew eq 1}">
									   <a href="javascript:void(0)" onClick="jkSubmit('${recordJkDtj.batchNo }')">提交</a>
									</c:if>
										   
								    <c:if test="${recordJkDtj.isnew eq 2 }">
								       <a href="javascript:void(0)" onClick="jkSubmit4activiti('${recordJkDtj.batchNo }')">提交</a>
								    </c:if>
								   
									
								</td>
							</tr>
							<tr id="childJkDtj_${recordJkDtj.batchNo }" class="activity_team_top1" style="display:none">
								<td colspan="7" class="team_top">
									<table class="table activitylist_bodyer_table" style="margin:0 auto;">
										<thead>
											<tr>
												<th class="tc" width="4%">姓名</th>
												<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
												<th class="tc" width="7%">订单号</th>
												<th class="tc" width="7%">签证类型</th>
												<th class="tc" width="6%">签证国家</th>
												<th class="tc" width="7%">实际出团时间</th>
												<th class="tc" width="7%">实际约签时间</th>
												<th class="tc" width="7%">签证状态</th>
												<th class="tc" width="6%">借款金额</th>
												<th class="tc" width="6%">借款人</th>
												<th  width="6%" class="tc">操作</th>	
											</tr>
										</thead>
									</table>
									<div class="table_activity_scroll">
										<table class="table activitylist_bodyer_table ">
											<tbody id="travelerListJkDtj_${recordJkDtj.batchNo }">
											
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="jkyitijiao">
				<!--查询结果筛选条件排序结束-->	 
				<table class="table activitylist_bodyer_table" id="contentTable">
					<thead>
					<tr>
						<th width="10%">批次</th>
						<th width="10%">操作人</th>
						<th width="12%">申请时间</th>
						<th width="12%">借款金额</th>
						<th width="12%">借款状态</th>
						<th width="12%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(pageJk.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${pageJk.list }" var="recordJkYtj">
							<tr>
								<td>${recordJkYtj.batchNo }</td>
								<td class="tc">${recordJkYtj.txnPerson }</td>
								<td class="tc"><fmt:formatDate value="${recordJkYtj.createDate }" pattern="yyyy-MM-dd"/></td>
								<td class="tr">￥<fmt:formatNumber value="${recordJkYtj.batchTotalMoney }" pattern="#,##0.00"/></td>
								<c:choose>
									<c:when test="${recordJkYtj.reviewId eq null || recordJkYtj.reviewId eq ''}">
										<td></td>
									</c:when>
									<c:when test="${recordJkYtj.reviewStatus eq 0}">
										<td class="invoice_back" title="${list.denyReason}">
										   <c:if test="${recordJkYtj.isnew}">
										      ${fns:getNextReview(recordJkYtj.reviewId) }
										   </c:if>
										   
										   <c:if test="${recordJkYtj.isnew eq 2 }">
										      ${fns:getChineseReviewStatusByUuid(recordJkYtj.reviewId) }
										   </c:if>
										
										</td>
									</c:when>
									<c:when test="${recordJkYtj.reviewStatus eq 2}">
										<td class="invoice_yes">
										      <c:if test="${recordJkYtj.isnew eq 1}">
										           ${fns:getNextReview(recordJkYtj.reviewId) }
										     </c:if>
										     
										     <c:if test="${recordJkYtj.isnew eq 2}">
										           ${fns:getChineseReviewStatusByUuid(recordJkYtj.reviewId) }
										   </c:if>
										</td>
									</c:when>
									<c:when test="${recordJkYtj.reviewStatus eq 3 or recordJkYtj.reviewStatus eq 1}">
										<td class="invoice_no">
										    <c:if test="${recordJkYtj.isnew eq 1}">
										       ${fns:getNextReview(recordJkYtj.reviewId) }
										    </c:if>
										    
										    <c:if test="${recordJkYtj.isnew eq 2}">
										      ${fns:getChineseReviewStatusByUuid(recordJkYtj.reviewId) }
										   </c:if>
										</td>
									</c:when>
									<c:when test="${recordJkYtj.reviewStatus eq 4}">
										<td class="invoice_back">
										 <c:if test="${recordJkYtj.isnew eq 1}">
										    ${fns:getNextReview(recordJkYtj.reviewId) }
										 </c:if>
										 
										  <c:if test="${recordJkYtj.isnew  eq 2}">
										      ${fns:getChineseReviewStatusByUuid(recordJkYtj.reviewId) }
										   </c:if>
										</td>
									</c:when>
								</c:choose>
								<td class="tc tda">
									<a class="team_a_click" onclick="expandJk('${recordJkYtj.batchNo }',this,'submit')"  href="javascript:void(0)">游客列表</a>
									<c:if test="${recordJkYtj.reviewStatus eq 2}">
										<a onclick="returnReceiptand('${recordJkYtj.visaIds }');" href="javascript:void(0)">还收据</a>
									</c:if>
								</td>
							</tr>
							<tr id="childJkYtj_${recordJkYtj.batchNo }" class="activity_team_top1" style="display:none">
								<td colspan="7" class="team_top">
									<table class="table activitylist_bodyer_table" style="margin:0 auto;">
										<thead>
											<tr>
												<th class="tc" width="4%">姓名</th>
												
												<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
												
												<th class="tc" width="7%">订单号</th>
												<th class="tc" width="7%">签证类型</th>
												<th class="tc" width="6%">签证国家</th>
												<th class="tc" width="7%">实际出团时间</th>
												<th class="tc" width="7%">实际约签时间</th>
												<th class="tc" width="7%">签证状态</th>
												<th class="tc" width="6%">借款金额</th>
												<th class="tc" width="6%">借款人</th>
												<th width="6%" class="tc">操作</th>	
											</tr>
										</thead>
									</table>
									<div class="table_activity_scroll">
										<table class="table activitylist_bodyer_table ">
											<tbody id="travelerListJkYtj_${recordJkYtj.batchNo }">
											
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="pagination clearFix">
				${pageStrJk}
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
	<div id="huanshouju">
		<!--右侧内容部分开始-->
		<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
			<form method="post" action="${ctx}/visa/order/visaBatchEditRecordList" id="searchFormHsj">
				<input id="pageNoHsj" name="pageNo" type="hidden" value="${pageHsj.pageNo}"/>
				<input id="pageSizeHsj" name="pageSize" type="hidden" value="${pageHsj.pageSize}"/>
				<input id="orderByhsj" name="orderBy" type="hidden" value="${pageHsj.orderBy}"/>
				<input value="" type="hidden" id="showListHsj" name="showList"/>
				<input value="" type="hidden" id="saveOrSubmitHsj" name="saveOrSubmit"/>
				<div class="activitylist_bodyer_right_team_co">
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">批次编号：</div>
						<input value="${batchNo}" class="inputTxt" name="batchNo" id="batchNo">
					</div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">操作人：</div>
						<input value="${txnPerson}" class="inputTxt" name="txnPerson" id="txnPerson">
					</div>
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">申请时间：</label>
						<input readonly="" onclick="WdatePicker()" value="${createTimeStart}" name="createTimeStart" class="inputTxt dateinput" id="createTimeStart">
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker()" value="${createTimeEnt}" name="createTimeEnt" class="inputTxt dateinput" id="createTimeEnt">
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">游客名称：</div>
						<input value="${travellerName}" name="travellerName" class="inputTxt" id="travellerName"> 
					</div>
					<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">实际约签时间：</label>
						<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeStart }" name="signTimeStart" class="inputTxt dateinput" id="signTimeStart">
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeEnd }" name="signTimeEnd" class="inputTxt dateinput" id="signTimeEnd">
					</div>
					
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
					<div class="form_submit">
						<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
						<input type="button" value="条件重置" onclick="resetSearchParamsHsj()" class="btn btn-primary ydbz_x">
					</div>
				</div>
			</form>
			<!--查询结果筛选条件排序开始-->
			<div class="supplierLine">
				<a onclick="showHsjDtj();" href="javascript:void(0)" id="hsjdtj">待提交</a>
				<a onclick="showHsjYtj();" href="javascript:void(0)" id="hsjytj">已提交</a>
			</div>
			<div class="activitylist_bodyer_right_team_co_paixu">
				<div class="activitylist_paixu">
					<div class="activitylist_paixu_left">
						<ul>
							<li >排序</li>
							<c:choose>
								<c:when test="${pageHsj.orderBy == 'u.createDate DESC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="hsjsortby('u.createDate',this)">
											创建时间
											<i class="icon icon-arrow-down"></i>
										</a>
									</li>
								</c:when>
								<c:when test="${pageHsj.orderBy == 'u.createDate ASC'}">
									<li class="activitylist_paixu_moren">
										<a onclick="hsjsortby('u.createDate',this)">
											创建时间
											<i class="icon icon-arrow-up"></i>
										</a>
									</li>
								</c:when>
								<c:otherwise>
									<li class="activitylist_paixu_left_biankuang lipro.updateDate">
										<a onclick="hsjsortby('u.createDate',this)">
											创建时间
										</a>
									</li>
								</c:otherwise>
							</c:choose>
						</ul>
					</div>
					<div class="activitylist_paixu_right"> 
					<c:choose>
						<c:when test="${pageHsj.count >0}">
							查询结果&nbsp;&nbsp;<strong>${pageHsj.count}</strong>&nbsp;条
						</c:when>
						<c:otherwise>
							查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
						</c:otherwise>
					</c:choose>
					</div>
					<div class="kong"></div>
				</div>
			</div>
			<div id="hsjdaitijiao">
				<!--查询结果筛选条件排序结束-->
				<table class="table activitylist_bodyer_table" id="contentTable">
					<thead>
					<tr>
						<th width="10%">批次</th>
						<th width="10%">操作人</th>
						<th width="12%">申请时间</th>
						<th width="12%">收据金额</th>
						<th width="12%">还收据状态</th>
						<th width="12%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(pageHsj.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${pageHsj.list }" var="recordHsjDtj">
							<tr>
								<td>${recordHsjDtj.batchNo }</td>
								<td class="tc">${recordHsjDtj.txnPerson }</td>
								<td class="tc"><fmt:formatDate value="${recordHsjDtj.createDate }" pattern="yyyy-MM-dd"/></td>
								<td class="tr">￥<fmt:formatNumber value="${recordHsjDtj.batchTotalMoney }" pattern="#,##0.00"/></td>
								<td class="tc"><c:if test="${recordHsjDtj.reviewStatus=='99' }">未还</c:if></td>
								<td class="tc tda">
									<a class="team_a_click" onclick="expandHsj('${recordHsjDtj.batchNo }',this,'save')" href="javascript:void(0)">游客列表</a>
									
									<c:if test="${recordHsjDtj.isnew eq 1}">
							           <a href="javascript:void(0)" onClick="hsjSubmit('${recordHsjDtj.batchNo }')">提交</a>
							        </c:if>
							     
							        <c:if test="${recordHsjDtj.isnew eq 2}">
							          <a href="javascript:void(0)" onClick="hsjSubmit4activiti('${recordHsjDtj.batchNo }')">提交</a>
							        </c:if>
									
									
								</td>
							</tr>
							<tr id="childHsjDtj_${recordHsjDtj.batchNo }" class="activity_team_top1" style="display:none">
								<td colspan="7" class="team_top">
									<table class="table activitylist_bodyer_table" style="margin:0 auto;">
										<thead>
											<tr>
												<th class="tc" width="4%">姓名</th>
												<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
												<th class="tc" width="7%">订单号</th>
												<th class="tc" width="7%">签证类型</th>
												<th class="tc" width="6%">签证国家</th>
												<th class="tc" width="7%">实际出团时间</th>
												<th class="tc" width="7%">实际约签时间</th>
												<th class="tc" width="7%">签证状态</th>
												<th class="tc" width="6%">还收据金额</th>
												<th class="tc" width="6%">收据领取人</th>
												<th  width="6%" class="tc">操作</th>	
											</tr>
										</thead>
									</table>
									<div class="table_activity_scroll">
										<table class="table activitylist_bodyer_table ">
											<tbody id="travelerListHsjDtj_${recordHsjDtj.batchNo }">
											
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="hsjyitijiao">
				<!--查询结果筛选条件排序结束-->	 
				<table class="table activitylist_bodyer_table" id="contentTable">
					<thead>
					<tr>
						<th width="10%">批次</th>
						<th width="10%">操作人</th>
						<th width="12%">申请时间</th>
						<th width="12%">收据金额</th>
						<th width="12%">还收据状态</th>
						<th width="12%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(pageHsj.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${pageHsj.list }" var="recordHsjYtj">
							<tr>
								<td>${recordHsjYtj.batchNo }</td>
								<td class="tc">${recordHsjYtj.txnPerson }</td>
								<td class="tc"><fmt:formatDate value="${recordHsjYtj.createDate }" pattern="yyyy-MM-dd"/></td>
								<td class="tr">￥<fmt:formatNumber value="${recordHsjYtj.batchTotalMoney }" pattern="#,##0.00"/></td>
								<c:choose>
									<c:when test="${recordHsjYtj.reviewId eq null || recordHsjYtj.reviewId eq ''}">
										<td></td>
									</c:when>
									<c:when test="${recordHsjYtj.reviewStatus eq 0}">
										<td class="invoice_back" title="${list.denyReason}">
										
										 <c:if test="${recordHsjYtj.isnew eq 1}">
										           ${fns:getNextReview(recordHsjYtj.reviewId) }
										     </c:if>
										     
										     <c:if test="${recordHsjYtj.isnew eq 2}">
										           ${fns:getChineseReviewStatusByUuid(recordHsjYtj.reviewId) }
										   </c:if>
										
										</td>
									</c:when>
									<c:when test="${recordHsjYtj.reviewStatus eq 2}">
										<td class="invoice_yes">
										       <c:if test="${recordHsjYtj.isnew eq 1}">
										           ${fns:getNextReview(recordHsjYtj.reviewId) }
										      </c:if>
										     
										     <c:if test="${recordHsjYtj.isnew eq 2}">
										           ${fns:getChineseReviewStatusByUuid(recordHsjYtj.reviewId) }
										      </c:if>
										</td>
									</c:when>
									<c:when test="${recordHsjYtj.reviewStatus eq 3 or recordHsjYtj.reviewStatus eq 1}">
										<td class="invoice_no">
											 <c:if test="${recordHsjYtj.isnew eq 1}">
										           ${fns:getNextReview(recordHsjYtj.reviewId) }
										     </c:if>
										     
										     <c:if test="${recordHsjYtj.isnew eq 2}">
										           ${fns:getChineseReviewStatusByUuid(recordHsjYtj.reviewId) }
										     </c:if>
										</td>
									</c:when>
									<c:when test="${recordHsjYtj.reviewStatus eq 4}">
										<td class="invoice_back">
										
										     <c:if test="${recordHsjYtj.isnew eq 1}">
										           ${fns:getNextReview(recordHsjYtj.reviewId) }
										     </c:if>
										     
										     <c:if test="${recordHsjYtj.isnew eq 2}">
										           ${fns:getChineseReviewStatusByUuid(recordHsjYtj.reviewId) }
										     </c:if>
										
										</td>
									</c:when>
								</c:choose>
								<td class="tc tda">
									<a class="team_a_click" onclick="expandHsj('${recordHsjYtj.batchNo }',this,'submit')"  href="javascript:void(0)">游客列表</a>
								</td>
							</tr>
							<tr id="childHsjYtj_${recordHsjYtj.batchNo }" class="activity_team_top1" style="display:none">
								<td colspan="7" class="team_top">
									<table class="table activitylist_bodyer_table" style="margin:0 auto;">
										<thead>
											<tr>
												<th class="tc" width="4%">姓名</th>
												<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
												<th class="tc" width="7%">订单号</th>
												<th class="tc" width="7%">签证类型</th>
												<th class="tc" width="6%">签证国家</th>
												<th class="tc" width="7%">实际出团时间</th>
												<th class="tc" width="7%">实际约签时间</th>
												<th class="tc" width="7%">签证状态</th>
												<th class="tc" width="6%">还收据金额</th>
												<th class="tc" width="6%">收据领取人</th>
												<th width="6%" class="tc">操作</th>	
											</tr>
										</thead>
									</table>
									<div class="table_activity_scroll">
										<table class="table activitylist_bodyer_table ">
											<tbody id="travelerListHsjYtj_${recordHsjYtj.batchNo }">
											
											</tbody>
										</table>
									</div>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="pagination clearFix">
				${pageStrHsj}
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
	<div id="jiehuzhao">
		<!--右侧内容部分开始-->
		<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
			<form method="post" action="${ctx}/visa/order/visaBatchEditRecordList" id="searchFormjhz">
					<input id="pageNo" name="pageNo" type="hidden" value="${page1.pageNo}"/>
			        <input id="pageSize" name="pageSize" type="hidden" value="${page1.pageSize}"/>
			        <input id="orderBy" name="orderBy" type="hidden" value="${page1.orderBy}"/>
			        
			        <input id="pageNo_ytj" name="pageNo" type="hidden" value="${page2.pageNo}"/>
			        <input id="pageSize_ytj" name="pageSize" type="hidden" value="${page2.pageSize}"/>
			        <input id="orderBy_ytj" name="orderBy" type="hidden" value="${page2.orderBy}"/>
			        
			        <input value="" type="hidden" id="showListjhz" name="showList"/>
			        <input value="" type="hidden" id="saveOrSubmitjhz" name="saveOrSubmit"/>
			        
						<div class="activitylist_bodyer_right_team_co">
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">批次编号：</div>
									<input value="${batchNo}" class="inputTxt"  name="batchNo"  id=""> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">操作人：</div>
								   <input value="${txnPerson}" class="inputTxt"  name="txnPerson" id="txnPerson"> 
								</div>
								<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
									<label class="activitylist_team_co3_text" style="width:85px;">记录时间：</label>
									<input readonly="" onclick="WdatePicker()" value="${createTimeStart}" name="createTimeStart" class="inputTxt dateinput" id="createTimeStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${createTimeEnt}" name="createTimeEnt" class="inputTxt dateinput" id="createTimeEnt">
								</div>
								<div class="kong"></div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">游客名称：</div>
								    <input value="${travellerName}" name="travellerName" class="inputTxt"  id="travellerName"> 
								</div>
							
							<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
							<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
								<label class="activitylist_team_co3_text" style="width:85px;">实际约签时间：</label>
								<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeStart }" name="signTimeStart" class="inputTxt dateinput" id="signTimeStart">
								<span> 至 </span>
								<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeEnd }" name="signTimeEnd" class="inputTxt dateinput" id="signTimeEnd">
							</div>
					
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
							<div class="form_submit">
								<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
								<input type="button" value="条件重置" onclick="resetSearchParamsjhz()" class="btn btn-primary ydbz_x">
							</div>	 
								 
						</div>
			</form>
			<!--查询结果筛选条件排序开始-->
			<div class="supplierLine">
				<a onclick="showJhzDtj();" href="javascript:void(0)" id="jhzdtj">待提交</a>
				<a onclick="showJhzYtj();" href="javascript:void(0)" id="jhzytj">已提交</a>
			</div>
			<div id="jhzdaitijiao">
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
						<ul>
										<li >排序</li>
										<c:choose>
					                    	<c:when test="${page1.orderBy == 'u.createTime DESC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="cantuansortby('u.createTime',this)">
						                    			创建时间
						                    			<i class="icon icon-arrow-down"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	
					                    	
					                    	<c:when test="${page1.orderBy == 'u.createTime ASC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="cantuansortby('u.createTime',this)">
						                    			创建时间
					                    				<i class="icon icon-arrow-up"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="cantuansortby('u.createTime',this)">
						                    			创建时间
					                    			</a>
					                    		</li>
					                    	</c:otherwise>
					                    </c:choose>
									</ul>
						</div>
						<div class="activitylist_paixu_right"> 
						<c:choose>
							 	<c:when test="${page1.count >0}">
							 	查询结果&nbsp;&nbsp;<strong>${page1.count}</strong>&nbsp;条
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
						<th width="12%">申请时间</th>
						<th width="12%">护照出借状态</th>
						<th width="10%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(page1.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${page1.list }" var="record">
						<tr id="pici_${record.batchNo }">
							<td>${record.batchNo }</td>
							<td class="tc">${record.createUserName }</td>
							<td class="tc"><fmt:formatDate value="${record.createTime }" pattern="yyyy-MM-dd"/></td>
							<td class="tc">未借出</td>
							<td class="tc tda">
								<a class="team_a_click" onclick="expand('${record.batchNo }',this,'${record.visaIds }','save')"  href="javascript:void(0)">游客列表</a>
								<a href="javascript:void(0)" onclick="submit4BorrowPassport('${record.batchNo }','${record.visaIds }')">提交</a>
							</td>
						</tr>
						<tr id="child_${record.batchNo }" class="activity_team_top1" style="display:none" >
							<td colspan="5" class="team_top">
								<table class="table activitylist_bodyer_table" style="margin:0 auto;">
									<thead>
										<tr>
											<th class="tc" width="4%">姓名</th>
											<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
											<th class="tc" width="7%">护照号</th>
											<th class="tc" width="7%">签证类型</th>
											<th class="tc" width="6%">签证国家</th>
											<th class="tc" width="7%">实际出团时间</th>
											<th class="tc" width="7%">实际约签时间</th>
											<th class="tc" width="7%">签证状态</th>
											<th class="tc" width="6%">领取人</th>
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
					</tbody>
				</table>
				<div class="pagination clearFix">
					 ${pageStr1}
				</div>
			</div>
			<div id="jhzyitijiao">
				<div class="activitylist_bodyer_right_team_co_paixu">
			  <div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
				  <ul>
										<li >排序</li>
										<c:choose>
					                    	<c:when test="${page2.orderBy == 'u.createTime DESC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="cantuansortby_ytj('u.createTime',this)">
						                    			创建时间
						                    			<i class="icon icon-arrow-down"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	
					                    	
					                    	<c:when test="${page2.orderBy == 'u.createTime ASC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="cantuansortby_ytj('u.createTime',this)">
						                    			创建时间
					                    				<i class="icon icon-arrow-up"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="cantuansortby_ytj('u.createTime',this)">
						                    			创建时间
					                    			</a>
					                    		</li>
					                    	</c:otherwise>
					                    </c:choose>
									</ul>
				</div>
				<div class="activitylist_paixu_right"> 
				<c:choose>
					<c:when test="${page2.count >0}">
					查询结果&nbsp;&nbsp;<strong>${page2.count}</strong>&nbsp;条
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
						<th width="12%">申请时间</th>
						<th width="12%">护照出借状态</th>
						<th width="10%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(page2.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${page2.list }" var="page2">
						<tr>
							<td>${page2.batchNo }</td>
							<td class="tc">${page2.createUserName }</td>
							<td class="tc"><fmt:formatDate value="${page2.createTime }" pattern="yyyy-MM-dd"/></td>
							<td class="tc" id="status_${page2.batchNo }">
							 	<c:choose>
                                	<c:when test="${page2.passportStatus==1}">已借出</c:when>
									<c:otherwise>已还</c:otherwise>
								</c:choose>
							</td>
							<td class="tc tda">
								<a class="team_a_click" onclick="expand('${page2.batchNo }',this,'${page2.visaIds }','submit')"  href="javascript:void(0)">游客列表</a>
								<c:if test="${page2.passportStatus==1}">
								<a onclick="passportReturn(null,'${page2.visaIds}',null,'${page2.batchNo }');"href="javascript:void(0)" id="hhz_${page2.batchNo }">还护照</a>
								</c:if>
							</td>
						</tr>
						<tr id="child_${page2.batchNo }" class="activity_team_top1" style="display:none">
							<td colspan="5" class="team_top">
								<table class="table activitylist_bodyer_table" style="margin:0 auto;">
									<thead>
										<tr>
											<th class="tc" width="4%">姓名</th>
											<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
											<th class="tc" width="7%">护照号</th>
											<th class="tc" width="7%">签证类型</th>
											<th class="tc" width="6%">签证国家</th>
											<th class="tc" width="7%">实际出团时间</th>
											<th class="tc" width="7%">实际约签时间</th>
											<th class="tc" width="7%">签证状态</th>
											<th class="tc" width="6%">领取人</th>
											<th width="3%" class="tc">操作</th>	
										</tr>
									</thead>
								</table>
								<div class="table_activity_scroll">
									<table class="table activitylist_bodyer_table ">
										<tbody id="travelerList_${page2.batchNo }">
											
										</tbody>
									</table>
								</div>
							</td>
						</tr>
					  </c:forEach>
					</tbody>
				</table>
				<div class="pagination clearFix">
					 ${pageStr2}
				</div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>
	<div id="huanhuzhao">
		<!--右侧内容部分开始-->
		<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
			<form method="post" action="${ctx}/visa/order/visaBatchEditRecordList" id="searchFormhhz">
				<input id="pageNohhz" name="pageNo" type="hidden" value="${page3.pageNo}"/>
			    <input id="pageSizehhz" name="pageSize" type="hidden" value="${page3.pageSize}"/>
			    <input id="orderByhhz" name="orderBy" type="hidden" value="${page3.orderBy}"/>
			        
			    <input id="pageNohhz_ytj" name="pageNo" type="hidden" value="${page4.pageNo}"/>
			    <input id="pageSizehhz_ytj" name="pageSize" type="hidden" value="${page4.pageSize}"/>
			    <input id="orderByhhz_ytj" name="orderBy" type="hidden" value="${page4.orderBy}"/>
			        
			    <input value="" type="hidden" id="showListhhz" name="showList"/>
			    <input value="" type="hidden" id="saveOrSubmithhz" name="saveOrSubmit"/>
				
				<div class="activitylist_bodyer_right_team_co">
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">批次编号：</div>
									<input value="${batchNo}" class="inputTxt"  name="batchNo"  id=""> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">操作人：</div>
								   <input value="${txnPerson}" class="inputTxt"  name="txnPerson" id="txnPerson"> 
								</div>
								<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
									<label class="activitylist_team_co3_text" style="width:85px;">记录时间：</label>
									<input readonly="" onclick="WdatePicker()" value="${createTimeStart}" name="createTimeStart" class="inputTxt dateinput" id="createTimeStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${createTimeEnt}" name="createTimeEnt" class="inputTxt dateinput" id="createTimeEnt">
								</div>
									<div class="kong"></div>
									<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">游客名称：</div>
								   <input value="${travellerName}" name="travellerName" class="inputTxt"  id="travellerName"> 
								</div>
							
							<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
							<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
								<label class="activitylist_team_co3_text" style="width:85px;">实际约签时间：</label>
								<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeStart }" name="signTimeStart" class="inputTxt dateinput" id="signTimeStart">
								<span> 至 </span>
								<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${signTimeEnd }" name="signTimeEnd" class="inputTxt dateinput" id="signTimeEnd">
							</div>
					
							<div class="kong"></div>
							<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
							<div class="form_submit">
								<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
								<input type="button" value="条件重置" onclick="resetSearchParamshhz()" class="btn btn-primary ydbz_x">
							</div>			 
								 
				</div>
			</form>
			<!--查询结果筛选条件排序开始-->
			<div class="supplierLine">
				<a onclick="showHhzDtj();" href="javascript:void(0)" id="hhzdtj">待提交</a>
				<a onclick="showHhzYtj();" href="javascript:void(0)" id="hhzytj">已提交</a>
			</div>
			<div id="hhzdaitijiao">
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
						<ul>
										<li >排序</li>
										<c:choose>
					                    	<c:when test="${page3.orderBy == 'u.createTime DESC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="hhzsortby('u.createTime',this)">
						                    			创建时间
						                    			<i class="icon icon-arrow-down"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	
					                    	
					                    	<c:when test="${page3.orderBy == 'u.createTime ASC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="hhzsortby('u.createTime',this)">
						                    			创建时间
					                    				<i class="icon icon-arrow-up"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="hhzsortby('u.createTime',this)">
						                    			创建时间
					                    			</a>
					                    		</li>
					                    	</c:otherwise>
					                    </c:choose>
									</ul>
						</div>
						<div class="activitylist_paixu_right"> 
						<c:choose>
							 	<c:when test="${page3.count >0}">
							 	查询结果&nbsp;&nbsp;<strong>${page3.count}</strong>&nbsp;条
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
						<th width="12%">申请时间</th>
						<th width="12%">护照归还状态</th>
						<th width="10%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(page3.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${page3.list }" var="page3">
						<tr id="pici_${page3.batchNo }">
							<td>${page3.batchNo }</td>
							<td class="tc">${page3.createUserName }</td>
							<td class="tc"><fmt:formatDate value="${page3.createTime }" pattern="yyyy-MM-dd"/></td>
							<td class="tc">未还</td>
							<td class="tc tda">
								<a class="team_a_click" onclick="expand('${page3.batchNo }',this,'${page3.visaIds }','save')"  href="javascript:void(0)">游客列表</a>
								<a href="javascript:void(0)" onclick="submit4ReturnPassport('${page3.batchNo }','${page3.visaIds }')">提交</a>
							</td>
						</tr>
						<tr id="child_${page3.batchNo }" class="activity_team_top1" style="display:none" >
							<td colspan="5" class="team_top">
								<table class="table activitylist_bodyer_table" style="margin:0 auto;">
									<thead>
										<tr>
											<th class="tc" width="4%">姓名</th>
											<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
											<th class="tc" width="7%">护照号</th>
											<th class="tc" width="7%">签证类型</th>
											<th class="tc" width="6%">签证国家</th>
											<th class="tc" width="7%">实际出团时间</th>
											<th class="tc" width="7%">实际约签时间</th>
											<th class="tc" width="7%">签证状态</th>
											<th class="tc" width="6%">领取人</th>
											<th width="3%" class="tc">操作</th>	
										</tr>
									</thead>
								</table>
								<div class="table_activity_scroll">
									<table class="table activitylist_bodyer_table ">
										<tbody id="travelerList_${page3.batchNo }">
										
										</tbody>
									</table>
								</div>
							</td>
						</tr>
					  </c:forEach>
					</tbody>
				</table>
				<div class="pagination clearFix">
					 ${pageStr3}
				</div>
			</div>
			<div id="hhzyitijiao">
				<div class="activitylist_bodyer_right_team_co_paixu">
			  <div class="activitylist_paixu">
				<div class="activitylist_paixu_left">
				  <ul>
										<li >排序</li>
										<c:choose>
					                    	<c:when test="${page4.orderBy == 'u.createTime DESC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="hhzsortby_ytj('u.createTime',this)">
						                    			创建时间
						                    			<i class="icon icon-arrow-down"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	
					                    	
					                    	<c:when test="${page4.orderBy == 'u.createTime ASC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="hhzsortby_ytj('u.createTime',this)">
						                    			创建时间
					                    				<i class="icon icon-arrow-up"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	<c:otherwise>
					                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
					                    			<a onclick="hhzsortby_ytj('u.createTime',this)">
						                    			创建时间
					                    			</a>
					                    		</li>
					                    	</c:otherwise>
					                    </c:choose>
									</ul>
				</div>
				<div class="activitylist_paixu_right">
				<c:choose>
							 	<c:when test="${page4.count >0}">
							 	查询结果&nbsp;&nbsp;<strong>${page4.count}</strong>&nbsp;条
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
						<th width="12%">申请时间</th>
						<th width="12%">护照归还状态</th>
						<th width="10%">操作</th>
					</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(page4.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${page4.list }" var="page4">
						<tr>
							<td>${page4.batchNo }</td>
							<td class="tc">${page4.createUserName }</td>
							<td class="tc"><fmt:formatDate value="${page4.createTime }" pattern="yyyy-MM-dd"/></td>
							<td class="tc">已还</td>
							<td class="tc tda">
								<a class="team_a_click" onclick="expand('${page4.batchNo }',this,'${page4.visaIds }','submit')"  href="javascript:void(0)">游客列表</a>
							</td>
						</tr>
						<tr id="child_${page4.batchNo }" class="activity_team_top1" style="display:none">
							<td colspan="5" class="team_top">
								<table class="table activitylist_bodyer_table" style="margin:0 auto;">
									<thead>
										<tr>
											<th class="tc" width="4%">姓名</th>
											<c:if test="${fns:getUser().company.uuid  eq '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">订单团号</th>
												</c:if>
												<c:if test="${fns:getUser().company.uuid  ne '7a816f5077a811e5bc1e000c29cf2586'}">
												    <th class="tc" width="7%">团号</th>
												</c:if>
											<th class="tc" width="7%">护照号</th>
											<th class="tc" width="7%">签证类型</th>
											<th class="tc" width="6%">签证国家</th>
											<th class="tc" width="7%">实际出团时间</th>
											<th class="tc" width="7%">实际约签时间</th>
											<th class="tc" width="7%">签证状态</th>
											<th class="tc" width="6%">领取人</th>
											<th width="3%" class="tc">操作</th>	
										</tr>
									</thead>
								</table>
								<div class="table_activity_scroll">
									<table class="table activitylist_bodyer_table ">
										<tbody id="travelerList_${page4.batchNo }">
											
										</tbody>
									</table>
								</div>
							</td>
						</tr>
					  </c:forEach>
					</tbody>
				</table>
				<div class="pagination clearFix">
					 ${pageStr4}
				</div>
			</div>
		</div>
		<!--右侧内容部分结束-->
	</div>

	<!--66 批量设置担保类型 START-->
	<div id="setGuaranteeType" class="hide">
		<!--右侧内容部分开始-->
		<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
			<form method="post" action="${ctx}/visa/order/visaBatchEditRecordList" id="searchForGua">
				<input value="" type="hidden" id="showListGua" name="showList"/>
				<input id="pageNogua" name="pageNo" type="hidden" value="${page4Gua.pageNo}"/>
				<input id="pageSizegua" name="pageSize" type="hidden" value="${page4Gua.pageSize}"/>
				<input id="orderBygua" name="orderBy" type="hidden" value="${page4Gua.orderBy}"/>

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
						<label class="activitylist_team_co3_text" style="width:85px;">创建时间：</label>
						<input readonly="" onclick="WdatePicker()" value="${createTimeStart}" name="createTimeStart" class="inputTxt dateinput" id="createTimeStart">
						<span> 至 </span>
						<input readonly="" onclick="WdatePicker()" value="${createTimeEnt}" name="createTimeEnt" class="inputTxt dateinput" id="createTimeEnt">
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">游客名称：</div>
						<input value="${travellerName}" name="travellerName" class="inputTxt" id="travellerName">
					</div>

					<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">签证国家：</div>
						<select value="" class="inputTxt" name="country">
							<option value="">全部</option>
							<c:forEach items="${countrys}" var="countryInfo">
								<option value="${countryInfo.id}" <c:if test="${country eq countryInfo.id}">selected</c:if>>${countryInfo.countryName_cn}</option>
							</c:forEach>
						</select>
					</div>
					<!-- 占位 -->
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">签证领区：</label>
						<select value="" class="inputTxt" name="area">
							<option value="">全部</option>
							<c:forEach items="${fromAreaMapList}" var="fromArea">
								<option value="${fromArea.value}" <c:if test="${area eq fromArea.value}">selected</c:if>>${fromArea.label}</option>
							</c:forEach>
						</select>
					</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1" >
						<label class="activitylist_team_co3_text" >担保类型：</label>
						<select class="inputTxt" name="guaranteeType">
							<option value="">全部</option>
							<option value="1" <c:if test="${guaranteeType eq 1}">selected</c:if>>担保</option>
							<option value="2" <c:if test="${guaranteeType eq 2}">selected</c:if>>押金+担保</option>
							<option value="3" <c:if test="${guaranteeType eq 3}">selected</c:if>>押金</option>
							<option value="4" <c:if test="${guaranteeType eq 4}">selected</c:if>>无需担保</option>
						</select>
					</div>
					<!-- 占位 -->
					<div class="form_submit" style="margin-left:47px">
						<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
						<input type="button" value="条件重置" onclick="resetSearchParamsgua()" class="btn btn-primary ydbz_x">
					</div>

				</div>
			</form>
			<!--查询结果筛选条件排序开始-->

			<div id="mqtzdaitijiao">
				<div class="activitylist_bodyer_right_team_co_paixu">
					<div class="activitylist_paixu">
						<div class="activitylist_paixu_left">
							<ul>
								<li>排序</li>
								<li class="activitylist_paixu_left_biankuang licreateTime">
									<a onclick="guasortby('createTime',this)">
										创建时间
									</a>
								</li>
							</ul>
						</div>
						<div class="activitylist_paixu_right">
							查询结果&nbsp;&nbsp;
							<strong>${page4Gua.count}</strong>
							&nbsp;条
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
							<th width="12%">创建时间</th>
							<th width="10%">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${fn:length(page4Gua.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
						</c:if>
						<c:forEach items="${page4Gua.list }" var="pageGua">
							<tr>
								<td class="tc">${pageGua.batch_no}</td>
								<td class="tc">${pageGua.userName}</td>
								<td class="tc"><fmt:formatDate value="${pageGua.createTime}" pattern="yyyy-MM-dd"/></td>
								<td class="tc tda">
									<a class="team_a_click" onclick="expandTravelers('${pageGua.batch_no}',this)" href="javascript:void(0)">游客列表
									</a>
								</td>
							</tr>
							<tr id="child_${pageGua.batch_no}" class="activity_team_top1" style="display:none">
								<td colspan="7" class="team_top">
									<table class="table activitylist_bodyer_table" style="margin:0 auto;">
										<thead>
											<tr>
												<th class="tc">销售</th>
												<th class="tc">游客姓名</th>
												<th class="tc">护照号</th>
												<th class="tc">签证国家</th>
												<th class="tc">签证领区</th>
												<th class="tc">担保类型</th>
												<th class="tc">押金金额</th>
												<th class="tc">操作</th>
											</tr>
										</thead>

										<tbody id="travelerGuaList_${pageGua.batch_no}">
											<tr>
												<td class="tc">张三</td>
												<td class="tc">李四</td>
												<td class="tc">xxxxxxxx</td>
												<td class="tc">美国</td>
												<td class="tc">北京</td>
												<td class="tc">押金</td>
												<td class="tc">￥1111</td>
												<td class="tc"><a href="#">查看</a></td>
											</tr>
										</tbody>
									</table>
								</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
				<div class="pagination clearFix">
					${page4Gua}
				</div>
			</div>

		</div>

	</div>

	<!--66 批量设置担保类型内容 END-->
	
	<div id="mianqiantongzhi">
		<div class="header-search"  id="headerSearch">
		<form method="post" action="${ctx}/visa/order/visaBatchEditRecordList" id="searchFormMqtz">
			<input id="pageNoMqtz" name="pageNo" type="hidden" value="${pageMqtz.pageNo}"/>
			<input id="pageSizeMqtz" name="pageSize" type="hidden" value="${pageMqtz.pageSize}"/>
			<input id="orderBy" name="orderBy" type="hidden" value="${pageMqtz.orderBy}"/>
			<input type="hidden" id="showListMqtz" name="showList" value=""/>
			<!-- <input value="" type="hidden" id="saveOrSubmit" name="saveOrSubmit"/> -->
			
			<div class="activitylist_bodyer_right_team_co">
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">批次编号：</div>
					<input value="${batchNo}" class="inputTxt"  name="batchNo"  id=""> 
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">操作人：</div>
				   <input value="${txnPerson}" class="inputTxt"  name="txnPerson" id="txnPerson"> 
				</div>
				<div class=" activitylist_bodyer_right_team_co2" >
					<label class="activitylist_team_co3_text" style="width:85px;">创建时间：</label>
					<input readonly="" onclick="WdatePicker()" value="${createTimeStart}" name="createTimeStart" class="inputTxt dateinput" id="createTimeStart"> 
					<span> 至 </span>  
					<input readonly="" onclick="WdatePicker()" value="${createTimeEnt}" name="createTimeEnt" class="inputTxt dateinput" id="createTimeEnt">
				</div>
					<div class="kong"></div>
					<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">游客名称：</div>
				   <input value="${travellerName}" name="travellerName" class="inputTxt"  id="travellerName"> 
				</div>
				<div class="activitylist_bodyer_right_team_co1">
						<div class="activitylist_team_co3_text">签证国家：</div>
						<select value="" class="inputTxt" name="country" id="country">
							<option value="">全部</option>
							<c:forEach items="${countrys}" var="countryInfo">
								<option value="${countryInfo.id}" <c:if test="${country eq countryInfo.id}">selected</c:if>>${countryInfo.countryName_cn}</option>
							</c:forEach>
						</select>
					</div>
					<!-- 占位 -->
					<div class=" activitylist_bodyer_right_team_co2" style="width:37%;">
						<label class="activitylist_team_co3_text" style="width:85px;">签证领区：</label>
						<select value="" class="inputTxt" name="area" id="area">
							<option value="">全部</option>
							<c:forEach items="${fromAreaMapList}" var="fromArea">
								<option value="${fromArea.value}" <c:if test="${area eq fromArea.value}">selected</c:if>>${fromArea.label}</option>
							</c:forEach>
						</select>
					</div>
				<%-- <div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">签证国家：</div>
					<input value="${country }" name="country" id="country" class="inputTxt" > 
				</div>
				<div class="activitylist_bodyer_right_team_co1">
					<div class="activitylist_team_co3_text">签证领区：</div>
					<input value="${area }" name="area" id="area" class="inputTxt" > 
				</div> --%>
				<div class="kong"></div>
				<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位 -->
				<div class="form_submit">
					<input type="submit"  value="搜索"  class="btn btn-primary ydbz_x">
					<input type="button" value="条件重置" onclick="resetSearch()"  class="btn btn-primary ydbz_x">
				</div>
			</div>
		</form>	
		</div>
		<div class="content">
			<div class="nav">
				<div class="nav-left">
					<ul>
						<li>排序</li>
						<c:choose>
							<c:when test="${pageMqtz.orderBy == 'u.createTime DESC'}">
								<li class="activitylist_paixu_moren">
									<a onclick="mqtzSortby('u.createTime',this)">
											创建时间
										<i class="icon icon-arrow-down"></i>
									</a>
								</li>
							</c:when>
							<c:when test="${pageMqtz.orderBy == 'u.createTime ASC'}">
								<li class="activitylist_paixu_moren">
									<a onclick="mqtzSortby('u.createTime',this)">
											创建时间
										<i class="icon icon-arrow-up"></i>
									</a>
								</li>
							</c:when>
						<c:otherwise>
									<li class="activitylist_paixu_left_biankuang lipro.updateDate">
										<a onclick="mqtzSortby('u.createTime',this)">
											创建时间
										</a>
									</li>
								</c:otherwise>
						</c:choose>
						<!-- <li class="bck-img">
							<a>创建时间</a>
						</li> -->
					</ul>
				</div>
				<div class="nav-right"> 
				<c:choose>
					<c:when test="${pageMqtz.count gt 0 }">查询结果&nbsp;&nbsp;<strong>${pageMqtz.count }</strong>&nbsp;条</c:when>
					<c:otherwise>查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条</c:otherwise>
				</c:choose>
				</div>
			</div>
			<table border="1" class="main-table"  id="mainTable">
				<thead>
					<tr>
						<th>批次</th>
						<th>操作人</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${fn:length(pageMqtz.list) <= 0 }">
							<tr class="toptr" >
								<td colspan="15" style="text-align: center;">没有符合条件的记录</td>
							</tr>
					</c:if>
					<c:forEach items="${pageMqtz.list }" var="pageMqtz">
					<tr>
						<td >${pageMqtz.batchNo }</td>
						<td  class="ac">${pageMqtz.txnPerson }</td>
						<td  class="ac"><fmt:formatDate value="${pageMqtz.createTime }" pattern="yyyy-MM-dd"/></td>
						<td  class="ac"><a  class="team_a_click"  onclick="showVisitorList('${pageMqtz.batchNo }',this);">游客列表</a></td>
					</tr>
					<tr id="childMqtz_${pageMqtz.batchNo }" class="sub-tr" style="display:none;">
						<td  colspan="4">
							<table  class="sub-table">
							    <thead>
									<tr>
										<th style="width:7%;">销售</th>
									    <th style="width:7%;">游客姓名</th>
										<th style="width:10%;">签证国家</th>
										<th style="width:8%;">签证领区</th>
										<th style="width:15%;">约签时间</th>
										<th style="width:15%;">说明会时间</th>
										<th style="width:10%;">预约地点</th>
										<th style="width:7%;">联系人</th>
										<th style="width:10%;">联系方式</th>
										<th style="width:7%;">操作</th>
									</tr>
								</thead>
								
							</table>
							<div  class="sub-content">
								<table  class="sub-table sub-table-second">
									<tbody id="travelerList_${pageMqtz.batchNo }">
											
									</tbody>
								</table>
							</div>
						</td>
					</tr>
					</c:forEach>
				</tbody>
			</table>
			<div class="pagination clearFix">
				${pageMqtzStr }
			</div>
		</div>
		<!-- </div> -->
	</div>
	
	
	
	<!--0064需求,游客加入批次-s-批发商环球行 -解决待提交和已提交更新页面关于排序方式:更新时间的展示问题 -->
	<script type="text/javascript">
	    $(function(){
	    	var tempytj=$("#jkytj");
	    	if('select'==tempytj.attr("class")){ //利用的是已提交的的class="select"的属性
	    		$("[name='li4UpdateTime']").css("display","none");//隐藏"更新时间"
	    	}else{
	    		$("[name='li4UpdateTime']").css("display","inline-block");//显示"更新时间"
	    	}
	    });
	</script>
	<!-- 0064需求,游客加入批次-e -->
</body>
</html>
