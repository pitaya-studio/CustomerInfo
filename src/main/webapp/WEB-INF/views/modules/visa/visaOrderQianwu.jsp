<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>  
<%@ page import="com.trekiz.admin.modules.sys.utils.UserUtils" %>

<%
String flag= "";
if (UserUtils.getUser().getCompany().getIsNeedAttention() == 1) 
flag = "1";
else
	flag="0";
%>

<head>
<meta name="decorator" content="wholesaler"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>订单-签务签证订单</title>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.datepickerRefactor.css" rel="stylesheet" type="text/css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />

<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-migrate-1.js"></script>
<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/jquery-jbox/2.3/i18n/jquery.jBox-zh-CN.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/jquery-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
<script type="text/javascript" src="${ctxStatic}/modules/visa/visaOrderList.js"></script>

<style type="text/css">

    .td-extend .handle {
    background-image: none;
    
}

.xuanfu {
    position: absolute;
    /* right: -6px; */
    top: 0px;
    left: 0;
    right:130px;
}

#hoverWindow {
    text-align:left;
    width:35em;
    top: 29%;
    left: 0%;
    word-wrap: break-word;
    
}

.activity_team_top1 .team_top table thead tr th{
    border-right: 1px solid #c1cde3;
}

</style>

<script type="text/javascript">
function selectQuery(){
	$("#sysCountryId" ).comboboxInquiry();
    $("#agentinfoId" ).comboboxInquiry();
    $("#createBy" ).comboboxInquiry();
	$("#salerId" ).comboboxInquiry();
}
$(function(){


g_context_url = "${ctx}";
	//搜索条件显示隐藏
	launch();
	//文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	show('${flag}','${showList}');
	//产品名称团号切换
	switchNumAndPro();
	//产品销售和下单人切换
	switchSalerAndPicker();
	//订单应收金额和未收余额切换
	switchYingshouAndWeishou();
	selectQuery();


    $("#visa").hover(function () 
		{  
	    	$("#tm").val(new Date().getTime());
	    },   
        function () 
	    {  
	    	var tm2  = new Date().getTime();
	    	var tm1= $("#tm").val();
	    	//alert(tm2 - tm1); 执行更新fangfa
        });

	//渠道联系人增加"..."
	sliceAgentContacts();
    
    
});

function downloads(docIds,fileName){
	 
	 var doc = docIds.replace(/[ ]/g,"");
		if(doc==null || doc==""){
			$.jBox.tip("文件不存在");
			return;
		}
		var fileUrl =encodeURI("${ctx}/sys/docinfo/fileExists/" + doc + "/"+fileName);
		
		$.ajax({ url:encodeURI(fileUrl) ,async:false, success: function(result){
			
			if("文件存在"==result){
				  window.open(encodeURI(encodeURI("${ctx}/sys/docinfo/zipdownload/" + doc + "/"+fileName)));
			  } else{
				  $.jBox.tip("文件不存在");
			  }
	      }});

	}

/*订单列表-下载-确认单*/
function downloadData(orderId) {
	window.open(encodeURI(encodeURI(g_context_url + "/sys/docinfo/download/" + orderId)));
}


//订单列表全选
//var activityIds = "";
function checkall(obj,orderId){
	if(obj.checked){ 
		$("input[name='activityId"+orderId+"']").not("input:checked").each(function(){this.checked=true;}); 
		$("input[name='allChk"+orderId+"']").not("input:checked").each(function(){this.checked=true;});
	}else{ 
		$("input[name='activityId"+orderId+"']:checked").each(function(){this.checked=false;}); 
		$("input[name='allChk"+orderId+"']:checked").each(function(){this.checked=false;});	
	} 
}
function checkallNo(obj,orderId){
	$("input[name='activityId"+orderId+"']").each(function () {   
      $(this).attr("checked", !$(this).attr("checked"));   
  }); 
	allchk(orderId);
}
//每行中的复选框
function idcheckchg(obj,orderId){
	if(obj.checked){
		if($("input[name='activityId"+orderId+"']").not("input:checked").length == 0){
			$("input[name='allChk"+orderId+"']").not("input:checked").each(function(){this.checked=true;});
		}
	}else{
		$("input[name='allChk"+orderId+"']:checked").each(function(){
			this.checked=false;	
		});
	}
}
function allchk(orderId){ 
  var chknum = $("input[name='activityId"+orderId+"']").size(); 
  var chk = 0; 
  $("input[name='activityId"+orderId+"']").each(function () {   
      if($(this).attr("checked")==true){ 
          chk++; 
      } 
  }); 
  if(chknum==chk){//全选 
      $("input[name='allChk"+orderId+"']").attr("checked",true); 
  }else{//不全选 
      $("input[name='allChk"+orderId+"']").attr("checked",false); 
  } 
} 

//批量还收据
function batchHuanshouju(aId,checkName){
	///获取选中的游客复选框的值
	var str=document.getElementsByName(checkName);
	var objarray=str.length;
	var chestr="";
	var agentId ="";
	for (var i=0;i<objarray;i++){
	  if(str[i].checked == true)
	  {
	   var tid=str[i].value.split("@")[0];
	   chestr+=str[i].value+",";
	  }
	}
	//chestr就是游客id
	if("" == chestr || chestr.length ==0){
		top.$.jBox.tip('至少选择一位游客');
        return false;
	}else{
		//var obj = document.getElementById(aId);
    	//obj.href = "${ctx}/visa/order/batchPay?parString="+chestr; 
	}
}

