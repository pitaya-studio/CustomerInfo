<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<meta name="decorator" content="wholesaler"/>
<title>订单-签证-批量操作</title>
<%--t2改版 去掉重复引用的样式 modified by Tlw--%>
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jqueryUI/themes/base/jquery.ui.all.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
<link type="text/css" rel="stylesheet" href="${ctxStatic}/css/jquery.validate.min.css" />
<script type="text/javascript" src="${ctxStatic}/js/little_logo.js"></script>
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
	show('${flag}');
	//可输入select
	$(".selectinput" ).comboboxSingle();
	$("#sysCountryId" ).comboboxInquiry();
	$("#agentinfoId" ).comboboxInquiry();
	$("#createBy" ).comboboxInquiry();

	<%--var ids = "";--%>
	<%--var names = "";--%>
	<%--<c:forEach items="${fns:findCurrencyList()}" var="currency" varStatus="s">--%>
		<%--&lt;%&ndash;ids += "${currency.id}" + ",";&ndash;%&gt;--%>
		<%--&lt;%&ndash;name += "${currency.name}" + ",";&ndash;%&gt;--%>
		<%--ids = ids + "${currency.id}"+",";--%>
		<%--names = names +"${currency.name}"+",";--%>
	<%--</c:forEach>--%>

});

//收起、展开客户
//function expand(child, obj, srcActivityId, payMode_deposit, payMode_advance, payMode_full,payMode_data,payMode_guarantee,payMode_express) {
//	if($(child).is(":hidden")){
//			$(obj).html("收起客户");
//			$(obj).parents('tr').addClass('tr-hover');
////			$(child).show();
//			$(obj).parents("td").addClass("td-extend");
//
//	}else{
//		if(!$(child).is(":hidden")){
//			$(obj).parents('tr').removeClass('tr-hover');
//			$(child).hide();
//			$(obj).parents("td").removeClass("td-extend");
//			$(obj).html("展开客户");
//		}
//	}
//}				

function show(flag){
 	if("zhankai"== flag){
		document.getElementById("showFlag").value="zhankai";
		if($('.ydxbd').is(":hidden")==true) {
			document.getElementById("showFlag").value="zhankai";
			$('.ydxbd').show();
			$('.zksx').text('收起筛选');
			$('.zksx').addClass('zksx-on');
		}
	}
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
}
//获取当前时间
function getCurDate(){
	var curDate = new Date();
	return curDate.getFullYear()+"-"+(curDate.getMonth()+1)+"-"+(curDate.getDate()+1);
}

function showTraveler(){
	 $("#showList").val("youke");
	 
	 $("#pageNo").val(1);
	 $("#pageSize").val(10);
	 $("#searchForm").submit();
	}
function page(n,s){
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").attr("action","${ctx}/visa/order/visaBatchEditListNew");
	$("#searchForm").submit();
}

//排序
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
	    $("#searchForm").attr("action","${ctx}/visa/order/visaBatchEditListNew");
	    $("#youkeOrderBy").val(sortBy);
	    $("#searchForm").submit();
}


