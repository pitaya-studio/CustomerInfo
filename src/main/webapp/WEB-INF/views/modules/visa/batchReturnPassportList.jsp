<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<head>
<meta name="decorator" content="wholesaler"/>
<%--t2改版 去掉重复引用的样式 bootstrap huanqiu jh modified by Tlw--%>
<title>订单-签务身份订单</title>
 

<!--[if lte IE 6]>
<link type="text/css" rel="stylesheet" href="css/bootstrap-ie6.min.css" />
<script type="text/javascript" src="js/bootstrap-ie.min.js"></script>
<![endif]-->
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
<script type="text/javascript">
$(function(){
	g_context_url = "${ctx}";
	//搜索条件显示隐藏
	launch();
	//文本框提示信息
	inputTips();
	//操作浮框
	operateHandler();
	document.getElementById("jiehuzhaoliebiao").className="select";
    document.getElementById("jiekuanliebiao").className="";
	//可输入select
	$(".selectinput" ).comboboxSingle();
});

//订单-签务身份订单-还护照
function jbox_dghhz(travelerId,visaId) {
//	if(!(document.getElementById("passportStatus_"+travelerId).value=="1")){
//		top.$.jBox.tip('护照状态不符！');
//		return;
//	}
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
		 //验证时间周期
		/*  var t1=passportOperateTime.substring(0,10);
		 var str = t1.replace(/-/g, '/'); 
		 var date = new Date(str);
		  if(date>new Date()){
			 $.jBox.info("时间不能早于当前日期", "信息");
			 return false;
		 }   */
		if (v=="1"){
			 var passportOperator = $("#passportOperator").val();
			 var passportOperateTime = $("#passportOperateTime").val();
			 var passportOperateRemark = $("#passportOperateRemark").val();
			 $("#passportStatus"+visaId+" option").eq(3).attr("selected",true);
			 
			 $.ajax({                 
					cache: true,                 
					type: "POST",                 
					url:g_context_url+ "/visa/order/updatePassportStatus",                 
					data:{id:visaId, 
						travelerId:travelerId, 
						passportStatus:"4",
						passportOperator:passportOperator,
						passportOperateTime:passportOperateTime,
						passportOperateRemark:passportOperateRemark,
						passportOperateRemark:passportOperateRemark},                
					async: false,                 
					error: function(request) {                     
						top.$.jBox.tip('操作失败');
					},                 
					success: function(data) {                     
						$("#visa_status_"+travelerId).html("已还");
					    $("#visa_changtime_"+travelerId).html(passportOperateTime);
						top.$.jBox.tip('操作成功');
						return true;
					}             
				});
		}
	},height:220,width:380});

	
}
function showTravelerList(data,visaIds){
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
			var html="";
			for(var i = 0,len = traveler.length; i<len; i++){
			 	html += '<tr><td width="4%" class="p0"><span class="sqcq-fj"><input type="checkbox" name="activityId" onclick="idcheckchg(this)" trallerId="'+traveler[i].tid+'" visaId="'+traveler[i].visaId+'" value="'+traveler[i].tid+'"/>'+traveler[i].tname+'</span></td>';
			    html += '<input type="hidden" id="passportStatus_'+traveler[i].tid+'" value="'+traveler[i].passportStatus+'">';
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
			 	html +='<td id="visa_status_'+traveler[i].tid+'" width="7%">'+traveler[i].passportStatusDesc+'</td>';
			 	html +='<td width="6%">'+traveler[i].passportoperator+'</td>';
			 	html +='<td width="3%" class="tc"><a href="javascript:void(0)"  onclick="jbox_dghhz('+traveler[i].tid+','+traveler[i].visaId+')" >还护照</a></td></tr>';
		
			}
		     html += '<tr class="checkalltd"><td colspan="11" class="tl"><label><input type="checkbox" name="allChk" onclick="checkall(this,\''+batchNo+'\')">全选</label><label><input type="checkbox" name="allChkNo" onclick="checkallNo(this,\''+batchNo+'\')">反选</label><a  onclick="batchUpdatePassporty(\''+batchNo+'\')">批量还护照</a></td></tr>';
			 $("#travelerList_"+batchNo).empty();
			 $("#travelerList_"+batchNo).append(html);
		}
	});
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
var resetSearchParams = function(){
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
		$("#travelerList_"+batchNo+"  input[name='allChk']").not("input:checked").each(function(){this.checked=true;});
	}else{ 
		$("#travelerList_"+batchNo+" input[name='activityId']:checked").each(function(){this.checked=false;}); 
		$("#travelerList_"+batchNo+"  input[name='allChk']:checked").each(function(){this.checked=false;});	
	} 
}
function checkallNo(obj,batchNo){
	$("#travelerList_"+batchNo+" input[name='activityId']").each(function () {   
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

//批量还护照
function passportReturn(orderId,visaIds , travellerIds){
	 $.ajax({                 
			cache: true,                 
			type: "POST",                 
			url:g_context_url+ "/visa/order/checkBatchReturnPassport",                 
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
					 batchReturnPassport(msg);
				 }
	        }   
		});
}

function batchReturnPassport(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
//	html += '<p>不满足条件用户：</p>';
//	if(errList.length==0){
//		html += ' (无)';
//	}else{
//		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>护照状态</th><th>原因</th></tr></thead><tbody>';
//		for(var i = 0,len = errList.length; i<len; i++){
//			html += '<tr><td>'+errList[i].tname+'</td><td>'+errList[i].passportCode+'</td><td>'+errList[i].passportStatusDes+'</td><td>'+errList[i].errMsg+'</td></tr>';
//		}
//		html+='</tbody></table>';
//	}
//	html += '<p>满足条件用户：</p>';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量还护照",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:580});
		
	}else{
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">护照状态</th><th width="15%" >归还日期</th><th width="10%">护照归还人</th><th width="10%">备注</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].passportStatusDes+'</td><td><input type="text"  onclick="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" /></td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
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
						 	var travellArray = travellerIds.split(",");
							 var dateArray = dates.split(",");
							 for(var i =0;i<dateArray.length;i++){								 
								 var date = dateArray[i];
								 if(date!=null&&date!=""){
							   $("#visa_changtime_"+travellArray[i]).html(date);
								 }
							}  
													
							for(var i =0;i<travellArray.length;i++){
								 var travelerId = travellArray[i];
								 if(travelerId!=null&&travelerId!=""){
									$("#visa_status_"+travelerId).html("已还");
								 }
							} 
							top.$.jBox.tip("操作成功",'warning');
						 }
			        }   
				});
			
			
			
			return true;
		}
		},height:'auto',width:580});
		
	}
	
	
}
//wang
function batchUpdatePassporty(orderId){

	//标志位 判断是否有选中
	var travellerIds ="";
	var visaIds ="";
	//游客界面
	
		$("#travelerList_"+orderId+" input[type=checkbox][name=activityId]").each(function(){
			  if($(this).attr("checked")){
				  var trallerId =   $(this).attr("trallerId");
				  var visaId =   $(this).attr("visaId");
				  var trallerName =   $(this).attr("trallerName");
					 travellerIds+=trallerId+",";
					 visaIds+=visaId+",";
			  }
		});
	
	if(travellerIds==""){
		top.$.jBox.tip('请选择游客！');
		return;
	}
		backPassporty(orderId,visaIds,travellerIds);
	}
	