//游客列表全选
//var activityIds = "";
function t_checkall(obj){
	if(obj.checked){ 
		$("input[name='activityId']").not("input:checked").each(function(){this.checked=true;}); 
		$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
	}else{ 
		$("input[name='activityId']:checked").each(function(){this.checked=false;}); 
		$("input[name='allChk']:checked").each(function(){this.checked=false;});	
	} 
}
function t_checkallNo(obj){
	$("input[name='activityId']").each(function () {   
    $(this).attr("checked", !$(this).attr("checked"));   
}); 
	t_allchk();
}
//每行中的复选框
function t_idcheckchg(obj){
	if(obj.checked){
		if($("input[name='activityId']").not("input:checked").length == 0){
			$("input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
		}
	}else{
		$("input[name='allChk']:checked").each(function(){
			this.checked=false;	
		});
	}
}
function t_allchk(){ 
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

function show(flag,showList){
// 	if("zhankai"== flag){
//		document.getElementById("showFlag").value="zhankai";
//		if($('.ydxbd').is(":hidden")==true) {
//			document.getElementById("showFlag").value="zhankai";
//			$('.ydxbd').show();
//			$('.zksx').text('收起筛选');
//			$('.zksx').addClass('zksx-on');
//		}
//	}
 	if("youke" ==showList){
 		 $("#showList").val("youke");
 		 document.getElementById("dingdanliebiao").className="";
 		 document.getElementById("youkeliebiao").className="select";
 		 document.getElementById("youke").style.display='block';
 		 document.getElementById("dingdan").style.display='none';
 	}else{
 		 $("#showList").val("dingdan");
 		 document.getElementById("dingdanliebiao").className="select";
 		 document.getElementById("youkeliebiao").className="";
 		 document.getElementById("dingdan").style.display='block';
 		 document.getElementById("youke").style.display='none';
 	}
}					

function showOrder(){
	 $("#showList").val("dingdan");
	 document.getElementById("dingdanliebiao").className="select";
	 document.getElementById("youkeliebiao").className="";
	 document.getElementById("dingdan").style.display='block';
	 document.getElementById("youke").style.display='none';
	 $("#pageNo").val(1);
	 $("#pageSize").val(10);
	 $("#youkePageNo").val(1);
	 $("#youkePageSize").val(10);
	 $("#searchForm").submit();
}
function exportExcelUserList(){
	var sysCtx = $("#sysCtx").val();
	$('#searchForm').attr("action",  sysCtx + "/visa/order/exportExcelUserList");
	$("#searchForm").submit();
}

function searchOrderList(){
	var sysCtx = $("#sysCtx").val();
	$('#searchForm').attr("action",  sysCtx + "/visa/order/searchqw");
	$("#searchForm").submit();
}

function showTraveler(){
	 $("#showList").val("youke");
	 document.getElementById("dingdanliebiao").className="";
	 document.getElementById("youkeliebiao").className="select";
	 document.getElementById("youke").style.display='block';
	 document.getElementById("dingdan").style.display='none';
	 $("#pageNo").val(1);
	 $("#pageSize").val(10);
	 $("#youkePageNo").val(1);
	 $("#youkePageSize").val(10);
	 $("#searchForm").submit();
	}


//保留金额value的小数点后num位，不足补零
function formatnumber(value, num) {  
	var a, b, c, i;
	debugger;
    a = value.toString();
    if(a.indexOf(".")==0){}
    else{
    	a = a.replace(/\b(0*)/,"");//去掉整数前面的0
    }
    b = a.indexOf(".");  
    c = a.length;  
    if (num == 0) {  
        if (b != -1) {  
        	a = a.substring(0, b);  
        }  
    } else {  
        if (b == -1) {  
        	a = a + ".";  
            for (i = 1; i <= num; i++) {  
                a = a + "0";  
            }  
        } else {  
            if(b == 0){
            	a = "0" + a;
            	a = a.substring(0, b + num + 2);  
            	for (i = c; i <= b + num; i++) {  
                	a = a + "0";  
            	}  
            }else{
        		a = a.substring(0, b + num + 1);  
            	for (i = c; i <= b + num; i++) {  
                	a = a + "0";  
            	}  
            }	
        }  
    }  
    return a;  
}  

//订单-签务身份订单-交押金
//id:visa表的主键
function jbox_jyj_qianwu(visaId,travelerId,yingshouId,type) {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p><label class="jbox-label">选择币种：</label><select id="currencyId" class="jbox-width100"><c:forEach items="${currencyList}" var="currency"><option value ="${currency.id}">${currency.currencyName}</option></c:forEach></select></p><label class="jbox-label">押金金额：</label><input id="yingshouyajin" type="text" onafterpaste="refundInput(this)" onkeyup="refundInput(this)" class="jbox-width100" value=""> ';
	html += '</div>';
	if(type == 3) {
		$pop = $.jBox(html, { title: "确定需交押金？",buttons:{'需交押金': 1,'取消':0}, submit:function(v, h, f){
			if (v=="1"){
				var value = $pop.find("#yingshouyajin").val();

				if(""==value.trim()){
					alert("请输入押金金额！");
					return false;
				}
				if("0"==value.trim() || value.trim() < 0){
					alert("押金金额必须大于0！");
					return false;
				}
				value = formatnumber(value, 2);//保留2位小数，不足补零
				var currencyId = $pop.find("#currencyId").val();
				$.ajax({
					type: "POST",
					url: g_context_url+"/visa/order/addDeposit",
					data:{
						totalDeposit:value,
						visaId:visaId,
						travelerId:travelerId,
						currencyId:currencyId
					},
					success: function(msg){
						//var text = document.getElementById("currencyId").text;
						//document.getElementById("traveleryingshouyajin"+visaId+travelerId).value = text.value;
						top.$.jBox.tip(msg.message);
						if("担保类型修改成功" == msg.message ){
							document.getElementById(yingshouId).innerHTML  = msg.mark + msg.value;
						}
					}
				});
			}
		},height:200,width:380});
	} else if (type == 2) {
		$pop = $.jBox($("#setYajindanbao").html(), {
			title: "设置押金+担保金额", buttons: {'取消': 0, '提交': 1}, submit: function (v, h, f) {
				if (v == "1") {
					var value = $pop.find("#yingshouyajin").val();

					if(""==value.trim()){
						alert("请输入押金金额！");
						return false;
					}
					if("0"==value.trim() || value.trim() < 0){
						alert("押金金额必须大于0！");
						return false;
					}
					value = formatnumber(value, 2);//保留2位小数，不足补零
					var currencyId = $pop.find("#currencyId").val();
					$.ajax({
						type: "POST",
						url: g_context_url+"/visa/order/addDeposit",
						data:{
							totalDeposit:value,
							visaId:visaId,
							travelerId:travelerId,
							currencyId:currencyId
						},
						success: function(msg){
							//var text = document.getElementById("currencyId").text;
							//document.getElementById("traveleryingshouyajin"+visaId+travelerId).value = text.value;
							top.$.jBox.tip(msg.message);
							if("担保类型修改成功" == msg.message ){
								document.getElementById(yingshouId).innerHTML  = msg.mark + msg.value;
							}
						}
					});
				}
			}, height: 250, width: 300
		});
	}
}

function expand(order,orderId, obj,lockStatus,activityLockStatus,main_orderId) {
	//国家的id
	var companyId="${fns:getUser().company.id }";
	//供应商唯一标识uuid
	var companyUuid="${fns:getUser().company.uuid }";
	var ttsFlag=true;
	if(companyId=='68'){
		ttsFlag=false;
	}

	if($(order).is(":hidden")){
			$(obj).html("收起客户");
			$(obj).parents('tr').addClass('tr-hover');
			$(order).show();
			$(obj).parents("td").addClass("td-extend");
			$.ajax({
				cache:true,
				type: "POST",
				url: "${ctx}/visa/order/searchTravelerByOrderId",
				data:{
					orderId:orderId,
					shenfen:"qianwu"
				},
				async:true,
				success: function(traveler){
						var html="";
						
						for(var i = 0,len = traveler.length; i<len; i++){
							html += '<tr>';
							html += '<td width="8%" class="p0">';
							html += '<span id="traveler_travelerName" class="sqcq-fj">';
							if(ttsFlag){
								html += '<input type="checkbox" class="tdCheckBox" trallerId="'+traveler[i].id+'" visaId="'+traveler[i].visaId+'" trallerName="'+traveler[i].travelerName+'" value="'+traveler[i].id+'@'+orderId+'" name="activityId'+orderId+'" onclick="idcheckchg(this,'+orderId+')"/>';
							}
							html += traveler[i].travelerName;
							html += '</span>';
							html += '<input type="hidden" value="'+traveler[i].agentinfoId+'" id="activityId_agentId_'+traveler[i].id+'">';
							html += '</td>';
							html += '<td width="7%">';
							html += '<span id="traveler_passportId">'+traveler[i].passportId+'</span>';
							html += '</td>';
							html += '<td width="3%">';
							html += '<span id="traveler_visaType">'+traveler[i].visaType+'</span>';
							html += '</td>';
							html += '<td width="3%">';
							html += '<span id="traveler_visaCountry">'+traveler[i].visaCountry+'</span>';
							html += '</td>';
							html += '<td width="3%">';
							html += '<span id="traveler_visaArea">'+traveler[i].collarZoning+'</span>';
							html += '</td>';
							html += '<td width="6%">';
							html += '<span>'+traveler[i].groupType+'</span>';
							html += '</td>';
							html += '<td width="7%" class="tc">';
							html += '<div class="yfje_dd">';
							html += '<span class="fbold">';
							html += '<span id="traveler_forecastStartOut">'+traveler[i].forecastStartOut+'</span>';
							html += '</span>';
							html += '</div>';
							html += '<div class="dzje_dd">';
							html += '<span class="fbold" >';
							html += '<input id="traveler_startOut_'+traveler[i].id+'" value="'+traveler[i].startOut+'" onclick="WdatePicker()" name="" type="text" />';
							html += '</span>';
							html += '</div>';
							html += '</td>';
							
							html += '<td width="7%" class="tc">';
							html += '<div class="yfje_dd">';
							html += '<span class="fbold">';
							html += '<span id="traveler_forecastContract">'+traveler[i].forecastContract+'</span>';
							html += '</span>';
							html += '</div>';
							/* c482懿洋假期美国签证的游客列表和订单列表 
							管理员：7a816f5077a811e5bc1e000c29cf2586
							懿洋：f5c8969ee6b845bcbeb5c2b40bac3a23
							*/
							if(companyUuid=='f5c8969ee6b845bcbeb5c2b40bac3a23' && traveler[i].visaCountry=='美国'){
							/*隐藏实际约签时间  */
							html += '<div class="dzje_dd" style="display: none">';
							}else{
							/*显示实际约签时间  */
							html += '<div class="dzje_dd">';
							}							
							html += '<span class="fbold" >';
							html += '<input id="traveler_contract_'+traveler[i].id+'" value="'+traveler[i].contract+'" onclick="WdatePicker({dateFmt:\'yyyy-MM-dd HH:mm\'})" name="" type="text" />';
							html += '</span>';
							html += '</div>';
							html += '</td>';
							html +='<td width="8%" class="tc">';
							//说明会时间 
							html += '<div class="yfje_dd">';
							html += '<span class="fbold">';
							html += '<span id="traveler_explanationMeeting" style="color:green">'+traveler[i].explanationTime+'</span>';
							html += '</span>';
							html += '</div>';
							//end
							html +='<div class="dzje_dd">';
							html +='<span class="fbold" >';
							html +='<input id="actual_delivery_time_'+traveler[i].id+'" value="'+traveler[i].deliveryTime+'" onclick="WdatePicker()" name="" type="text" />';
							html +='</span>';
							html +='</div>';
							html +='</td>';
							
							html +='<td width="5%">';
							html +='<select id="traveler_visaStatus_'+traveler[i].id+'" name="" style="width:100%;">';
							html +='<option value="-1" selected="selected">请选择</option>';
							html +='<c:forEach items="${visaStatusList}" var="visaStatus">';
							if("${visaStatus.value }" == 2){
								if("${visaStatus.value }" == traveler[i].visaStatus){
									html +='<option selected="selected" value="2">已约签</option>';
								}else{
									html +='<option value="2">已约签</option>';
								}
							}else if("${visaStatus.value}" == 3){
								if("${visaStatus.value }" == traveler[i].visaStatus){
									html +='<option selected="selected" value="3">通过</option>';
								}else{
									html +='<option value="3">通过</option>';
								}
							}else{
								if("${visaStatus.value }" == traveler[i].visaStatus){
									html +='<option selected="selected" value="${visaStatus.value}">${visaStatus.label}</option>';
								}else{
									html +='<option value="${visaStatus.value}">${visaStatus.label}</option>';
								}
							}
							html +='</c:forEach>';
							html +='</select>';
							html +='</td>';
							html +='<td width="5%">';
							html +='<input type="hidden" id="hidden_passportStatus_'+traveler[i].id+'" value="'+traveler[i].passportStatus+'">';
							html +='<select id="passportStatus_'+traveler[i].id+'"  name="" style="width:100%;">';
							html +='<option value="0" selected="selected">请选择</option>';
							if('1'==traveler[i].passportStatus){
								html +='<option value="1" selected="selected">借出</option>';
							}else{
								html +='<option value="1">借出</option>';
							}
							if('2'==traveler[i].passportStatus){
								html +='<option selected="selected" value="2">销售已领取</option>';
							}else{
								html +='<option value="2">销售已领取</option>';
							}
							if('4'==traveler[i].passportStatus){
								html +='<option selected="selected" value="4" >已还</option>';
							}else{
								html +='<option value="4" >已还</option>';
							}
							if('5'==traveler[i].passportStatus){
								html +='<option selected="selected" value="5" >已取出</option>';
							}else{
								html +='<option value="5" >已取出</option>';
							}
							if('6'==traveler[i].passportStatus){
								html +='<option selected="selected" value="6" >未取出</option>';
							}else{
								html +='<option value="6" >未取出</option>';
							}
							if('8'==traveler[i].passportStatus){
								html +='<option selected="selected" value="8" >计调领取</option>';
							}else{
								html +='<option value="8" >计调领取</option>';
							}
							html +='</select>';
							html +='</td>';
							html +='<td width="6%">';
							html +='<select id="traveler_guaranteeStatus_'+traveler[i].id+'" onchange="xuanze(\''+traveler[i].visaId+'\',\''+traveler[i].id+'\',\'traveler_guaranteeStatus_'+traveler[i].id+'\',\'traveleryingshouyajin_'+traveler[i].id+'\','+traveler[i].visaorderId+')" name="" style="width:100%;">';
							if('0'==traveler[i].guaranteeStatus){
								html +='<option selected="selected" value="0" >请选择</option>';
							}else{
								html +='<option value="0" >请选择</option>';
							}
							if('1'==traveler[i].guaranteeStatus){
								html +='<option value="1" selected="selected">担保</option>';
							}else{
								html +='<option value="1">担保</option>';
							}
							if('2'==traveler[i].guaranteeStatus){
								html +='<option  selected="selected" value="2">担保+押金</option>';
							}else{
								html +='<option value="2">担保+押金</option>';
							}
							if('3'==traveler[i].guaranteeStatus){
								html +='<option selected="selected"  value="3">押金</option>';
							}else{
								html +='<option value="3">押金</option>';
							}
							if('4'==traveler[i].guaranteeStatus){
								html +='<option selected="selected"  value="4">无需担保</option>';
							}else{
								html +='<option value="4">无需担保</option>';
							}
							html +='</select>';
							html +='</td>';
							var totalDeposit = "";
							if('2'==traveler[i].guaranteeStatus||'3'==traveler[i].guaranteeStatus){
								totalDeposit = traveler[i].totalDeposit;
							}
							html +='<td class="tr" id="traveleryingshouyajin_'+traveler[i].id+'">' + totalDeposit + '</td>';
							html +='<td width="7%" class="p0 tr">';
							html +='<div class="yfje_dd">';
							html +='<span class="fbold">';
							//if('3'==traveler[i].guaranteeStatus){
								html +=traveler[i].payedDeposit;
							//}
							html +='</span>';
							html +='</div>';
							html +='<div class="dzje_dd">';
							html +='<span class="fbold" id="accountedDeposit">';
							html +=traveler[i].accountedDeposit;
							html +='</span>';
							html +='</div>';
							html +='</td>';
							html +='<td width="5%" class="p0 tr">'+traveler[i].jiekuanStatus+'</td>';
							if('已收'==traveler[i].fukuanStatus){
								html +='<td width="5%" class="p0 tr" style="color:green;">'+traveler[i].fukuanStatus+'</td>';
							}else{
								html +='<td width="5%" class="p0 tr" style="color:red;">'+traveler[i].fukuanStatus+'</td>';
							}
							html +='<td class="tc" width="5%"><input class="btn btn-primary" type="button" value="保存" onclick="updateTraveler_qianwu(\''+lockStatus+'\',\'traveler_AACode_'+traveler[i].id+'\',\'traveler_visaStatus_'+traveler[i].id+'\',\'passportStatus_'+traveler[i].id+'\',\'traveler_guaranteeStatus_'+traveler[i].id+'\',\'traveler_startOut_'+traveler[i].id+'\',\'traveler_contract_'+traveler[i].id+'\',\''+traveler[i].id+'\',\''+traveler[i].visaId+'\',\'actual_delivery_time_'+traveler[i].id+'\','+traveler[i].visaorderId+')"></td>';
							html +='<td width="3%" class="p0">';
							html +='<dl class="handle">';
							html +='<dt><img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作"></dt>';
							html +='<dd>';
							html +='<p>';
							html +='<span></span>';
							if(companyId!='71'){
								if(lockStatus != 1 && activityLockStatus !=1){
									//html +='<a href="javascript:void(0)" onclick="jbox_hsj_qianwu(\''+traveler[i].id+'\');">还收据</a>';
								}
							}
							html +='<c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586'}">';
							html +='<a href="javascript:void(0)" onclick="jbox_jkmx_qianwu(\''+traveler[i].travelerName+'\',\''+traveler[i].passportId+'\',\''+traveler[i].creatUser+'\',\''+traveler[i].createTime+'\',\''+traveler[i].groupType+'\',\''+traveler[i].creatUser+'\',\''+traveler[i].visaorderNo+'\',\''+traveler[i].jiekuanCreateUser+'\',\''+traveler[i].jiekuanBizhong+traveler[i].jiekuanAmount+'\',\''+traveler[i].jiekuanRemarks+'\');">借款明细</a>';
							html +='</c:if>';
							
							html +='<c:if test="${companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586'}">';
							if(traveler[i].main_orderId == '' || traveler[i].main_orderId == null)
							html +='<a href="javascript:void(0)" onclick="jbox_jkmx_qianwu(\''+traveler[i].travelerName+'\',\''+traveler[i].passportId+'\',\''+traveler[i].creatUser+'\',\''+traveler[i].createTime+'\',\''+traveler[i].groupType+'\',\''+traveler[i].creatUser+'\',\''+traveler[i].visaorderNo+'\',\''+traveler[i].jiekuanCreateUser+'\',\''+traveler[i].jiekuanBizhong+traveler[i].jiekuanAmount+'\',\''+traveler[i].jiekuanRemarks+'\');">借款明细</a>';
							html +='</c:if>';
							
							
							
							
							if(lockStatus != 1 && activityLockStatus !=1){
								if("${jieKuanFlag}"=='true'){
									html +='<c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586'}">';
									//html +='<a href="javascript:void(0)" onclick="jbox_jk_qianwu(\''+traveler[i].id+'\',this);">借1款</a>';
									html +='</c:if>';
									
									html +='<c:if test="${companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586'}">';
									if(traveler[i].main_orderId == '' || traveler[i].main_orderId == null)
									html +='<a href="javascript:void(0)" onclick="jbox_jk_qianwu(\''+traveler[i].id+'\',this);">借款</a>';
									html +='</c:if>';
									
									
									
								}
								if('2'==traveler[i].guaranteeStatus||'3'==traveler[i].guaranteeStatus) {
									html += '<a href="javascript:void(0)" onclick="xiugaiyajin_qianwu(\'' + traveler[i].visaId + '\',\'' + traveler[i].totalDepositUUID + '\',\'' + traveler[i].id + '\',\'' + orderId + '\',\'traveleryingshouyajin' + traveler[i].visaId + traveler[i].id + '\') ">修改押金</a>';
								}
								html +='<a href="javascript:void(0)" onclick="jbox_jhz(\''+traveler[i].id+'\',\''+traveler[i].visaId+'\');">借护照</a>';
								html +='<a href="javascript:void(0)" onclick="jbox_hhz(\''+traveler[i].id+'\',\''+traveler[i].visaId+'\');">还护照</a>';
								if("${tuiYaJinFlag}"=='true'){
								//	html +='<a href="${ctx}/order/manager/visaDeposit/refundList?proId='+traveler[i].visaorderId+'">退签证押金</a>';
								}
								if("${huanYaJinShouJuFlag}"=='true'){
								//	html +='<a href="javascript:void(0)" onclick="jbox_hyjsj(\''+traveler[i].id+'\');">还押金收据</a>';
								}
								html +='<a href="javascript:void(0)" onclick="jbox_qszl_qianwu(\''+traveler[i].visaProductId+'\',\''+traveler[i].visaId+'\');">签收资料</a>';
							}
							html +='</p>';
							html +='</dd>';
							html +='</dl>';
							html +='</td>';
							html +='</tr>';
						}
						if(ttsFlag){
							html +='<tr class="checkalltd">';
							html +='<td colspan="18" class="tl">';
							html +='<label>';
							html +='<input type="checkbox" name="allChk'+orderId+'" onclick="checkall(this,'+orderId+')">全选';
							html +='</label>';
							html +='<label>';
							html +='<input type="checkbox" name="allChkNo" onclick="checkallNo(this,'+orderId+')">反选';
							html +='</label>';
							if(lockStatus != 1 && activityLockStatus !=1){
								if("${jieKuanFlag}"=='true'){
									html +='<c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586'}">';
									//html +='<a onclick="batchJk('+orderId+');">批量借款</a>';
									html +='</c:if>';
									
									
									html +='<c:if test="${companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586'}">';
									if(main_orderId == '' || main_orderId == null)
									//html +='<a onclick="batchJk('+orderId+');">批量借款</a>';
									html +='</c:if>';
									
								
									
									
									
									
									
									
									
									
									
									
									
								}
								//html +='<a id="piliang_o_huanshouju_'+orderId+'" onclick="batchHsj('+orderId+');">批量还收据</a>';
								html +='<a onclick="batchUpdatePassport('+orderId+',\'1\')">批量借护照</a>';
								html +='<a onclick="batchUpdatePassport('+orderId+',\'2\')">批量还护照</a>';
							}
							html +='<a onclick="batchUpdateVisaStatus('+orderId+')">批量操作签证</a>';
							if("${tuiYaJinFlag}"=='true'){
								html +='<a id="piliang_o_tuiyajin_'+orderId+'" onclick="batchtuiyajin(\'piliang_o_tuiyajin_'+orderId+'\',\'activityId'+orderId+'\');">批量退押金</a>';
							}
							html +='</td>';
							html +='</tr>';
						}
						$("#traveler_"+orderId).empty();
						$("#traveler_"+orderId).append(html);
				}
			});
	}else{
		if(!$(order).is(":hidden")){
			$(obj).parents('tr').removeClass('tr-hover');
			$(order).hide();
			$(obj).parents("td").removeClass("td-extend");
			$(obj).html("展开客户");
		}
	}
}