//保留金额value的小数点后num位，不足补零
function formatnumber(value, num) {  
	var a, b, c, i;  
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
function jbox_jyj_qianwu(visaId,travelerId,yingshouId) {
	var html = '<div style="margin-top:20px; padding-left:20px;">';
	html += '<p><label class="jbox-label">选择币种：</label><select id="currencyId" class="jbox-width100"><c:forEach items="${currencyList}" var="currency"><option value ="${currency.id}" <c:if test="${currency.currencyName eq \"人民币\"}">selected</c:if>>${currency.currencyName}</option></c:forEach></select></p><label class="jbox-label">押金金额：</label><input id="yingshouyajin" type="text" class="jbox-width100" value=""> ';
	html += '</div>';
	$.jBox(html, { title: "确定需交押金？",buttons:{'需交押金': 1,'取消':0}, submit:function(v, h, f){
		if (v=="1"){
		var value = document.getElementById("yingshouyajin").value;
		 value = formatnumber(value, 2);//保留2位小数，不足补零	
        var currencyId = document.getElementById("currencyId").value;
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
}
//展开筛选按钮
//UG_V2 统一调用common.js里的launch()
//function launch(){
//	$('.zksx').click(function() {
//		if($('.ydxbd').is(":hidden")==true) {
//			document.getElementById("showFlag").value="zhankai";
//			$('.ydxbd').show();
//			$('.zksx').text('收起筛选');
//			$('.zksx').addClass('zksx-on');
//		}else{
//			document.getElementById("showFlag").value="shouqi";
//			$('.ydxbd').hide();
//			$('.zksx').text('展开筛选');
//			$('.zksx').removeClass('zksx-on');
//		}
//	});
//}

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

	//批量操作护照状态
	function batchUpdatePassportStatus(orderId){
		//标志位 判断是否有选中
		var travelerIds ="";
		var visaIds ="";
		//游客界面
		if(orderId==null){
			$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}else{
			$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}
		if(travelerIds==""){
			top.$.jBox.tip('请选择游客！');
			return;
		}
		
		travelerIds = travelerIds;
		batchUpdatePassportStatus1(travelerIds);
	}
			
		function batchUpdatePassportStatus1(travellerIds){
		var html = '<div style="margin-top:20px;padding:0 10px" id="batchUpdatePassportStatus">';
		html += '<label class="jbox-label">选择护照状态：</label>';
		html += '<select id="passportStatus" name="passportStatus">';
		html += '<option value="0" selected="selected">请选择</option>';
		html += '<option value="1">借出</option>';
		html += '<option value="2">销售已领取</option>';
		html += '<option value="4">已还</option>';
		html += '<option value="5">已取出</option>';
		html += '<option value="6">未取出</option>';
		html += '<option value="8">计调领取</option>';
		html += '</select>';
		html += '</div>';

		$.jBox(html, { title: "批量操作护照状态",buttons:{'提交': 1,'取消':0}, submit:function(v, h, f){
			if (v=="1"){
				var passportStatus=$("#batchUpdatePassportStatus").find("select[id=passportStatus]").val();
				$.ajax({
					cache: true,
					type: "POST",
					url:g_context_url+ "/visa/order/batchUpdatePassportStatus",
					data:{ 
						"passportStatus":passportStatus,
						"travellerIds":travellerIds
					},
						async: false,
						success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
						}else{
							top.$.jBox.tip("操作成功",'warning');
							showTraveler();
						}
					}
				});
				return true;
			}
		},height:160,width:420});
	}
	
	//批量借款 orderId:订单号
	function batchJk(orderId){
		//标志位 判断是否有选中
		var travelerIds ="";
		var visaIds ="";
		//游客界面
		if(orderId==null){
			$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var travelerName = $(this).attr("travelerName");
						travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}else{
			$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var travelerName = $(this).attr("travelerName");
					var passPortStatus1 = $("#hidden_passportStatus_"+travelerId).val();
					travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}
		if(travelerIds==""){
			top.$.jBox.tip("请选择游客！");
			return;
		}
		$.ajax({
				cache: true,
				type: "POST",
				url:g_context_url+ "/visa/order/checkBatchJkHqx",
				data:{
					"orderId":orderId,
					"travelerIds":travelerIds,
					"visaIds":visaIds
					},
				async: false,
				success: function(msg){
					if(msg.msg!=null&&msg.msg!=""){
						top.$.jBox.tip(msg.msg,'warning');
					}else{
						batchJk1(msg);
						lendPriceInputTip("employee");
					}
				}
		});
	}
	
	
	function lendPriceInputTip(productType){
		$('.t-type-jbox').on("mouseover",".huanjia",function(){
			
			$(this).addClass("huanjia-hover").find('dt input'); // .attr('defaultValue');
			$(this).find('dd').show();
			//alert("mouseover");
			
		}).on("mouseout",".huanjia",function(){
			
			$(this).removeClass("huanjia-hover").find('dd').hide();
			//alert("mouseout");
		}).on("click","[flag=appAll]",function(){
		
			var lendvalue = $(this).parents(".huanjia").find("[name='lendPrice']").val();
			
			$("[name='lendPrice']").each(function(){
				$(this).parents(".huanjia").find("[name='lendPrice']").val(lendvalue);
			});
			//alert("appAll");
			//totalRefund();
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='lendPrice']").val("");
			//totalRefund();
			//alert("reset");
		//-------0065&0099需求---djw--satrt---------------------------------------------------------------
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='lendPrice']").val("");
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='interviewPlace']").val("");
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='interviewTime']").val("");
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='explainationTime']").val("");
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='contactMan']").val("");
		}).on("click","[flag=reset]",function(){
			
			$(this).parents(".huanjia").find("[name='contactWay']").val("");
		}).on("click","[flag=appAll0]",function(){      //预约地点
			var value0 = $(this).parents(".huanjia").find("[name='interviewPlace']").val();
			
			$("[name='interviewPlace']").each(function(){
				$(this).parents(".huanjia").find("[name='interviewPlace']").val(value0);	
			});
		
		}).on("click","[flag=appAll1]",function(){		//约签时间
			var value0 = $(this).parents(".huanjia").find("[name='interviewTime']").val();
			
			$("[name='interviewTime']").each(function(){
				$(this).parents(".huanjia").find("[name='interviewTime']").val(value0);	
			});
		
		}).on("click","[flag=appAll2]",function(){      //说明会时间
			var value0 = $(this).parents(".huanjia").find("[name='explainationTime']").val();
			
			$("[name='explainationTime']").each(function(){
				$(this).parents(".huanjia").find("[name='explainationTime']").val(value0);	
			});
		
		}).on("click","[flag=appAll3]",function(){		//联系人
			var value0 = $(this).parents(".huanjia").find("[name='contactMan']").val();
			
			$("[name='contactMan']").each(function(){
				$(this).parents(".huanjia").find("[name='contactMan']").val(value0);	
			});
		
		}).on("click","[flag=appAll4]",function(){		//联系方式
			var value0 = $(this).parents(".huanjia").find("[name='contactWay']").val();
			
			$("[name='contactWay']").each(function(){
				$(this).parents(".huanjia").find("[name='contactWay']").val(value0);	
			});
		});
		//-------0065&0099需求---djw--end---------------------------------------------------------------

		
	}
	
	
	//批量还收据 orderId:订单号
	function batchHsj(orderId){
		//标志位 判断是否有选中
		var travelerIds ="";
		var visaIds ="";
		//游客界面
		if(orderId==null){
			$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var travelerName = $(this).attr("travelerName");
						travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}else{
			$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var travelerName = $(this).attr("travelerName");
					var passPortStatus1 = $("#hidden_passportStatus_"+travelerId).val();
					travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}
		if(travelerIds==""){
			top.$.jBox.tip("请选择游客！");
			return;
		}
		$.ajax({
				cache: true,
				type: "POST",
				url:g_context_url+ "/visa/order/checkBatchHsjHqx",
				data:{
					"orderId":orderId,
					"travelerIds":travelerIds,
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
			html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="8%">签证费用</th><th width="12%">借款日期</th><th width="10%">借款人</th><th width="12%">借款金额</th><th width="8%">借款状态</th><th width="10%">备注</th></tr></thead><tbody>';
			for(var i = 0,len = rightList.length; i<len; i++){
				html += '<tr><td><input visaId="'+rightList[i].visaId+'" travelerId ="'+rightList[i].tid+'" orderId ="'+rightList[i].orderId+'" travelerName="'+rightList[i].tname+'" type="checkbox" disabled="disabled" checked="checked"/>'+rightList[i].tname+'</td>';
				html += '<td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'" disabled/></td>';
				html +='<td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" disabled /></td>';
				html +='<td><dl class="huanjia"><dt><input type="text" onkeyup="clearNoNum(this)" id="borrowWinMoney_'+rightList[i].visaId+'" defaultvalue="" value="" name="lendPrice"/></dt>';
				html +='<dd ><div class="ydbz_x" flag="appAll" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
				html +='<td>'+rightList[i].jiekuanStatus+'</td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
			}
			html += '</tbody></table>';
			html += '</div>';
			$.jBox(html, { title: "批量借款",buttons:{'保存': 1,'提交': 2,'取消':0}, submit:function(v, h, f){
			if (v!="0"){
				var travelerIds="";
				var visaIds="";
				var dates="";
				var persons="";
				var others ="";
				var moneys="";
				var t = true;
				var orderIds="";
				$("#borrowWin").find("input[type=checkbox]").each(function(){
					if($(this).attr("checked")){
						var travelerId = $(this).attr("travelerId");
						var visaId = $(this).attr("visaId");
						var orderId= $(this).attr("orderId");
						var name= $(this).attr("travelerName");
						travelerIds+=travelerId+",";
						visaIds+=visaId+",";
						orderIds+=orderId+",";
						if($("#borrowWinDate_"+visaId).val()==null||$("#borrowWinDate_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款日期不能为空",'warning');
							t=  false;
						}
						dates+=$("#borrowWinDate_"+visaId).val()+",";
						if($("#borrowWinPerson_"+visaId).val()==null||$("#borrowWinPerson_"+visaId).val()==""){
								top.$.jBox.tip(name+"对应的借款人不能为空",'warning');
								t= false;
							}
						persons+=$("#borrowWinPerson_"+visaId).val()+",";
						
						if($("#borrowWinMoney_"+visaId).val()==null||$("#borrowWinMoney_"+visaId).val()==""){
								top.$.jBox.tip(name+"对应的借款金额不能为空",'warning');
								t= false;
							}
						moneys+=$("#borrowWinMoney_"+visaId).val()+",";
						others+=$("#borrowWinOther_"+visaId).val()+" "+",";
					}
				});
				
				if(travelerIds==""){
					top.$.jBox.tip("请勾选对应的游客",'warning');
					return false;
				}
				
				if(!t){
					return false;
				}
				$.ajax({
						cache: true,
						type: "POST",
						url:g_context_url+ "/visa/order/batchJk",
						data:{
							"type":v,
							"travelerIds":travelerIds,
							"visaIds":visaIds,
							"orderIds":orderIds,
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
								
								var travellArray = travelerIds.split(",");
								for(var i =0;i<travellArray.length;i++){
									 var travelerId = travellArray[i];
									 if(travelerId!=null&&travelerId!=""){
										$("#hidden_passportStatus_"+travelerId).val(1);
										 $("#passportStatus_"+travelerId+" option").eq(0).attr("selected",true);
									 }
								}
								
								if (v=="2") {
									$("#searchForm").submit();
								}
							}
						}
					});
				return true;
			}
			},height:'auto',width:800});
		}
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
				for(var i = 0,len = rightList.length; i<len; i++){
					html += '<tr><td><input visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'"  trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].passportStatusDes+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" /></td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
				}
				html += '</tbody></table>';
				html += '</div>';
				$.jBox(html, { title: "批量借护照",buttons:{'保存': 1,'提交':2,'取消':0}, submit:function(v, h, f){
				if (v!="0"){
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
								passportOperateRemark:others,
								deflag:v},                
						    	async: false,                 
							   success: function(msg){
								 if(msg.msg!=null&&msg.msg!=""){
									 top.$.jBox.tip(msg.msg,'warning');
								 }else{
									top.$.jBox.tip("操作成功",'warning');
									
									var travellArray = travellerIds.split(",");
									for(var i =0;i<travellArray.length;i++){
										 var travelerId = travellArray[i];
										 if(v==2){
											 if(travelerId!=null&&travelerId!=""){
													$("#hidden_passportStatus_"+travelerId).val(1);
													$("#passportStatus_"+travelerId+" option").eq(1).attr("selected",true);
												 } 
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
				$.jBox(html, { title: "批量还护照",buttons:{'保存': 1,'提交':2,'取消':0}, submit:function(v, h, f){
				if (v!="0"){
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
								passportOperateRemark:others,
								deflag:v},                
						    	async: false,                 
							   success: function(msg){
								 if(msg.msg!=null&&msg.msg!=""){
									 top.$.jBox.tip(msg.msg,'warning');
								 }else{
									top.$.jBox.tip("操作成功",'warning');
									
									var travellArray = travellerIds.split(",");
									for(var i =0;i<travellArray.length;i++){
										 var travelerId = travellArray[i];
										 if(v==2){
											 if(travelerId!=null&&travelerId!=""){
												$("#hidden_passportStatus_"+travelerId).val(4);
												$("#passportStatus_"+travelerId+" option").eq(3).attr("selected",true);
											 } 
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
				visaIds = visaIds.substring(0,visaIds.length-1)
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
							showTraveler();	
						}
					}
				});
				return true;
			}
			},height:160,width:420});
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


//--------------activiti 环球行签证借款，相关-----------------
//批量借款 orderId:订单号
function batchJk4activiti(orderId){
	//标志位 判断是否有选中
	var travelerIds ="";
	var visaIds ="";
	//游客界面
	if(orderId==null){
		$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			if($(this).attr("checked")){
				var travelerId = $(this).attr("travelerId");
				var visaId = $(this).attr("visaId");
				var travelerName = $(this).attr("travelerName");
					travelerIds+=travelerId+",";
					visaIds+=visaId+",";
			}
		});
	}else{
		$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
			if($(this).attr("checked")){
				var travelerId = $(this).attr("travelerId");
				var visaId = $(this).attr("visaId");
				var travelerName = $(this).attr("travelerName");
				var passPortStatus1 = $("#hidden_passportStatus_"+travelerId).val();
				travelerIds+=travelerId+",";
					visaIds+=visaId+",";
			}
		});
	}
	if(travelerIds==""){
		top.$.jBox.tip("请选择游客！");
		return;
	}
	$.ajax({
			cache: true,
			type: "POST",
			url:g_context_url+ "/visa/hqx/borrowmoney/checkBatchJkHqx",
			data:{
				"orderId":orderId,
				"travelerIds":travelerIds,
				"visaIds":visaIds
				},
			async: false,
			success: function(msg){
				if(msg.msg!=null&&msg.msg!=""){
					top.$.jBox.tip(msg.msg,'warning');
				}else{
					batchJk1activiti(msg);
					lendPriceInputTip("employee");
				}
			}
	});
}

function batchJk1activiti(msg){
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
	//***270需求--添加"还款日期"项-s****//
	//TODO-270需求,临时性屏蔽,以后上线用
	  if(msg.refundDateOption=='1'){ //refundDateOption:1-必填,0-非必填
	    html+='<p><span style="color:red;">*</span><span>还款日期：<input id="refundDate4BatchJK" value="" onclick="WdatePicker()" name="refundDate4BatchJK" type="text" style="width:100px;" /> </span></p>';
	 }else{
		 html+='<p><span>还款日期：<input id="refundDate4BatchJK" value="" onclick="WdatePicker()" name="refundDate4BatchJK" type="text" style="width:100px;" /> </span></p>'; 
	 } 
	//***270需求--添加"还款日期"项-e****//
	if(rightList.length==0){
		html += ' (无)';
		html += '</div>';
		$.jBox(html, { title: "批量借款",buttons:{'取消':0}, submit:function(v, h, f){
		},height:'auto',width:680});
	}else{
		html +='<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="8%">游客</th><th  width="10%">护照号</th><th width="8%">签证费用</th><th width="12%">借款日期</th><th width="10%">借款人</th><th width="12%">借款金额</th><th width="8%">借款状态</th><th width="10%">备注</th></tr></thead><tbody>';
		for(var i = 0,len = rightList.length; i<len; i++){
			html += '<tr><td><input visaId="'+rightList[i].visaId+'" travelerId ="'+rightList[i].tid+'" orderId ="'+rightList[i].orderId+'" travelerName="'+rightList[i].tname+'" type="checkbox" disabled="disabled" checked="checked"/>'+rightList[i].tname+'</td>';
			html += '<td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td><input type="text" onclick="WdatePicker()" id="borrowWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'" disabled/></td>';
			html +='<td><input type="text"  id="borrowWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].borrowPerson+'" disabled /></td>';
			html +='<td><dl class="huanjia"><dt><input type="text" onkeyup="clearNoNum(this)" id="borrowWinMoney_'+rightList[i].visaId+'" defaultvalue="" value="" name="lendPrice"/></dt>';
			html +='<dd ><div class="ydbz_x" flag="appAll" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
			html +='<td>'+rightList[i].jiekuanStatus+'</td><td><input id="borrowWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
		}
		html += '</tbody></table>';
		html += '</div>';
		$.jBox(html, { title: "批量借款",buttons:{'保存': 1,'提交': 2,'取消':0}, submit:function(v, h, f){
		if (v!="0"){
			var travelerIds="";
			var visaIds="";
			var dates="";
			var persons="";
			var others ="";
			var moneys="";
			var t = true;
			var orderIds="";
			//***270需求--添加"还款日期"项-s****//
			 //获得选中的日期的值
			 //TODO-270需求临时性
		    var refundDate=$("#refundDate4BatchJK").val();
			 //关于还款日期是否需要校验,规则:必填配置为1且还款日期为空时
		    if((msg.refundDateOption=='1')&&(refundDate==null||refundDate=="")){
				 top.$.jBox.tip("还款日期为必填项,不能为空!",'error');
				 return false;
			 }
			//***270需求--添加"还款日期"项-e****//
			还款日期的配置:
			$("#borrowWin").find("input[type=checkbox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var orderId= $(this).attr("orderId");
					var name= $(this).attr("travelerName");
					travelerIds+=travelerId+",";
					visaIds+=visaId+",";
					orderIds+=orderId+",";
					if($("#borrowWinDate_"+visaId).val()==null||$("#borrowWinDate_"+visaId).val()==""){
						top.$.jBox.tip(name+"对应的借款日期不能为空",'warning');
						t=  false;
					}
					dates+=$("#borrowWinDate_"+visaId).val()+",";
					if($("#borrowWinPerson_"+visaId).val()==null||$("#borrowWinPerson_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款人不能为空",'warning');
							t= false;
						}
					persons+=$("#borrowWinPerson_"+visaId).val()+",";
					
					if($("#borrowWinMoney_"+visaId).val()==null||$("#borrowWinMoney_"+visaId).val()==""){
							top.$.jBox.tip(name+"对应的借款金额不能为空",'warning');
							t= false;
						}
					moneys+=$("#borrowWinMoney_"+visaId).val()+",";
					others+=$("#borrowWinOther_"+visaId).val()+" "+",";
				}
			});
			
			if(travelerIds==""){
				top.$.jBox.tip("请勾选对应的游客",'warning');
				return false;
			}
			
			if(!t){
				return false;
			}
			$.ajax({
					cache: true,
					type: "POST",
					url:g_context_url+ "/visa/hqx/borrowmoney/batchJk4activiti",
					data:{
						"type":v,
						"travelerIds":travelerIds,
						"visaIds":visaIds,
						"orderIds":orderIds,
						"passportOperator":persons,
						"passportOperateTime":dates,
						"moneys":moneys,
						"passportOperateRemark":others,
					    "refundDate":refundDate},//270-新增还款日期-TODO-临时性屏蔽 */
						"async": false,
						success: function(msg){
						if(msg.msg!=null&&msg.msg!=""){
							top.$.jBox.tip(msg.msg,'warning');
							return false;
						}else{
							top.$.jBox.tip("操作成功!",'warning');
							
							var travellArray = travelerIds.split(",");
							for(var i =0;i<travellArray.length;i++){
								 var travelerId = travellArray[i];
								 if(travelerId!=null&&travelerId!=""){
									$("#hidden_passportStatus_"+travelerId).val(1);
									 $("#passportStatus_"+travelerId+" option").eq(0).attr("selected",true);
								 }
							}
							
							if (v=="2") {
								$("#searchForm").submit();
							}
						}
					}
				});
			return true;
		}
		},height:'auto',width:800});
	}
}

// --------------- 环球行  还 签证收据相关  ----------------
	//批量还收据 orderId:订单号
	function batchHsj4activiti(orderId){
		//标志位 判断是否有选中
		var travelerIds ="";
		var visaIds ="";
		//游客界面
		if(orderId==null){
			$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var travelerName = $(this).attr("travelerName");
						travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}else{
			$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					var travelerName = $(this).attr("travelerName");
					var passPortStatus1 = $("#hidden_passportStatus_"+travelerId).val();
					travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		}
		if(travelerIds==""){
			top.$.jBox.tip("请选择游客！");
			return;
		}
		$.ajax({
				cache: true,
				type: "POST",
				url:g_context_url+ "/visa/hqx/returnvisareceipt/checkBatchHsjHqx",
				data:{
					"orderId":orderId,
					"travelerIds":travelerIds,
					"visaIds":visaIds
					},
				async: false,
				success: function(msg){
					if(msg.msg!=null&&msg.msg!=""){
						top.$.jBox.tip(msg.msg,'warning');
					}else{
						batchHsj14activiti(msg);
					}
				}
		});
	}
	
	
	function batchHsj14activiti(msg){
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
				html += '<tr><td><input borrowamount="'+rightList[i].jiekuanJe+'" visaId="'+rightList[i].visaId+'" trallerId ="'+rightList[i].tid+'" orderId ="'+rightList[i].orderId+'" trallerName="'+rightList[i].tname+'" type="checkbox" checked="checked" disabled="disabled"/>'+rightList[i].tname+'</td><td>'+rightList[i].passportCode+'</td><td>'+rightList[i].visaFee+'</td><td>'+rightList[i].jiekuanStatus+'</td><td>¥'+rightList[i].jiekuanJe+'</td><td><input type="text" onkeyup="clearNoNum(this)" id="returnReceiptWinJe_'+rightList[i].visaId+'" class="rmb inputTxt" value="'+rightList[i].jiekuanJe+'"/></td><td><input type="text" disabled="disabled" onclick="WdatePicker()" id="returnReceiptWinDate_'+rightList[i].visaId+'" value="'+rightList[i].curDate+'"/></td><td><input type="text" disabled="disabled" id="returnReceiptWinPerson_'+rightList[i].visaId+'" value="'+rightList[i].returnReceiptPerson+'" /></td><td><input id="returnReceiptWinOther_'+rightList[i].visaId+'" type="text" /></td></tr>';
			}
			html += '</tbody></table>';
			html += '</div>';
			$.jBox(html, { title: "批量还收据",buttons:{'保存': 1,'提交': 2,'取消':0}, submit:function(v, h, f){
			if (v!="0"){
				var travellerIds="";
				var borrowamounts="";
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
						var borrowamount= $(this).attr("borrowamount");
						
						travellerIds+=trallerId+",";
						visaIds+=visaId+",";
						borrowamounts+=borrowamount+",";
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
					url:"${ctx}/visa/hqx/returnvisareceipt/batchHsj4activiti",
					data:{
						"type":v,
						"borrowamounts":borrowamounts,
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
							
							var travellArray = travellerIds.split(",");
							for(var i =0;i<travellArray.length;i++){
								 var travelerId = travellArray[i];
								 if(travelerId!=null&&travelerId!=""){
									$("#hidden_passportStatus_"+travelerId).val(1);
									 $("#passportStatus_"+travelerId+" option").eq(0).attr("selected",true);
								 }
							}
							
							if (v=="2") {
								$("#searchForm").submit();
							}
						}
					}
				});
				return true;
			}
			},height:'auto',width:700});
		}
	}