function backPassporty(orderId,visaIds , travellerIds){
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
					 batchReturnPassporty(msg);
				 }
	        }   
		});
}
function batchReturnPassporty(msg){
	var rightList =msg.rightList;
	var errList =msg.errList;
	var html = '<div style="margin-top:20px;padding:0 10px;overflow:scroll;height:500px;" id="borrowWin">';
//	html += '<p>不满足条件用户：</p>';
//	if(errList.length==0){
//		html += ' (无)';
//	}else{
//		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th>游客</th><th>护照号</th><th>护照状态</th><th>原因</th><th>批次号</th></tr></thead><tbody>';
//		for(var i = 0,len = errList.length; i<len; i++){
			//html += '<tr><td>'+errList[i].tname+'</td><td>'+errList[i].passportCode+'</td><td>'+errList[i].passportStatusDes+'</td><td>'+errList[i].errMsg+'</td><td>'+errList[i].batchno+'</td></tr>';
//		}
//		html+='</tbody></table>';
//	}
//	html += '<p>满足条件用户：</p>';
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量还护照",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:580});
		
	}else{
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="10%">护照状态</th><th width="22%">归还日期</th><th width="10%">护照归还人</th><th width="10%">备注</th><th width="5%">批次号</th></tr></thead><tbody>';
		//html += '<tr><td><input type="checkbox"/> 张三</td><td>2323555</td><td>未借</td><td><input type="text" value="2015-03-03"/></td><td><input type="text" value="李四"/></td><td><input type="text" /></td></tr>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].passportStatusDes+'</td><td><input type="text" onclick="WdatePicker({dateFmt:'+"'"+'yyyy-MM-dd HH:mm'+"'"+'})" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" /></td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td><td>'+rightList[i].batchno+'</td></tr>';
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
							//top.$.jBox.tip("操作成功",'warning');
							
							
							var travellArray = travellerIds.split(",");
							 var dateArray = dates.split(",");
							 for(var i =0;i<dateArray.length;i++){								 
								 var date = dateArray[i];
								 if(date!=null&&date!=""){
							   $("#visa_changtime_"+travellArray[i]).html(date);
								 }
							}  
													
							for(var i =0;i<travellArray.length;i++){
								 var travelerId = travellArray[i];
								 if(travelerId!=null&&travelerId!=""){
									$("#visa_status_"+travelerId).html("已还");
								 }
							}
							
							/* var travellArray = travellerIds.split(",");
							for(var i =0;i<travellArray.length;i++){
								 var travelerId = travellArray[i];
								 if(travelerId!=null&&travelerId!=""){
									$("#hidden_passportStatus_"+travelerId).val(4);
									$("#passportStatus_"+travelerId+" option").eq(4).attr("selected",true);
								 }
							} */
							
							top.$.jBox.tip("操作成功",'warning');
						 }
			        }   
				});
			
			
			
			return true;
		}
		},height:'auto',width:580});
		
	}
	
	
}	
//wang
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
//wang
function totalbatchUpdatePassporty( ){

	var batchnos ="";
		$("#totalcheck input[name='totalactivityId']").each(function(){
			  if($(this).attr("checked")){
				    var batchno = $(this).val();
					 batchnos+=batchno+",";
			  }
		});
		  batchnos= batchnos.substr(0,batchnos.length-1);
		 
	if(batchnos==""){
		top.$.jBox.tip('请选择批次！');
		return;
	}
	$.ajax({                 
		cache: true,                 
		type: "POST",                 
		url:g_context_url+ "/visa/order/totalcheckBatchReturnPassport",                 
		data:{ 
			"batchnos":batchnos
			},                
		async: false,                 
		 success: function(msg){
			 if(msg.msg!=null&&msg.msg!=""){
				 top.$.jBox.tip(msg.msg,'warning');
			 }else{
				 backPassporty(null,msg,null);
			 }
        }   
	});
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
    $("#searchForm").attr("action","${ctx}/visa/order/batchReturnPassportList");
    $("#orderBy").val(sortBy);
    $("#searchForm").submit();
}
</script>
</head>
<body>
				<div style="float:left;width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
					<form method="post" action="${ctx}/visa/order/batchReturnPassportList" id="searchForm">      
					<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			        <input id="orderBy" name="orderBy" type="hidden" value="${page.orderBy}"/>
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
							<div class="form_submit">
								 <input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
								 <input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
							</div>
						 
								 
						</div>
					</form>
					<!--查询结果筛选条件排序开始-->
					<div class="supplierLine">
						<a  href="${ctx }/visa/order/batchBorrowMoneyList"  id="jiekuanliebiao">借款列表</a>
						<a  href="${ctx }/visa/order/batchReturnPassportList" id="jiehuzhaoliebiao">借护照列表</a>
		           </div>
		           <br />
					<div class="activitylist_bodyer_right_team_co_paixu">
							<div class="activitylist_paixu">
								<div class="activitylist_paixu_left">
									<ul>
										<li >排序</li>
										<c:choose>
					                    	<c:when test="${page.orderBy == 'u.createTime DESC'}">
					                    		<li class="activitylist_paixu_moren">
					                    			<a onclick="cantuansortby('u.createTime',this)">
						                    			创建时间
						                    			<i class="icon icon-arrow-down"></i>
					                    			</a>
					                    		</li>
					                    	</c:when>
					                    	
					                    	
					                    	<c:when test="${page.orderBy == 'u.createTime ASC'}">
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
							 	<c:when test="${page.count >0}">
							 	查询结果&nbsp;&nbsp;<strong>${page.count}</strong>&nbsp;条
							 	</c:when>
							 	<c:otherwise>
							 	查询结果&nbsp;&nbsp;<strong>0</strong>&nbsp;条
							 	</c:otherwise>
							 </c:choose>
							</div>
							</div>
						</div>
					
					<!--查询结果筛选条件排序结束-->	 
					<table class="table activitylist_bodyer_table" id="contentTable">
						<thead>
						<tr>
							<th width="10%">批次</th>
							<th width="10%">操作人</th>
							<th width="12%">记录时间</th>
							<th width="10%">操作</th>
						</tr>
						</thead>
						<tbody id="totalcheck" >
							<c:forEach items="${page.list }" var="record">
							<tr>
								<%-- <td>${record.batchNo }</td> --%>
								<td ><input type="checkbox" name="totalactivityId"   value="${record.batchNo }"/>${record.batchNo }</td>
								<c:if test="${'单借'==record.batchNo}">
								  <td class="tc"></td>
								</c:if>
								<c:if test="${'单借'!=record.batchNo}">
								  <td class="tc">${record.createUserName }</td>
								</c:if>
								
								<td class="tc"><fmt:formatDate value="${record.createTime }" pattern="yyyy-MM-dd"/>
								</td>
								<td class="tc tda">
									<a class="team_a_click"  onclick="expand('${record.batchNo }',this,'${record.visaIds }')"  href="javascript:void(0)">游客列表</a>
									<a onclick="passportReturn(null,'${record.visaIds}',null);"href="javascript:void(0)">还护照</a>
								</td>								
							</tr>
							<tr id="child_${record.batchNo }" class="activity_team_top1" style="display:none">
								<td colspan="4" class="team_top">
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
												<th width="7%">护照状态</th>
												<th width="6%">借出人</th>
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
							<tr class="checkalltd"><td colspan="11" class="tl"><label><input type="checkbox" name="totalallChk" onclick="totalcheckall(this)">全选</label><label><input type="checkbox" name="allChkNo" onclick="totalcheckallNo(this)">反选</label><a  onclick="totalbatchUpdatePassporty()">批量还护照</a></td></tr>
						</tbody>
						
					</table>
				</div>
				
				<div class="pagination clearFix">	 ${pageStr}</div>


</body>
</html>