function expandPaidRecord(child,orderId,obj){
	if($(child).is(":hidden")){
		$(obj).parents('tr').addClass('tr-hover');
		$(child).show();
		$(obj).parents("td").addClass("td-extend");
		$.ajax({
				cache:true,
			 	type: "POST",
				url: "${ctx}/visa/order/paidRecords",
				data:{
	                orderId:orderId
	            },
	            async:true,
				success: function(list){		
					if(list.length == 0){
						$(obj).parents('tr').removeClass('tr-hover');
						$(child).hide();
						$(obj).parents("td").removeClass("td-extend");
						$.jBox.tip("没有收款信息!");
						return;
					}
						var html="";
						for(var i = 0,len = list.length; i<len; i++){
							html += '<tr><td class="tc">' + list[i][10] + '</td>';
							html += '<td class="tc">';
							if(list[i][0]==1){
								html += '支票支付';
							}else if(list[i][0]==2){
								html += 'POS支付';
							}else if(list[i][0]==3){
								html += '现金支付';
							}else if(list[i][0]==4){
								html += '汇款支付';
							}else if(list[i][0]==5){
								html += '快速支付';
							}else if(list[i][0]==6){
								html += '银行转账';
							}else if(list[i][0]==7){
								html +='汇票'
							}else if(list[i][0]==8){
								html +='POS机刷卡'
							}else if(list[i][0]==9){
								html +='因公支付宝'
							}else{
								html += ' ';
							}
							html += '</td><td class="tc">'+list[i][2]+'</td>';
							html += '<td class="tc">'+list[i][3]+'</td><td class="tc">';
							if(list[i][5]==1){
								html += '收全款';
							}else if(list[i][5]==2){
								html += '收尾款';
							}else if(list[i][5]==3){
								html += '收订金';
							}else if(list[i][5]==7){
								html += '收押金';
							}else if(list[i][5]==16){
								html += '收押金';
							}else{
								html += ' ';
							}
							html += '</td><td class="tc"><div  class="pr xuanfudiv">';
							if(list[i][4]==1){
								html += '已达账';
							}else if(list[i][4]==0){
								html += '已撤销';
							}else if(list[i][4]==2){
								html += '已驳回';
							}else{
								html += '未达账';
							}
							if(null!=list[i][9] && ""!=list[i][9]){
								html += '<div class="ycq xuanfu" style="width: 24px;">备注</div>';
								html += '<div class="hover-title team_top hide" id="hoverWindow">'+list[i][9]+'</div>';
							}
							
							html += '</td><td class="tc"><a  class="showpayVoucher" onclick="downloads(\''+list[i][8]+'\',\'收款凭证\')" >收款凭证</a></td>';
							html += '<td class="tc">'+'<a href=\"javascript:void(0)\" onClick=\"changepayVoucher('+list[i][6]+','+list[i][7]+')\">改收款凭证</a>'+'</td></tr>';
						}
					    $("#recordInfo_"+orderId).empty();
					    $("#recordInfo_"+orderId).append(html);
					
				}
			});
	}else{
		if(!$(child).is(":hidden")){
			$(obj).parents('tr').removeClass('tr-hover');
			$(child).hide();
			$(obj).parents("td").removeClass("td-extend");
		}
	}
	
	
}

/**
 * 修改支付凭证
 * 
 * param payId
 * param orderId
 */
function changepayVoucher(payId, orderId){
    window.open ("${ctx}/visa/order/modifypayVoucher/" + payId + "/" + orderId);
}

       
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
		    $("#searchForm").attr("action","${ctx}/visa/order/searchqw");
		    $("#orderBy").val(sortBy);
		    $("#searchForm").submit();
} 
 function youkesortby(sortBy,obj){
	    var temporderBy = $("#youkeOrderBy").val();
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
	    $("#searchForm").attr("action","${ctx}/visa/order/searchqw");
	    $("#youkeOrderBy").val(sortBy);
	    $("#searchForm").submit();
}

	
 function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#youkePageNo").val(n);
			$("#youkePageSize").val(s);
			$("#searchForm").attr("action","${ctx}/visa/order/searchqw");
			$("#searchForm").submit();
	    }
	    
function lockOrder(orderId){
   doOrderLockStatus(orderId, "lockOrder", "锁定成功");
}


function unLockOrder(orderId){
   doOrderLockStatus(orderId, "unLockOrder", "解锁成功");
}

function doOrderLockStatus(orderId, actionName, tipMsg){
    $.ajax({
        type: "POST",
        url: "${ctx}/visa/order/" + actionName,
        data: {
            "orderId":orderId
        },
        success: function(msg){
           if(msg){
	           if(msg.success){
	           		//bug 17570 ymx 订单锁死与解锁操作提示信息没有icon Strat
	                $("#btn_search").click();
				    top.$.jBox.tip(tipMsg,'warning');
				    //bug 17570 ymx 订单锁死与解锁操作提示信息没有icon End
	            }else{
	                top.$.jBox.tip(msg.error,'warning');
	            }
           }
            
        }
     });

}
var resetSearchParams = function(){
    $(':input','#searchForm')
		.not(':button, :submit, :reset, :hidden')
		.val('')
		.removeAttr('checked')
		.removeAttr('selected');
    $('#targetAreaId').val('');
    $('#orderShowType').val('${showType}');
    $('#commonCode').val('');
    $('#sysCountryId').val('');
    $('#visaType').val('');
    $('#agentinfoId').val('');
    $('#AACode').val('');
    $('#createBy').val('');
    $('#orderPayStatus').val('');
    $('#visaType').val('');
    $('#visaStatus').val('');
    $('#travelerName').val('');
    $('#salerId').val('');
};

//批量还收据  orderId:订单号
function batchHsj(orderId){

	//标志位 判断是否有选中
	var travellerIds ="";
	var visaIds ="";
	//游客界面
	if(orderId==null){
		$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}else{
		$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
				 var passPortStatus1 =  $("#hidden_passportStatus_"+trallerId).val();
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}
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
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'" trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td>'+rightList[i].jiekuanStatus+'</td><td>¥'+rightList[i].jiekuanJe+'</td><td><input type="text" onkeyup="clearNoNum(this)" id="returnReceiptWinJe_'+rightList[i].visaId+'" class="rmb inputTxt" value="'+rightList[i].jiekuanJe+'"/></td><td><input type="text" disabled="disabled" onclick="WdatePicker()" id="returnReceiptWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text" disabled="disabled" id="returnReceiptWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].returnReceiptPerson+'" /></td><td><input id="returnReceiptWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
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
					  var trallerId =   $(this).attr("trallerId");
					  var visaId =   $(this).attr("visaId");
					  var  name=   $(this).attr("trallerName");
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


//批量借款  orderId:订单号
function batchJk(orderId){

	//标志位 判断是否有选中
	var travellerIds ="";
	var visaIds ="";
	//游客界面
	if(orderId==null){
		$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}else{
		$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
				 var passPortStatus1 =  $("#hidden_passportStatus_"+trallerId).val();
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}
	if(travellerIds==""){
		top.$.jBox.tip("请至少选择一名游客",'warning');
		return;
	}
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/visa/order/checkBatchJk",                 
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
					 batchJk1(msg);
				 }
	        }   
		});
	
}


function batchJk1(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
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
		$.jBox(html, { title: "批量借款",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:680});
		
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">签证费用</th><th width="12%">借款日期</th><th width="10%">借款人</th><th width="10%">借款金额</th><th width="8%">借款状态</th><th width="10%">备注</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td>';
			html += '<td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'" disabled/></td>';
			html +='<td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" disabled /></td><td><input type="text" onkeyup="clearNoNum(this)" id="borrowWinMoney_'+rightList[i].visaId+'" value="" /></td><td>'+rightList[i].jiekuanStatus+'</td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
		}
		html += '</tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量借款",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var travellerIds="";
			var  visaIds="";
			var dates="";
			var persons="";
			var others ="";
			var moneys="";
			var t = true;
			 $("#borrowWin").find("input[type=checkbox]").each(function(){
				  if($(this).attr("checked")){
					  var trallerId =   $(this).attr("trallerId");
					  var visaId =   $(this).attr("visaId");
					  var  name=   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
					 if($("#borrowWinDate_"+visaId).val()==null||$("#borrowWinDate_"+visaId).val()==""){
						top.$.jBox.tip(name+"对应的借款日期不能为空",'warning');
						t=  false;
					}
					 dates+=$("#borrowWinDate_"+visaId).val()+",";
					 if($("#borrowWinPerson_"+visaId).val()==null||$("#borrowWinPerson_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款人不能为空",'warning');
							t=  false;
						}
					  persons+=$("#borrowWinPerson_"+visaId).val()+",";
					
					 if($("#borrowWinMoney_"+visaId).val()==null||$("#borrowWinMoney_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款金额不能为空",'warning');
							t=  false;
						}
					 moneys+=$("#borrowWinMoney_"+visaId).val()+",";
					 others+=$("#borrowWinOther_"+visaId).val()+" "+",";
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
					//url:g_context_url+ "/visa/order/batchJk",    
					url:g_context_url+ "/visa/workflow/borrowmoney/createBatchVisaJK",
					data:{ 
					    "travellerIds":travellerIds,
					    "visaIds":visaIds,
						"passportOperator":persons,
						"passportOperateTime":dates,
						"moneys":moneys,
						"passportOperateRemark":others},                
				    	"async": false,                 
					   success: function(msg){
						 if(msg.msg!=null&&msg.msg!=""){
							 top.$.jBox.tip(msg.msg,'warning');
							 return false;
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
							window.location.reload();
						 }
			        }   
				});
			return true;
		}
		},height:'auto',width:680});
		
	}
	
	
}