//66 批量设置担保类型 START
function batchSetGuarantee(){
	//为弹窗取值并赋值
	var checkedObj=[];
	$("#tourDetailList").find("tr").each(function(){
		var _jQueryTarget=$(this).find("td.unuseless");
			if($(this).find("td").first().find("span>input").first().is(':checked')){
			var _tempArr=[];
			_jQueryTarget.each(function(){
				if($(this).find("span").length>0){
					_tempArr.push($(this).get(0).innerText);
				}else{
//					$(this).find("select").val();
//					jQuery("#select1  option:selected").text();
					_tempArr.push($(this).find("select option:selected").val());
				}
				/*if($(this).is(".p0.unuseless")){
				 _tempArr.push($(this).find("input:hidden").attr("data-value"));
				 }*/
			})
			if(_jQueryTarget.first().is(".p0.unuseless")){
				_tempArr.push($(this).find("input:hidden").attr("data-value"));
				_tempArr.push($(this).find("input:hidden").attr("data-id"));
				_tempArr.push($(this).find("input:hidden").attr("data-orderId"));
			}
			checkedObj.push(_tempArr);
		}
	});
	var dataTravalId=[];
	var forUpdaeOrder=[];
	var setHtmlIn="";
	for(var i= 0,j=checkedObj.length;i<j;i++){
		var _attr=checkedObj[i];
		if(_attr){
			setHtmlIn+='<tr class="data">';
			var _tempId=_attr[5]+","+_attr[7];
			setHtmlIn+='<td id="'+_attr[5]+'" visaId="'+_attr[6]+'" traAndOrderId="'+_tempId+'">'+_attr[0]+'</td>';
			setHtmlIn+='<td>'+_attr[1]+'</td>';
			setHtmlIn+='<td>'+_attr[2]+'</td>';
			setHtmlIn+='<td>'+_attr[3]+'</td>';
			var t1 = _attr[4]==1?"selected='selected'":"";
			var t2 = _attr[4]==2?"selected='selected'":"";
			var t3 = (_attr[4]==3||_attr[4]=="0")?"selected='selected'":""
			var t4 = _attr[4]==4?"selected='selected'":"";
			setHtmlIn+='<td><select class="selectGuarantee jbox-width100">' +
					'<option value="1" '+t1+'>担保</option>'+
					'<option value="2" '+t2+'>担保+押金</option>'+
					'<option value="3" '+t3+'>押金</option>'+
					'<option value="4" '+t4+'>无需担保</option>'+
					'</select></td>';
			setHtmlIn+='<td><select id="currencyId" class="jbox-width100" onload="check()">';
			setHtmlIn+='<c:forEach items="${currencyList}" var="currency"><option value ="${currency.id}" <c:if test="${currency.currencyName eq \"人民币\"}">selected</c:if>>${currency.currencyName}</option></c:forEach>';
			setHtmlIn+='</select></td>'+
					'<td><dl class="huanjia">'+
					'<dt>'+
					'<input id="yingshouyajin" type="text" onafterpaste="refundInput(this)" onkeyup="refundInput(this)" name="depositPrice">'+
					'</dt>'+
					'<dd style="display: none;">'+
					'<div class="ydbz_x" flag="appAll">应用全部</div>'+
					'<div class="ydbz_x gray" flag="reset">清空</div>'+
					'</dd>'+
					'</dl>'+
					'</td><td class="tc"><a href="javascript:void(0)" class="deletethis">删除</a></td>';
			setHtmlIn+='</tr>'

		}
		dataTravalId.push({"travelerId":_attr[5],"traAndOrderId":_attr[5]+","+_attr[7]});
//		forUpdaeOrder.push({"travelerId":_attr[5]+_attr[7],"travelerName":_attr[0]})

	}
	$.ajax({
		url : '${ctx}/visa/order/check',
		type : 'post',
		data : {objs:JSON.stringify(dataTravalId)},
		async : true,
		success : function(data) {
			var names="";
			var traAndOrderId="";
			if(data.result == "reviewing") {
				var unCross=data.jsonList;
				for(var _i=0;_i<unCross.length;_i++){
					names+=unCross[_i].name+",";
					traAndOrderId+=unCross[_i].traAndOrderId+";";
				}
				var $html ="<div style='margin:20px;color: red'>游客 " + names.substring(0,names.length-1) + "&nbsp;正在担保变更审批中，无法设置担保类型和金额</div>"
				$("#tip").empty();
//				$("#tip").append($html);
				//forUpdaeOrder.push({"travelerId":[_attr[5],_attr[7]],"travelerName":_attr[0]})
			}
			var traAndOrderIdArr=traAndOrderId.split(";");
			var namesArr=names.split(",");
			dataTravalId=[];
			if($("#needToSet").children().length>0) $("#needToSet").empty();
			$("#needToSet").append(setHtmlIn);

			$pop = $.jBox($("#setYajindanbao").html(), {
				title: "批量设置担保类型", buttons: {'取消': 0, '提交': 1}, submit: function (v, h, f) {
					if (v == "1") {
						var flag = true;
						$pop.find(".selectGuarantee").each(function(){
							if($(this).val()=="2" ||$(this).val()=="3"){
								if($(this).parent().next().next().find("input").val()==""){
									$.jBox.tip("有游客押金金额未填");
									flag = false;
									return false;
								}
							}
						});

						/*var traAndOrderIdArr=traAndOrderId.split(";");
						var namesArr=names.split(",");*/
//						var _x=traAndOrderIdArr.indexOf(_objTraAndOrderId);
						if(namesArr.length>0&&namesArr[0]){
							$.jBox.tip("游客 " + namesArr.join(",").slice(0,-1) +"&nbsp;正在担保变更审批中，无法设置担保类型和金额");
							flag = false;
							return false;
						}
						if(!flag) {
							return false;
						}
						var dataArr=[];
						$pop.find("tr[class='data']").each(function(){
							var dataObj={};
							dataObj["travelerId"] = $(this).children().first().attr("id");
							dataObj["visaId"] = $(this).children().first().attr("visaId");
							dataObj["guarantee"] = $(this).children().find(".selectGuarantee").val();
							dataObj["currencyId"] = $(this).children().find("#currencyId").val();
							dataObj["amount"] = $(this).children().find("#yingshouyajin").val();
							dataObj["traAndOrderId"] = $(this).children().first().attr("traAndOrderId");
							dataArr.push(dataObj);
						});

						$.ajax({
							url : '${ctx}/visa/order/batchUpdateDeposit',
							type : 'post',
							data : {objs:JSON.stringify(dataArr)},
							async : true,
							success : function(data) {
								if (data.result == "success") {
									top.$.jBox.tip("批量设置担保类型成功", "success");
									window.location.href = '${ctx}/visa/order/visaBatchEditListNew';
								} else if(data.result == "reviewing") {
									var $html ="<div style='margin:20px'>游客 " + data.names + "正在担保变更审批中，无法设置担保类型和金额</div>"
									// $.jBox.confirm("此游客正在担保变更审批中，无法修改押金金额。", "提示",{buttons:{'确定':false}});

									$.jBox($html,{
										title:"提示",
										width:240,
										height:160,
										buttons:{'确定':false}
									});
									return false;
								} else {
									top.$.jBox.tip(data.msg, "warning");
								}
							}
						});

					}
				}, height: 'auto', width: 900
			});
//			$pop.find("div['class=jbox-button']").attr('disabled',"true");

//			$pop.bind("mousedown",function(e){
//				e=event||window.event;
//				if($(e.target).text()&&$(e.target).text()=="清除"){
//					delThis(e.target);
//				}else if($(e.target).text()&&$(e.target).text()=="应用全部"){
//					useAll(e.target);
//				}
//				else{
//					$pop.find(".delanduseall").hide();
//				}
//
//				/*	if(e.target){
//
//				 }
//				 */
//			})
			//  点击复制按钮
			$pop.find("#btn_copy").click(function(){
				var value =$pop.find(".selectGuarantee:first").val();
				$pop.find(".selectGuarantee").each(function(){
					$(this).find("option").filter('option[value='+value+']').attr("selected","selected");
					if($pop.find(".selectGuarantee").val()=="1" || $pop.find(".selectGuarantee").val()=="4"){
						$(this).parent().next().find("select").attr("style","width:10%; display:none");
						$(this).parent().next().next().find("input").attr("style","width:10%; display:none");
					}else{
						$(this).parent().next().find("select").removeAttr("style","width:10%;");
						$(this).parent().next().next().find("input").removeAttr("style","width:10%;");
					}
				});
			});

			//选中担保时金额等置灰
			$pop.find(".selectGuarantee").change(function(){
				if($(this).val()=="1" || $(this).val()=="4"){
					$(this).parent().next().find("select").attr("style","width:10%; display:none");
					$(this).parent().next().next().find("input").attr("style","width:10%; display:none");
				}else{
					$(this).parent().next().find("select").removeAttr("style","width:10%;");
					$(this).parent().next().next().find("input").removeAttr("style","width:10%;");
				}
			});

			//选中担保时金额等置灰
			$pop.find(".selectGuarantee").each(function(){
				if($(this).val()=="1" || $(this).val()=="4"){
					$(this).parent().next().find("select").attr("style","width:10%; display:none");
					$(this).parent().next().next().find("input").attr("style","width:10%; display:none");
				}else{
					$(this).parent().next().find("select").removeAttr("style","width:10%;");
					$(this).parent().next().next().find("input").removeAttr("style","width:10%;");
				}
			});

			//删除
			/**
			 * 当选中的游客信息不符合担保类型的时候，进行删除，去掉提示中对应的名字
			 */
			$pop.find(".deletethis").click(function(){
				$deleteObj=$(this).parent().parent();

//				$(this).parent().parent().remove();
				var _objId=$deleteObj.find("td:eq(0)").attr("id");
				var _objTraAndOrderId=$deleteObj.find("td:eq(0)").attr("traAndOrderId");
				var _objName=$deleteObj.find("td:eq(0)").text();
				/*var traAndOrderIdArr=traAndOrderId.split(";");
				var namesArr=names.split(",");*/
				var _x=traAndOrderIdArr.indexOf(_objTraAndOrderId);
				if(_x>-1){
					namesArr.splice(_x,1);
					//此时更新html
					var _updateObj=$(this).parent().parent().parent().parent().next();
					_updateObj.empty();
					if(namesArr.length>0&&namesArr[0]){
						var $html ="<div style='margin:20px;background-color: red'>游客 " + namesArr.join(",") + "正在担保变更审批中，无法设置担保类型和金额</div>"
						_updateObj.append($html);
					}else{
						$(this).parent().parent().parent().parent().parent().parent().parent().next().find("button:eq(1)").removeAttr("disabled");
					}

				}
				$(this).parent().parent().remove();
			});

			//160719--应用全部统一样式 START
			$pop.on("mouseover",".huanjia",function(){
				$(this).addClass("huanjia-hover").find("dt input");
				$(this).find("dd").show();
			}).on("mouseout",".huanjia",function(){
				$(this).removeClass("huanjia-hover").find("dd").hide();
			}).on("click","[flag=appAll]",function(){
				var value = $(this).parents(".huanjia").find("[name='depositPrice']").val();
				$("[name='depositPrice']").each(function(){
					$(this).val(value);
				});
			}).on("click","[flag=reset]",function(){
				$(this).parents(".huanjia").find("[name='depositPrice']").val("");
			});
			//160719--应用全部统一样式 END
		}
	});



}
//66 批量设置担保类型 END
	//-----0065&0099需求---addby djw start-------------------------------------------------------------------------------------------------
	//批量设置面签通知
	function batchSetingInterviewNotice(){
		//标志位  判断是否选中
		var travelerIds = "";
		var visaIds = "";
		
		//游客界面
		//if(orderId == null){
			$("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					travelerIds = travelerIds + travelerId + ",";
					visaIds = visaIds + visaId + ",";
				}
			});
			
		/* }else{
			$("#child_"+orderId).find("input[type=checkbox][type=checkbox][class=tdCheckBox]").each(function(){
				if($(this).attr("checked")){
					var travelerId = $(this).attr("travelerId");
					var visaId = $(this).attr("visaId");
					travelerIds+=travelerId+",";
						visaIds+=visaId+",";
				}
			});
		} */
		
		if(travelerIds==""){
			top.$.jBox.tip('请选择游客！');
			return;
		}
		
		$.ajax({
			cache: true,
			type: "POST",
			url:g_context_url+ "/visa/order/batchSetingIN",
			data:{
				//"orderId":orderId,
				"travelerIds":travelerIds,
				"visaIds":visaIds
				},
			async: false,
			success: function(msg){
				if(msg.msg!=null&&msg.msg!=""){
					top.$.jBox.tip(msg.msg,'warning');
				}else{
					batchSetInterviewNotice1(msg);
					lendPriceInputTip("employee");
				}
			}
		}); 
		
		
		
		
		//batchSetInterviewNotice1(travelerIds);
	}
	function batchSetInterviewNotice1(msg){
		var rightList =msg.rightList;
		//var errList =msg.errList;
		var html = '<div>';
		html += '<table class="activitylist_bodyer_table t-type-jbox"><thead><tr><th width="7%">游客</th><th  width="9%">销售</th><th width="12%">预约地点</th><th width="12%"><span class="xing">*</span>约签时间</th><th width="12%">说明会时间</th><th width="8%">联系人</th><th width="12%">联系方式</th><th width="6%">操作</th></tr></thead><tbody>';
		for(var i = 0,len = rightList.length; i<len; i++){
			//游客
			html += '<tr name="traveler"><td name="travelerName" visaId="'+rightList[i].visaId+'" travelerId ="'+rightList[i].tid+'" orderId ="'+rightList[i].orderId+'" travelerName="'+rightList[i].tname+'" travelerNum="'+rightList[i].travelerNum+'" visaCountryId="'+rightList[i].visaCountryId+'" collarZoning="'+rightList[i].collarZoning+'">'+rightList[i].tname+'</td></td>';
			//销售
			html += '<td name="salerName">'+rightList[i].salerName+'</td>';
			//预约地点  可输入
			html +='<td name="interviewPlace"><dl class="huanjia"><dt><input type="text" id="" defaultvalue="" value="" name="interviewPlace"/></dt>';
			html +='<dd ><div class="ydbz_x" flag="appAll0" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
			//约签时间   必填 可输入
			//html +='<td><dl class="huanjia"><dt><input type="text" onclick="WdatePicker()" id="" defaultvalue="" value="" name=""/></dt>';  // onkeyup="clearNoNum(this)"
			html +='<td name="interviewTime"><dl class="huanjia"><dt><input type="text" onclick="WdatePicker({dateFmt:'+"'yyyy-MM-dd HH:mm'"+'})" id="" defaultvalue="" value="" name="interviewTime" onchange="changeTime(this)"/></dt>';
			html +='<dd ><div class="ydbz_x" flag="appAll1" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
			//说明会时间  可输入
			html +='<td name="explainationTime"><dl class="huanjia"><dt><input type="text" onclick="WdatePicker({dateFmt:'+"'yyyy-MM-dd HH:mm'"+'})" id="" defaultvalue="" value="" name="explainationTime"/></dt>';
			html +='<dd ><div class="ydbz_x" flag="appAll2" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
			//联系人     可输入
			html +='<td name="contactMan"><dl class="huanjia"><dt><input type="text" id="" defaultvalue="" value="" name="contactMan"/></dt>'; 
			html +='<dd ><div class="ydbz_x" flag="appAll3" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
			//联系方式    可输入
			html +='<td name="contactWay"><dl class="huanjia"><dt><input type="text" id="" defaultvalue="" value="" name="contactWay"/></dt>';
			html +='<dd ><div class="ydbz_x" flag="appAll4" >应用全部</div><div class="ydbz_x gray" flag="reset">清空</div></dd></dl></td>';
			//操作
			html += '<td style="text-align:center;"><a onclick="removeThis(this)">删除</a></td></tr>';
			
		}
		html += '</tbody>';
		html += '</table>';
		html += '</div>';
			
		$.jBox(html,{title: "批量设置面签通知",buttons:{'提交':1,'取消':0}, submit:function(v,h,f){
			if(v == 1){
				if($("tr[name='traveler']").html() == null){
					top.$.jBox.tip('请选择约签人！');
				}
		var flag = 0;	
			$("input[name='interviewTime']").each(function(){
				if($(this).val() == null || $.trim($(this).val()) == ""){
					$.jBox.tip('有游客约签时间未填写！');
					flag = 1;
				}
			});		
		if(flag == 1){
			return false;
		}				
			var visaIds = "";
			var travelerIds = "";
			var orderIds = "";
			var visaCountryIds = "";
			var collarZonings = "";
			var travelerNums = "";
			var salerNames = "";
			var interviewPlaces = "";
			var interviewTimes = "";
			var explainationTimes = "";
			var contactMans  = "";
			var contactWays = "";
			$("tr[name='traveler']").each(function(){
				var visaId = $(this).find("td[name='travelerName']").attr("visaId");
				var travelerId = $(this).find("td[name='travelerName']").attr("travelerId");
				var orderId = $(this).find("td[name='travelerName']").attr("orderId"); 
				var visaCountryId = $(this).find("td[name='travelerName']").attr("visaCountryId");
				var collarZoning = $(this).find("td[name='travelerName']").attr("collarZoning");
				var travelerNum = $(this).find("td[name='travelerName']").attr("travelerNum");
				var salerName = $(this).find("td[name='salerName']").html();
				var interviewPlace = $(this).find("td[name='interviewPlace']").find("input").val();
				var interviewTime = $(this).find("td[name='interviewTime']").find("input").val();
				var explainationTime = $(this).find("td[name='explainationTime']").find("input").val();
				var contactMan = $(this).find("td[name='contactMan']").find("input").val();
				var contactWay = $(this).find("td[name='contactWay']").find("input").val();
				
				visaIds += visaId + ",";
				travelerIds += travelerId + ",";
				orderIds += orderId + ",";
				visaCountryIds += visaCountryId + ",";
				collarZonings += collarZoning + ",";
				travelerNums += travelerNum + ",";
				salerNames += salerName + ",";
				interviewPlaces += interviewPlace + ",";
				interviewTimes += interviewTime + ",";
				explainationTimes += explainationTime + ",";
				contactMans += contactMan + ",";
				contactWays += contactWay + ",";
				
			});	
				$.ajax({
					cache: true,
					 type: "post",
					  url: g_context_url+ "/visa/interviewNotice/batchDoCreate",
					 data:{
						"visaIds": visaIds,	
						"travelerIds": travelerIds,	
						"travelerNums": travelerNums,	
						"orderIds": orderIds,
						"visaCountryIds": visaCountryIds,
						"collarZonings": collarZonings,
						"salerNames": salerNames,
						"interviewPlaces": interviewPlaces,
						"interviewTimes": interviewTimes,
						"explainationTimes": explainationTimes,
						"contactMans": contactMans,
						"contactWays": contactWays
						},
					async: false,
					dataType: "json", 
					success:function(msg){
						if(msg.msg==null || msg.msg == ""){
							top.$.jBox.tip('批量设置成功！');
						}else{
							top.$.jBox.tip(msg.msg,"warning");
						} 
					}
				});
				
			}
			
		},height:550,width:900});
	}
		
	function removeThis(obj){
		debugger;
		var em = $(obj).parent().parent().remove();
		if($("tr[name='traveler']").length == 0){
			window.parent.jBox.close();
		}
		//$(obj).parents().parents()..parents()removeChild(em);	
		//return;
	}	

	function changeTime(obj){
		var value = $(obj).val();
		if(value==""){
			return;
		}
		value = value.split(" ")[0];
		//var explainationTimeVal = $("#explainationTime").val();
		$(obj).parent().parent().parent().next().find("input[name='explainationTime']").val(getYestoday(new Date(value)));
		//$("#explainationTime").val(getYestoday(new Date(value)));
	}

	function getYestoday(date){       
	    var yesterday_milliseconds=date.getTime()-1000*60*60*24;        
	    var yesterday = new Date();        
	        yesterday.setTime(yesterday_milliseconds);        
	         
	    var strYear = yesterday.getFullYear();     
	    var strDay = yesterday.getDate();     
	    var strMonth = yesterday.getMonth()+1;   
	    if(strMonth<10)     
	    {     
	        strMonth="0"+strMonth;     
	    }     
	    datastr = strYear+"-"+strMonth+"-"+strDay+" 00:00";   
	    return datastr;   
	  } 

</script>
</head>
<body>
	<!--右侧内容部分开始-->
	<div style="width: 100%" class="activitylist_bodyer_right_team_co_bgcolor">
		<form method="post" action="${ctx}/visa/order/visaBatchEditListNew" id="searchForm" name="searchForm">	  
			
			<input id="departmentId" name="departmentId" type="hidden" value="${departmentId}" />			
			<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
			<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
			<input id="youkeOrderBy" name="youkeOrderBy" type="hidden" value="${page.orderBy}"/>
					
			<input value="youke" type="hidden" id="showList" name="showList"/> 
			<input value="" type="hidden" id="showFlag" name="showFlag"/>     
			<input value="" type="hidden" id="saveOrSubmit" name="saveOrSubmit"/>
			<div class="activitylist_bodyer_right_team_co">
			<div class="activitylist_bodyer_right_team_co2 pr">
				<%--<div class="activitylist_team_co3_text">搜索：</div>--%>
				<input value="${visaOrderForm.commonCode}" class="inputTxt searchInput inputTxtlong" flag="istips" name="commonCode" id="commonCode" placeholder="请输入订单号、产品编号">
					<%--<span class="ipt-tips">订单号、产品编号</span>--%>
			</div>
			<a class="zksx">筛选</a>
			<div class="form_submit">
				<input type="submit" id="btn_search" value="搜索" onclick="" class="btn btn-primary ydbz_x">
				<input type="button" value="清空所有条件" onclick="resetSearchParams()" class="btn ydbz_x">
			</div>
							<div class="ydxbd" style="display: none;">
								<span></span>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">签证国家：</label>
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
									<label class="activitylist_team_co3_text">签证类型：</label>
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
								<!--  
								<div class="activitylist_bodyer_right_team_co1">
								</div>
								-->
								<div class="activitylist_bodyer_right_team_co1">
								<label class="activitylist_team_co3_text">渠道选择：</label>
									<select id="agentinfoId" name="agentinfoId">
										<option value="">不限</option>
										<c:choose>
											<c:when test="${ visaOrderForm.agentinfoId eq '-1' }">
												<option value="-1" selected="selected">非签约渠道</option>
											</c:when>
											<c:otherwise>
												<option value="-1">非签约渠道</option>
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
								<div class="activitylist_bodyer_right_team_co1" >
									<label class="activitylist_team_co3_text">签证状态：</label>
									<div class="selectStyle">
										<select id="visaStatus" name="visaStatus">
											<option value="">不限</option>
											<c:forEach items="${visaStatusList}" var="visaStatus">
												
													<c:choose>
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
								<!-- 
								<div class="activitylist_bodyer_right_team_co1">
									<div class="activitylist_team_co3_text">制表人：</div>
									<input name="makeTable" type="text" class="inputTxt" />
								</div>
								 -->
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
								 <div class="activitylist_team_co3_text" >下单人：</div>
									<select id="createBy" name="createBy">
										<option value="">不限</option>
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
												<option value="1" >审核中</option>
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
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">实际约签日期：</label>
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.contractStart}" name="contractStart" class="inputTxt dateinput" id="contractStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker()" value="${visaOrderForm.contractEnd}" name="contractEnd" class="inputTxt dateinput" id="contractEnd">
								</div>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">游客姓名：</label>
										<input value="${visaOrderForm.travelName}" name="travelName" class="inputTxt" id="travelName"> 
									</div>
								