//批量借护照 orderId:订单号  ;passportStatus 操作类型：1：借 2：还
function batchUpdatePassport(orderId,passportStatus){

	//标志位 判断是否有选中
	var travellerIds ="";
	var visaIds ="";
	//游客界面
	if(orderId==null){
		$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}else{
		$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
				 var passPortStatus1 =  $("#hidden_passportStatus_"+trallerId).val();
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	}
	if(travellerIds==""){
		top.$.jBox.tip('请选择游客！');
		return;
	}
	if(passportStatus=="1"){
		batchBorrowPassport(orderId,visaIds,travellerIds);
	}else{
		backPassport(orderId,visaIds,travellerIds);
	}
	
	
}
	
	function batchBorrowPassport(orderId,visaIds , travellerIds){
		 $.ajax({                 
				cache: true,                 
				type: "POST",                 
				url:g_context_url+ "/visa/order/checkBatchBorrowPassport",                 
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
						 batchBorrowPassport1(msg);
					 }
		        }   
			});
	}
	
	
	function batchBorrowPassport1(msg){
		var rightList =msg.rightList;
		var errList =msg.errList;
		var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
		html += '<p>不满足条件用户：</p>';
		if(errList.length==0){
			html += ' (无)';
		}else{
			html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>护照状态</th><th>原因</th></tr></thead><tbody>';
			for(var i = 0,len = errList.length; i<len; i++){
				html += '<tr><td>'+errList[i].tname+'</td><td>'+errList[i].passportCode+'</td><td>'+errList[i].passportStatusDes+'</td><td>'+errList[i].errMsg+'</td></tr>';
			}
			html+='</tbody></table>';
		}
		html += '<p>满足条件用户：</p>';
		if(rightList.length==0){
			html += ' (无)';
			html += '</div>';
			$.jBox(html, { title: "批量借护照",buttons:{'取消':0}, submit:function(v, h, f){
			},height:'auto',width:580});
			
		}else{
			html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">护照状态</th><th width="10%">借出日期</th><th width="10%">护照领取人</th><th width="10%">备注</th></tr></thead><tbody>';
			//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
			for(var i = 0,len = rightList.length; i<len; i++){
				html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].passportStatusDes+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" /></td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
			}
			html += '</tbody></table>';
			html += '</div>';
			$.jBox(html, { title: "批量借护照",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
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
							top.$.jBox.tip(name+"对应的借出日期不能为空",'warning');
							t=  false;
						}
						 dates+=$("#borrowWinDate_"+visaId).val()+",";
						 if($("#borrowWinPerson_"+visaId).val()==null||$("#borrowWinPerson_"+visaId).val()==""){
								top.$.jBox.tip(name+"对应的护照领取人不能为空",'warning');
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
						url:g_context_url+ "/visa/order/batchUpdatePassport",                 
						data:{ 
						    "travellerIds":travellerIds,
						    "visaIds":visaIds,
							passportStatus:"1",
							passportOperator:persons,
							passportOperateTime:dates,
							passportOperateRemark:others},                
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
										 $("#passportStatus_"+travelerId+" option").eq(1).attr("selected",true);
									 }
								}
							 }
				        }   
					});
				
				
				
				return true;
			}
			},height:'auto',width:580});
			
		}
		
		
	}
	
	function backPassport(orderId,visaIds , travellerIds){
		 $.ajax({                 
				cache: true,                 
				type: "POST",                 
				url:g_context_url+ "/visa/order/checkBatchReturnPassport",                 
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
						 batchReturnPassport(msg);
					 }
		        }   
			});
	}
	
	function batchReturnPassport(msg){
		var rightList =msg.rightList;
		var errList =msg.errList;
		var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
//		html += '<p>不满足条件用户：</p>';
//		if(errList.length==0){
//			html += ' (无)';
//		}else{
//			html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>护照状态</th><th>原因</th></tr></thead><tbody>';
//			for(var i = 0,len = errList.length; i<len; i++){
//				html += '<tr><td>'+errList[i].tname+'</td><td>'+errList[i].passportCode+'</td><td>'+errList[i].passportStatusDes+'</td><td>'+errList[i].errMsg+'</td></tr>';
//			}
//			html+='</tbody></table>';
//		}
//		html += '<p>满足条件用户：</p>';
		if(rightList.length==0){
			html += ' (无)';
			html += '</div>';
			$.jBox(html, { title: "批量还护照",buttons:{'取消':0}, submit:function(v, h, f){
			},height:'auto',width:580});
			
		}else{
			html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">护照状态</th><th width="10%">归还日期</th><th width="10%">护照归还人</th><th width="10%">备注</th></tr></thead><tbody>';
			//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
			for(var i = 0,len = rightList.length; i<len; i++){
				html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].passportStatusDes+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" /></td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
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
						url:g_context_url+ "/visa/order/batchUpdatePassport",                 
						data:{ 
						    "travellerIds":travellerIds,
						    "visaIds":visaIds,
							passportStatus:"4",
							passportOperator:persons,
							passportOperateTime:dates,
							passportOperateRemark:others},                
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
										$("#hidden_passportStatus_"+travelerId).val(4);
										 $("#passportStatus_"+travelerId+" option").eq(3).attr("selected",true);
									 }
								}
							 }
				        }   
					});
				
				
				
				return true;
			}
			},height:'auto',width:580});
			
		}
		
		
	}	
		
		/*
		var html = '<div style="margin-top:20px; padding-left:20px;">';
		html += '<label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照归还领取人：</label><input id="passportOperator" type="text" /><br /><label class="jbox-label" style="width:120px;"><span class="xing">*</span>护照归还时间：</label><input id="passportOperateTime" class="inputTxt dateinput" onfocus="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" type="text"  style="width:206px;" /><br /><label class="jbox-label" style="width:120px;">护照归还备注：</label><input id="passportOperateRemark" type="text" />';
		//html += '<label class="jbox-label" style="width:100px;">护照归还领取人：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照归还时间：</label><input name="" type="text" /><br /><label class="jbox-label" style="width:100px;">护照归还备注：</label><input name="" type="text" />';
		html += '</div>';
		$.jBox(html, { title: "还护照",buttons:{'确认归还': 1}, submit:function(v, h, f){
			var passportOperator = $("#passportOperator").val();
			var passportOperateTime = $("#passportOperateTime").val();
			if(passportOperator==""){
				 $.jBox.info("护照归还领取人为空，请重新填写！", "信息");
				 return false;
			 }
			 if(passportOperateTime==""){
				 $.jBox.info("护照归还时间为空，请重新选择！", "信息");
				 return false;
			 }
			if (v=="1"){
				var passportOperator = $("#passportOperator").val();
				 var passportOperateTime = $("#passportOperateTime").val();
				 var passportOperateRemark = $("#passportOperateRemark").val();
				 
				 
				 $("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
					  if($(this).attr("checked")){
						  var trallerId =   $(this).attr("trallerId");
						  var visaId =   $(this).attr("visaId");
						  var trallerName =   $(this).attr("trallerName");
						 var passPortStatus1 =  $("#hidden_passportStatus_"+trallerId).val();
							 travellerIds+=trallerId+",";
							 visaIds+=visaId+",";
					  }
				});
				 $.ajax({                 
						cache: true,                 
						type: "POST",                 
						url:g_context_url+ "/visa/order/batchUpdatePassport",                 
						data:{ 
							"orderId":orderId,
						    "travellerIds":travellerIds,
						    "visaIds":visaIds,
							passportStatus:"4",
							passportOperator:passportOperator,
							passportOperateTime:passportOperateTime,
							passportOperateRemark:passportOperateRemark,
							passportOperateRemark:passportOperateRemark},                
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
										$("#hidden_passportStatus_"+travelerId).val(4);
										 $("#passportStatus_"+travelerId+" option").eq(3).attr("selected",true);
									 }
								}
							 }
				        }   
					});
			}
		},height:220,width:380});

		
		*/

	//批量更新签证状态
	function batchUpdateVisaStatus(orderId){
		//标志位 判断是否有选中
		var travellerIds ="";
		var visaIds ="";
		//游客界面
		if(orderId==null){
			$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var trallerId = $(this).attr("trallerId");
					var visaId = $(this).attr("visaId");
						travellerIds+=trallerId+",";
						visaIds+=visaId+",";
				}
			});
		}else{
			$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var trallerId = $(this).attr("trallerId");
					var visaId = $(this).attr("visaId");
						travellerIds+=trallerId+",";
						visaIds+=visaId+",";
				}
			});
		}
		if(travellerIds==""){
			top.$.jBox.tip('请选择游客！');
			return;
		} else {
			visaIds = visaIds.substring(0,visaIds.length-1);
		}
		batchUpdateVisaStatus1(orderId,visaIds);
	}

	function batchUpdateVisaStatus1(orderId,visaIds){
		var html = '<div style="margin-top:20px;padding:0 10px" id="batchUpdateVisaStatus">';
		html += '<label class="jbox-label">选择签证状态：</label>';
		html += '<select id="visaStatus" name="visaStatus">';
		html += '<option value="-1">请选择</option>';
		html += '<c:forEach items="${fns:getDictList(\'visa_status\')}" var="visaStatus">';
		html += '<c:if test="${visaStatus.value == 2}">';
		html += '<option value="2">已约签</option>';
		html += '</c:if>';
		html += '<c:if test="${visaStatus.value == 3}">';
		html += '<option value="3">通过</option>';
		html += '</c:if>';
		html += '<c:if test="${visaStatus.value != 3 and visaStatus.value != 2}">';
		html += '<option value="${visaStatus.value}">${visaStatus.label}</option>';
		html += '</c:if>';
		html += '</c:forEach>';
		html += '<select>';
		html += '</div>';

		$.jBox(html, { title: "批量操作签证状态",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var visaStatus=$("#batchUpdateVisaStatus").find("select[id=visaStatus]").val();
			$.ajax({
				cache: true,
				type: "POST",
				url:g_context_url+ "/visa/order/batchUpdateVisaStatus",
				data:{ 
					"visaStatus":visaStatus,
					"visaIds":visaIds},
					async: false,
					success: function(msg){
					if(msg.msg!=null&&msg.msg!=""){
						top.$.jBox.tip(msg.msg,'warning');
					}else{
						top.$.jBox.tip("操作成功",'warning');
						if(orderId==null){
							showTraveler();
						} else {
							showOrder();;
						}
					}
				}
			});
			return true;
		}
		},height:300,width:420});
	}

	/**
	*渠道选择的时候,选择一次以后就获取不到空的值,于是就用这个方法控制
	*/
	function cleanAgent(){
			  var sel=document.getElementById('agentinfoId'); 
			  var str = sel.options[sel.selectedIndex].text;
			  if(null == str || 0==str.length){
			    sel.options[sel.selectedIndex].value ="";
			}
}

//订单-销售身份订单-还押金收据----wxw added----
function jbox_hyjsj(travelerID) {
	//alert("还押金收据！");
	//alert(travelerID);
	//收据金额: depositReceiptAmount
	//还收据人: depositReceiptor
	//归还时间: depositReceiptReturnTime
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<label class="jbox-label">收据金额：</label><input name="depositReceiptAmount"  type="text" /><br /><label class="jbox-label">还收据人：</label><input name="depositReceiptor" type="text" /><br /><label class="jbox-label">归还时间：</label><input name="depositReceiptReturnTime" onclick="WdatePicker()" class="inputTxt dateinput"  type="text" />';
	html += '</div>';
	$
			.jBox(
					html,
					{
						title : "还押金收据",
						buttons : {
							'还收据' : 1
						},
						submit : function(v, h, f) {

							if (!f.depositReceiptAmount) {
								top.$.jBox.tip("收据金额为必填项！");
								return false;
							}

							var text1 = (f.depositReceiptor).replace(
									/^\s+/, "").replace(/\s+$/, "");
							if (!f.depositReceiptor || text1.length < 1) {
								top.$.jBox.tip("还收据人为必填项！");
								return false;
							}

							if (!f.depositReceiptReturnTime) {
								top.$.jBox.tip("归还时间为必填项！");
								return false;
							}

							if (isNumber(f.depositReceiptAmount)) {
								$
										.ajax({
											type : "POST",
											url : "${ctx}/visa/workflow/returndepositreceipt/createVisaHYJSJ",
											dataType : "json",
											data : {
												"travelerID" : travelerID,
												"depositReceiptAmount" : f.depositReceiptAmount,
												"depositReceiptor" : f.depositReceiptor,
												"depositReceiptReturnTime" : f.depositReceiptReturnTime
											},
											async : false,
											success : function(msg) {
												top.$.jBox
														.tip(msg.visaHYJSHreply);
											}
										});
							} else {
								top.$.jBox.tip("收据金额必须为数字或小数！");
								return false;
							}
						},
						height : 220,
						width : 380
					});
}

/**
 * 必须为数字或小数点的校验
 * 必填校验
 */
function isNumber(oNum) {
	if (!oNum)
		return false;
	var strP = /^\d+(\.\d+)?$/;
	if (!strP.test(oNum))
		return false;
	try {
		if (parseFloat(oNum) != oNum)
			return false;
	} catch (ex) {
		return false;
	}
	return true;
}

//点击批量退押金
function batchtuiyajin(aId,checkName){
	var str=document.getElementsByName(checkName);
	var objarray=str.length;
	//游客id列表
	var tidArray="";
	//订单id
	var visaOrderId ="";
	

	for ( var i = 0; i < objarray; i++) {
			if (str[i].checked == true) {
				var vid = str[i].value.split("@")[1];
				//如果选中的游客不属于一个订单,清空订单id
				if (visaOrderId != "") {
					if (visaOrderId != vid) {
						visaOrderId = "";
						break;
					}
				} else {
					visaOrderId = vid;
				}
			}
		}

		for (var i = 0; i < objarray; i++) {
			if (str[i].checked == true) {
				//选中游客的id
				var tid = str[i].value.split("@")[0];
				tidArray += tid + ",";
			}
		}

		if ("" == tidArray || tidArray.length == 0) {
			top.$.jBox.tip('至少选择一位游客');
			return false;
		} else {
			batchYaJinHsj(visaOrderId,tidArray);
		}
	}


//批量还收据  orderId:订单号
function batchYaJinHsj(orderId,tidArray){

	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url: "${ctx}" + "/order/manager/visaDeposit/checkDepositRefund",                 
			data:{ 
			    "travelerIds":tidArray
				},                
			async: false,                 
			 success: function(msg){
				 if(msg.msg!=null&&msg.msg!=""){
					 top.$.jBox.tip(msg.msg,'warning');
				 }else{
					 batchYaJinHsj_msg(msg);
				 }
	        }   
		});
	
}


function batchYaJinHsj_msg(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
	html += '<p>不满足条件用户：</p>';
	if(errList.length==0){
		html += ' (无)';
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>订单编号</th><th>币种</th><th>押金金额</th><th>押金状态</th><th>不满足原因</th></tr></thead><tbody>';
		for(var i = 0,len = errList.length; i<len; i++){
			html += '<tr><td>'+errList[i].name+'</td><td>'+errList[i].code+'</td><td>'+errList[i].totalCurrency+'</td><td>'+errList[i].total+'</td><td>'+errList[i].moneyStatus+'</td><td>'+errList[i].message+'</td></tr>';
		}
		html+='</tbody></table>';
	}
	html += '<p>满足条件用户：</p>';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量退押金",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:780});
		
	}else{
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="15%">游客</th><th  width="20%">订单编号</th><th width="10%">币种</th><th width="10%">押金金额</th><th width="10%">达账押金</th><th width="10%">退款时间</th><th width="10%">申请金额</th><th width="15%">备注</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input name="checkedTraveler" travelerId ="'+rightList[i].id+'" travelerName="'+rightList[i].name+'" type="checkbox" checked="checked"/>'+rightList[i].name+'</td><td>'+rightList[i].code+'</td><td id="' + rightList[i].totalCurrencyId + '">'+rightList[i].totalCurrency+'</td><td>'+rightList[i].total+'</td><td>'+rightList[i].accountedMoney+'</td><td><input type="text" onclick="WdatePicker()" id="applyDate'+rightList[i].id+'"/></td><td><input type="text"  id="applyMoney'+rightList[i].id+'" value="' + rightList[i].totalPrice + '"/></td><td><input id="applyMark'+rightList[i].id+'" type="text" /></td></tr>';
		}
		html += '<input name="visaId" value="' + rightList[0].visaId + '" style="display: none;"/></tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量退押金",buttons:{'确定': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
			var travelerIds = [];
			var travelerNames = [];
			var priceCurrency = [];
			var price = [];
			var totalPrice = [];
			var applyDates = [];
			var applyPrice = [];
			var applyMark = [];
			var t = true;
			 $("[name='checkedTraveler']:checked").each(function(){
				  if($(this).attr("checked")){
					 var travelerId = $(this).attr("travelerId");
					 var name = $(this).attr("travelerName");
					 travelerIds.push(travelerId);
					 if($("#applyDate"+travelerId).val()==null||$("#applyDate"+travelerId).val()==""){
						top.$.jBox.tip("游客"+name+"对应的退款日期不能为空",'warning');
						t=  false;
					 }
					 applyDates.push($("#applyDate"+travelerId).val());
					 if($("#applyMoney"+travelerId).val()==null||$("#applyMoney"+travelerId).val()==""){
							top.$.jBox.tip("游客"+name+"对应的申请金额不能为空",'warning');
							t=  false;
						}
					 travelerNames.push($(this).attr("travelername"));
					 priceCurrency.push($(this).parent().parent().find("td").eq(2).attr("id"));
					 price.push($(this).parent().parent().find("td").eq(3).html());
					 totalPrice.push($(this).parent().parent().find("td").eq(4).html());
					 applyPrice.push($("#applyMoney"+travelerId).val());
					 applyMark.push($("#applyMark"+travelerId).val());
				  }
			});
			 
			 if(0 == travelerIds.length){
				 top.$.jBox.tip("请勾选对应的游客",'warning');
				 return false;
			 }
			 
			 if(!t){
				 return false;
			 }
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					url: "${ctx}" + "/order/manager/visaDeposit/refundApply",                 
					data:{ 
						proId : $("[name='visaId']").val(),
						travelerIds : travelerIds.join(),
						travelerName : travelerNames.join(),
						priceCurrency : priceCurrency.join(),
						price : price.join(),
						totalPrice : totalPrice.join(),
					    applyDates : applyDates.join(),
					    applyPrice : applyPrice.join(),
					    reasonMark : applyMark.join(),
					    ajax : 'true'
						},                
				    	async: false,                 
					    success: function(msg){
						if(msg!=null&&msg!=""){
							top.$.jBox.tip(msg,'warning');
						}else{
							top.$.jBox.tip("操作成功",'warning');
						}
			        }   
				});
			return true;
			}
		},height:'auto',width:780});
	}
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