<!-- 								<div class="activitylist_bodyer_right_team_co1"></div> -->
								
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

								<%--<div class="activitylist_bodyer_right_team_co1"></div><!-- 占位用 -->--%>
								<!-- 0067新增说明会时间 -->
								<%--<div class="activitylist_bodyer_right_team_co1"></div>--%>
								<div class="activitylist_bodyer_right_team_co1">
									<label class="activitylist_team_co3_text">说明会时间：</label>
									<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingStart}" name="explanationMeetingStart" class="inputTxt dateinput" id="explanationMeetingStart"> 
									<span> 至 </span>  
									<input readonly="" onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'})" value="${visaOrderForm.explanationMeetingEnd}" name="explanationMeetingEnd" class="inputTxt dateinput" id="explanationMeetingEnd">
								</div>
							</div>
							<div class="kong"></div>
						</div>
		</form>
	</div>
	<!--查询结果筛选条件排序开始-->
	<div class="activitylist_bodyer_right_team_co_paixu">
	  <div class="activitylist_paixu">
		<div class="activitylist_paixu_left">
		  <ul>
			<li style=" width:50px;border:none; background:none; height:28px; line-height:28px;">排序</li>
		    <c:choose>
												<c:when test="${page.orderBy == 're.createDate DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('re.createDate',this)">
							                    		借款日期
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${page.orderBy == 're.createDate ASC'}">
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
												<c:when test="${page.orderBy == 'vo.create_date DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('vo.create_date',this)">
							                    		创建时间
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${page.orderBy == 'vo.create_date ASC'}">
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
												<c:when test="${page.orderBy == 'vo.update_date DESC'}">
													<li class="activitylist_paixu_moren">
						                    			<a onclick="youkesortby('vo.update_date',this)">
							                    		更新时间
														<i class="icon icon-arrow-down"></i>
						                    			</a>
													</li>
												</c:when>
												<c:when test="${page.orderBy == 'vo.update_date ASC'}">
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
	<table class="activitylist_bodyer_table">
		<tbody>
			<tr class="checkalltd" height="50">
			<td colspan='15' class="tl">
				<label><input type="checkbox" name="allChk" onclick="checkall(this)">全选</label>
				<label><input type="checkbox" name="allChkNo" onclick="checkallNo(this)">反选</label>
				<a onclick="batchUpdatePassport(null,'1')">批量借护照</a><a onclick="batchUpdatePassport(null,'2')">批量还护照</a>
				<c:if test="${jieKuanFlag ==true}">
					<a onclick="batchJk4activiti(null);">批量借款</a>
				</c:if>
				<a onclick="batchHsj4activiti(null);">批量还收据</a>
				<a onclick="batchUpdateVisaStatus(null)">批量操作签证</a>
				<a onclick="batchUpdatePassportStatus(null)">批量操作护照</a>
				<!-- 0065&0099需求  addby djw start -->
				<a onclick="batchSetingInterviewNotice()">批量设置面签通知</a>
				<!-- 0065&0099需求  addby djw end -->
				<a onclick="batchSetGuarantee()">批量设置担保类型</a>
				<p class="main-right-topbutt" style=""><a href="${ctx }/visa/order/visaBatchEditRecordList" target="_blank" style="top:-28px;">批量操作记录</a></p>
			</td>
			</tr>
		</tbody>
	</table>
	<table id="contentTable" class="table activitylist_bodyer_table">
		<thead>
			<tr>
				<th width="5%">姓名</th>
				<th width="5%">护照号</th>
				<!-- <th width="7%">AA码</th> -->
				<th width="2%">签证类别</th>
				<th width="2%">签证国家</th>  
				<th width="2%">领区</th>
				<th width="7%">预计出团时间<br />实际出团时间</th>
				<th width="7%">预计约签时间<br />实际约签时间</th>
				<th width="7%">说明会时间</th>
				<th width="7%">签证状态</th>
				<th width="7%">护照状态</th>
				<th width="6%">担保类型</th>
				<th width="6%">下单人</th>
				<th width="4%">借款状态</th>
				<th width="8%">借款申请日期</th>
				<th width="7%">应收押金</th>
				<th width="7%">已收押金<br />达账押金</th>
				<th width="5%">保存<br />修改</th>	
				<th width="5%">操作</th>		
			</tr>
		</thead>
		<tbody  id="tourDetailList">
			<c:if test="${fn:length(travelList) <= 0 }">
							<tr class="toptr" >
								<td colspan="17" style="text-align: center;color:green">没有符合条件的记录</td>
							</tr>
			</c:if>
			<c:forEach items="${travelList}" var="traveler" varStatus="count">
									<tr>
										<td width="4%" class="p0 unuseless">
										<c:choose>
																<c:when test="${!empty traveler.paymentType}">
																	<div class="ycq yj" style="margin-top:1px;">
																		${traveler.paymentType}
																	</div>
																</c:when>
															</c:choose>