function changeCount(obj,orderCode)
{
	if($("#childMenu_575").find("span").text() !=0 && $(obj).find(".new-tips").css("display") =="inline-block")
		{
			if(($("#childMenu_575").find("span").text() -1)==0)
				$("#childMenu_575").find("em").remove();
			else
			$("#childMenu_575").find("span").text($("#childMenu_575").find("span").text() -1);
			$(obj).find(".new-tips").hide();
			
			$.ajax({
		        type: "POST",
		        url: "${ctx}/visa/preorder/updateCount",
		        data: {
		        	'orderCode':orderCode
		        }
	     	}); 
		}
}

</script>
</head>
<% 
 String mainOrderId = request.getParameter("mainOrderId");
%>
<body>
<!-- 顶部菜单列表 -->
<page:applyDecorator name="visaQW_order_head">
	<page:param name="showType">${showType}</page:param>
</page:applyDecorator>
<input type="hidden" id="ctx" value="${ctx}">
<input id="tm" type="hidden" value="">
				<!--右侧内容部分开始-->

				<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
					<form method="post" action="${ctx}/visa/order/searchqw" id="searchForm" name="searchForm" onsubmit="cleanAgent()">      
						<input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
						<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
						<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
						<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />
						<input id="mainOrderId" name="mainOrderIdYouKe" type="hidden" value="<%=mainOrderId %>" />
						<input id="youkePageNo" name="youkePageNo" type="hidden" value="${travelPage.pageNo}"/>
						<input id="youkePageSize" name="youkePageSize" type="hidden" value="${travelPage.pageSize}"/>
						<input id="youkeOrderBy" name="youkeOrderBy" type="hidden" value="${travelPage.orderBy}"/>
					
						<input value="" type="hidden" id="showList" name="showList"/>     
						<input value="" type="hidden" id="showFlag" name="showFlag"/>
						<input type="hidden" id="showType" name="showType" value="${showType }" />
						<shiro:hasPermission name="visaOrderQW:list:cancelorder">
							<c:set var="cancelOrderPermission" value="1"></c:set>
						</shiro:hasPermission>
						<input type="hidden" id="cancelOrderPermission" name="cancelOrderPermission" value="${cancelOrderPermission }" />
					
						<div class="activitylist_bodyer_right_team_co">
							<div class="activitylist_bodyer_right_team_co2 pr">
								<%--<div class="activitylist_team_co3_text">搜索：</div>--%>
								<input value="${visaOrderForm.commonCode}" class="inputTxt inputTxtlong searchInput" placeholder="订单号、产品编号" name="commonCode" id="commonCode">
									<%--<span class="ipt-tips">订单号、产品编号</span>--%>
							</div>
							<a class="zksx">筛选</a>
							<div class="form_submit">
								 <input type="submit" id="btn_search" value="搜索" onclick="searchOrderList();" class="btn btn-primary ydbz_x">
								 <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
							</div>
                            <c:if test="${not ttsFlag }">
                                <p class="main-right-topbutt"><a class="primary" href="${ctx }/visa/order/visaBatchEditListNew" target="_blank" id="piliangcaozuo">批量操作</a></p>
                            </c:if>
							<div class="ydxbd" style="display: none;">
								<span></span>
								<c:if test="${showList eq 'dingdan' || showList eq null }">
									<div class="activitylist_bodyer_right_team_co1">
										<div class="activitylist_team_co3_text">销售：</div>
										<select name="salerId" id="salerId" >
											<option value="">不限</option>
											
											<c:forEach items="${fns:getSaleUserList('1')}" var="userinfo">
												<!-- 用户类型  1 代表销售 -->
												<option value="${userinfo.id }" <c:if test="${userinfo.id eq visaOrderForm.salerId}">selected="selected"</c:if>>${userinfo.name }</option>
											</c:forEach> 
											
										</select>
									</div>
									<%-- <div class="activitylist_bodyer_right_team_co1">
										<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
										<select name="paymentType" id="paymentType" >
											<option value="">不限</option>
											
											<c:forEach items="${fns:findAllPaymentType()}" var="pType">
												<!-- 用户类型  1 代表销售 -->
												<option value="${pType[0]}" <c:if test="${pType[0] eq visaOrderForm.paymentType}">selected="selected"</c:if>>${pType[1] }</option>
											</c:forEach> 
											
										</select>
									</div> --%>
								
									<!-- 0067新增说明会时间 -->
									<!-- <div class="activitylist_bodyer_right_team_co1"></div> -->
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text">说明会时间：</label>
										<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingStart}" name="explanationMeetingStart" class="inputTxt dateinput" id="explanationMeetingStart"> 
										<span> 至 </span>  
										<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingEnd}" name="explanationMeetingEnd" class="inputTxt dateinput" id="explanationMeetingEnd">
									</div>
								</c:if>
<%-- 								<c:if test="${!(showList eq 'dingdan' || showList eq null) }"> --%>
<!-- 									<div class="activitylist_bodyer_right_team_co1"></div> -->
<%-- 								</c:if> --%>
								
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">签证国家：</div>
									<select id="sysCountryId" name="sysCountryId">
										<option value="">不限</option>
										<c:forEach items="${countryList}" var="country">
											<c:choose>
												<c:when test="${ country.id eq visaOrderForm.sysCountryId}">
													<option value="${country.id}" selected="selected">${country.countryName_cn}</option>
												</c:when>
												<c:otherwise>
													<option value="${country.id}">${country.countryName_cn}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">签证类型：</div>
									<div class="selectStyle">
										<select id="visaType" name="visaType">
											<option value="">不限</option>
											<c:forEach items="${visaTypeList}" var="visaType">
												<c:choose>
													<c:when test="${ visaType.value eq visaOrderForm.visaType}">
														<option value="${visaType.value}" selected="selected">${visaType.label}</option>
													</c:when>
													<c:otherwise>
														<option value="${visaType.value}">${visaType.label}</option>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">预计出团日期：</label>
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.forecastStartOutStart}" name="forecastStartOutStart" class="inputTxt dateinput" id="forecastStartOutStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.forecastStartOutEnd }" name="forecastStartOutEnd" class="inputTxt dateinput" id="forecastStartOutEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co3">
									<label class="activitylist_team_co3_text">渠道选择：</label>
									<select id="agentinfoId" name="agentinfoId" >
										<option value="">全部</option>
										<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
										<c:choose>
											<c:when test="${ visaOrderForm.agentinfoId eq '-1' }">
												<c:choose><c:when test="${companyUUid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1" selected="selected">未签</option></c:when><c:otherwise>
												<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1" selected="selected">直客</option>
												</c:if>
												<c:if test="${fns:getUser().company.uuid ne  '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1" selected="selected">非签约渠道</option>
												</c:if>
												</c:otherwise></c:choose>
											</c:when>
											<c:otherwise>
												<c:choose><c:when test="${companyUUid eq '7a81a03577a811e5bc1e000c29cf2586' }"><option value="-1">未签</option></c:when><c:otherwise>
													<c:if test="${fns:getUser().company.uuid eq '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1">直客</option>
												</c:if>
												<c:if test="${fns:getUser().company.uuid ne  '7a81b21a77a811e5bc1e000c29cf2586' }">
													<option value="-1">非签约渠道</option>
												</c:if>
												</c:otherwise></c:choose>
											</c:otherwise>
										</c:choose>
										
										<c:forEach items="${agentinfoList}" var="agentinfo">
											<c:choose>
												<c:when test="${ agentinfo.id eq visaOrderForm.agentinfoId}">
													<option value="${agentinfo.id}" selected="selected">${agentinfo.agentName}</option>
												</c:when>
												<c:otherwise>
													<option value="${agentinfo.id}">${agentinfo.agentName}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										
									</select> 
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">签证状态：</label>
									<div class="selectStyle">
										<select id="visaStatus" name="visaStatus">
											<option value="">不限</option>
											<c:forEach items="${visaStatusList}" var="visaStatus">
												
													<c:choose>
														<c:when test="${visaStatus.value == 3}">
															<c:choose>
																<c:when test="${visaStatus.value eq visaOrderForm.visaStatus}">
																	<option selected="selected" value="3">通过</option>
																</c:when>
																<c:otherwise>
																	<option value="3">通过</option>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:when test="${visaStatus.value == 2}">
															<c:choose>
																<c:when test="${visaStatus.value eq visaOrderForm.visaStatus}">
																	<option selected="selected" value="2">已约签</option>
																</c:when>
																<c:otherwise>
																	<option value="2">已约签</option>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															<c:choose>
																<c:when test="${visaStatus.value eq visaOrderForm.visaStatus}">
																	<option selected="selected" value="${visaStatus.value}">${visaStatus.label}</option>
																</c:when>
																<c:otherwise>
																	<option value="${visaStatus.value}">${visaStatus.label}</option>
																</c:otherwise>
															</c:choose>
														</c:otherwise>
													</c:choose>
												
										</c:forEach>
										</select>
									</div>
								</div> 
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">预计约签日期：</label>
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.forecastContractStart}" name="forecastContractStart" class="inputTxt dateinput" id="forecastContractStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.forecastContractEnd}" name="forecastContractEnd" class="inputTxt dateinput" id="forecastContractEnd">
								</div>

								<div class="activitylist_bodyer_right_team_co1">
								<!-- 
									<div class="activitylist_team_co3_text">AA码：</div>
									<input name="AACode" id="AACode" type="text"class="inputTxt" value="${visaOrderForm.AACode}"/>
								 -->
									<label class="activitylist_team_co3_text">下单人：</label>
									<select id="createBy" name="createBy" >
										<option value="">请选择</option>
										
										<c:forEach items="${createByList}" var="createBy">
											<c:choose>
												<c:when test="${createBy eq visaOrderForm.createBy}">
													<option value="${createBy}" selected="selected">${createBy}</option>
												</c:when>
												<c:otherwise>
													<option value="${createBy}">${createBy}</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
										
									</select> 
								</div>
								
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">订单状态：</label>
									<div class="selectStyle">
										<select id="orderPayStatus" name="orderPayStatus">
											<option value="">全部订单状态</option>
											<c:choose>
												<c:when test="${'1' eq visaOrderForm.orderPayStatus}">
													<option value="1" selected="selected">未收款</option>
												</c:when>
												<c:otherwise>
													<option value="1" >未收款</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '5' eq visaOrderForm.orderPayStatus}">
													<option value="5" selected="selected">已收款</option>
												</c:when>
												<c:otherwise>
													<option value="5">已收款</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '3' eq visaOrderForm.orderPayStatus}">
													<option value="3" selected="selected">预定</option>
												</c:when>
												<c:otherwise>
													<option value="3">预定</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '99' eq visaOrderForm.orderPayStatus}">
													<option value="99" selected="selected">已取消</option>
												</c:when>
												<c:otherwise>
													<option value="99">已取消</option>
												</c:otherwise>
											</c:choose>
										</select>
									</div>
								</div>
								<!-- 
								<div class="activitylist_bodyer_right_team_co1">
									
								</div>
								-->
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">实际出团日期：</label>
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.startOutStart}"name="startOutStart" class="inputTxt dateinput" id="startOutStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.startOutEnd}" name="startOutEnd" class="inputTxt dateinput" id="startOutEnd">
								</div>

								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">借款状态：</label>
									<div class="selectStyle">
										<select id="jiekuanStatus" name="jiekuanStatus">
											<option value="">不限</option>
											<c:choose>
												<c:when test="${'1' eq visaOrderForm.jiekuanStatus}">
													<option value="1" selected="selected">审批中</option>
												</c:when>
												<c:otherwise>
													<option value="1" >审批中</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '2' eq visaOrderForm.jiekuanStatus}">
													<option value="2" selected="selected">已借</option>
												</c:when>
												<c:otherwise>
													<option value="2">已借</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '3' eq visaOrderForm.jiekuanStatus}">
													<option value="3" selected="selected">未借</option>
												</c:when>
												<c:otherwise>
													<option value="3">未借</option>
												</c:otherwise>
											</c:choose>
										</select>
									</div>
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">参团类型：</label>
									<div class="selectStyle">
										<select id="orderType" name="orderType">
											<option value="">不限</option>
											<c:choose>
												<c:when test="${'1' eq visaOrderForm.orderType}">
													<option value="1" selected="selected">单团</option>
												</c:when>
												<c:otherwise>
													<option value="1" >单团</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '2' eq visaOrderForm.orderType}">
													<option value="2" selected="selected">散拼</option>
												</c:when>
												<c:otherwise>
													<option value="2">散拼</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '3' eq visaOrderForm.orderType}">
													<option value="3" selected="selected">游学</option>
												</c:when>
												<c:otherwise>
													<option value="3">游学</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '4' eq visaOrderForm.orderType}">
													<option value="4" selected="selected">大客户</option>
												</c:when>
												<c:otherwise>
													<option value="4" >大客户</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '5' eq visaOrderForm.orderType}">
													<option value="5" selected="selected">自由行</option>
												</c:when>
												<c:otherwise>
													<option value="5">自由行</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '6' eq visaOrderForm.orderType}">
													<option value="6" selected="selected">单办签</option>
												</c:when>
												<c:otherwise>
													<option value="6">单办签</option>
												</c:otherwise>
											</c:choose>
										</select>
									</div>
								</div>
								<!-- 
								<div class="activitylist_bodyer_right_team_co1">
									
									<div class="activitylist_team_co3_text">借款状态：</div>
									<select id="jiekuanStatus" name="jiekuanStatus">
										<option value="">不限</option>
										<option value="">未借款</option>
										<option value="">已借款</option>
										<option value="">已还收据</option>
									</select> 
									 
								</div>
								-->
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">实际约签日期：</label>
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.contractStart}" name="contractStart" class="inputTxt dateinput" id="contractStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.contractEnd}" name="contractEnd" class="inputTxt dateinput" id="contractEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">游客姓名：</label>
									<input value="${visaOrderForm.travelName }" class="inputTxt inputTxtlong" name="travelName" id="travelName">
								</div>
								
								<!-- 新增护照状态 -->
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text">护照状态：</label>
										<div class="selectStyle">
											<select id="passportStatus" name="passportStatus">
											<option value="">请选择</option>
											<c:choose>
												<c:when test="${'1' eq visaOrderForm.passportStatus}">
													<option value="1" selected="selected">借出</option>
												</c:when>
												<c:otherwise>
													<option value="1" >借出</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '2' eq visaOrderForm.passportStatus}">
													<option value="2" selected="selected">销售已领取</option>
												</c:when>
												<c:otherwise>
													<option value="2">销售已领取</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '4' eq visaOrderForm.passportStatus}">
													<option value="4" selected="selected">已还</option>
												</c:when>
												<c:otherwise>
													<option value="4" >已还</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '5' eq visaOrderForm.passportStatus}">
													<option value="5" selected="selected">已取出</option>
												</c:when>
												<c:otherwise>
													<option value="5">已取出</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '6' eq visaOrderForm.passportStatus}">
													<option value="6" selected="selected">未取出</option>
												</c:when>
												<c:otherwise>
													<option value="6">未取出</option>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${ '8' eq visaOrderForm.passportStatus}">
													<option value="8" selected="selected">计调领取</option>
												</c:when>
												<c:otherwise>
													<option value="8">计调领取</option>
												</c:otherwise>
											</c:choose>
											</select>
										</div>
									</div>
								
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">借款申请日期：</label>
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.jiekuanTimeStart}" name="jiekuanTimeStart" class="inputTxt dateinput" id="jiekuanTimeStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.jiekuanTimeEnd}" name="jiekuanTimeEnd" class="inputTxt dateinput" id="jiekuanTimeEnd">
								</div>
								<c:if test="${showList eq 'youke' }">
									<%-- <div class="activitylist_bodyer_right_team_co1">
											<div class="activitylist_team_co3_text" style="width: 85px;">渠道结算方式：</div>
											<select name="paymentType" id="paymentType" >
												<option value="">不限</option>
												
												<c:forEach items="${fns:findAllPaymentType()}" var="pType">
													<!-- 用户类型  1 代表销售 -->
													<option value="${pType[0]}" <c:if test="${pType[0] eq visaOrderForm.paymentType}">selected="selected"</c:if>>${pType[1] }</option>
												</c:forEach> 
												
											</select>
										</div> --%>
								 </c:if>
								<c:if test="${!(showList eq 'dingdan' || showList eq null) }"> 
									<!-- <div class="kong"></div>
									<div class="activitylist_bodyer_right_team_co1"></div>占位用 -->
									<!-- 0067新增说明会时间 -->
									<div class="activitylist_bodyer_right_team_co1">
										<label class="activitylist_team_co3_text">说明会时间：</label>
										<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingStart}" name="explanationMeetingStart" class="inputTxt dateinput" id="explanationMeetingStart"> 
										<span> 至 </span>  
										<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingEnd}" name="explanationMeetingEnd" class="inputTxt dateinput" id="explanationMeetingEnd">
									</div>
								</c:if>
							
							</div>
						</div>
					</form>
					<c:set var="ttsFlag" value="true"></c:set>
					<c:if test="${fns:getUser().company.id != '71' }">