<span id="traveler_travelerName" class="sqcq-fj">
<input type="checkbox" class="tdCheckBox" travelerId="${traveler.id }" visaId="${traveler.visaId}" travelerName="${traveler.travelerName }" value="${traveler.id }@${traveler.visaorderId}" name="activityId" onclick=""/>${traveler.travelerName}</span>
<input type="hidden" value="${traveler.agentinfoId }" id="activityId_agentId_${traveler.id}" data-orderId="${traveler.visaorderId}" data-value="${traveler.id}" data-id="${traveler.visaId}">

												</td>
													<td width="5%"  class="unuseless">
														<span id="traveler_passportId">${traveler.passportId}</span>
													</td>
													 <!-- <td width="7%">
														<input name="" type="text" maxlength="40" id="traveler_AACode_${traveler.id }" value="${traveler.AACode}" /> 
													</td>-->
													<td width="6%">
														<span id="traveler_visaType">${traveler.visaType}</span>
													</td>
													<td width="6%"  class="unuseless">
														<span id="traveler_visaCountry">${traveler.visaCountry}</span>
													</td>
													<!-- 领区 -->
													<td width="6%"  class="unuseless">
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
													</td>
													<!-- 说明会时间 -->
													<td width="6%">
														<span id="traveler_explanationTime" style="color:green">${traveler.explanationTime}</span>
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
															<!--  
															<c:choose>
																<c:when test="${ '3' eq traveler.passportStatus}">
																	<option selected="selected" value="3">未签收</option>
																</c:when>
																<c:otherwise>
																	<option value="3">未签收</option>
																</c:otherwise>
															</c:choose>
															
															<c:choose>
																<c:when test="${ '4' eq traveler.passportStatus}">
																	<option selected="selected" value="4" >已签收</option>
																</c:when>
																<c:otherwise>
																	<option value="4" >已签收</option>
																</c:otherwise>
															</c:choose>
															-->
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
															<!--
														<c:choose>
																<c:when test="${ '7' eq traveler.passportStatus}">
																	<option selected="selected" value="7" >走团</option>
																</c:when>
																<c:otherwise>
																	<option value="7" >走团</option>
																</c:otherwise>
															</c:choose>
														-->	
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
													<td width="6%"  class="unuseless">
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

													<td width="7%" class="p0 tr" id="traveleryingshouyajin_${traveler.id}">
                                                        <c:if test="${traveler.guaranteeStatus eq 2 or traveler.guaranteeStatus eq 3}">${traveler.totalDeposit}</c:if>
													</td>
													<td width="7%" class="p0 tr">	
														<div class="yfje_dd">	
															<span class="fbold" >
														<%--<c:if test="${ '3' eq traveler.guaranteeStatus}">--%>
																${traveler.payedDeposit}
														<%--</c:if>--%>
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
															<dt><img src="${ctxStatic}/images/handle_cz.png" title="操作"></dt>
															<dd>
																<p>
																	<span></span>
																	  <c:if test="${result.lockStatus != 1 && result.activityLockStatus != 1 }">
																	  <!--  -->
																		<!-- 
																	  	<a href="javascript:void(0)" onclick="jbox_hsj_qianwu('${traveler.id}');">
																		 	还收据
																		 </a>
																		 -->
																	  </c:if>
																	 <a href="javascript:void(0)" 
																	 onclick="jbox_jkmx_qianwu('${traveler.travelerName}','${traveler.passportId}','${traveler.creatUser}','${traveler.createTime}','${traveler.groupType}','${traveler.creatUser}','${traveler.visaorderNo}','${traveler.jiekuanCreateUser}','${traveler.jiekuanBizhong}${ traveler.jiekuanAmount}','${traveler.jiekuanRemarks }');">
																	 	借款明细
																	 </a>
																	 <c:if test="${result.lockStatus != 1 && result.activityLockStatus != 1 }">
																		<c:if test="${jieKuanFlag}">
																		<!--  -->
																		<!-- 
																			<a href="javascript:void(0)" onclick="jbox_jk_qianwu('${traveler.id}');">
																				借款
																			 </a>
																			  -->
																		 </c:if>
																		 <a href="javascript:void(0)" 
																		 onclick="xiugaiyajin_qianwu('${traveler.visaId}','${traveler.totalDepositUUID }','${traveler.id }','${traveler.visaorderId}','traveleryingshouyajin${traveler.visaId}${traveler.id}${count.index}') ">
																		 	修改押金
																		 </a>
																		 	<a href="javascript:void(0)" onclick="jbox_jhz('${traveler.id}','${traveler.visaId}');">借护照</a>
																		 <a href="javascript:void(0)" onclick="jbox_hhz('${traveler.id}','${traveler.visaId}');">还护照</a>
																		 <c:if test="${tuiYaJinFlag}">
																			<a href="${ctx}/order/manager/visaDeposit/refundList?proId=${traveler.visaorderId}">退签证押金</a>
																		 </c:if>
																		 <c:if test="${huanYaJinShouJuFlag}">
																			<a href="javascript:void(0)" onclick="jbox_hyjsj('${traveler.id}');">还押金收据</a>
																		 </c:if>
																		 <a href="javascript:void(0)" onclick="jbox_qszl_qianwu('${traveler.visaProductId}','${traveler.visaId}');">签收资料</a>
																	 </c:if>
																</p>
															</dd>
														</dl>
													</td>
												</tr> 
												</c:forEach>
			
			<tr class="checkalltd"><td colspan='17' class="tl"><label><input type="checkbox" name="allChk" onclick="checkall(this)">全选</label>
				<label><input type="checkbox" name="allChkNo" onclick="checkallNo(this)">反选</label>
				<a onclick="batchUpdatePassport(null,'1')">批量借护照</a><a onclick="batchUpdatePassport(null,'2')">批量还护照</a>
				
				<c:if test="${jieKuanFlag ==true}">
					<!-- 原  批量借款 -->
					<!-- 
					<a onclick="batchJk(null);">批量借款</a>
					-->
				</c:if>
				<!-- 原  批量还收据 -->
				<!-- 
				<a onclick="batchHsj(null);">批量还收据</a>
				 -->
				
				<c:if test="${jieKuanFlag ==true}">
				    <!-- 新  批量借款 -->
					<a onclick="batchJk4activiti(null);">批量借款</a>
				</c:if>
				<!-- 新  批量还收据 -->
				<a onclick="batchHsj4activiti(null);">批量还收据</a>
				
				
				<a onclick="batchUpdateVisaStatus(null)">批量操作签证</a>
				<a onclick="batchUpdatePassportStatus(null)">批量操作护照</a>
				
				<!-- 0065&0099需求  addby djw start -->
				<a onclick="batchSetingInterviewNotice()">批量设置面签通知</a>
				<!-- 0065&0099需求  addby djw end -->
				<a onclick="batchSetGuarantee()">批量设置担保类型</a>
				
				

				
				<!-- 
				(签证借款--》<a href="${ctx}/activitytest/createModelId" target="_blank">获取流程定义Id</a>
			    <a href="${ctx}/activitytest/modelDesign?mid=120001" target="_blank">根据获取Id进行流程定义</a>
			    <a href="${ctx}/activitytest/export?modelId=120001" target="_blank">导出加部署</a>)
			    
			    (还签证收据--》
			    <a href="${ctx}/activitytest/modelDesign?mid=395001" target="_blank">根据获取Id进行流程定义</a>
			    <a href="${ctx}/activitytest/export?modelId=395001" target="_blank">导出加部署</a>)
				 -->
				</td>
			</tr>
			
			
			
		</tbody>
	</table>
	<div class="pagination clearFix">${page}</div>
	<!--右侧内容部分结束-->

	<!--66 设置/修改押金+担保金额弹窗内容 START-->
	<div id="setYajindanbao" class="hide">
		<div style="margin:20px;">
			<table id="contentTable" class="table activitylist_bodyer_table">
				<thead>
				<tr>
					<th width="5%">游客</th>
					<th width="5%">护照号</th>
					<th width="5%">签证国家</th>
					<th width="5%">签证领区</th>

					<th width="4%">担保类型<input type="button" id="btn_copy" value="复制" onclick="" class="btn btn-primary "style="margin-left:10px"></th>

					<th width="6%">币种</th>
					<th width="6%">押金金额</th>

					<th width="8%">操作</th>
				</tr>
				</thead>
				<tbody id="needToSet">

				</tbody>

			</table>
			<div id="tip"></div>
		</div>

	</div>

	<!--66 设置/修改押金+担保金额弹窗内容 END-->
</body>
</html>