<!-- 						如果是环球行，则不显示批量操作按钮 -->
						<c:set var="ttsFlag" value="false"></c:set>
					</c:if>
					
					<!-- 待oppen -->

					
					<div class="supplierLine">
						<a onclick="showOrder();" href="javascript:void(0)" id="dingdanliebiao">订单列表</a>
						<a onclick="showTraveler();" href="javascript:void(0)" id="youkeliebiao">游客列表</a>
						<!-- 改功能已经废弃，由新的批量操作代替 wxw added 2015-09-18 -->
						<!-- 
						<c:if test="${fns:getUser().company.id != '71' && ttsFlag }">
							<a href="${ctx }/visa/order/batchBorrowMoneyList"  target="_blank" id="piciliebiao">批次列表</a>
						</c:if>
					    -->
						<c:if test="${not ttsFlag }">
							<a href="${ctx }/visa/order/visaBatchEditRecordList" target="_blank" id="piliangcaozuojilu">批量操作记录</a>
						</c:if>
					</div>
					<div id="dingdan">
					<div class="activitylist_bodyer_right_team_co_paixu">
						<div class="activitylist_paixu">
							<div class="activitylist_paixu_left">
								<ul>

	                    <c:choose>
	                    	<c:when test="${page.orderBy == '' || page.orderBy == null }">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.create_date DESC'}">
	                    		<li class="activitylist_paixu_moren">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
		                    			<i class="icon icon-arrow-down"></i>
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.create_date ASC'}">
	                    		<li class="activitylist_paixu_moren">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
		                    			<i class="icon icon-arrow-up"></i>
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_left_biankuang lipro.updateDate">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.update_Date DESC'}">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_moren">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
		                    			<i class="icon icon-arrow-down"></i>
										
									</a>
								</li>
	                    	</c:when>
	                    	
	                    	<c:when test="${page.orderBy == 'vo.update_Date ASC'}">
	                    		<li class="activitylist_paixu_left_biankuang lipro.updateDate">
	                    			<a onclick="sortby('vo.create_date',this)">
		                    			创建时间
	                    			</a>
	                    		</li>
								<li class="activitylist_paixu_moren">
									<a onclick="sortby('vo.update_Date',this)">
										更新时间
		                    			<i class="icon icon-arrow-up"></i>
									</a>
								</li>
	                    	</c:when>
	                    </c:choose>
								</ul>
								
								${message}
							</div>
							<div class="activitylist_paixu_right">
							<c:if test="${page.count >0}">
							<c:if test="${fns:getUser().company.uuid eq '58a27feeab3944378b266aff05b627d2' }">
								<button onclick="exportExcelUserList();" class="btn btn-primary" style="width:auto;">导出游客</button></c:if>
								查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
							</c:if>
							<c:if test="${page.count eq null }">
								查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
							</c:if>
							</div>
							<div class="kong"></div>
						</div>
					</div>
					<table class="table mainTable activitylist_bodyer_table" id="contentTable">
						<thead>
						<tr>
							<th width="4%">序号</th>
							<th width="8%">预定渠道 </th>
							<!-- 200 针对优加，签务签证订单 订单列表加入"渠道联系人" -->
							<c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
								<th width="8%">渠道联系人</th>
							</c:if>
							<c:choose>
								<c:when test="${dayangCompanyUuid==companyUUid}">
									<!-- 隐藏此列 -->
								</c:when>
								<c:otherwise>
									<th width="12%">订单号</th>
								</c:otherwise>
							</c:choose>
							<c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}">
								<!-- <th width="12%">订单团号</th> -->
								<!-- 对应需求号C460v3   -->
								<th width="12%">团号</th>
							</c:if>
							<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">
							                                             
								<th width="12%">订单团号</th>
							</c:if>
							<!--  <th width="6%">领区联系人</th>-->
							<th width="12%">产品编号</br>产品名称</th>
							<th width="6%">订单状态</th>
							<th width="6%"><span class="order-saler-title on">销售</span>/<span class="order-picker-title">下单人</span></th>
							<th width="8%">下单时间</th>
							<th width="5%">人数</th>
							<th width="7%">
								<c:if test="${fns:getUser().company.uuid ne '7a45838277a811e5bc1e000c29cf2586'}">
									应收金额
								</c:if>
								<c:if test="${fns:getUser().company.uuid eq '7a45838277a811e5bc1e000c29cf2586'}">
									<span class="order-yingshou-title on">应收金额</span>/<span class="order-weishou-title">未收余额</span>
								</c:if>
							</th>
							<th width="7%">已收总金额<br>到账总金额</th>
							<th width="4%">操作</th>
							<th width="4%">下载</th>
							<th width="4%">财务</th>
						</tr>
						</thead>
						<tbody>
						<c:if test="${ empty resultFormList}">	
						<tr ><td colspan="14" >
						<center>暂无搜索结果</center>
						</td>
						</tr>
						</c:if>
						<c:if test="${ !empty resultFormList}">	
						<c:forEach items="${resultFormList}" var="result" varStatus="s">
							<tr id="visa" onclick="changeCount(this,'${result.orderCode}');">
							<!-- 序号 -->
								<td>
									<span class="sqcq-fj">
										<input type="checkbox" class="tdCheckBox" value="${result.orderCode}@<c:if test="${not empty result.confirmationFileId}">${result.orderId}</c:if>" name="visaOrderId" onclick="visaOrderCheckchg()" />
										${(page.pageNo-1)*page.pageSize + s.count}
									</span>
								</td>
								<td>
								<c:choose>
									<c:when test="${!empty result.paymentType}">
										<div class="ycq yj" style="margin-top:1px;">
											${result.paymentType}
										</div>
									</c:when>
								</c:choose>
								<!-- 315需求 针对越柬行踪 将非签约渠道改为直客 -->
								<c:choose>
									<c:when test="${companyUUid eq '7a81a03577a811e5bc1e000c29cf2586' and result.agentinfoName eq '非签约渠道' }">未签</c:when>
									<c:when test="${companyUUid eq '7a81b21a77a811e5bc1e000c29cf2586' and result.agentinfoName eq '非签约渠道' }">直客</c:when>
									<c:otherwise>${result.agentinfoName }</c:otherwise>
								</c:choose>
								</td>

								<!-- 200 针对优加，签务签证订单 订单列表加入"渠道联系人" -->
								<c:if test="${fns:getUser().company.uuid eq '7a81c5d777a811e5bc1e000c29cf2586'}">
									<td>
										<span class="agentContactsNameSpan">${result.contactsName}</span>
										<input class="agentContactsName" value="${result.contactsName}" style="display: none"/>
									</td>
								</c:if>

								<c:choose>
									<c:when test="${dayangCompanyUuid==companyUUid}">
										<!-- 隐藏此列 -->
									</c:when>
									<c:otherwise>
										<td class="que_parent">${result.orderCode }<c:if test="${result.confirmFlag }"><i class="que"></i></c:if>
											<% if("1".equals(flag)) { %>
											<c:if test="${null != result.isRead}">   
												<c:if test="${0 ==result.isRead}">  
													<span class="new-tips" style="display:true;"></span>
												</c:if>	
											</c:if>
											<% } %>
										</td>
									</c:otherwise>
								</c:choose>
                                
                                <!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-s-签务签证订单列表页 -->
								<!-- 订单团号-->
								<c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 环球行团号 -->
								   <td>${result.orderTuanhaoFromVOTable }</td>
								</c:if>
								<c:if test="${fns:getUser().company.uuid ne '7a816f5077a811e5bc1e000c29cf2586'}"><!-- 非环球行团号 -->
								   <td>${result.orderTuanhao }</td>
								</c:if>
								<!--  C460V3Plus,对于环球行,团号取的是订单的groupCode不是产品的groupCode-e -->
								
								<!-- 领区联系人 -->
								<!--  <td>${result.contactPerson }</td>-->
								<!-- 订单编号 -->
								<td>${result.productCode }</br>${result.productName }</td>
								<%-- 订单状态 --%>
								<td>
									<c:choose>
										<c:when test="${'1' eq result.payStatus}">未收款</c:when>
										<c:when test="${'3' eq result.payStatus}">预定</c:when>
										<c:when test="${'5' eq result.payStatus}">已收款</c:when>
										<c:when test="${'99' eq result.payStatus}">已取消</c:when>
									</c:choose>
								</td>
								<%--销售/下单人 --%>
								<td><span class="order-saler onshow">${result.salerName }</span><span class="order-picker">${result.creatUser }</span></td>
								<%--下单时间 --%>
								<td><fmt:formatDate value="${result.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
								<%--人数 --%>
								<td>${result.travelerCount }</td>
								<td class="tr">
									<c:if test="${fns:getUser().company.uuid ne '7a45838277a811e5bc1e000c29cf2586'}">
										<span class="tdorange fbold ">
											${fns:getMoneyAmountBySerialNum(result.visaPay,2)}
										</span>
									</c:if>
									<c:if test="${fns:getUser().company.uuid eq '7a45838277a811e5bc1e000c29cf2586'}">
										<span class="order-yingshou onshow">
											<span class="tdorange fbold ">
												${fns:getMoneyAmountBySerialNum(result.visaPay,2)}
											</span>
										</span>
										<span class="order-weishou">
											<span class="tdorange fbold ">
												<c:if test="${'1' ne result.isAsAccount }">
													<c:if test="${'1' eq result.payStatus || '3' eq result.payStatus}">
														${fns:getMoneyAmountBySerialNum(result.visaPay,2)}
													</c:if>
													<c:if test="${'5' eq result.payStatus}">
														${fns:getDecrease(result.visaPay,result.payedMoney)}
													</c:if>
												</c:if>
												<c:if test="${'1' eq result.isAsAccount }">
													${fns:getDecrease(result.visaPay,result.accountedMoney)}
												</c:if>
											</span>
										</span>
									</c:if>
								</td>
								<td class="p0 tr">	
									<div class="yfje_dd">	
										<span class="fbold">
											<c:if test="${'1' eq result.visaOrderStatus }">
												${fns:getMoneyAmountBySerialNum(result.payedMoney,2)}
											</c:if>
										</span>
									</div>
									<div class="dzje_dd">
											<span class="fbold">
												<c:if test="${'0' eq result.isAsAccount || '1' eq result.isAsAccount || '2' eq result.isAsAccount }">
													${fns:getMoneyAmountBySerialNum(result.accountedMoney,2)}
												</c:if>
											</span>
									</div>
								</td>		
						
								
								<td class="p0">
									<dl class="handle">
										<dt><img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作"></dt>
										<dd>
											<p>
												<span></span> 
												<a target="_blank" href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId=${result.orderId}&details=1">详情 </a>
												<c:if test="${'1' != result.orderStatus && result.lockStatus != 1 && result.activityLockStatus != 1}">
													<a target="_blank" href="${ctx}/visa/order/goUpdateVisaOrder?visaOrderId=${result.orderId}">修改</a>
												</c:if>
												<a target="_blank" href="${ctx}/visa/interviewNotice/list?orderId=${result.orderId}">面签通知</a>
												<a target="_blank" href="${ctx}/visa/order/orderTable?orderId=${result.orderId}">查看预约表</a>
                                                <shiro:hasPermission name="visaOrderForOp:operation:lock">
                                                  <!-- 非取消订单才可以锁死、解锁 -->
                                                   <c:if test="${result.payStatus != 99 && result.activityLockStatus != 1}">
	                                                   <c:if test="${result.lockStatus == 1 }">
													        <a href="javascript:unLockOrder(${result.orderId});">解锁</a>
													    </c:if>
													    <c:if test="${result.lockStatus == 0 || result.lockStatus == null }">
													        <a href="javascript:lockOrder(${result.orderId});">锁死</a>
													    </c:if>
                                                   </c:if>
											    </shiro:hasPermission>
												<c:if test="${jieKuanFlag==false}">
												    <!-- 屏蔽原有 的 新型者借款链接   -->
												    <!-- 
													<a href="${ctx}/visa/workflow/borrowmoney/applyForm?visaOrderId=${result.orderId}" target="_blank">借款</a>
													 -->
													 <a href="${ctx}/visa/xxz/borrowmoney/toVisaXXZBorrowMoneyAppPage?visaOrderId=${result.orderId}" target="_blank">借款</a>
												</c:if>
												<c:if test="${isAllowMultiRebateObject == 1 }">
													 <a href="${ctx}/visa/rebate/visaRebateList?orderId=${result.orderId}" target="_blank">返佣</a>
												</c:if>
												<!-- 上传确认单 -->
												<c:if test="${result.payStatus!=99 }"><!-- 已取消订单不能上传 -->
													<shiro:hasPermission name="visaOrderForOp:upload:confirmation">
														<a href="javascript:void(0)"  onclick="uploadConfirmFiles('confirmFiles',this,'${result.orderId}','/visa/order/visaUploadConfirmation');">上传确认单</a>
													</shiro:hasPermission>
												</c:if>
												<a onclick="expand('#child_${result.orderId }','${result.orderId }',this,'${result.lockStatus }','${result.activityLockStatus }','${result.main_orderId }') " href="javascript:void(0)">展开客户</a>
										   </p>
										</dd>
									</dl>
								</td>
								<td class="p0">
									<dl class="handle">
										<dt><img src="${ctxStatic}/images/handle_xz_rebuild.png" title="下载"></dt>
										<dd>
											<p>
												<span></span>
												<a target="_blank" id="huzhaolingqu" href="${ctx}/order/download/download?fileName=<c:choose>
													<c:when test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">hzlqd.rar</c:when>
													<c:otherwise>hzlqd_other.rar</c:otherwise>
												</c:choose>">护照领取单</a>
												<shiro:hasPermission name="visaOrderForOp:download:pretrial">
												<a target="_blank" href="${ctx}/order/download/exportExcel?orderId=${result.orderId}">预审表</a>
												</shiro:hasPermission>
												<shiro:hasPermission name="visaOrderForOp:download:reservation">
												<a target="_blank" href="${ctx}/order/download/exportYuyueExcel?orderId=${result.orderId}">预约表</a>
												</shiro:hasPermission>
												<%-- <a target="_blank" href="${ctx}/visa/order/interviewNotice?type=order&id=${result.orderId}">面签通知</a> --%>
												
												<!-- 0214新增面签通知附件上传,下载上传文件-s -->
												  <a target="_blank" href="${ctx}/visa/order/interviewNoticeAll?type=order&id=${result.orderId}">面签通知</a>
												<!-- 0214新增面签通知附件上传,下载上传文件-e -->
												
												<shiro:hasPermission name="visaOrderForOp:download:confirmation">
													<c:if test="${not empty result.confirmationFileId}">
														<a href="javascript:void(0)"  onClick="downloadConfirm('${result.orderId}','/visa/order/downloadConfirmFiles')">确认单</a>
													</c:if>
												</shiro:hasPermission>
										   </p>
										</dd>
									</dl>
								</td>
								<td class="p0">
									<dl class="handle">
										<dt>
											<img src="${ctxStatic}/images/handle_fk_rebuild.png" title="财务">
										</dt>
										<dd>
											<p>
												<span></span>
									 
												<c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty result.main_orderId)}">
												<a onclick="expandPaidRecord('#payRecord_${result.orderId}','${result.orderId}',this);"  href="javascript:void(0)">收款记录</a>
												<!-- 原来 借款 记录  关闭 -->
												<!-- 
												<a target="_blank" href="${ctx}/visa/order/borrowMoneyRecord?orderId=${result.orderId}">借款记录</a>
												 -->
													<!-- activiti借款记录    新行者71 -->
													<c:if test="${fns:getUser().company.uuid eq '7a8175bc77a811e5bc1e000c29cf2586'}">
													   <!--  a target="_blank" href="${ctx}/visa/xxz/borrowmoney/borrowMoneyRecord4XXZactivity?orderId=${result.orderId}">act借xxz</a-->
													   <a target="_blank" href="${ctx}/visa/xxz/borrowmoney/borrowMoneyRecord4XXZactivity?orderId=${result.orderId}">借款记录</a>
													</c:if>
													
													<!-- 环球行借款记录 -->           
								                    <c:if test="${fns:getUser().company.uuid eq '7a816f5077a811e5bc1e000c29cf2586'}">
								                    <!--activiti借款记录    环球行68 -->
								                     <a target="_blank" href="${ctx}/visa/hqx/borrowmoney/borrowMoneyRecord4HQXactivity?orderId=${result.orderId}">借款记录</a>
								                        <!-- 还收据记录暂不开放 activiti还收据记录 -->
								                        <!-- 
													    <a target="_blank" href="${ctx}/visa/hqx/returnvisareceipt/returnReceiptRecord4HQXactivity?orderId=${result.orderId}">activiti还收据记录</a>
													     -->
													</c:if>
												
												</c:if>
											</p>
										</dd>
									</dl>
								</td>
							</tr>
							
								<!-- 支付记录 -->
					<tr id="payRecord_${result.orderId}" name="subtr" style="display: none;" class="activity_team_top1">
						<td colspan="14" class="team_top" style="background-color:#dde7ef;">
							<table  class="table activitylist_bodyer_table" style="margin:0 auto;" >
								<thead>
									<tr>
										<th>游客姓名</th>
										<th>收款方式</th>
										<th>金额</th>
										<th>日期</th>
										<th>收款类型</th>
										<th>是否已确认达账</th>
										<th>收款凭证</th>
									    <th colspan=2>操作</th>
									</tr>
								</thead> 
								<tbody id="recordInfo_${result.orderId}">
								
								</tbody>
							</table>
						</td>
					</tr>
							
							<tr id="child_${result.orderId}" class="activity_team_top1" style="display:none;">
								<td colspan="14" class="team_top">
									<table class="table activitylist_bodyer_table table_th_nop" style="margin:0 auto;">
										<thead>
											<tr>
												<th width="8%">姓名</th>
												<th width="7%">护照号</th>
												<th width="3%">签证类别</th>
												<th width="3%">签证国家</th>   
												<th width="3%">领区</th>
												<th width="6%">参团类型</th>
												<th width="7%">预计出团时间<br/>实际出团时间</th>
												<th width="7%">预计约签时间
												<!-- 懿洋假期的美国签证隐藏 C482
												uuid：f5c8969ee6b845bcbeb5c2b40bac3a23
												-->
												<c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && result.visaCountry=='美国')}">
													<br/>实际约签时间
												</c:if>
												<!-- 懿洋假期的美国签证隐藏 C482-->
												</th>
												<th width="7%">&nbsp;&nbsp;说明会时间<br/>实际送签时间</th>
												<th width="5%">签证状态</th>
												<th width="5%">护照状态</th>
												<th width="6%">担保类型</th>
												<th width="7%">应收押金</th>
												<th width="7%">已收押金<br />达账押金</th>
												<th width="5%">借款状态</th>
												<th width="5%">收款状态</th>
												<th width="5%">保存<br />修改</th>	
												<th width="3%">操作</th>	
											</tr>
										</thead>
										<tbody id="traveler_${result.orderId}">
										
											</tbody>
									</table>
								</td>
							</tr>
							</c:forEach>
							<tr class="checkalltd">
								<td colspan='19' class="t1">
									<label> <input type="checkbox" name="visa_orderAllChk" onclick="visa_orderAllChecked(this)" /> 全选 </label> 	
									<label> <input type="checkbox" name="visa_orderAllChkNo" onclick="visa_orderAllNoCheck()" /> 反选 </label>
									<shiro:hasPermission name="visaOrderForOp:download:confirmation">
										<input type="button" class="btn" value="批量下载确认单" onclick="visa_orderBatchDownload('visaOrderId')">
									</shiro:hasPermission>
									<shiro:lacksPermission name="visaOrderForOp:download:confirmation">
										<input type="button" class="btn gray" value="批量下载确认单" disabled="disabled">
									</shiro:lacksPermission>
								</td>
							</tr>
							</c:if>
						</tbody>
					</table>
					<div class="pagination clearFix">${page}</div>
				</div>
				
				
				<div id="youke">
					<div class="activitylist_bodyer_right_team_co_paixu">
							<div class="activitylist_paixu">
								<div class="activitylist_paixu_left">
									<ul>
					                    	<c:choose>
												<c:when test="${travelPage.orderBy == 're.createDate DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('re.createDate',this)">
							                    		借款日期
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${travelPage.orderBy == 're.createDate ASC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('re.createDate',this)">
							                    		借款日期
														<i class="icon icon-arrow-up"></i>
						                    			</a>
													</li>
												</c:when>
												<c:otherwise>
													<li class="activitylist_paixu_left_biankuang  lipro.updateDate">
						                    			<a onclick="youkesortby('re.createDate',this)">
							                    		借款日期
						                    			</a>
													</li>
												</c:otherwise>
											</c:choose>
											
											<c:choose>
												<c:when test="${travelPage.orderBy == 'vo.create_date DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('vo.create_date',this)">
							                    		创建时间
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${travelPage.orderBy == 'vo.create_date ASC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('vo.create_date',this)">
							                    		创建时间
														<i class="icon icon-arrow-up"></i>
						                    			</a>
													</li>
												</c:when> 
												<c:otherwise>
													<li class="activitylist_paixu_left_biankuang  lipro.updateDate">
						                    			<a onclick="youkesortby('vo.create_date',this)">
							                    		创建时间
						                    			</a>
													</li>
												</c:otherwise>
											</c:choose>
											
											<c:choose>
												<c:when test="${travelPage.orderBy == 'vo.update_date DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('vo.update_date',this)">
							                    		更新时间
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${travelPage.orderBy == 'vo.update_date ASC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('vo.update_date',this)">
							                    		更新时间
														<i class="icon icon-arrow-up"></i>
						                    			</a>
													</li>
												</c:when> 
												<c:otherwise>
													<li class="activitylist_paixu_left_biankuang  lipro.updateDate">
						                    			<a onclick="youkesortby('vo.update_date',this)">
							                    		更新时间
						                    			</a>
													</li>
												</c:otherwise>
											</c:choose>
											
									</ul>
								</div>
							 <div class="activitylist_paixu_right">
							 <c:choose>
							 	<c:when test="${travelPage.count >0}">
							 	查询结果&nbsp;&nbsp;<strong>${travelPage.count}</strong>&nbsp;条
							 	</c:when>
							 	<c:otherwise>
							 	查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
							 	</c:otherwise>
							 </c:choose>
							</div>
							  <div class="kong"></div>
							</div>
						</div>
						<table class="table activitylist_bodyer_table mainTable table_th_nop">
							<thead>
								<tr>
									<th width="5%">姓名</th>
									<th width="6%">护照号</th>
									<!-- <th width="7%">AA码</th> -->
									<th width="2%">签证类别</th>
									<th width="2%">签证国家</th>
									<th width="2%">领区</th>
									<th width="7%">预计出团时间<br />实际出团时间</th>
									<th width="7%">预计约签时间<br />实际约签时间</th>
									<th width="6%">说明会时间</th>
									<th width="7%">签证状态</th>
									<th width="7%">护照状态</th>
									<th width="6%">担保类型</th>
									<th width="6%">下单人</th>
									<th width="4%">借款状态</th>
									<th width="6%">借款申请日期</th>
									<th width="6%">应收押金</th>
									<th width="6%">已收押金<br />达账押金</th>
									<th width="5%">保存<br />修改</th>	
									<th width="5%">操作</th>	
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${travelList}" var="traveler" varStatus="count">
									<tr>
										<td width="4%" class="p0">
										<c:choose>
																<c:when test="${!empty traveler.paymentType}">
																	<div class="ycq yj" style="margin-top:1px;">
																		${traveler.paymentType}
																	</div>
																</c:when>
															</c:choose>
<span id="traveler_travelerName" class="sqcq-fj">
<c:if test="${ttsFlag}">
<c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty traveler.main_orderId)}">
${traveler.main_orderId}
<input type="checkbox" class="tdCheckBox" trallerId="${traveler.id }" visaId="${traveler.visaId}"  trallerName="${traveler.travelerName }" value="${traveler.id }@${traveler.visaorderId}" name="activityId" onclick="t_idcheckchg(this)"/>
</c:if>
</c:if>
${traveler.travelerName}</span>
<input type="hidden" value="${traveler.agentinfoId }" id="activityId_agentId_${traveler.id}">

												</td>
													<td width="6%">
														<span id="traveler_passportId">${traveler.passportId}</span>
													</td>
													 <!-- <td width="7%">
														<input name="" type="text" maxlength="40" id="traveler_AACode_${traveler.id }" value="${traveler.AACode}" /> 
													</td>-->
													<td width="6%">
														<span id="traveler_visaType">${traveler.visaType}</span>
													</td>
													<td width="6%">
														<span id="traveler_visaCountry">${traveler.visaCountry}</span>
													</td>
													<!-- 领区 -->
													<td width="6%">
														<span id="traveler_visaArea">${traveler.collarZoning}</span>
													</td>
													<td width="7%" class="tc">
														<div class="yfje_dd">	
															<span class="fbold">
																<span id="traveler_forecastStartOut">${traveler.forecastStartOut}</span>
															</span>
														</div>
														<div class="dzje_dd">
															<span class="fbold" > 
																<input id="traveler_startOut_${traveler.id }" value="${traveler.startOut}" onclick="WdatePicker()" name="" type="text" /> 
															</span>
														</div>
													</td>
													
													<td width="7%" class="tc">
														<div class="yfje_dd">	
															<span class="fbold">
																<span id="traveler_forecastContract">${traveler.forecastContract}</span>
															</span>
														</div>
														<div class="dzje_dd">
														    
															<span class="fbold" > 																						     
																<input id="traveler_contract_${traveler.id }" value="${traveler.contract}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" name="" type="text" />			
															</span>
														</div>
														<!-- 懿洋假期的美国签证隐藏 实际约签时间C482
														uuid：f5c8969ee6b845bcbeb5c2b40bac3a23-->
														
                                     					<%-- <c:if test="${!(fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && traveler.visaCountry=='美国')}">										
									
														<div class="dzje_dd">
														    
															<span class="fbold" > 																						     
																<input id="traveler_contract_${traveler.id }" value="${traveler.contract}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" name="" type="text" />			
															</span>
														</div>
														</c:if> 
														<c:if test="${fns:getUser().company.uuid == 'f5c8969ee6b845bcbeb5c2b40bac3a23' && traveler.visaCountry=='美国'}">										
									
														<div class="dzje_dd" style="display: none">
														    
															<span class="fbold" > 																						     
																<input id="traveler_contract_${traveler.id }" value="${traveler.contract}" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" name="" type="text" />			
															</span>
														</div>
														</c:if> --%> 
														<!-- 懿洋假期的美国签证隐藏 实际约签时间 C482-->
													</td>
													<!-- 说明会时间 -->
													<td width="7%">
														<span id="traveler_explanationMeeting" style="color:green">${traveler.explanationTime}</span>
													</td>
													<td width="7%">
														<select id="traveler_visaStatus_${traveler.id }" name="" style="width:100%;">
														 <option value="-1" selected="selected">请选择</option>
															<c:forEach items="${visaStatusList}" var="visaStatus">
																
																	<c:choose>
																		<c:when test="${visaStatus.value == 2}">
																			<c:choose>
																				<c:when test="${visaStatus.value eq traveler.visaStatus}">
																					<option selected="selected" value="2">已约签</option>
																				</c:when>
																				<c:otherwise>
																					<option value="2">已约签</option>
																				</c:otherwise>
																			</c:choose>
																		</c:when>
																		<c:when test="${visaStatus.value == 3}">
																			<c:choose>
																				<c:when test="${visaStatus.value eq traveler.visaStatus}">
																					<option selected="selected" value="3">通过</option>
																				</c:when>
																				<c:otherwise>
																					<option value="3">通过</option>
																				</c:otherwise>
																			</c:choose>
																		</c:when>
																		<c:otherwise>
																			<c:choose>
																				<c:when test="${visaStatus.value eq traveler.visaStatus}">
																					<option selected="selected" value="${visaStatus.value}">${visaStatus.label}</option>
																				</c:when>
																				<c:otherwise>
																					<option value="${visaStatus.value}">${visaStatus.label}</option>
																				</c:otherwise>
																			</c:choose>
																		</c:otherwise>
																	</c:choose>
																
															</c:forEach>
														</select> 
													</td>
													<td width="7%">
													  <input type="hidden" id="hidden_passportStatus_${traveler.id}" value="${traveler.passportStatus}">
														<select id="passportStatus_${traveler.id }"  name="" style="width:100%;">
															 <option value="0" selected="selected">请选择</option>
															<c:choose>
																<c:when test="${ '1' eq traveler.passportStatus}">
																	<option value="1" selected="selected">借出</option>
																</c:when>
																<c:otherwise>
																	<option value="1">借出</option>
																</c:otherwise>
															</c:choose>
															
															<c:choose>
																<c:when test="${ '2' eq traveler.passportStatus}">
																	<option  selected="selected" value="2">销售已领取</option>
																</c:when>
																<c:otherwise>
																	<option value="2">销售已领取</option>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when test="${ '4' eq traveler.passportStatus}">
																	<option selected="selected" value="4" >已还</option>
																</c:when>
																<c:otherwise>
																	<option value="4" >已还</option>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when test="${ '5' eq traveler.passportStatus}">
																	<option selected="selected" value="5" >已取出</option>
																</c:when>
																<c:otherwise>
																	<option value="5" >已取出</option>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when test="${ '6' eq traveler.passportStatus}">
																	<option selected="selected" value="6" >未取出</option>
																</c:when>
																<c:otherwise>
																	<option value="6" >未取出</option>
																</c:otherwise>
															</c:choose>
															<c:choose>
																<c:when test="${ '8' eq traveler.passportStatus}">
																	<option selected="selected" value="8" >计调领取</option>
																</c:when>
																<c:otherwise>
																	<option value="8" >计调领取</option>
																</c:otherwise>
															</c:choose>
															
														</select> 
													</td>
													<td width="6%">
<select id="traveler_guaranteeStatus_${traveler.id }" onchange="xuanze('${traveler.visaId}','${traveler.id}','traveler_guaranteeStatus_${traveler.id }','traveleryingshouyajin_${traveler.id}',${traveler.visaorderId})" name="" style="width:100%;">
														<c:choose>
																<c:when test="${ '0' eq traveler.guaranteeStatus}">
																	<option selected="selected" value="0" >请选择</option>
																</c:when>
																<c:otherwise>
																	<option value="0" >请选择</option>
																</c:otherwise>
														</c:choose>
															
														<c:choose>
																<c:when test="${ '1' eq traveler.guaranteeStatus}">
																	<option value="1" selected="selected">担保</option>
																</c:when>
																<c:otherwise>
																	<option value="1">担保</option>
																</c:otherwise>
															</c:choose>
															
															<c:choose>
																<c:when test="${ '2' eq traveler.guaranteeStatus}">
																	<option  selected="selected" value="2">担保+押金</option>
																</c:when>
																<c:otherwise>
																	<option value="2">担保+押金</option>
																</c:otherwise>
															</c:choose>
															
															<c:choose>
																<c:when test="${ '3' eq traveler.guaranteeStatus}">
																	<option selected="selected"  value="3">押金</option>
																</c:when>
																<c:otherwise>
																	<option value="3">押金</option>
																</c:otherwise>
															</c:choose>

															<c:choose>
																<c:when test="${ '4' eq traveler.guaranteeStatus}">
																	<option selected="selected"  value="4">无需担保</option>
																</c:when>
																<c:otherwise>
																	<option value="4">无需担保</option>
																</c:otherwise>
															</c:choose>
														</select> 
														
													</td>
													
													<td  width="6%" class="p0 tr">	
													<span><p align="center"> ${traveler.creatUser} </p></span>
													</td>
													
													<td  width="4%" class="p0 tr">	
													<span><p align="center"> ${traveler.jiekuanStatus} </p></span>
													</td>
													
													<td  width="6%" class="p0 tr">	
													<span><p align="center"> ${traveler.jiekuanTime} </p></span>
													</td>
													<td class="tr" id="traveleryingshouyajin_${traveler.id}"><c:if test="${traveler.guaranteeStatus eq 2 or traveler.guaranteeStatus eq 3}">${traveler.totalDeposit}</c:if></td>
													<td width="7%" class="p0 tr">	
														<div class="yfje_dd">	
															<span class="fbold">
															<!-- 
														<c:if test="${ '3' eq traveler.guaranteeStatus}">

														</c:if>
														 -->
														${traveler.payedDeposit}
															</span>
														</div>
														<div class="dzje_dd">
															<span class="fbold" id="accountedDeposit">
																${traveler.accountedDeposit}
															</span>
														</div>
													</td>
													<td class="tc" width="5%"><input class="btn btn-primary" type="button" value="保存" 
														onclick="updateTraveler_qianwu1('${traveler.orderStatus}','traveler_AACode_${traveler.id }','traveler_visaStatus_${traveler.id }','passportStatus_${traveler.id }','traveler_guaranteeStatus_${traveler.id }','traveler_startOut_${traveler.id }','traveler_contract_${traveler.id }','${traveler.id }','${traveler.visaId }','${traveler.visaorderId}')"></td>
													<td width="5%" class="p0">
														<dl class="handle">
															<dt><img src="${ctxStatic}/images/handle_cz_rebuild.png" title="操作"></dt>
															<dd>
																<p>
																	<span></span>
																	  <c:if test="${fns:getUser().company.id != '71' }">
																	  	<c:if test="${traveler.lockStatus != 1 && traveler.activityLockStatus != 1 }">
																	  	<!-- 
																	  	<a href="javascript:void(0)" onclick="jbox_hsj_qianwu('${traveler.id}');">
																		 	3还收据
																		 </a>
																		 -->
																	  </c:if>
																	  </c:if>
																	  <c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty traveler.main_orderId)}">
																	 <a href="javascript:void(0)"
																	 onclick="jbox_jkmx_qianwu('${traveler.travelerName}','${traveler.passportId}','${traveler.creatUser}','${traveler.createTime}','${traveler.groupType}','${traveler.creatUser}','${traveler.visaorderNo}','${traveler.jiekuanCreateUser}','${traveler.jiekuanBizhong}${ traveler.jiekuanAmount}','${traveler.jiekuanRemarks }');">
																	 	借款明细
																	 </a>
																	 </c:if>
																	 <c:if test="${traveler.lockStatus != 1 && traveler.activityLockStatus != 1 }">
																		<c:if test="${jieKuanFlag}">
																		<c:if test="${companyUUid ne '7a81a26b77a811e5bc1e000c29cf2586' || (companyUUid eq '7a81a26b77a811e5bc1e000c29cf2586' &&  empty traveler.main_orderId)}">
																		   <!-- 
																			<a href="javascript:void(0)" onclick="jbox_jk_qianwu_new('${traveler.id}',$(this));">
																				3借款
																			 </a>
																			  -->
																			 </c:if>
																		 </c:if>
																		 <a href="javascript:void(0)" 
																		 onclick="xiugaiyajin_qianwu('${traveler.visaId}','${traveler.totalDepositUUID }','${traveler.id }','${traveler.visaorderId}','traveleryingshouyajin${traveler.visaId}${traveler.id}${count.index}') ">
																		 	修改押金
																		 </a>
																		 	<a href="javascript:void(0)" onclick="jbox_jhz('${traveler.id}','${traveler.visaId}');">借护照</a>
																		 <a href="javascript:void(0)" onclick="jbox_hhz('${traveler.id}','${traveler.visaId}');">还护照</a>
																		 <c:if test="${tuiYaJinFlag}">
																		 <!--  
																			<a href="${ctx}/order/manager/visaDeposit/refundList?proId=${traveler.visaorderId}">退签证押金</a>
																			-->
																		 </c:if>
																		 <c:if test="${huanYaJinShouJuFlag}">
																		 <!--  
																			<a href="javascript:void(0)" onclick="jbox_hyjsj('${traveler.id}');">还押金收据</a>
																		 -->
																		 </c:if>
																		 <a href="javascript:void(0)" onclick="jbox_qszl_qianwu('${traveler.visaProductId}','${traveler.visaId}');">签收资料</a>
																	 </c:if>
																</p>
															</dd>
														</dl>
													</td>
												</tr> 
												</c:forEach>
												
												<c:if test="${ttsFlag}">	
												<tr class="checkalltd">
													<td colspan='15' class="tl">
														<label>
															<input type="checkbox" name="allChk" onclick="t_checkall(this)">全选
														</label>
														<label>
														<input type="checkbox" name="allChkNo" onclick="t_checkallNo(this)">反选
														</label>

											<!--  游客Tab列表  -->
												<c:if test="${traveler.lockStatus != 1 && traveler.activityLockStatus != 1 }">
													<c:if test="${fns:getUser().company.id != '71' }">
														<c:if test="${jieKuanFlag ==true}">
															<a onclick="batchJk(null);">批量借款</a>
														</c:if>
														<a id="piliang_o_huanshouju_${traveler.visaorderId}" onclick="batchHsj(null);">批量还收据</a>
													</c:if>
														<a onclick="batchUpdatePassport(null,'1')">批量借护照</a>
														<a onclick="batchUpdatePassport(null,'2')">批量还护照</a>
												</c:if>
												<a onclick="batchUpdateVisaStatus(null)">批量操作签证</a>
												<c:if test="${tuiYaJinFlag}">
												<a id="piliang_t_tuiyajin" onclick="batchtuiyajin('piliang_t_tuiyajin','activityId');">批量退押金</a>
												</c:if>
												</td></tr>
											</c:if>
											</tbody>
										</table>
						<div class="pagination clearFix">${travelPage}</div>
					</div>
				</div>
                <!--右侧内容部分结束-->

<!--66 设置/修改押金+担保金额弹窗内容 START-->
<div id="setYajindanbao" style="display:none">
	<div style="margin-top:20px; padding-left:20px;">
		<p>押金：</p>
		<p>
			<label class="jbox-label">选择币种：</label>
			<select id="currencyId" class="jbox-width100">
				<c:forEach items="${currencyList}" var="currency">
					<option value ="${currency.id}">${currency.currencyName}</option>
				</c:forEach>
			</select>
		</p>
		<label class="jbox-label">押金金额：</label>
		<input id="yingshouyajin" type="text" onafterpaste="refundInput(this)" onkeyup="refundInput(this)" class="jbox-width100" value="">
		<span></span>
	</div>
</div>
<!--66 设置/修改押金+担保金额弹窗内容 END-->
</body>
</html>
